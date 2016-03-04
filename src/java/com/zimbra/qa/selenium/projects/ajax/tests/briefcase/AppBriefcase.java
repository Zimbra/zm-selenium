package com.zimbra.qa.selenium.projects.ajax.tests.briefcase;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.DocumentItem;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.XmlStringUtil;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.framework.util.ZimbraURI;
import com.zimbra.qa.selenium.projects.ajax.core.FeatureBriefcaseTest;

public class AppBriefcase extends FeatureBriefcaseTest {

	public AppBriefcase() {
		logger.info("New " + AppBriefcase.class.getCanonicalName());

		super.startingPage = app.zPageBriefcase;
		
		super.startingAccountPreferences.put("zimbraPrefBriefcaseReadingPaneLocation", "bottom");
		super.startingAccountPreferences.put("zimbraPrefShowSelectionCheckbox","TRUE");
	}

	@Test(description = "?app=briefcase in url", groups = { "smoke" })
	public void AppBriefcase_01() throws HarnessException {
		
		//Go to AB tab
				app.zPageContacts.zNavigateTo();				
				SleepUtil.sleepMedium();
		ZimbraAccount account = app.zGetActiveAccount();

		FolderItem briefcaseFolder = FolderItem.importFromSOAP(account,	SystemFolder.Briefcase);

		// Create document item
		DocumentItem document = new DocumentItem();

		String docName = document.getName();
		String docText = document.getDocText();

		// Create document using SOAP
		String contentHTML = XmlStringUtil.escapeXml("<html>" + "<body>"
				+ docText + "</body>" + "</html>");

		account.soapSend("<SaveDocumentRequest requestId='0' xmlns='urn:zimbraMail'>"
						+ "<doc name='"
						+ docName
						+ "' l='"
						+ briefcaseFolder.getId()
						+ "' ct='application/x-zimbra-doc'>"
						+ "<content>"
						+ contentHTML
						+ "</content>"
						+ "</doc>"
						+ "</SaveDocumentRequest>");
		
		
		// Reload the application, with app=tasks query parameter
		ZimbraURI uri = new ZimbraURI(ZimbraURI.getBaseURI());
		uri.addQuery("app", "briefcase");
		app.zPageMail.sOpen(uri.toString());
		SleepUtil.sleepMedium();	

		// refresh briefcase page
		app.zTreeBriefcase.zTreeItem(Action.A_LEFTCLICK, briefcaseFolder, true);

		// Verify document is created
		// String name = app.zPageBriefcase.getText(docName);
		// ZAssert.assertEquals(name, docName,
		// "Verify document name through GUI");
		boolean present = app.zPageBriefcase.waitForPresentInListView(docName);
		ZAssert.assertTrue(present, "Verify document name through GUI");

	}
}

