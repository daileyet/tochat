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
* @Title: ActionMessage.java 
* @Package cc.tochat.webserver.model 
* @Description: TODO
* @author dailey.yet@outlook.com  
* @date Apr 25, 2016
* @version V1.0   
*/
package cc.tochat.webserver.model.message;

import cc.tochat.webserver.helper.MessageTypes;
import cc.tochat.webserver.model.IConstant;

import com.google.gson.annotations.SerializedName;

/**
 * @author dailey.yet@outlook.com
 *
 */
public abstract class ActionMessage extends AbstractMessage {
	@SerializedName(IConstant.MSG_FROM)
	private String from;

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	@Override
	public String getType() {
		return MessageTypes.lookup(this.getClass());
	}

	@Override
	public String encode() {
		return gson.toJson(this);
	}

	@Override
	public ActionMessage decode(String target) {
		return gson.fromJson(target, this.getClass());
	}

	public final static ActionMessage EMPTY = new NullMessage();

	static class NullMessage extends ActionMessage {
		@Override
		public String getType() {
			return "EMPTY";
		}

		@Override
		public String encode() {
			return "";
		}

		@Override
		public ActionMessage decode(String target) {
			return this;
		}

		@SuppressWarnings("unchecked")
		@Override
		public String getContent() {
			return "";
		}

	}
}
