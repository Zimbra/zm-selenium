package com.zimbra.qa.selenium.projects.zcs.tests.calendar.meetingrequests;

import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
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
import com.zimbra.qa.selenium.framework.util.Stafzmprov;
import com.zimbra.qa.selenium.framework.util.ZimbraSeleniumProperties;
import com.zimbra.qa.selenium.projects.zcs.tests.CommonTest;




@SuppressWarnings({ "static-access", "unused" })
public class CalendarQuickAddAndFishEyeView extends CommonTest {
	@DataProvider(name = "apptCreateDataProvider")
	private Object[][] createData(Method method) throws Exception {
		String test = method.getName();
		if (test.equals("quickAddApptDayView1")
				|| test.equals("quickAddAllDayApptDayView")
				|| test.equals("quickAddAllDayApptWeekView")
				|| test.equals("quickAddAllDayApptMonthView")
				|| test.equals("quickAddApptFishEyeMonthView")
				|| test.equals("quickAddAllDayApptFishEyeMonthView")
				|| test.equals("fishEyeOpensWrongDayOnMonday_Bug45184")) {
			return new Object[][] { { getLocalizedData_NoSpecialChar(),
					getLocalizedData(1), Stafzmprov.getRandomAccount(),
					getLocalizedData(3), "", "",
					"10:00:" + localize(locator.periodAm),
					"11:00:" + localize(locator.periodAm) } };
		} else if (test.equals("quickAddApptDayView2")
				|| test.equals("quickAddApptWeekView2")) {
			return new Object[][] { { getLocalizedData_NoSpecialChar(),
					getLocalizedData(1), Stafzmprov.getRandomAccount(),
					getLocalizedData(3), "", "",
					"12:00:" + localize(locator.periodAm),
					"1:00:" + localize(locator.periodAm) } };
		} else if (test.equals("quickAddApptWeekView1")) {
			return new Object[][] { { getLocalizedData_NoSpecialChar(),
					getLocalizedData(1), Stafzmprov.getRandomAccount(),
					getLocalizedData(3), "", "",
					"11:00:" + localize(locator.periodPm),
					"12:00:" + localize(locator.periodPm) } };
		} else if (test.equals("quickAddApptMonthView")) {
			return new Object[][] { { getLocalizedData_NoSpecialChar(),
					getLocalizedData(1), Stafzmprov.getRandomAccount(),
					getLocalizedData(3), "", "",
					"11:00:" + localize(locator.periodPm),
					"12:00:" + localize(locator.periodPm) } };
		} else if (test.equals("quickAddApptFishEyeMonthView")) {
			return new Object[][] { { getLocalizedData_NoSpecialChar(),
					getLocalizedData(1), Stafzmprov.getRandomAccount(),
					getLocalizedData(3), "", "",
					"3:00:" + localize(locator.periodAm),
					"4:00:" + localize(locator.periodAm) } };
		} else {
			return new Object[][] { { getLocalizedData_NoSpecialChar(),
					getLocalizedData(1), Stafzmprov.getRandomAccount(),
					getLocalizedData(3) } };
		}
	}
	// --------------
	// section 2 BeforeClass
	// --------------
	@BeforeClass(groups = { "always" })
	public void zLogin() throws Exception {
		super.NAVIGATION_TAB="calendar";
		super.zLogin();
	}

	/**
	 * Steps, 1. Go to Calendar 2. Switch to Day view 3. Quick add appointment >
	 * set time according to data provider > add appointment 4. Verify created
	 * appointment
	 */
	@Test(dataProvider = "apptCreateDataProvider", groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void quickAddApptDayView1(String subject, String location,
			String attendees, String body, String startDate, String endDate,
			String startTime, String endTime) throws Exception {
		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		obj.zButton.zClick(page.zCalApp.zViewBtn);
		obj.zMenuItem.zClick(localize(locator.viewDay));
		int i = 5;
		String element = "//div[contains(@class, 'calendar_view')]["
				+ i
				+ "]/div[contains(@class, 'calendar_hour_scroll')]//table/tbody/tr/td/div[contains(@class, 'calendar_grid_body_time_text') and contains(text(), '9')]";
		while (!ClientSessionFactory.session().selenium().isElementPresent(element) && i > 0) {
			element = "//div[contains(@class, 'calendar_view')]["
					+ --i
					+ "]/div[contains(@class, 'calendar_hour_scroll')]//table/tbody/tr/td/div[contains(@class, 'calendar_grid_body_time_text') and contains(text(), '9')]";
		}

		// obj.zButton.zRtClick(element);
		if (ZimbraSeleniumProperties.getStringProperty("browser").equals("IE")) {
			Robot zRobot = new Robot();
			zRobot.mouseMove(500, 500);
			zRobot.mousePress(InputEvent.BUTTON3_MASK);
			zRobot.mouseRelease(InputEvent.BUTTON3_MASK);
		} else {
			ClientSessionFactory.session().selenium().mouseDownRight(element);
			ClientSessionFactory.session().selenium().mouseUpRight(element);
		}

		obj.zMenuItem.zClick(localize(locator.newAppt));
		obj.zEditField.zTypeInDlgByName(localize(locator.subjectLabel),
				subject, localize(locator.quickAddAppt));

		page.zCalCompose.zSetStartTimeInQuickAddApptDlg(startTime);
		page.zCalCompose.zSetEndTimeInQuickAddApptDlg(endTime);

		obj.zButton.zClickInDlgByName(localize(locator.ok),
				localize(locator.quickAddAppt));
		SleepUtil.sleep(1500);
		ClientSessionFactory.session().selenium().doubleClickAt(
						"xpath=//div[contains(@class, 'ImgCalendarDayGrid')]/div[contains(@class, 'appt')]/div[contains(@class, 'appt_body')]/table/tbody/tr/td[contains(@class, appt_name) and contains(text(), '"
								+ subject + "')]", "");
		// time objects are changed
		// page.zCalCompose.zVerifyStartEndTime(startTime, "startTime");
		// page.zCalCompose.zVerifyStartEndTime(endTime, "endTime");
		obj.zButton.zClick(page.zCalCompose.zApptCloseBtn);
		closeWarningDlg();

		SelNGBase.needReset.set(false);
	}

	/**
	 * Steps, 1. Go to Calendar 2. Switch to Day view 3. Quick add appointment
	 * on particular time slot and verify it > add appointment 4. Verify created
	 * appointment
	 */
	@Test(dataProvider = "apptCreateDataProvider", groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void quickAddApptDayView2(String subject, String location,
			String attendees, String body, String startDate, String endDate,
			String startTime, String endTime) throws Exception {
		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		obj.zButton.zClick(page.zCalApp.zViewBtn);
		obj.zMenuItem.zClick(localize(locator.viewDay));
		SleepUtil.sleep(1000);
		obj.zButton
				.zClick("xpath=//div[contains(@class, 'calendar_heading_day_today')]");
		obj.zButton
				.zClick("xpath=//div[contains(@class, 'calendar_heading_day_today')]");

		int i = 5;
		String element = "//div[contains(@class, 'calendar_view')]["
				+ i
				+ "]/div[contains(@class, 'calendar_hour_scroll')]//table/tbody/tr/td/div[contains(@class, 'calendar_grid_body_time_text') and contains(text(), '9')]";
		while (!ClientSessionFactory.session().selenium().isElementPresent(element) && i > 0) {
			element = "//div[contains(@class, 'calendar_view')]["
					+ --i
					+ "]/div[contains(@class, 'calendar_hour_scroll')]//table/tbody/tr/td/div[contains(@class, 'calendar_grid_body_time_text') and contains(text(), '9')]";
		}
		if (ZimbraSeleniumProperties.getStringProperty("browser").equals("IE")) {
			Robot zRobot = new Robot();
			zRobot.mouseMove(500, 500);
			zRobot.mousePress(InputEvent.BUTTON3_MASK);
			zRobot.mouseRelease(InputEvent.BUTTON3_MASK);
		} else {
			ClientSessionFactory.session().selenium().mouseDownRight(element);
			ClientSessionFactory.session().selenium().mouseUpRight(element);
		}
		obj.zMenuItem.zClick(localize(locator.newAppt));

		// obj.zButton
		// .zRtClick(
		// "xpath=//div[contains(@class, 'calendar_time_selection') and contains(@style, 'display: block') and contains(@style, 'left: 2px')]"
		// );
		// SleepUtil.sleep(1000);
		// obj.zMenuItem.zClick(localize(locator.newAppt));
		// time objects are changed
		// if
		// (ZimbraSeleniumProperties.getStringProperty("browser").contains("FF"))
		// {
		// page.zCalCompose.zVerifyStartEndTimeInQuickAddApptDlg(startTime,
		// "startTime");
		// page.zCalCompose.zVerifyStartEndTimeInQuickAddApptDlg(endTime,
		// "endTime");
		// }
		obj.zEditField.zTypeInDlgByName(localize(locator.subjectLabel),
				subject, localize(locator.quickAddAppt));
		obj.zButton.zClickInDlgByName(localize(locator.ok),
				localize(locator.quickAddAppt));
		SleepUtil.sleep(1500);
		ClientSessionFactory.session().selenium().doubleClickAt(
						"xpath=//div[contains(@class, 'ImgCalendarDayGrid')]/div[contains(@class, 'appt')]/div[contains(@class, 'appt_body')]/table/tbody/tr/td[contains(@class, appt_name) and contains(text(), '"
								+ subject + "')]", "");
		// time objects are changed
		// if
		// (!ZimbraSeleniumProperties.getStringProperty("browser").equals("IE"))
		// {
		// page.zCalCompose.zVerifyStartEndTime(startTime, "startTime");
		// page.zCalCompose.zVerifyStartEndTime(endTime, "endTime");
		// }
		obj.zButton.zClick(page.zCalCompose.zApptCloseBtn);
		closeWarningDlg();

		SelNGBase.needReset.set(false);
	}

	/**
	 * Steps, 1. Go to Calendar 2. Switch to Day view 3. Quick add all day
	 * appointment 4. Verify created all day appointment
	 */
	@Test(dataProvider = "apptCreateDataProvider", groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void quickAddAllDayApptDayView(String subject, String location,
			String attendees, String body, String startDate, String endDate,
			String startTime, String endTime) throws Exception {
		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		obj.zButton.zClick(page.zCalApp.zViewBtn);
		obj.zMenuItem.zClick(localize(locator.viewDay));

		int i = 5;
		String element = "//div[contains(@class, 'calendar_view')]["
				+ i
				+ "]/div[contains(@class, 'calendar_hour_scroll')]//table/tbody/tr/td/div[contains(@class, 'calendar_grid_body_time_text') and contains(text(), '9')]";
		while (!ClientSessionFactory.session().selenium().isElementPresent(element) && i > 0) {
			element = "//div[contains(@class, 'calendar_view')]["
					+ --i
					+ "]/div[contains(@class, 'calendar_hour_scroll')]//table/tbody/tr/td/div[contains(@class, 'calendar_grid_body_time_text') and contains(text(), '9')]";
		}
		// obj.zButton.zRtClick(element);
		if (ZimbraSeleniumProperties.getStringProperty("browser").equals("IE")) {
			Robot zRobot = new Robot();
			zRobot.mouseMove(500, 500);
			zRobot.mousePress(InputEvent.BUTTON3_MASK);
			zRobot.mouseRelease(InputEvent.BUTTON3_MASK);
		} else {
			ClientSessionFactory.session().selenium().mouseDownRight(element);
			ClientSessionFactory.session().selenium().mouseUpRight(element);
		}
		obj.zMenuItem.zClick(localize(locator.newAllDayAppt));
		page.zCalCompose.zVerifyStartEndTimeInQuickAddApptDlgNotExists();
		obj.zEditField.zTypeInDlgByName(localize(locator.subjectLabel),
				subject, localize(locator.quickAddAppt));
		obj.zButton.zClickInDlgByName(localize(locator.ok),
				localize(locator.quickAddAppt));
		SleepUtil.sleep(1500);
		ClientSessionFactory.session().selenium().doubleClickAt(
						"xpath=//div[contains(@class, 'appt')]/table/tbody/tr/td[contains (@class, 'appt_allday_name') and contains(text(), '"
								+ subject + "')]", "");
		SleepUtil.sleep(2000);
		page.zCalCompose.zVerifyStartEndTimeNotExists();
		obj.zButton.zClick(page.zCalCompose.zApptCloseBtn);
		closeWarningDlg();

		SelNGBase.needReset.set(false);
	}

	/**
	 * Steps, 1. Go to Calendar 2. Switch to Week view 3. Quick add appointment
	 * by setting particular time > add appointment 4. Verify created
	 * appointment
	 */
	@Test(dataProvider = "apptCreateDataProvider", groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void quickAddApptWeekView1(String subject, String location,
			String attendees, String body, String startDate, String endDate,
			String startTime, String endTime) throws Exception {
		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		obj.zButton.zClick(page.zCalApp.zViewBtn);
		obj.zMenuItem.zClick(localize(locator.week));
		int i = 5;
		String element = "//div[contains(@class, 'calendar_view')]["
				+ i
				+ "]/div[contains(@class, 'calendar_hour_scroll')]//table/tbody/tr/td/div[contains(@class, 'calendar_grid_body_time_text') and contains(text(), '9')]";

		while (!ClientSessionFactory.session().selenium().isElementPresent(element) && i > 0) {
			element = "//div[contains(@class, 'calendar_view')]["
					+ --i
					+ "]/div[contains(@class, 'calendar_hour_scroll')]//table/tbody/tr/td/div[contains(@class, 'calendar_grid_body_time_text') and contains(text(), '9')]";
		}
		// obj.zButton.zRtClick(element);
		if (ZimbraSeleniumProperties.getStringProperty("browser").equals("IE")) {
			Robot zRobot = new Robot();
			zRobot.mouseMove(500, 500);
			zRobot.mousePress(InputEvent.BUTTON3_MASK);
			zRobot.mouseRelease(InputEvent.BUTTON3_MASK);
		} else {
			ClientSessionFactory.session().selenium().mouseDownRight(element);
			ClientSessionFactory.session().selenium().mouseUpRight(element);
		}
		obj.zMenuItem.zClick(localize(locator.newAppt));
		obj.zEditField.zTypeInDlgByName(localize(locator.subjectLabel),
				subject, localize(locator.quickAddAppt));
		page.zCalCompose.zSetStartTimeInQuickAddApptDlg(startTime);
		page.zCalCompose.zSetEndTimeInQuickAddApptDlg(endTime);
		obj.zButton.zClickInDlgByName(localize(locator.ok),
				localize(locator.quickAddAppt));

		SleepUtil.sleep(1500);
		ClientSessionFactory.session().selenium()
				.doubleClickAt(
						"xpath=//div[contains(@class, 'ImgCalendarDayGrid')]/div[contains(@class, 'appt')]/div[contains(@class, 'appt_body')]/table/tbody/tr/td[contains(@class, appt_name) and contains(text(), '"
								+ subject + "')]", "");
		// time objects are changed
		// page.zCalCompose.zVerifyStartEndTime(startTime, "startTime");
		// page.zCalCompose.zVerifyStartEndTime(endTime, "endTime");
		obj.zButton.zClick(page.zCalCompose.zApptCloseBtn);
		closeWarningDlg();

		SelNGBase.needReset.set(false);
	}

	/**
	 * Steps, 1. Go to Calendar 2. Switch to Week view 3. Quick add appointment
	 * on particular time slot > add appointment 4. Verify created appointment
	 */
	@Test(dataProvider = "apptCreateDataProvider", groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void quickAddApptWeekView2(String subject, String location,
			String attendees, String body, String startDate, String endDate,
			String startTime, String endTime) throws Exception {
		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		obj.zButton.zClick(page.zCalApp.zViewBtn);
		obj.zMenuItem.zClick(localize(locator.viewDay));
		SleepUtil.sleep(1000);
		obj.zButton
				.zClick("xpath=//div[contains(@class, 'calendar_heading_day_today')]");
		obj.zButton.zClick(page.zCalApp.zViewBtn);
		obj.zMenuItem.zClick(localize(locator.viewWeek));

		int i = 5;
		String element = "//div[contains(@class, 'calendar_view')]["
				+ i
				+ "]/div[contains(@class, 'calendar_hour_scroll')]//table/tbody/tr/td/div[contains(@class, 'calendar_grid_body_time_text') and contains(text(), '9')]";
		while (!ClientSessionFactory.session().selenium().isElementPresent(element) && i > 0) {
			element = "//div[contains(@class, 'calendar_view')]["
					+ --i
					+ "]/div[contains(@class, 'calendar_hour_scroll')]//table/tbody/tr/td/div[contains(@class, 'calendar_grid_body_time_text') and contains(text(), '9')]";
		}
		if (ZimbraSeleniumProperties.getStringProperty("browser").equals("IE")) {
			Robot zRobot = new Robot();
			zRobot.mouseMove(500, 500);
			zRobot.mousePress(InputEvent.BUTTON3_MASK);
			zRobot.mouseRelease(InputEvent.BUTTON3_MASK);
		} else {
			ClientSessionFactory.session().selenium().mouseDownRight(element);
			ClientSessionFactory.session().selenium().mouseUpRight(element);
		}
		obj.zMenuItem.zClick(localize(locator.newAppt));

		// SleepUtil.sleep(1000);
		// obj.zButton
		// .zRtClick(
		// "xpath=//div[contains(@class, 'calendar_time_selection') and contains(@style, 'display: block') and contains(@style, 'left: 2px')]"
		// );
		// SleepUtil.sleep(1000);
		// obj.zMenuItem.zClick(localize(locator.newAppt));
		// time objects are changed
		// if
		// (ZimbraSeleniumProperties.getStringProperty("browser").contains("FF"))
		// {
		// page.zCalCompose.zVerifyStartEndTimeInQuickAddApptDlg(startTime,
		// "startTime");
		// page.zCalCompose.zVerifyStartEndTimeInQuickAddApptDlg(endTime,
		// "endTime");
		// }
		obj.zEditField.zTypeInDlgByName(localize(locator.subjectLabel),
				subject, localize(locator.quickAddAppt));
		obj.zButton.zClickInDlgByName(localize(locator.ok),
				localize(locator.quickAddAppt));
		SleepUtil.sleep(1500);
		ClientSessionFactory.session().selenium().doubleClickAt(
						"xpath=//div[contains(@class, 'ImgCalendarDayGrid')]/div[contains(@class, 'appt')]/div[contains(@class, 'appt_body')]/table/tbody/tr/td[contains(@class, appt_name) and contains(text(), '"
								+ subject + "')]", "");
		// time objects are changed
		// if
		// (!ZimbraSeleniumProperties.getStringProperty("browser").equals("IE"))
		// {
		// page.zCalCompose.zVerifyStartEndTime(startTime, "startTime");
		// page.zCalCompose.zVerifyStartEndTime(endTime, "endTime");
		// }
		obj.zButton.zClick(page.zCalCompose.zApptCloseBtn);
		closeWarningDlg();

		SelNGBase.needReset.set(false);
	}

	/**
	 * Steps, 1. Go to Calendar 2. Switch to Week view 3. Quick add all day
	 * appointment > add appointment 4. Verify created appointment
	 */
	@Test(dataProvider = "apptCreateDataProvider", groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void quickAddAllDayApptWeekView(String subject, String location,
			String attendees, String body, String startDate, String endDate,
			String startTime, String endTime) throws Exception {
		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		obj.zButton.zClick(page.zCalApp.zViewBtn);
		obj.zMenuItem.zClick(localize(locator.week));
		int i = 5;
		String element = "//div[contains(@class, 'calendar_view')]["
				+ i
				+ "]/div[contains(@class, 'calendar_hour_scroll')]//table/tbody/tr/td/div[contains(@class, 'calendar_grid_body_time_text') and contains(text(), '9')]";
		while (!ClientSessionFactory.session().selenium().isElementPresent(element) && i > 0) {
			element = "//div[contains(@class, 'calendar_view')]["
					+ --i
					+ "]/div[contains(@class, 'calendar_hour_scroll')]//table/tbody/tr/td/div[contains(@class, 'calendar_grid_body_time_text') and contains(text(), '9')]";
		}
		// obj.zButton.zRtClick(element);
		if (ZimbraSeleniumProperties.getStringProperty("browser").equals("IE")) {
			Robot zRobot = new Robot();
			zRobot.mouseMove(500, 500);
			zRobot.mousePress(InputEvent.BUTTON3_MASK);
			zRobot.mouseRelease(InputEvent.BUTTON3_MASK);
		} else {
			ClientSessionFactory.session().selenium().mouseDownRight(element);
			ClientSessionFactory.session().selenium().mouseUpRight(element);
		}
		obj.zMenuItem.zClick(localize(locator.newAllDayAppt));
		page.zCalCompose.zVerifyStartEndTimeInQuickAddApptDlgNotExists();
		obj.zEditField.zTypeInDlgByName(localize(locator.subjectLabel),
				subject, localize(locator.quickAddAppt));
		obj.zButton.zClickInDlgByName(localize(locator.ok),
				localize(locator.quickAddAppt));
		SleepUtil.sleep(1500);
		ClientSessionFactory.session().selenium().doubleClickAt(
						"xpath=//div[contains(@class, 'appt')]/table/tbody/tr/td[contains (@class, 'appt_allday_name') and contains(text(), '"
								+ subject + "')]", "");
		SleepUtil.sleep(2000);
		page.zCalCompose.zVerifyStartEndTimeNotExists();
		obj.zButton.zClick(page.zCalCompose.zApptCloseBtn);
		closeWarningDlg();

		SelNGBase.needReset.set(false);
	}

	/**
	 * Steps, 1. Go to Calendar 2. Switch to Month view 3. Quick add appointment
	 * > add appointment 4. Verify created appointment
	 */
	@Test(dataProvider = "apptCreateDataProvider", groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void quickAddApptMonthView(String subject, String location,
			String attendees, String body, String startDate, String endDate,
			String startTime, String endTime) throws Exception {
		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		obj.zButton.zClick(page.zCalApp.zViewBtn);
		obj.zMenuItem.zClick(localize(locator.viewMonth));
		obj.zButton
				.zRtClick("xpath=//div[contains(@id, 'DWT')]/table/tbody/tr/td/div/table/tbody/tr/td[contains (@id,'DWT') and contains(@class, 'calendar_month_day_label') and contains(text(), '10')]");
		SleepUtil.sleep(2000);
		obj.zMenuItem.zClick(localize(locator.newAppt));
		obj.zEditField.zTypeInDlgByName(localize(locator.subjectLabel),
				subject, localize(locator.quickAddAppt));
		page.zCalCompose.zSetStartTimeInQuickAddApptDlg(startTime);
		page.zCalCompose.zSetEndTimeInQuickAddApptDlg(endTime);
		obj.zButton.zClickInDlgByName(localize(locator.ok),
				localize(locator.quickAddAppt));
		SleepUtil.sleep(1500);
		ClientSessionFactory.session().selenium().doubleClickAt(
				"xpath=//td[contains(@class, 'calendar_month_day_item') and contains(text(), "
						+ subject + ")]", "");
		// time objects are changed
		// page.zCalCompose.zVerifyStartEndTime(startTime, "startTime");
		// page.zCalCompose.zVerifyStartEndTime(endTime, "endTime");
		obj.zButton.zClick(page.zCalCompose.zApptCloseBtn);
		closeWarningDlg();

		SelNGBase.needReset.set(false);
	}

	/**
	 * Steps, 1. Go to Calendar 2. Switch to Month view 3. Quick add all day
	 * appointment > add appointment 4. Verify created appointment
	 */
	@Test(dataProvider = "apptCreateDataProvider", groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void quickAddAllDayApptMonthView(String subject, String location,
			String attendees, String body, String startDate, String endDate,
			String startTime, String endTime) throws Exception {
		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		obj.zButton.zClick(page.zCalApp.zViewBtn);
		obj.zMenuItem.zClick(localize(locator.viewMonth));

		obj.zButton
				.zRtClick("xpath=//div[contains(@id, 'DWT')]/table/tbody/tr/td/div/table/tbody/tr/td[contains (@id,'DWT') and contains(@class, 'calendar_month_day_label') and contains(text(), '11')]");
		obj.zMenuItem.zClick(localize(locator.newAllDayAppt));
		obj.zEditField.zTypeInDlgByName(localize(locator.subjectLabel),
				subject, localize(locator.quickAddAppt));
		obj.zButton.zClickInDlgByName(localize(locator.ok),
				localize(locator.quickAddAppt));
		SleepUtil.sleep(1500);
		ClientSessionFactory.session().selenium().doubleClickAt(
						"xpath=//div[contains(@class, 'appt')]/table/tbody/tr/td[contains (@class, 'appt_allday_name') and contains(text(), '"
								+ subject + "')]", "");
		SleepUtil.sleep(2000);
		page.zCalCompose.zVerifyStartEndTimeNotExists();
		obj.zButton.zClick(page.zCalCompose.zApptCloseBtn);
		closeWarningDlg();

		SelNGBase.needReset.set(false);
	}

	/**
	 * Steps, 1. Go to Calendar 2. Switch to Month view 3. Open fish eye view >
	 * add appointment 4. Verify created appointment
	 */
	@Test(dataProvider = "apptCreateDataProvider", groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void quickAddApptFishEyeMonthView(String subject, String location,
			String attendees, String body, String startDate, String endDate,
			String startTime, String endTime) throws Exception {
		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		obj.zButton.zClick(page.zCalApp.zViewBtn);
		obj.zMenuItem.zClick(localize(locator.viewMonth));

		obj.zButton
				.zClick("xpath=//div[contains(@id, 'DWT')]/table/tbody/tr/td/div/table/tbody/tr/td[contains (@id,'DWT') and contains(@class, 'calendar_month_day_label') and contains(text(), '12')]");
		obj.zButton
				.zRtClick("xpath=//div[contains(@class, 'calendar_grid_body_time_text') and contains(text(), 8)]");

		obj.zMenuItem.zClick(localize(locator.newAppt));
		obj.zEditField.zTypeInDlgByName(localize(locator.subjectLabel),
				subject, localize(locator.quickAddAppt));
		page.zCalCompose.zSetStartTimeInQuickAddApptDlg(startTime);
		page.zCalCompose.zSetEndTimeInQuickAddApptDlg(endTime);
		obj.zButton.zClickInDlgByName(localize(locator.ok),
				localize(locator.quickAddAppt));
		SleepUtil.sleep(1500);
		ClientSessionFactory.session().selenium().doubleClickAt(
						"xpath=//div[contains(@class, 'appt_body')]//td[contains(@class, 'appt_name') and contains(text(), "
								+ subject + ")]", "");
		// time objects are changed
		// page.zCalCompose.zVerifyStartEndTime(startTime, "startTime");
		// page.zCalCompose.zVerifyStartEndTime(endTime, "endTime");
		obj.zButton.zClick(page.zCalCompose.zApptCloseBtn);
		closeWarningDlg();
		ClientSessionFactory.session().selenium().click("xpath=//div[contains(@class, 'appt_body')]//td[contains(@class, 'appt_name') and contains(text(), "
						+ subject + ")]");
		obj.zButton.zClick("ImgClose");

		SelNGBase.needReset.set(false);
	}

	/**
	 * Steps, 1. Go to Calendar 2. Switch to Month view 3. Open fish eye view >
	 * add all day appointment 4. Verify created appointment
	 */
	@Test(dataProvider = "apptCreateDataProvider", groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void quickAddAllDayApptFishEyeMonthView(String subject,
			String location, String attendees, String body, String startDate,
			String endDate, String startTime, String endTime) throws Exception {
		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		obj.zButton.zClick(page.zCalApp.zViewBtn);
		obj.zMenuItem.zClick(localize(locator.viewMonth));

		obj.zButton
				.zClick("xpath=//div[contains(@id, 'DWT')]/table/tbody/tr/td/div/table/tbody/tr/td[contains (@id,'DWT') and contains(@class, 'calendar_month_day_label') and contains(text(), '12')]");
		obj.zButton
				.zRtClick("xpath=//div[contains(@class, 'calendar_grid_body_time_text') and contains(text(), 8)]");
		obj.zMenuItem.zClick(localize(locator.newAllDayAppt));
		page.zCalCompose.zVerifyStartEndTimeInQuickAddApptDlgNotExists();
		obj.zEditField.zTypeInDlgByName(localize(locator.subjectLabel),
				subject, localize(locator.quickAddAppt));
		obj.zButton.zClickInDlgByName(localize(locator.ok),
				localize(locator.quickAddAppt));
		SleepUtil.sleep(1500);
		ClientSessionFactory.session().selenium().doubleClickAt(
						"xpath=//div[contains(@class, 'appt_allday_body')]//td[contains(@class, 'appt_allday_name') and contains(text(), "
								+ subject + ")]", "");
		page.zCalCompose.zVerifyStartEndTimeNotExists();
		obj.zButton.zClick(page.zCalCompose.zApptCloseBtn);
		closeWarningDlg();
		obj.zButton.zClick("ImgClose");

		SelNGBase.needReset.set(false);
	}

	/**
	 * Steps, 1. Go to Calendar 2. Switch to Month view 3. Find our Monday 4.
	 * Open fish eye view > add appointment 5. Verify created appointment and
	 * dat displayed.
	 */
	@Test(dataProvider = "apptCreateDataProvider", groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void fishEyeOpensWrongDayOnMonday_Bug45184(String subject,
			String location, String attendees, String body, String startDate,
			String endDate, String startTime, String endTime) throws Exception {
		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		obj.zButton.zClick(page.zCalApp.zViewBtn);
		obj.zMenuItem.zClick(localize(locator.viewMonth));

		/**
		 * Find out Monday
		 */
		for (int i = 11; i <= 17; i++) {
			obj.zButton
					.zClick("xpath=//div[contains(@id, 'DWT')]/table/tbody/tr/td/div/table/tbody/tr/td[contains (@id,'DWT') and contains(@class, 'calendar_month_day_label') and contains(text(), '"
							+ i + "')]");
			SleepUtil.sleep(1000);
			if (ClientSessionFactory.session().selenium().isElementPresent(
							"//*[contains(text(), 'Monday') and contains(@class,'calendar_heading_day')]")) {
				obj.zButton
						.zRtClick("xpath=//div[contains(@class, 'calendar_grid_body_time_text') and contains(text(), 8)]");
				break;
			}

		}

		obj.zMenuItem.zClick(localize(locator.newAppt));
		obj.zEditField.zTypeInDlgByName(localize(locator.subjectLabel),
				subject, localize(locator.quickAddAppt));
		page.zCalCompose.zSetStartTimeInQuickAddApptDlg(startTime);
		page.zCalCompose.zSetEndTimeInQuickAddApptDlg(endTime);
		obj.zButton.zClickInDlgByName(localize(locator.ok),
				localize(locator.quickAddAppt));
		SleepUtil.sleep(1500);
		ClientSessionFactory.session().selenium().doubleClickAt(
						"xpath=//div[contains(@class, 'appt_body')]//td[contains(@class, 'appt_name') and contains(text(), "
								+ subject + ")]", "");
		obj.zButton.zClick(page.zCalCompose.zApptCloseBtn);
		closeWarningDlg();
		ClientSessionFactory.session().selenium().click("xpath=//div[contains(@class, 'appt_body')]//td[contains(@class, 'appt_name') and contains(text(), "
						+ subject + ")]");
		Assert.assertTrue(ClientSessionFactory.session().selenium().isElementPresent(
						"//*[contains(text(), 'Monday') and contains(@class,'calendar_heading_day')]"));
		obj.zButton.zClick("ImgClose");

		SelNGBase.needReset.set(false);
	}

	private void closeWarningDlg() throws Exception {
		// if
		// (ZimbraSeleniumProperties.getStringProperty("browser").equals("IE"))
		// {
		String isDlgExists;
		for (int i = 0; i <= 5; i++) {
			SleepUtil.sleep(500);
			isDlgExists = obj.zDialog
					.zExistsDontWait(localize(locator.warningMsg));
			if (isDlgExists.equals("true")) {
				obj.zButton.zClickInDlgByName(localize(locator.no),
						localize(locator.warningMsg));
				SleepUtil.sleep(1000);
				break;
			}
		}
		// }
	}
}
