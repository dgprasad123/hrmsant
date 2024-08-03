package hrms.common;

import java.io.*;
import java.net.*;

public class SMSHttpPostClient {

    static String username = "HRMSODI";
    static String password = "hrms#321#";
    static String senderid = "ODHRMS";
    static String message = "Test SMS from HRMS Team!";
    static String mobileNo = "9438558578";
    static String mobileNos = "9438558578,9658037377,";
    // StartTime Format: YYYYMMDD hh:mm:ss
    static String scheduledTime = "20161230 02:27:00";
    static HttpURLConnection connection = null;

    public SMSHttpPostClient(String mobileNo, String message) {
        this.username = username;
        this.password = password;
        this.senderid = senderid;
        this.mobileNo = mobileNo;
        this.message = message;
    }

    public static void main(String[] args) {

        try {
            URL url = new URL("http://msdgweb.mgov.gov.in/esms/sendsmsrequest");
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setFollowRedirects(true);
            connection = sendSingleSMS(username, password, senderid, mobileNo, message);
            // connection = sendBulkSMS(username, password, senderid, mobileNos,message);

            

        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static String send_sms() {
        String msgdeliver = " Ignore Message if not relevant to you.Thanks";
        try {
            URL url = new URL("http://msdgweb.mgov.gov.in/esms/sendsmsrequest");
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setFollowRedirects(true);
            connection = sendSingleSMS(username, password, senderid, mobileNo, message);
            // connection = sendBulkSMS(username, password, senderid, mobileNos,message);

            
            msgdeliver = connection.getResponseCode() + "-" + connection.getResponseMessage() + "-" + connection.getHeaderField("Date") + "-" + connection.getDate() + " ";

        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return msgdeliver;
    }

    // Method for sending single SMS.
    public static HttpURLConnection sendSingleSMS(String username,
            String password, String senderId,
            String mobileNo, String message) {
        try {
            String smsservicetype = "singlemsg"; // For single message.
            String query = "username=" + URLEncoder.encode(username, "UTF-8")
                    + "&password=" + URLEncoder.encode(password, "UTF-8")
                    + "&smsservicetype=" + URLEncoder.encode(smsservicetype, "UTF-8")
                    + "&content=" + URLEncoder.encode(message, "UTF-8") + "&mobileno="
                    + URLEncoder.encode(mobileNo, "UTF-8") + "&senderid="
                    + URLEncoder.encode(senderId, "UTF-8");

            connection.setRequestProperty("Content-length", String
                    .valueOf(query.length()));
            connection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            connection.setRequestProperty("User-Agent",
                    "Mozilla/4.0 (compatible; MSIE 5.0; Windows 98; DigExt)");

            // open up the output stream of the connection
            DataOutputStream output = new DataOutputStream(connection
                    .getOutputStream());

            // write out the data
            int queryLength = query.length();
            output.writeBytes(query);
					// output.close();

            // get ready to read the response from the cgi script
            DataInputStream input = new DataInputStream(connection
                    .getInputStream());

            // read in each character until end-of-stream is detected
            
            input.close();
        } catch (Exception e) {
            
            e.printStackTrace();
        }

        return connection;
    }

    // method for sending bulk SMS
    public static HttpURLConnection sendBulkSMS(String username,
            String password, String senderId, String mobileNos, String message) {
        try {
            String smsservicetype = "bulkmsg"; // For bulk msg
            String query = "username=" + URLEncoder.encode(username)
                    + "&password=" + URLEncoder.encode(password)
                    + "&smsservicetype=" + URLEncoder.encode(smsservicetype)
                    + "&content=" + URLEncoder.encode(message)
                    + "&bulkmobno=" + URLEncoder.encode(mobileNos, "UTF-8")
                    + "&senderid=" + URLEncoder.encode(senderid);

            connection.setRequestProperty("Content-length", String
                    .valueOf(query.length()));
            connection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            connection.setRequestProperty("User-Agent",
                    "Mozilla/4.0 (compatible; MSIE 5.0; Windows 98; DigExt)");

            // open up the output stream of the connection
            DataOutputStream output = new DataOutputStream(connection
                    .getOutputStream());

            // write out the data
            int queryLength = query.length();
            output.writeBytes(query);
					// output.close();

           

            // get ready to read the response from the cgi script
            DataInputStream input = new DataInputStream(connection
                    .getInputStream());

            // read in each character until end-of-stream is detected
            
            input.close();
        } catch (Exception e) {
            
            e.printStackTrace();
        }
        return connection;
    }

    public static HttpURLConnection sendSingleUicodeSMS(String username,
            String password, String senderId, String mobileNo, String message) {
        try {
            URL url = new URL("http://msdgweb.mgov.gov.in/esms/sendsmsrequest");

            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setFollowRedirects(true);

            String finalmessage = "";
            String sss = "";
            char ch = 0;
            //	String smsservicetype = "singlemsg"; // For single message.
            String smsservicetype = "unicodemsg";

            for (int i = 0; i < message.length(); i++) {

                ch = message.charAt(i);
                int j = (int) ch;
			

                sss = "&#" + j + ";";
                finalmessage = finalmessage + sss;
            }
        

            message = finalmessage;
            
            String query = "username=" + URLEncoder.encode(username)
                    + "&password=" + URLEncoder.encode(password)
                    + "&smsservicetype=" + URLEncoder.encode(smsservicetype)
                    + "&content=" + URLEncoder.encode(message) + "&mobileno="
                    + URLEncoder.encode(mobileNo) + "&senderid="
                    + URLEncoder.encode(senderId);

            connection.setRequestProperty("Content-length", String
                    .valueOf(query.length()));
            connection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            connection.setRequestProperty("User-Agent",
                    "Mozilla/4.0 (compatible; MSIE 5.0; Windows 98; DigExt)");

            // open up the output stream of the connection
            DataOutputStream output = new DataOutputStream(connection
                    .getOutputStream());

            // write out the data
            int queryLength = query.length();
            output.writeBytes(query);
				// output.close();

            // get ready to read the response from the cgi script
            DataInputStream input = new DataInputStream(connection
                    .getInputStream());

            // read in each character until end-of-stream is detected
            
            input.close();
        } catch (Exception e) {
            
            e.printStackTrace();
        }

        return connection;
    }

    public static HttpURLConnection sendBulkUnicodeSMS(String username,
            String password, String senderId, String mobileNos, String message) {
        try {

            
            String finalmessage = "";
            String sss = "";
            char ch = 0;

            for (int i = 0; i < message.length(); i++) {

                ch = message.charAt(i);
                int j = (int) ch;
		

                sss = "&#" + j + ";";
                finalmessage = finalmessage + sss;
            }
          

            message = finalmessage;
         

            String smsservicetype = "unicodemsg"; // For bulk msg
            String query = "username=" + URLEncoder.encode(username)
                    + "&password=" + URLEncoder.encode(password)
                    + "&smsservicetype=" + URLEncoder.encode(smsservicetype)
                    + "&content=" + URLEncoder.encode(message)
                    + "&bulkmobno=" + URLEncoder.encode(mobileNos, "UTF-8")
                    + "&senderid=" + URLEncoder.encode(senderid);

            connection.setRequestProperty("Content-length", String
                    .valueOf(query.length()));
            connection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            connection.setRequestProperty("User-Agent",
                    "Mozilla/4.0 (compatible; MSIE 5.0; Windows 98; DigExt)");

            // open up the output stream of the connection
            DataOutputStream output = new DataOutputStream(connection
                    .getOutputStream());

            // write out the data
            int queryLength = query.length();
            output.writeBytes(query);
					// output.close();

            
            // get ready to read the response from the cgi script
            DataInputStream input = new DataInputStream(connection
                    .getInputStream());

            // read in each character until end-of-stream is detected
            
            input.close();
        } catch (Exception e) {
            
            e.printStackTrace();
        }
        return connection;
    }
}
				

			/*


Operator Name

Operator     Code

Aircel, Dishnet Wireless        D

Airtel                          A

BSNL    			B

BPL Mobile/Loop Telecom 	L

Idea Cellular 			 I

MTNL           			M

Reliance Communications      R

Reliance Telecom      		 E
		
Tata Teleservices      		  T

Unitech          		 U

Vodafone       			 V

Location of the operator

Operator      Code

Andhra PradeshA

Assam  S

Bihar    B

Delhi    D

Gujarat G

HaryanaH

Himachal Pradesh           I

Jammu & Kashmir          J

Karnataka           X

Kerala  		  L

Kolkata 		 K

Madhya Pradesh		Y

Maharashtra       Z

MumbaiM

North East          N

Orissa  	  O

Punjab  		 P

Rajasthan           R

Tamil Nadu          T

UP-East 		 E

UP-West 	W

West Bengal         V




*/
