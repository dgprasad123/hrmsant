package hrms.model.promotion;

public class PromotionForm {

    private String promotionId = null;

    private String txtNotOrdNo = null;
    private String txtNotOrdDt = null;

    private String notifyingPostName = null;
    private String hidNotifyingDeptCode = null;
    private String hidNotifyingOffCode = null;
    private String notifyingSpc = null;
    private String hidTempNotifyingOffCode;
    private String hidTempNotifyingPost;

    private String sltAllotmentDesc;
    private String chkRetroPromotion;
    private String sltCadreDept;
    private String sltCadre;
    private String hidTempCadre;
    private String sltGrade;
    private String gradeName;
    private String gradeCode;
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
    private String hidPostedDistrict = null;
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
    private String note = null;

    private String empid = null;
    private String hnotType = null;
    private int hnotid;
    private int hpayid = 0;
    private String hCadId;

    private String postedPostName = null;

    private String hidTempPostedOffCode;
    private String hidTempPostedPost;
    private String hidTempPostedFieldOffCode;

    private String hidNotifyingDistCode;
    private String hidNotifyingGPC;

    private String rdTransaction;

    private String hidPostedGPC;

    private String chkNotSBPrint;
    private String sltPostGroup;

    private String rdoPaycomm;
    private String payLevel;
    private String payCell;

    private String radnotifyingauthtype;
    private String hidNotifyingOthSpc;
    private String radpostingauthtype;
    private String hidPostingOthSpc;
    private String hidGradeCode;
    private String entryType;
    private String hidEntryType;
    private String hidStatus;

    private String correctionid;
    private String entrytypeSBCorrection;
    
    private String hnotidSBCorrection;
    private String hpayidSBCorrection;

    public String getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(String promotionId) {
        this.promotionId = promotionId;
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

    public String getSltAllotmentDesc() {
        return sltAllotmentDesc;
    }

    public void setSltAllotmentDesc(String sltAllotmentDesc) {
        this.sltAllotmentDesc = sltAllotmentDesc;
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

    public String getEmpid() {
        return empid;
    }

    public void setEmpid(String empid) {
        this.empid = empid;
    }

    public String getHnotType() {
        return hnotType;
    }

    public void setHnotType(String hnotType) {
        this.hnotType = hnotType;
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

    public String getPostedPostName() {
        return postedPostName;
    }

    public void setPostedPostName(String postedPostName) {
        this.postedPostName = postedPostName;
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

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
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

    public String getHidNotifyingDistCode() {
        return hidNotifyingDistCode;
    }

    public void setHidNotifyingDistCode(String hidNotifyingDistCode) {
        this.hidNotifyingDistCode = hidNotifyingDistCode;
    }

    public String getHidNotifyingGPC() {
        return hidNotifyingGPC;
    }

    public void setHidNotifyingGPC(String hidNotifyingGPC) {
        this.hidNotifyingGPC = hidNotifyingGPC;
    }

    public String getRdTransaction() {
        return rdTransaction;
    }

    public void setRdTransaction(String rdTransaction) {
        this.rdTransaction = rdTransaction;
    }

    public String getHidPostedGPC() {
        return hidPostedGPC;
    }

    public void setHidPostedGPC(String hidPostedGPC) {
        this.hidPostedGPC = hidPostedGPC;
    }

    public String getChkNotSBPrint() {
        return chkNotSBPrint;
    }

    public void setChkNotSBPrint(String chkNotSBPrint) {
        this.chkNotSBPrint = chkNotSBPrint;
    }

    public String getSltPostGroup() {
        return sltPostGroup;
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

    public void setSltPostGroup(String sltPostGroup) {
        this.sltPostGroup = sltPostGroup;
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

    public String getRadpostingauthtype() {
        return radpostingauthtype;
    }

    public void setRadpostingauthtype(String radpostingauthtype) {
        this.radpostingauthtype = radpostingauthtype;
    }

    public String getHidPostingOthSpc() {
        return hidPostingOthSpc;
    }

    public void setHidPostingOthSpc(String hidPostingOthSpc) {
        this.hidPostingOthSpc = hidPostingOthSpc;
    }

    public String getGradeCode() {
        return gradeCode;
    }

    public void setGradeCode(String gradeCode) {
        this.gradeCode = gradeCode;
    }

    public String getHidGradeCode() {
        return hidGradeCode;
    }

    public void setHidGradeCode(String hidGradeCode) {
        this.hidGradeCode = hidGradeCode;
    }

    public String getEntryType() {
        return entryType;
    }

    public void setEntryType(String entryType) {
        this.entryType = entryType;
    }

    public String getHidEntryType() {
        return hidEntryType;
    }

    public void setHidEntryType(String hidEntryType) {
        this.hidEntryType = hidEntryType;
    }

    public String getHidStatus() {
        return hidStatus;
    }

    public void setHidStatus(String hidStatus) {
        this.hidStatus = hidStatus;
    }

    public String getCorrectionid() {
        return correctionid;
    }

    public void setCorrectionid(String correctionid) {
        this.correctionid = correctionid;
    }

    public String getEntrytypeSBCorrection() {
        return entrytypeSBCorrection;
    }

    public void setEntrytypeSBCorrection(String entrytypeSBCorrection) {
        this.entrytypeSBCorrection = entrytypeSBCorrection;
    }

    public String getHnotidSBCorrection() {
        return hnotidSBCorrection;
    }

    public void setHnotidSBCorrection(String hnotidSBCorrection) {
        this.hnotidSBCorrection = hnotidSBCorrection;
    }

    public String getHpayidSBCorrection() {
        return hpayidSBCorrection;
    }

    public void setHpayidSBCorrection(String hpayidSBCorrection) {
        this.hpayidSBCorrection = hpayidSBCorrection;
    }

}
