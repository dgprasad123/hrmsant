package hrms.model.leave;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Leave implements Serializable {

    private String leaveId = null;
    private String notType = null;
    private String empId = null;
    //@DateTimeFormat(pattern = "yyyy-MMM-dd")
    private String txtperiodFrom = null;
    private String txtperiodTo = null;
    private String txtsuffixFrom = null;
    private String txtsuffixTo = null;
    private String txtprefixFrom = null;
    private String txtprefixTo = null;
    private String txtconaddress = null;
    private String txtphoneNo = null;
    private String ifmedical;
    private String ifcommuted;
    private String hidSpcCode = null;
    private String chkLongTerm = null;
    private String txtnote = null;
    private String tollid;
    private String sltleaveType = null;
    private String[] attachmentid = null;
    private String submittedTo = null;
    private int taskId;
    private String applicantName = null;
    private String offname = null;
     private String appOffname = null;
    private String hidAuthEmpId = null;
    private String hidSpcAuthCode = null;
    private String hidempId = null;
    private ArrayList fileArrList = null;
    private String txtApplyFor = null;
    private String passString = null;

    private String hidTaskId = null;
    private String sltActionType = null;
    private String txtauthnote = null;
    private ArrayList leaveFlowList = null;
    private String statusId = null;
    private String txtApproveFrom = null;
    private String txtApproveTo = null;
    private String txtOrdDate = null;
    private String txtOrdNo = null;
    private String authPost = null;
    private String hidAuthDeptCode = null;
    private String hidAuthOffCode = null;
    private String hqperrad = null;
    private String extendedFromDate = null;
    private String pendingPostWthName = null;
    private String txtSancAuthority = null;
    private String joiningDate = null;
    private String joiningNote = null;
    private ArrayList joinFileArrList = null;
    private String loginUser = null;
    private String sltChild = null;
    private String notid = null;
    private String cancelNotId = null;
    private String servicedate = null;
    private String syear = null;
    private String applyFor=null;
    private String leavePeriodFrom=null;
    private String leavePeriodTo=null;
    private String fname;
    private String mname;
    private String lname;
    private String intitals;
    private String post;
    private String hidIssuingAuthEmpId;
    private String hidIssuingSpcAuthCode;
    private String daysdiff;
    private String sltYear;
    private String sltMonth;
    private String status;
    private String leaveBalance;
    private String sltPartofday;
    private String approvedPost;
    private String workFlowStatusId = null;
    private String criteria;
    private String rdFullPartPeriod;
    private String initiatedOn;
    private String searchString;
    private String issuingAuthName;
    private String leaveType;
    
    
    
    
    private String txtsearch;
    private String[] radempid;
    private String txtcodehid;
    private String forAddRow;
    private String[] hidEmpName;
    private String processCode=null;
    private ArrayList empdeatails;
    private String myempId;
    private ArrayList deptList=null;
    private String deptCode=null;
    private String loggedInUserSpc=null;
    private String offCode=null;
    private String sltDept=null;
    private String sltOffice=null;
    private ArrayList offList=null;
    private ArrayList authArrList=null;
    private String chkAuth=null;
    private ArrayList myAuthList=null;
    private ArrayList processTypeArrList=null;
   
    /*By Satya*/
    private String authType=null;
    private String rowid=null;

    public String getTxtsearch() {
        return txtsearch;
    }

    public void setTxtsearch(String txtsearch) {
        this.txtsearch = txtsearch;
    }

    public String[] getRadempid() {
        return radempid;
    }

    public void setRadempid(String[] radempid) {
        this.radempid = radempid;
    }

    public String getTxtcodehid() {
        return txtcodehid;
    }

    public void setTxtcodehid(String txtcodehid) {
        this.txtcodehid = txtcodehid;
    }

    public String getForAddRow() {
        return forAddRow;
    }

    public void setForAddRow(String forAddRow) {
        this.forAddRow = forAddRow;
    }

    public String[] getHidEmpName() {
        return hidEmpName;
    }

    public void setHidEmpName(String[] hidEmpName) {
        this.hidEmpName = hidEmpName;
    }

    public String getProcessCode() {
        return processCode;
    }

    public void setProcessCode(String processCode) {
        this.processCode = processCode;
    }

    public ArrayList getEmpdeatails() {
        return empdeatails;
    }

    public void setEmpdeatails(ArrayList empdeatails) {
        this.empdeatails = empdeatails;
    }

    public String getMyempId() {
        return myempId;
    }

    public void setMyempId(String myempId) {
        this.myempId = myempId;
    }

    public ArrayList getDeptList() {
        return deptList;
    }

    public void setDeptList(ArrayList deptList) {
        this.deptList = deptList;
    }

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    public String getLoggedInUserSpc() {
        return loggedInUserSpc;
    }

    public void setLoggedInUserSpc(String loggedInUserSpc) {
        this.loggedInUserSpc = loggedInUserSpc;
    }

    public String getOffCode() {
        return offCode;
    }

    public void setOffCode(String offCode) {
        this.offCode = offCode;
    }

    public String getSltDept() {
        return sltDept;
    }

    public void setSltDept(String sltDept) {
        this.sltDept = sltDept;
    }

    public String getSltOffice() {
        return sltOffice;
    }

    public void setSltOffice(String sltOffice) {
        this.sltOffice = sltOffice;
    }

    public ArrayList getOffList() {
        return offList;
    }

    public void setOffList(ArrayList offList) {
        this.offList = offList;
    }

    public ArrayList getAuthArrList() {
        return authArrList;
    }

    public void setAuthArrList(ArrayList authArrList) {
        this.authArrList = authArrList;
    }

    public String getChkAuth() {
        return chkAuth;
    }

    public void setChkAuth(String chkAuth) {
        this.chkAuth = chkAuth;
    }

    public ArrayList getMyAuthList() {
        return myAuthList;
    }

    public void setMyAuthList(ArrayList myAuthList) {
        this.myAuthList = myAuthList;
    }

    public ArrayList getProcessTypeArrList() {
        return processTypeArrList;
    }

    public void setProcessTypeArrList(ArrayList processTypeArrList) {
        this.processTypeArrList = processTypeArrList;
    }

    public String getAuthType() {
        return authType;
    }

    public void setAuthType(String authType) {
        this.authType = authType;
    }

    public String getRowid() {
        return rowid;
    }

    public void setRowid(String rowid) {
        this.rowid = rowid;
    }

    
    
    
    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }
    
    

    public String getIssuingAuthName() {
        return issuingAuthName;
    }

    public void setIssuingAuthName(String issuingAuthName) {
        this.issuingAuthName = issuingAuthName;
    }

    
    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }
    
    
    public String getCriteria() {
        return criteria;
    }

    public void setCriteria(String criteria) {
        this.criteria = criteria;
    }

    
    
    public String getInitiatedOn() {
        return initiatedOn;
    }

    public void setInitiatedOn(String initiatedOn) {
        this.initiatedOn = initiatedOn;
    }
    

    public String getAppOffname() {
        return appOffname;
    }

    public void setAppOffname(String appOffname) {
        this.appOffname = appOffname;
    }
    
    public String getWorkFlowStatusId() {
        return workFlowStatusId;
    }

    public void setWorkFlowStatusId(String workFlowStatusId) {
        this.workFlowStatusId = workFlowStatusId;
    }

    

    public String getIntitals() {
        return intitals;
    }

    public void setIntitals(String intitals) {
        this.intitals = intitals;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getMname() {
        return mname;
    }

    public void setMname(String mname) {
        this.mname = mname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }
    

    public String getLeavePeriodFrom() {
        return leavePeriodFrom;
    }

    public void setLeavePeriodFrom(String leavePeriodFrom) {
        this.leavePeriodFrom = leavePeriodFrom;
    }

    public String getLeavePeriodTo() {
        return leavePeriodTo;
    }

    public void setLeavePeriodTo(String leavePeriodTo) {
        this.leavePeriodTo = leavePeriodTo;
    }

    public String getApplyFor() {
        return applyFor;
    }

    public void setApplyFor(String applyFor) {
        this.applyFor = applyFor;
    }
    

    public String getSltChild() {
        return sltChild;
    }

    public void setSltChild(String sltChild) {
        this.sltChild = sltChild;
    }

    public String getLoginUser() {
        return loginUser;
    }

    public void setLoginUser(String loginUser) {
        this.loginUser = loginUser;
    }

    public ArrayList getJoinFileArrList() {
        return joinFileArrList;
    }

    public void setJoinFileArrList(ArrayList joinFileArrList) {
        this.joinFileArrList = joinFileArrList;
    }

    public String getJoiningNote() {
        return joiningNote;
    }

    public void setJoiningNote(String joiningNote) {
        this.joiningNote = joiningNote;
    }

    public String getJoiningDate() {
        return joiningDate;
    }

    public void setJoiningDate(String joiningDate) {
        this.joiningDate = joiningDate;
    }

    public String getTxtSancAuthority() {
        return txtSancAuthority;
    }

    public void setTxtSancAuthority(String txtSancAuthority) {
        this.txtSancAuthority = txtSancAuthority;
    }

    public String getPendingPostWthName() {
        return pendingPostWthName;
    }

    public void setPendingPostWthName(String pendingPostWthName) {
        this.pendingPostWthName = pendingPostWthName;
    }

    public String getExtendedFromDate() {
        return extendedFromDate;
    }

    public void setExtendedFromDate(String extendedFromDate) {
        this.extendedFromDate = extendedFromDate;
    }

    public String getHqperrad() {
        return hqperrad;
    }

    public void setHqperrad(String hqperrad) {
        this.hqperrad = hqperrad;
    }

    public String getHidAuthDeptCode() {
        return hidAuthDeptCode;
    }

    public void setHidAuthDeptCode(String hidAuthDeptCode) {
        this.hidAuthDeptCode = hidAuthDeptCode;
    }

    public String getHidAuthOffCode() {
        return hidAuthOffCode;
    }

    public void setHidAuthOffCode(String hidAuthOffCode) {
        this.hidAuthOffCode = hidAuthOffCode;
    }

    public String getAuthPost() {
        return authPost;
    }

    public void setAuthPost(String authPost) {
        this.authPost = authPost;
    }

    public String getTxtOrdDate() {
        return txtOrdDate;
    }

    public void setTxtOrdDate(String txtOrdDate) {
        this.txtOrdDate = txtOrdDate;
    }

    public String getTxtOrdNo() {
        return txtOrdNo;
    }

    public void setTxtOrdNo(String txtOrdNo) {
        this.txtOrdNo = txtOrdNo;
    }

    public String getTxtApproveFrom() {
        return txtApproveFrom;
    }

    public void setTxtApproveFrom(String txtApproveFrom) {
        this.txtApproveFrom = txtApproveFrom;
    }

    public String getTxtApproveTo() {
        return txtApproveTo;
    }

    public void setTxtApproveTo(String txtApproveTo) {
        this.txtApproveTo = txtApproveTo;
    }

    public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }

    public ArrayList getLeaveFlowList() {
        return leaveFlowList;
    }

    public void setLeaveFlowList(ArrayList leaveFlowList) {
        this.leaveFlowList = leaveFlowList;
    }

    public String getHidTaskId() {
        return hidTaskId;
    }

    public void setHidTaskId(String hidTaskId) {
        this.hidTaskId = hidTaskId;
    }

    public String getSltActionType() {
        return sltActionType;
    }

    public void setSltActionType(String sltActionType) {
        this.sltActionType = sltActionType;
    }

    public String getTxtauthnote() {
        return txtauthnote;
    }

    public void setTxtauthnote(String txtauthnote) {
        this.txtauthnote = txtauthnote;
    }

    public String getPassString() {
        return passString;
    }

    public void setPassString(String passString) {
        this.passString = passString;
    }

    public String getTxtApplyFor() {
        return txtApplyFor;
    }

    public void setTxtApplyFor(String txtApplyFor) {
        this.txtApplyFor = txtApplyFor;
    }

    public ArrayList getFileArrList() {
        return fileArrList;
    }

    public void setFileArrList(ArrayList fileArrList) {
        this.fileArrList = fileArrList;
    }

    public String getLeaveId() {
        return leaveId;
    }

    public void setLeaveId(String leaveId) {
        this.leaveId = leaveId;
    }

    public String getNotType() {
        return notType;
    }

    public void setNotType(String notType) {
        this.notType = notType;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getTollid() {
        return tollid;
    }

    public void setTollid(String tollid) {
        this.tollid = tollid;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getHidAuthEmpId() {
        return hidAuthEmpId;
    }

    public void setHidAuthEmpId(String hidAuthEmpId) {
        this.hidAuthEmpId = hidAuthEmpId;
    }

    public String getTxtsuffixFrom() {
        return txtsuffixFrom;
    }

    public void setTxtsuffixFrom(String txtsuffixFrom) {
        this.txtsuffixFrom = txtsuffixFrom;
    }

    public String getTxtsuffixTo() {
        return txtsuffixTo;
    }

    public void setTxtsuffixTo(String txtsuffixTo) {
        this.txtsuffixTo = txtsuffixTo;
    }

    public String getTxtprefixFrom() {
        return txtprefixFrom;
    }

    public void setTxtprefixFrom(String txtprefixFrom) {
        this.txtprefixFrom = txtprefixFrom;
    }

    public String getTxtprefixTo() {
        return txtprefixTo;
    }

    public void setTxtprefixTo(String txtprefixTo) {
        this.txtprefixTo = txtprefixTo;
    }

    public String getHidempId() {
        return hidempId;
    }

    public void setHidempId(String hidempId) {
        this.hidempId = hidempId;
    }

    public String getTxtconaddress() {
        return txtconaddress;
    }

    public void setTxtconaddress(String txtconaddress) {
        this.txtconaddress = txtconaddress;
    }

    public String getTxtphoneNo() {
        return txtphoneNo;
    }

    public void setTxtphoneNo(String txtphoneNo) {
        this.txtphoneNo = txtphoneNo;
    }

    public String getIfmedical() {
        return ifmedical;
    }

    public void setIfmedical(String ifmedical) {
        this.ifmedical = ifmedical;
    }

    public String getIfcommuted() {
        return ifcommuted;
    }

    public void setIfcommuted(String ifcommuted) {
        this.ifcommuted = ifcommuted;
    }

    public String getHidSpcCode() {
        return hidSpcCode;
    }

    public void setHidSpcCode(String hidSpcCode) {
        this.hidSpcCode = hidSpcCode;
    }

    public String getChkLongTerm() {
        return chkLongTerm;
    }

    public void setChkLongTerm(String chkLongTerm) {
        this.chkLongTerm = chkLongTerm;
    }

    public String getSltleaveType() {
        return sltleaveType;
    }

    public void setSltleaveType(String sltleaveType) {
        this.sltleaveType = sltleaveType;
    }

    public String[] getAttachmentid() {
        return attachmentid;
    }

    public void setAttachmentid(String[] attachmentid) {
        this.attachmentid = attachmentid;
    }

    public String getTxtperiodFrom() {
        return txtperiodFrom;
    }

    public void setTxtperiodFrom(String txtperiodFrom) {
        this.txtperiodFrom = txtperiodFrom;
    }

    public String getTxtperiodTo() {
        return txtperiodTo;
    }

    public void setTxtperiodTo(String txtperiodTo) {
        this.txtperiodTo = txtperiodTo;
    }

    public String getHidSpcAuthCode() {
        return hidSpcAuthCode;
    }

    public void setHidSpcAuthCode(String hidSpcAuthCode) {
        this.hidSpcAuthCode = hidSpcAuthCode;
    }

    public String getSubmittedTo() {
        return submittedTo;
    }

    public void setSubmittedTo(String submittedTo) {
        this.submittedTo = submittedTo;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }

    public String getOffname() {
        return offname;
    }

    public void setOffname(String offname) {
        this.offname = offname;
    }

    public String getTxtnote() {
        return txtnote;
    }

    public void setTxtnote(String txtnote) {
        this.txtnote = txtnote;
    }

    public String getNotid() {
        return notid;
    }

    public void setNotid(String notid) {
        this.notid = notid;
    }

    public String getCancelNotId() {
        return cancelNotId;
    }

    public void setCancelNotId(String cancelNotId) {
        this.cancelNotId = cancelNotId;
    }

    public String getServicedate() {
        return servicedate;
    }

    public void setServicedate(String servicedate) {
        this.servicedate = servicedate;
    }

    public String getSyear() {
        return syear;
    }

    public void setSyear(String syear) {
        this.syear = syear;
    }

    public String getHidIssuingAuthEmpId() {
        return hidIssuingAuthEmpId;
    }

    public void setHidIssuingAuthEmpId(String hidIssuingAuthEmpId) {
        this.hidIssuingAuthEmpId = hidIssuingAuthEmpId;
    }

    public String getHidIssuingSpcAuthCode() {
        return hidIssuingSpcAuthCode;
    }

    public void setHidIssuingSpcAuthCode(String hidIssuingSpcAuthCode) {
        this.hidIssuingSpcAuthCode = hidIssuingSpcAuthCode;
    }

    public String getDaysdiff() {
        return daysdiff;
    }

    public void setDaysdiff(String daysdiff) {
        this.daysdiff = daysdiff;
    }

    public String getSltYear() {
        return sltYear;
    }

    public void setSltYear(String sltYear) {
        this.sltYear = sltYear;
    }

    public String getSltMonth() {
        return sltMonth;
    }

    public void setSltMonth(String sltMonth) {
        this.sltMonth = sltMonth;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLeaveBalance() {
        return leaveBalance;
    }

    public void setLeaveBalance(String leaveBalance) {
        this.leaveBalance = leaveBalance;
    }

    public String getSltPartofday() {
        return sltPartofday;
    }

    public void setSltPartofday(String sltPartofday) {
        this.sltPartofday = sltPartofday;
    }

    public String getApprovedPost() {
        return approvedPost;
    }

    public void setApprovedPost(String approvedPost) {
        this.approvedPost = approvedPost;
    }

    public String getRdFullPartPeriod() {
        return rdFullPartPeriod;
    }

    public void setRdFullPartPeriod(String rdFullPartPeriod) {
        this.rdFullPartPeriod = rdFullPartPeriod;
    }

       

}
