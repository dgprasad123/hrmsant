/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.TransferProposal;

import java.util.ArrayList;

/**
 *
 * @author Manoj PC
 */
public class TransferProposalBean {

    private String proposalType;
    private int letterno;
    private String fileno;
    private String authorityName;
    private String authorityDesg;
    private String letterheader;
    private String letterfooter;
    private int transferProposalId;
    private int transferProposalDetailId;    
    private ArrayList transferProposalDetail;
    private int proposalId;
    private String empId = null;
    private String cadreCode = null;
    private String cadreName = null;
    private String cadreGrade = null;
    private String pay = null;
    private String oldSpc = null;
    private String newSpc = null;
    private String orderNumber = null;
    private String orderDate = null;
    private String empName = null;
    private String isApproved = null;
    private String dateofEntry = null;

    private String hidAuthDeptCode = null;
    private String hidAuthOffCode = null;
    private String authSpc = null;
    private String rdPostedAuthType = null;
    private String hidPostedDeptCode = null;
    private String hidPostedOffCode = null;
    private String postedspc = null;
    private String hidTempDeptCode = null;
    private String hidTempAuthOffCode;
    private String hidTempAuthPost;
    private String authPostName = null;
    private String postedPostName = null;
    private String hidTempPostedOffCode;
    private String hidTempPostedPost;
    private String hidTempPostedFieldOffCode;
    private String departmentName = null;
    private String officeName = null;
    private String newPostName = null;
    private int numEmps = 0;
    private String hasAdditional = null;
    private String transferType = null;
    private String additionalPostName = null;

    public String getProposalType() {
        return proposalType;
    }

    public void setProposalType(String proposalType) {
        this.proposalType = proposalType;
    }

    public int getLetterno() {
        return letterno;
    }

    public void setLetterno(int letterno) {
        this.letterno = letterno;
    }

    public String getFileno() {
        return fileno;
    }

    public void setFileno(String fileno) {
        this.fileno = fileno;
    }

    public String getAuthorityName() {
        return authorityName;
    }

    public void setAuthorityName(String authorityName) {
        this.authorityName = authorityName;
    }

    public String getAuthorityDesg() {
        return authorityDesg;
    }

    public void setAuthorityDesg(String authorityDesg) {
        this.authorityDesg = authorityDesg;
    }

    public int getTransferProposalId() {
        return transferProposalId;
    }

    public void setTransferProposalId(int transferProposalId) {
        this.transferProposalId = transferProposalId;
    }

    public String getLetterheader() {
        return letterheader;
    }

    public void setLetterheader(String letterheader) {
        this.letterheader = letterheader;
    }

    public String getLetterfooter() {
        return letterfooter;
    }

    public void setLetterfooter(String letterfooter) {
        this.letterfooter = letterfooter;
    }

    public int getTransferProposalDetailId() {
        return transferProposalDetailId;
    }

    public void setTransferProposalDetailId(int transferProposalDetailId) {
        this.transferProposalDetailId = transferProposalDetailId;
    }

    

    public ArrayList getTransferProposalDetail() {
        return transferProposalDetail;
    }

    public void setTransferProposalDetail(ArrayList transferProposalDetail) {
        this.transferProposalDetail = transferProposalDetail;
    }

    public int getProposalId() {
        return proposalId;
    }

    public void setProposalId(int proposalId) {
        this.proposalId = proposalId;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getCadreCode() {
        return cadreCode;
    }

    public void setCadreCode(String cadreCode) {
        this.cadreCode = cadreCode;
    }

    public String getCadreName() {
        return cadreName;
    }

    public void setCadreName(String cadreName) {
        this.cadreName = cadreName;
    }

    public String getCadreGrade() {
        return cadreGrade;
    }

    public void setCadreGrade(String cadreGrade) {
        this.cadreGrade = cadreGrade;
    }

    public String getPay() {
        return pay;
    }

    public void setPay(String pay) {
        this.pay = pay;
    }

    public String getOldSpc() {
        return oldSpc;
    }

    public void setOldSpc(String oldSpc) {
        this.oldSpc = oldSpc;
    }

    public String getNewSpc() {
        return newSpc;
    }

    public void setNewSpc(String newSpc) {
        this.newSpc = newSpc;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(String isApproved) {
        this.isApproved = isApproved;
    }

    public String getDateofEntry() {
        return dateofEntry;
    }

    public void setDateofEntry(String dateofEntry) {
        this.dateofEntry = dateofEntry;
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

    public String getAuthSpc() {
        return authSpc;
    }

    public void setAuthSpc(String authSpc) {
        this.authSpc = authSpc;
    }

    public String getRdPostedAuthType() {
        return rdPostedAuthType;
    }

    public void setRdPostedAuthType(String rdPostedAuthType) {
        this.rdPostedAuthType = rdPostedAuthType;
    }

    public String getHidPostedDeptCode() {
        return hidPostedDeptCode;
    }

    public void setHidPostedDeptCode(String hidPostedDeptCode) {
        this.hidPostedDeptCode = hidPostedDeptCode;
    }

    public String getHidPostedOffCode() {
        return hidPostedOffCode;
    }

    public void setHidPostedOffCode(String hidPostedOffCode) {
        this.hidPostedOffCode = hidPostedOffCode;
    }

    public String getPostedspc() {
        return postedspc;
    }

    public void setPostedspc(String postedspc) {
        this.postedspc = postedspc;
    }

    public String getHidTempDeptCode() {
        return hidTempDeptCode;
    }

    public void setHidTempDeptCode(String hidTempDeptCode) {
        this.hidTempDeptCode = hidTempDeptCode;
    }

    public String getHidTempAuthOffCode() {
        return hidTempAuthOffCode;
    }

    public void setHidTempAuthOffCode(String hidTempAuthOffCode) {
        this.hidTempAuthOffCode = hidTempAuthOffCode;
    }

    public String getHidTempAuthPost() {
        return hidTempAuthPost;
    }

    public void setHidTempAuthPost(String hidTempAuthPost) {
        this.hidTempAuthPost = hidTempAuthPost;
    }

    public String getAuthPostName() {
        return authPostName;
    }

    public void setAuthPostName(String authPostName) {
        this.authPostName = authPostName;
    }

    public String getPostedPostName() {
        return postedPostName;
    }

    public void setPostedPostName(String postedPostName) {
        this.postedPostName = postedPostName;
    }

    public String getHidTempPostedOffCode() {
        return hidTempPostedOffCode;
    }

    public void setHidTempPostedOffCode(String hidTempPostedOffCode) {
        this.hidTempPostedOffCode = hidTempPostedOffCode;
    }

    public String getHidTempPostedPost() {
        return hidTempPostedPost;
    }

    public void setHidTempPostedPost(String hidTempPostedPost) {
        this.hidTempPostedPost = hidTempPostedPost;
    }

    public String getHidTempPostedFieldOffCode() {
        return hidTempPostedFieldOffCode;
    }

    public void setHidTempPostedFieldOffCode(String hidTempPostedFieldOffCode) {
        this.hidTempPostedFieldOffCode = hidTempPostedFieldOffCode;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getOfficeName() {
        return officeName;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }

    public int getNumEmps() {
        return numEmps;
    }

    public void setNumEmps(int numEmps) {
        this.numEmps = numEmps;
    }

    public String getNewPostName() {
        return newPostName;
    }

    public void setNewPostName(String newPostName) {
        this.newPostName = newPostName;
    }

    public String getHasAdditional() {
        return hasAdditional;
    }

    public void setHasAdditional(String hasAdditional) {
        this.hasAdditional = hasAdditional;
    }

    public String getTransferType() {
        return transferType;
    }

    public void setTransferType(String transferType) {
        this.transferType = transferType;
    }

    public String getAdditionalPostName() {
        return additionalPostName;
    }

    public void setAdditionalPostName(String additionalPostName) {
        this.additionalPostName = additionalPostName;
    }

}
