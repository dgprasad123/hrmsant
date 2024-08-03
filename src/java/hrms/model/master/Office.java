/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.master;

import java.io.Serializable;
import java.util.List;

public class Office implements Serializable {

    private String offCode = null;
    private String offEn = null;
    private String offName = null;
    private String deptCode = null;
    private String category = null;
    private String suffix = null;
    private String lvl = null;
    private String lvlDesc=null;
    private String ifGroup = null;
    private String offAddress = null;
    private String stateCode = null;
    private String distCode = null;
    private String blockCode = null;
    private String psCode = null;
    private String poCode = null;
    private String villCode = null;
    private String pincode = null;
    private String trCode = null;
    private String ddoCode = null;
    private String ddoPost = null;
    private String ddoPostName = null;
    private String telStd = null;
    private String telNo = null;
    private String faxStd = null;
    private String faxNo = null;
    private String offEmail = null;
    private String offIncharge = null;
    private String poffCode = null;
    private String sltBank = null;
    private String sltBranch = null;
    private String recBy = null;
    private String desg = null;
    private String salHead = null;
    private String salHeadDesc = null;
    private String offStatus = null;
    private String ddoRegNo = null;
    private String tanNo = null;
    private String dtoRegNo = null;
    private String onlineBillSubmission = null;
    private String paCode = null;
    private String ddoCurAccNo = null;
    private String isDdo = null;
    private String offBillStatus = null;
    private String ddoSpc = null;
    private String ddoHrmsid = null;
    private String officeCategoryId = null;
    private String hodOfficeCode = null;
    private String subDivisionCode = null;
    private int paybillPriority = 0;
    private String pTypeId = null;
    private String extFieldCode = null;
    private String haveSubOffice = null;
    private String offAbbrev = null;
    private String intDdoId = null;
    private String stateName = null;
    private String ddoName = null;
    private String subDivisionName = null;
    private String blockName = null;
    private String deptName = null;
    private String distName = null;
    private String villagename = null;
    private String mode = null;
    private int noofemployee;
    private String sltBlock = null;
    private int aerId = 0;
    private List aerPostData=null;
    private String hidOfficeCode=null;
    private String backLogOfficeCode=null;
    private String hidDeptCode=null;
    private String hidDistCode=null;
    private String hidMode=null;
    private String hidOfcType=null;
    private String maxOfcCode=null;
    private String hidMaxOffCode=null;
    private String billGrpSpn;
    private String sltOffCode=null;
    private String fyYear=null;

    public int getAerId() {
        return aerId;
    }

    public void setAerId(int aerId) {
        this.aerId = aerId;
    }

    public String getSltBlock() {
        return sltBlock;
    }

    public void setSltBlock(String sltBlock) {
        this.sltBlock = sltBlock;
    }

    public String getVillagename() {
        return villagename;
    }

    public void setVillagename(String villagename) {
        this.villagename = villagename;
    }

    public String getDistName() {
        return distName;
    }

    public void setDistName(String distName) {
        this.distName = distName;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getBlockName() {
        return blockName;
    }

    public void setBlockName(String blockName) {
        this.blockName = blockName;
    }

    public String getpTypeId() {
        return pTypeId;
    }

    public void setpTypeId(String pTypeId) {
        this.pTypeId = pTypeId;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getDdoName() {
        return ddoName;
    }

    public void setDdoName(String ddoName) {
        this.ddoName = ddoName;
    }

    public String getSubDivisionName() {
        return subDivisionName;
    }

    public void setSubDivisionName(String subDivisionName) {
        this.subDivisionName = subDivisionName;
    }

    public String getOffCode() {
        return offCode;
    }

    public void setOffCode(String offCode) {
        this.offCode = offCode;
    }

    public String getOffEn() {
        return offEn;
    }

    public void setOffEn(String offEn) {
        this.offEn = offEn;
    }

    public String getOffName() {
        return offName;
    }

    public void setOffName(String offName) {
        this.offName = offName;
    }

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getLvl() {
        return lvl;
    }

    public void setLvl(String lvl) {
        this.lvl = lvl;
    }

    public String getIfGroup() {
        return ifGroup;
    }

    public void setIfGroup(String ifGroup) {
        this.ifGroup = ifGroup;
    }

    public String getOffAddress() {
        return offAddress;
    }

    public void setOffAddress(String offAddress) {
        this.offAddress = offAddress;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getDistCode() {
        return distCode;
    }

    public void setDistCode(String distCode) {
        this.distCode = distCode;
    }

    public String getBlockCode() {
        return blockCode;
    }

    public void setBlockCode(String blockCode) {
        this.blockCode = blockCode;
    }

    public String getPsCode() {
        return psCode;
    }

    public void setPsCode(String psCode) {
        this.psCode = psCode;
    }

    public String getPoCode() {
        return poCode;
    }

    public void setPoCode(String poCode) {
        this.poCode = poCode;
    }

    public String getVillCode() {
        return villCode;
    }

    public void setVillCode(String villCode) {
        this.villCode = villCode;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getTrCode() {
        return trCode;
    }

    public void setTrCode(String trCode) {
        this.trCode = trCode;
    }

    public String getDdoCode() {
        return ddoCode;
    }

    public void setDdoCode(String ddoCode) {
        this.ddoCode = ddoCode;
    }

    public String getDdoPost() {
        return ddoPost;
    }

    public void setDdoPost(String ddoPost) {
        this.ddoPost = ddoPost;
    }

    public String getTelStd() {
        return telStd;
    }

    public void setTelStd(String telStd) {
        this.telStd = telStd;
    }

    public String getTelNo() {
        return telNo;
    }

    public void setTelNo(String telNo) {
        this.telNo = telNo;
    }

    public String getFaxStd() {
        return faxStd;
    }

    public void setFaxStd(String faxStd) {
        this.faxStd = faxStd;
    }

    public String getFaxNo() {
        return faxNo;
    }

    public void setFaxNo(String faxNo) {
        this.faxNo = faxNo;
    }

    public String getOffEmail() {
        return offEmail;
    }

    public void setOffEmail(String offEmail) {
        this.offEmail = offEmail;
    }

    public String getOffIncharge() {
        return offIncharge;
    }

    public void setOffIncharge(String offIncharge) {
        this.offIncharge = offIncharge;
    }

    public String getSltBank() {
        return sltBank;
    }

    public void setSltBank(String sltBank) {
        this.sltBank = sltBank;
    }

    public String getSltBranch() {
        return sltBranch;
    }

    public void setSltBranch(String sltBranch) {
        this.sltBranch = sltBranch;
    }

    public String getRecBy() {
        return recBy;
    }

    public void setRecBy(String recBy) {
        this.recBy = recBy;
    }

    public String getDesg() {
        return desg;
    }

    public void setDesg(String desg) {
        this.desg = desg;
    }

    public String getSalHead() {
        return salHead;
    }

    public void setSalHead(String salHead) {
        this.salHead = salHead;
    }

    public String getSalHeadDesc() {
        return salHeadDesc;
    }

    public void setSalHeadDesc(String salHeadDesc) {
        this.salHeadDesc = salHeadDesc;
    }

    public String getOffStatus() {
        return offStatus;
    }

    public void setOffStatus(String offStatus) {
        this.offStatus = offStatus;
    }

    public String getDdoRegNo() {
        return ddoRegNo;
    }

    public void setDdoRegNo(String ddoRegNo) {
        this.ddoRegNo = ddoRegNo;
    }

    public String getTanNo() {
        return tanNo;
    }

    public void setTanNo(String tanNo) {
        this.tanNo = tanNo;
    }

    public String getDtoRegNo() {
        return dtoRegNo;
    }

    public void setDtoRegNo(String dtoRegNo) {
        this.dtoRegNo = dtoRegNo;
    }

    public String getOnlineBillSubmission() {
        return onlineBillSubmission;
    }

    public void setOnlineBillSubmission(String onlineBillSubmission) {
        this.onlineBillSubmission = onlineBillSubmission;
    }

    public String getPaCode() {
        return paCode;
    }

    public void setPaCode(String paCode) {
        this.paCode = paCode;
    }

    public String getDdoCurAccNo() {
        return ddoCurAccNo;
    }

    public void setDdoCurAccNo(String ddoCurAccNo) {
        this.ddoCurAccNo = ddoCurAccNo;
    }

    public String getIsDdo() {
        return isDdo;
    }

    public void setIsDdo(String isDdo) {
        this.isDdo = isDdo;
    }

    public String getOffBillStatus() {
        return offBillStatus;
    }

    public void setOffBillStatus(String offBillStatus) {
        this.offBillStatus = offBillStatus;
    }

    public String getDdoSpc() {
        return ddoSpc;
    }

    public void setDdoSpc(String ddoSpc) {
        this.ddoSpc = ddoSpc;
    }

    public String getDdoHrmsid() {
        return ddoHrmsid;
    }

    public void setDdoHrmsid(String ddoHrmsid) {
        this.ddoHrmsid = ddoHrmsid;
    }

    public String getOfficeCategoryId() {
        return officeCategoryId;
    }

    public void setOfficeCategoryId(String officeCategoryId) {
        this.officeCategoryId = officeCategoryId;
    }

    public String getHodOfficeCode() {
        return hodOfficeCode;
    }

    public void setHodOfficeCode(String hodOfficeCode) {
        this.hodOfficeCode = hodOfficeCode;
    }

    public String getSubDivisionCode() {
        return subDivisionCode;
    }

    public void setSubDivisionCode(String subDivisionCode) {
        this.subDivisionCode = subDivisionCode;
    }

    public int getPaybillPriority() {
        return paybillPriority;
    }

    public void setPaybillPriority(int paybillPriority) {
        this.paybillPriority = paybillPriority;
    }

    public String getPTypeId() {
        return pTypeId;
    }

    public void setPTypeId(String pTypeId) {
        this.pTypeId = pTypeId;
    }

    public String getExtFieldCode() {
        return extFieldCode;
    }

    public void setExtFieldCode(String extFieldCode) {
        this.extFieldCode = extFieldCode;
    }

    public String getHaveSubOffice() {
        return haveSubOffice;
    }

    public void setHaveSubOffice(String haveSubOffice) {
        this.haveSubOffice = haveSubOffice;
    }

    public String getOffAbbrev() {
        return offAbbrev;
    }

    public void setOffAbbrev(String offAbbrev) {
        this.offAbbrev = offAbbrev;
    }

    public String getIntDdoId() {
        return intDdoId;
    }

    public void setIntDdoId(String intDdoId) {
        this.intDdoId = intDdoId;
    }

    public String getPoffCode() {
        return poffCode;
    }

    public void setPoffCode(String poffCode) {
        this.poffCode = poffCode;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public int getNoofemployee() {
        return noofemployee;
    }

    public void setNoofemployee(int noofemployee) {
        this.noofemployee = noofemployee;
    }

    public String getDdoPostName() {
        return ddoPostName;
    }

    public void setDdoPostName(String ddoPostName) {
        this.ddoPostName = ddoPostName;
    }

    public List getAerPostData() {
        return aerPostData;
    }

    public void setAerPostData(List aerPostData) {
        this.aerPostData = aerPostData;
    }

    public String getHidOfficeCode() {
        return hidOfficeCode;
    }

    public void setHidOfficeCode(String hidOfficeCode) {
        this.hidOfficeCode = hidOfficeCode;
    }

    public String getBackLogOfficeCode() {
        return backLogOfficeCode;
    }

    public void setBackLogOfficeCode(String backLogOfficeCode) {
        this.backLogOfficeCode = backLogOfficeCode;
    }

    public String getHidDeptCode() {
        return hidDeptCode;
    }

    public void setHidDeptCode(String hidDeptCode) {
        this.hidDeptCode = hidDeptCode;
    }

    public String getHidDistCode() {
        return hidDistCode;
    }

    public void setHidDistCode(String hidDistCode) {
        this.hidDistCode = hidDistCode;
    }

    public String getHidMode() {
        return hidMode;
    }

    public void setHidMode(String hidMode) {
        this.hidMode = hidMode;
    }    

    public String getLvlDesc() {
        return lvlDesc;
    }

    public void setLvlDesc(String lvlDesc) {
        this.lvlDesc = lvlDesc;
    }

    public String getHidOfcType() {
        return hidOfcType;
    }

    public void setHidOfcType(String hidOfcType) {
        this.hidOfcType = hidOfcType;
    }

    public String getMaxOfcCode() {
        return maxOfcCode;
    }

    public void setMaxOfcCode(String maxOfcCode) {
        this.maxOfcCode = maxOfcCode;
    }

    public String getHidMaxOffCode() {
        return hidMaxOffCode;
    }

    public void setHidMaxOffCode(String hidMaxOffCode) {
        this.hidMaxOffCode = hidMaxOffCode;
    }

    public String getBillGrpSpn() {
        return billGrpSpn;
    }

    public void setBillGrpSpn(String billGrpSpn) {
        this.billGrpSpn = billGrpSpn;
    }

    public String getSltOffCode() {
        return sltOffCode;
    }

    public void setSltOffCode(String sltOffCode) {
        this.sltOffCode = sltOffCode;
    }

    public String getFyYear() {
        return fyYear;
    }

    public void setFyYear(String fyYear) {
        this.fyYear = fyYear;
    }

   
    
    
}
