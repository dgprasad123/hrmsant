/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.model.leave;

import java.io.Serializable;

/**
 *
 * @author lenovo pc
 */
public class LeaveEntrytakenBean implements Serializable{
    private String deptCode=null;
    private String offcode=null;

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    public String getOffcode() {
        return offcode;
    }

    public void setOffcode(String offcode) {
        this.offcode = offcode;
    }
    
    
    
}
