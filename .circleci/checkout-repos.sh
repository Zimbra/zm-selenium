#!/bin/bash

set -euxo pipefail

# Additionally get circleci container public ip address and hostname
CONTAINER_IP_ADDRESS=$(wget -qO- http://checkip.amazonaws.com)
echo $CONTAINER_IP_ADDRESS
echo $(hostname -s)

# Checkout dependent repositories
GITHUB_ORG_URL="https://github.com/Zimbra"
GITHUB_REPOS=( "zm-mailbox" "zm-ajax" "zm-web-client" "zm-zimlets" "zm-network-selenium" )
for GITHUB_REPO in "${GITHUB_REPOS[@]}"; do
	git clone --depth=1 -b $ENV_BRANCH $GITHUB_ORG_URL/$GITHUB_REPO ~/$GITHUB_REPO
done