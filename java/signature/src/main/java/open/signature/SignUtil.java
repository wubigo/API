package open.signature;

import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;


@Slf4j
public class SignUtil {

    static final String algorithm = "SHA1WithRSA";

    public static byte[] encodePublicKey(){
        // calling getKeyPair() method and assining in keypair
        try {
            KeyPair keyPair = SignUtil.getKeyPair();
            log.info("encoded public key:{}", keyPair.getPublic().getEncoded());
            log.info("encoded private key:{}", keyPair.getPrivate().getEncoded());
            return keyPair.getPublic().getEncoded();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static boolean verifySign(PublicKey publicKey, String msg, byte[] md){


        // initializing the signature object with key pair
        // for signing
        try {

            // creating the object of Signature
            Signature sr = Signature.getInstance(algorithm);
            sr.initVerify(publicKey);
            sr.update(msg.getBytes("UTF8"));
            return sr.verify(md);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Technically speaking, a digital signature is the encrypted hash (digest, checksum) of a message.
     * That means we generate a hash from a message and encrypt it with a private key according to a
     * chosen algorithm.
     *
     * The message, the encrypted hash, the corresponding public key, and the algorithm are all
     * then sent. This is classified as a message with its digital signature.
     * @param privateKey
     * @param msg
     * @return
     */
    public  static byte[]  sign(PrivateKey privateKey, String msg){
        try {
            // data to be updated
            byte[] data = msg.getBytes("UTF8");

            // creating the object of Signature
            Signature sr = Signature.getInstance(algorithm);

            // initializing the signature object with key pair
            // for signing
            sr.initSign(privateKey);

            // updating the data
            sr.update(data);

            // getting the msg digest
            // by using method sign()
            return sr.sign();

            // pritning the number of byte
            //log.info("Signature:" + new String(bytes, "UTF8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        }

        return null;

    }

    // defining getKeyPair method
    public static KeyPair getKeyPair() throws NoSuchAlgorithmException
    {

        // creating the object of KeyPairGenerator
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");

        // initializing with 1024
        kpg.initialize(1024);

        // returning the key pairs
        return kpg.genKeyPair();
    }

    public static PublicKey transformPubkey(byte[] encodedKey){
        PublicKey publicKey = null;
        try {
            publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(encodedKey));
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return  publicKey;
    }

    public static PrivateKey transformPriKey(byte[] encodedKey){
        PrivateKey privateKey = null;
        try {
            KeyFactory kf = KeyFactory.getInstance("RSA"); // or "EC" or whatever
            privateKey = kf.generatePrivate(new PKCS8EncodedKeySpec(encodedKey));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return privateKey;
    }

}
