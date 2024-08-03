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
public class UploadPreviousPAR {

    private String fiscalyear;
    private int parId;
    private String empId = null;
    private String empName = null;
    private String postName = null;
    private String cadreName = null;
    private String postGroupType;
    private String designationDuringPeriod;
    private String PrdFrmDate;
    private String PrdToDate;
    private MultipartFile previousYearPARDocument;
    private String originalFileNameForpreviousYearPAR = "";
    private String diskfileNameForpreviousYearPAR = "";
    private String fileTypeForpreviousYearPAR = "";
    private String finalGradingForPreviousYrPAR;
    private String finalGradingNameForPreviousYrPAR;
    private String previousyrParUploadOn;
    private String previousyrParUploadedbyempId;
    private String previousyrParUploadedbyspc;
    private String previousyrParUploadedbypost;
    private String previousyrParUploadedbyempName;
    private byte[] filecontent = null;
    private String getContentType;
    private String deptcode;
    private String hidcadrename;
    private String hidParId;
    private String hidspc;
    private String parTypeForPreviousYearPAR;
    private String nrcReasonForPreviousYearPAR;
    
   //PROPERTIES FOR SI PAR WORK
    private String parType;
    private String encParId;
    private String encEmpId = null;
    private String gpfNo;
    private String hidFiscalYear;
    private String placeOfPostingDuringPeriod;
    private String subInspectorType;
    

    public String getFiscalyear() {
        return fiscalyear;
    }

    public void setFiscalyear(String fiscalyear) {
        this.fiscalyear = fiscalyear;
    }

    public int getParId() {
        return parId;
    }

    public void setParId(int parId) {
        this.parId = parId;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getPostName() {
        return postName;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }

    public String getCadreName() {
        return cadreName;
    }

    public void setCadreName(String cadreName) {
        this.cadreName = cadreName;
    }

    public String getPostGroupType() {
        return postGroupType;
    }

    public void setPostGroupType(String postGroupType) {
        this.postGroupType = postGroupType;
    }

    public String getDesignationDuringPeriod() {
        return designationDuringPeriod;
    }

    public void setDesignationDuringPeriod(String designationDuringPeriod) {
        this.designationDuringPeriod = designationDuringPeriod;
    }

    public String getPrdFrmDate() {
        return PrdFrmDate;
    }

    public void setPrdFrmDate(String PrdFrmDate) {
        this.PrdFrmDate = PrdFrmDate;
    }

    public String getPrdToDate() {
        return PrdToDate;
    }

    public void setPrdToDate(String PrdToDate) {
        this.PrdToDate = PrdToDate;
    }

    public MultipartFile getPreviousYearPARDocument() {
        return previousYearPARDocument;
    }

    public void setPreviousYearPARDocument(MultipartFile previousYearPARDocument) {
        this.previousYearPARDocument = previousYearPARDocument;
    }

    public String getOriginalFileNameForpreviousYearPAR() {
        return originalFileNameForpreviousYearPAR;
    }

    public void setOriginalFileNameForpreviousYearPAR(String originalFileNameForpreviousYearPAR) {
        this.originalFileNameForpreviousYearPAR = originalFileNameForpreviousYearPAR;
    }

    public String getDiskfileNameForpreviousYearPAR() {
        return diskfileNameForpreviousYearPAR;
    }

    public void setDiskfileNameForpreviousYearPAR(String diskfileNameForpreviousYearPAR) {
        this.diskfileNameForpreviousYearPAR = diskfileNameForpreviousYearPAR;
    }

    public String getFileTypeForpreviousYearPAR() {
        return fileTypeForpreviousYearPAR;
    }

    public void setFileTypeForpreviousYearPAR(String fileTypeForpreviousYearPAR) {
        this.fileTypeForpreviousYearPAR = fileTypeForpreviousYearPAR;
    }

    public String getFinalGradingForPreviousYrPAR() {
        return finalGradingForPreviousYrPAR;
    }

    public void setFinalGradingForPreviousYrPAR(String finalGradingForPreviousYrPAR) {
        this.finalGradingForPreviousYrPAR = finalGradingForPreviousYrPAR;
    }

    public String getFinalGradingNameForPreviousYrPAR() {
        return finalGradingNameForPreviousYrPAR;
    }

    public void setFinalGradingNameForPreviousYrPAR(String finalGradingNameForPreviousYrPAR) {
        this.finalGradingNameForPreviousYrPAR = finalGradingNameForPreviousYrPAR;
    }

    public String getPreviousyrParUploadOn() {
        return previousyrParUploadOn;
    }

    public void setPreviousyrParUploadOn(String previousyrParUploadOn) {
        this.previousyrParUploadOn = previousyrParUploadOn;
    }

    public String getPreviousyrParUploadedbyempId() {
        return previousyrParUploadedbyempId;
    }

    public void setPreviousyrParUploadedbyempId(String previousyrParUploadedbyempId) {
        this.previousyrParUploadedbyempId = previousyrParUploadedbyempId;
    }

    public String getPreviousyrParUploadedbyspc() {
        return previousyrParUploadedbyspc;
    }

    public void setPreviousyrParUploadedbyspc(String previousyrParUploadedbyspc) {
        this.previousyrParUploadedbyspc = previousyrParUploadedbyspc;
    }

    public String getPreviousyrParUploadedbypost() {
        return previousyrParUploadedbypost;
    }

    public void setPreviousyrParUploadedbypost(String previousyrParUploadedbypost) {
        this.previousyrParUploadedbypost = previousyrParUploadedbypost;
    }

    public String getPreviousyrParUploadedbyempName() {
        return previousyrParUploadedbyempName;
    }

    public void setPreviousyrParUploadedbyempName(String previousyrParUploadedbyempName) {
        this.previousyrParUploadedbyempName = previousyrParUploadedbyempName;
    }

    public byte[] getFilecontent() {
        return filecontent;
    }

    public void setFilecontent(byte[] filecontent) {
        this.filecontent = filecontent;
    }

    public String getGetContentType() {
        return getContentType;
    }

    public void setGetContentType(String getContentType) {
        this.getContentType = getContentType;
    }

    public String getDeptcode() {
        return deptcode;
    }

    public void setDeptcode(String deptcode) {
        this.deptcode = deptcode;
    }

    public String getHidcadrename() {
        return hidcadrename;
    }

    public void setHidcadrename(String hidcadrename) {
        this.hidcadrename = hidcadrename;
    }

    public String getHidParId() {
        return hidParId;
    }

    public void setHidParId(String hidParId) {
        this.hidParId = hidParId;
    }

    public String getHidspc() {
        return hidspc;
    }

    public void setHidspc(String hidspc) {
        this.hidspc = hidspc;
    }

    public String getParTypeForPreviousYearPAR() {
        return parTypeForPreviousYearPAR;
    }

    public void setParTypeForPreviousYearPAR(String parTypeForPreviousYearPAR) {
        this.parTypeForPreviousYearPAR = parTypeForPreviousYearPAR;
    }

    public String getNrcReasonForPreviousYearPAR() {
        return nrcReasonForPreviousYearPAR;
    }

    public void setNrcReasonForPreviousYearPAR(String nrcReasonForPreviousYearPAR) {
        this.nrcReasonForPreviousYearPAR = nrcReasonForPreviousYearPAR;
    }

    public String getEncParId() {
        return encParId;
    }

    public void setEncParId(String encParId) {
        this.encParId = encParId;
    }

    public String getEncEmpId() {
        return encEmpId;
    }

    public void setEncEmpId(String encEmpId) {
        this.encEmpId = encEmpId;
    }

    public String getPlaceOfPostingDuringPeriod() {
        return placeOfPostingDuringPeriod;
    }

    public void setPlaceOfPostingDuringPeriod(String placeOfPostingDuringPeriod) {
        this.placeOfPostingDuringPeriod = placeOfPostingDuringPeriod;
    }

    public String getSubInspectorType() {
        return subInspectorType;
    }

    public void setSubInspectorType(String subInspectorType) {
        this.subInspectorType = subInspectorType;
    }

    public String getHidFiscalYear() {
        return hidFiscalYear;
    }

    public void setHidFiscalYear(String hidFiscalYear) {
        this.hidFiscalYear = hidFiscalYear;
    }

    public String getParType() {
        return parType;
    }

    public void setParType(String parType) {
        this.parType = parType;
    }

    public String getGpfNo() {
        return gpfNo;
    }

    public void setGpfNo(String gpfNo) {
        this.gpfNo = gpfNo;
    }
    
    

}
