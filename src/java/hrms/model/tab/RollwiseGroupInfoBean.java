/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.tab;

import java.util.ArrayList;

/**
 *
 * @author Surendra
 */
public class RollwiseGroupInfoBean {
    private ArrayList grpList;
    private ArrayList favList;
    
    private String empName=null;
    private String empId=null;
    private String gpfNo=null;
    private String designation=null;
    private String curCadre=null;
    private String curStatus=null;
    private String rollID=null;
    private String userType=null;
    private String isAccessible=null;
    private String isManageGoal;
    private String isManageObjective;
    private String curOffice=null;

    public ArrayList getGrpList() {
        return grpList;
    }

    public void setGrpList(ArrayList grpList) {
        this.grpList = grpList;
    }

    public ArrayList getFavList() {
        return favList;
    }

    public void setFavList(ArrayList favList) {
        this.favList = favList;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getGpfNo() {
        return gpfNo;
    }

    public void setGpfNo(String gpfNo) {
        this.gpfNo = gpfNo;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getCurCadre() {
        return curCadre;
    }

    public void setCurCadre(String curCadre) {
        this.curCadre = curCadre;
    }

    public String getCurStatus() {
        return curStatus;
    }

    public void setCurStatus(String curStatus) {
        this.curStatus = curStatus;
    }

    public String getRollID() {
        return rollID;
    }

    public void setRollID(String rollID) {
        this.rollID = rollID;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getIsAccessible() {
        return isAccessible;
    }

    public void setIsAccessible(String isAccessible) {
        this.isAccessible = isAccessible;
    }

    public String getIsManageGoal() {
        return isManageGoal;
    }

    public void setIsManageGoal(String isManageGoal) {
        this.isManageGoal = isManageGoal;
    }

    public String getIsManageObjective() {
        return isManageObjective;
    }

    public void setIsManageObjective(String isManageObjective) {
        this.isManageObjective = isManageObjective;
    }

    public String getCurOffice() {
        return curOffice;
    }

    public void setCurOffice(String curOffice) {
        this.curOffice = curOffice;
    }

    
    
}
