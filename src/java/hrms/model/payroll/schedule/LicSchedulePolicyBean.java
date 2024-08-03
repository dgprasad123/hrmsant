/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.model.payroll.schedule;

public class LicSchedulePolicyBean extends ScheduleHelper{
    public String policyNo=null;
    public String recoveryMonth=null;
    public String amount=null;
    public String total=null;
    public String carryForwardTotal=null;

    public String getPolicyNo() {
        return policyNo;
    }

    public void setPolicyNo(String policyNo) {
        this.policyNo = policyNo;
    }

    public String getRecoveryMonth() {
        return recoveryMonth;
    }

    public void setRecoveryMonth(String recoveryMonth) {
        this.recoveryMonth = recoveryMonth;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getCarryForwardTotal() {
        return carryForwardTotal;
    }

    public void setCarryForwardTotal(String carryForwardTotal) {
        this.carryForwardTotal = carryForwardTotal;
    }
    
    
    
}
