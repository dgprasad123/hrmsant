/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.model.Rent;

import java.util.List;

/**
 *
 * @author Manoj PC
 */
public class ScrollData {
    private String departmentRefId = "";
    private String challanRefId = "";
    private String totalAmt = "";
    private String bankTransId = "";
    private String bankTransStatus = "";
    private String bankTransTime = "";
    private List<ChallanDtls> challanDtls;
    private List agencyDtls = null;

    public String getDepartmentRefId() {
        return departmentRefId;
    }

    public void setDepartmentRefId(String departmentRefId) {
        this.departmentRefId = departmentRefId;
    }

    public String getChallanRefId() {
        return challanRefId;
    }

    public void setChallanRefId(String challanRefId) {
        this.challanRefId = challanRefId;
    }

    public String getTotalAmt() {
        return totalAmt;
    }

    public void setTotalAmt(String totalAmt) {
        this.totalAmt = totalAmt;
    }

    public String getBankTransId() {
        return bankTransId;
    }

    public void setBankTransId(String bankTransId) {
        this.bankTransId = bankTransId;
    }

    public String getBankTransStatus() {
        return bankTransStatus;
    }

    public void setBankTransStatus(String bankTransStatus) {
        this.bankTransStatus = bankTransStatus;
    }

    public String getBankTransTime() {
        return bankTransTime;
    }

    public void setBankTransTime(String bankTransTime) {
        this.bankTransTime = bankTransTime;
    }

    public List<ChallanDtls> getChallanDtls() {
        return challanDtls;
    }

    public void setChallanDtls(List<ChallanDtls> challanDtls) {
        this.challanDtls = challanDtls;
    }

    public List getAgencyDtls() {
        return agencyDtls;
    }

    public void setAgencyDtls(List agencyDtls) {
        this.agencyDtls = agencyDtls;
    }



    
    
}
