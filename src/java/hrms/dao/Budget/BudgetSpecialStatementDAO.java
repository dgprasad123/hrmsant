/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.Budget;

import hrms.model.Budget.BudgetForm;
import java.io.OutputStream;
import jxl.write.WritableWorkbook;

/**
 *
 * @author Madhusmita
 */
public interface BudgetSpecialStatementDAO {

    public void downloadSpecialStatementExcel(OutputStream out, String fileName, WritableWorkbook workbook,String deptCode, String month, String year);

}
