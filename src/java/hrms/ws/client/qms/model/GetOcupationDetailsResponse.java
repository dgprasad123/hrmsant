/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.ws.client.qms.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author lenovo
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "getOcupationDetailsResult"
})
@XmlRootElement(name = "GetOcupationDetailsResponse")
public class GetOcupationDetailsResponse {
    @XmlElement(name = "GetOcupationDetailsResult")
    protected GetOcupationDetailsResponse.GetOcupationDetailsResult getOcupationDetailsResult;

    /**
     * Gets the value of the getOcupationDetailsResult property.
     * 
     * @return
     *     possible object is
     *     {@link GetOcupationDetailsResponse.GetOcupationDetailsResult }
     *     
     */
    public GetOcupationDetailsResponse.GetOcupationDetailsResult getGetOcupationDetailsResult() {
        return getOcupationDetailsResult;
    }

    /**
     * Sets the value of the getOcupationDetailsResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link GetOcupationDetailsResponse.GetOcupationDetailsResult }
     *     
     */
    public void setGetOcupationDetailsResult(GetOcupationDetailsResponse.GetOcupationDetailsResult value) {
        this.getOcupationDetailsResult = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;any/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "any"
    })
    public static class GetOcupationDetailsResult {

        @XmlAnyElement(lax = true)
        protected Object any;

        /**
         * Gets the value of the any property.
         * 
         * @return
         *     possible object is
         *     {@link Object }
         *     
         */
        public Object getAny() {
            return any;
        }

        /**
         * Sets the value of the any property.
         * 
         * @param value
         *     allowed object is
         *     {@link Object }
         *     
         */
        public void setAny(Object value) {
            this.any = value;
        }
    }
}
