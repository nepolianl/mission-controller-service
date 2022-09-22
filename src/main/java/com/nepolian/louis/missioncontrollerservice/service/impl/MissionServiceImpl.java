/*
 * @(#)MissionServiceImpl.java							21-Sep-2022	
 * Copyright (c) 2022 Nepolian, Louis, Bengaluru, karnataka - 560076, INDIA
 * All rights reserved.

 * The default software copyright goes here, and navigation link that describe the terms and conditions
 * will also be added such that,
 *  
 * The software is the confidential and proprietary information of Company, Nepolian Louis and so on.. 
 */
package com.nepolian.louis.missioncontrollerservice.service.impl;

import java.util.Arrays;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.nepolian.louis.missioncontrollerservice.service.CommandService;
import com.nepolian.louis.missioncontrollerservice.service.MissionService;
import com.nepolian.louis.missioncontrollerservice.simulator.MissionControl;

/**
 * @author Nepolian, Louis
 * @date 21-Sep-2022
 * @version 1.0
 *
 */
@Service
public class MissionServiceImpl implements MissionService {
	private static final Logger LOGGER = LoggerFactory.getLogger(MissionServiceImpl.class.getName());
	
	@Autowired
	private TaskExecutor asyncTaskExecutor;
	
	@Autowired
	private MissionControl missionControl;
	
	@Autowired
	private CommandService commandService;
	
	@Value("${robot.initial.position}")
	private double[] defaultCoordinates = {1,0};
	
	private volatile int wayPointIndex = 0;
	private volatile double[][] trajectory;
	
	/**
	 * Receives trajectory with points to send navigation commands
	 * 
	 * @param wayPoints
	 * @return void
	 */
	@Override
	public void setTrajectory(double[][] wayPoints) {
		LOGGER.info("Mission service received trajectory " + Arrays.deepToString(wayPoints));
		this.commandService.clearCommand();
		this.trajectory = wayPoints;
		this.wayPointIndex = 0; // set initial way point index
		// Do not start another thread if the trajectory submitted with empty points
		if (wayPoints != null && wayPoints.length > 0) {
			this.asyncTaskExecutor.execute(this.missionControl);
		}
	}

	/**
	 * Get trajectory with points
	 * 
	 * @return trajectory
	 */
	@Override
	public double[][] getTrajectory() {
		return this.trajectory;
	}
	
	/**
	 * Manage current way point index
	 * 
	 * @param index
	 */
	@Override
	public void setWayPointIndex(int index) {
		this.wayPointIndex = index;
	}
	
	/**
	 * Get the current way point 
	 * 
	 * @return current way point index
	 */
	@Override
	public int getWayPointIndex() {
		return this.wayPointIndex;
	}
	
	/**
	 * Initialize the mission service variables, and send navigation
	 * commands to robot
	 * 
	 */
	@PostConstruct
	private void onStartup() {
		this.commandService.clearCommand();
		
		double[][] defaultCoordinates = { this.defaultCoordinates };
		this.trajectory = defaultCoordinates;
		this.wayPointIndex = 0; // set initial way point index
		this.asyncTaskExecutor.execute(this.missionControl);
	}
	
	/**
	 * Destroys the mission service and un-reference variable
	 * to free-up memory
	 * 
	 */
	@PreDestroy
	private void onShutdown() {
		this.trajectory = null;
		((ThreadPoolTaskExecutor) this.asyncTaskExecutor ).destroy();
		LOGGER.info("Destoryed mission service");
	}
}
