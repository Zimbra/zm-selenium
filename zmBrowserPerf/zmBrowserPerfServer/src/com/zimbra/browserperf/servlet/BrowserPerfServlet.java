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
 * Takes a character, converts it to hex, decimal, binary, octal and html. Then
 * wraps each of the fields with XML and sends it back through the response.
 */
package com.zimbra.browserperf.servlet;

import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
  
import com.zimbra.browserperf.DatabaseConnector;
import com.zimbra.browserperf.JSONUtil;

public class BrowserPerfServlet extends HttpServlet {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		ServletContext sc = getServletContext();
		RequestDispatcher rd = null;
		System.out.println("in  post");
		if (addUser(req)) {
			System.out.println("add user succeeded");
			res.setContentType("text/xml");
			res.setHeader("Cache-Control", "no-cache");
			res.getWriter().write("<result>success</result>");
		} else {
			//System.out.println("add user failed");
			//rd = sc.getRequestDispatcher("/failure.html");
			//rd.forward(req, res);
		}
	}

	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		System.out.println("in  get");
		String responseString = "";
		ArrayList customerList = getCustomerInfo();
		responseString = JSONUtil.buildJSONWithArrayList(customerList);
		if (!responseString.equals("")) {
			res.setContentType("text/xml");
			res.setHeader("Cache-Control", "no-cache");
			res.getWriter().write(responseString);
		} else {
			// If key comes back as a null, return a question mark.
			res.setContentType("text/xml");
			res.setHeader("Cache-Control", "no-cache");
			res.getWriter().write("?");
		}
	}

	private ArrayList getCustomerInfo() {
		System.out.println("in  getCustomerInfo");
		Connection con = DatabaseConnector.getConnection();
		ResultSet result;
		HashMap map = new HashMap();
		ArrayList mapArray = new ArrayList();

		try {

			System.out.println("in  getCustomerInfo try block");
			Statement select = con.createStatement();
			result = select.executeQuery("SELECT * from perf order by browser,version;");
			/*result = select.executeQuery("SELECT browser,version,login,mailcompose_1,mailcompose_2,mailcompose_3," +
								"compose_new_window_1,compose_new_window_2,goto_calendar,goto_contacts," +
								"goto_tasks,goto_docs,goto_pref,goto_mail,goto_calendar_2," +
								"goto_contacts_2,goto_tasks_2,goto_docs_2,goto_pref_2,goto_mail_2," +
								"cal_schedule_tab,cal_attendees_tab,cal_locations_tab,cal_resources_tab,cal_schedule_tab_2," +
								"cal_attendees_tab_2,cal_locations_tab_2,cal_resources_tab_2," +
								"pref_mail_tab,pref_compose_tab,pref_signature_tab,pref_addressbook_tab,pref_accounts_tab," +
								"pref_calendar_tab,pref_shortcuts_tab,contacts_card_view,contacts_list_view," +
								"contacts_card_view_2,contacts_list_view_2,mail_msg_view,mail_conv_view,mail_msg_view_2,mail_conv_view_2," +
								"click_sent,click_trash,click_inbox,click_drafts,click_junk,"+
								"click_sent_2,click_trash_2,click_inbox_2,click_drafts_2,click_junk_2"+
								" from perf;"); */

			String name = null;
			String value = null;
			while (result.next()) {
				map = new HashMap();
				ResultSetMetaData metaData = result.getMetaData();
				for (int i = 1; i <= metaData.getColumnCount(); i++) {
					name = metaData.getColumnName(i);
					value = result.getString(name);
					loadMap(map, name, value);
				}
				mapArray.add(map);
			}
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());

			return mapArray;
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
				}

			}
		}
		return mapArray;
	}

	/*
		private HashMap getCustomerInfo(String username)
		{
			Connection con = DatabaseConnector.getConnection();
			ResultSet result = null;
			HashMap map = new HashMap();
			// preload the map with empty strings.
			map.put("email", "");
			map.put("name", "");
			map.put("address", "");
			map.put("city", "");
			map.put("state", "");
			map.put("zipcode", "");

			try
			{
				Statement select = con.createStatement();
				result = select
						.executeQuery("SELECT * from USERS where USERNAME = '"
								+ username + "';");
				String name = null;
				String value = null;
				if (result != null && result.next())
				{
					ResultSetMetaData metaData = result.getMetaData();
					for (int i = 1; i <= metaData.getColumnCount(); i++)
					{
						name = metaData.getColumnName(i);
						value = result.getString(name);
						loadMap(map, name, value);
					}
				}
			} catch (SQLException e)
			{
				return map;
			} finally{
				if(con != null)
				{
					try{
						con.close();
					}catch(SQLException e){}

				}
			}
			return map;
		}

	  */
	private void loadMap(HashMap map, String key, String value) {
		map.put(key, value == null ? "?" : value);

	}

	private boolean addUser(HttpServletRequest req) {
		System.out.println("in  add user");
		Connection con = DatabaseConnector.getConnection();
		//Hard coded values. Wouldn't it be better for the form values
		// to just match the database?
		String browser = req.getParameter("browser");
		String version = req.getParameter("version");
		String login = req.getParameter("login");
		String open_mail = req.getParameter("open_mail");
		String open_2ndmail = req.getParameter("open_2ndmail");
		String dblclick_mail = req.getParameter("dblclick_mail");
		String dblclick_2ndmail = req.getParameter("dblclick_2ndmail");
		String mailcompose_1 = req.getParameter("mailcompose_1");
		String mailcompose_2 = req.getParameter("mailcompose_2");
		String mailcompose_3 = req.getParameter("mailcompose_3");
		String compose_new_window_1 = req.getParameter("compose_new_window_1");
		String compose_new_window_2 = req.getParameter("compose_new_window_2");
		String goto_calendar = req.getParameter("goto_calendar");
		String goto_contacts = req.getParameter("goto_contacts");
		String goto_tasks = req.getParameter("goto_tasks");
		String goto_docs = req.getParameter("goto_docs");
		String goto_pref = req.getParameter("goto_pref");
		String goto_mail = req.getParameter("goto_mail");
		String goto_calendar_2 = req.getParameter("goto_calendar_2");
		String goto_contacts_2 = req.getParameter("goto_contacts_2");
		String goto_tasks_2 = req.getParameter("goto_tasks_2");
		String goto_docs_2 = req.getParameter("goto_docs_2");
		String goto_pref_2 = req.getParameter("goto_pref_2");
		String goto_mail_2 = req.getParameter("goto_mail_2");
		String cal_schedule_tab = req.getParameter("cal_schedule_tab");
		String cal_attendees_tab = req.getParameter("cal_attendees_tab");
		String cal_locations_tab = req.getParameter("cal_locations_tab");
		String cal_resources_tab = req.getParameter("cal_resources_tab");
		String cal_schedule_tab_2 = req.getParameter("cal_schedule_tab_2");
		String cal_attendees_tab_2 = req.getParameter("cal_attendees_tab_2");
		String cal_locations_tab_2 = req.getParameter("cal_locations_tab_2");
		String cal_resources_tab_2 = req.getParameter("cal_resources_tab_2");
		String pref_mail_tab = req.getParameter("pref_mail_tab");
		String pref_compose_tab = req.getParameter("pref_compose_tab");
		String pref_signature_tab = req.getParameter("pref_signature_tab");
		String pref_addressbook_tab = req.getParameter("pref_addressbook_tab");
		String pref_accounts_tab = req.getParameter("pref_accounts_tab");
		String pref_calendar_tab = req.getParameter("pref_calendar_tab");
		String pref_shortcuts_tab = req.getParameter("pref_shortcuts_tab");
		String contacts_card_view = req.getParameter("contacts_card_view");
		String contacts_list_view = req.getParameter("contacts_list_view");
		String contacts_card_view_2 = req.getParameter("contacts_card_view_2");
		String contacts_list_view_2 = req.getParameter("contacts_list_view_2");
		String click_sent = req.getParameter("click_sent");
		String click_trash = req.getParameter("click_trash");
		String click_inbox = req.getParameter("click_inbox");
		String click_drafts = req.getParameter("click_drafts");
		String click_junk = req.getParameter("click_junk");
		String click_sent_2 = req.getParameter("click_sent_2");
		String click_trash_2 = req.getParameter("click_trash_2");
		String click_inbox_2 = req.getParameter("click_inbox_2");
		String click_drafts_2 = req.getParameter("click_drafts_2");
		String click_junk_2 = req.getParameter("click_junk_2");
		String mail_msg_view = req.getParameter("mail_msg_view");
		String mail_conv_view = req.getParameter("mail_conv_view");
		String mail_msg_view_2 = req.getParameter("mail_msg_view_2");
		String mail_conv_view_2 = req.getParameter("mail_conv_view_2");

	

		try {
			Statement add = con.createStatement();
			String sqlstatement = "INSERT perf (browser,version,login,open_mail,open_2ndmail,dblclick_mail,dblclick_2ndmail,"+
					"mailcompose_1,mailcompose_2,mailcompose_3," +
					"compose_new_window_1,compose_new_window_2,goto_calendar,goto_contacts," +
					"goto_tasks,goto_docs,goto_pref,goto_mail,goto_calendar_2," +
					"goto_contacts_2,goto_tasks_2,goto_docs_2,goto_pref_2,goto_mail_2," +
					"cal_schedule_tab,cal_attendees_tab,cal_locations_tab,cal_resources_tab,cal_schedule_tab_2," +
					"cal_attendees_tab_2,cal_locations_tab_2,cal_resources_tab_2," +
					"pref_mail_tab,pref_compose_tab,pref_signature_tab,pref_addressbook_tab,pref_accounts_tab," +
					"pref_calendar_tab,pref_shortcuts_tab,contacts_card_view,contacts_list_view," +
					"contacts_card_view_2,contacts_list_view_2,mail_msg_view,mail_conv_view,mail_msg_view_2,mail_conv_view_2," +
					"click_sent,click_trash,click_inbox,click_drafts,click_junk," +
					"click_sent_2,click_trash_2,click_inbox_2,click_drafts_2,click_junk_2) " +
					" VALUES('" + browser + "','" + version + "','" + login + "','" 
					+ open_mail + "','" + open_2ndmail + "','" + dblclick_mail + "','" + dblclick_2ndmail
					+ "','" + mailcompose_1 + "','" + mailcompose_2 + "','" + mailcompose_3
					+ "','" + compose_new_window_1 + "','" + compose_new_window_2 + "','" + goto_calendar + "','" + goto_contacts + "','"
					+ goto_tasks + "','" + goto_docs + "','" + goto_pref + "','" + goto_mail + "','" + goto_calendar_2
					+ "','" + goto_contacts_2 + "','" + goto_tasks_2 + "','" + goto_docs_2 + "','" + goto_pref_2 + "','" + goto_mail_2 + "','"
					+ cal_schedule_tab + "','" + cal_attendees_tab + "','" + cal_locations_tab + "','" + cal_resources_tab + "','" + cal_schedule_tab_2 + "','"
					+ cal_attendees_tab_2 + "','" + cal_locations_tab_2 + "','" + cal_resources_tab_2 + "','"
					+ pref_mail_tab + "','" + pref_compose_tab + "','" + pref_signature_tab + "','" + pref_addressbook_tab + "','" + pref_accounts_tab + "','"
					+ pref_calendar_tab + "','" + pref_shortcuts_tab + "','" + contacts_card_view + "','" + contacts_list_view + "','"
					+ contacts_card_view_2 + "','" + contacts_list_view_2 + "','" + mail_msg_view + "','" + mail_conv_view + "','" + mail_msg_view_2 + "','" + mail_conv_view_2 + "','"
					+ click_sent + "','" + click_trash + "','" + click_inbox + "','" + click_drafts + "','" + click_junk + "','"
					+ click_sent_2 + "','" + click_trash_2 + "','" + click_inbox_2 + "','" + click_drafts_2 + "','" + click_junk_2 + "')";
			add.execute(sqlstatement);

		} catch (SQLException e) {
			// TODO: use log4j or other loggin system instead
			System.out.println("exception in adding user '" + browser + "','" + version + "','" + login + "','"
					+ open_mail + "','" + open_2ndmail + "','" + dblclick_mail + "','" + dblclick_2ndmail
					+ "','" + mailcompose_1 + "','" + mailcompose_2 + "','" + mailcompose_3
					+ "','" + compose_new_window_1 + "','" + compose_new_window_2 + "','" + goto_calendar + "','" + goto_contacts + "','"
					+ goto_tasks + "','" + goto_docs + "','" + goto_pref + "','" + goto_mail + "','" + goto_calendar_2
					+ "','" + goto_contacts_2 + "','" + goto_tasks_2 + "','" + goto_docs_2 + "','" + goto_pref_2 + "','" + goto_mail_2 + "','"
					+ cal_schedule_tab + "','" + cal_attendees_tab + "','" + cal_locations_tab + "','" + cal_resources_tab + "','" + cal_schedule_tab_2 + "','"
					+ cal_attendees_tab_2 + "','" + cal_locations_tab_2 + "','" + cal_resources_tab_2 + "','"
					+ pref_mail_tab + "','" + pref_compose_tab + "','" + pref_signature_tab + "','" + pref_addressbook_tab + "','" + pref_accounts_tab + "','"
					+ pref_calendar_tab + "','" + pref_shortcuts_tab + "','" + contacts_card_view + "','" + contacts_list_view + "','"
					+ contacts_card_view_2 + "','" + contacts_list_view_2 + "','" + mail_msg_view + "','" + mail_conv_view + "','" + mail_msg_view_2 + "','" + mail_conv_view_2 + "','"
					+ click_sent + "','" + click_trash + "','" + click_inbox + "','" + click_drafts + "','" + click_junk + "','"
					+ click_sent_2 + "','" + click_trash_2 + "','" + click_inbox_2 + "','" + click_drafts_2 + "','" + click_junk_2
					+ " ERROR: " + e.getMessage());


			return false;
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
				}

			}
		}
		return true;

	}


}
