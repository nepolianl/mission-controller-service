package com.nepolian.louis.missioncontrollerservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
 public class MissionControllerServiceApplication {

	public static void main(String[] args) {
		final ConfigurableApplicationContext context = SpringApplication.run(MissionControllerServiceApplication.class, args);
		// Register shutdown hook with spring application context
		context.registerShutdownHook();
		Runtime.getRuntime().addShutdownHook(new Thread(context::close));
	}

}
