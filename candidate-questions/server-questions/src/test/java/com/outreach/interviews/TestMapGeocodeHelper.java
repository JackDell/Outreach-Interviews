package com.outreach.interviews;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

import com.outreach.interviews.map.builder.MapGeocodeHelper;
import com.outreach.interviews.map.enums.MapOperations;

public class TestMapGeocodeHelper {

	// Change to false if you don't want the info obtained from the Google API to be printed
	private final boolean printGeocodeInfo = true;
	
	@Test
	public void testMapGeocodeHelperApiKey1() throws UnsupportedOperationException, IOException {
		new MapGeocodeHelper.GeocodeBuilder()
			.setStreetAddress("94 Auburn Lane")
			.setArea("Courtice")
			.setDivision("Ontario")
			.setURL(MapOperations.geocode)
			.build();
	}
	
	@Test
	public void testMapGeocodeHelperApiKey2() throws UnsupportedOperationException, IOException {
		List<String> info = new MapGeocodeHelper.GeocodeBuilder()
			.setStreetAddress("94 Auburn Lane")
			.setArea("Courtice")
			.setDivision("Ontario")
			.setURL(MapOperations.geocode)
			.build()
			.getInfo();
		
		assertNotNull(info);
		assertTrue(info.size() == 10);
		
		if(printGeocodeInfo) {
			for(String s : info) {
				System.out.println(s);
			}
		}
	}
	
	@Test
	public void testMapGeocodeHelperApiKey3() throws UnsupportedOperationException, IOException {
		List<String> info = new MapGeocodeHelper.GeocodeBuilder()
			.setStreetAddress("227 Daly Ave")
			.setArea("Ottawa")
			.setDivision("Ontario")
			.setURL(MapOperations.geocode)
			.build()
			.getInfo();
		
		assertNotNull(info);
		
		if(printGeocodeInfo) {
			for(String s : info) {
				System.out.println(s);
			}
		}
	}
	
	@Test(expected = java.lang.UnsupportedOperationException.class)
	public void testMapGeocodeHelperApiKey4() throws UnsupportedOperationException, IOException {
		List<String> info = new MapGeocodeHelper.GeocodeBuilder()
			.setStreetAddress("94 Auburn Lane")
			.setArea("Courtice")
			.setDivision("Ontario")
			.setURL(MapOperations.directions)
			.build()
			.getInfo();
		
		assertNotNull(info);
	}
	
	@Test(expected = java.lang.IllegalArgumentException.class)
	public void testMapGeocodeHelperApiKey5() throws UnsupportedOperationException, IOException {
		List<String> info = new MapGeocodeHelper.GeocodeBuilder()
			.setStreetAddress("94 Auburn Lane")
			.setArea("Courtice")
			.setURL(MapOperations.geocode)
			.build()
			.getInfo();
	}
	
	@Test(expected = java.lang.IllegalArgumentException.class)
	public void testMapGeocodeHelperApiKey6() throws UnsupportedOperationException, IOException {
		List<String> info = new MapGeocodeHelper.GeocodeBuilder()
			.setStreetAddress("94 Auburn Lane")
			.setDivision("Ontario")
			.setURL(MapOperations.geocode)
			.build()
			.getInfo();
	}
	
	@Test(expected = java.lang.IllegalArgumentException.class)
	public void testMapGeocodeHelperApiKey7() throws UnsupportedOperationException, IOException {
		List<String> info = new MapGeocodeHelper.GeocodeBuilder()
			.setArea("Courtice")
			.setDivision("Ontario")
			.setURL(MapOperations.geocode)
			.build()
			.getInfo();
	}
}
