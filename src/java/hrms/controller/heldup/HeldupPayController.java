/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.heldup;

import hrms.dao.employee.EmployeeDAO;
import hrms.dao.heldup.RegularHeldUpDAO;
import hrms.dao.payroll.billbrowser.BillGroupDAO;
import hrms.model.employee.Employee;
import hrms.model.heldup.RegularHeldUpBean;
import hrms.model.login.LoginUserBean;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Manas
 */
@Controller
@SessionAttributes("LoginUserBean")
public class HeldupPayController {

    @Autowired
    BillGroupDAO billGroupDAO;
    @Autowired
    RegularHeldUpDAO regularHeldUpDAO;
    @Autowired
    EmployeeDAO employeeDAO;

    @RequestMapping(value = "payheldupregular")
    public ModelAndView payheldupregular(HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletResponse response, @ModelAttribute("RegularHeldUpBean") RegularHeldUpBean regularHeldUpBean) {
        ModelAndView mv = new ModelAndView("heldup/HeldupPay");
        //mv.addObject("yearlist", regularHeldUpDAO.getyearList(lub.getOffcode()));
        //mv.addObject("monthlist", regularHeldUpDAO.getmonthList());
        mv.addObject("billgrouplist", billGroupDAO.getActiveBillGroupList(lub.getLoginoffcode()));
        return mv;
    }

    @RequestMapping(value = "getbillgroupwiseemployee")
    public ModelAndView getbillgroupwiseemployee(HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletResponse response, @ModelAttribute("RegularHeldUpBean") RegularHeldUpBean regularHeldUpBean) {
        ModelAndView mv = new ModelAndView("heldup/HeldupPay");
        regularHeldUpBean.setOffcode(lub.getLoginoffcode());
        mv.addObject("RegularHeldUpBean", regularHeldUpBean);
        mv.addObject("billgrouplist", billGroupDAO.getBillGroupList(lub.getLoginoffcode()));
        mv.addObject("emplist", regularHeldUpDAO.getBillGroupWiseEmployee(regularHeldUpBean.getBillgroupid()));
        return mv;
    }

    @RequestMapping(value = "heldupemployeedetail")
    public ModelAndView heldupemployeedetail(HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletResponse response, @ModelAttribute("RegularHeldUpBean") RegularHeldUpBean regularHeldUpBean) {
        ModelAndView mv = new ModelAndView("heldup/HeldUp");
        Employee employee = employeeDAO.getEmployeeProfile(regularHeldUpBean.getHrmsId());
        regularHeldUpBean.setEmpname(employee.getFullname());
        regularHeldUpBean.setPost(employee.getPost());
        regularHeldUpBean.setGpfNo(employee.getGpfno());
        return mv;
    }

    @RequestMapping(value = "heldupemployee", params = "action=HeldUp")
    public ModelAndView heldupemployee(HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletResponse response, @ModelAttribute("RegularHeldUpBean") RegularHeldUpBean regularHeldUpBean) {
        ModelAndView mv = new ModelAndView("heldup/HeldupPay");
        regularHeldUpDAO.helduppay(regularHeldUpBean);
        mv.addObject("billgrouplist", billGroupDAO.getActiveBillGroupList(lub.getLoginoffcode()));
        mv.addObject("emplist", regularHeldUpDAO.getBillGroupWiseEmployee(regularHeldUpBean.getBillgroupid()));
        return mv;
    }

    @RequestMapping(value = "heldupemployee", params = "action=Back")
    public ModelAndView backheldupemployee(HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletResponse response, @ModelAttribute("RegularHeldUpBean") RegularHeldUpBean regularHeldUpBean) {
        ModelAndView mv = new ModelAndView("heldup/HeldupPay");
        regularHeldUpBean.setOffcode(lub.getLoginoffcode());
        mv.addObject("RegularHeldUpBean", regularHeldUpBean);
        mv.addObject("billgrouplist", billGroupDAO.getBillGroupList(lub.getLoginoffcode()));
        mv.addObject("emplist", regularHeldUpDAO.getBillGroupWiseEmployee(regularHeldUpBean.getBillgroupid()));
        return mv;
    }

    @RequestMapping(value = "releaseemployeedetail")
    public ModelAndView releaseemployeedetail(HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletResponse response, @ModelAttribute("RegularHeldUpBean") RegularHeldUpBean regularHeldUpBean) {
        ModelAndView mv = new ModelAndView("heldup/PayRelease");
        Employee employee = employeeDAO.getEmployeeProfile(regularHeldUpBean.getHrmsId());
        //regularHeldUpDAO.getPayHeldupData(regularHeldUpBean);
        regularHeldUpBean.setEmpname(employee.getFullname());
        regularHeldUpBean.setPost(employee.getPost());
        regularHeldUpBean.setGpfNo(employee.getGpfno());
        return mv;
    }

    @RequestMapping(value = "releasepay", params = "action=Release")
    public ModelAndView releasepay(HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletResponse response, @ModelAttribute("RegularHeldUpBean") RegularHeldUpBean regularHeldUpBean) {
        ModelAndView mv = new ModelAndView("heldup/HeldupPay");
        regularHeldUpDAO.releasepay(regularHeldUpBean);
        mv.addObject("billgrouplist", billGroupDAO.getActiveBillGroupList(lub.getLoginoffcode()));
        mv.addObject("emplist", regularHeldUpDAO.getBillGroupWiseEmployee(regularHeldUpBean.getBillgroupid()));
        return mv;
    }
    
    @RequestMapping(value = "releasepay", params = "action=Back")
    public ModelAndView backreleasepay(HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletResponse response, @ModelAttribute("RegularHeldUpBean") RegularHeldUpBean regularHeldUpBean) {
        ModelAndView mv = new ModelAndView("heldup/HeldupPay");
        regularHeldUpBean.setOffcode(lub.getLoginoffcode());
        mv.addObject("RegularHeldUpBean", regularHeldUpBean);
        mv.addObject("billgrouplist", billGroupDAO.getBillGroupList(lub.getLoginoffcode()));
        mv.addObject("emplist", regularHeldUpDAO.getBillGroupWiseEmployee(regularHeldUpBean.getBillgroupid()));
        return mv;
    }
}
