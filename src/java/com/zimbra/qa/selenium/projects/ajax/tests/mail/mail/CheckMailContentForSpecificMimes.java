/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.mail.mail;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.SetGroupMailByMessagePreference;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.DisplayMail;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.DisplayMail.Field;

public class CheckMailContentForSpecificMimes extends SetGroupMailByMessagePreference {

	public CheckMailContentForSpecificMimes() {
		logger.info("New "+ CheckMailContentForSpecificMimes.class.getCanonicalName());
	}


	@Bugs (ids = "11684")
	@Test (description = "Bug 11684 - Web client stops rendering message when it encounters an inline image",
			groups = { "functional" })

	public void CheckMailContentForSpecificMime_01() throws HarnessException {

		String subject = "subject13010064065623";
		String bodyBeforeImage = "K\u00e6re alle";
		String bodyAfterImage = "Problemet best\u00E5r";
		String mimeFile = ConfigProperties.getBaseDirectory() + "/data/public/mime/Bugs/Bug13911/bug13911att3713.txt";

		// Inject the sample mime
		injectMessage(app.zGetActiveAccount(), mimeFile);

		// Refresh current view
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

		// Select the message so that it shows in the reading pane
		DisplayMail display = (DisplayMail) app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		String body = display.zGetMailProperty(Field.Body);

		ZAssert.assertStringContains(body, bodyBeforeImage, "Verify the text before the image");
		ZAssert.assertStringContains(body, bodyAfterImage, "Verify the text after the image");
	}


	@Bugs (ids = "21415")
	@Test (description = "Bug 21415 - Message content truncated",
			groups = { "functional" })

	public void CheckMailContentForSpecificMime_02() throws HarnessException {

		String subject = "subject12998858731253";
		String beginningContent = "Uso Interno";
		String endingContent = "Esta mensagem";
		String mimeFile = ConfigProperties.getBaseDirectory() + "/data/public/mime/Bugs/Bug21415/bug21415att10438.txt";

		// Inject the sample mime
		injectMessage(app.zGetActiveAccount(), mimeFile);

		// Refresh current view
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

		// Select the message so that it shows in the reading pane
		DisplayMail display = (DisplayMail) app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		// Get the body
		String body = display.zGetMailProperty(Field.Body);

		// Make sure both the beginning and ending text appear
		ZAssert.assertStringContains(body, beginningContent, "Verify the ending text appears");
		ZAssert.assertStringContains(body, endingContent, "Verify the ending text appears");
	}


	@Bugs (ids = "21415")
	@Test (description = "Bug 21415 - Message content truncated",
			groups = { "functional" })

	public void CheckMailContentForSpecificMime_03() throws HarnessException {

		String subject = "subject12998912514374";
		String beginningContent = "Change 77406";
		String endingContent = "SkinResources.java";
		String mimeFile = ConfigProperties.getBaseDirectory() + "/data/public/mime/Bugs/Bug21415/bug21415att8124.txt";

		// Inject the sample mime
		injectMessage(app.zGetActiveAccount(), mimeFile);

		// Refresh current view
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

		// Select the message so that it shows in the reading pane
		DisplayMail display = (DisplayMail) app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		// Get the body
		String body = display.zGetMailProperty(Field.Body);

		// Make sure both the beginning and ending text appear
		ZAssert.assertStringContains(body, beginningContent, "Verify the ending text appears");
		ZAssert.assertStringContains(body, endingContent, "Verify the ending text appears");
	}


	@Bugs (ids = "25624")
	@Test (description = "Bug 25624 - Email reply to an invite shows a fragment of the message but opens as blank Message",
			groups = { "functional" })

	public void CheckMailContentForSpecificMime_04() throws HarnessException {

		String subject = "subject13001430504373";
		String mimeFile = ConfigProperties.getBaseDirectory() + "/data/public/mime/Bugs/Bug25624/bug25624att9322.txt";

		// Inject the sample mime
		injectMessage(app.zGetActiveAccount(), mimeFile);

		// Refresh current view
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

		// Select the message so that it shows in the reading pane
		DisplayMail display = (DisplayMail) app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		String body = display.zGetMailProperty(Field.Body);
		ZAssert.assertStringContains(body, "Hi Candace", "Verify the message content is not blank");
	}


	@Bugs (ids = "27796")
	@Test (description = "Bug 27796 - Message renders in standard client but not advanced",
			groups = { "functional" })

	public void CheckMailContentForSpecificMime_05() throws HarnessException {

		String subject = "subject13001430504374";
		String mimeFile = ConfigProperties.getBaseDirectory() + "/data/public/mime/Bugs/Bug27796/bug27796att10515.txt";

		// Inject the sample mime
		injectMessage(app.zGetActiveAccount(), mimeFile);

		// Refresh current view
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

		// Select the message so that it shows in the reading pane
		DisplayMail display = (DisplayMail) app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		String body = display.zGetMailProperty(Field.Body);
		ZAssert.assertStringContains(body, "I realized that may be the best place to post this question", "Verify the message content is not blank");
		ZAssert.assertStringContains(body, "http://twiki.corp.yahoo.com/view/Devel/DevelRandom", "Verify the message content contains the footer");

	}


	@Bugs (ids = "83527")
	@Test (description = "Bug 83527 - InlineImage attribute is missing for inline image in main branch",
			groups = { "functional" })

	public void CheckMailContentForSpecificMime_06() throws HarnessException {

		String subject = "subject13002239738283";
		String mimeFile = ConfigProperties.getBaseDirectory() + "/data/public/mime/Bugs/Bug31535/bug34401att14395.txt";

		// Inject the sample mime
		injectMessage(app.zGetActiveAccount(), mimeFile);

		// Refresh current view
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

		// Select the message so that it shows in the reading pane
		DisplayMail display = (DisplayMail) app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		HtmlElement bodyElement = display.zGetMailPropertyAsHtml(Field.Body);

		HtmlElement.evaluate(bodyElement, "//body//img", "class", "InlineImage", 1);
	}


	@Bugs (ids = "66192")
	@Test (description = "Blank body with JS error on viewing some messages",
			groups = { "functional" })

	public void CheckMailContentForSpecificMime_07() throws HarnessException {

		String subject = "Fwd: test bug66192";
		String bodytext = "Kind regards,";
		String mimeFile = ConfigProperties.getBaseDirectory() + "/data/public/mime/Bugs/Bug66192/bug66192.txt";

		// Inject the sample mime
		injectMessage(app.zGetActiveAccount(), mimeFile);

		// Refresh current view
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

		// Select the message so that it shows in the reading pane
		DisplayMail display = (DisplayMail) app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);
		String body = display.zGetMailProperty(Field.Body);

		// Verify body contents
		ZAssert.assertStringContains(body, bodytext, "Verify the ending text appears");
	}
}