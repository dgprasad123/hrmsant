/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.controller.payroll.schedule;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import hrms.dao.payroll.schedule.PayBillDMPDAO;
import hrms.dao.payroll.schedule.ScheduleDAO;
import hrms.model.common.CommonReportParamBean;
import hrms.model.payroll.schedule.OTC84Bean;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.ServletContextAware;

/**
 *
 * @author prashant
 */
@Controller
public class RTIScheduleController implements ServletContextAware{
    
    @Autowired
    public ScheduleDAO comonScheduleDao;
    
    @Autowired
    public PayBillDMPDAO paybillDmpDao;
    
    private ServletContext context;

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.context = servletContext;
    }
    
    @RequestMapping(value = "RTISchedulePdf")
    public void RTISchedulePagePdf(HttpServletResponse response, @RequestParam("billNo") String billNo) {
        response.setContentType("application/pdf");
        Document document = new Document(PageSize.A4);
        
        try {
            response.setHeader("Content-Disposition", "attachment; filename=RTISchedulePdf_"+billNo+".pdf"); // RTI Schedule Pdf
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();

            CommonReportParamBean crb = paybillDmpDao.getCommonReportParameter(billNo);
            comonScheduleDao.getRTISchedulePdf(document, billNo, crb);
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }
    
    
}
