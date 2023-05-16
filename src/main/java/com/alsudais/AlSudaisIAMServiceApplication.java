package com.alsudais;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactivefeign.spring.config.EnableReactiveFeignClients;

@SpringBootApplication
@EnableReactiveFeignClients
public class AlSudaisIAMServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AlSudaisIAMServiceApplication.class, args);
	}

}
