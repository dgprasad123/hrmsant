package hrms.dao.leaveauthority;

import java.util.ArrayList;
import java.util.List;

public interface LeaveAuthorityDAO {
    List leaveAuthorityList(String spc, String loginOffcode, String empId,String processCode,String sltYear);
    ArrayList getDeptList()throws Exception;
    public ArrayList getOfficeList(String deptCode)throws Exception;
    public ArrayList getAuthorityList(String deptCode,String offCode)throws Exception;
    public ArrayList getOtherStaffList(String deptCode,String offCode)throws Exception;
     List otherStaffList(String spc,String empId,String processCode,String sltYear);
    public boolean saveAuthority(String loginEmpId,String spcCode,String chkAuth,String proCode,String deptCode,String offCode)throws Exception;
    public void deleteSancAuth(String authSpc,String applicantSpc);
    
}
