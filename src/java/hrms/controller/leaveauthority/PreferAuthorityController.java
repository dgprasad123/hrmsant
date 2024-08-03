package hrms.controller.leaveauthority;

import hrms.dao.leaveauthority.LeaveAuthorityDAOImpl;
import hrms.dao.master.DepartmentDAO;
import hrms.model.leaveauthority.LeaveAuthority;
import hrms.model.login.LoginUserBean;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes("LoginUserBean")

public class PreferAuthorityController {

    @Autowired
    public LeaveAuthorityDAOImpl leaveAuthorityDAO;
    @Autowired
    DepartmentDAO departmentDao;

    public static int getYearFromDate(Date date) {
        int result = -1;
        if (date != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            result = cal.get(Calendar.YEAR);
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "SelectSanctionAuthorityAction.htm", method = RequestMethod.POST)
    public void viewAuthorityList(@ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, Map<String, Object> model, HttpServletResponse response) {
        response.setContentType("application/json");
        JSONObject json = new JSONObject();
        PrintWriter out = null;
        try {
            int year = getYearFromDate(new Date());
            List leaveAuthoritylist = leaveAuthorityDAO.leaveAuthorityList(lub.getLoginspc(), lub.getLoginoffcode(), lub.getLoginempid(), "1", year + "-" + (year + 1));

            json.put("total", 50);
            json.put("rows", leaveAuthoritylist);
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
    @RequestMapping(value = "SelectIssuingAuthorityAction.htm", method = {RequestMethod.POST, RequestMethod.GET})
    public void viewIssueAuthorityList(@ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, Map<String, Object> model, HttpServletResponse response) {
        response.setContentType("application/json");
        JSONObject json = new JSONObject();
        PrintWriter out = null;
        try {
            int year = getYearFromDate(new Date());
            List leaveAuthoritylist = leaveAuthorityDAO.leaveAuthorityList(lub.getLoginspc(), lub.getLoginoffcode(), lub.getLoginempid(), "1", year + "-" + (year + 1));
            json.put("total", 50);
            json.put("rows", leaveAuthoritylist);
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
    @RequestMapping(value = "SelectOtherStaffAction.htm", method = RequestMethod.POST)
    public void viewOtherStaffList(@ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, Map<String, Object> model, HttpServletResponse response) {
        response.setContentType("application/json");
        JSONObject json = new JSONObject();
        PrintWriter out = null;
        try {
            int year = getYearFromDate(new Date());
            List leaveAuthoritylist = leaveAuthorityDAO.otherStaffList(lub.getLoginspc(), lub.getLoginempid(), "1", year + "-" + (year + 1));
            json.put("total", 50);
            json.put("rows", leaveAuthoritylist);
            out = response.getWriter();
            out.write(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    @RequestMapping(value = "searchLeaveAuth.htm", method = {RequestMethod.GET,RequestMethod.POST})
    public String viewLeaveAuth(@ModelAttribute("leaveAuthorityForm") LeaveAuthority leaveAuthority, Map<String, Object> model) {
        model.put("leaveAuthorityForm", leaveAuthority);
         model.put("departmentList", departmentDao.getDepartmentList());
        return "/leaveauthority/SearchSancAuth";
    }

    @RequestMapping(value = "leaveauthority.htm", method = RequestMethod.GET)
    public String viewLeave(@ModelAttribute("leaveAuthorityForm") LeaveAuthority leaveAuthority, Map<String, Object> model) {
        model.put("leaveAuthorityForm", leaveAuthority);
        return "/leaveauthority/SearchSancAuthority";
    }

    @RequestMapping(value = "leaveissuingauthority.htm", method = RequestMethod.GET)
    public String viewIssuingAuth(@ModelAttribute("leaveAuthorityForm") LeaveAuthority leaveAuthority, Map<String, Object> model) {
        model.put("leaveAuthorityForm", leaveAuthority);
        return "/leaveauthority/SearchIssuingAuthority";
    }

    @RequestMapping(value = "leaveauthority1.htm", method = RequestMethod.GET)
    public String viewSubordinate(@ModelAttribute("leaveAuthorityForm") LeaveAuthority leaveAuthority, Map<String, Object> model, @RequestParam("id") String id, HttpServletRequest request) {

        model.put("leaveAuthorityForm", leaveAuthority);
        return "/leaveauthority/ApplyForOther";
    }

    @RequestMapping(value = "leaveauthority.htm", method = RequestMethod.POST, params = "Search")
    public String searchAuthority(@ModelAttribute("leaveAuthorityForm") LeaveAuthority leaveAuthority, Map<String, Object> model) {
        model.put("leaveAuthorityForm", leaveAuthority);
        return "/leaveauthority/PreferAuthorityEdit";
    }

    @RequestMapping(value = "leaveissuingauthority.htm", method = RequestMethod.POST, params = "Search")
    public String searchIssuingAuthority(@ModelAttribute("leaveAuthorityForm") LeaveAuthority leaveAuthority, Map<String, Object> model) {
        model.put("leaveAuthorityForm", leaveAuthority);
        return "/leaveauthority/PreferIssuingAuthorityEdit";
    }

    @RequestMapping(value = "selectotherstaff.htm", method = {RequestMethod.POST, RequestMethod.GET}, params = "Search")
    public String searchOtherStaff(@ModelAttribute("leaveAuthorityForm") LeaveAuthority leaveAuthority, Map<String, Object> model) {
        model.put("leaveAuthorityForm", leaveAuthority);
        return "/leaveauthority/ApplyForOtherEdit";
    }

    @RequestMapping(value = "leaveapplyfor.htm", method = RequestMethod.GET)
    public String applyFor(@ModelAttribute("leaveAuthorityForm") LeaveAuthority leaveAuthority, Map<String, Object> model) {
        model.put("leaveAuthorityForm", leaveAuthority);
        return "/applyingfor/ApplyingForEdit";
    }

    @RequestMapping(value = "getDeptComboData.htm", method = RequestMethod.POST)
    public @ResponseBody
    String getDeptList() {
        JSONArray json = null;
        try {
            List leaveType = departmentDao.getDeptList();
            json = new JSONArray(leaveType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json.toString();
    }

    @RequestMapping(value = "getOfficeComboData.htm", method = RequestMethod.POST)
    public @ResponseBody
    String getOfficeList(@RequestParam("deptCode") String deptCode) {
        JSONArray json = null;
        try {
            ArrayList leaveType = leaveAuthorityDAO.getOfficeList(deptCode);
            json = new JSONArray(leaveType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json.toString();
    }

    @RequestMapping(value = "getPostListJSON.htm", method = RequestMethod.POST)
    public @ResponseBody
    String getAuthorityList(@RequestParam("deptCode") String deptCode, @RequestParam("offCode") String offCode) {
        JSONArray json = null;
        try {
            ArrayList leaveType = leaveAuthorityDAO.getAuthorityList(deptCode, offCode);
            json = new JSONArray(leaveType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json.toString();
    }

    @RequestMapping(value = "getOtherStaffListJSON.htm", method = RequestMethod.POST)
    public @ResponseBody
    String getOtherStaffList(@RequestParam("deptCode") String deptCode, @RequestParam("offCode") String offCode) {
        JSONArray json = null;
        try {
            ArrayList leaveType = leaveAuthorityDAO.getOtherStaffList(deptCode, offCode);
            json = new JSONArray(leaveType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json.toString();
    }

    @RequestMapping(value = "leaveauthority.htm", method = {RequestMethod.POST, RequestMethod.POST}, params = "Save")
    public String saveAuthority(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("leaveAuthorityForm") LeaveAuthority leaveAuthority, Map<String, Object> model) {
        try {
            leaveAuthorityDAO.saveAuthority(lub.getLoginempid(), lub.getLoginspc(), leaveAuthority.getChkAuth(), "1", leaveAuthority.getSltDept(), leaveAuthority.getSltOffice());
            model.put("leaveAuthorityForm", leaveAuthority);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/leaveauthority/SearchSancAuthority";
    }

    @RequestMapping(value = "leaveissuingauthority.htm", method = {RequestMethod.POST, RequestMethod.POST}, params = "Save")
    public String saveIssuingAuthority(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("leaveAuthorityForm") LeaveAuthority leaveAuthority, Map<String, Object> model) {
        try {
            leaveAuthorityDAO.saveAuthority(lub.getLoginempid(), lub.getLoginspc(), leaveAuthority.getChkAuth(), "1", leaveAuthority.getSltDept(), leaveAuthority.getSltOffice());
            model.put("leaveAuthorityForm", leaveAuthority);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/leaveauthority/SearchIssuingAuthority";
    }

    @RequestMapping(value = "selectotherstaff.htm", method = {RequestMethod.GET, RequestMethod.POST}, params = "Save")
    public String saveOtherStaff(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("leaveAuthorityForm") LeaveAuthority leaveAuthority, Map<String, Object> model) {
        try {
            leaveAuthorityDAO.saveAuthority(lub.getLoginempid(), lub.getLoginspc(), leaveAuthority.getChkAuth(), "1", leaveAuthority.getSltDept(), leaveAuthority.getSltOffice());
            model.put("leaveAuthorityForm", leaveAuthority);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/leaveauthority/ApplyForOther";
    }
     @RequestMapping(value = "deleteSancAuth.htm", method = {RequestMethod.GET, RequestMethod.POST})
   
    String deleteSancAuth( @ModelAttribute("leaveAuthorityForm") LeaveAuthority leaveAuthority,@RequestParam("authSpc") String authSpc,@RequestParam("applicantSpc") String applicantSpc,Map<String, Object> model) {
        try {
           
            leaveAuthorityDAO.deleteSancAuth(authSpc,applicantSpc);
             model.put("leaveAuthorityForm", leaveAuthority);
        } catch (Exception e) {
            e.printStackTrace();
        }
      return "/leaveauthority/SearchSancAuthority";
    }
}
