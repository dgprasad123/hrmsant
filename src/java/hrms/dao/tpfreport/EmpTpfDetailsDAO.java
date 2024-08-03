/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.dao.tpfreport;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Madhusmita
 */
public interface EmpTpfDetailsDAO {
    public ArrayList getTpfEmployeeList(int month, int year, String offCode);
    
}
