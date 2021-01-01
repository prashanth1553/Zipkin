package com.example.zipkinservice2;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.sleuth.sampler.AlwaysSampler;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class ZipkinService2Application {

	public static void main(String[] args) {
		SpringApplication.run(ZipkinService2Application.class, args);
	}
}

@RestController
class ZipkinController {

	@Autowired
	RestTemplate restTemplate;

	@Bean
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}

	@Bean
	public AlwaysSampler alwaysSampler() {
		return new AlwaysSampler();
	}

	private static final Logger LOG = Logger.getLogger(ZipkinController.class.getName());

	@GetMapping(value = "/zipkin2")
	public String zipkinService1(@RequestHeader HttpHeaders headers) {
		LOG.info("Inside zipkinService 2..");
		LOG.info("Now let's create some intentional delay...");
		try {
			Thread.sleep(20 * 1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LOG.info("returning afte delay..");
		String response = (String) restTemplate.exchange("http://localhost:8083/zipkin3", HttpMethod.GET, null,
				new ParameterizedTypeReference<String>() {
				}).getBody();
		System.out.println(response);
		return "Hi...";
	}
}