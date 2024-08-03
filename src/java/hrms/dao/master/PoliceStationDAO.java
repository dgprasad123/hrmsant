/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.master;

import hrms.model.master.PoliceStation;
import java.util.ArrayList;

/**
 *
 * @author lenovo pc
 */
public interface PoliceStationDAO {
    public ArrayList getPoliceStationList(String distCode);
    
     public PoliceStation editPoliceStation(PoliceStation pstation);
     
     public void saveNewPoliceStation(PoliceStation pstation);
     
     public void updateNewPoliceStation(PoliceStation pstation);
}
