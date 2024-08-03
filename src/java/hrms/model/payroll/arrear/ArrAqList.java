/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.payroll.arrear;

import java.util.List;

/**
 *
 * @author lenovo
 */
public class ArrAqList {
    private String empName=null;
     private List  empList=null; 

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public List getEmpList() {
        return empList;
    }

    public void setEmpList(List empList) {
        this.empList = empList;
    }
}
