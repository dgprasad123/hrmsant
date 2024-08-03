/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.conclusionproceedings;

import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Manas
 */
public class ConclusionProceedings {

    private String empid;
    private String gpfNo;
    private String empNameForProceeding;
    private String empSpcForProceeding;
    private String empPostForProceeding;
    private String initNotOrdNo = null;
    private String initNotOrdDt = null;
    private int initnotid;
    private String offcode;
    private String searchby;

    private String notauthority = null;
    private String notdept = null;
    private String notoffice = null;
    private String notspc = null;

    private String postedPostName = null;
    private String posteddept = null;
    private String postedoffice = null;
    private String postedspc = null;

    private int scnnotid;
    private int crnotid;
    private int resnotid;
    private String showcauseOrdNo = null;
    private String showcauseOrdDt = null;
    private String showcausenotdept = null;
    private String showcausenotoffice = null;
    private String showcausenotspc = null;
    private String showcausenotauthority = null;
    private String showcausedate = null;

    private String receiprofcomplianceOrdNo;
    private String receiprofcomplianceOrdDt;
    private String receiprofcompliancenotdept;
    private String receiprofcompliancenotoffice;
    private String receiprofcompliancenotspc;
    private String receiprofcompliancenotauthority;
    private String receiprofcompliancedate;

    private String freefromcharges;
    private String conclusionOrdNo;
    private String conclusionOrdDt;
    private String conclusionnotdept;
    private String conclusionnotoffice;
    private String conclusionnotspc;
    private String conclusionnotauthority;

    private String svid = null;
    private int concprocid;
    private String doesvbk = null;
    private String concprocOrNo = null;
    private String concprocOrdate = null;
    private String auth = null;
    private String doiscnotice = null;
    private String comprcptdate = null;
    private String ifsensure = null;
    private String causeofproc = null;
    private String ruleofproc;
    private String alreadyAdded;
    private String initiatedByempId;
    private String initiatedByspc;
    private String initonDate;
    private String censureType;
    private MultipartFile showcauseDocument;
    private String showcauseoriginalfilename;
    private String showcausediskFileName;
    private String showcauseContentType;
    private MultipartFile backlogDocument;
    private String diskFileName;
    private String originalFilename;
    private String getContentType;
    private MultipartFile uploadDocument;
    private String punishmentRewarded;
    private byte[] Filecontent;
    private String finalOrderNumber;
    private String finalOrderDate;

    private String typeofleave;
    private String tolid;
    private String tol;
    private String fullName;
    private String post;
    private String chkNotSBPrint;
    private String isValidated;
    private String ifCensure;
    private String ifPunishment;
    private String punishmentType;
    private String narrationForFreeCharge;

    public String getOffcode() {
        return offcode;
    }

    public void setOffcode(String offcode) {
        this.offcode = offcode;
    }

    public String getSearchby() {
        return searchby;
    }

    public void setSearchby(String searchby) {
        this.searchby = searchby;
    }

    public int getScnnotid() {
        return scnnotid;
    }

    public void setScnnotid(int scnnotid) {
        this.scnnotid = scnnotid;
    }

    public int getCrnotid() {
        return crnotid;
    }

    public void setCrnotid(int crnotid) {
        this.crnotid = crnotid;
    }

    public int getResnotid() {
        return resnotid;
    }

    public void setResnotid(int resnotid) {
        this.resnotid = resnotid;
    }

    public String getFreefromcharges() {
        return freefromcharges;
    }

    public void setFreefromcharges(String freefromcharges) {
        this.freefromcharges = freefromcharges;
    }

    public String getConclusionOrdNo() {
        return conclusionOrdNo;
    }

    public void setConclusionOrdNo(String conclusionOrdNo) {
        this.conclusionOrdNo = conclusionOrdNo;
    }

    public String getConclusionOrdDt() {
        return conclusionOrdDt;
    }

    public void setConclusionOrdDt(String conclusionOrdDt) {
        this.conclusionOrdDt = conclusionOrdDt;
    }

    public String getConclusionnotdept() {
        return conclusionnotdept;
    }

    public void setConclusionnotdept(String conclusionnotdept) {
        this.conclusionnotdept = conclusionnotdept;
    }

    public String getConclusionnotoffice() {
        return conclusionnotoffice;
    }

    public void setConclusionnotoffice(String conclusionnotoffice) {
        this.conclusionnotoffice = conclusionnotoffice;
    }

    public String getConclusionnotspc() {
        return conclusionnotspc;
    }

    public void setConclusionnotspc(String conclusionnotspc) {
        this.conclusionnotspc = conclusionnotspc;
    }

    public String getConclusionnotauthority() {
        return conclusionnotauthority;
    }

    public void setConclusionnotauthority(String conclusionnotauthority) {
        this.conclusionnotauthority = conclusionnotauthority;
    }

    public String getReceiprofcomplianceOrdNo() {
        return receiprofcomplianceOrdNo;
    }

    public void setReceiprofcomplianceOrdNo(String receiprofcomplianceOrdNo) {
        this.receiprofcomplianceOrdNo = receiprofcomplianceOrdNo;
    }

    public String getReceiprofcomplianceOrdDt() {
        return receiprofcomplianceOrdDt;
    }

    public void setReceiprofcomplianceOrdDt(String receiprofcomplianceOrdDt) {
        this.receiprofcomplianceOrdDt = receiprofcomplianceOrdDt;
    }

    public String getReceiprofcompliancenotdept() {
        return receiprofcompliancenotdept;
    }

    public void setReceiprofcompliancenotdept(String receiprofcompliancenotdept) {
        this.receiprofcompliancenotdept = receiprofcompliancenotdept;
    }

    public String getReceiprofcompliancenotoffice() {
        return receiprofcompliancenotoffice;
    }

    public void setReceiprofcompliancenotoffice(String receiprofcompliancenotoffice) {
        this.receiprofcompliancenotoffice = receiprofcompliancenotoffice;
    }

    public String getReceiprofcompliancenotspc() {
        return receiprofcompliancenotspc;
    }

    public void setReceiprofcompliancenotspc(String receiprofcompliancenotspc) {
        this.receiprofcompliancenotspc = receiprofcompliancenotspc;
    }

    public String getReceiprofcompliancenotauthority() {
        return receiprofcompliancenotauthority;
    }

    public void setReceiprofcompliancenotauthority(String receiprofcompliancenotauthority) {
        this.receiprofcompliancenotauthority = receiprofcompliancenotauthority;
    }

    public String getReceiprofcompliancedate() {
        return receiprofcompliancedate;
    }

    public void setReceiprofcompliancedate(String receiprofcompliancedate) {
        this.receiprofcompliancedate = receiprofcompliancedate;
    }

    public String getShowcausedate() {
        return showcausedate;
    }

    public void setShowcausedate(String showcausedate) {
        this.showcausedate = showcausedate;
    }

    public String getShowcausenotdept() {
        return showcausenotdept;
    }

    public void setShowcausenotdept(String showcausenotdept) {
        this.showcausenotdept = showcausenotdept;
    }

    public String getShowcausenotoffice() {
        return showcausenotoffice;
    }

    public void setShowcausenotoffice(String showcausenotoffice) {
        this.showcausenotoffice = showcausenotoffice;
    }

    public String getShowcausenotspc() {
        return showcausenotspc;
    }

    public void setShowcausenotspc(String showcausenotspc) {
        this.showcausenotspc = showcausenotspc;
    }

    public String getShowcausenotauthority() {
        return showcausenotauthority;
    }

    public void setShowcausenotauthority(String showcausenotauthority) {
        this.showcausenotauthority = showcausenotauthority;
    }

    public String getShowcauseOrdNo() {
        return showcauseOrdNo;
    }

    public void setShowcauseOrdNo(String showcauseOrdNo) {
        this.showcauseOrdNo = showcauseOrdNo;
    }

    public String getShowcauseOrdDt() {
        return showcauseOrdDt;
    }

    public void setShowcauseOrdDt(String showcauseOrdDt) {
        this.showcauseOrdDt = showcauseOrdDt;
    }

    public String getEmpid() {
        return empid;
    }

    public void setEmpid(String empid) {
        this.empid = empid;
    }

    public String getGpfNo() {
        return gpfNo;
    }

    public void setGpfNo(String gpfNo) {
        this.gpfNo = gpfNo;
    }

    public String getEmpNameForProceeding() {
        return empNameForProceeding;
    }

    public void setEmpNameForProceeding(String empNameForProceeding) {
        this.empNameForProceeding = empNameForProceeding;
    }

    public String getEmpSpcForProceeding() {
        return empSpcForProceeding;
    }

    public void setEmpSpcForProceeding(String empSpcForProceeding) {
        this.empSpcForProceeding = empSpcForProceeding;
    }

    public String getEmpPostForProceeding() {
        return empPostForProceeding;
    }

    public void setEmpPostForProceeding(String empPostForProceeding) {
        this.empPostForProceeding = empPostForProceeding;
    }

    public String getInitNotOrdNo() {
        return initNotOrdNo;
    }

    public void setInitNotOrdNo(String initNotOrdNo) {
        this.initNotOrdNo = initNotOrdNo;
    }

    public String getInitNotOrdDt() {
        return initNotOrdDt;
    }

    public void setInitNotOrdDt(String initNotOrdDt) {
        this.initNotOrdDt = initNotOrdDt;
    }

    public int getInitnotid() {
        return initnotid;
    }

    public void setInitnotid(int initnotid) {
        this.initnotid = initnotid;
    }

    public String getNotauthority() {
        return notauthority;
    }

    public void setNotauthority(String notauthority) {
        this.notauthority = notauthority;
    }

    public String getNotdept() {
        return notdept;
    }

    public void setNotdept(String notdept) {
        this.notdept = notdept;
    }

    public String getNotoffice() {
        return notoffice;
    }

    public void setNotoffice(String notoffice) {
        this.notoffice = notoffice;
    }

    public String getNotspc() {
        return notspc;
    }

    public void setNotspc(String notspc) {
        this.notspc = notspc;
    }

    public String getPostedPostName() {
        return postedPostName;
    }

    public void setPostedPostName(String postedPostName) {
        this.postedPostName = postedPostName;
    }

    public String getPosteddept() {
        return posteddept;
    }

    public void setPosteddept(String posteddept) {
        this.posteddept = posteddept;
    }

    public String getPostedoffice() {
        return postedoffice;
    }

    public void setPostedoffice(String postedoffice) {
        this.postedoffice = postedoffice;
    }

    public String getPostedspc() {
        return postedspc;
    }

    public void setPostedspc(String postedspc) {
        this.postedspc = postedspc;
    }

    public String getSvid() {
        return svid;
    }

    public void setSvid(String svid) {
        this.svid = svid;
    }

    public int getConcprocid() {
        return concprocid;
    }

    public void setConcprocid(int concprocid) {
        this.concprocid = concprocid;
    }

    public String getDoesvbk() {
        return doesvbk;
    }

    public void setDoesvbk(String doesvbk) {
        this.doesvbk = doesvbk;
    }

    public String getConcprocOrNo() {
        return concprocOrNo;
    }

    public void setConcprocOrNo(String concprocOrNo) {
        this.concprocOrNo = concprocOrNo;
    }

    public String getConcprocOrdate() {
        return concprocOrdate;
    }

    public void setConcprocOrdate(String concprocOrdate) {
        this.concprocOrdate = concprocOrdate;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public String getDoiscnotice() {
        return doiscnotice;
    }

    public void setDoiscnotice(String doiscnotice) {
        this.doiscnotice = doiscnotice;
    }

    public String getComprcptdate() {
        return comprcptdate;
    }

    public void setComprcptdate(String comprcptdate) {
        this.comprcptdate = comprcptdate;
    }

    public String getIfsensure() {
        return ifsensure;
    }

    public void setIfsensure(String ifsensure) {
        this.ifsensure = ifsensure;
    }

    public String getCauseofproc() {
        return causeofproc;
    }

    public void setCauseofproc(String causeofproc) {
        this.causeofproc = causeofproc;
    }

    public String getRuleofproc() {
        return ruleofproc;
    }

    public void setRuleofproc(String ruleofproc) {
        this.ruleofproc = ruleofproc;
    }

    public String getAlreadyAdded() {
        return alreadyAdded;
    }

    public void setAlreadyAdded(String alreadyAdded) {
        this.alreadyAdded = alreadyAdded;
    }

    public String getInitiatedByempId() {
        return initiatedByempId;
    }

    public void setInitiatedByempId(String initiatedByempId) {
        this.initiatedByempId = initiatedByempId;
    }

    public String getInitiatedByspc() {
        return initiatedByspc;
    }

    public void setInitiatedByspc(String initiatedByspc) {
        this.initiatedByspc = initiatedByspc;
    }

    public String getInitonDate() {
        return initonDate;
    }

    public void setInitonDate(String initonDate) {
        this.initonDate = initonDate;
    }

    public String getCensureType() {
        return censureType;
    }

    public void setCensureType(String censureType) {
        this.censureType = censureType;
    }

    public MultipartFile getShowcauseDocument() {
        return showcauseDocument;
    }

    public void setShowcauseDocument(MultipartFile showcauseDocument) {
        this.showcauseDocument = showcauseDocument;
    }

    public String getShowcauseoriginalfilename() {
        return showcauseoriginalfilename;
    }

    public void setShowcauseoriginalfilename(String showcauseoriginalfilename) {
        this.showcauseoriginalfilename = showcauseoriginalfilename;
    }

    public String getShowcausediskFileName() {
        return showcausediskFileName;
    }

    public void setShowcausediskFileName(String showcausediskFileName) {
        this.showcausediskFileName = showcausediskFileName;
    }

    public String getShowcauseContentType() {
        return showcauseContentType;
    }

    public void setShowcauseContentType(String showcauseContentType) {
        this.showcauseContentType = showcauseContentType;
    }

    public MultipartFile getBacklogDocument() {
        return backlogDocument;
    }

    public void setBacklogDocument(MultipartFile backlogDocument) {
        this.backlogDocument = backlogDocument;
    }

    public String getDiskFileName() {
        return diskFileName;
    }

    public void setDiskFileName(String diskFileName) {
        this.diskFileName = diskFileName;
    }

    public String getOriginalFilename() {
        return originalFilename;
    }

    public void setOriginalFilename(String originalFilename) {
        this.originalFilename = originalFilename;
    }

    public String getGetContentType() {
        return getContentType;
    }

    public void setGetContentType(String getContentType) {
        this.getContentType = getContentType;
    }

    public MultipartFile getUploadDocument() {
        return uploadDocument;
    }

    public void setUploadDocument(MultipartFile uploadDocument) {
        this.uploadDocument = uploadDocument;
    }

    public String getPunishmentRewarded() {
        return punishmentRewarded;
    }

    public void setPunishmentRewarded(String punishmentRewarded) {
        this.punishmentRewarded = punishmentRewarded;
    }

    public byte[] getFilecontent() {
        return Filecontent;
    }

    public void setFilecontent(byte[] Filecontent) {
        this.Filecontent = Filecontent;
    }

    public String getFinalOrderNumber() {
        return finalOrderNumber;
    }

    public void setFinalOrderNumber(String finalOrderNumber) {
        this.finalOrderNumber = finalOrderNumber;
    }

    public String getFinalOrderDate() {
        return finalOrderDate;
    }

    public void setFinalOrderDate(String finalOrderDate) {
        this.finalOrderDate = finalOrderDate;
    }

    public String getTypeofleave() {
        return typeofleave;
    }

    public void setTypeofleave(String typeofleave) {
        this.typeofleave = typeofleave;
    }

    public String getTolid() {
        return tolid;
    }

    public void setTolid(String tolid) {
        this.tolid = tolid;
    }

    public String getTol() {
        return tol;
    }

    public void setTol(String tol) {
        this.tol = tol;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getChkNotSBPrint() {
        return chkNotSBPrint;
    }

    public void setChkNotSBPrint(String chkNotSBPrint) {
        this.chkNotSBPrint = chkNotSBPrint;
    }

    public String getIsValidated() {
        return isValidated;
    }

    public void setIsValidated(String isValidated) {
        this.isValidated = isValidated;
    }

    public String getIfCensure() {
        return ifCensure;
    }

    public void setIfCensure(String ifCensure) {
        this.ifCensure = ifCensure;
    }

    public String getIfPunishment() {
        return ifPunishment;
    }

    public void setIfPunishment(String ifPunishment) {
        this.ifPunishment = ifPunishment;
    }

    public String getPunishmentType() {
        return punishmentType;
    }

    public void setPunishmentType(String punishmentType) {
        this.punishmentType = punishmentType;
    }

    public String getNarrationForFreeCharge() {
        return narrationForFreeCharge;
    }

    public void setNarrationForFreeCharge(String narrationForFreeCharge) {
        this.narrationForFreeCharge = narrationForFreeCharge;
    }

}
