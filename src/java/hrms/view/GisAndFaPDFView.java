/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.view;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import hrms.model.payroll.schedule.GisAndFaScheduleBean;
import java.io.ByteArrayOutputStream;
import java.io.Writer;
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
public class GisAndFaPDFView extends AbstractView {

    public GisAndFaPDFView() {
        setContentType("application/pdf");
    }

    @Override
    protected boolean generatesDownloadContent() {
        return true;
    }

    @Override
    protected void renderMergedOutputModel(Map<String, Object> model,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        GisAndFaScheduleBean gisAndFaBean = (GisAndFaScheduleBean) model.get("GisAndFaHeader");
        List empList = (List) model.get("GisAndFaList");
        int total = (int) model.get("TotalAmt");
        String totalFig = (String) model.get("TotalFig");

        int x = 0;
        int j = 1;
        int rowCnt = 0;
        int mypage = 1;
        int pageNo = 1;
        ByteArrayOutputStream baos = createTemporaryOutputStream();
        Document document = new Document(PageSize.A4);
        PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());
        writer.setViewerPreferences(PdfWriter.ALLOW_PRINTING | PdfWriter.PageLayoutSinglePage);
        document.open();

        Font hdrTextFont = new Font(Font.FontFamily.TIMES_ROMAN, 10f, Font.BOLD, BaseColor.BLACK);
        Font textFont = new Font(Font.FontFamily.TIMES_ROMAN, 8.4f, Font.NORMAL, BaseColor.BLACK);
        Font bigTextFont = new Font(Font.FontFamily.TIMES_ROMAN, 8.6f, Font.NORMAL, BaseColor.BLACK);
        Font boldTextFont = new Font(Font.FontFamily.TIMES_ROMAN, 8.6f, Font.BOLD, BaseColor.BLACK);

        Font f1 = new Font();
        f1.setSize(10);
        f1.setFamily("Times New Roman");
        f1.setStyle(Font.BOLD);

        Font f2 = new Font();
        f2.setSize(10);
        f2.setFamily("Times New Roman");

        PdfPTable table = null;
        table = new PdfPTable(9);
        table.setWidths(new int[]{3, 15, 8, 6, 8, 6, 6, 7, 6});
        table.setWidthPercentage(100);
        PdfPCell cell = null;
        cell = new PdfPCell(new Phrase("Page No : " + pageNo, hdrTextFont));
        cell.setColspan(9);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(StringUtils.defaultString(gisAndFaBean.getReportName()), hdrTextFont));
        cell.setColspan(9);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(StringUtils.defaultString("FOR THE MONTH OF " + gisAndFaBean.getRecMonth() + "-" + gisAndFaBean.getRecYear()), hdrTextFont));
        cell.setColspan(9);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", hdrTextFont));
        cell.setColspan(9);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("NAME OF THE DEPARTMENT: " + gisAndFaBean.getDeptName(), hdrTextFont));
        cell.setColspan(9);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("OFFICE CODE: " + gisAndFaBean.getOffName(), hdrTextFont));
        cell.setColspan(9);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("DESIGNATION OF DDO: " + gisAndFaBean.getDdoName(), hdrTextFont));
        cell.setColspan(9);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("NAME OF TREASURY: " + gisAndFaBean.getTreasuryName(), hdrTextFont));
        cell.setColspan(9);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Bill No: " + gisAndFaBean.getBilldesc(), hdrTextFont));
        cell.setColspan(9);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        document.add(table);

        table = new PdfPTable(10);
        table.setWidths(new float[]{0.5f, 3, 1.5f, 1.2f, 1.2f, 1.2f, 1.3f, 1.1f, 1.1f, 1.4f});
        table.setWidthPercentage(100);

        printHeader(table, cell, f1);

        if (empList == null || empList.size() == 0) {
            cell = new PdfPCell(new Phrase("No Matching Found", f1));
            cell.setColspan(10);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

        } else if (empList != null && empList.size() > 0) {
            Iterator itr = empList.iterator();
            GisAndFaScheduleBean ar = null;
            while (itr.hasNext()) {
                ar = (GisAndFaScheduleBean) itr.next();
                x = x + 1;
                j = j + 1;
                rowCnt++;
                cell = new PdfPCell(new Phrase(StringUtils.defaultString("" + ar.getSlno()), f2));
                //cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(StringUtils.defaultString(ar.getEmpname()) + "/\n" + StringUtils.defaultString(ar.getCurDesg()), f2));
                //cell.setBorder(Rectangle.NO_BORDER);
                cell.setFixedHeight(30);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(StringUtils.defaultString(ar.getTreasuryVoucherNo()), f2));
                //cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(StringUtils.defaultString(ar.getMonthdrawn()), f2));
                //cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(StringUtils.defaultString(ar.getOriginalAmount()), f2));
                //cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(StringUtils.defaultString(ar.getNoofInstallment()), f2));
                //cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(StringUtils.defaultString(ar.getDeductedAmount()), f2));
                //cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(StringUtils.defaultString(ar.getRecoveryUptoMonth()), f2));
                //cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(StringUtils.defaultString(ar.getBalance()), f2));
                //cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(StringUtils.defaultString(ar.getRemark()), f2));
                //cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(cell);
                if ((rowCnt == 15) && (rowCnt != empList.size())) {
                    cell = new PdfPCell(new Phrase("PAGE:" + mypage++));
                    cell.setColspan(10);
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(cell);

                    document.add(table);
                    document.newPage();

                    table = new PdfPTable(10);
                    table.setWidths(new float[]{0.5f, 3, 1.5f, 1.2f, 1.2f, 1.2f, 1.3f, 1.1f, 1.1f, 1.4f});
                    table.setWidthPercentage(100);

                    printHeader(table, cell, f1);
                } else if ((rowCnt < 15) && (rowCnt == empList.size())) {
                    cell = new PdfPCell();
                    cell.setColspan(10);
                    cell.setFixedHeight(10);
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("RECOVERY FOR THE MONTH OF " + StringUtils.defaultString(gisAndFaBean.getRecMonth()) + "-" + StringUtils.defaultString(gisAndFaBean.getRecYear()), f1));
                    cell.setColspan(6);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(StringUtils.defaultString(""+total), f1));
                    //cell.setColspan(4);
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(cell);
                    cell = new PdfPCell();
                    cell.setColspan(3);
                    table.addCell(cell);

                    cell = new PdfPCell();
                    cell.setColspan(6);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase("RUPEES " + StringUtils.defaultString(totalFig) + " ONLY", f1));
                    cell.setColspan(4);
                    table.addCell(cell);

                    cell = new PdfPCell();
                    cell.setColspan(10);
                    cell.setFixedHeight(50);
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);

                    cell = new PdfPCell();
                    cell.setColspan(5);
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase("Signature of DDO", f2));
                    cell.setColspan(5);
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);

                    cell = new PdfPCell();
                    cell.setColspan(10);
                    cell.setFixedHeight(10);
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);

                    cell = new PdfPCell();
                    cell.setColspan(5);
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase("Date:", f2));
                    cell.setColspan(5);
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);

//                    cell = new PdfPCell(new Phrase("PAGE:" + mypage++));
//                    cell.setColspan(10);
//                    cell.setBorder(Rectangle.NO_BORDER);
//                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//                    table.addCell(cell);

                } else if (((rowCnt + 5) % 20 == 0) && (rowCnt != empList.size())) {

                    cell = new PdfPCell(new Phrase("PAGE:" + mypage++));
                    cell.setColspan(10);
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(cell);

                    document.add(table);
                    document.newPage();

                    table = new PdfPTable(10);
                    table.setWidths(new float[]{0.5f, 3, 1.5f, 1.2f, 1.2f, 1.2f, 1.3f, 1.1f, 1.1f, 1.4f});
                    table.setWidthPercentage(100);

                    printHeader(table, cell, f1);
                } else if (((rowCnt + 5) % 20 == 0) && (rowCnt == empList.size())) {
                    cell = new PdfPCell(new Phrase("PAGE:" + mypage++));
                    cell.setColspan(10);
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(cell);

                    cell = new PdfPCell();
                    cell.setColspan(10);
                    cell.setFixedHeight(10);
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("RECOVERY FOR THE MONTH OF " + StringUtils.defaultString(gisAndFaBean.getRecMonth()) + "-" + StringUtils.defaultString(gisAndFaBean.getRecYear()), f1));
                    cell.setColspan(6);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(StringUtils.defaultString(""+total), f1));
                    //cell.setColspan(4);
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(cell);
                    cell = new PdfPCell();
                    cell.setColspan(3);
                    table.addCell(cell);

                    cell = new PdfPCell();
                    cell.setColspan(6);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase("RUPEES " + StringUtils.defaultString(totalFig) + " ONLY", f1));
                    cell.setColspan(4);
                    table.addCell(cell);

                    cell = new PdfPCell();
                    cell.setColspan(10);
                    cell.setFixedHeight(30);
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("PAGE:" + mypage++));
                    cell.setColspan(10);
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(cell);
                } else if (((rowCnt + 5) % 20 != 0) && (rowCnt == empList.size())) {

                    cell = new PdfPCell();
                    cell.setColspan(10);
                    cell.setFixedHeight(10);
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("RECOVERY FOR THE MONTH OF " + StringUtils.defaultString(gisAndFaBean.getRecMonth()) + "-" + StringUtils.defaultString(gisAndFaBean.getRecYear()), f1));
                    cell.setColspan(6);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(StringUtils.defaultString(""+total), f1));
                    //cell.setColspan(4);
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(cell);
                    cell = new PdfPCell();
                    cell.setColspan(3);
                    table.addCell(cell);

                    cell = new PdfPCell();
                    cell.setColspan(6);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase("RUPEES " + StringUtils.defaultString(totalFig) + " ONLY", f1));
                    cell.setColspan(4);
                    table.addCell(cell);

                    cell = new PdfPCell();
                    cell.setColspan(10);
                    cell.setFixedHeight(50);
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);

                    cell = new PdfPCell();
                    cell.setColspan(5);
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase("Signature of DDO", f2));
                    cell.setColspan(5);
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);

                    cell = new PdfPCell();
                    cell.setColspan(10);
                    cell.setFixedHeight(10);
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);

                    cell = new PdfPCell();
                    cell.setColspan(5);
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase("Date:", f2));
                    cell.setColspan(5);
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("PAGE:" + mypage++));
                    cell.setColspan(10);
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(cell);
                }
            }
            mypage = 1;
            rowCnt = 0;
        }
        document.add(table);
        document.close();
    }

    private void printHeader(PdfPTable table, PdfPCell cell, Font f1) throws Exception {

        cell = new PdfPCell(new Phrase("Sl No", f1));
        //cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Name of the\nEmployee/\nDesignation", f1));
        //cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("T.V. No. in which original adv drawn with Treasury Name", f1));
        //cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Month in which Original Advance was Drawn", f1));
        //cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Amount of Original Advance", f1));
        //cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("No of Installment of recovery", f1));
        //cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Amount Deducted in the Bill", f1));
        //cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Recovery upto the Month", f1));
        //cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Balance Outstanding", f1));
        //cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Remarks", f1));
        //cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
    }

}
