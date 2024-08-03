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
import hrms.model.payroll.schedule.AuditRecoveryBean;
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
public class AuditRecoveryController implements ServletContextAware {

    @Autowired
    public ScheduleDAOImpl comonScheduleDao;

    @Autowired
    public PayBillDMPDAO paybillDmpDao;

    private ServletContext context;

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.context = servletContext;
    }

    @RequestMapping(value = "AuditRecoveryHTML", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView AuditRecoveryHTML(@RequestParam("billNo") String billNo) {

        ModelAndView mav = null;
        List empList = null;
        AuditRecoveryBean auditBean = new AuditRecoveryBean();
        int dedutAmtTot = 0;
        String totalFig = "";
        try {
            mav = new ModelAndView();

            CommonReportParamBean crb = paybillDmpDao.getCommonReportParameter(billNo);

            auditBean = comonScheduleDao.getAuditRecoveryScheduleDetails(billNo);
            empList = comonScheduleDao.getAuditRecoveryScheduleEmpDetails(billNo, crb.getAqyear(), crb.getAqmonth());

            AuditRecoveryBean obj = null;
            if (empList != null && empList.size() > 0) {
                obj = new AuditRecoveryBean();
                for (int i = 0; i < empList.size(); i++) {
                    obj = (AuditRecoveryBean) empList.get(i);
                    dedutAmtTot = dedutAmtTot + Integer.parseInt(obj.getAmtDeduct());
                    totalFig = Numtowordconvertion.convertNumber((dedutAmtTot));
                }
            }

            mav.addObject("ARHeader", auditBean);
            mav.addObject("AREmpList", empList);
            mav.addObject("TotalDAmt", dedutAmtTot);
            mav.addObject("TotalFig", totalFig);
            mav.setViewName("/payroll/schedule/AuditRecoverySchedule");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "AuditRecoveryPDF", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView AuditRecoveryPDF(@RequestParam("billNo") String billNo) {

        ModelAndView mav = null;
        List empList = null;
        AuditRecoveryBean auditBean = new AuditRecoveryBean();
        int dedutAmtTot = 0;
        String totalFig = "";
        try {
            mav = new ModelAndView("auditRecoveryPDFView");
            CommonReportParamBean crb = paybillDmpDao.getCommonReportParameter(billNo);
            auditBean = comonScheduleDao.getAuditRecoveryScheduleDetails(billNo);
            empList = comonScheduleDao.getAuditRecoveryScheduleEmpDetails(billNo, crb.getAqyear(), crb.getAqmonth());

            AuditRecoveryBean obj = null;
            if (empList != null && empList.size() > 0) {
                obj = new AuditRecoveryBean();
                for (int i = 0; i < empList.size(); i++) {
                    obj = (AuditRecoveryBean) empList.get(i);
                    dedutAmtTot = dedutAmtTot + Integer.parseInt(obj.getAmtDeduct());
                    totalFig = Numtowordconvertion.convertNumber((dedutAmtTot));
                }
            }

            mav.addObject("ARHeader", auditBean);
            mav.addObject("AREmpList", empList);
            mav.addObject("TotalDAmt", dedutAmtTot);
            mav.addObject("TotalFig", totalFig);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

}
