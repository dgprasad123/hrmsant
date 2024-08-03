
package hrms.model.master;

public class PoliceStation {
    private String psCode=null;
    private String psName=null;
    
    private String hidPsCode=null;
    private String stateCode = null;
    private String distCode = null;
    private String blockCode=null;

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
    public String getHidPsCode() {
        return hidPsCode;
    }

    public void setHidPsCode(String hidPsCode) {
        this.hidPsCode = hidPsCode;
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

    public String getBlockCode() {
        return blockCode;
    }

    public void setBlockCode(String blockCode) {
        this.blockCode = blockCode;
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
    
}
