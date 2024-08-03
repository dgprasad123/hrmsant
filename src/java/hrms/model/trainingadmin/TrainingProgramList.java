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
public class TrainingProgramList {

    private String programName;
    private String fromDate;
    private String toDate;
    private String duration;
    private String trainingProgramID;
    private String venue;
    private String diskFileName;
    private String facultyName;
    private String sponsorName;
    private int capacity = 0;
    private String instituteName = null;
    private int numApplicants = 0;
    private int trainingOption = 0;
    private String strTrainingOption = null;
    private String applyEndDate = null;
    private boolean isExpired = false;
    private String isSelected = null;
    private String isShortlisted = null;
    private String financialYear = null;

    public int getNumApplicants() {
        return numApplicants;
    }

    public String getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(String isSelected) {
        this.isSelected = isSelected;
    }

    public String getIsShortlisted() {
        return isShortlisted;
    }

    public void setIsShortlisted(String isShortlisted) {
        this.isShortlisted = isShortlisted;
    }

    public void setNumApplicants(int numApplicants) {
        this.numApplicants = numApplicants;
    }

    public String getInstituteName() {
        return instituteName;
    }

    public void setInstituteName(String instituteName) {
        this.instituteName = instituteName;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    private String status;

    public String getFacultyName() {
        return facultyName;
    }

    public void setFacultyName(String facultyName) {
        this.facultyName = facultyName;
    }

    public String getSponsorName() {
        return sponsorName;
    }

    public void setSponsorName(String sponsorName) {
        this.sponsorName = sponsorName;
    }

    public String getDiskFileName() {
        return diskFileName;
    }

    public void setDiskFileName(String diskFileName) {
        this.diskFileName = diskFileName;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getTrainingProgramID() {
        return trainingProgramID;
    }

    public void setTrainingProgramID(String trainingProgramID) {
        this.trainingProgramID = trainingProgramID;
    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public int getTrainingOption() {
        return trainingOption;
    }

    public void setTrainingOption(int trainingOption) {
        this.trainingOption = trainingOption;
    }

    public String getStrTrainingOption() {
        return strTrainingOption;
    }

    public void setStrTrainingOption(String strTrainingOption) {
        this.strTrainingOption = strTrainingOption;
    }

    public String getApplyEndDate() {
        return applyEndDate;
    }

    public void setApplyEndDate(String applyEndDate) {
        this.applyEndDate = applyEndDate;
    }

    public boolean isIsExpired() {
        return isExpired;
    }

    public void setIsExpired(boolean isExpired) {
        this.isExpired = isExpired;
    }

    public String getFinancialYear() {
        return financialYear;
    }

    public void setFinancialYear(String financialYear) {
        this.financialYear = financialYear;
    }
    
}
