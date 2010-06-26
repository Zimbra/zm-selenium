package framework.util;

import java.io.File;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import org.apache.commons.configuration.PropertiesConfiguration;

public class ZimbraSeleniumProperties {
	private static ZimbraSeleniumProperties instance = new ZimbraSeleniumProperties();

	private PropertiesConfiguration configProp;
	
	public static ZimbraSeleniumProperties getInstance() {
		return instance;
	}

	private ZimbraSeleniumProperties() {
		init();
	}

	public PropertiesConfiguration getConfigProperties() {
		return configProp;
	}

	private void init() {
		File dir = new File("");

		final String configProperties = "config.properties";

		File file = new File(dir.getAbsolutePath() + File.separator + "conf"
				+ File.separator + configProperties);

		if (!file.exists()) {
			file = findFile(dir.getAbsolutePath(), configProperties);
		}

		if (!file.exists() || !file.getName().contains(configProperties)) {
			ZimbraSeleniumLogger.mLog.error(configProperties
					+ " does not exist!");
			configProp = createDefaultProperties();
		}

		try {
			if (null == configProp)
				configProp = new PropertiesConfiguration(file);
		} catch (Exception e) {
			ZimbraSeleniumLogger.mLog.error("Exception is: " + e);
		}

		String locale = configProp.getString("locale");
		
		configProp.setProperty("zmMsg", ResourceBundle.getBundle(
				"framework.locale.ZmMsg", new Locale(locale)));
		
		configProp.setProperty("zhMsg", ResourceBundle.getBundle(
				"framework.locale.ZhMsg", new Locale(locale)));

		configProp.setProperty("ajxMsg", ResourceBundle.getBundle(
				"framework.locale.AjxMsg", new Locale(locale)));

		configProp.setProperty("i18Msg", ResourceBundle.getBundle(
				"framework.locale.I18nMsg", new Locale(locale)));

		configProp.setProperty("zsMsg", ResourceBundle.getBundle(
				"framework.locale.ZsMsg", new Locale(locale)));

	}

	private PropertiesConfiguration createDefaultProperties() {
		PropertiesConfiguration defaultProp = new PropertiesConfiguration();

		defaultProp.setProperty("browser", "FF3");
		
		defaultProp.setProperty("runMode", "DEBUG");

		defaultProp.setProperty("product", "zcs");

		defaultProp.setProperty("locale", "en_US");

		defaultProp.setProperty("intl", "us");

		defaultProp.setProperty("testdomain", "testdomain.com");

		defaultProp.setProperty("multiWindow", "true");

		defaultProp.setProperty("objectDataFile",
				"projects/zcs/data/objectdata.xml");

		defaultProp.setProperty("testDataFile",
				"projects/zcs/data/testdata.xml");

		defaultProp.setProperty("serverMachineName", "localhost");

		defaultProp.setProperty("serverport", "4444");

		defaultProp.setProperty("mode", "http");

		defaultProp.setProperty("server", "qa60.lab.zimbra.com");

		defaultProp.setProperty("ProvServer", "qa60.lab.zimbra.com");

		defaultProp.setProperty("ZimbraLogRoot", "test-output");

		defaultProp.setProperty("adminName", "admin");

		defaultProp.setProperty("adminPwd", "test123");

		defaultProp.setProperty("small_wait", "1000");

		defaultProp.setProperty("medium_wait", "2000");

		defaultProp.setProperty("long_wait", "4000");

		defaultProp.setProperty("very_long_wait", "10000");

		defaultProp.setProperty("zmMsg", ResourceBundle.getBundle(
				"framework.locale.ZmMsg", new Locale("en_US")));
		
		defaultProp.setProperty("zhMsg", ResourceBundle.getBundle(
				"framework.locale.ZhMsg", new Locale("en_US")));

		defaultProp.setProperty("ajxMsg", ResourceBundle.getBundle(
				"framework.locale.AjxMsg", new Locale("en_US")));

		defaultProp.setProperty("i18Msg", ResourceBundle.getBundle(
				"framework.locale.I18nMsg", new Locale("en_US")));

		defaultProp.setProperty("zsMsg", ResourceBundle.getBundle(
				"framework.locale.ZsMsg", new Locale("en_US")));

		return defaultProp;
	}

	private File findFile(String dir, String name) {
		File file = new File(dir);
		String[] filenames = file.list();
		String filename = "";
		File myfile = null;

		if (null != filenames) {
			for (int i = 0; i < filenames.length; i++) {
				filename = filenames[i];
				if (filename.endsWith(name)) {
					myfile = new File(dir + File.separator + filename);
					break;
				} else {
					myfile = new File(dir + File.separator + filename);
					if (myfile.isDirectory()) {
						myfile = findFile(myfile.getAbsolutePath(), name);
					}
					if (myfile != null && myfile.getName().endsWith(name)
							&& myfile.exists()) {
						break;
					}
				}
			}
		}
		return myfile;
	}

	// FileFilter fileFilter = new FileFilter() {
	// public boolean accept(File file) {
	// return file.isDirectory();
	// }
	// };
	// File[] fl = file.listFiles(fileFilter);;

	// FilenameFilter filenameFilter = new FilenameFilter() {
	// public boolean accept(File file, String fname) {
	// return fname.contains(".");
	// }
	// };
	// String[] fnl = file.list(filenameFilter);

	public static class CurClassGetter extends SecurityManager {
		public Class<?> getCurrentClass() {
			return getClassContext()[1];
		}
	}
}