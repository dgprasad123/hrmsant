/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.model.empinfo;

/**
 *
 * @author Manas
 */
public class GrievanceDashBoard {
    private String battalionName;
    private String offcode;
    private int totalGrievance;
    private int disposedGrievance;
    private int rejectedGrievance;
    private int pendingGrievance;

    public String getBattalionName() {
        return battalionName;
    }

    public void setBattalionName(String battalionName) {
        this.battalionName = battalionName;
    }

    public String getOffcode() {
        return offcode;
    }

    public void setOffcode(String offcode) {
        this.offcode = offcode;
    }

    public int getTotalGrievance() {
        return totalGrievance;
    }

    public void setTotalGrievance(int totalGrievance) {
        this.totalGrievance = totalGrievance;
    }

    public int getDisposedGrievance() {
        return disposedGrievance;
    }

    public void setDisposedGrievance(int disposedGrievance) {
        this.disposedGrievance = disposedGrievance;
    }

    public int getRejectedGrievance() {
        return rejectedGrievance;
    }

    public void setRejectedGrievance(int rejectedGrievance) {
        this.rejectedGrievance = rejectedGrievance;
    }

    public int getPendingGrievance() {
        return this.totalGrievance - (this.disposedGrievance+this.rejectedGrievance);
    }    
        
}
