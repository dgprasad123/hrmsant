/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.model.allowances;

import java.util.Date;

/**
 *
 * @author Madhusmita
 */
public class AllowanceModel {
    private int notId;
    private String adId=null;
    private String notType=null;
    private String empid=null;
    private String doe=null;
    private String toe=null;
    private String ifAssumed=null;
    private String ordno=null;
    private String ordDate=null;
    
    private String notifyingPostName = null;
    private String hidNotifyingDeptCode = null;
    private String hidNotifyingOffCode = null;
    private String notifyingSpc = null;
    private String hidTempNotifyingOffCode;
    private String hidTempNotifyingPost;
    
    
    private String chkRetroPromotion;
    private String sltCadreDept;
    private String sltCadre;
    private String hidTempCadre;
    private String sltGrade;
    private String hidTempGrade;
    private String sltCadreLevel;
    private String sltDescription;
    private String hidTempDescription;
    private String txtAllotmentYear;
    private String txtCadreId;
    
    private String sltPostingDept;
    private String sltGenericPost;
    private String hidTempGenericPost;
    
    private String rdPostClassification;
    private String rdPostStatus;
    
    private String txtCadreJoiningWEFDt;
    private String sltCadreJoiningWEFTime;
    private String chkUpdateCadreStatus;
    private String chkJoinedAsSuch;
    private String chkProformaPromotion;
    
    private String hidPostedDeptCode = null;
    private String hidPostedOffCode = null;
    private String postedspc = null;
    
    private String sltPayScale = null;
    private String txtGP = null;
    private String txtBasic = null;
    private String txtSP = null;
    private String txtPP = null;
    private String txtOP = null;
    private String txtDescOP = null;
    private String txtWEFDt = null;
    private String sltWEFTime;
    private String note = null;
    private String allowanceId=null;
    private String allowanceDesc=null;
    private String allowanceAmt=null;
    private String hidAllowDedDesc=null;
    private String wefdate=null;
    
   
    private String hnotType = null;
    private int hnotid;
    private int hpayid = 0;
    private String hCadId;
    
    
    
    private String hidTempPostedOffCode;
    private String hidTempPostedPost;
    private String hidTempPostedFieldOffCode;
    private String chkNotSBPrint;
    private String radpostingauthtype;
    private String sltAllowanceCode;

    public int getNotId() {
        return notId;
    }

    public void setNotId(int notId) {
        this.notId = notId;
    }

    public String getNotType() {
        return notType;
    }

    public void setNotType(String notType) {
        this.notType = notType;
    }

    public String getEmpid() {
        return empid;
    }

    public void setEmpid(String empid) {
        this.empid = empid;
    }

    public String getDoe() {
        return doe;
    }

    public void setDoe(String doe) {
        this.doe = doe;
    }

    public String getToe() {
        return toe;
    }

    public void setToe(String toe) {
        this.toe = toe;
    }

    public String getIfAssumed() {
        return ifAssumed;
    }

    public void setIfAssumed(String ifAssumed) {
        this.ifAssumed = ifAssumed;
    }

    public String getOrdno() {
        return ordno;
    }

    public void setOrdno(String ordno) {
        this.ordno = ordno;
    }

    public String getOrdDate() {
        return ordDate;
    }

    public void setOrdDate(String ordDate) {
        this.ordDate = ordDate;
    }

    public String getNotifyingPostName() {
        return notifyingPostName;
    }

    public void setNotifyingPostName(String notifyingPostName) {
        this.notifyingPostName = notifyingPostName;
    }

    public String getHidNotifyingDeptCode() {
        return hidNotifyingDeptCode;
    }

    public void setHidNotifyingDeptCode(String hidNotifyingDeptCode) {
        this.hidNotifyingDeptCode = hidNotifyingDeptCode;
    }

    public String getHidNotifyingOffCode() {
        return hidNotifyingOffCode;
    }

    public void setHidNotifyingOffCode(String hidNotifyingOffCode) {
        this.hidNotifyingOffCode = hidNotifyingOffCode;
    }

    public String getNotifyingSpc() {
        return notifyingSpc;
    }

    public void setNotifyingSpc(String notifyingSpc) {
        this.notifyingSpc = notifyingSpc;
    }

    public String getHidTempNotifyingOffCode() {
        return hidTempNotifyingOffCode;
    }

    public void setHidTempNotifyingOffCode(String hidTempNotifyingOffCode) {
        this.hidTempNotifyingOffCode = hidTempNotifyingOffCode;
    }

    public String getHidTempNotifyingPost() {
        return hidTempNotifyingPost;
    }

    public void setHidTempNotifyingPost(String hidTempNotifyingPost) {
        this.hidTempNotifyingPost = hidTempNotifyingPost;
    }

    public String getWefdate() {
        return wefdate;
    }

    public void setWefdate(String wefdate) {
        this.wefdate = wefdate;
    }    

    public String getChkRetroPromotion() {
        return chkRetroPromotion;
    }

    public void setChkRetroPromotion(String chkRetroPromotion) {
        this.chkRetroPromotion = chkRetroPromotion;
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

    public String getHidTempCadre() {
        return hidTempCadre;
    }

    public void setHidTempCadre(String hidTempCadre) {
        this.hidTempCadre = hidTempCadre;
    }

    public String getSltGrade() {
        return sltGrade;
    }

    public void setSltGrade(String sltGrade) {
        this.sltGrade = sltGrade;
    }

    public String getHidTempGrade() {
        return hidTempGrade;
    }

    public void setHidTempGrade(String hidTempGrade) {
        this.hidTempGrade = hidTempGrade;
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

    public String getAllowanceAmt() {
        return allowanceAmt;
    }

    public void setAllowanceAmt(String allowanceAmt) {
        this.allowanceAmt = allowanceAmt;
    }

   

    public String getHidTempDescription() {
        return hidTempDescription;
    }

    public void setHidTempDescription(String hidTempDescription) {
        this.hidTempDescription = hidTempDescription;
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

    public String getHidTempGenericPost() {
        return hidTempGenericPost;
    }

    public void setHidTempGenericPost(String hidTempGenericPost) {
        this.hidTempGenericPost = hidTempGenericPost;
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

    public String getChkUpdateCadreStatus() {
        return chkUpdateCadreStatus;
    }

    public void setChkUpdateCadreStatus(String chkUpdateCadreStatus) {
        this.chkUpdateCadreStatus = chkUpdateCadreStatus;
    }

    public String getChkJoinedAsSuch() {
        return chkJoinedAsSuch;
    }

    public void setChkJoinedAsSuch(String chkJoinedAsSuch) {
        this.chkJoinedAsSuch = chkJoinedAsSuch;
    }

    public String getChkProformaPromotion() {
        return chkProformaPromotion;
    }

    public void setChkProformaPromotion(String chkProformaPromotion) {
        this.chkProformaPromotion = chkProformaPromotion;
    }

    public String getAllowanceId() {
        return allowanceId;
    }

    public void setAllowanceId(String allowanceId) {
        this.allowanceId = allowanceId;
    }

    public String getAllowanceDesc() {
        return allowanceDesc;
    }

    public void setAllowanceDesc(String allowanceDesc) {
        this.allowanceDesc = allowanceDesc;
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

    public String getHnotType() {
        return hnotType;
    }

    public void setHnotType(String hnotType) {
        this.hnotType = hnotType;
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

    public String gethCadId() {
        return hCadId;
    }

    public void sethCadId(String hCadId) {
        this.hCadId = hCadId;
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

    public String getChkNotSBPrint() {
        return chkNotSBPrint;
    }

    public void setChkNotSBPrint(String chkNotSBPrint) {
        this.chkNotSBPrint = chkNotSBPrint;
    }

    public String getRadpostingauthtype() {
        return radpostingauthtype;
    }

    public void setRadpostingauthtype(String radpostingauthtype) {
        this.radpostingauthtype = radpostingauthtype;
    }

    public String getAdId() {
        return adId;
    }

    public void setAdId(String adId) {
        this.adId = adId;
    }

    public String getSltAllowanceCode() {
        return sltAllowanceCode;
    }

    public void setSltAllowanceCode(String sltAllowanceCode) {
        this.sltAllowanceCode = sltAllowanceCode;
    }

    public String getHidAllowDedDesc() {
        return hidAllowDedDesc;
    }

    public void setHidAllowDedDesc(String hidAllowDedDesc) {
        this.hidAllowDedDesc = hidAllowDedDesc;
    }
    
    
    
}
