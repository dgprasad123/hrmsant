/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.model.LTC;

/**
 *
 * @author Manoj PC
 */
public class FamilyMemberBean {
        private String fMemberName = null;
    private String fMemberRelationship = null;
    private String fMemberdob = null;
    private String fMemberMstatus = null;
    private String isStateGovt = null;
    private String monthlyIncome = null;

    public String getfMemberName() {
        return fMemberName;
    }

    public void setfMemberName(String fMemberName) {
        this.fMemberName = fMemberName;
    }

    public String getfMemberRelationship() {
        return fMemberRelationship;
    }

    public void setfMemberRelationship(String fMemberRelationship) {
        this.fMemberRelationship = fMemberRelationship;
    }

    public String getfMemberdob() {
        return fMemberdob;
    }

    public void setfMemberdob(String fMemberdob) {
        this.fMemberdob = fMemberdob;
    }

    public String getfMemberMstatus() {
        return fMemberMstatus;
    }

    public void setfMemberMstatus(String fMemberMstatus) {
        this.fMemberMstatus = fMemberMstatus;
    }

    public String getIsStateGovt() {
        return isStateGovt;
    }

    public void setIsStateGovt(String isStateGovt) {
        this.isStateGovt = isStateGovt;
    }

    public String getMonthlyIncome() {
        return monthlyIncome;
    }

    public void setMonthlyIncome(String monthlyIncome) {
        this.monthlyIncome = monthlyIncome;
    }

}
