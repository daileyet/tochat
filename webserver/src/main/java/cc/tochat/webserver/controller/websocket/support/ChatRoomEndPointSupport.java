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
* @Title: ChatRoomEndPointSupport.java 
* @Package cc.tochat.webserver.controller.websocket.support 
* @Description: TODO
* @author dailey.yet@outlook.com  
* @date May 16, 2016
* @version V1.0   
*/
package cc.tochat.webserver.controller.websocket.support;

import java.util.Date;
import java.util.List;

import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.Session;

import cc.tochat.webserver.controller.websocket.ChatRoomEndPoint;
import cc.tochat.webserver.model.Room;
import cc.tochat.webserver.model.User;
import cc.tochat.webserver.model.message.AbstractMessage;
import cc.tochat.webserver.model.message.FetchRoomListMessage;
import cc.tochat.webserver.model.message.IMessage;
import cc.tochat.webserver.model.message.UserInfoMessage;
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
public class ChatRoomEndPointSupport implements IEndPointSupported<ChatRoomEndPoint, ChatSession> {
	private ChatRoomService roomService = WebContexts.get().lookup(ChatRoomServiceImpl.class);
	private SecurityService securityService = WebContexts.get().lookup(SecurityServiceImpl.class);
	@SuppressWarnings("unused")
	private final EndpointConfig endpointConfig;

	private final SessionCache<ChatSession> sessionCache = new SessionCache<ChatSession>();

	public ChatRoomEndPointSupport() {
		endpointConfig = null;
	}

	public ChatRoomEndPointSupport(EndpointConfig endpointConfig) {
		this.endpointConfig = endpointConfig;
	}

	@Override
	public void addSession(ChatSession endPointSession) {
		securityService.validateEndpoit(endPointSession.getInstance());
		sessionCache.add(endPointSession);
	}

	@Override
	public IErrorHander getErrorHander(Session session) {
		return new IErrorHander() {
			@Override
			public void process(Throwable t) {
				ProcessLogger.error(CommonUtilities.getCurrentInvokerMethod() + t.getMessage());
			}
		};
	}

	@Override
	public IChannelMessageHandler getMessageHander(final Session session) {
		return new IChannelMessageHandler() {
			final ChatSession chatSession = ChatSession.convert(session);

			@Override
			public void process(IMessage t) {
				if (chatSession.isOpen()) {
					chatSession.sendMessage(t);
				}
			}

			@Override
			public void processSessionUser() {
				User user = securityService.getValidatedUser(session);
				chatSession.sendMessage(UserInfoMessage.valueOf(user));
			}

			@Override
			public void processFetchChannels(AbstractMessage actionMessage) {
				ProcessLogger.debug("Receive message on Chat Server:" + actionMessage);
				FetchRoomListMessage actionMsg = (FetchRoomListMessage) actionMessage;
				if (actionMsg == null) {
					actionMsg = new FetchRoomListMessage();
				}
				List<Room> rooms = roomService.getRooms(actionMsg.getCount(), actionMsg.getOffset());
				actionMsg.setTimestamp(new Date().getTime());
				actionMsg.setContent(rooms);
				this.process(actionMsg);
			}
		};
	}

	@Override
	public void remove(Session session, CloseReason closeReason) {
		sessionCache.remove(ChatSession.convert(session));
	}

}
