/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.master;

import java.io.Serializable;

public class Village implements Serializable {

    private String villageCode = null;
    private String villageName = null;
    
    private String hidvilCode= null;
    private String stateCode = null;
    private String distCode = null;
    private String psCode=null;
    private String psName=null;
    
    
    private String hidstateCode= null;
    private String hiddistCode= null;
    private String hidpsCode= null;

    
    
    
    public String getHidstateCode() {
        return hidstateCode;
    }

    public void setHidstateCode(String hidstateCode) {
        this.hidstateCode = hidstateCode;
    }

    public String getHiddistCode() {
        return hiddistCode;
    }

    public void setHiddistCode(String hiddistCode) {
        this.hiddistCode = hiddistCode;
    }

    public String getHidpsCode() {
        return hidpsCode;
    }

    public void setHidpsCode(String hidpsCode) {
        this.hidpsCode = hidpsCode;
    }
    
    public String getHidvilCode() {
        return hidvilCode;
    }

    public void setHidvilCode(String hidvilCode) {
        this.hidvilCode = hidvilCode;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getDistCode() {
        return distCode;
    }

    public void setDistCode(String distCode) {
        this.distCode = distCode;
    }

    public String getPsCode() {
        return psCode;
    }

    public void setPsCode(String psCode) {
        this.psCode = psCode;
    }

    public String getPsName() {
        return psName;
    }

    public void setPsName(String psName) {
        this.psName = psName;
    }
    
    
    

    public String getVillageCode() {
        return villageCode;
    }

    public void setVillageCode(String villageCode) {
        this.villageCode = villageCode;
    }

    public String getVillageName() {
        return villageName;
    }

    public void setVillageName(String villageName) {
        this.villageName = villageName;
    }

}
