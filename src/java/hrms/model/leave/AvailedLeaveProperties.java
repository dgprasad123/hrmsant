

package hrms.model.leave;

public class AvailedLeaveProperties {
   	private String fromDate = null;
	private String toDate = null;
	private String availedLeaveNum = null;
	private String leaveBal = null;

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

    public String getAvailedLeaveNum() {
        return availedLeaveNum;
    }

    public void setAvailedLeaveNum(String availedLeaveNum) {
        this.availedLeaveNum = availedLeaveNum;
    }

    public String getLeaveBal() {
        return leaveBal;
    }

    public void setLeaveBal(String leaveBal) {
        this.leaveBal = leaveBal;
    }
        
}
