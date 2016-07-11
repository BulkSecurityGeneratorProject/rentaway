package com.thoughtservice.rent.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "rentaway", ignoreUnknownFields = false)
public class RentawayProperties {
	
	private static final Logger log = LoggerFactory.getLogger(RentawayProperties.class);

	private final Google google = new Google();
	
	public RentawayProperties() {
		log.info("Constructor invoked with");
	}
	
	public Google getGoogle() {
		return google;
	}

	public static class Google {
		private String searchKey;

		public String getSearchKey() {
			return searchKey;
		}

		public void setSearchKey(String searchKey) {
			this.searchKey = searchKey;
		}
		
		
	}
}
