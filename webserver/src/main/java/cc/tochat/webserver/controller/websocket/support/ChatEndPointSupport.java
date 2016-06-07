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
* @Title: ChatEndPointSupport.java 
* @Package cc.tochat.webserver.controller.websocket.support 
* @Description: TODO
* @author dailey.yet@outlook.com  
* @date Apr 28, 2016
* @version V1.0   
*/
package cc.tochat.webserver.controller.websocket.support;

import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.Session;

import cc.tochat.webserver.controller.websocket.ChatEndPoint;
import cc.tochat.webserver.model.User;
import cc.tochat.webserver.model.message.IMessage;
import cc.tochat.webserver.model.message.LoginMessage;
import cc.tochat.webserver.model.message.LogoutMessage;
import cc.tochat.webserver.model.message.UserInfoMessage;
import cc.tochat.webserver.service.SecurityService;
import cc.tochat.webserver.service.impl.SecurityServiceImpl;

import com.openthinks.easyweb.context.WebContexts;
import com.openthinks.libs.utilities.CommonUtilities;
import com.openthinks.libs.utilities.logger.ProcessLogger;

/**
 * @author dailey.yet@outlook.com
 *
 */
public class ChatEndPointSupport implements IEndPointSupported<ChatEndPoint, ChatSession> {
	private SecurityService securityService = WebContexts.get().lookup(SecurityServiceImpl.class);

	@SuppressWarnings("unused")
	private final EndpointConfig endpointConfig;

	private final SessionCache<ChatSession> sessionCache = new SessionCache<ChatSession>();

	public ChatEndPointSupport() {
		endpointConfig = null;
	}

	public ChatEndPointSupport(EndpointConfig endpointConfig) {
		this.endpointConfig = endpointConfig;
	}

	@Override
	public IErrorHander getErrorHander(final Session session) {
		final ChatSession chatSession = ChatSession.convert(session);
		return new IErrorHander() {
			@Override
			public void process(Throwable t) {
				ProcessLogger.error(CommonUtilities.getCurrentInvokerMethod() + " on Group:[" + chatSession.getGroup()
						+ "]:" + t.getMessage());
			}
		};
	}

	@Override
	public IChatMessageHander getMessageHander(final Session session) {
		securityService.requireValidated(session);
		final ChatSession chatSession = ChatSession.convert(session);
		final User currentUser = securityService.getValidatedUser(session);

		return new IChatMessageHander() {

			@Override
			public void process(IMessage t) {
				ProcessLogger.debug("Receive message on Chat Server:" + t);
				ProcessLogger.debug("Find client number in Chat room[" + chatSession.getGroup() + "]:"
						+ sessionCache.getSessionGroup(chatSession).size());
				for (ChatSession cs : sessionCache.getSessionGroup(chatSession)) {
					if (!cs.isOpen())
						continue;
					cs.sendMessage(t);
				}
			}

			@Override
			public void processLoginBroadcast() {
				LoginMessage loginMessage = LoginMessage.empty().setUser(currentUser);
				for (ChatSession cs : sessionCache.getSessionGroup(chatSession)) {
					User user = securityService.getValidatedUser(cs.getInstance());
					if (user != null) {
						loginMessage.addUser(user);
					}
				}
				ProcessLogger.debug("Process LoginMessage:" + loginMessage);
				this.process(loginMessage);
			}

			@Override
			public void processLogoutBroadcast() {
				LogoutMessage logoutMessage = LogoutMessage.empty().setUser(currentUser);

				for (ChatSession cs : sessionCache.getSessionGroup(chatSession)) {
					if (cs.equals(chatSession))
						continue;
					User user = securityService.getValidatedUser(cs.getInstance());
					if (user != null) {
						logoutMessage.addUser(user);
					}
				}
				this.process(logoutMessage);
			}

			@Override
			public void processSessionUser() {
				chatSession.sendMessage(UserInfoMessage.valueOf(currentUser));
			}
		};
	}

	@Override
	public void remove(Session session, CloseReason closeReason) {
		sessionCache.remove(ChatSession.convert(session));
	}

	@Override
	public void addSession(ChatSession endPointSession) {
		// validate is login and pass the room security 
		securityService.requireValidated(endPointSession.getInstance());
		sessionCache.add(endPointSession);
	}

}
