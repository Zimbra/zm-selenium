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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

class StreamGobbler extends Thread {
	InputStream is;
	protected static Logger logger = LogManager.getLogger(StreamGobbler.class);
	StringBuilder output = new StringBuilder("");

	StreamGobbler(InputStream is) {
		this.is = is;
	}

	public String getOutput() {
		return this.output.toString();
	}

	public void run() {
		try {
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			while ((line = br.readLine()) != null) {
				this.output.append(line).append("\n");
				logger.info(line);
			}
		} catch (IOException ioe) {
			logger.warn(ioe);
		}
	}
}


public class CommandLineUtility {
	private static Logger logger = LogManager.getLogger(CommandLineUtility.class);

	public static int CmdExec(String command) throws IOException, InterruptedException {
		return CmdExec(command, null);
	}

	public static int CmdExec(String command, String[] params) throws IOException, InterruptedException {
		return CmdExec(command, params, false);
	}

	public static int CmdExec(String command, String[] params, boolean background)
			throws IOException, InterruptedException {
		Process p = Runtime.getRuntime().exec(command);

		StreamGobbler errorGobbler = new StreamGobbler(p.getErrorStream());
		StreamGobbler outputGobbler = new StreamGobbler(p.getInputStream());
		errorGobbler.start();
		outputGobbler.start();

		if (params != null) {
			OutputStream outputStream = p.getOutputStream();
			for (int i = 0; i < params.length; i++) {
				outputStream.write(params[i].getBytes());
				outputStream.flush();
			}
			outputStream.close();
		}
		int exitValue = -1;

		if (!background) {
			exitValue = p.waitFor();
		}

		logger.info(command + " - " + exitValue);
		return (exitValue);
	}

	public static String cmdExecWithOutput(String command) throws IOException, InterruptedException, HarnessException {
		return cmdExecWithOutput(command, null);
	}

	public static String cmdExecWithOutput(String command, String[] params)
			throws IOException, InterruptedException, HarnessException {
		logger.debug("Executing command: " + command);
		Process process = Runtime.getRuntime().exec(command);
		return _startStreaming(process, params);
	}

	public static String cmdExecWithOutput(String[] command, String[] params)
			throws IOException, InterruptedException, HarnessException {
		logger.debug("Executing command: " + Arrays.toString(command));
		Process process = Runtime.getRuntime().exec(command);

		return _startStreaming(process, params);
	}

	private static String _startStreaming(Process process, String[] params) throws IOException, InterruptedException {
		InputStream inputStream = process.getInputStream();
		StreamGobbler errorGobbler = new StreamGobbler(process.getErrorStream());
		StreamGobbler outputGobbler = new StreamGobbler(inputStream);

		errorGobbler.start();
		outputGobbler.start();

		if (params != null) {
			OutputStream outputStream = process.getOutputStream();
			for (int i = 0; i < params.length; i++) {
				outputStream.write(params[i].getBytes());
				outputStream.flush();
			}
			outputStream.close();
		}

		long startTime = Calendar.getInstance().getTimeInMillis();

		while ((Calendar.getInstance().getTimeInMillis() - startTime) < 30000
				&& (errorGobbler.isAlive() || outputGobbler.isAlive())) {
			continue;
		}

		logger.debug("Starting the reader thread");
		process.waitFor();
		String output = outputGobbler.output.toString() + errorGobbler.output.toString();
		return output;
	}

	public static String runCommandOnStoreServerToGetTOTP(String email, String secret) {
		String privateKey = null, host = null, totp = "0", command;
		String zimbraCommand = "zmtotp -a " + email + " -s " + secret;
		host = ConfigProperties.getStringProperty("server.host");

		if (ConfigProperties.getStringProperty("server.zimbrax").equals("true")) {
			command = "kubectl exec -i zmc-mailbox-0 -- /bin/bash -c '/opt/zimbra/bin/" + zimbraCommand + "'";
		} else {
			command = "su - zimbra -c '" + zimbraCommand + "'";
		}

		if (!ConfigProperties.getStringProperty("server.host").endsWith(".zimbra.com")) {
			command = "sudo " + command;
		}

		try {

			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			JSch jsch = new JSch();

			if (!ConfigProperties.getStringProperty("server.host").endsWith(".zimbra.com")) {
				privateKey = getUserHome() + "/.ssh/id_rsa";
				jsch.addIdentity(privateKey);
			}

			Session session = jsch.getSession(ConfigProperties.getServerUser(), host, 22);
			if (ConfigProperties.getStringProperty("server.host").endsWith(".zimbra.com")) {
				session.setPassword(ConfigProperties.getServerPassword());
			}

			session.setConfig(config);
			session.connect();
			System.out.println("Connected");
			System.out.println(command);

			Channel channel = session.openChannel("exec");
			((ChannelExec) channel).setCommand(command);
			channel.setInputStream(null);
			((ChannelExec) channel).setErrStream(System.err);

			InputStream in = channel.getInputStream();
			channel.connect();
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			StringBuilder out = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				out.append(line);
			}
			System.out.println(out.toString());

			totp = out.toString();
			totp = totp.replaceAll("\\D+", "");
			channel.disconnect();
			session.disconnect();
			System.out.println(totp);

		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}

		return (totp);
	}

	public static ArrayList<String> runCommandOnZimbraServer(String host, String zimbraCommand) {
		String privateKey = null;
		String command = null;

		if (ConfigProperties.getStringProperty("server.zimbrax").equals("true")) {
			command = "kubectl exec -i zmc-mailbox-0 -- /bin/bash -c '/opt/zimbra/bin/" + zimbraCommand + "'";
		} else if (zimbraCommand.contains("sh -c")) {
			command = zimbraCommand;
		} else {
			command = "su - zimbra -c '" + zimbraCommand + "'";
		}

		if (!ConfigProperties.getStringProperty("server.host").endsWith(".zimbra.com")) {
			command = "sudo " + command;
		}

		ArrayList<String> out = null;
		try {

			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			JSch jsch = new JSch();

			if (!ConfigProperties.getStringProperty("server.host").endsWith(".zimbra.com")) {
				privateKey = getUserHome() + "/.ssh/id_rsa";
				jsch.addIdentity(privateKey);
			}

			Session session = jsch.getSession(ConfigProperties.getServerUser(), host, 22);
			if (ConfigProperties.getStringProperty("server.host").endsWith(".zimbra.com")) {
				session.setPassword(ConfigProperties.getServerPassword());
			}

			session.setConfig(config);
			session.connect();
			System.out.println("Connected");

			Channel channel = session.openChannel("exec");
			((ChannelExec) channel).setCommand(command);
			channel.setInputStream(null);
			((ChannelExec) channel).setErrStream(System.err);

			InputStream in = channel.getInputStream();
			channel.connect();
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			out = new ArrayList<String>();
			String line;
			while ((line = reader.readLine()) != null) {
				out.add(line);
			}
			System.out.println(out.toString());

			channel.disconnect();
			session.disconnect();

		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}

		return (out);
	}

	public static String getUserHome () {
		String userHome = null;
		userHome = System.getenv("HOME");
		if (userHome == null || userHome == "") {
			userHome = System.getProperty("user.home");
		}
		return userHome;
	}
}