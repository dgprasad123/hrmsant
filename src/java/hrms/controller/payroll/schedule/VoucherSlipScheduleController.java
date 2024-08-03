/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.payroll.schedule;

import hrms.common.CommonFunctions;
import hrms.dao.payroll.schedule.PayBillDMPDAO;
import hrms.dao.payroll.schedule.ScheduleDAO;
import hrms.model.common.CommonReportParamBean;
import hrms.model.payroll.schedule.VoucherSlipBean;
import javax.servlet.ServletContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class VoucherSlipScheduleController implements ServletContextAware {

    @Autowired
    public ScheduleDAO comonScheduleDao;

    @Autowired
    public PayBillDMPDAO paybillDmpDao;

    private ServletContext context;

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.context = servletContext;
    }

    @RequestMapping(value = "VoucherSlipHTML", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView VoucherSlipHTML(@RequestParam("billNo") String billNo) {

        ModelAndView mav = null;
        VoucherSlipBean voucherBean = new VoucherSlipBean();

        String monthNam1 = "";
        String monthName2 = "";
        int year1;
        int year2;
        try {
            mav = new ModelAndView();
            CommonReportParamBean crb = paybillDmpDao.getCommonReportParameter(billNo);

            if (crb.getTypeofBill() != null && crb.getTypeofBill().equals("OTHER_ARREAR")) {
                voucherBean = comonScheduleDao.getVoucherSlipScheduleDetails(billNo, crb.getAqyear(), crb.getAqmonth());

                monthNam1 = CommonFunctions.getMonthAsString(crb.getFromMonth()-1);
                year1 = crb.getFromYear();
                monthName2 = CommonFunctions.getMonthAsString(crb.getToMonth()-1);
                year2 = crb.getToYear();

                mav.addObject("monthNam1", monthNam1);
                mav.addObject("year1", year1);
                mav.addObject("monthName2", monthName2);
                mav.addObject("year2", year2);

                mav.addObject("VoucherHeadr", voucherBean);
                mav.setViewName("/payroll/arrear/VoucherSlipArrear");
            } else {
                voucherBean = comonScheduleDao.getVoucherSlipScheduleDetails(billNo, crb.getAqyear(), crb.getAqmonth());

                mav.addObject("VoucherHeadr", voucherBean);
                mav.setViewName("/payroll/schedule/VoucherSlip");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }
     @RequestMapping(value = "VoucherSlipPDF", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView VoucherSlipPDF(@RequestParam("billNo") String billNo) {

        ModelAndView mav = null;
        VoucherSlipBean voucherBean = new VoucherSlipBean();

        String monthNam1 = "";
        String monthName2 = "";
        int year1;
        int year2;
        try {
            mav = new ModelAndView("voucherSlipPDFView");
            CommonReportParamBean crb = paybillDmpDao.getCommonReportParameter(billNo);

            if (crb.getTypeofBill() != null && crb.getTypeofBill().equals("OTHER_ARREAR")) {
                voucherBean = comonScheduleDao.getVoucherSlipScheduleDetails(billNo, crb.getAqyear(), crb.getAqmonth());

                monthNam1 = CommonFunctions.getMonthAsString(crb.getFromMonth()-1);
                year1 = crb.getFromYear();
                monthName2 = CommonFunctions.getMonthAsString(crb.getToMonth()-1);
                year2 = crb.getToYear();

                mav.addObject("monthNam1", monthNam1);
                mav.addObject("year1", year1);
                mav.addObject("monthName2", monthName2);
                mav.addObject("year2", year2);

                mav.addObject("VoucherHeadr", voucherBean);
               // mav.setViewName("/payroll/arrear/VoucherSlipArrear");
            } else {
                voucherBean = comonScheduleDao.getVoucherSlipScheduleDetails(billNo, crb.getAqyear(), crb.getAqmonth());

                mav.addObject("VoucherHeadr", voucherBean);
               // mav.setViewName("/payroll/schedule/VoucherSlip");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

}
