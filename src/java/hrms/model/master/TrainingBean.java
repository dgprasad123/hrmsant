/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.master;

/**
 *
 * @author lenovo
 */
public class TrainingBean {
    private int trainingId;
    private String trainingType;
    private String trainingTitle;
    private String trainingTitleId;
    private String hiddentrainingid;

    public String getHiddentrainingid() {
        return hiddentrainingid;
    }

    public void setHiddentrainingid(String hiddentrainingid) {
        this.hiddentrainingid = hiddentrainingid;
    }

    public int getTrainingId() {
        return trainingId;
    }

    public void setTrainingId(int trainingId) {
        this.trainingId = trainingId;
    }

    public String getTrainingType() {
        return trainingType;
    }

    public void setTrainingType(String trainingType) {
        this.trainingType = trainingType;
    }

    public String getTrainingTitle() {
        return trainingTitle;
    }

    public void setTrainingTitle(String trainingTitle) {
        this.trainingTitle = trainingTitle;
    }

    public String getTrainingTitleId() {
        return trainingTitleId;
    }

    public void setTrainingTitleId(String trainingTitleId) {
        this.trainingTitleId = trainingTitleId;
    }
}
