/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.report.gispassbook;

import java.util.Date;

/**
 *
 * @author lenovo
 */
public class gisPassbook {

    private int slNo;
    private String empId = null;
    private String instru = null;
    private String vchno = null;
    private String dod = null;
    private double amoutDeposit;
    private String depositBy = null;
    private String trName = null;
    private String empName = null;
    private String gfpNo = null;
    private String currentStatus = null;
    private String curPost = null;
    private String prevPost = null;
    private int currentSal = 0;
    private int prevSal = 0;
    private String curMonth = null;
    private String joinDate="";
    private String gisNo=null;
    

    public int getSlNo() {
        return slNo;
    }

    public void setSlNo(int slNo) {
        this.slNo = slNo;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getInstru() {
        return instru;
    }

    public void setInstru(String instru) {
        this.instru = instru;
    }

    public String getVchno() {
        return vchno;
    }

    public void setVchno(String vchno) {
        this.vchno = vchno;
    }

    public double getAmoutDeposit() {
        return amoutDeposit;
    }

    public void setAmoutDeposit(double amoutDeposit) {
        this.amoutDeposit = amoutDeposit;
    }

    public String getDepositBy() {
        return depositBy;
    }

    public void setDepositBy(String depositBy) {
        this.depositBy = depositBy;
    }

    public String getDod() {
        return dod;
    }

    public void setDod(String dod) {
        this.dod = dod;
    }

    public String getTrName() {
        return trName;
    }

    public void setTrName(String trName) {
        this.trName = trName;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getGfpNo() {
        return gfpNo;
    }

    public void setGfpNo(String gfpNo) {
        this.gfpNo = gfpNo;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }

    public String getCurPost() {
        return curPost;
    }

    public void setCurPost(String curPost) {
        this.curPost = curPost;
    }

    public String getPrevPost() {
        return prevPost;
    }

    public void setPrevPost(String prevPost) {
        this.prevPost = prevPost;
    }

    public int getCurrentSal() {
        return currentSal;
    }

    public void setCurrentSal(int currentSal) {
        this.currentSal = currentSal;
    }

    public int getPrevSal() {
        return prevSal;
    }

    public void setPrevSal(int prevSal) {
        this.prevSal = prevSal;
    }

    public String getCurMonth() {
        return curMonth;
    }

    public void setCurMonth(String curMonth) {
        this.curMonth = curMonth;
    }

    public String getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(String joinDate) {
        this.joinDate = joinDate;
    }

    public String getGisNo() {
        return gisNo;
    }

    public void setGisNo(String gisNo) {
        this.gisNo = gisNo;
    }
    

}
