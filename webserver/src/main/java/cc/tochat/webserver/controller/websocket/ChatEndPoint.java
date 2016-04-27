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

import java.io.IOException;
import java.util.Set;

import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import cc.tochat.webserver.model.ChatMessage;
import cc.tochat.webserver.model.IConstant;
import cc.tochat.webserver.model.decoder.ChatMessageDecoder;
import cc.tochat.webserver.model.encoder.ChatMessageEncoder;

import com.openthinks.libs.utilities.logger.ProcessLogger;

/**
 * @author dailey.yet@outlook.com
 *
 */
@ServerEndpoint(value = "/chat/{room}", encoders = { ChatMessageEncoder.class }, decoders = { ChatMessageDecoder.class })
public class ChatEndPoint {
	@OnOpen
	public void open(Session session, EndpointConfig configuration, @PathParam("room") String room) {
		session.getUserProperties().put(IConstant.MSG_ROOM, room);
		ProcessLogger.debug("One client connect to room:[" + room + "]");
	}

	@OnMessage
	public void processMessage(Session session, ChatMessage chatMessage) {
		String room = (String) session.getUserProperties().get(IConstant.MSG_ROOM);
		ProcessLogger.debug("One client send message to room:[" + room + "]," + chatMessage);
		String rmsg = "Receive your message:[" + chatMessage.getContent() + "], you are in room:[" + room + "]";
		log(session.getOpenSessions(), room);
		for (Session s : session.getOpenSessions()) {
			if (!room.equals(s.getUserProperties().get(IConstant.MSG_ROOM)))
				continue;
			try {
				s.getBasicRemote().sendText(rmsg);
			} catch (IOException e) {
				ProcessLogger.error("Error on process message in room:[" + room + "]");
			}
		}
	}

	private void log(Set<Session> openSessions, String room) {
		ProcessLogger.debug("all client which coonect to room:[" + room + "]");
		for (Session s : openSessions) {
			String r = (String) s.getUserProperties().get(IConstant.MSG_ROOM);
			ProcessLogger.debug(">>> Session ID:" + s.getId() + ",Room:" + r);
		}
	}

	@OnClose
	public void close(Session session, @PathParam("room") String room) {
		ProcessLogger.debug("One client disconnect to room:[" + room + "]");
	}

}
