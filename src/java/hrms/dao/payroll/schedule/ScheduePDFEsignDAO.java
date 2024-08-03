/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.dao.payroll.schedule;

import com.itextpdf.text.Document;

/**
 *
 * @author lenovo
 */
public interface ScheduePDFEsignDAO {
    
    public void BillContributionPDFDownload(Document document, String billNo,String annexure, int month, int year);
    
}
