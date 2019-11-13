package open.signature;

import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import static open.signature.SignUtil.sign;

@SpringBootTest
@Slf4j
class SignatureApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void testEncodePublicKey(){
		try {
			// calling getKeyPair() method and assining in keypair
			KeyPair keyPair = SignUtil.getKeyPair();

			JSONObject jo = new JSONObject();
			jo.put("name", "open");
			jo.put("type", "工程豆");
			String msg = jo.toJSONString();
			log.info("msg={}", msg);
			byte[] pk = SignUtil.encodePublicKey();
			log.info("pk={} len={}", Base64.getEncoder().encodeToString(pk), Base64.getEncoder().encodeToString(pk).length());

			byte[] encryptedHash = sign(keyPair.getPrivate(), msg);
			String base64String = Base64.getEncoder().encodeToString(encryptedHash);
			boolean isValid = SignUtil.verifySign(keyPair.getPublic(), msg, encryptedHash);
			log.info("isValid={}", base64String);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

}
