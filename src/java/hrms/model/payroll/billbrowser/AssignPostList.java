/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.payroll.billbrowser;

/**
 *
 * @author Manas
 */
public class AssignPostList {

    private String spc = null;
    private String spn = null;
    private String serialNo = null;
    private int sectionId;

    public void setSpc(String spc) {
        this.spc = spc;
    }

    public String getSpc() {
        return spc;
    }

    public void setSpn(String spn) {
        this.spn = spn;
    }

    public String getSpn() {
        return spn;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public int getSectionId() {
        return sectionId;
    }

    public void setSectionId(int sectionId) {
        this.sectionId = sectionId;
    }

    
}
