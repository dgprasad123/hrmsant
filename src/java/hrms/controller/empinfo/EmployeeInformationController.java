package hrms.controller.empinfo;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import hrms.dao.empinfo.EmployeeInformationDAO;
import hrms.dao.employee.EmployeeDAO;
import hrms.dao.login.LoginDAO;
import hrms.dao.master.OfficeDAO;
import hrms.model.empinfo.EmployeeInformation;
import hrms.model.employee.Employee;
import hrms.model.login.LoginUserBean;
import hrms.model.login.Users;
import hrms.model.master.Office;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ModelAndView;

@Controller
@SessionAttributes("LoginUserBean")
public class EmployeeInformationController implements ServletContextAware {

    @Autowired
    public EmployeeInformationDAO empinfoDAO;

    @Autowired
    public LoginDAO loginDao;

    @Autowired
    public EmployeeDAO employeeDAO;

    @Autowired
    public OfficeDAO offDAO;

    private ServletContext context;
    private String checkedEmpProfile;

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.context = servletContext;
    }

    @RequestMapping(value = "GetEmployeeInformationPage")
    public String GetEmployeeInformation() {
        return "/empinfo/EmployeeInformation";
    }

    @RequestMapping(value = "GetEmployeeInformation")
    public String GetEmployeeInformation(@Valid @ModelAttribute("employeeInformation") EmployeeInformation empinfo, ModelMap model) {

        EmployeeInformation empinfo1 = null;

        try {
            if ((empinfo.getCadreCode() != null && !empinfo.getCadreCode().equals("")
                    || (empinfo.getMobile() != null && !empinfo.getMobile().equals("")))) {
                empinfoDAO.updateEmployeeData(empinfo);
            }

            empinfo1 = empinfoDAO.getEmployeeData(empinfo.getEmpid(), empinfo.getGpfno());

            model.addAttribute("empinfo", empinfo1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/empinfo/EmployeeInformation";
    }

    @RequestMapping(value = "downloadEmployeeExcel.htm", method = RequestMethod.GET)
    public ModelAndView downloadExcel(@RequestParam("offCode") String offCode) {
        // create some sample data
        List<Employee> listEmployees = employeeDAO.getOfficeWiseEmployeeList(offCode);
        //System.out.println("listEmployees"+listEmployees);
        // return a view which will be resolved by an excel view resolver
        Office ofc = offDAO.getOfficeNameDeptName(offCode);
        ModelAndView mv = new ModelAndView("excelView", "listEmployees", listEmployees);
        mv.addObject("offCode", offCode);
        mv.addObject("deptName", ofc.getDeptName());
        mv.addObject("offname", ofc.getOffEn());
        mv.addObject("dist", ofc.getDistName());
        return mv;
    }

    @RequestMapping(value = "getEmpListForLeftOutEmployee.htm", method = RequestMethod.GET)
    public ModelAndView getEmpListForLeftOutEmployee(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, @ModelAttribute("SelectedEmpOffice") String selectedEmpOffice, HttpServletResponse response) {//
        String path = "/employee/SelectEmployee";

        ModelAndView mav = new ModelAndView(path);
        //mav.addObject("empList", empList);

        return mav;

    }

    @ResponseBody
    @RequestMapping(value = "/getEmployeeDataasJson.htm")
    public void getEmployeeDataasJson(@RequestParam("gpfno") String gpfno, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = null;
        Employee emp = employeeDAO.getEmpoyeeData(gpfno);
        ArrayList emplist = new ArrayList();
        emplist.add(emp);
        JSONArray json = new JSONArray(emplist);
        out = response.getWriter();
        out.write(json.toString());
    }

    @ResponseBody
    @RequestMapping(value = "/getEmployeeDetails.htm")
    public void getEmployeeDetails(@RequestParam("hrmsid") String hrmsid, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = null;
        Employee emp = employeeDAO.getEmployeeProfile(hrmsid);
        ArrayList emplist = new ArrayList();
        emplist.add(emp);
        JSONArray json = new JSONArray(emplist);
        out = response.getWriter();
        out.write(json.toString());
    }

    @RequestMapping(value = "GetIcardDetails")
    public String GetIcardDetails(@ModelAttribute("LoginUserBean") LoginUserBean lub, @Valid @ModelAttribute("employeeInformation") EmployeeInformation empinfo, ModelMap model) {
        EmployeeInformation empinfo1 = null;
        try {
            empinfo1 = empinfoDAO.GetIcardDetails(lub.getLoginempid(), empinfo.getGpfno());
            Users emp = loginDao.getEmployeeProfileInfo(lub.getLoginempid());
            model.addAttribute("empinfo", empinfo1);
            model.addAttribute("users", emp);
            DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
            Date date = new Date();
            model.addAttribute("cdate", dateFormat.format(date));
            Calendar now = Calendar.getInstance();
            now.setTime(dateFormat.parse(dateFormat.format(date)));
            now.add(Calendar.YEAR, 4);
            String yearAdd = dateFormat.format(now.getTime());

            now.setTime(dateFormat.parse(yearAdd));
            now.add(Calendar.DATE, -1);
            String daysub = dateFormat.format(now.getTime());

            model.addAttribute("yearAdd", daysub);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/empinfo/IcardDetails";
    }

    @RequestMapping(value = "downloadIcardDetails")
    public void downloadIcardDetails(@ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletResponse response) {

        response.setContentType("application/pdf");
        Document document = null;
        document = new Document(PageSize.A4);
        PdfWriter writer = null;
        String hrmsId = "";
        try {

            if (lub.getLoginempid() != null) {
                hrmsId = lub.getLoginempid();
            }
            response.setHeader("Content-Disposition", "attachment; filename=ICard_" + hrmsId + ".pdf");
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();

            Users userBean = loginDao.getEmployeeProfileInfo(lub.getLoginempid());
            String filePath = context.getInitParameter("PhotoPath");
            empinfoDAO.getICardPDF(document, userBean, filePath);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
        //  return null;
    }

    @RequestMapping(value = "downloadIcardESignDetails")
    public void downloadIcardESignDetails(@ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletResponse response) {

        response.setContentType("application/pdf");
        Document document = null;
        document = new Document(PageSize.A4);
        PdfWriter writer = null;
        String hrmsId = "";

        try {

            if (lub.getLoginempid() != null) {
                hrmsId = lub.getLoginempid();
            }
            response.setHeader("Content-Disposition", "attachment; filename=ICard_" + hrmsId + ".pdf");
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();

            Users userBean = loginDao.getEmployeeProfileInfo(lub.getLoginempid());
            String filePath = context.getInitParameter("PhotoPath");
            empinfoDAO.getICardPDFforEsign(document, userBean, filePath);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
        //  return null;
    }

    @RequestMapping(value = "UnlockEmployeeProfile.htm")
    public ModelAndView UnlockEmployeeProfile(@Valid @ModelAttribute("unloclempprofile") EmployeeInformation unlockempprofile, ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("empid") String empid) {

        ModelAndView mav = new ModelAndView();
        try {
            mav.addObject("empid", empid);
            mav.addObject("empprofile", employeeDAO.getEmployeeProfile(empid));
            mav.setViewName("/empinfo/UnlockEmployeeProfile");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @ResponseBody
    @RequestMapping(value = "unlockEmpprofileGroup")
    public void unlockEmpprofileGroup(ModelMap model, HttpServletRequest request, HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub,
            @RequestParam("empId") String empId, @RequestParam("checkedEmpProfile") String checkedEmpProfile) {
        PrintWriter out = null;
        try {
            response.setContentType("application/json");
            employeeDAO.profileCompletedLog(empId, lub.getLoginempid(), lub.getLoginempid());
            String status = empinfoDAO.unlockEmpprofileGroup(checkedEmpProfile, empId);

            out = response.getWriter();
            JSONObject obj = new JSONObject();
            obj.append("status", status);
            out.write(obj.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    @ResponseBody
    @RequestMapping(value = "getCheckedEmpIdentityProfile")
    public void getCheckedEmpIdentityProfile(ModelMap model, HttpServletRequest request, HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub,
            @RequestParam("empId") String empId) {
        PrintWriter out = null;
        try {
            response.setContentType("application/json");
            String msg = empinfoDAO.getCheckedEmpIdentityProfile(empId);
            out = response.getWriter();
            JSONObject obj = new JSONObject();
            obj.append("msg", msg);
            out.write(obj.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    @ResponseBody
    @RequestMapping(value = "getCheckedEmpLanguageProfile")
    public void getCheckedEmpLanguageProfile(ModelMap model, HttpServletRequest request, HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub,
            @RequestParam("empId") String empId) {
        PrintWriter out = null;
        try {
            response.setContentType("application/json");
            String msg = empinfoDAO.getCheckedEmpLanguageProfile(empId);
            out = response.getWriter();
            JSONObject obj = new JSONObject();
            obj.append("msg", msg);
            out.write(obj.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    @ResponseBody
    @RequestMapping(value = "getCheckedEmpQualificationProfile")
    public void getCheckedEmpQualificationProfile(ModelMap model, HttpServletRequest request, HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub,
            @RequestParam("empId") String empId) {
        PrintWriter out = null;
        try {
            response.setContentType("application/json");
            String msg = empinfoDAO.getCheckedEmpQualificationProfile(empId);
            out = response.getWriter();
            JSONObject obj = new JSONObject();
            obj.append("msg", msg);
            out.write(obj.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    @ResponseBody
    @RequestMapping(value = "getCheckedEmpFamilyProfile")
    public void getCheckedEmpFamilyProfile(ModelMap model, HttpServletRequest request, HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub,
            @RequestParam("empId") String empId) {
        PrintWriter out = null;
        try {
            response.setContentType("application/json");
            String msg = empinfoDAO.getCheckedEmpFamilyProfile(empId);
            out = response.getWriter();
            JSONObject obj = new JSONObject();
            obj.append("msg", msg);
            out.write(obj.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    @ResponseBody
    @RequestMapping(value = "getCheckedEmpAddressProfile")
    public void getCheckedEmpAddressProfile(ModelMap model, HttpServletRequest request, HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub,
            @RequestParam("empId") String empId) {
        PrintWriter out = null;
        try {
            response.setContentType("application/json");
            String msg = empinfoDAO.getCheckedEmpAddressProfile(empId);
            out = response.getWriter();
            JSONObject obj = new JSONObject();
            obj.append("msg", msg);
            out.write(obj.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

}
