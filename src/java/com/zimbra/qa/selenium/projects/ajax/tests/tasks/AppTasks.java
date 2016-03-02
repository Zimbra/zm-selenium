package com.zimbra.qa.selenium.projects.ajax.tests.tasks;



import java.util.HashMap;
import java.util.List;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.items.TaskItem;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.PrefGroupMailByMessageTest;


public class AppTasks extends PrefGroupMailByMessageTest {

	int pollIntervalSeconds = 60;

	@SuppressWarnings("serial")
	public AppTasks() {
		logger.info("New " + AppTasks.class.getCanonicalName());

		super.startingPage = app.zPageTasks;
		super.startingAccountPreferences = new HashMap<String , String>() {{
			
			put("zimbraPrefShowSelectionCheckbox", "TRUE");
			put("zimbraPrefTasksReadingPaneLocation", "bottom");
		}};

	}

	@Test(	description = "?app=tasks in url",
			groups = { "smoke" })
	public void AppTasks_01() throws HarnessException {

		//Go to AB tab
		app.zPageContacts.zNavigateTo();				
		SleepUtil.sleepMedium();

		FolderItem taskFolder = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Tasks);
		
		// Create a basic task to delete
		String subject = "task"+ ZimbraSeleniumProperties.getUniqueString();
				
		app.zGetActiveAccount().soapSend(
				"<CreateTaskRequest xmlns='urn:zimbraMail'>" +
					"<m >" +
			        	"<inv>" +
			        		"<comp name='"+ subject +"'>" +
			        			"<or a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
			        		"</comp>" +
			        	"</inv>" +
			        	"<su>"+ subject +"</su>" +
			        	"<mp ct='text/plain'>" +
			        		"<content>content"+ ZimbraSeleniumProperties.getUniqueString() +"</content>" +
			        	"</mp>" +
					"</m>" +
				"</CreateTaskRequest>");

		TaskItem task = TaskItem.importFromSOAP(app.zGetActiveAccount(), subject);
		ZAssert.assertNotNull(task, "Verify the task is created");

		// Reload the application, with app=tasks query parameter
		ZimbraURI uri = new ZimbraURI(ZimbraURI.getBaseURI());
		uri.addQuery("app", "tasks");
		app.zPageMail.sOpen(uri.toString());
		SleepUtil.sleepMedium();		
		// Refresh the tasks view
		app.zTreeTasks.zTreeItem(Action.A_LEFTCLICK, taskFolder);
		SleepUtil.sleepMedium();

		// Get all the tasks
		List<TaskItem> tasks = app.zPageTasks.zGetTasks();
		ZAssert.assertNotNull(tasks, "Verify the task list exists");

		TaskItem found = null;
		for (TaskItem t : tasks) {
			logger.info("Subject: looking for "+ subject +" found: "+ t.gSubject);
			if ( subject.equals(t.gSubject) ) {
				found = t;
				break;
			}
		}
		ZAssert.assertNotNull(found, "Verify the task is present");


	}
}