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
public class ParUnReviewedDetailBean {
     private int logId;
    private String unReviewedOn;
    private String unReviewedReason;
    private String unReviewedBy;
    private int parId;

    public int getLogId() {
        return logId;
    }

    public void setLogId(int logId) {
        this.logId = logId;
    }

    public String getUnReviewedOn() {
        return unReviewedOn;
    }

    public void setUnReviewedOn(String unReviewedOn) {
        this.unReviewedOn = unReviewedOn;
    }

    public String getUnReviewedReason() {
        return unReviewedReason;
    }

    public void setUnReviewedReason(String unReviewedReason) {
        this.unReviewedReason = unReviewedReason;
    }

    public String getUnReviewedBy() {
        return unReviewedBy;
    }

    public void setUnReviewedBy(String unReviewedBy) {
        this.unReviewedBy = unReviewedBy;
    }

    public int getParId() {
        return parId;
    }

    public void setParId(int parId) {
        this.parId = parId;
    }
    
    
}
