require 'java'

java_import javax.crypto.Mac
java_import com.asquera.hmac.WardenHMacSigner
java_import com.asquera.hmac.RequestParams

class SignerTest < Test::Unit::TestCase
  attr_accessor :javasigner, :hmacsigner

  context "WardenHMacSigner" do
    setup do
      mac = Mac.get_instance("HmacMD5")
      self.javasigner = WardenHMacSigner.new(mac)
      self.hmacsigner = HMAC::Signer.new("md5")
    end

    should "match the signed url with same time stamp" do
      actual = hmacsigner.sign_url("/test", "secret", :date => "Fri, 24 Aug 2012 14:44:15 GMT")

      options = RequestParams.new()
      options.set_date(2012, 7, 24, 14, 44, 15)
      expected = javasigner.sign_url("/test", "secret", options)

      assert_equal(actual, expected)
    end

  end
end
