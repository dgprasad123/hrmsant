/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.payroll.billbrowser;

/**
 *
 * @author Manas Jena
 */
public class Schedule {
    private String scheduleName=null;
    private String schAmount="0";
    private String nowDeduct=null;
    private String objectHead=null;
    private String scheduleDesc=null;
     private String demandNo=null;

    public String getScheduleDesc() {
        return scheduleDesc;
    }

    public void setScheduleDesc(String scheduleDesc) {
        this.scheduleDesc = scheduleDesc;
    }

    public String getDemandNo() {
        return demandNo;
    }

    public void setDemandNo(String demandNo) {
        this.demandNo = demandNo;
    }
     
     
    

    public void setSchAmount(String schAmount) {
        this.schAmount = schAmount;
    }

    public String getSchAmount() {
        return schAmount;
    }
    
    public void addSchAmount(int amt) {
        this.schAmount = (Integer.parseInt(this.schAmount) + amt)+"" ;
    }
    
    public void setScheduleName(String scheduleName) {
        this.scheduleName = scheduleName;
    }

    public String getScheduleName() {
        return scheduleName;
    }

    public void setNowDeduct(String nowDeduct) {
        this.nowDeduct = nowDeduct;
    }

    public String getNowDeduct() {
        return nowDeduct;
    }

    public void setObjectHead(String objectHead) {
        this.objectHead = objectHead;
    }

    public String getObjectHead() {
        return objectHead;
    }
}
