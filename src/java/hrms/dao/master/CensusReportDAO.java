/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.master;

import java.io.OutputStream;
import jxl.write.WritableWorkbook;

/**
 *
 * @author Madhusmita
 */
public interface CensusReportDAO {

    public void downloadCensusReportOfficeWise(OutputStream out, String fileName, WritableWorkbook workbook, String offCode, String fiyear);
}
