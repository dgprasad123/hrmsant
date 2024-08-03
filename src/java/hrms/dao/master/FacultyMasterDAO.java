/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.dao.master;

import hrms.model.master.FacultyBean;
import java.util.ArrayList;

/**
 *
 * @author Surendra
 */
public interface FacultyMasterDAO {
    
     
    public void saveUserDetails(FacultyBean facultyBean);
   public ArrayList getFacultyList();

    public Object editFaculty(int facultySlno);

    public void updateFacultyDetails(FacultyBean facultyBean);

    public void deleteFaculty(int facultyserialNumber);

    
}
