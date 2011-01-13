package com.zimbra.qa.selenium.projects.admin.ui;

import com.zimbra.qa.selenium.framework.ui.AbsApplication;

/**
 * The "Edit Zimlet" page has the same functionality as "Edit Admin Extension"
 * @author zimbra
 *
 */
public class PageEditZimlet extends PageEditAdminExtension {

	public PageEditZimlet(AbsApplication application) {
		super(application);
	}

	/* (non-Javadoc)
	 * @see projects.admin.ui.AbsTab#myPageName()
	 */
	@Override
	public String myPageName() {
		return (this.getClass().getName());
	}


}
