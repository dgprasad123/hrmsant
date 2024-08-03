/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.performanceappraisal;

import hrms.common.CommonFunctions;
import hrms.dao.fiscalyear.FiscalYearDAO;
import hrms.dao.master.CadreDAO;
import hrms.dao.master.DepartmentDAO;
import hrms.dao.master.OfficeDAO;
import hrms.dao.performanceappraisal.PARAdminDAO;
import hrms.dao.performanceappraisal.PARBrowserDAO;
import hrms.dao.preferauthority.SactionedAuthorityDAO;
import hrms.dao.task.TaskDAO;
import hrms.model.login.LoginUserBean;
import hrms.model.login.Users;
import hrms.model.master.Cadre;
import hrms.model.master.Module;
import hrms.model.parmast.DistrictWiseAuthorizationForPolice;
import hrms.model.parmast.ParAchievement;
import hrms.model.parmast.ParAdminProperties;
import hrms.model.parmast.ParAdminSearchCriteria;
import hrms.model.parmast.ParAdverseCommunicationDetail;
import hrms.model.parmast.ParApplyForm;
import hrms.model.parmast.ParAssignPrivilage;
import hrms.model.parmast.ParDetail;
import hrms.model.parmast.Parauthorityhelperbean;
import hrms.model.parmast.UploadPreviousPAR;
import hrms.model.task.TaskListHelperBean;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
 * @author pk_cmgi
 */
@Controller
@SessionAttributes({"LoginUserBean", "Privileges", "SelectedEmpObj", "SelectedEmpOffice", "users", "parreviewedspc"})
public class SiPARAdminController implements ServletContextAware {

    @Autowired
    PARAdminDAO parAdminDAO;

    @Autowired
    FiscalYearDAO fiscalDAO;

    @Autowired
    DepartmentDAO departmentDAO;

    @Autowired
    OfficeDAO officeDao;

    @Autowired
    CadreDAO cadreDAO;

    @Autowired
    TaskDAO taskDAO;

    @Autowired
    DepartmentDAO deptDAO;

    @Autowired
    public SactionedAuthorityDAO sactionedAuthorityDao;

    @Autowired
    public PARBrowserDAO parbrowserDao;

    private ServletContext context;
    private static final Logger logger = LogManager.getLogger(SiPARAdminController.class);

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.context = servletContext;
    }

    /* ======= PAR Custodian work starts form here ========= */
    @RequestMapping(value = "SiEqivOfficerList.htm", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView viewSiEqivOfficerListPage(ModelMap model, HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub,
            @ModelAttribute("Privileges") Module[] privileges, @ModelAttribute("ParAdminSearchCriteria") ParAdminSearchCriteria parAdminBean) {

        ModelAndView mv = new ModelAndView();
        if (lub.getLoginusertype().equals("B")) {
            if (CommonFunctions.hasPrivileage(privileges, "viewSiParCustodian.htm") == true) {
                List siEqivOfficerList = parAdminDAO.getSiEqivAllOfficerDataList(lub.getLoginoffcode());
                mv.setViewName("/par/SiParAllOfficerList");
                mv.addObject("siEqivOfficerList", siEqivOfficerList);
            } else {
                mv.setViewName("/under_const");
            }
        }
        return mv;
    }

    @RequestMapping(value = "siParAdvRemarkIntimation.htm", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView ViewSiParAdverseRemarkIntimationPDF(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub,
            @ModelAttribute("parAdverseCommunicationDetail") ParAdverseCommunicationDetail parAdverseCommBean) {

        ModelAndView mv = new ModelAndView();
        int parId1 = Integer.parseInt(CommonFunctions.decodedTxt(parAdverseCommBean.getEncParId()));
        ParAdverseCommunicationDetail empData = parAdminDAO.getAdverseRemarksDetailAtCustodianSIPAR(parId1);
        String filePath = context.getInitParameter("PhotoPath");
        empData.setOpLogoFilePath(filePath);
        mv.addObject("EmpData", empData);
        mv.setViewName("siParAdvRemarkIntimation");
        return mv;
    }

    @RequestMapping(value = "SiEqivOfficerList.htm", params = "Download", method = {RequestMethod.GET, RequestMethod.POST})
    public void GetSiEqivOfficerListExcel(HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub,
            @ModelAttribute("ParAdminSearchCriteria") ParAdminSearchCriteria parAdminBean, HttpServletResponse response) {

        response.setContentType("application/vnd.ms-excel");
        OutputStream out = null;

        try {

            List siEqivOfficerList = parAdminDAO.getSiEqivAllOfficerDataList(lub.getLoginoffcode());
            String fileName = "SiEqivOfficerReport-" + CommonFunctions.getCurDateDMY() + ".xls";
            out = new BufferedOutputStream(response.getOutputStream());

            WritableWorkbook workbook = Workbook.createWorkbook(out);
            WritableSheet sheet = workbook.createSheet("Officer List", 0); // This 0 is for sheet no

            WritableFont headFont = new WritableFont(WritableFont.COURIER, 13, WritableFont.BOLD);
            headFont.setColour(Colour.BLACK);

            WritableCellFormat headFormat = new WritableCellFormat(headFont);
            //headFormat.setBackground(Colour.SKY_BLUE);
            headFormat.setBorder(Border.ALL, BorderLineStyle.THIN);

            WritableCellFormat headcell = new WritableCellFormat(headFormat);
            headcell.setAlignment(Alignment.CENTRE);
            headcell.setVerticalAlignment(VerticalAlignment.CENTRE);
            headcell.setWrap(true);

            // CELL DESIGN FOR ALL VALUES
            WritableFont dataFont = new WritableFont(WritableFont.COURIER, 11, WritableFont.NO_BOLD);
            dataFont.setColour(Colour.BLACK);

            WritableCellFormat dataFormat = new WritableCellFormat(dataFont);
            //dataFormat.setBackground(Colour.GRAY_25);
            dataFormat.setBorder(Border.ALL, BorderLineStyle.THIN);

            WritableCellFormat datacell = new WritableCellFormat(dataFormat);
            datacell.setAlignment(Alignment.LEFT);
            datacell.setVerticalAlignment(VerticalAlignment.CENTRE);
            datacell.setWrap(true);

            int rowFor1st = 0;

            Label label = new Label(0, rowFor1st++, lub.getLoginoffname(), headcell);//column,row
            sheet.addCell(label);
            sheet.mergeCells(0, 0, 8, 0);

            label = new Label(0, rowFor1st++, " Home Department, Government of Odisha. ", headcell);//column,row
            sheet.addCell(label);
            sheet.mergeCells(0, 1, 8, 1);

            label = new Label(0, rowFor1st++, " Custodian wise list of Officers for SI & Equivalent Ranks (Group - B) ", headcell);//column,row
            sheet.addCell(label);
            sheet.mergeCells(0, 2, 8, 2);

            label = new Label(0, rowFor1st, "Sl No", headcell);
            sheet.addCell(label);

            int widthInChars1 = 25;
            label = new Label(1, rowFor1st, "Employee Id", headcell);
            sheet.setColumnView(1, widthInChars1);
            sheet.addCell(label);

            label = new Label(2, rowFor1st, "GPF / PRAN NO", headcell);
            sheet.setColumnView(2, widthInChars1);
            sheet.addCell(label);

            int widthInChars2 = 45;
            label = new Label(3, rowFor1st, "Employee Name", headcell);
            sheet.setColumnView(3, widthInChars2);
            sheet.addCell(label);

            label = new Label(4, rowFor1st, "Mobile", headcell);
            sheet.setColumnView(4, widthInChars1);
            sheet.addCell(label);

            label = new Label(5, rowFor1st, "Date of Birth", headcell);
            sheet.setColumnView(5, widthInChars1);
            sheet.addCell(label);

            int widthInChars3 = 50;
            label = new Label(6, rowFor1st, "Designation", headcell);
            sheet.setColumnView(6, widthInChars3);
            sheet.addCell(label);

            label = new Label(7, rowFor1st, "Post Group", headcell);
            sheet.setColumnView(7, widthInChars1);
            sheet.addCell(label);
            
            label = new Label(8, rowFor1st, "Current Cadre", headcell);
            sheet.setColumnView(8, widthInChars3);
            sheet.addCell(label);

            int row = 4;
            int slNo = 1;
            if (siEqivOfficerList != null && siEqivOfficerList.size() > 0) {
                ParAdminProperties parDataBean = null;
                for (Object empDataList1 : siEqivOfficerList) {

                    parDataBean = (ParAdminProperties) empDataList1;

                    label = new Label(0, row, slNo + "", headcell);
                    sheet.addCell(label);

                    label = new Label(1, row, parDataBean.getEmpId(), datacell);
                    sheet.addCell(label);

                    label = new Label(2, row, parDataBean.getGpfno(), datacell);
                    sheet.addCell(label);

                    label = new Label(3, row, parDataBean.getEmpName(), datacell);
                    sheet.addCell(label);

                    label = new Label(4, row, parDataBean.getMobile(), datacell);
                    sheet.addCell(label);

                    label = new Label(5, row, parDataBean.getDob(), datacell);
                    sheet.addCell(label);

                    label = new Label(6, row, parDataBean.getPostName(), datacell);
                    sheet.addCell(label);

                    label = new Label(7, row, parDataBean.getGroupName(), datacell);
                    sheet.addCell(label);
                    
                    label = new Label(8, row, parDataBean.getCadreName(), datacell);
                    sheet.addCell(label);

                    row++;
                    slNo++;
                }
            }
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            workbook.write();
            workbook.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "viewSiParCustodian.htm", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView viewSiParCustodianPage(ModelMap model, HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub,
            @ModelAttribute("Privileges") Module[] privileges, @ModelAttribute("ParAdminSearchCriteria") ParAdminSearchCriteria parAdminBean) {

        ModelAndView mv = new ModelAndView();
        if (lub.getLoginusertype().equals("B")) {
            if (CommonFunctions.hasPrivileage(privileges, "viewSiParCustodian.htm") == true) {
                List fiscyear = fiscalDAO.getFiscalYearList();
                String fyVal = parAdminBean.getFiscalyear();
                mv.setViewName("/par/SiParCustodian");
                mv.addObject("FiscYear", fiscyear);
                mv.addObject("FyYear", fyVal);
            } else {
                mv.setViewName("/under_const");
            }
        }
        return mv;
    }

    @RequestMapping(value = "viewSiParCustodian.htm", params = "Search", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView viewSiParCustodianPageData(ModelMap model, HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub,
            @ModelAttribute("Privileges") Module[] privileges, @ModelAttribute("ParAdminSearchCriteria") ParAdminSearchCriteria parAdminBean) {
        ModelAndView mv = new ModelAndView();
        if (lub.getLoginusertype().equals("B")) {
            if (CommonFunctions.hasPrivileage(privileges, "viewSiParCustodian.htm") == true) {
                List fiscyear = fiscalDAO.getFiscalYearList();
                String fyVal = parAdminBean.getFiscalyear();
                //List parDataList = parAdminDAO.getSiParFyWiseCustodianList(fyVal, lub.getLoginoffcode(), lub.getLogindistrictcode());
                List parList = parAdminDAO.getSiParFyWiseCustodianList(fyVal, lub.getLoginoffcode(), lub.getLogindistrictcode(), parAdminBean.getSearchCriteria(), parAdminBean.getSearchString());
                mv.setViewName("/par/SiParCustodian");
                mv.addObject("FiscYear", fiscyear);
                mv.addObject("FyYear", fyVal);
                mv.addObject("parList", parList);
            } else {
                mv.setViewName("/under_const");
            }
        }
        return mv;
    }

    /*Use to get SI List in Custodian Login using JSON*/
    @ResponseBody
    @RequestMapping(value = "getSearchSiPARList.htm")
    public void getSearchSiPARList(ModelMap model, HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub,
            @ModelAttribute("Privileges") Module[] privileges, HttpServletResponse response, @ModelAttribute("ParAdminSearchCriteria") ParAdminSearchCriteria parAdminSearchCriteria) throws IOException, JSONException {
        response.setContentType("application/json");
        PrintWriter out = null;
        if (lub.getLoginusertype().equals("B")) {
            List fiscyear = fiscalDAO.getFiscalYearList();
            String fyVal = parAdminSearchCriteria.getFiscalyear();
            List parList = parAdminDAO.getSiParFyWiseCustodianList(fyVal, lub.getLoginoffcode(), lub.getLogindistrictcode(), parAdminSearchCriteria.getSearchCriteria(), parAdminSearchCriteria.getSearchString());

            JSONArray obj = new JSONArray(parList);
            out = response.getWriter();
            out.write(obj.toString());
            out.flush();
            out.close();
        }
    }

    @RequestMapping(value = "viewSiParCustodian.htm", params = "Back", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView Back2SiParCustodianPageData(HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub,
            @ModelAttribute("ParAdminSearchCriteria") ParAdminSearchCriteria parAdminBean) {

        ModelAndView mv = new ModelAndView();
        if (lub.getLoginusertype().equals("B")) {
            List fiscyear = fiscalDAO.getFiscalYearList();
            String fyYear = parAdminBean.getFiscalyear();
            //List parDataList = parAdminDAO.getSiParFyWiseCustodianList(fyYear, lub.getLoginoffcode(), lub.getLogindistrictcode());
            List parList = parAdminDAO.getSiParFyWiseCustodianList(fyYear, lub.getLoginoffcode(), lub.getLogindistrictcode(), parAdminBean.getSearchCriteria(), parAdminBean.getSearchString());
            parAdminBean.setFiscalyear(fyYear);
            mv = new ModelAndView("/par/SiParCustodian");
            mv.addObject("FiscYear", fiscyear);
            mv.addObject("FyYear", fyYear);
            mv.addObject("ParDataList", parList);
        } else {
            mv.setViewName("/under_const");
        }
        return mv;
    }

    @RequestMapping(value = "uploadPreviousYearSiParList.htm", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView LoadPreviousYearSiParListPage(ModelMap model, HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub,
            @ModelAttribute("Privileges") Module[] privileges) {

        ModelAndView mv = new ModelAndView();
        if (lub.getLoginusertype().equals("B")) {
            if (CommonFunctions.hasPrivileage(privileges, "uploadPreviousYearSiParList.htm") == true) {
                List fiscyear = fiscalDAO.getFiscalYearList();
                List uploadPARList = parAdminDAO.getPreviousYearPARUpload(lub.getLoginempid());

                mv.setViewName("/par/UploadPreviousYearSiParList");
                mv.addObject("UploadSiParList", uploadPARList);
                mv.addObject("FiscYear", fiscyear);

            } else {
                mv.setViewName("/under_const");
            }
        }
        return mv;
    }

    @ResponseBody
    @RequestMapping(value = "getEmpAndSiParData.htm", method = {RequestMethod.GET, RequestMethod.POST})
    public void GetEmpAndSiParData(@ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletResponse response,
            @RequestParam Map<String, String> requestParams) throws IOException {

        response.setContentType("application/html");
        PrintWriter out = response.getWriter();
        String empId = "";
        String fyVal = "";
        String fromDate = "";
        String toDate = "";
        String returnData = "";

        try {

            if (requestParams.get("linkId") != null) {
                empId = requestParams.get("linkId").trim();
                fyVal = requestParams.get("fyVal").trim();
                fromDate = requestParams.get("fDate").trim();
                toDate = requestParams.get("tDate").trim();
            }

            boolean isDuplicatePeriod = parbrowserDao.isDuplicatePARPeriod(empId, fromDate, toDate, fyVal, "");
            Date fromDate1 = new SimpleDateFormat("dd-MMM-yyyy").parse(fromDate);
            Date toDate1 = new SimpleDateFormat("dd-MMM-yyyy").parse(toDate);
            int diffInDays = (int) ((toDate1.getTime() - fromDate1.getTime()) / (1000 * 60 * 60 * 24));
            Users emp = parAdminDAO.getEmployeeProfileInfo(empId);
            String errMsg = "";
            if (isDuplicatePeriod == false) {
                errMsg = "Err";// PAR Data Present for the financial year date range
            } else if (isDuplicatePeriod == true) {
                errMsg = "Pass";//NO PAR Present for the financial year date range
            }

            if (diffInDays < 120) {
                errMsg = "Err1";
            }
            returnData = emp.getFullName() + " (" + emp.getGpfno() + ")" + "@" + errMsg;
            //returnData = emp.getFullName()+" ("+emp.getGpfno()+")"+"@"+errMsg+"@"+diffInDays;
            out.print(returnData);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.close();
            out.flush();
        }
    }

    @RequestMapping(value = "uploadPreviousYearSiPar.htm", params = "UploadPar", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView UploadOldSiParCustodianPage(ModelMap model, HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub,
            @ModelAttribute("Privileges") Module[] privileges, @ModelAttribute("UploadPreviousPAR") UploadPreviousPAR uplPrevSiPar) {

        ModelAndView mv = new ModelAndView();
        if (lub.getLoginusertype().equals("B")) {
            if (CommonFunctions.hasPrivileage(privileges, "viewSiParCustodian.htm") == true) {
                List fiscyear = fiscalDAO.getFiscalYearList();
                String fyVal = uplPrevSiPar.getFiscalyear();
                List deptList = deptDAO.getDepartmentList();
                List gradeList = parbrowserDao.getPARGradeList();

                mv.setViewName("/par/UploadPreviousYearSiPar");
                mv.addObject("FiscYear", fiscyear);
                mv.addObject("DeptList", deptList);
                mv.addObject("GradeList", gradeList);
                mv.addObject("FyYear", fyVal);

            } else {
                mv.setViewName("/under_const");
            }
        }
        return mv;
    }

    @RequestMapping(value = "uploadPreviousYearSiPar.htm", params = "Save", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView SaveUploadOldSiParData(ModelMap model, HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub,
            @ModelAttribute("Privileges") Module[] privileges, @ModelAttribute("UploadPreviousPAR") UploadPreviousPAR uplPrevSiPar) {

        ModelAndView mv = new ModelAndView("/par/UploadPreviousYearSiPar");
        if (lub.getLoginusertype().equals("B")) {
            if (CommonFunctions.hasPrivileage(privileges, "viewSiParCustodian.htm") == true) {
                String fyVal = uplPrevSiPar.getFiscalyear();
                uplPrevSiPar.setPreviousyrParUploadedbyempId(lub.getLoginempid());
                uplPrevSiPar.setPreviousyrParUploadedbyspc(lub.getLoginspc());
                boolean isDuplicatePeriod = parbrowserDao.isDuplicatePARPeriod(uplPrevSiPar.getEmpId(), uplPrevSiPar.getPrdFrmDate(), uplPrevSiPar.getPrdToDate(), fyVal, "");
                if (isDuplicatePeriod == true) {
                    uplPrevSiPar.setParType("SiPar");
                    uplPrevSiPar.setHidcadrename("1484");
                    uplPrevSiPar.setPostGroupType("B");
                    uplPrevSiPar.setFiscalyear(fyVal);
                    parAdminDAO.saveEmployeeListForUploadPAR(uplPrevSiPar);
                    mv.addObject("ParErrMsg", "Uploaded Previous Year PAR Successfully.");
                } else {
                    mv.addObject("ParErrMsg", "Duplicate Period.");
                }
            } else {
                mv = new ModelAndView("/under_const");
            }
        }
        return mv;
    }

    @RequestMapping(value = "SiParDataCustodianView.htm", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView SiParDetailDataCustodianView(ModelMap model, HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub,
            @ModelAttribute("ParAdminProperties") ParAdminProperties ParAdminProperties, @RequestParam("empId") String empId,
            @RequestParam("fiscalyear") String fiscalYear, @RequestParam("encParId") String parIdEnc, @ModelAttribute("parDetail") ParDetail parDetail) {

        ModelAndView mv = new ModelAndView("/par/SiParCustodianView");
        mv.addObject("Fyear", fiscalYear);
        String parViewPrivilige = parAdminDAO.checkParViewPrivilege(lub.getLoginspc());
        parIdEnc = CommonFunctions.decodedTxt(parIdEnc);
        List pardetail = parAdminDAO.getSiParDetails(empId, fiscalYear, parIdEnc);
        int taskid = parbrowserDao.getTaskid(lub.getLoginempid(), ParAdminProperties.getParId());
        String[] revertreason = parbrowserDao.getRevertReason(ParAdminProperties.getParId(), taskid);

        mv.addObject("pardetail", pardetail);
        mv.addObject("PrivType", parViewPrivilige);
        mv.addObject("usertype", lub.getLoginusertype());
        mv.addObject("authorityType", revertreason[0]);
        mv.addObject("revertRemarks", revertreason[1]);
        mv.addObject("authorityName", revertreason[2]);
        mv.setViewName("/par/SiParCustodianView"); //ParViewerSiDetails
        return mv;
    }

    //@RequestMapping(value = "viewSiParCustodian.htm", params = "Download", method = {RequestMethod.GET, RequestMethod.POST})
    @RequestMapping(value = "downloadSiParCustodian.htm")
    public void GetSiParCustodianExcel(HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub,
            @ModelAttribute("ParAdminSearchCriteria") ParAdminSearchCriteria parAdminBean, HttpServletResponse response) {

        response.setContentType("application/vnd.ms-excel");
        OutputStream out = null;

        try {
            //List parDataList = parAdminDAO.getSiParFyWiseCustodianList(parAdminBean.getFiscalyear(), lub.getLoginoffcode(), lub.getLogindistrictcode());
            List parlist = parAdminDAO.getSiParFyWiseCustodianList(parAdminBean.getFiscalyear(), lub.getLoginoffcode(), lub.getLogindistrictcode(), parAdminBean.getSearchCriteria(), parAdminBean.getSearchString());
            String fileName = "SiParReport-" + CommonFunctions.getCurDateDMY() + ".xls";
            out = new BufferedOutputStream(response.getOutputStream());

            WritableWorkbook workbook = Workbook.createWorkbook(out);
            WritableSheet sheet = workbook.createSheet("PAR List", 0); // This 0 is for sheet no

            WritableFont headFont = new WritableFont(WritableFont.COURIER, 13, WritableFont.BOLD);
            headFont.setColour(Colour.BLACK);

            WritableCellFormat headFormat = new WritableCellFormat(headFont);
            //headFormat.setBackground(Colour.SKY_BLUE);
            headFormat.setBorder(Border.ALL, BorderLineStyle.THIN);

            WritableCellFormat headcell = new WritableCellFormat(headFormat);
            headcell.setAlignment(Alignment.CENTRE);
            headcell.setVerticalAlignment(VerticalAlignment.CENTRE);
            headcell.setWrap(true);

            // CELL DESIGN FOR ALL VALUES
            WritableFont dataFont = new WritableFont(WritableFont.COURIER, 11, WritableFont.NO_BOLD);
            dataFont.setColour(Colour.BLACK);

            WritableCellFormat dataFormat = new WritableCellFormat(dataFont);
            //dataFormat.setBackground(Colour.GRAY_25);
            dataFormat.setBorder(Border.ALL, BorderLineStyle.THIN);

            WritableCellFormat datacell = new WritableCellFormat(dataFormat);
            datacell.setAlignment(Alignment.LEFT);
            datacell.setVerticalAlignment(VerticalAlignment.CENTRE);
            datacell.setWrap(true);

            int rowFor1st = 0;

            Label label = new Label(0, rowFor1st++, lub.getLoginoffname(), headcell);//column,row
            sheet.addCell(label);
            sheet.mergeCells(0, 0, 12, 0);

            label = new Label(0, rowFor1st++, " Home Department, Government of Odisha. ", headcell);//column,row
            sheet.addCell(label);
            sheet.mergeCells(0, 1, 12, 1);

            label = new Label(0, rowFor1st++, " Performance Appraisal Report (PAR) for SI & Equivalent Ranks (Group - B) of " + parAdminBean.getFiscalyear(), headcell);//column,row
            sheet.addCell(label);
            sheet.mergeCells(0, 2, 12, 2);

            label = new Label(0, rowFor1st, "Sl No", headcell);
            sheet.addCell(label);

            int widthInChars1 = 15;
            label = new Label(1, rowFor1st, "Financial Year", headcell);
            sheet.setColumnView(1, widthInChars1);
            sheet.addCell(label);

            int widthInChars2 = 25;
            label = new Label(2, rowFor1st, "Employee Id" + "\n" + "GPF / PRAN NO", headcell);
            sheet.setColumnView(2, widthInChars2);
            sheet.addCell(label);

            int widthInChars3 = 40;
            label = new Label(3, rowFor1st, "Employee Name" + "\n" + "Mobile", headcell);
            sheet.setColumnView(3, widthInChars3);
            sheet.addCell(label);

            label = new Label(4, rowFor1st, "Date of Birth", headcell);
            sheet.setColumnView(4, widthInChars1);
            sheet.addCell(label);

            label = new Label(5, rowFor1st, "Post Group", headcell);
            sheet.setColumnView(5, widthInChars1);
            sheet.addCell(label);

            int widthInChars6 = 35;
            label = new Label(6, rowFor1st, "Designation", headcell);
            sheet.setColumnView(6, widthInChars6);
            sheet.addCell(label);

            label = new Label(7, rowFor1st, "Cadre", headcell);
            sheet.setColumnView(7, widthInChars2);
            sheet.addCell(label);

            label = new Label(8, rowFor1st, "Place of Posting", headcell);
            sheet.setColumnView(8, widthInChars3);
            sheet.addCell(label);

            label = new Label(9, rowFor1st, "Status", headcell);
            sheet.setColumnView(9, widthInChars3);
            sheet.mergeCells(9, rowFor1st, 12, rowFor1st);
            sheet.addCell(label);

            int row2nd = rowFor1st + 1;
            label = new Label(0, row2nd, " ", headcell);
            sheet.mergeCells(0, 4, 8, 4);
            sheet.addCell(label);

            label = new Label(9, row2nd, " Period ", headcell);
            sheet.setColumnView(9, widthInChars2);
            sheet.addCell(label);

            label = new Label(10, row2nd, "Status", headcell);
            sheet.setColumnView(10, widthInChars3);
            sheet.addCell(label);

            label = new Label(11, row2nd, " Pending At Authority with Designation ", headcell);
            sheet.setColumnView(11, widthInChars3);
            sheet.addCell(label);

            label = new Label(12, row2nd, " Pending At Authority From ", headcell);
            sheet.setColumnView(12, widthInChars3);
            sheet.addCell(label);

            int row = 5;
            int slNo = 1;
            if (parlist != null && parlist.size() > 0) {
                ParAdminProperties parDataBean = null;
                for (Object parDataList1 : parlist) {

                    parDataBean = (ParAdminProperties) parDataList1;

                    label = new Label(0, row, slNo + "", headcell);
                    sheet.addCell(label);

                    label = new Label(1, row, parDataBean.getFiscalyear(), datacell);
                    sheet.addCell(label);

                    label = new Label(2, row, parDataBean.getEmpId() + "\n" + parDataBean.getGpfno(), datacell);
                    sheet.addCell(label);

                    label = new Label(3, row, parDataBean.getEmpName() + "\n" + parDataBean.getMobile(), datacell);
                    sheet.addCell(label);

                    label = new Label(4, row, parDataBean.getDob(), datacell);
                    sheet.addCell(label);

                    label = new Label(5, row, parDataBean.getGroupName(), datacell);
                    sheet.addCell(label);

                    label = new Label(6, row, parDataBean.getPostName(), datacell);
                    sheet.addCell(label);

                    label = new Label(7, row, parDataBean.getCadreName(), datacell);
                    sheet.addCell(label);

                    label = new Label(8, row, parDataBean.getCurrentOfficeName(), datacell);
                    sheet.addCell(label);

                    //List parInnerDataList = parAdminDAO.getSiParDetails(parDataBean.getEmpId(), parDataBean.getFiscalyear());
                    List parInnerDataList = parDataBean.getSiParInnerDataList();
                    if (parInnerDataList != null && parInnerDataList.size() > 0) {
                        ParAdminProperties pbf = null;
                        for (Object parInnerList : parInnerDataList) {

                            pbf = (ParAdminProperties) parInnerList;

                            label = new Label(9, row, pbf.getPrdFrmDate() + " - to -" + pbf.getPrdToDate(), datacell);
                            sheet.addCell(label);

                            label = new Label(10, row, pbf.getParstatus(), datacell);
                            sheet.setColumnView(10, widthInChars3);
                            sheet.addCell(label);

                            String pendingAuthName = "";
                            if (pbf.getPendingAtAuthName() != null && !pbf.getPendingAtAuthName().equals("")) {
                                pendingAuthName = pbf.getPendingAtAuthName() + ", " + pbf.getPendingAtSpc();
                            }
                            if (pbf.getNrcDetails() != null && !pbf.getNrcDetails().equals("")) {
                                pendingAuthName = pbf.getNrcDetails();
                            }
                            int widthInChars5 = 50;
                            label = new Label(11, row, pendingAuthName, datacell);
                            sheet.setColumnView(11, widthInChars5);
                            sheet.addCell(label);

                            label = new Label(12, row, pbf.getParPendingDateFrom(), datacell);
                            sheet.addCell(label);

                            row++;
                        }
                    } else {
                        row++;
                    }
                    slNo++;
                }
            }
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            workbook.write();
            workbook.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /* ======= PAR Custodian work ends form here ========= */
    /* ======= Use For Download Complete SI PAR pdf ========= */
    @RequestMapping(value = "SiParPDF.htm", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView ViewTotalSiParPDF(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub,
            @ModelAttribute("parApplyForm") ParApplyForm parApplyForm, @ModelAttribute("parauthorityhelperbean") Parauthorityhelperbean parauthorityhelperbean,
            @ModelAttribute("parAchievement") ParAchievement parAchievement) {
        ModelAndView mv = new ModelAndView();
        String parId = CommonFunctions.decodedTxt(parApplyForm.getEncryptedParid());
        String taskId = CommonFunctions.decodedTxt(parApplyForm.getEncryptedTaskid());
        int parId1 = Integer.parseInt(parId);
        int taskId1 = Integer.parseInt(taskId);
        ParApplyForm paf = parAdminDAO.viewTotalSiParDetail(parId1, taskId1);
        mv.addObject("paf", paf);
        mv.setViewName("SiParPdfView");
        return mv;
    }

    @RequestMapping(value = "viewSiPARAdmin.htm", method = RequestMethod.GET)
    public ModelAndView viewSiPARAdminPage(ModelMap model, HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub,
            @ModelAttribute("Privileges") Module[] privileges, @ModelAttribute("ParAdminSearchCriteria") ParAdminSearchCriteria parAdminBean) {

        ModelAndView mv = new ModelAndView();
        if (lub.getLoginuserid().equals("dgpoliceacr")) {
            if (CommonFunctions.hasPrivileage(privileges, "viewSiPARAdmin.htm") == true) {
                List fiscyear = fiscalDAO.getFiscalYearList();
                mv.setViewName("/par/ParAdminSi");
                mv.addObject("FiscYear", fiscyear);
            } else {
                mv.setViewName("/under_const");
            }
        }
        return mv;
    }

    @RequestMapping(value = "viewSiPARAdmin.htm", params = "Search", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView GetTotalSiParList(HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub,
            @ModelAttribute("ParAdminSearchCriteria") ParAdminSearchCriteria parAdminBean) {

        ModelAndView mv = null;
        if (lub.getLoginuserid().equals("dgpoliceacr")) {
            List fiscyear = fiscalDAO.getFiscalYearList();
            String fyYear = parAdminBean.getFiscalyear();
            List parDataList = parAdminDAO.getAllSiPARFyWiseList(parAdminBean);
            parAdminBean.setFiscalyear(fyYear);
            mv = new ModelAndView("/par/ParAdminSi");
            mv.addObject("FiscYear", fiscyear);
            mv.addObject("FyYear", fyYear);
            mv.addObject("SiParDataList", parDataList);
        }
        return mv;
    }

    @RequestMapping(value = "viewSiPARAdmin.htm", params = "Back", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView Back2TotalSiParList(HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub,
            @ModelAttribute("ParAdminSearchCriteria") ParAdminSearchCriteria parAdminBean) {

        ModelAndView mv = null;
        if (lub.getLoginuserid().equals("dgpoliceacr")) {
            List fiscyear = fiscalDAO.getFiscalYearList();
            String fyYear = parAdminBean.getFiscalyear();
            List parDataList = parAdminDAO.getAllSiPARFyWiseList(parAdminBean);
            parAdminBean.setFiscalyear(fyYear);
            mv = new ModelAndView("/par/ParAdminSi");
            mv.addObject("FiscYear", fiscyear);
            mv.addObject("FyYear", fyYear);
            mv.addObject("SiParDataList", parDataList);
        }
        return mv;
    }

    @RequestMapping(value = "ParAdminSiView.htm", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView ParAdminSiView(ModelMap model, HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub,
            @ModelAttribute("ParAdminProperties") ParAdminProperties ParAdminProperties, @RequestParam("empId") String empId,
            @RequestParam("fiscalyear") String fiscalYear, @RequestParam("encParId") String parIdEnc, @ModelAttribute("parDetail") ParDetail parDetail) {

        ModelAndView mv = new ModelAndView("/par/ParAdminSiView");
        mv.addObject("Fyear", fiscalYear);
        String parViewPrivilige = parAdminDAO.checkParViewPrivilege(lub.getLoginspc());
        parIdEnc = CommonFunctions.decodedTxt(parIdEnc);
        List pardetail = parAdminDAO.getSiParDetails(empId, fiscalYear, parIdEnc);
        int taskid = parbrowserDao.getTaskid(lub.getLoginempid(), ParAdminProperties.getParId());
        String[] revertreason = parbrowserDao.getRevertReason(ParAdminProperties.getParId(), taskid);

        mv.addObject("pardetail", pardetail);
        mv.addObject("PrivType", parViewPrivilige);
        mv.addObject("usertype", lub.getLoginusertype());
        mv.addObject("authorityType", revertreason[0]);
        mv.addObject("revertRemarks", revertreason[1]);
        mv.addObject("authorityName", revertreason[2]);
        mv.setViewName("/par/ParAdminSiView"); //ParViewerSiDetails
        return mv;
    }

    @RequestMapping(value = "viewSiPARAdmin.htm", params = "Download", method = {RequestMethod.GET, RequestMethod.POST})
    public void GetTotalSiParListExcel(HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub,
            @ModelAttribute("ParAdminSearchCriteria") ParAdminSearchCriteria parAdminBean, HttpServletResponse response) {

        response.setContentType("application/vnd.ms-excel");
        OutputStream out = null;

        try {
            List parDataList = parAdminDAO.getAllSiPARFyWiseList(parAdminBean);

            String fileName = "SiParReport-" + CommonFunctions.getCurDateDMY() + ".xls";
            out = new BufferedOutputStream(response.getOutputStream());

            WritableWorkbook workbook = Workbook.createWorkbook(out);
            WritableSheet sheet = workbook.createSheet("PAR List", 0); // This 0 is for sheet no

            WritableFont headFont = new WritableFont(WritableFont.COURIER, 13, WritableFont.BOLD);
            headFont.setColour(Colour.BLACK);

            WritableCellFormat headFormat = new WritableCellFormat(headFont);
            //headFormat.setBackground(Colour.SKY_BLUE);
            headFormat.setBorder(Border.ALL, BorderLineStyle.THIN);

            WritableCellFormat headcell = new WritableCellFormat(headFormat);
            headcell.setAlignment(Alignment.CENTRE);
            headcell.setVerticalAlignment(VerticalAlignment.CENTRE);
            headcell.setWrap(true);

            // CELL DESIGN FOR ALL VALUES
            WritableFont dataFont = new WritableFont(WritableFont.COURIER, 11, WritableFont.NO_BOLD);
            dataFont.setColour(Colour.BLACK);

            WritableCellFormat dataFormat = new WritableCellFormat(dataFont);
            //dataFormat.setBackground(Colour.GRAY_25);
            dataFormat.setBorder(Border.ALL, BorderLineStyle.THIN);

            WritableCellFormat datacell = new WritableCellFormat(dataFormat);
            datacell.setAlignment(Alignment.LEFT);
            datacell.setVerticalAlignment(VerticalAlignment.CENTRE);
            datacell.setWrap(true);

            int rowFor1st = 0;

            Label label = new Label(0, rowFor1st++, lub.getLoginoffname(), headcell);//column,row
            sheet.addCell(label);
            sheet.mergeCells(0, 0, 12, 0);

            label = new Label(0, rowFor1st++, " Home Department, Government of Odisha. ", headcell);//column,row
            sheet.addCell(label);
            sheet.mergeCells(0, 1, 12, 1);

            label = new Label(0, rowFor1st++, " Performance Appraisal Report (PAR) for SI & Equivalent Ranks (Group - B) of " + parAdminBean.getFiscalyear(), headcell);//column,row
            sheet.addCell(label);
            sheet.mergeCells(0, 2, 12, 2);

            label = new Label(0, rowFor1st, "Sl No", headcell);
            sheet.addCell(label);

            int widthInChars1 = 15;
            label = new Label(1, rowFor1st, "Financial Year", headcell);
            sheet.setColumnView(1, widthInChars1);
            sheet.addCell(label);

            int widthInChars2 = 25;
            label = new Label(2, rowFor1st, "Employee Id" + "\n" + "GPF / PRAN NO", headcell);
            sheet.setColumnView(2, widthInChars2);
            sheet.addCell(label);

            int widthInChars3 = 40;
            label = new Label(3, rowFor1st, "Employee Name" + "\n" + "Mobile", headcell);
            sheet.setColumnView(3, widthInChars3);
            sheet.addCell(label);

            label = new Label(4, rowFor1st, "Date of Birth", headcell);
            sheet.setColumnView(4, widthInChars1);
            sheet.addCell(label);

            label = new Label(5, rowFor1st, "Post Group", headcell);
            sheet.setColumnView(5, widthInChars1);
            sheet.addCell(label);

            int widthInChars6 = 35;
            label = new Label(6, rowFor1st, "Designation", headcell);
            sheet.setColumnView(6, widthInChars6);
            sheet.addCell(label);

            label = new Label(7, rowFor1st, "Cadre", headcell);
            sheet.setColumnView(7, widthInChars2);
            sheet.addCell(label);

            label = new Label(8, rowFor1st, "Place of Posting", headcell);
            sheet.setColumnView(8, widthInChars3);
            sheet.addCell(label);

            label = new Label(9, rowFor1st, "Status", headcell);
            sheet.setColumnView(9, widthInChars3);
            sheet.mergeCells(9, rowFor1st, 12, rowFor1st);
            sheet.addCell(label);

            int row2nd = rowFor1st + 1;
            label = new Label(0, row2nd, " ", headcell);
            sheet.mergeCells(0, 4, 8, 4);
            sheet.addCell(label);

            label = new Label(9, row2nd, " Period ", headcell);
            sheet.setColumnView(9, widthInChars2);
            sheet.addCell(label);

            label = new Label(10, row2nd, "Status", headcell);
            sheet.setColumnView(10, widthInChars3);
            sheet.addCell(label);

            label = new Label(11, row2nd, " Pending At Authority with Designation ", headcell);
            sheet.setColumnView(11, widthInChars3);
            sheet.addCell(label);

            label = new Label(12, row2nd, " Pending At Authority From ", headcell);
            sheet.setColumnView(12, widthInChars3);
            sheet.addCell(label);

            int row = 5;
            int slNo = 1;
            if (parDataList != null && parDataList.size() > 0) {
                ParAdminProperties parDataBean = null;
                for (Object parDataList1 : parDataList) {

                    parDataBean = (ParAdminProperties) parDataList1;

                    label = new Label(0, row, slNo + "", headcell);
                    sheet.addCell(label);

                    label = new Label(1, row, parDataBean.getFiscalyear(), datacell);
                    sheet.addCell(label);

                    label = new Label(2, row, parDataBean.getEmpId() + "\n" + parDataBean.getGpfno(), datacell);
                    sheet.addCell(label);

                    label = new Label(3, row, parDataBean.getEmpName() + "\n" + parDataBean.getMobile(), datacell);
                    sheet.addCell(label);

                    label = new Label(4, row, parDataBean.getDob(), datacell);
                    sheet.addCell(label);

                    label = new Label(5, row, parDataBean.getGroupName(), datacell);
                    sheet.addCell(label);

                    label = new Label(6, row, parDataBean.getPostName(), datacell);
                    sheet.addCell(label);

                    label = new Label(7, row, parDataBean.getCadreName(), datacell);
                    sheet.addCell(label);

                    label = new Label(8, row, parDataBean.getCurrentOfficeName(), datacell);
                    sheet.addCell(label);

                    //List parInnerDataList = parAdminDAO.getSiParDetails(parDataBean.getEmpId(), parDataBean.getFiscalyear());
                    List parInnerDataList = parDataBean.getSiParInnerDataList();
                    if (parInnerDataList != null && parInnerDataList.size() > 0) {
                        ParAdminProperties pbf = null;
                        for (Object parInnerList : parInnerDataList) {

                            pbf = (ParAdminProperties) parInnerList;

                            label = new Label(9, row, pbf.getPrdFrmDate() + " - to -" + pbf.getPrdToDate(), datacell);
                            sheet.addCell(label);

                            label = new Label(10, row, pbf.getParstatus(), datacell);
                            sheet.setColumnView(10, widthInChars3);
                            sheet.addCell(label);

                            String pendingAuthName = "";
                            if (pbf.getPendingAtAuthName() != null && !pbf.getPendingAtAuthName().equals("")) {
                                pendingAuthName = pbf.getPendingAtAuthName() + ", " + pbf.getPendingAtSpc();
                            }
                            if (pbf.getNrcDetails() != null && !pbf.getNrcDetails().equals("")) {
                                pendingAuthName = pbf.getNrcDetails();
                            }
                            int widthInChars5 = 50;
                            label = new Label(11, row, pendingAuthName, datacell);
                            sheet.setColumnView(11, widthInChars5);
                            sheet.addCell(label);

                            label = new Label(12, row, pbf.getParPendingDateFrom(), datacell);
                            sheet.addCell(label);

                            row++;
                        }
                    } else {
                        row++;
                    }
                    slNo++;
                }
            }
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            workbook.write();
            workbook.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "viewSiParDetail.htm", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView viewSiParDetail(ModelMap model, HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub,
            @ModelAttribute("ParAdminProperties") ParAdminProperties ParAdminProperties, @RequestParam("empId") String empId,
            @RequestParam("fiscalyear") String fiscalYear, @RequestParam("encParId") String parIdEnc, @ModelAttribute("parDetail") ParDetail parDetail) {

        ModelAndView mv = new ModelAndView("/par/PARViewerSI");
        mv.addObject("Fyear", fiscalYear);
        String parViewPrivilige = parAdminDAO.checkParViewPrivilege(lub.getLoginspc());
        parIdEnc = CommonFunctions.decodedTxt(parIdEnc);
        List pardetail = parAdminDAO.getSiParDetails(empId, fiscalYear, parIdEnc);
        int taskid = parbrowserDao.getTaskid(lub.getLoginempid(), ParAdminProperties.getParId());
        String[] revertreason = parbrowserDao.getRevertReason(ParAdminProperties.getParId(), taskid);

        mv.addObject("pardetail", pardetail);
        mv.addObject("PrivType", parViewPrivilige);
        mv.addObject("usertype", lub.getLoginusertype());
        mv.addObject("authorityType", revertreason[0]);
        mv.addObject("revertRemarks", revertreason[1]);
        mv.addObject("authorityName", revertreason[2]);
        mv.setViewName("/par/ParViewerSiDetails");
        return mv;
    }

    /*Use For get all Details Of PAR By PAR Admin*/
    @RequestMapping(value = "viewTotalSiParDetail.htm")
    public ModelAndView viewTotalSiParDetail(HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam Map<String, String> requestParams) {

        int encParId = 0;
        int encTaskId = 0;
        if (requestParams.get("parId") != null) {
            encParId = Integer.parseInt(CommonFunctions.decodedTxt(requestParams.get("parId").trim()));
        } else if (requestParams.get("taskId") != null) {
            encTaskId = Integer.parseInt(CommonFunctions.decodedTxt(requestParams.get("taskId").trim()));
        }
        ModelAndView mv = new ModelAndView();
        ParApplyForm paf = parAdminDAO.viewTotalSiParDetail(encParId, encTaskId);
        List reportingdata = parAdminDAO.getParAuthority("REPORTING", encParId, encTaskId);
        List reviewingdata = parAdminDAO.getParAuthority("REVIEWING", encParId, encTaskId);
        List acceptingdata = parAdminDAO.getParAuthority("ACCEPTING", encParId, encTaskId);
        String parViewPrivilige = parAdminDAO.checkParViewPrivilege(lub.getLoginspc());

        List gradelist = parbrowserDao.getPARGradeList();
        mv.addObject("gradelist", gradelist);
        mv.addObject("parApplyForm", paf);
        mv.addObject("PrivType", parViewPrivilige);
        mv.addObject("reporting", reportingdata);
        mv.addObject("reviewing", reviewingdata);
        mv.addObject("accepting", acceptingdata);
        mv.addObject("Loginempid", lub.getLoginempid());
        mv.addObject("IsReviewed", 'Y');
        mv.setViewName("/par/SiParAdminViewDetail");
        return mv;
    }

    /*Use To get Districtwise Privilege For Police SI PAR*/
    @RequestMapping(value = "DistrictWisePrivilegeForPoliceSiPar")
    public ModelAndView LoadDistWisePrivilege4PoliceSiParPage(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("cadre") Cadre cadre,
            @ModelAttribute("parApplyForm") ParApplyForm parApplyForm) {

        ModelAndView mv = new ModelAndView();
        if (lub.getLoginuserid().equals("dgpoliceacr")) {
            mv.setViewName("/par/PARDistrictwisePrivilegeForPoliceSI");
            List distWisePrivilegedList = parAdminDAO.getAssignedPrivilegeListDistWiseForPoliceSiPar();
            mv.addObject("distWisePrivilegedList", distWisePrivilegedList);
            mv.addObject("departmentList", departmentDAO.getDepartmentList4SiPAR());
        } else {
            mv.setViewName("/under_const");
        }
        return mv;
    }

//    /*Use To get Districtwise Assigned PrivilegeList For Police SI PAR*/
//    @RequestMapping(value = "DistrictWisePrivilegeForPoliceSiPar.htm", method = {RequestMethod.GET}, params = {"action=Get Assigned PrivilegeListSi"})
//    public ModelAndView getDistWisePrivilege4PoliceSiParList(@ModelAttribute("LoginUserBean") LoginUserBean lub) {
//        ModelAndView mav = new ModelAndView("/par/PARDistrictwisePrivilegeForPoliceSI");
//        List distWisePrivilegedList = parAdminDAO.getAssignedPrivilegeListDistWiseForPoliceSiPar();
//        mav.addObject("distWisePrivilegedList", distWisePrivilegedList);
//        return mav;
//    }
    /*Use To Assigned PrivilegeList Officewise*/
    @ResponseBody
    @RequestMapping(value = "saveOfficewisePrivilage.htm")
    public void assignOfficewisePrivilage4SiPar(ModelMap model, HttpServletRequest request, HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("parApplyForm") ParApplyForm parApplyForm) throws IOException, JSONException {
        response.setContentType("application/json");
        PrintWriter out = null;
        ModelAndView mv = new ModelAndView();
        String msg = parAdminDAO.saveAssignOfficewisePrivilige4SiPar(parApplyForm);
        JSONObject obj = new JSONObject();
        obj.append("msg", msg);
        out = response.getWriter();
        out.write(obj.toString());
    }

    /*Use To delete Districtwise Assigned PrivilegeList For Police SI PAR*/
    @RequestMapping(value = "removeDistWisePrivilege4SiPar.htm")
    public ModelAndView RemoveDistrictWisePrivilege4SiPar(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("districtWiseAuthorizationForPolice") DistrictWiseAuthorizationForPolice districtWiseAuthorizationForPolice) {
        parAdminDAO.deletePrivilegedListDistWiseForPoliceSi(districtWiseAuthorizationForPolice.getSpc(), districtWiseAuthorizationForPolice.getPostGrp());
        ModelAndView mv = new ModelAndView("/par/PARDistrictwisePrivilegeForPoliceSI");
        List districtwiseprivilegedList = parAdminDAO.getAssignedPrivilegeListDistWiseForPoliceSiPar();
        mv.addObject("distWisePrivilegedList", districtwiseprivilegedList);
        return mv;
    }

    /*Use To remove OfficeWise Privilege*/
    @ResponseBody
    @RequestMapping(value = "removeSiParPrivilage.htm")
    public void RemoveOfficeWisePrivilage(ModelMap model, HttpServletRequest request, HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("parApplyForm") ParApplyForm parApplyForm) throws IOException, JSONException {
        response.setContentType("application/json");
        PrintWriter out = null;
        parAdminDAO.deleteOfficeWiseSiPrivilage(parApplyForm.getOffcode(), parApplyForm.getSpc(), parApplyForm.getPostGroup());
        JSONArray json = new JSONArray();
        out = response.getWriter();
        out.write(json.toString());
        out.flush();
        out.close();
    }

    /*Use To get Assigned PrivilegeList Officewise*/
    @ResponseBody
    @RequestMapping(value = "getOfficeWiseAssignPrivilegeSiPar.htm")
    public void GetOfficeWiseAssignPrivilegeSiPar(ModelMap model, HttpServletRequest request, HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("parApplyForm") ParApplyForm parApplyForm) throws IOException, JSONException {
        response.setContentType("application/json");
        PrintWriter out = null;
        List officewiseprivilegedList = parAdminDAO.getOfficeWiseAssignedPrivilegeList4SiPar(parApplyForm.getOffcode());
        JSONArray json = new JSONArray(officewiseprivilegedList);
        out = response.getWriter();
        out.write(json.toString());
        out.flush();
        out.close();
    }

    /*Use To view the SI PAR Viewer Detail*/
    @RequestMapping(value = "SiParViewer.htm", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView LoadSiParViewerPage(ModelMap model, HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub) {
        ModelAndView mv = new ModelAndView("/par/PARViewerSI");
        List fiscyear = fiscalDAO.getFiscalYearList();
        ParAssignPrivilage pap = parAdminDAO.getAssignPrivilage(lub.getLoginspc());
        mv.addObject("fiscyear", fiscyear);
        mv.addObject("parreviewedspc", "");
        mv.addObject("pap", pap);
        return mv;
    }

    /*Use For Get ALL PAR Details in PAR Admin after click on view link*/
    @RequestMapping(value = "viewSiParDetail.htm", params = "Back", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView Back2OffWiseSiPARList(HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("ParAdminProperties") ParAdminProperties parAdminProperties) {

        ModelAndView mav = new ModelAndView("/par/PARViewerSI");
        ParAdminSearchCriteria parAdminSearchCriteria = new ParAdminSearchCriteria();
        List fiscyear = fiscalDAO.getFiscalYearList();
        ParAssignPrivilage pap1 = parAdminDAO.getAssignPrivilage(lub.getLoginspc());
        parAdminSearchCriteria.setPrivilegedSpc(lub.getLoginspc());
        parAdminSearchCriteria.setFiscalyear(parAdminProperties.getFiscalyear());
        List parDataList = parAdminDAO.getOfficeSpecificSiPARList(parAdminSearchCriteria);

        mav.addObject("fiscyear", fiscyear);
        mav.addObject("parreviewedspc", "");
        mav.addObject("pap", pap1);
        mav.addObject("SiParDataList", parDataList);
        return mav;
    }

    @RequestMapping(value = "offWiseSiPARList.htm", params = "Search", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView getOffWiseSiPARList(HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub,
            @ModelAttribute(value = "parreviewedspc") String parreviewedspc, HttpServletResponse response,
            @ModelAttribute("ParAdminSearchCriteria") ParAdminSearchCriteria parAdminSearchCriteria) {

        ModelAndView mav = null;

        try {

            List fiscyear = fiscalDAO.getFiscalYearList();
            String fyVal = parAdminSearchCriteria.getFiscalyear();
            ParAssignPrivilage pap1 = parAdminDAO.getAssignPrivilage(lub.getLoginspc());

            parAdminSearchCriteria.setPrivilegedSpc(lub.getLoginspc());
            List parDataList = parAdminDAO.getOfficeSpecificSiPARList(parAdminSearchCriteria);
            mav = new ModelAndView("/par/PARViewerSI");
            mav.addObject("fiscyear", fiscyear);
            mav.addObject("FyYear", fyVal);
            mav.addObject("parreviewedspc", "");
            mav.addObject("pap", pap1);
            mav.addObject("SiParDataList", parDataList);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "offWiseSiPARList.htm", params = "Download", method = {RequestMethod.GET, RequestMethod.POST})
    public void getOffWiseSiPARListExcel(HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub,
            @ModelAttribute(value = "parreviewedspc") String parreviewedspc, HttpServletResponse response,
            @ModelAttribute("ParAdminSearchCriteria") ParAdminSearchCriteria parAdminSearchCriteria) {

        response.setContentType("application/vnd.ms-excel");
        OutputStream out = null;
        List parDataList = null;

        try {

            parAdminSearchCriteria.setPrivilegedSpc(lub.getLoginspc());
            parDataList = parAdminDAO.getOfficeSpecificSiPARList(parAdminSearchCriteria);

            String fileName = "SiParReport-" + CommonFunctions.getCurDateDMY() + ".xls";
            out = new BufferedOutputStream(response.getOutputStream());

            WritableWorkbook workbook = Workbook.createWorkbook(out);
            WritableSheet sheet = workbook.createSheet("PAR List", 0); // This 0 is for sheet no

            WritableFont headFont = new WritableFont(WritableFont.COURIER, 13, WritableFont.BOLD);
            headFont.setColour(Colour.BLACK);

            WritableCellFormat headFormat = new WritableCellFormat(headFont);
            //headFormat.setBackground(Colour.SKY_BLUE);
            headFormat.setBorder(Border.ALL, BorderLineStyle.THIN);

            WritableCellFormat headcell = new WritableCellFormat(headFormat);
            headcell.setAlignment(Alignment.CENTRE);
            headcell.setVerticalAlignment(VerticalAlignment.CENTRE);
            headcell.setWrap(true);

            // CELL DESIGN FOR ALL VALUES
            WritableFont dataFont = new WritableFont(WritableFont.COURIER, 11, WritableFont.NO_BOLD);
            dataFont.setColour(Colour.BLACK);

            WritableCellFormat dataFormat = new WritableCellFormat(dataFont);
            //dataFormat.setBackground(Colour.GRAY_25);
            dataFormat.setBorder(Border.ALL, BorderLineStyle.THIN);

            WritableCellFormat datacell = new WritableCellFormat(dataFormat);
            datacell.setAlignment(Alignment.LEFT);
            datacell.setVerticalAlignment(VerticalAlignment.CENTRE);
            datacell.setWrap(true);

            int rowFor1st = 0;

            Label label = new Label(0, rowFor1st++, lub.getLoginoffname(), headcell);//column,row
            sheet.addCell(label);
            sheet.mergeCells(0, 0, 12, 0);

            label = new Label(0, rowFor1st++, " Home Department, Government of Odisha. ", headcell);//column,row
            sheet.addCell(label);
            sheet.mergeCells(0, 1, 12, 1);

            label = new Label(0, rowFor1st++, " Performance Appraisal Report (PAR) for SI & Equivalent Ranks (Group - B) ", headcell);//column,row
            sheet.addCell(label);
            sheet.mergeCells(0, 2, 12, 2);

            label = new Label(0, rowFor1st, "Sl No", headcell);
            sheet.addCell(label);

            int widthInChars1 = 15;
            label = new Label(1, rowFor1st, "Financial Year", headcell);
            sheet.setColumnView(1, widthInChars1);
            sheet.addCell(label);

            int widthInChars2 = 25;
            label = new Label(2, rowFor1st, "Employee Id" + "\n" + "GPF / PRAN NO", headcell);
            sheet.setColumnView(2, widthInChars2);
            sheet.addCell(label);

            int widthInChars3 = 40;
            label = new Label(3, rowFor1st, "Employee Name" + "\n" + "Mobile", headcell);
            sheet.setColumnView(3, widthInChars3);
            sheet.addCell(label);

            label = new Label(4, rowFor1st, "Date of Birth", headcell);
            sheet.setColumnView(4, widthInChars1);
            sheet.addCell(label);

            label = new Label(5, rowFor1st, "Post Group", headcell);
            sheet.setColumnView(5, widthInChars1);
            sheet.addCell(label);

            int widthInChars6 = 35;
            label = new Label(6, rowFor1st, "Designation", headcell);
            sheet.setColumnView(6, widthInChars6);
            sheet.addCell(label);

            label = new Label(7, rowFor1st, "Cadre", headcell);
            sheet.setColumnView(7, widthInChars2);
            sheet.addCell(label);

            label = new Label(8, rowFor1st, "Place of Posting", headcell);
            sheet.setColumnView(8, widthInChars3);
            sheet.addCell(label);

            label = new Label(9, rowFor1st, "Status", headcell);
            sheet.setColumnView(9, widthInChars3);
            sheet.mergeCells(9, rowFor1st, 12, rowFor1st);
            sheet.addCell(label);

            int row2nd = rowFor1st + 1;
            label = new Label(0, row2nd, " ", headcell);
            sheet.mergeCells(0, 4, 8, 4);
            sheet.addCell(label);

            label = new Label(9, row2nd, " Period ", headcell);
            sheet.setColumnView(9, widthInChars2);
            sheet.addCell(label);

            label = new Label(10, row2nd, "Status", headcell);
            sheet.setColumnView(10, widthInChars3);
            sheet.addCell(label);

            label = new Label(11, row2nd, " Pending At Authority with Designation ", headcell);
            sheet.setColumnView(11, widthInChars3);
            sheet.addCell(label);

            label = new Label(12, row2nd, " Pending At Authority From ", headcell);
            sheet.setColumnView(12, widthInChars3);
            sheet.addCell(label);

            int row = 5;
            int slNo = 1;
            if (parDataList != null && parDataList.size() > 0) {
                ParAdminProperties parDataBean = null;
                for (Object parDataList1 : parDataList) {

                    parDataBean = (ParAdminProperties) parDataList1;

                    label = new Label(0, row, slNo + "", headcell);
                    sheet.addCell(label);

                    label = new Label(1, row, parDataBean.getFiscalyear(), datacell);
                    sheet.addCell(label);

                    label = new Label(2, row, parDataBean.getEmpId() + "\n" + parDataBean.getGpfno(), datacell);
                    sheet.addCell(label);

                    label = new Label(3, row, parDataBean.getEmpName() + "\n" + parDataBean.getMobile(), datacell);
                    sheet.addCell(label);

                    label = new Label(4, row, parDataBean.getDob(), datacell);
                    sheet.addCell(label);

                    label = new Label(5, row, parDataBean.getGroupName(), datacell);
                    sheet.addCell(label);

                    label = new Label(6, row, parDataBean.getPostName(), datacell);
                    sheet.addCell(label);

                    label = new Label(7, row, parDataBean.getCadreName(), datacell);
                    sheet.addCell(label);

                    label = new Label(8, row, parDataBean.getCurrentOfficeName(), datacell);
                    sheet.addCell(label);

                    //List parInnerDataList = parAdminDAO.getSiParDetails(parDataBean.getEmpId(), parDataBean.getFiscalyear());
                    List parInnerDataList = parDataBean.getSiParInnerDataList();
                    if (parInnerDataList != null && parInnerDataList.size() > 0) {
                        ParAdminProperties pbf = null;
                        for (Object parInnerList : parInnerDataList) {

                            pbf = (ParAdminProperties) parInnerList;

                            label = new Label(9, row, pbf.getPrdFrmDate() + " - to -" + pbf.getPrdToDate(), datacell);
                            sheet.addCell(label);

                            label = new Label(10, row, pbf.getParstatus(), datacell);
                            sheet.setColumnView(10, widthInChars3);
                            sheet.addCell(label);

                            String pendingAuthName = "";
                            if (pbf.getPendingAtAuthName() != null && !pbf.getPendingAtAuthName().equals("")) {
                                pendingAuthName = pbf.getPendingAtAuthName() + ", " + pbf.getPendingAtSpc();
                            }
                            if (pbf.getNrcDetails() != null && !pbf.getNrcDetails().equals("")) {
                                pendingAuthName = pbf.getNrcDetails();
                            }
                            int widthInChars5 = 50;
                            label = new Label(11, row, pendingAuthName, datacell);
                            sheet.setColumnView(11, widthInChars5);
                            sheet.addCell(label);

                            label = new Label(12, row, pbf.getParPendingDateFrom(), datacell);
                            sheet.addCell(label);

                            row++;
                        }
                    }
                    slNo++;
                }
            }
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            workbook.write();
            workbook.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @RequestMapping(value = "parPDFAdverseAppraiseCommunicationSiPAR.htm")
    public ModelAndView parPDFAdverseAppraiseCommunication(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub,
            @ModelAttribute("parAdverseCommunicationDetail") ParAdverseCommunicationDetail parAdverseCommunicationDetail, @RequestParam Map<String, String> requestParams) {

        ModelAndView mv = new ModelAndView();
        String tempParid = CommonFunctions.decodedTxt(parAdverseCommunicationDetail.getEncParId());
        parAdverseCommunicationDetail.setParId(Integer.parseInt(tempParid));

        ParApplyForm paf = parAdminDAO.getParBriefDetail(parAdverseCommunicationDetail.getParId());
        parAdverseCommunicationDetail = parAdminDAO.getAdverseRemarksDetailAtCustodianSIPAR(parAdverseCommunicationDetail.getParId());
        /*if (paf.getIsadversed() == null || paf.getIsadversed().equals("N")) {
         parAdminDAO.markParAsAdverse(parAdverseCommunicationDetail.getParId());
         } */
        parAdminDAO.getAppraiseForAdverseSiPAR(parAdverseCommunicationDetail);
        List custodianadverseremarkList = parAdminDAO.getcustodianremarksAdverseSiPAR(parAdverseCommunicationDetail.getParId());
        List custodianEntryList = parAdminDAO.getAdverseRemarksDetailListAtCustodianSIPAR(parAdverseCommunicationDetail.getParId());
        if (paf.getAdverseCommunicationStatusId() == 122) {
            ArrayList reportingdata = parAdminDAO.getParAuthority("REPORTING", parAdverseCommunicationDetail.getParId(), parAdverseCommunicationDetail.getTaskId());
            ArrayList reviewingdata = parAdminDAO.getParAuthority("REVIEWING", parAdverseCommunicationDetail.getParId(), parAdverseCommunicationDetail.getTaskId());
            ArrayList acceptingdata = parAdminDAO.getParAuthority("ACCEPTING", parAdverseCommunicationDetail.getParId(), parAdverseCommunicationDetail.getTaskId());
            mv.addObject("reporting", reportingdata);
            mv.addObject("reviewing", reviewingdata);
            mv.addObject("accepting", acceptingdata);
        }
        parAdverseCommunicationDetail.setAdverseCommunicationStatusId(paf.getAdverseCommunicationStatusId());
        mv.addObject("parBriefDetail", paf);
        mv.addObject("parAdverseCommunicationDetail", parAdverseCommunicationDetail);
        mv.addObject("custodianadverseremarkList", custodianadverseremarkList);
        mv.addObject("custodianEntryList", custodianEntryList);
        mv.setViewName("/par/ParAdverseAppraiseeRemarkSIPAR");
        return mv;
    }

    @RequestMapping(value = "parPDFAdverseAppraiseCommunicationSiPAR.htm", params = {"action=Submit"}, method = RequestMethod.POST)
    public ModelAndView saveparparPDFAdverseAppraiseCommunication(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("parAdverseCommunicationDetail") ParAdverseCommunicationDetail parAdverseCommunicationDetail, @ModelAttribute("parApplyForm") ParApplyForm parApplyForm) {
        ModelAndView mav = new ModelAndView();
        parAdverseCommunicationDetail.setFromempId(lub.getLoginempid());
        //parAdverseCommunicationDetail.setFromspc(lub.getLoginspc());
        parAdverseCommunicationDetail.setFromspc(lub.getLoginoffcode());
        if (parAdverseCommunicationDetail.getAdverseCommunicationStatusId() >= 122) {
            parAdverseCommunicationDetail.setFromAuthType("Custodian");
            parAdverseCommunicationDetail.setToAuthType("Authority");
            parAdminDAO.savecustodianremarksAdversesiPAR(parAdverseCommunicationDetail);
        } else if (parAdverseCommunicationDetail.getAdverseCommunicationStatusId() == 0) {
            parAdverseCommunicationDetail.setFromAuthType("Custodian");
            parAdverseCommunicationDetail.setToAuthType("Apprisee");
            parAdminDAO.savecustodianremarksAdversePARToAppraiseeSiPAR(parAdverseCommunicationDetail);
        }
        List custodianadverseremarkList = parAdminDAO.getcustodianremarksAdverseSiPAR(parAdverseCommunicationDetail.getParId());
        mav.addObject("custodianadverseremarkList", custodianadverseremarkList);
        mav.setViewName("/par/ParAdverseAppraiseeRemarkSIPAR");
        return mav;
    }

    @RequestMapping(value = "parPDFAdverseAppraiseCommunicationSiPAR.htm", params = {"action=Enter Adverse Remarks Detail"}, method = RequestMethod.POST)
    public ModelAndView enterdverseAppraiseCommunicationDetail(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("parAdverseCommunicationDetail") ParAdverseCommunicationDetail parAdverseCommunicationDetail,
            @ModelAttribute("ParAdminSearchCriteria") ParAdminSearchCriteria parAdminBean) {
        ModelAndView mav = new ModelAndView();
        parAdverseCommunicationDetail.setFromempId(lub.getLoginempid());
        List fiscyear = fiscalDAO.getFiscalYearList();
        String fyVal = parAdminBean.getFiscalyear();
        parAdverseCommunicationDetail.setCustodianOffice(lub.getLoginoffcode());
        parAdminDAO.getAppraiseDetailForAdverseAtCustodianSiPAR(parAdverseCommunicationDetail);
        mav.addObject("parAdverseCommunicationDetail", parAdverseCommunicationDetail);
        mav.addObject("FiscYear", fiscyear);
        mav.addObject("FyYear", fyVal);
        mav.setViewName("/par/ParAdverseAppraiseeDetailEntryForSIPAR");
        return mav;
    }

    @RequestMapping(value = "saveAdverseRemarksDetailForAppraise", method = {RequestMethod.POST}, params = {"action=Save"})
    public ModelAndView saveDisccharge(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("parAdverseCommunicationDetail") ParAdverseCommunicationDetail parAdverseCommunicationDetail) {
        ModelAndView mav = new ModelAndView();
        try {

            parAdverseCommunicationDetail.setCustodianLoginId(lub.getLoginempid());
            String tempParid = CommonFunctions.encodedTxt(parAdverseCommunicationDetail.getParId() + "");
            parAdminDAO.saveAdverseRemarksDetailAtCustodianSIPAR(parAdverseCommunicationDetail);
            mav.setViewName("redirect:parPDFAdverseAppraiseCommunicationSiPAR.htm?encParId=" + tempParid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "saveAdverseRemarksDetailForAppraise.htm", params = {"action=Cancel"}, method = RequestMethod.POST)
    public ModelAndView cancelAdverseAppraiseCommunicationDetail(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("parAdverseCommunicationDetail") ParAdverseCommunicationDetail parAdverseCommunicationDetail, @ModelAttribute("parApplyForm") ParApplyForm parApplyForm) {
        ModelAndView mav = new ModelAndView();
        try {
            String tempParid = CommonFunctions.encodedTxt(parAdverseCommunicationDetail.getParId() + "");
            mav.setViewName("redirect:parPDFAdverseAppraiseCommunicationSiPAR.htm?encParId=" + tempParid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "editAdverseRemarksDetailEntryForAppraise.htm", method = {RequestMethod.GET})
    public ModelAndView editAdverseRemarksDetailEntryForAppraise(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("parAdverseCommunicationDetail") ParAdverseCommunicationDetail parAdverseCommunicationDetail,
            @ModelAttribute("ParAdminSearchCriteria") ParAdminSearchCriteria parAdminBean) {
        ModelAndView mav = new ModelAndView();
        List fiscyear = fiscalDAO.getFiscalYearList();
        String fyVal = parAdminBean.getFiscalyear();
        String tempParId = parAdverseCommunicationDetail.getEncParId();
        tempParId = CommonFunctions.decodedTxt(tempParId);
        parAdverseCommunicationDetail = parAdminDAO.getAdverseRemarksDetailAtCustodianSIPAR(Integer.parseInt(tempParId));
        parAdminDAO.getAppraiseDetailForAdverseAtCustodianSiPAR(parAdverseCommunicationDetail);
        mav.addObject("FiscYear", fiscyear);
        mav.addObject("FyYear", fyVal);
        mav.addObject("parAdverseCommunicationDetail", parAdverseCommunicationDetail);
        mav.setViewName("/par/ParAdverseAppraiseeDetailEntryForSIPAR");
        //parAdminDAO.saveAdverseRemarksDetailAtCustodianSIPAR(parAdverseCommunicationDetail);
        return mav;
    }

    /*Use To Submit Remarks For Adverse Communication By appraisee*/
    @RequestMapping(value = "parAdverseCommunicationReplySiPAR.htm", params = {"action=Submit"}, method = RequestMethod.POST)
    public ModelAndView parAdverseCommunicationReplySiPAR(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("parAdverseCommunicationDetail") ParAdverseCommunicationDetail parAdverseCommunicationDetail, @ModelAttribute("parApplyForm") ParApplyForm parApplyForm) {
        ModelAndView mav = new ModelAndView();
        parAdverseCommunicationDetail.setFromempId(lub.getLoginempid());
        parAdverseCommunicationDetail.setFromspc(lub.getLoginspc());
        if (parAdverseCommunicationDetail.getAdverseCommunicationStatusId() == 121) {
            parAdverseCommunicationDetail.setFromAuthType("Apprisee");
            parAdverseCommunicationDetail.setToAuthType("Custodian");
            parAdverseCommunicationDetail.setAdverseCommunicationStatusId(122);
            parAdminDAO.saveparAdverseCommunicationReplySiPAR(parAdverseCommunicationDetail);
        } else if (parAdverseCommunicationDetail.getAdverseCommunicationStatusId() == 123) {
            parAdverseCommunicationDetail.setFromAuthType("Authority");
            parAdverseCommunicationDetail.setToAuthType("Custodian");
            parAdverseCommunicationDetail.setAdverseCommunicationStatusId(124);
            parAdminDAO.saveparAdverseCommunicationReplySiPAR(parAdverseCommunicationDetail);
        }
        TaskListHelperBean taskDetails = taskDAO.getTaskDetails(parAdverseCommunicationDetail.getTaskId());
        mav.addObject("taskListHelperBean", taskDetails);
        mav.setViewName("/par/ParAdverseAppraiseeRemarkSubmittedSiPAR");
        return mav;
    }

    @RequestMapping(value = "pardverseRemarkReplySiPAR.htm")
    public ModelAndView pardverseRemarkReplySiPAR(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("parAdverseCommunicationDetail") ParAdverseCommunicationDetail parAdverseCommunicationDetail) {
        ModelAndView mv = new ModelAndView();
        //parAdminDAO.markParAsAdverse(parAdverseCommunicationDetail.getParId());
        parAdminDAO.getAppraiseForAdversePAR(parAdverseCommunicationDetail);
        List custodianadverseremarkList = parAdminDAO.getcustodianremarksAdversePAR(parAdverseCommunicationDetail.getParId());

        mv.addObject("custodianadverseremarkList", custodianadverseremarkList);
        mv.setViewName("/par/ParAdverseRemarkReplySiPAR");
        return mv;
    }

    @RequestMapping(value = "parAdverseremarkSiPAR.htm")
    public ModelAndView parAdverseremarkSiPAR(@ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("taskId") int taskId, @ModelAttribute("parAdverseCommunicationDetail") ParAdverseCommunicationDetail parAdverseCommunicationDetail) {
        ModelAndView mv = new ModelAndView();
        TaskListHelperBean taskDetails = taskDAO.getTaskDetails(taskId);
        mv.addObject("taskListHelperBean", taskDetails);
        if (taskDetails.getStatusId() == 121 || taskDetails.getStatusId() == 123) {
            parAdverseCommunicationDetail.setToempId(taskDetails.getAppEmpCode());
            parAdverseCommunicationDetail.setTospc(taskDetails.getAppSpc());
            parAdverseCommunicationDetail.setAdverseCommunicationStatusId(taskDetails.getStatusId());
            ParAdverseCommunicationDetail communicationDetail = parAdminDAO.getCommunicationDetailsSiPAR(taskDetails.getReferenceId());
            parAdverseCommunicationDetail.setParId(communicationDetail.getParId());
            mv.addObject("communicationDetail", communicationDetail);
            mv.setViewName("/par/ParAdverseRemarkReplySiPAR");

        }
        return mv;
    }

    /*Employee list for Not initiated PAR*/
    @RequestMapping(value = "SiPARNotInitiatedOfficerList.htm", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView SiPARNotInitiatedOfficerList(ModelMap model, HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub,
            @ModelAttribute("Privileges") Module[] privileges, @ModelAttribute("ParAdminSearchCriteria") ParAdminSearchCriteria parAdminBean) {

        ModelAndView mv = new ModelAndView();
        if (lub.getLoginusertype().equals("B")) {
            if (CommonFunctions.hasPrivileage(privileges, "viewSiParCustodian.htm") == true) {
                // mv.setViewName("/par/SiParNotInitiatedOfficerList");
                List fiscyear = fiscalDAO.getFiscalYearList();
                String fyVal = parAdminBean.getFiscalyear();
                mv.setViewName("/par/SiParNotInitiatedOfficerList");
                mv.addObject("FiscYear", fiscyear);
                mv.addObject("FyYear", fyVal);
            } else {
                mv.setViewName("/under_const");
            }
        }
        return mv;
    }

    @RequestMapping(value = "SiPARNotInitiatedOfficerList.htm", params = {"action=Search"}, method = RequestMethod.POST)
    public ModelAndView searchSiPARNotInitiatedOfficerList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("Privileges") Module[] privileges,
            @ModelAttribute("ParAdminSearchCriteria") ParAdminSearchCriteria parAdminBean) {
        ModelAndView mav = new ModelAndView();
        if (CommonFunctions.hasPrivileage(privileges, "viewSiParCustodian.htm") == true) {
            List fiscyear = fiscalDAO.getFiscalYearList();
            String fyVal = parAdminBean.getFiscalyear();
            List employeeListNotInitiated = parAdminDAO.getSiPARNotInitiatedOfficerList(lub.getLoginoffcode(), parAdminBean.getFiscalyear());
            mav.setViewName("/par/SiParNotInitiatedOfficerList");
            mav.addObject("FiscYear", fiscyear);
            mav.addObject("FyYear", fyVal);
            mav.addObject("employeeListNotInitiated", employeeListNotInitiated);
        } else {
            mav.setViewName("/under_const");
        }
        return mav;
    }

    @RequestMapping(value = "siPARNotInitiatedListExcel.htm", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView getsiPARNotInitiatedListExcel(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("ParAdminSearchCriteria") ParAdminSearchCriteria parAdminSearchCriteria, @ModelAttribute("parApplyForm") ParApplyForm parApplyForm) {
        ModelAndView mv = new ModelAndView();
        List employeeListNotInitiated = parAdminDAO.getSiPARNotInitiatedOfficerList(lub.getLoginoffcode(), parAdminSearchCriteria.getFiscalyear());
        mv.addObject("employeeListNotInitiated", employeeListNotInitiated);
        mv.setViewName("siPARNotInitiatedExcel");
        return mv;
    }

    /*Get NRC DEtail In Custodian Login SI PAR*/
    @RequestMapping(value = "getNRCdetailSiPAR.htm", method = RequestMethod.GET)
    public ModelAndView getNRCdetailSiPAR(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub,
            @RequestParam Map<String, String> requestParams, @ModelAttribute("parApplyForm") ParApplyForm parApplyForm) {
        ModelAndView mv = new ModelAndView();
        ParApplyForm paf = parAdminDAO.getNRCDetails(parApplyForm.getParId());
        mv.addObject("parApplyForm", paf);
        mv.addObject("Loginempid", lub.getLoginempid());
        mv.setViewName("/par/PARRequestNRC");

        return mv;
    }

}
