#!/bin/bash
currentdir=`pwd`

# fail on errors
set -e

# install ruby gems
gem install sinatra
gem install bundler

# checkout warden module
git clone git://github.com/justahero/warden-hmac-authentication.git ../authentication
# build gem and install it
cd ../../authentication
bundle install
gem build warden-hmac-authentication.gemspec
gem install warden-hmac-authentication-0.6.2.gem

# get back to origin directory
#cd $currentdir

# start sinatra web app
#cd scripts/sinatra
