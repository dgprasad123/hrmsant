/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.parmast;

import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Manisha
 */
public class ParAdverseCommunicationDetail {

    private int parId;
    private String encParId;
    private int communicationId;
    private String fromempId;
    private String fromempName;
    private String fromPost;
    private String fromspc;
    private String toempId;
    private String toempName;
    private String toPost;
    private String tospc;
    private String remarksdetail;
    private int attachmentId;
    private String diskFileName;
    private String originalFilename;
    private String getContentType;
    private byte[] filecontent = null;
    private MultipartFile uploadDocument;
    private int taskId;
    private String remarksOnDate;
    private String authoritytype;
    private int authid;
    private String privilegedSpc;    
    private String adverseRemarkstype;
    private int adverseCommunicationStatusId;
    private String adverseCommunicationOnDate;
    private String adverseRemarksOnDate;
    private String fromAuthType;
    private String toAuthType;
    private int taskNextStatusId;
    private String mobileNo;
    private String appraiseFname;
    private String fiscalYear;
    private String appraisePlaceOfPosting;
    private String appraiseHrmsId;
    private String appraiseGpfNo;
    private String siType;
    private String parPeriodFrom;
    private String parPeriodTo;
    private String custodianOffice;
    private String custodianFullName;
    private String custodianDesignation;
    private String custodianAddress;
    private String finalGrading;
    private String appraisePostingDetail;
    private String webAddress;
    private String webSite;
    private String pinCode;
    private String isActive;
    private String custodianEmpId;
    private int detailIdForCustodianEntry;
    private String telFaxNo;
    private String custodianLoginId;
    private String opLogoFilePath;
    
    
    
    public String getToPost() {
        return toPost;
    }

    public void setToPost(String toPost) {
        this.toPost = toPost;
    }

    public int getParId() {
        return parId;
    }

    public void setParId(int parId) {
        this.parId = parId;
    }

    public int getCommunicationId() {
        return communicationId;
    }

    public void setCommunicationId(int communicationId) {
        this.communicationId = communicationId;
    }

    public String getFromempId() {
        return fromempId;
    }

    public void setFromempId(String fromempId) {
        this.fromempId = fromempId;
    }

    public String getFromempName() {
        return fromempName;
    }

    public void setFromempName(String fromempName) {
        this.fromempName = fromempName;
    }

    public String getFromPost() {
        return fromPost;
    }

    public void setFromPost(String fromPost) {
        this.fromPost = fromPost;
    }

    public String getFromspc() {
        return fromspc;
    }

    public void setFromspc(String fromspc) {
        this.fromspc = fromspc;
    }

    public String getToempId() {
        return toempId;
    }

    public void setToempId(String toempId) {
        this.toempId = toempId;
    }

    public String getToempName() {
        return toempName;
    }

    public void setToempName(String toempName) {
        this.toempName = toempName;
    }

    public String getTospc() {
        return tospc;
    }

    public void setTospc(String tospc) {
        this.tospc = tospc;
    }

    public String getRemarksdetail() {
        return remarksdetail;
    }

    public void setRemarksdetail(String remarksdetail) {
        this.remarksdetail = remarksdetail;
    }

    public int getAttachmentId() {
        return attachmentId;
    }

    public void setAttachmentId(int attachmentId) {
        this.attachmentId = attachmentId;
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

    public byte[] getFilecontent() {
        return filecontent;
    }

    public void setFilecontent(byte[] filecontent) {
        this.filecontent = filecontent;
    }

    public MultipartFile getUploadDocument() {
        return uploadDocument;
    }

    public void setUploadDocument(MultipartFile uploadDocument) {
        this.uploadDocument = uploadDocument;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getRemarksOnDate() {
        return remarksOnDate;
    }

    public void setRemarksOnDate(String remarksOnDate) {
        this.remarksOnDate = remarksOnDate;
    }

    public String getAuthoritytype() {
        return authoritytype;
    }

    public void setAuthoritytype(String authoritytype) {
        this.authoritytype = authoritytype;
    }

    public int getAuthid() {
        return authid;
    }

    public void setAuthid(int authid) {
        this.authid = authid;
    }

    public String getPrivilegedSpc() {
        return privilegedSpc;
    }

    public void setPrivilegedSpc(String privilegedSpc) {
        this.privilegedSpc = privilegedSpc;
    }    

    public String getAdverseRemarkstype() {
        return adverseRemarkstype;
    }

    public void setAdverseRemarkstype(String adverseRemarkstype) {
        this.adverseRemarkstype = adverseRemarkstype;
    }

    public int getAdverseCommunicationStatusId() {
        return adverseCommunicationStatusId;
    }

    public void setAdverseCommunicationStatusId(int adverseCommunicationStatusId) {
        this.adverseCommunicationStatusId = adverseCommunicationStatusId;
    }

    public String getAdverseCommunicationOnDate() {
        return adverseCommunicationOnDate;
    }

    public void setAdverseCommunicationOnDate(String adverseCommunicationOnDate) {
        this.adverseCommunicationOnDate = adverseCommunicationOnDate;
    }

    public String getAdverseRemarksOnDate() {
        return adverseRemarksOnDate;
    }

    public void setAdverseRemarksOnDate(String adverseRemarksOnDate) {
        this.adverseRemarksOnDate = adverseRemarksOnDate;
    }

    public String getFromAuthType() {
        return fromAuthType;
    }

    public void setFromAuthType(String fromAuthType) {
        this.fromAuthType = fromAuthType;
    }

    public String getToAuthType() {
        return toAuthType;
    }

    public void setToAuthType(String toAuthType) {
        this.toAuthType = toAuthType;
    }

    public int getTaskNextStatusId() {
        return taskNextStatusId;
    }

    public void setTaskNextStatusId(int taskNextStatusId) {
        this.taskNextStatusId = taskNextStatusId;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }
    
    public String getEncParId() {
        return encParId;
    }

    public void setEncParId(String encParId)throws Exception {
        this.encParId = encParId;
    }

    public String getAppraiseFname() {
        return appraiseFname;
    }

    public void setAppraiseFname(String appraiseFname) {
        this.appraiseFname = appraiseFname;
    }

    public String getFiscalYear() {
        return fiscalYear;
    }

    public void setFiscalYear(String fiscalYear) {
        this.fiscalYear = fiscalYear;
    }

    public String getAppraisePlaceOfPosting() {
        return appraisePlaceOfPosting;
    }

    public void setAppraisePlaceOfPosting(String appraisePlaceOfPosting) {
        this.appraisePlaceOfPosting = appraisePlaceOfPosting;
    }

    public String getAppraiseHrmsId() {
        return appraiseHrmsId;
    }

    public void setAppraiseHrmsId(String appraiseHrmsId) {
        this.appraiseHrmsId = appraiseHrmsId;
    }

    public String getAppraiseGpfNo() {
        return appraiseGpfNo;
    }

    public void setAppraiseGpfNo(String appraiseGpfNo) {
        this.appraiseGpfNo = appraiseGpfNo;
    }

    public String getSiType() {
        return siType;
    }

    public void setSiType(String siType) {
        this.siType = siType;
    }

    public String getParPeriodFrom() {
        return parPeriodFrom;
    }

    public void setParPeriodFrom(String parPeriodFrom) {
        this.parPeriodFrom = parPeriodFrom;
    }

    public String getParPeriodTo() {
        return parPeriodTo;
    }

    public void setParPeriodTo(String parPeriodTo) {
        this.parPeriodTo = parPeriodTo;
    }

    public String getCustodianOffice() {
        return custodianOffice;
    }

    public void setCustodianOffice(String custodianOffice) {
        this.custodianOffice = custodianOffice;
    }

    public String getCustodianFullName() {
        return custodianFullName;
    }

    public void setCustodianFullName(String custodianFullName) {
        this.custodianFullName = custodianFullName;
    }

    public String getCustodianDesignation() {
        return custodianDesignation;
    }

    public void setCustodianDesignation(String custodianDesignation) {
        this.custodianDesignation = custodianDesignation;
    }

    public String getCustodianAddress() {
        return custodianAddress;
    }

    public void setCustodianAddress(String custodianAddress) {
        this.custodianAddress = custodianAddress;
    }

    public String getFinalGrading() {
        return finalGrading;
    }

    public void setFinalGrading(String finalGrading) {
        this.finalGrading = finalGrading;
    }

    public String getAppraisePostingDetail() {
        return appraisePostingDetail;
    }

    public void setAppraisePostingDetail(String appraisePostingDetail) {
        this.appraisePostingDetail = appraisePostingDetail;
    }

    public String getWebAddress() {
        return webAddress;
    }

    public void setWebAddress(String webAddress) {
        this.webAddress = webAddress;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getWebSite() {
        return webSite;
    }

    public void setWebSite(String webSite) {
        this.webSite = webSite;
    }

    public String getCustodianEmpId() {
        return custodianEmpId;
    }

    public void setCustodianEmpId(String custodianEmpId) {
        this.custodianEmpId = custodianEmpId;
    }

    public int getDetailIdForCustodianEntry() {
        return detailIdForCustodianEntry;
    }

    public void setDetailIdForCustodianEntry(int detailIdForCustodianEntry) {
        this.detailIdForCustodianEntry = detailIdForCustodianEntry;
    }

    public String getTelFaxNo() {
        return telFaxNo;
    }

    public void setTelFaxNo(String telFaxNo) {
        this.telFaxNo = telFaxNo;
    }

    public String getCustodianLoginId() {
        return custodianLoginId;
    }

    public void setCustodianLoginId(String custodianLoginId) {
        this.custodianLoginId = custodianLoginId;
    }

    public String getOpLogoFilePath() {
        return opLogoFilePath;
    }

    public void setOpLogoFilePath(String opLogoFilePath) {
        this.opLogoFilePath = opLogoFilePath;
    }
    
    

}
