/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.propertystatement;

import java.util.List;

/**
 *
 * @author Manisha
 */
public class PropertySearchResult {

    private List propertyList;
    private int totalPropertyFound;

    public List getPropertyList() {
        return propertyList;
    }

    public void setPropertyList(List propertyList) {
        this.propertyList = propertyList;
    }

    public int getTotalPropertyFound() {
        return totalPropertyFound;
    }

    public void setTotalPropertyFound(int totalPropertyFound) {
        this.totalPropertyFound = totalPropertyFound;
    }

}
