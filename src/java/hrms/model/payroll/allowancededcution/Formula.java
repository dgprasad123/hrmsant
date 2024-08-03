/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.model.payroll.allowancededcution;

/**
 *
 * @author Manas
 */
public class Formula {
    private int fid;
    private int ad_code;
    private String formulaName;
    private String orgformula;
    private String label;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getFid() {
        return fid;
    }

    public void setFid(int fid) {
        this.fid = fid;
    }

    public int getAd_code() {
        return ad_code;
    }

    public void setAd_code(int ad_code) {
        this.ad_code = ad_code;
    }

    public String getFormulaName() {
        return formulaName;
    }

    public void setFormulaName(String formulaName) {
        this.formulaName = formulaName;
    }

    public String getOrgformula() {
        return orgformula;
    }

    public void setOrgformula(String orgformula) {
        this.orgformula = orgformula;
    }
    
    
}
