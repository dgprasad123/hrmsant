package hrms.controller.loansanction;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import hrms.common.CommonFunctions;
import hrms.common.Numtowordconvertion;
import hrms.dao.leaveapply.LeaveApplyDAOImpl;
import hrms.dao.loansanction.LoanSancDAO;
import hrms.dao.master.BankDAOImpl;
import hrms.dao.master.BranchDAO;
import hrms.dao.master.DepartmentDAO;
import hrms.dao.master.DistrictDAO;
import hrms.dao.master.LoanTypeDAO;
import hrms.dao.master.OfficeDAO;
import hrms.dao.master.SubStantivePostDAO;
import hrms.dao.master.TreasuryDAO;
import hrms.dao.notification.NotificationDAOImpl;
import hrms.dao.servicebook.SbCorrectionRequestDAO;
import hrms.model.common.FileAttribute;
import hrms.model.loan.EmployeeLoan;
import hrms.model.loan.GroupLoanForm;
import hrms.model.loan.Loan;
import hrms.model.login.LoginUserBean;
import hrms.model.login.Users;
import hrms.model.master.Office;
import hrms.model.notification.NotificationBean;
import hrms.model.servicebook.SbCorrectionRequestModel;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;
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
import org.springframework.web.servlet.ModelAndView;

@Controller
@SessionAttributes({"LoginUserBean", "SelectedEmpObj"})
public class LoanSanctionController {

    @Autowired
    public LoanSancDAO loanSancDAO;
    @Autowired
    NotificationDAOImpl notificationDao;
    @Autowired
    public LeaveApplyDAOImpl leaveapplyDAO;
    @Autowired
    public BankDAOImpl bankDAO;
    @Autowired
    public TreasuryDAO treasuryDao;
    @Autowired
    public LoanTypeDAO loanTypeDAO;

    @Autowired
    public DepartmentDAO deptDAO;

    @Autowired
    public DistrictDAO districtDAO;

    @Autowired
    public OfficeDAO offDAO;

    @Autowired
    BranchDAO branchDao;

    @Autowired
    SbCorrectionRequestDAO sbCorReqDao;

    @Autowired
    public SubStantivePostDAO substantivePostDAO;

    @Autowired
    private ServletContext servletContext;

    public static String getServerDoe() {
        String currDate;
        Format formatter;
        formatter = new SimpleDateFormat("dd-MMM-yyyy");
        currDate = formatter.format(new Date());
        return currDate;
    }

    @ResponseBody
    @RequestMapping(value = "loansanctionlist.htm", method = {RequestMethod.GET, RequestMethod.POST})
    public void viewLoanList(@ModelAttribute("SelectedEmpObj") Users lub, @ModelAttribute("loanForm") Loan loanForm, BindingResult result, Map<String, Object> model, HttpServletResponse response) {
        response.setContentType("application/json");
        JSONObject json = new JSONObject();
        PrintWriter out = null;
        int transferlistCnt = 0;

        try {
            List loanSancList = loanSancDAO.getLoanSancList(loanForm.getEmpid());
            json.put("total", 50);
            json.put("rows", loanSancList);
            out = response.getWriter();
            out.write(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    @RequestMapping(value = "loansanction.htm", method = RequestMethod.GET)
    public ModelAndView LoanSanctionList(@ModelAttribute("LoginUserBean") LoginUserBean lubloginuser, @ModelAttribute("SelectedEmpObj") Users lub, @ModelAttribute("loanForm") Loan loanForm, Map<String, Object> model, @RequestParam Map<String, String> requestParams) {
        ModelAndView mv = null;
        loanForm.setEmpid(lub.getEmpId());
        List loanSancList = loanSancDAO.getLoanSancList(loanForm.getEmpid());
        String errorMsg = requestParams.get("msg");
        mv = new ModelAndView("/loansanction/LoanSanctionList", "command", loanForm);
        mv.addObject("loanSancList", loanSancList);
        mv.addObject("errorMsg", errorMsg);
        mv.addObject("aginterestempcount", loanSancDAO.getAGInterestDetailedReportEmployeeCount(lubloginuser.getLoginoffcode()));
        //mv.setViewName("/loansanction/LoanSanctionList");
        return mv;
    }

    @RequestMapping(value = "saveloanSanction.htm", method = RequestMethod.POST, params = "action=Save Loan")
    public ModelAndView saveloanSanction(@ModelAttribute("SelectedEmpObj") Users lub, ModelMap model, @ModelAttribute("loanForm") Loan loanForm, BindingResult result, HttpServletResponse response) throws IOException {
        int notId;

        String errorMsg = "";
        NotificationBean nb = new NotificationBean();

        if (loanForm.getNotid() > 0) {
            nb.setNotid(loanForm.getNotid());
        }
        nb.setNottype("LOAN_SANC");
        nb.setEmpId(loanForm.getEmpid());
        nb.setDateofEntry(new Date());
        nb.setOrdno(loanForm.getOrderno());
        nb.setOrdDate(CommonFunctions.getDateFromString(loanForm.getOrderdate(), "dd-MMM-yyyy"));
        nb.setNote(loanForm.getNote());
        //nb.setSancDeptCode(loanForm.getHidSancDistCode());
        nb.setSancDeptCode(loanForm.getHidSancDeptCode());
        nb.setSancOffCode(loanForm.getHidSancOffCode());
        nb.setSancAuthCode(loanForm.getSancSpc());
        if (loanForm.getChkNotSBPrint() != null && loanForm.getChkNotSBPrint().equals("Y")) {
            nb.setIfVisible("N");
        }
        //System.out.println("Loan ID:"+loanForm.getLoanid());
        if (loanForm.getLoanid() != null && !loanForm.getLoanid().equals("")) {
            String filepath = servletContext.getInitParameter("PhotoPath");
            if (loanForm.getNotid() == 0) {
                notId = notificationDao.insertNotificationData(nb);
                loanForm.setNotid(notId);
                loanSancDAO.updateLoanDetail(loanForm, filepath);
                //System.out.println("Note ID:"+loanForm.getNotid());
            } else {
                nb.setNotid(loanForm.getNotid());
                loanSancDAO.updateLoanDetail(loanForm, filepath);
                notificationDao.modifyNotificationData(nb);
            }
        } else {
            //System.out.println("Else");
            notId = notificationDao.insertNotificationData(nb);
            //notId = 0;
            //System.out.println("NotID:"+notId);
            if (notId > 0) {
                loanSancDAO.saveLoanDetail(loanForm, notId);
            } else {
                errorMsg = "Error Occured! Please try after sometime.";
            }
        }
        List loanSancList = loanSancDAO.getLoanSancList(loanForm.getEmpid());
        ModelAndView mv = new ModelAndView("redirect:/loansanction.htm?msg=" + errorMsg);
        mv.addObject("loanSancList", loanSancList);

        return mv;
    }

    @RequestMapping(value = "saveloanSanction.htm", method = RequestMethod.POST, params = "action=Back")
    public ModelAndView backToEmpLoanList() {
        ModelAndView mav = new ModelAndView("redirect:/loansanction.htm");
        return mav;
    }

    @RequestMapping(value = "newloanData.htm", method = RequestMethod.POST)
    public ModelAndView newLoanData(@ModelAttribute("SelectedEmpObj") Users lub, @ModelAttribute("loanForm") Loan loanForm, Map<String, Object> model) {
        loanForm.setNowDeduct("P");
        ModelAndView mv = new ModelAndView("/loansanction/NewLoanSanction", "command", loanForm);
        List bankList = bankDAO.getBankList();
        List treasuryList = treasuryDao.getTreasuryList();
        List loanTypeList = loanTypeDAO.getLoanTypeList();
        List deptlist = deptDAO.getDepartmentList();
        List distlist = districtDAO.getDistrictList();
        mv.addObject("bankList", bankList);
        mv.addObject("treasuryList", treasuryList);
        mv.addObject("loanTypeList", loanTypeList);
        mv.addObject("deptlist", deptlist);
        mv.addObject("distlist", distlist);
        return mv;
    }

    @RequestMapping(value = "newloanInterestData.htm")
    public ModelAndView newloanInterestData(@ModelAttribute("SelectedEmpObj") Users lub, @ModelAttribute("loanForm") Loan loanForm, Map<String, Object> model) {

        if (loanForm.getAgcalculationid() != null && !loanForm.getAgcalculationid().equals("")) {
            loanForm.setAgAdrefid(loanForm.getPrinloanid());
            loanSancDAO.editAGInterestLoanData(lub.getEmpId(), loanForm.getAgAdrefid(), loanForm);
        }

        loanForm.setEmpid(lub.getEmpId());
        loanForm.setNowDeduct("I");
        loanForm.setSltloan(loanSancDAO.getLoanTP(loanForm.getPrinloanid()));

        ModelAndView mv = new ModelAndView("/loansanction/NewLoanSanction", "command", loanForm);
        List bankList = bankDAO.getBankList();
        List treasuryList = treasuryDao.getTreasuryList();
        List loanTypeList = loanTypeDAO.getLoanTypeList();
        mv.addObject("bankList", bankList);
        mv.addObject("treasuryList", treasuryList);
        mv.addObject("loanTypeList", loanTypeList);
        return mv;
    }

    @RequestMapping(value = "editloanData.htm", method = RequestMethod.GET)
    public ModelAndView editloanData(@ModelAttribute("LoginUserBean") LoginUserBean lubloginuser, @ModelAttribute("SelectedEmpObj") Users lub, @ModelAttribute("loanForm") Loan loanForm, Map<String, Object> model) {
        loanForm = loanSancDAO.editLoanData(loanForm.getLoanid());
        List bankList = bankDAO.getBankList();
        List treasuryList = treasuryDao.getTreasuryList();
        List loanTypeList = loanTypeDAO.getLoanTypeList();
        List deptlist = deptDAO.getDepartmentList();
        List distlist = districtDAO.getDistrictList();
        String userType = lubloginuser.getLoginusertype();
        String allowUpdate = "";
        if (userType != null && !userType.equals("") && (userType.equals("A") || userType.equals("D"))) {
            allowUpdate = "Y";
        }
        ModelAndView mv = new ModelAndView("/loansanction/NewLoanSanction", "command", loanForm);

        //List gpcauthlist = substantivePostDAO.getAuthorityGenericPostList(trform.getHidAuthOffCode());
        List gpcpostedList = substantivePostDAO.getGenericPostList(loanForm.getHidSancOffCode());

        List spcList = substantivePostDAO.getGPCWiseSectionWiseSPCList(loanForm.getHidSancOffCode(),loanForm.getHidSancGenericPost());

        //List gpcpostedList = substantivePostDAO.getGenericPostList(loanForm.getHidSancOffCode());
        List offList = offDAO.getTotalOfficeList(loanForm.getHidSancDeptCode(), loanForm.getHidSancDistCode());
        //List spcList=substantivePostDAO.getGPCWiseSPCList(loanForm.getHidSancOffCode(), userType)
        mv.addObject("Monthwiseinstl", loanSancDAO.getMonthWiseInstlment(lub.getEmpId(), loanForm.getLoanid()));
        mv.addObject("bankList", bankList);
        mv.addObject("treasuryList", treasuryList);
        mv.addObject("loanTypeList", loanTypeList);
        mv.addObject("AllowUpdate", allowUpdate);
        mv.addObject("deptlist", deptlist);
        mv.addObject("distlist", distlist);
        mv.addObject("gpcpostedList", gpcpostedList);
        mv.addObject("offList", offList);
        mv.addObject("spcList", spcList);
        return mv;
    }

    @RequestMapping(value = "viewloanData.htm")
    public ModelAndView viewloanData(@ModelAttribute("LoginUserBean") LoginUserBean lubloginuser, @ModelAttribute("SelectedEmpObj") Users lub, @ModelAttribute("loanForm") Loan loanForm, Map<String, Object> model) {

        ModelAndView mv = new ModelAndView();

        try {
            loanForm = loanSancDAO.editLoanData(loanForm.getLoanid());

            mv.addObject("loanForm", loanForm);

            mv.addObject("loanname", loanSancDAO.getLoanName(loanForm.getSltloan()));

            mv.addObject("bankname", bankDAO.getBankName(loanForm.getBank()));
            mv.addObject("branchname", branchDao.getBranch(loanForm.getBranch()));

            mv.addObject("treasuryname", treasuryDao.getTreasuryName(loanForm.getTreasuryname()));

            String notideptname = deptDAO.getDeptName(loanForm.getHidAuthDeptCode());
            mv.addObject("sancdeptname", notideptname);

            Office office = offDAO.getOfficeDetails(loanForm.getHidAuthOffCode());
            mv.addObject("sancoffice", office.getOffEn());

            mv.addObject("Monthwiseinstl", loanSancDAO.getMonthWiseInstlment(lub.getEmpId(), loanForm.getLoanid()));

            mv.setViewName("/loansanction/ViewLoanSanction");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mv;
    }

    @RequestMapping(value = "editLoanSanc.htm", method = {RequestMethod.GET, RequestMethod.POST})
    public void editloanData(@ModelAttribute("loanForm") Loan lfb, Map<String, Object> model, @RequestParam("loanId") String loanId, @RequestParam("notId") int notid, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = null;
        try {
            lfb = loanSancDAO.editLoanData(loanId);
            lfb.setLoanid(loanId);
            lfb.setNotid(notid);
            JSONObject job = new JSONObject(lfb);
            out = response.getWriter();
            out.write(job.toString());
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // response.sendRedirect("loansanction.htm");
        //return "/loansanction/LoanSanctionList";
    }
    /*
     @RequestMapping(value = "deleteLoan.htm", method = {RequestMethod.GET, RequestMethod.POST})
     public void deleteLoanData(@ModelAttribute("loanForm") Loan lfb, Map<String, Object> model, @RequestParam("loanId") String loanId, @RequestParam("notId") String notid, HttpServletResponse response) throws IOException {
     response.setContentType("application/json");
     PrintWriter out = null;
     try {
     loanSancDAO.removeLoanData(loanId, notid);
     JSONObject job = new JSONObject(lfb);
     out = response.getWriter();
     out.write(job.toString());
     out.close();
     } catch (Exception e) {
     e.printStackTrace();
     }

     }*/

    @RequestMapping(value = "employeeloanaccount.htm", method = RequestMethod.GET)
    public ModelAndView EmpLoanAccount(@ModelAttribute("LoginUserBean") LoginUserBean lub, ModelMap model, @ModelAttribute("loanacount") Loan loan) {
        loan.setEmpName(lub.getLoginname());
        loan.setGpfNo(lub.getLogingpfno());
        ModelAndView mv = new ModelAndView("/loansanction/EmployeeLoanAccount", "loanacount", loan);
        mv.addObject("loanTypeList", loanTypeDAO.getLoanTypeList());
        return mv;
    }

    @RequestMapping(value = "loanaccount.htm", method = RequestMethod.POST)
    public ModelAndView getLoanEmpWise(@ModelAttribute("LoginUserBean") LoginUserBean lub, ModelMap model, @ModelAttribute("loanacount") Loan loan) {
        loan.setEmpName(lub.getLoginname());
        loan.setGpfNo(lub.getLogingpfno());
        List loandetails = loanSancDAO.getLoanDeductionDeailEmpWise(lub.getLoginempid(), loan.getSltloan());
        ModelAndView mv = new ModelAndView("/loansanction/EmployeeLoanAccount", "loanacount", loan);
        mv.addObject("loanTypeList", loanTypeDAO.getLoanTypeList());
        mv.addObject("loanName", loanTypeDAO.getLoanTypeDetails(loan.getSltloan()));
        mv.addObject("empLoanDetail", loandetails);
        return mv;
    }

    @RequestMapping(value = "EmployeeWiseLoanList.htm", method = RequestMethod.GET)
    public ModelAndView EmployeeWiseLoanList(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpObj") Users selectedEmpObj, HttpServletResponse response) throws IOException {
        ModelAndView mv = new ModelAndView();
        //  ModelAndView mv = null;
        List loanSancList = loanSancDAO.EmployeeWiseLoanList(selectedEmpObj.getEmpId());
        String path = "/loansanction/EmployeeWiseLoanList";
        mv.setViewName(path);
        mv.addObject("loanSancList", loanSancList);
        return mv;
    }

    @RequestMapping(value = "GroupLoan.htm", method = RequestMethod.GET)
    public ModelAndView GroupLoan(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("groupLoanForm") GroupLoanForm gForm, HttpServletResponse response, @RequestParam Map<String, String> requestParams) throws IOException {
        if (requestParams.get("loan") != null && requestParams.get("billname") != null) {
            gForm.setSltLoanlist(requestParams.get("loan"));
            gForm.setSltbillname(requestParams.get("billname"));
        }
        gForm.setBillnamelist(loanSancDAO.getSectionList(lub.getLoginoffcode()));
        gForm.setLoanlist(loanSancDAO.getLoanList());
        ArrayList empList = new ArrayList();
        ModelAndView mv = new ModelAndView("/loansanction/GroupLoan", "groupLoanForm", gForm);
        if (gForm.getSltbillname() != null && !gForm.getSltbillname().equals("")) {
            empList = loanSancDAO.getEmpList(lub.getLoginoffcode(), gForm.getSltbillname(), gForm.getSltLoanlist());
        }
        mv.addObject("sectionList", loanSancDAO.getSectionList(lub.getLoginoffcode()));
        mv.addObject("loanList", loanSancDAO.getLoanList());
        mv.addObject("empList", empList);
        return mv;
    }

    @RequestMapping(value = "GroupLoan.htm", params = "action=Search")
    public ModelAndView showGroupLoanList(ModelMap model, @ModelAttribute("groupLoanForm") GroupLoanForm gForm, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response
    ) {
        gForm.setBillnamelist(loanSancDAO.getSectionList(lub.getLoginoffcode()));
        gForm.setLoanlist(loanSancDAO.getLoanList());
        ArrayList empList = new ArrayList();

        ModelAndView mv = new ModelAndView("/loansanction/GroupLoan", "groupLoanForm", gForm);
        if (gForm.getSltbillname() != null && !gForm.getSltbillname().equals("")) {
            empList = loanSancDAO.getEmpList(lub.getLoginoffcode(), gForm.getSltbillname(), gForm.getSltLoanlist());
        }
        mv.addObject("sectionList", loanSancDAO.getSectionList(lub.getLoginoffcode()));
        mv.addObject("loanList", loanSancDAO.getLoanList());
        mv.addObject("empList", empList);
        return mv;
    }

    @RequestMapping(value = "SaveNewGroupLoan")
    public void SaveNewGroupLoan(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response, @RequestParam("data") String postData
    ) {
        String[] insertfirstpart;
        String[] insertsecondpart;
        String hidselected = postData;
        String empId = null;
        String rowData = null;
        if (hidselected != null && !hidselected.equals("")) {
            insertfirstpart = hidselected.split("@@");
            for (int i = 0; i < insertfirstpart.length; i++) {
                insertsecondpart = insertfirstpart[i].split("\\|");
                empId = insertsecondpart[2];

                NotificationBean nb = new NotificationBean();
                nb.setNottype("LOAN_SANC");
                nb.setEmpId(empId);
                nb.setDateofEntry(new Date());
                int notId = notificationDao.insertNotificationData(nb);
                rowData = insertfirstpart[i] + "|" + notId;

                loanSancDAO.saveEmpData(rowData);
            }
        }
    }

    @ResponseBody
    @RequestMapping(value = "StartStopLoan.htm")
    public void StartStopLoan(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response, @RequestParam("loanid") String loanId, @RequestParam("status") String status
    ) {

        loanSancDAO.startStopLoandata(loanId, status);
    }

    @RequestMapping(value = "editAGInterestLoanData.htm")
    public ModelAndView editAGInterestloanData(@ModelAttribute("LoginUserBean") LoginUserBean lubloginuser, @ModelAttribute("SelectedEmpObj") Users lub, @ModelAttribute("loanForm") Loan loanForm, Map<String, Object> model
    ) {

        loanForm = loanSancDAO.editAGInterestLoanData(lub.getEmpId(), loanForm.getAgAdrefid(), loanForm);

        List bankList = bankDAO.getBankList();
        List treasuryList = treasuryDao.getTreasuryList();
        List loanTypeList = loanTypeDAO.getLoanTypeList();

        String userType = lubloginuser.getLoginusertype();

        String allowUpdate = "";
        if (userType != null && !userType.equals("") && (userType.equals("A") || userType.equals("D"))) {
            allowUpdate = "Y";
        }

        ModelAndView mv = new ModelAndView("/loansanction/NewLoanSanction", "command", loanForm);
        //mv.addObject("Monthwiseinstl", loanSancDAO.getMonthWiseInstlment(lub.getEmpId(), loanForm.getLoanid()));
        mv.addObject("bankList", bankList);
        mv.addObject("treasuryList", treasuryList);
        mv.addObject("loanTypeList", loanTypeList);
        mv.addObject("AllowUpdate", allowUpdate);
        return mv;
    }

    @RequestMapping(value = "AGInterestIntimationPDF")
    public void AGInterestIntimationPDF(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @RequestParam("agAdrefid") String agAdrefid
    ) {

        response.setContentType("application/pdf");
        Document document = new Document(PageSize.A4);

        PdfWriter writer = null;
        try {
            Loan lform = loanSancDAO.getLoanInterestIntimationData(agAdrefid);
            response.setHeader("Content-Disposition", "attachment; filename=AG_INTEREST_INTIMATION_" + selectedEmpObj.getEmpId() + ".pdf");

            writer = PdfWriter.getInstance(document, response.getOutputStream());

            document.open();

            PdfPTable table = null;
            PdfPCell cell = null;

            Font f1 = new Font();
            f1.setSize(10);
            f1.setFamily("Times New Roman");

            table = new PdfPTable(2);
            table.setWidths(new int[]{2, 4});
            table.setWidthPercentage(100);

            cell = new PdfPCell(new Phrase("Sl No:   15016", f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Interest Intimation Form", new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD)));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(2);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setFixedHeight(20);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("OFFICE OF THE PRINCIPAL ACCOUNTANT GENERAL (A&E)", new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.BOLD)));
            cell.setColspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("ODISHA, BHUBANESWAR", new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.BOLD)));
            cell.setColspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(2);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setFixedHeight(20);
            table.addCell(cell);

            document.add(table);

            table = new PdfPTable(2);
            table.setWidths(new int[]{4, 2});
            table.setWidthPercentage(100);

            cell = new PdfPCell(new Phrase("No LTA-II.............../MCA(       )/", f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Dated: 12-Oct-2020", f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(2);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setFixedHeight(20);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("To", f1));
            cell.setColspan(2);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(2);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setFixedHeight(50);
            table.addCell(cell);

            document.add(table);

            table = new PdfPTable(2);
            table.setWidths(new float[]{0.5f, 5.5f});
            table.setWidthPercentage(100);

            cell = new PdfPCell(new Phrase("Subject:", f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Interest on " + lform.getLoanName() + " in respect of Shri/Smt " + selectedEmpObj.getFullName() + "(ID No. " + selectedEmpObj.getGpfno() + ")", f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(2);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setFixedHeight(20);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Sir,", f1));
            cell.setColspan(2);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("        I am to state that the interest on " + lform.getLoanName() + " Advance of " + lform.getOriginalAmt() + "(Rupees " + Numtowordconvertion.convertNumber(lform.getOriginalAmt()) + ")only drawn vide Tv. No. Dt by Shri/Smt " + selectedEmpObj.getFullName() + "(ID No. " + selectedEmpObj.getGpfno() + ")"
                    + " is worked out to " + lform.getAgInterestAmt() + " (Rupees " + Numtowordconvertion.convertNumber(Integer.parseInt(lform.getAgInterestAmt())) + ") only subject to recovery of interest made immediately after last instalment of principal as stipulated"
                    + " in sanction order, which may be recovered under the Head of Account \"0049-04-800-0060-10019-003 Interest for purchase of Motor Conveyances\".", f1));
            cell.setColspan(2);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(2);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setFixedHeight(20);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("2.In case, recovery starts from salary of OCT-2020, the interest to be recovered is " + lform.getAgInterestAmt() + "(Rupees " + Numtowordconvertion.convertNumber(Integer.parseInt(lform.getAgInterestAmt())) + ") only. In the event"
                    + " of further default, revised interest would be intimated in due course.", f1));
            cell.setColspan(2);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            /*cell = new PdfPCell(new Phrase("3.The interest calculated above is subject to adjustment of instalments of principal for 5,200 which relates to SEP-2020,OCT-2020,NOV-2020,DEC-2020.", f1));
             cell.setColspan(2);
             cell.setBorder(Rectangle.NO_BORDER);
             table.addCell(cell);
            
             cell = new PdfPCell(new Phrase("4.In respect of Para.3 above detailed particulars of wanting instalment such as T.V.No/Challan No, Date, Treasury and Salary Head of Account may please be"+
             " furnished to this office duly signed by the Drawing and Disbursing Officer for necessary reconciliation of his/her M.C. Advance Account.", f1));
             cell.setColspan(2);
             cell.setBorder(Rectangle.NO_BORDER);
             table.addCell(cell);*/
            cell = new PdfPCell();
            cell.setColspan(2);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setFixedHeight(20);
            table.addCell(cell);

            document.add(table);

            table = new PdfPTable(2);
            table.setWidths(new int[]{4, 2});
            table.setWidthPercentage(100);

            cell = new PdfPCell();
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Yours faithfully,", f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(2);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setFixedHeight(30);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("SR. ACCOUNTS OFFICER, ODISHA", f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(2);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setFixedHeight(20);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Memo No.LTA-II.............../MCA(       )/", f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Dated: 12-Oct-2020", f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(2);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setFixedHeight(20);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Copy forwarded to Shri/Smt " + selectedEmpObj.getFullName() + "(ID No. " + selectedEmpObj.getGpfno() + ") C/o", f1));
            cell.setColspan(2);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(2);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setFixedHeight(20);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("SR. ACCOUNTS OFFICER, ODISHA", f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            document.add(table);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }

    @ResponseBody
    @RequestMapping(value = "updateLoanSanctionSBData")
    public void updateLoanSanctionSBData(HttpServletResponse response, @ModelAttribute("SelectedEmpObj") Users lub, ModelMap model, @ModelAttribute("loanForm") Loan loanForm, BindingResult result
    ) {

        response.setContentType("text/html");
        PrintWriter out = null;

        try {
            int updatestatus = loanSancDAO.updateLoanSanctionSBData(loanForm);

            out = response.getWriter();
            out.write(updatestatus + "");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    @RequestMapping(value = "DownloadAGInterestDetailedCalculation")
    public void DownloadAGInterestDetailedCalculation(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpObj") Users selectedemp, @RequestParam("gpfno") String gpfno, @RequestParam("loantp") String loantp) throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ZipOutputStream zos = new ZipOutputStream(baos);
        FileInputStream fis = null;
        BufferedInputStream bis = null;

        byte bytes[] = new byte[2048];

        String zipFilename = gpfno + "_" + loantp + ".zip";
        try {
            if (selectedemp.getOffcode() != null && !selectedemp.getOffcode().equals("")) {
                String ddocode = selectedemp.getOffcode().substring(0, 9);
                String filename = ddocode + "_" + gpfno + "_" + loantp + ".pdf";
                String filenamedetail = ddocode + "_" + gpfno + "_" + loantp + "_DET.pdf";

                String agfilePath = servletContext.getInitParameter("agdownloadPDFPath");
                //String agfilePath = servletContext.getInitParameter("agfilePath");

                fis = new FileInputStream(agfilePath + "/LTA/LTA_INT_CAL/" + loantp + "/" + ddocode + "/" + filename);
                bis = new BufferedInputStream(fis);
                zos.putNextEntry(new ZipEntry(filename));
                int bytesRead;
                while ((bytesRead = bis.read(bytes)) != -1) {
                    zos.write(bytes, 0, bytesRead);
                }

                zos.closeEntry();
                bis.close();
                fis.close();

                fis = new FileInputStream(agfilePath + "/LTA/LTA_INT_CAL/" + loantp + "/" + ddocode + "/" + filenamedetail);
                bis = new BufferedInputStream(fis);
                zos.putNextEntry(new ZipEntry(filenamedetail));

                while ((bytesRead = bis.read(bytes)) != -1) {
                    zos.write(bytes, 0, bytesRead);
                }

                zos.closeEntry();
                bis.close();
                fis.close();

                zos.flush();
                baos.flush();
                zos.close();
                baos.close();

                OutputStream out = response.getOutputStream();
                response.setContentType("application/zip");
                response.setHeader("Content-Disposition", "attachment; filename=\"" + zipFilename + "\"");
                out.write(baos.toByteArray());
                out.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    @ResponseBody
    @RequestMapping(value = "downloadNPSLGovernmentContributionDocument")
    public void downloadAttachedDocumentCopy(HttpServletResponse response, @RequestParam("loanid") String loanid
    ) {

        FileAttribute fa = null;

        FileInputStream inputStream = null;
        OutputStream outStream = null;

        try {
            outStream = response.getOutputStream();

            String filepath = servletContext.getInitParameter("PhotoPath");
            fa = loanSancDAO.getAttachedDocument(filepath, "GCFILE", loanid);

            response.setContentLength((int) fa.getUploadAttachment().length());
            response.setContentType(fa.getFileType());
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fa.getOriginalFileName() + "\"");

            byte[] buffer = new byte[4096];
            int bytesRead = -1;

            // write bytes read from the input stream into the output stream
            inputStream = new FileInputStream(fa.getUploadAttachment());
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
                outStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @ResponseBody
    @RequestMapping(value = "deleteLoanSanctionAttachment")
    public void deleteLoanSanctionAttachment(HttpServletResponse response, @RequestParam("loanId") String loanid
    ) {

        response.setContentType("application/json");

        FileAttribute fa = null;

        JSONObject obj = null;
        try {
            String filepath = servletContext.getInitParameter("PhotoPath");
            fa = loanSancDAO.getAttachedDocument(filepath, "GCFILE", loanid);

            int deletestatus = loanSancDAO.deleteLoanAttachment(loanid, filepath, fa);

            obj = new JSONObject();
            obj.append("deletestatus", deletestatus);
            PrintWriter out = response.getWriter();
            out.write(obj.toString());
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "EditLoanDataSBCorrection.htm")
    public ModelAndView EditLoanDataSBCorrection(@ModelAttribute("LoginUserBean") LoginUserBean lubloginuser, @ModelAttribute("loanForm") Loan loanForm, @RequestParam Map<String, String> requestParams) {

        ModelAndView mv = null;

        try {
            int correctionid = 0;
            if (requestParams.get("correctionid") != null && !requestParams.get("correctionid").equals("") && !requestParams.get("correctionid").equals("undefined")) {
                correctionid = Integer.parseInt(requestParams.get("correctionid"));
            }
            if (correctionid > 0) {
                loanForm = loanSancDAO.editLoanDataSBCorrectionDDO(loanForm.getLoanid(), correctionid);
            } else {
                loanForm = loanSancDAO.editLoanData(loanForm.getLoanid());
            }

            String mode = requestParams.get("mode");
            String entrytype = requestParams.get("entrytypeSBCorrection");
            loanForm.setEntryTypeSBCorrection(entrytype);

            List bankList = bankDAO.getBankList();
            List treasuryList = treasuryDao.getTreasuryList();
            List loanTypeList = loanTypeDAO.getLoanTypeList();
            List deptlist = deptDAO.getDepartmentList();
            List distlist = districtDAO.getDistrictList();
            String userType = lubloginuser.getLoginusertype();
            String allowUpdate = "";
            if (userType != null && !userType.equals("") && (userType.equals("A") || userType.equals("D"))) {
                allowUpdate = "Y";
            }

            mv = new ModelAndView("/loansanction/EditLoanSanctionSBCorrection", "command", loanForm);
            //mv.addObject("Monthwiseinstl", loanSancDAO.getMonthWiseInstlment(lubloginuser.getLoginempid(), loanForm.getLoanid()));
            mv.addObject("bankList", bankList);
            mv.addObject("treasuryList", treasuryList);
            mv.addObject("loanTypeList", loanTypeList);
            mv.addObject("AllowUpdate", allowUpdate);
            mv.addObject("deptlist", deptlist);
            mv.addObject("distlist", distlist);
            mv.addObject("mode", mode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mv;
    }

    @RequestMapping(value = "SaveLoanSanctionSBCorrection.htm", params = "action=Save")
    public String SaveLoanSanctionSBCorrection(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("loanForm") Loan loanForm) throws IOException {

        try {
            loanForm.setEmpid(lub.getLoginempid());
            SbCorrectionRequestModel sbCorReqBean = new SbCorrectionRequestModel();
            sbCorReqBean.setEmpHrmsId(loanForm.getEmpid());
            sbCorReqBean.setHidNotId(loanForm.getNotid() + "");
            sbCorReqBean.setHidNotType("LOAN_SANC");
            sbCorReqBean.setEntrytype(loanForm.getEntryTypeSBCorrection());
            sbCorReqDao.deleteRequestedServiceBookLanguage(sbCorReqBean.getEmpHrmsId(), sbCorReqBean.getHidNotType(), sbCorReqBean.getHidNotId());
            int sbcorrectionid = sbCorReqDao.saveRequestedServiceBookLanguage(sbCorReqBean);

            NotificationBean nb = new NotificationBean();

            if (loanForm.getNotid() > 0) {
                nb.setNotid(loanForm.getNotid());
            }
            nb.setNottype("LOAN_SANC");
            nb.setEmpId(loanForm.getEmpid());
            nb.setDateofEntry(new Date());
            nb.setOrdno(loanForm.getOrderno());
            nb.setOrdDate(CommonFunctions.getDateFromString(loanForm.getOrderdate(), "dd-MMM-yyyy"));
            nb.setNote(loanForm.getNote());
            nb.setSancDeptCode(loanForm.getHidSancDeptCode());
            nb.setSancOffCode(loanForm.getHidSancOffCode());
            nb.setSancAuthCode(loanForm.getSancSpc());
            if (loanForm.getChkNotSBPrint() != null && loanForm.getChkNotSBPrint().equals("Y")) {
                nb.setIfVisible("N");
            }
            nb.setRefcorrectionid(sbcorrectionid);

            notificationDao.deleteNotificationDataSBCorrection(nb.getNotid(), nb.getNottype());
            notificationDao.insertNotificationDataSBCorrection(nb);

            String sblang = loanSancDAO.SaveLoanDetailSBCorrection(loanForm, nb.getNotid(), sbcorrectionid);

            sbCorReqDao.updateRequestedServiceBookLanguage(sbcorrectionid, sblang);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/sbCorrectRequest.htm?btnRequest=Search&sbReqModuleName=LOAN";
    }

    @RequestMapping(value = "SaveLoanSanctionSBCorrection", params = {"action=Submit"})
    public String submitPayFixationSBCorrection(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("loanForm") Loan loanForm) {

        try {
            sbCorReqDao.submitRequestedServiceBookLanguage(lub.getLoginempid(), loanForm.getNotid(), "LOAN_SANC");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/sbCorrectRequest.htm?btnRequest=Search&sbReqModuleName=LOAN";
    }

    @RequestMapping(value = "EditLoanDataSBCorrectionDDO.htm")
    public ModelAndView EditLoanDataSBCorrectionDDO(@ModelAttribute("LoginUserBean") LoginUserBean lubloginuser, @ModelAttribute("loanForm") Loan loanForm, @RequestParam Map<String, String> requestParams) {

        ModelAndView mv = null;

        try {
            String mode = requestParams.get("mode");
            String loanid = requestParams.get("loanid");
            String entrytype = requestParams.get("entrytype");

            String empid = requestParams.get("empid");
            int sbcorrectionid = Integer.parseInt(requestParams.get("correctionid"));

            loanForm = loanSancDAO.editLoanDataSBCorrectionDDO(loanid, sbcorrectionid);
            loanForm.setCorrectionid(sbcorrectionid + "");
            loanForm.setEntryTypeSBCorrection(entrytype);

            List bankList = bankDAO.getBankList();
            List treasuryList = treasuryDao.getTreasuryList();
            List loanTypeList = loanTypeDAO.getLoanTypeList();
            List deptlist = deptDAO.getDepartmentList();
            List distlist = districtDAO.getDistrictList();
            String userType = lubloginuser.getLoginusertype();
            String allowUpdate = "";
            if (userType != null && !userType.equals("") && (userType.equals("A") || userType.equals("D"))) {
                allowUpdate = "Y";
            }

            mv = new ModelAndView("/loansanction/EditLoanSanctionSBCorrection", "command", loanForm);
            //mv.addObject("Monthwiseinstl", loanSancDAO.getMonthWiseInstlment(empid, loanid));
            mv.addObject("bankList", bankList);
            mv.addObject("treasuryList", treasuryList);
            mv.addObject("loanTypeList", loanTypeList);
            mv.addObject("AllowUpdate", allowUpdate);
            mv.addObject("deptlist", deptlist);
            mv.addObject("distlist", distlist);
            mv.addObject("mode", mode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mv;
    }

    @RequestMapping(value = "SaveLoanSanctionSBCorrection", params = {"action=Approve"})
    public String ApproveLoanSanctionSBCorrection(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("loanForm") Loan loanForm) {

        try {
            loanSancDAO.approveLoansanctionDataSBCorrection(loanForm, lub.getLogindeptcode(), lub.getLoginoffcode(), lub.getLoginspc(), lub.getLoginempid());

            sbCorReqDao.updateRequestedServiceBookFlag(loanForm.getEmpid(), "LOAN_SANC", loanForm.getNotid() + "", loanForm.getCorrectionid(), lub.getLoginempid());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/DDOServiceBookCorrection.htm?empid=" + loanForm.getEmpid();
    }
}
