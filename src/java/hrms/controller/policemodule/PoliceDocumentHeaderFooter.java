package hrms.controller.policemodule;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.IOException;

public class PoliceDocumentHeaderFooter extends PdfPageEventHelper {

    private String empname = "";

    public PoliceDocumentHeaderFooter(String empname) throws IOException {
        this.empname = empname;
    }

    @Override
    public void onStartPage(PdfWriter writer, Document document) {
        try {
            //ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_LEFT, new Phrase(this.empname, new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK)), 10, 820, 0);
            
            /*ColumnText ct = new ColumnText(writer.getDirectContent());
            
            ct.setSimpleColumn(new Rectangle(36, 10, 559, 32));
            ct.addText(new Phrase("http://hrmsorissa.gov.in/", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK)));
            ct.setAlignment(Element.ALIGN_LEFT);
            ct.go();*/
        } catch (Exception de) {
            throw new ExceptionConverter(de);
        }
    }

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        try {
            /*ColumnText ct = new ColumnText(writer.getDirectContent());

             ct.setSimpleColumn(new Rectangle(36, 10, 559, 32));
             ct.addText(new Phrase("http://hrmsorissa.gov.in/", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK)));
             ct.setAlignment(Element.ALIGN_LEFT);
             ct.go();*/
            ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_LEFT, new Phrase("http://hrmsodisha.gov.in/", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK)), 10, 10, 0);
            
            ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_RIGHT, new Phrase(String.format("PAGE: %d ", writer.getPageNumber()), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK)), 590, 820, 0);
            ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_LEFT, new Phrase(this.empname, new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK)), 10, 820, 0);
        } catch (Exception de) {
            throw new ExceptionConverter(de);
        }
    }
}
