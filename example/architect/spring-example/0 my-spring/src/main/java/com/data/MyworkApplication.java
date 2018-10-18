package com.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;

/**
 * API: https://docs.spring.io/spring-boot/docs/current/api/
 */

@Service
@RestController
@SpringBootApplication
//@SpringBootApplication(scanBasePackages={"com.example.something", "com.example.application"})
public class MyworkApplication {

	@RequestMapping("/home")
	String home() {
		return "home";
	}

	public static void main(String[] args) {
		SpringApplication.run(MyworkApplication.class, args);
	}

/**
 *  需要 extends WebMvcConfigurerAdapter
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addRedirectViewController("/", "/welcome");
	}
*/
}
