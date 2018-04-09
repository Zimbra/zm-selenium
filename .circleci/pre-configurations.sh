#!/bin/bash

set -euxo pipefail

# Selenium configuration
seleniumConfigFile="$HOME/zm-selenium/conf/config.properties"

# Update server host
echo -e "Updating server host in config.properties file"
configServerHost=`cat $seleniumConfigFile | grep server.host`
configServerHost=`echo $configServerHost | cut -d \= -f 2`
sed -i "0,/$configServerHost/s/$configServerHost/$ENV_SELENIUM_SERVER_HOST/" $seleniumConfigFile

# Update server user
echo -e "Updating server user in config.properties file"
configServerUser=`cat $seleniumConfigFile | grep server.user`
configServerUser=`echo $configServerUser | cut -d \= -f 2`
sed -i "0,/$configServerUser/s/$configServerUser/$ENV_SELENIUM_SERVER_USER/" $seleniumConfigFile

# Update browser
echo -e "Updating browser in config.properties file"
configBrowser=`cat $seleniumConfigFile | grep browser`
configBrowser=`echo $configBrowser | cut -d \= -f 2`
sed -i "0,/$configBrowser/s/$configBrowser/$ENV_SELENIUM_BROWSER/" $seleniumConfigFile

# Copy private key to the container
echo -e "Copy private key to the container"
cat ~/.ssh/id_rsa_* > ~/.ssh/id_rsa

# Generate public key from private key
echo -e "Generating public key from private key"
rm -f ~/.ssh/id_rsa.pub && ssh-keygen -f ~/.ssh/id_rsa -y > ~/.ssh/id_rsa.pub