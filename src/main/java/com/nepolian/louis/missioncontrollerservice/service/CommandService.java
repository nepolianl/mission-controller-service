/*
 * @(#)CommandService.java							21-Sep-2022	
 * Copyright (c) 2022 Nepolian, Louis, Bengaluru, karnataka - 560076, INDIA
 * All rights reserved.

 * The default software copyright goes here, and navigation link that describe the terms and conditions
 * will also be added such that,
 *  
 * The software is the confidential and proprietary information of Company, Nepolian Louis and so on.. 
 */
package com.nepolian.louis.missioncontrollerservice.service;

/**
 * @author Nepolian, Louis
 * @date 21-Sep-2022
 * @version 1.0
 *
 */
public interface CommandService {
	/**
	 * Send navigation command to robot
	 * 
	 * @param wayPoint
	 */
	void setNavigation(double[] wayPoint);
	/**
	 * Get current way point position
	 * 
	 * @return
	 */
	double[] getPosition();
	/**
	 * Clear way point commands
	 * 
	 */
	void clearCommand();
	/**
	 * Poll the latest way point command
	 * 
	 * @return
	 * @throws InterruptedException
	 */
	Double[] takeCommand() throws InterruptedException;
}
