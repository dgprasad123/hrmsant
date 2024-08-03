/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.lic;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author Manas Jena
 */
public class Lic {

    private int slno;
    private BigDecimal elId;
    private String empid = null;
    private String policyNo = null;
    private int subAmount;
    private String wef = null;
    private String note = null;
    private String trDataType = null;
    private String status = null;
    private String btid;
    private String month;
    private String year;

    public String getBtid() {
        return btid;
    }

    public void setBtid(String btid) {
        this.btid = btid;
    }

    private String insuranceType = null;

    public BigDecimal getElId() {
        return elId;
    }

    public void setElId(BigDecimal elId) {
        this.elId = elId;
    }

    public String getEmpid() {
        return empid;
    }

    public void setEmpid(String empid) {
        this.empid = empid;
    }

    public String getPolicyNo() {
        return policyNo;
    }

    public void setPolicyNo(String policyNo) {
        this.policyNo = policyNo;
    }

    public int getSubAmount() {
        return subAmount;
    }

    public void setSubAmount(int subAmount) {
        this.subAmount = subAmount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getTrDataType() {
        return trDataType;
    }

    public void setTrDataType(String trDataType) {
        this.trDataType = trDataType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInsuranceType() {
        return insuranceType;
    }

    public void setInsuranceType(String insuranceType) {
        this.insuranceType = insuranceType;
    }

    public int getSlno() {
        return slno;
    }

    public void setSlno(int slno) {
        this.slno = slno;
    }

    public void setYear(int Year) {
        this.year = year;
    }

    public String getWef() {
        return wef;
    }

    public void setWef(String wef) {
        this.wef = wef;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

}
