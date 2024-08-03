/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.trainingadmin;

/**
 *
 * @author Manoj PC
 */
public class TrainingFacultyForm {
    private String facultyName = null;
    private String designation = null;
    private String abbr = null;
    private String opt = null;
    private String facultyType = null;
    private String hidPost = null;
    private String facultyCode = null;

    public String getFacultyCode() {
        return facultyCode;
    }

    public void setFacultyCode(String facultyCode) {
        this.facultyCode = facultyCode;
    }

    public String getHidPost() {
        return hidPost;
    }

    public void setHidPost(String hidPost) {
        this.hidPost = hidPost;
    }

    public String getGovtAbbr() {
        return govtAbbr;
    }

    public void setGovtAbbr(String govtAbbr) {
        this.govtAbbr = govtAbbr;
    }
    private String govtAbbr = null;

    public String getFacultyType() {
        return facultyType;
    }

    public void setFacultyType(String facultyType) {
        this.facultyType = facultyType;
    }
    private int facultyId = 0;    

    public String getFacultyName() {
        return facultyName;
    }

    public void setFacultyName(String facultyName) {
        this.facultyName = facultyName;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getAbbr() {
        return abbr;
    }

    public void setAbbr(String abbr) {
        this.abbr = abbr;
    }

    public String getOpt() {
        return opt;
    }

    public void setOpt(String opt) {
        this.opt = opt;
    }

    public int getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(int facultyId) {
        this.facultyId = facultyId;
    }
}
