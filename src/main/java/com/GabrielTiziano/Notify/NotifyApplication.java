package com.GabrielTiziano.Notify;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class NotifyApplication {

	public static void main(String[] args) {
		SpringApplication.run(NotifyApplication.class, args);
	}

}
