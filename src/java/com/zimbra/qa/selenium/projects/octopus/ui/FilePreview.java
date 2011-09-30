package com.zimbra.qa.selenium.projects.octopus.ui;

import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.HarnessException;

public class FilePreview extends AbsDisplay {

	public static class Locators {
		public static final Locators zFileWatchIcon = new Locators(
				"css=div[id=my-files-preview-toolbar] span[class=file-info-view-watch-icon]");
		public final String locator;

		private Locators(String locator) {
			this.locator = locator;
		}
	}

	/**
	 * The various displayed fields in preview panel
	 */
	public static enum Field {
		Name, Version, Size, Body
	}

	protected FilePreview(AbsApplication application) {
		super(application);
		logger.info("new " + FilePreview.class.getCanonicalName());
	}

	@Override
	public String myPageName() {
		return (this.getClass().getName());
	}

	@Override
	public AbsPage zPressButton(Button button) throws HarnessException {
		logger.info(myPageName() + " zPressButton(" + button + ")");

		tracer.trace("Click " + button);

		throw new HarnessException("no logic defined for button: " + button);

	}

	/**
	 * Get the string value of the specified field
	 * 
	 * @return the displayed string value
	 * @throws HarnessException
	 */
	public String zGetFileProperty(Field field) throws HarnessException {
		logger.info("DocumentPreview.zGetDocumentProperty(" + field + ")");
		String locator = null;

		if (field == Field.Name) {
			throw new HarnessException("implement me!");
		} else if (field == Field.Body) {
			/*
			 * To get the body contents, need to switch iframes
			 */
			try {
				this.sSelectFrame("//iframe[contains(@class, 'PreviewFrame')]");
				String bodyLocator = "css=body";
				// Make sure the body is present
				if (!this.sIsElementPresent(bodyLocator))
					throw new HarnessException("Unable to preview body!");

				// Get the body value
				// String body = this.sGetText(bodyLocator).trim();
				String html = this.zGetHtml(bodyLocator);

				logger.info("DocumentPreview GetBody(" + bodyLocator + ") = "
						+ html);
				return (html);
			} finally {
				// Make sure to go back to the original iframe
				this.sSelectFrame("relative=top");
			}
		} else if (field == Field.Version) {
			locator = "css=";
			this.sGetText(locator);
			throw new HarnessException("implement me!");
		} else if (field == Field.Size) {
			locator = "css=";
			this.sGetText(locator);
			throw new HarnessException("implement me!");
		}else{
			throw new HarnessException(" no such field " + field);
		}
	}

	@Override
	public boolean zIsActive() throws HarnessException {

		zWaitForElementPresent("css=div[id=preview-content-view]");

		return true;
	}
}
