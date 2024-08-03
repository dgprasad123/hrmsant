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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.view.AbstractView;

/**
 *
 * @author Manas
 */
public class SiParPdfView extends AbstractView {

    public SiParPdfView() {
        setContentType("application/pdf");
    }

    @Override
    protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest hsr, HttpServletResponse response) throws Exception {
        ParApplyForm paf = (ParApplyForm) model.get("paf");
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
        String financialyear="2019-20";
        String getfinancialyear = paf.getFiscalYear();

        cell = new PdfPCell(new Phrase("Performance Appraisal Report (PAR) for SI & Equivalent Ranks (Group - B)", new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.BOLD)));
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

        cell = new PdfPCell(new Phrase("(To be filled in at the time of transmission by respective officer / staff)", new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD)));
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

        cell = new PdfPCell(new Phrase("SI & Equivalent Ranks (Group - B) Officers of Govt. of Odisha.", new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.NORMAL)));
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
        c1 = new Chunk(" Report for the financial year ", f1);
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
        c1 = new Chunk("( Period from ", f1);
        c2 = new Chunk(paf.getPrdFrmDate(), new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.BOLD));
        c3 = new Chunk(" to ", f1);
        c4 = new Chunk(paf.getPrdToDate(), new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.BOLD));
        c5 = new Chunk(" )", f1);
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

        cell = new PdfPCell(new Phrase(" 7. Sub Inspector Type:", f1));
        cell.setColspan(2);
        cell.setBorder(Rectangle.LEFT);
        cell.setBorderWidth(1f);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(paf.getSiType().toUpperCase(), new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.NORMAL)));
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

        cell = new PdfPCell(new Phrase(" 8. Place of Posting:", f1));
        cell.setColspan(2);
        cell.setBorder(Rectangle.LEFT);
        cell.setBorderWidth(1f);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(paf.getPlaceOfPostingSi().toUpperCase(), new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.NORMAL)));
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
            
        cell = new PdfPCell(new Phrase(" 9. Period(s) of absence (on leave, training etc.,\n    if 30 days or more).Please mention date(s):", f1));
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

        cell = new PdfPCell(new Phrase(" 10. Name & Designation of the Reporting Authority\n    and period worked under him/her:", f1));
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

        cell = new PdfPCell(new Phrase(" 11. Name & Designation of the Reviewing Authority\n    and period worked under him/her:", f1));
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

        cell = new PdfPCell(new Phrase(" 12. Name & Designation of the Accepting Authority\n    and period worked under him/her:", f1));
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
        innertable.setWidths(new int[]{3, 15, 10, 20, 10});
        innertable.setWidthPercentage(100);

        innercell = new PdfPCell(new Phrase("SL. No", f1));
        innercell.setHorizontalAlignment(Element.ALIGN_CENTER);
        //innercell.setBorder(Rectangle.LEFT);
        innertable.addCell(innercell);
        
        innercell = new PdfPCell(new Phrase("Task", f1));
        innercell.setHorizontalAlignment(Element.ALIGN_CENTER);
        //innercell.setBorder(Rectangle.LEFT);
        innertable.addCell(innercell);
        
        innercell = new PdfPCell(new Phrase("Target", f1));
        innercell.setHorizontalAlignment(Element.ALIGN_CENTER);
        //innercell.setBorder(Rectangle.LEFT);
        innertable.addCell(innercell);
        
        innercell = new PdfPCell(new Phrase("Achievement", f1));
        innercell.setHorizontalAlignment(Element.ALIGN_CENTER);
        //innercell.setBorder(Rectangle.LEFT);
        innertable.addCell(innercell);
        
        innercell = new PdfPCell(new Phrase("% of Achievement", f1));
        innercell.setHorizontalAlignment(Element.ALIGN_CENTER);
        innertable.addCell(innercell);

        cell = new PdfPCell(innertable);
        cell.setColspan(5);
        cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
        cell.setBorderWidth(1f);
        table.addCell(cell);

        List achievementList = paf.getAchivementList();
        if (achievementList != null && achievementList.size() > 0) {
            ParAchievement ab = null;
            for (int i = 0; i < achievementList.size(); i++) {
                ab = (ParAchievement) achievementList.get(i);
                
                innertable = new PdfPTable(5);
                innertable.setWidths(new int[]{3, 15, 10, 20, 10});
                innertable.setWidthPercentage(100);
                
                int j = i + 1;
                innercell = new PdfPCell(new Phrase(j + "", f1));
                innercell.setBorder(Rectangle.NO_BORDER);
                innercell.setHorizontalAlignment(Element.ALIGN_CENTER);
                innertable.addCell(innercell);

                innercell = new PdfPCell(new Phrase(ab.getTask(), f1));
                innercell.setBorder(Rectangle.NO_BORDER);
                innercell.setHorizontalAlignment(Element.ALIGN_LEFT);
                innertable.addCell(innercell);

                innercell = new PdfPCell(new Phrase(ab.getTarget(), f1));
                innercell.setBorder(Rectangle.NO_BORDER);
                innercell.setHorizontalAlignment(Element.ALIGN_LEFT);
                innertable.addCell(innercell);

                innercell = new PdfPCell(new Phrase(ab.getAchievement(), f1));
                innercell.setBorder(Rectangle.NO_BORDER);
                innercell.setHorizontalAlignment(Element.ALIGN_LEFT);
                innertable.addCell(innercell);

//                innercell = new PdfPCell(new Phrase(ab.getAchievement(), f1));
//                innercell.setBorder(Rectangle.NO_BORDER);
//                innercell.setHorizontalAlignment(Element.ALIGN_CENTER);
//                innertable.addCell(innercell);

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
        
//        ==== Starts from here ====
//        innertable = new PdfPTable(5);
//        innertable.setWidths(new int[]{2, 2, 2, 2, 2});
//        innertable.setWidthPercentage(100);
//
//        innercell = new PdfPCell(new Phrase("SL. No", f1));
//        innercell.setHorizontalAlignment(Element.ALIGN_CENTER);
//
//        innertable.addCell(innercell);
//        innercell = new PdfPCell(new Phrase("Task", f1));
//        innercell.setHorizontalAlignment(Element.ALIGN_CENTER);
//
//        innertable.addCell(innercell);
//        innercell = new PdfPCell(new Phrase("Target", f1));
//        innercell.setHorizontalAlignment(Element.ALIGN_CENTER);
//
//        innertable.addCell(innercell);
//        innercell = new PdfPCell(new Phrase("Achievement", f1));
//        innercell.setHorizontalAlignment(Element.ALIGN_CENTER);
//
//        innertable.addCell(innercell);
//        innercell = new PdfPCell(new Phrase("% of Achievement", f1));
//        innercell.setHorizontalAlignment(Element.ALIGN_CENTER);
//        innertable.addCell(innercell);
//
//        cell = new PdfPCell(innertable);
//        cell.setColspan(5);
//        cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
//        cell.setBorderWidth(1f);
//        table.addCell(cell);
//
//        ArrayList achievementList = paf.getAchivementList();
//        if (achievementList != null && achievementList.size() > 0) {
//            ParAchievement ab = null;
//            for (int i = 0; i < achievementList.size(); i++) {
//                ab = (ParAchievement) achievementList.get(i);
//                innertable = new PdfPTable(5);
//                innertable.setWidths(new int[]{2, 2, 2, 2, 2});
//                innertable.setWidthPercentage(100);
//                int j = i + 1;
//                innercell = new PdfPCell(new Phrase(j + "", f1));
//                innercell.setBorder(Rectangle.NO_BORDER);
//                innercell.setHorizontalAlignment(Element.ALIGN_CENTER);
//                innertable.addCell(innercell);
//                innercell = new PdfPCell(new Phrase(ab.getTask(), f1));
//                innercell.setBorder(Rectangle.NO_BORDER);
//                innercell.setHorizontalAlignment(Element.ALIGN_CENTER);
//                innertable.addCell(innercell);
//                innercell = new PdfPCell(new Phrase(ab.getTarget(), f1));
//                innercell.setBorder(Rectangle.NO_BORDER);
//                innercell.setHorizontalAlignment(Element.ALIGN_CENTER);
//                innertable.addCell(innercell);
//                innercell = new PdfPCell(new Phrase(ab.getAchievement(), f1));
//                innercell.setBorder(Rectangle.NO_BORDER);
//                innercell.setHorizontalAlignment(Element.ALIGN_CENTER);
//                innertable.addCell(innercell);
//                innercell = new PdfPCell(new Phrase(ab.getPercentAchievement(), f1));
//                innercell.setBorder(Rectangle.NO_BORDER);
//                innercell.setHorizontalAlignment(Element.ALIGN_CENTER);
//                innertable.addCell(innercell);
//
//                cell = new PdfPCell(innertable);
//                cell.setColspan(5);
//                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
//                cell.setBorderWidth(1f);
//                table.addCell(cell);
//            }
//        }
//
//        ====Ends here========
                
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

                cell = new PdfPCell(new Phrase(" 1. Personal Attribute (On a scale of 1-5 weightage for this section in 40%) 'A' ", CommonFunctions.getDesired_PDF_Font(10, false, false)));
                cell.setColspan(4);
                cell.setFixedHeight(5);
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
                
                cell = new PdfPCell(new Phrase("(c)  Communication skill :", CommonFunctions.getDesired_PDF_Font(9, false, false)));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                cell.setBorderWidth(1f);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(rhb.getRatingcomskill1(), CommonFunctions.getDesired_PDF_Font(9, false, false)));
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
                
                cell = new PdfPCell(new Phrase("(d)  Leadership Qualities :", CommonFunctions.getDesired_PDF_Font(9, false, false)));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                cell.setBorderWidth(1f);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(rhb.getRatingleadership1(), CommonFunctions.getDesired_PDF_Font(9, false, false)));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                cell.setBorderWidth(1f);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
                                
                cell = new PdfPCell();
                cell.setColspan(4);
                cell.setFixedHeight(10);
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                cell.setBorderWidth(1f);
                table.addCell(cell);

                
                cell = new PdfPCell(new Phrase("2. Functional (On a Scale of 1-5,weightage for this section is 20%)'B' ", CommonFunctions.getDesired_PDF_Font(10, false, false)));
                cell.setColspan(4);
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                cell.setBorderWidth(1f);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("", CommonFunctions.getDesired_PDF_Font(9, false, false)));
                cell.setColspan(4);
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                cell.setBorderWidth(1f);
                table.addCell(cell);
                
                    cell = new PdfPCell(new Phrase("(a) Knowledge of Criminal Laws/ Police Manuals and rules/ Procedures/IT Skills/ Local norms in the relevant subjects: ", CommonFunctions.getDesired_PDF_Font(10, false, false)));
                    cell.setColspan(4);
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                    cell.setBorderWidth(1f);
                    table.addCell(cell);
                    
                    String tempVal2a = "";
                    int val2a = rhb.getRatingitskill();
                    if(val2a == 1){
                        tempVal2a = "Below Average";
                    }else if(val2a == 2){
                        tempVal2a = "Average";
                    }else if(val2a == 3){
                        tempVal2a = "Good";
                    }else if(val2a == 4){
                        tempVal2a = "Very Good";
                    }else if(val2a == 5){
                       tempVal2a = "Outstanding";
                    }                                                                
                    cell = new PdfPCell(new Phrase(StringUtils.repeat(" ", 10) + StringUtils.defaultString(tempVal2a), CommonFunctions.getDesired_PDF_Font(9, false, false)));
                    cell.setColspan(4);
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                    cell.setBorderWidth(1f);
                    table.addCell(cell);
                
                    cell = new PdfPCell(new Phrase("(b) Attitude towards ST/SC/Weaker Sections & relation with Public: ", CommonFunctions.getDesired_PDF_Font(10, false, false)));
                    cell.setColspan(4);
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                    cell.setBorderWidth(1f);
                    table.addCell(cell);
                    
                    String tempVal2b = "";
                    int val2b = rhb.getRatingAttitudeStScSection();
                    if(val2b == 1){
                        tempVal2b = "Below Average";
                    }else if(val2b == 2){
                        tempVal2b = "Average";
                    }else if(val2b == 3){
                        tempVal2b = "Good";
                    }else if(val2b == 4){
                        tempVal2b = "Very Good";
                    }else if(val2b == 5){
                       tempVal2b = "Outstanding";
                    }
                    cell = new PdfPCell(new Phrase(StringUtils.repeat(" ", 10) + StringUtils.defaultString(tempVal2b), CommonFunctions.getDesired_PDF_Font(9, false, false)));
                    cell.setColspan(4);
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                    cell.setBorderWidth(1f);
                    table.addCell(cell);
                
                
                cell = new PdfPCell(new Phrase("3. Assessment Of Performance Of 5T (20%) 'C' ", CommonFunctions.getDesired_PDF_Font(10, false, false)));
                cell.setColspan(4);
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                cell.setBorderWidth(1f);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("", CommonFunctions.getDesired_PDF_Font(9, false, false)));
                cell.setColspan(4);
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                cell.setBorderWidth(1f);
                table.addCell(cell);
                
                    cell = new PdfPCell(new Phrase("(a) 10% on 5T charter of Department: (out of 10%) ", CommonFunctions.getDesired_PDF_Font(10, false, false)));
                    cell.setColspan(4);
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                    cell.setBorderWidth(1f);
                    table.addCell(cell);
                    
                    String tempVal3a = "";
                    if(rhb.getFiveTChartertenpercent()!=null && !rhb.getFiveTChartertenpercent().equals("")){
                        tempVal3a = rhb.getFiveTChartertenpercent();
                        if(tempVal3a.equals("1")){
                            tempVal3a = "Below Average";
                        }else if(tempVal3a.equals("2")){
                            tempVal3a = "Average";
                        }else if(tempVal3a.equals("3")){
                            tempVal3a = "Good";
                        }else if(tempVal3a.equals("4")){
                            tempVal3a = "Very Good";
                        }else if(tempVal3a.equals("5")){
                           tempVal3a = "Outstanding";
                        }
                    }else{
                        tempVal3a = "";
                    }
                    
                    cell = new PdfPCell(new Phrase(StringUtils.repeat(" ", 10) + StringUtils.defaultString(tempVal3a), CommonFunctions.getDesired_PDF_Font(9, false, false)));
                    cell.setColspan(4);
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                    cell.setBorderWidth(1f);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("(b) 5% on 5T charter of Government: (out of 5%):", CommonFunctions.getDesired_PDF_Font(10, false, false)));
                    cell.setColspan(4);
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                    cell.setBorderWidth(1f);
                    table.addCell(cell);
                    
                    String tempVal3b = "";
                    if(rhb.getFiveTCharterfivePercent()!=null  && !rhb.getFiveTCharterfivePercent().equals("")){
                        tempVal3b = rhb.getFiveTCharterfivePercent();
                        if(tempVal3b.equals("1")){
                            tempVal3b = "Below Average";
                        }else if(tempVal3b.equals("2")){
                            tempVal3b = "Average";
                        }else if(tempVal3b.equals("3")){
                            tempVal3b = "Good";
                        }else if(tempVal3b.equals("4")){
                            tempVal3b = "Very Good";
                        }else if(tempVal3b.equals("5")){
                           tempVal3b = "Outstanding";
                        }
                    }else{
                        tempVal3b = "";
                    }
                    
                    cell = new PdfPCell(new Phrase(StringUtils.repeat(" ", 10) + StringUtils.defaultString(tempVal3b), CommonFunctions.getDesired_PDF_Font(9, false, false)));
                    cell.setColspan(4);
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                    cell.setBorderWidth(1f);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("(c) 5% on Mo Sarkar: (out of 5%) :", CommonFunctions.getDesired_PDF_Font(10, false, false)));
                    cell.setColspan(4);
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                    cell.setBorderWidth(1f);
                    table.addCell(cell);
                    
                    String tempVal3c = "";
                    if(rhb.getFiveTCharterfivePercent()!=null  && !rhb.getFiveTCharterfivePercent().equals("")){
                        tempVal3c  = rhb.getFiveTComponentmoSarkar();
                        if(tempVal3c.equals("1")){
                            tempVal3c = "Below Average";
                        }else if(tempVal3c.equals("2")){
                            tempVal3c = "Average";
                        }else if(tempVal3c.equals("3")){
                            tempVal3c = "Good";
                        }else if(tempVal3c.equals("4")){
                            tempVal3c = "Very Good";
                        }else if(tempVal3c.equals("5")){
                           tempVal3c = "Outstanding";
                        }
                    }else{
                        tempVal3c = "";
                    }
                    cell = new PdfPCell(new Phrase(StringUtils.repeat(" ", 10) + StringUtils.defaultString(tempVal3c), CommonFunctions.getDesired_PDF_Font(9, false, false)));
                    cell.setColspan(4);
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                    cell.setBorderWidth(1f);
                    table.addCell(cell);
               
                
                cell = new PdfPCell(new Phrase("4. Assessment of Work output (on scale of 1-5 weightage for this section 20%) 'D' ", CommonFunctions.getDesired_PDF_Font(10, false, false)));
                cell.setColspan(4);
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                cell.setBorderWidth(1f);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("", CommonFunctions.getDesired_PDF_Font(9, false, false)));
                cell.setColspan(4);
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                cell.setBorderWidth(1f);
                table.addCell(cell);
                    
                    if(paf.getSiType().equals("Sub Inspector (Civil)")){
                        cell = new PdfPCell(new Phrase("(a) Quality of output and effectiveness in investigation/ Enquiry (Eg. Convictions): ", CommonFunctions.getDesired_PDF_Font(10, false, false)));
                    }else if(paf.getSiType().equals("Sub Inspector (Armed)")){
                        cell = new PdfPCell(new Phrase("(a) Quality of output and effectiveness in management of Stores/ Procurement/ Maintenance of Arms & Ammunitions: ", CommonFunctions.getDesired_PDF_Font(10, false, false)));
                    }else if(paf.getSiType().equals("Sub Inspector (Equivalent)")){
                        cell = new PdfPCell(new Phrase("(a) Quality of output and effectiveness in management of equipment/stores/records pertaining to their field: ", CommonFunctions.getDesired_PDF_Font(10, false, false)));
                    }
                    
                    cell.setColspan(4);
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                    cell.setBorderWidth(1f);
                    table.addCell(cell);
                    
                    String tempVal4a = "";
                    int val4a = rhb.getRatingQualityOfOutput();
                    if(val4a == 1){
                        tempVal4a = "Below Average";
                    }else if(val4a == 2){
                        tempVal4a = "Average";
                    }else if(val4a == 3){
                        tempVal4a = "Good";
                    }else if(val4a == 4){
                        tempVal4a = "Very Good";
                    }else if(val4a == 5){
                       tempVal4a = "Outstanding";
                    }
                    cell = new PdfPCell(new Phrase(StringUtils.repeat(" ", 10) + StringUtils.defaultString(tempVal4a), CommonFunctions.getDesired_PDF_Font(9, false, false)));
                    cell.setColspan(4);
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                    cell.setBorderWidth(1f);
                    table.addCell(cell);

                    if(paf.getSiType().equals("Sub Inspector (Civil)")){
                        cell = new PdfPCell(new Phrase("(b) Effectiveness in handling Law & Order/ Collection of Intelligence / command /control over Subordinates: ", CommonFunctions.getDesired_PDF_Font(10, false, false)));
                    }else if(paf.getSiType().equals("Sub Inspector (Armed)")){
                        cell = new PdfPCell(new Phrase("(b) Effectiveness in handling Force management / command /control over Subordinates/ interest in their training /development/ welfare: ", CommonFunctions.getDesired_PDF_Font(10, false, false)));
                    }else if(paf.getSiType().equals("Sub Inspector (Equivalent)")){
                        cell = new PdfPCell(new Phrase("(b) Effectiveness in handling work pertaining to their field/ command/control over Subordinates/ interest in their training /development/ welfare: ", CommonFunctions.getDesired_PDF_Font(10, false, false)));
                    }
                    cell.setColspan(4);
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                    cell.setBorderWidth(1f);
                    table.addCell(cell);
                    
                    String tempVal4b = "";
                    int val4b = rhb.getRatingeffectivenessHandlingWork();
                    if(val4b == 1){
                        tempVal4b = "Below Average";
                    }else if(val4b == 2){
                        tempVal4b = "Average";
                    }else if(val4b == 3){
                        tempVal4b = "Good";
                    }else if(val4b == 4){
                        tempVal4b = "Very Good";
                    }else if(val4b == 5){
                       tempVal4b = "Outstanding";
                    }
                    cell = new PdfPCell(new Phrase(StringUtils.repeat(" ", 10) + StringUtils.defaultString(tempVal4b), CommonFunctions.getDesired_PDF_Font(9, false, false)));
                    cell.setColspan(4);
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                    cell.setBorderWidth(1f);
                    table.addCell(cell);

                cell = new PdfPCell(new Phrase("5. Pen picture of Officer (not more than 100 words)", CommonFunctions.getDesired_PDF_Font(10, false, false)));
                cell.setColspan(4);
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                cell.setBorderWidth(1f);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(StringUtils.repeat(" ", 10) + StringUtils.defaultString(rhb.getPenPictureOfOficerNote()), CommonFunctions.getDesired_PDF_Font(9, false, false)));
                cell.setColspan(4);
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                cell.setBorderWidth(1f);
                table.addCell(cell);
                    

                cell = new PdfPCell(new Phrase("6.  Inadequacies, deficiencies or shortcomings, if any, not more than 200 words (Remarks to be treated as adverse)", CommonFunctions.getDesired_PDF_Font(10, false, false)));
                cell.setColspan(4);
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                cell.setBorderWidth(1f);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(StringUtils.repeat(" ", 10) + StringUtils.defaultString(rhb.getInadequaciesNote()), CommonFunctions.getDesired_PDF_Font(9, false, false)));
                cell.setColspan(4);
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                cell.setBorderWidth(1f);
                table.addCell(cell);
                
                
                cell = new PdfPCell(new Phrase("7.  Integrity ( If integrity is doubtful or adverse please write 'Not certified' in the space below and justify your remarks here ", CommonFunctions.getDesired_PDF_Font(10, false, false)));
                cell.setColspan(4);
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                cell.setBorderWidth(1f);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(StringUtils.repeat(" ", 10) + StringUtils.defaultString(rhb.getIntegrityNote()), CommonFunctions.getDesired_PDF_Font(9, false, false)));
                cell.setColspan(4);
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                cell.setBorderWidth(1f);
                table.addCell(cell);
                
                
                cell = new PdfPCell(new Phrase("8. State of Health ", CommonFunctions.getDesired_PDF_Font(10, false, false)));
                cell.setColspan(4);
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                cell.setBorderWidth(1f);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("", CommonFunctions.getDesired_PDF_Font(9, false, false)));
                cell.setColspan(4);
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                cell.setBorderWidth(1f);
                table.addCell(cell);
                
                    cell = new PdfPCell(new Phrase("(a) State of Health (please indicate whether the officer’s state of health is) ", CommonFunctions.getDesired_PDF_Font(10, false, false)));
                    cell.setColspan(4);
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                    cell.setBorderWidth(1f);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(StringUtils.repeat(" ", 10) + StringUtils.defaultString(rhb.getStateOfHealth()), CommonFunctions.getDesired_PDF_Font(9, false, false)));
                    cell.setColspan(4);
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                    cell.setBorderWidth(1f);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("(b) Sick Report (if more than 10 days at one time) mention period of sick report:", CommonFunctions.getDesired_PDF_Font(10, false, false)));
                    cell.setColspan(4);
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                    cell.setBorderWidth(1f);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(StringUtils.repeat(" ", 10) + StringUtils.defaultString(rhb.getSickReportOnDate()), CommonFunctions.getDesired_PDF_Font(9, false, false)));
                    cell.setColspan(4);
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                    cell.setBorderWidth(1f);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("(c) Please indicate if appraisee reported sick to avoid posting on transfer or a specific duty : ", CommonFunctions.getDesired_PDF_Font(10, false, false)));
                    cell.setColspan(4);
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                    cell.setBorderWidth(1f);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(StringUtils.repeat(" ", 10) + StringUtils.defaultString(rhb.getSickDetails()), CommonFunctions.getDesired_PDF_Font(9, false, false)));
                    cell.setColspan(4);
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                    cell.setBorderWidth(1f);
                    table.addCell(cell);
                
                
                cell = new PdfPCell(new Phrase("9. Overall Grading", CommonFunctions.getDesired_PDF_Font(10, false, false)));
                cell.setColspan(4);
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                cell.setBorderWidth(1f);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(StringUtils.repeat(" ", 10) + StringUtils.defaultString(rhb.getSltGrading1()), CommonFunctions.getDesired_PDF_Font(9, false, false)));
                cell.setColspan(4);
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                cell.setBorderWidth(1f);
                table.addCell(cell);
                
                
                
                

                cell = new PdfPCell(new Phrase("10. For Overall Grading 'Below Average' / 'Outstanding' please provide justification in the space below : ", CommonFunctions.getDesired_PDF_Font(10, false, false)));
                cell.setColspan(4);
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                cell.setBorderWidth(1f);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(StringUtils.repeat(" ", 10) + StringUtils.defaultString(rhb.getGradingNote()), CommonFunctions.getDesired_PDF_Font(9, false, false)));
                cell.setColspan(4);
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                cell.setBorderWidth(1f);
                table.addCell(cell);

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

                cell = new PdfPCell(new Phrase(StringUtils.repeat(" ", 10) + StringUtils.defaultString(rvb.getReviewingNote()), CommonFunctions.getDesired_PDF_Font(9, false, false)));
                cell.setColspan(4);
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                cell.setBorderWidth(1f);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("2. Overall Grading Given By Reviewing Authority  :" + ParGrade.getGradingName(rvb.getSltReviewGrading()), CommonFunctions.getDesired_PDF_Font(10, false, false)));
                cell.setColspan(4);
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                cell.setBorderWidth(1f);
                table.addCell(cell);

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

            cell = new PdfPCell(new Phrase(StringUtils.repeat(" ", 10) + StringUtils.defaultString(ahb.getAcceptingNote()), CommonFunctions.getDesired_PDF_Font(10, false, false)));
            cell.setColspan(4);
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Overall Grading Given By Accepting Authority  :" + ParGrade.getGradingName(ahb.getSltAcceptingGrading()), CommonFunctions.getDesired_PDF_Font(10, false, false)));
            cell.setColspan(4);
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

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
        document.close();
    }
}
