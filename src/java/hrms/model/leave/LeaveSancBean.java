/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.model.leave;

import java.io.Serializable;

/**
 *
 * @author lenovo pc
 */
public class LeaveSancBean implements Serializable{
   private String deptCode=null;
   private String offCode=null;
   private String authCode=null;
   private String initiatedEmpId=null;
   private String tolId=null;
   private String fromDate=null;
   private String toDate=null;
   private String suffixeFrom=null;
   private String suffixeTo=null;
    private String prefixFrom=null;
   private String prefixTo=null;
    private String joinDate=null;

    public String getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(String joinDate) {
        this.joinDate = joinDate;
    }
    
    

    public String getTolId() {
        return tolId;
    }

    public void setTolId(String tolId) {
        this.tolId = tolId;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getSuffixeFrom() {
        return suffixeFrom;
    }

    public void setSuffixeFrom(String suffixeFrom) {
        this.suffixeFrom = suffixeFrom;
    }

    public String getSuffixeTo() {
        return suffixeTo;
    }

    public void setSuffixeTo(String suffixeTo) {
        this.suffixeTo = suffixeTo;
    }

    public String getPrefixFrom() {
        return prefixFrom;
    }

    public void setPrefixFrom(String prefixFrom) {
        this.prefixFrom = prefixFrom;
    }

    public String getPrefixTo() {
        return prefixTo;
    }

    public void setPrefixTo(String prefixTo) {
        this.prefixTo = prefixTo;
    }
   

    public String getInitiatedEmpId() {
        return initiatedEmpId;
    }

    public void setInitiatedEmpId(String initiatedEmpId) {
        this.initiatedEmpId = initiatedEmpId;
    }
   
    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    

    public String getOffCode() {
        return offCode;
    }

    public void setOffCode(String offCode) {
        this.offCode = offCode;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }
   
   
}
