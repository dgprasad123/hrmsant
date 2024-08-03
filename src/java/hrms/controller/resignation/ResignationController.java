/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.resignation;

import hrms.dao.employee.EmployeeDAO; 
import hrms.dao.master.DepartmentDAO;
import hrms.dao.master.OfficeDAO;
import hrms.dao.master.SubStantivePostDAO;
import hrms.dao.notification.NotificationDAO;
import hrms.dao.resignation.ResignationDAO;
import hrms.model.employee.Employee;
import hrms.model.login.LoginUserBean;
import hrms.model.login.Users;
import hrms.model.notification.NotificationBean;
import hrms.model.resignation.Resignation;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author lenovo
 */
@Controller
@SessionAttributes({"Users", "SelectedEmpObj", "LoginUserBean"})
public class ResignationController {

    @Autowired
    public ResignationDAO resignationDAO;
    @Autowired
    EmployeeDAO employeeDAO;
    @Autowired
    DepartmentDAO departmentDAO;
    @Autowired
    OfficeDAO offDAO;
    @Autowired
    SubStantivePostDAO substantivePostDAO;
    @Autowired
    public NotificationDAO notificationDao;

    @RequestMapping(value = "Resignation")
    public ModelAndView ShowResignationForm(@ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("LoginUserBean") LoginUserBean lub) throws Exception {

        ModelAndView mv = new ModelAndView();
        Employee employee = employeeDAO.getEmployeeProfile(selectedEmpObj.getEmpId());
        Resignation resignation = resignationDAO.getEmployeeResignationData(selectedEmpObj.getEmpId());
        //resignation.setDuedate(employee.getDor());
        resignation.setRetiredEmpid(employee.getEmpid());

        mv.addObject("departmentlist", departmentDAO.getDepartmentList());
        mv.addObject("retFromOfficeList", offDAO.getTotalOfficeList(resignation.getRetiredfromdeptCode()));
        mv.addObject("retFromSPCList", substantivePostDAO.getOfficeWithSPCList(resignation.getRetiredfromofficeCode()));
        mv.addObject("Resignation", resignation);
        mv.addObject("employee", employee);
        mv.addObject("resignationlist", resignationDAO.getResginationList(selectedEmpObj.getEmpId()));
        mv.setViewName("/resignation/ResignationList");
        return mv;
    }

    @RequestMapping(value = "SaveEmployeeResignation", params = "action=Save")
    public String SaveEmployeeRetirment(HttpServletResponse response, @ModelAttribute("SelectedEmpObj") Users selectedEmpObj,
            @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("Resignation") Resignation resignation) throws IOException {
        NotificationBean nb = new NotificationBean();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        String dateofentry = sdf.format(new Date());
        try {
            resignation.setHidempid(selectedEmpObj.getEmpId());
            resignation.setRetiredEmpid(selectedEmpObj.getEmpId());
            nb.setNottype("RESIGNATION");
            nb.setDateofEntry(sdf.parse(dateofentry));

            nb.setOrdno(resignation.getOrdno());
            nb.setNote(resignation.getNote());
            nb.setOrdDate(sdf.parse(resignation.getOrdDate()));
            nb.setEmpId(selectedEmpObj.getEmpId());
            nb.setSancDeptCode(resignation.getAuthDeptCode());
            nb.setSancOffCode(resignation.getAuthOfficeCode());
            nb.setSancAuthCode(resignation.getAuthSpc());

            System.out.println("hiddennotid:+" + resignation.getHidnotid());
            if (resignation.getHidnotid() > 0) {
                nb.setNotid(resignation.getHidnotid());
                notificationDao.modifyNotificationData(nb);
                resignationDAO.updateEmployeeResignation(resignation);
            } else {
                System.out.println("insert hiddenid:" + resignation.getHidnotid());

                resignationDAO.saveEmployeeResignation(resignation);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/Resignation.htm";
    }

    @RequestMapping(value = "editResignation")
    public ModelAndView EditResignation(@ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("LoginUserBean") LoginUserBean lub,
            @ModelAttribute("Resignation") Resignation resignation, @RequestParam Map<String, String> requestParams) {
        ModelAndView mav = null;
        try {
            Employee employee = employeeDAO.getEmployeeProfile(selectedEmpObj.getEmpId());
            String notId = requestParams.get("notId");
            System.out.println("notId:+" + notId);
            resignation = resignationDAO.editResignationData(notId);
            mav = new ModelAndView("/resignation/Resignation", "Resignation", resignation);
            mav.addObject("employee", employee);
            mav.addObject("departmentlist", departmentDAO.getDepartmentList());

            mav.addObject("retFromOfficeList", offDAO.getTotalOfficeList(resignation.getRetiredfromdeptCode()));
            mav.addObject("retFromSPCList", substantivePostDAO.getOfficeWithSPCList(resignation.getRetiredfromofficeCode()));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;

    }

    @RequestMapping(value = "NewResignation")
    public ModelAndView NewResignation(@ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("Resignation") Resignation resignation) {
        ModelAndView mv = new ModelAndView();
        Employee employee = employeeDAO.getEmployeeProfile(selectedEmpObj.getEmpId());
        resignation = resignationDAO.getEmployeeResignationDataForNewEntry(selectedEmpObj.getEmpId());
        mv.setViewName("/resignation/Resignation");
        mv.addObject("employee", employee);
        mv.addObject("Resignation", resignation);
        mv.addObject("departmentlist", departmentDAO.getDepartmentList());
        mv.addObject("retFromOfficeList", offDAO.getTotalOfficeList(resignation.getRetiredfromdeptCode()));
        mv.addObject("retFromSPCList", substantivePostDAO.getOfficeWithSPCList(resignation.getRetiredfromofficeCode()));
        System.out.println("newnoteid:" + resignation.getHidnotid());
        return mv;
    }

    @RequestMapping(value = "SaveEmployeeResignation", params = "action=Delete")
    public String DeleteResignation(@ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("Resignation") Resignation resignation) {

        try {
            resignation.setRetiredEmpid(selectedEmpObj.getEmpId());
            resignationDAO.deleteResignation(resignation);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/Resignation.htm";
    }
}
