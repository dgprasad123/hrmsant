/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.dao.roabsence;

import hrms.model.roabsence.RoabsenceBean;
import java.util.List;


public interface RoabsenceDAO {
 public List getRoabsenceList(String empid);
    public void saveRoabsence(RoabsenceBean pb, int notid);
    public void updateRoabsence(RoabsenceBean pb);
    public void deleteRoabsence(RoabsenceBean pb);
    public RoabsenceBean getEmpRoabsenceData(String leaveId);   
}
