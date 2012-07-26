require 'rubygems'

begin
  require 'hmac/signer'
rescue
  puts "Please install the warden-hmac-authentication gem"
end

begin
  require 'shoulda'
rescue LoadError
  puts "Failed to load shoulda"
end

begin
  require 'java'
  require File.expand_path('../hmac/target/WardenHMacSigner-0.1.jar')
rescue LoadError
  puts "Failed to import necessary java packages"
end

# find all test files
Dir["#{File.dirname(__FILE__)}/**/*_test.rb"].each { |file| require file }