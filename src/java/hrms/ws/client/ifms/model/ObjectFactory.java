/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.ws.client.ifms.model;

import hrms.ws.client.ifms.model.GetScrollResponse;
import hrms.ws.client.ifms.model.GetScroll;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 *
 * @author Manas
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _GetScrollResponse_QNAME = new QName("http://hrms.ifms.cmcltd.com/", "getScrollResponse");
    private final static QName _GetScroll_QNAME = new QName("http://hrms.ifms.cmcltd.com/", "getScroll");

    /**
     * Create a new ObjectFactory that can be used to create new instances of
     * schema derived classes for package: com.cmcltd.ifms.hrms
     *
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetScroll }
     *
     */
    public GetScroll createGetScroll() {
        return new GetScroll();
    }

    /**
     * Create an instance of {@link GetScrollResponse }
     *
     */
    public GetScrollResponse createGetScrollResponse() {
        return new GetScrollResponse();
    }

    /**
     * Create an instance of
     * {@link JAXBElement }{@code <}{@link GetScrollResponse }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://hrms.ifms.cmcltd.com/", name = "getScrollResponse")
    public JAXBElement<GetScrollResponse> createGetScrollResponse(final GetScrollResponse value) {
        return new JAXBElement<GetScrollResponse>(_GetScrollResponse_QNAME, GetScrollResponse.class, null, value);
    }

    /**
     * Create an instance of
     * {@link JAXBElement }{@code <}{@link GetScroll }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://hrms.ifms.cmcltd.com/", name = "getScroll")
    public JAXBElement<GetScroll> createGetScroll(final GetScroll value) {
        return new JAXBElement<GetScroll>(_GetScroll_QNAME, GetScroll.class, null, value);
    }
}
