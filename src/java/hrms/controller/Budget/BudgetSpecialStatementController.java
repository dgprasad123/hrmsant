/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.Budget;

import hrms.dao.Budget.BudgetSpecialStatementDAO;
import hrms.dao.master.DepartmentDAO;
import hrms.model.Budget.BudgetForm;
import hrms.model.login.LoginUserBean;
import hrms.model.mergeHRMSID.mergeDuplicateHrmsidForm;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import static java.lang.System.out;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import jxl.Workbook;
import jxl.write.WritableWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Madhusmita
 */
@Controller
@SessionAttributes({"LoginUserBean", "SelectedEmpObj"})
public class BudgetSpecialStatementController {

    @Autowired
    BudgetSpecialStatementDAO budgetDao;
    @Autowired
    public DepartmentDAO deptDAO;

    public void setDeptDAO(DepartmentDAO deptDAO) {
        this.deptDAO = deptDAO;
    }

    public void setBudgetDao(BudgetSpecialStatementDAO budgetDao) {
        this.budgetDao = budgetDao;
    }

    @RequestMapping(value = "specialStatement.htm")
    public String SpecialStatement(ModelMap model, HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub,
            @ModelAttribute("BudgetForm") BudgetForm budgetform) {

        List deptlist = deptDAO.getDepartmentList();
        model.addAttribute("deptlist", deptlist);
        String path = "/Budget/SpecialStatementExcel";

        return path;
    }

    @RequestMapping(value = "downloadSpecialStatementExcel.htm")
    public void downloadSpecialStatementExcel(ModelMap model, HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub,
            @RequestParam("dept") String dept, @RequestParam("deptname") String deptname, @RequestParam("year") String year, @RequestParam("month") String month) {

        response.setContentType("application/vnd.ms-excel");
        OutputStream out = null;
        try {
            int filemonth=Integer.parseInt(month)+1;
            String fileName = "Special_Statement_Excel_" + deptname + "_" + filemonth + "_" + year + ".xls";
            out = new BufferedOutputStream(response.getOutputStream());

            WritableWorkbook workbook = Workbook.createWorkbook(out);
            budgetDao.downloadSpecialStatementExcel(out, fileName, workbook, dept, month, year);
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            workbook.write();
            workbook.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }

    }

}
