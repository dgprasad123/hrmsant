/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.master;

import java.io.Serializable;

public class District implements Serializable {

    private String distCode = null;
    private String distName = null;
    private String stateCode = null;
    private String hiddistCode = null;
    private String activedist = null;
    
    private String hidstateCode= null;

    
    
    
    public String getHidstateCode() {
        return hidstateCode;
    }

    public void setHidstateCode(String hidstateCode) {
        this.hidstateCode = hidstateCode;
    }
    public String getActivedist() {
        return activedist;
    }

    public void setActivedist(String activedist) {
        this.activedist = activedist;
    }
    
    public String getHiddistCode() {
        return hiddistCode;
    }

    public void setHiddistCode(String hiddistCode) {
        this.hiddistCode = hiddistCode;
    }

    public String getDistCode() {
        return distCode;
    }

    public void setDistCode(String distCode) {
        this.distCode = distCode;
    }

    public String getDistName() {
        return distName;
    }

    public void setDistName(String distName) {
        this.distName = distName;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }
}
