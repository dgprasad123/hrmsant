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
public class ParticipantBean {
    private String participantName = null;
    private String designation = null;
    private String officeName = null;
    private String department = null;
    private String status = null;
    private String participantId = null;
    private String[] participants = null;
    private String empId = null;
    private int numShortlisted = 0;

    public String[] getParticipants() {
        return participants;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public void setParticipants(String[] participants) {
        this.participants = participants;
    }

    public int getNumShortlisted() {
        return numShortlisted;
    }

    public void setNumShortlisted(int numShortlisted) {
        this.numShortlisted = numShortlisted;
    }

    public String getParticipantId() {
        return participantId;
    }

    public void setParticipantId(String participantId) {
        this.participantId = participantId;
    }

    public String getParticipantName() {
        return participantName;
    }

    public void setParticipantName(String participantName) {
        this.participantName = participantName;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getOfficeName() {
        return officeName;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
}
