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
* @Title: ChatRoomServiceImpl.java 
* @Package cc.tochat.webserver.service.impl 
* @Description: TODO
* @author dailey.yet@outlook.com  
* @date Apr 29, 2016
* @version V1.0   
*/
package cc.tochat.webserver.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import cc.tochat.webserver.model.Room;
import cc.tochat.webserver.service.ChatRoomService;

/**
 * @author dailey.yet@outlook.com
 *
 */
public class ChatRoomServiceImpl implements ChatRoomService {

	private final List<Room> rooms;
	{
		rooms = new ArrayList<Room>();
		for (int i = 0; i < 100; i++) {
			Room room = new Room();
			room.setId(String.valueOf(i));
			room.setActive("Y");
			room.setName("Test" + i);
			room.setOwner("Dailey");
			room.setCreateon(String.valueOf(new Date().getTime()));
			room.setPeriod(Float.valueOf(i));
			room.setDesc("Test" + i + ", it is a test description.");
			rooms.add(room);
		}
	}

	@Override
	public List<Room> getRooms(int count, Long offset) {
		int from = (offset == null ? 0 : offset.intValue());
		if (from > rooms.size()) {
			return Collections.emptyList();
		}
		int to = Math.min(from + count, rooms.size());
		return rooms.subList(from, to);
	}
}
