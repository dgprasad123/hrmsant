/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.dao.master;

import hrms.model.privilege.RoleProperty;
import java.util.ArrayList;

/**
 *
 * @author DurgaPrasad
 */
public interface RoleDAO {
    public ArrayList getRoleList();
    public void saveRole(RoleProperty roleProperty);
}
