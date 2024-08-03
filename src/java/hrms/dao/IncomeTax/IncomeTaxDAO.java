/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.dao.IncomeTax;

import hrms.model.IncomeTax.AnnexureIBean;
import hrms.model.IncomeTax.IT24QBean;
import hrms.model.IncomeTax.DeducteeBean;
import java.util.ArrayList;
import static java.util.Collections.list;
import java.util.List;
import jxl.Workbook;

/**
 *
 * @author Manoj PC
 */
public interface IncomeTaxDAO {
    public List getYears();
    public List getEmployeeWiseIT(int year, int month, String trCode);
    public List getDDOEmployeeWiseIT(int year, int month, String offCode);
    public List getDDODetailsList(String trCode);
    public void addExcelRowIntoDB(Workbook workbook, List li, String offCode);
    public AnnexureIBean getAnnexure(String offCode);
    public String getCurFiscalYear();
    public  ArrayList getFiscalYear();
    
    public IT24QBean get24Q(String offCode,String finyear,String finQuarter);
    public IT24QBean getChallanDetails(String offCode, String finyear, String finQuarter, String formType);
    public int getTotal26QAmount(String financialYear, String quarter, String month, String offCode);
    public void SaveDeducteeDetail(DeducteeBean dBean, String empId, String spc, String offCode);
    public  ArrayList get26QList(String offCode, String financialYear, String quarter, String month);
    public void deleteDeductee(String deducteeId);
}
