/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2013, 2014, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.pages;

import java.util.Arrays;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriverException;
import com.zimbra.qa.selenium.framework.core.ClientSessionFactory;
import com.zimbra.qa.selenium.framework.util.HarnessException;

/**
 * This utility class helps the harness determine the dynamically generated DOM
 * ID's. Based on given paramenters, the DOM ID can be found using Zimbra app
 * provided ZmId.lookup().
 *
 * There are two ways to access the methods: instance and static
 *
 * Using instance: create a ZimbraDOM, add the params, then getID(s) Using
 * static: create a JSONObject, add the params, then ZimbraDOM.getID(JSON)
 *
 * @author Matt Rhoades
 *
 */

public class ZimbraDOM {
	public static Logger logger = LogManager.getLogger(ZimbraDOM.class);

	public static Map<String, EnumMap<KEY, String>> ids;

	public static enum KEY {
		app("app"), componentName("componentName"), componentType("componentType"), containingView(
				"containingView"), skinComponent("skinComponent"), sequence("sequence");

		private String key;

		private KEY(final String key) {
			this.key = key;
		}

		public String getKEY() {
			return key;
		}
	}

	private static final String PART = "var ids = ZmId.lookup()," + "len = ids.length," + "text = \"\\n\\n{\"," + "i; "
			+ "for (i = 0; i < len; i++) " + "{var id = ids[i].id;" + "var params = ZmId._idHash[id];"
			+ "text += \"\\n\" + id + \":\" + params.description + \",\\n\";"
			+ "var paramNames = AjxUtil.keys(params).sort();" + "for (var j = 0; j < paramNames.length; j++) "
			+ "{var paramName = paramNames[j];" + "if (paramName === 'id' || paramName === 'description') {"
			+ "continue;" + "}" + "var value = params[paramName];" + "if (!value) {" + "continue;" + "}"
			+ "value = ZmId._backMap[value] ? \"ZmId.\" + ZmId._backMap[value] : value;"
			+ "text += paramName + \":\" + value + \",\\n\";}text + \"}\\n\\n\"}";

	private static final String SCRIPT;

	static {
		SCRIPT = "var AjxUtil = top.AjxUtil; " + "var AjxStringUtil = top.AjxStringUtil;" + "var ZmId = top.ZmId;"
				+ PART + "return text";
	}

	protected static String lookup(JSONObject parms) throws HarnessException {

		String command = null;

		try {

			StringBuilder js = new StringBuilder();
			js.append("var ZmId = this.browserbot.getUserWindow().top.ZmId;");
			js.append("var idParams = ").append(parms.toString().replace("\"", "")).append(";");
			js.append("var domIds = ZmId.lookup(idParams);");
			js.append("domIds;");
			command = js.toString();

			String value = ((JavascriptExecutor) ClientSessionFactory.session().webDriver()).executeScript(SCRIPT)
					.toString();
			logger.info("Selenium.getEval(" + command + ") = " + value);

			return (value);

		} catch (WebDriverException e) {
			throw new HarnessException("Selenium.getEval(" + command + ") threw WebDriverException", e);
		}

	}

	private static Map<String, EnumMap<KEY, String>> getMapFromScript() throws HarnessException {
		final Map<String, EnumMap<KEY, String>> map = new HashMap<String, EnumMap<KEY, String>>();
		String resp;
		try {
			resp = showIDs().replaceAll("[\n\\{\\}]", "");
			final List<String> args = Arrays.asList(resp.split(","));
			EnumMap<KEY, String> emap = null;
			for (String arg : args) {
				final String[] arr = arg.trim().split(":");
				if (arr[0].startsWith("zcs")) {
					emap = new EnumMap<KEY, String>(KEY.class);
					map.put(arr[0], emap);
					continue;
				}
				if (arr[0].contains(KEY.app.getKEY())) {
					emap.put(KEY.app, arr[1]);
				} else if (arr[0].contains(KEY.componentName.getKEY())) {
					emap.put(KEY.componentName, arr[1]);
				} else if (arr[0].contains(KEY.componentType.getKEY())) {
					emap.put(KEY.componentType, arr[1]);
				} else if (arr[0].contains(KEY.containingView.getKEY())) {
					emap.put(KEY.containingView, arr[1]);
				} else if (arr[0].contains(KEY.skinComponent.getKEY())) {
					emap.put(KEY.skinComponent, arr[1]);
				} else if (arr[0].contains(KEY.sequence.getKEY())) {
					emap.put(KEY.sequence, arr[1]);
				}
			}
		} catch (Exception ex) {
			throw new HarnessException(ex);
		}
		ids = map;

		return ids;
	}

	public static class APP {

		public static final String APP_TASKS = "ZmId.APP_TASKS";

	}

	public static class COMPONENT_NAME {

		public static final String VIEW_TASKLIST = "ZmId.VIEW_TASKLIST";

		public static final String OP_EDIT = "ZmId.OP_EDIT";
		public static final String OP_DELETE = "ZmId.OP_DELETE";
		public static final String OP_PRINT = "ZmId.OP_PRINT";
		public static final String OP_MARK_AS_COMPLETED = "ZmId.OP_MARK_AS_COMPLETED";
		public static final String OP_CLOSE = "ZmId.OP_CLOSE";
		public static final String OP_COMPOSE_OPTIONS = "ZmId.OP_COMPOSE_OPTIONS";
		public static final String OP_VIEW_TASKEDIT = "ZmId.VIEW_TASKEDIT";

		public static final String OP_MOVE_MENU = "ZmId.OP_MOVE_MENU";
		public static final String OP_TAG_MENU = "ZmId.OP_TAG_MENU";
		public static final String OP_VIEW_MENU = "ZmId.OP_VIEW_MENU";
		public static final String OP_ATTACHMENT = "ZmId.OP_ATTACHMENT";

		public static final String SEP = "SEP";

	}

	public static class COMPONENT_TYPE {

		public static final String WIDGET_VIEW = "ZmId.WIDGET_VIEW";
		public static final String WIDGET_BUTTON = "ZmId.WIDGET_BUTTON";

	}

	public static class CONTAINING_VIEW {

		public static final String VIEW_TASKLIST = "ZmId.VIEW_TASKLIST";
		public static final String VIEW_TASKEDIT = "ZmId.VIEW_TASKEDIT";

	}

	public static class SKIN_COMPONENT {

		public static final String SKIN_APP_MAIN = "ZmId.SKIN_APP_MAIN";
		public static final String SKIN_APP_TOP_TOOLBAR = "ZmId.SKIN_APP_TOP_TOOLBAR";

	}

	public static class SEQUENCE {
		public static final String ONE = "1";
	}

	protected JSONObject MyJSON = null;

	public ZimbraDOM() {
		MyJSON = new JSONObject();
	}

	public ZimbraDOM accumulate(KEY key, Object value) throws HarnessException {
		try {
			MyJSON.accumulate(key.getKEY(), value);
			return (this);
		} catch (JSONException e) {
			throw new HarnessException(e);
		}
	}

	public String getID() throws HarnessException {
		List<String> ids = ZimbraDOM.getIDs(this.MyJSON);
		if (ids == null || ids.size() != 1) {
			throw new HarnessException("Incorrect number of matches.  Expected 1, got: " + ids.size());
		}
		return (ids.get(0));
	}

	public List<String> getIDs() throws HarnessException {
		return (ZimbraDOM.getIDs(this.MyJSON));
	}

	public static String showIDs() throws HarnessException {
		try {
			final String response;
			response = ((JavascriptExecutor) ClientSessionFactory.session().webDriver()).executeScript(SCRIPT)
					.toString();
			logger.info("\n...showIds response: " + response);
			return response;
		} catch (Exception ex) {
			throw new HarnessException(ex);
		}
	}

	public static String getID(final String... params) throws HarnessException {
		String id = null;
		if (params == null || !(params.length > 0)) {
			logger.info("...empty arguments list");
		} else {
			if (ids == null || ids.isEmpty()) {
				getMapFromScript();
			}
			final List<String> args = Arrays.asList(params);
			int i = 0;
			while (i < 2) {
				final Set<Entry<String, EnumMap<KEY, String>>> set = ids.entrySet();
				for (Entry<String, EnumMap<KEY, String>> en : set) {
					final EnumMap<KEY, String> emap = en.getValue();
					final Collection<String> vals = emap.values();
					if (vals.containsAll(args)) {
						if (id == null) {
							id = en.getKey();
							logger.info(
									"\n id = " + id + "\n params provided: " + args + "\n available values: " + vals);
							if (args.containsAll(vals)) {
								break;
							}
						} else {
							logger.info("\n for provided params: " + args + "\n ...found more than one matches of id");
							break;
						}
					}
				}
				if (id == null) {
					if (i < 1) {
						getMapFromScript();
					} else {
						logger.info("\n for provided params: " + args + "...id is null ");
					}
					i++;
				} else {
					break;
				}
			}
		}
		return id;
	}

	private static List<String> getIDs(JSONObject json) throws HarnessException {
		logger.debug("getIds()");

		// The lookup method returns a comma separated list of matching DOM ids
		List<String> ids = Arrays.asList(lookup(json).split(","));
		logger.info("getIds() = " + Arrays.toString(ids.toArray()));

		return (ids);
	}
}