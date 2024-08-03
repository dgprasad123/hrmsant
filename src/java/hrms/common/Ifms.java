/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import hrms.model.loanworkflow.IFMSAuthObject;
import hrms.model.loanworkflow.LoanBillAck;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import org.json.JSONObject;

/**
 *
 * @author Manas
 */
public class Ifms {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        
        //CommonFunctions.validateXMLSchema("E:\\xmlloan\\EPB09010000000044001\\" + "bill-with-loan-dtls.xsd","E:\\xmlloan\\EPB09010000000044001\\" + "EPB09010000000044001.xml");
        
        SecretKey appKey = Ifms.getAESKey();//256 Unique Key        
        String appkey = Ifms.getAppKey(appKey);

        Ifms a = new Ifms();

        IFMSAuthObject ifmsAuthObject = Ifms.authorization(appkey);
        String sek = ifmsAuthObject.getData().getSek();
        System.out.println("====" + appKey.getEncoded().length);
        String str = Ifms.decrptyBySyymetricKey(sek, appKey.getEncoded());
        byte[] decSEK = Ifms.decrptyBySyymetricKeyByteArray(sek, appKey.getEncoded());        
        System.out.println("str:" + str);        
        FileInputStream in = new FileInputStream("E:\\xmlloan\\" + "EPB09010000000044100.zip");
        byte[] ba = IOUtils.toByteArray(in);
        String encryptedData = Ifms.encryptBySymmetricKey(ba, str);
        System.out.println("*************");
        System.out.println(ifmsAuthObject.getData().getAuthToken());
        LoanBillAck loanBillAck = Ifms.sendData(encryptedData, ifmsAuthObject.getData().getAuthToken());
        
        String Dataeny=loanBillAck.getData();
        
        System.out.println("*Status*" + loanBillAck.isStatus());
        System.out.println("*Data*" + loanBillAck.getData());
        System.out.println("*REK*" + loanBillAck.getRek());

        byte[] decryptedRek = Ifms.decrptyBySyymetricKeyByteArray(loanBillAck.getRek(), decSEK);
        
        byte[] datadec=Ifms.decrptyBySyymetricKeyByteArray(Dataeny, decryptedRek);
        String s = new String(datadec, StandardCharsets.UTF_8);
        
        System.out.println("*Data*" + s);

    }

    private static String encryptBySymmetricKey1(byte[] ba, String decryptedSek) {
        byte[] sekByte = Base64.getEncoder().encode(decryptedSek.getBytes());

        //byte[] sekByte = Base64.getDecoder().decode(decryptedSek);
        Key aesKey = new SecretKeySpec(sekByte, "AES");
        try {
            String dataStr = new String(Base64.getEncoder().encodeToString(ba));
            System.out.println("Original byte[]:" + dataStr);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, aesKey);
            byte[] encryptedjsonbytes = cipher.doFinal(dataStr.getBytes());
            String encryptedJson = Base64.getEncoder().encodeToString(encryptedjsonbytes);
            return encryptedJson;
        } catch (Exception e) {
            return "Exception " + e;
        }
    }

    private static String encryptBySymmetricKey(byte[] ba, String decryptedSek) {
        byte[] sekByte = Base64.getDecoder().decode(decryptedSek);
        Key aesKey = new SecretKeySpec(sekByte, "AES");
        try {
            String dataStr = new String(Base64.getEncoder().encodeToString(ba));
            System.out.println("Original byte[]:" + dataStr);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, aesKey);
            byte[] encryptedjsonbytes = cipher.doFinal(dataStr.getBytes("UTF-8"));//CHECK
            String encryptedJson = Base64.getEncoder().encodeToString(encryptedjsonbytes);
            return encryptedJson;
        } catch (Exception e) {
            return "Exception " + e;
        }
    }

    public static String decrptyBySyymetricKey(String encryptedSek, byte[] appKey) {
        Key aesKey = new SecretKeySpec(appKey, "AES"); // converts bytes(32 byte random generated) to key
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding"); // encryption type = AES with padding PKCS5
            cipher.init(Cipher.DECRYPT_MODE, aesKey); // initiate decryption type with the key
            byte[] encryptedSekBytes = Base64.getDecoder().decode(encryptedSek); // decode the base64 encryptedSek to bytes
            byte[] decryptedSekBytes = cipher.doFinal(encryptedSekBytes); // decrypt the encryptedSek with the initialized cipher containing the key(Results in bytes)
            String decryptedSek = Base64.getEncoder().encodeToString(decryptedSekBytes); // convert the decryptedSek(bytes) to Base64 StriNG
            return decryptedSek; // return results in base64 string
        } catch (Exception e) {
            e.printStackTrace();
            return "Exception; " + e;
        }
    }
    
    public static byte[] decrptyBySyymetricKeyByteArray(String encryptedSek, byte[] appKey) {
        Key aesKey = new SecretKeySpec(appKey, "AES"); // converts bytes(32 byte random generated) to key
        byte[] decryptedSekBytes = null;
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding"); // encryption type = AES with padding PKCS5
            cipher.init(Cipher.DECRYPT_MODE, aesKey); // initiate decryption type with the key
            byte[] encryptedSekBytes = Base64.getDecoder().decode(encryptedSek); // decode the base64 encryptedSek to bytes
            decryptedSekBytes = cipher.doFinal(encryptedSekBytes); // decrypt the encryptedSek with the initialized cipher containing the key(Results in bytes)            
             // return results in base64 string
        } catch (Exception e) {
            e.printStackTrace();            
        }
        return decryptedSekBytes;
    }

    public byte[] decrypt(SecretKey key, byte[] plaintext) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA1AndMGF1Padding");
        cipher.init(Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(plaintext);
    }

    public static String getAppKey(SecretKey appKey) {
        String appkey = null;

        Ifms a = new Ifms();
        try {
            PublicKey publicKey = a.readPublicKey("E:\\IFMS Loan Integration\\toHRMS\\HRMS\\publicKey");
            byte[] clientSecret = appKey.getEncoded();
            byte[] secret = a.encrypt(publicKey, clientSecret); //Secrete key byte array
            appkey = Base64.getEncoder().encodeToString(secret);// Secrte key byte arry converted to string
            //System.out.println(appkey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return appkey;
    }

    public static SecretKey getAESKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256, SecureRandom.getInstanceStrong());
        SecretKey appKey = keyGen.generateKey();
        //byte[] bytesArray = Base64.encodeBase64URLSafe(appKey.getEncoded());
        //String str = Base64.getEncoder().encodeToString(appKey.getEncoded());
        //System.out.println(str);
        return appKey;
    }

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

    private static CloseableHttpClient createAcceptSelfSignedCertificateClient()
            throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, CertificateException, IOException {

        TrustManager[] trustAllCerts = new TrustManager[]{
            new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                @Override
                public void checkClientTrusted(X509Certificate[] arg0, String arg1)
                        throws CertificateException {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] arg0, String arg1)
                        throws CertificateException {
                }

                public boolean isClientTrusted(X509Certificate[] chain) {
                    return true;
                }

                public boolean isServerTrusted(X509Certificate[] chain) {
                    return true;
                }
            }
        };
        // use the TrustSelfSignedStrategy to allow Self Signed Certificates
         /* SSLContext sslContext = SSLContextBuilder
         .create()
         .loadTrustMaterial(new TrustSelfSignedStrategy())
         .build();
         String keyPassphrase = "";
         KeyStore keyStore = KeyStore.getInstance("PKCS12");
         keyStore.load(new FileInputStream("D:\\IFMS Loan Integration\\toHRMS\\HRMS\\publicKey"), keyPassphrase.toCharArray());
         SSLContext sslContext = SSLContextBuilder.create().loadTrustMaterial(keyStore, null).build();*/

        SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
        // we can optionally disable hostname verification. 
        // if you don't want to further weaken the security, you don't have to include this.
        /*HostnameVerifier allowAllHosts = HttpsURLConnection.getDefaultHostnameVerifier();*/
        HostnameVerifier allowAllHosts = new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                HostnameVerifier hv = HttpsURLConnection.getDefaultHostnameVerifier();
                boolean t = hv.verify("localhost", session);
                System.out.println(t);
                return true;
            }
        };
        // create an SSL Socket Factory to use the SSLContext with the trust self signed certificate strategy
        // and allow all hosts verifier.
        SSLConnectionSocketFactory connectionFactory = new SSLConnectionSocketFactory(sslContext, allowAllHosts);
        //SSLConnectionSocketFactory connectionFactory = new SSLConnectionSocketFactory(SSLCertificateSocketFactory.getInsecure(0, null));

        // finally create the HttpClient using HttpClient factory methods and assign the ssl socket factory
        return HttpClients
                .custom()
                .setSSLSocketFactory(connectionFactory)
                .build();
    }

    public static IFMSAuthObject authorization(String appkey) {
        IFMSAuthObject ifms = null;
        try {

            URIBuilder uriBuilder = new URIBuilder();
            //https://uat.odishatreasury.gov.in/bdbillreceivingws/0.1/authenticate
            uriBuilder.setScheme("https").setHost("uat.odishatreasury.gov.in").setPath("/bdbillreceivingws/0.1/authenticate/");

            HttpPost postRequest = new HttpPost(uriBuilder.build());
            postRequest.addHeader("accept", "application/json");
            postRequest.addHeader("clientId", "HRMS");
            postRequest.addHeader("clientSecret", "ofh3G4L7hU/TvBhYz0RsgK0mb1FF7aVKZRm7TVfOpcA=");

            HashMap app = new HashMap();
            app.put("appKey", appkey);
            JSONObject job = new JSONObject(app);
            String JSON_STRING = job.toString();
            System.out.println("JSON_STRING is: " + JSON_STRING);

            StringEntity requestEntity = new StringEntity(JSON_STRING, ContentType.APPLICATION_JSON);

            //DefaultHttpClient httpClient = new DefaultHttpClient();
            CloseableHttpClient httpClient = createAcceptSelfSignedCertificateClient();

            //CloseableHttpClient httpClient = HttpClients.createDefault();
            postRequest.setEntity(requestEntity);
            HttpResponse response = httpClient.execute(postRequest);
            //  System.out.println("response==="+response);
            if (response.getStatusLine().getStatusCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatusLine().getStatusCode());
            }
            ObjectMapper Obj = new ObjectMapper();
            // System.out.println(response.getStatusLine() +"obj===" + response.getEntity().getContent());
            ifms = Obj.readValue(response.getEntity().getContent(), IFMSAuthObject.class);
            // System.out.println(ifms.getData().getSek());
            //}
            httpClient.getConnectionManager().shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ifms;
    }

    public static LoanBillAck sendData(String data, String authToken) {
        LoanBillAck loanBillAck = null;
        try {

            URIBuilder uriBuilder = new URIBuilder();
            //https://uat.odishatreasury.gov.in/bdbillreceivingws/0.1/authenticate
            uriBuilder.setScheme("https").setHost("uat.odishatreasury.gov.in").setPath("/bdbillreceivingws/0.1/submit-loan-bill/");

            HttpPost postRequest = new HttpPost(uriBuilder.build());
            //postRequest.addHeader("accept", "application/json");
            postRequest.addHeader("clientId", "HRMS");
            postRequest.addHeader("authToken", authToken);

            System.out.println("DATA: " + data);

            StringEntity requestEntity = new StringEntity(data);

            //DefaultHttpClient httpClient = new DefaultHttpClient();
            CloseableHttpClient httpClient = createAcceptSelfSignedCertificateClient();

            //CloseableHttpClient httpClient = HttpClients.createDefault();
            postRequest.setEntity(requestEntity);
            HttpResponse response = httpClient.execute(postRequest);

            if (response.getStatusLine().getStatusCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatusLine().getStatusCode());
            }
            
            ObjectMapper Obj = new ObjectMapper();
            loanBillAck = Obj.readValue(response.getEntity().getContent(), LoanBillAck.class);

            httpClient.getConnectionManager().shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return loanBillAck;
    }
}
