package com.zimbra.qa.selenium.projects.admin.pages;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import com.zimbra.qa.selenium.framework.items.IItem;
import com.zimbra.qa.selenium.framework.ui.AbsTab;
import com.zimbra.qa.selenium.framework.ui.AbsWizard;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.projects.admin.items.DelegatedAdminNGItem;
import com.zimbra.qa.selenium.projects.admin.items.DelegatedAdminNGItem.Grantlimt;


public class WizardAddDelegatedNG extends AbsWizard {

	public WizardAddDelegatedNG(AbsTab page) {
		super(page);
	}

	public static class Locators {
		public static final String zAddNewDelegatedAdminDialog = "css=div[class='DwtDialog']:not([aria-hidden='true']) > div:contains('delegated admin')";
		public static final String zAccountField = zAddNewDelegatedAdminDialog + " input[id$='admin_display']";
		public static final String zDomainField = zAddNewDelegatedAdminDialog + " input[id$='domain_display']";
		public static final String zDelegatedAuth = "div[class='DwtDialog']:not([aria-hidden='true']) input[id$='viewMail']";
		public static final String zEditFeatures = "div[class='DwtDialog']:not([aria-hidden='true']) input[id$='editFeatures']";
		public static final String zGrantLimitdropdown = zAddNewDelegatedAdminDialog + " div.ImgSelectPullDownArrow";
		public static final String zGrantUnlimited = "css=div[id='___OSELECT_MENU___'] table tr td div:contains('Unlimited')";
		public static final String zGrantNone = "css=div[id='___OSELECT_MENU___'] table tr td div:contains('None')";
		public static final String zGrantCustom = "css=div[id='___OSELECT_MENU___'] table tr td div:contains('Custom')";
		public static final String zGrantValue = zAddNewDelegatedAdminDialog + " input[id$='adminQuotaId']";
		public static final String zOkbutton = "div.DwtDialog:not([aria-hidden='true']) td[class='ZWidgetTitle']:contains('OK')";
		public static final String zSaveDailog = "div.DwtDialog[style*='display: block;'] table tr:contains('Saving Settings')";
		public static final String zSavedialogClosebtn = "div.DwtDialog[style*='display: block;'] td.ZWidgetTitle:contains('Close')";
	}

	@Override
	public IItem zCompleteWizard(IItem item) throws HarnessException {

		if (!(item instanceof DelegatedAdminNGItem))
			throw new HarnessException("item must be an DelegatedAdminNGItem, was " + item.getClass().getCanonicalName());

		DelegatedAdminNGItem delegatedadmin = (DelegatedAdminNGItem)item;

		if (delegatedadmin.getAccountAddress() != null) {
			sType(Locators.zAccountField, delegatedadmin.getAccountAddress());
			SleepUtil.sleepLong();
			sClick("css=div[id='___OSELECT_MENU___'] table tr td div");
		}

		if (delegatedadmin.getDomain() != null) {
			sType(Locators.zDomainField, delegatedadmin.getDomain());
			SleepUtil.sleepLong();
			sClick("css=div[id='___OSELECT_MENU___'] table tr td div");
		}

		if (delegatedadmin.getDelegatedAuth() != null) {
			if(delegatedadmin.getDelegatedAuth()) {
				if(!sIsElementPresent(Locators.zDelegatedAuth + ":checked"))
					sClick(Locators.zDelegatedAuth);
			} else {
				if(sIsElementPresent(Locators.zDelegatedAuth + ":checked"))
					sClick(Locators.zDelegatedAuth);
			}
		}

		if (delegatedadmin.getEditFeatures() != null) {
			if(delegatedadmin.getEditFeatures()) {
				if(!sIsElementPresent(Locators.zEditFeatures + ":checked"))
					sClick(Locators.zEditFeatures);
			} else {
				if(sIsElementPresent(Locators.zEditFeatures + ":checked"))
					sClick(Locators.zEditFeatures);
			}
		}

		if (delegatedadmin.getGrantLimit() != null) {
			sClick(Locators.zGrantLimitdropdown);

			if (delegatedadmin.getGrantLimit() == Grantlimt.None) {
				sClick(Locators.zGrantNone);
			} else if (delegatedadmin.getGrantLimit() == Grantlimt.Unlimited) {
				sClick(Locators.zGrantUnlimited);
			} else {
				sClick(Locators.zGrantCustom);
				sType(Locators.zGrantValue, delegatedadmin.getGrantCustomValue());
			}
		}
		sClick(Locators.zOkbutton);

		if(zWaitForElementPresent(Locators.zSaveDailog)) {
			sClick(Locators.zSavedialogClosebtn);
		} else {
			throw new HarnessException("Save Settings Dialog is not appeared");
		}

		return null;
	}


	@Override
	public String myPageName() {
		return (this.getClass().getName());
	}

	@Override
	public boolean zIsActive() throws HarnessException {
		logger.info(myPageName() + " zIsActive()");

		String locator = Locators.zAddNewDelegatedAdminDialog;

		if (!this.sIsElementPresent(locator)) {
			return (false);
		}

		if (!this.zIsVisiblePerPosition(locator, 0, 0)) {
			return (false);
		}

		logger.info(myPageName() + " zIsActive() = true");
		return (true);
	}

	public void zKeyboardKeyEvent(Keys event) throws HarnessException {
		this.zWaitForBusyOverlay();
		String locator = "//html//body";
		WebElement we = getElement(locator);
		we.sendKeys(event);

	}
}
