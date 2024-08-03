package hrms.controller.payroll.thirdscheduleVerifyingAuth;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import hrms.common.Message;
import hrms.dao.master.DepartmentDAO;
import hrms.dao.master.PostDAO;
import hrms.dao.payroll.thirdscheduleVerifyingAuth.ThirdScheduleVerifyingAuthDAO;
import hrms.model.login.LoginUserBean;
import hrms.model.payroll.thirdscheduleverifyingauth.ThirdScheduleVerifyingAuthForm;
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

@Controller
@SessionAttributes("LoginUserBean")
public class ThirdScheduleVerifyingAuthController {
    
    @Autowired
    DepartmentDAO departmentDao;
    
    @Autowired
    public ThirdScheduleVerifyingAuthDAO thirdSchedleVerifyingAuthDAO;

    @Autowired
    public PostDAO postDAO;

    @RequestMapping(value = "ThirdScheduleVerifyingAuthEmpList")
    public String thirdScheduleVerifyingAuthEmpList() {

        return "/payroll/thirdscheduleVerifyingAuth/ThirdScheduleVerifyingAuthList";

    }

    @RequestMapping(value = "getVerifyingAuthThirdScheduleEmpListJSON")
    public void getVerifyingAuthThirdScheduleEmpListJSON(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("rows") int rows, @RequestParam("page") int page,@RequestParam("offcode") String offcode) {

        response.setContentType("application/json");

        JSONObject json = new JSONObject();
        PrintWriter out = null;

        int total = 0;

        try {
            List empList = thirdSchedleVerifyingAuthDAO.getEmployeeList(lub.getLoginempid(), rows, page,offcode);
            total = thirdSchedleVerifyingAuthDAO.getEmployeeListCount(lub.getLoginempid(),offcode);

            json.put("rows", empList);
            json.put("total", total);

            out = response.getWriter();
            out.write(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "viewVerifyingAuthThirdSchedule")
    public String ViewVerifyingAuthThirdSchedule(Model model, @RequestParam("empid") String empid) {

        ThirdScheduleVerifyingAuthForm tform = null;
        
        String path = "";
        
        List postlist2nd = null;
        try {
            tform = thirdSchedleVerifyingAuthDAO.getThirdScheduleData(empid);
            
            if(tform != null && tform.getIsIASCadre().equals("Y")){
                path = "/payroll/thirdscheduleVerifyingAuth/ThirdScheduleVerifyingAuthIASView";
                postlist2nd = postDAO.getPostList(tform.getDeptCodeIAS());
            }else if(tform != null && tform.getIsUGCCadre().equals("Y")){
                path = "/payroll/thirdscheduleVerifyingAuth/ThirdScheduleVerifyingAuthViewUGC";
                postlist2nd = postDAO.getPostList(tform.getDeptCode());
            }else{
                path = "/payroll/thirdscheduleVerifyingAuth/ThirdScheduleVerifyingAuthView";
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

    @RequestMapping(value = "ThirdscheduleVerifyingAuthPDF")
    public void thirdscheduleVerifyingAuthPDF(HttpServletResponse response, @RequestParam("empid") String empid) {

        response.setContentType("application/pdf");
        Document document = new Document(PageSize.A4);
        
        ThirdScheduleVerifyingAuthForm tform = null;
        PdfWriter writer = null;
        try {
            String offName = thirdSchedleVerifyingAuthDAO.getEmployeeOfficeName(empid);
            
            response.setHeader("Content-Disposition", "attachment; filename=Third_Schedule_" + empid + ".pdf");
            
            writer = PdfWriter.getInstance(document, response.getOutputStream());
            writer.setPageEvent(new ThirdScheduleVerifyingHeaderFooter(offName,empid));
            
            document.open();
            
            tform = thirdSchedleVerifyingAuthDAO.getThirdScheduleData(empid);
            
            if(tform != null && tform.getIsIASCadre().equals("Y")){
                thirdSchedleVerifyingAuthDAO.thirdScheduleIASPDF(document, tform);
            }else if(tform != null && tform.getIsUGCCadre().equals("Y")){
                thirdSchedleVerifyingAuthDAO.thirdScheduleUGCPDF(document, empid);
            }else{
                thirdSchedleVerifyingAuthDAO.thirdSchedulePDF(document, empid);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }

    @ResponseBody
    @RequestMapping(value = "saveThirdVerifyingAuthScheduleData")
    public void SaveThirdScheduleVerifyingAuthData(HttpServletResponse response, @ModelAttribute("thirdScheduleCheckingAuthForm") ThirdScheduleVerifyingAuthForm tform, @RequestParam("approve") String approve) {

        response.setContentType("application/json");
        PrintWriter out = null;

        Message msg = null;

        try {
            msg = thirdSchedleVerifyingAuthDAO.saveThirdScheduleVerifyingAuthData(tform, approve);

            JSONObject job = new JSONObject(msg);
            out = response.getWriter();
            out.write(job.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @RequestMapping(value = "revertVerifyingAuthEmp")
    public void RevertVerifyingAuthEmp(HttpServletResponse response, @RequestParam Map<String, String> requestParams) {

        response.setContentType("application/json");
        PrintWriter out = null;

        Message msg = null;

        try {
            String chkEmp = (String) requestParams.get("chkEmp");

            

            msg = thirdSchedleVerifyingAuthDAO.revertVerifyingAuthEmp(chkEmp);

            JSONObject job = new JSONObject(msg);
            out = response.getWriter();
            out.write(job.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @ResponseBody
    @RequestMapping(value = "getVerifyingEmployeesOfficeListJSON")
    public void GetVerifyingEmployeesOfficeListJSON(HttpServletResponse response,@ModelAttribute("LoginUserBean") LoginUserBean lub) {
        
        response.setContentType("application/json");
        PrintWriter out = null;
        JSONArray json = null;
        
        try{
            List offlist = thirdSchedleVerifyingAuthDAO.getOfficeList(lub.getLoginempid());
            json = new JSONArray(offlist);
            out = response.getWriter();
            out.write(json.toString());
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            out.flush();
            out.close();
        }       
    }
}
