package com.example.warehouse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author Дмитрий
 * @version 1.0
 * @since 1.0
 *This is warehouse application start class
 */
@SpringBootApplication
@EnableScheduling
public class WarehouseApplication {
	/**
	 * This is main method, he starts the application
	 * @param args command line values
	 */
	public static void main(String[] args) {
		SpringApplication.run(WarehouseApplication.class, args);
	}

}
