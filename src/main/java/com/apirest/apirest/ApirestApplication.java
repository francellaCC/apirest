package com.apirest.apirest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class ApirestApplication {

	public static void main(String[] args) {

		Dotenv dotenv = Dotenv.load();

		SpringApplication.run(ApirestApplication.class, args);
	}

}
