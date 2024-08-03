package hrms.model.promotion;

public class PromotionBean {
    private String promotionId = null;
    private String hnotid = null;
    private String notType = null;
    private String doe = null;
    private String ordno = null;
    private String ordt = null;
    
    private String hrlvid;
    
    private String printInServiceBook;
    
    private String modifiedby;
    
    private String isValidated;
    
    public String getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(String promotionId) {
        this.promotionId = promotionId;
    }

    public String getHnotid() {
        return hnotid;
    }

    public void setHnotid(String hnotid) {
        this.hnotid = hnotid;
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

    public String getHrlvid() {
        return hrlvid;
    }

    public void setHrlvid(String hrlvid) {
        this.hrlvid = hrlvid;
    }

    public String getPrintInServiceBook() {
        return printInServiceBook;
    }

    public void setPrintInServiceBook(String printInServiceBook) {
        this.printInServiceBook = printInServiceBook;
    }

    public String getModifiedby() {
        return modifiedby;
    }

    public void setModifiedby(String modifiedby) {
        this.modifiedby = modifiedby;
    }

    public String getIsValidated() {
        return isValidated;
    }

    public void setIsValidated(String isValidated) {
        this.isValidated = isValidated;
    }
}
