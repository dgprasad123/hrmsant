/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.controller.payroll.schedule;

import hrms.common.Numtowordconvertion;
import hrms.dao.payroll.schedule.PayBillDMPDAO;
import hrms.dao.payroll.schedule.ScheduleDAOImpl;
import hrms.model.common.CommonReportParamBean;
import hrms.model.payroll.schedule.PrivateLoanScheduleBean;
import hrms.model.payroll.schedule.ScheduleHelper;
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
public class PrivateLoanController implements ServletContextAware{
    
    @Autowired
    public ScheduleDAOImpl comonScheduleDao;
    
    @Autowired
    public PayBillDMPDAO paybillDmpDao;
    
    @Autowired
    private ServletContext context;

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.context = servletContext;
    }
    
    @RequestMapping(value = "PrivateLoanHTML",method = {RequestMethod.POST,RequestMethod.GET})
    public ModelAndView PrivateLoanHTML(@RequestParam("billNo") String billNo){
    
        ModelAndView mav = null;
        List empList = null;
        int deductAmt = 0;
        String deductAmtFig = "";
        PrivateLoanScheduleBean loanBean = new PrivateLoanScheduleBean();
        try{
            mav = new ModelAndView();
            CommonReportParamBean crb=paybillDmpDao.getCommonReportParameter(billNo);
            loanBean = comonScheduleDao.getPrivateLoanScheduleDetails(billNo);
            loanBean.setDeductedAmtDesc("PRIVATE LOAN / DEDUCTION");
            empList = comonScheduleDao.getPrivateLoanScheduleEmpDetails(billNo,crb.getAqmonth(),crb.getAqyear());
            
            ScheduleHelper obj = null;
            if(empList != null && empList.size() > 0){
                obj = new ScheduleHelper();
                for(int i = 0; i < empList.size(); i++){
                    obj = (ScheduleHelper)empList.get(i);
                    
                    deductAmt = obj.getDeductAmt();
                    deductAmtFig = Numtowordconvertion.convertNumber((int) deductAmt);
                }
            }
            
            mav.addObject("PLHeader", loanBean); 
            mav.addObject("PLEmpList", empList);
            mav.addObject("TotDAmt", deductAmt);
            mav.addObject("TotDAmtFig", deductAmtFig);
            mav.setViewName("/payroll/schedule/PrivateLoanSchedule");
            
        }catch(Exception e){
            e.printStackTrace();
        } 
        return mav;  
    }
    
    @RequestMapping(value = "cmrfScheduleHTML",method = {RequestMethod.POST,RequestMethod.GET})
    public ModelAndView cmrfScheduleHTML(@RequestParam("billNo") String billNo){
    
        ModelAndView mav = null;
        List empList = null;
        int deductAmt = 0;
        String deductAmtFig = "";
        PrivateLoanScheduleBean loanBean = new PrivateLoanScheduleBean();
        try{
            mav = new ModelAndView();
            CommonReportParamBean crb=paybillDmpDao.getCommonReportParameter(billNo);
            loanBean = comonScheduleDao.getPrivateLoanScheduleDetails(billNo);
            loanBean.setDeductedAmtDesc("CHIEF MINISTER RELIEF FUND");
            empList = comonScheduleDao.getCmrfScheduleEmpDetails(billNo,crb.getAqmonth(),crb.getAqyear());
            
            ScheduleHelper obj = null;
            if(empList != null && empList.size() > 0){
                obj = new ScheduleHelper();
                for(int i = 0; i < empList.size(); i++){
                    obj = (ScheduleHelper)empList.get(i);
                    
                    deductAmt = obj.getDeductAmt();
                    deductAmtFig = Numtowordconvertion.convertNumber((int) deductAmt);
                }
            }
            
            mav.addObject("PLHeader", loanBean); 
            mav.addObject("PLEmpList", empList);
            mav.addObject("TotDAmt", deductAmt);
            mav.addObject("TotDAmtFig", deductAmtFig);
            mav.setViewName("/payroll/schedule/PrivateLoanSchedule");
            
        }catch(Exception e){
            e.printStackTrace();
        } 
        return mav;  
    }
}
