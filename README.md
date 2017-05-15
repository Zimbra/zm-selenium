# Building zm-selenium

1. Create .zcs-deps directory at your %UserProfile%.
2. Install ant, ivy, Java1.8+ and set respective environment variables.
3. Copy following jars at %UserProfile%/.zcs-deps:
    ```
    ant-contrib-1.0b1.jar
    ```
4. Clone following git repos:
    ```
    git clone https://github.com/Zimbra/zimbra-package-stub.git
    git clone https://github.com/Zimbra/zm-mailbox.git
    git clone https://github.com/Zimbra/zm-zcs.git
    git clone https://github.com/Zimbra/zm-web-client.git
    git clone https://github.com/Zimbra/zm-zimlets.git
    git clone https://github.com/Zimbra/zm-selenium.git
    ```
5. Build zm-native and zm-common jar using zm-mailbox repo.
   ```
   Go to zm-mailbox and build using following command.
         ant publish-local-all -Dzimbra.buildinfo.version=8.7.6_GA
 
   It will create zm-common.jar and zm-native.jar file
6. Build zm-selenium using the following command:

    ant jar
    ```
    It will create 3 jars at ..\zm-selenium\build\dist\lib:
    coverage.jar
    resources.jar
    zimbraselenium.jar
7. You can run testcases using build.xml or by adding debug confirmation run.
    ```
    Build.xml:
        ant Run-ExecuteHarnessMain -Dpattern ajax.tests.conversation.quickreply -Dgroups always,smoke

    zimbraselenium.jar:
    - Create new configuration
    - Select 'zm-selenium' project and 'com.zimbra.qa.selenium.framework.core.ExecuteHarnessMain' as main class
    - Give '-j C:\pathToZm-selenium\build\dist\lib\zimbraselenium.jar -p com.zimbra.qa.selenium.projects.admin.tests.login.BasicLogin -g always,L0,L1,L2,L3 -l conf/log4j.properties' in argument