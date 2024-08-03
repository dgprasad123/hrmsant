/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.master;

import java.io.Serializable;

/**
 *
 * @author Durga
 */
public class Payscale implements Serializable {

    private String payscale;
    private int gp;
    private double amt;
    private String contYear;

    public String getPayscale() {
        return payscale;
    }

    public void setPayscale(String payscale) {
        this.payscale = payscale;
    }

    public int getGp() {
        return gp;
    }

    public void setGp(int gp) {
        this.gp = gp;
    }

    public double getAmt() {
        return amt;
    }

    public void setAmt(double amt) {
        this.amt = amt;
    }

    public String getContYear() {
        return contYear;
    }

    public void setContYear(String contYear) {
        this.contYear = contYear;
    }
    
}
