/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.EmpQuarterAllotment;

import com.itextpdf.text.Document;
import hrms.model.EmpQuarterAllotment.EmpQuarterBean;
import java.io.OutputStream;
import java.util.List;
import jxl.write.WritableWorkbook;

/**
 *
 * @author lenovo
 */
public interface QMSDAO {

    public List OverdueVacateDate();

    public void RetentionOverDueNotice(EmpQuarterBean qbean);

    public List OrderFiveTwoIssued();

    public void DownloadFiveTwoOrder(Document document, int caseId);

    public void viewPDFEvictionNotice(Document document, String empId, String consumerNo, int CaseId);

    public void downloadVacationOccupationExcel(OutputStream out, String fileName, WritableWorkbook workbook, String vstatus, String loginName, String fdate, String tdate);

    public List DropOPPCaseList();
    
     public void SaveOPPVacationStatus(EmpQuarterBean qbean);

}
