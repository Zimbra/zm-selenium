package com.zimbra.qa.selenium.projects.admin.ui;

import com.zimbra.qa.selenium.framework.ui.AbsApplication;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;


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
	public PageManageRights				zPageManageRights = null;
	public PageManageGlobalACL			zPageManageACL = null;
	public PageManageVoiceChatService	zPageManageVoiceChatService = null;
	
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
		
		zPageManageACL = new PageManageGlobalACL(this);
		pages.put(zPageManageACL.myPageName(), zPageManageACL);
		
		zPageManageVoiceChatService = new PageManageVoiceChatService(this);
		pages.put(zPageManageVoiceChatService.myPageName(), zPageManageVoiceChatService);

		
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

	protected ZimbraAccount zSetActiveAcount(ZimbraAccount account) throws HarnessException {
		// Should we throw an exception if the account is not a ZimbraAdminAccount?
		return (super.zSetActiveAcount(account));
	}


	
}
