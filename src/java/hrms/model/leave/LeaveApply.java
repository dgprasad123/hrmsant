package hrms.model.leave;

import java.io.Serializable;

public class LeaveApply implements Serializable {

    private String hidempId = null;
    private String leaveId = null;
    private String hidSpcCode = null;
    private String hidAuthEmpId = null;
    private String hidSpcAuthCode = null;
    private String txtperiodFrom = null;
    private String txtperiodTo = null;
    private String sltleaveType = null;
    private String txtSancAuthority = null;
    private String sltChild = null;
    private String txtprefixFrom = null;
    private String txtprefixTo = null;
    private String txtconaddress = null;
    private String txtphoneNo = null;
    private String ifmedical;
    private String ifcommuted;
    private String chkLongTerm = null;
    private String txtnote = null;
    private String hqperrad = null;
    private String hidTaskId = null;
    private String txtsuffixFrom = null;
    private String txtsuffixTo = null;
    private String[] attachmentid = null;
    private String statusId = null;
    private String extendedFromDate=null;

    public String getExtendedFromDate() {
        return extendedFromDate;
    }

    public void setExtendedFromDate(String extendedFromDate) {
        this.extendedFromDate = extendedFromDate;
    }
    
    

    public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }

    public String getHidTaskId() {
        return hidTaskId;
    }

    public void setHidTaskId(String hidTaskId) {
        this.hidTaskId = hidTaskId;
    }

    public String getHqperrad() {
        return hqperrad;
    }

    public void setHqperrad(String hqperrad) {
        this.hqperrad = hqperrad;
    }

    public String[] getAttachmentid() {
        return attachmentid;
    }

    public void setAttachmentid(String[] attachmentid) {
        this.attachmentid = attachmentid;
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

    public String getChkLongTerm() {
        return chkLongTerm;
    }

    public void setChkLongTerm(String chkLongTerm) {
        this.chkLongTerm = chkLongTerm;
    }

    public String getTxtnote() {
        return txtnote;
    }

    public void setTxtnote(String txtnote) {
        this.txtnote = txtnote;
    }

    public String getHidempId() {
        return hidempId;
    }

    public void setHidempId(String hidempId) {
        this.hidempId = hidempId;
    }

    public String getLeaveId() {
        return leaveId;
    }

    public void setLeaveId(String leaveId) {
        this.leaveId = leaveId;
    }

    public String getHidSpcCode() {
        return hidSpcCode;
    }

    public void setHidSpcCode(String hidSpcCode) {
        this.hidSpcCode = hidSpcCode;
    }

    public String getHidAuthEmpId() {
        return hidAuthEmpId;
    }

    public void setHidAuthEmpId(String hidAuthEmpId) {
        this.hidAuthEmpId = hidAuthEmpId;
    }

    public String getHidSpcAuthCode() {
        return hidSpcAuthCode;
    }

    public void setHidSpcAuthCode(String hidSpcAuthCode) {
        this.hidSpcAuthCode = hidSpcAuthCode;
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

    public String getSltleaveType() {
        return sltleaveType;
    }

    public void setSltleaveType(String sltleaveType) {
        this.sltleaveType = sltleaveType;
    }

    public String getTxtSancAuthority() {
        return txtSancAuthority;
    }

    public void setTxtSancAuthority(String txtSancAuthority) {
        this.txtSancAuthority = txtSancAuthority;
    }

    public String getSltChild() {
        return sltChild;
    }

    public void setSltChild(String sltChild) {
        this.sltChild = sltChild;
    }

}
