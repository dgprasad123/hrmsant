/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.leave;

/**
 *
 * @author Manas
 */
public class LeaveRule {

    private String leaveId = "";
    private int interval = 0;
    private float crdCnt = 0;
    private float partCrdt = 0;
    private String wefDate = null;
    private String annualRound = null;
    private String when_credit = null;

    public float getCrdCnt() {
        return crdCnt;
    }

    public void setCrdCnt(float crdCnt) {
        this.crdCnt = crdCnt;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public String getLeaveId() {
        return leaveId;
    }

    public void setLeaveId(String leaveId) {
        this.leaveId = leaveId;
    }

    public float getPartCrdt() {
        return partCrdt;
    }

    public void setPartCrdt(float partCrdt) {
        this.partCrdt = partCrdt;
    }

    public String getWefDate() {
        return wefDate;
    }

    public void setWefDate(String wefDate) {
        this.wefDate = wefDate;
    }

    public void setAnnualRound(String annualRound) {
        this.annualRound = annualRound;
    }

    public String getAnnualRound() {
        return annualRound;
    }

    public void setWhen_credit(String when_credit) {
        this.when_credit = when_credit;
    }

    public String getWhen_credit() {
        return when_credit;
    }
}
