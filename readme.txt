1. Create .zcs-deps folder at %UserProfile% in windows 
2. Copy following jars there:

ant-contrib-1.0b1.jar
ant-tar-patched.jar
apache-log4j-extras-1.0.jar
commons-beanutils-1.7.jar
commons-cli-1.2.jar
commons-codec-1.7.jar
commons-configuration-1.5.jar
commons-httpclient-3.1.jar
commons-lang-2.6.jar
commons-logging.jar
dom4j-1.5.2.jar
ezmorph-1.0.6.jar
guava-13.0.1.jar
htmlcleaner-2.2.jar
javamail-1.4.5.jar
jaxen-1.1.3.jar
jsch-0.1.53.jar
json.jar
json-lib-2.3-jdk15.jar
ldtp-1.0.jar
libidn-1.24.jar
log4j-1.2.16.jar
mariadb-java-client-1.1.8.jar
ocutil-2.4.2.jar
selenium-java-2.51.0.jar
selenium-server-standalone-2.51.0.jar
testng-5.12.1.jar
ws-commons-util-1.0.2.jar
xalan.jar
xmlrpc-client-3.1.3.jar
xmlrpc-common-3.1.3.jar
zimbracommon.jar
zimbra-native.jar

3. git clone ssh://git@stash.corp.synacor.com:7999/zimbra/zm-zcs.git

4. git clone ssh://git@stash.corp.synacor.com:7999/zimbra/zm-sotre-conf.git

5. git clone ssh://git@stash.corp.synacor.com:7999/zimbra/zm-web-client.git

6. git clone ssh://git@stash.corp.synacor.com:7999/zimbra/zm-zimlets.git

7. ant jar

this creates three jars at C:\stash\zm-selenium\build\dist\lib

coverage.jar
resources.jar
zimbraselenium.jar

8. For creating selng tgz 
ant jar-staf-selenium
ant build-selng

