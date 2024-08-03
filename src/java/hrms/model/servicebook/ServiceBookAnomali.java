/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.servicebook;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Manas
 */
public class ServiceBookAnomali {

    private int anomaliId;
    private String empId;
    private String empname;
    private String servicePeriodFrom;
    private String servicePeriodTo;
    private String postingdeails;
    private String anomaliType;
    private String furtherDesc;
    private MultipartFile supportingDocument;
    private String originalFileName;
    private String fileType;
    private String status;

    public int getAnomaliId() {
        return anomaliId;
    }

    public void setAnomaliId(int anomaliId) {
        this.anomaliId = anomaliId;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getEmpname() {
        return empname;
    }

    public void setEmpname(String empname) {
        this.empname = empname;
    }

    public String getServicePeriodFrom() {
        return servicePeriodFrom;
    }

    public void setServicePeriodFrom(String servicePeriodFrom) {
        this.servicePeriodFrom = servicePeriodFrom;
    }

    public String getServicePeriodTo() {
        return servicePeriodTo;
    }

    public void setServicePeriodTo(String servicePeriodTo) {
        this.servicePeriodTo = servicePeriodTo;
    }

    public String getPostingdeails() {
        return postingdeails;
    }

    public void setPostingdeails(String postingdeails) {
        this.postingdeails = postingdeails;
    }

    public String getAnomaliType() {
        return anomaliType;
    }

    public void setAnomaliType(String anomaliType) {
        this.anomaliType = anomaliType;
    }

    public String getFurtherDesc() {
        return furtherDesc;
    }

    public void setFurtherDesc(String furtherDesc) {
        this.furtherDesc = furtherDesc;
    }

    public MultipartFile getSupportingDocument() {
        return supportingDocument;
    }

    public void setSupportingDocument(MultipartFile supportingDocument) {
        this.supportingDocument = supportingDocument;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
