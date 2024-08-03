package hrms.dao.payroll.schedule;

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

public class LTAAGPDFHeaderFooter extends PdfPageEventHelper {

    private String principalHeader;
    private String interestHeader;

    public LTAAGPDFHeaderFooter(String principalHeader, String interestHeader) throws IOException {
        this.principalHeader = principalHeader;
        this.interestHeader = interestHeader;
    }

    @Override
    public void onStartPage(PdfWriter writer, Document document) {
        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_LEFT, new Phrase(this.principalHeader, new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK)), 10, 820, 0);
        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_LEFT, new Phrase(this.interestHeader, new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK)), 350, 820, 0);
    }

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        try {
            ColumnText ct = new ColumnText(writer.getDirectContent());
            
            ct.setSimpleColumn(new Rectangle(36, 10, 559, 32));
            ct.addText(new Phrase("N:B:- The document is digitally signed and electronically generated and therefore needs no ink signed signature as per IT Act 2000. http://hrmsorissa.gov.in/", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK)));
            ct.setAlignment(Element.ALIGN_LEFT);
            ct.go();
        } catch (DocumentException de) {
            throw new ExceptionConverter(de);
        }
    }
}
