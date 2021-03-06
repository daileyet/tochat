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
* @Title: FetchRoomListMessage.java 
* @Package cc.tochat.webserver.model 
* @Description: TODO
* @author dailey.yet@outlook.com  
* @date Apr 26, 2016
* @version V1.0   
*/
package cc.tochat.webserver.model.message;

import java.util.List;

import cc.tochat.webserver.model.IConstant;
import cc.tochat.webserver.model.Room;

import com.google.gson.annotations.SerializedName;

/**
 * @author dailey.yet@outlook.com
 *
 */
public class FetchRoomListMessage extends ActionMessage {
	@SerializedName(IConstant.MSG_COUNT)
	private int count = IConstant.FETCH_COUNT_DEFAULT;
	@SerializedName(IConstant.MSG_OFFSET)
	private long offset;

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public long getOffset() {
		return offset;
	}

	public void setOffset(long offset) {
		this.offset = offset;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Room> getContent() {
		return super.getContent();
	}

	@Override
	public String toString() {
		return "FetchRoomListMessage [getCount()=" + getCount() + ", getOffset()=" + getOffset() + ", getContent()="
				+ getContent() + ", getFrom()=" + getFrom() + ", getType()=" + getType() + "]";
	}

	public static final FetchRoomListMessage EMPTY = new FetchRoomListMessage();

}
