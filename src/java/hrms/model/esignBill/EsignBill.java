/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.esignBill;

/**
 *
 * @author lenovo
 */
public class EsignBill {

    private String responseData;
    private int status;
    private String errorMessage;
    private String version;
    private String errorCode;
    private String encryptionKeyID;
    private String encryptedData;
    private String stoteId;
    private String displayName;
    private String tokenSize;
    private String certId;
    private String tempPath;
    private int esignLogId;
    private String unsignedpath;
    private String unsignedFile;
    private String billNo;
    private String signedStatus;

    public String getResponseData() {
        return responseData;
    }

    public void setResponseData(String responseData) {
        this.responseData = responseData;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getEncryptionKeyID() {
        return encryptionKeyID;
    }

    public void setEncryptionKeyID(String encryptionKeyID) {
        this.encryptionKeyID = encryptionKeyID;
    }

    public String getEncryptedData() {
        return encryptedData;
    }

    public void setEncryptedData(String encryptedData) {
        this.encryptedData = encryptedData;
    }

    public String getStoteId() {
        return stoteId;
    }

    public void setStoteId(String stoteId) {
        this.stoteId = stoteId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getTokenSize() {
        return tokenSize;
    }

    public void setTokenSize(String tokenSize) {
        this.tokenSize = tokenSize;
    }

    public String getCertId() {
        return certId;
    }

    public void setCertId(String certId) {
        this.certId = certId;
    }

    public String getTempPath() {
        return tempPath;
    }
    public void setTempPath(String tempPath) {
        this.tempPath = tempPath;
    }

    public int getEsignLogId() {
        return esignLogId;
    }

    public void setEsignLogId(int esignLogId) {
        this.esignLogId = esignLogId;
    }

    public String getUnsignedpath() {
        return unsignedpath;
    }

    public void setUnsignedpath(String unsignedpath) {
        this.unsignedpath = unsignedpath;
    }

    public String getUnsignedFile() {
        return unsignedFile;
    }

    public void setUnsignedFile(String unsignedFile) {
        this.unsignedFile = unsignedFile;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getSignedStatus() {
        return signedStatus;
    }

    public void setSignedStatus(String signedStatus) {
        this.signedStatus = signedStatus;
    }
}
