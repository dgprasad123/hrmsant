/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.payroll.SalaryProjection;

import hrms.dao.payroll.billbrowser.BillBrowserDAO;
import hrms.model.login.LoginUserBean;
import hrms.model.payroll.billbrowser.BillBean;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Madhusmita
 */
@Controller
@SessionAttributes({"LoginUserBean", "SelectedEmpOffice"})
public class SalaryProjectionController {

    @Autowired
    BillBrowserDAO billBrowserDao;

    @RequestMapping(value = "salaryProjectionView.htm", method = RequestMethod.GET)
    public ModelAndView salaryProjectionView(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("BillBean") BillBean bbean,
            HttpServletResponse response, BindingResult result) {

        ModelAndView mv = new ModelAndView();
        List billYear = billBrowserDao.getBillYear(lub.getLoginempid());
        mv.addObject("billyear", billYear);
        mv.addObject("empnm", lub.getLoginname());
        mv.addObject("gpfNo", lub.getLogingpfno());

        mv.setViewName("/payroll/MySalaryProjection");
        return mv;

    }

    @RequestMapping(value = "mysalaryProjectionReport.htm", method = RequestMethod.POST)
    public ModelAndView mysalaryProjectionReport(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("BillBean") BillBean bbean,
            @RequestParam("finyear") String fYear,
            HttpServletResponse response, BindingResult result) {
        ModelAndView mv = new ModelAndView();
        mv.addObject("empnm", lub.getLoginname());
        mv.addObject("empid", lub.getLoginempid());
        mv.addObject("gpfNo", lub.getLogingpfno());
        bbean.setHidempid(lub.getLoginempid());
        System.out.println("Salaried");
        
        mv.setViewName("/payroll/MySalaryProjection");
        mv.addObject("billyear", billBrowserDao.getBillYear(lub.getLoginempid()));
        List salarydetails = billBrowserDao.getSalaryDetails(lub.getLoginempid(), fYear);
        mv.addObject("salDetails", salarydetails);

        return mv;

    }

}
