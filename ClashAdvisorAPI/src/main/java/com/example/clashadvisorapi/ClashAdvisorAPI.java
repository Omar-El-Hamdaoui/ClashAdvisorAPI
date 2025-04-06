package com.example.clashadvisorapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class ClashAdvisorAPI {

	public static void main(String[] args) {
		SpringApplication.run(ClashAdvisorAPI.class, args);
		System.out.println("New Begin");
		RestTemplate restTemplate = new RestTemplate();
		System.out.println("RestTemplate OK ✅");
	}

}
