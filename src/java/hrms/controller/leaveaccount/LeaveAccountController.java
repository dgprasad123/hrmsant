/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.leaveaccount;

import hrms.dao.leave.LeaveDAO;
import hrms.dao.master.LeaveTypeDAO;
import hrms.model.leave.CreditLeaveBean;
import hrms.model.leave.CreditLeaveProperties;
import hrms.model.leave.EmpLeaveAccountPropeties;
import hrms.model.leave.EmployeeLeaveAccount;
import hrms.model.login.LoginUserBean;
import hrms.model.login.Users;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
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
@Controller
@SessionAttributes({"LoginUserBean", "SelectedEmpObj", "SelectedEmpOffice"})
public class LeaveAccountController {

    @Autowired
    LeaveTypeDAO leaveTypeDAO;
    @Autowired
    LeaveDAO leaveDao;

    @RequestMapping(value = "myleaveAccountInputPage")
    public ModelAndView myLeaveAccountInputPage(@ModelAttribute("employeeLeaveAccount") EmployeeLeaveAccount employeeLeaveAccount, @ModelAttribute("LoginUserBean") LoginUserBean lub) {
        ModelAndView mv = new ModelAndView();
        employeeLeaveAccount.setEmpid(lub.getLoginempid());
        mv.setViewName("/leaveAccount/MyleaveAccountInputPage");
        mv.addObject("leaveType", leaveTypeDAO.getLeaveTypeList());
        return mv;
    }

    @RequestMapping(value = "myLeaveAccount")
    public ModelAndView myLeaveAccount(@ModelAttribute("employeeLeaveAccount") EmployeeLeaveAccount employeeLeaveAccount, @ModelAttribute("LoginUserBean") LoginUserBean lub) {
        ModelAndView mv = new ModelAndView();
        EmpLeaveAccountPropeties eap = leaveDao.getLeaveAccountDetails(employeeLeaveAccount.getEmpid(), employeeLeaveAccount.getLeavePeriodFrom(), employeeLeaveAccount.getLeavePeriodTo(), employeeLeaveAccount.getLeaveType(), "EOL");
        mv.addObject("eap", eap);
        mv.setViewName("/leaveAccount/LeaveAccountOutput");
        return mv;
    }

    @RequestMapping(value = "employeeLeaveAccountInputPage")
    public ModelAndView employeeLeaveAccountInputPage(@ModelAttribute("employeeLeaveAccount") EmployeeLeaveAccount employeeLeaveAccount, @ModelAttribute("SelectedEmpObj") Users selectedEmpObj) {
        ModelAndView mv = new ModelAndView();
        employeeLeaveAccount.setEmpid(selectedEmpObj.getEmpId());
        mv.setViewName("/leaveAccount/LeaveAccountInput");
        mv.addObject("leaveType", leaveTypeDAO.getLeaveTypeList());
        return mv;
    }

    @RequestMapping(value = "employeeLeaveAccount")
    public ModelAndView employeeLeaveAccount(@ModelAttribute("employeeLeaveAccount") EmployeeLeaveAccount employeeLeaveAccount, @ModelAttribute("SelectedEmpObj") Users selectedEmpObj) {
        ModelAndView mv = new ModelAndView();
        EmpLeaveAccountPropeties eap = leaveDao.getLeaveAccountDetails(employeeLeaveAccount.getEmpid(), employeeLeaveAccount.getLeavePeriodFrom(), employeeLeaveAccount.getLeavePeriodTo(), employeeLeaveAccount.getLeaveType(), "EOL");
        mv.addObject("eap", eap);
        if (employeeLeaveAccount.getLeaveType().equals("EL")) {
            mv.setViewName("/leaveAccount/LeaveAccountOutput");
        } else {
            mv.setViewName("/leaveAccount/HPLLeaveAccountOutput");
        }
        return mv;
    }

    @RequestMapping(value = "employeeLeaveCredit.htm", method = RequestMethod.GET)
    public ModelAndView employeeLeaveCredit(@ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("creditLeaveBean") CreditLeaveBean creditLeaveBean) {
        ModelAndView mv = new ModelAndView();
        mv.addObject("leaveType", leaveTypeDAO.getLeaveTypeList());
        mv.setViewName("/leaveAccount/PeriodicLeaveCredit");
        return mv;
    }

    @RequestMapping(value = "employeeLeaveCredit.htm", method = RequestMethod.POST)
    public ModelAndView employeeLeaveCreditReport(@ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("creditLeaveBean") CreditLeaveBean creditLeaveBean) {
        ModelAndView mv = new ModelAndView();
        CreditLeaveProperties clv = leaveDao.getLeaveOBDate(selectedEmpObj.getEmpId(), creditLeaveBean.getTolid());
        ArrayList alcleave = leaveDao.getUpdateData(selectedEmpObj.getEmpId(), creditLeaveBean.getTolid());
        mv.addObject("clv", clv);
        mv.addObject("alcleave", alcleave);
        mv.addObject("leaveType", leaveTypeDAO.getLeaveTypeList());
        mv.setViewName("/leaveAccount/PeriodicLeaveCredit");
        return mv;
    }

    @ResponseBody
    @RequestMapping(value = "updateLeaveCreditData", method = RequestMethod.POST)
    public void updateLeaveCreditData(@ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("clv") CreditLeaveProperties clv, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = null;
        try {
            clv.setEmpId(selectedEmpObj.getEmpId());
            leaveDao.updateLeaveCreditData(clv);
            Map result = new HashMap();
            result.put("data", "success");
            JSONObject job = new JSONObject(result);
            out = response.getWriter();
            out.write(job.toString());
        } finally {
            out.flush();
            out.close();
        }
    }

    @ResponseBody
    @RequestMapping(value = "editLeaveCreditDetail.htm", method = {RequestMethod.GET, RequestMethod.POST})
    public void editLeaveCreditDetail(@ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("creditLeaveBean") CreditLeaveBean creditLeaveBean, @RequestParam Map<String, String> parameters, HttpServletResponse response) {
        response.setContentType("application/json");
        PrintWriter out = null;
        try {
            String lcrId = parameters.get("lcrId");
            creditLeaveBean = leaveDao.editLeaveData(lcrId);
            JSONObject job = new JSONObject(creditLeaveBean);

            out = response.getWriter();
            out.write(job.toString());

            //mv.setViewName("/leaveAccount/PeriodicLeaveCredit");
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            out.flush();
            out.close();
        }
    }

    @RequestMapping(value = "employeeLeaveCredit.htm", method = {RequestMethod.POST}, params = "Update")
    public ModelAndView updateLeaveCredit(@ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("creditLeaveBean") CreditLeaveBean creditLeaveBean) {
        ModelAndView mv = new ModelAndView();
        leaveDao.updateLeaveData(creditLeaveBean);
        CreditLeaveProperties clv = leaveDao.getLeaveOBDate(selectedEmpObj.getEmpId(), creditLeaveBean.getTolid());
        ArrayList alcleave = leaveDao.getUpdateData(selectedEmpObj.getEmpId(), creditLeaveBean.getTolid());
        mv.addObject("clv", clv);
        mv.addObject("alcleave", alcleave);
        mv.addObject("leaveType", leaveTypeDAO.getLeaveTypeList());
        mv.setViewName("/leaveAccount/PeriodicLeaveCredit");
        return mv;
    }
     @RequestMapping(value = "employeeLeaveCredit.htm", method = {RequestMethod.POST}, params = "Delete")
    public ModelAndView deleteLeaveCredit(@ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("creditLeaveBean") CreditLeaveBean creditLeaveBean) {
        ModelAndView mv = new ModelAndView();
        CreditLeaveProperties clv = leaveDao.getLeaveOBDate(selectedEmpObj.getEmpId(), creditLeaveBean.getTolid());
        leaveDao.deletePeriodicLeaveData(selectedEmpObj.getEmpId(), creditLeaveBean.getTolid());
        mv.addObject("clv", clv);
       // mv.addObject("alcleave", alcleave);
        mv.addObject("leaveType", leaveTypeDAO.getLeaveTypeList());
        mv.setViewName("/leaveAccount/PeriodicLeaveCredit");
        return mv;
    }
}
