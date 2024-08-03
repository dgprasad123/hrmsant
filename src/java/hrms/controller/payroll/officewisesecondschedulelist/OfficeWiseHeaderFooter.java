package hrms.controller.payroll.officewisesecondschedulelist;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import hrms.common.CommonFunctions;
import java.io.IOException;
import java.util.Date;

public class OfficeWiseHeaderFooter extends PdfPageEventHelper {
    
    private String submittedDate = null;
    private String changedBy = null;

    public OfficeWiseHeaderFooter(String submitteddate,String changedBy) throws IOException {
        this.submittedDate = submitteddate;
        this.changedBy = changedBy;
    }

    @Override
    public void onStartPage(PdfWriter writer, Document document) {
        try {
            ColumnText ct = new ColumnText(writer.getDirectContent());
            ct.setSimpleColumn(new Rectangle(136, 830, 559, 32));
            ct.addText(new Phrase("Submitted On : "+this.submittedDate+" ("+this.changedBy+")"));
            ct.setAlignment(Element.ALIGN_RIGHT);
            ct.go();
        } catch (DocumentException de) {
            throw new ExceptionConverter(de);
        }
    }
    
}
