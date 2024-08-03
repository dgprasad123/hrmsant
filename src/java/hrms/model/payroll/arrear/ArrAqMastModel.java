/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.payroll.arrear;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Manas
 */
public class ArrAqMastModel {

    
    private int slno;
    private int billNo;
    private String aqSlNo = null;
    private BigDecimal aqGroup;
    private String aqGroupDesc = null;
    private String empCode = null;
    private String empName = null;
    private String accType = null;
    private String gpfAccNo = null;
    private String gpfType = null;
    private String offCode = null;
    private String offDdo = null;
    private String curDesg = null;
    private String empType = null;
    private int payTotal;
    private int gpTotal;
    private int daTotal;
    private int hraTotal;
    private int oaTotal;
    private int irTotal;
    
    private int payMonth;
    private int payYear;
    private int payday;
    private Date choiceDate = null;
    private String inputChoiceDate;
    private int payrevisionbasic;
    private int payrevisiongp;
    private int curBasic;
    private int arrearpay;
    private int fullArrearpay;
    private String pt;
    private String arrtype;
    
    private String adType = null;
    private int duePayAmt;
    private int dueDaAmt;
    private int dueGpAmt;
    private int dueTotalAmt;
    
    private int drawnPayAmt;
    private int drawnDaAmt;
    private int drawnGpAmt;
    private int drawnTotalAmt;
    
    private int arrear100;
    private double arrear40;
    private double arrear60;
    
    private String incomeTaxAmt;
    private int professionalTax;
    private double grandTotNetPay;
    
    private String remark = null;
    private String drawnBillNo = null;
    public List arrDetails = null;
    
    private int grandTotArr100;
    private double grandTotArr40;
    private double grandTotArr60;

    private String billDesc = null;
    private String fromMonth = null;
    private String fromYear = null;
    private String toMonth = null;
    private String toYear = null;
    
    private String cpfHead = null;
    
    private int percentageArraer;
    
    private int billstatusid;
    private String billstatus;
    private String vchno;
    
    private String otherRecovery;
    private String ddoRecovery;
    
    public int getProfessionalTax() {
        return professionalTax;
    }

    public void setProfessionalTax(int professionalTax) {
        this.professionalTax = professionalTax;
    }

    public int getCurBasic() {
        return curBasic;
    }

    public void setCurBasic(int curBasic) {
        this.curBasic = curBasic;
    }

    public int getArrearpay() {
        return arrearpay;
    }

    public void setArrearpay(int arrearpay) {
        this.arrearpay = arrearpay;
    }

    public int getSlno() {
        return slno;
    }

    public void setSlno(int slno) {
        this.slno = slno;
    }

    public int getBillNo() {
        return billNo;
    }

    public void setBillNo(int billNo) {
        this.billNo = billNo;
    }

    public String getAqSlNo() {
        return aqSlNo;
    }

    public void setAqSlNo(String aqSlNo) {
        this.aqSlNo = aqSlNo;
    }

    public BigDecimal getAqGroup() {
        return aqGroup;
    }

    public void setAqGroup(BigDecimal aqGroup) {
        this.aqGroup = aqGroup;
    }

    public String getAqGroupDesc() {
        return aqGroupDesc;
    }

    public void setAqGroupDesc(String aqGroupDesc) {
        this.aqGroupDesc = aqGroupDesc;
    }

    public String getEmpCode() {
        return empCode;
    }

    public void setEmpCode(String empCode) {
        this.empCode = empCode;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getAccType() {
        return accType;
    }

    public void setAccType(String accType) {
        this.accType = accType;
    }

    public String getGpfAccNo() {
        return gpfAccNo;
    }

    public void setGpfAccNo(String gpfAccNo) {
        this.gpfAccNo = gpfAccNo;
    }

    public String getGpfType() {
        return gpfType;
    }

    public void setGpfType(String gpfType) {
        this.gpfType = gpfType;
    }

    public String getOffCode() {
        return offCode;
    }

    public void setOffCode(String offCode) {
        this.offCode = offCode;
    }

    public String getOffDdo() {
        return offDdo;
    }

    public void setOffDdo(String offDdo) {
        this.offDdo = offDdo;
    }

    public String getCurDesg() {
        return curDesg;
    }

    public void setCurDesg(String curDesg) {
        this.curDesg = curDesg;
    }    

    public String getEmpType() {
        return empType;
    }

    public void setEmpType(String empType) {
        this.empType = empType;
    }

    public int getPayTotal() {
        return payTotal;
    }

    public void setPayTotal(int payTotal) {
        this.payTotal = payTotal;
    }

    public int getGpTotal() {
        return gpTotal;
    }

    public void setGpTotal(int gpTotal) {
        this.gpTotal = gpTotal;
    }

    public int getDaTotal() {
        return daTotal;
    }

    public void setDaTotal(int daTotal) {
        this.daTotal = daTotal;
    }

    public int getHraTotal() {
        return hraTotal;
    }

    public void setHraTotal(int hraTotal) {
        this.hraTotal = hraTotal;
    }

    public int getOaTotal() {
        return oaTotal;
    }

    public void setOaTotal(int oaTotal) {
        this.oaTotal = oaTotal;
    }

    public String getArrtype() {
        return arrtype;
    }

    public void setArrtype(String arrtype) {
        this.arrtype = arrtype;
    }

    public Date getChoiceDate() {
        return choiceDate;
    }

    public void setChoiceDate(Date choiceDate) {
        this.choiceDate = choiceDate;
    }

    public String getInputChoiceDate() {
        return inputChoiceDate;
    }

    public void setInputChoiceDate(String inputChoiceDate) {
        this.inputChoiceDate = inputChoiceDate;
    }

    public int getPayrevisionbasic() {
        return payrevisionbasic;
    }

    public void setPayrevisionbasic(int payrevisionbasic) {
        this.payrevisionbasic = payrevisionbasic;
    }

    public int getPayrevisiongp() {
        return payrevisiongp;
    }

    public void setPayrevisiongp(int payrevisiongp) {
        this.payrevisiongp = payrevisiongp;
    }

    public int getPayMonth() {
        return payMonth;
    }

    public void setPayMonth(int payMonth) {
        this.payMonth = payMonth;
    }

    public int getPayYear() {
        return payYear;
    }

    public void setPayYear(int payYear) {
        this.payYear = payYear;
    }    

    public int getPayday() {
        return payday;
    }

    public void setPayday(int payday) {
        this.payday = payday;
    }

    public String getAdType() {
        return adType;
    }

    public void setAdType(String adType) {
        this.adType = adType;
    }

    public int getDuePayAmt() {
        return duePayAmt;
    }

    public void setDuePayAmt(int duePayAmt) {
        this.duePayAmt = duePayAmt;
    }

    public int getDueDaAmt() {
        return dueDaAmt;
    }

    public void setDueDaAmt(int dueDaAmt) {
        this.dueDaAmt = dueDaAmt;
    }

    public int getDueGpAmt() {
        return dueGpAmt;
    }

    public void setDueGpAmt(int dueGpAmt) {
        this.dueGpAmt = dueGpAmt;
    }

    public int getDueTotalAmt() {
        return dueTotalAmt;
    }

    public void setDueTotalAmt(int dueTotalAmt) {
        this.dueTotalAmt = dueTotalAmt;
    }

    public int getDrawnPayAmt() {
        return drawnPayAmt;
    }

    public void setDrawnPayAmt(int drawnPayAmt) {
        this.drawnPayAmt = drawnPayAmt;
    }

    public int getDrawnDaAmt() {
        return drawnDaAmt;
    }

    public void setDrawnDaAmt(int drawnDaAmt) {
        this.drawnDaAmt = drawnDaAmt;
    }

    public int getDrawnGpAmt() {
        return drawnGpAmt;
    }

    public void setDrawnGpAmt(int drawnGpAmt) {
        this.drawnGpAmt = drawnGpAmt;
    }

    public int getDrawnTotalAmt() {
        return drawnTotalAmt;
    }

    public void setDrawnTotalAmt(int drawnTotalAmt) {
        this.drawnTotalAmt = drawnTotalAmt;
    }

    public int getArrear100() {
        return arrear100;
    }

    public void setArrear100(int arrear100) {
        this.arrear100 = arrear100;
    }

    public double getArrear40() {
        return arrear40;
    }

    public void setArrear40(double arrear40) {
        this.arrear40 = arrear40;
    }

    public double getArrear60() {
        return arrear60;
    }

    public void setArrear60(double arrear60) {
        this.arrear60 = arrear60;
    }

    public String getIncomeTaxAmt() {
        return incomeTaxAmt;
    }

    public void setIncomeTaxAmt(String incomeTaxAmt) {
        this.incomeTaxAmt = incomeTaxAmt;
    }

    public double getGrandTotNetPay() {
        return grandTotNetPay;
    }

    public void setGrandTotNetPay(double grandTotNetPay) {
        this.grandTotNetPay = grandTotNetPay;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDrawnBillNo() {
        return drawnBillNo;
    }

    public void setDrawnBillNo(String drawnBillNo) {
        this.drawnBillNo = drawnBillNo;
    }

    public List getArrDetails() {
        return arrDetails;
    }

    public void setArrDetails(List arrDetails) {
        this.arrDetails = arrDetails;
    }

    public int getGrandTotArr100() {
        return grandTotArr100;
    }

    public void setGrandTotArr100(int grandTotArr100) {
        this.grandTotArr100 = grandTotArr100;
    }

    public double getGrandTotArr40() {
        return grandTotArr40;
    }

    public void setGrandTotArr40(double grandTotArr40) {
        this.grandTotArr40 = grandTotArr40;
    }

    public double getGrandTotArr60() {
        return grandTotArr60;
    }

    public void setGrandTotArr60(double grandTotArr60) {
        this.grandTotArr60 = grandTotArr60;
    }

    public String getBillDesc() {
        return billDesc;
    }

    public void setBillDesc(String billDesc) {
        this.billDesc = billDesc;
    }

    public String getFromMonth() {
        return fromMonth;
    }

    public void setFromMonth(String fromMonth) {
        this.fromMonth = fromMonth;
    }

    public String getFromYear() {
        return fromYear;
    }

    public void setFromYear(String fromYear) {
        this.fromYear = fromYear;
    }

    public String getToMonth() {
        return toMonth;
    }

    public void setToMonth(String toMonth) {
        this.toMonth = toMonth;
    }

    public String getToYear() {
        return toYear;
    }

    public void setToYear(String toYear) {
        this.toYear = toYear;
    }

    public String getCpfHead() {
        return cpfHead;
    }

    public void setCpfHead(String cpfHead) {
        this.cpfHead = cpfHead;
    }

    public String getPt() {
        return pt;
    }

    public void setPt(String pt) {
        this.pt = pt;
    }

    public int getPercentageArraer() {
        return percentageArraer;
    }

    public void setPercentageArraer(int percentageArraer) {
        this.percentageArraer = percentageArraer;
    }

    public int getBillstatusid() {
        return billstatusid;
    }

    public void setBillstatusid(int billstatusid) {
        this.billstatusid = billstatusid;
    }

    public String getBillstatus() {
        return billstatus;
    }

    public void setBillstatus(String billstatus) {
        this.billstatus = billstatus;
    }

    public int getIrTotal() {
        return irTotal;
    }

    public void setIrTotal(int irTotal) {
        this.irTotal = irTotal;
    }

    public int getFullArrearpay() {
        return fullArrearpay;
    }

    public void setFullArrearpay(int fullArrearpay) {
        this.fullArrearpay = fullArrearpay;
    }

    public String getVchno() {
        return vchno;
    }

    public void setVchno(String vchno) {
        this.vchno = vchno;
    }

    public String getOtherRecovery() {
        return otherRecovery;
    }

    public void setOtherRecovery(String otherRecovery) {
        this.otherRecovery = otherRecovery;
    }

    public String getDdoRecovery() {
        return ddoRecovery;
    }

    public void setDdoRecovery(String ddoRecovery) {
        this.ddoRecovery = ddoRecovery;
    }
    
}
