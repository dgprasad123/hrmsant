/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.controller.payroll.schedule;

import hrms.common.Numtowordconvertion;
import hrms.dao.payroll.schedule.PayBillDMPDAO;
import hrms.dao.payroll.schedule.ScheduleDAO;
import hrms.model.common.CommonReportParamBean;
import hrms.model.payroll.schedule.PLIScheduleBean;
import java.util.List;
import javax.servlet.ServletContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PLIScheduleController implements ServletContextAware{
    
    @Autowired
    public ScheduleDAO comonScheduleDao;
    
    @Autowired
    public PayBillDMPDAO paybillDmpDao;
    
    private ServletContext context;

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.context = servletContext;
    }
    
    @RequestMapping(value = "PLIScheduleHTML",method = {RequestMethod.POST,RequestMethod.GET})
    public ModelAndView PLIScheduleHTML(@RequestParam("billNo") String billNo){
        ModelAndView mav = null;
        List empList = null;
        PLIScheduleBean pliBean=null;
        int totalAmt=0;
        String totalAmtFig="";
        CommonReportParamBean crb = null;
        try{
            mav = new ModelAndView();
            
            crb = paybillDmpDao.getCommonReportParameter(billNo);
            
            pliBean = comonScheduleDao.getPLIScheduleHeaderDetails(billNo);
            empList = comonScheduleDao.getPLIScheduleEmpList(billNo,crb.getAqyear(),crb.getAqmonth()); 
            
            PLIScheduleBean obj=null;
            if(empList != null && empList.size() > 0){
                obj = new PLIScheduleBean();
                for(int i = 0; i < empList.size(); i++){
                    obj = (PLIScheduleBean)empList.get(i);
                    totalAmt=(int)Double.parseDouble(obj.getCarryForward());
                    totalAmtFig = Numtowordconvertion.convertNumber((int)totalAmt).toUpperCase();
                }
            }
            
            mav.addObject("pliHeader",pliBean);
            mav.addObject("PliEmpList",empList);
            mav.addObject("TotalAmt",totalAmt);
            mav.addObject("TotalAmtFig",totalAmtFig);
            mav.setViewName("/payroll/schedule/PLISchedule"); 
            
        }catch(Exception e){
            e.printStackTrace();
        } 
        return mav;  
    }
     @RequestMapping(value = "PLISchedulePDF",method = {RequestMethod.POST,RequestMethod.GET})
    public ModelAndView PLISchedulePDF(@RequestParam("billNo") String billNo){
        ModelAndView mav = null;
        List empList = null;
        PLIScheduleBean pliBean=null;
        int totalAmt=0;
        String totalAmtFig="";
        CommonReportParamBean crb = null;
        try{
            mav = new ModelAndView("pliSchedulePDFView");
            
            crb = paybillDmpDao.getCommonReportParameter(billNo);
            
            pliBean = comonScheduleDao.getPLIScheduleHeaderDetails(billNo);
            empList = comonScheduleDao.getPLIScheduleEmpList(billNo,crb.getAqyear(),crb.getAqmonth()); 
            
            PLIScheduleBean obj=null;
            if(empList != null && empList.size() > 0){
                obj = new PLIScheduleBean();
                for(int i = 0; i < empList.size(); i++){
                    obj = (PLIScheduleBean)empList.get(i);
                    totalAmt=(int)Double.parseDouble(obj.getCarryForward());
                    totalAmtFig = Numtowordconvertion.convertNumber((int)totalAmt).toUpperCase();
                }
            }
            
            mav.addObject("pliHeader",pliBean);
            mav.addObject("PliEmpList",empList);
            mav.addObject("TotalAmt",totalAmt);
            mav.addObject("TotalAmtFig",totalAmtFig);
           
            
        }catch(Exception e){
            e.printStackTrace();
        } 
        return mav;  
    }
}
