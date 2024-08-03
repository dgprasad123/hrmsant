package hrms.controller.reappointment;

import hrms.dao.employee.EmployeeDAO;
import hrms.dao.master.DepartmentDAO;
import hrms.dao.master.OfficeDAO;
import hrms.dao.master.SubStantivePostDAO;
import hrms.dao.notification.NotificationDAO;
import hrms.dao.reappointment.ReAppointmentDAO;
import hrms.model.employee.Employee;
import hrms.model.login.LoginUserBean;
import hrms.model.login.Users;
import hrms.model.reappointment.ReAppointmentForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

@Controller
@SessionAttributes({"Users", "SelectedEmpObj", "LoginUserBean"})
public class ReAppointmentController {

    @Autowired
    ReAppointmentDAO reAppointmentDAO;

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

    @RequestMapping(value = "ReAppointmentList")
    public String ReAppointmentList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpObj") Users selectedEmpObj) {

        try {
            ModelAndView mv = new ModelAndView();
            ReAppointmentForm reapp = reAppointmentDAO.getEmployeeReAppointmentData(selectedEmpObj.getEmpId());
            //resignation.setDuedate(employee.getDor());
            reapp.setReappointedEmpid(selectedEmpObj.getEmpId());

            mv.addObject("departmentlist", departmentDAO.getDepartmentList());
            mv.addObject("retFromOfficeList", offDAO.getTotalOfficeList(reapp.getRetiredfromdeptCode()));
            mv.addObject("retFromSPCList", substantivePostDAO.getOfficeWithSPCList(reapp.getRetiredfromofficeCode()));
            mv.addObject("reappointmentlist", reAppointmentDAO.getReAppointmentList(selectedEmpObj.getEmpId()));
            mv.setViewName("/reappointment/ReAppointmentList");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/reappointment/ReAppointmentList";
    }

    @RequestMapping(value = "ReAppointment")
    public ModelAndView ReAppointment(Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("reAppointmentForm") ReAppointmentForm reapp) {

        ModelAndView mav = null;
        try {
            reapp = reAppointmentDAO.getEmployeeReAppointmentData(selectedEmpObj.getEmpId());

            Employee employee = employeeDAO.getEmployeeProfile(selectedEmpObj.getEmpId());

            mav = new ModelAndView("/reappointment/ReAppointmentNew", "reAppointmentForm", reapp);

            mav.addObject("departmentlist", departmentDAO.getDepartmentList());
            mav.addObject("officelist", offDAO.getTotalOfficeList(reapp.getAuthDeptCode()));
            mav.addObject("postlist", substantivePostDAO.getOfficeWithSPCList(reapp.getAuthOfficeCode()));
            mav.addObject("employee", employee);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "SaveEmployeeReAppointment")
    public String SaveEmployeeReAppointment(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("reAppointmentForm") ReAppointmentForm reapp) {

        try {
            reapp.setReappointedEmpid(selectedEmpObj.getEmpId());
            reAppointmentDAO.saveEmployeeReAppointment(reapp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/ReAppointment.htm";
    }

}
