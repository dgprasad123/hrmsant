/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.misreport;

import hrms.model.employee.Employee;
import hrms.model.employee.EmployeeERCommentForm;
import java.util.ArrayList;

/**
 *
 * @author Manas Jena
 */
public interface CadreEmployeeReportDAO {
    public ArrayList getEmployeeList(String cadreCode,String allYear, String sortby);
    public Employee getEmployeeData(String empid,String cadreCode);
  //  public ArrayList getEmployeeData(String empid,String cadreCode);
    public void saveEmployeeData(Employee employee);
    public ArrayList getIncumbancyChart(String spc);
    public ArrayList getEmployeeIncumbancyChart(String empid);
    public void SavecommentERSheet(EmployeeERCommentForm ersheet,String empid,String gpfNo);
     public EmployeeERCommentForm editcommentErsheet(String empId);
}
