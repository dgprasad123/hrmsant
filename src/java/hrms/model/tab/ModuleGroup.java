package hrms.model.tab;

import java.util.ArrayList;

public class ModuleGroup {

    private String modGrpId=null;
    private String modGrpName=null;
    private String assigned=null;
    private ArrayList moduleListArr=null;
    private String moduleOfc=null;

    public void setModGrpId(String modGrpId) {
        this.modGrpId = modGrpId;
    }

    public String getModGrpId() {
        return modGrpId;
    }

    public void setModGrpName(String modGrpName) {
        this.modGrpName = modGrpName;
    }

    public String getModGrpName() {
        return modGrpName;
    }

    public void setModuleListArr(ArrayList moduleListArr) {
        this.moduleListArr = moduleListArr;
    }

    public ArrayList getModuleListArr() {
        return moduleListArr;
    }

    public void setAssigned(String assigned) {
        this.assigned = assigned;
    }

    public String getAssigned() {
        return assigned;
    }

    public String getModuleOfc() {
        return moduleOfc;
    }

    public void setModuleOfc(String moduleOfc) {
        this.moduleOfc = moduleOfc;
    }
    
}
