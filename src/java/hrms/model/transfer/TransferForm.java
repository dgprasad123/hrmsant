package hrms.model.transfer;

public class TransferForm {
    
    private String transferId = null;
    private String txtNotOrdNo = null;
    private String txtNotOrdDt = null;
    private String rdAuthType = null;
    private String hidAuthDeptCode = null;
    private String hidAuthOffCode = null;
    private String authSpc = null;
    private String rdPostedAuthType = null;
    private String hidPostedDeptCode = null;
    private String hidPostedDistrict=null;
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
    private int hpayid = 0;
    
    private String authPostName = null;
    private String postedPostName = null;
    
    private String hidTempAuthOffCode;
    private String hidTempAuthPost;
    
    private String hidTempPostedOffCode;
    private String hidTempPostedPost;
    private String hidTempPostedFieldOffCode;
    
    private String hidAuthDistCode;
    private String hidPostedDistCode;
    private String genericpostAuth;
    private String genericpostPosted;
    
    private String rdTransaction;
    private String chkNotSBPrint;
    
    private String rdoPaycomm;
    private String payLevel;
    private String payCell;
    
    private String radsanctioningauthtype;
    private String radpostingauthtype;
    private String hidSanctioningOthSpc;
    private String hidPostedOthSpc;
    
    private String linkid;
    private String supersedeid;
    private String cancelnotId;
     private String payLevelBacklog;
    private String payCellBacklog;
    private String hidStatus;
    
    public String getTransferId() {
        return transferId;
    }

    public void setTransferId(String transferId) {
        this.transferId = transferId;
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

    

    public int getHpayid() {
        return hpayid;
    }

    public void setHpayid(int hpayid) {
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

    public int getHnotid() {
        return hnotid;
    }

    public void setHnotid(int hnotid) {
        this.hnotid = hnotid;
    }

    public String getHidPostedDistrict() {
        return hidPostedDistrict;
    }

    public void setHidPostedDistrict(String hidPostedDistrict) {
        this.hidPostedDistrict = hidPostedDistrict;
    }

    public String getHidAuthDistCode() {
        return hidAuthDistCode;
    }

    public void setHidAuthDistCode(String hidAuthDistCode) {
        this.hidAuthDistCode = hidAuthDistCode;
    }

    public String getRdTransaction() {
        return rdTransaction;
    }

    public void setRdTransaction(String rdTransaction) {
        this.rdTransaction = rdTransaction;
    }

    public String getHidPostedDistCode() {
        return hidPostedDistCode;
    }

    public void setHidPostedDistCode(String hidPostedDistCode) {
        this.hidPostedDistCode = hidPostedDistCode;
    }

    public String getGenericpostAuth() {
        return genericpostAuth;
    }

    public void setGenericpostAuth(String genericpostAuth) {
        this.genericpostAuth = genericpostAuth;
    }

    public String getGenericpostPosted() {
        return genericpostPosted;
    }

    public void setGenericpostPosted(String genericpostPosted) {
        this.genericpostPosted = genericpostPosted;
    }

    public String getChkNotSBPrint() {
        return chkNotSBPrint;
    }

    public String getRdoPaycomm() {
        return rdoPaycomm;
    }

    public void setRdoPaycomm(String rdoPaycomm) {
        this.rdoPaycomm = rdoPaycomm;
    }

    public String getPayLevel() {
        return payLevel;
    }

    public void setPayLevel(String payLevel) {
        this.payLevel = payLevel;
    }

    public String getPayCell() {
        return payCell;
    }

    public void setPayCell(String payCell) {
        this.payCell = payCell;
    }

    public void setChkNotSBPrint(String chkNotSBPrint) {
        this.chkNotSBPrint = chkNotSBPrint;
    }

    public String getRadsanctioningauthtype() {
        return radsanctioningauthtype;
    }

    public void setRadsanctioningauthtype(String radsanctioningauthtype) {
        this.radsanctioningauthtype = radsanctioningauthtype;
    }

    public String getRadpostingauthtype() {
        return radpostingauthtype;
    }

    public void setRadpostingauthtype(String radpostingauthtype) {
        this.radpostingauthtype = radpostingauthtype;
    }

    public String getHidSanctioningOthSpc() {
        return hidSanctioningOthSpc;
    }

    public void setHidSanctioningOthSpc(String hidSanctioningOthSpc) {
        this.hidSanctioningOthSpc = hidSanctioningOthSpc;
    }

    public String getHidPostedOthSpc() {
        return hidPostedOthSpc;
    }

    public void setHidPostedOthSpc(String hidPostedOthSpc) {
        this.hidPostedOthSpc = hidPostedOthSpc;
    }

    public String getLinkid() {
        return linkid;
    }

    public void setLinkid(String linkid) {
        this.linkid = linkid;
    }

    public String getSupersedeid() {
        return supersedeid;
    }

    public void setSupersedeid(String supersedeid) {
        this.supersedeid = supersedeid;
    }

    public String getCancelnotId() {
        return cancelnotId;
    }

    public void setCancelnotId(String cancelnotId) {
        this.cancelnotId = cancelnotId;
    }

    public String getPayLevelBacklog() {
        return payLevelBacklog;
    }

    public void setPayLevelBacklog(String payLevelBacklog) {
        this.payLevelBacklog = payLevelBacklog;
    }

    public String getPayCellBacklog() {
        return payCellBacklog;
    }

    public void setPayCellBacklog(String payCellBacklog) {
        this.payCellBacklog = payCellBacklog;
    }

    public String getHidStatus() {
        return hidStatus;
    }

    public void setHidStatus(String hidStatus) {
        this.hidStatus = hidStatus;
    }
    
    
}
