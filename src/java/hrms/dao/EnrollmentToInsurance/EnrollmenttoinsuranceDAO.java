/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.EnrollmentToInsurance;

import hrms.model.EnrollmentToInsurance.Enrollmenttoinsurance;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Surendra
 */
public interface EnrollmenttoinsuranceDAO {
    
     public ArrayList getSchemeList();
     
     public void saveEnrollment(Enrollmenttoinsurance enrollmentForm, int notid);
     
     public void updateEnrollment(Enrollmenttoinsurance enrollmentForm);
     
     public List getEnrollmentList(String empid);
      
     public Enrollmenttoinsurance getempEnrollmentData(String empid, int notId);
    
}
