package hrms.controller.leaveapply;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;
import hrms.dao.employee.EmployeeDAO;
import hrms.dao.leaveapply.LeaveApplyDAO;
import hrms.dao.login.LoginDAOImpl;
import hrms.dao.master.LeaveTypeDAO;
import hrms.dao.notification.NotificationDAOImpl;
import hrms.dao.workflowrouting.WorkflowRoutingDAO;
import hrms.model.leave.Leave;
import hrms.model.leave.LeaveEntrytakenBean;
import hrms.model.leave.LeaveSancBean;
import hrms.model.login.LoginUserBean;
import hrms.model.login.Users;
import hrms.model.notification.NotificationBean;
import hrms.thread.leave.LeaveThread;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

@Controller
@SessionAttributes("LoginUserBean")
public class LeaveApplyController {

    @Autowired
    public LeaveApplyDAO leaveApplyDAO;
    @Autowired
    public LoginDAOImpl loginDAO;
    @Autowired
    NotificationDAOImpl notificationDao;
    @Autowired
    WorkflowRoutingDAO workflowRoutingDao;
    @Autowired
    EmployeeDAO employeeDAO;

    @Autowired
    public LeaveThread leaveThread;

    @Autowired
    LeaveTypeDAO leaveTypeDAO;

    public static String getServerDoe() {
        String currDate;
        Format formatter;
        formatter = new SimpleDateFormat("dd-MMM-yyyy");
        currDate = formatter.format(new Date());
        return currDate;
    }
    SimpleDateFormat simpleformat = new SimpleDateFormat("MM");
    String strMonth = simpleformat.format(new Date());

    @ResponseBody
    @RequestMapping(value = "leaveapplylist.htm", method = {RequestMethod.GET, RequestMethod.POST})
    public void viewLeaveList(@ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, Map<String, Object> model, HttpServletResponse response, @RequestParam("empId") String empid) {
        response.setContentType("application/json");
        JSONObject json = new JSONObject();
        PrintWriter out = null;
        List leavelist = null;
        String employeeid = "";
        try {
            if (empid != null && !empid.equals("")) {
                employeeid = empid;
            } else {
                employeeid = lub.getLoginempid();
            }
            leavelist = leaveApplyDAO.getLeaveApplyList(employeeid);
            json.put("total", 50);
            json.put("rows", leavelist);
            out = response.getWriter();
            out.write(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    @RequestMapping(value = "leaveapply.htm", method = {RequestMethod.GET, RequestMethod.POST})
    public String viewLeave(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("leaveForm") Leave leave, Map<String, Object> model, @RequestParam("empId") String empid) {

        String employeeid = "";

        if (empid != null && !empid.equals("")) {
            employeeid = empid;
        } else {
            employeeid = lub.getLoginempid();
        }
        leave.setHidempId(employeeid);
        leave.setOffCode(lub.getLoginoffcode());
        model.put("curdate", getServerDoe());
//        leaveApplyDAO.getLeaveOpeningBalance(employeeid, "CL", getServerDoe());
//         leaveApplyDAO.getLeaveOpeningBalance(employeeid, "EL", getServerDoe());
//         leaveApplyDAO.getLeaveOpeningBalance(employeeid, "HPL", getServerDoe());
//         leaveApplyDAO.getLeaveOpeningBalance(employeeid, "COL", getServerDoe());
        
        model.put("elBalance", leaveApplyDAO.getLeaveBalanceInfo(employeeid, "EL", getServerDoe().substring(7, 11), strMonth));
        model.put("clBalance", leaveApplyDAO.getLeaveBalanceInfo(employeeid, "CL", getServerDoe().substring(7, 11), strMonth));
        model.put("hplBalance", leaveApplyDAO.getLeaveBalanceInfo(employeeid, "HPL", getServerDoe().substring(7, 11), strMonth));
        model.put("colBalance", leaveApplyDAO.getLeaveBalanceInfo(employeeid, "COL", getServerDoe().substring(7, 11), strMonth));
        model.put("leaveForm", leave);
        return "/leaveapply/LeaveApplyList";
    }

    @RequestMapping(value = "addleaveapply.htm", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView addLeave(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("leaveForm") Leave leave, Map<String, Object> model, @RequestParam("empId") String empid, @RequestParam("offCode") String offCode) {
        // lub.setEmpid("59001138");
        ModelAndView mav = new ModelAndView();
        Users user = loginDAO.getEmployeeProfileInfo(empid);
        if (!empid.equals(lub.getLoginempid())) {
            leave.setHidempId(empid);
            leave.setHidSpcCode(user.getCurspc());
            mav.addObject("empList", workflowRoutingDao.getWorkFlowRoutingList(1, user.getGpc(), user.getOffcode()));
            leave.setApplyFor("OTHER");
        } else {
            leave.setHidempId(lub.getLoginempid());
            leave.setHidSpcCode(lub.getLoginspc());
            mav.addObject("empList", workflowRoutingDao.getWorkFlowRoutingList(1, lub.getLogingpc(), lub.getLoginoffcode()));
        }
        leave.setOffCode(offCode);

        leave.setApplicantName(user.getFullName());
        mav.addObject("leaveForm", leave);
        mav.setViewName("leaveapply/LeaveApplyEdit");
        return mav;
    }

    @RequestMapping(value = "leaveextension.htm", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView leaveExtension(@RequestParam("taskId") String taskId, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("leaveForm") Leave leave, Map<String, Object> model) {
        ModelAndView mav = new ModelAndView();
        leave = leaveApplyDAO.getLeaveData(taskId, lub.getLoginempid(), lub.getLoginspc());
        leave.setHidempId(lub.getLoginempid());
        leave.setHidSpcCode(lub.getLoginspc());
        mav.addObject("leaveForm", leave);
        mav.addObject("empList", workflowRoutingDao.getWorkFlowRoutingList(1, lub.getLogingpc(), lub.getLoginoffcode()));
        mav.setViewName("leaveapply/LeaveApplyEdit");
        return mav;
    }

    @RequestMapping(params = "ApplyFor", value = "leaveapply.htm", method = {RequestMethod.POST, RequestMethod.GET})
    public String applyForOther(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("leaveForm") Leave leave, Map<String, Object> model) {
        leave.setHidempId(lub.getLoginempid());
        leave.setHidSpcCode(lub.getLoginspc());
        model.put("leaveForm", leave);
        return "leaveapply/LeaveApplyFor";
    }

    @RequestMapping(value = "leaveapplyforemp.htm", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView LeaveApplyForOther(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("leaveForm") Leave leave, Map<String, Object> model) {
        // lub.setEmpid("59001138");
        ModelAndView mav = new ModelAndView();
        leave.setHidempId(lub.getLoginempid());
        leave.setHidSpcCode(lub.getLoginspc());
        mav.addObject("empList", employeeDAO.getOffWiseEmpList(lub.getLoginoffcode()));
        mav.addObject("leaveForm", leave);
        mav.setViewName("leaveapply/LeaveApplyForEmployees");
        return mav;
    }

    @RequestMapping(value = "leavestatus.htm", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView leaveStatus(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("leaveForm") Leave leave, Map<String, Object> model) {
        // lub.setEmpid("59001138");

        ModelAndView mav = new ModelAndView();
        leave.setHidempId(lub.getLoginempid());
        leave.setHidSpcCode(lub.getLoginspc());
        // mav.addObject("empLeaveStatusList", leaveApplyDAO.getOffWiseEmpLeaveList(lub.getLoginoffcode(),leave.getLeavePeriodFrom(),leave.getLeavePeriodTo()));
        mav.addObject("leaveForm", leave);
        mav.setViewName("leaveapply/LeaveStatusList");
        return mav;
    }

    @RequestMapping(value = "leavestatus.htm", params = "Status", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView leaveStatusList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("leaveForm") Leave leave, Map<String, Object> model) {
        // lub.setEmpid("59001138");

        ModelAndView mav = new ModelAndView();
        leave.setHidempId(lub.getLoginempid());
        leave.setHidSpcCode(lub.getLoginspc());
        mav.addObject("empLeaveStatusList", leaveApplyDAO.getOffWiseEmpLeaveList(lub.getLoginoffcode(), leave.getLeavePeriodFrom(), leave.getLeavePeriodTo()));
        mav.addObject("leaveForm", leave);
        mav.setViewName("leaveapply/LeaveStatusList");
        return mav;
    }

    @RequestMapping(value = "leaveapplyedit.htm", method = {RequestMethod.POST, RequestMethod.GET}, params = "Save")
    public String submitLeave(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("leaveForm") Leave leave, Map<String, Object> model) {
        String path = "";
        String deviceFcmToken = "";
        try {
            boolean ifMaxChild = leaveApplyDAO.ifMaxSurviveChild(leave.getSltleaveType(), leave.getHidempId());
            boolean ifMoreThanMaxPeriod = leaveApplyDAO.maxPeriodCount(leave);
            double maxPeriodCnt = leaveApplyDAO.maxLeavePeriodCount(leave.getSltleaveType());
            String leaveType = leaveApplyDAO.getLeaveType(leave.getSltleaveType());
            boolean ifEmpExist = leaveApplyDAO.ifEmpExist(leave.getHidempId());
            boolean ifexist = leaveApplyDAO.getIfLeaveRecordExist(leave.getHidempId(), leave.getSltleaveType(), leave.getTxtperiodFrom(), leave.getTxtperiodTo());
            model.put("curdate", getServerDoe());
            if (ifEmpExist == false) {
                leaveApplyDAO.saveLeave(leave);
                String authKey = "AIzaSyBEybmPdFAQh85R4EC2Vy2qu6two-y7Jcs";   // You FCM AUTH key
                String FMCurl = "https://fcm.googleapis.com/fcm/send";

                URL url = new URL(FMCurl);

                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                // HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setUseCaches(false);
                conn.setDoInput(true);
                conn.setDoOutput(true);

                conn.setRequestMethod("POST");

                conn.setRequestProperty("Authorization:", "key=AIzaSyBEybmPdFAQh85R4EC2Vy2qu6two-y7Jcs");
                conn.setRequestProperty("Content-Type:", "application/json");
                conn.connect();
                JSONObject json = new JSONObject();
                json.put("to", "c2kkaUXvq1E:APA91bENuFnB3MW1gHkh1nZZnRhwuC1JJ-9oi6synS8qemtCw96l2R1WV_M3G73Su3D5hJAc0v0Q02bm4I2X3Xke7ssmdrwJJCY3u4xjyUx-dItIgJmBOFWI3UR7qEhR53-BDU9UYrE2");
                JSONObject info = new JSONObject();
                info.put("title", "HRMS");   // Notification title
                info.put("body", "Leave is Applied By"); // Notification body
                json.put("notification", info);
                json.put("priority", "high");

                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                wr.write(json.toString());
                wr.flush();
                wr.close();

                int responseCode = conn.getResponseCode();

                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                /*leaveApplyDAO.getLeaveOpeningBalance(lub.getLoginempid(), "EL", getServerDoe());
                 leaveApplyDAO.getLeaveOpeningBalance(lub.getLoginempid(), "CL", getServerDoe());
                 leaveApplyDAO.getLeaveOpeningBalance(lub.getLoginempid(), "HPL", getServerDoe());
                 */
                //LeaveBalanceBean balanceBean = leaveapplyDAO.getLeaveBalanceInfo(lub.getEmpid());
                model.put("elBalance", leaveApplyDAO.getLeaveBalanceInfo(lub.getLoginempid(), "EL", getServerDoe().substring(7, 11), strMonth));
                model.put("clBalance", leaveApplyDAO.getLeaveBalanceInfo(lub.getLoginempid(), "CL", getServerDoe().substring(7, 11), strMonth));
                model.put("hplBalance", leaveApplyDAO.getLeaveBalanceInfo(lub.getLoginempid(), "HPL", getServerDoe().substring(7, 11), strMonth));
                model.put("colBalance", leaveApplyDAO.getLeaveBalanceInfo(lub.getLoginempid(), "COL", getServerDoe().substring(7, 11), strMonth));
                path = "/leaveapply/LeaveApplyList";

            } else {
                ifexist = leaveApplyDAO.getIfLeaveRecordExist(leave.getHidempId(), leave.getSltleaveType(), leave.getTxtperiodFrom(), leave.getTxtperiodTo());
                if (ifexist == false) {
                    model.put("errors", "Leave already applied for this period");
                    path = "/leaveapply/LeaveApplyEdit";
                } else if (ifMoreThanMaxPeriod == false && !leaveType.equals("HDL")) {
                    model.put("errors1", "MAXIMUM " + leaveType + " MAY BE GRANTED AT A TIME TO A GOVERNMENT EMPLOYEE CAN NOT BE MORE THAN " + maxPeriodCnt + " DAYS");
                    path = "/leaveapply/LeaveApplyEdit";
                } else if (ifMaxChild == false) {
                    model.put("errors2", "MAXIMUM TWO SURVIVING CHILDREN PARENT CAN APPLY FOR MATERNITY/PATERNITY LEAVE");
                    path = "/leaveapply/LeaveApplyEdit";
                } else {
                    leaveApplyDAO.saveLeave(leave);
                    deviceFcmToken = leaveApplyDAO.getFcmToken(leave.getHidAuthEmpId());
                    if (deviceFcmToken != null && !deviceFcmToken.equals("")) {
                        leaveApplyDAO.pushFCMNotification(deviceFcmToken, leave.getHidempId(), leave.getHidSpcCode(), "3");
                    }
//                   leaveApplyDAO.getLeaveOpeningBalance(lub.getLoginempid(), "EL", getServerDoe());
//                     leaveApplyDAO.getLeaveOpeningBalance(lub.getLoginempid(), "CL", getServerDoe());
//                     leaveApplyDAO.getLeaveOpeningBalance(lub.getLoginempid(), "HPL", getServerDoe());
                    
                    model.put("elBalance", leaveApplyDAO.getLeaveBalanceInfo(lub.getLoginempid(), "EL", getServerDoe().substring(7, 11), strMonth));
                    model.put("clBalance", leaveApplyDAO.getLeaveBalanceInfo(lub.getLoginempid(), "CL", getServerDoe().substring(7, 11), strMonth));
                    model.put("hplBalance", leaveApplyDAO.getLeaveBalanceInfo(lub.getLoginempid(), "HPL", getServerDoe().substring(7, 11), strMonth));
                    model.put("colBalance", leaveApplyDAO.getLeaveBalanceInfo(lub.getLoginempid(), "COL", getServerDoe().substring(7, 11), strMonth));

                    if (leave.getApplyFor() != null && leave.getApplyFor().equals("OTHER")) {
                        path = "redirect:leaveapplyforemp.htm";
                    } else {
                        // /leaveapply/LeaveApplyList
                        path = "redirect:leaveapply.htm?empId=" + lub.getLoginempid();
                    }
                }
            }

            model.put("leaveForm", leave);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return path;
    }

    @RequestMapping(value = "leaveViewData.htm")
    public ModelAndView getLeaveData(@ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("taskId") String taskId, @ModelAttribute("leaveForm") Leave leave, Map<String, Object> model) throws Exception {
        String authorityEmpCode = null;
        ModelAndView mav = new ModelAndView();
        ArrayList workFlowDtls = null;
        if (taskId != null && !taskId.equals("")) {
            leave.setHidTaskId(taskId);
        } else {
            leave.setHidTaskId(leave.getHidTaskId());
        }
        if (taskId != null && !taskId.equals("")) {
            leave = leaveApplyDAO.getLeaveData(taskId, lub.getLoginempid(), lub.getLoginspc());
            authorityEmpCode = leaveApplyDAO.getAuthorityEmpCode(taskId);
            workFlowDtls = leaveApplyDAO.getLeaveWorkFlowDtls(taskId);
        }
        if (leave.getHidTaskId() != null && !leave.getHidTaskId().equals("")) {
            leave = leaveApplyDAO.getLeaveData(taskId, lub.getLoginempid(), lub.getLoginspc());
            authorityEmpCode = leaveApplyDAO.getAuthorityEmpCode(taskId);
        }
        if (lub.getLoginempid().equals(authorityEmpCode)) {
            leave.setPassString("Task");
        } else {
            leave.setPassString("Own");
        }
        leave.setStatusId(leaveApplyDAO.getStatusId(taskId));
        leave.setFileArrList(leaveApplyDAO.getFileName(taskId, "M"));
        leave.setJoinFileArrList(leaveApplyDAO.getFileName(taskId, "N"));
        model.put("elBalance", leaveApplyDAO.getLeaveBalanceInfo(leave.getHidempId(), "EL", getServerDoe().substring(7, 11), strMonth));
        model.put("clBalance", leaveApplyDAO.getLeaveBalanceInfo(leave.getHidempId(), "CL", getServerDoe().substring(7, 11), strMonth));
        model.put("hplBalance", leaveApplyDAO.getLeaveBalanceInfo(leave.getHidempId(), "HPL", getServerDoe().substring(7, 11), strMonth));
        model.put("colBalance", leaveApplyDAO.getLeaveBalanceInfo(leave.getHidempId(), "COL", getServerDoe().substring(7, 11), strMonth));
        model.put("curdate", getServerDoe());
        mav.addObject("leaveForm", leave);
        mav.addObject("work", workFlowDtls);
        mav.addObject("taskId", taskId);
        mav.setViewName("leaveapply/LeaveApplyView");
        return mav;
    }

    @RequestMapping(value = "leaveViewDataOE.htm")
    public ModelAndView getLeaveDataForOE(@ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("taskId") String taskId, @ModelAttribute("leaveForm") Leave leave, Map<String, Object> model) throws Exception {
        String authorityEmpCode = null;
        ModelAndView mav = new ModelAndView();
        ArrayList workFlowDtls = null;

        if (taskId != null && !taskId.equals("")) {
            leave.setHidTaskId(taskId);
        } else {
            leave.setHidTaskId(leave.getHidTaskId());
        }
        if (taskId != null && !taskId.equals("")) {
            leave = leaveApplyDAO.getLeaveData(taskId, lub.getLoginempid(), lub.getLoginspc());
            authorityEmpCode = leaveApplyDAO.getAuthorityEmpCode(taskId);
            workFlowDtls = leaveApplyDAO.getLeaveWorkFlowDtls(taskId);
        }
        if (leave.getHidTaskId() != null && !leave.getHidTaskId().equals("")) {
            leave = leaveApplyDAO.getLeaveData(taskId, lub.getLoginempid(), lub.getLoginspc());
            authorityEmpCode = leaveApplyDAO.getAuthorityEmpCode(taskId);
        }
        if (lub.getLoginempid().equals(authorityEmpCode)) {
            leave.setPassString("Task");
        } else {
            leave.setPassString("Own");
        }
        leave.setStatusId(leaveApplyDAO.getStatusId(taskId));
        leave.setFileArrList(leaveApplyDAO.getFileName(taskId, "M"));
        leave.setJoinFileArrList(leaveApplyDAO.getFileName(taskId, "N"));
        model.put("elBalance", leaveApplyDAO.getLeaveBalanceInfo(leave.getHidempId(), "EL", getServerDoe().substring(7, 11), strMonth));
        model.put("clBalance", leaveApplyDAO.getLeaveBalanceInfo(leave.getHidempId(), "CL", getServerDoe().substring(7, 11), strMonth));
        model.put("hplBalance", leaveApplyDAO.getLeaveBalanceInfo(leave.getHidempId(), "HPL", getServerDoe().substring(7, 11), strMonth));
        model.put("colBalance", leaveApplyDAO.getLeaveBalanceInfo(leave.getHidempId(), "COL", getServerDoe().substring(7, 11), strMonth));
        model.put("curdate", getServerDoe());
        mav.addObject("leaveForm", leave);
        mav.addObject("work", workFlowDtls);
        mav.setViewName("leaveapply/LeaveViewForOE");
        return mav;
    }

    @RequestMapping(value = "leaveViewData.htm", method = RequestMethod.POST, params = "Back")
    public String back(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("leaveForm") Leave leave, Map<String, Object> model) {

        return "redirect:leaveapply.htm?empId=" + leave.getHidempId();
    }

    @RequestMapping(value = "leaveViewDataOE.htm", method = RequestMethod.POST, params = "Back")
    public String backToOELeaveList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("leaveForm") Leave leave, Map<String, Object> model) {

        return "redirect:AppliedLeaveEmpList.htm";
    }

    @RequestMapping(value = "leaveapplyedit.htm", method = RequestMethod.POST, params = "Back1")
    public String backButton(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("leaveForm") Leave leave, Map<String, Object> model
    ) {
        return "redirect:leaveapply.htm?empId=" + leave.getHidempId();
    }

    @RequestMapping(value = "leaveViewData.htm", method = RequestMethod.POST, params = "TakeAction")
    public ModelAndView takeAction(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("leaveForm") Leave leave, Map<String, Object> model
    ) {
        String path = "";
        ModelAndView mav = new ModelAndView();
        double noOfDays = 0;
        LeaveSancBean lsb = null;
        String deviceFcmToken = "";
        Calendar cal = Calendar.getInstance();
        int curYear = cal.get(Calendar.YEAR);
        SimpleDateFormat simpleformat = new SimpleDateFormat("MM");
        String strMonth = simpleformat.format(new Date());
       
        try {
            leave.setHidempId(lub.getLoginempid());
            leave.setHidSpcCode(lub.getLoginspc());
            lsb = leaveApplyDAO.getLeaveSancInfo(leave.getHidTaskId());
            if (leave.getSltActionType() != null && leave.getSltActionType().equals("1")) {
                leaveApplyDAO.updateApproveDate(leave);
                leaveApplyDAO.updateTaskList(leave);
                noOfDays = leaveApplyDAO.calculateDateDiff(leave.getTxtApproveFrom(), leave.getTxtApproveTo(), lsb.getInitiatedEmpId(), leave.getTollid());
                deviceFcmToken = leaveApplyDAO.getFcmToken(leave.getHidempId());
                if (deviceFcmToken != null && !deviceFcmToken.equals("")) {
                    leaveApplyDAO.pushApproveFCMNotification(deviceFcmToken, leave.getHidAuthEmpId(), leave.getHidSpcAuthCode(), "1");
                    //leaveApplyDAO.pushFCMNotification(deviceFcmToken, leave.getHidAuthEmpId(), leave.getHidSpcAuthCode(), "3");
                }
                if (leave.getTollid().equals("CL") || leave.getTollid().equals("HDL")) {
                    leaveApplyDAO.updateClLeaveBalance(lsb.getInitiatedEmpId(), leave.getTollid(), noOfDays, String.valueOf(curYear), strMonth);
                }
                if (leave.getTollid().equals("EL")) {
                    leaveApplyDAO.updateElLeaveBalance(lsb.getInitiatedEmpId(), leave.getTollid(), noOfDays, String.valueOf(curYear), strMonth);
                }
                if (leave.getTollid().equals("HPL")) {
                    leaveApplyDAO.updateHplLeaveBalance(lsb.getInitiatedEmpId(), leave.getTollid(), noOfDays, getServerDoe().substring(7, 11));
                }
                if (leave.getTollid().equals("COL")) {
                    leaveApplyDAO.updateCommutedLeaveBalance(lsb.getInitiatedEmpId(), leave.getTollid(), noOfDays);
                }
                if (leave.getTollid().equals("ML")) {
                    leaveApplyDAO.updateMaternityLeaveBalance(lsb.getInitiatedEmpId(), leave.getTollid(), noOfDays);
                }
                if (leave.getTollid().equals("PL")) {
                    leaveApplyDAO.updatePaternityLeaveBalance(lsb.getInitiatedEmpId(), leave.getTollid(), noOfDays);
                }

            }
            if (leave.getSltActionType() != null && leave.getSltActionType().equals("42")) {
                leaveApplyDAO.updateApproveDate(leave);
                leaveApplyDAO.updateTaskList(leave);
            }

            if (leave.getSltActionType() != null && leave.getSltActionType().equals("2")) {
                leaveApplyDAO.updateTaskList(leave);
            }
            if (leave.getSltActionType() != null && leave.getSltActionType().equals("6")) {
                leaveApplyDAO.updateTaskList(leave);
                path = "leaveapply/LeaveViewAction";
            }

            if (leave.getSltActionType() != null && leave.getSltActionType().equals("105")) {
                path = "leaveapply/LeaveViewAction";
                leaveApplyDAO.approveCLCancel(leave, lsb.getInitiatedEmpId());
                
            }

            String appEmpName = leaveApplyDAO.getApplicant(leave.getHidTaskId());
            mav.addObject("empName", appEmpName);
            if (leave.getSltActionType() != null) {
                if (leave.getSltActionType().equals("2") || leave.getSltActionType().equals("0") || leave.getSltActionType().equals("4")) {
                    path = "leaveapply/LeaveViewAction";
                    //mav.setViewName("leaveapply/LeaveViewAction");
                }
                if (leave.getSltActionType().equals("1") || leave.getSltActionType().equals("5") || leave.getSltActionType().equals("42")) {
                    path = "leaveapply/LeaveViewAction";
                    // mav.setViewName("leaveapply/GenerateLeaveOrdEdit");
                }
            }

            mav.setViewName(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "leaveViewData.htm", method = RequestMethod.POST, params = "Submit")
    public ModelAndView submitLeaveDoc(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("leaveForm") Leave leave, Map<String, Object> model
    ) {
        ModelAndView mav = new ModelAndView();
        NotificationBean nb = new NotificationBean();
        LeaveEntrytakenBean leb = new LeaveEntrytakenBean();
        LeaveSancBean lsb = new LeaveSancBean();
        DateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        Date orddate = null;
        double noOfDays = 0;
        int notId = 0;
        try {
            leave.setLoginUser(lub.getLoginempid());
            leave.setHidSpcCode(lub.getLoginspc());
            if (leave.getSltActionType() != null && !leave.getSltActionType().equals("")) {
                leave.setStatusId(leave.getSltActionType());
            } else {

                leave.setStatusId(leave.getStatusId());
            }
            if (leave.getTxtOrdDate() != null && !leave.getTxtOrdDate().equals("")) {
                orddate = df.parse(leave.getTxtOrdDate());
            }

            leaveApplyDAO.updateLeaveOrder(leave);
            // leaveApplyDAO.updateTaskList(leave);

            Date servdate = df.parse(getServerDoe());
//             
            nb.setNottype("LEAVE");
//            nb.setEmpId(lub.getEmpid());
            nb.setDateofEntry(servdate);
            nb.setOrdno(leave.getTxtOrdNo());
            nb.setOrdDate(orddate);

            leb = leaveApplyDAO.getEntryTaken(lub.getLoginempid());
            nb.setEntryDeptCode(leb.getDeptCode());
            nb.setEntryOffCode(leb.getOffcode());
            nb.setEntryAuthCode(lub.getLoginspc());

            lsb = leaveApplyDAO.getLeaveSancInfo(leave.getHidTaskId());
            nb.setSancDeptCode(lsb.getDeptCode());
            nb.setSancOffCode(lsb.getOffCode());
            nb.setSancAuthCode(lsb.getAuthCode());
            nb.setEmpId(lsb.getInitiatedEmpId());

            if (leave.getStatusId().equals("4")) {
                notId = notificationDao.insertNotificationData(nb);
            }

            leave.setEmpId(lsb.getInitiatedEmpId());
            leave.setTollid(lsb.getTolId());
            // leave.setTxtApproveFrom(lsb.getFromDate());
            //leave.setTxtApproveTo(lsb.getToDate());
            leave.setTxtprefixFrom(lsb.getPrefixFrom());
            leave.setTxtprefixTo(lsb.getPrefixTo());
            leave.setTxtsuffixFrom(lsb.getSuffixeFrom());
            leave.setTxtsuffixTo(lsb.getSuffixeTo());
            leave.setAuthPost(nb.getSancAuthCode());
            //leave.setJoiningDate(lsb.getJoinDate());
            if (leave.getStatusId().equals("4")) {
                leaveApplyDAO.updateEmpleave(leave, notId);
            }
            SimpleDateFormat sdfo = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdfo1 = new SimpleDateFormat("dd-MMM-yyyy");

            Date d1 = sdfo1.parse(leave.getTxtApproveTo());
            Date d2 = sdfo.parse(lsb.getToDate());
            Date d3 = sdfo.parse(lsb.getJoinDate());

            if (d1.compareTo(d2) <= 0 && d3.compareTo(d1) > 0) {

                if (leave.getStatusId().equals("41")) {
                    noOfDays = leaveApplyDAO.calculateDateDiffAfterJoin(leave.getTxtApproveFrom(), leave.getTxtApproveTo(), lsb.getInitiatedEmpId(), leave.getTollid());
                    leaveApplyDAO.updateApproveDate(leave);
                    if (leave.getTollid().equals("CL")) {

                        leaveApplyDAO.updateClLeaveBalanceAfterJoin(lsb.getInitiatedEmpId(), leave.getTollid(), noOfDays, getServerDoe().substring(7, 11));
                    }
                }

            }

            mav.addObject("leaveForm", leave);
            mav.setViewName("redirect:leaveViewData.htm?taskId=" + leave.getHidTaskId());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "leaveViewData.htm", method = RequestMethod.POST, params = "Print")
    public void viewLeavePdf(@ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletResponse response, @ModelAttribute("leaveForm") Leave leave
    ) {
        String gender = "";
        String gender1 = "";
        response.setContentType("application/pdf");
        Document document = new Document(PageSize.A4);
        try {
            response.setHeader("Content-Disposition", "attachment; filename=Leave_" + lub.getLoginempid() + ".pdf");
            leave = leaveApplyDAO.getLeaveData(leave.getHidTaskId(), lub.getLoginempid(), lub.getLoginspc());
            PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());
            Rectangle rect = new Rectangle(30, 30, 500, 800);
            writer.setBoxSize("art", rect);
            document.open();
            gender = leaveApplyDAO.getGender(lub.getLoginempid());
            if (gender != null && gender.equalsIgnoreCase("he")) {
                gender1 = "his";
            }
            if (gender != null && gender.equalsIgnoreCase("she")) {
                gender1 = "her";
            }
            if (leave.getStatusId().equals("4")) {
                leaveApplyDAO.viewPDFfunc(document, leave, lub.getLoginempid(), gender, gender1);
            }
            if (leave.getStatusId().equals("5")) {
                leaveApplyDAO.viewAllowedPDFfunc(document, leave, lub.getLoginempid(), gender, gender1);
            }
            if (leave.getStatusId().equals("41")) {
                leaveApplyDAO.viewBlankOrdnoDatePDFfunc(document, leave, lub.getLoginempid(), gender, gender1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }

    @RequestMapping(value = "leaveViewDataOE.htm", method = RequestMethod.POST, params = "Print")
    public void viewLeavePdfForOE(@ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletResponse response, @ModelAttribute("leaveForm") Leave leave
    ) {
        String gender = "";
        String gender1 = "";
        response.setContentType("application/pdf");
        Document document = new Document(PageSize.A4);
        try {
            response.setHeader("Content-Disposition", "attachment; filename=Leave_" + lub.getLoginempid() + ".pdf");
            leave = leaveApplyDAO.getLeaveData(leave.getHidTaskId(), lub.getLoginempid(), lub.getLoginspc());
            PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());
            Rectangle rect = new Rectangle(30, 30, 500, 800);
            writer.setBoxSize("art", rect);
            document.open();
            gender = leaveApplyDAO.getGender(lub.getLoginempid());
            if (gender != null && gender.equalsIgnoreCase("he")) {
                gender1 = "his";
            }
            if (gender != null && gender.equalsIgnoreCase("she")) {
                gender1 = "her";
            }
            if (leave.getStatusId().equals("4")) {
                leaveApplyDAO.viewPDFfunc(document, leave, lub.getLoginempid(), gender, gender1);
            }
            if (leave.getStatusId().equals("5")) {
                leaveApplyDAO.viewAllowedPDFfunc(document, leave, lub.getLoginempid(), gender, gender1);
            }
            if (leave.getStatusId().equals("41")) {
                leaveApplyDAO.viewBlankOrdnoDatePDFfunc(document, leave, lub.getLoginempid(), gender, gender1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }

    @RequestMapping(value = "joiningData.htm", method = {RequestMethod.GET, RequestMethod.POST})
    public void joiningData(@RequestParam("taskId") String taskId, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("leaveForm") Leave leave, Map<String, Object> model, HttpServletResponse response
    ) {
        response.setContentType("application/json");
        PrintWriter out = null;
        try {

            leave = leaveApplyDAO.getLeaveData(taskId, lub.getLoginempid(), lub.getLoginspc());
            leave.setTxtSancAuthority(leave.getPendingPostWthName());
            leave.setFileArrList(leaveApplyDAO.getFileName(taskId, "N"));
            JSONObject job = new JSONObject(leave);
            out = response.getWriter();
            out.write(job.toString());
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "saveJoiningData.htm", method = RequestMethod.POST)
    public void saveJoiningData(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("leaveForm") Leave leave, BindingResult result, HttpServletResponse response) throws IOException {
        try {
            response.setContentType("application/json");
            PrintWriter out = null;
            leaveApplyDAO.saveJoinData(leave);

            String isOSWASUser = leaveApplyDAO.isOSWASUser(lub.getLoginempid());

            if (isOSWASUser != null && isOSWASUser.equals("Y")) {
                //Web Service for OSWAS
                leaveThread.setThreadValue(lub.getLoginempid(), Integer.parseInt(leave.getHidTaskId()));
                //leaveThread.setThreadValue("43000104", 1);
                Thread t = new Thread(leaveThread);
                t.start();
            }

            JSONObject job = new JSONObject();
            out = response.getWriter();
            out.write(job.toString());
        } catch (Exception e) {
            e.printStackTrace();;
        } finally {

        }
    }

    @RequestMapping(value = "AppliedLeaveEmpList")
    public String appliedLeaveEmpList(Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("leaveForm") Leave leave) throws IOException {
        try {
            ArrayList emplist = leaveApplyDAO.getAppliedLeaveEmpList(lub.getLoginoffcode());
            model.addAttribute("emplist", emplist);
            model.addAttribute("leaveForm", leave);
        } catch (Exception e) {
            e.printStackTrace();;
        }
        return "/leaveapply/AppliedLeaveEmpList";
    }

    @RequestMapping(value = "viewSanctionLeaveOrder.htm", method = {RequestMethod.GET, RequestMethod.POST})
    public void viewSanctionOrd(@RequestParam("empId") String empid, @RequestParam("taskId") String taskId, @RequestParam("spc") String spc, @ModelAttribute("leaveForm") Leave leave, HttpServletResponse response) {
        response.setContentType("application/pdf");
        Document document = new Document(PageSize.A4);
        String gender = "";
        String gender1 = "";
        try {
            response.setHeader("Content-Disposition", "attachment; filename=Leave_" + empid + ".pdf");
            leave = leaveApplyDAO.getLeaveData(taskId, empid, spc);
            gender = leaveApplyDAO.getGender(empid);
            if (gender != null && gender.equalsIgnoreCase("he")) {
                gender1 = "his";
            }
            if (gender != null && gender.equalsIgnoreCase("she")) {
                gender1 = "her";
            }

            PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());
            Rectangle rect = new Rectangle(30, 30, 500, 800);
            writer.setBoxSize("art", rect);
            document.open();
            leaveApplyDAO.viewPDFfunc(document, leave, empid, gender, gender1);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }

    @RequestMapping(value = "AppliedLeaveEmpList", params = "action=Ok")
    public ModelAndView getAbseneteeList(@ModelAttribute("LoginUserBean") LoginUserBean lub, Users selectedEmpObj, @ModelAttribute("leaveForm") Leave leave) throws IOException {
        ModelAndView mv = new ModelAndView("/leaveapply/AppliedLeaveEmpList");
        List leaveAbsenteeStmt = leaveApplyDAO.getAbseneteeStmtList(lub.getLoginoffcode(), leave.getTxtperiodFrom(), leave.getTxtperiodTo());
        mv.addObject("emplist", leaveAbsenteeStmt);
        return mv;
    }

    @RequestMapping(value = "leaveViewData.htm", params = "cancel")
    public String cancelLeave(@ModelAttribute("LoginUserBean") LoginUserBean lub, Users selectedEmpObj, @ModelAttribute("leaveForm") Leave leave) throws IOException {
        leaveApplyDAO.cancelLeave(leave.getHidTaskId());
        return "redirect:leaveapply.htm?empId=" + leave.getHidempId();
    }

    @RequestMapping(value = "saveCLCancelData.htm")
    public String saveCLCancelData(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("leaveForm") Leave leave) throws IOException {
        try {
            leaveApplyDAO.saveCLCancelData(leave);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:leaveapply.htm?empId=" + lub.getLoginempid();
    }
    
    @ResponseBody
    @RequestMapping(value = "saveEOCancelData.htm")
    public void saveEOCancelData(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("leaveForm") Leave leave, @RequestParam("hidTaskId") String taskId) throws IOException {
        try {
            leaveApplyDAO.saveEOCancelData(leave);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "cancelleavelist.htm", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView cancelLeaveList(@ModelAttribute("LoginUserBean") LoginUserBean lub, Users selectedEmpObj, @ModelAttribute("leaveForm") Leave leave, @RequestParam("empId") String empid) throws IOException {
        ModelAndView mv = new ModelAndView("/leaveapply/CancelleaveList");
        // List searchleavelist = leaveApplyDAO.getSearchLeaveList(leave.getCriteria(),leave.getSearchString());
        mv.addObject("leave", leave);
        return mv;

    }

    @RequestMapping(value = "cancelleavelist.htm", method = {RequestMethod.GET, RequestMethod.POST}, params = "Search")
    public ModelAndView searchLeaveList(@ModelAttribute("LoginUserBean") LoginUserBean lub, Users selectedEmpObj, @ModelAttribute("leaveForm") Leave leave) throws IOException {
        ModelAndView mv = new ModelAndView("/leaveapply/CancelleaveList");
        List searchleavelist = leaveApplyDAO.getSearchLeaveList(leave.getCriteria(), leave.getSearchString());
        mv.addObject("searchleavelist", searchleavelist);
        mv.addObject("leave", leave);
        return mv;
    }

    @RequestMapping(value = "deleteLeaveData.htm", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView deleteLeave(@ModelAttribute("LoginUserBean") LoginUserBean lub, Users selectedEmpObj, @ModelAttribute("leaveForm") Leave leave, @RequestParam("taskId") String taskId, @RequestParam("criteria") String criteria, @RequestParam("searchstring") String searchstring) throws IOException {
        ModelAndView mv = new ModelAndView("/leaveapply/CancelleaveList");
        leaveApplyDAO.deleteLeave(taskId);

        List searchleavelist = leaveApplyDAO.getSearchLeaveList(criteria, searchstring);
        mv.addObject("searchleavelist", searchleavelist);
        mv.addObject("leave", leave);
        return mv;
    }

    /* @RequestMapping(value = "editLeaveData.htm", method = {RequestMethod.GET, RequestMethod.POST})
     public ModelAndView editLeave(@ModelAttribute("LoginUserBean") LoginUserBean lub, Users selectedEmpObj, @ModelAttribute("leaveForm") Leave leave, @RequestParam("taskId") String taskId, @RequestParam("criteria") String criteria, @RequestParam("searchstring") String searchstring) throws IOException {
     ModelAndView mv = new ModelAndView("/leaveapply/LeaveData");
     leave = leaveApplyDAO.getLeaveData(taskId, lub.getLoginempid(), lub.getLoginspc());
     leave.setSearchString(searchstring);
     List searchleavelist = leaveApplyDAO.getSearchLeaveList(criteria, searchstring);
     mv.addObject("searchleavelist", searchleavelist);
     List leaveTypelist = leaveTypeDAO.getLeaveTypeList();
     mv.addObject("leavetypelist", leaveTypelist);
     mv.addObject("leave", leave);

     return mv;
     }*/
    @RequestMapping(value = "editLeaveData.htm", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView updateLeave(@ModelAttribute("LoginUserBean") LoginUserBean lub, Users selectedEmpObj, @ModelAttribute("leaveForm") Leave leave, @RequestParam("taskId") String taskId, @RequestParam("criteria") String criteria, @RequestParam("searchstring") String searchstring) throws IOException {
        ModelAndView mv = new ModelAndView("/leaveapply/AppliedLeaveData");
        leave = leaveApplyDAO.getLeaveData(taskId, "", "");
        leave.setSearchString(searchstring);
        leave.setCriteria(criteria);
        leave.setTaskId(Integer.parseInt(taskId));
        List searchleavelist = leaveApplyDAO.getSearchLeaveList(criteria, searchstring);
        mv.addObject("searchleavelist", searchleavelist);
        List leaveTypelist = leaveTypeDAO.getLeaveTypeList();
        mv.addObject("leavetypelist", leaveTypelist);
        mv.addObject("leave", leave);

        return mv;
    }

    @RequestMapping(value = "editLeaveData.htm", method = {RequestMethod.GET, RequestMethod.POST}, params = "btn=Back")
    public ModelAndView leaveList(@ModelAttribute("LoginUserBean") LoginUserBean lub, Users selectedEmpObj, @ModelAttribute("leaveForm") Leave leave) throws IOException {
        ModelAndView mv = new ModelAndView("/leaveapply/CancelleaveList");
        List searchleavelist = leaveApplyDAO.getSearchLeaveList(leave.getCriteria(), leave.getSearchString());
        mv.addObject("searchleavelist", searchleavelist);
        mv.addObject("leave", leave);
        return mv;

    }

    @RequestMapping(value = "editLeaveData.htm", method = {RequestMethod.GET, RequestMethod.POST}, params = "btn=Save")
    public ModelAndView saveLeave(@ModelAttribute("LoginUserBean") LoginUserBean lub, Users selectedEmpObj, @ModelAttribute("leaveForm") Leave leave) throws IOException {
        ModelAndView mv = new ModelAndView("/leaveapply/CancelleaveList");
        leaveApplyDAO.saveManageLeave(leave);
        List searchleavelist = leaveApplyDAO.getSearchLeaveList(leave.getCriteria(), leave.getSearchString());
        mv.addObject("searchleavelist", searchleavelist);
        mv.addObject("leave", leave);
        return mv;

    }
}
