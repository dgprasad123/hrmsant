/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.payroll.schedule;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Prashant
 */
public class WrrScheduleBean extends ScheduleHelper {

    private String address;
    private String quarterNo;
    private String carryForward;
    private String poolName;

    private String deptName;
    private String officeName;
    private String ddoDegn;
    private String billNo;
    private String billDesc;
    private String demandNo;
    private String txtYear;
    private String txtMonth;
    private List emplist = null;
    private String despListSize;
    private String consumerno;
    private String demandnoAsString;
    private String pageBreak;

    private int slNo = 0;
    private String pagebreakWR = null;
    private String pageHeaderWR = null;

    public int getSlNo() {
        return slNo;
    }

    public void setSlNo(int slNo) {
        this.slNo = slNo;
    }

    public String getPagebreakWR() {
        return pagebreakWR;
    }

    public void setPagebreakWR(String pagebreakWR) {
        this.pagebreakWR = pagebreakWR;
    }

    public String getPageHeaderWR() {
        return pageHeaderWR;
    }

    public void setPageHeaderWR(String pageHeaderWR) {
        this.pageHeaderWR = pageHeaderWR;
    }
    
    

    public String getConsumerno() {
        return consumerno;
    }

    public void setConsumerno(String consumerno) {
        this.consumerno = consumerno;
    }

    public String getDemandnoAsString() {
        return demandnoAsString;
    }

    public void setDemandnoAsString(String demandnoAsString) {
        this.demandnoAsString = demandnoAsString;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getQuarterNo() {
        return quarterNo;
    }

    public void setQuarterNo(String quarterNo) {
        this.quarterNo = quarterNo;
    }

    public String getCarryForward() {
        return carryForward;
    }

    public void setCarryForward(String carryForward) {
        this.carryForward = carryForward;
    }

    public String getPoolName() {
        return poolName;
    }

    public void setPoolName(String poolName) {
        this.poolName = poolName;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getOfficeName() {
        return officeName;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }

    public String getDdoDegn() {
        return ddoDegn;
    }

    public void setDdoDegn(String ddoDegn) {
        this.ddoDegn = ddoDegn;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getBillDesc() {
        return billDesc;
    }

    public void setBillDesc(String billDesc) {
        this.billDesc = billDesc;
    }

    public String getDemandNo() {
        return demandNo;
    }

    public void setDemandNo(String demandNo) {
        this.demandNo = demandNo;
    }

    public String getTxtYear() {
        return txtYear;
    }

    public void setTxtYear(String txtYear) {
        this.txtYear = txtYear;
    }

    public String getTxtMonth() {
        return txtMonth;
    }

    public void setTxtMonth(String txtMonth) {
        this.txtMonth = txtMonth;
    }

    public List getEmplist() {
        return emplist;
    }

    public void setEmplist(List emplist) {
        this.emplist = emplist;
    }

    public String getDespListSize() {
        return despListSize;
    }

    public void setDespListSize(String despListSize) {
        this.despListSize = despListSize;
    }

    public String getPageBreak() {
        return pageBreak;
    }

    public void setPageBreak(String pageBreak) {
        this.pageBreak = pageBreak;
    }

}
