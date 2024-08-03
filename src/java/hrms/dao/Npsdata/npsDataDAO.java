/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.dao.Npsdata;

import hrms.model.NpsData.NpsDataForm;
import java.io.OutputStream;
import java.util.ArrayList;
import jxl.write.WritableWorkbook;

/**
 *
 * @author Madhusmita
 */
public interface npsDataDAO {
    
    public ArrayList getTreasuryList();
    public void downloadNpsData(OutputStream out, String fileName, WritableWorkbook workbook,NpsDataForm npsForm); 
    
    public void downloadnonNpsData(OutputStream out, String fileName, WritableWorkbook workbook,NpsDataForm nonnpsform);  
    
}
