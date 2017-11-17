/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2012, 2013, 2014, 2015, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.framework.util;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import com.zimbra.qa.selenium.framework.core.ClientSessionFactory;
import com.zimbra.qa.selenium.framework.core.ExecuteHarnessMain;
import com.zimbra.qa.selenium.framework.util.ConfigProperties.AppType;
import com.zimbra.qa.selenium.framework.util.performance.PerfMetrics;

public class ZimbraURI {
	private static final Logger logger = LogManager.getLogger(ZimbraURI.class);
	private URI myURI = null;

	public ZimbraURI() {
	}

	public ZimbraURI(String uri) {
		setURI(uri);
	}

	public ZimbraURI(URI uri) {
		setURI(uri);
	}

	/**
	 * Check if the current URL does not match the 'default' URL. For instance, if
	 * the test case adds query parameters, then the URL needs to be reloaded.
	 * 
	 * @return true if a reload is required
	 * @throws UnsupportedEncodingException
	 */
	public static boolean needsReload() {

		ZimbraURI base = new ZimbraURI(ZimbraURI.getBaseURI());
		ZimbraURI current = new ZimbraURI(ZimbraURI.getCurrentURI());

		logger.debug("base: " + base.getURL().toString());
		logger.debug("current: " + current.getURL().toString());

		// If the scheme, host, and query parameters are equal, then
		// no reload required
		//

		// Check the scheme
		if (!base.getURL().getScheme().equals(current.getURL().getScheme())) {
			logger.info(
					"Scheme: base(" + base.getURL().getScheme() + ") != current(" + current.getURL().getScheme() + ")");
			return (true);
		}

		// Check the host
		if (!base.getURL().getHost().equals(current.getURL().getHost())) {
			logger.info("Host: base(" + base.getURL().getHost() + ") != current(" + current.getURL().getHost() + ")");
			return (true);
		}

		// Check the query parameters
		Map<String, String> baseMap = ZimbraURI.getQueryFromString(base.getURL().getQuery());
		Map<String, String> currMap = ZimbraURI.getQueryFromString(current.getURL().getQuery());
		if (baseMap.entrySet().size() != currMap.entrySet().size()) {
			logger.info("Query: inequal query count");
			return (true);
		}
		for (Map.Entry<String, String> entry : baseMap.entrySet()) {

			if (!currMap.containsKey(entry.getKey())) {
				logger.info("Query: current does not contain query key: " + entry.getKey());
				return (true); // Missing this key
			}

			if (!currMap.get(entry.getKey()).equals(baseMap.get(entry.getKey()))) {
				logger.debug("Query key/value pair do not match: " + currMap.get(entry.getKey()) + " != "
						+ baseMap.get(entry.getKey()));
				return (true); // Values don't match
			}

		}

		logger.debug("equal!  no reload is required");
		return (false);

	}

	/**
	 * Set the URL value for this ZimbraURL (for instance, to edit later)
	 * 
	 * @param url
	 * @throws URLSyntaxException
	 */
	public void setURI(URI uri) {
		myURI = uri;
	}

	/**
	 * Set the URL value for this ZimbraURL (for instance, to edit later)
	 * 
	 * @param URL
	 * @throws URLSyntaxException
	 */
	public void setURI(String uri) {
		try {
			myURI = new URI(uri);
		} catch (URISyntaxException e) {
			logger.error("Unable to parse uri: " + uri, e);
			myURI = ZimbraURI.defaultURI();
		}
	}

	/**
	 * Set the URL value for this ZimbraURL (for instance, to edit later)
	 * 
	 * @param URL
	 * @throws URLSyntaxException
	 */
	public void setURL(String scheme, String userInfo, String host, int port, String path, String query,
			String fragment) {
		try {
			setURI(new URI(scheme, userInfo, host, port, path, query, fragment));
		} catch (URISyntaxException e) {
			logger.error("Unable to parse uri", e);
			myURI = ZimbraURI.defaultURI();
		}
	}

	/**
	 * Get the current URL value
	 * 
	 * @param URL
	 * @throws URLSyntaxException
	 */
	public URI getURL() {
		return (myURI);
	}

	/**
	 * Get the current URL value as a string
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public String toString() {
		return (myURI.toString());
	}

	public URI addQuery(String key, String value) {

		// Get the current query
		Map<String, String> query = ZimbraURI.getQueryFromString(myURI.getQuery());

		// Add the new value
		query.put(key, value);

		// Convert the query into the URL
		setURL(myURI.getScheme(), myURI.getUserInfo(), myURI.getHost(), myURI.getPort(), myURI.getPath(),
				ZimbraURI.buildQueryFromMap(query), myURI.getFragment());

		return (myURI);

	}

	public URI addQuery(Map<String, String> map) {

		// Get the current query
		Map<String, String> query = ZimbraURI.getQueryFromString(myURI.getQuery());

		// Add the new value
		query.putAll(map);

		// Convert the query into the URL
		setURL(myURI.getScheme(), myURI.getUserInfo(), myURI.getHost(), myURI.getPort(), myURI.getPath(),
				ZimbraURI.buildQueryFromMap(query), myURI.getFragment());

		return (myURI);

	}

	/**
	 * Get the URI query parameters as a Map
	 * 
	 * @return
	 */
	public Map<String, String> getQuery() {
		return (getQueryFromString(myURI.getQuery()));
	}

	public static URI getCurrentURI() {
		String uri;
		uri = ClientSessionFactory.session().webDriver().getCurrentUrl();
		try {
			return (new URI(uri));
		} catch (URISyntaxException e) {
			logger.error("Unable to parse current URL: " + uri, e);
			return (ZimbraURI.defaultURI());
		}
	}

	public static URI getBaseURI() {

		String scheme = ConfigProperties.getStringProperty("server.scheme");
		String userInfo = null;
		String host = ConfigProperties.getStringProperty("server.host");

		String path = null;
		Map<String, String> queryMap = new HashMap<String, String>();
		String fragment = null;

		if (CodeCoverage.getInstance().isEnabled()) {
			queryMap.putAll(CodeCoverage.getInstance().getQueryMap());
		}

		if (PerfMetrics.getInstance().Enabled) {
			queryMap.putAll(PerfMetrics.getInstance().getQueryMap());
		}

		if (ConfigProperties.getAppType() == AppType.AJAX || ConfigProperties.getAppType() == AppType.UNIVERSAL
				|| ConfigProperties.getAppType() == AppType.TOUCH) {
			path = "";
		}

		if (ConfigProperties.getAppType() == AppType.HTML) {
			path = "/h/";
		}

		if (ConfigProperties.getAppType() == AppType.MOBILE) {
			path = "/m/";
		}

		if (ConfigProperties.getAppType() == AppType.ADMIN) {
			scheme = "https";
			// path = "/zimbraAdmin/";
			path = "";
		}

		String query = buildQueryFromMap(queryMap);
		try {
			URI uri;
			if (ConfigProperties.getAppType() == AppType.TOUCH) {
				uri = new URI(scheme, userInfo, host, ExecuteHarnessMain.serverPort, path, "client=touch", fragment);
			} else if (ConfigProperties.getAppType() == AppType.ADMIN) {
				uri = new URI(scheme, userInfo, host, ExecuteHarnessMain.adminPort, path, query, fragment);
			} else {
				uri = new URI(scheme, userInfo, host, ExecuteHarnessMain.serverPort, path, query, fragment);
			}
			logger.info("Base uri: " + uri.toString());
			return (uri);

		} catch (URISyntaxException e) {
			logger.error("unalbe to parse uri", e);
			return (ZimbraURI.defaultURI());
		}

	}

	/**
	 * Build Query from the map
	 * 
	 * @return String
	 * 
	 */
	private static String buildQueryFromMap(Map<String, String> queryMap) {
		// Build the query from the map
		StringBuilder sb = null;
		for (Entry<String, String> set : queryMap.entrySet()) {
			String q;
			if (set.getValue() == null) {
				q = set.getKey(); // If value is null, just use the key as the parameter value
			} else {
				q = set.getKey() + "=" + set.getValue();
			}
			if (sb == null) {
				sb = new StringBuilder();
				sb.append(q);
			} else {
				sb.append('&').append(q);
			}
		}
		String query = (sb == null ? null : sb.toString());
		return query;
	}

	/**
	 * Convert a query string (i.e. ?key1=value1&key2=value2...) to a map of
	 * key/values@param query
	 * 
	 * @return
	 */
	private static Map<String, String> getQueryFromString(String query) {

		Map<String, String> map = new HashMap<String, String>();

		if (query == null || query.trim().length() == 0) {
			return (map);
		}

		// Strip any starting '?' character
		String q = (query.startsWith("?") ? query.replace("?", "") : query);

		for (String p : q.split("&")) {
			if (p.contains("=")) {
				map.put(p.split("=")[0], p.split("=")[1]);
			} else {
				// No value, just use p as the key and null as the value
				map.put(p, null);
			}
		}

		return (map);

	}

	private static URI defaultURI() {

		String scheme = ConfigProperties.getStringProperty("server.scheme");
		String host = ConfigProperties.getStringProperty("server.host");
		int port = 0;
		if (scheme.equals("https")) {
			port = 443;
		} else {
			port = 80;
		}

		try {
			return (new URI(scheme, null, host, port, null, null, null));
		} catch (URISyntaxException e) {
			logger.error("Unable to generate default URL", e);
			return (null);
		}
	}
}