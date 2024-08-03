package hrms.model.payfixation;

import java.util.List;

public class PayFixation {

    private int notId;
    private String notType = null;
    private String empid = null;
    private String doe = null;
    private String payid;
    private int payRecordId=0;
 
    private String txtNotOrdNo = null;
    private String txtNotOrdDt = null;
    
    private String notifyingPostName = null;
    private String hidNotifyingDeptCode;
    private String hidNotifyingOffCode = null;
    private String notifyingSpc = null;
    private String rdoPaycomm="";
    private String sltPayScale;
    private String payLevel="";
    private String payCell="";
    private String txtGP;
    private String txtBasic;
    private String txtPP;
    private String txtSP;
    private String txtOP;
    private String txtDescOP;
    private String txtWEFDt;
    private String sltWEFTime;
    private String txtNextIncrementDt;
    
    private String note = null;
    
    private String incrOrdNo1;
    
    private String rdTransaction="";
    
    private List<RetrospectiveIncrement> retroIncrement;
    
    private String sltPayFixationReason;
    
    private String chkNotSBPrint;
    
    private String radnotifyingauthtype;
    private String hidNotifyingOthSpc;
    
    private String cancelnotId;
    private String supersedeid;
    
    private String linkid;
    private String correctionid;
    private String entrytype;
    
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

    public String getTxtPP() {
        return txtPP;
    }

    public void setTxtPP(String txtPP) {
        this.txtPP = txtPP;
    }

    public String getTxtSP() {
        return txtSP;
    }

    public void setTxtSP(String txtSP) {
        this.txtSP = txtSP;
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

    public String getTxtNextIncrementDt() {
        return txtNextIncrementDt;
    }

    public void setTxtNextIncrementDt(String txtNextIncrementDt) {
        this.txtNextIncrementDt = txtNextIncrementDt;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getPayid() {
        return payid;
    }

    public void setPayid(String payid) {
        this.payid = payid;
    }

    

    public String getIncrOrdNo1() {
        return incrOrdNo1;
    }

    public void setIncrOrdNo1(String incrOrdNo1) {
        this.incrOrdNo1 = incrOrdNo1;
    }

    public int getNotId() {
        return notId;
    }

    public void setNotId(int notId) {
        this.notId = notId;
    }

    public int getPayRecordId() {
        return payRecordId;
    }

    public void setPayRecordId(int payRecordId) {
        this.payRecordId = payRecordId;
    }

    public List<RetrospectiveIncrement> getRetroIncrement() {
        return retroIncrement;
    }

    public void setRetroIncrement(List<RetrospectiveIncrement> retroIncrement) {
        this.retroIncrement = retroIncrement;
    }

    public String getRdTransaction() {
        return rdTransaction;
    }

    public void setRdTransaction(String rdTransaction) {
        this.rdTransaction = rdTransaction;
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

    public String getRdoPaycomm() {
        return rdoPaycomm;
    }

    public void setRdoPaycomm(String rdoPaycomm) {
        this.rdoPaycomm = rdoPaycomm;
    }

    public String getSltPayFixationReason() {
        return sltPayFixationReason;
    }

    public void setSltPayFixationReason(String sltPayFixationReason) {
        this.sltPayFixationReason = sltPayFixationReason;
    }

    public String getChkNotSBPrint() {
        return chkNotSBPrint;
    }

    public void setChkNotSBPrint(String chkNotSBPrint) {
        this.chkNotSBPrint = chkNotSBPrint;
    }

    public String getRadnotifyingauthtype() {
        return radnotifyingauthtype;
    }

    public void setRadnotifyingauthtype(String radnotifyingauthtype) {
        this.radnotifyingauthtype = radnotifyingauthtype;
    }

    public String getHidNotifyingOthSpc() {
        return hidNotifyingOthSpc;
    }

    public void setHidNotifyingOthSpc(String hidNotifyingOthSpc) {
        this.hidNotifyingOthSpc = hidNotifyingOthSpc;
    }

    public String getCancelnotId() {
        return cancelnotId;
    }

    public void setCancelnotId(String cancelnotId) {
        this.cancelnotId = cancelnotId;
    }

    public String getSupersedeid() {
        return supersedeid;
    }

    public void setSupersedeid(String supersedeid) {
        this.supersedeid = supersedeid;
    }

    public String getLinkid() {
        return linkid;
    }

    public void setLinkid(String linkid) {
        this.linkid = linkid;
    }

    public String getCorrectionid() {
        return correctionid;
    }

    public void setCorrectionid(String correctionid) {
        this.correctionid = correctionid;
    }

    public String getEntrytype() {
        return entrytype;
    }

    public void setEntrytype(String entrytype) {
        this.entrytype = entrytype;
    }
}
