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

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.websocket.EncodeException;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import cc.tochat.webserver.model.Room;
import cc.tochat.webserver.model.decoder.ActionMessageDecoder;
import cc.tochat.webserver.model.encoder.ActionMessageEncoder;
import cc.tochat.webserver.model.message.ActionMessage;
import cc.tochat.webserver.model.message.FetchRoomListMessage;
import cc.tochat.webserver.service.ChatRoomService;
import cc.tochat.webserver.service.SecurityService;
import cc.tochat.webserver.service.impl.ChatRoomServiceImpl;
import cc.tochat.webserver.service.impl.SecurityServiceImpl;

import com.openthinks.easyweb.context.WebContexts;
import com.openthinks.libs.utilities.CommonUtilities;
import com.openthinks.libs.utilities.logger.ProcessLogger;

/**
 * @author dailey.yet@outlook.com
 *
 */
@ServerEndpoint(value = "/room", configurator = EndPointsConfigurator.class, encoders = { ActionMessageEncoder.class }, decoders = { ActionMessageDecoder.class })
public class ChatRoomEndPoint {
	private ChatRoomService roomService = WebContexts.get().lookup(ChatRoomServiceImpl.class);
	private SecurityService securityService = WebContexts.get().lookup(SecurityServiceImpl.class);

	@OnOpen
	public void open(Session session, EndpointConfig configuration) {
		if (!securityService.validateEndpoit(session)) {
			ProcessLogger.debug("Not login.");
			try {
				session.close();
			} catch (IOException e) {
				ProcessLogger.error(CommonUtilities.getCurrentInvokerMethod() + ":" + e.getMessage());
			}
			return;
		}
		FetchRoomListMessage actionMsg = new FetchRoomListMessage();
		List<Room> rooms = roomService.getRooms(actionMsg.getCount(), actionMsg.getOffset());
		actionMsg.setTimestamp(new Date().getTime());
		actionMsg.setContent(rooms);
		try {
			session.getBasicRemote().sendObject(actionMsg);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (EncodeException e) {
			e.printStackTrace();
		}
	}

	@OnMessage
	public void receive(Session session, ActionMessage actionMessage) {
		FetchRoomListMessage fetchRoomListMessage = (FetchRoomListMessage) actionMessage;
		List<Room> rooms = roomService.getRooms(fetchRoomListMessage.getCount(), fetchRoomListMessage.getOffset());
		fetchRoomListMessage.setContent(rooms);
		try {
			session.getBasicRemote().sendObject(fetchRoomListMessage);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (EncodeException e) {
			e.printStackTrace();
		}
	}

	@OnClose
	public void close(Session session) {
		ProcessLogger.debug("One client disconnected to  room ,session id :[" + session.getId() + "]");
	}

	@OnError
	public void error(Session session, Throwable throwable) {
		ProcessLogger.error(CommonUtilities.getCurrentInvokerMethod() + ":" + throwable.getMessage());
	}
}
