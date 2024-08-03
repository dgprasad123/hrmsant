/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.ws.client.ifms;
import hrms.ws.client.ifms.model.ObjectFactory;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;
/**
 *
 * @author Manas
 */
@WebService(name = "HrmsScrollDownload", targetNamespace = "http://hrms.ifms.cmcltd.com/")
@XmlSeeAlso({
	ObjectFactory.class
})
public interface HrmsScrollDownload {

    /**
     *
     * @param arg0
     * @return returns java.lang.String
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getScroll", targetNamespace = "http://hrms.ifms.cmcltd.com/", className = "hrms.ws.client.ifms.model.GetScroll")
    @ResponseWrapper(localName = "getScrollResponse", targetNamespace = "http://hrms.ifms.cmcltd.com/", className = "hrms.ws.client.ifms.model.GetScrollResponse")
    public String getScroll(@WebParam(name = "arg0", targetNamespace = "") String arg0);
}
