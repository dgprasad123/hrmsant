package hrms.controller.payroll.annualincometaxreport;

import hrms.common.CommonFunctions;
import hrms.dao.payroll.annualincometaxreport.AnnualTaxReportDAOImpl;
import hrms.model.login.LoginUserBean;
import hrms.model.payroll.AnnualIncomeTax;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ModelAndView;

@Controller
@SessionAttributes({"LoginUserBean", "SelectedEmpOffice"})
public class AnnualIncomeTaxReportController implements ServletContextAware {

    @Autowired
    public AnnualTaxReportDAOImpl annualIncomeTaxDao;

    private ServletContext context;

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.context = servletContext;
    }

    @RequestMapping(value = "AnnualIncomeTaxEmployeeList", method = {RequestMethod.GET, RequestMethod.POST})
    public String getEmployeeList(Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpOffice") String selectedEmpOffice) {

        model.addAttribute("offcode", selectedEmpOffice);
        return "/payroll/annualincometaxreport/AnnualIncomeTaxReport";

    }

    @ResponseBody
    @RequestMapping(value = "GetFinYearJSON")
    public String GetFinYearJSON(HttpServletResponse response) {

        JSONArray json = null;

        try {
            List finlist = annualIncomeTaxDao.getFinyearList();
            json = new JSONArray(finlist);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return json.toString();
    }

    @ResponseBody
    @RequestMapping(value = "GetBillListJSON")
    public String GetBillListJSON(HttpServletResponse response, @RequestParam("offCode") String offcode) {

        JSONArray json = null;

        try {
            //String offcode1 = CommonFunctions.decodedTxt(offcode);
            List billlist = annualIncomeTaxDao.getBillList(offcode);
            json = new JSONArray(billlist);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return json.toString();
    }

    @ResponseBody
    @RequestMapping(value = "getEmpListJSON", method = {RequestMethod.GET, RequestMethod.POST})
    public void getEmpListJSON(HttpServletResponse response, Model model, @RequestParam("finyear") String finyear, @RequestParam("billname") String billname) {

        response.setContentType("application/json");
        JSONObject json = new JSONObject();
        PrintWriter out = null;

        try {
            List emplist = annualIncomeTaxDao.getEmployeeList(billname, finyear);

            json.put("total", 10);
            json.put("rows", emplist);
            out = response.getWriter();
            out.write(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    @RequestMapping(value = "AnnualIncomeTAXExcelReport", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView AnnualIncomeTAXExcelReport(@ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletResponse response, @RequestParam Map<String, String> requestParams) {

        String offcode = requestParams.get("offcode");
        String empId = requestParams.get("empid");        
        String fiscalYear = requestParams.get("fyear");
        ModelAndView mav = new ModelAndView("annualIncomeTaxView");            
        if (offcode == null || offcode.equals("")) {
            offcode = lub.getLoginoffcode();
        }
        AnnualIncomeTax annualIncomeTaxData = annualIncomeTaxDao.getAnnualIncomeTaxData(empId, fiscalYear);
        mav.addObject("annualIncomeTaxData", annualIncomeTaxData);
        //annualIncomeTaxDao.downloadExcel(response,empId,offcode,bgrId,fiscalYear);
        return mav;
    }

    @RequestMapping(value = "EmployeeITReport")
    public String EmployeeITReport(Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub) {
        try {
            model.addAttribute("empid", lub.getLoginempid());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/payroll/annualincometaxreport/MyAnnualITReport";
    }
}
