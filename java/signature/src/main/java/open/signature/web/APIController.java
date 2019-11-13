package open.signature.web;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import lombok.extern.slf4j.Slf4j;
import open.signature.SignUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import static open.signature.SignUtil.sign;

@RestController
@Slf4j
public class APIController {

    @PostMapping(value = "upload")
    public int upload(@RequestBody String body){
        DocumentContext jsonContext = JsonPath.parse(body);
        String encryptedHash = jsonContext.read("$.md");
        String trans = jsonContext.read("$.trans");
        byte[] hashBytes = Base64.getDecoder().decode(encryptedHash);
        //read user public key from database
        //String pubken_Base64= db row
        //PublicKey pubkey = SignUtil.transformPubkey(pubkenBase64)
        //boolean isValid = SignUtil.verifySign(pubkey, trans, hashBytes);
        return 1;
    }
}
