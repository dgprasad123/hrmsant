/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.common;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.TabSettings;
import com.itextpdf.text.TabStop;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javafx.scene.control.Tab;
import javafx.scene.text.TextAlignment;
//import javafx.scene.text.
import javax.servlet.http.HttpServletResponse;
import jxl.read.biff.BiffException;
import jxl.write.WriteException;
import org.w3c.dom.Text;

/**
 *
 * @author Madhusmita
 */
public class GPFSchedulePdf {

    public static void main(String[] args) throws WriteException, IOException, BiffException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection con = null;
        Document document = new Document(PageSize.A4);
        try {
            Class.forName("org.postgresql.Driver");
            //local//
            con = DriverManager.getConnection("jdbc:postgresql://192.168.1.19/hrmis", "hrmis2", "cmgi");
            //Production Readonly//
            //con = DriverManager.getConnection("jdbc:postgresql://172.16.1.16/hrmis", "hrmis2", "cmgi");
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("D:/GPFSchedule/GpfSchedule.pdf"));
            document.open();
            //document.add(new Paragraph("A Hello World PDF document."));

            Font f1 = new Font();
            f1.setSize(6.9f);
            f1.setFamily("Times New Roman");

            Font bold = new Font(Font.FontFamily.HELVETICA, 13, Font.BOLD);
            Font bold1 = new Font(Font.FontFamily.TIMES_ROMAN, 7, Font.BOLD);

            Font f2 = new Font();
            f2.setSize(9.1f);
            f2.setFamily("Times New Roman");

            Paragraph p1 = new Paragraph("GENERAL PROVIDENT FUND", bold);
            Paragraph p2 = new Paragraph("BILL NO:", new Font(Font.FontFamily.TIMES_ROMAN, 9.1f, Font.BOLD));
            Paragraph p3 = new Paragraph("T.V No / Date: / ", new Font(Font.FontFamily.TIMES_ROMAN, 9.1f, Font.BOLD));
            Paragraph p4 = new Paragraph("SCHEDULE OF :", new Font(Font.FontFamily.TIMES_ROMAN, 9.1f));
            Paragraph p5 = new Paragraph("Demand No-" + "8009/_____________________- State/Centre G.P.F Withdrawals\" (Strike out which is not applicable)", f2);
            Paragraph p6 = new Paragraph("1. Arrange the A/C Nos in serial order. Accounts Nos may be written very clearly.Separate Schedules should be prepared for each group.\n"
                    + "2. The names of the subscribers should be written in full.\n"
                    + "3. If interest is paid on advance, mention it in remarks column.\n"
                    + "4. Figures in columns 3,4,5 and 7 should be rounded to whole rupees.\n"
                    + "5. Use similar form, if names are few. But do not write subscribers name and account numbers very close to each other.\n"
                    + "6. The total of schedules also should be written both in figures and words.\n"
                    + "7. This form should not be used for transactions of General Provident Fund for which form No. O.T.C. 76 has been provided.\n"
                    + "8. In Col. 1 quote account number unfailingly. The guide letters e.g. I.C.S. (ICS Provident Fund) etc. should be invariably prefixed to Account Nos.\n"
                    + "9. In the remarks column write description against every new name such as 'New Subscriber' came on transfer from Office District resumed subscription.\n"
                    + "10. Separate schedule should be prepared in respect of persons whose account are kept by different Accountant General.", f1);
            p1.setAlignment(Element.ALIGN_CENTER);
            p2.setAlignment(Element.ALIGN_CENTER);
            p3.setAlignment(Element.ALIGN_CENTER);
            p4.setAlignment(Element.ALIGN_CENTER);
            p5.setAlignment(Element.ALIGN_CENTER);

            p6.setSpacingAfter(20);
            p6.setSpacingBefore(15);
            p6.setAlignment(Element.ALIGN_LEFT);
            p6.setIndentationLeft(8);
            p6.setIndentationRight(8);

            Paragraph p7 = new Paragraph("" + ", GOVERNMENT OF ODISHA", bold1);
            p7.setAlignment(Element.ALIGN_CENTER);
            Paragraph p8 = new Paragraph("DEDUCTION MADE FROM THE SALARY FOR" + "", bold1);
            p8.setAlignment(Element.ALIGN_CENTER);
            p8.setSpacingAfter(15);

            document.add(p1);
            document.add(p2);
            document.add(p3);
            document.add(p4);
            document.add(p5);
            document.add(p6);
            document.add(p7);
            document.add(p8);

            PdfPTable dataTable = new PdfPTable(8);
            PdfPCell dataCell = null;

            dataTable.setWidths(new int[]{1, 3, 5, 3, 2, 2, 2, 3});
            dataTable.setWidthPercentage(100);

            // Table Header
            dataCell = new PdfPCell(new Phrase("Sl No.", new Font(Font.FontFamily.HELVETICA, 6.5f, Font.BOLD)));
            dataCell.setBorderWidth(0.5f);
            dataTable.addCell(dataCell);

            dataCell = new PdfPCell(new Phrase("ACCOUNT NO./\n"
                    + "DATE OF ENTRY\n"
                    + "INTO GOVT. SERVICE", new Font(Font.FontFamily.HELVETICA, 6.5f, Font.BOLD)));
            dataCell.setBorderWidth(0.5f);
            dataTable.addCell(dataCell);

            dataCell = new PdfPCell(new Phrase("NAME OF THE SUBSCRIBER/\n"
                    + "DESIGNATION", new Font(Font.FontFamily.HELVETICA, 6.5f, Font.BOLD)));
            dataCell.setBorderWidth(0.5f);
            dataTable.addCell(dataCell);

            dataCell = new PdfPCell(new Phrase("BASIC PAY/ "
                    + "GRADE PAY / "
                    + "SCALE OF PAY", new Font(Font.FontFamily.HELVETICA, 6.5f, Font.BOLD)));
            dataCell.setBorderWidth(0.5f);
            dataTable.addCell(dataCell);

            dataCell = new PdfPCell(new Phrase("MONTHLY SUBSCRIPTION", new Font(Font.FontFamily.HELVETICA, 6.5f, Font.BOLD)));
            dataCell.setBorderWidth(0.5f);
            dataTable.addCell(dataCell);

            dataCell = new PdfPCell(new Phrase("REFUND OF "
                    + "WITHDRAWLS AMT / "
                    + "NO. OF INST.", new Font(Font.FontFamily.HELVETICA, 6.5f, Font.BOLD)));
            dataCell.setBorderWidth(0.5f);
            dataTable.addCell(dataCell);

            dataCell = new PdfPCell(new Phrase("TOTAL RELEASED", new Font(Font.FontFamily.HELVETICA, 6.5f, Font.BOLD)));
            dataCell.setBorderWidth(0.5f);
            dataTable.addCell(dataCell);

            dataCell = new PdfPCell(new Phrase("REMARKS\n"
                    + "D.O.B and D.O.R.", new Font(Font.FontFamily.HELVETICA, 6.5f, Font.BOLD)));
            dataCell.setBorderWidth(0.5f);
            dataTable.addCell(dataCell);

            // Table First Row
            dataCell = new PdfPCell(new Phrase("1", new Font(Font.FontFamily.TIMES_ROMAN, 7, Font.BOLD)));
            dataCell.setBorder(Rectangle.NO_BORDER);
            dataCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            dataTable.addCell(dataCell);
            dataCell = new PdfPCell(new Phrase("2", new Font(Font.FontFamily.TIMES_ROMAN, 7, Font.BOLD)));
            dataCell.setBorder(Rectangle.NO_BORDER);
            dataCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            dataTable.addCell(dataCell);
            dataCell = new PdfPCell(new Phrase("3", new Font(Font.FontFamily.TIMES_ROMAN, 7, Font.BOLD)));
            dataCell.setBorder(Rectangle.NO_BORDER);
            dataCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            dataTable.addCell(dataCell);
            dataCell = new PdfPCell(new Phrase("4", new Font(Font.FontFamily.TIMES_ROMAN, 7, Font.BOLD)));
            dataCell.setBorder(Rectangle.NO_BORDER);
            dataCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            dataTable.addCell(dataCell);
            dataCell = new PdfPCell(new Phrase("5", new Font(Font.FontFamily.TIMES_ROMAN, 7, Font.BOLD)));
            dataCell.setBorder(Rectangle.NO_BORDER);
            dataCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            dataTable.addCell(dataCell);
            dataCell = new PdfPCell(new Phrase("6", new Font(Font.FontFamily.TIMES_ROMAN, 7, Font.BOLD)));
            dataCell.setBorder(Rectangle.NO_BORDER);
            dataCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            dataTable.addCell(dataCell);
            dataCell = new PdfPCell(new Phrase("7", new Font(Font.FontFamily.TIMES_ROMAN, 7, Font.BOLD)));
            dataCell.setBorder(Rectangle.NO_BORDER);
            dataCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            dataTable.addCell(dataCell);
            dataCell = new PdfPCell(new Phrase("8", new Font(Font.FontFamily.TIMES_ROMAN, 7, Font.BOLD)));
            dataCell.setBorder(Rectangle.NO_BORDER);
            dataCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            dataTable.addCell(dataCell);
            document.add(dataTable);
            Paragraph p9 = new Paragraph("Signature of the D.D.O. with Seal\n"
                    + ",\n"
                    + "Date: ", new Font(Font.FontFamily.HELVETICA, 5f));
            //p9.setTabSettings(new TabSettings(2000f));
            //p9.add(Chunk.TABBING);
            //p9.setAlignment(Element.ALIGN_RIGHT);
            p9.setSpacingAfter(20);
            p9.setSpacingBefore(20);
            //p6.setIndentationRight(45);
            //document.setMargins(0, 0, marginTop, marginBottom)

            /* PdfPTable dataTable2 = new PdfPTable(2);
             PdfPCell dataCell2 = null;
             //PdfPCell dataCel1 = null;

             dataTable2.setWidths(new int[]{5, 5});
             dataTable2.setWidthPercentage(100);

             dataCell2 = new PdfPCell(new Phrase("gdg", new Font(Font.FontFamily.TIMES_ROMAN, 7, Font.BOLD)));
             dataCell2 = new PdfPCell(new Phrase("gdg", new Font(Font.FontFamily.TIMES_ROMAN, 7, Font.BOLD)));
             dataCell2.setBorderWidth(0.5f);
             dataTable2.addCell(dataCell2);
             document.add(dataTable2);

             //dataCel.setColspan(6);
             //dataCel.setBorder(Rectangle.NO_BORDER);
             // dataTab.addCell(dataCel.setColspan(6));
             //dataTab.addCell(dataCel);
             // dataCel1 = new PdfPCell(new Phrase("Signature of the D.D.O. with Seal\n"
             //         + ",\n"
             //        + "Date: ", new Font(Font.FontFamily.HELVETICA, 5f)));
             //dataCel1.setBorder(Rectangle.NO_BORDER);
             //dataCel1.setHorizontalAlignment(Element.ALIGN_CENTER);
             //dataTab.addCell(dataCel1);
             /*Paragraph p9 = new Paragraph("Signature of the D.D.O. with Seal\n"
             + ",\n"
             + "Date: ", new Font(Font.FontFamily.HELVETICA, 5f));
             p9.setAlignment(Element.ALIGN_CENTER);
             //p9.setTextAlignment(TextAlignment.CENTER); 
             p9.setSpacingAfter(15);
             p9.setSpacingBefore(15);*/
            Paragraph p10 = new Paragraph("Certified that all particulars of recovery have been correctly furnished as per the instruction issued in respect of preparation of G.P.F. Schedules.\n"
                    + "Voucher No.................................... ", new Font(Font.FontFamily.HELVETICA, 5f));
            p10.setAlignment(Element.ALIGN_LEFT);
            p10.setSpacingAfter(20);
            p10.setSpacingBefore(30);
            p10.setTabSettings(new TabSettings(400f));
            p10.add(Chunk.TABBING);
            p10.add(new Chunk("Date of Encashment:// "));

            Paragraph p11 = new Paragraph("FOR USE IN AUDIT OFFICE", new Font(Font.FontFamily.TIMES_ROMAN, 6f, Font.BOLD));
            p11.setAlignment(Element.ALIGN_CENTER);

            Paragraph p12 = new Paragraph("Certified that the name and account No. of individual deduction and total shown in column - 6 have been checked with ref. to the bill vide page 224 of the Audit Manual.\n"
                    + "Certified that the rates of pay shown in column - 4 have been verified with amount drawn in this bill.", new Font(Font.FontFamily.HELVETICA, 5f));
            p12.setAlignment(Element.ALIGN_LEFT);
            p12.setSpacingAfter(3);
            p12.setSpacingBefore(15);
            p12.setTabSettings(new TabSettings(450f));
            p12.add(Chunk.TABBING);
            p12.add(new Chunk("AUDITOR", new Font(Font.FontFamily.TIMES_ROMAN, 6f, Font.BOLD)));

            document.add(p9);
            document.add(p10);
            document.add(p11);
            document.add(p12);
            //Start New Page
            document.newPage();

            Paragraph p13 = new Paragraph("GPF ABSTRACT", new Font(Font.FontFamily.TIMES_ROMAN, 9.8f, Font.BOLD));
            Paragraph p14 = new Paragraph("BILL NO:", new Font(Font.FontFamily.TIMES_ROMAN, 9.1f, Font.BOLD));
            Paragraph p15 = new Paragraph("T.V No / Date: / ", new Font(Font.FontFamily.TIMES_ROMAN, 9.1f, Font.BOLD));
            p13.setAlignment(Element.ALIGN_CENTER);
            p14.setAlignment(Element.ALIGN_CENTER);
            p15.setAlignment(Element.ALIGN_CENTER);
            p15.setSpacingAfter(15);
            //p15.setSpacingBefore(15);

            document.add(p13);
            document.add(p14);
            document.add(p15);

            PdfPTable dataTable1 = new PdfPTable(2);
            PdfPCell dataCell1 = null;

            dataTable1.setWidths(new int[]{5, 5});
            dataTable1.setWidthPercentage(100);

            dataCell1 = new PdfPCell(new Phrase("PF CODE", new Font(Font.FontFamily.HELVETICA, 6.5f, Font.BOLD)));
            dataCell.setBorderWidth(0.5f);
            dataCell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            dataTable1.addCell(dataCell1);
            dataCell1 = new PdfPCell(new Phrase("TOTAL AMOUNT", new Font(Font.FontFamily.HELVETICA, 6.5f, Font.BOLD)));
            dataCell.setBorderWidth(0.5f);
            dataCell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            dataTable1.addCell(dataCell1);

            document.add(dataTable1);

            Paragraph p16 = new Paragraph("Signature of the D.D.O. with Seal\n"
                    + ",\n"
                    + "Date: ", new Font(Font.FontFamily.HELVETICA, 5f));
            p16.setAlignment(Element.ALIGN_RIGHT);
            //p9.setTextAlignment(TextAlignment.CENTER); 
            p16.setSpacingAfter(15);
            p16.setSpacingBefore(15);

            Paragraph p17 = new Paragraph("Certified that all particulars of recovery have been correctly furnished as per the instruction issued in respect of preparation of G.P.F. Schedules.\n"
                    + "Voucher No.................................... ", new Font(Font.FontFamily.HELVETICA, 5f));
            p17.setAlignment(Element.ALIGN_LEFT);
            p17.setSpacingAfter(10);
            p17.setSpacingBefore(10);
            p17.setTabSettings(new TabSettings(400f));
            p17.add(Chunk.TABBING);
            p17.add(new Chunk("Date of Encashment:// "));

            Paragraph p18 = new Paragraph("FOR USE IN AUDIT OFFICE", new Font(Font.FontFamily.TIMES_ROMAN, 6f, Font.BOLD));
            p18.setAlignment(Element.ALIGN_CENTER);

            Paragraph p19 = new Paragraph("Certified that the name and account No. of individual deduction and total shown in column - 6 have been checked with ref. to the bill vide page 224 of the Audit Manual.\n"
                    + "Certified that the rates of pay shown in column - 4 have been verified with amount drawn in this bill.", new Font(Font.FontFamily.HELVETICA, 5f));
            p19.setAlignment(Element.ALIGN_LEFT);
            p19.setSpacingAfter(3);
            p19.setSpacingBefore(15);
            p19.setTabSettings(new TabSettings(450f));
            p19.add(Chunk.TABBING);
            p19.add(new Chunk("AUDITOR", new Font(Font.FontFamily.TIMES_ROMAN, 5.5f, Font.BOLD)));

            document.add(p16);
            document.add(p17);
            document.add(p18);
            document.add(p19);

            document.close();
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

}
