/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.model.payroll.arrear;

import java.util.Date;

/**
 *
 * @author Manas
 */
public class PayRevisionIncrement {
    private Date incrementDate = null;
    private int incrementedbasic;

    public Date getIncrementDate() {
        return incrementDate;
    }

    public void setIncrementDate(Date incrementDate) {
        this.incrementDate = incrementDate;
    }

    public int getIncrementedbasic() {
        return incrementedbasic;
    }

    public void setIncrementedbasic(int incrementedbasic) {
        this.incrementedbasic = incrementedbasic;
    }
    
    
}
