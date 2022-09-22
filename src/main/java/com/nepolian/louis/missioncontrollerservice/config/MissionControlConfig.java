/*
 * @(#)MissionControlConfig.java							21-Sep-2022	
 * Copyright (c) 2022 Nepolian, Louis, Bengaluru, karnataka - 560076, INDIA
 * All rights reserved.

 * The default software copyright goes here, and navigation link that describe the terms and conditions
 * will also be added such that,
 *  
 * The software is the confidential and proprietary information of Company, Nepolian Louis and so on.. 
 */
package com.nepolian.louis.missioncontrollerservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Nepolian, Louis
 * @date 21-Sep-2022
 * @version 1.0
 *
 */
@Configuration
@EnableAsync
public class MissionControlConfig {

	@Bean(name = {"asyncTaskExecutor"})
	public TaskExecutor asyncTaskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setMaxPoolSize(10);
		executor.setCorePoolSize(10);
		executor.setQueueCapacity(100);
		executor.setThreadNamePrefix("AsyncThread -");
		executor.initialize();
		
		return executor;
	}
	
	@Bean
	public ObjectMapper objectMapper() {
		return new ObjectMapper();
	}
}
