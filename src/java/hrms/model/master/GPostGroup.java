/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.model.master;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Manoj PC
 */
public class GPostGroup implements Serializable {
    private String mergeGroup = null;
    private List dataList = new ArrayList();

    public String getMergeGroup() {
        return mergeGroup;
    }

    public void setMergeGroup(String mergeGroup) {
        this.mergeGroup = mergeGroup;
    }

    public List getDataList() {
        return dataList;
    }

    public void setDataList(List dataList) {
        this.dataList = dataList;
    }
    
}
