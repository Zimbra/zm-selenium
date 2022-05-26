# Building zm-selenium

1. Create .zcs-deps directory at your %UserProfile%.
2. Install ant, ivy, Java1.8+ and set respective environment variables.
3. Download ant-contrib-1.0b1 jar file from below URL put into %UserProfile%/.zcs-deps location.
```
https://files.zimbra.com/repository/ant-contrib/ant-contrib-1.0b1.jar
```

4. Clone following git repos:
```
git clone https://github.com/Zimbra/zm-mailbox.git
git clone https://github.com/Zimbra/zm-ajax.git
git clone https://github.com/Zimbra/zm-web-client.git
git clone https://github.com/Zimbra/zm-zimlets.git
git clone https://github.com/Zimbra/zm-selenium.git
```

5. Build zm-native and zm-common jar using zm-mailbox repo.
- Go to zm-mailbox and build using following command.
```
ant publish-local-all -Dzimbra.buildinfo.version=9.0.0_GA
```
- It will create zm-native.jar and zm-common.jar file
 
6. Build zm-selenium using the following command:
```
cd zm-selenium
ant clean download-ivy init-ivy selenium-staf-package
```

It will create 3 jars at zm-selenium\build\dist\lib:
```
coverage.jar
resources.jar
ZimbraSelenium.jar
```

7. You can run testcases using build.xml or by adding debug confirmation run.
```
ant Run-ExecuteHarnessMain -Dpattern ajax.tests.mail.folders -Dgroups always,smoke1
```
       
# Executing selenium testcases through Eclipse

1. Download JDK 1.8.0_291 using https://www.oracle.com/java/technologies/javase/javase8u211-later-archive-downloads.html#license-lightbox (create oracle account) and install it.

2. Download latest Eclipse IDE for e.g. https://www.eclipse.org/downloads/download.php?file=/technology/epp/downloads/release/2022-03/R/eclipse-java-2022-03-R-win32-x86_64.zip
- Go to Help > Check for updates and update it.
- Go to Help -> Eclipse Marketplace -> Search testng -> Install
- Go to Help -> Eclipse Marketplace -> Search ivyde -> Install

![image](https://user-images.githubusercontent.com/21263826/170434503-af880feb-8a73-4887-828c-9deba68b0ad1.png)

3. In Eclipse, everywhere select JDK 1.8.0_291 as alternative JRE.

![image](https://user-images.githubusercontent.com/21263826/170434722-dccae5ff-3d92-4259-8167-ef07b4088cf3.png)

![image](https://user-images.githubusercontent.com/21263826/170434757-0f309865-1f95-4069-bc2d-74f4585150d6.png)

4. Set system environment variable JAVA_HOME (please replace path according to your OS).
```
JAVA_HOME: C:\Program Files\Java\jdk1.8.0_291
```

5. Add java bin path system environment variable (please replace path according to your OS).
```
path: C:\Program Files\Java\jdk1.8.0_291\bin
```

6. In Eclipse, right click to build.xml -> Debug as -> Add targets:
```
clean, download-ivy, init-ivy, selenium-staf-package (in order)
```
Debug it -> It will create the jar and packages.

7. Modify testcase group from smoke to smoke1 to run atleast one testcase. In below command, you can modify test pattern.
For e.g.
```
test pattern from com.zimbra.qa.selenium.projects.ajax.tests to com.zimbra.qa.selenium.projects.ajax.tests.contacts
test group from smoke to smoke1 to any test for e.g. projects -> ajax -> tests -> contacts -> contacts -> CreateContact.ClickContact_01()
```

8. Select Debug toolbar button -> Select Debug Configurations -> Right click to Java Application -> New Configuration:

Name:
```
test
```
Main class:
```
com.zimbra.qa.selenium.framework.core.ExecuteHarnessMain
```
Arguments -> Program arguments:
```
-j C:\git\zm-selenium\build\dist\lib\ZimbraSelenium.jar -p com.zimbra.qa.selenium.projects.ajax.tests -g always,smoke1,bhr1,sanity1,functional1 -l conf/log4j.properties -r 3 -v 9.0.0.NETWORK
```
JRE:
```
jdk1.8.0_291
```

Note: You should use 'configure' test group first time running the test to create server data like testdomain.com domain, galsync and globaladmin account. Argument will become like below and later you can remove configure test group.
```
-j C:\git\zm-selenium\build\dist\lib\ZimbraSelenium.jar -p com.zimbra.qa.selenium.projects.ajax.tests -g always,configure,smoke1,bhr1,sanity1,functional1 -l conf/log4j.properties -r 3 -v 9.0.0.NETWORK
```

9. Click to Debug Test toolbar button -> Select test to execute the tests.

![image](https://user-images.githubusercontent.com/21263826/170435032-004815aa-6726-4847-9f88-42b69bb06559.png)

10. Test would be executed and you will get result like:
```
Total Tests:   1
Total Passed:  1
Total Failed:  0
Total Skipped: 0
Total Retried: 0
```