/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.privilege;

/**
 *
 * @author Surendra
 */
public class RoleMapping {
    
    private String modGroup=null;
    private String chkPrivilage=null;
    private int slNo=0;

    private String modGrpName=null;
    private String modId=null;
    private String modGrpMap=null;
    
    public void setModGroup(String modGroup) {
        this.modGroup = modGroup;
    }

    public String getModGroup() {
        return modGroup;
    }

    public void setChkPrivilage(String chkPrivilage) {
        this.chkPrivilage = chkPrivilage;
    }

    public String getChkPrivilage() {
        return chkPrivilage;
    }

    public void setSlNo(int slNo) {
        this.slNo = slNo;
    }

    public int getSlNo() {
        return slNo;
    }

    public void setModGrpName(String modGrpName) {
        this.modGrpName = modGrpName;
    }

    public String getModGrpName() {
        return modGrpName;
    }

    public void setModId(String modId) {
        this.modId = modId;
    }

    public String getModId() {
        return modId;
    }

    public void setModGrpMap(String modGrpMap) {
        this.modGrpMap = modGrpMap;
    }

    public String getModGrpMap() {
        return modGrpMap;
    }

}
