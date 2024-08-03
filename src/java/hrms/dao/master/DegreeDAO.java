/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.dao.master;

import hrms.model.master.DegreeBean;
import java.util.ArrayList;

/**
 *
 * @author Surendra
 */
public interface DegreeDAO {
    public ArrayList getDegreeList();
        public void saveUserDetails(DegreeBean degreeBean);
         public DegreeBean editDegree(int degreeSlno);
             public void updateDegreeDetails(DegreeBean degreeBean);
              public void deleteDegree(int degreeserialNumber);


    
}
