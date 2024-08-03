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
public class EmpLeaveSurrenderedPropeties {
    private String surrenderDate;
    private int surrenderDays;
    private long balanceLeave;

    public String getSurrenderDate() {
        return surrenderDate;
    }

    public void setSurrenderDate(String surrenderDate) {
        this.surrenderDate = surrenderDate;
    }

    public int getSurrenderDays() {
        return surrenderDays;
    }

    public void setSurrenderDays(int surrenderDays) {
        this.surrenderDays = surrenderDays;
    }

    public long getBalanceLeave() {
        return balanceLeave;
    }

    public void setBalanceLeave(long balanceLeave) {
        this.balanceLeave = balanceLeave;
    }
    
    
}
