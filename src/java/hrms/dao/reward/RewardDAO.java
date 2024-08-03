/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.reward;

import hrms.model.common.FileAttribute;
import hrms.model.reward.Reward;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author lenovo
 */
public interface RewardDAO {

    public List getRewardList(String empid);

    public void updateReward(Reward rewardForm,String filepath);

    public void saveReward(Reward RewardForm, int notid,String filepath);

    public void deleteReward(String rewardId);

    public ArrayList empRewardType();

    public Reward getEmpRewardData(String rewardId, int notificationId) throws SQLException;
    
    public FileAttribute getAttachedDocument(String filePath, String docName, int attachId);
    
    public int deleteRewardAttachment(int notid,String filepath,FileAttribute fa);

}
