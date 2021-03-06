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
* @Title: ChatRoomEndPoint.java 
* @Package cc.tochat.webserver.controller.websocket 
* @Description: TODO
* @author dailey.yet@outlook.com  
* @date Apr 29, 2016
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
import javax.websocket.server.ServerEndpoint;

import cc.tochat.webserver.controller.websocket.support.ChatRoomEndPointSupport;
import cc.tochat.webserver.controller.websocket.support.ChatSession;
import cc.tochat.webserver.controller.websocket.support.EndPointSupports;
import cc.tochat.webserver.model.IConstant;
import cc.tochat.webserver.model.decoder.AbstractMessageDecoder;
import cc.tochat.webserver.model.encoder.AbstractMessageEncoder;
import cc.tochat.webserver.model.message.AbstractMessage;
import cc.tochat.webserver.model.message.FetchRoomListMessage;

import com.openthinks.libs.utilities.logger.ProcessLogger;

/**
 * @author dailey.yet@outlook.com
 *
 */
@ServerEndpoint(value = "/room", configurator = EndPointsConfigurator.class, encoders = { AbstractMessageEncoder.class }, decoders = { AbstractMessageDecoder.class })
public class ChatRoomEndPoint {

	@OnOpen
	public void open(Session session, EndpointConfig configuration) {
		EndPointSupports.lookup(ChatRoomEndPointSupport.class).addSession(
				ChatSession.valueOf(session, IConstant.MSG_ROOM_DEFAULT_INSTANCE));
		EndPointSupports.lookup(ChatRoomEndPointSupport.class).getMessageHander(session).processSessionUser();
		EndPointSupports.lookup(ChatRoomEndPointSupport.class).getMessageHander(session)
				.processFetchChannels(FetchRoomListMessage.EMPTY);
	}

	@OnMessage
	public void receive(Session session, AbstractMessage actionMessage) {
		EndPointSupports.lookup(ChatRoomEndPointSupport.class).getMessageHander(session)
				.processFetchChannels(actionMessage);
	}

	@OnClose
	public void close(Session session, CloseReason closeReason) {
		EndPointSupports.lookup(ChatRoomEndPointSupport.class).remove(session, closeReason);
		ProcessLogger.debug("One client disconnected to chat channels list,session id :[" + session.getId() + "],"
				+ closeReason);
	}

	@OnError
	public void error(Session session, Throwable throwable) {
		EndPointSupports.lookup(ChatRoomEndPointSupport.class).getErrorHander(session).process(throwable);
	}
}
