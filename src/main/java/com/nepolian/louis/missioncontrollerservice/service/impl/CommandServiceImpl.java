/*
 * @(#)CommandServiceImpl.java							21-Sep-2022	
 * Copyright (c) 2022 Nepolian, Louis, Bengaluru, karnataka - 560076, INDIA
 * All rights reserved.

 * The default software copyright goes here, and navigation link that describe the terms and conditions
 * will also be added such that,
 *  
 * The software is the confidential and proprietary information of Company, Nepolian Louis and so on.. 
 */
package com.nepolian.louis.missioncontrollerservice.service.impl;

import java.util.concurrent.LinkedBlockingQueue;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.nepolian.louis.missioncontrollerservice.service.CommandService;
import com.nepolian.louis.missioncontrollerservice.simulator.SimulatedRobot;
import com.nepolian.louis.missioncontrollerservice.utils.Constants;

/**
 * @author Nepolian, Louis
 * @date 21-Sep-2022
 * @version 1.0
 *
 */
@Service
public class CommandServiceImpl implements CommandService {
	private static final Logger LOGGER = LoggerFactory.getLogger(CommandServiceImpl.class.getName());
	
	private LinkedBlockingQueue<Double[]> commands = new LinkedBlockingQueue<>();
	private volatile double[] position;
	
	@Autowired
	private TaskExecutor asyncTaskExecutor;
	
	@Autowired
	private SimulatedRobot robot;
	
	/**
	 * Send way point navigation command to robot
	 * 
	 * @param wayPoint
	 */
	@Override
	public void setNavigation(double[] wayPoint) {
		LOGGER.info("Commanding robot to move to {}", wayPoint);
		this.position = wayPoint;
		
		Double[] arr = Constants.DOUBLE_VALUE_OF_FUNC.apply(wayPoint);
		this.commands.add(arr);
		this.asyncTaskExecutor.execute(this.robot);
	}

	/**
	 * Get current way point position
	 * 
	 * @return current position
	 */
	@Override
	public double[] getPosition() {
		return this.position;
	}
	
	/**
	 * Poll the way point position 
	 * 
	 * @return FIFO - very first command in the queue
	 * @throws InterruptedException 
	 */
	@Override
	public Double[] takeCommand() throws InterruptedException {
		return this.commands.take();
	}
	
	/**
	 * Clear the way points
	 * 
	 */
	@Override
	public void clearCommand() {
		this.commands.clear();
	}
	
	/**
	 * Initialize the service variables
	 * 
	 */
	@PostConstruct
	private void onStartup() {
		this.position = null;
	}
	
	/**
	 * Destroy threads, and un-reference object variables to
	 * free-up memory
	 * 
	 */
	@PreDestroy
	private void onShutdown() {
		this.clearCommand();
		this.position = null;
		((ThreadPoolTaskExecutor) this.asyncTaskExecutor ).destroy();
		LOGGER.info("Destoryed command service");
	}
}
