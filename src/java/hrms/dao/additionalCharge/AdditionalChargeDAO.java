/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.additionalCharge;


import hrms.model.additionalCharge.AdditionalCharge;
import java.util.List;

/**
 *
 * @author Surendra
 */
public interface AdditionalChargeDAO {

    //public static List findAllAdditionalCharge(String empid, int minlimit, int maxlimit);
    
    public int insertAdditionalChargeData(AdditionalCharge addchage);
    
    public int updateAdditionalChargeData(AdditionalCharge addchage);
    
    public void deleteAdditionalCharge(String empid, int notId, String spc);
    
    public AdditionalCharge editAdditionalCharge(String empid, int notId);
    
    public List findAllAdditionalCharge(String empId);
    
    public void updateDDOInOfficeDetails(String offCode,String postCode,String empid);
}
