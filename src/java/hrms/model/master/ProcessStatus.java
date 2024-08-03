/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.master;

import java.io.Serializable;

/**
 *
 * @author Surendra
 */
public class ProcessStatus implements Serializable {

    private int statusid;

    private String statusname;

    private GworkFlowProcess gworklow;

    private String iscompleted;

    private String isimportant;

    public int getStatusid() {
        return statusid;
    }

    public void setStatusid(int statusid) {
        this.statusid = statusid;
    }

    public String getStatusname() {
        return statusname;
    }

    public void setStatusname(String statusname) {
        this.statusname = statusname;
    }

    public GworkFlowProcess getGworklow() {
        return gworklow;
    }

    public void setGworklow(GworkFlowProcess gworklow) {
        this.gworklow = gworklow;
    }

    public String getIscompleted() {
        return iscompleted;
    }

    public void setIscompleted(String iscompleted) {
        this.iscompleted = iscompleted;
    }

    public String getIsimportant() {
        return isimportant;
    }

    public void setIsimportant(String isimportant) {
        this.isimportant = isimportant;
    }

}
