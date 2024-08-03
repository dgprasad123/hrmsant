/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.misreport;

import hrms.common.CommonFunctions;
import hrms.dao.deputation.DeputationDAO;
import hrms.dao.employee.EmployeeDAO;
import hrms.dao.misreport.CadreEmployeeReportDAO;
import hrms.model.employee.Education;
import hrms.model.employee.Employee;
import hrms.model.employee.EmployeeERCommentForm;
import hrms.model.employee.Training;
import hrms.model.employee.TransferJoining;
import hrms.model.login.LoginUserBean;
import hrms.model.master.Module;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Manas Jena
 */
@Controller
@SessionAttributes({"LoginUserBean", "Privileges", "SelectedEmpObj", "SelectedEmpOffice"})
public class CadreEmployeeReport {

    @Autowired
    CadreEmployeeReportDAO cadreEmployeeReportDAO;

    @Autowired
    EmployeeDAO employeeDAO;

    @Autowired
    public DeputationDAO deputationDAO;

    @RequestMapping(value = "cadreEmployeeSearch")
    public ModelAndView cadreEmployeeSearch(@ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam Map<String, String> requestParams) {
        ModelAndView mav = new ModelAndView();
        String allYear = requestParams.get("allYear");        
        String sortby = requestParams.get("sortby");    
        String cadrecode = requestParams.get("cadrecode");        
        mav.addObject("allYear", requestParams.get("allYear"));
        String path = "misreport/CadreEmployeeSearch";
        if (cadrecode != null && !cadrecode.equals("")) {
            ArrayList employees = cadreEmployeeReportDAO.getEmployeeList(cadrecode, allYear, sortby);
            mav.addObject("employees", employees);
        }
        mav.setViewName(path);
        return mav;
    }

    @RequestMapping(value = "servicesdashboard", method = {RequestMethod.GET})
    public ModelAndView servicesDashboard() {
        ModelAndView mav = new ModelAndView();
        String path = "/tab/ServiceConditionAdmin";
        mav.setViewName(path);
        return mav;
    }

    //  @ResponseBody
    @RequestMapping(value = "getCadreEmpData", method = RequestMethod.GET)
    public String getCadreEmpData(ModelMap model, HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("empid") String empid, @RequestParam("cadrecode") String cadrecode, HttpServletResponse response) throws IOException {
        Employee employee = new Employee();
        employee = cadreEmployeeReportDAO.getEmployeeData(empid, cadrecode);
        model.addAttribute("Employee", employee);
        return "misreport/ViewcadreEmployeeSearch";
    }

    @RequestMapping(value = "viewEREmpSheet", method = RequestMethod.GET)
    public String viewEREmpSheet(ModelMap model, HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("empid") String empid, HttpServletResponse response) throws IOException {
        Employee employeeProfile = new Employee();
        employeeProfile = employeeDAO.getEmployeeProfile(empid);
        Education[] educations = employeeDAO.getEmployeeEducation(empid);
        Training[] training = employeeDAO.getEmployeeTraining(empid, "01-01-1950");
        TransferJoining[] transfer = employeeDAO.getEmployeeTransferAndJoining(empid, "01-01-1950");
        List depuList = deputationDAO.getDeputationList(empid);
        model.addAttribute("depuList", depuList);
        model.addAttribute("transfer", transfer);
        model.addAttribute("trainings", training);
        model.addAttribute("EmployeeProfile", employeeProfile);
        model.addAttribute("educations", educations);
        model.addAttribute("EmpId", empid);
        return "misreport/viewEREmpSheet";
    }

    @RequestMapping(value = "postIncumbencyChart", method = RequestMethod.GET)
    public String getholidayList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("Privileges") Module[] privileges) {
        String path = "/under_const";
        if (CommonFunctions.hasPrivileage(privileges, "postIncumbencyChart.htm")) {
            path = "/misreport/PostIncumbencyChart";
        }
        
        return path;
    }

    @ResponseBody
    @RequestMapping(value = "getIncumbancyChart", method = RequestMethod.GET)
    public void getIncumbancyChart(HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("spc") String spc, HttpServletResponse response) {
        response.setContentType("application/json");
        PrintWriter out = null;
        try {
            ArrayList incumbancyReport = cadreEmployeeReportDAO.getIncumbancyChart(spc);
            JSONArray jobj = new JSONArray(incumbancyReport);
            out = response.getWriter();
            out.print(jobj);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    @ResponseBody
    @RequestMapping(value = "getEmployeeIncumbancyChart", method = RequestMethod.GET)
    public void getEmployeeIncumbancyChart(HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("empid") String empid, HttpServletResponse response) {
        response.setContentType("application/json");
        PrintWriter out = null;
        try {
            ArrayList incumbancyReport = cadreEmployeeReportDAO.getEmployeeIncumbancyChart(empid);
            JSONArray jobj = new JSONArray(incumbancyReport);
            out = response.getWriter();
            out.print(jobj);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    @RequestMapping(value = "empwiseEREmpSheet", method = RequestMethod.GET)
    public String empwiseEREmpSheet(ModelMap model, HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletResponse response) throws IOException {
        String empid = lub.getLoginempid();

        Employee employeeProfile = new Employee();
        employeeProfile = employeeDAO.getEmployeeProfile(empid);
        Education[] educations = employeeDAO.getEmployeeEducation(empid);
        Training[] training = employeeDAO.getEmployeeTraining(empid, "01-01-1950");
        TransferJoining[] transfer = employeeDAO.getEmployeeTransferAndJoining(empid, "01-01-1950");
        model.addAttribute("transfer", transfer);
        model.addAttribute("trainings", training);
        model.addAttribute("EmployeeProfile", employeeProfile);
        model.addAttribute("educations", educations);
        EmployeeERCommentForm ersheet = new EmployeeERCommentForm();
        ersheet = cadreEmployeeReportDAO.editcommentErsheet(empid);
        model.addAttribute("Ersheet", ersheet);
        List depuList = deputationDAO.getDeputationList(empid);
        model.addAttribute("depuList", depuList);
        return "misreport/empwiseEREmpSheet";
    }

    @RequestMapping(value = "commentERSheet")
    public String commentERSheet(ModelMap model, HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("commentERSheet") EmployeeERCommentForm ersheet, HttpServletResponse response) throws IOException {
        String empid = lub.getLoginempid();
        String gpfNo = lub.getLogingpfno();
        cadreEmployeeReportDAO.SavecommentERSheet(ersheet, empid, gpfNo);
        //  return "misreport/empwiseEREmpSheet";
        return "redirect:/empwiseEREmpSheet.htm";
    }
}
