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
* @Title: AbstractMessageDecoder.java 
* @Package cc.tochat.webserver.model.decoder 
* @Description: TODO
* @author dailey.yet@outlook.com  
* @date May 11, 2016
* @version V1.0   
*/
package cc.tochat.webserver.model.decoder;

import static cc.tochat.webserver.model.IConstant.MSG_TYPE;

import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonObject;
import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

import cc.tochat.webserver.helper.MessageTypes;
import cc.tochat.webserver.model.message.AbstractMessage;

import com.openthinks.libs.utilities.InstanceUtilities;
import com.openthinks.libs.utilities.logger.ProcessLogger;

/**
 * @author dailey.yet@outlook.com
 *
 */
public class AbstractMessageDecoder implements Decoder.Text<AbstractMessage> {

	@Override
	public void destroy() {

	}

	@Override
	public void init(EndpointConfig arg0) {

	}

	@Override
	public AbstractMessage decode(String message) throws DecodeException {
		AbstractMessage abstrMessage = AbstractMessage.EMPTY;
		JsonObject msgObj = Json.createReader(new StringReader(message)).readObject();
		String msgType = msgObj.getString(MSG_TYPE);
		Class<AbstractMessage> clz = MessageTypes.valueOf(msgType);
		try {
			abstrMessage = InstanceUtilities.create(clz, null);
		} catch (Exception e) {
			ProcessLogger.error("Decode ChatMessage:[" + message + "] failed on instancing: " + e.getMessage());
		}
		return abstrMessage.decode(message);
	}

	@Override
	public boolean willDecode(String arg0) {
		return true;
	}

}
