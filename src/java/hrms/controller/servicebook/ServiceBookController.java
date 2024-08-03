package hrms.controller.servicebook;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import hrms.SelectOption;
import hrms.common.FileDownload;
import hrms.common.HRMSServiceConfiguration;
import hrms.dao.PayInServiceRecord.PayInServiceRecordDAO;
import hrms.dao.employee.EmployeeDAO;
import hrms.dao.incrementsanction.IncrementSanctionDAO;
import hrms.dao.leave.LeaveDAO;
import hrms.dao.leaveAccount.ServiceBookLanguageDAO;
import hrms.dao.report.gispassbook.GisPassbookDAO;
import hrms.dao.servicebook.ServiceBookDAO;
import hrms.dao.servicerecord.ServiceRecordDAO;
import hrms.model.employee.Address;
import hrms.model.employee.Education;
import hrms.model.employee.Employee;
import hrms.model.employee.FamilyRelation;
import hrms.model.employee.IdentityInfo;
import hrms.model.incrementsanction.IncrementForm;
import hrms.model.leave.CreditLeaveProperties;
import hrms.model.leave.EmpLeaveAccountPropeties;
import hrms.model.leave.EmpLeaveAvailedPropeties;
import hrms.model.leave.EmpLeaveSurrenderedPropeties;
import hrms.model.leave.EmployeeLeaveAccount;
import hrms.model.login.LoginUserBean;
import hrms.model.login.Users;
import hrms.model.parmast.ParStatusBean;
import hrms.model.recommendation.RecommendationDetailBean;
import hrms.model.report.gispassbook.gisPassbook;
import hrms.model.servicebook.EmpServiceBook;
import hrms.model.servicebook.EmpServiceHistory;
import hrms.model.servicebook.ServiceBookAnomali;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ModelAndView;

@Controller
@SessionAttributes({"LoginUserBean", "SelectedEmpObj"})
public class ServiceBookController implements ServletContextAware {

    @Autowired
    ServiceBookDAO serviceBookDao;

    @Autowired
    ServiceBookLanguageDAO sbDAO;

    @Autowired
    EmployeeDAO employeeDAO;

    @Autowired
    LeaveDAO leaveDao;

    @Autowired
    GisPassbookDAO gispassbookDao;

    @Autowired
    public IncrementSanctionDAO incrementsancDAO;

    @Autowired
    public ServiceRecordDAO servicerecordDAO;

    @Autowired
    public PayInServiceRecordDAO payInSRDao;

    private ServletContext context;

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.context = servletContext;
    }

    @ResponseBody
    @RequestMapping(value = "generateServicebookLanguage.htm", method = RequestMethod.GET)
    public void generateServicebookLanguage(@ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @RequestParam Map<String, String> req, HttpServletRequest request, HttpServletResponse response) {
        String moduleName = req.get("moduleName");
        int notId = Integer.parseInt(req.get("notId"));
        String sblang = "";

        response.setContentType("application/json");

        PrintWriter out = null;
        JSONObject jobj = null;

        try {
            if (moduleName != null && !moduleName.equals("")) {
                if (moduleName.equalsIgnoreCase("INCREMENT")) {
                    String payComm = selectedEmpObj.getPayCommission();
                    if (payComm == null || payComm.equals("")) {
                        payComm = "6";
                    }
                    IncrementForm incfb = incrementsancDAO.getEmpIncRowDataForSB(selectedEmpObj.getEmpId(), notId, payComm);
                    sblang = sbDAO.getIncrementSanctionDetails(incfb, notId, payComm);

                }
            }

            out = response.getWriter();
            out.write(jobj.toString());
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }

    }

    @RequestMapping(value = "servicebook.htm", method = RequestMethod.GET)
    public ModelAndView serviceBook(@ModelAttribute("LoginUserBean") LoginUserBean lub) {

        EmpServiceBook esb = serviceBookDao.getSHReport(lub.getLoginempid());
        Date todaysDate = new Date();
        DateFormat df3 = new SimpleDateFormat("dd-MMM-yyyy");
        String str3 = df3.format(todaysDate);
        Employee employeeProfile = employeeDAO.getEmployeeProfile(lub.getLoginempid());
        Education[] educations = employeeDAO.getEmployeeEducation(lub.getLoginempid(), str3);
        FamilyRelation[] familyRel = employeeDAO.getEmployeeFamily(lub.getLoginempid());
        List nomineedetaillist = employeeDAO.getEmployeeNominee(lub.getLoginempid());
        //Address[] address = employeeDAO.getAddress(lub.getLoginempid());
        Address address = employeeDAO.getAddressForServiceBook(lub.getLoginempid());
        List identity = employeeDAO.getIdentityList(lub.getLoginempid());
        ModelAndView mv = new ModelAndView("/servicebook/MyServiceBook", "employeeProfile", employeeProfile);
        mv.addObject("esb", esb);
        mv.addObject("educations", educations);
        mv.addObject("familyRel", familyRel);
        mv.addObject("address", address);
        String presentAddressDetails = "";
        String permanentAddressDetails = "";
        List addressList = Arrays.asList(address);
        if (addressList != null && addressList.size() > 0) {
            Address addr = null;
            for (int i = 0; i < addressList.size(); i++) {
                addr = (Address) addressList.get(i);
                if (addr.getAddressType() != null && addr.getAddressType().equals("PRESENT")) {
                    presentAddressDetails = addr.getAddress();
                }
                if (addr.getAddressType() != null && addr.getAddressType().equals("PERMANENT")) {
                    permanentAddressDetails = addr.getAddress();
                }
            }
        }
        mv.addObject("identity", identity);
        mv.addObject("nomineedetail", nomineedetaillist);
        mv.addObject("presentAddressDetails", address.getPresentAddress());
        mv.addObject("permenentAddressDetails", address.getPermanentAddress());
        return mv;
    }

    @RequestMapping(value = "myserviceBookPrePage.htm", method = RequestMethod.GET)
    public ModelAndView myserviceBookPrePage(@ModelAttribute("LoginUserBean") LoginUserBean lub, Map<String, Object> model) {
        ModelAndView mv = new ModelAndView("/servicebook/MyServiceBookPrePage");

        return mv;
    }

    @RequestMapping(value = "serviceBookPrePage.htm", method = RequestMethod.GET)
    public ModelAndView serviceBookPrePage(@ModelAttribute("LoginUserBean") LoginUserBean lub, Map<String, Object> model) {
        ModelAndView mv = new ModelAndView("/servicebook/ServiceBookPrePage");

        return mv;
    }

    @RequestMapping(value = "employeeServiceBook.htm", method = RequestMethod.GET)
    public ModelAndView employeeServiceBook(@ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("employeeLeaveAccount") EmployeeLeaveAccount employeeLeaveAccount,
            @ModelAttribute("empGisBook") gisPassbook empGisBook, Map<String, Object> model) {
        HRMSServiceConfiguration.serviceBookRequested();
        ModelAndView mv = new ModelAndView();
        System.out.println(HRMSServiceConfiguration.noofServiceBookRequestedByOffice());
        if (HRMSServiceConfiguration.isResourceAvailable() == true) {
            EmpServiceBook esb = serviceBookDao.getSHReport(selectedEmpObj.getEmpId());
            EmpLeaveAccountPropeties eap = leaveDao.getLeaveAccountDetails(selectedEmpObj.getEmpId(), "", "", "EL", "EOL");
            Date todaysDate = new Date();
            DateFormat df3 = new SimpleDateFormat("dd-MMM-yyyy");
            String str3 = df3.format(todaysDate);
            Employee employeeProfile = employeeDAO.getEmployeeProfile(selectedEmpObj.getEmpId());
            Education[] educations = employeeDAO.getEmployeeEducation(selectedEmpObj.getEmpId(), str3);
            FamilyRelation[] familyRel = employeeDAO.getEmployeeFamily(selectedEmpObj.getEmpId());
            List nomineedetaillist = employeeDAO.getEmployeeNominee(selectedEmpObj.getEmpId());
            //Address[] address = employeeDAO.getAddress(selectedEmpObj.getEmpId());
            Address address = employeeDAO.getAddressForServiceBook(selectedEmpObj.getEmpId());
            List identity = employeeDAO.getIdentityList(selectedEmpObj.getEmpId());
            ArrayList gispassbookDetails = gispassbookDao.getgisPassbookList(selectedEmpObj.getEmpId());
            ArrayList servicerecordlist = servicerecordDAO.getServiceRecordList(selectedEmpObj.getEmpId());
            List payInServiceList = payInSRDao.getpayInServiceData(selectedEmpObj.getEmpId());
            //List empDetails = gispassbookDao.getgisEmployeeDetaills(selectedEmpObj.getEmpId());

            String presentaddress = "";
            String permanentaddress = "";

            List addressList = Arrays.asList(address);
            if (addressList != null && addressList.size() > 0) {
                Address addr = null;
                for (int i = 0; i < addressList.size(); i++) {
                    addr = (Address) addressList.get(i);
                    if (addr.getAddressType() != null && !addr.getAddressType().equals("")) {
                        if (addr.getAddressType().equals("PRESENT")) {
                            presentaddress = addr.getAddress();
                        } else if (addr.getAddressType().equals("PERMANENT")) {
                            permanentaddress = addr.getAddress();
                        }
                    }
                }
            }

            //mav.addObject("empInfo", empDetails);
            mv = new ModelAndView("/servicebook/ServiceBook", "employeeProfile", employeeProfile);
            mv.addObject("esb", esb);
            mv.addObject("educations", educations);
            mv.addObject("familyRel", familyRel);
            mv.addObject("address", address);
            mv.addObject("presentaddress", address.getPresentAddress());
            mv.addObject("permanentaddress", address.getPermanentAddress());
            mv.addObject("identity", identity);
            mv.addObject("eap", eap);
            mv.addObject("gisList", gispassbookDetails);
            mv.addObject("servicerecordList", servicerecordlist);
            mv.addObject("payInServicelist", payInServiceList);
            mv.addObject("nomineedetail", nomineedetaillist);
        } else if (HRMSServiceConfiguration.isResourceAvailable() == false) {
            mv = new ModelAndView("/errorpage/errorresourcebusy");

        }
        HRMSServiceConfiguration.serviceBookRequestEnds();
        return mv;
    }

    @RequestMapping(value = "servicebookPDF.htm")
    public void servicebookPDF(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpObj") Users selectedEmpObj) {

        response.setContentType("application/pdf");
        Document document = new Document(PageSize.A4);

        String empGender = null;
        boolean stopMonthlySubscrip = false;

        PdfWriter writer = null;
        try {
            String logopath = context.getInitParameter("SBPath");
            String photopath = context.getInitParameter("PhotoPath");

            response.setHeader("Content-Disposition", "attachment; filename=SERVICE_BOOK_" + selectedEmpObj.getEmpId() + ".pdf");

            writer = PdfWriter.getInstance(document, response.getOutputStream());
            writer.setPageEvent(new ServiceBookHeaderFooter(selectedEmpObj.getEmpId(), selectedEmpObj.getGpfno(), selectedEmpObj.getFullName()));
            Font bold = new Font(Font.FontFamily.HELVETICA, 13, Font.BOLD);
            document.open();

            //EmpServiceBook esb = new EmpServiceBook();
            EmpServiceBook esb = serviceBookDao.getSHReport(selectedEmpObj.getEmpId());
            Date todaysDate = new Date();
            DateFormat df3 = new SimpleDateFormat("dd-MMM-yyyy");
            String str3 = df3.format(todaysDate);
            //  esb.setServerDate(str3);
            Employee employeeProfile = employeeDAO.getEmployeeProfile(selectedEmpObj.getEmpId());
            Education[] educations = employeeDAO.getEmployeeEducation(selectedEmpObj.getEmpId(), str3);
            FamilyRelation[] familyRel = employeeDAO.getEmployeeFamily(selectedEmpObj.getEmpId());
            //Address[] address = employeeDAO.getAddress(selectedEmpObj.getEmpId());
            Address address = employeeDAO.getAddressForServiceBook(selectedEmpObj.getEmpId());
            List identity = employeeDAO.getIdentityList(selectedEmpObj.getEmpId());
            List nomineedetaillist = employeeDAO.getEmployeeNominee(selectedEmpObj.getEmpId());

            stopMonthlySubscrip = employeeDAO.stopGpfDeduction(selectedEmpObj.getEmpId());

            PdfPTable table = null;
            PdfPCell cell = null;

            Font f1 = new Font();
            f1.setSize(8);
            f1.setFamily("Times New Roman");

            Font f2 = new Font();
            f2.setSize(8);
            f2.setFamily("Times New Roman");

            table = new PdfPTable(1);
            table.setWidths(new int[]{2});
            table.setWidthPercentage(100);

            //String url = filePath + paf.getApplicantempid() + ".jpg";
            String url = logopath + "/odgovt.gif";
            File f = null;
            f = new File(url);
            Image img = null;
            if (f.exists()) {
                img = Image.getInstance(url);
            }

            if (img != null) {
                img.scaleToFit(100f, 80f);
                cell = new PdfPCell(img);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
            } else {
                cell = new PdfPCell();
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
            }

            cell = new PdfPCell(new Phrase("HUMAN RESOURCES MANAGEMENT SYSTEM", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
            cell.setFixedHeight(20);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Government of Odisha", new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD)));
            cell.setFixedHeight(20);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setFixedHeight(60);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("SERVICE BOOK", new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("OF", new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            url = photopath + selectedEmpObj.getEmpId() + ".jpg";
            f = new File(url);
            img = null;
            if (f.exists()) {
                img = Image.getInstance(url);
            }

            if (img != null) {
                img.scaleToFit(100f, 80f);
                img.setBorder(Rectangle.BOX);
                img.setBorderColor(BaseColor.BLACK);
                img.setBorderWidth(1f);
                cell = new PdfPCell(img);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
            } else {
                url = logopath + "NoEmployee.png";
                f = new File(url);
                img = null;
                if (f.exists()) {
                    img = Image.getInstance(url);
                    img.scaleToFit(100f, 80f);
                    img.setBorder(Rectangle.BOX);
                    img.setBorderColor(BaseColor.BLACK);
                    img.setBorderWidth(1f);
                    cell = new PdfPCell(img);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);
                }
            }

            cell = new PdfPCell(new Phrase(employeeProfile.getEmpName(), new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(employeeProfile.getEmpCadre(), f1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(employeeProfile.getEmpAllotmentYear(), f1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            if (employeeProfile.getCadreId() != null && !employeeProfile.getCadreId().equals("")) {
                cell = new PdfPCell(new Phrase(employeeProfile.getCadreId(), f1));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
            }

            cell = new PdfPCell(new Phrase("HRMS ID: " + employeeProfile.getEmpid(), f1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(employeeProfile.getAccttype() + " NO: " + employeeProfile.getGpfno(), f1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            //if (employeeProfile.getGisName() != null && !employeeProfile.getGisName().equals("")) {
            cell = new PdfPCell(new Phrase("GIS NO: " + employeeProfile.getGisNo(), f1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            //}
            /*if (employeeProfile.getGisNo() != null && !employeeProfile.getGisNo().equals("")) {
             cell = new PdfPCell(new Phrase(employeeProfile.getGisNo(), f1));
             cell.setHorizontalAlignment(Element.ALIGN_CENTER);
             cell.setBorder(Rectangle.NO_BORDER);
             table.addCell(cell);
             }*/

            cell = new PdfPCell();
            cell.setFixedHeight(60);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setFixedHeight(60);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setFixedHeight(60);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setFixedHeight(60);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            url = logopath + "/file.JPG";
            f = new File(url);
            img = null;
            if (f.exists()) {
                img = Image.getInstance(url);
            }

            if (img != null) {
                img.scaleToFit(100f, 80f);
                cell = new PdfPCell(img);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
            } else {
                cell = new PdfPCell();
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
            }

            cell = new PdfPCell(new Phrase("Developed By", f1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Centre for Modernizing Government Initiative(CMGI)", f1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            //document.add(table);
            document.newPage();

            url = logopath + "/" + selectedEmpObj.getEmpId() + ".jpg";
            f = new File(url);
            img = null;
            if (f.exists()) {
                img = Image.getInstance(url);
            }

            if (img != null) {
                img.scaleToFit(600f, 580f);
                img.setBorder(Rectangle.BOX);
                img.setBorderColor(BaseColor.BLACK);
                img.setBorderWidth(1f);
                cell = new PdfPCell(img);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
            } else {
                url = logopath + "/SB1stPage.png";
                f = new File(url);
                img = null;
                if (f.exists()) {
                    img = Image.getInstance(url);
                }
                if (img != null) {
                    img.scaleToFit(600f, 5800f);
                    img.setBorder(Rectangle.BOX);
                    img.setBorderColor(BaseColor.BLACK);
                    img.setBorderWidth(1f);
                    cell = new PdfPCell(img);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);
                }
            }

            document.add(table);
            document.newPage();

            table = new PdfPTable(4);
            table.setWidths(new int[]{1, 1, 1, 1});
            table.setWidthPercentage(100);

            cell = new PdfPCell(new Phrase("NAME:", f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(employeeProfile.getEmpName(), f1));
            cell.setColspan(3);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(employeeProfile.getAccttype() + " NO:", f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(employeeProfile.getGpfno(), f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("HRMS ID:", f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(employeeProfile.getEmpid(), f1));
            table.addCell(cell);

            String addressDetails = "";

            String presentaddress = "";
            String permanentaddress = "";

            List addressList = Arrays.asList(address);
            if (addressList != null && addressList.size() > 0) {
                Address addr = null;
                for (int i = 0; i < addressList.size(); i++) {
                    addr = (Address) addressList.get(i);
                    if (addr.getAddressType() != null && !addr.getAddressType().equals("")) {
                        if (addr.getAddressType().equals("PRESENT")) {
                            presentaddress = addr.getAddress();
                        } else if (addr.getAddressType().equals("PERMANENT")) {
                            permanentaddress = addr.getAddress();
                        }
                    }
                    //addressDetails = addr.getAddress();
                }
            }

            cell = new PdfPCell(new Phrase("PRESENT ADDRESS:\n(RESIDENCE)", f1));
            cell.setFixedHeight(20);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(StringUtils.defaultString(address.getPresentAddress()), f2));
            cell.setFixedHeight(20);
            cell.setColspan(3);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("DATE OF BIRTH BY CHRISTIAN ERA AS NEARLY AS CAN BE ASCERTAINED :", f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(StringUtils.defaultString(employeeProfile.getDob()) + "(" + StringUtils.defaultString(employeeProfile.getDobText()) + ")", f2));
            cell.setColspan(3);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("EMPLOYEE EDUCATIONAL DETAILS:", f1));
            cell.setColspan(4);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);

            List educationList = Arrays.asList(educations);

            if (educationList != null && educationList.size() > 0) {
                Education education = null;
                for (int i = 0; i < educationList.size(); i++) {
                    education = (Education) educationList.get(i);

                    cell = new PdfPCell(new Phrase(education.getQualification(), f1));
                    cell.setColspan(4);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("Year of Passing: " + education.getYearofpass(), f1));
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase("Faculty: " + education.getFaculty(), f1));
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase("Degree\nCertificate:", f1));
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(education.getDegree(), f1));
                    table.addCell(cell);
                }
            }

            cell = new PdfPCell(new Phrase("HEIGHT(in cm):", f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(StringUtils.defaultString(employeeProfile.getHeight() + ""), f2));
            cell.setColspan(3);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("PERSONAL\nIDENTIFICATION MARK :", f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(StringUtils.defaultString(employeeProfile.getIdmark()), f2));
            cell.setColspan(3);
            table.addCell(cell);

            String fatherDetails = "";

            List familyList = Arrays.asList(familyRel);
            if (familyList != null && familyList.size() > 0) {
                FamilyRelation familyRelation = null;
                for (int i = 0; i < familyList.size(); i++) {
                    familyRelation = (FamilyRelation) familyList.get(i);
                    if (familyRelation.getRelation() != null && familyRelation.getRelation().equals("FATHER")) {
                        fatherDetails = StringUtils.defaultString(familyRelation.getInitials()) + " " + StringUtils.defaultString(familyRelation.getFname()) + " " + StringUtils.defaultString(familyRelation.getMname()) + " " + StringUtils.defaultString(familyRelation.getLname());
                    }
                }
            }

            cell = new PdfPCell(new Phrase("FATHER'S NAME\nAND RESIDENCE :", f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(fatherDetails + "\n" + address.getPermanentAddress(), f2));
            cell.setColspan(3);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("DATE OF ENTRY IN GOVERMENT SERVICE :", f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(employeeProfile.getDoeGov() + "(" + StringUtils.defaultString(employeeProfile.getEntryGovDateText()) + ")\n" + StringUtils.defaultString(employeeProfile.getEntryGovDateTime()), f2));
            cell.setColspan(3);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("DATE OF ENTRY IN THE SERVICE FOR WHICH SERVICE BOOK CREATED:", f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(employeeProfile.getJoindategoo() + "(" + StringUtils.defaultString(employeeProfile.getJoinDateText()) + ")", f2));
            cell.setColspan(3);
            table.addCell(cell);

            SelectOption so = serviceBookDao.getInitialJoiningData(selectedEmpObj.getEmpId());

            cell = new PdfPCell(new Phrase("INITIAL JOINING OFFICE:", f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(StringUtils.defaultString(so.getLabel()), f2));
            cell.setColspan(3);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("INITIAL JOINING POST:", f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(StringUtils.defaultString(so.getValue()), f2));
            cell.setColspan(3);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("DECLARATION OF HOME TOWN:", f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(employeeProfile.getHomeTown(), f2));
            cell.setColspan(3);
            table.addCell(cell);

            document.newPage();

            cell = new PdfPCell(new Phrase("DATE OF SUPERANNUATION:", f1));
            cell.setColspan(2);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(employeeProfile.getDor(), f2));
            cell.setColspan(2);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("CATEGORY:", f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(employeeProfile.getCategory(), f2));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("MARITAL STATUS:", f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(employeeProfile.getMaritalStatus(), f2));
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("CELL PHONE:", f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(employeeProfile.getMobile(), f2));
            cell.setColspan(3);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("IDENTITY OF THE EMPLOYEE:", f1));
            cell.setFixedHeight(20);
            cell.setColspan(4);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);

            PdfPTable innertable = new PdfPTable(5);
            innertable.setWidths(new int[]{1, 1, 1, 1, 1});
            innertable.setWidthPercentage(100);

            PdfPCell innercell = null;

            innercell = new PdfPCell(new Phrase("Type of Identification", f1));
            innertable.addCell(innercell);
            innercell = new PdfPCell(new Phrase("No.", f1));
            innertable.addCell(innercell);
            innercell = new PdfPCell(new Phrase("Place of Issue", f1));
            innertable.addCell(innercell);
            innercell = new PdfPCell(new Phrase("Date of Issue", f1));
            innertable.addCell(innercell);
            innercell = new PdfPCell(new Phrase("Date of Expiry", f1));
            innertable.addCell(innercell);

            cell = new PdfPCell(innertable);
            cell.setFixedHeight(20);
            cell.setColspan(4);
            table.addCell(cell);

            //List identityList = Arrays.asList(identity);
            if (identity != null && identity.size() > 0) {
                IdentityInfo idInfo = null;
                for (int i = 0; i < identity.size(); i++) {
                    idInfo = (IdentityInfo) identity.get(i);

                    innertable = new PdfPTable(5);
                    innertable.setWidths(new int[]{1, 1, 1, 1, 1});
                    innertable.setWidthPercentage(100);

                    innercell = new PdfPCell(new Phrase(idInfo.getIdentityDesc(), f2));
                    innertable.addCell(innercell);
                    innercell = new PdfPCell(new Phrase(idInfo.getIdentityNo(), f2));
                    innertable.addCell(innercell);
                    innercell = new PdfPCell(new Phrase(idInfo.getPlaceOfIssue(), f2));
                    innertable.addCell(innercell);
                    innercell = new PdfPCell(new Phrase(idInfo.getIssueDate(), f2));
                    innertable.addCell(innercell);
                    innercell = new PdfPCell(new Phrase(idInfo.getExpiryDate(), f2));
                    innertable.addCell(innercell);

                    cell = new PdfPCell(innertable);
                    cell.setFixedHeight(20);
                    cell.setColspan(4);
                    table.addCell(cell);
                }
            }

            cell = new PdfPCell(new Phrase("FAMILY OF THE EMPLOYEE:", f1));
            cell.setFixedHeight(20);
            cell.setColspan(4);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);

            innertable = new PdfPTable(6);
            innertable.setWidths(new int[]{1, 1, 1, 1, 1, 1});
            innertable.setWidthPercentage(100);

            innercell = new PdfPCell(new Phrase("Relation Type", f1));
            innertable.addCell(innercell);
            innercell = new PdfPCell(new Phrase("Name", f1));
            innertable.addCell(innercell);
            innercell = new PdfPCell(new Phrase("Is Alive?", f1));
            innertable.addCell(innercell);
            innercell = new PdfPCell(new Phrase("DOB", f1));
            innertable.addCell(innercell);
            innercell = new PdfPCell(new Phrase("Marital Status", f1));
            innertable.addCell(innercell);
            innercell = new PdfPCell(new Phrase("Age", f1));
            innertable.addCell(innercell);

            cell = new PdfPCell(innertable);
            cell.setFixedHeight(30);
            cell.setColspan(4);
            table.addCell(cell);

            if (familyList != null && familyList.size() > 0) {
                FamilyRelation familyRelation = null;
                for (int i = 0; i < familyList.size(); i++) {
                    familyRelation = (FamilyRelation) familyList.get(i);

                    innertable = new PdfPTable(6);
                    innertable.setWidths(new int[]{1, 1, 1, 1, 1, 1});
                    innertable.setWidthPercentage(100);

                    innercell = new PdfPCell(new Phrase(familyRelation.getRelation(), f2));
                    innertable.addCell(innercell);
                    innercell = new PdfPCell(new Phrase(StringUtils.defaultString(familyRelation.getInitials()) + " " + StringUtils.defaultString(familyRelation.getFname()) + " " + StringUtils.defaultString(familyRelation.getMname()) + " " + StringUtils.defaultString(familyRelation.getLname()), f2));
                    innertable.addCell(innercell);
                    innercell = new PdfPCell(new Phrase(familyRelation.getIfalive(), f2));
                    innertable.addCell(innercell);
                    innercell = new PdfPCell(new Phrase(familyRelation.getDob(), f2));
                    innertable.addCell(innercell);
                    innercell = new PdfPCell(new Phrase(familyRelation.getMarital(), f2));
                    innertable.addCell(innercell);
                    innercell = new PdfPCell(new Phrase(familyRelation.getAge(), f2));
                    innertable.addCell(innercell);

                    cell = new PdfPCell(innertable);
                    cell.setFixedHeight(20);
                    cell.setColspan(4);
                    table.addCell(cell);
                }
            }

            cell = new PdfPCell(new Phrase("NOMINEE:", f1));
            cell.setFixedHeight(20);
            cell.setColspan(4);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);

            innertable = new PdfPTable(6);
            innertable.setWidths(new int[]{1, 1, 1, 1, 1, 1});
            innertable.setWidthPercentage(100);

            innercell = new PdfPCell(new Phrase("Relation Type", f1));
            innertable.addCell(innercell);
            innercell = new PdfPCell(new Phrase("Name", f1));
            innertable.addCell(innercell);
            innercell = new PdfPCell(new Phrase("Is Alive?", f1));
            innertable.addCell(innercell);
            innercell = new PdfPCell(new Phrase("DOB", f1));
            innertable.addCell(innercell);
            innercell = new PdfPCell(new Phrase("Marital Status", f1));
            innertable.addCell(innercell);
            innercell = new PdfPCell(new Phrase("Age", f1));
            innertable.addCell(innercell);

            cell = new PdfPCell(innertable);
            cell.setFixedHeight(20);
            cell.setColspan(4);
            table.addCell(cell);

            innertable = new PdfPTable(6);
            innertable.setWidths(new int[]{1, 1, 1, 1, 1, 1});
            innertable.setWidthPercentage(100);

            FamilyRelation familyRelation = null;

            if (nomineedetaillist != null && nomineedetaillist.size() > 0) {
                for (int i = 0; i < nomineedetaillist.size(); i++) {
                    familyRelation = (FamilyRelation) nomineedetaillist.get(i);
                    innercell = new PdfPCell(new Phrase(familyRelation.getRelation(), f2));
                    innertable.addCell(innercell);
                    innercell = new PdfPCell(new Phrase(StringUtils.defaultString(familyRelation.getInitials()) + " " + StringUtils.defaultString(familyRelation.getFname()) + " " + StringUtils.defaultString(familyRelation.getMname()) + " " + StringUtils.defaultString(familyRelation.getLname()), f2));
                    innertable.addCell(innercell);
                    innercell = new PdfPCell(new Phrase(familyRelation.getIfalive(), f2));
                    innertable.addCell(innercell);
                    innercell = new PdfPCell(new Phrase(familyRelation.getDob(), f2));
                    innertable.addCell(innercell);
                    innercell = new PdfPCell(new Phrase(familyRelation.getMarital(), f2));
                    innertable.addCell(innercell);
                    innercell = new PdfPCell(new Phrase(familyRelation.getAge(), f2));
                    innertable.addCell(innercell);
                }
            }

            cell = new PdfPCell(innertable);
            cell.setFixedHeight(40);
            cell.setColspan(4);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Is Employed under any Reservation Category?", f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(employeeProfile.getIfReservation(), f2));
            cell.setColspan(3);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Reservation Category Under Which Employed", f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(StringUtils.defaultString(employeeProfile.getCategory()), f2));
            cell.setColspan(3);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Is Employed under Rehabilation Assistance Scheme?", f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(StringUtils.defaultString(employeeProfile.getIfRehabiltation()), f2));
            cell.setColspan(3);
            table.addCell(cell);

            document.add(table);
            document.newPage();

            table = new PdfPTable(4);
            table.setWidths(new float[]{1, 0.5f, 0.5f, 2});
            table.setWidthPercentage(100);

            cell = new PdfPCell(new Phrase("Post/Cadre/Scale of Pay", f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Pay", f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("WEF", f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Entry in the Service Book", f1));
            table.addCell(cell);

            if (esb.getEmpsbrecord() != null && esb.getEmpsbrecord().size() > 0) {
                EmpServiceHistory esh = null;
                for (int i = 0; i < esb.getEmpsbrecord().size(); i++) {
                    esh = (EmpServiceHistory) esb.getEmpsbrecord().get(i);

                    int slno = i + 1;

                    Phrase phrs = new Phrase();
                    Chunk c1 = new Chunk(StringUtils.defaultString(esh.getCategory()), new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL | Font.UNDERLINE));
                    Chunk c2 = new Chunk("\n\n" + StringUtils.defaultString(esh.getSbdescription()), f2);
                    Chunk c3 = new Chunk();
                    Chunk c4 = new Chunk();
                    if (esh.getModuleNote() != null && !esh.getModuleNote().equals("")) {
                        c3 = new Chunk("\n\nNote : ", f2);
                        c4 = new Chunk(StringUtils.defaultString(esh.getModuleNote()), f2);
                    }
                    phrs.add(c1);
                    phrs.add(c2);
                    phrs.add(c3);
                    phrs.add(c4);

                    Phrase col1stphrs = new Phrase();
                    c1 = new Chunk(slno + "", new Font(Font.FontFamily.TIMES_ROMAN, 4, Font.NORMAL));
                    col1stphrs.add(c1);
                    if (esh.getSpn() != null && !esh.getSpn().equals("")) {
                        c2 = new Chunk("\n" + esh.getSpn(), f2);
                        col1stphrs.add(c2);
                    }
                    if (esh.getCadre() != null && !esh.getCadre().equals("")) {
                        c3 = new Chunk("\n" + esh.getCadre(), f2);
                        col1stphrs.add(c3);
                    }
                    if (esh.getPayscale() != null && !esh.getPayscale().equals("")) {
                        c4 = new Chunk("\n" + esh.getPayscale(), f2);
                        col1stphrs.add(c4);
                    }
                    //cell = new PdfPCell(new Phrase(slno + "", new Font(Font.FontFamily.TIMES_ROMAN, 3, Font.NORMAL)));
                    cell = new PdfPCell(col1stphrs);
                    table.addCell(cell);
                    if (esh.getPay() != null && !esh.getPay().equals("")) {
                        cell = new PdfPCell(new Phrase("PAY: Rs." + esh.getPay(), f2));
                        table.addCell(cell);
                    } else {
                        cell = new PdfPCell(new Phrase("", f2));
                        table.addCell(cell);
                    }
                    cell = new PdfPCell(new Phrase(StringUtils.defaultString(esh.getWefChange()), f2));
                    table.addCell(cell);
                    cell = new PdfPCell(phrs);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("ENTRY TAKEN ON " + StringUtils.defaultString(esh.getDoe()), new Font(Font.FontFamily.TIMES_ROMAN, 4, Font.NORMAL)));
                    cell.setColspan(4);
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);
                }
            }

            document.add(table);
            document.add(new Paragraph("\n\n"));

            if (employeeProfile.getGender() != null && employeeProfile.getGender().equals("M")) {
                empGender = "He";
            } else if (employeeProfile.getGender() != null && employeeProfile.getGender().equals("F")) {
                empGender = "She";
            } else {
                empGender = "He/She";
            }

            if (employeeProfile.getAccttype() != null && !employeeProfile.getAccttype().equals("") && (employeeProfile.getAccttype().equalsIgnoreCase("GPF") || employeeProfile.getAccttype().equalsIgnoreCase("TPF"))) {

                if (stopMonthlySubscrip == true) {
                    //document.add(new Paragraph("\n\n\n\n"));
                    /*Paragraph p1 = new Paragraph(empGender + " is going to be retired from Government Services w.e.f. " + employeeProfile.getTxtDos() + " on attaining the age of Superannuation.", bold);
                     p1.setAlignment(Element.ALIGN_CENTER);
                     p1.setSpacingBefore(25f);
                     p1.setSpacingAfter(15f);
                     document.add(p1);*/
                    /* cell = new PdfPCell(new Phrase(empGender + " is going to be retired from Government Services w.e.f. " + employeeProfile.getTxtDos() + " on attaining the age of Superannuation.", bold));
                     cell.setColspan(4);
                     cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                     cell.setBorder(Rectangle.BOTTOM | Rectangle.TOP | Rectangle.LEFT | Rectangle.RIGHT);
                     cell.setFixedHeight(55);
                     table.addCell(cell);*/

                    //document.add(table);
                    /*Paragraph p2 = new Paragraph("Last pay on the date of retirement under normal circumstances :", bold);
                     p2.setAlignment(Element.ALIGN_CENTER);                    
                     p2.setSpacingBefore(25f);
                     p2.setSpacingAfter(15f);                   
                     document.add(p2);*/
                }
            }

            document.newPage();

            table = new PdfPTable(11);
            table.setWidths(new float[]{0.7f, 0.7f, 0.5f, 0.5f, 0.7f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f});
            table.setWidthPercentage(100);

            EmpLeaveAccountPropeties eap = leaveDao.getLeaveAccountDetails(selectedEmpObj.getEmpId(), "", "", "EL", "EOL");

            cell = new PdfPCell(new Phrase(StringUtils.defaultString(eap.getLeaveType()) + " ACCOUNT", f1));
            cell.setColspan(11);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Period of Account", f1));
            cell.setColspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("No of Complete Months", f1));
            cell.setRowspan(2);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Leave Credit in Days", f1));
            cell.setRowspan(2);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Total No. of Leave(EOL) Availed", f1));
            cell.setRowspan(2);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("EL to be Deducted", f1));
            cell.setRowspan(2);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Balance on return from Leave", f1));
            cell.setRowspan(2);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Leave Availed", f1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(3);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Balance Leave", f1));
            cell.setRowspan(2);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("From", f1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("To", f1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("From", f1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("To", f1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Total", f1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            if (eap.getLeaveOBalDate() != null && !eap.getLeaveOBalDate().equals("")) {
                cell = new PdfPCell(new Phrase("OPENING BALANCE ON DATE " + eap.getLeaveOBalDate() + " " + eap.getFnan(), f1));
                cell.setColspan(6);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(eap.getLeaveOBal() + "", f1));
                table.addCell(cell);
                cell = new PdfPCell();
                cell.setColspan(4);
                table.addCell(cell);
            }

            if (eap.getCreditLvList() != null && eap.getCreditLvList().size() > 0) {

                CreditLeaveProperties clp = null;

                for (int i = 0; i < eap.getCreditLvList().size(); i++) {

                    clp = (CreditLeaveProperties) eap.getCreditLvList().get(i);

                    if (clp.getCreditType() != null && clp.getCreditType().equals("U")) {

                        cell = new PdfPCell(new Phrase("UNAVAILED JOINING TIME", f1));
                        cell.setColspan(2);
                        table.addCell(cell);
                        cell = new PdfPCell(new Phrase(clp.getCompMonths(), f1));
                        cell.setRowspan(2);
                        table.addCell(cell);
                        cell = new PdfPCell(new Phrase(clp.getLeaveCredited(), f1));
                        cell.setRowspan(2);
                        table.addCell(cell);
                        cell = new PdfPCell(new Phrase(clp.getTotEOLNumber(), f1));
                        cell.setRowspan(2);
                        table.addCell(cell);
                        cell = new PdfPCell(new Phrase(clp.getLeaveDeduct(), f1));
                        cell.setRowspan(2);
                        table.addCell(cell);
                        cell = new PdfPCell(new Phrase(clp.getCreditBalShow(), f1));
                        cell.setRowspan(2);
                        table.addCell(cell);
                        cell = new PdfPCell();
                        table.addCell(cell);
                        cell = new PdfPCell();
                        table.addCell(cell);
                        cell = new PdfPCell();
                        table.addCell(cell);
                    }

                    if (clp.getCreditType() != null && clp.getCreditType().equals("G")) {

                        cell = new PdfPCell(new Phrase(clp.getFromDate(), f1));
                        table.addCell(cell);
                        cell = new PdfPCell(new Phrase(clp.getToDate(), f1));
                        table.addCell(cell);
                        cell = new PdfPCell(new Phrase(clp.getCompMonths(), f1));
                        table.addCell(cell);
                        cell = new PdfPCell(new Phrase(clp.getLeaveCredited(), f1));
                        table.addCell(cell);
                        cell = new PdfPCell(new Phrase(clp.getTotEOLNumber(), f1));
                        table.addCell(cell);
                        cell = new PdfPCell(new Phrase(clp.getLeaveDeduct(), f1));
                        table.addCell(cell);
                        cell = new PdfPCell(new Phrase(clp.getCreditBalShow(), f1));
                        table.addCell(cell);
                        cell = new PdfPCell();
                        cell.setColspan(4);
                        table.addCell(cell);
                    }

                    if (clp.getAvailedLeave() != null && clp.getAvailedLeave().size() > 0) {
                        EmpLeaveAvailedPropeties empLeaveAvailedProperties = null;
                        for (int j = 0; j < clp.getAvailedLeave().size(); j++) {
                            empLeaveAvailedProperties = (EmpLeaveAvailedPropeties) clp.getAvailedLeave().get(j);

                            cell = new PdfPCell();
                            cell.setColspan(7);
                            table.addCell(cell);
                            cell = new PdfPCell(new Phrase(empLeaveAvailedProperties.getFromdate(), f1));
                            table.addCell(cell);
                            cell = new PdfPCell(new Phrase(empLeaveAvailedProperties.getTodate(), f1));
                            table.addCell(cell);
                            cell = new PdfPCell(new Phrase(empLeaveAvailedProperties.getTotalNoofdays() + "", f1));
                            table.addCell(cell);
                            cell = new PdfPCell(new Phrase(empLeaveAvailedProperties.getBalanceLeave() + "", f1));
                            table.addCell(cell);
                        }
                    }

                    if (clp.getSurrenderedLeave() != null && clp.getSurrenderedLeave().size() > 0) {
                        EmpLeaveSurrenderedPropeties empLeaveSurrenderedPropeties = null;
                        for (int j = 0; j < clp.getSurrenderedLeave().size(); j++) {
                            empLeaveSurrenderedPropeties = (EmpLeaveSurrenderedPropeties) clp.getSurrenderedLeave().get(j);

                            cell = new PdfPCell();
                            cell.setColspan(7);
                            table.addCell(cell);
                            cell = new PdfPCell(new Phrase("SURRENDERED", f1));
                            table.addCell(cell);
                            cell = new PdfPCell(new Phrase("LEAVE", f1));
                            table.addCell(cell);
                            cell = new PdfPCell(new Phrase(empLeaveSurrenderedPropeties.getSurrenderDays() + "", f1));
                            table.addCell(cell);
                            cell = new PdfPCell(new Phrase(empLeaveSurrenderedPropeties.getBalanceLeave() + "", f1));
                            table.addCell(cell);
                        }
                    }
                }
            }
            document.add(table);
            document.add(new Paragraph("\n\n"));

            if (employeeProfile.getGender() != null && employeeProfile.getGender().equals("M")) {
                empGender = "He";
            } else if (employeeProfile.getGender() != null && employeeProfile.getGender().equals("F")) {
                empGender = "She";
            } else {
                empGender = "He/She";
            }

            if (employeeProfile.getAccttype() != null && !employeeProfile.getAccttype().equals("") && (employeeProfile.getAccttype().equalsIgnoreCase("GPF") || employeeProfile.getAccttype().equalsIgnoreCase("TPF"))) {

                if (stopMonthlySubscrip == true) {
                    //document.add(new Paragraph("\n\n\n\n"));
                    /*Paragraph p1 = new Paragraph(empGender + " is going to be retired from Government Services w.e.f. " + employeeProfile.getTxtDos() + " on attaining the age of Superannuation.", bold);
                     p1.setAlignment(Element.ALIGN_CENTER);
                     p1.setSpacingBefore(25f);
                     p1.setSpacingAfter(15f);
                     document.add(p1);*/
                    /*cell = new PdfPCell(new Phrase(empGender + " is going to be retired from Government Services w.e.f. " + employeeProfile.getTxtDos() + " on attaining the age of Superannuation.", bold));
                     cell.setColspan(4);
                     cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                     cell.setBorder(Rectangle.BOTTOM | Rectangle.TOP | Rectangle.LEFT | Rectangle.RIGHT);
                     cell.setFixedHeight(55);
                     table.addCell(cell);*/

                    //document.add(table);
                    /*Paragraph p2 = new Paragraph("Last pay on the date of retirement under normal circumstances :", bold);
                     p2.setAlignment(Element.ALIGN_CENTER);
                    
                     p2.setSpacingBefore(25f);
                     p2.setSpacingAfter(15f);              
                     document.add(p2);*/
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }

    @RequestMapping(value = "MyServicebookPDF.htm")
    public void MyServicebookPDF(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub) {

        response.setContentType("application/pdf");
        Document document = new Document(PageSize.A4);

        PdfWriter writer = null;
        try {
            String logopath = context.getInitParameter("SBPath");
            String photopath = context.getInitParameter("PhotoPath");

            response.setHeader("Content-Disposition", "attachment; filename=SERVICE_BOOK_" + lub.getLoginempid() + ".pdf");

            writer = PdfWriter.getInstance(document, response.getOutputStream());
            writer.setPageEvent(new ServiceBookHeaderFooter(lub.getLoginempid(), lub.getLogingpfno(), lub.getLoginname()));

            document.open();

            //EmpServiceBook esb = new EmpServiceBook();
            EmpServiceBook esb = serviceBookDao.getSHReport(lub.getLoginempid());
            Date todaysDate = new Date();
            DateFormat df3 = new SimpleDateFormat("dd-MMM-yyyy");
            String str3 = df3.format(todaysDate);
            //  esb.setServerDate(str3);
            Employee employeeProfile = employeeDAO.getEmployeeProfile(lub.getLoginempid());
            Education[] educations = employeeDAO.getEmployeeEducation(lub.getLoginempid(), str3);
            FamilyRelation[] familyRel = employeeDAO.getEmployeeFamily(lub.getLoginempid());
            //Address[] address = employeeDAO.getAddress(lub.getLoginempid());
            Address address = employeeDAO.getAddressForServiceBook(lub.getLoginempid());
            List identity = employeeDAO.getIdentityList(lub.getLoginempid());
            List nomineedetaillist = employeeDAO.getEmployeeNominee(lub.getLoginempid());

            PdfPTable table = null;
            PdfPCell cell = null;

            Font f1 = new Font();
            f1.setSize(8);
            f1.setFamily("Times New Roman");

            Font f2 = new Font();
            f2.setSize(8);
            f2.setFamily("Times New Roman");

            table = new PdfPTable(1);
            table.setWidths(new int[]{2});
            table.setWidthPercentage(100);

            //String url = filePath + paf.getApplicantempid() + ".jpg";
            String url = logopath + "/odgovt.gif";
            File f = null;
            f = new File(url);
            Image img = null;
            if (f.exists()) {
                img = Image.getInstance(url);
            }

            if (img != null) {
                img.scaleToFit(100f, 80f);
                cell = new PdfPCell(img);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
            } else {
                cell = new PdfPCell();
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
            }

            cell = new PdfPCell(new Phrase("HUMAN RESOURCES MANAGEMENT SYSTEM", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
            cell.setFixedHeight(20);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Government of Odisha", new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD)));
            cell.setFixedHeight(20);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setFixedHeight(60);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("SERVICE BOOK", new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("OF", new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            url = photopath + lub.getLoginempid() + ".jpg";
            f = new File(url);
            img = null;
            if (f.exists()) {
                img = Image.getInstance(url);
            }

            if (img != null) {
                img.scaleToFit(100f, 80f);
                img.setBorder(Rectangle.BOX);
                img.setBorderColor(BaseColor.BLACK);
                img.setBorderWidth(1f);
                cell = new PdfPCell(img);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
            } else {
                url = logopath + "NoEmployee.png";
                f = new File(url);
                img = null;
                if (f.exists()) {
                    img = Image.getInstance(url);
                    img.scaleToFit(100f, 80f);
                    img.setBorder(Rectangle.BOX);
                    img.setBorderColor(BaseColor.BLACK);
                    img.setBorderWidth(1f);
                }

                cell = new PdfPCell(img);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
            }

            cell = new PdfPCell(new Phrase(employeeProfile.getEmpName(), new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(employeeProfile.getEmpCadre(), f1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(employeeProfile.getEmpAllotmentYear(), f1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            if (employeeProfile.getCadreId() != null && !employeeProfile.getCadreId().equals("")) {
                cell = new PdfPCell(new Phrase(employeeProfile.getCadreId(), f1));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
            }

            cell = new PdfPCell(new Phrase("HRMS ID: " + employeeProfile.getEmpid(), f1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(employeeProfile.getAccttype() + " NO: " + employeeProfile.getGpfno(), f1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            //if (employeeProfile.getGisName() != null && !employeeProfile.getGisName().equals("")) {
            cell = new PdfPCell(new Phrase("GIS NO: " + employeeProfile.getGisNo(), f1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            //}
            /*if (employeeProfile.getGisNo() != null && !employeeProfile.getGisNo().equals("")) {
             cell = new PdfPCell(new Phrase(employeeProfile.getGisNo(), f1));
             cell.setHorizontalAlignment(Element.ALIGN_CENTER);
             cell.setBorder(Rectangle.NO_BORDER);
             table.addCell(cell);
             }*/

            cell = new PdfPCell();
            cell.setFixedHeight(60);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setFixedHeight(60);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setFixedHeight(60);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setFixedHeight(60);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            url = logopath + "/file.JPG";
            f = new File(url);
            img = null;
            if (f.exists()) {
                img = Image.getInstance(url);
            }

            if (img != null) {
                img.scaleToFit(100f, 80f);
                cell = new PdfPCell(img);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
            } else {
                cell = new PdfPCell();
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
            }

            cell = new PdfPCell(new Phrase("Developed By", f1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Centre for Modernizing Government Initiative(CMGI)", f1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            //document.add(table);
            document.newPage();

            url = logopath + "/" + lub.getLoginempid() + ".jpg";
            f = new File(url);
            img = null;
            if (f.exists()) {
                img = Image.getInstance(url);
            }

            if (img != null) {
                img.scaleToFit(600f, 580f);
                img.setBorder(Rectangle.BOX);
                img.setBorderColor(BaseColor.BLACK);
                img.setBorderWidth(1f);
                cell = new PdfPCell(img);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
            } else {
                url = logopath + "/SB1stPage.png";
                f = new File(url);
                img = null;
                if (f.exists()) {
                    img = Image.getInstance(url);
                }
                if (img != null) {
                    img.scaleToFit(600f, 5800f);
                    img.setBorder(Rectangle.BOX);
                    img.setBorderColor(BaseColor.BLACK);
                    img.setBorderWidth(1f);

                    cell = new PdfPCell(img);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);
                }
            }

            document.add(table);
            document.newPage();

            table = new PdfPTable(4);
            table.setWidths(new int[]{1, 1, 1, 1});
            table.setWidthPercentage(100);

            cell = new PdfPCell(new Phrase("NAME:", f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(employeeProfile.getEmpName(), f1));
            cell.setColspan(3);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(employeeProfile.getAccttype() + " NO:", f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(employeeProfile.getGpfno(), f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("HRMS ID:", f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(employeeProfile.getEmpid(), f1));
            table.addCell(cell);

            String presentAddressDetails = "";
            String permenentAddressDetails = "";

            List addressList = Arrays.asList(address);
            if (addressList != null && addressList.size() > 0) {
                Address addr = null;
                for (int i = 0; i < addressList.size(); i++) {
                    addr = (Address) addressList.get(i);
                    if (addr.getAddressType() != null && addr.getAddressType().equals("PRESENT")) {
                        presentAddressDetails = addr.getAddress();
                    }
                    if (addr.getAddressType() != null && addr.getAddressType().equals("PERMANENT")) {
                        permenentAddressDetails = addr.getAddress();
                    }
                }
            }

            cell = new PdfPCell(new Phrase("PRESENT ADDRESS:\n(RESIDENCE)", f1));
            cell.setFixedHeight(20);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(StringUtils.defaultString(address.getPresentAddress()), f2));
            cell.setFixedHeight(20);
            cell.setColspan(3);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("DATE OF BIRTH BY CHRISTIAN ERA AS NEARLY AS CAN BE ASCERTAINED :", f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(StringUtils.defaultString(employeeProfile.getDob()) + "(" + StringUtils.defaultString(employeeProfile.getDobText()) + ")", f2));
            cell.setColspan(3);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("EMPLOYEE EDUCATIONAL DETAILS:", f1));
            cell.setColspan(4);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);

            List educationList = Arrays.asList(educations);

            if (educationList != null && educationList.size() > 0) {
                Education education = null;
                for (int i = 0; i < educationList.size(); i++) {
                    education = (Education) educationList.get(i);

                    cell = new PdfPCell(new Phrase(education.getQualification(), f1));
                    cell.setColspan(4);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("Year of Passing: " + education.getYearofpass(), f1));
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase("Faculty: " + education.getFaculty(), f1));
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase("Degree\nCertificate:", f1));
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(education.getDegree(), f1));
                    table.addCell(cell);
                }
            }

            cell = new PdfPCell(new Phrase("HEIGHT(in cm):", f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(StringUtils.defaultString(employeeProfile.getHeight() + ""), f2));
            cell.setColspan(3);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("PERSONAL\nIDENTIFICATION MARK :", f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(StringUtils.defaultString(employeeProfile.getIdmark()), f2));
            cell.setColspan(3);
            table.addCell(cell);

            String fatherDetails = "";

            List familyList = Arrays.asList(familyRel);
            if (familyList != null && familyList.size() > 0) {
                FamilyRelation familyRelation = null;
                for (int i = 0; i < familyList.size(); i++) {
                    familyRelation = (FamilyRelation) familyList.get(i);
                    if (familyRelation.getRelation() != null && familyRelation.getRelation().equals("FATHER")) {
                        fatherDetails = StringUtils.defaultString(familyRelation.getInitials()) + " " + StringUtils.defaultString(familyRelation.getFname()) + " " + StringUtils.defaultString(familyRelation.getMname()) + " " + StringUtils.defaultString(familyRelation.getLname());
                    }
                }
            }

            cell = new PdfPCell(new Phrase("FATHER'S NAME\nAND RESIDENCE :", f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(fatherDetails + "\n" + address.getPermanentAddress(), f2));
            cell.setColspan(3);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("DATE OF ENTRY IN GOVERMENT SERVICE :", f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(employeeProfile.getDoeGov() + "(" + StringUtils.defaultString(employeeProfile.getEntryGovDateText()) + ")\n" + StringUtils.defaultString(employeeProfile.getEntryGovDateTime()), f2));
            cell.setColspan(3);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("DATE OF ENTRY IN THE SERVICE FOR WHICH SERVICE BOOK CREATED:", f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(employeeProfile.getJoindategoo() + "(" + StringUtils.defaultString(employeeProfile.getJoinDateText()) + ")", f2));
            cell.setColspan(3);
            table.addCell(cell);

            SelectOption so = serviceBookDao.getInitialJoiningData(lub.getLoginempid());

            cell = new PdfPCell(new Phrase("INITIAL JOINING OFFICE:", f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(StringUtils.defaultString(so.getLabel()), f2));
            cell.setColspan(3);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("INITIAL JOINING POST:", f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(StringUtils.defaultString(so.getValue()), f2));
            cell.setColspan(3);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("DECLARATION OF HOME TOWN:", f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(employeeProfile.getHomeTown(), f2));
            cell.setColspan(3);
            table.addCell(cell);

            document.newPage();

            cell = new PdfPCell(new Phrase("DATE OF SUPERANNUATION:", f1));
            cell.setColspan(2);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(employeeProfile.getDor(), f2));
            cell.setColspan(2);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("CATEGORY:", f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(employeeProfile.getCategory(), f2));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("MARITAL STATUS:", f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(employeeProfile.getMaritalStatus(), f2));
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("CELL PHONE:", f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(employeeProfile.getMobile(), f2));
            cell.setColspan(3);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("IDENTITY OF THE EMPLOYEE:", f1));
            cell.setFixedHeight(20);
            cell.setColspan(4);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);

            PdfPTable innertable = new PdfPTable(5);
            innertable.setWidths(new int[]{1, 1, 1, 1, 1});
            innertable.setWidthPercentage(100);

            PdfPCell innercell = null;

            innercell = new PdfPCell(new Phrase("Type of Identification", f1));
            innertable.addCell(innercell);
            innercell = new PdfPCell(new Phrase("No.", f1));
            innertable.addCell(innercell);
            innercell = new PdfPCell(new Phrase("Place of Issue", f1));
            innertable.addCell(innercell);
            innercell = new PdfPCell(new Phrase("Date of Issue", f1));
            innertable.addCell(innercell);
            innercell = new PdfPCell(new Phrase("Date of Expiry", f1));
            innertable.addCell(innercell);

            cell = new PdfPCell(innertable);
            cell.setFixedHeight(20);
            cell.setColspan(4);
            table.addCell(cell);

            //List identityList = Arrays.asList(identity);
            if (identity != null && identity.size() > 0) {
                IdentityInfo idInfo = null;
                for (int i = 0; i < identity.size(); i++) {
                    idInfo = (IdentityInfo) identity.get(i);

                    innertable = new PdfPTable(5);
                    innertable.setWidths(new int[]{1, 1, 1, 1, 1});
                    innertable.setWidthPercentage(100);

                    innercell = new PdfPCell(new Phrase(idInfo.getIdentityDesc(), f2));
                    innertable.addCell(innercell);
                    innercell = new PdfPCell(new Phrase(idInfo.getIdentityNo(), f2));
                    innertable.addCell(innercell);
                    innercell = new PdfPCell(new Phrase(idInfo.getPlaceOfIssue(), f2));
                    innertable.addCell(innercell);
                    innercell = new PdfPCell(new Phrase(idInfo.getIssueDate(), f2));
                    innertable.addCell(innercell);
                    innercell = new PdfPCell(new Phrase(idInfo.getExpiryDate(), f2));
                    innertable.addCell(innercell);

                    cell = new PdfPCell(innertable);
                    cell.setFixedHeight(20);
                    cell.setColspan(4);
                    table.addCell(cell);
                }
            }

            cell = new PdfPCell(new Phrase("FAMILY OF THE EMPLOYEE:", f1));
            cell.setFixedHeight(20);
            cell.setColspan(4);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);

            innertable = new PdfPTable(6);
            innertable.setWidths(new int[]{1, 1, 1, 1, 1, 1});
            innertable.setWidthPercentage(100);

            innercell = new PdfPCell(new Phrase("Relation Type", f1));
            innertable.addCell(innercell);
            innercell = new PdfPCell(new Phrase("Name", f1));
            innertable.addCell(innercell);
            innercell = new PdfPCell(new Phrase("Is Alive?", f1));
            innertable.addCell(innercell);
            innercell = new PdfPCell(new Phrase("DOB", f1));
            innertable.addCell(innercell);
            innercell = new PdfPCell(new Phrase("Marital Status", f1));
            innertable.addCell(innercell);
            innercell = new PdfPCell(new Phrase("Age", f1));
            innertable.addCell(innercell);

            cell = new PdfPCell(innertable);
            cell.setFixedHeight(20);
            cell.setColspan(4);
            table.addCell(cell);

            if (familyList != null && familyList.size() > 0) {
                FamilyRelation familyRelation = null;
                for (int i = 0; i < familyList.size(); i++) {
                    familyRelation = (FamilyRelation) familyList.get(i);

                    innertable = new PdfPTable(6);
                    innertable.setWidths(new int[]{1, 1, 1, 1, 1, 1});
                    innertable.setWidthPercentage(100);

                    innercell = new PdfPCell(new Phrase(familyRelation.getRelation(), f2));
                    innertable.addCell(innercell);
                    innercell = new PdfPCell(new Phrase(StringUtils.defaultString(familyRelation.getInitials()) + " " + StringUtils.defaultString(familyRelation.getFname()) + " " + StringUtils.defaultString(familyRelation.getMname()) + " " + StringUtils.defaultString(familyRelation.getLname()), f2));
                    innertable.addCell(innercell);
                    innercell = new PdfPCell(new Phrase(familyRelation.getIfalive(), f2));
                    innertable.addCell(innercell);
                    innercell = new PdfPCell(new Phrase(familyRelation.getDob(), f2));
                    innertable.addCell(innercell);
                    innercell = new PdfPCell(new Phrase(familyRelation.getMarital(), f2));
                    innertable.addCell(innercell);
                    innercell = new PdfPCell(new Phrase(familyRelation.getAge(), f2));
                    innertable.addCell(innercell);

                    cell = new PdfPCell(innertable);
                    cell.setFixedHeight(30);
                    cell.setColspan(4);
                    table.addCell(cell);
                }
            }

            cell = new PdfPCell(new Phrase("NOMINEE:", f1));
            cell.setFixedHeight(20);
            cell.setColspan(4);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);

            innertable = new PdfPTable(6);
            innertable.setWidths(new int[]{1, 1, 1, 1, 1, 1});
            innertable.setWidthPercentage(100);

            innercell = new PdfPCell(new Phrase("Relation Type", f1));
            innertable.addCell(innercell);
            innercell = new PdfPCell(new Phrase("Name", f1));
            innertable.addCell(innercell);
            innercell = new PdfPCell(new Phrase("Is Alive?", f1));
            innertable.addCell(innercell);
            innercell = new PdfPCell(new Phrase("DOB", f1));
            innertable.addCell(innercell);
            innercell = new PdfPCell(new Phrase("Marital Status", f1));
            innertable.addCell(innercell);
            innercell = new PdfPCell(new Phrase("Age", f1));
            innertable.addCell(innercell);

            cell = new PdfPCell(innertable);
            cell.setFixedHeight(20);
            cell.setColspan(4);
            table.addCell(cell);

            innertable = new PdfPTable(6);
            innertable.setWidths(new int[]{1, 1, 1, 1, 1, 1});
            innertable.setWidthPercentage(100);

            if (nomineedetaillist != null && nomineedetaillist.size() > 0) {
                FamilyRelation familyRelation = null;
                for (int i = 0; i < nomineedetaillist.size(); i++) {
                    familyRelation = (FamilyRelation) nomineedetaillist.get(i);
                    innercell = new PdfPCell(new Phrase(familyRelation.getRelation(), f2));
                    innertable.addCell(innercell);
                    innercell = new PdfPCell(new Phrase(StringUtils.defaultString(familyRelation.getInitials()) + " " + StringUtils.defaultString(familyRelation.getFname()) + " " + StringUtils.defaultString(familyRelation.getMname()) + " " + StringUtils.defaultString(familyRelation.getLname()), f2));
                    innertable.addCell(innercell);
                    innercell = new PdfPCell(new Phrase(familyRelation.getIfalive(), f2));
                    innertable.addCell(innercell);
                    innercell = new PdfPCell(new Phrase(familyRelation.getDob(), f2));
                    innertable.addCell(innercell);
                    innercell = new PdfPCell(new Phrase(familyRelation.getMarital(), f2));
                    innertable.addCell(innercell);
                    innercell = new PdfPCell(new Phrase(familyRelation.getAge(), f2));
                    innertable.addCell(innercell);
                }
            }

            cell = new PdfPCell(innertable);
            cell.setFixedHeight(40);
            cell.setColspan(4);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Is Employed under any Reservation Category?", f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(employeeProfile.getIfReservation(), f2));
            cell.setColspan(3);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Reservation Category Under Which Employed", f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(StringUtils.defaultString(employeeProfile.getCategory()), f2));
            cell.setColspan(3);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Is Employed under Rehabilation Assistance Scheme?", f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(StringUtils.defaultString(employeeProfile.getIfRehabiltation()), f2));
            cell.setColspan(3);
            table.addCell(cell);

            document.add(table);
            document.newPage();

            table = new PdfPTable(4);
            table.setWidths(new float[]{1, 0.5f, 0.5f, 2});
            table.setWidthPercentage(100);

            cell = new PdfPCell(new Phrase("Post/Cadre/Scale of Pay", f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Pay", f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("WEF", f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Entry in the Service Book", f1));
            table.addCell(cell);

            if (esb.getEmpsbrecord() != null && esb.getEmpsbrecord().size() > 0) {
                EmpServiceHistory esh = null;
                for (int i = 0; i < esb.getEmpsbrecord().size(); i++) {
                    esh = (EmpServiceHistory) esb.getEmpsbrecord().get(i);

                    int slno = i + 1;

                    Phrase phrs = new Phrase();
                    Chunk c1 = new Chunk(StringUtils.defaultString(esh.getCategory()), new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL | Font.UNDERLINE));
                    Chunk c2 = new Chunk("\n\n" + StringUtils.defaultString(esh.getSbdescription()), f2);
                    Chunk c3 = new Chunk("\n\nNote : ", f2);
                    Chunk c4 = new Chunk(StringUtils.defaultString(esh.getModuleNote()), f2);
                    phrs.add(c1);
                    phrs.add(c2);
                    phrs.add(c3);
                    phrs.add(c4);

                    Phrase col1stphrs = new Phrase();
                    c1 = new Chunk(slno + "", new Font(Font.FontFamily.TIMES_ROMAN, 4, Font.NORMAL));
                    col1stphrs.add(c1);
                    if (esh.getSpn() != null && !esh.getSpn().equals("")) {
                        c2 = new Chunk("\n" + esh.getSpn(), f2);
                        col1stphrs.add(c2);
                    }
                    if (esh.getCadre() != null && !esh.getCadre().equals("")) {
                        c3 = new Chunk("\n" + esh.getCadre(), f2);
                        col1stphrs.add(c3);
                    }
                    if (esh.getPayscale() != null && !esh.getPayscale().equals("")) {
                        c4 = new Chunk("\n" + esh.getPayscale(), f2);
                        col1stphrs.add(c4);
                    }

                    //cell = new PdfPCell(new Phrase(slno + "", new Font(Font.FontFamily.TIMES_ROMAN, 4, Font.NORMAL)));
                    cell = new PdfPCell(col1stphrs);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase("", f2));
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(StringUtils.defaultString(esh.getWefChange()), f2));
                    table.addCell(cell);
                    cell = new PdfPCell(phrs);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("ENTRY TAKEN ON " + StringUtils.defaultString(esh.getDoe()), new Font(Font.FontFamily.TIMES_ROMAN, 4, Font.NORMAL)));
                    cell.setColspan(4);
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);
                }
            }

            document.add(table);
            document.newPage();

            table = new PdfPTable(11);
            table.setWidths(new float[]{0.7f, 0.7f, 0.5f, 0.5f, 0.7f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f});
            table.setWidthPercentage(100);

            EmpLeaveAccountPropeties eap = leaveDao.getLeaveAccountDetails(lub.getLoginempid(), "", "", "EL", "EOL");

            cell = new PdfPCell(new Phrase(StringUtils.defaultString(eap.getLeaveType()) + " ACCOUNT", f1));
            cell.setColspan(11);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Period of Account", f1));
            cell.setColspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("No of Complete Months", f1));
            cell.setRowspan(2);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Leave Credit in Days", f1));
            cell.setRowspan(2);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Total No. of Leave(EOL) Availed", f1));
            cell.setRowspan(2);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("EL to be Deducted", f1));
            cell.setRowspan(2);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Balance on return from Leave", f1));
            cell.setRowspan(2);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Leave Availed", f1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(3);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Balance Leave", f1));
            cell.setRowspan(2);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("From", f1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("To", f1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("From", f1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("To", f1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Total", f1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            if (eap.getLeaveOBalDate() != null && !eap.getLeaveOBalDate().equals("")) {
                cell = new PdfPCell(new Phrase("OPENING BALANCE ON DATE " + eap.getLeaveOBalDate() + " " + eap.getFnan(), f1));
                cell.setColspan(6);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(eap.getLeaveOBal() + "", f1));
                table.addCell(cell);
                cell = new PdfPCell();
                cell.setColspan(4);
                table.addCell(cell);
            }

            if (eap.getCreditLvList() != null && eap.getCreditLvList().size() > 0) {

                CreditLeaveProperties clp = null;

                for (int i = 0; i < eap.getCreditLvList().size(); i++) {

                    clp = (CreditLeaveProperties) eap.getCreditLvList().get(i);

                    if (clp.getCreditType() != null && clp.getCreditType().equals("U")) {

                        cell = new PdfPCell(new Phrase("UNAVAILED JOINING TIME", f1));
                        cell.setColspan(2);
                        table.addCell(cell);
                        cell = new PdfPCell(new Phrase(clp.getCompMonths(), f1));
                        cell.setRowspan(2);
                        table.addCell(cell);
                        cell = new PdfPCell(new Phrase(clp.getLeaveCredited(), f1));
                        cell.setRowspan(2);
                        table.addCell(cell);
                        cell = new PdfPCell(new Phrase(clp.getTotEOLNumber(), f1));
                        cell.setRowspan(2);
                        table.addCell(cell);
                        cell = new PdfPCell(new Phrase(clp.getLeaveDeduct(), f1));
                        cell.setRowspan(2);
                        table.addCell(cell);
                        cell = new PdfPCell(new Phrase(clp.getCreditBalShow(), f1));
                        cell.setRowspan(2);
                        table.addCell(cell);
                        cell = new PdfPCell();
                        table.addCell(cell);
                        cell = new PdfPCell();
                        table.addCell(cell);
                        cell = new PdfPCell();
                        table.addCell(cell);
                    }

                    if (clp.getCreditType() != null && clp.getCreditType().equals("G")) {

                        cell = new PdfPCell(new Phrase(clp.getFromDate(), f1));
                        table.addCell(cell);
                        cell = new PdfPCell(new Phrase(clp.getToDate(), f1));
                        table.addCell(cell);
                        cell = new PdfPCell(new Phrase(clp.getCompMonths(), f1));
                        table.addCell(cell);
                        cell = new PdfPCell(new Phrase(clp.getLeaveCredited(), f1));
                        table.addCell(cell);
                        cell = new PdfPCell(new Phrase(clp.getTotEOLNumber(), f1));
                        table.addCell(cell);
                        cell = new PdfPCell(new Phrase(clp.getLeaveDeduct(), f1));
                        table.addCell(cell);
                        cell = new PdfPCell(new Phrase(clp.getCreditBalShow(), f1));
                        table.addCell(cell);
                        cell = new PdfPCell();
                        cell.setColspan(4);
                        table.addCell(cell);
                    }

                    if (clp.getAvailedLeave() != null && clp.getAvailedLeave().size() > 0) {
                        EmpLeaveAvailedPropeties empLeaveAvailedProperties = null;
                        for (int j = 0; j < clp.getAvailedLeave().size(); j++) {
                            empLeaveAvailedProperties = (EmpLeaveAvailedPropeties) clp.getAvailedLeave().get(j);

                            cell = new PdfPCell();
                            cell.setColspan(7);
                            table.addCell(cell);
                            cell = new PdfPCell(new Phrase(empLeaveAvailedProperties.getFromdate(), f1));
                            table.addCell(cell);
                            cell = new PdfPCell(new Phrase(empLeaveAvailedProperties.getTodate(), f1));
                            table.addCell(cell);
                            cell = new PdfPCell(new Phrase(empLeaveAvailedProperties.getTotalNoofdays() + "", f1));
                            table.addCell(cell);
                            cell = new PdfPCell(new Phrase(empLeaveAvailedProperties.getBalanceLeave() + "", f1));
                            table.addCell(cell);
                        }
                    }

                    if (clp.getSurrenderedLeave() != null && clp.getSurrenderedLeave().size() > 0) {
                        EmpLeaveSurrenderedPropeties empLeaveSurrenderedPropeties = null;
                        for (int j = 0; j < clp.getSurrenderedLeave().size(); j++) {
                            empLeaveSurrenderedPropeties = (EmpLeaveSurrenderedPropeties) clp.getSurrenderedLeave().get(j);

                            cell = new PdfPCell();
                            cell.setColspan(7);
                            table.addCell(cell);
                            cell = new PdfPCell(new Phrase("SURRENDERED", f1));
                            table.addCell(cell);
                            cell = new PdfPCell(new Phrase("LEAVE", f1));
                            table.addCell(cell);
                            cell = new PdfPCell(new Phrase(empLeaveSurrenderedPropeties.getSurrenderDays() + "", f1));
                            table.addCell(cell);
                            cell = new PdfPCell(new Phrase(empLeaveSurrenderedPropeties.getBalanceLeave() + "", f1));
                            table.addCell(cell);
                        }
                    }
                }
            }

            document.add(table);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }

    @RequestMapping(value = "eServiceBook.htm")
    public void eServiceBook(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpObj") Users selectedEmpObj) {

        response.setContentType("application/pdf");
        Document document = new Document(PageSize.A4);

        PdfWriter writer = null;

        try {
            response.setHeader("Content-Disposition", "attachment; filename=eSERVICE_BOOK_" + selectedEmpObj.getEmpId() + ".pdf");

            writer = PdfWriter.getInstance(document, response.getOutputStream());
            writer.setPageEvent(new ServiceBookHeaderFooter(selectedEmpObj.getEmpId(), selectedEmpObj.getGpfno(), selectedEmpObj.getFullName()));

            document.open();

            Date todaysDate = new Date();
            DateFormat df3 = new SimpleDateFormat("dd-MMM-yyyy");
            String str3 = df3.format(todaysDate);

            Employee employeeProfile = employeeDAO.getEmployeeProfile(selectedEmpObj.getEmpId());
            Education[] educations = employeeDAO.getEmployeeEducation(selectedEmpObj.getEmpId(), str3);
            FamilyRelation[] familyRel = employeeDAO.getEmployeeFamily(selectedEmpObj.getEmpId());
            Address[] address = employeeDAO.getAddress(selectedEmpObj.getEmpId());
            List identity = employeeDAO.getIdentityList(selectedEmpObj.getEmpId());

            PdfPTable table = null;
            PdfPCell cell = null;

            Font f1 = new Font();
            f1.setSize(8);
            f1.setFamily("Times New Roman");

            table = new PdfPTable(3);
            table.setWidths(new float[]{0.5f, 0.7f, 2});
            table.setWidthPercentage(100);

            cell = new PdfPCell(new Phrase("e-Service Book", new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD | Font.UNDERLINE)));
            cell.setColspan(3);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Brief description of Service of " + selectedEmpObj.getFullName(), new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD | Font.NORMAL)));
            cell.setColspan(3);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Sl No.", new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD | Font.NORMAL)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Description", new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD | Font.NORMAL)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Details", new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD | Font.NORMAL)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            int slno = 0;
            slno = slno + 1;

            cell = new PdfPCell(new Phrase(slno + "", f1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("HRMS Id. No.", f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(employeeProfile.getEmpid(), f1));
            table.addCell(cell);

            slno = slno + 1;

            cell = new PdfPCell(new Phrase(slno + "", f1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Name", f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(employeeProfile.getEmpName(), f1));
            table.addCell(cell);

            String fatherDetails = "";

            List familyList = Arrays.asList(familyRel);
            if (familyList != null && familyList.size() > 0) {
                FamilyRelation familyRelation = null;
                for (int i = 0; i < familyList.size(); i++) {
                    familyRelation = (FamilyRelation) familyList.get(i);
                    if (familyRelation.getRelation() != null && familyRelation.getRelation().equals("FATHER")) {
                        fatherDetails = StringUtils.defaultString(familyRelation.getInitials()) + " " + StringUtils.defaultString(familyRelation.getFname()) + " " + StringUtils.defaultString(familyRelation.getMname()) + " " + StringUtils.defaultString(familyRelation.getLname());
                    }
                }
            }

            slno = slno + 1;

            cell = new PdfPCell(new Phrase(slno + "", f1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Father's Name", f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(fatherDetails, f1));
            table.addCell(cell);

            slno = slno + 1;

            cell = new PdfPCell(new Phrase(slno + "", f1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Date of Birth", f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(employeeProfile.getDob(), f1));
            table.addCell(cell);

            slno = slno + 1;

            cell = new PdfPCell(new Phrase(slno + "", f1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Educational Qualification", f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("", f1));
            table.addCell(cell);

            slno = slno + 1;

            cell = new PdfPCell(new Phrase(slno + "", f1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Other Qualification", f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("", f1));
            table.addCell(cell);

            slno = slno + 1;

            cell = new PdfPCell(new Phrase(slno + "", f1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Date of Joining in the Service", f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(employeeProfile.getDoeGov(), f1));
            table.addCell(cell);

            slno = slno + 1;

            cell = new PdfPCell(new Phrase(slno + "", f1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Office in which employed", f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(employeeProfile.getOffice(), f1));
            table.addCell(cell);

            slno = slno + 1;

            cell = new PdfPCell(new Phrase(slno + "", f1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Post Held", f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(employeeProfile.getSpn(), f1));
            table.addCell(cell);

            slno = slno + 1;

            cell = new PdfPCell(new Phrase(slno + "", f1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Scale of Pay", f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(employeeProfile.getPayScale(), f1));
            table.addCell(cell);

            slno = slno + 1;

            cell = new PdfPCell(new Phrase(slno + "", f1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("GPF A/c No.", f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(employeeProfile.getGpfno(), f1));
            table.addCell(cell);

            slno = slno + 1;

            cell = new PdfPCell(new Phrase(slno + "", f1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Mobile No.", f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(employeeProfile.getMobile(), f1));
            table.addCell(cell);

            slno = slno + 1;

            cell = new PdfPCell(new Phrase(slno + "", f1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Email Id", f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(employeeProfile.getEmail(), f1));
            table.addCell(cell);

            document.add(table);
            document.newPage();

            table = new PdfPTable(1);
            table.setWidths(new int[]{1});
            table.setWidthPercentage(100);

            cell = new PdfPCell(new Phrase("Annexure-A", new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD | Font.UNDERLINE)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Statement of service verification", new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD | Font.UNDERLINE)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            document.add(table);

            table = new PdfPTable(7);
            table.setWidths(new float[]{0.3f, 0.7f, 0.7f, 1, 2, 0.5f, 1});
            table.setWidthPercentage(100);

            cell = new PdfPCell(new Phrase("Sl. No.", new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD | Font.NORMAL)));
            cell.setRowspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Chronology of posting", new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD | Font.NORMAL)));
            cell.setColspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Name of the post", new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD | Font.NORMAL)));
            cell.setRowspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Office where posted", new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD | Font.NORMAL)));
            cell.setRowspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Whether service verified or not", new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD | Font.NORMAL)));
            cell.setRowspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Remarks/Reasons for non-verification of service", new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD | Font.NORMAL)));
            cell.setRowspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("From date", new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD | Font.NORMAL)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("To date", new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD | Font.NORMAL)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            Font f2 = new Font();
            f2.setSize(7);
            f2.setFamily("Times New Roman");

            slno = 0;

            List annexureAList = serviceBookDao.getServiceBookAnnexureAData(selectedEmpObj.getEmpId());

            if (annexureAList != null && annexureAList.size() > 0) {
                EmpServiceHistory esh = null;
                for (int i = 0; i < annexureAList.size(); i++) {
                    esh = (EmpServiceHistory) annexureAList.get(i);

                    slno = slno + 1;

                    String sv = "Not Verified";
                    if (esh.getIfSV() != null && !esh.getIfSV().equals("")) {
                        sv = "Verified";
                    }
                    cell = new PdfPCell(new Phrase(slno + "", f2));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(esh.getFrmDate(), f2));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(esh.getToDate(), f2));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(esh.getSpn(), f2));
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(esh.getOffen(), f2));
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(sv, f2));
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(esh.getRemarksForNoSV(), f2));
                    table.addCell(cell);
                }
            }
            document.add(table);
            document.newPage();

            table = new PdfPTable(1);
            table.setWidths(new int[]{1});
            table.setWidthPercentage(100);

            cell = new PdfPCell(new Phrase("Annexure-B", new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD | Font.UNDERLINE)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Statement of Pay Fixation", new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD | Font.UNDERLINE)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            document.add(table);

            table = new PdfPTable(9);
            table.setWidths(new float[]{0.3f, 1, 0.9f, 1.5f, 2, 1, 0.8f, 0.8f, 0.9f});
            table.setWidthPercentage(100);

            cell = new PdfPCell(new Phrase("Sl. No", new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD | Font.NORMAL)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Events", new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD | Font.NORMAL)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Date", new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD | Font.NORMAL)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Office", new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD | Font.NORMAL)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Post Held", new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD | Font.NORMAL)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Scale of Pay/Pay Band/Pay Level and Cell No.", new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD | Font.NORMAL)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Pay fixed\n(Pay+GP)", new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD | Font.NORMAL)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Date of Next Increment(DNI)", new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD | Font.NORMAL)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Remarks", new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD | Font.NORMAL)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            slno = 0;

            List annexureBList = serviceBookDao.getServiceBookAnnexureBData(selectedEmpObj.getEmpId());
            if (annexureBList != null && annexureBList.size() > 0) {
                EmpServiceHistory esh = null;
                for (int i = 0; i < annexureBList.size(); i++) {
                    esh = (EmpServiceHistory) annexureBList.get(i);

                    slno = slno + 1;

                    cell = new PdfPCell(new Phrase(slno + "", f2));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(esh.getTabname(), f2));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(esh.getWefChange(), f2));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(esh.getOffen(), f2));
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(esh.getSpn(), f2));
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(esh.getPayscale(), f2));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(esh.getPay(), f2));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(esh.getNextwefChange(), f2));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(esh.getRemarksForNoSV(), f2));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                }
            }
            document.add(table);
            document.newPage();

            table = new PdfPTable(1);
            table.setWidths(new int[]{1});
            table.setWidthPercentage(100);

            cell = new PdfPCell(new Phrase("Annexure-C", new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD | Font.UNDERLINE)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Statement of Non-qualifying service", new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD | Font.UNDERLINE)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            document.add(table);
            document.newPage();

            table = new PdfPTable(1);
            table.setWidths(new int[]{1});
            table.setWidthPercentage(100);

            cell = new PdfPCell(new Phrase("Annexure-D", new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD | Font.UNDERLINE)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Statement of Long-Term Advance", new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD | Font.UNDERLINE)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            document.add(table);

            table = new PdfPTable(7);
            table.setWidths(new float[]{0.4f, 1, 0.7f, 0.7f, 0.7f, 0.7f, 1});
            table.setWidthPercentage(100);

            cell = new PdfPCell(new Phrase("Sl No.", new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD | Font.NORMAL)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Type of Advance", new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD | Font.NORMAL)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Date of Sanction", new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD | Font.NORMAL)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Amount of Advance", new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD | Font.NORMAL)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Amount re-paid", new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD | Font.NORMAL)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Amount outstanding", new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD | Font.NORMAL)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Remarks", new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD | Font.NORMAL)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            slno = 0;

            List annexureDList = serviceBookDao.getServiceBookAnnexureDData(selectedEmpObj.getEmpId());
            if (annexureDList != null && annexureDList.size() > 0) {
                EmpServiceHistory esh = null;
                for (int i = 0; i < annexureDList.size(); i++) {
                    esh = (EmpServiceHistory) annexureDList.get(i);

                    slno = slno + 1;

                    cell = new PdfPCell(new Phrase(slno + "", f2));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(esh.getTypeOfAdvance(), f2));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(esh.getDateSanction(), f2));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(esh.getAdvanceAmt(), f2));
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(esh.getAmtRepaid(), f2));
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(esh.getAmtOutstanding(), f2));
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(esh.getRemarksForNoSV(), f2));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                }
            }

            document.add(table);
            document.newPage();

            table = new PdfPTable(1);
            table.setWidths(new int[]{1});
            table.setWidthPercentage(100);

            cell = new PdfPCell(new Phrase("Annexure-E", new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD | Font.UNDERLINE)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Details of Disciplinary Proceedings", new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD | Font.UNDERLINE)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            document.add(table);

            table = new PdfPTable(6);
            table.setWidths(new float[]{0.5f, 1, 0.7f, 1, 0.7f, 1});
            table.setWidthPercentage(100);

            cell = new PdfPCell(new Phrase("Sl No.", new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD | Font.NORMAL)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Details", new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD | Font.NORMAL)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Date", new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD | Font.NORMAL)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Office", new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD | Font.NORMAL)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Date of finalization of Disciplinary Proceedings", new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD | Font.NORMAL)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Remarks/Result of Disciplinary Proceedings", new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD | Font.NORMAL)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            slno = 0;

            List annexureEList = serviceBookDao.getServiceBookAnnexureEData(selectedEmpObj.getEmpId());
            if (annexureEList != null && annexureEList.size() > 0) {
                EmpServiceHistory esh = null;
                for (int i = 0; i < annexureEList.size(); i++) {
                    esh = (EmpServiceHistory) annexureEList.get(i);

                    slno = slno + 1;

                    cell = new PdfPCell(new Phrase(slno + "", f2));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(esh.getDetailsDisciplinaryProceeding(), f2));
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(esh.getDateOfInitiation(), f2));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(esh.getOfficeDisciplinaryProceeding(), f2));
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(esh.getDateOfFinalization(), f2));
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(esh.getRemarksDisciplinaryProceeding(), f2));
                    table.addCell(cell);
                }
            }
            document.add(table);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }

    @ResponseBody
    @RequestMapping(value = "SaveServiceBookAnomali.htm")
    public void SaveServiceBookAnomali(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("ServiceBookAnomali") ServiceBookAnomali serviceBookAnomali, HttpServletResponse response) throws Exception {
        System.out.println("*****");
        serviceBookAnomali.setEmpId(lub.getLoginempid());
        String msg = serviceBookDao.saveServiceBookAnomali(serviceBookAnomali);
        response.setContentType("application/json");
        JSONObject job = new JSONObject();
        job.append("msg", msg);
        PrintWriter out = response.getWriter();
        out.print(job.toString());
        out.flush();
    }

    @RequestMapping(value = "employeeServiceBookAnomalies.htm", method = RequestMethod.GET)
    public ModelAndView employeeServiceBookAnomalies(@ModelAttribute("LoginUserBean") LoginUserBean lub) {
        ModelAndView mv = new ModelAndView("/servicebook/ListofAnnomaliesEmployee");
        List anomaliesList = serviceBookDao.getAnomaliesListEmployee(lub.getLoginempid());
        mv.addObject("anomaliesList", anomaliesList);
        return mv;
    }

    @RequestMapping(value = "officeServiceBookAnomalies.htm", method = RequestMethod.GET)
    public ModelAndView officeServiceBookAnomalies(@ModelAttribute("LoginUserBean") LoginUserBean lub) {
        ModelAndView mv = new ModelAndView("/servicebook/ListofAnnomaliesEmployeeoffice");
        List anomaliesList = serviceBookDao.getAnomaliesListOffice(lub.getLoginoffcode());
        mv.addObject("anomaliesList", anomaliesList);
        return mv;
    }

    @RequestMapping(value = "downloadSupportingFile", method = {RequestMethod.GET})
    public void downloadFile(@ModelAttribute("LoginUserBean") LoginUserBean lub,
            @ModelAttribute("ServiceBookAnomali") ServiceBookAnomali serviceBookAnomali, HttpServletResponse response) throws IOException {
        FileDownload fileDownloadData = serviceBookDao.getFileDownloadData(serviceBookAnomali.getAnomaliId());
        response.setContentType(fileDownloadData.getFiletype());
        response.setHeader("Content-Disposition", "attachment;filename=" + fileDownloadData.getOriginalfilename());
        OutputStream out = response.getOutputStream();
        out.write(fileDownloadData.getFilecontent());
        out.flush();
        out.close();
    }

    @RequestMapping(value = "EmployeeServiceBookHED.htm")
    public ModelAndView EmployeeServiceBookHED(@RequestParam("empid") String empid) {
        HRMSServiceConfiguration.serviceBookRequested();
        ModelAndView mv = new ModelAndView();
        System.out.println(HRMSServiceConfiguration.noofServiceBookRequestedByOffice());
        if (HRMSServiceConfiguration.isResourceAvailable() == true) {
            EmpServiceBook esb = serviceBookDao.getSHReport(empid);
            EmpLeaveAccountPropeties eap = leaveDao.getLeaveAccountDetails(empid, "", "", "EL", "EOL");
            Date todaysDate = new Date();
            DateFormat df3 = new SimpleDateFormat("dd-MMM-yyyy");
            String str3 = df3.format(todaysDate);
            Employee employeeProfile = employeeDAO.getEmployeeProfile(empid);
            Education[] educations = employeeDAO.getEmployeeEducation(empid, str3);
            FamilyRelation[] familyRel = employeeDAO.getEmployeeFamily(empid);
            Address[] address = employeeDAO.getAddress(empid);
            List identity = employeeDAO.getIdentityList(empid);
            ArrayList gispassbookDetails = gispassbookDao.getgisPassbookList(empid);
            ArrayList servicerecordlist = servicerecordDAO.getServiceRecordList(empid);
            List payInServiceList = payInSRDao.getpayInServiceData(empid);
            //List empDetails = gispassbookDao.getgisEmployeeDetaills(selectedEmpObj.getEmpId());

            //mav.addObject("empInfo", empDetails);
            mv = new ModelAndView("/servicebook/ServiceBook", "employeeProfile", employeeProfile);
            mv.addObject("esb", esb);
            mv.addObject("educations", educations);
            mv.addObject("familyRel", familyRel);
            mv.addObject("address", address);
            mv.addObject("identity", identity);
            mv.addObject("eap", eap);
            mv.addObject("gisList", gispassbookDetails);
            mv.addObject("servicerecordList", servicerecordlist);
            mv.addObject("payInServicelist", payInServiceList);
        } else if (HRMSServiceConfiguration.isResourceAvailable() == false) {
            mv = new ModelAndView("/errorpage/errorresourcebusy");

        }
        HRMSServiceConfiguration.serviceBookRequestEnds();
        return mv;
    }

    @RequestMapping(value = "EmployeeServiceBookByID.htm")
    public ModelAndView EmployeeServiceBookByID(@RequestParam("empid") String empid) {

        ModelAndView mv = new ModelAndView();

        EmpServiceBook esb = serviceBookDao.getSHReport(empid);
        EmpLeaveAccountPropeties eap = leaveDao.getLeaveAccountDetails(empid, "", "", "EL", "EOL");
        Date todaysDate = new Date();
        DateFormat df3 = new SimpleDateFormat("dd-MMM-yyyy");
        String str3 = df3.format(todaysDate);
        Employee employeeProfile = employeeDAO.getEmployeeProfile(empid);
        Education[] educations = employeeDAO.getEmployeeEducation(empid, str3);
        FamilyRelation[] familyRel = employeeDAO.getEmployeeFamily(empid);
        Address[] address = employeeDAO.getAddress(empid);
        List identity = employeeDAO.getIdentityList(empid);
        ArrayList gispassbookDetails = gispassbookDao.getgisPassbookList(empid);
        ArrayList servicerecordlist = servicerecordDAO.getServiceRecordList(empid);
        List payInServiceList = payInSRDao.getpayInServiceData(empid);
            
        mv = new ModelAndView("/servicebook/ServiceBook", "employeeProfile", employeeProfile);
        mv.addObject("esb", esb);
        mv.addObject("educations", educations);
        mv.addObject("familyRel", familyRel);
        mv.addObject("address", address);
        mv.addObject("identity", identity);
        mv.addObject("eap", eap);
        mv.addObject("gisList", gispassbookDetails);
        mv.addObject("servicerecordList", servicerecordlist);
        mv.addObject("payInServicelist", payInServiceList);

        return mv;
    }

    @RequestMapping(value = "EmployeeDeputationDetails.htm")
    public ModelAndView EmployeeDeputationDetails(@RequestParam("empid") String empid) {
        HRMSServiceConfiguration.serviceBookRequested();
        ModelAndView mv = new ModelAndView();
        if (HRMSServiceConfiguration.isResourceAvailable() == true) {
            EmpLeaveAccountPropeties eap = leaveDao.getLeaveAccountDetails(empid, "", "", "EL", "EOL");
            Date todaysDate = new Date();
            DateFormat df3 = new SimpleDateFormat("dd-MMM-yyyy");
            String str3 = df3.format(todaysDate);
            Employee employeeProfile = employeeDAO.getEmployeeProfile(empid);
            List promotionList = employeeDAO.allPosting(empid);
            FamilyRelation[] familyRel = employeeDAO.getEmployeeFamily(empid);
            Education[] educations = employeeDAO.getEmployeeEducation(empid, str3);
            List ccrdetails = employeeDAO.getCcrdetail(empid);
            List trainingdetails = employeeDAO.getTrainingdetail(empid);
            List disclipnary = employeeDAO.getdisclipnary(empid);
            Employee employeecurrentProfile = employeeDAO.getEmployeecurrentProfile(empid);

            mv = new ModelAndView("/misreport/EmployeeDeputationview", "employeeProfile", employeeProfile);
            mv.addObject("promotionList", promotionList);
            mv.addObject("familyRel", familyRel);
            mv.addObject("educations", educations);
            mv.addObject("eap", eap);
            mv.addObject("ccrdetails", ccrdetails);
            mv.addObject("trainingdetails", trainingdetails);
            mv.addObject("disclipnary", disclipnary);
            mv.addObject("employeecurrentProfile", employeecurrentProfile);

        } else if (HRMSServiceConfiguration.isResourceAvailable() == false) {
            mv = new ModelAndView("/errorpage/errorresourcebusy");

        }
        HRMSServiceConfiguration.serviceBookRequestEnds();
        return mv;
    }

    @ResponseBody
    @RequestMapping(value = "GetEmployeeServiceBookDataForSBCorrection.htm")
    public void GetEmployeeServiceBookDataForSBCorrection(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub) {

        response.setContentType("application/json");
        PrintWriter out = null;
        JSONObject json = new JSONObject();
        try {

            EmpServiceBook esb = serviceBookDao.getSHReport(lub.getLoginempid());

            json.put("EmpServiceBook", esb.getEmpsbrecord());
            out = response.getWriter();
            out.write(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    @RequestMapping(value = "ServicebookPDFforAG.htm")
    public void ServicebookPDFforAG(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpObj") Users selectedEmpObj) {

        response.setContentType("application/pdf");
        Document document = new Document(PageSize.A4);

        PdfWriter writer = null;
        String empGender = null;
        boolean stopMonthlySubscrip = false;
        try {
            String logopath = context.getInitParameter("SBPath");
            String photopath = context.getInitParameter("PhotoPath");

            response.setHeader("Content-Disposition", "attachment; filename=SERVICE_BOOK_" + selectedEmpObj.getEmpId() + ".pdf");

            writer = PdfWriter.getInstance(document, response.getOutputStream());
            writer.setPageEvent(new ServiceBookHeaderFooter(selectedEmpObj.getEmpId(), selectedEmpObj.getGpfno(), selectedEmpObj.getFullName()));

            document.open();
            Rectangle rect = new Rectangle(36, 108);
            rect.enableBorderSide(1);
            rect.enableBorderSide(2);
            rect.enableBorderSide(4);
            rect.enableBorderSide(8);
            rect.setBorder(2);
            rect.setBorderColor(BaseColor.BLACK);
            rect.setBorderWidth(2);

            //EmpServiceBook esb = new EmpServiceBook();
            Font bold = new Font(Font.FontFamily.HELVETICA, 13, Font.BOLD);
            EmpServiceBook esb = serviceBookDao.getSHReportForAG(selectedEmpObj.getEmpId());
            Date todaysDate = new Date();
            DateFormat df3 = new SimpleDateFormat("dd-MMM-yyyy");
            String str3 = df3.format(todaysDate);
            //  esb.setServerDate(str3);
            Employee employeeProfile = employeeDAO.getEmployeeProfile(selectedEmpObj.getEmpId());

            stopMonthlySubscrip = employeeDAO.stopGpfDeduction(selectedEmpObj.getEmpId());

            Education[] educations = employeeDAO.getEmployeeEducation(selectedEmpObj.getEmpId(), str3);
            FamilyRelation[] familyRel = employeeDAO.getEmployeeFamily(selectedEmpObj.getEmpId());
            //Address[] address = employeeDAO.getAddress(selectedEmpObj.getEmpId());
            Address address = employeeDAO.getAddressForServiceBook(selectedEmpObj.getEmpId());
            List identity = employeeDAO.getIdentityList(selectedEmpObj.getEmpId());
            List nomineedetaillist = employeeDAO.getEmployeeNominee(lub.getLoginempid());

            PdfPTable table = null;
            PdfPCell cell = null;

            Font f1 = new Font();
            f1.setSize(8);
            f1.setFamily("Times New Roman");

            Font f2 = new Font();
            f2.setSize(8);
            f2.setFamily("Times New Roman");

            table = new PdfPTable(1);
            table.setWidths(new int[]{2});
            table.setWidthPercentage(100);

            //String url = filePath + paf.getApplicantempid() + ".jpg";
            String url = logopath + "/odgovt.gif";
            File f = null;
            f = new File(url);
            Image img = null;
            if (f.exists()) {
                img = Image.getInstance(url);
            }

            if (img != null) {
                img.scaleToFit(100f, 80f);
                cell = new PdfPCell(img);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
            } else {
                cell = new PdfPCell();
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
            }

            cell = new PdfPCell(new Phrase("HUMAN RESOURCES MANAGEMENT SYSTEM", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
            cell.setFixedHeight(20);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Government of Odisha", new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD)));
            cell.setFixedHeight(20);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setFixedHeight(60);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("SERVICE BOOK", new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("OF", new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            url = photopath + selectedEmpObj.getEmpId() + ".jpg";
            f = new File(url);
            img = null;
            if (f.exists()) {
                img = Image.getInstance(url);
            }

            if (img != null) {
                img.scaleToFit(100f, 80f);
                img.setBorder(Rectangle.BOX);
                img.setBorderColor(BaseColor.BLACK);
                img.setBorderWidth(1f);
                cell = new PdfPCell(img);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
            } else {
                url = logopath + "NoEmployee.png";
                f = new File(url);
                img = null;
                if (f.exists()) {
                    img = Image.getInstance(url);
                    img.scaleToFit(100f, 80f);
                    img.setBorder(Rectangle.BOX);
                    img.setBorderColor(BaseColor.BLACK);
                    img.setBorderWidth(1f);
                    cell = new PdfPCell(img);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);
                }
            }

            cell = new PdfPCell(new Phrase(employeeProfile.getEmpName(), new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(employeeProfile.getEmpCadre(), f1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(employeeProfile.getEmpAllotmentYear(), f1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            if (employeeProfile.getCadreId() != null && !employeeProfile.getCadreId().equals("")) {
                cell = new PdfPCell(new Phrase(employeeProfile.getCadreId(), f1));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
            }

            cell = new PdfPCell(new Phrase("HRMS ID: " + employeeProfile.getEmpid(), f1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(employeeProfile.getAccttype() + " NO: " + employeeProfile.getGpfno(), f1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            //if (employeeProfile.getGisName() != null && !employeeProfile.getGisName().equals("")) {
            cell = new PdfPCell(new Phrase("GIS NO: " + employeeProfile.getGisNo(), f1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            //}
            /*if (employeeProfile.getGisNo() != null && !employeeProfile.getGisNo().equals("")) {
             cell = new PdfPCell(new Phrase(employeeProfile.getGisNo(), f1));
             cell.setHorizontalAlignment(Element.ALIGN_CENTER);
             cell.setBorder(Rectangle.NO_BORDER);
             table.addCell(cell);
             }*/

            cell = new PdfPCell();
            cell.setFixedHeight(60);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setFixedHeight(60);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setFixedHeight(60);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setFixedHeight(60);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            url = logopath + "/file.JPG";
            f = new File(url);
            img = null;
            if (f.exists()) {
                img = Image.getInstance(url);
            }

            if (img != null) {
                img.scaleToFit(100f, 80f);
                cell = new PdfPCell(img);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
            } else {
                cell = new PdfPCell();
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
            }

            cell = new PdfPCell(new Phrase("Developed By", f1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Centre for Modernizing Government Initiative(CMGI)", f1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            //document.add(table);
            document.newPage();

            url = logopath + "/" + selectedEmpObj.getEmpId() + ".jpg";
            f = new File(url);
            img = null;
            if (f.exists()) {
                img = Image.getInstance(url);
            }

            if (img != null) {
                img.scaleToFit(600f, 580f);
                img.setBorder(Rectangle.BOX);
                img.setBorderColor(BaseColor.BLACK);
                img.setBorderWidth(1f);
                cell = new PdfPCell(img);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
            } else {
                url = logopath + "/SB1stPage.png";
                f = new File(url);
                img = null;
                if (f.exists()) {
                    img = Image.getInstance(url);
                }
                if (img != null) {
                    img.scaleToFit(600f, 5800f);
                    img.setBorder(Rectangle.BOX);
                    img.setBorderColor(BaseColor.BLACK);
                    img.setBorderWidth(1f);
                    cell = new PdfPCell(img);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);
                }
            }

            document.add(table);
            document.newPage();

            table = new PdfPTable(4);
            table.setWidths(new int[]{1, 1, 1, 1});
            table.setWidthPercentage(100);

            cell = new PdfPCell(new Phrase("NAME:", f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(employeeProfile.getEmpName(), f1));
            cell.setColspan(3);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(employeeProfile.getAccttype() + " NO:", f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(employeeProfile.getGpfno(), f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("HRMS ID:", f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(employeeProfile.getEmpid(), f1));
            table.addCell(cell);

            String addressDetails = "";

            String presentaddress = "";
            String permanentaddress = "";

            List addressList = Arrays.asList(address);
            if (addressList != null && addressList.size() > 0) {
                Address addr = null;
                for (int i = 0; i < addressList.size(); i++) {
                    addr = (Address) addressList.get(i);
                    if (addr.getAddressType() != null && !addr.getAddressType().equals("")) {
                        if (addr.getAddressType().equals("PRESENT")) {
                            presentaddress = addr.getAddress();
                        } else if (addr.getAddressType().equals("PERMANENT")) {
                            permanentaddress = addr.getAddress();
                        }
                    }
                    //addressDetails = addr.getAddress();
                }
            }

            cell = new PdfPCell(new Phrase("PRESENT ADDRESS:\n(RESIDENCE)", f1));
            cell.setFixedHeight(20);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(StringUtils.defaultString(address.getPresentAddress()), f2));
            cell.setFixedHeight(20);
            cell.setColspan(3);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("DATE OF BIRTH BY CHRISTIAN ERA AS NEARLY AS CAN BE ASCERTAINED :", f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(StringUtils.defaultString(employeeProfile.getDob()) + "(" + StringUtils.defaultString(employeeProfile.getDobText()) + ")", f2));
            cell.setColspan(3);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("EMPLOYEE EDUCATIONAL DETAILS:", f1));
            cell.setColspan(4);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);

            List educationList = Arrays.asList(educations);

            if (educationList != null && educationList.size() > 0) {
                Education education = null;
                for (int i = 0; i < educationList.size(); i++) {
                    education = (Education) educationList.get(i);

                    cell = new PdfPCell(new Phrase(education.getQualification(), f1));
                    cell.setColspan(4);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("Year of Passing: " + education.getYearofpass(), f1));
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase("Faculty: " + education.getFaculty(), f1));
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase("Degree\nCertificate:", f1));
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(education.getDegree(), f1));
                    table.addCell(cell);
                }
            }

            cell = new PdfPCell(new Phrase("HEIGHT(in cm):", f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(StringUtils.defaultString(employeeProfile.getHeight() + ""), f2));
            cell.setColspan(3);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("PERSONAL\nIDENTIFICATION MARK :", f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(StringUtils.defaultString(employeeProfile.getIdmark()), f2));
            cell.setColspan(3);
            table.addCell(cell);

            String fatherDetails = "";

            List familyList = Arrays.asList(familyRel);
            if (familyList != null && familyList.size() > 0) {
                FamilyRelation familyRelation = null;
                for (int i = 0; i < familyList.size(); i++) {
                    familyRelation = (FamilyRelation) familyList.get(i);
                    if (familyRelation.getRelation() != null && familyRelation.getRelation().equals("FATHER")) {
                        fatherDetails = StringUtils.defaultString(familyRelation.getInitials()) + " " + StringUtils.defaultString(familyRelation.getFname()) + " " + StringUtils.defaultString(familyRelation.getMname()) + " " + StringUtils.defaultString(familyRelation.getLname());
                    }
                }
            }

            cell = new PdfPCell(new Phrase("FATHER'S NAME\nAND RESIDENCE :", f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(fatherDetails + "\n" + address.getPermanentAddress(), f2));
            cell.setColspan(3);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("DATE OF ENTRY IN GOVERMENT SERVICE :", f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(employeeProfile.getDoeGov() + "(" + StringUtils.defaultString(employeeProfile.getEntryGovDateText()) + ")\n" + StringUtils.defaultString(employeeProfile.getEntryGovDateTime()), f2));
            cell.setColspan(3);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("DATE OF ENTRY IN THE SERVICE FOR WHICH SERVICE BOOK CREATED:", f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(employeeProfile.getJoindategoo() + "(" + StringUtils.defaultString(employeeProfile.getJoinDateText()) + ")", f2));
            cell.setColspan(3);
            table.addCell(cell);

            SelectOption so = serviceBookDao.getInitialJoiningData(selectedEmpObj.getEmpId());

            cell = new PdfPCell(new Phrase("INITIAL JOINING OFFICE:", f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(StringUtils.defaultString(so.getLabel()), f2));
            cell.setColspan(3);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("INITIAL JOINING POST:", f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(StringUtils.defaultString(so.getValue()), f2));
            cell.setColspan(3);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("DECLARATION OF HOME TOWN:", f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(employeeProfile.getHomeTown(), f2));
            cell.setColspan(3);
            table.addCell(cell);

            document.newPage();

            cell = new PdfPCell(new Phrase("DATE OF SUPERANNUATION:", f1));
            cell.setColspan(2);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(employeeProfile.getDor(), f2));
            cell.setColspan(2);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("CATEGORY:", f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(employeeProfile.getCategory(), f2));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("MARITAL STATUS:", f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(employeeProfile.getMaritalStatus(), f2));
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("CELL PHONE:", f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(employeeProfile.getMobile(), f2));
            cell.setColspan(3);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("IDENTITY OF THE EMPLOYEE:", f1));
            cell.setFixedHeight(20);
            cell.setColspan(4);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);

            PdfPTable innertable = new PdfPTable(5);
            innertable.setWidths(new int[]{1, 1, 1, 1, 1});
            innertable.setWidthPercentage(100);

            PdfPCell innercell = null;

            innercell = new PdfPCell(new Phrase("Type of Identification", f1));
            innertable.addCell(innercell);
            innercell = new PdfPCell(new Phrase("No.", f1));
            innertable.addCell(innercell);
            innercell = new PdfPCell(new Phrase("Place of Issue", f1));
            innertable.addCell(innercell);
            innercell = new PdfPCell(new Phrase("Date of Issue", f1));
            innertable.addCell(innercell);
            innercell = new PdfPCell(new Phrase("Date of Expiry", f1));
            innertable.addCell(innercell);

            cell = new PdfPCell(innertable);
            cell.setFixedHeight(20);
            cell.setColspan(4);
            table.addCell(cell);

            //List identityList = Arrays.asList(identity);
            if (identity != null && identity.size() > 0) {
                IdentityInfo idInfo = null;
                for (int i = 0; i < identity.size(); i++) {
                    idInfo = (IdentityInfo) identity.get(i);

                    innertable = new PdfPTable(5);
                    innertable.setWidths(new int[]{1, 1, 1, 1, 1});
                    innertable.setWidthPercentage(100);

                    innercell = new PdfPCell(new Phrase(idInfo.getIdentityDesc(), f2));
                    innertable.addCell(innercell);
                    innercell = new PdfPCell(new Phrase(idInfo.getIdentityNo(), f2));
                    innertable.addCell(innercell);
                    innercell = new PdfPCell(new Phrase(idInfo.getPlaceOfIssue(), f2));
                    innertable.addCell(innercell);
                    innercell = new PdfPCell(new Phrase(idInfo.getIssueDate(), f2));
                    innertable.addCell(innercell);
                    innercell = new PdfPCell(new Phrase(idInfo.getExpiryDate(), f2));
                    innertable.addCell(innercell);

                    cell = new PdfPCell(innertable);
                    cell.setFixedHeight(20);
                    cell.setColspan(4);
                    table.addCell(cell);
                }
            }

            cell = new PdfPCell(new Phrase("FAMILY OF THE EMPLOYEE:", f1));
            cell.setFixedHeight(20);
            cell.setColspan(4);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);

            innertable = new PdfPTable(6);
            innertable.setWidths(new int[]{1, 1, 1, 1, 1, 1});
            innertable.setWidthPercentage(100);

            innercell = new PdfPCell(new Phrase("Relation Type", f1));
            innertable.addCell(innercell);
            innercell = new PdfPCell(new Phrase("Name", f1));
            innertable.addCell(innercell);
            innercell = new PdfPCell(new Phrase("Is Alive?", f1));
            innertable.addCell(innercell);
            innercell = new PdfPCell(new Phrase("DOB", f1));
            innertable.addCell(innercell);
            innercell = new PdfPCell(new Phrase("Marital Status", f1));
            innertable.addCell(innercell);
            innercell = new PdfPCell(new Phrase("Age", f1));
            innertable.addCell(innercell);

            cell = new PdfPCell(innertable);
            cell.setFixedHeight(20);
            cell.setColspan(4);
            table.addCell(cell);

            if (familyList != null && familyList.size() > 0) {
                FamilyRelation familyRelation = null;
                for (int i = 0; i < familyList.size(); i++) {
                    familyRelation = (FamilyRelation) familyList.get(i);

                    innertable = new PdfPTable(6);
                    innertable.setWidths(new int[]{1, 1, 1, 1, 1, 1});
                    innertable.setWidthPercentage(100);

                    innercell = new PdfPCell(new Phrase(familyRelation.getRelation(), f2));
                    innertable.addCell(innercell);
                    innercell = new PdfPCell(new Phrase(StringUtils.defaultString(familyRelation.getInitials()) + " " + StringUtils.defaultString(familyRelation.getFname()) + " " + StringUtils.defaultString(familyRelation.getMname()) + " " + StringUtils.defaultString(familyRelation.getLname()), f2));
                    innertable.addCell(innercell);
                    innercell = new PdfPCell(new Phrase(familyRelation.getIfalive(), f2));
                    innertable.addCell(innercell);
                    innercell = new PdfPCell(new Phrase(familyRelation.getDob(), f2));
                    innertable.addCell(innercell);
                    innercell = new PdfPCell(new Phrase(familyRelation.getMarital(), f2));
                    innertable.addCell(innercell);
                    innercell = new PdfPCell(new Phrase(familyRelation.getAge(), f2));
                    innertable.addCell(innercell);

                    cell = new PdfPCell(innertable);
                    cell.setFixedHeight(20);
                    cell.setColspan(4);
                    table.addCell(cell);
                }
            }

            cell = new PdfPCell(new Phrase("NOMINEE:", f1));
            cell.setFixedHeight(20);
            cell.setColspan(4);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);

            innertable = new PdfPTable(6);
            innertable.setWidths(new int[]{1, 1, 1, 1, 1, 1});
            innertable.setWidthPercentage(100);

            innercell = new PdfPCell(new Phrase("Relation Type", f1));
            innertable.addCell(innercell);
            innercell = new PdfPCell(new Phrase("Name", f1));
            innertable.addCell(innercell);
            innercell = new PdfPCell(new Phrase("Is Alive?", f1));
            innertable.addCell(innercell);
            innercell = new PdfPCell(new Phrase("DOB", f1));
            innertable.addCell(innercell);
            innercell = new PdfPCell(new Phrase("Marital Status", f1));
            innertable.addCell(innercell);
            innercell = new PdfPCell(new Phrase("Age", f1));
            innertable.addCell(innercell);

            cell = new PdfPCell(innertable);
            cell.setFixedHeight(20);
            cell.setColspan(4);
            table.addCell(cell);

            innertable = new PdfPTable(6);
            innertable.setWidths(new int[]{1, 1, 1, 1, 1, 1});
            innertable.setWidthPercentage(100);

            FamilyRelation familyRelation = null;

            if (nomineedetaillist != null && nomineedetaillist.size() > 0) {
                for (int i = 0; i < nomineedetaillist.size(); i++) {
                    familyRelation = (FamilyRelation) nomineedetaillist.get(i);
                    innercell = new PdfPCell(new Phrase(familyRelation.getRelation(), f2));
                    innertable.addCell(innercell);
                    innercell = new PdfPCell(new Phrase(StringUtils.defaultString(familyRelation.getInitials()) + " " + StringUtils.defaultString(familyRelation.getFname()) + " " + StringUtils.defaultString(familyRelation.getMname()) + " " + StringUtils.defaultString(familyRelation.getLname()), f2));
                    innertable.addCell(innercell);
                    innercell = new PdfPCell(new Phrase(familyRelation.getIfalive(), f2));
                    innertable.addCell(innercell);
                    innercell = new PdfPCell(new Phrase(familyRelation.getDob(), f2));
                    innertable.addCell(innercell);
                    innercell = new PdfPCell(new Phrase(familyRelation.getMarital(), f2));
                    innertable.addCell(innercell);
                    innercell = new PdfPCell(new Phrase(familyRelation.getAge(), f2));
                    innertable.addCell(innercell);
                }
            }

            cell = new PdfPCell(innertable);
            cell.setFixedHeight(40);
            cell.setColspan(4);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Is Employed under any Reservation Category?", f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(employeeProfile.getIfReservation(), f2));
            cell.setColspan(3);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Reservation Category Under Which Employed", f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(StringUtils.defaultString(employeeProfile.getCategory()), f2));
            cell.setColspan(3);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Is Employed under Rehabilation Assistance Scheme?", f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(StringUtils.defaultString(employeeProfile.getIfRehabiltation()), f2));
            cell.setColspan(3);
            table.addCell(cell);

            document.add(table);
            document.newPage();

            table = new PdfPTable(4);
            table.setWidths(new float[]{1, 0.5f, 0.5f, 2});
            table.setWidthPercentage(100);

            cell = new PdfPCell(new Phrase("Post/Cadre/Scale of Pay", f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Pay", f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("WEF", f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Entry in the Service Book", f1));
            table.addCell(cell);

            if (esb.getEmpsbrecord() != null && esb.getEmpsbrecord().size() > 0) {
                EmpServiceHistory esh = null;
                for (int i = 0; i < esb.getEmpsbrecord().size(); i++) {
                    esh = (EmpServiceHistory) esb.getEmpsbrecord().get(i);

                    int slno = i + 1;

                    Phrase phrs = new Phrase();
                    Chunk c1 = new Chunk(StringUtils.defaultString(esh.getCategory()), new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL | Font.UNDERLINE));
                    Chunk c2 = new Chunk("\n\n" + StringUtils.defaultString(esh.getSbdescription()), f2);
                    Chunk c3 = new Chunk();
                    Chunk c4 = new Chunk();
                    if (esh.getModuleNote() != null && !esh.getModuleNote().equals("")) {
                        c3 = new Chunk("\n\nNote : ", f2);
                        c4 = new Chunk(StringUtils.defaultString(esh.getModuleNote()), f2);
                    }
                    phrs.add(c1);
                    phrs.add(c2);
                    phrs.add(c3);
                    phrs.add(c4);

                    Phrase col1stphrs = new Phrase();
                    c1 = new Chunk(slno + "", new Font(Font.FontFamily.TIMES_ROMAN, 4, Font.NORMAL));
                    col1stphrs.add(c1);
                    if (esh.getSpn() != null && !esh.getSpn().equals("")) {
                        c2 = new Chunk("\n" + esh.getSpn(), f2);
                        col1stphrs.add(c2);
                    }
                    if (esh.getCadre() != null && !esh.getCadre().equals("")) {
                        c3 = new Chunk("\n" + esh.getCadre(), f2);
                        col1stphrs.add(c3);
                    }
                    if (esh.getPayscale() != null && !esh.getPayscale().equals("")) {
                        c4 = new Chunk("\n" + esh.getPayscale(), f2);
                        col1stphrs.add(c4);
                    }
                    //cell = new PdfPCell(new Phrase(slno + "", new Font(Font.FontFamily.TIMES_ROMAN, 3, Font.NORMAL)));
                    cell = new PdfPCell(col1stphrs);
                    table.addCell(cell);
                    if (esh.getPay() != null && !esh.getPay().equals("")) {
                        cell = new PdfPCell(new Phrase("PAY: Rs." + esh.getPay(), f2));
                        table.addCell(cell);
                    } else {
                        cell = new PdfPCell(new Phrase("", f2));
                        table.addCell(cell);
                    }
                    cell = new PdfPCell(new Phrase(StringUtils.defaultString(esh.getWefChange()), f2));
                    table.addCell(cell);
                    cell = new PdfPCell(phrs);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("ENTRY TAKEN ON " + StringUtils.defaultString(esh.getDoe()), new Font(Font.FontFamily.TIMES_ROMAN, 4, Font.NORMAL)));
                    cell.setColspan(4);
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);
                }
            }
            document.add(new Paragraph("\n\n"));

            if (employeeProfile.getGender() != null && employeeProfile.getGender().equals("M")) {
                empGender = "He";
            } else if (employeeProfile.getGender() != null && employeeProfile.getGender().equals("F")) {
                empGender = "She";
            } else {
                empGender = "He/She";
            }

            if (employeeProfile.getAccttype() != null && !employeeProfile.getAccttype().equals("") && (employeeProfile.getAccttype().equalsIgnoreCase("GPF") || employeeProfile.getAccttype().equalsIgnoreCase("TPF"))) {

                if (stopMonthlySubscrip == true) {
                    /*cell = new PdfPCell(new Phrase(empGender + " is going to be retired from Government Services w.e.f. " + employeeProfile.getTxtDos() + " on attaining the age of Superannuation.", bold));
                     cell.setColspan(4);
                     cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                     cell.setBorder(Rectangle.BOTTOM | Rectangle.TOP | Rectangle.LEFT | Rectangle.RIGHT);
                     cell.setFixedHeight(55);
                     table.addCell(cell);*/
                    document.add(table);
                    /*Paragraph p2 = new Paragraph("Last pay on the date of retirement under normal circumstances :", bold);
                     p2.setAlignment(Element.ALIGN_CENTER);                    
                     p2.setSpacingBefore(25f);
                     p2.setSpacingAfter(15f);                   
                     document.add(p2);*/
                }
            }

            document.newPage();

            table = new PdfPTable(11);
            table.setWidths(new float[]{0.7f, 0.7f, 0.5f, 0.5f, 0.7f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f});
            table.setWidthPercentage(100);

            EmpLeaveAccountPropeties eap = leaveDao.getLeaveAccountDetails(selectedEmpObj.getEmpId(), "", "", "EL", "EOL");

            cell = new PdfPCell(new Phrase(StringUtils.defaultString(eap.getLeaveType()) + " ACCOUNT", f1));
            cell.setColspan(11);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Period of Account", f1));
            cell.setColspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("No of Complete Months", f1));
            cell.setRowspan(2);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Leave Credit in Days", f1));
            cell.setRowspan(2);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Total No. of Leave(EOL) Availed", f1));
            cell.setRowspan(2);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("EL to be Deducted", f1));
            cell.setRowspan(2);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Balance on return from Leave", f1));
            cell.setRowspan(2);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Leave Availed", f1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(3);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Balance Leave", f1));
            cell.setRowspan(2);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("From", f1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("To", f1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("From", f1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("To", f1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Total", f1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            if (eap.getLeaveOBalDate() != null && !eap.getLeaveOBalDate().equals("")) {
                cell = new PdfPCell(new Phrase("OPENING BALANCE ON DATE " + eap.getLeaveOBalDate() + " " + eap.getFnan(), f1));
                cell.setColspan(6);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(eap.getLeaveOBal() + "", f1));
                table.addCell(cell);
                cell = new PdfPCell();
                cell.setColspan(4);
                table.addCell(cell);
            }

            if (eap.getCreditLvList() != null && eap.getCreditLvList().size() > 0) {

                CreditLeaveProperties clp = null;

                for (int i = 0; i < eap.getCreditLvList().size(); i++) {

                    clp = (CreditLeaveProperties) eap.getCreditLvList().get(i);

                    if (clp.getCreditType() != null && clp.getCreditType().equals("U")) {

                        cell = new PdfPCell(new Phrase("UNAVAILED JOINING TIME", f1));
                        cell.setColspan(2);
                        table.addCell(cell);
                        cell = new PdfPCell(new Phrase(clp.getCompMonths(), f1));
                        cell.setRowspan(2);
                        table.addCell(cell);
                        cell = new PdfPCell(new Phrase(clp.getLeaveCredited(), f1));
                        cell.setRowspan(2);
                        table.addCell(cell);
                        cell = new PdfPCell(new Phrase(clp.getTotEOLNumber(), f1));
                        cell.setRowspan(2);
                        table.addCell(cell);
                        cell = new PdfPCell(new Phrase(clp.getLeaveDeduct(), f1));
                        cell.setRowspan(2);
                        table.addCell(cell);
                        cell = new PdfPCell(new Phrase(clp.getCreditBalShow(), f1));
                        cell.setRowspan(2);
                        table.addCell(cell);
                        cell = new PdfPCell();
                        table.addCell(cell);
                        cell = new PdfPCell();
                        table.addCell(cell);
                        cell = new PdfPCell();
                        table.addCell(cell);
                    }

                    if (clp.getCreditType() != null && clp.getCreditType().equals("G")) {

                        cell = new PdfPCell(new Phrase(clp.getFromDate(), f1));
                        table.addCell(cell);
                        cell = new PdfPCell(new Phrase(clp.getToDate(), f1));
                        table.addCell(cell);
                        cell = new PdfPCell(new Phrase(clp.getCompMonths(), f1));
                        table.addCell(cell);
                        cell = new PdfPCell(new Phrase(clp.getLeaveCredited(), f1));
                        table.addCell(cell);
                        cell = new PdfPCell(new Phrase(clp.getTotEOLNumber(), f1));
                        table.addCell(cell);
                        cell = new PdfPCell(new Phrase(clp.getLeaveDeduct(), f1));
                        table.addCell(cell);
                        cell = new PdfPCell(new Phrase(clp.getCreditBalShow(), f1));
                        table.addCell(cell);
                        cell = new PdfPCell();
                        cell.setColspan(4);
                        table.addCell(cell);
                    }

                    if (clp.getAvailedLeave() != null && clp.getAvailedLeave().size() > 0) {
                        EmpLeaveAvailedPropeties empLeaveAvailedProperties = null;
                        for (int j = 0; j < clp.getAvailedLeave().size(); j++) {
                            empLeaveAvailedProperties = (EmpLeaveAvailedPropeties) clp.getAvailedLeave().get(j);

                            cell = new PdfPCell();
                            cell.setColspan(7);
                            table.addCell(cell);
                            cell = new PdfPCell(new Phrase(empLeaveAvailedProperties.getFromdate(), f1));
                            table.addCell(cell);
                            cell = new PdfPCell(new Phrase(empLeaveAvailedProperties.getTodate(), f1));
                            table.addCell(cell);
                            cell = new PdfPCell(new Phrase(empLeaveAvailedProperties.getTotalNoofdays() + "", f1));
                            table.addCell(cell);
                            cell = new PdfPCell(new Phrase(empLeaveAvailedProperties.getBalanceLeave() + "", f1));
                            table.addCell(cell);
                        }
                    }

                    if (clp.getSurrenderedLeave() != null && clp.getSurrenderedLeave().size() > 0) {
                        EmpLeaveSurrenderedPropeties empLeaveSurrenderedPropeties = null;
                        for (int j = 0; j < clp.getSurrenderedLeave().size(); j++) {
                            empLeaveSurrenderedPropeties = (EmpLeaveSurrenderedPropeties) clp.getSurrenderedLeave().get(j);

                            cell = new PdfPCell();
                            cell.setColspan(7);
                            table.addCell(cell);
                            cell = new PdfPCell(new Phrase("SURRENDERED", f1));
                            table.addCell(cell);
                            cell = new PdfPCell(new Phrase("LEAVE", f1));
                            table.addCell(cell);
                            cell = new PdfPCell(new Phrase(empLeaveSurrenderedPropeties.getSurrenderDays() + "", f1));
                            table.addCell(cell);
                            cell = new PdfPCell(new Phrase(empLeaveSurrenderedPropeties.getBalanceLeave() + "", f1));
                            table.addCell(cell);
                        }
                    }
                }
            }

            document.add(table);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }

    @RequestMapping(value = "financialStatementReport.htm", method = RequestMethod.GET)
    public ModelAndView financialStatement(@ModelAttribute("LoginUserBean") LoginUserBean lub,
            @ModelAttribute("EmpServiceHistory") EmpServiceHistory empServiceHistory) {
        ModelAndView mv = new ModelAndView("/payroll/financialReport/FinancialStatement");
        List empFinanceStateMent = serviceBookDao.getFinancialDetailsById(lub.getLoginempid());
        Employee employee = employeeDAO.EditEmpoyeeDataAdmin(lub.getLoginempid());
        mv.addObject("empFinanceStateMent", empFinanceStateMent);
        mv.addObject("employee", employee);
        return mv;
    }
}
