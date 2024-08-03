/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.restservices;

import static hrms.controller.leaveapply.LeaveApplyController.getServerDoe;
import hrms.dao.Rent.RentDAO;
import hrms.dao.employee.EmployeeDAO;
import hrms.dao.incrementsanction.IncrementSanctionDAO;
import hrms.dao.leaveapply.LeaveApplyDAO;
import hrms.dao.leaveauthority.LeaveAuthorityDAO;
import hrms.dao.loanworkflow.LoanApplyDAO;
import hrms.dao.login.LoginDAO;
import hrms.dao.payroll.payslip.PaySlipDAO;
import hrms.dao.task.TaskDAO;
import hrms.model.Rent.ScrollMain;
import hrms.model.employee.Employee;
import hrms.model.employee.PensionProfile;
import hrms.model.leave.Leave;
import hrms.model.leave.LeaveSancBean;
import hrms.model.loanworkflow.LoanForm;
import hrms.model.payroll.payslip.ADDetails;
import hrms.model.payroll.payslip.PaySlipDetailBean;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Blob;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.jws.WebParam;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.ServletContextAware;

/**
 *
 * @author Manas
 */
@RestController
@RequestMapping("/employeeRestService")
public class EmployeePS implements ServletContextAware {

    @Autowired
    LoanApplyDAO loanworkflowDao;

    @Autowired
    public LoginDAO loginDao;

    @Autowired
    public EmployeeDAO employeeDAO;

    @Autowired
    public LeaveApplyDAO leaveApplyDAO;

    private ServletContext context;
    @Autowired
    private LeaveAuthorityDAO leaveAuthorityDAO;

    @Autowired
    private TaskDAO taskDAO;
    @Autowired
    private PaySlipDAO payslipDao;

    @Autowired
    public RentDAO rentDAO;

    @Autowired
    public IncrementSanctionDAO incrementsancDAO;

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.context = servletContext;
    }

    public static int getYearFromDate(Date date) {
        int result = -1;
        if (date != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            result = cal.get(Calendar.YEAR);
        }
        return result;
    }

    @RequestMapping(value = "/login/{username}/{userpassword}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Object loginps(HttpServletResponse response, @PathVariable("username") String username, @PathVariable("userpassword") String userpassword) {
        JSONObject jobj = new JSONObject(loginDao.checkWSLogin(username, userpassword));
        return jobj.toString();
    }

    @ResponseBody
    @RequestMapping(value = "/employeeProfile/{empid}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Object getEmployeeProfile(HttpServletResponse response, @PathVariable("empid") String empid) {
        JSONObject jobj = null;
        String filePath = context.getInitParameter("PhotoPath");
        Employee employee = employeeDAO.getEmployeeProfile(empid);
        employee.setBase64Image(employeeDAO.getEmployeeImage(empid, filePath));
        jobj = new JSONObject(employee);
        return jobj.toString();
    }

    @ResponseBody
    @RequestMapping(value = "/employeePayslip/{empid}/{year}/{month}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Object getEmployeePayslip(HttpServletResponse response, @PathVariable("empid") String empid, @PathVariable("year") int year, @PathVariable("month") int month) {
        JSONObject jobj = null;
        String aqslno = payslipDao.getAQSLNo(empid, year, month);
        PaySlipDetailBean paySlipDetailBean = payslipDao.getEmployeeData(aqslno, year, month);
        ADDetails[] allowanceArray = payslipDao.getAllowanceDeductionList(aqslno, "A", year, month);
        ADDetails[] deductionArray = payslipDao.getAllowanceDeductionList(aqslno, "D", year, month);
        ADDetails[] pvtdeductionArray = payslipDao.getPrivateDeductionList(aqslno, year, month);

        int totalAllowance = 0;
        for (int i = 0; i < allowanceArray.length; i++) {
            totalAllowance = totalAllowance + allowanceArray[i].getAdAmt();
        }
        int totalDeduction = 0;
        for (int i = 0; i < deductionArray.length; i++) {
            totalDeduction = totalDeduction + deductionArray[i].getAdAmt();
        }
        int totalPvtDeduction = 0;
        for (int i = 0; i < pvtdeductionArray.length; i++) {
            totalPvtDeduction = totalPvtDeduction + pvtdeductionArray[i].getAdAmt();
        }

        paySlipDetailBean.setGrossAmount(Integer.parseInt(paySlipDetailBean.getCurBasic()) + totalAllowance);
        paySlipDetailBean.setNetAmount((Integer.parseInt(paySlipDetailBean.getCurBasic()) + totalAllowance) - (totalDeduction + totalPvtDeduction));
        paySlipDetailBean.setTotallowAmt(totalAllowance);
        paySlipDetailBean.setTotdeductAmt(totalDeduction);
        paySlipDetailBean.setTotpvtdedAmt(totalPvtDeduction);
        paySlipDetailBean.setAllowList(allowanceArray);
        paySlipDetailBean.setDeductList(deductionArray);
        paySlipDetailBean.setPvtdeductList(pvtdeductionArray);
        jobj = new JSONObject(paySlipDetailBean);
        return jobj.toString();
    }

    @RequestMapping(value = "/employeeLeaveList/{empid}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Object getEmployeeLeaveList(HttpServletResponse response, @PathVariable("empid") String empid) {

        JSONObject jobj = new JSONObject();
        JSONArray arr = new JSONArray();
        try {
            List leaveList = leaveApplyDAO.getLeaveApplyList(empid);
            arr = new JSONArray(leaveList);
            jobj.put("leaveList", arr);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return jobj.toString();
    }

    @RequestMapping(value = "/saveemployeeLeave", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_VALUE)
    public Object saveEmployeeLeave(HttpServletResponse response, @RequestBody Leave leave) {
        JSONObject jobj = new JSONObject();
        String statusofleave = "";
        boolean ifexist = false;
        boolean status = false;
        boolean ifMoreThanMaxPeriod = false;
        String leaveType = "";
        try {
            ifexist = leaveApplyDAO.getIfLeaveRecordExist(leave.getHidempId(), leave.getSltleaveType(), leave.getTxtperiodFrom(), leave.getTxtperiodTo());
            ifMoreThanMaxPeriod = leaveApplyDAO.maxPeriodCount(leave);
            leaveType = leaveApplyDAO.getLeaveType(leave.getSltleaveType());
            if (ifexist == false) {
                statusofleave = "Leave already applied for this period";
            } else if (ifMoreThanMaxPeriod == false && !leaveType.equals("HDL")) {
                statusofleave = "Period of leave applied is more than available leave";
            } else {
                status = leaveApplyDAO.saveLeave(leave);
                if (status == true) {
                    /*Call Google Service For Notification In Mobile*/

                    /*Call Google Service For Notification In Mobile*/
                    statusofleave = "Leave applied successfully";
                } else {
                    statusofleave = "Leave is not applied";
                }

            }
            //jobj.append("status", status);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusofleave;
    }

    @RequestMapping(value = "/SelectSanctionAuthorityAction/{empid}", method = {RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_VALUE)
    public Object viewAuthorityList(HttpServletResponse response, @PathVariable("empid") String empid) {

        JSONObject jobj = new JSONObject();
        JSONArray arr = new JSONArray();
        try {
            int year = getYearFromDate(new Date());
            Employee employee = employeeDAO.getEmployeeProfile(empid);
            List leaveAuthoritylist = leaveAuthorityDAO.leaveAuthorityList(employee.getSpc(), employee.getOfficecode(), empid, "1", year + "-" + (year + 1));
            arr = new JSONArray(leaveAuthoritylist);
            jobj.put("leaveAuthList", arr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jobj.toString();

    }

    @RequestMapping(value = "taskListActionJSON/{empid}", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_VALUE)
    public Object getTaskListJSON(HttpServletResponse response, @PathVariable("empid") String empid) {
        JSONObject jobj = new JSONObject();
        JSONArray arr = new JSONArray();
        try {
            Employee employee = employeeDAO.getEmployeeProfile(empid);
            List tasklist = taskDAO.getMyTaskList(empid, employee.getSpc(), null, 0, 0, employee.getEmpName(), employee.getGpfno(), 1);
            arr = new JSONArray(tasklist);
            jobj.put("taskList", arr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jobj.toString();
    }

    @RequestMapping(value = "/getLeaveData/{taskId}/{empid}", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_VALUE)
    public Object getLeaveData(HttpServletResponse response, @PathVariable("empid") String empid, @PathVariable("taskId") String taskId) {
        JSONObject jobj = null;
        try {
            Employee employee = employeeDAO.getEmployeeProfile(empid);
            jobj = new JSONObject(leaveApplyDAO.getLeaveData(taskId, empid, employee.getSpc()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jobj.toString();
    }

    @RequestMapping(value = "/SelectIssuingAuthorityAction/{empid}", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_VALUE)
    public Object viewIssuingAuthorityList(HttpServletResponse response, @PathVariable("empid") String empid) {

        JSONObject jobj = new JSONObject();
        JSONArray arr = new JSONArray();
        try {
            int year = getYearFromDate(new Date());
            Employee employee = employeeDAO.getEmployeeProfile(empid);
            List leaveAuthoritylist = leaveAuthorityDAO.leaveAuthorityList(employee.getSpc(), employee.getOfficecode(), empid, "1", year + "-" + (year + 1));
            arr = new JSONArray(leaveAuthoritylist);
            jobj.put("leaveAuthList", arr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jobj.toString();

    }

    @RequestMapping(value = "/updateLeaveAction", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_VALUE)
    public void updateLeaveAction(HttpServletResponse response, @RequestBody Leave leave) {
        double noOfDays = 0;
        LeaveSancBean lsb = null;
        Calendar cal = Calendar.getInstance();
        int curYear = cal.get(Calendar.YEAR);
        SimpleDateFormat simpleformat = new SimpleDateFormat("MM");
        String strMonth = simpleformat.format(new Date());
        try {
            leaveApplyDAO.updateMobileApproveDate(leave);
            leaveApplyDAO.updateTaskList(leave);
            noOfDays = leaveApplyDAO.calculateDateDiff(leave.getTxtApproveFrom(), leave.getTxtApproveTo(), lsb.getInitiatedEmpId(), leave.getTollid());
            lsb = leaveApplyDAO.getLeaveSancInfo(leave.getHidTaskId());
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

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @RequestMapping(value = "/empprofilephoto/{empid}", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_VALUE)
    public void profilePhoto(HttpServletResponse response, @PathVariable("empid") String empid) throws IOException {
        response.setContentType("image/gif");
        ServletOutputStream os = response.getOutputStream();
        String strSQL = null;
        Blob img;
        byte[] imgData = null;
        int BUFFER_LENGTH = 4096;
        try {
            if (empid != null && !empid.equals("")) {
                String filePath = context.getInitParameter("PhotoPath");
                String filename = empid + ".jpg";
                File file = new File(filePath + filename);
                response.setContentLength((int) file.length());
                if (file.exists()) {
                    FileInputStream is = new FileInputStream(file);
                    os = response.getOutputStream();
                    byte[] bytes = new byte[BUFFER_LENGTH];
                    int read = 0;
                    while ((read = is.read(bytes, 0, BUFFER_LENGTH)) != -1) {
                        os.write(bytes, 0, read);
                    }
                    os.flush();
                    is.close();
                    os.close();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        os.close();
        os.flush();

    }

    @RequestMapping(value = "/saveJoiningData", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_VALUE)
    public void saveJoinData(HttpServletResponse response, @RequestBody Leave leave) {
        try {
            leaveApplyDAO.saveJoinData(leave);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @RequestMapping(value = "/cancelleave/{taskid}", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_VALUE)
    public void cancelLeave(HttpServletResponse response, @PathVariable("taskid") String taskid) {
        try {
            leaveApplyDAO.cancelLeave(taskid);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/saveFCMToken/{fcmtoken}/{linkid}/{serverkey}", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_VALUE)
    public void saveFcmToken(HttpServletResponse response, @PathVariable("fcmtoken") String fcmtoken, @PathVariable("linkid") String linkid, @PathVariable("serverkey") String serverkey) {
        try {
            leaveApplyDAO.saveFcmToken(fcmtoken, linkid, serverkey);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @RequestMapping(value = "/deleteFCMToken/{linkid}", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_VALUE)
    public void deleteFcmToken(HttpServletResponse response, @PathVariable("linkid") String linkid) {
        try {
            leaveApplyDAO.deleteFcmToken(linkid);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @RequestMapping(value = "/getFCMToken/{linkid}", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getFcmToken(HttpServletResponse response, @PathVariable("linkid") String linkid) {
        String fcmToken = "";
        try {
            fcmToken = leaveApplyDAO.getFcmToken(linkid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fcmToken;
    }

    @ResponseBody
    @RequestMapping(value = "/pensionProfileDetails/{empid}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Object getpensionProfileDetails(HttpServletResponse response, @PathVariable("empid") String empid) {
        JSONObject jobj = null;
        //String filePath = context.getInitParameter("PhotoPath");
        PensionProfile employee = employeeDAO.getpensionProfileDetails(empid);
        jobj = new JSONObject(employee);
        return jobj.toString();
    }

    @ResponseBody
    @RequestMapping(value = "/loandetails/{loanid}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Object getLoanDetails(HttpServletResponse response, @PathVariable("loanid") int loanid) {
        JSONObject jobj = null;
        //String filePath = context.getInitParameter("PhotoPath");
        LoanForm lform = loanworkflowDao.getLoanDetails(loanid);
        jobj = new JSONObject(lform);
        return jobj.toString();
    }

    @RequestMapping(value = "/SaveScrollData", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_VALUE)
    public void saveScrollData(HttpServletResponse response, @RequestBody ScrollMain sMain) {

        try {
            rentDAO.saveScrollData(sMain);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ResponseBody
    @RequestMapping(value = "/employeeIncrementList/{empid}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Object getEmployeeIncrementList(HttpServletResponse response, @PathVariable("empid") String empid) {
        JSONObject jobj = new JSONObject();
        try {
            List incrlist = incrementsancDAO.getIncrementList(empid);
            JSONArray arr = new JSONArray(incrlist);
            jobj.put("incrementList", arr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jobj.toString();
    }

}
