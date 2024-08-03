/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.absentee;

import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Manoj PC
 */
public class ExcelImportBean {

    private String amonth = null;
    private String ayear = null;
    private String fileName = null;
    private MultipartFile documentFile;
    private String empName = null;
    private String empId = null;
    private String daysWorked = null;

    public String getAmonth() {
        return amonth;
    }

    public void setAmonth(String amonth) {
        this.amonth = amonth;
    }

    public String getAyear() {
        return ayear;
    }

    public void setAyear(String ayear) {
        this.ayear = ayear;
    }

    
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public MultipartFile getDocumentFile() {
        return documentFile;
    }

    public void setDocumentFile(MultipartFile documentFile) {
        this.documentFile = documentFile;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getDaysWorked() {
        return daysWorked;
    }

    public void setDaysWorked(String daysWorked) {
        this.daysWorked = daysWorked;
    }
}
