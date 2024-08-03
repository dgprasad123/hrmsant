/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.ws.client.cctns;

import javax.xml.soap.SOAPMessage;
import org.w3c.dom.Element;

/**
 *
 * @author lenovo pc
 */
public interface CctnsDAO {
   // public  void callSoapWebService(String soapEndpointUrl, String soapAction);
    public  SOAPMessage createSOAPRequest(String soapAction,String wsType)throws Exception;
    public  void createSoapEnvelope(SOAPMessage soapMessage,String wsType) throws Exception;
    public  void saveRewardData(Element eElement);
    public  void savePunishmentData(Element eElement);
    public void saveTrainingData(Element eElement);
}
