/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.propertystatement;

import hrms.model.propertystatement.PropertyAdminSearchCriteria;
import hrms.model.propertystatement.PropertySearchResult;
import hrms.model.propertystatement.PropertyStatementAdminBean;
import hrms.model.propertystatement.PropertyStatementStatusBean;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Manisha
 */
public interface PropertyStatementAdminDAO {

    public String unlockPropertyStatement(String loginempid,PropertyStatementAdminBean propertyStatementAdminBean);

    public PropertySearchResult getPropertyStatement(String fiscalyear, String searchCriteria, String searchString, int noofrows, int page);
    
    public PropertySearchResult getPropertyStatementForDDO(String fiscalyear,String offCode, String searchCriteria, String searchString, int noofrows, int page);

    public PropertySearchResult getPropertyStatement(PropertyAdminSearchCriteria propertyAdminSearchCriteria);
    
    public List getGroupWisePropertyStatus(String fiscalyear);
    
    public void savePropertyStatementtatusDetailByPARAdmin(PropertyStatementStatusBean propertyStatementStatusBean);
    
    public ArrayList getPropertyStatementtatusDetailByPARAdmin();
    
    public PropertyStatementStatusBean getAttachedFileforChangePropertyStatusByPARAdmin(int logId);
    
    public ArrayList getOfficeListForDDO(String empId);
}
