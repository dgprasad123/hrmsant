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
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import hrms.model.payroll.schedule.ExcessPayBean;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.view.AbstractView;

/**
 *
 * @author lenovo pc
 */
public class ExcessPayPDFView extends AbstractView {

    public ExcessPayPDFView() {
        setContentType("application/pdf");
    }

    @Override
    protected boolean generatesDownloadContent() {
        return true;
    }

    @Override
    protected void renderMergedOutputModel(Map<String, Object> model,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        int cnt = 1;
        int x = 0;
        int pageNo = 1;

        String monthYear = "";
        String offName = "";
        boolean pageTotalingPrinted = false;
        ExcessPayBean excessBean = (ExcessPayBean) model.get("ExcessHeader");
        List empList = (List) model.get("ExcessList");
//        int grossTot = (int) model.get("GrossTot");
//        int taxTot = (int) model.get("TaxTot");
        String taxTotFig = (String) model.get("TaxTotFig");

        Document document = new Document(PageSize.A4);
        try {
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();

            Font f1 = new Font();
            f1.setSize(10);
            f1.setFamily("Times New Roman");

            PdfPTable table = new PdfPTable(6);
            table.setWidths(new float[]{0.5f, 2, 3, 1.5f, 1.5f, 2});
            table.setWidthPercentage(100);

            PdfPCell cell = null;

            cell = new PdfPCell(new Phrase("EXCESS PAY RECOVERY", new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD)));
            cell.setColspan(6);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("FOR THE MONTH OF " + StringUtils.defaultString(excessBean.getMonthYear()), f1));
            cell.setColspan(6);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(6);
            cell.setFixedHeight(40);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Name of the Department:", f1));
            cell.setColspan(2);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(StringUtils.defaultString(excessBean.getDeptName()), f1));
            cell.setColspan(4);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Name of the Office:", f1));
            cell.setColspan(2);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(StringUtils.defaultString(excessBean.getOffName()), f1));
            cell.setColspan(4);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Designation of DDO:", f1));
            cell.setColspan(2);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(StringUtils.defaultString(excessBean.getDdoDegn()), f1));
            cell.setColspan(4);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Bill No:", f1));
            cell.setColspan(2);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(StringUtils.defaultString(excessBean.getBillDesc()), f1));
            cell.setColspan(4);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(6);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            printHeader(table, cell, f1);
            int totalGross = 0;
            int totalTax = 0;

            if (empList != null && empList.size() > 0) {
                Iterator itr = empList.iterator();
                ExcessPayBean ptobjclass = null;
                while (itr.hasNext()) {
                    ptobjclass = (ExcessPayBean) itr.next();
                    x++;
                    totalGross = Integer.parseInt(ptobjclass.getTotalGross());
                    totalTax = Integer.parseInt(ptobjclass.getTotalTax());

                    cell = new PdfPCell(new Phrase(cnt + "", f1));
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(StringUtils.defaultString(ptobjclass.getEmpName()), f1));
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(StringUtils.defaultString(ptobjclass.getEmpDegn()), f1));
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(ptobjclass.getEmpGrossSal(), f1));
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(ptobjclass.getEmpTaxOnProffesion(), f1));
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(cell);
                    cell = new PdfPCell();
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);
                    cnt++;
                    if (x == 25 && pageNo == 1) {
                        x = 0;

                        cell = new PdfPCell(new Phrase(StringUtils.repeat("-", 155), f1));
                        cell.setColspan(6);
                        cell.setBorder(Rectangle.NO_BORDER);
                        table.addCell(cell);

                        cell = new PdfPCell();
                        cell.setColspan(3);
                        cell.setBorder(Rectangle.NO_BORDER);
                        table.addCell(cell);
                        cell = new PdfPCell(new Phrase("Carry Forward", f1));
                        cell.setBorder(Rectangle.NO_BORDER);
                        table.addCell(cell);
                        cell = new PdfPCell(new Phrase(StringUtils.defaultString(ptobjclass.getTotalTax()), f1));
                        cell.setBorder(Rectangle.NO_BORDER);
                        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        table.addCell(cell);
                        cell = new PdfPCell();
                        cell.setBorder(Rectangle.NO_BORDER);
                        table.addCell(cell);

                        cell = new PdfPCell(new Phrase("Page No:" + pageNo, f1));
                        cell.setColspan(6);
                        cell.setBorder(Rectangle.NO_BORDER);
                        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        table.addCell(cell);

                        pageNo++;
                        document.add(table);
                        document.newPage();

                        table = new PdfPTable(6);
                        table.setWidths(new float[]{0.5f, 2, 3, 1.5f, 1.5f, 2});
                        table.setWidthPercentage(100);
                        printHeader(table, cell, f1);

                        cell = new PdfPCell();
                        cell.setColspan(3);
                        cell.setBorder(Rectangle.NO_BORDER);
                        table.addCell(cell);
                        cell = new PdfPCell(new Phrase("Brought Forward", f1));
                        cell.setBorder(Rectangle.NO_BORDER);
                        table.addCell(cell);
                        cell = new PdfPCell(new Phrase(StringUtils.defaultString(ptobjclass.getTotalTax()), f1));
                        cell.setBorder(Rectangle.NO_BORDER);
                        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        table.addCell(cell);
                        cell = new PdfPCell();
                        cell.setBorder(Rectangle.NO_BORDER);
                        table.addCell(cell);

                        cell = new PdfPCell(new Phrase(StringUtils.repeat("-", 155), f1));
                        cell.setColspan(6);
                        cell.setBorder(Rectangle.NO_BORDER);
                        table.addCell(cell);
                    } else if (x == 27 && cnt == empList.size() + 1) {
                        pageTotalingPrinted = true;

                        cell = new PdfPCell(new Phrase(StringUtils.repeat("-", 155), f1));
                        cell.setColspan(6);
                        cell.setBorder(Rectangle.NO_BORDER);
                        table.addCell(cell);

                        cell = new PdfPCell(new Phrase("TOTAL FOR THE MONTH OF " + monthYear, f1));
                        cell.setColspan(3);
                        cell.setBorder(Rectangle.NO_BORDER);
                        table.addCell(cell);
                        cell = new PdfPCell(new Phrase(ptobjclass.getTotalGross(), f1));
                        cell.setBorder(Rectangle.NO_BORDER);
                        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        table.addCell(cell);
                        cell = new PdfPCell(new Phrase(ptobjclass.getTotalTax(), f1));
                        cell.setBorder(Rectangle.NO_BORDER);
                        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        table.addCell(cell);
                        cell = new PdfPCell();
                        cell.setBorder(Rectangle.NO_BORDER);
                        table.addCell(cell);

                        cell = new PdfPCell(new Phrase("(RUPEES " + taxTotFig + ") ONLY", f1));
                        cell.setColspan(6);
                        cell.setBorder(Rectangle.NO_BORDER);
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table.addCell(cell);

                        cell = new PdfPCell(new Phrase(StringUtils.repeat("-", 155), f1));
                        cell.setColspan(6);
                        cell.setBorder(Rectangle.NO_BORDER);
                        table.addCell(cell);
                    } else if (x == 27 && cnt != empList.size()) {
                        x = 0;

                        cell = new PdfPCell(new Phrase(StringUtils.repeat("-", 155), f1));
                        cell.setColspan(6);
                        cell.setBorder(Rectangle.NO_BORDER);
                        table.addCell(cell);

                        cell = new PdfPCell();
                        cell.setColspan(3);
                        cell.setBorder(Rectangle.NO_BORDER);
                        table.addCell(cell);
                        cell = new PdfPCell(new Phrase("Carry Forward", f1));
                        cell.setBorder(Rectangle.NO_BORDER);
                        table.addCell(cell);
                        cell = new PdfPCell(new Phrase(StringUtils.defaultString(ptobjclass.getTotalTax()), f1));
                        cell.setBorder(Rectangle.NO_BORDER);
                        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        table.addCell(cell);
                        cell = new PdfPCell();
                        cell.setBorder(Rectangle.NO_BORDER);
                        table.addCell(cell);

                        cell = new PdfPCell(new Phrase("Page No:" + pageNo, f1));
                        cell.setColspan(6);
                        cell.setBorder(Rectangle.NO_BORDER);
                        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        table.addCell(cell);

                        pageNo++;
                        document.add(table);
                        document.newPage();

                        table = new PdfPTable(6);
                        table.setWidths(new float[]{0.5f, 2, 3, 1.5f, 1.5f, 2});
                        table.setWidthPercentage(100);
                        printHeader(table, cell, f1);

                        cell = new PdfPCell();
                        cell.setColspan(3);
                        cell.setBorder(Rectangle.NO_BORDER);
                        table.addCell(cell);
                        cell = new PdfPCell(new Phrase("Brought Forward", f1));
                        cell.setBorder(Rectangle.NO_BORDER);
                        table.addCell(cell);
                        cell = new PdfPCell(new Phrase(StringUtils.defaultString(ptobjclass.getTotalTax()), f1));
                        cell.setBorder(Rectangle.NO_BORDER);
                        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        table.addCell(cell);
                        cell = new PdfPCell();
                        cell.setBorder(Rectangle.NO_BORDER);
                        table.addCell(cell);

                        cell = new PdfPCell(new Phrase(StringUtils.repeat("-", 155), f1));
                        cell.setColspan(6);
                        cell.setBorder(Rectangle.NO_BORDER);
                        table.addCell(cell);

                        //pageNo++;
                    } else if (cnt == empList.size() + 1 && x < 25) {
                        pageNo++;

                        cell = new PdfPCell(new Phrase(StringUtils.repeat("-", 155), f1));
                        cell.setColspan(6);
                        cell.setBorder(Rectangle.NO_BORDER);
                        table.addCell(cell);

                        cell = new PdfPCell(new Phrase("TOTAL FOR THE MONTH OF " + monthYear, f1));
                        cell.setColspan(3);
                        cell.setBorder(Rectangle.NO_BORDER);
                        table.addCell(cell);
                        cell = new PdfPCell(new Phrase(ptobjclass.getTotalGross(), f1));
                        cell.setBorder(Rectangle.NO_BORDER);
                        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        table.addCell(cell);
                        cell = new PdfPCell(new Phrase(ptobjclass.getTotalTax(), f1));
                        cell.setBorder(Rectangle.NO_BORDER);
                        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        table.addCell(cell);
                        cell = new PdfPCell();
                        cell.setBorder(Rectangle.NO_BORDER);
                        table.addCell(cell);

                        cell = new PdfPCell(new Phrase("(RUPEES " + taxTotFig.toUpperCase() + ") ONLY", f1));
                        cell.setColspan(6);
                        cell.setBorder(Rectangle.NO_BORDER);
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table.addCell(cell);

                        cell = new PdfPCell(new Phrase(StringUtils.repeat("-", 155), f1));
                        cell.setColspan(6);
                        cell.setBorder(Rectangle.NO_BORDER);
                        table.addCell(cell);
                    } else if (cnt == empList.size() + 1 && x < 27) {
                        pageNo++;
                    }
                }
            }

            document.add(table);

        } catch (Exception e) {
            e.printStackTrace();;
        } finally {
            document.close();
        }

    }

    private void printHeader(PdfPTable table, PdfPCell cell, Font f1) {

        cell = new PdfPCell(new Phrase("Sl No", f1));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Name of the Employee", f1));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Designation", f1));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Gross Salary", f1));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Excess Pay", f1));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Remark", f1));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
    }

}
