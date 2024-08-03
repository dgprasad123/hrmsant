package hrms.controller.report.supperannuationProjectionReport;

import hrms.SelectOption;
import hrms.common.CommonFunctions;
import hrms.controller.payroll.billbrowser.BillBrowserController;
import hrms.dao.master.DepartmentDAO;
import hrms.dao.master.DistrictDAO;
import hrms.dao.master.OfficeDAO;
import hrms.dao.report.superannuationProjectionReport.SuperannuationProjectionReportDAO;
import hrms.model.login.LoginUserBean;
import hrms.model.master.Department;
import hrms.model.master.Office;
import hrms.model.report.superannuationProjectionReport.SuperannuationProjectionReportBean;
import hrms.model.report.superannuationProjectionReport.SuperannuationProjectionReportForm;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import jxl.Workbook;
import jxl.write.WritableWorkbook;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
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
public class SuperannuationProjectionReportController implements ServletContextAware {

    public static final String FILE_SEPARATOR = System.getProperty("file.separator");
    private ServletContext servletContext;

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Autowired
    public SuperannuationProjectionReportDAO superannuationProjectionDAO;

    @Autowired
    DepartmentDAO departmentDao;

    @Autowired
    DistrictDAO districtDAO;

    @Autowired
    OfficeDAO offDAO;

    @RequestMapping(value = "SupperannuationProjectionReport")
    public ModelAndView supperannuationProjectionReport(Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") SuperannuationProjectionReportForm spreportform) {
        ModelAndView mav = new ModelAndView();
        try {

            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            List li = new ArrayList();
            SelectOption so = new SelectOption();
            so = new SelectOption();
            so.setLabel(year + "");
            so.setValue(year + "");
            li.add(so);
            year++;
            so = new SelectOption();
            so.setLabel(year + "");
            so.setValue(year + "");
            li.add(so);

            year++;
            so = new SelectOption();
            so.setLabel(year + "");
            so.setValue(year + "");
            li.add(so);

            year++;
            so = new SelectOption();
            so.setLabel(year + "");
            so.setValue(year + "");
            li.add(so);

            year++;
            so = new SelectOption();
            so.setLabel(year + "");
            so.setValue(year + "");
            li.add(so);

            mav.addObject("yearList", li);

        } catch (Exception e) {
            e.printStackTrace();
        }
        mav.setViewName("/report/superannuationreport/SuperannuationProjectionReport");

        return mav;
    }

    @RequestMapping(value = "ViewSupperannuationProjectionReport")
    public String viewSupperannuationProjectionReport(Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") SuperannuationProjectionReportForm spreportform) {

        try {
            Calendar c = Calendar.getInstance();
            c.set(Calendar.DAY_OF_MONTH, 1);

            if (spreportform.getTxtperiodFrom() != null && !spreportform.getTxtperiodFrom().equals("")) {
                Date fdate = new SimpleDateFormat("dd-MMM-yyyy").parse(spreportform.getTxtperiodFrom());
                spreportform.setTxtperiodFrom(CommonFunctions.getFormattedOutputDate1(fdate));
            } else {
                spreportform.setTxtperiodFrom(CommonFunctions.getFormattedOutputDate1(c.getTime()));
                // System.out.println("Inside,From Date: "+CommonFunctions.getFormattedOutputDate1(c.getTime()));
            }
            // System.out.println("From Date: "+empQuarterBean.getTxtperiodFrom());
            if (spreportform.getTxtperiodTo() != null && !spreportform.getTxtperiodTo().equals("")) {
                Date fdate = new SimpleDateFormat("dd-MMM-yyyy").parse(spreportform.getTxtperiodTo());
                spreportform.setTxtperiodTo(CommonFunctions.getFormattedOutputDate1(fdate));
            } else {
                c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
                spreportform.setTxtperiodTo(CommonFunctions.getFormattedOutputDate1(c.getTime()));

            }
            // System.out.println("spreportform.getTxtperiodFrom()"+spreportform.getTxtperiodFrom()+"spreportform.getTxtperiodTo()==="+spreportform.getTxtperiodTo());

            ArrayList emplist = superannuationProjectionDAO.getSuperannuationEmpList(lub.getLoginoffcode(), spreportform.getTxtperiodFrom(), spreportform.getTxtperiodTo());
            model.addAttribute("emplist", emplist);
            Calendar cal = Calendar.getInstance();
            int curyear = cal.get(Calendar.YEAR);
            List li = new ArrayList();
            SelectOption so = new SelectOption();
            so = new SelectOption();
            so.setLabel(curyear + "");
            so.setValue(curyear + "");
            li.add(so);

            curyear++;
            so = new SelectOption();
            so.setLabel(curyear + "");
            so.setValue(curyear + "");
            li.add(so);

            curyear++;
            so = new SelectOption();
            so.setLabel(curyear + "");
            so.setValue(curyear + "");
            li.add(so);

            curyear++;
            so = new SelectOption();
            so.setLabel(curyear + "");
            so.setValue(curyear + "");
            li.add(so);

            curyear++;
            so = new SelectOption();
            so.setLabel(curyear + "");
            so.setValue(curyear + "");
            li.add(so);

            model.addAttribute("yearList", li);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "/report/superannuationreport/SuperannuationProjectionReport";
    }

    @RequestMapping(value = "superannuationProjectionReportForAOO")
    public String superannuationProjectionReportForHOA(Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") SuperannuationProjectionReportForm spreportform) {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH);
        month = month + 1;
        int curyear = year;
        try {
            if (spreportform.getSltYear() != null && !spreportform.getSltYear().equals("")) {
                year = Integer.parseInt(spreportform.getSltYear());
            }
            if (spreportform.getSltMonth() != null && !spreportform.getSltMonth().equals("")) {
                month = Integer.parseInt(spreportform.getSltMonth());
            }
            String acctType = "ALL";
            ArrayList emplist = superannuationProjectionDAO.ViewSupperannuationProjectionReportAdministartiveDeptOnly(year, month, acctType);
            model.addAttribute("emplist", emplist);
            model.addAttribute("year", year);
            model.addAttribute("month", month);
            spreportform.setSltYear(year + "");
            spreportform.setSltMonth(month + "");

            List li = new ArrayList();
            SelectOption so = null;
            so = new SelectOption();
            so.setLabel(curyear + "");
            so.setValue(curyear + "");
            li.add(so);

            curyear++;
            so = new SelectOption();
            so.setLabel(curyear + "");
            so.setValue(curyear + "");
            li.add(so);

            curyear++;
            so = new SelectOption();
            so.setLabel(curyear + "");
            so.setValue(curyear + "");
            li.add(so);

            curyear++;
            so = new SelectOption();
            so.setLabel(curyear + "");
            so.setValue(curyear + "");
            li.add(so);

            curyear++;
            so = new SelectOption();
            so.setLabel(curyear + "");
            so.setValue(curyear + "");
            li.add(so);

            model.addAttribute("yearList", li);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "/report/superannuationreport/SuperannuationProjectionForHOA";
    }

    @RequestMapping(value = "superannuationProjectionReportForAOOList.htm")
    public String superannuationProjectionReportForHOAList(Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") SuperannuationProjectionReportForm spreportform) {

        int year = Calendar.getInstance().get(Calendar.YEAR);
        //  year = Integer.parseInt(year);
        int month = Calendar.getInstance().get(Calendar.MONTH);
        month = month + 1;
        int curyear = year;
        String acctType = null;
        try {
            if (spreportform.getSltYear() != null && !spreportform.getSltYear().equals("")) {
                year = Integer.parseInt(spreportform.getSltYear());
            }
            if (spreportform.getSltMonth() != null && !spreportform.getSltMonth().equals("")) {
                month = Integer.parseInt(spreportform.getSltMonth());
            }
            if (spreportform.getSltAcctType() != null && !spreportform.getSltAcctType().equals("")) {
                acctType = spreportform.getSltAcctType();
            }
            ArrayList emplist = superannuationProjectionDAO.ViewSupperannuationProjectionReportAdministartiveDeptOnly(year, month, acctType);
            model.addAttribute("emplist", emplist);
            model.addAttribute("year", year);
            model.addAttribute("month", month);
            spreportform.setSltYear(year + "");
            spreportform.setSltMonth(month + "");

            List li = new ArrayList();
            SelectOption so = null;
            so = new SelectOption();
            so.setLabel(curyear + "");
            so.setValue(curyear + "");
            li.add(so);

            curyear++;
            so = new SelectOption();
            so.setLabel(curyear + "");
            so.setValue(curyear + "");
            li.add(so);

            curyear++;
            so = new SelectOption();
            so.setLabel(curyear + "");
            so.setValue(curyear + "");
            li.add(so);

            curyear++;
            so = new SelectOption();
            so.setLabel(curyear + "");
            so.setValue(curyear + "");
            li.add(so);

            curyear++;
            so = new SelectOption();
            so.setLabel(curyear + "");
            so.setValue(curyear + "");
            li.add(so);

            model.addAttribute("yearList", li);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/report/superannuationreport/SuperannuationProjectionForHOA";
    }

    @RequestMapping(value = "superannuationProjectionReportForAOOList.htm", params = "Download", method = {RequestMethod.GET, RequestMethod.POST})
    public String downloadSuperannuationProjectionReportForHOA(Model model, HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub,
            @ModelAttribute("command") SuperannuationProjectionReportForm spreportform, @ModelAttribute("Office") Office officeform) throws IndexOutOfBoundsException, IOException {
        String folderPath = servletContext.getInitParameter("SuperAnnuationProjectionReport");
        OutputStream out = null;
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH);
        month = month + 1;
        List deptddoList = null;
        String dept_name = null;
        String dept_off_code = null;
        String fileName = null;
        String filemonth = null;
        String zipFilename = null;
        String acct_type = null;
        String[] monthName = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
        File directory = null;
        try {
            if (spreportform.getSltYear() != null && !spreportform.getSltYear().equals("")) {
                year = Integer.parseInt(spreportform.getSltYear());
            }
            if (spreportform.getSltMonth() != null && !spreportform.getSltMonth().equals("")) {
                month = Integer.parseInt(spreportform.getSltMonth());
            }
            if (spreportform.getSltAcctType() != null && !spreportform.getSltAcctType().equals("")) {
                acct_type = spreportform.getSltAcctType();
            }
            if (acct_type != null && acct_type.equals("ALL")) {
                deptddoList = offDAO.getDeptOfficeCode();
            } else {
                deptddoList = offDAO.getSuperannuatedDeptList(year, month, acct_type);
            }

            filemonth = monthName[month - 1];
            Iterator itr = deptddoList.iterator();
            Office office = null;
            while (itr.hasNext()) {
                office = (Office) itr.next();
                dept_name = office.getOffEn();
                dept_off_code = office.getOffCode();
                if (acct_type != null && acct_type.equals("ALL")) {
                    fileName = "SuperannuationProject_" + dept_name + "_" + filemonth + "_" + year + ".xls";
                } else {
                    fileName = "SuperannuationProject_" + dept_name + "_" + acct_type + "_" + filemonth + "_" + year + ".xls";
                }

                //fileName = "SuperannuationProject_" + dept_name + "_" + filemonth + "_" + year + ".xls";
                //out = new FileOutputStream(new File("D:/SuperAnnuationProject/" + fileName));                
                out = new FileOutputStream(new File(folderPath + fileName));

                WritableWorkbook workbook = Workbook.createWorkbook(out);
                superannuationProjectionDAO.downloadSuperannuationProjectHOA(out, fileName, dept_off_code, workbook, month, year, acct_type);
                response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
                workbook.write();
                workbook.close();
            }

            //directory = new File("D:/SuperAnnuationProject/");
            directory = new File(folderPath);
            String[] files = directory.list();
            if (acct_type != null && acct_type.equals("ALL")) {
                zipFilename = "SuperAnnuationProjectioReport_" + filemonth + "_" + year + "" + ".zip";
            } else {
                zipFilename = "SuperAnnuationProjectioReport_" + acct_type + "_" + filemonth + "_" + year + "" + ".zip";
            }

            byte[] zip = zipFilesSuperAnnuationProjection(directory, files);
            OutputStream outst = response.getOutputStream();
            response.setContentType("application/zip");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + zipFilename + "\"");
            outst.write(zip);
            outst.flush();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            FileUtils.deleteDirectory(directory);
        }

        return "/report/superannuationreport/SuperannuationProjectionForHOA";
    }

    @RequestMapping(value = "getsuperannuationEmployeeList")
    public String getsuperannuationEmployeeList(Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam Map<String, String> param) {

        int year = 0;
        int month = 0;
        String offcode = "";
        String acctType = null;
        try {
            if (param.get("year") != null && !param.get("year").equals("")) {
                year = Integer.parseInt(param.get("year"));
            }
            if (param.get("month") != null && !param.get("month").equals("")) {
                month = Integer.parseInt(param.get("month"));
            }

            if (param.get("acctType") != null && !param.get("acctType").equals("")) {
                acctType = param.get("acctType");
            }

            offcode = param.get("offCode");
            ArrayList emplist = superannuationProjectionDAO.getEmployeesSuperannuationList(year, month, offcode, acctType);

            model.addAttribute("emplist", emplist);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/report/superannuationreport/SuperannuationReportEmployeeList";
    }

    @RequestMapping(value = "getsuperannuationEmployeeListGroupWise")
    public String getsuperannuationEmployeeListGroupWise(Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam Map<String, String> param) {

        int year = 0;
        int month = 0;
        String offcode = "";
        String acctType = null;
        try {
            if (param.get("year") != null && !param.get("year").equals("")) {
                year = Integer.parseInt(param.get("year"));
            }
            if (param.get("month") != null && !param.get("month").equals("")) {
                month = Integer.parseInt(param.get("month"));
            }
            if (param.get("acctType") != null && !param.get("acctType").equals("")) {
                acctType = param.get("acctType");
            }

            offcode = param.get("offCode");

            String postGrp = param.get("postGrp");

            ArrayList emplist = superannuationProjectionDAO.getEmployeesSuperannuationListGroupWise(year, month, offcode, postGrp, acctType);

            model.addAttribute("emplist", emplist);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/report/superannuationreport/SuperannuationReportEmployeeList";
    }

    @RequestMapping(value = "AdminSuperannuationProjectionReport")
    public String adminSuperannuationReport(Model model, @ModelAttribute("command") SuperannuationProjectionReportForm spreportform) {

        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/report/superannuationreport/AdminSuperannuationProjectionReport";
    }

    @RequestMapping(value = "ViewAdminSuperannuationProjectionReport")
    public String viewAdminSuperannuationReport(Model model, @ModelAttribute("command") SuperannuationProjectionReportForm spreportform) {

        ArrayList datalist = new ArrayList();

        try {
            List deptlist = departmentDao.getDepartmentList();

            List distlist = districtDAO.getDistrictList();
            model.addAttribute("distlist", distlist);
            if ((spreportform.getSltFromYear() != null && !spreportform.getSltFromYear().equals("")) && (spreportform.getSltFromMonth() != null && !spreportform.getSltFromMonth().equals(""))) {
                if ((spreportform.getSltToYear() != null && !spreportform.getSltToYear().equals("")) && (spreportform.getSltToMonth() != null && !spreportform.getSltToMonth().equals(""))) {
                    datalist = superannuationProjectionDAO.getSuperAnnuationProjectionData(deptlist, Integer.parseInt(spreportform.getSltFromYear()), Integer.parseInt(spreportform.getSltFromMonth()), Integer.parseInt(spreportform.getSltToYear()), Integer.parseInt(spreportform.getSltToMonth()));
                }
            }

            model.addAttribute("datalist", datalist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/report/superannuationreport/AdminSuperannuationProjectionReport";
    }

    @RequestMapping(value = "ViewSupperannuationProjectionReportDeptWise")
    public String ViewSupperannuationProjectionReportDeptWise(Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") SuperannuationProjectionReportForm spreportform) {

        int year = Calendar.getInstance().get(Calendar.YEAR);
        //  year = Integer.parseInt(year);
        int month = Calendar.getInstance().get(Calendar.MONTH);
        month = month + 1;

        try {
            if (spreportform.getSltYear() != null && !spreportform.getSltYear().equals("")) {
                year = Integer.parseInt(spreportform.getSltYear());
            }
            if (spreportform.getSltMonth() != null && !spreportform.getSltMonth().equals("")) {
                month = Integer.parseInt(spreportform.getSltMonth());
            }
            ArrayList emplist = superannuationProjectionDAO.ViewSupperannuationProjectionReportDeptWise(year, month);
            model.addAttribute("emplist", emplist);
            model.addAttribute("year", year);
            model.addAttribute("month", month);
            spreportform.setSltYear(year + "");
            spreportform.setSltMonth(month + "");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/report/superannuationreport/ViewSupperannuationProjectionReportDeptWise";
    }

    @RequestMapping(value = "ViewSupperannuationProjectionReportDistWise")
    public String ViewSupperannuationProjectionReportDistWise(Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") SuperannuationProjectionReportForm spreportform, @RequestParam Map<String, String> param) {

        int year = Integer.parseInt(param.get("year"));
        int month = Integer.parseInt(param.get("month"));
        String deptCode = param.get("deptCode");
        String departmentname = param.get("departmentname");

        try {

            ArrayList emplist = superannuationProjectionDAO.ViewSupperannuationProjectionReportDistWise(deptCode, year, month);
            model.addAttribute("emplist", emplist);
            model.addAttribute("departmentname", departmentname);
            model.addAttribute("year", year);
            model.addAttribute("month", month);
            model.addAttribute("deptCode", deptCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/report/superannuationreport/ViewSupperannuationProjectionReportDistWise";
    }

    @RequestMapping(value = "ViewSupperannuationProjectionReportOfficeWise")
    public String ViewSupperannuationProjectionReportOfficeWise(Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") SuperannuationProjectionReportForm spreportform, @RequestParam Map<String, String> param) {

        int year = Integer.parseInt(param.get("year"));
        int month = Integer.parseInt(param.get("month"));
        String districtCode = param.get("districtCode");
        String departmentname = param.get("departmentname");
        String districtname = param.get("districtname");
        String deptCode = param.get("deptCode");

        try {

            ArrayList emplist = superannuationProjectionDAO.ViewSupperannuationProjectionReportOfficeWise(districtCode, year, month, deptCode);
            model.addAttribute("emplist", emplist);
            model.addAttribute("departmentname", departmentname);
            model.addAttribute("districtname", districtname);
            model.addAttribute("year", year);
            model.addAttribute("month", month);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/report/superannuationreport/ViewSupperannuationProjectionReportOfficeWise";
    }

    @RequestMapping(value = "ViewSupperannuationProjectionReportEmpWise")
    public String ViewSupperannuationProjectionReportEmpWise(Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") SuperannuationProjectionReportForm spreportform, @RequestParam Map<String, String> param) {

        int year = Integer.parseInt(param.get("year"));
        int month = Integer.parseInt(param.get("month"));
        String offCode = param.get("officeCode");
        String officeName = param.get("officeName");

        try {

            ArrayList emplist = superannuationProjectionDAO.ViewSupperannuationProjectionReportEmpWise(offCode, year, month);
            model.addAttribute("emplist", emplist);
            model.addAttribute("officeName", officeName);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/report/superannuationreport/ViewSupperannuationProjectionReportEmpWise";
    }

    @RequestMapping(value = "ViewSupperannuationProjectionReportOnlyDept")
    public String ViewSupperannuationProjectionReportOnlyDept(Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") SuperannuationProjectionReportForm spreportform, @RequestParam Map<String, String> param) {

        int year = Integer.parseInt(param.get("year"));
        int month = Integer.parseInt(param.get("month"));
        //String accttype=param.get("acctType");

        try {

            ArrayList emplist = superannuationProjectionDAO.ViewSupperannuationProjectionReportOnlyDept(year, month);

            model.addAttribute("emplist", emplist);
            model.addAttribute("year", year);
            model.addAttribute("month", month);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/report/superannuationreport/ViewSupperannuationProjectionReportOnlyDept";
    }

    @RequestMapping(value = "ViewSupperannuationProjectionGraphOnlyDept")
    public String ViewSupperannuationProjectionGraphOnlyDept(Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") SuperannuationProjectionReportForm spreportform, @RequestParam Map<String, String> param) {

        try {
            Calendar cal = Calendar.getInstance();
            int curyear = cal.get(Calendar.YEAR);
            String allYear = "";

            String prevData = "";
            String curData = "";

            List li = new ArrayList();
            SelectOption so = new SelectOption();
            so = new SelectOption();
            so.setLabel(curyear + "");
            so.setValue(curyear + "");
            li.add(so);
            allYear = curyear + "";
            int defaultYear = curyear;

            String data1 = superannuationProjectionDAO.getSuperannuationDataGroupWise(curyear - 1); // 2019
            prevData = curyear + data1;
            String data2 = superannuationProjectionDAO.getSuperannuationDataGroupWise(curyear); // 2020
            curData = curyear + data2;

            curyear++;
            so = new SelectOption();
            so.setLabel(curyear + "");
            so.setValue(curyear + "");
            li.add(so);
            allYear = allYear + "," + curyear;
            String data3 = superannuationProjectionDAO.getSuperannuationDataGroupWise(curyear); // 2021
            prevData = prevData + "," + curyear + data2;
            curData = curData + "," + curyear + data3;

            curyear++;
            so = new SelectOption();
            so.setLabel(curyear + "");
            so.setValue(curyear + "");
            li.add(so);
            allYear = allYear + "," + curyear;
            String data4 = superannuationProjectionDAO.getSuperannuationDataGroupWise(curyear); // 2022
            prevData = prevData + "," + curyear + data3;
            curData = curData + "," + curyear + data4;

            curyear++;
            so = new SelectOption();
            so.setLabel(curyear + "");
            so.setValue(curyear + "");
            li.add(so);
            allYear = allYear + "," + curyear;
            String data5 = superannuationProjectionDAO.getSuperannuationDataGroupWise(curyear); // 2023
            prevData = prevData + "," + curyear + data4;
            curData = curData + "," + curyear + data5;

            curyear++;
            so = new SelectOption();
            so.setLabel(curyear + "");
            so.setValue(curyear + "");
            li.add(so);
            allYear = allYear + "," + curyear;
            String data6 = superannuationProjectionDAO.getSuperannuationDataGroupWise(curyear); // 2024
            prevData = prevData + "," + curyear + data5;
            curData = curData + "," + curyear + data6;

            model.addAttribute("yearList", li);
            model.addAttribute("defaultYear", defaultYear);
            model.addAttribute("prevdefaultYear", (defaultYear - 1));
            model.addAttribute("yearArray", allYear);
            model.addAttribute("prevData", prevData);
            model.addAttribute("curData", curData);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/report/superannuationreport/ViewSupperannuationProjectionGraphOnlyDept";
    }

    @RequestMapping(value = "ViewSupperannuationProjectionGraphDrillDown")
    public String ViewSupperannuationProjectionGraphDrillDown(Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") SuperannuationProjectionReportForm spreportform, @RequestParam Map<String, String> param) {

        try {
            Calendar cal = Calendar.getInstance();
            int curyear = cal.get(Calendar.YEAR);
            String allYear = "";

            String prevData = "";
            String curData = "";

            List li = new ArrayList();

            SelectOption so = new SelectOption();
            so = new SelectOption();
            so.setLabel(curyear + "");
            so.setValue(curyear + "");
            li.add(so);
            allYear = curyear + "";
            int defaultYear = curyear;

            String fromPeriod = "";
            String toPeriod = "";

            //spreportform.setSltFromMonth(spreportform.getSltFromMonth());
            //spreportform.setSltToMonth(spreportform.getSltToMonth());
            //spreportform.setSltFromYear(spreportform.getSltFromYear());
            //spreportform.setSltToYear(spreportform.getSltToYear());
            if (spreportform.getSltToYear() != null && !spreportform.getSltToYear().equals("")) {

                Calendar cal2 = Calendar.getInstance();
                cal2.set(Calendar.YEAR, Integer.parseInt(spreportform.getSltToYear()));
                cal2.set(Calendar.MONTH, (Integer.parseInt(spreportform.getSltToMonth()) - 1));
                int lastdateofMonth = cal2.getActualMaximum(Calendar.DATE);

                fromPeriod = spreportform.getSltFromYear() + "-" + spreportform.getSltFromMonth() + "-1";
                toPeriod = spreportform.getSltToYear() + "-" + spreportform.getSltToMonth() + "-" + lastdateofMonth;

                List projectionList = superannuationProjectionDAO.getSuperannuationReportPeriodWise(fromPeriod, toPeriod);

                JSONArray periodProjectList = new JSONArray(projectionList);

                String drilldownDeptWiseSeries = superannuationProjectionDAO.getSuperannuationReportPeriodDepartmentWise(fromPeriod, toPeriod);

                model.addAttribute("periodProjectList", periodProjectList);
                model.addAttribute("deptwiseList", drilldownDeptWiseSeries);

            }
            model.addAttribute("yearList", li);
            model.addAttribute("fromMonth", spreportform.getSltFromMonth());
            model.addAttribute("toMonth", spreportform.getSltToMonth());
            model.addAttribute("fromYear", spreportform.getSltFromYear());
            model.addAttribute("toYear", spreportform.getSltToYear());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/report/superannuationreport/SupperannuationProjectionGraphSixMonthOnlyAO";
    }

    private byte[] zipFilesSuperAnnuationProjection(File directory, String[] files) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ZipOutputStream zos = new ZipOutputStream(baos);
        byte bytes[] = new byte[2048];
        for (int i = 0; i < files.length; i++) {
            String fileName = files[i];
            FileInputStream fis = new FileInputStream(directory.getPath() + BillBrowserController.FILE_SEPARATOR + fileName);
            BufferedInputStream bis = new BufferedInputStream(fis);
            zos.putNextEntry(new ZipEntry(fileName));
            int bytesRead;
            while ((bytesRead = bis.read(bytes)) != -1) {
                zos.write(bytes, 0, bytesRead);
            }
            zos.closeEntry();
            bis.close();
            fis.close();
        }
        zos.flush();
        baos.flush();
        zos.close();
        baos.close();
        return baos.toByteArray();
    }

    @RequestMapping(value = "ViewSupperannuationReportDistWise")
    public String ViewSupperannuationReportDistWise(Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") SuperannuationProjectionReportForm spreportform, @RequestParam Map<String, String> param) {

        //int year = Integer.parseInt(param.get("year"));
        //int month = Integer.parseInt(param.get("month"));
        //String deptCode = param.get("deptCode");
        // String departmentname = param.get("departmentname");
        try {
            Calendar c = Calendar.getInstance();
            c.set(Calendar.DAY_OF_MONTH, 1);

            if (spreportform.getTxtperiodFrom() != null && !spreportform.getTxtperiodFrom().equals("")) {
                Date fdate = new SimpleDateFormat("dd-MMM-yyyy").parse(spreportform.getTxtperiodFrom());
                spreportform.setTxtperiodFrom(CommonFunctions.getFormattedOutputDate1(fdate));
            } else {
                spreportform.setTxtperiodFrom(CommonFunctions.getFormattedOutputDate1(c.getTime()));
                // System.out.println("Inside,From Date: "+CommonFunctions.getFormattedOutputDate1(c.getTime()));
            }
            // System.out.println("From Date: "+empQuarterBean.getTxtperiodFrom());
            if (spreportform.getTxtperiodTo() != null && !spreportform.getTxtperiodTo().equals("")) {
                Date fdate = new SimpleDateFormat("dd-MMM-yyyy").parse(spreportform.getTxtperiodTo());
                spreportform.setTxtperiodTo(CommonFunctions.getFormattedOutputDate1(fdate));
            } else {
                c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
                spreportform.setTxtperiodTo(CommonFunctions.getFormattedOutputDate1(c.getTime()));

            }
            String deptcode = lub.getLoginempid();

            ArrayList emplist = superannuationProjectionDAO.ViewSupperannuationDetailsDistWise(lub.getLoginempid(), spreportform.getTxtperiodFrom(), spreportform.getTxtperiodTo());
            //ArrayList emplist = superannuationProjectionDAO.getSuperannuationEmpList(lub.getLoginoffcode(), spreportform.getTxtperiodFrom(), spreportform.getTxtperiodTo());
            model.addAttribute("emplist", emplist);
            //model.addAttribute("departmentname", departmentname);
            model.addAttribute("year", 2023);
            model.addAttribute("month", 7);
            model.addAttribute("deptCode", deptcode);
            model.addAttribute("fromdate", spreportform.getTxtperiodFrom());
            model.addAttribute("todate", spreportform.getTxtperiodTo());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/report/superannuationreport/viewSuperannuationDetails";
    }

    @RequestMapping(value = "ViewSupperannuationReport")
    public ModelAndView ViewSupperannuationReport(Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") SuperannuationProjectionReportForm spreportform, @RequestParam Map<String, String> param) {

        ModelAndView mv = new ModelAndView();

        try {
            mv.setViewName("/report/superannuationreport/viewSuperannuationDetails");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mv;
    }
    

    @RequestMapping(value = "ViewSupperannuationDetailsOfficeWise")
    public String ViewSupperannuationDetailsOfficeWise(Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") SuperannuationProjectionReportForm spreportform, @RequestParam Map<String, String> param) {

        String deptCode = lub.getLoginempid();
        String distcode = param.get("districtCode");
        String distName = param.get("districtNm");
        String fdate = param.get("fdate");
        String tdate = param.get("tdate");

        try {

            ArrayList emplist = superannuationProjectionDAO.getDistSuperannuationEmpList(distcode, fdate, tdate);
            model.addAttribute("emplist", emplist);
            model.addAttribute("fdate", fdate);
            model.addAttribute("tdate", tdate);
            model.addAttribute("distName", distName);
            model.addAttribute("deptCode", deptCode);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/report/superannuationreport/viewSuperannuationDetailsOfficewise";
    }

    @ResponseBody
    @RequestMapping(value = "viewMacpRacpJSON", method = {RequestMethod.GET, RequestMethod.POST})
    public void viewMacpRacpJSON(ModelMap model, HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub,
            @RequestParam("empid") String empid) {
        response.setContentType("application/json");
        PrintWriter out = null;
        JSONArray json = null;
        try {
            System.out.println("priv");
            List macpList = superannuationProjectionDAO.getMacpRacpList(empid);
            json = new JSONArray(macpList);
            out = response.getWriter();
            out.write(json.toString());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    @ResponseBody
    @RequestMapping(value = "viewTppoJSON", method = {RequestMethod.GET, RequestMethod.POST})
    public void viewTppoJSON(ModelMap model, HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub,
            @RequestParam("empid") String empid) {
        response.setContentType("application/json");
        PrintWriter out = null;
        JSONArray json = null;
        try {
            System.out.println("priv");
            List tppoList = superannuationProjectionDAO.getTppoList(empid);
            json = new JSONArray(tppoList);
            out = response.getWriter();
            out.write(json.toString());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    @RequestMapping(value = "viewpersonnelIsectionOfficewise")
    public String viewpersonnelIsectionOfficewise(Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") SuperannuationProjectionReportForm spreportform, @RequestParam Map<String, String> param) {
        String offCode =spreportform.getSltOffCode();
        try {
//            System.out.println("sltoffCode:"+offCode);

            ArrayList emplist = superannuationProjectionDAO.getOfficeWiseEmpList(offCode);
            model.addAttribute("emplist", emplist);
            ArrayList officelist = superannuationProjectionDAO.getOfficeList();
            model.addAttribute("officelist", officelist);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/report/PersonnelISection/viewpersonnelIsectionOfficewise";
    }

    @RequestMapping(value = "PersonnelISection")
    public ModelAndView PersonnelISection(Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") SuperannuationProjectionReportForm spreportform, @RequestParam Map<String, String> param) {

        ModelAndView mv = new ModelAndView();

        try {
            ArrayList officelist = superannuationProjectionDAO.getOfficeList();
            model.addAttribute("officelist", officelist);
            mv.setViewName("/report/PersonnelISection/viewpersonnelIsectionOfficewise");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mv;
    }
    
    
    
    @ResponseBody
    @RequestMapping(value = "viewPoJSON", method = {RequestMethod.GET, RequestMethod.POST})
    public void viewPoJSON(ModelMap model, HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub,
            @RequestParam("empid") String empid) {
        response.setContentType("application/json");
        PrintWriter out = null;
        JSONArray json = null;
        try {
            System.out.println("priv");
            List tppoList = superannuationProjectionDAO.getPOList(empid);
            json = new JSONArray(tppoList);
            out = response.getWriter();
            out.write(json.toString());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }
    
    
    

    @RequestMapping(value = "viewStatisticalCellOfcList")
    public String viewStatisticalCellOfcList(Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub, 
            @ModelAttribute("command") SuperannuationProjectionReportForm spreportform, @RequestParam Map<String, String> param) {
       
        try {
            List ofcList=offDAO.getTotalDDOOfficeList("14");
            model.addAttribute("ofcList", ofcList);           
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/report/superannuationreport/viewTotalOfficeList";
    }
     @RequestMapping(value = "ViewstatisticalReportPostStrength")
    public ModelAndView ViewstatisticalReportPostStrength(Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub, 
            @ModelAttribute("command") SuperannuationProjectionReportForm spreportform, @RequestParam Map<String, String> param) {

        ModelAndView mv = new ModelAndView();
        String OffCode = param.get("offcode");
        String OffName = param.get("offname");

        try {
            List postlist=superannuationProjectionDAO.getPolicePostList(OffCode);
            mv.setViewName("/report/superannuationreport/viewStatisticalReport");
            mv.addObject("postlist", postlist);
            mv.addObject("OffName", OffName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mv;
    }
}
