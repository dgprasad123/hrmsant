/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.training;

import hrms.model.trainingschedule.TrainingSchedule;
import java.util.List;

/**
 *
 * @author lenovo pc
 */
public interface TrainingDAO {
 public List getTrainingList(String empId);  
 public void saveTrainingSchedule(TrainingSchedule training);
 public void updateTrainingSchedule(TrainingSchedule training);;
 public TrainingSchedule getTrainingData(String trainId);
 public void deleteTraining(String trainId);
}
