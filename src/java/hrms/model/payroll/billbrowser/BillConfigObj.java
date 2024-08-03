/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.payroll.billbrowser;

import java.util.ArrayList;

/**
 *
 * @author Manas Jena
 */
public class BillConfigObj {
    private ArrayList aqListSectionWise=null;
    private String [] col6List=null;
    private String [] col7List=null;
    private String [] col16List=null;
    private String [] col17List=null;
    private String [] col18List=null;    
    private String notToPrintInAquitance;
    
    public void setCol7List(String[] col7List) {
        this.col7List = col7List;
    }

    public String[] getCol7List() {
        return col7List;
    }

    public void setCol16List(String[] col16List) {
        this.col16List = col16List;
    }

    public String[] getCol16List() {
        return col16List;
    }

    public void setCol17List(String[] col17List) {
        this.col17List = col17List;
    }

    public String[] getCol17List() {
        return col17List;
    }

    public void setCol18List(String[] col18List) {
        this.col18List = col18List;
    }

    public String[] getCol18List() {
        return col18List;
    }

    public void setAqListSectionWise(ArrayList aqListSectionWise) {
        this.aqListSectionWise = aqListSectionWise;
    }

    public ArrayList getAqListSectionWise() {
        return aqListSectionWise;
    }

    public void setCol6List(String[] col6List) {
        this.col6List = col6List;
    }

    public String[] getCol6List() {
        return col6List;
    }

    public String getNotToPrintInAquitance() {
        return notToPrintInAquitance;
    }

    public void setNotToPrintInAquitance(String notToPrintInAquitance) {
        this.notToPrintInAquitance = notToPrintInAquitance;
    }

}
