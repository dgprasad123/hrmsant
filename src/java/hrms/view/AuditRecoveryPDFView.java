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
import hrms.common.CalendarCommonMethods;
import hrms.common.DataBaseFunctions;
import hrms.model.payroll.schedule.AuditRecoveryBean;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.view.AbstractView;

/**
 *
 * @author lenovo pc
 */
public class AuditRecoveryPDFView extends AbstractView {

    public AuditRecoveryPDFView() {
        setContentType("application/pdf");
    }

    @Override
    protected boolean generatesDownloadContent() {
        return true;
    }

    @Override
    protected void renderMergedOutputModel(Map<String, Object> model,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        Document document = new Document(PageSize.A4);
        try {
          
             AuditRecoveryBean arb = (AuditRecoveryBean) model.get("ARHeader");
            List arEmpList = (List) model.get("AREmpList");
            int totalDAmt = (int) model.get("TotalDAmt");
            String totalFig = (String) model.get("TotalFig");
            
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();

            Font f1 = new Font();
            f1.setSize(10);
            f1.setFamily("Times New Roman");

            PdfPTable table = new PdfPTable(9);
            table.setWidths(new float[]{0.5f, 3, 1.2f, 1, 1, 1, 0.8f, 1, 1});
            table.setWidthPercentage(100);

            PdfPCell cell = null;

            cell = new PdfPCell(new Phrase("SCHEDULE OF DEDUCTION OF AUDIT RECOVERIES FOR THE MONTH OF-" + arb.getAqMonth(), new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD)));
            cell.setColspan(9);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(9);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setFixedHeight(10);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Head of Account in which deduction shall be credited-", f1));
            cell.setColspan(9);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(9);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setFixedHeight(10);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("0056-jails-800-0097-02054 Misc. Receipts", f1));
            cell.setColspan(9);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(9);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setFixedHeight(20);
            table.addCell(cell);

            PdfPTable innertable = new PdfPTable(2);
            innertable.setWidths(new int[]{1, 5});
            innertable.setWidthPercentage(100);

            PdfPCell innercell = null;

            innercell = new PdfPCell(new Phrase("Office Name:", f1));
            innercell.setBorder(Rectangle.NO_BORDER);
            innertable.addCell(innercell);
            innercell = new PdfPCell(new Phrase(arb.getOfficeName(), f1));
            innercell.setBorder(Rectangle.NO_BORDER);
            innertable.addCell(innercell);

            cell = new PdfPCell(innertable);
            cell.setColspan(9);
            cell.setBorder(Rectangle.NO_BORDER);
            //cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            innertable = new PdfPTable(2);
            innertable.setWidths(new int[]{1, 5});
            innertable.setWidthPercentage(100);

            innercell = new PdfPCell(new Phrase("Bill No:", f1));
            innercell.setBorder(Rectangle.NO_BORDER);
            innertable.addCell(innercell);
            innercell = new PdfPCell(new Phrase(arb.getBillDesc(), f1));
            innercell.setBorder(Rectangle.NO_BORDER);
            innertable.addCell(innercell);

            cell = new PdfPCell(innertable);
            cell.setColspan(9);
            cell.setBorder(Rectangle.NO_BORDER);
            //cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(9);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setFixedHeight(20);
            table.addCell(cell);

//            AuditRecoveryFunctionality arfunc = new AuditRecoveryFunctionality();
//            ArrayList audList = new ArrayList();
//            audList = arfunc.getAuditList(con, billNo);

            int slno = 0;
            int pageNo = 0;
            int totded = 0;
            String totdedInWord = "";
            if (arEmpList != null && arEmpList.size() > 0) {
                AuditRecoveryBean arbean = null;
                for (int i = 0; i < arEmpList.size(); i++) {
                    arbean = (AuditRecoveryBean) arEmpList.get(i);
                    slno++;
                    totded = totded + Integer.parseInt(arbean.getAmtDeduct());
                   // totdedInWord = Numtowordconvertion.convertNumber(totded);
                    if (pageNo == 0) {
                        pageNo++;
                        printHeader(table, cell, f1);
                    }
                    cell = new PdfPCell(new Phrase(slno + "", f1));
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(arbean.getEmpname() + "\n" + arbean.getEmpdesg(), f1));
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(arbean.getAmtRec(), f1));
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(arbean.getAmtDeduct(), f1));
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(arbean.getNoofInstallment(), f1));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(arbean.getBalance() + "", f1));
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase("", f1));
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase("", f1));
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase("", f1));
                    table.addCell(cell);

                    if (slno % 15 == 0) {
                        printPageTotal(table, cell, totded, totdedInWord, f1);
                        document.add(table);
                        document.newPage();

                        table = new PdfPTable(9);
                        table.setWidths(new float[]{0.5f, 3, 1.2f, 1, 1, 1, 0.8f, 1, 1});
                        table.setWidthPercentage(100);

                        pageNo++;
                        printHeader(table, cell, f1);
                    }
                    if (slno == arEmpList.size()) {
                        printPageTotal(table, cell, totded, totdedInWord, f1);
                    }

                }
            }
            document.add(table);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
           // DataBaseFunctions.closeSqlObjects(con);
        }

        
    }

    private void printHeader(PdfPTable table, PdfPCell cell, Font f1) throws Exception {

        cell = new PdfPCell(new Phrase("Sl No", f1));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Name of the incumbants/\nDesignation", f1));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Amount of\nRecoveries", f1));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Amount to\nbe deducted\nnow", f1));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("No. of\ninstalment", f1));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Balance", f1));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Audit report\nNo. & para", f1));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Head of\nAccount to be Credited ", f1));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Remarks", f1));
        table.addCell(cell);
    }

    private void printPageTotal(PdfPTable table, PdfPCell cell, int deductedAmt, String deductedAmtInWord, Font f1) throws Exception {

        cell = new PdfPCell();
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Total", f1));
        cell.setColspan(2);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(deductedAmt + "", f1));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);
        cell = new PdfPCell();
        cell.setColspan(5);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(9);
        cell.setFixedHeight(20);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("RUPEES " + deductedAmtInWord + " ONLY", f1));
        cell.setColspan(9);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
    }

}


