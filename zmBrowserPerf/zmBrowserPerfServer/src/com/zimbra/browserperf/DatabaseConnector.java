/*
 * ***** BEGIN LICENSE BLOCK *****
 * 
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2010, 2011 VMware, Inc.
 * 
 * The contents of this file are subject to the Zimbra Public License
 * Version 1.3 ("License"); you may not use this file except in
 * compliance with the License.  You may obtain a copy of the License at
 * http://www.zimbra.com/license.
 * 
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied.
 * 
 * ***** END LICENSE BLOCK *****
 */
 package com.zimbra.browserperf;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnector {
	
	 public static Connection getConnection() {
	        Connection con = null;
	        String driver = "com.mysql.jdbc.Driver";
	        try {
	            Class.forName(driver).newInstance();

	        } catch (Exception e) {
	            System.out.println("Failed to load mySQL driver.");
	            return null;
	        }
	        try {
	            con = DriverManager
	                    .getConnection("jdbc:mysql://qa61.liquidsys.com:7306/AJAX?user=root&password=zimbra");
		     //   con = DriverManager
			    //                       .getConnection("jdbc:mysql://localhost:7306/AJAX?user=root&password=zimbra");

	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return con;
	    }
}
