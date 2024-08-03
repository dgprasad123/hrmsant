/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.importemployee;

import hrms.common.DataBaseFunctions;
import hrms.dao.createEmployee.CreateEmployeeDAO;
import hrms.dao.employee.EmployeeDAO;
import hrms.dao.importemployee.ImportEmpDetailsExcelDAO;
import hrms.model.createEmployee.CreateEmployee;
import hrms.model.employee.Employee;
import hrms.model.login.LoginUserBean;
import java.awt.print.Book;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Madhusmita
 */
@Controller
public class ImportEmpDetailsExcelController {

    @Autowired
    public EmployeeDAO employeeDAO;
    @Autowired
    public CreateEmployeeDAO createEmployeeDao;
    @Autowired
    public ImportEmpDetailsExcelDAO importEmpExlDao;

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @RequestMapping(value = "loadEmployeXL")
    public ModelAndView loadEmpDetailsExcel(ModelMap model, HttpServletResponse response, @ModelAttribute("importEmpExcelData") CreateEmployee ceBean, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result) throws IOException, BiffException {
        String path = "/employee/ImportEmployee";
        ModelAndView mav = new ModelAndView();
        mav.setViewName(path);
        return mav;
    }

    @RequestMapping(value = "importEmployeXL")
    public ModelAndView ImportEmpDetailsExcel(ModelMap model, HttpServletResponse response, @ModelAttribute("importEmpExcelData") CreateEmployee ceBean, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result) throws IOException, BiffException, SQLException {
        String path = "/employee/ImportEmployee";
        ModelAndView mav = new ModelAndView();
        Workbook workbook = null;

        if (!ceBean.getDocumentFile().isEmpty()) {
            if (!ceBean.getDocumentFile().isEmpty() && ceBean.getDocumentFile() != null) {
                try {
                    
                    if (ceBean.getDocumentFile().getOriginalFilename().endsWith(".xls")) {
                        InputStream inputStream = null;
                        OutputStream outputStream = null;
                        List li = new ArrayList();
                        inputStream = ceBean.getDocumentFile().getInputStream();
                        workbook = Workbook.getWorkbook(inputStream);
                        importEmpExlDao.createEmployees(workbook);
                        model.addAttribute("imprtMsg", "File : " + ceBean.getDocumentFile().getOriginalFilename() + " Imported Successfully");
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (BiffException e) {
                    e.printStackTrace();
                } finally {

                    if (workbook != null) {
                        workbook.close();
                    }

                }

            } 
        }
        mav.setViewName(path);
        
        return mav;

    }
}
