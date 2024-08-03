/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.eDespatch;

import com.fasterxml.jackson.databind.ObjectMapper;
import hrms.model.eDespatch.GetAddresseeInformationResponse;
import hrms.model.eDespatch.GetGroupInfoResponse;
import hrms.model.eDespatch.LetterNoResponseBean;
import hrms.model.eDespatch.SectionResponseBean;
import hrms.model.eDespatch.SigningAuthorityResponseBean;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

/**
 *
 * @author Manas
 */
public class eDespatchDAOImpl implements eDespatchDAO {

    public String getAuthToken() {
        String authWebToken = "";
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet postRequest = new HttpGet("http://audit.edodisha.gov.in/eD_WebApi/RequestToken/Get?username=eDSecurity&password=e_D_API-uri");
            HttpResponse response = httpClient.execute(postRequest);
            BufferedReader br = new BufferedReader(
                    new InputStreamReader((response.getEntity().getContent())));

            String output;
            //System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                authWebToken = output;
                System.out.println(output);
            }
            authWebToken = authWebToken.replaceAll("^\"|\"$", "");
            httpClient.getConnectionManager().shutdown();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return authWebToken;
    }

    @Override
    public void getofficeinfo(String officeCode) {
        try {

            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost postRequest = new HttpPost("http://audit.edodisha.gov.in/eD_WebApi/eDIntegration/getofficeinfo");
            postRequest.addHeader("accept", "application/json");
            postRequest.addHeader("Authorization", "Basic ZURTZWN1cml0eTplX0RfQVBJLXVyaQ==");
            //postRequest.addHeader("Authorization", "Bearer ZURTZWN1cml0eTplX0RfQVBJLXVyaQ==");
            HttpResponse response = httpClient.execute(postRequest);

            if (response.getStatusLine().getStatusCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatusLine().getStatusCode());
            }

            BufferedReader br = new BufferedReader(
                    new InputStreamReader((response.getEntity().getContent())));

            String output;
            //System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                System.out.println(output);
            }

            httpClient.getConnectionManager().shutdown();

        } catch (ClientProtocolException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    @Override
    public String genLetterNo(String officeCode) {
        String letterNo = "";
        try {
            String bearerToken = getAuthToken();
            JSONObject job = new JSONObject();
            job.put("office_code", officeCode);
            job.put("memo_no", 0);
            String JSON_STRING = job.toString();//"{\"office_code\":\"42924\"}";
            StringEntity requestEntity = new StringEntity(JSON_STRING, ContentType.APPLICATION_JSON);
            URIBuilder uriBuilder = new URIBuilder();
            uriBuilder.setScheme("http").setHost("audit.edodisha.gov.in").setPath("/eD_WebAPI/eDIntegration/GetLetterNo/");
            uriBuilder.setParameter("office_code", officeCode);

            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost postRequest = new HttpPost(uriBuilder.build());
            postRequest.addHeader("accept", "application/json");
            postRequest.addHeader("Authorization", "Bearer "+bearerToken);
            postRequest.setEntity(requestEntity);
            HttpResponse response = httpClient.execute(postRequest);
            ObjectMapper mapper = new ObjectMapper();
            LetterNoResponseBean apod = mapper.readValue(response.getEntity().getContent(), LetterNoResponseBean.class);
            letterNo = apod.getGenLetterNoResponse().get(0).getData().get(0).letter_no + "";
            System.out.println(letterNo);
            httpClient.getConnectionManager().shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return letterNo;
    }

    @Override
    public List getGroupList(String officeCode) {
        List groupList = new ArrayList();
        try {
            String bearerToken = getAuthToken();
            JSONObject job = new JSONObject();
            job.put("office_code", officeCode);
            String JSON_STRING = job.toString();//"{\"office_code\":\"42924\"}";
            StringEntity requestEntity = new StringEntity(JSON_STRING, ContentType.APPLICATION_JSON);
            URIBuilder uriBuilder = new URIBuilder();
            uriBuilder.setScheme("http").setHost("audit.edodisha.gov.in").setPath("/eD_WebAPI/eDIntegration/getGroups/");
            uriBuilder.setParameter("office_code", officeCode);

            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost postRequest = new HttpPost(uriBuilder.build());
            postRequest.addHeader("accept", "application/json");
            postRequest.addHeader("Authorization", "Bearer "+bearerToken);
            postRequest.setEntity(requestEntity);
            HttpResponse response = httpClient.execute(postRequest);
            ObjectMapper mapper = new ObjectMapper();

            GetGroupInfoResponse apod = mapper.readValue(response.getEntity().getContent(), GetGroupInfoResponse.class);
            groupList = apod.getGroupInfoResponse().get(0).getAddressGroup_list();
            httpClient.getConnectionManager().shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return groupList;
    }

    @Override
    public List getAddresseeList(String officeCode, String addresseeGroupCode) {
        List addresseeList = new ArrayList();
        try {
            String bearerToken = getAuthToken();
            JSONObject job = new JSONObject();
            job.put("office_code", officeCode);
            job.put("addresseeGroup_code", addresseeGroupCode);
            String JSON_STRING = job.toString();//"{\"office_code\":\"42924\"}";
            StringEntity requestEntity = new StringEntity(JSON_STRING, ContentType.APPLICATION_JSON);
            URIBuilder uriBuilder = new URIBuilder();
            uriBuilder.setScheme("http").setHost("audit.edodisha.gov.in").setPath("/eD_WebAPI/api/getAddressees/");
            //uriBuilder.setParameter("office_code", officeCode);

            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost postRequest = new HttpPost(uriBuilder.build());
            postRequest.addHeader("accept", "application/json");
            postRequest.addHeader("Authorization", "Bearer "+bearerToken);
            postRequest.setEntity(requestEntity);
            HttpResponse response = httpClient.execute(postRequest);
            ObjectMapper mapper = new ObjectMapper();

            GetAddresseeInformationResponse apod = mapper.readValue(response.getEntity().getContent(), GetAddresseeInformationResponse.class);
            addresseeList = apod.getAddresseeInformationResponse().get(0).getAddressee_list();
            httpClient.getConnectionManager().shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return addresseeList;
    }

    @Override
    public List getSigningAuthorityList(String officeCode) {
        List addresseeList = new ArrayList();
        try {
            String bearerToken = getAuthToken();
            JSONObject job = new JSONObject();
            job.put("office_code", officeCode);
            String JSON_STRING = job.toString();//"{\"office_code\":\"42924\"}";
            StringEntity requestEntity = new StringEntity(JSON_STRING, ContentType.APPLICATION_JSON);
            URIBuilder uriBuilder = new URIBuilder();
            uriBuilder.setScheme("http").setHost("audit.edodisha.gov.in").setPath("/eD_WebAPI/eDIntegration/GetSentDetails/");
            //uriBuilder.setParameter("office_code", officeCode);

            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost postRequest = new HttpPost(uriBuilder.build());
            postRequest.addHeader("accept", "application/json");
            postRequest.addHeader("Authorization", "Bearer "+bearerToken);
            postRequest.setEntity(requestEntity);
            HttpResponse response = httpClient.execute(postRequest);
            ObjectMapper mapper = new ObjectMapper();

            SigningAuthorityResponseBean apod = mapper.readValue(response.getEntity().getContent(), SigningAuthorityResponseBean.class);
            addresseeList = apod.getSigningAuthorityResponse().get(0).getData();
            httpClient.getConnectionManager().shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return addresseeList;
    }

    @Override
    public List getSectionList(String officeCode) {
        List sectionList = new ArrayList();
        try {
            String bearerToken = getAuthToken();
            JSONObject job = new JSONObject();
            job.put("office_code", officeCode);
            String JSON_STRING = job.toString();//"{\"office_code\":\"42924\"}";
            StringEntity requestEntity = new StringEntity(JSON_STRING, ContentType.APPLICATION_JSON);
            URIBuilder uriBuilder = new URIBuilder();
            uriBuilder.setScheme("http").setHost("audit.edodisha.gov.in").setPath("/eD_WebAPI/eDIntegration/GetSectionDetails/");
            //uriBuilder.setParameter("office_code", officeCode);

            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost postRequest = new HttpPost(uriBuilder.build());
            postRequest.addHeader("accept", "application/json");
            postRequest.addHeader("Authorization", "Bearer "+bearerToken);
            postRequest.setEntity(requestEntity);
            HttpResponse response = httpClient.execute(postRequest);
            ObjectMapper mapper = new ObjectMapper();

            SectionResponseBean apod = mapper.readValue(response.getEntity().getContent(), SectionResponseBean.class);
            sectionList = apod.getSectionResponse().get(0).getData();
            httpClient.getConnectionManager().shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sectionList;
    }
}
