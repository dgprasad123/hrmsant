/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.dao.importemployee;

import java.util.ArrayList;
import jxl.Workbook;

/**
 *
 * @author Madhusmita
 */
public interface ImportEmpDetailsExcelDAO {
    
    public void createEmployees(Workbook workbook);
    
}
