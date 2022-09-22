/*
 * @(#)MissionControllerIntegrationTest.java							22-Sep-2022	
 * Copyright (c) 2022 Nepolian, Louis, Bengaluru, karnataka - 560076, INDIA
 * All rights reserved.

 * The default software copyright goes here, and navigation link that describe the terms and conditions
 * will also be added such that,
 *  
 * The software is the confidential and proprietary information of Company, Nepolian Louis and so on.. 
 */
package com.nepolian.louis.missioncontrollerservice;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

/**
 * @author Nepolian, Louis
 * @date 22-Sep-2022
 * @version 1.0
 *
 */
@SpringBootTest(classes = MissionControllerServiceApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class MissionControllerIntegrationTest {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;
	
	private HttpHeaders httpHeaders = new HttpHeaders();
	
	@Test
	public void setTrajectorRequestTest() {
		double[][] trajectory = {{1,2},{2,2},{3,2},{4,2},{5,2},{6,2},{7,2},{8,2},{9,2},{10,2},{11,2},{12,2}};
		HttpEntity<double[][]> entity = new HttpEntity<double[][]>(trajectory, this.httpHeaders);
		
		ResponseEntity<String> response = restTemplate.exchange(
				createURLWithPort("/api/mission/setTrajectory"), HttpMethod.POST, entity, String.class);
		
		assertEquals(response.getStatusCodeValue(), 200);
	}
	
	private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}
