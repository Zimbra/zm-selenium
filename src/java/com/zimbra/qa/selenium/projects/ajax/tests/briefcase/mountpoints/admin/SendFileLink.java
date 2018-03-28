/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2015, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.briefcase.mountpoints.admin;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.FileItem;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.AbsDialog;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.ajax.core.EnableBriefcaseFeature;
import com.zimbra.qa.selenium.projects.ajax.pages.briefcase.DialogConfirm;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.FormMailNew;

public class SendFileLink extends EnableBriefcaseFeature {

	public SendFileLink() throws HarnessException {
		logger.info("New " + SendFileLink.class.getCanonicalName());
		super.startingPage = app.zPageBriefcase;
		super.startingAccountPreferences.put("zimbraPrefShowSelectionCheckbox","TRUE");
		super.startingAccountPreferences.put("zimbraPrefComposeFormat", "html");
		super.startingAccountPreferences.put("zimbraPrefBriefcaseReadingPaneLocation", "bottom");
	}


	@Bugs (ids = "46371")
	@Test (description = "Briefcase linking problems upon renames",
			groups = { "functional", "L2" })

	public void SendFileLink_01() throws HarnessException {

		// Create file item
		String filePath = ConfigProperties.getBaseDirectory() + "/data/public/other/testtextfile.txt";
		String foldername = "folder" + ConfigProperties.getUniqueString();
		String mountpointname = "mountpoint" + ConfigProperties.getUniqueString();
		FileItem fileItem = new FileItem(filePath);

		// Upload file to server through RestUtil
		String attachmentId = ZimbraAccount.AccountA().uploadFileUsingRestUtil(filePath);
		FolderItem briefcaseFolder = FolderItem.importFromSOAP(ZimbraAccount.AccountA(), SystemFolder.Briefcase);

		// Create a folder to share
		ZimbraAccount.AccountA().soapSend(
					"<CreateFolderRequest xmlns='urn:zimbraMail'>"
				+		"<folder name='" + foldername + "' l='" + briefcaseFolder.getId() + "' view='document'/>"
				+	"</CreateFolderRequest>");

		FolderItem folder = FolderItem.importFromSOAP(ZimbraAccount.AccountA(), foldername);

		// Save uploaded file to briefcase through SOAP
		ZimbraAccount.AccountA().soapSend("<SaveDocumentRequest xmlns='urn:zimbraMail'>"
				+ "<doc l='" + folder.getId() + "'><upload id='"
				+ attachmentId + "'/></doc></SaveDocumentRequest>");

		// Share it
		ZimbraAccount.AccountA().soapSend(
					"<FolderActionRequest xmlns='urn:zimbraMail'>"
				+		"<action id='"+ folder.getId() +"' op='grant'>"
				+			"<grant d='"+ app.zGetActiveAccount().EmailAddress +"' gt='usr' perm='rwidxa' view='document'/>"
				+		"</action>"
				+	"</FolderActionRequest>");

		// Mount it
		app.zGetActiveAccount().soapSend(
					"<CreateMountpointRequest xmlns='urn:zimbraMail'>"
				+		"<link l='1' name='"+ mountpointname +"'  rid='"+ folder.getId() +"' zid='"+ ZimbraAccount.AccountA().ZimbraId +"' view='document' color='5'/>"
				+	"</CreateMountpointRequest>");

		// Select briefcase folder
		app.zPageBriefcase.zToolbarPressButton(Button.B_REFRESH);

		String locator = "css=td[id^=zti__main_Briefcase__]:contains('" + mountpointname + "')";
		app.zPageBriefcase.sClick(locator);

		// Refersh Page
		app.zPageBriefcase.zToolbarPressButton(Button.B_REFRESH);

		app.zPageBriefcase.zListItem(Action.A_BRIEFCASE_CHECKBOX, fileItem);

		// Click on Send Link
		DialogConfirm confDlg = (DialogConfirm) app.zPageBriefcase.zToolbarPressPulldown(
					Button.B_ACTIONS, Button.O_SEND_LINK, fileItem);

		// Click Yes on confirmation dialog
		FormMailNew mailform = (FormMailNew) confDlg.zPressButton(Button.B_YES);

		// Verify the new mail form is opened
		ZAssert.assertTrue(mailform.zIsActive(), "Verify the new form opened");

		// Verify link
		ZAssert.assertTrue(mailform.zWaitForIframeText("css=iframe[id*=_body_ifr]", ZimbraAccount.AccountA().EmailAddress),"Verify the link text");

		AbsDialog warning = (AbsDialog)mailform.zToolbarPressButton(Button.B_CANCEL);
		ZAssert.assertNotNull(warning, "Verify the dialog is returned");

		// Dismiss the dialog
		warning.zPressButton(Button.B_NO);

		// Delete file upon test completion
		app.zPageBriefcase.deleteFileByName(fileItem.getName());
	}

}
