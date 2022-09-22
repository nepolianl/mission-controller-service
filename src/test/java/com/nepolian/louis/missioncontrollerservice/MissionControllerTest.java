/*
 * @(#)MissionControllerTest.java							22-Sep-2022	
 * Copyright (c) 2022 Nepolian, Louis, Bengaluru, karnataka - 560076, INDIA
 * All rights reserved.

 * The default software copyright goes here, and navigation link that describe the terms and conditions
 * will also be added such that,
 *  
 * The software is the confidential and proprietary information of Company, Nepolian Louis and so on.. 
 */
package com.nepolian.louis.missioncontrollerservice;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nepolian.louis.missioncontrollerservice.service.MissionService;

/**
 * @author Nepolian, Louis
 * @date 22-Sep-2022
 * @version 1.0
 *
 */
@SpringBootTest
@AutoConfigureMockMvc
public class MissionControllerTest {

	@Autowired
	private MockMvc mvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private MissionService missionService;
	
	@Test
	public void submitSingleTrajectoryPointsTest() throws Exception {
		double[][] trajectory = {{1,2},{2,2},{3,2},{4,2},{5,2},{6,2},{7,2},{8,2},{9,2},{10,2},{11,2},{12,2}};
		
		mvc.perform(
				post("/mission/setTrajectory")
				.contentType(MediaType.APPLICATION_JSON)
				.content(this.objectMapper.writeValueAsString(trajectory)))
		.andExpect(status().isOk());
		
	}
	
	@Test
	public void setTrajectory() throws Exception {
		double[][] trajectory = {{1,2},{2,2},{3,2},{4,2},{5,2},{6,2},{7,2},{8,2},{9,2},{10,2},{11,2},{12,2}};
		this.missionService.setTrajectory(trajectory);
	}
}
