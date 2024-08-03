/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.servicebook;

import hrms.common.CommonFunctions;
import hrms.dao.SBMonitoring.SBMonitoringDAO;
import hrms.dao.master.DepartmentDAO;
import hrms.dao.master.DistrictDAO;
import hrms.model.employee.EmployeeCadre;
import hrms.model.login.LoginUserBean;
import hrms.model.servicebook.esbListBean;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import jxl.Workbook;
import jxl.write.WritableWorkbook;
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

/**
 *
 * @author Manoj PC
 */
@Controller
@SessionAttributes({"LoginUserBean"})
public class SBMonitoringController implements ServletContextAware {

    @Autowired
    SBMonitoringDAO SBMonitoringDAO;

    @Autowired
    public DepartmentDAO deptDAO;

    @Autowired
    public DistrictDAO districtDAO;

    private ServletContext context;

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.context = servletContext;
    }

    @RequestMapping(value = "eSBDeptWiseList.htm", method = RequestMethod.GET)
    public ModelAndView eSBDeptWiseList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("esbListBean") esbListBean eBean, @RequestParam Map<String, String> requestParams) {
        ModelAndView mv = new ModelAndView("/servicebook/eSBDeptWiseList", "esbListBean", eBean);
        try {
            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
            String startTime = dateFormat.format(cal.getTime());
            Date fromDate1 = dateFormat.parse(startTime);
            Date toDate1 = dateFormat.parse(startTime);
            Date dFromDate = null;
            Date dToDate = null;
            String displayFromDate = "";
            String displayToDate = "";
            String fromDate = requestParams.get("fromDate");
            String toDate = requestParams.get("toDate");
            if (fromDate == null || fromDate.equals("")) {
                displayFromDate = CommonFunctions.getFormattedOutputDate1(fromDate1);
            } else {
                displayFromDate = fromDate;
            }
            if (toDate == null || toDate.equals("")) {
                displayToDate = CommonFunctions.getFormattedOutputDate1(toDate1);
            } else {
                displayToDate = toDate;
            }

            mv.addObject("fromDate", displayFromDate);
            mv.addObject("toDate", displayToDate);
            List li = SBMonitoringDAO.getSBDeptWiseList(displayFromDate, displayToDate);
            mv.addObject("deptList", li);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mv;
    }

    @RequestMapping(value = "empCadreMIS.htm", method = RequestMethod.GET)
    public ModelAndView empCadreMIS(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("empCadreBean") EmployeeCadre eBean, @RequestParam Map<String, String> requestParams) {
        ModelAndView mv = new ModelAndView("/servicebook/EmpCadreMIS", "esbListBean", eBean);
        try {
            String deptCode = requestParams.get("deptCode");
            
            if(deptCode != null && !deptCode.equals(""))
            {
                
            }
            else
            {
                deptCode = lub.getLogindeptcode();
            }
            System.out.println("Department Code: "+deptCode);
            List li = SBMonitoringDAO.getEmpCadreList(deptCode);
            List deptlist = deptDAO.getDepartmentList();
            mv.addObject("deptlist", deptlist);            
            mv.addObject("cadreList", li);
            mv.addObject("deptName", lub.getLogindeptname());
            mv.addObject("deptCode", deptCode);
            mv.addObject("ldeptCode", lub.getLogindeptcode());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mv;
    }
    @RequestMapping(value = "eSBDeptWiseRetireList.htm", method = RequestMethod.GET)
    public ModelAndView eSBDeptWiseRetireList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("esbListBean") esbListBean eBean, @RequestParam Map<String, String> requestParams) {
        ModelAndView mv = new ModelAndView("/servicebook/eSBDeptWiseRetireList", "esbListBean", eBean);
        Calendar c = Calendar.getInstance();

        int currentYear = c.get(Calendar.YEAR);
        String year = requestParams.get("year");
        if (year == null || year.equals("")) {
            year = currentYear + "";
        }
        List li = SBMonitoringDAO.getSBDeptWiseRetireList(year, lub.getLogindeptcode());
        mv.addObject("deptList", li);
        mv.addObject("year", year);
        mv.addObject("deptCode", lub.getLogindeptcode());
        return mv;
    }

    @RequestMapping(value = "CadreWiseSBReport.htm", method = RequestMethod.GET)
    public ModelAndView CadreWiseSBReport(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("esbListBean") esbListBean eBean, @RequestParam Map<String, String> requestParams) {
        ModelAndView mv = new ModelAndView("/servicebook/CadreWiseSBReport", "esbListBean", eBean);
        Calendar c = Calendar.getInstance();

        int currentYear = c.get(Calendar.YEAR);
        String year = requestParams.get("year");
        if (year == null || year.equals("")) {
            year = currentYear + "";
        }
        List li = SBMonitoringDAO.getCadreWiseRetireList(year);
        mv.addObject("deptList", li);
        mv.addObject("year", year);
        mv.addObject("deptCode", lub.getLogindeptcode());
        return mv;
    }

    @RequestMapping(value = "eSBDistrictWiseRetireList.htm", method = RequestMethod.GET)
    public ModelAndView eSBDistrictWiseRetireList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("esbListBean") esbListBean eBean, @RequestParam Map<String, String> requestParams) {
        ModelAndView mv = new ModelAndView("/servicebook/eSBDistrictWiseRetireList", "esbListBean", eBean);
        Calendar c = Calendar.getInstance();

        int currentYear = c.get(Calendar.YEAR);
        String year = requestParams.get("year");
        if (year == null || year.equals("")) {
            year = currentYear + "";
        }
        List li = SBMonitoringDAO.getSBDistrictWiseRetireList(year, lub.getLogindeptcode());
        mv.addObject("deptList", li);
        mv.addObject("year", year);
        mv.addObject("deptCode", lub.getLogindeptcode());
        return mv;
    }

    @RequestMapping(value = "eSBOfficeWiseList.htm", method = RequestMethod.GET)
    public ModelAndView eSBOfficeWiseList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("esbListBean") esbListBean eBean, @RequestParam Map<String, String> requestParams) {
        ModelAndView mv = new ModelAndView("/servicebook/eSBOfficeWiseList", "esbListBean", eBean);
        try {
            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
            String startTime = dateFormat.format(cal.getTime());
            Date fromDate1 = dateFormat.parse(startTime);
            Date toDate1 = dateFormat.parse(startTime);
            Date dFromDate = null;
            Date dToDate = null;
            String displayFromDate = "";
            String displayToDate = "";
            String fromDate = requestParams.get("fromDate");
            String toDate = requestParams.get("toDate");
            String deptCode = requestParams.get("deptCode");
            if (fromDate == null || fromDate.equals("")) {
                displayFromDate = CommonFunctions.getFormattedOutputDate1(fromDate1);
            } else {
                displayFromDate = fromDate;
            }
            if (toDate == null || toDate.equals("")) {
                displayToDate = CommonFunctions.getFormattedOutputDate1(toDate1);
            } else {
                displayToDate = toDate;
            }

            mv.addObject("fromDate", displayFromDate);
            mv.addObject("toDate", displayToDate);
            List li = SBMonitoringDAO.getSBOfficeWiseList(displayFromDate, displayToDate, deptCode);
            mv.addObject("officeList", li);
            mv.addObject("deptCode", deptCode);
            mv.addObject("deptName", deptDAO.getDeptName(deptCode));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mv;
    }

    @RequestMapping(value = "eSBOfficeWiseRetireList.htm", method = RequestMethod.GET)
    public ModelAndView eSBOfficeWiseRetireList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("esbListBean") esbListBean eBean, @RequestParam Map<String, String> requestParams) {
        ModelAndView mv = new ModelAndView("/servicebook/eSBOfficeWiseRetireList", "esbListBean", eBean);
        try {
            String deptCode = requestParams.get("deptCode");
            Calendar c = Calendar.getInstance();

            int currentYear = c.get(Calendar.YEAR);
            String year = requestParams.get("year");
            if (year == null || year.equals("")) {
                year = currentYear + "";
            }
            List li = SBMonitoringDAO.getSBOfficeWiseRetireList(deptCode, year);
            mv.addObject("officeList", li);
            mv.addObject("deptCode", deptCode);
            mv.addObject("deptName", deptDAO.getDeptName(deptCode));
            mv.addObject("year", year);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mv;
    }

    @RequestMapping(value = "eSBOfficeWiseDetailList.htm", method = RequestMethod.GET)
    public ModelAndView eSBOfficeWiseDetailList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("esbListBean") esbListBean eBean, @RequestParam Map<String, String> requestParams) {
        ModelAndView mv = new ModelAndView("/servicebook/eSBOfficeWiseDetailList", "esbListBean", eBean);
        try {
            String deptCode = requestParams.get("deptCode");
            Calendar c = Calendar.getInstance();

            int currentYear = c.get(Calendar.YEAR);
            String year = requestParams.get("year");
            if (year == null || year.equals("")) {
                year = currentYear + "";
            }
            List li = SBMonitoringDAO.getSBOfficeWiseDetailList(deptCode, year);
            mv.addObject("officeList", li);
            mv.addObject("deptCode", deptCode);
            mv.addObject("deptName", deptDAO.getDeptName(deptCode));
            mv.addObject("curDate", CommonFunctions.getFormattedOutputDate1(new Date()));
            mv.addObject("year", year);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mv;
    }

    @RequestMapping(value = "eSBDistOfficeWiseRetireList.htm", method = RequestMethod.GET)
    public ModelAndView eSBDistOfficeWiseRetireList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("esbListBean") esbListBean eBean, @RequestParam Map<String, String> requestParams) {
        ModelAndView mv = new ModelAndView("/servicebook/eSBDistOfficeWiseRetireList", "esbListBean", eBean);
        try {
            String distCode = requestParams.get("distCode");
            Calendar c = Calendar.getInstance();

            int currentYear = c.get(Calendar.YEAR);
            String year = requestParams.get("year");
            if (year == null || year.equals("")) {
                year = currentYear + "";
            }
            List li = SBMonitoringDAO.getSBDistOfficeWiseRetireList(distCode, year, lub.getLogindeptcode());
            mv.addObject("officeList", li);
            mv.addObject("distCode", distCode);
            mv.addObject("year", year);
            mv.addObject("deptCode", lub.getLogindeptcode());
            //mv.addObject("deptName", deptDAO.getDeptName(deptCode));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mv;
    }

    @RequestMapping(value = "eSBDCOfficeWiseRetireList.htm", method = RequestMethod.GET)
    public ModelAndView eSBDCOfficeWiseRetireList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("esbListBean") esbListBean eBean, @RequestParam Map<String, String> requestParams) {
        ModelAndView mv = new ModelAndView("/servicebook/eSBDCWiseRetireList", "esbListBean", eBean);
        try {
            String distCode = lub.getLoginempid();
            //System.out.println("Dist Code:"+lub.getLoginempid());
            Calendar c = Calendar.getInstance();

            int currentYear = c.get(Calendar.YEAR);
            String year = requestParams.get("year");
            if (year == null || year.equals("")) {
                year = currentYear + "";
            }
            List li = SBMonitoringDAO.getSBDCWiseRetireList(distCode, year);
            mv.addObject("officeList", li);
            mv.addObject("distCode", distCode);
            mv.addObject("year", year);
            mv.addObject("deptCode", lub.getLogindeptcode());
            //mv.addObject("deptName", deptDAO.getDeptName(deptCode));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mv;
    }

    @RequestMapping(value = "eSBEmpWiseList.htm", method = RequestMethod.GET)
    public ModelAndView eSBEmpWiseList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("esbListBean") esbListBean eBean, @RequestParam Map<String, String> requestParams) {
        ModelAndView mv = new ModelAndView("/servicebook/eSBEmpWiseList", "esbListBean", eBean);
        try {
            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
            String startTime = dateFormat.format(cal.getTime());
            Date fromDate1 = dateFormat.parse(startTime);
            Date toDate1 = dateFormat.parse(startTime);
            Date dFromDate = null;
            Date dToDate = null;
            String displayFromDate = "";
            String displayToDate = "";
            String fromDate = requestParams.get("fromDate");
            String toDate = requestParams.get("toDate");
            String deptCode = requestParams.get("deptCode");
            String offCode = requestParams.get("offCode");
            if (fromDate == null || fromDate.equals("")) {
                displayFromDate = CommonFunctions.getFormattedOutputDate1(fromDate1);
            } else {
                displayFromDate = fromDate;
            }
            if (toDate == null || toDate.equals("")) {
                displayToDate = CommonFunctions.getFormattedOutputDate1(toDate1);
            } else {
                displayToDate = toDate;
            }

            mv.addObject("fromDate", displayFromDate);
            mv.addObject("toDate", displayToDate);
            List li = SBMonitoringDAO.getSBEmpWiseList(displayFromDate, displayToDate, offCode);
            mv.addObject("officeList", li);
            mv.addObject("deptCode", deptCode);
            mv.addObject("offCode", offCode);
            mv.addObject("deptName", deptDAO.getDeptName(deptCode));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mv;
    }

    @RequestMapping(value = "eSBEmpWiseRetireList.htm", method = RequestMethod.GET)
    public ModelAndView eSBEmpWiseRetireList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("esbListBean") esbListBean eBean, @RequestParam Map<String, String> requestParams) {
        ModelAndView mv = new ModelAndView("/servicebook/eSBEmpWiseRetireList", "esbListBean", eBean);
        try {

            String deptCode = requestParams.get("deptCode");
            String offCode = requestParams.get("offCode");
            Calendar c = Calendar.getInstance();

            int currentYear = c.get(Calendar.YEAR);
            String year = requestParams.get("year");
            if (year == null || year.equals("")) {
                year = currentYear + "";
            }
            List li = SBMonitoringDAO.getSBEmpWiseRetireList(offCode, year);
            mv.addObject("officeList", li);
            mv.addObject("deptCode", deptCode);
            mv.addObject("offCode", offCode);
            mv.addObject("deptName", deptDAO.getDeptName(deptCode));
            mv.addObject("year", year);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mv;
    }

    @RequestMapping(value = "eSBOffEmpWiseRetireList.htm", method = RequestMethod.GET)
    public ModelAndView eSBOffEmpWiseRetireList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("esbListBean") esbListBean eBean, @RequestParam Map<String, String> requestParams) {
        ModelAndView mv = new ModelAndView("/servicebook/eSBOffEmpWiseRetireList", "esbListBean", eBean);
        try {

            String deptCode = requestParams.get("deptCode");
            String offCode = requestParams.get("offCode");
            String strType = requestParams.get("type");
            Calendar c = Calendar.getInstance();

            int currentYear = c.get(Calendar.YEAR);
            String year = requestParams.get("year");
            if (year == null || year.equals("")) {
                year = currentYear + "";
            }
            List li = SBMonitoringDAO.getSBOffWiseRetireList(offCode, year, strType);
            mv.addObject("officeList", li);
            mv.addObject("deptCode", deptCode);
            mv.addObject("offCode", offCode);
            mv.addObject("deptName", deptDAO.getDeptName(deptCode));
            mv.addObject("year", year);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mv;
    }

    @RequestMapping(value = "eSBOffAllEmpWiseRetireList.htm", method = RequestMethod.GET)
    public ModelAndView eSBOffAllEmpWiseRetireList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("esbListBean") esbListBean eBean, @RequestParam Map<String, String> requestParams) {
        ModelAndView mv = new ModelAndView("/servicebook/eSBOffAllEmpWiseRetireList", "esbListBean", eBean);
        try {

            String deptCode = requestParams.get("deptCode");
            String offCode = requestParams.get("offCode");
            String strType = requestParams.get("type");
            Calendar c = Calendar.getInstance();

            int currentYear = c.get(Calendar.YEAR);
            String year = requestParams.get("year");
            if (year == null || year.equals("")) {
                year = currentYear + "";
            }
            List li = SBMonitoringDAO.getSBOffWiseAllRetireList(offCode, year, strType);
            mv.addObject("officeList", li);
            mv.addObject("deptCode", deptCode);
            mv.addObject("offCode", offCode);
            mv.addObject("deptName", deptDAO.getDeptName(deptCode));
            mv.addObject("year", year);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mv;
    }

    @RequestMapping(value = "eSBDeptEmpWiseRetireList.htm", method = RequestMethod.GET)
    public ModelAndView eSBDeptEmpWiseRetireList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("esbListBean") esbListBean eBean, @RequestParam Map<String, String> requestParams) {
        ModelAndView mv = new ModelAndView("/servicebook/eSBDeptEmpWiseRetireList", "esbListBean", eBean);
        try {

            String deptCode = requestParams.get("deptCode");
            String offCode = requestParams.get("offCode");
            String strType = requestParams.get("type");
            Calendar c = Calendar.getInstance();

            int currentYear = c.get(Calendar.YEAR);
            String year = requestParams.get("year");
            if (year == null || year.equals("")) {
                year = currentYear + "";
            }
            List li = SBMonitoringDAO.getSBDeptWiseRetireList(deptCode, year, strType);
            mv.addObject("officeList", li);
            mv.addObject("deptCode", deptCode);
            mv.addObject("offCode", offCode);
            mv.addObject("deptName", deptDAO.getDeptName(deptCode));
            mv.addObject("year", year);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mv;
    }

    @RequestMapping(value = "eSBDistEmpWiseRetireList.htm", method = RequestMethod.GET)
    public ModelAndView eSBDistEmpWiseRetireList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("esbListBean") esbListBean eBean, @RequestParam Map<String, String> requestParams) {
        ModelAndView mv = new ModelAndView("/servicebook/eSBDistEmpWiseRetireList", "esbListBean", eBean);
        try {

            String distCode = requestParams.get("distCode");
            String offCode = requestParams.get("offCode");

            Calendar c = Calendar.getInstance();

            int currentYear = c.get(Calendar.YEAR);
            String year = requestParams.get("year");
            if (year == null || year.equals("")) {
                year = currentYear + "";
            }
            List li = SBMonitoringDAO.getSBEmpWiseRetireList(offCode, year);
            mv.addObject("officeList", li);
            mv.addObject("distCode", distCode);
            mv.addObject("offCode", offCode);
            mv.addObject("year", year);
            //mv.addObject("deptName", deptDAO.getDeptName(deptCode));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mv;
    }

    @RequestMapping(value = "TrackCompletedSB")
    public ModelAndView trackCompletedSB(@ModelAttribute("LoginUserBean") LoginUserBean lub) {

        List list = new ArrayList();
        ModelAndView mav = new ModelAndView("/servicebook/TrackCompletedSB");

        try {
            //Get Office Code
            String offCode = SBMonitoringDAO.getOffCode(lub.getLoginempid());
            list = SBMonitoringDAO.getDOSEmpList(offCode);
            mav.addObject("EmpList", list);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return mav;
    }

    @ResponseBody
    @RequestMapping(value = "UpdateServiceBookStatus", method = {RequestMethod.GET, RequestMethod.POST})
    public void updateServiceBookStatus(ModelMap model,
            @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response,
            @RequestParam("empId") String empId, @RequestParam("sbStatus") String sbStatus) {

        response.setContentType("text/html");
        PrintWriter out = null;

        try {
            if (sbStatus.equals("Y")) {
                String validationcomplete = SBMonitoringDAO.getServiceBookValidationStatus(empId);
                if (validationcomplete.equals("Y")) {
                    SBMonitoringDAO.updateSBUpdateStatus(empId, sbStatus);
                }
                out = response.getWriter();
                out.write(validationcomplete);
            } else {
                SBMonitoringDAO.updateSBUpdateStatus(empId, sbStatus);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "downloadExcelDeptwise", method = {RequestMethod.GET, RequestMethod.POST})
    public void downloadeSBDeptWiseRetireList(@ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletResponse response,
            @ModelAttribute("esbListBean") esbListBean eBean, @RequestParam Map<String, String> requestParams)
            throws IndexOutOfBoundsException, IOException {
        response.setContentType("application/vnd.ms-excel");
        OutputStream out = null;
        String fileName = null;
        try {
            fileName = "Service Book Monitoring Department Wise Report" + ".xls";
            out = new BufferedOutputStream(response.getOutputStream());
            WritableWorkbook workbook = Workbook.createWorkbook(out);
            SBMonitoringDAO.downloadDeptWiseSBUpdatedReport(out, fileName, workbook);
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            workbook.write();
            workbook.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @RequestMapping(value = "downloadeSBOfficeWiseRetireList", method = {RequestMethod.GET, RequestMethod.POST})
    public void downloadeeSBOfficeWiseRetireList(@ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletResponse response,
            @ModelAttribute("esbListBean") esbListBean eBean, @RequestParam Map<String, String> requestParams, @RequestParam("deptCode") String deptCode)
            throws IndexOutOfBoundsException, IOException {
        response.setContentType("application/vnd.ms-excel");
        OutputStream out = null;
        String fileName = null;
        try {
            String deptName = deptDAO.getDeptName(deptCode);
            fileName = "ServiceBook_Monitoring_OfficeWiseReport_" + deptName + "_DEPT." + ".xls";
            out = new BufferedOutputStream(response.getOutputStream());
            WritableWorkbook workbook = Workbook.createWorkbook(out);
            SBMonitoringDAO.downloadOfficeWiseSBUpdatedReport(deptCode, out, fileName, workbook);
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            workbook.write();
            workbook.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @RequestMapping(value = "downloadeSBDistrcitWiseRetireList", method = {RequestMethod.GET, RequestMethod.POST})
    public void downloadeSBDistrcitWiseRetireList(@ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletResponse response,
            @ModelAttribute("esbListBean") esbListBean eBean, @RequestParam Map<String, String> requestParams)
            throws IndexOutOfBoundsException, IOException {
        response.setContentType("application/vnd.ms-excel");
        OutputStream out = null;
        String fileName = null;
        try {
            fileName = "ServiceBook_Monitoring_DistrictWise_Report" + ".xls";
            out = new BufferedOutputStream(response.getOutputStream());
            WritableWorkbook workbook = Workbook.createWorkbook(out);
            SBMonitoringDAO.downloadeDistrcitWiseSBUpdatedReport(lub.getLogindeptcode(), out, fileName, workbook);
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            workbook.write();
            workbook.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @RequestMapping(value = "downloadeSBDistrictOfficeWiseRetireList", method = {RequestMethod.GET, RequestMethod.POST})
    public void downloadeSBDistrictOfficeWiseRetireList(@ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletResponse response,
            @ModelAttribute("esbListBean") esbListBean eBean, @RequestParam Map<String, String> requestParams, @RequestParam("distCode") String distCode)
            throws IndexOutOfBoundsException, IOException {
        response.setContentType("application/vnd.ms-excel");
        OutputStream out = null;
        String fileName = null;
        try {
            String distName = districtDAO.getDistrictName(distCode);
            fileName = "ServiceBook_Monitoring_OfficeWise_Report_District_" + distName + ".xls";
            out = new BufferedOutputStream(response.getOutputStream());
            WritableWorkbook workbook = Workbook.createWorkbook(out);
            //SBMonitoringDAO.downloadeSBDistOfficeWiseRetireList(lub.getLogindeptcode(),distCode,out, fileName, workbook);
            SBMonitoringDAO.downloadeSBDistOfficeWiseRetireList(lub.getLogindeptcode(),distName, distCode, fileName, workbook);
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            workbook.write();
            workbook.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
