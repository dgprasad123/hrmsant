/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.billvouchingTreasury;

/**
 *
 * @author Surendra
 */
public interface VouchingServicesDAO {
    public double getGrossAmt(int billId,String tabname) ;
    public double getdeductAmt(int billId, String tabname,int aqmonth, int aqyear);
    

}
