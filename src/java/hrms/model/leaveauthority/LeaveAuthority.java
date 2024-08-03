package hrms.model.leaveauthority;

import java.io.Serializable;
import java.util.ArrayList;

public class LeaveAuthority implements Serializable{
    private String txtsearch;
    private String[] radempid;
    private String txtcodehid;
    private String forAddRow;
    private String[] hidEmpName;
    private String processCode=null;
    private ArrayList empdeatails;
    private String myempId;
    private ArrayList deptList=null;
    private String deptCode=null;
    private String loggedInUserSpc=null;
    private String offCode=null;
    private String sltDept=null;
    private String sltOffice=null;
    private ArrayList offList=null;
    private ArrayList authArrList=null;
    private String chkAuth=null;
    private ArrayList myAuthList=null;
    private ArrayList processTypeArrList=null;
    private String sltYear = null;
    /*By Satya*/
    private String authType=null;
    private String rowid=null;
    
    private String parid = null;

    public String getTxtsearch() {
        return txtsearch;
    }

    public void setTxtsearch(String txtsearch) {
        this.txtsearch = txtsearch;
    }

    public String[] getRadempid() {
        return radempid;
    }

    public void setRadempid(String[] radempid) {
        this.radempid = radempid;
    }

    public String getTxtcodehid() {
        return txtcodehid;
    }

    public void setTxtcodehid(String txtcodehid) {
        this.txtcodehid = txtcodehid;
    }

    public String getForAddRow() {
        return forAddRow;
    }

    public void setForAddRow(String forAddRow) {
        this.forAddRow = forAddRow;
    }

    public String[] getHidEmpName() {
        return hidEmpName;
    }

    public void setHidEmpName(String[] hidEmpName) {
        this.hidEmpName = hidEmpName;
    }

    public String getProcessCode() {
        return processCode;
    }

    public void setProcessCode(String processCode) {
        this.processCode = processCode;
    }

    public ArrayList getEmpdeatails() {
        return empdeatails;
    }

    public void setEmpdeatails(ArrayList empdeatails) {
        this.empdeatails = empdeatails;
    }

    public String getMyempId() {
        return myempId;
    }

    public void setMyempId(String myempId) {
        this.myempId = myempId;
    }

    public ArrayList getDeptList() {
        return deptList;
    }

    public void setDeptList(ArrayList deptList) {
        this.deptList = deptList;
    }

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    public String getLoggedInUserSpc() {
        return loggedInUserSpc;
    }

    public void setLoggedInUserSpc(String loggedInUserSpc) {
        this.loggedInUserSpc = loggedInUserSpc;
    }

    public String getOffCode() {
        return offCode;
    }

    public void setOffCode(String offCode) {
        this.offCode = offCode;
    }

    public String getSltDept() {
        return sltDept;
    }

    public void setSltDept(String sltDept) {
        this.sltDept = sltDept;
    }

    public String getSltOffice() {
        return sltOffice;
    }

    public void setSltOffice(String sltOffice) {
        this.sltOffice = sltOffice;
    }

    public ArrayList getOffList() {
        return offList;
    }

    public void setOffList(ArrayList offList) {
        this.offList = offList;
    }

    public ArrayList getAuthArrList() {
        return authArrList;
    }

    public void setAuthArrList(ArrayList authArrList) {
        this.authArrList = authArrList;
    }

    public String getChkAuth() {
        return chkAuth;
    }

    public void setChkAuth(String chkAuth) {
        this.chkAuth = chkAuth;
    }

    

   

   
    public ArrayList getMyAuthList() {
        return myAuthList;
    }

    public void setMyAuthList(ArrayList myAuthList) {
        this.myAuthList = myAuthList;
    }

    public ArrayList getProcessTypeArrList() {
        return processTypeArrList;
    }

    public void setProcessTypeArrList(ArrayList processTypeArrList) {
        this.processTypeArrList = processTypeArrList;
    }

    public String getSltYear() {
        return sltYear;
    }

    public void setSltYear(String sltYear) {
        this.sltYear = sltYear;
    }

    public String getAuthType() {
        return authType;
    }

    public void setAuthType(String authType) {
        this.authType = authType;
    }

    public String getRowid() {
        return rowid;
    }

    public void setRowid(String rowid) {
        this.rowid = rowid;
    }

    public String getParid() {
        return parid;
    }

    public void setParid(String parid) {
        this.parid = parid;
    }
    

}
