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
import hrms.model.payroll.schedule.ItScheduleBean;
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

@Controller
public class ITScheduleController implements ServletContextAware{
    
    @Autowired
    public ScheduleDAO comonScheduleDao;
    
    @Autowired
    public PayBillDMPDAO paybillDmpDao;
    
    private ServletContext context;

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.context = servletContext;
    }
    
    @RequestMapping(value = "ITScheduleHTML",method = {RequestMethod.POST,RequestMethod.GET})
    public ModelAndView ITScheduleHTML(@RequestParam("billNo") String billNo, @RequestParam("schedule") String schedule){

        ModelAndView mav = null;
        
        List itEmpList = null;
        ItScheduleBean itHeaderDtls=null;
        String btId=null;
        double dedutAmt=0.0;
        double totDedutAmt=0.0;
        String totalFig=null;
        try{
            mav = new ModelAndView();
            CommonReportParamBean crb=paybillDmpDao.getCommonReportParameter(billNo);
            itHeaderDtls = comonScheduleDao.getITScheduleHeaderDetails(billNo, schedule);
            itEmpList = comonScheduleDao.getITScheduleEmployeeList(billNo, schedule, crb.getAqmonth(),crb.getAqyear());
            
            ItScheduleBean obj = null;
            if(itEmpList != null && itEmpList.size() > 0){
                obj = new ItScheduleBean();
                for(int i = 0; i < itEmpList.size(); i++){
                    obj = (ItScheduleBean)itEmpList.get(i);
                    dedutAmt = Double.parseDouble(obj.getEmpDedutAmount());
                    totDedutAmt=totDedutAmt+dedutAmt;
                    totalFig=Numtowordconvertion.convertNumber((int)totDedutAmt);
                }
            }

            mav.addObject("offName",comonScheduleDao.getCommonReportParameter(billNo).getOfficeen());
            mav.addObject("ITEmpList",itEmpList);
            mav.addObject("Schdule",schedule);
            mav.addObject("ITHeader",itHeaderDtls);
            mav.addObject("DedutAmount",totDedutAmt);
            mav.addObject("TotalFig",totalFig);
            mav.addObject("VchNo", crb.getVchNo());
            mav.addObject("VchDate", crb.getVchDate());
            
            if(schedule.equals("IT")){
                mav.addObject("SchName", "IT Schedule");                
            }else if(schedule.equals("HC")){
                mav.addObject("SchName", "HC Schedule");                
            }else if(schedule.equals("CGEGIS")){
                mav.addObject("SchName", "CEGIS Schedule");                
            }else if(schedule.equals("GIS")){
                mav.addObject("SchName", "GIS Schedule");
            }else if(schedule.equals("ZAO-CBDT")){
                mav.addObject("SchName", "ZAO-CBDT Schedule");
            }else if(schedule.equals("PAO-SUSP-ZAO-CBDT")){
                mav.addObject("SchName", "PAO-SUSPENSION ZAO-CBDT Schedule");
            }else if(schedule.equals("PAO-SUSP-GIS")){
                mav.addObject("SchName", "PAO-SUSPENSION AIS/GIS Schedule");
            }
            
            mav.setViewName("/payroll/schedule/ITSchedule");
            
        }catch(Exception e){
            e.printStackTrace();
        } 
        return mav;  
    }
    
    @RequestMapping(value = "ITSchedulePDF")
    public void ITSchedulePagePDF(HttpServletResponse response, @RequestParam("billNo") String billNo, @RequestParam("schedule") String schedule) {
        response.setContentType("application/pdf");
        Document document = new Document(PageSize.A4);
        try {
            
            if(schedule.equals("IT")){
                response.setHeader("Content-Disposition", "attachment; filename=ITSchedule_"+billNo+".pdf");
            }else if(schedule.equals("HC")){
                response.setHeader("Content-Disposition", "attachment; filename=HCSchedule_"+billNo+".pdf");
            }else if(schedule.equals("CGEGIS")){
                response.setHeader("Content-Disposition", "attachment; filename=CEGISSchedule_"+billNo+".pdf");
            }else if(schedule.equals("GIS")){
                response.setHeader("Content-Disposition", "attachment; filename=GISSchedule_"+billNo+".pdf");
            }
            
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();

            CommonReportParamBean crb = paybillDmpDao.getCommonReportParameter(billNo);
            ItScheduleBean itBean = comonScheduleDao.getITScheduleHeaderDetails(billNo, schedule);
            List empList = comonScheduleDao.getITScheduleEmployeeList(billNo, schedule, crb.getAqmonth(),crb.getAqyear());
            
            comonScheduleDao.ITSchedulePagePDF(crb, document, schedule, billNo, itBean, empList);
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }
    
}
