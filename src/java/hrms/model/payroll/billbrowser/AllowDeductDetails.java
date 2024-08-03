/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.model.payroll.billbrowser;

import java.util.Comparator;

public class AllowDeductDetails implements Comparator,Comparable{
    
    private String adname=null;
    private String objecthead=null;
    private String adamount=null;

    public String getAdname() {
        return adname;
    }

    public void setAdname(String adname) {
        this.adname = adname;
    }

    public String getObjecthead() {
        return objecthead;
    }

    public void setObjecthead(String objecthead) {
        this.objecthead = objecthead;
    }

    public String getAdamount() {
        return adamount;
    }

    public void setAdamount(String adamount) {
        this.adamount = adamount;
    }
    
    public int compareTo(Object o) {
        AllowDeductDetails p = (AllowDeductDetails) o;
        return Integer.parseInt(this.objecthead) - Integer.parseInt(p.getObjecthead()) ;
    }
    
    public int compare(Object o1, Object o2){    
        AllowDeductDetails ad1=(AllowDeductDetails)o1;
        AllowDeductDetails ad2=(AllowDeductDetails)o2;
        return Integer.parseInt(ad1.getObjecthead()) - Integer.parseInt(ad2.getObjecthead());        
    }
    
}

