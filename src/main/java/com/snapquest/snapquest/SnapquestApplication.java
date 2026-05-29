package com.snapquest.snapquest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SnapquestApplication {

	public static void main(String[] args) {
		SpringApplication.run(SnapquestApplication.class, args);
	}

}
