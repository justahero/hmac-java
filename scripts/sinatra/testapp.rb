require 'sinatra'
require 'hmac/signer'

get '/' do
  'Hello World'
end

get '/test' do
  puts "request: #{request.url}"

  signer = HMAC::Signer.new("md5")
  result = signer.validate_url_signature(request.url, "TESTSECRET")

  #url = signer.sign_url("/test", "secret", :date => "Fri, 24 Aug 2012 14:44:15 GMT")
  #puts "signed:  #{url}"

  if result
    "everything is fine"
  else
    "something is wrong"
  end

end