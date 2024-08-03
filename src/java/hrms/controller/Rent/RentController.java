/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.Rent;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import hrms.common.CommonFunctions;
import hrms.common.HeaderFooterPageEvent;
import hrms.dao.Rent.RentDAO;
import hrms.dao.Rent.RentDAOImpl;
import hrms.dao.master.DepartmentDAO;
import hrms.model.Rent.IfmsTransactionBean;
import hrms.model.Rent.QuarterBean;
import hrms.model.Rent.QuarterList;
import hrms.model.employee.Address;
import hrms.model.login.LoginUserBean;
import hrms.model.login.Users;
import hrms.model.Rent.ScrollMain;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
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
@SessionAttributes("LoginUserBean")
public class RentController implements ServletContextAware {

    @Autowired
    public RentDAO rentDAO;

    @Autowired
    public DepartmentDAO deptDAO;

    private ServletContext context;

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.context = servletContext;
    }

    @RequestMapping(value = "ApplyNDC", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView ProposalOrderList(ModelMap model, @ModelAttribute("QuarterBean") QuarterBean qBean, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response) {

        ModelAndView mav = null;
        QuarterBean qBean1 = new QuarterBean();
        qBean1.setFullName(lub.getLoginname());
        Users ue = rentDAO.getRentEmpDetail(lub.getLoginempid());
        mav = new ModelAndView("/Rent/ApplyNDC", "QuarterBean", qBean);
        mav.addObject("qBean1", qBean1);
        mav.addObject("user", ue);
        QuarterBean qBean2 = rentDAO.getNdcApplicationDetail(lub.getLoginempid());
        List deptlist = deptDAO.getDepartmentList();
        mav.addObject("deptlist", deptlist);
        mav.addObject("qBean", qBean2);
        return mav;
    }

    @RequestMapping(value = "ApplyRentNDC", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView applyRentNDC(ModelMap model, @ModelAttribute("QuarterBean") QuarterBean qBean, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response) {

        ModelAndView mav = null;
        String keyFilePath = context.getInitParameter("IFMSKeyPath");
        String returnPath = context.getInitParameter("IFMSReturnURL");
        QuarterBean qBean1 = new QuarterBean();
        qBean1.setFullName(lub.getLoginname());
        Users ue = rentDAO.getRentEmpDetail(lub.getLoginempid());
        QuarterBean qBean2 = rentDAO.getNdcApplicationDetail(lub.getLoginempid());
        String decMsg = "";
        IfmsTransactionBean iBean = null;
        String isPaymentDone = "N";
        if (qBean.getMsg() != null && !qBean.getMsg().equals("")) {
            decMsg = rentDAO.getDecryptedText(qBean.getMsg(), keyFilePath);
            //decMsg = qBean.getMsg();
            //System.out.println("Decrypted:"+decMsg);
            iBean = rentDAO.updateTransaction(decMsg, qBean2.getApplicationId(), lub.getLoginempid());
            isPaymentDone = "Y";
        }
        Address addr = rentDAO.getRentEmpAddress(lub.getLoginempid());
        mav = new ModelAndView("/Rent/ApplyRentNDC", "QuarterBean", qBean);
        mav.addObject("qBean1", qBean1);
        mav.addObject("user", ue);
        mav.addObject("msg", decMsg);
        mav.addObject("addr", addr);

        mav.addObject("encryptedText", rentDAO.getEncryptedText(lub.getLoginempid(), keyFilePath, returnPath, qBean2.getRecoveryAmount()));
        List unitList = rentDAO.getQuarterUnitAreaList();
        List deptlist = deptDAO.getDepartmentList();
        QuarterBean aqBean = rentDAO.getAppQuarterDetail(lub.getLoginempid());
        if(aqBean.getQuarterType() != null && !aqBean.getQuarterType().equals(""))
        {
            qBean2.setQuarterType(aqBean.getQuarterType());
            qBean2.setQuarterUnit(aqBean.getQuarterUnit());
            qBean2.setBuildingNo(aqBean.getBuildingNo());
            qBean2.setAddress(aqBean.getAddress());
        }
        mav.addObject("deptlist", deptlist);
        mav.addObject("qBean", qBean2);
        mav.addObject("iBean", iBean);
        mav.addObject("aqBean", aqBean);
        mav.addObject("quarterUnit", aqBean.getQuarterUnit());
        mav.addObject("quarterType", aqBean.getQuarterType());
        mav.addObject("buildingNo", aqBean.getBuildingNo());
        mav.addObject("address", aqBean.getAddress());
        mav.addObject("isPaymentDone", isPaymentDone);
        mav.addObject("unitList", unitList);
        return mav;
    }

    @RequestMapping(value = "PrintNDC", method = RequestMethod.GET)
    public ModelAndView printNDC(ModelMap model, @ModelAttribute("QuarterBean") QuarterBean qBean, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response, @RequestParam Map<String, String> requestParams) {
        ModelAndView mav = null;
        QuarterBean qBean1 = new QuarterBean();
        qBean1.setFullName(lub.getLoginname());
        Users ue = rentDAO.getRentEmpDetail(lub.getLoginempid());
        mav = new ModelAndView("/Rent/PrintNDC", "QuarterBean", qBean);
        mav.addObject("qBean1", qBean1);
        mav.addObject("user", ue);
        QuarterBean qBean2 = rentDAO.getNdcApplicationDetail(lub.getLoginempid());
        List deptlist = deptDAO.getDepartmentList();
        mav.addObject("deptlist", deptlist);
        mav.addObject("qBean", qBean2);
        return mav;
    }
    
    @RequestMapping(value = "PrintAdminNDC", method = RequestMethod.GET)
    public ModelAndView printAdminNDC(ModelMap model, @ModelAttribute("QuarterBean") QuarterBean qBean, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam Map<String, String> requestParams) {
        ModelAndView mav = null;
        String applicationId = requestParams.get("id");
        
                    qBean = rentDAO.getAuthApplicationDetail(applicationId);
            List li = rentDAO.getAuthList();
            System.out.println(""+qBean.getEmpId());
            QuarterBean aqBean = rentDAO.getAppQuarterDetail(qBean.getEmpId());
            QuarterBean lBean = rentDAO.getLedgerAmount(qBean.getEmpId());
            if(aqBean.getQuarterType() != null && !aqBean.getQuarterType().equals(""))
            {
                qBean.setQuarterType(aqBean.getQuarterType());
                qBean.setQuarterUnit(aqBean.getQuarterUnit());
                qBean.setBuildingNo(aqBean.getBuildingNo());
                qBean.setAddress(aqBean.getAddress());
            }
            qBean.setLedgerAmount(lBean.getLedgerAmount());
            System.out.println(""+qBean.getEmpId());
            mav = new ModelAndView("/Rent/PrintAdminNDC", "QuarterBean", qBean);
            mav.addObject("qBean", qBean);
            mav.addObject("authList", li);
            //System.out.println("Display:"+displayForm);
            mav.addObject("gpc", lub.getLogingpc());
        return mav;
    }    

    @RequestMapping(value = "SaveNDCApplication", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView saveNDCApplication(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("QuarterBean") QuarterBean qtBean) {
        String dirPath = context.getInitParameter("ClearanceCertificatePath");
        rentDAO.saveNDCApplication(qtBean, lub.getLoginempid(), lub.getLoginspc(), dirPath);
        ModelAndView mav = new ModelAndView("redirect:/ApplyNDC.htm");
        return mav;
    }

    @RequestMapping(value = "SaveNDCRentApplication", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView saveNDCRentApplication(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("QuarterBean") QuarterBean qtBean) {
        rentDAO.saveNDCRentApplication(qtBean, lub.getLoginempid(), lub.getLoginspc());
        ModelAndView mav = new ModelAndView("redirect:/ApplyRentNDC.htm");
        return mav;
    }

    @RequestMapping(value = "downloadCC", method = {RequestMethod.GET, RequestMethod.POST})
    public void downloadCC(HttpServletResponse response, @RequestParam Map<String, String> requestParams
    ) {
        int applicationId = Integer.parseInt(requestParams.get("applicationId"));
        String fileType = "";
        String dirPath = context.getInitParameter("ClearanceCertificatePath");
        rentDAO.downloadNDCDocument(response, dirPath, applicationId, fileType);
    }

    @RequestMapping(value = "downloadNDC", method = {RequestMethod.GET, RequestMethod.POST})
    public void downloadNDC(HttpServletResponse response, @RequestParam Map<String, String> requestParams
    ) {
        int applicationId = Integer.parseInt(requestParams.get("applicationId"));
        String fileType = "";
        String dirPath = context.getInitParameter("FinalNDCPath");
        rentDAO.downloadFinalNDC(response, dirPath, applicationId, fileType);
    }

    @RequestMapping(value = "RentNDCTask", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView rentNDCTask(@ModelAttribute("QuarterBean") QuarterBean qBean,
            @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response, @RequestParam("taskId") int taskId) {
        String path = "/incrementProposal/RentNDCTask";

        ModelAndView mav = new ModelAndView();
        try {
            // int proposalId = incrementProposalDao.getProposalMasterId(taskId);
            // int statusId = incrementProposalDao.getTaskStatusId(taskId);
            // incr.setProposalId(proposalId);
            // incr.setProcessStatus(statusId);
            // mav.addObject("incrementForm", incr);
            // mav.setViewName(path);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return mav;

    }

    @RequestMapping(value = "NDCApplicationList", method = RequestMethod.GET)
    public ModelAndView NDCApplicationList(ModelMap model, @ModelAttribute("QuarterBean") QuarterBean qBean, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response) {
        ModelAndView mav = null;
        System.out.println("EmpID:" + lub.getLoginempid());
        List li = rentDAO.applicationList(lub.getLoginempid());
        mav = new ModelAndView("/Rent/NDCApplicationList");
        mav.addObject("applicationList", li);
        return mav;
    }
    
    @RequestMapping(value = "DisposedApplicationList", method = RequestMethod.GET)
    public ModelAndView DisposedApplicationList(ModelMap model, @ModelAttribute("QuarterBean") QuarterBean qBean, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response) {
        ModelAndView mav = null;
        List li = rentDAO.disposedApplicationList(lub.getLoginempid());
        mav = new ModelAndView("/Rent/DisposedApplicationList");
        mav.addObject("applicationList", li);
        return mav;
    }    

    @RequestMapping(value = "NDCDetailAction", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView authorityView(@ModelAttribute("QuarterBean") QuarterBean qBean,
            @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response, @RequestParam("applicationId") int applicationId) {
        String path = "/Rent/NDCDetailAction";
        String displayForm = "N";
        ModelAndView mav = new ModelAndView();
        try {
            qBean = rentDAO.getAuthApplicationDetail(applicationId + "");
            if (qBean.getPendingAt().equals(lub.getLoginempid())) {
                displayForm = "Y";
            }
            if (qBean.getRentOfficer().equals(lub.getLoginempid())) {
                qBean.setIsRentOfficer("Y");
            }

            List li = rentDAO.getAuthList();
            List tList = rentDAO.getEmpPaymentTransactions(qBean.getEmpId(), qBean.getApplicationId());
            QuarterBean aqBean = rentDAO.getAppQuarterDetail(qBean.getEmpId());
            QuarterBean lBean = rentDAO.getLedgerAmount(qBean.getEmpId());
            if(aqBean.getQuarterType() != null && !aqBean.getQuarterType().equals(""))
            {
                qBean.setQuarterType(aqBean.getQuarterType());
                qBean.setQuarterUnit(aqBean.getQuarterUnit());
                qBean.setBuildingNo(aqBean.getBuildingNo());
                qBean.setAddress(aqBean.getAddress());
            }
            qBean.setLedgerAmount(lBean.getLedgerAmount());
            mav.addObject("qBean", qBean);
            mav.addObject("authList", li);
            //System.out.println("Display:"+displayForm);
            mav.addObject("displayForm", displayForm);
            mav.addObject("gpc", lub.getLogingpc());
            mav.addObject("tList", tList);
            mav.setViewName(path);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return mav;
    }

    @RequestMapping(value = "SaveNDCAuthority", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView saveNDCAuthority(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("QuarterBean") QuarterBean qtBean) {
        rentDAO.saveNDCAuthority(qtBean, lub.getLoginempid(), lub.getLogingpc());
        ModelAndView mav = new ModelAndView("redirect:/NDCApplicationList.htm");
        return mav;
    }

    @RequestMapping(value = "GenerateNDCpdf", method = RequestMethod.GET)
    public void generateNDCPDF(ModelMap model, @ModelAttribute("QuarterBean") QuarterBean qBean,
            @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response, @RequestParam("applicationId") int applicationId) {

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=NO_DEMAND_CERTIFICATE-" + applicationId + ".pdf");
        Document document = new Document(PageSize.A4);
        final Font titlefontSmall = FontFactory.getFont("Arial", 15, Font.BOLD | Font.UNDERLINE);
        final Font headingRule = FontFactory.getFont("Arial", 15, Font.BOLD);
        final Font standardRule = FontFactory.getFont("Arial", 10, Font.NORMAL);
        String offname = "";
        qBean = rentDAO.getAuthApplicationDetail(applicationId + "");
        Users ue = rentDAO.getRentEmpDetail(qBean.getEmpId());
        System.out.println("Office: "+ue.getOffname());
        String ndcFileId = rentDAO.getNdcFileId(applicationId);
        try {
            HeaderFooterPageEvent event = new HeaderFooterPageEvent();
            PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());
            Rectangle rect = new Rectangle(150, 30, 550, 800);
            writer.setBoxSize("art", rect);
            writer.setPageEvent(event);
            document.open();

            Paragraph rule = new Paragraph("Government of Odisha", headingRule);
            rule.setAlignment(Element.ALIGN_CENTER);
            document.add(rule);

            Paragraph heading = new Paragraph("General Administration & Public Grievance (Rent) Department\nBhubaneswar\n\n", headingRule);
            heading.setAlignment(Element.ALIGN_CENTER);
            document.add(heading);

            PdfPTable table = new PdfPTable(3);
            table.setWidths(new int[]{4, 1, 3});
            table.setWidthPercentage(100);

            PdfPCell cell = null;

            cell = new PdfPCell(new Phrase("Rent NDC No. " + ndcFileId, standardRule));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("", standardRule));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Date: " + CommonFunctions.getFormattedOutputDate1(new Date()), standardRule));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);

            document.add(table);

            Paragraph rule2 = new Paragraph("\n", standardRule);
            rule2.setAlignment(Element.ALIGN_CENTER);
            document.add(rule2);

            Paragraph rule1 = new Paragraph("NO DEMAND CERTIFICATE", titlefontSmall);
            rule1.setAlignment(Element.ALIGN_CENTER);
            document.add(rule1);
            if (qBean.getHasOccupied().equals("No")) {
                System.out.println("Office: "+ue.getOffname());
                String pointStr = "\nCertified that Shri/Smt." + qBean.getFullName() + " Designation " + qBean.getDesignation() + " Office of the " + ue.getOffname() + " had not occupied any G.A. Pool Quarters at "
                        + "Bhubaneswar & Cuttack during the entire period of his/her service.";
                Paragraph point1 = new Paragraph(pointStr);
                point1.setAlignment(Element.ALIGN_JUSTIFIED);
                document.add(point1);

                String pointStr2 = "\nAs such, no amount of House Licence Fee is outstanding against him.\n\n\n\n";
                Paragraph point2 = new Paragraph(pointStr2);
                document.add(point2);
            }
            if (qBean.getHasOccupied().equals("Yes")) {
                String pointStr = "\nCertified that Shri/Smt." + qBean.getFullName() + " Designation " + qBean.getDesignation() + " Office of the " + ue.getOffname() + " was in occupation of G.A. Pool Quarter No. " + qBean.getBuildingNo() + " of " + qBean.getQuarterType() + " in " + qBean.getQuarterUnit() + ""
                        + " and has vacated the said quarters. He has cleared all arrear House Licence Fee and no amount is outstanding against him/her.";
                Paragraph point1 = new Paragraph(pointStr);
                point1.setAlignment(Element.ALIGN_JUSTIFIED);
                document.add(point1);

                String pointStr2 = "\nAs such, no amount of House Licence Fee is outstanding against him.\n\n\n\n";
                Paragraph point2 = new Paragraph(pointStr2);
                document.add(point2);
            }
            PdfPTable table1 = new PdfPTable(3);
            table1.setWidths(new int[]{4, 1, 3});
            table1.setWidthPercentage(100);

            PdfPCell cell1 = null;

            cell1 = new PdfPCell(new Phrase("Bhubaneswar", standardRule));
            cell1.setBorder(Rectangle.NO_BORDER);
            table1.addCell(cell1);
            cell1 = new PdfPCell(new Phrase("", standardRule));
            cell1.setBorder(Rectangle.NO_BORDER);
            table1.addCell(cell1);
            cell1 = new PdfPCell(new Phrase("Rent Officer\nG.A. & P.G. Department", standardRule));
            cell1.setBorder(Rectangle.NO_BORDER);
            cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell1);

            document.add(table1);

            Paragraph blank = new Paragraph("Copy to:", titlefontSmall);
            document.add(blank);

            Paragraph rule3 = new Paragraph("\n", standardRule);
            rule3.setAlignment(Element.ALIGN_CENTER);
            document.add(rule3);

            Paragraph cpy = new Paragraph("\nPension Sanctioning Authority: " + qBean.getPensionSanctioningAuthority());
            cpy.setAlignment(Element.ALIGN_LEFT);
            document.add(cpy);

            document.newPage();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }

    @RequestMapping(value = "GenerateRIpdf", method = RequestMethod.GET)
    public void generateRIpdf(ModelMap model, @ModelAttribute("QuarterBean") QuarterBean qBean,
            @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response, @RequestParam("applicationId") int applicationId) {

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=RECOVERY_INTIMATION-" + applicationId + ".pdf");
        Document document = new Document(PageSize.A4);
        final Font titlefontSmall = FontFactory.getFont("Arial", 15, Font.BOLD | Font.UNDERLINE);
        final Font headingRule = FontFactory.getFont("Arial", 15, Font.BOLD);
        final Font standardRule = FontFactory.getFont("Arial", 10, Font.NORMAL);
        String offname = "";
        qBean = rentDAO.getAuthApplicationDetail(applicationId + "");
        Users ue = rentDAO.getRentEmpDetail(qBean.getEmpId());
        try {
            HeaderFooterPageEvent event = new HeaderFooterPageEvent();
            PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());
            Rectangle rect = new Rectangle(150, 30, 550, 800);
            writer.setBoxSize("art", rect);
            writer.setPageEvent(event);
            document.open();

            Paragraph rule = new Paragraph("Government of Odisha", headingRule);
            rule.setAlignment(Element.ALIGN_CENTER);
            document.add(rule);

            Paragraph heading = new Paragraph("General Administration & Public Grievance (Rent) Department\nBhubaneswar\n\n", headingRule);
            heading.setAlignment(Element.ALIGN_CENTER);
            document.add(heading);

            PdfPTable table = new PdfPTable(3);
            table.setWidths(new int[]{4, 1, 3});
            table.setWidthPercentage(100);

            PdfPCell cell = null;

            cell = new PdfPCell(new Phrase("Rent NDC No. " + applicationId, standardRule));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("", standardRule));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Date: " + CommonFunctions.getFormattedOutputDate1(new Date()), standardRule));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);

            document.add(table);

            table = new PdfPTable(2);
            table.setWidths(new int[]{1, 15});
            table.setWidthPercentage(100);

            cell = new PdfPCell(new Phrase("To", standardRule));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("", standardRule));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("", standardRule));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(qBean.getPensionSanctioningAuthority(), standardRule));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            Paragraph rule2 = new Paragraph("\n", standardRule);
            rule2.setAlignment(Element.ALIGN_CENTER);
            document.add(rule2);

            String pointStr = "To";
            Paragraph point3 = new Paragraph(pointStr);
            point3.setAlignment(Element.ALIGN_LEFT);
            document.add(point3);

            String pointStr2 = StringUtils.repeat(" ", 5)+""+qBean.getFullName()+"\n\n";
            Paragraph point2 = new Paragraph(pointStr2,headingRule);
            point2.setAlignment(Element.ALIGN_JUSTIFIED);
            document.add(point2);
            pointStr2 = StringUtils.repeat(" ", 20)+"";
            point2 = new Paragraph(pointStr2);
            point2.setAlignment(Element.ALIGN_LEFT);
           // document.add(point2);
            pointStr2 = StringUtils.repeat(" ", 20)+"";
            point2 = new Paragraph(pointStr2);
            point2.setAlignment(Element.ALIGN_LEFT);
           // document.add(point2);
            
            pointStr2 = "Sub: Payment of outstanding house licence fee for occupation of Government quarters at Bhubaneswar.";
            point2 = new Paragraph(pointStr2,headingRule);
            point2.setAlignment(Element.ALIGN_JUSTIFIED);
            document.add(point2);

            pointStr = "\nI am directed to intimate a sum of Rs. " + qBean.getRecoveryAmount() + " only is outstanding against you for occupation of Government Quarter No." + qBean.getBuildingNo() + " Type: " + qBean.getQuarterType() + " Unit: " + qBean.getQuarterUnit() + ", Bhubaneswar/Cuttack  till the date of vacation.";
            Paragraph point1 = new Paragraph(pointStr);
            point1.setAlignment(Element.ALIGN_JUSTIFIED);
            document.add(point1);

            pointStr2 = "\nI would, therefore, request you to take necessary steps to deposit the aforesaid amount at an early date for necessary adjustment of your rent ledger..\n\n";
            point2 = new Paragraph(pointStr2);
            point2.setAlignment(Element.ALIGN_JUSTIFIED);
            document.add(point2);
            PdfPTable table1 = new PdfPTable(3);
            table1.setWidths(new int[]{4, 1, 3});
            table1.setWidthPercentage(100);

            PdfPCell cell1 = null;

            cell1 = new PdfPCell(new Phrase("Bhubaneswar", standardRule));
            cell1.setBorder(Rectangle.NO_BORDER);
            table1.addCell(cell1);
            cell1 = new PdfPCell(new Phrase("", standardRule));
            cell1.setBorder(Rectangle.NO_BORDER);
            table1.addCell(cell1);
            cell1 = new PdfPCell(new Phrase("Yours faithfully,\n\n\n\n\nRent Officer\nG.A. & P.G. Department", standardRule));
            cell1.setBorder(Rectangle.NO_BORDER);
            cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell1);

            document.add(table1);

            Paragraph blank = new Paragraph("Copy to:", titlefontSmall);
           // document.add(blank);

            Paragraph rule3 = new Paragraph("\n", standardRule);
            rule3.setAlignment(Element.ALIGN_CENTER);
            document.add(rule3);

            Paragraph cpy = new Paragraph("\nCopy forwarded to Shri/Smt." + qBean.getFullName() + " Designation " + qBean.getDesignation() + " for information and necessary action.");
            cpy.setAlignment(Element.ALIGN_LEFT);
            //document.add(cpy);

            document.newPage();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }

    @RequestMapping(value = "UpdateNDCStatus", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView updateNDCStatus(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("QuarterBean") QuarterBean qtBean) {
        rentDAO.updateNDCStatus(qtBean);
        ModelAndView mav = new ModelAndView("redirect:/NDCApplicationList.htm");
        return mav;
    }

    @RequestMapping(value = "UploadFinalNDC", method = RequestMethod.GET)
    public ModelAndView UploadFinalNDC(ModelMap model, @ModelAttribute("QuarterBean") QuarterBean qBean, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response, @RequestParam Map<String, String> requestParams) {
        ModelAndView mav = null;
        int applicationId = Integer.parseInt(requestParams.get("applicationId"));
        String res = requestParams.get("result");
        mav = new ModelAndView("/Rent/UploadFinalNDC");
        mav.addObject("applicationId", applicationId);
        mav.addObject("result", res);
        return mav;
    }

    @RequestMapping(value = "SaveFinalNDC", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView SaveFinalNDC(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("QuarterBean") QuarterBean qtBean) {
        String dirPath = context.getInitParameter("FinalNDCPath");
        rentDAO.saveFinalNDC(qtBean, dirPath);
        ModelAndView mav = new ModelAndView("redirect:/UploadFinalNDC.htm?applicationId=" + qtBean.getApplicationId() + "&result=success");
        return mav;
    }

    @RequestMapping(value = "NDCDashboard", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView AdminDashboard(@ModelAttribute("LoginUserBean") LoginUserBean lub) {
        ModelAndView mav = new ModelAndView();
        String path = null;
        mav.addObject("fullName", lub.getLoginoffname());
        path = "/Rent/NDCDashboard";
        mav.setViewName(path);
        return mav;
    }

    @RequestMapping(value = "PSANDCApply", method = RequestMethod.GET)
    public ModelAndView PSANDCApply(ModelMap model, @ModelAttribute("QuarterBean") QuarterBean qBean, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response) {

        ModelAndView mav = null;
        QuarterBean qBean1 = new QuarterBean();
        qBean1.setFullName(lub.getLoginname());
        Users ue = rentDAO.getRentEmpDetail(lub.getLoginempid());
        mav = new ModelAndView("/Rent/PSANDCApply", "QuarterBean", qBean);
        mav.addObject("qBean1", qBean1);
        mav.addObject("user", ue);
        QuarterBean qBean2 = rentDAO.getNdcApplicationDetail(lub.getLoginempid());
        List deptlist = deptDAO.getDepartmentList();
        mav.addObject("deptlist", deptlist);
        mav.addObject("qBean", qBean2);
        return mav;
    }

    @RequestMapping(value = "SavePSAApplication", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView SavePSAApplication(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("QuarterBean") QuarterBean qtBean) {
        rentDAO.savePSAApplication(qtBean, lub.getLoginempid(), lub.getLoginspc());
        ModelAndView mav = new ModelAndView("redirect:/PSAApplicationList.htm");
        return mav;
    }

    @RequestMapping(value = "PSAApplicationList", method = RequestMethod.GET)
    public ModelAndView PSAApplicationList(ModelMap model, @ModelAttribute("QuarterBean") QuarterBean qBean, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response) {
        ModelAndView mav = null;
        //System.out.println("EmpID:"+lub.getLoginempid());
        List li = rentDAO.psaApplicationList(lub.getLoginempid());
        mav = new ModelAndView("/Rent/PSANDC");
        mav.addObject("applicationList", li);
        return mav;
    }

    @RequestMapping(value = "PSAAdminApplicationList", method = RequestMethod.GET)
    public ModelAndView PSAAdminApplicationList(ModelMap model, @ModelAttribute("QuarterBean") QuarterBean qBean, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response) {
        ModelAndView mav = null;
        //System.out.println("EmpID:"+lub.getLoginempid());
        List li = rentDAO.psaAdminApplicationList();
        mav = new ModelAndView("/Rent/PSAAdminApplicationList");
        mav.addObject("applicationList", li);
        return mav;
    }

    @RequestMapping(value = "GetBuildingNoDataJson")
    @ResponseBody
    public void getBuildingNoDataJson(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam Map<String, String> parameters) {
        response.setContentType("application/json");
        PrintWriter out = null;
        try {
            out = response.getWriter();
            String qtrunit = parameters.get("qtrunit");
            String qtrtype = parameters.get("qtrtype");
            //System.out.println("QtrUnit: "+qtrunit);
            List unitAreaList = rentDAO.getTypeWiseBuildingNo(qtrunit, qtrtype);
            JSONArray arr = new JSONArray(unitAreaList);
            JSONObject jobj = new JSONObject();
            jobj.put("unitAreaList", arr);
            out.write(jobj.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }
    @RequestMapping(value = "unitWiseQuarterTypeRentJson")
    @ResponseBody
    public void unitWiseQuarterTypeDataJson(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam Map<String, String> parameters) {
        response.setContentType("application/json");
        PrintWriter out = null;
        try {
            out = response.getWriter();
            String quarterunitarea = parameters.get("quarterunitarea");
            List unitAreaList = rentDAO.getUnitAreawiseQuarterTypeRent(quarterunitarea);
            JSONArray arr = new JSONArray(unitAreaList);
            JSONObject jobj = new JSONObject();
            jobj.put("unitAreaList", arr);
            out.write(jobj.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }    
@RequestMapping(value = "/SaveScrollData", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_VALUE)
    public Object saveScrollData(HttpServletResponse response, @RequestBody ScrollMain sMain) {
        JSONObject jobj = new JSONObject();
        String statusofleave = "";
        boolean ifexist = false;
        boolean status = false;
        boolean ifMoreThanMaxPeriod = false;
        String leaveType = "";
        try {
            statusofleave = sMain.getDeptCode();
            //jobj.append("status", status);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusofleave;
    }    
    @RequestMapping(value = "NDCPasswordChange", method = RequestMethod.GET)
    public ModelAndView NDCPasswordChange(ModelMap model, @ModelAttribute("QuarterBean") QuarterBean qBean, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response) {
        ModelAndView mav = null;
        mav = new ModelAndView("/Rent/NDCPasswordChange");
        return mav;
    }  
    @RequestMapping(value = "SaveNDCChangePassword", method = RequestMethod.POST)
    public ModelAndView SaveNDCPasswordChange(ModelMap model, @ModelAttribute("QuarterBean") QuarterBean qBean, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response) throws Exception {
        ModelAndView mav = new ModelAndView();
        mav.addObject("qBean", qBean);
       
        int pwdstatus = 0;
        Map responseResult = new HashMap();        
        if (qBean.getUserPassword() != null && !qBean.getUserPassword().equals("")) {
            if (qBean.getNewpassword().equalsIgnoreCase(qBean.getConfirmpassword())) {
                
                pwdstatus = rentDAO.modifyNDCPassword(lub.getLoginempid(), qBean.getUserPassword(), qBean.getNewpassword());
                if (pwdstatus == 1) {
                    responseResult.put("msg", "password changed successfully");
                } else if (pwdstatus == 0) {
                    responseResult.put("msg", "Invalid Password");
                } else if (pwdstatus == 2) {
                    responseResult.put("msg", "Password does not meet the policy");
                }
            } else {
                responseResult.put("msg", "New Password does not Match with Confirm Password");
            }
        }
        
        mav.addObject("msg", responseResult.get("msg"));
        mav.setViewName("/Rent/NDCPasswordChange");
        return mav;

    }
}
