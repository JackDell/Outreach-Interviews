package com.outreach.interviews.map.builder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.outreach.interviews.map.enums.MapOperations;

public class MapGeocodeHelper {

	public static class GeocodeBuilder {

		private final String URL = "https://maps.googleapis.com/maps/api/";
		private String streetAddress; // Will hold the standard street address
		private String area; // Will hold the town/city/municipality where the street address is located
		private String division; // Will hold the province/state where the area is located
		private MapOperations operation;

		private JsonObject result; // Where our result from Google will be stored

		private CloseableHttpClient httpclient = HttpClients.createDefault();

		/**
		 * Set the starting point
		 * 
		 * @param streetAddress String containing just the street address, ex: 123 Main
		 *                      Street
		 * @return {@link GeocodeBuilder}
		 */
		public GeocodeBuilder setStreetAddress(String streetAddress) {
			this.streetAddress = streetAddress;
			return this;
		}

		/**
		 * Set the starting point
		 * 
		 * @param area String representing the town/city/municipality where the street
		 *             address is located
		 * @return {@link GeocodeBuilder}
		 */
		public GeocodeBuilder setArea(String area) {
			this.area = area;
			return this;
		}

		/**
		 * Set the starting point
		 * 
		 * @param division String representing the province/state where the area is
		 *                 located
		 * @return {@link GeocodeBuilder}
		 */
		public GeocodeBuilder setDivision(String division) {
			this.division = division;
			return this;
		}

		/**
		 * Create the URL to communicate with the Google Maps API
		 * 
		 * @param type URL to provide to Apache HttpClient
		 * @return {@link GeocodeBuilder}
		 */
		public GeocodeBuilder setURL(MapOperations type) {
			if (type.equals(MapOperations.directions))
				throw new UnsupportedOperationException();

			this.operation = type;
			return this;

		}

		/**
		 * Perform the HTTP request and retrieve the data from the HttpClient object
		 * @return {@link GeocodeBuilder}  
		 * @throws UnsupportedOperationException Thrown to indicate that the requested operation is not supported.
		 * @throws IOException Thrown to indicate that the requested operation is not supported.
		 * @throws IllegalArgumentException Thrown to indicate that a method has been passed an illegal or inappropriate argument.
		 */
		public GeocodeBuilder build() throws UnsupportedOperationException, IOException, IllegalArgumentException {
			String requestURL = this.getURL() + "address=" + this.getStreetAddress() + ",+" + this.getArea() + ",+" + this.getDivision() + "&key=" + this.getAPIKey(); 
			
			HttpGet httpGet = new HttpGet(requestURL);
			
			try(CloseableHttpResponse response = httpclient.execute(httpGet)) {
				HttpEntity entity = response.getEntity();
				String result = IOUtils.toString(entity.getContent(), "UTF-8");
				this.result = new JsonParser().parse(result).getAsJsonObject();
			}
			
			return this;
		}
		
		/**
		 * Retrieve the address components received and the latitude & longitude locations
		 * @return List of String containing the address components, followed by the latitude & longitude locations
		 */
		public List<String> getInfo() {
			if(this.operation.equals(MapOperations.geocode) && zeroResults(this.result)) {
				List<String> list = new ArrayList<String>();
				JsonArray components = this.result.get("results").getAsJsonArray().get(0).getAsJsonObject().get("address_components").getAsJsonArray();
				Iterator<JsonElement> i = components.iterator();
				
				while(i.hasNext()) {
					JsonObject component = (JsonObject) i.next();
					list.add("long name: " + component.get("long_name").getAsString() + ", short name: " + component.get("short_name").getAsString() + ", types: " + component.get("types").toString());
				}
				
				JsonObject location = this.result.get("results").getAsJsonArray().get(0).getAsJsonObject().get("geometry").getAsJsonObject().get("location").getAsJsonObject();
				
				list.add("latitude: " + location.get("lat").getAsString());
				list.add("longitude: " + location.get("lng").getAsString());
				
				return list;
			} else {
				throw new IllegalArgumentException("Does not support " + MapOperations.geocode.name());
			}
		}
		
		// *************************For Internal Use Only***********************************//
		private final String getURL() {
			if(this.operation == null)
				throw new IllegalArgumentException("Operation must be specified (directions/geocode)");
			
			return this.URL + this.operation.name() + "/json?";
		}

		private final String getAPIKey() {
			return System.getenv("OUTREACH_MAPS_KEY");
		}

		private final String getStreetAddress() {
			if (this.streetAddress == null)
				throw new IllegalArgumentException("Street address cannot be empty");

			return this.streetAddress.replace(' ', '+');
		}

		private final String getArea() {
			if (this.area == null)
				throw new IllegalArgumentException("Area cannot be empty");

			return this.area.replace(' ', '+');
		}

		private final String getDivision() {
			if (this.division == null)
				throw new IllegalArgumentException("Division cannot be empty");

			return this.division.replace(' ', '+');
		}

		private final boolean zeroResults(JsonObject obj) {
			return !obj.get("status").getAsString().equals("ZERO_RESULTS");
		}
	}
}
