package com.zimbra.qa.selenium.projects.admin.pages;

import com.zimbra.qa.selenium.framework.items.IItem;
import com.zimbra.qa.selenium.framework.ui.AbsTab;
import com.zimbra.qa.selenium.framework.ui.AbsWizard;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.projects.admin.items.DomainNGItem;

public class WizardAddCosLimitNG extends AbsWizard {

	public WizardAddCosLimitNG(AbsTab page) {
		super(page);
	}

	public static class Locators {
		public static final String zAddCosLimitDialog = "css=div[class='DwtDialog']:not([aria-hidden='true'])[style*='z-index: 702;'] > div:contains('Add COS Limit')";
		public static final String zCosNameField = zAddCosLimitDialog + " input[id$='cosName_display']";
		public static final String zCosAccountLimitField = "css=div[class='DwtDialog']:not([aria-hidden='true'])[style*='z-index: 702;'] input[id$='cosLimit']";
		public static final String zOkbtn = "css=div[class='DwtDialog']:not([aria-hidden='true'])[style*='z-index: 702;'] td[class='ZWidgetTitle']:contains('OK')";
	}

	@Override
	public IItem zCompleteWizard(IItem item) throws HarnessException {

		if (!(item instanceof DomainNGItem))
			throw new HarnessException("item must be an DomainNGItem, was " + item.getClass().getCanonicalName());

		DomainNGItem domainitem = (DomainNGItem)item;

		if(domainitem.getCosName() != null) {
			sType(Locators.zCosNameField, domainitem.getCosName());
			SleepUtil.sleepLong();
			sClick("css=div[id='___OSELECT_MENU___'] table tr td div");
		}

		if(domainitem.getCosAccountLimit() != null) {
			sType(Locators.zCosAccountLimitField, domainitem.getCosAccountLimit());
		}

		sClick(Locators.zOkbtn);

		return null;
	}

	@Override
	public String myPageName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean zIsActive() throws HarnessException {
		// TODO Auto-generated method stub
		return false;
	}

}
