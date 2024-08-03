package hrms.controller.report.annualestablishment;

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
import hrms.common.CommonFunctions;
import java.io.IOException;
import java.util.Date;

public class AERHeaderFooter extends PdfPageEventHelper {

    private String officename = null;

    public AERHeaderFooter(String officename) throws IOException {
        this.officename = officename;
    }

    @Override
    public void onStartPage(PdfWriter writer, Document document) {
        
        //ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase(this.officename), 30, 800, 0);
            ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_LEFT, new Phrase(this.officename,new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK)), 10, 820, 0);
            /*ColumnText ct = new ColumnText(writer.getDirectContent());
            ct.setSimpleColumn(new Rectangle(36, 10, 559, 32));
            ct.addText(new Phrase(this.officename, new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK)));
            ct.setAlignment(Element.ALIGN_LEFT);
            ct.go();*/
        
    }

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        try {
            ColumnText ct = new ColumnText(writer.getDirectContent());
            /*ct.setSimpleColumn(new Rectangle(36, 10, 559, 32));
             ct.addText(new Phrase(this.officename));
             ct.setAlignment(Element.ALIGN_RIGHT);
             ct.go();*/

            ct.setSimpleColumn(new Rectangle(36, 10, 559, 32));
            ct.addText(new Phrase("http://hrmsorissa.gov.in/", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK)));
            ct.setAlignment(Element.ALIGN_LEFT);
            ct.go();
        } catch (DocumentException de) {
            throw new ExceptionConverter(de);
        }
    }

}
