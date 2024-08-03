/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.reward;

import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author lenovo
 */
public class Reward {

    private String rewardId = null;
    private String txtNotOrdNo = null;
    private String txtNotOrdDt = null;
    private String rdAuthType = null;
    private String hidAuthDeptCode = null;
    private String hidAuthOffCode = null;
    private String authSpc = null;
    private String rdPostedAuthType = null;
    private String hidPostedDeptCode = null;
    private String hidPostedOffCode = null;
    private String postedspc = null;
    private String sltPostedFieldOff = null;
    private String sltPayScale = null;
    private String txtGP = null;
    private String txtBasic = null;
    private String txtSP = null;
    private String txtPP = null;
    private String txtOP = null;
    private String txtDescOP = null;
    private String txtWEFDt = null;
    private String sltWEFTime;
    private String chkCdrSts = null;
    private String note = null;

    private String empid = null;
    private String hnotType = null;
    private int hnotid;
    private String hpayid = null;

    private String authPostName = null;
    private String postedPostName = null;

    private String hidTempAuthOffCode;
    private String hidTempAuthPost;

    private String hidTempPostedOffCode;
    private String hidTempPostedPost;
    private String hidTempPostedFieldOffCode;
    private String rewardLevel = null;
    private String rewardType = null;
    private String rewardTypeLabel=null;
    private String chkNotSBPrint = "";
    private String moneyReward="";

    private String notType = null;
    private String doe = null;
    private String ordno = null;
    private String ordt = null;
    private String transferToOffice = null;

    private MultipartFile appreciationAttch;
    private String originalFileNameAppreciationAttch;
    private String diskFilenameAppreciationAttch;
    
    private String radpostingauthtype;
    private String hidOthSpc;
    
    public String getRewardType() {
        return rewardType;
    }

    public void setRewardType(String rewardType) {
        this.rewardType = rewardType;
    }

    public String getRewardId() {
        return rewardId;
    }

    public void setRewardId(String rewardId) {
        this.rewardId = rewardId;
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

    public String getRdAuthType() {
        return rdAuthType;
    }

    public void setRdAuthType(String rdAuthType) {
        this.rdAuthType = rdAuthType;
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

    public String getSltPostedFieldOff() {
        return sltPostedFieldOff;
    }

    public void setSltPostedFieldOff(String sltPostedFieldOff) {
        this.sltPostedFieldOff = sltPostedFieldOff;
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

    public String getChkCdrSts() {
        return chkCdrSts;
    }

    public void setChkCdrSts(String chkCdrSts) {
        this.chkCdrSts = chkCdrSts;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getEmpid() {
        return empid;
    }

    public void setEmpid(String empid) {
        this.empid = empid;
    }

    public String getHpayid() {
        return hpayid;
    }

    public void setHpayid(String hpayid) {
        this.hpayid = hpayid;
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

    public String getHnotType() {
        return hnotType;
    }

    public void setHnotType(String hnotType) {
        this.hnotType = hnotType;
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

    public String getSltWEFTime() {
        return sltWEFTime;
    }

    public void setSltWEFTime(String sltWEFTime) {
        this.sltWEFTime = sltWEFTime;
    }

    public String getRewardLevel() {
        return rewardLevel;
    }

    public void setRewardLevel(String rewardLevel) {
        this.rewardLevel = rewardLevel;
    }

    public String getNotType() {
        return notType;
    }

    public void setNotType(String notType) {
        this.notType = notType;
    }

    public String getDoe() {
        return doe;
    }

    public void setDoe(String doe) {
        this.doe = doe;
    }

    public String getOrdno() {
        return ordno;
    }

    public void setOrdno(String ordno) {
        this.ordno = ordno;
    }

    public String getOrdt() {
        return ordt;
    }

    public void setOrdt(String ordt) {
        this.ordt = ordt;
    }

    public String getTransferToOffice() {
        return transferToOffice;
    }

    public void setTransferToOffice(String transferToOffice) {
        this.transferToOffice = transferToOffice;
    }

    public int getHnotid() {
        return hnotid;
    }

    public void setHnotid(int hnotid) {
        this.hnotid = hnotid;
    }

    public String getChkNotSBPrint() {
        return chkNotSBPrint;
    }

    public void setChkNotSBPrint(String chkNotSBPrint) {
        this.chkNotSBPrint = chkNotSBPrint;
    }

    public String getMoneyReward() {
        return moneyReward;
    }

    public void setMoneyReward(String moneyReward) {
        this.moneyReward = moneyReward;
    }

    public String getRewardTypeLabel() {
        return rewardTypeLabel;
    }

    public void setRewardTypeLabel(String rewardTypeLabel) {
        this.rewardTypeLabel = rewardTypeLabel;
    }

    public MultipartFile getAppreciationAttch() {
        return appreciationAttch;
    }

    public void setAppreciationAttch(MultipartFile appreciationAttch) {
        this.appreciationAttch = appreciationAttch;
    }

    public String getOriginalFileNameAppreciationAttch() {
        return originalFileNameAppreciationAttch;
    }

    public void setOriginalFileNameAppreciationAttch(String originalFileNameAppreciationAttch) {
        this.originalFileNameAppreciationAttch = originalFileNameAppreciationAttch;
    }

    public String getDiskFilenameAppreciationAttch() {
        return diskFilenameAppreciationAttch;
    }

    public void setDiskFilenameAppreciationAttch(String diskFilenameAppreciationAttch) {
        this.diskFilenameAppreciationAttch = diskFilenameAppreciationAttch;
    }

    public String getHidOthSpc() {
        return hidOthSpc;
    }

    public String getRadpostingauthtype() {
        return radpostingauthtype;
    }

    public void setRadpostingauthtype(String radpostingauthtype) {
        this.radpostingauthtype = radpostingauthtype;
    }

    public void setHidOthSpc(String hidOthSpc) {
        this.hidOthSpc = hidOthSpc;
    }
}
