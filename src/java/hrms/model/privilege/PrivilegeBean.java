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
public class PrivilegeBean {
    
    private String abstractGroupId;
    private String abstractGroupName;
    private String assignedGroupId;

    public String getAbstractGroupId() {
        return abstractGroupId;
    }

    public void setAbstractGroupId(String abstractGroupId) {
        this.abstractGroupId = abstractGroupId;
    }

    public String getAbstractGroupName() {
        return abstractGroupName;
    }

    public void setAbstractGroupName(String abstractGroupName) {
        this.abstractGroupName = abstractGroupName;
    }

    public String getAssignedGroupId() {
        return assignedGroupId;
    }

    public void setAssignedGroupId(String assignedGroupId) {
        this.assignedGroupId = assignedGroupId;
    }
    
}
