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
public class CreditLeaveBean {
    private String creId;
    private String fromdate;
    private String todate;
    private String tolid;
    private String tol;
    private String compMonths;
    private String creleave;

    public String getCreId() {
        return creId;
    }

    public void setCreId(String creId) {
        this.creId = creId;
    }

    public String getCompMonths() {
        return compMonths;
    }

    public void setCompMonths(String compMonths) {
        this.compMonths = compMonths;
    }

    public String getCreleave() {
        return creleave;
    }

    public void setCreleave(String creleave) {
        this.creleave = creleave;
    }

    public String getFromdate() {
        return fromdate;
    }

    public void setFromdate(String fromdate) {
        this.fromdate = fromdate;
    }

    public String getTodate() {
        return todate;
    }

    public void setTodate(String todate) {
        this.todate = todate;
    }

    public String getTolid() {
        return tolid;
    }

    public void setTolid(String tolid) {
        this.tolid = tolid;
    }

    public String getTol() {
        return tol;
    }

    public void setTol(String tol) {
        this.tol = tol;
    }
    
    
}
