/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model;

import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Manas
 */
public class WaterRent {
    private String hrmsid = null;
    private String gpfno = null;
    private int wtax = 0;
    private int swtax = 0;
    private String tvno = null;
    private String tvdate = null;
    private String fname = null;
    private String mname = null;
    private String lname = null;
    private String mobile = null;
    private String quarterNo = null;
    private String consumerNo = null;
    private String recoverymonth = null;
    private String recoveryyear = null;    
    private String qrtrtype = null;
    private String qrtrunit = null;
    private String ddocode = null;
    private String officename = null;
    private String bldgno = null;
    private String dateofallotment = null;

    public String getHrmsid() {
        return hrmsid;
    }
    
    public void setHrmsid(String hrmsid) {
        this.hrmsid = hrmsid;
    }
    public String getFullName() {
        return StringUtils.defaultIfEmpty(this.fname, "")+" "+StringUtils.defaultIfEmpty(this.mname, "")+" "+StringUtils.defaultIfEmpty(this.lname, "");
    }
    public String getGpfno() {
        return gpfno;
    }

    public void setGpfno(String gpfno) {
        this.gpfno = gpfno;
    }

    public int getWtax() {
        return wtax;
    }

    public void setWtax(int wtax) {
        this.wtax = wtax;
    }

    public int getSwtax() {
        return swtax;
    }

    public void setSwtax(int swtax) {
        this.swtax = swtax;
    }

    public String getTvno() {
        return tvno;
    }

    public void setTvno(String tvno) {
        this.tvno = tvno;
    }

    public String getTvdate() {
        return tvdate;
    }

    public void setTvdate(String tvdate) {
        this.tvdate = tvdate;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getMname() {
        return mname;
    }

    public void setMname(String mname) {
        this.mname = mname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getQuarterNo() {
        return quarterNo;
    }

    public void setQuarterNo(String quarterNo) {
        this.quarterNo = quarterNo;
    }

    public String getConsumerNo() {
        return consumerNo;
    }

    public void setConsumerNo(String consumerNo) {
        this.consumerNo = consumerNo;
    }

    public String getRecoverymonth() {
        return recoverymonth;
    }

    public void setRecoverymonth(String recoverymonth) {
        this.recoverymonth = recoverymonth;
    }

    public String getRecoveryyear() {
        return recoveryyear;
    }

    public void setRecoveryyear(String recoveryyear) {
        this.recoveryyear = recoveryyear;
    }

    public String getQrtrtype() {
        return qrtrtype;
    }

    public void setQrtrtype(String qrtrtype) {
        this.qrtrtype = qrtrtype;
    }

    public String getQrtrunit() {
        return qrtrunit;
    }

    public void setQrtrunit(String qrtrunit) {
        this.qrtrunit = qrtrunit;
    }

    public String getDdocode() {
        return ddocode;
    }

    public void setDdocode(String ddocode) {
        this.ddocode = ddocode;
    }

    public String getBldgno() {
        return bldgno;
    }

    public void setBldgno(String bldgno) {
        this.bldgno = bldgno;
    }

    public String getDateofallotment() {
        return dateofallotment;
    }

    public void setDateofallotment(String dateofallotment) {
        this.dateofallotment = dateofallotment;
    }

    public String getOfficename() {
        return officename;
    }

    public void setOfficename(String officename) {
        this.officename = officename;
    }
    
    
}
