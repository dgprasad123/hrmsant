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
public class Range {

    private String fromDt;
    private String toDate;
    private String completeMon;
    private String noofLeave;

    public String getCompleteMon() {
        return completeMon;
    }

    public void setCompleteMon(String completeMon) {
        this.completeMon = completeMon;
    }

    public String getNoofLeave() {
        return noofLeave;
    }

    public void setNoofLeave(String noofLeave) {
        this.noofLeave = noofLeave;
    }

    public String getFromDt() {
        return fromDt;
    }

    public void setFromDt(String fromDt) {
        this.fromDt = fromDt;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }
}
