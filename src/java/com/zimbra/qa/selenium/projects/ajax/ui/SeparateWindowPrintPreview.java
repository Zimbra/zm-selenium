/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2013, 2014, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.ui;

import java.awt.event.*;
import java.util.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;

public class SeparateWindowPrintPreview extends SeparateWindow {

	public SeparateWindowPrintPreview(AbsApplication application) {
		super(application);

		existingWindowNames = new ArrayList<String>();
		this.DialogWindowName = null;
	}

	protected boolean IsDismissed = false;

	public void zDismissPrintDialog() throws HarnessException {

		if (IsDismissed) {
			return;
		}

		// Wait for the print dialog
		SleepUtil.sleepMedium();
		zKeyboard.zTypeKeyEvent(KeyEvent.VK_ESCAPE);

		IsDismissed = true;
	}

	public void zSetWindowName() throws HarnessException {
		zDismissPrintDialog(); // On the first attempt, dismiss the print dialog
		super.zSetWindowName();
	}
}