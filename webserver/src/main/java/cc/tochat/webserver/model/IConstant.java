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
* @Title: IConstant.java 
* @Package cc.tochat.webserver.model 
* @Description: TODO
* @author dailey.yet@outlook.com  
* @date Apr 27, 2016
* @version V1.0   
*/
package cc.tochat.webserver.model;

/**
 * @author dailey.yet@outlook.com
 *
 */
public interface IConstant {
	String MESSAGE_TYPE = "mt";
	String EMPTY_CHAT_MESSAGE_TYPE = "C99";
	String EMPTY_ACTION_MESSAGE_TYPE = "A99";

	String MSG_ID = "id";
	String MSG_ROOM = "ro";
	String MSG_FROM = "fr";
	String MSG_TO = "to";
	String MSG_CONTENT = "co";
	String MSG_TIMESTAMP = "ti";
}
