/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.trainingadmin;

import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Manoj PC
 */
public class TrainingProgramForm {

    private String trainingProgram = null;
    private String fromDate = null;
    private String toDate = null;
    private String opt = null;
    private String programDate = null;
    private int trainingId = 0;
    private String venueId = null;
    private MultipartFile documentFile;
    private String strFile = null;
    private String instituteName = null;
    private String venueName = null;
    private String sponsorName = null;
    private String facultyName = null;
    private String duration = null;
    private String empName = null;
    private String designation = null;
    private int capacity = 0;
    private String trainingAuthority = null;
    private String strTrainingAuthority = null;
    private int numApplied = 0;
     private String[] participants = null;
     private int numShortlisted = 0;
     private String isArchived = null;
     private String applyEndDate = null;
     private boolean isExpired = false;

    public int getNumShortlisted() {
        return numShortlisted;
    }

    public void setNumShortlisted(int numShortlisted) {
        this.numShortlisted = numShortlisted;
    }

    public String[] getParticipants() {
        return participants;
    }

    public void setParticipants(String[] participants) {
        this.participants = participants;
    }

    public int getNumApplied() {
        return numApplied;
    }

    public void setNumApplied(int numApplied) {
        this.numApplied = numApplied;
    }

    public String getStrTrainingAuthority() {
        return strTrainingAuthority;
    }

    public void setStrTrainingAuthority(String strTrainingAuthority) {
        this.strTrainingAuthority = strTrainingAuthority;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getTrainingAuthority() {
        return trainingAuthority;
    }

    public void setTrainingAuthority(String trainingAuthority) {
        this.trainingAuthority = trainingAuthority;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getInstituteName() {
        return instituteName;
    }

    public void setInstituteName(String instituteName) {
        this.instituteName = instituteName;
    }

    public String getVenueName() {
        return venueName;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }

    public String getSponsorName() {
        return sponsorName;
    }

    public void setSponsorName(String sponsorName) {
        this.sponsorName = sponsorName;
    }

    public String getFacultyName() {
        return facultyName;
    }

    public void setFacultyName(String facultyName) {
        this.facultyName = facultyName;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getStrFile() {
        return strFile;
    }

    public void setStrFile(String strFile) {
        this.strFile = strFile;
    }

    public String getVenueId() {
        return venueId;
    }

    public void setVenueId(String venueId) {
        this.venueId = venueId;
    }

    public MultipartFile getDocumentFile() {
        return documentFile;
    }

    public void setDocumentFile(MultipartFile documentFile) {
        this.documentFile = documentFile;
    }

    public int getTrainingId() {
        return trainingId;
    }

    public void setTrainingId(int trainingId) {
        this.trainingId = trainingId;
    }

    public String getProgramDate() {
        return programDate;
    }

    public void setProgramDate(String programDate) {
        this.programDate = programDate;
    }

    public String getOpt() {
        return opt;
    }

    public void setOpt(String opt) {
        this.opt = opt;
    }

    public String getTrainingProgram() {
        return trainingProgram;
    }

    public void setTrainingProgram(String trainingProgram) {
        this.trainingProgram = trainingProgram;
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

    public String getIsArchived() {
        return isArchived;
    }

    public void setIsArchived(String isArchived) {
        this.isArchived = isArchived;
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

}
