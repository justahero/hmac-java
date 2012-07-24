require 'sinatra'
require 'hmac/signer'

get '/' do
  'Hello World'
end

get '/test' do
  h = HMAC::Signer.new('sha1')
  result = h.validate_url_signature(request.url, "TESTSECRET")

  signed = h.sign_url('http://localhost:4567/test', 'TESTSECRET')

  puts "signed:  #{signed}"
  puts "request: #{request.url}"

  if result
    "everything is fine"
  else
    "something is wrong"
  end

end

#h.sign_url('http://example.org/example.html', 'secret')
#h.validate_url_signature('http://example.org/example.html?auth[signature]=foo', 'secret')