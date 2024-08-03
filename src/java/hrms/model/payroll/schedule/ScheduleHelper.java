/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.model.payroll.schedule;

import java.util.ArrayList;
import java.util.List;

public class ScheduleHelper extends Schedule{
    
    private String gpfNo=null;
    private String gpfType=null;
    private String pranNo=null;
    
    private String empcode=null;
    private String empname=null;
    private String empBasicSal=null;
    private String empPanNo=null;
    private String empdesg=null;
    private String amount=null;
    private String empGradepay=null;
    private String empPersonalpay=null;
    private String empDearnespay=null;
    private String empCpf=null;
    private String empGcpf=null;
    private String empNps=null;
    private int empNo;
    private String rowspan=null;
    private ArrayList helperList=null;
    
    private List allowanceList=null;
    private List deductionList=null;
    
    private int releaseTot=0;
    private String releaseTotFig=null;
    
    private String pageheaderparent = null;
    private String pagebreakparent = null;
    private String pageNoParent = null;
    private String aqslNo=null;
    
    private int deductAmt = 0;
    private String pagebreakPLS = null;
    private String pageHeaderPLS = null;
    private String pageTotalPLS = null;
    private String pagebreakOTC = null;

    public String getPageTotalPLS() {
        return pageTotalPLS;
    }

    public void setPageTotalPLS(String pageTotalPLS) {
        this.pageTotalPLS = pageTotalPLS;
    }
    
    public String getAqslNo() {
        return aqslNo;
    }

    public void setAqslNo(String aqslNo) {
        this.aqslNo = aqslNo;
    }

    public String getPagebreakparent() {
        return pagebreakparent;
    }

    public void setPagebreakparent(String pagebreakparent) {
        this.pagebreakparent = pagebreakparent;
    }

    public String getPageNoParent() {
        return pageNoParent;
    }

    public void setPageNoParent(String pageNoParent) {
        this.pageNoParent = pageNoParent;
    }

    
    public String getEmpcode() {
        return empcode;
    }

    public void setEmpcode(String empcode) {
        this.empcode = empcode;
    }

    public String getEmpname() {
        return empname;
    }

    public void setEmpname(String empname) {
        this.empname = empname;
    }

    public String getEmpdesg() {
        return empdesg;
    }

    public void setEmpdesg(String empdesg) {
        this.empdesg = empdesg;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getGpfNo() {
        return gpfNo;
    }

    public void setGpfNo(String gpfNo) {
        this.gpfNo = gpfNo;
    }  

    public String getEmpBasicSal() {
        return empBasicSal;
    }

    public void setEmpBasicSal(String empBasicSal) {
        this.empBasicSal = empBasicSal;
    }

    public String getEmpPanNo() {
        return empPanNo;
    }

    public void setEmpPanNo(String empPanNo) {
        this.empPanNo = empPanNo;
    }

    public String getPranNo() {
        return pranNo;
    }

    public void setPranNo(String pranNo) {
        this.pranNo = pranNo;
    }

    public String getEmpGradepay() {
        return empGradepay;
    }

    public void setEmpGradepay(String empGradepay) {
        this.empGradepay = empGradepay;
    }

    public String getEmpPersonalpay() {
        return empPersonalpay;
    }

    public void setEmpPersonalpay(String empPersonalpay) {
        this.empPersonalpay = empPersonalpay;
    }

    public String getEmpCpf() {
        return empCpf;
    }

    public void setEmpCpf(String empCpf) {
        this.empCpf = empCpf;
    }

    public String getEmpGcpf() {
        return empGcpf;
    }

    public void setEmpGcpf(String empGcpf) {
        this.empGcpf = empGcpf;
    }

    public String getEmpNps() {
        return empNps;
    }

    public void setEmpNps(String empNps) {
        this.empNps = empNps;
    }

    public String getEmpDearnespay() {
        return empDearnespay;
    }

    public void setEmpDearnespay(String empDearnespay) {
        this.empDearnespay = empDearnespay;
    }

    public String getRowspan() {
        return rowspan;
    }

    public void setRowspan(String rowspan) {
        this.rowspan = rowspan;
    }

    public ArrayList getHelperList() {
        return helperList;
    }

    public void setHelperList(ArrayList helperList) {
        this.helperList = helperList;
    }

    public List getAllowanceList() {
        return allowanceList;
    }

    public void setAllowanceList(List allowanceList) {
        this.allowanceList = allowanceList;
    }

    public List getDeductionList() {
        return deductionList;
    }

    public void setDeductionList(List deductionList) {
        this.deductionList = deductionList;
    }

    public String getGpfType() {
        return gpfType;
    }

    public void setGpfType(String gpfType) {
        this.gpfType = gpfType;
    }

    public int getEmpNo() {
        return empNo;
    }

    public void setEmpNo(int empNo) {
        this.empNo = empNo;
    }

    public int getReleaseTot() {
        return releaseTot;
    }

    public void setReleaseTot(int releaseTot) {
        this.releaseTot = releaseTot;
    }

    public String getReleaseTotFig() {
        return releaseTotFig;
    }

    public void setReleaseTotFig(String releaseTotFig) {
        this.releaseTotFig = releaseTotFig;
    }

    public String getPageheaderparent() {
        return pageheaderparent;
    }

    public void setPageheaderparent(String pageheaderparent) {
        this.pageheaderparent = pageheaderparent;
    }

    public int getDeductAmt() {
        return deductAmt;
    }

    public void setDeductAmt(int deductAmt) {
        this.deductAmt = deductAmt;
    }

    public String getPagebreakPLS() {
        return pagebreakPLS;
    }

    public void setPagebreakPLS(String pagebreakPLS) {
        this.pagebreakPLS = pagebreakPLS;
    }

    public String getPageHeaderPLS() {
        return pageHeaderPLS;
    }

    public void setPageHeaderPLS(String pageHeaderPLS) {
        this.pageHeaderPLS = pageHeaderPLS;
    }

    public String getPagebreakOTC() {
        return pagebreakOTC;
    }

    public void setPagebreakOTC(String pagebreakOTC) {
        this.pagebreakOTC = pagebreakOTC;
    }

    
    
    
    
}

