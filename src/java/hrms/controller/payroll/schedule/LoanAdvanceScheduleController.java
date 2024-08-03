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
import hrms.model.payroll.schedule.LoanAdvanceScheduleBean;
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
public class LoanAdvanceScheduleController implements ServletContextAware {

    @Autowired
    public ScheduleDAO comonScheduleDao;
    
    @Autowired
    public PayBillDMPDAO paybillDmpDao;

    private ServletContext context;

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.context = servletContext;
    }

    @RequestMapping(value = "LoanAdvScheduleHTML", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView LoanAdvanceHTML(@RequestParam("schedule") String schedule, @RequestParam("billNo") String billNo) {

        ModelAndView mav = null;
        LoanAdvanceScheduleBean loanAdvHeaderDtls = null;
        List principalList = null;
        List interestList = null;
        double totalP = 0.0;
        String totalPFig = null;
        double totalI = 0.0;
        String totalIFig = null;
        
        try {
            mav = new ModelAndView();
            CommonReportParamBean crb=paybillDmpDao.getCommonReportParameter(billNo);
            loanAdvHeaderDtls = comonScheduleDao.getLoanAdvanceScheduleHeaderDetails(schedule, billNo);
            principalList = comonScheduleDao.getLoanAdvanceSchedulePrincipalList(schedule, billNo,crb.getAqmonth(),crb.getAqyear());
            interestList = comonScheduleDao.getLoanAdvanceScheduleInterestList(schedule, billNo,crb.getAqmonth(),crb.getAqyear());

            LoanAdvanceScheduleBean objP = null;
            if (principalList != null && principalList.size() > 0) {
                objP = new LoanAdvanceScheduleBean();
                for (int i = 0; i < principalList.size(); i++) {
                    objP = (LoanAdvanceScheduleBean) principalList.get(i);
                    totalP = objP.getTotal();
                    totalPFig = Numtowordconvertion.convertNumber((int) totalP).toUpperCase();
                }
            }

            LoanAdvanceScheduleBean objI = null;
            if (interestList != null && interestList.size() > 0) {
                objI = new LoanAdvanceScheduleBean();
                for (int i = 0; i < interestList.size(); i++) {
                    objI = (LoanAdvanceScheduleBean) interestList.get(i);
                    totalI = objI.getTotal();
                    totalIFig = Numtowordconvertion.convertNumber((int) totalI).toUpperCase();
                }
            }

            mav.addObject("EmpPrincpalList", principalList);
            mav.addObject("EmpInterestList", interestList);
            mav.addObject("AllHeader", loanAdvHeaderDtls);
            mav.addObject("TotalAmtP", totalP);
            mav.addObject("TotalPFig", totalPFig);
            mav.addObject("TotalAmtI", totalI);
            mav.addObject("TotalIFig", totalIFig);
            mav.addObject("VchNo", crb.getVchNo());
            mav.addObject("VchDate", crb.getVchDate());

            if (schedule.equalsIgnoreCase("HBA")) {
                mav.setViewName("/payroll/schedule/HBASchedule"); // House Building

            } else if (schedule.equalsIgnoreCase("VE")) {
                mav.setViewName("/payroll/schedule/VESchedule");// Motor Car 

            } else if (schedule.equalsIgnoreCase("SHBA")) {
                mav.setViewName("/payroll/schedule/SHBASchedule");// HOUSE BUILDING ADVANCE FOR CYCLONE 

            } else if (schedule.equalsIgnoreCase("MCA")) {
                mav.setViewName("/payroll/schedule/MCASchedule");// MOTOR CYCLE ADVANCE 

            } else if (schedule.equalsIgnoreCase("MOPA")) {
                mav.setViewName("/payroll/schedule/MOPASchedule");// MOPED 
                
            }else if (schedule.equalsIgnoreCase("CMPA")) {
                mav.setViewName("/payroll/schedule/CMPASchedule");// COMPUTER ADVANCE SCHEDULE 
                
            }else if (schedule.equalsIgnoreCase("OVDL")) {
                mav.setViewName("/payroll/schedule/OverDrawl");// COMPUTER ADVANCE SCHEDULE 
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }
    
    @RequestMapping(value = "LoanAdvSchedulePDF")
    public void LoanSchedulePagePDF(HttpServletResponse response, @RequestParam("schedule") String schedule, @RequestParam("billNo") String billNo) {
        response.setContentType("application/pdf");
        Document document = new Document(PageSize.A4);
        
        try {
            
            if (schedule.equalsIgnoreCase("HBA")) {
                response.setHeader("Content-Disposition", "attachment; filename=HBASchedule_"+billNo+".pdf"); // HOUSE BUILDING

            } else if (schedule.equalsIgnoreCase("VE")) {
                response.setHeader("Content-Disposition", "attachment; filename=VESchedule_"+billNo+".pdf"); // MOTOR CAR 

            } else if (schedule.equalsIgnoreCase("SHBA")) {
                response.setHeader("Content-Disposition", "attachment; filename=SHBASchedule_"+billNo+".pdf"); // HOUSE BUILDING ADVANCE FOR CYCLONE 

            } else if (schedule.equalsIgnoreCase("MCA")) {
                response.setHeader("Content-Disposition", "attachment; filename=MCASchedule_"+billNo+".pdf"); // MOTOR CYCLE ADVANCE 

            } else if (schedule.equalsIgnoreCase("MOPA")) {
                response.setHeader("Content-Disposition", "attachment; filename=MOPASchedule_"+billNo+".pdf"); // MOPED 
                
            }else if (schedule.equalsIgnoreCase("CMPA")) {
                response.setHeader("Content-Disposition", "attachment; filename=CMPASchedule_"+billNo+".pdf"); // COMPUTER ADVANCE SCHEDULE 
            } else if (schedule.equalsIgnoreCase("EV")) {
                response.setHeader("Content-Disposition", "attachment; filename=EVSchedule_"+billNo+".pdf"); // ELECTRIC VEHICLE SCHEDULE 
            }
                        
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();

            CommonReportParamBean crb = paybillDmpDao.getCommonReportParameter(billNo);
            LoanAdvanceScheduleBean laBean = comonScheduleDao.getLoanAdvanceScheduleHeaderDetails(schedule, billNo);
            List principalList = comonScheduleDao.getLoanAdvanceSchedulePrincipalList(schedule, billNo,crb.getAqmonth(),crb.getAqyear());
            List interestList = comonScheduleDao.getLoanAdvanceScheduleInterestList(schedule, billNo,crb.getAqmonth(),crb.getAqyear());
            
            comonScheduleDao.LoanSchedulePagePDF(crb, document, schedule, billNo, laBean, principalList, interestList);
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }
}
