/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.model.payroll.arrear;

/**
 *
 * @author prashant
 */
public class OAClaimModel {
    
    private String hrmsId;
    private String empName;
    private String post;
    private String basic;
    private String gp;
    
    private String objectHead;
    private String billGroupName;
    private String fromMonth;
    private String fromYear;
    private String toMonth;
    private String toYear;
    private String txtOaAmount;
    private String hidHrmsId;
    private String hidType;

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

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getBasic() {
        return basic;
    }

    public void setBasic(String basic) {
        this.basic = basic;
    }

    public String getGp() {
        return gp;
    }

    public void setGp(String gp) {
        this.gp = gp;
    }
    
    public String getBillGroupName() {
        return billGroupName;
    }

    public void setBillGroupName(String billGroupName) {
        this.billGroupName = billGroupName;
    }

    public String getFromMonth() {
        return fromMonth;
    }

    public void setFromMonth(String fromMonth) {
        this.fromMonth = fromMonth;
    }

    public String getFromYear() {
        return fromYear;
    }

    public void setFromYear(String fromYear) {
        this.fromYear = fromYear;
    }

    public String getToMonth() {
        return toMonth;
    }

    public void setToMonth(String toMonth) {
        this.toMonth = toMonth;
    }

    public String getToYear() {
        return toYear;
    }

    public void setToYear(String toYear) {
        this.toYear = toYear;
    }

    public String getTxtOaAmount() {
        return txtOaAmount;
    }

    public void setTxtOaAmount(String txtOaAmount) {
        this.txtOaAmount = txtOaAmount;
    }

    public String getHidHrmsId() {
        return hidHrmsId;
    }

    public void setHidHrmsId(String hidHrmsId) {
        this.hidHrmsId = hidHrmsId;
    }

    public String getObjectHead() {
        return objectHead;
    }

    public void setObjectHead(String objectHead) {
        this.objectHead = objectHead;
    }

    public String getHidType() {
        return hidType;
    }

    public void setHidType(String hidType) {
        this.hidType = hidType;
    }

    
    
}
