/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.servicebook;

import java.util.List;

/**
 *
 * @author lenovo pc
 */
public class EmpServiceBook {

    private List<EmpServiceHistory> empsbrecord;

    public List<EmpServiceHistory> getEmpsbrecord() {
        return empsbrecord;
    }

    public void setEmpsbrecord(List<EmpServiceHistory> empsbrecord) {
        this.empsbrecord = empsbrecord;
    }
}
