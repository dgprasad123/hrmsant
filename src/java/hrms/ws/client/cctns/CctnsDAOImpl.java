/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.ws.client.cctns;

import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.sql.Connection;
import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.PreparedStatement;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class CctnsDAOImpl implements CctnsDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    public SOAPMessage createSOAPRequest(String soapAction,String wsType) throws Exception {
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();
        createSoapEnvelope(soapMessage,wsType);
        MimeHeaders headers = soapMessage.getMimeHeaders();
        headers.addHeader("SOAPAction", soapAction);
        soapMessage.saveChanges();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        soapMessage.writeTo(out);
        return soapMessage;
    }

    

    public void createSoapEnvelope(SOAPMessage soapMessage,String wsType) throws Exception {
        SOAPPart soapPart = soapMessage.getSOAPPart();
        String myNamespace = "tem";
        String myNamespaceURI = "http://tempuri.org/";
        SOAPEnvelope envelope = soapPart.getEnvelope();
        envelope.addNamespaceDeclaration(myNamespace, myNamespaceURI);
        SOAPBody soapBody = envelope.getBody();
        SOAPElement soapBodyElem = soapBody.addChildElement(wsType, myNamespace);
        SOAPElement soapBodyElem1 = soapBodyElem.addChildElement("hrms_no", myNamespace);
        soapBodyElem1.addTextNode("10007699");
    }
    public void saveRewardData(Element eElement) {
        Connection con = null;
        PreparedStatement pst = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("insert into emp_reward(rwd_id,not_id,not_type,emp_id,if_oth,oth_auth,rwd_lvl)values(?,?,?,?,?,?,?)");
            pst.setString(1, CommonFunctions.getMaxCode("emp_reward", "rwd_id", con));
            pst.setString(2, CommonFunctions.getMaxCode("emp_reward", "not_id", con));
            pst.setString(3, "REWARD");
            pst.setString(4, eElement.getElementsByTagName("HRMS_NO").item(0).getTextContent());
            pst.setString(5, null);
            pst.setString(6, eElement.getElementsByTagName("DO_DETAILS").item(0).getTextContent());
            pst.setString(7, null);
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
    }
     public void savePunishmentData(Element eElement) {
        Connection con = null;
        PreparedStatement pst = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("insert into emp_ad_action(not_id,not_type,emp_id,reason)values(?,?,?,?)");
            pst.setString(1, CommonFunctions.getMaxCode("emp_ad_action", "not_id", con));
            pst.setString(2, "ADM_ACTION");
            pst.setString(3, eElement.getElementsByTagName("HRMS_NO").item(0).getTextContent());
            pst.setString(4, eElement.getElementsByTagName("DO_DETAILS").item(0).getTextContent());
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
    }
     public void saveTrainingData(Element eElement) {
        Connection con = null;
        PreparedStatement pst = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("insert into emp_train(trainid,emp_id,title)values(?,?,?)");
            pst.setString(1, CommonFunctions.getMaxCode("emp_train", "trainid", con));
            pst.setString(2, eElement.getElementsByTagName("HRMS_NO").item(0).getTextContent());
            pst.setString(3, eElement.getElementsByTagName("DO_DETAILS").item(0).getTextContent());
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

}
