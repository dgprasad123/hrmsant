/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.view;

import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
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
import com.itextpdf.tool.xml.ElementList;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.dao.performanceappraisal.PARBrowserDAOImpl;
import static hrms.dao.performanceappraisal.PARBrowserDAOImpl.generateQRcode;
import hrms.dao.propertystatement.PropertyStatementDAOImpl;
import hrms.model.parmast.AbsenteeBean;
import hrms.model.parmast.AchievementBean;
import hrms.model.parmast.ParDetail;
import hrms.model.parmast.Parauthorityhelperbean;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.view.AbstractView;

/**
 *
 * @author Manas
 */
public class ParPdfViewApprise extends AbstractView {

    public ParPdfViewApprise() {
        setContentType("application/pdf");
    }

    @Override
    protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest hsr, HttpServletResponse response) throws Exception {

        Document document = new Document(PageSize.A4);
        Font mainHdrTextFont = new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.BOLD, BaseColor.BLACK);
        Font subHdrTextFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK);//TIMES_ROMAN
        Font dataValTextFont = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL, BaseColor.BLACK);

        ParDetail paf = (ParDetail) model.get("paf");
        String filePath = (String) model.get("filePath");
        String qrCodePathPAR = (String) model.get("qrCodePathPAR");
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

        cell = new PdfPCell();
        cell.setColspan(4);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        if (paf.getParType() != null && paf.getParType().equals("SiPar")) {
            cell = new PdfPCell(new Phrase("Performance Appraisal Report (PAR) for SI & Equivalent Ranks (Group - B)", mainHdrTextFont));
        } else {
            cell = new PdfPCell(new Phrase("Performance Appraisal Report (PAR) for Group A and B officers of Govt. of Odisha", mainHdrTextFont));
        }
        cell.setColspan(4);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Transmission Record", subHdrTextFont));
        cell.setColspan(4);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(StringUtils.repeat("_", 93), f1));
        cell.setColspan(4);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("( To be filled in by Appraisee )", new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD)));
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
        Chunk c4 = new Chunk(paf.getParPeriodFrom(), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD));
        Chunk c5 = new Chunk(" to ", f1);
        Chunk c6 = new Chunk(paf.getParPeriodTo(), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD));
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
        c2 = new Chunk(paf.getApplicant(), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD | Font.UNDERLINE));
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
        cell = new PdfPCell(new Phrase(paf.getApprisespn(), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
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

        cell = new PdfPCell();
        cell.setColspan(4);
        cell.setFixedHeight(8);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("( To be filled in at the time of transmission by respective officer / staff)", new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD)));
        cell.setColspan(4);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(4);
        cell.setFixedHeight(20);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Transmission by", dataValTextFont));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Transmitted to whom (Name, Designation & Address)", f1));
        cell.setColspan(3);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        /*cell = new PdfPCell(new Phrase("Letter No & Date of\nTransmission", f1));
         cell.setHorizontalAlignment(Element.ALIGN_CENTER);
         table.addCell(cell);
         cell = new PdfPCell(new Phrase("Signature of\nOfficer/Staff\nTransmitting the PAR", f1));
         cell.setHorizontalAlignment(Element.ALIGN_CENTER);
         table.addCell(cell);*/

        cell = new PdfPCell(new Phrase("Appraisee", f1));
        cell.setBorder(Rectangle.LEFT);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("", f1));
        cell.setColspan(3);
        cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
        table.addCell(cell);
        /*cell = new PdfPCell(new Phrase("", f1));
         cell.setBorder(Rectangle.LEFT);
         table.addCell(cell);
         cell = new PdfPCell(new Phrase("", f1));
         cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
         table.addCell(cell);*/

        cell = new PdfPCell();
        cell.setBorder(Rectangle.LEFT);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("", f1));
        cell.setColspan(3);
        cell.setBorder(Rectangle.LEFT | Rectangle.BOTTOM | Rectangle.RIGHT);
        table.addCell(cell);
        /*cell = new PdfPCell(new Phrase("", f1));
         cell.setBorder(Rectangle.LEFT);
         table.addCell(cell);
         cell = new PdfPCell(new Phrase("", f1));
         cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
         table.addCell(cell);*/

        cell = new PdfPCell(new Phrase("Reporting\nAuthority", f1));
        cell.setBorder(Rectangle.LEFT | Rectangle.TOP | Rectangle.BOTTOM);
        table.addCell(cell);
        ArrayList reportAuthlist = paf.getReportingauth();
        Parauthorityhelperbean parhelper = null;
        String reportAuthName = "";
        String reportAuthDesg = "";
        int slno = 0;
        for (int i = 0; i < reportAuthlist.size(); i++) {
            parhelper = (Parauthorityhelperbean) reportAuthlist.get(i);
            slno = i + 1;
            if (reportAuthName == "") {
                reportAuthName = slno + ". " + parhelper.getAuthorityname() + " (" + parhelper.getAuthorityspn() + ")\n(From:" + parhelper.getFromdt() + " To:" + parhelper.getTodt() + ")";
            } else {
                reportAuthName = reportAuthName + "\n\n" + slno + ". " + parhelper.getAuthorityname() + " (" + parhelper.getAuthorityspn() + ")\n(From:" + parhelper.getFromdt() + " To:" + parhelper.getTodt() + ")";
            }
            //reportAuthDesg = parhelper.getAuthorityspn();
        }
        cell = new PdfPCell(new Phrase(reportAuthName, new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)));
        cell.setColspan(3);
        cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.BOTTOM);
        table.addCell(cell);

        ArrayList reviewAuthlist = paf.getReviewingauth();
        parhelper = null;
        String reviewAuthName = "";
        String reviewAuthDesg = "";
        slno = 0;
        for (int i = 0; i < reviewAuthlist.size(); i++) {
            parhelper = (Parauthorityhelperbean) reviewAuthlist.get(i);
            slno = i + 1;
            if (reviewAuthName == "") {
                reviewAuthName = slno + ". " + parhelper.getAuthorityname() + " (" + parhelper.getAuthorityspn() + ")\n(From:" + parhelper.getFromdt() + " To:" + parhelper.getTodt() + ")";
            } else {
                reviewAuthName = reviewAuthName + "\n\n" + slno + ". " + parhelper.getAuthorityname() + " (" + parhelper.getAuthorityspn() + ")\n(From:" + parhelper.getFromdt() + " To:" + parhelper.getTodt() + ")";
            }
            //reviewAuthDesg = parhelper.getAuthorityspn();
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
        String acceptAuthDesg = "";
        slno = 0;
        for (int i = 0; i < acceptAuthlist.size(); i++) {
            parhelper = (Parauthorityhelperbean) acceptAuthlist.get(i);
            slno = i + 1;
            if (acceptAuthName == "") {
                acceptAuthName = slno + ". " + parhelper.getAuthorityname() + " (" + parhelper.getAuthorityspn() + ")\n(From:" + parhelper.getFromdt() + " To:" + parhelper.getTodt() + ")";
            } else {
                acceptAuthName = acceptAuthName + "\n\n" + slno + ". " + parhelper.getAuthorityname() + " (" + parhelper.getAuthorityspn() + ")\n(From:" + parhelper.getFromdt() + " To:" + parhelper.getTodt() + ")";
            }
            //acceptAuthDesg = parhelper.getAuthorityspn();
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

        //Second Page
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

        if (paf.getParType() != null && paf.getParType().equals("SiPar")) {
            cell = new PdfPCell(new Phrase("SI & Equivalent Ranks (Group - B)", mainHdrTextFont));
        } else {
            cell = new PdfPCell(new Phrase("Group A and Group B Officers of Govt. of Odisha.", mainHdrTextFont));
        }
        cell.setColspan(4);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(4);
        cell.setFixedHeight(8);
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
        c2 = new Chunk(paf.getParPeriodFrom(), new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.BOLD));
        c3 = new Chunk(" to ", f1);
        c4 = new Chunk(paf.getParPeriodTo(), new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.BOLD));
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

        cell = new PdfPCell(new Phrase("PERSONAL DATA", mainHdrTextFont));
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
        cell = new PdfPCell(new Phrase(paf.getApplicant(), subHdrTextFont));
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

        String url = filePath + paf.getApplicantempid() + ".jpg";
        File f = null;
        f = new File(url);
        Image img1 = null;
        if (f.exists()) {
            img1 = Image.getInstance(url);
        }
        if (img1 != null) {
            //img1.scalePercent(15f);
            img1.scaleToFit(100f, 80f);
            cell = new PdfPCell(img1);
            cell.setRowspan(3);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);
        } else {
            cell = new PdfPCell();
            cell.setRowspan(3);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);
        }

        cell = new PdfPCell(new Phrase(" 2. Date of Birth:", f1));
        cell.setColspan(2);
        cell.setBorder(Rectangle.LEFT);
        cell.setBorderWidth(1f);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(paf.getDob(), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)));
        //cell.setColspan(2);
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
        cell = new PdfPCell(new Phrase(paf.getApprisespn(), new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.NORMAL)));
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

        cell = new PdfPCell(new Phrase(" 6. Office to which posted with Head Quarters:", f1));
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

        if (paf.getParType() != null && paf.getParType().equals("SiPar")) {
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
            String leaves = "";
            for (int i = 0; i < leaveAbsentee.size(); i++) {
                pab = (AbsenteeBean) leaveAbsentee.get(i);
                innertable = new PdfPTable(2);
                innertable.setWidths(new int[]{2, 2});
                innertable.setWidthPercentage(100);
                //innercell = new
                if (pab.getFromDate() != null && !pab.getFromDate().equals("")) {
                    if (leaves == "") {
                        leaves = "From: " + pab.getFromDate() + " to: " + pab.getToDate();
                    } else {
                        leaves = leaves + "\nFrom: " + pab.getFromDate() + " to: " + pab.getToDate();
                    }
                }
                //leaveFrmDt = pab.getFromDate();
                //leaveToDt = pab.getToDate();
            }
            if (leaves != "") {
                cell = new PdfPCell(new Phrase(leaves, f1));
                cell.setColspan(2);
                cell.setBorder(Rectangle.RIGHT);
                cell.setBorderWidth(1f);
                table.addCell(cell);
            } else {
                cell = new PdfPCell(new Phrase("Nil", f1));
                cell.setColspan(2);
                cell.setBorder(Rectangle.RIGHT);
                cell.setBorderWidth(1f);
                table.addCell(cell);
            }

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

            /*cell = new PdfPCell(new Phrase("", f1));
             cell.setColspan(2);
             cell.setBorder(Rectangle.LEFT);
             cell.setBorderWidth(1f);
             table.addCell(cell);
             cell = new PdfPCell(new Phrase("From", f1));
             cell.setBorder(Rectangle.NO_BORDER);
             table.addCell(cell);
             cell = new PdfPCell(new Phrase("To", f1));
             cell.setBorder(Rectangle.RIGHT);
             cell.setBorderWidth(1f);
             table.addCell(cell);*/
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

            /*cell = new PdfPCell(new Phrase("", f1));
             cell.setColspan(2);
             cell.setBorder(Rectangle.LEFT);
             cell.setBorderWidth(1f);
             table.addCell(cell);
             cell = new PdfPCell(new Phrase("From", f1));
             cell.setBorder(Rectangle.NO_BORDER);
             table.addCell(cell);
             cell = new PdfPCell(new Phrase("To", f1));
             cell.setBorder(Rectangle.RIGHT);
             cell.setBorderWidth(1f);
             table.addCell(cell);*/
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

        } else {

            cell = new PdfPCell(new Phrase(" 7. Period(s) of absence (on leave, training etc.,\n    if 30 days or more).Please mention date(s):", f1));
            cell.setColspan(2);
            cell.setBorder(Rectangle.LEFT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            AbsenteeBean pab = null;
            ArrayList leaveAbsentee = paf.getLeaveAbsentee();
            String leaveFrmDt = "";
            String leaveToDt = "";
            String leaves = "";
            for (int i = 0; i < leaveAbsentee.size(); i++) {
                pab = (AbsenteeBean) leaveAbsentee.get(i);
                innertable = new PdfPTable(2);
                innertable.setWidths(new int[]{2, 2});
                innertable.setWidthPercentage(100);
                //innercell = new
                if (pab.getFromDate() != null && !pab.getFromDate().equals("")) {
                    if (leaves == "") {
                        leaves = "From: " + pab.getFromDate() + " to: " + pab.getToDate();
                    } else {
                        leaves = leaves + "\nFrom: " + pab.getFromDate() + " to: " + pab.getToDate();
                    }
                }
                //leaveFrmDt = pab.getFromDate();
                //leaveToDt = pab.getToDate();
            }
            if (leaves != "") {
                cell = new PdfPCell(new Phrase(leaves, f1));
                cell.setColspan(2);
                cell.setBorder(Rectangle.RIGHT);
                cell.setBorderWidth(1f);
                table.addCell(cell);
            } else {
                cell = new PdfPCell(new Phrase("Nil", f1));
                cell.setColspan(2);
                cell.setBorder(Rectangle.RIGHT);
                cell.setBorderWidth(1f);
                table.addCell(cell);
            }

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

            /*cell = new PdfPCell(new Phrase("", f1));
             cell.setColspan(2);
             cell.setBorder(Rectangle.LEFT);
             cell.setBorderWidth(1f);
             table.addCell(cell);
             cell = new PdfPCell(new Phrase("From", f1));
             cell.setBorder(Rectangle.NO_BORDER);
             table.addCell(cell);
             cell = new PdfPCell(new Phrase("To", f1));
             cell.setBorder(Rectangle.RIGHT);
             cell.setBorderWidth(1f);
             table.addCell(cell);*/
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

            /*cell = new PdfPCell(new Phrase("", f1));
             cell.setColspan(2);
             cell.setBorder(Rectangle.LEFT);
             cell.setBorderWidth(1f);
             table.addCell(cell);
             cell = new PdfPCell(new Phrase("From", f1));
             cell.setBorder(Rectangle.NO_BORDER);
             table.addCell(cell);
             cell = new PdfPCell(new Phrase("To", f1));
             cell.setBorder(Rectangle.RIGHT);
             cell.setBorderWidth(1f);
             table.addCell(cell);*/
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
        }

        /*cell = new PdfPCell(new Phrase("", f1));
         cell.setColspan(2);
         cell.setBorder(Rectangle.LEFT);
         cell.setBorderWidth(1f);
         table.addCell(cell);
         cell = new PdfPCell(new Phrase("From", f1));
         cell.setBorder(Rectangle.NO_BORDER);
         table.addCell(cell);
         cell = new PdfPCell(new Phrase("To", f1));
         cell.setBorder(Rectangle.RIGHT);
         cell.setBorderWidth(1f);
         table.addCell(cell);*/
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
        cell = new PdfPCell(new Phrase(paf.getApplicant(), f1));
        cell.setColspan(2);
        cell.setBorder(Rectangle.RIGHT);
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

        //Third Page
        table = new PdfPTable(5);
        table.setWidths(new int[]{1, 4, 5, 3, 3});
        table.setWidthPercentage(100);

        cell = new PdfPCell();
        cell.setColspan(5);
        cell.setFixedHeight(20);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("PART-II" + StringUtils.repeat(" ", 51) + "SELF-APPRAISAL", new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD)));
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
        cell.setFixedHeight(10);
        cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
        cell.setBorderWidth(1f);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" 1. Brief description of duties/tasks entrusted.(in about 200 words)", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
        cell.setColspan(5);
        cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
        cell.setBorderWidth(1f);
        table.addCell(cell);

        String selfappraisal = paf.getSelfappraisal();
        /*selfappraisal = selfappraisal.replaceAll("<b>", "");
         selfappraisal = selfappraisal.replaceAll("</b>", "");
         selfappraisal = selfappraisal.replaceAll("<i>", "");
         selfappraisal = selfappraisal.replaceAll("</i>", "");
         selfappraisal = selfappraisal.replaceAll("<u>", "");
         selfappraisal = selfappraisal.replaceAll("</u>", "");*/
        ElementList elements = null;
        elements = XMLWorkerHelper.parseToElementList(selfappraisal, "");
        Paragraph selfappraisalParagraph = new Paragraph();
        for (Element element : elements) {
            selfappraisalParagraph.add(element);
        }

        cell = new PdfPCell();
        cell.setBorder(Rectangle.LEFT);
        cell.setBorderWidth(1f);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.addElement(selfappraisalParagraph);
        cell.setColspan(4);
        cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
        cell.setBorder(Rectangle.RIGHT);
        cell.setBorderWidth(1f);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(5);
        cell.setFixedHeight(90);
        cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
        cell.setBorderWidth(1f);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(StringUtils.repeat("_", 93), f1));
        cell.setColspan(5);
        cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
        cell.setBorderWidth(1f);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" 2. Physical / Financial Targets & Achievements", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
        cell.setColspan(5);
        cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
        cell.setBorderWidth(1f);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(StringUtils.repeat("_", 93), f1));
        cell.setColspan(5);
        cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
        cell.setBorderWidth(1f);
        table.addCell(cell);

        innertable = new PdfPTable(6);
        innertable.setWidths(new int[]{3, 15, 10, 10, 10, 10});
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
        innercell = new PdfPCell(new Phrase("Qualitative % of Achievement", f1));
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

        ArrayList achievementList = paf.getAchivementList();
        if (achievementList != null && achievementList.size() > 0) {
            AchievementBean ab = null;
            for (int i = 0; i < achievementList.size(); i++) {
                ab = (AchievementBean) achievementList.get(i);
                innertable = new PdfPTable(6);
                innertable.setWidths(new int[]{3, 15, 10, 10, 10, 10});
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

                innercell = new PdfPCell(new Phrase(ab.getPercentQualitative(), f1));
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

        cell = new PdfPCell(new Phrase(StringUtils.repeat("_", 93), f1));
        cell.setColspan(5);
        cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
        cell.setBorderWidth(1f);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" 3.(i) Significant work, if any, done", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
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

        String specialcontribution = paf.getSpecialcontribution();
        /*specialcontribution = specialcontribution.replaceAll("<b>", "");
         specialcontribution = specialcontribution.replaceAll("</b>", "");
         specialcontribution = specialcontribution.replaceAll("<i>", "");
         specialcontribution = specialcontribution.replaceAll("</i>", "");
         specialcontribution = specialcontribution.replaceAll("<u>", "");
         specialcontribution = specialcontribution.replaceAll("</u>", "");*/

        elements = XMLWorkerHelper.parseToElementList(specialcontribution, "");
        Paragraph specialcontributionParagraph = new Paragraph();
        for (Element element : elements) {
            specialcontributionParagraph.add(element);
        }

        cell = new PdfPCell();
        cell.setBorder(Rectangle.LEFT);
        cell.setBorderWidth(1f);
        table.addCell(cell);
        //cell = parseHtmlToParagraph(paf.getSpecialcontribution());
        cell = new PdfPCell();
        cell.addElement(specialcontributionParagraph);
        cell.setColspan(4);
        cell.setBorder(Rectangle.RIGHT);
        cell.setBorderWidth(1f);
        table.addCell(cell);

        paf.getFiscalYear();

        if (!paf.getFiscalYear().equals("2014-15") && !paf.getFiscalYear().equals("2015-16") && !paf.getFiscalYear().equals("2016-17") && !paf.getFiscalYear().equals("2017-18") && !paf.getFiscalYear().equals("2018-19")) {

            // if (paf.getFiscalYear().equals("2019-20")) {
            cell = new PdfPCell(new Phrase("(ii) Work Done For Implementation of 5TS (Transparency,Teamwork,Technology,Transformation and Time):", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
            cell.setColspan(5);
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setBorder(Rectangle.LEFT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(paf.getFiveTComponentappraise(), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)));
            cell.setColspan(5);
            cell.setBorder(Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);
        }

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

            String reason = paf.getReason();
            /*reason = reason.replaceAll("<b>", "");
             reason = reason.replaceAll("</b>", "");
             reason = reason.replaceAll("<i>", "");
             reason = reason.replaceAll("</i>", "");
             reason = reason.replaceAll("<u>", "");
             reason = reason.replaceAll("</u>", "");*/

            elements = XMLWorkerHelper.parseToElementList(reason, "");
            Paragraph reasonParagraph = new Paragraph();
            for (Element element : elements) {
                reasonParagraph.add(element);
            }

            cell = new PdfPCell();
            cell.setBorder(Rectangle.LEFT);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            //cell = new PdfPCell(parseHtmlToPDF(paf.getReason()));
            cell = new PdfPCell();
            cell.addElement(reasonParagraph);
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
            //document.newPage();

            //Third Page
            table = new PdfPTable(5);
            table.setWidths(new int[]{1, 4, 5, 3, 3});
            table.setWidthPercentage(100);

            cell = new PdfPCell();
            cell.setColspan(5);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(5);
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.NO_BORDER);
            //cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.TOP);
            //cell.setBorderWidth(1f);
            table.addCell(cell);

        } else {
            cell = new PdfPCell();
            cell.setColspan(5);
            cell.setFixedHeight(50);
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
            cell.setBorderWidth(1f);
            table.addCell(cell);
        }

        cell = new PdfPCell(new Phrase(StringUtils.defaultString(paf.getApplicant()), f1));
        cell.setColspan(5);
        cell.setBorder(Rectangle.NO_BORDER);
        //cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
        //cell.setBorderWidth(1f);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Place: " + StringUtils.defaultString(paf.getPlace()), f1));
        cell.setColspan(2);
        cell.setBorder(Rectangle.NO_BORDER);
        //cell.setBorder(Rectangle.LEFT);
        //cell.setBorderWidth(1f);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" PAR Submitted on: " + paf.getSubmitted_on(), f1));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Signature of Appraisee", f1));
        cell.setColspan(2);
        cell.setBorder(Rectangle.NO_BORDER);
        //cell.setBorder(Rectangle.RIGHT);
        //cell.setBorderWidth(1f);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
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
        cell = new PdfPCell(new Phrase(paf.getIpAddress() + "", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
        cell.setFixedHeight(10);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", f1));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        String parFilePath = qrCodePathPAR;

        String storedpath = qrCodePathPAR + paf.getFiscalYear() + CommonFunctions.getResourcePath();
        String qrCodeurl = storedpath + paf.getApplicantempid() + "-" + paf.getParid() + ".png";
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
            //img1.scalePercent(15f);
            img2.scaleToFit(100f, 80f);
            cell = new PdfPCell(img2);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
//            } else {
            cell = new PdfPCell();
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
        }

        document.add(table);
        document.close();
    }

}
