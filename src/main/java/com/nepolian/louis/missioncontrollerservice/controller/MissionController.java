/*
  * @(#)MissionController.java							21-Sep-2022	
 * Copyright (c) 2022 Nepolian, Louis, Bengaluru, karnataka - 560076, INDIA
 * All rights reserved.

 * The default software copyright goes here, and navigation link that describe the terms and conditions
 * will also be added such that,
 *  
 * The software is the confidential and proprietary information of Company, Nepolian Louis and so on.. 
 */
package com.nepolian.louis.missioncontrollerservice.controller;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nepolian.louis.missioncontrollerservice.service.MissionService;
import com.nepolian.louis.missioncontrollerservice.utils.Constants;

/**
 * @author Nepolian, Louis
 * @date 21-Sep-2022
 * @version 1.0
 *
 */
@RestController
@RequestMapping("/mission")
public class MissionController {
	private static final Logger LOGGER = LoggerFactory.getLogger(MissionController.class.getName());
	
	@Autowired
	private MissionService missionService;
	
	/**
	 * REST API to receive trajectory with points to submit to mission controller
	 * to send navigation commands to robot
	 * 
	 * @param trajectory
	 * @return
	 */
	@PostMapping( value = "/setTrajectory", consumes = {"application/json"})
	public ResponseEntity<String> setTrajectory(@RequestBody double[][] trajectory) {
		LOGGER.info("Received trajectory {}", Arrays.deepToString(trajectory));
		this.missionService.setTrajectory(trajectory);
		
		return new ResponseEntity<>(Constants.API_RESPONSE, HttpStatus.OK);
	}
}
