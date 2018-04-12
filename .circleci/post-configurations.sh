#!/bin/bash

set -euxo pipefail

# Create test output links
ln -s ~/zm-selenium/test-output/*/*/*/*/TestNG/index.html ~/zm-selenium/test-output/testng-report.html
ln -s ~/zm-selenium/test-output/*/*/*/*/debug/projects/harness.log ~/zm-selenium/test-output