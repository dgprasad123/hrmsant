/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.LongTermTraining;

import hrms.model.notification.NotificationBean;
import hrms.model.trainingschedule.TrainingSchedule;
import java.util.List;

/**
 *
 * @author Madhusmita
 */
public interface longTermTrainingDAO {

    public List getLtTrainingList(String empId);

    public void saveLtTraining(TrainingSchedule lttraining,int notId);

    public void updateLtTraining(TrainingSchedule training, int notId);

    public TrainingSchedule getLtTrainingData(int notId);

    public void deleteLtTraining(String trainId);

}
