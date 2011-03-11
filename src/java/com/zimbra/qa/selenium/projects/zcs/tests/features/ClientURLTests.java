package com.zimbra.qa.selenium.projects.zcs.tests.features;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.core.*;
import com.zimbra.qa.selenium.framework.util.RetryFailedTests;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.ZimbraSeleniumProperties;
import com.zimbra.qa.selenium.framework.util.staf.Stafzmprov;
import com.zimbra.qa.selenium.projects.zcs.tests.CommonTest;
import com.zimbra.qa.selenium.projects.zcs.ui.CalApp;



/**
 * Class contains client URL tests (applicationTab, skin, composeMail &
 * composeAppt, mail compose (with and without login))
 * 
 * @author Jitesh Sojitra
 * 
 */
@SuppressWarnings("static-access")
public class ClientURLTests extends CommonTest {

	// --------------
	// section 2 BeforeClass
	// --------------
	@BeforeClass(groups = { "always" })
	public void zLogin() throws Exception {
		//super.NAVIGATION_TAB="documents";
		super.zLogin();
	}
	// Tests
	@Test(groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void customLoginToCalendarApp() throws Exception {
		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		checkForSkipException("all", "na", "44367", "Client URL for composing appointment broken");

		resetSession();
		page.zLoginpage.zCustomLoginToZimbraAjax("?app=calendar");
		obj.zFolder.zExists(localize(locator.calendar));
		obj.zButton.zNotExists(localize(locator.newFolder));
		obj.zButton.zNotExists(page.zMailApp.zGetMailIconBtn);

		SelNGBase.needReset.set(false);
	}

	@Test(groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void appTabURL() throws Exception {
		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		// Address book
		ClientSessionFactory.session().selenium().open(ZimbraSeleniumProperties.getStringProperty("server.scheme") + "://"
				+ ZimbraSeleniumProperties.getStringProperty("server.host") + "/?app=contacts");
		zNavigateAgainIfRequired(ZimbraSeleniumProperties.getStringProperty("server.scheme") + "://"
				+ ZimbraSeleniumProperties.getStringProperty("server.host") + "/?app=contacts");
		obj.zFolder.zExists(page.zABCompose.zContactsFolder);
		obj.zButton.zExists(page.zABCompose.zNewABOverviewPaneIcon);
		obj.zButton.zExists(page.zABCompose.zNewContactMenuIconBtn);

		// Calendar
		ClientSessionFactory.session().selenium().open(ZimbraSeleniumProperties.getStringProperty("server.scheme") + "://"
				+ ZimbraSeleniumProperties.getStringProperty("server.host") + "/?app=calendar");
		zNavigateAgainIfRequired(ZimbraSeleniumProperties.getStringProperty("server.scheme") + "://"
				+ ZimbraSeleniumProperties.getStringProperty("server.host") + "/?app=calendar");
		obj.zFolder.zExists(page.zCalApp.zCalendarFolder);
		obj.zButton.zExists(page.zCalApp.zNewCalOverviewPaneIcon);
		obj.zButton.zExists(page.zCalApp.zCalNewApptBtn);

		// Tasks
		ClientSessionFactory.session().selenium().open(ZimbraSeleniumProperties.getStringProperty("server.scheme") + "://"
				+ ZimbraSeleniumProperties.getStringProperty("server.host") + "/?app=tasks");
		zNavigateAgainIfRequired(ZimbraSeleniumProperties.getStringProperty("server.scheme") + "://"
				+ ZimbraSeleniumProperties.getStringProperty("server.host") + "/?app=tasks");
		obj.zFolder.zExists(page.zTaskApp.zTasksFolder);
		obj.zButton.zExists(page.zTaskApp.zNewTasksOverviewPaneIcon);
		obj.zButton.zExists(page.zTaskApp.zTasksNewBtn);

		// Documents
		ClientSessionFactory.session().selenium().open(ZimbraSeleniumProperties.getStringProperty("server.scheme") + "://"
				+ ZimbraSeleniumProperties.getStringProperty("server.host") + "/?app=documents");
		zNavigateAgainIfRequired(ZimbraSeleniumProperties.getStringProperty("server.scheme") + "://"
				+ ZimbraSeleniumProperties.getStringProperty("server.host") + "/?app=documents");
		obj.zFolder.zExists(page.zDocumentApp.zNotebookFolder);
		obj.zButton.zExists(page.zDocumentApp.zNewNotebookOverviewPaneIcon);
		obj.zButton.zExists(page.zDocumentCompose.zNewPageIconBtn);

		// Briefcase
		ClientSessionFactory.session().selenium().open(ZimbraSeleniumProperties.getStringProperty("server.scheme") + "://"
				+ ZimbraSeleniumProperties.getStringProperty("server.host") + "/?app=briefcase");
		zNavigateAgainIfRequired(ZimbraSeleniumProperties.getStringProperty("server.scheme") + "://"
				+ ZimbraSeleniumProperties.getStringProperty("server.host") + "/?app=briefcase");
		obj.zFolder.zExists(page.zBriefcaseApp.zBriefcaseFolder);
		obj.zButton.zExists("id=ztih__main_Briefcase__BRIEFCASE_textCell");
		obj.zButton.zExists(page.zBriefcaseApp.zNewMenuIconBtn);

		SelNGBase.needReset.set(false);
	}

	@Test(groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void composeURL() throws Exception {
		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		checkForSkipException("hi", "SF", "na", "compose related fields replaced by ???? in the URL (selenium bug)");

		
		String toField = Stafzmprov.getRandomAccount();
		String subjectField;
		String bodyField;
		if (ZimbraSeleniumProperties.getStringProperty("locale").equals("zh_CN")
				|| ZimbraSeleniumProperties.getStringProperty("locale").equals("ko")
				|| ZimbraSeleniumProperties.getStringProperty("locale").equals("zh_HK")
				|| ZimbraSeleniumProperties.getStringProperty("locale").equals("ja")
				|| ZimbraSeleniumProperties.getStringProperty("locale").equals("ru")) {
			subjectField = "testSubject";
			bodyField = "testBody";
		} else {
			subjectField = getLocalizedData_NoSpecialChar();
			bodyField = getLocalizedData_NoSpecialChar();
		}
		ClientSessionFactory.session().selenium().open(ZimbraSeleniumProperties.getStringProperty("server.scheme") + "://"
				+ ZimbraSeleniumProperties.getStringProperty("server.host") + "/?view=compose" + "&to="
				+ toField + "&subject=" + subjectField + "&body=" + bodyField);
		zNavigateAgainIfRequired(ZimbraSeleniumProperties.getStringProperty("server.scheme") + "://"
				+ ZimbraSeleniumProperties.getStringProperty("server.host") + "/?view=compose" + "&to="
				+ toField + "&subject=" + subjectField + "&body=" + bodyField);
		obj.zButton.zExists(page.zComposeView.zSendIconBtn);
		String toValue = obj.zTextAreaField
				.zGetInnerText(page.zComposeView.zToField);
		String subjectValue = obj.zEditField
				.zGetInnerText(page.zComposeView.zSubjectField);
		String bodyValue = obj.zEditor.zGetInnerText(bodyField).trim();
		assertReport(
				toField,
				toValue,
				"To text area field value mismatched while directly type client URL to compose a mail");
		assertReport(
				subjectField,
				subjectValue,
				"Subject edit field value mismatched while directly type client URL to compose a mail");
		assertReport(bodyField, bodyValue,
				"Body editor value mismatched while directly type client URL to compose a mail");
		obj.zButton.zClick(page.zComposeView.zCancelIconBtn);

		SelNGBase.needReset.set(false);
	}

	@Test(groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void composeURLWithSpecialCharacter_Bug44332() throws Exception {
		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		resetSession();
		page.zLoginpage
				.zCustomLoginToZimbraAjax("mail?view=compose&to=foo@example.com&subject=���&body=body");
		zNavigateAgainIfRequired(ZimbraSeleniumProperties.getStringProperty("server.scheme") + "://"
				+ ZimbraSeleniumProperties.getStringProperty("server.host")
				+ "/mail?view=compose&to=foo@example.com&subject=���&body=body");
		obj.zButton.zExists(page.zComposeView.zSendIconBtn);
		assertReport(
				"foo@example.com",
				obj.zTextAreaField.zGetInnerText(page.zComposeView.zToField),
				"To text area field value mismatched while directly type client URL to compose a mail");
		assertReport(
				"?",
				obj.zEditField.zGetInnerText(page.zComposeView.zSubjectField),
				"Subject edit field value mismatched while directly type client URL to compose a mail");
		assertReport("body", obj.zEditor.zGetInnerText("").trim(),
				"Body editor value mismatched while directly type client URL to compose a mail");
		obj.zButton.zClick(page.zComposeView.zCancelIconBtn);

		SelNGBase.needReset.set(false);
	}

	@Test(groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void calendarViewURL() throws Exception {
		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		checkForSkipException("ko", "na", "na", "start/end date contains UTF8 char in new appt page so not able to compare with expected value (selenium bug)");
		checkForSkipException("zh_CN,zh_HK,ko", "na", "na", "Need to convert date");
		checkForSkipException("all", "na", "44367", "Client URL for composing appointment broken");

		// day|workWeek|week|month
		String startDate = "20090310";
		String expectedDate = null;
		if (ZimbraSeleniumProperties.getStringProperty("locale").equals("zh_CN")) {
			expectedDate = "2009/3/10";
		} else if (ZimbraSeleniumProperties.getStringProperty("locale").equals("zh_HK")
				|| ZimbraSeleniumProperties.getStringProperty("locale").equals("en_GB")
				|| ZimbraSeleniumProperties.getStringProperty("locale").equals("en_AU")
				|| ZimbraSeleniumProperties.getStringProperty("locale").equals("es")
				|| ZimbraSeleniumProperties.getStringProperty("locale").equals("pt_BR")
				|| ZimbraSeleniumProperties.getStringProperty("locale").equals("it")
				|| ZimbraSeleniumProperties.getStringProperty("locale").equals("hi")) {
			expectedDate = "10/3/2009";
		} else if (ZimbraSeleniumProperties.getStringProperty("locale").equals("ja")) {
			expectedDate = "2009/3/10";
		} else if (ZimbraSeleniumProperties.getStringProperty("locale").equals("nl")) {
			expectedDate = "10-3-2009";
		} else if (ZimbraSeleniumProperties.getStringProperty("locale").equals("fr")) {
			expectedDate = "10/3/2009";
		} else if (ZimbraSeleniumProperties.getStringProperty("locale").equals("ru")
				|| ZimbraSeleniumProperties.getStringProperty("locale").equals("de")) {
			expectedDate = "10.3.2009";
		} else if (ZimbraSeleniumProperties.getStringProperty("locale").equals("pl")) {
			expectedDate = "2009-3-10";
		} else if (ZimbraSeleniumProperties.getStringProperty("locale").equals("da")) {
			expectedDate = "10/3/2009";
		} else if (ZimbraSeleniumProperties.getStringProperty("locale").equals("ar")) {
			expectedDate = "200/3/10";
		} else if (ZimbraSeleniumProperties.getStringProperty("locale").equals("en_US")) {
			expectedDate = "3/10/2009";
		} else if (ZimbraSeleniumProperties.getStringProperty("locale").equals("sv")) {
			expectedDate = "2009-3-10";
		}
		String calView = "workWeek";
		ClientSessionFactory.session().selenium().open(ZimbraSeleniumProperties.getStringProperty("server.scheme") + "://"
				+ ZimbraSeleniumProperties.getStringProperty("server.host") + "/?app=calendar&view=" + calView
				+ "&date=" + startDate);
		zNavigateAgainIfRequired(ZimbraSeleniumProperties.getStringProperty("server.scheme") + "://"
				+ ZimbraSeleniumProperties.getStringProperty("server.host") + "/?app=calendar&view=" + calView
				+ "&date=" + startDate);
		obj.zFolder.zExists(page.zCalApp.zCalendarFolder);
		obj.zButton.zExists(CalApp.zViewBtn);
		page.zCalApp.zNavigateToApptCompose();
		String actualDate;
		actualDate = obj.zEditField.zGetInnerText("id=startDateField*");
		if (!ZimbraSeleniumProperties.getStringProperty("locale").equals("ar")) {
			assertReport(
					expectedDate,
					actualDate,
					"Start date edit field value mismatched while directly type client URL to create appointment");
		}

		obj.zButton.zClick(page.zCalCompose.zApptCancelBtn);
		boolean found = false;
		for (int i = 1; i <= 10; i++) {
			String retVal = null;
			retVal = obj.zDialog.zExistsDontWait(localize(locator.warningMsg));
			System.out.println(retVal);
			if (retVal.equals("false")) {
				SleepUtil.sleep(1000);
			} else {
				found = true;
				break;
			}
		}
		if (found) {
			obj.zButton.zClickInDlgByName(localize(locator.no),
					localize(locator.warningMsg));
		}

		SelNGBase.needReset.set(false);
	}

	public static String getTimeMenuLocation(String val) {
		String str = localize(locator.formatDateShort);
		if (str.indexOf("H") >= 0)
			return getLocation24(val);
		else
			return getLocation12(val);
	}

	private static String getLocation12(String val) {
		String str = localize(locator.formatDateShort);
		String[] str1 = str.split(" ");
		String one = "";
		String two = "";
		String three = "";
		if (str1[0].indexOf(":") > 0) {
			String[] str2 = str1[0].split(":");
			one = str2[0];
			two = str2[1];
			three = str1[1];
		} else if (str1[1].indexOf(":") > 0) {
			String[] str2 = str1[1].split(":");
			two = str2[0];
			three = str2[1];
			one = str1[0];
		}
		if (one.toLowerCase().indexOf(val) >= 0)
			return "1";
		else if (two.toLowerCase().indexOf(val) >= 0)
			return "2";
		else if (three.toLowerCase().indexOf(val) >= 0)
			return "3";

		// something has gone wrong
		return "-1";
	}

	private static String getLocation24(String val) {
		String str = localize(locator.formatTimeShort);
		String[] str1 = str.split(":");
		String one = str1[0];
		String two = str1[1];
		if (one.toLowerCase().indexOf(val) >= 0)
			return "1";
		else if (two.toLowerCase().indexOf(val) >= 0)
			return "2";

		// something has gone wrong
		return "-1";
	}
}
