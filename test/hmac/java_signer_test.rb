require 'java'

java_import javax.crypto.Mac
java_import com.asquera.hmac.WardenHMacSigner

class SignerTest < Test::Unit::TestCase
  attr_accessor :javasigner, :hmacsigner

  context "WardenHMacSigner" do
    setup do
      mac = Mac.get_instance("HmacMD5")
      self.javasigner = WardenHMacSigner.new(mac)
      self.hmacsigner = HMAC::Signer.new("md5")
    end

    should "matches the signed url" do
      actual   = hmacsigner.sign_url("/test", "secret")
      expected = javasigner.sign_url("/test", "secret")
      
      assert_equal(actual, expected)
    end
  end
end
