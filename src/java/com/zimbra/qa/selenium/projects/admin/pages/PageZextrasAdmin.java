package com.zimbra.qa.selenium.projects.admin.pages;

import com.zimbra.qa.selenium.framework.ui.AbsApplication;
import com.zimbra.qa.selenium.framework.ui.AbsPage;
import com.zimbra.qa.selenium.framework.ui.AbsTab;
import com.zimbra.qa.selenium.framework.ui.AbsWizard;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;

public class PageZextrasAdmin extends AbsTab{

	public static class Locators {
		public static final String AdminTab = "css=div[id='ztab__ZxAdmin']:not([aria-hidden='true'])";
		public static final String Admin = "css=td[id^='zti__AppAdmin__ZeXtras__ZxAdmin']";
		public static final String DelegatedAddButton = "css=div[id='ztab__ZxAdmin']:not([aria-hidden='true']) table[id='ztabv__ZxAdmin_delegatedAdmins_table'] td.ZWidgetTitle:contains('Add')";
		public static final String DelegatedEditButton = "css=div[id='ztab__ZxAdmin']:not([aria-hidden='true']) table[id='ztabv__ZxAdmin_delegatedAdmins_table'] td.ZWidgetTitle:contains('Edit')";
		public static final String DelegatedDeleteButton = "css=div[id='ztab__ZxAdmin']:not([aria-hidden='true']) table[id='ztabv__ZxAdmin_delegatedAdmins_table'] td.ZWidgetTitle:contains('Delete')";
		public static final String Network_NG_ICON="css=div.ImgZeXtras";
		public static final String DeleteDialogHeader = "css=div[class='DwtDialog']:not([aria-hidden='true']) td[class='DwtDialogTitle']:contains('Delete permissions')";
		public static final String DeleteDialogYesButton = "css=div[class='DwtDialog']:not([aria-hidden='true']) td[class='ZWidgetTitle']:contains('Yes')";
		public static final String zSaveDailog = "css=div.DwtDialog[style*='display: block;'] table tr:contains('Saving Settings')";
		public static final String zSavedialogCloseButton = "css=div.DwtDialog[style*='display: block;'] td.ZWidgetTitle:contains('Close')";
		public static final String zResetDomainDialog = "css=div[class='DwtDialog']:not([aria-hidden='true']) td[class='DwtDialogTitle']:contains('Reset Domain settings')";
		public static final String zResetDomainDialogYesButton = "css=div[class='DwtDialog']:not([aria-hidden='true']) 	td[class='ZWidgetTitle']:contains('Yes')";
		public static final String zDomainResetButton = "css=div[id='ztab__ZxAdmin']:not([aria-hidden='true']) table[id='ztabv__ZxAdmin_domainSettings_table'] td.ZWidgetTitle:contains('Reset')";
		public static final String zDomainEditButton = "css=div[id='ztab__ZxAdmin']:not([aria-hidden='true']) table[id='ztabv__ZxAdmin_domainSettings_table'] td.ZWidgetTitle:contains('Edit')";
		public static final String zCompleteResetDialogOkButton = "css=div[class='DwtDialog']:not([aria-hidden='true']) td[class='ZWidgetTitle']:contains('OK')";
	}

	public PageZextrasAdmin(AbsApplication application) {
		super(application);
		logger.info("new " + myPageName());
	}

	@Override
	public void zNavigateTo() throws HarnessException {

		if (zIsActive()) {
			return;
		}

		SleepUtil.sleepLong();
		sClickAt(Locators.Network_NG_ICON, "");
		zWaitForWorkInProgressDialogInVisible();
		sClickAt(Locators.Admin, "");
		zWaitForWorkInProgressDialogInVisible();

	}

	@Override
	public boolean zIsActive() throws HarnessException {

		// Make sure the Admin Console is loaded in the browser
		if (!MyApplication.zIsLoaded())
			throw new HarnessException("Admin Console application is not active!");

		boolean present = sIsElementPresent(Locators.AdminTab);
		if (!present) {
			return (false);
		}

		boolean visible = zIsVisiblePerPosition(Locators.DelegatedAddButton, 0, 0);
		if (!visible) {
			logger.debug("isActive() visible = " + visible);
			return (false);
		}

		return (true);
	}

	public AbsWizard zPressButton(Button button) throws HarnessException {
		logger.info(myPageName() + " zPressButton(" + button + ")");

		tracer.trace("Press the " + button + " button");

		if (button == null)
			throw new HarnessException("Button cannot be null!");

		String locator = null;
		AbsWizard wizard = null;

		if (button==Button.B_ADD_ACCOUNT) {
			locator = Locators.DelegatedAddButton;
			wizard = new WizardAddDelegatedNG(this);
		}

		if (button==Button.B_EDIT_ACCOUNT) {
			locator = Locators.DelegatedEditButton;
			wizard = new WizardAddDelegatedNG(this);
		}

		if (button==Button.B_DELETE_ACCOUNT) {
			locator = Locators.DelegatedDeleteButton;
		}

		if (button==Button.B_EDIT_DOMAIN) {
			locator = Locators.zDomainEditButton;
			wizard = new WizardDomainSettingNG(this);
		}

		if (button==Button.B_RESET_DOMAIN) {
			locator = Locators.zDomainResetButton;
			sClick(locator);

			if(zWaitForElementPresent(Locators.zResetDomainDialog)){
				sClick(Locators.zResetDomainDialogYesButton);

				if(zWaitForElementPresent(Locators.zCompleteResetDialogOkButton))
					sClick(Locators.zCompleteResetDialogOkButton);

				return null;
			} else {
				throw new HarnessException("Reset Domain Settings Dialog is not appeared");
			}
		}

		this.sClick(locator);
		SleepUtil.sleepLong();

		if(button==Button.B_DELETE_ACCOUNT) {
			if (zWaitForElementPresent(Locators.DeleteDialogHeader)) {
				sClick(Locators.DeleteDialogYesButton);

				if(zWaitForElementPresent(Locators.zSaveDailog)) {
					sClick(Locators.zSavedialogCloseButton);
				} else {
					throw new HarnessException("Save Settings Dialog is not appeared");
				}
			} else {
				throw new HarnessException("Delete Dialog not appeared");
			}
		}

		return wizard;
	}

	@Override
	public String myPageName() {
		return (this.getClass().getName());
	}

	public void SelectAccountRow(String email) throws HarnessException {
		sClick("xpath=//tr/descendant::div[@title='"+email+"']");
	}

	public Boolean IsAccountPresentonUI(String email) throws HarnessException {
		return sIsElementPresent("xpath=//tr/descendant::div[@title='"+email+"']");
	}

	public String GetDomainfromUI(String email) throws HarnessException{
		return sGetText("xpath=//tr/descendant::div[@title='"+email+"']//parent::td/following-sibling::td[1]/div");
	}

	public String GetDelegateAuthfromUI(String email) throws HarnessException{
		return sGetText("xpath=//tr/descendant::div[@title='"+email+"']//parent::td/following-sibling::td[2]/div");
	}

	public String GetEditFeaturesfromUI(String email) throws HarnessException{
		return sGetText("xpath=//tr/descendant::div[@title='"+email+"']//parent::td/following-sibling::td[3]/div");
	}

	public String GetGrantLimitfromUI(String email) throws HarnessException{
		return sGetText("xpath=//tr/descendant::div[@title='"+email+"']//parent::td/following-sibling::td[4]/div");
	}

	public void SelectDomainRow(String domain) throws HarnessException {
		sClick("css=table[id='ztabv__ZxAdmin_domainSettings_table'] div[title='"+domain+"']");
	}

	public Boolean IsDomainPresentonUI(String domain) throws HarnessException {
		return sIsElementPresent("xpath=//table[@id='ztabv__ZxAdmin_domainSettings_table']//tr/descendant::div[@title='" + domain + "']");
	}

	public String GetGlobalLimitfromUI(String domain) throws HarnessException {
		return sGetText("xpath=//table[@id='ztabv__ZxAdmin_domainSettings_table']//tr/descendant::div[@title='" + domain + "']//parent::td/following-sibling::td[2]/div");
	}

	public String GetDomainQuotafromUI(String domain) throws HarnessException {
		return sGetText("xpath=//table[@id='ztabv__ZxAdmin_domainSettings_table']//tr/descendant::div[@title='" + domain + "']//parent::td/following-sibling::td[3]/div");
	}

	public String GetCosNumberfromUI(String domain) throws HarnessException {
		return sGetText("xpath=//table[@id='ztabv__ZxAdmin_domainSettings_table']//tr/descendant::div[@title='" + domain + "']//parent::td/following-sibling::td[1]/div");
	}


	@Override
	public AbsPage zListItem(Action action, String item) throws HarnessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbsPage zListItem(Action action, Button option, String item) throws HarnessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbsPage zListItem(Action action, Button option, Button subOption, String item) throws HarnessException {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public AbsPage zToolbarPressButton(Button button) throws HarnessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbsPage zToolbarPressPulldown(Button pulldown, Button option) throws HarnessException {
		// TODO Auto-generated method stub
		return null;
	}

}
