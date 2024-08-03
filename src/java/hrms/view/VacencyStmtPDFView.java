/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.view;

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
import hrms.model.payroll.schedule.VacancyStatementScheduleBean;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.view.AbstractView;

public class VacencyStmtPDFView extends AbstractView {

    public VacencyStmtPDFView() {
        setContentType("application/pdf");
    }

    @Override
    protected boolean generatesDownloadContent() {
        return true;
    }

    @Override
    protected void renderMergedOutputModel(Map<String, Object> model,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        int slno = 0;
        int pageNo = 0;
        int total = 0;
        Document document = new Document(PageSize.A4);
        try {
            VacancyStatementScheduleBean vssb = (VacancyStatementScheduleBean) model.get("VSHeader");
            List vacancyList = (List) model.get("VEpeList");
            int gTotal = (int) model.get("Gtotal");
            String totalAmtFig = (String) model.get("TotalAmtFig");

            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();

            Font f1 = new Font();
            f1.setSize(9);
            f1.setFamily("Times New Roman");

            PdfPTable table = new PdfPTable(4);
            table.setWidths(new int[]{1, 5, 2, 2});
            table.setWidthPercentage(100);

            PdfPCell cell = null;

            if (vacancyList != null && vacancyList.size() > 0) {
                VacancyStatementScheduleBean vs = null;
                Iterator itr = vacancyList.iterator();
                while (itr.hasNext()) {
                    vs = (VacancyStatementScheduleBean) itr.next();
                    slno++;
                    if (pageNo == 0) {
                        pageNo++;
                        printHeader(vssb.getOffName(), table, cell, vssb.getBillNo(), pageNo, f1);
                    }

                    total = total + vs.getPostno();

                    cell = new PdfPCell(new Phrase(slno + "", f1));
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(vs.getDesignation(), f1));
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(vs.getPayscale(), f1));
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(vs.getPostno() + "", f1));
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);

                    if (slno % 15 == 0) {
                        printTotal(table, cell, "", gTotal, f1);
                        document.add(table);
                        document.newPage();

                        table = new PdfPTable(4);
                        table.setWidths(new int[]{1, 5, 2, 2});
                        table.setWidthPercentage(100);

                        pageNo++;
                        printHeader(vssb.getOffName(), table, cell, vssb.getBillNo(), pageNo, f1);
                    }
                    if (slno == vacancyList.size()) {
                        printTotal(table, cell, "", gTotal, f1);
                        document.add(table);
                    }
                }
            }
            if (slno == 0) {
                Paragraph p1 = new Paragraph("There is no record", f1);
                // p1.setAlignment("center");
                document.add(p1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();

        }

    }

    private void printHeader(String offname, PdfPTable table, PdfPCell cell, String billdesc, int pgno, Font f1) throws Exception {

        cell = new PdfPCell(new Phrase("Page : " + pgno, f1));
        cell.setColspan(4);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(offname, new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD)));
        cell.setColspan(4);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Form No. O.T.C. 23", f1));
        cell.setColspan(4);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("ABSENTEE STATEMENT", f1));
        cell.setColspan(4);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("[ See Subsidiary Rule 223 ]", f1));
        cell.setColspan(4);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(4);
        cell.setFixedHeight(10);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Bill No : ", f1));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(StringUtils.defaultString(billdesc), new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD)));
        cell.setColspan(3);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(StringUtils.repeat("-", 173), f1));
        cell.setColspan(4);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Sl No", f1));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Designation", f1));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("SCALE OF PAY", f1));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("NO OF POST", f1));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(StringUtils.repeat("-", 173), f1));
        cell.setColspan(4);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("1", f1));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("2", f1));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("3", f1));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("4", f1));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(StringUtils.repeat("-", 173), f1));
        cell.setColspan(4);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

    }

    private void printTotal(PdfPTable table, PdfPCell cell, String ddoname, int total, Font f1) throws Exception {

        cell = new PdfPCell(new Phrase(StringUtils.repeat("-", 173), f1));
        cell.setColspan(4);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("* Total * :", f1));
        cell.setColspan(2);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(total + "", f1));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(StringUtils.repeat("-", 173), f1));
        cell.setColspan(4);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(StringUtils.defaultString(ddoname), f1));
        cell.setColspan(4);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

    }

}
