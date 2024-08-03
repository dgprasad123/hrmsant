/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.login;

import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.common.SMSServices;
import hrms.dao.changepassword.ChangePasswordDAOImpl;
import hrms.dao.employee.EmployeeDAO;
import hrms.dao.login.LoginDAO;
import hrms.dao.master.TreasuryDAO;
import hrms.dao.master.UserExpertiseDAO;
import hrms.dao.privilege.PrivilegeManagementDAO;
import hrms.dao.task.TaskDAO;
import hrms.model.lamembers.LAMembers;
import hrms.model.login.AdminUsers;
import hrms.model.login.LoginUserBean;
import hrms.model.login.UserDetails;
import hrms.model.login.UserExpertise;
import hrms.model.login.Users;
import hrms.model.master.Module;
import hrms.model.master.Treasury;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;
import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.apache.commons.codec.binary.Hex;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 *
 * @author Surendra
 */
@Controller
@SessionAttributes({"LoginUserBean", "Privileges", "SelectedEmpObj", "SelectedEmpOffice", "TreasuryDtls", "users", "parreviewedspc", "forgotpasswordotpdetails", "loginotpdetails"})
public class LoginController implements ServletContextAware {

    @Autowired
    public LoginDAO getRegisteredMobileno;

    @Autowired
    public LoginDAO loginDao;

    @Autowired
    public UserExpertiseDAO userExpertiseDAO;

    @Autowired
    TreasuryDAO treasuryDao;

    @Autowired
    public EmployeeDAO employeeDAO;

    @Autowired
    public PrivilegeManagementDAO privilegeManagementDAO;

    @Autowired
    public ChangePasswordDAOImpl changepwdDao;

    @Autowired
    public TaskDAO taskDAO;

    private ServletContext context;

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.context = servletContext;
    }

    private static final Logger logger = LogManager.getLogger(LoginController.class);

    @RequestMapping(value = "index", method = RequestMethod.GET)
    public String viewLogin(Map<String, Object> model, Model md, HttpServletRequest request, HttpServletResponse response) {
        Users user = new Users();
        model.put("loginForm", user);
        String msg = (String) md.asMap().get("errorMsg");
        model.put("errorMsg", msg);

        Cookie cookieWithoutSlash = new Cookie("appshrms", null);
        cookieWithoutSlash.setPath(request.getContextPath());
        cookieWithoutSlash.setMaxAge(0);
        response.addCookie(cookieWithoutSlash); //For JBoss                
        return "index";
    }

    @RequestMapping(value = "redirectToLoginPage", method = RequestMethod.GET)
    public void viewLoginSessionOut(Map<String, Object> model, HttpServletResponse response) {
        try {
            PrintWriter out = response.getWriter();
            out.println("<script language=\"JavaScript\" type=\"text/javascript\">");
            out.println("document.location = \"http://google.com\"");
            out.println("</script>");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "login", method = {RequestMethod.GET, RequestMethod.POST})
    public String doLogin(@Valid @ModelAttribute("loginForm") Users loginForm, BindingResult result, ModelMap model, HttpServletRequest request, RedirectAttributes redirectAttributes) throws UnknownHostException, org.apache.commons.codec.DecoderException {
        //ModelAndView mav = new ModelAndView();
        String path = "redirect:/index.htm";
        LoginUserBean lub = new LoginUserBean();
        String empid = "";
        UserDetails ud = null;
        HttpSession session = request.getSession(true);
        InetAddress IP = InetAddress.getLocalHost();
        String curIP = IP.getHostAddress();
        String matchingPassword = null;
        boolean errorOccured = false;
        String encryptedPassword = "";
        String usersalt = "";
        boolean captchavalid = false;
        try {

            String decryptedPassword = loginDao.decryptPassword(loginForm.getUserPassword());
            if (decryptedPassword != null && !decryptedPassword.equals("")) {
                byte[] bytes = Hex.decodeHex(decryptedPassword.toCharArray());
                matchingPassword = new String(bytes, "UTF-8");

                usersalt = changepwdDao.getUserSalt(loginForm.getUserName());
                encryptedPassword = CommonFunctions.getEncryptedPassword(matchingPassword, usersalt);
            } else {
                path = "redirect:/index.htm";
            }
        } catch (Exception io) {
            errorOccured = true;
            io.printStackTrace();
        }
        if (loginForm.getLoginType() != null && loginForm.getLoginType().equals("OTP")) {
            errorOccured = false;
            captchavalid = true;
            if (session.getAttribute("loginotpdetails") != null) {
                UserDetails udetail = (UserDetails) session.getAttribute("loginotpdetails");
                if (udetail.getUsername() != null && !udetail.getUsername().equals("")) {
                    loginForm.setUserName(udetail.getUsername());
                } else if (udetail.getMobile() != null && !udetail.getMobile().equals("")) {
                    loginForm.setUserName(loginDao.getUsernameUsingMobile(udetail.getMobile()));
                }
            }
        }
        if (errorOccured == false) {
            try {
                String captcha = (String) session.getAttribute("CAPTCHA");
                if (captcha != null && captcha.equalsIgnoreCase(loginForm.getCaptcha())) {
                    // if (captcha == null || captcha.equalsIgnoreCase("")) {
                    //String decryptedPassword = loginForm.getUserPassword();
                    //ud = loginDao.checkLogin(loginForm.getUserName(), matchingPassword);
                    captchavalid = true;
                }
                if (captchavalid == true) {
                    if (loginForm.getUserPassword() != null && !loginForm.getUserPassword().equals("")) {
                        ud = loginDao.checkLogin(loginForm.getUserName(), encryptedPassword);
                    } else {
                        ud = loginDao.checkOTPLogin(loginForm.getUserName());
                    }

                    if (ud != null && ud.getLinkid() != null && !ud.getLinkid().equals("") && !ud.getLinkid().equals("F")) {
                        if (ud.getAccountnonlocked() == 0) {
                            //model.addAttribute("errorMsg", "Your Account has been locked");
                            redirectAttributes.addFlashAttribute("errorMsg", "Your Account has been locked");
                            path = "redirect:/index.htm";
                        } else if (ud.getAccountnonexpired() == 0) {
                            //model.addAttribute("errorMsg", "Your Account has been expired");
                            redirectAttributes.addFlashAttribute("errorMsg", "Your Account has been expired");
                            path = "redirect:/index.htm";
                        } else if (ud.getUsertype().equalsIgnoreCase("G")) {
                            Users emp = loginDao.getEmployeeProfileInfo(ud.getLinkid());
                            UserExpertise ue = loginDao.getUserInfo(ud.getLinkid());
                            Date sysDate = new Date();
                            if (emp.getDos() != null && !emp.getDos().equals("")) {
                                long diff = emp.getDos().getTime() - sysDate.getTime();
                                long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                                if (days < 90) {
                                    model.addAttribute("hasNDCLink", "Y");
                                }
                            }
                            lub.setLoginempid(emp.getEmpId());
                            lub.setLoginname(emp.getFullName());
                            lub.setLoginmobile(emp.getMobile());
                            lub.setLogingpfno(emp.getGpfno());
                            emp.setUsertype("G");
                            lub.setLoginEmployeeType(emp.getIsRegular());
                            lub.setLoginPostGrptype(emp.getPostgrp());
                            lub.setLogindeptcode(emp.getDeptcode());
                            lub.setLogindistrictcode(emp.getDistCode());
                            lub.setLoginoffcode(emp.getOffcode());
                            lub.setLoginoffname(emp.getOffname());
                            lub.setLoginspc(emp.getCurspc());
                            lub.setLoginspn(emp.getSpn());
                            lub.setLogingpc(emp.getGpc());
                            lub.setLoginuserid(loginForm.getUserName());
                            lub.setLoginusername(emp.getFullName());
                            lub.setLoginhasofficePriv(emp.getHasPrivilages());
                            lub.setLoginusertype(ud.getUsertype());
                            lub.setIsauthority(emp.getIsauthority());
                            lub.setLoginAsDDO(emp.getIsddoLogin());
                            lub.setLogindeptname(emp.getDeptName());
                            lub.setLoginDDOCode(emp.getDdoCode());
                            lub.setLoginOffLevel(emp.getOfficeLevel());
                            lub.setLoginIsDDOType(emp.getEmpIsDDO());
                            lub.setIfVisited(emp.getIfVisited());
                            lub.setLoginempSpecific(emp.getLoginEmpid());
                            lub.setLoginAsForeignbody(emp.getIsForeignbodyEmp());
                            lub.setLogincadrecode(emp.getCadrecode());
                            if (emp.getHasAERAuthorizationForAdditionalChargeSPC() != null && emp.getHasAERAuthorizationForAdditionalChargeSPC().equals("Y")) {
                                lub.setLoginAdditionalChargeOffCode(emp.getAdditionalChargeOffCode());
                                lub.setLoginAdditionalChargeSpc(emp.getAdditionalChargespc());
                                lub.setLoginAdditionalChargeDDOCode(emp.getAdditionalChargeDDOCode());
                                lub.setLoginadditionalChargeOfficename(emp.getAdditionalChargeOfficename());
                            }
                            model.addAttribute("users", emp);
                            model.addAttribute("LoginUserBean", lub);
                            boolean hasFilled = loginDao.countExpertise(ud.getLinkid());
                            String postCode = emp.getPostCode();
                            boolean isEligible = loginDao.getEligibility(postCode);
                            model.addAttribute("ifProfileCompleted", emp.getIfprofileCompleted());
                            model.addAttribute("distCode", emp.getDistCode());
                            model.addAttribute("isAddlCharge", employeeDAO.getempDDOchargefficeList(ud.getLinkid()));
                            model.addAttribute("isEligible", isEligible);
                            model.addAttribute("displaySBTrack", loginDao.ifEmpisDDO(emp.getEmpId()));
                            model.addAttribute("trainingCadre", emp.getCadrecode());
                            model.addAttribute("userinfo", ue);
                            model.addAttribute("hasFilled", hasFilled);
                            model.addAttribute("curip", curIP);
                            model.addAttribute("forcereset", ud.getForceReset());
                            boolean isFeedbackEligible = loginDao.getFeedbackEligibility(ud.getLinkid());
                            model.addAttribute("feedbackEligible", isFeedbackEligible);

                            System.out.println("lub.setloginasddo:" + lub.getLoginAsDDO() + "::" + employeeDAO.getempDDOchargefficeList(ud.getLinkid()));

                            /**
                             * *********************** View message Details
                             * *************************
                             */
                            String OfficePri = "N";
                            if (lub.getLoginhasofficePriv() != null && !lub.getLoginhasofficePriv().isEmpty()) {
                                OfficePri = "Y";
                            }
                            String totalCnt = employeeDAO.employeeCommunicationCount(OfficePri, lub.getLoginempid(), lub.getLoginoffcode());
                            String[] parts = totalCnt.split("|");
                            String part1 = "0"; // 004
                            String part2 = "0"; // 034556
                            StringTokenizer dtk = new StringTokenizer(totalCnt, "|");
                            while (dtk.hasMoreTokens()) {
                                part1 = dtk.nextToken().trim();
                                part2 = dtk.nextToken().trim();
                            }
                            int TotalCount = Integer.parseInt(part1) + Integer.parseInt(part2);
                            model.addAttribute("totalCnt", TotalCount);
                            model.addAttribute("totalnotview", part1);
                            model.addAttribute("totalview", part2);

                            int totalPAR = taskDAO.getTaskListTotalPARCnt(lub.getLoginempid(), 0);
                            int totalPAR1 = taskDAO.getTaskListTotalPARCnt(lub.getLoginempid(), 6);
                            int totalPAR2 = taskDAO.getTaskListTotalPARCnt(lub.getLoginempid(), 7);
                            int totalPAR3 = taskDAO.getTaskListTotalPARCnt(lub.getLoginempid(), 8);

                            model.addAttribute("totalPAR", totalPAR);
                            model.addAttribute("totalPAR1", totalPAR1);
                            model.addAttribute("totalPAR2", totalPAR2);
                            model.addAttribute("totalPAR3", totalPAR3);

                            model.addAttribute("v1", lub.getLoginempid());
                            model.addAttribute("v2", CommonFunctions.getStringMD5Encoded(loginForm.getUserName()));
                            model.addAttribute("v3", CommonFunctions.getStringMD5Encoded(usersalt));

                            /**
                             * *********************************************************************
                             */
                            path = "/tab/mypage";
                        } else if (ud.getUsertype().equalsIgnoreCase("M")) {
                            LAMembers lam = loginDao.getMiniSterialProfileInfo(ud.getLinkid());
                            lub.setLoginempid(lam.getEmpId() + "");
                            lub.setLoginoffname(lam.getOffName());
                            lub.setLoginusertype(ud.getUsertype());
                            model.addAttribute("users", lam);
                            model.addAttribute("LoginUserBean", lub);
                            path = "/tab/mypage";
                        } else if (ud.getUsertype().equalsIgnoreCase("A")) {

                            AdminUsers adm = loginDao.getAdminUsersProfileInfo(ud.getLinkid());

                            Users emp = loginDao.getEmployeeProfileInfo(ud.getLinkid());
                            Module[] privileges = privilegeManagementDAO.getUserPrivileageList(loginForm.getUserName());
                            UserExpertise ue = loginDao.getUserInfo(ud.getLinkid());
                            model.addAttribute("users", adm);
                            model.addAttribute("Privileges", privileges);
                            lub.setLogindeptcode(adm.getRefcode());
                            lub.setLoginoffname(adm.getOffName());
                            lub.setLoginusername(loginForm.getUserName());
                            lub.setLoginusertype(ud.getUsertype());
                            lub.setLoginspc(emp.getCurspc());
                            lub.setLoginadmintype(emp.getUsertype());
                            lub.setLoginempid(ud.getLinkid());
                            lub.setLoginuserid(adm.getEmpId());
                            lub.setLoginname(adm.getFullName());
                            model.addAttribute("LoginUserBean", lub);
                            boolean hasFilled = loginDao.countExpertise(ud.getLinkid());
                            model.addAttribute("userinfo", ue);
                            model.addAttribute("hasFilled", hasFilled);
                            path = "/tab/hrmsadmin";

                        } else if (ud.getUsertype().equalsIgnoreCase("D")) { /* District Co-ordinator Login */

                            AdminUsers adm = loginDao.getAdminUsersProfileInfo(ud.getLinkid());
                            Module[] privileges = privilegeManagementDAO.getUserPrivileageList(loginForm.getUserName());
                            model.addAttribute("users", adm);
                            model.addAttribute("Privileges", privileges);
                            lub.setLoginuserid(loginForm.getUserName());
                            lub.setLoginempid(adm.getEmpId() + "");
                            lub.setLoginoffname(adm.getOffName());
                            lub.setLoginname(adm.getFullName());
                            lub.setLogindistrictcode(adm.getRefcode());
                            lub.setLoginusertype(ud.getUsertype());
                            Treasury trBean = new Treasury();
                            model.addAttribute("TreasuryDtls", trBean);
                            model.addAttribute("LoginUserBean", lub);
                            path = "/tab/hrmsadmin";

                        } else if (ud.getUsertype().equalsIgnoreCase("F")) { /* Single Pay Bill Controller  */

                            AdminUsers adm = loginDao.getAdminUsersProfileInfo(ud.getLinkid());
                            Treasury trBean = treasuryDao.getTreasuryDetails(adm.getRefcode());

                            model.addAttribute("users", adm);
                            model.addAttribute("TreasuryDtls", trBean);

                            lub.setLoginuserid(loginForm.getUserName());
                            lub.setLoginempid(adm.getEmpId() + "");
                            lub.setLoginoffname(adm.getOffName());
                            lub.setLoginname(adm.getFullName());
                            lub.setLogindistrictcode(adm.getRefcode());
                            lub.setLoginusertype(ud.getUsertype());
                            model.addAttribute("LoginUserBean", lub);

                            path = "/tab/hrmsadmin";

                        } else if (ud.getUsertype().equalsIgnoreCase("I")) {
                            Users lam = loginDao.getInstituteInfo(ud.getLinkid());
                            lub.setLoginempid(lam.getEmpId());
                            lub.setLoginoffname(lam.getFullName());

                            model.addAttribute("users", lam);
                            model.addAttribute("LoginUserBean", lub);
                            path = "/trainingadmin/InstitutionDashboard";

                        } else if (ud.getUsertype().equalsIgnoreCase("T")) {
                            lub.setLoginempid("0");
                            if (loginForm.getUserName().equals("trainingadmin")) {
                                lub.setLoginoffname("Domestic & Foreign Training, (GA & PG DEPT)");
                                model.addAttribute("fullName", "Domestic & Foreign Training, (GA & PG DEPT)");
                                path = "/trainingadmin/administrator/AdminDashboard";
                            } else if (loginForm.getUserName().equals("onlinetraining")) {
                                lub.setLoginoffname("Online Training Management");
                                model.addAttribute("fullName", "Online Training Management");
                                path = "/trainingadmin/onlinetraining/Dashboard";
                                model.addAttribute("userType", "");
                            } else {
                                AdminUsers adm = loginDao.getDeptUsersProfileInfo(ud.getLinkid());
                                model.addAttribute("users", adm);
                                lub.setLoginempid(ud.getLinkid());
                                lub.setLoginname(adm.getFullName());
                                lub.setLoginusertype("Dept");
                                model.addAttribute("userType", "Dept");
                                model.addAttribute("fullName", adm.getFullName());
                                path = "/trainingadmin/onlinetraining/DeptDashboard";
                            }
                            model.addAttribute("LoginUserBean", lub);
                        } else if (ud.getUsertype().equalsIgnoreCase("Q")) {
                            Users lam = loginDao.getEmployeeProfileInfo(ud.getLinkid());
                            lub.setLoginempid(lam.getEmpId());
                            lub.setLoginoffname(lam.getFullName());
                            lub.setLoginspc(lam.getCurspc());
                            lub.setLoginspn(lam.getSpn());
                            lub.setLogingpc(lam.getGpc());
                            lub.setLoginuserid(loginForm.getUserName());
                            lub.setLoginusername(lam.getFullName());
                            lub.setLoginhasofficePriv(lam.getHasPrivilages());
                            model.addAttribute("users", lam);
                            model.addAttribute("LoginUserBean", lub);
                            path = "/Rent/NDCDashboard";

                        } else if (ud.getUsertype().equalsIgnoreCase("P")) {//Deptment mis data entry
                            String commUserName = loginForm.getUserName();
                            if (commUserName.equals("opsc.gad")) {
                                AdminUsers adm = loginDao.getDeptUsersProfileInfo(ud.getLinkid());
                                model.addAttribute("users", adm);
                                lub.setLoginempid(ud.getLinkid());
                                lub.setLoginoffname("Odisha Public Service Commission");
                                lub.setLoginname("Odisha Public Service Commission");
                                lub.setLoginuserid("2468592");
                                model.addAttribute("LoginUserBean", lub);

                                path = "/tab/commissionadmin";
                            } else if (commUserName.equals("ossc.gad")) {
                                AdminUsers adm = loginDao.getDeptUsersProfileInfo(ud.getLinkid());
                                model.addAttribute("users", adm);
                                lub.setLoginempid(ud.getLinkid());
                                lub.setLoginoffname("Odisha Staff Selection Commission");
                                lub.setLoginname("Odisha Staff Selection Commission");
                                lub.setLoginuserid("2468591");
                                model.addAttribute("LoginUserBean", lub);

                                path = "/tab/commissionadmin";
                            } else if (commUserName.equals("osssc.gad")) {
                                AdminUsers adm = loginDao.getDeptUsersProfileInfo(ud.getLinkid());
                                model.addAttribute("users", adm);
                                lub.setLoginempid(ud.getLinkid());
                                lub.setLoginoffname("Odisha Sub-ordinate Staff Selection Commission");
                                lub.setLoginname("Odisha Sub-ordinate Staff Selection Commission");
                                lub.setLoginuserid("2468593");
                                model.addAttribute("LoginUserBean", lub);

                                path = "/tab/commissionadmin";
                            } else {
                                AdminUsers adm = loginDao.getDeptUsersProfileInfo(ud.getLinkid());
                                model.addAttribute("users", adm);
                                lub.setLoginempid(ud.getLinkid());
                                lub.setLoginoffname(adm.getOffName());
                                lub.setLoginname(adm.getFullName());
                                lub.setLoginusertype("Dept");
                                model.addAttribute("LoginUserBean", lub);

                                if (ud.getLinkid().equals("00")) {
                                    path = "/tab/deptmis";
                                } else {
                                    path = "/tab/deptadmin";
                                }
                            }
                        } else if (ud.getUsertype().equalsIgnoreCase("S")) {
                            AdminUsers adm = loginDao.getAdminUsersProfileInfo(ud.getLinkid());
                            model.addAttribute("users", adm);
                            lub.setLoginusertype(ud.getUsertype());
                            lub.setLoginusername(loginForm.getUserName());
                            lub.setLoginuserid(loginForm.getUserName());
                            lub.setLoginempid(adm.getEmpId() + "");
                            lub.setLoginoffname(adm.getOffName());
                            lub.setLoginname(adm.getFullName());
                            /*ADDED BY MANAS SIR FOR ALPHA ODISHA*/
                            Module[] privileges = privilegeManagementDAO.getUserPrivileageList(loginForm.getUserName());
                            model.addAttribute("Privileges", privileges);
                            /*--------------END CODE--------------------------*/
                            /**
                             * *********************** View message Details
                             * *************************
                             */
                            String OfficePri = "N";
                            if (lub.getLoginhasofficePriv() != null && !lub.getLoginhasofficePriv().isEmpty()) {
                                OfficePri = "Y";
                            }
                            String totalCnt = employeeDAO.employeeCommunicationCount(OfficePri, lub.getLoginempid(), lub.getLoginoffcode());
                            String[] parts = totalCnt.split("|");
                            String part1 = "0"; // 004
                            String part2 = "0"; // 034556
                            StringTokenizer dtk = new StringTokenizer(totalCnt, "|");
                            while (dtk.hasMoreTokens()) {
                                part1 = dtk.nextToken().trim();
                                part2 = dtk.nextToken().trim();
                            }
                            int TotalCount = Integer.parseInt(part1) + Integer.parseInt(part2);
                            model.addAttribute("totalCnt", TotalCount);
                            model.addAttribute("totalnotview", part1);
                            model.addAttribute("totalview", part2);

                            /**
                             * *********************************************************************
                             */
                            model.addAttribute("LoginUserBean", lub);
                            path = "/tab/hrmsadmin";

                        } else if (ud.getUsertype().equalsIgnoreCase("H")) { /* Police offices Login Interface */

                            AdminUsers adm = loginDao.getPoliceOfficesLoginDetails(ud.getLinkid());
                            Module[] privileges = privilegeManagementDAO.getUserPrivileageList(loginForm.getUserName());
                            model.addAttribute("users", adm);
                            model.addAttribute("Privileges", privileges);
                            lub.setLoginuserid(loginForm.getUserName());
                            lub.setLoginempid(adm.getEmpId() + "");
                            lub.setLoginoffname(adm.getFullName());
                            lub.setLoginname(adm.getFullName());
                            lub.setLoginoffcode(adm.getRefcode());
                            lub.setLoginusertype(ud.getUsertype());

                            model.addAttribute("LoginUserBean", lub);
                            path = "/tab/hrmsadmin";

                        } else if (ud.getUsertype().equalsIgnoreCase("B")) { /* FOR SI PAR CUSTODIAN Login Interface */

                            AdminUsers adm = loginDao.getPoliceOfficesLoginDetails(ud.getLinkid());
                            Module[] privileges = privilegeManagementDAO.getUserPrivileageList(loginForm.getUserName());
                            model.addAttribute("users", adm);
                            model.addAttribute("Privileges", privileges);
                            lub.setLoginuserid(loginForm.getUserName());
                            lub.setLoginempid(adm.getEmpId() + "");
                            lub.setLoginoffname(adm.getFullName());
                            lub.setLoginname(adm.getFullName());
                            lub.setLoginoffcode(adm.getRefcode());
                            lub.setLoginusertype(ud.getUsertype());
                            lub.setLogindistrictcode(adm.getDistCode());

                            model.addAttribute("LoginUserBean", lub);
                            path = "/tab/hrmsadmin";

                        } else {
                            path = "redirect:/index.htm";
                        }

                        String logIP = CommonFunctions.getISPIPAddress();
                        loginDao.saveLoginLog(ud.getLinkid(), loginForm.getUserName(), logIP, "LOGIN", lub.getLoginoffcode(),null,lub.getLoginusertype(),null);

                    } else {
                        if (ud.getMessage() != null) {
                            //model.addAttribute("errorMsg", ud.getMessage());
                            redirectAttributes.addFlashAttribute("errorMsg", ud.getMessage());
                        } else {
                            //model.addAttribute("errorMsg", "Invalid User or Password");
                            redirectAttributes.addFlashAttribute("errorMsg", "Invalid User or Password");
                        }
                        path = "redirect:/index.htm";
                    }
                } else {
                    loginForm.setCaptcha("");

                    //model.addAttribute("errorMsg", "Security Code does not match");
                    redirectAttributes.addFlashAttribute("errorMsg", "Security Code does not match");

                    path = "redirect:/index.htm";
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {

            }
        }
        //mav.setViewName(path);
        return path;
    }

    @RequestMapping("/logout")
    public String logout(HttpSession session, HttpServletRequest request, HttpServletResponse response) {
        Cookie cookieWithoutSlash = new Cookie("appshrms", null);
        cookieWithoutSlash.setPath(request.getContextPath());
        cookieWithoutSlash.setMaxAge(0);
        response.addCookie(cookieWithoutSlash); //For JBoss
        if (session != null) {
            session.invalidate();
        }
        return "index";
    }

    @RequestMapping("/error404")
    public ModelAndView error(HttpSession session, HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("errorpage/error404");
        return mav;
    }

    @ResponseBody
    @RequestMapping(value = "ForgotPassword", method = RequestMethod.POST)
    public void ForgotPassword(Model model, HttpServletResponse response, @RequestParam Map<String, String> requestParams) {

        response.setContentType("text/html");
        String status = "";
        PrintWriter out = null;

        try {
            UserDetails udetail = loginDao.requestPassword(requestParams);
            status = udetail.getMessage();
            if (status != null && status.equals("1")) {
                String otp = loginDao.generateOTP(requestParams.get("mobile"));
                udetail.setOtp(otp);
                udetail.setMobile(requestParams.get("mobile"));
                udetail.setMessage(null);
                System.out.println("Generated OTP is: " + otp);
                model.addAttribute("forgotpasswordotpdetails", udetail);
                String otpsms = "Use OTP " + otp + " to reset your password. HRMS Odisha";

                if (loginDao.getSMSGatewayStatus() == false) {
                    new SMSServices(requestParams.get("mobile"), otpsms, "1407166565935517641");
                } else {
                    String send = "CMGHRM";
                    String message = "Use OTP " + otp + " to reset your password. HRMS Odisha";
                    String username = "20230049";
                    String password = "D9JrBBlH";

                    /* String requestUrl = "http://164.52.195.161/API/SendMsg.aspx?"
                     + "uname=" + URLEncoder.encode(username, "UTF-8")
                     + "&pass=" + URLEncoder.encode(password, "UTF-8")
                     + "&dest=" + URLEncoder.encode(requestParams.get("mobile"), "UTF-8")
                     + "&msg=" + URLEncoder.encode(message, "UTF-8")
                     + "&send=" + URLEncoder.encode(send, "UTF-8")
                     + "";

                     URL url = new URL(requestUrl);
                     HttpURLConnection uc = (HttpURLConnection) url.openConnection();

                     uc.disconnect();*/
                    String data = ""
                            + "uname=" + URLEncoder.encode(username, "UTF-8")
                            + "&pass=" + URLEncoder.encode(password, "UTF-8")
                            + "&dest=" + URLEncoder.encode(requestParams.get("mobile"), "UTF-8")
                            + "&msg=" + URLEncoder.encode(message, "UTF-8")
                            + "&send=" + URLEncoder.encode(send, "UTF-8")
                            + "";
                    URL url = new URL("http://164.52.195.161/API/SendMsg.aspx?" + data);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    conn.setUseCaches(false);
                    conn.connect();
                    BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String line;
                    StringBuffer buffer = new StringBuffer();
                    while ((line = rd.readLine()) != null) {
                        buffer.append(line).append("\n");
                    }
                    System.out.println(buffer.toString());
                    rd.close();
                    conn.disconnect();
                }
            }
            out = response.getWriter();
            out.write(status);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    @RequestMapping(value = "reviewLogin.htm")
    public ModelAndView reviewLogin(@ModelAttribute("loginForm") Users loginForm) {
        ModelAndView mv = new ModelAndView("/par/ReviewLogin");

        return mv;
    }

    @RequestMapping(value = "validateReviewLogin", method = {RequestMethod.POST})
    public ModelAndView validateReviewLogin(@Valid @ModelAttribute("loginForm") Users loginForm, HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView();
        try {
            String usersalt = changepwdDao.getUserSalt(loginForm.getUserName());
            String encryptedPassword = CommonFunctions.getEncryptedPassword(loginForm.getUserPassword(), usersalt);
            UserDetails ud = loginDao.checkLogin(loginForm.getUserName(), encryptedPassword);
            if (ud != null && ud.getLinkid() != null && !ud.getLinkid().equals("") && !ud.getLinkid().equals("F")) {
                if (ud.getUsertype().equalsIgnoreCase("G")) {
                    Users emp = loginDao.getEmployeeProfileInfo(ud.getLinkid());

                    mav.addObject("parreviewedspc", emp.getCurspc());
                    mav.setViewName("redirect:/parReviewingView.htm");
                } else {
                    mav.setViewName("redirect:/reviewLogin.htm");
                }
            } else {
                mav.setViewName("redirect:/reviewLogin.htm");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @ResponseBody
    @RequestMapping(value = "ValidateForgotPasswordOTP")
    //public void ValidateForgotPasswordOTP(HttpServletResponse response, @ModelAttribute(value = "forgotpasswordotp") String forgotpasswordotp,@RequestParam Map<String, String> requestParams) {
    public void ValidateForgotPasswordOTP(HttpServletResponse response, @ModelAttribute("forgotpasswordotpdetails") UserDetails udetail, @RequestParam Map<String, String> requestParams) {

        response.setContentType("text/html");
        int status = 0;
        PrintWriter out = null;

        try {
            String otp = udetail.getOtp();
            if (otp.equals(requestParams.get("enteredotp"))) {
                status = 0;
            } else {
                status = -1;
            }

            String username = loginDao.getUserName(udetail.getLinkid(), udetail.getMobile());

            out = response.getWriter();
            out.write(status + "@" + username);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    @ResponseBody
    @RequestMapping(value = "ResetForgotPassword")
    public void ResetForgotPassword(HttpServletResponse response, @ModelAttribute("forgotpasswordotpdetails") UserDetails udetail, @RequestParam Map<String, String> requestParams) {

        response.setContentType("text/html");
        int status = 0;
        PrintWriter out = null;

        try {
            String linkid = udetail.getLinkid();
            String mobile = udetail.getMobile();
            String newpassword = requestParams.get("newpassword");
            String confirmpassword = requestParams.get("confirmpassword");

            status = loginDao.resetForgotPassword(linkid, newpassword, confirmpassword, mobile);

            if (status == 1) {
                udetail.setLinkid(null);
                udetail.setOtp(null);
                udetail.setMobile(null);
                udetail.setMessage(null);
                udetail = null;
            }

            out = response.getWriter();
            out.write(status + "");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    private boolean getSMSGatewayStatus(Connection con) {

        PreparedStatement pst = null;
        ResultSet rs = null;

        boolean secondSMS = false;
        try {
            String sql = "SELECT * FROM HRMS_CONFIG WHERE GLOBAL_VAR_NAME='SWITCH_SECOND_SMS'";
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                if (rs.getString("global_var_value") != null && rs.getString("global_var_value").equals("Y")) {
                    secondSMS = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
        }
        return secondSMS;
    }

    @ResponseBody
    @RequestMapping(value = "SendLoginOTP")

    public void SendLoginOTP(HttpServletResponse response, Model model, @RequestParam("userName") String userName, @RequestParam("mobile") String mobile) {

        response.setContentType("text/html");

        PrintWriter out = null;

        String registeredmobile = "";
        String output = "";
        try {
            UserDetails udetail = new UserDetails();

            if (mobile != null && !mobile.equals("")) {
                boolean mobileexist = loginDao.checkMobileno(mobile);
                if (mobileexist == true) {
                    udetail.setMobile(mobile);
                    output = mobile;
                } else if (mobileexist == false) {
                    output = "MI";
                }
            } else if (userName != null && !userName.equals("")) {
                boolean usernameexist = loginDao.checkUsername(userName);
                if (usernameexist == true) {
                    registeredmobile = loginDao.getRegisteredMobileno(userName);
                    udetail.setUsername(userName);
                    udetail.setMobile(registeredmobile);
                    output = registeredmobile;
                } else if (usernameexist == false) {
                    output = "UI";
                }
            }
            String otp = loginDao.generateOTP(registeredmobile);
            //String otp = "12345";
            udetail.setOtp(otp);
            model.addAttribute("loginotpdetails", udetail);
            System.out.println(otp);

            if (loginDao.getSMSGatewayStatus() == false) {
                String otpsms = "Verification code:" + otp;

                new SMSServices(udetail.getMobile(), otpsms, "1407161708585784081");
            } else {
                String send = "CMGHRM";
                String message = "Use OTP " + otp + " to Login. HRMS Odisha";
                String username = "20230049";
                String password = "D9JrBBlH";

                String data = ""
                        + "uname=" + URLEncoder.encode(username, "UTF-8")
                        + "&pass=" + URLEncoder.encode(password, "UTF-8")
                        + "&dest=" + URLEncoder.encode(registeredmobile, "UTF-8")
                        + "&msg=" + URLEncoder.encode(message, "UTF-8")
                        + "&send=" + URLEncoder.encode(send, "UTF-8")
                        + "";
                URL url = new URL("http://164.52.195.161/API/SendMsg.aspx?" + data);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setUseCaches(false);
                conn.connect();
                BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;
                StringBuffer buffer = new StringBuffer();
                while ((line = rd.readLine()) != null) {
                    buffer.append(line).append("\n");
                }
                System.out.println(buffer.toString());
                rd.close();
                conn.disconnect();
            }
            out = response.getWriter();
            out.write(output);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }

    }

    @ResponseBody
    @RequestMapping(value = "ValidateLoginOTP")
    public void ValidateLoginOTP(HttpServletResponse response, @ModelAttribute("loginotpdetails") UserDetails udetail, @RequestParam("enteredloginotp") String enteredloginotp) {
        response.setContentType("text/html");
        PrintWriter out = null;
        try {
            System.out.println("enteredloginotp is: " + enteredloginotp);
            String status = loginDao.validateLoginOTP(enteredloginotp, udetail.getOtp());
            out = response.getWriter();
            out.write(status);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("LoginUsingOTP")
    public ModelAndView LoginUsingOTP(@RequestParam("type") String type) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/wrrreport/LoginOTP");
        return mav;
    }

}
