package hrms.model.loanrelease;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Controller;

public class LoanRelease {

    private String loggedInUserSpc = null;
    private String empPost = null;
    private String hidPageno = null;
    private String mode = "E";
    private String txtEid = null;
    private String txtGPF = null;
    private String txtEmpName = null;
    private String txtreorderno = null;
    private String txtreorderdt = null;
    private String txtrnote = null;
    private String sltdept = null;
    private String sltoffice = null;
    private String sltauth = null;
    private List deptArray = null;
    private List offArray = null;
    private List authArray = null;

    private List repayArray = null;
    private int hnid;
    private String hrepid = null;
    private String txtamount = null;
    private String txtnote = null;
    private String txtdoe = null;
    private String radfull = null;
    private String radinstall = null;
    private String txtinstno = null;
    private String ptype = null;
    private String sltloan = null;
    private String htrantype = null;
    private List loanTypeArray = null;
    //private SubstansivePost	sp=null;
    private String chknsb = null;
    private String hidSvid = null;

    private String sltedept = null;
    private String slteoffice = null;
    private String slteauth = null;

    private String txtothDeptName = null;
    private String txtothOffName = null;
    private String txtothAuthName = null;
    private String radauthtyp = null;
    private String hidothspc = null;

    private String txteothDeptName = null;
    private String txteothOffName = null;
    private String txteothAuthName = null;
    private String radeauthtyp = null;
    private String hideothspc = null;

    private String entryAuthName = null;
    private String entryOffName = null;
    private String entryDeptName = null;

    private String sancAuthName = null;
    private String sancOffName = null;
    private String sancDeptName = null;
    
    private String hsltdept= null;
    private String hsltoffice= null;
    private String hsltauth= null;
    private String chkNotSBPrint=null;

    public String getHsltdept() {
        return hsltdept;
    }

    public void setHsltdept(String hsltdept) {
        this.hsltdept = hsltdept;
    }

    public String getHsltoffice() {
        return hsltoffice;
    }

    public void setHsltoffice(String hsltoffice) {
        this.hsltoffice = hsltoffice;
    }

    public String getHsltauth() {
        return hsltauth;
    }

    public void setHsltauth(String hsltauth) {
        this.hsltauth = hsltauth;
    }

    public List getDeptArray() {
        return deptArray;
    }

    public void setDeptArray(List deptArray) {
        this.deptArray = deptArray;
    }

    public List getOffArray() {
        return offArray;
    }

    public void setOffArray(List offArray) {
        this.offArray = offArray;
    }

    public List getAuthArray() {
        return authArray;
    }

    public void setAuthArray(List authArray) {
        this.authArray = authArray;
    }

    public String getLoggedInUserSpc() {
        return loggedInUserSpc;
    }

    public void setLoggedInUserSpc(String loggedInUserSpc) {
        this.loggedInUserSpc = loggedInUserSpc;
    }

    public String getEmpPost() {
        return empPost;
    }

    public void setEmpPost(String empPost) {
        this.empPost = empPost;
    }

    public String getHidPageno() {
        return hidPageno;
    }

    public void setHidPageno(String hidPageno) {
        this.hidPageno = hidPageno;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getTxtEid() {
        return txtEid;
    }

    public void setTxtEid(String txtEid) {
        this.txtEid = txtEid;
    }

    public String getTxtGPF() {
        return txtGPF;
    }

    public void setTxtGPF(String txtGPF) {
        this.txtGPF = txtGPF;
    }

    public String getTxtEmpName() {
        return txtEmpName;
    }

    public void setTxtEmpName(String txtEmpName) {
        this.txtEmpName = txtEmpName;
    }

    public String getTxtreorderno() {
        return txtreorderno;
    }

    public void setTxtreorderno(String txtreorderno) {
        this.txtreorderno = txtreorderno;
    }

    public String getTxtreorderdt() {
        return txtreorderdt;
    }

    public void setTxtreorderdt(String txtreorderdt) {
        this.txtreorderdt = txtreorderdt;
    }

    public String getTxtrnote() {
        return txtrnote;
    }

    public void setTxtrnote(String txtrnote) {
        this.txtrnote = txtrnote;
    }

    public String getSltdept() {
        return sltdept;
    }

    public void setSltdept(String sltdept) {
        this.sltdept = sltdept;
    }

    public String getSltoffice() {
        return sltoffice;
    }

    public void setSltoffice(String sltoffice) {
        this.sltoffice = sltoffice;
    }

    public String getSltauth() {
        return sltauth;
    }

    public void setSltauth(String sltauth) {
        this.sltauth = sltauth;
    }

    public void setOffArray(ArrayList offArray) {
        this.offArray = offArray;
    }

    public void setAuthArray(ArrayList authArray) {
        this.authArray = authArray;
    }

    public List getRepayArray() {
        return repayArray;
    }

    public void setRepayArray(List repayArray) {
        this.repayArray = repayArray;
    }

   

    public String getHrepid() {
        return hrepid;
    }

    public void setHrepid(String hrepid) {
        this.hrepid = hrepid;
    }

    public String getTxtamount() {
        return txtamount;
    }

    public void setTxtamount(String txtamount) {
        this.txtamount = txtamount;
    }

    public String getTxtnote() {
        return txtnote;
    }

    public void setTxtnote(String txtnote) {
        this.txtnote = txtnote;
    }

    public String getTxtdoe() {
        return txtdoe;
    }

    public void setTxtdoe(String txtdoe) {
        this.txtdoe = txtdoe;
    }

    public String getRadfull() {
        return radfull;
    }

    public void setRadfull(String radfull) {
        this.radfull = radfull;
    }

    public String getRadinstall() {
        return radinstall;
    }

    public void setRadinstall(String radinstall) {
        this.radinstall = radinstall;
    }

    public String getTxtinstno() {
        return txtinstno;
    }

    public void setTxtinstno(String txtinstno) {
        this.txtinstno = txtinstno;
    }

    public String getPtype() {
        return ptype;
    }

    public void setPtype(String ptype) {
        this.ptype = ptype;
    }

    public String getSltloan() {
        return sltloan;
    }

    public void setSltloan(String sltloan) {
        this.sltloan = sltloan;
    }

    public String getHtrantype() {
        return htrantype;
    }

    public void setHtrantype(String htrantype) {
        this.htrantype = htrantype;
    }

    public List getLoanTypeArray() {
        return loanTypeArray;
    }

    public void setLoanTypeArray(List loanTypeArray) {
        this.loanTypeArray = loanTypeArray;
    }

  

    public String getChknsb() {
        return chknsb;
    }

    public void setChknsb(String chknsb) {
        this.chknsb = chknsb;
    }

    public String getHidSvid() {
        return hidSvid;
    }

    public void setHidSvid(String hidSvid) {
        this.hidSvid = hidSvid;
    }

    public String getSltedept() {
        return sltedept;
    }

    public void setSltedept(String sltedept) {
        this.sltedept = sltedept;
    }

    public String getSlteoffice() {
        return slteoffice;
    }

    public void setSlteoffice(String slteoffice) {
        this.slteoffice = slteoffice;
    }

    public String getSlteauth() {
        return slteauth;
    }

    public void setSlteauth(String slteauth) {
        this.slteauth = slteauth;
    }

    public String getTxtothDeptName() {
        return txtothDeptName;
    }

    public void setTxtothDeptName(String txtothDeptName) {
        this.txtothDeptName = txtothDeptName;
    }

    public String getTxtothOffName() {
        return txtothOffName;
    }

    public void setTxtothOffName(String txtothOffName) {
        this.txtothOffName = txtothOffName;
    }

    public String getTxtothAuthName() {
        return txtothAuthName;
    }

    public void setTxtothAuthName(String txtothAuthName) {
        this.txtothAuthName = txtothAuthName;
    }

    public String getRadauthtyp() {
        return radauthtyp;
    }

    public void setRadauthtyp(String radauthtyp) {
        this.radauthtyp = radauthtyp;
    }

    public String getHidothspc() {
        return hidothspc;
    }

    public void setHidothspc(String hidothspc) {
        this.hidothspc = hidothspc;
    }

    public String getTxteothDeptName() {
        return txteothDeptName;
    }

    public void setTxteothDeptName(String txteothDeptName) {
        this.txteothDeptName = txteothDeptName;
    }

    public String getTxteothOffName() {
        return txteothOffName;
    }

    public void setTxteothOffName(String txteothOffName) {
        this.txteothOffName = txteothOffName;
    }

    public String getTxteothAuthName() {
        return txteothAuthName;
    }

    public void setTxteothAuthName(String txteothAuthName) {
        this.txteothAuthName = txteothAuthName;
    }

    public String getRadeauthtyp() {
        return radeauthtyp;
    }

    public void setRadeauthtyp(String radeauthtyp) {
        this.radeauthtyp = radeauthtyp;
    }

    public String getHideothspc() {
        return hideothspc;
    }

    public void setHideothspc(String hideothspc) {
        this.hideothspc = hideothspc;
    }

    public String getEntryAuthName() {
        return entryAuthName;
    }

    public void setEntryAuthName(String entryAuthName) {
        this.entryAuthName = entryAuthName;
    }

    public String getEntryOffName() {
        return entryOffName;
    }

    public void setEntryOffName(String entryOffName) {
        this.entryOffName = entryOffName;
    }

    public String getEntryDeptName() {
        return entryDeptName;
    }

    public void setEntryDeptName(String entryDeptName) {
        this.entryDeptName = entryDeptName;
    }

    public String getSancAuthName() {
        return sancAuthName;
    }

    public void setSancAuthName(String sancAuthName) {
        this.sancAuthName = sancAuthName;
    }

    public String getSancOffName() {
        return sancOffName;
    }

    public void setSancOffName(String sancOffName) {
        this.sancOffName = sancOffName;
    }

    public String getSancDeptName() {
        return sancDeptName;
    }

    public void setSancDeptName(String sancDeptName) {
        this.sancDeptName = sancDeptName;
    }

    public int getHnid() {
        return hnid;
    }

    public void setHnid(int hnid) {
        this.hnid = hnid;
    }

    public String getChkNotSBPrint() {
        return chkNotSBPrint;
    }

    public void setChkNotSBPrint(String chkNotSBPrint) {
        this.chkNotSBPrint = chkNotSBPrint;
    }
    

}
