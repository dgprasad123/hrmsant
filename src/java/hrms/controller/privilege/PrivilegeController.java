/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.privilege;

import hrms.common.CommonFunctions;
import hrms.dao.login.LoginDAO;
import hrms.dao.master.DepartmentDAO;
import hrms.dao.master.OfficeDAO;
import hrms.dao.privilege.PrivilegeManagementDAO;
import hrms.model.login.LoginUserBean;
import hrms.model.master.Module;
import hrms.model.privilege.Privilege;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
//import static org.joda.time.DateTimeFieldType.year;
import org.json.JSONArray;
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
 * @author Surendra
 */
@Controller
@SessionAttributes("LoginUserBean")
public class PrivilegeController {

    @Autowired
    public PrivilegeManagementDAO privilegeManagementDAO;

    @Autowired
    public DepartmentDAO departmentDao;

    @Autowired
    public OfficeDAO offDAO;

    @Autowired
    public LoginDAO loginDao;

    @RequestMapping(value = "displayAssignPrivilegepage", method = {RequestMethod.GET, RequestMethod.POST})
    public String DisplayPage(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response) {
        String path = "under_const";
        String distCode = null;

        if (lub.getLoginusertype().equalsIgnoreCase("A")) {
            distCode = "2117";
        } else {
            distCode = lub.getLogindistrictcode();
        }
        if (lub.getLoginusertype().equalsIgnoreCase("A") || (lub.getLoginusertype().equalsIgnoreCase("D") || lub.getLoginuserid().contains("dc")) || lub.getLoginusertype().equalsIgnoreCase("S")) {
            path = "/privilege/AssignPrivilege";
        } else {
            path = "under_const";
        }
        model.addAttribute("loginNm", lub.getLoginempid());
        model.addAttribute("distcode", distCode);
        model.addAttribute("deptList", departmentDao.getDepartmentList());
        return path;
    }

    @ResponseBody
    @RequestMapping(value = "getAssignedPrivilege", method = {RequestMethod.GET, RequestMethod.POST})
    public void getAssignedPrivilege(HttpServletRequest request, HttpServletResponse response, @RequestParam("spc") String spc) {
        response.setContentType("application/json");
        PrintWriter out = null;
        try {
            List li = privilegeManagementDAO.getAssignedPrivilageList(spc);
            JSONArray json = new JSONArray(li);
            out = response.getWriter();
            out.write(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            out.flush();
            out.close();
        }
    }

    @ResponseBody
    @RequestMapping(value = "getRoleList", method = {RequestMethod.GET, RequestMethod.POST})
    public void getRoleList(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("application/json");
        PrintWriter out = null;
        try {
            List li = privilegeManagementDAO.getRoleList();
            JSONArray json = new JSONArray(li);
            out = response.getWriter();
            out.write(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            out.flush();
            out.close();
        }
    }

    @ResponseBody
    @RequestMapping(value = "getModuleGroupList", method = {RequestMethod.GET, RequestMethod.POST})
    public void getModuleGroupList(HttpServletRequest request, HttpServletResponse response, @RequestParam("role") String role) {
        response.setContentType("application/json");
        PrintWriter out = null;
        try {
            List li = privilegeManagementDAO.getModuleGroupList(role);
            JSONArray json = new JSONArray(li);
            out = response.getWriter();
            out.write(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            out.flush();
            out.close();
        }
    }

    @ResponseBody
    @RequestMapping(value = "revokePrivilage", method = {RequestMethod.GET, RequestMethod.POST})
    public void revokePrivilage(ModelMap model, @RequestParam("privmapid") int privmapid, @RequestParam("spc") String spc, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response) {
        PrintWriter out = null;
        try {
            String status = privilegeManagementDAO.revokePrivilege(spc, privmapid);
            out = response.getWriter();
            out.write(status);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    @ResponseBody
    @RequestMapping(value = "assignPrivilege", method = {RequestMethod.GET, RequestMethod.POST})
    public void assignPrivilege(@RequestParam("roleid") String roleid, @RequestParam("modgrpid") String modgrpid, @RequestParam("modid") int modid,
            @RequestParam("spc") String spc, @RequestParam("hrmsid") String hrmsid, HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletResponse response) {
        PrintWriter out = null;
        try {
            Module module = new Module();
            module.setRoleid(roleid);
            module.setModgrpid(modgrpid);
            module.setModid(modid);

            String offcode = spc.substring(0, 12);

            String loginIP = CommonFunctions.getISPIPAddress();
            //System.out.println("spc:::" + spc + "::" + offcode+":"+lub.getLoginusername());

            boolean privFound = loginDao.checkUserprivLog(loginIP, spc);

            if (privFound == false) {
                loginDao.saveLoginLog(lub.getLoginempid(), lub.getLoginusername(), loginIP, "ASSIGN PRIVILEGE", lub.getLoginoffcode(), spc, lub.getLoginusertype(), hrmsid);
            }

            String status = privilegeManagementDAO.assignPrivilege(module, spc, hrmsid);
            out = response.getWriter();
            out.write(status);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    @RequestMapping(value = "displayAssignPrivilegepageUserNameSpecific", method = {RequestMethod.GET, RequestMethod.POST})
    public String DisplayPageUsernameSpecific(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response) {
        String path = "/privilege/AssignPrivilegeUserNameSpecific";
        model.addAttribute("usertypelist", privilegeManagementDAO.getUserType());
        model.addAttribute("userList", privilegeManagementDAO.getUserList("A"));
        return path;
    }

    @ResponseBody
    @RequestMapping(value = "getUserNameUsertypeSpecific", method = {RequestMethod.GET, RequestMethod.POST})
    public void getUserNameUsertypeSpecific(HttpServletRequest request, HttpServletResponse response, @RequestParam("usertype") String usertype) {
        response.setContentType("application/json");
        PrintWriter out = null;
        try {
            List li = privilegeManagementDAO.getUserList(usertype);
            JSONArray json = new JSONArray(li);
            out = response.getWriter();
            out.write(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            out.flush();
            out.close();
        }
    }

    @ResponseBody
    @RequestMapping(value = "getAssignedPrivilegeUserNameSpecific", method = {RequestMethod.GET, RequestMethod.POST})
    public void getAssignedPrivilegeUserNameSpecific(HttpServletRequest request, HttpServletResponse response, @RequestParam("username") String userName) {
        response.setContentType("application/json");
        PrintWriter out = null;
        try {
            List li = privilegeManagementDAO.getAssignedPrivilageUserNameSpecificList(userName);
            JSONArray json = new JSONArray(li);
            out = response.getWriter();
            out.write(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            out.flush();
            out.close();
        }
    }

    @ResponseBody
    @RequestMapping(value = "getModuleListUserNameSpecific", method = {RequestMethod.GET, RequestMethod.POST})
    public void getModuleListUserNameSpecific(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("application/json");
        PrintWriter out = null;
        try {
            List li = privilegeManagementDAO.getModuleListUserNameSpecific("U");
            JSONArray json = new JSONArray(li);
            out = response.getWriter();
            out.write(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            out.flush();
            out.close();
        }
    }

    @ResponseBody
    @RequestMapping(value = "assignPrivilegeUserNameSpecific", method = {RequestMethod.GET, RequestMethod.POST})
    public void assignPrivilegeUserNameSpecific(@RequestParam("username") String username, @RequestParam("modid") int modid, HttpServletRequest request, HttpServletResponse response) {
        PrintWriter out = null;
        try {
            Module module = new Module();
            module.setModid(modid);
            String status = privilegeManagementDAO.assignPrivilegeUserNameSpecific(module, username);
            out = response.getWriter();
            out.write(status);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            out.flush();
            out.close();
        }
    }

    @ResponseBody
    @RequestMapping(value = "revokePrivilageUserNameSpecific", method = {RequestMethod.GET, RequestMethod.POST})
    public void revokePrivilageUserNameSpecific(ModelMap model, @RequestParam("privmapid") int privmapid, @RequestParam("username") String username, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response) {
        PrintWriter out = null;
        try {
            String status = privilegeManagementDAO.revokePrivilageUserNameSpecific(username, privmapid);
            out = response.getWriter();
            out.write(status);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            out.flush();
            out.close();
        }
    }

    @ResponseBody
    @RequestMapping(value = "getAllAssignedPrivileges", method = {RequestMethod.GET, RequestMethod.POST})
    public void getAllAssignedPrivilegeUserList(HttpServletRequest request, HttpServletResponse response, @RequestParam("offCode") String offCode) {
        response.setContentType("application/json");
        PrintWriter out = null;
        try {
            List li = privilegeManagementDAO.getAllAssignedPrivilegeUserList(offCode);
            JSONArray json = new JSONArray(li);
            out = response.getWriter();
            out.write(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            out.flush();
            out.close();
        }
    }

    @ResponseBody
    @RequestMapping(value = "revokeUserPrivilege", method = {RequestMethod.GET, RequestMethod.POST})
    public void revokeUserPrivilege(ModelMap model, @RequestParam("privspc") String privspc, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response) {
        //response.setContentType("text/html");
        PrintWriter out = null;
        try {
            int status = privilegeManagementDAO.revokeUserPrivilege(privspc);
            out = response.getWriter();
            out.write(status);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    @RequestMapping(value = "displayAssignPrivilegeGroupWisePage")
    public String DisplayGroupWisePrivilegePage(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") Privilege privform) {
        String path = "under_const";
        //String distCode = null;

        if (lub.getLoginusertype().equalsIgnoreCase("A")) {
            privform.setDistcode("2117");
        } else {
            privform.setDistcode(lub.getLogindistrictcode());
        }
        if (lub.getLoginusertype().equalsIgnoreCase("A") || (lub.getLoginusertype().equalsIgnoreCase("D") || lub.getLoginuserid().contains("dc")) || lub.getLoginusertype().equalsIgnoreCase("S")) {
            path = "/privilege/AssignPrivilegeGroupWise";
        } else {
            path = "under_const";
        }
        model.addAttribute("distcode", privform.getDistcode());
        model.addAttribute("deptList", departmentDao.getDepartmentList());
        return path;
    }

    @RequestMapping(value = "displayAssignPrivilegeGroupWise")
    public ModelAndView displayAssignPrivilegeGroupWise(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") Privilege privform) {

        ModelAndView mav = null;

        mav = new ModelAndView("/privilege/AssignPrivilegeGroupWise", "command", privform);

        mav.addObject("deptList", departmentDao.getDepartmentList());
        mav.addObject("distcode", privform.getDistcode());

        List offlist = offDAO.getTotalOfficeList(privform.getDeptCode(), privform.getDistcode());
        mav.addObject("offlist", offlist);
        mav.addObject("hidPostCode", privform.getPostcode());
        //mav.addObject("distcode", privform.getPostcode());

        mav.addObject("abstractList", privilegeManagementDAO.getGroupWisePrivilegeList(privform.getPostcode()));
        //mav.addObject("assignedPrivilegeList",privilegeManagementDAO.getGroupWisePrivilegeAssignedList(privform.getPostcode()));
        return mav;
    }

    @ResponseBody
    @RequestMapping(value = "assignGroupWisePrivilege")
    public void assignGroupWisePrivilege(HttpServletRequest request, HttpServletResponse response,
            @RequestParam("checkedAbstractModule") String checkedAbstractModule, @RequestParam("spc") String spc,
            @RequestParam("hrmsid") String hrmsid, @ModelAttribute("LoginUserBean") LoginUserBean lub) {
        PrintWriter out = null;
        try {
            String status = privilegeManagementDAO.assignGroupWisePrivilege(checkedAbstractModule, spc, hrmsid);

            String offcode = spc.substring(0, 12);
            String loginIP = CommonFunctions.getISPIPAddress();
            boolean privFound = loginDao.checkUserprivLog(loginIP, spc);

            if (privFound == false) {
                loginDao.saveLoginLog(lub.getLoginempid(), lub.getLoginusername(), loginIP, "MODULE PRIVILEGE", lub.getLoginoffcode(), spc, lub.getLoginusertype(), hrmsid);
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

    @ResponseBody
    @RequestMapping(value = "revokeGroupWisePrivilege")
    public void revokeGroupWisePrivilege(HttpServletRequest request, HttpServletResponse response, @RequestParam("checkedAbstractModule") String checkedAbstractModule, @RequestParam("spc") String spc) {
        PrintWriter out = null;
        try {
            String status = privilegeManagementDAO.revokeGroupWisePrivilege(checkedAbstractModule, spc);
            out = response.getWriter();
            out.write(status);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }
}
