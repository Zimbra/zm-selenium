package com.zimbra.qa.selenium.projects.touch.tests.mail.mail.message;

	import java.awt.AWTException;

import org.testng.annotations.*;

import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.touch.core.PrefGroupMailByConversationTest;
//import com.zimbra.qa.selenium.projects.touch.core.PrefGroupMailByMessageTest;

	public class CancelMoveMessage extends PrefGroupMailByConversationTest{

		public CancelMoveMessage() {
			logger.info("New "+ CancelMoveMessage.class.getCanonicalName());
		}
			@Test(	description = "cancel move mail operation",
					groups = { "t1" })
			
			
			public void CancelMoveMail_01() throws HarnessException, AWTException {

				String subject = "subject"+ ZimbraSeleniumProperties.getUniqueString();
				FolderItem userRoot = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.UserRoot);
				String folderName = "folder" + ZimbraSeleniumProperties.getUniqueString();
				app.zGetActiveAccount().soapSend(
						"<CreateFolderRequest xmlns='urn:zimbraMail'>" +
		                	"<folder name='"+ folderName +"' l='"+ userRoot.getId() +"'/>" +
		                "</CreateFolderRequest>");
				FolderItem pagemail = FolderItem.importFromSOAP(app.zGetActiveAccount(), folderName);

				// Send a message to the account
				ZimbraAccount.AccountA().soapSend(
							"<SendMsgRequest xmlns='urn:zimbraMail'>" +
								"<m>" +
									"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
									"<su>"+ subject +"</su>" +
									"<mp ct='text/plain'>" +
										"<content>content"+ ZimbraSeleniumProperties.getUniqueString() +"</content>" +
									"</mp>" +
								"</m>" +
							"</SendMsgRequest>");

				// Get the mail item for the new message
				MailItem mail = MailItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ subject +")");
				

		app.zPageMail.zRefresh();	

		// Switch to message view
		app.zPageMail.zListItem(Action.A_LEFTCLICK, Button.B_MOVE_MESSAGE, subject);
		app.zPageMail.zCancelMailAction(Button.B_CANCEL_MOVE_MAIL);
		
		// Verification
		app.zGetActiveAccount().soapSend(
				"<GetMsgRequest xmlns='urn:zimbraMail'>" +
					"<m id='" + mail.getId() +"'/>" +
				"</GetMsgRequest>");
		String folderId = app.zGetActiveAccount().soapSelectValue("//mail:m", "l");
		
		ZAssert.assertNotEqual(folderId, pagemail.getId(), "Verify the subfolder ID that the message was moved into");
		}	
	}