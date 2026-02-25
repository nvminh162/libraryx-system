package com.nvminh162.notificationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

import io.github.cdimascio.dotenv.Dotenv;

@EnableDiscoveryClient
@SpringBootApplication
@ComponentScan({"com.nvminh162.notificationservice", "com.nvminh162.commonservice"})
public class NotificationServiceApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
        dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
		SpringApplication.run(NotificationServiceApplication.class, args);
	}

}
