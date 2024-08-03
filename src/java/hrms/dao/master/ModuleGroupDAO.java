/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.dao.master;

import hrms.model.privilege.ModuleGroup;
import java.util.ArrayList;

/**
 *
 * @author DurgaPrasad
 */
public interface ModuleGroupDAO {
    public ArrayList getModuleGroupList(String roleId);
    public void saveModuleGroup(ModuleGroup moduleGroup);
}
