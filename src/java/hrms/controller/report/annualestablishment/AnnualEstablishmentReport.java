/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.report.annualestablishment;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import hrms.SelectOption;
import hrms.common.CommonFunctions;
import hrms.common.ValueComparator;
import hrms.dao.employee.EmployeeDAO;
import hrms.dao.fiscalyear.FiscalYearDAO;
import hrms.dao.master.BankDAO;
import hrms.dao.master.BlockDAO;
import hrms.dao.master.BranchDAO;
import hrms.dao.master.DepartmentDAO;
import hrms.dao.master.DistrictDAO;
import hrms.dao.master.OfficeDAO;
import hrms.dao.master.PayScaleDAO;
import hrms.dao.master.PostDAO;
import hrms.dao.master.StateDAO;
import hrms.dao.master.SubDivisionDAO;
import hrms.dao.master.SubStantivePostDAO;
import hrms.dao.master.TreasuryDAO;
import hrms.dao.report.annualestablishment.AnnuaiEstablishmentReportDAO;
import hrms.dao.report.officewisesanctionedstrength.OfficeWiseSanctionedStrengthDAO;
import hrms.model.login.LoginUserBean;
import hrms.model.master.Block;
import hrms.model.master.Department;
import hrms.model.master.Office;
import hrms.model.master.State;
import hrms.model.master.SubDivision;
import hrms.model.master.SubstantivePost;
import hrms.model.report.annualestablishmentreport.AerListBox;
import hrms.model.report.annualestablishmentreport.AnnualEstablishment;
import hrms.model.report.annualestablishmentreport.ScheduleIIBean;
import hrms.model.workflowrouting.WorkflowRouting;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import jxl.Workbook;
import jxl.write.WritableWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;
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

/**
 *
 * @author Surendra
 */
@SessionAttributes("LoginUserBean")
@Controller
public class AnnualEstablishmentReport implements ServletContextAware {

    private ServletContext context;

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.context = servletContext;
    }

    @Autowired
    AnnuaiEstablishmentReportDAO annuaiEstablishmentDao;

    @Autowired
    OfficeWiseSanctionedStrengthDAO officeWiseSanctionedStrengthDAO;

    @Autowired
    public DepartmentDAO deptDAO;

    @Autowired
    public PostDAO postdao;

    @Autowired
    public OfficeDAO officeDao;

    @Autowired
    SubStantivePostDAO subStantivePostDao;

    @Autowired
    public PayScaleDAO payscaleDAO;

    @Autowired
    BankDAO bankDAO;

    @Autowired
    BranchDAO branchDAO;

    @Autowired
    TreasuryDAO treasuryDao;

    @Autowired
    BlockDAO blockDAO;

    @Autowired
    StateDAO stateDAO;

    @Autowired
    EmployeeDAO employeeDAO;

    @Autowired
    DistrictDAO districtDAO;

    @Autowired
    SubDivisionDAO subDivisionDAO;

    @Autowired
    FiscalYearDAO fiscalDAO;

    @RequestMapping(value = "aerstatuslist.htm")
    public ModelAndView deptWiseAerStatus(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") AnnualEstablishment ae) {
        ModelAndView mav = null;
        mav = new ModelAndView("report/DepartmentWiseAerStatus", "aerstatuslist", ae);
        List fylist = annuaiEstablishmentDao.getFinancialYearList();
        mav.addObject("financialYearList", fylist);
        if (ae.getFy() != null && !ae.getFy().equals("")) {
            List aerStatusList = annuaiEstablishmentDao.departmentWiseAerStatus(ae.getFy());
            mav.addObject("aerStatusList", aerStatusList);
        }

        return mav;

    }

    @RequestMapping(value = "aerreportsubmittedofflist.htm")
    public ModelAndView aerSubmittedOfficeList(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("deptCode") String deptCode, @RequestParam("finYear") String finYear) {
        ModelAndView mav = null;
        mav = new ModelAndView("report/AerSubmittedOfficeList");
        List offList = annuaiEstablishmentDao.aerSubmittedOfficeList(deptCode, finYear);
        mav.addObject("offList", offList);
        return mav;

    }

    @RequestMapping(value = "aerreportapprovedofflist.htm")
    public ModelAndView aerReportApprovedOfflist(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("deptCode") String deptCode, @RequestParam("finYear") String finYear) {
        ModelAndView mav = null;
        mav = new ModelAndView("report/AerSubmittedOfficeList");
        List offList = annuaiEstablishmentDao.aerApprovedOfficeList(deptCode, finYear);
        mav.addObject("offList", offList);
        return mav;

    }

    @RequestMapping(value = "displayReportList")
    public ModelAndView displayReportList(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") AnnualEstablishment ae, @RequestParam Map<String, String> parameters) {
        ModelAndView mav = null;
        mav = new ModelAndView("report/AnnualEstablishmentReportList", "command", ae);
        List<AnnualEstablishment> submitList = annuaiEstablishmentDao.getAerReportList(lub.getLoginoffcode(), ae.getAerId());
        AnnualEstablishment aer = annuaiEstablishmentDao.submittedforCurrentFinancialYear(lub.getLoginoffcode(), ae);

        String status = aer.getStatus();
        String stat = "";

        if (status != null && !status.equals("")) {
            mav = new ModelAndView("report/AnnualEstablishmentReportList", "command", ae);
            if (status.equals("YES")) {
                stat = "YES";
            } else if (status.equals("REV")) {
                stat = "REV";
            }
            mav.addObject("visible", stat);
            mav.addObject("OffName", lub.getLoginoffname());
            mav.addObject("MasterList", submitList);
        } else {
            stat = "NO";
            mav = new ModelAndView("redirect:/displayEstablishmentReport.htm?fy=" + ae.getFy() + "&aerId=" + ae.getAerId(), "command", ae);
        }

        return mav;
    }

    @RequestMapping(value = "displayEstablishmentReport")
    public ModelAndView displayEstablishmentReport(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") AnnualEstablishment ae, @RequestParam Map<String, String> req) {
        ModelAndView mav = new ModelAndView();

        Map<String, String> map = annuaiEstablishmentDao.getAuthorityList(lub.getLoginoffcode());
        String fyyear = req.get("fy");

        Map<String, String> sortedMap = new TreeMap<String, String>(new ValueComparator(map));
        sortedMap.putAll(map);

        ae = annuaiEstablishmentDao.geteditAerdata(Integer.parseInt(req.get("aerId")));
        mav = new ModelAndView("report/AnnualEstablishmentReport", "command", ae);

        int issingleCo = annuaiEstablishmentDao.getisSingleCo(ae.getAerId());
        List<AnnualEstablishment> submitList = annuaiEstablishmentDao.getSubmittedReportList(lub.getLoginoffcode(), ae.getFy(), ae.getAerId());
        if (submitList.size() > 0) {

            mav.addObject("EstablishmentList", submitList);
            mav.addObject("submitted", "Y");

        } else {

            AerListBox abox = annuaiEstablishmentDao.getAnnualEstablistmentReportList(lub.getLoginoffcode(), ae.getAerId(), issingleCo);
            mav.addObject("groupADataSystem", abox.getGroupADataSystem());
            mav.addObject("groupBDataSystem", abox.getGroupBDataSystem());
            mav.addObject("groupCDataSystem", abox.getGroupCDataSystem());
            mav.addObject("groupDDataSystem", abox.getGroupDDataSystem());
            mav.addObject("grantInAidSystem", abox.getGrantInAidSystem());
            mav.addObject("EstablishmentList", abox.getLi());
            mav.addObject("AuthListArray", sortedMap);
            mav.addObject("submitted", "N");
        }
        annuaiEstablishmentDao.submittedforCurrentFinancialYear(lub.getLoginoffcode(), ae);

        List deptlist = deptDAO.getDepartmentList();
        mav.addObject("deptlist", deptlist);
        mav.addObject("OffName", lub.getLoginoffname());

        mav.addObject("fy", fyyear);
        return mav;
    }

    @RequestMapping(value = "submitEstablishmentReport", params = {"btnAer=Back"})
    public ModelAndView backToEstablishment(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") AnnualEstablishment ae, @RequestParam Map<String, String> C) {
        ModelAndView mav = null;
        //mav = new ModelAndView("redirect:/getOfficeWiseSanctionedStrength.htm");
        return new ModelAndView("redirect:/displayAERlist.htm?financialYear=" + ae.getFinancialYear(), "command", ae);
        //return mav;
    }

    /*
     @RequestMapping(value = "submitEstablishmentReport", params = {"btnAer=Submit"})
     public ModelAndView submitEstablishmentReport(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") AnnualEstablishment ae, @RequestParam Map<String, String> C) {
     ModelAndView mav = null;
     Document document = new Document();
     String fy = "";
     int aerId = 0;
     try {
           
     aerId = ae.getAerId();
     int issingleCo = annuaiEstablishmentDao.getisSingleCo(ae.getAerId());
     AerListBox abox = annuaiEstablishmentDao.getAnnualEstablistmentReportList(lub.getLoginoffcode(), ae.getAerId(), issingleCo);
     String serverfilePath = context.getInitParameter("AERPath");
     String filePath = serverfilePath + "AER_" + lub.getLoginoffcode() + "_" + ae.getAerId() + ".pdf";
     
     PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filePath));
     document.setPageSize(PageSize.A4.rotate());
     document.open();

     PdfPTable table = new PdfPTable(11);
     table.setWidths(new float[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1});
     table.setWidthPercentage(100);
     int i = 1;

     PdfPCell cell = new PdfPCell(new Phrase(lub.getLoginoffname()));
     cell.setColspan(11);
     cell.setHorizontalAlignment(Element.ALIGN_CENTER);
     table.addCell(cell);

     cell = new PdfPCell(new Phrase("ANNUAL ESTABLISHMENT REVIEW REPORT- " + ae.getFy()));
     cell.setColspan(11);
     cell.setHorizontalAlignment(Element.ALIGN_CENTER);
     table.addCell(cell);
            
            

     table.addCell("Sl No");
     table.addCell("Description of the Posts");
     table.addCell("Group");
     table.addCell("As per 6th Pay Commission");
     table.addCell("Grade Pay");
     table.addCell("As per 7th Pay Commission");
     table.addCell("Level in the Pay Matrix as per ORSP Rules, 2017");
     table.addCell("Sanctioned Strength ");
     table.addCell("Men in Position");
     table.addCell("Vacancy Position");
     table.addCell("Remarks");

     table.addCell("1");
     table.addCell("2");
     table.addCell("3");
     table.addCell("4");
     table.addCell("5");
     table.addCell("6");
     table.addCell("7");
     table.addCell("8");
     table.addCell("9");
     table.addCell("10");
     table.addCell("11");
     ae.setOffCode(lub.getLoginoffcode());
     fy = ae.getFy();
     annuaiEstablishmentDao.addAERMaster(ae, lub.getLoginempid(), aerId);

     for (AnnualEstablishment aer : abox.getLi()) {
     annuaiEstablishmentDao.addAEReportData(aer, lub.getLoginempid(), fy);

     table.addCell(i + "");
     table.addCell(aer.getPostname());
     table.addCell(aer.getGroup());
     table.addCell(aer.getScaleofPay());
     table.addCell(aer.getGp());
     table.addCell(aer.getScaleofPay7th());
     table.addCell(aer.getLevel());
     table.addCell(aer.getSanctionedStrength() + "");
     table.addCell(aer.getMeninPosition() + "");
     table.addCell(aer.getVacancyPosition() + "");
     table.addCell("");

     i++;

     }

     document.add(table);

     } catch (Exception e) {
     e.printStackTrace();
     } finally {
     document.close();
     }
     return new ModelAndView("redirect:/displayAERlist.htm?financialYear=" + ae.getFinancialYear(), "command", ae);

     }
    
     */
    @RequestMapping(value = "submitForPrivewEstablishmentReport")
    public ModelAndView submitForPrivewEstablishmentReport(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") AnnualEstablishment ae, @RequestParam Map<String, String> param) {
        ModelAndView mav = null;

        try {

            boolean dataAvailable = annuaiEstablishmentDao.privewDataInserted(Integer.parseInt(param.get("aerId")));

            if (dataAvailable) {

            } else {
                annuaiEstablishmentDao.addAEReportDataPartA(Integer.parseInt(param.get("aerId")), "A", "AIS");
                annuaiEstablishmentDao.addAEReportDataPartA(Integer.parseInt(param.get("aerId")), "A", "UGC");
                annuaiEstablishmentDao.addAEReportDataPartA(Integer.parseInt(param.get("aerId")), "A", "OJS");
                annuaiEstablishmentDao.addAEReportDataPartA(Integer.parseInt(param.get("aerId")), "A", "");
                annuaiEstablishmentDao.addAEReportDataPartA(Integer.parseInt(param.get("aerId")), "B", "");
                annuaiEstablishmentDao.addAEReportDataPartA(Integer.parseInt(param.get("aerId")), "C", "");
                annuaiEstablishmentDao.addAEReportDataPartA(Integer.parseInt(param.get("aerId")), "D", "");

                annuaiEstablishmentDao.addAEReportDataPartB(Integer.parseInt(param.get("aerId")), "A", "AIS");
                annuaiEstablishmentDao.addAEReportDataPartB(Integer.parseInt(param.get("aerId")), "A", "UGC");
                annuaiEstablishmentDao.addAEReportDataPartB(Integer.parseInt(param.get("aerId")), "A", "OJS");
                annuaiEstablishmentDao.addAEReportDataPartB(Integer.parseInt(param.get("aerId")), "A", "");
                annuaiEstablishmentDao.addAEReportDataPartB(Integer.parseInt(param.get("aerId")), "B", "");
                annuaiEstablishmentDao.addAEReportDataPartB(Integer.parseInt(param.get("aerId")), "C", "");
                annuaiEstablishmentDao.addAEReportDataPartB(Integer.parseInt(param.get("aerId")), "D", "");

                annuaiEstablishmentDao.addAEReportDataPartC(Integer.parseInt(param.get("aerId")));

                annuaiEstablishmentDao.addAEReportDataOther(lub.getLoginoffcode(), Integer.parseInt(param.get("aerId")), "PART-D");
                annuaiEstablishmentDao.addAEReportDataOther(lub.getLoginoffcode(), Integer.parseInt(param.get("aerId")), "PART-E");

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return new ModelAndView("redirect:/aerReportPartA.htm?aerId=" + Integer.parseInt(param.get("aerId")));

    }

    @RequestMapping(value = "submitEstablishmentReport")
    public ModelAndView submitEstablishmentReport(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") AnnualEstablishment ae, @RequestParam Map<String, String> param) {
        ModelAndView mav = null;
        try {
            String offcode = lub.getLoginoffcode();
            String spc = lub.getLoginspc();
            String officename = lub.getLoginoffname();

            if ((lub.getLoginAdditionalChargeOffCode() != null && !lub.getLoginAdditionalChargeOffCode().equals("")) && (lub.getLoginAdditionalChargeSpc() != null && !lub.getLoginAdditionalChargeSpc().equals(""))) {
                offcode = lub.getLoginAdditionalChargeOffCode();
                spc = lub.getLoginAdditionalChargeSpc();
                officename = lub.getLoginadditionalChargeOfficename();
            }
            annuaiEstablishmentDao.submitEstablishmentReportAsOperator(Integer.parseInt(param.get("aerId")), offcode, lub.getLoginempid(), spc);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return new ModelAndView("redirect:/aerReportPartA.htm?aerId=" + Integer.parseInt(param.get("aerId")));

    }

    @RequestMapping(value = "downloadEstablishmentReport")
    public void downloadReport(HttpServletRequest request, HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam Map<String, String> req) {

        String serverfilePath = context.getInitParameter("AERPath");
        String fileName = annuaiEstablishmentDao.getAerFileName(req.get("aerId"));
        Path file = Paths.get(serverfilePath, fileName);
        if (Files.exists(file)) {
            response.setContentType("application/pdf");
            response.addHeader("Content-Disposition", "attachment; filename=" + fileName);
            try {
                Files.copy(file, response.getOutputStream());
                response.getOutputStream().flush();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    @RequestMapping(value = "submitEstablishmentReport", params = {"btnAer=Return"})
    public ModelAndView returnReport(HttpServletRequest request, HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") AnnualEstablishment ae) {

        return new ModelAndView("redirect:/displayReportList.htm", "command", ae);
    }

    @RequestMapping(value = "authApprovedAER")
    public ModelAndView approve(HttpServletRequest request, HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") AnnualEstablishment ae, @RequestParam Map<String, String> parameters) {

        String taskId = parameters.get("taskId");
        ae.setTaskid(Integer.parseInt(taskId));
        String submitted = annuaiEstablishmentDao.getAERStatus(Integer.parseInt(taskId));
        ModelAndView mav = new ModelAndView("/report/AuthReportViewPage", "command", ae);
        List<AnnualEstablishment> li = annuaiEstablishmentDao.getAnnualEstablistmentReportListFromAuthLogin(taskId);
        mav.addObject("EstablishmentList", li);
        mav.addObject("submitted", submitted);
        return mav;
    }

    @RequestMapping(value = "approvedAERByAuth", params = {"btnAer=Approve"})
    public ModelAndView approvedAERByAuth(HttpServletRequest request, HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") AnnualEstablishment ae) {

        annuaiEstablishmentDao.approvedAER(ae.getTaskid());

        //ModelAndView mav = new ModelAndView("/report/AuthReportViewPage?taskId="+ae.getTaskid(), "command", ae);
        ModelAndView mav = new ModelAndView();
        mav.addObject("taskId", ae.getTaskid());
        return new ModelAndView("redirect:/authApprovedAER.htm?taskId=" + ae.getTaskid(), "command", ae);

        //return mav;
    }

    @RequestMapping(value = "approvedAERByAuth", params = {"btnAer=Revert"})
    public ModelAndView Revert(HttpServletRequest request, HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") AnnualEstablishment ae) {

        String serverfilePath = context.getInitParameter("AERPath");
        String fileName = "AER_" + lub.getLoginoffcode() + ".pdf";

        annuaiEstablishmentDao.revertAER(ae.getTaskid(), serverfilePath, fileName, ae.getRevertReason());

        ModelAndView mav = new ModelAndView();
        mav.addObject("taskId", ae.getTaskid());
        return new ModelAndView("redirect:/authApprovedAER.htm?taskId=" + ae.getTaskid(), "command", ae);
    }

    @RequestMapping(value = "scheduleIIReport")
    public String ScheduleIIReport(Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam Map<String, String> req) {

        List listdata = null;

        int teacherSanctionedStrengthPlanTotal = 0;
        int teacherSanctionedStrengthNonPlanTotal = 0;
        int teacherSanctionedStrengthTotalTotal = 0;

        int othersSanctionedStrengthPlanTotal = 0;
        int othersSanctionedStrengthNonPlanTotal = 0;
        int othersSanctionedStrengthTotalTotal = 0;

        int totalPlanTotal = 0;
        int totalNonPlanTotal = 0;
        int totalSanctionedStrengthTotal = 0;

        int teacherVacancyPositionPlanTotal = 0;
        int teacherVacancyPositionNonPlanTotal = 0;
        int othersVacancyPositionPlanTotal = 0;
        int othersVacancyPositionNonPlanTotal = 0;
        int totalTeacherVacancyPositionTotal = 0;
        int totalOthersVacancyPositionTotal = 0;

        try {
            int issingleCo = annuaiEstablishmentDao.getisSingleCo(Integer.parseInt(req.get("aerId")));
            listdata = annuaiEstablishmentDao.getScheduleIIData(Integer.parseInt(req.get("aerId")), lub.getLoginoffcode(), issingleCo);
            model.addAttribute("data", listdata);

            if (listdata != null && listdata.size() > 0) {
                ScheduleIIBean scbean = null;
                for (int i = 0; i < listdata.size(); i++) {
                    scbean = (ScheduleIIBean) listdata.get(i);
                    teacherSanctionedStrengthPlanTotal = teacherSanctionedStrengthPlanTotal + scbean.getTeacherSanctionedStrengthPlan();
                    teacherSanctionedStrengthNonPlanTotal = teacherSanctionedStrengthNonPlanTotal + scbean.getTeacherSanctionedStrengthNonPlan();
                    teacherSanctionedStrengthTotalTotal = teacherSanctionedStrengthTotalTotal + scbean.getTeacherSanctionedStrengthTotal();
                    othersSanctionedStrengthPlanTotal = othersSanctionedStrengthPlanTotal + scbean.getOthersSanctionedStrengthPlan();
                    othersSanctionedStrengthNonPlanTotal = othersSanctionedStrengthNonPlanTotal + scbean.getOthersSanctionedStrengthNonPlan();
                    othersSanctionedStrengthTotalTotal = othersSanctionedStrengthTotalTotal + scbean.getOthersSanctionedStrengthTotal();
                    totalPlanTotal = totalPlanTotal + scbean.getTotalPlan();
                    totalNonPlanTotal = totalNonPlanTotal + scbean.getTotalNonPlan();
                    totalSanctionedStrengthTotal = totalSanctionedStrengthTotal + scbean.getTotalSanctionedStrength();
                    teacherVacancyPositionPlanTotal = teacherVacancyPositionPlanTotal + scbean.getTeacherVacancyPositionPlan();
                    teacherVacancyPositionNonPlanTotal = teacherVacancyPositionNonPlanTotal + scbean.getTeacherVacancyPositionNonPlan();
                    othersVacancyPositionPlanTotal = othersVacancyPositionPlanTotal + scbean.getOthersVacancyPositionPlan();
                    othersVacancyPositionNonPlanTotal = othersVacancyPositionNonPlanTotal + scbean.getOthersVacancyPositionNonPlan();
                    totalTeacherVacancyPositionTotal = totalTeacherVacancyPositionTotal + scbean.getTotalTeacherVacancyPosition();
                    totalOthersVacancyPositionTotal = totalOthersVacancyPositionTotal + scbean.getTotalOthersVacancyPosition();
                }
                model.addAttribute("teacherSanctionedStrengthPlanTotal", teacherSanctionedStrengthPlanTotal);
                model.addAttribute("teacherSanctionedStrengthNonPlanTotal", teacherSanctionedStrengthNonPlanTotal);
                model.addAttribute("teacherSanctionedStrengthTotalTotal", teacherSanctionedStrengthTotalTotal);
                model.addAttribute("othersSanctionedStrengthPlanTotal", othersSanctionedStrengthPlanTotal);
                model.addAttribute("othersSanctionedStrengthNonPlanTotal", othersSanctionedStrengthNonPlanTotal);
                model.addAttribute("othersSanctionedStrengthTotalTotal", othersSanctionedStrengthTotalTotal);
                model.addAttribute("totalPlanTotal", totalPlanTotal);
                model.addAttribute("totalNonPlanTotal", totalNonPlanTotal);
                model.addAttribute("totalSanctionedStrengthTotal", totalSanctionedStrengthTotal);
                model.addAttribute("teacherVacancyPositionPlanTotal", teacherVacancyPositionPlanTotal);
                model.addAttribute("teacherVacancyPositionNonPlanTotal", teacherVacancyPositionNonPlanTotal);
                model.addAttribute("othersVacancyPositionPlanTotal", othersVacancyPositionPlanTotal);
                model.addAttribute("othersVacancyPositionNonPlanTotal", othersVacancyPositionNonPlanTotal);
                model.addAttribute("totalTeacherVacancyPositionTotal", totalTeacherVacancyPositionTotal);
                model.addAttribute("totalOthersVacancyPositionTotal", totalOthersVacancyPositionTotal);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "report/AERReportScheduleII";
    }

    @ResponseBody
    @RequestMapping(value = "getAERAuthortiyList")
    public void GetAERAuthortiyList(HttpServletResponse response, @RequestParam("offcode") String offCode) {

        response.setContentType("application/json");
        PrintWriter out = null;
        JSONArray json = null;

        try {
            List listdata = annuaiEstablishmentDao.getAERAuthorityList(offCode);
            json = new JSONArray(listdata);
            out = response.getWriter();
            out.write(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    @RequestMapping(value = "displayAERlist.htm")
    public ModelAndView displayAERlist(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") AnnualEstablishment ae) {
        ModelAndView mav = null;
        String roleType = "";
        mav = new ModelAndView("report/AnnualEstablishmentList", "command", ae);
        List<AnnualEstablishment> submitList = null;
        //int giaDataCount = annuaiEstablishmentDao.getGIACount(lub.getLoginoffcode());
        //mav.addObject("giaDataCount", giaDataCount);
        List fylist = annuaiEstablishmentDao.getFinancialYearList();
        mav.addObject("financialYearList", fylist);

        List fiscyear = fiscalDAO.getFiscalYearList();
        mav.addObject("fiscyear", fiscyear);

        String offcode = lub.getLoginoffcode();
        String spc = lub.getLoginspc();
        String officename = lub.getLoginoffname();

        if ((lub.getLoginAdditionalChargeOffCode() != null && !lub.getLoginAdditionalChargeOffCode().equals("")) && (lub.getLoginAdditionalChargeSpc() != null && !lub.getLoginAdditionalChargeSpc().equals(""))) {
            offcode = lub.getLoginAdditionalChargeOffCode();
            spc = lub.getLoginAdditionalChargeSpc();
            officename = lub.getLoginadditionalChargeOfficename();
        }

        String isOperator = annuaiEstablishmentDao.checkOperator(offcode, spc);
        mav.addObject("isOperator", isOperator);

        String isApprover = annuaiEstablishmentDao.checkApprover(offcode, spc);
        mav.addObject("isApprover", isApprover);

        if (isOperator.equalsIgnoreCase("Y")) {
            roleType = "OP";
        } else if (isApprover.equalsIgnoreCase("Y")) {
            roleType = "AP";
            ArrayList colist = annuaiEstablishmentDao.getDDOtoCOMappingList(spc);
            mav.addObject("colist", colist);
        }

        if (isOperator.equalsIgnoreCase("Y") && isApprover.equalsIgnoreCase("Y")) {
            roleType = "BO";
            ArrayList colist = annuaiEstablishmentDao.getDDOtoCOMappingList(spc);
            mav.addObject("colist", colist);
        }

        submitList = annuaiEstablishmentDao.getAerReportListFinancialYearWise(offcode, ae.getFinancialYear(), roleType, spc);
        mav.addObject("EstablishmentList", submitList);
        mav.addObject("OffName", officename);

        List deptlist = deptDAO.getDepartmentList();
        mav.addObject("deptlist", deptlist);

        return mav;

    }

    @RequestMapping(value = "createAER", params = {"btnAer=CreateAER"})
    public ModelAndView CreateAER(HttpServletRequest request, HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") AnnualEstablishment ae) {

        //annuaiEstablishmentDao.approvedAER(ae.getTaskid());
        ModelAndView mav = new ModelAndView();
        boolean disableSingleCO = false;
        mav = new ModelAndView("report/CreateAERData", "command", ae);

        String offcode = lub.getLoginoffcode();
        String officename = lub.getLoginoffname();

        if ((lub.getLoginAdditionalChargeOffCode() != null && !lub.getLoginAdditionalChargeOffCode().equals("")) && (lub.getLoginAdditionalChargeSpc() != null && !lub.getLoginAdditionalChargeSpc().equals(""))) {
            offcode = lub.getLoginAdditionalChargeOffCode();
            officename = lub.getLoginadditionalChargeOfficename();
        }

        boolean status = annuaiEstablishmentDao.duplicateCreateAER(offcode, ae.getFinancialYear());
        String coType = annuaiEstablishmentDao.getCoType(offcode, ae.getFinancialYear());

        if (coType != null && !coType.equals("") && coType.equalsIgnoreCase("N")) {
            disableSingleCO = true;
        }

        mav.addObject("status", status);
        mav.addObject("disableSingleCO", disableSingleCO);
        mav.addObject("OffName", officename);
        return mav;
    }

    @RequestMapping(value = "createAER", params = {"btnAer=Search"})
    public ModelAndView SearchAER(HttpServletRequest request, HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") AnnualEstablishment ae) {
        ModelAndView mav = new ModelAndView();
        String roleType = "";
        List<AnnualEstablishment> submitList = null;

        String offcode = lub.getLoginoffcode();
        String spc = lub.getLoginspc();
        String officename = lub.getLoginoffname();

        if ((lub.getLoginAdditionalChargeOffCode() != null && !lub.getLoginAdditionalChargeOffCode().equals("")) && (lub.getLoginAdditionalChargeSpc() != null && !lub.getLoginAdditionalChargeSpc().equals(""))) {
            offcode = lub.getLoginAdditionalChargeOffCode();
            spc = lub.getLoginAdditionalChargeSpc();
            officename = lub.getLoginadditionalChargeOfficename();
        }

        String isOperator = annuaiEstablishmentDao.checkOperator(offcode, spc);
        mav.addObject("isOperator", isOperator);

        String isApprover = annuaiEstablishmentDao.checkApprover(offcode, spc);
        mav.addObject("isApprover", isApprover);

        if (isOperator.equalsIgnoreCase("Y")) {
            roleType = "OP";

        } else if (isApprover.equalsIgnoreCase("Y")) {
            roleType = "AP";
        }
        submitList = annuaiEstablishmentDao.getAerReportListFinancialYearWise(offcode, ae.getFinancialYear(), roleType, spc);
        mav.addObject("EstablishmentList", submitList);
        List fylist = annuaiEstablishmentDao.getFinancialYearList();
        mav.addObject("financialYearList", fylist);
        mav.addObject("OffName", officename);
        return new ModelAndView("redirect:/displayAERlist.htm?financialYear=" + ae.getFinancialYear(), "command", ae);

    }

    @RequestMapping(value = "createAER", params = {"btnAer=Back"})
    public ModelAndView BacktoAERList(HttpServletRequest request, HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") AnnualEstablishment ae) {

        ModelAndView mav = null;

        String offcode = lub.getLoginoffcode();
        String officename = lub.getLoginoffname();

        if ((lub.getLoginAdditionalChargeOffCode() != null && !lub.getLoginAdditionalChargeOffCode().equals("")) && (lub.getLoginAdditionalChargeSpc() != null && !lub.getLoginAdditionalChargeSpc().equals(""))) {
            offcode = lub.getLoginAdditionalChargeOffCode();
            officename = lub.getLoginadditionalChargeOfficename();
        }
        mav = new ModelAndView("redirect:/displayAERlist.htm?financialYear=" + ae.getFinancialYear(), "command", ae);
        boolean status = annuaiEstablishmentDao.duplicateCreateAER(offcode, ae.getFinancialYear());
        mav.addObject("status", status);
        mav.addObject("OffName", officename);

        return mav;
    }

    @RequestMapping(value = "createAERPartB", params = {"btnAer=Back"})
    public ModelAndView BacktoAERListPartB(HttpServletRequest request, HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") AnnualEstablishment ae) {

        ModelAndView mav = null;
        String offcode = lub.getLoginoffcode();
        String officename = lub.getLoginoffname();

        if ((lub.getLoginAdditionalChargeOffCode() != null && !lub.getLoginAdditionalChargeOffCode().equals("")) && (lub.getLoginAdditionalChargeSpc() != null && !lub.getLoginAdditionalChargeSpc().equals(""))) {
            offcode = lub.getLoginAdditionalChargeOffCode();
            officename = lub.getLoginadditionalChargeOfficename();
        }
        mav = new ModelAndView("redirect:/displayAERlist.htm?financialYear=" + ae.getFinancialYear(), "command", ae);
        boolean status = annuaiEstablishmentDao.duplicateCreateAER(offcode, ae.getFinancialYear());
        mav.addObject("status", status);
        mav.addObject("OffName", officename);

        return mav;
    }

    @RequestMapping(value = "createAER", params = {"btnAer=Edit"})
    public ModelAndView editAERPage(HttpServletRequest request, HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") AnnualEstablishment ae, @RequestParam Map<String, String> req) {
        ModelAndView mav = null;
        List billGrpList = new ArrayList();
        ae = annuaiEstablishmentDao.geteditAerdata(Integer.parseInt(req.get("aerId")));

        String offcode = lub.getLoginoffcode();
        String officename = lub.getLoginoffname();

        if ((lub.getLoginAdditionalChargeOffCode() != null && !lub.getLoginAdditionalChargeOffCode().equals("")) && (lub.getLoginAdditionalChargeSpc() != null && !lub.getLoginAdditionalChargeSpc().equals(""))) {
            offcode = lub.getLoginAdditionalChargeOffCode();
            officename = lub.getLoginadditionalChargeOfficename();
        }

        if (ae.getSingleCO().equals("1")) {
            ae.setSingleCO("Y");
            mav = new ModelAndView("report/SingleCOAERCreate", "command", ae);
        } else {

            if (ae.getAerId() > 0) {
                billGrpList = officeWiseSanctionedStrengthDAO.getBillgroupListNotInEditNewCase(offcode, ae.getFinancialYear(), ae.getAerId() + "");
                List<BigDecimal> li = officeWiseSanctionedStrengthDAO.getSelectedBillGroup(ae.getAerId() + "");

                BigDecimal bg[] = new BigDecimal[li.size()];

                for (int i = 0; i < li.size(); i++) {
                    bg[i] = li.get(i);
                }
                ae.setBillGrpId(bg);
            } else {
                billGrpList = officeWiseSanctionedStrengthDAO.getBillgroupListNotInAddNewCase(offcode, ae.getFinancialYear());

            }
            ae.setSingleCO("N");
            mav = new ModelAndView("report/MultipleCOAERCreate", "command", ae);

        }

        boolean status = annuaiEstablishmentDao.duplicateCreateAER(offcode, ae.getFinancialYear());
        mav.addObject("status", status);
        mav.addObject("OffName", officename);
        mav.addObject("BillGroupList", billGrpList);

        return mav;
    }

    @RequestMapping(value = "createAER", params = {"btnAer=Update"})
    public String Update(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") AnnualEstablishment ae) {

        String path = "redirect://aerReportPartA.htm?aerId=" + ae.getAerId();

        int retVal = annuaiEstablishmentDao.updatePostInformation(ae.getOffCode(), ae.getGpc(), ae.getScaleofPay(), ae.getScaleofPay7th(), ae.getPostgrp(), ae.getHidPostGrp(), ae.getPaylevel(), ae.getGp());

        return path;
    }

    @RequestMapping(value = "createAERPartB", params = {"btnAer=Update"})
    public String createAERPartB(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") AnnualEstablishment ae) {

        String path = "redirect://aerReportPartB.htm?aerId=" + ae.getAerId();

        int retVal = annuaiEstablishmentDao.updatePostInformation(lub.getLoginoffcode(), ae.getGpc(), ae.getScaleofPay(), ae.getScaleofPay7th(), ae.getPostgrp(), ae.getHidPostGrp(), ae.getPaylevel(), ae.getGp());

        return path;
    }

    @RequestMapping(value = "createAERforSingleCO")
    public ModelAndView createAERforSingleCO(HttpServletRequest request, HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") AnnualEstablishment ae, @RequestParam Map<String, String> req) {

        ModelAndView mav = null;
        mav = new ModelAndView("report/MultipleCOAERCreate", "command", ae);

        List billGrpList = new ArrayList();

        String ddocode = lub.getLoginDDOCode();
        String officename = lub.getLoginoffname();

        if (lub.getLoginAdditionalChargeDDOCode() != null && !lub.getLoginAdditionalChargeDDOCode().equals("")) {
            mav.addObject("AdditionalChargeDDOCode", lub.getLoginAdditionalChargeDDOCode());
            ddocode = lub.getLoginAdditionalChargeDDOCode();
            officename = lub.getLoginadditionalChargeOfficename();
        }
        if (lub.getLoginAdditionalChargeDDOCode() != null && !lub.getLoginAdditionalChargeDDOCode().equals("")) {
            billGrpList = new ArrayList();
        } else {
            billGrpList = officeWiseSanctionedStrengthDAO.getBillgroupListNotInAddNewCase(ddocode, ae.getFinancialYear());
        }

        ae.setSingleCO(req.get("singleCO"));
        mav.addObject("OffName", officename);
        mav.addObject("BillGroupList", billGrpList);
        return mav;

    }

    @RequestMapping(value = "createAERforMultipleCO")
    public ModelAndView nextToCOSelectionPage(HttpServletRequest request, HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") AnnualEstablishment ae, @RequestParam Map<String, String> req) {

        ModelAndView mav = null;
        mav = new ModelAndView("report/MultipleCOAERCreate", "command", ae);

        List billGrpList = new ArrayList();

        String ddocode = lub.getLoginDDOCode();
        String officename = lub.getLoginoffname();

        if (lub.getLoginAdditionalChargeDDOCode() != null && !lub.getLoginAdditionalChargeDDOCode().equals("")) {
            ddocode = lub.getLoginAdditionalChargeDDOCode();
            officename = lub.getLoginadditionalChargeOfficename();
        }

        if (ae.getAerId() > 0) {

            billGrpList = officeWiseSanctionedStrengthDAO.getBillgroupListNotInEditNewCase(ddocode, ae.getFinancialYear(), ae.getAerId() + "");
            List<BigDecimal> li = officeWiseSanctionedStrengthDAO.getSelectedBillGroup(ae.getAerId() + "");

            BigDecimal bg[] = new BigDecimal[li.size()];

            for (int i = 0; i < li.size(); i++) {
                bg[i] = li.get(i);
            }

            ae.setBillGrpId(bg);
        } else {
            billGrpList = officeWiseSanctionedStrengthDAO.getBillgroupListNotInAddNewCase(ddocode, ae.getFinancialYear());
        }

        mav.addObject("OffName", officename);
        mav.addObject("BillGroupList", billGrpList);
        return mav;
    }

    @RequestMapping(value = "finishAER", params = {"action=Finish"})
    public ModelAndView finishAER(HttpServletRequest request, HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") AnnualEstablishment ae, @RequestParam Map<String, String> req) {

        String offcode = lub.getLoginoffcode();
        String ddocode = lub.getLoginDDOCode();
        String officename = lub.getLoginoffname();

        if ((lub.getLoginAdditionalChargeOffCode() != null && !lub.getLoginAdditionalChargeOffCode().equals("")) && (lub.getLoginAdditionalChargeSpc() != null && !lub.getLoginAdditionalChargeSpc().equals(""))) {
            offcode = lub.getLoginAdditionalChargeOffCode();
            officename = lub.getLoginadditionalChargeOfficename();
        }

        if (lub.getLoginAdditionalChargeDDOCode() != null && !lub.getLoginAdditionalChargeDDOCode().equals("")) {
            ddocode = lub.getLoginAdditionalChargeDDOCode();
        }

        annuaiEstablishmentDao.saveAERSanctionedPostData(offcode, ae, ddocode);

        ModelAndView mav = null;
        mav = new ModelAndView("redirect:/displayAERlist.htm?financialYear=" + ae.getFinancialYear(), "command", ae);

        mav.addObject("OffName", officename);

        return mav;
    }

    @RequestMapping(value = "finishAER", params = {"action=Back"})
    public ModelAndView backtoCOSelectionPage(HttpServletRequest request, HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") AnnualEstablishment ae, @RequestParam Map<String, String> req) {

        //ae.setSingleCO(req.get("singleCO")); 
        ModelAndView mav = null;

        String offcode = lub.getLoginoffcode();
        String officename = lub.getLoginoffname();

        if ((lub.getLoginAdditionalChargeOffCode() != null && !lub.getLoginAdditionalChargeOffCode().equals("")) && (lub.getLoginAdditionalChargeSpc() != null && !lub.getLoginAdditionalChargeSpc().equals(""))) {
            offcode = lub.getLoginAdditionalChargeOffCode();
            officename = lub.getLoginadditionalChargeOfficename();
        }

        mav = new ModelAndView("redirect:/createAER.htm?btnAer=CreateAER&financialYear=" + ae.getFinancialYear(), "command", ae);
        boolean status = annuaiEstablishmentDao.duplicateCreateAER(offcode, ae.getFinancialYear());
        mav.addObject("status", status);
        mav.addObject("OffName", officename);

        return mav;
    }

    @RequestMapping(value = "finishAER", params = {"action=Previous"})
    public ModelAndView backtoMultipleCO1stPage(HttpServletRequest request, HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") AnnualEstablishment ae, @RequestParam Map<String, String> req) {

        ModelAndView mav = null;

        String offcode = lub.getLoginoffcode();
        String officename = lub.getLoginoffname();

        if ((lub.getLoginAdditionalChargeOffCode() != null && !lub.getLoginAdditionalChargeOffCode().equals("")) && (lub.getLoginAdditionalChargeSpc() != null && !lub.getLoginAdditionalChargeSpc().equals(""))) {
            offcode = lub.getLoginAdditionalChargeOffCode();
            officename = lub.getLoginadditionalChargeOfficename();
        }

        mav = new ModelAndView("redirect:/createAER.htm?btnAer=CreateAER&financialYear=" + ae.getFinancialYear(), "command", ae);
        boolean status = annuaiEstablishmentDao.duplicateCreateAER(offcode, ae.getFinancialYear());
        mav.addObject("status", status);
        mav.addObject("OffName", officename);

        return mav;
    }

    @RequestMapping(value = "viewRevertReason")
    public String viewRevertReason(Model model, @RequestParam("aerId") String aerId) {

        try {
            String revertReason = annuaiEstablishmentDao.getAERRevertReason(aerId);
            model.addAttribute("revertReason", revertReason);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/report/AERRevertReason";
    }

    @RequestMapping(value = "viewVerifierRevertReason")
    public String viewVerifierRevertReason(Model model, @RequestParam("aerId") String aerId) {

        try {
            String revertReason = annuaiEstablishmentDao.getAERRevertReasonVerifier(aerId);
            model.addAttribute("revertReason", revertReason);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/report/AERRevertReason";
    }

    @RequestMapping(value = "ProformaGIAEmployeeReport")
    public String proformaGIAEmployeeReport(Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub) {

        try {
            List groupAEmployeeList = annuaiEstablishmentDao.getProformaGIAEmployeeData(lub.getLoginoffcode(), "A");
            List groupBEmployeeList = annuaiEstablishmentDao.getProformaGIAEmployeeData(lub.getLoginoffcode(), "B");
            List groupCEmployeeList = annuaiEstablishmentDao.getProformaGIAEmployeeData(lub.getLoginoffcode(), "C");
            List groupDEmployeeList = annuaiEstablishmentDao.getProformaGIAEmployeeData(lub.getLoginoffcode(), "D");

            model.addAttribute("groupAEmpList", groupAEmployeeList);
            model.addAttribute("groupBEmpList", groupBEmployeeList);
            model.addAttribute("groupCEmpList", groupCEmployeeList);
            model.addAttribute("groupDEmpList", groupDEmployeeList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/report/ProformaGIAEmployeeReport";
    }

    @RequestMapping(value = "displayAERworkflowrouting")
    public ModelAndView displayAERworkflowrouting(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub) {

        List li = postdao.getPostListForAERMapping(lub.getLogindeptcode());
        ModelAndView mav = new ModelAndView("/report/AERHierarchyList");
        mav.addObject("PostList", li);

        return mav;
    }

    @RequestMapping(value = "AdminToCOMapping")
    public ModelAndView AdminToCOMapping(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("postcode") String postcode) {

        ModelAndView mav = null;
        try {
            WorkflowRouting wr = new WorkflowRouting();
            wr.setGpc(postcode);
            wr.setSltDept(lub.getLogindeptcode());

            mav = new ModelAndView("/report/AdminToCOMapping", "command", wr);

            String postname = postdao.getPostName(wr.getGpc());

            List deptlist = deptDAO.getDepartmentList();
            Map<String, String> dept = new HashMap<String, String>();
            Iterator<Department> itr2 = deptlist.iterator();
            Department dpt = null;
            while (itr2.hasNext()) {
                dpt = itr2.next();
                dept.put(dpt.getDeptCode(), dpt.getDeptName());
            }
            Map<String, String> sortedDept = new TreeMap<String, String>(new ValueComparator(dept));
            sortedDept.putAll(dept);

            mav.addObject("deptList", sortedDept);

            SelectOption so = new SelectOption();
            List offlist = officeDao.getOfficeListFilter(lub.getLoginoffcode());
            Map<String, String> offmap = new HashMap<String, String>();
            Iterator<SelectOption> itr3 = offlist.iterator();
            while (itr3.hasNext()) {
                so = itr3.next();
                offmap.put(so.getValue(), so.getLabel());
            }

            mav.addObject("offList", offmap);

            mav.addObject("postname", postname);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "assignAERWorkflowrouting")
    public ModelAndView assignAERWorkflowrouting(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") WorkflowRouting wr, @RequestParam Map<String, String> parameters) {

        ModelAndView mav = null;

        String postname = postdao.getPostName(wr.getGpc());
        List deptlist = deptDAO.getDepartmentList();
        Map<String, String> dept = new HashMap<String, String>();
        Iterator<Department> itr2 = deptlist.iterator();
        Department dpt = new Department();
        while (itr2.hasNext()) {
            dpt = itr2.next();
            dept.put(dpt.getDeptCode(), dpt.getDeptName());
        }

        String btn = parameters.get("btnval");

        if ((btn == null || btn.equalsIgnoreCase("")) || btn.equalsIgnoreCase("GetList")) {
            mav = new ModelAndView("/report/AdminToCOMapping", "command", wr);

            SelectOption so = new SelectOption();
            List offlist = officeDao.getOfficeListFilter(wr.getSltOffcode());
            Map<String, String> offmap = new HashMap<String, String>();
            Iterator<SelectOption> itr3 = offlist.iterator();
            while (itr3.hasNext()) {
                so = itr3.next();
                offmap.put(so.getValue(), so.getLabel());
            }

            mav.addObject("offList", offmap);

            wr.setProcessId("13");

            List postlist = annuaiEstablishmentDao.getPostListAuthorityWise(wr.getSltOffcode(), wr.getGpc(), wr.getProcessId());
            mav.addObject("proposeList", postlist);

            List assignedlist = annuaiEstablishmentDao.getmappedPostList(wr.getGpc(), wr.getProcessId());
            mav.addObject("assignedList", assignedlist);
        } else {
            mav = new ModelAndView("redirect:displayAERworkflowrouting.htm", "command", wr);
        }
        mav.addObject("deptList", dept);
        mav.addObject("postname", postname);
        return mav;
    }

    @RequestMapping(value = "addAERWorkflowrouting")
    public ModelAndView addAERWorkflowrouting(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") WorkflowRouting wr, @RequestParam Map<String, String> parameters) {

        String postcode = parameters.get("postcode");
        String assignedPostcode = parameters.get("assignedPostcode");
        String processId = parameters.get("processId");
        String offcode = parameters.get("offCode");
        String departmentcode = parameters.get("departmentcode");
        wr.setProcessId("13");
        wr.setOfficeCode(offcode);
        wr.setSltDept(departmentcode);
        wr.setSltOffcode(offcode);
        wr.setGpc(postcode);
        wr.setReportingGpc(assignedPostcode);
        wr.setLoginOffcode(lub.getLoginoffcode());
        annuaiEstablishmentDao.addHierarchy(wr);

        ModelAndView mav = new ModelAndView("redirect:assignAERWorkflowrouting.htm", "command", wr);
        mav.addObject("sltDept", departmentcode);
        mav.addObject("sltOffcode", offcode);
        mav.addObject("processId", processId);
        mav.addObject("gpc", postcode);
        return mav;
    }

    @RequestMapping(value = "removeAERWorkflowrouting")
    public ModelAndView removeAERWorkflowrouting(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") WorkflowRouting wr, @RequestParam Map<String, String> parameters) {

        String workflowRoutingId = parameters.get("workflowRoutingId");
        wr.setWorkflowRoutingId(Integer.parseInt(workflowRoutingId));
        String postcode = parameters.get("postcode");
        String processId = parameters.get("processId");
        String offcode = parameters.get("offCode");
        String departmentcode = parameters.get("departmentcode");
        wr.setProcessId("13");
        wr.setOfficeCode(offcode);
        wr.setSltDept(departmentcode);
        wr.setSltOffcode(offcode);
        wr.setGpc(postcode);

        annuaiEstablishmentDao.removeHierarchy(wr);

        ModelAndView mav = new ModelAndView("redirect:assignAERWorkflowrouting.htm", "command", wr);

        mav.addObject("sltDept", departmentcode);
        mav.addObject("sltOffcode", offcode);
        mav.addObject("processId", processId);
        mav.addObject("gpc", postcode);
        return mav;
    }

    @RequestMapping(value = "addOtherEstablishment.htm")
    public ModelAndView addOtherEstablishment(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") AnnualEstablishment ae, @RequestParam Map<String, String> parameters) {
        ModelAndView mav = null;
        mav = new ModelAndView("report/addOtherEstablishment", "command", ae);

        String officename = lub.getLoginoffname();
        if (lub.getLoginadditionalChargeOfficename() != null && !lub.getLoginadditionalChargeOfficename().equals("")) {
            officename = lub.getLoginadditionalChargeOfficename();
        }

        mav.addObject("OffName", officename);

        String fyear = parameters.get("fy");

        if (parameters.get("aerId") != null && !parameters.get("aerId").equals("")) {
            ae.setAerId(Integer.parseInt(parameters.get("aerId")));
        } else {
            ae.setAerId(0);
        }

        mav.addObject("financialYear", fyear);
        String partType = "C";
        AnnualEstablishment[] aeDetails = annuaiEstablishmentDao.getOtherEst(ae.getAerId(), partType);
        ae.setFinancialYear(fyear);
        mav.addObject("aeDetails", aeDetails);
        // mav.addObject("partType", "C");
        return mav;

    }

    @RequestMapping(value = "createOtherEst.htm")
    public ModelAndView createOtherEst(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") AnnualEstablishment ae) {
        ModelAndView mav = null;
        String partTypeVal = ae.getPartType();
        if (partTypeVal.equals("C")) {
            mav = new ModelAndView("redirect:/addOtherEstablishment.htm?fy=" + ae.getFinancialYear() + "&aerId=" + ae.getAerId());
        }
        if (partTypeVal.equals("D")) {
            mav = new ModelAndView("redirect:/addPartDOtherEstablishment.htm?fy=" + ae.getFinancialYear() + "&aerId=" + ae.getAerId());
        }
        if (partTypeVal.equals("E")) {
            mav = new ModelAndView("redirect:/addPartEOutsourced.htm?fy=" + ae.getFinancialYear() + "&aerId=" + ae.getAerId());
        }
        annuaiEstablishmentDao.createOtherEst(ae);

        return mav;
    }

    @RequestMapping(value = "deleteOtherEst.htm")
    public ModelAndView deleteOtherEst(@ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("aerOtherId") int aerOtherId, @RequestParam("aerId") int aerId, @RequestParam("partType") String partType) {
        ModelAndView mv = null;

        // mv = new ModelAndView("redirect:/addOtherEstablishment.htm?partType=" + partType + "&aerId=" + aerId);
        if (partType.equals("C")) {
            mv = new ModelAndView("redirect:/addOtherEstablishment.htm?partType=" + partType + "&aerId=" + aerId);
        }
        if (partType.equals("D")) {
            mv = new ModelAndView("redirect:/addPartDOtherEstablishment.htm?partType=" + partType + "&aerId=" + aerId);
        }
        if (partType.equals("E")) {
            mv = new ModelAndView("redirect:/addPartEOutsourced.htm?partType=" + partType + "&aerId=" + aerId);
        }

        annuaiEstablishmentDao.deleteOtherEst(aerOtherId);
        return mv;
    }

    @RequestMapping(value = "editOtherEst.htm")
    public ModelAndView editOtherEst(@ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("aerOtherId") int aerOtherId, @RequestParam("aerId") int aerId, @RequestParam("partType") String partType, @ModelAttribute("command") AnnualEstablishment ae, @RequestParam Map<String, String> parameters) {
        ModelAndView mav = null;
        mav = new ModelAndView("report/editOtherEstablishment");
        String officename = lub.getLoginoffname();
        if (lub.getLoginadditionalChargeOfficename() != null && !lub.getLoginadditionalChargeOfficename().equals("")) {
            officename = lub.getLoginadditionalChargeOfficename();
        }
        mav.addObject("OffName", officename);
        String fyear = parameters.get("fy");
        if (ae.getAerId() == 0) {
            ae.setAerId(Integer.parseInt(parameters.get("aerId")));
        }
        mav.addObject("financialYear", fyear);
        // AnnualEstablishment[] aeDetails = annuaiEstablishmentDao.getOtherEst(ae.getAerId(),partType);
        ae.setFinancialYear(fyear);
        //  mav.addObject("aeDetails", aeDetails);
        ae = annuaiEstablishmentDao.geOtherEstDetals(aerOtherId);
        mav.addObject("partType", partType);
        mav.addObject("command", ae);

        return mav;
    }

    @RequestMapping(value = "saveOtherEst.htm")
    public ModelAndView saveOtherEst(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") AnnualEstablishment ae) {
        ModelAndView mav = null;
        String partTypeVal = ae.getPartType();
        // mav = new ModelAndView("redirect:/addOtherEstablishment.htm?fy=" + ae.getFinancialYear() + "&aerId=" + ae.getAerId());
        if (partTypeVal.equals("C")) {
            mav = new ModelAndView("redirect:/addOtherEstablishment.htm?fy=" + ae.getFinancialYear() + "&aerId=" + ae.getAerId());
        }
        if (partTypeVal.equals("D")) {
            mav = new ModelAndView("redirect:/addPartDOtherEstablishment.htm?fy=" + ae.getFinancialYear() + "&aerId=" + ae.getAerId());
        }
        if (partTypeVal.equals("E")) {
            mav = new ModelAndView("redirect:/addPartEOutsourced.htm?fy=" + ae.getFinancialYear() + "&aerId=" + ae.getAerId());
        }

        annuaiEstablishmentDao.saveOtherEst(ae);
        return mav;
    }

    @RequestMapping(value = "aerReportPartA")
    public ModelAndView aerReportPartA(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") AnnualEstablishment ae, @RequestParam Map<String, String> param) {

        List grpAList = new ArrayList();
        List partAGrpAAllIndiaService = new ArrayList();
        List partAGrpAUGC = new ArrayList();
        List partAJudiciary = new ArrayList();
        List grpBList = new ArrayList();
        List grpCList = new ArrayList();
        List grpDList = new ArrayList();

        List partBGrpAAllIndiaService = new ArrayList();
        List partBGrpAUGC = new ArrayList();
        List partBJudiciary = new ArrayList();
        List partBGrpAList = new ArrayList();
        List partBGrpBList = new ArrayList();
        List partBGrpCList = new ArrayList();
        List partBGrpDList = new ArrayList();

        List grpPartCList = new ArrayList();
        String hideEditLink = "";
        ModelAndView mv = new ModelAndView();
        String hideUrl = "";

        int partATotalGrpASanctionedStrength = 0;
        int partATotalGrpAMenInPosition = 0;
        int partATotalGrpAVacancy = 0;

        int partATotalGrpBSanctionedStrength = 0;
        int partATotalGrpBMenInPosition = 0;
        int partATotalGrpBVacancy = 0;

        int partATotalGrpCSanctionedStrength = 0;
        int partATotalGrpCMenInPosition = 0;
        int partATotalGrpCVacancy = 0;

        int partATotalGrpDSanctionedStrength = 0;
        int partATotalGrpDMenInPosition = 0;
        int partATotalGrpDVacancy = 0;

        int partBTotalGrpASanctionedStrength = 0;
        int partBTotalGrpAMenInPosition = 0;
        int partBTotalGrpAVacancy = 0;

        int partBTotalGrpBSanctionedStrength = 0;
        int partBTotalGrpBMenInPosition = 0;
        int partBTotalGrpBVacancy = 0;

        int partBTotalGrpCSanctionedStrength = 0;
        int partBTotalGrpCMenInPosition = 0;
        int partBTotalGrpCVacancy = 0;

        int partBTotalGrpDSanctionedStrength = 0;
        int partBTotalGrpDMenInPosition = 0;
        int partBTotalGrpDVacancy = 0;

        int giaTotalSanctionedStrength = 0;
        int giaTotalMenInPosition = 0;
        int giaTotalVacancy = 0;
        try {

            String offcode = lub.getLoginoffcode();
            String officename = lub.getLoginoffname();

            if ((lub.getLoginAdditionalChargeOffCode() != null && !lub.getLoginAdditionalChargeOffCode().equals("")) && (lub.getLoginAdditionalChargeSpc() != null && !lub.getLoginAdditionalChargeSpc().equals(""))) {
                offcode = lub.getLoginAdditionalChargeOffCode();
                officename = lub.getLoginadditionalChargeOfficename();
            }

            partAGrpAAllIndiaService = annuaiEstablishmentDao.getGroupAlistBeforeSubmission("PART-A", offcode, "A", Integer.parseInt(param.get("aerId")), "AIS");
            partAGrpAUGC = annuaiEstablishmentDao.getGroupAlistBeforeSubmission("PART-A", offcode, "A", Integer.parseInt(param.get("aerId")), "UGC");
            partAJudiciary = annuaiEstablishmentDao.getGroupAlistBeforeSubmission("PART-A", offcode, "A", Integer.parseInt(param.get("aerId")), "OJS");
            grpAList = annuaiEstablishmentDao.getGroupAlistBeforeSubmission("PART-A", offcode, "A", Integer.parseInt(param.get("aerId")), "");

            if (partAGrpAAllIndiaService != null && partAGrpAAllIndiaService.size() > 0) {
                AnnualEstablishment aer = null;
                for (int i = 0; i < partAGrpAAllIndiaService.size(); i++) {
                    aer = (AnnualEstablishment) partAGrpAAllIndiaService.get(i);
                    partATotalGrpASanctionedStrength = partATotalGrpASanctionedStrength + aer.getSanctionedStrength();
                    partATotalGrpAMenInPosition = partATotalGrpAMenInPosition + aer.getMeninPosition();
                    partATotalGrpAVacancy = partATotalGrpAVacancy + aer.getVacancyPosition();
                }
            }
            if (partAGrpAUGC != null && partAGrpAUGC.size() > 0) {
                AnnualEstablishment aer = null;
                for (int i = 0; i < partAGrpAUGC.size(); i++) {
                    aer = (AnnualEstablishment) partAGrpAUGC.get(i);
                    partATotalGrpASanctionedStrength = partATotalGrpASanctionedStrength + aer.getSanctionedStrength();
                    partATotalGrpAMenInPosition = partATotalGrpAMenInPosition + aer.getMeninPosition();
                    partATotalGrpAVacancy = partATotalGrpAVacancy + aer.getVacancyPosition();
                }
            }
            if (partAJudiciary != null && partAJudiciary.size() > 0) {
                AnnualEstablishment aer = null;
                for (int i = 0; i < partAJudiciary.size(); i++) {
                    aer = (AnnualEstablishment) partAJudiciary.get(i);
                    partATotalGrpASanctionedStrength = partATotalGrpASanctionedStrength + aer.getSanctionedStrength();
                    partATotalGrpAMenInPosition = partATotalGrpAMenInPosition + aer.getMeninPosition();
                    partATotalGrpAVacancy = partATotalGrpAVacancy + aer.getVacancyPosition();
                }
            }
            if (grpAList != null && grpAList.size() > 0) {
                AnnualEstablishment aer = null;
                for (int i = 0; i < grpAList.size(); i++) {
                    aer = (AnnualEstablishment) grpAList.get(i);
                    partATotalGrpASanctionedStrength = partATotalGrpASanctionedStrength + aer.getSanctionedStrength();
                    partATotalGrpAMenInPosition = partATotalGrpAMenInPosition + aer.getMeninPosition();
                    partATotalGrpAVacancy = partATotalGrpAVacancy + aer.getVacancyPosition();
                }
            }
            mv.addObject("partATotalGrpASanctionedStrength", partATotalGrpASanctionedStrength);
            mv.addObject("partATotalGrpAMenInPosition", partATotalGrpAMenInPosition);
            mv.addObject("partATotalGrpAVacancy", partATotalGrpAVacancy);

            grpBList = annuaiEstablishmentDao.getGroupAlistBeforeSubmission("PART-A", offcode, "B", Integer.parseInt(param.get("aerId")), "");

            if (grpBList != null && grpBList.size() > 0) {
                AnnualEstablishment aer = null;
                for (int i = 0; i < grpBList.size(); i++) {
                    aer = (AnnualEstablishment) grpBList.get(i);
                    partATotalGrpBSanctionedStrength = partATotalGrpBSanctionedStrength + aer.getSanctionedStrength();
                    partATotalGrpBMenInPosition = partATotalGrpBMenInPosition + aer.getMeninPosition();
                    partATotalGrpBVacancy = partATotalGrpBVacancy + aer.getVacancyPosition();
                }
            }
            mv.addObject("partATotalGrpBSanctionedStrength", partATotalGrpBSanctionedStrength);
            mv.addObject("partATotalGrpBMenInPosition", partATotalGrpBMenInPosition);
            mv.addObject("partATotalGrpBVacancy", partATotalGrpBVacancy);

            grpCList = annuaiEstablishmentDao.getGroupAlistBeforeSubmission("PART-A", offcode, "C", Integer.parseInt(param.get("aerId")), "");

            if (grpCList != null && grpCList.size() > 0) {
                AnnualEstablishment aer = null;
                for (int i = 0; i < grpCList.size(); i++) {
                    aer = (AnnualEstablishment) grpCList.get(i);
                    partATotalGrpCSanctionedStrength = partATotalGrpCSanctionedStrength + aer.getSanctionedStrength();
                    partATotalGrpCMenInPosition = partATotalGrpCMenInPosition + aer.getMeninPosition();
                    partATotalGrpCVacancy = partATotalGrpCVacancy + aer.getVacancyPosition();
                }
            }
            mv.addObject("partATotalGrpCSanctionedStrength", partATotalGrpCSanctionedStrength);
            mv.addObject("partATotalGrpCMenInPosition", partATotalGrpCMenInPosition);
            mv.addObject("partATotalGrpCVacancy", partATotalGrpCVacancy);

            grpDList = annuaiEstablishmentDao.getGroupAlistBeforeSubmission("PART-A", offcode, "D", Integer.parseInt(param.get("aerId")), "");

            if (grpDList != null && grpDList.size() > 0) {
                AnnualEstablishment aer = null;
                for (int i = 0; i < grpDList.size(); i++) {
                    aer = (AnnualEstablishment) grpDList.get(i);
                    partATotalGrpDSanctionedStrength = partATotalGrpDSanctionedStrength + aer.getSanctionedStrength();
                    partATotalGrpDMenInPosition = partATotalGrpDMenInPosition + aer.getMeninPosition();
                    partATotalGrpDVacancy = partATotalGrpDVacancy + aer.getVacancyPosition();
                }
            }
            mv.addObject("partATotalGrpDSanctionedStrength", partATotalGrpDSanctionedStrength);
            mv.addObject("partATotalGrpDMenInPosition", partATotalGrpDMenInPosition);
            mv.addObject("partATotalGrpDVacancy", partATotalGrpDVacancy);

            partBGrpAAllIndiaService = annuaiEstablishmentDao.getGroupAlistBeforeSubmission("PART-B", offcode, "A", Integer.parseInt(param.get("aerId")), "AIS");
            partBGrpAUGC = annuaiEstablishmentDao.getGroupAlistBeforeSubmission("PART-B", offcode, "A", Integer.parseInt(param.get("aerId")), "UGC");
            partBJudiciary = annuaiEstablishmentDao.getGroupAlistBeforeSubmission("PART-B", offcode, "A", Integer.parseInt(param.get("aerId")), "OJS");
            partBGrpAList = annuaiEstablishmentDao.getGroupAlistBeforeSubmission("PART-B", offcode, "A", Integer.parseInt(param.get("aerId")), "");

            if (partBGrpAAllIndiaService != null && partBGrpAAllIndiaService.size() > 0) {
                AnnualEstablishment aer = null;
                for (int i = 0; i < partBGrpAAllIndiaService.size(); i++) {
                    aer = (AnnualEstablishment) partBGrpAAllIndiaService.get(i);
                    partBTotalGrpASanctionedStrength = partBTotalGrpASanctionedStrength + aer.getSanctionedStrength();
                    partBTotalGrpAMenInPosition = partBTotalGrpAMenInPosition + aer.getMeninPosition();
                    partBTotalGrpAVacancy = partBTotalGrpAVacancy + aer.getVacancyPosition();
                }
            }
            if (partBGrpAUGC != null && partBGrpAUGC.size() > 0) {
                AnnualEstablishment aer = null;
                for (int i = 0; i < partBGrpAUGC.size(); i++) {
                    aer = (AnnualEstablishment) partBGrpAUGC.get(i);
                    partBTotalGrpASanctionedStrength = partBTotalGrpASanctionedStrength + aer.getSanctionedStrength();
                    partBTotalGrpAMenInPosition = partBTotalGrpAMenInPosition + aer.getMeninPosition();
                    partBTotalGrpAVacancy = partBTotalGrpAVacancy + aer.getVacancyPosition();
                }
            }
            if (partBJudiciary != null && partBJudiciary.size() > 0) {
                AnnualEstablishment aer = null;
                for (int i = 0; i < partBJudiciary.size(); i++) {
                    aer = (AnnualEstablishment) partBJudiciary.get(i);
                    partBTotalGrpASanctionedStrength = partBTotalGrpASanctionedStrength + aer.getSanctionedStrength();
                    partBTotalGrpAMenInPosition = partBTotalGrpAMenInPosition + aer.getMeninPosition();
                    partBTotalGrpAVacancy = partBTotalGrpAVacancy + aer.getVacancyPosition();
                }
            }
            if (partBGrpAList != null && partBGrpAList.size() > 0) {
                AnnualEstablishment aer = null;
                for (int i = 0; i < partBGrpAList.size(); i++) {
                    aer = (AnnualEstablishment) partBGrpAList.get(i);
                    partBTotalGrpASanctionedStrength = partBTotalGrpASanctionedStrength + aer.getSanctionedStrength();
                    partBTotalGrpAMenInPosition = partBTotalGrpAMenInPosition + aer.getMeninPosition();
                    partBTotalGrpAVacancy = partBTotalGrpAVacancy + aer.getVacancyPosition();
                }
            }
            mv.addObject("partBTotalGrpASanctionedStrength", partBTotalGrpASanctionedStrength);
            mv.addObject("partBTotalGrpAMenInPosition", partBTotalGrpAMenInPosition);
            mv.addObject("partBTotalGrpAVacancy", partBTotalGrpAVacancy);

            partBGrpBList = annuaiEstablishmentDao.getGroupAlistBeforeSubmission("PART-B", offcode, "B", Integer.parseInt(param.get("aerId")), "");

            if (partBGrpBList != null && partBGrpBList.size() > 0) {
                AnnualEstablishment aer = null;
                for (int i = 0; i < partBGrpBList.size(); i++) {
                    aer = (AnnualEstablishment) partBGrpBList.get(i);
                    partBTotalGrpBSanctionedStrength = partBTotalGrpBSanctionedStrength + aer.getSanctionedStrength();
                    partBTotalGrpBMenInPosition = partBTotalGrpBMenInPosition + aer.getMeninPosition();
                    partBTotalGrpBVacancy = partBTotalGrpBVacancy + aer.getVacancyPosition();
                }
            }
            mv.addObject("partBTotalGrpBSanctionedStrength", partBTotalGrpBSanctionedStrength);
            mv.addObject("partBTotalGrpBMenInPosition", partBTotalGrpBMenInPosition);
            mv.addObject("partBTotalGrpBVacancy", partBTotalGrpBVacancy);

            partBGrpCList = annuaiEstablishmentDao.getGroupAlistBeforeSubmission("PART-B", offcode, "C", Integer.parseInt(param.get("aerId")), "");

            if (partBGrpCList != null && partBGrpCList.size() > 0) {
                AnnualEstablishment aer = null;
                for (int i = 0; i < partBGrpCList.size(); i++) {
                    aer = (AnnualEstablishment) partBGrpCList.get(i);
                    partBTotalGrpCSanctionedStrength = partBTotalGrpCSanctionedStrength + aer.getSanctionedStrength();
                    partBTotalGrpCMenInPosition = partBTotalGrpCMenInPosition + aer.getMeninPosition();
                    partBTotalGrpCVacancy = partBTotalGrpCVacancy + aer.getVacancyPosition();
                }
            }
            mv.addObject("partBTotalGrpCSanctionedStrength", partBTotalGrpCSanctionedStrength);
            mv.addObject("partBTotalGrpCMenInPosition", partBTotalGrpCMenInPosition);
            mv.addObject("partBTotalGrpCVacancy", partBTotalGrpCVacancy);

            partBGrpDList = annuaiEstablishmentDao.getGroupAlistBeforeSubmission("PART-B", offcode, "D", Integer.parseInt(param.get("aerId")), "");

            if (partBGrpDList != null && partBGrpDList.size() > 0) {
                AnnualEstablishment aer = null;
                for (int i = 0; i < partBGrpDList.size(); i++) {
                    aer = (AnnualEstablishment) partBGrpDList.get(i);
                    partBTotalGrpDSanctionedStrength = partBTotalGrpDSanctionedStrength + aer.getSanctionedStrength();
                    partBTotalGrpDMenInPosition = partBTotalGrpDMenInPosition + aer.getMeninPosition();
                    partBTotalGrpDVacancy = partBTotalGrpDVacancy + aer.getVacancyPosition();
                }
            }
            mv.addObject("partBTotalGrpDSanctionedStrength", partBTotalGrpDSanctionedStrength);
            mv.addObject("partBTotalGrpDMenInPosition", partBTotalGrpDMenInPosition);
            mv.addObject("partBTotalGrpDVacancy", partBTotalGrpDVacancy);

            grpPartCList = annuaiEstablishmentDao.getGroupAlistBeforeSubmission("PART-C", offcode, "C", Integer.parseInt(param.get("aerId")), "");

            boolean status = annuaiEstablishmentDao.privewDataInserted(Integer.parseInt(param.get("aerId")));
            if (status) {
                hideEditLink = "Y";
            } else {
                hideEditLink = "N";
            }

            boolean hidelink = annuaiEstablishmentDao.getAerSubmittedStatus(Integer.parseInt(param.get("aerId")));

            if (hidelink == false) {
                hideUrl = "Y";
            } else {
                hideUrl = "N";
            }

            mv.addObject("PartAGroupAlist", grpAList);
            mv.addObject("partAGrpAAllIndiaService", partAGrpAAllIndiaService);
            mv.addObject("partAGrpAUGC", partAGrpAUGC);
            mv.addObject("partAJudiciary", partAJudiciary);
            mv.addObject("PartAGroupBlist", grpBList);
            mv.addObject("PartAGroupClist", grpCList);
            mv.addObject("PartAGroupDlist", grpDList);

            mv.addObject("partBGrpAAllIndiaService", partBGrpAAllIndiaService);
            mv.addObject("partBGrpAUGC", partBGrpAUGC);
            mv.addObject("partBJudiciary", partBJudiciary);
            mv.addObject("partBGrpAList", partBGrpAList);
            mv.addObject("partBGrpBList", partBGrpBList);
            mv.addObject("partBGrpCList", partBGrpCList);
            mv.addObject("partBGrpDList", partBGrpDList);

            mv.addObject("PartCGrouplist", grpPartCList);

            mv.addObject("hideEditLink", hideEditLink);
            mv.addObject("Editable", hideUrl);

            List payscaleList = payscaleDAO.getPayScaleList();
            mv.addObject("payscaleList", payscaleList);
            mv.addObject("OffName", officename);

            if (hideEditLink.equals("Y")) {

                AnnualEstablishment[] partDdetails = annuaiEstablishmentDao.getOtherEst(ae.getAerId(), "D");
                mv.addObject("aeDdetails", partDdetails);

                AnnualEstablishment[] partEdetails = annuaiEstablishmentDao.getOtherEst(ae.getAerId(), "E");
                mv.addObject("aeEdetails", partEdetails);

                List giaList = annuaiEstablishmentDao.getAllGrantinAidOfficesByAerId(ae.getAerId());
                mv.addObject("giaList", giaList);

                //List giaArrList = Arrays.asList(giaList);
                if (giaList != null && giaList.size() > 0) {
                    AnnualEstablishment aer = null;
                    for (int i = 0; i < giaList.size(); i++) {
                        aer = (AnnualEstablishment) giaList.get(i);
                        giaTotalSanctionedStrength = giaTotalSanctionedStrength + aer.getOtherSS();
                        giaTotalMenInPosition = giaTotalMenInPosition + aer.getMeninPosition();
                        giaTotalVacancy = giaTotalVacancy + aer.getOtherVacancy();
                    }
                }

                mv.addObject("giaTotalSanctionedStrength", giaTotalSanctionedStrength);
                mv.addObject("giaTotalMenInPosition", giaTotalMenInPosition);
                mv.addObject("giaTotalVacancy", giaTotalVacancy);

                mv.setViewName("/report/PreviewSubmitAerReport");
            } else {
                mv.setViewName("/report/ManageSanctionPost");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return mv;
    }

    @RequestMapping(value = "aerReportPartB")
    public ModelAndView aerReportPartB(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") AnnualEstablishment ae, @RequestParam Map<String, String> param) {
        List grpAList = new ArrayList();
        List partAGrpAAllIndiaService = new ArrayList();
        List partAGrpAUGC = new ArrayList();
        List partAJudiciary = new ArrayList();
        List grpBList = new ArrayList();
        List grpCList = new ArrayList();
        List grpDList = new ArrayList();
        String hideEditLink = "";
        ModelAndView mv = new ModelAndView();
        String hideUrl = "";

        int partBTotalGrpASanctionedStrength = 0;
        int partBTotalGrpAMenInPosition = 0;
        int partBTotalGrpAVacancy = 0;

        int partBTotalGrpBSanctionedStrength = 0;
        int partBTotalGrpBMenInPosition = 0;
        int partBTotalGrpBVacancy = 0;

        int partBTotalGrpCSanctionedStrength = 0;
        int partBTotalGrpCMenInPosition = 0;
        int partBTotalGrpCVacancy = 0;

        int partBTotalGrpDSanctionedStrength = 0;
        int partBTotalGrpDMenInPosition = 0;
        int partBTotalGrpDVacancy = 0;

        int giaTotalSanctionedStrength = 0;
        int giaTotalMenInPosition = 0;
        int giaTotalVacancy = 0;

        try {

            String offcode = lub.getLoginoffcode();
            String officename = lub.getLoginoffname();

            if ((lub.getLoginAdditionalChargeOffCode() != null && !lub.getLoginAdditionalChargeOffCode().equals("")) && (lub.getLoginAdditionalChargeSpc() != null && !lub.getLoginAdditionalChargeSpc().equals(""))) {
                offcode = lub.getLoginAdditionalChargeOffCode();

                officename = lub.getLoginadditionalChargeOfficename();
            }

            partAGrpAAllIndiaService = annuaiEstablishmentDao.getGroupAlistBeforeSubmission("PART-B", offcode, "A", Integer.parseInt(param.get("aerId")), "AIS");
            partAGrpAUGC = annuaiEstablishmentDao.getGroupAlistBeforeSubmission("PART-B", offcode, "A", Integer.parseInt(param.get("aerId")), "UGC");
            partAJudiciary = annuaiEstablishmentDao.getGroupAlistBeforeSubmission("PART-B", offcode, "A", Integer.parseInt(param.get("aerId")), "OJS");
            grpAList = annuaiEstablishmentDao.getGroupAlistBeforeSubmission("PART-B", offcode, "A", Integer.parseInt(param.get("aerId")), "");

            if (partAGrpAAllIndiaService != null && partAGrpAAllIndiaService.size() > 0) {
                AnnualEstablishment aer = null;
                for (int i = 0; i < partAGrpAAllIndiaService.size(); i++) {
                    aer = (AnnualEstablishment) partAGrpAAllIndiaService.get(i);
                    partBTotalGrpASanctionedStrength = partBTotalGrpASanctionedStrength + aer.getSanctionedStrength();
                    partBTotalGrpAMenInPosition = partBTotalGrpAMenInPosition + aer.getMeninPosition();
                    partBTotalGrpAVacancy = partBTotalGrpAVacancy + aer.getVacancyPosition();
                }
            }
            if (partAGrpAUGC != null && partAGrpAUGC.size() > 0) {
                AnnualEstablishment aer = null;
                for (int i = 0; i < partAGrpAUGC.size(); i++) {
                    aer = (AnnualEstablishment) partAGrpAUGC.get(i);
                    partBTotalGrpASanctionedStrength = partBTotalGrpASanctionedStrength + aer.getSanctionedStrength();
                    partBTotalGrpAMenInPosition = partBTotalGrpAMenInPosition + aer.getMeninPosition();
                    partBTotalGrpAVacancy = partBTotalGrpAVacancy + aer.getVacancyPosition();
                }
            }
            if (partAJudiciary != null && partAJudiciary.size() > 0) {
                AnnualEstablishment aer = null;
                for (int i = 0; i < partAJudiciary.size(); i++) {
                    aer = (AnnualEstablishment) partAJudiciary.get(i);
                    partBTotalGrpASanctionedStrength = partBTotalGrpASanctionedStrength + aer.getSanctionedStrength();
                    partBTotalGrpAMenInPosition = partBTotalGrpAMenInPosition + aer.getMeninPosition();
                    partBTotalGrpAVacancy = partBTotalGrpAVacancy + aer.getVacancyPosition();
                }
            }
            if (grpAList != null && grpAList.size() > 0) {
                AnnualEstablishment aer = null;
                for (int i = 0; i < grpAList.size(); i++) {
                    aer = (AnnualEstablishment) grpAList.get(i);
                    partBTotalGrpASanctionedStrength = partBTotalGrpASanctionedStrength + aer.getSanctionedStrength();
                    partBTotalGrpAMenInPosition = partBTotalGrpAMenInPosition + aer.getMeninPosition();
                    partBTotalGrpAVacancy = partBTotalGrpAVacancy + aer.getVacancyPosition();
                }
            }
            mv.addObject("partBTotalGrpASanctionedStrength", partBTotalGrpASanctionedStrength);
            mv.addObject("partBTotalGrpAMenInPosition", partBTotalGrpAMenInPosition);
            mv.addObject("partBTotalGrpAVacancy", partBTotalGrpAVacancy);

            grpBList = annuaiEstablishmentDao.getGroupAlistBeforeSubmission("PART-B", offcode, "B", Integer.parseInt(param.get("aerId")), "");

            if (grpBList != null && grpBList.size() > 0) {
                AnnualEstablishment aer = null;
                for (int i = 0; i < grpBList.size(); i++) {
                    aer = (AnnualEstablishment) grpBList.get(i);
                    partBTotalGrpBSanctionedStrength = partBTotalGrpBSanctionedStrength + aer.getSanctionedStrength();
                    partBTotalGrpBMenInPosition = partBTotalGrpBMenInPosition + aer.getMeninPosition();
                    partBTotalGrpBVacancy = partBTotalGrpBVacancy + aer.getVacancyPosition();
                }
            }
            mv.addObject("partBTotalGrpBSanctionedStrength", partBTotalGrpBSanctionedStrength);
            mv.addObject("partBTotalGrpBMenInPosition", partBTotalGrpBMenInPosition);
            mv.addObject("partBTotalGrpBVacancy", partBTotalGrpBVacancy);

            grpCList = annuaiEstablishmentDao.getGroupAlistBeforeSubmission("PART-B", offcode, "C", Integer.parseInt(param.get("aerId")), "");

            if (grpCList != null && grpCList.size() > 0) {
                AnnualEstablishment aer = null;
                for (int i = 0; i < grpCList.size(); i++) {
                    aer = (AnnualEstablishment) grpCList.get(i);
                    partBTotalGrpCSanctionedStrength = partBTotalGrpCSanctionedStrength + aer.getSanctionedStrength();
                    partBTotalGrpCMenInPosition = partBTotalGrpCMenInPosition + aer.getMeninPosition();
                    partBTotalGrpCVacancy = partBTotalGrpCVacancy + aer.getVacancyPosition();
                }
            }
            mv.addObject("partBTotalGrpCSanctionedStrength", partBTotalGrpCSanctionedStrength);
            mv.addObject("partBTotalGrpCMenInPosition", partBTotalGrpCMenInPosition);
            mv.addObject("partBTotalGrpCVacancy", partBTotalGrpCVacancy);

            grpDList = annuaiEstablishmentDao.getGroupAlistBeforeSubmission("PART-B", offcode, "D", Integer.parseInt(param.get("aerId")), "");

            if (grpDList != null && grpDList.size() > 0) {
                AnnualEstablishment aer = null;
                for (int i = 0; i < grpDList.size(); i++) {
                    aer = (AnnualEstablishment) grpDList.get(i);
                    partBTotalGrpDSanctionedStrength = partBTotalGrpDSanctionedStrength + aer.getSanctionedStrength();
                    partBTotalGrpDMenInPosition = partBTotalGrpDMenInPosition + aer.getMeninPosition();
                    partBTotalGrpDVacancy = partBTotalGrpDVacancy + aer.getVacancyPosition();
                }
            }
            mv.addObject("partBTotalGrpDSanctionedStrength", partBTotalGrpDSanctionedStrength);
            mv.addObject("partBTotalGrpDMenInPosition", partBTotalGrpDMenInPosition);
            mv.addObject("partBTotalGrpDVacancy", partBTotalGrpDVacancy);

            boolean status = annuaiEstablishmentDao.privewDataInserted(Integer.parseInt(param.get("aerId")));
            if (status) {
                hideEditLink = "Y";
            } else {
                hideEditLink = "N";
            }

            boolean hidelink = annuaiEstablishmentDao.getAerSubmittedStatus(Integer.parseInt(param.get("aerId")));

            if (hidelink == false) {
                hideUrl = "Y";
            } else {
                hideUrl = "N";
            }
            //annuaiEstablishmentDao.insertGranteeOfficeData(Integer.parseInt(param.get("aerId")),lub.getLoginoffcode());
            List officewiseAerPartBList = annuaiEstablishmentDao.getTotalGranteeOfficeListWithAerdata(offcode, Integer.parseInt(param.get("aerId")));

            mv.addObject("PartAGroupAlist", grpAList);
            mv.addObject("partAGrpAAllIndiaService", partAGrpAAllIndiaService);
            mv.addObject("partAGrpAUGC", partAGrpAUGC);
            mv.addObject("partAJudiciary", partAJudiciary);
            mv.addObject("PartAGroupBlist", grpBList);
            mv.addObject("PartAGroupClist", grpCList);
            mv.addObject("PartAGroupDlist", grpDList);
            mv.addObject("hideEditLink", hideEditLink);
            mv.addObject("Editable", hideUrl);
            mv.addObject("aerId", ae.getAerId());
            mv.addObject("officeList", officewiseAerPartBList);
            List payscaleList = payscaleDAO.getPayScaleList();
            mv.addObject("payscaleList", payscaleList);
            mv.addObject("OffName", officename);

            if (hideEditLink.equals("Y")) {

                AnnualEstablishment[] partDdetails = annuaiEstablishmentDao.getOtherEst(ae.getAerId(), "D");
                mv.addObject("aeDdetails", partDdetails);

                AnnualEstablishment[] partEdetails = annuaiEstablishmentDao.getOtherEst(ae.getAerId(), "E");
                mv.addObject("aeEdetails", partEdetails);

                List giaList = annuaiEstablishmentDao.getAllGrantinAidOfficesByAerId(ae.getAerId());
                mv.addObject("giaList", giaList);

                //List giaArrList = Arrays.asList(giaList);
                if (giaList != null && giaList.size() > 0) {
                    AnnualEstablishment aer = null;
                    for (int i = 0; i < giaList.size(); i++) {
                        aer = (AnnualEstablishment) giaList.get(i);
                        giaTotalSanctionedStrength = giaTotalSanctionedStrength + aer.getOtherSS();
                        giaTotalMenInPosition = giaTotalMenInPosition + aer.getMeninPosition();
                        giaTotalVacancy = giaTotalVacancy + aer.getOtherVacancy();
                    }
                }

                mv.addObject("giaTotalSanctionedStrength", giaTotalSanctionedStrength);
                mv.addObject("giaTotalMenInPosition", giaTotalMenInPosition);
                mv.addObject("giaTotalVacancy", giaTotalVacancy);

                mv.setViewName("/report/PreviewSubmitAerReport");
            } else {
                mv.setViewName("/report/AerReportPartB");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return mv;
    }

    @RequestMapping(value = "aerReportPartC")
    public ModelAndView aerReportPartC(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") AnnualEstablishment ae, @RequestParam Map<String, String> param) {

        List grpCList = new ArrayList();
        String hideEditLink = "";

        ModelAndView mv = new ModelAndView();
        try {

            String offcode = lub.getLoginoffcode();
            String officename = lub.getLoginoffname();

            if ((lub.getLoginAdditionalChargeOffCode() != null && !lub.getLoginAdditionalChargeOffCode().equals("")) && (lub.getLoginAdditionalChargeSpc() != null && !lub.getLoginAdditionalChargeSpc().equals(""))) {
                offcode = lub.getLoginAdditionalChargeOffCode();
                officename = lub.getLoginadditionalChargeOfficename();
            }

            grpCList = annuaiEstablishmentDao.getGroupAlistBeforeSubmission("PART-C", offcode, "C", Integer.parseInt(param.get("aerId")), "");
            boolean status = annuaiEstablishmentDao.getAerSubmittedStatus(Integer.parseInt(param.get("aerId")));
            if (status) {
                hideEditLink = "Y";
            } else {
                hideEditLink = "N";
            }
            mv.addObject("OffName", officename);

            List payscaleList = payscaleDAO.getPayScaleList();
            mv.addObject("payscaleList", payscaleList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mv.addObject("Editable", hideEditLink);
        mv.addObject("PartCGrouplist", grpCList);
        mv.setViewName("/report/AerReportPartC");
        return mv;
    }

    @RequestMapping(value = "deleteReportPartA")
    public ModelAndView deleteReportPartA(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") AnnualEstablishment ae, @RequestParam Map<String, String> param) {

        ModelAndView mav = null;

        String offcode = lub.getLoginoffcode();

        if (lub.getLoginAdditionalChargeOffCode() != null && !lub.getLoginAdditionalChargeOffCode().equals("")) {
            offcode = lub.getLoginAdditionalChargeOffCode();
        }

        annuaiEstablishmentDao.deleteAerData(Integer.parseInt(param.get("aerId")), offcode);

        return new ModelAndView("redirect:/displayAERlist.htm?financialYear=" + ae.getFinancialYear(), "command", ae);

    }

    @RequestMapping(value = "deleteDataFormAerReport")
    public ModelAndView deleteDataFormAerReport(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") AnnualEstablishment ae, @RequestParam Map<String, String> param) {

        ModelAndView mav = null;

        String ddocode = lub.getLoginDDOCode();

        if (lub.getLoginAdditionalChargeDDOCode() != null && !lub.getLoginAdditionalChargeDDOCode().equals("")) {
            ddocode = lub.getLoginAdditionalChargeDDOCode();
        }

        annuaiEstablishmentDao.deleteAerReportData(Integer.parseInt(param.get("aerId")), ddocode);

        return new ModelAndView("redirect:/displayAERlist.htm?financialYear=" + ae.getFinancialYear(), "command", ae);

    }

    @RequestMapping(value = "addPartDOtherEstablishment.htm")
    public ModelAndView addPartDOtherEstablishment(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") AnnualEstablishment ae, @RequestParam Map<String, String> parameters) {
        ModelAndView mav = null;
        mav = new ModelAndView("report/partD_other", "command", ae);
        String officename = lub.getLoginoffname();
        if (lub.getLoginadditionalChargeOfficename() != null && !lub.getLoginadditionalChargeOfficename().equals("")) {
            officename = lub.getLoginadditionalChargeOfficename();
        }
        mav.addObject("OffName", officename);

        String fyear = parameters.get("fy");

        if (parameters.get("aerId") != null && !parameters.get("aerId").equals("")) {
            ae.setAerId(Integer.parseInt(parameters.get("aerId")));
        } else {
            ae.setAerId(0);
        }

        mav.addObject("financialYear", fyear);
        String partType = "D";
        AnnualEstablishment[] aeDetails = annuaiEstablishmentDao.getOtherEst(ae.getAerId(), partType);
        boolean status = annuaiEstablishmentDao.getAerSubmittedStatus(ae.getAerId());
        ae.setFinancialYear(fyear);
        mav.addObject("Submittedstatus", status);
        mav.addObject("aeDetails", aeDetails);
        return mav;

    }

    @RequestMapping(value = "addPartEOutsourced.htm")
    public ModelAndView addPartCOutsourced(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") AnnualEstablishment ae, @RequestParam Map<String, String> parameters) {
        ModelAndView mav = null;
        mav = new ModelAndView("report/partE_outsourced", "command", ae);
        String officename = lub.getLoginoffname();
        if (lub.getLoginadditionalChargeOfficename() != null && !lub.getLoginadditionalChargeOfficename().equals("")) {
            officename = lub.getLoginadditionalChargeOfficename();
        }
        mav.addObject("OffName", officename);

        String fyear = parameters.get("fy");

        if (parameters.get("aerId") != null && !parameters.get("aerId").equals("")) {
            ae.setAerId(Integer.parseInt(parameters.get("aerId")));
        } else {
            ae.setAerId(0);
        }

        mav.addObject("financialYear", fyear);
        String partType = "E";
        AnnualEstablishment[] aeDetails = annuaiEstablishmentDao.getOtherEst(ae.getAerId(), partType);
        boolean status = annuaiEstablishmentDao.getAerSubmittedStatus(ae.getAerId());
        ae.setFinancialYear(fyear);
        mav.addObject("Submittedstatus", status);
        mav.addObject("aeDetails", aeDetails);
        return mav;

    }

    @RequestMapping(value = "updatePartCPost")
    public String updatePartCPost(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") AnnualEstablishment ae) {

        String path = "redirect://aerReportPartC.htm?aerId=" + ae.getAerId();

        int retVal = annuaiEstablishmentDao.updatePartCPostInformation(ae.getOffCode(), ae.getGpc(), ae.getScaleofPay(), ae.getPostgrp(), ae.getPaylevel(), ae.getGp());

        return path;
    }

    @RequestMapping(value = "createAER", params = {"btnAer=Approve"})
    public String approveAER(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") AnnualEstablishment ae) {

        String path = "redirect:/displayAERlist.htm";

        annuaiEstablishmentDao.approveAER(ae.getAerId(), lub.getLoginempid(), lub.getLoginspc(), lub.getLoginoffcode(), ae.getHidOffCode(), "APPROVER");

        return path;
    }

    @RequestMapping(value = "granteeofficewiseAer")
    public ModelAndView granteeofficewiseAer(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") AnnualEstablishment ae, @RequestParam Map<String, String> param) {
        ModelAndView mv = new ModelAndView();

        ae.setAerId(Integer.parseInt(param.get("aerId")));
        ae.setOffCode(param.get("offCode"));
        AnnualEstablishment[] aeDetails = annuaiEstablishmentDao.getGiaEst(ae.getAerId(), ae.getOffCode(), "B");
        Office offObj = officeDao.getOfficeDetails(ae.getOffCode());
        mv.addObject("OffName", offObj.getOffName());
        mv.addObject("aeDetails", aeDetails);
        mv.setViewName("/report/OfficeWiseAERData");
        return mv;
    }

    @RequestMapping(value = "editGiaEst.htm")
    public ModelAndView editGiaEst(@ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("offCode") String offCode, @RequestParam("aerOtherId") int aerOtherId, @RequestParam("aerId") int aerId, @RequestParam("partType") String partType, @ModelAttribute("command") AnnualEstablishment ae, @RequestParam Map<String, String> parameters) {
        ModelAndView mav = null;
        mav = new ModelAndView("report/OfficeWiseAERData");
        mav.addObject("OffName", lub.getLoginoffname());
        String fyear = parameters.get("fy");

        if (ae.getAerId() == 0) {
            ae.setAerId(Integer.parseInt(parameters.get("aerId")));
        }
        mav.addObject("financialYear", fyear);
        // AnnualEstablishment[] aeDetails = annuaiEstablishmentDao.getOtherEst(ae.getAerId(),partType);
        ae.setFinancialYear(fyear);
        //  mav.addObject("aeDetails", aeDetails);
        ae = annuaiEstablishmentDao.getGiaEstDetals(aerOtherId, partType, aerId);
        ae.setOffCode(offCode);
        mav.addObject("partType", partType);
        mav.addObject("offCode", ae.getOffCode());
        mav.addObject("command", ae);
        AnnualEstablishment[] aeDetails = annuaiEstablishmentDao.getGiaEst(ae.getAerId(), offCode, "B");
        mav.addObject("aeDetails", aeDetails);

        return mav;
    }

    @RequestMapping(value = "saveGIAData", params = {"action=Save"})
    public ModelAndView saveGIAData(HttpServletRequest request, HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") AnnualEstablishment ae, @RequestParam Map<String, String> req) {

        if (ae.getAerOtherId() != 0) {
            annuaiEstablishmentDao.updateAerGiaData(ae);
        } else {
            annuaiEstablishmentDao.saveAerGiaData(ae);
        }

        String aerId = req.get("aerId");
        ModelAndView mav = null;
        mav = new ModelAndView("redirect:granteeofficewiseAer.htm?aerId=" + ae.getAerId() + "&offCode=" + ae.getOffCode(), "command", ae);

        mav.addObject("OffName", lub.getLoginoffname());

        return mav;
    }

    @RequestMapping(value = "saveGIAData", params = {"action=Back"})
    public ModelAndView backGIAData(HttpServletRequest request, HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") AnnualEstablishment ae, @RequestParam Map<String, String> req) {
        ModelAndView mav = null;
        mav = new ModelAndView("redirect:aerReportPartB.htm?aerId=" + ae.getAerId());
        mav.addObject("OffName", lub.getLoginoffname());
        return mav;

    }

    @RequestMapping(value = "deleteGiaEst.htm")
    public ModelAndView deleteGiaEst(@ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("aerOtherId") int aerOtherId, @RequestParam("aerId") int aerId, @RequestParam("partType") String partType, @RequestParam("offCode") String offCode) {
        ModelAndView mav = null;

        annuaiEstablishmentDao.deleteGiaEst(aerOtherId, partType, aerId);
        //mav = new ModelAndView("redirect:editGiaEst.htm?aerId=" + aerId + "&aerOtherId=" + aerOtherId + "&offCode=" + offCode + "&partType=" + partType);
        mav = new ModelAndView("redirect:granteeofficewiseAer.htm?aerId=" + aerId + "&offCode=" + offCode + "&partType=" + partType);

        mav.addObject("OffName", lub.getLoginoffname());
        return mav;
    }

    @RequestMapping(value = "DownloadaerPDFReport")
    public void DownloadaerPDFReport(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam Map<String, String> param) {
        String aerId = param.get("aerId");
        String fileName = "AER_REPORT_" + aerId;
        response.setContentType("application/pdf");
        Document document = new Document(PageSize.A4);

        PdfWriter writer = null;
        try {
            response.setHeader("Content-Disposition", "attachment; filename=AER_REPORT_" + aerId + ".pdf");

            String officename = annuaiEstablishmentDao.getAERSubmittedOfficeName(Integer.parseInt(aerId));

            writer = PdfWriter.getInstance(document, response.getOutputStream());
            writer.setPageEvent(new AERHeaderFooter(officename));

            document.open();
            annuaiEstablishmentDao.DownloadaerPDFReport(document, aerId, lub.getLoginoffcode());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }

    }

    @RequestMapping(value = "DownloadPDFReportDC")
    public void DownloadPDFReportDC(HttpServletResponse response, @RequestParam Map<String, String> param) {
        String aerId = param.get("aerId");
        String fileName = "AER_REPORT_" + aerId;
        response.setContentType("application/pdf");
        Document document = new Document(PageSize.A4);

        PdfWriter writer = null;
        try {
            response.setHeader("Content-Disposition", "attachment; filename=AER_REPORT_" + aerId + ".pdf");

            String officename = annuaiEstablishmentDao.getAERSubmittedOfficeName(Integer.parseInt(aerId));

            writer = PdfWriter.getInstance(document, response.getOutputStream());
            writer.setPageEvent(new AERHeaderFooter(officename));

            document.open();
            annuaiEstablishmentDao.DownloadaerPDFReport(document, aerId, param.get("offcode"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }

    @RequestMapping(value = "downloadaerPDFReportForScheduleII")
    public void DownloadaerPDFReportForScheduleII(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam Map<String, String> param) {
        String aerId = param.get("aerId");
        String fileName = "AER_REPORT_" + aerId;
        response.setContentType("application/pdf");
        Document document = new Document(PageSize.A4);

        PdfWriter writer = null;
        try {
            response.setHeader("Content-Disposition", "attachment; filename=AER_REPORT_" + aerId + ".pdf");

            String officename = annuaiEstablishmentDao.getAERSubmittedCOOfficeName(Integer.parseInt(aerId));

            writer = PdfWriter.getInstance(document, response.getOutputStream());
            writer.setPageEvent(new AERHeaderFooter(officename));

            document.open();

            if ((lub.getLoginAdditionalChargeOffCode() != null && !lub.getLoginAdditionalChargeOffCode().equals("")) && (lub.getLoginAdditionalChargeSpc() != null && !lub.getLoginAdditionalChargeSpc().equals(""))) {
                lub.setLoginoffcode(lub.getLoginAdditionalChargeOffCode());
                lub.setLoginspc(lub.getLoginAdditionalChargeSpc());
            }

            annuaiEstablishmentDao.downloadaerPDFReportScheduleII(document, aerId, lub.getLoginoffcode());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }

    }

    @RequestMapping(value = "createAER", params = {"btnAer=Decline"})
    public String declineAER(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") AnnualEstablishment ae) {

        String path = "redirect:/displayAERlist.htm";

        annuaiEstablishmentDao.declineAER(ae.getAerId(), lub.getLoginempid(), lub.getLoginspc(), lub.getLoginoffcode(), ae.getRevertReason(), "APPROVER");

        return path;
    }

    @ResponseBody
    @RequestMapping(value = "getRemarks")
    public void getRemarks(HttpServletResponse response, @ModelAttribute("command") AnnualEstablishment ae) throws Exception {

        response.setContentType("application/json");
        PrintWriter out = null;

        String remarks = annuaiEstablishmentDao.getRemarks(ae.getAerId(), ae.getGpc());

        JSONObject jobj = new JSONObject();
        jobj.append("remarks", remarks);
        out = response.getWriter();
        out.write(jobj.toString());
    }

    @ResponseBody
    @RequestMapping(value = "saveRemarks")
    public void SaveRemarks(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") AnnualEstablishment ae) {

        annuaiEstablishmentDao.updateRemarks(ae.getAerId(), ae.getGpc(), ae.getOtherRemarks());
    }

    @RequestMapping(value = "displayAERlistForControllingAuthority", params = {"btnAer=Approve"})
    public String approveAERForCO(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") AnnualEstablishment ae) {
        String roleType = "";
        String path = "";
        if (ae.getCoOffCode() != null && !ae.getCoOffCode().equals("")) {
            path = "redirect:/displayAERlistForControllingAuthority.htm?btnAer=Search&financialYear=" + ae.getFinancialYear() + "&coOffCode=" + ae.getCoOffCode();
        } else {
            path = "redirect:/displayAERlistForControllingAuthority.htm?btnAer=Search&financialYear=" + ae.getFinancialYear();
        }

        String isReviewer = annuaiEstablishmentDao.checkReviewer(lub.getLoginoffcode(), lub.getLoginspc());

        String isVerifier = annuaiEstablishmentDao.checkVerifier(lub.getLoginoffcode(), lub.getLoginspc());

        if (isReviewer.equalsIgnoreCase("Y")) {
            roleType = "REVIEWER";
        } else if (isVerifier.equalsIgnoreCase("Y")) {
            roleType = "VERIFIER";
        }

        if (isReviewer.equalsIgnoreCase("Y") && isVerifier.equalsIgnoreCase("Y")) {
            roleType = "BO";
        }

        annuaiEstablishmentDao.approveAER(ae.getAerId(), lub.getLoginempid(), lub.getLoginspc(), lub.getLoginoffcode(), ae.getHidOffCode(), roleType);

        return path;
    }

    @RequestMapping(value = "displayAERlistForControllingAuthority", params = {"btnAer=DisApprove"})
    public String DisApproveAERForCO(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") AnnualEstablishment ae) {
        String roleType = "";
        String path = "";
        if (ae.getCoOffCode() != null && !ae.getCoOffCode().equals("")) {
            path = "redirect:/displayAERlistForControllingAuthority.htm?btnAer=Search&financialYear=" + ae.getFinancialYear() + "&coOffCode=" + ae.getCoOffCode();
        } else {
            path = "redirect:/displayAERlistForControllingAuthority.htm?btnAer=Search&financialYear=" + ae.getFinancialYear();
        }

        String isReviewer = annuaiEstablishmentDao.checkReviewer(lub.getLoginoffcode(), lub.getLoginspc());

        String isVerifier = annuaiEstablishmentDao.checkVerifier(lub.getLoginoffcode(), lub.getLoginspc());

        if (isReviewer.equalsIgnoreCase("Y")) {
            roleType = "REVIEWER";
        } else if (isVerifier.equalsIgnoreCase("Y")) {
            roleType = "VERIFIER";
        }

        if (isReviewer.equalsIgnoreCase("Y") && isVerifier.equalsIgnoreCase("Y")) {
            roleType = "BO";
        }

        annuaiEstablishmentDao.disApproveAER(ae.getAerId(), lub.getLoginempid(), lub.getLoginspc(), lub.getLoginoffcode(), ae.getHidOffCode(), roleType);

        return path;
    }

    @RequestMapping(value = "displayAERlistForControllingAuthority", params = {"btnAer=Decline"})
    public String declineAERForCO(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") AnnualEstablishment ae) {

        String path = "redirect:/displayAERlistForControllingAuthority.htm?btnAer=Search";

        if (ae.getCoOffCode() != null && !ae.getCoOffCode().equals("")) {
            path = "redirect:/displayAERlistForControllingAuthority.htm?btnAer=Search&financialYear=" + ae.getFinancialYear() + "&coOffCode=" + ae.getCoOffCode();
        }

        String isReviewer = annuaiEstablishmentDao.checkReviewer(lub.getLoginoffcode(), lub.getLoginspc());

        String isVerifier = annuaiEstablishmentDao.checkVerifier(lub.getLoginoffcode(), lub.getLoginspc());

        String roleType = "";
        if (isReviewer.equalsIgnoreCase("Y")) {
            roleType = "REVIEWER";
        } else if (isVerifier.equalsIgnoreCase("Y")) {
            roleType = "VERIFIER";
        }

        if (isReviewer.equalsIgnoreCase("Y") && isVerifier.equalsIgnoreCase("Y")) {
            roleType = "BO";
        }

        annuaiEstablishmentDao.declineAER(ae.getAerId(), lub.getLoginempid(), lub.getLoginspc(), lub.getLoginoffcode(), ae.getRevertReason(), roleType);

        return path;
    }

    @RequestMapping(value = "displayAERlistForControllingAuthority.htm", params = {"btnAer=GetCOData"})
    public ModelAndView displayAERlistForControllingAuthorityCOOfficeList(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") AnnualEstablishment ae) {

        ModelAndView mav = null;

        mav = new ModelAndView("report/AnnualEstablishmentReportForCO", "command", ae);

        List fiscyear = fiscalDAO.getFiscalYearList();

        List coofficelist = annuaiEstablishmentDao.getVerifierCOOfficeList(lub.getLoginempid(), lub.getLoginspc(), ae.getFinancialYear());
        if (coofficelist != null && coofficelist.size() > 1) {
            mav = new ModelAndView("report/AnnualEstablishmentReportCOOfficeData", "command", ae);
            mav.addObject("coofficelist", coofficelist);
            mav.addObject("selectedFiscalYear", ae.getFinancialYear());
            mav.addObject("fiscyear", fiscyear);
        } else {
            mav = new ModelAndView("redirect:/displayAERlistForControllingAuthority.htm?btnAer=Search&financialYear=" + ae.getFinancialYear());
        }

        mav.addObject("OffName", lub.getLoginoffname());

        return mav;

    }

    @RequestMapping(value = "displayAERlistForControllingAuthority.htm", params = {"btnAer=Search"})
    public ModelAndView displayAERlistForControllingAuthority(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") AnnualEstablishment ae) {
        ModelAndView mav = null;

        mav = new ModelAndView("report/AnnualEstablishmentReportForCO", "command", ae);
        List<AnnualEstablishment> submitList = null;

        String roletype = annuaiEstablishmentDao.getRoleTypeForReviewerAndVerifier(lub.getLoginempid(), lub.getLoginspc(), ae.getFinancialYear(), lub.getLoginoffcode());
        mav.addObject("roleType", roletype);

        List fylist = annuaiEstablishmentDao.getFinancialYearList();
        mav.addObject("financialYearList", fylist);

        List fiscyear = fiscalDAO.getFiscalYearList();
        mav.addObject("fiscyear", fiscyear);

        int coAerId = 0;

        if (ae.getCoOffCode() != null && !ae.getCoOffCode().equals("")) {
            submitList = annuaiEstablishmentDao.getAerReportListFinancialYearWiseForCOLevelForMultipleCOOffice(ae.getCoOffCode(), ae.getFinancialYear(), roletype, lub.getLoginspc());
            coAerId = annuaiEstablishmentDao.getSchedule2AerIdForMultipleCOOffice(lub.getLoginspc(), lub.getLoginempid(), ae.getFinancialYear(), ae.getCoOffCode());
            ae.setCoOffCode(ae.getCoOffCode());
        } else {
            submitList = annuaiEstablishmentDao.getAerReportListFinancialYearWiseForCOLevel(lub.getLoginoffcode(), ae.getFinancialYear(), roletype, lub.getLoginspc());
            coAerId = annuaiEstablishmentDao.getSchedule2AerId(lub.getLoginspc(), lub.getLoginempid(), ae.getFinancialYear());
            mav.addObject("cooffcode", "");
        }

        mav.addObject("coaerid", coAerId);
        mav.addObject("EstablishmentList", submitList);
        mav.addObject("OffName", lub.getLoginoffname());

        return mav;

    }

    @RequestMapping(value = "displayAERlistForControllingAuthority.htm", params = {"btnAer=View"})
    public ModelAndView viewAERlistForControllingAuthority(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") AnnualEstablishment ae) {
        ModelAndView mav = null;

        List partAGrpAAllIndiaService = new ArrayList();
        List partAGrpAUGC = new ArrayList();
        List partAJudiciary = new ArrayList();
        List grpAList = new ArrayList();
        List grpBList = new ArrayList();
        List grpCList = new ArrayList();
        List grpDList = new ArrayList();

        List partBGrpAAllIndiaService = new ArrayList();
        List partBGrpAUGC = new ArrayList();
        List partBJudiciary = new ArrayList();
        List partBgrpAList = new ArrayList();
        List partBgrpBList = new ArrayList();
        List partBgrpCList = new ArrayList();
        List partBgrpDList = new ArrayList();

        List grpPartCList = new ArrayList();

        mav = new ModelAndView("report/AnnualEstablishmentReportViewForCO", "command", ae);

        String showsubmitbtn = "";
        showsubmitbtn = annuaiEstablishmentDao.getSubmitStatusForReviewer(lub.getLoginempid(), lub.getLoginspc(), ae.getFinancialYear(), lub.getLoginoffcode());
        mav.addObject("showsubmitbtn", showsubmitbtn);

        String roletype = annuaiEstablishmentDao.getRoleTypeForReviewerAndVerifier(lub.getLoginempid(), lub.getLoginspc(), ae.getFinancialYear(), lub.getLoginoffcode());
        mav.addObject("roleType", roletype);
        if (roletype.equals("BO")) {
            roletype = "REVIEWER";
        }

        partAGrpAAllIndiaService = annuaiEstablishmentDao.getAerViewForCO(ae.getFinancialYear(), lub.getLoginspc(), lub.getLoginoffcode(), "A", "AIS", "PART-A", roletype);
        partAGrpAUGC = annuaiEstablishmentDao.getAerViewForCO(ae.getFinancialYear(), lub.getLoginspc(), lub.getLoginoffcode(), "A", "UGC", "PART-A", roletype);
        partAJudiciary = annuaiEstablishmentDao.getAerViewForCO(ae.getFinancialYear(), lub.getLoginspc(), lub.getLoginoffcode(), "A", "OJS", "PART-A", roletype);
        grpAList = annuaiEstablishmentDao.getAerViewForCO(ae.getFinancialYear(), lub.getLoginspc(), lub.getLoginoffcode(), "A", "", "PART-A", roletype);
        grpBList = annuaiEstablishmentDao.getAerViewForCO(ae.getFinancialYear(), lub.getLoginspc(), lub.getLoginoffcode(), "B", "", "PART-A", roletype);
        grpCList = annuaiEstablishmentDao.getAerViewForCO(ae.getFinancialYear(), lub.getLoginspc(), lub.getLoginoffcode(), "C", "", "PART-A", roletype);
        grpDList = annuaiEstablishmentDao.getAerViewForCO(ae.getFinancialYear(), lub.getLoginspc(), lub.getLoginoffcode(), "D", "", "PART-A", roletype);

        partBGrpAAllIndiaService = annuaiEstablishmentDao.getAerViewForCO(ae.getFinancialYear(), lub.getLoginspc(), lub.getLoginoffcode(), "A", "AIS", "PART-B", roletype);
        partBGrpAUGC = annuaiEstablishmentDao.getAerViewForCO(ae.getFinancialYear(), lub.getLoginspc(), lub.getLoginoffcode(), "A", "UGC", "PART-B", roletype);
        partBJudiciary = annuaiEstablishmentDao.getAerViewForCO(ae.getFinancialYear(), lub.getLoginspc(), lub.getLoginoffcode(), "A", "OJS", "PART-B", roletype);
        partBgrpAList = annuaiEstablishmentDao.getAerViewForCO(ae.getFinancialYear(), lub.getLoginspc(), lub.getLoginoffcode(), "A", "", "PART-B", roletype);
        partBgrpBList = annuaiEstablishmentDao.getAerViewForCO(ae.getFinancialYear(), lub.getLoginspc(), lub.getLoginoffcode(), "B", "", "PART-B", roletype);
        partBgrpCList = annuaiEstablishmentDao.getAerViewForCO(ae.getFinancialYear(), lub.getLoginspc(), lub.getLoginoffcode(), "C", "", "PART-B", roletype);
        partBgrpDList = annuaiEstablishmentDao.getAerViewForCO(ae.getFinancialYear(), lub.getLoginspc(), lub.getLoginoffcode(), "D", "", "PART-B", roletype);

        grpPartCList = annuaiEstablishmentDao.getAerViewForCO(ae.getFinancialYear(), lub.getLoginspc(), lub.getLoginoffcode(), "D", "", "PART-C", roletype);

        mav.addObject("PartAGroupAlist", grpAList);
        mav.addObject("partAGrpAAllIndiaService", partAGrpAAllIndiaService);
        mav.addObject("partAGrpAUGC", partAGrpAUGC);
        mav.addObject("partAJudiciary", partAJudiciary);
        mav.addObject("PartAGroupBlist", grpBList);
        mav.addObject("PartAGroupClist", grpCList);
        mav.addObject("PartAGroupDlist", grpDList);

        mav.addObject("partBgrpAList", partBgrpAList);
        mav.addObject("partBGrpAAllIndiaService", partBGrpAAllIndiaService);
        mav.addObject("partBGrpAUGC", partBGrpAUGC);
        mav.addObject("partBJudiciary", partBJudiciary);
        mav.addObject("partBgrpBList", partBgrpBList);
        mav.addObject("partBgrpCList", partBgrpCList);
        mav.addObject("partBgrpDList", partBgrpDList);

        mav.addObject("PartCGrouplist", grpPartCList);

        AnnualEstablishment[] partDdetails = annuaiEstablishmentDao.getAerViewOtherPartForCO(ae.getFinancialYear(), lub.getLoginspc(), "D", roletype);
        mav.addObject("aeDdetails", partDdetails);

        AnnualEstablishment[] partEdetails = annuaiEstablishmentDao.getAerViewOtherPartForCO(ae.getFinancialYear(), lub.getLoginspc(), "E", roletype);
        mav.addObject("aeEdetails", partEdetails);

        AnnualEstablishment[] giaList = annuaiEstablishmentDao.getAerViewOtherPartForCO(ae.getFinancialYear(), lub.getLoginspc(), "B", roletype);
        mav.addObject("giaList", giaList);

        return mav;

    }

    @RequestMapping(value = "displayAERlistForControllingAuthority.htm", params = {"btnAer=ViewAsVerifier"})
    public ModelAndView viewAERlistForVerifier(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") AnnualEstablishment ae) {
        ModelAndView mav = null;
        List grpAList = new ArrayList();
        List partAGrpAAllIndiaService = new ArrayList();
        List partAGrpAUGC = new ArrayList();
        List partAJudiciary = new ArrayList();
        List grpBList = new ArrayList();
        List grpCList = new ArrayList();
        List grpDList = new ArrayList();

        List partBGrpAAllIndiaService = new ArrayList();
        List partBGrpAUGC = new ArrayList();
        List partBJudiciary = new ArrayList();
        List partBgrpAList = new ArrayList();
        List partBgrpBList = new ArrayList();
        List partBgrpCList = new ArrayList();
        List partBgrpDList = new ArrayList();

        List grpPartCList = new ArrayList();

        int partATotalGrpASanctionedStrength = 0;
        int partATotalGrpAMenInPosition = 0;
        int partATotalGrpAVacancy = 0;

        int partATotalGrpBSanctionedStrength = 0;
        int partATotalGrpBMenInPosition = 0;
        int partATotalGrpBVacancy = 0;

        int partATotalGrpCSanctionedStrength = 0;
        int partATotalGrpCMenInPosition = 0;
        int partATotalGrpCVacancy = 0;

        int partATotalGrpDSanctionedStrength = 0;
        int partATotalGrpDMenInPosition = 0;
        int partATotalGrpDVacancy = 0;

        int partBtotalGrpASanctionedStrength = 0;
        int partBtotalGrpAMenInPosition = 0;
        int partBtotalGrpAVacancy = 0;

        int partBTotalGrpBSanctionedStrength = 0;
        int partBTotalGrpBMenInPosition = 0;
        int partBTotalGrpBVacancy = 0;

        int partBTotalGrpCSanctionedStrength = 0;
        int partBTotalGrpCMenInPosition = 0;
        int partBTotalGrpCVacancy = 0;

        int partBTotalGrpDSanctionedStrength = 0;
        int partBTotalGrpDMenInPosition = 0;
        int partBTotalGrpDVacancy = 0;

        int giaTotalSanctionedStrength = 0;
        int giaTotalMenInPosition = 0;
        int giaTotalVacancy = 0;

        mav = new ModelAndView("report/AnnualEstablishmentReportViewForCO", "command", ae);

        String showsubmitbtn = "";
        showsubmitbtn = annuaiEstablishmentDao.getSubmitStatusForReviewer(lub.getLoginempid(), lub.getLoginspc(), ae.getFinancialYear(), lub.getLoginoffcode());
        mav.addObject("showsubmitbtn", showsubmitbtn);

        int coaerId = annuaiEstablishmentDao.getCoAerid(lub.getLoginempid(), lub.getLoginspc(), ae.getFinancialYear(), lub.getLoginoffcode());
        mav.addObject("coaerId", coaerId);
        String roletype = annuaiEstablishmentDao.getRoleTypeForReviewerAndVerifier(lub.getLoginempid(), lub.getLoginspc(), ae.getFinancialYear(), lub.getLoginoffcode());
        mav.addObject("roleType", roletype);

        if (roletype.equals("BO")) {
            roletype = "VERIFIER";
        }

        partAGrpAAllIndiaService = annuaiEstablishmentDao.getAerViewForCO(ae.getFinancialYear(), lub.getLoginspc(), lub.getLoginoffcode(), "A", "AIS", "PART-A", roletype);
        partAGrpAUGC = annuaiEstablishmentDao.getAerViewForCO(ae.getFinancialYear(), lub.getLoginspc(), lub.getLoginoffcode(), "A", "UGC", "PART-A", roletype);
        partAJudiciary = annuaiEstablishmentDao.getAerViewForCO(ae.getFinancialYear(), lub.getLoginspc(), lub.getLoginoffcode(), "A", "OJS", "PART-A", roletype);
        grpAList = annuaiEstablishmentDao.getAerViewForCO(ae.getFinancialYear(), lub.getLoginspc(), lub.getLoginoffcode(), "A", "", "PART-A", roletype);

        if (partAGrpAAllIndiaService != null && partAGrpAAllIndiaService.size() > 0) {
            AnnualEstablishment aer = null;
            for (int i = 0; i < partAGrpAAllIndiaService.size(); i++) {
                aer = (AnnualEstablishment) partAGrpAAllIndiaService.get(i);
                partATotalGrpASanctionedStrength = partATotalGrpASanctionedStrength + aer.getSanctionedStrength();
                partATotalGrpAMenInPosition = partATotalGrpAMenInPosition + aer.getMeninPosition();
                partATotalGrpAVacancy = partATotalGrpAVacancy + aer.getVacancyPosition();
            }
        }

        if (partAGrpAUGC != null && partAGrpAUGC.size() > 0) {
            AnnualEstablishment aer = null;
            for (int i = 0; i < partAGrpAUGC.size(); i++) {
                aer = (AnnualEstablishment) partAGrpAUGC.get(i);
                partATotalGrpASanctionedStrength = partATotalGrpASanctionedStrength + aer.getSanctionedStrength();
                partATotalGrpAMenInPosition = partATotalGrpAMenInPosition + aer.getMeninPosition();
                partATotalGrpAVacancy = partATotalGrpAVacancy + aer.getVacancyPosition();
            }
        }

        if (partAJudiciary != null && partAJudiciary.size() > 0) {
            AnnualEstablishment aer = null;
            for (int i = 0; i < partAJudiciary.size(); i++) {
                aer = (AnnualEstablishment) partAJudiciary.get(i);
                partATotalGrpASanctionedStrength = partATotalGrpASanctionedStrength + aer.getSanctionedStrength();
                partATotalGrpAMenInPosition = partATotalGrpAMenInPosition + aer.getMeninPosition();
                partATotalGrpAVacancy = partATotalGrpAVacancy + aer.getVacancyPosition();
            }
        }

        if (grpAList != null && grpAList.size() > 0) {
            AnnualEstablishment aer = null;
            for (int i = 0; i < grpAList.size(); i++) {
                aer = (AnnualEstablishment) grpAList.get(i);
                partATotalGrpASanctionedStrength = partATotalGrpASanctionedStrength + aer.getSanctionedStrength();
                partATotalGrpAMenInPosition = partATotalGrpAMenInPosition + aer.getMeninPosition();
                partATotalGrpAVacancy = partATotalGrpAVacancy + aer.getVacancyPosition();
            }
        }

        mav.addObject("partATotalGrpASanctionedStrength", partATotalGrpASanctionedStrength);
        mav.addObject("partATotalGrpAMenInPosition", partATotalGrpAMenInPosition);
        mav.addObject("partATotalGrpAVacancy", partATotalGrpAVacancy);

        grpBList = annuaiEstablishmentDao.getAerViewForCO(ae.getFinancialYear(), lub.getLoginspc(), lub.getLoginoffcode(), "B", "", "PART-A", roletype);

        if (grpBList != null && grpBList.size() > 0) {
            AnnualEstablishment aer = null;
            for (int i = 0; i < grpBList.size(); i++) {
                aer = (AnnualEstablishment) grpBList.get(i);
                partATotalGrpBSanctionedStrength = partATotalGrpBSanctionedStrength + aer.getSanctionedStrength();
                partATotalGrpBMenInPosition = partATotalGrpBMenInPosition + aer.getMeninPosition();
                partATotalGrpBVacancy = partATotalGrpBVacancy + aer.getVacancyPosition();
            }
        }

        mav.addObject("partATotalGrpBSanctionedStrength", partATotalGrpBSanctionedStrength);
        mav.addObject("partATotalGrpBMenInPosition", partATotalGrpBMenInPosition);
        mav.addObject("partATotalGrpBVacancy", partATotalGrpBVacancy);

        grpCList = annuaiEstablishmentDao.getAerViewForCO(ae.getFinancialYear(), lub.getLoginspc(), lub.getLoginoffcode(), "C", "", "PART-A", roletype);

        if (grpCList != null && grpCList.size() > 0) {
            AnnualEstablishment aer = null;
            for (int i = 0; i < grpCList.size(); i++) {
                aer = (AnnualEstablishment) grpCList.get(i);
                partATotalGrpCSanctionedStrength = partATotalGrpCSanctionedStrength + aer.getSanctionedStrength();
                partATotalGrpCMenInPosition = partATotalGrpCMenInPosition + aer.getMeninPosition();
                partATotalGrpCVacancy = partATotalGrpCVacancy + aer.getVacancyPosition();
            }
        }

        mav.addObject("partATotalGrpCSanctionedStrength", partATotalGrpCSanctionedStrength);
        mav.addObject("partATotalGrpCMenInPosition", partATotalGrpCMenInPosition);
        mav.addObject("partATotalGrpCVacancy", partATotalGrpCVacancy);

        grpDList = annuaiEstablishmentDao.getAerViewForCO(ae.getFinancialYear(), lub.getLoginspc(), lub.getLoginoffcode(), "D", "", "PART-A", roletype);

        if (grpDList != null && grpDList.size() > 0) {
            AnnualEstablishment aer = null;
            for (int i = 0; i < grpDList.size(); i++) {
                aer = (AnnualEstablishment) grpDList.get(i);
                partATotalGrpDSanctionedStrength = partATotalGrpDSanctionedStrength + aer.getSanctionedStrength();
                partATotalGrpDMenInPosition = partATotalGrpDMenInPosition + aer.getMeninPosition();
                partATotalGrpDVacancy = partATotalGrpDVacancy + aer.getVacancyPosition();
            }
        }

        mav.addObject("partATotalGrpDSanctionedStrength", partATotalGrpDSanctionedStrength);
        mav.addObject("partATotalGrpDMenInPosition", partATotalGrpDMenInPosition);
        mav.addObject("partATotalGrpDVacancy", partATotalGrpDVacancy);

        partBGrpAAllIndiaService = annuaiEstablishmentDao.getAerViewForCO(ae.getFinancialYear(), lub.getLoginspc(), lub.getLoginoffcode(), "A", "AIS", "PART-B", roletype);
        partBGrpAUGC = annuaiEstablishmentDao.getAerViewForCO(ae.getFinancialYear(), lub.getLoginspc(), lub.getLoginoffcode(), "A", "UGC", "PART-B", roletype);
        partBJudiciary = annuaiEstablishmentDao.getAerViewForCO(ae.getFinancialYear(), lub.getLoginspc(), lub.getLoginoffcode(), "A", "OJS", "PART-B", roletype);
        partBgrpAList = annuaiEstablishmentDao.getAerViewForCO(ae.getFinancialYear(), lub.getLoginspc(), lub.getLoginoffcode(), "A", "", "PART-B", roletype);

        if (partBGrpAAllIndiaService != null && partBGrpAAllIndiaService.size() > 0) {
            AnnualEstablishment aer = null;
            for (int i = 0; i < partBGrpAAllIndiaService.size(); i++) {
                aer = (AnnualEstablishment) partBGrpAAllIndiaService.get(i);
                partBtotalGrpASanctionedStrength = partBtotalGrpASanctionedStrength + aer.getSanctionedStrength();
                partBtotalGrpAMenInPosition = partBtotalGrpAMenInPosition + aer.getMeninPosition();
                partBtotalGrpAVacancy = partBtotalGrpAVacancy + aer.getVacancyPosition();
            }
        }

        if (partBGrpAUGC != null && partBGrpAUGC.size() > 0) {
            AnnualEstablishment aer = null;
            for (int i = 0; i < partBGrpAUGC.size(); i++) {
                aer = (AnnualEstablishment) partBGrpAUGC.get(i);
                partBtotalGrpASanctionedStrength = partBtotalGrpASanctionedStrength + aer.getSanctionedStrength();
                partBtotalGrpAMenInPosition = partBtotalGrpAMenInPosition + aer.getMeninPosition();
                partBtotalGrpAVacancy = partBtotalGrpAVacancy + aer.getVacancyPosition();
            }
        }

        if (partBJudiciary != null && partBJudiciary.size() > 0) {
            AnnualEstablishment aer = null;
            for (int i = 0; i < partBJudiciary.size(); i++) {
                aer = (AnnualEstablishment) partBJudiciary.get(i);
                partBtotalGrpASanctionedStrength = partBtotalGrpASanctionedStrength + aer.getSanctionedStrength();
                partBtotalGrpAMenInPosition = partBtotalGrpAMenInPosition + aer.getMeninPosition();
                partBtotalGrpAVacancy = partBtotalGrpAVacancy + aer.getVacancyPosition();
            }
        }

        if (partBgrpAList != null && partBgrpAList.size() > 0) {
            AnnualEstablishment aer = null;
            for (int i = 0; i < partBgrpAList.size(); i++) {
                aer = (AnnualEstablishment) partBgrpAList.get(i);
                partBtotalGrpASanctionedStrength = partBtotalGrpASanctionedStrength + aer.getSanctionedStrength();
                partBtotalGrpAMenInPosition = partBtotalGrpAMenInPosition + aer.getMeninPosition();
                partBtotalGrpAVacancy = partBtotalGrpAVacancy + aer.getVacancyPosition();
            }
        }

        mav.addObject("partBtotalGrpASanctionedStrength", partBtotalGrpASanctionedStrength);
        mav.addObject("partBtotalGrpAMenInPosition", partBtotalGrpAMenInPosition);
        mav.addObject("partBtotalGrpAVacancy", partBtotalGrpAVacancy);

        partBgrpBList = annuaiEstablishmentDao.getAerViewForCO(ae.getFinancialYear(), lub.getLoginspc(), lub.getLoginoffcode(), "B", "", "PART-B", roletype);

        if (partBgrpBList != null && partBgrpBList.size() > 0) {
            AnnualEstablishment aer = null;
            for (int i = 0; i < partBgrpBList.size(); i++) {
                aer = (AnnualEstablishment) partBgrpBList.get(i);
                partBTotalGrpBSanctionedStrength = partBTotalGrpBSanctionedStrength + aer.getSanctionedStrength();
                partBTotalGrpBMenInPosition = partBTotalGrpBMenInPosition + aer.getMeninPosition();
                partBTotalGrpBVacancy = partBTotalGrpBVacancy + aer.getVacancyPosition();
            }
        }

        mav.addObject("partBTotalGrpBSanctionedStrength", partBTotalGrpBSanctionedStrength);
        mav.addObject("partBTotalGrpBMenInPosition", partBTotalGrpBMenInPosition);
        mav.addObject("partBTotalGrpBVacancy", partBTotalGrpBVacancy);

        partBgrpCList = annuaiEstablishmentDao.getAerViewForCO(ae.getFinancialYear(), lub.getLoginspc(), lub.getLoginoffcode(), "C", "", "PART-B", roletype);

        if (partBgrpCList != null && partBgrpCList.size() > 0) {
            AnnualEstablishment aer = null;
            for (int i = 0; i < partBgrpCList.size(); i++) {
                aer = (AnnualEstablishment) partBgrpCList.get(i);
                partBTotalGrpCSanctionedStrength = partBTotalGrpCSanctionedStrength + aer.getSanctionedStrength();
                partBTotalGrpCMenInPosition = partBTotalGrpCMenInPosition + aer.getMeninPosition();
                partBTotalGrpCVacancy = partBTotalGrpCVacancy + aer.getVacancyPosition();
            }
        }

        mav.addObject("partBTotalGrpCSanctionedStrength", partBTotalGrpCSanctionedStrength);
        mav.addObject("partBTotalGrpCMenInPosition", partBTotalGrpCMenInPosition);
        mav.addObject("partBTotalGrpCVacancy", partBTotalGrpCVacancy);

        partBgrpDList = annuaiEstablishmentDao.getAerViewForCO(ae.getFinancialYear(), lub.getLoginspc(), lub.getLoginoffcode(), "D", "", "PART-B", roletype);

        if (partBgrpDList != null && partBgrpDList.size() > 0) {
            AnnualEstablishment aer = null;
            for (int i = 0; i < partBgrpDList.size(); i++) {
                aer = (AnnualEstablishment) partBgrpDList.get(i);
                partBTotalGrpDSanctionedStrength = partBTotalGrpDSanctionedStrength + aer.getSanctionedStrength();
                partBTotalGrpDMenInPosition = partBTotalGrpDMenInPosition + aer.getMeninPosition();
                partBTotalGrpDVacancy = partBTotalGrpDVacancy + aer.getVacancyPosition();
            }
        }

        mav.addObject("partBTotalGrpDSanctionedStrength", partBTotalGrpDSanctionedStrength);
        mav.addObject("partBTotalGrpDMenInPosition", partBTotalGrpDMenInPosition);
        mav.addObject("partBTotalGrpDVacancy", partBTotalGrpDVacancy);

        grpPartCList = annuaiEstablishmentDao.getAerViewForCO(ae.getFinancialYear(), lub.getLoginspc(), lub.getLoginoffcode(), "D", "", "PART-C", roletype);

        mav.addObject("PartAGroupAlist", grpAList);
        mav.addObject("partAGrpAAllIndiaService", partAGrpAAllIndiaService);
        mav.addObject("partAGrpAUGC", partAGrpAUGC);
        mav.addObject("partAJudiciary", partAJudiciary);
        mav.addObject("PartAGroupBlist", grpBList);
        mav.addObject("PartAGroupClist", grpCList);
        mav.addObject("PartAGroupDlist", grpDList);

        mav.addObject("partBgrpAList", partBgrpAList);
        mav.addObject("partBGrpAAllIndiaService", partBGrpAAllIndiaService);
        mav.addObject("partBGrpAUGC", partBGrpAUGC);
        mav.addObject("partBJudiciary", partBJudiciary);
        mav.addObject("partBgrpBList", partBgrpBList);
        mav.addObject("partBgrpCList", partBgrpCList);
        mav.addObject("partBgrpDList", partBgrpDList);

        mav.addObject("PartCGrouplist", grpPartCList);

        AnnualEstablishment[] partDdetails = annuaiEstablishmentDao.getAerViewOtherPartForCO(ae.getFinancialYear(), lub.getLoginspc(), "D", roletype);
        mav.addObject("aeDdetails", partDdetails);

        AnnualEstablishment[] partEdetails = annuaiEstablishmentDao.getAerViewOtherPartForCO(ae.getFinancialYear(), lub.getLoginspc(), "E", roletype);
        mav.addObject("aeEdetails", partEdetails);

        AnnualEstablishment[] giaList = annuaiEstablishmentDao.getAerViewOtherPartForCO(ae.getFinancialYear(), lub.getLoginspc(), "B", roletype);
        mav.addObject("giaList", giaList);

        List giaArrList = Arrays.asList(giaList);

        if (giaArrList != null && giaArrList.size() > 0) {
            AnnualEstablishment aer = null;
            for (int i = 0; i < giaArrList.size(); i++) {
                aer = (AnnualEstablishment) giaArrList.get(i);
                giaTotalSanctionedStrength = giaTotalSanctionedStrength + aer.getOtherSS();
                giaTotalMenInPosition = giaTotalMenInPosition + aer.getMeninPosition();
                giaTotalVacancy = giaTotalVacancy + aer.getOtherVacancy();
            }
        }
        mav.addObject("giaTotalSanctionedStrength", giaTotalSanctionedStrength);
        mav.addObject("giaTotalMenInPosition", giaTotalMenInPosition);
        mav.addObject("giaTotalVacancy", giaTotalVacancy);

        mav.addObject("pendingApproveCount", annuaiEstablishmentDao.getCOAuthorityApprovedOperatorList(lub.getLoginoffcode(), ae.getFinancialYear(), roletype, lub.getLoginspc()));

        return mav;

    }

    @RequestMapping(value = "displayAERlistForControllingAuthority.htm", params = {"btnAer=ViewAsVerifierMultipleCO"})
    public ModelAndView viewAERlistForVerifierMultipleCO(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") AnnualEstablishment ae) {

        ModelAndView mav = null;

        List grpAList = new ArrayList();
        List partAGrpAAllIndiaService = new ArrayList();
        List partAGrpAUGC = new ArrayList();
        List partAJudiciary = new ArrayList();
        List grpBList = new ArrayList();
        List grpCList = new ArrayList();
        List grpDList = new ArrayList();

        List partBGrpAAllIndiaService = new ArrayList();
        List partBGrpAUGC = new ArrayList();
        List partBJudiciary = new ArrayList();
        List partBgrpAList = new ArrayList();
        List partBgrpBList = new ArrayList();
        List partBgrpCList = new ArrayList();
        List partBgrpDList = new ArrayList();

        List grpPartCList = new ArrayList();

        int partATotalGrpASanctionedStrength = 0;
        int partATotalGrpAMenInPosition = 0;
        int partATotalGrpAVacancy = 0;

        int partATotalGrpBSanctionedStrength = 0;
        int partATotalGrpBMenInPosition = 0;
        int partATotalGrpBVacancy = 0;

        int partATotalGrpCSanctionedStrength = 0;
        int partATotalGrpCMenInPosition = 0;
        int partATotalGrpCVacancy = 0;

        int partATotalGrpDSanctionedStrength = 0;
        int partATotalGrpDMenInPosition = 0;
        int partATotalGrpDVacancy = 0;

        int partBtotalGrpASanctionedStrength = 0;
        int partBtotalGrpAMenInPosition = 0;
        int partBtotalGrpAVacancy = 0;

        int partBTotalGrpBSanctionedStrength = 0;
        int partBTotalGrpBMenInPosition = 0;
        int partBTotalGrpBVacancy = 0;

        int partBTotalGrpCSanctionedStrength = 0;
        int partBTotalGrpCMenInPosition = 0;
        int partBTotalGrpCVacancy = 0;

        int partBTotalGrpDSanctionedStrength = 0;
        int partBTotalGrpDMenInPosition = 0;
        int partBTotalGrpDVacancy = 0;

        int giaTotalSanctionedStrength = 0;
        int giaTotalMenInPosition = 0;
        int giaTotalVacancy = 0;

        mav = new ModelAndView("report/AnnualEstablishmentReportViewForCO", "command", ae);

        String showsubmitbtn = "";
        showsubmitbtn = annuaiEstablishmentDao.getSubmitStatusForReviewer(lub.getLoginempid(), lub.getLoginspc(), ae.getFinancialYear(), lub.getLoginoffcode());
        mav.addObject("showsubmitbtn", showsubmitbtn);

        String roletype = annuaiEstablishmentDao.getRoleTypeForReviewerAndVerifier(lub.getLoginempid(), lub.getLoginspc(), ae.getFinancialYear(), lub.getLoginoffcode());
        mav.addObject("roleType", roletype);

        if (roletype.equals("BO")) {
            roletype = "VERIFIER";
        }

        partAGrpAAllIndiaService = annuaiEstablishmentDao.getAerViewForCOMultiple(ae.getFinancialYear(), lub.getLoginspc(), ae.getCoOffCode(), "A", "AIS", "PART-A", roletype);
        partAGrpAUGC = annuaiEstablishmentDao.getAerViewForCOMultiple(ae.getFinancialYear(), lub.getLoginspc(), ae.getCoOffCode(), "A", "UGC", "PART-A", roletype);
        partAJudiciary = annuaiEstablishmentDao.getAerViewForCOMultiple(ae.getFinancialYear(), lub.getLoginspc(), ae.getCoOffCode(), "A", "OJS", "PART-A", roletype);
        grpAList = annuaiEstablishmentDao.getAerViewForCOMultiple(ae.getFinancialYear(), lub.getLoginspc(), ae.getCoOffCode(), "A", "", "PART-A", roletype);

        if (partAGrpAAllIndiaService != null && partAGrpAAllIndiaService.size() > 0) {
            AnnualEstablishment aer = null;
            for (int i = 0; i < partAGrpAAllIndiaService.size(); i++) {
                aer = (AnnualEstablishment) partAGrpAAllIndiaService.get(i);
                partATotalGrpASanctionedStrength = partATotalGrpASanctionedStrength + aer.getSanctionedStrength();
                partATotalGrpAMenInPosition = partATotalGrpAMenInPosition + aer.getMeninPosition();
                partATotalGrpAVacancy = partATotalGrpAVacancy + aer.getVacancyPosition();
            }
        }

        if (partAGrpAUGC != null && partAGrpAUGC.size() > 0) {
            AnnualEstablishment aer = null;
            for (int i = 0; i < partAGrpAUGC.size(); i++) {
                aer = (AnnualEstablishment) partAGrpAUGC.get(i);
                partATotalGrpASanctionedStrength = partATotalGrpASanctionedStrength + aer.getSanctionedStrength();
                partATotalGrpAMenInPosition = partATotalGrpAMenInPosition + aer.getMeninPosition();
                partATotalGrpAVacancy = partATotalGrpAVacancy + aer.getVacancyPosition();
            }
        }

        if (partAJudiciary != null && partAJudiciary.size() > 0) {
            AnnualEstablishment aer = null;
            for (int i = 0; i < partAJudiciary.size(); i++) {
                aer = (AnnualEstablishment) partAJudiciary.get(i);
                partATotalGrpASanctionedStrength = partATotalGrpASanctionedStrength + aer.getSanctionedStrength();
                partATotalGrpAMenInPosition = partATotalGrpAMenInPosition + aer.getMeninPosition();
                partATotalGrpAVacancy = partATotalGrpAVacancy + aer.getVacancyPosition();
            }
        }

        if (grpAList != null && grpAList.size() > 0) {
            AnnualEstablishment aer = null;
            for (int i = 0; i < grpAList.size(); i++) {
                aer = (AnnualEstablishment) grpAList.get(i);
                partATotalGrpASanctionedStrength = partATotalGrpASanctionedStrength + aer.getSanctionedStrength();
                partATotalGrpAMenInPosition = partATotalGrpAMenInPosition + aer.getMeninPosition();
                partATotalGrpAVacancy = partATotalGrpAVacancy + aer.getVacancyPosition();
            }
        }

        mav.addObject("partATotalGrpASanctionedStrength", partATotalGrpASanctionedStrength);
        mav.addObject("partATotalGrpAMenInPosition", partATotalGrpAMenInPosition);
        mav.addObject("partATotalGrpAVacancy", partATotalGrpAVacancy);

        grpBList = annuaiEstablishmentDao.getAerViewForCOMultiple(ae.getFinancialYear(), lub.getLoginspc(), ae.getCoOffCode(), "B", "", "PART-A", roletype);

        if (grpBList != null && grpBList.size() > 0) {
            AnnualEstablishment aer = null;
            for (int i = 0; i < grpBList.size(); i++) {
                aer = (AnnualEstablishment) grpBList.get(i);
                partATotalGrpBSanctionedStrength = partATotalGrpBSanctionedStrength + aer.getSanctionedStrength();
                partATotalGrpBMenInPosition = partATotalGrpBMenInPosition + aer.getMeninPosition();
                partATotalGrpBVacancy = partATotalGrpBVacancy + aer.getVacancyPosition();
            }
        }

        mav.addObject("partATotalGrpBSanctionedStrength", partATotalGrpBSanctionedStrength);
        mav.addObject("partATotalGrpBMenInPosition", partATotalGrpBMenInPosition);
        mav.addObject("partATotalGrpBVacancy", partATotalGrpBVacancy);

        grpCList = annuaiEstablishmentDao.getAerViewForCOMultiple(ae.getFinancialYear(), lub.getLoginspc(), ae.getCoOffCode(), "C", "", "PART-A", roletype);

        if (grpCList != null && grpCList.size() > 0) {
            AnnualEstablishment aer = null;
            for (int i = 0; i < grpCList.size(); i++) {
                aer = (AnnualEstablishment) grpCList.get(i);
                partATotalGrpCSanctionedStrength = partATotalGrpCSanctionedStrength + aer.getSanctionedStrength();
                partATotalGrpCMenInPosition = partATotalGrpCMenInPosition + aer.getMeninPosition();
                partATotalGrpCVacancy = partATotalGrpCVacancy + aer.getVacancyPosition();
            }
        }

        mav.addObject("partATotalGrpCSanctionedStrength", partATotalGrpCSanctionedStrength);
        mav.addObject("partATotalGrpCMenInPosition", partATotalGrpCMenInPosition);
        mav.addObject("partATotalGrpCVacancy", partATotalGrpCVacancy);

        grpDList = annuaiEstablishmentDao.getAerViewForCOMultiple(ae.getFinancialYear(), lub.getLoginspc(), ae.getCoOffCode(), "D", "", "PART-A", roletype);

        if (grpDList != null && grpDList.size() > 0) {
            AnnualEstablishment aer = null;
            for (int i = 0; i < grpDList.size(); i++) {
                aer = (AnnualEstablishment) grpDList.get(i);
                partATotalGrpDSanctionedStrength = partATotalGrpDSanctionedStrength + aer.getSanctionedStrength();
                partATotalGrpDMenInPosition = partATotalGrpDMenInPosition + aer.getMeninPosition();
                partATotalGrpDVacancy = partATotalGrpDVacancy + aer.getVacancyPosition();
            }
        }

        mav.addObject("partATotalGrpDSanctionedStrength", partATotalGrpDSanctionedStrength);
        mav.addObject("partATotalGrpDMenInPosition", partATotalGrpDMenInPosition);
        mav.addObject("partATotalGrpDVacancy", partATotalGrpDVacancy);

        partBGrpAAllIndiaService = annuaiEstablishmentDao.getAerViewForCOMultiple(ae.getFinancialYear(), lub.getLoginspc(), ae.getCoOffCode(), "A", "AIS", "PART-B", roletype);
        partBGrpAUGC = annuaiEstablishmentDao.getAerViewForCOMultiple(ae.getFinancialYear(), lub.getLoginspc(), ae.getCoOffCode(), "A", "UGC", "PART-B", roletype);
        partBJudiciary = annuaiEstablishmentDao.getAerViewForCOMultiple(ae.getFinancialYear(), lub.getLoginspc(), ae.getCoOffCode(), "A", "OJS", "PART-B", roletype);
        partBgrpAList = annuaiEstablishmentDao.getAerViewForCOMultiple(ae.getFinancialYear(), lub.getLoginspc(), ae.getCoOffCode(), "A", "", "PART-B", roletype);

        if (partBGrpAAllIndiaService != null && partBGrpAAllIndiaService.size() > 0) {
            AnnualEstablishment aer = null;
            for (int i = 0; i < partBGrpAAllIndiaService.size(); i++) {
                aer = (AnnualEstablishment) partBGrpAAllIndiaService.get(i);
                partBtotalGrpASanctionedStrength = partBtotalGrpASanctionedStrength + aer.getSanctionedStrength();
                partBtotalGrpAMenInPosition = partBtotalGrpAMenInPosition + aer.getMeninPosition();
                partBtotalGrpAVacancy = partBtotalGrpAVacancy + aer.getVacancyPosition();
            }
        }

        if (partBGrpAUGC != null && partBGrpAUGC.size() > 0) {
            AnnualEstablishment aer = null;
            for (int i = 0; i < partBGrpAUGC.size(); i++) {
                aer = (AnnualEstablishment) partBGrpAUGC.get(i);
                partBtotalGrpASanctionedStrength = partBtotalGrpASanctionedStrength + aer.getSanctionedStrength();
                partBtotalGrpAMenInPosition = partBtotalGrpAMenInPosition + aer.getMeninPosition();
                partBtotalGrpAVacancy = partBtotalGrpAVacancy + aer.getVacancyPosition();
            }
        }

        if (partBJudiciary != null && partBJudiciary.size() > 0) {
            AnnualEstablishment aer = null;
            for (int i = 0; i < partBJudiciary.size(); i++) {
                aer = (AnnualEstablishment) partBJudiciary.get(i);
                partBtotalGrpASanctionedStrength = partBtotalGrpASanctionedStrength + aer.getSanctionedStrength();
                partBtotalGrpAMenInPosition = partBtotalGrpAMenInPosition + aer.getMeninPosition();
                partBtotalGrpAVacancy = partBtotalGrpAVacancy + aer.getVacancyPosition();
            }
        }

        if (partBgrpAList != null && partBgrpAList.size() > 0) {
            AnnualEstablishment aer = null;
            for (int i = 0; i < partBgrpAList.size(); i++) {
                aer = (AnnualEstablishment) partBgrpAList.get(i);
                partBtotalGrpASanctionedStrength = partBtotalGrpASanctionedStrength + aer.getSanctionedStrength();
                partBtotalGrpAMenInPosition = partBtotalGrpAMenInPosition + aer.getMeninPosition();
                partBtotalGrpAVacancy = partBtotalGrpAVacancy + aer.getVacancyPosition();
            }
        }

        mav.addObject("partBtotalGrpASanctionedStrength", partBtotalGrpASanctionedStrength);
        mav.addObject("partBtotalGrpAMenInPosition", partBtotalGrpAMenInPosition);
        mav.addObject("partBtotalGrpAVacancy", partBtotalGrpAVacancy);

        partBgrpBList = annuaiEstablishmentDao.getAerViewForCOMultiple(ae.getFinancialYear(), lub.getLoginspc(), ae.getCoOffCode(), "B", "", "PART-B", roletype);

        if (partBgrpBList != null && partBgrpBList.size() > 0) {
            AnnualEstablishment aer = null;
            for (int i = 0; i < partBgrpBList.size(); i++) {
                aer = (AnnualEstablishment) partBgrpBList.get(i);
                partBTotalGrpBSanctionedStrength = partBTotalGrpBSanctionedStrength + aer.getSanctionedStrength();
                partBTotalGrpBMenInPosition = partBTotalGrpBMenInPosition + aer.getMeninPosition();
                partBTotalGrpBVacancy = partBTotalGrpBVacancy + aer.getVacancyPosition();
            }
        }

        mav.addObject("partBTotalGrpBSanctionedStrength", partBTotalGrpBSanctionedStrength);
        mav.addObject("partBTotalGrpBMenInPosition", partBTotalGrpBMenInPosition);
        mav.addObject("partBTotalGrpBVacancy", partBTotalGrpBVacancy);

        partBgrpCList = annuaiEstablishmentDao.getAerViewForCOMultiple(ae.getFinancialYear(), lub.getLoginspc(), ae.getCoOffCode(), "C", "", "PART-B", roletype);

        if (partBgrpCList != null && partBgrpCList.size() > 0) {
            AnnualEstablishment aer = null;
            for (int i = 0; i < partBgrpCList.size(); i++) {
                aer = (AnnualEstablishment) partBgrpCList.get(i);
                partBTotalGrpCSanctionedStrength = partBTotalGrpCSanctionedStrength + aer.getSanctionedStrength();
                partBTotalGrpCMenInPosition = partBTotalGrpCMenInPosition + aer.getMeninPosition();
                partBTotalGrpCVacancy = partBTotalGrpCVacancy + aer.getVacancyPosition();
            }
        }

        mav.addObject("partBTotalGrpCSanctionedStrength", partBTotalGrpCSanctionedStrength);
        mav.addObject("partBTotalGrpCMenInPosition", partBTotalGrpCMenInPosition);
        mav.addObject("partBTotalGrpCVacancy", partBTotalGrpCVacancy);

        partBgrpDList = annuaiEstablishmentDao.getAerViewForCOMultiple(ae.getFinancialYear(), lub.getLoginspc(), ae.getCoOffCode(), "D", "", "PART-B", roletype);

        if (partBgrpDList != null && partBgrpDList.size() > 0) {
            AnnualEstablishment aer = null;
            for (int i = 0; i < partBgrpDList.size(); i++) {
                aer = (AnnualEstablishment) partBgrpDList.get(i);
                partBTotalGrpDSanctionedStrength = partBTotalGrpDSanctionedStrength + aer.getSanctionedStrength();
                partBTotalGrpDMenInPosition = partBTotalGrpDMenInPosition + aer.getMeninPosition();
                partBTotalGrpDVacancy = partBTotalGrpDVacancy + aer.getVacancyPosition();
            }
        }

        mav.addObject("partBTotalGrpDSanctionedStrength", partBTotalGrpDSanctionedStrength);
        mav.addObject("partBTotalGrpDMenInPosition", partBTotalGrpDMenInPosition);
        mav.addObject("partBTotalGrpDVacancy", partBTotalGrpDVacancy);

        grpPartCList = annuaiEstablishmentDao.getAerViewForCOMultiple(ae.getFinancialYear(), lub.getLoginspc(), ae.getCoOffCode(), "D", "", "PART-C", roletype);

        mav.addObject("PartAGroupAlist", grpAList);
        mav.addObject("partAGrpAAllIndiaService", partAGrpAAllIndiaService);
        mav.addObject("partAGrpAUGC", partAGrpAUGC);
        mav.addObject("partAJudiciary", partAJudiciary);
        mav.addObject("PartAGroupBlist", grpBList);
        mav.addObject("PartAGroupClist", grpCList);
        mav.addObject("PartAGroupDlist", grpDList);

        mav.addObject("partBgrpAList", partBgrpAList);
        mav.addObject("partBGrpAAllIndiaService", partBGrpAAllIndiaService);
        mav.addObject("partBGrpAUGC", partBGrpAUGC);
        mav.addObject("partBJudiciary", partBJudiciary);
        mav.addObject("partBgrpBList", partBgrpBList);
        mav.addObject("partBgrpCList", partBgrpCList);
        mav.addObject("partBgrpDList", partBgrpDList);

        mav.addObject("PartCGrouplist", grpPartCList);

        AnnualEstablishment[] partDdetails = annuaiEstablishmentDao.getAerViewOtherPartForCOMultiple(ae.getFinancialYear(), lub.getLoginspc(), "D", roletype, ae.getCoOffCode());
        mav.addObject("aeDdetails", partDdetails);

        AnnualEstablishment[] partEdetails = annuaiEstablishmentDao.getAerViewOtherPartForCOMultiple(ae.getFinancialYear(), lub.getLoginspc(), "E", roletype, ae.getCoOffCode());
        mav.addObject("aeEdetails", partEdetails);

        AnnualEstablishment[] giaList = annuaiEstablishmentDao.getAerViewOtherPartForCOMultiple(ae.getFinancialYear(), lub.getLoginspc(), "B", roletype, ae.getCoOffCode());
        mav.addObject("giaList", giaList);

        List giaArrList = Arrays.asList(giaList);

        if (giaArrList != null && giaArrList.size() > 0) {
            AnnualEstablishment aer = null;
            for (int i = 0; i < giaArrList.size(); i++) {
                aer = (AnnualEstablishment) giaArrList.get(i);
                giaTotalSanctionedStrength = giaTotalSanctionedStrength + aer.getOtherSS();
                giaTotalMenInPosition = giaTotalMenInPosition + aer.getMeninPosition();
                giaTotalVacancy = giaTotalVacancy + aer.getOtherVacancy();
            }
        }
        mav.addObject("giaTotalSanctionedStrength", giaTotalSanctionedStrength);
        mav.addObject("giaTotalMenInPosition", giaTotalMenInPosition);
        mav.addObject("giaTotalVacancy", giaTotalVacancy);

        mav.addObject("pendingApproveCount", annuaiEstablishmentDao.getCOAuthorityApprovedOperatorList(lub.getLoginoffcode(), ae.getFinancialYear(), roletype, lub.getLoginspc()));

        ae.setCoOffCode(ae.getCoOffCode());

        return mav;

    }

    @RequestMapping(value = "displayAERlistForControllingAuthority.htm", params = {"btnAer=SubmitToAcceptor"})
    public ModelAndView submitAERlistToAcceptor(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") AnnualEstablishment ae) {
        ModelAndView mav = null;

        mav = new ModelAndView("report/AnnualEstablishmentReportViewForCO", "command", ae);
        String aoOffCode = "";

        String cooffCode = "";
        cooffCode = annuaiEstablishmentDao.getcoOffCodeasVerifier(lub.getLoginempid(), ae.getFinancialYear());
        aoOffCode = annuaiEstablishmentDao.getAoOffCode(lub.getLogindeptcode());

        int coAerId = annuaiEstablishmentDao.submitAerReportAsVerifier(lub.getLoginempid(), lub.getLoginspc(), cooffCode, aoOffCode, ae.getFinancialYear());

        annuaiEstablishmentDao.updateAerCOAerId(lub.getLoginempid(), lub.getLoginspc(), cooffCode, ae.getFinancialYear(), coAerId);

        /*===============================================================  PART-A =============================================================*/
        annuaiEstablishmentDao.insertConsolidatedDataForAOPartABFORGIAandC(coAerId, ae.getFinancialYear(), lub.getLoginspc(), lub.getLoginoffcode(), "AIS", "A", "PART-A");
        annuaiEstablishmentDao.insertConsolidatedDataForAOPartABFORGIAandC(coAerId, ae.getFinancialYear(), lub.getLoginspc(), lub.getLoginoffcode(), "UGC", "A", "PART-A");
        annuaiEstablishmentDao.insertConsolidatedDataForAOPartABFORGIAandC(coAerId, ae.getFinancialYear(), lub.getLoginspc(), lub.getLoginoffcode(), "OJS", "A", "PART-A");
        annuaiEstablishmentDao.insertConsolidatedDataForAOPartABFORGIAandC(coAerId, ae.getFinancialYear(), lub.getLoginspc(), lub.getLoginoffcode(), "", "A", "PART-A");
        annuaiEstablishmentDao.insertConsolidatedDataForAOPartABFORGIAandC(coAerId, ae.getFinancialYear(), lub.getLoginspc(), lub.getLoginoffcode(), "", "B", "PART-A");
        annuaiEstablishmentDao.insertConsolidatedDataForAOPartABFORGIAandC(coAerId, ae.getFinancialYear(), lub.getLoginspc(), lub.getLoginoffcode(), "", "C", "PART-A");
        annuaiEstablishmentDao.insertConsolidatedDataForAOPartABFORGIAandC(coAerId, ae.getFinancialYear(), lub.getLoginspc(), lub.getLoginoffcode(), "", "D", "PART-A");
        /*===============================================================  PART-B for GIA wihtin Office =============================================================*/
        annuaiEstablishmentDao.insertConsolidatedDataForAOPartABFORGIAandC(coAerId, ae.getFinancialYear(), lub.getLoginspc(), lub.getLoginoffcode(), "AIS", "A", "PART-B");
        annuaiEstablishmentDao.insertConsolidatedDataForAOPartABFORGIAandC(coAerId, ae.getFinancialYear(), lub.getLoginspc(), lub.getLoginoffcode(), "UGC", "A", "PART-B");
        annuaiEstablishmentDao.insertConsolidatedDataForAOPartABFORGIAandC(coAerId, ae.getFinancialYear(), lub.getLoginspc(), lub.getLoginoffcode(), "OJS", "A", "PART-B");
        annuaiEstablishmentDao.insertConsolidatedDataForAOPartABFORGIAandC(coAerId, ae.getFinancialYear(), lub.getLoginspc(), lub.getLoginoffcode(), "", "A", "PART-B");
        annuaiEstablishmentDao.insertConsolidatedDataForAOPartABFORGIAandC(coAerId, ae.getFinancialYear(), lub.getLoginspc(), lub.getLoginoffcode(), "", "B", "PART-B");
        annuaiEstablishmentDao.insertConsolidatedDataForAOPartABFORGIAandC(coAerId, ae.getFinancialYear(), lub.getLoginspc(), lub.getLoginoffcode(), "", "C", "PART-B");
        annuaiEstablishmentDao.insertConsolidatedDataForAOPartABFORGIAandC(coAerId, ae.getFinancialYear(), lub.getLoginspc(), lub.getLoginoffcode(), "", "D", "PART-B");

        /*===============================================================  PART-B =============================================================*/
        annuaiEstablishmentDao.insertConsolidatedDataForAOPartBDE(ae.getFinancialYear(), lub.getLoginspc(), "B", coAerId, lub.getLoginoffcode());
        /*===============================================================  PART-C =============================================================*/
        annuaiEstablishmentDao.insertConsolidatedDataForAOPartABFORGIAandC(coAerId, ae.getFinancialYear(), lub.getLoginspc(), lub.getLoginoffcode(), "", "D", "PART-C");

        /*===============================================================  PART-D =============================================================*/
        annuaiEstablishmentDao.insertConsolidatedDataForAOPartBDE(ae.getFinancialYear(), lub.getLoginspc(), "D", coAerId, lub.getLoginoffcode());
        /*===============================================================  PART-E =============================================================*/
        annuaiEstablishmentDao.insertConsolidatedDataForAOPartBDE(ae.getFinancialYear(), lub.getLoginspc(), "E", coAerId, lub.getLoginoffcode());

        return mav;

    }

    @RequestMapping(value = "displayAERlistForControllingAuthority.htm", params = {"btnAer=SubmitToAcceptorMultipleCO"})
    public ModelAndView submitAERlistToAcceptorMultipleCO(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") AnnualEstablishment ae) {

        ModelAndView mav = null;

        mav = new ModelAndView("report/AnnualEstablishmentReportViewForCO", "command", ae);
        String aoOffCode = "";

        String cooffCode = ae.getCoOffCode();

        aoOffCode = annuaiEstablishmentDao.getAoOffCode(lub.getLogindeptcode());

        int coAerId = annuaiEstablishmentDao.submitAerReportAsVerifier(lub.getLoginempid(), lub.getLoginspc(), cooffCode, aoOffCode, ae.getFinancialYear());

        annuaiEstablishmentDao.updateAerCOAerId(lub.getLoginempid(), lub.getLoginspc(), cooffCode, ae.getFinancialYear(), coAerId);

        /*===============================================================  PART-A =============================================================*/
        annuaiEstablishmentDao.insertConsolidatedDataForAOPartABFORGIAandCMultipleCO(coAerId, ae.getFinancialYear(), lub.getLoginspc(), cooffCode, "AIS", "A", "PART-A");
        annuaiEstablishmentDao.insertConsolidatedDataForAOPartABFORGIAandCMultipleCO(coAerId, ae.getFinancialYear(), lub.getLoginspc(), cooffCode, "UGC", "A", "PART-A");
        annuaiEstablishmentDao.insertConsolidatedDataForAOPartABFORGIAandCMultipleCO(coAerId, ae.getFinancialYear(), lub.getLoginspc(), cooffCode, "OJS", "A", "PART-A");
        annuaiEstablishmentDao.insertConsolidatedDataForAOPartABFORGIAandCMultipleCO(coAerId, ae.getFinancialYear(), lub.getLoginspc(), cooffCode, "", "A", "PART-A");
        annuaiEstablishmentDao.insertConsolidatedDataForAOPartABFORGIAandCMultipleCO(coAerId, ae.getFinancialYear(), lub.getLoginspc(), cooffCode, "", "B", "PART-A");
        annuaiEstablishmentDao.insertConsolidatedDataForAOPartABFORGIAandCMultipleCO(coAerId, ae.getFinancialYear(), lub.getLoginspc(), cooffCode, "", "C", "PART-A");
        annuaiEstablishmentDao.insertConsolidatedDataForAOPartABFORGIAandCMultipleCO(coAerId, ae.getFinancialYear(), lub.getLoginspc(), cooffCode, "", "D", "PART-A");
        /*===============================================================  PART-B for GIA wihtin Office =============================================================*/
        annuaiEstablishmentDao.insertConsolidatedDataForAOPartABFORGIAandCMultipleCO(coAerId, ae.getFinancialYear(), lub.getLoginspc(), cooffCode, "AIS", "A", "PART-B");
        annuaiEstablishmentDao.insertConsolidatedDataForAOPartABFORGIAandCMultipleCO(coAerId, ae.getFinancialYear(), lub.getLoginspc(), cooffCode, "UGC", "A", "PART-B");
        annuaiEstablishmentDao.insertConsolidatedDataForAOPartABFORGIAandCMultipleCO(coAerId, ae.getFinancialYear(), lub.getLoginspc(), cooffCode, "OJS", "A", "PART-B");
        annuaiEstablishmentDao.insertConsolidatedDataForAOPartABFORGIAandCMultipleCO(coAerId, ae.getFinancialYear(), lub.getLoginspc(), cooffCode, "", "A", "PART-B");
        annuaiEstablishmentDao.insertConsolidatedDataForAOPartABFORGIAandCMultipleCO(coAerId, ae.getFinancialYear(), lub.getLoginspc(), cooffCode, "", "B", "PART-B");
        annuaiEstablishmentDao.insertConsolidatedDataForAOPartABFORGIAandCMultipleCO(coAerId, ae.getFinancialYear(), lub.getLoginspc(), cooffCode, "", "C", "PART-B");
        annuaiEstablishmentDao.insertConsolidatedDataForAOPartABFORGIAandCMultipleCO(coAerId, ae.getFinancialYear(), lub.getLoginspc(), cooffCode, "", "D", "PART-B");

        /*===============================================================  PART-B =============================================================*/
        annuaiEstablishmentDao.insertConsolidatedDataForAOPartBDEMultipleCO(ae.getFinancialYear(), lub.getLoginspc(), "B", coAerId, cooffCode);
        /*===============================================================  PART-C =============================================================*/
        annuaiEstablishmentDao.insertConsolidatedDataForAOPartABFORGIAandCMultipleCO(coAerId, ae.getFinancialYear(), lub.getLoginspc(), cooffCode, "", "D", "PART-C");

        /*===============================================================  PART-D =============================================================*/
        annuaiEstablishmentDao.insertConsolidatedDataForAOPartBDEMultipleCO(ae.getFinancialYear(), lub.getLoginspc(), "D", coAerId, cooffCode);
        /*===============================================================  PART-E =============================================================*/
        annuaiEstablishmentDao.insertConsolidatedDataForAOPartBDEMultipleCO(ae.getFinancialYear(), lub.getLoginspc(), "E", coAerId, cooffCode);

        return mav;

    }

    @RequestMapping(value = "displayAERlistForControllingAuthority.htm", params = {"btnAer=Back"})
    public ModelAndView back2displayAERlistForControllingAuthority(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") AnnualEstablishment ae) {
        ModelAndView mav = null;

        mav = new ModelAndView("redirect:/displayAERlistForControllingAuthority.htm?btnAer=Search");

        return mav;

    }

    @RequestMapping(value = "displayAERlistForAdministrativeAuthority.htm", params = {"btnAer=GetAOData"})
    public ModelAndView displayAERlistForAdministrativeAuthorityMultipleAOOffice(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") AnnualEstablishment ae) {

        ModelAndView mav = null;

        mav = new ModelAndView("report/AnnualEstablishmentReportForAO", "command", ae);

        List fiscyear = fiscalDAO.getFiscalYearList();

        List aoofficelist = annuaiEstablishmentDao.getAcceptorAOOfficeList(lub.getLoginempid(), lub.getLoginspc(), ae.getFinancialYear());
        if (aoofficelist != null && aoofficelist.size() > 1) {
            mav = new ModelAndView("report/AnnualEstablishmentReportAOOfficeData", "command", ae);
            mav.addObject("aoofficelist", aoofficelist);
            mav.addObject("selectedFiscalYear", ae.getFinancialYear());
            mav.addObject("fiscyear", fiscyear);
        } else {
            mav = new ModelAndView("redirect:/displayAERlistForAdministrativeAuthority.htm?btnAer=Search&financialYear=" + ae.getFinancialYear());
        }

        mav.addObject("OffName", lub.getLoginoffname());

        return mav;
    }

    @RequestMapping(value = "displayAERlistForAdministrativeAuthority.htm", params = {"btnAer=Search"})
    public ModelAndView displayAERlistForAdministrativeAuthority(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") AnnualEstablishment ae) {

        ModelAndView mav = null;

        mav = new ModelAndView("report/AnnualEstablishmentReportForAO", "command", ae);

        List<AnnualEstablishment> submitList = null;

        List fylist = annuaiEstablishmentDao.getFinancialYearList();
        mav.addObject("financialYearList", fylist);

        List fiscyear = fiscalDAO.getFiscalYearList();
        mav.addObject("fiscyear", fiscyear);

        String aooffcode = "";
        if (ae.getAoOffCode() != null && !ae.getAoOffCode().equals("")) {
            aooffcode = ae.getAoOffCode();
            ae.setAoOffCode(ae.getAoOffCode());
        } else {
            aooffcode = annuaiEstablishmentDao.getAcceptorAssignedOffice(lub.getLoginempid(), lub.getLoginspc());
        }

        int aoAerId = 0;

        String roleType = annuaiEstablishmentDao.getRoleTypeForReviewerAndVerifierOfAO(lub.getLoginempid(), lub.getLoginspc(), ae.getFinancialYear(), aooffcode);

        if (ae.getAoOffCode() != null && !ae.getAoOffCode().equals("")) {
            submitList = annuaiEstablishmentDao.getAerReportListFinancialYearWiseForMultipleAOLevel(aooffcode, ae.getFinancialYear(), roleType, lub.getLoginspc());
            aoAerId = annuaiEstablishmentDao.getSchedule3AerIdMultipleAO(lub.getLoginspc(), lub.getLoginempid(), ae.getFinancialYear(), aooffcode);
        } else {
            submitList = annuaiEstablishmentDao.getAerReportListFinancialYearWiseForAOLevel(lub.getLoginoffcode(), ae.getFinancialYear(), roleType, lub.getLoginspc());
            aoAerId = annuaiEstablishmentDao.getSchedule3AerId(lub.getLoginspc(), lub.getLoginempid(), ae.getFinancialYear());
        }

        mav.addObject("aoAerId", aoAerId);
        mav.addObject("EstablishmentList", submitList);
        mav.addObject("OffName", lub.getLoginoffname());

        return mav;
    }

    @RequestMapping(value = "displayAERlistForAdministrativeAuthority", params = {"btnAer=Approve"})
    public String approveAERlistForAdministrativeAuthority(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") AnnualEstablishment ae) {

        String path = "redirect:/displayAERlistForAdministrativeAuthority.htm?btnAer=Search";

        String aooffcode = annuaiEstablishmentDao.getAcceptorAssignedOffice(lub.getLoginempid(), lub.getLoginspc());
        if (ae.getAoOffCode() != null && !ae.getAoOffCode().equals("")) {
            aooffcode = ae.getAoOffCode();
            path = "redirect:/displayAERlistForAdministrativeAuthority.htm?btnAer=Search&aoOffCode=" + aooffcode;
        }

        String roleType = annuaiEstablishmentDao.getRoleTypeForReviewerAndVerifierOfAO(lub.getLoginempid(), lub.getLoginspc(), ae.getFinancialYear(), aooffcode);

        annuaiEstablishmentDao.approveAER(ae.getAerId(), lub.getLoginempid(), lub.getLoginspc(), lub.getLoginoffcode(), ae.getHidOffCode(), roleType);

        return path;
    }

    @RequestMapping(value = "displayAERlistForAdministrativeAuthority", params = {"btnAer=DisApprove"})
    public String disApproveAERlistForAdministrativeAuthority(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") AnnualEstablishment ae) {

        String path = "redirect:/displayAERlistForAdministrativeAuthority.htm?btnAer=Search";

        String aooffcode = annuaiEstablishmentDao.getAcceptorAssignedOffice(lub.getLoginempid(), lub.getLoginspc());

        String roleType = annuaiEstablishmentDao.getRoleTypeForReviewerAndVerifierOfAODisapprove(lub.getLoginempid(), lub.getLoginspc(), ae.getFinancialYear(), aooffcode);
        System.out.println("roleType is: " + roleType);
        if (roleType.contains("ACCEPTOR") && roleType.contains("DREVIEWER") && roleType.contains("DVERIFIER")) {
            roleType = "AOTR";
        } else if (roleType.contains("ACCEPTOR") && roleType.contains("DREVIEWER") && !roleType.contains("DVERIFIER")) {
            roleType = "AOBO";
        } else if (roleType.contains("ACCEPTOR") && roleType.contains("DVERIFIER") && !roleType.contains("DREVIEWER")) {
            roleType = "AOBO";
        } else if (!roleType.contains("ACCEPTOR") && roleType.contains("DREVIEWER") && roleType.contains("DVERIFIER")) {
            roleType = "AOBO";
        }
        System.out.println("roleType is: " + roleType);
        annuaiEstablishmentDao.disApproveAER(ae.getAerId(), lub.getLoginempid(), lub.getLoginspc(), lub.getLoginoffcode(), ae.getHidOffCode(), roleType);

        return path;
    }

    @RequestMapping(value = "DeptWiseAERStatus")
    public ModelAndView DeptWiseAERStatus(@ModelAttribute("command") AnnualEstablishment ae) {
        String path = "/misreport/DeptWiseAERStatus";
        ModelAndView mav = new ModelAndView();
        ArrayList AERDetails = annuaiEstablishmentDao.DeptWiseAERStatus(ae.getFinancialYear());
        mav.addObject("AERDetails", AERDetails);
        mav.addObject("fiscalyear", ae.getFinancialYear());
        mav.setViewName(path);
        return mav;
    }

    @RequestMapping(value = "DDOWiseAERStatus")
    public ModelAndView DDOWiseAERStatus(ModelMap model, HttpServletResponse response, @RequestParam("dcode") String dcode) {
        String path = "/misreport/DDOWiseAERStatus";
        ModelAndView mav = new ModelAndView();
        ArrayList AERDetails = annuaiEstablishmentDao.DDOWiseAERStatus(dcode);
        mav.addObject("AERDetails", AERDetails);
        mav.setViewName(path);
        return mav;
    }

    @RequestMapping(value = "DistWiseAERReport")
    public ModelAndView DistWiseAERReport(@ModelAttribute("command") AnnualEstablishment ae) {
        String path = "/misreport/DistWiseAERReport";
        ModelAndView mav = new ModelAndView();
        ArrayList aerDetails = annuaiEstablishmentDao.DistWiseAERReport(ae.getFinancialYear());
        mav.addObject("AERDetails", aerDetails);
        mav.addObject("fiscalyear", ae.getFinancialYear());
        // return "/misreport/DistWiseAERReport";
        mav.setViewName(path);
        return mav;

    }

    @RequestMapping(value = "DistWiseOfficeAERReport")
    public ModelAndView DistWiseAERReport(ModelMap model, HttpServletResponse response, @RequestParam("distcode") String distcode) {
        String path = "/misreport/DistWiseOfficeAERReport";
        ModelAndView mav = new ModelAndView();
        ArrayList aerDetails = annuaiEstablishmentDao.DistWiseOfficeAERReport(distcode);
        mav.addObject("AERDetails", aerDetails);
        mav.setViewName(path);
        return mav;

    }

    @RequestMapping(value = "viewAERProcessUserNameList")
    public ModelAndView viewAERProcessUserNameList(@RequestParam("aerId") int aerId) {

        ModelAndView mv = new ModelAndView();

        try {
            ArrayList userlist = annuaiEstablishmentDao.getAERProcessUserNameList(aerId);
            mv.setViewName("report/ShowAERUserList");
            mv.addObject("userlist", userlist);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return mv;
    }

    @RequestMapping(value = "getGranteeDetail.htm")
    public ModelAndView getGranteeDetail(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("officeModel") Office office, Map<String, Object> model, @RequestParam Map<String, String> param) {

        String officecode = office.getOffCode();
        office.setOffStatus("GA");
        office.setDeptCode(lub.getLogindeptcode());
        office.setAerId(Integer.parseInt(param.get("aerId")));
        office.setDeptName(deptDAO.getDeptName(lub.getLogindeptcode()));
        if (officecode != null && !officecode.equals("")) {

            String mode = office.getMode();
            office = officeDao.getOfficeDetails(office.getOffCode());
            office.setAerId(Integer.parseInt(param.get("aerId")));
            Block block = blockDAO.getBlockDetails(office.getBlockCode());
            office.setBlockName(block.getBlockName());
            State state = stateDAO.getStateDetails(office.getStateCode());
            office.setStateName(state.getStatename());
            SubDivision subDivision = subDivisionDAO.getSubDivisionDetail(office.getSubDivisionCode());
            office.setSubDivisionName(subDivision.getSubDivisionName());
            office.setMode(mode);
            ModelAndView mv = new ModelAndView("/report/GranteeOfficeDetail", "officeModel", office);

            mv.addObject("blockList", blockDAO.getAllBlockList());
            mv.addObject("bankList", bankDAO.getBankList());
            mv.addObject("branchList", branchDAO.getBranchList(office.getSltBank()));
            mv.addObject("treasuryList", treasuryDao.getTreasuryList());
            mv.addObject("subdivisionList", subDivisionDAO.getSubDivisionList());
            mv.addObject("districtList", districtDAO.getDistrictList());
            mv.addObject("departmentList", deptDAO.getDepartmentList());
            mv.addObject("parentOffList", officeDao.getParentOffice(office.getPoffCode()));
            mv.addObject("employeeList", employeeDAO.getDDOEmployeeList(office.getOffCode()));
            mv.addObject("employeePostCode", employeeDAO.getEmployeePostCode(office.getDdoHrmsid()));
            mv.addObject("aerId", office.getAerId());
            return mv;
        } else {
            ModelAndView mv = new ModelAndView();

            mv.addObject("bankList", bankDAO.getBankList());
            mv.addObject("branchList", branchDAO.getBranchList(office.getSltBank()));
            mv.addObject("treasuryList", treasuryDao.getTreasuryList());
            mv.addObject("subdivisionList", subDivisionDAO.getSubDivisionList());
            mv.addObject("districtList", districtDAO.getDistrictList());
            mv.addObject("departmentList", deptDAO.getDepartmentList());
            mv.addObject("blockList", blockDAO.getAllBlockList());
            mv.addObject("parentOffList", officeDao.getParentOffice(office.getPoffCode()));
            mv.addObject("employeeList", employeeDAO.getDDOEmployeeList(office.getOffCode()));
            mv.addObject("employeePostCode", employeeDAO.getEmployeePostCode(office.getDdoHrmsid()));
            mv.addObject("aerId", Integer.parseInt(param.get("aerId")));
            mv.setViewName("/report/GranteeOfficeDetail");
            return mv;
        }
    }

    @RequestMapping(value = "addGranteeDetail.htm")
    public ModelAndView saveGranteeOfficeDetail(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("officeModel") Office office) {

        if (office.getOffCode() != null && !office.getOffCode().equals("")) {
            officeDao.updateGranteeOfficeDetails(office);
        } else {
            office.setPoffCode(lub.getLoginoffcode());
            officeDao.saveGranteeOfficeDetails(office);
        }

        return new ModelAndView("redirect:/aerReportPartB.htm?aerId=" + office.getAerId());
    }

    @RequestMapping(value = "COWiseAERStatus")
    public ModelAndView COWiseAERStatus(ModelMap model, HttpServletResponse response) {
        String path = "/misreport/COWiseAERStatus";
        ModelAndView mav = new ModelAndView();
        ArrayList AERDetails = annuaiEstablishmentDao.COWiseAERStatus();
        mav.addObject("AERDetails", AERDetails);
        mav.setViewName(path);
        return mav;
    }

    @RequestMapping(value = "viewAERMappedBillGroupList")
    public ModelAndView viewAERMappedBillGroupList(@RequestParam("aerId") int aerId) {

        ModelAndView mv = new ModelAndView();

        try {
            ArrayList mappedbillgrouplist = annuaiEstablishmentDao.getAERBillGroupNameList(aerId);

            mv.addObject("aerId", aerId);
            mv.addObject("mappedbillgrouplist", mappedbillgrouplist);

            mv.setViewName("report/ShowAERMappedBillGroup");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mv;
    }

    @RequestMapping(value = "deleteAERBillGroupList")
    public ModelAndView deleteAERBillGroupList(@RequestParam("aerId") int aerId, @RequestParam("billgroupid") BigDecimal billgroupid) {

        ModelAndView mv = new ModelAndView();

        try {
            annuaiEstablishmentDao.deleteAERBillGroupNameList(aerId, billgroupid);

            return new ModelAndView("redirect:/displayAERlist.htm");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mv;
    }

    @RequestMapping(value = "TreasuryWiseAERStatus")
    public ModelAndView TreasuryWiseAERStatus(@ModelAttribute("command") AnnualEstablishment ae) {
        String path = "/misreport/TreasuryWiseAERStatus";
        ModelAndView mav = new ModelAndView();
        ArrayList AERDetails = annuaiEstablishmentDao.TreasuryWiseAERStatus(ae.getFinancialYear());
        mav.addObject("AERDetails", AERDetails);
        mav.addObject("fiscalyear", ae.getFinancialYear());
        mav.setViewName(path);
        return mav;
    }

    @RequestMapping(value = "TreasuryWiseOfficeAERReport")
    public ModelAndView TreasuryWiseOfficeAERReport(ModelMap model, HttpServletResponse response, @RequestParam("trCode") String trCode) {
        String path = "/misreport/TreasuryWiseOfficeAERReport";
        ModelAndView mav = new ModelAndView();
        ArrayList aerDetails = annuaiEstablishmentDao.TreasuryWiseOfficeAERReport(trCode);
        mav.addObject("AERDetails", aerDetails);
        mav.setViewName(path);
        return mav;

    }

    @RequestMapping(value = "displayAERlistForAdministrativeAuthority.htm", params = {"btnAer=View"})
    public ModelAndView viewAERlistForAdministrativeAuthority(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") AnnualEstablishment ae) {
        ModelAndView mav = null;

        List partAGrpAAllIndiaService = new ArrayList();
        List partAGrpAUGC = new ArrayList();
        List partAJudiciary = new ArrayList();
        List grpAList = new ArrayList();
        List grpBList = new ArrayList();
        List grpCList = new ArrayList();
        List grpDList = new ArrayList();

        List partBGrpAAllIndiaService = new ArrayList();
        List partBGrpAUGC = new ArrayList();
        List partBJudiciary = new ArrayList();
        List partBgrpAList = new ArrayList();
        List partBgrpBList = new ArrayList();
        List partBgrpCList = new ArrayList();
        List partBgrpDList = new ArrayList();

        List grpPartCList = new ArrayList();

        int partAtotalGrpASanctionedStrength = 0;
        int partAtotalGrpAMenInPosition = 0;
        int partAtotalGrpAVacancy = 0;

        int partATotalGrpBSanctionedStrength = 0;
        int partATotalGrpBMenInPosition = 0;
        int partATotalGrpBVacancy = 0;

        int partATotalGrpCSanctionedStrength = 0;
        int partATotalGrpCMenInPosition = 0;
        int partATotalGrpCVacancy = 0;

        int partATotalGrpDSanctionedStrength = 0;
        int partATotalGrpDMenInPosition = 0;
        int partATotalGrpDVacancy = 0;

        int partBtotalGrpASanctionedStrength = 0;
        int partBtotalGrpAMenInPosition = 0;
        int partBtotalGrpAVacancy = 0;

        int partBTotalGrpBSanctionedStrength = 0;
        int partBTotalGrpBMenInPosition = 0;
        int partBTotalGrpBVacancy = 0;

        int partBTotalGrpCSanctionedStrength = 0;
        int partBTotalGrpCMenInPosition = 0;
        int partBTotalGrpCVacancy = 0;

        int partBTotalGrpDSanctionedStrength = 0;
        int partBTotalGrpDMenInPosition = 0;
        int partBTotalGrpDVacancy = 0;

        int giaTotalSanctionedStrength = 0;
        int giaTotalMenInPosition = 0;
        int giaTotalVacancy = 0;

        mav = new ModelAndView("report/AnnualEstablishmentReportViewForAO", "command", ae);

        String aooffcode = annuaiEstablishmentDao.getAcceptorAssignedOffice(lub.getLoginempid(), lub.getLoginspc());

        String showsubmitbtn = "";
        showsubmitbtn = annuaiEstablishmentDao.getSubmitStatusForAcceptor(lub.getLoginempid(), lub.getLoginspc(), ae.getFinancialYear(), aooffcode);
        mav.addObject("showsubmitbtn", showsubmitbtn);

        String roletype = annuaiEstablishmentDao.getRoleTypeForReviewerAndVerifierOfAO(lub.getLoginempid(), lub.getLoginspc(), ae.getFinancialYear(), aooffcode);
        mav.addObject("roleType", roletype);
        if (roletype.equals("BO")) {
            roletype = "DREVIEWER";
        }

        partAGrpAAllIndiaService = annuaiEstablishmentDao.getAerViewForAO(ae.getFinancialYear(), lub.getLoginspc(), aooffcode, "A", "AIS", "PART-A", roletype);
        partAGrpAUGC = annuaiEstablishmentDao.getAerViewForAO(ae.getFinancialYear(), lub.getLoginspc(), aooffcode, "A", "UGC", "PART-A", roletype);
        partAJudiciary = annuaiEstablishmentDao.getAerViewForAO(ae.getFinancialYear(), lub.getLoginspc(), aooffcode, "A", "OJS", "PART-A", roletype);
        grpAList = annuaiEstablishmentDao.getAerViewForAO(ae.getFinancialYear(), lub.getLoginspc(), aooffcode, "A", "", "PART-A", roletype);

        if (partAGrpAAllIndiaService != null && partAGrpAAllIndiaService.size() > 0) {
            AnnualEstablishment aer = null;
            for (int i = 0; i < partAGrpAAllIndiaService.size(); i++) {
                aer = (AnnualEstablishment) partAGrpAAllIndiaService.get(i);
                partAtotalGrpASanctionedStrength = partAtotalGrpASanctionedStrength + aer.getSanctionedStrength();
                partAtotalGrpAMenInPosition = partAtotalGrpAMenInPosition + aer.getMeninPosition();
                partAtotalGrpAVacancy = partAtotalGrpAVacancy + aer.getVacancyPosition();
            }
        }
        if (partAGrpAUGC != null && partAGrpAUGC.size() > 0) {
            AnnualEstablishment aer = null;
            for (int i = 0; i < partAGrpAUGC.size(); i++) {
                aer = (AnnualEstablishment) partAGrpAUGC.get(i);
                partAtotalGrpASanctionedStrength = partAtotalGrpASanctionedStrength + aer.getSanctionedStrength();
                partAtotalGrpAMenInPosition = partAtotalGrpAMenInPosition + aer.getMeninPosition();
                partAtotalGrpAVacancy = partAtotalGrpAVacancy + aer.getVacancyPosition();
            }
        }
        if (partAJudiciary != null && partAJudiciary.size() > 0) {
            AnnualEstablishment aer = null;
            for (int i = 0; i < partAJudiciary.size(); i++) {
                aer = (AnnualEstablishment) partAJudiciary.get(i);
                partAtotalGrpASanctionedStrength = partAtotalGrpASanctionedStrength + aer.getSanctionedStrength();
                partAtotalGrpAMenInPosition = partAtotalGrpAMenInPosition + aer.getMeninPosition();
                partAtotalGrpAVacancy = partAtotalGrpAVacancy + aer.getVacancyPosition();
            }
        }
        if (grpAList != null && grpAList.size() > 0) {
            AnnualEstablishment aer = null;
            for (int i = 0; i < grpAList.size(); i++) {
                aer = (AnnualEstablishment) grpAList.get(i);
                partAtotalGrpASanctionedStrength = partAtotalGrpASanctionedStrength + aer.getSanctionedStrength();
                partAtotalGrpAMenInPosition = partAtotalGrpAMenInPosition + aer.getMeninPosition();
                partAtotalGrpAVacancy = partAtotalGrpAVacancy + aer.getVacancyPosition();
            }
        }

        mav.addObject("partAtotalGrpASanctionedStrength", partAtotalGrpASanctionedStrength);
        mav.addObject("partAtotalGrpAMenInPosition", partAtotalGrpAMenInPosition);
        mav.addObject("partAtotalGrpAVacancy", partAtotalGrpAVacancy);

        grpBList = annuaiEstablishmentDao.getAerViewForAO(ae.getFinancialYear(), lub.getLoginspc(), aooffcode, "B", "", "PART-A", roletype);

        if (grpBList != null && grpBList.size() > 0) {
            AnnualEstablishment aer = null;
            for (int i = 0; i < grpBList.size(); i++) {
                aer = (AnnualEstablishment) grpBList.get(i);
                partATotalGrpBSanctionedStrength = partATotalGrpBSanctionedStrength + aer.getSanctionedStrength();
                partATotalGrpBMenInPosition = partATotalGrpBMenInPosition + aer.getMeninPosition();
                partATotalGrpBVacancy = partATotalGrpBVacancy + aer.getVacancyPosition();
            }
        }
        mav.addObject("partATotalGrpBSanctionedStrength", partATotalGrpBSanctionedStrength);
        mav.addObject("partATotalGrpBMenInPosition", partATotalGrpBMenInPosition);
        mav.addObject("partATotalGrpBVacancy", partATotalGrpBVacancy);

        grpCList = annuaiEstablishmentDao.getAerViewForAO(ae.getFinancialYear(), lub.getLoginspc(), aooffcode, "C", "", "PART-A", roletype);

        if (grpCList != null && grpCList.size() > 0) {
            AnnualEstablishment aer = null;
            for (int i = 0; i < grpCList.size(); i++) {
                aer = (AnnualEstablishment) grpCList.get(i);
                partATotalGrpCSanctionedStrength = partATotalGrpCSanctionedStrength + aer.getSanctionedStrength();
                partATotalGrpCMenInPosition = partATotalGrpCMenInPosition + aer.getMeninPosition();
                partATotalGrpCVacancy = partATotalGrpCVacancy + aer.getVacancyPosition();
            }
        }
        mav.addObject("partATotalGrpCSanctionedStrength", partATotalGrpCSanctionedStrength);
        mav.addObject("partATotalGrpCMenInPosition", partATotalGrpCMenInPosition);
        mav.addObject("partATotalGrpCVacancy", partATotalGrpCVacancy);

        grpDList = annuaiEstablishmentDao.getAerViewForAO(ae.getFinancialYear(), lub.getLoginspc(), aooffcode, "D", "", "PART-A", roletype);

        if (grpDList != null && grpDList.size() > 0) {
            AnnualEstablishment aer = null;
            for (int i = 0; i < grpDList.size(); i++) {
                aer = (AnnualEstablishment) grpDList.get(i);
                partATotalGrpDSanctionedStrength = partATotalGrpDSanctionedStrength + aer.getSanctionedStrength();
                partATotalGrpDMenInPosition = partATotalGrpDMenInPosition + aer.getMeninPosition();
                partATotalGrpDVacancy = partATotalGrpDVacancy + aer.getVacancyPosition();
            }
        }
        mav.addObject("partATotalGrpDSanctionedStrength", partATotalGrpDSanctionedStrength);
        mav.addObject("partATotalGrpDMenInPosition", partATotalGrpDMenInPosition);
        mav.addObject("partATotalGrpDVacancy", partATotalGrpDVacancy);

        partBGrpAAllIndiaService = annuaiEstablishmentDao.getAerViewForAO(ae.getFinancialYear(), lub.getLoginspc(), aooffcode, "A", "AIS", "PART-B", roletype);
        partBGrpAUGC = annuaiEstablishmentDao.getAerViewForAO(ae.getFinancialYear(), lub.getLoginspc(), aooffcode, "A", "UGC", "PART-B", roletype);
        partBJudiciary = annuaiEstablishmentDao.getAerViewForAO(ae.getFinancialYear(), lub.getLoginspc(), aooffcode, "A", "OJS", "PART-B", roletype);
        partBgrpAList = annuaiEstablishmentDao.getAerViewForAO(ae.getFinancialYear(), lub.getLoginspc(), aooffcode, "A", "", "PART-B", roletype);

        if (partBGrpAAllIndiaService != null && partBGrpAAllIndiaService.size() > 0) {
            AnnualEstablishment aer = null;
            for (int i = 0; i < partBGrpAAllIndiaService.size(); i++) {
                aer = (AnnualEstablishment) partBGrpAAllIndiaService.get(i);
                partBtotalGrpASanctionedStrength = partBtotalGrpASanctionedStrength + aer.getSanctionedStrength();
                partBtotalGrpAMenInPosition = partBtotalGrpAMenInPosition + aer.getMeninPosition();
                partBtotalGrpAVacancy = partBtotalGrpAVacancy + aer.getVacancyPosition();
            }
        }
        if (partBGrpAUGC != null && partBGrpAUGC.size() > 0) {
            AnnualEstablishment aer = null;
            for (int i = 0; i < partBGrpAUGC.size(); i++) {
                aer = (AnnualEstablishment) partBGrpAUGC.get(i);
                partBtotalGrpASanctionedStrength = partBtotalGrpASanctionedStrength + aer.getSanctionedStrength();
                partBtotalGrpAMenInPosition = partBtotalGrpAMenInPosition + aer.getMeninPosition();
                partBtotalGrpAVacancy = partBtotalGrpAVacancy + aer.getVacancyPosition();
            }
        }
        if (partBJudiciary != null && partBJudiciary.size() > 0) {
            AnnualEstablishment aer = null;
            for (int i = 0; i < partBJudiciary.size(); i++) {
                aer = (AnnualEstablishment) partBJudiciary.get(i);
                partBtotalGrpASanctionedStrength = partBtotalGrpASanctionedStrength + aer.getSanctionedStrength();
                partBtotalGrpAMenInPosition = partBtotalGrpAMenInPosition + aer.getMeninPosition();
                partBtotalGrpAVacancy = partBtotalGrpAVacancy + aer.getVacancyPosition();
            }
        }
        if (partBgrpAList != null && partBgrpAList.size() > 0) {
            AnnualEstablishment aer = null;
            for (int i = 0; i < partBgrpAList.size(); i++) {
                aer = (AnnualEstablishment) partBgrpAList.get(i);
                partBtotalGrpASanctionedStrength = partBtotalGrpASanctionedStrength + aer.getSanctionedStrength();
                partBtotalGrpAMenInPosition = partBtotalGrpAMenInPosition + aer.getMeninPosition();
                partBtotalGrpAVacancy = partBtotalGrpAVacancy + aer.getVacancyPosition();
            }
        }
        mav.addObject("partBtotalGrpASanctionedStrength", partBtotalGrpASanctionedStrength);
        mav.addObject("partBtotalGrpAMenInPosition", partBtotalGrpAMenInPosition);
        mav.addObject("partBtotalGrpAVacancy", partBtotalGrpAVacancy);

        partBgrpBList = annuaiEstablishmentDao.getAerViewForAO(ae.getFinancialYear(), lub.getLoginspc(), aooffcode, "B", "", "PART-B", roletype);

        if (partBgrpBList != null && partBgrpBList.size() > 0) {
            AnnualEstablishment aer = null;
            for (int i = 0; i < partBgrpBList.size(); i++) {
                aer = (AnnualEstablishment) partBgrpBList.get(i);
                partBTotalGrpBSanctionedStrength = partBTotalGrpBSanctionedStrength + aer.getSanctionedStrength();
                partBTotalGrpBMenInPosition = partBTotalGrpBMenInPosition + aer.getMeninPosition();
                partBTotalGrpBVacancy = partBTotalGrpBVacancy + aer.getVacancyPosition();
            }
        }
        mav.addObject("partBTotalGrpBSanctionedStrength", partBTotalGrpBSanctionedStrength);
        mav.addObject("partBTotalGrpBMenInPosition", partBTotalGrpBMenInPosition);
        mav.addObject("partBTotalGrpBVacancy", partBTotalGrpBVacancy);

        partBgrpCList = annuaiEstablishmentDao.getAerViewForAO(ae.getFinancialYear(), lub.getLoginspc(), aooffcode, "C", "", "PART-B", roletype);

        if (partBgrpCList != null && partBgrpCList.size() > 0) {
            AnnualEstablishment aer = null;
            for (int i = 0; i < partBgrpCList.size(); i++) {
                aer = (AnnualEstablishment) partBgrpCList.get(i);
                partBTotalGrpCSanctionedStrength = partBTotalGrpCSanctionedStrength + aer.getSanctionedStrength();
                partBTotalGrpCMenInPosition = partBTotalGrpCMenInPosition + aer.getMeninPosition();
                partBTotalGrpCVacancy = partBTotalGrpCVacancy + aer.getVacancyPosition();
            }
        }
        mav.addObject("partBTotalGrpCSanctionedStrength", partBTotalGrpCSanctionedStrength);
        mav.addObject("partBTotalGrpCMenInPosition", partBTotalGrpCMenInPosition);
        mav.addObject("partBTotalGrpCVacancy", partBTotalGrpCVacancy);

        partBgrpDList = annuaiEstablishmentDao.getAerViewForAO(ae.getFinancialYear(), lub.getLoginspc(), aooffcode, "D", "", "PART-B", roletype);

        if (partBgrpDList != null && partBgrpDList.size() > 0) {
            AnnualEstablishment aer = null;
            for (int i = 0; i < partBgrpDList.size(); i++) {
                aer = (AnnualEstablishment) partBgrpDList.get(i);
                partBTotalGrpDSanctionedStrength = partBTotalGrpDSanctionedStrength + aer.getSanctionedStrength();
                partBTotalGrpDMenInPosition = partBTotalGrpDMenInPosition + aer.getMeninPosition();
                partBTotalGrpDVacancy = partBTotalGrpDVacancy + aer.getVacancyPosition();
            }
        }
        mav.addObject("partBTotalGrpDSanctionedStrength", partBTotalGrpDSanctionedStrength);
        mav.addObject("partBTotalGrpDMenInPosition", partBTotalGrpDMenInPosition);
        mav.addObject("partBTotalGrpDVacancy", partBTotalGrpDVacancy);

        grpPartCList = annuaiEstablishmentDao.getAerViewForAO(ae.getFinancialYear(), lub.getLoginspc(), aooffcode, "D", "", "PART-C", roletype);

        mav.addObject("PartAGroupAlist", grpAList);
        mav.addObject("partAGrpAAllIndiaService", partAGrpAAllIndiaService);
        mav.addObject("partAGrpAUGC", partAGrpAUGC);
        mav.addObject("partAJudiciary", partAJudiciary);
        mav.addObject("PartAGroupBlist", grpBList);
        mav.addObject("PartAGroupClist", grpCList);
        mav.addObject("PartAGroupDlist", grpDList);

        mav.addObject("partBgrpAList", partBgrpAList);
        mav.addObject("partBGrpAAllIndiaService", partBGrpAAllIndiaService);
        mav.addObject("partBGrpAUGC", partBGrpAUGC);
        mav.addObject("partBJudiciary", partBJudiciary);
        mav.addObject("partBgrpBList", partBgrpBList);
        mav.addObject("partBgrpCList", partBgrpCList);
        mav.addObject("partBgrpDList", partBgrpDList);

        mav.addObject("PartCGrouplist", grpPartCList);

        AnnualEstablishment[] partDdetails = annuaiEstablishmentDao.getAerViewOtherPartForCO(ae.getFinancialYear(), lub.getLoginspc(), "D", roletype);
        mav.addObject("aeDdetails", partDdetails);

        AnnualEstablishment[] partEdetails = annuaiEstablishmentDao.getAerViewOtherPartForCO(ae.getFinancialYear(), lub.getLoginspc(), "E", roletype);
        mav.addObject("aeEdetails", partEdetails);

        //AnnualEstablishment[] giaList = annuaiEstablishmentDao.getAerViewOtherPartForCO(ae.getFinancialYear(), lub.getLoginspc(), "B", roletype);
        AnnualEstablishment[] giaList = annuaiEstablishmentDao.getAerViewOtherPartForAO(ae.getFinancialYear(), lub.getLoginspc(), "B", roletype);
        mav.addObject("giaList", giaList);

        List giaArrList = Arrays.asList(giaList);

        if (giaArrList != null && giaArrList.size() > 0) {
            AnnualEstablishment aer = null;
            for (int i = 0; i < giaArrList.size(); i++) {
                aer = (AnnualEstablishment) giaArrList.get(i);
                giaTotalSanctionedStrength = giaTotalSanctionedStrength + aer.getOtherSS();
                giaTotalMenInPosition = giaTotalMenInPosition + aer.getMeninPosition();
                giaTotalVacancy = giaTotalVacancy + aer.getOtherVacancy();
            }
        }
        mav.addObject("giaTotalSanctionedStrength", giaTotalSanctionedStrength);
        mav.addObject("giaTotalMenInPosition", giaTotalMenInPosition);
        mav.addObject("giaTotalVacancy", giaTotalVacancy);

        return mav;

    }

    @RequestMapping(value = "displayAERlistForAdministrativeAuthority.htm", params = {"btnAer=ViewMultipleAO"})
    public ModelAndView viewAERlistForAdministrativeAuthorityMultipleAO(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") AnnualEstablishment ae) {
        ModelAndView mav = null;

        List partAGrpAAllIndiaService = new ArrayList();
        List partAGrpAUGC = new ArrayList();
        List partAJudiciary = new ArrayList();
        List grpAList = new ArrayList();
        List grpBList = new ArrayList();
        List grpCList = new ArrayList();
        List grpDList = new ArrayList();

        List partBGrpAAllIndiaService = new ArrayList();
        List partBGrpAUGC = new ArrayList();
        List partBJudiciary = new ArrayList();
        List partBgrpAList = new ArrayList();
        List partBgrpBList = new ArrayList();
        List partBgrpCList = new ArrayList();
        List partBgrpDList = new ArrayList();

        List grpPartCList = new ArrayList();

        int partAtotalGrpASanctionedStrength = 0;
        int partAtotalGrpAMenInPosition = 0;
        int partAtotalGrpAVacancy = 0;

        int partATotalGrpBSanctionedStrength = 0;
        int partATotalGrpBMenInPosition = 0;
        int partATotalGrpBVacancy = 0;

        int partATotalGrpCSanctionedStrength = 0;
        int partATotalGrpCMenInPosition = 0;
        int partATotalGrpCVacancy = 0;

        int partATotalGrpDSanctionedStrength = 0;
        int partATotalGrpDMenInPosition = 0;
        int partATotalGrpDVacancy = 0;

        int partBtotalGrpASanctionedStrength = 0;
        int partBtotalGrpAMenInPosition = 0;
        int partBtotalGrpAVacancy = 0;

        int partBTotalGrpBSanctionedStrength = 0;
        int partBTotalGrpBMenInPosition = 0;
        int partBTotalGrpBVacancy = 0;

        int partBTotalGrpCSanctionedStrength = 0;
        int partBTotalGrpCMenInPosition = 0;
        int partBTotalGrpCVacancy = 0;

        int partBTotalGrpDSanctionedStrength = 0;
        int partBTotalGrpDMenInPosition = 0;
        int partBTotalGrpDVacancy = 0;

        int giaTotalSanctionedStrength = 0;
        int giaTotalMenInPosition = 0;
        int giaTotalVacancy = 0;

        mav = new ModelAndView("report/AnnualEstablishmentReportViewForAO", "command", ae);

        String showsubmitbtn = "";
        showsubmitbtn = annuaiEstablishmentDao.getSubmitStatusForAcceptor(lub.getLoginempid(), lub.getLoginspc(), ae.getFinancialYear(), ae.getAoOffCode());
        mav.addObject("showsubmitbtn", showsubmitbtn);

        String roletype = annuaiEstablishmentDao.getRoleTypeForReviewerAndVerifierOfAO(lub.getLoginempid(), lub.getLoginspc(), ae.getFinancialYear(), ae.getAoOffCode());
        mav.addObject("roleType", roletype);
        if (roletype.equals("BO")) {
            roletype = "DREVIEWER";
        }

        partAGrpAAllIndiaService = annuaiEstablishmentDao.getAerViewForMultipleAO(ae.getFinancialYear(), lub.getLoginspc(), ae.getAoOffCode(), "A", "AIS", "PART-A", roletype);
        partAGrpAUGC = annuaiEstablishmentDao.getAerViewForMultipleAO(ae.getFinancialYear(), lub.getLoginspc(), ae.getAoOffCode(), "A", "UGC", "PART-A", roletype);
        partAJudiciary = annuaiEstablishmentDao.getAerViewForMultipleAO(ae.getFinancialYear(), lub.getLoginspc(), ae.getAoOffCode(), "A", "OJS", "PART-A", roletype);
        grpAList = annuaiEstablishmentDao.getAerViewForMultipleAO(ae.getFinancialYear(), lub.getLoginspc(), ae.getAoOffCode(), "A", "", "PART-A", roletype);

        if (partAGrpAAllIndiaService != null && partAGrpAAllIndiaService.size() > 0) {
            AnnualEstablishment aer = null;
            for (int i = 0; i < partAGrpAAllIndiaService.size(); i++) {
                aer = (AnnualEstablishment) partAGrpAAllIndiaService.get(i);
                partAtotalGrpASanctionedStrength = partAtotalGrpASanctionedStrength + aer.getSanctionedStrength();
                partAtotalGrpAMenInPosition = partAtotalGrpAMenInPosition + aer.getMeninPosition();
                partAtotalGrpAVacancy = partAtotalGrpAVacancy + aer.getVacancyPosition();
            }
        }
        if (partAGrpAUGC != null && partAGrpAUGC.size() > 0) {
            AnnualEstablishment aer = null;
            for (int i = 0; i < partAGrpAUGC.size(); i++) {
                aer = (AnnualEstablishment) partAGrpAUGC.get(i);
                partAtotalGrpASanctionedStrength = partAtotalGrpASanctionedStrength + aer.getSanctionedStrength();
                partAtotalGrpAMenInPosition = partAtotalGrpAMenInPosition + aer.getMeninPosition();
                partAtotalGrpAVacancy = partAtotalGrpAVacancy + aer.getVacancyPosition();
            }
        }
        if (partAJudiciary != null && partAJudiciary.size() > 0) {
            AnnualEstablishment aer = null;
            for (int i = 0; i < partAJudiciary.size(); i++) {
                aer = (AnnualEstablishment) partAJudiciary.get(i);
                partAtotalGrpASanctionedStrength = partAtotalGrpASanctionedStrength + aer.getSanctionedStrength();
                partAtotalGrpAMenInPosition = partAtotalGrpAMenInPosition + aer.getMeninPosition();
                partAtotalGrpAVacancy = partAtotalGrpAVacancy + aer.getVacancyPosition();
            }
        }
        if (grpAList != null && grpAList.size() > 0) {
            AnnualEstablishment aer = null;
            for (int i = 0; i < grpAList.size(); i++) {
                aer = (AnnualEstablishment) grpAList.get(i);
                partAtotalGrpASanctionedStrength = partAtotalGrpASanctionedStrength + aer.getSanctionedStrength();
                partAtotalGrpAMenInPosition = partAtotalGrpAMenInPosition + aer.getMeninPosition();
                partAtotalGrpAVacancy = partAtotalGrpAVacancy + aer.getVacancyPosition();
            }
        }

        mav.addObject("partAtotalGrpASanctionedStrength", partAtotalGrpASanctionedStrength);
        mav.addObject("partAtotalGrpAMenInPosition", partAtotalGrpAMenInPosition);
        mav.addObject("partAtotalGrpAVacancy", partAtotalGrpAVacancy);

        grpBList = annuaiEstablishmentDao.getAerViewForMultipleAO(ae.getFinancialYear(), lub.getLoginspc(), ae.getAoOffCode(), "B", "", "PART-A", roletype);

        if (grpBList != null && grpBList.size() > 0) {
            AnnualEstablishment aer = null;
            for (int i = 0; i < grpBList.size(); i++) {
                aer = (AnnualEstablishment) grpBList.get(i);
                partATotalGrpBSanctionedStrength = partATotalGrpBSanctionedStrength + aer.getSanctionedStrength();
                partATotalGrpBMenInPosition = partATotalGrpBMenInPosition + aer.getMeninPosition();
                partATotalGrpBVacancy = partATotalGrpBVacancy + aer.getVacancyPosition();
            }
        }
        mav.addObject("partATotalGrpBSanctionedStrength", partATotalGrpBSanctionedStrength);
        mav.addObject("partATotalGrpBMenInPosition", partATotalGrpBMenInPosition);
        mav.addObject("partATotalGrpBVacancy", partATotalGrpBVacancy);

        grpCList = annuaiEstablishmentDao.getAerViewForMultipleAO(ae.getFinancialYear(), lub.getLoginspc(), ae.getAoOffCode(), "C", "", "PART-A", roletype);

        if (grpCList != null && grpCList.size() > 0) {
            AnnualEstablishment aer = null;
            for (int i = 0; i < grpCList.size(); i++) {
                aer = (AnnualEstablishment) grpCList.get(i);
                partATotalGrpCSanctionedStrength = partATotalGrpCSanctionedStrength + aer.getSanctionedStrength();
                partATotalGrpCMenInPosition = partATotalGrpCMenInPosition + aer.getMeninPosition();
                partATotalGrpCVacancy = partATotalGrpCVacancy + aer.getVacancyPosition();
            }
        }
        mav.addObject("partATotalGrpCSanctionedStrength", partATotalGrpCSanctionedStrength);
        mav.addObject("partATotalGrpCMenInPosition", partATotalGrpCMenInPosition);
        mav.addObject("partATotalGrpCVacancy", partATotalGrpCVacancy);

        grpDList = annuaiEstablishmentDao.getAerViewForMultipleAO(ae.getFinancialYear(), lub.getLoginspc(), ae.getAoOffCode(), "D", "", "PART-A", roletype);

        if (grpDList != null && grpDList.size() > 0) {
            AnnualEstablishment aer = null;
            for (int i = 0; i < grpDList.size(); i++) {
                aer = (AnnualEstablishment) grpDList.get(i);
                partATotalGrpDSanctionedStrength = partATotalGrpDSanctionedStrength + aer.getSanctionedStrength();
                partATotalGrpDMenInPosition = partATotalGrpDMenInPosition + aer.getMeninPosition();
                partATotalGrpDVacancy = partATotalGrpDVacancy + aer.getVacancyPosition();
            }
        }
        mav.addObject("partATotalGrpDSanctionedStrength", partATotalGrpDSanctionedStrength);
        mav.addObject("partATotalGrpDMenInPosition", partATotalGrpDMenInPosition);
        mav.addObject("partATotalGrpDVacancy", partATotalGrpDVacancy);

        partBGrpAAllIndiaService = annuaiEstablishmentDao.getAerViewForMultipleAO(ae.getFinancialYear(), lub.getLoginspc(), ae.getAoOffCode(), "A", "AIS", "PART-B", roletype);
        partBGrpAUGC = annuaiEstablishmentDao.getAerViewForMultipleAO(ae.getFinancialYear(), lub.getLoginspc(), ae.getAoOffCode(), "A", "UGC", "PART-B", roletype);
        partBJudiciary = annuaiEstablishmentDao.getAerViewForMultipleAO(ae.getFinancialYear(), lub.getLoginspc(), ae.getAoOffCode(), "A", "OJS", "PART-B", roletype);
        partBgrpAList = annuaiEstablishmentDao.getAerViewForMultipleAO(ae.getFinancialYear(), lub.getLoginspc(), ae.getAoOffCode(), "A", "", "PART-B", roletype);

        if (partBGrpAAllIndiaService != null && partBGrpAAllIndiaService.size() > 0) {
            AnnualEstablishment aer = null;
            for (int i = 0; i < partBGrpAAllIndiaService.size(); i++) {
                aer = (AnnualEstablishment) partBGrpAAllIndiaService.get(i);
                partBtotalGrpASanctionedStrength = partBtotalGrpASanctionedStrength + aer.getSanctionedStrength();
                partBtotalGrpAMenInPosition = partBtotalGrpAMenInPosition + aer.getMeninPosition();
                partBtotalGrpAVacancy = partBtotalGrpAVacancy + aer.getVacancyPosition();
            }
        }
        if (partBGrpAUGC != null && partBGrpAUGC.size() > 0) {
            AnnualEstablishment aer = null;
            for (int i = 0; i < partBGrpAUGC.size(); i++) {
                aer = (AnnualEstablishment) partBGrpAUGC.get(i);
                partBtotalGrpASanctionedStrength = partBtotalGrpASanctionedStrength + aer.getSanctionedStrength();
                partBtotalGrpAMenInPosition = partBtotalGrpAMenInPosition + aer.getMeninPosition();
                partBtotalGrpAVacancy = partBtotalGrpAVacancy + aer.getVacancyPosition();
            }
        }
        if (partBJudiciary != null && partBJudiciary.size() > 0) {
            AnnualEstablishment aer = null;
            for (int i = 0; i < partBJudiciary.size(); i++) {
                aer = (AnnualEstablishment) partBJudiciary.get(i);
                partBtotalGrpASanctionedStrength = partBtotalGrpASanctionedStrength + aer.getSanctionedStrength();
                partBtotalGrpAMenInPosition = partBtotalGrpAMenInPosition + aer.getMeninPosition();
                partBtotalGrpAVacancy = partBtotalGrpAVacancy + aer.getVacancyPosition();
            }
        }
        if (partBgrpAList != null && partBgrpAList.size() > 0) {
            AnnualEstablishment aer = null;
            for (int i = 0; i < partBgrpAList.size(); i++) {
                aer = (AnnualEstablishment) partBgrpAList.get(i);
                partBtotalGrpASanctionedStrength = partBtotalGrpASanctionedStrength + aer.getSanctionedStrength();
                partBtotalGrpAMenInPosition = partBtotalGrpAMenInPosition + aer.getMeninPosition();
                partBtotalGrpAVacancy = partBtotalGrpAVacancy + aer.getVacancyPosition();
            }
        }
        mav.addObject("partBtotalGrpASanctionedStrength", partBtotalGrpASanctionedStrength);
        mav.addObject("partBtotalGrpAMenInPosition", partBtotalGrpAMenInPosition);
        mav.addObject("partBtotalGrpAVacancy", partBtotalGrpAVacancy);

        partBgrpBList = annuaiEstablishmentDao.getAerViewForMultipleAO(ae.getFinancialYear(), lub.getLoginspc(), ae.getAoOffCode(), "B", "", "PART-B", roletype);

        if (partBgrpBList != null && partBgrpBList.size() > 0) {
            AnnualEstablishment aer = null;
            for (int i = 0; i < partBgrpBList.size(); i++) {
                aer = (AnnualEstablishment) partBgrpBList.get(i);
                partBTotalGrpBSanctionedStrength = partBTotalGrpBSanctionedStrength + aer.getSanctionedStrength();
                partBTotalGrpBMenInPosition = partBTotalGrpBMenInPosition + aer.getMeninPosition();
                partBTotalGrpBVacancy = partBTotalGrpBVacancy + aer.getVacancyPosition();
            }
        }
        mav.addObject("partBTotalGrpBSanctionedStrength", partBTotalGrpBSanctionedStrength);
        mav.addObject("partBTotalGrpBMenInPosition", partBTotalGrpBMenInPosition);
        mav.addObject("partBTotalGrpBVacancy", partBTotalGrpBVacancy);

        partBgrpCList = annuaiEstablishmentDao.getAerViewForMultipleAO(ae.getFinancialYear(), lub.getLoginspc(), ae.getAoOffCode(), "C", "", "PART-B", roletype);

        if (partBgrpCList != null && partBgrpCList.size() > 0) {
            AnnualEstablishment aer = null;
            for (int i = 0; i < partBgrpCList.size(); i++) {
                aer = (AnnualEstablishment) partBgrpCList.get(i);
                partBTotalGrpCSanctionedStrength = partBTotalGrpCSanctionedStrength + aer.getSanctionedStrength();
                partBTotalGrpCMenInPosition = partBTotalGrpCMenInPosition + aer.getMeninPosition();
                partBTotalGrpCVacancy = partBTotalGrpCVacancy + aer.getVacancyPosition();
            }
        }
        mav.addObject("partBTotalGrpCSanctionedStrength", partBTotalGrpCSanctionedStrength);
        mav.addObject("partBTotalGrpCMenInPosition", partBTotalGrpCMenInPosition);
        mav.addObject("partBTotalGrpCVacancy", partBTotalGrpCVacancy);

        partBgrpDList = annuaiEstablishmentDao.getAerViewForMultipleAO(ae.getFinancialYear(), lub.getLoginspc(), ae.getAoOffCode(), "D", "", "PART-B", roletype);

        if (partBgrpDList != null && partBgrpDList.size() > 0) {
            AnnualEstablishment aer = null;
            for (int i = 0; i < partBgrpDList.size(); i++) {
                aer = (AnnualEstablishment) partBgrpDList.get(i);
                partBTotalGrpDSanctionedStrength = partBTotalGrpDSanctionedStrength + aer.getSanctionedStrength();
                partBTotalGrpDMenInPosition = partBTotalGrpDMenInPosition + aer.getMeninPosition();
                partBTotalGrpDVacancy = partBTotalGrpDVacancy + aer.getVacancyPosition();
            }
        }
        mav.addObject("partBTotalGrpDSanctionedStrength", partBTotalGrpDSanctionedStrength);
        mav.addObject("partBTotalGrpDMenInPosition", partBTotalGrpDMenInPosition);
        mav.addObject("partBTotalGrpDVacancy", partBTotalGrpDVacancy);

        grpPartCList = annuaiEstablishmentDao.getAerViewForMultipleAO(ae.getFinancialYear(), lub.getLoginspc(), ae.getAoOffCode(), "D", "", "PART-C", roletype);

        mav.addObject("PartAGroupAlist", grpAList);
        mav.addObject("partAGrpAAllIndiaService", partAGrpAAllIndiaService);
        mav.addObject("partAGrpAUGC", partAGrpAUGC);
        mav.addObject("partAJudiciary", partAJudiciary);
        mav.addObject("PartAGroupBlist", grpBList);
        mav.addObject("PartAGroupClist", grpCList);
        mav.addObject("PartAGroupDlist", grpDList);

        mav.addObject("partBgrpAList", partBgrpAList);
        mav.addObject("partBGrpAAllIndiaService", partBGrpAAllIndiaService);
        mav.addObject("partBGrpAUGC", partBGrpAUGC);
        mav.addObject("partBJudiciary", partBJudiciary);
        mav.addObject("partBgrpBList", partBgrpBList);
        mav.addObject("partBgrpCList", partBgrpCList);
        mav.addObject("partBgrpDList", partBgrpDList);

        mav.addObject("PartCGrouplist", grpPartCList);

        AnnualEstablishment[] partDdetails = annuaiEstablishmentDao.getAerViewOtherPartForMultipleAO(ae.getFinancialYear(), lub.getLoginspc(), "D", roletype, ae.getAoOffCode());
        mav.addObject("aeDdetails", partDdetails);

        AnnualEstablishment[] partEdetails = annuaiEstablishmentDao.getAerViewOtherPartForMultipleAO(ae.getFinancialYear(), lub.getLoginspc(), "E", roletype, ae.getAoOffCode());
        mav.addObject("aeEdetails", partEdetails);

        //AnnualEstablishment[] giaList = annuaiEstablishmentDao.getAerViewOtherPartForCO(ae.getFinancialYear(), lub.getLoginspc(), "B", roletype);
        AnnualEstablishment[] giaList = annuaiEstablishmentDao.getAerViewOtherPartForGIAMultipleAO(ae.getFinancialYear(), lub.getLoginspc(), "B", roletype, ae.getAoOffCode());
        mav.addObject("giaList", giaList);

        List giaArrList = Arrays.asList(giaList);

        if (giaArrList != null && giaArrList.size() > 0) {
            AnnualEstablishment aer = null;
            for (int i = 0; i < giaArrList.size(); i++) {
                aer = (AnnualEstablishment) giaArrList.get(i);
                giaTotalSanctionedStrength = giaTotalSanctionedStrength + aer.getOtherSS();
                giaTotalMenInPosition = giaTotalMenInPosition + aer.getMeninPosition();
                giaTotalVacancy = giaTotalVacancy + aer.getOtherVacancy();
            }
        }
        mav.addObject("giaTotalSanctionedStrength", giaTotalSanctionedStrength);
        mav.addObject("giaTotalMenInPosition", giaTotalMenInPosition);
        mav.addObject("giaTotalVacancy", giaTotalVacancy);

        ae.setAoOffCode(ae.getAoOffCode());

        return mav;

    }

    @RequestMapping(value = "downloadaerPDFReportForScheduleIII")
    public void DownloadaerPDFReportForScheduleIII(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam Map<String, String> param) {

        String aerId = param.get("aerId");
        //String fileName = "AER_REPORT_" + aerId;
        response.setContentType("application/pdf");

        Document document = new Document(PageSize.A4);

        PdfWriter writer = null;
        try {
            response.setHeader("Content-Disposition", "attachment; filename=AER_REPORT_" + aerId + ".pdf");

            String officename = annuaiEstablishmentDao.getAERSubmittedAOOfficeName(Integer.parseInt(aerId));

            writer = PdfWriter.getInstance(document, response.getOutputStream());
            writer.setPageEvent(new AERHeaderFooter(officename));

            document.open();
            annuaiEstablishmentDao.downloadPDFReportScheduleIII(document, aerId, lub.getLoginoffcode());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }

    @RequestMapping(value = "displayAERlistForAdministrativeAuthority.htm", params = {"btnAer=SubmitToFinance"})
    public ModelAndView SubmitToFinance(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") AnnualEstablishment ae) {

        ModelAndView mav = null;

        mav = new ModelAndView("report/AnnualEstablishmentReportForAO", "command", ae);

        String aooffcode = annuaiEstablishmentDao.getAcceptorAssignedOffice(lub.getLoginempid(), lub.getLoginspc());

        //String aoOffCode = annuaiEstablishmentDao.getAoOffCode(lub.getLogindeptcode());
        int aoAerId = annuaiEstablishmentDao.submitAerReportAsAcceptor(lub.getLoginempid(), lub.getLoginspc(), aooffcode, ae.getFinancialYear());

        annuaiEstablishmentDao.updateAerAOAerId(lub.getLoginempid(), lub.getLoginspc(), aooffcode, ae.getFinancialYear(), aoAerId);

        annuaiEstablishmentDao.insertConsolidatedDataAfterAcceptorSubmit(aoAerId, ae.getFinancialYear(), lub.getLoginspc(), aooffcode, "AIS", "A", "PART-A");
        annuaiEstablishmentDao.insertConsolidatedDataAfterAcceptorSubmit(aoAerId, ae.getFinancialYear(), lub.getLoginspc(), aooffcode, "UGC", "A", "PART-A");
        annuaiEstablishmentDao.insertConsolidatedDataAfterAcceptorSubmit(aoAerId, ae.getFinancialYear(), lub.getLoginspc(), aooffcode, "OJS", "A", "PART-A");
        annuaiEstablishmentDao.insertConsolidatedDataAfterAcceptorSubmit(aoAerId, ae.getFinancialYear(), lub.getLoginspc(), aooffcode, "", "A", "PART-A");
        annuaiEstablishmentDao.insertConsolidatedDataAfterAcceptorSubmit(aoAerId, ae.getFinancialYear(), lub.getLoginspc(), aooffcode, "", "B", "PART-A");
        annuaiEstablishmentDao.insertConsolidatedDataAfterAcceptorSubmit(aoAerId, ae.getFinancialYear(), lub.getLoginspc(), aooffcode, "", "C", "PART-A");
        annuaiEstablishmentDao.insertConsolidatedDataAfterAcceptorSubmit(aoAerId, ae.getFinancialYear(), lub.getLoginspc(), aooffcode, "", "D", "PART-A");

        annuaiEstablishmentDao.insertConsolidatedDataAfterAcceptorSubmit(aoAerId, ae.getFinancialYear(), lub.getLoginspc(), aooffcode, "AIS", "A", "PART-B");
        annuaiEstablishmentDao.insertConsolidatedDataAfterAcceptorSubmit(aoAerId, ae.getFinancialYear(), lub.getLoginspc(), aooffcode, "UGC", "A", "PART-B");
        annuaiEstablishmentDao.insertConsolidatedDataAfterAcceptorSubmit(aoAerId, ae.getFinancialYear(), lub.getLoginspc(), aooffcode, "OJS", "A", "PART-B");
        annuaiEstablishmentDao.insertConsolidatedDataAfterAcceptorSubmit(aoAerId, ae.getFinancialYear(), lub.getLoginspc(), aooffcode, "", "A", "PART-B");
        annuaiEstablishmentDao.insertConsolidatedDataAfterAcceptorSubmit(aoAerId, ae.getFinancialYear(), lub.getLoginspc(), aooffcode, "", "B", "PART-B");
        annuaiEstablishmentDao.insertConsolidatedDataAfterAcceptorSubmit(aoAerId, ae.getFinancialYear(), lub.getLoginspc(), aooffcode, "", "C", "PART-B");
        annuaiEstablishmentDao.insertConsolidatedDataAfterAcceptorSubmit(aoAerId, ae.getFinancialYear(), lub.getLoginspc(), aooffcode, "", "D", "PART-B");

        annuaiEstablishmentDao.insertConsolidatedDataAfterAcceptorSubmit(aoAerId, ae.getFinancialYear(), lub.getLoginspc(), aooffcode, "", "A", "PART-C");

        //annuaiEstablishmentDao.insertConsolidatedDataAfterAcceptorSubmit(aoAerId, ae.getFinancialYear(), lub.getLoginspc(), lub.getLoginoffcode(), "", "A", "D");
        //annuaiEstablishmentDao.insertConsolidatedDataAfterAcceptorSubmit(aoAerId, ae.getFinancialYear(), lub.getLoginspc(), lub.getLoginoffcode(), "", "A", "E");
        annuaiEstablishmentDao.insertConsolidatedDataAfterAcceptorSubmitForBDE(aoAerId, ae.getFinancialYear(), lub.getLoginspc(), aooffcode, "", "B");
        annuaiEstablishmentDao.insertConsolidatedDataAfterAcceptorSubmitForBDE(aoAerId, ae.getFinancialYear(), lub.getLoginspc(), aooffcode, "", "D");
        annuaiEstablishmentDao.insertConsolidatedDataAfterAcceptorSubmitForBDE(aoAerId, ae.getFinancialYear(), lub.getLoginspc(), aooffcode, "", "E");

        return mav;

    }

    @RequestMapping(value = "displayAERlistForAdministrativeAuthority.htm", params = {"btnAer=SubmitToFinanceMultipleAO"})
    public ModelAndView SubmitToFinanceMultipleAO(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") AnnualEstablishment ae) {

        ModelAndView mav = null;

        mav = new ModelAndView("report/AnnualEstablishmentReportForAO", "command", ae);

        //String aoOffCode = annuaiEstablishmentDao.getAoOffCode(lub.getLogindeptcode());
        int aoAerId = annuaiEstablishmentDao.submitAerReportAsAcceptor(lub.getLoginempid(), lub.getLoginspc(), ae.getAoOffCode(), ae.getFinancialYear());

        annuaiEstablishmentDao.updateAerAOAerId(lub.getLoginempid(), lub.getLoginspc(), ae.getAoOffCode(), ae.getFinancialYear(), aoAerId);

        annuaiEstablishmentDao.insertConsolidatedDataAfterAcceptorSubmitMultipleAO(aoAerId, ae.getFinancialYear(), lub.getLoginspc(), ae.getAoOffCode(), "AIS", "A", "PART-A");
        annuaiEstablishmentDao.insertConsolidatedDataAfterAcceptorSubmitMultipleAO(aoAerId, ae.getFinancialYear(), lub.getLoginspc(), ae.getAoOffCode(), "UGC", "A", "PART-A");
        annuaiEstablishmentDao.insertConsolidatedDataAfterAcceptorSubmitMultipleAO(aoAerId, ae.getFinancialYear(), lub.getLoginspc(), ae.getAoOffCode(), "OJS", "A", "PART-A");
        annuaiEstablishmentDao.insertConsolidatedDataAfterAcceptorSubmitMultipleAO(aoAerId, ae.getFinancialYear(), lub.getLoginspc(), ae.getAoOffCode(), "", "A", "PART-A");
        annuaiEstablishmentDao.insertConsolidatedDataAfterAcceptorSubmitMultipleAO(aoAerId, ae.getFinancialYear(), lub.getLoginspc(), ae.getAoOffCode(), "", "B", "PART-A");
        annuaiEstablishmentDao.insertConsolidatedDataAfterAcceptorSubmitMultipleAO(aoAerId, ae.getFinancialYear(), lub.getLoginspc(), ae.getAoOffCode(), "", "C", "PART-A");
        annuaiEstablishmentDao.insertConsolidatedDataAfterAcceptorSubmitMultipleAO(aoAerId, ae.getFinancialYear(), lub.getLoginspc(), ae.getAoOffCode(), "", "D", "PART-A");

        annuaiEstablishmentDao.insertConsolidatedDataAfterAcceptorSubmitMultipleAO(aoAerId, ae.getFinancialYear(), lub.getLoginspc(), ae.getAoOffCode(), "AIS", "A", "PART-B");
        annuaiEstablishmentDao.insertConsolidatedDataAfterAcceptorSubmitMultipleAO(aoAerId, ae.getFinancialYear(), lub.getLoginspc(), ae.getAoOffCode(), "UGC", "A", "PART-B");
        annuaiEstablishmentDao.insertConsolidatedDataAfterAcceptorSubmitMultipleAO(aoAerId, ae.getFinancialYear(), lub.getLoginspc(), ae.getAoOffCode(), "OJS", "A", "PART-B");
        annuaiEstablishmentDao.insertConsolidatedDataAfterAcceptorSubmitMultipleAO(aoAerId, ae.getFinancialYear(), lub.getLoginspc(), ae.getAoOffCode(), "", "A", "PART-B");
        annuaiEstablishmentDao.insertConsolidatedDataAfterAcceptorSubmitMultipleAO(aoAerId, ae.getFinancialYear(), lub.getLoginspc(), ae.getAoOffCode(), "", "B", "PART-B");
        annuaiEstablishmentDao.insertConsolidatedDataAfterAcceptorSubmitMultipleAO(aoAerId, ae.getFinancialYear(), lub.getLoginspc(), ae.getAoOffCode(), "", "C", "PART-B");
        annuaiEstablishmentDao.insertConsolidatedDataAfterAcceptorSubmitMultipleAO(aoAerId, ae.getFinancialYear(), lub.getLoginspc(), ae.getAoOffCode(), "", "D", "PART-B");

        annuaiEstablishmentDao.insertConsolidatedDataAfterAcceptorSubmitMultipleAO(aoAerId, ae.getFinancialYear(), lub.getLoginspc(), ae.getAoOffCode(), "", "A", "PART-C");

        //annuaiEstablishmentDao.insertConsolidatedDataAfterAcceptorSubmit(aoAerId, ae.getFinancialYear(), lub.getLoginspc(), lub.getLoginoffcode(), "", "A", "D");
        //annuaiEstablishmentDao.insertConsolidatedDataAfterAcceptorSubmit(aoAerId, ae.getFinancialYear(), lub.getLoginspc(), lub.getLoginoffcode(), "", "A", "E");
        annuaiEstablishmentDao.insertConsolidatedDataAfterAcceptorSubmitForBDEMultipleAO(aoAerId, ae.getFinancialYear(), lub.getLoginspc(), ae.getAoOffCode(), "", "B");
        annuaiEstablishmentDao.insertConsolidatedDataAfterAcceptorSubmitForBDEMultipleAO(aoAerId, ae.getFinancialYear(), lub.getLoginspc(), ae.getAoOffCode(), "", "D");
        annuaiEstablishmentDao.insertConsolidatedDataAfterAcceptorSubmitForBDEMultipleAO(aoAerId, ae.getFinancialYear(), lub.getLoginspc(), ae.getAoOffCode(), "", "E");

        return mav;

    }

    @RequestMapping(value = "displayAERlistForAdministrativeAuthority.htm", params = {"btnAer=Back"})
    public ModelAndView back2displayAERlistForAdministrativeAuthority(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") AnnualEstablishment ae) {
        ModelAndView mav = null;

        if (ae.getAoOffCode() != null && !ae.getAoOffCode().equals("")) {
            String aooffcode = ae.getAoOffCode();
            mav = new ModelAndView("redirect:/displayAERlistForAdministrativeAuthority.htm?btnAer=Search&aoOffCode=" + aooffcode);
        } else {
            mav = new ModelAndView("redirect:/displayAERlistForAdministrativeAuthority.htm?btnAer=Search");
        }
        return mav;
    }

    @RequestMapping(value = "DeptWiseGroupERStatus")
    public ModelAndView DeptWiseGroupERStatus(ModelMap model, HttpServletResponse response) {
        String path = "/misreport/DeptWiseGroupAERStatus";
        ModelAndView mav = new ModelAndView();
        ArrayList AERDetails = annuaiEstablishmentDao.DeptWiseGroupAERStatus();
        mav.addObject("AERDetails", AERDetails);
        mav.setViewName(path);
        return mav;
    }

    @RequestMapping(value = "displayAERlistForAdministrativeAuthority", params = {"btnAer=Decline"})
    public String declineAERForAO(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") AnnualEstablishment ae) {

        String path = "redirect:/displayAERlistForAdministrativeAuthority.htm?btnAer=Search";

        String isAODReviewer = annuaiEstablishmentDao.checkAODReviewer(lub.getLoginoffcode(), lub.getLoginspc());

        String isAODVerifier = annuaiEstablishmentDao.checkAODVerifier(lub.getLoginoffcode(), lub.getLoginspc());

        String isAOAcceptor = annuaiEstablishmentDao.checkAOAcceptor(lub.getLoginoffcode(), lub.getLoginspc());

        String roleType = "";
        if (isAODReviewer.equalsIgnoreCase("Y")) {
            roleType = "DREVIEWER";
        } else if (isAODVerifier.equalsIgnoreCase("Y")) {
            roleType = "DVERIFIER";
        } else if (isAOAcceptor.equalsIgnoreCase("Y")) {
            roleType = "ACCEPTOR";
        }

        if (isAODReviewer.equalsIgnoreCase("Y") && isAODVerifier.equalsIgnoreCase("Y")) {
            roleType = "BO";
        }

        annuaiEstablishmentDao.declineAER(ae.getAerId(), lub.getLoginempid(), lub.getLoginspc(), lub.getLoginoffcode(), ae.getRevertReason(), roleType);

        return path;
    }

    @RequestMapping(value = "viewAERFlowList")
    public ModelAndView viewAERFlowList(@RequestParam("aerId") int aerId) {

        ModelAndView mv = new ModelAndView();

        try {
            ArrayList flowlist = annuaiEstablishmentDao.getAERFlowList(aerId);
            mv.setViewName("report/ShowAERFlowList");
            mv.addObject("flowlist", flowlist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mv;
    }

    @RequestMapping(value = "ScheduleIChangeAtReviewer", params = {"btnAer=List"})
    public ModelAndView ScheduleIChangeAtReviewer(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") AnnualEstablishment ae, @RequestParam Map<String, String> param) {

        List grpAList = new ArrayList();
        List partAGrpAAllIndiaService = new ArrayList();
        List partAGrpAUGC = new ArrayList();
        List partAJudiciary = new ArrayList();
        List grpBList = new ArrayList();
        List grpCList = new ArrayList();
        List grpDList = new ArrayList();

        List partBGrpAAllIndiaService = new ArrayList();
        List partBGrpAUGC = new ArrayList();
        List partBJudiciary = new ArrayList();
        List partBGrpAList = new ArrayList();
        List partBGrpBList = new ArrayList();
        List partBGrpCList = new ArrayList();
        List partBGrpDList = new ArrayList();

        List grpPartCList = new ArrayList();
        String hideEditLink = "";
        ModelAndView mv = new ModelAndView();
        String hideUrl = "";
        try {
            partAGrpAAllIndiaService = annuaiEstablishmentDao.getGroupAlistBeforeSubmission("PART-A", lub.getLoginoffcode(), "A", Integer.parseInt(param.get("aerId")), "AIS");
            partAGrpAUGC = annuaiEstablishmentDao.getGroupAlistBeforeSubmission("PART-A", lub.getLoginoffcode(), "A", Integer.parseInt(param.get("aerId")), "UGC");
            partAJudiciary = annuaiEstablishmentDao.getGroupAlistBeforeSubmission("PART-A", lub.getLoginoffcode(), "A", Integer.parseInt(param.get("aerId")), "OJS");
            grpAList = annuaiEstablishmentDao.getGroupAlistBeforeSubmission("PART-A", lub.getLoginoffcode(), "A", Integer.parseInt(param.get("aerId")), "");
            grpBList = annuaiEstablishmentDao.getGroupAlistBeforeSubmission("PART-A", lub.getLoginoffcode(), "B", Integer.parseInt(param.get("aerId")), "");
            grpCList = annuaiEstablishmentDao.getGroupAlistBeforeSubmission("PART-A", lub.getLoginoffcode(), "C", Integer.parseInt(param.get("aerId")), "");
            grpDList = annuaiEstablishmentDao.getGroupAlistBeforeSubmission("PART-A", lub.getLoginoffcode(), "D", Integer.parseInt(param.get("aerId")), "");

            partBGrpAAllIndiaService = annuaiEstablishmentDao.getGroupAlistBeforeSubmission("PART-B", lub.getLoginoffcode(), "A", Integer.parseInt(param.get("aerId")), "AIS");
            partBGrpAUGC = annuaiEstablishmentDao.getGroupAlistBeforeSubmission("PART-B", lub.getLoginoffcode(), "A", Integer.parseInt(param.get("aerId")), "UGC");
            partBJudiciary = annuaiEstablishmentDao.getGroupAlistBeforeSubmission("PART-B", lub.getLoginoffcode(), "A", Integer.parseInt(param.get("aerId")), "OJS");
            partBGrpAList = annuaiEstablishmentDao.getGroupAlistBeforeSubmission("PART-B", lub.getLoginoffcode(), "A", Integer.parseInt(param.get("aerId")), "");
            partBGrpBList = annuaiEstablishmentDao.getGroupAlistBeforeSubmission("PART-B", lub.getLoginoffcode(), "B", Integer.parseInt(param.get("aerId")), "");
            partBGrpCList = annuaiEstablishmentDao.getGroupAlistBeforeSubmission("PART-B", lub.getLoginoffcode(), "C", Integer.parseInt(param.get("aerId")), "");
            partBGrpDList = annuaiEstablishmentDao.getGroupAlistBeforeSubmission("PART-B", lub.getLoginoffcode(), "D", Integer.parseInt(param.get("aerId")), "");

            grpPartCList = annuaiEstablishmentDao.getGroupAlistBeforeSubmission("PART-C", lub.getLoginoffcode(), "C", Integer.parseInt(param.get("aerId")), "");

            boolean status = annuaiEstablishmentDao.privewDataInserted(Integer.parseInt(param.get("aerId")));
            if (status) {
                hideEditLink = "Y";
            } else {
                hideEditLink = "N";
            }

            boolean hidelink = annuaiEstablishmentDao.getAerSubmittedStatus(Integer.parseInt(param.get("aerId")));

            if (hidelink == false) {
                hideUrl = "Y";
            } else {
                hideUrl = "N";
            }

            mv.addObject("PartAGroupAlist", grpAList);
            mv.addObject("partAGrpAAllIndiaService", partAGrpAAllIndiaService);
            mv.addObject("partAGrpAUGC", partAGrpAUGC);
            mv.addObject("partAJudiciary", partAJudiciary);
            mv.addObject("PartAGroupBlist", grpBList);
            mv.addObject("PartAGroupClist", grpCList);
            mv.addObject("PartAGroupDlist", grpDList);

            mv.addObject("partBGrpAAllIndiaService", partBGrpAAllIndiaService);
            mv.addObject("partBGrpAUGC", partBGrpAUGC);
            mv.addObject("partBJudiciary", partBJudiciary);
            mv.addObject("partBGrpAList", partBGrpAList);
            mv.addObject("partBGrpBList", partBGrpBList);
            mv.addObject("partBGrpCList", partBGrpCList);
            mv.addObject("partBGrpDList", partBGrpDList);

            mv.addObject("PartCGrouplist", grpPartCList);

            mv.addObject("hideEditLink", hideEditLink);
            mv.addObject("Editable", hideUrl);

            List payscaleList = payscaleDAO.getPayScaleList();
            mv.addObject("payscaleList", payscaleList);
            mv.addObject("OffName", lub.getLoginoffname());

            if (hideEditLink.equals("Y")) {

                AnnualEstablishment[] partDdetails = annuaiEstablishmentDao.getOtherEst(ae.getAerId(), "D");
                mv.addObject("aeDdetails", partDdetails);

                AnnualEstablishment[] partEdetails = annuaiEstablishmentDao.getOtherEst(ae.getAerId(), "E");
                mv.addObject("aeEdetails", partEdetails);

                List giaList = annuaiEstablishmentDao.getAllGrantinAidOfficesByAerId(ae.getAerId());
                mv.addObject("giaList", giaList);

                mv.setViewName("/report/PreviewSubmitAerReport");
            } else {
                mv.setViewName("/report/ManageSanctionPost");
            }
            mv.setViewName("/report/ScheduleIChangeAtReviewerLevel");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mv;
    }

    @RequestMapping(value = "ScheduleIChangeAtReviewer", params = {"btnAer=Update"})
    public String ScheduleIChangeAtReviewerUpdate(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") AnnualEstablishment ae) {

        String path = "redirect://ScheduleIChangeAtReviewer.htm?btnAer=List&aerId=" + ae.getAerId();

        //int retVal = annuaiEstablishmentDao.updatePostInformation(lub.getLoginoffcode(), ae.getGpc(), ae.getScaleofPay(), ae.getScaleofPay7th(), ae.getPostgrp(), ae.getHidPostGrp(), ae.getPaylevel(), ae.getGp());
        annuaiEstablishmentDao.updatePartAPostInformationAtReviewerLevel(ae.getAerId(), ae.getGpc(), ae.getScaleofPay(), ae.getPostgrp(), ae.getHidPostGrp(), ae.getPaylevel(), ae.getGp());
        //annuaiEstablishmentDao.updatePartCPostInformationAtReviewerLevel(ae.getAerId(), ae.getGpc(), ae.getScaleofPay(),  ae.getPostgrp(),ae.getPaylevel(), ae.getGp());

        return path;
    }

    @RequestMapping(value = "ScheduleIChangeAtReviewer", params = {"btnAer=Back"})
    public String Back2ScheduleIChangeAtReviewer(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") AnnualEstablishment ae) {

        String path = "redirect:/displayAERlistForControllingAuthority.htm?btnAer=Search";

        return path;
    }

    @RequestMapping(value = "DeptWiseGroupAERSan")
    public ModelAndView DeptWiseGroupAERSan(ModelMap model, HttpServletResponse response) {
        String path = "/misreport/DeptWiseGroupAERSan";
        ModelAndView mav = new ModelAndView();
        ArrayList AERDetails = annuaiEstablishmentDao.DeptWiseGroupAERSan();
        mav.addObject("AERDetails", AERDetails);
        mav.setViewName(path);
        return mav;
    }

    @RequestMapping(value = "COWiseDeptAERStatus")
    public ModelAndView COWiseDeptAERStatus(@ModelAttribute("command") AnnualEstablishment ae) {
        String path = "/misreport/COWiseDeptAERStatus";
        ModelAndView mav = new ModelAndView();
        ArrayList AERDetails = annuaiEstablishmentDao.COWiseDeptAERStatus(ae.getFinancialYear());
        mav.addObject("AERDetails", AERDetails);
        mav.addObject("fiscalyear", ae.getFinancialYear());
        mav.setViewName(path);
        return mav;
    }

    @RequestMapping(value = "DeptWiseGroupAERSanCoWise")
    public ModelAndView DeptWiseGroupAERSanCoWise(ModelMap model, HttpServletResponse response, @RequestParam Map<String, String> param, @ModelAttribute("command") AnnualEstablishment ae) {
        String path = "/misreport/DeptWiseGroupAERSanCoWise";
        ModelAndView mav = new ModelAndView();
        String fyear = param.get("financialYear");
        if (fyear == null || fyear.equals("")) {
            fyear = "2023-24";
        }
        ArrayList AERDetails = annuaiEstablishmentDao.DeptWiseGroupAERSanCoWise(fyear);
        mav.addObject("financialYear", fyear);
        mav.addObject("AERDetails", AERDetails);
        mav.setViewName(path);
        return mav;
    }

    @RequestMapping(value = "DeptWiseGroupAERSanCoWise", params = {"btnAer=Download Excel"})
    public void DownloadDeptWiseGroupAERSanCoWiseExcel(HttpServletResponse response, @RequestParam Map<String, String> param, @ModelAttribute("command") AnnualEstablishment ae) {

        response.setContentType("application/vnd.ms-excel");

        OutputStream out = null;

        try {
            String fyear = ae.getFinancialYear();
            if (fyear == null || fyear.equals("")) {
                fyear = "2023-24";
            }

            out = response.getOutputStream();
            WritableWorkbook workbook = Workbook.createWorkbook(out);

            response.setHeader("Content-Disposition", "attachment; filename=AER_SANCTIONED_STRENGTH_" + fyear + ".xls");

            ArrayList aerDetails = annuaiEstablishmentDao.DeptWiseGroupAERSanCoWise(fyear);
            annuaiEstablishmentDao.downloadAERSanctionedStrengthExcel(workbook, aerDetails, fyear);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "DeptWiseGroupAERMIPCoWise")
    public ModelAndView DeptWiseGroupAERMIPCoWise(ModelMap model, HttpServletResponse response, @RequestParam Map<String, String> param, @ModelAttribute("command") AnnualEstablishment ae) {
        String path = "/misreport/DeptWiseGroupAERMIPCoWise";
        ModelAndView mav = new ModelAndView();
        String fyear = param.get("financialYear");
        if (fyear == null || fyear.equals("")) {
            fyear = "2023-24";
        }
        ArrayList AERDetails = annuaiEstablishmentDao.DeptWiseGroupAERMIPCoWise(fyear);
        mav.addObject("AERDetails", AERDetails);
        mav.addObject("financialYear", fyear);
        mav.setViewName(path);
        return mav;
    }

    @RequestMapping(value = "DeptWiseGroupAERMIPCoWise", params = {"btnAer=Download Excel"})
    public void DownloadDeptWiseGroupAERMIPCoWiseExcel(HttpServletResponse response, @RequestParam Map<String, String> param, @ModelAttribute("command") AnnualEstablishment ae) {

        response.setContentType("application/vnd.ms-excel");

        OutputStream out = null;

        try {
            String fyear = ae.getFinancialYear();
            if (fyear == null || fyear.equals("")) {
                fyear = "2023-24";
            }

            out = response.getOutputStream();
            WritableWorkbook workbook = Workbook.createWorkbook(out);

            response.setHeader("Content-Disposition", "attachment; filename=AER_MEN_IN_POSITION_" + fyear + ".xls");

            ArrayList aerDetails = annuaiEstablishmentDao.DeptWiseGroupAERMIPCoWise(fyear);
            annuaiEstablishmentDao.downloadAERMenInPositionExcel(workbook, aerDetails, fyear);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "DeptWiseGroupAERVacancyCoWise")
    public ModelAndView DeptWiseGroupAERVacancyCoWise(ModelMap model, HttpServletResponse response, @RequestParam Map<String, String> param, @ModelAttribute("command") AnnualEstablishment ae) {
        String path = "/misreport/DeptWiseGroupAERVacancyCoWise";
        ModelAndView mav = new ModelAndView();
        String fyear = param.get("financialYear");
        if (fyear == null || fyear.equals("")) {
            fyear = "2023-24";
        }
        ArrayList AERDetails = annuaiEstablishmentDao.DeptWiseGroupAERVacancyCoWise(fyear);
        mav.addObject("AERDetails", AERDetails);
        mav.addObject("financialYear", fyear);
        mav.setViewName(path);
        return mav;
    }

    @RequestMapping(value = "DeptWiseGroupAERVacancyCoWise", params = {"btnAer=Download Excel"})
    public void DownloadDeptWiseGroupAERVacancyCoWiseExcel(HttpServletResponse response, @RequestParam Map<String, String> param, @ModelAttribute("command") AnnualEstablishment ae) {

        response.setContentType("application/vnd.ms-excel");

        OutputStream out = null;

        try {
            String fyear = ae.getFinancialYear();
            if (fyear == null || fyear.equals("")) {
                fyear = "2023-24";
            }

            out = response.getOutputStream();
            WritableWorkbook workbook = Workbook.createWorkbook(out);

            response.setHeader("Content-Disposition", "attachment; filename=AER_VACANCY_" + fyear + ".xls");

            ArrayList aerDetails = annuaiEstablishmentDao.DeptWiseGroupAERVacancyCoWise(fyear);
            annuaiEstablishmentDao.downloadAERVacancyExcel(workbook, aerDetails, fyear);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "viewCODeptWise")
    public ModelAndView viewCODeptWise(ModelMap model, HttpServletResponse response, @RequestParam("deptCode") String dcode, @RequestParam("fy") String fy) {
        String path = "/misreport/viewCODeptWise";
        ModelAndView mav = new ModelAndView();
        ArrayList AERDetails = annuaiEstablishmentDao.viewCODeptWise(dcode, fy);
        mav.addObject("AERDetails", AERDetails);
        mav.addObject("deptname", deptDAO.getDeptName(dcode));
        mav.setViewName(path);
        return mav;
    }

    @RequestMapping(value = "viewDDODeptWiseSancStrength")
    public ModelAndView viewDDODeptWiseSancStrength(ModelMap model, HttpServletResponse response, @RequestParam("offCode") String offCode, @RequestParam("aerId") String coaerid) {
        String path = "/misreport/ViewDDODeptWiseSancStrength";
        ModelAndView mav = new ModelAndView();
        ArrayList AERDetails = annuaiEstablishmentDao.viewDDODeptWiseSancStrength(offCode, coaerid);
        mav.addObject("AERDetails", AERDetails);
        mav.addObject("officename", officeDao.getOfficeDetails(offCode).getOffEn());
        mav.setViewName(path);
        return mav;
    }

    @RequestMapping(value = "viewCODeptWiseMIP")
    public ModelAndView viewCODeptWiseMIP(ModelMap model, HttpServletResponse response, @RequestParam("deptCode") String dcode, @RequestParam("fy") String fy) {
        String path = "/misreport/viewCODeptWiseMIP";
        ModelAndView mav = new ModelAndView();
        ArrayList AERDetails = annuaiEstablishmentDao.viewCODeptWiseMIP(dcode, fy);
        mav.addObject("AERDetails", AERDetails);
        mav.addObject("deptname", deptDAO.getDeptName(dcode));
        mav.setViewName(path);
        return mav;
    }

    @RequestMapping(value = "viewDDODeptWiseMIP")
    public ModelAndView viewDDODeptWiseMIP(ModelMap model, HttpServletResponse response, @RequestParam("offCode") String offCode, @RequestParam("aerId") String coaerid) {
        String path = "/misreport/ViewDDODeptWiseMIP";
        ModelAndView mav = new ModelAndView();
        ArrayList AERDetails = annuaiEstablishmentDao.viewDDODeptWiseMIP(offCode, coaerid);
        mav.addObject("AERDetails", AERDetails);
        mav.addObject("officename", officeDao.getOfficeDetails(offCode).getOffEn());
        mav.setViewName(path);
        return mav;
    }

    @RequestMapping(value = "viewCODeptWiseVacancy")
    public ModelAndView viewCODeptWiseVacancy(ModelMap model, HttpServletResponse response, @RequestParam("deptCode") String dcode, @RequestParam("fy") String fy) {
        String path = "/misreport/viewCODeptWiseVacancy";
        ModelAndView mav = new ModelAndView();
        ArrayList AERDetails = annuaiEstablishmentDao.viewCODeptWiseVacancy(dcode, fy);
        mav.addObject("AERDetails", AERDetails);
        mav.addObject("deptname", deptDAO.getDeptName(dcode));
        mav.setViewName(path);
        return mav;
    }

    @RequestMapping(value = "viewDDODeptWiseVacancy")
    public ModelAndView viewDDODeptWiseVacancy(ModelMap model, HttpServletResponse response, @RequestParam("offCode") String offCode, @RequestParam("aerId") String coaerid) {
        String path = "/misreport/viewDDODeptWiseVacancy";
        ModelAndView mav = new ModelAndView();
        ArrayList AERDetails = annuaiEstablishmentDao.viewDDODeptWiseVacancy(offCode, coaerid);
        mav.addObject("AERDetails", AERDetails);
        mav.addObject("officename", officeDao.getOfficeDetails(offCode).getOffEn());
        mav.setViewName(path);
        return mav;
    }

    @ResponseBody
    @RequestMapping(value = "changeCadre")
    public void ChangeCadre(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") AnnualEstablishment ae) {

        annuaiEstablishmentDao.changeCadre(ae.getReportId(), ae.getGpc(), ae.getSltCadre());
    }

    @RequestMapping(value = "saveGIAData", params = {"action=Insert Data from Previous Year"})
    public ModelAndView InsertGranteeDatafromPreviousYear(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") AnnualEstablishment ae) {

        annuaiEstablishmentDao.insertGranteeOfficeData(ae.getAerId(), lub.getLoginoffcode());

        ModelAndView mav = new ModelAndView("redirect:aerReportPartB.htm?aerId=" + ae.getAerId());

        return mav;
    }

    @RequestMapping(value = "COWiseDDONotSubmited")
    public ModelAndView COWiseDDONotSubmited(ModelMap model, HttpServletResponse response, @RequestParam("ddocode") String dcode) {
        String path = "/misreport/COWiseDDONotSubmited";
        ModelAndView mav = new ModelAndView();
        ArrayList AERDetails = annuaiEstablishmentDao.COWiseDDONotSubmited(dcode);
        mav.addObject("AERDetails", AERDetails);
        mav.setViewName(path);
        return mav;
    }

    @RequestMapping(value = "AERProcessUserListDC")
    public String AERProcessUserListDC(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("command") AnnualEstablishment ae) {

        try {
            List fiscyear = fiscalDAO.getFiscalYearList();
            model.addAttribute("fiscyear", fiscyear);

            if (ae.getOffCode() != null && !ae.getOffCode().equals("")) {
                List aerliststatus = annuaiEstablishmentDao.getAERListStatus(ae.getOffCode(), ae.getFinancialYear());

                model.addAttribute("aerliststatus", aerliststatus);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/report/AERProcessUserListDC";
    }

    @RequestMapping(value = "displayScheduleIListForAdministrativeAuthority.htm")
    public ModelAndView displayScheduleIListForAdministrativeAuthority(@ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("coAerId") int coAerId) {

        ModelAndView mav = new ModelAndView();

        List<AnnualEstablishment> submitList = null;

        submitList = annuaiEstablishmentDao.getAerScheduleIListForAOLevel(coAerId);

        mav.addObject("EstablishmentList", submitList);
        mav.addObject("OffName", lub.getLoginoffname());

        mav.setViewName("/report/ScheduleIListForAO");

        return mav;
    }

    @RequestMapping(value = "ViewDDODeptWiseVacancyPostList")
    public ModelAndView ViewDDODeptWiseVacancyPostList(ModelMap model, HttpServletResponse response, @RequestParam("offcode") String offcode, @RequestParam("postgroup") String postgroup, @RequestParam("parttype") String parttype, @RequestParam("aerId") String aerId) {
        String path = "/misreport/ViewDDODeptWiseVacancyPostList";
        ModelAndView mav = new ModelAndView();
        ArrayList AERDetails = annuaiEstablishmentDao.viewDDODeptWiseVacancyPostList(postgroup, parttype, aerId);
        mav.addObject("AERDetails", AERDetails);
        mav.addObject("parttype", parttype);
        mav.addObject("postgroup", postgroup);
        mav.addObject("officename", officeDao.getOfficeDetails(offcode).getOffEn());
        mav.setViewName(path);
        return mav;
    }

    @RequestMapping(value = "ViewDDODeptWiseSancStrengthPostList")
    public ModelAndView ViewDDODeptWiseSancStrengthPostList(@RequestParam("offcode") String offcode, @RequestParam("postgroup") String postgroup, @RequestParam("parttype") String parttype, @RequestParam("aerId") String aerId) {
        String path = "/misreport/ViewDDODeptWiseSancStrengthPostList";
        ModelAndView mav = new ModelAndView();
        ArrayList AERDetails = annuaiEstablishmentDao.viewDDODeptWiseSancStrengthPostList(postgroup, parttype, aerId);
        mav.addObject("AERDetails", AERDetails);
        mav.addObject("parttype", parttype);
        mav.addObject("postgroup", postgroup);
        mav.addObject("officename", officeDao.getOfficeDetails(offcode).getOffEn());
        mav.setViewName(path);
        return mav;
    }

    @RequestMapping(value = "ViewDDODeptWiseMIPPostList")
    public ModelAndView ViewDDODeptWiseMIPPostList(@RequestParam("offcode") String offcode, @RequestParam("postgroup") String postgroup, @RequestParam("parttype") String parttype, @RequestParam("aerId") String aerId) {
        String path = "/misreport/ViewDDODeptWiseMIPPostList";
        ModelAndView mav = new ModelAndView();
        ArrayList AERDetails = annuaiEstablishmentDao.viewDDODeptWiseMIPPostList(postgroup, parttype, aerId);
        mav.addObject("AERDetails", AERDetails);
        mav.addObject("parttype", parttype);
        mav.addObject("postgroup", postgroup);
        mav.addObject("officename", officeDao.getOfficeDetails(offcode).getOffEn());
        mav.setViewName(path);
        return mav;
    }
    ///---GenericPostWiseStrength For DDO Office---

    @RequestMapping(value = "GenericPostWiseStrength", method = RequestMethod.GET)
    public ModelAndView EmployeePostList(HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletResponse response) throws IOException {
        ModelAndView mav = new ModelAndView();
        String path = "/report/AERPostWiseStrengthReport";
        List genPostList = annuaiEstablishmentDao.getGenericPostList(lub.getLoginoffcode());
        mav.addObject("genericPostList", genPostList);

        mav.setViewName(path);
        return mav;
    }

    @RequestMapping(value = "downloadExcelPostWiseStrength")
    public void downloadPostWiseStrengthExcel(@ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletResponse response) {
        response.setContentType("application/vnd.ms-excel");
        OutputStream out = null;
        WritableWorkbook workbook = null;
        try {

            String fileName = "PostStatus_" + lub.getLoginDDOCode() + ".xls";

            out = new BufferedOutputStream(response.getOutputStream());
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            String off_code = lub.getLoginoffcode();
            workbook = Workbook.createWorkbook(out);
            String offLvl = lub.getLoginOffLevel();

            annuaiEstablishmentDao.downloadExcelGenericPostWiseStrength(out, workbook, fileName, off_code);
            workbook.write();
            workbook.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "gpcWiseemployeeList")
    public ModelAndView genericPostWiseemployeeList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("gpc") String gpc) {
        ModelAndView mav = new ModelAndView();
        String path = "/report/gpcWiseEmployeeList";
        List empList = annuaiEstablishmentDao.gpcWiseEmployeeMapped(lub.getLoginoffcode(), gpc);
        mav.addObject("genericEmpList", empList);
        mav.setViewName(path);
        return mav;
    }

    @ResponseBody
    @RequestMapping(value = "viewDuplicateEmpJSON", method = {RequestMethod.GET, RequestMethod.POST})
    public void viewMultipleEmpInOneSpc(ModelMap model, HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("dupPostCode") String dupPostCode) {
        response.setContentType("application/json");
        PrintWriter out = null;
        JSONArray json = null;
        try {
            List dupSpcEmpList = annuaiEstablishmentDao.getMultipleEmpInOneSPC(dupPostCode, lub.getLoginoffcode());
            json = new JSONArray(dupSpcEmpList);
            out = response.getWriter();
            out.write(json.toString());

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            out.flush();
            out.close();
        }

    }
    /////----GenericPostWiseStrength For CO Office -------

    @RequestMapping(value = "GenericPostWiseStrengthForCO", method = RequestMethod.GET)
    public ModelAndView EmployeePostListForCO(HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletResponse response) throws IOException {
        ModelAndView mav = new ModelAndView();
        String path = "/report/AERPostWiseStrengthReportForCO";
        List genPostList = annuaiEstablishmentDao.getGenericPostListForCO(lub.getLoginoffcode(), lub.getLoginOffLevel());
        mav.addObject("genericPostList", genPostList);
        //mav.addObject("ofcLvlType",lub.getLoginOffLevel());

        mav.setViewName(path);
        return mav;
    }

    @RequestMapping(value = "downloadExcelPostWiseStrengthForCO")
    public void downloadPostWiseStrengthExcelForCO(@ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletResponse response) {
        response.setContentType("application/vnd.ms-excel");
        OutputStream out = null;
        WritableWorkbook workbook = null;
        try {

            String fileName = "PostStatus_" + lub.getLoginDDOCode() + ".xls";

            out = new BufferedOutputStream(response.getOutputStream());
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            String off_code = lub.getLoginoffcode();
            workbook = Workbook.createWorkbook(out);
            String offLvl = lub.getLoginOffLevel();

            annuaiEstablishmentDao.downloadExcelGenericPostWiseStrengthForCO(out, workbook, fileName, off_code, offLvl);
            workbook.write();
            workbook.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "gpcWiseemployeeListForCO")
    public ModelAndView genericPostWiseemployeeListForCO(@ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("gpc") String gpc) {
        ModelAndView mav = new ModelAndView();
        String path = "/report/gpcWiseEmployeeListForCO";
        List empList = annuaiEstablishmentDao.gpcWiseEmployeeMappedForCO(lub.getLoginoffcode(), gpc, lub.getLoginOffLevel());
        mav.addObject("genericEmpList", empList);
        mav.setViewName(path);
        return mav;
    }

    @ResponseBody
    @RequestMapping(value = "viewDuplicateEmpJSONForCO", method = {RequestMethod.GET, RequestMethod.POST})
    public void viewMultipleEmpInOneSpcForCO(ModelMap model, HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("dupPostCode") String dupPostCode) {
        response.setContentType("application/json");
        PrintWriter out = null;
        JSONArray json = null;
        try {
            List dupSpcEmpList = annuaiEstablishmentDao.getMultipleEmpInOneSPCForCO(dupPostCode, lub.getLoginoffcode(), lub.getLoginOffLevel());
            json = new JSONArray(dupSpcEmpList);
            out = response.getWriter();
            out.write(json.toString());

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            out.flush();
            out.close();
        }

    }

    @RequestMapping(value = "ViewMappedSectionAgainstGPC")
    public String ViewMappedSectionAgainstGPC(Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("postcode") String postcode) {

        try {
            List sectionnamelist = annuaiEstablishmentDao.viewMappedSectionAgainstGPC(lub.getLoginoffcode(), postcode);
            model.addAttribute("mappedsectionnamelist", sectionnamelist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/report/ViewMappedSectionAgainstPostCode";
    }

    @RequestMapping(value = "DistWiseOfficeAERReportDC")
    public ModelAndView DistWiseOfficeAERReportDC(@ModelAttribute("command") AnnualEstablishment ae, @ModelAttribute("LoginUserBean") LoginUserBean lub) {
        String path = "/misreport/DistWiseOfficeAERReportDC";
        ModelAndView mav = new ModelAndView();
        ArrayList aerDetails = annuaiEstablishmentDao.DistWiseOfficeAERReportDC(ae.getFinancialYear(), lub.getLogindistrictcode());
        mav.addObject("AERDetails", aerDetails);
        mav.setViewName(path);
        return mav;
    }

}
