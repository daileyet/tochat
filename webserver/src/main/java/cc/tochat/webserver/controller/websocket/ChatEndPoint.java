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

import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import cc.tochat.webserver.controller.websocket.support.ChatEndPointSupport;
import cc.tochat.webserver.controller.websocket.support.ChatSession;
import cc.tochat.webserver.controller.websocket.support.EndPointSupports;
import cc.tochat.webserver.model.decoder.AbstractMessageDecoder;
import cc.tochat.webserver.model.encoder.AbstractMessageEncoder;
import cc.tochat.webserver.model.message.AbstractMessage;

import com.openthinks.libs.utilities.logger.ProcessLogger;

/**
 * @author dailey.yet@outlook.com
 *
 */
@ServerEndpoint(value = "/chat/{room}", configurator = EndPointsConfigurator.class, encoders = { AbstractMessageEncoder.class }, decoders = { AbstractMessageDecoder.class })
public class ChatEndPoint {

	@OnOpen
	public void open(Session session, EndpointConfig configuration, @PathParam("room") String room) {
		EndPointSupports.lookup(ChatEndPointSupport.class).addSession(ChatSession.valueOf(session, room));
		EndPointSupports.lookup(ChatEndPointSupport.class).getMessageHander(session).processSessionUser();
		EndPointSupports.lookup(ChatEndPointSupport.class).getMessageHander(session).processLoginBroadcast();
		ProcessLogger.debug("One client connected to chat room:[" + room + "],session id :[" + session.getId() + "]");
	}

	@OnMessage
	public void receive(Session session, AbstractMessage chatMessage) {
		EndPointSupports.lookup(ChatEndPointSupport.class).getMessageHander(session).process(chatMessage);
	}

	@OnClose
	public void close(Session session, @PathParam("room") String room, CloseReason closeReason) {
		EndPointSupports.lookup(ChatEndPointSupport.class).getMessageHander(session).processLogoutBroadcast();
		EndPointSupports.lookup(ChatEndPointSupport.class).remove(session, closeReason);
		ProcessLogger.debug("One client disconnected to chat room:[" + room + "],session id :[" + session.getId()
				+ "]," + closeReason);
	}

	@OnError
	public void error(Session session, Throwable throwable) {
		EndPointSupports.lookup(ChatEndPointSupport.class).getErrorHander(session).process(throwable);
	}
}
