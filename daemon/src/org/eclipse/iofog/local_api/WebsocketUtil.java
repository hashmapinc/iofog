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
package org.eclipse.iofog.local_api;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.iofog.utils.logging.LoggingService;

import io.netty.channel.ChannelHandlerContext;

/**
 * Utility class for the real-time message and control websockets
 * @author ashita
 * @since 2016
 */
public class WebsocketUtil {
	private static final String MODULE_NAME = "Local API";
	
	/**
	 * Remove inactive websocket from the open websocket map
	 * @param ChannelHandlerContext, Hashtable<String, ChannelHandlerContext>
	 * @return void
	 */
	public static synchronized void removeWebsocketContextFromMap(ChannelHandlerContext ctx, Hashtable<String, ChannelHandlerContext> socketMap){
		for (Iterator<Map.Entry<String,ChannelHandlerContext>> it = socketMap.entrySet().iterator(); it.hasNext();) {
			Map.Entry<String,ChannelHandlerContext> e = it.next();
			if (ctx.equals(e.getValue())) {
				LoggingService.logInfo(MODULE_NAME,"Removing real-time websocket context for the id: " + e.getKey());
				it.remove();
			}
		}
	}
	
	/**
	 * Check if the container has open real-time websocket
	 * @param ChannelHandlerContext, Hashtable<String, ChannelHandlerContext>
	 * @return boolean
	 */
	public static boolean hasContextInMap(ChannelHandlerContext ctx, Hashtable<String, ChannelHandlerContext> socketMap) throws Exception{
		for (ChannelHandlerContext context: socketMap.values()) 
			if (context.equals(ctx))
				return true;
		
		return false;
	}
	
	/**
	 * Get id for the real-time socket channel
	 * @param ChannelHandlerContext, Hashtable<String, ChannelHandlerContext>
	 * @return String
	 */
	public static String getIdForWebsocket(ChannelHandlerContext ctx, Hashtable<String, ChannelHandlerContext> socketMap){
		String id = "";
		for (Iterator<Map.Entry<String,ChannelHandlerContext>> it = socketMap.entrySet().iterator(); it.hasNext();) {
			Map.Entry<String,ChannelHandlerContext> e = it.next();
			if (ctx.equals(e.getValue())) {
				LoggingService.logInfo(MODULE_NAME,"Context found as real-time websocket");
				return e.getKey();
			}
		}
		return id;
	}
}
