/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.master;

import hrms.model.master.TrainingBean;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author lenovo
 */
public interface TrainingMasterDAO {

    public ArrayList getTrainingMasterTitle();

    public List getTrainingType();

    public void addNewTrainingMaster(TrainingBean training);

    public void deleteTrainingMaster(int trainingId);
    
    public TrainingBean editNewTrainingMaster(int trainingId, TrainingBean training);
    
    public void updateNewTrainingMaster(TrainingBean training);

}
