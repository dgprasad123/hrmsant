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
import hrms.model.payroll.schedule.GisAndFaScheduleBean;
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
public class GisAndFaScheduleController implements ServletContextAware {

    @Autowired
    public ScheduleDAO comonScheduleDao;

    @Autowired
    public PayBillDMPDAO paybillDmpDao;

    private ServletContext context;

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.context = servletContext;
    }

    @RequestMapping(value = "GisAndFaHTML", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView GisAndFaHTML(@RequestParam("schedule") String schedule, @RequestParam("billNo") String billNo) {

        ModelAndView mav = null;
        List empList = null;
        GisAndFaScheduleBean gisAndFaBean = null;
        int total = 0;
        String totalFig = null;
        try {
            mav = new ModelAndView();
            CommonReportParamBean crb = paybillDmpDao.getCommonReportParameter(billNo);
            gisAndFaBean = comonScheduleDao.getGisAndFaScheduleHeaderDetails(schedule, billNo);
            empList = comonScheduleDao.getGISandFAScheduleEmpList(schedule, billNo, crb.getAqyear(), crb.getAqmonth());

            GisAndFaScheduleBean obj = null;
            if (empList != null && empList.size() > 0) {
                obj = new GisAndFaScheduleBean();
                for (int i = 0; i < empList.size(); i++) {
                    obj = (GisAndFaScheduleBean) empList.get(i);
                    total = total + Integer.parseInt(obj.getDeductedAmount());
                    totalFig = Numtowordconvertion.convertNumber((int) total).toUpperCase();
                }
            }

            mav.addObject("GisAndFaHeader", gisAndFaBean);
            mav.addObject("GisAndFaList", empList);
            mav.addObject("TotalAmt", total);
            mav.addObject("TotalFig", totalFig);
            mav.setViewName("/payroll/schedule/GisAndFaSchedule");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "GisAndFaPDF", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView GisAndFaPDF(@RequestParam("schedule") String schedule, @RequestParam("billNo") String billNo) {
        int total = 0;
        String totalFig = null;
        ModelAndView mav = new ModelAndView("gisAndFaPDFView");
        CommonReportParamBean crb = paybillDmpDao.getCommonReportParameter(billNo);
        GisAndFaScheduleBean gisAndFaBean = comonScheduleDao.getGisAndFaScheduleHeaderDetails(schedule, billNo);
        List empList = comonScheduleDao.getGISandFAScheduleEmpList(schedule, billNo, crb.getAqyear(), crb.getAqmonth());
        GisAndFaScheduleBean obj = null;
        if (empList != null && empList.size() > 0) {
            obj = new GisAndFaScheduleBean();
            for (int i = 0; i < empList.size(); i++) {
                obj = (GisAndFaScheduleBean) empList.get(i);
                total = total + Integer.parseInt(obj.getDeductedAmount());
                totalFig = Numtowordconvertion.convertNumber((int) total).toUpperCase();
            }
        }
        mav.addObject("GisAndFaHeader", gisAndFaBean);
        mav.addObject("GisAndFaList", empList);
        mav.addObject("TotalAmt", total);
        mav.addObject("TotalFig", totalFig);
        return mav;
    }

}
