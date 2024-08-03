/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.ws.client.ifms.model;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
/**
 *
 * @author Manas
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getScroll", propOrder = {
    "arg0"
})
@XmlRootElement(name = "getScroll")
public class GetScroll {
    protected String arg0;

    public String getArg0() {
        return arg0;
    }

    public void setArg0(String arg0) {
        this.arg0 = arg0;
    }
        
}
