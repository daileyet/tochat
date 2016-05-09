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

import java.io.IOException;

import javax.websocket.CloseReason;
import javax.websocket.EncodeException;
import javax.websocket.EndpointConfig;
import javax.websocket.Session;

import cc.tochat.webserver.controller.websocket.ChatEndPoint;
import cc.tochat.webserver.model.message.IMessage;

import com.openthinks.libs.utilities.CommonUtilities;
import com.openthinks.libs.utilities.logger.ProcessLogger;

/**
 * @author dailey.yet@outlook.com
 *
 */
public class ChatEndPointSupport implements IEndPointSupported<ChatEndPoint, ChatSession> {

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
	public IErrorHander getErrorHander(Session session) {
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
	public IMessageHander getMessageHander(Session session) {
		final ChatSession chatSession = ChatSession.convert(session);

		return new IMessageHander() {

			@Override
			public void process(IMessage t) {
				ProcessLogger.debug("Receive message on Chat Server:" + t.getContent());
				ProcessLogger.debug("Find client number in Chat room[" + chatSession.getGroup() + "]:"
						+ sessionCache.getSessionGroup(chatSession).size());
				for (ChatSession cs : sessionCache.getSessionGroup(chatSession)) {

					if (!cs.isOpen())
						continue;
					try {
						ProcessLogger.debug("Sending message from Chat Server:" + t.getContent());
						cs.getInstance().getBasicRemote().sendObject(t);
						ProcessLogger.debug("Send message from Chat Server finished.");
					} catch (IOException e) {
						ProcessLogger.error(CommonUtilities.getCurrentInvokerMethod() + ":" + e.getMessage());
					} catch (EncodeException e) {
						ProcessLogger.error(CommonUtilities.getCurrentInvokerMethod() + ":" + e.getMessage());
					}
				}
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
		sessionCache.add(endPointSession);
	}

}
