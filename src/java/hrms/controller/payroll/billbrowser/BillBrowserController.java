/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.payroll.billbrowser;

import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import hrms.SelectOption;
import hrms.common.CalendarCommonMethods;
import hrms.common.CommonFunctions;
import hrms.common.HrmsBillConfig;
import hrms.dao.billvouchingTreasury.VouchingServicesDAO;
import hrms.dao.employee.EmployeeDAO;
import hrms.dao.esignBill.EsignBillDAO;
import hrms.dao.master.LoanTypeDAO;
import hrms.dao.master.OfficeDAO;
import hrms.dao.master.TreasuryDAO;
import hrms.dao.payroll.aqmast.AqmastDAO;
import hrms.dao.payroll.billbrowser.AqReportDAO;
import hrms.dao.payroll.billbrowser.BillBrowserDAO;
import hrms.dao.payroll.billbrowser.BillGroupDAO;
import hrms.dao.payroll.billmast.BillMastDAOImpl;
import hrms.dao.payroll.schedule.BillFrontpageDAOImpl;
import hrms.dao.payroll.schedule.PayBillDMPDAO;
import hrms.dao.payroll.schedule.ScheduleDAO;
import hrms.model.billvouchingTreasury.BillDetail;
import hrms.model.billvouchingTreasury.ObjectBreakup;
import hrms.model.common.CommonReportParamBean;
import hrms.model.login.LoginUserBean;
import hrms.model.master.Office;
import hrms.model.master.OfficeBean;
import hrms.model.mergeHRMSID.mergeDuplicateHrmsidForm;
import hrms.model.payroll.BytransferDetails;
import hrms.model.payroll.GpfTpfDetails;
import hrms.model.payroll.NPSDetails;
import hrms.model.payroll.SalaryBenefitiaryDetails;
import hrms.model.payroll.billbrowser.BillAttr;
import hrms.model.payroll.billbrowser.BillBean;
import hrms.model.payroll.billbrowser.BillBrowserbean;
import hrms.model.payroll.billbrowser.FailedTransactionBean;
import hrms.model.payroll.billbrowser.GetBillStatusBean;
import hrms.model.payroll.billbrowser.GlobalBillStatus;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author Manas Jena
 */
@Controller
@SessionAttributes({"LoginUserBean", "SelectedEmpOffice"})
public class BillBrowserController implements ServletContextAware {

    public static final String FILE_SEPARATOR = System.getProperty("file.separator");
    public static final String STOP_BILL_PROCESS = "STOP_BILL_PROCESS";

    private ServletContext servletContext;

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Autowired
    AqReportDAO aqReportDAO;

    @Autowired
    PayBillDMPDAO paybillDmpDao;

    @Autowired
    ScheduleDAO comonScheduleDao;

    @Autowired
    BillBrowserDAO billBrowserDao;

    @Autowired
    VouchingServicesDAO vouchingServicesDAOImpl;

    @Autowired
    BillGroupDAO billGroupDAO;

    @Autowired
    TreasuryDAO treasuryDao;

    @Autowired
    BillMastDAOImpl billMastDAO;

    @Autowired
    BillFrontpageDAOImpl billFrontPageDmpDao;
    @Autowired
    AqReportDAO aqReportDao;

    @Autowired
    public LoanTypeDAO loanTypeDao;

    @Autowired
    OfficeDAO officeDao;

    @Autowired
    AqmastDAO aqmastDao;

    @Autowired
    EsignBillDAO esignBillDao;

    @RequestMapping(value = "billBrowserAction", method = RequestMethod.GET)
    public ModelAndView billBrowserAction(ModelMap model, @ModelAttribute("BillBrowserbean") BillBrowserbean bbbean, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpOffice") String selectedEmpOffice, BindingResult result, HttpServletResponse response) {//       
        String path = "/payroll/BillBrowser";
        boolean isCollegeDHE = false;
        ArrayList billYears = new ArrayList();
        String hideExtraDutyButton = "Y";
        String isDDODHE = null;
        boolean isddoDheOff = false;
        System.out.println("hibill");
        /* For DHE---Start-------*/
        System.out.println("");
        isCollegeDHE = billBrowserDao.isCollegeUnderDHE(selectedEmpOffice);
        isddoDheOff = billBrowserDao.isDDODHE(selectedEmpOffice);
        if (isCollegeDHE) {
            billYears = billBrowserDao.getBillPrepareYearDHE(selectedEmpOffice);
            isDDODHE = "B";

        } else if (isddoDheOff) {
            billYears = billBrowserDao.getBillPrepareYearDDODHE(selectedEmpOffice);
            isDDODHE = "Y";
        }/*--End--*/ else {
            billYears = billBrowserDao.getBillPrepareYear(selectedEmpOffice);
        }

        ModelAndView mav = new ModelAndView(path, "command", bbbean);
        if (lub.getLoginoffcode().contains("HOM")) {
            hideExtraDutyButton = "N";
        }

        mav.addObject("billYears", billYears);
        mav.addObject("isDDODHE", isDDODHE);

        mav.addObject("hideExtraDutyProcess", hideExtraDutyButton);
        mav.setViewName(path);
        return mav;
    }

    @RequestMapping(value = "newBill", method = RequestMethod.POST)
    public ModelAndView newBill(ModelMap model, @ModelAttribute("BillBrowserbean") BillBrowserbean bbbean, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpOffice") String selectedEmpOffice, BindingResult result, HttpServletResponse response) {
        String path = "/payroll/BillBrowser";
        GlobalBillStatus gbs = billBrowserDao.getBillProcessStatus(STOP_BILL_PROCESS);
        ModelAndView mav = null;

        boolean isCollegeDHE = false;
        int billYear = 0;
        int billMonth = 0;

        isCollegeDHE = billBrowserDao.isCollegeUnderDHE(selectedEmpOffice);

        if (gbs.getGlobalVarValue().equalsIgnoreCase("Y")) {
            path = "/payroll/ErrorMessage";

            mav = new ModelAndView(path, "command", bbbean);
            mav.addObject("gbs", gbs.getMessage());
        } /* For DHE---Start-------*/ else if (isCollegeDHE) {
            billYear = billBrowserDao.getNewBillYearDHE(selectedEmpOffice, "PAY");
            billMonth = billBrowserDao.getNewBillMonthDHE(selectedEmpOffice, billYear, "PAY");
            if (billMonth == 11) {
                billYear = billYear + 1;
                billMonth = 0;
            } else {
                billMonth = billMonth + 1;

            }
            bbbean.setSltMonth(billMonth);
            bbbean.setSltYear(billYear);
            path = "/payroll/PrepareBill";
            mav = new ModelAndView(path, "command", bbbean);
            SelectOption year = new SelectOption();
            year.setLabel(billYear + "");
            year.setValue(billYear + "");
            ArrayList billYears = new ArrayList();
            billYears.add(year);
            SelectOption month = new SelectOption();
            month.setValue(billMonth + "");
            month.setLabel(CalendarCommonMethods.getFullMonthAsString(billMonth));
            ArrayList billMonths = new ArrayList();
            billMonths.add(month);
            mav.addObject("billMonths", billMonths);
            mav.addObject("billYears", billYears);

        } /*--End--*/ else {
            billYear = billBrowserDao.getNewBillYear(selectedEmpOffice, "PAY");
            billMonth = billBrowserDao.getNewBillMonth(selectedEmpOffice, billYear, "PAY");
            if (billMonth == 11) {
                billYear = billYear + 1;
                billMonth = 0;
            } else {
                billMonth = billMonth + 1;

            }
            bbbean.setSltMonth(billMonth);
            bbbean.setSltYear(billYear);
            path = "/payroll/PrepareBill";
            mav = new ModelAndView(path, "command", bbbean);
            SelectOption year = new SelectOption();
            year.setLabel(billYear + "");
            year.setValue(billYear + "");
            ArrayList billYears = new ArrayList();
            billYears.add(year);
            SelectOption month = new SelectOption();
            month.setValue(billMonth + "");
            month.setLabel(CalendarCommonMethods.getFullMonthAsString(billMonth));
            ArrayList billMonths = new ArrayList();
            billMonths.add(month);
            mav.addObject("billMonths", billMonths);
            mav.addObject("billYears", billYears);
            //mav.addObject("billYear", billYear);
            //mav.addObject("billMonth", billMonth);
            //mav.addObject("monthName", monthName);

        }

        mav.setViewName(path);
        return mav;
    }

    @RequestMapping(value = "payAdvanceBill000000000", method = RequestMethod.POST)
    public ModelAndView payAdvanceBill(ModelMap model, @ModelAttribute("BillBrowserbean") BillBrowserbean bbbean, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpOffice") String selectedEmpOffice, BindingResult result, HttpServletResponse response) {
        String path = "/payroll/BillBrowser";
        GlobalBillStatus gbs = billBrowserDao.getBillProcessStatus(STOP_BILL_PROCESS);
        ModelAndView mav = null;
        if (gbs.getGlobalVarValue().equalsIgnoreCase("Y")) {
            path = "/payroll/ErrorMessage";

            mav = new ModelAndView(path, "command", bbbean);
            mav.addObject("gbs", gbs.getMessage());
        } else {
            int billYear = billBrowserDao.getNewBillYear(selectedEmpOffice, "PAYADV");
            int billMonth = billBrowserDao.getNewBillMonth(selectedEmpOffice, billYear, "PAYADV");
            if (billMonth == 11) {
                billYear = billYear + 1;
                billMonth = 0;
            } else {
                billMonth = billMonth + 1;

            }
            bbbean.setSltMonth(billMonth);
            bbbean.setSltYear(billYear);
            path = "/payroll/PreparePayAdvBill";
            mav = new ModelAndView(path, "command", bbbean);
            SelectOption year = new SelectOption();
            year.setLabel(billYear + "");
            year.setValue(billYear + "");
            ArrayList billYears = new ArrayList();
            billYears.add(year);
            SelectOption month = new SelectOption();
            month.setValue(billMonth + "");
            month.setLabel(CalendarCommonMethods.getFullMonthAsString(billMonth));
            ArrayList billMonths = new ArrayList();
            billMonths.add(month);
            mav.addObject("billMonths", billMonths);
            mav.addObject("billYears", billYears);
            //mav.addObject("billYear", billYear);
            //mav.addObject("billMonth", billMonth);
            //mav.addObject("monthName", monthName);

        }

        mav.setViewName(path);
        return mav;
    }

    @RequestMapping(value = "extraMonthIncentiveBill", method = RequestMethod.POST)
    public ModelAndView extraMonthIncentiveBill(ModelMap model, @ModelAttribute("BillBrowserbean") BillBrowserbean bbbean, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpOffice") String selectedEmpOffice, BindingResult result, HttpServletResponse response) {
        String path = "/payroll/BillBrowser";
        GlobalBillStatus gbs = billBrowserDao.getBillProcessStatus(STOP_BILL_PROCESS);
        ModelAndView mav = null;
        if (gbs.getGlobalVarValue().equalsIgnoreCase("Y")) {
            path = "/payroll/ErrorMessage";

            mav = new ModelAndView(path, "command", bbbean);
            mav.addObject("gbs", gbs.getMessage());
        } else {
            int billYear = billBrowserDao.getNewBillYear(selectedEmpOffice, bbbean.getTxtbilltype());
            int billMonth = billBrowserDao.getNewBillMonth(selectedEmpOffice, billYear, bbbean.getTxtbilltype());
            if (billMonth == 11) {
                billYear = billYear + 1;
                billMonth = 0;
            } else {
                billMonth = billMonth + 1;

            }
            bbbean.setSltMonth(billMonth);
            bbbean.setSltYear(billYear);
            path = "/payroll/PrepareExtraDutyBill";
            mav = new ModelAndView(path, "command", bbbean);
            SelectOption year = new SelectOption();
            year.setLabel(billYear + "");
            year.setValue(billYear + "");
            ArrayList billYears = new ArrayList();
            billYears.add(year);
            SelectOption month = new SelectOption();
            month.setValue(billMonth + "");
            month.setLabel(CalendarCommonMethods.getFullMonthAsString(billMonth));
            ArrayList billMonths = new ArrayList();
            billMonths.add(month);
            mav.addObject("billMonths", billMonths);
            mav.addObject("billYears", billYears);
            //mav.addObject("billYear", billYear);
            //mav.addObject("billMonth", billMonth);
            //mav.addObject("monthName", monthName);

        }

        mav.setViewName(path);
        return mav;
    }

    @RequestMapping(value = "prepareExtraMonthIncentiveBill", method = RequestMethod.POST, params = "action=Ok")
    public ModelAndView prepareprepareExtraMonthIncentiveBill(ModelMap model, @ModelAttribute("BillBrowserbean") BillBrowserbean bbbean, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpOffice") String selectedEmpOffice, BindingResult result, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView("/payroll/PrepareExtraDutyBill", "command", bbbean);
        SelectOption year = new SelectOption();
        year.setLabel(bbbean.getSltYear() + "");
        year.setValue(bbbean.getSltYear() + "");
        ArrayList billYears = new ArrayList();
        billYears.add(year);
        SelectOption month = new SelectOption();
        month.setValue(bbbean.getSltMonth() + "");
        month.setLabel(CalendarCommonMethods.getFullMonthAsString(bbbean.getSltMonth()));
        ArrayList billMonths = new ArrayList();
        billMonths.add(month);
        mav.addObject("billGroupList", billBrowserDao.getBillGroupList(selectedEmpOffice, lub.getLoginspc()));
        mav.addObject("billMonths", billMonths);
        mav.addObject("billYears", billYears);
        return mav;
    }

    @RequestMapping(value = "prepareExtraMonthIncentiveBill", method = RequestMethod.POST, params = "action=Back")
    public String ReturnprepareExtraMonthIncentiveBill(ModelMap model, @ModelAttribute("BillBrowserbean") BillBrowserbean bbbean, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response) {
        return "redirect:/billBrowserAction.htm";
    }

    @RequestMapping(value = "prepareExtraMonthIncentiveBill", method = RequestMethod.POST, params = "action=Process")
    public ModelAndView processprepareExtraMonthIncentiveBill(ModelMap model, @ModelAttribute("BillBrowserbean") BillBrowserbean bbbean, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpOffice") String selectedEmpOffice, BindingResult result, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView("/payroll/ShowBillProcessStatus", "command", bbbean);
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        GlobalBillStatus gbs = billBrowserDao.getBillProcessStatus(STOP_BILL_PROCESS);

        if (gbs.getGlobalVarValue().equalsIgnoreCase("Y")) {

            mav.addObject("msg", gbs.getMessage());
        } else {

            int priority = billBrowserDao.getBillPriority(selectedEmpOffice);
            BillAttr[] billAttr = billBrowserDao.createBillFromBillGroup(bbbean.getSltMonth(), bbbean.getSltYear(), bbbean.getBillgroupId(), bbbean.getProcessDate(), priority, bbbean.getTxtbilltype(), selectedEmpOffice);
            mav.addObject("billAttr", billAttr);
            mav.addObject("msg", "Bill is Under Process. Check After 1 hour");

            //int priority = billBrowserDao.getBillPriority(selectedEmpOffice);
            //BillAttr[] billAttr = billBrowserDao.createBillForExtraMonthIncentive(bbbean.getSltMonth(), bbbean.getSltYear(), bbbean.getBillgroupId(), bbbean.getProcessDate(), priority, bbbean.getTxtbilltype(), selectedEmpOffice);
            // mav.addObject("billAttr", billAttr);
            mav.addObject("msg", "Bill is Under Process. Check After 1 hour");

        }

        return mav;
    }

    @RequestMapping(value = "newArrearBill", method = RequestMethod.POST)
    public ModelAndView newArrearBill(ModelMap model, @ModelAttribute("BillBrowserbean") BillBrowserbean bbbean, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpOffice") String selectedEmpOffice, BindingResult result, HttpServletResponse response) {
        String path = "/payroll/BillBrowser";
        GlobalBillStatus gbs = billBrowserDao.getBillProcessStatus(STOP_BILL_PROCESS);
        ModelAndView mav = null;
        if (gbs.getGlobalVarValue().equalsIgnoreCase("Y")) {
            path = "/payroll/ErrorMessage";

            mav = new ModelAndView(path, "command", bbbean);
            mav.addObject("gbs", gbs.getMessage());
        } else {
            int billYear = billBrowserDao.getNewBillYear(selectedEmpOffice, bbbean.getTxtbilltype());
            int billMonth = billBrowserDao.getNewBillMonth(selectedEmpOffice, billYear, bbbean.getTxtbilltype());
            String monthName = billBrowserDao.getMonthName(billMonth);
            if (billMonth == 11) {
                billYear = billYear + 1;
                billMonth = 0;
            } else {
                billMonth = billMonth + 1;
            }

            bbbean.setSltMonth(billMonth);
            bbbean.setSltYear(billYear);
            path = "/payroll/PrepareArrearBill";
            mav = new ModelAndView(path, "BillBrowserbean", bbbean);
            SelectOption year = new SelectOption();
            year.setLabel(billYear + "");
            year.setValue(billYear + "");
            ArrayList billYears = new ArrayList();
            billYears.add(year);
            SelectOption month = new SelectOption();
            month.setValue(billMonth + "");
            month.setLabel(CalendarCommonMethods.getFullMonthAsString(billMonth));
            ArrayList billMonths = new ArrayList();
            billMonths.add(month);
            mav.addObject("billMonths", billMonths);
            mav.addObject("billYears", billYears);

        }

        mav.setViewName(path);
        return mav;
    }

    @RequestMapping(value = "newleftOutBill", method = RequestMethod.POST)
    public ModelAndView newleftOutBill(ModelMap model, @ModelAttribute("BillBrowserbean") BillBrowserbean bbbean, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpOffice") String selectedEmpOffice, BindingResult result, HttpServletResponse response) {
        String path = "/payroll/NewBillForLeftOutEmp";
        GlobalBillStatus gbs = billBrowserDao.getBillProcessStatus(STOP_BILL_PROCESS);
        ModelAndView mav = null;
        if (gbs.getGlobalVarValue().equalsIgnoreCase("Y")) {
            path = "/payroll/ErrorMessage";

            mav = new ModelAndView(path, "command", bbbean);
            mav.addObject("gbs", gbs.getMessage());
        } else {

            path = "/payroll/NewBillForLeftOutEmp";

            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);

            bbbean.setSltMonth(month);
            bbbean.setSltYear(year);

            mav = new ModelAndView(path, "BillBrowserbean", bbbean);
            bbbean.setOffCode(selectedEmpOffice);

            ArrayList billYears = new ArrayList();
            SelectOption soyear = new SelectOption();
            soyear.setValue(year + "");
            soyear.setLabel(year + "");
            billYears.add(soyear);

            SelectOption so = new SelectOption();
            so.setValue(month + "");
            so.setLabel(CalendarCommonMethods.getFullMonthAsString(month));

            ArrayList billMonths = new ArrayList();
            billMonths.add(so);
            mav.addObject("billMonths", billMonths);
            mav.addObject("billYears", billYears);

        }

        mav.setViewName(path);
        return mav;
    }

    @RequestMapping(value = "prepareNewBillForLeftOut", method = RequestMethod.POST, params = "action=Back")
    public String ReturnLeftOutNewBillform(ModelMap model, @ModelAttribute("BillBrowserbean") BillBrowserbean bbbean, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response) {
        return "redirect:/billBrowserAction.htm";
    }

    @RequestMapping(value = "prepareNewBillForLeftOut", method = RequestMethod.POST, params = "action=Process")
    public ModelAndView ProcessLeftOutNewBillform(ModelMap model, @ModelAttribute("BillBrowserbean") BillBrowserbean bbbean, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView("/payroll/ShowBillProcessStatus", "command", bbbean);
        try {
            billBrowserDao.isContainsKeyForLeftOut(bbbean.getOffCode(), bbbean.getSltMonth(), bbbean.getSltYear(), bbbean.getBillgroupIdASString(), bbbean.getProcessDate(), bbbean.getTxtbilltype(), 0, "REGULAR", 4);

        } catch (Exception e) {
            e.printStackTrace();
        }

        mav.addObject("msg", "Bill is Under Process. Check After 1 hour");
        return mav;
    }

    @RequestMapping(value = "prepareNewBillform", method = RequestMethod.POST, params = "action=Ok")
    public ModelAndView prepareNewBillform(ModelMap model, @ModelAttribute("BillBrowserbean") BillBrowserbean bbbean, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpOffice") String selectedEmpOffice, BindingResult result, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView("/payroll/PrepareBill", "command", bbbean);
        SelectOption year = new SelectOption();
        year.setLabel(bbbean.getSltYear() + "");
        year.setValue(bbbean.getSltYear() + "");
        ArrayList billYears = new ArrayList();
        billYears.add(year);
        SelectOption month = new SelectOption();
        month.setValue(bbbean.getSltMonth() + "");
        month.setLabel(CalendarCommonMethods.getFullMonthAsString(bbbean.getSltMonth()));
        ArrayList billMonths = new ArrayList();
        billMonths.add(month);
        if (lub.getLoginIsDDOType().equals("B") && lub.getLoginOffLevel().equals("20")) {
            System.out.println("lvl");
            mav.addObject("billGroupList", billBrowserDao.getsubOfficeBillGroupList(selectedEmpOffice));
        } else {
            mav.addObject("billGroupList", billBrowserDao.getBillGroupList(selectedEmpOffice, lub.getLoginspc()));
        }
        mav.addObject("billMonths", billMonths);
        mav.addObject("billYears", billYears);
        return mav;
    }

    @RequestMapping(value = "prepareNewBillform", method = RequestMethod.POST, params = "action=Back")
    public String ReturnNewBillform(ModelMap model, @ModelAttribute("BillBrowserbean") BillBrowserbean bbbean, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response) {
        return "redirect:/billBrowserAction.htm";
    }

    @RequestMapping(value = "prepareNewBillform", method = RequestMethod.POST, params = "action=Process")
    public ModelAndView processNewBillform(ModelMap model, @ModelAttribute("BillBrowserbean") BillBrowserbean bbbean, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpOffice") String selectedEmpOffice, BindingResult result, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView("/payroll/ShowBillProcessStatus", "command", bbbean);
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int currentDate = cal.get(Calendar.DATE);

        GlobalBillStatus gbs = billBrowserDao.getBillProcessStatus(STOP_BILL_PROCESS);

        if (gbs.getGlobalVarValue().equalsIgnoreCase("Y")) {

            mav.addObject("msg", gbs.getMessage());
        } else {

            gbs = billBrowserDao.getBillProcessStatus("PROCESS_DATE");

            if (year == bbbean.getSltYear() && month == bbbean.getSltMonth() && Integer.parseInt(gbs.getGlobalVarValue()) > currentDate) {
                mav.addObject("msg", gbs.getMessage());
            } else {

                if ((year >= bbbean.getSltYear() + 1) || (year == bbbean.getSltYear() && bbbean.getSltMonth() <= month)) {
                    Office offObj = officeDao.getOfficeDetails(selectedEmpOffice);
                    System.out.println("offObj.getLvl()+offObj.getIsDdo():" + offObj.getLvl() + ";" + offObj.getIsDdo());

                    String msg = "";
                    if (offObj != null) {

                        if (offObj.getDdoCode() == null || offObj.equals("")) {
                            msg = "DDO code cannot be blank. ";
                        } else if (offObj.getDdoHrmsid() == null || offObj.getDdoHrmsid().equals("")) {
                            msg = "DDO name cannot be blank. ";
                        } else if (offObj.getDdoPost() == null || offObj.getDdoPost().equals("")) {
                            msg = "DDO Designation cannot be blank. ";
                        } else if (Integer.parseInt(offObj.getLvl()) == 20 && offObj.getIsDdo().equals("B")) {
                            System.out.println("getLoginOffLevel");
                            BillAttr[] billAttr = billBrowserDao.createBillFromBillGroupDHE(bbbean.getSltMonth(), bbbean.getSltYear(), bbbean.getBillgroupId(), bbbean.getProcessDate(), offObj.getPaybillPriority(), bbbean.getTxtbilltype(), selectedEmpOffice);
                            mav.addObject("billAttr", billAttr);
                            msg = "Bill Is Under Process..... ";
                        } else {
                            System.out.println("Processed");
                            BillAttr[] billAttr = billBrowserDao.createBillFromBillGroup(bbbean.getSltMonth(), bbbean.getSltYear(), bbbean.getBillgroupId(), bbbean.getProcessDate(), offObj.getPaybillPriority(), bbbean.getTxtbilltype(), selectedEmpOffice);
                            mav.addObject("billAttr", billAttr);
                            msg = "Bill is Under Process. ";
                        }
                        mav.addObject("msg", msg);
                    }

                } else {
                    mav.addObject("msg", "Advance Month Salary Cannot Be prepared");
                }
            }
        }

        return mav;
    }

    @RequestMapping(value = "prepareNewAdvanceBillform", method = RequestMethod.POST, params = "action=Ok")
    public ModelAndView prepareNewAdvanceBillform(ModelMap model, @ModelAttribute("BillBrowserbean") BillBrowserbean bbbean, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpOffice") String selectedEmpOffice, BindingResult result, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView("/payroll/PreparePayAdvBill", "command", bbbean);
        SelectOption year = new SelectOption();
        year.setLabel(bbbean.getSltYear() + "");
        year.setValue(bbbean.getSltYear() + "");
        ArrayList billYears = new ArrayList();
        billYears.add(year);
        SelectOption month = new SelectOption();
        month.setValue(bbbean.getSltMonth() + "");
        month.setLabel(CalendarCommonMethods.getFullMonthAsString(bbbean.getSltMonth()));
        ArrayList billMonths = new ArrayList();
        billMonths.add(month);
        mav.addObject("billGroupList", billBrowserDao.getBillGroupList(selectedEmpOffice, lub.getLoginspc()));
        mav.addObject("billMonths", billMonths);
        mav.addObject("billYears", billYears);
        return mav;
    }

    @RequestMapping(value = "prepareNewAdvanceBillform", method = RequestMethod.POST, params = "action=Back")
    public String ReturnNewAdvanceBillform(ModelMap model, @ModelAttribute("BillBrowserbean") BillBrowserbean bbbean, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response) {
        return "redirect:/billBrowserAction.htm";
    }

    @RequestMapping(value = "prepareNewAdvanceBillform", method = RequestMethod.POST, params = "action=Process")
    public ModelAndView ProcessNewAdvanceBillform(ModelMap model, @ModelAttribute("BillBrowserbean") BillBrowserbean bbbean, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpOffice") String selectedEmpOffice, BindingResult result, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView("/payroll/ShowBillProcessStatus", "command", bbbean);
        GlobalBillStatus gbs = billBrowserDao.getBillProcessStatus(STOP_BILL_PROCESS);

        if (gbs.getGlobalVarValue().equalsIgnoreCase("Y")) {
            mav.addObject("msg", gbs.getMessage());
        } else {
            int priority = billBrowserDao.getBillPriority(selectedEmpOffice);

            if (bbbean.getPercentageArraer() == null || bbbean.getPercentageArraer().equals("")) {
                bbbean.setPercentageArraer("0");
            }

            BillAttr[] billAttr = billBrowserDao.createBillFromBillGroupForAdvancePay(bbbean.getProcessFromDate(), bbbean.getProcessToDate(), bbbean.getBillgroupId(), bbbean.getProcessDateArr(), priority, bbbean.getTxtbilltype(), selectedEmpOffice, Integer.parseInt(bbbean.getPercentageArraer()));
            mav.addObject("billAttr", billAttr);
            mav.addObject("msg", "Bill is Under Process. Check After 1 hour");
        }

        return mav;
    }

    @RequestMapping(value = "prepareNewArrearBillform", method = RequestMethod.POST, params = "action=Ok")
    public ModelAndView prepareNewArrearBillform(ModelMap model, @ModelAttribute("BillBrowserbean") BillBrowserbean bbbean, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpOffice") String selectedEmpOffice, BindingResult result, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView("/payroll/PrepareArrearBill", "command", bbbean);
        SelectOption year = new SelectOption();
        year.setLabel(bbbean.getSltYear() + "");
        year.setValue(bbbean.getSltYear() + "");
        ArrayList billYears = new ArrayList();
        billYears.add(year);
        SelectOption month = new SelectOption();
        month.setValue(bbbean.getSltMonth() + "");
        month.setLabel(CalendarCommonMethods.getFullMonthAsString(bbbean.getSltMonth()));
        ArrayList billMonths = new ArrayList();
        billMonths.add(month);
        //mav.addObject("billGroupList", billBrowserDao.getBillGroupList(selectedEmpOffice, lub.getLoginspc()));
        mav.addObject("billGroupList", billBrowserDao.getArrearBillGroupList(selectedEmpOffice, lub.getLoginspc()));
        mav.addObject("billMonths", billMonths);
        mav.addObject("billYears", billYears);
        return mav;
    }

    @RequestMapping(value = "prepareNewArrearBillform", method = RequestMethod.POST, params = "action=Back")
    public String ReturnNewBillArrearform(ModelMap model, @ModelAttribute("BillBrowserbean") BillBrowserbean bbbean, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response) {
        return "redirect:/billBrowserAction.htm";
    }

    @RequestMapping(value = "prepareNewArrearBillform", method = RequestMethod.POST, params = "action=Process")
    public ModelAndView ProcessNewArrearBillform(ModelMap model, @ModelAttribute("BillBrowserbean") BillBrowserbean bbbean, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpOffice") String selectedEmpOffice, BindingResult result, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView("/payroll/ShowBillProcessStatus", "command", bbbean);
        GlobalBillStatus gbs = billBrowserDao.getBillProcessStatus(STOP_BILL_PROCESS);

        if (gbs.getGlobalVarValue().equalsIgnoreCase("Y")) {
            mav.addObject("msg", gbs.getMessage());
        } else {
            int priority = billBrowserDao.getBillPriority(selectedEmpOffice);

            if (bbbean.getPercentageArraer() == null || bbbean.getPercentageArraer().equals("")) {
                bbbean.setPercentageArraer("0");
            }
            if (bbbean.getTxtbilltype() != null && bbbean.getTxtbilltype().equals("ARREAR_6_J") && bbbean.getPercentageArraerJudiciary() != null && !bbbean.getPercentageArraerJudiciary().equals("")) {
                bbbean.setPercentageArraer(bbbean.getPercentageArraerJudiciary());
            } else if (bbbean.getTxtbilltype() != null && bbbean.getTxtbilltype().equals("ARREAR_J")) {
                bbbean.setPercentageArraer("25");
            }

            BillAttr[] billAttr = billBrowserDao.createBillFromBillGroupForArrear(bbbean.getProcessFromDate(), bbbean.getProcessToDate(), bbbean.getBillgroupId(), bbbean.getProcessDateArr(), priority, bbbean.getTxtbilltype(), selectedEmpOffice, Integer.parseInt(bbbean.getPercentageArraer()));
            mav.addObject("billAttr", billAttr);
            mav.addObject("msg", "Bill is Under Process. Check After 1 hour");
        }

        return mav;
    }

    @RequestMapping(value = "processArrearIndividualBill", method = RequestMethod.GET)
    public ModelAndView processArrearIndividualBill(ModelMap model, @ModelAttribute("BillBrowserbean") BillBrowserbean bbbean, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpOffice") String selectedEmpOffice, BindingResult result, HttpServletResponse response) {
        bbbean.setOffCode(selectedEmpOffice);
        if (bbbean.getTxtbilltype() != null && !bbbean.getTxtbilltype().equals("") && bbbean.getTxtbilltype().equalsIgnoreCase("ARREAR")) {
            BillBrowserbean arrearData = billBrowserDao.getArrearBillPeriod(selectedEmpOffice, bbbean.getSltMonth(), bbbean.getSltYear());
            bbbean.setSltFromMonth(arrearData.getSltFromMonth());
            bbbean.setSltFromYear(arrearData.getSltFromYear());
            bbbean.setSltToMonth(arrearData.getSltToMonth());
            bbbean.setSltToYear(arrearData.getSltToYear());

        }
        ModelAndView mav = new ModelAndView("/payroll/ReprocessArrearBill", "command", bbbean);

        return mav;
    }

    @RequestMapping(value = "payBillReportAction", method = RequestMethod.GET)
    public String payBillReportAction(ModelMap model, @ModelAttribute("BillBrowserbean") BillBrowserbean bbbean, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response) {
        String path = "/payroll/PayBillReport";
        //  HashMap<String, String> listTokenData = esignBillDAO.getTokenRequestData();

        CommonReportParamBean crb = paybillDmpDao.getCommonReportParameter(bbbean.getBillNo());
        List reportList = comonScheduleDao.getDisplayReportList(bbbean.getBillNo(), bbbean.getTxtbilltype(), crb.getRequiredReports(), crb.getAqyear(), crb.getAqmonth());
        if (lub.getLoginoffcode() != null && !lub.getLoginoffcode().equals("")) {
            String allowEsign = comonScheduleDao.allowedOfficeEsign(lub.getLoginoffcode());
            String[] rowArray = allowEsign.split("\\|");
            String allowEsignStatus = rowArray[0];
            String ddohrmsid = rowArray[1];
            model.addAttribute("ddohrmsid", ddohrmsid);
            model.addAttribute("allowEsign", allowEsignStatus);
        } else {
            String allowEsign = comonScheduleDao.allowedOfficeEsign(crb.getOffcode());
            String[] rowArray = allowEsign.split("\\|");
            String allowEsignStatus = rowArray[0];
            String ddohrmsid = rowArray[1];
            model.addAttribute("ddohrmsid", ddohrmsid);
            model.addAttribute("allowEsign", allowEsignStatus);
        }
        model.addAttribute("reportList", reportList);
        model.addAttribute("bbbean", bbbean);
        // model.addAttribute("esign", listTokenData);
        // model.addAttribute("allowEsign", allowEsign);
        model.addAttribute("loginId", lub.getLoginempid());
        //  System.out.println("allowEsignSttaus:" + allowEsignStatus + "ddohrmsid:" + ddohrmsid + "Login id" + lub.getLoginempid() + "Sttaus Id" + crb.getBillStatusId());

        model.addAttribute("statusId", crb.getBillStatusId());

        return path;
    }

    @RequestMapping(value = "processIndividualBill", method = RequestMethod.GET)
    public ModelAndView processIndividualBill(ModelMap model, @ModelAttribute("BillBrowserbean") BillBrowserbean bbbean, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpOffice") String selectedEmpOffice, BindingResult result, HttpServletResponse response) {
        bbbean.setOffCode(selectedEmpOffice);
        ModelAndView mav = new ModelAndView("/payroll/ReprocessBill", "command", bbbean);

        return mav;
    }

    @RequestMapping(value = "showUploadBillStatus", method = RequestMethod.GET)
    public ModelAndView uploadBillStatus(ModelMap model, @ModelAttribute("GetBillStatusBean") GetBillStatusBean statusBill, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response, @RequestParam("billNo") int billNo) {

        statusBill = billBrowserDao.getUploadBillStatus(billNo);

        ModelAndView mav = new ModelAndView("/payroll/GetUploadBillStatus", "command", statusBill);
        return mav;
    }

    /*
     @RequestMapping(value = "processBillform", method = RequestMethod.POST)
     public ModelAndView processBillform(ModelMap model, @ModelAttribute("BillBrowserbean") BillBrowserbean bbbean, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response) {//
     String path = "/payroll/ErrorMessage";
     GlobalBillStatus gbs = billBrowserDao.getBillProcessStatus();
     ModelAndView mav = null;
     if (gbs.getGlobalVarValue().equalsIgnoreCase("Y")) {
     path = "/payroll/ErrorMessage";
     mav = new ModelAndView(path, "command", bbbean);
     mav.addObject("gbs", gbs);
     }else{
            
     }
     return mav;
     }    
     */
    @ResponseBody
    @RequestMapping(value = "getBillMonthYearWise", method = RequestMethod.POST)
    public void getBillMonthYearWise(HttpServletRequest request, @ModelAttribute("BillBrowserbean") BillBrowserbean bbbean, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpOffice") String selectedEmpOffice, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = null;
        ArrayList billMonths = billBrowserDao.getMonthFromSelectedYear(selectedEmpOffice, bbbean.getSltYear(), bbbean.getTxtbilltype());
        JSONArray json = new JSONArray(billMonths);
        out = response.getWriter();
        out.write(json.toString());
    }

    @ResponseBody
    @RequestMapping(value = "getBillMonthYearWiseDHE", method = RequestMethod.POST)
    public void getBillMonthYearWiseDHE(HttpServletRequest request, @ModelAttribute("BillBrowserbean") BillBrowserbean bbbean, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpOffice") String selectedEmpOffice, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = null;
        ArrayList billMonths = billBrowserDao.getMonthFromSelectedYearDHE(selectedEmpOffice, bbbean.getSltYear(), bbbean.getTxtbilltype());
        JSONArray json = new JSONArray(billMonths);
        out = response.getWriter();
        out.write(json.toString());
    }

    @RequestMapping(value = "getPayBillList")
    public ModelAndView getPayBillList(HttpServletRequest request, @ModelAttribute("BillBrowserbean") BillBrowserbean bbbean, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpOffice") String selectedEmpOffice, HttpServletResponse response, @RequestParam(value = "uploaderrorbillno", required = false) String uploaderrorbillno, @RequestParam(value = "xmlfilecounterror", required = false) String xmlfilecounterror) throws IOException {
        String path = "/payroll/BillBrowser";
        boolean isCollegeDHE = false;
        boolean isddoDheOff = false;
        String collegeUnderDhe = null;
        int noofPrivileges = 0;
        ArrayList<BillBean> billList = new ArrayList();
        ArrayList billYears = new ArrayList();
        ArrayList billMonths = new ArrayList();
        boolean accessBills = true;
        String is_DDODHE = null;

        isCollegeDHE = billBrowserDao.isCollegeUnderDHE(selectedEmpOffice);
        isddoDheOff = billBrowserDao.isDDODHE(selectedEmpOffice);

        if (isCollegeDHE) {
            billYears = billBrowserDao.getBillPrepareYearDHE(selectedEmpOffice);
            is_DDODHE = "B";

        } else if (isddoDheOff) {
            billYears = billBrowserDao.getBillPrepareYearDDODHE(selectedEmpOffice);
            is_DDODHE = "Y";
        }
        noofPrivileges = officeDao.hasOfficePriv(lub.getLoginspc());
        Office ofcobj = officeDao.getOfficeDetails(selectedEmpOffice);
        if (ofcobj.getDdoHrmsid() != null && !ofcobj.getDdoHrmsid().equals("")) {
            ofcobj.setDdoHrmsid(ofcobj.getDdoHrmsid());
        }
        System.out.println("bbbean.getBillNo():" + bbbean.getBillNo());

        if (bbbean.getBillNo() != null && !bbbean.getBillNo().equals("")) {
            BillDetail bill = billBrowserDao.getBillDetails(Integer.parseInt(bbbean.getBillNo()));
            selectedEmpOffice = bill.getOffcode();
            bbbean.setSltYear(bill.getBillyear());
            bbbean.setSltMonth(bill.getBillmonth() - 1);
            bbbean.setTxtbilltype(bill.getTypeofBillString());
            System.out.println("bill.getTypeofBillString():" + bill.getTypeofBillString());
        }
        //For DHE--Start--
        isCollegeDHE = billBrowserDao.isCollegeUnderDHE(selectedEmpOffice);//For Colleges Under DHE
        isddoDheOff = billBrowserDao.isDDODHE(selectedEmpOffice);//For DHE(Parent DDO)

        if (isCollegeDHE) {
            collegeUnderDhe = "B";
        }
        if (isddoDheOff) {
            //System.out.println("isddoDheOff");
            billList = billBrowserDao.getPayBillListDHE(bbbean.getSltYear(), bbbean.getSltMonth(), selectedEmpOffice, bbbean.getTxtbilltype(), lub.getLoginspc());
            billYears = billBrowserDao.getBillPrepareYearDHE(selectedEmpOffice);
            billMonths = billBrowserDao.getMonthFromSelectedYearDHE(selectedEmpOffice, bbbean.getSltYear(), bbbean.getTxtbilltype());
        } else if (isCollegeDHE) {
            //System.out.println("isddoDheOff");
            billList = billBrowserDao.getPayBillListDHEColleges(bbbean.getSltYear(), bbbean.getSltMonth(), selectedEmpOffice, bbbean.getTxtbilltype(), lub.getLoginspc());
            billYears = billBrowserDao.getBillPrepareYearDHE(selectedEmpOffice);
            billMonths = billBrowserDao.getMonthFromSelectedYearDHE(selectedEmpOffice, bbbean.getSltYear(), bbbean.getTxtbilltype());

        }// --End--
        else if (lub.getLoginoffcode().equals(selectedEmpOffice)) {
            if (noofPrivileges > 0 || (ofcobj.getDdoHrmsid() != null && ofcobj.getDdoHrmsid().equals(lub.getLoginempid()))) {
                System.out.println("MorethanOnePriv. or DDO:");
                System.out.println("bbbean.getTxtbilltype()::" + bbbean.getTxtbilltype());
                if (bbbean.getTxtbilltype() != null && bbbean.getTxtbilltype().equals("PAY")) {
                    System.out.println("pay");
                    billList = billBrowserDao.getPayBillList(bbbean.getSltYear(), bbbean.getSltMonth(), selectedEmpOffice, bbbean.getTxtbilltype(), lub.getLoginspc());
                } else if (bbbean.getTxtbilltype() != null && bbbean.getTxtbilltype().contains("ARREAR")) {
                    System.out.println("arrear");
                    billList = billBrowserDao.getArrearPayBillList(bbbean.getSltYear(), bbbean.getSltMonth(), selectedEmpOffice, bbbean.getTxtbilltype(), lub.getLoginspc());
                }

                billYears = billBrowserDao.getBillPrepareYear(selectedEmpOffice);
                billMonths = billBrowserDao.getMonthFromSelectedYear(selectedEmpOffice, bbbean.getSltYear(), bbbean.getTxtbilltype());
            } else {
                System.out.println("SinglePriv");
                billList = billBrowserDao.getPayBillList(0, 0, null, bbbean.getTxtbilltype(), lub.getLoginspc());
                billYears = billBrowserDao.getBillPrepareYear(null);
                billMonths = billBrowserDao.getMonthFromSelectedYear(null, 0, bbbean.getTxtbilltype());
                accessBills = false;

            }

        } else {
            System.out.println("OfficePriv.");
            billList = billBrowserDao.getPayBillList(bbbean.getSltYear(), bbbean.getSltMonth(), selectedEmpOffice, bbbean.getTxtbilltype(), lub.getLoginspc());
            billYears = billBrowserDao.getBillPrepareYear(selectedEmpOffice);
            billMonths = billBrowserDao.getMonthFromSelectedYear(selectedEmpOffice, bbbean.getSltYear(), bbbean.getTxtbilltype());
        }
        //ArrayList<BillBean> billList = billBrowserDao.getPayBillList(bbbean.getSltYear(), bbbean.getSltMonth(), selectedEmpOffice, bbbean.getTxtbilltype(), lub.getLoginspc());
        if (bbbean.getTxtbilltype() != null && !bbbean.getTxtbilltype().equals("") && bbbean.getTxtbilltype().equalsIgnoreCase("ARREAR")) {
            BillBrowserbean arrearData = billBrowserDao.getArrearBillPeriod(selectedEmpOffice, bbbean.getSltMonth(), bbbean.getSltYear());
            bbbean.setSltFromMonth(arrearData.getSltFromMonth());
            bbbean.setSltFromYear(arrearData.getSltFromYear());
            bbbean.setSltToMonth(arrearData.getSltToMonth());
            bbbean.setSltToYear(arrearData.getSltToYear());
        } else if (bbbean.getTxtbilltype() != null && !bbbean.getTxtbilltype().equals("") && bbbean.getTxtbilltype().equalsIgnoreCase("ARREAR_6")) {
            BillBrowserbean arrearData = billBrowserDao.getArrearBillPeriod(selectedEmpOffice, bbbean.getSltMonth(), bbbean.getSltYear());
            bbbean.setSltFromMonth(arrearData.getSltFromMonth());
            bbbean.setSltFromYear(arrearData.getSltFromYear());
            bbbean.setSltToMonth(arrearData.getSltToMonth());
            bbbean.setSltToYear(arrearData.getSltToYear());
        } else if (bbbean.getTxtbilltype() != null && !bbbean.getTxtbilltype().equals("") && bbbean.getTxtbilltype().equalsIgnoreCase("OTHER_ARREAR")) {
            BillBrowserbean arrearData = billBrowserDao.getArrearBillPeriod(selectedEmpOffice, bbbean.getSltMonth(), bbbean.getSltYear());
            bbbean.setSltFromMonth(arrearData.getSltFromMonth());
            bbbean.setSltFromYear(arrearData.getSltFromYear());
            bbbean.setSltToMonth(arrearData.getSltToMonth());
            bbbean.setSltToYear(arrearData.getSltToYear());
        }
        bbbean.setTxtbilltype(bbbean.getTxtbilltype());
        ModelAndView mav = new ModelAndView(path, "command", bbbean);
        mav.addObject("billYears", billYears);
        mav.addObject("billMonths", billMonths);
        mav.addObject("billList", billList);
        mav.addObject("is_DDODHE", is_DDODHE);

        /**
         * ************************************* Esign Check Status
         * ****************************
         */
        //CommonReportParamBean crb = paybillDmpDao.getCommonReportParameter(bbbean.getBillNo());
        //  HashMap<String, String> listTokenData = esignBillDAO.getTokenRequestData();
        String allowEsign = comonScheduleDao.allowedOfficeEsign(lub.getLoginoffcode());
        //  mav.addObject("esign", listTokenData);
        String[] rowArray = allowEsign.split("\\|");
        String allowEsignStatus = rowArray[0];
        String ddohrmsid = rowArray[1];
        mav.addObject("loginId", lub.getLoginempid());
        mav.addObject("ddohrmsid", ddohrmsid);
        mav.addObject("allowEsign", allowEsignStatus);
        mav.addObject("collegeUnderDhe", collegeUnderDhe);
        if (accessBills == false) {
            mav.addObject("accessBills", accessBills);
        }

        if (xmlfilecounterror != null && xmlfilecounterror.equals("Y")) {
            mav.addObject("xmlfilecounterror", xmlfilecounterror);
            mav.addObject("uploaderrorbillno", uploaderrorbillno);
        }
        mav.setViewName(path);
        return mav;
    }

    public String getFinancialYear(int sysYear, int sysMonth) {
        String fy = "";
        try {

            if (sysMonth > 2 && sysMonth <= 11) {
                Calendar cal2 = Calendar.getInstance();
                cal2.set(Calendar.YEAR, (sysYear + 1));

                DateFormat df = new SimpleDateFormat("yy"); // Just the year, with 2 digits
                String advYear = df.format(cal2.getTime());

                fy = sysYear + "-" + advYear;
            } else if (sysMonth >= 0 && sysMonth <= 2) {
                Calendar cal2 = Calendar.getInstance();
                cal2.set(Calendar.YEAR, sysYear);

                DateFormat df = new SimpleDateFormat("yy"); // Just the year, with 2 digits
                String currentYear = df.format(cal2.getTime());
                int prevYear = sysYear - 1;

                fy = prevYear + "-" + currentYear;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return fy;
    }

    public String uploadBillDateVerify(String billType, String fromDate, String todate, String billdate) throws Exception {
        String str = "";
        Calendar cal = Calendar.getInstance();
        int curYear = cal.get(Calendar.YEAR);
        int curMonth = cal.get(Calendar.MONTH);
        int todayDate = cal.get(Calendar.DATE);
        DateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        if (todate != null && !todate.equals("")) {

            Date parseToDt = sdf.parse(todate);

            Calendar parseToCAL = Calendar.getInstance();
            parseToCAL.setTime(parseToDt);
            int billToMonth = parseToCAL.get(Calendar.MONTH);
            int billToYear = parseToCAL.get(Calendar.YEAR);
            int billToDate = parseToCAL.get(Calendar.DATE);

            Date parseFromDt = sdf.parse(fromDate);

            Calendar parseFromCAL = Calendar.getInstance();
            parseFromCAL.setTime(parseFromDt);
            int billFromMonth = parseFromCAL.get(Calendar.MONTH);
            int billFromYear = parseFromCAL.get(Calendar.YEAR);

            if (parseFromDt.compareTo(parseToDt) > 0) {
                str = "\nBill From date (" + fromDate + ") Should not be greater than to date (" + todate + ")";
            } else if (billToYear == curYear && billToMonth == 2 && billToMonth == curMonth) {
                str = str + "\nYou cannot upload March month bill in March -" + todate;
            } else if (billToYear > curYear) {
                str = str + "\nFuture year cannot be allowed to upload -" + todate;
            } else if (billToYear == curYear && billToMonth > curMonth) {
                str = str + "\nBill to date is Future date -" + todate;
            } else if (billType.contains("ARREAR") && billToYear == curYear && billToMonth == curMonth && billToDate > todayDate) {
                str = str + "\nBill to date Should not be future Date -" + todate;
            }

        } else {
            str = "Bill to date could not be blank";
        }

        if (billdate != null && !billdate.equals("")) {
            Date parseBillDt = sdf.parse(billdate);

            Calendar parseBillCAL = Calendar.getInstance();
            parseBillCAL.setTime(parseBillDt);
            int billMonth = parseBillCAL.get(Calendar.MONTH);
            int billYear = parseBillCAL.get(Calendar.YEAR);
            int billDate = parseBillCAL.get(Calendar.DATE);

            if (billYear > curYear) {
                str = str + "\nBill Date should not be Future Date -" + billdate;
            } else if (billYear == curYear && billMonth > curMonth) {
                str = str + "\nBill Date should not be Future Date -" + billdate;
            } else if (billYear == curYear && billMonth == curMonth && billDate > todayDate) {
                str = str + "\nBill Date should not be Future Date -" + billdate;
            } /*else if (curYear >= billYear && curMonth >= 2 && billMonth <= 2) {
             str = str + "\nBill Date should not be Previous Financial Year -" + billdate;
             }*/

        } else {
            str = "Bill Date should not be blank. ";
        }

        return str;
    }

    @RequestMapping(value = "submitToIFMS", method = RequestMethod.GET)
    public ModelAndView submitToIFMS(HttpServletRequest request, @ModelAttribute("BillBrowserbean") BillBrowserbean bbbean, @ModelAttribute("SelectedEmpOffice") String selectedEmpOffice, @ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletResponse response, @RequestParam("billNo") int billno, @RequestParam(value = "xmlfilecounterror", required = false) String xmlfilecounterror) throws IOException {
        String path = "/payroll/BillVerificationData";

        long startTime = System.currentTimeMillis();

        String billNo = null;
        String mode = null;
        String year = null;
        String month = null;
        String pageno = null;
        String jndiStr = null;

        double allowAmt = 0;
        double dedAmt = 0;
        String officeCode = "";

        int monthasNumber = 0;
        boolean billdetailNullFound = false;
        boolean objectbreakupNullFound = false;
        boolean bytransferNullFound = false;
        boolean gpfNullFound = false;
        boolean npsNullFound = false;
        boolean headofAcct = true;
        int year2 = 0;
        double grossAmt = 0;
        //double objBrkSum = 0;
        double netAmt = 0;
        double btSum = 0;
        String billGross = "";
        String billNet = "";
        double gpfSum8690 = 0;
        double gpfSum8692 = 0;
        double npsSum = 0;
        double gpfBTsum8690 = 0;
        double gpfBTsum8692 = 0;
        double npsBTsum = 0;

        String DEMAND_NUMBER = "";
        String MAJOR_HEAD = "";
        String SUBMAJOR_HEAD = "";
        String MINOR_HEAD = "";
        String SUB_HEAD = "";
        String DETAIL_HEAD = "";
        String PLAN_STATUS = "";
        String CHARGED_VOTED = "";
        String SECTOR_CODE = "";
        String scheduleSlNo = "";

        ModelAndView mav = null;

        BillDetail bill = new BillDetail();
        String typeOfBill = "";
        int billMonth = 0;
        int billYear = 0;
        //String[] cpfFixedEmpId = new String[i];
        List cntCpfFixedEmp = new ArrayList();
        List cpflist = new ArrayList();
        try {
            Calendar cal = Calendar.getInstance();
            int sysyear = cal.get(Calendar.YEAR);
            int sysmonth = cal.get(Calendar.MONTH);
            int currentDate = cal.get(Calendar.DATE);

            String financialYear = getFinancialYear(sysyear, sysmonth);

            List errorList = new ArrayList();

            bill = billBrowserDao.getBillDetails(billno);

            bbbean.setBillNo(billno + "");
            bbbean.setChartofAcct(billBrowserDao.getBillChartofAccount(Integer.parseInt(bbbean.getBillNo())));
            boolean upverify = true;
            //upverify = billBrowserDao.aerSubmitted(bill.getOffcode(), "2017-18");

            /* Verify whether this office is a ddo office or not. if is_ddo= Y   then allow */
            upverify = billBrowserDao.grantUploadBill(selectedEmpOffice, lub.getLoginoffcode());

            if (bill != null) {
                //grossAmt=vserv.getGrossAmt(con,billNo);
                //netAmt= grossAmt-vserv.getdeductAmt(con,billNo);
                typeOfBill = bill.getTypeofBillString();
                billMonth = bill.getBillmonth() - 1;
                billYear = bill.getBillyear();
                DEMAND_NUMBER = bill.getDemandNumber();
                MAJOR_HEAD = bill.getMajorHead();
                SUBMAJOR_HEAD = bill.getSubMajorHead();
                MINOR_HEAD = bill.getMinorHead();
                SUB_HEAD = bill.getSubHead();
                DETAIL_HEAD = bill.getDetailHead();
                PLAN_STATUS = bill.getPlanStatus();
                CHARGED_VOTED = bill.getChargedVoted();
                SECTOR_CODE = bill.getSectorCode();
                officeCode = bill.getOffcode();
                if (bill.getBillmonth() >= 0) {
                    monthasNumber = bill.getBillmonth() + 1;
                    month = CalendarCommonMethods.getFullNameMonthAsString((bill.getBillmonth()));

                    cal.set(Calendar.YEAR, bill.getBillyear());
                    cal.set(Calendar.MONTH, bill.getBillmonth());
                    cal.set(Calendar.DATE, 1);
                }

                cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DAY_OF_MONTH));

                //--NPS Employees having fixed CPF Value from a bill
                cntCpfFixedEmp = billBrowserDao.getAllFixedCpfEmployeesInBill(billno, bill.getTypeOfBill());
                Iterator cntEmpIterator = cntCpfFixedEmp.iterator();
                BillBrowserbean bbbean1 = null;
                while (cntEmpIterator.hasNext()) {
                    bbbean1 = (BillBrowserbean) cntEmpIterator.next();
                    if (cntCpfFixedEmp.size() > 0) {
                        cpflist.add(bbbean1.getBillEmpid());
                    }
                }
                if (cpflist.size() > 0) {
                    errorList.add("NPS amount is fixed for the employee having Hrmsid : " + cpflist);
                }

                String billDateError = uploadBillDateVerify(bill.getTypeofBillString(), bill.getSalFromdate(), bill.getSalTodate(), bill.getBillDate());
                if (billDateError != null && !billDateError.equals("")) {
                    billdetailNullFound = true;
                    errorList.add(billDateError);
                }

                if (bill.getDdoccode() == null || bill.getDdoccode().equals("")) {
                    billdetailNullFound = true;
                    errorList.add(" DDO code is blank for this Bill. Check in Bill Details file. ");
                }

                if (bill.getAgbillTypeId() == null || bill.getAgbillTypeId().equals("")) {
                    billdetailNullFound = true;
                    errorList.add(" Bill Type is null. ");
                }

                if (bill.getNetAmount() != null && bill.getNetAmount().equals("")) {
                    billdetailNullFound = true;
                    errorList.add(" Bill net amount cannot be blank. ");
                }
                if (StringUtils.isEmpty(bill.getDdohrmisd())) {
                    billdetailNullFound = true;
                    errorList.add("DDO Name cannot be Empty. For add/change ddo name go to Office Details link under Report Box. ");
                }
                if (StringUtils.isEmpty(bill.getDdodesgn())) {
                    billdetailNullFound = true;
                    errorList.add("Designation of DDO cannot be Empty. For add/change ddo name go to Office Details link under Report Box. ");
                }
                if (StringUtils.isEmpty(bill.getBillnumber())) {
                    billdetailNullFound = true;
                    errorList.add("Bill Number cannot be Empty");
                }
                if (StringUtils.isEmpty(bill.getBillDate())) {
                    billdetailNullFound = true;
                    errorList.add("Bill Date cannot be Empty");
                }
                if (StringUtils.isEmpty(bill.getBilldesc())) {
                    billdetailNullFound = true;
                    errorList.add("Bill Description cannot be Empty");
                }
                if (StringUtils.isEmpty(bill.getBillType())) {
                    billdetailNullFound = true;
                    errorList.add("Bill Type cannot be Empty");
                }

                monthasNumber = bill.getBillmonth() + 1;

                if (StringUtils.isEmpty(bill.getDemandNumber())) {
                    billdetailNullFound = true;
                    errorList.add("Demand Number cannot be Empty");
                }
                if (StringUtils.isEmpty(bill.getMajorHead())) {
                    billdetailNullFound = true;
                    errorList.add("Bill Major Head cannot be Empty");
                }
                if (StringUtils.isEmpty(bill.getSubMajorHead())) {
                    billdetailNullFound = true;
                    errorList.add("Bill Sub Major Head cannot be Empty");
                }
                if (StringUtils.isEmpty(bill.getMinorHead())) {
                    billdetailNullFound = true;
                    errorList.add("Bill Minor Head cannot be Empty");
                }
                if (StringUtils.isEmpty(bill.getSubHead())) {
                    billdetailNullFound = true;
                    errorList.add("Bill Sub Head cannot be Empty");
                }
                if (StringUtils.isEmpty(bill.getDetailHead())) {
                    billdetailNullFound = true;
                    errorList.add("Bill Detail Head cannot be Empty");
                }
                if (StringUtils.isEmpty(bill.getPlanStatus())) {
                    billdetailNullFound = true;
                    errorList.add("Plan cannot be Empty");
                }
                if (StringUtils.isEmpty(bill.getChargedVoted())) {
                    billdetailNullFound = true;
                    errorList.add("Charge/Voted cannot be Empty");
                }
                if (StringUtils.isEmpty(bill.getSectorCode())) {
                    billdetailNullFound = true;
                    errorList.add("Sector code cannot be Empty");
                }
                if (StringUtils.isEmpty(bill.getTreasuryCode())) {
                    billdetailNullFound = true;
                    errorList.add("Treasury Code cannot be Empty");
                }

                if (StringUtils.isEmpty(bill.getGrossAmount())) {
                    errorList.add("Bill Gross amount cannot be Empty");

                } else {
                    grossAmt = Double.parseDouble(bill.getGrossAmount());
                    billGross = bill.getGrossAmount();
                }

                if (StringUtils.isEmpty(bill.getNetAmount())) {
                    errorList.add("Bill Net amount cannot be Empty");
                } else {
                    netAmt = Double.parseDouble(bill.getNetAmount());
                    billNet = bill.getNetAmount();
                }

                if (StringUtils.isEmpty(bill.getCoCode())) {
                    errorList.add("Co Code cannot be Empty !");
                }

                if (netAmt > grossAmt) {
                    errorList.add("Bill Net amount cannot be greater than Gross amount.");
                }

                if ((bill.getCoCode() == null || bill.getCoCode().equals(""))) {
                    billdetailNullFound = true;
                    errorList.add("CO Code should not be blank.");

                }
                System.out.println(" bill.getAllowEsign():" + bill.getAllowEsign());

                if (bill.getAllowEsign() != null && bill.getAllowEsign().equals("Y")) {
                    String esignBillStatus = billBrowserDao.getESignedFileStatus(billno);
                    String esignArrearBillStatus = billBrowserDao.getESignedFileStatusForArrearBill(billno);
                    if ((bill.getTypeOfBill() != null && (bill.getTypeOfBill().equals("PAY")))) {
                        if ((Integer.parseInt(bill.getAq_month()) >= 3 && bill.getAq_year().equals("2023")) || (Integer.parseInt(bill.getAq_year()) > 2023)) {
                            //String esignBillStatus = billBrowserDao.getESignedFileStatus(billno);
                            if (esignBillStatus == null || esignBillStatus.equals("")) {
                                errorList.add("Pay Bill is not Digitally Signed");
                            }
                        }
                    } else if ((bill.getTypeOfBill() != null && (bill.getTypeOfBill().contains("ARREAR") || bill.getTypeOfBill().equals("EXTRA_MONTH")))) {
                        if ((Integer.parseInt(bill.getAq_month()) >= 7 && bill.getAq_year().equals("2023")) || (Integer.parseInt(bill.getAq_year()) > 2023)) {
                            if (esignArrearBillStatus == null || esignArrearBillStatus.equals("")) {
                                errorList.add("Arrear Bill is not Digitally Signed");
                            }
                        }
                    }
                }

                HrmsBillConfig billConfig = new HrmsBillConfig();
                billConfig = billBrowserDao.getHrmsBillRestrictionStatus();
                if (billConfig != null) {

                    if (billConfig.getStop_SUBMIT_BILL_FOR_PROFILE().equalsIgnoreCase("Y")) {
                        boolean profileVerifiedStatus = billBrowserDao.isprofileVerifiedBillWise(billno);
                        if (profileVerifiedStatus == false) {
                            billdetailNullFound = true;
                            errorList.add(billConfig.getStop_SUBMIT_BILL_FOR_PROFILE_MESSAGE() + " <a target=\"_blank\" href=\"EmployeeProfileList.htm?billNo=" + billno + "\">Click Here</a>");

                        }

                    }

                    if (sysyear == billYear && billMonth >= 1 && !bill.getAgbillTypeId().equals("43")) {

                        if (billConfig.getStop_SUBMIT_BILL_FOR_AER().equalsIgnoreCase("Y")) {

                            boolean aersubmission = billBrowserDao.isaersubmitted(lub.getLoginoffcode(), financialYear);
                            if (aersubmission == false) {
                                billdetailNullFound = true;
                                errorList.add(billConfig.getStop_SUBMIT_BILL_FOR_AER_MESSAGE() + financialYear + ".");

                            }

                        }
                    }
                    if (sysyear == billYear && billMonth >= 1 && !bill.getAgbillTypeId().equals("43")) {
                        if (billConfig.getStop_SUBMIT_BILL_FOR_AER_CO().equalsIgnoreCase("Y")) {

                            boolean aersubmissionASCO = billBrowserDao.isaersubmittedAsCO(officeCode, financialYear);
                            if (aersubmissionASCO == false) {
                                billdetailNullFound = true;
                                errorList.add(billConfig.getStop_SUBMIT_BILL_FOR_AER_CO_MESSAGE() + financialYear + ".");

                            }

                        }
                    }
                    if (sysyear == billYear && billMonth >= 2 && !bill.getAgbillTypeId().equals("43")) {

                        if (billConfig.getStop_SUBMIT_BILL_FOR_AER_AO().equalsIgnoreCase("Y")) {

                            boolean aersubmissionASAO = billBrowserDao.isaersubmittedAsAO(officeCode, financialYear);
                            if (aersubmissionASAO == false) {
                                billdetailNullFound = true;
                                errorList.add(billConfig.getStop_SUBMIT_BILL_FOR_AER_AO_MESSAGE() + financialYear + ".");

                            }

                        }
                    }
                    if (billConfig.getStop_SUBMIT_BILL_FOR_POST_TERMINATION_SUBMITTED().equalsIgnoreCase("Y")) {

                        boolean aersubmission = billBrowserDao.ispostTerminationDataSubmitted(officeCode, financialYear);

                        if (aersubmission == false) {
                            billdetailNullFound = true;
                            errorList.add(billConfig.getStop_SUBMIT_BILL_FOR_POST_TERMINATION_SUBMITTED_MESSAGE() + " For more details visit http://hrmsorissa.gov.in/notificationaction.php.");

                        }

                    }
                    if (billConfig.getStop_BILL_SUBMISSION().equalsIgnoreCase("Y")) {
                        errorList.add(billConfig.getStop_BILL_SUBMISSION_MESSAGE());

                    }

                    if (typeOfBill != null && !typeOfBill.equals("") && typeOfBill.equalsIgnoreCase("PAY")) {
                        if (sysyear == billYear && sysmonth == billMonth && Integer.parseInt(billConfig.getSubmission_DATE()) > currentDate) {
                            errorList.add(billConfig.getSubmission_DATE_MESSAGE() + " " + billConfig.getSubmission_DATE());
                        }

                    }

                    if (currentDate > Integer.parseInt(billConfig.getClosing_DATE()) && Integer.parseInt(billConfig.getClosing_MONTH()) == sysmonth && Integer.parseInt(billConfig.getClosing_YEAR()) == sysyear) {
                        errorList.add(billConfig.getClosing_DATE_MESSAGE());

                    }
                }

            }

            // There shall be a deferment of 50% in gross salary for the month of March-2020 onwards, in respect of All India Services., IAS, IPS and IFS as per Finance Department letter no. 1386 dt:-31-03-2020
            // boolean isIASBill = billBrowserDao.verifySalaryBillofIAS(billno);
            double basic = billBrowserDao.getBasicAmountBillWise(billno);

            CommonReportParamBean crb = paybillDmpDao.getCommonReportParameter(billno + "");
            String payHead = billFrontPageDmpDao.getPayHead(billno);
            List objlist = billBrowserDao.getOBJXMLData(billno, bill.getTreasuryCode(), basic, bill.getBillDate(), bill.getTypeofBillString(), payHead, crb.getAqmonth(), crb.getAqyear());

            //List objlist=billFunc.getOBJXMLData(con, billNo,treasuryCode, basicPay,billdate);        
            Iterator objItrList = objlist.iterator();
            ObjectBreakup object = null;
            while (objItrList.hasNext()) {
                object = (ObjectBreakup) objItrList.next();

                //objBrkSum = objBrkSum + object.getObjectHeadwiseAmount();
                if (StringUtils.isEmpty(object.getObjectHead())) {
                    objectbreakupNullFound = true;
                    errorList.add("Object Head Found blank.");
                }
                if (StringUtils.isEmpty(object.getObjectHeadwiseAmount() + "")) {
                    objectbreakupNullFound = true;
                    errorList.add("Object Head Amount Found blank.");
                }
            }

            // (billno, bill.getTreasuryCode(), 0, bill.getBillDate(), bill.getTypeofBillString())
            List bytranlist = billBrowserDao.getBTXMLData(billno, bill.getTreasuryCode(), bill.getBillDate(), bill.getTypeofBillString(), crb.getAqmonth(), crb.getAqyear());
            Iterator byItrList = bytranlist.iterator();
            BytransferDetails bytransfer = null;
            while (byItrList.hasNext()) {
                bytransfer = (BytransferDetails) byItrList.next();
                btSum = btSum + bytransfer.getAmount();

                String tempSchSlNo = billBrowserDao.getScheduleSlNo(bytransfer.getBtserialno());
                if (tempSchSlNo != null && !tempSchSlNo.equals("")) {
                    scheduleSlNo = scheduleSlNo + "," + tempSchSlNo;
                }

                if (StringUtils.isEmpty(bytransfer.getBtserialno())) {
                    bytransferNullFound = true;
                    errorList.add("ByTransfer Head Found blank.");
                } else {
                    if (bytransfer.getBtserialno().equals("55545")) {
                        gpfBTsum8690 = gpfBTsum8690 + bytransfer.getAmount();
                    } else if (bytransfer.getBtserialno().equals("57649")) {
                        gpfBTsum8692 = gpfBTsum8692 + bytransfer.getAmount();
                    } else if (bytransfer.getBtserialno().equals("9871") || bytransfer.getBtserialno().equals("32149")) {
                        npsBTsum = npsBTsum + bytransfer.getAmount();
                    }

                }
                if (StringUtils.isEmpty(bytransfer.getAmount() + "")) {
                    bytransferNullFound = true;
                    errorList.add("ByTransfer Head Amount Found blank.");
                }

                if (bytransfer.getBtserialno().equals("60146")
                        || bytransfer.getBtserialno().equals("60188")
                        || bytransfer.getBtserialno().equals("60148")
                        || bytransfer.getBtserialno().equals("60149")
                        || bytransfer.getBtserialno().equals("60150")
                        || bytransfer.getBtserialno().equals("60152")
                        || bytransfer.getBtserialno().equals("60153")
                        || bytransfer.getBtserialno().equals("60154")
                        || bytransfer.getBtserialno().equals("60155")
                        || bytransfer.getBtserialno().equals("60156")
                        || bytransfer.getBtserialno().equals("60157")
                        || bytransfer.getBtserialno().equals("60158")
                        || bytransfer.getBtserialno().equals("60159")
                        || bytransfer.getBtserialno().equals("60160")
                        || bytransfer.getBtserialno().equals("60161")
                        || bytransfer.getBtserialno().equals("60162")
                        || bytransfer.getBtserialno().equals("60163")
                        || bytransfer.getBtserialno().equals("60164")
                        || bytransfer.getBtserialno().equals("60165")
                        || bytransfer.getBtserialno().equals("60166")
                        || bytransfer.getBtserialno().equals("60167")
                        || bytransfer.getBtserialno().equals("60168")
                        || bytransfer.getBtserialno().equals("60169")
                        || bytransfer.getBtserialno().equals("60170")
                        || bytransfer.getBtserialno().equals("60171")
                        || bytransfer.getBtserialno().equals("60172")
                        || bytransfer.getBtserialno().equals("60173")
                        || bytransfer.getBtserialno().equals("60174")
                        || bytransfer.getBtserialno().equals("60175")
                        || bytransfer.getBtserialno().equals("60176")
                        || bytransfer.getBtserialno().equals("60177")
                        || bytransfer.getBtserialno().equals("60178")
                        || bytransfer.getBtserialno().equals("60179")
                        || bytransfer.getBtserialno().equals("60180")
                        || bytransfer.getBtserialno().equals("60181")
                        || bytransfer.getBtserialno().equals("60182")
                        || bytransfer.getBtserialno().equals("60183")
                        || bytransfer.getBtserialno().equals("60184")
                        || bytransfer.getBtserialno().equals("60185")
                        || bytransfer.getBtserialno().equals("60186")
                        || bytransfer.getBtserialno().equals("60187")
                        || bytransfer.getBtserialno().equals("60277")) {

                    errorList.add("BT ID of Festival Advance is changed from fy 2020-21 . So put new BT ID - 60151 By edit Bill Browser and update FA BT ID.");
                }

            }

            if (grossAmt != (netAmt + btSum)) {
                if (!bill.getTypeofBillString().equals("ARREAR_NPS")) {
                    errorList.add("Bill Gross does not match with sum of bill net and by transfer amt . bill gross  amt:-" + billGross + " bill net amt-" + billNet + " total bytransfer amt:-" + btSum + " for more details download bill.");
                }
            }

            ArrayList objList = billBrowserDao.getBeneficiaryList(billno, crb.getTypeofBill());
            if (objList.size() > 0) {
                Iterator itrListBenf = objList.iterator();
                SalaryBenefitiaryDetails sb = new SalaryBenefitiaryDetails();
                while (itrListBenf.hasNext()) {
                    sb = (SalaryBenefitiaryDetails) itrListBenf.next();
                    /* this block of code is for restrict to draw full salary (AIS)
                     if (isIASBill && sb.getBeneficiaryType().equalsIgnoreCase("EMP") && sb.getEmployeeId() != null) {
                     boolean stopSalary = billBrowserDao.foundFiftyPercentGrossForIASCorrect(sb.getEmployeeId(), billno);

                     if (stopSalary) {
                     errorList.add(" 50% gross salary not prepared for given beneficiary. " + sb.getBenf_name());
                     }
                     }
                     */
                    if (sb.getIfGpfAssumed() != null && !sb.getIfGpfAssumed().equals("") && sb.getIfGpfAssumed().equalsIgnoreCase("N")) {
                        if (sb.getGpf_number() != null && !sb.getGpf_number().equals("")) {
                            if (sb.getGpf_series() == null || sb.getGpf_series().equals("")) {
                                errorList.add(" GPF Series is blank of given beneficiary " + sb.getBenf_name());
                            }
                        }
                    }

                    if (sb.getBen_acct_no() == null || sb.getBen_acct_no().equals("")) {
                        errorList.add("Bank Account is blank of given beneficiary " + sb.getBenf_name());
                    }

                    if (sb.getBank_ifsc_code() == null || sb.getBank_ifsc_code().equals("")) {
                        errorList.add("Ifsc Code is blank of given beneficiary " + sb.getBenf_name());
                    }

                    if (sb.getEmpAcctType() != null && sb.getEmpAcctType().equals("PRAN")) {
                        if (sb.getIfGpfAssumed() != null && sb.getIfGpfAssumed().equals("N")) {
                            if (!StringUtils.isNumeric(sb.getGpfAcctNo())) {
                                errorList.add("PRAN No of " + sb.getBenf_name() + " should be numeric");
                            }
                        }
                    } /*else if (sb.getEmpAcctType() == null || sb.getEmpAcctType().equals("")) {
                     errorList.add("Account Type cann't be blank for the employee: " + sb.getEmployeeId());
                     }
                     /* PRAN RESTRICTION */


                    if (bill.getTypeofBillString().equals("PAY")) {
                        if (sb.getEmpAcctType() != null && !sb.getEmpAcctType().equals("GIA")) {
                            if ((sb.getIfReengaged() == null || sb.getIfReengaged().equals("N") || sb.getIfReengaged().equals("")) && (sb.getEmpNonPran() != null && sb.getEmpNonPran().equals("N"))) {
                                if (sb.getIsRegular() != null && !sb.getIsRegular().equals("N") && (sb.getIfGpfAssumed() != null && sb.getIfGpfAssumed().equals("Y")) && !sb.getIsRegular().equals("A")) {
                                    if (sb.getIfStopPayNps() != null && sb.getIfStopPayNps().equals("Y")) {
                                        boolean npstopped = aqmastDao.stopNpsDeduction(sb.getEmployeeId(), billMonth, billYear);
                                        if (npstopped == false) {
                                            errorList.add(sb.getBenf_name() + " has Dummy PRAN No.Please apply for new PRAN.");

                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (bill.getTypeofBillString().equals("ARREAR_J") || bill.getTypeofBillString().equals("ARREAR_6_J")) {
                        if (sb.getLawLevel().equals("N")) {
                            errorList.add(sb.getBenf_name() + " : not belongs to Judiciary employee.");
                        }
                    }
                    if (bill.getTypeofBillString().equals("PAY")) {
                        if ((sb.getEmpType() != null && sb.getEmpType().equals("R")) && (sb.getPaycomm() != null && sb.getPaycomm().equals("7"))) {
                            if (sb.getPaycell() == null || sb.getPaycell().equals("") || sb.getPaylevel() == null || sb.getPaylevel().equals("")
                                    || sb.getPayscale() == null || sb.getPayscale().equals("")) {
                                errorList.add(sb.getBenf_name() + " Pay CELL and LEVEL / Pay Scale cann't be blank. ");
                            }
                        }

                    }

                }
            } else {
                if (!bill.getTypeofBillString().equals("ARREAR_NPS")) {
                    if (errorList.size() > 0) {
                        BillDetail bd = new BillDetail();
                        bd.setBillnumber(billno + "");
                        String ipaddress = CommonFunctions.getIpAndHost(request);
                        billBrowserDao.unlockBill(bd, lub.getLoginuserid(), ipaddress);
                    }
                    errorList.add("Beneficiary Details found blank.  For more details download bill. ");
                }

            }

            //scheduleSlNo = billBrowserDao.getFixedScheduleSlNo(bill.getTypeofBillString());
            scheduleSlNo = "";
            //int billMastUpd = billBrowserDao.updateRequiredReportsColumn(scheduleSlNo, billno);

            List npslist = billBrowserDao.getNPSXMLData(billno, bill.getBillDate(), bill.getBillmonth(), bill.getBillyear(), bill.getTypeofBillString(), crb.getAqmonth(), crb.getAqyear());

            Iterator itrListNPS = npslist.iterator();
            NPSDetails nps = null;

            while (itrListNPS.hasNext()) {
                nps = (NPSDetails) itrListNPS.next();
                if (StringUtils.isEmpty(nps.getDdoRegno())) {
                    npsNullFound = false;
                    errorList.add("DDO Registration number is blank ");
                }
                if (StringUtils.isEmpty(nps.getHrmsgeneratedRefno() + "")) {
                    npsNullFound = true;
                }
                if (StringUtils.isEmpty(nps.getBasic() + "")) {
                    npsNullFound = true;
                }
                if (StringUtils.isEmpty(nps.getNameofSubscrib())) {
                    npsNullFound = true;
                }
                if (StringUtils.isEmpty(nps.getPran())) {
                    npsNullFound = true;
                }

                if (StringUtils.isEmpty(nps.getSc())) {
                    npsNullFound = true;

                } else {
                    npsSum = npsSum + Double.parseDouble(nps.getSc());
                }
                if (nps.getGc() != null && !nps.getGc().equals("")) {
                    int gc = Integer.parseInt(nps.getGc());
                    if (gc < 1) {
                        errorList.add("Government Contribution of " + nps.getPran() + " is wrong. Refer Annexure 2 for details.");
                    }
                }

                if ((nps.getIfGPFAssumed() != null && nps.getIfGPFAssumed().equals("Y")) && !nps.getUserType().equals("A")) {
                    boolean npsNotStopped = aqmastDao.stopShowingDummyPranForRetiredEmp(nps.getNpsEmpid(), crb.getAqmonth(), crb.getAqyear());
                    if (nps.getIfStopPayNps() != null && nps.getIfStopPayNps().equals("Y")) {
                        //System.out.println("npsdetails:"+nps.getNpsEmpid()+":"+bill.getBillmonth());
                        if (npsNotStopped == false) {
                            errorList.add(nps.getPran() + " has Dummy PRAN No.Please apply for new PRAN...");
                        }

                    }
                }

            }

            bbbean.setSltMonth(crb.getAqmonth());
            bbbean.setSltYear(crb.getAqyear());
            bbbean.setTxtbilltype(crb.getTypeofBill());

            BillDetail billDtls = billBrowserDao.getBillDetails(Integer.parseInt(bbbean.getBillNo()));
            bbbean.setBilldesc(billDtls.getBillnumber());
            bbbean.setBenificiaryNumber(billDtls.getBeneficiaryrefno());
            bbbean.setVchDt(billDtls.getVchDt());
            bbbean.setVchNo(billDtls.getVchNo());
            bbbean.setBillDate(billDtls.getBillDate());
            bbbean.setTreasury(billDtls.getTreasuryCode());
            mav = new ModelAndView(path, "command", bbbean);

            if (upverify == false) {
                errorList.add("You are not authorized to upload bill.");
            }

            bbbean.setStatus(billDtls.getBillStatusId());
            if (errorList.size() > 0) {
                BillDetail bd = new BillDetail();
                bd.setBillnumber(billno + "");
                String ipaddress = CommonFunctions.getIpAndHost(request);
                billBrowserDao.unlockBill(bd, lub.getLoginuserid(), ipaddress);
            }
            if (xmlfilecounterror != null && xmlfilecounterror.equals("Y")) {
                errorList.add("Uploaded XML Files count is wrong.");
            }
            int pvtDeductArrearAmt = billBrowserDao.getDDORecoveryList(Integer.parseInt(bbbean.getBillNo()));
            List treasuryList = treasuryDao.getTreasuryList();
            mav.addObject("allowanceList", billBrowserDao.getAllowanceList(Integer.parseInt(bbbean.getBillNo()), crb.getAqmonth(), crb.getAqyear()));
            mav.addObject("deductionList", billBrowserDao.getDeductionList(Integer.parseInt(bbbean.getBillNo()), crb.getAqmonth(), crb.getAqyear()));
            mav.addObject("pvtloanList", billBrowserDao.getPvtLoanList(Integer.parseInt(bbbean.getBillNo()), crb.getAqmonth(), crb.getAqyear()));
            mav.addObject("treasuryList", treasuryList);
            mav.addObject("billDtls", billDtls);
            mav.addObject("pvtDeductArrearAmt", pvtDeductArrearAmt);
            mav.addObject("errorList", errorList);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }

        return mav;
    }

    @RequestMapping(value = "saveBill", params = "action=Upload", method = RequestMethod.POST)
    public ModelAndView upload(HttpServletRequest request, @ModelAttribute("BillBrowserbean") BillBrowserbean bbbean, @ModelAttribute("SelectedEmpOffice") String selectedEmpOffice,
            @ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletResponse response) {
        String billDetailFile = "BILL_DETAILS";
        String folderPath = servletContext.getInitParameter("payBillXMLDOC");
        String pdfPath = servletContext.getInitParameter("PayBillPDf");
        Calendar cal = Calendar.getInstance();
        //BillDetail bill = new BillDetail();
        com.itextpdf.text.Document document = null;
        String path = "redirect:/getPayBillList.htm";
        ModelAndView mav = null;
        int pvtDeductArrearAmt = 0;
        try {

            BillDetail bill = billBrowserDao.getBillDetails(Integer.parseInt(bbbean.getBillNo()));
            bbbean.setSltYear(bill.getBillyear());
            CommonReportParamBean crb = paybillDmpDao.getCommonReportParameter(bbbean.getBillNo());

            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
            String submissionDate = dateFormat.format(cal.getTime());
            int billno = Integer.parseInt(bbbean.getBillNo());

            boolean upverify = true;
            //upverify = billBrowserDao.aerSubmitted(bill.getOffcode(), "2017-18");

            /* Verify whether this office is a ddo office or not. if is_ddo= Y   then allow */
            upverify = billBrowserDao.grantUploadBill(selectedEmpOffice, lub.getLoginoffcode());
            pvtDeductArrearAmt = billBrowserDao.getDDORecoveryList(Integer.parseInt(bbbean.getBillNo()));

            if (upverify) {
                bill = billBrowserDao.getBillDetails(billno);

                billDetailFile = billDetailFile + "_" + bill.getDdoccode() + "_" + bill.getBillmonth() + bill.getBillyear() + ".xml";
                createXMLBillDetails(folderPath + BillBrowserController.FILE_SEPARATOR + bill.getHrmsgeneratedRefno(), billDetailFile, bill);

                ArrayList objList = billBrowserDao.getBeneficiaryList(billno, crb.getTypeofBill());
                billDetailFile = "BENEFICIARY_DETAILS" + "_" + bill.getDdoccode() + "_" + bill.getBillmonth() + bill.getBillyear() + ".xml";
                createXMLBeneficiary(folderPath + BillBrowserController.FILE_SEPARATOR + bill.getHrmsgeneratedRefno(), billDetailFile, objList);

                /*
                 * Object Breakup data is collected here
                 * */
                double basic = billBrowserDao.getBasicAmountBillWise(billno);
                String payHead = billFrontPageDmpDao.getPayHead(billno);
                ArrayList objlist = billBrowserDao.getOBJXMLData(billno, bill.getTreasuryCode(), basic, bill.getBillDate(), bill.getTypeofBillString(), payHead, crb.getAqmonth(), crb.getAqyear());
                billDetailFile = "OBJ_BREAKUP" + "_" + bill.getDdoccode() + "_" + bill.getBillmonth() + bill.getBillyear() + ".xml";
                createXMLObjectBreakUp(folderPath + BillBrowserController.FILE_SEPARATOR + bill.getHrmsgeneratedRefno(), billDetailFile, objlist);

                /*
                 * GPF data is collected here
                 * */
                ArrayList gpflist = new ArrayList();
                gpflist = billBrowserDao.getGPFXMLData(billno, bill.getBillDate(), bill.getBillmonth(), bill.getBillyear(), bill.getSalFromdate(), bill.getSalTodate(), bill.getTypeofBillString(), crb.getAqmonth(), crb.getAqyear());

                billDetailFile = "GPF_DETAILS" + "_" + bill.getDdoccode() + "_" + bill.getBillmonth() + bill.getBillyear() + ".xml";
                createXMLGPF(folderPath + BillBrowserController.FILE_SEPARATOR + billno, billDetailFile, gpflist);

                /*
                 * TPF data is collected here
                 * */
                ArrayList tpflist = new ArrayList();
                tpflist = billBrowserDao.getTPFXMLData(billno, bill.getBillDate(), bill.getBillmonth(), bill.getBillyear(), bill.getSalFromdate(), bill.getSalTodate(), bill.getTypeofBillString(), crb.getAqmonth(), crb.getAqyear());
                billDetailFile = "TPF_DETAILS" + "_" + bill.getDdoccode() + "_" + bill.getBillmonth() + bill.getBillyear() + ".xml";
                createXMLTPF(folderPath + BillBrowserController.FILE_SEPARATOR + billno, billDetailFile, tpflist);

                /*
                 * Bytransfer Data is collected here
                 * 
                 */
                ArrayList bytranlist = billBrowserDao.getBTXMLData(billno, bill.getTreasuryCode(), bill.getBillDate(), bill.getTypeofBillString(), crb.getAqmonth(), crb.getAqyear());
                billDetailFile = "BT_DETAILS" + "_" + bill.getDdoccode() + "_" + bill.getBillmonth() + bill.getBillyear() + ".xml";
                createXMLBT(folderPath + BillBrowserController.FILE_SEPARATOR + bill.getHrmsgeneratedRefno(), billDetailFile, bytranlist);

                /*
                 * Data for NPS
                 * */
                ArrayList npslist = billBrowserDao.getNPSXMLData(billno, bill.getBillDate(), bill.getBillmonth(), bill.getBillyear(), bill.getTypeofBillString(), crb.getAqmonth(), crb.getAqyear());
                billDetailFile = "NPS_DETAILS" + "_" + bill.getDdoccode() + "_" + bill.getBillmonth() + bill.getBillyear() + ".xml";
                createXMLCPF(folderPath + BillBrowserController.FILE_SEPARATOR + billno, billDetailFile, npslist);

                /*
                 * Zip file is created and uploaded to server
                 * */
                int count = 0;
                count = billBrowserDao.getbillsubmissionCount(billno);
                String zipFilename = "";
                if (count > 0) {
                    zipFilename = bill.getDdoccode() + "-" + CalendarCommonMethods.getFullMonthAsString(bill.getBillmonth() - 1) + "-" + bill.getBillyear() + "-" + billno + "_" + count + ".zip";
                } else {
                    zipFilename = bill.getDdoccode() + "-" + CalendarCommonMethods.getFullMonthAsString(bill.getBillmonth() - 1) + "-" + bill.getBillyear() + "-" + billno + ".zip";
                }
                String[] signedsinglepagesalaryfilepath = {};
                if (bill.getTypeOfBill() != null && bill.getTypeOfBill().contains("ARREAR")) {
                    int frontpageslno = billBrowserDao.getBillFrontPageSlNo(bill.getTypeOfBill());
                    signedsinglepagesalaryfilepath = billBrowserDao.getSignedSinglePageSalaryPDFPath(billno, frontpageslno);
                } else {
                    signedsinglepagesalaryfilepath = billBrowserDao.getSignedSinglePageSalaryPDFPath(billno, 2);
                }
                if (signedsinglepagesalaryfilepath[0] != null) {
                    FileUtils.copyFile(new File(signedsinglepagesalaryfilepath[0] + BillBrowserController.FILE_SEPARATOR + signedsinglepagesalaryfilepath[1]), new File(folderPath + BillBrowserController.FILE_SEPARATOR + billno + BillBrowserController.FILE_SEPARATOR + signedsinglepagesalaryfilepath[1]));
                }

                File directory = new File(folderPath + BillBrowserController.FILE_SEPARATOR + billno);
                String[] files = directory.list();
                byte[] zip = zipFiles(directory, files);

                System.out.println("files length is: " + files.length);
                if (files.length == 8) {

                    try {
                        FileOutputStream fout = new FileOutputStream(new File(folderPath + BillBrowserController.FILE_SEPARATOR + zipFilename));
                        fout.write(zip);
                        fout.close();
                        fout.flush();
                        FileUtils.deleteDirectory(directory);
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }

                    /*String[] signedsinglepagesalaryfilepath = billBrowserDao.getSignedSinglePageSalaryPDFPath(billno,2);
                
                     directory = new File(signedsinglepagesalaryfilepath[0] + BillBrowserController.FILE_SEPARATOR + signedsinglepagesalaryfilepath[1]);
                     files = directory.list();
                     zip = zipFiles(directory, files);
                     fout = new FileOutputStream(new File(folderPath + BillBrowserController.FILE_SEPARATOR + zipFilename));
                     fout.write(zip);
                     fout.close();
                     fout.flush();  */
                    //ftpUpload(zip, zipFilename);
                    billBrowserDao.updateBillStatus(billno, 3);
                    String billgrossNet = "Gross: " + bill.getGrossAmount() + "  / Net Amount:" + bill.getNetAmount();
                    billBrowserDao.updateBillHistory(billno, submissionDate, billgrossNet);
                } else if (files.length < 8) {
                    FileUtils.deleteDirectory(directory);
                    path = "redirect:/getPayBillList.htm?uploaderrorbillno=" + billno + "&xmlfilecounterror=Y";
                }
                mav = new ModelAndView(path, "command", bbbean);
                /* 
                
                 File preserve work starts here (PDF files to be compreesed bill wise)
                
                 */
                String absolutePath = pdfPath + BillBrowserController.FILE_SEPARATOR + bill.getBillyear() + BillBrowserController.FILE_SEPARATOR + CalendarCommonMethods.getFullMonthAsString(bill.getBillmonth() - 1) + BillBrowserController.FILE_SEPARATOR + bill.getDdoccode() + BillBrowserController.FILE_SEPARATOR + billno;
                String filePattern = bill.getDdoccode() + "-" + CalendarCommonMethods.getFullMonthAsString(bill.getBillmonth() - 1) + "-" + bill.getBillyear() + "-" + billno;

                File dr = new File(absolutePath);
                if (!dr.exists()) {
                    dr.mkdirs();
                    // If you require it to make the entire directory path including parents,
                    // use directory.mkdirs(); here instead.
                }

                FileOutputStream pdfout = null;

                // BILL FRONT PAGE SCHEDULE
                /*
                 document = new com.itextpdf.text.Document(PageSize.A4);
                 pdfout = new FileOutputStream(new File(absolutePath + BillBrowserController.FILE_SEPARATOR + "FRONTPAGE_" + filePattern + ".pdf"));
                 PdfWriter.getInstance(document, pdfout);
                 document.open();

                 BillChartOfAccount billChartOfAccount = aqReportDAO.getBillChartOfAccount(billno + "");
                 BillObjectHead boha = aqReportDAO.getBillObjectHeadAndAmt(billno + "", crb.getAqyear(), crb.getAqmonth());
                 ArrayList scheduleList = billFrontPageDmpDao.getScheduleListWithADCode(billno + "", crb.getAqmonth(), crb.getAqyear());
                 List oaList = aqReportDAO.getAllowanceDetails(billno + "", crb.getAqyear(), crb.getAqmonth());

                 int spAmt = billFrontPageDmpDao.getSpecialPayAmount(billno, crb.getAqmonth(), crb.getAqyear());
                 int irAmt = billFrontPageDmpDao.getIrAmount(billno, crb.getAqmonth(), crb.getAqyear());
                 int payAmt = aqReportDAO.getPayAmt(billno);

                 String monthName = CalendarCommonMethods.getFullMonthAsString(crb.getAqmonth());
                 monthName = monthName + " - " + crb.getAqyear();

                 comonScheduleDao.billFrontPagePDF(document, monthName, billno + "", billChartOfAccount, boha, scheduleList, oaList, spAmt, irAmt, payAmt, payHead);

                 document.close();
                 fout.close();
                 fout.flush();

                 // AQUITANCE-1 PDF FILE GENERATED BLOCK
                 document = new com.itextpdf.text.Document(PageSize.A4);
                 pdfout = new FileOutputStream(new File(absolutePath + BillBrowserController.FILE_SEPARATOR + "AQUITANCE-1_" + filePattern + ".pdf"));
                 PdfWriter.getInstance(document, pdfout);
                 document.open();
                 String format = "f1";
                 ArrayList aqlist = new ArrayList();
                 String year = "";
                 String month = "";
                 String billdesc = "";
                 String billdate = "";
                 String empType = "";
                 int col19Tot = 0;
                 int col20Tot = 0;
                 int col191Tot = 0;
                 int col201Tot = 0;
                 int col202Tot = 0;
                 String netPay = "";

                 AqreportBean aqreportFormBean = new AqreportBean();

                 year = crb.getAqyear() + "";
                 month = crb.getAqmonth() + "";
                 billdesc = crb.getBilldesc();
                 billdate = crb.getBilldate();
                 empType = aqReportDAO.getEmpType(billno + "", month, year);
                 BillConfigObj billConfig = aqReportDAO.getBillConfig(billno + "");
                 if (format != null && format.equals("f1")) {
                 aqlist = aqReportDAO.getSectionWiseBillDtls(billno + "", month, year, "f1", billConfig, empType);
                 } else if (format != null && format.equals("f2")) {
                 aqlist = aqReportDAO.getSectionWiseBillDtls(billno + "", month, year, "f2", billConfig, empType);
                 }
                 aqreportFormBean.setAqlist(aqlist);

                 aqreportFormBean.setOffen(crb.getOfficeen());
                 aqreportFormBean.setDept(crb.getDeptname());
                 aqreportFormBean.setDistrict(crb.getDistrict());
                 aqreportFormBean.setState(crb.getStatename());
                 aqreportFormBean.setMonth(aqReportDAO.getMonth(Integer.parseInt(month)));
                 aqreportFormBean.setYear(year);
                 aqreportFormBean.setBilldesc(billdesc);
                 aqreportFormBean.setBilldate(billdate);
                 int col3Tot = aqReportDAO.getColGrandTotal(aqlist, "col3", "BASIC", null) + aqReportDAO.getColGrandTotal(aqlist, "col3", "SP", null) + aqReportDAO.getColGrandTotal(aqlist, "col3", "GP", null) + aqReportDAO.getColGrandTotal(aqlist, "col3", "IR", null);
                 int col4Tot = aqReportDAO.getColGrandTotal(aqlist, "col4", "GP", null) + aqReportDAO.getColGrandTotal(aqlist, "col4", "PPAY", null);
                 int col5Tot = aqReportDAO.getColGrandTotal(aqlist, "col5", 0, null) + aqReportDAO.getColGrandTotal(aqlist, "col5", 1, null);
                 int col6Tot = aqReportDAO.getColGrandTotal(aqlist, "col6", "HRA", null) + aqReportDAO.getColGrandTotal(aqlist, "col6", "ADLHRA", null) + aqReportDAO.getColGrandTotal(aqlist, "col6", "LFQ", null) + aqReportDAO.getColGrandTotal(aqlist, "col6", "RAFAL", null);
                 //int col7Tot = aqReportDAO.getColGrandTotal(aqlist, "col7", 0, null) + aqReportDAO.getColGrandTotal(aqlist, "col7", 1, null) + aqReportDAO.getColGrandTotal(aqlist, "col7", 2, null) + aqReportDAO.getColGrandTotal(aqlist, "col7", 3, null) + aqReportDAO.getColGrandTotal(aqlist, "col7", 4, null) + aqReportDAO.getColGrandTotal(aqlist, "col7", 5, null) + aqReportDAO.getColGrandTotal(aqlist, "col7", 6, null) + aqReportDAO.getColGrandTotal(aqlist, "col7", 7, null);
                 int col8Tot = aqReportDAO.getColGrandTotal(aqlist, "col8", "GROSS PAY", null);
                 int col9Tot = aqReportDAO.getColGrandTotal(aqlist, "col9", "LIC", null) + aqReportDAO.getColGrandTotal(aqlist, "col9", "PLI", null);
                 int col10Tot = aqReportDAO.getColGrandTotal(aqlist, "col10", "CPF", null) + aqReportDAO.getColGrandTotal(aqlist, "col10", "GPF", null) + aqReportDAO.getColGrandTotal(aqlist, "col10", "TPF", null) + aqReportDAO.getColGrandTotal(aqlist, "col10", "GA", null) + aqReportDAO.getColGrandTotal(aqlist, "col10", "TPFGA", null) + aqReportDAO.getColGrandTotal(aqlist, "col10", "GPDD", null) + aqReportDAO.getColGrandTotal(aqlist, "col10", "GPIR", null);
                 int col11Tot = aqReportDAO.getColGrandTotal(aqlist, "col11", "PT", null) + aqReportDAO.getColGrandTotal(aqlist, "col11", "IT", null);
                 int col12Tot = aqReportDAO.getColGrandTotal(aqlist, "col12", "HRR", null) + aqReportDAO.getColGrandTotal(aqlist, "col12", "WRR", null) + aqReportDAO.getColGrandTotal(aqlist, "col12", "SWR", null) + aqReportDAO.getColGrandTotal(aqlist, "col12", "HC", null);
                 int col13Tot = aqReportDAO.getColGrandTotal(aqlist, "col13", "HBA", "P") + aqReportDAO.getColGrandTotal(aqlist, "col13", "HBA", "I") + aqReportDAO.getColGrandTotal(aqlist, "col13", "SHBA", "P") + aqReportDAO.getColGrandTotal(aqlist, "col13", "SHBA", "I");
                 int col14Tot = aqReportDAO.getColGrandTotal(aqlist, "col14", "MCA", "P") + aqReportDAO.getColGrandTotal(aqlist, "col14", "MCA", "I") + aqReportDAO.getColGrandTotal(aqlist, "col14", "MOPA", "P") + aqReportDAO.getColGrandTotal(aqlist, "col14", "MOPA", "I");
                 int col15Tot = aqReportDAO.getColGrandTotal(aqlist, "col15", "VE", "P") + aqReportDAO.getColGrandTotal(aqlist, "col15", "VE", "I") + aqReportDAO.getColGrandTotal(aqlist, "col15", "BI", "P") + aqReportDAO.getColGrandTotal(aqlist, "col15", "BI", "I");
                 int col16Tot = aqReportDAO.getColGrandTotal(aqlist, "col16", "PAY", null) + aqReportDAO.getColGrandTotal(aqlist, "col16", "MED", null) + aqReportDAO.getColGrandTotal(aqlist, "col16", "TRADE", null) + aqReportDAO.getColGrandTotal(aqlist, "col16", "OVDL", null);
                 int col17Tot = aqReportDAO.getColGrandTotal(aqlist, "col17", "FA", null) + aqReportDAO.getColGrandTotal(aqlist, "col17", "NPSL", null) + aqReportDAO.getColGrandTotal(aqlist, "col17", "EP", null) + +aqReportDAO.getColGrandTotal(aqlist, "col17", "AUDR", null);
                 int col18Tot = aqReportDAO.getColGrandTotal(aqlist, "col18", "OR", null) + aqReportDAO.getColGrandTotal(aqlist, "col18", "GISA", "P") + aqReportDAO.getColGrandTotal(aqlist, "col18", "GIS", null) + aqReportDAO.getColGrandTotal(aqlist, "col18", "CMPA", null);
                 if (format != null && format.equals("f1")) {
                 col19Tot = aqReportDAO.getColGrandTotal(aqlist, "col19", "TOTDEN", null);
                 col20Tot = aqReportDAO.getColGrandTotal(aqlist, "col20", "NETPAY", null);
                 netPay = Numtowordconvertion.convertNumber(aqReportDAO.getColGrandTotal(aqlist, "col20", "NETPAY", null));
                 } else if (format != null && format.equals("f2")) {
                 col19Tot = aqReportDAO.getColGrandTotal(aqlist, "col19", "TOTDEN", null);
                 col191Tot = aqReportDAO.getColGrandTotal(aqlist, "col19", "NETPAY", null);

                 col20Tot = aqReportDAO.getColGrandTotal(aqlist, "col20", "PVTDED", null);
                 col201Tot = aqReportDAO.getColGrandTotal(aqlist, "col20", "BANKLOAN", null);
                 col202Tot = aqReportDAO.getColGrandTotal(aqlist, "col20", "NETBALANCE", null);
                 netPay = Numtowordconvertion.convertNumber(aqReportDAO.getColGrandTotal(aqlist, "col20", "PVTDED", null) + aqReportDAO.getColGrandTotal(aqlist, "col20", "BANKLOAN", null) + aqReportDAO.getColGrandTotal(aqlist, "col20", "NETBALANCE", null));
                 }
                 aqreportFormBean.setCol3Tot(col3Tot);
                 aqreportFormBean.setCol4Tot(col4Tot);
                 aqreportFormBean.setCol5Tot(col5Tot);
                 aqreportFormBean.setCol6Tot(col6Tot);
                 //aqreportFormBean.setCol7Tot(col7Tot);
                 aqreportFormBean.setCol8Tot(col8Tot);
                 aqreportFormBean.setCol9Tot(col9Tot);
                 aqreportFormBean.setCol10Tot(col10Tot);
                 aqreportFormBean.setCol11Tot(col11Tot);
                 aqreportFormBean.setCol12Tot(col12Tot);
                 aqreportFormBean.setCol13Tot(col13Tot);
                 aqreportFormBean.setCol14Tot(col14Tot);
                 aqreportFormBean.setCol15Tot(col15Tot);
                 aqreportFormBean.setCol16Tot(col16Tot);
                 aqreportFormBean.setCol17Tot(col17Tot);
                 aqreportFormBean.setCol18Tot(col18Tot);
                 aqreportFormBean.setCol19Tot(col19Tot);
                 aqreportFormBean.setCol191Tot(col191Tot);
                 aqreportFormBean.setNetPay(netPay);
                 aqreportFormBean.setCol20Tot(col20Tot);
                 aqreportFormBean.setCol201Tot(col201Tot);
                 aqreportFormBean.setCol202Tot(col202Tot);
                 int totHrr = aqReportDAO.getColGrandTotal(aqlist, "col12", "HRR", null);
                 int totHbaPri = aqReportDAO.getColGrandTotal(aqlist, "col13", "HBA", "P");
                 int totShbaPri = aqReportDAO.getColGrandTotal(aqlist, "col13", "SHBA", "P");
                 int totMcaPri = aqReportDAO.getColGrandTotal(aqlist, "col14", "MCA", "P") + aqReportDAO.getColGrandTotal(aqlist, "col14", "MOPA", "P");
                 int totShbaInt = aqReportDAO.getColGrandTotal(aqlist, "col13", "SHBA", "I");
                 int totBicycPri = aqReportDAO.getColGrandTotal(aqlist, "col15", "BI", "P");
                 int totPt = aqReportDAO.getColGrandTotal(aqlist, "col11", "PT", null);
                 int totGisaPri = aqReportDAO.getColGrandTotal(aqlist, "col18", "GISA", "P");
                 int totIt = aqReportDAO.getColGrandTotal(aqlist, "col11", "IT", null);
                 int totGpf = aqReportDAO.getColGrandTotal(aqlist, "col10", "GPF", null) + aqReportDAO.getColGrandTotal(aqlist, "col10", "GA", null) + aqReportDAO.getColGrandTotal(aqlist, "col10", "GPDD", null) + aqReportDAO.getColGrandTotal(aqlist, "col10", "GPIR", null);
                 int totMopInt = aqReportDAO.getColGrandTotal(aqlist, "col14", "MCA", "I") + aqReportDAO.getColGrandTotal(aqlist, "col14", "MOPA", "I");
                 int totHc = aqReportDAO.getColGrandTotal(aqlist, "col12", "HC", null);
                 int totHbaInt = aqReportDAO.getColGrandTotal(aqlist, "col13", "HBA", "I");
                 int totGis = aqReportDAO.getColGrandTotal(aqlist, "col18", "GIS", null);
                 int totVehicleInt = aqReportDAO.getColGrandTotal(aqlist, "col15", "VE", "I");
                 int totCpf = aqReportDAO.getColGrandTotal(aqlist, "col10", "CPF", null);
                 int totPa = aqReportDAO.getColGrandTotal(aqlist, "col16", "PA", null);
                 int totCgegis = aqReportDAO.getColGrandTotal(aqlist, "col18", "CGEGIS", null);
                 int totVehiclePri = aqReportDAO.getColGrandTotal(aqlist, "col15", "VE", "P");
                 int totTpf = aqReportDAO.getColGrandTotal(aqlist, "col10", "TPF", null);
                 int totTfga = aqReportDAO.getColGrandTotal(aqlist, "col10", "TPFGA", null);
                 int totTlci = aqReportDAO.getColGrandTotal(aqlist, "col9", "TLIC", null);
                 int totWrr = aqReportDAO.getColGrandTotal(aqlist, "col12", "WRR", null);
                 int totSwr = aqReportDAO.getColGrandTotal(aqlist, "col12", "SWR", null);
                 int totCc = aqReportDAO.getColGrandTotal(aqlist, "col18", "CC", null);
                 int totCmpa = aqReportDAO.getColGrandTotal(aqlist, "col18", "CMPA", null);
                 int totOvdl = aqReportDAO.getColGrandTotal(aqlist, "col16", "OVDL", null);
                 aqreportFormBean.setTotHrr(totHrr);
                 aqreportFormBean.setTotHbaPri(totHbaPri);
                 aqreportFormBean.setTotShbaPri(totShbaPri);
                 aqreportFormBean.setTotMcaPri(totMcaPri);
                 aqreportFormBean.setTotShbaInt(totShbaInt);
                 aqreportFormBean.setTotBicycPri(totBicycPri);
                 aqreportFormBean.setTotPt(totPt);
                 aqreportFormBean.setTotGisaPri(totGisaPri);
                 aqreportFormBean.setTotIt(totIt);
                 aqreportFormBean.setTotGpf(totGpf);
                 aqreportFormBean.setTotMopInt(totMopInt);
                 aqreportFormBean.setTotHc(totHc);
                 aqreportFormBean.setTotHbaInt(totHbaInt);
                 aqreportFormBean.setTotGis(totGis);
                 aqreportFormBean.setTotVehicleInt(totVehicleInt);
                 aqreportFormBean.setTotCpf(totCpf);
                 aqreportFormBean.setTotPa(totPa);
                 aqreportFormBean.setTotCgegis(totCgegis);
                 aqreportFormBean.setTotVehiclePri(totVehiclePri);
                 aqreportFormBean.setTotTpf(totTpf);
                 aqreportFormBean.setTotTfga(totTfga);
                 aqreportFormBean.setTotTlci(totTlci);
                 aqreportFormBean.setTotWrr(totWrr);
                 aqreportFormBean.setTotSwr(totSwr);
                 aqreportFormBean.setTotCc(totCc);
                 aqreportFormBean.setTotCmpa(totCmpa);
                 aqreportFormBean.setTotOvdl(totOvdl);
                 HashMap payAbstract = aqReportDAO.getPayAbstract(aqlist);
                 String pay = payAbstract.get("pay").toString();
                 String dp = payAbstract.get("dp").toString();
                 String da = payAbstract.get("da").toString();
                 String hra = payAbstract.get("hra").toString();
                 String oa = payAbstract.get("oa").toString();
                 int col7Tot = 0;
                 if (oa != null && !oa.equals("")) {
                 col7Tot = Integer.parseInt(oa);
                 aqreportFormBean.setCol7Tot(col7Tot);
                 }
                 aqreportFormBean.setPay(pay);
                 aqreportFormBean.setDp(dp);
                 aqreportFormBean.setDa(da);
                 aqreportFormBean.setHra(hra);
                 aqreportFormBean.setOa(oa);
                 int totAbstract = Integer.parseInt(pay) + Integer.parseInt(dp) + Integer.parseInt(da) + Integer.parseInt(hra) + Integer.parseInt(oa);
                 aqreportFormBean.setTotAbstract(totAbstract);

                 aqReportDAO.generateAqReportPDF(document, billno + "", crb, aqreportFormBean);

                 document.close();
                 fout.close();
                 fout.flush();

                 // PT SCHEDULE PDF FILE GENERATED BLOCK
                 document = new com.itextpdf.text.Document(PageSize.A4);
                 pdfout = new FileOutputStream(new File(absolutePath + BillBrowserController.FILE_SEPARATOR + "PT_" + filePattern + ".pdf"));
                 PdfWriter.getInstance(document, pdfout);
                 document.open();

                 PtScheduleBean ptBean = comonScheduleDao.getPTScheduleHeaderDetails(billno + "");
                 List empList = comonScheduleDao.getPTScheduleEmployeeList(billno + "", crb.getAqmonth(), crb.getAqyear());

                 comonScheduleDao.PTSchedulePagePDF(crb, document, billno + "", ptBean, empList, crb.getAqmonth(), crb.getAqyear());

                 document.close();
                 fout.close();
                 fout.flush();

                 // OTC 82 SCHEDULE PDF FILE GENERATED BLOCK
                 document = new com.itextpdf.text.Document(PageSize.A4);
                 pdfout = new FileOutputStream(new File(absolutePath + BillBrowserController.FILE_SEPARATOR + "OTC82_" + filePattern + ".pdf"));
                 PdfWriter.getInstance(document, pdfout);
                 document.open();

                 OtcForm82Bean otcBean = comonScheduleDao.getOTCForm82ScheduleDetails(billno + "");
                 comonScheduleDao.OTC82SchedulePDF(document, billno + "", otcBean);

                 document.close();
                 fout.close();
                 fout.flush();

                 // OTC 52 SCHEDULE PDF FILE GENERATED BLOCK
                 document = new com.itextpdf.text.Document(PageSize.A4);
                 pdfout = new FileOutputStream(new File(absolutePath + BillBrowserController.FILE_SEPARATOR + "OTC52_" + filePattern + ".pdf"));
                 PdfWriter.getInstance(document, pdfout);
                 document.open();

                 OtcFormBean otc52Bean = comonScheduleDao.getOTCForm52ScheduleDetails(billno + "", crb.getAqmonth(), crb.getAqyear());
                 comonScheduleDao.OtcForm52SchedulePDF(document, billno + "", otc52Bean);

                 document.close();
                 fout.close();
                 fout.flush();

                 // OTC 40 SCHEDULE PDF FILE GENERATED BLOCK
                 document = new com.itextpdf.text.Document(PageSize.A4);
                 pdfout = new FileOutputStream(new File(absolutePath + BillBrowserController.FILE_SEPARATOR + "OTC40_" + filePattern + ".pdf"));
                 PdfWriter.getInstance(document, pdfout);
                 document.open();
                 List alowanceList = null;
                 List deductList = null;
                 List newAlowanceList = new ArrayList();
                 empList = comonScheduleDao.getOTCForm40ScheduleEmpList(billno + "", crb.getAqyear(), crb.getAqmonth());

                 ScheduleHelper obj = null;
                 if (empList != null && empList.size() > 0) {
                 obj = new ScheduleHelper();
                 for (int i = 0; i < empList.size(); i++) {
                 obj = (ScheduleHelper) empList.get(i);

                 alowanceList = obj.getAllowanceList();
                 deductList = obj.getDeductionList();
                 }
                 }
                 int totgp = 0;
                 OtcPlanForm40Bean otc40Obj = null;
                 for (int i = 0; i < alowanceList.size(); i++) {
                 otc40Obj = (OtcPlanForm40Bean) alowanceList.get(i);
                 String adCode = otc40Obj.getAdCode();

                 if (adCode.equals("GP")) {
                 totgp = totgp + otc40Obj.getAdAmt();
                 } else if (!adCode.equals("GP")) {
                 OtcPlanForm40Bean otcObj = new OtcPlanForm40Bean();

                 otcObj.setAdAmt(otc40Obj.getAdAmt());
                 otcObj.setBtId(otc40Obj.getBtId());
                 otcObj.setNowDedn(otc40Obj.getNowDedn());
                 otcObj.setAdCode(otc40Obj.getAdCode());
                 newAlowanceList.add(otcObj);
                 }
                 }

                 OtcPlanForm40Bean otc40Bean = comonScheduleDao.getOTCForm40ScheduleDetails(billno + "", crb.getAqyear(), crb.getAqmonth(), totgp, crb);
                 comonScheduleDao.OtcForm40SchedulePDF(document, billno + "", otc40Bean, payHead, newAlowanceList, deductList);

                 document.close();
                 fout.close();
                 fout.flush();

                 // DA Certificate
                 document = new com.itextpdf.text.Document(PageSize.A4);
                 pdfout = new FileOutputStream(new File(absolutePath + BillBrowserController.FILE_SEPARATOR + "DA_CERTIFICATE_" + filePattern + ".pdf"));
                 PdfWriter.getInstance(document, pdfout);
                 document.open();

                 comonScheduleDao.downloadDACertificatePDF(document);

                 document.close();
                 fout.close();
                 fout.flush();

                 // CONVEYANCE SCHEDULE
                 document = new com.itextpdf.text.Document(PageSize.A4);
                 pdfout = new FileOutputStream(new File(absolutePath + BillBrowserController.FILE_SEPARATOR + "CONVEYANCE_" + filePattern + ".pdf"));
                 PdfWriter.getInstance(document, pdfout);
                 document.open();

                 List dataList = comonScheduleDao.getCSDataList(billno + "", crb.getAqyear(), crb.getAqmonth());

                 comonScheduleDao.getConveyanceSchedulePDF(document, billno + "", crb, dataList);

                 document.close();
                 fout.close();
                 fout.flush();

                 // BILL BACK PAGE SCHEDULE PDF FILE GENERATED BLOCK
                 document = new com.itextpdf.text.Document(PageSize.A4);
                 pdfout = new FileOutputStream(new File(absolutePath + BillBrowserController.FILE_SEPARATOR + "BACKPAGE_" + filePattern + ".pdf"));
                 PdfWriter.getInstance(document, pdfout);
                 document.open();

                 BillBackPageBean backPageBean = comonScheduleDao.getBillBackPgScheduleHeaderDetails(billno + "", crb.getAqyear(), crb.getAqmonth());
                 List empListBillBack = comonScheduleDao.getBillBackPgScheduleEmpList(billno + "", crb.getAqyear(), crb.getAqmonth());

                 comonScheduleDao.billBackPagePDF(document, billno + "", backPageBean, empListBillBack);

                 document.close();
                 fout.close();
                 fout.flush();

                 // INCOME TAX SCHEDULE PDF FILE GENERATED BLOCK
                 document = new com.itextpdf.text.Document(PageSize.A4);
                 pdfout = new FileOutputStream(new File(absolutePath + BillBrowserController.FILE_SEPARATOR + "IT_" + filePattern + ".pdf"));
                 PdfWriter.getInstance(document, pdfout);
                 document.open();

                 ItScheduleBean itBean = comonScheduleDao.getITScheduleHeaderDetails(billno + "", "IT");
                 List empITList = comonScheduleDao.getITScheduleEmployeeList(billno + "", "IT", crb.getAqmonth(), crb.getAqyear());
                 comonScheduleDao.ITSchedulePagePDF(crb, document, "IT", billno + "", itBean, empITList);

                 document.close();
                 fout.close();
                 fout.flush();

                 // HIRE CHARGE TAX SCHEDULE PDF FILE GENERATED BLOCK
                 document = new com.itextpdf.text.Document(PageSize.A4);
                 pdfout = new FileOutputStream(new File(absolutePath + BillBrowserController.FILE_SEPARATOR + "HC_" + filePattern + ".pdf"));
                 PdfWriter.getInstance(document, pdfout);
                 document.open();

                 ItScheduleBean itBean2 = comonScheduleDao.getITScheduleHeaderDetails(billno + "", "HC");
                 empITList = comonScheduleDao.getITScheduleEmployeeList(billno + "", "HC", crb.getAqmonth(), crb.getAqyear());
                 comonScheduleDao.ITSchedulePagePDF(crb, document, "HC", billno + "", itBean2, empITList);

                 document.close();
                 fout.close();
                 fout.flush();

                 // CEGIS SCHEDULE PDF FILE GENERATED BLOCK
                 document = new com.itextpdf.text.Document(PageSize.A4);
                 pdfout = new FileOutputStream(new File(absolutePath + BillBrowserController.FILE_SEPARATOR + "CGEGIS_" + filePattern + ".pdf"));
                 PdfWriter.getInstance(document, pdfout);
                 document.open();

                 itBean2 = comonScheduleDao.getITScheduleHeaderDetails(billno + "", "CGEGIS");
                 empITList = comonScheduleDao.getITScheduleEmployeeList(billno + "", "CGEGIS", crb.getAqmonth(), crb.getAqyear());
                 comonScheduleDao.ITSchedulePagePDF(crb, document, "CGEGIS", billno + "", itBean2, empITList);

                 document.close();
                 fout.close();
                 fout.flush();

                
                 //HOUSE RENT RECOVERY SCHEDULE CODE BLOCK STARTS HERE
                 
                 document = new com.itextpdf.text.Document(PageSize.A4);
                 pdfout = new FileOutputStream(new File(absolutePath + BillBrowserController.FILE_SEPARATOR + "HRR_" + filePattern + ".pdf"));
                 PdfWriter.getInstance(document, pdfout);
                 document.open();

                 comonScheduleDao.WRRSchedulePagePDF(document, "HRR", billno + "", crb);

                 document.close();
                 fout.close();
                 fout.flush();

                
                 //WATER RENT RECOVERY SCHEDULE CODE BLOCK STARTS HERE
                
                 document = new com.itextpdf.text.Document(PageSize.A4);
                 pdfout = new FileOutputStream(new File(absolutePath + BillBrowserController.FILE_SEPARATOR + "WRR_" + filePattern + ".pdf"));
                 PdfWriter.getInstance(document, pdfout);
                 document.open();

                 comonScheduleDao.WRRSchedulePagePDF(document, "WRR", billno + "", crb);

                 document.close();
                 fout.close();
                 fout.flush();

                
                 //SWEARAGE RENT RECOVERY SCHEDULE CODE BLOCK STARTS HERE
                 
                 document = new com.itextpdf.text.Document(PageSize.A4);
                 pdfout = new FileOutputStream(new File(absolutePath + BillBrowserController.FILE_SEPARATOR + "SWR_" + filePattern + ".pdf"));
                 PdfWriter.getInstance(document, pdfout);
                 document.open();

                 comonScheduleDao.WRRSchedulePagePDF(document, "SWR", billno + "", crb);

                 document.close();
                 fout.close();
                 fout.flush();

                 ArrayList<LoanType> loanList = loanTypeDao.getLoanTypeList();
                 for (LoanType loantp : loanList) {

                 String schedule = loantp.getLoanType();
                 String loanname = loantp.getLoanName();
                 String haveint = loantp.getHaveInt();

                 if (schedule.equalsIgnoreCase("HBA")) {
                 // HOUSE BUILDING
                 document = new com.itextpdf.text.Document(PageSize.A4);
                 pdfout = new FileOutputStream(new File(absolutePath + BillBrowserController.FILE_SEPARATOR + "HBA_" + filePattern + ".pdf"));
                 PdfWriter.getInstance(document, pdfout);
                 document.open();

                 LoanAdvanceScheduleBean laBean = comonScheduleDao.getLoanAdvanceScheduleHeaderDetails(schedule, billno + "");
                 List principalList = comonScheduleDao.getLoanAdvanceSchedulePrincipalList(schedule, billno + "", crb.getAqmonth(), crb.getAqyear());
                 List interestList = comonScheduleDao.getLoanAdvanceScheduleInterestList(schedule, billno + "", crb.getAqmonth(), crb.getAqyear());

                 comonScheduleDao.LoanSchedulePagePDF(crb, document, schedule, billno + "", laBean, principalList, interestList);

                 document.close();
                 fout.close();
                 fout.flush();

                 } else if (schedule.equalsIgnoreCase("VE")) {
                 // MOTOR CAR 

                 document = new com.itextpdf.text.Document(PageSize.A4);
                 pdfout = new FileOutputStream(new File(absolutePath + BillBrowserController.FILE_SEPARATOR + "VEHICLE_" + filePattern + ".pdf"));
                 PdfWriter.getInstance(document, pdfout);
                 document.open();

                 LoanAdvanceScheduleBean laBean = comonScheduleDao.getLoanAdvanceScheduleHeaderDetails(schedule, billno + "");
                 List principalList = comonScheduleDao.getLoanAdvanceSchedulePrincipalList(schedule, billno + "", crb.getAqmonth(), crb.getAqyear());
                 List interestList = comonScheduleDao.getLoanAdvanceScheduleInterestList(schedule, billno + "", crb.getAqmonth(), crb.getAqyear());

                 comonScheduleDao.LoanSchedulePagePDF(crb, document, schedule, billno + "", laBean, principalList, interestList);

                 document.close();
                 fout.close();
                 fout.flush();

                 } else if (schedule.equalsIgnoreCase("SHBA")) {
                 // HOUSE BUILDING ADVANCE FOR CYCLONE 

                 document = new com.itextpdf.text.Document(PageSize.A4);
                 pdfout = new FileOutputStream(new File(absolutePath + BillBrowserController.FILE_SEPARATOR + "SPECIAL_HBA_" + filePattern + ".pdf"));
                 PdfWriter.getInstance(document, pdfout);
                 document.open();

                 LoanAdvanceScheduleBean laBean = comonScheduleDao.getLoanAdvanceScheduleHeaderDetails(schedule, billno + "");
                 List principalList = comonScheduleDao.getLoanAdvanceSchedulePrincipalList(schedule, billno + "", crb.getAqmonth(), crb.getAqyear());
                 List interestList = comonScheduleDao.getLoanAdvanceScheduleInterestList(schedule, billno + "", crb.getAqmonth(), crb.getAqyear());

                 comonScheduleDao.LoanSchedulePagePDF(crb, document, schedule, billno + "", laBean, principalList, interestList);

                 document.close();
                 fout.close();
                 fout.flush();

                 } else if (schedule.equalsIgnoreCase("MCA")) {
                 // MOTOR CYCLE ADVANCE 
                 document = new com.itextpdf.text.Document(PageSize.A4);
                 pdfout = new FileOutputStream(new File(absolutePath + BillBrowserController.FILE_SEPARATOR + "MCA_" + filePattern + ".pdf"));
                 PdfWriter.getInstance(document, pdfout);
                 document.open();

                 LoanAdvanceScheduleBean laBean = comonScheduleDao.getLoanAdvanceScheduleHeaderDetails(schedule, billno + "");
                 List principalList = comonScheduleDao.getLoanAdvanceSchedulePrincipalList(schedule, billno + "", crb.getAqmonth(), crb.getAqyear());
                 List interestList = comonScheduleDao.getLoanAdvanceScheduleInterestList(schedule, billno + "", crb.getAqmonth(), crb.getAqyear());

                 comonScheduleDao.LoanSchedulePagePDF(crb, document, schedule, billno + "", laBean, principalList, interestList);

                 document.close();
                 fout.close();
                 fout.flush();

                 } else if (schedule.equalsIgnoreCase("MOPA")) {
                 // MOPED 
                 document = new com.itextpdf.text.Document(PageSize.A4);
                 pdfout = new FileOutputStream(new File(absolutePath + BillBrowserController.FILE_SEPARATOR + "MOPED_" + filePattern + ".pdf"));
                 PdfWriter.getInstance(document, pdfout);
                 document.open();

                 LoanAdvanceScheduleBean laBean = comonScheduleDao.getLoanAdvanceScheduleHeaderDetails(schedule, billno + "");
                 List principalList = comonScheduleDao.getLoanAdvanceSchedulePrincipalList(schedule, billno + "", crb.getAqmonth(), crb.getAqyear());
                 List interestList = comonScheduleDao.getLoanAdvanceScheduleInterestList(schedule, billno + "", crb.getAqmonth(), crb.getAqyear());

                 comonScheduleDao.LoanSchedulePagePDF(crb, document, schedule, billno + "", laBean, principalList, interestList);

                 document.close();
                 fout.close();
                 fout.flush();

                 } else if (schedule.equalsIgnoreCase("CMPA")) {
                 // COMPUTER ADVANCE SCHEDULE 
                 document = new com.itextpdf.text.Document(PageSize.A4);
                 pdfout = new FileOutputStream(new File(absolutePath + BillBrowserController.FILE_SEPARATOR + "COMPUTER_" + filePattern + ".pdf"));
                 PdfWriter.getInstance(document, pdfout);
                 document.open();

                 LoanAdvanceScheduleBean laBean = comonScheduleDao.getLoanAdvanceScheduleHeaderDetails(schedule, billno + "");
                 List principalList = comonScheduleDao.getLoanAdvanceSchedulePrincipalList(schedule, billno + "", crb.getAqmonth(), crb.getAqyear());
                 List interestList = comonScheduleDao.getLoanAdvanceScheduleInterestList(schedule, billno + "", crb.getAqmonth(), crb.getAqyear());

                 comonScheduleDao.LoanSchedulePagePDF(crb, document, schedule, billno + "", laBean, principalList, interestList);

                 document.close();
                 fout.close();
                 fout.flush();
                 }

                 }

                 File directoryPDF = new File(absolutePath);
                 String[] filesPDF = directoryPDF.list();
                 byte[] zipPDF = zipFiles(directoryPDF, filesPDF);
                 fout = new FileOutputStream(new File(absolutePath + ".zip"));
                 fout.write(zipPDF);
                 fout.close();
                 fout.flush();
                 */
                File folder = new File(absolutePath);
                String[] entries = folder.list();
                for (String s : entries) {
                    File currentFile = new File(folder.getPath(), s);
                    currentFile.delete();
                }

                folder.delete();

            } else {

            }

            mav.addObject("sltYear", bbbean.getSltYear());
            mav.addObject("sltMonth", bbbean.getSltMonth());
            mav.addObject("txtbilltype", bbbean.getTxtbilltype());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "saveBill", params = "action=Download", method = RequestMethod.POST)
    public void downloadXML(HttpServletRequest request, @ModelAttribute("BillBrowserbean") BillBrowserbean bbbean,
            @ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletResponse response) {
        String billDetailFile = "BILL_DETAILS";
        String folderPath = servletContext.getInitParameter("payBillXMLDOC");
        Calendar cal = Calendar.getInstance();
        BillDetail bill = new BillDetail();
        String tableName = aqReportDao.getAqDtlsTableName(bbbean.getBillNo());
        String path = "redirect:/getPayBillList.htm";
        //ModelAndView mav = null;
        try {

            BillDetail billDtls = billBrowserDao.getBillDetails(Integer.parseInt(bbbean.getBillNo()));
            bbbean.setSltYear(billDtls.getBillyear());
            //bbbean.setSltMonth(billDtls.getBillmonth());
            //bbbean.setTxtbilltype(billDtls.getBillType());

            //mav = new ModelAndView(path, "command", bbbean);
            CommonReportParamBean crb = paybillDmpDao.getCommonReportParameter(bbbean.getBillNo());

            int billno = Integer.parseInt(bbbean.getBillNo());

            boolean upverify = true;
            if (upverify) {
                bill = billBrowserDao.getBillDetails(billno);

                billDetailFile = billDetailFile + "_" + bill.getDdoccode() + "_" + bill.getBillmonth() + bill.getBillyear() + ".xml";
                createXMLBillDetails(folderPath + BillBrowserController.FILE_SEPARATOR + bill.getHrmsgeneratedRefno(), billDetailFile, bill);

                ArrayList objList = billBrowserDao.getBeneficiaryList(billno, crb.getTypeofBill());
                billDetailFile = "BENEFICIARY_DETAILS" + "_" + bill.getDdoccode() + "_" + bill.getBillmonth() + bill.getBillyear() + ".xml";
                createXMLBeneficiary(folderPath + BillBrowserController.FILE_SEPARATOR + bill.getHrmsgeneratedRefno(), billDetailFile, objList);

                /*
                 * Object Breakup data is collected here
                 * */
                double basic = billBrowserDao.getBasicAmountBillWise(billno);
                String payHead = billFrontPageDmpDao.getPayHead(billno);
                ArrayList objlist = billBrowserDao.getOBJXMLData(billno, bill.getTreasuryCode(), basic, bill.getBillDate(), bill.getTypeofBillString(), payHead, crb.getAqmonth(), crb.getAqyear());
                billDetailFile = "OBJ_BREAKUP" + "_" + bill.getDdoccode() + "_" + bill.getBillmonth() + bill.getBillyear() + ".xml";
                createXMLObjectBreakUp(folderPath + BillBrowserController.FILE_SEPARATOR + bill.getHrmsgeneratedRefno(), billDetailFile, objlist);

                /*
                 * Bytransfer Data is collected here
                 * */
                ArrayList bytranlist = billBrowserDao.getBTXMLData(billno, bill.getTreasuryCode(), bill.getBillDate(), bill.getTypeofBillString(), crb.getAqmonth(), crb.getAqyear());
                billDetailFile = "BT_DETAILS" + "_" + bill.getDdoccode() + "_" + bill.getBillmonth() + bill.getBillyear() + ".xml";
                createXMLBT(folderPath + BillBrowserController.FILE_SEPARATOR + bill.getHrmsgeneratedRefno(), billDetailFile, bytranlist);

                /*
                 * GPF data is collected here
                 * */
                ArrayList gpflist = new ArrayList();
                gpflist = billBrowserDao.getGPFXMLData(billno, bill.getBillDate(), bill.getBillmonth(), bill.getBillyear(), bill.getSalFromdate(), bill.getSalTodate(), bill.getTypeofBillString(), crb.getAqmonth(), crb.getAqyear());

                billDetailFile = "GPF_DETAILS" + "_" + bill.getDdoccode() + "_" + bill.getBillmonth() + bill.getBillyear() + ".xml";
                createXMLGPF(folderPath + BillBrowserController.FILE_SEPARATOR + billno, billDetailFile, gpflist);

                /*
                 * TPF data is collected here
                 * */
                ArrayList tpflist = new ArrayList();
                tpflist = billBrowserDao.getTPFXMLData(billno, bill.getBillDate(), bill.getBillmonth(), bill.getBillyear(), bill.getSalFromdate(), bill.getSalTodate(), bill.getTypeofBillString(), crb.getAqmonth(), crb.getAqyear());
                billDetailFile = "TPF_DETAILS" + "_" + bill.getDdoccode() + "_" + bill.getBillmonth() + bill.getBillyear() + ".xml";
                createXMLTPF(folderPath + BillBrowserController.FILE_SEPARATOR + billno, billDetailFile, tpflist);

                /*
                 *   Data for NPS
                 * 
                 */
                ArrayList npslist = billBrowserDao.getNPSXMLData(billno, bill.getBillDate(), bill.getBillmonth(), bill.getBillyear(), bill.getTypeofBillString(), crb.getAqmonth(), crb.getAqyear());
                billDetailFile = "NPS_DETAILS" + "_" + bill.getDdoccode() + "_" + bill.getBillmonth() + bill.getBillyear() + ".xml";
                createXMLCPF(folderPath + BillBrowserController.FILE_SEPARATOR + billno, billDetailFile, npslist);

                /*
                 * Zip file is created and uploaded to server
                 * */
                int count = 0;
                count = billBrowserDao.getbillsubmissionCount(billno);
                String zipFilename = "";

                if (count > 0) {
                    zipFilename = bill.getDdoccode() + "-" + CalendarCommonMethods.getFullMonthAsString(bill.getBillmonth() - 1) + "-" + bill.getBillyear() + "-" + billno + "_" + count + ".zip";
                } else {
                    zipFilename = bill.getDdoccode() + "-" + CalendarCommonMethods.getFullMonthAsString(bill.getBillmonth() - 1) + "-" + bill.getBillyear() + "-" + billno + ".zip";
                }

                String[] signedsinglepagesalaryfilepath = billBrowserDao.getSignedSinglePageSalaryPDFPath(billno, 2);
                /*System.out.println("signedsinglepagesalaryfilepath name is: "+signedsinglepagesalaryfilepath[0]);
                 System.out.println("signedsinglepagesalaryfile is: "+signedsinglepagesalaryfilepath[1]);
                 System.out.println("signedsinglepagesalaryfile path is: "+signedsinglepagesalaryfilepath[0] + BillBrowserController.FILE_SEPARATOR + signedsinglepagesalaryfilepath[1]);*/
                if (signedsinglepagesalaryfilepath[0] != null) {
                    FileUtils.copyFile(new File(signedsinglepagesalaryfilepath[0] + BillBrowserController.FILE_SEPARATOR + signedsinglepagesalaryfilepath[1]), new File(folderPath + BillBrowserController.FILE_SEPARATOR + billno + BillBrowserController.FILE_SEPARATOR + signedsinglepagesalaryfilepath[1]));
                }

                File directory = new File(folderPath + BillBrowserController.FILE_SEPARATOR + billno);
                String[] files = directory.list();
                byte[] zip = zipFiles(directory, files);

                FileUtils.deleteDirectory(directory);

                OutputStream out = response.getOutputStream();
                response.setContentType("application/zip");
                response.setHeader("Content-Disposition", "attachment; filename=\"" + zipFilename + "\"");
                out.write(zip);
                out.flush();

                //ftpUpload(zip, zipFilename);
                //billBrowserDao.updateBillStatus(billno, 3);
                //billBrowserDao.updateBillHistory(billno, submissionDate);
            }

            //mav.addObject("sltYear", bbbean.getSltYear());
            //mav.addObject("sltMonth", bbbean.getSltMonth());
            //mav.addObject("txtbilltype", bbbean.getTxtbilltype());
        } catch (Exception e) {
            e.printStackTrace();
        }
        //return mav;
    }

    @RequestMapping(value = "saveBillURL")
    public ModelAndView downloadXMLURL(HttpServletRequest request, @ModelAttribute("BillBrowserbean") BillBrowserbean bbbean,
            @ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletResponse response) {
        String billDetailFile = "BILL_DETAILS";
        String folderPath = servletContext.getInitParameter("payBillXMLDOC");
        Calendar cal = Calendar.getInstance();
        BillDetail bill = new BillDetail();

        String path = "redirect:/getPayBillList.htm";
        ModelAndView mav = null;
        try {
            String billNo = CommonFunctions.decodedTxt(bbbean.getBillNo());
            bbbean.setBillNo(billNo);
            BillDetail billDtls = billBrowserDao.getBillDetails(Integer.parseInt(bbbean.getBillNo()));
            bbbean.setSltYear(billDtls.getBillyear());
            //bbbean.setSltMonth(billDtls.getBillmonth());
            //bbbean.setTxtbilltype(billDtls.getBillType());

            mav = new ModelAndView(path, "command", bbbean);
            CommonReportParamBean crb = paybillDmpDao.getCommonReportParameter(bbbean.getBillNo());

            int billno = Integer.parseInt(bbbean.getBillNo());

            boolean upverify = true;
            if (upverify) {
                bill = billBrowserDao.getBillDetails(billno);

                billDetailFile = billDetailFile + "_" + bill.getDdoccode() + "_" + bill.getBillmonth() + bill.getBillyear() + ".xml";
                createXMLBillDetails(folderPath + BillBrowserController.FILE_SEPARATOR + bill.getHrmsgeneratedRefno(), billDetailFile, bill);

                ArrayList objList = billBrowserDao.getBeneficiaryList(billno, crb.getTypeofBill());
                billDetailFile = "BENEFICIARY_DETAILS" + "_" + bill.getDdoccode() + "_" + bill.getBillmonth() + bill.getBillyear() + ".xml";
                createXMLBeneficiary(folderPath + BillBrowserController.FILE_SEPARATOR + bill.getHrmsgeneratedRefno(), billDetailFile, objList);


                /*
                 * Object Breakup data is collected here
                 * */
                double basic = billBrowserDao.getBasicAmountBillWise(billno);
                String payHead = billFrontPageDmpDao.getPayHead(billno);
                ArrayList objlist = billBrowserDao.getOBJXMLData(billno, bill.getTreasuryCode(), basic, bill.getBillDate(), bill.getTypeofBillString(), payHead, crb.getAqmonth(), crb.getAqyear());
                billDetailFile = "OBJ_BREAKUP" + "_" + bill.getDdoccode() + "_" + bill.getBillmonth() + bill.getBillyear() + ".xml";
                createXMLObjectBreakUp(folderPath + BillBrowserController.FILE_SEPARATOR + bill.getHrmsgeneratedRefno(), billDetailFile, objlist);

                /*
                 * Bytransfer Data is collected here
                 * */
                ArrayList bytranlist = billBrowserDao.getBTXMLData(billno, bill.getTreasuryCode(), bill.getBillDate(), bill.getTypeofBillString(), crb.getAqmonth(), crb.getAqyear());
                billDetailFile = "BT_DETAILS" + "_" + bill.getDdoccode() + "_" + bill.getBillmonth() + bill.getBillyear() + ".xml";
                createXMLBT(folderPath + BillBrowserController.FILE_SEPARATOR + bill.getHrmsgeneratedRefno(), billDetailFile, bytranlist);

                /*
                 * GPF data is collected here
                 * */
                ArrayList gpflist = new ArrayList();
                gpflist = billBrowserDao.getGPFXMLData(billno, bill.getBillDate(), bill.getBillmonth(), bill.getBillyear(), bill.getSalFromdate(), bill.getSalTodate(), bill.getTypeofBillString(), crb.getAqmonth(), crb.getAqyear());

                billDetailFile = "GPF_DETAILS" + "_" + bill.getDdoccode() + "_" + bill.getBillmonth() + bill.getBillyear() + ".xml";
                createXMLGPF(folderPath + BillBrowserController.FILE_SEPARATOR + billno, billDetailFile, gpflist);

                /*
                 * TPF data is collected here
                 * */
                ArrayList tpflist = new ArrayList();
                tpflist = billBrowserDao.getTPFXMLData(billno, bill.getBillDate(), bill.getBillmonth(), bill.getBillyear(), bill.getSalFromdate(), bill.getSalTodate(), bill.getTypeofBillString(), crb.getAqmonth(), crb.getAqyear());
                billDetailFile = "TPF_DETAILS" + "_" + bill.getDdoccode() + "_" + bill.getBillmonth() + bill.getBillyear() + ".xml";
                createXMLTPF(folderPath + BillBrowserController.FILE_SEPARATOR + billno, billDetailFile, tpflist);

                /*
                 * Data for NPS
                 * */
                ArrayList npslist = billBrowserDao.getNPSXMLData(billno, bill.getBillDate(), bill.getBillmonth(), bill.getBillyear(), bill.getTypeofBillString(), crb.getAqmonth(), crb.getAqyear());
                billDetailFile = "NPS_DETAILS" + "_" + bill.getDdoccode() + "_" + bill.getBillmonth() + bill.getBillyear() + ".xml";
                createXMLCPF(folderPath + BillBrowserController.FILE_SEPARATOR + billno, billDetailFile, npslist);

                /*
                 * Zip file is created and uploaded to server
                 * */
                int count = 0;
                count = billBrowserDao.getbillsubmissionCount(billno);
                String zipFilename = "";

                if (count > 0) {
                    zipFilename = bill.getDdoccode() + "-" + CalendarCommonMethods.getFullMonthAsString(bill.getBillmonth() - 1) + "-" + bill.getBillyear() + "-" + billno + "_" + count + ".zip";
                } else {
                    zipFilename = bill.getDdoccode() + "-" + CalendarCommonMethods.getFullMonthAsString(bill.getBillmonth() - 1) + "-" + bill.getBillyear() + "-" + billno + ".zip";

                }

                File directory = new File(folderPath + BillBrowserController.FILE_SEPARATOR + billno);
                String[] files = directory.list();
                byte[] zip = zipFiles(directory, files);
                OutputStream out = response.getOutputStream();
                response.setContentType("application/zip");
                response.setHeader("Content-Disposition", "attachment; filename=\"" + zipFilename + "\"");
                out.write(zip);
                out.flush();
                //ftpUpload(zip, zipFilename);
                //billBrowserDao.updateBillStatus(billno, 3);
                //billBrowserDao.updateBillHistory(billno, submissionDate);

            }

            mav.addObject("sltYear", bbbean.getSltYear());
            mav.addObject("sltMonth", bbbean.getSltMonth());
            mav.addObject("txtbilltype", bbbean.getTxtbilltype());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    private byte[] zipFiles(File directory, String[] files) throws IOException {
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

    public void ftpUpload(byte[] file, String fileName) {
        String SFTPHOST = "bpel.hrmsorissa.gov.in";
        int SFTPPORT = 22;
        String SFTPUSER = "root";
        String SFTPPASS = "cmgi#2012#";
        String SFTPWORKINGDIR = "/home/paybillxml";
        /*String SFTPHOST = "117.240.239.72";
         int    SFTPPORT = 21;
         String SFTPUSER = "hrms";
         String SFTPPASS = "hrmsiotms@321";
         String SFTPWORKINGDIR = "/shared/hrms/upload/"; */
        Session session = null;
        Channel channel = null;
        ChannelSftp channelSftp = null;
        try {
            JSch jsch = new JSch();
            session = jsch.getSession(SFTPUSER, SFTPHOST, SFTPPORT);
            session.setPassword(SFTPPASS);
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.connect();
            channel = session.openChannel("sftp");
            channel.connect();
            channelSftp = (ChannelSftp) channel;
            channelSftp.cd(SFTPWORKINGDIR);
            channelSftp.put(new ByteArrayInputStream(file), fileName);
            channelSftp.disconnect();
            session.disconnect();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void createXMLBillDetails(String folderPath, String filename, BillDetail bill) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            // root elements
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("BILL_DETAILS");
            doc.appendChild(rootElement);
            // ROW elements
            Element row = doc.createElement("ROW");
            rootElement.appendChild(row);
            // firstname elements
            Element element = doc.createElement("HRMS_NO");
            element.appendChild(doc.createTextNode(bill.getHrmsgeneratedRefno()));
            row.appendChild(element);
            element = doc.createElement("HRMS_DATE");
            element.appendChild(doc.createTextNode(bill.getHrmsgeneratedRefdate()));
            row.appendChild(element);
            element = doc.createElement("BILL_TYPE");
            element.appendChild(doc.createTextNode(bill.getBillType()));
            row.appendChild(element);
            element = doc.createElement("BILL_NUMBER");
            element.appendChild(doc.createTextNode(bill.getBillnumber()));
            row.appendChild(element);
            element = doc.createElement("BILL_DATE");
            element.appendChild(doc.createTextNode(bill.getBillDate()));
            row.appendChild(element);
            element = doc.createElement("AG_BILL_TYPE_ID");
            element.appendChild(doc.createTextNode(bill.getAgbillTypeId()));
            row.appendChild(element);
            element = doc.createElement("SAL_FROM_DATE");
            element.appendChild(doc.createTextNode(bill.getSalFromdate()));
            row.appendChild(element);
            element = doc.createElement("SAL_TO_DATE");
            element.appendChild(doc.createTextNode(bill.getSalTodate()));
            row.appendChild(element);
            element = doc.createElement("DDO_CODE");
            element.appendChild(doc.createTextNode(bill.getDdoccode()));
            row.appendChild(element);
            element = doc.createElement("CO_CODE");
            element.appendChild(doc.createTextNode(bill.getCoCode()));
            row.appendChild(element);
            element = doc.createElement("DEMAND_NUMBER");
            element.appendChild(doc.createTextNode(bill.getDemandNumber()));
            row.appendChild(element);
            element = doc.createElement("MAJOR_HEAD");
            element.appendChild(doc.createTextNode(bill.getMajorHead()));
            row.appendChild(element);
            element = doc.createElement("SUB_MAJOR_HEAD");
            element.appendChild(doc.createTextNode(bill.getSubMajorHead()));
            row.appendChild(element);
            element = doc.createElement("MINOR_HEAD");
            element.appendChild(doc.createTextNode(bill.getMinorHead()));
            row.appendChild(element);
            element = doc.createElement("SUB_HEAD");
            element.appendChild(doc.createTextNode(bill.getSubHead()));
            row.appendChild(element);
            element = doc.createElement("DETAILS_HEAD");
            element.appendChild(doc.createTextNode(bill.getDetailHead()));
            row.appendChild(element);
            element = doc.createElement("PLAN_STATUS");
            element.appendChild(doc.createTextNode(bill.getPlanStatus()));
            row.appendChild(element);
            element = doc.createElement("CHARGED_VOTED");
            element.appendChild(doc.createTextNode(bill.getChargedVoted()));
            row.appendChild(element);
            element = doc.createElement("SECTOR_CODE");
            element.appendChild(doc.createTextNode(bill.getSectorCode()));
            row.appendChild(element);
            element = doc.createElement("GROSS_AMOUNT");
            element.appendChild(doc.createTextNode(bill.getGrossAmount() + ""));
            row.appendChild(element);
            element = doc.createElement("NET_AMOUNT");
            element.appendChild(doc.createTextNode(bill.getNetAmount() + ""));
            row.appendChild(element);
            element = doc.createElement("PREVIOUS_TOKEN_NUMBER");
            element.appendChild(doc.createTextNode(bill.getPrevTokenNumber()));
            row.appendChild(element);
            element = doc.createElement("PREVIOUS_TOKEN_DATE");
            element.appendChild(doc.createTextNode(bill.getPrevTokendate()));
            row.appendChild(element);
            element = doc.createElement("TREASURY_CODE");
            element.appendChild(doc.createTextNode(bill.getTreasuryCode()));
            row.appendChild(element);
            element = doc.createElement("MOBILE_NO");
            element.appendChild(doc.createTextNode(bill.getDdomobileno()));
            row.appendChild(element);

            // write the content into xml file
            boolean success = new File(folderPath).mkdirs();

            File f = new File(folderPath, filename);
            if (f.exists()) {
                f.delete();
                f.createNewFile();
            }
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(f);
            transformer.transform(source, result);
            if (result.getOutputStream() != null) {
                result.getOutputStream().close();
            }

        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        } catch (IOException iox) {
            iox.printStackTrace();
        }
    }

    public void createXMLObjectBreakUp(String folderPath, String filename, ArrayList objlist) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            // root elements
            // root elements
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("OBJ_BREAKUP");
            doc.appendChild(rootElement);
            for (int i = 0; i < objlist.size(); i++) {
                ObjectBreakup object = (ObjectBreakup) objlist.get(i);
                Element row = doc.createElement("ROW");
                Element element = doc.createElement("HRMS_NO");
                element.appendChild(doc.createTextNode(object.getHrmsgeneratedRefno() + ""));
                row.appendChild(element);
                element = doc.createElement("HRMS_DATE");
                element.appendChild(doc.createTextNode(object.getHrmsgeneratedRefdate()));
                row.appendChild(element);
                element = doc.createElement("OBJECT_HEAD");
                element.appendChild(doc.createTextNode(object.getObjectHead()));
                row.appendChild(element);
                element = doc.createElement("AMOUNT");
                element.appendChild(doc.createTextNode(object.getObjectHeadwiseAmount() + ""));
                row.appendChild(element);
                element = doc.createElement("TREASURY_CODE");
                element.appendChild(doc.createTextNode(object.getTreasuryCode()));
                row.appendChild(element);

                rootElement.appendChild(row);
            }
            // write the content into xml file
            boolean success = new File(folderPath).mkdirs();

            File f = new File(folderPath, filename);
            if (f.exists()) {
                f.delete();
                f.createNewFile();
            }
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(f);
            transformer.transform(source, result);
            if (result.getOutputStream() != null) {
                result.getOutputStream().close();
            }
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        } catch (IOException iox) {
            iox.printStackTrace();
        }
    }

    public void createXMLTPF(String folderPath, String filename, ArrayList objlist) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            // root elements
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("TPF_DETAILS");
            doc.appendChild(rootElement);
            for (int i = 0; i < objlist.size(); i++) {
                GpfTpfDetails object = (GpfTpfDetails) objlist.get(i);
                Element row = doc.createElement("ROW");
                Element element = doc.createElement("HRMS_NO");
                element.appendChild(doc.createTextNode(object.getHrmsgeneratedRefno()));
                row.appendChild(element);
                element = doc.createElement("HRMS_DATE");
                element.appendChild(doc.createTextNode(object.getHrmsgeneratedRefdate()));
                row.appendChild(element);
                element = doc.createElement("BT_SERIAL_NUMBER");
                element.appendChild(doc.createTextNode(object.getBtserialno()));
                row.appendChild(element);
                element = doc.createElement("GPF_SERIES");
                element.appendChild(doc.createTextNode(object.getGpfSeries()));
                row.appendChild(element);
                element = doc.createElement("GPF_NUMBER");
                element.appendChild(doc.createTextNode(object.getGpfnumber()));
                row.appendChild(element);
                element = doc.createElement("SUBSCRIBER_NAME");
                element.appendChild(doc.createTextNode(object.getSubscribName()));
                row.appendChild(element);
                element = doc.createElement("DESIGNATION");
                element.appendChild(doc.createTextNode(object.getDesig()));
                row.appendChild(element);
                element = doc.createElement("DOB");
                element.appendChild(doc.createTextNode(object.getDob()));
                row.appendChild(element);
                element = doc.createElement("DOS");
                element.appendChild(doc.createTextNode(object.getDos()));
                row.appendChild(element);
                element = doc.createElement("SUBSCRIPTION");
                element.appendChild(doc.createTextNode(object.getMonthlySubscrip()));
                row.appendChild(element);
                element = doc.createElement("PERIOD_FROM");
                element.appendChild(doc.createTextNode(object.getPeriodFrom()));
                row.appendChild(element);
                element = doc.createElement("PERIOD_TO");
                element.appendChild(doc.createTextNode(object.getPeriodTo()));
                row.appendChild(element);
                element = doc.createElement("REFUND_OF_WITHDRAWL");
                element.appendChild(doc.createTextNode(object.getRefundWithdrawl()));
                row.appendChild(element);
                element = doc.createElement("INSTAL_NUMBER");
                element.appendChild(doc.createTextNode(object.getInstNumber()));
                row.appendChild(element);
                element = doc.createElement("OTH_DEPOSITS");
                element.appendChild(doc.createTextNode(object.getOtherDeposit() + ""));
                row.appendChild(element);
                element = doc.createElement("TOTAL_REALISED");
                element.appendChild(doc.createTextNode(object.getTotRealised() + ""));
                row.appendChild(element);
                element = doc.createElement("REMARKS");
                element.appendChild(doc.createTextNode(object.getRemarks()));
                row.appendChild(element);

                rootElement.appendChild(row);
            }
            // write the content into xml file
            boolean success = new File(folderPath).mkdirs();

            File f = new File(folderPath, filename);
            if (f.exists()) {
                f.delete();
                f.createNewFile();
            }
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(f);
            transformer.transform(source, result);
            if (result.getOutputStream() != null) {
                result.getOutputStream().close();
            }
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        } catch (IOException iox) {
            iox.printStackTrace();
        }
    }

    public void createXMLCPF(String folderPath, String filename, ArrayList objlist) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            // root elements
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("NPS_DETAILS");
            doc.appendChild(rootElement);
            for (int i = 0; i < objlist.size(); i++) {
                NPSDetails object = (NPSDetails) objlist.get(i);
                Element row = doc.createElement("ROW");
                Element element = doc.createElement("HRMS_NO");
                element.appendChild(doc.createTextNode(object.getHrmsgeneratedRefno() + ""));
                row.appendChild(element);
                element = doc.createElement("HRMS_DATE");
                element.appendChild(doc.createTextNode(object.getHrmsgeneratedRefdate()));
                row.appendChild(element);
                element = doc.createElement("BT_SERIAL_NUMBER");
                element.appendChild(doc.createTextNode(object.getBtserialno()));
                row.appendChild(element);
                element = doc.createElement("DDO_REG_NUMBER");
                element.appendChild(doc.createTextNode(object.getDdoRegno()));
                row.appendChild(element);
                element = doc.createElement("PRAN");
                element.appendChild(doc.createTextNode(object.getPran()));
                row.appendChild(element);
                element = doc.createElement("SUBSCRIBER_NAME");
                element.appendChild(doc.createTextNode(object.getNameofSubscrib()));
                row.appendChild(element);
                element = doc.createElement("BASIC_GP");
                element.appendChild(doc.createTextNode((object.getGp() + object.getBasic() + object.getPpay()) + ""));
                row.appendChild(element);
                element = doc.createElement("DA");
                element.appendChild(doc.createTextNode(object.getDa() + ""));
                row.appendChild(element);
                element = doc.createElement("SC");
                element.appendChild(doc.createTextNode(object.getSc() + ""));
                row.appendChild(element);
                element = doc.createElement("GC");
                element.appendChild(doc.createTextNode(object.getGc()));
                row.appendChild(element);
                element = doc.createElement("INSTALLMENT");
                element.appendChild(doc.createTextNode(object.getInstAmt() + ""));
                row.appendChild(element);
                element = doc.createElement("PAY_MONTH");
                element.appendChild(doc.createTextNode(object.getPaymonth()));
                row.appendChild(element);
                element = doc.createElement("PAY_YEAR");
                element.appendChild(doc.createTextNode(object.getPayYear()));
                row.appendChild(element);
                element = doc.createElement("CONTRIBUTION_TYPE");
                element.appendChild(doc.createTextNode(object.getContType()));
                row.appendChild(element);
                element = doc.createElement("REMARKS");
                element.appendChild(doc.createTextNode(object.getRemarks()));
                row.appendChild(element);

                rootElement.appendChild(row);
            }
            // write the content into xml file
            boolean success = new File(folderPath).mkdirs();

            File f = new File(folderPath, filename);
            if (f.exists()) {
                f.delete();
                f.createNewFile();
            }
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(f);
            transformer.transform(source, result);
            if (result.getOutputStream() != null) {
                result.getOutputStream().close();
            }
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        } catch (IOException iox) {
            iox.printStackTrace();
        }
    }

    public void createXMLGPF(String folderPath, String filename, ArrayList objlist) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            // root elements
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("GPF_DETAILS");
            doc.appendChild(rootElement);
            for (int i = 0; i < objlist.size(); i++) {
                GpfTpfDetails object = (GpfTpfDetails) objlist.get(i);
                Element row = doc.createElement("ROW");
                Element element = doc.createElement("HRMS_NO");
                element.appendChild(doc.createTextNode(object.getHrmsgeneratedRefno()));
                row.appendChild(element);
                element = doc.createElement("HRMS_DATE");
                element.appendChild(doc.createTextNode(object.getHrmsgeneratedRefdate()));
                row.appendChild(element);
                element = doc.createElement("BT_SERIAL_NUMBER");
                element.appendChild(doc.createTextNode(object.getBtserialno()));
                row.appendChild(element);
                element = doc.createElement("GPF_SERIES");
                element.appendChild(doc.createTextNode(object.getGpfSeries()));
                row.appendChild(element);
                element = doc.createElement("GPF_NUMBER");
                element.appendChild(doc.createTextNode(object.getGpfnumber()));
                row.appendChild(element);
                element = doc.createElement("SUBSCRIBER_NAME");
                element.appendChild(doc.createTextNode(object.getSubscribName()));
                row.appendChild(element);
                element = doc.createElement("DESIGNATION");
                element.appendChild(doc.createTextNode(object.getDesig()));
                row.appendChild(element);
                element = doc.createElement("DOB");
                element.appendChild(doc.createTextNode(object.getDob()));
                row.appendChild(element);
                element = doc.createElement("DOS");
                element.appendChild(doc.createTextNode(object.getDos()));
                row.appendChild(element);
                element = doc.createElement("SUBSCRIPTION");
                element.appendChild(doc.createTextNode(object.getMonthlySubscrip()));
                row.appendChild(element);
                element = doc.createElement("PERIOD_FROM");
                element.appendChild(doc.createTextNode(object.getPeriodFrom()));
                row.appendChild(element);
                element = doc.createElement("PERIOD_TO");
                element.appendChild(doc.createTextNode(object.getPeriodTo()));
                row.appendChild(element);
                element = doc.createElement("REFUND_OF_WITHDRAWL");
                element.appendChild(doc.createTextNode(object.getRefundWithdrawl()));
                row.appendChild(element);
                element = doc.createElement("INSTAL_NUMBER");
                element.appendChild(doc.createTextNode(object.getInstNumber()));
                row.appendChild(element);
                element = doc.createElement("OTH_DEPOSITS");
                element.appendChild(doc.createTextNode(object.getOtherDeposit() + ""));
                row.appendChild(element);
                element = doc.createElement("TOTAL_REALISED");
                element.appendChild(doc.createTextNode(object.getTotRealised() + ""));
                row.appendChild(element);
                element = doc.createElement("REMARKS");
                element.appendChild(doc.createTextNode(object.getRemarks()));
                row.appendChild(element);

                rootElement.appendChild(row);
            }
            // write the content into xml file
            boolean success = new File(folderPath).mkdirs();

            File f = new File(folderPath, filename);

            if (f.exists()) {
                f.delete();
                f.createNewFile();
            }
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(f);
            transformer.transform(source, result);
            if (result.getOutputStream() != null) {
                result.getOutputStream().close();
            }
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        } catch (IOException iox) {
            iox.printStackTrace();
        }
    }

    /*
     * Function to create Salary Benefitiary Detail XML Document
     * */
    public void createXMLBeneficiary(String folderPath, String filename, ArrayList objlist) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            // root elements
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("BENEFICIARY_DETAILS");
            doc.appendChild(rootElement);
            for (int i = 0; i < objlist.size(); i++) {
                SalaryBenefitiaryDetails object = (SalaryBenefitiaryDetails) objlist.get(i);
                Element row = doc.createElement("ROW");

                Element element = doc.createElement("HRMS_NO");
                element.appendChild(doc.createTextNode(object.getHrms_no()));
                row.appendChild(element);

                element = doc.createElement("HRMS_DATE");
                element.appendChild(doc.createTextNode(object.getHrms_date()));
                row.appendChild(element);

                element = doc.createElement("BENF_NAME");
                element.appendChild(doc.createTextNode(object.getBenf_name()));
                row.appendChild(element);

                element = doc.createElement("BENF_ADDRESS");
                String address = StringEscapeUtils.escapeXml(StringUtils.replace(object.getBenf_address(), "\n", " "));
                address = address.replace("&", "and");
                element.appendChild(doc.createTextNode(StringEscapeUtils.escapeXml(address.replaceAll("\\s+", " ").trim())));
                row.appendChild(element);

                element = doc.createElement("BEN_ACCT_NO");
                element.appendChild(doc.createTextNode(object.getBen_acct_no()));
                row.appendChild(element);

                element = doc.createElement("BANK_IFSC_CODE");
                element.appendChild(doc.createTextNode(object.getBank_ifsc_code()));
                row.appendChild(element);

                element = doc.createElement("ACCOUNT_TYPE");
                element.appendChild(doc.createTextNode(object.getAccount_type()));
                row.appendChild(element);

                element = doc.createElement("AMOUNT");
                element.appendChild(doc.createTextNode(object.getAmount() + ""));
                row.appendChild(element);

                element = doc.createElement("EMAIL_ID");
                element.appendChild(doc.createTextNode(object.getEmail_id()));
                row.appendChild(element);

                element = doc.createElement("MOBILE_NUMBER");
                element.appendChild(doc.createTextNode(StringUtils.defaultString(object.getMobile_number())));
                row.appendChild(element);

                element = doc.createElement("GPF_SERIES");
                element.appendChild(doc.createTextNode(object.getGpf_series()));
                row.appendChild(element);

                element = doc.createElement("GPF_NUMBER");
                element.appendChild(doc.createTextNode(object.getGpf_number()));
                row.appendChild(element);

                element = doc.createElement("PRAN");
                element.appendChild(doc.createTextNode(object.getPran()));
                row.appendChild(element);

                element = doc.createElement("EMPLOYEE_ID");
                element.appendChild(doc.createTextNode(object.getEmployeeId()));

                row.appendChild(element);

                rootElement.appendChild(row);
            }
            // write the content into xml file
            boolean success = new File(folderPath).mkdirs();

            File f = new File(folderPath, filename);

            if (f.exists()) {
                f.delete();
                f.createNewFile();
            }
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(f);
            transformer.transform(source, result);
            if (result.getOutputStream() != null) {
                result.getOutputStream().close();
            }
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        } catch (IOException iox) {
            iox.printStackTrace();
        }
    }

    public void createXMLBT(String folderPath, String filename, ArrayList objlist) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            // root elements
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("BT_DETAILS");
            doc.appendChild(rootElement);
            for (int i = 0; i < objlist.size(); i++) {
                BytransferDetails object = (BytransferDetails) objlist.get(i);
                if (object.getAmount() > 0) {

                    Element row = doc.createElement("ROW");
                    Element element = doc.createElement("HRMS_NO");
                    element.appendChild(doc.createTextNode(object.getHrmsgeneratedRefno() + ""));
                    row.appendChild(element);
                    element = doc.createElement("HRMS_DATE");
                    element.appendChild(doc.createTextNode(object.getHrmsgeneratedRefdate()));
                    row.appendChild(element);
                    element = doc.createElement("BT_SERIAL_NUMBER");
                    element.appendChild(doc.createTextNode(object.getBtserialno()));
                    row.appendChild(element);
                    element = doc.createElement("AMOUNT");
                    element.appendChild(doc.createTextNode(object.getAmount() + ""));
                    row.appendChild(element);
                    element = doc.createElement("TREASURY_CODE");
                    element.appendChild(doc.createTextNode(object.getTreasuryCode()));
                    row.appendChild(element);

                    rootElement.appendChild(row);
                }
            }
            // write the content into xml file
            boolean success = new File(folderPath).mkdirs();

            File f = new File(folderPath, filename);
            if (f.exists()) {
                f.delete();
                f.createNewFile();
            }
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(f);
            transformer.transform(source, result);
            if (result.getOutputStream() != null) {
                result.getOutputStream().close();
            }
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        } catch (IOException iox) {
            iox.printStackTrace();
        }
    }

    @ResponseBody
    @RequestMapping(value = "getMajorHeadListTreasuryWise", method = {RequestMethod.GET, RequestMethod.POST})
    public void getMajorHeadListTreasuryWise(HttpServletRequest request, @RequestParam("aqyear") int aqyear, @RequestParam("aqmonth") int aqmonth, @RequestParam("trcode") String trcode, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = null;
        ArrayList mjhcdlist = billBrowserDao.getMajorHeadListTreasuryWise(trcode, aqyear, aqmonth);
        JSONArray json = new JSONArray(mjhcdlist);
        out = response.getWriter();
        out.write(json.toString());
    }

    @ResponseBody
    @RequestMapping(value = "getVoucherListMajorHeadWise", method = {RequestMethod.GET, RequestMethod.POST})
    public void getVoucherListMajorHeadWise(HttpServletRequest request, @RequestParam("majorhead") String majorhead, @RequestParam("aqyear") int aqyear, @RequestParam("aqmonth") int aqmonth, @RequestParam("trcode") String trcode, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = null;
        ArrayList vchlist = billBrowserDao.getVoucherListTreasuryWise(trcode, aqyear, aqmonth, majorhead);
        JSONArray json = new JSONArray(vchlist);
        out = response.getWriter();
        out.write(json.toString());
    }

    @RequestMapping(value = "lockBill", method = RequestMethod.GET)
    public ModelAndView lockBill(HttpServletRequest request, @ModelAttribute("BillBrowserbean") BillBrowserbean bbbean, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpOffice") String selectedEmpOffice, HttpServletResponse response) throws IOException {

        String path = "/payroll/BillBrowser";

        ModelAndView mav = null;

        String agfilePath = servletContext.getInitParameter("agdownloadPDFPath");
        com.itextpdf.text.Document document = new com.itextpdf.text.Document(PageSize.A4);

        PdfWriter writer = null;
        try {

            billBrowserDao.updateBillStatus(Integer.parseInt(bbbean.getBillNo()), 2);

            BillDetail billDetail = billBrowserDao.getBillDetails(Integer.parseInt(bbbean.getBillNo()));

            ArrayList billYears = billBrowserDao.getBillPrepareYear(selectedEmpOffice);
            ArrayList billMonths = billBrowserDao.getMonthFromSelectedYear(selectedEmpOffice, bbbean.getSltYear(), bbbean.getTxtbilltype());
            ArrayList<BillBean> billList = billBrowserDao.getPayBillList(bbbean.getSltYear(), bbbean.getSltMonth(), selectedEmpOffice, bbbean.getTxtbilltype(), lub.getLoginspc());
            mav = new ModelAndView(path, "command", bbbean);

            bbbean.setTxtbilltype(bbbean.getTxtbilltype());

            mav.addObject("billYears", billYears);
            mav.addObject("billMonths", billMonths);

            mav.addObject("billYears", billYears);
            mav.addObject("billMonths", billMonths);
            mav.addObject("billList", billList);
            mav.setViewName(path);

            String folderName = agfilePath + "/BeneficiaryFiles/" + bbbean.getSltYear();

            File folder = new File(folderName);
            if (!folder.exists()) {
                folder.mkdirs();
            }

            CommonReportParamBean crb = paybillDmpDao.getCommonReportParameter(bbbean.getBillNo());

            File innerfile = new File(folderName + "/" + crb.getDdocode() + "_" + bbbean.getBillNo() + ".pdf");

            /*if (innerfile.exists()) {
             innerfile.delete();
             }else{
             innerfile = new File(folderName + "/" + crb.getDdocode() + "_" + bbbean.getBillNo() + ".pdf");
             }*/
            writer = PdfWriter.getInstance(document, new FileOutputStream(innerfile));

            document.open();

            ArrayList emplist = billBrowserDao.getBeneficiaryList(Integer.parseInt(bbbean.getBillNo()), crb.getTypeofBill());
            double netAmt = billBrowserDao.getBillGrossAndNet(Integer.parseInt(bbbean.getBillNo()));
            comonScheduleDao.generateBeneficiaryListPDF(document, crb, bbbean.getBillNo(), emplist, netAmt);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }

        return mav;
    }

    @RequestMapping(value = "verifyDataForLockBill")
    public String verifyDataForLockBill(Model model, @RequestParam("billNo") int billno, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpOffice") String selectedEmpOffice, @ModelAttribute("BillBrowserbean") BillBrowserbean bbbean) throws Exception {

        try {
            ArrayList lockerrordatalist = billBrowserDao.verifyforLockBill(billno);

            SelectOption so = billBrowserDao.getBeneficiaryNet(billno);

            List mismatch = billBrowserDao.getMismatchBeneficiaryDetailsFromPreviousMonth(billno);

            List mismatchGPFData = billBrowserDao.getMismatchGPFData(billno, bbbean.getTxtbilltype());

            model.addAttribute("lockerrordatalist", lockerrordatalist);
            model.addAttribute("beneficiarydata", so);
            model.addAttribute("mismatch", mismatch);
            model.addAttribute("mismatchGPFData", mismatchGPFData);

            model.addAttribute("billno", billno);
            model.addAttribute("bbbean", bbbean);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/payroll/LockBillStatus";
    }

    @ResponseBody
    @RequestMapping(value = "changePayHeadOfBill", method = RequestMethod.GET)
    public void changePayHeadOfBill(ModelMap model, @ModelAttribute("BillBrowserbean") BillBrowserbean bbbean, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpOffice") String selectedEmpOffice, BindingResult result, HttpServletResponse response) throws Exception {
        response.setContentType("application/json");
        PrintWriter out = null;
        bbbean.setOffCode(selectedEmpOffice);
        String status = billBrowserDao.changePayHeadOfBill(bbbean);
        JSONObject jobj = new JSONObject();
        jobj.append("status", status);
        out = response.getWriter();
        out.write(jobj.toString());
    }

    @ResponseBody
    @RequestMapping(value = "changeObjectBtHeadOfBill", method = RequestMethod.GET)
    public void changeObjectBtHeadOfBill(ModelMap model, @ModelAttribute("BillBrowserbean") BillBrowserbean bbbean, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response) throws Exception {
        response.setContentType("application/json");
        PrintWriter out = null;
        String status = billBrowserDao.changeObjectBtHead(bbbean);
        JSONObject jobj = new JSONObject();
        jobj.append("status", status);
        out = response.getWriter();
        out.write(jobj.toString());
    }

    @ResponseBody
    @RequestMapping(value = "changeObjectBtHeadOfArrearBill", method = RequestMethod.GET)
    public void changeObjectBtHeadOfArrearBill(ModelMap model, @ModelAttribute("BillBrowserbean") BillBrowserbean bbbean, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response) throws Exception {
        response.setContentType("application/json");
        PrintWriter out = null;
        String status = billBrowserDao.changeObjectBtHeadOfArrear(bbbean);
        JSONObject jobj = new JSONObject();
        jobj.append("status", status);
        out = response.getWriter();
        out.write(jobj.toString());
    }

    @ResponseBody
    @RequestMapping(value = "changeBtHeadOfArrearBill", method = RequestMethod.GET)
    public void changeBtHeadOfArrearBill(ModelMap model, @ModelAttribute("BillBrowserbean") BillBrowserbean bbbean, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response) throws Exception {
        response.setContentType("application/json");
        PrintWriter out = null;
        String status = billBrowserDao.changeBtHeadOfArrear(bbbean);
        JSONObject jobj = new JSONObject();
        jobj.append("status", status);
        out = response.getWriter();
        out.write(jobj.toString());
    }

    @RequestMapping(value = "editBill", method = RequestMethod.GET)
    public ModelAndView editBill(ModelMap model, @ModelAttribute("BillBrowserbean") BillBrowserbean bbbean, @ModelAttribute("LoginUserBean") LoginUserBean lub,
            @ModelAttribute("SelectedEmpOffice") String selectedEmpOffice, BindingResult result, HttpServletResponse response) {
        String path = "/payroll/BillBrowserData";
        CommonReportParamBean crb = null;
        boolean isCollegeUnderDHE = false;
        boolean isDHE = false;

        isCollegeUnderDHE = billBrowserDao.isCollegeUnderDHE(selectedEmpOffice);
        isDHE = billBrowserDao.isDDODHE(selectedEmpOffice);

        BillDetail billDtls = new BillDetail();
        String iscolgDhe = null;
        if (isDHE) {
            crb = paybillDmpDao.getCommonReportParameterDHE(bbbean.getBillNo());
        } else {
            crb = paybillDmpDao.getCommonReportParameter(bbbean.getBillNo());
        }

        bbbean.setSltMonth(crb.getAqmonth());
        bbbean.setSltYear(crb.getAqyear());
        bbbean.setTxtbilltype(crb.getTypeofBill());
        System.out.println("bbbean.setTxtbilltype::" + bbbean.getTxtbilltype());
        bbbean.setChartofAcct(billBrowserDao.getBillChartofAccount(Integer.parseInt(bbbean.getBillNo())));

        if (isCollegeUnderDHE) {
            billDtls = billBrowserDao.getBillDetailsDHE(Integer.parseInt(bbbean.getBillNo()), selectedEmpOffice);
            iscolgDhe = "B";
        } else if (isDHE) {
            billDtls = billBrowserDao.getBillDetailsDHE(Integer.parseInt(bbbean.getBillNo()), selectedEmpOffice);
            iscolgDhe = "D";

        } else {
            billDtls = billBrowserDao.getBillDetails(Integer.parseInt(bbbean.getBillNo()));
        }

        //BillDetail billDtls = billBrowserDao.getBillDetails(Integer.parseInt(bbbean.getBillNo()));
        bbbean.setBilldesc(billDtls.getBillnumber());
        bbbean.setBenificiaryNumber(billDtls.getBeneficiaryrefno());
        bbbean.setVchDt(billDtls.getVchDt());
        bbbean.setVchNo(billDtls.getVchNo());
        bbbean.setBillDate(billDtls.getBillDate());
        bbbean.setTreasury(billDtls.getTreasuryCode());
        bbbean.setSltCOList(billDtls.getCoCode());
        bbbean.setSltddoCode(billDtls.getDdoccode());

        ModelAndView mav = new ModelAndView(path, "command", bbbean);

        bbbean.setStatus(billDtls.getBillStatusId());

        List treasuryList = treasuryDao.getTreasuryListByOffCode(crb.getOffcode());
        List ddoList = billBrowserDao.getDdoCodeList(crb.getOffcode());
        mav.addObject("ddoCodeList", ddoList);
        mav.addObject("paydata", billBrowserDao.getPay(Integer.parseInt(bbbean.getBillNo()), selectedEmpOffice));
        mav.addObject("allowanceList", billBrowserDao.getAllowanceList(Integer.parseInt(bbbean.getBillNo()), crb.getAqmonth(), crb.getAqyear()));
        mav.addObject("deductionList", billBrowserDao.getDeductionList(Integer.parseInt(bbbean.getBillNo()), crb.getAqmonth(), crb.getAqyear()));
        mav.addObject("pvtloanList", billBrowserDao.getPvtLoanList(Integer.parseInt(bbbean.getBillNo()), crb.getAqmonth(), crb.getAqyear()));
        mav.addObject("treasuryList", treasuryList);
        mav.addObject("billDtls", billDtls);
        mav.addObject("colist", billGroupDAO.getCOList(selectedEmpOffice));
        mav.addObject("usrtname", lub.getLoginusername());
        mav.addObject("iscollegeDhe", iscolgDhe);
        return mav;
    }

    @RequestMapping(value = "editBillArrear", method = RequestMethod.GET)
    public ModelAndView editBillArrear(ModelMap model, @ModelAttribute("BillBrowserbean") BillBrowserbean bbbean, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpOffice") String selectedEmpOffice, BindingResult result, HttpServletResponse response) {
        String path = "/payroll/BillBrowserArrearData";
        CommonReportParamBean crb = paybillDmpDao.getCommonReportParameter(bbbean.getBillNo());
        bbbean.setSltMonth(crb.getAqmonth());
        bbbean.setSltYear(crb.getAqyear());
        bbbean.setTxtbilltype(crb.getTypeofBill());

        BillDetail billDtls = billBrowserDao.getBillDetails(Integer.parseInt(bbbean.getBillNo()));
        bbbean.setChartofAcct(billBrowserDao.getBillChartofAccount(Integer.parseInt(bbbean.getBillNo())));
        bbbean.setBilldesc(billDtls.getBillnumber());
        bbbean.setBenificiaryNumber(billDtls.getBeneficiaryrefno());
        bbbean.setVchDt(billDtls.getVchDt());
        bbbean.setVchNo(billDtls.getVchNo());
        bbbean.setBillDate(billDtls.getBillDate());
        bbbean.setTreasury(billDtls.getTreasuryCode());
        bbbean.setSltCOList(billDtls.getCoCode());
        bbbean.setSltddoCode(billDtls.getDdoccode());
        ModelAndView mav = new ModelAndView(path, "command", bbbean);

        bbbean.setStatus(billDtls.getBillStatusId());
        int pvtDedAmt = billBrowserDao.getDDORecoveryList(Integer.parseInt(bbbean.getBillNo()));

        List treasuryList = treasuryDao.getTreasuryListByOffCode(crb.getOffcode());
        List ddoList = billBrowserDao.getDdoCodeList(crb.getOffcode());

        if (crb.getTypeofBill().equals("PAY")) {
            mav.addObject("allowanceList", billBrowserDao.getAllowanceList(Integer.parseInt(bbbean.getBillNo()), crb.getAqmonth(), crb.getAqyear()));
            mav.addObject("deductionList", billBrowserDao.getDeductionList(Integer.parseInt(bbbean.getBillNo()), crb.getAqmonth(), crb.getAqyear()));
            mav.addObject("pvtloanList", billBrowserDao.getPvtLoanList(Integer.parseInt(bbbean.getBillNo()), crb.getAqmonth(), crb.getAqyear()));
        } else {
            mav.addObject("allowanceList", billBrowserDao.getAllowanceListForArrear(Integer.parseInt(bbbean.getBillNo()), crb.getAqmonth(), crb.getAqyear()));
            mav.addObject("deductionList", billBrowserDao.getDeductionListForArrear(Integer.parseInt(bbbean.getBillNo()), crb.getAqmonth(), crb.getAqyear()));
            mav.addObject("pvtDeductionAmt", pvtDedAmt);
        }
        mav.addObject("ddoCodeList", ddoList);
        mav.addObject("colist", billGroupDAO.getCOList(selectedEmpOffice));
        mav.addObject("treasuryList", treasuryList);
        mav.addObject("billDtls", billDtls);

        String billperiod = "";
        if (crb.getFromYear() > 0) {
            billperiod = CommonFunctions.getMonthAsString(crb.getFromMonth() - 1) + "-" + crb.getFromYear() + " to " + CommonFunctions.getMonthAsString(crb.getToMonth() - 1) + "-" + crb.getToYear();
        }

        mav.addObject("billPeriod", billperiod);

        return mav;
    }

    @RequestMapping(value = "saveBill", params = "action=Cancel", method = RequestMethod.POST)
    public ModelAndView returnBill(ModelMap model, @ModelAttribute("BillBrowserbean") BillBrowserbean bbbean, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response) {
        String path = "redirect:/getPayBillList.htm";
        BillDetail billDtls = billBrowserDao.getBillDetails(Integer.parseInt(bbbean.getBillNo()));
        bbbean.setSltYear(billDtls.getBillyear());

        ModelAndView mav = new ModelAndView(path, "command", bbbean);
        mav.addObject("sltYear", bbbean.getSltYear());
        mav.addObject("sltMonth", bbbean.getSltMonth());
        mav.addObject("txtbilltype", bbbean.getTxtbilltype());

        mav.setViewName(path);
        return mav;
    }

    @RequestMapping(value = "saveBill", params = "action=ChangeChartofAccount", method = RequestMethod.POST)
    public ModelAndView changeBillChartofAccount(ModelMap model, @ModelAttribute("BillBrowserbean") BillBrowserbean bbbean, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response) {
        String path = "payroll/ChangeChartofAccount";
        BillDetail billDtls = billBrowserDao.getBillDetails(Integer.parseInt(bbbean.getBillNo()));
        bbbean.setSltYear(billDtls.getBillyear());

        bbbean.setChartofAcct(billBrowserDao.getBillChartofAccount(Integer.parseInt(bbbean.getBillNo())));
        bbbean.setTxtDemandno(billDtls.getDemandNumber());
        bbbean.setTxtmajcode(billDtls.getMajorHead());
        bbbean.setTxtmincode(billDtls.getMinorHead());
        bbbean.setSubmajcode(billDtls.getSubMajorHead());
        bbbean.setSubmincode1(billDtls.getSubHead());
        bbbean.setSubmincode2(billDtls.getDetailHead());
        bbbean.setSubmincode3(billDtls.getChargedVoted());
        bbbean.setPlanCode(billDtls.getPlanStatus());
        bbbean.setSectorCode(billDtls.getSectorCode());

        ModelAndView mav = new ModelAndView(path, "command", bbbean);
        mav.addObject("txtDemandno", bbbean.getTxtDemandno());
        mav.addObject("sltYear", bbbean.getSltYear());
        mav.addObject("sltMonth", bbbean.getSltMonth());
        mav.addObject("txtbilltype", bbbean.getTxtbilltype());

        mav.setViewName(path);
        return mav;
    }

    @RequestMapping(value = "saveBillArrear", params = "action=ChangeChartofAccount", method = RequestMethod.POST)
    public ModelAndView changeArrearBillChartofAccount(ModelMap model, @ModelAttribute("BillBrowserbean") BillBrowserbean bbbean, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response) {
        String path = "payroll/ChangeChartofAccount";
        BillDetail billDtls = billBrowserDao.getBillDetails(Integer.parseInt(bbbean.getBillNo()));
        bbbean.setSltYear(billDtls.getBillyear());

        bbbean.setChartofAcct(billBrowserDao.getBillChartofAccount(Integer.parseInt(bbbean.getBillNo())));

        bbbean.setTxtDemandno(billDtls.getDemandNumber());
        bbbean.setTxtmajcode(billDtls.getMajorHead());

        bbbean.setTxtmincode(billDtls.getMinorHead());
        bbbean.setSubmajcode(billDtls.getSubMajorHead());
        bbbean.setSubmincode1(billDtls.getSubHead());
        bbbean.setSubmincode2(billDtls.getDetailHead());
        bbbean.setSubmincode3(billDtls.getChargedVoted());
        bbbean.setPlanCode(billDtls.getPlanStatus());
        bbbean.setSectorCode(billDtls.getSectorCode());
        ModelAndView mav = new ModelAndView(path, "command", bbbean);
        mav.addObject("sltYear", bbbean.getSltYear());
        mav.addObject("sltMonth", bbbean.getSltMonth());
        mav.addObject("txtbilltype", bbbean.getTxtbilltype());

        mav.setViewName(path);
        return mav;
    }

    @RequestMapping(value = "saveBill", params = "action=Update", method = RequestMethod.POST)
    public ModelAndView updateBillChartofAccount(ModelMap model, @ModelAttribute("BillBrowserbean") BillBrowserbean bbbean, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response) {
        String path = "payroll/ChangeChartofAccount";
        BillDetail billDtls = billBrowserDao.getBillDetails(Integer.parseInt(bbbean.getBillNo()));
        bbbean.setSltYear(billDtls.getBillyear());

        billBrowserDao.updateBillChartofAccount(Integer.parseInt(bbbean.getBillNo()), bbbean);
        bbbean.setChartofAcct(billBrowserDao.getBillChartofAccount(Integer.parseInt(bbbean.getBillNo())));

        ModelAndView mav = new ModelAndView(path, "command", bbbean);
        mav.addObject("sltYear", bbbean.getSltYear());
        mav.addObject("sltMonth", bbbean.getSltMonth());
        mav.addObject("txtbilltype", bbbean.getTxtbilltype());

        mav.setViewName(path);
        return mav;
    }

    @RequestMapping(value = "saveBillArrear", params = "action=Update", method = RequestMethod.POST)
    public ModelAndView updateArrearBillChartofAccount(ModelMap model, @ModelAttribute("BillBrowserbean") BillBrowserbean bbbean, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response) {
        String path = "payroll/ChangeChartofAccount";
        BillDetail billDtls = billBrowserDao.getBillDetails(Integer.parseInt(bbbean.getBillNo()));
        bbbean.setSltYear(billDtls.getBillyear());

        billBrowserDao.updateBillChartofAccount(Integer.parseInt(bbbean.getBillNo()), bbbean);
        bbbean.setChartofAcct(billBrowserDao.getBillChartofAccount(Integer.parseInt(bbbean.getBillNo())));

        ModelAndView mav = new ModelAndView(path, "command", bbbean);
        mav.addObject("sltYear", bbbean.getSltYear());
        mav.addObject("sltMonth", bbbean.getSltMonth());
        mav.addObject("txtbilltype", bbbean.getTxtbilltype());

        mav.setViewName(path);
        return mav;
    }

    @RequestMapping(value = "saveBillArrear", params = "action=Cancel", method = RequestMethod.POST)
    public ModelAndView returnBack(ModelMap model, @ModelAttribute("BillBrowserbean") BillBrowserbean bbbean, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpOffice") String selectedEmpOffice, BindingResult result, HttpServletResponse response) {
        String path = "redirect:/getPayBillList.htm";
        BillDetail billDtls = billBrowserDao.getBillDetails(Integer.parseInt(bbbean.getBillNo()));
        bbbean.setSltYear(billDtls.getBillyear());
        //bbbean.setSltMonth(billDtls.getBillmonth());
        //bbbean.setTxtbilltype(billDtls.getBillType());

        ArrayList billYears = billBrowserDao.getBillPrepareYear(selectedEmpOffice);
        ArrayList billMonths = billBrowserDao.getMonthFromSelectedYear(selectedEmpOffice, bbbean.getSltYear(), bbbean.getTxtbilltype());
        ArrayList<BillBean> billList = billBrowserDao.getPayBillList(bbbean.getSltYear(), bbbean.getSltMonth(), selectedEmpOffice, bbbean.getTxtbilltype(), lub.getLoginspc());
        ModelAndView mav = new ModelAndView(path, "command", bbbean);
        mav.addObject("sltYear", bbbean.getSltYear());
        mav.addObject("sltMonth", bbbean.getSltMonth());
        mav.addObject("txtbilltype", bbbean.getTxtbilltype());

        mav.setViewName(path);
        return mav;
    }

    @RequestMapping(value = "saveBillArrear", params = "action=Reprocess")
    public ModelAndView reprocessBillArrear(ModelMap model, @ModelAttribute("BillBrowserbean") BillBrowserbean bbbean, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpOffice") String selectedEmpOffice, BindingResult result, HttpServletResponse response) {
        //String path = "redirect:/getPayBillList.htm";
        String path = "redirect:/getPayBillList.htm?txtbilltype=" + bbbean.getTxtbilltype() + "&sltYear=" + bbbean.getSltYear() + "&sltMonth=" + bbbean.getSltMonth();
        BillDetail billDtls = billBrowserDao.getBillDetails(Integer.parseInt(bbbean.getBillNo()));
        bbbean.setSltYear(billDtls.getBillyear());

        bbbean.setTxtbilltype(billDtls.getTypeofBillString());
        ArrayList billYears = billBrowserDao.getBillPrepareYear(selectedEmpOffice);
        ArrayList billMonths = billBrowserDao.getMonthFromSelectedYear(selectedEmpOffice, bbbean.getSltYear(), bbbean.getTxtbilltype());
        ArrayList<BillBean> billList = billBrowserDao.getPayBillList(bbbean.getSltYear(), bbbean.getSltMonth(), selectedEmpOffice, bbbean.getTxtbilltype(), lub.getLoginspc());
        int priority = billBrowserDao.getBillPriority(selectedEmpOffice);
        bbbean.setOffCode(selectedEmpOffice);
        bbbean.setBgid(billDtls.getBillgroupId());
        bbbean.setPriority(priority);
        Office offobj = officeDao.getOfficeDetails(selectedEmpOffice);
        billBrowserDao.reprocessSingleBill(bbbean, offobj);
        ModelAndView mav = new ModelAndView(path, "command", bbbean);
        mav.addObject("billYears", billYears);
        mav.addObject("billMonths", billMonths);
        mav.addObject("billList", billList);
        mav.setViewName(path);
        return mav;
    }

    @RequestMapping(value = "saveBill", params = "action=Reprocess")
    public ModelAndView reprocessBill(ModelMap model, @ModelAttribute("BillBrowserbean") BillBrowserbean bbbean, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpOffice") String selectedEmpOffice, BindingResult result, HttpServletResponse response) {
        String path = "redirect:/getPayBillList.htm?txtbilltype=PAY&sltYear=" + bbbean.getSltYear() + "&sltMonth=" + bbbean.getSltMonth();
        System.out.println("reprocess");
        boolean isCollegeDHE = false;
        BillDetail billDtls = new BillDetail();
        ArrayList billYears = new ArrayList();
        ArrayList billMonths = new ArrayList();
        ArrayList<BillBean> billList = new ArrayList();

        ModelAndView mav = new ModelAndView(path, "command", bbbean);
        //isCollegeDHE = billBrowserDao.isCollegeUnderDHE(selectedEmpOffice);
        isCollegeDHE = billBrowserDao.isCollegeUnderDHE(bbbean.getOffCode());

        if (isCollegeDHE) {
            //Reprocess for Non Govt. Aided(DHE Colleges)
            billDtls = billBrowserDao.getBillDetailsDHE(Integer.parseInt(bbbean.getBillNo()), selectedEmpOffice);
            bbbean.setSltYear(billDtls.getBillyear());
            bbbean.setTxtbilltype("PAY");
            billYears = billBrowserDao.getBillPrepareYearDHE(selectedEmpOffice);
            billMonths = billBrowserDao.getMonthFromSelectedYearDHE(selectedEmpOffice, bbbean.getSltYear(), bbbean.getTxtbilltype());
            billList = billBrowserDao.getPayBillListDHEColleges(bbbean.getSltYear(), bbbean.getSltMonth(), selectedEmpOffice, bbbean.getTxtbilltype(), lub.getLoginspc());

        } else {
            System.out.println("Reprocess + getTxtbilltype():" + bbbean.getTxtbilltype());
            //Reprocess for Regular Bills
            bbbean.setIsDir("N");
            billDtls = billBrowserDao.getBillDetails(Integer.parseInt(bbbean.getBillNo()));
            bbbean.setSltYear(billDtls.getBillyear());
            bbbean.setTxtbilltype("PAY");
            billYears = billBrowserDao.getBillPrepareYear(selectedEmpOffice);
            billMonths = billBrowserDao.getMonthFromSelectedYear(selectedEmpOffice, bbbean.getSltYear(), bbbean.getTxtbilltype());
            billList = billBrowserDao.getPayBillList(bbbean.getSltYear(), bbbean.getSltMonth(), selectedEmpOffice, bbbean.getTxtbilltype(), lub.getLoginspc());
        }

        /*BillDetail billDtls = billBrowserDao.getBillDetails(Integer.parseInt(bbbean.getBillNo()));
         bbbean.setSltYear(billDtls.getBillyear());

         bbbean.setTxtbilltype("PAY");
         ArrayList billYears = billBrowserDao.getBillPrepareYear(selectedEmpOffice);
         ArrayList billMonths = billBrowserDao.getMonthFromSelectedYear(selectedEmpOffice, bbbean.getSltYear(), bbbean.getTxtbilltype());
         ArrayList<BillBean> billList = billBrowserDao.getPayBillList(bbbean.getSltYear(), bbbean.getSltMonth(), selectedEmpOffice, bbbean.getTxtbilltype(), lub.getLoginspc());*/
        GlobalBillStatus gbs = billBrowserDao.getBillProcessStatus(STOP_BILL_PROCESS);
        if (gbs.getGlobalVarValue().equalsIgnoreCase("Y")) {
            mav.addObject("msg", gbs.getMessage());
            path = "/payroll/ShowBillProcessStatus";
        } else {

            int priority = billBrowserDao.getBillPriority(lub.getLoginoffcode());
            bbbean.setOffCode(lub.getLoginoffcode());
            bbbean.setBgid(billDtls.getBillgroupId());
            bbbean.setPriority(priority);
            bbbean.setTxtbilltype(bbbean.getTxtbilltype());
            Office offobj = officeDao.getOfficeDetails(selectedEmpOffice);
            billBrowserDao.reprocessSingleBill(bbbean, offobj);

            mav.addObject("billYears", billYears);
            mav.addObject("billMonths", billMonths);
            mav.addObject("billList", billList);
        }
        mav.setViewName(path);
        return mav;
    }

    @RequestMapping(value = "saveBill", params = "action=Save", method = RequestMethod.POST)
    public ModelAndView saveBill(ModelMap model, @ModelAttribute("BillBrowserbean") BillBrowserbean bbbean, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response) {
        String path = "redirect:/getPayBillList.htm";
        BillDetail billDtls = billBrowserDao.getBillDetails(Integer.parseInt(bbbean.getBillNo()));
        bbbean.setSltYear(billDtls.getBillyear());

        billMastDAO.updateBillTotaling(Integer.parseInt(bbbean.getBillNo()));
        billBrowserDao.updateBillData(bbbean, billDtls.getTypeofBillString());

        ModelAndView mav = new ModelAndView(path, "command", bbbean);

        mav.addObject("sltYear", bbbean.getSltYear());
        mav.addObject("sltMonth", bbbean.getSltMonth());
        mav.addObject("txtbilltype", bbbean.getTxtbilltype());
        mav.setViewName(path);
        return mav;
    }

    @RequestMapping(value = "saveBillArrear", params = "action=Save", method = RequestMethod.POST)
    public ModelAndView saveBillArrear(ModelMap model, @ModelAttribute("BillBrowserbean") BillBrowserbean bbbean, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response) {
        String path = "redirect:/getPayBillList.htm";
        BillDetail billDtls = billBrowserDao.getBillDetails(Integer.parseInt(bbbean.getBillNo()));
        bbbean.setSltYear(billDtls.getBillyear());

        billBrowserDao.updateBillData(bbbean, billDtls.getTypeofBillString());

        ModelAndView mav = new ModelAndView(path, "command", bbbean);

        mav.addObject("sltYear", bbbean.getSltYear());
        mav.addObject("sltMonth", bbbean.getSltMonth());
        mav.addObject("txtbilltype", bbbean.getTxtbilltype());
        mav.setViewName(path);
        return mav;
    }

    @RequestMapping(value = "unlockbill")
    public ModelAndView getBillData(@ModelAttribute("billDetail") BillDetail billDetail, @ModelAttribute("LoginUserBean") LoginUserBean lub, Map<String, Object> model, HttpServletResponse response) throws IOException {
        ModelAndView mv = new ModelAndView();
        if (lub.getLoginuserid() != null && lub.getLoginusertype().equals("D")) {
            String errMsg = billBrowserDao.verifyUnlockBillDistrict(lub.getLogindistrictcode(), billDetail.getOffcode(), billDetail.getBillnumber());

            if (errMsg.equalsIgnoreCase("N")) {
                billDetail.setUsertype(lub.getLoginusertype());
                String billno = billDetail.getHidbillNo();
                List data = billBrowserDao.getBillData(billDetail);
                ArrayList billList = billGroupDAO.getBillTypeList();
                String status = billBrowserDao.getBillStatus(billDetail);
                mv.addObject("billstatus", status);
                mv.addObject("billList", billList);
                mv.addObject("data", data);
                mv.addObject("usrtype", billDetail.getUsertype());
                mv.addObject("usrtname", lub.getLoginusername());
                mv.setViewName("/payroll/UnlockBill");
            } else {
                mv.setViewName("/under_const");
            }
        } else if (lub.getLoginusertype().equals("A") || lub.getLoginusertype().equals("S")) {
            billDetail.setUsertype(lub.getLoginusertype());
            String billno = billDetail.getHidbillNo();
            List data = billBrowserDao.getBillData(billDetail);
            ArrayList billList = billGroupDAO.getBillTypeList();
            //System.out.println("billnobillnobillnobillno" + billno);
            String status = billBrowserDao.getBillStatus(billDetail);
            mv.addObject("billList", billList);
            mv.addObject("billstatus", status);
            mv.addObject("data", data);
            mv.addObject("usrtype", billDetail.getUsertype());
            mv.addObject("usrtname", lub.getLoginusername());
            mv.setViewName("/payroll/UnlockBill");
        } else {
            mv.setViewName("/under_const");
        }

        return mv;
    }

    @RequestMapping(value = "billEditNo", method = {RequestMethod.GET, RequestMethod.POST})
    public void billEditNoData(@ModelAttribute("billDetail") BillDetail billDetail, Map<String, Object> model, @RequestParam("billnumber") String billnumber, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = null;
        try {
            billDetail = billBrowserDao.getEditBillInfo(billnumber);

            billDetail.setBillnumber(billnumber);
            JSONObject jos = new JSONObject(billDetail);
            out = response.getWriter();
            out.write(jos.toString());
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @RequestMapping(value = "billStatusview", method = {RequestMethod.GET, RequestMethod.POST})
    public void billStatusview(@ModelAttribute("billDetail") BillDetail billDetail, Map<String, Object> model, @RequestParam("billnumber") String billnumber, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = null;
        try {
            billDetail = billBrowserDao.getStatusBillInfo(billnumber);

            billDetail.setBillnumber(billnumber);
            JSONObject jos = new JSONObject(billDetail);
            out = response.getWriter();
            out.write(jos.toString());
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @RequestMapping(value = "unlockbill", params = {"action=Update"})
    public ModelAndView UpdateBillInfo(Model model, @ModelAttribute("billDetail") BillDetail billDetail) {
        billBrowserDao.updateBillInfo(billDetail);
        return new ModelAndView("redirect:/unlockbill.htm");
    }

    @RequestMapping(value = "unlockbilldata")
    public ModelAndView unlockbill(HttpServletRequest request, Model model, @ModelAttribute("billDetail") BillDetail billDetail, @ModelAttribute("LoginUserBean") LoginUserBean lub) throws Exception {
        String ipaddress = CommonFunctions.getIpAndHost(request);
        billBrowserDao.unlockBill(billDetail, lub.getLoginuserid(), ipaddress);
        return new ModelAndView("redirect:/unlockbill.htm");
    }

    @RequestMapping(value = "unlockbilltoResubmit")
    public ModelAndView unlockBillToResubmit(HttpServletRequest request, Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("billDetail") BillDetail billDetail) throws Exception {
        String ipaddress = CommonFunctions.getIpAndHost(request);
        billBrowserDao.unlockBillToResubmit(billDetail, ipaddress, lub.getLoginuserid());
        return new ModelAndView("redirect:/unlockbill.htm");
    }

    @RequestMapping(value = "unlockBillForError")
    public ModelAndView unlockBillForError(HttpServletRequest request, Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("billDetail") BillDetail billDetail) throws Exception {
        String ipaddress = CommonFunctions.getIpAndHost(request);
        billBrowserDao.unlockBillForError(billDetail, ipaddress, lub.getLoginuserid());
        return new ModelAndView("redirect:/unlockbill.htm");
    }

    @RequestMapping(value = "unlockbill", params = {"action=ObjectBill"})
    public ModelAndView objectBill(Model model, @ModelAttribute("billDetail") BillDetail billDetail) {
        billBrowserDao.objectBill(billDetail);
        return new ModelAndView("redirect:/unlockbill.htm");
    }

    @RequestMapping(value = "editunlockBill")
    public ModelAndView editunlockBill(ModelMap model, @ModelAttribute("billDetail") BillBrowserbean bbbean, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response) {

        String billNo = bbbean.getBillNo();

        CommonReportParamBean crb = paybillDmpDao.getCommonReportParameter(bbbean.getBillNo());
        bbbean.setSltMonth(crb.getAqmonth());
        bbbean.setSltYear(crb.getAqyear());
        bbbean.setTxtbilltype(crb.getTypeofBill());
        bbbean.setChartofAcct(billBrowserDao.getBillChartofAccount(Integer.parseInt(bbbean.getBillNo())));

        BillDetail billDtls = billBrowserDao.getBillDetails(Integer.parseInt(bbbean.getBillNo()));

        bbbean.setBilldesc(billDtls.getBillnumber());
        bbbean.setBenificiaryNumber(billDtls.getBeneficiaryrefno());
        bbbean.setVchDt(billDtls.getVchDt());
        bbbean.setVchNo(billDtls.getVchNo());
        bbbean.setBillDate(billDtls.getBillDate());
        bbbean.setTreasury(billDtls.getTreasuryCode());
        bbbean.setStatus(billDtls.getBillStatusId());
        List treasuryList = treasuryDao.getTreasuryList();
        String path = "/payroll/EditunlockBill";
        ModelAndView mav = new ModelAndView(path, "command", bbbean);
        mav.addObject("paydata", billBrowserDao.getPay(Integer.parseInt(bbbean.getBillNo()), lub.getLoginoffcode()));
        mav.addObject("allowanceList", billBrowserDao.getAllowanceList(Integer.parseInt(bbbean.getBillNo()), crb.getAqmonth(), crb.getAqyear()));
        mav.addObject("deductionList", billBrowserDao.getDeductionList(Integer.parseInt(bbbean.getBillNo()), crb.getAqmonth(), crb.getAqyear()));
        mav.addObject("pvtloanList", billBrowserDao.getPvtLoanList(Integer.parseInt(bbbean.getBillNo()), crb.getAqmonth(), crb.getAqyear()));
        mav.addObject("treasuryList", treasuryList);
        mav.addObject("billDtls", billDtls);
        mav.addObject("usrtname", lub.getLoginusername());

        return mav;

    }

    /*@RequestMapping(value = "unlockbill",params = {"action=Update"} )
     public ModelAndView billEdit(Model model, @ModelAttribute("billDetail") BillDetail billDetail ) {
        
     
     //List data = billBrowserDao.getBillData(billDetail);
     //ArrayList billList = billGroupDAO.getBillTypeList();
     //ModelAndView mv = new ModelAndView();
     //billBrowserDao.addObject("billList", billList);
     //mv.addObject("data", data);
     String billno = billDetail.getHidbillNo();
     
     billBrowserDao.updateBillInfo(billDetail);
     //mv.setViewName("/payroll/UnlockBill");
     return new ModelAndView("redirect:/unlockbill.htm");
     }*/
    @RequestMapping(value = "AssignNewBillNo")
    public ModelAndView assignNewBillNo(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response) {
        ModelAndView mv = new ModelAndView();
        String path = "/payroll/AssignNewBillNo";
        mv.setViewName(path);
        return mv;
    }

    @ResponseBody
    @RequestMapping(value = "GetPreviousBillDetail", method = RequestMethod.GET)
    public void getPreviousBillDetail(HttpServletRequest request, HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("billNo") String billNo) throws IOException {
        response.setContentType("application/html");
        PrintWriter out = response.getWriter();
        StringBuffer content
                = new StringBuffer("");
        BillDetail bd = new BillDetail();
        boolean isExisting = false;
        try {
            isExisting = billBrowserDao.checkBillNo(Integer.parseInt(billNo));
            if (isExisting) {
                bd = billBrowserDao.getBillDetails(Integer.parseInt(billNo));
                content.append("<table class=\"table table-bordered table-hover table-striped\">"
                        + "                                    <thead>"
                        + "                                        <tr>"
                        + "                                            <th>Bill Number</th>"
                        + "                                            <th>Bill Date</th>"
                        + "                                            <th>Bill Desc</th>"
                        + "                                            <th>Month</th>"
                        + "                                            <th>Year</th>"
                        + "                                            <th>Type Of Bill</th>"
                        + "                                            <th>Assign New Bill Number</th>"
                        + "                                        </tr>"
                        + "                                    </thead>"
                        + "                                    <tbody>"
                        + "                                            <tr>"
                        + "                                                <td>" + billNo
                        + "</td>"
                        + "                                                <td>" + bd.getBillDate() + "</td>"
                        + "                                                <td>" + bd.getBilldesc() + "</td>"
                        + "                                                <td>" + CommonFunctions.getMonthAsString(bd.getBillmonth() - 1) + "</td>"
                        + "                                                <td>" + bd.getBillyear() + "</td>"
                        + "                                                <td>" + bd.getTypeofBillString() + "</td>"
                        + "                                            <th><input type=\"button\" id='btn_generate' style='background:#FF0000;font-weight:bold;' class=\"btn btn-sm btn-danger \" value=\"Assign New Bill No\" onclick=\"javascript:regenerateBillNo(" + billNo + ")\" /> <span id=\"loader1\" style=\"display:none;color:#999999;font-size:8pt;font-style:italic;\"><img src=\"images/ajax-loader.gif\" /> Please wait...</span></th>"
                        + "                                            </tr>"
                        + "                                    </tbody>"
                        + "                                </table>");
            } else {
                content.append("<h1 style='color:#FF0000;font-size:15pt;'>Sorry, no records found. Please check the Bill Number and Search.</h1>");
            }

            out.print(content);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    @ResponseBody
    @RequestMapping(value = "SaveNewBillNo", method = RequestMethod.GET)
    public void SaveNewBillNo(HttpServletRequest request, HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("billNo") String billNo) throws IOException {
        response.setContentType("application/html");
        PrintWriter out = response.getWriter();
        int newBillNo = 0;
        StringBuffer content
                = new StringBuffer("");
        BillDetail bd = new BillDetail();
        try {
            newBillNo = billBrowserDao.assignNewBillNo(Integer.parseInt(billNo));
            bd = billBrowserDao.getBillDetails(newBillNo);
            content.append("<div style='width:40%;margin:0px auto;margin-bottom:10px;border-radius:5px;line-height:30px;font-weight:bold;text-align:center;background:#890000;color:#FFFFFF;font-size:15pt;'>New Bill Number: " + newBillNo + "</div>");
            content.append("<table class=\"table table-bordered table-hover table-striped\">"
                    + "                                    <thead>"
                    + "                                        <tr>"
                    + "                                            <th>New Bill Number</th>"
                    + "                                            <th>Bill Date</th>"
                    + "                                            <th>Bill Desc</th>"
                    + "                                            <th>Month</th>"
                    + "                                            <th>Year</th>"
                    + "                                            <th>Type Of Bill</th>"
                    + "                                        </tr>"
                    + "                                    </thead>"
                    + "                                    <tbody>"
                    + "                                            <tr>"
                    + "                                                <td>" + billNo
                    + "</td>"
                    + "                                                <td>" + bd.getBillDate() + "</td>"
                    + "                                                <td>" + bd.getBilldesc() + "</td>"
                    + "                                                <td>" + CommonFunctions.getMonthAsString(bd.getBillmonth() - 1) + "</td>"
                    + "                                                <td>" + bd.getBillyear() + "</td>"
                    + "                                                <td>" + bd.getTypeofBillString() + "</td>"
                    + "                                            </tr>"
                    + "                                    </tbody>"
                    + "                                </table>");

            out.print(content);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    @RequestMapping(value = "getFailedTransactionData")
    public String getFailedTransactionData(Model model, @ModelAttribute("ftbean") FailedTransactionBean ftbean, @ModelAttribute("SelectedEmpOffice") String selectedEmpOffice, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("billNo") int billno) throws IOException {

        try {
            ArrayList ftlist = billBrowserDao.getFailedTransactionData(billno);
            //System.out.println("ftlist size is: "+ftlist.size());
            model.addAttribute("ftlist", ftlist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/payroll/FailedTransactionDataList";
    }

    @RequestMapping(value = "unlockFortyPercentVoucheredBillPage")
    public ModelAndView getVoucheredBillData(@ModelAttribute("billDetail") BillDetail billDetail, @ModelAttribute("LoginUserBean") LoginUserBean lub, Map<String, Object> model,
            HttpServletResponse response) throws IOException {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/payroll/UnlockVoucheredBill");
        return mv;
    }

    @RequestMapping(value = "unlockFortyPercentVoucheredBill.htm")
    public ModelAndView getFortyPercentVoucheredBillData(@ModelAttribute("billDetail") BillDetail billDetail, @ModelAttribute("LoginUserBean") LoginUserBean lub,
            Map<String, Object> model,
            HttpServletResponse response) throws IOException {
        ModelAndView mv = new ModelAndView();

        if (lub.getLoginuserid() != null && lub.getLoginusertype().equals("D")) {
            String errMsg = billBrowserDao.verifyUnlockBillDistrict(lub.getLogindistrictcode(), billDetail.getOffcode(), billDetail.getBillnumber());

            if (errMsg.equalsIgnoreCase("N")) {
                billDetail.setUsertype(lub.getLoginusertype());
                //String billno = billDetail.getHidbillNo();
                List data = billBrowserDao.getFortyPercentVoucheredBillData(billDetail);
                ArrayList billList = billGroupDAO.getBillTypeList();
                mv.addObject("billList", billList);
                mv.addObject("data", data);
                mv.addObject("usrtype", billDetail.getUsertype());
                mv.addObject("usrtname", lub.getLoginusername());
                mv.setViewName("/payroll/UnlockVoucheredBill");
            } else {
                mv.setViewName("/under_const");
            }
        } else if (lub.getLoginusertype().equals("A") || lub.getLoginusertype().equals("S")) {
            billDetail.setUsertype(lub.getLoginusertype());
            //String billno = billDetail.getHidbillNo();
            List data = billBrowserDao.getFortyPercentVoucheredBillData(billDetail);
            ArrayList billList = billGroupDAO.getBillTypeList();
            mv.addObject("billList", billList);
            mv.addObject("data", data);
            mv.addObject("usrtype", billDetail.getUsertype());
            mv.addObject("usrtname", lub.getLoginusername());
            mv.setViewName("/payroll/UnlockVoucheredBill");
        } else {
            mv.setViewName("/under_const");
        }

        return mv;
    }

    @RequestMapping(value = "unlockVoucheredBillData.htm")
    public ModelAndView unlockVoucheredBillData(@RequestParam("billnumber") int billno, @ModelAttribute("LoginUserBean") LoginUserBean lub, Map<String, Object> model,
            HttpServletResponse response) throws IOException {
        ModelAndView mv = null;
        boolean isUnlocked = false;
        try {
            isUnlocked = billBrowserDao.unlockFortyPercentVoucheredBill(billno);
            if (isUnlocked) {
                mv = new ModelAndView("redirect:/unlockFortyPercentVoucheredBill.htm?billnumber=" + billno);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return mv;
    }

    @RequestMapping(value = "updateVoucherBillStatus.htm")
    public ModelAndView updateVoucherBillStatus(@RequestParam("billnumber") int billno, @ModelAttribute("LoginUserBean") LoginUserBean lub, Map<String, Object> model,
            HttpServletResponse response) throws IOException {
        ModelAndView mv = null;
        boolean isVouchered = false;
        try {
            isVouchered = billBrowserDao.updateFortyPercentVoucheredBillStatus(billno);
            if (isVouchered) {
                mv = new ModelAndView("redirect:/unlockFortyPercentVoucheredBill.htm?billnumber=" + billno);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mv;
    }

    @RequestMapping(value = "veryfyBill.htm")
    public ModelAndView verifyBillDHE(Model model, @RequestParam("billNo") int billno, @RequestParam("aqmonth") int aqmonth, @RequestParam("aqyear") int aqyear, @ModelAttribute("LoginUserBean") LoginUserBean lub,
            @ModelAttribute("SelectedEmpOffice") String selectedEmpOffice, @ModelAttribute("BillBrowserbean") BillBrowserbean bbbean) throws Exception {
        boolean isVerified = true;
        ArrayList<BillBean> billList = new ArrayList();
        ArrayList billYears = new ArrayList();
        ArrayList billMonths = new ArrayList();
        ModelAndView mav = null;
        String path = "redirect:/getPayBillList.htm";
        try {
            System.out.println("selectedEmpOffice::" + selectedEmpOffice + "," + billno + "," + aqmonth + "," + aqyear);
            isVerified = billBrowserDao.updatebillVerificationStatus(selectedEmpOffice, billno, aqmonth, aqyear);

            if (isVerified) {
                billList = billBrowserDao.getPayBillListDHEColleges(aqyear, aqmonth, selectedEmpOffice, "PAY", lub.getLoginspc());
                billYears = billBrowserDao.getBillPrepareYearDHE(selectedEmpOffice);
                billMonths = billBrowserDao.getMonthFromSelectedYearDHE(selectedEmpOffice, aqyear, "PAY");
                bbbean.setTxtbilltype(bbbean.getTxtbilltype());
                mav = new ModelAndView(path, "command", bbbean);
                mav.addObject("billYears", billYears);
                mav.addObject("billMonths", billMonths);
                mav.addObject("billList", billList);
                mav.setViewName(path);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;

    }

    @RequestMapping(value = "billVerificationStatus.htm")
    public ModelAndView billVerificationStatus(Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub,
            @ModelAttribute("SelectedEmpOffice") String selectedEmpOffice, @ModelAttribute("billBean") BillBean bbbean) throws Exception {
        List billgrouplist = new ArrayList();
        ModelAndView mav = new ModelAndView();
        String path = "/master/BillGroupWiseBillVerificationReport";
        try {

            billgrouplist = billGroupDAO.getBillGroupList(selectedEmpOffice);
            mav.addObject("billGrouplist", billgrouplist);
            mav.setViewName(path);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;

    }

    @RequestMapping(value = "getOfficeBillVerificationStatus.htm")
    public ModelAndView getOfficeBillVerificationStatus(Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub,
            @ModelAttribute("SelectedEmpOffice") String selectedEmpOffice, @ModelAttribute("billBean") BillBean bbbean) throws Exception {
        List billGroupwiseOfcMappedList = new ArrayList();
        List billgrouplist = new ArrayList();
        ModelAndView mav = new ModelAndView();
        String path = "/master/BillGroupWiseBillVerificationReport";
        Calendar cal = Calendar.getInstance();
        String allowProcess = null;
        try {
            billgrouplist = billGroupDAO.getBillGroupList(selectedEmpOffice);
            billGroupwiseOfcMappedList = aqmastDao.getBillgroupwiseOfficeMapped(bbbean.getSltbillgroupId(), bbbean.getBillMonth(), bbbean.getBillYear());
            mav.addObject("billGroupwiseOfcMappedList", billGroupwiseOfcMappedList);
            mav.addObject("billGrouplist", billgrouplist);
            mav.addObject("aqgroup", bbbean.getSltbillgroupId());
            mav.addObject("billmonth", bbbean.getBillMonth());
            mav.addObject("billyear", bbbean.getBillYear());
            //System.out.println("Current Month & year:" + cal.get(Calendar.YEAR) + ":" + cal.get(Calendar.MONTH));
            if ((bbbean.getBillMonth() == cal.get(Calendar.MONTH)) && (bbbean.getBillYear() == cal.get(Calendar.YEAR))) {
                allowProcess = "Y";
            } else {
                allowProcess = "N";
            }
            mav.addObject("allowProcess", allowProcess);
            mav.addObject("billyear", bbbean.getBillYear());
            bbbean.setHidBillGrpId(bbbean.getSltbillgroupId());
            mav.setViewName(path);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;

    }

    @ResponseBody
    @RequestMapping(value = "viewprivilegeEmpJSON", method = {RequestMethod.GET, RequestMethod.POST})
    public void viewLastSalary(ModelMap model, HttpServletResponse response, @ModelAttribute("mergeDuplicateHrmsid") mergeDuplicateHrmsidForm mergingForm, @ModelAttribute("LoginUserBean") LoginUserBean lub,
            @RequestParam("offcode") String offcode) {
        response.setContentType("application/json");
        PrintWriter out = null;
        JSONArray json = null;
        try {
            System.out.println("priv");
            List ofcprivList = billBrowserDao.getprivilegedetails(offcode);
            json = new JSONArray(ofcprivList);
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
    @RequestMapping(value = "viewEmployeeNotForArrearJSON", method = {RequestMethod.GET, RequestMethod.POST})
    public void viewEmployeeNotForArrearJSON(ModelMap model, HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("billGroupId") String billGroupId) {
        response.setContentType("application/json");
        PrintWriter out = null;
        JSONArray json = null;
        try {
            System.out.println("bgid:" + billGroupId);
            List notArrearEmpList = billBrowserDao.getempListNotForArrear(billGroupId);

            json = new JSONArray(notArrearEmpList);
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
    @RequestMapping(value = "checkNewBillGeneratedorNot", method = {RequestMethod.GET, RequestMethod.POST})
    public void checkNewBillGeneratedorNot(HttpServletRequest request, HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub,
            @RequestParam("billgroupId") String billgroupId, @RequestParam("billmonth") String billmonth, @RequestParam("billyear") String billyear) throws Exception {
        Calendar cal = Calendar.getInstance();

        response.setContentType("application/json");
        PrintWriter out = null;
        try {
            //System.out.println("billgroupId:" + billgroupId);
            out = response.getWriter();
            JSONObject obj = new JSONObject();
            String newbillid = billBrowserDao.checkNewBillGenerationStatus(billgroupId, Integer.parseInt(billmonth), Integer.parseInt(billyear));
            //System.out.println("newbillid:" + newbillid);
            obj.append("billno", newbillid);
            out.write(obj.toString());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    @RequestMapping(value = "DeleteOfficeFromBillDHE", method = {RequestMethod.GET, RequestMethod.POST})
    public void DeleteOfficeFromBillDHE(HttpServletRequest request, HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub,
            @RequestParam("billNo") String billNo, @RequestParam("offcode") String offcode) throws Exception {

        // System.out.println("ofc:" + offcode + "billno:" + billNo);
        billBrowserDao.deleteOfficeDHE(billNo, offcode);
    }

}
