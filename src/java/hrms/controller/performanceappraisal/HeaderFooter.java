package hrms.controller.performanceappraisal;

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

public class HeaderFooter extends PdfPageEventHelper {

    private String empid = null;

    public HeaderFooter(String empid) throws IOException {
        this.empid = empid;
    }

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        try {
            ColumnText ct = new ColumnText(writer.getDirectContent());
            ct.setSimpleColumn(new Rectangle(36, 10, 559, 32));
            ct.addText(new Phrase("Printed By:"+this.empid));
            ct.setAlignment(Element.ALIGN_RIGHT);
            ct.go();
            
			ct.setSimpleColumn(new Rectangle(36, 10, 559, 32));
            ct.addText(new Phrase("Printed Date:"+CommonFunctions.getFormattedOutputDate1(new Date())));
            ct.setAlignment(Element.ALIGN_CENTER);
            ct.go();
			
            ct.setSimpleColumn(new Rectangle(36, 10, 559, 32));
            ct.addText(new Phrase("http://hrmsorissa.gov.in/"));
            ct.setAlignment(Element.ALIGN_LEFT);
            ct.go();
        } catch (DocumentException de) {
            throw new ExceptionConverter(de);
        }
    }
}
