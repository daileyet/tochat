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
* @Title: UserServiceImpl.java 
* @Package cc.tochat.webserver.service.impl 
* @Description: TODO
* @author dailey.yet@outlook.com  
* @date May 4, 2016
* @version V1.0   
*/
package cc.tochat.webserver.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cc.tochat.webserver.model.User;
import cc.tochat.webserver.service.UserService;

/**
 * @author dailey.yet@outlook.com
 *
 */
public class UserServiceImpl implements UserService {
	private final List<User> users;
	{
		users = new ArrayList<User>();
		for (int i = 0; i < 100; i++) {
			User user = new User();
			user.setId("U" + i);
			user.setName("Dailey" + i);
			user.setEmail("dailey" + i + "@oracle.com");
			user.setPassword("1234");
			user.setAlias("d" + 1);
			users.add(user);
		}
	}

	@Override
	public User findUser(String account, String pass) {
		for (User user : users) {
			if ((user.getName().equals(account) || user.getEmail().equals(account)) && user.getPassword().equals(pass)) {
				return user;
			}
		}
		return null;
	}

	@Override
	public boolean update(User user) {
		for (User u : users) {
			if (u.getId().equals(user.getId())) {
				u.setName(user.getName());
				u.setAlias(user.getAlias());
				u.setEmail(user.getEmail());
				u.setPassword(user.getPassword());
				u.setLastip(user.getLastip());
				return true;
			}
		}
		return false;
	}

	@Override
	public String generateToken(User user) {

		return user.getId() + "@" + user.getLastip() + "@" + new Date().getTime();
	}
}
