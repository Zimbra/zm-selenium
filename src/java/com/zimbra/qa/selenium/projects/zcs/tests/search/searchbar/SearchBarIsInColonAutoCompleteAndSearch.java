package com.zimbra.qa.selenium.projects.zcs.tests.search.searchbar;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.lang.reflect.Method;
import junit.framework.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.zimbra.common.service.ServiceException;
import com.zimbra.qa.selenium.framework.core.*;
import com.zimbra.qa.selenium.framework.util.LmtpUtil;
import com.zimbra.qa.selenium.framework.util.RetryFailedTests;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.Stafzmprov;
import com.zimbra.qa.selenium.framework.util.ZimbraSeleniumProperties;
import com.zimbra.qa.selenium.projects.zcs.tests.CommonTest;



/**
 * @author Jitesh Sojitra
 */

@SuppressWarnings("static-access")
public class SearchBarIsInColonAutoCompleteAndSearch extends CommonTest {
	protected boolean randomAcctCreatedFlag = false;
	protected static String currentLoggedInUser;

	//--------------------------------------------------------------------------
	// SECTION 1: DATA-PROVIDERS
	//--------------------------------------------------------------------------
	@DataProvider(name = "autocompleteDataProvider")
	public Object[][] createData(Method method) throws ServiceException {
		String test = method.getName();
		if (test.equals("verifyAutocompleteUIWithIsColonInTopSearchBox")
				|| test
						.equals("verifyAutocompleteUIWithIsColonNegativeInTopSearchBox")) {
			return new Object[][] { { "isColonString", 0 } };
		} else if (test.equals("verifyAll_IsColon_AutocompleteOptions")) {
			return new Object[][] { { "anywhere", 0 }, { "ccme", 1 },
					{ "flagged", 2 }, { "forwarded", 3 }, { "fromccme", 4 },
					{ "fromme", 5 }, { "invite", 6 }, { "local", 7 },
					{ "read", 8 }, { "received", 9 }, { "remote", 10 },
					{ "replied", 11 }, { "sent", 12 }, { "solo", 13 },
					{ "tofromccme", 14 }, { "tofromme", 15 }, { "tome", 16 },
					{ "unflagged", 17 }, { "unforwarded", 18 },
					{ "unread", 19 }, { "unreplied", 20 } };
		} else if (test.equals("verifyAutocompleteUIWithInColonInTopSearchBox")
				|| (test
						.equals("verifyAutocompleteUIWithInColonNegativeInTopSearchBox"))) {
			return new Object[][] { { "InColonString", 0 } };
		} else if (test.equals("verifyAll_InColon_AutocompleteOptions")) {
			return new Object[][] { { "Inbox", 0 }, { "Sent", 2 },
					{ "Drafts", 3 }, { "Junk", 4 }, { "Trash", 5 },
					{ "parentfolder", 6 }, { "parentfolder/subfolder", 7 },
					{ "parentfolder/subfolder/subsubxfolder", 8 } };
		} else if (test.equals("verifyAutocompleteNegativeIsColonRead")) {
			return new Object[][] { { "is:read", 0 } };
		} else if (test.equals("verifyAutocompleteNegativeInColonInbox")) {
			return new Object[][] { { "in:inbox", 0 } };
		} else if (test.equals("verifyAutocompleteNegativeInColonRemoteFolder")) {
			return new Object[][] { { "in:remotefolder", 0 } };
		} else {
			return new Object[][] { { "default", 0 } };
		}
	}

	//--------------------------------------------------------------------------
	// SECTION 2: SETUP
	//--------------------------------------------------------------------------
	@BeforeClass(groups = { "always" })
	public void zLogin() throws Exception {
		resetSession();
		currentLoggedInUser = Stafzmprov.getRandomAccount();
		
		Stafzmprov.modifyAccount(currentLoggedInUser, "userPassword", "test123");
		page.zLoginpage.zLoginToZimbraAjax(currentLoggedInUser);
		zGoToApplication("Mail");
		SelNGBase.isExecutionARetry.set(false);
	}

	@BeforeMethod(groups = { "always" })
	public void zResetIfRequired() throws Exception {
		if (SelNGBase.needReset.get() && !SelNGBase.isExecutionARetry.get()) {
			resetSession();
			page.zLoginpage.zLoginToZimbraAjax(currentLoggedInUser);
		}
		SelNGBase.needReset.set(true);
	}

	//--------------------------------------------------------------------------
	// SECTION 3: TEST-METHODS
	//--------------------------------------------------------------------------
	/**
	 * Verify all autocomplete options with is: in top search edit field. Check
	 * all is: 21 autocomplete
	 */
	@Test(dataProvider = "autocompleteDataProvider", groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void verifyAutocompleteUIWithIsColonInTopSearchBox(
			String isColonString, int no) throws Exception {
		if (SelNGBase.isExecutionARetry.get())
			handleRetry1();

		Robot zRobot = new Robot();
		obj.zEditField.zActivate("xpath=//input[@class='search_input']");
		zRobot.keyPress(KeyEvent.VK_CONTROL);
		zRobot.keyPress(KeyEvent.VK_A);
		zRobot.keyRelease(KeyEvent.VK_CONTROL);
		zRobot.keyRelease(KeyEvent.VK_A);
		String[] autoComplete = { "anywhere", "ccme", "flagged", "forwarded",
				"fromccme", "fromme", "invite", "local", "read", "received",
				"remote", "replied", "sent", "solo", "tofromccme", "tofromme",
				"tome", "unflagged", "unforwarded", "unread", "unreplied" };
		pressKeys("i");
		SleepUtil.sleep(1000);
		for (int i = 1; i <= autoComplete.length; i++) {
			VerifyIsColonAutocompleteNotExists(autoComplete[i - 1], i);
		}
		pressKeys("s");
		SleepUtil.sleep(1000);
		for (int i = 1; i <= autoComplete.length; i++) {
			VerifyIsColonAutocompleteNotExists(autoComplete[i - 1], i);
		}
		pressKeys(":");
		SleepUtil.sleep(1000);
		for (int i = 1; i <= autoComplete.length; i++) {
			VerifyIsColonAutocompleteExists(autoComplete[i - 1], i);
		}

		SelNGBase.needReset.set(false);
	}

	/**
	 * Verify all autocomplete options with -is: in top search edit field. Check
	 * all is: 21 autocomplete with negation (-is:). Also veriy autocomplete is
	 * filled properly via enter key
	 */
	@Test(dataProvider = "autocompleteDataProvider", groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void verifyAutocompleteUIWithIsColonNegativeInTopSearchBox(
			String isColonString, int no) throws Exception {
		if (SelNGBase.isExecutionARetry.get())
			handleRetry1();

		Robot zRobot = new Robot();
		obj.zEditField.zActivate("xpath=//input[@class='search_input']");
		zRobot.keyPress(KeyEvent.VK_CONTROL);
		zRobot.keyPress(KeyEvent.VK_A);
		zRobot.keyRelease(KeyEvent.VK_CONTROL);
		zRobot.keyRelease(KeyEvent.VK_A);
		String[] autoComplete = { "anywhere", "ccme", "flagged", "forwarded",
				"fromccme", "fromme", "invite", "local", "read", "received",
				"remote", "replied", "sent", "solo", "tofromccme", "tofromme",
				"tome", "unflagged", "unforwarded", "unread", "unreplied" };
		pressKeys("-");
		SleepUtil.sleep(1000);
		for (int i = 1; i <= autoComplete.length; i++) {
			VerifyIsColonAutocompleteNotExists(autoComplete[i - 1], i);
		}
		pressKeys("i");
		SleepUtil.sleep(1000);
		for (int i = 1; i <= autoComplete.length; i++) {
			VerifyIsColonAutocompleteNotExists(autoComplete[i - 1], i);
		}
		pressKeys("s");
		SleepUtil.sleep(1000);
		for (int i = 1; i <= autoComplete.length; i++) {
			VerifyIsColonAutocompleteNotExists(autoComplete[i - 1], i);
		}
		pressKeys(":");
		SleepUtil.sleep(1000);
		for (int i = 1; i <= autoComplete.length; i++) {
			VerifyIsColonAutocompleteExists(autoComplete[i - 1], i);
		}
		SleepUtil.sleep(1000);
		pressKeys("down");
		pressKeys("enter");
		SleepUtil.sleep(1000);
		String ccme = ClientSessionFactory.session().selenium().getValue("xpath=//input[@class='search_input']");
		assertReport(
				"-is:ccme",
				ccme,
				"Advanced search string not showing on search edit field while select '-is:ccme' autocomplete");

		SelNGBase.needReset.set(false);
	}

	/**
	 * Verify all autocomplete and search functionality with is: in top search
	 * edit field. Steps, 1.Inject mails 2.Type in: autocomplete one by one in
	 * top search edit field 3.Press Search button and verify search and
	 * autocomplete works for all is: option
	 */
	@Test(dataProvider = "autocompleteDataProvider", groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void verifyAll_IsColon_AutocompleteOptions(String isColonString,
			int no) throws Exception {
		if (SelNGBase.isExecutionARetry.get())
			handleRetry1();

		commonData();
		Robot zRobot = new Robot();
		System.out
				.println("-------------------------------------- Verifying autocomplete with - "
						+ isColonString
						+ " -------------------------------------------");

		switch (no) {
		case 0: // anywhere
			ClickAutoComplete(isColonString, no);
			obj.zButton.zClick("id=zb__Search__SEARCH_title");
			VerifyMessageExists(localize(locator.cancelled)
					+ ": testSubject"
					+ ","
					+ "Repliedly, Fwd: forwardedx, anywhere, ccme, fromccmex, flagged, fromme, local, invite, remotex, received, readx, sent, tofromccme, tofromme, solo, tome, unforwarded, unflagged, unreplied, unread");
			break;
		case 1: // ccme
			ClickAutoComplete(isColonString, no);
			obj.zButton.zClick("id=zb__Search__SEARCH_title");
			VerifyMessageExists("anywhere, ccme, fromme, local, tofromccme, tome");
			VerifyMessageNotExists("Repliedly, Fwd: forwardedx, fromccmex, flagged, invite, remotex, received, readx, sent, tofromme, solo, unforwarded, unflagged, unreplied, unread");
			break;
		case 2: // flagged
			ClickAutoComplete(isColonString, no);
			obj.zButton.zClick("id=zb__Search__SEARCH_title");
			VerifyMessageNotExists("Repliedly, Fwd: forwardedx, anywhere, ccme, fromccmex, flagged, fromme, local, invite, remotex, received, readx, sent, tofromccme, tofromme, solo, tome, unforwarded, unflagged, unreplied, unread");
			break;
		case 3: // forwardedx
			ClickAutoComplete(isColonString, no);
			obj.zEditField.zActivate("xpath=//input[@class='search_input']");
			zRobot.keyPress(KeyEvent.VK_END);
			zRobot.keyRelease(KeyEvent.VK_END);
			pressKeys("f, o, r");
			obj.zButton.zClick("id=zb__Search__SEARCH_title");
			VerifyMessageExists("forwardedx");
			VerifyMessageNotExists("Repliedly, Fwd: forwardedx, anywhere, ccme, fromccmex, flagged, fromme, local, invite, remotex, received, readx, sent, tofromccme, tofromme, solo, tome, unforwarded, unflagged, unreplied, unread");
			break;
		case 4: // fromccmex
			ClickAutoComplete(isColonString, no);
			obj.zButton.zClick("id=zb__Search__SEARCH_title");
			VerifyMessageExists(localize(locator.cancelled)
					+ ": testSubject"
					+ ","
					+ "Repliedly, Fwd: forwardedx, anywhere, ccme, fromme, local, tofromccme, tome");
			break;
		case 5: // fromme
			ClickAutoComplete(isColonString, no);
			obj.zButton.zClick("id=zb__Search__SEARCH_title");
			VerifyMessageExists(localize(locator.cancelled) + ": testSubject"
					+ "," + "Repliedly, Fwd: forwardedx");
			break;
		case 6: // invite
			ClickAutoComplete(isColonString, no);
			obj.zButton.zClick("id=zb__Search__SEARCH_title");
			VerifyMessageExists(localize(locator.cancelled) + ": testSubject");
			VerifyMessageNotExists("Repliedly, Fwd: forwardedx, anywhere, ccme, fromccmex, flagged, fromme, local, invite, remotex, received, readx, sent, tofromccme, tofromme, solo, tome, unforwarded, unflagged, unreplied, unread");
			break;
		case 7: // local
			ClickAutoComplete(isColonString, no);
			obj.zButton.zClick("id=zb__Search__SEARCH_title");
			VerifyMessageExists(localize(locator.cancelled)
					+ ": testSubject"
					+ ","
					+ "Repliedly, Fwd: forwardedx, anywhere, ccme, fromccmex, flagged, fromme, local, invite, remotex, received, readx, sent, tofromccme, tofromme, solo, tome, unforwarded, unflagged, unreplied, unread");
			VerifyMessageNotExists("remotesubject");
			break;
		case 8: // readx
			ClickAutoComplete(isColonString, no);
			obj.zButton.zClick("id=zb__Search__SEARCH_title");
			VerifyMessageExists(localize(locator.cancelled) + ": testSubject"
					+ "," + "Repliedly, Fwd: forwardedx, readx");
			break;
		case 9: // received
			ClickAutoComplete(isColonString, no);
			obj.zButton.zClick("id=zb__Search__SEARCH_title");
			VerifyMessageExists("Repliedly, Fwd: forwardedx, anywhere, ccme, fromccmex, flagged, fromme, local, invite, remotex, received, readx, sent, tofromccme, tofromme, solo, tome, unforwarded, unflagged, unreplied, unread");
			break;
		case 10: // remotex
			ClickAutoComplete(isColonString, no);
			obj.zButton.zClick("id=zb__Search__SEARCH_title");
			VerifyMessageExists("remotesubject, remotex, invite");
			VerifyMessageNotExists("Repliedly, Fwd: forwardedx, anywhere, ccme, fromccmex, flagged, fromme, local, received, readx, sent, tofromccme, tofromme, solo, tome, unforwarded, unflagged, unreplied, unread");
			break;
		case 11: // repliedx
			ClickAutoComplete(isColonString, no);
			obj.zButton.zClick("id=zb__Search__SEARCH_title");
			VerifyMessageExists("repliedx");
			break;
		case 12: // sent
			ClickAutoComplete(isColonString, no);
			obj.zButton.zClick("id=zb__Search__SEARCH_title");
			VerifyMessageExists(localize(locator.cancelled) + ": testSubject"
					+ "," + "Repliedly, Fwd: forwardedx");
			VerifyMessageNotExists("fromccmex");
			break;
		case 13: // solo
			ClickAutoComplete(isColonString, no);
			obj.zButton.zClick("id=zb__Search__SEARCH_title");
			VerifyMessageExists("fromccmex, flagged, invite, remotex, received, readx, sent, tofromme, solo, unforwarded, unflagged, unreplied, unread");
			break;
		case 14: // tofromccme
			ClickAutoComplete(isColonString, no);
			VerifyMessageExists("flagged, fromccmex, readx, invite, remotex, received, solo, sent, tofromme, unread, unforwarded, unflagged, unreplied");
			break;
		case 15: // tofromme
			ClickAutoComplete(isColonString, no);
			obj.zButton.zClick("id=zb__Search__SEARCH_title");
			VerifyMessageExists(localize(locator.cancelled)
					+ ": testSubject"
					+ ","
					+ "Repliedly, Fwd: forwardedx, anywhere, ccme, fromccmex, flagged, fromme, local, invite, remotex, received, readx, sent, tofromccme, tofromme, solo, tome, unforwarded, unflagged, unreplied, unread");
			break;
		case 16: // tome
			ClickAutoComplete(isColonString, no);
			obj.zButton.zClick("id=zb__Search__SEARCH_title");
			VerifyMessageExists("Repliedly, Fwd: forwardedx, anywhere, ccme, fromccmex, flagged, fromme, local, invite, remotex, received, readx, sent, tofromccme, tofromme, solo, tome, unforwarded, unflagged, unreplied, unread");
			break;
		case 17: // unflagged
			obj.zEditField.zActivate("xpath=//input[@class='search_input']");
			zRobot.keyPress(KeyEvent.VK_CONTROL);
			zRobot.keyPress(KeyEvent.VK_A);
			zRobot.keyRelease(KeyEvent.VK_CONTROL);
			zRobot.keyRelease(KeyEvent.VK_A);
			pressKeys("i, s, :, u, n, f, l, a, enter");
			obj.zButton.zClick("id=zb__Search__SEARCH_title");
			VerifyMessageExists(localize(locator.cancelled)
					+ ": testSubject"
					+ ","
					+ "Repliedly, Fwd: forwardedx, anywhere, ccme, fromccmex, flagged, fromme, local, invite, remotex, received, readx, sent, tofromccme, tofromme, solo, tome, unforwarded, unflagged, unreplied, unread");
			break;
		case 18: // unforwarded
			ClickAutoComplete(isColonString, no);
			obj.zEditField.zActivate("xpath=//input[@class='search_input']");
			zRobot.keyPress(KeyEvent.VK_END);
			zRobot.keyRelease(KeyEvent.VK_END);
			pressKeys("u, n, f, *");
			obj.zButton.zClick("id=zb__Search__SEARCH_title");
			VerifyMessageExists("unflagged, unforwarded");
			break;
		case 19: // unread
			obj.zEditField.zActivate("xpath=//input[@class='search_input']");
			zRobot.keyPress(KeyEvent.VK_CONTROL);
			zRobot.keyPress(KeyEvent.VK_A);
			zRobot.keyRelease(KeyEvent.VK_CONTROL);
			zRobot.keyRelease(KeyEvent.VK_A);
			pressKeys("i, s, :, u, n, r, e, enter");
			VerifyMessageExists("Fwd: forwardedx, ccme, fromccmex, flagged, fromme, local, invite, remotex, received, sent, tofromccme, tofromme, solo, tome, unforwarded, unflagged, unreplied, unread");
			VerifyMessageNotExists("readx, anywhere");
			break;
		case 20: // unreplied
			obj.zEditField.zActivate("xpath=//input[@class='search_input']");
			zRobot.keyPress(KeyEvent.VK_CONTROL);
			zRobot.keyPress(KeyEvent.VK_A);
			zRobot.keyRelease(KeyEvent.VK_CONTROL);
			zRobot.keyRelease(KeyEvent.VK_A);
			pressKeys("i, s, :, u, n, r, e, p, enter");
			VerifyMessageExists("drafts, "
					+ localize(locator.cancelled)
					+ ": testSubject"
					+ ","
					+ "Repliedly, anywhere, ccme, flagged, forwardedx, fromccmex, fromme, invite, local, readx, received, remotex, sent, solo, tofromccme, tofromme, tome, unflagged, unforwarded, unread, unreplied, Fwd: forwardedx");
			break;
		default:
			System.out.println("Is: string is not recognized");
			break;
		}

		SelNGBase.needReset.set(false);
	}

	/**
	 * Verify autocomplete with negation for e.g. -is:read and verify it returns
	 * all unread messages
	 */
	@Test(dataProvider = "autocompleteDataProvider", groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void verifyAutocompleteNegativeIsColonRead(String isColonString,
			int no) throws Exception {
		if (SelNGBase.isExecutionARetry.get())
			handleRetry1();

		commonData();
		Robot zRobot = new Robot();
		obj.zEditField.zActivate("xpath=//input[@class='search_input']");
		zRobot.keyPress(KeyEvent.VK_CONTROL);
		zRobot.keyPress(KeyEvent.VK_A);
		zRobot.keyRelease(KeyEvent.VK_CONTROL);
		zRobot.keyRelease(KeyEvent.VK_A);
		SleepUtil.sleep(1000);
		pressKeys("-, i, s, :, r, e, a, d, enter");
		obj.zButton.zClick("id=zb__Search__SEARCH_title");
		VerifyMessageExists("Fwd: forwardedx, ccme, fromccmex, flagged, fromme, local, invite, remotex, received, sent, tofromccme, tofromme, solo, tome, unforwarded, unflagged, unreplied, unread");
		VerifyMessageNotExists("readx, anywhere, parentfolder, subfolder, subsubxfolder");

		SelNGBase.needReset.set(false);
	}

	/**
	 * Verify all autocomplete options with in: in top search edit field. Check
	 * all in: autocomplete options (including parentfolder, subfolder,
	 * subsubfolder & remotefolder). Also veriy autocomplete is filled properly
	 * via enter key
	 */
	@Test(dataProvider = "autocompleteDataProvider", groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void verifyAutocompleteUIWithInColonInTopSearchBox(
			String isColonString, int no) throws Exception {
		if (SelNGBase.isExecutionARetry.get())
			handleRetry1();

		commonData();
		Robot zRobot = new Robot();
		obj.zEditField.zActivate("xpath=//input[@class='search_input']");
		zRobot.keyPress(KeyEvent.VK_CONTROL);
		zRobot.keyPress(KeyEvent.VK_A);
		zRobot.keyRelease(KeyEvent.VK_CONTROL);
		zRobot.keyRelease(KeyEvent.VK_A);
		String[] autoComplete = { "Inbox", "Chats", "Sent", "Drafts", "Junk",
				"Trash", "parentfolder", "parentfolder/subfolder",
				"parentfolder/subfolder/subsubxfolder", "remotefolder" };
		pressKeys("i");
		SleepUtil.sleep(1000);
		for (int i = 1; i <= autoComplete.length; i++) {
			page.zMailApp.zVerifyAutocompleteNotExistsForSearchBar(
					autoComplete[i - 1], i, 1);
		}
		pressKeys("n");
		SleepUtil.sleep(1000);
		for (int i = 1; i <= autoComplete.length; i++) {
			page.zMailApp.zVerifyAutocompleteNotExistsForSearchBar(
					autoComplete[i - 1], i, 1);
		}
		pressKeys(":");
		SleepUtil.sleep(1000);
		for (int i = 1; i <= autoComplete.length; i++) {
			page.zMailApp.zVerifyAutocompleteExistsForSearchBar(
					autoComplete[i - 1], i, 1);
		}
		SleepUtil.sleep(1000);
		pressKeys("down");
		SleepUtil.sleep(1000);
		pressKeys("down");
		pressKeys("enter");
		SleepUtil.sleep(1000);
		String sent = ClientSessionFactory.session().selenium().getValue("xpath=//input[@class='search_input']");
		assertReport(
				"in:" + (char) 34 + "sent" + (char) 34,
				sent,
				"Advanced search string not showing on search edit field while select 'in:sent' autocomplete");
		VerifyMessageExists(localize(locator.cancelled) + ": testSubject" + ","
				+ "Repliedly, Fwd: forwardedx");
		VerifyMessageNotExists("parentfolder, subfolder, subsubxfolder, junk, anywhere, ccme, flagged, fromme, fromccmex, local, readx, remotex, received, tofromccme, solo, sent, tome, tofromme, unread, unforwarded, unflagged, unreplied");

		SelNGBase.needReset.set(false);
	}

	/**
	 * Verify all autocomplete options with -in: in top search edit field. Check
	 * all -in: autocomplete options (including parentfolder, subfolder,
	 * subsubfolder & remotefolder)
	 */
	@Test(dataProvider = "autocompleteDataProvider", groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void verifyAutocompleteUIWithInColonNegativeInTopSearchBox(
			String isColonString, int no) throws Exception {
		if (SelNGBase.isExecutionARetry.get())
			handleRetry1();

		commonData();
		Robot zRobot = new Robot();
		obj.zEditField.zActivate("xpath=//input[@class='search_input']");
		zRobot.keyPress(KeyEvent.VK_CONTROL);
		zRobot.keyPress(KeyEvent.VK_A);
		zRobot.keyRelease(KeyEvent.VK_CONTROL);
		zRobot.keyRelease(KeyEvent.VK_A);
		String[] autoComplete = { "Inbox", "Chats", "Sent", "Drafts", "Junk",
				"Trash", "parentfolder", "parentfolder/subfolder",
				"parentfolder/subfolder/subsubxfolder", "remotefolder" };
		pressKeys("-");
		SleepUtil.sleep(1000);
		for (int i = 1; i <= autoComplete.length; i++) {
			page.zMailApp.zVerifyAutocompleteNotExistsForSearchBar(
					autoComplete[i - 1], i, 1);
		}
		pressKeys("i");
		SleepUtil.sleep(1000);
		for (int i = 1; i <= autoComplete.length; i++) {
			page.zMailApp.zVerifyAutocompleteNotExistsForSearchBar(
					autoComplete[i - 1], i, 1);
		}
		pressKeys("n");
		SleepUtil.sleep(1000);
		for (int i = 1; i <= autoComplete.length; i++) {
			page.zMailApp.zVerifyAutocompleteNotExistsForSearchBar(
					autoComplete[i - 1], i, 1);
		}
		pressKeys(":");
		SleepUtil.sleep(1000);
		for (int i = 1; i <= autoComplete.length; i++) {
			page.zMailApp.zVerifyAutocompleteExistsForSearchBar(
					autoComplete[i - 1], i, 1);
		}
		pressKeys("tab");
		SleepUtil.sleep(1000);
		String inbox = ClientSessionFactory.session().selenium()
				.getValue("xpath=//input[@class='search_input']");
		assertReport(
				"-in:" + (char) 34 + "inbox" + (char) 34,
				inbox,
				"Advanced search string not showing 'in:inbox' on search edit field while press tab in autocomplete option");

		SelNGBase.needReset.set(false);
	}

	/**
	 * Verify all autocomplete and search functionality with in: in top search
	 * edit field. Steps, 1.Create folders (parent, sub, subsub & remote) 2.Type
	 * is: autocomplete system and custom options 3.Press Search button and
	 * verify search and autocomplete works for all in: option
	 */
	@Test(dataProvider = "autocompleteDataProvider", groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void verifyAll_InColon_AutocompleteOptions(String inColonString,
			int no) throws Exception {
		if (SelNGBase.isExecutionARetry.get())
			handleRetry1();

		commonData();
		System.out
				.println("-------------------------------------- Verifying autocomplete with - "
						+ inColonString
						+ " -------------------------------------------");

		Robot zRobot = new Robot();
		switch (no) {
		case 0: // inbox
			ClickAutoComplete(inColonString, no);
			obj.zEditField.zActivate("xpath=//input[@class='search_input']");
			zRobot.keyPress(KeyEvent.VK_END);
			zRobot.keyRelease(KeyEvent.VK_END);
			SleepUtil.sleep(2000);
			pressKeys("u, n, *");
			obj.zButton.zClick("id=zb__Search__SEARCH_title");
			VerifyMessageExists("unread, unforwarded, unflagged, unreplied");
			VerifyMessageNotExists(localize(locator.cancelled)
					+ ": testSubject"
					+ ","
					+ "junk, parentfolder, subfolder, subsubxfolder, anywhere, ccme, fromme, fromccmex, forwardedx, local, readx, invite, repliedx, remotex, received, tofromccme, solo, sent, tome, tofromme");
			break;
		case 2: // sent
			ClickAutoComplete(inColonString, no);
			obj.zButton.zClick("id=zb__Search__SEARCH_title");
			VerifyMessageExists(localize(locator.cancelled) + ": testSubject"
					+ "," + "Repliedly, Fwd: forwardedx");
			VerifyMessageNotExists("parentfolder, subfolder, subsubxfolder, junk, anywhere, ccme, flagged, fromme, fromccmex, local, readx, remotex, received, tofromccme, solo, sent, tome, tofromme, unread, unforwarded, unflagged, unreplied");
			break;
		case 3: // drafts
			ClickAutoComplete(inColonString, no);
			obj.zButton.zClick("id=zb__Search__SEARCH_title");
			VerifyMessageExists("drafts");
			VerifyMessageNotExists("parentfolder, subfolder, subsubxfolder,"
					+ localize(locator.cancelled)
					+ ": testSubject"
					+ ","
					+ "Repliedly, Fwd: forwardedx, anywhere, ccme, flagged, fromme, fromccmex, local, readx, invite, repliedx, remotex, received, tofromccme, solo, sent, tome, tofromme, unread, unforwarded, unflagged, unreplied");
			break;
		case 4: // junk
			ClickAutoComplete(inColonString, no);
			obj.zButton.zClick("id=zb__Search__SEARCH_title");
			VerifyMessageExists("junk");
			VerifyMessageNotExists("parentfolder, subfolder, subsubxfolder,"
					+ localize(locator.cancelled)
					+ ": testSubject"
					+ ","
					+ "Repliedly, Fwd: forwardedx, anywhere, ccme, flagged, fromme, fromccmex, local, readx, invite, repliedx, remotex, received, tofromccme, solo, sent, tome, tofromme, unread, unforwarded, unflagged, unreplied");
			break;
		case 5: // trash
			ClickAutoComplete(inColonString, no);
			obj.zButton.zClick("id=zb__Search__SEARCH_title");
			VerifyMessageExists("remote");
			VerifyMessageNotExists("parentfolder, subfolder, subsubxfolder,"
					+ localize(locator.cancelled)
					+ ": testSubject"
					+ ","
					+ "Repliedly, Fwd: forwardedx, anywhere, ccme, flagged, fromme, fromccmex, local, readx, invite, repliedx, remotex, received, tofromccme, solo, sent, tome, tofromme, unread, unforwarded, unflagged, unreplied");
			break;
		case 6: // parentfolder
			obj.zEditField.zActivate("xpath=//input[@class='search_input']");
			zRobot.keyPress(KeyEvent.VK_CONTROL);
			zRobot.keyPress(KeyEvent.VK_A);
			zRobot.keyRelease(KeyEvent.VK_CONTROL);
			zRobot.keyRelease(KeyEvent.VK_A);
			pressKeys("i, n, :, p, a, r, enter");
			obj.zButton.zClick("id=zb__Search__SEARCH_title");
			VerifyMessageExists("parentfolder");
			VerifyMessageNotExists("subfolder, subsubxfolder,"
					+ localize(locator.cancelled)
					+ ": testSubject"
					+ ","
					+ "Repliedly, Fwd: forwardedx, anywhere, ccme, flagged, fromme, fromccmex, local, readx, invite, repliedx, remotex, received, tofromccme, solo, sent, tome, tofromme, unread, unforwarded, unflagged, unreplied");
			break;
		case 7: // subfolder
			ClickAutoComplete(inColonString, no);
			obj.zButton.zClick("id=zb__Search__SEARCH_title");
			VerifyMessageExists("subfolder");
			VerifyMessageNotExists("parentfolder, subsubxfolder,"
					+ localize(locator.cancelled)
					+ ": testSubject"
					+ ","
					+ "Repliedly, Fwd: forwardedx, anywhere, ccme, flagged, fromme, fromccmex, local, readx, invite, repliedx, remotex, received, tofromccme, solo, sent, tome, tofromme, unread, unforwarded, unflagged, unreplied");
			break;
		case 8: // subsubxfolder
			ClickAutoComplete(inColonString, no);
			obj.zButton.zClick("id=zb__Search__SEARCH_title");
			VerifyMessageExists("subsubxfolder");
			VerifyMessageNotExists("parentfolder, subfolder, "
					+ localize(locator.cancelled)
					+ ": testSubject"
					+ ","
					+ "Repliedly, Fwd: forwardedx, anywhere, ccme, flagged, fromme, fromccmex, local, readx, invite, repliedx, remotex, received, tofromccme, solo, sent, tome, tofromme, unread, unforwarded, unflagged, unreplied");
			break;
		default:
			System.out.println("Is: string is not recognized");
			break;
		}

		SelNGBase.needReset.set(false);
	}

	/**
	 * Verify autocomplete with negation & partial search. Steps, 1.Press
	 * -in:inb and hit enter key 2.Verify corresponding message search works and
	 * autocomplete filled with -in:inbox
	 */
	@Test(dataProvider = "autocompleteDataProvider", groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void verifyAutocompleteNegativeInColonInbox(String inColonString,
			int no) throws Exception {
		if (SelNGBase.isExecutionARetry.get())
			handleRetry1();

		commonData();
		Robot zRobot = new Robot();
		obj.zEditField.zActivate("xpath=//input[@class='search_input']");
		zRobot.keyPress(KeyEvent.VK_CONTROL);
		zRobot.keyPress(KeyEvent.VK_A);
		zRobot.keyRelease(KeyEvent.VK_CONTROL);
		zRobot.keyRelease(KeyEvent.VK_A);
		SleepUtil.sleep(1000);
		pressKeys("-, i, n, :, i, n, b, tab");
		obj.zButton.zClick("id=zb__Search__SEARCH_title");
		VerifyMessageExists("drafts, "
				+ localize(locator.cancelled)
				+ ": testSubject"
				+ ", Repliedly, Fwd: forwardedx, parentfolder, subfolder, subsubxfolder");
		VerifyMessageNotExists("anywhere, ccme, fromme, fromccmex, local, readx, invite, remotex, received, tofromccme, solo, sent, tome, tofromme");

		SelNGBase.needReset.set(false);
	}

	/**
	 * Verify autocomplete with negation & partial search for remote folder.
	 * Steps, 1.Press -in:remotef and hit enter key 2.Verify corresponding
	 * message search works and autocomplete filled with -in:remotefolder
	 */
	@Test(dataProvider = "autocompleteDataProvider", groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void verifyAutocompleteNegativeInColonRemoteFolder(
			String inColonString, int no) throws Exception {
		if (SelNGBase.isExecutionARetry.get())
			handleRetry1();

		checkForSkipException("all", "na", "47017", "-in:remotefolder doesn't show messages from local folder");

		commonData();
		Robot zRobot = new Robot();
		obj.zEditField.zActivate("xpath=//input[@class='search_input']");
		zRobot.keyPress(KeyEvent.VK_CONTROL);
		zRobot.keyPress(KeyEvent.VK_A);
		zRobot.keyRelease(KeyEvent.VK_CONTROL);
		zRobot.keyRelease(KeyEvent.VK_A);
		SleepUtil.sleep(1000);
		pressKeys("-, i, n, :, r, e, m, o, t, e, f, enter");
		obj.zButton.zClick("id=zb__Search__SEARCH_title");
		VerifyMessageNotExists("remotesubject, remotex");
		VerifyMessageNotExists("drafts, "
				+ localize(locator.cancelled)
				+ ": testSubject"
				+ ", Repliedly, Fwd: forwardedx, parentfolder, subfolder, subsubxfolder, anywhere, ccme, fromme, fromccmex, local, readx, invite, received, tofromccme, solo, sent, tome, tofromme");

		SelNGBase.needReset.set(false);
	}

	//------------------------------autocomplete_functions----------------------
	public static void VerifyIsColonAutocompleteExists(String value, int rank)
			throws Exception {
		Assert.assertTrue("Verifying is: autocomplete list rank " + rank
				+ " for " + value, ClientSessionFactory.session().selenium()
				.isElementPresent("//tr[contains(@id, 'DWT18_acRow_"
						+ (rank - 1) + "')]//td[contains(text(), '" + value
						+ "')]"));
	}

	public static void VerifyIsColonAutocompleteNotExists(String value, int rank)
			throws Exception {
		Assert.assertFalse("Verifying is: autocomplete list rank " + rank
				+ " for " + value, ClientSessionFactory.session().selenium()
				.isElementPresent("//tr[contains(@id, 'DWT18_acRow_"
						+ (rank - 1) + "')]//td[contains(text(), '" + value
						+ "')]"));
	}

	public static void ClickAutoComplete(String value, int rank)
			throws Exception {
		obj.zEditField.zActivate("xpath=//input[@class='search_input']");
		Robot zRobot = new Robot();
		zRobot.keyPress(KeyEvent.VK_CONTROL);
		zRobot.keyPress(KeyEvent.VK_A);
		zRobot.keyRelease(KeyEvent.VK_CONTROL);
		zRobot.keyRelease(KeyEvent.VK_A);
		if (value.equals("Inbox") || value.equals("Sent")
				|| value.equals("Drafts") || value.equals("Junk")
				|| value.equals("Trash") || value.equals("parentfolder")
				|| value.contains("subfolder")
				|| value.contains("subsubxfolder")
				|| value.contains("remotefolder")) {
			pressKeys("i, n, :");
			SleepUtil.sleep(1000);
			ClientSessionFactory.session().selenium().clickAt("//tr[contains(@id, 'DWT18_acRow_" + rank
					+ "')]//td[contains(text(), '" + value + "')]", "0,0");

		} else {
			pressKeys("i, s, :");
			SleepUtil.sleep(1000);
			ClientSessionFactory.session().selenium().clickAt("//tr[contains(@id, 'DWT18_acRow_" + rank
					+ "')]//td[contains(text(), '" + value + "')]", "0,0");
		}
	}

	public static void VerifyMessageExists(String subjects) throws Exception {
		String[] subject;
		subject = subjects.split(",");
		for (int i = 0; i <= subject.length - 1; i++) {
			System.out.println(obj.zMessageItem.zExistsDontWait(subject[i]
					.trim()));
			obj.zMessageItem.zExists(subject[i].trim());
		}
	}

	public static void VerifyMessageNotExists(String subjects) throws Exception {
		String[] subject;
		subject = subjects.split(",");
		for (int i = 0; i <= subject.length - 1; i++) {
			System.out.println(obj.zMessageItem.zExistsDontWait(subject[i]
					.trim()));
			Assert
					.assertEquals(
							"Unwanted message exist on searching via in: in top search edit field",
							"false", obj.zMessageItem
									.zExistsDontWait(subject[i].trim()));
		}
	}

	public void commonData() throws Exception {
		int i = 0;
		String[] fromArray = { ClientSessionFactory.session().currentUserName(),
				"ccme@testdomain.com", "flagged@testdomain.com",
				"forwardedx@testdomain.com", ClientSessionFactory.session().currentUserName(),
				ClientSessionFactory.session().currentUserName(), "invite@testdomain.com",
				ClientSessionFactory.session().currentUserName(), "readx@testdomain.com",
				"received@testdomain.com", "remote@testdomain.com",
				"repliedx@testdomain.com", "sent@testdomain.com",
				"solo@testdomain.com", ClientSessionFactory.session().currentUserName(),
				ClientSessionFactory.session().currentUserName(), ClientSessionFactory.session().currentUserName(),
				"unflagged@testdomain.com", "unforwarded@testdomain.com",
				"unread@testdomain.com", "unreplied@testdomain.com" };
		String[] recipientsArray = { ClientSessionFactory.session().currentUserName() };
		String[] ccArray = { ClientSessionFactory.session().currentUserName(),
				ClientSessionFactory.session().currentUserName(), "flagged@testdomain.com",
				"forwardedx@testdomain.com", "ccuser@testdomain.com",
				ClientSessionFactory.session().currentUserName(), "invite@testdomain.com",
				ClientSessionFactory.session().currentUserName(), "readx@testdomain.com",
				"received@testdomain.com", "remote@testdomain.com",
				"repliedx@testdomain.com", "sent@testdomain.com",
				"solo@testdomain.com", ClientSessionFactory.session().currentUserName(),
				"ccuser@testdomain.com", ClientSessionFactory.session().currentUserName(),
				"unflagged@testdomain.com", "unforwarded@testdomain.com",
				"unread@testdomain.com", "unreplied@testdomain.com" };
		String[] subjectArray = { "anywhere", "ccme", "flagged", "forwardedx",
				"fromccmex", "fromme", "invite", "local", "readx", "received",
				"remotex", "repliedx", "sent", "solo", "tofromccme",
				"tofromme", "tome", "unflagged", "unforwarded", "unread",
				"unreplied" };

		String[] fromArray1 = { ClientSessionFactory.session().currentUserName() };
		String[] recipientsArray1 = { ClientSessionFactory.session().currentUserName() };
		String[] ccArray1 = { ClientSessionFactory.session().currentUserName() };
		String[] subjectArray1 = { "junk", "parentfolder", "subfolder",
				"subsubxfolder" };

		if (randomAcctCreatedFlag == false) {
			// for in: autocomplete
			page.zMailApp.zCreateFolder("parentfolder");
			page.zMailApp.zCreateFolder("subfolder", "parentfolder");
			page.zMailApp.zCreateFolder("subsubxfolder", "subfolder");

			Stafzmprov.createAccount("ccme@testdomain.com");
			Stafzmprov.createAccount("flagged@testdomain.com");
			Stafzmprov.createAccount("forwardedx@testdomain.com");
			Stafzmprov.createAccount("invite@testdomain.com");
			Stafzmprov.createAccount("readx@testdomain.com");
			Stafzmprov.createAccount("received@testdomain.com");
			Stafzmprov.createAccount("remote@testdomain.com");
			Stafzmprov.createAccount("repliedx@testdomain.com");
			Stafzmprov.createAccount("sent@testdomain.com");
			Stafzmprov.createAccount("unflagged@testdomain.com");
			Stafzmprov.createAccount("unforwarded@testdomain.com");
			Stafzmprov.createAccount("unread@testdomain.com");
			Stafzmprov.createAccount("unreplied@testdomain.com");
			Stafzmprov.createAccount("solo@testdomain.com");
			for (i = 20; i >= 0; i--) {
				LmtpUtil.injectMessage(fromArray[i], recipientsArray,
						ccArray[i], subjectArray[i], "commonBody");
			}
			for (i = 3; i >= 0; i--) {
				LmtpUtil.injectMessage(fromArray1[0], recipientsArray1,
						ccArray1[0], subjectArray1[i], "commonBody");
			}
			obj.zButton.zClick(page.zMailApp.zGetMailIconBtn);
			SleepUtil.sleep(1000);

			// move junk message
			obj.zMessageItem.zClick("junk");
			obj.zButton.zClick(page.zMailApp.zMoveIconBtn);
			obj.zFolder.zClickInDlgByName(localize(locator.junk),
					localize(locator.moveMessage));
			obj.zButton.zClickInDlgByName(localize(locator.ok),
					localize(locator.moveMessage));
			SleepUtil.sleep(1000);
			// Assert.assertEquals(
			// "Message is not moved properly to different folder",
			// obj.zMessageItem.zExistsDontWait("junk"), "false");

			// move parentfolder message
			obj.zMessageItem.zClick("parentfolder");
			obj.zButton.zClick(page.zMailApp.zMoveIconBtn);
			obj.zFolder.zClickInDlgByName("parentfolder",
					localize(locator.moveMessage));
			obj.zButton.zClickInDlgByName(localize(locator.ok),
					localize(locator.moveMessage));
			SleepUtil.sleep(1000);
			// Assert.assertEquals(
			// "Message is not moved properly to different folder",
			// obj.zMessageItem.zExistsDontWait("parentfolder"), "false");

			// move subfolder message
			obj.zMessageItem.zClick("subfolder");
			obj.zButton.zClick(page.zMailApp.zMoveIconBtn);
			zWaitTillObjectExist("dialog", localize(locator.moveMessage));
			Boolean isElementPresent;
			isElementPresent = ClientSessionFactory.session().selenium()
					.isElementPresent("//div[contains(@id, 'zti__ZmChooseFolderDialog_Mail')]//div[contains(@class, 'ImgNodeCollapsed')]");
			if (isElementPresent == true) {
				ClientSessionFactory.session().selenium()
						.clickAt(
								"//div[contains(@id, 'zti__ZmChooseFolderDialog_Mail')]//div[contains(@class, 'ImgNodeCollapsed')]",
								"");
			}
			obj.zFolder.zClickInDlgByName("subfolder",
					localize(locator.moveMessage));
			obj.zButton.zClickInDlgByName(localize(locator.ok),
					localize(locator.moveMessage));
			SleepUtil.sleep(1000);
			// Assert.assertEquals(
			// "Message is not moved properly to different folder",
			// obj.zMessageItem.zExistsDontWait("subfolder"), "false");

			// move subsubfolder message
			obj.zMessageItem.zClick("subsubxfolder");
			obj.zButton.zClick(page.zMailApp.zMoveIconBtn);
			zWaitTillObjectExist("dialog", localize(locator.moveMessage));
			if (isElementPresent == true) {
				ClientSessionFactory.session().selenium()
						.click("//div[contains(@id, 'zti__ZmChooseFolderDialog_Mail')]//div[contains(@class, 'ImgNodeCollapsed')]");
			}
			pressKeys("right");

			obj.zFolder.zClickInDlgByName("subsubxfolder",
					localize(locator.moveMessage));
			obj.zButton.zClickInDlgByName(localize(locator.ok),
					localize(locator.moveMessage));
			SleepUtil.sleep(1000);
			// Assert.assertEquals(
			// "Message is not moved properly to different folder",
			// obj.zMessageItem.zExistsDontWait("subsubxfolder"), "false");

			// flag
			// String body = obj.zMessageItem.zGetInnerHTML("flagged");
			// int position1, position2;
			// position1 = body.indexOf("<tr id=");
			// position2 = body.indexOf("class=");
			//
			// GetInnerHtml(
			// "//td[contains(@id, 'zlif__CLV__') and contains(text(), 'flagged') and not(contains(text(), 'unflagged'))]"
			// );
			//
			// System.out
			// .println(selenium
			// .getText(
			// "//td[contains(@id, 'zlif__CLV__') and contains(text(), 'flagged') and not(contains(text(), 'unflagged'))]"
			// ));
			// obj.zButton.zClick(body.substring(position1 + 4,
			// position2).trim());

			// forwardedx
			obj.zMessageItem.zClick("forwardedx");
			obj.zButton.zClick(page.zMailApp.zForwardIconBtn);
			obj.zTextAreaField.zType(page.zComposeView.zToField,
					ClientSessionFactory.session().currentUserName());
			obj.zButton.zClick(page.zComposeView.zSendIconBtn);
			SleepUtil.sleep(1000);

			// readx
			obj.zMessageItem.zClick("readx");

			// repliedx
			obj.zMessageItem.zClick("repliedx");
			obj.zButton.zClick(page.zMailApp.zReplyAllBtn);
			obj.zTextAreaField.zType(page.zComposeView.zToField,
					ClientSessionFactory.session().currentUserName());
			obj.zEditField.zType(page.zComposeView.zSubjectField, "Repliedly");
			obj.zButton.zClick(page.zComposeView.zSendIconBtn);
			SleepUtil.sleep(1000);

			// invite
			zGoToApplication("Calendar");
			page.zCalCompose.zCreateSimpleAppt("testSubject", "",
					"invite@testdomain.com", "testBody");
			obj.zAppointment.zClick("testSub");
			pressKeys("delete");
			obj.zButton.zClickInDlgByName(localize(locator.yes),
					localize(locator.confirmTitle));
			SleepUtil.sleep(1500);
			obj.zButton.zClick(page.zComposeView.zSendIconBtn);
			SleepUtil.sleep(2000);

			page.zComposeView.zNavigateToMailCompose();
			obj.zEditField.zType(page.zComposeView.zSubjectField, "drafts");
			obj.zButton.zClick(page.zComposeView.zSaveDraftsIconBtn);
			SleepUtil.sleep(2000);

			// local (share folder)
			Stafzmprov.modifyAccount("remote@testdomain.com", "userPassword",
					"test123");
			Stafzmprov.modifyAccount("remote@testdomain.com", "zimbraPrefLocale",
					ZimbraSeleniumProperties.getStringProperty("locale"));

			resetSession();
			String[] recipientsArray2 = { "remote@testdomain.com" };
			page.zLoginpage.zLoginToZimbraAjax("remote@testdomain.com");
			LmtpUtil.injectMessage("invite@testdomain.com", recipientsArray2,
					"invite@testdomain.com", "remotesubject", "remotebody");
			page.zSharing.zShareFolder("Mail", localize(locator.inbox), "",
					currentLoggedInUser, "", "", "", "");

			resetSession();
			page.zLoginpage.zLoginToZimbraAjax(currentLoggedInUser);
			page.zSharing.zAcceptShare("remotefolder");

			randomAcctCreatedFlag = true;
		}
	}

	protected String GetInnerHtml(String ElemId) throws Exception {
		return ClientSessionFactory.session().selenium()
				.getEval("this.browserbot.getCurrentWindow().document.getElementById('"
						+ ElemId + "').innerHTML");
	}

	//--------------------------------------------------------------------------
	// SECTION 4: RETRY-METHODS
	//--------------------------------------------------------------------------
	// since all the tests are independent, retry is simply kill and re-login
	private void handleRetry1() throws Exception {
		SelNGBase.isExecutionARetry.set(false);
		resetSession();
		page.zLoginpage.zLoginToZimbraAjax(currentLoggedInUser);
	}
}