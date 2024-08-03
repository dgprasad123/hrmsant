/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.report.gispassbook;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author lenovo
 */
public interface GisPassbookDAO {
    public ArrayList getgisPassbookList(String empId);
    
    public List getgisEmployeeDetaills(String empId);
    
    public List getGisAlertList(String offCode, int Year);
    
    public ArrayList EmployeePostList(String offCode,int month,int Year);
}
