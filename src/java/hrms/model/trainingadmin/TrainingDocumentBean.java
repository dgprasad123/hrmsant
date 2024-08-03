/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.model.trainingadmin;

import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Manoj PC
 */
public class TrainingDocumentBean {
    private String trainingProgramId;
    private String chkComplete;
    private MultipartFile certificateFile;
    private MultipartFile receiptFile;
    private MultipartFile intimationFile;
    private MultipartFile bankFile;

    public String getChkComplete() {
        return chkComplete;
    }

    public void setChkComplete(String chkComplete) {
        this.chkComplete = chkComplete;
    }

    public MultipartFile getCertificateFile() {
        return certificateFile;
    }

    public void setCertificateFile(MultipartFile certificateFile) {
        this.certificateFile = certificateFile;
    }

    public MultipartFile getReceiptFile() {
        return receiptFile;
    }

    public void setReceiptFile(MultipartFile receiptFile) {
        this.receiptFile = receiptFile;
    }

    public String getTrainingProgramId() {
        return trainingProgramId;
    }

    public void setTrainingProgramId(String trainingProgramId) {
        this.trainingProgramId = trainingProgramId;
    }

    public MultipartFile getIntimationFile() {
        return intimationFile;
    }

    public void setIntimationFile(MultipartFile intimationFile) {
        this.intimationFile = intimationFile;
    }

    public MultipartFile getBankFile() {
        return bankFile;
    }

    public void setBankFile(MultipartFile bankFile) {
        this.bankFile = bankFile;
    }
    
    
}
