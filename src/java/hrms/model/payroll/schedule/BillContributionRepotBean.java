/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.payroll.schedule;

public class BillContributionRepotBean extends ScheduleHelper {

    private String billDesc = null;
    private String billDate = null;
    private String ddoName = null;
    private String ddoRegdNo = null;
    private String dtoRegdNo = null;
    private String aqMonth = null;
    private String billMonth = null;
    private String billYear = null;
    private String offName = null;
    private String billNo = null;
    private String annexure = null;
    private String treasuryName = null;

    private String cpfPlusGcpf = null;
    private String totCpfPlusGcpf = null;

    private String total = null;
    private String totalAnnexure3 = null;
    private String arrInstalment = null;
    private String arrearAmt = null;
    private String totCpf = null;
    private String totGcpf = null;
    private String totCpfWord = null;
    private String totGcpfWord = null;
    private String grandTotal = "";

    private String pagebreakAnx = null;
    private String pageHeaderAnx = null;
    private String cpfCaryFrd = null;
    private String totCaryFrd = null;

    private String anx2CaryFrd = null;
    private String anx3CaryFrd = null;
    
    private String arrearGAmt = null;
    
    public String getBillDesc() {
        return billDesc;
    }

    public void setBillDesc(String billDesc) {
        this.billDesc = billDesc;
    }

    public String getBillDate() {
        return billDate;
    }

    public void setBillDate(String billDate) {
        this.billDate = billDate;
    }

    public String getDdoName() {
        return ddoName;
    }

    public void setDdoName(String ddoName) {
        this.ddoName = ddoName;
    }

    public String getDdoRegdNo() {
        return ddoRegdNo;
    }

    public void setDdoRegdNo(String ddoRegdNo) {
        this.ddoRegdNo = ddoRegdNo;
    }

    public String getDtoRegdNo() {
        return dtoRegdNo;
    }

    public void setDtoRegdNo(String dtoRegdNo) {
        this.dtoRegdNo = dtoRegdNo;
    }

    public String getAqMonth() {
        return aqMonth;
    }

    public void setAqMonth(String aqMonth) {
        this.aqMonth = aqMonth;
    }

    public String getBillMonth() {
        return billMonth;
    }

    public void setBillMonth(String billMonth) {
        this.billMonth = billMonth;
    }

    public String getBillYear() {
        return billYear;
    }

    public void setBillYear(String billYear) {
        this.billYear = billYear;
    }

    public String getOffName() {
        return offName;
    }

    public void setOffName(String offName) {
        this.offName = offName;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getAnnexure() {
        return annexure;
    }

    public void setAnnexure(String annexure) {
        this.annexure = annexure;
    }

    public String getTreasuryName() {
        return treasuryName;
    }

    public void setTreasuryName(String treasuryName) {
        this.treasuryName = treasuryName;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getTotalAnnexure3() {
        return totalAnnexure3;
    }

    public void setTotalAnnexure3(String totalAnnexure3) {
        this.totalAnnexure3 = totalAnnexure3;
    }

    public String getArrInstalment() {
        return arrInstalment;
    }

    public void setArrInstalment(String arrInstalment) {
        this.arrInstalment = arrInstalment;
    }

    public String getArrearAmt() {
        return arrearAmt;
    }

    public void setArrearAmt(String arrearAmt) {
        this.arrearAmt = arrearAmt;
    }

    public String getTotCpf() {
        return totCpf;
    }

    public void setTotCpf(String totCpf) {
        this.totCpf = totCpf;
    }

    public String getTotGcpf() {
        return totGcpf;
    }

    public void setTotGcpf(String totGcpf) {
        this.totGcpf = totGcpf;
    }

    public String getTotCpfWord() {
        return totCpfWord;
    }

    public void setTotCpfWord(String totCpfWord) {
        this.totCpfWord = totCpfWord;
    }

    public String getTotGcpfWord() {
        return totGcpfWord;
    }

    public void setTotGcpfWord(String totGcpfWord) {
        this.totGcpfWord = totGcpfWord;
    }

    public String getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(String grandTotal) {
        this.grandTotal = grandTotal;
    }

    public String getCpfPlusGcpf() {
        return cpfPlusGcpf;
    }

    public void setCpfPlusGcpf(String cpfPlusGcpf) {
        this.cpfPlusGcpf = cpfPlusGcpf;
    }

    public String getTotCpfPlusGcpf() {
        return totCpfPlusGcpf;
    }

    public void setTotCpfPlusGcpf(String totCpfPlusGcpf) {
        this.totCpfPlusGcpf = totCpfPlusGcpf;
    }

    public String getPagebreakAnx() {
        return pagebreakAnx;
    }

    public void setPagebreakAnx(String pagebreakAnx) {
        this.pagebreakAnx = pagebreakAnx;
    }

    public String getPageHeaderAnx() {
        return pageHeaderAnx;
    }

    public void setPageHeaderAnx(String pageHeaderAnx) {
        this.pageHeaderAnx = pageHeaderAnx;
    }

    public String getCpfCaryFrd() {
        return cpfCaryFrd;
    }

    public void setCpfCaryFrd(String cpfCaryFrd) {
        this.cpfCaryFrd = cpfCaryFrd;
    }

    public String getTotCaryFrd() {
        return totCaryFrd;
    }

    public void setTotCaryFrd(String totCaryFrd) {
        this.totCaryFrd = totCaryFrd;
    }

    public String getAnx2CaryFrd() {
        return anx2CaryFrd;
    }

    public void setAnx2CaryFrd(String anx2CaryFrd) {
        this.anx2CaryFrd = anx2CaryFrd;
    }

    public String getAnx3CaryFrd() {
        return anx3CaryFrd;
    }

    public void setAnx3CaryFrd(String anx3CaryFrd) {
        this.anx3CaryFrd = anx3CaryFrd;
    }

    public String getArrearGAmt() {
        return arrearGAmt;
    }

    public void setArrearGAmt(String arrearGAmt) {
        this.arrearGAmt = arrearGAmt;
    }

}
