package hrms.model.transfer;

public class TransferBean {
    
    private String transferId = null;
    private String hnotid = null;
    private String notType = null;
    private String doe = null;
    private String ordno = null;
    private String ordt = null;
    private String transferToOffice = null;
    
    private String hrlvid;
    
    private String ifVisible;
    
    private String modifiedBy;
    
    private String isValidated;
    
    private String cancelnotid;
    private String canceltype;
    
    public String getHnotid() {
        return hnotid;
    }

    public void setHnotid(String hnotid) {
        this.hnotid = hnotid;
    }

    public String getDoe() {
        return doe;
    }

    public void setDoe(String doe) {
        this.doe = doe;
    }

    public String getOrdt() {
        return ordt;
    }

    public void setOrdt(String ordt) {
        this.ordt = ordt;
    }

    public String getOrdno() {
        return ordno;
    }

    public void setOrdno(String ordno) {
        this.ordno = ordno;
    }

    public String getNotType() {
        return notType;
    }

    public void setNotType(String notType) {
        this.notType = notType;
    }

    public String getTransferId() {
        return transferId;
    }

    public void setTransferId(String transferId) {
        this.transferId = transferId;
    }

    public String getTransferToOffice() {
        return transferToOffice;
    }

    public void setTransferToOffice(String transferToOffice) {
        this.transferToOffice = transferToOffice;
    }

    public String getHrlvid() {
        return hrlvid;
    }

    public void setHrlvid(String hrlvid) {
        this.hrlvid = hrlvid;
    }

    public String getIfVisible() {
        return ifVisible;
    }

    public void setIfVisible(String ifVisible) {
        this.ifVisible = ifVisible;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getIsValidated() {
        return isValidated;
    }

    public void setIsValidated(String isValidated) {
        this.isValidated = isValidated;
    }

    public String getCanceltype() {
        return canceltype;
    }

    public void setCanceltype(String canceltype) {
        this.canceltype = canceltype;
    }

    public String getCancelnotid() {
        return cancelnotid;
    }

    public void setCancelnotid(String cancelnotid) {
        this.cancelnotid = cancelnotid;
    }
}
