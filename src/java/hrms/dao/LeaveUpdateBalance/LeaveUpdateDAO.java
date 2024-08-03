/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.LeaveUpdateBalance;

import hrms.model.updateleave.UpdateLeave;
import java.util.ArrayList;

/**
 *
 * @author lenovo pc
 */
public interface LeaveUpdateDAO {

    public ArrayList getPostWiseEmpList(String postCode, String offCode, String year,String month);

    UpdateLeave getLeaveBalance(String empId, String curYear, String tolId,String curmon);

    public void updateLeaveBalance(UpdateLeave updateLeave);

    public ArrayList getLeaveStatusReport(String offCode, String curYear,String curMon,String curDate);

    public ArrayList getMonthlyWorkingHour(String year,String month,String userId);
    
    public ArrayList getEmpWiseLeaveList(String empId,String year, String month);
}
