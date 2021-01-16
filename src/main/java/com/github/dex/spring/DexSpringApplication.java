package com.github.dex.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DexSpringApplication {

	public static void main(String[] args) {
		SpringApplication.run(DexSpringApplication.class, args);
	}
}
