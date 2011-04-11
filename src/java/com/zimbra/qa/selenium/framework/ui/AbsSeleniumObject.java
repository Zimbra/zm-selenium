package com.zimbra.qa.selenium.framework.ui;

import java.util.*;

import org.apache.log4j.*;

import com.thoughtworks.selenium.*;
import com.zimbra.qa.selenium.framework.core.*;
import com.zimbra.qa.selenium.framework.util.*;

/**
 * The <code>AbsSeleniumObject</code> class is a base class that all "GUI"
 * objects can derive from, allowing access to the DefaultSelenium methods.
 * <p>
 * The <code>AbsSeleniumObject</code> is implemented as a thread safe (on the
 * test class level) way to access DefaultSelenium methods.
 * <p>
 * It is intended that Pages, Forms, Trees, etc. will derive from
 * AbsSeleniumObject and call DefaultSelenium methods using AbsSeleniumObject
 * methods. The class implementations should not use the {@link ClientSession}
 * objects directly.
 * <p>
 * Selenium methods start with a lower case "s", so that
 * {@link DefaultSelenium#click(String)} can be accessed using
 * {@link #sClick(String)}.
 * <p>
 * Zimbra specific methods start with a lower case "z", such as the
 * Zimbra-specific implementation of click {@link #zClick(String)}, which
 * performs the more stable action of MOUSE_DOWN followed by MOUSE_UP.
 * <p>
 * 
 * @author Matt Rhoades
 * 
 */
public abstract class AbsSeleniumObject {
	protected static final int LoadDelay = 30000; // wait 30 seconds for objects
													// to load
	protected static Logger logger = LogManager
			.getLogger(AbsSeleniumObject.class);

	protected static final Logger tracer = LogManager
			.getLogger(ExecuteHarnessMain.TraceLoggerName);

	public AbsSeleniumObject() {
		logger.info("new " + AbsSeleniumObject.class.getCanonicalName());
	}

	
	public boolean zIsLocatorPresent(String locator) {
		if (!sIsElementPresent(locator)) {
			logger.info("isVisiblePerPosition(" + locator
					+ ") element is not present");
			return (false);
		}
		return true;
	}
	
	/**
	 * Zimbra: return if the specified element is visible per style coordinates
	 * 
	 * @param locator
	 * @param leftLimit
	 * @param topLimit
	 * @return
	 */
	public boolean zIsVisiblePerPosition(String locator, int leftLimit,
			int topLimit) {

		// Check if the locator is present
		if(zIsLocatorPresent(locator));
		
		// Find the current position
		Number left = ClientSessionFactory.session().selenium()
				.getElementPositionLeft(locator);
		Number top = ClientSessionFactory.session().selenium()
				.getElementPositionTop(locator);

		// If the position is less than the limits, then it is hidden
		boolean hidden = ((left.intValue() < leftLimit) && (top.intValue() < topLimit));
		logger.info("isVisiblePerPosition(" + locator + ") - (left, top) = ("
				+ left.intValue() + ", " + top.intValue()
				+ ") (limit, limit) = (" + leftLimit + ", " + topLimit + ") "
				+ (!hidden));
		return (!hidden);
	}

	/**
	 * Execute mouseDownAt followed by mouseUpAt on the (0,0) position of a
	 * locator
	 * 
	 * @param locator
	 * @throws HarnessException
	 */
	public void zClick(String locator) throws HarnessException {
		// Check if the locator is present
		if(zIsLocatorPresent(locator));

		ClientSessionFactory.session().selenium().mouseDownAt(locator, "0,0");
		ClientSessionFactory.session().selenium().mouseUpAt(locator, "0,0");

		logger.info("zClick(" + locator + ")");
	}

	/**
	 * Execute mouseDownRight followed by mouseUpRight on a locator
	 * 
	 * @param locator
	 * @throws HarnessException
	 */
	public void zRightClick(String locator) throws HarnessException {
		// Check if the locator is present
		if(zIsLocatorPresent(locator));

		ClientSessionFactory.session().selenium().mouseDownRightAt(locator,
				"0,0");
		ClientSessionFactory.session().selenium()
				.mouseUpRightAt(locator, "0,0");
		logger.info("zRightClick(" + locator + ")");
	}

	/**
	 * Execute select on a windowID
	 * 
	 * @param windowID
	 * @throws HarnessException
	 */
	public void zSelectWindow(String windowID) throws HarnessException {
		logger.info("zSelectWindow(" + windowID + ")");

		this.sSelectWindow(windowID);

		this.sWindowFocus();

		this.sWindowMaximize();

	}

	public String zGetHtml(String locator) throws HarnessException {
		try {
			String script = "this.page().findElement('" + locator
					+ "').innerHTML";
			String html = ClientSessionFactory.session().selenium().getEval(
					script);
			logger.info("zGetHtml(" + locator + ") = " + html);
			return (html);
		} catch (SeleniumException e) {
			throw new HarnessException("Unable to grab HTML from locator "
					+ locator, e);
		}

	}

	/**
	 * The method writes in dialog box's input fields.
	 * @param locator
	 * @param value
	 * @throws HarnessException
	 */
	public void zType(String locator, String value) throws HarnessException {
		// Check if the locator is present
		if(zIsLocatorPresent(locator));

		
		ClientSessionFactory.session().selenium().focus(locator);
		ClientSessionFactory.session().selenium().clickAt(locator, "0,0");
		ClientSessionFactory.session().selenium().type(locator, value); 
	
		logger.info("zType(" + locator + ","  + value + ")");	
	}
	
	
	// // ***
	// Start: Selenium methods
	// // ***

	/**
	 * DefaultSelenium.getEval()
	 * 
	 * @param script
	 */
	public String sGetEval(String script) throws HarnessException {
		String value = ClientSessionFactory.session().selenium()
				.getEval(script);
		logger.info("getEval(" + script + ") = " + value);
		return (value);
	}

	/**
	 * DefaultSelenium.getHtmlSource()
	 * 
	 * @param locator
	 */
	public String sGetHtmlSource() throws HarnessException {
		String htmlString = ClientSessionFactory.session().selenium()
				.getHtmlSource();
		logger.info("getHtmlSource()");
		return (htmlString);
	}

	/**
	 * DefaultSelenium.getElementPositionLeft()
	 * 
	 * @param locator
	 */
	public int sGetElementPositionLeft(String locator) {
		int n = ClientSessionFactory.session().selenium().getElementPositionLeft(locator).intValue();
		logger.info("getElementPositionLeft("+ locator +") = "+ n);
		return (n);
	}
	
	/**
	 * DefaultSelenium.getElementPositionTop()
	 * 
	 * @param locator
	 */
	public int sGetElementPositionTop(String locator) {
		int n = ClientSessionFactory.session().selenium().getElementPositionTop(locator).intValue();
		logger.info("getElementPositionTop("+ locator +") = "+ n);
		return (n);
	}
	

	/**
	 * getNextSiblingId()
	 * 
	 * @param
	 */
	public String sGetNextSiblingId(String id) {
		String sibLingid = ClientSessionFactory.session().selenium().getEval(
				"this.browserbot.getUserWindow().document.getElementById('"
						+ id + "')" + ".nextSibling.id");
		logger.info("sGetNextSiblingId( " + id + ") = " + sibLingid);
		return (sibLingid);
	}

	/**
	 * DefaultSelenium.getSelectedId()
	 * 
	 * @param locator
	 */
	public String sGetSelectedId(String locator) {
		String id = ClientSessionFactory.session().selenium().getSelectedId(
				locator);
		logger.info("getSelectedId(" + locator + ") = " + id);
		return (id);
	}

	/**
	 * DefaultSelenium.click()
	 */
	public void sClick(String locator) {
		// Cast to DefaultSelenium ... Workaround until ZimbraSelnium is removed
		((DefaultSelenium) ClientSessionFactory.session().selenium())
				.mouseOver(locator);
		((DefaultSelenium) ClientSessionFactory.session().selenium()).clickAt(
				locator, "");
		logger.info("click(" + locator + ")");
	}

	/**
	 * DefaultSelenium.close()
	 */
	public void sClose() {
		ClientSessionFactory.session().selenium().close();
		logger.info("close()");
	}

	/**
	 * DefaultSelenium.doubleClick()
	 */
	public void sDoubleClick(String locator) {
		// Cast to DefaultSelenium ... Workaround until ZimbraSelnium is removed
		((DefaultSelenium) ClientSessionFactory.session().selenium())
				.doubleClick(locator);
		logger.info("doubleClick(" + locator + ")");
	}

	/**
	 * DefaultSelenium.waitForPageToLoad()
	 */
	public void sWaitForPageToLoad() {
		String timeout = ZimbraSeleniumProperties.getStringProperty(
				"selenium.maxpageload.msec", "10000");

		try {

			// Cast to DefaultSelenium ... Workaround until ZimbraSelnium is
			// removed
			logger.info("waitForPageToLoad(" + timeout + ")");
			((DefaultSelenium) ClientSessionFactory.session().selenium())
					.waitForPageToLoad(timeout);

		} catch (Exception ex) {
			logger.warn("sWaitForPageToLoad() error", ex);
		}
	}

	/**
	 * DefaultSelenium.mouseDown()
	 */
	public void sMouseDown(String locator) {
		ClientSessionFactory.session().selenium().mouseDown(locator);
		logger.info("mouseDown(" + locator + ")");
	}
	public void sMouseDownAt(String locator, String coordString) {
		ClientSessionFactory.session().selenium().mouseDownAt(locator, coordString);
		logger.info("mouseDownAt(" + locator + ",'" + coordString + "')");
	}

	/**
	 * DefaultSelenium.mouseOver()
	 */
	public void sMouseOver(String locator) {
		ClientSessionFactory.session().selenium().mouseOver(locator);
		logger.info("mouseOver(" + locator + ")");
	}

	/**
	 * DefaultSelenium.mouseUp()
	 */
	public void sMouseUp(String locator) {
		ClientSessionFactory.session().selenium().mouseUp(locator);
		logger.info("mouseUp(" + locator + ")");
	}

	/**
	 * DefaultSelenium.mouseMoveAt()
	 */
	public void sMouseMoveAt(String locator, String coordString) {
		ClientSessionFactory.session().selenium().mouseMoveAt(locator, coordString);
		logger.info("mouseMoveAt(" + locator + ",'" + coordString + "')");
	}
	public void sMouseMove(String locator) {
		ClientSessionFactory.session().selenium().mouseMove(locator);
		logger.info("mouseMoveAt(" + locator + ")");
	}
	/**
	 * DefaultSelenium.mouseUpAt()
	 */
	public void sMouseUpAt(String locator, String coordString) {
		ClientSessionFactory.session().selenium().mouseUpAt(locator, coordString);
		logger.info("mouseUpAt(" + locator + ",'" + coordString +")'");
	}

	/**
	 * DefaultSelenium.focus()
	 */
	public void sFocus(String locator) {
		ClientSessionFactory.session().selenium().focus(locator);
		logger.info("focus(" + locator + ")");
	}

	/**
	 * DefaultSelenium.isElementPresent()
	 */
	public boolean sIsElementPresent(String locator) {
		// Cast to DefaultSelenium ... Workaround until ZimbraSelnium is removed
		boolean present = ((DefaultSelenium) ClientSessionFactory.session()
				.selenium()).isElementPresent(locator);
		logger.info("isElementPresent(" + locator + ") = " + present);
		return (present);
	}

	/**
	 * DefaultSelenium.getXpathCount()
	 */
	public int sGetXpathCount(String xpath) {
		int count = ClientSessionFactory.session().selenium().getXpathCount(
				xpath).intValue();
		logger.info("getXpathCount(" + xpath + ") = " + count);
		return (count);
	}

	/**
	 * DefaultSelenium.getCssCount()
	 */
	public int sGetCssCount(String css) {
		int count = ClientSessionFactory.session().selenium().getCssCount(css)
				.intValue();
		logger.info("getCssCount(" + css + ") = " + count);
		return (count);
	}

	/**
	 * DefaultSelenium.getAllWindowTitles()
	 */
	public List<String> sGetAllWindowTitles() {
		logger.info("getAllWindowNames()");
		String[] windows = ClientSessionFactory.session().selenium()
				.getAllWindowTitles();
		return (Arrays.asList(windows));
	}

	/**
	 * DefaultSelenium.getAttribute()
	 * 
	 * @throws SeleniumException
	 */
	public String sGetAttribute(String locator) throws SeleniumException {

		// How can we determine whether the attribute exists or not?
		// Default selenium doesn't seem to have a way.
		// Tasks requires the SeleniumException to be thrown, then caught ...
		// so, can't convert to HarnessException
		//

		try {

			logger.info("getAttribute(" + locator + ")");
			String attrs = ClientSessionFactory.session().selenium()
					.getAttribute(locator);
			logger.info("getAttribute(" + locator + ") = " + attrs);
			return (attrs);

		} catch (SeleniumException e) {
			logger.error(e.getMessage(), e); // SeleniumExceptions don't use
												// logger, so log it here
			throw e;
		}
	}

	/**
	 * DefaultSelenium.isVisible()
	 */
	public boolean sIsVisible(String locator) {
		boolean visible = ClientSessionFactory.session().selenium().isVisible(
				locator);
		logger.info("isVisible(" + locator + ") = " + visible);
		return (visible);
	}

	/**
	 * zIsBusyOverlay()
	 */
	public boolean zIsBusyOverlay() {
		boolean isBusyOverlay = (ClientSessionFactory
				.session()
				.selenium()
				.getEval(
						"this.browserbot.getUserWindow().top.appCtxt.getShell().getBusy()")
				.equals("true"));

		logger.info("isBusyOverlay(" + ") = " + isBusyOverlay);
		return (isBusyOverlay);
	}

	/**
	 * zWaitForBusyOverlay()
	 */

	public void zWaitForBusyOverlay() throws HarnessException {
		logger.info("zWaitForBusyOverlay()");

		try {
			sWaitForCondition("selenium.browserbot.getUserWindow().top.appCtxt.getShell().getBusy()==false");
		} catch (Exception ex) {
			throw new HarnessException("Busy Overlay never disappeared!", ex);
		}
	}

	/**
	 * zWaitForBusyOverlayHTML
	 */
	public void zWaitForBusyOverlayHTML() throws HarnessException {
		logger.info("zWaitForBusyOverlayHTML()");
		SleepUtil.sleepLong();
	}

	/**
	 * DefaultSelenium.waitForCondition() Runs the specified JavaScript snippet
	 * repeatedly until it evaluates to true
	 * 
	 * @param locator
	 * @throws HarnessException
	 */
	private void sWaitForCondition(String condition) throws HarnessException {
		logger.info("sWaitForCondition(" + condition + "), timeout="
				+ LoadDelay);
		try {
			ClientSessionFactory.session().selenium().waitForCondition(
					condition, "" + LoadDelay);
		} catch (Exception ex) {
			throw new HarnessException(condition + " never become true: ", ex);
		}
	}

	/**
	 * zWaitForElementPresent() Waits for condition when
	 * selenium.isElementPresent() returns true
	 * 
	 * @param locator
	 * @throws HarnessException
	 */
	public void zWaitForElementPresent(String locator) throws HarnessException {
		logger.info("zWaitForElementPresent(" + locator + ")");

		try {
			sWaitForCondition("selenium.isElementPresent(\"" + locator + "\")");
		} catch (Exception ex) {
			throw new HarnessException(locator + " never appeared : ", ex);
		}
	}

	/**
	 * zWaitForElementDeleted() Waits for condition when
	 * selenium.isElementPresent() returns false
	 * 
	 * @param locator
	 * @throws HarnessException
	 */
	public void zWaitForElementDeleted(String locator) throws HarnessException {
		logger.info("zWaitForElementDeleted(" + locator + ")");
		try {
			sWaitForCondition("!selenium.isElementPresent(\"" + locator + "\")");
		} catch (Exception ex) {
			throw new HarnessException(locator + " never disappeared : ", ex);
		}
	}

	/**
	 * zWaitForElementEnabled(String id) Wait until the element (id) becomes
	 * enabled
	 * 
	 * @param id
	 * @throws HarnessException
	 */
	public void zWaitForElementEnabled(String id) throws HarnessException {
		logger.info("zWaitForElementEnabled(" + id + ")");

		// not applicable for the element
		if (!sIsElementPresent("xpath=//div[@id='" + id + "']")) {
			return;
		}

		for (int i = 0; i < 15; i++) {
			String attrs = sGetAttribute("xpath=(//div[@id='" + id
					+ "'])@class");
			if (!attrs.contains("ZDisabled"))
				return;
			SleepUtil.sleepSmall();
		}
		throw new HarnessException("Element with id=" + id
				+ " never become enabled: ");

	}

	/**
	 * zWaitForElementVisible(String id) Wait until the element (id) becomes
	 * visible
	 * 
	 * @param id
	 * @throws HarnessException
	 */
	public void zWaitForElementVisible(String locator) throws HarnessException {
		logger.info("zWaitForElementVisible(" + locator + ")");
		for (int i = 0; i < 15; i++) {
			if (zIsVisiblePerPosition(locator, 0, 0)) {
				return;
			}
			SleepUtil.sleepSmall();
		}
		throw new HarnessException(locator + "never visibled!");
	}

	/**
	 * zWaitForElementInvisible(String id) Wait until the element (id) becomes
	 * invisible
	 * 
	 * @param id
	 * @throws HarnessException
	 */
	public void zWaitForElementInvisible(String locator)
			throws HarnessException {
		logger.info("zWaitForElementInvisible(" + locator + ")");
		for (int i = 0; i < 15; i++) {
			if (!zIsVisiblePerPosition(locator, 0, 0)) {
				return;
			}
			SleepUtil.sleepSmall();
		}
		throw new HarnessException(locator + "never invisible!");
	}

	/**
	 * zWaitForWindow() Waits for condition when window with a given name is
	 * opened
	 * 
	 * @param name
	 * @throws HarnessException
	 */
	public void zWaitForWindow(String name) throws HarnessException {
		logger.info("zWaitForWindow(" + name + ")");

		try {
			sWaitForCondition("{var x; for(var windowName in selenium.browserbot.openedWindows ){"
					+ "var targetWindow = selenium.browserbot.openedWindows[windowName];"
					+ "if((!selenium.browserbot._windowClosed(targetWindow))&&"
					+ "(targetWindow.name == '"
					+ name
					+ "' || targetWindow.document.title == '"
					+ name
					+ "')){x=windowName;" + "}}}; x!=null;");
		} catch (Exception ex) {
			throw new HarnessException(name + " never opened : ", ex);
		}
	}

	/**
	 * zWaitForIframeText() Waits for condition when text appears in the iframe
	 * body
	 * 
	 * @param iframe
	 * @param text
	 * @throws HarnessException
	 */
	public boolean zWaitForIframeText(String iframe, String text)
			throws HarnessException {
		logger.info("zWaitForIframeText(" + iframe + ", " + text + ")");

		try {
			sWaitForCondition("var x = selenium.browserbot.findElementOrNull(\""
					+ iframe
					+ "\");if(x!=null){x=x.contentWindow.document.body;}if(browserVersion.isChrome){x.textContent.indexOf('"
					+ text
					+ "') >= 0;}else if(browserVersion.isIE){x.innerText.indexOf('"
					+ text
					+ "') >= 0;}else{x.textContent.indexOf('"
					+ text
					+ "') >= 0;}");
			return true;
		} catch (Exception ex) {
			throw new HarnessException(iframe + " never opened : ", ex);
		}
	}

	/**
	 * zIsWindowOpen() Checks if window with a given name is open
	 * 
	 * @param name
	 * @throws HarnessException
	 */
	public boolean zIsWindowOpen(String name) throws HarnessException {
		logger.info("zIsWindowOpen(" + name + ")");

		String result = sGetEval("{var x; for(var windowName in selenium.browserbot.openedWindows ){"
				+ "var targetWindow = selenium.browserbot.openedWindows[windowName];"
				+ "if((!selenium.browserbot._windowClosed(targetWindow))&&"
				+ "(targetWindow.name == '"
				+ name
				+ "' || targetWindow.document.title == '"
				+ name
				+ "')){x=windowName;" + "}}}; x!=null;");
		logger.info("zIsWindowOpen(" + name + ") = " + result);
		return (result.contains("true"));
	}

	/**
	 * zWaitForWindowClosed() Waits for condition when window with a given name
	 * is closed
	 * 
	 * @param name
	 * @throws HarnessException
	 */
	public boolean zWaitForWindowClosed(String name) throws HarnessException {
		logger.info("zWaitForWindowClosed(" + name + ")");

		try {
			String condition = "{var x; for(var windowName in selenium.browserbot.openedWindows ){"
					+ "var targetWindow = selenium.browserbot.openedWindows[windowName];"
					+ "if((!selenium.browserbot._windowClosed(targetWindow))&&"
					+ "(targetWindow.name == '"
					+ name
					+ "' || targetWindow.document.title == '"
					+ name
					+ "')){x=windowName;" + "}}}; x==null;";

			sWaitForCondition(condition);
			return true;
		} catch (Exception ex) {
			logger.info("Error: win not opened " + name, ex.fillInStackTrace());
			return false;
		}
	}

	/**
	 * DefaultSelenium.check()
	 */
	public void sCheck(String locator) {
		ClientSessionFactory.session().selenium().check(locator);
		logger.info("check(" + locator + ")");
	}

	/**
	 * DefaultSelenium.isChecked()
	 */
	public boolean sIsChecked(String locator) {
		// Cast to DefaultSelenium ... Workaround until ZimbraSelnium is removed
		boolean checked = ((DefaultSelenium) ClientSessionFactory.session()
				.selenium()).isChecked(locator);
		logger.info("isChecked(" + locator + ") = " + checked);
		return (checked);
	}

	/**
	 * DefaultSelenium.getText()
	 * 
	 * @throws HarnessException
	 */
	public String sGetText(String locator) throws HarnessException {
		try {
			String text = ClientSessionFactory.session().selenium().getText(
					locator);
			logger.info("DefaultSelenium.getText(" + locator + ") = " + text);
			return (text);
		} catch (SeleniumException e) {
			throw new HarnessException(e);
		}
	}

	/**
	 * DefaultSelenium.getValue()
	 */
	public String sGetValue(String locator) {
		String text = ClientSessionFactory.session().selenium().getValue(
				locator);
		logger.info("DefaultSelenium.getValue(" + locator + ") = " + text);
		return (text);
	}

	/**
	 * DefaultSelenium.getBodyText()
	 * 
	 * @return
	 */
	public String sGetBodyText() {
		String text = ClientSessionFactory.session().selenium().getBodyText();
		return text;

	}
	

	/**
	 * DefaultSelenium.type()
	 */
	public void sType(String locator, String text) {
		ClientSessionFactory.session().selenium().type(locator, text);
		logger.info("type(" + locator + ", " + text + ")");
	}

	/**
	 * DefaultSelenium.typeKeys()
	 */
	public void sTypeKeys(String locator, String text) {
		ClientSessionFactory.session().selenium().typeKeys(locator, text);
		logger.info("typeKeys(" + locator + ", " + text + ")");
	}

	/**
	 * DefaultSelenium.keyPressNative()
	 */
	public void sKeyPressNative(String code) {
		ClientSessionFactory.session().selenium().keyPressNative(code);
		logger.info("keyPressNative(" + code + ")");
	}

	/**
	 * DefaultSeleniu.selectFrame()
	 */
	public void sSelectFrame(String locator) {
		ClientSessionFactory.session().selenium().selectFrame(locator);
		logger.info("sSelectFrame(" + locator + ")");
	}

	/**
	 * DefaultSelenium.selectWindow()
	 */
	public void sSelectWindow(String windowID) {
		ClientSessionFactory.session().selenium().selectWindow(windowID);
		logger.info("sSelectWindow(" + windowID + ")");
	}

	/**
	 * DefaultSelenium.openWindow()
	 * @param url
	 * @param windowID
	 */
	public void sOpenWindow(String url,String windowID) {
		ClientSessionFactory.session().selenium().openWindow(url, windowID);
		logger.info("sOpenWindow(" + windowID + ")");
	}
	
	public void sWaitForPopUp(String windowID,String timeout ) {
		ClientSessionFactory.session().selenium().waitForPopUp(windowID, timeout);
		logger.info("sWaitForPopUp(" + windowID + ")");
	}
	/**
	 * DefaultSelenium.windowFocus()
	 */
	public void sWindowFocus() {
		ClientSessionFactory.session().selenium().windowFocus();
		logger.info("sWindowFocus()");
	}

	/**
	 * DefaultSelenium.wwindowMaximize()
	 */
	public void sWindowMaximize() {
		ClientSessionFactory.session().selenium().windowMaximize();
		logger.info("sWindowMaximize()");
	}

	// // ***
	// End: Selenium methods
	// // ***

}
