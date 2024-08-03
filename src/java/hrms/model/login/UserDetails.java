/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.login;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Surendra
 */
public class UserDetails implements Serializable {

    private int userid;
    private String username;
    private String password;
    private String usertype;
    private String linkid;
    private int enable;
    private int accountnonexpired;
    private int accountnonlocked;
    private int credentialsnonexpired;
    private Date lastloginfailed;
    private Date lastlogin;
    private int noofloginfailed;
    private String message;
    private String depCode;
    private String forceReset;
    
    private String otp;
    private String mobile;
    
    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

    public String getLinkid() {
        return linkid;
    }

    public void setLinkid(String linkid) {
        this.linkid = linkid;
    }

    public int getEnable() {
        return enable;
    }

    public void setEnable(int enable) {
        this.enable = enable;
    }

    public int getAccountnonexpired() {
        return accountnonexpired;
    }

    public void setAccountnonexpired(int accountnonexpired) {
        this.accountnonexpired = accountnonexpired;
    }

    public int getAccountnonlocked() {
        return accountnonlocked;
    }

    public void setAccountnonlocked(int accountnonlocked) {
        this.accountnonlocked = accountnonlocked;
    }

    public int getCredentialsnonexpired() {
        return credentialsnonexpired;
    }

    public void setCredentialsnonexpired(int credentialsnonexpired) {
        this.credentialsnonexpired = credentialsnonexpired;
    }

    public Date getLastloginfailed() {
        return lastloginfailed;
    }

    public void setLastloginfailed(Date lastloginfailed) {
        this.lastloginfailed = lastloginfailed;
    }

    public Date getLastlogin() {
        return lastlogin;
    }

    public void setLastlogin(Date lastlogin) {
        this.lastlogin = lastlogin;
    }

    public int getNoofloginfailed() {
        return noofloginfailed;
    }

    public void setNoofloginfailed(int noofloginfailed) {
        this.noofloginfailed = noofloginfailed;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDepCode() {
        return depCode;
    }

    public void setDepCode(String depCode) {
        this.depCode = depCode;
    }

    public String getForceReset() {
        return forceReset;
    }

    public void setForceReset(String forceReset) {
        this.forceReset = forceReset;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
