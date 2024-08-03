/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.login;

import hrms.dao.createEmployee.CreateEmployeeDAO;
import hrms.dao.employee.EmployeeDAO;
import hrms.dao.login.LoginDAO;
import hrms.dao.master.BankDAO;
import hrms.dao.master.BlockDAO;
import hrms.dao.master.BloodGroupDAO;
import hrms.dao.master.BranchDAO;
import hrms.dao.master.CategoryDAO;
import hrms.dao.master.DepartmentDAO;
import hrms.dao.master.DistrictDAO;
import hrms.dao.master.GisTypeDAO;
import hrms.dao.master.MaritalStatusDAO;
import hrms.dao.master.OfficeDAO;
import hrms.dao.master.PoliceStationDAO;
import hrms.dao.master.PostOfficeDAO;
import hrms.dao.master.ReligionDAO;
import hrms.dao.master.StateDAO;
import hrms.dao.master.TreasuryDAO;
import hrms.dao.master.VillageDAO;
import hrms.model.empinfo.EmployeeMessage;
import hrms.model.empinfo.EmployeeSearchResult;
import hrms.model.empinfo.SearchEmployee;
import hrms.model.employee.Address;
import hrms.model.employee.Employee;
import hrms.model.employee.EmployeePayProfile;
import hrms.model.employee.IdentityInfo;
import hrms.model.login.AdminUsers;
import hrms.model.login.LoginUserBean;
import hrms.model.login.Users;
import hrms.model.master.Office;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Manas
 */
@Controller
@SessionAttributes({"LoginUserBean", "SelectedEmpObj", "SelectedEmpOffice"})
public class EmployeeController {

    @Autowired
    EmployeeDAO employeeDAO;

    @Autowired
    LoginDAO loginDao;

    @Autowired
    DepartmentDAO departmentDao;

    @Autowired
    OfficeDAO officeDao;

    @Autowired
    TreasuryDAO treasuryDao;
    @Autowired
    CreateEmployeeDAO createEmployeeDao;

    @Autowired
    public BankDAO bankDAO;
    @Autowired
    public BranchDAO branchDAO;

    @Autowired
    public MaritalStatusDAO maritalStatusDAO;

    @Autowired
    public CategoryDAO categoryDAO;

    @Autowired
    public ReligionDAO religionDAO;

    @Autowired
    public BloodGroupDAO bloodGroupDAO;

    @Autowired
    public StateDAO stateDAOImpl;

    @Autowired
    public PostOfficeDAO postOfficeDAO;

    @Autowired
    public VillageDAO villageDAO;

    @Autowired
    public DistrictDAO districtDAO;

    @Autowired
    public BlockDAO blockDAO;

    @Autowired
    public PoliceStationDAO policeStationDAO;

    @Autowired
    public GisTypeDAO gisTypeDAO;

    @ResponseBody
    @RequestMapping(value = "getEmployeePayProfile", method = RequestMethod.GET)
    public void getEmployeeWiseADList(HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = null;
        EmployeePayProfile empPayProfile = employeeDAO.getEmployeePayProfile(lub.getLoginempid());
        JSONObject jsonObject = new JSONObject(empPayProfile);
        out = response.getWriter();
        out.write(jsonObject.toString());
    }

    @ResponseBody
    @RequestMapping(value = "getOfficeEmployeeList")
    public void getOfficeEmployeeList(HttpServletRequest request, @RequestParam("offcode") String offcode, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = null;
        ArrayList empList = employeeDAO.getOfficeWiseEmployeeList(offcode);
        System.out.println(empList.size());
        JSONArray jsonObject = new JSONArray(empList);
        out = response.getWriter();
        out.write(jsonObject.toString());
    }

    @RequestMapping(value = "LocateEmployee")
    public ModelAndView locateEmployee(@Valid @ModelAttribute("searchEmployee") SearchEmployee empinfo, ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub) {
        ModelAndView mv = new ModelAndView();
        String path = "under_const";
        String employeecode = empinfo.getEmpid();
        String offcode = null;
        if (lub.getLoginusertype().equalsIgnoreCase("A") || lub.getLoginusertype().equalsIgnoreCase("D") || lub.getLoginuserid().contains("dc") || lub.getLoginusertype().equalsIgnoreCase("S") || lub.getLoginuserid().contains("deo")) {
            empinfo.setPage(1);
            empinfo.setRows(10);
            //empinfo.setDistcode(offcode);
            path = "empinfo/SearchEmployeeDC";
            empinfo.setUsertype(lub.getLoginusertype());
            List conJob = employeeDAO.getContractualJobCategory();
            mv.setViewName(path);
            mv.addObject("searchEmployee", empinfo);
            mv.addObject("departmentList", departmentDao.getDepartmentList());
            mv.addObject("officeList", officeDao.getTotalOfficeList(empinfo.getDeptName()));
            if ((empinfo.getOffcode() != null && !empinfo.getOffcode().equals("")) || (empinfo.getCriteria() != null && !empinfo.getCriteria().equals(""))) {
                mv.addObject("empSearchResult", employeeDAO.LocateEmployee(empinfo));
            }
            Office office = officeDao.getOfficeDetails(empinfo.getOffcode());
            String officeDistCode = office.getDistCode();
            mv.addObject("conJob", conJob);
            mv.addObject("loginDistcode", lub.getLogindistrictcode());
            if (officeDistCode != null && officeDistCode.equals(lub.getLogindistrictcode())) {
                mv.addObject("matchDistCode", "Y");
                System.out.println("Yes");
            } else {
                mv.addObject("matchDistCode", "N");
                System.out.println("No");
            }

            //mv.addObject("usertype", usertype);
            /*if (lub.getLoginuserid() != null && lub.getLoginuserid().contains("deo")) {
             mv.addObject("logintype", "DEO");
             }*/
            if (lub.getLoginuserid() != null && !lub.getLoginuserid().equals("")) {
                mv.addObject("username", lub.getLoginuserid());
            }
        } else {
            path = "under_const";
            mv.setViewName(path);
        }
        return mv;
    }

    @RequestMapping(value = "SearchEmployeeInfo")
    public ModelAndView SearchEmployeeInfo(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("searchEmployee") SearchEmployee empinfo) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("empinfo/SearchEmployee");
        mv.addObject("searchEmployee", empinfo);
        mv.addObject("departmentList", departmentDao.getDepartmentList());
        mv.addObject("treasuryList", treasuryDao.getAGTreasuryList());
        return mv;
    }

    @ResponseBody
    @RequestMapping(value = "SearchEmployeeInfoJson")
    public void SearchEmployeeInfoJson(@Valid @ModelAttribute("searchEmployee") SearchEmployee empinfo, HttpServletResponse response, ModelMap model) throws IOException, JSONException {
        response.setContentType("application/json");
        EmployeeSearchResult empSearchResult = new EmployeeSearchResult();
        if (empinfo.getYear() == 0) {
            empSearchResult = employeeDAO.SearchEmployee(empinfo);
        } else {
            empSearchResult = employeeDAO.SearchEmployeeYearWise(empinfo);
        }
        JSONObject obj = new JSONObject(empSearchResult);
        PrintWriter out = response.getWriter();
        out.write(obj.toString());
        out.flush();
    }

    @RequestMapping(value = "SearchEmployeeAGInfo")
    public ModelAndView SearchEmployeeAGInfo(@Valid @ModelAttribute("searchEmployee") SearchEmployee empinfo, ModelMap model) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("empinfo/SearchEmployeeAG");
        mv.addObject("searchEmployee", empinfo);
        mv.addObject("departmentList", departmentDao.getDepartmentList());
        mv.addObject("treasuryList", treasuryDao.getAGTreasuryList());
        return mv;
    }

    @ResponseBody
    @RequestMapping(value = "SearchEmployeeAGInfoJson")
    public void SearchEmployeeAGInfoJson(@Valid @ModelAttribute("searchEmployee") SearchEmployee empinfo, HttpServletResponse response, ModelMap model) throws IOException, JSONException {
        response.setContentType("application/json");
        EmployeeSearchResult empSearchResult = employeeDAO.SearchEmployeeAG(empinfo);
        JSONObject obj = new JSONObject(empSearchResult);
        PrintWriter out = response.getWriter();
        out.write(obj.toString());
        out.flush();
    }

    @RequestMapping(value = "EmployeeBasicProfile")
    public ModelAndView EmployeeBasicProfile(@RequestParam("empid") String empid) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("empinfo/EmployeeBasicProfile");
        mv.addObject("EmployeeProfile", employeeDAO.getEmployeeProfile(empid));
        return mv;
    }

    @RequestMapping(value = "EmployeemessageAttachment")
    public ModelAndView EmployeemessageAttachment(@ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("messageId") int messageId) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("empinfo/messageAttachment");
        mv.addObject("messageattachments", employeeDAO.getMessageAttachment(messageId, "MESSAGE"));
        return mv;
    }

    @RequestMapping(value = "newMessage")
    public ModelAndView newMessage(@ModelAttribute("employeeMessage") EmployeeMessage employeeMessage, @ModelAttribute("LoginUserBean") LoginUserBean lub) {
        ModelAndView mv = new ModelAndView();
        employeeMessage.setReceiverUserType("G");
        Employee emp = employeeDAO.getEmployeeProfile(employeeMessage.getEmpid());
        if (emp.getCurDesg() != null && !emp.getCurDesg().equals("")) {
            employeeMessage.setRecieverSpn(emp.getFullname() + ", " + emp.getCurDesg());
        } else {
            employeeMessage.setRecieverSpn(emp.getFullname());
        }
        employeeMessage.setRecieverSpc(emp.getSpc());
        mv.addObject("employeeMessage", employeeMessage);
        mv.setViewName("empinfo/messageDetail");

        return mv;
    }

    /* 
     */
    @RequestMapping(value = "saveEmployeeMessage", method = {RequestMethod.POST}, params = {"action=Submit"})
    public ModelAndView saveEmployeeMessage(@ModelAttribute("employeeMessage") EmployeeMessage employeeMessage, @ModelAttribute("LoginUserBean") LoginUserBean lub) throws SQLException {
        ModelAndView mv = new ModelAndView();
        employeeMessage.setSenderid(lub.getLoginempid());
        employeeMessage.setSenderSpc(lub.getLoginspc());
        employeeMessage.setSenderUserType(lub.getLoginusertype());
        int messageId = employeeDAO.saveEmployeeMessage(employeeMessage);
        List<MultipartFile> files = employeeMessage.getUploadedFile();

        if (null != files && files.size() > 0) {
            employeeDAO.uploadMultipleAttachedFile(messageId, "MESSAGE", employeeMessage.getUploadedFile());
        }
        mv = new ModelAndView("redirect:/getSentMessageList.htm");
        return mv;
    }

    @RequestMapping(value = "saveEmployeeMessage", method = {RequestMethod.POST}, params = {"action=Back"})
    public ModelAndView BacksaveEmployeeMessage(@ModelAttribute("employeeMessage") EmployeeMessage employeeMessage, @ModelAttribute("LoginUserBean") LoginUserBean lub) throws SQLException {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("redirect:SearchEmployeeAGInfo.htm");
        return mv;
    }

    @RequestMapping(value = "downloadEmployeeAttachment")
    public void downloadEmployeeAttachment(@ModelAttribute("employeeMessage") EmployeeMessage employeeMessage, HttpServletResponse response) throws IOException {
        ModelAndView mv = new ModelAndView();
        employeeMessage = employeeDAO.getAttachedFile(employeeMessage.getAttachmentid());
        response.setContentType(employeeMessage.getFiletype());
        response.setHeader("Content-Disposition", "attachment;filename=" + employeeMessage.getAttachementname());
        OutputStream out = response.getOutputStream();
        out.write(employeeMessage.getFilecontent());
        out.flush();
        out.close();
    }

    @RequestMapping(value = "SentMessageDetailPDF.htm", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView parPDF(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("messageId") int messageId) {
        ModelAndView mv = new ModelAndView();
        String OfficePri = "N";
        if (lub.getLoginhasofficePriv() != null && !lub.getLoginhasofficePriv().isEmpty()) {
            OfficePri = "Y";
        }
        EmployeeMessage employeeMessage = employeeDAO.downloadCommunicationMessage(messageId);
        System.out.println("messageId" + messageId);
        //ArrayList employeeMessageList = employeeDAO.downloadCommunicationMessage(employeeMessage.getMessageId());
        mv.addObject("employeeMessage", employeeMessage);
        mv.setViewName("SentMessageDetailPDF");
        return mv;
    }

    @RequestMapping(value = "getSentMessageList")
    public ModelAndView SentMessage(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("adminUsers") AdminUsers adminUsers) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("empinfo/SentMessage");
        int pgno = 0;
        int totrectcount = 0;
        String searchBox = "";
        if (searchBox != null && !searchBox.equals("")) {
            totrectcount = employeeDAO.getSentMessageListcount(searchBox);
        } else {
            totrectcount = employeeDAO.getSentMessageListcount(lub.getLoginempid());
        }

        if (adminUsers.getPageno() != 0) {
            pgno = adminUsers.getPageno();
        }
        if (adminUsers.getSearchBox() != null && !adminUsers.getSearchBox().equals("")) {
            searchBox = adminUsers.getSearchBox();
        }
        int pageSize = 15;
        int totalPage = totrectcount / pageSize;
        if ((totrectcount % pageSize) > 0) {
            totalPage++;
        }
        if (totalPage == 0) {
            totalPage = 1;
        }
        List audituser = loginDao.getLocalFundAuditUsers(adminUsers);
        mv.addObject("totalPage", totalPage);
        if (searchBox != null && !searchBox.equals("")) {
            mv.addObject("employeemessagelist", employeeDAO.getSentMessageList(searchBox, pgno));
        } else {
            mv.addObject("employeemessagelist", employeeDAO.getSentMessageList(lub.getLoginempid(), pgno));
        }
        mv.addObject("audituser", audituser);
        return mv;
    }

    @RequestMapping(value = "getSentMessageList", params = "action=Search")
    public ModelAndView SentMessageSearch(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("adminUsers") AdminUsers adminUsers) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("empinfo/SentMessage");
        int pgno = 0;
        String searchBox = "";
        int totrectcount = employeeDAO.getSentMessageListcount(searchBox);
        if (adminUsers.getPageno() != 0) {
            pgno = adminUsers.getPageno();
        }
        if (adminUsers.getSearchBox() != null && !adminUsers.getSearchBox().equals("")) {
            searchBox = adminUsers.getSearchBox();
        }
        int pageSize = 15;
        int totalPage = totrectcount / pageSize;
        if ((totrectcount % pageSize) > 0) {
            totalPage++;
        }
        if (totalPage == 0) {
            totalPage = 1;
        }
        List audituser = loginDao.getLocalFundAuditUsers(adminUsers);
        mv.addObject("totalPage", totalPage);
        mv.addObject("employeemessagelist", employeeDAO.getSentMessageList(searchBox, pgno));
        mv.addObject("audituser", audituser);
        return mv;
    }

    @RequestMapping(value = "viewMessageCommunicationDtls")
    public ModelAndView viewMessageCommunicationDtls(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("employeeMessage") EmployeeMessage employeeMessage) {
        ModelAndView mv = new ModelAndView();
        String OfficePri = "N";
        if (lub.getLoginhasofficePriv() != null && !lub.getLoginhasofficePriv().isEmpty()) {
            OfficePri = "Y";
        }
        employeeMessage = employeeDAO.getParentMessageDetail(employeeMessage.getMessageId());
        mv.addObject("employeeMessage", employeeMessage);
        mv.addObject("employeemessagelist", employeeDAO.viewCommunicationMessage(OfficePri, lub.getLoginempid(), lub.getLoginoffcode(), employeeMessage.getMessageId()));
        mv.setViewName("empinfo/viewMessageCommunicationDtls");
        return mv;
    }

    @RequestMapping(value = "ResetEmpoyeePassword_1new")
    public ModelAndView ResetEmpoyeePassword(@RequestParam("empid") String empid, @ModelAttribute("LoginUserBean") LoginUserBean lub) {
        ModelAndView mv = new ModelAndView();
        boolean employeedistrict = employeeDAO.verifyEmployeeDistrict(lub.getLogindistrictcode(), empid);
        if (lub.getLoginusertype().equalsIgnoreCase("A") || lub.getLoginusertype().equalsIgnoreCase("S")) {
            employeedistrict = true;
        }
        if (employeedistrict == true) {
            mv.setViewName("empinfo/ResetEmpoyeePassword");
            String dcLoginId = lub.getLoginuserid();

            //mv.addObject("ResetEmpoyeePassword", employeeDAO.ResetEmpoyeePassword(empid, dcLoginId));
            mv.addObject("ResetEmpoyeePassword", employeeDAO.ResetEmployeeEncodedPassword(empid, dcLoginId));
        } else {
            mv.setViewName("under_const");
        }
        return mv;
    }

    @RequestMapping(value = "EditEmpoyeeData")
    public ModelAndView EditEmpoyeeData(@RequestParam("empid") String empid,ModelMap model, @RequestParam Map<String, String> requestParams,HttpServletRequest request, HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub) {
        ModelAndView mv = new ModelAndView();

        boolean sameDistrictEmployee = employeeDAO.verifyEmployeeDistrict(lub.getLogindistrictcode(), empid);
        if (lub.getLoginuserid() != null && (lub.getLoginuserid().contains("deo") || lub.getLoginuserid().contains("dc") && sameDistrictEmployee == true) || lub.getLoginusertype().equals("A") || lub.getLoginusertype().equals("S")) {

            String idinfo = employeeDAO.getAadhaarAndPanData(empid);
            String aadhaarno = "";
            String panno = "";
            if (idinfo != null && !idinfo.equals("")) {
                String strarr[] = idinfo.split("@");
                aadhaarno = strarr[0];
                if (strarr.length > 1) {
                    panno = strarr[1];
                }
            }

            Employee emp = employeeDAO.EditEmpoyeeData(empid);
            emp.setAadhaarno(aadhaarno);
            emp.setPanno(panno);
            mv.setViewName("empinfo/EditEmpoyeeData");
            mv.addObject("EmpoyeeData", emp);
            ArrayList empTitleList = createEmployeeDao.empTitleType();
            mv.addObject("empTitleList", empTitleList);
            String isduplicate = requestParams.get("isduplicate");
            mv.addObject("isduplicate", isduplicate);
            mv.addObject("panInfo", panno);

            List bankList = bankDAO.getBankList();
            mv.addObject("bankList", bankList);
            List branchList = branchDAO.getBranchList(emp.getSltBank());
            mv.addObject("branchList", branchList);

            List maritallist = maritalStatusDAO.getMaritalList();
            mv.addObject("maritallist", maritallist);

            List categoryList = categoryDAO.getCategoryList();
            mv.addObject("categoryList", categoryList);

            List religionList = religionDAO.getReligionList();
            mv.addObject("religionList", religionList);

            List bloodGroupList = bloodGroupDAO.getBloodGroup();
            mv.addObject("bloodGroupList", bloodGroupList);

            List gisNumberList = gisTypeDAO.getGisTypeList();
            mv.addObject("gisNumberList", gisNumberList);
            
            List splSubCatgList=employeeDAO.specialSubCategory();
            mv.addObject("splSubCatgList", splSubCatgList);
                    

            mv.addObject("loginusertype", lub.getLoginusertype());
           
            if (lub.getLoginuserid() != null && !lub.getLoginuserid().equals("")) {
               mv.addObject("username", lub.getLoginuserid());                 
            }
        } else {
            mv.setViewName("under_const");
        }
        return mv;
    }

    @RequestMapping(value = "updateEmployeeData")
    public ModelAndView updateEmployeeData(@ModelAttribute("searchEmployee") Employee employee, @ModelAttribute("LoginUserBean") LoginUserBean lub, ModelMap model) {
        ModelAndView mv = new ModelAndView();
        String isduplicate = "";
        String empid = employee.getEmpid();
        String dcLoginId = null;
        if (lub.getLoginusertype().equals("A")) {
            dcLoginId = lub.getLoginusername();
        } else if (lub.getLoginusertype().equals("D")) {
            dcLoginId = lub.getLoginuserid();
        }
        String updatedData = employee.getHidmsg();
        if (dcLoginId != null && (dcLoginId.contains("deo") || dcLoginId.contains("dc") || dcLoginId.contains("hrmsupport"))) {
            if (updatedData != null && !updatedData.equals("")) {
                employeeDAO.saveAdminLog("LocateEmployee", dcLoginId, lub.getLoginusertype(), empid, updatedData);
            }
        }
        String isDuplicateGPF = employeeDAO.updateEmployeeData(employee, dcLoginId);

        IdentityInfo idinfo = new IdentityInfo();
        idinfo.setEmpId(empid);
        idinfo.setHidIdentityId("AADHAAR");
        idinfo.setIdentityDocNo(employee.getAadhaarno());
        if (lub.getLoginusertype().equals("A") || lub.getLoginusertype().equals("D")) {
            employeeDAO.updateIdentityForDC(idinfo);
        } else {
            employeeDAO.updateIdentity(idinfo);
        }

        idinfo = new IdentityInfo();
        idinfo.setEmpId(empid);
        idinfo.setHidIdentityId("PAN");
        idinfo.setIdentityDocNo(employee.getPanno());
        if (lub.getLoginusertype().equals("A") || lub.getLoginusertype().equals("D")) {
            employeeDAO.updateIdentityForDC(idinfo);
        } else {
            employeeDAO.updateIdentity(idinfo);
        }

        if (isDuplicateGPF.equals("Y")) {
            isduplicate = "Y";
        }
        return new ModelAndView("redirect:/EditEmpoyeeData.htm?isduplicate=" + isduplicate + "&empid=" + empid);
    }

    @RequestMapping(value = "employeeCommunicationCount")
    public ModelAndView employeeCommunicationCount(@ModelAttribute("searchEmployee") Employee employee, @ModelAttribute("LoginUserBean") LoginUserBean lub, ModelMap model) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("empinfo/EmpCommunicationCount");
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
        mv.addObject("totalCnt", TotalCount);
        mv.addObject("totalnotview", part1);
        mv.addObject("totalview", part2);
        return mv;
    }

    @RequestMapping(value = "viewCommunicationDetails")
    public ModelAndView viewCommunicationList(@ModelAttribute("LoginUserBean") LoginUserBean lub) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("empinfo/communicationDetailsList");
        String OfficePri = "N";
        if (lub.getLoginhasofficePriv() != null && !lub.getLoginhasofficePriv().isEmpty()) {
            OfficePri = "Y";
        }
        mv.addObject("employeemessagelist", employeeDAO.viewCommunicationDetails(OfficePri, lub.getLoginempid(), lub.getLoginoffcode()));
        return mv;
    }

    /*Back to Communication List*/
    @RequestMapping(value = "viewCommunicationDetails", params = "action=Back")
    public ModelAndView viewCommunicationListBack(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("employeeMessage") EmployeeMessage employeeMessage) {
        ModelAndView mv = new ModelAndView("redirect:/viewCommunicationDetails.htm");

        return mv;
    }
    /*Back to Communication List after click on back button of view option*/

    @RequestMapping(value = "backviewCommunicationDetails")
    public ModelAndView backviewCommunicationList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("employeeMessage") EmployeeMessage employeeMessage) {
        ModelAndView mv = new ModelAndView("redirect:/viewCommunicationDetails.htm");

        return mv;
    }

    /*Reply to Communication Message*/
    @RequestMapping(value = "viewCommunicationDetails", params = "action=Submit")
    public ModelAndView replyCommunication(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("employeeMessage") EmployeeMessage employeeMessage) {
        ModelAndView mv = new ModelAndView("redirect:/viewCommunicationDetails.htm");
        employeeMessage.setEmpid(lub.getLoginempid());
        employeeMessage.setRecieverSpc(lub.getLoginspc());
        employeeMessage.setReceiverUserType(lub.getLoginusertype());

        //messageId
        int messageId = employeeDAO.saveReplyEmployeeMessage(employeeMessage);
        List<MultipartFile> files = employeeMessage.getUploadedFile();

        if (null != files && files.size() > 0) {
            employeeDAO.uploadMultipleAttachedFile(messageId, "MESSAGE", employeeMessage.getUploadedFile());
        }
        /* if (!employeeMessage.getUploadedFile().isEmpty()) {
            
         } */
        return mv;
    }

    @RequestMapping(value = "viewCommunicationMessage")
    public ModelAndView viewCommunicationMessage(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("employeeMessage") EmployeeMessage employeeMessage) {
        ModelAndView mv = new ModelAndView();
        String OfficePri = "N";
        if (lub.getLoginhasofficePriv() != null && !lub.getLoginhasofficePriv().isEmpty()) {
            OfficePri = "Y";
        }
        mv.setViewName("empinfo/viewCommunicationMessage");
        employeeMessage = employeeDAO.getParentMessageDetail(employeeMessage.getMessageId());
        mv.addObject("employeeMessage", employeeMessage);
        mv.addObject("employeemessagelist", employeeDAO.viewCommunicationMessage(OfficePri, lub.getLoginempid(), lub.getLoginoffcode(), employeeMessage.getMessageId()));
        return mv;
    }

    @RequestMapping(value = "replyCommunicationMessage")
    public ModelAndView replyCommunicationMessage(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("employeeMessage") EmployeeMessage employeeMessage) {
        ModelAndView mv = new ModelAndView();
        String OfficePri = "N";
        if (lub.getLoginhasofficePriv() != null && !lub.getLoginhasofficePriv().isEmpty()) {
            OfficePri = "Y";
        }
        mv.setViewName("empinfo/replyCommunicationMessage");
        employeeMessage = employeeDAO.getParentMessageDetail(employeeMessage.getMessageId());
        mv.addObject("employeeMessage", employeeMessage);
        mv.addObject("employeemessagelist", employeeDAO.viewCommunicationMessage(OfficePri, lub.getLoginempid(), lub.getLoginoffcode(), employeeMessage.getMessageId()));
        return mv;
    }

    @RequestMapping(value = "convertRegularToContractualSixYr")
    public ModelAndView convertRegularToContractualSixYr(@RequestParam("empid") String empid, @RequestParam Map<String, String> requestParams) {
        employeeDAO.updateEmpRegularToSixYrContractual(empid);
        return new ModelAndView("redirect:/LocateEmployee.htm");
    }

    @RequestMapping(value = "LocateEmployee.htm", params = "submit=Save")
    public ModelAndView contractualSixYrToContractual(@ModelAttribute("searchEmployee") SearchEmployee employee) {
        employeeDAO.saveContractualEmpData(employee);
        return new ModelAndView("redirect:/LocateEmployee.htm");
    }

    @RequestMapping(value = "LocateEmployeeAdmin")
    public ModelAndView locateEmployeeAdmin(@Valid @ModelAttribute("searchEmployee") SearchEmployee empinfo, ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub) {
        ModelAndView mv = new ModelAndView();
        empinfo.setPage(1);
        empinfo.setRows(10000);
        mv.setViewName("empinfo/SearchEmployeeAdmin");
        mv.addObject("searchEmployee", empinfo);
        mv.addObject("departmentList", departmentDao.getDepartmentList());
        mv.addObject("officeList", officeDao.getTotalOfficeList(empinfo.getDeptName()));
        mv.addObject("empSearchResult", employeeDAO.LocateEmployee(empinfo));
        return mv;
    }

    @RequestMapping(value = "EditEmpoyeeDataAdmin")
    public ModelAndView EditEmpoyeeDataAdmin(@RequestParam("empid") String empid, @RequestParam Map<String, String> requestParams) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("empinfo/EditEmpoyeeDataAdmin");
        mv.addObject("EmpoyeeData", employeeDAO.EditEmpoyeeDataAdmin(empid));
        ArrayList empTitleList = createEmployeeDao.empTitleType();
        ArrayList empDepList = employeeDAO.empDepType();
        ArrayList empPayscaleList = employeeDAO.empPayScale();
        ArrayList empPostgrptype = employeeDAO.empPostgrptype();
        mv.addObject("empTitleList", empTitleList);
        mv.addObject("empDepList", empDepList);
        mv.addObject("empPayscaleList", empPayscaleList);
        mv.addObject("empPostgrptype", empPostgrptype);
        String isduplicate = requestParams.get("isduplicate");
        mv.addObject("isduplicate", isduplicate);
        return mv;
    }

    @RequestMapping(value = "updateEmployeeDataAdmin")
    public ModelAndView updateEmployeeDataAdmin(@ModelAttribute("searchEmployee") Employee employee, @ModelAttribute("LoginUserBean") LoginUserBean lub, ModelMap model) {
        ModelAndView mv = new ModelAndView();
        String isduplicate = "";
        String empid = employee.getEmpid();
        String dcLoginId = lub.getLoginuserid();
        String isDuplicateGPF = employeeDAO.updateEmployeeDataAdmin(employee, dcLoginId);
        if (isDuplicateGPF.equals("Y")) {
            isduplicate = "Y";
        }
        return new ModelAndView("redirect:/EditEmpoyeeDataAdmin.htm?isduplicate=" + isduplicate + "&empid=" + empid);
    }

    @RequestMapping(value = "LocateEmployeeLMS")
    public ModelAndView LocateEmployeeLMS(@Valid @ModelAttribute("searchEmployee") SearchEmployee empinfo, ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub) {
        ModelAndView mv = new ModelAndView();
        String path = "under_const";
        String employeecode = empinfo.getEmpid();
        if (lub.getLoginusertype().equalsIgnoreCase("A") || lub.getLoginusertype().equalsIgnoreCase("D") || lub.getLoginuserid().contains("dc") || lub.getLoginusertype().equalsIgnoreCase("S") || lub.getLoginusertype().equalsIgnoreCase("H")) {
            empinfo.setPage(1);
            empinfo.setRows(10);
            path = "empinfo/SearchEmployeeLMS";
            empinfo.setUsertype(lub.getLoginusertype());
            empinfo.setUsername(lub.getLoginusername());
            mv.setViewName(path);
            mv.addObject("searchEmployeelms", empinfo);
            mv.addObject("departmentList", departmentDao.getDepartmentList());
            mv.addObject("officeList", officeDao.getTotalOfficeList(empinfo.getDeptName()));
            if ((empinfo.getOffcode() != null && !empinfo.getOffcode().equals("")) || (empinfo.getCriteria() != null && !empinfo.getCriteria().equals(""))) {
                mv.addObject("empSearchResult", employeeDAO.LocateEmployee(empinfo));
            }

        } else {
            path = "under_const";
            mv.setViewName(path);
        }
        return mv;
    }

    @RequestMapping(value = "EmployeeBasicProfileLMS")
    public ModelAndView EmployeeBasicProfileLMS(@RequestParam("empid") String empid) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("empinfo/EmployeeBasicProfileLMS");
        mv.addObject("EmployeeProfile", employeeDAO.getEmployeeProfile(empid));
        return mv;
    }

    @RequestMapping(value = "Update7thPayCommissionDataPage")
    public String Update7thPayCommissionDataPage(Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("employee") Employee employee) {

        return "/empinfo/Update7thPayCommissionData";
    }

    @RequestMapping(value = "Update7thPayCommissionData", params = "btnSubmit=Search")
    public String Search7thPayCommissionData(Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("employee") Employee employee) {
        try {
            String errMsg = employeeDAO.verifyEmpIdfor7thPayCommission(lub.getLogindistrictcode(), employee.getEmpid());
            model.addAttribute("errMsg", errMsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/empinfo/Update7thPayCommissionData";
    }

    @RequestMapping(value = "Update7thPayCommissionData", params = "btnSubmit=Update")
    public String Update7thPayCommissionData(Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("employee") Employee employee) {
        try {
            int retVal = employeeDAO.updateEmpIdfor7thPayCommission(employee.getEmpid(), employee.getSltPayCommission());
            model.addAttribute("retVal", retVal);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/empinfo/Update7thPayCommissionData";
    }

    @RequestMapping(value = "LocateEmployeeAddress.htm")
    public ModelAndView LocateEmployeeAddress(@ModelAttribute("address") Address address) {
        ModelAndView mv = new ModelAndView("/empinfo/LocateEmployeeAddress");
        address.setStateCode("21");
        mv.addObject("stateList", stateDAOImpl.getStateList());
        Address[] empAddress = employeeDAO.getAddress(address.getEmpId());
        mv.addObject("empAddress", empAddress);
        return mv;
    }

    @RequestMapping(value = "LocateEmployeeSaveAddress.htm")
    public ModelAndView LocateEmployeeSaveAddress(@ModelAttribute("address") Address address) {
        ModelAndView mv = new ModelAndView("redirect:/LocateEmployeeAddress.htm?empId=" + address.getEmpId());
        employeeDAO.saveAddressData(address);
        boolean isCmpleted = employeeDAO.isprofileCompleted(address.getEmpId());
        if (isCmpleted == true) {
            employeeDAO.updateProfileCompletedStatus(address.getEmpId());
        }
        return mv;
    }

    @RequestMapping(value = "LocateEmployeeEditAddress")
    public ModelAndView LocateEmployeeEditAddress(@RequestParam("addressId") int addressId, @ModelAttribute("address") Address address) {
        ModelAndView mv = new ModelAndView("/empinfo/LocateEmployeeAddress");
        String empid = address.getEmpId();
        address = employeeDAO.getAddressDetals(address.getEmpId(), addressId);
        address.setEmpId(empid);
        mv.addObject("policeList", policeStationDAO.getPoliceStationList(address.getDistCode()));
        mv.addObject("postofficeList", postOfficeDAO.getPostOfficeList(address.getDistCode()));
        mv.addObject("villageList", villageDAO.getVillageList(address.getDistCode(), address.getPsCode()));
        mv.addObject("stateList", stateDAOImpl.getStateList());
        mv.addObject("blockList", blockDAO.getBlockList(address.getDistCode()));
        mv.addObject("address", address);
        Address[] empAddress = employeeDAO.getAddress(address.getEmpId());
        mv.addObject("empAddress", empAddress);
        mv.addObject("districtList", districtDAO.getDistrictList(address.getStateCode()));

        return mv;
    }

}
