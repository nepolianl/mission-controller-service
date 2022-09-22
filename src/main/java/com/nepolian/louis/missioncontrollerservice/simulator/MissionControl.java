/*
 * @(#)MissionControl.java							21-Sep-2022	
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
import com.nepolian.louis.missioncontrollerservice.service.MissionService;

/**
 * @author Nepolian, Louis
 * @date 21-Sep-2022
 * @version 1.0
 *
 */
@Component
@Scope(scopeName = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class MissionControl implements Runnable {
	private static final Logger LOGGER = LoggerFactory.getLogger(MissionControl.class.getName());
	
	@Autowired
	private MissionService missionService;
	
	@Autowired
	private CommandService commandService;
	
	@Value("${mission.poll.sleep}")
	private final long millis = 1000;
	
	@Override
	public void run() {
		double[][] trajectory = this.missionService.getTrajectory();
		if (trajectory == null || trajectory.length == 0 ) {
			LOGGER.info("The robot stops");
		} else {
			this.pollWayPoints(trajectory, this.missionService.getWayPointIndex());
		}
	}
	
	/**
	 * Continuously poll for way points in the submitted trajectory   
	 * 
	 * @param trajectory
	 * @param wayPointIndex
	 */
	private void pollWayPoints(double[][] trajectory, int wayPointIndex) {
		// A gap before polling for another way point index
		try {
			Thread.sleep(this.millis);
		} catch (InterruptedException e) {
			// Stop the current task of the thread
			Thread.currentThread().interrupt();
		}
		
		// 1. If the waypoint index is zero, then send navigation command
		// 2. Otherwise, if the robot position and waypoint matching with the current waypoint index
		// then send navigation command
		if (wayPointIndex == 0) 
			this.sendNavigationCommand(wayPointIndex);
		
		// Need to check if the way point matches to any previous elements until the 
		// current way point index
		else if (hasAnyMatch(wayPointIndex))
			this.sendNavigationCommand(wayPointIndex);
		
		synchronized(this.missionService) {
			trajectory = this.missionService.getTrajectory();
			wayPointIndex = this.missionService.getWayPointIndex();
			// Hard-rule to stop the thread to process further if the way point index was reset 
			// by overriding with another trajectory with points
			// otherwise, the robot stops if the trajectory submitted with [] empty points
			boolean hasWayPointIndexReset = (wayPointIndex == 0);
			boolean isTrajectoryEmpty = (trajectory == null || trajectory.length == 0);
			if (hasWayPointIndexReset || isTrajectoryEmpty) {
				// Log appropriately to notify robot status
				if (isTrajectoryEmpty) LOGGER.info("The robot stops");
				else LOGGER.info("The robot changes the direction to {}...", trajectory[0]);
				// Quit or stop the thread
				return;
			} else {
				// Continuously poll for position until it reaches the final way point
				if (wayPointIndex < trajectory.length && wayPointIndex > -1) {
					// Fetching the updated values in the mission service every time while polling for position
					this.pollWayPoints(this.missionService.getTrajectory(), wayPointIndex);
				}
			}
		}
	}
	
	/**
	 * Send navigation command to robot
	 * 
	 * @param index
	 */
	private void sendNavigationCommand(int index) {
		synchronized(this.missionService) {
			LOGGER.info("Sending waypoint " + index);
			double[][] trajectory = this.missionService.getTrajectory();
			if(trajectory != null && trajectory.length > 0) {
				double[] wayPoint = trajectory[index];
				synchronized (this.commandService) {
					this.commandService.setNavigation(wayPoint); // update and send way point command to robot
					this.missionService.setWayPointIndex(++index); // increment the way point index
				}
			}
		}
	}
	
	/**
	 * Check for way point position in the trajectory [:, currentWayPointIndex]
	 * 
	 * @param wayPointIndex
	 * @return hasAnyMatch for the index and position
	 */
	private boolean hasAnyMatch(int wayPointIndex) {
		double[][] trajectory = this.missionService.getTrajectory();
		if (trajectory != null && trajectory.length > 0) {
			double[][] traversed = Arrays.copyOfRange(trajectory, 0, wayPointIndex);
			
			boolean matched = Arrays.stream(traversed)
					.anyMatch(wayPoint -> wayPoint == this.commandService.getPosition());
			return matched;
		}
		return false;
	}

}
