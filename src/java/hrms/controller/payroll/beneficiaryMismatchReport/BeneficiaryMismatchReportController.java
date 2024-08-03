package hrms.controller.payroll.beneficiaryMismatchReport;

import hrms.dao.payroll.beneficiaryMismatchReport.BeneficiaryMismatchReportDAO;
import hrms.model.login.LoginUserBean;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes("LoginUserBean")
public class BeneficiaryMismatchReportController {
    
    @Autowired
    BeneficiaryMismatchReportDAO beneficiaryMismatchReportDAO;
    
    @RequestMapping(value = "BeneficiaryMismatchReport")
    public String beneficiaryMismatchReport(ModelMap model, HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub){
        try{
            ArrayList beneficiaryList = beneficiaryMismatchReportDAO.getBeneficiaryMismatchReport(lub.getLoginoffcode());
            model.addAttribute("beneficiaryList", beneficiaryList);
            
            
        }catch(Exception e){
            e.printStackTrace();
        }
      return "/payroll/beneficiaryMismatchReport/BeneficiaryMismatchReport";  
    }
    
}
