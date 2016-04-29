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
* @Title: ActionMessageDecoder.java 
* @Package cc.tochat.webserver.model.decoder 
* @Description: TODO
* @author dailey.yet@outlook.com  
* @date Apr 29, 2016
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
import cc.tochat.webserver.model.message.ActionMessage;

import com.openthinks.libs.utilities.InstanceUtilities;
import com.openthinks.libs.utilities.logger.ProcessLogger;

/**
 * @author dailey.yet@outlook.com
 *
 */
public class ActionMessageDecoder implements Decoder.Text<ActionMessage> {

	@Override
	public void destroy() {

	}

	@Override
	public void init(EndpointConfig conf) {

	}

	@Override
	public ActionMessage decode(String actionMsg) throws DecodeException {
		ActionMessage actionMessage = ActionMessage.EMPTY;
		JsonObject msgObj = Json.createReader(new StringReader(actionMsg)).readObject();
		String msgType = msgObj.getString(MSG_TYPE);
		Class<ActionMessage> clz = MessageTypes.valueOf(msgType);
		try {
			actionMessage = InstanceUtilities.create(clz, null);
		} catch (Exception e) {
			ProcessLogger.error("Decode ChatMessage:[" + actionMsg + "] failed on instancing: " + e.getMessage());
		}
		return actionMessage.decode(actionMsg);
	}

	@Override
	public boolean willDecode(String actionMsg) {
		return true;
	}

}
