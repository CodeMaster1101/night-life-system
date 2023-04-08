package com.mile.mail.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MailServerApplication {

	public static void main(String[] args) {
		System.setProperty("javax.net.ssl.trustStore", "src/main/resources/clientTruststore.jks");
		System.setProperty("javax.net.ssl.trustStorePassword", System.getenv("STORE_PASSWORD"));
		SpringApplication.run(MailServerApplication.class, args);
	}

}
