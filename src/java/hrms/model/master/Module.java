/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.master;

import java.io.Serializable;

/**
 *
 * @author Manas Jena
 */
public class Module implements Serializable{
    private int modid;
    private String username;
    private String roleid;
    private String modgrpid;
    private String modname;
    private String modurl;
    private String empspecific;
    private String converturl;
    private int modserial;
    private String newwindow;
    private String helpurl;
    private String moduletype;
    private String hideurl;
    private int privmapid;
    private String isNew;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    
    public int getModid() {
        return modid;
    }

    public void setModid(int modid) {
        this.modid = modid;
    }

    public String getRoleid() {
        return roleid;
    }

    public void setRoleid(String roleid) {
        this.roleid = roleid;
    }

    public String getModgrpid() {
        return modgrpid;
    }

    public void setModgrpid(String modgrpid) {
        this.modgrpid = modgrpid;
    }

    public String getModname() {
        return modname;
    }

    public void setModname(String modname) {
        this.modname = modname;
    }

    public String getModurl() {
        return modurl;
    }

    public void setModurl(String modurl) {
        this.modurl = modurl;
    }

    public String getEmpspecific() {
        return empspecific;
    }

    public void setEmpspecific(String empspecific) {
        this.empspecific = empspecific;
    }

    public String getConverturl() {
        return converturl;
    }

    public void setConverturl(String converturl) {
        this.converturl = converturl;
    }

    public int getModserial() {
        return modserial;
    }

    public void setModserial(int modserial) {
        this.modserial = modserial;
    }

    public String getNewwindow() {
        return newwindow;
    }

    public void setNewwindow(String newwindow) {
        this.newwindow = newwindow;
    }

    public String getHelpurl() {
        return helpurl;
    }

    public void setHelpurl(String helpurl) {
        this.helpurl = helpurl;
    }

    public String getModuletype() {
        return moduletype;
    }

    public void setModuletype(String moduletype) {
        this.moduletype = moduletype;
    }

    public String getHideurl() {
        return hideurl;
    }

    public void setHideurl(String hideurl) {
        this.hideurl = hideurl;
    }

    public int getPrivmapid() {
        return privmapid;
    }

    public void setPrivmapid(int privmapid) {
        this.privmapid = privmapid;
    }

    public String getIsNew() {
        return isNew;
    }

    public void setIsNew(String isNew) {
        this.isNew = isNew;
    }
    
    
}
