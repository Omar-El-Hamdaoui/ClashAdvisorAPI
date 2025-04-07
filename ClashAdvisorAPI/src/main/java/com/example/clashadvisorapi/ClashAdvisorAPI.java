package com.example.clashadvisorapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication(scanBasePackages = "com.example.clashadvisorapi")
public class ClashAdvisorAPI {

	public static void main(String[] args) {
		SpringApplication.run(ClashAdvisorAPI.class, args);
	}

}
