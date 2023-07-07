package com.owori;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = {"spring.config.location=classpath:application-test.yml"})
class OworiApplicationTests {
	@Test
	void contextLoads() {
	}
}
