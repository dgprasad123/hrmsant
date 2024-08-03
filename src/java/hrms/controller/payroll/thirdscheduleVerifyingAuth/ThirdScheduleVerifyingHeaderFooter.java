package hrms.controller.payroll.thirdscheduleVerifyingAuth;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.IOException;

public class ThirdScheduleVerifyingHeaderFooter extends PdfPageEventHelper {
    
    private String offName;
    private String empid;
    private String curDate;
    
    public ThirdScheduleVerifyingHeaderFooter(String offName,String empid) throws IOException {
        this.offName = offName;
        this.empid = empid;
    }

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        
        Font ffont = new Font(Font.FontFamily.UNDEFINED, 6, Font.NORMAL);
        
        PdfContentByte cb = writer.getDirectContent();
        //Phrase header = new Phrase("this is a header", ffont);
        Phrase footer = new Phrase(offName+"( HRMS ID - "+empid+")", ffont);
        /*ColumnText.showTextAligned(cb, Element.ALIGN_CENTER,
                header,
                (document.right() - document.left()) / 2 + document.leftMargin(),
                document.top() + 10, 0);*/
        ColumnText.showTextAligned(cb, Element.ALIGN_CENTER,
                footer,
                (document.right() - document.left()) / 2 + document.leftMargin(),
                document.bottom() - 10, 0);
    }

}
