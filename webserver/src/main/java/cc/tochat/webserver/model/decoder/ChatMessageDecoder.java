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
* @Title: ChatMessageDecoder.java 
* @Package cc.tochat.webserver.model.decoder 
* @Description: TODO
* @author dailey.yet@outlook.com  
* @date Apr 27, 2016
* @version V1.0   
*/
package cc.tochat.webserver.model.decoder;

import static cc.tochat.webserver.model.IConstant.MESSAGE_TYPE;
import static cc.tochat.webserver.model.IConstant.MSG_CONTENT;
import static cc.tochat.webserver.model.IConstant.MSG_FROM;
import static cc.tochat.webserver.model.IConstant.MSG_ID;
import static cc.tochat.webserver.model.IConstant.MSG_ROOM;
import static cc.tochat.webserver.model.IConstant.MSG_TIMESTAMP;
import static cc.tochat.webserver.model.IConstant.MSG_TO;

import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonObject;
import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

import cc.tochat.webserver.helper.MessageTypes;
import cc.tochat.webserver.model.ChatMessage;

import com.openthinks.libs.utilities.InstanceUtilities;
import com.openthinks.libs.utilities.logger.ProcessLogger;

/**
 * @author dailey.yet@outlook.com
 *
 */
public class ChatMessageDecoder implements Decoder.Text<ChatMessage> {

	@Override
	public void destroy() {

	}

	@Override
	public void init(EndpointConfig cfg) {

	}

	@Override
	public ChatMessage decode(String message) throws DecodeException {
		ChatMessage chatMessage = ChatMessage.EMPTY;
		JsonObject msgObj = Json.createReader(new StringReader(message)).readObject();
		String msgType = msgObj.getString(MESSAGE_TYPE);
		Class<ChatMessage> clz = MessageTypes.valueOf(msgType);
		try {
			chatMessage = InstanceUtilities.create(clz, null);
		} catch (Exception e) {
			ProcessLogger.error("Decode ChatMessage:[" + message + "] failed on instancing: " + e.getMessage());
		}
		chatMessage.setId(msgObj.getString(MSG_ID));
		chatMessage.setFrom(msgObj.getString(MSG_FROM));
		chatMessage.setTo(msgObj.getString(MSG_TO));
		chatMessage.setContent(msgObj.getString(MSG_CONTENT));
		chatMessage.setRoom(msgObj.getString(MSG_ROOM));
		chatMessage.setTimestamp(msgObj.getString(MSG_TIMESTAMP));
		return chatMessage;
	}

	@Override
	public boolean willDecode(String message) {
		return true;
	}

}
