package framework.ui;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import framework.core.ClientSessionFactory;
import framework.util.HarnessException;

/**
 * This class defines a logical GUI object that accesses Selenium functions
 * 
 * The class has DefaultSelenium methods and Zimbra-specific GUI methods that are
 * common to multiple objects.
 * 
 * @author Matt Rhoades
 *
 */
public abstract class AbsSeleniumObject {
	protected static Logger logger = LogManager.getLogger(AbsSeleniumObject.class);

	public enum PopupButton {
		Yes, No, Cancel, Help
	}
	
	public enum ListNavButton {
		Previous, Next
	}
		
	public AbsSeleniumObject() {
		logger.info("new AbsSeleniumObject");
	}
	

	/**
	 * Zimbra: return if the specified element is visible per style coordinates
	 * @param locator
	 * @param leftLimit
	 * @param topLimit
	 * @return
	 */
	public boolean zIsVisiblePerPosition(String locator, int leftLimit, int topLimit) {
		
		// Find the current position
		Number left = ClientSessionFactory.session().selenium().getElementPositionLeft(locator);
		Number top = ClientSessionFactory.session().selenium().getElementPositionTop(locator);
		
		// If the position is less than the limits, then it is hidden
		boolean hidden = ( (left.intValue() < leftLimit) && (top.intValue() < topLimit) );
		logger.info("isVisiblePerPosition("+ locator +") - (left, top) = ("+ left.intValue() +", "+ top.intValue() +") (limit, limit) = ("+ leftLimit +", "+ topLimit +") "+ (!hidden));
		return (!hidden);
	}

	//// ***
	// Start: Selenium methods
	//// ***
	
	/**
	 * DefaultSelenium.getHtmlSource()
	 * @param locator
	 */
	public String sGetHtmlSource() throws HarnessException {
		String htmlString = ClientSessionFactory.session().selenium().getHtmlSource();
		logger.info("getHtmlSource()");
		return (htmlString);
	}
	
	/**
	 * DefaultSelenium.getSelectedId()
	 * @param locator
	 */
	public String sGetSelectedId(String locator) {
		String id = ClientSessionFactory.session().selenium().getSelectedId(locator);
		logger.info("getSelectedId(" + locator + ") = "+ id);
		return (id);
	}
	
	/**
	 * DefaultSelenium.chooseOkOnNextConfirmation()
	 */
	public void sChooseOkOnNextConfirmation() {
		ClientSessionFactory.session().selenium().chooseOkOnNextConfirmation();
		logger.info("chooseOkOnNextConfirmation()");
	}
	
	/**
	 * DefaultSelenium.click()
	 */
	public void sClick(String locator) {
		ClientSessionFactory.session().selenium().click(locator);
		logger.info("click(" + locator + ")");
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
		boolean present = ClientSessionFactory.session().selenium().isElementPresent(locator);
		logger.info("isElementPresent(" + locator + ") = " + present);
		return (present);
	}

	/**
	 * DefaultSelenium.getXpathCount()
	 */
	public int sGetXpathCount(String xpath) {
		int count = ClientSessionFactory.session().selenium().getXpathCount(xpath).intValue();
		logger.info("getXpathCount(" + xpath + ") = " + count);
		return (count);
	}

	/**
	 * DefaultSelenium.getAttribute()
	 */
	public String sGetAttribute(String locator) {
		String attrs = ClientSessionFactory.session().selenium().getAttribute(locator);
		logger.info("getAttribute(" + locator + ") = " + attrs);
		return (attrs);
	}

	/**
	 * DefaultSelenium.isVisible()
	 */
	public boolean sIsVisible(String locator) {
		boolean visible = ClientSessionFactory.session().selenium().isVisible(locator);
		logger.info("isVisible(" + locator + ") = " + visible);
		return (visible);
	}


	/**
	 * DefaultSelenium.getText()
	 */
	public String sGetText(String locator) {
		String text = ClientSessionFactory.session().selenium().getText(locator);
		logger.info("DefaultSelenium.getText(" + locator + ") = " + text);
		return (ClientSessionFactory.session().selenium().getText(locator));
	}
	
	/**
	 * DefaultSelenium.type()
	 */
	public void sType(String locator, String text) {
		ClientSessionFactory.session().selenium().type(locator, text);
		logger.info("type(" + locator + ", " + text + ")");
	}


	//// ***
	// End: Selenium methods
	//// ***

}
