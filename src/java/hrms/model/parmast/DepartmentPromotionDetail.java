/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.model.parmast;

import hrms.model.fiscalyear.FiscalYear;
import java.util.ArrayList;

/**
 *
 * @author manisha
 */
public class DepartmentPromotionDetail {
    private int dpcId;
    private int detailId;
    private String hrmsId;
    private String empName;
    private String dob;
    private String empPost;
    private String isReviewed;
    private String spc;
    private String isadded;
    private ArrayList<FiscalYearWiseParData> fiscalYearList = new ArrayList() ;
    private String postGroupType;
    private String deployType;
    

    public int getDpcId() {
        return dpcId;
    }

    public void setDpcId(int dpcId) {
        this.dpcId = dpcId;
    }

    public int getDetailId() {
        return detailId;
    }

    public void setDetailId(int detailId) {
        this.detailId = detailId;
    }

    public String getHrmsId() {
        return hrmsId;
    }

    public void setHrmsId(String hrmsId) {
        this.hrmsId = hrmsId;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getEmpPost() {
        return empPost;
    }

    public void setEmpPost(String empPost) {
        this.empPost = empPost;
    }

    public String getIsReviewed() {
        return isReviewed;
    }

    public void setIsReviewed(String isReviewed) {
        this.isReviewed = isReviewed;
    }

    public String getSpc() {
        return spc;
    }

    public void setSpc(String spc) {
        this.spc = spc;
    }
   
    public ArrayList<FiscalYearWiseParData> getFiscalYearList() {
        return fiscalYearList;
    }

    public void setFiscalYearList(ArrayList<FiscalYearWiseParData> fiscalYearList) {
        this.fiscalYearList = fiscalYearList;
    }

    public String getIsadded() {
        return isadded;
    }

    public void setIsadded(String isadded) {
        this.isadded = isadded;
    }

    public String getPostGroupType() {
        return postGroupType;
    }

    public void setPostGroupType(String postGroupType) {
        this.postGroupType = postGroupType;
    }

    public String getDeployType() {
        return deployType;
    }

    public void setDeployType(String deployType) {
        this.deployType = deployType;
    }
    
    
}


