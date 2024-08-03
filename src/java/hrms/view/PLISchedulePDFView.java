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
import hrms.common.DataBaseFunctions;
import hrms.model.payroll.schedule.PLIScheduleBean;
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
public class PLISchedulePDFView extends AbstractView {

    public PLISchedulePDFView() {
        setContentType("application/pdf");
    }

    @Override
    protected boolean generatesDownloadContent() {
        return true;
    }

    @Override
    protected void renderMergedOutputModel(Map<String, Object> model,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            PLIScheduleBean plibean = (PLIScheduleBean) model.get("pliHeader");
            List pliEmpList = (List) model.get("PliEmpList");
            int totalAmt = (int) model.get("TotalAmt");
            String totalAmtFig = (String) model.get("TotalAmtFig");
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();
            Font f1 = new Font();
            f1.setSize(9);
            f1.setFamily("Times New Roman");
            PdfPTable table = new PdfPTable(2);
            table.setWidths(new int[]{2, 2});
            table.setWidthPercentage(100);
            PdfPCell cell = null;

            cell = new PdfPCell(new Phrase("SCHEDULE OF RECOVERY OF PLI PREMIUM", new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD)));
            cell.setColspan(2);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("FOR THE MONTH OF " + StringUtils.defaultString(plibean.getMonthYear()), f1));
            cell.setColspan(2);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(StringUtils.defaultString(plibean.getOfficeName()), f1));
            cell.setColspan(2);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Bill No: " + StringUtils.defaultString(plibean.getBilldesc()), f1));
            cell.setColspan(2);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(2);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("POSTAL LIFE INSURANCE", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.UNDERLINE)));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Form No.Tr. 183", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.UNDERLINE)));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Orissa State Government Servant's\npolicies statementshowing deduction\non account of premia towards Postal\nLife Insurance Policiesfrom pay salary bill.\nfor ________________ 20", f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Designation of Drawing Officer ............................................................. / This statement should be completed in triplicated every month. Two copies to be sent alongwith the pay bill & the other to be retained in the offices alongwith the copy of the"
                    + " pay bill.", f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(2);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Name:............................................................", f1));
            cell.setBorder(Rectangle.NO_BORDER);
            //cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(2);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Address:.........................................................", f1));
            cell.setBorder(Rectangle.NO_BORDER);
            //cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(2);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Institution:......................................................", f1));
            cell.setBorder(Rectangle.NO_BORDER);
            //cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);

            document.add(table);
            //document.newPage();

            table = new PdfPTable(8);
            table.setWidths(new float[]{0.8f, 3.5f, 3.5f, 1.9f, 1.3f, 1.2f, 1, 2});
            table.setWidthPercentage(100);

            cell = new PdfPCell();
            cell.setColspan(8);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            printHeader(table, cell, f1);

            cell = new PdfPCell();
            cell.setColspan(8);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            int k = 0;
            int pageNo = 1;
            int j = 0;
            int cnt = 0;

            // pliList = plifunc.getList(con, billno, plibean.getOffname());
            if (pliEmpList != null && pliEmpList.size() > 0) {
                Iterator itr = pliEmpList.iterator();
                PLIScheduleBean plisc = null;
                while (itr.hasNext()) {
                    plisc = (PLIScheduleBean) itr.next();
                    k++;
                    j++;

                    cell = new PdfPCell(new Phrase(k + "", f1));
                    cell.setBorder(Rectangle.NO_BORDER);
                    //cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(plisc.getEmpname(), f1));
                    cell.setBorder(Rectangle.NO_BORDER);
                    //cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(plisc.getEmpdesg(), f1));
                    cell.setBorder(Rectangle.NO_BORDER);
                    //cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(plisc.getPolicyNo(), f1));
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(plisc.getRecMonth(), f1));
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(plisc.getAmount(), f1));
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(plisc.getTotal(), f1));
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase("", f1));
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);

                    if (j == 15 && pageNo == 1 && pliEmpList.size() != k) {
                        j = 0;

                        cell = new PdfPCell(new Phrase(StringUtils.repeat("-", 170), f1));
                        cell.setColspan(8);
                        cell.setBorder(Rectangle.NO_BORDER);
                        table.addCell(cell);

                        cell = new PdfPCell(new Phrase("Carry Forward", f1));
                        cell.setColspan(5);
                        cell.setBorder(Rectangle.NO_BORDER);
                        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        table.addCell(cell);
                        cell = new PdfPCell(new Phrase(plisc.getCarryForward(), f1));
                        cell.setBorder(Rectangle.NO_BORDER);
                        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        table.addCell(cell);
                        cell = new PdfPCell();
                        cell.setColspan(2);
                        cell.setBorder(Rectangle.NO_BORDER);
                        table.addCell(cell);

                        cell = new PdfPCell(new Phrase("Page:" + pageNo++, f1));
                        cell.setColspan(8);
                        cell.setBorder(Rectangle.NO_BORDER);
                        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        table.addCell(cell);

                        document.add(table);
                        document.newPage();

                        table = new PdfPTable(8);
                        table.setWidths(new float[]{0.8f, 3.5f, 3.5f, 1.9f, 1.3f, 1.2f, 1, 2});
                        table.setWidthPercentage(100);

                        cell = new PdfPCell(new Phrase("Brought Forward", f1));
                        cell.setColspan(5);
                        cell.setBorder(Rectangle.NO_BORDER);
                        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        table.addCell(cell);
                        cell = new PdfPCell(new Phrase(plisc.getCarryForward(), f1));
                        cell.setBorder(Rectangle.NO_BORDER);
                        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        table.addCell(cell);
                        cell = new PdfPCell();
                        cell.setColspan(2);
                        cell.setBorder(Rectangle.NO_BORDER);
                        table.addCell(cell);

                        cell = new PdfPCell(new Phrase(StringUtils.repeat("-", 170), f1));
                        cell.setColspan(8);
                        cell.setBorder(Rectangle.NO_BORDER);
                        table.addCell(cell);

                        printHeader(table, cell, f1);

                        cell = new PdfPCell();
                        cell.setColspan(8);
                        cell.setFixedHeight(10);
                        cell.setBorder(Rectangle.NO_BORDER);
                        table.addCell(cell);
                    } else if (j == 20 && pageNo > 1 && pliEmpList.size() != k) {
                        j = 0;

                        cell = new PdfPCell(new Phrase(StringUtils.repeat("-", 170), f1));
                        cell.setColspan(8);
                        cell.setBorder(Rectangle.NO_BORDER);
                        table.addCell(cell);

                        cell = new PdfPCell(new Phrase("Carry Forward", f1));
                        cell.setColspan(5);
                        cell.setBorder(Rectangle.NO_BORDER);
                        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        table.addCell(cell);
                        cell = new PdfPCell(new Phrase(plisc.getCarryForward(), f1));
                        cell.setBorder(Rectangle.NO_BORDER);
                        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        table.addCell(cell);
                        cell = new PdfPCell();
                        cell.setColspan(2);
                        cell.setBorder(Rectangle.NO_BORDER);
                        table.addCell(cell);

                        cell = new PdfPCell(new Phrase("Page:" + pageNo++, f1));
                        cell.setColspan(8);
                        cell.setBorder(Rectangle.NO_BORDER);
                        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        table.addCell(cell);

                        document.add(table);
                        document.newPage();

                        table = new PdfPTable(8);
                        table.setWidths(new float[]{0.8f, 3.5f, 3.5f, 1.9f, 1.3f, 1.2f, 1, 2});
                        table.setWidthPercentage(100);

                        cell = new PdfPCell(new Phrase("Brought Forward", f1));
                        cell.setColspan(5);
                        cell.setBorder(Rectangle.NO_BORDER);
                        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        table.addCell(cell);
                        cell = new PdfPCell(new Phrase(plisc.getCarryForward(), f1));
                        cell.setBorder(Rectangle.NO_BORDER);
                        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        table.addCell(cell);
                        cell = new PdfPCell();
                        cell.setColspan(2);
                        cell.setBorder(Rectangle.NO_BORDER);
                        table.addCell(cell);

                        cell = new PdfPCell(new Phrase(StringUtils.repeat("-", 170), f1));
                        cell.setColspan(8);
                        cell.setBorder(Rectangle.NO_BORDER);
                        table.addCell(cell);

                        printHeader(table, cell, f1);

                        cell = new PdfPCell();
                        cell.setColspan(8);
                        cell.setFixedHeight(10);
                        cell.setBorder(Rectangle.NO_BORDER);
                        table.addCell(cell);
                    } else if (j <= 20 && pliEmpList.size() == k) {

                        cell = new PdfPCell(new Phrase(StringUtils.repeat("-", 170), f1));
                        cell.setColspan(8);
                        cell.setBorder(Rectangle.NO_BORDER);
                        table.addCell(cell);

                        cell = new PdfPCell(new Phrase("TOTAL FOR THE MONTH OF " + plibean.getMonthYear(), f1));
                        cell.setColspan(5);
                        cell.setBorder(Rectangle.NO_BORDER);
                        //cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        table.addCell(cell);
                        cell = new PdfPCell(new Phrase(plisc.getCarryForward(), f1));
                        cell.setBorder(Rectangle.NO_BORDER);
                        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        table.addCell(cell);
                        cell = new PdfPCell();
                        cell.setColspan(2);
                        cell.setBorder(Rectangle.NO_BORDER);
                        table.addCell(cell);

                        cell = new PdfPCell();
                        cell.setColspan(8);
                        cell.setFixedHeight(10);
                        cell.setBorder(Rectangle.NO_BORDER);
                        table.addCell(cell);

                        cell = new PdfPCell(new Phrase("Rupees " + plisc.getTotFig() + " Only", f1));
                        cell.setColspan(8);
                        cell.setBorder(Rectangle.NO_BORDER);
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table.addCell(cell);
                    }
                    if (pageNo > 1) {
                        cnt = 14;
                    } else {
                        cnt = 9;
                    }
                }
            }

            cell = new PdfPCell();
            cell.setColspan(8);
            cell.setFixedHeight(30);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Signature of D.D.O", f1));
            cell.setColspan(4);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(8);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Date:", f1));
            cell.setColspan(4);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(8);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Note:", f1));
            //cell.setColspan(4);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("1. The avoid credit for premiums being given to wrong accounts care should be taken to"
                    + " state the correct policy numbers.\n2. If remittance is made by cheque the No. Amount & Drawing Bank should be mentioned in the remark column.\n3. Please give full particulars of previous office in case of transfer to your office. In the"
                    + " column's Remark-above.", f1));
            cell.setColspan(7);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Page:" + pageNo++, f1));
            cell.setColspan(8);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);

            document.add(table);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }

    }

    private void printHeader(PdfPTable table, PdfPCell cell, Font f1) {

        cell = new PdfPCell(new Phrase("Sl No", f1));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Name of the Employee", f1));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Designation of the Employee", f1));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Policy No", f1));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Recovery\nMonth", f1));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Amount", f1));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Total", f1));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Remarks", f1));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
    }

}
