/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.common;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.json.JSONObject;

/**
 *
 * @author lenovo
 */
public class LoanRestService {

    private static CloseableHttpClient createAcceptSelfSignedCertificateClient()
            throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {

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
            }
        };
        // use the TrustSelfSignedStrategy to allow Self Signed Certificates
        /*SSLContext sslContext = SSLContextBuilder
         .create()
         .loadTrustMaterial(new TrustSelfSignedStrategy())
         .build();*/
        SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
        // we can optionally disable hostname verification. 
        // if you don't want to further weaken the security, you don't have to include this.
        /*HostnameVerifier allowAllHosts = HttpsURLConnection.getDefaultHostnameVerifier();*/
        HostnameVerifier allowAllHosts = new HostnameVerifier() {
            @Override
            public boolean verify(String arg0, SSLSession arg1) {
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

    public static void main(String args[]) {
        try {

            URIBuilder uriBuilder = new URIBuilder();
            //https://uat.odishatreasury.gov.in/bdbillreceivingws/0.1/authenticate
            uriBuilder.setScheme("https").setHost("uat.odishatreasury.gov.in").setPath("/bdbillreceivingws/0.1/authenticate/");

            HttpPost postRequest = new HttpPost(uriBuilder.build());
            postRequest.addHeader("accept", "application/json");
            postRequest.addHeader("clientId", "HRMS");
            postRequest.addHeader("clientSecret", "ofh3G4L7hU/TvBhYz0RsgK0mb1FF7aVKZRm7TVfOpcA=");
            Ifms a = new Ifms();
           // String appKey = a.getAppKey();
          //  System.out.println("appkey=" + appKey);
            HashMap app = new HashMap();
          //  app.put("appKey", appKey);
            JSONObject job = new JSONObject(app);
            String JSON_STRING = job.toString();
            System.out.println("JSON_STRING is: " + JSON_STRING);

            StringEntity requestEntity = new StringEntity(JSON_STRING, ContentType.APPLICATION_JSON);

            //DefaultHttpClient httpClient = new DefaultHttpClient();
            CloseableHttpClient httpClient = createAcceptSelfSignedCertificateClient();
            postRequest.setEntity(requestEntity);
            HttpResponse response = httpClient.execute(postRequest);

            if (response.getStatusLine().getStatusCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatusLine().getStatusCode());
            }

            BufferedReader br = new BufferedReader(
                    new InputStreamReader((response.getEntity().getContent())));

            String output;
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
             System.out.println(output);
                
            }
            httpClient.getConnectionManager().shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
