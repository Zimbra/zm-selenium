package com.zimbra.qa.selenium.projects.zcs.tests.briefcase.files;

import java.lang.reflect.Method;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.core.*;
import com.zimbra.qa.selenium.framework.util.RetryFailedTests;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.ZimbraSeleniumProperties;
import com.zimbra.qa.selenium.projects.zcs.tests.CommonTest;


/**
 * @author Jitesh Sojitra
 */
@SuppressWarnings("static-access")
public class BriefcaseFileContextMenu extends CommonTest {
	//--------------------------------------------------------------------------
	// SECTION 1: DATA-PROVIDERS
	//--------------------------------------------------------------------------
	@DataProvider(name = "BriefcaseFileUpload")
	public Object[][] createData(Method method) {
		String test = method.getName();
		if (test.equals("verifyDownloadMenuDisabledForNewDoc"))
			return new Object[][] { { "CreateNewDoc",
					getLocalizedData_NoSpecialChar() } };
		else
			return new Object[][] { { "" } };
	}

	//--------------------------------------------------------------------------
	// SECTION 2: SETUP
	//--------------------------------------------------------------------------
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
	 * 1. Login to web client 2. Go to Briefcase > create any document Or
	 * presentation Or spreadsheet 3. Right click to Saved doc. 4. Verify
	 * 'Download' menu remains disabled 5. Right side column 'Download' link as
	 * disabled.
	 * 
	 * @author Girish
	 */
	@Test(dataProvider = "BriefcaseFileUpload", groups = { "smoke", "full" }, retryAnalyzer = RetryFailedTests.class)
	public void verifyDownloadMenuDisabledForNewDoc(String filename,
			String newBFFolder) throws Exception {
		if (SelNGBase.isExecutionARetry.get())
			handleRetry();

		if (ZimbraSeleniumProperties.getStringProperty("locale")
				.equals("en_US")) {
			obj.zButton.zClick(localize(locator.newDocument));
			SleepUtil.sleep(1500);
			ClientSessionFactory.session().selenium().selectWindow(
					ClientSessionFactory.session().selenium().getAllWindowTitles()[1]);
			ClientSessionFactory.session().selenium().windowFocus();
			zWaitTillObjectExist("button", localize(locator.save));
			ClientSessionFactory.session().selenium().type("xpath=//input[@type='text']",
					filename);
			obj.zButton.zClick(localize(locator.save));
			SleepUtil.sleep(1000);
			ClientSessionFactory.session().selenium().close();
			ClientSessionFactory.session().selenium().selectWindow(null);
			obj.zFolder.zClick(page.zBriefcaseApp.zBriefcaseFolder);
			SleepUtil.sleep(1000);
			obj.zBriefcaseItem.zClick(filename);
			obj.zBriefcaseItem.zRtClick(filename);
			SleepUtil.sleep(500);
			String download = ClientSessionFactory.session().selenium().getEval(
							"selenium.browserbot.getCurrentWindow().document.getElementById('zmi__Briefcase__SAVE_FILE').className");
			Assert.assertTrue(download.contains("ZDisabled"),
					"Download is in enable state");

			Boolean downloadLink = ClientSessionFactory.session().selenium().isElementPresent(
					"Link=" + localize(locator.saveFile));
			assertReport("false", downloadLink.toString(),
					"Verifying Download link exist");
		}

		SelNGBase.needReset.set(false);
	}
}