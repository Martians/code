package com.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;

/**
 * API: https://docs.spring.io/spring-boot/docs/current/api/
 */

@Service
@RestController
/**
 * 如果使用 m3_dynamic时，需要关闭自动配置 datasource
 */
//@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class,
//		DataSourceTransactionManagerAutoConfiguration.class,
//		HibernateJpaAutoConfiguration.class })
//@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class})
//@SpringBootApplication(scanBasePackages={"com.example.something", "com.example.application"})
@SpringBootApplication
@EnableTransactionManagement
public class MyworkApplication {

	@RequestMapping("/home")
	String home() {
		return "home";
	}

	public static void main(String[] args) {
		SpringApplication.run(MyworkApplication.class, args);
	}
}
