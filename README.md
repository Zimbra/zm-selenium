# Building zm-selenium

1. Create .zcs-deps directory at your %UserProfile%

2. Copy following jars at %UserProfile%/.zcs-deps
    ```
    ant-contrib-1.0b1.jar
    ```

3. Clone following git repos:
    ```
    git clone https://github.com/Zimbra/zimbra-package-stub.git
	git clone https://github.com/Zimbra/zm-mailbox.git
    git clone https://github.com/Zimbra/zm-zcs.git
    git clone https://github.com/Zimbra/zm-web-client.git
    git clone https://github.com/Zimbra/zm-zimlets.git
    ```

4. Build zm-native and zm-common using zm-mailbox repo.
   ```
   - Go to zm-mailbox and build using following command.
         ant publish-local-all -Dzimbra.buildinfo.version=8.7.6_GA
 
   ```It will create zm-common.jar and zm-native.jar file 
   

5. Build zm-selenium using the following command 

	ant jar 

    ```
    It will create 3 jars at ..\zm-selenium\build\dist\lib:
    coverage.jar
    resources.jar
    zimbraselenium.jar