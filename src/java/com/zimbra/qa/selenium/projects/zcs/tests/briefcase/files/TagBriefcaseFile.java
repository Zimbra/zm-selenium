package com.zimbra.qa.selenium.projects.zcs.tests.briefcase.files;

import java.lang.reflect.Method;

import junit.framework.Assert;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.zimbra.common.service.ServiceException;
import com.zimbra.qa.selenium.framework.core.*;
import com.zimbra.qa.selenium.framework.util.RetryFailedTests;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.Stafzmprov;
import com.zimbra.qa.selenium.projects.zcs.tests.CommonTest;


/**
 * @author Jitesh Sojitra
 */

@SuppressWarnings("static-access")
public class TagBriefcaseFile extends CommonTest {
	//--------------------------------------------------------------------------
	// SECTION 1: DATA-PROVIDERS
	//--------------------------------------------------------------------------
	@DataProvider(name = "tagDataProvider")
	public Object[][] createData(Method method) throws ServiceException {
		String test = method.getName();
		if (test.equals("createRenameDeleteTagForFileAndVerify_ColumnView")) {
			return new Object[][] { { "data/public/other/testexcelfile.xls" } };
		} else if (test
				.equals("verifyTagFunctionalityFor2FileAndRemoveTag_DetailView")) {
			return new Object[][] { { "data/public/other/testwordfile.doc" } };
		} else if (test.equals("applyMutlipleTagToFileAndVerify_ColumnView")) {
			return new Object[][] { { "data/public/other/samlejpg.jpg" } };
		} else if (test.equals("addRemoveTagAndVerifyInAll3View")) {
			return new Object[][] { { "data/public/csv/contacts.csv" } };
		} else if (test.equals("applyTagByDnDTagToFileAndViceVersa")) {
			return new Object[][] { { "data/public/ics/Calendar.ics" } };
		} else if (test.equals("tryToCreateDuplicateTagInBriefcase")) {
			return new Object[][] { { "data/public/ics/Calendar.ics" } };
		} else {
			return new Object[][] { { "" } };
		}
	}

	// --------------
	// section 2 BeforeClass
	// --------------
	@BeforeClass(groups = { "always" })
	public void zLogin() throws Exception {
		super.NAVIGATION_TAB="briefcase";
		super.zLogin();
	}

	//--------------------------------------------------------------------------
	// SECTION 3: TEST-METHODS
	//--------------------------------------------------------------------------

	/**
	 * Verify create, rename & delete functionality for tag for briefcase file
	 * (column view)
	 */
	@Test(dataProvider = "tagDataProvider", groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void createRenameDeleteTagForFileAndVerify_ColumnView(String fileName)
			throws Exception {
		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		String tag1, newTag1;
		obj.zButton.zClick(localize(locator.view));
		obj.zMenuItem.zClick(localize(locator.columnBrowserView));
		SleepUtil.sleep(500);
		page.zBriefcaseApp.zBriefcaseFileUpload(fileName, "");
		tag1 = getLocalizedData_NoSpecialChar();
		newTag1 = getLocalizedData_NoSpecialChar();
		zCreateTag(tag1);
		obj.zBriefcaseItem.zClick(fileName);
		SleepUtil.sleep(1000);
		obj.zButton.zClick(page.zBriefcaseApp.zTagItemIconBtn);
		obj.zMenuItem.zClick(tag1);
		SleepUtil.sleep(1000);
		obj.zBriefcaseItem.zVerifyIsTagged(fileName);
		SleepUtil.sleep(1000);

		zRenameTag(tag1, newTag1);
		obj.zFolder.zNotExists(tag1);
		obj.zFolder.zClick(newTag1);
		SleepUtil.sleep(1000);
		obj.zBriefcaseItem.zVerifyIsTagged(fileName);

		zDeleteTag(newTag1);
		obj.zBriefcaseItem.zClick(fileName);
		SleepUtil.sleep(1000);
		obj.zButton.zClick(page.zBriefcaseApp.zTagItemIconBtn);
		obj.zMenuItem.zIsEnabled(localize(locator.newTag));
		obj.zMenuItem.zIsDisabled(localize(locator.removeTag));

		SelNGBase.needReset.set(false);
	}

	/**
	 * Create 2 tag, apply 1 tag to each file and verify file exist / not exist
	 * by clicking to tag (detail view)
	 */
	@Test(dataProvider = "tagDataProvider", groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void verifyTagFunctionalityFor2FileAndRemoveTag_DetailView(
			String fileName) throws Exception {
		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		String fileName2, tag1, tag2;
		fileName2 = "data/public/other/testsoundfile.wav";
		obj.zButton.zClick(localize(locator.view));
		obj.zMenuItem.zClick(localize(locator.detailView));
		SleepUtil.sleep(500);
		page.zBriefcaseApp.zBriefcaseFileUpload(fileName, "");
		page.zBriefcaseApp.zBriefcaseFileUpload(fileName2, "");
		tag1 = getLocalizedData_NoSpecialChar();
		tag2 = getLocalizedData_NoSpecialChar();
		zCreateTag(tag1);
		obj.zBriefcaseItem.zClick(fileName);
		SleepUtil.sleep(1000);
		obj.zButton.zClick(page.zBriefcaseApp.zTagItemIconBtn);
		obj.zMenuItem.zClick(tag1);
		SleepUtil.sleep(1000);
		obj.zBriefcaseItem.zVerifyIsTagged(fileName);
		SleepUtil.sleep(1000);
		obj.zBriefcaseItem.zClick(fileName2);
		SleepUtil.sleep(1000);
		obj.zButton.zClick(page.zBriefcaseApp.zTagItemIconBtn);
		obj.zMenuItem.zClick(localize(locator.newTag));
		obj.zEditField.zTypeInDlg(localize(locator.tagName), tag2);
		obj.zButton.zClickInDlg(localize(locator.ok));
		SleepUtil.sleep(1000);
		obj.zBriefcaseItem.zVerifyIsTagged(fileName2);
		obj.zFolder.zClick(tag1);
		SleepUtil.sleep(1000);
		obj.zBriefcaseItem.zExists(fileName);
		assertReport("false", obj.zBriefcaseItem.zExistsDontWait(fileName2),
				"Verify file2 not exists");
		obj.zFolder.zClick(tag2);
		SleepUtil.sleep(1000);
		obj.zBriefcaseItem.zExists(fileName2);
		assertReport("false", obj.zBriefcaseItem.zExistsDontWait(fileName),
				"Verify file1 not exists");
		obj.zFolder.zClick(page.zBriefcaseApp.zBriefcaseFolder);
		SleepUtil.sleep(1000);
		assertReport("true", obj.zBriefcaseItem.zExistsDontWait(fileName),
				"Verify file1 not exists");
		assertReport("true", obj.zBriefcaseItem.zExistsDontWait(fileName2),
				"Verify file2 not exists");
		obj.zFolder.zClick(page.zBriefcaseApp.zTrashFolder);
		SleepUtil.sleep(1000);
		assertReport("false", obj.zBriefcaseItem.zExistsDontWait(fileName),
				"Verify file1 not exists");
		assertReport("false", obj.zBriefcaseItem.zExistsDontWait(fileName2),
				"Verify file2 not exists");

		obj.zFolder.zClick(page.zBriefcaseApp.zBriefcaseFolder);
		obj.zBriefcaseItem.zClick(fileName2);
		obj.zButton.zClick(page.zBriefcaseApp.zTagItemIconBtn);
		obj.zMenuItem.zClick(localize(locator.removeTag));
		SleepUtil.sleep(1000);
		obj.zButton.zClick(page.zBriefcaseApp.zTagItemIconBtn);
		obj.zMenuItem.zIsEnabled(localize(locator.newTag));
		obj.zMenuItem.zIsDisabled(localize(locator.removeTag));

		SelNGBase.needReset.set(false);
	}

	/**
	 * Create 2 tag, apply both tag to file and verify both file exists after
	 * clicking to tag (column view)
	 */
	@Test(dataProvider = "tagDataProvider", groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void applyMutlipleTagToFileAndVerify_ColumnView(String fileName)
			throws Exception {
		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		String tag1, tag2;
		obj.zButton.zClick(localize(locator.view));
		obj.zMenuItem.zClick(localize(locator.columnBrowserView));
		SleepUtil.sleep(500);
		page.zBriefcaseApp.zBriefcaseFileUpload(fileName, "");
		tag1 = getLocalizedData_NoSpecialChar();
		tag2 = getLocalizedData_NoSpecialChar();
		SleepUtil.sleep(1000);
		obj.zBriefcaseItem.zClick(fileName);
		SleepUtil.sleep(1000);
		obj.zButton.zClick(page.zBriefcaseApp.zTagItemIconBtn);
		obj.zMenuItem.zClick(localize(locator.newTag));
		obj.zEditField.zTypeInDlg(localize(locator.tagName), tag1);
		obj.zButton.zClickInDlg(localize(locator.ok));
		SleepUtil.sleep(1000);
		obj.zBriefcaseItem.zVerifyIsTagged(fileName);
		obj.zButton.zClick(page.zBriefcaseApp.zTagItemIconBtn);
		obj.zMenuItem.zNotExists(tag1);
		obj.zMenuItem.zClick(localize(locator.newTag));
		obj.zEditField.zTypeInDlg(localize(locator.tagName), tag2);
		obj.zButton.zClickInDlg(localize(locator.ok));
		SleepUtil.sleep(1000);
		obj.zBriefcaseItem.zVerifyIsTagged(fileName);
		obj.zButton.zClick(page.zBriefcaseApp.zTagItemIconBtn);
		obj.zMenuItem.zNotExists(tag1);
		obj.zMenuItem.zNotExists(tag2);
		obj.zFolder.zClick(tag1);
		SleepUtil.sleep(1000);
		obj.zBriefcaseItem.zExists(fileName);
		obj.zFolder.zClick(tag2);
		SleepUtil.sleep(1000);
		obj.zBriefcaseItem.zExists(fileName);

		SelNGBase.needReset.set(false);
	}

	/**
	 * Verify add, remove tag functionality for briefcase file in all 3 view -
	 * Detail View, Column Browser View & Explorer view
	 */
	@Test(dataProvider = "tagDataProvider", groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void addRemoveTagAndVerifyInAll3View(String fileName)
			throws Exception {
		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		// temporary work around for proper object identification
		resetSession();
		String acc1;
		acc1 = Stafzmprov.getRandomAccount();
		page.zLoginpage.zLoginToZimbraAjax(acc1);
		zGoToApplication("Briefcase");

		String tag1;
		obj.zButton.zClick(localize(locator.view));
		obj.zMenuItem.zClick(localize(locator.explorerView));
		SleepUtil.sleep(1000);
		obj.zButton.zClick("id=zb__BC__NEW_FILE_left_icon");
		page.zBriefcaseApp.zBriefcaseFileUpload(fileName, "");
		tag1 = getLocalizedData_NoSpecialChar();
		zCreateTag(tag1);
		SleepUtil.sleep(1000);
		ClientSessionFactory.session().selenium()
				.clickAt(
						"//div[contains(@id, 'zlif__BC') and contains(@class, 'ZmThumbnailItem')]",
						"");
		SleepUtil.sleep(1000);
		obj.zButton.zClick("id=zb__BC__TAG_MENU_left_icon");
		obj.zMenuItem.zClick(tag1);
		SleepUtil.sleep(1000);
		Assert
				.assertTrue(ClientSessionFactory.session().selenium()
						.isElementPresent("//div[contains(@id, 'zv__BC')]//div[contains(@class, 'ImgTagOrange')]"));

		obj.zButton.zClick(localize(locator.view));
		obj.zMenuItem.zClick(localize(locator.columnBrowserView));
		SleepUtil.sleep(1000);
		obj.zBriefcaseItem.zVerifyIsTagged(fileName);
		SleepUtil.sleep(1000);
		obj.zButton.zClick(localize(locator.view));
		obj.zMenuItem.zClick(localize(locator.explorerView));
		obj.zFolder.zClick(tag1);
		SleepUtil.sleep(1000);
		Assert
				.assertTrue(ClientSessionFactory.session().selenium()
						.isElementPresent("//div[contains(@id, 'zv__BC')]//div[contains(@class, 'ImgTagOrange')]"));
		assertReport(fileName, ClientSessionFactory.session().selenium().getText(
				"//div[contains(@id, 'zli__BC')]").trim(),
				"Verify clicking to tag returns proper file in explorer view");

		obj.zButton.zClick(localize(locator.view));
		obj.zMenuItem.zClick(localize(locator.detailView));
		SleepUtil.sleep(1000);
		Assert
				.assertTrue(ClientSessionFactory.session().selenium()
						.isElementPresent("//tr[contains(@id, 'zlif__BCD')]//td[contains(@class, 'Tag')]//div[contains(@class, 'ImgTagOrange')]"));

		obj.zButton.zClick(localize(locator.view));
		obj.zMenuItem.zClick(localize(locator.columnBrowserView));
		SleepUtil.sleep(1000);
		obj.zBriefcaseItem.zVerifyIsTagged(fileName);

		SelNGBase.needReset.set(false);
	}

	/**
	 * Verify drag n drop functionality for tag and file. Drag file to tag and
	 * verify tag applied & same way drag tag to file and verify tag applied
	 */
	@Test(dataProvider = "tagDataProvider", groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void applyTagByDnDTagToFileAndViceVersa(String fileName)
			throws Exception {
		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		String fileName2, tag1, tag2;
		fileName2 = "data/public/other/testpptfile.ppt";
		obj.zButton.zClick(localize(locator.view));
		obj.zMenuItem.zClick(localize(locator.columnBrowserView));
		SleepUtil.sleep(500);
		page.zBriefcaseApp.zBriefcaseFileUpload(fileName, "");
		page.zBriefcaseApp.zBriefcaseFileUpload(fileName2, "");
		tag1 = getLocalizedData_NoSpecialChar();
		tag2 = getLocalizedData_NoSpecialChar();
		zCreateTag(tag1);
		zCreateTag(tag2);

		zDragAndDrop("//td[contains(@id, 'zlif__BCC') and contains(text(), '"
				+ fileName.toLowerCase() + "')]",
				"//td[contains(@id, 'zti__main_Briefcase') and contains(text(), '"
						+ tag1 + "')]");
		obj.zBriefcaseItem.zVerifyIsTagged(fileName.toLowerCase());
		SleepUtil.sleep(1000);
		obj.zFolder.zClick(page.zBriefcaseApp.zTrashFolder);
		SleepUtil.sleep(1000);
		assertReport("false", obj.zBriefcaseItem.zExistsDontWait(fileName),
				"Verify file1 not exists");
		assertReport("false", obj.zBriefcaseItem.zExistsDontWait(fileName2),
				"Verify file2 not exists");
		obj.zFolder.zClick(tag1);
		SleepUtil.sleep(1000);
		obj.zBriefcaseItem.zExists(fileName.toLowerCase());

		obj.zFolder.zClick(page.zBriefcaseApp.zBriefcaseFolder);
		zDragAndDrop(
				"//td[contains(@id, 'zti__main_Briefcase') and contains(text(), '"
						+ tag2 + "')]",
				"//td[contains(@id, 'zlif__BCC') and contains(text(), '"
						+ fileName2.toLowerCase() + "')]");
		obj.zBriefcaseItem.zVerifyIsTagged(fileName2.toLowerCase());
		SleepUtil.sleep(1000);
		obj.zFolder.zClick(page.zBriefcaseApp.zBriefcaseFolder);
		SleepUtil.sleep(1000);
		obj.zFolder.zClick(tag2);
		SleepUtil.sleep(1000);
		obj.zBriefcaseItem.zExists(fileName2.toLowerCase());
		assertReport("false", obj.zBriefcaseItem.zExistsDontWait(fileName),
				"Verify message1 not exists");

		SelNGBase.needReset.set(false);
	}

	/**
	 * Try to create duplicate tag and verify its not allowed
	 */
	@Test(dataProvider = "tagDataProvider", groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void tryToCreateDuplicateTagInBriefcase(String fileName)
			throws Exception {
		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		String tag1;
		tag1 = getLocalizedData_NoSpecialChar();
		zCreateTag(tag1);
		zDuplicateTag(tag1);

		SelNGBase.needReset.set(false);
	}
}