/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2012, 2013, 2014, 2015, 2016 Synacor, Inc.
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software Foundation,
 * version 2 of the License.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see <https://www.gnu.org/licenses/>.
 * ***** END LICENSE BLOCK *****
 */
package com.zimbra.qa.selenium.projects.ajax.pages.drive;

import java.util.Map;
import org.apache.commons.httpclient.HttpStatus;
import org.openqa.selenium.WebElement;
import com.zimbra.qa.selenium.framework.items.IItem;
import com.zimbra.qa.selenium.framework.ui.AbsApplication;
import com.zimbra.qa.selenium.framework.ui.AbsPage;
import com.zimbra.qa.selenium.framework.ui.AbsTab;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.RestUtil;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.projects.ajax.pages.AjaxPages;
import com.zimbra.qa.selenium.projects.ajax.pages.drive.DialogUploadFile;

public class PageDrive extends AbsTab {

	WebElement we = null;

	public static final String pageTitle = "Zimbra: ZextrasDrive";

	public static class Locators {
		
		public static final Locators zDriveTab = new Locators("zb__App__ZIMBRA_DRIVE_title");
		public static final Locators zDriveUploadFilesBtn = new Locators("zb__NEW_MENU_title");
		public static final Locators zDriveNewFolderBtn = new Locators("zb__ZDRIVE_DLV-main__ZIMBRA_DRIVE_NEW_FOLDER_title");
		public static final Locators zl__ZDRIVE_DLV__rowsID = new Locators("zl__ZDRIVE_DLV-main__rows");
		public static final Locators driveListView = new Locators("css=div[id='zl__ZDRIVE_DLV-main__rows'][class='DwtListView-Rows']");
		public static final Locators zli___ZDRIVE_DLV = new Locators("zli__ZDRIVE_DLV-");
		public static final Locators zDeleteDriveItem = new Locators("zb__ZDRIVE_DLV-main__ZIMBRA_DRIVE_DELETE_title");
		public static final Locators zMoveDriveItem = new Locators("zb__ZDRIVE_DLV-main__ZIMBRA_DRIVE_MOVE_title");
		public static final Locators zRenameDriveItem = new Locators("zb__ZDRIVE_DLV-main__ZIMBRA_DRIVE_RENAME_title");
		public static final String zDriveFolderIcon = "css=div[id='zov__main_ZIMBRA_DRIVE'] div[class='DwtTreeItem-Control'] td:contains('Drive')";
		public static final String zDriveFolderPane= "css=div[id='zov__main_ZIMBRA_DRIVE']";
		public static final Locators zUploadLeftIconBtn = new Locators("css=div[id=skin_container_app_new_button]");
		public static final Locators zUploadFileTitleBtn = new Locators("css=div[id=zb__NEW_MENU] td[id='zb__NEW_MENU_title']");
	
		public final String locator;

		public Locators(String locator) {
			this.locator = locator;
		}
	}

	public static class Response {
		public static enum ResponsePart {
			BODY, HEADERS
		}

		public static enum Format {
			HTML("html"), TEXT("text"), TGZ("tgz"), XML("xml"), JSON("json"), NATIVE("native"), RAW("raw");

			private String fmt;

			private Format(String fmt) {
				this.fmt = fmt;
			}

			public String getFormat() {
				return fmt;
			}
		}
	}

	public PageDrive(AbsApplication application) {
		super(application);
		logger.info("new " + PageDrive.class.getCanonicalName());
	}

	@Override
	public boolean zIsActive() throws HarnessException {

		String locator = Locators.zDriveFolderIcon;

		boolean loaded = sIsElementPresent(locator);// zWaitForElementPresent(locator, "1000");

		if (!loaded)
			return (loaded);
		boolean active = this.zIsVisiblePerPosition(locator, 0, 0);
		return (active);

	}

	@Override
	public String myPageName() {
		return (this.getClass().getName());
	}

	@Override
	public void zNavigateTo() throws HarnessException {

		if (zIsActive()) {
			logger.info(myPageName() + " is already loaded");
			return;
		}

		try {
			((AjaxPages) MyApplication).zPageMain.zNavigateToAppTab(Locators.zDriveFolderPane);
		} catch (Exception ex) {
			throw new HarnessException("Try connecting Nextcloud Server first or check zimbra settings. Drive not Connected");
		}
	}

	public AbsPage zToolbarPressButton(Button button, IItem fileItem) throws HarnessException{

		logger.info(myPageName() + " zToolbarPressButton(" + button + ")");

		tracer.trace("Press the " + button + " button");

		if (button == null)
			throw new HarnessException("Button cannot be null!");

		String locator = null;
		AbsPage page = null;

		if (button == Button.B_NEW) {
			return page;

		} else if (button == Button.B_UPLOAD_FILE) {
			// Check if the button is disabled
			locator = Locators.zUploadFileTitleBtn.locator;

			page = new DialogUploadFile(MyApplication, this);
		} 
		else {
			throw new HarnessException("no logic defined for button " + button);
		}

		if (locator == null) {
			throw new HarnessException("locator was null for button " + button);
		}

		if (!this.sIsElementPresent(locator))
			throw new HarnessException("Button is not present locator=" + locator + " button=" + button);

		this.sClickAt(locator, "0,0");
		SleepUtil.sleepMedium();

		zWaitForBusyOverlay();

		return (page);
	}
	
	public AbsPage zToolbarPressButton(Button button) throws HarnessException {

		logger.info(myPageName() + " zToolbarPressButton(" + button + ")");

		tracer.trace("Press the " + button + " button");

		if (button == null)
			throw new HarnessException("Button cannot be null!");

		String locator = null;
		AbsPage page = null;

		if (button == Button.B_NEW_FOLDER) {
			// Check if the button is disabled
			locator = Locators.zDriveNewFolderBtn.locator;

			// Click on New Document icon
			this.sClickAt(locator, "0,0");
			zWaitForBusyOverlay();
			SleepUtil.sleepLong();

			//page = new DialogCreateDriveFolder(MyApplication, this);
			//page.zIsActive();
			return page;

		} else if (button == Button.B_UPLOAD_FILE) {
			// Check if the button is disabled
			locator = Locators.zUploadFileTitleBtn .locator;

			page = new DialogUploadFile(MyApplication, this);
		} 
		else {
			throw new HarnessException("no logic defined for button " + button);
		}

		if (locator == null) {
			throw new HarnessException("locator was null for button " + button);
		}

		if (!this.sIsElementPresent(locator))
			throw new HarnessException("Button is not present locator=" + locator + " button=" + button);

		this.sClickAt(locator, "0,0");
		SleepUtil.sleepMedium();

		zWaitForBusyOverlay();

		return (page);
	}
	public String openUrl(String url) throws HarnessException {

		this.sOpen(url);

		return url;
	}

	public String getLocation() {
		String url = null;
		try {
			url = sGetLocation();
		} catch (Exception ex) {
			logger.info(ex);
		}
		return url;
	}

	public String openUrl(String path, Map<String, String> params) throws HarnessException {
		ZimbraAccount account = MyApplication.zGetActiveAccount();

		RestUtil util = new RestUtil();

		util.setAuthentication(account);

		if (null != path && !path.isEmpty())
			util.setPath("/" + path + "/");
		else
			util.setPath("/");

		if (null != params && !params.isEmpty()) {
			for (Map.Entry<String, String> query : params.entrySet()) {
				util.setQueryParameter(query.getKey(), query.getValue());
			}
		}

		if (util.doGet() != HttpStatus.SC_OK)
			throw new HarnessException("Unable to open " + util.getLastURI());

		String url = util.getLastURI().toString();

		if (url.endsWith("?"))
			url = url.substring(0, url.length() - 1);

		this.sOpen(url);

		return url;
	}

	public void closeWindow() throws HarnessException {
		tracer.trace("Close the separate window");

		this.sClose();
	}

	@Override
	public void zSelectWindow(String windowID) throws HarnessException {
		logger.info("zSelectWindow(" + windowID + ")");
		super.zSelectWindow(windowID);
	}


	@Override
	public AbsPage zListItem(Action action, String name) throws HarnessException {

		if ((action == null) || (name == null)) {
			throw new HarnessException("Must define an action and item");
		}

		tracer.trace(action + " on briefcase item = " + name);

		logger.info(myPageName() + " zListItem(" + action + ", " + name + ")");

		AbsPage page = null;
		String listLocator = "//div[contains(@id,'zl__BDLV')]";
		String itemLocator = listLocator + "//div[contains(@id,'zli__BDLV') and contains(@class,'Row')]";
		String itemNameLocator = listLocator + "//*[contains(@id,'zlif__BDLV') and contains(text(),'" + name + "')]";

		if (!this.sIsElementPresent(itemLocator))
			throw new HarnessException("List View Rows is not present " + itemLocator);

		if (action == Action.A_LEFTCLICK) {
			zWaitForElementPresent(itemNameLocator);
			this.sClickAt(itemNameLocator, "0,0");

		} else if (action == Action.A_BRIEFCASE_CHECKBOX) {
			zWaitForElementPresent(itemNameLocator);

			String checkBoxLocator = "";
			String itemIndexLocator = "";
			int count = sGetXpathCount(itemLocator);

			for (int i = 1; i <= count; i++) {
				itemIndexLocator = itemLocator + "[position()=" + i + "]";
				if (sIsElementPresent(itemIndexLocator + "//*[contains(text(),'" + name + "')]")) {
					checkBoxLocator = itemIndexLocator
							+ "//div[contains(@id,'zlif__BDLV')]/div[contains(@class,'ImgCheckbox')]";
					break;
				}
			}

			if (!this.sIsElementPresent(checkBoxLocator)) {
				throw new HarnessException("Checkbox locator is not present " + checkBoxLocator);
			}

			String image = this.sGetAttribute(checkBoxLocator + "@class");

			if (!image.equals("ImgCheckboxChecked")) {
				this.sClickAt(checkBoxLocator, "0,0");
			} else {
				logger.error("Trying to mark check box, but it was already enabled");
			}
			page = null;

		}
		return (page);
	}

	@Override
	public AbsPage zListItem(Action action, Button option, String subject) throws HarnessException {
		if (action == null)
			throw new HarnessException("action cannot be null");
		if (option == null)
			throw new HarnessException("button cannot be null");
		if (subject == null)
			throw new HarnessException("subject cannot be null or blank");

		tracer.trace(action + " then " + option + " on briefcase item = " + subject);

		logger.info(myPageName() + " zListItem(" + action + ", " + option + ", " + subject + ")");

		AbsPage page = null;

		if (action == Action.A_RIGHTCLICK) {

			zWaitForElementPresent(subject);

			this.sRightClickAt(subject, "0,0");
			String optionLocator = null;

			if (option == Button.B_RENAME) {

				//optionLocator = Locators.zRenameMenu;

				page = null;

			} else if (option == Button.O_RESTORE_AS_CURRENT_VERSION) {

				//optionLocator = Locators.zRestoreAsCurrentVersion;

				page = null;

			}
			this.sClick(optionLocator);
		}

		zWaitForBusyOverlay();
		SleepUtil.sleepSmall();

		return (page);

	}

	@Override
	public AbsPage zListItem(Action action, Button option, Button subOption, String item) throws HarnessException {
		throw new HarnessException("implement me! : action=" + action + " subOption=" + subOption + "item=" + item);
	}

	@Override
	public AbsPage zToolbarPressPulldown(Button pulldown, Button option) throws HarnessException {
		throw new HarnessException("implement me! : pulldown=" + pulldown + " option=" + option);
	}

	public String zGetItemNameFromListView(String itemName) throws HarnessException {

		String itemLocator = "css=div[id^='zlif__ZDRIVE_DLV-main']:contains('"+itemName+"')";

		if(zWaitForElementPresent(itemLocator))
			return sGetText(itemLocator);
		else
			return "";
	}
}