package hrms.controller.servicebook;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import hrms.common.CommonFunctions;
import java.io.IOException;
import java.util.Date;
import org.apache.commons.lang.StringUtils;

public class ServiceBookHeaderFooter extends PdfPageEventHelper {

    String empid = null;
    String gpfno = null;
    String name = null;

    public ServiceBookHeaderFooter(String empid, String gpfno, String name) throws IOException {
        this.empid = empid;
        this.gpfno = gpfno;
        this.name = name;
    }

    @Override
    public void onStartPage(PdfWriter writer, Document document) {
        
    }

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        try {
            ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_LEFT, new Phrase("GOVERNMENT OF ODISHA", new Font(Font.FontFamily.HELVETICA, 3, Font.NORMAL, BaseColor.BLACK)), 10, 820, 0);
            ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_LEFT, new Phrase("SERVICE BOOK OF " + this.name + "(HRMS ID-" + this.empid + ",GPF NO-" + this.gpfno + ")", new Font(Font.FontFamily.HELVETICA, 3, Font.NORMAL, BaseColor.BLACK)), 400, 820, 0);
            ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_LEFT, new Phrase(String.format("PAGE: %d ", writer.getPageNumber()), new Font(Font.FontFamily.HELVETICA, 3, Font.NORMAL, BaseColor.BLACK)), 550, 830, 0);

            ColumnText ct = new ColumnText(writer.getDirectContent());

            ct.setSimpleColumn(new Rectangle(20, 10, 759, 32));
            ct.addText(new Phrase("N:B:- The document is digitally signed and electronically generated and therefore needs no ink signed signature as per IT Act 2000. http://hrmsorissa.gov.in/"+StringUtils.repeat(" ", 350)+"Print Date: " + CommonFunctions.getFormattedOutputDate1(new Date()), new Font(Font.FontFamily.HELVETICA, 3, Font.NORMAL, BaseColor.BLACK)));
            ct.setAlignment(Element.ALIGN_LEFT);
            ct.go();
        } catch (DocumentException de) {
            throw new ExceptionConverter(de);
        }
    }
}
