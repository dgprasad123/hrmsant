/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.itdeclaration;

/**
 *
 * @author Surendra
 */
public class InvestmentDeclaration {
    
    private String hidEmpId;
    private String txtEmployeeName;
    private String txtAddress;
    private String txtPan;
    private String txtFy;
    
    private String txtRentPaid;
    private String txtLandLordName;
    private String txtLandLordAddress;
    private String txtLandLordPan;
    private String txtLTC;
    private String txtEvidence;
    
    private String hidSubmitDeptCode;
    private String hidSubmitOffCode;
    
    public String getTxtEmployeeName() {
        return txtEmployeeName;
    }

    public void setTxtEmployeeName(String txtEmployeeName) {
        this.txtEmployeeName = txtEmployeeName;
    }

    public String getTxtAddress() {
        return txtAddress;
    }

    public void setTxtAddress(String txtAddress) {
        this.txtAddress = txtAddress;
    }

    public String getTxtPan() {
        return txtPan;
    }

    public void setTxtPan(String txtPan) {
        this.txtPan = txtPan;
    }

    public String getTxtFy() {
        return txtFy;
    }

    public void setTxtFy(String txtFy) {
        this.txtFy = txtFy;
    }

    public String getHidEmpId() {
        return hidEmpId;
    }

    public void setHidEmpId(String hidEmpId) {
        this.hidEmpId = hidEmpId;
    }

    public String getTxtRentPaid() {
        return txtRentPaid;
    }

    public void setTxtRentPaid(String txtRentPaid) {
        this.txtRentPaid = txtRentPaid;
    }

    public String getTxtLandLordName() {
        return txtLandLordName;
    }

    public void setTxtLandLordName(String txtLandLordName) {
        this.txtLandLordName = txtLandLordName;
    }

    public String getTxtLandLordAddress() {
        return txtLandLordAddress;
    }

    public void setTxtLandLordAddress(String txtLandLordAddress) {
        this.txtLandLordAddress = txtLandLordAddress;
    }

    public String getTxtLandLordPan() {
        return txtLandLordPan;
    }

    public void setTxtLandLordPan(String txtLandLordPan) {
        this.txtLandLordPan = txtLandLordPan;
    }

    public String getTxtLTC() {
        return txtLTC;
    }

    public void setTxtLTC(String txtLTC) {
        this.txtLTC = txtLTC;
    }

    public String getTxtEvidence() {
        return txtEvidence;
    }

    public void setTxtEvidence(String txtEvidence) {
        this.txtEvidence = txtEvidence;
    }

    public String getHidSubmitDeptCode() {
        return hidSubmitDeptCode;
    }

    public void setHidSubmitDeptCode(String hidSubmitDeptCode) {
        this.hidSubmitDeptCode = hidSubmitDeptCode;
    }

    public String getHidSubmitOffCode() {
        return hidSubmitOffCode;
    }

    public void setHidSubmitOffCode(String hidSubmitOffCode) {
        this.hidSubmitOffCode = hidSubmitOffCode;
    }
}
