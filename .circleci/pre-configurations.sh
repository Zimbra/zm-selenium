#!/bin/bash

set -euxo pipefail

# Configuration
seleniumConfigFile="$HOME/zm-selenium/conf/config.properties"

# Update server
echo -e "Updating server host in config.properties file"
configServer=`cat $seleniumConfigFile | grep server.host`
configServer=`echo $configServer | cut -d \= -f 2`
sed -i "0,/$configServer/s/$configServer/$ENV_SELENIUM_SERVER_HOST/" $seleniumConfigFile

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