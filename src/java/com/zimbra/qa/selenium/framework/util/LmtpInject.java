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
package com.zimbra.qa.selenium.framework.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.zimbra.common.lmtp.LmtpClient;
import com.zimbra.common.lmtp.LmtpClientException;
import com.zimbra.common.lmtp.LmtpProtocolException;
import com.zimbra.common.util.ByteUtil;
import com.zimbra.qa.selenium.framework.core.ExecuteHarnessMain;
import com.zimbra.qa.selenium.framework.util.staf.Stafpostqueue;

/**
 * @author Matt Rhoades
 *
 */
public class LmtpInject {
	protected static Logger logger = LogManager.getLogger(LmtpInject.class);

	/**
	 * Inject a mime file to an email addresses
	 * @param recipient an email addresses
	 * @param mime the mime file or directory of files to inject
	 * @throws HarnessException
	 */
	public static void injectFile(String recipient, File mime) throws HarnessException {

		// conver the recipient to an array
		injectFile(
				Arrays.asList(recipient.split(",")),
				mime
		);

	}

	/**
	 * Inject a mime file to a list of email addresses
	 * @param recipients an array of email addresses
	 * @param mime the mime file or directory of files to inject
	 * @throws HarnessException
	 */
	public static void injectFile(List<String> recipients, File mime) throws HarnessException {

		// Use default sender
		injectFile(
				recipients,
				"foo@example.com",
				mime);

	}

	/**
	 * Inject a mime file to a list of email addresses
	 * @param recipients an array of email addresses
	 * @param sender the sender of the message
	 * @param mime the mime file or directory of files to inject
	 * @throws HarnessException
	 */
	public static void injectFile(List<String> recipients, String sender, File mime) throws HarnessException  {

		try {

			try {

				injectFolder(recipients, sender, mime);

			} finally {

				Stafpostqueue sp = new Stafpostqueue();
				sp.waitForPostqueue();

			}

		} catch (IOException e) {
			throw new HarnessException("Unable to read mime file "+ mime.getAbsolutePath(), e);
		} catch (LmtpProtocolException e) {
			throw new HarnessException("Unable to inject mime file "+ mime.getAbsolutePath(), e);
		} catch (LmtpClientException e) {
            throw new HarnessException("Unable to inject mime file "+ mime.getAbsolutePath(), e);
        }

	}

	protected static void injectFolder(List<String> recipients, String sender, File mime) throws IOException, LmtpProtocolException,  LmtpClientException {

		if ( mime.isFile() ) {

			// Inject a single file
			inject(recipients, sender, mime);

		} else if ( mime.isDirectory() ) {

			for (File f : mime.listFiles()) {

				injectFolder(recipients, sender, f);

			}

		} else {

			// Unknown File type
			logger.warn("MIME file was not file or directory.  Skipping. " + mime.getAbsolutePath());

		}

	}


	protected static void inject(List<String> recipients, String sender, File mime) throws IOException, LmtpProtocolException, LmtpClientException {
		logger.info("LMTP: to: "+ recipients.toString());
		logger.info("LMTP: from: "+ sender);
		logger.info("LMTP: filename: "+ mime.getAbsolutePath());

		long length = mime.length();

		logger.info( length > 2000 ? "LMTP: large mime" : "LMTP:\n" + new String(ByteUtil.getContent(mime)));

		LogManager.getLogger(ExecuteHarnessMain.TraceLoggerName).trace(
				"Inject using LMTP: " +
				" to:"+ recipients.toString() +
				" from:"+ sender +
				" filename:"+ mime.getAbsolutePath()
		);


		LmtpClient lmtp = null;
		try {

			lmtp = new LmtpClient(
					ConfigProperties.getStringProperty(ConfigProperties.getLocalHost() + ".store.host", ConfigProperties.getStringProperty("store.host")),
					7025);

			lmtp.sendMessage(new FileInputStream(mime), recipients.toArray(new String[recipients.size()]), sender, "Selenium", length);

		} finally {

			// TODO: do we need to close the FileInputStream?

			if (lmtp != null ) {
				lmtp.close();
				lmtp = null;
			}

		}

	}

}
