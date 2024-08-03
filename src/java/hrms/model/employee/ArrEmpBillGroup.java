/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.model.employee;

import java.util.List;

/**
 *
 * @author Manoj PC
 */
public class ArrEmpBillGroup {
    private String billGroupName = null;
    private List empList = null;

    public String getBillGroupName() {
        return billGroupName;
    }

    public void setBillGroupName(String billGroupName) {
        this.billGroupName = billGroupName;
    }

    public List getEmpList() {
        return empList;
    }

    public void setEmpList(List empList) {
        this.empList = empList;
    }
            
}
