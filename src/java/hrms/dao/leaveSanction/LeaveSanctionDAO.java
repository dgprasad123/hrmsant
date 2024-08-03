package hrms.dao.leaveSanction;

import hrms.model.leaveSanction.LeaveSanctionForm;
import hrms.model.notification.NotificationBean;
import java.util.List;

public interface LeaveSanctionDAO {
    
    public List getLeaveSanctionList(String empid,String ordType);
    
    public List getLeaveTypeList(String ordType);
    
    public void saveLeaveSanction(LeaveSanctionForm lform, int notId);
    
    public void updateLeaveSanction(LeaveSanctionForm lform);
    
    public LeaveSanctionForm editLeaveSanction(String empid, int notid);
    
    public boolean deleteLeaveSanction(String empId, String leaveId);
    
    public String getLeaveTypeName(String tolid);
    
    public void saveCancelLeaveSanction(NotificationBean nb, int notId, String othspc);
    
    public void updateCancellationNotificationData(int notid, int linkidnotid);
    
    public LeaveSanctionForm editCancelLeaveSanction(String empid, int notid);
    
    public void updateSupersedeNotificationData(int notid, int linkidnotid);
    
    public void saveSupersedeLeaveSanction(LeaveSanctionForm lform, int notId);
    
    public void updateSupersedeLeaveSanction(LeaveSanctionForm lform);
    
    public LeaveSanctionForm editSupersedeLeaveSanction(String empid, int notid);
    
}
