/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.employee;

/**
 *
 * @author Manas Jena
 */
public class EmployeeLanguage {
    private String language;
    private String ifread;
    private String ifwrite;
    private String ifspeak;
    private String ifmlang;
    
    private int slno;
    
    private String isLocked;
    
    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getIfread() {
        return ifread;
    }

    public void setIfread(String ifread) {
        this.ifread = ifread;
    }

    public String getIfwrite() {
        return ifwrite;
    }

    public void setIfwrite(String ifwrite) {
        this.ifwrite = ifwrite;
    }

    public String getIfspeak() {
        return ifspeak;
    }

    public void setIfspeak(String ifspeak) {
        this.ifspeak = ifspeak;
    }

    public String getIfmlang() {
        return ifmlang;
    }

    public void setIfmlang(String ifmlang) {
        this.ifmlang = ifmlang;
    }

    public int getSlno() {
        return slno;
    }

    public void setSlno(int slno) {
        this.slno = slno;
    }


    public String getIsLocked() {
        return isLocked;
    }

    public void setIsLocked(String isLocked) {
        this.isLocked = isLocked;
    }
    
}
