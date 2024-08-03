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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class OTC84Controller implements ServletContextAware{
    
    @Autowired
    public ScheduleDAO comonScheduleDao;
    
    @Autowired
    public PayBillDMPDAO paybillDmpDao;
    
    private ServletContext context;

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.context = servletContext;
    }
    
    @RequestMapping(value = "OTCForm84HTML",method = {RequestMethod.POST,RequestMethod.GET})
    public ModelAndView OTCForm84HTML(@RequestParam("billNo") String billNo){
    
        ModelAndView mav = null;
        OTC84Bean otc84Bean = new OTC84Bean();
        try{
            mav = new ModelAndView();
            
            CommonReportParamBean crb = paybillDmpDao.getCommonReportParameter(billNo);
            otc84Bean = comonScheduleDao.getOTC84ScheduleDetails(billNo, crb.getAqyear(), crb.getAqmonth(), crb);
            
            mav.addObject("OTC84Header", otc84Bean);     
            mav.setViewName("/payroll/schedule/OTC84");
            
        }catch(Exception e){
            e.printStackTrace();
        } 
        return mav;  
    }
    
    @RequestMapping(value = "OTCForm84Pdf")
    public void OTCForm84PDF(HttpServletResponse response, @RequestParam("billNo") String billNo) {
        response.setContentType("application/pdf");
        Document document = new Document(PageSize.A4);
        
        try {
            response.setHeader("Content-Disposition", "attachment; filename=OTC84Schedule_"+billNo+".pdf"); // HOUSE BUILDING
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();

            CommonReportParamBean crb = paybillDmpDao.getCommonReportParameter(billNo);
            OTC84Bean otc84Bean = comonScheduleDao.getOTC84ScheduleDetails(billNo, crb.getAqyear(), crb.getAqmonth(), crb);
            
            comonScheduleDao.OTCForm84PDF(document, billNo, otc84Bean);
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }
    
}
