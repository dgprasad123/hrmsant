/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.model.payroll.schedule;

public class BillBackPageBean extends ScheduleHelper{
    
    private String txtDa=null;
    private String txtGp=null;
    private String txtPPAY=null;
    private String txtPay=null;
    private String txtHra=null;
    private String txtOa=null;
    private String txtDearnessPay=null;
    private String netTotal=null;
    private long netTotaUnder=0;
    private String netTotalinWord=null;
    private String netTotalinWordUnder=null; 
    private String totalPaise=null;

    private String scheduleName=null;
    private String schAmount="0";
    private String nowDeduct=null;
    private String objectHead=null;
    
    
    public void addSchAmount(int amt) {
        this.schAmount = (Integer.parseInt(this.schAmount) + amt)+"" ;
    }
    
    public String getTxtDa() {
        return txtDa;
    }

    public void setTxtDa(String txtDa) {
        this.txtDa = txtDa;
    }

    public String getTxtGp() {
        return txtGp;
    }

    public void setTxtGp(String txtGp) {
        this.txtGp = txtGp;
    }

    public String getTxtPPAY() {
        return txtPPAY;
    }

    public void setTxtPPAY(String txtPPAY) {
        this.txtPPAY = txtPPAY;
    }

    public String getTxtPay() {
        return txtPay;
    }

    public void setTxtPay(String txtPay) {
        this.txtPay = txtPay;
    }

    public String getTxtHra() {
        return txtHra;
    }

    public void setTxtHra(String txtHra) {
        this.txtHra = txtHra;
    }

    public String getTxtOa() {
        return txtOa;
    }

    public void setTxtOa(String txtOa) {
        this.txtOa = txtOa;
    }

    public String getTxtDearnessPay() {
        return txtDearnessPay;
    }

    public void setTxtDearnessPay(String txtDearnessPay) {
        this.txtDearnessPay = txtDearnessPay;
    }

    public String getNetTotal() {
        return netTotal;
    }

    public void setNetTotal(String netTotal) {
        this.netTotal = netTotal;
    }

    public long getNetTotaUnder() {
        return netTotaUnder;
    }

    public void setNetTotaUnder(long netTotaUnder) {
        this.netTotaUnder = netTotaUnder;
    }

    public String getNetTotalinWord() {
        return netTotalinWord;
    }

    public void setNetTotalinWord(String netTotalinWord) {
        this.netTotalinWord = netTotalinWord;
    }

    public String getNetTotalinWordUnder() {
        return netTotalinWordUnder;
    }

    public void setNetTotalinWordUnder(String netTotalinWordUnder) {
        this.netTotalinWordUnder = netTotalinWordUnder;
    }

    public String getTotalPaise() {
        return totalPaise;
    }

    public void setTotalPaise(String totalPaise) {
        this.totalPaise = totalPaise;
    }

    public String getScheduleName() {
        return scheduleName;
    }

    public void setScheduleName(String scheduleName) {
        this.scheduleName = scheduleName;
    }

    public String getSchAmount() {
        return schAmount;
    }

    public void setSchAmount(String schAmount) {
        this.schAmount = schAmount;
    }

    public String getNowDeduct() {
        return nowDeduct;
    }

    public void setNowDeduct(String nowDeduct) {
        this.nowDeduct = nowDeduct;
    }

    public String getObjectHead() {
        return objectHead;
    }

    public void setObjectHead(String objectHead) {
        this.objectHead = objectHead;
    }
    
     
    
    
}
