package hrms.controller.report.budgetRequirement;

import hrms.dao.report.budgetRequirement.BudgetRequirementDAO;
import hrms.model.login.LoginUserBean;
import java.util.ArrayList;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

@Controller
@SessionAttributes("LoginUserBean")
public class BudgetRequirementController {
    
    @Autowired
    BudgetRequirementDAO budgetRequirementDAO;
    
    @RequestMapping(value = "DownloadBudgetRequirementExcel")
    public void downloadBudgetRequirementExcel(HttpServletResponse response,@ModelAttribute("LoginUserBean") LoginUserBean lub){
        
        try{
            ArrayList emplist = budgetRequirementDAO.getEmployeeList(lub.getLoginoffcode());
            
            budgetRequirementDAO.downloadBudgetRequirementDataExcel(response, lub.getLoginoffcode(), emplist);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
}
