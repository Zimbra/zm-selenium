package com.zimbra.qa.selenium.projects.admin.pages;

import com.zimbra.qa.selenium.framework.ui.AbsApplication;
import com.zimbra.qa.selenium.framework.ui.AbsPage;
import com.zimbra.qa.selenium.framework.ui.AbsTab;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;

public class PageZextrasMobile extends AbsTab{
	
	public static class Locators {
		public static final String NETWORK_MODULE_NG_ICON = "css=td[id^='zti__AppAdmin__Home__ZeXtras_'] div[class='ImgZeXtras']";
		public static final String CLOSE_BUTTON = "css=td[id^='zb__ZaCurrentAppBar__CLOSE_']:contains('Close')";
		public static final String HOME = "Home";
		public static final String NETWORK_MODULE_NG = "Network Modules NG";
		public static final String MOBILE = "Mobile";
		public static final String MOBILE_TAB = "css=td[id^='zti__AppAdmin__ZeXtras__ZxMobile_']:contains('Mobile')";
		public static final String MOBILE_DEVICE_MANAGEMENT = "xpath=//input[contains(@id,'ZxMobile_DeviceManagementEnabled')]";
		public static final String OK_BUTTON = "css=div[class='DwtDialog']:not([aria-hidden='true']) td[class='ZWidgetTitle']:contains('OK')";
		public static final String SAVE_BUTTON = "css=td[id^='zb__ZaCurrentAppBar__SAVE_']:contains('Save')";

	}

	public PageZextrasMobile(AbsApplication application) {
		super(application);
		logger.info("new " + myPageName());
	}
	
	@Override
	public String myPageName() {
		return (this.getClass().getName());
	}

	@Override
	public boolean zIsActive() throws HarnessException {

		// Make sure the Admin Console is loaded in the browser
		if (!MyApplication.zIsLoaded())
			throw new HarnessException("Admin Console application is not active!");

		boolean present = sIsElementPresent(Locators.CLOSE_BUTTON);
		if (!present) {
			return (false);
		}

		boolean visible = zIsVisiblePerPosition(Locators.CLOSE_BUTTON, 0, 0);
		if (!visible) {
			logger.debug("isActive() visible = " + visible);
			return (false);
		}
		return (true);
	}

	@Override
	public void zNavigateTo() throws HarnessException {

		if (zIsActive() && zVerifyHeader(Locators.MOBILE)) {
			return;
		}

		SleepUtil.sleepLong();
		sClickAt(Locators.NETWORK_MODULE_NG_ICON, "");
		zWaitForWorkInProgressDialogInVisible();
		sClickAt(Locators.MOBILE_TAB, "");
		zWaitForWorkInProgressDialogInVisible();
	}
	
	public boolean zVerifyHeader(String header) throws HarnessException {
		if (this.sIsElementPresent("css=span:contains('" + header + "')"))
			return true;
		return false;
	}


	@Override
	public AbsPage zListItem(Action action, String item) throws HarnessException {
		return null;
	}

	@Override
	public AbsPage zListItem(Action action, Button option, String item) throws HarnessException {
		return null;
	}

	@Override
	public AbsPage zListItem(Action action, Button option, Button subOption, String item) throws HarnessException {
		return null;
	}

	@Override
	public AbsPage zToolbarPressButton(Button button) throws HarnessException {
		return null;
	}

	@Override
	public AbsPage zToolbarPressPulldown(Button pulldown, Button option) throws HarnessException {
		return null;
	}

	public void zEnableMobileDeviceManagement() throws HarnessException {
		sCheck(Locators.MOBILE_DEVICE_MANAGEMENT);
		SleepUtil.sleepLong();
		if (this.sIsElementPresent(Locators.OK_BUTTON)) {
			sClick(Locators.OK_BUTTON);
		}
	}
	
	public void zSave() throws HarnessException {
		SleepUtil.sleepSmall();
		sClickAt(Locators.SAVE_BUTTON, "");
		SleepUtil.sleepLong();
	}
	
	public void zClose() throws HarnessException {
		SleepUtil.sleepSmall();
		sClickAt(Locators.CLOSE_BUTTON, "");
		SleepUtil.sleepLong();
	}

}
