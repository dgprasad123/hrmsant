/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.dao.changepassword;

import java.sql.SQLException;

/**
 *
 * @author Surendra
 */
public interface ChangePasswordDAO {
    
    public int modifyUserPassword(String empid, String usertype, String oldpwd, String newpwd, String loginusername) throws SQLException;
    
    public String getUserSalt(String username) throws Exception;
}
