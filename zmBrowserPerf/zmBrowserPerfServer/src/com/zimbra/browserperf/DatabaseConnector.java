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
