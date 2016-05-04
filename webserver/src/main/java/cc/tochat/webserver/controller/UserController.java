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
* @Title: UserController.java 
* @Package cc.tochat.webserver.controller 
* @Description: TODO
* @author dailey.yet@outlook.com  
* @date May 4, 2016
* @version V1.0   
*/
package cc.tochat.webserver.controller;

import cc.tochat.webserver.model.IConstant;
import cc.tochat.webserver.model.User;
import cc.tochat.webserver.service.UserService;
import cc.tochat.webserver.service.impl.UserServiceImpl;

import com.openthinks.easyweb.WebUtils;
import com.openthinks.easyweb.annotation.Controller;
import com.openthinks.easyweb.annotation.Mapping;
import com.openthinks.easyweb.annotation.ResponseReturn;
import com.openthinks.easyweb.context.WebContexts;
import com.openthinks.easyweb.context.handler.WebAttributers;
import com.openthinks.easyweb.utils.json.OperationJson;

/**
 * @author dailey.yet@outlook.com
 *
 */
@Controller("/auth")
public class UserController {
	private UserService userService = WebContexts.get().lookup(UserServiceImpl.class);

	@Mapping("/login")
	@ResponseReturn(contentType = "text/json")
	public String login(WebAttributers was) {
		String account = (String) was.get(IConstant.PARAM_LOGIN_NAME);
		String pass = (String) was.get(IConstant.PARAM_LOGIN_PASS);
		String token = "";
		User user = userService.findUser(account, pass);
		if (user != null) {
			String clientIP = WebUtils.getRemoteIP(was.getRequest());
			user.setLastip(clientIP);
			userService.update(user);
			token = userService.generateToken(user);
			OperationJson.build().sucess(token).toString();
		}

		return OperationJson.build().error().toString();
	}
}
