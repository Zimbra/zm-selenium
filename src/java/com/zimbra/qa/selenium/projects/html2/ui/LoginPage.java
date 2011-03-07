package com.zimbra.qa.selenium.projects.html2.ui;

import static org.testng.Assert.assertTrue;

import java.util.Map;


import com.zimbra.common.service.ServiceException;
import com.zimbra.qa.selenium.framework.core.ClientSessionFactory;
import com.zimbra.qa.selenium.framework.core.SelNGBase;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.Stafzmprov;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.framework.util.ZimbraSeleniumProperties;
import com.zimbra.qa.selenium.projects.html2.tests.CommonTest;


@SuppressWarnings("static-access")
public class LoginPage extends CommonTest {


	/**
	 * Creates a random-user with the preferences passed in as Map. PS: user's
	 * locale is automatically set to locale in config.properties
	 * 
	 * @param accntAttrs
	 *            Map of key=value, where key is zmprov name, value is zmprov
	 *            value
	 * @return username of the random-user
	 * @throws ServiceException
	 * @throws HarnessException 
	 */
	public synchronized String zLoginToZimbraHTML(Map<String, Object> accntAttrs)
			throws ServiceException, HarnessException {
		String username = "";		
		// if we are retrying the execution, then use the same account.
		if (SelNGBase.isExecutionARetry.get())
			username = ClientSessionFactory.session().currentUserName();
		else{		
			username = Stafzmprov.getRandomAccount();			
		}
		zLoginToZimbraHTML(username);
		return username;
	}

	/**
	 * Logs into Zimbra using the given username. Also sets
	 * currentBrowserName-variable
	 * 
	 * @param username
	 */
	public void zLoginToZimbraHTML(String username)  throws HarnessException  {
		zLoginToZimbraHTML(username, "test123");
	}

	/**
	 * Logs into Zimbra using the given username. Also sets
	 * currentBrowserName-variable
	 * 
	 * @param username
	 * @param password
	 */
	public void zLoginToZimbraHTML(String username, String password)  throws HarnessException  {
		
		ClientSessionFactory.session().setCurrentUser(null);
		ZimbraSeleniumProperties.setAppType(ZimbraSeleniumProperties.AppType.HTML);
		SelNGBase.openApplication();
		SleepUtil.sleep(1500);
		
		obj.zEditField.zType("Username:", username);
		obj.zPwdField.zType("Password:", password);
		obj.zButton.zClick("class=zLoginButton");
		
		SleepUtil.sleep(2000);// without this we get permission denied error
		
		for (int i = 0; i < 10; i++) {
			if (ClientSessionFactory.session().selenium().isElementPresent("id=searchField"))
				break;
			SleepUtil.sleep(2000);
		}
		// TODO: What happens when the id=searchField never shows up?
		// We should throw an exception

		SleepUtil.sleep(2000);// wait another 2 secs after we see the search
		
		ClientSessionFactory.session().setCurrentUser(new ZimbraAccount(username, password));

	}


	/**
	 * clicks logoff link
	 * 
	 * @throws Exception
	 */
	public void logoutOfZimbraAjax() throws Exception {
		ClientSessionFactory.session().selenium().click("link=" + localize("logOff"));
		SleepUtil.sleep(1000);
		assertTrue(true);
	}
	
	
}
