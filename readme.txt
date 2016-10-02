1. Create .zcs-deps directory at your %UserProfile%

2. Copy following jars at %UserProfile%/.zcs-deps

ant-contrib-1.0b1.jar
ant-tar-patched.jar
apache-log4j-extras-1.0.jar
client-combined-3.0.0-beta4-nodeps.jar
commons-beanutils-1.7.jar
commons-cli-1.2.jar
commons-codec-1.7.jar
commons-collections-3.2.2.jar
commons-configuration-1.5.jar
commons-httpclient-3.1.jar
commons-io-2.5.jar
commons-lang-2.6.jar
commons-logging.jar
dom4j-1.5.2.jar
ezmorph-1.0.6.jar
guava-13.0.1.jar
htmlcleaner-2.2.jar
javamail-1.4.5.jar
jaxen-1.1-beta-6.jar
jsch-0.1.53.jar
json-lib-2.3-jdk15.jar
json.jar
JSTAF.jar
ldtp-1.0.jar
libidn-1.24.jar
log4j-1.2.16.jar
mariadb-java-client-1.1.8.jar
ocutil-2.4.2.jar
selenium-server-standalone-3.0.0-beta4.jar
testng-5.12.1.jar
ws-commons-util-1.0.2.jar
xalan.jar
xml-apis-2.0.2.jar
xmlrpc-client-3.1.3.jar
xmlrpc-common-3.1.3.jar
zm-native-8.8.0.jar
zm-common-8.8.0.jar

3. Clone following git repos:
git clone https://git@github.com:Zimbra/zimbra-package-stub.git
git clone ssh://git@stash.corp.synacor.com:7999/zimbra/zm-zcs.git
git clone ssh://git@stash.corp.synacor.com:7999/zimbra/zm-store-conf.git
git clone ssh://git@stash.corp.synacor.com:7999/zimbra/zm-web-client.git
git clone ssh://git@stash.corp.synacor.com:7999/zimbra/zm-zimlets.git

4. ant jar

It will create 3 jars at ..\zm-selenium\build\dist\lib:
coverage.jar
resources.jar
zimbraselenium.jar