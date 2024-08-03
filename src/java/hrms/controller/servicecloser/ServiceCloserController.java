/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.servicecloser;

import hrms.dao.employee.EmployeeDAO;
import hrms.dao.master.DepartmentDAO;
import hrms.dao.master.OfficeDAO;
import hrms.dao.master.SubStantivePostDAO;
import hrms.dao.servicecloser.ServiceCloserDAO;
import hrms.model.employee.Employee;
import hrms.model.login.LoginUserBean;
import hrms.model.login.Users;
import hrms.model.servicecloser.EmployeeDeceased;
import hrms.model.servicecloser.Retirement;
import java.util.Calendar;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Manas
 */
@Controller
@SessionAttributes({"LoginUserBean", "SelectedEmpObj"})
public class ServiceCloserController {

    @Autowired
    ServiceCloserDAO serviceCloserDAO;
    @Autowired
    EmployeeDAO employeeDAO;
    @Autowired
    DepartmentDAO departmentDAO;
    @Autowired
    OfficeDAO offDAO;
    @Autowired
    SubStantivePostDAO substantivePostDAO;

    @RequestMapping(value = "ShowDeceasedForm")
    public ModelAndView ShowDeceasedForm(@ModelAttribute("SelectedEmpObj") Users selectedEmpObj) {
        ModelAndView mv = new ModelAndView();
        Employee employee = employeeDAO.getEmployeeProfile(selectedEmpObj.getEmpId());
        EmployeeDeceased employeeDeceased = serviceCloserDAO.getEmployeeDeceasedData(selectedEmpObj.getEmpId());
        employeeDeceased.setEmpId(employee.getEmpid());
        employeeDeceased.setCurspc(employee.getSpc());
        mv.addObject("employee", employee);
        mv.addObject("EmployeeDeceased", employeeDeceased);
        mv.setViewName("/servicecloser/EmpDeceased");
        return mv;
    }

    @RequestMapping(value = "SaveEmployeeDeceased", params = "action=Save")
    public ModelAndView SaveEmployeeDeceased(@ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("EmployeeDeceased") EmployeeDeceased employeeDeceased) {
        ModelAndView mv = new ModelAndView();
        serviceCloserDAO.saveEmployeeDeceased(employeeDeceased);
        Employee employee = employeeDAO.getEmployeeProfile(selectedEmpObj.getEmpId());
        mv.addObject("employee", employee);
        mv.addObject("EmployeeDeceased", serviceCloserDAO.getEmployeeDeceasedData(selectedEmpObj.getEmpId()));
        mv.setViewName("/servicecloser/EmpDeceased");
        return mv;
    }

    @RequestMapping(value = "SaveEmployeeDeceased", params = "action=Delete")
    public ModelAndView DeleteEmployeeDeceased(@ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("EmployeeDeceased") EmployeeDeceased employeeDeceased) {
        ModelAndView mv = new ModelAndView();
        serviceCloserDAO.deleteEmployeeDeceased(employeeDeceased);
        Employee employee = employeeDAO.getEmployeeProfile(selectedEmpObj.getEmpId());
        mv.addObject("employee", employee);
        mv.addObject("EmployeeDeceased", serviceCloserDAO.getEmployeeDeceasedData(selectedEmpObj.getEmpId()));
        mv.setViewName("/servicecloser/EmpDeceased");
        return mv;
    }

    @RequestMapping(value = "ShowRetirmentForm")
    public ModelAndView ShowRetirmentForm(@ModelAttribute("SelectedEmpObj") Users selectedEmpObj) throws Exception {
        ModelAndView mv = new ModelAndView();
        String restrictUser = "N";
        Employee employee = employeeDAO.getEmployeeProfile(selectedEmpObj.getEmpId());
        Retirement retirement = serviceCloserDAO.getEmployeeRetirementData(selectedEmpObj.getEmpId());
        retirement.setDuedate(employee.getTxtDos());
        retirement.setRetiredEmpid(employee.getEmpid());

        if (employee.getTxtDos() == null || employee.getTxtDos().equals("")) {
            restrictUser = "Y";
        } else {
            Date d = new Date(employee.getTxtDos());
            Calendar cal = Calendar.getInstance();
            int curMonth = cal.get(Calendar.MONTH);
            int curYear = cal.get(Calendar.YEAR);
            cal.setTime(d);
            int retiredMonth = cal.get(Calendar.MONTH);
            int retiredYear = cal.get(Calendar.YEAR);

            if ((retiredYear > curYear) || (retiredYear == curYear && retiredMonth > curMonth)) {
                restrictUser = "Y";
            }

        }

        mv.addObject("departmentlist", departmentDAO.getDepartmentList());
        mv.addObject("authOfficeList", offDAO.getTotalOfficeList(retirement.getAuthDeptCode()));
        mv.addObject("authSPCList", substantivePostDAO.getOfficeWithSPCList(retirement.getAuthOfficeCode()));
        mv.addObject("retFromOfficeList", offDAO.getTotalOfficeList(retirement.getRetiredfromdeptCode()));
        mv.addObject("retFromSPCList", substantivePostDAO.getOfficeWithSPCList(retirement.getRetiredfromofficeCode()));
        mv.addObject("Retirement", retirement);
        mv.addObject("employee", employee);
        mv.addObject("restrictUser", restrictUser);
        mv.setViewName("/servicecloser/Retirement");
        return mv;
    }

    @RequestMapping(value = "SaveEmployeeRetirment")
    public ModelAndView SaveEmployeeRetirment(@ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("Retirement") Retirement retirement) {
        ModelAndView mv = new ModelAndView();

        retirement.setEntDeptCode(lub.getLogindeptcode());
        retirement.setEntOfficeCode(lub.getLoginoffcode());
        retirement.setEntAuthCode(lub.getLoginspc());
        retirement.setToe("AN");
        if (retirement.getRetid() != null && !retirement.getRetid().equals("")) {
            serviceCloserDAO.updateEmployeeRetirement(retirement);
        } else {
            serviceCloserDAO.saveEmployeeRetirement(retirement);
        }
        Employee employee = employeeDAO.getEmployeeProfile(retirement.getRetiredEmpid());
        Retirement retirement1 = serviceCloserDAO.getEmployeeRetirementData(selectedEmpObj.getEmpId());
        retirement1.setDuedate(employee.getDor());
        mv.addObject("departmentlist", departmentDAO.getDepartmentList());
        mv.addObject("retFromOfficeList", offDAO.getTotalOfficeList(retirement1.getRetiredfromdeptCode()));
        mv.addObject("retFromSPCList", substantivePostDAO.getOfficeWithSPCList(retirement1.getRetiredfromofficeCode()));
        mv.addObject("Retirement", retirement1);
        mv.addObject("employee", employee);
        mv.setViewName("redirect:/ShowRetirmentForm.htm");
        return mv;
    }

    @RequestMapping(value = "SaveEmployeeRetirment", params = "action=Delete")
    public ModelAndView DeleteEmployeeRetirment(@ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("Retirement") Retirement retirement) {

        ModelAndView mv = new ModelAndView();

        if (retirement.getRetid() != null && !retirement.getRetid().equals("")) {
            serviceCloserDAO.deleteEmployeeRetirement(retirement);
        }

        mv.setViewName("redirect:/ShowRetirmentForm.htm");
        return mv;
    }

    @RequestMapping(value = "showEmpSuperannuation")
    public ModelAndView ShowEmpSuperannuation(@ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("Retirement") Retirement retirement) {
        ModelAndView mv = new ModelAndView();
        String restrictUser = "N";
        Employee employee = employeeDAO.getEmployeeProfile(selectedEmpObj.getEmpId());
        retirement = serviceCloserDAO.getEmployeeSuperannuationData(selectedEmpObj.getEmpId());
        retirement.setDuedate(employee.getTxtDos());
        retirement.setRetiredEmpid(employee.getEmpid());

        if (employee.getTxtDos() == null || employee.getTxtDos().equals("")) {
            restrictUser = "Y";
        } else {
            Date d = new Date(employee.getTxtDos());
            Calendar cal = Calendar.getInstance();
            int curMonth = cal.get(Calendar.MONTH);
            int curYear = cal.get(Calendar.YEAR);
            cal.setTime(d);
            int retiredMonth = cal.get(Calendar.MONTH);
            int retiredYear = cal.get(Calendar.YEAR);

            if ((retiredYear > curYear) || (retiredYear == curYear && retiredMonth > curMonth)) {
                restrictUser = "Y";
            }

        }

        mv.addObject("departmentlist", departmentDAO.getDepartmentList());
        mv.addObject("authOfficeList", offDAO.getTotalOfficeList(retirement.getAuthDeptCode()));
        mv.addObject("authSPCList", substantivePostDAO.getOfficeWithSPCList(retirement.getAuthOfficeCode()));

        mv.addObject("Retirement", retirement);
        mv.addObject("employee", employee);
        mv.addObject("restrictUser", restrictUser);
        mv.setViewName("/servicecloser/Superannuation");
        return mv;
    }

    @RequestMapping(value = "SaveEmpSuperannuation")
    public ModelAndView SaveSuperannuationData(@ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("Retirement") Retirement retirement) {
        ModelAndView mv = new ModelAndView();
        /*retirement.setEntDeptCode(lub.getLogindeptcode());
         retirement.setEntOfficeCode(lub.getLoginoffcode());
         retirement.setEntAuthCode(lub.getLoginspc());
         retirement.setToe("AN");*/
        Employee employee = employeeDAO.getEmployeeProfile(retirement.getRetiredEmpid());
        
        retirement.setRetiredfromdeptCode(employee.getDeptCode());
        retirement.setRetiredfromofficeCode(employee.getOfficecode());
        retirement.setRetiredfromspc(employee.getSpc());

        serviceCloserDAO.saveSuperannuation(retirement);

        Retirement retirement1 = serviceCloserDAO.getEmployeeSuperannuationData(selectedEmpObj.getEmpId());
        retirement1.setDuedate(employee.getDor());
        mv.addObject("departmentlist", departmentDAO.getDepartmentList());
        mv.addObject("authOfficeList", offDAO.getTotalOfficeList(retirement.getAuthDeptCode()));
        mv.addObject("authSPCList", substantivePostDAO.getOfficeWithSPCList(retirement.getAuthOfficeCode()));
        mv.addObject("Retirement", retirement1);
        mv.addObject("employee", employee);
        mv.setViewName("redirect:/showEmpSuperannuation.htm");
        return mv;
    }
}
