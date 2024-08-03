package hrms.model.payroll.aqreport;

public class BillAmtDetails {
    private double billGross;
    private double billNet;
    private double billDeduction;
    private double billPvtDeduction;
    

    public void setBillGross(double billGross) {
        this.billGross = billGross;
    }

    public double getBillGross() {
        return billGross;
    }

    public void setBillNet(double billNet) {
        this.billNet = billNet;
    }

    public double getBillNet() {
        return billNet;
    }

    public void setBillDeduction(double billDeduction) {
        this.billDeduction = billDeduction;
    }

    public double getBillDeduction() {
        return billDeduction;
    }

    public void setBillPvtDeduction(double billPvtDeduction) {
        this.billPvtDeduction = billPvtDeduction;
    }

    public double getBillPvtDeduction() {
        return billPvtDeduction;
    }
}
