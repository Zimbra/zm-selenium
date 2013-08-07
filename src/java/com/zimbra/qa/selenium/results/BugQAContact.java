/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2013 Zimbra Software, LLC.
 * 
 * The contents of this file are subject to the Zimbra Public License
 * Version 1.3 ("License"); you may not use this file except in
 * compliance with the License.  You may obtain a copy of the License at
 * http://www.zimbra.com/license.
 * 
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied.
 * ***** END LICENSE BLOCK *****
 */
package com.zimbra.qa.selenium.results;

import java.io.*;
import java.util.*;


public class BugQAContact extends BugDataFile {

	/**
	 * Return the current list of "Bug ID" to "QA Contact"
	 * @return
	 * @throws IOException
	 */
	public static Map<String, String> getQAContactData() throws IOException {
		BugQAContact engine = new BugQAContact();
		return (engine.getData());
	}
	

	
	
	protected static final String DataFilename = "bugQaContact.txt";
	
	protected static Map<String, String> bugQAContactMap = new HashMap<String, String>();
	
	protected BugQAContact() {
		logger.info("new " + BugQAContact.class.getCanonicalName());
	}


	protected Map<String, String> getData() throws IOException {
				
		// New datafile was found.  Clear the map
		Map<String, String> bugQAContactMap = new HashMap<String, String>();
		
		// Read the file and build the map
		BufferedReader reader = null;
		String line;
		
		try {
			
			reader = new BufferedReader(new FileReader(getDatafile(DataFilename)));
			while ( (line=reader.readLine()) != null ) {
				
				// Example: 42337	sarang@zimbra.com

				String[] values = line.split("\\s");
				if ( values.length != 2 ) {
					logger.warn("bugQAContact: invalid line: "+ line);
					continue;
				}
				
				String bugid = values[0];
				String bugcontact = values[1];
				
				bugQAContactMap.put(bugid, bugcontact);
				logger.debug("bugQAContact: put "+ line);

			}
			
		} finally {
			if ( reader != null )
				reader.close();
			reader = null;
		}


		return (bugQAContactMap);
	}
	


}
