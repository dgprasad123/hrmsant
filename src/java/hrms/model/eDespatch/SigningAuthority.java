/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.eDespatch;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author Manas
 */
public class SigningAuthority {
    @JsonProperty("Signing_authorityCode") 
    public int signing_authorityCode;
    @JsonProperty("Signing_authorityName") 
    public String signing_authorityName;

    public int getSigning_authorityCode() {
        return signing_authorityCode;
    }

    public void setSigning_authorityCode(int signing_authorityCode) {
        this.signing_authorityCode = signing_authorityCode;
    }

    public String getSigning_authorityName() {
        return signing_authorityName;
    }

    public void setSigning_authorityName(String signing_authorityName) {
        this.signing_authorityName = signing_authorityName;
    }
    
    
}
