/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.payentitlement;

import hrms.model.payentitlement.PayEntitlementForm;
import java.util.List;

/**
 *
 * @author Pintu-DELL
 */
public interface PayEntitlementDAO {
    
    public List getPayEntitlementList(String empid);
    
    public List getAllotDescList(String notType);
    
    public void savePayEntitlement(PayEntitlementForm payEntitlementForm, int notId,String loginempid,String sblanguage);
    
    public void updatePayEntitlement(PayEntitlementForm payEntitlementForm,String loginempid);
    
    public PayEntitlementForm getPayEntitlementData(PayEntitlementForm payEntitlementForm, int notId);
    
}
