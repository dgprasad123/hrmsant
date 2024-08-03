/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.dao.master;

import hrms.model.login.UserExpertise;
import java.util.List;

/**
 *
 * @author Manoj PC
 */
public interface UserExpertiseDAO {
    public String saveUserExpertise(UserExpertise ue);

    public String updateUserExpertise(UserExpertise ue, String empid);

    public UserExpertise getUserExpertise(String empid);

    public List getUserExpertiseList(int maxlimit,int minlimit);
    
    public int getUserExpertiseCount();
    
    public String ViewExpertiseList();
}
