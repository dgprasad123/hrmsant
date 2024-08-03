/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.master;

import hrms.model.master.Village;
import java.util.ArrayList;

/**
 *
 * @author lenovo pc
 */
public interface VillageDAO {
    public ArrayList getVillageList(String distCode,String psCode);
    
    public Village editVillage(Village  village);
    
    public void saveNewVillage(Village  village);
    
    public void updateVillage(Village  village);
}
