/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.propertystatement;

/**
 *
 * @author Manisha
 */
public class PropertyAdminSearchCriteria {

    private String fiscalyear;
    private String searchby;
    private String searchCriteria;
    private String searchString;
    private String cadre;
    private String offcode;
    private String curOfficeName;
    private int rows;
    private int page;

    public String getFiscalyear() {
        return fiscalyear;
    }

    public void setFiscalyear(String fiscalyear) {
        this.fiscalyear = fiscalyear;
    }

    public String getSearchby() {
        return searchby;
    }

    public void setSearchby(String searchby) {
        this.searchby = searchby;
    }

    public String getSearchCriteria() {
        return searchCriteria;
    }

    public void setSearchCriteria(String searchCriteria) {
        this.searchCriteria = searchCriteria;
    }

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    public String getCadre() {
        return cadre;
    }

    public void setCadre(String cadre) {
        this.cadre = cadre;
    }

    public String getOffcode() {
        return offcode;
    }

    public void setOffcode(String offcode) {
        this.offcode = offcode;
    }

    public String getCurOfficeName() {
        return curOfficeName;
    }

    public void setCurOfficeName(String curOfficeName) {
        this.curOfficeName = curOfficeName;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }   

}
