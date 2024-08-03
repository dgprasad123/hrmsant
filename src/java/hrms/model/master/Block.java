
package hrms.model.master;
public class Block {
    private String blockCode=null;
    private String blockName=null;
    
    private String hidblockCode=null;
    private String distCode = null;  
    private String stateCode = null;
    
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

    public String getHidblockCode() {
        return hidblockCode;
    }

    public void setHidblockCode(String hidblockCode) {
        this.hidblockCode = hidblockCode;
    }

    public String getDistCode() {
        return distCode;
    }

    public void setDistCode(String distCode) {
        this.distCode = distCode;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }
    
    
    
    public String getBlockCode() {
        return blockCode;
    }

    public void setBlockCode(String blockCode) {
        this.blockCode = blockCode;
    }

    public String getBlockName() {
        return blockName;
    }

    public void setBlockName(String blockName) {
        this.blockName = blockName;
    }
    
}
