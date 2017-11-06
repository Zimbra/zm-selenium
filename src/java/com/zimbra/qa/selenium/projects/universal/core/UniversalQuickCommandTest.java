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
package com.zimbra.qa.selenium.projects.universal.core;

import java.util.ArrayList;

import org.apache.log4j.*;

import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.items.QuickCommand.*;
import com.zimbra.qa.selenium.framework.util.*;

/**
 * A base class that creates some basic Quick Command shortcuts in the mailbox.  This
 * class should only be used when testing Quick Commands.
 * 
 * 
 * @author Matt Rhoades
 *
 */
public class UniversalQuickCommandTest extends UniversalCommonTest {
	protected static Logger logger = LogManager.getLogger(UniversalQuickCommandTest.class);

	private QuickCommand command1;
	private QuickCommand command2;
	private QuickCommand command3;


	public UniversalQuickCommandTest() {
		logger.info("New "+ UniversalQuickCommandTest.class.getCanonicalName());
		
		command1 = null;
		command2 = null;
		command3 = null;
		
	}

	/**
	 * Quick Command:
	 * Item Type: Mail
	 * Action 1: Tag
	 * Action 2: Mark Read
	 * Action 3: Mark Flagged
	 * Action 4: Move To Subfolder
	 * @throws HarnessException
	 */
	protected QuickCommand getQuickCommand01() throws HarnessException {
		
		if ( command1 != null ) {
			// Command already exists, just return it
			return (command1);
		}
		
		
		// Create a tag
		String tagname = "tag" + ConfigProperties.getUniqueString();
		ZimbraAccount.AccountZCS().soapSend(
					"<CreateTagRequest xmlns='urn:zimbraMail'>"
				+		"<tag name='"+ tagname +"' color='1' />"
				+	"</CreateTagRequest>");

		TagItem tag = TagItem.importFromSOAP(ZimbraAccount.AccountZCS(), tagname);
		ZAssert.assertNotNull(tag, "Verify the tag was created");

		// Create a subfolder
		String foldername = "folder" + ConfigProperties.getUniqueString();
		ZimbraAccount.AccountZCS().soapSend(
					"<CreateFolderRequest xmlns='urn:zimbraMail'>"
				+		"<folder name='"+ foldername +"' l='1' view='message'/>"
				+	"</CreateFolderRequest>");

		FolderItem folder = FolderItem.importFromSOAP(ZimbraAccount.AccountZCS(), foldername);
		ZAssert.assertNotNull(folder, "Verify the subfolder is available");

		// Create the list of actions
		ArrayList<QuickCommandAction> actions = new ArrayList<QuickCommandAction>();
		actions.add(new QuickCommandAction(QuickCommandAction.TypeId.actionTag, tag.getId(), true));
		actions.add(new QuickCommandAction(QuickCommandAction.TypeId.actionFlag, "read", true));
		actions.add(new QuickCommandAction(QuickCommandAction.TypeId.actionFlag, "flagged", true));
		actions.add(new QuickCommandAction(QuickCommandAction.TypeId.actionFileInto, folder.getId(), true));


		String name = "name" + ConfigProperties.getUniqueString();
		String description = "description" + ConfigProperties.getUniqueString();
		
		command1 = new QuickCommand(name, description, ItemTypeId.MSG, true);
		command1.addActions(actions);

		return (command1);
	}
	
	/**
	 * Quick Command:
	 * Item Type: Contact
	 * Action 1: Tag
	 * Action 2: Move To Subfolder
	 * @throws HarnessException
	 */
	protected QuickCommand getQuickCommand02() throws HarnessException {

		
		if ( command2 != null ) {
			// Command already exists, just return it
			return (command2);
		}
		
		
		// Create a tag
		String tagname = "tag" + ConfigProperties.getUniqueString();
		ZimbraAccount.AccountZCS().soapSend(
					"<CreateTagRequest xmlns='urn:zimbraMail'>"
				+		"<tag name='"+ tagname +"' color='1' />"
				+	"</CreateTagRequest>");

		TagItem tag = TagItem.importFromSOAP(ZimbraAccount.AccountZCS(), tagname);
		ZAssert.assertNotNull(tag, "Verify the tag was created");

		// Create a subfolder
		String foldername = "folder" + ConfigProperties.getUniqueString();
		ZimbraAccount.AccountZCS().soapSend(
					"<CreateFolderRequest xmlns='urn:zimbraMail'>"
				+		"<folder name='"+ foldername +"' l='1' view='contact'/>"
				+	"</CreateFolderRequest>");

		FolderItem folder = FolderItem.importFromSOAP(ZimbraAccount.AccountZCS(), foldername);
		ZAssert.assertNotNull(folder, "Verify the subfolder is available");

		// Create the list of actions
		ArrayList<QuickCommandAction> actions = new ArrayList<QuickCommandAction>();
		actions.add(new QuickCommandAction(QuickCommandAction.TypeId.actionTag, tag.getId(), true));
		actions.add(new QuickCommandAction(QuickCommandAction.TypeId.actionFileInto, folder.getId(), true));


		String name = "name" + ConfigProperties.getUniqueString();
		String description = "description" + ConfigProperties.getUniqueString();
		
		command2 = new QuickCommand(name, description, ItemTypeId.CONTACT, true);
		command2.addActions(actions);

		return (command2);
	}
	
	/**
	 * Quick Command:
	 * Item Type: Appointment
	 * Action 1: Tag
	 * Action 2: Mark Read
	 * Action 3: Mark Flagged
	 * Action 4: Move To Subfolder
	 * @throws HarnessException
	 */
	protected QuickCommand getQuickCommand03() throws HarnessException {
		
		if ( command3 != null ) {
			// Command already exists, just return it
			return (command3);
		}
		
		
		// Create a tag
		String tagname = "tag" + ConfigProperties.getUniqueString();
		ZimbraAccount.AccountZCS().soapSend(
					"<CreateTagRequest xmlns='urn:zimbraMail'>"
				+		"<tag name='"+ tagname +"' color='1' />"
				+	"</CreateTagRequest>");

		TagItem tag = TagItem.importFromSOAP(ZimbraAccount.AccountZCS(), tagname);
		ZAssert.assertNotNull(tag, "Verify the tag was created");

		// Create a subfolder
		String foldername = "folder" + ConfigProperties.getUniqueString();
		ZimbraAccount.AccountZCS().soapSend(
					"<CreateFolderRequest xmlns='urn:zimbraMail'>"
				+		"<folder name='"+ foldername +"' l='1' view='appointment'/>"
				+	"</CreateFolderRequest>");

		FolderItem folder = FolderItem.importFromSOAP(ZimbraAccount.AccountZCS(), foldername);
		ZAssert.assertNotNull(folder, "Verify the subfolder is available");

		// Create the list of actions
		ArrayList<QuickCommandAction> actions = new ArrayList<QuickCommandAction>();
		actions.add(new QuickCommandAction(QuickCommandAction.TypeId.actionTag, tag.getId(), true));
		actions.add(new QuickCommandAction(QuickCommandAction.TypeId.actionFlag, "read", true));
		actions.add(new QuickCommandAction(QuickCommandAction.TypeId.actionFlag, "flagged", true));
		actions.add(new QuickCommandAction(QuickCommandAction.TypeId.actionFileInto, folder.getId(), true));


		String name = "name" + ConfigProperties.getUniqueString();
		String description = "description" + ConfigProperties.getUniqueString();
		
		command3 = new QuickCommand(name, description, ItemTypeId.APPT, true);
		command3.addActions(actions);

		return (command3);
	}
	

	/**
	 * Set up basic quick commands for all combinations 
	 * @throws HarnessException
	 */
	@Bugs(ids = "71389")	// Hold off on GUI implementation of Quick Commands in 8.X
//	@BeforeClass( groups = { "always" } )
	public void addQuickCommands() throws HarnessException {
		logger.info("addQuickCommands: start");
		

		// Create a quick command in the user preferences
		ZimbraAccount.AccountZCS().soapSend(
				"<ModifyPrefsRequest xmlns='urn:zimbraAccount'>"
				+		"<pref name='zimbraPrefQuickCommand'>"+ this.getQuickCommand01().toString() +"</pref>"
				+		"<pref name='zimbraPrefQuickCommand'>"+ this.getQuickCommand02().toString() +"</pref>"
				+		"<pref name='zimbraPrefQuickCommand'>"+ this.getQuickCommand03().toString() +"</pref>"
				+	"</ModifyPrefsRequest>");

		
		// Re-login to pick up the new preferences
		app.zPageMain.zRefreshMainUI();

		// The UniversalCommonTest.commonTestBeforeMethod() method will log into the client
		logger.info("addQuickCommands: finish");
	}


}
