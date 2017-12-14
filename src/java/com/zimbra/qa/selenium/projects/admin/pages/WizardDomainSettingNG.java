package com.zimbra.qa.selenium.projects.admin.pages;

import com.zimbra.qa.selenium.framework.items.IItem;
import com.zimbra.qa.selenium.framework.ui.AbsTab;
import com.zimbra.qa.selenium.framework.ui.AbsWizard;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.projects.admin.items.DomainNGItem;
import com.zimbra.qa.selenium.projects.admin.items.DomainNGItem.CosAction;

public class WizardDomainSettingNG extends AbsWizard {

	public WizardDomainSettingNG(AbsTab page) {
		super(page);
	}

	public static class Locators {
		public static final String zEditDomainDialog = "css=div[class='DwtDialog']:not([aria-hidden='true']) > div:contains('Edit Domain limits')";
		public static final String zAccountLimitField = zEditDomainDialog + " input[id$='accountLimit']";
		public static final String zDomainQuotaField = zEditDomainDialog + " input[id$='domainAccountQuota']";
		public static final String zCosLimitAddbtn = "css=div[class='DwtDialog']:not([aria-hidden='true']) td[class='ZWidgetTitle']:contains('Add')";
		public static final String zCosLimitEditbtn = "css=div[class='DwtDialog']:not([aria-hidden='true']) td[class='ZWidgetTitle']:contains('Edit')";
		public static final String zCosLimitDeletebtn = "css=div[class='DwtDialog']:not([aria-hidden='true']) td[class='ZWidgetTitle']:contains('Delete')";
		public static final String zCosListPanel = "xpath=//div[@class='DwtDialog'][not(contains(@aria-hidden,'true'))]";
		public static final String zOkbtnEditDomainDialog = "css=div[class='DwtDialog']:not([aria-hidden='true']) td[class='ZWidgetTitle']:contains('OK')";
		public static final String zRemoveDomainDialog = "css=div[class='DwtDialog']:not([aria-hidden='true'])[style*='z-index: 702;'] > div:contains('Remove Domain')";
		public static final String zRemoveDomainDialogYesbtn = "css=div[class='DwtDialog']:not([aria-hidden='true'])[style*='z-index: 702;'] td[class='ZWidgetTitle']:contains('Yes')";
		public static final String zSaveDialogOkbtn = "css=div[class='DwtDialog']:not([aria-hidden='true']) td[class='ZWidgetTitle']:contains('OK')";
	}

	@Override
	public IItem zCompleteWizard(IItem item) throws HarnessException {

		if (!(item instanceof DomainNGItem))
			throw new HarnessException("item must be an DomainNGItem, was " + item.getClass().getCanonicalName());

		DomainNGItem domainitem = (DomainNGItem)item;

		if (domainitem.getGlobalAccountLimit()!=null) {
			sType(Locators.zAccountLimitField, domainitem.getGlobalAccountLimit());
		}
		if (domainitem.getDomainQuota()!=null) {
			sType(Locators.zDomainQuotaField, domainitem.getDomainQuota());
		}

		if(domainitem.getCosName() != null) {

			if (domainitem.getCosAction() == null) {
				throw new HarnessException("Cos Action is not defined");
			}

			if (domainitem.getCosAction() == CosAction.Add) {
				//Click on Add button of COS Limit panel
				sClick(Locators.zCosLimitAddbtn);

				WizardAddCosLimitNG wizardCos = new WizardAddCosLimitNG(this.MyPage);
				wizardCos.zCompleteWizard(domainitem);
			} else if (domainitem.getCosAction() == CosAction.Edit) {
				//select the Cos
				SelectCosRow(domainitem.getCosName());
				sClick(Locators.zCosLimitEditbtn);

				domainitem.setCosName(null);

				WizardAddCosLimitNG wizardCos = new WizardAddCosLimitNG(this.MyPage);
				wizardCos.zCompleteWizard(domainitem);
			} else {
				//select the Cos
				SelectCosRow(domainitem.getCosName());
				sClick(Locators.zCosLimitDeletebtn);

				if(zWaitForElementPresent(Locators.zRemoveDomainDialog)) {
					sClick(Locators.zRemoveDomainDialogYesbtn);
				} else {
					throw new HarnessException("Remove Domain Dialog Not appeared");
				}
			}
		}
		return null;
	}

	public void zCloseWizard() throws HarnessException {
		sClick(Locators.zOkbtnEditDomainDialog);
		SleepUtil.sleepMedium();
		if (zWaitForElementPresent(Locators.zSaveDialogOkbtn))
			sClick("css=div[class='DwtDialog']:not([aria-hidden='true']) td[class='ZWidgetTitle']:contains('OK')");
		else
			throw new HarnessException("Save dialog is not appeared");
	}

	public Boolean zIsCosNamePresent(String cosname) throws HarnessException{
		return sIsElementPresent(Locators.zCosListPanel + "//tr/descendant::div[@title='"+cosname+"']");
	}

	public String zGetCosAccountLimit(String cosname) throws HarnessException{
		return sGetText(Locators.zCosListPanel + "//tr/descendant::div[@title='"+cosname+"']//parent::td/following-sibling::td[1]/div");
	}

	private void SelectCosRow(String cosname) throws HarnessException {
		if (!zIsCosNamePresent(cosname)) {
			throw new HarnessException("Cos is not present on the Cos Limits panel");
		} else {
			sClick(Locators.zCosListPanel + "//tr/descendant::div[@title='"+cosname+"']");
		}

	}

	@Override
	public String myPageName() {
		return (this.getClass().getName());
	}

	@Override
	public boolean zIsActive() throws HarnessException {
		logger.info(myPageName() + " zIsActive()");

		String locator = Locators.zEditDomainDialog;

		if (!this.sIsElementPresent(locator)) {
			return (false);
		}

		if (!this.zIsVisiblePerPosition(locator, 0, 0)) {
			return (false);
		}

		logger.info(myPageName() + " zIsActive() = true");
		return (true);
	}

}
