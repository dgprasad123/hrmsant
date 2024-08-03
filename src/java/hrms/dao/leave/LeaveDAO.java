/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.leave;

import hrms.model.leave.CreditLeaveBean;
import hrms.model.leave.CreditLeaveProperties;
import hrms.model.leave.EmpLeaveAccountPropeties;
import hrms.model.leave.Leave;
import hrms.model.leave.LeaveRule;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Surendra
 */
//
public interface LeaveDAO {
    
    public int addLeaveData(Leave leave);
    
    public void updateLeaveData(CreditLeaveBean leave);
    
    public int deleteLeaveData(int leaveId);
    
    public CreditLeaveBean editLeaveData(String leaveId);
    
    public List getAbsenteeList(String empId, String leaveType);
    
    public List getLeaveList(String empId, String leaveType);
    
    public boolean checkHalfPayLeave(String leaveType);
    
    public boolean checkEarnedLeave(String leaveType);
    
    public EmpLeaveAccountPropeties getLeaveAccountDetails(String empCode,String fDate,String tDate,String leaveType,String exOrdLeave);
    
    public ArrayList getAvailedLeaveList(String empId, String leaveType);
    
    public CreditLeaveProperties getLeaveOBDate(String empId,String tolid);
    
    public ArrayList getUpdateData(String empId,String tolid);
    
    public void updateLeaveCreditData(CreditLeaveProperties clp);
    
    public LeaveRule getLeaveRule(String empId,String typeLeave,String inputDate);
    
    public void deletePeriodicLeaveData(String empId,String tolid);
}
