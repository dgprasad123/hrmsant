/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.billvouchingTreasury;

/**
 *
 * @author Surendra
 */
public class ObjectBreakup {
    private int hrmsgeneratedRefno=0;
    private String hrmsgeneratedRefdate=null;
    private String objectHead=null;
    private String objectHeadwiseAmount=null;
    private String treasuryCode=null;

    public void setHrmsgeneratedRefno(int hrmsgeneratedRefno) {
        this.hrmsgeneratedRefno = hrmsgeneratedRefno;
    }

    public int getHrmsgeneratedRefno() {
        return hrmsgeneratedRefno;
    }

    public void setHrmsgeneratedRefdate(String hrmsgeneratedRefdate) {
        this.hrmsgeneratedRefdate = hrmsgeneratedRefdate;
    }

    public String getHrmsgeneratedRefdate() {
        return hrmsgeneratedRefdate;
    }

    public void setObjectHead(String objectHead) {
        this.objectHead = objectHead;
    }

    public String getObjectHead() {
        if(objectHead == null){
            objectHead = "";
        }
        return objectHead;
    }

    

    public void setTreasuryCode(String treasuryCode) {
        this.treasuryCode = treasuryCode;
    }

    public String getTreasuryCode() {
        if(treasuryCode == null){
            treasuryCode = "";
        }
        return treasuryCode;
    }

    public String getObjectHeadwiseAmount() {
        return objectHeadwiseAmount;
    }

    public void setObjectHeadwiseAmount(String objectHeadwiseAmount) {
        this.objectHeadwiseAmount = objectHeadwiseAmount;
    }

}
