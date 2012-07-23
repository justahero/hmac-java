require 'sinatra'
require 'hmac/signer'

get '/' do
  'Hello World'
end

get '/test' do
  h = HMAC::Signer.new('sha1')
  result = h.validate_url_signature(request.url, "TESTSECRET")

  if result == true then
    "everything is fine"
  else
    "something is wrong"
  end

end

#h.sign_url('http://example.org/example.html', 'secret')
#h.validate_url_signature('http://example.org/example.html?auth[signature]=foo', 'secret')