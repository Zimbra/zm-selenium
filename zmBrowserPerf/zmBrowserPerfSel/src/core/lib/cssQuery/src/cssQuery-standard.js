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
	cssQuery, version 2.0.2 (2005-08-19)
	Copyright: 2004-2005, Dean Edwards (http://dean.edwards.name/)
	License: http://creativecommons.org/licenses/LGPL/2.1/
*/

cssQuery.addModule("css-standard", function() { // override IE optimisation

// cssQuery was originally written as the CSS engine for IE7. It is
//  optimised (in terms of size not speed) for IE so this module is
//  provided separately to provide cross-browser support.

// -----------------------------------------------------------------------
// browser compatibility
// -----------------------------------------------------------------------

// sniff for Win32 Explorer
isMSIE = eval("false;/*@cc_on@if(@\x5fwin32)isMSIE=true@end@*/");

if (!isMSIE) {
	getElementsByTagName = function($element, $tagName, $namespace) {
		return $namespace ? $element.getElementsByTagNameNS("*", $tagName) :
			$element.getElementsByTagName($tagName);
	};

	compareNamespace = function($element, $namespace) {
		return !$namespace || ($namespace == "*") || ($element.prefix == $namespace);
	};

	isXML = document.contentType ? function($element) {
		return /xml/i.test(getDocument($element).contentType);
	} : function($element) {
		return getDocument($element).documentElement.tagName != "HTML";
	};

	getTextContent = function($element) {
		// mozilla || opera || other
		return $element.textContent || $element.innerText || _getTextContent($element);
	};

	function _getTextContent($element) {
		var $textContent = "", $node, i;
		for (i = 0; ($node = $element.childNodes[i]); i++) {
			switch ($node.nodeType) {
				case 11: // document fragment
				case 1: $textContent += _getTextContent($node); break;
				case 3: $textContent += $node.nodeValue; break;
			}
		}
		return $textContent;
	};
}
}); // addModule
