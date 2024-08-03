package hrms.model.payroll.schedule;

import java.util.List;

public class ECScheduleForm extends ScheduleHelper{
    
    private String monthYear;
    private String deptName;
    private String officeName;
    private String ddoDegn;
    private String billNo;
    
    private String billDesc;   
    
    private List ecScheduleDtls = null;   
    
    public String totalcharges;

    public String getMonthYear() {
        return monthYear;
    }

    public void setMonthYear(String monthYear) {
        this.monthYear = monthYear;
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

    public List getEcScheduleDtls() {
        return ecScheduleDtls;
    }

    public void setEcScheduleDtls(List ecScheduleDtls) {
        this.ecScheduleDtls = ecScheduleDtls;
    }

    public String getTotalcharges() {
        return totalcharges;
    }

    public void setTotalcharges(String totalcharges) {
        this.totalcharges = totalcharges;
    }
    
}
