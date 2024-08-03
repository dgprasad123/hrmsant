/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.model.parmast;

/**
 *
 * @author Manisha
 */
public class GroupCAdminAdminSearchCriteria {
   
    private int page;
    private int rows;
    private String fiscalyear;
    private String searchCriteria;
    private String searchString;
    private String searchGroupCStatus;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public String getFiscalyear() {
        return fiscalyear;
    }

    public void setFiscalyear(String fiscalyear) {
        this.fiscalyear = fiscalyear;
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

    public String getSearchGroupCStatus() {
        return searchGroupCStatus;
    }

    public void setSearchGroupCStatus(String searchGroupCStatus) {
        this.searchGroupCStatus = searchGroupCStatus;
    }
    
    
}
