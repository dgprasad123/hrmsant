/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.master;

/**
 *
 * @author lenovo pc
 */
public class Category {
    private String categoryid=null;
    private String categoryName=null;
    private String disabilityCode=null;
    private String disabilityName=null;
    private String specificCode=null;
    private String specificName=null;

    public String getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(String categoryid) {
        this.categoryid = categoryid;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getDisabilityCode() {
        return disabilityCode;
    }

    public void setDisabilityCode(String disabilityCode) {
        this.disabilityCode = disabilityCode;
    }

    public String getDisabilityName() {
        return disabilityName;
    }

    public void setDisabilityName(String disabilityName) {
        this.disabilityName = disabilityName;
    }

    public String getSpecificCode() {
        return specificCode;
    }

    public void setSpecificCode(String specificCode) {
        this.specificCode = specificCode;
    }

    public String getSpecificName() {
        return specificName;
    }

    public void setSpecificName(String specificName) {
        this.specificName = specificName;
    }

    
    
}
