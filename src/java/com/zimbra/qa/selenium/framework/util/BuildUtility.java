package com.zimbra.qa.selenium.framework.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import com.zimbra.qa.selenium.framework.util.OperatingSystem;
import com.zimbra.qa.selenium.framework.util.OperatingSystem.OsType;

public class BuildUtility {
   private static final StringBuilder _buildBaseUrl = new StringBuilder("http://zre-matrix.eng.vmware.com");
   private static final String _buildUrl = new StringBuilder(_buildBaseUrl).append("/cgi-bin/build/builds.cgi").toString();
   protected static Logger logger = LogManager.getLogger(BuildUtility.class);
   public class Build {
      String buildUrl;
      String buildName;
      String branch;
      String arch;
      String osType;
   }

   public enum PRODUCT_NAME {
      NETWORK,
      ZDESKTOP,
      FOSS,
      ISYNC,
      ZCO,
      APPLIANCE
   }
   public enum BRANCH {
      GNR,
      HELIX, ZDESKTOP_700,
      MAIN,
      ZCB_7,
      ZCB_MAIN
   }
   public enum ARCH {
      UBUNTU10_64, RHEL5_64, RHEL4_64, MACOSX_X86_10_6, RHEL4, SLES10_64, SLES11_64, WINDOWS
   }

   public static class URLUtil {
      /*
       * Returns a String containing the content of a web page
       */
      public static String get(String url) throws Exception {
         final int NUM_RETRY = 10;
         Exception ex = null;

         int i = 0;
         while (i++ < NUM_RETRY) {
            try {
               URL url_ = new URL(url);

               HttpURLConnection conn = (HttpURLConnection)url_.openConnection();
               conn.setConnectTimeout(90000);
               conn.setReadTimeout(420000);
               conn.setInstanceFollowRedirects( false );
               conn.setRequestProperty( "User-agent", "Zeppelin Automation" );
               InputStream errorStream = conn.getErrorStream( );
               if ( errorStream != null ) {
                  throw new IOException(inputStreamToString(errorStream, false));
               } else {
                  return inputStreamToString((InputStream)conn.getContent(), false);
               }
            } catch (IOException e) {
               logger.error("Error retrieving web page: ");
               logger.error(url);
               logger.error(e);
               if( i < NUM_RETRY ) {
                  logger.debug("Sleeping for 1 seconds");
                  Thread.sleep(1000);
               }
            }
         }
         throw new IOException("Error retrieving web page. " + ex + ": " + url);
      }

      /*
       * Converts an InputStream to a String.
       */
      public static String inputStreamToString(InputStream stream, boolean printStream) throws IOException,NullPointerException {
         BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
         StringBuilder sb = new StringBuilder();
         String line = null;
         while ((line = reader.readLine()) != null) {
            if( printStream ) {
               logger.info(line);
            }
            sb.append(line + "\n");
         }
         try {
            stream.close();
         } catch (IOException e) {
            logger.info("Error closing stream: "+e.toString());
         }
         String s = sb.toString();
         if (s == null) {
            throw new NullPointerException("Error converting InputStream to string");
         }
         return s;
      }
   }

   /**
    * Return the HTML Information of a given URL
    * @param url URL link, from which the HTML information will be obtained
    * @return
    * @throws HarnessException
    */
   private static String _getHtmlInfo(String url) throws HarnessException {
      logger.debug("Complete URL query is: " + url);
      String results;
      try {
         results = URLUtil.get(url);
      } catch (Exception e) {
         throw new HarnessException("ERROR: Unable to retrieve page: " + url, e);
      }
      logger.debug("Build query result is: " + results);

      return results;
      // For example: wget -m -nd -P C:\Jeff_Test\ http://zre-matrix.eng.vmware.com/links/WINDOWS/HELIX/20110110070101_ZDESKTOP/ZimbraBuild/i386/zdesktop_7_0_dev-helix_b10684_win32.msi
   }

   /**
    * Get downloadable-build URL from the provided specific build URL from the build site
    * @param buildUrl Build URL obtained from Build object
    * @return Downloadable-build URL
    * @throws HarnessException
    */
   private static String _getDownloadableBuildUrl(String buildUrl) throws HarnessException {
      String[] lines = _getHtmlInfo(buildUrl).split("\n");
      OsType os = OperatingSystem.getOSType();
      String fileExtension = null;

      switch (os) {
      case WINDOWS: case WINDOWS_XP:
         fileExtension = ".msi";
         break;
      case LINUX:
         fileExtension = ".tgz";
         break;
      case MAC:
         fileExtension = ".dmg";
         break;
      }

      for (int i = 0; i < lines.length; i++) {
         if (lines[i].contains(fileExtension)) {
            // Plus 6 because of these characters --> href="
            int startPos = lines[i].indexOf("href=\"") + 6;
            return buildUrl + lines[i].substring(startPos, lines[i].indexOf("\"",startPos));
         }
      }
      return null;
   }

   /**
    * Downloads the build from build URL based on the product's name, branch, and arch
    * @param downloadDest Destination where to put the file in.
    * @param productName Product's name
    * @param branch Branch's name
    * @param arch Architecture Type
    * @return (String) Downloaded file path
    * @throws HarnessException
    * @throws SAXException
    * @throws IOException
    */
   public static String downloadLatestBuild(String downloadDest, PRODUCT_NAME productName, BRANCH branch, ARCH arch)
   throws HarnessException, SAXException, IOException {
      BufferedOutputStream bout = null;
      FileOutputStream fos = null;
      BufferedInputStream in = null;
      int bufferedSize = 1024;
      String output = null;

      File file = new File(downloadDest);
      if (!file.exists()) {
         logger.info("Creating directory " + downloadDest + "...");
         file.mkdir();
      }

      try {
         logger.debug("Getting the builds from Build web");
         Build build = _buildOutputFilter(productName, branch, arch)[0];
         logger.debug("Getting the builds from Build web successful");

         String url = _getDownloadableBuildUrl(build.buildUrl);
         logger.info("Downloadable build URL is: " + url);

         OperatingSystem.OsType osType = OperatingSystem.getOSType();
         logger.debug("OS Type is: " + osType.toString());

         String fileSeparator = null;
         switch (osType) {
         case WINDOWS: case WINDOWS_XP:
            fileSeparator = "\\";
            break;
         case LINUX: case MAC:
            fileSeparator = "/";
            break;
         }

         if (!downloadDest.trim().endsWith(fileSeparator)) {
            downloadDest = downloadDest.trim() + fileSeparator;
         }

         logger.debug("Now downloading the file to location: " + downloadDest + " ...");
         in = new BufferedInputStream(new java.net.URL(url).openStream());

         String [] temp = url.split("/");
         logger.debug("Downloaded file name is: " + temp[temp.length - 1]);
         output = downloadDest + temp[temp.length - 1];
         fos = new FileOutputStream(downloadDest + temp[temp.length - 1]);
         bout = new BufferedOutputStream(fos, bufferedSize);

         logger.debug("Downloading...");

         byte [] data = new byte[1024];
         int byteRead = 0;
         while ((byteRead = in.read(data, 0 , bufferedSize)) != -1) {
            bout.write(data, 0, byteRead);
         }

      } catch (IOException ioe){
         ioe.printStackTrace();
      } catch (ParserConfigurationException pce) {
         pce.printStackTrace();
      } finally {
         bout.flush();
         bout.close();
         fos.flush();
         fos.close();
         in.close();
      }
      return output;
   }

   /**
    * Filter only the builds based on the product name, branch, and arch
    * @param productName Name of the product
    * @param branch Branch name
    * @param arch Arch name
    * @return Build objects of the matched product's name, branch's name, and arch's name
    * @throws SAXException
    * @throws IOException
    * @throws ParserConfigurationException
    * @throws HarnessException
    */
   private static Build[] _buildOutputFilter(PRODUCT_NAME productName, BRANCH branch, ARCH arch) throws SAXException, IOException, ParserConfigurationException, HarnessException {
      String[] outputLines = _getHtmlInfo(_buildUrl).split("\n");
      int numOfElements = 0;
      Vector<Build> parsedOutput = new Vector<Build> ();
      logger.debug("outputString length is: " + outputLines.length);
      BuildUtility buildUtil = new BuildUtility();
      for (int i = 0; i < outputLines.length; i++) {
         if (outputLines[i].startsWith("<TR BGCOLOR") &&
             outputLines[i].contains("logs") &&
             outputLines[i].contains(productName.toString()) &&
             outputLines[i].contains(branch.toString().replace('_', '-') + "<") &&
             outputLines[i].contains(arch.toString())) {
            numOfElements ++;
            Build temp = buildUtil.new Build();

            int currentReadPos = 0;
            for (int j = 0; j < 7; j++) {
               currentReadPos = outputLines[i].indexOf("<TD", currentReadPos);
               currentReadPos = outputLines[i].indexOf(">", currentReadPos);
               int tempReadPos = outputLines[i].indexOf("</TD", currentReadPos);
               String currentElement = outputLines[i].substring(currentReadPos + 1, tempReadPos);
               switch (j){
               //First element contains Build Name and link
               case 0:
                  temp.buildUrl = new StringBuilder(_buildBaseUrl).append(currentElement.substring(
                        currentElement.indexOf("\"") + 1, currentElement.indexOf("\">"))).append("/").toString();
                  temp.buildName = currentElement.substring(
                        currentElement.indexOf(">") + 1, currentElement.indexOf("</A"));
                  break;

               // Second element contains branch
               case 1:
                  temp.branch = currentElement;
                  break;

               // Third element contains Arch
               case 2:
                  temp.osType = currentElement;
                  break;

               default:
                  // Nothing to do for now
               }
            }

            parsedOutput.add(temp);
            
         }
      }

      logger.debug("numOfElements is: " + numOfElements);
      Build [] output = new Build[parsedOutput.size()];

      for (int j = 0; j < output.length; j++) {
         output[j] = parsedOutput.remove(0);
      }

      return output;
   }
}
