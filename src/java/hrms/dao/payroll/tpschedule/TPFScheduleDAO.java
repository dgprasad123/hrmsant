package hrms.dao.payroll.tpschedule;

import com.itextpdf.text.Document;
import hrms.model.common.CommonReportParamBean;
import hrms.model.payroll.billbrowser.BillConfigObj;
import java.io.OutputStream;
import java.util.List;
import jxl.write.WritableWorkbook;

public interface TPFScheduleDAO {
    
    public List getEmployeeWiseTPFList(String billno, int aqMonth, int aqyear);
    
    public List getTPFAbstract(String billno, int aqMonth, int aqyear);
    
    public void downloadQuarterITExcel(OutputStream out, String fileName, WritableWorkbook workbook, String billId, BillConfigObj billConfig);
    
    public void generateTPFSchedulePDF(String filename,Document document,String billno,List tpfEmpList,List tpfAbstract,CommonReportParamBean crb);
    
}
