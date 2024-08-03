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
import hrms.model.payroll.schedule.BankAcountScheduleBean;
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
public class BankAcountScheduleController implements ServletContextAware{
    
    @Autowired
    public ScheduleDAO comonScheduleDao;
    
    @Autowired
    public PayBillDMPDAO paybillDmpDao;
    
    private ServletContext context;

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.context = servletContext;
    }
    
    @RequestMapping(value = "BankAccountHTML",method = {RequestMethod.POST,RequestMethod.GET})
    public ModelAndView BankAccountHTML(@RequestParam("billNo") String billNo){
    
        ModelAndView mav = null;
        List empList = null;
        List empList1 = null;
        String total = "";
        String totalDdo = "";
        int netAmount = 0;
        int totRel = 0;
        int othDepo = 0;
        BankAcountScheduleBean bankAccBean=null;
        try{
            mav = new ModelAndView();
            
            CommonReportParamBean crb = paybillDmpDao.getCommonReportParameter(billNo);
            
            bankAccBean = comonScheduleDao.getBankAcountScheduleHeaderDetails(billNo);
            empList = comonScheduleDao.getBankAcountScheduleEmpList(billNo, crb.getAqyear(), crb.getAqmonth());
            //empList1 = comonScheduleDao.getBankNameScheduleList(billNo, crb.getAqyear(), crb.getAqmonth());
            
            BankAcountScheduleBean obj = null;
            if (empList != null && empList.size() > 0) {
                for (int i = 0; i < empList.size(); i++) {
                    obj = (BankAcountScheduleBean) empList.get(i);
                    netAmount = netAmount + Integer.parseInt(obj.getNetAmount());
                    totRel = totRel + obj.getTotalReleased();
                    othDepo = othDepo + obj.getOtherDeposits();
                    
                    //total = obj.getCarryForward();
                    //totalDdo = obj.getCarryForwardDDO();
                }
            }
        
            mav.addObject("BankAcHeader", bankAccBean);     
            mav.addObject("BankAccList", empList); 
            mav.addObject("NetTotal", netAmount);
            mav.addObject("TotalSbAc", totRel); 
            mav.addObject("TotalDDO", othDepo); 
            mav.setViewName("/payroll/schedule/BankAcountSchedule");
            
        }catch(Exception e){
            e.printStackTrace();
        } 
        return mav;  
    }
    @RequestMapping(value = "BankAccountPDF",method = {RequestMethod.POST,RequestMethod.GET})
    public ModelAndView BankAccountPDF(@RequestParam("billNo") String billNo){
    
        ModelAndView mav = null;
        List empList = null;
        List empList1 = null;
        String total = "";
        String totalDdo = "";
        int netAmount = 0;
        int totRel = 0;
        int othDepo = 0;
        String netAmtFig=null;
        BankAcountScheduleBean bankAccBean=null;
        try{
            mav = new ModelAndView("bankAccountPDFView");
            
            CommonReportParamBean crb = paybillDmpDao.getCommonReportParameter(billNo);
            
            bankAccBean = comonScheduleDao.getBankAcountScheduleHeaderDetails(billNo);
            empList = comonScheduleDao.getBankAcountScheduleEmpList(billNo, crb.getAqyear(), crb.getAqmonth());
            //empList1 = comonScheduleDao.getBankNameScheduleList(billNo, crb.getAqyear(), crb.getAqmonth());
            
            BankAcountScheduleBean obj = null;
            if (empList != null && empList.size() > 0) {
                for (int i = 0; i < empList.size(); i++) {
                    obj = (BankAcountScheduleBean) empList.get(i);
                    netAmount = netAmount + Integer.parseInt(obj.getNetAmount());
                    totRel = totRel + obj.getTotalReleased();
                    othDepo = othDepo + obj.getOtherDeposits();
                     netAmtFig = Numtowordconvertion.convertNumber((int)netAmount).toUpperCase();
                    //total = obj.getCarryForward();
                    //totalDdo = obj.getCarryForwardDDO();
                }
            }
        
            mav.addObject("BankAcHeader", bankAccBean);     
            mav.addObject("BankAccList", empList); 
            mav.addObject("NetTotal", netAmount);
            mav.addObject("TotalSbAc", totRel); 
            mav.addObject("TotalDDO", othDepo); 
            mav.addObject("NetAmtFig", netAmtFig); 
           
        }catch(Exception e){
            e.printStackTrace();
        } 
        return mav;  
    }
}
