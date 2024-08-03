package hrms.model.master;

import java.io.Serializable;

public class Training implements Serializable {

    private String trainingtypeid;

    private String trainingtype;

    public String getTrainingtype() {
        return trainingtype;
    }

    public void setTrainingtype(String trainingtype) {
        this.trainingtype = trainingtype;
    }

    public String getTrainingtypeid() {
        return trainingtypeid;
    }

    public void setTrainingtypeid(String trainingtypeid) {
        this.trainingtypeid = trainingtypeid;
    }
}
