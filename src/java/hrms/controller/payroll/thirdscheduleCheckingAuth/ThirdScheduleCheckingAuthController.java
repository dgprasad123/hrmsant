package hrms.controller.payroll.thirdscheduleCheckingAuth;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import hrms.common.Message;
import hrms.dao.employee.EmployeeDAO;
import hrms.dao.master.DepartmentDAO;
import hrms.dao.master.PostDAO;
import hrms.dao.payroll.thirdscheduleCheckingAuth.ThirdScheduleCheckingAuthDAO;
import hrms.model.login.LoginUserBean;
import hrms.model.payroll.thirdschedulecheckingauth.ThirdScheduleCheckingAuthForm;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

@Controller
@SessionAttributes("LoginUserBean")
public class ThirdScheduleCheckingAuthController {

    @Autowired
    DepartmentDAO departmentDao;

    @Autowired
    public ThirdScheduleCheckingAuthDAO thirdSchedleCheckingAuthDAO;

    @Autowired
    public PostDAO postDAO;
    
    @Autowired
    EmployeeDAO employeeDAO;
    
    @RequestMapping(value = "ThirdScheduleCheckingAuthEmpList")
    public String thirdScheduleCheckingAuthEmpList() {

        return "/payroll/thirdschedulecheckingauth/ThirdScheduleCheckingAuthList";

    }

    @RequestMapping(value = "getCheckingAuthThirdScheduleEmpListJSON")
    public void getCheckingAuthThirdScheduleEmpListJSON(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("rows") int rows, @RequestParam("page") int page, @RequestParam("offcode") String offcode) {

        response.setContentType("application/json");

        JSONObject json = new JSONObject();
        PrintWriter out = null;

        int total = 0;

        try {
            List empList = thirdSchedleCheckingAuthDAO.getEmployeeList(lub.getLoginempid(), rows, page, offcode);
            total = thirdSchedleCheckingAuthDAO.getEmployeeListCount(lub.getLoginempid(), offcode);

            json.put("rows", empList);
            json.put("total", total);

            out = response.getWriter();
            out.write(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "viewCheckingAuthThirdSchedule")
    public String ViewCheckingAuthThirdSchedule(Model model, @RequestParam("empid") String empid) {

        ThirdScheduleCheckingAuthForm tform = null;
        String path = "";

        List postlist2nd = null;
        try {
            tform = thirdSchedleCheckingAuthDAO.getThirdScheduleData(empid);

            if (tform != null && tform.getIsIASCadre().equals("Y")) {
                path = "/payroll/thirdschedulecheckingauth/ThirdScheduleCheckingAuthIASView";
                postlist2nd = postDAO.getPostList(tform.getDeptCodeIAS());
            } else if(tform != null && tform.getIsUGCCadre().equals("Y")){
                path = "/payroll/thirdschedulecheckingauth/ThirdScheduleCheckingAuthViewUGC";
                postlist2nd = postDAO.getPostList(tform.getDeptCode());
            }else {
                path = "/payroll/thirdschedulecheckingauth/ThirdScheduleCheckingAuthView";
                postlist2nd = postDAO.getPostList(tform.getDeptCode());
            }

            List deptlist = departmentDao.getDepartmentList();
            List postlist6th = postDAO.getPostList(tform.getEntryDeptCode());

            model.addAttribute("deptlist", deptlist);
            model.addAttribute("PostList2nd", postlist2nd);
            model.addAttribute("PostList6th", postlist6th);
            model.addAttribute("tform", tform);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return path;
    }

    @RequestMapping(value = "ThirdscheduleCheckingAuthPDF")
    public void thirdscheduleCheckingAuthPDF(HttpServletResponse response, @RequestParam("empid") String empid) {

        response.setContentType("application/pdf");
        Document document = new Document(PageSize.A4);

        ThirdScheduleCheckingAuthForm tform = null;
        PdfWriter writer = null;
        try {
            String offName = thirdSchedleCheckingAuthDAO.getEmployeeOfficeName(empid);

            response.setHeader("Content-Disposition", "attachment; filename=Third_Schedule_" + empid + ".pdf");

            writer = PdfWriter.getInstance(document, response.getOutputStream());
            writer.setPageEvent(new ThirdScheduleCheckingHeaderFooter(offName, empid));

            document.open();

            tform = thirdSchedleCheckingAuthDAO.getThirdScheduleData(empid);

            if (tform != null && tform.getIsIASCadre().equals("Y")) {
                thirdSchedleCheckingAuthDAO.thirdScheduleIASPDF(document, tform);
            } else if(tform != null && tform.getIsUGCCadre().equals("Y")){
                thirdSchedleCheckingAuthDAO.thirdScheduleUGCPDF(document, empid);
            } else {
                thirdSchedleCheckingAuthDAO.thirdSchedulePDF(document, empid);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }

    @ResponseBody
    @RequestMapping(value = "saveThirdCheckingAuthScheduleData")
    public void SaveThirdScheduleCheckingAuthData(HttpServletResponse response, @ModelAttribute("thirdScheduleCheckingAuthForm") ThirdScheduleCheckingAuthForm tform, @RequestParam("approve") String approve) {

        response.setContentType("application/json");
        PrintWriter out = null;

        Message msg = null;

        try {
            msg = thirdSchedleCheckingAuthDAO.saveThirdScheduleCheckingAuthData(tform, approve);

            JSONObject job = new JSONObject(msg);
            out = response.getWriter();
            out.write(job.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "saveVerifyingAuthData")
    public void SaveVerifyingAuthData(HttpServletResponse response, @RequestParam Map<String, String> requestParams) {

        response.setContentType("application/json");
        PrintWriter out = null;

        Message msg = null;

        try {
            String chkEmp = (String) requestParams.get("chkEmp");
            String authEmp = (String) requestParams.get("authEmp");

            
            

            msg = thirdSchedleCheckingAuthDAO.saveVerifyingAuthData(chkEmp, authEmp);

            JSONObject job = new JSONObject(msg);
            out = response.getWriter();
            out.write(job.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "revertCheckingAuthEmp")
    public void RevertCheckingAuthEmp(HttpServletResponse response, @RequestParam Map<String, String> requestParams) {

        response.setContentType("application/json");
        PrintWriter out = null;

        Message msg = null;

        try {
            String chkEmp = (String) requestParams.get("chkEmp");

            

            msg = thirdSchedleCheckingAuthDAO.revertCheckingAuthEmp(chkEmp);

            JSONObject job = new JSONObject(msg);
            out = response.getWriter();
            out.write(job.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ResponseBody
    @RequestMapping(value = "getCheckingEmployeesOfficeListJSON")
    public void GetCheckingEmployeesOfficeListJSON(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub) {

        response.setContentType("application/json");
        PrintWriter out = null;
        JSONArray json = null;

        try {
            List offlist = thirdSchedleCheckingAuthDAO.getOfficeList(lub.getLoginempid());
            json = new JSONArray(offlist);
            out = response.getWriter();
            out.write(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }
    
    @RequestMapping(value = "RevertThirdScheduleDC")
    public ModelAndView RevertThirdScheduleDC(@RequestParam("empid") String empid, @ModelAttribute("LoginUserBean") LoginUserBean lub) {
        ModelAndView mv = new ModelAndView();
        boolean employeedistrict = employeeDAO.verifyEmployeeDistrict(lub.getLogindistrictcode(), empid);
        if (employeedistrict == true) {
            String revertStatus = thirdSchedleCheckingAuthDAO.thirdScheduleRevertDC(empid);
            
            mv.addObject("revertStatus", revertStatus);
            
            mv.setViewName("/payroll/thirdschedulecheckingauth/RevertThirdScheduleDC");
        } else {
            mv.setViewName("under_const");
        }
        return mv;
    }
}
