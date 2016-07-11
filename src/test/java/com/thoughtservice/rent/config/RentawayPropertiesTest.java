package com.thoughtservice.rent.config;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.thoughtservice.rent.RentApp;

import static org.junit.Assert.*;

import org.junit.Test;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RentApp.class)
@IntegrationTest
@EnableConfigurationProperties
public class RentawayPropertiesTest {

	@Autowired
	private RentawayProperties rentawayProperties;
	
	@Test
	public void testRentawayProps(){
		assertNotNull(rentawayProperties.getGoogle().getSearchKey());
	}
}
