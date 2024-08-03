/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.dao.master;

import java.util.List;

/**
 *
 * @author Durga
 */
public interface DepartmentDAO {
    
    public List getDepartmentList();
    
    public List getDepartmentList4SiPAR();
    
    public List getDepartmentList(String offcode);
    
    public List getDeptList();
    
    public String getDeptName(String deptCode);
    
    public List getAllDepartmentLIst();
    
     public List getDepartmentPARList();
    
}
