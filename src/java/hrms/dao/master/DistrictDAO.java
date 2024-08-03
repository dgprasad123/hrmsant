/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.master;

import hrms.model.master.District;
import java.util.ArrayList;

/**
 *
 * @author Manas Jena
 */
public interface DistrictDAO {

    public ArrayList getDistrictList();

    public ArrayList getDistrictList(String stateCode);
    
    public ArrayList getPoliceDistrictList();
    
    public void saveNewDistrict(District  district);
    
    public void updateNewDistrict(District  district);
    
    public District editDistrict(District  district);
    
    public String getDistrictName(String distCode);
}
