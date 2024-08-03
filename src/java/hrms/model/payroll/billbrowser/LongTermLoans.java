/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.payroll.billbrowser;

import java.util.ArrayList;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Manas Jena
 */
@XmlRootElement(name="loanrecovery")
@XmlAccessorType (XmlAccessType.FIELD)
public class LongTermLoans {
    @XmlElement(name = "loan")
    private ArrayList<LtaBeanForAG> ltaBeanForAG;

    public ArrayList<LtaBeanForAG> getLtaBeanForAG() {
        return ltaBeanForAG;
    }

    public void setLtaBeanForAG(ArrayList<LtaBeanForAG> ltaBeanForAG) {
        this.ltaBeanForAG = ltaBeanForAG;
    }
    
    
}
