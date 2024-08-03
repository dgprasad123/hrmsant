/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.Pension;

import hrms.dao.PensionEmployee.PensionEmployeeDAO;
import hrms.dao.employee.EmployeeDAO;
import hrms.model.employee.PensionNomineeList;
import hrms.model.employee.PensionProfile;
import hrms.model.login.LoginUserBean;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Manas
 */
@SessionAttributes("LoginUserBean")
@Controller
public class PensionEmployeeController {

    @Autowired
    PensionEmployeeDAO pensionEmployeeDAO;
    @Autowired
    public EmployeeDAO employeeDAO;

    @RequestMapping(value = "PensionEmpDetailsAck.htm", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView PensionEmpDetailsAck(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("employee") PensionProfile employee) {
        ModelAndView mv = new ModelAndView("/pension/PensionEmpDetailsAck");
        //PensionProfile employee = null;
        List pensionGuardianDetails = null;
        List nomineeList = null;
        try {
            String empid = lub.getLoginempid();
            PensionProfile employeenew = employeeDAO.getPensionEmpDetails(empid);
            if (employeenew.getMsg() != null && employeenew.getMsg().equals("Y")) {
                pensionEmployeeDAO.insertEmployeePensionDetails(employeenew, empid);
//                pensionEmployeeDAO.updateEmployeePensionDetails(employeenew, empid);
                employee = pensionEmployeeDAO.getPensionEmpProfileDetailsById(empid, employeenew.getGpftype());
                mv = new ModelAndView("/pension/PensionEmpDetailsAck", "employee", employee);
                pensionGuardianDetails = pensionEmployeeDAO.getPensionEmpGuardianDetails(empid);
                nomineeList = pensionEmployeeDAO.getPensionEmpNomineeDetails(empid);
                mv.addObject("message", "");
            } else {
                mv.addObject("message", "Account Holder Not Eligible For Pension");
            }
            //pensionEmployeeDAO.insertEmployeePensionDetails(employeenew, empid);

            //List nomineeList = pensionEmployeeDAO.getPensionEmpNomineeDetails(empid);
            // System.out.println("nomineeList==="+nomineeList.size());
            mv.addObject("pensionGuardianDetails", pensionGuardianDetails);
            mv.addObject("nomineeList", nomineeList);
            //mv.addObject("employee", employee);
            mv.addObject("loginid", empid);
            //mv=new ModelAndView("/pension/PensionEmpDetailsAck","employee",employee); 

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mv;
    }
    
    
    
    
      @RequestMapping(value = "SubmitPensionAcknowledgement", params={"btnSave=Refresh"})
    public ModelAndView PensionEmpDetailsAckfresh(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("employee") PensionProfile employee) {
        ModelAndView mv = new ModelAndView("/pension/PensionEmpDetailsAck");
        //PensionProfile employee = null;
        List pensionGuardianDetails = null;
        List nomineeList = null;
        try {
            String empid = lub.getLoginempid();
            PensionProfile employeenew = employeeDAO.getPensionEmpDetails(empid);
            if (employeenew.getMsg() != null && employeenew.getMsg().equals("Y")) {
//                System.out.println("employeenew.getMsg() is: "+employeenew.getMsg());
                pensionEmployeeDAO.updateEmployeePensionDetails(employeenew, empid);
                employee = pensionEmployeeDAO.getPensionEmpProfileDetailsById(empid, employeenew.getGpftype());
                mv = new ModelAndView("/pension/PensionEmpDetailsAck", "employee", employee);
                pensionGuardianDetails = pensionEmployeeDAO.getPensionEmpGuardianDetails(empid);
                nomineeList = pensionEmployeeDAO.getPensionEmpNomineeDetails(empid);
                mv.addObject("message", "");
            } else {
                mv.addObject("message", "Account Holder Not Eligible For Pension");
            }
            mv.addObject("pensionGuardianDetails", pensionGuardianDetails);
            mv.addObject("nomineeList", nomineeList);
            //mv.addObject("employee", employee);
            mv.addObject("loginid", empid);
            //mv=new ModelAndView("/pension/PensionEmpDetailsAck","employee",employee); 

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mv;
    }
    

    @RequestMapping(value = "SubmitPensionAcknowledgement",params={"btnSave=Submit"})
    public String SubmitPensionAcknowledgement(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("employee") PensionProfile employee) {
        try {
            String empid = lub.getLoginempid();
            pensionEmployeeDAO.updatePensionCompletedStatusData(employee, empid);
//            employeeDAO.profileCompletedLog(lub.getLoginempid(), lub.getLoginempid(), null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/restService/PensionEmpDetailssubmit.htm";
    }
    
//    @RequestMapping(value = "SubmitPensionAcknowledgement", params={"btnSave=Refresh"})
//    public String RefreshPensionAcknowledgement(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("employee") PensionProfile employee) {
//        try {
//            String empid = lub.getLoginempid();
//            pensionEmployeeDAO.updateEmployeePensionDetails(employee, empid);
////            employeeDAO.profileCompletedLog(lub.getLoginempid(), lub.getLoginempid(), null);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return "/pension/PensionEmpDetailsAck";
//    }

//    @ResponseBody
//    @RequestMapping("/updatepension")
//    public void updatePensionType(ModelMap model, HttpServletRequest request, HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub,
//            @RequestParam("serialNo") int serialNo, @RequestParam("empId") int empId, @RequestBody PensionNomineeList pensionNomineeList) {
//        PrintWriter out = null;
//        try {
//            String empid = lub.getLoginempid();
//            pensionEmployeeDAO.updatePensionType(pensionNomineeList, empid, serialNo);
//            String status = "success";
//            response.setContentType("application/json");
//            out = response.getWriter();
//            JSONObject obj = new JSONObject();
//            obj.put("status", status);
//            out.write(obj.toString());
//        } catch (IOException e) {
//            e.printStackTrace();
//            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//        } catch (Exception e) {
//            e.printStackTrace();
//            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//        } finally {
//            if (out != null) {
//                out.close();
//            }
//        }
//    }
    @ResponseBody
    @RequestMapping("/updatepension")
    public void updatePensionType(ModelMap model, HttpServletRequest request, HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub,
            @RequestParam("serialNo") int serialNo, @RequestParam("empId") int empId, @RequestBody PensionNomineeList pensionNomineeList) {
        response.setContentType("application/json");
        PrintWriter out = null;
        JSONArray json = null;
        try {
            String empid = lub.getLoginempid();
            pensionEmployeeDAO.updatePensionType(pensionNomineeList, empid, serialNo);
            json = new JSONArray();
            out = response.getWriter();
            out.write(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    @RequestMapping(value = "PensionEmpDetailssubmit.htm", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView PensionEmpDetailssubmit(@ModelAttribute("LoginUserBean") LoginUserBean lub) {
        ModelAndView mv = new ModelAndView("/pension/PensionEmpDetailsAck");
        try {
            String empid = lub.getLoginempid();
            List nomineeList = pensionEmployeeDAO.getPensionEmpNomineeDetails(empid);
            // System.out.println("nomineeList==="+nomineeList.size());
            PensionProfile employee = pensionEmployeeDAO.getPensionEmpProfileDetailsById(empid, null);
            List pensionGuardianDetails = pensionEmployeeDAO.getPensionEmpGuardianDetails(empid);
            mv.addObject("pensionGuardianDetails", pensionGuardianDetails);
            mv.addObject("nomineeList", nomineeList);
            mv.addObject("employee", employee);
            mv.addObject("loginid", empid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mv;
    }

}
