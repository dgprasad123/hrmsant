/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.payroll.schedule;

import hrms.dao.payroll.schedule.ScheduleDAOImpl;
import hrms.model.common.CommonReportParamBean;
import hrms.model.payroll.schedule.LicScheduleBean;
import java.util.List;
import hrms.common.Numtowordconvertion;
import java.util.ArrayList;
import javax.servlet.ServletContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LICScheduleController implements ServletContextAware {

    @Autowired
    public ScheduleDAOImpl comonScheduleDao;

    private ServletContext context;

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.context = servletContext;
    }

    @RequestMapping(value = "LICScheduleHTML", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView LICScheduleHTML(@RequestParam("schedule") String schedule, @RequestParam("billNo") String billNo) {
        ModelAndView mav = null;
        List empList = null;
        String grandTotal = "";
        String grandTotalFig = "";
        LicScheduleBean licBean = null;
        try {
            mav = new ModelAndView();
            CommonReportParamBean crb = comonScheduleDao.getCommonReportParameter(billNo);
            licBean = comonScheduleDao.getLICScheduleHeaderDetails(billNo);
            empList = comonScheduleDao.getLICScheduleEmpList(schedule, billNo, crb.getAqmonth(), crb.getAqyear());

            LicScheduleBean obj = null;
            if (empList != null && empList.size() > 0) {
                for (int i = 0; i < empList.size(); i++) {
                    obj = (LicScheduleBean) empList.get(i);
                    grandTotal = obj.getCarryForward();
                    grandTotalFig = Numtowordconvertion.convertNumber(Integer.parseInt(grandTotal));
                }
            }
           
            mav.addObject("licBeanHeader", licBean);
            mav.addObject("LicEmpList", empList);
            mav.addObject("GTotal", grandTotal);
            mav.addObject("GTFig", grandTotalFig);

            mav.setViewName("/payroll/schedule/LICSchedule");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "LICSchedulePDF", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView LICSchedulePDF(@RequestParam("schedule") String schedule, @RequestParam("billNo") String billNo) {
        ModelAndView mav = null;
        List empList = null;
        String grandTotal = "";
        String grandTotalFig = "";
        LicScheduleBean licBean = null;
        ArrayList premiumDtlsArray=null;
        try {

            mav = new ModelAndView("licSchedulePDFView");
            CommonReportParamBean crb = comonScheduleDao.getCommonReportParameter(billNo);
            licBean = comonScheduleDao.getLICScheduleHeaderDetails(billNo);
            empList = comonScheduleDao.getLICScheduleEmpList(schedule, billNo, crb.getAqmonth(), crb.getAqyear());

            LicScheduleBean obj = null;
            if (empList != null && empList.size() > 0) {
                for (int i = 0; i < empList.size(); i++) {
                    obj = (LicScheduleBean) empList.get(i);
                    grandTotal = obj.getCarryForward();
                     premiumDtlsArray=obj.getPremiumDetails();
                    grandTotalFig = Numtowordconvertion.convertNumber(Integer.parseInt(grandTotal));
                }
            }
              mav.addObject("crb", crb);
            mav.addObject("licBeanHeader", licBean);
            mav.addObject("LicEmpList", empList);
           // mav.addObject("GTotal", grandTotal);
             mav.addObject("premiumDtlsArray", premiumDtlsArray);
            mav.addObject("GTFig", grandTotalFig);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

}
