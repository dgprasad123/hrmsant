/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.model.payroll.arrear;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Manas
 */
public class PayRevisionOption {
    private int msgcode=0;
    private String message;
    private Date choiceDate = null;
    private int payrevisionbasic;
    private String isapproved = "N";
    private List payRevisionIncrements = null;

    public String getIsapproved() {
        return isapproved;
    }

    public void setIsapproved(String isapproved) {
        this.isapproved = isapproved;
    }

    public int getMsgcode() {
        return msgcode;
    }

    public void setMsgcode(int msgcode) {
        this.msgcode = msgcode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getChoiceDate() {
        return choiceDate;
    }

    public void setChoiceDate(Date choiceDate) {
        this.choiceDate = choiceDate;
    }

    public int getPayrevisionbasic() {
        return payrevisionbasic;
    }

    public void setPayrevisionbasic(int payrevisionbasic) {
        this.payrevisionbasic = payrevisionbasic;
    }

    public List getPayRevisionIncrements() {
        return payRevisionIncrements;
    }

    public void setPayRevisionIncrements(List payRevisionIncrements) {
        this.payRevisionIncrements = payRevisionIncrements;
    }
    
    
}
