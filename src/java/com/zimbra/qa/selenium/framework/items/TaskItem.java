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
package com.zimbra.qa.selenium.framework.items;

import org.apache.log4j.*;
import com.zimbra.common.soap.Element;
import com.zimbra.qa.selenium.framework.util.*;

/**
 * This class represents a task item
 *
 * @author Matt Rhoades
 *
 */
public class TaskItem implements IItem {

	protected static Logger logger = LogManager.getLogger(IItem.class);

	// //
	// START: GUI Data
	// //

	public boolean gIsChecked;

	public String gTags; // TODO: how to represent the icon?

	public String gPriority; // TODO: how to represent the icon?

	public boolean gHasAttachments;

	public String gSubject;

	public String gStatus;

	public String gPercentComplete;

	public String gDueDate;

	public String gtaskBody;
	public String gtaskHtmlBody;

	// //
	// FINISH: GUI Data
	// //

	/**
	 * Create a mail item
	 */
	public TaskItem() {
		populateTaskData();
	}

	public void setName(String name) {
		gSubject = name;
	}

	public String getName() {
		return (gSubject);
	}

	public void setPriority(String p) {
		gPriority = p;
	}

	public String getPriority() {
		return (gPriority);
	}

	public String gettaskBody() {

		return gtaskBody;
	}

	public void settaskBody(String taskBody) {
		gtaskBody = taskBody;
	}

	public String getHtmlTaskBody() {

		return gtaskHtmlBody;
	}

	public void setHtmlTaskBody(String taskHtmlBody) {
		gtaskHtmlBody = taskHtmlBody;
	}

	public void populateTaskData() {
		// taskSubject = "subject" + ConfigProperties.getUniqueString();
		// taskBody = "Body" + ConfigProperties.getUniqueString();
	}

	public String myId;

	public String getId() {
		return (myId);
	}

	public void setId(String id) {
		myId = id;
	}

	public static TaskItem importFromSOAP(Element GetMsgResponse) throws HarnessException {

		if (GetMsgResponse == null)
			throw new HarnessException("Element cannot be null");

		TaskItem task = null;

		try {

			// Make sure we only have the GetMsgResponse part
			Element getMsgResponse = ZimbraAccount.SoapClient.selectNode(GetMsgResponse, "//mail:GetMsgResponse");

			if (getMsgResponse == null)
				throw new HarnessException("Element does not contain GetMsgResponse");
			Element m = ZimbraAccount.SoapClient.selectNode(getMsgResponse, "//mail:comp");
			if (m == null)
				throw new HarnessException("Element does not contain an m element");

			// Create the object
			task = new TaskItem();
			// Set task body
			task.settaskBody(m.getAttribute("desc", null));
			// Set task name
			task.setName(m.getAttribute("name", null));
			// Set task id
			task.setId(m.getAttribute("calItemId", null));
			task.setHtmlTaskBody(m.getAttribute("descHtml", null));
			task.setPriority(m.getAttribute("priority", null));
			// TODO: parse the <m/> element

			return (task);

		} catch (Exception e) {
			throw new HarnessException("Could not parse GetMsgResponse: " + GetMsgResponse.prettyPrint(), e);
		} finally {
			if (task != null)
				logger.info(task.prettyPrint());
		}

	}

	public static TaskItem importFromSOAP(ZimbraAccount account, String query) throws HarnessException {

		try {

			account.soapSend("<SearchRequest xmlns='urn:zimbraMail' types='task' >" + "<query>" + query + "</query>"
					+ "</SearchRequest>");

			Element[] results = account.soapSelectNodes("//mail:SearchResponse/mail:task");
			if (results.length != 1)
				throw new HarnessException("Query should return 1 result, not " + results.length);

			String invId = account.soapSelectValue("//mail:SearchResponse/mail:task", "invId");

			account.soapSend(
					"<GetMsgRequest xmlns='urn:zimbraMail'>" + "<m id='" + invId + "' />" + "</GetMsgRequest>");
			Element getMsgResponse = account.soapSelectNode("//mail:GetMsgResponse", 1);

			// Using the response, create this item
			return (importFromSOAP(getMsgResponse));

		} catch (Exception e) {
			throw new HarnessException(
					"Unable to import using SOAP query(" + query + ") and account(" + account.EmailAddress + ")", e);
		}
	}

	public void createUsingSOAP(ZimbraAccount account) throws HarnessException {
		throw new HarnessException("implement me!");
	}

	@Override
	public String prettyPrint() {
		StringBuilder sb = new StringBuilder();
		sb.append(TaskItem.class.getSimpleName()).append('\n');
		sb.append("Checked: ").append(gIsChecked).append('\n');
		sb.append("Tags: ").append(gTags).append('\n');
		sb.append("Priority: ").append(gPriority).append('\n');
		sb.append("Attachments: ").append(gHasAttachments).append('\n');
		sb.append("Subject: ").append(gSubject).append('\n');
		sb.append("Status: ").append(gStatus).append('\n');
		sb.append("% Complete: ").append(gPercentComplete).append('\n');
		sb.append("Due Date: ").append(gDueDate).append('\n');
		return (sb.toString());
	}
}