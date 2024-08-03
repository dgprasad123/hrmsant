/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.eDespatch;

/**
 *
 * @author Manas
 */
public class eDespatchBean {
    private String officeCode;
    private String letterno;
    private String fileno;
    private String letterdate;
    private String lettertype;
    private String section;
    private String sentfrom;
    private int proposalId;

    public String getOfficeCode() {
        return officeCode;
    }

    public void setOfficeCode(String officeCode) {
        this.officeCode = officeCode;
    }

    public String getLetterno() {
        return letterno;
    }

    public void setLetterno(String letterno) {
        this.letterno = letterno;
    }

    public String getFileno() {
        return fileno;
    }

    public void setFileno(String fileno) {
        this.fileno = fileno;
    }

    public String getLetterdate() {
        return letterdate;
    }

    public void setLetterdate(String letterdate) {
        this.letterdate = letterdate;
    }

    public String getLettertype() {
        return lettertype;
    }

    public void setLettertype(String lettertype) {
        this.lettertype = lettertype;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getSentfrom() {
        return sentfrom;
    }

    public void setSentfrom(String sentfrom) {
        this.sentfrom = sentfrom;
    }

    public int getProposalId() {
        return proposalId;
    }

    public void setProposalId(int proposalId) {
        this.proposalId = proposalId;
    }
    
}
