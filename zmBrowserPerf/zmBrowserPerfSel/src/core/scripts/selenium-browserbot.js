/*
 * ***** BEGIN LICENSE BLOCK *****
 * 
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2010, 2011 VMware, Inc.
 * 
 * The contents of this file are subject to the Zimbra Public License
 * Version 1.3 ("License"); you may not use this file except in
 * compliance with the License.  You may obtain a copy of the License at
 * http://www.zimbra.com/license.
 * 
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied.
 * 
 * ***** END LICENSE BLOCK *****
 */
/*
* Copyright 2004 ThoughtWorks, Inc
*
*  Licensed under the Apache License, Version 2.0 (the "License");
*  you may not use this file except in compliance with the License.
*  You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
*  Unless required by applicable law or agreed to in writing, software
*  distributed under the License is distributed on an "AS IS" BASIS,
*  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*  See the License for the specific language governing permissions and
*  limitations under the License.
*
*/

/*
* This script provides the Javascript API to drive the test application contained within
* a Browser Window.
* TODO:
*    Add support for more events (keyboard and mouse)
*    Allow to switch "user-entry" mode from mouse-based to keyboard-based, firing different
*          events in different modes.
*/

// The window to which the commands will be sent.  For example, to click on a
// popup window, first select that window, and then do a normal click command.
var BrowserBot = function(topLevelApplicationWindow) {
	this.topWindow = topLevelApplicationWindow;
	this.topFrame = this.topWindow;
	this.baseUrl = window.location.href;
	this.dontFailOnTimeout = false; //used for -ve testing
	// the buttonWindow is the Selenium window
	// it contains the Run/Pause buttons... this should *not* be the AUT window
	this.buttonWindow = window;
	this.currentWindow = this.topWindow;
	this.currentWindowName = null;
	this.allowNativeXpath = true;
	this.xpathLibrary = 'ajaxslt' // change to "javascript-xpath" for the newer, faster engine

	// We need to know this in advance, in case the frame closes unexpectedly
	this.isSubFrameSelected = false;

	this.altKeyDown = false;
	this.controlKeyDown = false;
	this.shiftKeyDown = false;
	this.metaKeyDown = false;

	this.modalDialogTest = null;
	this.recordedAlerts = new Array();
	this.recordedConfirmations = new Array();
	this.recordedPrompts = new Array();
	this.openedWindows = {};
	this.nextConfirmResult = true;
	this.nextPromptResult = '';
	this.newPageLoaded = false;
	this.pageLoadError = null;

	this.shouldHighlightLocatedElement = false;

	this.uniqueId = "seleniumMarker" + new Date().getTime();
	this.pollingForLoad = new Object();
	this.permDeniedCount = new Object();
	this.windowPollers = new Array();
    // DGF for backwards compatibility
	this.browserbot = this;

	var self = this;

	objectExtend(this, PageBot.prototype);
	this._registerAllLocatorFunctions();

	this.recordPageLoad = function(elementOrWindow) {
		LOG.debug("Page load detected");
		try {
			if (elementOrWindow.location && elementOrWindow.location.href) {
				LOG.debug("Page load location=" + elementOrWindow.location.href);
			} else if (elementOrWindow.contentWindow && elementOrWindow.contentWindow.location && elementOrWindow.contentWindow.location.href) {
				LOG.debug("Page load location=" + elementOrWindow.contentWindow.location.href);
			} else {
				LOG.debug("Page load location unknown, current window location=" + this.getCurrentWindow(true).location);
			}
		} catch (e) {
			LOG.error("Caught an exception attempting to log location; this should get noticed soon!");
			LOG.exception(e);
			self.pageLoadError = e;
			return;
		}
		self.newPageLoaded = true;
	};

	this.isNewPageLoaded = function() {
		if (this.pageLoadError) {
			LOG.error("isNewPageLoaded found an old pageLoadError");
			var e = this.pageLoadError;
			this.pageLoadError = null;
			throw e;
		}
		return self.newPageLoaded;
	};

	this._browserName = this.zGetBrowserName();//set browsername


};


BrowserBot.prototype.formalizeHTMLTag = function(tag) {
	//returns browser specific tag(either lowecase/uppercase)
	if (this._browserName.indexOf("IE") >= 0) //if Internet Explorer, make all the tags lowecase
		return tag.toUpperCase();
	else
		return tag.toLowerCase();
}

// DGF PageBot exists for backwards compatibility with old user-extensions
var PageBot = function() {
};

BrowserBot.createForWindow = function(window, proxyInjectionMode) {
	var browserbot;
	LOG.debug('createForWindow');
	LOG.debug("browserName: " + browserVersion.name);
	LOG.debug("userAgent: " + navigator.userAgent);
	if (browserVersion.isIE) {
		browserbot = new IEBrowserBot(window);
	}
	else if (browserVersion.isKonqueror) {
		browserbot = new KonquerorBrowserBot(window);
	}
	else if (browserVersion.isOpera) {
		browserbot = new OperaBrowserBot(window);
	}
	else if (browserVersion.isSafari) {
		browserbot = new SafariBrowserBot(window);
	}
	else {
		// Use mozilla by default
		browserbot = new MozillaBrowserBot(window);
	}
    // getCurrentWindow has the side effect of modifying it to handle page loads etc
	browserbot.proxyInjectionMode = proxyInjectionMode;
	browserbot.getCurrentWindow();    // for modifyWindow side effect.  This is not a transparent style
	return browserbot;
};

// todo: rename?  This doesn't actually "do" anything.
BrowserBot.prototype.doModalDialogTest = function(test) {
	this.modalDialogTest = test;
};

BrowserBot.prototype.cancelNextConfirmation = function(result) {
	this.nextConfirmResult = result;
};

BrowserBot.prototype.setNextPromptResult = function(result) {
	this.nextPromptResult = result;
};

BrowserBot.prototype.hasAlerts = function() {
	return (this.recordedAlerts.length > 0);
};

BrowserBot.prototype.relayBotToRC = function(s) {
	// DGF need to do this funny trick to see if we're in PI mode, because
	// "this" might be the window, rather than the browserbot (e.g. during window.alert)
	var piMode = this.proxyInjectionMode;
	if (!piMode) {
		if (typeof(selenium) != "undefined") {
			piMode = selenium.browserbot && selenium.browserbot.proxyInjectionMode;
		}
	}
	if (piMode) {
		this.relayToRC("selenium." + s);
	}
};

BrowserBot.prototype.relayToRC = function(name) {
	var object = eval(name);
	var s = 'state:' + serializeObject(name, object) + "\n";
	sendToRC(s, "state=true");
}

BrowserBot.prototype.resetPopups = function() {
	this.recordedAlerts = [];
	this.recordedConfirmations = [];
	this.recordedPrompts = [];
}

BrowserBot.prototype.getNextAlert = function() {
	var t = this.recordedAlerts.shift();
	this.relayBotToRC("browserbot.recordedAlerts");
	return t;
};

BrowserBot.prototype.hasConfirmations = function() {
	return (this.recordedConfirmations.length > 0);
};

BrowserBot.prototype.getNextConfirmation = function() {
	var t = this.recordedConfirmations.shift();
	this.relayBotToRC("browserbot.recordedConfirmations");
	return t;
};

BrowserBot.prototype.hasPrompts = function() {
	return (this.recordedPrompts.length > 0);
};

BrowserBot.prototype.getNextPrompt = function() {
	var t = this.recordedPrompts.shift();
	this.relayBotToRC("browserbot.recordedPrompts");
	return t;
};

/* Fire a mouse event in a browser-compatible manner */

BrowserBot.prototype.triggerMouseEvent = function(element, eventType, canBubble, clientX, clientY) {
	clientX = clientX ? clientX : 0;
	clientY = clientY ? clientY : 0;

	LOG.debug("triggerMouseEvent assumes setting screenX and screenY to 0 is ok");
	var screenX = 0;
	var screenY = 0;

	canBubble = (typeof(canBubble) == undefined) ? true : canBubble;
	if (element.fireEvent) {
		var evt = createEventObject(element, this.controlKeyDown, this.altKeyDown, this.shiftKeyDown, this.metaKeyDown);
		evt.detail = 0;
		evt.button = 1;
		evt.relatedTarget = null;
		if (!screenX && !screenY && !clientX && !clientY && !this.controlKeyDown && !this.altKeyDown && !this.shiftKeyDown && !this.metaKeyDown) {
			element.fireEvent('on' + eventType);
		}
		else {
			evt.screenX = screenX;
			evt.screenY = screenY;
			evt.clientX = clientX;
			evt.clientY = clientY;

            // when we go this route, window.event is never set to contain the event we have just created.
			// ideally we could just slide it in as follows in the try-block below, but this normally
			// doesn't work.  This is why I try to avoid this code path, which is only required if we need to
			// set attributes on the event (e.g., clientX).
			try {
				window.event = evt;
			}
			catch(e) {
				// getting an "Object does not support this action or property" error.  Save the event away
				// for future reference.
				// TODO: is there a way to update window.event?

				// work around for http://jira.openqa.org/browse/SEL-280 -- make the event available somewhere:
				selenium.browserbot.getCurrentWindow().selenium_event = evt;
			}
			element.fireEvent('on' + eventType, evt);
		}
	}
	else {
		var evt = document.createEvent('MouseEvents');
		if (evt.initMouseEvent)
		{
			//Safari
			evt.initMouseEvent(eventType, canBubble, true, document.defaultView, 1, screenX, screenY, clientX, clientY,
					this.controlKeyDown, this.altKeyDown, this.shiftKeyDown, this.metaKeyDown, 0, null);
		}
		else {
			LOG.warn("element doesn't have initMouseEvent; firing an event which should -- but doesn't -- have other mouse-event related attributes here, as well as controlKeyDown, altKeyDown, shiftKeyDown, metaKeyDown");
			evt.initEvent(eventType, canBubble, true);

			evt.shiftKey = this.shiftKeyDown;
			evt.metaKey = this.metaKeyDown;
			evt.altKey = this.altKeyDown;
			evt.ctrlKey = this.controlKeyDown;

		}
		element.dispatchEvent(evt);
	}
}

BrowserBot.prototype._windowClosed = function(win) {
	var c = win.closed;
	if (c == null) return true;
	return c;
};

BrowserBot.prototype._modifyWindow = function(win) {
	// In proxyInjectionMode, have to suppress LOG calls in _modifyWindow to avoid an infinite loop
	if (this._windowClosed(win)) {
		if (!this.proxyInjectionMode) {
			LOG.error("modifyWindow: Window was closed!");
		}
		return null;
	}
	if (!this.proxyInjectionMode) {
		LOG.debug('modifyWindow ' + this.uniqueId + ":" + win[this.uniqueId]);
	}
	if (!win[this.uniqueId]) {
		win[this.uniqueId] = 1;
		this.modifyWindowToRecordPopUpDialogs(win, this);
	}
    // In proxyInjection mode, we have our own mechanism for detecting page loads
	if (!this.proxyInjectionMode) {
		this.modifySeparateTestWindowToDetectPageLoads(win);
	}
	if (win.frames && win.frames.length && win.frames.length > 0) {
		for (var i = 0; i < win.frames.length; i++) {
			try {
				this._modifyWindow(win.frames[i]);
			} catch (e) {
			} // we're just trying to be opportunistic; don't worry if this doesn't work out
		}
	}
	return win;
};

BrowserBot.prototype.selectWindow = function(target) {
	// TODO implement a locator syntax here
	if (target && target != "null") {
		try {
			this._selectWindowByName(target);
		}
		catch (e) {
			this._selectWindowByTitle(target);
		}
	} else {
		this._selectTopWindow();
	}
};

BrowserBot.prototype._selectTopWindow = function() {
	this.currentWindowName = null;
	this.currentWindow = this.topWindow;
	this.topFrame = this.topWindow;
	this.isSubFrameSelected = false;
}

BrowserBot.prototype._selectWindowByName = function(target) {
	this.currentWindow = this.getWindowByName(target, false);
	this.topFrame = this.currentWindow;
	this.currentWindowName = target;
	this.isSubFrameSelected = false;
}

BrowserBot.prototype._selectWindowByTitle = function(target) {
	var windowName = this.getWindowNameByTitle(target);
	if (!windowName) {
		this._selectTopWindow();
	} else {
		this._selectWindowByName(windowName);
	}
}

BrowserBot.prototype.selectFrame = function(target) {
	if (target.indexOf("index=") == 0) {
		target = target.substr(6);
		var frame = this.getCurrentWindow().frames[target];
		if (frame == null) {
			throw new SeleniumError("Not found: frames[" + index + "]");
		}
		if (!frame.document) {
			throw new SeleniumError("frames[" + index + "] is not a frame");
		}
		this.currentWindow = frame;
		this.isSubFrameSelected = true;
	}
	else if (target == "relative=up" || target == "relative=parent") {
		this.currentWindow = this.getCurrentWindow().parent;
		this.isSubFrameSelected = (this._getFrameElement(this.currentWindow) != null);
	} else if (target == "relative=top") {
		this.currentWindow = this.topFrame;
		this.isSubFrameSelected = false;
	} else {
		var frame = this.findElement(target);
		if (frame == null) {
			throw new SeleniumError("Not found: " + target);
		}
        // now, did they give us a frame or a frame ELEMENT?
		var match = false;
		if (frame.contentWindow) {
			// this must be a frame element
			if (browserVersion.isHTA) {
				// stupid HTA bug; can't get in the front door
				target = frame.contentWindow.name;
			} else {
				this.currentWindow = frame.contentWindow;
				this.isSubFrameSelected = true;
				match = true;
			}
		} else if (frame.document && frame.location) {
			// must be an actual window frame
			this.currentWindow = frame;
			this.isSubFrameSelected = true;
			match = true;
		}

		if (!match) {
			// neither, let's loop through the frame names
			var win = this.getCurrentWindow();

			if (win && win.frames && win.frames.length) {
				for (var i = 0; i < win.frames.length; i++) {
					if (win.frames[i].name == target) {
						this.currentWindow = win.frames[i];
						this.isSubFrameSelected = true;
						match = true;
						break;
					}
				}
			}
			if (!match) {
				throw new SeleniumError("Not a frame: " + target);
			}
		}
	}
    // modifies the window
	this.getCurrentWindow();
};

BrowserBot.prototype.doesThisFrameMatchFrameExpression = function(currentFrameString, target) {
	var isDom = false;
	if (target.indexOf("dom=") == 0) {
		target = target.substr(4);
		isDom = true;
	} else if (target.indexOf("index=") == 0) {
		target = "frames[" + target.substr(6) + "]";
		isDom = true;
	}
	var t;
	try {
		eval("t=" + currentFrameString + "." + target);
	} catch (e) {
	}
	var autWindow = this.browserbot.getCurrentWindow();
	if (t != null) {
		try {
			if (t.window == autWindow) {
				return true;
			}
			if (t.window.uniqueId == autWindow.uniqueId) {
				return true;
			}
			return false;
		} catch (permDenied) {
			// DGF if the windows are incomparable, they're probably not the same...
		}
	}
	if (isDom) {
		return false;
	}
	var currentFrame;
	eval("currentFrame=" + currentFrameString);
	if (target == "relative=up") {
		if (currentFrame.window.parent == autWindow) {
			return true;
		}
		return false;
	}
	if (target == "relative=top") {
		if (currentFrame.window.top == autWindow) {
			return true;
		}
		return false;
	}
	if (currentFrame.window == autWindow.parent) {
		if (autWindow.name == target) {
			return true;
		}
		try {
			var element = this.findElement(target, currentFrame.window);
			if (element.contentWindow == autWindow) {
				return true;
			}
		} catch (e) {
		}
	}
	return false;
};

BrowserBot.prototype.openLocation = function(target) {
	// We're moving to a new page - clear the current one
	var win = this.getCurrentWindow();
	LOG.debug("openLocation newPageLoaded = false");
	this.newPageLoaded = false;

	this.setOpenLocation(win, target);
};

BrowserBot.prototype.openWindow = function(url, windowID) {
	if (url != "") {
		url = absolutify(url, this.baseUrl);
	}
	if (browserVersion.isHTA) {
		// in HTA mode, calling .open on the window interprets the url relative to that window
		// we need to absolute-ize the URL to make it consistent
		var child = this.getCurrentWindow().open(url, windowID);
		selenium.browserbot.openedWindows[windowID] = child;
	} else {
		this.getCurrentWindow().open(url, windowID);
	}
};

BrowserBot.prototype.setIFrameLocation = function(iframe, location) {
	iframe.src = location;
};

BrowserBot.prototype.setOpenLocation = function(win, loc) {
	loc = absolutify(loc, this.baseUrl);
	if (browserVersion.isHTA) {
		var oldHref = win.location.href;
		win.location.href = loc;
		var marker = null;
		try {
			marker = this.isPollingForLoad(win);
			if (marker && win.location[marker]) {
				win.location[marker] = false;
			}
		} catch (e) {
		} // DGF don't know why, but this often fails
	} else {
		win.location.href = loc;
	}
};

BrowserBot.prototype.getCurrentPage = function() {
	return this;
};

BrowserBot.prototype.modifyWindowToRecordPopUpDialogs = function(windowToModify, browserBot) {
	var self = this;

	windowToModify.seleniumAlert = windowToModify.alert;

	windowToModify.alert = function(alert) {
		browserBot.recordedAlerts.push(alert);
		self.relayBotToRC.call(self, "browserbot.recordedAlerts");
	};

	windowToModify.confirm = function(message) {
		browserBot.recordedConfirmations.push(message);
		var result = browserBot.nextConfirmResult;
		browserBot.nextConfirmResult = true;
		self.relayBotToRC.call(self, "browserbot.recordedConfirmations");
		return result;
	};

	windowToModify.prompt = function(message) {
		browserBot.recordedPrompts.push(message);
		var result = !browserBot.nextConfirmResult ? null : browserBot.nextPromptResult;
		browserBot.nextConfirmResult = true;
		browserBot.nextPromptResult = '';
		self.relayBotToRC.call(self, "browserbot.recordedPrompts");
		return result;
	};

    // Keep a reference to all popup windows by name
	// note that in IE the "windowName" argument must be a valid javascript identifier, it seems.
	var originalOpen = windowToModify.open;
	var originalOpenReference;
	if (browserVersion.isHTA) {
		originalOpenReference = 'selenium_originalOpen' + new Date().getTime();
		windowToModify[originalOpenReference] = windowToModify.open;
	}

	var isHTA = browserVersion.isHTA;

	var newOpen = function(url, windowName, windowFeatures, replaceFlag) {
		var myOriginalOpen = originalOpen;
		if (isHTA) {
			myOriginalOpen = this[originalOpenReference];
		}
		var openedWindow = myOriginalOpen(url, windowName, windowFeatures, replaceFlag);
		LOG.debug("window.open call intercepted; window ID (which you can use with selectWindow()) is \"" + windowName + "\"");
		if (windowName != null) {
			openedWindow["seleniumWindowName"] = windowName;
		}
		selenium.browserbot.openedWindows[windowName] = openedWindow;
		return openedWindow;
	};

	if (browserVersion.isHTA) {
		originalOpenReference = 'selenium_originalOpen' + new Date().getTime();
		newOpenReference = 'selenium_newOpen' + new Date().getTime();
		var setOriginalRef = "this['" + originalOpenReference + "'] = this.open;";

		if (windowToModify.eval) {
			windowToModify.eval(setOriginalRef);
			windowToModify.open = newOpen;
		} else {
			// DGF why can't I eval here?  Seems like I'm querying the window at a bad time, maybe?
			setOriginalRef += "this.open = this['" + newOpenReference + "'];";
			windowToModify[newOpenReference] = newOpen;
			windowToModify.setTimeout(setOriginalRef, 0);
		}
	} else {
		windowToModify.open = newOpen;
	}
};

/**
 * Call the supplied function when a the current page unloads and a new one loads.
 * This is done by polling continuously until the document changes and is fully loaded.
 */
BrowserBot.prototype.modifySeparateTestWindowToDetectPageLoads = function(windowObject) {
	// Since the unload event doesn't fire in Safari 1.3, we start polling immediately
	if (!windowObject) {
		LOG.warn("modifySeparateTestWindowToDetectPageLoads: no windowObject!");
		return;
	}
	if (this._windowClosed(windowObject)) {
		LOG.info("modifySeparateTestWindowToDetectPageLoads: windowObject was closed");
		return;
	}
	var oldMarker = this.isPollingForLoad(windowObject);
	if (oldMarker) {
		LOG.debug("modifySeparateTestWindowToDetectPageLoads: already polling this window: " + oldMarker);
		return;
	}

	var marker = 'selenium' + new Date().getTime();
	LOG.debug("Starting pollForLoad (" + marker + "): " + windowObject.location);
	this.pollingForLoad[marker] = true;
    // if this is a frame, add a load listener, otherwise, attach a poller
	var frameElement = this._getFrameElement(windowObject);
    // DGF HTA mode can't attach load listeners to subframes (yuk!)
	var htaSubFrame = this._isHTASubFrame(windowObject);
	if (frameElement && !htaSubFrame) {
		LOG.debug("modifySeparateTestWindowToDetectPageLoads: this window is a frame; attaching a load listener");
		addLoadListener(frameElement, this.recordPageLoad);
		frameElement[marker] = true;
		frameElement["frame" + this.uniqueId] = marker;
		LOG.debug("dgf this.uniqueId=" + this.uniqueId);
		LOG.debug("dgf marker=" + marker);
		LOG.debug("dgf frameElement['frame'+this.uniqueId]=" + frameElement['frame' + this.uniqueId]);
		frameElement[this.uniqueId] = marker;
		LOG.debug("dgf frameElement[this.uniqueId]=" + frameElement[this.uniqueId]);
	} else {
		windowObject.location[marker] = true;
		windowObject[this.uniqueId] = marker;
		this.pollForLoad(this.recordPageLoad, windowObject, windowObject.document, windowObject.location, windowObject.location.href, marker);
	}
};

BrowserBot.prototype._isHTASubFrame = function(win) {
	if (!browserVersion.isHTA) return false;
    // DGF this is wrong! what if "win" isn't the selected window?
	return this.isSubFrameSelected;
}

BrowserBot.prototype._getFrameElement = function(win) {
	var frameElement = null;
	var caught;
	try {
		frameElement = win.frameElement;
	} catch (e) {
		caught = true;
	}
	if (caught) {
		// on IE, checking frameElement in a pop-up results in a "No such interface supported" exception
		// but it might have a frame element anyway!
		var parentContainsIdenticallyNamedFrame = false;
		try {
			parentContainsIdenticallyNamedFrame = win.parent.frames[win.name];
		} catch (e) {
		} // this may fail if access is denied to the parent; in that case, assume it's not a pop-up

		if (parentContainsIdenticallyNamedFrame) {
			// it can't be a coincidence that the parent has a frame with the same name as myself!
			var result;
			try {
				result = parentContainsIdenticallyNamedFrame.frameElement;
				if (result) {
					return result;
				}
			} catch (e) {
			} // it was worth a try! _getFrameElementsByName is often slow
			result = this._getFrameElementByName(win.name, win.parent.document, win);
			return result;
		}
	}
	LOG.debug("_getFrameElement: frameElement=" + frameElement);
	if (frameElement) {
		LOG.debug("frameElement.name=" + frameElement.name);
	}
	return frameElement;
}

BrowserBot.prototype._getFrameElementByName = function(name, doc, win) {
	var frames;
	var frame;
	var i;
	frames = doc.getElementsByTagName("iframe");
	for (i = 0; i < frames.length; i++) {
		frame = frames[i];
		if (frame.name === name) {
			return frame;
		}
	}
	frames = doc.getElementsByTagName("frame");
	for (i = 0; i < frames.length; i++) {
		frame = frames[i];
		if (frame.name === name) {
			return frame;
		}
	}
    // DGF weird; we only call this function when we know the doc contains the frame
	LOG.warn("_getFrameElementByName couldn't find a frame or iframe; checking every element for the name " + name);
	return BrowserBot.prototype.locateElementByName(win.name, win.parent.document);
}


/**
 * Set up a polling timer that will keep checking the readyState of the document until it's complete.
 * Since we might call this before the original page is unloaded, we first check to see that the current location
 * or href is different from the original one.
 */
BrowserBot.prototype.pollForLoad = function(loadFunction, windowObject, originalDocument, originalLocation, originalHref, marker) {
	LOG.debug("pollForLoad original (" + marker + "): " + originalHref);
	try {
		if (this._windowClosed(windowObject)) {
			LOG.debug("pollForLoad WINDOW CLOSED (" + marker + ")");
			delete this.pollingForLoad[marker];
			return;
		}

		var isSamePage = this._isSamePage(windowObject, originalDocument, originalLocation, originalHref, marker);
		var rs = this.getReadyState(windowObject, windowObject.document);

		if (!isSamePage && rs == 'complete') {
			var currentHref = windowObject.location.href;
			LOG.debug("pollForLoad FINISHED (" + marker + "): " + rs + " (" + currentHref + ")");
			delete this.pollingForLoad[marker];
			this._modifyWindow(windowObject);
			var newMarker = this.isPollingForLoad(windowObject);
			if (!newMarker) {
				LOG.debug("modifyWindow didn't start new poller: " + newMarker);
				this.modifySeparateTestWindowToDetectPageLoads(windowObject);
			}
			newMarker = this.isPollingForLoad(windowObject);
			var currentlySelectedWindow;
			var currentlySelectedWindowMarker;
			currentlySelectedWindow = this.getCurrentWindow(true);
			currentlySelectedWindowMarker = currentlySelectedWindow[this.uniqueId];

			LOG.debug("pollForLoad (" + marker + ") restarting " + newMarker);
			if (/(TestRunner-splash|Blank)\.html\?start=true$/.test(currentHref)) {
				LOG.debug("pollForLoad Oh, it's just the starting page.  Never mind!");
			} else if (currentlySelectedWindowMarker == newMarker) {
				loadFunction(currentlySelectedWindow);
			} else {
				LOG.debug("pollForLoad page load detected in non-current window; ignoring (currentlySelected=" + currentlySelectedWindowMarker + ", detection in " + newMarker + ")");
			}
			return;
		}
		LOG.debug("pollForLoad continue (" + marker + "): " + currentHref);
		this.reschedulePoller(loadFunction, windowObject, originalDocument, originalLocation, originalHref, marker);
	} catch (e) {
		LOG.debug("Exception during pollForLoad; this should get noticed soon (" + e.message + ")!");
        //DGF this is supposed to get logged later; log it at debug just in case
		//LOG.exception(e);
		this.pageLoadError = e;
	}
};

BrowserBot.prototype._isSamePage = function(windowObject, originalDocument, originalLocation, originalHref, marker) {
	var currentDocument = windowObject.document;
	var currentLocation = windowObject.location;
	var currentHref = currentLocation.href

	var sameDoc = this._isSameDocument(originalDocument, currentDocument);

	var sameLoc = (originalLocation === currentLocation);

    // hash marks don't meant the page has loaded, so we need to strip them off if they exist...
	var currentHash = currentHref.indexOf('#');
	if (currentHash > 0) {
		currentHref = currentHref.substring(0, currentHash);
	}
	var originalHash = originalHref.indexOf('#');
	if (originalHash > 0) {
		originalHref = originalHref.substring(0, originalHash);
	}
	LOG.debug("_isSamePage: currentHref: " + currentHref);
	LOG.debug("_isSamePage: originalHref: " + originalHref);

	var sameHref = (originalHref === currentHref);
	var markedLoc = currentLocation[marker];

	if (browserVersion.isKonqueror || browserVersion.isSafari) {
		// the mark disappears too early on these browsers
		markedLoc = true;
	}

    // since this is some _very_ important logic, especially for PI and multiWindow mode, we should log all these out
	LOG.debug("_isSamePage: sameDoc: " + sameDoc);
	LOG.debug("_isSamePage: sameLoc: " + sameLoc);
	LOG.debug("_isSamePage: sameHref: " + sameHref);
	LOG.debug("_isSamePage: markedLoc: " + markedLoc);

	return sameDoc && sameLoc && sameHref && markedLoc
};

BrowserBot.prototype._isSameDocument = function(originalDocument, currentDocument) {
	return originalDocument === currentDocument;
};


BrowserBot.prototype.getReadyState = function(windowObject, currentDocument) {
	var rs = currentDocument.readyState;
	if (rs == null) {
		if ((this.buttonWindow != null && this.buttonWindow.document.readyState == null) // not proxy injection mode (and therefore buttonWindow isn't null)
				|| (top.document.readyState == null)) {                                               // proxy injection mode (and therefore everything's in the top window, but buttonWindow doesn't exist)
			// uh oh!  we're probably on Firefox with no readyState extension installed!
			// We'll have to just take a guess as to when the document is loaded; this guess
			// will never be perfect. :-(
			if (typeof currentDocument.getElementsByTagName != 'undefined'
					&& typeof currentDocument.getElementById != 'undefined'
					&& ( currentDocument.getElementsByTagName('body')[0] != null
					|| currentDocument.body != null )) {
				if (windowObject.frameElement && windowObject.location.href == "about:blank" && windowObject.frameElement.src != "about:blank") {
					LOG.info("getReadyState not loaded, frame location was about:blank, but frame src = " + windowObject.frameElement.src);
					return null;
				}
				LOG.debug("getReadyState = windowObject.frames.length = " + windowObject.frames.length);
				for (var i = 0; i < windowObject.frames.length; i++) {
					LOG.debug("i = " + i);
					if (this.getReadyState(windowObject.frames[i], windowObject.frames[i].document) != 'complete') {
						LOG.debug("getReadyState aha! the nested frame " + windowObject.frames[i].name + " wasn't ready!");
						return null;
					}
				}

				rs = 'complete';
			} else {
				LOG.debug("pollForLoad readyState was null and DOM appeared to not be ready yet");
			}
		}
	}
	else if (rs == "loading" && browserVersion.isIE) {
		LOG.debug("pageUnloading = true!!!!");
		this.pageUnloading = true;
	}
	LOG.debug("getReadyState returning " + rs);
	return rs;
};

/** This function isn't used normally, but was the way we used to schedule pollers:
 asynchronously executed autonomous units.  This is deprecated, but remains here
 for future reference.
 */
BrowserBot.prototype.XXXreschedulePoller = function(loadFunction, windowObject, originalDocument, originalLocation, originalHref, marker) {
	var self = this;
	window.setTimeout(function() {
		self.pollForLoad(loadFunction, windowObject, originalDocument, originalLocation, originalHref, marker);
	}, 500);
};

/** This function isn't used normally, but is useful for debugging asynchronous pollers
 * To enable it, rename it to "reschedulePoller", so it will override the
 * existing reschedulePoller function
 */
BrowserBot.prototype.XXXreschedulePoller = function(loadFunction, windowObject, originalDocument, originalLocation, originalHref, marker) {
	var doc = this.buttonWindow.document;
	var button = doc.createElement("button");
	var buttonName = doc.createTextNode(marker + " - " + windowObject.name);
	button.appendChild(buttonName);
	var tools = doc.getElementById("tools");
	var self = this;
	button.onclick = function() {
		tools.removeChild(button);
		self.pollForLoad(loadFunction, windowObject, originalDocument, originalLocation, originalHref, marker);
	};
	tools.appendChild(button);
	window.setTimeout(button.onclick, 500);
};

BrowserBot.prototype.reschedulePoller = function(loadFunction, windowObject, originalDocument, originalLocation, originalHref, marker) {
	var self = this;
	var pollerFunction = function() {
		self.pollForLoad(loadFunction, windowObject, originalDocument, originalLocation, originalHref, marker);
	};
	this.windowPollers.push(pollerFunction);
};

BrowserBot.prototype.runScheduledPollers = function() {
	LOG.debug("runScheduledPollers");
	var oldPollers = this.windowPollers;
	this.windowPollers = new Array();
	for (var i = 0; i < oldPollers.length; i++) {
		oldPollers[i].call();
	}
	LOG.debug("runScheduledPollers DONE");
};

BrowserBot.prototype.isPollingForLoad = function(win) {
	var marker;
	var frameElement = this._getFrameElement(win);
	var htaSubFrame = this._isHTASubFrame(win);
	if (frameElement && !htaSubFrame) {
		marker = frameElement["frame" + this.uniqueId];
	} else {
		marker = win[this.uniqueId];
	}
	if (!marker) {
		LOG.debug("isPollingForLoad false, missing uniqueId " + this.uniqueId + ": " + marker);
		return false;
	}
	if (!this.pollingForLoad[marker]) {
		LOG.debug("isPollingForLoad false, this.pollingForLoad[" + marker + "]: " + this.pollingForLoad[marker]);
		return false;
	}
	return marker;
};

BrowserBot.prototype.getWindowByName = function(windowName, doNotModify) {
	LOG.debug("getWindowByName(" + windowName + ")");
    // First look in the map of opened windows
	var targetWindow = this.openedWindows[windowName];
	if (!targetWindow) {
		targetWindow = this.topWindow[windowName];
	}
	if (!targetWindow && windowName == "_blank") {
		for (var winName in this.openedWindows) {
			// _blank can match selenium_blank*, if it looks like it's OK (valid href, not closed)
			if (/^selenium_blank/.test(winName)) {
				targetWindow = this.openedWindows[winName];
				var ok;
				try {
					if (!this._windowClosed(targetWindow)) {
						ok = targetWindow.location.href;
					}
				} catch (e) {
				}
				if (ok) break;
			}
		}
	}
	if (!targetWindow) {
		throw new SeleniumError("Window does not exist");
	}
	if (browserVersion.isHTA) {
		try {
			targetWindow.location.href;
		} catch (e) {
			targetWindow = window.open("", targetWindow.name);
			this.openedWindows[targetWindow.name] = targetWindow;
		}
	}
	if (!doNotModify) {
		this._modifyWindow(targetWindow);
	}
	return targetWindow;
};

/**
 * Find a window name from the window title.
 */
BrowserBot.prototype.getWindowNameByTitle = function(windowTitle) {
	LOG.debug("getWindowNameByTitle(" + windowTitle + ")");

    // First look in the map of opened windows and iterate them
	for (var windowName in this.openedWindows) {
		var targetWindow = this.openedWindows[windowName];

        // If the target window's title is our title
		try {
			// TODO implement Pattern Matching here
			if (!this._windowClosed(targetWindow) &&
			    targetWindow.document.title == windowTitle) {
				return windowName;
			}
		} catch (e) {
			// You'll often get Permission Denied errors here in IE
			// eh, if we can't read this window's title,
			// it's probably not available to us right now anyway
		}
	}

	try {
		if (this.topWindow.document.title == windowTitle) {
			return "";
		}
	} catch (e) {
	} // IE Perm denied

	throw new SeleniumError("Could not find window with title " + windowTitle);
};

BrowserBot.prototype.getCurrentWindow = function(doNotModify) {
	if (this.proxyInjectionMode) {
		return window;
	}
	var testWindow = this.currentWindow;
	if (!doNotModify) {
		this._modifyWindow(testWindow);
		LOG.debug("getCurrentWindow newPageLoaded = false");
		this.newPageLoaded = false;
	}
	testWindow = this._handleClosedSubFrame(testWindow, doNotModify);
	return testWindow;
};

BrowserBot.prototype._handleClosedSubFrame = function(testWindow, doNotModify) {
	if (this.proxyInjectionMode) {
		return testWindow;
	}

	if (this.isSubFrameSelected) {
		var missing = true;
		if (testWindow.parent && testWindow.parent.frames && testWindow.parent.frames.length) {
			for (var i = 0; i < testWindow.parent.frames.length; i++) {
				if (testWindow.parent.frames[i] == testWindow) {
					missing = false;
					break;
				}
			}
		}
		if (missing) {
			LOG.warn("Current subframe appears to have closed; selecting top frame");
			this.selectFrame("relative=top");
			return this.getCurrentWindow(doNotModify);
		}
	} else if (this._windowClosed(testWindow)) {
		var closedError = new SeleniumError("Current window or frame is closed!");
		closedError.windowClosed = true;
		throw closedError;
	}
	return testWindow;
};

BrowserBot.prototype.highlight = function (element, force) {
	if (force || this.shouldHighlightLocatedElement) {
		try {
			highlight(element);
		} catch (e) {
		} // DGF element highlighting is low-priority and possibly dangerous
	}
	return element;
}

BrowserBot.prototype.setShouldHighlightElement = function (shouldHighlight) {
	this.shouldHighlightLocatedElement = shouldHighlight;
}

/*****************************************************************/
/* BROWSER-SPECIFIC FUNCTIONS ONLY AFTER THIS LINE */


BrowserBot.prototype._registerAllLocatorFunctions = function() {
	// TODO - don't do this in the constructor - only needed once ever
	this.locationStrategies = {};
	for (var functionName in this) {
		var result = /^locateElementBy([A-Z].+)$/.exec(functionName);
		if (result != null) {
			var locatorFunction = this[functionName];
			if (typeof(locatorFunction) != 'function') {
				continue;
			}
            // Use a specified prefix in preference to one generated from
			// the function name
			var locatorPrefix = locatorFunction.prefix || result[1].toLowerCase();
			this.locationStrategies[locatorPrefix] = locatorFunction;
		}
	}

	/**
	 * Find a locator based on a prefix.
	 */
	this.findElementBy = function(locatorType, locator, inDocument, inWindow) {
		var locatorFunction = this.locationStrategies[locatorType];
		if (! locatorFunction) {
			throw new SeleniumError("Unrecognised locator type: '" + locatorType + "'");
		}
		return locatorFunction.call(this, locator, inDocument, inWindow);
	};

	/**
	 * The implicit locator, that is used when no prefix is supplied.
	 */
	this.locationStrategies['implicit'] = function(locator, inDocument, inWindow) {
		if (locator.startsWith('//')) {
			return this.locateElementByXPath(locator, inDocument, inWindow);
		}
		if (locator.startsWith('document.')) {
			return this.locateElementByDomTraversal(locator, inDocument, inWindow);
		}
		return this.locateElementByIdentifier(locator, inDocument, inWindow);
	};
}

BrowserBot.prototype.getDocument = function() {
	return this.getCurrentWindow().document;
}

BrowserBot.prototype.getTitle = function() {
	var t = this.getDocument().title;
	if (typeof(t) == "string") {
		t = t.trim();
	}
	return t;
}

BrowserBot.prototype.getCookieByName = function(cookieName, doc) {
	if (!doc) doc = this.getDocument();
	var ck = doc.cookie;
	if (!ck) return null;
	var ckPairs = ck.split(/;/);
	for (var i = 0; i < ckPairs.length; i++) {
		var ckPair = ckPairs[i].trim();
		var ckNameValue = ckPair.split(/=/);
		var ckName = decodeURIComponent(ckNameValue[0]);
		if (ckName === cookieName) {
			return decodeURIComponent(ckNameValue[1]);
		}
	}
	return null;
}

BrowserBot.prototype.getAllCookieNames = function(doc) {
	if (!doc) doc = this.getDocument();
	var ck = doc.cookie;
	if (!ck) return [];
	var cookieNames = [];
	var ckPairs = ck.split(/;/);
	for (var i = 0; i < ckPairs.length; i++) {
		var ckPair = ckPairs[i].trim();
		var ckNameValue = ckPair.split(/=/);
		var ckName = decodeURIComponent(ckNameValue[0]);
		cookieNames.push(ckName);
	}
	return cookieNames;
}

BrowserBot.prototype.deleteCookie = function(cookieName, domain, path, doc) {
	if (!doc) doc = this.getDocument();
	var expireDateInMilliseconds = (new Date()).getTime() + (-1 * 1000);
	var cookie = cookieName + "=deleted; ";
	if (path) {
		cookie += "path=" + path + "; ";
	}
	if (domain) {
		cookie += "domain=" + domain + "; ";
	}
	cookie += "expires=" + new Date(expireDateInMilliseconds).toGMTString();
	LOG.debug("Setting cookie to: " + cookie);
	doc.cookie = cookie;
}

/** Try to delete cookie, return false if it didn't work */
BrowserBot.prototype._maybeDeleteCookie = function(cookieName, domain, path, doc) {
	this.deleteCookie(cookieName, domain, path, doc);
	return (!this.getCookieByName(cookieName, doc));
}


BrowserBot.prototype._recursivelyDeleteCookieDomains = function(cookieName, domain, path, doc) {
	var deleted = this._maybeDeleteCookie(cookieName, domain, path, doc);
	if (deleted) return true;
	var dotIndex = domain.indexOf(".");
	if (dotIndex == 0) {
		return this._recursivelyDeleteCookieDomains(cookieName, domain.substring(1), path, doc);
	} else if (dotIndex != -1) {
		return this._recursivelyDeleteCookieDomains(cookieName, domain.substring(dotIndex), path, doc);
	} else {
		// No more dots; try just not passing in a domain at all
		return this._maybeDeleteCookie(cookieName, null, path, doc);
	}
}

BrowserBot.prototype._recursivelyDeleteCookie = function(cookieName, domain, path, doc) {
	var slashIndex = path.lastIndexOf("/");
	var finalIndex = path.length - 1;
	if (slashIndex == finalIndex) {
		slashIndex--;
	}
	if (slashIndex != -1) {
		deleted = this._recursivelyDeleteCookie(cookieName, domain, path.substring(0, slashIndex + 1), doc);
		if (deleted) return true;
	}
	return this._recursivelyDeleteCookieDomains(cookieName, domain, path, doc);
}

BrowserBot.prototype.recursivelyDeleteCookie = function(cookieName, domain, path, win) {
	if (!win) win = this.getCurrentWindow();
	var doc = win.document;
	if (!domain) {
		domain = doc.domain;
	}
	if (!path) {
		path = win.location.pathname;
	}
	var deleted = this._recursivelyDeleteCookie(cookieName, "." + domain, path, doc);
	if (deleted) return;
    // Finally try a null path (Try it last because it's uncommon)
	deleted = this._recursivelyDeleteCookieDomains(cookieName, "." + domain, null, doc);
	if (deleted) return;
	throw new SeleniumError("Couldn't delete cookie " + cookieName);
}

/*
 * Finds an element recursively in frames and nested frames
 * in the specified document, using various lookup protocols
 */
BrowserBot.prototype.findElementRecursive = function(locatorType, locatorString, inDocument, inWindow) {

	var element = this.findElementBy(locatorType, locatorString, inDocument, inWindow);
	if (element != null) {
		return element;
	}

	for (var i = 0; i < inWindow.frames.length; i++) {
		element = this.findElementRecursive(locatorType, locatorString, inWindow.frames[i].document, inWindow.frames[i]);

		if (element != null) {
			return element;
		}
	}
};

/*
* Finds an element on the current page, using various lookup protocols
*/
BrowserBot.prototype.findElementOrNull = function(locator, win) {
	var locatorType = 'implicit';
	var locatorString = locator;

    // If there is a locator prefix, use the specified strategy
	var result = locator.match(/^([A-Za-z]+)=(.+)/);
	if (result) {
		locatorType = result[1].toLowerCase();
		locatorString = result[2];
	}

	if (win == null) {
		win = this.getCurrentWindow();
	}
	var element = this.findElementRecursive(locatorType, locatorString, win.document, win);

	if (element != null) {
		return this.browserbot.highlight(element);
	}

    // Element was not found by any locator function.
	return null;
};

BrowserBot.prototype.findElement = function(locator, win) {
	var element = this.findElementOrNull(locator, win);
	if (element == null) throw new SeleniumError("Element " + locator + " not found");
	return element;
}
BrowserBot.prototype.verifyZButton = function(locator) {
	if (this.findZButton(locator) == null)
		return false;
	else
		return true;
}

BrowserBot.prototype.zGetBrowserName = function() {
	try {
		var win = this.browserbot.getCurrentWindow();
	} catch(e) {
		return "";
	}
	var agent = navigator.userAgent;
	var browserName = "";
	if (agent.indexOf("Firefox/") >= 0)
		browserName = "FF " + agent.split("Firefox/")[1];
	else if (agent.indexOf("MSIE") >= 0) {
		var arry = agent.split(";");
		for (var t = 0; t < arry.length; t++) {
			if (arry[t].indexOf("MSIE") >= 0) {
				browserName = arry[t];
				break;
			}
		}
	} else if (agent.indexOf("Safari") >= 0) {
		var arry = agent.split("/");
		for (var t = 0; t < arry.length; t++) {
			if (arry[t].indexOf("Safari") >= 0) {
				browserName = arry[t];
				break;
			}
		}
	}

	return browserName;
}

BrowserBot.prototype.findZButton = function(locator) {

if(locator.indexOf("=")>0) {	
	return (this.findElementOrNull(locator));
}
	var win = this.getCurrentWindow();
	var newBtn = true;
	var inDocument = win.document;
	    // Loop through all elements, looking for ones that have 
	// a value === our expected value
	//var divElements = inDocument.getElementsByTagName("DIV");
	try {
		var divElements = inDocument.getElementById("z_shell").childNodes;
	} catch(e) {
		return null;
	}
	for (var i = 0; i < divElements.length; i++) {
		var testElement = divElements[i];
		if (parseInt(testElement.style.zIndex) == 300 &&
		    (testElement.className.indexOf("DwtControl") == -1) &&
		    ((testElement.className.indexOf("ZToolbar") >= 0) 
				|| (testElement.className.indexOf("ZmAppToolBar") >= 0)
				|| (testElement.className.indexOf("ZmConvView") >= 0)) &&
		    (testElement.innerHTML.indexOf(locator) >= 0)) {
			var div1 = testElement.getElementsByTagName("DIV");
			for (var j = 0; j < div1.length; j++) {
				var testElement = div1[j];
				if ((locator != "New") 
					&& (
						(testElement.className.indexOf("ZToolbarButton") >= 0)
						|| (testElement.className.indexOf("DwtToolbarButton") >= 0)
					)	
					&& (testElement.innerHTML.indexOf(locator) >= 0)) {
					//LOG.perfinfo("#DIVS i: "+ i);
					//LOG.perfinfo("#DIVS j: "+ j);
					//LOG.perfinfo("#Total DIVS: "+ inDocument.getElementsByTagName("DIV").length);
					LOG.perfinfo("#Total ELEMENTS: " + inDocument.getElementsByTagName("*").length);
					return testElement;
				} else if (locator == "New") {
					if (testElement.textContent)
						var actTxt = testElement.textContent;
					else if (testElement.innerText)
						var actTxt = testElement.innerText;

					if (testElement.className.indexOf("ZToolbarButton") >= 0 && (actTxt == locator)) {
						//LOG.perfinfo("#DIVS i: "+ i);
						//LOG.perfinfo("#DIVS j: "+ j);
						//LOG.perfinfo("#Total DIVS: "+ inDocument.getElementsByTagName("DIV").length);
						LOG.perfinfo("#Total ELEMENTS: " + inDocument.getElementsByTagName("*").length);

						return testElement;
					}
				}
			}

		}
	}
	return null;
};

BrowserBot.prototype.findZMail = function(locator) {

	var win = this.getCurrentWindow();
	var newBtn = true;
	var inDocument = win.document;
	    // Loop through all elements, looking for ones that have 
	// a value === our expected value
	//var divElements = inDocument.getElementsByTagName("DIV");
	try {
		var divElements = inDocument.getElementById("z_shell").childNodes;
	} catch(e) {
		return null;
	}
	for (var i = 0; i < divElements.length; i++) {
		var testElement = divElements[i];
		if (parseInt(testElement.style.zIndex) == 300 &&
			  (testElement.className.indexOf("ZmConvDoublePaneView") >= 0)){
			var div1 = testElement.getElementsByTagName("DIV");
			
			for (var j = 0; j < div1.length; j++) {
				var testElement = div1[j];
				var innerTxt = "";
				testElement.textContent ? innerTxt = testElement.textContent :  innerTxt = testElement.innerText


				if ((testElement.className.indexOf("Row ") >= 0) && (innerTxt.indexOf(locator) >= 0)) {
					if (LOG.perfStartLogging) {//todo: will have to move this to a function
						this._scannedDivsCount = this._scannedDivsCount + i + j;
						LOG.perfinfo("#DIVS i: " + i);
						LOG.perfinfo("#DIVS j: " + j);
						LOG.perfinfo("#Total DIVS: " + inDocument.getElementsByTagName("DIV").length);
						LOG.perfinfo("#Total ELEMENTS: " + inDocument.getElementsByTagName("*").length);
						var weight = inDocument.getElementsByTagName("HEAD")[0].innerHTML.length + inDocument.getElementsByTagName("BODY")[0].innerHTML.length;
						this.weight_CSV = this.weight_CSV + "," + weight;
						var divcnt = inDocument.getElementsByTagName("DIV").length;
						this.totalDivs_CSV = this.totalDivs_CSV + "," + divcnt;
						this.totalDivsCount = this.totalDivsCount + divcnt;
						this.scannedDivs_CSV = this.scannedDivs_CSV + "," + (i + j);
						this.totalElements_CSV = this.totalElements_CSV + "," + inDocument.getElementsByTagName("*").length;
						this.verifyingObjIds_CSV = this.verifyingObjIds_CSV + "," + testElement.id;
					}
					return testElement;
				} 
			}

		}
	}
	if (LOG.perfStartLogging) {//todo: will have to move this to a function
		this._scannedDivsCount = this._scannedDivsCount + i;
	}
	return null;
};


BrowserBot.prototype.findZFormObject = function(objName, objTag, objType, objNumber) {
if(objName.indexOf("=")>0) {	
	return (this.findElementOrNull(objName));
}

	if (!objTag)
		objTag = this.formalizeHTMLTag("input");
	else
		objTag = this.formalizeHTMLTag(objTag);

	var win = this.getCurrentWindow();
	var newBtn = true;
	var rowFound = false;
	var typeFlg = true;
	var rowsWithObj = new Array();
	var inDocument = win.document;
	if (!objNumber) objNumber = 1;
	    // Loop through all elements, looking for ones that have 
	// a value === our expected value
	var formalizedTr = this.formalizeHTMLTag("tr");
	var rowEls = inDocument.getElementsByTagName(formalizedTr);
	for (var i = 0; i < rowEls.length; i++) {
		var rowObj = rowEls[i];
		var inhtml = (rowObj.innerHTML);
		if (rowObj.textContent)
			var actTxt = rowObj.textContent;
		else if (rowObj.innerText)
			var actTxt = rowObj.innerText;

		var objNameIndx = actTxt.indexOf(objName);
		var innerRowsLen = rowObj.getElementsByTagName(formalizedTr).length
		var tagIndx = inhtml.indexOf(objTag);
		if ((objNameIndx >= 0) && (tagIndx >= 0) && (innerRowsLen == 0)) {
			if ((objType != "text") && (objType != "textarea")) //if its not edit, makesure we have a radio/checkbox
				var typeFlg = (inhtml.indexOf(objType) >= 0);

			if (typeFlg) {
				rowsWithObj.push(rowObj);
				rowFound = true;
			}
		}
	}

//if nothing was found.. see if there is a row with obj AND internal-row..
	if (!rowFound) {
		for (var j = 0; j < rowEls.length; j++) {
			var rowObj = rowEls[j];
			var inhtml = rowObj.innerHTML;
			if (rowObj.textContent)
				var actTxt = rowObj.textContent;
			else if (rowObj.innerText)
				var actTxt = rowObj.innerText;

			if ((actTxt.indexOf(objName) >= 0) && (inhtml.indexOf(objTag) >= 0)) {
				if ((objType != "text") && (objType != "textarea")) //if its not edit, makesure we have a radio/checkbox
					var typeFlg = (inhtml.indexOf(objType) >= 0);

				if (typeFlg) {
					rowsWithObj.push(rowObj);
					rowFound = true;
				}
			}
		}

	}
	
	//use the last-row as the correct/required row(if object# is not specified)
	var lastRowObj = rowsWithObj[rowsWithObj.length - 1];
	if (rowFound) {
		var formObjs = this.getAllFormObjs([lastRowObj], objTag, objType);
		if (formObjs.length == 1) {
			return formObjs[0];
		} else {
			return	 this._getFormObjsInRow_MultipleObjs(lastRowObj, objName, objTag, objType, objNumber);
		}
	}
	return null;
};

BrowserBot.prototype.getAllFormObjs = function(rowsWithObj, objTag, objType) {
	var arry = new Array();
	objTag = this.formalizeHTMLTag(objTag);
	for (var k = 0; k < rowsWithObj.length; k++) {
		var tmp = rowsWithObj[k].getElementsByTagName(objTag);
		for (var i = 0; i < tmp.length; i++) {
			var formObj = tmp[i];
			if (!formObj.type) {
				if ((objType == "text") || (objType == "textarea"))
					arry.push(tmp[i]);
			} else if (formObj.type == objType) {
				arry.push(tmp[i]);
			}
		}
	}

	return arry;
}

BrowserBot.prototype._getFormObjsInRow_MultipleObjs = function(rowObj, objName, objTag, objType, objNumber) {
	var allNodes = rowObj.getElementsByTagName("DIV");
	var ParsePattern = "";
	var previousTxt = "";
	var objCnt = 0;
	var txtCount = 0;
	var preObjCnt = 0;
	var foundcount = 0;
	objTag = objTag.toLowerCase();
	for (var j = 0; j < allNodes.length; j++) {
		var someEl = allNodes[j];
		var nval = "";
		//get node name
		var nname = someEl.nodeName.toLowerCase();
		//get node text
		if (someEl.textContent)
			nval = someEl.textContent;
		else if (someEl.innerText)
			nval = someEl.innerText;

		if (nname == objTag) {
			ParsePattern = ParsePattern + "O";
			objCnt = objCnt + 1;
			previousTxt = "O";
		} else if (nval.indexOf(objName) == 0 || nval.indexOf(objName) == 1) {
			if (previousTxt != "F") {
				ParsePattern = ParsePattern + "F";
				foundcount = foundcount + 1;
			}
			preObjCnt = objCnt;
			previousTxt = "F";
			txtCount = txtCount + 1;
		} else if (nval.length > 1) {
			if (previousTxt != "T") {
				ParsePattern = ParsePattern + "T";
			}
			previousTxt = "T";
			txtCount = txtCount + 1;
		}

		if ((txtCount == objCnt) && (ParsePattern.indexOf("F") >= 0) && (ParsePattern.indexOf("O") >= 0))
			break;

	}
	if (foundcount > 1 && foundcount == objCnt)
		return objList[objCnt - 1];
	else if ((ParsePattern.indexOf("O") == 0) && (ParsePattern.indexOf("TOOF") >= 0) && (ParsePattern.indexOf("F") >= 0))
		return objList[preObjCnt - 1];
	else if (objCnt >= (preObjCnt + objNumber - 1) && (ParsePattern.indexOf("F") >= 0))
		return objList[preObjCnt + objNumber - 1];
	else if (objList.length >= ObjNoInRow)
		return objList[ObjNoInRow];
	else
		return null;


}

BrowserBot.prototype.findZIconButton = function(locator) {

if(locator.indexOf("=")>0) {	
	return (this.findElementOrNull(locator));
}
	var win = this.getCurrentWindow();
	var newBtn = true;
	var inDocument = win.document;
	    // Loop through all elements, looking for ones that have 
	// a value === our expected value
	//var divElements = inDocument.getElementsByTagName("DIV");
	try {
		var divElements = inDocument.getElementById("z_shell").childNodes;
	} catch(e) {
		return null;
	}
	for (var i = 0; i < divElements.length; i++) {
		var testElement = divElements[i];
		if (parseInt(testElement.style.zIndex) == 300 &&
		    (testElement.className.indexOf("DwtControl") == -1) &&
		    ((testElement.className.indexOf("ZToolbar") >= 0) || (testElement.className.indexOf("ZmAppToolBar") >= 0))) {
			var div1 = testElement.getElementsByTagName("DIV");
			for (var j = 0; j < div1.length; j++) {
				var testElement = div1[j];
				if ((testElement.className.indexOf("ZToolbarButton") >= 0) && (testElement.innerHTML.indexOf(locator) >= 0)) {
					//LOG.perfinfo("#DIVS i: "+ i);
					//LOG.perfinfo("#DIVS j: "+ j);
					//LOG.perfinfo("#Total DIVS: "+ inDocument.getElementsByTagName("DIV").length);
					LOG.perfinfo("#Total ELEMENTS: " + inDocument.getElementsByTagName("*").length);
					return testElement;
				}
			}

		}
	}
	return null;
};


BrowserBot.prototype.findZButtonInDlg = function(buttonName, dialogName) {

if(buttonName.indexOf("=")>0) {	
	return (this.findElementOrNull(buttonName));
}
	var divElements = (this.findZDialog(dialogName)).getElementsByTagName("DIV");
	for (var j = 0; j < divElements.length; j++) {
		var testElement = divElements[j];
		if ((testElement.className.indexOf("ZButton") >= 0) && (testElement.innerHTML.indexOf(buttonName) >= 0))
			return testElement;
	}
	return null;
};

BrowserBot.prototype.findZTab = function(locator) {

if(locator.indexOf("=")>0) {	
	return (this.findElementOrNull(locator));
}
	var win = this.getCurrentWindow();
	var inDocument = win.document;
	//var divElements = inDocument.getElementsByTagName("DIV");
	try {
		var divElements = inDocument.getElementById("z_shell").childNodes;
	} catch(e) {
		return null;
	}


	for (var i = 0; i < divElements.length; i++) {
		var testElement = divElements[i];
		if (parseInt(testElement.style.zIndex) >= 300 && (testElement.innerHTML.indexOf(locator) >= 0) &&
		    (testElement.className.indexOf("ZToolbar") == -1) && (testElement.className.indexOf("ZmAppToolBar") == -1) &&
		    (testElement.className.indexOf("DwtControl") == -1) && (testElement.className.indexOf("ZmAppChooser") == -1)) {
			var div1 = testElement.getElementsByTagName("DIV");

			for (var j = 0; j < div1.length; j++) {
				var testElement = div1[j];
				if (((testElement.className.indexOf("Button") >= 0) || (testElement.className.indexOf("ZTab ") >= 0)) && (testElement.innerHTML.indexOf(locator) >= 0)) {
					//LOG.perfinfo("#DIVS i: "+ i);
					//LOG.perfinfo("#DIVS j: "+ j);
					//LOG.perfinfo("#Total DIVS: "+ inDocument.getElementsByTagName("DIV").length);
					LOG.perfinfo("#Total ELEMENTS: " + inDocument.getElementsByTagName("*").length);
					return testElement;
				}
			}

		}
	}
	return null;
};

BrowserBot.prototype.findZAppTab = function(locator) {

if(locator.indexOf("=")>0) {	
	return (this.findElementOrNull(locator));
}
	var win = this.getCurrentWindow();
	var inDocument = win.document;
	//var divElements = inDocument.getElementsByTagName("DIV");
	try {
		var divElements = inDocument.getElementById("z_shell").childNodes;
	} catch(e) {
		return null;
	}
	for (var i = 0; i < divElements.length; i++) {
		var testElement = divElements[i];
		if (parseInt(testElement.style.zIndex) >= 300 && (testElement.innerHTML.indexOf(locator) >= 0)) {
			var div1 = testElement.getElementsByTagName("DIV");
			for (var j = 0; j < div1.length; j++) {
				var testElement = div1[j];
				if ((testElement.className.indexOf("Button") >= 0)
						&& (testElement.innerHTML.indexOf(locator) >= 0)
						&& (testElement.className.indexOf("ZToolbar") == -1)
						&& (testElement.className.indexOf("ZAppTab") >= 0))
				{
					//LOG.perfinfo("#DIVS i: "+ i);
					//LOG.perfinfo("#DIVS j: "+ j);
					//LOG.perfinfo("#Total DIVS: "+ inDocument.getElementsByTagName("DIV").length);
					LOG.perfinfo("#Total ELEMENTS: " + inDocument.getElementsByTagName("*").length);
					return testElement;
				}
			}

		}
	}
	return null;
};

BrowserBot.prototype.verifyZTab = function(tabName) {
	if (this.findZTab(tabName) != null)
		return true;
	else
		return false;
}
BrowserBot.prototype.verifyZMenuWithMenuItem = function(menuItem) {
	if (this.findZMenuItem(menuItem) == null)
		return false;
	else
		return true;

}

BrowserBot.prototype.findZMenuItem = function(menuItem) {

if(menuItem.indexOf("=")>0) {	
	return (this.findElementOrNull(menuItem));
}
	var win = this.getCurrentWindow();
	var inDocument = win.document;

	//var divElements = inDocument.getElementsByTagName("DIV");
	try {
		var divElements = inDocument.getElementById("z_shell").childNodes;
	} catch(e) {
		return null;
	}
	for (var i = 0; i < divElements.length; i++) {
		var testElement = divElements[i];
		if (parseInt(testElement.style.zIndex) >= 500 && (testElement.className.indexOf("ActionMenu") >= 0) && (testElement.innerHTML.indexOf(menuItem) >= 0)) {
			var div1 = testElement.getElementsByTagName("DIV");

			for (var j = 0; j < div1.length; j++) {
				var testElement = div1[j];
				if ((testElement.className.indexOf("ZMenuItem") >= 0) && (testElement.innerHTML.indexOf(menuItem) >= 0)) {
					//LOG.perfinfo("#DIVS i: "+ i);
					//LOG.perfinfo("#DIVS j: "+ j);
					//LOG.perfinfo("#Total DIVS: "+ inDocument.getElementsByTagName("DIV").length);
					LOG.perfinfo("#Total ELEMENTS: " + inDocument.getElementsByTagName("*").length);
					return testElement;
				}
			}
		}

	}

	return null;

}

BrowserBot.prototype.verifyZTable = function(tableId) {
	var win = this.getCurrentWindow();
	var inDocument = win.document;
	    // Loop through all elements, looking for ones that have 
	// a value === our expected value
	//var divElements = inDocument.getElementsByTagName("DIV");
	try {
		var divElements = inDocument.getElementById("z_shell").childNodes;
	} catch(e) {
		return null;
	}
	for (var i = 0; i < divElements.length; i++) {
		var testElement = divElements[i];
		var zIndx = parseInt(testElement.style.zIndex);
		if (!isNaN(zIndx) && (zIndx >= 300) && (testElement.innerHTML.indexOf(tableId) >= 0)) {
			var tblElements = testElement.getElementsByTagName("TABLE");
			for (var j = 0; j < tblElements.length; j++) {
				var testElement = tblElements[j];
				try {
					if (testElement.id.indexOf(tableId) >= 0)
						return true;
				} catch(e) {
				}
			}
		}
	}
	return false;
}

BrowserBot.prototype._getInnerMostElement = function(element, elementName) {
	var children = element.childNodes;
	for (var i = 0; i < children.length; i++) {
		var child = children[i];
		if (child.textContent.indexOf(elementName) >= 0 && child.childNodes.length > 0) {
			this._getInnerMostElement(child, elementName);
		} else if (child.textContent.indexOf(elementName) >= 0 && child.childNodes.length == 0) {
			return child;
		}
	}


	return null;
}

BrowserBot.prototype.verifyZview = function(viewName) {
	var className;
	switch (viewName) {
		case "Message":
			className = "ZmTradView";
			break;
		case "Conversation":
			className = "ZmConvDoublePaneView";
			break;
		case "Mail Compose":
			className = "ZmComposeView";
			break;
		case "Appointment Compose":
			className = "ZmApptComposeView";
			break;

		case "List":
			className = "ZmContactSplitView";
			break;
		case "Card":
			className = "ZmContactCardsView";
			break;
		case "Notebook Compose":
			className = "ZmPageEditView";
			break;
	}

	var win = this.getCurrentWindow();
	var inDocument = win.document;
	    // Loop through all elements, looking for ones that have 
	// a value === our expected value
	//var divElements = inDocument.getElementsByTagName("DIV");
	try {
		var divElements = inDocument.getElementById("z_shell").childNodes;
	} catch(e) {
		return null;
	}
	for (var i = 0; i < divElements.length; i++) {
		var testElement = divElements[i];
		if (parseInt(testElement.style.zIndex) >= 300 && (testElement.className.indexOf(className) >= 0))
			return true;
	}
	return false;
};

BrowserBot.prototype.findZApp = function(locator) {

if(locator.indexOf("=")>0) {	
	return (this.findElementOrNull(locator));
}
	var win = this.getCurrentWindow();
	var inDocument = win.document;
	    // Loop through all elements, looking for ones that have 
	// a value === our expected value
	//var divElements = inDocument.getElementsByTagName("DIV");
	try {
		var divElements = inDocument.getElementById("z_shell").childNodes;
	} catch(e) {
		return null;
	}
	for (var i = 0; i < divElements.length; i++) {
		var testElement = divElements[i];
		if (parseInt(testElement.style.zIndex) == 300 && (testElement.className.indexOf("ZmAppChooser") >= 0) &&
		    (testElement.innerHTML.indexOf(locator) >= 0)) {
			var div1 = testElement.getElementsByTagName("DIV");
			for (var j = 0; j < div1.length; j++) {
				var testElement = div1[j];
				if ((testElement.className.indexOf("ZButton") >= 0) && (testElement.innerHTML.indexOf(locator) >= 0)) {
					LOG.perfinfo("#DIVS: " + (j + i));
					return testElement;
				}
			}

		}
	}
	return null;
};

BrowserBot.prototype.isCalLoaded = function() {
	if (this.findZFolder("Calendar") != null && this._isCalViewLoaded())
		return true;
	else
		return false;
}

BrowserBot.prototype.isABLoaded = function() {
	if (this.findZFolder("Contacts") != null && this._isABViewLoaded())
		return true;
	else
		return false;
}

BrowserBot.prototype.isTasksLoaded = function() {
	if (this.findZFolder("Tasks") != null)
		return true;
	else
		return false;
}

BrowserBot.prototype.isDocLoaded = function() {
	if (this.findZFolder("Notebook") != null && this._isDocViewLoaded())
		return true;
	else
		return false;
}
BrowserBot.prototype.isPrefLoaded = function() {
	if (this.findZTab("Composing") != null)
		return true;
	else
		return false;
}
BrowserBot.prototype.isMailLoaded = function() {
	if (this.findZFolder("Inbox") != null && this._isMailViewLoaded())
		return true;
	else
		return false;
}
BrowserBot.prototype._isDocViewLoaded = function() {
	return this._isviewLoaded("ZmNotebookPageView");
}

BrowserBot.prototype._isDocViewLoaded = function() {
	return this._isviewLoaded("ZmNotebookPageView");
}
BrowserBot.prototype._isCalViewLoaded = function() {
	return this._isviewLoaded("ZmCalViewMgr");
}

BrowserBot.prototype._isABViewLoaded = function() {
	return this._isviewLoaded("ZmContactSplitView");
}
BrowserBot.prototype._isMailViewLoaded = function() {
	return this._isviewLoaded("ZmConvDoublePaneView");
}


BrowserBot.prototype._isviewLoaded = function(viewClassName) {
	var win = this.getCurrentWindow();
	var inDocument = win.document;

	//var divElements = inDocument.getElementsByTagName("DIV");
	try {
		var divElements = inDocument.getElementById("z_shell").childNodes;
	} catch(e) {
		return null;
	}
	for (var i = 0; i < divElements.length; i++) {
		var testElement = divElements[i];
		if (parseInt(testElement.style.zIndex) == 300 && (testElement.className == viewClassName)) {
			return true;
		}

	}

	return false;
}

BrowserBot.prototype.verifyZFolder = function(folderName) {
	if (this.findZFolder(folderName) != null)
		return true;
	else
		return false;

}

BrowserBot.prototype.verifyZDisplayed = function(locatorWithZIndx) {
	var element = this.findElementOrNull(locatorWithZIndx);
	if (element != null && element.style.zIndex >=300) {
		return true;
	} else
		return false;

}


BrowserBot.prototype.findZFolder = function(locator) {
if(locator.indexOf("=")>0) {	
	return (this.findElementOrNull(locator));
}
	var win = this.getCurrentWindow();
	var inDocument = win.document;
	    // Loop through all elements, looking for ones that have 
	// a value === our expected value
	//var divElements = inDocument.getElementsByTagName("DIV");
	try {
		var divElements = inDocument.getElementById("z_shell").childNodes;
	} catch(e) {
		return null;
	}
	for (var i = 0; i < divElements.length; i++) {
		var testElement = divElements[i];
		if (parseInt(testElement.style.zIndex) == 300 && (testElement.className.indexOf("ZmOverview") >= 0) &&
		    (testElement.innerHTML.indexOf(locator) >= 0)) {
			var div1 = testElement.getElementsByTagName("DIV");
			for (var j = 0; j < div1.length; j++) {
				var testElement = div1[j];
				if ((testElement.className == "DwtTreeItem" || testElement.className.indexOf("DwtTreeItem ") >= 0) && (testElement.innerHTML.indexOf(locator) >= 0)) {
					//LOG.perfinfo("#DIVS i: "+ i);
					//LOG.perfinfo("#DIVS j: "+ j);
					//LOG.perfinfo("#Total DIVS: "+ inDocument.getElementsByTagName("DIV").length);
					LOG.perfinfo("#Total ELEMENTS: " + inDocument.getElementsByTagName("*").length);
					return testElement;
				}
			}

		}
	}
	return null;
};

BrowserBot.prototype._getView = function() {

	var win = this.getCurrentWindow();
	var inDocument = win.document;
	    // Loop through all elements, looking for ones that have 
	// a value === our expected value
	//var divElements = inDocument.getElementsByTagName("DIV");
	try {
		var divElements = inDocument.getElementById("z_shell").childNodes;
	} catch(e) {
		return null;
	}
	for (var i = 0; i < divElements.length; i++) {
		var testElement = divElements[i];
		if (parseInt(testElement.style.zIndex) == 300 && (testElement.className == "ZmConvDoublePaneView")) {
			return testElement;
		}
	}
	return null;
};


BrowserBot.prototype.verify_msgBdyInHyb = function(msgText)  {
	return this._verifyMsgBody(msgText, "hybrid");
}

BrowserBot.prototype.verify_msgBdyInConv = function(msgText)  {
	return this._verifyMsgBody(msgText, "conversation");
}

BrowserBot.prototype._verifyMsgBody = function(msgText, view)  {
	var win = this.getCurrentWindow();
	var inDocument = win.document;
	var msgBodyid = "";
	var viewObj  = "";
	if(view == "conversation") {
		viewObj= this.findElementOrNull("class=ZmConvView");
		msgBodyid ="z_cvMsgView";
	} else if(view =="hybrid") {
		viewObj= this.findElementOrNull("class=ZmConvDoublePaneView");
		msgBodyid = "z_clvMsgView";
	} else
		return false;

	//check if the view is displayed
	if(viewObj.style.zIndex <300)
		return false;
	
	//check if the message with the correct text exist
	try {
		var iframeMsgBody = inDocument.getElementById(msgBodyid).getElementsByTagName("iframe");
	} catch(e) {
		return false;
	} 
	if(iframeMsgBody.length == 0)
		return false;

	var iframeHTML = iframeMsgBody[0].contentWindow.document.body.innerHTML;

	if(iframeHTML.indexOf(msgText) >=0) 
		return true;
	else
		return false;
}

BrowserBot.prototype._getViewRowList = function() {
	var viewDivs = this._getView().getElementsByTagName("DIV");
	for (var i = 0; i < viewDivs.length; i++) {
		var testElement = viewDivs[i];
		if (testElement.className == "DwtListView-Rows") {
			return testElement;
		}
	}
	return null;
};

BrowserBot.prototype.storeViewHTML = function() {
	this._storedViewHTML = this._getView().innerHTML;
};

BrowserBot.prototype.appendChildToView = function() {
	var p = this.getCurrentWindow().document.createElement("p");
	p.id = "testObjID";
	this._getViewRowList().appendChild(p);
};

BrowserBot.prototype.verifyViewHasNoChild = function() {
	if (this.getCurrentWindow().document.getElementById("testObjID") != null) {
		LOG.info("verifyViewHasNoChild called, obj exists");
		return false;
	} else {
		LOG.info("verifyViewHasNoChild called, obj NOT exists");
		var v = this._getViewRowList();

		this.appendChildToView();//appends child to convView(just to make sure we wait until the list is displayed)
		return true;
	}
};


BrowserBot.prototype.verifyNewView = function() {

	return ( this._storedViewHTML != this._getView().innerHTML) ? true : false;

};

BrowserBot.prototype.findZDialog = function(dialogName) {

if(dialogName.indexOf("=")>0) {	
	return (this.findElementOrNull(dialogName));
}

	var win = this.getCurrentWindow();
	var t1 = true;
	var inDocument = win.document;
	    // Loop through all elements, looking for ones that have 
	// a value === our expected value
	//var divElements = inDocument.getElementsByTagName("DIV");
	try {
		var divElements = inDocument.getElementById("z_shell").childNodes;
	} catch(e) {
		return null;
	}
	for (var i = 0; i < divElements.length; i++) {
		var testElement = divElements[i];
		if (parseInt(testElement.style.zIndex) >= 500 && (testElement.className.indexOf("DwtDialog") >= 0) &&
		    ((dialogName == undefined) || (dialogName == "") || (dialogName != undefined && testElement.innerHTML.indexOf(dialogName) >= 0))) {
			return testElement;

		}
	}
	return null;
};


BrowserBot.prototype.closeDlgIfExists = function(dlgNameCommaBtnName) {
	//this function should be called using waitDecorator(with some timeout), that way, waitDecorator
	//	keeps calling this function until timout(or this returns true), consequently providing closeDlgIfExists
	var arry = dlgNameCommaBtnName.split(",");
	var buttonName, dialogName;
	(arry[0] != undefined) ? dialogName = arry[0] : dialogName = "";
	(arry[1] != undefined) ? buttonName = arry[1] : buttonName = "";


	if (!this.verifyZDialog(dialogName)) {//make sure dlg exists
		return false;
	} else {
		var element = this.browserbot.findZButtonInDlg(buttonName, dialogName);
		this.browserbot.clickZElement(element);
		return true;
	}

}
BrowserBot.prototype.verifyZDialog = function(dialogName) {
	if (this.findZDialog(dialogName))
		return true;
	else
		return false;
};

BrowserBot.prototype.verifyZText = function(text) {

	var win = this.getCurrentWindow();
	var t1 = true;
	var inDocument = win.document;
	    // Loop through all elements, looking for ones that have 
	// a value === our expected value
	//var divElements = inDocument.getElementsByTagName("DIV");
	try {
		var divElements = inDocument.getElementById("z_shell").childNodes;
	} catch(e) {
		return null;
	}
	for (var i = 0; i < divElements.length; i++) {
		var testElement = divElements[i];
		if (parseInt(testElement.style.zIndex) >= 300 && (testElement.innerHTML.indexOf(text) >= 0)) {
			return true;
		}
	}
	return false;
};
/**
 * In non-IE browsers, getElementById() does not search by name.  Instead, we
 * we search separately by id and name.
 */
BrowserBot.prototype.locateElementByIdentifier = function(identifier, inDocument, inWindow) {
	return BrowserBot.prototype.locateElementById(identifier, inDocument, inWindow)
			|| BrowserBot.prototype.locateElementByName(identifier, inDocument, inWindow)
			|| null;
};

/**
 * Find the element with id - can't rely on getElementById, coz it returns by name as well in IE..
 */
BrowserBot.prototype.locateElementById = function(identifier, inDocument, inWindow) {
	var element = inDocument.getElementById(identifier);
	if (element && element.id === identifier) {
		return element;
	}
	else {
		return null;
	}
};

/**
 * Find an element by name, refined by (optional) element-filter
 * expressions.
 */
BrowserBot.prototype.locateElementByName = function(locator, document, inWindow) {
	var elements = document.getElementsByTagName("*");

	var filters = locator.split(' ');
	filters[0] = 'name=' + filters[0];

	while (filters.length) {
		var filter = filters.shift();
		elements = this.selectElements(filter, elements, 'value');
	}

	if (elements.length > 0) {
		return elements[0];
	}
	return null;
};

/**
 * Finds an element using by evaluating the specfied string.
 */
BrowserBot.prototype.locateElementByDomTraversal = function(domTraversal, document, window) {

	var browserbot = this.browserbot;
	var element = null;
	try {
		element = eval(domTraversal);
	} catch (e) {
		return null;
	}

	if (!element) {
		return null;
	}

	return element;
};
BrowserBot.prototype.locateElementByDomTraversal.prefix = "dom";

/**
 * Evaluates an xpath on a document, and returns a list containing nodes in the
 * resulting nodeset. The browserbot xpath methods are now backed by this
 * function. A context node may optionally be provided, and the xpath will be
 * evaluated from that context.
 *
 * @param xpath                  the xpath to evaluate
 * @param inDocument             the document in which to evaluate the xpath.
 * @param opt_allowNativeXpath   (optional) whether to allow native evaluate().
 *                               Defaults to true.
 * @param opt_xpathLibrary       (optional) the javascript library to use for
 *                               XPath. "ajaxslt" is the default. "javascript-xpath"
 *                               is newer and faster, but needs more testing.
 * @param opt_namespaceResolver  (optional) the namespace resolver function.
 *                               Defaults to null.
 * @param opt_contextNode        (optional) the context node from which to
 *                               evaluate the xpath. If unspecified, the context
 *                               will be the root document element.
 */
function eval_xpath(xpath, inDocument, opt_allowNativeXpath, opt_xpathLibrary, opt_namespaceResolver, opt_contextNode)
{
	if (arguments.length < 6) {
		var opt_contextNode = inDocument;
	}
	if (arguments.length < 5) {
		var opt_namespaceResolver = null;
	}
	if (arguments.length < 4) {
		var opt_xpathLibrary = null;
	}
	if (arguments.length < 3) {
		var opt_allowNativeXpath = true;
	}

    // Trim any trailing "/": not valid xpath, and remains from attribute
	// locator.
	if (xpath.charAt(xpath.length - 1) == '/') {
		xpath = xpath.slice(0, -1);
	}
    // HUGE hack - remove namespace from xpath for IE
	if (browserVersion && browserVersion.isIE) {
		xpath = xpath.replace(/x:/g, '')
	}


    // When using the new and faster javascript-xpath library,
	// we'll use the TestRunner's document object, not the App-Under-Test's document.
	// The new library only modifies the TestRunner document with the new
	// functionality.
	if (opt_xpathLibrary == 'javascript-xpath') {
		documentForXpath = document;
	} else {
		documentForXpath = inDocument;
	}
	var results = [];
    
    // Use document.evaluate() if it's available
	if (opt_allowNativeXpath && documentForXpath.evaluate) {
		try {
			// Regarding use of the second argument to document.evaluate():
			// http://groups.google.com/group/comp.lang.javascript/browse_thread/thread/a59ce20639c74ba1/a9d9f53e88e5ebb5
			var xpathResult = documentForXpath
					.evaluate((opt_contextNode == inDocument ? xpath : '.' + xpath),
					opt_contextNode, opt_namespaceResolver, 0, null);
		}
		catch (e) {
			throw new SeleniumError("Invalid xpath: " + extractExceptionMessage(e));
		}
		finally {
			if (xpathResult == null) {
				// If the result is null, we should still throw an Error.
				throw new SeleniumError("Invalid xpath: *");
			}
		}
		var result = xpathResult.iterateNext();
		while (result) {
			results.push(result);
			result = xpathResult.iterateNext();
		}
		return results;
	}

    // If not, fall back to slower JavaScript implementation
	// DGF set xpathdebug = true (using getEval, if you like) to turn on JS XPath debugging
	//xpathdebug = true;
	var context;
	if (opt_contextNode == inDocument) {
		context = new ExprContext(inDocument);
	}
	else {
		// provide false values to get the default constructor values
		context = new ExprContext(opt_contextNode, false, false,
				opt_contextNode.parentNode);
	}
	context.setCaseInsensitive(true);
	context.setIgnoreAttributesWithoutValue(true);
	var xpathObj;
	try {
		xpathObj = xpathParse(xpath);
	}
	catch (e) {
		throw new SeleniumError("Invalid xpath: " + extractExceptionMessage(e));
	}
	var xpathResult = xpathObj.evaluate(context);
	if (xpathResult && xpathResult.value) {
		for (var i = 0; i < xpathResult.value.length; ++i) {
			results.push(xpathResult.value[i]);
		}
	}
	return results;
}

/**
 * Finds an element identified by the xpath expression. Expressions _must_
 * begin with "//".
 */
BrowserBot.prototype.locateElementByXPath = function(xpath, inDocument, inWindow) {
	var results = eval_xpath(xpath, inDocument, this.allowNativeXpath,
			this.xpathLibrary, this._namespaceResolver);
	return (results.length > 0) ? results[0] : null;
};

BrowserBot.prototype._namespaceResolver = function(prefix) {
	if (prefix == 'html' || prefix == 'xhtml' || prefix == 'x') {
		return 'http://www.w3.org/1999/xhtml';
	} else if (prefix == 'mathml') {
		return 'http://www.w3.org/1998/Math/MathML';
	} else {
		throw new Error("Unknown namespace: " + prefix + ".");
	}
}

/**
 * Returns the number of xpath results.
 */
BrowserBot.prototype.evaluateXPathCount = function(xpath, inDocument) {
	var results = eval_xpath(xpath, inDocument, this.allowNativeXpath,
			this.xpathLibrary, this._namespaceResolver);
	return results.length;
};

/**
 * Finds a link element with text matching the expression supplied. Expressions must
 * begin with "link:".
 */
BrowserBot.prototype.locateElementByLinkText = function(linkText, inDocument, inWindow) {
	var links = inDocument.getElementsByTagName('a');
	for (var i = 0; i < links.length; i++) {
		var element = links[i];
		if (PatternMatcher.matches(linkText, getText(element))) {
			return element;
		}
	}
	return null;
};
BrowserBot.prototype.locateElementByLinkText.prefix = "link";

/**
 * Returns an attribute based on an attribute locator. This is made up of an element locator
 * suffixed with @attribute-name.
 */
BrowserBot.prototype.findAttribute = function(locator) {
	// Split into locator + attributeName
	var attributePos = locator.lastIndexOf("@");
	var elementLocator = locator.slice(0, attributePos);
	var attributeName = locator.slice(attributePos + 1);

    // Find the element.
	var element = this.findElement(elementLocator);

    // Handle missing "class" attribute in IE.
	if (browserVersion.isIE && attributeName == "class") {
		attributeName = "className";
	}

    // Get the attribute value.
	var attributeValue = element.getAttribute(attributeName);

	return attributeValue ? attributeValue.toString() : null;
};

/*
* Select the specified option and trigger the relevant events of the element.
*/
BrowserBot.prototype.selectOption = function(element, optionToSelect) {
	triggerEvent(element, 'focus', false);
	var changed = false;
	for (var i = 0; i < element.options.length; i++) {
		var option = element.options[i];
		if (option.selected && option != optionToSelect) {
			option.selected = false;
			changed = true;
		}
		else if (!option.selected && option == optionToSelect) {
			option.selected = true;
			changed = true;
		}
	}

	if (changed) {
		triggerEvent(element, 'change', true);
	}
};

/*
* Select the specified option and trigger the relevant events of the element.
*/
BrowserBot.prototype.addSelection = function(element, option) {
	this.checkMultiselect(element);
	triggerEvent(element, 'focus', false);
	if (!option.selected) {
		option.selected = true;
		triggerEvent(element, 'change', true);
	}
};

/*
* Select the specified option and trigger the relevant events of the element.
*/
BrowserBot.prototype.removeSelection = function(element, option) {
	this.checkMultiselect(element);
	triggerEvent(element, 'focus', false);
	if (option.selected) {
		option.selected = false;
		triggerEvent(element, 'change', true);
	}
};

BrowserBot.prototype.checkMultiselect = function(element) {
	if (!element.multiple)
	{
		throw new SeleniumError("Not a multi-select");
	}

};

BrowserBot.prototype.replaceText = function(element, stringValue) {
	triggerEvent(element, 'focus', false);
	triggerEvent(element, 'select', true);
	var maxLengthAttr = element.getAttribute("maxLength");
	var actualValue = stringValue;
	if (maxLengthAttr != null) {
		var maxLength = parseInt(maxLengthAttr);
		if (stringValue.length > maxLength) {
			actualValue = stringValue.substr(0, maxLength);
		}
	}

	if (getTagName(element) == "body") {
		if (element.ownerDocument && element.ownerDocument.designMode) {
			var designMode = new String(element.ownerDocument.designMode).toLowerCase();
			if (designMode = "on") {
				// this must be a rich text control!
				element.innerHTML = actualValue;
			}
		}
	} else {
		element.value = actualValue;
	}
    // DGF this used to be skipped in chrome URLs, but no longer.  Is xpcnativewrappers to blame?
	try {
		triggerEvent(element, 'change', true);
	} catch (e) {
	}
};

BrowserBot.prototype.submit = function(formElement) {
	var actuallySubmit = true;
	this._modifyElementTarget(formElement);
	if (formElement.onsubmit) {
		if (browserVersion.isHTA) {
			// run the code in the correct window so alerts are handled correctly even in HTA mode
			var win = this.browserbot.getCurrentWindow();
			var now = new Date().getTime();
			var marker = 'marker' + now;
			win[marker] = formElement;
			win.setTimeout("var actuallySubmit = " + marker + ".onsubmit();" +
			               "if (actuallySubmit) { " +
			               marker + ".submit(); " +
			               "if (" + marker + ".target && !/^_/.test(" + marker + ".target)) {" +
			               "window.open('', " + marker + ".target);" +
			               "}" +
			               "};" +
			               marker + "=null", 0);
            // pause for up to 2s while this command runs
			var terminationCondition = function () {
				return !win[marker];
			}
			return Selenium.decorateFunctionWithTimeout(terminationCondition, 2000);
		} else {
			actuallySubmit = formElement.onsubmit();
			if (actuallySubmit) {
				formElement.submit();
				if (formElement.target && !/^_/.test(formElement.target)) {
					this.browserbot.openWindow('', formElement.target);
				}
			}
		}
	} else {
		formElement.submit();
	}
}

BrowserBot.prototype.clickElement = function(element, clientX, clientY) {
	this._fireEventOnElement("click", element, clientX, clientY);
};
BrowserBot.prototype.clickZElement = function(element, clientX, clientY) {
	//LOG.debug("***************in clickZElement");
	this._fireEventOnElement("mousedown", element, 20, 10);
	//LOG.debug("eventType: mousedown");
	this._fireEventOnElement("mouseup", element, 20, 10);
	//LOG.debug("eventType: mouseup");
	// this._fireEventOnElement("click", element, 20, 10);
};

BrowserBot.prototype.doubleclickZElement = function(element, clientX, clientY) {
	//LOG.debug("***************in clickZElement");
	this._fireEventOnElement("mousedown", element, 20, 10);
	this._fireEventOnElement("dblclick", element, 20, 10);
	//LOG.debug("eventType: mousedown");
	this._fireEventOnElement("mouseup", element, 20, 10);
	//LOG.debug("eventType: mouseup");
	// this._fireEventOnElement("click", element, 20, 10);
};

BrowserBot.prototype.doubleClickElement = function(element, clientX, clientY) {
	this._fireEventOnElement("dblclick", element, clientX, clientY);
};

// The contextmenu event is fired when the user right-clicks to open the context menu
BrowserBot.prototype.contextMenuOnElement = function(element, clientX, clientY) {
	this._fireEventOnElement("contextmenu", element, clientX, clientY);
};

BrowserBot.prototype._modifyElementTarget = function(element) {
	if (element.target) {
		if (element.target == "_blank" || /^selenium_blank/.test(element.target)) {
			var tagName = getTagName(element);
			if (tagName == "a" || tagName == "form") {
				var newTarget = "selenium_blank" + Math.round(100000 * Math.random());
				LOG.warn("Link has target '_blank', which is not supported in Selenium!  Randomizing target to be: " + newTarget);
				this.browserbot.openWindow('', newTarget);
				element.target = newTarget;
			}
		}
	}
}


BrowserBot.prototype._handleClickingImagesInsideLinks = function(targetWindow, element) {
	var itrElement = element;
	while (itrElement != null) {
		if (itrElement.href) {
			targetWindow.location.href = itrElement.href;
			break;
		}
		itrElement = itrElement.parentNode;
	}
}

BrowserBot.prototype._getTargetWindow = function(element) {
	var targetWindow = element.ownerDocument.defaultView;
	if (element.target) {
		targetWindow = this._getFrameFromGlobal(element.target);
	}
	return targetWindow;
}

BrowserBot.prototype._getFrameFromGlobal = function(target) {

	if (target == "_self") {
		return this.getCurrentWindow();
	}
	if (target == "_top") {
		return this.topFrame;
	} else if (target == "_parent") {
		return this.getCurrentWindow().parent;
	} else if (target == "_blank") {
		// TODO should this set cleverer window defaults?
		return this.getCurrentWindow().open('', '_blank');
	}
	var frameElement = this.findElementBy("implicit", target, this.topFrame.document, this.topFrame);
	if (frameElement) {
		return frameElement.contentWindow;
	}
	var win = this.getWindowByName(target);
	if (win) return win;
	return this.getCurrentWindow().open('', target);
}


BrowserBot.prototype.bodyText = function() {
	if (!this.getDocument().body) {
		throw new SeleniumError("Couldn't access document.body.  Is this HTML page fully loaded?");
	}
	return getText(this.getDocument().body);
};

BrowserBot.prototype.getAllButtons = function() {
	var elements = this.getDocument().getElementsByTagName('input');
	var result = [];

	for (var i = 0; i < elements.length; i++) {
		if (elements[i].type == 'button' || elements[i].type == 'submit' || elements[i].type == 'reset') {
			result.push(elements[i].id);
		}
	}

	return result;
};


BrowserBot.prototype.getAllFields = function() {
	var elements = this.getDocument().getElementsByTagName('input');
	var result = [];

	for (var i = 0; i < elements.length; i++) {
		if (elements[i].type == 'text') {
			result.push(elements[i].id);
		}
	}

	return result;
};

BrowserBot.prototype.getAllLinks = function() {
	var elements = this.getDocument().getElementsByTagName('a');
	var result = [];

	for (var i = 0; i < elements.length; i++) {
		result.push(elements[i].id);
	}

	return result;
};

function isDefined(value) {
	return typeof(value) != undefined;
}

BrowserBot.prototype.goBack = function() {
	this.getCurrentWindow().history.back();
};

BrowserBot.prototype.goForward = function() {
	this.getCurrentWindow().history.forward();
};

BrowserBot.prototype.close = function() {
	if (browserVersion.isChrome || browserVersion.isSafari || browserVersion.isOpera) {
		this.getCurrentWindow().close();
	} else {
		this.getCurrentWindow().eval("window.close();");
	}
};

BrowserBot.prototype.refresh = function() {
	this.getCurrentWindow().location.reload(true);
};

/**
 * Refine a list of elements using a filter.
 */
BrowserBot.prototype.selectElementsBy = function(filterType, filter, elements) {
	var filterFunction = BrowserBot.filterFunctions[filterType];
	if (! filterFunction) {
		throw new SeleniumError("Unrecognised element-filter type: '" + filterType + "'");
	}

	return filterFunction(filter, elements);
};

BrowserBot.filterFunctions = {};

BrowserBot.filterFunctions.name = function(name, elements) {
	var selectedElements = [];
	for (var i = 0; i < elements.length; i++) {
		if (elements[i].name === name) {
			selectedElements.push(elements[i]);
		}
	}
	return selectedElements;
};

BrowserBot.filterFunctions.value = function(value, elements) {
	var selectedElements = [];
	for (var i = 0; i < elements.length; i++) {
		if (elements[i].value === value) {
			selectedElements.push(elements[i]);
		}
	}
	return selectedElements;
};

BrowserBot.filterFunctions.index = function(index, elements) {
	index = Number(index);
	if (isNaN(index) || index < 0) {
		throw new SeleniumError("Illegal Index: " + index);
	}
	if (elements.length <= index) {
		throw new SeleniumError("Index out of range: " + index);
	}
	return [elements[index]];
};

BrowserBot.prototype.selectElements = function(filterExpr, elements, defaultFilterType) {

	var filterType = (defaultFilterType || 'value');

    // If there is a filter prefix, use the specified strategy
	var result = filterExpr.match(/^([A-Za-z]+)=(.+)/);
	if (result) {
		filterType = result[1].toLowerCase();
		filterExpr = result[2];
	}

	return this.selectElementsBy(filterType, filterExpr, elements);
};

/**
 * Find an element by class
 */
BrowserBot.prototype.locateElementByClass = function(locator, document) {
	return elementFindFirstMatchingChild(document,
			function(element) {
				return element.className == locator
			}
			);
}

/**
 * Find an element by alt
 */
BrowserBot.prototype.locateElementByAlt = function(locator, document) {
	return elementFindFirstMatchingChild(document,
			function(element) {
				return element.alt == locator
			}
			);
}

/**
 * Find an element by css selector
 */
BrowserBot.prototype.locateElementByCss = function(locator, document) {
	var elements = cssQuery(locator, document);
	if (elements.length != 0)
		return elements[0];
	return null;
}


/*****************************************************************/
/* BROWSER-SPECIFIC FUNCTIONS ONLY AFTER THIS LINE */

function MozillaBrowserBot(frame) {
	BrowserBot.call(this, frame);
}
objectExtend(MozillaBrowserBot.prototype, BrowserBot.prototype);

function KonquerorBrowserBot(frame) {
	BrowserBot.call(this, frame);
}
objectExtend(KonquerorBrowserBot.prototype, BrowserBot.prototype);

KonquerorBrowserBot.prototype.setIFrameLocation = function(iframe, location) {
	// Window doesn't fire onload event when setting src to the current value,
	// so we set it to blank first.
	iframe.src = "about:blank";
	iframe.src = location;
};

KonquerorBrowserBot.prototype.setOpenLocation = function(win, loc) {
	// Window doesn't fire onload event when setting src to the current value,
	// so we just refresh in that case instead.
	loc = absolutify(loc, this.baseUrl);
	loc = canonicalize(loc);
	var startUrl = win.location.href;
	if ("about:blank" != win.location.href) {
		var startLoc = parseUrl(win.location.href);
		startLoc.hash = null;
		var startUrl = reassembleLocation(startLoc);
	}
	LOG.debug("startUrl=" + startUrl);
	LOG.debug("win.location.href=" + win.location.href);
	LOG.debug("loc=" + loc);
	if (startUrl == loc) {
		LOG.debug("opening exact same location");
		this.refresh();
	} else {
		LOG.debug("locations differ");
		win.location.href = loc;
	}
    // force the current polling thread to detect a page load
	var marker = this.isPollingForLoad(win);
	if (marker) {
		delete win.location[marker];
	}
};

KonquerorBrowserBot.prototype._isSameDocument = function(originalDocument, currentDocument) {
	// under Konqueror, there may be this case:
	// originalDocument and currentDocument are different objects
	// while their location are same.
	if (originalDocument) {
		return originalDocument.location == currentDocument.location
	} else {
		return originalDocument === currentDocument;
	}
};

function SafariBrowserBot(frame) {
	BrowserBot.call(this, frame);
}
objectExtend(SafariBrowserBot.prototype, BrowserBot.prototype);

SafariBrowserBot.prototype.setIFrameLocation = KonquerorBrowserBot.prototype.setIFrameLocation;
SafariBrowserBot.prototype.setOpenLocation = KonquerorBrowserBot.prototype.setOpenLocation;


function OperaBrowserBot(frame) {
	BrowserBot.call(this, frame);
}
objectExtend(OperaBrowserBot.prototype, BrowserBot.prototype);
OperaBrowserBot.prototype.setIFrameLocation = function(iframe, location) {
	if (iframe.src == location) {
		iframe.src = location + '?reload';
	} else {
		iframe.src = location;
	}
}

function IEBrowserBot(frame) {
	BrowserBot.call(this, frame);
}
objectExtend(IEBrowserBot.prototype, BrowserBot.prototype);

IEBrowserBot.prototype._handleClosedSubFrame = function(testWindow, doNotModify) {
	if (this.proxyInjectionMode) {
		return testWindow;
	}

	try {
		testWindow.location.href;
		this.permDenied = 0;
	} catch (e) {
		this.permDenied++;
	}
	if (this._windowClosed(testWindow) || this.permDenied > 4) {
		if (this.isSubFrameSelected) {
			LOG.warn("Current subframe appears to have closed; selecting top frame");
			this.selectFrame("relative=top");
			return this.getCurrentWindow(doNotModify);
		} else {
			var closedError = new SeleniumError("Current window or frame is closed!");
			closedError.windowClosed = true;
			throw closedError;
		}
	}
	return testWindow;
};

IEBrowserBot.prototype.modifyWindowToRecordPopUpDialogs = function(windowToModify, browserBot) {
	BrowserBot.prototype.modifyWindowToRecordPopUpDialogs(windowToModify, browserBot);

    // we will call the previous version of this method from within our own interception
	oldShowModalDialog = windowToModify.showModalDialog;

	windowToModify.showModalDialog = function(url, args, features) {
		// Get relative directory to where TestRunner.html lives
		// A risky assumption is that the user's TestRunner is named TestRunner.html
		var doc_location = document.location.toString();
		var end_of_base_ref = doc_location.indexOf('TestRunner.html');
		var base_ref = doc_location.substring(0, end_of_base_ref);
		var runInterval = '';
        
        // Only set run interval if options is defined
		if (typeof(window.runOptions) != undefined) {
			runInterval = "&runInterval=" + runOptions.runInterval;
		}

		var testRunnerURL = "TestRunner.html?auto=true&singletest="
				+ escape(browserBot.modalDialogTest)
				+ "&autoURL="
				+ escape(url)
				+ runInterval;
		var fullURL = base_ref + testRunnerURL;
		browserBot.modalDialogTest = null;

        // If using proxy injection mode
		if (this.proxyInjectionMode) {
			var sessionId = runOptions.getSessionId();
			if (sessionId == undefined) {
				sessionId = injectedSessionId;
			}
			if (sessionId != undefined) {
				LOG.debug("Invoking showModalDialog and injecting URL " + fullURL);
			}
			fullURL = url;
		}
		var returnValue = oldShowModalDialog(fullURL, args, features);
		return returnValue;
	};
};

IEBrowserBot.prototype.modifySeparateTestWindowToDetectPageLoads = function(windowObject) {
	this.pageUnloading = false;
	var self = this;
	var pageUnloadDetector = function() {
		self.pageUnloading = true;
	};
	windowObject.attachEvent("onbeforeunload", pageUnloadDetector);
	BrowserBot.prototype.modifySeparateTestWindowToDetectPageLoads.call(this, windowObject);
};

IEBrowserBot.prototype.pollForLoad = function(loadFunction, windowObject, originalDocument, originalLocation, originalHref, marker) {
	LOG.debug("IEBrowserBot.pollForLoad: " + marker);
	if (!this.permDeniedCount[marker]) this.permDeniedCount[marker] = 0;
	BrowserBot.prototype.pollForLoad.call(this, loadFunction, windowObject, originalDocument, originalLocation, originalHref, marker);
	if (this.pageLoadError) {
		if (this.pageUnloading) {
			var self = this;
			LOG.debug("pollForLoad UNLOADING (" + marker + "): caught exception while firing events on unloading page: " + this.pageLoadError.message);
			this.reschedulePoller(loadFunction, windowObject, originalDocument, originalLocation, originalHref, marker);
			this.pageLoadError = null;
			return;
		} else if (((this.pageLoadError.message == "Permission denied") || (/^Access is denied/.test(this.pageLoadError.message)))
				&& this.permDeniedCount[marker]++ < 8) {
			if (this.permDeniedCount[marker] > 4) {
				var canAccessThisWindow;
				var canAccessCurrentlySelectedWindow;
				try {
					windowObject.location.href;
					canAccessThisWindow = true;
				} catch (e) {
				}
				try {
					this.getCurrentWindow(true).location.href;
					canAccessCurrentlySelectedWindow = true;
				} catch (e) {
				}
				if (canAccessCurrentlySelectedWindow & !canAccessThisWindow) {
					LOG.debug("pollForLoad (" + marker + ") ABORTING: " + this.pageLoadError.message + " (" + this.permDeniedCount[marker] + "), but the currently selected window is fine");
                    // returning without rescheduling
					this.pageLoadError = null;
					return;
				}
			}

			var self = this;
			LOG.debug("pollForLoad (" + marker + "): " + this.pageLoadError.message + " (" + this.permDeniedCount[marker] + "), waiting to see if it goes away");
			this.reschedulePoller(loadFunction, windowObject, originalDocument, originalLocation, originalHref, marker);
			this.pageLoadError = null;
			return;
		}
        //handy for debugging!
		//throw this.pageLoadError;
	}
};

IEBrowserBot.prototype._windowClosed = function(win) {
	try {
		var c = win.closed;
        // frame windows claim to be non-closed when their parents are closed
		// but you can't access their document objects in that case
		if (!c) {
			try {
				win.document;
			} catch (de) {
				if (de.message == "Permission denied") {
					// the window is probably unloading, which means it's probably not closed yet
					return false;
				}
				else if (/^Access is denied/.test(de.message)) {
					// rare variation on "Permission denied"?
					LOG.debug("IEBrowserBot.windowClosed: got " + de.message + " (this.pageUnloading=" + this.pageUnloading + "); assuming window is unloading, probably not closed yet");
					return false;
				} else {
					// this is probably one of those frame window situations
					LOG.debug("IEBrowserBot.windowClosed: couldn't read win.document, assume closed: " + de.message + " (this.pageUnloading=" + this.pageUnloading + ")");
					return true;
				}
			}
		}
		if (c == null) {
			LOG.debug("IEBrowserBot.windowClosed: win.closed was null, assuming closed");
			return true;
		}
		return c;
	} catch (e) {
		LOG.debug("IEBrowserBot._windowClosed: Got an exception trying to read win.closed; we'll have to take a guess!");

		if (browserVersion.isHTA) {
			if (e.message == "Permission denied") {
				// the window is probably unloading, which means it's not closed yet
				return false;
			} else {
				// there's a good chance that we've lost contact with the window object if it is closed
				return true;
			}
		} else {
			// the window is probably unloading, which means it's not closed yet
			return false;
		}
	}
};

/**
 * In IE, getElementById() also searches by name - this is an optimisation for IE.
 */
IEBrowserBot.prototype.locateElementByIdentifer = function(identifier, inDocument, inWindow) {
	return inDocument.getElementById(identifier);
};

SafariBrowserBot.prototype.modifyWindowToRecordPopUpDialogs = function(windowToModify, browserBot) {
	BrowserBot.prototype.modifyWindowToRecordPopUpDialogs(windowToModify, browserBot);

	var originalOpen = windowToModify.open;
	/*
		 * Safari seems to be broken, so that when we manually trigger the onclick method
		 * of a button/href, any window.open calls aren't resolved relative to the app location.
		 * So here we replace the open() method with one that does resolve the url correctly.
		 */
	windowToModify.open = function(url, windowName, windowFeatures, replaceFlag) {

		if (url.startsWith("http://") || url.startsWith("https://") || url.startsWith("/")) {
			return originalOpen(url, windowName, windowFeatures, replaceFlag);
		}

        // Reduce the current path to the directory
		var currentPath = windowToModify.location.pathname || "/";
		currentPath = currentPath.replace(/\/[^\/]*$/, "/");

        // Remove any leading "./" from the new url.
		url = url.replace(/^\.\//, "");

		newUrl = currentPath + url;

		var openedWindow = originalOpen(newUrl, windowName, windowFeatures, replaceFlag);
		LOG.debug("window.open call intercepted; window ID (which you can use with selectWindow()) is \"" + windowName + "\"");
		if (windowName != null) {
			openedWindow["seleniumWindowName"] = windowName;
		}
		return openedWindow;
	};
};

MozillaBrowserBot.prototype._fireEventOnElement = function(eventType, element, clientX, clientY) {
	var win = this.getCurrentWindow();
	triggerEvent(element, 'focus', false);

    // Add an event listener that detects if the default action has been prevented.
	// (This is caused by a javascript onclick handler returning false)
	// we capture the whole event, rather than the getPreventDefault() state at the time,
	// because we need to let the entire event bubbling and capturing to go through
	// before making a decision on whether we should force the href
	var savedEvent = null;

	element.addEventListener(eventType, function(evt) {
		savedEvent = evt;
	}, false);

	this._modifyElementTarget(element);

    // Trigger the event.
	this.browserbot.triggerMouseEvent(element, eventType, true, clientX, clientY);

	if (this._windowClosed(win)) {
		return;
	}

    // Perform the link action if preventDefault was set.
	// In chrome URL, the link action is already executed by triggerMouseEvent.
	if (!browserVersion.isChrome && savedEvent != null && !savedEvent.getPreventDefault()) {
		var targetWindow = this.browserbot._getTargetWindow(element);
		if (element.href) {
			targetWindow.location.href = element.href;
		} else {
			this.browserbot._handleClickingImagesInsideLinks(targetWindow, element);
		}
	}

};


OperaBrowserBot.prototype._fireEventOnElement = function(eventType, element, clientX, clientY) {
	var win = this.getCurrentWindow();
	triggerEvent(element, 'focus', false);

	this._modifyElementTarget(element);

    // Trigger the click event.
	LOG.debug("in _fireEventOnElement eventType:" + eventType);
	this.browserbot.triggerMouseEvent(element, eventType, true, clientX, clientY);

	if (this._windowClosed(win)) {
		return;
	}

};


KonquerorBrowserBot.prototype._fireEventOnElement = function(eventType, element, clientX, clientY) {
	var win = this.getCurrentWindow();
	triggerEvent(element, 'focus', false);

	this._modifyElementTarget(element);

	if (element[eventType]) {
		element[eventType]();
	}
	else {
		this.browserbot.triggerMouseEvent(element, eventType, true, clientX, clientY);
	}

	if (this._windowClosed(win)) {
		return;
	}

};

SafariBrowserBot.prototype._fireEventOnElement = function(eventType, element, clientX, clientY) {
	triggerEvent(element, 'focus', false);
	var wasChecked = element.checked;

	this._modifyElementTarget(element);

    // For form element it is simple.
	if (element[eventType]) {
		element[eventType]();
	}
		// For links and other elements, event emulation is required.
	else {
		var targetWindow = this.browserbot._getTargetWindow(element);
        // todo: deal with anchors?
		this.browserbot.triggerMouseEvent(element, eventType, true, clientX, clientY);

	}

};

SafariBrowserBot.prototype.refresh = function() {
	var win = this.getCurrentWindow();
	if (win.location.hash) {
		// DGF Safari refuses to refresh when there's a hash symbol in the URL
		win.location.hash = "";
		var actuallyReload = function() {
			win.location.reload(true);
		}
		window.setTimeout(actuallyReload, 1);
	} else {
		win.location.reload(true);
	}
};

IEBrowserBot.prototype._fireEventOnElement = function(eventType, element, clientX, clientY) {
	var win = this.getCurrentWindow();
	triggerEvent(element, 'focus', false);

	var wasChecked = element.checked;

    // Set a flag that records if the page will unload - this isn't always accurate, because
	// <a href="javascript:alert('foo'):"> triggers the onbeforeunload event, even thought the page won't unload
	var pageUnloading = false;
	var pageUnloadDetector = function() {
		pageUnloading = true;
	};
	win.attachEvent("onbeforeunload", pageUnloadDetector);
	this._modifyElementTarget(element);
	if (element[eventType]) {
		element[eventType]();
	}
	else {
		this.browserbot.triggerMouseEvent(element, eventType, true, clientX, clientY);
	}


    // If the page is going to unload - still attempt to fire any subsequent events.
	// However, we can't guarantee that the page won't unload half way through, so we need to handle exceptions.
	try {
		win.detachEvent("onbeforeunload", pageUnloadDetector);

		if (this._windowClosed(win)) {
			return;
		}

        // Onchange event is not triggered automatically in IE.
		if (isDefined(element.checked) && wasChecked != element.checked) {
			triggerEvent(element, 'change', true);
		}

	}
	catch (e) {
		// If the page is unloading, we may get a "Permission denied" or "Unspecified error".
		// Just ignore it, because the document may have unloaded.
		if (pageUnloading) {
			LOG.logHook = function() {
			};
			LOG.warn("Caught exception when firing events on unloading page: " + e.message);
			return;
		}
		throw e;
	}
};
