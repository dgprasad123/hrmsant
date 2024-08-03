
package hrms.model.master;

public class PostOffice {
    private String postOfficeCode=null;
    private String postOfficeName=null;
    
    private String pinCode=null;
    
    private String hidPoCode=null;
    private String stateCode = null;
    private String distCode = null;

    private String hidstateCode= null;
    private String hiddistCode= null;

    
    
    
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
    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }
    public String getHidPoCode() {
        return hidPoCode;
    }

    public void setHidPoCode(String hidPoCode) {
        this.hidPoCode = hidPoCode;
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
    public String getPostOfficeCode() {
        return postOfficeCode;
    }

    public void setPostOfficeCode(String postOfficeCode) {
        this.postOfficeCode = postOfficeCode;
    }

    public String getPostOfficeName() {
        return postOfficeName;
    }

    public void setPostOfficeName(String postOfficeName) {
        this.postOfficeName = postOfficeName;
    }
    
}
