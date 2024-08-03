/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.misreport;

import hrms.common.CommonFunctions;
import hrms.dao.employee.EmployeeDAO;
import hrms.dao.payroll.billmast.BillMastDAO;
import hrms.model.payroll.billmast.BillStatus;
import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import javax.servlet.http.HttpServletResponse;
import jxl.Workbook;
import jxl.write.WritableWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author lenovo
 */
@Controller
public class DeptPayBillReportController {

    @Autowired
    EmployeeDAO employeeDAO;
    @Autowired
    BillMastDAO billMastDAO;

    @RequestMapping(value = "DeptWisePayBillReport")
    public ModelAndView DeptWisePayBillReport(ModelMap model, HttpServletResponse response) {
        String path = "/misreport/DeptWisePayBillReport";
        ModelAndView mav = new ModelAndView();
        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        int month = now.get(Calendar.MONTH);
        mav.addObject("selectmonth", month);
        mav.addObject("selectyear", year);
        ArrayList billDetails = billMastDAO.getDeptWisePayBillStatus(month, year);
        mav.addObject("billDetails", billDetails);
        // return "/misreport/DeptWisePayBillReport";
        mav.setViewName(path);
        return mav;

    }

    @RequestMapping(value = "getDeptWisePayBillStatus")
    public ModelAndView getDeptWisePayBillStatus(ModelMap model, HttpServletResponse response, @RequestParam("year") int year, @RequestParam("month") int month) {
        String path = "/misreport/DeptWisePayBillReport";
        ModelAndView mav = new ModelAndView();
        Calendar now = Calendar.getInstance();

        mav.addObject("selectmonth", month);
        mav.addObject("selectyear", year);
        ArrayList billDetails = billMastDAO.getDeptWisePayBillStatus(month, year);
        mav.addObject("billDetails", billDetails);
        mav.setViewName(path);
        return mav;

    }

    @RequestMapping(value = "DistWisePayBillReport")
    public ModelAndView DistWisePayBillReport(ModelMap model, HttpServletResponse response) {
        String path = "/misreport/DistWisePayBillStatus";
        ModelAndView mav = new ModelAndView();
        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        int month = now.get(Calendar.MONTH);
        mav.addObject("selectmonth", month);
        mav.addObject("selectyear", year);
        mav.addObject("typeofBill", "PAY");
        ArrayList billDetails = billMastDAO.DistWisePayBillReport(month, year, "PAY");
        mav.addObject("billDetails", billDetails);
        // return "/misreport/DeptWisePayBillReport";
        mav.setViewName(path);
        return mav;

    }

    @RequestMapping(value = "getDistWisePayBillReport")
    public ModelAndView getDistWisePayBillReport(ModelMap model, HttpServletResponse response, @RequestParam("year") int year, @RequestParam("month") int month, @RequestParam("typeofBill") String typeofBill) {
        String path = "/misreport/DistWisePayBillStatus";
        ModelAndView mav = new ModelAndView();
        Calendar now = Calendar.getInstance();

        mav.addObject("selectmonth", month);
        mav.addObject("selectyear", year);
        mav.addObject("typeofBill", typeofBill);
        ArrayList billDetails = billMastDAO.DistWisePayBillReport(month, year, typeofBill);
        mav.addObject("billDetails", billDetails);
        mav.setViewName(path);
        return mav;

    }

    @RequestMapping(value = "DistWiseOfficePayBill")
    public ModelAndView DistWiseOfficePayBill(ModelMap model, HttpServletResponse response, @RequestParam("year") int year, @RequestParam("month") int month, @RequestParam("dcode") String dcode, @RequestParam("typeofBill") String typeofBill) {
        String path = "/misreport/DistWiseOfficePayBill";
        ModelAndView mav = new ModelAndView();
        mav.addObject("selectmonth", month);
        mav.addObject("selectyear", year);
        mav.addObject("typeofBill", typeofBill);
        ArrayList billDetails = billMastDAO.DistWiseOfficePayBill(month, year, dcode,typeofBill);
        mav.addObject("billDetails", billDetails);
        // return "/misreport/DeptWisePayBillReport";
        mav.setViewName(path);
        return mav;

    }

    @RequestMapping(value = "DownlaodOfficeWiseEmployee")
    public void DownlaodOfficeWiseEmployee(ModelMap model, HttpServletResponse response, @RequestParam("year") String year, @RequestParam("month") String month, @RequestParam("ocode") String ocode) {
        response.setContentType("application/vnd.ms-excel");
        OutputStream out = null;
        try {
            
            out = new BufferedOutputStream(response.getOutputStream());
            WritableWorkbook workbook = Workbook.createWorkbook(out);
            String dYear = CommonFunctions.decodedTxt(year+"");
            String dMonth = CommonFunctions.decodedTxt(month+"");
            String dOcode = CommonFunctions.decodedTxt(ocode);
            String fileName = "empList_" + dOcode + ".xls";
            employeeDAO.DownlaodOfficeWiseEmployee(out, fileName, workbook, Integer.parseInt(dMonth), Integer.parseInt(dYear), dOcode);
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            workbook.write();
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }

    }

    @RequestMapping(value = "DeptWiseOfficePayBill")
    public ModelAndView DeptWiseOfficePayBill(ModelMap model, HttpServletResponse response, @RequestParam("year") int year, @RequestParam("month") int month, @RequestParam("dcode") String dcode) {
        String path = "/misreport/DeptWiseOfficePayBill";
        ModelAndView mav = new ModelAndView();
        mav.addObject("selectmonth", month);
        mav.addObject("selectyear", year);
        ArrayList billDetails = billMastDAO.DeptWiseOfficePayBill(month, year, dcode);
        mav.addObject("billDetails", billDetails);
        mav.setViewName(path);
        return mav;

    }

    @RequestMapping(value = "DeptWiseVoucherList")
    public ModelAndView DeptWiseVoucherList(ModelMap model, HttpServletResponse response, @ModelAttribute("BillStatus") BillStatus bstatus) {
        String path = "/misreport/DeptWiseVoucherList";
        ModelAndView mav = new ModelAndView();
        ArrayList vchDetails = billMastDAO.DeptWiseVoucherList(bstatus.getMonth(), bstatus.getYear(), bstatus.getBillType());
        mav.addObject("vchDetails", vchDetails);
        mav.setViewName(path);
        return mav;

    }
}
