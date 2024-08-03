/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.view;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import hrms.common.CommonFunctions;
import hrms.common.ParGrade;
import hrms.model.parmast.AbsenteeBean;
import hrms.model.parmast.AcceptingHelperBean;
import hrms.model.parmast.ParAchievement;
import hrms.model.parmast.ParApplyForm;
import hrms.model.parmast.Parauthorityhelperbean;
import hrms.model.parmast.ReportingHelperBean;
import hrms.model.parmast.ReviewingHelperBean;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.view.AbstractView;

/**
 *
 * @author Manas
 */
public class ParPdfView extends AbstractView {

    public ParPdfView() {
        setContentType("application/pdf");
    }

    @Override
    protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest hsr, HttpServletResponse response) throws Exception {
        ParApplyForm paf = (ParApplyForm) model.get("paf");
        String filePath = (String) model.get("filePath");
        String qrCodePathPAR = (String) model.get("qrCodePathPAR");
        
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();
        Font f1 = new Font();
        f1.setSize(10);
        f1.setFamily("Times New Roman");

        PdfPTable table = null;
        PdfPTable innertable = null;

        table = new PdfPTable(4);
        table.setWidths(new int[]{2, 3, 3, 3});
        table.setWidthPercentage(100);

        PdfPCell cell = null;
        PdfPCell innercell = null;
        String financialyear = "2019-20";
        String getfinancialyear = paf.getFiscalYear();

        cell = new PdfPCell(new Phrase("Performance Appraisal Report (PAR) for Group 'A' & 'B' officers of Govt. of Orissa", new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.BOLD)));
        cell.setColspan(4);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Transmission Record", new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD)));
        cell.setColspan(4);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(StringUtils.repeat("_", 93), f1));
        cell.setColspan(4);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("(To be filled in by Appraisee )", new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD)));
        cell.setColspan(4);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(4);
        cell.setFixedHeight(10);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        Phrase phrs = new Phrase();
        Chunk c1 = new Chunk("Financial Year : ", f1);
        Chunk c2 = new Chunk(paf.getFiscalYear(), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD | Font.UNDERLINE));
        Chunk c3 = new Chunk(" (for the period from ", f1);
        Chunk c4 = new Chunk(paf.getPrdFrmDate(), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD));
        Chunk c5 = new Chunk(" to ", f1);
        Chunk c6 = new Chunk(paf.getPrdToDate(), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD));
        Chunk c7 = new Chunk(")", f1);
        phrs.add(c1);
        phrs.add(c2);
        phrs.add(c3);
        phrs.add(c4);
        phrs.add(c5);
        phrs.add(c6);
        phrs.add(c7);
        cell = new PdfPCell(phrs);
        cell.setColspan(4);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(4);
        cell.setFixedHeight(20);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        phrs = new Phrase();
        c1 = new Chunk("Name & Designation of the Officer Reported Upon     ", f1);
        c2 = new Chunk(paf.getEmpName(), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD | Font.UNDERLINE));
        phrs.add(c1);
        phrs.add(c2);
        cell = new PdfPCell(phrs);
        cell.setColspan(4);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(4);
        cell.setFixedHeight(10);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", f1));
        cell.setColspan(2);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(paf.getApprisespc(), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
        cell.setColspan(2);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(4);
        cell.setFixedHeight(10);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Service and Group (A/B) to which the  Officer belongs ", f1));
        cell.setColspan(2);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(paf.getEmpService(), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD | Font.UNDERLINE)));
        cell.setColspan(2);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", f1));
        cell.setColspan(2);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Group - " + StringUtils.defaultString(paf.getEmpGroup()), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD | Font.UNDERLINE)));
        cell.setColspan(2);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(StringUtils.repeat("_", 93), f1));
        cell.setColspan(4);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(4);
        cell.setFixedHeight(10);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Details of Transmission / Movement of PAR", new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD)));
        cell.setColspan(4);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("(To be filled in at the time of transmission", new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD)));
        cell.setColspan(4);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("by respective officer/staff)", new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD)));
        cell.setColspan(4);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(4);
        cell.setFixedHeight(10);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Transmission by", f1));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Transmitted to whom (Name, Designation &Address)", f1));
        cell.setColspan(3);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Appraisee", f1));
        cell.setBorder(Rectangle.LEFT);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(paf.getEmpName(), f1));
        cell.setColspan(3);
        cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(Rectangle.LEFT);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(paf.getApprisespc(), f1));
        cell.setColspan(3);
        cell.setBorder(Rectangle.LEFT | Rectangle.BOTTOM | Rectangle.RIGHT);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Reporting\nAuthority", f1));
        cell.setBorder(Rectangle.LEFT | Rectangle.TOP | Rectangle.BOTTOM);
        table.addCell(cell);
        ArrayList reportAuthlist = paf.getReportingauth();
        Parauthorityhelperbean parhelper = null;
        String reportAuthName = "";
        int slno = 0;
        for (int i = 0; i < reportAuthlist.size(); i++) {
            parhelper = (Parauthorityhelperbean) reportAuthlist.get(i);
            slno = i + 1;
            if (reportAuthName.equals("")) {
                reportAuthName = slno + ". " + parhelper.getAuthorityname() + " (" + parhelper.getAuthorityspn() + ")\n(From:" + parhelper.getFromdt() + " To:" + parhelper.getTodt() + ")";
            } else {
                reportAuthName = reportAuthName + "\n\n" + slno + ". " + parhelper.getAuthorityname() + " (" + parhelper.getAuthorityspn() + ")\n(From:" + parhelper.getFromdt() + " To:" + parhelper.getTodt() + ")";
            }
        }
        cell = new PdfPCell(new Phrase(reportAuthName, new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)));
        cell.setColspan(3);
        cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.BOTTOM);
        table.addCell(cell);

        ArrayList reviewAuthlist = paf.getReviewingauth();
        parhelper = null;
        String reviewAuthName = "";
        slno = 0;
        for (int i = 0; i < reviewAuthlist.size(); i++) {
            parhelper = (Parauthorityhelperbean) reviewAuthlist.get(i);
            slno = i + 1;
            if (reviewAuthName.equals("")) {
                reviewAuthName = slno + ". " + parhelper.getAuthorityname() + " (" + parhelper.getAuthorityspn() + ")\n(From:" + parhelper.getFromdt() + " To:" + parhelper.getTodt() + ")";
            } else {
                reviewAuthName = reviewAuthName + "\n\n" + slno + ". " + parhelper.getAuthorityname() + " (" + parhelper.getAuthorityspn() + ")\n(From:" + parhelper.getFromdt() + " To:" + parhelper.getTodt() + ")";
            }
        }
        cell = new PdfPCell(new Phrase("Reviewing\nAuthority", f1));
        cell.setBorder(Rectangle.LEFT | Rectangle.TOP | Rectangle.BOTTOM);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(reviewAuthName, new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)));
        cell.setColspan(3);
        cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.BOTTOM);
        table.addCell(cell);

        ArrayList acceptAuthlist = paf.getAcceptingauth();
        parhelper = null;
        String acceptAuthName = "";
        slno = 0;
        for (int i = 0; i < acceptAuthlist.size(); i++) {
            parhelper = (Parauthorityhelperbean) acceptAuthlist.get(i);
            slno = i + 1;
            if (acceptAuthName.equals("")) {
                acceptAuthName = slno + ". " + parhelper.getAuthorityname() + " (" + parhelper.getAuthorityspn() + "))\n(From:" + parhelper.getFromdt() + " To:" + parhelper.getTodt() + ")";
            } else {
                acceptAuthName = acceptAuthName + "\n\n" + slno + ". " + parhelper.getAuthorityname() + " (" + parhelper.getAuthorityspn() + "))\n(From:" + parhelper.getFromdt() + " To:" + parhelper.getTodt() + ")";
            }
        }
        cell = new PdfPCell(new Phrase("Accepting \nAuthority", f1));
        cell.setBorder(Rectangle.LEFT | Rectangle.BOTTOM);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(acceptAuthName, new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)));
        cell.setColspan(3);
        cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.BOTTOM);
        table.addCell(cell);

        document.add(table);
        document.newPage();

        /*Second Page Start Here*/
        table = new PdfPTable(4);
        table.setWidths(new int[]{2, 3, 3, 3});
        table.setWidthPercentage(100);

        cell = new PdfPCell(new Phrase("PERFORMANCE APPRAISAL REPORT", new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.BOLD)));
        cell.setColspan(4);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("for", new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.NORMAL)));
        cell.setColspan(4);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Group A and Group B Officers of Govt. of Orissa.", new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.NORMAL)));
        cell.setColspan(4);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(4);
        cell.setFixedHeight(10);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        phrs = new Phrase();
        c1 = new Chunk("Report for the financial year ", f1);
        c2 = new Chunk(paf.getFiscalYear(), new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.BOLD));
        phrs.add(c1);
        phrs.add(c2);
        cell = new PdfPCell(phrs);
        cell.setColspan(4);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(4);
        cell.setFixedHeight(20);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        phrs = new Phrase();
        c1 = new Chunk("(  Period from ", f1);
        c2 = new Chunk(paf.getPrdFrmDate(), new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.BOLD));
        c3 = new Chunk(" to ", f1);
        c4 = new Chunk(paf.getPrdToDate(), new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.BOLD));
        c5 = new Chunk(")", f1);
        phrs.add(c1);
        phrs.add(c2);
        phrs.add(c3);
        phrs.add(c4);
        phrs.add(c5);
        cell = new PdfPCell(phrs);
        cell.setColspan(4);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(4);
        cell.setFixedHeight(20);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("PERSONAL DATA", new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.NORMAL)));
        cell.setColspan(4);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(Rectangle.TOP | Rectangle.LEFT | Rectangle.RIGHT);
        cell.setBorderWidth(1f);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(StringUtils.repeat(" ", 10) + "PART-I", f1));
        cell.setBorder(Rectangle.LEFT);
        cell.setBorderWidth(1f);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(StringUtils.repeat(" ", 33) + "(To be filled in by the Appraisee )", f1));
        cell.setColspan(3);
        cell.setBorder(Rectangle.RIGHT);
        cell.setBorderWidth(1f);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(4);
        cell.setFixedHeight(20);
        cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
        cell.setBorderWidth(1f);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" 1. Full Name of the Officer:", f1));
        cell.setColspan(2);
        cell.setBorder(Rectangle.LEFT);
        cell.setBorderWidth(1f);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(paf.getEmpName(), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)));
        cell.setColspan(2);
        cell.setBorder(Rectangle.RIGHT);
        cell.setBorderWidth(1f);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(3);
        cell.setFixedHeight(10);
        cell.setBorder(Rectangle.LEFT);
        cell.setBorderWidth(1f);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setRowspan(3);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(Rectangle.RIGHT);
        cell.setBorderWidth(1f);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" 2. Date of Birth:", f1));
        cell.setColspan(2);
        cell.setBorder(Rectangle.LEFT);
        cell.setBorderWidth(1f);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(paf.getDob(), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setBorderWidth(1f);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(3);
        cell.setFixedHeight(10);
        cell.setBorder(Rectangle.LEFT);
        cell.setBorderWidth(1f);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" 3. Service to which the Officer belongs:", f1));
        cell.setColspan(2);
        cell.setBorder(Rectangle.LEFT);
        cell.setBorderWidth(1f);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(paf.getEmpService(), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)));
        cell.setColspan(2);
        cell.setBorder(Rectangle.RIGHT);
        cell.setBorderWidth(1f);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(4);
        cell.setFixedHeight(10);
        cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
        cell.setBorderWidth(1f);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" 4. Group to which the Officer belongs(A or B):", f1));
        cell.setColspan(2);
        cell.setBorder(Rectangle.LEFT);
        cell.setBorderWidth(1f);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(paf.getEmpGroup(), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)));
        cell.setColspan(2);
        cell.setBorder(Rectangle.RIGHT);
        cell.setBorderWidth(1f);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(4);
        cell.setFixedHeight(10);
        cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
        cell.setBorderWidth(1f);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" 5. Designation  during the period of Report:", f1));
        cell.setColspan(2);
        cell.setBorder(Rectangle.LEFT);
        cell.setBorderWidth(1f);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(paf.getApprisespc(), new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.NORMAL)));
        cell.setColspan(2);
        cell.setBorder(Rectangle.RIGHT);
        cell.setBorderWidth(1f);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(4);
        cell.setFixedHeight(10);
        cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
        cell.setBorderWidth(1f);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" 6. Office to which posted with Head Quarters:", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)));
        cell.setColspan(2);
        cell.setBorder(Rectangle.LEFT);
        cell.setBorderWidth(1f);
        table.addCell(cell);
        String office = "";
        if (paf.getSltHeadQuarter() != null && !paf.getSltHeadQuarter().equals("")) {
            office = paf.getEmpOffice() + "," + paf.getSltHeadQuarter();
        } else {
            office = paf.getEmpOffice();
        }
        cell = new PdfPCell(new Phrase(office, new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.NORMAL)));
        cell.setColspan(2);
        cell.setBorder(Rectangle.RIGHT);
        cell.setBorderWidth(1f);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(4);
        cell.setFixedHeight(10);
        cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
        cell.setBorderWidth(1f);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" 7. Period(s) of absence (on leave, training etc.,\n    if 30 days or more).Please mention date(s):", f1));
        cell.setColspan(2);
        cell.setBorder(Rectangle.LEFT);
        cell.setBorderWidth(1f);
        table.addCell(cell);

        AbsenteeBean pab = null;
        ArrayList leaveAbsentee = paf.getLeaveAbsentee();
        String leaveFrmDt = "";
        String leaveToDt = "";
        for (int i = 0; i < leaveAbsentee.size(); i++) {
            pab = (AbsenteeBean) leaveAbsentee.get(i);
            innertable = new PdfPTable(2);
            innertable.setWidths(new int[]{2, 2});
            innertable.setWidthPercentage(100);

            leaveFrmDt = pab.getFromDate();
            leaveToDt = pab.getToDate();
        }
        if ((leaveFrmDt != null && !leaveFrmDt.equals("")) && (leaveToDt != null && !leaveToDt.equals(""))) {
            cell = new PdfPCell(new Phrase("From " + leaveFrmDt + " to " + leaveToDt, f1));
        } else {
            cell = new PdfPCell();
        }
        cell.setColspan(2);
        cell.setBorder(Rectangle.RIGHT);
        cell.setBorderWidth(1f);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(4);
        cell.setFixedHeight(10);
        cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
        cell.setBorderWidth(1f);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" 8. Name & Designation of the Reporting Authority\n    and period worked under him/her:", f1));
        cell.setColspan(2);
        cell.setBorder(Rectangle.LEFT);
        cell.setBorderWidth(1f);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(reportAuthName, new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.NORMAL)));
        cell.setColspan(2);
        cell.setBorder(Rectangle.RIGHT);
        cell.setBorderWidth(1f);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", f1));
        cell.setColspan(2);
        cell.setBorder(Rectangle.LEFT);
        cell.setBorderWidth(1f);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(StringUtils.repeat("_", 47), f1));
        cell.setColspan(2);
        cell.setBorder(Rectangle.RIGHT);
        cell.setBorderWidth(1f);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(4);
        cell.setFixedHeight(10);
        cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
        cell.setBorderWidth(1f);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" 9. Name & Designation of the Reviewing Authority\n    and period worked under him/her:", f1));
        cell.setColspan(2);
        cell.setBorder(Rectangle.LEFT);
        cell.setBorderWidth(1f);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(reviewAuthName, new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.NORMAL)));
        cell.setColspan(2);
        cell.setBorder(Rectangle.RIGHT);
        cell.setBorderWidth(1f);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", f1));
        cell.setColspan(2);
        cell.setBorder(Rectangle.LEFT);
        cell.setBorderWidth(1f);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(StringUtils.repeat("_", 47), f1));
        cell.setColspan(2);
        cell.setBorder(Rectangle.RIGHT);
        cell.setBorderWidth(1f);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(4);
        cell.setFixedHeight(10);
        cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
        cell.setBorderWidth(1f);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" 10. Name & Designation of the Accepting Authority\n    and period worked under him/her:", f1));
        cell.setColspan(2);
        cell.setBorder(Rectangle.LEFT);
        cell.setBorderWidth(1f);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(acceptAuthName, new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.NORMAL)));
        cell.setColspan(2);
        cell.setBorder(Rectangle.RIGHT);
        cell.setBorderWidth(1f);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", f1));
        cell.setColspan(2);
        cell.setBorder(Rectangle.LEFT);
        cell.setBorderWidth(1f);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(StringUtils.repeat("_", 47), f1));
        cell.setColspan(2);
        cell.setBorder(Rectangle.RIGHT);
        cell.setBorderWidth(1f);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(4);
        cell.setFixedHeight(40);
        cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
        cell.setBorderWidth(1f);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", f1));
        cell.setColspan(2);
        cell.setBorder(Rectangle.LEFT);
        cell.setBorderWidth(1f);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Signature of the Appraisee", f1));
        cell.setColspan(2);
        cell.setBorder(Rectangle.RIGHT);
        cell.setBorderWidth(1f);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(4);
        cell.setFixedHeight(40);
        cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
        cell.setBorderWidth(1f);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(4);
        cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.BOTTOM);
        cell.setBorderWidth(1f);
        table.addCell(cell);

        document.add(table);
        document.newPage();

        /*Third Page Start Here*/
        table = new PdfPTable(5);
        table.setWidths(new int[]{1, 4, 5, 3, 3});
        table.setWidthPercentage(100);

        cell = new PdfPCell();
        cell.setColspan(5);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("    PART-II" + StringUtils.repeat(" ", 51) + "SELF-APPRAISAL", new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD)));
        cell.setColspan(5);
        cell.setBorder(Rectangle.LEFT | Rectangle.TOP | Rectangle.RIGHT);
        cell.setBorderWidth(1f);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("(To be filled in by the Appraisee )", f1));
        cell.setColspan(5);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
        cell.setBorderWidth(1f);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(5);
        cell.setFixedHeight(30);
        cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
        cell.setBorderWidth(1f);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" 1. Brief description of duties/tasks entrusted.(in about 200 words)", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
        cell.setColspan(5);
        cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
        cell.setBorderWidth(1f);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(Rectangle.LEFT);
        cell.setBorderWidth(1f);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(CommonFunctions.htmlToText(paf.getSelfappraisal()), f1));
        cell.setColspan(4);
        cell.setBorder(Rectangle.RIGHT);
        cell.setBorderWidth(1f);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(5);
        cell.setFixedHeight(100);
        cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
        cell.setBorderWidth(1f);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(StringUtils.repeat("_", 93), f1));
        cell.setColspan(5);
        cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
        cell.setBorderWidth(1f);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" 2. Physical/Financial Targets & Achievements", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
        cell.setColspan(5);
        cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
        cell.setBorderWidth(1f);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(StringUtils.repeat("_", 93), f1));
        cell.setColspan(5);
        cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
        cell.setBorderWidth(1f);
        table.addCell(cell);

        innertable = new PdfPTable(5);
        innertable.setWidths(new int[]{2, 2, 2, 2, 2});
        innertable.setWidthPercentage(100);

        innercell = new PdfPCell(new Phrase("SL. No", f1));
        innercell.setHorizontalAlignment(Element.ALIGN_CENTER);

        innertable.addCell(innercell);
        innercell = new PdfPCell(new Phrase("Task", f1));
        innercell.setHorizontalAlignment(Element.ALIGN_CENTER);

        innertable.addCell(innercell);
        innercell = new PdfPCell(new Phrase("Target", f1));
        innercell.setHorizontalAlignment(Element.ALIGN_CENTER);

        innertable.addCell(innercell);
        innercell = new PdfPCell(new Phrase("Achievement", f1));
        innercell.setHorizontalAlignment(Element.ALIGN_CENTER);

        innertable.addCell(innercell);
        innercell = new PdfPCell(new Phrase("% of Achievement", f1));
        innercell.setHorizontalAlignment(Element.ALIGN_CENTER);
        innertable.addCell(innercell);

        cell = new PdfPCell(innertable);
        cell.setColspan(5);
        cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
        cell.setBorderWidth(1f);
        table.addCell(cell);

        ArrayList achievementList = paf.getAchivementList();
        if (achievementList != null && achievementList.size() > 0) {
            ParAchievement ab = null;
            for (int i = 0; i < achievementList.size(); i++) {
                ab = (ParAchievement) achievementList.get(i);
                innertable = new PdfPTable(5);
                innertable.setWidths(new int[]{2, 2, 2, 2, 2});
                innertable.setWidthPercentage(100);
                int j = i + 1;
                innercell = new PdfPCell(new Phrase(j + "", f1));
                innercell.setBorder(Rectangle.NO_BORDER);
                innercell.setHorizontalAlignment(Element.ALIGN_CENTER);
                innertable.addCell(innercell);
                innercell = new PdfPCell(new Phrase(ab.getTask(), f1));
                innercell.setBorder(Rectangle.NO_BORDER);
                innercell.setHorizontalAlignment(Element.ALIGN_CENTER);
                innertable.addCell(innercell);
                innercell = new PdfPCell(new Phrase(ab.getTarget(), f1));
                innercell.setBorder(Rectangle.NO_BORDER);
                innercell.setHorizontalAlignment(Element.ALIGN_CENTER);
                innertable.addCell(innercell);
                innercell = new PdfPCell(new Phrase(ab.getAchievement(), f1));
                innercell.setBorder(Rectangle.NO_BORDER);
                innercell.setHorizontalAlignment(Element.ALIGN_CENTER);
                innertable.addCell(innercell);
                innercell = new PdfPCell(new Phrase(ab.getPercentAchievement(), f1));
                innercell.setBorder(Rectangle.NO_BORDER);
                innercell.setHorizontalAlignment(Element.ALIGN_CENTER);
                innertable.addCell(innercell);

                cell = new PdfPCell(innertable);
                cell.setColspan(5);
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                cell.setBorderWidth(1f);
                table.addCell(cell);
            }
        }

        cell = new PdfPCell(new Phrase(" 3. Significant work, if any, done", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
        cell.setColspan(5);
        cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
        cell.setBorderWidth(1f);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(5);
        cell.setFixedHeight(10);
        cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
        cell.setBorderWidth(1f);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(Rectangle.LEFT);
        cell.setBorderWidth(1f);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(CommonFunctions.htmlToText(paf.getSpecialcontribution()), f1));
        cell.setColspan(4);
        cell.setBorder(Rectangle.RIGHT);
        cell.setBorderWidth(1f);
        table.addCell(cell);

        if (paf.getReason() != null && !paf.getReason().equals("")) {
            //Reason Start
            cell = new PdfPCell(new Phrase(" 4. Hindrance", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
            cell.setColspan(5);
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(5);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setBorder(Rectangle.LEFT);
            cell.setBorderWidth(1f);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(CommonFunctions.htmlToText(paf.getReason()), f1));
            cell.setColspan(4);
            cell.setBorder(Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);
            //Reason Stop
            cell = new PdfPCell();
            cell.setColspan(5);
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.BOTTOM);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            document.add(table);

            //Third Page
            table = new PdfPTable(5);
            table.setWidths(new int[]{1, 4, 5, 3, 3});
            table.setWidthPercentage(100);

            cell = new PdfPCell();
            cell.setColspan(5);
            cell.setFixedHeight(50);
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.TOP);
            cell.setBorderWidth(1f);
            table.addCell(cell);
        } else {
            cell = new PdfPCell();
            cell.setColspan(5);
            cell.setFixedHeight(50);
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);
        }

        cell = new PdfPCell();
        cell.setColspan(5);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Place: " + StringUtils.defaultString(paf.getPlace()), f1));
        cell.setColspan(2);
        cell.setBorder(Rectangle.LEFT);
        cell.setBorderWidth(1f);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(" Date " + StringUtils.defaultString(paf.getSubmittedon()), f1));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Signature of Appraisee", f1));
        cell.setColspan(2);
        cell.setBorder(Rectangle.RIGHT);
        cell.setBorderWidth(1f);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(5);
        cell.setFixedHeight(30);
        cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.BOTTOM);
        cell.setBorderWidth(1f);
        table.addCell(cell);

        document.add(table);
        document.newPage();

        table = new PdfPTable(4);
        table.setWidths(new int[]{4, 2, 4, 2});
        table.setWidthPercentage(100);

        cell = new PdfPCell(new Phrase("Remarks of Reporting Authority", CommonFunctions.getDesired_PDF_Font(9, true, true)));
        cell.setColspan(4);
        cell.setFixedHeight(20);
        cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.TOP);
        cell.setBorderWidth(1f);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        ReportingHelperBean rhb = null;
        for (int i = 0; i < paf.getReportingdata().size(); i++) {
            rhb = (ReportingHelperBean) paf.getReportingdata().get(i);
            if (i > 0) {
                cell = new PdfPCell();
                cell.setColspan(4);
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                cell.setBorderWidth(1f);
                table.addCell(cell);
            }

            if (rhb.getIscurrentreporting().equals("Y") || rhb.getIsreportingcompleted().equals("Y")) {

                cell = new PdfPCell(new Phrase(rhb.getReportingauthName(), CommonFunctions.getDesired_PDF_Font(9, false, true)));
                cell.setColspan(4);
                cell.setFixedHeight(20);
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                cell.setBorderWidth(1f);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("1. Assessment of work output, attributes and functional competencies.(This should be on a relative scale of 1-5, with 1 referring to the lowest level and 5 to the highest level. Please indicate your rating for the officer against each item.)", CommonFunctions.getDesired_PDF_Font(10, false, false)));
                cell.setColspan(4);
                cell.setFixedHeight(40);
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                cell.setBorderWidth(1f);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("Description", CommonFunctions.getDesired_PDF_Font(9, false, true)));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                cell.setBorderWidth(1f);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("Rating", CommonFunctions.getDesired_PDF_Font(9, false, true)));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                cell.setBorderWidth(1f);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("Description", CommonFunctions.getDesired_PDF_Font(9, false, true)));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                cell.setBorderWidth(1f);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("Rating", CommonFunctions.getDesired_PDF_Font(9, false, true)));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                cell.setBorderWidth(1f);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("(a)  Attitude to work :", CommonFunctions.getDesired_PDF_Font(9, false, false)));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                cell.setBorderWidth(1f);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(rhb.getRatingattitude1(), CommonFunctions.getDesired_PDF_Font(9, false, false)));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                cell.setBorderWidth(1f);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("(f) Co-ordination ability :", CommonFunctions.getDesired_PDF_Font(9, false, false)));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                cell.setBorderWidth(1f);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(rhb.getRatingcoordination1(), CommonFunctions.getDesired_PDF_Font(9, false, false)));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                cell.setBorderWidth(1f);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("(b)  Sense of responsibility:", CommonFunctions.getDesired_PDF_Font(9, false, false)));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                cell.setBorderWidth(1f);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(rhb.getRatingresponsibility1(), CommonFunctions.getDesired_PDF_Font(9, false, false)));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                cell.setBorderWidth(1f);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("(g) Ability to work in a team:", CommonFunctions.getDesired_PDF_Font(9, false, false)));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                cell.setBorderWidth(1f);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(rhb.getTeamworkrating1(), CommonFunctions.getDesired_PDF_Font(9, false, false)));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                cell.setBorderWidth(1f);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("(c)  Communication skill :", CommonFunctions.getDesired_PDF_Font(9, false, false)));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                cell.setBorderWidth(1f);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(rhb.getRatingcomskill1(), CommonFunctions.getDesired_PDF_Font(9, false, false)));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                cell.setBorderWidth(1f);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("(h) Knowledge of Rules/Procedures/ IT  Skills/ Relevant Subject:", CommonFunctions.getDesired_PDF_Font(9, false, false)));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                cell.setBorderWidth(1f);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(rhb.getRatingitskill1(), CommonFunctions.getDesired_PDF_Font(9, false, false)));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                cell.setBorderWidth(1f);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("(d)  Leadership Qualities :", CommonFunctions.getDesired_PDF_Font(9, false, false)));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                cell.setBorderWidth(1f);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(rhb.getRatingleadership1(), CommonFunctions.getDesired_PDF_Font(9, false, false)));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                cell.setBorderWidth(1f);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("(i) Initiative :", CommonFunctions.getDesired_PDF_Font(9, false, false)));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                cell.setBorderWidth(1f);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(rhb.getRatinginitiative1(), CommonFunctions.getDesired_PDF_Font(9, false, false)));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                cell.setBorderWidth(1f);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("(e)  Decision-making ability :", CommonFunctions.getDesired_PDF_Font(9, false, false)));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                cell.setBorderWidth(1f);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(rhb.getRatingdecisionmaking1(), CommonFunctions.getDesired_PDF_Font(9, false, false)));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                cell.setBorderWidth(1f);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("(j) Quality of Work :", CommonFunctions.getDesired_PDF_Font(9, false, false)));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                cell.setBorderWidth(1f);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(rhb.getRatequalityofwork1(), CommonFunctions.getDesired_PDF_Font(9, false, false)));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                cell.setBorderWidth(1f);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell();
                cell.setColspan(4);
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                cell.setBorderWidth(1f);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("2.(i) General Assessment (Please give an overall assessment of the officer including   his/her   attitude towards  S.T/S.C/Weaker Sections &  relation  with public:)", CommonFunctions.getDesired_PDF_Font(10, false, false)));
                cell.setColspan(4);
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                cell.setBorderWidth(1f);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(StringUtils.repeat(" ", 10) + StringUtils.defaultString(rhb.getAuthNote()), CommonFunctions.getDesired_PDF_Font(9, false, false)));
                cell.setColspan(4);
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                cell.setBorderWidth(1f);
                table.addCell(cell);

                if (!paf.getFiscalYear().equals("2014-15") && !paf.getFiscalYear().equals("2015-16") && !paf.getFiscalYear().equals("2016-17") && !paf.getFiscalYear().equals("2017-18") && !paf.getFiscalYear().equals("2018-19")) {
                    cell = new PdfPCell(new Phrase("(ii) Assessment Of Performance Of 5t (Out Of 20%)", CommonFunctions.getDesired_PDF_Font(10, false, false)));
                    cell.setColspan(4);
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                    cell.setBorderWidth(1f);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("(a) 10% on 5T charter of Department: ", CommonFunctions.getDesired_PDF_Font(10, false, false)));
                    cell.setColspan(4);
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                    cell.setBorderWidth(1f);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(StringUtils.repeat(" ", 10) + StringUtils.defaultString(rhb.getFiveTChartertenpercent()), CommonFunctions.getDesired_PDF_Font(9, false, false)));
                    cell.setColspan(4);
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                    cell.setBorderWidth(1f);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("(b)  5% on 5T charter of Government:", CommonFunctions.getDesired_PDF_Font(10, false, false)));
                    cell.setColspan(4);
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                    cell.setBorderWidth(1f);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(StringUtils.repeat(" ", 10) + StringUtils.defaultString(rhb.getFiveTChartertenpercent()), CommonFunctions.getDesired_PDF_Font(9, false, false)));
                    cell.setColspan(4);
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                    cell.setBorderWidth(1f);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("(c)   5% on Mo Sarkar:", CommonFunctions.getDesired_PDF_Font(10, false, false)));
                    cell.setColspan(4);
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                    cell.setBorderWidth(1f);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(StringUtils.repeat(" ", 10) + StringUtils.defaultString(rhb.getFiveTComponentmoSarkar()), CommonFunctions.getDesired_PDF_Font(9, false, false)));
                    cell.setColspan(4);
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                    cell.setBorderWidth(1f);
                    table.addCell(cell);
                }

                cell = new PdfPCell(new Phrase("3. Inadequacies, deficiencies or shortcomings, if any (Remarks to be treated as adverse )", CommonFunctions.getDesired_PDF_Font(10, false, false)));
                cell.setColspan(4);
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                cell.setBorderWidth(1f);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(StringUtils.repeat(" ", 10) + StringUtils.defaultString(rhb.getInadequaciesNote()), CommonFunctions.getDesired_PDF_Font(9, false, false)));
                cell.setColspan(4);
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                cell.setBorderWidth(1f);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("4. Integrity (If integrity is doubtful or  adverse please write \"Not certified\" in the space below and justify your remarks in box 4 above)", CommonFunctions.getDesired_PDF_Font(10, false, false)));
                cell.setColspan(4);
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                cell.setBorderWidth(1f);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(StringUtils.repeat(" ", 10) + StringUtils.defaultString(rhb.getIntegrityNote()), CommonFunctions.getDesired_PDF_Font(9, false, false)));
                cell.setColspan(4);
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                cell.setBorderWidth(1f);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("5. Overall Grading", CommonFunctions.getDesired_PDF_Font(10, false, false)));
                cell.setColspan(4);
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                cell.setBorderWidth(1f);
                table.addCell(cell);

                if (paf.getLoginUserType() != null && paf.getLoginUserType().equals("A")) {
                    cell = new PdfPCell(new Phrase(StringUtils.repeat(" ", 10) + StringUtils.defaultString("**********"), CommonFunctions.getDesired_PDF_Font(9, false, false)));
                    cell.setColspan(4);
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                    cell.setBorderWidth(1f);
                    table.addCell(cell);
                } else if (paf.getLoginUserType() != null && paf.getLoginUserType().equals("G"))  {
                    cell = new PdfPCell(new Phrase(StringUtils.repeat(" ", 10) + StringUtils.defaultString(rhb.getSltGrading1()), CommonFunctions.getDesired_PDF_Font(9, false, false)));
                    cell.setColspan(4);
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                    cell.setBorderWidth(1f);
                    table.addCell(cell);
                } 

                cell = new PdfPCell(new Phrase("6. For  Overall Grading  \"Below Average\" /  \"Outstanding\"  please provide justification in the   space below.:", CommonFunctions.getDesired_PDF_Font(10, false, false)));
                cell.setColspan(4);
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                cell.setBorderWidth(1f);
                table.addCell(cell);

                if (paf.getLoginUserType() != null && paf.getLoginUserType().equals("A")) {
                    cell = new PdfPCell(new Phrase(StringUtils.repeat(" ", 10) + StringUtils.defaultString("**********"), CommonFunctions.getDesired_PDF_Font(9, false, false)));
                    cell.setColspan(4);
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                    cell.setBorderWidth(1f);
                    table.addCell(cell);
                } else if (paf.getLoginUserType() != null && paf.getLoginUserType().equals("G")) {
                    cell = new PdfPCell(new Phrase(StringUtils.repeat(" ", 10) + StringUtils.defaultString(rhb.getGradingNote()), CommonFunctions.getDesired_PDF_Font(9, false, false)));
                    cell.setColspan(4);
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                    cell.setBorderWidth(1f);
                    table.addCell(cell);
                } 

                cell = new PdfPCell(new Phrase(StringUtils.defaultString(rhb.getReportingauthName()), CommonFunctions.getDesired_PDF_Font(9, true, false)));
                cell.setColspan(4);
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                cell.setBorderWidth(1f);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("Submitted on- " + StringUtils.defaultString(rhb.getSubmittedon()), CommonFunctions.getDesired_PDF_Font(9, false, false)));
                cell.setColspan(4);
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.BOTTOM);
                cell.setBorderWidth(1f);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table.addCell(cell);
            }
        }
        cell = new PdfPCell(new Phrase("Remarks of Reviewing Authority", CommonFunctions.getDesired_PDF_Font(9, true, true)));
        cell.setColspan(4);
        cell.setFixedHeight(20);
        cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
        cell.setBorderWidth(1f);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        ReviewingHelperBean rvb = null;
        for (int i = 0; i < paf.getReviewingdata().size(); i++) {
            rvb = (ReviewingHelperBean) paf.getReviewingdata().get(i);
            cell = new PdfPCell(new Phrase(rvb.getReviewingauthName(), CommonFunctions.getDesired_PDF_Font(9, false, true)));
            cell.setColspan(4);
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            if (rvb.getIsreviewingcompleted() != null && rvb.getIsreviewingcompleted().equals("Y")) {

                cell = new PdfPCell(new Phrase("1. Please Indicate if you agree with the general assessment/ adverse remarks/ overall grading  made by the   Reporting Authority, and give your assessment.", CommonFunctions.getDesired_PDF_Font(10, false, false)));
                cell.setColspan(4);
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                cell.setBorderWidth(1f);
                table.addCell(cell);

                if (paf.getLoginUserType() != null && paf.getLoginUserType().equals("A")) {
                    cell = new PdfPCell(new Phrase(StringUtils.repeat(" ", 10) + "**********", CommonFunctions.getDesired_PDF_Font(9, false, false)));
                    cell.setColspan(4);
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                    cell.setBorderWidth(1f);
                    table.addCell(cell);
                } else if (paf.getLoginUserType() != null && paf.getLoginUserType().equals("G")) {
                    cell = new PdfPCell(new Phrase(StringUtils.repeat(" ", 10) + StringUtils.defaultString(rvb.getReviewingNote()), CommonFunctions.getDesired_PDF_Font(9, false, false)));
                    cell.setColspan(4);
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                    cell.setBorderWidth(1f);
                    table.addCell(cell);
                } 

                if (paf.getLoginUserType() != null && paf.getLoginUserType().equals("A")) {
                    cell = new PdfPCell(new Phrase("2. Overall Grading Given By Reviewing Authority  :" + "**********", CommonFunctions.getDesired_PDF_Font(10, false, false)));
                    cell.setColspan(4);
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                    cell.setBorderWidth(1f);
                    table.addCell(cell);
                } else if (paf.getLoginUserType() != null && paf.getLoginUserType().equals("G")){
                    cell = new PdfPCell(new Phrase("2. Overall Grading Given By Reviewing Authority  :" + ParGrade.getGradingName(rvb.getSltReviewGrading()), CommonFunctions.getDesired_PDF_Font(10, false, false)));
                    cell.setColspan(4);
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                    cell.setBorderWidth(1f);
                    table.addCell(cell);
                } 

                cell = new PdfPCell(new Phrase(StringUtils.defaultString(rvb.getReviewingauthName()), CommonFunctions.getDesired_PDF_Font(9, true, false)));
                cell.setColspan(4);
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                cell.setBorderWidth(1f);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("Submitted on- " + rvb.getSubmittedon(), CommonFunctions.getDesired_PDF_Font(9, false, false)));
                cell.setColspan(4);
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                cell.setBorderWidth(1f);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table.addCell(cell);

                cell = new PdfPCell();
                cell.setColspan(4);
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.BOTTOM);
                cell.setBorderWidth(1f);
                table.addCell(cell);
            }
        }

        cell = new PdfPCell(new Phrase("Remarks of Accepting Authority", CommonFunctions.getDesired_PDF_Font(9, true, true)));
        cell.setColspan(4);
        cell.setFixedHeight(20);
        cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
        cell.setBorderWidth(1f);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        AcceptingHelperBean ahb = null;
        for (int i = 0; i < paf.getAcceptingdata().size(); i++) {
            ahb = (AcceptingHelperBean) paf.getAcceptingdata().get(i);

            cell = new PdfPCell(new Phrase(ahb.getAcceptingauthName(), CommonFunctions.getDesired_PDF_Font(9, false, true)));
            cell.setColspan(4);
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("1. Accepting Note.", CommonFunctions.getDesired_PDF_Font(10, false, false)));
            cell.setColspan(4);
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);
            
            
            if (paf.getLoginUserType() != null && paf.getLoginUserType().equals("A")) {
                cell = new PdfPCell(new Phrase(StringUtils.repeat(" ", 10) + "**********", CommonFunctions.getDesired_PDF_Font(10, false, false)));
                cell.setColspan(4);
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                cell.setBorderWidth(1f);
                table.addCell(cell);
            } else if (paf.getLoginUserType() != null && paf.getLoginUserType().equals("G")) {
                cell = new PdfPCell(new Phrase(StringUtils.repeat(" ", 10) + StringUtils.defaultString(ahb.getAcceptingNote()), CommonFunctions.getDesired_PDF_Font(10, false, false)));
                cell.setColspan(4);
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                cell.setBorderWidth(1f);
                table.addCell(cell);
            } 

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);

            if (paf.getLoginUserType() != null && paf.getLoginUserType().equals("A")) {
                cell = new PdfPCell(new Phrase("2. Overall Grading Given By Accepting Authority  :" + "**********", CommonFunctions.getDesired_PDF_Font(10, false, false)));
                cell.setColspan(4);
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                cell.setBorderWidth(1f);
                table.addCell(cell);
            } else if (paf.getLoginUserType() != null && paf.getLoginUserType().equals("G")) {
                cell = new PdfPCell(new Phrase("2. Overall Grading Given By Accepting Authority  :" + ParGrade.getGradingName(ahb.getSltAcceptingGrading()), CommonFunctions.getDesired_PDF_Font(10, false, false)));
                cell.setColspan(4);
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                cell.setBorderWidth(1f);
                table.addCell(cell);
            } 

            cell = new PdfPCell(new Phrase(StringUtils.defaultString(ahb.getAcceptingauthName()), CommonFunctions.getDesired_PDF_Font(9, true, false)));
            cell.setColspan(4);
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Submitted on- " + StringUtils.defaultString(ahb.getSubmittedon()), CommonFunctions.getDesired_PDF_Font(9, false, false)));
            cell.setColspan(4);
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
        }
        cell = new PdfPCell();
        cell.setColspan(4);
        cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.BOTTOM);
        cell.setBorderWidth(1f);
        table.addCell(cell);
        
        document.add(table);

        table = new PdfPTable(1);
        table.setWidths(new int[]{10});
        table.setWidthPercentage(100);
        cell = new PdfPCell(new Phrase("", f1));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setFixedHeight(20);
        table.addCell(cell);
        document.add(table);

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss aa");
        Calendar cal = Calendar.getInstance();
        //String IP = CommonFunctions.getISPIPAddress();
        //String[] IpAddress = CommonFunctions.getIpAndHost(request);

        table = new PdfPTable(6);
        table.setWidths(new int[]{6, 3, 6, 3, 6, 3});
        table.setWidthPercentage(100);

        cell = new PdfPCell(new Phrase("PDF Downloaded on :", f1));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(dateFormat.format(cal.getTime()) + "", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
        cell.setFixedHeight(10);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Downloaded From IP :", f1));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(paf.getDownloadedByIp(), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
        cell.setFixedHeight(10);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Downloaded By ID :", f1));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(paf.getDownloadedByName() + "(" + paf.getDownloadedById() + ")", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
        //cell.setFixedHeight(10);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", f1));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        
        document.add(table);
        
        table = new PdfPTable(1);
        table.setWidths(new int[]{10});
        table.setWidthPercentage(100);
        cell = new PdfPCell(new Phrase("", f1));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setBorder(Rectangle.NO_BORDER);
        //cell.setFixedHeight(20);
        table.addCell(cell);
        document.add(table);


        table = new PdfPTable(2);
        table.setWidths(new int[]{6, 3});
        table.setWidthPercentage(100);
        
        String parFilePath = qrCodePathPAR;

        String storedpath = qrCodePathPAR + paf.getFiscalYear() + CommonFunctions.getResourcePath();
        String qrCodeurl = storedpath + paf.getEmpId() + "-" + paf.getParId() + ".png";
        File fi = null;
        fi = new File(qrCodeurl);
        Image img2 = null;
        

        if (fi.exists()) {
            img2 = Image.getInstance(qrCodeurl);
            img2.setBorder(Rectangle.BOX);
        } else {
            img2 = Image.getInstance(qrCodePathPAR + "NotSubmitted.png");
        }

        if (img2 != null) {
            img2.scaleToFit(100f, 80f);
            cell = new PdfPCell(img2);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell();
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
        }

        document.add(table);
        document.close();
    }
}
