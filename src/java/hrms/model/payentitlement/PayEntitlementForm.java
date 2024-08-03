/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.payentitlement;

/**
 *
 * @author Pintu-DELL
 */
public class PayEntitlementForm {
    private String empid;
    
    private int hnotid = 0;
    private int hpayid = 0;
    private String hcadId;
    
    private String chkNotSBPrint;
    
    private String rdTransaction;
    
    private String txtNotOrdNo;
    private String txtNotOrdDt;
    
    private String notifyingPostName;
    
    private String sltCadreDept;
    private String sltCadre;
    private String sltGrade;
    private String sltCadreLevel;
    private String sltDescription;
    private String txtAllotmentYear;
    private String txtCadreId;
    
    private String sltPostingDept;
    private String sltGenericPost;
    
    private String rdPostClassification;
    private String rdPostStatus;
    
    private String txtCadreJoiningWEFDt;
    private String sltCadreJoiningWEFTime;
    
    private String sltPayScale;
    private String txtGP;
    private String txtBasic;
    private String txtSP;
    private String txtPP;
    private String txtOP;
    private String txtDescOP;
    
    private String txtWEFDt;
    private String sltWEFTime;
    
    private String note;
    
    private String hidNotifyingDeptCode;
    private String hidNotifyingDistCode;
    private String hidNotifyingOffCode;
    private String hidNotifyingGPC;
    private String notifyingSpc;

    public String getEmpid() {
        return empid;
    }

    public void setEmpid(String empid) {
        this.empid = empid;
    }

    public int getHnotid() {
        return hnotid;
    }

    public void setHnotid(int hnotid) {
        this.hnotid = hnotid;
    }

    public int getHpayid() {
        return hpayid;
    }

    public void setHpayid(int hpayid) {
        this.hpayid = hpayid;
    }

    public String getHcadId() {
        return hcadId;
    }

    public void setHcadId(String hcadId) {
        this.hcadId = hcadId;
    }

    public String getChkNotSBPrint() {
        return chkNotSBPrint;
    }

    public void setChkNotSBPrint(String chkNotSBPrint) {
        this.chkNotSBPrint = chkNotSBPrint;
    }

    public String getRdTransaction() {
        return rdTransaction;
    }

    public void setRdTransaction(String rdTransaction) {
        this.rdTransaction = rdTransaction;
    }

    public String getTxtNotOrdNo() {
        return txtNotOrdNo;
    }

    public void setTxtNotOrdNo(String txtNotOrdNo) {
        this.txtNotOrdNo = txtNotOrdNo;
    }

    public String getTxtNotOrdDt() {
        return txtNotOrdDt;
    }

    public void setTxtNotOrdDt(String txtNotOrdDt) {
        this.txtNotOrdDt = txtNotOrdDt;
    }

    public String getNotifyingPostName() {
        return notifyingPostName;
    }

    public void setNotifyingPostName(String notifyingPostName) {
        this.notifyingPostName = notifyingPostName;
    }

    public String getSltCadreDept() {
        return sltCadreDept;
    }

    public void setSltCadreDept(String sltCadreDept) {
        this.sltCadreDept = sltCadreDept;
    }

    public String getSltCadre() {
        return sltCadre;
    }

    public void setSltCadre(String sltCadre) {
        this.sltCadre = sltCadre;
    }

    public String getSltGrade() {
        return sltGrade;
    }

    public void setSltGrade(String sltGrade) {
        this.sltGrade = sltGrade;
    }

    public String getSltCadreLevel() {
        return sltCadreLevel;
    }

    public void setSltCadreLevel(String sltCadreLevel) {
        this.sltCadreLevel = sltCadreLevel;
    }

    public String getSltDescription() {
        return sltDescription;
    }

    public void setSltDescription(String sltDescription) {
        this.sltDescription = sltDescription;
    }

    public String getTxtAllotmentYear() {
        return txtAllotmentYear;
    }

    public void setTxtAllotmentYear(String txtAllotmentYear) {
        this.txtAllotmentYear = txtAllotmentYear;
    }

    public String getTxtCadreId() {
        return txtCadreId;
    }

    public void setTxtCadreId(String txtCadreId) {
        this.txtCadreId = txtCadreId;
    }

    public String getSltPostingDept() {
        return sltPostingDept;
    }

    public void setSltPostingDept(String sltPostingDept) {
        this.sltPostingDept = sltPostingDept;
    }

    public String getSltGenericPost() {
        return sltGenericPost;
    }

    public void setSltGenericPost(String sltGenericPost) {
        this.sltGenericPost = sltGenericPost;
    }

    public String getRdPostClassification() {
        return rdPostClassification;
    }

    public void setRdPostClassification(String rdPostClassification) {
        this.rdPostClassification = rdPostClassification;
    }

    public String getRdPostStatus() {
        return rdPostStatus;
    }

    public void setRdPostStatus(String rdPostStatus) {
        this.rdPostStatus = rdPostStatus;
    }

    public String getTxtCadreJoiningWEFDt() {
        return txtCadreJoiningWEFDt;
    }

    public void setTxtCadreJoiningWEFDt(String txtCadreJoiningWEFDt) {
        this.txtCadreJoiningWEFDt = txtCadreJoiningWEFDt;
    }

    public String getSltCadreJoiningWEFTime() {
        return sltCadreJoiningWEFTime;
    }

    public void setSltCadreJoiningWEFTime(String sltCadreJoiningWEFTime) {
        this.sltCadreJoiningWEFTime = sltCadreJoiningWEFTime;
    }

    public String getSltPayScale() {
        return sltPayScale;
    }

    public void setSltPayScale(String sltPayScale) {
        this.sltPayScale = sltPayScale;
    }

    public String getTxtGP() {
        return txtGP;
    }

    public void setTxtGP(String txtGP) {
        this.txtGP = txtGP;
    }

    public String getTxtBasic() {
        return txtBasic;
    }

    public void setTxtBasic(String txtBasic) {
        this.txtBasic = txtBasic;
    }

    public String getTxtSP() {
        return txtSP;
    }

    public void setTxtSP(String txtSP) {
        this.txtSP = txtSP;
    }

    public String getTxtPP() {
        return txtPP;
    }

    public void setTxtPP(String txtPP) {
        this.txtPP = txtPP;
    }

    public String getTxtOP() {
        return txtOP;
    }

    public void setTxtOP(String txtOP) {
        this.txtOP = txtOP;
    }

    public String getTxtDescOP() {
        return txtDescOP;
    }

    public void setTxtDescOP(String txtDescOP) {
        this.txtDescOP = txtDescOP;
    }

    public String getTxtWEFDt() {
        return txtWEFDt;
    }

    public void setTxtWEFDt(String txtWEFDt) {
        this.txtWEFDt = txtWEFDt;
    }

    public String getSltWEFTime() {
        return sltWEFTime;
    }

    public void setSltWEFTime(String sltWEFTime) {
        this.sltWEFTime = sltWEFTime;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getHidNotifyingDeptCode() {
        return hidNotifyingDeptCode;
    }

    public void setHidNotifyingDeptCode(String hidNotifyingDeptCode) {
        this.hidNotifyingDeptCode = hidNotifyingDeptCode;
    }

    public String getHidNotifyingDistCode() {
        return hidNotifyingDistCode;
    }

    public void setHidNotifyingDistCode(String hidNotifyingDistCode) {
        this.hidNotifyingDistCode = hidNotifyingDistCode;
    }

    public String getHidNotifyingOffCode() {
        return hidNotifyingOffCode;
    }

    public void setHidNotifyingOffCode(String hidNotifyingOffCode) {
        this.hidNotifyingOffCode = hidNotifyingOffCode;
    }

    public String getHidNotifyingGPC() {
        return hidNotifyingGPC;
    }

    public void setHidNotifyingGPC(String hidNotifyingGPC) {
        this.hidNotifyingGPC = hidNotifyingGPC;
    }

    public String getNotifyingSpc() {
        return notifyingSpc;
    }

    public void setNotifyingSpc(String notifyingSpc) {
        this.notifyingSpc = notifyingSpc;
    }
    
    
}
