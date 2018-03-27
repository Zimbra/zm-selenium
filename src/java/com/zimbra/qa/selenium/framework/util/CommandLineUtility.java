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
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

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


// Command line utility class

public class CommandLineUtility {
	private static Logger logger = LogManager.getLogger(CommandLineUtility.class);

	/**
	 * Execute Command line with no STDIN parameter and return the execution status
	 *
	 * @param command
	 *            Command line to be executed
	 * @return (Integer) Execution status code
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static int CmdExec(String command) throws IOException, InterruptedException {
		return CmdExec(command, null);
	}

	/**
	 * Execute Command line and return the execution status
	 *
	 * @param command
	 *            Command line to be executed
	 * @param params
	 *            Parameter to be passed to STDIN
	 * @return (Integer) Execution status code
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static int CmdExec(String command, String[] params) throws IOException, InterruptedException {
		return CmdExec(command, params, false);
	}

	/**
	 * Execute Command line and return the execution status
	 *
	 * @param command
	 *            Command line to be executed
	 * @param params
	 *            Parameter to be passed to STDIN
	 * @param background
	 *            Running in the background process
	 * @return (Integer) Execution status code
	 * @throws IOException
	 * @throws InterruptedException
	 */
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

	/**
	 * Execute command line with no params and return the output as a String
	 *
	 * @param command
	 *            Command line to be executed
	 * @return (String) output from the console
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws HarnessException
	 */
	public static String cmdExecWithOutput(String command) throws IOException, InterruptedException, HarnessException {
		return cmdExecWithOutput(command, null);
	}

	/**
	 * Execute command line with parameters and return the output as a String
	 *
	 * @param command
	 *            Command line to be executed
	 * @param params
	 *            Parameter to be passed to STDIN
	 * @return (String) output from the console
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws HarnessException
	 */
	public static String cmdExecWithOutput(String command, String[] params)
			throws IOException, InterruptedException, HarnessException {
		logger.debug("Executing command: " + command);
		Process process = Runtime.getRuntime().exec(command);

		return _startStreaming(process, params);
	}

	/**
	 * Execute (tokenized) command line with parameters and return the output as a
	 * String
	 *
	 * @param command
	 *            Command line to be executed
	 * @param params
	 *            Parameter to be passed to STDIN
	 * @return (String) output from the console
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws HarnessException
	 */
	public static String cmdExecWithOutput(String[] command, String[] params)
			throws IOException, InterruptedException, HarnessException {
		logger.debug("Executing command: " + Arrays.toString(command));
		Process process = Runtime.getRuntime().exec(command);

		return _startStreaming(process, params);
	}

	/**
	 * Streaming the input and output from the command line execution
	 *
	 * @param process
	 * @param params
	 * @return Aggregated output from the command line execution
	 * @throws IOException
	 * @throws InterruptedException
	 */
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

		String privateKey = null;
		String host = ZimbraAccount.AccountZCS().zGetAccountStoreHost();
		String command = "sudo su - zimbra -c 'zmtotp -a " + email + " -s " + secret + "'";
		String totp = "0";

		try {

			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			JSch jsch = new JSch();

			if (!ConfigProperties.getStringProperty("server.host").endsWith(".zimbra.com")) {
				privateKey = getUserHome() + "/.ssh/id_rsa";
				jsch.addIdentity(privateKey);
			}

			Session session = jsch.getSession(ConfigProperties.getStringProperty("server.user"), host, 22);
			if (ConfigProperties.getStringProperty("server.host").endsWith(".zimbra.com")) {
				session.setPassword(ConfigProperties.getStringProperty("server.password"));
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
			e.printStackTrace();
		}

		return (totp);
	}

	public static ArrayList<String> runCommandOnZimbraServer(String host, String zimbraCommand) {

		String privateKey = null;
		String command = "sudo su - zimbra -c '" + zimbraCommand + "'";
		ArrayList<String> out = null;

		try {

			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			JSch jsch = new JSch();

			if (!ConfigProperties.getStringProperty("server.host").endsWith(".zimbra.com")) {
				privateKey = getUserHome() + "/.ssh/id_rsa";
				jsch.addIdentity(privateKey);
			}

			Session session = jsch.getSession(ConfigProperties.getStringProperty("server.user"), host, 22);
			if (ConfigProperties.getStringProperty("server.host").endsWith(".zimbra.com")) {
				session.setPassword(ConfigProperties.getStringProperty("server.password"));
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
			out = new ArrayList<String>();
			String line;
			while ((line = reader.readLine()) != null) {
				out.add(line);
			}
			System.out.println(out.toString());

			channel.disconnect();
			session.disconnect();

		} catch (Exception e) {
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