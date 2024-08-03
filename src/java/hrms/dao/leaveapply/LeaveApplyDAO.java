package hrms.dao.leaveapply;

import com.itextpdf.text.Document;
import hrms.model.leave.Leave;

import hrms.model.leave.LeaveBalanceBean;
import hrms.model.leave.LeaveEntrytakenBean;
import hrms.model.leave.LeaveSancBean;
import hrms.model.leave.LeaveWsBean;
import hrms.model.parmast.ParDetail;
import hrms.model.leave.LeaveApply;
import hrms.model.leave.LeaveOswass;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface LeaveApplyDAO {

    public List getLeaveApplyList(String empId);

    public String getLeaveType(String tolId);

    public String getAuthOffice(String authSpc);

    public boolean saveLeave(Leave leaveForm);

    public String getNameWithPost(String authEmpId, String authSpc, String leaveStatus);

    public Leave getLeaveData(String taskId, String loginEmpId, String loggedinSpc);

    public ArrayList getFileName(String taskId, String attFlag);

    public String getAuthorityEmpCode(String taskId);

    public boolean updateTaskList(Leave leaveForm);

    public String getApplicant(String taskId);

    public ArrayList getLeaveWorkFlowDtls(String taskId);

    public String getStatusId(String taskId);

    //  public LeaveBalanceBean getLeaveBalanceInfo(String empId)throws Exception;
    public String getLeaveBalanceInfo(String empCode, String tolId, String year,String strMonth);

    public boolean getIfLeaveRecordExist(String empid, String leaveType, String fromDate, String toDate);

    public boolean ifEmpExist(String empid);

    public boolean ifMaxSurviveChild(String tolId, String empCode);

    public void getLeaveOpeningBalance(String empid, String tolId, String currDate);

    public void updateEmpleave(Leave leaveForm, int notId);

    // public void updateLeaveBalance(String empCode, String tolId, int noOfDays);
    public void updateClLeaveBalance(String empCode, String tolId, double noOfDays, String year,String month);

    public void updateElLeaveBalance(String empCode, String tolId, double noOfDays, String year,String month);

    public void updateHplLeaveBalance(String empCode, String tolId, double noOfDays, String year);

    public void updateCommutedLeaveBalance(String empCode, String tolId, double noOfDays);

    public void updateMaternityLeaveBalance(String empCode, String tolId, double noOfDays);

    public void updatePaternityLeaveBalance(String empCode, String tolId, double noOfDays);

    public void updateLeaveOrder(Leave leaveForm);

    public double calculateDateDiff(String fromdate, String toDate, String empId, String tolid);

    public void updateApproveDate(Leave leave);

    //public boolean getmaxLeaveAvailable(String leaveType,String periodFrom,String periodTo);
    public void viewPDFfunc(Document document, Leave leaveForm, String empid, String gender, String gender1);

    public void viewAllowedPDFfunc(Document document, Leave leaveForm, String empid, String gender, String gender1);

    public void viewBlankOrdnoDatePDFfunc(Document document, Leave leaveForm, String empid, String gender, String gender1);

    public LeaveEntrytakenBean getEntryTaken(String empId);

    public LeaveSancBean getLeaveSancInfo(String taskId);

    public boolean maxPeriodCount(Leave leaveForm);

    public double maxLeavePeriodCount(String tolId);

    public LeaveWsBean[] getEmployeeLeave(String empid, String inputdate);

    public void saveJoinData(Leave leaveForm);

    public List getOffWiseEmpLeaveList(String offCode, String frdate, String todate);

    public ArrayList getAppliedLeaveEmpList(String offcode);

    public ArrayList getAbseneteeStmtList(String offCode, String year, String month);

    public void cancelLeave(String taskid);

    public void updateClLeaveBalanceAfterJoin(String initiatedEmpId, String tolId, double noOfDays, String year);

    public double calculateDateDiffAfterJoin(String joinDate, String toDate, String empId, String tolid);

    public void updateApproveDateAfterJoin(Leave leave);

    public void saveFcmToken(String fcmtoken, String linkid, String serverkey);

    public void deleteFcmToken(String linkid);

    public String getFcmToken(String linkid);

    public void pushFCMNotification(String userDeviceIdKey, String applicantId, String appSpcCode, String statusId);

    public void pushApproveFCMNotification(String userDeviceIdKey, String authId, String authSpcCode, String statusId);

    public LeaveOswass getLeaveDataForOSWAS(String empid, int taskId);

    public String isOSWASUser(String empid);

    String getGender(String empid);

    public void saveCLCancelData(Leave leaveForm);
    
    public void saveEOCancelData(Leave leaveForm);

    public void approveCLCancel(Leave leaveForm, String applicantEmpId);

    public void updateMobileApproveDate(Leave leave);

    public String getAppOffice(String initiatedBy, String appSpc);

    public void updateCLBalance(String offCode, String postCode, String clDays, String year, String month);

    //public HashMap<String,Object> getPostWiseEmpList(String postCode, String offCode);
   

    public ArrayList getSearchLeaveList(String criteria, String searchString);

    public void deleteLeave(String taskId);

    public void saveManageLeave(Leave leaveForm);
}
