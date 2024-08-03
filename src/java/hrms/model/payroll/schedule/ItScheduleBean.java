/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.model.payroll.schedule;

import java.util.List;

public class ItScheduleBean extends ScheduleHelper{
    
    
    private String empDedutAmount;
    private String btId;
    private String montYear;
    private String schedule;
    private String scheduleName;
    private String aqmonth;
    private String aqyear;
    private String tanno;
    private List ITScheduleDtls = null;       
    private String deptName;
    private String officeName;
    private String ddoDegn;
    private String billNo;
    private String billDesc;
    private String carryForward;
    private String total;
    
    private String pagebreakIT;
    private String pageHeaderIT;

    public String getPagebreakIT() {
        return pagebreakIT;
    }

    public void setPagebreakIT(String pagebreakIT) {
        this.pagebreakIT = pagebreakIT;
    }

    public String getPageHeaderIT() {
        return pageHeaderIT;
    }

    public void setPageHeaderIT(String pageHeaderIT) {
        this.pageHeaderIT = pageHeaderIT;
    }
    
    public String getEmpDedutAmount() {
        return empDedutAmount;
    }

    public void setEmpDedutAmount(String empDedutAmount) {
        this.empDedutAmount = empDedutAmount;
    }

    public String getBtId() {
        return btId;
    }

    public void setBtId(String btId) {
        this.btId = btId;
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

    public String getMontYear() {
        return montYear;
    }

    public void setMontYear(String montYear) {
        this.montYear = montYear;
    }

    public String getScheduleName() {
        return scheduleName;
    }

    public void setScheduleName(String scheduleName) {
        this.scheduleName = scheduleName;
    }

    public String getAqmonth() {
        return aqmonth;
    }

    public void setAqmonth(String aqmonth) {
        this.aqmonth = aqmonth;
    }

    public String getAqyear() {
        return aqyear;
    }

    public void setAqyear(String aqyear) {
        this.aqyear = aqyear;
    }

    public String getTanno() {
        return tanno;
    }

    public void setTanno(String tanno) {
        this.tanno = tanno;
    }

    public List getITScheduleDtls() {
        return ITScheduleDtls;
    }

    public void setITScheduleDtls(List ITScheduleDtls) {
        this.ITScheduleDtls = ITScheduleDtls;
    }

    public String getCarryForward() {
        return carryForward;
    }

    public void setCarryForward(String carryForward) {
        this.carryForward = carryForward;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

        
}
