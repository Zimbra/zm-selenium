package com.zimbra.qa.selenium.projects.zcs.tests.preferences.shortcuts;

import java.awt.Robot;
import java.awt.Robot;
import java.awt.event.KeyEvent;
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

import com.thoughtworks.selenium.Selenium;
import com.zimbra.common.service.ServiceException;
import com.zimbra.cs.service.admin.GetConfig;
import com.zimbra.client.ZFolder;
import com.zimbra.qa.selenium.framework.core.*;
import com.zimbra.qa.selenium.framework.util.LmtpUtil;
import com.zimbra.qa.selenium.framework.util.RetryFailedTests;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.staf.Stafzmprov;
import com.zimbra.qa.selenium.projects.zcs.tests.CommonTest;
import com.zimbra.qa.selenium.projects.zcs.ui.MailApp;




@SuppressWarnings( { "static-access", "unused" })
public class ShortcutsCustom extends CommonTest {

	//--------------------------------------------------------------------------
	// SECTION 1: DATA-PROVIDERS
	//--------------------------------------------------------------------------
	@DataProvider(name = "shortcutsDataProvider")
	public Object[][] createData(Method method) throws ServiceException {
		String test = method.getName();

		if (test.equals("shortcutsMailFolder")) {

			return new Object[][] { { KeyEvent.VK_R, "reply" },
					{ KeyEvent.VK_A, "reply-all" },
					{ KeyEvent.VK_F, "forward" },
					{ KeyEvent.VK_DELETE, "delete" } };

		} else if (test.equals("shortcutsTag")) {

			return new Object[][] { { KeyEvent.VK_R, "read" },
					{ KeyEvent.VK_U, "unread" }, { KeyEvent.VK_F, "flag" },
					{ KeyEvent.VK_F, "unflag" } };

		} else if (test.equals("shortcutsSavedSearch")) {

			return new Object[][] { { KeyEvent.VK_R, "read" },
					{ KeyEvent.VK_U, "unread" }, { KeyEvent.VK_F, "flag" },
					{ KeyEvent.VK_F, "unflag" } };

		} else {

			return new Object[][] { { "test" } };
		}
	}

	// Before Class

	@BeforeClass(groups = { "always" })
	public void zLogin() throws Exception {
		super.NAVIGATION_TAB="mail";
		super.zLogin();
	}


	
	@Test(groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void shortcutsMailFolder() throws Exception {

		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		checkForSkipException("na", "SF", "na", "shortcuts dont work with Selenium on Safari");
		

		String folderName = getLocalizedData_NoSpecialChar();
		String shortcut = "1";
		String[] recipients = { ClientSessionFactory.session().currentUserName() };
		String subject = "test subject mail folder";

		page.zMailApp.zCreateFolder(folderName);

		createShortcut("folder", folderName, shortcut);

		LmtpUtil.injectMessage(Stafzmprov.getRandomAccount(), recipients, Stafzmprov
				.getRandomAccount(), subject, "test content");

		page.zMailApp.ClickCheckMailUntilMailShowsUp(subject);

		obj.zMessageItem.zClick(subject);

		ClientSessionFactory.session().selenium().windowFocus();

		Robot zRobot = new Robot();

		zRobot.keyPress(KeyEvent.VK_PERIOD);
		zRobot.keyPress(KeyEvent.VK_1);

		zRobot.keyRelease(KeyEvent.VK_PERIOD);
		zRobot.keyRelease(KeyEvent.VK_1);

		obj.zMessageItem.zNotExists(subject);

		zRobot.keyPress(KeyEvent.VK_V);
		zRobot.keyPress(KeyEvent.VK_1);

		zRobot.keyRelease(KeyEvent.VK_V);
		zRobot.keyRelease(KeyEvent.VK_1);

		obj.zMessageItem.zExists(subject);

		SelNGBase.needReset.set(false);
	}

	@Test(groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void shortcutsTag() throws Exception {

		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		checkForSkipException("na", "SF", "na", "shortcuts dont work with Selenium on Safari");

		String tagName = getLocalizedData_NoSpecialChar();
		String shortcut = "2";
		String[] recipients = { ClientSessionFactory.session().currentUserName() };
		String subject = "test subject shortcutsTag";

		page.zMailApp.zCreateTag(tagName);

		createShortcut("tag", tagName, shortcut);

		LmtpUtil.injectMessage(Stafzmprov.getRandomAccount(), recipients, Stafzmprov
				.getRandomAccount(), subject, "test content");

		page.zMailApp.ClickCheckMailUntilMailShowsUp(subject);

		obj.zMessageItem.zClick(subject);

		ClientSessionFactory.session().selenium().windowFocus();

		Robot zRobot = new Robot();

		zRobot.keyPress(KeyEvent.VK_T);
		zRobot.keyPress(KeyEvent.VK_2);

		zRobot.keyRelease(KeyEvent.VK_T);
		zRobot.keyRelease(KeyEvent.VK_2);
		
		obj.zMessageItem.zVerifyIsTagged(subject);
		
		obj.zFolder.zClick(localize(locator.trash));
		
		SleepUtil.sleep(500);

		zRobot.keyPress(KeyEvent.VK_Y);
		zRobot.keyPress(KeyEvent.VK_2);

		zRobot.keyRelease(KeyEvent.VK_Y);
		zRobot.keyRelease(KeyEvent.VK_2);

		obj.zMessageItem.zExists(subject);

		SelNGBase.needReset.set(false);
	}

	@Test(groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void shortcutsSearches() throws Exception {

		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		String searchName = getLocalizedData_NoSpecialChar();
		String shortcut = "3";
		String[] recipients = { ClientSessionFactory.session().currentUserName() };
		String subject = "test subject shortcutsSearches";

		
		SelNGBase.fieldLabelIsAnObject = true;
		obj.zTextAreaField.zType(localize(locator.search), subject);
		
		SelNGBase.fieldLabelIsAnObject = false;
		SleepUtil.sleep(200);
		
//		obj.zEditField.zType("id=ztb_search_inputField", subject);
		
		
		
		obj.zButton.zClick(localize(locator.search));
		
		obj.zButton.zClick(localize(locator.save));

		obj.zEditField.zTypeInDlg(localize(locator.searchName), searchName);
		
		obj.zButton.zClickInDlg(localize(locator.ok));
		
		createShortcut("search", searchName, shortcut);

		LmtpUtil.injectMessage(Stafzmprov.getRandomAccount(), recipients, Stafzmprov
				.getRandomAccount(), subject, "test content");

		page.zMailApp.ClickCheckMailUntilMailShowsUp(subject);

		obj.zMessageItem.zClick(subject);

		obj.zFolder.zClick(localize(locator.trash));
		
		ClientSessionFactory.session().selenium().windowFocus();

		Robot zRobot = new Robot();

		zRobot.keyPress(KeyEvent.VK_S);
		zRobot.keyPress(KeyEvent.VK_3);

		zRobot.keyRelease(KeyEvent.VK_S);
		zRobot.keyRelease(KeyEvent.VK_3);

		
		obj.zMessageItem.zExists(subject);

		SelNGBase.needReset.set(false);
	}
	
	
	
	
	private void createShortcut(String type, String folderName, String shortcut)
			throws Exception {

		zGoToApplication("Preferences");

		obj.zTab.zClick(localize(locator.shortcuts));

		if (type.equals("folder"))
			obj.zTab.zClick(localize(locator.mailShortcuts));
		else if (type.equals("tag"))
			obj.zTab.zClick(localize(locator.tagShortcuts));
		else if (type.equals("search"))
			obj.zTab.zClick(localize(locator.searchShortcuts));

		obj.zButton.zClick(localize(locator.addShortcut));
		obj.zButton.zClick(localize(locator.browse));

		obj.zFolder.zClickInDlg(folderName);

		obj.zButton.zClickInDlg(localize(locator.ok));

		obj.zEditField.zType(localize(locator.shortcut), shortcut);

		obj.zButton.zClick(page.zCalApp.zPreferencesSaveIconBtn);
	}

	
}
