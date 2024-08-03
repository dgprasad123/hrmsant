/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.cctnsws;

import hrms.ws.client.cctns.CctnsDAO;
import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
@Controller
public class CctnsController {
    @Autowired
    public CctnsDAO cctnsDAO;
    @RequestMapping(value = "callRewardSoapWS.htm", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView callRewardSoapWS() throws Exception {
        ModelAndView mav = new ModelAndView();
        String soapEndpointUrl = "https://citizenportal-op.gov.in/DO_DATASERVICE/HRMSDO.asmx";
        String soapAction = "http://tempuri.org/GetRewardData";
        //cctnsDAO.callSoapWebService(soapEndpointUrl, soapAction);
        SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
        SOAPConnection soapConnection = soapConnectionFactory.createConnection();
         SOAPMessage soapResponse = soapConnection.call(cctnsDAO.createSOAPRequest(soapAction,"GetRewardData"), soapEndpointUrl);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        soapResponse.writeTo(out);
        String strMsg = new String(out.toByteArray());
        
        
            // soapResponse.writeTo(System.out);
        
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        InputSource is = new InputSource();
        is.setCharacterStream(new StringReader(strMsg));
        Document doc = dBuilder.parse(is);
        doc.getDocumentElement().normalize();
        
        NodeList nList = doc.getElementsByTagName("Table");
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                    cctnsDAO.saveRewardData(eElement);
            }
        }

        soapConnection.close();
        mav.setViewName("");
        return mav;
    }
    @RequestMapping(value = "callPunishmentSoapWS.htm", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView callPunishmentSoapWS() throws Exception {
        ModelAndView mav = new ModelAndView();
        String soapEndpointUrl = "https://citizenportal-op.gov.in/DO_DATASERVICE/HRMSDO.asmx";
        String soapAction = "http://tempuri.org/GetPunishmentData";
        //cctnsDAO.callSoapWebService(soapEndpointUrl, soapAction);
        SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
        SOAPConnection soapConnection = soapConnectionFactory.createConnection();
         SOAPMessage soapResponse = soapConnection.call(cctnsDAO.createSOAPRequest(soapAction,"GetPunishmentData"), soapEndpointUrl);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        soapResponse.writeTo(out);
        String strMsg = new String(out.toByteArray());
        
        
            // soapResponse.writeTo(System.out);
        
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        InputSource is = new InputSource();
        is.setCharacterStream(new StringReader(strMsg));
        Document doc = dBuilder.parse(is);
        doc.getDocumentElement().normalize();
        
        NodeList nList = doc.getElementsByTagName("Table");
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                    cctnsDAO.savePunishmentData(eElement);
            }
        }

        soapConnection.close();
        mav.setViewName("");
        return mav;
    }
    @RequestMapping(value = "callTrainingSoapWS.htm", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView callTrainingSoapWS() throws Exception {
        ModelAndView mav = new ModelAndView();
        String soapEndpointUrl = "https://citizenportal-op.gov.in/DO_DATASERVICE/HRMSDO.asmx";
        String soapAction = "http://tempuri.org/GetTrainingData";
        //cctnsDAO.callSoapWebService(soapEndpointUrl, soapAction);
        SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
        SOAPConnection soapConnection = soapConnectionFactory.createConnection();
         SOAPMessage soapResponse = soapConnection.call(cctnsDAO.createSOAPRequest(soapAction,"GetPunishmentData"), soapEndpointUrl);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        soapResponse.writeTo(out);
        String strMsg = new String(out.toByteArray());
        
        
            // soapResponse.writeTo(System.out);
        
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        InputSource is = new InputSource();
        is.setCharacterStream(new StringReader(strMsg));
        Document doc = dBuilder.parse(is);
        doc.getDocumentElement().normalize();
        
        NodeList nList = doc.getElementsByTagName("Table");
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                    cctnsDAO.saveTrainingData(eElement);
            }
        }

        soapConnection.close();
        mav.setViewName("");
        return mav;
    }
}
