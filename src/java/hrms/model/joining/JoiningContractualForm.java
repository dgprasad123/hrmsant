package hrms.model.joining;

public class JoiningContractualForm {
    
    private String hidempid = null;
    private String hidjoinId = null;
    private String hidPrimaryTransferId;
    private String hidForeignTransferId;
    private String hidPostNomenclature;
    
    private String joiningOrdNo = null;
    private String joiningOrdDt = null;
    
    public String getHidempid() {
        return hidempid;
    }

    public void setHidempid(String hidempid) {
        this.hidempid = hidempid;
    }

    public String getHidjoinId() {
        return hidjoinId;
    }

    public void setHidjoinId(String hidjoinId) {
        this.hidjoinId = hidjoinId;
    }

    public String getJoiningOrdNo() {
        return joiningOrdNo;
    }

    public void setJoiningOrdNo(String joiningOrdNo) {
        this.joiningOrdNo = joiningOrdNo;
    }

    public String getJoiningOrdDt() {
        return joiningOrdDt;
    }

    public void setJoiningOrdDt(String joiningOrdDt) {
        this.joiningOrdDt = joiningOrdDt;
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

    public String getHidPostNomenclature() {
        return hidPostNomenclature;
    }

    public void setHidPostNomenclature(String hidPostNomenclature) {
        this.hidPostNomenclature = hidPostNomenclature;
    }
    
    
}
