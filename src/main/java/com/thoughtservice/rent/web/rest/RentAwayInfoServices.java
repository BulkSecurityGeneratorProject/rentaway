package com.thoughtservice.rent.web.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.thoughtservice.rent.config.RentawayProperties;

@RestController
@RequestMapping("/api")
public class RentAwayInfoServices {

	@Autowired
	private RentawayProperties rentawayProperties;
	

	@RequestMapping(value = "/rentaway-profile-info", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public RentAwayInfoResponse getGoogleSearchKey() {
		return new RentAwayInfoResponse(rentawayProperties.getGoogle().getSearchKey());
	}	
}

class RentAwayInfoResponse {

	public String googleSearchKey;
	
	public RentAwayInfoResponse(String searchkey) {
		this.googleSearchKey = searchkey;
	}
	
}
