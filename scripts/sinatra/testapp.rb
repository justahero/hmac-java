require 'sinatra'
require 'hmac/signer'

get '/' do
  'Hello World'
end

get '/test' do
  h = HMAC::Signer.new('sha1')
  result = h.validate_url_signature(request.url, "TESTSECRET")

  if result
    "everything is fine"
  else
    "something is wrong"
  end

end