package hrms.model.incrementsanction;

public class IncrementForm {

    private String empid = null;
    private String txtSanctionOrderNo = null;
    private String txtSanctionOrderDt = null;
    private String txtWEFDt = null;
    private String txtWEFTime = null;
    private String sltPayScale = null;
    private String payLevel="";
    private String payCell="";
    private String txtIncrAmt = null;
    private String txtP_pay = null;
    private String txtOthPay = null;
    private String txtSPay = null;
    private String txtNewBasic = null;
    private String txtDescOth = null;
    private String txtGradePay = null;
    private String incrementLvl = null;
    private String incrementType = null;
    private String txtIncrNote = null;
    private String rdoPaycomm="";
    
    private String deptCode = null;
    private String hidOffCode = null;
    private String hidSpc = null;
    private String sancAuthPostName = null;
    private int hidIncrId;
    private int hnotid;
    private int hidPayId = 0;
    private String isRemuneration=null;
    
    /* Added for Service Book Transaction  */
    

    private String txtfirst_incr="";
    private String txtsecond_incr="";
    private String txtthird_incr="";
    private String txtfourth_incr="";
    
    private String chkNotSBPrint;
    
    /* Added for Service Book Transaction  */
    
    private String rdTransaction;
    
    private String radsancauthtype;
    private String hidSancAuthorityOthSpc;
    
    private String linkid;
    private String cancelnotId;
    private String supersedeid;
    private String contRemStages;
    private String contRemAmount;    
    private int contGp;
    private String contPayScale;
    private String hidremBasic;
    //private String incrType=null;
    
    private String correctionid;
    private String entrytype;
    private String maxIncrDate;
    
    public String getEmpid() {
        return empid;
    }

    public void setEmpid(String empid) {
        this.empid = empid;
    }

    public String getTxtSanctionOrderNo() {
        return txtSanctionOrderNo;
    }

    public void setTxtSanctionOrderNo(String txtSanctionOrderNo) {
        this.txtSanctionOrderNo = txtSanctionOrderNo;
    }

    public String getTxtSanctionOrderDt() {
        return txtSanctionOrderDt;
    }

    public void setTxtSanctionOrderDt(String txtSanctionOrderDt) {
        this.txtSanctionOrderDt = txtSanctionOrderDt;
    }

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

   
    public String getHidOffCode() {
        return hidOffCode;
    }

    public void setHidOffCode(String hidOffCode) {
        this.hidOffCode = hidOffCode;
    }

    public String getHidSpc() {
        return hidSpc;
    }

    public void setHidSpc(String hidSpc) {
        this.hidSpc = hidSpc;
    }

    public String getTxtWEFDt() {
        return txtWEFDt;
    }

    public void setTxtWEFDt(String txtWEFDt) {
        this.txtWEFDt = txtWEFDt;
    }

    public String getTxtWEFTime() {
        return txtWEFTime;
    }

    public void setTxtWEFTime(String txtWEFTime) {
        this.txtWEFTime = txtWEFTime;
    }

    public String getSltPayScale() {
        return sltPayScale;
    }

    public void setSltPayScale(String sltPayScale) {
        this.sltPayScale = sltPayScale;
    }

    public String getTxtIncrAmt() {
        return txtIncrAmt;
    }

    public void setTxtIncrAmt(String txtIncrAmt) {
        this.txtIncrAmt = txtIncrAmt;
    }

    public String getTxtP_pay() {
        return txtP_pay;
    }

    public void setTxtP_pay(String txtP_pay) {
        this.txtP_pay = txtP_pay;
    }

    public String getTxtOthPay() {
        return txtOthPay;
    }

    public void setTxtOthPay(String txtOthPay) {
        this.txtOthPay = txtOthPay;
    }

    public String getTxtSPay() {
        return txtSPay;
    }

    public void setTxtSPay(String txtSPay) {
        this.txtSPay = txtSPay;
    }

    public String getTxtNewBasic() {
        return txtNewBasic;
    }

    public void setTxtNewBasic(String txtNewBasic) {
        this.txtNewBasic = txtNewBasic;
    }

    public String getTxtDescOth() {
        return txtDescOth;
    }

    public void setTxtDescOth(String txtDescOth) {
        this.txtDescOth = txtDescOth;
    }

    public String getTxtGradePay() {
        return txtGradePay;
    }

    public void setTxtGradePay(String txtGradePay) {
        this.txtGradePay = txtGradePay;
    }

    public String getIncrementLvl() {
        return incrementLvl;
    }

    public void setIncrementLvl(String incrementLvl) {
        this.incrementLvl = incrementLvl;
    }

    public String getIncrementType() {
        return incrementType;
    }

    public void setIncrementType(String incrementType) {
        this.incrementType = incrementType;
    }

    public String getTxtIncrNote() {
        return txtIncrNote;
    }

    public void setTxtIncrNote(String txtIncrNote) {
        this.txtIncrNote = txtIncrNote;
    }

    

    
    public String getSancAuthPostName() {
        return sancAuthPostName;
    }

    public void setSancAuthPostName(String sancAuthPostName) {
        this.sancAuthPostName = sancAuthPostName;
    }

    

    
    public String getTxtfirst_incr() {
        return txtfirst_incr;
    }

    public void setTxtfirst_incr(String txtfirst_incr) {
        this.txtfirst_incr = txtfirst_incr;
    }

    public String getTxtsecond_incr() {
        return txtsecond_incr;
    }

    public void setTxtsecond_incr(String txtsecond_incr) {
        this.txtsecond_incr = txtsecond_incr;
    }

    public String getTxtthird_incr() {
        return txtthird_incr;
    }

    public void setTxtthird_incr(String txtthird_incr) {
        this.txtthird_incr = txtthird_incr;
    }

    public String getTxtfourth_incr() {
        return txtfourth_incr;
    }

    public void setTxtfourth_incr(String txtfourth_incr) {
        this.txtfourth_incr = txtfourth_incr;
    }

    public int getHnotid() {
        return hnotid;
    }

    public void setHnotid(int hnotid) {
        this.hnotid = hnotid;
    }

    public int getHidIncrId() {
        return hidIncrId;
    }

    public void setHidIncrId(int hidIncrId) {
        this.hidIncrId = hidIncrId;
    }

    public int getHidPayId() {
        return hidPayId;
    }

    public void setHidPayId(int hidPayId) {
        this.hidPayId = hidPayId;
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

    public String getRadsancauthtype() {
        return radsancauthtype;
    }

    public void setRadsancauthtype(String radsancauthtype) {
        this.radsancauthtype = radsancauthtype;
    }

    public String getHidSancAuthorityOthSpc() {
        return hidSancAuthorityOthSpc;
    }

    public void setHidSancAuthorityOthSpc(String hidSancAuthorityOthSpc) {
        this.hidSancAuthorityOthSpc = hidSancAuthorityOthSpc;
    }

    public String getLinkid() {
        return linkid;
    }

    public void setLinkid(String linkid) {
        this.linkid = linkid;
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

    public String getContRemStages() {
        return contRemStages;
    }

    public void setContRemStages(String contRemStages) {
        this.contRemStages = contRemStages;
    }

    public String getContRemAmount() {
        return contRemAmount;
    }

    public void setContRemAmount(String contRemAmount) {
        this.contRemAmount = contRemAmount;
    }


    public int getContGp() {
        return contGp;
    }

    public void setContGp(int contGp) {
        this.contGp = contGp;
    }

    public String getContPayScale() {
        return contPayScale;
    }

    public void setContPayScale(String contPayScale) {
        this.contPayScale = contPayScale;
    }  

   
    public String getHidremBasic() {
        return hidremBasic;
    }

    public void setHidremBasic(String hidremBasic) {
        this.hidremBasic = hidremBasic;
    }

    public String getIsRemuneration() {
        return isRemuneration;
    }

    public void setIsRemuneration(String isRemuneration) {
        this.isRemuneration = isRemuneration;
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

    public String getMaxIncrDate() {
        return maxIncrDate;
    }

    public void setMaxIncrDate(String maxIncrDate) {
        this.maxIncrDate = maxIncrDate;
    }
    
}
