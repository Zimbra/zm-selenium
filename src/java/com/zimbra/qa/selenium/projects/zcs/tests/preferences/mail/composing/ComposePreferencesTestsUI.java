package com.zimbra.qa.selenium.projects.zcs.tests.preferences.mail.composing;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Calendar;
import java.text.SimpleDateFormat;

import org.testng.Assert;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;

//import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.zimbra.common.service.ServiceException;
import com.zimbra.qa.selenium.framework.core.*;
import com.zimbra.qa.selenium.framework.util.RetryFailedTests;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.Stafzmprov;
import com.zimbra.qa.selenium.framework.util.ZimbraSeleniumProperties;
import com.zimbra.qa.selenium.projects.zcs.tests.CommonTest;
import com.zimbra.qa.selenium.projects.zcs.ui.MailApp;




@SuppressWarnings( { "static-access", "unused" })
public class ComposePreferencesTestsUI extends CommonTest {

	//--------------------------------------------------------------------------
	@DataProvider(name = "composePreferencesDataProvider")
	public Object[][] createData(Method method) throws ServiceException {
		String test = method.getName();

		return new Object[][] { { "localize(locator.GAL)" } };

	}

	// Before Class
	@BeforeClass(groups = { "always" })
	public void zLogin() throws Exception {
		zLoginIfRequired();

		String accountName = ClientSessionFactory.session().currentUserName();

		Stafzmprov.modifyAccount(accountName, "zimbraPrefComposeFormat", "text");

		Stafzmprov.modifyAccount(accountName,
				"zimbraPrefHtmlEditorDefaultFontFamily", "Times New Roman");

		Stafzmprov.modifyAccount(accountName,
				"zimbraPrefHtmlEditorDefaultFontSize", "12pt");

		Stafzmprov.modifyAccount(accountName,
				"zimbraPrefForwardReplyInOriginalFormat", "FALSE");

		Stafzmprov.modifyAccount(accountName, "zimbraPrefComposeInNewWindow",
				"FALSE");

		Stafzmprov.modifyAccount(accountName, "zimbraPrefAutoSaveDraftInterval",
				"30s");

		Stafzmprov.modifyAccount(accountName,
				"zimbraPrefReplyIncludeOriginalText", "includeBody");

		Stafzmprov.modifyAccount(accountName,
				"zimbraPrefForwardIncludeOriginalText", "includeBody");

		Stafzmprov.modifyAccount(accountName, "zimbraPrefForwardReplyPrefixChar",
				">");

		Stafzmprov.modifyAccount(accountName, "zimbraPrefSaveToSent", "TRUE");

		super.zLogin();
	}

	// Before method
	@BeforeMethod(groups = { "always" })
	public void zResetIfRequired() throws Exception {
		if (SelNGBase.needReset.get() && !SelNGBase.isExecutionARetry.get()) {
			zLogin();
		}
		SelNGBase.needReset.set(true);
	}

	@Test(groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void zComposingComposeFormat() throws Exception {

		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		checkForSkipException("all", "na", "na", "Not able to identify 'compose as html' radio button (tried localize key as well)");
		 
		String actualVal;
		String accountName = ClientSessionFactory.session().currentUserName();

		page.zMailApp.zNavigateToComposingPreferences();

		obj.zRadioBtn.zClick(localize(locator.composeAsHTML), "1");

		waitForIE();

		obj.zButton.zClick(page.zCalApp.zPreferencesSaveIconBtn);

		SleepUtil.sleep(3000);

		actualVal = Stafzmprov.getAccountPreferenceValue(accountName,
				"zimbraPrefComposeFormat");

		Assert.assertEquals(actualVal, "html",
				"Compose format set is not set in db. Actual value is "
						+ actualVal);

		page.zMailApp.zNavigateToComposingPreferences();

		obj.zRadioBtn.zClick(localize(locator.composeAsText));

		waitForIE();
		obj.zButton.zClick(page.zCalApp.zPreferencesSaveIconBtn);

		SleepUtil.sleep(2000);

		actualVal = Stafzmprov.getAccountPreferenceValue(accountName,
				"zimbraPrefComposeFormat");

		Assert.assertEquals(actualVal, "text",
				"Compose format set is not set in db. Actual value is "
						+ actualVal);

		SleepUtil.sleep(500);

		SelNGBase.needReset.set(false);
	}

	@Test(groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void zComposingFontSetting() throws Exception {
		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		if (ZimbraSeleniumProperties.getStringProperty("locale").equals("en_US")) {
			String actualVal;
			String accountName = ClientSessionFactory.session().currentUserName();
			String fontSize;

			page.zMailApp.zNavigateToComposingPreferences();

			obj.zFeatureMenu.zClick(localize(locator.fFamilyLabel));
			obj.zMenuItem.zClick("Arial");

			String currentFont = "10";
			if (ZimbraSeleniumProperties.getStringProperty("locale").equals("fr")
					|| ZimbraSeleniumProperties.getStringProperty("locale").equals("nl")
					|| ZimbraSeleniumProperties.getStringProperty("locale").equals("de")
					|| ZimbraSeleniumProperties.getStringProperty("locale").equals("hi")
					|| ZimbraSeleniumProperties.getStringProperty("locale").equals("sv")
					|| ZimbraSeleniumProperties.getStringProperty("locale").equals("da")) {
				fontSize = currentFont + " pt";
			} else if (ZimbraSeleniumProperties.getStringProperty("locale").equals("es")) {
				fontSize = currentFont + " p";
			} else if (ZimbraSeleniumProperties.getStringProperty("locale").equals("it")) {
				fontSize = currentFont + " punti";
			} else {
				fontSize = currentFont + "pt";
			}

			obj.zFeatureMenu.zClick(localize(locator.fFamilyLabel), "2");
			obj.zMenuItem.zClick(fontSize);

			waitForIE();

			obj.zButton.zClick(page.zCalApp.zPreferencesSaveIconBtn);

			SleepUtil.sleep(2000);

			actualVal = Stafzmprov.getAccountPreferenceValue(accountName,
					"zimbraPrefHtmlEditorDefaultFontFamily");

			Assert.assertEquals(actualVal, "Arial",
					"Font face set is not set in db. Actual value is "
							+ actualVal);

			actualVal = Stafzmprov.getAccountPreferenceValue(accountName,
					"zimbraPrefHtmlEditorDefaultFontSize");

			Assert.assertTrue(actualVal.indexOf(currentFont) >= 0,
					"Font size set is not set in db. Actual value is "
							+ actualVal);

			page.zMailApp.zNavigateToComposingPreferences();

			currentFont = "12";
			if (ZimbraSeleniumProperties.getStringProperty("locale").equals("fr")
					|| ZimbraSeleniumProperties.getStringProperty("locale").equals("nl")
					|| ZimbraSeleniumProperties.getStringProperty("locale").equals("de")
					|| ZimbraSeleniumProperties.getStringProperty("locale").equals("hi")
					|| ZimbraSeleniumProperties.getStringProperty("locale").equals("sv")
					|| ZimbraSeleniumProperties.getStringProperty("locale").equals("da")) {
				fontSize = currentFont + " pt";
			} else if (ZimbraSeleniumProperties.getStringProperty("locale").equals("es")) {
				fontSize = currentFont + " p";
			} else if (ZimbraSeleniumProperties.getStringProperty("locale").equals("it")) {
				fontSize = currentFont + " punti";
			} else {
				fontSize = currentFont + "pt";
			}

			obj.zFeatureMenu.zClick(localize(locator.fFamilyLabel));
			obj.zMenuItem.zClick("Times New Roman");

			obj.zFeatureMenu.zClick(localize(locator.fFamilyLabel), "2");
			obj.zMenuItem.zClick(fontSize);

			waitForIE();

			obj.zButton.zClick(page.zCalApp.zPreferencesSaveIconBtn);

			SleepUtil.sleep(2000);

			actualVal = Stafzmprov.getAccountPreferenceValue(accountName,
					"zimbraPrefHtmlEditorDefaultFontFamily");

			Assert.assertEquals(actualVal, "Times New Roman",
					"Font face set is not set in db. Actual value is "
							+ actualVal);

			actualVal = Stafzmprov.getAccountPreferenceValue(accountName,
					"zimbraPrefHtmlEditorDefaultFontSize");

			Assert.assertTrue(actualVal.indexOf(currentFont) >= 0,
					"Font size set is not set in db. Actual value is "
							+ actualVal);
		}

		SelNGBase.needReset.set(false);
	}

	@Test(groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void zComposingReplyFwdUsingOriginalFormat() throws Exception {

		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		String actualVal;
		String accountName = ClientSessionFactory.session().currentUserName();

		page.zMailApp.zNavigateToComposingPreferences();

		obj.zCheckbox.zClick(localize(locator.replyForwardInSameFormat));

		waitForIE();

		obj.zButton.zClick(page.zCalApp.zPreferencesSaveIconBtn);

		SleepUtil.sleep(2000);

		actualVal = Stafzmprov.getAccountPreferenceValue(accountName,
				"zimbraPrefForwardReplyInOriginalFormat");

		Assert.assertEquals(actualVal, "TRUE",
				"Reply/fwd format set is not set in db. Actual value is "
						+ actualVal);

		page.zMailApp.zNavigateToComposingPreferences();

		obj.zCheckbox.zClick(localize(locator.replyForwardInSameFormat));

		waitForIE();
		obj.zButton.zClick(page.zCalApp.zPreferencesSaveIconBtn);

		SleepUtil.sleep(2000);

		actualVal = Stafzmprov.getAccountPreferenceValue(accountName,
				"zimbraPrefForwardReplyInOriginalFormat");

		Assert.assertEquals(actualVal, "FALSE",
				"Reply/fwd format set is not set in db. Actual value is "
						+ actualVal);

		SleepUtil.sleep(500);

		SelNGBase.needReset.set(false);
	}

	@Test(groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void zComposingComposeInNewWindow() throws Exception {

		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		String actualVal;
		String accountName = ClientSessionFactory.session().currentUserName();

		page.zMailApp.zNavigateToComposingPreferences();

		obj.zCheckbox.zClick(localize(locator.composeInNewWin));

		waitForIE();

		obj.zButton.zClick(page.zCalApp.zPreferencesSaveIconBtn);

		SleepUtil.sleep(2000);

		actualVal = Stafzmprov.getAccountPreferenceValue(accountName,
				"zimbraPrefComposeInNewWindow");

		Assert.assertEquals(actualVal, "TRUE",
				"Compose in new window set is not set in db. Actual value is "
						+ actualVal);

		page.zMailApp.zNavigateToComposingPreferences();

		obj.zCheckbox.zClick(localize(locator.composeInNewWin));

		waitForIE();
		obj.zButton.zClick(page.zCalApp.zPreferencesSaveIconBtn);

		SleepUtil.sleep(2000);

		actualVal = Stafzmprov.getAccountPreferenceValue(accountName,
				"zimbraPrefComposeInNewWindow");

		Assert.assertEquals(actualVal, "FALSE",
				"Compose in new window set is not set in db. Actual value is "
						+ actualVal);

		SleepUtil.sleep(500);

		SelNGBase.needReset.set(false);
	}

	@Test(groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void zComposingAutomaticallySaveDrafts() throws Exception {

		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		checkForSkipException("na", "all", "33551", "unchecking checkboxes doesn't show up correctly");

		String actualVal;
		String accountName = ClientSessionFactory.session().currentUserName();

		page.zMailApp.zNavigateToComposingPreferences();

		obj.zCheckbox.zClick(localize(locator.autoSaveDrafts));

		waitForIE();

		obj.zButton.zClick(page.zCalApp.zPreferencesSaveIconBtn);

		SleepUtil.sleep(2000);

		actualVal = Stafzmprov.getAccountPreferenceValue(accountName,
				"zimbraPrefAutoSaveDraftInterval");

		Assert.assertEquals(actualVal, "0",
				"Autosave drafts value set is not set in db. Actual value is "
						+ actualVal);

		page.zMailApp.zNavigateToComposingPreferences();

		obj.zCheckbox.zClick(localize(locator.autoSaveDrafts));

		waitForIE();
		obj.zButton.zClick(page.zCalApp.zPreferencesSaveIconBtn);

		SleepUtil.sleep(2000);

		actualVal = Stafzmprov.getAccountPreferenceValue(accountName,
				"zimbraPrefAutoSaveDraftInterval");

		Assert.assertEquals(actualVal, "30",
				"Autosave draft value set is not set in db. Actual value is "
						+ actualVal);

		SleepUtil.sleep(500);

		SelNGBase.needReset.set(false);
	}

	@Test(groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void zComposingReplyingToEmail() throws Exception {

		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		String actualVal;
		String accountName = ClientSessionFactory.session().currentUserName();

		page.zMailApp.zNavigateToComposingPreferences();

		obj.zFeatureMenu.zClick(localize(locator.composeReplyEmail), "2");
		obj.zMenuItem.zClick(localize(locator.dontInclude));

		waitForIE();

		obj.zButton.zClick(page.zCalApp.zPreferencesSaveIconBtn);

		SleepUtil.sleep(2000);

		actualVal = Stafzmprov.getAccountPreferenceValue(accountName,
				"zimbraPrefReplyIncludeOriginalText");

		Assert.assertEquals(actualVal, "includeNone",
				"Reply/Reply All option set is not set in db. Actual value is "
						+ actualVal);

		page.zMailApp.zNavigateToComposingPreferences();

		obj.zFeatureMenu.zClick(localize(locator.composeReplyEmail), "2");
		obj.zMenuItem.zClick(localize(locator.includeInBody));

		waitForIE();
		obj.zButton.zClick(page.zCalApp.zPreferencesSaveIconBtn);

		SleepUtil.sleep(2000);

		actualVal = Stafzmprov.getAccountPreferenceValue(accountName,
				"zimbraPrefReplyIncludeOriginalText");

		Assert.assertEquals(actualVal, "includeBodyAndHeaders",
				"Reply/Reply All option set is not set in db. Actual value is "
						+ actualVal);

		SleepUtil.sleep(500);

		SelNGBase.needReset.set(false);
	}

	@Test(groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void zComposingForwardEmail() throws Exception {

		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		String actualVal;
		String accountName = ClientSessionFactory.session().currentUserName();

		page.zMailApp.zNavigateToComposingPreferences();

		obj.zFeatureMenu.zClick(localize(locator.forwardingEmail), "3");
		obj.zMenuItem.zClick(localize(locator.includeOriginalAsAttach));

		waitForIE();

		obj.zButton.zClick(page.zCalApp.zPreferencesSaveIconBtn);

		SleepUtil.sleep(2000);

		actualVal = Stafzmprov.getAccountPreferenceValue(accountName,
				"zimbraPrefForwardIncludeOriginalText");

		Assert.assertEquals(actualVal, "includeAsAttachment",
				"Forward option set is not set in db. Actual value is "
						+ actualVal);

		page.zMailApp.zNavigateToComposingPreferences();

		obj.zFeatureMenu.zClick(localize(locator.forwardingEmail), "3");
		obj.zMenuItem.zClick(localize(locator.includeInBody));

		waitForIE();
		obj.zButton.zClick(page.zCalApp.zPreferencesSaveIconBtn);

		SleepUtil.sleep(2000);

		actualVal = Stafzmprov.getAccountPreferenceValue(accountName,
				"zimbraPrefForwardIncludeOriginalText");

		Assert.assertEquals(actualVal, "includeBodyAndHeaders",
				"Forward option set is not set in db. Actual value is "
						+ actualVal);

		SleepUtil.sleep(500);

		SelNGBase.needReset.set(false);
	}

	@Test(groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void zComposingPrefixSetting() throws Exception {

		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		String actualVal;
		String accountName = ClientSessionFactory.session().currentUserName();

		page.zMailApp.zNavigateToComposingPreferences();

		obj.zRadioBtn.zClick("|");

		waitForIE();

		obj.zButton.zClick(page.zCalApp.zPreferencesSaveIconBtn);

		SleepUtil.sleep(2000);

		actualVal = Stafzmprov.getAccountPreferenceValue(accountName,
				"zimbraPrefForwardReplyPrefixChar");

		Assert.assertEquals(actualVal, "|",
				"Prefix option set is not set in db. Actual value is "
						+ actualVal);

		page.zMailApp.zNavigateToComposingPreferences();

		obj.zRadioBtn.zClick(">");

		waitForIE();
		obj.zButton.zClick(page.zCalApp.zPreferencesSaveIconBtn);

		SleepUtil.sleep(2000);

		actualVal = Stafzmprov.getAccountPreferenceValue(accountName,
				"zimbraPrefForwardReplyPrefixChar");

		Assert.assertEquals(actualVal, ">",
				"Prefix option set is not set in db. Actual value is "
						+ actualVal);

		SleepUtil.sleep(500);

		SelNGBase.needReset.set(false);
	}

	@Test(groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void zComposingSaveToSent() throws Exception {

		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		String actualVal;
		String accountName = ClientSessionFactory.session().currentUserName();

		page.zMailApp.zNavigateToComposingPreferences();
		ClientSessionFactory.session().selenium().uncheck("//input[contains(@id,'_SAVE_TO_SENT')]");
		
		//obj.zRadioBtn.zClick(localize(locator.saveToSentNOT));

		waitForIE();

		obj.zButton.zClick(page.zCalApp.zPreferencesSaveIconBtn);

		SleepUtil.sleep(2000);

		actualVal = Stafzmprov.getAccountPreferenceValue(accountName,
				"zimbraPrefSaveToSent");

		Assert.assertEquals(actualVal, "FALSE",
				"'Save to Sent' option set is not set in db. Actual value is "
						+ actualVal);

		page.zMailApp.zNavigateToComposingPreferences();

		ClientSessionFactory.session().selenium().check("//input[contains(@id,'_SAVE_TO_SENT')]");
	/*	if (ZimbraSeleniumProperties.getStringProperty("locale").equals("de")) {
			obj.zRadioBtn.zClick("Kopie im Ordner");
		} else {
			//obj.zRadioBtn.zClick(localize(locator.saveToSent));
			selenium.check("//input[contains(@id,'_SAVE_TO_SENT')]");
		}*/

		waitForIE();
		obj.zButton.zClick(page.zCalApp.zPreferencesSaveIconBtn);

		SleepUtil.sleep(2000);

		actualVal = Stafzmprov.getAccountPreferenceValue(accountName,
				"zimbraPrefSaveToSent");

		Assert.assertEquals(actualVal, "TRUE",
				"'Save to Sent' option set is not set in db. Actual value is "
						+ actualVal);

		SleepUtil.sleep(500);

		SelNGBase.needReset.set(false);
	}

	private void waitForIE() throws Exception {
		String browser = ZimbraSeleniumProperties.getStringProperty("browser");

		if (browser.equals("IE"))
			SleepUtil.sleep(1000);

	}

	/**
	 * Test case:ZWC doesn't honor html font size from Preferences Steps 1.Login
	 * to Web client 2.Go to Preferences 3.Clikc on Compose 4. Select Radio
	 * Button "As Html" and select font size=12 pt 5.Click on Save. 6.Compose
	 * Mail to self and verify 7.Verify text in the Message body 8.verify Its
	 * Font Size it should be 12Pt
	 * 
	 * @throws Exception
	 * @author Girish
	 */
	@Test(groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void checkFontSizeInMsgBody_36919() throws Exception {

		if (SelNGBase.isExecutionARetry.get())
			handleRetry();
		String fontSize;
		String currentFont;
		String actualVal;
		String accountName = ClientSessionFactory.session().currentUserName();

		page.zMailApp.zNavigateToComposingPreferences();

		ClientSessionFactory.session().selenium().clickAt(
				"xpath=//label[contains(@id,'_text_right') and contains(text(),'"
						+ localize(locator.composeAsHTML) + "')]", "");

		currentFont = "12";
		if (ZimbraSeleniumProperties.getStringProperty("locale").equals("fr")
				|| ZimbraSeleniumProperties.getStringProperty("locale").equals("nl")
				|| ZimbraSeleniumProperties.getStringProperty("locale").equals("de")
				|| ZimbraSeleniumProperties.getStringProperty("locale").equals("hi")
				|| ZimbraSeleniumProperties.getStringProperty("locale").equals("sv")
				|| ZimbraSeleniumProperties.getStringProperty("locale").equals("da")) {
			fontSize = currentFont + " pt";
		} else if (ZimbraSeleniumProperties.getStringProperty("locale").equals("es")) {
			fontSize = currentFont + " p";
		} else if (ZimbraSeleniumProperties.getStringProperty("locale").equals("it")) {
			fontSize = currentFont + " punti";
		} else {
			fontSize = currentFont + "pt";
		}

		obj.zFeatureMenu.zClick(localize(locator.fFamilyLabel));
		obj.zMenuItem.zClick("Times New Roman");

		obj.zFeatureMenu.zClick(localize(locator.fFamilyLabel), "2");
		obj.zMenuItem.zClick(fontSize);
		obj.zButton.zClick(page.zCalApp.zPreferencesSaveIconBtn);

		SleepUtil.sleep(2000);
		page.zComposeView.zNavigateToMailCompose();
		page.zComposeView.zSendMailToSelfAndVerify(accountName,
				"ccuser@testdomain.com", "bccuser@testdomain.com", "sub",
				"hello", "");

		ClientSessionFactory.session().selenium().selectFrame("css=iframe[id*='zv__CLV__MSG_body__iframe']");
		String messageBodyText = ClientSessionFactory.session().selenium().getText("xpath=/html/body/div");
		Assert.assertTrue(messageBodyText.contains("hello"));
		Assert
				.assertTrue(ClientSessionFactory.session().selenium()
						.isElementPresent("xpath=/html/body/div[contains(@style,'font-size: "
								+ fontSize + "')]"));
		ClientSessionFactory.session().selenium().selectFrame("relative=top");

		SelNGBase.needReset.set(false);
	}
}
