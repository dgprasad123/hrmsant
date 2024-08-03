/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.master;

import hrms.model.master.Degree;
import java.util.ArrayList;

/**
 *
 * @author lenovo pc
 */
public interface DegreeeDAO {
    public ArrayList getDegreeList();
     public void saveUserDetails(Degree degree);
         public Degree editDegree(int degreeSlno);
             public void updateDegreeDetails(Degree degree);
              public void deleteDegree(int degreeserialNumber);
}
