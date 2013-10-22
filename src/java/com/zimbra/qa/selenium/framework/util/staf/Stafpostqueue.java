/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2012, 2013 Zimbra Software, LLC.
 * 
 * The contents of this file are subject to the Zimbra Public License
 * Version 1.4 ("License"); you may not use this file except in
 * compliance with the License.  You may obtain a copy of the License at
 * http://www.zimbra.com/license.
 * 
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied.
 * ***** END LICENSE BLOCK *****
 */
package com.zimbra.qa.selenium.framework.util.staf;

import java.util.*;
import java.util.regex.*;
import com.ibm.staf.STAFException;
import com.ibm.staf.STAFHandle;
import com.ibm.staf.STAFMarshallingContext;
import com.ibm.staf.STAFResult;
import com.zimbra.qa.selenium.framework.core.*;
import com.zimbra.qa.selenium.framework.util.*;

public class Stafpostqueue extends StafServicePROCESS {

	private static final String MailQueueIsEmpty = "Mail queue is empty";
	private static final String MailQueueIsUnavailable = "mail system is down"; // 	postqueue: fatal: Queue report unavailable - mail system is down
	private static List<String> zimbraMtaServers =  new ArrayList<String>();
	private String StafAdminServer = null;

	public String getStafAdminServer() {
		return StafAdminServer;
	}

	public void setStafAdminServer(String stafAdminServer) {
		StafAdminServer = stafAdminServer;
	}

	/**
	 * Wait for messages for the current test account to be delivered
	 * @throws HarnessException
	 */
	@SuppressWarnings("unchecked")
	public void waitForPostqueue() throws HarnessException {
		List<String> servers = null;
				
		if(zimbraMtaServers == null || zimbraMtaServers.isEmpty()){
			servers = getMtaServers(StafAdminServer);
		}else{
			servers = zimbraMtaServers;
		}
		
		// Start: Dev env hack
		if ( "false".equalsIgnoreCase(ZimbraSeleniumProperties.getStringProperty("postqueue.use.staf", "true")) ) {
			logger.info("In dev environment, waiting for message to be delivered ...");
			int delay = Integer.parseInt(ZimbraSeleniumProperties.getStringProperty("postqueue.sleep.nonstaf.msec", "5000"));
			SleepUtil.sleep(delay);
			return;
		}
		// End: Dev env hack
		
		String command = "postqueue -p";
		String emailaddress = ClientSessionFactory.session().currentUserName().toLowerCase().trim();
		
		// emailaddress could be null or blank
		// if so, set it to @domain.com
		if ( (emailaddress == null) || (emailaddress.equals("")) ) {
			logger.warn("Unable to determien current user account.  Use @testdomain.com instead");
			emailaddress = "@" + ZimbraSeleniumProperties.getStringProperty("testdomain", "testdomain.com");
		}

		int max = Integer.parseInt(ZimbraSeleniumProperties.getStringProperty("postqueue.sleep.max.msec", "30000"));
		int interval = Integer.parseInt(ZimbraSeleniumProperties.getStringProperty("postqueue.sleep.interval.msec", "1000"));
		for (int i = 0; i < max; i += interval) {
			
			// Check the server queue if it is empty
			for(int y = 0; y < servers.size(); y++){
				if (execute(command, servers.get(y))) {
				
					logger.debug("looking for "+ emailaddress +" in "+ this.StafResponse);

					if (this.StafResponse.contains(MailQueueIsEmpty) ) {
						// Queue is empty
						if(y == servers.size()-1){
							return;
						}else{
							continue;
						}
					} 
				
					if (!this.StafResponse.contains(emailaddress)) {						
						if(y == servers.size()-1){
							return;		
						}
					}else{
						// Queue contains messages for the test account
						break;
					}
				
					if ( this.StafResponse.contains(MailQueueIsUnavailable)) {
						throw new HarnessException("Unable to check message queue.  MTA is down.  "+ this.StafResponse);
					}
				
				}	
			}		
			
			// Wait a bit for the message to be delivered
			SleepUtil.sleep(interval);			

		}

			
		logger.warn("Item never delivered, deleting from the queue");

		// Get only the postqueue output from the STAF result object
     	STAFMarshallingContext mc = STAFMarshallingContext.unmarshall(this.StafResult.result);        	
     	Map resultMap = (Map)mc.getRootObject();
    	List returnedFileList = (List)resultMap.get("fileList");
    	Map stdoutMap = (Map)returnedFileList.get(0);
    	String output = (String)stdoutMap.get("data");

		/* Example "output":

    	-Queue ID- --Size-- ----Arrival Time---- -Sender/Recipient-------
    	A391E167584     1164 Thu Jun 30 11:23:15  zimbra@server
    	(delivery temporarily suspended: connect to server[10.137.245.174]:7025: Connection refused)
    	                                         admin@server

    	03E3A16757E     2012 Thu Jun 30 11:25:52  enus130945819697629@testdomain.com
    	(delivery temporarily suspended: connect to server[10.137.245.174]:7025: Connection refused)
    	                                         enus130945833860146@testdomain.com

    	48E6016757B     1164 Thu Jun 30 11:21:46  zimbra@server
    	  (connect to server[10.137.245.174]:7025: Connection refused)
    	                                         admin@server

    	9A588167581     1164 Thu Jun 30 11:23:15  zimbra@server
    	(delivery temporarily suspended: connect to server[10.137.245.174]:7025: Connection refused)
    	                                         admin@server


    	-- 17 Kbytes in 4 Requests.

    				*/

    	//
    	// Determine each queue ID
    	// ID's are 10 hex digits
    	//
    	
    	// Keep a table of strings to entries, i.e.
    	// key = 48E6016757B
    	// value = 48E6016757B     1164 Thu Jun 30 11:21:46  enus1231@server\n(connect to ... 
    	//
    	Hashtable<String, String> idTable = new Hashtable<String, String>();

    	// Look for all instances of a 10 digit hex number (The queue ID)
    	// Pattern patter = Pattern.compile("\\b[0-9A-F]{10}\\b");
    	Pattern pattern = Pattern.compile("^[0-9A-F]+\\b", Pattern.MULTILINE);
    	Matcher matcher = pattern.matcher(output);
    	
    	while (matcher.find()) {
    		logger.debug("matched: "+ matcher.group() +" at "+ matcher.start() + " to "+ matcher.end());

    		String id = matcher.group();
    		int start = matcher.start();
    		String value = output.substring(start);
    		logger.debug("matched: "+ id + " value "+ value);

    		// Determine if there is another message in the queue
    		// And, if yes, then delete the remainder from "value"
    		Matcher next = pattern.matcher(value);
    		if ( matcher.find() ) {
    			int finish = next.start();
    			value = value.substring(0, finish);
    		}
    		
    		// Add this ID and element to the table
    		idTable.put(id, value);
    		logger.debug("matched: "+ id + " value "+ value);
    	}

		// Separate all the current queue IDS associated with the test account
		ArrayList<String> qid = new ArrayList<String>(); 
		
		for (Map.Entry<String, String> entry : idTable.entrySet()) {
			if ( entry.getValue().contains(emailaddress) )
				qid.add(entry.getKey());
		}

		// Delete each ID one by one
		deletePostqueueItems(qid);
		
		throw new HarnessException("Message(s) never delivered from queue.  IDs: "+ qid.toString());
			
	}

	public void deletePostqueueItems(ArrayList<String> ids)
			throws HarnessException {
		
		// STAF <SERVER> PROCESS START COMMAND
		// /opt/zimbra/postfix/sbin/postsuper PARMS -d <queue_id> RETURNSTDOUT
		// RETURNSTDERR WAIT 60000
		
		for (String id : ids)
			this.deletePostqueueItem(id);
		
	}
	
	private void deletePostqueueItem(String id) throws HarnessException {

		// STAF <SERVER> PROCESS START COMMAND
		// /opt/zimbra/postfix/sbin/postsuper PARMS -d <queue_id> RETURNSTDOUT
		// RETURNSTDERR WAIT 60000

		execute("/opt/zimbra/postfix/sbin/postsuper -d " + id);

	}


	public Stafpostqueue() {
		super();
		logger.info("new Stafpostqueue");
		StafService = "PROCESS";
		String server = ZimbraSeleniumProperties.getStringProperty("adminName", "local");
		if(!server.equals("local")){
			setStafAdminServer(server.split("@")[1]);
		}
	}

	public boolean execute(String command, String server) throws HarnessException {
		setCommand(command);
				
		STAFHandle handle = null;
		
		try
		{
			
			handle = new STAFHandle(Stafpostqueue.class.getName());
			
	        try
	        {
	        	
	        	logger.info("STAF Command example: " + getStafCommand());
	        	
	        	logger.info("executing on: " + server);
	        		
	        	StafResult = handle.submit2(server, StafService, StafParms);
	            
	        	if (StafResult == null){
	        		throw new HarnessException("StafResult was null");
	        	}
	        	
	        	logger.info("STAF Response Code: "+ StafResult.rc);

	        	if ( StafResult.rc == STAFResult.AccessDenied ) {
	        		// Common error in WDC.  Log a helper message.
	        		logger.error("On the server, use: staf local trust set machine *.eng.server.com level 5");
	        	}

	        	if ( StafResult.rc != STAFResult.Ok ) {
	        		throw new HarnessException("Invalid STAF response code ("+ StafResult.rc +"): "+ StafResult.result);
	        	}

	            if ( (StafResult.result != null) && (!StafResult.result.trim().equals("")) ) {
	            	
	            	//logger.info(StafResult.result);
	            		        	
	            	if ( STAFMarshallingContext.isMarshalledData(StafResult.result) ){
	            		STAFMarshallingContext mc = STAFMarshallingContext.unmarshall(StafResult.result);
	            		
	            		// Get the entire response
	            		StafResponse = STAFMarshallingContext.formatObject(mc);	            				
	            	}else{
	            		StafResponse = StafResult.result;
	            	}
	            
	            	logger.debug(StafResponse);	            		
	            }
	            	        	
	            return (StafResult.rc == STAFResult.Ok);
 
			} finally {
	        	
				logger.info("STAF Response: " + StafResponse);
				
				if (handle != null )
					handle.unRegister();
				
			}
        
		}
		catch (STAFException e)
		{
        	throw new HarnessException("Error registering or unregistering with STAF, RC: " + e.rc, e);
		}
	        	
	}
	
	private List<String> getMtaServers(String mailboxServer) {	
		final String STAF_ZIMBRA_SERVICE = "PROCESS";
		final String STAF_ZIMBRA_COMMAND = "/bin/su ";
		final String ZIMBRA_COMMAND = "zmprov gs "+ mailboxServer +" zimbraSmtpHostname | grep zimbraSmtpHostname";
		final String STAF_ZIMBRA_PARMS = "\"- zimbra -c \\\""+ ZIMBRA_COMMAND +"\\\"\"";
		
		STAFHandle handle = null;
       	
       	try
        {
       		
            handle = new STAFHandle(Stafpostqueue.class.getName());
            logger.debug("My handle is: " + handle.getHandle());
             
	        try
	        {
	        	// Build the STAF PROCESS command
	        	StringBuffer stafCommand = new StringBuffer();
	        	stafCommand.append(" START COMMAND ");
	        	stafCommand.append(STAF_ZIMBRA_COMMAND);
	        	stafCommand.append(" PARMS " + STAF_ZIMBRA_PARMS);
	        	stafCommand.append(" RETURNSTDOUT RETURNSTDERR WAIT ");

	        	logger.info("Execute STAF " + mailboxServer + " " + STAF_ZIMBRA_SERVICE + " " + stafCommand);
	            
	            STAFResult stafResult = handle.submit2(mailboxServer, STAF_ZIMBRA_SERVICE, stafCommand.toString());

            	// First, check for STAF errors, like unable to contact host
            	if ( stafResult.rc != STAFResult.Ok ) {
            		
            		logger.warn("STAF return code: " + stafResult.rc);
	            	
	            	// Fall back to the mailbox host
            		if(!zimbraMtaServers.contains(mailboxServer)){
            			zimbraMtaServers.add(mailboxServer);
            		}
            	   	return (zimbraMtaServers);	            	

	            }	            

            	// Unwind the result object.
            	// zmlmtpinject returns 0 on success and >0 on failure
	            //
             	STAFMarshallingContext mc = STAFMarshallingContext.unmarshall(stafResult.result);
             	logger.info("zmprov results:\n" + mc.toString());
            	
             	Map resultMap = (Map)mc.getRootObject();
            	List returnedFileList = (List)resultMap.get("fileList");
            	Map stdoutMap = (Map)returnedFileList.get(0);
            	String processOut = (String)stdoutMap.get("data");
            	
            	if ( processOut != null ) {
            		
	            	// Log the postqueue status:
            		logger.info("zmprov:\n" + processOut);
            		
            		String[] servers = processOut.trim().split("zimbraSmtpHostname:");
            		String server = null;
            		for(String str:servers){
            			server = str.trim();
            			if(!server.isEmpty() && !zimbraMtaServers.contains(server))
            			zimbraMtaServers.add(server);
            		}
            		         		
            		return (zimbraMtaServers);
            		
            	}
	        } finally {
	        	
	            try {
					handle.unRegister();
				} catch (STAFException e) {
					logger.warn("Error unregistering with STAF, RC:" + e.rc, e);
				}
	        
	        }
			
        } catch (STAFException e) {
        	
        	logger.warn("Error registering or unregistering with STAF, RC:" + e.rc, e);
        	logger.warn("Example:  RC: 21 is STAFNotRunning");
        	logger.warn("See Return Codes here:  http://staf.sourceforge.net/current/STAFJava.htm#Header_STAFResult");
                   
        } 

       return (zimbraMtaServers);	
		
	}

	protected String setCommand(String command) {
		// We must convert the command to a special format
		if (command.trim().contains("postsuper"))
			// Running a command as super user.
			StafParms = String
					.format(
							"START SHELL COMMAND \"su - -c '%s'\" RETURNSTDOUT RETURNSTDERR WAIT %d",
							command, this.getTimeout());
		else
			// Running a command as 'zimbra' user.
			StafParms = String
					.format(
							"START SHELL COMMAND \"su - zimbra -c '%s'\" RETURNSTDOUT RETURNSTDERR WAIT %d",
							command, this.getTimeout());

		return (getStafCommand());
	}
}
