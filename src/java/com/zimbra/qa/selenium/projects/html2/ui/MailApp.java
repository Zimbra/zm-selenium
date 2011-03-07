package com.zimbra.qa.selenium.projects.html2.ui;

import org.testng.Assert;

import com.zimbra.qa.selenium.framework.core.*;
import com.zimbra.qa.selenium.framework.util.LmtpUtil;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.Stafpostqueue;
import com.zimbra.qa.selenium.framework.util.ZimbraSeleniumLogger;
import com.zimbra.qa.selenium.projects.html2.tests.CommonTest;




/**
 * This Class have UI-level methods related Mail-app(conversation view). e.g:
 * zNavigateToMailCompose, zEnterComposeValues etc It also has static-final
 * variables that holds ids of icons on the mailList-toolbar(like
 * zNewMenuIconBtn, zGetMailIconBtn etc). If you are dealing with the toolbar
 * buttons, use these icons since in vmware resolutions and in some languages
 * button-labels are not displayed(but just their icons)
 * 
 * @author Raja Rao DV
 * 
 */
@SuppressWarnings("static-access")
public class MailApp extends CommonTest {

	// Mail folders
	public static final String zInboxFldr = "id=FLDR2";
	public static final String zSentFldr = "id=FLDR5";
	public static final String zDraftFldr = "id=FLDR6";
	public static final String zJunkFldr = "id=FLDR4";
	public static final String zTrashFldr = "id=FLDR3";
	public static final String zEditLinkFldrOverviewPane = "id=MFOLDERS";

	// Mail toolbar buttons
	public static final String zComposeBtn = localize(locator.compose);
	public static final String zRefreshBtn = localize(locator.refresh);
	public static final String zDeleteBtn = "id=SOPDELETE";
	public static final String zSelectAllMailChkBox = "id=OPCHALL";
	public static final String zJunkBtn = "id=SOPSPAM";
	public static final String zNotJunkBtn = "name=actionNotSpam";
	public static final String zEmptyJunkBtn = "name=actionEmpty";
	public static final String zNextPageIconBtn = "id=NEXT_PAGE";
	public static final String zPreviousPageIconBtn = "id=PREV_PAGE";
	public static final String zShowOrigIconBtn = "id=OPSHOWORIG";
	public static final String zOpenInNewWindowIconBtn = "id=OPNEWWIN";

	// Manage folders page ids
	public static final String zCloseBtn = "id=OPCLOSE";
	public static final String zNewFolderBtn = "id=actionNewFolder";
	public static final String zNewRssFeedBtn = "id=actionNewFeedFolder";
	public static final String zNewSearchBtn = "id=actionNewSearchFolder";
	public static final String zNewFldrNameEditField = "id=newName";
	public static final String zParentFolderWebList = "id=parentFolder";
	public static final String zCreateFolderBtn = "id=OPSAVE";
	public static final String zCreateFolderCancelBtn = "name=actionCancel";
	public static final String zEditFldrNameEditField = "id=name";
	public static final String zPermDelMailItemChkBox = "name=folderEmptyConfirm";
	public static final String zDeleteAllItemsBtn = "name=actionEmptyFolderConfirm";
	public static final String zDeleteThisFolderChkBox = "name=folderDeleteConfirm";
	public static final String zDeleteFolderBtn = "name=actionDelete";
	public static final String zPermDeleteFolderBtn = "name=actionPermDelete";
	public static final String zSystemFolderEditField = "name=folderName";
	public static final String zMarkAllReadBtn = "id=OPMARKALLREAD";
	public static final String zEmptyJunkFolder = "id=OPEMPTY";
	public static final String zRssFeedURLEditField = "id=url";
	public static final String zSearchQueryEditField = "id=folderQuery";

	// Tag related id's
	public static final String zNewTagBtn = "id=IOPNEWTAG";
	public static final String zTagNameEditfield = "id=name";
	public static final String zTagEditNameEditfield = "name=tagName";
	public static final String zTagSaveBtn = "id=OPSAVE";
	public static final String zTagPermDelChkBox = "name=tagDeleteConfirm";
	public static final String zTagDeleteBtn = "name=actionDelete";

	/**
	 * Clicks Get Mail button for up to 60seconds or until mail with subject
	 * appears.
	 * 
	 * @param mailSubject
	 *            mail's subject that needs to be selected
	 * @throws Exception
	 */
	public static void zClickCheckMailUntilMailShowsUp(String mailSubject)
			throws Exception {
		zClickCheckMailUntilMailShowsUp("", mailSubject);
	}

	/**
	 * Clicks Get Mail button for up to 60seconds or until mail with subject
	 * appears
	 * 
	 * @param mailSubject
	 *            mail's subject that needs to be selected
	 * @throws Exception
	 */
	public static void zClickCheckMailUntilMailShowsUp(String folderName,
		String mailSubject) throws Exception {
		int i = 0;
		boolean found = false;
		
		try{
			Stafpostqueue sp = new Stafpostqueue();
			sp.waitForPostqueue();
		}catch(Exception ex){
			ZimbraSeleniumLogger.mLog.error("Error occured using STAF service");
		}
		
		if (!folderName.equals("")) {
			obj.zFolder.zClick(folderName);
		} else {
			obj.zFolder.zClick(zInboxFldr);
		}
		SleepUtil.sleep(1000); // timing issue
		String rc = obj.zMessageItem.zExistsDontWait(mailSubject);
		if (rc.equals("false")) {
		// check in junk
			obj.zFolder.zClick(zJunkFldr);
			SleepUtil.sleep(1000);
			rc = obj.zMessageItem.zExistsDontWait(mailSubject);
			if (!rc.equals("false")) 
				found = true;				
		} else {
			found = true;
		}

		if (!found) {
			if (folderName == "")
				folderName = "Inbox";
			Assert.fail("Mail(" + mailSubject + ") didn't appear even after "
					+ 60 + " seconds in (" + folderName + ")");
		}
	}

	public static void zVerifyMailNotExists(String folderName,
			String mailSubject) throws Exception {
		SleepUtil.sleep(3000); // required otherwise it will fail
		int i = 0;
		boolean found = false;
		for (i = 0; i < 5; i++) {
			obj.zButton.zClick(zRefreshBtn);
			if (!folderName.equals("")) {
				obj.zFolder.zClick(folderName);
			}
			String rc = obj.zMessageItem.zNotExistsDontWait(mailSubject);
			if (rc.equals("false")) {
				SleepUtil.sleep(500);
			} else {
				found = true;
				break;
			}
		}
		if (found)
			Assert.fail("Mail(" + mailSubject + ") appeared after " + 30
					+ " seconds in (" + folderName + ")");
	}

	/**
	 * Zimbra doesnt show underscores, so we replace _ with " "and simply return
	 * the mail-name
	 * 
	 * @param actualEmail
	 * @return en us 123213 if "en_us_123213@test.com" was passed
	 */
	public static String zGetNameFromEmail(String actualEmail) {
		try {
			return actualEmail.substring(0, (actualEmail.indexOf("@") - 1))
					.replace("_", " ");
		} catch (Exception e) {
			return actualEmail;// resurn the same email upon expn
		}
	}

	public static void zInjectMessage(String from, String to, String cc,
			String bcc, String subject, String body, String attachments)
			throws Exception {
		to = ClientSessionFactory.session().currentUserName();
		String[] recipients = { to };
		LmtpUtil.injectMessage(from, recipients, cc, subject, body);
		SleepUtil.sleep(1000);
		zClickCheckMailUntilMailShowsUp(page.zMailApp.zInboxFldr, subject);
	}

	public static void zGoToMailAndCreateFolder(String folderName,
			String parentFolder) throws Exception {
		zGoToApplication("Mail");
		obj.zButton.zClick(zEditLinkFldrOverviewPane);
		SleepUtil.sleep(1000);
		zCreateFolderCore(folderName, parentFolder);
	}

	public static void zCreateFolder(String folderName, String parentFolder)
			throws Exception {
		zCreateFolderCore(folderName, parentFolder);
	}

	private static void zCreateFolderCore(String folderName, String parentFolder)
			throws Exception {
		obj.zButton.zClick(zNewFolderBtn);
		SleepUtil.sleep(2000); // test fails here
		obj.zEditField.zType(zNewFldrNameEditField, folderName);
		if (!parentFolder.equals("")) {
			obj.zHtmlMenu.zClick(zParentFolderWebList, parentFolder);
		}
		obj.zButton.zClick(zCreateFolderBtn);
		SleepUtil.sleep(3000);
		zWaitTillObjectExist("folder", folderName);
	}

	public static void zNavigateToNewTagPage() throws Exception {
		obj.zFolder.zEdit(localize(locator.tags));
		SleepUtil.sleep(1000);
		zWaitTillObjectExist("button", zNewTagBtn);
	}

	public static void zCreateTag(String tagName) throws Exception {
		zNavigateToNewTagPage();
		SleepUtil.sleep(1000);
		obj.zButton.zClick(zNewTagBtn);
		SleepUtil.sleep(1000);
		obj.zEditField.zType(zTagNameEditfield, tagName);
		obj.zButton.zClick(zTagSaveBtn);
		SleepUtil.sleep(1000);
	}

	public static void zRenameTag(String currentTagName, String newTagName)
			throws Exception {
		zNavigateToNewTagPage();
		SleepUtil.sleep(1000);
		// obj.zFolder.zClick(currentTagName);
		obj.zEditField.zType(zTagEditNameEditfield, newTagName);
		obj.zButton.zClick(zTagSaveBtn);
		SleepUtil.sleep(1000);
	}

	public static void zDeleteTag(String tagName) throws Exception {
		zNavigateToNewTagPage();
		// obj.zFolder.zClick(tagName);
		obj.zCheckbox.zClick(zTagPermDelChkBox);
		obj.zButton.zClick(zTagDeleteBtn);
		SleepUtil.sleep(1000);
	}

	public static void zMoveTo(String destinationFolder) throws Exception {
		if (destinationFolder.length() >= 8) {
			destinationFolder = destinationFolder.substring(0, 7) + ".*";
		}
		obj.zHtmlMenu.zClick("name=folderId", destinationFolder);
		SleepUtil.sleep(500);
	}

	public static void zMoveToBtmToolbar(String destinationFolder)
			throws Exception {
		if (destinationFolder.length() >= 8) {
			destinationFolder = destinationFolder.substring(0, 7) + ".*";
		}
		obj.zHtmlMenu.zClickMenuByLocation("name=folderId", destinationFolder,
				"2");
		SleepUtil.sleep(500);
	}

	public static void zMoreActions(String actionName) throws Exception {
		if (actionName.length() >= 8) {
			actionName = actionName.substring(0, 7) + ".*";
		}
		obj.zHtmlMenu.zClick("name=actionOp", actionName);
		SleepUtil.sleep(2000); /*
							 * this is necessary, selenium doesn't wait after
							 * doing some action and fails on next statement
							 */
	}

	public static void zMoreActionsBtmToolbar(String actionName)
			throws Exception {
		if (actionName.length() >= 8) {
			actionName = actionName.substring(0, 7) + ".*";
		}
		obj.zHtmlMenu.zClickMenuByLocation("name=actionOp", actionName, "2");
		SleepUtil.sleep(2000);
	}
}
