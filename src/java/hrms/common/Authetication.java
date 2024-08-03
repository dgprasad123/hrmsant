/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ifms;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

/**
 *
 * @author Manas
 */
public class Authetication {

    public byte[] readFileBytes(String filename) throws IOException {
        Path path = Paths.get(filename);
        return Files.readAllBytes(path);
    }

    public PublicKey readPublicKey(String filename) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        X509EncodedKeySpec publicSpec = new X509EncodedKeySpec(readFileBytes(filename));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(publicSpec);
    }

    public byte[] encrypt(PublicKey key, byte[] plaintext) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA1AndMGF1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(plaintext);
    }

    public static SecretKey getAESKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256, SecureRandom.getInstanceStrong());
        SecretKey appKey = keyGen.generateKey();
        //byte[] bytesArray = Base64.encodeBase64URLSafe(appKey.getEncoded());
        String str = Base64.getEncoder().encodeToString(appKey.getEncoded());
        System.out.println(str);
        return appKey;
    }

    public static void main(String[] args) {
        Authetication a = new Authetication();

        try {
            SecretKey appKey = Authetication.getAESKey();//Get 256 Unique Key
            PublicKey publicKey = a.readPublicKey("E:\\IFMS Loan Integration\\toHRMS\\HRMS\\publicKey");
            byte[] clientSecret = appKey.getEncoded();
            byte[] secret = a.encrypt(publicKey, clientSecret);

            String str = Base64.getEncoder().encodeToString(secret);
            System.out.println("--------------");
            System.out.println(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    public static void authorizationCall() {
        try {
            
            URL url = new URL("https://uat.odishatreasury.gov.in/bdbillreceivingws/0.1/authenticate");
            URLConnection urlc = url.openConnection();
            String query = "{'appKey':'hr0oEqhaiJvfzg0uMB3RmD8cYdeHuAu2jSQY7gB6vZqY2VNyzDfsaljNb5uCb61gxv9Gm5JP5fJeR9hfAIY3vBKpnSbKGt9NBozCzMkgyIPjo0odFJ/l0+yFIRRVSPEfqDWB8sYf0+3u4bXkgRU9xLuA/3tt7fDtVlX1gbe4fxSRSBLU/nuzwv7JjfHzWQ/XcS2V1cBz7u8jx/qtcMlXNeHD8uopVJaUlrU34SkEwG/DhFCiyc2VRIK18GYdXEsvBPkc160x04qvx2HUDmmDwZdneHQ5vc3/pzJkWRYLGLwVkMARJGN17MSl5fy68jrrRHWeepcD5rt6bRj4gbAM9w=='}";
            //use post mode
            urlc.setDoOutput(true);
            urlc.setAllowUserInteraction(false);
            urlc.setRequestProperty("Content-Type", "application/json");
            urlc.setRequestProperty("clientId", "HRMS");
            urlc.setRequestProperty("clientSecret", "ofh3G4L7hU/TvBhYz0RsgK0mb1FF7aVKZRm7TVfOpcA=");
            //send query
            PrintStream ps = new PrintStream(urlc.getOutputStream());
            ps.print(query);
            ps.close();

            //get result
            BufferedReader br = new BufferedReader(new InputStreamReader(urlc.getInputStream()));
            String l = null;
            while ((l = br.readLine()) != null) {
                System.out.println(l);
            }
            br.close();
        } catch (Exception exp) {
            exp.printStackTrace();
        }
    }
}
