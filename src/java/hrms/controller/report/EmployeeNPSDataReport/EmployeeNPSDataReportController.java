package hrms.controller.report.EmployeeNPSDataReport;

import hrms.dao.payroll.payslip.PaySlipDAO;
import hrms.dao.report.EmployeeNPSDataReport.EmployeeNPSDataReportDAO;
import hrms.model.login.LoginUserBean;
import hrms.model.payroll.payslip.PaySlipListBean;
import hrms.model.report.EmployeeNPSDataReport.EmployeeNPSDataReportForm;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

@Controller
@SessionAttributes("LoginUserBean")
public class EmployeeNPSDataReportController {

    @Autowired
    EmployeeNPSDataReportDAO employeeNPSDataDAO;
    
    @Autowired
    public PaySlipDAO payslipDao;
    
    @RequestMapping(value = "EmployeeNPSDataReportPage")
    public String EmployeeNPSDataReportPage(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("EmployeeNPSDataReportForm") EmployeeNPSDataReportForm empdataform) {

        return "/report/EmployeeNPSDataReport/EmployeeNPSDataReport";
    }
    
    @RequestMapping(value = "EmployeeNPSDataReport", params = {"submit=Show"})
    public String EmployeeNPSDataReport(Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("EmployeeNPSDataReportForm") EmployeeNPSDataReportForm empdataform) {
        
        List emppayslip = null;
        
        try {
            if (empdataform.getTxtpranno() != null && !empdataform.getTxtpranno().equals("")) {
                empdataform = employeeNPSDataDAO.getEmployeeNPSAquitanceData(empdataform.getTxtpranno(), empdataform.getSltYear(), empdataform.getSltMonth());
                model.addAttribute("EmployeeNPSDataReportForm", empdataform);
            }
            
            if ((empdataform.getTxthrmsid() != null && !empdataform.getTxthrmsid().equals(""))) {
                
                String empid = empdataform.getTxthrmsid();
                int year = Integer.parseInt(empdataform.getSltYear());
                int month = Integer.parseInt(empdataform.getSltMonth());
                
                empdataform.setTxthrmsid(empid);
                empdataform.setSltYear(year+"");
                empdataform.setSltMonth(month+"");
                
                String billNo = payslipDao.getTokenGeneratedBillNo(empid, year, month);

                if (billNo != null && !billNo.equals("")) {
                    emppayslip = payslipDao.getPaySlip(empid, year, month);                    
                    model.addAttribute("emppayslip", emppayslip);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/report/EmployeeNPSDataReport/EmployeeNPSDataReport";
    }
    
    @RequestMapping(value = "EmployeeNPSDataReport", params = {"submit=Show Payslip"})
    public String EmployeeNPSDataReportPaySlip(Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("EmployeeNPSDataReportForm") EmployeeNPSDataReportForm empdataform) {
        
        List emppayslip = null;
        
        try {if ((empdataform.getTxthrmsid() != null && !empdataform.getTxthrmsid().equals(""))) {
                
                String empid = empdataform.getTxthrmsid();
                int year = Integer.parseInt(empdataform.getSltPaySlipYear());
                int month = Integer.parseInt(empdataform.getSltPaySlipMonth());
                
                empdataform.setTxthrmsid(empid);
                empdataform.setSltYear(year+"");
                empdataform.setSltMonth(month+"");
                
                String billNo = payslipDao.getTokenGeneratedBillNo(empid, year, month);

                if (billNo != null && !billNo.equals("")) {
                    emppayslip = payslipDao.getPaySlip(empid, year, month);                    
                    model.addAttribute("emppayslip", emppayslip);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/report/EmployeeNPSDataReport/EmployeeNPSDataReport";
    }
}
