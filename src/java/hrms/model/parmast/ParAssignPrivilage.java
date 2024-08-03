/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.parmast;

/**
 *
 * @author Manas
 */
public class ParAssignPrivilage {
    /*Having this privilage the person only view the status of the par not detail of the par*/
    public static final String onlyParStatusView = "S";
    /*Having this privilage the person can view detail of the par*/
    public static final String onlyParView = "V";
    private String spc;
    private String cadreCode;
    private String postGrp;
    private String offCode;
    private String offName;
    private String distCode;
    private String distName;
    private String authorizationType;

    public String getSpc() {
        return spc;
    }

    public void setSpc(String spc) {
        this.spc = spc;
    }

    public String getCadreCode() {
        return cadreCode;
    }

    public void setCadreCode(String cadreCode) {
        this.cadreCode = cadreCode;
    }

    public String getPostGrp() {
        return postGrp;
    }

    public String getOffName() {
        return offName;
    }

    public void setOffName(String offName) {
        this.offName = offName;
    }

    public String getDistName() {
        return distName;
    }

    public void setDistName(String distName) {
        this.distName = distName;
    }

    public void setPostGrp(String postGrp) {
        this.postGrp = postGrp;
    }

    public String getOffCode() {
        return offCode;
    }

    public void setOffCode(String offCode) {
        this.offCode = offCode;
    }

    public String getDistCode() {
        return distCode;
    }

    public void setDistCode(String distCode) {
        this.distCode = distCode;
    }

    public String getAuthorizationType() {
        return authorizationType;
    }

    public void setAuthorizationType(String authorizationType) {
        this.authorizationType = authorizationType;
    }    
    
}
