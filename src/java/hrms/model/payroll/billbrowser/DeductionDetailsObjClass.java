/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.payroll.billbrowser;

/**
 *
 * @author Manas
 */
public class DeductionDetailsObjClass {

    private String deduction = null;
    private String accNo = null;
    private String deductionFor = null;
    private String amount = null;
    private String hidDeductionAqSlno = null;
    private String totalDeduction = null;
    private String nowdedn = null;
    private String adrefId = "";
    private int noofInstal = 0;
    private String dedType = null;
    private String deductionName = null;

    public String getDeductionName() {
        return deductionName;
    }

    public void setDeductionName(String deductionName) {
        this.deductionName = deductionName;
    }
    

    public String getDedType() {
        return dedType;
    }

    public void setDedType(String dedType) {
        this.dedType = dedType;
    }

    public String getDeduction() {
        return deduction;
    }

    public void setDeduction(String deduction) {
        this.deduction = deduction;
    }

    public String getAccNo() {
        return accNo;
    }

    public void setAccNo(String accNo) {
        this.accNo = accNo;
    }

    public String getDeductionFor() {
        return deductionFor;
    }

    public void setDeductionFor(String deductionFor) {
        this.deductionFor = deductionFor;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getHidDeductionAqSlno() {
        return hidDeductionAqSlno;
    }

    public void setHidDeductionAqSlno(String hidDeductionAqSlno) {
        this.hidDeductionAqSlno = hidDeductionAqSlno;
    }

    public String getTotalDeduction() {
        return totalDeduction;
    }

    public void setTotalDeduction(String totalDeduction) {
        this.totalDeduction = totalDeduction;
    }

    public String getNowdedn() {
        return nowdedn;
    }

    public void setNowdedn(String nowdedn) {
        this.nowdedn = nowdedn;
    }

    public String getAdrefId() {
        return adrefId;
    }

    public void setAdrefId(String adrefId) {
        this.adrefId = adrefId;
    }

    public int getNoofInstal() {
        return noofInstal;
    }

    public void setNoofInstal(int noofInstal) {
        this.noofInstal = noofInstal;
    }

}
