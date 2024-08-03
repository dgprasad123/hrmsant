/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.EmpQuarterAllotment;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 *
 * @author Manas
 */
public class FundSanctionOrderBean {

    private int fundid;
    private int phslno;
    private String empid;
    private String empname;
    private String mobileno;
    private int allotmentOrderId;
    private String orderNumber = null;
    private String orderDate = null;
    private String typeofWork = null;
    private String financialYear = null;
    private String qrtrunit = null;
    private String qrtrtype = null;
    private String quarterNo = null;
    private int sanctionamt = 0;
    private String smsstring = null;
    private String smssend = null;
    private String smsDeliveryStatus = null;

    public int getFundid() {
        return fundid;
    }

    public void setFundid(int fundid) {
        this.fundid = fundid;
    }

    public int getPhslno() {
        return phslno;
    }

    public void setPhslno(int phslno) {
        this.phslno = phslno;
    }

    public String getEmpid() {
        return empid;
    }

    public void setEmpid(String empid) {
        this.empid = empid;
    }

    public String getEmpname() {
        return empname;
    }

    public void setEmpname(String empname) {
        this.empname = empname;
    }

    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }

    public int getAllotmentOrderId() {
        return allotmentOrderId;
    }

    public void setAllotmentOrderId(int allotmentOrderId) {
        this.allotmentOrderId = allotmentOrderId;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getTypeofWork() {
        return typeofWork;
    }

    public void setTypeofWork(String typeofWork) {
        this.typeofWork = typeofWork;
    }

    public String getFinancialYear() {
        return financialYear;
    }

    public void setFinancialYear(String financialYear) {
        this.financialYear = financialYear;
    }

    public String getQrtrunit() {
        return qrtrunit;
    }

    public void setQrtrunit(String qrtrunit) {
        this.qrtrunit = qrtrunit;
    }

    public String getQrtrtype() {
        return qrtrtype;
    }

    public void setQrtrtype(String qrtrtype) {
        this.qrtrtype = qrtrtype;
    }

    public String getQuarterNo() {
        return quarterNo;
    }

    public void setQuarterNo(String quarterNo) {
        this.quarterNo = quarterNo;
    }

    public int getSanctionamt() {
        return sanctionamt;
    }

    public void setSanctionamt(int sanctionamt) {
        this.sanctionamt = sanctionamt;
    }

    public String getSmsstring() {
        return smsstring;
    }

    public void setSmsstring(String smsstring) {
        this.smsstring = smsstring;
    }

    public String getSmssend() {
        return smssend;
    }

    public void setSmssend(String smssend) {
        this.smssend = smssend;
    }

    public String getSmsDeliveryStatus() {
        return smsDeliveryStatus;
    }

    public void setSmsDeliveryStatus(String smsDeliveryStatus) {
        this.smsDeliveryStatus = smsDeliveryStatus;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
