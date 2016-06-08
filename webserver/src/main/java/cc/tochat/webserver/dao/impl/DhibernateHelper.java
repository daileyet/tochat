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
* @Title: DhibernateHelper.java 
* @Package cc.tochat.webserver.dao.impl 
* @Description: TODO
* @author dailey.yet@outlook.com  
* @date Jun 8, 2016
* @version V1.0   
*/
package cc.tochat.webserver.dao.impl;

import cc.tochat.webserver.dao.BaseDao;

import com.openthinks.libs.sql.dhibernate.Session;
import com.openthinks.libs.sql.dhibernate.support.SessionFactory;
import com.openthinks.libs.utilities.InstanceUtilities;

/**
 * @author dailey.yet@outlook.com
 *
 */
public abstract class DhibernateHelper {
	protected Session session;

	public DhibernateHelper() {
		this.session = SessionFactory.getSession();
	}

	public Session getSession() {
		if (this.session == null) {
			this.session = SessionFactory.getSession();
		}
		return session;
	}

	@SuppressWarnings("unchecked")
	public <T extends BaseDao> T newInstance() {
		Class<T> clazz = (Class<T>) this.getClass();
		return InstanceUtilities.create(clazz, null);
	}
}
