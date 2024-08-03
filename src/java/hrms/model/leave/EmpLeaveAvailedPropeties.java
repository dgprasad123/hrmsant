/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.leave;

/**
 *
 * @author Manas
 */
public class EmpLeaveAvailedPropeties {
    private String fromdate;
    private String todate;
    private int totalNoofdays;
    private long balanceLeave;
    private String lsotId;
    private String tolId;

    public String getTolId() {
        return tolId;
    }

    public void setTolId(String tolId) {
        this.tolId = tolId;
    }
    

    public String getLsotId() {
        return lsotId;
    }

    public void setLsotId(String lsotId) {
        this.lsotId = lsotId;
    }
    
    

    public String getFromdate() {
        return fromdate;
    }

    public void setFromdate(String fromdate) {
        this.fromdate = fromdate;
    }

    public String getTodate() {
        return todate;
    }

    public void setTodate(String todate) {
        this.todate = todate;
    }

    public int getTotalNoofdays() {
        return totalNoofdays;
    }

    public void setTotalNoofdays(int totalNoofdays) {
        this.totalNoofdays = totalNoofdays;
    }

    public long getBalanceLeave() {
        return balanceLeave;
    }

    public void setBalanceLeave(long balanceLeave) {
        this.balanceLeave = balanceLeave;
    }
    
    
}
