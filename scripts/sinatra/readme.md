Prerequisites
-------------

The Web application uses the [warden-hmac-authentication](https://github.com/Asquera/warden-hmac-authentication) module
to test authentication with signed urls.

Install the ruby gems `bundler` and `sinatra`.

    gem install bundler
    gem install sinatra

Bundle and install gem, go to authentication directory

    bundle install
    gem build warden-hmac-authentication.gemspec
    gem install warden-hmac-authentication-0.6.2.gem


Testing
-------

Start Web application

    ruby testapp.rb