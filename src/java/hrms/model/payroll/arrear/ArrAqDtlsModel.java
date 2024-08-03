/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.payroll.arrear;

import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Manas
 */
public class ArrAqDtlsModel {

    private String aqslno;
    private String refaqslno;
    private int calcuniqueno;
    private int payMonth;
    private int payYear;
    private String payMonthName;
    private String adType = null;
    private int drawnAMt;
    private int dueAmt;
    private int dueGpAmt;
    private int duePayAmt;
    private int dueDaAmt;
    private int dueHraAmt;
    private int dueOaAmt;
    private int dueIrAmt;
    private int dueTotalAmt;
    private int drawnPayAmt;
    private int drawnDaAmt;
    private int drawnGpAmt;
    private int drawnHraAmt;
    private int drawnOaAmt;
    private int drawnIrAmt;
    private int drawnTotalAmt;
    private String btid;
    private String remark = null;
    private String drawnBillNo = null;
    private int slno;
    private BigDecimal aqGroup;
    private String aqGroupDesc = null;
    private String empCode = null;
    private String empName = null;
    private String curDesg = null;
    private String curBasic = null;
    public List arrDetails = null;
    private String offCode = null;
    private String offDdo = null;
    private String arrtype;

    private String billDesc = null;
    private String fromMonth = null;
    private String fromYear = null;
    private String toMonth = null;
    private String toYear = null;
    private int arrear100;
    private double incomeTaxAmt;
    private double alreadyPaid;
    private double toBePaid;
    private List<ArrAqDtlsModel> payList = null;
    
    private int dueCPFAmt;
    private int drawnCPFAmt;
    
    private int autoincrid;
    private String allowArrear;
    
    public int getDueHraAmt() {
        return dueHraAmt;
    }

    public void setDueHraAmt(int dueHraAmt) {
        this.dueHraAmt = dueHraAmt;
    }

    public int getDueOaAmt() {
        return dueOaAmt;
    }

    public void setDueOaAmt(int dueOaAmt) {
        this.dueOaAmt = dueOaAmt;
    }

    public int getDueIrAmt() {
        return dueIrAmt;
    }

    public void setDueIrAmt(int dueIrAmt) {
        this.dueIrAmt = dueIrAmt;
    }

    public int getDrawnHraAmt() {
        return drawnHraAmt;
    }

    public void setDrawnHraAmt(int drawnHraAmt) {
        this.drawnHraAmt = drawnHraAmt;
    }

    public int getDrawnOaAmt() {
        return drawnOaAmt;
    }

    public void setDrawnOaAmt(int drawnOaAmt) {
        this.drawnOaAmt = drawnOaAmt;
    }

    public int getDrawnIrAmt() {
        return drawnIrAmt;
    }

    public void setDrawnIrAmt(int drawnIrAmt) {
        this.drawnIrAmt = drawnIrAmt;
    }

    public double getAlreadyPaid() {
        return alreadyPaid;
    }

    public void setAlreadyPaid(double alreadyPaid) {
        this.alreadyPaid = alreadyPaid;
    }

    public double getToBePaid() {
        return toBePaid;
    }

    public void setToBePaid(double toBePaid) {
        this.toBePaid = toBePaid;
    }

    public int getCalcuniqueno() {
        return calcuniqueno;
    }

    public void setCalcuniqueno(int calcuniqueno) {
        this.calcuniqueno = calcuniqueno;
    }

    public List<ArrAqDtlsModel> getPayList() {
        return payList;
    }

    public void setPayList(List<ArrAqDtlsModel> payList) {
        this.payList = payList;
    }

    public String getAqslno() {
        return aqslno;
    }

    public void setAqslno(String aqslno) {
        this.aqslno = aqslno;
    }

    public String getRefaqslno() {
        return refaqslno;
    }

    public void setRefaqslno(String refaqslno) {
        this.refaqslno = refaqslno;
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

    public String getPayMonthName() {
        return payMonthName;
    }

    public void setPayMonthName(String payMonthName) {
        this.payMonthName = payMonthName;
    }

    public String getAdType() {
        return adType;
    }

    public void setAdType(String adType) {
        this.adType = adType;
    }

    public int getDrawnAMt() {
        return drawnAMt;
    }

    public void setDrawnAMt(int drawnAMt) {
        this.drawnAMt = drawnAMt;
    }

    public int getDueAmt() {
        return dueAmt;
    }

    public void setDueAmt(int dueAmt) {
        this.dueAmt = dueAmt;
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

    public int getSlno() {
        return slno;
    }

    public void setSlno(int slno) {
        this.slno = slno;
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

    public String getCurDesg() {
        return curDesg;
    }

    public void setCurDesg(String curDesg) {
        this.curDesg = curDesg;
    }

    public String getCurBasic() {
        return curBasic;
    }

    public void setCurBasic(String curBasic) {
        this.curBasic = curBasic;
    }

    public List getArrDetails() {
        return arrDetails;
    }

    public void setArrDetails(List arrDetails) {
        this.arrDetails = arrDetails;
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

    public String getArrtype() {
        return arrtype;
    }

    public void setArrtype(String arrtype) {
        this.arrtype = arrtype;
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

    public int getDueGpAmt() {
        return dueGpAmt;
    }

    public void setDueGpAmt(int dueGpAmt) {
        this.dueGpAmt = dueGpAmt;
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

    public String getBtid() {
        return btid;
    }

    public void setBtid(String btid) {
        this.btid = btid;
    }

    public int getArrear100() {
        return arrear100;
    }

    public void setArrear100(int arrear100) {
        this.arrear100 = arrear100;
    }

    public double getIncomeTaxAmt() {
        return incomeTaxAmt;
    }

    public void setIncomeTaxAmt(double incomeTaxAmt) {
        this.incomeTaxAmt = incomeTaxAmt;
    }

    public int getDueCPFAmt() {
        return dueCPFAmt;
    }

    public void setDueCPFAmt(int dueCPFAmt) {
        this.dueCPFAmt = dueCPFAmt;
    }

    public int getDrawnCPFAmt() {
        return drawnCPFAmt;
    }

    public void setDrawnCPFAmt(int drawnCPFAmt) {
        this.drawnCPFAmt = drawnCPFAmt;
    }

    public int getAutoincrid() {
        return autoincrid;
    }

    public void setAutoincrid(int autoincrid) {
        this.autoincrid = autoincrid;
    }

    public String getAllowArrear() {
        return allowArrear;
    }

    public void setAllowArrear(String allowArrear) {
        this.allowArrear = allowArrear;
    }
    

}
