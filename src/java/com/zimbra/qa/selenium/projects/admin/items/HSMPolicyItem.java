/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2012, 2013, 2014 Zimbra, Inc.
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software Foundation,
 * version 2 of the License.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see <http://www.gnu.org/licenses/>.
 * ***** END LICENSE BLOCK *****
 */
package com.zimbra.qa.selenium.projects.admin.items;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.zimbra.qa.selenium.framework.items.IItem;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;

public class HSMPolicyItem implements IItem {
	protected static Logger logger = LogManager.getLogger(IItem.class);

	protected Boolean emailItem;
	protected Boolean documentItem;
	protected Boolean tasksItem;
	protected Boolean appointmentItem;
	protected Boolean contactsItem;
	protected Boolean chatsItem;
	protected String olderThan;
	public TimeDuration duration;

	public enum TimeDuration {
		years, months, weeks, days, hours, minutes
	}

	public Boolean getEmailItem() {
		return emailItem;
	}

	public void setEmailItem(Boolean emailItem) {
		this.emailItem = emailItem;
	}

	public Boolean getDocumentItem() {
		return documentItem;
	}

	public void setDocumentItem(Boolean documentItem) {
		this.documentItem = documentItem;
	}

	public Boolean getTasksItem() {
		return tasksItem;
	}

	public void setTasksItem(Boolean tasksItem) {
		this.tasksItem = tasksItem;
	}

	public Boolean getAppointmentItem() {
		return appointmentItem;
	}

	public void setAppointmentItem(Boolean appointmentItem) {
		this.appointmentItem = appointmentItem;
	}

	public Boolean getContactsItem() {
		return contactsItem;
	}

	public void setContactsItem(Boolean contactsItem) {
		this.contactsItem = contactsItem;
	}

	public Boolean getChatsItem() {
		return chatsItem;
	}

	public void setChatsItem(Boolean chatsItem) {
		this.chatsItem = chatsItem;
	}

	public String getOlderThan() {
		return olderThan;
	}

	public void setOlderThan(String olderThan, TimeDuration duration) {
		this.olderThan = olderThan;
		this.duration = duration;
	}

	public TimeDuration getTimeDuration() {
		return duration;
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public void createUsingSOAP(ZimbraAccount account) throws HarnessException {
	}

	@Override
	public String prettyPrint() {
		return null;
	}

}
