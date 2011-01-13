package com.zimbra.qa.selenium.projects.zcs.tests.mail.compose.attachments;

import java.lang.reflect.Method;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.zimbra.common.service.ServiceException;
import com.zimbra.qa.selenium.framework.core.*;
import com.zimbra.qa.selenium.framework.util.RetryFailedTests;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.ZimbraSeleniumProperties;
import com.zimbra.qa.selenium.projects.zcs.tests.CommonTest;


/**
 * @author Jitesh Sojitra
 */

@SuppressWarnings("static-access")
public class AttachBriefcaseFile extends CommonTest {
	protected int j = 0;

	//--------------------------------------------------------------------------
	// SECTION 1: DATA-PROVIDERS
	//--------------------------------------------------------------------------
	@DataProvider(name = "mailDataProvider")
	public Object[][] createData(Method method) throws ServiceException {
		String test = method.getName();
		if (test.equals("attachBriefcaseFileInMail")) {
			return new Object[][] { { ClientSessionFactory.session().currentUserName(),
					"ccuser@testdomain.com", "bccuser@testdomain.com",
					getLocalizedData(5), getLocalizedData(5),
					"data/public/other/testexcelfile.xls,data/public/other/testwordfile.doc" } };
		} else if (test.equals("attachingFilesFromBothWayAndVerifyAllLinks")) {
			return new Object[][] { { ClientSessionFactory.session().currentUserName(),
					"ccuser@testdomain.com", "bccuser@testdomain.com",
					getLocalizedData(5), getLocalizedData(5),
					"data/public/other/MultiLingualContact.csv" } };
		} else {
			return new Object[][] { { "" } };
		}
	}

	//--------------------------------------------------------------------------
	// SECTION 2: SETUP
	//--------------------------------------------------------------------------
	@BeforeClass(groups = { "always" })
	public void zLogin() throws Exception {
		super.zLogin();
	}

	//--------------------------------------------------------------------------
	// SECTION 3: TEST-METHODS
	//--------------------------------------------------------------------------
	@Test(dataProvider = "mailDataProvider", groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void attachBriefcaseFileInMail(String to, String cc, String bcc,
			String subject, String body, String attachments) throws Exception {
		if (SelNGBase.isExecutionARetry.get())
			handleRetry1();

		String[] attachment = attachments.split(",");
		uploadFile(attachments);

		zGoToApplication("Mail");

		page.zComposeView.zNavigateToMailCompose();
		obj.zTextAreaField.zType(page.zComposeView.zToField,
				ClientSessionFactory.session().currentUserName());
		obj.zTextAreaField.zType(page.zComposeView.zCcField, cc);
		obj.zEditField.zType(page.zComposeView.zSubjectField, subject);
		obj.zEditor.zType(body);

		// adding briefcase file as an attachment & sending mail to self
		obj.zButton.zClick(page.zComposeView.zAddAttachmentIconBtn);
		obj.zTab.zClickInDlgByName(localize(locator.briefcase),
				localize(locator.attachFile));
		obj.zFolder.zClickInDlgByName(localize(locator.briefcase),
				localize(locator.attachFile));
		if (attachment.length == 2) {
			obj.zCheckbox.zClickInDlgByName("id=zlif__BCI__257__se",
					localize(locator.attachFile));
			obj.zCheckbox.zClickInDlgByName("id=zlif__BCI__258__se",
					localize(locator.attachFile));
		} else {
			obj.zCheckbox.zClickInDlgByName("id=zlif__BCI__257__se",
					localize(locator.attachFile));
		}
		obj.zButton.zClickInDlgByName(localize(locator.attach),
				localize(locator.attachFile));
		zWaitTillObjectExist("button", page.zComposeView.zSendIconBtn);
		for (int i = 0; i < attachment.length; i++) {
			obj.zCheckbox.zVerifyIsChecked(attachment[i].toLowerCase());
		}
		obj.zButton.zClick(page.zComposeView.zSendIconBtn);
		SleepUtil.sleep(3000);

		// verification
		page.zMailApp.ClickCheckMailUntilMailShowsUp(subject);
		obj.zMessageItem.zClick(subject);
		SleepUtil.sleep(2000);
		if (ZimbraSeleniumProperties.getStringProperty("browser").equals("IE")) {
			Assert
					.assertTrue(
							ClientSessionFactory.session().selenium().isElementPresent(
											"xpath=//div[contains(@id,'zlif__CLV') and contains(@class,'ImgAttachment')]"),
							"Attachment symbol does not found");
		} else {
			obj.zMessageItem.zVerifyHasAttachment(subject);
		}
		// obj.zMessageItem.zVerifyHasAttachment(subject);
		obj.zButton.zClick(page.zMailApp.zForwardBtn);
		zWaitTillObjectExist("button", page.zComposeView.zSendIconBtn);
		for (int i = 0; i < attachment.length; i++) {
			obj.zCheckbox.zVerifyIsChecked(attachment[i].toLowerCase());
		}
		obj.zButton.zClick(page.zComposeView.zCancelIconBtn);
		SleepUtil.sleep(1000);

		SelNGBase.needReset.set(false);
	}

	@Test(dataProvider = "mailDataProvider", groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void attachingFilesFromBothWayAndVerifyAllLinks(String to,
			String cc, String bcc, String subject, String body,
			String attachments) throws Exception {
		if (SelNGBase.isExecutionARetry.get())
			handleRetry1();

		String[] attachment = attachments.split(",");
		uploadFile(attachments);

		zGoToApplication("Mail");
		page.zComposeView.zNavigateToMailCompose();
		page.zComposeView.zSendMailToSelfAndVerify(
				ClientSessionFactory.session().currentUserName(),
				cc, bcc, subject, body, "data/public/other/putty.log");
		obj.zMessageItem.zClick(subject);
		SleepUtil.sleep(2000);
		if (ZimbraSeleniumProperties.getStringProperty("browser").equals("IE")) {
			Assert
					.assertTrue(
							ClientSessionFactory.session().selenium().isElementPresent(
											"xpath=//div[contains(@id,'zlif__CLV') and contains(@class,'ImgAttachment')]"),
							"Attachment symbol does not found");
		} else {
			obj.zMessageItem.zVerifyHasAttachment(subject);
		}
		// obj.zMessageItem.zVerifyHasAttachment(subject);
		Boolean downloadLink = ClientSessionFactory.session().selenium().isElementPresent(
				"Link=" + localize(locator.download));
		Boolean briefcaseLink;
		if (ZimbraSeleniumProperties.getStringProperty("locale").equals("nl")) {
			briefcaseLink = ClientSessionFactory.session().selenium().isElementPresent(
					"Link=Aktetas");
		} else {
			briefcaseLink = ClientSessionFactory.session().selenium().isElementPresent(
					"Link=" + localize(locator.briefcase));
		}
		Boolean removeLink = ClientSessionFactory.session().selenium().isElementPresent(
				"Link=" + localize(locator.remove));
		assertReport("true", downloadLink.toString(),
				"Verify Download link exists for message");
		assertReport("true", briefcaseLink.toString(),
				"Verify Briefcase link exists for message");
		assertReport("true", removeLink.toString(),
				"Verify Remove link exists for message");

		// verify reply
		obj.zButton.zClick(page.zMailApp.zReplyIconBtn);
		SleepUtil.sleep(1000);
		for (int i = 0; i < attachment.length; i++) {
			obj.zCheckbox.zVerifyIsNotChecked("putty.log");
			obj.zCheckbox.zClick("putty.log");
		}

		// adding briefcase file as an attachment & sending mail to self
		obj.zButton.zClick(page.zComposeView.zAddAttachmentIconBtn);
		obj.zTab.zClickInDlgByName(localize(locator.briefcase),
				localize(locator.attachFile));
		obj.zFolder.zClickInDlgByName(localize(locator.briefcase),
				localize(locator.attachFile));
		if (attachment.length == 2) {
			obj.zCheckbox.zClickInDlgByName("id=zlif__BCI__257__se",
					localize(locator.attachFile));
			obj.zCheckbox.zClickInDlgByName("id=zlif__BCI__258__se",
					localize(locator.attachFile));
		} else {
			obj.zCheckbox.zClickInDlgByName("id=zlif__BCI__257__se",
					localize(locator.attachFile));
		}
		obj.zButton.zClickInDlgByName(localize(locator.attach),
				localize(locator.attachFile));
		zWaitTillObjectExist("button", page.zComposeView.zSendIconBtn);
		SleepUtil.sleep(2000);
		for (int i = 0; i < attachment.length; i++) {
			obj.zCheckbox.zVerifyIsChecked(attachment[i].toLowerCase());
			obj.zCheckbox.zVerifyIsChecked("putty.log");
		}
		obj.zButton.zClick(page.zComposeView.zSendIconBtn);
		SleepUtil.sleep(2000);

		// verification
		page.zMailApp.ClickCheckMailUntilMailShowsUp("Re: " + subject);
		obj.zMessageItem.zClick(subject);
		SleepUtil.sleep(2000);
		if (ZimbraSeleniumProperties.getStringProperty("browser").equals("IE")) {
			Assert
					.assertTrue(
							ClientSessionFactory.session().selenium().isElementPresent(
											"xpath=//div[contains(@id,'zlif__CLV') and contains(@class,'ImgAttachment')]"),
							"Attachment symbol does not found");
		} else {
			obj.zMessageItem.zVerifyHasAttachment(subject);
		}
		// obj.zMessageItem.zVerifyHasAttachment(subject);
		Boolean downloadAllAttachmentsLink = ClientSessionFactory.session().selenium()
				.isElementPresent("link=" + localize(locator.downloadAll));
		Boolean removeAllAttachmentsLink = ClientSessionFactory.session().selenium()
				.isElementPresent(
						"link=" + localize(locator.removeAllAttachments));
		assertReport("true", downloadAllAttachmentsLink.toString(),
				"Verify Download all attachments link exists for message");
		assertReport("true", removeAllAttachmentsLink.toString(),
				"Verify Remove all attachments link exists for message");

		obj.zButton.zClick(page.zMailApp.zForwardBtn);
		zWaitTillObjectExist("button", page.zComposeView.zSendIconBtn);
		SleepUtil.sleep(2000);
		for (int i = 0; i < attachment.length; i++) {
			obj.zCheckbox.zVerifyIsChecked(attachment[i].toLowerCase());
			obj.zCheckbox.zVerifyIsChecked("putty.log");
		}
		obj.zButton.zClick(page.zComposeView.zCancelIconBtn);
		SleepUtil.sleep(1000);

		SelNGBase.needReset.set(false);
	}

	private void uploadFile(String attachments) throws Exception {
		if (j == 0) {
			zGoToApplication("Briefcase");
			String[] attachment = attachments.split(",");
			for (int i = 0; i < attachment.length; i++) {
				page.zBriefcaseApp.zBriefcaseFileUpload(attachment[i], "");
			}
			j = j + 1;
		}
	}

	//--------------------------------------------------------------------------
	// SECTION 4: RETRY-METHODS
	//--------------------------------------------------------------------------
	// since all the tests are independent, retry is simply kill and re-login
	private void handleRetry1() throws Exception {
		SelNGBase.isExecutionARetry.set(false);
		zLogin();
		j = 0;
	}
}