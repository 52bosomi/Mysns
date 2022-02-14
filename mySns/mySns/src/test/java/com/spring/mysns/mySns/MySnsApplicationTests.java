package com.spring.mysns.mySns;

import com.app.mysns.MySnsApplication;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@SpringBootApplication(scanBasePackages = {"com.portal"})
@PropertySources({
			@PropertySource("classpath:application.properties"),
			// @PropertySource("classpath:application-local.properties")
})
@SpringBootTest(classes = MySnsApplication.class)
class MySnsApplicationTests {

	// @Test
	// void contextLoads() {
	// }

}
