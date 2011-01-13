package com.zimbra.qa.selenium.projects.zcs.tests.documents.folders;

import java.lang.reflect.Method;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.core.*;
import com.zimbra.qa.selenium.framework.util.RetryFailedTests;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.projects.zcs.tests.CommonTest;
import com.zimbra.qa.selenium.projects.zcs.ui.DocumentApp;


/**
 * This covers some high priority test cases related to Documents
 * 
 * @author Prashant JAISWAL
 * 
 */
@SuppressWarnings("static-access")
public class DocumentFolderTests extends CommonTest {
	//--------------------------------------------------------------------------
	// SECTION 1: DATA-PROVIDERS
	//--------------------------------------------------------------------------
	@DataProvider(name = "DocumentDataProvider")
	public Object[][] createData(Method method) {
		String test = method.getName();
		if (test.equals("createDeleteNotebookFolder")
				|| test.equals("renameNotebookFolder")
				|| test.equals("tryToCreateDuplicateNotebookFolder")) {
			return new Object[][] { { getLocalizedData_NoSpecialChar() } };
		} else {
			return new Object[][] { { "" } };
		}
	}

	// --------------
	// section 2 BeforeClass
	// --------------
	@BeforeClass(groups = { "always" })
	public void zLogin() throws Exception {
		super.NAVIGATION_TAB="documents";
		super.zLogin();
	}

	/**
	 * Test to delete Notebook folder using right click delete menu and verify
	 */
	@Test(dataProvider = "DocumentDataProvider", groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void createDeleteNotebookFolder(String notebookName)
			throws Exception {
		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		page.zDocumentCompose.zCreateNewNotebook(notebookName, "", "");
		page.zDocumentApp.zDeleteNotebookFolder(notebookName);
		obj.zFolder.zNotExists(notebookName);

		SelNGBase.needReset.set(false);
	}

	@Test(dataProvider = "DocumentDataProvider", groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void renameNotebookFolder(String notebookName) throws Exception {
		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		String newNotebookName = getLocalizedData_NoSpecialChar();
		page.zDocumentCompose.zCreateNewNotebook(notebookName, "", "");
		SleepUtil.sleep(1000);
		obj.zFolder.zRtClick(notebookName);
		obj.zMenuItem.zClick(localize(locator.editProperties));
		obj.zEditField.zTypeInDlg(localize(locator.nameLabel), newNotebookName);
		obj.zButton.zClickInDlg(localize(locator.ok));
		SleepUtil.sleep(1000);
		obj.zFolder.zExists(newNotebookName);
		obj.zFolder.zNotExists(notebookName);

		SelNGBase.needReset.set(false);
	}

	@Test(dataProvider = "DocumentDataProvider", groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void tryToCreateDuplicateNotebookFolder(String notebookName)
			throws Exception {
		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		page.zDocumentCompose.zCreateNewNotebook(notebookName, "", "");
		SleepUtil.sleep(1000);
		obj.zButton
				.zRtClick(replaceUserNameInStaticId(DocumentApp.zNewNotebookOverviewPaneIcon));
		obj.zMenuItem.zClick(localize(locator.newNotebook));
		obj.zEditField.zTypeInDlg(localize(locator.nameLabel), notebookName);
		obj.zButton.zClickInDlg(localize(locator.ok));
		assertReport(localize(locator.errorAlreadyExists, notebookName, ""),
				obj.zDialog.zGetMessage(localize(locator.criticalMsg)),
				"Verifying dialog message");
		obj.zButton.zClickInDlgByName(localize(locator.ok),
				localize(locator.criticalMsg));
		obj.zButton.zClickInDlgByName(localize(locator.cancel),
				localize(locator.createNewNotebook));

		SelNGBase.needReset.set(false);
	}
}