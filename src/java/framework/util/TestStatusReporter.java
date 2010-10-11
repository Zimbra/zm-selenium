package framework.util;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;
import org.testng.xml.XmlSuite;

import framework.core.ClientSessionFactory;

public class TestStatusReporter extends TestListenerAdapter {  
   private PrintWriter    output=null;
   
   private ArrayList<String> failArray = new ArrayList<String>();
   private ArrayList<String> skipArray = new ArrayList<String>();
   private ArrayList<String> passArray = new ArrayList<String>();
   private ArrayList<String> confFailArray = new ArrayList<String>();
   private ArrayList<String> confSkipArray = new ArrayList<String>();
  
   private ArrayList<String> failReasonArray = new ArrayList<String>();
   private ArrayList<String> skipReasonArray = new ArrayList<String>();
   private ArrayList<String> confFailReasonArray = new ArrayList<String>();
   private ArrayList<String> confSkipReasonArray = new ArrayList<String>();

   private String path="";
   
   private PrintStream ps=null;
   private ByteArrayOutputStream baos;
   
   public static final String failDir="fail";
   public static final String skipDir="skip";
   public static final String passDir="pass";
   public static final String confFailDir="confFail";
   public static final String confSkipDir="confSkip";
   public static final String inProgressDir="inProgress";
   public static final String categoryDir="categories";  
   
   private Date startDate = new Date();
   
   private String appType = "";
   private PrintWriter classInProgressPrintWriter=null;
   private ArrayList<String> categoryArrayList = new ArrayList<String>();
   
   
   private static class GetFromFile{
	   
	    private static ArrayList<Long> longArray = new ArrayList<Long>();
	    private static String currentFileName="";
	    private static RandomAccessFile   raf= null;
	    private static long furthestReadLong=0;
	    
	    public static void setFileName (String fileName){
	    	if (!fileName.equals(currentFileName)){
	    		currentFileName=fileName;
	    		try {
	    		  if (raf != null)  raf.close();
	    		  raf = new RandomAccessFile("src/java/" + fileName,"r");
	    		  longArray.clear();
	    		  furthestReadLong=0;
	    		}
	    		catch (Exception e) {
	    			e.printStackTrace();
	    		}
	    	}
	    }
	    private static String getDoubleLineAt(int lineNumber){
	    	String result="";
	    	if (lineNumber > longArray.size()) {
	    		try {
	    		  //go to the furthest read point of file
	    		  raf.seek(furthestReadLong);
	    		  for (int i=longArray.size(); i<lineNumber; i++){
	                 longArray.add(i,raf.getFilePointer());
	                 result = (i +1)  + " : " + raf.readLine();	                 
	 	            	              	                 
	    		  }
	    		  //2 lines
	    		  if (lineNumber >1) {
	    		    raf.seek(longArray.get(lineNumber-2));
	    		    result = (lineNumber -1) + " : " + raf.readLine() + "<br>\n\t" + lineNumber + " : " + result;
	    		  }
	    		  furthestReadLong  = raf.getFilePointer();
	    		}
	    		catch (IOException e) {
	    			e.printStackTrace();
	    		}
	    	}
	    	else {
	    		long pos=longArray.get(lineNumber -1);
	    		
	    		
	    		try {
	    		  if (lineNumber >1){
	    			raf.seek(longArray.get(lineNumber -2));
		    	    result = (lineNumber -1) + " : " + raf.readLine() + "<br>\n\t" + lineNumber + " : " +raf.readLine();
	    		  }
	    		  else {
	    	        raf.seek(pos);
	    	        result = lineNumber  + " : " + raf.readLine();
	    		  }
	    	      
	    		}
	    		catch (Exception e) {
	    		    e.printStackTrace();	
	    		}
	    	}
	    	
	    	return result;
	    }
	    
	    private static String getLineAt(int lineNumber){
	    	String result="";
	    	if (lineNumber > longArray.size()) {
	    		try {
	    		  //go to the furthest read point of file
	    		  raf.seek(furthestReadLong);
	    		  for (int i=longArray.size(); i<lineNumber; i++){
	                 longArray.add(i,raf.getFilePointer());
	                 result =raf.readLine();	                 
	 	                         
	    		  }
	    		  furthestReadLong  = raf.getFilePointer();
	    		}
	    		catch (IOException e) {
	    			e.printStackTrace();
	    		}
	    	}
	    	else {
	    		long pos=longArray.get(lineNumber -1);
	    		
	    		
	    		try {
	    		    raf.seek(pos);
	    	        result =raf.readLine();	    		  	    	      
	    		}
	    		catch (Exception e) {
	    		    e.printStackTrace();	
	    		}
	    	}
	    	result = "\n  " + lineNumber  + " : " + result;	                 
             
	    	//System.out.println("Get Line At " + lineNumber + " : " + result);
		    return result;
	    }

	    
	    
	    public static String getDoubleLineAt(String fileName, int lineNumber){	    	
	    	setFileName(fileName);
	    	return getDoubleLineAt(lineNumber);	    	
	    }
	    
	    public static String getCode(String fileName, String methodName) {
	    	StringBuffer result= new StringBuffer();
	    	String line="";
	        int lineNumber=1;
	    //	System.out.println("DEBUG GetCodeFor "+ fileName + " " + methodName);
	        setFileName(fileName);
	        
	    	try {

	    	   do {
	    		   line = getLineAt(lineNumber++);
	    		   
	    		   //don't count comment /* //
	    		   if (line.indexOf("/*") != -1) {	    			
		    			  while (line.indexOf("*/") == -1) {
		    				  line = getLineAt(lineNumber++);	    		  	    		  
		    	    	  }
		    	   }		    			

	    		   if (line.indexOf("//") != -1) {
	    			     line = line.substring(0,line.indexOf("//") );
	    		   }
	    		   		
	    	   }	  
	    	   while (! ((line.indexOf(methodName)!= -1) && (line.indexOf("public")!= -1)) );

	    	   result.append(line);
	    	   
	    	   //System.out.println("DEBUG First line: " + line);
	    			   
	    	   //go backward to add additional info till see the end of the previous method
	    	   // assume there is such one exist.
	    	   //TODO: check for comment /*
	    	   String prevLine="";
	    	   
	    	   int i=1;
	    	   int numParen_=0;
	    	   do {
	    		  result.insert(0, prevLine);
	    		  prevLine  = getLineAt(lineNumber - ++i);
	    		  numParen_ = countParen(prevLine,numParen_);	    		  
	    	   }
	    	   
	    	   while ((lineNumber - i >1) && (numParen_ != -1));
	    	   
	    	   
	    	   
	           //TODO read until see the first {
	    	   while (line.indexOf("{") == -1){	    		   
	    		   line = getLineAt(lineNumber++);
	    		   result.append(line);
	    	   }
	    	   
	    	   // >0 if { > }
	    	   int numParen=1;
	
	    	   //read until the #{ and #} balance
	    	   while (numParen != 0) {
	    		  line = getLineAt(lineNumber++);	    		  	    		  
	    		  result.append(line);
	    		  //don't count comment /*
	    		  if (line.indexOf("/*") != -1) {	    			
	    			  while (line.indexOf("*/") == -1) {
	    				  line = getLineAt(lineNumber++);	    		  	    		  
	    	    		  result.append(line);	    	    			    				  
	    			  }
	    		  }
	    		  numParen = countParen(line,numParen);
	           }
	    	   
	           
	    	  
	    	}catch (Exception e) { e.printStackTrace(); }
	    	return result.toString();
	    }
	    
	    
   }
  
   private static int countParen(String line, int numParen) {
	   // no count after // comment
	   //System.out.println("DEBUG: " + line);
	   if (line.trim().length() ==0) {
		   return numParen;
	   }

	   if (line.indexOf("//") != -1) {
	     line = line.substring(0,line.indexOf("//"));
       }
	   
	   
	   Matcher mopen = Pattern.compile("(\\{)").matcher(line);
	   Matcher mclose = Pattern.compile("(\\})").matcher(line);
       
	   while (mopen.find()) {
		   numParen++;
	   }
	
	   while (mclose.find()) {
		   numParen--;
	   }
	
	   //System.out.println("DEBUG: " + numParen);
         
	   return numParen;
   }
   
   
   public TestStatusReporter(String atype, ByteArrayOutputStream baos, PrintStream ps) throws Exception{  
	     path   = ZimbraSeleniumProperties.getStringProperty("ZimbraLogRoot")+"\\"  +
	              ZimbraSeleniumProperties.zimbraGetVersionString()  + "\\"  + 
	                                               atype +"\\" + 
	              ZimbraSeleniumProperties.getStringProperty("browser") + "\\" +  
	              ZimbraSeleniumProperties.getStringProperty("locale") ;
	     
	    
	     mkDir(path);
	     mkDir(path+ "\\" +failDir);
	     mkDir(path+ "\\" +skipDir);
	     mkDir(path+ "\\" +passDir);
	     mkDir(path+ "\\" +confFailDir);
	     mkDir(path+ "\\" +confSkipDir);
	     mkDir(path+ "\\" +inProgressDir);
	     mkDir(path+ "\\" +categoryDir);

	     startDate = new Date();

	     appType=atype;
		 output = new PrintWriter(new FileWriter(new File(path+ "/zimbraSelenium-failed.xml")));       
		
		 this.baos=baos;
		 this.ps = ps;
	
		 
		 StringBuffer sb= new StringBuffer("<html> <head><meta http-equiv='refresh' content='30'>");
		 sb.append("<meta http-Equiv='Cache-Control' Content='no-cache'>");
		 sb.append("<meta http-Equiv='Pragma' Content='no-cache'>");
		 sb.append("<meta http-Equiv='Expires' Content='0'></head><body>");

		 sb.append("<iframe src ='result.txt' width='100%' height='150'>");
		 sb.append("<p>Your browser does not support iframes.</p>");
		 sb.append("</iframe>");
	
		 sb.append("<iframe src ='method.txt' width='100%' height='90'>");
		 sb.append("<p>Your browser does not support iframes.</p>");
		 sb.append("</iframe>");
	
		 sb.append("<iframe src ='class.html' width='100%' height='300'>");
		 sb.append("<p>Your browser does not support iframes.</p>");
		 sb.append("</iframe>");
		 sb.append("</body></html>");
		 
		 
		 
		 try {
		      PrintWriter pw = new PrintWriter(new File(path+ "\\" + inProgressDir + "\\index.html"));
		      pw.println(sb.toString());
		      pw.close();
		 } catch (Exception e) { e.printStackTrace(); }

			output.println("<!DOCTYPE suite SYSTEM \"http://testng.org/testng-1.0.dtd\">");
		    output.println("<suite name='SelNG'>");
		    output.println("<test name='Failed Tests' >");
		    output.println("<groups>");
		    output.println("  <run>");
		    output.println(" <include name=\"always\"/>");
		    output.println("<include name=\"smoke\"/>");
		    output.println("</run>");
		    output.println("</groups>");
		    output.println("<classes>");
		 
		 
		 		 
   }	

   private void deleteDir(File file) {
	   if (file.isDirectory()) {
	       File[] fileArray = file.listFiles();
	       for (int i=0; i < fileArray.length; i++) {
	   	      deleteDir(fileArray[i]); //recursive delete files & directory
	       }
	   }
       file.delete();
   }
   
   private void mkDir(String path) {
	   File file= new File(path);
	   deleteDir(file);
	   file.mkdirs();	    	     
   }

   private String getClass(String fullName){
		  return fullName.substring(0,fullName.lastIndexOf("."));
	  }
	  
   private String getTest(String fullName){
		  return fullName.substring(fullName.lastIndexOf(".")+1);
   }
	  
   @Override
   public void onFinish(ITestContext testContext)  {
			//write fail/skip tests in a file
			 //ps.close();
		     String classStart="";
		     
			 for (int i=0 ; i < failArray.size(); i++) {
				 String className=getClass(failArray.get(i));
				 String testName=getTest(failArray.get(i));
				 
				 if (!className.equals(classStart)) {
	                if (classStart.length() > 0) {
	                  output.println("</methods>");
	                  output.println("</class>");                
	                } 	
				    classStart=className;
	                                
	                output.println("<class name='" + className + "'>");
	                output.println("<methods>");
	                
				 }
				 
				 output.println("<include name='" + testName + "'/>");            	
	          }
			 output.println("</methods>");
             output.println("</class>");  
			 output.println("</classes>");
			 output.println("</test>");
			 output.println("</suite>");
			 		 
			 output.close();
			 printToFile("","finish"); 
			 generateReport();				
			 
			 classInProgressPrintWriter.print("</body></html>");
			 classInProgressPrintWriter.flush();
			 classInProgressPrintWriter.close();
             copyFile(path+ "\\" + inProgressDir + "\\class.html",path + "\\" + categoryDir + "\\" + testContext.getName() + ".html");
  
             categoryArrayList.add(testContext.getName());
   }
	  
   public static  void copyFile(String src, String dest) {
		File f = new File(dest);
		if (!f.exists())
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		try {

			// Create channel on the source
			FileChannel srcChannel = new FileInputStream(src).getChannel();

			// Create channel on the destination
			FileChannel dstChannel = new FileOutputStream(dest).getChannel();

			// Copy file contents from source to destination
			dstChannel.transferFrom(srcChannel, 0, srcChannel.size());
			// Close the channels
			srcChannel.close();
			dstChannel.close();
		} catch (IOException e) {
		}

	}
 
  @Override
  public void onStart(ITestContext testContext) {
	    //TODO: save this file into a different directory   
	    printToFile("","start");  
	    try 
        {
	    	
            classInProgressPrintWriter= new PrintWriter(new File(path+ "\\" + inProgressDir + "\\class.html"));
            classInProgressPrintWriter.println("<html> <head><meta http-equiv='refresh' content='30'>");
            classInProgressPrintWriter.println("<meta http-Equiv='Cache-Control' Content='no-cache'>");
            classInProgressPrintWriter.println("<meta http-Equiv='Pragma' Content='no-cache'>");
            classInProgressPrintWriter.println("<meta http-Equiv='Expires' Content='0'></head><body>");

            classInProgressPrintWriter.println(new Date().toString() + "\n " + "Running ... " + testContext.getName());
            classInProgressPrintWriter.flush();
        }
        catch (Exception e) { e.printStackTrace();}
  }


  @Override
  public void onTestStart(ITestResult tr) {
	  String fullTestName = tr.getTestClass().getName() + "." + tr.getName();
	  baos.reset();
      
	  if  ( !( new File(path + "\\" + failDir + "\\" + fullTestName + ".txt").exists()) || 
		     ( new File(path + "\\" + skipDir + "\\" + fullTestName + ".txt").exists()) ||
		     ( new File(path + "\\" + passDir + "\\" + fullTestName + ".txt").exists())) 
	  {
	    System.out.println(GetFromFile.getCode(tr.getTestClass().getName().replaceAll("\\.","/")+ ".java" ,tr.getName()));	  
	  }
	  
	  log(new Date().toString() + "-------------- STARTED " + fullTestName  + getParameters(tr.getParameters())+ " STARTED" + " ----------------------------------------");
		 
	  classInProgressPrintWriter.println( "<br><br>\t" + new Date().toString() + " " + fullTestName);
      classInProgressPrintWriter.flush();
      
      try {
      PrintWriter pw = new PrintWriter(new File(path+ "\\" + inProgressDir + "\\method.txt"));
      pw.println(new Date().toString() + "\n " + "Running testcase... " + fullTestName);
      pw.close();
      }
      catch (Exception e) { e.printStackTrace(); }
      
      try {
          PrintWriter pw = new PrintWriter(new File(path+ "\\" + inProgressDir + "\\result.txt"));
      	int passed = passArray.size();
		int failed = failArray.size();
		int skipped =skipArray.size(); 
		int confFailed = confFailArray.size();
		int confSkipped =confSkipArray.size(); 
		long duration = ((new Date()).getTime() - startDate.getTime())/1000;
          String testdetails = 
      		//	" version:" + CommonTest.ZimbraVersion +
        	    new Date().toString() + 
        		" -  Duration " + duration + " sec " +
        		" - Sleep " + (SleepUtil.TotalSleepMillis/1000) + " sec " +        		
        	    "\n - " + (passed + failed + skipped ) + " run " + 
      			"\n - " +  passed + " pass  " +
      			"\n - " +  failed + " fail   - " +   confFailed + " conf fail  " + 
      			"\n - " +  skipped  + " skip " + " -  " +  confSkipped  + " conf skip ";
      			
      			
		
          pw.println(testdetails);
          pw.close();
          }
          catch (Exception e) { e.printStackTrace(); }
  }
  
 private void generateReport() {
		long duration = ((new Date()).getTime() - startDate.getTime())/1000;
		
		int passed = passArray.size();
		int failed = failArray.size();
		int skipped =skipArray.size(); 
		int confFailed = confFailArray.size();
		int confSkipped =confSkipArray.size(); 
		


		String bodyfileXpPath = path;
		String lines = "\n--------------------------------------------\n";
		String uri = (bodyfileXpPath.replace("T:/",
				"http://tms.lab.zimbra.com/testlogs/")).replace("\\", "/");
	    uri = ZimbraSeleniumProperties.getStringProperty("TestURL") + uri;
	    
		StringBuffer body = new StringBuffer( "");
		body.append("\n" + ZimbraSeleniumProperties.zimbraGetVersionString());			
		
		body.append("\n\n" + lines + "FINISHED : "  +  lines);
		
		//TODO starttime
		body.append("\n\nRan: " + (passed + failed + skipped));
		body.append("\nFail: " + failed);
		body.append("\nSkip: " + skipped);
		body.append("\nPass: " + passed);			
		body.append("\nbrowser: " + ClientSessionFactory.session().currentBrowserName());
		body.append("\nlocale: " + ZimbraSeleniumProperties.getStringProperty("locale")); 		
		body.append("\nserver: " +ZimbraSeleniumProperties.getStringProperty("server"));
	    body.append("\nclient: " + System.getenv("COMPUTERNAME")) ;
		body.append("\nduration: " + duration + " sec( Sleep: " + (SleepUtil.TotalSleepMillis/1000) + " sec)");
        body.append("\nstart at: " + startDate);
        body.append("\nend at: " + new Date());
	
		
	
		body.append("\n\n" + lines + "LOGS: " + lines );
		body.append("\n" + "Initialization: " + uri+ "/start.txt");
		body.append("\n" + "All Results: " + uri+ "/finish.txt");
	
		body.append("\n\n" + lines + "CATEGORIES: " + lines );	
		for (String c : categoryArrayList) {
			body.append("\n" +  uri+ "/" + categoryDir + "/" + c + ".html");			
		}
		
		if (confFailed >0) {
			body.append("\n\n" + lines + "Configuration fail: " + uri + "/" + confFailDir + "/" +  lines);
			body.append(getTestList(confFailArray));
		}
		
		if (confSkipped >0){
			body.append("\n\n" + lines + "Configuration skip: "  + uri + "/" + confSkipDir + "/"+ lines);
			body.append(getTestList(confSkipArray));
		}
	
		
		
		body.append("\n\n" + lines + "FAIL: " + (failed>0?uri+"/fail/":"") +  lines);
		if (failed >0) {
		    body.append(getTestList(failArray, failReasonArray));
		    
		}
		
		body.append("\n\n" + lines + "SKIP: "  + (skipped>0?uri+"/skip/":"")+ lines);
		if (skipped >0){
			body.append(getTestList(skipArray));
		}
		
		body.append("\n\n" + lines + "PASS: "  + (passed>0?uri+"/pass/":"")+lines);
		if (passed > 0) {
			body.append(getTestList(passArray));
		}
		body.append("\n\n" + lines + "TestNG REPORTS: " + lines);		
	    body.append("\n" + uri );
		body.append("\n" + uri + "/emailable-report.html");
	
		//body.append("\n\n" + lines + "CONSOLE OUTPUT: " + lines + uri+ "/testresult.txt");
		
		//body.append("\n\n" + lines + "SKIP TESTS INFO: " + lines+ "\n" + getRetriedTestsInfo());
		body.append("\n\n" + lines + "SKIP TESTS INFO: " + lines+ "\n" + uri + "/SkippedTests.txt");

		
		
		Writer output = null;

		output = null;
		File bodyfile = new File(path+ "/ebody.txt");
		try {
			output = new BufferedWriter(new FileWriter(bodyfile));
			output.write(body.toString());
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}


	}
 private String getTestList(ArrayList<String> array) {
 	StringBuffer result= new StringBuffer();
 	
 	for (int i=0; i <array.size(); i++){
 		   String methodName=array.get(i).replaceAll("\\.", "/");
 		   methodName = methodName.substring(methodName.indexOf("/tests/")+7);
 		   
 		   //result.append("<a href='" + uri + methodName.replace(".","/") + "'>"  + methodName+ "</a>" + "\n");
 		   result.append(methodName + "\n");
  
 	 }           
 	 	
 	return result.toString();
 }
 private String getTestList(ArrayList<String> array,ArrayList<String> reasonArray) {
	 	StringBuffer result= new StringBuffer();
	 	
	 	for (int i=0; i <array.size(); i++){
	 		   String methodName=array.get(i).replaceAll("\\.", "/");
	 		   methodName = methodName.substring(methodName.indexOf("/tests/")+7);
	 		   
	 		   //result.append("<a href='" + uri + methodName.replace(".","/") + "'>"  + methodName+ "</a>" + "\n");
	 		   result.append(methodName + "\n\t");
	 		   result.append(reasonArray.get(i) + "\n");
	 	 }           
	 	 	
	 	return result.toString();
	 }
 
  private void printToFile(String dir, String testName) {
	  //always appending to file
	  try {	
  	    PrintWriter pw = new PrintWriter(new FileWriter(path + "\\" +dir + "\\" + testName + ".txt",true)); 
        pw.println(baos.toString());
  	    pw.close();
  	    
  	    if (testName.indexOf("tests") != -1) {
  	    StringBuffer sb= new StringBuffer(testName.substring(testName.indexOf(".tests.") + 7));
  	    
  	    int i=0;
  	    while (i >=0) {
  	    	i= sb.indexOf(".",i);
  	    	if (i >0) {
  	    		sb.setCharAt(i, '\\');
  	    	}
  	    }
  	    
  	    String subPath= sb.toString();
    	   
  	    if (subPath.indexOf("\\") != -1) {
  	     if (!new File(path+ "\\" + dir + "\\" + subPath).mkdirs()) {
  	       System.out.println("cannot create dir " + path+ "\\" + dir + "\\" + subPath);	  	    	
  	     }
  		
  	     //hierachy
  	     pw = new PrintWriter(new FileWriter(path + "\\" +dir + "\\" + subPath + "\\" + testName + ".txt"),true);
  	     pw.println(baos.toString());
	     pw.close();
  	    }
  	    }
        baos.reset();      	
	  }
		
      catch (Exception e) { e.printStackTrace();}
  }

  private String getParameters(Object[] objs) {
	  StringBuffer result=new StringBuffer("(");
	  
	  for (int i=0; i < objs.length; i++) {
		  if (i >0) {
			  result.append(",");
		  }
		  if (objs[i] instanceof Integer) {
			 result.append( ((Integer)objs[i]).intValue() + "");
		  }
		  else if (objs[i] instanceof String) {
			  result.append(objs.toString());
		  }
		  else if ((objs[i] instanceof String[])) {
			String[] temp= (String[])(objs[i]);
			result.append(temp[0]);  		  
		  }
		  else if ((objs[i] instanceof String[][])) {
				String[][] temp= (String[][])(objs[i]);
				result.append(temp[0][0]);  		  
	      }
		  else {
			  result.append(objs.toString());					  
		  }
	  }
		    
	  result.append(")");
	  return result.toString();
  }  
  
  @Override
  public void onConfigurationFailure(ITestResult tr) {
	  String fullTestName = tr.getTestClass().getName() + "." + tr.getName();
 	  tr.getThrowable().printStackTrace(ps);
      confFailArray.add(fullTestName);
	  log(new Date().toString() + "-------------- CONFIGURATION FAILED " + fullTestName + "  CONFIGURATION FAILED ----------------------------------------");

      printToFile(confFailDir , fullTestName);     
      classInProgressPrintWriter.println( "<br>\t" + new Date().toString() + " CONFIG FAILED" );
      
      if (tr.getThrowable() != null) {
    	  tr.getThrowable().printStackTrace(ps);
    	    
    	  classInProgressPrintWriter.println( "<br>\t" + tr.getThrowable().getMessage());
          classInProgressPrintWriter.println( "<br>\t" + getErrorLine(tr.getThrowable(),tr.getName()));
        

    	  confFailReasonArray.add(tr.getThrowable().getMessage() + "\n\t" + getErrorLine(tr.getThrowable(),tr.getName()));
      }
      else {
    	  confFailReasonArray.add("");
      }
      classInProgressPrintWriter.flush();

  }

  @Override
  public void onTestFailure(ITestResult tr) {
	  String fullTestName = tr.getTestClass().getName() + "." + tr.getName();
 	  tr.getThrowable().printStackTrace(ps);
      failArray.add(fullTestName);
	  log(new Date().toString() + "-------------- FAILED " + fullTestName + " FAILED ----------------------------------------");

      printToFile(failDir , fullTestName);
      //TODO: add back in
      ClientSessionFactory.session().selenium().captureScreenshot(path + "\\" + failDir + "\\"+ fullTestName + ".png");
	
      classInProgressPrintWriter.println( "<br>\t" + new Date().toString() + " FAILED" );
     //TODO Add back in
      classInProgressPrintWriter.println( "<br>\t" + "<a href='../fail/" + fullTestName + ".png' target=newWindow><img src='../fail/" + fullTestName + ".png' width=100 height=100 border=0></a>" 
    	                                  + "  <a href='../fail/" + fullTestName + ".txt' target=newWindow> details </a>"  );
      
      classInProgressPrintWriter.println( "<br>\t <pre>" + tr.getThrowable().getMessage().replaceAll("<","&lt;").replaceAll(">","&gt;") + "</pre>");
      classInProgressPrintWriter.println( "<br>\t" + getErrorLine(tr.getThrowable(),tr.getName()));
      classInProgressPrintWriter.flush();
      
      failReasonArray.add(tr.getThrowable().getMessage() + "\n\t" + getErrorLine(tr.getThrowable(),tr.getName()));
  
  }
  
  
  private String getErrorLine(Throwable thr, String methodName) {
	  String result="";
	  StackTraceElement[] seArray= thr.getStackTrace();
	  
	  for (int i=0; i<seArray.length; i++) {
		 StackTraceElement ste= seArray[i];
		 if (ste.getMethodName().equals(methodName)) {
			 result = "at " + ste.getClassName() + "." + methodName + " (" + ste.getFileName() + ":" +  ste.getLineNumber() + ")";
			 result +="<br>\n\t" + GetFromFile.getDoubleLineAt( ste.getClassName().replaceAll("\\.","/")+ ".java", ste.getLineNumber() );
			 break;
		 }
	  }
	  
	  return result;
  }
  
  @Override
  public void onConfigurationSkip(ITestResult tr) {
	  String fullTestName = tr.getTestClass().getName() + "." + tr.getName();
	   		
	  if (tr.getThrowable() != null) {
		  tr.getThrowable().printStackTrace(ps);		     	  
	  }
	  
	  log( new Date().toString() + "-------------- CONFIGURATION  SKIPPED " + fullTestName + "  CONFIGURATION SKIPPED ----------------------------------------");
      confSkipArray.add(fullTestName); 
      
      printToFile(confSkipDir , fullTestName);
      // TODO: add back in	 
      // SelNGBase.selenium.captureScreenshot(path + "\\"  + fullTestName + ".png");
      classInProgressPrintWriter.println( "<br>\t" + new Date().toString() + " CONFIG SKIPPED" );
      
      if (tr.getThrowable() != null) {
    	  tr.getThrowable().printStackTrace(ps);
    	    
    	  classInProgressPrintWriter.println( "<br>\t" + tr.getThrowable().getMessage());
          classInProgressPrintWriter.println( "<br>\t" + getErrorLine(tr.getThrowable(),tr.getName()));

    	  confSkipReasonArray.add(tr.getThrowable().getMessage() + "\n\t" + getErrorLine(tr.getThrowable(),tr.getName()));
      }
      else {
    	  confSkipReasonArray.add("");
      }
      classInProgressPrintWriter.flush();
    
  }
  
  @Override
  public void onTestSkipped(ITestResult tr) {
	  String fullTestName = tr.getTestClass().getName() + "." + tr.getName();
	   		
	  if (tr.getThrowable() != null) {
		  tr.getThrowable().printStackTrace(ps);		     	  
	  }
	  
	  log( new Date().toString() + "-------------- SKIPPED " + fullTestName + " SKIPPED ----------------------------------------");
      skipArray.add(fullTestName); 
      
      printToFile(skipDir , fullTestName);
      // TODO: add back in	 
      // SelNGBase.selenium.captureScreenshot(path + "\\"  + fullTestName + ".png");

      classInProgressPrintWriter.println( "<br>\t" + new Date().toString() + " SKIPPED" );
      
      if (tr.getThrowable() != null) {
    	  tr.getThrowable().printStackTrace(ps);
    	    
    	  classInProgressPrintWriter.println( "<br>\t" + tr.getThrowable().getMessage());
          classInProgressPrintWriter.println( "<br>\t" + getErrorLine(tr.getThrowable(),tr.getName()));

    	  skipReasonArray.add(tr.getThrowable().getMessage() + "\n\t" + getErrorLine(tr.getThrowable(),tr.getName()));
      }
      else {
    	  skipReasonArray.add("");
      }
       classInProgressPrintWriter.flush();  
  }
  @Override
  public void onTestSuccess(ITestResult tr) {
	  String fullTestName = tr.getTestClass().getName() + "." + tr.getName();
	  log( new Date().toString() + "-------------- PASSED " + fullTestName + " PASSED ----------------------------------------");
	  passArray.add(fullTestName); 
	   
	  printToFile(passDir , fullTestName);
	
      classInProgressPrintWriter.println( "<br>\t" + new Date().toString() + " PASSED" );
      classInProgressPrintWriter.flush();
  }
  
  public static void log(String string) {
	  System.out.println(string);
  }



	private String getRetriedTestsInfo() {
		String tmpstr = "";
		String str = "";
		try {
			BufferedReader in = new BufferedReader(new FileReader(
					path+ "\\SkippedTests.txt"));

			while ((tmpstr = in.readLine()) != null) {
				str = str + "\n" + (tmpstr.split("\\(")[0]);
			}
			in.close();
		} catch (IOException e) {
		}
		return str;
	}
} 
