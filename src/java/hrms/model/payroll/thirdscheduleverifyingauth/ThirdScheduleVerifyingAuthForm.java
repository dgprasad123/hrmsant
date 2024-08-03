package hrms.model.payroll.thirdscheduleverifyingauth;

import java.util.ArrayList;

public class ThirdScheduleVerifyingAuthForm {
    
    private String empid = null;
    private String empname = null;
    private String post = null;
    private String deptCode=null;
    private String hooSpc="";
    private String hooPostName=null;
    private String officiating=null;
    private String curBasic;
    private int curBasicGp = 0;
    private String curPayScale = null;
    private String curGp;
    
    private String curPost;
    private String curPostPaymatrixLevel;
    private String entryPost = null;
    private String entryGpc = null;
    private String entryPaymatrixLevel;
    
    private String revisionPayScale;
    private String revisionGP;
    
    private String nosRacpAvailed;
    private String nosPromotionAvailed;
    private String nosRacpBeforePromotion;
    private String nosRacpAfterPromotion;
    private String basicpayFixPaymatrix;
    private String dateOptionExercised = null;
    private String basic;
    private String gp;
    private String da;
    private String totalpay;
    private String payrev257Basicpay;
    private String payrevFittedAmount;
    private String payrevFittedLevel;
    
    private String payrevPaycell;
    private String doeIncr = null;
    private String otherInfo = null;
    
    private ArrayList incrementList = null;
    
    private String[] incrDt = null;
    private String[] incrCell;
    private String[] revisedbasic;
    private String[] incrLevel = null;
    private String[] pp = null;
    
    private String isApproved = null;
    
    private String revisionCurBasic;
    
    private String previousPayScale;
    private String previousGp;
    
    private String entryDeptCode;
    
    private String isIASCadre;
    
    private String juniorName;
    private String paySubstantive;
    private String payPersonal;
    private String npAllowance;
    
    private String deptCodeIAS;
    
    private String isUGCCadre;
    
    public String getEmpid() {
        return empid;
    }

    public void setEmpid(String empid) {
        this.empid = empid;
    }

    public String getEmpname() {
        return empname;
    }

    public void setEmpname(String empname) {
        this.empname = empname;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    public String getHooSpc() {
        return hooSpc;
    }

    public void setHooSpc(String hooSpc) {
        this.hooSpc = hooSpc;
    }

    public String getHooPostName() {
        return hooPostName;
    }

    public void setHooPostName(String hooPostName) {
        this.hooPostName = hooPostName;
    }

    public String getOfficiating() {
        return officiating;
    }

    public void setOfficiating(String officiating) {
        this.officiating = officiating;
    }

    public String getCurBasic() {
        return curBasic;
    }

    public void setCurBasic(String curBasic) {
        this.curBasic = curBasic;
    }

    public int getCurBasicGp() {
        return curBasicGp;
    }

    public void setCurBasicGp(int curBasicGp) {
        this.curBasicGp = curBasicGp;
    }

    public String getCurPayScale() {
        return curPayScale;
    }

    public void setCurPayScale(String curPayScale) {
        this.curPayScale = curPayScale;
    }

    public String getCurGp() {
        return curGp;
    }

    public void setCurGp(String curGp) {
        this.curGp = curGp;
    }

    public String getCurPost() {
        return curPost;
    }

    public void setCurPost(String curPost) {
        this.curPost = curPost;
    }

    public String getCurPostPaymatrixLevel() {
        return curPostPaymatrixLevel;
    }

    public void setCurPostPaymatrixLevel(String curPostPaymatrixLevel) {
        this.curPostPaymatrixLevel = curPostPaymatrixLevel;
    }

    public String getEntryGpc() {
        return entryGpc;
    }

    public void setEntryGpc(String entryGpc) {
        this.entryGpc = entryGpc;
    }

    public String getEntryPaymatrixLevel() {
        return entryPaymatrixLevel;
    }

    public void setEntryPaymatrixLevel(String entryPaymatrixLevel) {
        this.entryPaymatrixLevel = entryPaymatrixLevel;
    }

    public String getRevisionPayScale() {
        return revisionPayScale;
    }

    public void setRevisionPayScale(String revisionPayScale) {
        this.revisionPayScale = revisionPayScale;
    }

    public String getRevisionGP() {
        return revisionGP;
    }

    public void setRevisionGP(String revisionGP) {
        this.revisionGP = revisionGP;
    }

    public String getNosRacpAvailed() {
        return nosRacpAvailed;
    }

    public void setNosRacpAvailed(String nosRacpAvailed) {
        this.nosRacpAvailed = nosRacpAvailed;
    }

    public String getNosPromotionAvailed() {
        return nosPromotionAvailed;
    }

    public void setNosPromotionAvailed(String nosPromotionAvailed) {
        this.nosPromotionAvailed = nosPromotionAvailed;
    }

    public String getNosRacpBeforePromotion() {
        return nosRacpBeforePromotion;
    }

    public void setNosRacpBeforePromotion(String nosRacpBeforePromotion) {
        this.nosRacpBeforePromotion = nosRacpBeforePromotion;
    }

    public String getNosRacpAfterPromotion() {
        return nosRacpAfterPromotion;
    }

    public void setNosRacpAfterPromotion(String nosRacpAfterPromotion) {
        this.nosRacpAfterPromotion = nosRacpAfterPromotion;
    }

    public String getBasicpayFixPaymatrix() {
        return basicpayFixPaymatrix;
    }

    public void setBasicpayFixPaymatrix(String basicpayFixPaymatrix) {
        this.basicpayFixPaymatrix = basicpayFixPaymatrix;
    }

    public String getDateOptionExercised() {
        return dateOptionExercised;
    }

    public void setDateOptionExercised(String dateOptionExercised) {
        this.dateOptionExercised = dateOptionExercised;
    }

    public String getBasic() {
        return basic;
    }

    public void setBasic(String basic) {
        this.basic = basic;
    }

    public String getGp() {
        return gp;
    }

    public void setGp(String gp) {
        this.gp = gp;
    }

    public String getDa() {
        return da;
    }

    public void setDa(String da) {
        this.da = da;
    }

    public String getTotalpay() {
        return totalpay;
    }

    public void setTotalpay(String totalpay) {
        this.totalpay = totalpay;
    }

    public String getPayrev257Basicpay() {
        return payrev257Basicpay;
    }

    public void setPayrev257Basicpay(String payrev257Basicpay) {
        this.payrev257Basicpay = payrev257Basicpay;
    }

    public String getPayrevFittedAmount() {
        return payrevFittedAmount;
    }

    public void setPayrevFittedAmount(String payrevFittedAmount) {
        this.payrevFittedAmount = payrevFittedAmount;
    }

    public String getPayrevFittedLevel() {
        return payrevFittedLevel;
    }

    public void setPayrevFittedLevel(String payrevFittedLevel) {
        this.payrevFittedLevel = payrevFittedLevel;
    }

    public String getPayrevPaycell() {
        return payrevPaycell;
    }

    public void setPayrevPaycell(String payrevPaycell) {
        this.payrevPaycell = payrevPaycell;
    }

    public String getDoeIncr() {
        return doeIncr;
    }

    public void setDoeIncr(String doeIncr) {
        this.doeIncr = doeIncr;
    }

    public String getOtherInfo() {
        return otherInfo;
    }

    public void setOtherInfo(String otherInfo) {
        this.otherInfo = otherInfo;
    }

    public ArrayList getIncrementList() {
        return incrementList;
    }

    public void setIncrementList(ArrayList incrementList) {
        this.incrementList = incrementList;
    }

    public String[] getIncrDt() {
        return incrDt;
    }

    public void setIncrDt(String[] incrDt) {
        this.incrDt = incrDt;
    }

    public String[] getIncrCell() {
        return incrCell;
    }

    public void setIncrCell(String[] incrCell) {
        this.incrCell = incrCell;
    }

    public String[] getRevisedbasic() {
        return revisedbasic;
    }

    public void setRevisedbasic(String[] revisedbasic) {
        this.revisedbasic = revisedbasic;
    }

    public String[] getIncrLevel() {
        return incrLevel;
    }

    public void setIncrLevel(String[] incrLevel) {
        this.incrLevel = incrLevel;
    }

    public String getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(String isApproved) {
        this.isApproved = isApproved;
    }

    public String getRevisionCurBasic() {
        return revisionCurBasic;
    }

    public void setRevisionCurBasic(String revisionCurBasic) {
        this.revisionCurBasic = revisionCurBasic;
    }

    public String getPreviousPayScale() {
        return previousPayScale;
    }

    public void setPreviousPayScale(String previousPayScale) {
        this.previousPayScale = previousPayScale;
    }

    public String getPreviousGp() {
        return previousGp;
    }

    public void setPreviousGp(String previousGp) {
        this.previousGp = previousGp;
    }

    public String getEntryPost() {
        return entryPost;
    }

    public void setEntryPost(String entryPost) {
        this.entryPost = entryPost;
    }

    public String getEntryDeptCode() {
        return entryDeptCode;
    }

    public void setEntryDeptCode(String entryDeptCode) {
        this.entryDeptCode = entryDeptCode;
    }

    public String getIsIASCadre() {
        return isIASCadre;
    }

    public void setIsIASCadre(String isIASCadre) {
        this.isIASCadre = isIASCadre;
    }

    public String getJuniorName() {
        return juniorName;
    }

    public void setJuniorName(String juniorName) {
        this.juniorName = juniorName;
    }

    public String getPaySubstantive() {
        return paySubstantive;
    }

    public void setPaySubstantive(String paySubstantive) {
        this.paySubstantive = paySubstantive;
    }

    public String getPayPersonal() {
        return payPersonal;
    }

    public void setPayPersonal(String payPersonal) {
        this.payPersonal = payPersonal;
    }

    public String getNpAllowance() {
        return npAllowance;
    }

    public void setNpAllowance(String npAllowance) {
        this.npAllowance = npAllowance;
    }

    public String getDeptCodeIAS() {
        return deptCodeIAS;
    }

    public void setDeptCodeIAS(String deptCodeIAS) {
        this.deptCodeIAS = deptCodeIAS;
    }

    public String[] getPp() {
        return pp;
    }

    public void setPp(String[] pp) {
        this.pp = pp;
    }

    public String getIsUGCCadre() {
        return isUGCCadre;
    }

    public void setIsUGCCadre(String isUGCCadre) {
        this.isUGCCadre = isUGCCadre;
    }
    
}
