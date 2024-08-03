package hrms.model.joining;

public class JoiningContractualList {
    
    private String joinid = null;
    private String hidPrimaryTransferId;
    private String hidForeignTransferId;
    
    private String notOrdNo = null;
    private String notOrdDt = null;
    
    private String transferFromPost;

    public String getJoinid() {
        return joinid;
    }

    public void setJoinid(String joinid) {
        this.joinid = joinid;
    }

    public String getHidPrimaryTransferId() {
        return hidPrimaryTransferId;
    }

    public void setHidPrimaryTransferId(String hidPrimaryTransferId) {
        this.hidPrimaryTransferId = hidPrimaryTransferId;
    }

    public String getHidForeignTransferId() {
        return hidForeignTransferId;
    }

    public void setHidForeignTransferId(String hidForeignTransferId) {
        this.hidForeignTransferId = hidForeignTransferId;
    }

    public String getNotOrdNo() {
        return notOrdNo;
    }

    public void setNotOrdNo(String notOrdNo) {
        this.notOrdNo = notOrdNo;
    }

    public String getNotOrdDt() {
        return notOrdDt;
    }

    public void setNotOrdDt(String notOrdDt) {
        this.notOrdDt = notOrdDt;
    }

    public String getTransferFromPost() {
        return transferFromPost;
    }

    public void setTransferFromPost(String transferFromPost) {
        this.transferFromPost = transferFromPost;
    }
    
}
