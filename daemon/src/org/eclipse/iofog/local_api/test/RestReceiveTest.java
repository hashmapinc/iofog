/*******************************************************************************
 * Copyright (c) 2016, 2017 Iotracks, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Saeid Baghbidi
 * Kilton Hopkins
 *  Ashita Nagar
 *******************************************************************************/
package org.eclipse.iofog.local_api.test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.json.Json;
import javax.json.JsonObject;

import org.eclipse.iofog.message_bus.Message;

public class RestReceiveTest {
	private int delay;
	private String receiver;
	int counter;
	
	public RestReceiveTest(String receiver, int delay) {
		this.delay = delay;
		this.receiver = receiver;
		counter = 0;
	}
	
	private final Runnable receive = () -> {
		byte[] bytes = String.format("{\"id\":\"%s\"}", receiver).getBytes(StandardCharsets.US_ASCII);
		
		try {
			while (counter < 1500) {
				HttpURLConnection httpRequest = (HttpURLConnection) new URL("http://127.0.0.1:54321/v2/messages/next").openConnection();
				httpRequest.setRequestMethod("POST");
				httpRequest.setRequestProperty("Content-Type", "application/json");
				httpRequest.setRequestProperty("Content-Length", String.valueOf(bytes.length));
				httpRequest.setDoOutput(true);
				httpRequest.getOutputStream().write(bytes);
				BufferedReader in = new BufferedReader(new InputStreamReader(httpRequest.getInputStream(), "UTF-8"));
				JsonObject result = Json.createReader(in).readObject();
				System.out.println(receiver + " : " + counter);
				counter++;
				try {
					Thread.sleep(delay);
				} catch (Exception e) {
					break;
				}
			}
		} catch (Exception e) {}
	};
	
	public void start() {
		Thread t = new Thread(receive, "RECEIVER");
		t.start();
	}

}
