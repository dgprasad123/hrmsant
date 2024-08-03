/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.itdeclaration;

import hrms.dao.itdeclaration.InvestmentDeclarationDAO;
import hrms.dao.master.DepartmentDAO;
import hrms.model.itdeclaration.InvestmentDeclaration;
import hrms.model.login.LoginUserBean;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Surendra
 */
@Controller
@SessionAttributes("LoginUserBean")
public class InvestmentDeclarationController {

    @Autowired
    public InvestmentDeclarationDAO investmentDeclarationDao;

    @Autowired
    public DepartmentDAO deptDAO;

    @RequestMapping(value = "itdeclarationList")
    public ModelAndView ShowList(HttpServletRequest request, @ModelAttribute("InvestmentDeclaration") InvestmentDeclaration it,
            @ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletResponse response) {

        List datalist = investmentDeclarationDao.getInvestmentList(lub.getLoginempid());

        ModelAndView mav = new ModelAndView("itdeclaration/InvestmentDeclaraionList", "command", it);
        mav.addObject("datalist", datalist);

        List deptlist = deptDAO.getDepartmentList();
        mav.addObject("deptlist", deptlist);

        return mav;
    }

    @RequestMapping(value = "itdeclarationData")
    public ModelAndView createDeclaration(HttpServletRequest request, @ModelAttribute("InvestmentDeclaration") InvestmentDeclaration it,
            @ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletResponse response) {

        it = investmentDeclarationDao.showInvestmentDeclaration(lub.getLoginempid(), it);
        ModelAndView mav = new ModelAndView("itdeclaration/InvestmentDeclaraionData", "command", it);

        return mav;
    }

    @RequestMapping(value = "itdeclarationData", params = "submit=Edit")
    public ModelAndView createDeclarationEdit(HttpServletRequest request, @ModelAttribute("InvestmentDeclaration") InvestmentDeclaration it,
            @ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletResponse response) {

        it = investmentDeclarationDao.editInvestmentDeclaration(lub.getLoginempid(), "");
        ModelAndView mav = new ModelAndView("itdeclaration/InvestmentDeclaraionData", "command", it);

        return mav;
    }

    @RequestMapping(value = "itdeclarationData", params = "submit=Save")
    public ModelAndView createDeclarationSave(HttpServletRequest request, @ModelAttribute("InvestmentDeclaration") InvestmentDeclaration it,
            @ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletResponse response) {

        investmentDeclarationDao.saveInvestmentDeclaration(lub.getLoginempid(), lub.getLoginoffcode(), lub.getLoginspc(), it);
        ModelAndView mav = new ModelAndView("itdeclaration/InvestmentDeclaraionData", "command", it);

        return mav;
    }

    @RequestMapping(value = "itdeclarationData", params = "submit=Submit")
    public ModelAndView createDeclarationSubmit(HttpServletRequest request, @ModelAttribute("InvestmentDeclaration") InvestmentDeclaration it,
            @ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletResponse response) {

        //investmentDeclarationDao.submitInvestmentDeclaration(lub.getLoginempid(), lub.getLoginoffcode(), lub.getLoginspc(), it);
        ModelAndView mav = new ModelAndView("itdeclaration/InvestmentDeclaraionData", "command", it);

        return mav;
    }

    @RequestMapping(value = "itdeclarationPdfData")
    public ModelAndView createDeclarationPdf(HttpServletRequest request, @ModelAttribute("InvestmentDeclaration") InvestmentDeclaration it,
            HttpServletResponse response) {
        // ModelAndView mav=new ModelAndView("itdeclaration/InvestmentDeclaraionData","command",it);
        ModelAndView mav = new ModelAndView("investmentDeclarationPDFView");
        return mav;
    }
    
    @RequestMapping(value = "itdeclarationListDDOWise")
    public ModelAndView officeWiseDeclarationList(HttpServletRequest request, @ModelAttribute("InvestmentDeclaration") InvestmentDeclaration it,
            @ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletResponse response) {
        
        ModelAndView mav=new ModelAndView("itdeclaration/InvestmentDeclaraionListDDOWise","command",it);
        
        return mav;  
    }
}
