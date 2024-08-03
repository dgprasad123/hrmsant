/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.dao.SingleBill;

import java.util.List;

/**
 *
 * @author prashant
 */
public interface SinglePayBillDAO {
    
    public List getDDODetailsList(String distCode, String userType);
    
    public List getOnlineBillSubmittedDDOs(String distCode, String userType);
    public List getDdoWiseBillList(String billType, String offCode, String aqYear, String aqMonth, String userType,String arrearType);
    
    public List getAGWiseTreasuryCodeList();
    public List getAGWiseBillList(String trcode, String majorhead, String aqYear, String aqMonth);
}
