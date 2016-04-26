/**   
 *  Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
* @Title: ChatEndPoint.java 
* @Package cc.tochat.webserver.controller 
* @Description: TODO
* @author dailey.yet@outlook.com  
* @date Apr 26, 2016
* @version V1.0   
*/
package cc.tochat.webserver.controller.websocket;

import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import com.openthinks.libs.utilities.logger.ProcessLogger;

/**
 * @author dailey.yet@outlook.com
 *
 */
@ServerEndpoint("/chat/{room}")
public class ChatEndPoint {

	@OnOpen
	public void open(Session session, EndpointConfig configuration, @PathParam("room") String room) {
		ProcessLogger.debug("One client connect to room:[" + room + "]");
	}

	@OnMessage
	public String processMessage(Session session, String message, @PathParam("room") String room) {

		return "Receive your message:[" + message + "], you are in room:[" + room + "]";
	}

	@OnClose
	public void close(Session session, @PathParam("room") String room) {
		ProcessLogger.debug("One client disconnect to room:[" + room + "]");
	}
}
