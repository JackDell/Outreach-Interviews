package com.outreach.interviews;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

import com.outreach.interviews.map.builder.MapRoutesHelper;
import com.outreach.interviews.map.enums.MapOperations;
import com.outreach.interviews.map.enums.MapRegions;

public class TestMapRoutesHelper 
{	
	
	@Test
	public void testMapRoutesHelperApiKey1() throws UnsupportedOperationException, IOException {
		new MapRoutesHelper.RoutesBuilder()
			.setOrigin("Sudbury")
			.setDestination("Ottawa")
			.setRegion(MapRegions.en)
			.setURL(MapOperations.directions)
			.build();
	}
	
	@Test
	public void testMapRoutesHelperApiKey2() throws UnsupportedOperationException, IOException {
		List<String> steps = new MapRoutesHelper.RoutesBuilder()
			.setOrigin("Sudbury")
			.setDestination("Ottawa")
			.setRegion(MapRegions.en)
			.setURL(MapOperations.directions)
			.build()
			.getDirections();
		
		assertNotNull(steps);
		assertTrue(steps.size() > 5);
	}
	
	@Test(expected = java.lang.UnsupportedOperationException.class)
	public void testMapRoutesHelperApiKey3() throws UnsupportedOperationException, IOException {
		List<String> steps = new MapRoutesHelper.RoutesBuilder()
			.setOrigin("Sudbury")
			.setDestination("Ottawa")
			.setRegion(MapRegions.en)
			.setURL(MapOperations.geocode)
			.build()
			.getDirections();
		
		assertNotNull(steps);
	}
	
	@Test(expected = java.lang.IllegalArgumentException.class)
	public void testMapRoutesHelperApiKey4() throws UnsupportedOperationException, IOException {
		List<String> steps = new MapRoutesHelper.RoutesBuilder()
			.setDestination("Ottawa")
			.setRegion(MapRegions.en)
			.setURL(MapOperations.directions)
			.build()
			.getDirections();
		
		assertNotNull(steps);
	}
	
	@Test
	public void testMapRoutesHelperApiKey5() throws UnsupportedOperationException, IOException {
		List<String> steps = new MapRoutesHelper.RoutesBuilder()
			.setOrigin("Sudbury")
			.setDestination("Ottawa")
			.setRegion(MapRegions.en)
			.setURL(MapOperations.directions)
			.build()
			.getDirections();
		
		assertNotNull(steps);
	}
}
