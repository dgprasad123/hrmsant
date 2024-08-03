/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import hrms.common.CommonFunctions;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.net.ssl.SSLContext;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

/**
 *
 * @author Manoj PC
 */
public class UpdateIfmsPran {
    public static void main(String sur[]) {
        try {
            String responseString = "";
            final byte[] secretKey = CommonFunctions.readKeyFromFile("E:\\HRMS.key");
            String finalMsg = "";
            ObjectMapper mapper = new ObjectMapper();
            String empId = "";
            /*------*/

            Connection conpsql = null;

            PreparedStatement pst = null;
            ResultSet rs = null;
            Class.forName("org.postgresql.Driver");
            //conpsql = DriverManager.getConnection("jdbc:postgresql://172.16.1.14:6432/hrmis", "hrmis2", "cmgi");
            conpsql = DriverManager.getConnection("jdbc:postgresql://192.168.1.19/hrmis", "hrmis2", "hrmis2");
            String sql1 = "select DISTINCT emp_id from nps_hrmsid_request";
            sql1+= " where is_pran_updated = 'N' ";
            sql1+= "  AND emp_id IN('91339454', '96310291', '96310394', '96284621', '96045032', '91316208', '91333924', '91333921', '91333920', '91333926', '91339857', '91192914', '91079931', '96296293', '91179760', '91192489', '91339135', '91340203', '91339361', '91164750', '91328079', '91243180', '91243079', '91164751', '91205588', '91315552', '91187877', '91073337', '91301139', '91187228', '91339719', '91339494', '91339658', '91339745', '91339746', '91339747', '91341660', '96339943', '91315286', '91340692', '91340856', '91340799', '91340752', '91340689', '91340694', '91340688', '91340973', '91340705', '91340800', '91340690', '91198294', '91198619', '96262380', '91198620', '96262130', '91046372', '96262285', '91034287', '91336219', '96296271', '91311353', '91311354', '91310316', '91341632', '91334477', '96287027', '91283667', '91339718', '96247451', '96247457', '91316505', '91340732', '91340416', '91338791', '96283293', '91338792', '91201374', '91341633', '91339709', '91070098', '91078916', '91315850', '96265903', '91279280', '91338831', '91244972', '91244905', '91244770', '91244937', '91244952', '91244961', '91244809', '91244788', '91338166', '91338157', '91334218', '91286508', '96326404') LIMIT 2";
                    //sql1+=" AND (SELECT COUNT(*) FROM temp_pran_emp WHERE empid = EM.emp_id) = 0 ORDER BY emp_id LIMIT 100";
            // sql1+= " AND emp_id = '91339852'";
            pst = conpsql.prepareStatement(sql1);
            rs = pst.executeQuery();
            while (rs.next()) {
                empId = rs.getString("emp_id");
                System.out.println("EmpID: " + empId);
                final String hmac = CommonFunctions.getBase64String(CommonFunctions.genHmac(empId.getBytes("UTF-8"), secretKey));
                System.out.println("hmac***" + hmac);
                String encryptedText = CommonFunctions.getBase64String(CommonFunctions.encrypt(empId.getBytes("UTF-8"), secretKey));
                SSLSocketFactory sf = null;
                SSLContext context = null;
                String encryptedPassword;
                context = SSLContext.getInstance("TLSv1.2"); // Use this line for Java version 7 and above
                context.init(null, null, null);
                sf = new SSLSocketFactory(context, SSLSocketFactory.STRICT_HOSTNAME_VERIFIER);
                Scheme scheme = new Scheme("https", 443, sf);
                HttpClient client = new DefaultHttpClient();
                client.getConnectionManager().getSchemeRegistry().register(scheme);
                HttpPost post = new HttpPost("https://www.odishatreasury.gov.in/webservices/hrms/v1.0/get-pran-status");
                StringEntity params = new StringEntity("{\"data\":\"" + encryptedText + "\",\"hmac\":\"" + hmac + "\"}");
                post.setEntity(params);
                //table: temp_pran_emp
                post.addHeader("content-type", "application/json");
                HttpResponse response = client.execute(post);
                BufferedReader bf = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                String line = "";
                while ((line = bf.readLine()) != null) {
                    responseString = responseString + line;
                    //JSONArray arr = new JSONArray(responseString);   
                    System.out.println(responseString);
                    /*String sql = "INSERT INTO temp_pran_emp(empid, status) values(?,?)";
                     //System.out.println("SQL:"+sql);
                     pst = conpsql.prepareStatement(sql);

                     pst.setString(1, empId);
                     pst.setString(2, "No");
                     pst.executeUpdate();*/
                }
                //System.out.println(responseString);

                if (responseString.subSequence(0, 11).equals("{\"status\":1")) {
                    System.out.println("No Result");
                } else {
                    String[] arrReturn = responseString.split("\"data\":\"");
                     String str = arrReturn[1].replace("\"}", "");
                     String dStr = new String(CommonFunctions.decrypt(str, secretKey));
                    // System.out.println("Decoded string: " + dStr);
                     /*JSONArray arr = new JSONArray(dStr); 
                     JSONObject object = arr.getJSONObject(0);  
                     String pran = object.getString("pran");
                     System.out.println("Pran:"+pran);*/
                     
                     JSONObject dStrJSON = new JSONObject(dStr);
                     String pran = dStrJSON.getString("pran");
                     String sql = "UPDATE emp_mast SET if_gpf_assumed = 'N', gpf_no = ? WHERE emp_id = ? AND acct_type = 'PRAN'";
                    //System.out.println("SQL:"+sql);
                     pst = conpsql.prepareStatement(sql);

                     pst.setString(1, pran);
                     pst.setString(2, empId);
                     pst.executeUpdate();
                     
                     sql = "UPDATE nps_hrmsid_request SET is_pran_updated = 'Y' WHERE emp_id = ?";
                     //System.out.println("SQL:"+sql);
                     pst = conpsql.prepareStatement(sql);

                     pst.setString(1, empId);
                     pst.executeUpdate();                     
                }
            }

            /*JSONObject object = new JSONObject(responseString);  
             String returnData = object.getString("data");
             System.out.println("encryptedText="+encryptedText);
             Map<String, String> map = new HashMap<String, String>();
             map.put("data", encryptedText);
             map.put("hmac", hmac);
             String str=returnData;
            
             String dStr = new String(CommonFunctions.decrypt(str,secretKey));  
             System.out.println("Decoded string: "+dStr);  
             finalMsg = mapper.writeValueAsString(map);*/
        } catch (Exception e) {

        }
    }
}
