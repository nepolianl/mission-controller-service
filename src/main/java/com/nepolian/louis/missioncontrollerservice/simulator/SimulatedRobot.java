/*
 * @(#)SimulatedRobot.java							21-Sep-2022	
 * Copyright (c) 2022 Nepolian, Louis, Bengaluru, karnataka - 560076, INDIA
 * All rights reserved.

 * The default software copyright goes here, and navigation link that describe the terms and conditions
 * will also be added such that,
 *  
 * The software is the confidential and proprietary information of Company, Nepolian Louis and so on.. 
 */
package com.nepolian.louis.missioncontrollerservice.simulator;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import com.nepolian.louis.missioncontrollerservice.service.CommandService;

/**
 * @author Nepolian, Louis
 * @date 21-Sep-2022
 * @version 1.0
 *
 */
@Component
@Scope(scopeName = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SimulatedRobot implements Runnable {
	private static final Logger LOGGER = LoggerFactory.getLogger(SimulatedRobot.class.getName());
	
	@Value("${robot.delay}")
	private final boolean delay = true;
	
	@Value("${robot.sleep}")
	private final long millis = 1000;
	
	@Autowired
	private CommandService commandService;
	
	@Override
	public void run() {
		synchronized(this) {
			if (this.delay)
				try {
					Thread.sleep(this.millis);
				} catch (InterruptedException e) {
					// Stop the current task of the thread
					Thread.currentThread().interrupt();
				}
			
			// Process the command to navigate the robot to way points
			try {
				Double[] wayPoint = this.commandService.takeCommand();
				LOGGER.info("Robot is now at <{}>", Arrays.toString(wayPoint));
			} catch (InterruptedException e) {
				// Stop the current task of the thread
				Thread.currentThread().interrupt();
			}
		}
	}

}
