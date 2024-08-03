/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.master;

/**
 *
 * @author Manas Jena
 */
public class State {
    private String statecode;
    private String statename;
    private String hidstateCode;
    private String countryName;

    
    
    
    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }
    
    public String getHidstateCode() {
        return hidstateCode;
    }

    public void setHidstateCode(String hidstateCode) {
        this.hidstateCode = hidstateCode;
    }
       
    public String getStatecode() {
        return statecode;
    }

    public void setStatecode(String statecode) {
        this.statecode = statecode;
    }

    public String getStatename() {
        return statename;
    }

    public void setStatename(String statename) {
        this.statename = statename;
    }    
}
