/*
 * ***** BEGIN LICENSE BLOCK *****
 *
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2016 Synacor, Inc.
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
 *
 * ***** END LICENSE BLOCK *****
 */
package com.zimbra.qa.selenium.projects.ajax.tests.preferences.general;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCore;
import com.zimbra.qa.selenium.projects.ajax.pages.preferences.TreePreferences.TreeItem;
import com.zimbra.qa.selenium.projects.ajax.pages.preferences.PagePreferences.Locators;

public class VerifyTimezoneView extends AjaxCore {

	public VerifyTimezoneView() {
		super.startingPage = app.zPagePreferences;
	}


	@Test (description = "Check timezone options in preferences",
			groups = { "bhr" } )

	public void VerifyTimezoneView_01() throws HarnessException {
		
		String SingleQuoteUnicode = "\u2019";
		String[] Timezone = { "GMT -12:00 Dateline",
				"GMT -11:00 Samoa",
				"GMT -10:00 Adak",
				"GMT -10:00 Hawaii",
				"GMT -09:30 Marquesas",
				"GMT -09:00 Alaska",
				"GMT -08:00 US/Canada Pacific",
				"GMT -08:00 Baja California",
				"GMT -07:00 Chihuahua, La Paz, Mazatlan",
				"GMT -07:00 US/Canada Mountain",
				"GMT -07:00 Fort Nelson",
				"GMT -07:00 Arizona",
				"GMT -06:00 US/Canada Central",
				"GMT -06:00 Central America",
				"GMT -06:00 Easter",
				"GMT -06:00 Guadalajara, Mexico City, Monterrey",
				"GMT -06:00 Saskatchewan",
				"GMT -05:00 Port-au-Prince",
				"GMT -05:00 Colombia",
				"GMT -05:00 Cancun, Chetumal",
				"GMT -05:00 Indiana (East)",
				"GMT -05:00 Havana",
				"GMT -05:00 US/Canada Eastern",
				"GMT -04:00 Caracas",
				"GMT -04:00 Asuncion",
				"GMT -04:00 Cuiaba",
				"GMT -04:00 Pacific South America",
				"GMT -04:00 Turks and Caicos Islands",
				"GMT -04:00 Georgetown, La Paz, Manaus, San Juan",
				"GMT -04:00 Atlantic Time (Canada)",
				"GMT -03:30 Newfoundland",
				"GMT -03:00 Argentina",
				"GMT -03:00 Salvador",
				"GMT -03:00 Punta_Arenas",
				"GMT -03:00 Cayenne, Fortaleza",
				"GMT -03:00 Greenland",
				"GMT -03:00 Montevideo",
				"GMT -03:00 Miquelon",
				"GMT -03:00 Brasilia",
				"GMT -03:00 Araguaina",
				"GMT -02:00 Mid-Atlantic",
				"GMT -01:00 Azores",
				"GMT -01:00 Cape Verde Is.",
				"GMT +00:00 Casablanca",
				"GMT +00:00 Monrovia",
				"GMT +00:00 Britain, Ireland, Portugal",
				"GMT/UTC Coordinated Universal Time",
				"GMT +01:00 West Central Africa",
				"GMT +01:00 Namibia",
				"GMT +01:00 Belgrade, Bratislava, Budapest, Ljubljana, Prague",
				"GMT +01:00 Amsterdam, Berlin, Bern, Rome, Stockholm, Vienna",
				"GMT +01:00 Brussels, Copenhagen, Madrid, Paris",
				"GMT +01:00 Sarajevo, Skopje, Warsaw, Zagreb",
				"GMT +02:00 Egypt",
				"GMT +02:00 Harare, Pretoria",
				"GMT +02:00 Tripoli",
				"GMT +02:00 Jordan",
				"GMT +02:00 Beirut",
				"GMT +02:00 Chisinau",
				"GMT +02:00 Damascus",
				"GMT +02:00 Gaza",
				"GMT +02:00 Jerusalem",
				"GMT +02:00 Athens, Beirut, Bucharest, Istanbul",
				"GMT +02:00 Bucharest",
				"GMT +02:00 Helsinki, Kyiv, Riga, Sofia, Tallinn, Vilnius",
				"GMT +02:00 Kaliningrad (RTZ 1)",
				"GMT +03:00 Nairobi",
				"GMT +03:00 Iraq",
				"GMT +03:00 Istanbul",
				"GMT +03:00 Kuwait, Riyadh",
				"GMT +03:00 Minsk",
				"GMT +03:00 Moscow, St. Petersburg, Volgograd (RTZ 2)",
				"GMT +03:30 Tehran",
				"GMT +04:00 Astrakhan",
				"GMT +04:00 Baku",
				"GMT +04:00 Abu Dhabi, Muscat",
				"GMT +04:00 Tbilisi",
				"GMT +04:00 Yerevan",
				"GMT +04:00 Izhevsk, Samara (RTZ 3)",
				"GMT +04:00 Port Louis",
				"GMT +04:30 Kabul",
				"GMT +04:00 Saratov",
				"GMT +05:00 Islamabad, Karachi",
				"GMT +05:00 Tashkent",
				"GMT +05:00 Ekaterinburg (RTZ 4)",
				"GMT +05:30 Sri Jayawardenepura Kotte",
				"GMT +05:30 Chennai, Kolkata, Mumbai, New Delhi",
				"GMT +05:45 Kathmandu",
				"GMT +06:00 Astana",
				"GMT +06:00 Dhaka",
				"GMT +06:00 Omsk",
				"GMT +06:30 Yangon",
				"GMT +07:00 Bangkok, Hanoi, Jakarta",
				"GMT +07:00 Barnaul",
				"GMT +07:00 Hovd",
				"GMT +07:00 Krasnoyarsk (RTZ 6)",
				"GMT +07:00 Novosibirsk (RTZ 5)",
				"GMT +07:00 Tomsk",
				"GMT +08:00 Beijing, Chongqing, Hong Kong, Urumqi",
				"GMT +08:00 Irkutsk (RTZ 7)",
				"GMT +08:00 Kuala Lumpur",
				"GMT +08:00 Singapore",
				"GMT +08:00 Taipei",
				"GMT +08:00 Ulaanbaatar",
				"GMT +08:00 Perth",
				"GMT +08:30 Pyongyang",
				"GMT +08:45 Eucla",
				"GMT +09:00 Chita",
				"GMT +09:00 Korea",
				"GMT +09:00 Japan",
				"GMT +09:00 Yakutsk (RTZ 8)",
				"GMT +09:30 Adelaide",
				"GMT +09:30 Darwin",
				"GMT +10:00 Vladivostok, Magadan (RTZ 9)",
				"GMT +10:00 Brisbane",
				"GMT +10:00 Hobart",
				"GMT +10:00 Canberra, Melbourne, Sydney",
				"GMT +10:00 Guam, Port Moresby",
				"GMT +10:30 Lord_Howe",
				"GMT +11:00 Chokurdakh (RTZ 10)",
				"GMT +11:00 Bougainville Standard Time",
				"GMT +11:00 Magadan",
				"GMT +11:00 Norfolk",
				"GMT +11:00 Solomon Is. / New Caledonia",
				"GMT +11:00 Sakhalin",
				"GMT +12:00 Anadyr, Petropavlovsk-Kamchatsky (RTZ 11)",
				"GMT +12:00 New Zealand",
				"GMT +12:00 Fiji",
				"GMT +12:45 Chatham",
				"GMT +13:00 Samoa",
				"GMT +13:00 Nuku"+SingleQuoteUnicode+"alofa",
				"GMT +14:00 Kiritimati Island" };

		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.General);

		// Click on timezone pull down
		app.zPagePreferences.zFolderPressPulldown(Button.O_TIMEZONE);
		SleepUtil.sleepSmall();
		ZAssert.assertTrue(app.zPagePreferences.sIsElementPresent(Locators.zUpArrow),"Up Arrow key present");

		for (int i=0; i<=Timezone.length-1; i++) {
			System.out.println("Verify " + Timezone[i] + " timezone present");
			ZAssert.assertTrue(app.zPagePreferences.sIsElementPresent(Locators.zTimezonelistXpath+"\""+ Timezone[i] +"\")]" ), "Verify " + Timezone[i] + " timezone present");
		}
	}
}