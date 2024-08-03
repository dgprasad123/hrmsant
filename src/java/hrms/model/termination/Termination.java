
package hrms.model.termination;

public class Termination {
    private String retid = null;
    private String notId = null;
    private String empid = null;
    private String retiredEmpid = null;
    private String curspc = null;
    private String notType = null;
    private String authtyp = null;
    private String doe = null;
    private String toe = null;
    private String ifAssumed = null;
    private String ordno = null;
    private String ordDate = null;
    private String authDeptCode = null;
    private String authOfficeCode = null;
    private String authSpc = null;
    private String note = null;
    private String regtype = null;
    private String retauthtyp = null;
    private String ifVisible = null;
    private String entDeptCode = null;
    private String entOfficeCode = null;
    private String entAuthCode = null;
    private String cadreStatus = null;
    private String subStatus = null;
    private String retiredfromdeptCode;
    private String retiredfromofficeCode;
    private String retiredfromspc;
    private String duedate;
    private String retired;
    private String sltDept=null;
    private String sltOffice=null;
    private String sltAuth=null;
    private String entDept=null;
    private String entOffice=null;
    private String entAuth=null;
    
    private String rdTransaction;
    
    public String getRetid() {
        return retid;
    }

    public void setRetid(String retid) {
        this.retid = retid;
    }

    public String getRegtype() {
        return regtype;
    }

    public void setRegtype(String regtype) {
        this.regtype = regtype;
    }
    
    
    public String getNotId() {
        return notId;
    }

    public void setNotId(String notId) {
        this.notId = notId;
    }

    public String getRetiredEmpid() {
        return retiredEmpid;
    }

    public void setRetiredEmpid(String retiredEmpid) {
        this.retiredEmpid = retiredEmpid;
    }    

    public String getCurspc() {
        return curspc;
    }

    public void setCurspc(String curspc) {
        this.curspc = curspc;
    }

    public String getNotType() {
        return notType;
    }

    public void setNotType(String notType) {
        this.notType = notType;
    }

    public String getAuthtyp() {
        return authtyp;
    }

    public void setAuthtyp(String authtyp) {
        this.authtyp = authtyp;
    }    

    public String getDoe() {
        return doe;
    }

    public void setDoe(String doe) {
        this.doe = doe;
    }

    public String getToe() {
        return toe;
    }

    public void setToe(String toe) {
        this.toe = toe;
    }

    public String getIfAssumed() {
        return ifAssumed;
    }

    public void setIfAssumed(String ifAssumed) {
        this.ifAssumed = ifAssumed;
    }

    public String getOrdno() {
        return ordno;
    }

    public void setOrdno(String ordno) {
        this.ordno = ordno;
    }

    public String getOrdDate() {
        return ordDate;
    }

    public void setOrdDate(String ordDate) {
        this.ordDate = ordDate;
    }

    public String getAuthDeptCode() {
        return authDeptCode;
    }

    public void setAuthDeptCode(String authDeptCode) {
        this.authDeptCode = authDeptCode;
    }

    public String getAuthOfficeCode() {
        return authOfficeCode;
    }

    public void setAuthOfficeCode(String authOfficeCode) {
        this.authOfficeCode = authOfficeCode;
    }

    public String getAuthSpc() {
        return authSpc;
    }

    public void setAuthSpc(String authSpc) {
        this.authSpc = authSpc;
    }       

    public String getRetauthtyp() {
        return retauthtyp;
    }

    public void setRetauthtyp(String retauthtyp) {
        this.retauthtyp = retauthtyp;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getIfVisible() {
        return ifVisible;
    }

    public void setIfVisible(String ifVisible) {
        this.ifVisible = ifVisible;
    }

    public String getEntDeptCode() {
        return entDeptCode;
    }

    public void setEntDeptCode(String entDeptCode) {
        this.entDeptCode = entDeptCode;
    }

    public String getEntOfficeCode() {
        return entOfficeCode;
    }

    public void setEntOfficeCode(String entOfficeCode) {
        this.entOfficeCode = entOfficeCode;
    }

    public String getEntAuthCode() {
        return entAuthCode;
    }

    public void setEntAuthCode(String entAuthCode) {
        this.entAuthCode = entAuthCode;
    }

    public String getCadreStatus() {
        return cadreStatus;
    }

    public void setCadreStatus(String cadreStatus) {
        this.cadreStatus = cadreStatus;
    }

    public String getSubStatus() {
        return subStatus;
    }

    public void setSubStatus(String subStatus) {
        this.subStatus = subStatus;
    }

    public String getRetiredfromdeptCode() {
        return retiredfromdeptCode;
    }

    public void setRetiredfromdeptCode(String retiredfromdeptCode) {
        this.retiredfromdeptCode = retiredfromdeptCode;
    }

    public String getRetiredfromofficeCode() {
        return retiredfromofficeCode;
    }

    public void setRetiredfromofficeCode(String retiredfromofficeCode) {
        this.retiredfromofficeCode = retiredfromofficeCode;
    }

    public String getRetiredfromspc() {
        return retiredfromspc;
    }

    public void setRetiredfromspc(String retiredfromspc) {
        this.retiredfromspc = retiredfromspc;
    }

    public String getDuedate() {
        return duedate;
    }

    public void setDuedate(String duedate) {
        this.duedate = duedate;
    }

    public String getRetired() {
        return retired;
    }

    public void setRetired(String retired) {
        this.retired = retired;
    }

    public String getSltDept() {
        return sltDept;
    }

    public void setSltDept(String sltDept) {
        this.sltDept = sltDept;
    }

    public String getSltOffice() {
        return sltOffice;
    }

    public void setSltOffice(String sltOffice) {
        this.sltOffice = sltOffice;
    }

    public String getSltAuth() {
        return sltAuth;
    }

    public void setSltAuth(String sltAuth) {
        this.sltAuth = sltAuth;
    }

    public String getEntDept() {
        return entDept;
    }

    public void setEntDept(String entDept) {
        this.entDept = entDept;
    }

    public String getEntOffice() {
        return entOffice;
    }

    public void setEntOffice(String entOffice) {
        this.entOffice = entOffice;
    }

    public String getEntAuth() {
        return entAuth;
    }

    public void setEntAuth(String entAuth) {
        this.entAuth = entAuth;
    }

    public String getEmpid() {
        return empid;
    }

    public void setEmpid(String empid) {
        this.empid = empid;
    }

    public String getRdTransaction() {
        return rdTransaction;
    }

    public void setRdTransaction(String rdTransaction) {
        this.rdTransaction = rdTransaction;
    }
}
