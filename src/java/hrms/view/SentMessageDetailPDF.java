/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.view;

import com.itextpdf.text.Anchor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import hrms.model.empinfo.EmployeeMessage;
import hrms.model.login.LoginUserBean;
import java.util.ArrayList;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.view.AbstractView;

/**
 *
 * @author Manisha
 */
public class SentMessageDetailPDF extends AbstractView {

    public SentMessageDetailPDF() {
        setContentType("application/pdf");
    }

    @Override

    protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest hsr, HttpServletResponse response) throws Exception {

        Document document = new Document(PageSize.A4);
        EmployeeMessage employeeMessage = (EmployeeMessage) model.get("employeeMessage");
        //String filePath = (String) model.get("filePath");
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();
        Font f1 = new Font();
        f1.setSize(10);
        f1.setFamily("Times New Roman");

        PdfPTable table = null;
        PdfPTable innertable = null;

        table = new PdfPTable(6);
        table.setWidths(new int[]{1, 3, 3, 3, 2, 3});
        table.setWidthPercentage(100);

        PdfPCell cell = null;
        PdfPCell innercell = null;

        cell = new PdfPCell(new Phrase("Sent Message List Report", new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.NORMAL)));
        cell.setColspan(4);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(Rectangle.TOP | Rectangle.LEFT | Rectangle.RIGHT);
        cell.setBorderWidth(1f);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(7);
        cell.setFixedHeight(14);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" 1. Message Title:", f1));
        cell.setColspan(2);
        cell.setBorder(Rectangle.LEFT);
        cell.setBorderWidth(1f);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(employeeMessage.getMsgTitle(), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)));
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

        cell = new PdfPCell(new Phrase(" 2. Message Date:", f1));
        cell.setColspan(2);
        cell.setBorder(Rectangle.LEFT);
        cell.setBorderWidth(1f);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(employeeMessage.getMessageondate(), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)));
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

        cell = new PdfPCell(new Phrase(" 3. Sender Name:", f1));
        cell.setColspan(2);
        cell.setBorder(Rectangle.LEFT);
        cell.setBorderWidth(1f);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(employeeMessage.getSenderName(), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)));
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

        cell = new PdfPCell(new Phrase(" 4. Receiver Name:", f1));
        cell.setColspan(2);
        cell.setBorder(Rectangle.LEFT);
        cell.setBorderWidth(1f);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(employeeMessage.getReceiverName(), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)));
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

        cell = new PdfPCell(new Phrase(" 5. Message Details:", f1));
        cell.setColspan(2);
        cell.setBorder(Rectangle.LEFT);
        cell.setBorderWidth(1f);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(employeeMessage.getMessage(), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)));
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

        cell = new PdfPCell(new Phrase(" 6. Attachment:", f1));
        cell.setColspan(2);
        cell.setBorder(Rectangle.LEFT);
        cell.setBorderWidth(1f);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(employeeMessage.getAttachementname(), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)));
        cell.setColspan(2);
        cell.setBorder(Rectangle.RIGHT);
        cell.setBorderWidth(1f);
        table.addCell(cell);

       /* Paragraph anchor_Holder = new Paragraph();
        Anchor anchor = new Anchor("PAR");
        anchor.setReference("https://apps.hrmsodisha.gov.in/downloadEmployeeAttachment.htm?attachmentid=" + employeeMessage.getAttachmentid() + "");
        cell = new PdfPCell();
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        anchor_Holder.add(anchor);
        cell.addElement(anchor_Holder);
        table.addCell(cell); */

        cell = new PdfPCell();
        cell.setColspan(3);
        cell.setFixedHeight(10);
        cell.setBorder(Rectangle.LEFT);
        cell.setBorderWidth(1f);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(3);
        cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.BOTTOM);
        cell.setBorderWidth(1f);
        table.addCell(cell);

        document.add(table);
        document.close();
    }

}
