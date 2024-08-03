/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.discProceeding;

import java.util.List;

public class DpViewBean {

    private int daId;
    private int taskId;
    private int daioId;
    private String deptName;
    private String rule15OrderNo;
    private String rule15OrderDate;
    private String initAuthHrmsId;
    private String initAuthName;
    private String initAuthCurDegn;
    private String initAuthCurSpc;

    private String approveAuthHrmsId;
    private String approveAuthCurSpc;
    private String approveAuthName;
    private String approveAuthCurDegn;

    private String doEmpHrmsId;
    private String doEmpName;
    private String doEmpCurDegn;
    private String irrgularDetails;

    private List delinquent;
    private List chargeListOnly;
    private List chargeList;
    private List charge;
    private List chargeDtlsList;
    private List documentList;
    private List witnessList;
    private String gender;
    private String gender1;
    private String gender2;
    private String pendingAt;
    private String officeName;
    private String isapprovedbyauthority;
    private String notice1ondate;
    private String notice2ondate;
    private List ios;
    private List penalty;
     private String articleOfCharge;
    private String statementOfImputation;
    private String memoOfEvidence;
    private String memoNo;
    private String memoDate;

    public int getDaId() {
        return daId;
    }

    public void setDaId(int daId) {
        this.daId = daId;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public int getDaioId() {
        return daioId;
    }

    public void setDaioId(int daioId) {
        this.daioId = daioId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getRule15OrderNo() {
        return rule15OrderNo;
    }

    public void setRule15OrderNo(String rule15OrderNo) {
        this.rule15OrderNo = rule15OrderNo;
    }

    public String getRule15OrderDate() {
        return rule15OrderDate;
    }

    public void setRule15OrderDate(String rule15OrderDate) {
        this.rule15OrderDate = rule15OrderDate;
    }

    public String getInitAuthHrmsId() {
        return initAuthHrmsId;
    }

    public void setInitAuthHrmsId(String initAuthHrmsId) {
        this.initAuthHrmsId = initAuthHrmsId;
    }

    public String getInitAuthName() {
        return initAuthName;
    }

    public void setInitAuthName(String initAuthName) {
        this.initAuthName = initAuthName;
    }

    public String getInitAuthCurDegn() {
        return initAuthCurDegn;
    }

    public void setInitAuthCurDegn(String initAuthCurDegn) {
        this.initAuthCurDegn = initAuthCurDegn;
    }

    public String getInitAuthCurSpc() {
        return initAuthCurSpc;
    }

    public void setInitAuthCurSpc(String initAuthCurSpc) {
        this.initAuthCurSpc = initAuthCurSpc;
    }

    public String getApproveAuthHrmsId() {
        return approveAuthHrmsId;
    }

    public void setApproveAuthHrmsId(String approveAuthHrmsId) {
        this.approveAuthHrmsId = approveAuthHrmsId;
    }

    public String getApproveAuthName() {
        return approveAuthName;
    }

    public void setApproveAuthName(String approveAuthName) {
        this.approveAuthName = approveAuthName;
    }

    public String getApproveAuthCurSpc() {
        return approveAuthCurSpc;
    }

    public void setApproveAuthCurSpc(String approveAuthCurSpc) {
        this.approveAuthCurSpc = approveAuthCurSpc;
    }

    public String getApproveAuthCurDegn() {
        return approveAuthCurDegn;
    }

    public void setApproveAuthCurDegn(String approveAuthCurDegn) {
        this.approveAuthCurDegn = approveAuthCurDegn;
    }

    public String getDoEmpName() {
        return doEmpName;
    }

    public void setDoEmpName(String doEmpName) {
        this.doEmpName = doEmpName;
    }

    public String getDoEmpCurDegn() {
        return doEmpCurDegn;
    }

    public void setDoEmpCurDegn(String doEmpCurDegn) {
        this.doEmpCurDegn = doEmpCurDegn;
    }

    public String getDoEmpHrmsId() {
        return doEmpHrmsId;
    }

    public void setDoEmpHrmsId(String doEmpHrmsId) {
        this.doEmpHrmsId = doEmpHrmsId;
    }

    public List getChargeList() {
        return chargeList;
    }

    public void setChargeList(List chargeList) {
        this.chargeList = chargeList;
    }

    public List getWitnessList() {
        return witnessList;
    }

    public void setWitnessList(List witnessList) {
        this.witnessList = witnessList;
    }

    public List getDelinquent() {
        return delinquent;
    }

    public void setDelinquent(List delinquent) {
        this.delinquent = delinquent;
    }

    public String getIrrgularDetails() {
        return irrgularDetails;
    }

    public void setIrrgularDetails(String irrgularDetails) {
        this.irrgularDetails = irrgularDetails;
    }

    public List getCharge() {
        return charge;
    }

    public void setCharge(List charge) {
        this.charge = charge;
    }

    public List getChargeDtlsList() {
        return chargeDtlsList;
    }

    public void setChargeDtlsList(List chargeDtlsList) {
        this.chargeDtlsList = chargeDtlsList;
    }

    public List getDocumentList() {
        return documentList;
    }

    public void setDocumentList(List documentList) {
        this.documentList = documentList;
    }

    public List getChargeListOnly() {
        return chargeListOnly;
    }

    public void setChargeListOnly(List chargeListOnly) {
        this.chargeListOnly = chargeListOnly;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGender1() {
        return gender1;
    }

    public void setGender1(String gender1) {
        this.gender1 = gender1;
    }

    public String getGender2() {
        return gender2;
    }

    public void setGender2(String gender2) {
        this.gender2 = gender2;
    }

    public String getPendingAt() {
        return pendingAt;
    }

    public void setPendingAt(String pendingAt) {
        this.pendingAt = pendingAt;
    }

    public String getOfficeName() {
        return officeName;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }

    public String getIsapprovedbyauthority() {
        return isapprovedbyauthority;
    }

    public void setIsapprovedbyauthority(String isapprovedbyauthority) {
        this.isapprovedbyauthority = isapprovedbyauthority;
    }

    public String getNotice1ondate() {
        return notice1ondate;
    }

    public void setNotice1ondate(String notice1ondate) {
        this.notice1ondate = notice1ondate;
    }    

    public String getNotice2ondate() {
        return notice2ondate;
    }

    public void setNotice2ondate(String notice2ondate) {
        this.notice2ondate = notice2ondate;
    }

    public List getIos() {
        return ios;
    }

    public void setIos(List ios) {
        this.ios = ios;
    }

    public List getPenalty() {
        return penalty;
    }

    public void setPenalty(List penalty) {
        this.penalty = penalty;
    }

    public String getArticleOfCharge() {
        return articleOfCharge;
    }

    public void setArticleOfCharge(String articleOfCharge) {
        this.articleOfCharge = articleOfCharge;
    }

    public String getStatementOfImputation() {
        return statementOfImputation;
    }

    public void setStatementOfImputation(String statementOfImputation) {
        this.statementOfImputation = statementOfImputation;
    }

    public String getMemoOfEvidence() {
        return memoOfEvidence;
    }

    public void setMemoOfEvidence(String memoOfEvidence) {
        this.memoOfEvidence = memoOfEvidence;
    }

    public String getMemoNo() {
        return memoNo;
    }

    public void setMemoNo(String memoNo) {
        this.memoNo = memoNo;
    }

    public String getMemoDate() {
        return memoDate;
    }

    public void setMemoDate(String memoDate) {
        this.memoDate = memoDate;
    }

}
