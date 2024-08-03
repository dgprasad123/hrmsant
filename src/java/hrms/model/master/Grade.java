/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.model.master;

/**
 *
 * @author BIBHUTI
 */
public class Grade {
    private String cadreCode;    
    private String cadreName;
    private String grade; 
    private int gradeLevel;
    private int sanction;
    private int cadreGradeCode;
    private String is_visible;
    private String is_obsolate;
    private String deptName;
    private String deptCode = null;

    public String getCadreCode() {
        return cadreCode;
    }

    public void setCadreCode(String cadreCode) {
        this.cadreCode = cadreCode;
    }

    public String getCadreName() {
        return cadreName;
    }

    public void setCadreName(String cadreName) {
        this.cadreName = cadreName;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public int getGradeLevel() {
        return gradeLevel;
    }

    public void setGradeLevel(int gradeLevel) {
        this.gradeLevel = gradeLevel;
    }

    public int getSanction() {
        return sanction;
    }

    public void setSanction(int sanction) {
        this.sanction = sanction;
    }

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    public String getIs_visible() {
        return is_visible;
    }

    public void setIs_visible(String is_visible) {
        this.is_visible = is_visible;
    }

    public String getIs_obsolate() {
        return is_obsolate;
    }

    public void setIs_obsolate(String is_obsolate) {
        this.is_obsolate = is_obsolate;
    }

    public int getCadreGradeCode() {
        return cadreGradeCode;
    }

    public void setCadreGradeCode(int cadreGradeCode) {
        this.cadreGradeCode = cadreGradeCode;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }
    
}
