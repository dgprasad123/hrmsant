package hrms.service;

import hrms.common.DataBaseFunctions;
import hrms.dao.leaveapply.LeaveApplyDAO;
import hrms.model.leave.LeaveOswass;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import static org.apache.http.impl.client.HttpClients.createDefault;
import static org.apache.http.impl.conn.SchemeRegistryFactory.createDefault;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LeaveService {

    @Autowired
    public LeaveApplyDAO leaveApplyDAO;

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void sendLeaveDatatoOSWAS(String empid, int taskid) throws IOException, URISyntaxException {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = this.dataSource.getConnection();

            LeaveOswass leaveForm = leaveApplyDAO.getLeaveDataForOSWAS(empid, taskid);
            
            JSONObject job = new JSONObject(leaveForm);
            String JSON_STRING = job.toString();

            StringEntity requestEntity = new StringEntity(JSON_STRING, ContentType.APPLICATION_JSON);
            URIBuilder uriBuilder = new URIBuilder();
            uriBuilder.setScheme("http").setHost("117.239.20.27").setPort(8280).setPath("/digigov/receiveHrmsDtls/");

            DefaultHttpClient httpClient = new DefaultHttpClient();
            //CloseableHttpClient httpclient = createDefault();
            HttpPost postRequest = new HttpPost(uriBuilder.build());
            postRequest.addHeader("Content-Type", "application/json");
            postRequest.addHeader("token", "Basic bUY5VkV0eHBOK3JUYUF1TGJjM1FHRGh4N1hYejhYSEw=");
            postRequest.setEntity(requestEntity);
            HttpResponse response = httpClient.execute(postRequest);
            //CloseableHttpResponse response = httpclient.execute(postRequest);

            if (response.getStatusLine().getStatusCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatusLine().getStatusCode());
            }
            BufferedReader br = new BufferedReader(
                    new InputStreamReader((response.getEntity().getContent())));

            String output;
            String responseTxt = "";
            while ((output = br.readLine()) != null) {
                responseTxt = output;
            }
            httpClient.getConnectionManager().shutdown();
            //httpclient.close();
            if (responseTxt != null && !responseTxt.equals("")) {
                int resultFromServer = Integer.parseInt(responseTxt);
                String flag = "N";
                if (resultFromServer > 0) {
                    flag = "Y";
                }
                String sql = "update hw_emp_leave set oswas_sync=? where emp_id=? and task_id=?";
                pst = con.prepareStatement(sql);
                pst.setString(1, flag);
                pst.setString(2, empid);
                pst.setInt(3, taskid);
                pst.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

}
