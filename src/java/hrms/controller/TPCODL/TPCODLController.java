/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.TPCODL;

import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TPCODLController {

    private static final String FACTORY_INSTANCE = "PBKDF2WithHmacSHA256";
    private static final String ALGORITHM = "AES/CBC/PKCS5PADDING";
    private static final String ENCRYPTION_TYPE = "AES";
    private static final int IV_LENGTH = 16;

    private static final int ITERATION_COUNT = 65536;
    private static final int KEY_LENGTH = 256;

    @RequestMapping(value = "TPCODLService")
    public String TPCODLService() {
        String result = "";
        try {
            String password = "CeSuTpDdLTpCoDl#";//add the password shared with the mail.
            String salt = "mitraapplication";
            String encryptValue = "80030248407";
            result = encrypt(password, salt, encryptValue);
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static byte[] getRandomIv() {
        byte[] iv = new byte[IV_LENGTH];
        new SecureRandom().nextBytes(iv);
        return iv;
    }

    public static void main(String[] args) {
        try {
            String password = "CeSuTpDdLTpCoDl#";//add the password shared with the mail.
            String salt = "mitraapplication";
            String encryptValue = "80030248407";
            String result = encrypt(password, salt, encryptValue);
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static String encrypt(String password, String salt, String message) throws Exception {
        byte[] iv = getRandomIv();

        SecretKey secret = getSecretKey(password, salt);
        Cipher cipher = initCipher(Cipher.ENCRYPT_MODE, secret, iv);

        byte[] cipherText = cipher.doFinal(message.getBytes());
        byte[] cipherTextWithIv = ByteBuffer.allocate(iv.length + cipherText.length)
                .put(iv)
                .put(cipherText)
                .array();

        return Base64.getEncoder().encodeToString(cipherTextWithIv);
    }

    private static SecretKey getSecretKey(String password, String salt) throws Exception {
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), ITERATION_COUNT, KEY_LENGTH);
        SecretKeyFactory factory = SecretKeyFactory.getInstance(FACTORY_INSTANCE);

        return new SecretKeySpec(factory.generateSecret(spec).getEncoded(), ENCRYPTION_TYPE);
    }

    private static Cipher initCipher(final int mode, SecretKey secretKey, byte[] iv) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(mode, secretKey, new IvParameterSpec(iv));
        return cipher;
    }

}
