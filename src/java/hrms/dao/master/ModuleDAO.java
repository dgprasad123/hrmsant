/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.master;

import hrms.model.master.Module;
import java.util.ArrayList;

/**
 *
 * @author Manas Jena
 */
public interface ModuleDAO {
    public ArrayList getModuleList();
    public Module getModuleDetail(int modid);
    public void saveModule(Module module);    
    public ArrayList getModuleList(String moduleGroupId);
}
