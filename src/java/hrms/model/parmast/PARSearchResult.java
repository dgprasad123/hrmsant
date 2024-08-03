/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.model.parmast;

import java.util.List;

/**
 *
 * @author DurgaPrasad
 */
public class PARSearchResult {
    private List parlist;
    private int totalPARFound;

    public List getParlist() {
        return parlist;
    }

    public void setParlist(List parlist) {
        this.parlist = parlist;
    }

    public int getTotalPARFound() {
        return totalPARFound;
    }

    public void setTotalPARFound(int totalPARFound) {
        this.totalPARFound = totalPARFound;
    }
    
    
}
