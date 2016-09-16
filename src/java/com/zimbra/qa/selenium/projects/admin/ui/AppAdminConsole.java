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
package com.zimbra.qa.selenium.projects.admin.ui;

import com.zimbra.qa.selenium.framework.ui.AbsApplication;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.framework.util.ZimbraAdminAccount;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;


/**
 * This class defines the Admin Console application
 * @author Matt Rhoades
 *
 */
public class AppAdminConsole extends AbsApplication {

	
	// Login page
	public PageLogin					zPageLogin = null;
	
	// General pages (top menu, overview, search
	public PageMain						zPageMain = null;
	public PageSearchResults			zPageSearchResults = null;

	// Addresses
	public PageManageAccounts			zPageManageAccounts = null;
	public PageEditAccount				zPageEditAccount = null;
	public PageManageAliases			zPageManageAliases = null;
	public PageEditAlias				zPageEditAlias = null;
	public PageManageDistributionLists	zPageManageDistributionList = null;
	public PageEditDistributionList		zPageEditDistributionList = null;
	public PageManageResources			zPageManageResources = null;
	public PageEditResource				zPageEditResource = null;
	
	// Configuration
	public PageManageCOS				zPageManageCOS = null;
	public PageEditCOS					zPageEditCOS = null;
	public PageManageDomains			zPageManageDomains = null;
	public PageEditDomain				zPageEditDomain = null;
	public PageManageServers			zPageManageServers = null;
	public PageEditServer				zPageEditServer = null;
	public PageManageZimlets			zPageManageZimlets = null;
	public PageEditZimlet				zPageEditZimlet = null;
	public PageManageAdminExtensions	zPageManageAdminExtensions = null;
	public PageEditAdminExtension		zPageEditAdminExtension = null;
	public PageManageGlobalSettings		zPageManageGlobalSettings = null;
	public PageManageLicense        	zPageManageLicense = null;
	public PageManageRights				zPageManageRights = null;
	public PageManageACL			    zPageManageACL = null;
	public PageManageVoiceChatService	zPageManageVoiceChatService = null;
	public PageManageMTA 				zPageManageMTA = null;
	
	// Monitoring
	public PageManageServerStatus		zPageManageServerStatus = null;
	public PageManageServerStats		zPageManageServerStats = null;
	public PageManageServerStatistics	zPageManageServerStatistics = null;
	public PageManageAdvancedStatistics	zPageManageAdvancedStatistics = null;
	public PageManageMessageCount		zPageManageMessageCount = null;
	public PageManageMessageVolume		zPageManageMessageVolume = null;
	public PageManageAntiSpamAnitVirusActivity	zPageManageAntispamAntiVirusActivity = null;
	public PageManageMobileSyncStatistics	zPageManageMobileSyncStatistics = null;
	public PageManageMailQueues			zPageManageMailQueues = null;
	
	
	// Tools
	
	public PageEditMailQueue			zPageEditMailQueue = null;
	public PageManageAccountMigration	zPageManageAccountMigration = null;
	public PageManageSearch				zPageManageSearch = null;
	public PageManageCertificates		zPageManageCertificates = null;
	public PageManageClientUpload		zPageManageClientUpload = null;
	public PageEditCertificate			zPageEditCertificate = null;
	public PageManageSoftwareUpdates	zPageManageSoftwareUpdates = null;
	public PageManageSearchMail			zPageManageSearchMail = null;
	public PageEditSearchTask			zPageEditSearchTask = null;
	public PageManageBackups			zPageManageBackups = null;
	public PageEditBackup				zPageEditBackup = null;
	
	// Searches
	public PageEditSearch				zPageEditSearch = null;
	
	// Downloads page (http://server.com/zimbra/downloads/index.html
	public PageDownloads				zPageDownloads = null;
	public PageManageCofigureGAL			zPageManageCofigureGAL = null;
	public PageManageCofigureAuthentication			zPageManageCofigureAuthentication = null;
	public PageManageMigrationCoexistance			zPageManageMigrationCoexistance = null;

	public AppAdminConsole() {
		super();
		
		logger.info("new " + AppAdminConsole.class.getCanonicalName());

		
		// Login page
		
		zPageLogin = new PageLogin(this);
		pages.put(zPageLogin.myPageName(), zPageLogin);
		
		
		// Main pages
		
		zPageMain = new PageMain(this);
		pages.put(zPageMain.myPageName(), zPageMain);
		
		zPageSearchResults = new PageSearchResults(this);
		pages.put(zPageSearchResults.myPageName(), zPageSearchResults);
		
		
		// Addresses
		
		zPageManageAccounts = new PageManageAccounts(this);
		pages.put(zPageManageAccounts.myPageName(), zPageManageAccounts);
		
		zPageEditAccount = new PageEditAccount(this);
		pages.put(zPageEditAccount.myPageName(), zPageEditAccount);
		
		zPageManageAliases = new PageManageAliases(this);
		pages.put(zPageManageAliases.myPageName(), zPageManageAliases);
		
		zPageEditAlias = new PageEditAlias(this);
		pages.put(zPageEditAlias.myPageName(), zPageEditAlias);
		
		zPageManageDistributionList = new PageManageDistributionLists(this);
		pages.put(zPageManageDistributionList.myPageName(), zPageManageDistributionList);
		
		zPageEditDistributionList = new PageEditDistributionList(this);
		pages.put(zPageEditDistributionList.myPageName(), zPageEditDistributionList);
		
		zPageManageResources = new PageManageResources(this);
		pages.put(zPageManageResources.myPageName(), zPageManageResources);
		
		zPageEditResource = new PageEditResource(this);
		pages.put(zPageEditResource.myPageName(), zPageEditResource);
		
		zPageManageLicense = new PageManageLicense(this);
		pages.put(zPageManageLicense.myPageName(), zPageManageLicense);
		
		// Configuration pages
		
		zPageManageCOS = new PageManageCOS(this);
		pages.put(zPageManageCOS.myPageName(), zPageManageCOS);
		
		zPageEditCOS = new PageEditCOS(this);
		pages.put(zPageEditCOS.myPageName(), zPageEditCOS);
		
		zPageManageDomains = new PageManageDomains(this);
		pages.put(zPageManageDomains.myPageName(), zPageManageDomains);
		
		zPageEditDomain = new PageEditDomain(this);
		pages.put(zPageEditDomain.myPageName(), zPageEditDomain);
		
		zPageManageServers = new PageManageServers(this);
		pages.put(zPageManageServers.myPageName(), zPageManageServers);
		
		zPageEditServer = new PageEditServer(this);
		pages.put(zPageEditServer.myPageName(), zPageEditServer);
		
		zPageManageZimlets = new PageManageZimlets(this);
		pages.put(zPageManageZimlets.myPageName(), zPageManageZimlets);
		
		zPageEditZimlet = new PageEditZimlet(this);
		pages.put(zPageEditZimlet.myPageName(), zPageEditZimlet);
		
		zPageManageAdminExtensions = new PageManageAdminExtensions(this);
		pages.put(zPageManageAdminExtensions.myPageName(), zPageManageAdminExtensions);
		
		zPageEditAdminExtension = new PageEditAdminExtension(this);
		pages.put(zPageEditAdminExtension.myPageName(), zPageEditAdminExtension);
		
		zPageManageGlobalSettings = new PageManageGlobalSettings(this);
		pages.put(zPageManageGlobalSettings.myPageName(), zPageManageGlobalSettings);
		
		zPageManageRights = new PageManageRights(this);
		pages.put(zPageManageRights.myPageName(), zPageManageRights);
		
		zPageManageACL = new PageManageACL(this);
		pages.put(zPageManageACL.myPageName(), zPageManageACL);
		
		zPageManageVoiceChatService = new PageManageVoiceChatService(this);
		pages.put(zPageManageVoiceChatService.myPageName(), zPageManageVoiceChatService);

		zPageManageSearch = new PageManageSearch(this);
		pages.put(zPageManageSearch.myPageName(), zPageManageSearch);
		
		zPageManageMTA = new PageManageMTA(this);
		pages.put(zPageManageMTA.myPageName(), zPageManageMTA);

		
		// Monitoring

		zPageManageServerStatus = new PageManageServerStatus(this);
		pages.put(zPageManageServerStatus.myPageName(), zPageManageServerStatus);
		
		zPageManageServerStats = new PageManageServerStats(this);
		pages.put(zPageManageServerStats.myPageName(), zPageManageServerStats);

		zPageManageServerStatistics = new PageManageServerStatistics(this);
		pages.put(zPageManageServerStatistics.myPageName(), zPageManageServerStatistics);

		zPageManageAdvancedStatistics = new PageManageAdvancedStatistics(this);
		pages.put(zPageManageAdvancedStatistics.myPageName(), zPageManageAdvancedStatistics);

		zPageManageMessageCount = new PageManageMessageCount(this);
		pages.put(zPageManageMessageCount.myPageName(), zPageManageMessageCount);

		zPageManageMessageVolume = new PageManageMessageVolume(this);
		pages.put(zPageManageMessageVolume.myPageName(), zPageManageMessageVolume);

		zPageManageAntispamAntiVirusActivity = new PageManageAntiSpamAnitVirusActivity(this);
		pages.put(zPageManageAntispamAntiVirusActivity.myPageName(), zPageManageAntispamAntiVirusActivity);

		zPageManageMobileSyncStatistics = new PageManageMobileSyncStatistics(this);
		pages.put(zPageManageMobileSyncStatistics.myPageName(), zPageManageMobileSyncStatistics);

		
		// Tools
		
		zPageManageServerStats = new PageManageServerStats(this);
		pages.put(zPageManageServerStats.myPageName(), zPageManageServerStats);

		zPageManageMailQueues = new PageManageMailQueues(this);
		pages.put(zPageManageMailQueues.myPageName(), zPageManageMailQueues);

		zPageEditMailQueue = new PageEditMailQueue(this);
		pages.put(zPageEditMailQueue.myPageName(), zPageEditMailQueue);

		zPageManageAccountMigration = new PageManageAccountMigration(this);
		pages.put(zPageManageAccountMigration.myPageName(), zPageManageAccountMigration);

		zPageManageCertificates = new PageManageCertificates(this);
		pages.put(zPageManageCertificates.myPageName(), zPageManageCertificates);
		
		zPageManageClientUpload = new PageManageClientUpload(this);
		pages.put(zPageManageClientUpload.myPageName(), zPageManageClientUpload);

		zPageEditCertificate = new PageEditCertificate(this);
		pages.put(zPageEditCertificate.myPageName(), zPageEditCertificate);

		zPageManageSoftwareUpdates = new PageManageSoftwareUpdates(this);
		pages.put(zPageManageSoftwareUpdates.myPageName(), zPageManageSoftwareUpdates);

		zPageManageSearchMail = new PageManageSearchMail(this);
		pages.put(zPageManageSearchMail.myPageName(), zPageManageSearchMail);

		zPageEditSearchTask = new PageEditSearchTask(this);
		pages.put(zPageEditSearchTask.myPageName(), zPageEditSearchTask);

		zPageManageBackups = new PageManageBackups(this);
		pages.put(zPageManageBackups.myPageName(), zPageManageBackups);

		zPageEditBackup = new PageEditBackup(this);
		pages.put(zPageEditBackup.myPageName(), zPageEditBackup);

		
		// Searches
		
		zPageEditSearch = new PageEditSearch(this);
		pages.put(zPageEditSearch.myPageName(), zPageEditSearch);


		// Downloads
		
		zPageDownloads = new PageDownloads(this);
		pages.put(zPageDownloads.myPageName(), zPageDownloads);
		
		zPageManageCofigureGAL = new PageManageCofigureGAL(this);
		pages.put(zPageManageCofigureGAL.myPageName(), zPageManageCofigureGAL);
		
		zPageManageCofigureAuthentication = new PageManageCofigureAuthentication(this);
		pages.put(zPageManageCofigureAuthentication.myPageName(), zPageManageCofigureAuthentication);
		
		zPageManageMigrationCoexistance = new PageManageMigrationCoexistance(this);
		pages.put(zPageManageMigrationCoexistance.myPageName(), zPageManageMigrationCoexistance);
		
		
	}


	@Override
	public boolean zIsLoaded() throws HarnessException {
		// TODO: how to determine if the current browser app is the AdminConsole
		// Maybe check the current URL?
		return (true);
	}

	@Override
	public String myApplicationName() {
		return ("Admin Console");
	}

	public ZimbraAccount zSetActiveAcount(ZimbraAccount account) throws HarnessException {
		// Should we throw an exception if the account is not a ZimbraAdminAccount?
		return (super.zSetActiveAcount(account));
	}


	public void provisionAuthenticateDA() throws HarnessException {
		// Create a new AdminAccount
		ZimbraAdminAccount accounta = new ZimbraAdminAccount("admin"+ ConfigProperties.getUniqueString() + "@" + ConfigProperties.getStringProperty("testdomain"));

		accounta.provisionDA(accounta.EmailAddress);
		  
		ZimbraAdminAccount.GlobalAdmin().soapSend(
			"<ModifyAccountRequest xmlns='urn:zimbraAdmin'>"
	  +   " <id>"+ accounta.ZimbraId+"</id>"
	   +   "<a n='zimbraAdminConsoleUIComponents'>accountListView</a>"
	  +    "<a n='zimbraAdminConsoleUIComponents'>downloadsView</a>"
	  +    "<a n='zimbraAdminConsoleUIComponents'>DLListView</a>"
	  +    "<a n='zimbraAdminConsoleUIComponents'>aliasListView</a>"
	  +    "<a n='zimbraAdminConsoleUIComponents'>resourceListView</a>"
	  +    "<a n='zimbraAdminConsoleUIComponents'>saveSearch</a>"
	  +    "<a n='zimbraAdminConsoleUIComponents'>domainListView</a>"
	  +  "</ModifyAccountRequest>");
		
		
		ZimbraAdminAccount.GlobalAdmin().soapSend(
				"<FlushCacheRequest  xmlns='urn:zimbraAdmin'>" +
					"<cache type='galgroup'/>" +
	        	"</FlushCacheRequest>");
		
		this.zPageMain.logout();		
		accounta.authenticate();
	
		// Login
		this.zPageLogin.login(accounta);

	}
}

