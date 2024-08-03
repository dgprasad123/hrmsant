/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.AerAuthorization;

import java.util.ArrayList;

/**
 *
 * @author manisha
 */
public class AerAuthorizationBean {

    private int siNo;
    private String acceptor;
    private String acceptorname;
    private String dreviewer;
    private String dreviewername;
    private String dverifier;
    private String dverifiername;
    private String reviewer;
    private String verifier;
    private String reviewername;
    private String verifiername;
    private String operator;
    private String approver;
    private String profileApprover;
    private String action;
    private String financialyear;
    private int processid;
    private String processName;
    private String roletype;
    private String dateofentry;
    private String hrmsid;
    private String empname;
    private String spc;
    private String spn;
    private String authoritytype;
    private String islocked;    
    private String enteredbyhrmsid;
    private String deptCode;
    private String offCode;
    private String postCode;
    private String operatorname;
    private String approvername;
    private String profileApprovername;
    private String enteredByhrmsid;
    
    private String serviceBookValidator;
    private String validatorName;
    private String sbvalidator;

    
    
    private String serviceBookentry;
    private String serviceentryName;
     
    private ArrayList validatorlist;
     private ArrayList serviceBookentrylist;

   
     
    private ArrayList reviewerlist;
    private ArrayList verifierlist;
    
    private ArrayList dreviewerlist;
    private ArrayList dverifierlist;

    public ArrayList getServiceBookentrylist() {
        return serviceBookentrylist;
    }

    public void setServiceBookentrylist(ArrayList serviceBookentrylist) {
        this.serviceBookentrylist = serviceBookentrylist;
    }
    
    public String getSbvalidator() {
        return sbvalidator;
    }

    public void setSbvalidator(String sbvalidator) {
        this.sbvalidator = sbvalidator;
    }
    
    
     public ArrayList getValidatorlist() {
        return validatorlist;
    }

    public void setValidatorlist(ArrayList validatorlist) {
        this.validatorlist = validatorlist;
    }
    public String getAcceptor() {
        return acceptor;
    }

    public void setAcceptor(String acceptor) {
        this.acceptor = acceptor;
    }

    public String getAcceptorname() {
        return acceptorname;
    }

    public void setAcceptorname(String acceptorname) {
        this.acceptorname = acceptorname;
    }

    public String getDreviewer() {
        return dreviewer;
    }

    public void setDreviewer(String dreviewer) {
        this.dreviewer = dreviewer;
    }

    public String getDreviewername() {
        return dreviewername;
    }

    public void setDreviewername(String dreviewername) {
        this.dreviewername = dreviewername;
    }

    public String getDverifier() {
        return dverifier;
    }

    public void setDverifier(String dverifier) {
        this.dverifier = dverifier;
    }

    public String getDverifiername() {
        return dverifiername;
    }

    public void setDverifiername(String dverifiername) {
        this.dverifiername = dverifiername;
    }

    public String getReviewer() {
        return reviewer;
    }

    public void setReviewer(String reviewer) {
        this.reviewer = reviewer;
    }

    public String getVerifier() {
        return verifier;
    }

    public void setVerifier(String verifier) {
        this.verifier = verifier;
    }

    public String getReviewername() {
        return reviewername;
    }

    public void setReviewername(String reviewername) {
        this.reviewername = reviewername;
    }

    public String getVerifiername() {
        return verifiername;
    }

    public void setVerifiername(String verifiername) {
        this.verifiername = verifiername;
    }
        
    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public int getSiNo() {
        return siNo;
    }

    public void setSiNo(int siNo) {
        this.siNo = siNo;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getApprover() {
        return approver;
    }

    public void setApprover(String approver) {
        this.approver = approver;
    }

    public String getFinancialyear() {
        return financialyear;
    }

    public void setFinancialyear(String financialyear) {
        this.financialyear = financialyear;
    }

    public int getProcessid() {
        return processid;
    }

    public void setProcessid(int processid) {
        this.processid = processid;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public String getRoletype() {
        return roletype;
    }

    public void setRoletype(String roletype) {
        this.roletype = roletype;
    }

    public String getDateofentry() {
        return dateofentry;
    }

    public void setDateofentry(String dateofentry) {
        this.dateofentry = dateofentry;
    }

    public String getHrmsid() {
        return hrmsid;
    }

    public void setHrmsid(String hrmsid) {
        this.hrmsid = hrmsid;
    }

    public String getSpc() {
        return spc;
    }

    public void setSpc(String spc) {
        this.spc = spc;
    }

    public String getAuthoritytype() {
        return authoritytype;
    }

    public void setAuthoritytype(String authoritytype) {
        this.authoritytype = authoritytype;
    }

    public String getIslocked() {
        return islocked;
    }

    public void setIslocked(String islocked) {
        this.islocked = islocked;
    }    

    public String getEnteredbyhrmsid() {
        return enteredbyhrmsid;
    }

    public void setEnteredbyhrmsid(String enteredbyhrmsid) {
        this.enteredbyhrmsid = enteredbyhrmsid;
    }

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    public String getOffCode() {
        return offCode;
    }

    public void setOffCode(String offCode) {
        this.offCode = offCode;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getEmpname() {
        return empname;
    }

    public void setEmpname(String empname) {
        this.empname = empname;
    }

    public String getSpn() {
        return spn;
    }

    public void setSpn(String spn) {
        this.spn = spn;
    }

    public String getOperatorname() {
        return operatorname;
    }

    public void setOperatorname(String operatorname) {
        this.operatorname = operatorname;
    }

    public String getApprovername() {
        return approvername;
    }

    public void setApprovername(String approvername) {
        this.approvername = approvername;
    }

    public String getProfileApprover() {
        return profileApprover;
    }

    public void setProfileApprover(String profileApprover) {
        this.profileApprover = profileApprover;
    }

    public String getProfileApprovername() {
        return profileApprovername;
    }

    public void setProfileApprovername(String profileApprovername) {
        this.profileApprovername = profileApprovername;
    }

    public String getEnteredByhrmsid() {
        return enteredByhrmsid;
    }

    public void setEnteredByhrmsid(String enteredByhrmsid) {
        this.enteredByhrmsid = enteredByhrmsid;
    }

    public ArrayList getReviewerlist() {
        return reviewerlist;
    }

    public void setReviewerlist(ArrayList reviewerlist) {
        this.reviewerlist = reviewerlist;
    }

    public ArrayList getVerifierlist() {
        return verifierlist;
    }

    public void setVerifierlist(ArrayList verifierlist) {
        this.verifierlist = verifierlist;
    }

    public ArrayList getDreviewerlist() {
        return dreviewerlist;
    }

    public void setDreviewerlist(ArrayList dreviewerlist) {
        this.dreviewerlist = dreviewerlist;
    }

    public ArrayList getDverifierlist() {
        return dverifierlist;
    }

    public void setDverifierlist(ArrayList dverifierlist) {
        this.dverifierlist = dverifierlist;
    }

    public String getServiceBookValidator() {
        return serviceBookValidator;
    }

    public void setServiceBookValidator(String serviceBookValidator) {
        this.serviceBookValidator = serviceBookValidator;
    }

    public String getValidatorName() {
        return validatorName;
    }

    public void setValidatorName(String validatorName) {
        this.validatorName = validatorName;
    }

    public String getServiceBookentry() {
        return serviceBookentry;
    }

    public void setServiceBookentry(String serviceBookentry) {
        this.serviceBookentry = serviceBookentry;
    }

    public String getServiceentryName() {
        return serviceentryName;
    }

    public void setServiceentryName(String serviceentryName) {
        this.serviceentryName = serviceentryName;
    }
}
