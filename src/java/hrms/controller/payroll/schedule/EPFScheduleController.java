package hrms.controller.payroll.schedule;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import hrms.common.Numtowordconvertion;
import hrms.dao.payroll.schedule.PayBillDMPDAO;
import hrms.dao.payroll.schedule.ScheduleDAO;
import hrms.model.common.CommonReportParamBean;
import hrms.model.login.LoginUserBean;
import hrms.model.payroll.billbrowser.BillBrowserbean;
import hrms.model.payroll.schedule.GPFScheduleBean;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.ServletContextAware;

@Controller
public class EPFScheduleController {
    
    @Autowired
    public ScheduleDAO comonScheduleDao;

    @Autowired
    public PayBillDMPDAO paybillDmpDao;
    
    @RequestMapping(value = "EPFSchedulePDF")
    public void GPFSchedulePdfController(HttpServletResponse response,@ModelAttribute("BillBrowserbean") BillBrowserbean bbBean, @ModelAttribute("LoginUserBean") LoginUserBean lub) {
        
        response.setContentType("application/pdf");
        
        Document document = new Document(PageSize.A4);
        
        List gpfTypeList = null;
        List gpfAbstractList = null;
        GPFScheduleBean gpfHeader = null;
        GPFScheduleBean obj2 = null;
        
        double sal = 0.0;
        double totAmt = 0.0;
        String totalFig = null;
        String billNo = null;
        try {
            response.setHeader("Content-Disposition", "attachment; filename=EPFSchedulePdf_" + bbBean.getBillNo() + ".pdf");

            billNo = bbBean.getBillNo();

            PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());
            
            document.open();
            
            CommonReportParamBean crb = paybillDmpDao.getCommonReportParameter(billNo);
            gpfHeader = comonScheduleDao.getGPFScheduleHeaderDetails(billNo);

            gpfTypeList = comonScheduleDao.getEPFScheduleTypeList(billNo, crb.getAqmonth(), crb.getAqyear());
            gpfAbstractList = comonScheduleDao.getEPFScheduleAbstractList(billNo, crb.getAqmonth(), crb.getAqyear());

            GPFScheduleBean obj = null;
            if (gpfAbstractList != null && gpfAbstractList.size() > 0) {
                for (int i = 0; i < gpfAbstractList.size(); i++) {
                    obj = (GPFScheduleBean) gpfAbstractList.get(i);
                    sal = Double.parseDouble(obj.getTotalAmount());
                    totAmt = totAmt + sal;
                    totalFig = Numtowordconvertion.convertNumber((int) totAmt);
                }
            }

            comonScheduleDao.EPFSchedulePdf(writer, document, billNo, gpfTypeList, gpfAbstractList, crb,totalFig,totAmt,gpfHeader);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }
}
