package hrms.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class SendSMS {

    public static void main(String sur[]) {
        String toPhoneNumber = "9937342090";
        String myMessage = "Hi";
        String encoded_message = URLEncoder.encode(myMessage);
        SendSMS sms = new SendSMS(toPhoneNumber, encoded_message);
        sms.send();

    }

    //Your authentication key
    //private final String authkey = "100519AGGVN9j22kO1567611c6";
    private final String authkey = "7wQMmD6UR0q2l4wnxM6rjA";
    //Multiple mobiles numbers separated by comma
    private String toPhoneNumber = null;
    //Sender ID,While using route4 sender id should be 6 characters long.
    private final String senderId = "ORTPSA";
    //Your message to send, Add URL encoding here.
    private String myMessage = null;
    //define route
    //private final String route = "4";
    private final String channel = "2";

    public SendSMS(String toPhoneNumber, String myMessage) {
        this.toPhoneNumber = toPhoneNumber;
        this.myMessage = myMessage;
    }

    public String send() {
        String response = null;
        //Prepare Url
        URLConnection myURLConnection = null;
        URL myURL = null;
        BufferedReader reader = null;

        //encoding message
        String encoded_message = URLEncoder.encode(myMessage);

        //Send SMS API
        //String mainUrl = "http://54.254.154.166/api/sendhttp.php?";
        String mainUrl = "http://164.52.195.161/API/SendMsg.aspx?";

        //Prepare parameter string
        StringBuilder sbPostData = new StringBuilder(mainUrl);
        //sbPostData.append("authkey=" + authkey);
        sbPostData.append("uname=" + "20230049");

        //sbPostData.append("&mobiles=" + toPhoneNumber);
        sbPostData.append("&pass=" + "D9JrBBlH");

        //sbPostData.append("&message=" + encoded_message);
        sbPostData.append("&send=" + "CMGHRM");
        //sbPostData.append("&route=" + route);
        sbPostData.append("&dest=" + "919132055693");
        sbPostData.append("&msg="+"Use OTP 89524 to reset your password. HRMS Odisha");
        //sbPostData.append("&sender=" + senderId);
        //sbPostData.append("&senderid=" + senderId);
     
     
        mainUrl = sbPostData.toString();
        try {
            //prepare connection
            myURL = new URL(mainUrl);
            myURLConnection = myURL.openConnection();
            myURLConnection.connect();
            reader = new BufferedReader(new InputStreamReader(myURLConnection.getInputStream()));
            //reading response

            while ((response = reader.readLine()) != null) //print response
            {
                System.out.println(response);
            }

            //finally close connection
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

}
