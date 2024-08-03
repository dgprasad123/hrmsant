/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.dao.leave;

import hrms.model.leave.LSOrderType;
import hrms.model.leave.LeaveOpeningBalanceBean;
import hrms.model.leave.LeaveType;
import hrms.model.leave.MiscLeaveForm;
import java.util.ArrayList;

/**
 *
 * @author Manoj PC
 */
public interface MiscLeaveDAO {
   
    public LSOrderType getLSOrderType(String LSOrdId);
    
    public LeaveType getLeaveType(String leaveId);
    
    public ArrayList getLeaveList(String EmpID, String ordType);
    
    public void saveMiscLeave(MiscLeaveForm pb, int notid, String orderType);
    
    public MiscLeaveForm getMiscLeaveData(String leaveId);
    
    public void updateMiscLeave(MiscLeaveForm mLeave, int notid, String leaveId);
    
    public void deleteMiscLeave(String leaveId);
    
    public ArrayList getOpeningBalanceList(String empId);
    
    public void addOpeningBalance(LeaveOpeningBalanceBean loBean);
    
    public void updateOpeningBalance(LeaveOpeningBalanceBean loBean);
    
    public void deleteOpeningBalance(String lobId);
}
