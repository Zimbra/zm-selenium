/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2012, 2013, 2014, 2015, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.framework.util.staf;

import java.util.*;
import java.util.regex.*;
import com.ibm.staf.STAFMarshallingContext;
import com.zimbra.qa.selenium.framework.core.*;
import com.zimbra.qa.selenium.framework.util.*;

public class Stafpostqueue extends StafServicePROCESS {

	private static final String MailQueueIsEmpty = "Mail queue is empty";
	private static final String MailQueueIsUnavailable = "mail system is down";

	/**
	 * Wait for messages for the current test account to be delivered
	 *
	 */
	@SuppressWarnings("rawtypes")
	public void waitForPostqueue() throws HarnessException {

		if (ConfigProperties.getStringProperty("staf").equals("true")) {

			String command = "postqueue -p";
			String emailaddress = ClientSessionFactory.session().currentUserName().toLowerCase().trim();

			// emailaddress could be null or blank
			if ((emailaddress == null) || (emailaddress.equals(""))) {
				logger.warn("Unable to determine current user account. Use @testdomain.com instead");
				emailaddress = "@" + ConfigProperties.getStringProperty("testdomain");
			}

			int max = Integer.parseInt(ConfigProperties.getStringProperty("postqueue.sleep.max.msec"));
			int interval = Integer.parseInt(ConfigProperties.getStringProperty("postqueue.sleep.interval.msec"));
			for (int i = 0; i < max; i += interval) {

				// Check the server queue if it is empty
				if (execute(command)) {

					logger.debug("looking for " + emailaddress + " in " + this.StafResponse);

					if (this.StafResponse.contains(MailQueueIsEmpty)) {
						// Queue is empty
						return;
					}

					if (!this.StafResponse.contains(emailaddress)) {
						// Queue does not contain any messages for the test account
						return;
					}

					if (this.StafResponse.contains(MailQueueIsUnavailable)) {
						throw new HarnessException("Unable to check message queue.  MTA is down.  " + this.StafResponse);
					}
				}
				SleepUtil.sleep(interval);
			}

			logger.warn("Item never delivered, deleting from the queue");

			// Get only the postqueue output from the STAF result object
			STAFMarshallingContext mc = STAFMarshallingContext.unmarshall(this.StafResult.result);
			Map resultMap = (Map) mc.getRootObject();
			List returnedFileList = (List) resultMap.get("fileList");
			Map stdoutMap = (Map) returnedFileList.get(0);
			String output = (String) stdoutMap.get("data");
			Hashtable<String, String> idTable = new Hashtable<String, String>();

			// Look for all instances of a 10 digit hex number (The queue ID)
			// Pattern patter = Pattern.compile("\\b[0-9A-F]{10}\\b");
			Pattern pattern = Pattern.compile("^[0-9A-F]+\\b", Pattern.MULTILINE);
			Matcher matcher = pattern.matcher(output);

			while (matcher.find()) {
				logger.debug("matched: " + matcher.group() + " at " + matcher.start() + " to " + matcher.end());

				String id = matcher.group();
				int start = matcher.start();
				String value = output.substring(start);
				logger.debug("matched: " + id + " value " + value);

				// Determine if there is another message in the queue. If yes then delete the remainder from "value".
				Matcher next = pattern.matcher(value);
				if (matcher.find()) {
					int finish = next.start();
					value = value.substring(0, finish);
				}

				// Add this ID and element to the table
				idTable.put(id, value);
				logger.debug("matched: " + id + " value " + value);
			}

			// Separate all the current queue IDS associated with the test account
			ArrayList<String> qid = new ArrayList<String>();

			for (Map.Entry<String, String> entry : idTable.entrySet()) {
				if (entry.getValue().contains(emailaddress))
					qid.add(entry.getKey());
			}

			// Delete each ID one by one
			deletePostqueueItems(qid);

			throw new HarnessException("Message(s) never delivered from queue. IDs: " + qid.toString());

		} else {

			Boolean isMessageDelivered = false;
			int totalDeferredMessages = 0;
			SleepUtil.sleepMedium();

			for (int i=0; i<=5; i++) {
				totalDeferredMessages = 0;
				for (int mtaServer=0; mtaServer<ExecuteHarnessMain.mtaServers.size(); mtaServer++) {
					ZimbraAdminAccount.GlobalAdmin().soapSend("<GetMailQueueRequest xmlns='urn:zimbraAdmin'>"
							+ "<server name='" + ExecuteHarnessMain.mtaServers.get(mtaServer) + "'>"
			        		+	"<queue name='deferred'>"
			          		+		"<query limit='25' offset='0'/>"
			        		+	"</queue>"
			        		+ "</server>"
							+ "</GetMailQueueRequest>");
					String mtaDeferredMessages = ZimbraAdminAccount.GlobalAdmin().soapSelectValue("//admin:GetMailQueueResponse//admin:queue", "total");
					totalDeferredMessages = totalDeferredMessages + Integer.parseInt(mtaDeferredMessages);
				}
				if (totalDeferredMessages == 0) {
					isMessageDelivered = true;
					break;
				}
			}
			if (isMessageDelivered.equals(false)) {
				String mailqInfo;
				mailqInfo = "Mailq not empty, total deffered messages: " + totalDeferredMessages;
				logger.info(mailqInfo);
				//throw new HarnessException("Mailq not empty, total deffered messages: " + totalDeferredMessages);
			}
		}
	}

	public void deletePostqueueItems(ArrayList<String> ids) throws HarnessException {
		for (String id : ids) {
			this.deletePostqueueItem(id);
		}
	}

	private void deletePostqueueItem(String id) throws HarnessException {
		execute("/opt/zimbra/common/sbin/postsuper -d " + id);
	}

	public Stafpostqueue() {
		super();
		logger.info("new Stafpostqueue");
		StafService = "PROCESS";
	}

	public boolean execute(String command) throws HarnessException {
		setCommand(command);
		return (super.execute());
	}

	protected String setCommand(String command) {
		// We must convert the command to a special format
		if (command.trim().contains("postsuper"))
			// Running a command as super user.
			StafParms = String.format("START SHELL COMMAND \"su - -c '%s'\" RETURNSTDOUT RETURNSTDERR WAIT %d", command,
					this.getTimeout());
		else
			// Running a command as 'zimbra' user.
			StafParms = String.format("START SHELL COMMAND \"su - zimbra -c '%s'\" RETURNSTDOUT RETURNSTDERR WAIT %d",
					command, this.getTimeout());

		return (getStafCommand());
	}
}