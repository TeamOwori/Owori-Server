package com.owori;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class OworiApplication {
	public static void main(String[] args) {
		SpringApplication.run(OworiApplication.class, args);
	}
}
