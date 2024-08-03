/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.servicebook;

import hrms.dao.employee.EmployeeDAO;
import hrms.dao.servicebook.ServiceBookDAO;
import hrms.model.login.LoginUserBean;
import hrms.model.servicebook.EmpServiceBook;
import hrms.model.servicebook.EmpServiceHistory;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ModelAndView;

@Controller
@SessionAttributes("LoginUserBean")
public class ServiceBookValidatorController {

    @Autowired
    ServiceBookDAO serviceBookDao;

    @Autowired
    EmployeeDAO employeeDAO;

    @RequestMapping(value = "ServiceBookValidatorPage.htm")
    public ModelAndView ServiceBookValidatorPage(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("empService") EmpServiceHistory empService) {

        ModelAndView mav = new ModelAndView();

        try {
            List emplist = serviceBookDao.getEmployeeList(lub.getLoginempid(), lub.getLoginspc());

            mav.addObject("emplist", emplist);
            mav.setViewName("/servicebook/ServiceBookValidator");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "ServiceBookValidator.htm")
    public ModelAndView ServiceBookValidator(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("empService") EmpServiceHistory empService) {

        ModelAndView mav = new ModelAndView();

        try {
            EmpServiceBook esb = serviceBookDao.getSHReportValidator(empService.getEmpId());

            mav = new ModelAndView("/servicebook/ServiceBookValidator", "empService", empService);
            mav.addObject("esb", esb);

            mav.addObject("empid", empService.getEmpId());

            List emplist = serviceBookDao.getEmployeeList(lub.getLoginempid(), lub.getLoginspc());
            mav.addObject("emplist", emplist);

            //mav.setViewName("/servicebook/ServiceBookValidator");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @ResponseBody
    @RequestMapping(value = "ValidateServiceBookEntry")
    public String ValidateServiceBookEntry(@ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("empId") String empId, @RequestParam("id") String id) {
        JSONObject json = new JSONObject();
        try {
            int status = serviceBookDao.validateServiceBookEntry(empId, id, lub);
            json.put("status", status);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json.toString();
    }

    @ResponseBody
    @RequestMapping(value = "ValidateNoNotificationServiceBookEntry")
    public String ValidateNoNotificationServiceBookEntry(@ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("empId") String empId, @RequestParam("id") String id, @RequestParam("tabname") String tabname) {
        JSONObject json = new JSONObject();
        try {
            int status = serviceBookDao.validateNoNotificationServiceBookEntry(empId, id, tabname, lub);
            json.put("status", status);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json.toString();
    }

    @ResponseBody
    @RequestMapping(value = "InValidateServiceBookEntry")
    public String InValidateServiceBookEntry(@ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("empId") String empId, @RequestParam("id") String id) {
        JSONObject json = new JSONObject();
        try {
            int status = serviceBookDao.inValidateServiceBookEntry(empId, id);
            json.put("status", status);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json.toString();
    }

    @ResponseBody
    @RequestMapping(value = "InValidateNoNotificationServiceBookEntry")
    public String InValidateNoNotificationServiceBookEntry(@ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("empId") String empId, @RequestParam("id") String id, @RequestParam("tabname") String tabname) {
        JSONObject json = new JSONObject();
        try {
            int status = serviceBookDao.inValidateNoNotificationServiceBookEntry(empId, id, tabname);
            json.put("status", status);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json.toString();
    }

    @RequestMapping(value = "ServiceBookValidatorEmployeeWise.htm")
    public ModelAndView ServiceBookValidatorEmployeeWise(@ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("empid") String empid) {

        ModelAndView mav = new ModelAndView();

        try {
            EmpServiceBook esb = serviceBookDao.getSHReportValidator(empid);

            mav.addObject("esb", esb);

            mav.addObject("empid", empid);
            mav.addObject("empprofile", employeeDAO.getEmployeeProfile(empid));

            mav.setViewName("/servicebook/ServiceBookValidatorEmployeeWise");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "ServiceBookInValidator.htm")
    public ModelAndView ServiceBookInValidator(@ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("empid") String empid) {

        ModelAndView mav = new ModelAndView();

        try {
            EmpServiceBook esb = serviceBookDao.getSHReportValidator(empid);

            mav.addObject("esb", esb);

            mav.addObject("empid", empid);
            mav.addObject("empprofile", employeeDAO.getEmployeeProfile(empid));
            mav.addObject("usrTyp", lub.getLoginusertype());

            mav.setViewName("/servicebook/ServiceBookInValidator");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "ServiceBookInValidatorAllGroupWise.htm")
    public ModelAndView ServiceBookInValidatorAllGroupWise(@ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("empid") String empid) {

        ModelAndView mav = new ModelAndView();

        try {
            //EmpServiceBook esb = serviceBookDao.getSHReportValidator(empid);

            // mav.addObject("esb", esb);
            mav.addObject("empid", empid);
            mav.addObject("empprofile", employeeDAO.getEmployeeProfile(empid));
            //mav.setViewName("/servicebook/ServiceBookInValidator");
            

            mav.setViewName("/servicebook/ServiceBookInValidatorAllGroupWise");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @ResponseBody
    @RequestMapping(value = "inValidateSBGroupWise")
    public void inValidateSBGroupWise(ModelMap model, HttpServletRequest request, HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub,
            @RequestParam("empId") String empId, @RequestParam("checkedSBModule") String checkedSBModule) {
        PrintWriter out = null;
        try {
            //System.out.println("invalidate:" + checkedSBModule + empId );
            response.setContentType("application/json");
            String status = serviceBookDao.inValidateSBEntryGroupWise(checkedSBModule, empId);
            out = response.getWriter();
            JSONObject obj = new JSONObject();
            obj.append("status", status);
            out.write(obj.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    @ResponseBody
    @RequestMapping(value = "unlockServiceBookEntryType",method = {RequestMethod.POST, RequestMethod.GET})
    public void unlockServiceBookEntryType(ModelMap model,@ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("empId") String empId, @RequestParam("id") String id) {

        JSONObject json = new JSONObject();

        try {
            int retVal = serviceBookDao.removeServiceBookEntryType(empId, id);           

        } catch (Exception e) {
            e.printStackTrace();
        }
       
    }
}
