/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.payroll.schedule;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import hrms.common.Numtowordconvertion;
import hrms.dao.payroll.schedule.PayBillDMPDAO;
import hrms.dao.payroll.schedule.ScheduleDAO;
import hrms.model.common.CommonReportParamBean;
import hrms.model.payroll.schedule.ConveyanceBean;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author PKM
 */
@Controller
public class ConveyanceScheduleController implements ServletContextAware{
    
    @Autowired
    public ScheduleDAO comonScheduleDao;
    
    @Autowired
    public PayBillDMPDAO paybillDmpDao;
    
    private ServletContext context;

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.context = servletContext;
    }
    
    @RequestMapping(value = "ConveyanceScheduleHtml",method = {RequestMethod.POST,RequestMethod.GET})
    public ModelAndView ConveyanceScheduleHTML(@RequestParam("billNo") String billNo){
    
        ModelAndView mav = null;
        ConveyanceBean csBean = new ConveyanceBean();
        List dataList = new ArrayList();
        double total =0;
        String totalFig = "";
        try{
            mav = new ModelAndView();
            
            CommonReportParamBean crb = paybillDmpDao.getCommonReportParameter(billNo);
            dataList = comonScheduleDao.getCSDataList(billNo, crb.getAqyear(), crb.getAqmonth());
            
            ConveyanceBean obj = null;
            if(dataList != null && dataList.size() > 0){
                obj = new ConveyanceBean();
                for(int i = 0; i < dataList.size(); i++){
                    obj = (ConveyanceBean)dataList.get(i);
                    
                    total = total + obj.getAmtDed();
                    if(total > 0){
                        totalFig = Numtowordconvertion.convertNumber((int)total);
                    }
                }
            }
            
            mav.addObject("CsHeader", crb);
            mav.addObject("CsDataList", dataList);
            mav.addObject("GTotal", total);
            mav.addObject("GTotalFig", totalFig);
            mav.setViewName("/payroll/schedule/ConveyanceSchdule");
            
        }catch(Exception e){
            e.printStackTrace();
        } 
        return mav;  
    }
    
    @RequestMapping(value = "ConveyanceSchedulePdf")
    public void ConveyanceSchedulePDF(HttpServletResponse response, @RequestParam("billNo") String billNo) {
        
        response.setContentType("application/pdf");
        Document document = new Document(PageSize.A4);
        
        try {
            response.setHeader("Content-Disposition", "attachment; filename=ConveyanceSchedule_"+billNo+".pdf"); // HOUSE BUILDING
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();

            CommonReportParamBean crb = paybillDmpDao.getCommonReportParameter(billNo);
            List dataList = comonScheduleDao.getCSDataList(billNo, crb.getAqyear(), crb.getAqmonth());
            
            comonScheduleDao.getConveyanceSchedulePDF(document, billNo, crb, dataList);
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }
    
}
