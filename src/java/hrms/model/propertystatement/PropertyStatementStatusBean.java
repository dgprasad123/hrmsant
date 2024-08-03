/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.propertystatement;

import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Manisha
 */
public class PropertyStatementStatusBean {

    private int logId;
    private String isClosedProperty;
    private MultipartFile propertyStatusdocument;
    private String originalFileNameForpropertyStatus = "";
    private String diskfileNameForpropertyStatus = "";
    private String fileTypeForpropertyStatus = "";
    private String fiscalyear;
    private String propertyPeriodExtendedFor;
    private String propertyStatusChangeOnDate;
    private byte[] filecontent = null;

    public int getLogId() {
        return logId;
    }

    public void setLogId(int logId) {
        this.logId = logId;
    }

    public String getIsClosedProperty() {
        return isClosedProperty;
    }

    public void setIsClosedProperty(String isClosedProperty) {
        this.isClosedProperty = isClosedProperty;
    }

    public MultipartFile getPropertyStatusdocument() {
        return propertyStatusdocument;
    }

    public void setPropertyStatusdocument(MultipartFile propertyStatusdocument) {
        this.propertyStatusdocument = propertyStatusdocument;
    }

    public String getOriginalFileNameForpropertyStatus() {
        return originalFileNameForpropertyStatus;
    }

    public void setOriginalFileNameForpropertyStatus(String originalFileNameForpropertyStatus) {
        this.originalFileNameForpropertyStatus = originalFileNameForpropertyStatus;
    }

    public String getDiskfileNameForpropertyStatus() {
        return diskfileNameForpropertyStatus;
    }

    public void setDiskfileNameForpropertyStatus(String diskfileNameForpropertyStatus) {
        this.diskfileNameForpropertyStatus = diskfileNameForpropertyStatus;
    }

    public String getFileTypeForpropertyStatus() {
        return fileTypeForpropertyStatus;
    }

    public void setFileTypeForpropertyStatus(String fileTypeForpropertyStatus) {
        this.fileTypeForpropertyStatus = fileTypeForpropertyStatus;
    }

    public String getFiscalyear() {
        return fiscalyear;
    }

    public void setFiscalyear(String fiscalyear) {
        this.fiscalyear = fiscalyear;
    }

    public String getPropertyPeriodExtendedFor() {
        return propertyPeriodExtendedFor;
    }

    public void setPropertyPeriodExtendedFor(String propertyPeriodExtendedFor) {
        this.propertyPeriodExtendedFor = propertyPeriodExtendedFor;
    }

    public String getPropertyStatusChangeOnDate() {
        return propertyStatusChangeOnDate;
    }

    public void setPropertyStatusChangeOnDate(String propertyStatusChangeOnDate) {
        this.propertyStatusChangeOnDate = propertyStatusChangeOnDate;
    }

   

    public byte[] getFilecontent() {
        return filecontent;
    }

    public void setFilecontent(byte[] filecontent) {
        this.filecontent = filecontent;
    }
    
    
}
