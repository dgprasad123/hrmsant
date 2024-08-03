/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.master;

import java.io.Serializable;

/**
 *
 * @author Manas Jena
 */
public class Treasury implements Serializable{
    private String treasuryCode;
    private String treasuryName;
    private String officeCode;
    private String agtreasuryCode;
    private String distCode;

    public String getTreasuryCode() {
        return treasuryCode;
    }

    public void setTreasuryCode(String treasuryCode) {
        this.treasuryCode = treasuryCode;
    }

    public String getTreasuryName() {
        return treasuryName;
    }

    public void setTreasuryName(String treasuryName) {
        this.treasuryName = treasuryName;
    }

    public String getOfficeCode() {
        return officeCode;
    }

    public void setOfficeCode(String officeCode) {
        this.officeCode = officeCode;
    }

    public String getAgtreasuryCode() {
        return agtreasuryCode;
    }

    public void setAgtreasuryCode(String agtreasuryCode) {
        this.agtreasuryCode = agtreasuryCode;
    }

    public String getDistCode() {
        return distCode;
    }

    public void setDistCode(String distCode) {
        this.distCode = distCode;
    }
    
    
}
