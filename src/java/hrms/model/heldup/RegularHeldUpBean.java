/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.heldup;

import java.math.BigDecimal;

/**
 *
 * @author Manas
 */
public class RegularHeldUpBean {

    private int year = 0;
    private int month = 0;
    private BigDecimal billgroupid;
    private String offcode = null;
    private String slno = null;
    private String empname = null;
    private String post = null;
    private String status = null;
    private String hrmsId = null;
    private String gpfNo = null;
    private String heldupStatus = null;
    private String curspc = null;
    private BigDecimal heldupId = null;
    private String empIdSPC = null;    
    private String heldupdate = null;

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public BigDecimal getBillgroupid() {
        return billgroupid;
    }

    public void setBillgroupid(BigDecimal billgroupid) {
        this.billgroupid = billgroupid;
    }

    public String getOffcode() {
        return offcode;
    }

    public void setOffcode(String offcode) {
        this.offcode = offcode;
    }

    public String getSlno() {
        return slno;
    }

    public void setSlno(String slno) {
        this.slno = slno;
    }

    public String getEmpname() {
        return empname;
    }

    public void setEmpname(String empname) {
        this.empname = empname;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getHrmsId() {
        return hrmsId;
    }

    public void setHrmsId(String hrmsId) {
        this.hrmsId = hrmsId;
    }

    public String getGpfNo() {
        return gpfNo;
    }

    public void setGpfNo(String gpfNo) {
        this.gpfNo = gpfNo;
    }

    public String getHeldupStatus() {
        return heldupStatus;
    }

    public void setHeldupStatus(String heldupStatus) {
        this.heldupStatus = heldupStatus;
    }

    public String getCurspc() {
        return curspc;
    }

    public void setCurspc(String curspc) {
        this.curspc = curspc;
    }

    public BigDecimal getHeldupId() {
        return heldupId;
    }

    public void setHeldupId(BigDecimal heldupId) {
        this.heldupId = heldupId;
    }

    public String getEmpIdSPC() {
        return empIdSPC;
    }

    public void setEmpIdSPC(String empIdSPC) {
        this.empIdSPC = empIdSPC;
    }    

    public String getHeldupdate() {
        return heldupdate;
    }

    public void setHeldupdate(String heldupdate) {
        this.heldupdate = heldupdate;
    }

}
