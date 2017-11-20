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
package com.zimbra.qa.selenium.projects.universal.tests.preferences.general;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.universal.core.UniversalCore;
import com.zimbra.qa.selenium.projects.universal.pages.preferences.TreePreferences.TreeItem;
import com.zimbra.qa.selenium.projects.universal.pages.preferences.PagePreferences.Locators;

public class VerifyTimezoneView extends UniversalCore {
	
	public VerifyTimezoneView() {
		
		super.startingPage = app.zPagePreferences;
			
	}

	@Test (description = "Check timezone options in preferences", groups = { "smoke", "L1"  } )
	
	public void VerifyTimezoneView_01() throws HarnessException {
		
		String[] Timezone = { Locators.ZDateline, Locators.zSamoa,
				Locators.zHawaii, Locators.zAlaska, Locators.zPacificTimezone,
				Locators.zBajaCalifornia, Locators.zChihuahuaLaPazMazatlan,
				Locators.zUSCanadaMountain, Locators.zFortNelson,
				Locators.zArizona, Locators.zUSCanadaCentral,
				Locators.zCentralAmerica, Locators.zGuadalajara,
				Locators.zSaskatchewan, Locators.zColombia,
				Locators.zCancunChetumal, Locators.zIndianaEast,
				Locators.zUSCanadaEastern, Locators.ZCaracas,
				Locators.zAsuncion, Locators.zCuiaba,
				Locators.zTurksCaicosIslands, Locators.zGeorgetown,
				Locators.zAtlantic, Locators.zNewfoundland,
				Locators.zArgentina, Locators.zSalvador,
				Locators.zCayenneFortleza, Locators.zGreenland,
				Locators.zMontevideo, Locators.zPacificSouthAmerica,
				Locators.zBrasilia, Locators.zMidAtlantic, Locators.zAzores,
				Locators.zCapeVerde, Locators.zCasablanca, Locators.zMmonrovia,
				Locators.zBritainIrelandPortugal,
				Locators.zCoordiantedUniversalTime,
				Locators.zWestCentralAfrica, Locators.zNamibia,
				Locators.zBelgradeBrtislavaBudapestLjubljanaPrague,
				Locators.zAmsterdamBerlinBernRomeStockholmVienna,
				Locators.zBrusselsCopenhagenMadridParis,
				Locators.zSarajevoSkopjeWarsawZagreb, Locators.zEgypt,
				Locators.zHararePretoria, Locators.zTripoli, Locators.zJordan,
				Locators.zBeirut, Locators.zDamascus, Locators.zJerusalem,
				Locators.zAthensBeirutBucharestIstanbul, Locators.zBucharest,
				Locators.zHelsinkiKyivRigaSofiaTallinnVilnius,
				Locators.zInstanbul, Locators.zKaliningrad, Locators.zNairobi,
				Locators.zIraq, Locators.zKuwaitRiyadh, Locators.zMinsk,
				Locators.zMoscowStPetersburgVolgograd, Locators.zTehran,
				Locators.zBaku, Locators.zAbuDhabiMuscat, Locators.zTbilisi,
				Locators.zYerevan, Locators.zIzhevskSamara,
				Locators.zPortLouis, Locators.zKabul,
				Locators.zIslamabadKarachi, Locators.zTashkent,
				Locators.zEkaterinburg, Locators.zSriJayawardenepuraKotte,
				Locators.zIndianTimezone, Locators.zKathmandu,
				Locators.zAstana, Locators.zDhaka, Locators.zNovosibirsk,
				Locators.zYangon, Locators.zBangkokHanoiJakarta,
				Locators.zKrasnoyarsk,
				Locators.zBeijingChongqingHongKongUrumqi, Locators.zIrkutsk,
				Locators.zKualaLumpurSingapore, Locators.zSingapore,
				Locators.zTaipei, Locators.zUlaanbaatar, Locators.zPerth,
				Locators.zPyongyang, Locators.zKorea, Locators.zJapan,
				Locators.zYakutsk, Locators.zAdelaide, Locators.zDarwin,
				Locators.zMagadan, Locators.zVladivostokMagadan,
				Locators.zBrisbane, Locators.zHobart,
				Locators.zCanberraMelbourneSydney, Locators.zGuamPortMoresby,
				Locators.zChokurdakh, Locators.zBougainvilleStandardTime,
				Locators.zSolomonIslandNewCaledonia,
				Locators.zAnadyrPetropavlovskKamchatsky, Locators.zNewZealand,
				Locators.zFiji, Locators.zSamoa13, Locators.zNukualofa,
				Locators.zKiritmatiIsland };

		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.General);
				
		// Click on timezone pull down
		app.zPagePreferences.zFolderPressPulldown(Button.O_TIMEZONE);
		SleepUtil.sleepSmall();
		ZAssert.assertTrue(app.zPagePreferences.sIsElementPresent(Locators.zUpArrow),"Up Arrow key present");  
		
		for (int i=0; i<=6; i++) {
			ZAssert.assertTrue(app.zPagePreferences.sIsElementPresent(Timezone[i]), "Verify " + Timezone[i] + " timezone present"); 
		}
		
		for (int i=7; i<=Timezone.length-1; i++) {
			System.out.println("Verify " + Timezone[i] + " timezone present");
			app.zPagePreferences.sClick(Locators.zDownArrow);
			ZAssert.assertTrue(app.zPagePreferences.sIsElementPresent(Timezone[i]), "Verify " + Timezone[i] + " timezone present"); 
		}
		
	}
}