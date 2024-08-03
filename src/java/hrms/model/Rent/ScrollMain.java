/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.model.Rent;

import java.util.List;

/**
 *
 * @author Manoj PC
 */
public class ScrollMain {
    private String deptCode = "";
    private List<ScrollData> scrollData;

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    public List<ScrollData> getScrollData() {
        return scrollData;
    }

    public void setScrollData(List<ScrollData> scrollData) {
        this.scrollData = scrollData;
    }


    
}
