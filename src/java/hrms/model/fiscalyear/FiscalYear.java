package hrms.model.fiscalyear;

import java.io.Serializable;

public class FiscalYear implements Serializable {

    private String fy;

    private String active;

    private String isclosed;

    public String getFy() {
        return fy;
    }

    public void setFy(String fy) {
        this.fy = fy;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getIsclosed() {
        return isclosed;
    }

    public void setIsclosed(String isclosed) {
        this.isclosed = isclosed;
    }
}
