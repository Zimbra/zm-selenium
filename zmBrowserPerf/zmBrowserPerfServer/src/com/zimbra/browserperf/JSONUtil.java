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

import java.util.*;

public class JSONUtil {

	public static String buildJSONArray(List list, String title) {
		StringBuffer returnJSON = new StringBuffer("\r\n{\"" + title + "\": [");
		String key = "username";
		String value = "";
		// loop through all the map entries.
		Iterator it = list.iterator();

		while (it.hasNext()) {
			value = (String) it.next();
			returnJSON.append("\r\n{\"" + key + "\": \"" + value + "\"},");
		}
		// remove the last comma
		int lastCharIndex = returnJSON.length();
		returnJSON.deleteCharAt(lastCharIndex - 1);
		returnJSON.append("\r\n]}");
		return returnJSON.toString();
	}

	public static String buildJSONWithArrayList(ArrayList customerList) {
		int counter = 0;
		Iterator custIterator = customerList.iterator();


		StringBuffer returnJSON = new StringBuffer("{\"rows\": [");
		StringBuffer headers = new StringBuffer();
		while (custIterator.hasNext()) {
			HashMap customer = (HashMap) custIterator.next();
			returnJSON.append("\r\n{");
			String key = "";
			String value = "";
			// loop through all the map entries.
			Iterator it = customer.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry e = (Map.Entry) it.next();
				value = (String) e.getValue();
				key = (String) e.getKey();
				returnJSON.append("\r\n\"" + key + "\": \"" + value + "\",");
				//returnJSON.append("\r\n\"" + value + "\",");
				if(counter == 0) {
					headers.append("\r\n\"" +key+ "\",");
				}
			}
			// remove the last comma
			int lastCharIndex = returnJSON.length();
			returnJSON.deleteCharAt(lastCharIndex - 1);
			returnJSON.append("\r\n},");
			counter++;
		}
		// remove the last comma
		int lastCharIndex = returnJSON.length();
		returnJSON.deleteCharAt(lastCharIndex - 1);
		returnJSON.append("],");//for values

		//append headers...
		returnJSON.append("\r\n\"headers\": [");
		int ind = headers.length();
		headers.deleteCharAt(ind - 1);
		returnJSON.append(headers);
		returnJSON.append("]\r\n}");

		return returnJSON.toString();
	}
 public static String buildJSON(HashMap map, String title)
		{
			StringBuffer returnJSON = new StringBuffer("\r\n{\"" + title + "\":{");
			String key = "";
			String value = "";
			// loop through all the map entries.
			Iterator it = map.entrySet().iterator();

			while (it.hasNext())
			{
				Map.Entry e = (Map.Entry) it.next();
				value = (String) e.getValue();
				key = (String) e.getKey();
				returnJSON.append("\r\n\"" + key + "\": \"" + value + "\",");
			}
			// remove the last comma
			int lastCharIndex = returnJSON.length();
			returnJSON.deleteCharAt(lastCharIndex - 1);
			returnJSON.append("\r\n}}");
			return returnJSON.toString();
		} 
}
