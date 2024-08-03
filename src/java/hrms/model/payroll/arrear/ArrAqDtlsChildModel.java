/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.payroll.arrear;

/**
 *
 * @author lenovo
 */
public class ArrAqDtlsChildModel {
    
    private String adType;
    private double alreadyPaid;
    private double toBePaid;
    private String aqSlNo=null;

    public String getAdType() {
        return adType;
    }

    public void setAdType(String adType) {
        this.adType = adType;
    }

    public double getAlreadyPaid() {
        return alreadyPaid;
    }

    public void setAlreadyPaid(double alreadyPaid) {
        this.alreadyPaid = alreadyPaid;
    }

    public double getToBePaid() {
        return toBePaid;
    }

    public void setToBePaid(double toBePaid) {
        this.toBePaid = toBePaid;
    }

    public String getAqSlNo() {
        return aqSlNo;
    }

    public void setAqSlNo(String aqSlNo) {
        this.aqSlNo = aqSlNo;
    }
    
}
