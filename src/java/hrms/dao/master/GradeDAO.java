/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.dao.master;

import java.util.List;

import hrms.model.master.Grade;
/**
 *
 * @author BIBHUTI
 */
public interface GradeDAO {
    
    public List getCadreList(String deptcode);
    
    public List getGradeList(String cadreCode);
            
    public void saveGrade(Grade  grade);
    
    public void updateGrade(Grade  grade);
    
    public Grade getGradeData(int cadreGradeCode);
    
    public String getCadreName(String cadreCode);
    
    public String getGradeName(String grade);
}
