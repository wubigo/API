package open.signature;

import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import static open.signature.SignUtil.sign;

@Slf4j
@SpringBootApplication
public class SignatureApplication {



	public static void main(String[] args) {

		try {
			// calling getKeyPair() method and assining in keypair
			KeyPair keyPair = SignUtil.getKeyPair();

			JSONObject jo = new JSONObject();
			jo.put("name", "open");
			jo.put("type", "工程豆");
			String msg = jo.toJSONString();
			log.info("msg={}", msg);

			byte[] encryptedHash = sign(keyPair.getPrivate(), msg);
			String base64String = Base64.getEncoder().encodeToString(encryptedHash);
			boolean isValid = SignUtil.verifySign(keyPair.getPublic(), msg, encryptedHash);
			log.info("isValid={}", base64String);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		//SpringApplication.run(SignatureApplication.class, args);
	}


}
