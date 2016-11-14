1. Create .zcs-deps directory at your %UserProfile%

2. Copy following jars at %UserProfile%/.zcs-deps

ant-contrib-1.0b1.jar


3. Clone following git repos:
git clone https://git@github.com:Zimbra/zimbra-package-stub.git
git clone ssh://git@stash.corp.synacor.com:7999/zimbra/zm-zcs.git
git clone ssh://git@stash.corp.synacor.com:7999/zimbra/zm-store-conf.git
git clone ssh://git@stash.corp.synacor.com:7999/zimbra/zm-web-client.git
git clone ssh://git@stash.corp.synacor.com:7999/zimbra/zm-zimlets.git

4. Clone zm-common and zm-native and do a publish-local for both repos.

4. ant jar

It will create 3 jars at ..\zm-selenium\build\dist\lib:
coverage.jar
resources.jar
zimbraselenium.jar