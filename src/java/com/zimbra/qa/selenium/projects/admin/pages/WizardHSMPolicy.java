package com.zimbra.qa.selenium.projects.admin.pages;

import com.zimbra.qa.selenium.framework.items.IItem;
import com.zimbra.qa.selenium.framework.ui.AbsTab;
import com.zimbra.qa.selenium.framework.ui.AbsWizard;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.projects.admin.items.HSMPolicyItem;

public class WizardHSMPolicy extends AbsWizard {

	public WizardHSMPolicy(AbsTab page) {
		super(page);
	}
	
	public static class Locators {
		public static final String addNewHSMPolicyDialog = "css=div[class='DwtDialog']:not([aria-hidden='true'])";
		public static final String emailsItemCheckbox = "css=div[class='DwtDialog']:not([aria-hidden='true']) input[id$='_hsmType_3']";
		public static final String documentsItemCheckbox = "css=div[class='DwtDialog']:not([aria-hidden='true']) input[id$='_hsmType_4']";
		public static final String tasksItemCheckbox = "css=div[class='DwtDialog']:not([aria-hidden='true']) input[id$='_hsmType_5']";
		public static final String appointmentsItemCheckbox = "css=div[class='DwtDialog']:not([aria-hidden='true']) input[id$='_hsmType_6']";
		public static final String contactsItemCheckbox = "css=div[class='DwtDialog']:not([aria-hidden='true']) input[id$='_hsmType_7']";
		public static final String chatsItemCheckbox = "css=div[class='DwtDialog']:not([aria-hidden='true']) input[id$='_hsmType_8']";
		public static final String olderThan = "css=div[class='DwtDialog']:not([aria-hidden='true']) input[id$='_hsmQuery_3']";
		public static final String pullDown = "css=div[class='DwtDialog']:not([aria-hidden='true']) div.ImgSelectPullDownArrow";
		public static final String okButton = "css=div[class='DwtDialog']:not([aria-hidden='true']) td[class='ZWidgetTitle']:contains('OK')";
		public static final String successfulDialog = "css=div.DwtDialog[style*='display: block;'] table td:contains('Zimbra Administration')";
	}
	
	@Override
	public IItem zCompleteWizard(IItem item) throws HarnessException {
		
		if(!(item instanceof HSMPolicyItem))
			throw new HarnessException("item must be an HSMPolicyItem, was " + item.getClass().getCanonicalName());
		
		HSMPolicyItem policy = (HSMPolicyItem)item;
		
		if(policy.getEmailItem() != null) {
			if(policy.getEmailItem()){
				sCheck(Locators.emailsItemCheckbox);
			}else {
				sUncheck(Locators.emailsItemCheckbox);
			}
		}
		if(policy.getDocumentItem() != null){
			if(policy.getEmailItem()){
				sCheck(Locators.documentsItemCheckbox);
			}else {
				sUncheck(Locators.documentsItemCheckbox);
			}
		}
		if(policy.getTasksItem() != null){
			if(policy.getEmailItem()){
				sCheck(Locators.tasksItemCheckbox);
			}else {
				sUncheck(Locators.tasksItemCheckbox);
			}
		}
		if(policy.getAppointmentItem() != null){
			if(policy.getEmailItem()){
				sCheck(Locators.appointmentsItemCheckbox);
			}else {
				sUncheck(Locators.appointmentsItemCheckbox);
			}
		}
		if(policy.getContactsItem() != null){
			if(policy.getEmailItem()){
				sCheck(Locators.contactsItemCheckbox);
			}else {
				sUncheck(Locators.contactsItemCheckbox);
			}
		}
		if(policy.getChatsItem() != null){
			if(policy.getEmailItem()){
				sCheck(Locators.chatsItemCheckbox);
			}else {
				sUncheck(Locators.chatsItemCheckbox);
			}
		}
		
		SleepUtil.sleepVerySmall();
		sType(Locators.olderThan, policy.getOlderThan());
		sClickAt(Locators.pullDown, "");
		sClickAt("css=div[class='oselect_choice']:contains('"+policy.getTimeDuration()+"')", "");
		sClickAt(Locators.okButton, "");
		
		if(zWaitForElementPresent(Locators.successfulDialog)) {
			sClick(Locators.okButton);
		} else {
			throw new HarnessException("Policy added successfully dialog is not appeared");
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

		String locator = Locators.addNewHSMPolicyDialog;

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
