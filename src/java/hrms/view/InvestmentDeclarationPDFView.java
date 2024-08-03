/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.view;

import com.itextpdf.text.BaseColor;
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
import java.io.ByteArrayOutputStream;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.view.AbstractView;

public class InvestmentDeclarationPDFView extends AbstractView {

    public InvestmentDeclarationPDFView() {
        setContentType("application/pdf");
    }

    @Override
    protected boolean generatesDownloadContent() {
        return true;
    }

    @Override
    protected void renderMergedOutputModel(Map<String, Object> model,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        ByteArrayOutputStream baos = createTemporaryOutputStream();
        Document document = new Document(PageSize.A4);
        PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());
        writer.setViewerPreferences(PdfWriter.ALLOW_PRINTING | PdfWriter.PageLayoutSinglePage);
        document.open();

        Font hdrTextFont = new Font(Font.FontFamily.TIMES_ROMAN, 10f, Font.BOLD, BaseColor.BLACK);
        Font textFont = new Font(Font.FontFamily.TIMES_ROMAN, 8.4f, Font.NORMAL, BaseColor.BLACK);
        Font bigTextFont = new Font(Font.FontFamily.TIMES_ROMAN, 8.6f, Font.NORMAL, BaseColor.BLACK);
        Font boldTextFont = new Font(Font.FontFamily.TIMES_ROMAN, 8.6f, Font.BOLD, BaseColor.BLACK);

        Font f1 = new Font();
        f1.setSize(9);
        f1.setFamily("Times New Roman");

        PdfPTable table = null;
        table = new PdfPTable(9);
        table.setWidths(new int[]{3, 15, 8, 6, 8, 6, 6, 7, 6});
        table.setWidthPercentage(100);
        PdfPCell cell = null;

        cell = new PdfPCell(new Phrase(StringUtils.defaultString("FORM NO .12BB"), hdrTextFont));
        cell.setColspan(9);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("(See rules 26C)", f1));
        cell.setColspan(9);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(9);
        cell.setFixedHeight(10);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        document.add(table);

        table = new PdfPTable(2);
        table.setWidths(new int[]{18, 16});
        cell = new PdfPCell(new Phrase("1. Name and address of the employee :", f1));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" ", textFont));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(cell);
        table.setWidthPercentage(100);
        document.add(table);

        table = new PdfPTable(2);
        table.setWidths(new int[]{18, 16});
        cell = new PdfPCell(new Phrase("2.Permanent Account No of the employee :", f1));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" ", textFont));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(cell);
        table.setWidthPercentage(100);
        document.add(table);

        table = new PdfPTable(2);
        table.setWidths(new int[]{18, 16});
        cell = new PdfPCell(new Phrase("3.Financial Year :", f1));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" ", textFont));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(cell);
        table.setWidthPercentage(100);
        document.add(table);

        table = new PdfPTable(2);
        cell = new PdfPCell(new Phrase(" ", f1));
        cell.setColspan(2);
        table.addCell(cell);
        table.setWidthPercentage(100);
        document.add(table);

        table = new PdfPTable(2);
        cell = new PdfPCell(new Phrase("Detalis of Claims and evidences thereOf ", hdrTextFont));
        cell.setColspan(2);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        table.setWidthPercentage(100);
        document.add(table);

        table = new PdfPTable(4);
        table.setWidths(new int[]{3, 15, 6, 10});
        cell = new PdfPCell(new Phrase("Sl No", f1));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Nature Of Claim ", f1));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Amount(Rs)", f1));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Evidence/Particulars", f1));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        table.setWidthPercentage(100);
        document.add(table);

        table = new PdfPTable(4);
        table.setWidths(new int[]{3, 15, 6, 10});
        cell = new PdfPCell(new Phrase("(1)", f1));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("(2)", f1));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("(3)", f1));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("(4)", f1));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        table.setWidthPercentage(100);
        document.add(table);

        table = new PdfPTable(4);
        table.setWidths(new int[]{3, 15, 6, 10});
        cell = new PdfPCell(new Phrase("1", f1));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(cell);
        Chunk c1 = new Chunk("House Rent Allowance", f1);
        Chunk c2 = new Chunk("(i) Rent paid to the landlord", f1);
        Chunk c3 = new Chunk("(ii) Name of the landlord", f1);
        Chunk c4 = new Chunk("(iii) Address of the landlord", f1);
        Chunk c5 = new Chunk("(iv) Permanent account no of the landlord", f1);
        Chunk c6 = new Chunk(" ", f1);
        Chunk c7 = new Chunk("Note: Permanent Account Number shall be", f1);
        Chunk c8 = new Chunk("furnished if the aggregate rent paid during the", f1);
        Chunk c9 = new Chunk("previous year exceeds one lakh rupees", f1);
        cell = new PdfPCell(new Phrase());
        cell.addElement(c1);
        cell.addElement(c2);
        cell.addElement(c3);
        cell.addElement(c4);
        cell.addElement(c5);
        cell.addElement(c6);
        cell.addElement(c7);
        cell.addElement(c8);
        cell.addElement(c8);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("", f1));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("", f1));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        table.setWidthPercentage(100);
        document.add(table);

        table = new PdfPTable(4);
        table.setWidths(new int[]{3, 15, 6, 10});
        cell = new PdfPCell(new Phrase("2", f1));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(cell);
        Chunk c10 = new Chunk("Leave travel concession or assitance", f1);
        cell = new PdfPCell(new Phrase());
        cell.addElement(c10);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("", f1));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("", f1));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        table.setWidthPercentage(100);
        document.add(table);

        table = new PdfPTable(4);
        table.setWidths(new int[]{3, 15, 6, 10});
        cell = new PdfPCell(new Phrase("3", f1));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(cell);
        Chunk c11 = new Chunk("Deduction of interest on borrowing", f1);
        Chunk c12 = new Chunk("(i) Interest payable/paid to the lender", f1);
        Chunk c13 = new Chunk("(ii) Name of the lender", f1);
        Chunk c14 = new Chunk("(iii) Address of the lender", f1);
        Chunk c15 = new Chunk("(iv) Permanent account no of the lender", f1);
        Chunk c16 = new Chunk("(a)Financial instituition (if available) ", f1);
        Chunk c17 = new Chunk("(b)Employer (if available)", f1);
        Chunk c18 = new Chunk("(c)Others", f1);

        cell = new PdfPCell(new Phrase());
        cell.addElement(c11);
        cell.addElement(c12);
        cell.addElement(c13);
        cell.addElement(c14);
        cell.addElement(c15);
        cell.addElement(c16);
        cell.addElement(c17);
        cell.addElement(c18);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("", f1));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("", f1));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        table.setWidthPercentage(100);
        document.add(table);

        table = new PdfPTable(4);
        table.setWidths(new int[]{3, 15, 6, 10});
        cell = new PdfPCell(new Phrase("4", f1));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(cell);
        Chunk c19 = new Chunk("Deduction under chapter VI-A", f1);
        Chunk c20 = new Chunk("(A) Section 80C,80CCC and 80CCD", f1);
        Chunk c21 = new Chunk("  (i)Section 80C", f1);
        Chunk c22 = new Chunk("      (a).................", f1);
        Chunk c23 = new Chunk("      (b).................", f1);
        Chunk c24 = new Chunk("      (c).................", f1);
        Chunk c25 = new Chunk("      (d).................", f1);
        Chunk c26 = new Chunk("      (e).................", f1);
        Chunk c27 = new Chunk("      (f).................", f1);
        Chunk c28 = new Chunk("      (g).................", f1);
        Chunk c29 = new Chunk("  (ii)Section 80CCC", f1);
        Chunk c30 = new Chunk("  (iii)Section 80CCD", f1);
        Chunk c31 = new Chunk("(B) Other sections(eg. 80E,80G,80TTA etc))", f1);
        Chunk c32 = new Chunk("Under chapter VI-A.", f1);
        Chunk c33 = new Chunk("", f1);
        Chunk c34 = new Chunk("            (i)  section.........", f1);
        Chunk c35 = new Chunk("            (ii) section.........", f1);
        Chunk c36 = new Chunk("            (ii) section.........", f1);
        Chunk c37 = new Chunk("            (iii)section.........", f1);
        Chunk c38 = new Chunk("            (iv) section.........", f1);

        cell = new PdfPCell(new Phrase());
        cell.addElement(c19);
        cell.addElement(c20);
        cell.addElement(c21);
        cell.addElement(c22);
        cell.addElement(c23);
        cell.addElement(c24);
        cell.addElement(c25);
        cell.addElement(c26);
        cell.addElement(c27);
        cell.addElement(c28);
        cell.addElement(c29);
        cell.addElement(c30);
        cell.addElement(c31);
        cell.addElement(c32);
        cell.addElement(c33);
        cell.addElement(c34);
        cell.addElement(c35);
        cell.addElement(c36);
        cell.addElement(c37);
        cell.addElement(c38);

        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("", f1));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("", f1));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        table.setWidthPercentage(100);
        document.add(table);

        table = new PdfPTable(4);
        cell = new PdfPCell(new Phrase("Verification ", f1));
        cell.setColspan(4);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        table.setWidthPercentage(100);
        document.add(table);

        table = new PdfPTable(4);
        table.setWidths(new int[]{3, 15, 6, 10});
        Chunk c39 = new Chunk("I ..........................son/daughter of ............................ do hereby certify that the information given above is", f1);
        Chunk c40 = new Chunk("complete and correct", f1);

        cell = new PdfPCell(new Phrase());
        cell.setColspan(4);
        cell.addElement(c39);
        cell.addElement(c40);

        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("", f1));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("", f1));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        table.setWidthPercentage(100);
        document.add(table);

        table = new PdfPTable(2);
        table.setWidths(new int[]{18, 16});
        Chunk c41 = new Chunk("Place.........................", f1);
        Chunk c42 = new Chunk("Date.........................", f1);
        Chunk c43 = new Chunk("Designation.........................", f1);
        cell = new PdfPCell(new Phrase());
        cell.addElement(c41);
        cell.addElement(c42);
        cell.addElement(c43);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(cell);

        Chunk c44 = new Chunk(" ", f1);
        Chunk c45 = new Chunk("(Signature of the employee)", f1);
        Chunk c46 = new Chunk("Full Name", f1);
        cell = new PdfPCell(new Phrase());
        cell.addElement(c44);
        cell.addElement(c45);
        cell.addElement(c46);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(cell);
        table.setWidthPercentage(100);
        document.add(table);

        document.close();
    }

}
