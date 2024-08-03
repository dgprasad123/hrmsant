/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.LeaveUpdate;

import hrms.dao.LeaveUpdateBalance.LeaveUpdateDAO;
import hrms.dao.empinfo.EmployeeInformationDAO;
import hrms.dao.leaveapply.LeaveApplyDAO;
import hrms.dao.master.PostDAO;
import hrms.model.empinfo.EmployeeInformation;
import hrms.model.employee.Employee;
import hrms.model.login.LoginUserBean;
import hrms.model.updateleave.UpdateLeave;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

@Controller
@SessionAttributes("LoginUserBean")

public class LeaveUpdateController {

    @Autowired
    public PostDAO postDAO;
    @Autowired
    public LeaveApplyDAO leaveApplyDAO;
    @Autowired
    public LeaveUpdateDAO leaveUpdateDAO;
    @Autowired
    public EmployeeInformationDAO empinfoDAO;
    Calendar cal = Calendar.getInstance();
    int curYear = cal.get(Calendar.YEAR);
    int curmon = cal.get(Calendar.MONTH) + 1;
    SimpleDateFormat simpleformat = new SimpleDateFormat("MM");
    String strMonth = simpleformat.format(new Date());

    @RequestMapping(value = "officewisepostlist")
    public ModelAndView officeWisePostList(@ModelAttribute("clupdateform") UpdateLeave updateLeave, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response, @RequestParam Map<String, String> requestParams) {
        ModelAndView mav = new ModelAndView();
        ArrayList postList = postDAO.getOfficeWisePostList(lub.getLoginoffcode());
        mav.addObject("postList", postList);
        updateLeave.setOffCode(lub.getLoginoffcode());
        mav.addObject("clupdateform", updateLeave);
        mav.setViewName("/LeaveUpdate/PostList");
        return mav;
    }

    @RequestMapping(value = "updateCasualLeave")
    public ModelAndView casualLeaveupdate(@ModelAttribute("clupdateform") UpdateLeave updateLeave, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response, @RequestParam("postCode") String postCode, @RequestParam Map<String, String> requestParams) {

        ModelAndView mav = new ModelAndView();
        ArrayList postList = postDAO.getOfficeWisePostList(lub.getLoginoffcode());
        mav.addObject("postList", postList);
        updateLeave.setOffCode(lub.getLoginoffcode());
        updateLeave.setPostCode(postCode);
        mav.addObject("clupdateform", updateLeave);
        mav.setViewName("/LeaveUpdate/CasualLeaveUpdate");
        return mav;
    }

    @RequestMapping(value = "updateCasualLeave", method = {RequestMethod.POST}, params = "btn=Save")
    public ModelAndView updateCLBalance(@ModelAttribute("clupdateform") UpdateLeave updateLeave, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result) {

        ModelAndView mav = new ModelAndView();
        leaveApplyDAO.updateCLBalance(updateLeave.getOffCode(), updateLeave.getPostCode(), updateLeave.getCldays(), updateLeave.getYear(), updateLeave.getMonth());
        ArrayList postList = postDAO.getOfficeWisePostList(lub.getLoginoffcode());
        mav.addObject("postList", postList);
        updateLeave.setOffCode(lub.getLoginoffcode());
        mav.addObject("clupdateform", updateLeave);
        mav.setViewName("/LeaveUpdate/CasualLeaveUpdate");
        return mav;
    }

    @RequestMapping(value = "updateCasualLeave", method = {RequestMethod.POST}, params = "btn=Back")
    public ModelAndView backToPostList(@ModelAttribute("clupdateform") UpdateLeave updateLeave, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result) {

        ModelAndView mav = new ModelAndView();
        ArrayList postList = postDAO.getOfficeWisePostList(lub.getLoginoffcode());
        mav.addObject("postList", postList);
        updateLeave.setOffCode(lub.getLoginoffcode());
        mav.addObject("clupdateform", updateLeave);
        mav.setViewName("/LeaveUpdate/PostList");
        return mav;
    }

    @RequestMapping(value = "postWiseEmpList")
    public ModelAndView getPostWiseEmpList(@ModelAttribute("clupdateform") UpdateLeave updateLeave, @ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletResponse response, @RequestParam("postCode") String postCode, @RequestParam("offCode") String offCode, BindingResult result) {
        System.out.println("Month in MM format = " + strMonth);
        ModelAndView mav = new ModelAndView();
        ArrayList empList = leaveUpdateDAO.getPostWiseEmpList(postCode, offCode, String.valueOf(curYear), strMonth);
        mav.addObject("empList", empList);
        updateLeave.setOffCode(offCode);
        updateLeave.setPostCode(postCode);
        mav.addObject("clupdateform", updateLeave);
        mav.addObject("offCode", offCode);
        mav.addObject("postCode", postCode);
        mav.setViewName("/LeaveUpdate/PostWiseEmpList");
        return mav;
    }

    @RequestMapping(value = "postWiseEmpList", method = {RequestMethod.POST}, params = "btn=Back")
    public ModelAndView postList(@ModelAttribute("clupdateform") UpdateLeave updateLeave, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result) {

        ModelAndView mav = new ModelAndView();
        ArrayList postList = postDAO.getOfficeWisePostList(lub.getLoginoffcode());
        mav.addObject("postList", postList);
        updateLeave.setOffCode(lub.getLoginoffcode());
        mav.addObject("clupdateform", updateLeave);
        mav.setViewName("/LeaveUpdate/PostList");
        return mav;
    }

    @RequestMapping(value = "getLeaveBalance", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView updateCLLeaveBalance(@ModelAttribute("clupdateform") UpdateLeave updateLeave, @ModelAttribute("LoginUserBean") LoginUserBean lub,
            @RequestParam("empId") String empId, @RequestParam("postCode") String postCode, @RequestParam("offCode") String offCode, @RequestParam("tolId") String tolId, BindingResult result) {

        ModelAndView mav = new ModelAndView();
        updateLeave = leaveUpdateDAO.getLeaveBalance(empId, String.valueOf(curYear), tolId, strMonth);
        updateLeave.setOffCode(offCode);
        updateLeave.setPostCode(postCode);
        updateLeave.setEmpid(empId);
        updateLeave.setTolId(tolId);
        mav.addObject("clupdateform", updateLeave);
        mav.setViewName("/LeaveUpdate/UpdateLeaveBalance");
        return mav;
    }

    @RequestMapping(value = "updateELLeaveBalance", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView updateELLeaveBalance(@ModelAttribute("clupdateform") UpdateLeave updateLeave, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("empId") String empId, BindingResult result) {

        ModelAndView mav = new ModelAndView();
        updateLeave.setOffCode(lub.getLoginoffcode());
        updateLeave.setEmpid(empId);
        mav.addObject("clupdateform", updateLeave);
        mav.setViewName("/LeaveUpdate/UpdateELLeaveBalance");
        return mav;
    }

    @RequestMapping(value = "updateLeaveBalance", method = {RequestMethod.GET, RequestMethod.POST}, params = "btn=Back")
    public ModelAndView backToLeaveList(@ModelAttribute("clupdateform") UpdateLeave updateLeave, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result) {

        ModelAndView mav = new ModelAndView();
        EmployeeInformation empinfo = empinfoDAO.getEmployeeData(updateLeave.getEmpid(), "");
        ArrayList empWiseLeaveList = leaveUpdateDAO.getEmpWiseLeaveList(updateLeave.getEmpid(), String.valueOf(curYear), strMonth);
        mav.addObject("empWiseLeaveList", empWiseLeaveList);
        updateLeave.setEmpName(empinfo.getEmpname());
        mav.addObject("clupdateform", updateLeave);
        mav.setViewName("/LeaveUpdate/EmpWiseLeaveList");
        return mav;
    }

    @RequestMapping(value = "updateLeaveBalance", method = {RequestMethod.GET, RequestMethod.POST}, params = "btn=Save")
    public ModelAndView saveCLBalance(@ModelAttribute("clupdateform") UpdateLeave updateLeave, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result) {//

        ModelAndView mav = new ModelAndView();
        leaveUpdateDAO.updateLeaveBalance(updateLeave);
        EmployeeInformation empinfo = empinfoDAO.getEmployeeData(updateLeave.getEmpid(), "");
        ArrayList empWiseLeaveList = leaveUpdateDAO.getEmpWiseLeaveList(updateLeave.getEmpid(), String.valueOf(curYear), strMonth);
        mav.addObject("empWiseLeaveList", empWiseLeaveList);
        updateLeave.setEmpName(empinfo.getEmpname());
        mav.addObject("clupdateform", updateLeave);
        mav.setViewName("/LeaveUpdate/EmpWiseLeaveList");
        return mav;
    }

    @RequestMapping(value = "leaveStatusReport")
    public ModelAndView leaveStatusReport(@ModelAttribute("clupdateform") UpdateLeave updateLeave, @ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletResponse response, BindingResult result) {
        Calendar cal = Calendar.getInstance();
        int curmon = cal.get(Calendar.MONTH) + 1;
        String curDate = cal.get(Calendar.DATE) + "-" + curmon + "-" + cal.get(Calendar.YEAR) + "";
        ModelAndView mav = new ModelAndView();
        /* String year="";
         String month="";
         if(updateLeave.getSltyear()!=null && updateLeave.getSltmonth()!=null){
         year=updateLeave.getSltyear();
         month=updateLeave.getSltmonth();
         }else{
         year= String.valueOf(cal.get(Calendar.YEAR));
         month=String.valueOf(curmon);
         }*/
        // ArrayList postList = postDAO.getOfficeWisePostList(lub.getLoginoffcode());
        // HashMap<String,Object> empList = leaveApplyDAO.getPostWiseEmpList(postCode,offCode);
//        double mon=Double.parseDouble(updateLeave.getSltmonth())-1;
//        System.out.println("==mon===="+mon);
//        System.out.println("============="+updateLeave.getSltyear()+"========="+updateLeave.getSltmonth());
//        ArrayList empList = leaveUpdateDAO.getLeaveStatusReport(lub.getLoginoffcode(), updateLeave.getSltyear(), updateLeave.getSltmonth(), String.valueOf(cal.get(Calendar.DATE)));
//        updateLeave.setCurDate(curDate);
//        mav.addObject("empList", empList);
//        mav.addObject("clupdateform", updateLeave);
        mav.setViewName("/LeaveUpdate/LeaveStatusReport");
        return mav;
    }

    @RequestMapping(value = "leaveStatusReport", method = {RequestMethod.GET, RequestMethod.POST}, params = "btn=Ok")
    public ModelAndView leaveStatusReportByMonth(@ModelAttribute("clupdateform") UpdateLeave updateLeave, @ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletResponse response, BindingResult result) {
        Calendar cal = Calendar.getInstance();
        int curmon = cal.get(Calendar.MONTH) + 1;
        String curDate = cal.get(Calendar.DATE) + "-" + curmon + "-" + cal.get(Calendar.YEAR) + "";
        ModelAndView mav = new ModelAndView();
        // System.out.println(""+updateLeave.getSltyear()+"******"+updateLeave.getSltmonth());
        // ArrayList postList = postDAO.getOfficeWisePostList(lub.getLoginoffcode());
        // HashMap<String,Object> empList = leaveApplyDAO.getPostWiseEmpList(postCode,offCode);

        ArrayList empList = leaveUpdateDAO.getLeaveStatusReport(lub.getLoginoffcode(), updateLeave.getSltyear(), updateLeave.getSltmonth(), String.valueOf(cal.get(Calendar.DATE)));
        updateLeave.setCurDate(curDate);
        mav.addObject("empList", empList);
        mav.addObject("clupdateform", updateLeave);
        mav.setViewName("/LeaveUpdate/LeaveStatusReport");
        return mav;
    }

    @RequestMapping(value = "viewWorkingHourDetail", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView viewWorkingHourDetail(@ModelAttribute("clupdateform") UpdateLeave updateLeave, @ModelAttribute("LoginUserBean") LoginUserBean lub,
            @RequestParam("userId") String userId, @RequestParam("year") String year, @RequestParam("month") String month, BindingResult result) {//

        ModelAndView mav = new ModelAndView();
        updateLeave.setSltmonth(month);
        updateLeave.setSltyear(year);
        ArrayList monthlyWorkingHourList = leaveUpdateDAO.getMonthlyWorkingHour(year, month, userId);
        mav.addObject("monthlyWorkingHourList", monthlyWorkingHourList);
        mav.setViewName("/LeaveUpdate/WorkingHourDetail");
        return mav;
    }

    @RequestMapping(value = "viewWorkingHourDetail", method = {RequestMethod.GET, RequestMethod.POST}, params = "btn=Back")
    public ModelAndView backToLeaveBalanceReport(@ModelAttribute("clupdateform") UpdateLeave updateLeave, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result) {
        Calendar cal = Calendar.getInstance();
        int curmon = cal.get(Calendar.MONTH) + 1;
        String curDate = cal.get(Calendar.DATE) + "-" + curmon + "-" + cal.get(Calendar.YEAR) + "";
        ModelAndView mav = new ModelAndView();
        ArrayList empList = leaveUpdateDAO.getLeaveStatusReport(lub.getLoginoffcode(), updateLeave.getSltyear(), updateLeave.getSltmonth(), String.valueOf(cal.get(Calendar.DATE)));
        updateLeave.setCurDate(curDate);
        mav.addObject("empList", empList);
        mav.addObject("clupdateform", updateLeave);
        mav.setViewName("/LeaveUpdate/LeaveStatusReport");
        return mav;
    }

    @RequestMapping(value = "getLeaveList", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView getEmpWiseLeaveList(@ModelAttribute("clupdateform") UpdateLeave updateLeave, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("empId") String empId,
            @RequestParam("postCode") String postCode, @RequestParam("offCode") String offCode, BindingResult result) {//
        ModelAndView mav = new ModelAndView();
        EmployeeInformation empinfo = empinfoDAO.getEmployeeData(empId, "");
        ArrayList empWiseLeaveList = leaveUpdateDAO.getEmpWiseLeaveList(empId, String.valueOf(curYear), strMonth);
        mav.addObject("empWiseLeaveList", empWiseLeaveList);
        updateLeave.setOffCode(offCode);
        updateLeave.setPostCode(postCode);
        updateLeave.setEmpid(empId);
        updateLeave.setEmpName(empinfo.getEmpname());
        mav.addObject("clupdateform", updateLeave);
        mav.setViewName("/LeaveUpdate/EmpWiseLeaveList");
        return mav;
    }

    @RequestMapping(value = "getLeaveList", method = {RequestMethod.GET, RequestMethod.POST}, params = "btn=Back")
    public ModelAndView backToEmpWisePost(@ModelAttribute("clupdateform") UpdateLeave updateLeave, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result) {
        ModelAndView mav = new ModelAndView();
        leaveUpdateDAO.updateLeaveBalance(updateLeave);
        ArrayList empList = leaveUpdateDAO.getPostWiseEmpList(updateLeave.getPostCode(), updateLeave.getOffCode(), String.valueOf(curYear), strMonth);
        mav.addObject("empList", empList);
        //ArrayList empList = leaveUpdateDAO.updateCLLeaveBalance(updateLeave.getPostCode(),updateLeave.getOffCode(),curYear);
        //mav.addObject("empList", empList);
        mav.addObject("clupdateform", updateLeave);
        mav.setViewName("/LeaveUpdate/PostWiseEmpList");
        return mav;
    }

    @RequestMapping(value = "leaveStatusReport", method = {RequestMethod.GET, RequestMethod.POST}, params = "btn=Excel Format")
    protected ModelAndView downloadEmpAttendanceInExcel(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("clupdateform") UpdateLeave updateLeave, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        if (updateLeave.getSltyear() != null && !updateLeave.getSltyear().equals("") && updateLeave.getSltmonth() != null && !updateLeave.getSltmonth().equals("")) {
            List<UpdateLeave> empAttendanceList = leaveUpdateDAO.getLeaveStatusReport(lub.getLoginoffcode(), updateLeave.getSltyear(), updateLeave.getSltmonth(), String.valueOf(cal.get(Calendar.DATE)));
            return new ModelAndView("empAttendanceData", "empAttendanceList", empAttendanceList);
        } else {
            List<UpdateLeave> empAttendanceList = leaveUpdateDAO.getLeaveStatusReport(lub.getLoginoffcode(), "", "", String.valueOf(cal.get(Calendar.DATE)));
            return new ModelAndView("empAttendanceData", "empAttendanceList", empAttendanceList);
        }
    }
}
