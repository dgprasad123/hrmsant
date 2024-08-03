/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.leave;

import java.io.Serializable;

/**
 *
 * @author Manoj PC
 */
public class LSOrderType implements Serializable {

    String lsotid;
    String lsot;

    public String getLsotid() {
        return lsotid;
    }

    public void setLsotid(String lsotid) {
        this.lsotid = lsotid;
    }

    public String getLsot() {
        return lsot;
    }

    public void setLsot(String lsot) {
        this.lsot = lsot;
    }
}
