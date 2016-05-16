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
* @Title: ChatSession.java 
* @Package cc.tochat.webserver.controller.websocket.support 
* @Description: TODO
* @author dailey.yet@outlook.com  
* @date Apr 28, 2016
* @version V1.0   
*/
package cc.tochat.webserver.controller.websocket.support;

import java.io.IOException;

import javax.servlet.http.HttpSession;
import javax.websocket.EncodeException;
import javax.websocket.Session;

import cc.tochat.webserver.model.IConstant;
import cc.tochat.webserver.model.message.IMessage;

import com.openthinks.libs.utilities.CommonUtilities;
import com.openthinks.libs.utilities.logger.ProcessLogger;

/**
 * @author dailey.yet@outlook.com
 *
 */
public final class ChatSession implements IEndPointSession {
	private String room;
	private Session session;

	private ChatSession(Session session, String room) {
		this.session = session;
		this.room = room;
		this.session.getUserProperties().put(IConstant.MSG_ROOM, getRoom());
	}

	public final static ChatSession valueOf(Session session, String room) {
		return new ChatSession(session, room);
	}

	public final static ChatSession convert(Session session) {
		String room = (String) session.getUserProperties().get(IConstant.MSG_ROOM);
		room = (room == null ? IConstant.MSG_ROOM_DEFAULT_INSTANCE : room);
		return new ChatSession(session, room);
	}

	public String getRoom() {
		return room == null ? IConstant.MSG_ROOM_DEFAULT_INSTANCE : room;
	}

	@Override
	public String getGroup() {
		return getRoom();
	}

	@Override
	public String getId() {
		return getRoom() + IConstant.JOIN_AT + session.getId();
	}

	@Override
	public boolean isOpen() {
		return session.isOpen();
	}

	public Object getHttpSessionAttribute(String attributeName) {
		HttpSession httpSession = (HttpSession) session.getUserProperties().get(IConstant.ATTRIBUTE_HTTP_SESSION);
		if (httpSession != null) {
			return httpSession.getAttribute(attributeName);
		}
		return null;
	}

	public void sendMessage(IMessage message) {
		try {
			ProcessLogger.debug("Sending message from Chat Server:" + message);
			this.session.getBasicRemote().sendObject(message);
			ProcessLogger.debug("Send message from Chat Server finished.");
		} catch (IOException e) {
			ProcessLogger.error(CommonUtilities.getCurrentInvokerMethod() + ":" + e.getMessage());
		} catch (EncodeException e) {
			ProcessLogger.error(CommonUtilities.getCurrentInvokerMethod() + ":" + e.getMessage());
		}
	}

	public final Session getInstance() {
		return this.session;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ChatSession other = (ChatSession) obj;
		if (getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!getId().equals(other.getId()))
			return false;
		return true;
	}

}
