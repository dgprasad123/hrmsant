/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.common;

import java.util.HashMap;

/**
 *
 * @author Surendra
 */
public class ChartDrillDownAttribute {
    private String name="";
    private String id="";
    private HashMap dataList=null;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public HashMap getDataList() {
        return dataList;
    }

    public void setDataList(HashMap dataList) {
        this.dataList = dataList;
    }

    

    
    
    
    
}
