package com.zimbra.qa.selenium.projects.html.clients;

import org.testng.Assert;

import com.zimbra.qa.selenium.framework.core.*;
import com.zimbra.qa.selenium.framework.util.SleepUtil;


public class ToastAlertMessage extends SelNGBase{
	public String zGetMsg() throws Exception {
		SleepUtil.sleep(1500);
		return ClientSessionFactory.session().selenium().getText("id=app_st_msg_div");
	}
	
	public boolean zAlertMsgExists(String expectedMsg, String customMsg) throws Exception {
	    String actMsg = "";
		for(int i =0; i < 15; i++) {
		    	actMsg = zGetMsg();
			if(actMsg.indexOf(expectedMsg) >=0)
				return true;
			SleepUtil.sleep(2000);
		}
		Assert.assertTrue(false, customMsg + "\nActual("+actMsg+") didnt contain Expected("+expectedMsg+")");
		return false;
	}	
}
