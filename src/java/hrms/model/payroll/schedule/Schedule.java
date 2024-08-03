/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.payroll.schedule;

import java.util.List;

public class Schedule {

    private String recMonth;
    private String recYear;
    private String totFig;
    private int slno;
    private String reportName;
    private List scheduleList = null;
    private String listSize = "";
    private List scheduleList1 = null;
    private String scheduleName;
    private String objectHead;
    private String schAmount;
    private String scheduleDesc = null;
    private String demandNo = null;
    private int billno ;
    private double alowanceTotal;
    private String nowDeduct=null;
     
     private String itObjHead=null;
     private String cpfObjHead=null;
     
     private String acctType=null;
     private String ptObjHead=null;
      private String itAmt=null;
     private String cpfAmt=null;
     private String ptAmt=null;

   private String orObjHead=null;
   private String drObjHead=null;
   private String orAmt=null;
   private String drAmt=null;

    public String getItObjHead() {
        return itObjHead;
    }

    public void setItObjHead(String itObjHead) {
        this.itObjHead = itObjHead;
    }

    public String getCpfObjHead() {
        return cpfObjHead;
    }

    public void setCpfObjHead(String cpfObjHead) {
        this.cpfObjHead = cpfObjHead;
    }

    public String getPtObjHead() {
        return ptObjHead;
    }

    public void setPtObjHead(String ptObjHead) {
        this.ptObjHead = ptObjHead;
    }

    public String getItAmt() {
        return itAmt;
    }

    public void setItAmt(String itAmt) {
        this.itAmt = itAmt;
    }

    public String getCpfAmt() {
        return cpfAmt;
    }

    public void setCpfAmt(String cpfAmt) {
        this.cpfAmt = cpfAmt;
    }

    public String getPtAmt() {
        return ptAmt;
    }

    public void setPtAmt(String ptAmt) {
        this.ptAmt = ptAmt;
    }
            
    public String getNowDeduct() {
        return nowDeduct;
    }

    public void setNowDeduct(String nowDeduct) {
        this.nowDeduct = nowDeduct;
    }

   
    

    public double getAlowanceTotal() {
        return alowanceTotal;
    }

    public void setAlowanceTotal(double alowanceTotal) {
        this.alowanceTotal = alowanceTotal;
    }
    
    public int getBillno() {
        return billno;
    }

    public void setBillno(int billno) {
        this.billno = billno;
    }

   

    public String getScheduleDesc() {
        return scheduleDesc;
    }

    public void setScheduleDesc(String scheduleDesc) {
        this.scheduleDesc = scheduleDesc;
    }

    public String getDemandNo() {
        return demandNo;
    }

    public void setDemandNo(String demandNo) {
        this.demandNo = demandNo;
    }

    public String getScheduleName() {
        return scheduleName;
    }

    public void setScheduleName(String scheduleName) {
        this.scheduleName = scheduleName;
    }

    public String getObjectHead() {
        return objectHead;
    }

    public void setObjectHead(String objectHead) {
        this.objectHead = objectHead;
    }

    public String getSchAmount() {
        return schAmount;
    }

    public void setSchAmount(String schAmount) {
        this.schAmount = schAmount;
    }

    public String getRecMonth() {
        return recMonth;
    }

    public void setRecMonth(String recMonth) {
        this.recMonth = recMonth;
    }

    public String getRecYear() {
        return recYear;
    }

    public void setRecYear(String recYear) {
        this.recYear = recYear;
    }

    public String getTotFig() {
        return totFig;
    }

    public void setTotFig(String totFig) {
        this.totFig = totFig;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public int getSlno() {
        return slno;
    }

    public void setSlno(int slno) {
        this.slno = slno;
    }

    public String getListSize() {
        return listSize;
    }

    public void setListSize(String listSize) {
        this.listSize = listSize;
    }

    public List getScheduleList() {
        return scheduleList;
    }

    public void setScheduleList(List scheduleList) {
        this.scheduleList = scheduleList;
    }

    public List getScheduleList1() {
        return scheduleList1;
    }

    public void setScheduleList1(List scheduleList1) {
        this.scheduleList1 = scheduleList1;
    }

    public void addSchAmount(int amt) {
        this.schAmount = "0";
        this.schAmount = (Integer.parseInt(this.schAmount) + amt) + "";
    }

    public String getAcctType() {
        return acctType;
    }

    public void setAcctType(String acctType) {
        this.acctType = acctType;
    }

    public String getOrObjHead() {
        return orObjHead;
    }

    public void setOrObjHead(String orObjHead) {
        this.orObjHead = orObjHead;
    }

    public String getOrAmt() {
        return orAmt;
    }

    public void setOrAmt(String orAmt) {
        this.orAmt = orAmt;
    }

    public String getDrAmt() {
        return drAmt;
    }

    public void setDrAmt(String drAmt) {
        this.drAmt = drAmt;
    }

    public String getDrObjHead() {
        return drObjHead;
    }

    public void setDrObjHead(String drObjHead) {
        this.drObjHead = drObjHead;
    }
    

}
