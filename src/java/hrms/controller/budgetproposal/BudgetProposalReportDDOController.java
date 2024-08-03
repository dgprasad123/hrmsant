/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.budgetproposal;

import hrms.dao.budgetproposal.BudgetProposoalDAO;
import hrms.model.SingleBill.SinglePayBillForm;
import hrms.model.budgetproposal.BudgetProposal;
import hrms.model.login.LoginUserBean;
import hrms.model.master.Treasury;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Surendra
 */
@Controller
@SessionAttributes({"LoginUserBean", "SelectedEmpOffice"})
public class BudgetProposalReportDDOController {

    @Autowired
    public BudgetProposoalDAO budgetDAO;

    @RequestMapping(value = "budgetProposalDDOList", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView budgetProposalDDOList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("budget") BudgetProposal budget  ) {

        ModelAndView mav = null;

        try {
            mav = new ModelAndView();
            List fylist = budgetDAO.getFinancialYearList();
            mav.addObject("financialYearList", fylist);
            mav.setViewName("/budgetProposal/BudgetProposoalReportForDDO");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "createBudgetProposal", params = {"btnAer=Search"})
    public ModelAndView LoadDdoDetailsData(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("budget") BudgetProposal budget  ) {

        ModelAndView mav = null;

        try {
            mav = new ModelAndView();
            List fylist = budgetDAO.getFinancialYearList();
            mav.addObject("financialYearList", fylist);
            mav.setViewName("/budgetProposal/BudgetProposoalReportForDDO");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }  
      
      
    @RequestMapping(value = "createBudgetProposal", params = {"btnAer=CreateBudgetProposal"})
    public ModelAndView createBudgetProposal(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("budget") BudgetProposal budget) {
        
        
        
        ModelAndView mav = new ModelAndView();
        boolean disableSingleCO = false;
        mav = new ModelAndView("report/CreateAERData", "command", budget);
        //mav.setViewName("/budgetProposal/BudgetProposalBillList");
        
        //mav.addObject("status", status);
        mav.addObject("disableSingleCO", disableSingleCO);
        mav.addObject("OffName", lub.getLoginoffname());
        return mav;

       
    }    
    
}
