/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.misreport;

import hrms.common.CommonFunctions;
import hrms.dao.misreport.EmployeeSpecialCategoryReportDAO;
import hrms.model.login.LoginUserBean;
import hrms.model.misreport.EmployeeSpecialCategoryReport;
import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import jxl.Workbook;
import jxl.write.WritableWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author HP
 */
@Controller
@SessionAttributes({"Users", "SelectedEmpObj"})
public class EmployeeSpecialCategoryReportController {

    @Autowired
    EmployeeSpecialCategoryReportDAO empSplCatgdao;

    @RequestMapping(value = "EmployeeSpecialCategoryReport")
    public String EmployeeSpecialCategoryReport(Map<String, Object> model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("EmployeeSpecialCategoryReport") EmployeeSpecialCategoryReport empsplcategory) throws ParseException {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, 1);
        String dfrom = "";
        String dto = "";
        if (empsplcategory.getTxtperiodFrom() != null && !empsplcategory.getTxtperiodFrom().equals("")) {
            Date fdate = new SimpleDateFormat("dd-MMM-yyyy").parse(empsplcategory.getTxtperiodFrom());
            empsplcategory.setTxtperiodFrom(CommonFunctions.getFormattedOutputDate1(fdate));
        } else {
            empsplcategory.setTxtperiodFrom(CommonFunctions.getFormattedOutputDate1(c.getTime()));
            // System.out.println("Inside,From Date: "+CommonFunctions.getFormattedOutputDate1(c.getTime()));
        }
        if (empsplcategory.getTxtperiodTo() != null && !empsplcategory.getTxtperiodTo().equals("")) {
            Date fdate = new SimpleDateFormat("dd-MMM-yyyy").parse(empsplcategory.getTxtperiodTo());
            empsplcategory.setTxtperiodTo(CommonFunctions.getFormattedOutputDate1(fdate));
        } else {
            c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
            empsplcategory.setTxtperiodTo(CommonFunctions.getFormattedOutputDate1(c.getTime()));

        }
  
        List misCatgList = empSplCatgdao.searchEmployeeSpecialCategoryReportList(empsplcategory.getTxtperiodFrom(), empsplcategory.getTxtperiodTo());
        //List misCatgList = empSplCatgdao.getEmployeeSpecialCategoryReportList();
        model.put("misList", misCatgList);
        return "misreport/EmployeeSpecialCategoryReport";
    
    }

    @RequestMapping(value = "DownloadEmployeeSpecialCategoryReportExcel")
    public void DownloadEmployeeSpecialCategoryReportExcel(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub) {
        response.setContentType("application/vnd.ms-excel");
        OutputStream out = null;
        try {
            String fileName = "DeptDirectmast.xls";
            out = new BufferedOutputStream(response.getOutputStream());

            WritableWorkbook workbook = Workbook.createWorkbook(out);
            String loginid = lub.getLoginempid();
            String isddoStatus = lub.getLoginAsDDO();
            empSplCatgdao.downloadEmployeeSpecialCategoryReportExcel(out, fileName, workbook);

            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            workbook.write();
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }

    }
}
