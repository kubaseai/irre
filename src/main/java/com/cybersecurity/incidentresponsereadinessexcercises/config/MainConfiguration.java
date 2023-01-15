package com.cybersecurity.incidentresponsereadinessexcercises.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "application")
@Data
public class MainConfiguration {
	private String accessTokens = "";
	private String batchFolder = "./batch";

	public String getAccessTokens() {
		return accessTokens;
	}

	public void setAccessTokens(String accessTokens) {
		this.accessTokens = accessTokens;
	}
	
	public String getBatchFolder() {
		return batchFolder;
	}

	public void setBatchFolder(String batchFolder) {
		this.batchFolder = batchFolder;
	}
}
