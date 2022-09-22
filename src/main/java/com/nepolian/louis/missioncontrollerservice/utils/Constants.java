/*
 * @(#)Constants.java							22-Sep-2022	
 * Copyright (c) 2022 Nepolian, Louis, Bengaluru, karnataka - 560076, INDIA
 * All rights reserved.

 * The default software copyright goes here, and navigation link that describe the terms and conditions
 * will also be added such that,
 *  
 * The software is the confidential and proprietary information of Company, Nepolian Louis and so on.. 
 */
package com.nepolian.louis.missioncontrollerservice.utils;

import java.util.Arrays;
import java.util.function.Function;

/**
 * @author Nepolian, Louis
 * @date 22-Sep-2022
 * @version 1.0
 *
 */
public class Constants {
	// Messages
	public static final String API_RESPONSE = "Received trajectory with way points";
	
	// Functions
	public static final Function<double[], Double[]> DOUBLE_VALUE_OF_FUNC = (double[] array) -> Arrays.stream(array).boxed().toArray(Double[]::new);
	
}
