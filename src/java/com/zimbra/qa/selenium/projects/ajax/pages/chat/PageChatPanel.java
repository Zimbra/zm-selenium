package com.zimbra.qa.selenium.projects.ajax.pages.chat;

import com.zimbra.qa.selenium.framework.ui.AbsApplication;
import com.zimbra.qa.selenium.framework.ui.AbsPage;
import com.zimbra.qa.selenium.framework.ui.AbsTab;
import com.zimbra.qa.selenium.framework.ui.AbsWizard;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.projects.ajax.pages.AjaxPages;

public class PageChatPanel extends AbsTab {

	public static class Locators {
		public static final String ChatPanel = "css=div[class='DwtBaseDialog'] td.ZxChatWindowInnerContainer div[id$='ZxChat_Main_Window_content'][style*='display: block;']";
		public static final String ChatHeader = "css=tr[class='ZxChat_TitleBar']";
		public static final String ChatEllipsis = "css=div.ImgZxChat_preferences";
		public static final String AddNewBuddy_Option = "css=td[id='ZxChat_MenuItem_AddBuddy_title']";
		public static final String Dialog_Informational = "css=div[class='DwtDialog']:not([aria-hidden='true']) td[class='DwtDialogTitle']:contains('Informational')";
		public static final String Dialog_Ok_Button = "css=div[class='DwtDialog'] td[class='ZWidgetTitle']:contains('Yes')";
	}

	public enum Userstatus{
		offline("offline"),online("online"),invited("invited"),need_response("need_response");

		private final String status;

		private Userstatus(String val) {
			this.status = val;
		}

		public String getStatus() {
			return status;
		}
	}

	public PageChatPanel(AbsApplication application) {
		super(application);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void zNavigateTo() throws HarnessException {
		if (zIsActive()) {
			logger.info(myPageName() + " is already loaded");
			return;
		}
		SleepUtil.sleepVeryLong();
		if(zWaitForElementPresent(Locators.ChatHeader)) {

			if(!sIsElementPresent(Locators.ChatPanel))
				sClick(Locators.ChatHeader);
		}
	}

	@Override
	public boolean zIsActive() throws HarnessException {
		if (!((AjaxPages) MyApplication).zPageMain.zIsActive()) {
			((AjaxPages) MyApplication).zPageMain.zNavigateTo();
		}
		boolean active = sIsElementPresent(Locators.ChatPanel);

		active &= this.sIsVisible(Locators.ChatPanel);
		return (active);
	}

	public AbsWizard zEllipsesOption(Button option) throws HarnessException {
		logger.info(myPageName() + " zEllipsesOption(" + option + ")");

		tracer.trace("Click Ellipses then " + option);
		AbsWizard wizard = null;
		SleepUtil.sleepSmall();
		sClickAt(Locators.ChatEllipsis,"0");
		SleepUtil.sleepSmall();

		if (option == Button.B_ADD_NEW_BUDDY) {
			sClickAt(Locators.AddNewBuddy_Option,"0");
			wizard = new WizardAddBuddy(this);

		} else {
			throw new HarnessException("The method not implemented for "+ option);
		}

		return wizard;
	}

	public String zUserStatus(String email) throws HarnessException {

		String locator ="css=td[id$='ZxChat_BuddyTreeItem_"+email.replace("@", "_")+"_imageCell'] div@class";
		return sGetAttribute(locator);
	}

	public Boolean zSelectUser(String email, Userstatus status) throws HarnessException {

		String locator = "css=td[id$='ZxChat_BuddyTreeItem_"+email.replace("@", "_")+"_textCell']";
		sClickAt(locator,"0");

		if(status == Userstatus.need_response) {
			if(zWaitForElementPresent(Locators.Dialog_Informational)) {
				sClick(Locators.Dialog_Ok_Button);
				SleepUtil.sleepMedium();
				return true;
			} else {
				throw new HarnessException("Informational Dialog is not appeared");
			}
		}
		else{
			return true;
		}
	}

	public void zSendMsg(String message) throws HarnessException{
		SleepUtil.sleepLong();
		String loc = "css=div[id$='ZxChat_RoomWindow'] textarea";
		sType(loc, message +"\n");
	}

	public String zGetMsg() throws HarnessException {
		String loc = "css=div[class='ZxChat_Conversation'] div[class='DwtComposite']:last-child div.MsgBody";
		return sGetText(loc);
	}

	public Boolean zVerifyChatFolder() throws HarnessException {

		if(sIsElementPresent("css=div[class='DwtTreeItem-Control'] td:contains('Chats')")) {
			sClick("css=div[class='DwtTreeItem-Control'] td:contains('Chats')");
			SleepUtil.sleepMedium();
			return true;
		} else {
			return false;
		}
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

		return null;
	}


	@Override
	public String myPageName() {
		return (this.getClass().getName());
	}
}
