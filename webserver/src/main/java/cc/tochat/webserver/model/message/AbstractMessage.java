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
* @Title: AbstractMessage.java 
* @Package cc.tochat.webserver.model 
* @Description: TODO
* @author dailey.yet@outlook.com  
* @date Apr 25, 2016
* @version V1.0   
*/
package cc.tochat.webserver.model.message;

import cc.tochat.webserver.model.IConstant;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * @author dailey.yet@outlook.com
 *
 */
public abstract class AbstractMessage implements IMessage {

	@SerializedName(IConstant.MSG_TIMESTAMP)
	private String timestamp;
	@SerializedName(IConstant.MSG_CONTENT)
	private Object content;
	@SerializedName(IConstant.MSG_TYPE)
	private String type = getType();
	@SerializedName(IConstant.MSG_TOKEN)
	private String token;

	protected transient Gson gson = new Gson();

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = String.valueOf(timestamp);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Object> T getContent() {
		return (T) content;
	}

	public <T extends Object> void setContent(T content) {
		this.content = content;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
