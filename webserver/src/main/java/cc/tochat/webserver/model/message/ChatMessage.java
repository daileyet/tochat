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
* @Title: ChatMessage.java 
* @Package cc.tochat.webserver.model 
* @Description: TODO
* @author dailey.yet@outlook.com  
* @date Apr 25, 2016
* @version V1.0   
*/
package cc.tochat.webserver.model.message;

import cc.tochat.webserver.helper.MessageTypes;
import cc.tochat.webserver.model.IContentEncoder;
import cc.tochat.webserver.model.IRecordable;

/**
 * Message of Chat type
 * @author dailey.yet@outlook.com
 *
 */
public abstract class ChatMessage extends AbstractMessage implements IRecordable, IContentEncoder {
	private String id;
	private String room;
	private String from;
	private String to;
	private Object content;

	@Override
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	public String getRoom() {
		return room;
	}

	public void setRoom(String room) {
		this.room = room;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Object> T getContent() {
		return (T) content;
	}

	public <T extends Object> void setContent(T content) {
		this.content = content;
	}

	@Override
	public String getType() {
		return MessageTypes.lookup(this.getClass());
	}

	@Override
	public String encode() {
		return (String) getContent();
	}

	@Override
	public String toString() {
		return "ChatMessage [id=" + id + ", room=" + room + ", from=" + from + ", to=" + to + ", content=" + content
				+ "]";
	}

	public final static ChatMessage EMPTY = new NullChatMessage();

	static class NullChatMessage extends ChatMessage {
		@Override
		public String getType() {
			return "EMPTY";
		}
	}

}
