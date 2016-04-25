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
* @Title: MessageTypes.java 
* @Package cc.tochat.webserver.helper 
* @Description: TODO
* @author dailey.yet@outlook.com  
* @date Apr 25, 2016
* @version V1.0   
*/
package cc.tochat.webserver.helper;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import cc.tochat.webserver.model.FetchHistoryMessage;
import cc.tochat.webserver.model.IMessage;
import cc.tochat.webserver.model.LoginMessage;
import cc.tochat.webserver.model.LogoutMessage;
import cc.tochat.webserver.model.TextChatMessage;
import cc.tochat.webserver.model.VideoChatMessage;
import cc.tochat.webserver.model.VoiceChatMessage;

import com.openthinks.libs.utilities.Checker;

/**
 * Message types directory
 * @author dailey.yet@outlook.com
 *
 */
public final class MessageTypes {
	private final static Map<Class<? extends IMessage>, String> directory = new ConcurrentHashMap<Class<? extends IMessage>, String>();

	static {
		register(TextChatMessage.class, "TEXT_C");
		register(VoiceChatMessage.class, "VOICE_C");
		register(VideoChatMessage.class, "VIDEO_C");
		register(LoginMessage.class, "LOGIN_A");
		register(LogoutMessage.class, "LOGOUT_A");
		register(FetchHistoryMessage.class, "HISTORY_A");
	}

	/**
	 * @param clazz lookup {@link IMessage} class
	 * @return String
	 */
	public final static String lookup(Class<? extends IMessage> clazz) {
		Checker.require(clazz).notNull("IMessage Class Type could not be empty!");
		String type = directory.get(clazz);

		return type == null ? clazz.getName() : type;
	}

	public final static Class<? extends IMessage> valueOf(String messageTypeName) {
		Class<? extends IMessage> clazz = null;
		for (Entry<Class<? extends IMessage>, String> entry : directory.entrySet()) {
			if (entry.getValue().equals(messageTypeName)) {
				clazz = entry.getKey();
			}
		}
		return clazz;
	}

	public final static void register(Class<? extends IMessage> clazz, String typeName) {
		Checker.require(clazz).notEmpty("IMessage Class Type could not be empty!");
		Checker.require(typeName).notEmpty("Message Type Name could not be empty!");
		directory.put(clazz, typeName);
	}
}
