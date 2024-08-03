/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.privilege;

import hrms.model.login.Users;
import hrms.model.master.Module;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Surendra
 */
public interface PrivilegeManagementDAO {

    public ArrayList getRoleList();

    public ArrayList getModuleGroupList(String roleId);

    public ArrayList getAssignedPrivilageList(String spc);

    public String assignPrivilege(Module module, String spc,String empid);

    public String revokePrivilege(String spc, int privmapid);
    
    public List getUserType();
    
    public int saveUserDetail(Users users);

    public List getUserList(String userType);

    public List getAssignedPrivilageUserNameSpecificList(String userName);

    public List getModuleListUserNameSpecific(String userType);

    public String assignPrivilegeUserNameSpecific(Module module, String username);
    
    public String assignPrivilegeUserNameSpecific(String[] modulearray, String username);

    public String revokePrivilageUserNameSpecific(String username, int privmapid);
    
    public ArrayList getAllAssignedPrivilegeUserList(String offCode);
    
    public int revokeUserPrivilege(String privspc);
    
    public void revokeUserPrivilege(int privMapId);
    
    public Module[] getUserPrivileageList(String username);
    
    public Module[] getPrivileageList(String moduleType);
    
    public ArrayList getGroupWisePrivilegeList(String spc);
    
    public String assignGroupWisePrivilege(String checkedAbstractModule, String spc,String empId);
    
    public String revokeGroupWisePrivilege(String checkedAbstractModule, String spc);
    
    public ArrayList getGroupWisePrivilegeAssignedList(String spc);

}
