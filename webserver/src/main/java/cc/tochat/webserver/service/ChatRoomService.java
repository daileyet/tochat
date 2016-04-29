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
* @Title: ChatRoomService.java 
* @Package cc.tochat.webserver.service 
* @Description: TODO
* @author dailey.yet@outlook.com  
* @date Apr 29, 2016
* @version V1.0   
*/
package cc.tochat.webserver.service;

import java.util.List;

import cc.tochat.webserver.model.Room;

/**
 * @author dailey.yet@outlook.com
 *
 */
public interface ChatRoomService {

	/**
	 * fetch room list
	 * @param count int:limit size
	 * @param offset Long: offset 
	 * @return
	 */
	public List<Room> getRooms(int count, Long offset);

}
