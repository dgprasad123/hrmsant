/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.ws.client.ifms;

import hrms.ws.client.ifms.model.GetScroll;
import hrms.ws.client.ifms.model.GetScrollResponse;
import java.io.StringReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 *
 * @author Manas
 */
public class VoucherClient extends WebServiceGatewaySupport {

    public GetScrollResponse HrmsScrollDownloadService(String date) {
        GetScroll request = new GetScroll();
        request.setArg0(date);
        GetScrollResponse response = (GetScrollResponse) getWebServiceTemplate().marshalSendAndReceive(request, new SoapActionCallback("https://www.odishatreasury.gov.in:443/webservices/HrmsScrollDownloadPort"));
        return response;
    }

    public static void main(String args[]) {
        /*AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
         ctx.register(ClientAppConfig.class);
         ctx.refresh();
        
         VoucherClient vc = ctx.getBean(VoucherClient.class);
         GetScrollResponse response = vc.HrmsScrollDownloadService("31-MAR-2017");
         */
        final HrmsScrollDownloadService service = new HrmsScrollDownloadService();
        final HrmsScrollDownload port = service.getHrmsScrollDownloadPort();
        String xmlStr = port.getScroll("31-MAR-2018");
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(xmlStr));

            Document doc = dBuilder.parse(is);
            doc.getDocumentElement().normalize();

            //System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

            NodeList nList = doc.getElementsByTagName("ROW");


            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);                
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    //System.out.println("HRMIS_BILL_NUMBER : " + eElement.getElementsByTagName("HRMIS_BILL_NUMBER").item(0).getTextContent());
                    //System.out.println("VOUCHER_NUMBER : " + eElement.getElementsByTagName("VOUCHER_NUMBER").item(0).getTextContent());
                    //System.out.println("Last Name : " + eElement.getElementsByTagName("lastname").item(0).getTextContent());
                    //System.out.println("Nick Name : " + eElement.getElementsByTagName("nickname").item(0).getTextContent());
                    //System.out.println("Salary : " + eElement.getElementsByTagName("salary").item(0).getTextContent());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
