require 'java'
require '../../webclient/target/Client-0.1.jar'

java_import com.asquera.web.Client

class JavaClient
  def initialize()
    @client = Client.new
  end

  def send_request(url, secret)
    signedUrl = @client.sign_url(url, secret)
    @client.send_get_request(signedUrl)
  end
end

begin
  client = JavaClient.new
  response = client.send_request('http://localhost:4567/test', 'TESTSECRET')
  puts "response: #{response}"

rescue Exception => e
  puts "Failed to create Java Client"
  puts e.message
  puts e.backtracke.inspect
end
