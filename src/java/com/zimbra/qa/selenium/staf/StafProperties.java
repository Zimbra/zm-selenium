/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2012, 2013, 2014, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.staf;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Matt Rhoades
 *
 * StafProperties is used to generate config.properties specific to this STAF invocation.
 * The constructor reads the default conf/config.properties file, saves it to a Properties
 * object, then during save() will write the Properties to a temporary config.properties
 * file used by STAF for execution.
 *
 * When using STAF, the Zimbra server is set according to the arguments passed to STAF
 *
 */
public class StafProperties {
    private static Logger logger = LogManager.getLogger(StafProperties.class);

    private static final String ConfigPropertiesComments = "Auto Generated By STAF";

    protected Properties properties = null;
    protected String propertiesFilename = null;

    public StafProperties(String filename) throws FileNotFoundException, IOException {
    	logger.info("New StafProperties from "+ filename);

    	properties = new Properties();

    	FileReader reader = null;
    	try {

    		reader = new FileReader(filename);
        	properties.load(reader);

    	} finally {
    		if ( reader != null ) {
    			reader.close();
    			reader = null;
    		}
    	}
    }

    public String setProperty(String key, String value) {
    	String original = (String)properties.get(key);
    	properties.setProperty(key, value);
    	return (original);
    }

    public String save(String foldername) throws IOException {
    	propertiesFilename = foldername + "/" + System.currentTimeMillis() + "config.properties";

    	File f = new File(propertiesFilename);
    	f.getParentFile().mkdirs();

    	FileWriter writer = null;
    	try {

    		writer = new FileWriter(propertiesFilename);
        	properties.store(writer, ConfigPropertiesComments);

    	} finally {
    		if ( writer != null ) {
    			writer.close();
    			writer = null;
    		}
    	}

    	return (propertiesFilename);
    }
}
