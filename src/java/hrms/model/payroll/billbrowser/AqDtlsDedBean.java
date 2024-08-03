/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.payroll.billbrowser;

/**
 *
 * @author lenovo pc
 */
public class AqDtlsDedBean {

    private String adCode = null;
    private String dedInstalNo = null;
    private String dedAmt = null;
    private String nowDedn = null;
    private String billNo = null;
    private String aqslNo = null;
    private String adType = null;
    private String dedType = null;
    private String adRefId = null;
    private String refDesc = null;
    private String totRecAmt = null;
    private String dedInstalCount=null;
    private String policyNo=null;

    public String getPolicyNo() {
        return policyNo;
    }

    public void setPolicyNo(String policyNo) {
        this.policyNo = policyNo;
    }
    

    public String getDedInstalCount() {
        return dedInstalCount;
    }

    public void setDedInstalCount(String dedInstalCount) {
        this.dedInstalCount = dedInstalCount;
    }
   

    public String getRefDesc() {
        return refDesc;
    }

    public void setRefDesc(String refDesc) {
        this.refDesc = refDesc;
    }

    public String getDedType() {
        return dedType;
    }

    public void setDedType(String dedType) {
        this.dedType = dedType;
    }

    public String getAdRefId() {
        return adRefId;
    }

    public void setAdRefId(String adRefId) {
        this.adRefId = adRefId;
    }

    public String getAdCode() {
        return adCode;
    }

    public void setAdCode(String adCode) {
        this.adCode = adCode;
    }

    public String getAdType() {
        return adType;
    }

    public void setAdType(String adType) {
        this.adType = adType;
    }

    public String getNowDedn() {
        return nowDedn;
    }

    public void setNowDedn(String nowDedn) {
        this.nowDedn = nowDedn;
    }

    public String getAqslNo() {
        return aqslNo;
    }

    public void setAqslNo(String aqslNo) {
        this.aqslNo = aqslNo;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getDedInstalNo() {
        return dedInstalNo;
    }

    public void setDedInstalNo(String dedInstalNo) {
        this.dedInstalNo = dedInstalNo;
    }

    public String getDedAmt() {
        return dedAmt;
    }

    public void setDedAmt(String dedAmt) {
        this.dedAmt = dedAmt;
    }

    public String getTotRecAmt() {
        return totRecAmt;
    }

    public void setTotRecAmt(String totRecAmt) {
        this.totRecAmt = totRecAmt;
    }
}
