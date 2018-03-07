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
package com.zimbra.qa.selenium.framework.ui;

/**
 * The <code>Checkbox</code> class defines constants that represent general
 * CheckBoxs in the client apps.
 * <p>
 * <p>
 * Action constant names start with "C_" for CheckBoxs and take the general
 * format <code>C_PAGE_TEXT</code>, where "Page" is the application name such as
 * MAIL, ADDRESSBOOK, and "Text" is the displayed English text on the Checkbox.
 * For non-page specific CheckBoxes, the "Page" is not specified.
 * <p>
 * The action constants can be used in page methods, for example:
 * 
 * <pre>
 * {@code
 * // Select check box for CLIENTS_IP_ADDRESS on MTA configuration page
 * app.zPageMail.zCheckboxSet(Checkbox.C_MTA_CLIENTS_IP_ADDRESS, true);
 * }
 * </pre>
 * <p>
 *
 * @author Jitesh Singh
 *
 */
public class Checkbox {

	// General CheckBoxes

	// Calender checkBoxes
	public static final Checkbox C_CALENDAR = new Checkbox("C_CALENDAR");
	public static final Checkbox C_TRASH = new Checkbox("C_TRASH");

	// Admin UI MTA configuration page checkboxes
	public static final Checkbox C_MTA_CLIENTS_IP_ADDRESS = new Checkbox("C_MTA_CLIENTS_IP_ADDRESS");
	public static final Checkbox C_MTA_ENABLE_MILTER_SERVER = new Checkbox("C_MTA_ENABLE_MILTER_SERVER");
	public static final Checkbox C_MTA_TLS_AUTHENTICATION_ONLY = new Checkbox("C_MTA_TLS_AUTHENTICATION_ONLY");
	public static final Checkbox C_MTA_ENABLE_ARCHIVING = new Checkbox("C_MTA_ENABLE_ARCHIVING");
	
	// Ajax Preferences --> Accounts
	public static final Checkbox C_REPLY_TO_SENT_MESSAGE = new Checkbox("C_REPLY_TO_SENT_MESSAGE");

	// Checkbox properties
	private final String ID;

	protected Checkbox(String id) {
		this.ID = id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ID == null) ? 0 : ID.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {

		if (this == obj)
			return true;

		if (obj == null)
			return false;

		if (getClass() != obj.getClass())
			return false;

		Checkbox other = (Checkbox) obj;
		if (ID == null) {
			if (other.ID != null)
				return false;
		} else if (!ID.equals(other.ID))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return ID;
	}
}