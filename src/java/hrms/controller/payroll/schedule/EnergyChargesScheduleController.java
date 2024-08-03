package hrms.controller.payroll.schedule;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import hrms.dao.payroll.schedule.PayBillDMPDAO;
import hrms.dao.payroll.schedule.ScheduleDAO;
import hrms.model.common.CommonReportParamBean;
import hrms.model.payroll.schedule.ECScheduleForm;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class EnergyChargesScheduleController {
    
    @Autowired
    public ScheduleDAO comonScheduleDao;
    
    @Autowired
    public PayBillDMPDAO paybillDmpDao;
    
    @RequestMapping(value = "ECSchedulePDF")
    public void ECSchedulePDF(HttpServletResponse response, @RequestParam("billNo") String billNo){
        response.setContentType("application/pdf");
        Document document = new Document(PageSize.A4);
        try {
            
            response.setHeader("Content-Disposition", "attachment; filename=ECSchedule_"+billNo+".pdf");
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();

            CommonReportParamBean crb = paybillDmpDao.getCommonReportParameter(billNo);
            ECScheduleForm ecForm = comonScheduleDao.getECScheduleDetails(billNo);
            //List empList = comonScheduleDao.getPTScheduleEmployeeList(billNo, crb.getAqmonth(),crb.getAqyear());
            
            comonScheduleDao.ECSchedulePagePDF(crb, document, billNo, ecForm, ecForm.getEcScheduleDtls(), crb.getAqmonth(),crb.getAqyear());
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }
    
}
