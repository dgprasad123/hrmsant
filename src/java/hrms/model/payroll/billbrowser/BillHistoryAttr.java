/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.payroll.billbrowser;

import java.util.ArrayList;

/**
 *
 * @author tushar
 */
public class BillHistoryAttr {

    private String billStatus = null;
    private ArrayList message = null;

    public String getBillStatus() {
        return billStatus;
    }

    public void setBillStatus(String billStatus) {
        this.billStatus = billStatus;
    }

    public ArrayList getMessage() {
        return message;
    }

    public void setMessage(ArrayList message) {
        this.message = message;
    }
}
