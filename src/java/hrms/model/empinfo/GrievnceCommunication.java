/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.model.empinfo;

import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Manas
 */
public class GrievnceCommunication {
    public int gid;
    public int gcid;
    private String fromEmployee;
    private String toEmployee;
    private String commTime;
    private String remark;
    private String actiontaken;
    private MultipartFile grivAttachment;
    private String fromEmployeeSPC;
    private String attachementName;
    private String attachementId;
    
    
    

    public String getFromEmployeeSPC() {
        return fromEmployeeSPC;
    }

    public void setFromEmployeeSPC(String fromEmployeeSPC) {
        this.fromEmployeeSPC = fromEmployeeSPC;
    }

    public int getGcid() {
        return gcid;
    }

    public void setGcid(int gcid) {
        this.gcid = gcid;
    }        

    public int getGid() {
        return gid;
    }

    public void setGid(int gid) {
        this.gid = gid;
    }    

    public String getFromEmployee() {
        return fromEmployee;
    }

    public void setFromEmployee(String fromEmployee) {
        this.fromEmployee = fromEmployee;
    }

    public String getToEmployee() {
        return toEmployee;
    }

    public void setToEmployee(String toEmployee) {
        this.toEmployee = toEmployee;
    }

    public String getCommTime() {
        return commTime;
    }

    public void setCommTime(String commTime) {
        this.commTime = commTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getActiontaken() {
        return actiontaken;
    }

    public void setActiontaken(String actiontaken) {
        this.actiontaken = actiontaken;
    }

    public MultipartFile getGrivAttachment() {
        return grivAttachment;
    }

    public void setGrivAttachment(MultipartFile grivAttachment) {
        this.grivAttachment = grivAttachment;
    }

    public String getAttachementName() {
        return attachementName;
    }

    public void setAttachementName(String attachementName) {
        this.attachementName = attachementName;
    }

    public String getAttachementId() {
        return attachementId;
    }

    public void setAttachementId(String attachementId) {
        this.attachementId = attachementId;
    }
    
    
}
