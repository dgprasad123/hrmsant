package hrms.model.master;

import java.io.Serializable;

public class G_Leave_Master implements Serializable {

    private String tolid;

    private String tol;

    private String ifgeneral;

    private String ifvacjud;

    private String ifvacoth;

    private String ifaelapp;

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

    public String getIfgeneral() {
        return ifgeneral;
    }

    public void setIfgeneral(String ifgeneral) {
        this.ifgeneral = ifgeneral;
    }

    public String getIfvacjud() {
        return ifvacjud;
    }

    public void setIfvacjud(String ifvacjud) {
        this.ifvacjud = ifvacjud;
    }

    public String getIfvacoth() {
        return ifvacoth;
    }

    public void setIfvacoth(String ifvacoth) {
        this.ifvacoth = ifvacoth;
    }

    public String getIfaelapp() {
        return ifaelapp;
    }

    public void setIfaelapp(String ifaelapp) {
        this.ifaelapp = ifaelapp;
    }
}
