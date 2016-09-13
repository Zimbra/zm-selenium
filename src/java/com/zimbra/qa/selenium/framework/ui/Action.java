/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2012, 2013, 2014, 2016 Synacor, Inc.
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
 * The <code>Action</code> class defines constants that represent
 * general actions in the client apps.
 * <p>
 * This class defines constants for
 * <ol>
 * <li>Mouse actions, such as click or ctrl-click</li>
 * <li>Zimbra object actions, such as check a checkbox or flag a mail</li>
 * </ol>
 * <p>
 * Action constant names start with "A_" and take the general format
 * <code>A_PAGE_ACTION</code>,
 * where "Page" is the application name such as MAIL, ADDRESSBOOK and
 * "Action" is the general description of the action.  For non-page
 * specific Actions, "Page" is not specified.
 * <p>
 * The action constants can be used in page methods, for example:
 * <pre>
 * {@code
 * String subject = "foo123";
 * app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);
 * }
 * </pre>
 * <p>
 * 
 * @author Matt Rhoades
 *
 */
public class Action {

	// General actions
	public static final Action A_LEFTCLICK = new Action("A_LEFTCLICK");
	public static final Action A_SHIFTSELECT = new Action("A_SHIFTSELECT");
	public static final Action A_CTRLSELECT = new Action("A_CTRLSELECT");
	public static final Action A_RIGHTCLICK = new Action("A_RIGHTCLICK");
	public static final Action A_DOUBLECLICK = new Action("A_DOUBLECLICK");
	public static final Action A_HOVEROVER = new Action("A_HOVEROVER");
	public static final Action A_CHECKBOX = new Action("A_CHECKBOX");
	public static final Action A_UNCHECKBOX = new Action("A_UNCHECKBOX");

	// Mail page actions
	public static final Action A_MAIL_CHECKBOX = new Action("A_MAIL_CHECKBOX");
	public static final Action A_MAIL_UNCHECKBOX = new Action("A_MAIL_UNCHECKBOX");
	public static final Action A_MAIL_FLAG = new Action("A_MAIL_FLAG");
	public static final Action A_MAIL_UNFLAG = new Action("A_MAIL_UNFLAG");
	public static final Action A_MAIL_EXPANDCONVERSATION = new Action("A_MAIL_EXPANDCONVERSATION");
	public static final Action A_MAIL_COLLAPSECONVERSATION = new Action("A_MAIL_COLLAPSECONVERSATION");
	
	// Tree actions
	public static final Action A_TREE_EXPAND = new Action("A_TREE_EXPAND");
	public static final Action A_TREE_COLLAPSE = new Action("A_TREE_COLLAPSE");
	public static final Action A_TREE_CHECKBOX = new Action("A_TREE_CHECKBOX");

	// Briefcase page actions
	public static final Action A_BRIEFCASE_CHECKBOX = new Action("A_BRIEFCASE_CHECKBOX");
	public static final Action A_BRIEFCASE_HEADER_CHECKBOX = new Action("A_BRIEFCASE_HEADER_CHECKBOX");
	
	// Bubble actions
	public static final Action A_EXPAND = new Action("A_EXPAND");
	public static final Action A_REMOVE = new Action("A_REMOVE");

	private final String ID;
	
	protected Action(String id) {
		this.ID = id;
	}

	@Override
	public String toString() {
		return ID;
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
		Action other = (Action) obj;
		if (ID == null) {
			if (other.ID != null)
				return false;
		} else if (!ID.equals(other.ID))
			return false;
		return true;
	}

}
