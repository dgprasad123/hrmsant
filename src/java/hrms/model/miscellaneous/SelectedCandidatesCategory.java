/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.model.miscellaneous;

/**
 *
 * @author Manoj PC
 */
public class SelectedCandidatesCategory {
    private int categoryId = 0;
    private String departmentId = null;
    private String groupName = null;
    private String cadreId = null;
    private String postId = null;
    private int noOfSelectedCanddiates = 0;
    private String orderNumber = null;
    private String orderDate = null;

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getCadreId() {
        return cadreId;
    }

    public void setCadreId(String cadreId) {
        this.cadreId = cadreId;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public int getNoOfSelectedCanddiates() {
        return noOfSelectedCanddiates;
    }

    public void setNoOfSelectedCanddiates(int noOfSelectedCanddiates) {
        this.noOfSelectedCanddiates = noOfSelectedCanddiates;
    }

      
    
}
