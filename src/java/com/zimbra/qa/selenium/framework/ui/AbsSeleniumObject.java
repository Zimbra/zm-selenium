/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2012, 2013, 2014, 2015, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.framework.ui;

import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Point;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.MoveTargetOutOfBoundsException;
import org.openqa.selenium.interactions.internal.Coordinates;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.WebDriverException;
import com.zimbra.qa.selenium.framework.core.ClientSession;
import com.zimbra.qa.selenium.framework.core.ClientSessionFactory;
import com.zimbra.qa.selenium.framework.core.ExecuteHarnessMain;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.PageMail;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import org.openqa.selenium.interactions.Mouse;
import org.openqa.selenium.interactions.HasInputDevices;
import org.openqa.selenium.internal.Locatable;

/**
 * The <code>AbsSeleniumObject</code> class is a base class that all "GUI"
 * objects can derive from selenium methods.
 * <p>
 * The <code>AbsSeleniumObject</code> is implemented as a thread safe (on the
 * test class level) way to access selenium methods.
 * <p>
 * It is intended that Pages, Forms, Trees, etc. will derive from
 * AbsSeleniumObject and call selenium methods using AbsSeleniumObject
 * methods. The class implementations should not use the {@link ClientSession}
 * objects directly.
 * <p>
 * Selenium methods start with a lower case "s", so that
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

	protected static final int LoadDelay = 30000;
	protected static Logger logger = LogManager.getLogger(AbsSeleniumObject.class);
	protected static final Logger tracer = LogManager.getLogger(ExecuteHarnessMain.TraceLoggerName);
	public AbsSeleniumObject() {
		logger.info("new " + AbsSeleniumObject.class.getCanonicalName());
	}

	protected static class Coordinate {
		final int X;
		final int Y;

		public Coordinate(int x, int y) {
			this.X = x;
			this.Y = y;
		}

		public String toString() {
			return (this.X + "," + this.Y);
		}
	}

	protected class BrowserMasks {
		public static final int BrowserMaskIE = 1 << 0;
		public static final int BrowserMaskFF = 1 << 5;
		public static final int BrowserMaskChrome = 1 << 11;
		public static final int BrowserMaskSafari = 1 << 15;
	}

	private static String BrowserUserAgent = null;
	private static int BrowserMask = 0;

	protected boolean zIsBrowserMatch(int mask) throws HarnessException {

		if (BrowserUserAgent == null) {
			BrowserUserAgent = sGetEval("return navigator.userAgent;");
			logger.info("UserAgent: (navigator.userAgent;) >>>>>> "	+ BrowserUserAgent);
		}

		if (BrowserMask == 0) {
			if (BrowserUserAgent.contains("Firefox/")) {
				BrowserMask |= BrowserMasks.BrowserMaskFF;
			} else if (BrowserUserAgent.contains("Chrome/")) {
				BrowserMask |= BrowserMasks.BrowserMaskChrome;
			} else if (BrowserUserAgent.contains("MSIE")) {
				BrowserMask |= BrowserMasks.BrowserMaskIE;
			} else if (BrowserUserAgent.contains("Safari/")) {
				BrowserMask |= BrowserMasks.BrowserMaskSafari;
			}
		}
		return ((BrowserMask & mask) == mask);
	}


	protected WebDriver webDriver() {
		return ClientSessionFactory.session().webDriver();
	}


	public boolean zIsVisiblePerPosition(String locator, int leftLimit, int topLimit) throws HarnessException {
		logger.info("zIsVisiblePerPosition(" + locator + ")");
		return elementVisiblePerPosition(locator);
	}


	public void sClick(String locator, WebElement... elements) throws HarnessException {
	    logger.info("click(" + locator + ")");
	    SleepUtil.sleepVerySmall();

	    try {
		    logger.info("click()");
		    WebElement we = null;
		    if (elements != null && elements.length > 0) {
		    	we = elements[0];
		    } else {
		    	we = getElement(locator);
		    }
		    we.click();
		    zWaitForBusyOverlay();

	    } catch (Exception ex) {
	    	throw new HarnessException("Unable to click on locator " + locator, ex);
	    }
	}


	public void sClickAt(String locator, String coord, WebElement... elements) throws HarnessException {
	    logger.info("sClickAt(" + locator + "," + coord + ")");
	    SleepUtil.sleepVerySmall();

	    try {
		    WebElement we = null;
		    if (elements != null && elements.length > 0) {
		    	we = elements[0];
		    } else {
		    	we = getElement(locator);
		    }

		    Actions builder = new Actions(webDriver());
		    Action action = builder.moveToElement(we).click(we).build();
		    action.perform();
		    zWaitForBusyOverlay();

	    } catch (Exception ex) {
	    	throw new HarnessException("Unable to clickAt on locator " + locator, ex);
	    }
	}


	public void sClickJavaScript(String locator, WebElement... elements) throws HarnessException {
	    logger.info("sClickJavaScript(" + locator + ")");
	    SleepUtil.sleepVerySmall();

	    try {
		    WebElement we = null;
		    if (elements != null && elements.length > 0) {
		    	we = elements[0];
		    } else {
		    	we = getElement(locator);
		    }

		    ((JavascriptExecutor)webDriver()).executeScript("arguments[0].click()", we);
		    zWaitForBusyOverlay();

	    } catch (Exception ex) {
	    	throw new HarnessException("Unable to clickAt on locator " + locator, ex);
	    }
	}
	
	
	public void zClick(String locator, WebElement... elements) throws HarnessException {
		sClick(locator, elements);
	}


	public void zClickAt(String locator, String coord, WebElement... elements) throws HarnessException {
		sClickAt(locator, coord, elements);
	}


	public void zRightClick(String locator, WebElement... elements) throws HarnessException {
	    logger.info("zRightClick(" + locator + ")");
	    SleepUtil.sleepSmall();

	    WebElement we = null;
		if (elements != null && elements.length > 0) {
		    we = elements[0];
		} else {
		    we = getElement(locator);
		}

	    try {
		    final Actions builder = new Actions(webDriver());
		    final Action rClick = builder.contextClick(we).build();
		    rClick.perform();

	    } catch(Exception ex) {
	    	throw new HarnessException("Unable to rightClick on locator " + locator, ex);
	    }
	}


	public void zRightClickAt(String locator, String coord, WebElement... elements) throws HarnessException {
		logger.info("zRightClickAt(" + locator + "," + coord + ")");
		SleepUtil.sleepSmall();

	    WebElement we = null;
		if (elements != null && elements.length > 0) {
		    we = elements[0];
		} else {
		    we = getElement(locator);
		}

	    try {
    	    final Actions builder = new Actions(webDriver());
    	    final Action rClick = builder.moveToElement(we).contextClick(we).build();
    	    rClick.perform();

	    } catch(Exception ex) {
	    	throw new HarnessException("Unable to rightClickAt on locator " + locator, ex);
	    }
	}


	public void zCheckboxSet(String locator, boolean status) throws HarnessException {
		logger.info("zCheckboxSet(" + locator + ")");
		SleepUtil.sleepSmall();

		if ( !this.sIsElementPresent(locator) ) {
			throw new HarnessException(locator + " not present!");
		}

		if ( this.sIsChecked(locator) == status ) {
			logger.debug("checkbox status matched.  not doing anything");
			return;
		}
		if ( status == true ) {
			this.sCheck(locator);
		} else {
			this.sUncheck(locator);
		}

		this.zWaitForBusyOverlay();
	}


	public void zSelectWindow(String windowID) throws HarnessException {
		logger.info("zSelectWindow(" + windowID + ")");
		this.sSelectWindow(windowID);
		this.sWindowFocus();
		if (windowID != null) {
			//this.sWindowMaximize();
		}
	}


	public String zGetHtml(String locator, WebElement... elements) throws HarnessException {
		logger.info("zGetHtml(" + locator + ")");

		try {
			String html = "";
			String script = "";

			WebElement we = null;
			if (elements != null && elements.length > 0) {
			    we = elements[0];
			} else {
			    we = getElement(locator);
			}

			html = executeScript(script, we);
			return (html);

		} catch (WebDriverException e) {
			throw new HarnessException("Unable to grab HTML from locator " + locator, e);
		}

	}


	public void zType(String locator, String value, WebElement... elements) throws HarnessException {
	    logger.info("zType(" + locator + "," + value + ")");
	    SleepUtil.sleepSmall();

	    WebElement we = null;
		logger.info("getElement");
		if (elements != null && elements.length > 0) {
		    we = elements[0];
		} else {
		    we = getElement(locator);
		}
	    this.sFocus(locator, we);
	    this.sClickAt(locator, "0,0", we);
	    this.sType(locator, value, we);
	    SleepUtil.sleepVerySmall();
	}


	public void zTypeKeys(String locator, String value, WebElement... elements ) throws HarnessException {
		logger.info("sType()");
		WebElement we = null;
		if (elements != null && elements.length > 0) {
		    we = elements[0];
		} else {
		    we = getElement(locator);
		}

		sType(locator, value, we);
		SleepUtil.sleepVerySmall();
	}


	public void zKeyDown(String keyCode) throws HarnessException {
		logger.info("zKeyDown(" + keyCode + ")");

		if (keyCode == null || keyCode.isEmpty()) {
			throw new HarnessException("keyCode needs to be provided");
		}

		String locator = "//html//body";
		for (String kc : keyCode.split(",")) {
			zKeyEvent(locator, kc, "keydown");
		}
	}


	public void zKeyEvent(String locator, String keyCode, String event) throws HarnessException {
		logger.info("zKeyEvent(" + keyCode + ")");

		if (this.zIsBrowserMatch(BrowserMasks.BrowserMaskIE)) {
			executeScript(
				"try {var el = arguments[0]; "
				+ "var evObj = document.createEventObject(); "
				+ "evObj.keyCode="
				+ keyCode
				+ "; evObj.repeat = false; "
				+ "el.focus(); el.fireEvent(\"on"
				+ event
				+ "\", evObj);} catch (err) {return(err.message)}",
				getElement(locator));
		} else if (this.zIsBrowserMatch(BrowserMasks.BrowserMaskFF)) {
			executeScript(
				"try {var el = arguments[0]; "
			    + "var evo = document.createEvent('HTMLEvents'); "
				+ "evo.initEvent('"
				+ event
				+ "', true, true, window, 1 ); evo.keyCode="
				+ keyCode
				+ "; el.blur(); el.focus(); el.dispatchEvent(evo);} catch (err) {return(err.message)}",
				getElement("css=html body"));
		} else {
			executeScript(
				"try {var el = arguments[0]; "
			    + "var evo = document.createEvent('HTMLEvents'); "
				+ "evo.initEvent('"
				+ event
				+ "', true, true, window, 1 ); evo.keyCode="
				+ keyCode
				+ "; el.blur(); el.focus(); el.dispatchEvent(evo);} catch (err) {return(err.message)}",
				getElement("css=html body"));
		}
	}


	public void zTypeFormattedText(String locator, String html) throws HarnessException {
		logger.info("zTypeFormattedText(" + locator + ", " + html + ")");

		executeScript("try {var bodytext=\""
			+ html
			+ "\";"
			+ "var iframe_element=arguments[0];"
			+ "var iframe_body=iframe_element.contentWindow.document.body;"
			+ "iframe_body.innerHTML = bodytext;} catch (err) {return(err);}",getElement(locator));

	}


	public void sFireEvent(String locator, String eventName, WebElement... elements) throws HarnessException {
		logger.info("sFireEvent(" + eventName + ")");

		@SuppressWarnings("unused")
		WebElement we = null;
		if (elements != null && elements.length > 0) {
		    we = elements[0];
		} else {
		    we = getElement(locator);
		}

		/*
		JavascriptLibrary jsLib = new JavascriptLibrary();
		jsLib.callEmbeddedSelenium(webDriver(), "doFireEvent", we, eventName);
		*/

		logger.info("fireEvent(" + locator + ", " + eventName + ")");
	}


	public String sGetEval(String script) throws HarnessException {
		logger.info("sGetEval(" + script + ")");

		String value = null;
		try {
			value = executeScript(script);
			return (value);
		} catch (Exception e) {
			logger.info(e + " executing " + script);
			return value;
		}
	}


	public String sGetHtmlSource() throws HarnessException {
		logger.info("sGetHtmlSource()");
		String htmlSource = null;
		htmlSource = webDriver().getPageSource();
		return (htmlSource);
	}

	public String sGetHtmlBody() throws HarnessException {
		logger.info("sGetHtmlBody()");
		String htmlBody = null;
		htmlBody = webDriver().getPageSource();
		htmlBody = htmlBody.substring(htmlBody.indexOf("<body"));
		return (htmlBody);
	}


	public int sGetElementHeight(String locator) throws HarnessException {
		logger.info("sGetEval(" + locator + ")");
		try {
			int n = -1;
			n = getElement(locator).getSize().height;
			return (n);
		} catch (WebDriverException e) {
			throw new HarnessException(e);
		}
	}


	public int sGetElementWidth(String locator) throws HarnessException {
		try {
			int n = -1;
			WebElement el = getElement(locator);
			Dimension dim = el.getSize();
			n = dim.width;
			logger.info("getElementWidth(" + locator + ") = " + n);
			return (n);
		} catch (WebDriverException e) {
			throw new HarnessException(e);
		}
	}


	public int sGetElementPositionLeft(String locator) throws HarnessException {
		try {
			int n = -1;
			n = getElement(locator).getLocation().x;
			logger.info("getElementPositionLeft(" + locator + ") = " + n);
			return (n);
		} catch (WebDriverException e) {
			throw new HarnessException(e);
		}
	}


	public int sGetElementPositionTop(String locator) throws HarnessException {
		try {
			int n = -1;
			n = getElement(locator).getLocation().y;
			logger.info("getElementPositionTop(" + locator + ") = " + n);
			return (n);
		} catch (WebDriverException e) {
			throw new HarnessException(e);
		}
	}


	public String sGetNextSiblingId(String id) throws HarnessException {
		String sibLingid = null;
		sibLingid = executeScript("return document.getElementById('" + id + "')" + ".nextSibling.id");
		logger.info("sGetNextSiblingId( " + id + ") = " + sibLingid);
		return (sibLingid);
	}


	public String sGetPreviousSiblingId(String id) throws HarnessException {
		String sibLingid = null;
		sibLingid = executeScript("return document.getElementById('" + id + "')" + ".previousSibling.id");
		logger.info("sGetPreviousSiblingId( " + id + ") = " + sibLingid);
		return (sibLingid);
	}


	public String sGetSelectedId(String locator) throws HarnessException {
		String id = null;
		Select select =  new Select (getElement(locator));
		WebElement we = select.getFirstSelectedOption();
		id = we.getAttribute("id");
		logger.info("getSelectedId(" + locator + ") = " + id);
		return (id);
	}


	public void sClose() throws HarnessException {
	    logger.info("close()");
		webDriver().close();
	}


	public void sDoubleClick(String locator, WebElement... elements) throws HarnessException {
	    logger.info("doubleClick(" + locator + ")");

	    try {
		    WebElement we = null;
		    if (elements != null && elements.length > 0) {
		    	we = elements[0];
		    } else {
		    	we = getElement(locator);
		    }
		    Actions actions = new Actions(webDriver());
		    Action doubleClick = actions.doubleClick(we).build();
		    doubleClick.perform();

	    } catch(Exception ex) {
	    	throw new HarnessException("Unable to doubleclick on locator " + locator, ex);
	    }
	}


	public String zGetCenterPoint(String locator) throws HarnessException {
		int height = -1;
		int width  = -1;
		height = sGetElementHeight(locator) / 2;
		width =  sGetElementWidth(locator) / 2;

		String centerHeight = Integer.toString(height);
		String centerWidth = Integer.toString(width);
   	   	return new StringBuilder("(").append(centerWidth).append(",").append(centerHeight).append(")").toString();
	}


	public void sWaitForPageToLoad() throws HarnessException {
		String timeout = ConfigProperties.getStringProperty("selenium.maxpageload.msec", "20000");

		try {

			logger.info("waitForPageToLoad(" + timeout + ")");

			Wait<WebDriver> wait = new FluentWait<WebDriver>(webDriver()).withTimeout(10, TimeUnit.SECONDS).pollingEvery(500, TimeUnit.MILLISECONDS).ignoring(NoSuchElementException.class);

			try {
				wait.until(new ExpectedCondition<Boolean>() {
					public Boolean apply(WebDriver driver) {
						Boolean result = false;
						String str;
						str = executeScript("return document['readyState'] ? 'complete' == document.readyState : true");
						if (str!=null && str.contentEquals("true")) {
							result = true;
						}
						return result;
					}
				});

			} catch (Exception e) {
				logger.warn(e);
			}
		} catch (Exception ex) {
			logger.warn("sWaitForPageToLoad() error", ex);
		}
	}


	public void sMouseDown(String locator, WebElement... elements) throws HarnessException {
	    logger.info("mouseDown(" + locator + ")");

	    try {
		    WebElement we = null;
		    if (elements != null && elements.length > 0) {
		    	we = elements[0];
		    } else {
		    	we = getElement(locator);
		    }
		    Mouse mouse = ((HasInputDevices)webDriver()).getMouse();
		    mouse.mouseDown(((RemoteWebElement)we).getCoordinates());
		    SleepUtil.sleepVerySmall();

	    } catch(Exception ex) {
	    	throw new HarnessException("Unable to mouseDown on locator " + locator, ex);
	    }
	}


	public void sMouseDownAt(String locator, String coordString) throws HarnessException {
	    logger.info("mouseDownAt(" + locator + ",'" + coordString + "')");

	    try {
		    Coordinates co =  ((RemoteWebElement)getElement(locator)).getCoordinates();
		    Mouse mouse = ((HasInputDevices)webDriver()).getMouse();
		    mouse.mouseMove(co,0,0);
		    mouse.mouseDown(co);
		    SleepUtil.sleepVerySmall();

	    } catch (WebDriverException ex) {
	    	throw new HarnessException("Unable to mouseDownAt on locator " + locator, ex);
	    }
	}


	public void sMouseDownRightAt(String locator, String coordString, WebElement... elements) throws HarnessException {
	    logger.info("mouseDownRightAt(" + locator + ",'" + coordString + "')");

	    try {

		    WebElement we = null;
		    if (elements != null && elements.length > 0) {
		    	we = elements[0];
		    } else {
		    	we = getElement(locator);
		    }
		    Actions action = new Actions(webDriver());
		    action.moveToElement(we,1,1).contextClick(we).build().perform();
		    SleepUtil.sleepVerySmall();

	    } catch (WebDriverException ex) {
	    	throw new HarnessException("Unable to mouseDownRightAt on locator " + locator, ex);
	    }
	}


	public void sMouseUpRightAt(String locator, String coordString, WebElement... elements) throws HarnessException {
	    logger.info("mouseUpRightAt(" + locator + ",'" + coordString + "')");

	    try {
		    WebElement we = null;
		    if (elements != null && elements.length > 0) {
		    	we = elements[0];
		    } else {
		    	we = getElement(locator);
		    }
		    Actions action = new Actions(webDriver());
		    action.moveToElement(we,0,0).release(we).build().perform();
		    SleepUtil.sleepVerySmall();

	    } catch (WebDriverException ex) {
	    	throw new HarnessException("Unable to mouseUpRightAt on locator " + locator, ex);
	    }
	}


	public void sMouseOver(String locator, WebElement... elements) throws HarnessException {
	    logger.info("mouseOver(" + locator + ")");
	    try {
    	    WebElement we = null;
		    if (elements != null && elements.length > 0) {
		    	we = elements[0];
		    } else {
		    	we = getElement(locator);
		    }
    	    Actions action = new Actions(webDriver());
    	    action.moveToElement(we).build().perform();
    	    SleepUtil.sleepVerySmall();

	    } catch (WebDriverException ex) {
	    	throw new HarnessException("Unable to mouseOver on locator " + locator, ex);
	    }
	}


	public void sMouseOut(String locator, WebElement... elements) throws HarnessException {
	    logger.info("mouseOut(" + locator + ")");

	    try {
		    logger.info("action.clickAndHold.moveByOffset()");

		    WebElement we = null;
		    if (elements != null && elements.length > 0) {
		    	we = elements[0];
		    } else {
		    	we = getElement(locator);
		    }
		    Actions action = new Actions(webDriver());
		    action.clickAndHold(we).moveByOffset(1,1).build().perform();
		    SleepUtil.sleepVerySmall();

	    } catch (WebDriverException ex) {
	    	throw new HarnessException("Unable to mouseOut on locator " + locator, ex);
	    }
	}


	public void sMouseUp(String locator) throws HarnessException {
	    logger.info("mouseUp(" + locator + ")");

	    try {
		    logger.info("MouseUp()");
		    Coordinates co =  ((Locatable)getElement(locator)).getCoordinates();
		    Mouse mouse = ((HasInputDevices)webDriver()).getMouse();
		    mouse.mouseUp(co);
		    SleepUtil.sleepVerySmall();

	    } catch (WebDriverException ex) {
	    	throw new HarnessException("Unable to mouseUp on locator " + locator, ex);
	    }
	}


	public void sMouseMoveAt(String locator, String coordString, WebElement... elements) throws HarnessException {
	    logger.info("mouseMoveAt(" + locator + ",'" + coordString + "')");

	    try {
		    WebElement we = null;
		    if (elements != null && elements.length > 0) {
		    	we = elements[0];
		    } else {
		    	we = getElement(locator);
		    }

		    Actions action = new Actions(webDriver());
		    action.moveToElement(we,0,0).build().perform();
		    SleepUtil.sleepVerySmall();

	    } catch (WebDriverException ex) {
	    	throw new HarnessException("Unable to mouseMoveAt on locator " + locator, ex);
	    }
	}


	public void sMouseMove(String locator, WebElement... elements) throws HarnessException {
	    logger.info("mouseMove(" + locator + ")");

	    try {
		    WebElement we = null;
		    if (elements != null && elements.length > 0) {
		    	we = elements[0];
		    } else {
		    	we = getElement(locator);
		    }
		    Actions action = new Actions(webDriver());
		    action.moveToElement(we).build().perform();
		    SleepUtil.sleepVerySmall();

	    } catch(Exception ex) {
	    	throw new HarnessException("Unable to mouseMove on locator " + locator, ex);
	    }
	}


	public void sMouseUpAt(String locator, String coordString) throws HarnessException {
	    logger.info("mouseUpAt(" + locator + ",'" + coordString + ")'");

	    try {
		    logger.info("mouseMove.MouseUp()");
		    Coordinates co =  ((RemoteWebElement)getElement(locator)).getCoordinates();
		    Mouse mouse = ((HasInputDevices)webDriver()).getMouse();
		    mouse.mouseMove(co,0,0);
		    mouse.mouseUp(co);
		    SleepUtil.sleepVerySmall();

	    } catch (WebDriverException ex) {
	    	throw new HarnessException("Unable to mouseUpAt on locator " + locator, ex);
	    }
	}


	public void sMouseDownRight(String locator, WebElement... elements) throws HarnessException {
	    logger.info("mouseDownRight(" + locator + ")");

	    try {
		    logger.info("action.contextClick()");

		    WebElement we = null;
		    if (elements != null && elements.length > 0) {
		    	we = elements[0];
		    } else {
		    	we = getElement(locator);
		    }
		    Actions action = new Actions(webDriver());
		    action.contextClick(we).build().perform();
		    SleepUtil.sleepVerySmall();

	    } catch (WebDriverException ex) {
	    	throw new HarnessException("Unable to mouseDownRight on locator " + locator, ex);
	    }
	}


	public void sMouseUpRight(String locator, WebElement... elements) throws HarnessException {
	    logger.info("mouseUpRight(" + locator + ")");

	    try {
		    WebElement we = null;
		    if (elements != null && elements.length > 0) {
		    	we = elements[0];
		    } else {
		    	we = getElement(locator);
		    }
		    Actions action = new Actions(webDriver());
		    action.release(we).build().perform();
		    SleepUtil.sleepVerySmall();

	    } catch (WebDriverException ex) {
	    	throw new HarnessException("Unable to mouseUpRight on locator " + locator, ex);
	    }
	}


	public void sFocus(String locator, WebElement... elements) throws HarnessException {
	    logger.info("focus(" + locator + ")");

	    try {
			WebElement we = null;
			if (elements != null && elements.length > 0) {
			    we = elements[0];
			} else {
			    we = getElement(locator);
			}
			Capabilities cp =  ((RemoteWebDriver)webDriver()).getCapabilities();
			if (cp.getBrowserName().equals(DesiredCapabilities.firefox().getBrowserName())||cp.getBrowserName().equals(DesiredCapabilities.chrome().getBrowserName())||cp.getBrowserName().equals(DesiredCapabilities.internetExplorer().getBrowserName())) {
				executeScript("arguments[0].focus();", we);
			}

	    } catch (WebDriverException ex) {
	    	throw new HarnessException("Unable to focus on locator " + locator, ex);
	    }
	}


	public boolean sIsElementPresent(String locator) throws HarnessException {
		boolean present;
		if (locator.startsWith("//") || locator.startsWith("xpath")) {
			logger.warn("FIXME: the locator " + locator + " is a xpath - should change to css");
		}
		present = elementPresent(locator);
		logger.info("sIsElementPresent(" + present + ")");

		return (present);
	}


	public void sRefresh() throws HarnessException {
	    logger.info("refresh()");
		webDriver().navigate().refresh();
		if (ConfigProperties.getStringProperty("server.host").contains(ConfigProperties.getStringProperty("usLabDomain"))
				|| ConfigProperties.getStringProperty("server.host").contains(ConfigProperties.getStringProperty("indiaLabDomain"))) {
			zWaitTillElementPresent(PageMail.Locators.zMailTagsPane);
		} else {
			zWaitTillElementPresent(PageMail.Locators.zMailZimletsPane);
		}
		SleepUtil.sleepSmall();
	}


	public int sGetXpathCount(String xpath) throws HarnessException {
		int count = 0;
		count = getElements(By.xpath(xpath)).size();
		logger.info("getXpathCount(" + xpath + ") = " + count);
		return (count);
	}


	public int sGetCssCount(String css) throws HarnessException {
		int count = 0;
		count = getElements(By.cssSelector(getCssLocator(css).getLocator())).size();
		logger.info("getCssCount(" + css + ") = " + count);
		return (count);
	}


	public List<String> sGetAllWindowTitles() throws HarnessException {
		logger.info("getAllWindowTitles()");
		List<String> list = null;
		list =  getAllWindowNames();
		return list;
	}


	public List<String> sGetAllWindowIds() throws HarnessException {
		logger.info("getAllWindowIds()");
		List<String> list = null;
		list = new ArrayList<String>(webDriver().getWindowHandles());
		return list;
	}


	public List<String> sGetAllWindowNames() throws HarnessException {
		logger.info("getAllWindowNames()");
		List<String> list = null;
		list =  getAllWindowNames();
		return list;
	}


	public String sGetAttribute(String locator) throws WebDriverException {

		try {
		    String attrs = null;

			if (locator!=null && locator.lastIndexOf('@') + 1 < locator.length()) {
			    String elementLocator = locator.substring(0, locator.lastIndexOf('@'));
			    if (elementLocator.length() > 1) {
					try {
						if (elementLocator.endsWith(")")) {
							elementLocator = elementLocator.substring(0, elementLocator.length()-1);
						}
						if (elementLocator.startsWith("xpath=(")) {
							elementLocator = elementLocator.substring(7,elementLocator.length());
						}
					    WebElement we = getElement(elementLocator);
					    attrs = we.getAttribute(locator.substring(locator.lastIndexOf('@')+1));
					} catch (Exception ex) {
					    logger.error(ex);
					}
			    }
			}

		    logger.info("getAttribute(" + locator + ") = " + attrs);
		    return (attrs);

		} catch (WebDriverException e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
	}


	public String sGetCssValue(String locator) throws WebDriverException {

		try {
		    String attrs = null;

			if (locator!=null && locator.lastIndexOf('@') + 1 < locator.length()) {
			    String elementLocator = locator.substring(0, locator.lastIndexOf('@'));
			    if (elementLocator.length() > 1) {
					try {
						if (elementLocator.endsWith(")")) {
							elementLocator = elementLocator.substring(0, elementLocator.length()-1);
						}
						if (elementLocator.startsWith("xpath=(")) {
							elementLocator = elementLocator.substring(7,elementLocator.length());
						}
					    WebElement we = getElement(elementLocator);
					    attrs = we.getCssValue(locator.substring(locator.lastIndexOf('@')+1));
					} catch (Exception ex) {
					    logger.error(ex);
					}
			    }
			}

		    logger.info("getCssValue(" + locator + ") = " + attrs);
		    return (attrs);

		} catch (WebDriverException e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
	}


	public boolean sIsVisible(String locator) throws HarnessException {
		boolean visible = false;
	    visible = elementVisible(locator);
	    logger.info("isVisible(" + locator + ") = " + visible);
		return (visible);
	}


	public boolean zIsBusyOverlay() throws HarnessException {
		boolean isBusyOverlay = true;
		isBusyOverlay = Boolean.parseBoolean(executeScript("return top.appCtxt.getShell().getBusy()==true"));
		logger.info("isBusyOverlay(" + ") = " + isBusyOverlay);
		return (isBusyOverlay);
	}


	public void zWaitForBusyOverlay() throws HarnessException {
		logger.info("zWaitForBusyOverlay()");
		try {
			sWaitForCondition("return top.appCtxt.getShell().getBusy()==false");
		} catch (Exception ex) {
			throw new HarnessException("Busy Overlay never disappeared!", ex);
		}
	}


	public void zWaitForBusyOverlayHTML() throws HarnessException {
		logger.info("zWaitForBusyOverlayHTML()");
		sWaitForCondition("return top.appCtxt.getShell().getBusy()==false");
	}


	protected boolean sWaitForCondition(String condition) throws HarnessException {
		logger.info("sWaitForCondition(" + condition + "), timeout=" + LoadDelay);
		try {
			boolean result = false;
			final String script = condition;
			result = (new WebDriverWait(webDriver(), LoadDelay/SleepUtil.SleepGranularity)).until(new ExpectedCondition<Boolean>() {
						public Boolean apply(WebDriver d) {
							if (d==null) {
								return false;
							} else {
								return (Boolean) ((JavascriptExecutor) d).executeScript(script);
							}
						}
					});
			return result;
		} catch (Exception ex) {
			logger.info(condition + " never become true: " + ex);
			return false;
		}
	}


	public boolean sWaitForCondition(String condition, String timeout) throws HarnessException {
		logger.info("sWaitForCondition(" + condition + "), timeout=" + timeout);
		try {
			boolean result = false;
			final String script = condition;
			result = (new WebDriverWait(webDriver(), Long.valueOf(timeout)/SleepUtil.SleepGranularity))
					.until(new ExpectedCondition<Boolean>() {
						public Boolean apply(WebDriver d) {
							if (d==null) {
								return false;
							} else {
								return (Boolean) ((JavascriptExecutor) d)
									.executeScript(script);
							}
						}
					});
			return result;
		} catch (Exception ex) {
			logger.info(condition + " never become true: " + ex);
			return false;
		}
	}


	public boolean zWaitForElementPresent(String locator) throws HarnessException {
		logger.info("zWaitForElementPresent(" + locator + ")");
		return waitForElementPresent(locator, true, 10);

	}


	public boolean zWaitTillElementPresent(String locator) throws HarnessException {
		logger.info("zWaitTillElementPresent(" + locator + ")");
		this.zWaitForBusyOverlay();

		boolean present = false;
        for (int i=0; i<=10; i++) {
        	present = zIsVisiblePerPosition(locator, 10, 10);
            if (present == true) {
                SleepUtil.sleepSmall();
                return true;
            } else {
                SleepUtil.sleepMedium();
                if (i == 10) {
                    return false;
                }
            }
        }
        if (present == false) {
        	throw new HarnessException(locator + " not present in the UI");
        }
        return present;
    }


	public boolean zWaitForElementPresent(String locator, String timeout) throws HarnessException {
		logger.info("zWaitForElementPresent(" + locator + ", " + timeout +")");
		return waitForElementPresent(locator, true, Long.valueOf(timeout)/SleepUtil.SleepGranularity);
	}


	public void zWaitForElementDeleted(String locator) throws HarnessException {
		logger.info("zWaitForElementDeleted(" + locator + ")");
		try {
			waitForElementPresent(locator, false, 10);
		} catch (Exception ex) {
			throw new HarnessException(locator + " never disappeared : ", ex);
		}
	}


	public boolean zWaitForElementDeleted(String locator, String timeout) throws HarnessException {
		logger.info("zWaitForElementDeleted(" + locator + ", " + timeout +")");
		return waitForElementPresent(locator, false, Long.valueOf(timeout)/SleepUtil.SleepGranularity);
	}


	public boolean zIsElementDisabled(String cssLocator) throws HarnessException {
		String locator = (cssLocator.startsWith("css=") ? "" : "css=") + cssLocator + "[class*=ZDisabled]";
		logger.info("zIsElementDisabled(" + locator + ")");
		return sIsElementPresent(locator);
	}


	public void zWaitForElementEnabled(String cssLocator) throws HarnessException {
		logger.info("zWaitForElementEnabled(" + cssLocator + ")");
		for (int i = 0; i < 15; i++) {
			if (!zIsElementDisabled(cssLocator)) {
				return;
			}
			SleepUtil.sleepSmall();
		}
		throw new HarnessException("Element " + cssLocator + " never become enabled: ");
	}


	public void zWaitForElementVisible(String locator) throws HarnessException {
		logger.info("zWaitForElementVisible(" + locator + ")");
	    for (int i = 0; i < 15; i++) {
			if (zIsVisiblePerPosition(locator, 0, 0)) {
				return;
			}
			SleepUtil.sleepSmall();
		}
		throw new HarnessException(locator + " - never visibled!");
	}


	public void zWaitForElementInvisible(String locator) throws HarnessException {
		logger.info("zWaitForElementInvisible(" + locator + ")");
		if (waitForElementVisible(locator, false, 15)) {
			return;
		}
		throw new HarnessException(locator + "never invisible!");
	}


	public void zWaitForWindow(String name) throws HarnessException {
		logger.info("zWaitForWindow(" + name + ")");
		waitForWindowOpen(name, 60L);
	}


	public boolean zWaitForIframeText(String iframe, final String text) throws HarnessException {
		logger.info("zWaitForIframeText(" + iframe + ", " + text + ")");
		Boolean result = false;
		try {
			final WebElement we = getElement(iframe);
			ExpectedCondition<Boolean> ec = new ExpectedCondition<Boolean>() {
				public Boolean apply(WebDriver driver) {
					String result =  executeScript("var iframe = arguments[0];"
							+ "var iframe_body = "
							+ "iframe.contentWindow.document.body;"
							+ "if (navigator.userAgent.indexOf('MSIE')!=-1) {"
							+ "var result = iframe_body.innerHTML.indexOf('"
							+ text
							+ "') >= 0} else {"
							+ "var result = iframe_body.textContent.indexOf('"
							+ text
							+ "') >= 0} return result",we);
					if (result == null) {
						return false;
					} else {
						return Boolean.valueOf(result);
					}
				}
			};
			result =  waitForCondition(ec,10);
			return result;
		} catch (Exception ex) {
			throw new HarnessException(iframe + " never opened : ", ex);
		}
	}


	public boolean zIsWindowOpen(String name) throws HarnessException {
		logger.info("zIsWindowOpen(" + name + ")");
		return isWindowOpen(name);
	}


	public boolean zWaitForWindowClosed(String name) throws HarnessException {
		logger.info("zWaitForWindowClosed(" + name + ")");
		boolean result = false;
		try {
			result = waitForWindowClosed(name, 5L, webDriver().getWindowHandles().size());
			return result;
		} catch (Exception ex) {
			logger.info("window not found " + name, ex);
			return result;
		}
	}


	public void sCheck(String locator) throws HarnessException {
		logger.info("check(" + locator + ")");
		WebElement we = getElement(locator);
		if (!we.isSelected()) {
			we.click();
	    }
	}


	public void sUncheck(String locator) throws HarnessException {
		logger.info("uncheck(" + locator + ")");
		WebElement we = getElement(locator);
		if (we.isSelected()) {
			we.click();
		}
	}


	public boolean sIsChecked(String locator, WebElement... elements) throws HarnessException {
		boolean checked = false;
		WebElement we = null;
		if (elements != null && elements.length > 0) {
		    we = elements[0];
		} else {
		    we = getElement(locator);
		}

		checked = we.isSelected();
		logger.info("isChecked(" + locator + ") = " + checked);
		return (checked);
	}


	public String sGetText(String locator, WebElement... elements) throws HarnessException {
		try {
			String text = null;
			WebElement we = null;
			if (elements != null && elements.length > 0) {
			    we = elements[0];
			} else {
			    we = getElement(locator);
			}

			text = we.getText();
			logger.info("getText(" + locator + ") = " + text);
			return (text);
		} catch (WebDriverException e) {
			throw new HarnessException(e);
		}
	}


	public String sGetValue(String locator, WebElement... elements) throws HarnessException {
		String text = null;
		WebElement we = null;
		if (elements != null && elements.length > 0) {
		    we = elements[0];
		} else {
		    we = getElement(locator);
		}
		text = we.getAttribute("value");

		logger.info("getValue(" + locator + ") = " + text);
		return (text);
	}


	public String sGetShortcutsDialogBody(String locator) throws HarnessException {
		String text;
		WebElement el = getElement(locator);
		text = el.getText();
		logger.info("sGetBodyText() = " + text);
		return text;
	}


	public String sGetBodyText() throws HarnessException {
		String text;
		WebElement el = getElement("css=body");
		text = el.getText();
		logger.info("sGetBodyText() = " + text);
		return text;
	}


	public String sGetTitle() throws HarnessException {
		String text = null;
		text = webDriver().getTitle();
		logger.info("getTitle() = " + text);
		return text;
	}


	public void sType(String locator, String text, WebElement... elements) throws HarnessException {
		try {
			logger.info("type(" + locator + ", " + text + ")");

		    WebElement we = null;
		    if (elements != null && elements.length > 0) {
		    	we = elements[0];
		    } else {
		    	we = getElement(locator);
		    }
		    we.clear();
		    we.sendKeys(text);
		    SleepUtil.sleepVerySmall();

		} catch (WebDriverException e) {
			throw new HarnessException(e);
		}
	}


	public void sTypeDateTime(String locator, String text, WebElement... elements) throws HarnessException {
		try {
			logger.info("type(" + locator + ", " + text + ")");

		    WebElement we = null;
		    if (elements != null && elements.length > 0) {
		    	we = elements[0];
		    } else {
		    	we = getElement(locator);
		    }
		    we.sendKeys(text);
		    SleepUtil.sleepVerySmall();
			
		} catch (WebDriverException e) {
			throw new HarnessException(e);
		}
	}


	public void sTypeKeys(String locator, String text, WebElement... elements) throws HarnessException {
		try {
			logger.info("typeKeys(" + locator + ", " + text + ")");
	
			WebElement we = null;
			if (elements != null && elements.length > 0) {
			    we = elements[0];
			} else {
			    we = getElement(locator);
			}
	
			final Actions builder = new Actions(webDriver());
			final Action action = builder.sendKeys(we,text).build();
			action.perform();
			SleepUtil.sleepVerySmall();
			
		} catch (WebDriverException e) {
			throw new HarnessException(e);
		}
	}


	public String sGetConfirmation() throws HarnessException {
		String confirm = null;
		logger.info("getConfirmation()");
		confirm = webDriver().switchTo().alert().getText();
		return confirm;
	}


	public void sKeyPressNative(String code) throws HarnessException {
		logger.info("keyPressNative(" + code + ")");
		Actions builder = new Actions(webDriver());
		builder.sendKeys(code).build().perform();
		SleepUtil.sleepVerySmall();
	}


	public void sKeyPress(String locator, String code) throws HarnessException {
		logger.info("keypress(" + code + ")");
		Actions builder = new Actions(webDriver());
		builder.sendKeys(getElement(locator), code).build().perform();
		SleepUtil.sleepVerySmall();
	}


	public void sKeyDown(String locator, String code) throws HarnessException {
		logger.info("keyDown(" + code + ")");
		Actions builder = new Actions(webDriver());
		builder.keyDown(getElement(locator),Keys.valueOf(code)).build().perform();
		SleepUtil.sleepVerySmall();
	}


	public void sKeyUp(String locator, String code) throws HarnessException {
		logger.info("keyUp(" + code + ")");
		Actions builder = new Actions(webDriver());
		builder.keyUp(getElement(locator),Keys.valueOf(code)).build().perform();
		SleepUtil.sleepVerySmall();
	}


	public void sKeyDownNative(String code) throws HarnessException {
		logger.info("keyDownNative(" + code + ")");
		Actions builder = new Actions(webDriver());
		builder.sendKeys(code).build().perform();
		SleepUtil.sleepVerySmall();
	}


	public void sKeyUpNative(String code) throws HarnessException {
		logger.info("keyUpNative(" + code + ")");
		Actions builder = new Actions(webDriver());
		builder.release().build().perform();
		SleepUtil.sleepVerySmall();
	}


	public void sSelectDropDown(String selectLocator, String optionLocator) throws HarnessException {
		logger.info("sSelectDropDown(" + selectLocator + ", " + optionLocator + ")");
		Select select =  new Select (getElement(selectLocator));
		String option = optionLocator;
		if (option.contains("value=")) {
			option = option.split("value=")[1];
		}
		select.selectByValue(option);
	}


	public void sSelectFrame(String locator) throws HarnessException {

		try {
			if (locator.contains("relative=top")) {
				webDriver().switchTo().defaultContent();
			} else {
				webDriver().switchTo().frame(getElement(locator));
			}
			logger.info("sSelectFrame(" + locator + ")");
		} catch (WebDriverException e) {
			throw new HarnessException(e); // In case the frame doesn't exist
		}
	}


	public void sSelectWindow(String windowID) throws HarnessException {
		logger.info("sSelectWindow(" + windowID + ")");
		switchTo(windowID);
	}


	public void sDeleteAllVisibleCookies() {
		logger.info("sDeleteAllVisibleCookies()");
		webDriver().manage().deleteAllCookies();
	}


	public void sDeleteCookie(String name, String optionString) {
		logger.info("sDeleteCookie("+ name +", "+ optionString +")");
		webDriver().manage().deleteCookieNamed(name);
	}


	public void sOpen(String url) throws HarnessException {
		logger.info("open(" + url + ")");
		webDriver().navigate().to(url);
	}


	public void sOpenWindow(String url, String windowID) throws HarnessException {
		logger.info("openWindow(" + url + ", " + windowID + ")");
		webDriver().navigate().to(url);
	}


	public void sWaitForPopUp(String windowID, String timeout) throws HarnessException {
		logger.info("sWaitForPopUp(" + windowID + ")");
		waitForWindowOpen(windowID,Long.valueOf(timeout)/SleepUtil.SleepGranularity);
	}


	public void sWindowFocus() throws HarnessException {
		logger.info("sWindowFocus()");
		executeScript("window.focus()");
	}


	public void sWindowMaximize() throws HarnessException {
		logger.info("sWindowMaximize()");
		webDriver().manage().window().setPosition(new Point(0, 0));
		webDriver().manage().window().setSize(new Dimension((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth(),(int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()));
	}


	public String sGetLocation() throws HarnessException {
		String url = null;
		logger.info("sGetLocation(): " + url);
		url = webDriver().getCurrentUrl();
		return url;
	}


	public String sGetWindowURL(String title) throws HarnessException {

		String url = null;
		Set <String> windows = webDriver().getWindowHandles();
		String mainwindow = webDriver().getWindowHandle();

		try {
			for (String handle: windows) {
				webDriver().switchTo().window(handle);
				logger.info("Switching to " + webDriver().getTitle());
	            if (webDriver().getTitle().equalsIgnoreCase(title)) {
	            	url = webDriver().getCurrentUrl();
	            	logger.info("Closing '" + webDriver().getTitle() + ", URL: " + url);
	            	webDriver().close();
	            }
	        }
		} finally {
			webDriver().switchTo().window(mainwindow);
		}
		return url;
	}


	public void zDragAndDropBy(String locatorSource, String locatorDestination, int xOffset, int yOffset) throws HarnessException {

		if ( !this.sIsElementPresent(locatorSource) ) {
			throw new HarnessException("locator (source) cannot be found: "+ locatorSource);
		}

		if ( !this.sIsElementPresent(locatorDestination) ) {
			throw new HarnessException("locator (destination) cannot be found: "+ locatorDestination);
		}

		SleepUtil.sleepLong();

		Coordinate source = new Coordinate(this.sGetElementPositionLeft(locatorSource), this.sGetElementPositionTop(locatorSource));
		Coordinate destination = new Coordinate(this.sGetElementPositionLeft(locatorDestination), this.sGetElementPositionTop(locatorDestination));
		Coordinate relative = new Coordinate((destination.X - source.X) + xOffset, (destination.Y - source.Y) + yOffset);

		logger.info("x,y coordinate of the objectToBeDragged=" + source);
		logger.info("x,y coordinate of the objectToBeDroppedInto=" + destination);
		logger.info("xOffset,yOffset =" + xOffset + "," + yOffset);
		logger.info("x,y coordinate of the objectToBeDroppedInto relative to objectToBeDragged + offset = " + relative);

	    WebElement sourceElement = getElement(locatorSource);
	    WebElement destinationElement = getElement(locatorDestination);

	    //(new Actions(webDriver())).dragAndDropBy(sourceElement,relative.X,relative.Y).build().perform();
	    (new Actions(webDriver())).clickAndHold(sourceElement).moveToElement(destinationElement,xOffset,yOffset).build().perform();
	    (new Actions(webDriver())).release(sourceElement).build().perform();

		SleepUtil.sleepLong();

		this.zWaitForBusyOverlay();
	}


	protected boolean zWaitForElementVisible(String locator, Boolean flag, String timeout) throws HarnessException {
		logger.info("zWaitForElementVisible(" + locator + ", " + timeout +")");
		Long wait = Long.valueOf(timeout)/SleepUtil.SleepGranularity;
		if (waitForElementVisible(locator, flag, wait)) {
			return true;
		}
		throw new HarnessException(locator + " - wait for visisble timed out after " + wait + "s");
	}


	protected void typeKeys(Keys keys, String... locators) throws HarnessException {
	    if (locators != null && locators.length > 0) {
			logger.info("typeKeys(" + keys + ")");
			for(String locator : locators) {
			    sendKeys(locator, keys);
			}
	    } else {
			logger.info("actions.sendKeys(Keys)");
			Actions builder = new Actions(webDriver());
			builder.sendKeys(keys).build().perform();
	    }
	}


	private void sendKeys(String locator, CharSequence keyValues, WebElement... elements) throws HarnessException {
		logger.info("sendKeys(" + keyValues + ")");
		WebElement we = null;
		    if (elements != null && elements.length > 0) {
		    	we = elements[0];
		    } else {
		    	we = getElement(locator);
		    }

		we.sendKeys(keyValues);
	}


	protected void clearField(String locator) throws HarnessException{
		logger.info("clear(" + locator + ")");
		WebElement we = getElement(locator);
		we.clear();
	}


	protected String executeScript(String script, Object... arg) {
		logger.info("executeScript(" + script + ")");
		String value = null;
		try {
			if (script.equals("window.appCtxt.getCurrentViewType()")) {
				script = "return window.appCtxt.getCurrentViewType()";
			}
			Object ob = ((JavascriptExecutor) webDriver()).executeScript(script, arg);

			if (ob != null) {
				value = ob.toString();
			}
			return (value);

		} catch (Exception e) {
			logger.info(e + " Exception, executing " + script);
			return value;
		}
	}


	private static final class CssLocator {
		private String locator;
		private String text;
		private String preText;
		private String postText;

		private String getLocator() {
			return locator;
		}

		private void setLocator(String str) {
			locator = str;
		}

		private String getPreText() {
			return preText;
		}

		private void setPreText(String str) {
			preText = str;
		}

		private String getPostText() {
			return postText;
		}

		private void setPostText(String str) {
			postText = str;
		}
		private String getText() {
			return text;
		}

		private void setText(String txt) {
			text = txt;
		}
	}


	private WebElement findBy(By... bys) {
		WebElement we = null;
		if (bys != null) {
			for(By by:bys) {
				try {
					if (we == null) {
						we = webDriver().findElement(by);
					} else {
						we = we.findElement(by);
					}
				} catch(Exception ex) {
					logger.info("findBy()" + ex);
				}
			}
		}
		return we;
	}


	protected void clickBy(By... bys) {
		findBy(bys).click();
	}


	private CssLocator configureCssLocator(String locator, String startSuffix, String containSuffix) {
		String modLocator = locator;
		String preText = "";
		String text = "";
		String postText = "";
		CssLocator cssl = new CssLocator();

		if (modLocator!= null) {
			if (modLocator.startsWith(startSuffix)) {
				modLocator = modLocator.substring(startSuffix.length());
			}

			if (modLocator.contains(containSuffix)) {
				String[] tokens = modLocator.split(containSuffix);
				preText = tokens[0];
				if (tokens.length > 1) {
					if (tokens[1].startsWith("(")&& tokens[1].contains(")")) {
						text = tokens[1].substring(tokens[1].indexOf('(') + 1, tokens[1].lastIndexOf(')'));
						if (text.startsWith("'")&& text.endsWith("'")) {
							text = text.substring(text.indexOf('\'') + 1, text.lastIndexOf('\''));
						}
					}
					if (tokens[1].length()> tokens[1].lastIndexOf(')')) {
						postText = tokens[1].substring(tokens[1].lastIndexOf(')') + 1);
					}
				}
				modLocator = preText + postText;
			}
		}

		cssl.setPreText(preText);
		cssl.setText(text);
		cssl.setPostText(postText);
		cssl.setLocator(modLocator);
		return cssl;
	}


	public CssLocator getCssLocator(String locator) {
		logger.info("getCssLocator(" + locator + ")");
		return configureCssLocator(locator, "css=", ":contains");
	}


	private List<WebElement> getElements(By by) {
		logger.info("getElements()");
		return webDriver().findElements(by);
	}


	protected WebElement getElement(String locator) throws HarnessException{
		logger.info("getElement(" + locator + ")");
		WebElement we = getElementOrNull(locator);

		if (we==null) {
			throw new HarnessException("WebElement is null: " + locator );
		} else {
			return we;
		}
	}


	private WebElement getElementByXPath(String locator) {
		WebElement element = null;
		WebDriver driver = webDriver();
		if (locator != null) {
			try {
				element = driver.findElement(By.xpath(locator));
			} catch(Exception ex) {
				logger.info("getElementByXPath()" + ex);
			}
		}
		return element;
	}


	private WebElement getElementById(String locator) {
		String startSuffix = "id=";
		WebElement element = null;
		WebDriver driver = webDriver();
		String modifiedLocator = locator;
		if (modifiedLocator != null) {
			if ( modifiedLocator.startsWith(startSuffix)) {
				modifiedLocator = modifiedLocator.substring(startSuffix.length());
			}
			try {
				element = driver.findElement(By.id(modifiedLocator));
			} catch(Exception ex) {
				logger.info("getElementById()" + ex);
			}
		}
		return element;
	}


	private WebElement getElementByClassName(String locator) {
		String startSuffix = "class=";
		WebElement element = null;
		WebDriver driver = webDriver();
		String modifiedLocator = locator;
		if (modifiedLocator != null) {
			if ( modifiedLocator.startsWith(startSuffix)) {
				modifiedLocator = modifiedLocator.substring(startSuffix.length());
			}
			try {
				element = driver.findElement(By.className(modifiedLocator));
			} catch(Exception ex) {
				logger.info("getElementById()" + ex);
			}
		}
		return element;
	}


	private WebElement getElementByCss(String locator) {
		String startSuffix = "css=";
		String containSuffix = ":contains";
		WebElement we = null;
		WebDriver driver = null;
		CssLocator cssl = configureCssLocator(locator, startSuffix, containSuffix);
		driver = webDriver();
		String modLocator = cssl.getLocator();
		if (null != modLocator) {
			try {
				String txt = cssl.getText();

				if (txt == null || txt.isEmpty()) {
				    logger.info("findElement(By.cssSelector(" + modLocator + "))");
				    we = driver.findElement(By.cssSelector(modLocator));

				} else {
					String preText = cssl.getPreText();
					String postText = cssl.getPostText();
					logger.info("findElements(By.cssSelector("	+ preText + "))");

					List<WebElement> elements = driver.findElements(By.cssSelector(preText));
					Iterator<WebElement> it = elements.iterator();
					while (it.hasNext()) {
						WebElement el = it.next();
						String returnedText = el.getText();
						if (returnedText!=null && returnedText.contains(txt)) {
							logger.info("found element containing: " + txt);
							if (postText !=null && !postText.isEmpty()) {
								logger.info("applying filter: findElement(By.cssSelector(" + postText + "))");
								el = el.findElement(By.cssSelector(postText));
							}
							we = el;
							break;
						}
					}
				}
			} catch (Exception ex) {
				logger.info("getElementByCss()" + ex);
			}
		}
		logger.info("getElementByCss(" + we + ")");
		return we;
	}


	private WebElement getElementOrNull(String locator) {

		WebElement we = null;
		if (locator.startsWith("id=")) {
			logger.info("getElementById(" + locator + ")");
			we = getElementById(locator);

		} else if (locator.startsWith("class=")) {
			logger.info("getElementByClassName(" + locator + ")");
			we = getElementByClassName(locator);

		} else if (locator.startsWith("css=")) {
			we = getElementByCss(locator);

		} else if (locator.startsWith("//") || locator.startsWith("xpath=")) {
			logger.info("getElementByXPath(" + locator + ")");
			we = getElementByXPath(locator);

		} else {
			if (locator.contains("=")) {
				we = getElementByCss(locator);
			} else {
				logger.info("getElementById(" + locator + ")");
				we = getElementById(locator);
			}
		}
		return we;
	}


	private boolean elementPresent(String locator) {
		WebElement el = getElementOrNull(locator);
		return el != null;
	}


	private boolean elementVisible(String locator) {
		Boolean visible = false;
		WebElement we = getElementOrNull(locator);

		if ( we == null) {
		    logger.info("WebElement is null - " + locator );
		} else {
			visible = we.isDisplayed();
			logger.info("locator: " + locator +  " - visible : " + (visible));
		}

		return visible;
	}


	private boolean elementVisiblePerPosition(String locator) {
		logger.info("elementVisiblePerPosition(" + locator + ")");
		Boolean visible = false;
		WebElement we = getElementOrNull(locator);

		if ( we == null) {
			logger.info("WebElement is null - " + locator );
		} else {
			visible = we.isDisplayed();
			logger.info("locator: " + locator +  " - visible : " + (visible));
		}
		return visible;
	}


	private boolean waitForElementPresent(final String locator, final boolean flag, long timeout) {
		logger.info("waitForElementPresent(" + locator + ")");
		Boolean present = false;
		if (locator !=null && !locator.isEmpty()) {
			try {
				present = (new FluentWait<WebDriver>(webDriver()).withTimeout(timeout, TimeUnit.SECONDS).pollingEvery(500, TimeUnit.MILLISECONDS).ignoring(NoSuchElementException.class)).until(new ExpectedCondition<Boolean>() {
							public Boolean apply(WebDriver d) {
								if (flag) {
									return elementPresent(locator);
								} else {
									return !elementPresent(locator);
								}
							}
				});
			} catch (TimeoutException  e) {
				logger.info("waitForElementPresent() " + locator + " timed out after " + timeout + "s");
			}
		}
		return present;
	}


	private boolean waitForElementVisible(final String locator, final boolean flag , long timeout) {
		logger.info("waitForElementVisible(" + locator + ")");
		Boolean visible = false;
		if (locator !=null && !locator.isEmpty()) {
			try {
				visible = (new FluentWait<WebDriver>(webDriver()).withTimeout(timeout, TimeUnit.SECONDS).
					pollingEvery(500, TimeUnit.MILLISECONDS).ignoring(NoSuchElementException.class).ignoring(MoveTargetOutOfBoundsException.class).ignoring(ElementNotVisibleException.class)).until(new ExpectedCondition<Boolean>() {
							public Boolean apply(WebDriver d) {
								if (flag) {
									return elementVisiblePerPosition(locator);
								} else {
									return !elementVisiblePerPosition(locator);
								}
							}
				});
			} catch (TimeoutException  e) {
				logger.info("waitForElementVisible() " + locator + " timed out after " + timeout + "s");
			}
		}
		return visible;
	}


	private Boolean waitForCondition(ExpectedCondition<Boolean> condition, long timeout) {
		logger.info("waitForCondition()");
		WebDriverWait wait = new WebDriverWait(webDriver(), timeout);
		Boolean result = false;
		try {
			result = wait.until(condition);
		} catch (TimeoutException  e) {
			logger.info("waitForCondition() timed out after " + timeout + "s");
		}
		return result;
	}


	private List<String> getAllWindowNames() throws HarnessException{
		logger.info("getAllWindowNames()");

		List<String> list = new ArrayList<String>();
		WebDriver driver = webDriver();
		String currentWindowHandle = driver.getWindowHandle();
		try {
			Set<String> windowHandles = driver.getWindowHandles();
			if (windowHandles!=null && !windowHandles.isEmpty()) {
				for (String handle : windowHandles) {
					list.add(driver.switchTo().window(handle).getTitle());
					logger.info("Found window: " + driver.switchTo().window(handle).getTitle());
				}
			}
		} catch(Exception ex) {
			logger.error(ex);
		}
		finally {
		    String currentWindowName = driver.switchTo().window(currentWindowHandle).getTitle();
		    logger.info("Switching to window: " + currentWindowName);
		}
		return list;
	}


	protected boolean switchTo(String name) throws HarnessException {
		logger.info("switchTo(" + name + ")");
		WebDriver driver = webDriver();
		String defaultContent;
		boolean found = false;
		SleepUtil.sleepSmall();
		Set<String> handles = driver.getWindowHandles();
		if (handles == null) {
		    throw new HarnessException(" handles are null");
		}

		try {
		    if (name == null || name.contentEquals("null")) {
				LinkedHashSet<String> windowHandles = new LinkedHashSet<String>(handles);
				driver.switchTo().window(windowHandles.iterator().next());
				driver.switchTo().defaultContent();
				defaultContent = driver.switchTo().defaultContent().getTitle();
				logger.info("selecting defaultContent()" + defaultContent);
				sWindowFocus();
				found = true;
		    } else {
				logger.info("handles size: " + handles.size());
				String url = "";
				String windowName = null;
				for (String handle : handles) {
				    try {
						windowName = driver.switchTo().window(handle).getTitle();

						logger.info("Switched to window: " + windowName);
						url = driver.getCurrentUrl();

					    if (windowName != null && (windowName.contentEquals(name) || url.contains("/" + name + "?"))) {
							found = true;
							logger.info("Found window: " + windowName);
							break;
					    }

				    } catch(Exception ex) {
				    	logger.error(ex);
				    }
				}
		    }

		} catch(Exception ex) {
			logger.error(ex);

		} finally {
		    if (!found) {
				defaultContent = driver.switchTo().defaultContent().getTitle();
				logger.info("back to defaultContent()" + defaultContent);
				sWindowFocus();
		    }
		}
		return found;
	}


	private boolean isWindowOpen(String name) {
		logger.info("isWindowOpen(" + name + ")");

		boolean found = false;
		for (int i = 0; i < 5; i++) {
			try {
				found = switchTo(name);
			} catch(Exception ex) {
				logger.error(ex);
			}
			if (found) {
				break;
			}
			SleepUtil.sleepSmall();
		}
		return found;
	}


	private boolean waitForWindowClosed(final String name, Long timeout, int... handlesSize) {
		logger.info("waitForWindowClosed(" + name + ")");
		return waitForWindow(name, false, timeout, handlesSize);
	}


	private boolean waitForWindowOpen(final String name, Long timeout, int... handlesSize) {
		logger.info("waitForWindowOpen(" + name + ")");
		return waitForWindow(name, true, timeout, handlesSize);
	}


	private boolean waitForWindow(final String name, final Boolean flag, Long timeout, int... handlesSize) {
		logger.info("waitForWindow(" + name + ") ");

		Wait<WebDriver> wait = null;

		if (handlesSize != null && handlesSize.length > 0) {
			final int size = handlesSize[0];
			try {
				wait = new FluentWait<WebDriver>(webDriver()).withTimeout(5L, TimeUnit.SECONDS).pollingEvery(500, TimeUnit.MILLISECONDS);
				wait.until(new ExpectedCondition<Boolean>() {
					Boolean result = false;
					public Boolean apply(WebDriver driver) {
						if (flag) {
							result = driver.getWindowHandles().size() > size;
						} else {
							if (size > 1) {
								result = driver.getWindowHandles().size() < size;
							}
						}
						return result;
				}});
			} catch (Exception te) {
				logger.info("wait for getWindowHandles().size differ from " + size + " timed out");
			}
		}

		boolean status = false;
		wait = new FluentWait<WebDriver>(webDriver()).withTimeout(timeout, TimeUnit.SECONDS).pollingEvery(500, TimeUnit.MILLISECONDS);
		try {
			status = wait.until(new ExpectedCondition<Boolean>() {
				public Boolean apply(WebDriver driver) {
					try {
						if (flag) {
							return switchTo(name);
						} else {
							return !switchTo(name);
						}
					} catch (HarnessException ex) {
						logger.info(ex);
						return false;
					}
				}
			});
		} catch (Exception te) {
			logger.info("wait for window " + name + " become:" + flag + " timed out");
		}
		return status;
	}


	public void zScrollTo(int scrollTo) throws HarnessException {
		logger.info("scrollTo(" + scrollTo + ") ");
		JavascriptExecutor jse = (JavascriptExecutor)webDriver();
		jse.executeScript("window.scrollBy(0," + scrollTo + ")", "");
	}

}
