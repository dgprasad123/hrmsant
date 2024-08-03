package hrms.model.payroll;

import org.apache.commons.lang.builder.ToStringBuilder;

public class SalaryBenefitiaryDetails {

    private String hrms_no = "";
    private String hrms_date = "";
    private String bank_ifsc_code = "";
    private String benf_name = "";
    private String benf_address = "";
    private String ben_acct_no = "";
    private String account_type = "";

    private String email_id = "";
    private double amount = 0;
    private String mobile_number = "";
    private String gpf_series = "";
    private String gpf_number = "";
    private String pran = "";
    private String employeeId = "";
    
    private String beneficiaryType;
    
    private String gpfAcctNo;
    private String ifGpfAssumed;
    private String empAcctType;
    private String ifMaintenanceDeduct;
    
    private String ddoCode;
    
    private int gross;
    private int dedn;
    private int pvtDedn;
    
    private String ifStopPayNps;
    private String isRegular;
    private String empNonPran;
    private String ifReengaged;
    private String lawLevel;
    private String usertype;
    private String paycell;
    private String paylevel;
    private String paycomm;
    private String payscale;
    private String empType;
    
    public String getHrms_no() {
        return hrms_no;
    }

    public void setHrms_no(String hrms_no) {
        this.hrms_no = hrms_no;
    }

    public String getHrms_date() {
        return hrms_date;
    }

    public void setHrms_date(String hrms_date) {
        this.hrms_date = hrms_date;
    }

    public String getBank_ifsc_code() {
        return bank_ifsc_code;
    }

    public void setBank_ifsc_code(String bank_ifsc_code) {
        this.bank_ifsc_code = bank_ifsc_code;
    }

    public String getBenf_name() {
        return benf_name;
    }

    public void setBenf_name(String benf_name) {
        this.benf_name = benf_name;
    }

    public String getBenf_address() {
        return benf_address;
    }

    public void setBenf_address(String benf_address) {
        this.benf_address = benf_address;
    }

    public String getBen_acct_no() {
        return ben_acct_no;
    }

    public void setBen_acct_no(String ben_acct_no) {
        this.ben_acct_no = ben_acct_no;
    }

    public String getEmail_id() {
        return email_id;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }

    public String getGpf_series() {
        return gpf_series;
    }

    public void setGpf_series(String gpf_series) {
        this.gpf_series = gpf_series;
    }

    public String getGpf_number() {
        return gpf_number;
    }

    public void setGpf_number(String gpf_number) {
        this.gpf_number = gpf_number;
    }

    public String getPran() {
        return pran;
    }

    public void setPran(String pran) {
        this.pran = pran;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public String getAccount_type() {
        return account_type;
    }

    public void setAccount_type(String account_type) {
        this.account_type = account_type;
    }

    public String getBeneficiaryType() {
        return beneficiaryType;
    }

    public void setBeneficiaryType(String beneficiaryType) {
        this.beneficiaryType = beneficiaryType;
    }

    public String getGpfAcctNo() {
        return gpfAcctNo;
    }

    public void setGpfAcctNo(String gpfAcctNo) {
        this.gpfAcctNo = gpfAcctNo;
    }

    public String getIfGpfAssumed() {
        return ifGpfAssumed;
    }

    public void setIfGpfAssumed(String ifGpfAssumed) {
        this.ifGpfAssumed = ifGpfAssumed;
    }

    public String getEmpAcctType() {
        return empAcctType;
    }

    public void setEmpAcctType(String empAcctType) {
        this.empAcctType = empAcctType;
    }

    public String getIfMaintenanceDeduct() {
        return ifMaintenanceDeduct;
    }

    public void setIfMaintenanceDeduct(String ifMaintenanceDeduct) {
        this.ifMaintenanceDeduct = ifMaintenanceDeduct;
    }

    public String getDdoCode() {
        return ddoCode;
    }

    public void setDdoCode(String ddoCode) {
        this.ddoCode = ddoCode;
    }

    public int getGross() {
        return gross;
    }

    public void setGross(int gross) {
        this.gross = gross;
    }

    public int getDedn() {
        return dedn;
    }

    public void setDedn(int dedn) {
        this.dedn = dedn;
    }

    public int getPvtDedn() {
        return pvtDedn;
    }

    public void setPvtDedn(int pvtDedn) {
        this.pvtDedn = pvtDedn;
    }

    public String getIfStopPayNps() {
        return ifStopPayNps;
    }

    public void setIfStopPayNps(String ifStopPayNps) {
        this.ifStopPayNps = ifStopPayNps;
    }

    public String getIsRegular() {
        return isRegular;
    }

    public void setIsRegular(String isRegular) {
        this.isRegular = isRegular;
    }

    public String getEmpNonPran() {
        return empNonPran;
    }

    public void setEmpNonPran(String empNonPran) {
        this.empNonPran = empNonPran;
    }

    public String getIfReengaged() {
        return ifReengaged;
    }

    public void setIfReengaged(String ifReengaged) {
        this.ifReengaged = ifReengaged;
    }

    public String getLawLevel() {
        return lawLevel;
    }

    public void setLawLevel(String lawLevel) {
        this.lawLevel = lawLevel;
    }

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

    public String getPaycell() {
        return paycell;
    }

    public void setPaycell(String paycell) {
        this.paycell = paycell;
    }

    public String getPaylevel() {
        return paylevel;
    }

    public void setPaylevel(String paylevel) {
        this.paylevel = paylevel;
    }

    public String getPaycomm() {
        return paycomm;
    }

    public void setPaycomm(String paycomm) {
        this.paycomm = paycomm;
    }

    public String getPayscale() {
        return payscale;
    }

    public void setPayscale(String payscale) {
        this.payscale = payscale;
    }

    public String getEmpType() {
        return empType;
    }

    public void setEmpType(String empType) {
        this.empType = empType;
    }
    
    
}
