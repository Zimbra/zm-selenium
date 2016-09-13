/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2013, 2014, 2016 Synacor, Inc.
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software Foundation,
 * version 2 of the License.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see <https://www.gnu.org/licenses/>.
 * ***** END LICENSE BLOCK *****
 */
package com.zimbra.qa.selenium.framework.core;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Use the <code>ClientSessionFactory</code> class to retrieve
 * {@link ClientSession} instances.
 * <p>
 * The factory determines the current {@link ClientSession} instance
 * based on the current thread ID.
 * <p>
 * If no session exists, it is created.
 * <p>
 *
 * @author Matt Rhoades
 *
 */
public class ClientSessionFactory {
	private static Logger logger = LogManager.getLogger(ClientSessionFactory.class);
	public static ClientSession session() {
		long threadID = Thread.currentThread().getId();
		ClientSession session = getSingleton().getClientSession(Long.toString(threadID));
		logger.debug("Factory found session: "+ session.toString());
		return (session);
	}

	private volatile static ClientSessionFactory singleton;

	private static ClientSessionFactory getSingleton() {
		if(singleton==null) {
			synchronized(ClientSessionFactory.class){
				if(singleton==null)
					singleton= new ClientSessionFactory();
			}
		}
		return (singleton);
	}
	
	private Map<String, ClientSession> SessionMap = null;

	private ClientSessionFactory() {
		logger.info("New ClientSessionFactory");
		SessionMap = new HashMap<String, ClientSession>();
	}

	private ClientSession getClientSession(String threadID) {
		if ( SessionMap.containsKey(threadID) ) {
			return (SessionMap.get(threadID));
		}
		ClientSession session = new ClientSession();
		SessionMap.put(threadID, session);
		return (session);
	}
}
