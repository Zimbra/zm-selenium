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

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import org.apache.log4j.*;
import com.zimbra.qa.selenium.framework.util.*;

/**
 * A <code>AbsPage</code> object represents any of the GUI classes, such as
 * Dialogs, Tabs, Forms, etc.
 * <p>
 * Implementing AbsPage classes must define the {@link AbsPage#zIsActive()} and
 * {@link AbsPage#zNavigateTo()} methods.  The test method classes can set a
 * "startingapp", which the harness will attempt to navigate-to before running
 * each test method.
 * <p>
 * @author Matt Rhoades
 *
 */
public abstract class AbsPage extends AbsSeleniumObject {
	protected static Logger logger = LogManager.getLogger(AbsPage.class);

	protected static final int PageLoadDelay = 60000; // wait 60 seconds for pages to load
	protected AbsApplication MyApplication = null;
	public Keyboard zKeyboard = new Keyboard();
	public AbsPage(AbsApplication application) {
		MyApplication = application;
		logger.info("new "+ AbsPage.class.getCanonicalName());
	}

	public abstract String myPageName();
	public abstract boolean zIsActive() throws HarnessException;
	public void zWaitForActive() throws HarnessException {
		zWaitForActive(PageLoadDelay);
	}

	public void zWaitForActive(long millis) throws HarnessException {

		for (int time = 0; time <= millis; time += SleepUtil.SleepGranularity) {
			if ( zIsActive() ) {
				return; // Page became active
			}
			SleepUtil.sleep(SleepUtil.SleepGranularity);
		}

		throw new HarnessException("Page never became active: msec="+ millis);
	}

	public AbsTooltip zHoverOver(Button button) throws HarnessException {
		throw new HarnessException("implement me");
	}

	public void zDragAndDrop(String locatorSource, String locatorDestination) throws HarnessException {

		if ( !this.sIsElementPresent(locatorSource) ) {
			throw new HarnessException("locator (source) cannot be found: "+ locatorSource);
		}

		if ( !this.sIsElementPresent(locatorDestination) ) {
			throw new HarnessException("locator (destination) cannot be found: "+ locatorDestination);
		}

		SleepUtil.sleepMedium();

		/*

		// Get the coordinates for the locators
		Coordinate destination = new Coordinate(
				this.sGetElementPositionLeft(locatorDestination),
				this.sGetElementPositionTop(locatorDestination));

		Coordinate source = new Coordinate(
				this.sGetElementPositionLeft(locatorSource),
				this.sGetElementPositionTop(locatorSource));

		Coordinate relative = new Coordinate(
				destination.X - source.X,
				destination.Y - source.Y);

		logger.info("x,y coordinate of the objectToBeDroppedInto=" + destination);
		logger.info("x,y coordinate of the objectToBeDragged=" + source);
		logger.info("x,y coordinate of the objectToBeDroppedInto relative to objectToBeDragged = " + relative);

		// Hold the mouse down on the source
		this.sMouseDownAt(locatorSource, relative.toString());

		SleepUtil.sleep(1000);
		// Drag the mouse to the destination, plus the offset
		this.sMouseMoveAt(locatorDestination, relative.toString());

		// Wait a bit for things to happen
		SleepUtil.sleep(1000 * 3);

		this.sMouseMove(locatorDestination);
		this.sMouseOver(locatorDestination);

		SleepUtil.sleep(1000);
		// Release the mouse
		this.sMouseUpAt(locatorDestination, relative.toString());

		*/

		this.sMouseDownAt(locatorSource,"");
		SleepUtil.sleepMedium();

		// Drag the mouse to the destination, plus the offset
		this.sMouseMoveAt(locatorDestination,"");
		SleepUtil.sleepMedium();

		this.sMouseMoveAt(locatorDestination,"");
		this.sMouseOver(locatorDestination);
		SleepUtil.sleepMedium();

		// Release the mouse
		this.sMouseUpAt(locatorDestination,"");
		SleepUtil.sleepMedium();

		// Wait for the client to come back
		this.zWaitForBusyOverlay();

	}

	public void zMouseClick(int x, int y) throws HarnessException {
	   Mouse mouse = new Mouse();
	   mouse.leftClick(x, y);
	}

	public static class Mouse {
	   private static Logger logger = LogManager.getLogger(Mouse.class);
	   public Mouse() {
	      logger.info("new " + Mouse.class.getCanonicalName());
	   }

	   public void leftClick(int x, int y) throws HarnessException {
	      logger.info("leftClick(" + x + ", " + y + ")");
	      RobotMouse robotMouse = new RobotMouse();
	      robotMouse.click(x, y);
	   }

      private static class RobotMouse {
         private static Logger logger = LogManager.getLogger(RobotMouse.class);
         private Robot robot;

         public RobotMouse() throws HarnessException {
           logger.info("new " + RobotMouse.class.getCanonicalName());

           try {
              this.robot = new Robot();
           } catch (AWTException e) {
              throw new HarnessException(e);
           }
         }

         public void click(int x, int y) {
            logger.info("click(" + x + ", " + y + ")");
            robot.mouseMove(x, y);
            robot.mousePress(InputEvent.BUTTON1_MASK);
            robot.mouseRelease(InputEvent.BUTTON1_MASK);
          }
      }
	}

	public static class Keyboard {
		private static Logger logger = LogManager.getLogger(Keyboard.class);

		public Keyboard() {
			logger.info("new " + Keyboard.class.getCanonicalName());
		}

		public void zTypeKeyEvent(int keyEvent) throws HarnessException {
			SleepUtil.sleepSmall();
			logger.info("zTypeKeyEvent("+ keyEvent +")");
			RobotKeyboard keyboard = new RobotKeyboard();
			keyboard.doType(keyEvent);
			SleepUtil.sleepSmall();

		}

		public void zSelectAll() throws HarnessException {
			logger.info("zTypeKeyEvent(CTRL A)");
			RobotKeyboard keyboard = new RobotKeyboard();
			keyboard.robot.keyPress(KeyEvent.VK_CONTROL);
			keyboard.robot.keyPress(KeyEvent.VK_A);
			keyboard.robot.keyRelease(KeyEvent.VK_CONTROL);
			keyboard.robot.keyRelease(KeyEvent.VK_A);
		}

		public void zTypeCharacters(String chars) throws HarnessException {
			logger.info("zTypeCharacters("+ chars +")");
			SleepUtil.sleepSmall();
			RobotKeyboard keyboard = new RobotKeyboard();
			keyboard.type(chars);
			SleepUtil.sleepSmall();
		}

		public void zTypeCharactersUpload(String chars, String upload) throws HarnessException, InterruptedException {
			SleepUtil.sleepSmall();
			logger.info("zTypeCharacters("+ chars +")");
			RobotKeyboard keyboard = new RobotKeyboard();
			keyboard.typeUpload(chars, upload);
			SleepUtil.sleepSmall();
		}

		private static class RobotKeyboard {
			private static Logger logger = LogManager.getLogger(RobotKeyboard.class);

		    private Robot robot;

		    public RobotKeyboard() throws HarnessException {
				logger.info("new " + RobotKeyboard.class.getCanonicalName());

				try {
					this.robot = new Robot();
				} catch (AWTException e) {
					throw new HarnessException(e);
				}
		    }

		    private static boolean numLockHasBeenProcessed = false;

		    public void type(String characters) {
		    	logger.info("type("+ characters +")");
		    	if (characters.equals("<Delete>")) {
		    		doType(KeyEvent.VK_DELETE);
		    		
		    	} else if (characters.equals("<ESC>")) {
		    		doType(KeyEvent.VK_ESCAPE);
		    		
		    	} else if (characters.equals("<ENTER>")) {
		    		doType(KeyEvent.VK_ENTER);
		    		
			    } else if (characters.equals("<SHIFT><DEL>")) {
		    		if ( (!numLockHasBeenProcessed) && (Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_NUM_LOCK)) ) {
		    			logger.info("Setting KeyEvent.VK_NUM_LOCK=false");
		    			Toolkit.getDefaultToolkit().setLockingKeyState(KeyEvent.VK_NUM_LOCK, false);
		    			numLockHasBeenProcessed = true;
		    		}
		    		doType(KeyEvent.VK_SHIFT, KeyEvent.VK_DELETE);

		    	} else if (characters.equals("<SHIFT><DOWN>")) {
		    		if ( (!numLockHasBeenProcessed) && (Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_NUM_LOCK)) ) {
		    			logger.info("Setting KeyEvent.VK_NUM_LOCK=false");
		    			Toolkit.getDefaultToolkit().setLockingKeyState(KeyEvent.VK_NUM_LOCK, false);
		    			numLockHasBeenProcessed = true;
		    		}
		    		doType(KeyEvent.VK_SHIFT, KeyEvent.VK_DOWN);
		    		
		    	} else if (characters.equals("<CTRL><O>")) {
		    		if ( (!numLockHasBeenProcessed) && (Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_NUM_LOCK)) ) {
		    			logger.info("Setting KeyEvent.VK_NUM_LOCK=false");
		    			Toolkit.getDefaultToolkit().setLockingKeyState(KeyEvent.VK_NUM_LOCK, false);
		    			numLockHasBeenProcessed = true;
		    		}
		    		doType(KeyEvent.VK_CONTROL, KeyEvent.VK_O);

		    	} else {
		    	   for (char c : characters.toCharArray()) {
		    	      try {
		    	    	  Thread.sleep(100);
		    	    	  type(c);
		    	    	  Thread.sleep(100);
		    	      } catch (Exception e) {
		    	    	  logger.warn(e);
					}
		    	   }
		    	}

		    }

		    public void typeUpload(String characters, String upload) {

		    	logger.info("type("+ characters +")");
		    	try {
		    		Thread.sleep(500);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
		    	for (char c : characters.toCharArray()) {
	    	      try {
	    	    	  Thread.sleep(100);
	    	    	  type(c);
	    	    	  Thread.sleep(100);
	    	      } catch (Exception e) {
	    	    	  logger.warn(e);
	    	      }
				}

		    	RobotKeyboard keyboard;
				try {
					Thread.sleep(3000);
					keyboard = new RobotKeyboard();
					keyboard.robot.keyPress(KeyEvent.VK_ENTER);
					keyboard.robot.keyRelease(KeyEvent.VK_ENTER);
				} catch (HarnessException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

		    }

		    private void type(char character) {
		    	logger.info("type("+ character +")");

		        switch (character) {
		        case 'a': doType(KeyEvent.VK_A); break;
		        case 'b': doType(KeyEvent.VK_B); break;
		        case 'c': doType(KeyEvent.VK_C); break;
		        case 'd': doType(KeyEvent.VK_D); break;
		        case 'e': doType(KeyEvent.VK_E); break;
		        case 'f': doType(KeyEvent.VK_F); break;
		        case 'g': doType(KeyEvent.VK_G); break;
		        case 'h': doType(KeyEvent.VK_H); break;
		        case 'i': doType(KeyEvent.VK_I); break;
		        case 'j': doType(KeyEvent.VK_J); break;
		        case 'k': doType(KeyEvent.VK_K); break;
		        case 'l': doType(KeyEvent.VK_L); break;
		        case 'm': doType(KeyEvent.VK_M); break;
		        case 'n': doType(KeyEvent.VK_N); break;
		        case 'o': doType(KeyEvent.VK_O); break;
		        case 'p': doType(KeyEvent.VK_P); break;
		        case 'q': doType(KeyEvent.VK_Q); break;
		        case 'r': doType(KeyEvent.VK_R); break;
		        case 's': doType(KeyEvent.VK_S); break;
		        case 't': doType(KeyEvent.VK_T); break;
		        case 'u': doType(KeyEvent.VK_U); break;
		        case 'v': doType(KeyEvent.VK_V); break;
		        case 'w': doType(KeyEvent.VK_W); break;
		        case 'x': doType(KeyEvent.VK_X); break;
		        case 'y': doType(KeyEvent.VK_Y); break;
		        case 'z': doType(KeyEvent.VK_Z); break;
		        case 'A': doType(KeyEvent.VK_SHIFT, KeyEvent.VK_A); break;
		        case 'B': doType(KeyEvent.VK_SHIFT, KeyEvent.VK_B); break;
		        case 'C': doType(KeyEvent.VK_SHIFT, KeyEvent.VK_C); break;
		        case 'D': doType(KeyEvent.VK_SHIFT, KeyEvent.VK_D); break;
		        case 'E': doType(KeyEvent.VK_SHIFT, KeyEvent.VK_E); break;
		        case 'F': doType(KeyEvent.VK_SHIFT, KeyEvent.VK_F); break;
		        case 'G': doType(KeyEvent.VK_SHIFT, KeyEvent.VK_G); break;
		        case 'H': doType(KeyEvent.VK_SHIFT, KeyEvent.VK_H); break;
		        case 'I': doType(KeyEvent.VK_SHIFT, KeyEvent.VK_I); break;
		        case 'J': doType(KeyEvent.VK_SHIFT, KeyEvent.VK_J); break;
		        case 'K': doType(KeyEvent.VK_SHIFT, KeyEvent.VK_K); break;
		        case 'L': doType(KeyEvent.VK_SHIFT, KeyEvent.VK_L); break;
		        case 'M': doType(KeyEvent.VK_SHIFT, KeyEvent.VK_M); break;
		        case 'N': doType(KeyEvent.VK_SHIFT, KeyEvent.VK_N); break;
		        case 'O': doType(KeyEvent.VK_SHIFT, KeyEvent.VK_O); break;
		        case 'P': doType(KeyEvent.VK_SHIFT, KeyEvent.VK_P); break;
		        case 'Q': doType(KeyEvent.VK_SHIFT, KeyEvent.VK_Q); break;
		        case 'R': doType(KeyEvent.VK_SHIFT, KeyEvent.VK_R); break;
		        case 'S': doType(KeyEvent.VK_SHIFT, KeyEvent.VK_S); break;
		        case 'T': doType(KeyEvent.VK_SHIFT, KeyEvent.VK_T); break;
		        case 'U': doType(KeyEvent.VK_SHIFT, KeyEvent.VK_U); break;
		        case 'V': doType(KeyEvent.VK_SHIFT, KeyEvent.VK_V); break;
		        case 'W': doType(KeyEvent.VK_SHIFT, KeyEvent.VK_W); break;
		        case 'X': doType(KeyEvent.VK_SHIFT, KeyEvent.VK_X); break;
		        case 'Y': doType(KeyEvent.VK_SHIFT, KeyEvent.VK_Y); break;
		        case 'Z': doType(KeyEvent.VK_SHIFT, KeyEvent.VK_Z); break;
		        case '`': doType(KeyEvent.VK_BACK_QUOTE); break;
		        case '0': doType(KeyEvent.VK_0); break;
		        case '1': doType(KeyEvent.VK_1); break;
		        case '2': doType(KeyEvent.VK_2); break;
		        case '3': doType(KeyEvent.VK_3); break;
		        case '4': doType(KeyEvent.VK_4); break;
		        case '5': doType(KeyEvent.VK_5); break;
		        case '6': doType(KeyEvent.VK_6); break;
		        case '7': doType(KeyEvent.VK_7); break;
		        case '8': doType(KeyEvent.VK_8); break;
		        case '9': doType(KeyEvent.VK_9); break;
		        case '-': doType(KeyEvent.VK_MINUS); break;
		        case '=': doType(KeyEvent.VK_EQUALS); break;
		        case '~': doType(KeyEvent.VK_SHIFT, KeyEvent.VK_BACK_QUOTE); break;
		        case '!': doType(KeyEvent.VK_EXCLAMATION_MARK); break;
		        case '@': doType(KeyEvent.VK_SHIFT, KeyEvent.VK_2); break;
		        case '#': doType(KeyEvent.VK_NUMBER_SIGN); break;
		        case '$': doType(KeyEvent.VK_DOLLAR); break;
		        case '%': doType(KeyEvent.VK_SHIFT, KeyEvent.VK_5); break;
		        case '^': doType(KeyEvent.VK_CIRCUMFLEX); break;
		        case '&': doType(KeyEvent.VK_AMPERSAND); break;
		        case '*': doType(KeyEvent.VK_ASTERISK); break;
		        //case '(': doType(KeyEvent.VK_LEFT_PARENTHESIS); break;
		        // case ')': doType(KeyEvent.VK_RIGHT_PARENTHESIS); break;
		        case '(': doType(KeyEvent.VK_SHIFT,KeyEvent.VK_9); break;
		        case ')': doType(KeyEvent.VK_SHIFT,KeyEvent.VK_0); break;
		       // case '_': doType(KeyEvent.VK_UNDERSCORE); break;
		        case '_': doType(KeyEvent.VK_SHIFT, KeyEvent.VK_MINUS); break;

		        case '+': doType(KeyEvent.VK_PLUS); break;
		        case '\t': doType(KeyEvent.VK_TAB); break;
		        case '\n': doType(KeyEvent.VK_ENTER); break;
		        case '[': doType(KeyEvent.VK_OPEN_BRACKET); break;
		        case ']': doType(KeyEvent.VK_CLOSE_BRACKET); break;
		        case '\\': doType(KeyEvent.VK_BACK_SLASH); break;
		        case '{': doType(KeyEvent.VK_SHIFT, KeyEvent.VK_OPEN_BRACKET); break;
		        case '}': doType(KeyEvent.VK_SHIFT, KeyEvent.VK_CLOSE_BRACKET); break;
		        case '|': doType(KeyEvent.VK_SHIFT, KeyEvent.VK_BACK_SLASH); break;
		        case ';': doType(KeyEvent.VK_SEMICOLON); break;
		        case ':': doType(KeyEvent.VK_SHIFT, KeyEvent.VK_SEMICOLON); break;
		        case '\'': doType(KeyEvent.VK_QUOTE); break;
		        case '"': doType(KeyEvent.VK_SHIFT, KeyEvent.VK_QUOTE); break;
		        case ',': doType(KeyEvent.VK_COMMA); break;
		        case '<': doType(KeyEvent.VK_LESS); break;
		        case '.': doType(KeyEvent.VK_PERIOD); break;
		        case '>': doType(KeyEvent.VK_GREATER); break;
		        case '/': doType(KeyEvent.VK_SLASH); break;
		        case '?': doType(KeyEvent.VK_SHIFT, KeyEvent.VK_SLASH); break;
		        case ' ': doType(KeyEvent.VK_SPACE); break;

		        // Swedish
		        case '\u00c5': doTypeAltCode("143"); break;

		        // Spanish ... http://www.asciitable.com/
		        case '\u00e1': doTypeAltCode("160"); break;
		        case '\u00e9': doTypeAltCode("130"); break;
		        case '\u00ed': doTypeAltCode("161"); break;
		        case '\u00f3': doTypeAltCode("162"); break;
		        case '\u00fa': doTypeAltCode("163"); break;
		        case '\u00d1': doTypeAltCode("165"); break;
		        case '\u00f1': doTypeAltCode("164"); break;

		        default:
		        	throw new IllegalArgumentException("Cannot type character " + character);
		        }
		    }

		    public void doType(int... keyCodes) {
		        doType(keyCodes, 0, keyCodes.length);
		    }

		    private void doType(int[] keyCodes, int offset, int length) {
		        if (length == 0) {
		                return;
		        }

		        robot.keyPress(keyCodes[offset]);
		        doType(keyCodes, offset + 1, length - 1);
		        robot.keyRelease(keyCodes[offset]);
		    }

		    private void doTypeAltCode(String code) {

		    	robot.keyPress(KeyEvent.VK_ALT);

		    	for (int i = 0; i < code.length(); i ++) {
			        switch (code.charAt(i)) {
			        case '1': doType(KeyEvent.VK_NUMPAD1); break;
			        case '2': doType(KeyEvent.VK_NUMPAD2); break;
			        case '3': doType(KeyEvent.VK_NUMPAD3); break;
			        case '4': doType(KeyEvent.VK_NUMPAD4); break;
			        case '5': doType(KeyEvent.VK_NUMPAD5); break;
			        case '6': doType(KeyEvent.VK_NUMPAD6); break;
			        case '7': doType(KeyEvent.VK_NUMPAD7); break;
			        case '8': doType(KeyEvent.VK_NUMPAD8); break;
			        case '9': doType(KeyEvent.VK_NUMPAD9); break;
			        case '0': doType(KeyEvent.VK_NUMPAD0); break;
			        }
		    	}

		    	robot.keyRelease(KeyEvent.VK_ALT);
		    }
		}
	}
}