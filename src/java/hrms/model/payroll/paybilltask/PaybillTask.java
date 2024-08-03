/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.payroll.paybilltask;

import java.math.BigDecimal;

/**
 *
 * @author Manas Jena
 */
public class PaybillTask {
    private int taskid;
    private int billid;
    private BigDecimal billgroupid;
    private String offcode;
    private int aqmonth;
    private int aqyear;
    
    public int getTaskid() {
        return taskid;
    }

    public void setTaskid(int taskid) {
        this.taskid = taskid;
    }

    public int getBillid() {
        return billid;
    }

    public void setBillid(int billid) {
        this.billid = billid;
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

    public int getAqmonth() {
        return aqmonth;
    }

    public void setAqmonth(int aqmonth) {
        this.aqmonth = aqmonth;
    }

    public int getAqyear() {
        return aqyear;
    }

    public void setAqyear(int aqyear) {
        this.aqyear = aqyear;
    }
    
}
