/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.absenteestmt;

import hrms.SelectOption;
import hrms.common.CalendarCommonMethods;
import hrms.dao.absenteestmt.EmpAbsenteeDAO;
import hrms.model.absentee.Absentee;
import hrms.model.absentee.ExcelImportBean;
import hrms.model.login.LoginUserBean;
import hrms.model.login.Users;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Manas Jena
 */
@Controller
@SessionAttributes({"LoginUserBean", "SelectedEmpObj"})
public class EmpAbsenteeStmtController {

    @Autowired
    EmpAbsenteeDAO empAbsenteeDAO;

    @RequestMapping(value = "employeeAbsenteeAction", method = RequestMethod.GET)
    public ModelAndView employeeAbsenteeAction(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("absentee") Absentee absentee) {//
        ModelAndView mv = new ModelAndView("/absentee/EmployeeAbsentee");
        List yearlist = empAbsenteeDAO.getAbseneteeYear(selectedEmpObj.getEmpId());
        mv.addObject("yearlist", yearlist);
        return mv;
    }

    @RequestMapping(value = "getAbseneteeList", params = "action=Ok")
    public ModelAndView getAbseneteeList(HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("absentee") Absentee absentee, @RequestParam Map<String, String> requestParams) throws IOException {
        ModelAndView mv = new ModelAndView("/absentee/EmployeeAbsentee");

        String status = requestParams.get("status");
        mv.addObject("status", status);

        List yearlist = empAbsenteeDAO.getAbseneteeYear(selectedEmpObj.getEmpId());
        mv.addObject("yearlist", yearlist);
        List absenteelist = empAbsenteeDAO.getAbseneteeList(selectedEmpObj.getEmpId(), absentee.getSltyear(), absentee.getSltmonth());
        mv.addObject("absenteelist", absenteelist);
        return mv;
    }

    @RequestMapping(value = "editEmployeeAbsentee", method = RequestMethod.GET)
    public ModelAndView editEmployeeAbsentee(@ModelAttribute("absentee") Absentee absentee, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpObj") Users selectedEmpObj) {

        ModelAndView mv = null;

        List yearlist = empAbsenteeDAO.getAbseneteeYear(selectedEmpObj.getEmpId());

        absentee = empAbsenteeDAO.getAbsenteeDetail(Integer.parseInt(absentee.getAbsid()));
        
        mv = new ModelAndView("/absentee/EmployeeAbsenteeDetail", "absentee", absentee);
        mv.addObject("yearlist", yearlist);
        return mv;
    }

    @RequestMapping(value = "getAbseneteeList", method = RequestMethod.POST, params = "action=New Absentee")
    public ModelAndView newAbsenetee(@ModelAttribute("absentee") Absentee absentee, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpObj") Users selectedEmpObj) {
        ModelAndView mv = new ModelAndView("/absentee/EmployeeAbsenteeDetail");
        List yearlist = empAbsenteeDAO.getAbseneteeYear(selectedEmpObj.getEmpId());
        mv.addObject("yearlist", yearlist);

        return mv;
    }

    @RequestMapping(value = "saveEmployeeAbsentee", method = RequestMethod.POST, params = "action=Cancel")
    public String cancelEmployeeAbsentee(@ModelAttribute("absentee") Absentee absentee, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpObj") Users selectedEmpObj) {
        
        return "redirect:/getAbseneteeList.htm?action=Ok&sltyear=" + absentee.getSltyear() + "&sltmonth=" + absentee.getSltmonth();
    }

    @RequestMapping(value = "saveEmployeeAbsentee", method = RequestMethod.POST, params = "action=Save")
    public ModelAndView saveEmployeeAbsentee(@ModelAttribute("absentee") Absentee absentee, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpObj") Users selectedEmpObj) throws IOException {
        absentee.setEmpId(selectedEmpObj.getEmpId());
        absentee.setEntempId(lub.getLoginempid());
        if(absentee.getAbsid() != null && !absentee.getAbsid().equals("")){
            empAbsenteeDAO.updateAbsenteeData(absentee);
        }else{
            empAbsenteeDAO.saveAbsenteeData(absentee);
        }

        ModelAndView mv = new ModelAndView("/absentee/EmployeeAbsentee");
        List yearlist = empAbsenteeDAO.getAbseneteeYear(selectedEmpObj.getEmpId());
        mv.addObject("yearlist", yearlist);
        return mv;

    }

    @RequestMapping(value = "importAbsenteeExcel")
    public ModelAndView importAbsenteeExcel(@ModelAttribute("ExcelImport") ExcelImportBean eBean, @ModelAttribute("LoginUserBean") LoginUserBean lub) throws IOException {
        String path = "/absentee/ImportAbsenteeExcel";
        List li = new ArrayList();
        SelectOption so = new SelectOption();
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH);
        int previousYear = year - 1;
        so.setLabel(year + "");
        so.setValue(year + "");
        li.add(so);
        SelectOption so1 = new SelectOption();
        so1.setLabel(previousYear + "");
        so1.setValue(previousYear + "");
        li.add(so1);
        eBean.setAmonth(month + "");
        boolean isExisting = empAbsenteeDAO.countMonthAttendance(lub.getLoginoffcode(), month, year);
        ModelAndView mv = new ModelAndView(path, "ExcelImport", eBean);
        mv.addObject("isExisting", isExisting);
        mv.addObject("yearList", li);
        //mv.setViewName(path);
        return mv;
    }

    @RequestMapping(value = "UploadAbsenteeExcel")
    public String UploadAbsenteeExcel(@ModelAttribute("ExcelImport") ExcelImportBean eBean, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result) throws IOException, BiffException {
        ModelAndView mv = new ModelAndView();
        MultipartFile documentFile = eBean.getDocumentFile();
        boolean isExisting = empAbsenteeDAO.countMonthAttendance(lub.getLoginoffcode(), Integer.parseInt(eBean.getAmonth()), Integer.parseInt(eBean.getAyear()));
        String path = null;
        Workbook workbook = null;
        if (isExisting == false) {
            if (!documentFile.isEmpty()) {
                if (documentFile != null && !documentFile.isEmpty()) {

                    try {
                        if (documentFile.getOriginalFilename().endsWith("xlsx")) {

                        }
                        if (documentFile.getOriginalFilename().endsWith("xls")) {

                            InputStream inputStream = null;
                            OutputStream outputStream = null;
                            List li = new ArrayList();
                            inputStream = documentFile.getInputStream();
                            workbook = Workbook.getWorkbook(inputStream);
                            empAbsenteeDAO.addExcelRowIntoDB(workbook, li, Integer.parseInt(eBean.getAmonth()), Integer.parseInt(eBean.getAyear()), lub.getLoginoffcode());
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
                } else {
                    throw new IllegalArgumentException("The specified file is not Excel file");
                }
            }

            path = "redirect:/ImportSuccess.htm?month=" + eBean.getAmonth() + "&year=" + eBean.getAyear();
        } else {
            path = "redirect:/ImportFailure.htm?month=" + eBean.getAmonth() + "&year=" + eBean.getAyear();
        }

        return path;
    }

    @RequestMapping(value = "ImportSuccess", method = RequestMethod.GET)
    public ModelAndView ImportSuccess(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("year") String year, @RequestParam("month") String month, BindingResult result, HttpServletResponse response) {//
        ModelAndView mv = new ModelAndView();
        List li = empAbsenteeDAO.getAttendanceList(lub.getLoginoffcode(), Integer.parseInt(year), Integer.parseInt(month));
        mv.addObject("attendanceList", li);
        String path = "/absentee/ImportSuccess";
        mv.setViewName(path);
        return mv;
    }

    @RequestMapping(value = "ImportFailure", method = RequestMethod.GET)
    public ModelAndView ImportFailure(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("year") String year, @RequestParam("month") String month, BindingResult result, HttpServletResponse response) {//
        ModelAndView mv = new ModelAndView();
        String offCode = lub.getLoginoffcode();
        boolean isExisting = empAbsenteeDAO.countMonthAttendance(lub.getLoginoffcode(), Integer.parseInt(month), Integer.parseInt(year));
        List li = empAbsenteeDAO.getAttendanceList(offCode, Integer.parseInt(year), Integer.parseInt(month));
        mv.addObject("attendanceList", li);
        mv.addObject("month", month);
        mv.addObject("year", year);
        mv.addObject("strMonth", CalendarCommonMethods.getFullMonthAsString(Integer.parseInt(month)));
        mv.addObject("isExisting", isExisting);
        String path = "/absentee/ImportFailure";
        mv.setViewName(path);
        return mv;
    }

    @RequestMapping(value = "DeleteAttendanceList", method = RequestMethod.GET)
    public void DeleteAttendanceList(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("year") String year, @RequestParam("month") String month, BindingResult result, HttpServletResponse response) {//
        String offCode = lub.getLoginoffcode();
        empAbsenteeDAO.deleteAttendanceList(offCode, Integer.parseInt(year), Integer.parseInt(month));
    }

    @RequestMapping(value = "DeleteAbsenteeList")
    public String DeleteAbsenteeList(ModelMap model, @ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @RequestParam("year") String year, @RequestParam("month") String month, @RequestParam("absid") int absid) {

        String status = "";
        Calendar cal=Calendar.getInstance();
        cal.set(Calendar.YEAR,Integer.parseInt(year));
        cal.set(Calendar.MONTH, (Integer.parseInt(month)));
        boolean tobeDelete=false;
        
        int noofDaysinThatMonth=cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        
        int noofdaysDrawnSalary = empAbsenteeDAO.isBilledEmployee(selectedEmpObj.getEmpId(), Integer.parseInt(year), Integer.parseInt(month));
        
        int noofDaysinAbsent=empAbsenteeDAO.getnoofdaysInLeave(selectedEmpObj.getEmpId(), Integer.parseInt(year), Integer.parseInt(month));
        
        int noofdaystoBeDelete=empAbsenteeDAO.getnoofDaysTobeDelete(selectedEmpObj.getEmpId(), absid);
        
        int absenteePeriodinAquitance=noofDaysinThatMonth-noofdaysDrawnSalary;
        
        
        
        
        if(noofdaysDrawnSalary==0){
            tobeDelete=true;
        }else if(noofdaysDrawnSalary>0){
            if(noofDaysinAbsent>=noofdaystoBeDelete && absenteePeriodinAquitance>=noofdaystoBeDelete){
                tobeDelete=true;
            }
        }
        
        if (tobeDelete ) {
            empAbsenteeDAO.deleteAbsenteeList(selectedEmpObj.getEmpId(), absid);
        } else {
            status = "Unable to delete as Bill has already been processed for the following period.";
        }

        return "redirect:/getAbseneteeList.htm?action=Ok&sltyear=" + year + "&sltmonth=" + month+"&status="+status;
    }
}
