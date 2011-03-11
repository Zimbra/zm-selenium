package com.zimbra.qa.selenium.projects.zcs.tests.preferences.mail;

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
import com.zimbra.qa.selenium.framework.util.ZimbraSeleniumProperties;
import com.zimbra.qa.selenium.framework.util.staf.Stafzmprov;
import com.zimbra.qa.selenium.projects.zcs.tests.CommonTest;
import com.zimbra.qa.selenium.projects.zcs.ui.MailApp;




@SuppressWarnings( { "static-access", "unused" })
public class MailPreferencesTestsUI extends CommonTest {

	//--------------------------------------------------------------------------
	@DataProvider(name = "mailPreferencesDataProvider")
	public Object[][] createData(Method method) throws ServiceException {
		String test = method.getName();

		return new Object[][] { { "localize(locator.GAL)" } };

	}

	// Before Class
	@BeforeClass(groups = { "always" })
	public void zLogin() throws Exception {
		zLoginIfRequired();

		String accountName = ClientSessionFactory.session().currentUserName();

		Stafzmprov.modifyAccount(accountName, "zimbraPrefMailPollingInterval",
				"5m");

		Stafzmprov.modifyAccount(accountName,
				"zimbraPrefMessageViewHtmlPreferred", "TRUE");

		Stafzmprov.modifyAccount(accountName, "zimbraPrefShowFragments", "TRUE");

		Stafzmprov.modifyAccount(accountName, "zimbraPrefOpenMailInNewWindow",
				"FALSE");

		Stafzmprov.modifyAccount(accountName, "zimbraPrefDisplayExternalImages",
				"FALSE");

		Stafzmprov.modifyAccount(accountName, "zimbraPrefMarkMsgRead", "0");

		Stafzmprov.modifyAccount(accountName, "zimbraPrefMailInitialSearch",
				"in:inbox");

		Stafzmprov.modifyAccount(accountName, "zimbraPrefMailSoundsEnabled",
				"FALSE");

		Stafzmprov.modifyAccount(accountName, "zimbraPrefMailFlashIcon", "FALSE");

		Stafzmprov.modifyAccount(accountName, "zimbraPrefMailFlashTitle", "FALSE");

		Stafzmprov.modifyAccount(accountName, "zimbraPrefMailForwardingAddress",
				"");

		Stafzmprov.modifyAccount(accountName,
				"zimbraPrefMailLocalDeliveryDisabled", "FALSE");

		Stafzmprov.modifyAccount(accountName,
				"zimbraPrefNewMailNotificationAddress", "");

		Stafzmprov.modifyAccount(accountName,
				"zimbraPrefNewMailNotificationEnabled", "FALSE");

		Stafzmprov.modifyAccount(accountName, "zimbraPrefOutOfOfficeReply", "");

		Stafzmprov.modifyAccount(accountName, "zimbraPrefOutOfOfficeReplyEnabled",
				"FALSE");

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
	public void zMailCheckForNewMail() throws Exception {

		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		String actualVal;
		String accountName = ClientSessionFactory.session().currentUserName();

		page.zMailApp.zNavigateToMailPreferences();

		obj.zFeatureMenu.zClick(localize(locator.pollingIntervalLabel));
		obj.zMenuItem.zClick(localize(locator.pollNever));

		waitForIE();

		obj.zButton.zClick(page.zCalApp.zPreferencesSaveIconBtn);

		SleepUtil.sleep(2000);

		actualVal = Stafzmprov.getAccountPreferenceValue(accountName,
				"zimbraPrefMailPollingInterval");

		Assert.assertEquals(actualVal, "31536000",
				"Polling interval value set is not set in db. Actual value is "
						+ actualVal);

		SleepUtil.sleep(500);

		SelNGBase.needReset.set(false);
	}

	@Test(groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void zMailDisplayMail() throws Exception {

		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		String actualVal;
		String accountName = ClientSessionFactory.session().currentUserName();

		page.zMailApp.zNavigateToMailPreferences();

		obj.zRadioBtn.zClick(localize(locator.displayAsText));

		waitForIE();

		obj.zButton.zClick(page.zCalApp.zPreferencesSaveIconBtn);

		SleepUtil.sleep(2000);

		actualVal = Stafzmprov.getAccountPreferenceValue(accountName,
				"zimbraPrefMessageViewHtmlPreferred");

		Assert.assertEquals(actualVal, "FALSE",
				"'Display mail as' value set is not set in db. Actual value is "
						+ actualVal);

		page.zMailApp.zNavigateToMailPreferences();

		obj.zRadioBtn.zClick(localize(locator.displayAsHTML));

		waitForIE();
		obj.zButton.zClick(page.zCalApp.zPreferencesSaveIconBtn);

		SleepUtil.sleep(2000);

		actualVal = Stafzmprov.getAccountPreferenceValue(accountName,
				"zimbraPrefMessageViewHtmlPreferred");

		Assert.assertEquals(actualVal, "TRUE",
				"'Display mail as' value set is not set in db. Actual value is "
						+ actualVal);

		SleepUtil.sleep(500);

		SelNGBase.needReset.set(false);
	}

	@Test(groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void zMailDisplaySnippets() throws Exception {

		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		String actualVal;
		String accountName = ClientSessionFactory.session().currentUserName();

		page.zMailApp.zNavigateToMailPreferences();

		obj.zCheckbox.zClick(localize(locator.showFragments));

		waitForIE();

		obj.zButton.zClick(page.zCalApp.zPreferencesSaveIconBtn);

		SleepUtil.sleep(2000);

		actualVal = Stafzmprov.getAccountPreferenceValue(accountName,
				"zimbraPrefShowFragments");

		Assert.assertEquals(actualVal, "FALSE",
				"'Display mail snippets' value set is not set in db. Actual value is "
						+ actualVal);

		page.zMailApp.zNavigateToMailPreferences();

		obj.zCheckbox.zClick(localize(locator.showFragments));

		waitForIE();
		obj.zButton.zClick(page.zCalApp.zPreferencesSaveIconBtn);

		SleepUtil.sleep(2000);

		actualVal = Stafzmprov.getAccountPreferenceValue(accountName,
				"zimbraPrefShowFragments");

		Assert.assertEquals(actualVal, "TRUE",
				"'Display mail snippets' value set is not set in db. Actual value is "
						+ actualVal);

		SleepUtil.sleep(500);

		SelNGBase.needReset.set(false);
	}

	@Test(groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void zMailOpenInNewWindow() throws Exception {

		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		String actualVal;
		String accountName = ClientSessionFactory.session().currentUserName();

		page.zMailApp.zNavigateToMailPreferences();

		obj.zCheckbox.zClick(localize(locator.openMailNewWin));

		waitForIE();

		obj.zButton.zClick(page.zCalApp.zPreferencesSaveIconBtn);

		SleepUtil.sleep(2000);

		actualVal = Stafzmprov.getAccountPreferenceValue(accountName,
				"zimbraPrefOpenMailInNewWindow");

		Assert
				.assertEquals(
						actualVal,
						"TRUE",
						"'Double click opens in new window' value set is not set in db. Actual value is "
								+ actualVal);

		page.zMailApp.zNavigateToMailPreferences();

		obj.zCheckbox.zClick(localize(locator.openMailNewWin));

		waitForIE();
		obj.zButton.zClick(page.zCalApp.zPreferencesSaveIconBtn);

		SleepUtil.sleep(2000);

		actualVal = Stafzmprov.getAccountPreferenceValue(accountName,
				"zimbraPrefOpenMailInNewWindow");

		Assert
				.assertEquals(
						actualVal,
						"FALSE",
						"'Double click opens in new window' value set is not set in db. Actual value is "
								+ actualVal);

		SleepUtil.sleep(500);

		SelNGBase.needReset.set(false);
	}

	@Test(groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void zMailDownloadPicturesAutomatically() throws Exception {

		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		String actualVal;
		String accountName = ClientSessionFactory.session().currentUserName();

		page.zMailApp.zNavigateToMailPreferences();

		obj.zCheckbox.zClick(localize(locator.showExternalImages));

		waitForIE();

		obj.zButton.zClick(page.zCalApp.zPreferencesSaveIconBtn);

		SleepUtil.sleep(2000);

		actualVal = Stafzmprov.getAccountPreferenceValue(accountName,
				"zimbraPrefDisplayExternalImages");

		Assert.assertEquals(actualVal, "TRUE",
				"'Download pictures in email' value set is not set in db. Actual value is "
						+ actualVal);

		page.zMailApp.zNavigateToMailPreferences();

		obj.zCheckbox.zClick(localize(locator.showExternalImages));

		waitForIE();
		obj.zButton.zClick(page.zCalApp.zPreferencesSaveIconBtn);

		SleepUtil.sleep(2000);

		actualVal = Stafzmprov.getAccountPreferenceValue(accountName,
				"zimbraPrefDisplayExternalImages");

		Assert.assertEquals(actualVal, "FALSE",
				"'Download pictures in email' value set is not set in db. Actual value is "
						+ actualVal);

		SleepUtil.sleep(500);

		SelNGBase.needReset.set(false);
	}

	@Test(groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void zMailMarkAsRead() throws Exception {

		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		String actualVal;
		String accountName = ClientSessionFactory.session().currentUserName();

		page.zMailApp.zNavigateToMailPreferences();

		obj.zRadioBtn.zClick(localize(locator.messageReadNone));

		waitForIE();

		obj.zButton.zClick(page.zCalApp.zPreferencesSaveIconBtn);

		SleepUtil.sleep(2000);

		actualVal = Stafzmprov.getAccountPreferenceValue(accountName,
				"zimbraPrefMarkMsgRead");

		Assert.assertEquals(actualVal, "-1",
				"'Mark message as read' value set is not set in db. Actual value is "
						+ actualVal);

		page.zMailApp.zNavigateToMailPreferences();

		obj.zRadioBtn.zClick(localize(locator.messageReadNow));

		waitForIE();
		obj.zButton.zClick(page.zCalApp.zPreferencesSaveIconBtn);

		SleepUtil.sleep(2000);

		actualVal = Stafzmprov.getAccountPreferenceValue(accountName,
				"zimbraPrefMarkMsgRead");

		Assert.assertEquals(actualVal, "0",
				"'Mark message as read' value set is not set in db. Actual value is "
						+ actualVal);

		SleepUtil.sleep(500);

		SelNGBase.needReset.set(false);
	}

	@Test(groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void zMailInitialMailSearch() throws Exception {

		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		String actualVal;
		String accountName = ClientSessionFactory.session().currentUserName();

		page.zMailApp.zNavigateToMailPreferences();

		obj.zEditField.zType(
				getNameWithoutSpace(localize(locator.initialMailSearchLabel)),
				"in:sent");

		waitForIE();

		obj.zButton.zClick(page.zCalApp.zPreferencesSaveIconBtn);

		SleepUtil.sleep(2000);

		actualVal = Stafzmprov.getAccountPreferenceValue(accountName,
				"zimbraPrefMailInitialSearch");

		Assert.assertEquals(actualVal, "in:sent",
				"'Initial mail search' value set is not set in db. Actual value is "
						+ actualVal);

		page.zMailApp.zNavigateToMailPreferences();

		obj.zEditField.zType(
				getNameWithoutSpace(localize(locator.initialMailSearchLabel)),
				"in:inbox");

		waitForIE();

		obj.zButton.zClick(page.zCalApp.zPreferencesSaveIconBtn);

		SleepUtil.sleep(2000);

		actualVal = Stafzmprov.getAccountPreferenceValue(accountName,
				"zimbraPrefMailInitialSearch");

		Assert.assertEquals(actualVal, "in:inbox",
				"'Initial mail search' value set is not set in db. Actual value is "
						+ actualVal);

		SleepUtil.sleep(500);

		SelNGBase.needReset.set(false);
	}

	@Test(groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void zMailPlayASound() throws Exception {

		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		checkForSkipException("hi", "na", "na", "localization problems with hi locale");

		String actualVal;
		String accountName = ClientSessionFactory.session().currentUserName();

		page.zMailApp.zNavigateToMailPreferences();

		obj.zCheckbox.zClick(localize(locator.playSound));

		waitForIE();

		obj.zButton.zClick(page.zCalApp.zPreferencesSaveIconBtn);

		SleepUtil.sleep(2000);

		actualVal = Stafzmprov.getAccountPreferenceValue(accountName,
				"zimbraPrefMailSoundsEnabled");

		Assert
				.assertEquals(
						actualVal,
						"TRUE",
						"'Play a sound when message arrives' value set is not set in db. Actual value is "
								+ actualVal);

		page.zMailApp.zNavigateToMailPreferences();

		obj.zCheckbox.zClick(localize(locator.playSound));

		waitForIE();

		obj.zButton.zClick(page.zCalApp.zPreferencesSaveIconBtn);

		SleepUtil.sleep(2000);

		actualVal = Stafzmprov.getAccountPreferenceValue(accountName,
				"zimbraPrefMailSoundsEnabled");

		Assert
				.assertEquals(
						actualVal,
						"FALSE",
						"'Play a sound when message arrives' value set is not set in db. Actual value is "
								+ actualVal);

		SleepUtil.sleep(500);

		SelNGBase.needReset.set(false);
	}

	@Test(groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void zMailFlashBrowser() throws Exception {

		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		String actualVal;
		String accountName = ClientSessionFactory.session().currentUserName();

		page.zMailApp.zNavigateToMailPreferences();

		obj.zCheckbox.zClick(localize(locator.flashBrowser));

		waitForIE();

		obj.zButton.zClick(page.zCalApp.zPreferencesSaveIconBtn);

		SleepUtil.sleep(2000);

		actualVal = Stafzmprov.getAccountPreferenceValue(accountName,
				"zimbraPrefMailFlashTitle");

		Assert
				.assertEquals(
						actualVal,
						"TRUE",
						"'Flash browser title when message arrives' value set is not set in db. Actual value is "
								+ actualVal);

		page.zMailApp.zNavigateToMailPreferences();

		obj.zCheckbox.zClick(localize(locator.flashBrowser));

		waitForIE();

		obj.zButton.zClick(page.zCalApp.zPreferencesSaveIconBtn);

		SleepUtil.sleep(2000);

		actualVal = Stafzmprov.getAccountPreferenceValue(accountName,
				"zimbraPrefMailFlashTitle");

		Assert
				.assertEquals(
						actualVal,
						"FALSE",
						"'Flash browser title when message arrives' value set is not set in db. Actual value is "
								+ actualVal);

		SleepUtil.sleep(500);

		SelNGBase.needReset.set(false);
	}

	@Test(groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void zMailForwardCopyTo() throws Exception {

		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		checkForSkipException("all", "na", "na", "Not able to type values in 'Forward a copy to:' edit field");

		String actualVal;
		String accountName = ClientSessionFactory.session().currentUserName();

		page.zMailApp.zNavigateToMailPreferences();

		obj.zEditField.zType(localize(locator.forwardCopyTo),
				"test@testdomain.com");
		// obj.zTextAreaField.zType(localize(locator.forwardCopyTo),
		// "testdomain.com");
		obj.zCheckbox.zClick(localize(locator.mailDeliveryDisabled));

		waitForIE();

		obj.zButton.zClick(page.zCalApp.zPreferencesSaveIconBtn);

		SleepUtil.sleep(2000);

		actualVal = Stafzmprov.getAccountPreferenceValue(accountName,
				"zimbraPrefMailLocalDeliveryDisabled");

		Assert.assertEquals(actualVal, "TRUE",
				"'Mail forwarding check' value set is not set in db. Actual value is "
						+ actualVal);

		actualVal = Stafzmprov.getAccountPreferenceValue(accountName,
				"zimbraPrefMailForwardingAddress");

		Assert.assertEquals(actualVal, "test@testdomain.com",
				"'Mail forwarding address' value set is not set in db. Actual value is "
						+ actualVal);

		page.zMailApp.zNavigateToMailPreferences();

		obj.zCheckbox.zClick(localize(locator.mailDeliveryDisabled));
		obj.zEditField.zType(localize(locator.forwardCopyTo), "");

		waitForIE();

		obj.zButton.zClick(page.zCalApp.zPreferencesSaveIconBtn);

		SleepUtil.sleep(2000);

		actualVal = Stafzmprov.getAccountPreferenceValue(accountName,
				"zimbraPrefMailFlashTitle");

		Assert.assertEquals(actualVal, "FALSE",
				"'Mail forwarding check' value set is not set in db. Actual value is "
						+ actualVal);

		actualVal = Stafzmprov.getAccountPreferenceValue(accountName,
				"zimbraPrefMailForwardingAddress");

		Assert.assertEquals(actualVal, "",
				"'Mail forwarding address' value set is not set in db. Actual value is "
						+ actualVal);

		SleepUtil.sleep(500);

		SelNGBase.needReset.set(false);
	}

	@Test(groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void zMailSendNotification() throws Exception {

		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		checkForSkipException("all", "na", "na", "Not able to type values in 'Send a notification message to:' edit field");

		String actualVal;
		String accountName = ClientSessionFactory.session().currentUserName();

		page.zMailApp.zNavigateToMailPreferences();

		obj.zCheckbox.zClick(localize(locator.mailNotifEnabled));
		obj.zEditField.zType(localize(locator.mailNotifEnabled),
				"notification@testdomain.com");

		waitForIE();

		obj.zButton.zClick(page.zCalApp.zPreferencesSaveIconBtn);

		SleepUtil.sleep(2000);

		actualVal = Stafzmprov.getAccountPreferenceValue(accountName,
				"zimbraPrefNewMailNotificationEnabled");

		Assert.assertEquals(actualVal, "TRUE",
				"'Send mail notification check' value set is not set in db. Actual value is "
						+ actualVal);

		actualVal = Stafzmprov.getAccountPreferenceValue(accountName,
				"zimbraPrefNewMailNotificationAddress");

		Assert.assertEquals(actualVal, "notification@testdomain.com",
				"'Notification address' value set is not set in db. Actual value is "
						+ actualVal);

		page.zMailApp.zNavigateToMailPreferences();

		obj.zCheckbox.zClick(localize(locator.mailNotifEnabled));
		obj.zEditField.zType(localize(locator.mailNotifEnabled),
				"notification@testdomain.com");

		waitForIE();

		obj.zButton.zClick(page.zCalApp.zPreferencesSaveIconBtn);

		SleepUtil.sleep(2000);

		actualVal = Stafzmprov.getAccountPreferenceValue(accountName,
				"zimbraPrefNewMailNotificationEnabled");

		Assert.assertEquals(actualVal, "FALSE",
				"'Send mail notification check' value set is not set in db. Actual value is "
						+ actualVal);

		actualVal = Stafzmprov.getAccountPreferenceValue(accountName,
				"zimbraPrefNewMailNotificationAddress");

		Assert.assertEquals(actualVal, "",
				"'Notification address' value set is not set in db. Actual value is "
						+ actualVal);

		SleepUtil.sleep(500);

		SelNGBase.needReset.set(false);
	}

	@Test(groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void zMailAutoReply() throws Exception {

		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		String actualVal;
		String accountName = ClientSessionFactory.session().currentUserName();

		page.zMailApp.zNavigateToMailPreferences();

		obj.zCheckbox
				.zClick(getNameWithoutSpace(localize(locator.awayMessageEnabled)));
		obj.zTextAreaField.zType(
				getNameWithoutSpace(localize(locator.awayMessageEnabled)),
				"message");

		waitForIE();

		obj.zButton.zClick(page.zCalApp.zPreferencesSaveIconBtn);

		SleepUtil.sleep(2000);
		/**
		 * Following check is added to test bug 44002
		 */
		obj.zToastAlertMessage.zAlertMsgExists(localize(locator.optionsSaved), "Valid toast message is present");

		actualVal = Stafzmprov.getAccountPreferenceValue(accountName,
				"zimbraPrefOutOfOfficeReplyEnabled");

		Assert.assertEquals(actualVal, "TRUE",
				"'Autoreply enabled check' value set is not set in db. Actual value is "
						+ actualVal);

		actualVal = Stafzmprov.getAccountPreferenceValue(accountName,
				"zimbraPrefOutOfOfficeReply");

		Assert.assertEquals(actualVal, "message",
				"'Autoreply message' value set is not set in db. Actual value is "
						+ actualVal);

		page.zMailApp.zNavigateToMailPreferences();

		obj.zCheckbox
				.zClick(getNameWithoutSpace(localize(locator.awayMessageEnabled)));

		waitForIE();

		obj.zButton.zClick(page.zCalApp.zPreferencesSaveIconBtn);

		SleepUtil.sleep(2000);

		actualVal = Stafzmprov.getAccountPreferenceValue(accountName,
				"zimbraPrefOutOfOfficeReplyEnabled");

		Assert.assertEquals(actualVal, "FALSE",
				"'Autoreply enabled check' value set is not set in db. Actual value is "
						+ actualVal);

		SleepUtil.sleep(500);

		SelNGBase.needReset.set(false);
	}

	private void waitForIE() throws Exception {
		String browser = ZimbraSeleniumProperties.getStringProperty("browser");

		if (browser.equals("IE"))
			SleepUtil.sleep(1000);

	}

	// private static String getNameWithoutSpace(String key) {
	// if (ZimbraSeleniumProperties.getStringProperty("browser").equals("IE"))
	// return key.replace("�:", "");
	// else
	// return key;
	// }
}
