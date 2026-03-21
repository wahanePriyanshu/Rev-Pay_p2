package com.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.revpay.RevPayP2Application;

@SpringBootTest(classes = RevPayP2Application.class)
@ActiveProfiles("test")
class RevPayP2ApplicationTests {

	@Test
	void contextLoads() {
	}

}
