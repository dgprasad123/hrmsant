
package hrms.model.payroll.schedule;

import java.util.List;

public class ECScheduleBean{
    
    private String regdNo;
    private String address;
    private String nameOfOccupant;
    private String standardRent;
    private String surcharge;
    private String currentAmt;
    private String arrearAmt;
    private String totalAmt;

    public String getRegdNo() {
        return regdNo;
    }

    public void setRegdNo(String regdNo) {
        this.regdNo = regdNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNameOfOccupant() {
        return nameOfOccupant;
    }

    public void setNameOfOccupant(String nameOfOccupant) {
        this.nameOfOccupant = nameOfOccupant;
    }

    public String getStandardRent() {
        return standardRent;
    }

    public void setStandardRent(String standardRent) {
        this.standardRent = standardRent;
    }

    public String getSurcharge() {
        return surcharge;
    }

    public void setSurcharge(String surcharge) {
        this.surcharge = surcharge;
    }

    public String getCurrentAmt() {
        return currentAmt;
    }

    public void setCurrentAmt(String currentAmt) {
        this.currentAmt = currentAmt;
    }

    public String getArrearAmt() {
        return arrearAmt;
    }

    public void setArrearAmt(String arrearAmt) {
        this.arrearAmt = arrearAmt;
    }

    public String getTotalAmt() {
        return totalAmt;
    }

    public void setTotalAmt(String totalAmt) {
        this.totalAmt = totalAmt;
    }
    
    
}
