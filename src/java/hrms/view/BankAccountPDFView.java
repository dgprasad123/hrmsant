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
import hrms.model.payroll.schedule.BankAcountScheduleBean;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.view.AbstractView;

public class BankAccountPDFView extends AbstractView {

    public BankAccountPDFView() {
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
            BankAcountScheduleBean basb = (BankAcountScheduleBean) model.get("BankAcHeader");
            List empList = (List) model.get("BankAccList");
            int nettotal = (int) model.get("NetTotal");
            int totalSbAc = (int) model.get("TotalSbAc");
            int totalDDO = (int) model.get("TotalDDO");
            String netAmtFig = (String) model.get("NetAmtFig");

            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();

            Font f2 = new Font();
            f2.setSize(9);

            int slno = 0;
            int pageNo = 0;
            int pagetotal = 0;
            int otherdeptotal = 0;
            int totalreltotal = 0;

            PdfPTable table = null;

            table = new PdfPTable(7);
            table.setWidths(new float[]{1, 2.2f, 5.3f, 2, 1.5f, 1.8f, 1.5f});
            table.setWidthPercentage(100);
            PdfPCell cell = null;

            String desigArray[] = null;

            if (empList != null && empList.size() > 0) {
                Iterator itr = empList.iterator();
                BankAcountScheduleBean ebean = null;
                while (itr.hasNext()) {
                    ebean = (BankAcountScheduleBean) itr.next();

                    slno++;
                    if (pageNo == 0) {
                        table = new PdfPTable(7);
                        table.setWidths(new float[]{1, 2.2f, 5.3f, 2, 1.5f, 1.8f, 1.5f});
                        table.setWidthPercentage(100);
                        printHeader(basb, table, cell);
                        pageNo++;
                    }

                    desigArray = wrapText(ebean.getDesignation(), 25);

                    pagetotal = pagetotal + Integer.parseInt(ebean.getNetAmount());
                    otherdeptotal = otherdeptotal + ebean.getOtherDeposits();
                    totalreltotal = totalreltotal + ebean.getTotalReleased();

                    String banckAccNo = ebean.getAccountNo();
                    if (banckAccNo == null) {
                        banckAccNo = "";
                    }

                    //1st row inside while
                    cell = new PdfPCell(new Paragraph(slno + "", f2));
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(banckAccNo, f2));
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(ebean.getEmpname(), f2));
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(ebean.getGpfNo(), f2));
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase("", f2));
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase("", f2));
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase("", f2));
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);

                    //2nd row inside while
                    cell = new PdfPCell(new Phrase("", f2));
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase("", f2));
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(ebean.getDesignation(), f2));
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(ebean.getNetAmount() + "", f2));
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(ebean.getTowardsLoan() + "", f2));
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(ebean.getTotalReleased() + "", f2));
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(ebean.getOtherDeposits() + "", f2));
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(cell);

                    /*if(desigArray.length > 1){
                     //2nd row inside while continued
                     cell = new PdfPCell(new Phrase("",f2));
                     cell.setBorder(Rectangle.NO_BORDER);
                     table.addCell(cell);
                     cell = new PdfPCell(new Phrase("",f2));
                     cell.setBorder(Rectangle.NO_BORDER);
                     table.addCell(cell);
                     cell = new PdfPCell(new Phrase(desigArray[1],f2));
                     cell.setBorder(Rectangle.NO_BORDER);
                     table.addCell(cell);
                     cell = new PdfPCell(new Phrase("",f2));
                     cell.setBorder(Rectangle.NO_BORDER);
                     table.addCell(cell);
                     cell = new PdfPCell(new Phrase("",f2));
                     cell.setBorder(Rectangle.NO_BORDER);
                     table.addCell(cell);
                     cell = new PdfPCell(new Phrase("",f2));
                     cell.setBorder(Rectangle.NO_BORDER);
                     table.addCell(cell);
                     cell = new PdfPCell(new Phrase("",f2));
                     cell.setBorder(Rectangle.NO_BORDER);
                     table.addCell(cell);
                     }*/
                    if (slno % 16 == 0) {
                        printPageTotal(table, cell, pagetotal, otherdeptotal, totalreltotal, f2, false);
                        document.add(table);
                        document.newPage();

                        table = new PdfPTable(7);
                        table.setWidths(new float[]{1, 2.2f, 5.3f, 2, 1.5f, 1.8f, 1.5f});
                        table.setWidthPercentage(100);

                        printHeader(ebean, table, cell);
                        printCarryForward(pagetotal,netAmtFig, table, cell, false);
                        pageNo++;
                    }
                    if (slno == empList.size()) {
                        printPageTotal(table, cell, pagetotal, otherdeptotal, totalreltotal, f2, true);
                        printCarryForward(pagetotal,netAmtFig, table, cell, true);
                        document.add(table);
                    }
                }
            }
            if (slno == 0) {
                Paragraph p1 = new Paragraph("There is no record");
                // p1.setAlignment("center");
                document.add(p1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            document.close();
        }

    }

    private void printPageTotal(PdfPTable table, PdfPCell cell, int pagetotal, int otherdeptotal, int totalreltotal, Font f2, boolean isLastPage) {
        cell = new PdfPCell(new Phrase(StringUtils.repeat("-", 173), f2));
        cell.setColspan(7);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        // Page Total
        cell = new PdfPCell(new Phrase("", f2));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Page Total:", f2));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("", f2));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(Double.valueOf(pagetotal + "").longValue() + "", f2));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("", f2));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        if (isLastPage == true) {
            cell = new PdfPCell(new Phrase(Double.valueOf(totalreltotal + "").longValue() + "", f2));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
        } else {
            cell = new PdfPCell();
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
        }
        if (isLastPage == true) {
            cell = new PdfPCell(new Phrase(Double.valueOf(otherdeptotal + "").longValue() + "", f2));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
        } else {
            cell = new PdfPCell();
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
        }
    }

    private void printHeader(BankAcountScheduleBean basb, PdfPTable table, PdfPCell cell) throws Exception {

        int payBillMonth = 0;
        int payBillYear = 0;

        Font f1 = new Font();
        f1.setSize(9);
        String offname = basb.getOffName();

        cell = new PdfPCell(new Phrase(offname, new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD)));
        cell.setColspan(7);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        Font f5 = new Font();
        f5.setSize(9);
        f5.setStyle(Font.BOLD);

        cell = new PdfPCell(new Phrase("Bill No: ", f1));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(basb.getBilldesc(), f5));
        cell.setColspan(6);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Schedule - Bank Statement", f5));
        cell.setColspan(7);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("SCHEDULE OF - Employee-wise list with SB A/c No(Name of the Bank)", f1));
        cell.setColspan(7);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", f1));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("        Net Amount, Loan/Advance Liability Amount", f1));
        cell.setColspan(6);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", f1));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("        Amount Credited to SB A/c of the employee", f1));
        cell.setColspan(6);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", f1));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("    Amount Credited to CA A/C of DDO", f1));
        cell.setColspan(6);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

//        if (Integer.parseInt(basb.getMonth())  <= 10) {
//            payBillMonth = Integer.parseInt(basb.getMonth()) + 1;
//            payBillYear = Integer.parseInt(basb.getYear());
//        } else {
//            payBillMonth = 0;
//            payBillYear = Integer.parseInt(basb.getYear()) + 1;
//        }
        cell = new PdfPCell(new Phrase("", f1));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("        For " + basb.getMonth() + " " + basb.getYear() + " payable in " + payBillMonth + " " + payBillYear + "'", f1));
        cell.setColspan(6);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Name of the DDO who maintains these accounts", f1));
        cell.setColspan(7);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        Font f2 = new Font();
        f2.setSize(9);

        cell = new PdfPCell(new Phrase(StringUtils.repeat("-", 173), f2));
        cell.setColspan(7);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        //1st row
        cell = new PdfPCell(new Phrase("Sl\nNo", f2));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("SB A/c no\nName of the\nBank", f2));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Name/\nDesignation", f2));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("(PF No)\nNet Amount\n(in Rs.)", f2));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Loan/\nAdvance\nLiabilty\nAmount\n(in Rs)", f2));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Amt Credited\nSB A/c of\nthe employee\n(in Rs)", f2));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Amt Credited\nCA A/c of\nthe DDO\n(in Rs)", f2));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);

        //4th row
        cell = new PdfPCell(new Phrase(StringUtils.repeat("-", 173), f2));
        cell.setColspan(7);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        //5th row
        cell = new PdfPCell(new Phrase("1", f2));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("2", f2));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("3", f2));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("4", f2));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("5", f2));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("6", f2));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("7", f2));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);

        //6th row
        cell = new PdfPCell(new Phrase(StringUtils.repeat("-", 173), f2));
        cell.setColspan(7);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
    }

    public void printCarryForward(int pagetotal,String netAmtFig, PdfPTable table, PdfPCell cell, boolean isLastPage) throws Exception {
        Font f2 = new Font();
        f2.setSize(9);
        if (isLastPage == true) {
            cell = new PdfPCell(new Phrase(StringUtils.repeat("-", 173), f2));
            cell.setColspan(7);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            //TOTAL
            cell = new PdfPCell(new Phrase("", f2));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("TOTAL", f2));
            cell.setBorder(Rectangle.NO_BORDER);
            //cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("", f2));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(pagetotal + "", f2));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("", f2));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("", f2));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("", f2));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(StringUtils.repeat("-", 173), f2));
            cell.setColspan(7);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("RUPEES " + netAmtFig + " ONLY", f2));
            cell.setColspan(7);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
        } else {
            //CARRIED FROM PREVIOUS PAGE
            cell = new PdfPCell(new Phrase("", f2));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("CARRIED FROM PREVIOUS PAGE :", f2));
            cell.setColspan(2);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(pagetotal + "", f2));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("", f2));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("", f2));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("", f2));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(StringUtils.repeat("-", 173), f2));
            cell.setColspan(7);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
        }

    }

    private String[] wrapText(String text, int len) {
        // return empty array for null text
        if (text == null) {
            return new String[]{};
        }

        // return text if len is zero or less
        if (len <= 0) {
            return new String[]{text};
        }

        // return text if less than length
        if (text.length() <= len) {
            return new String[]{text};
        }

        char[] chars = text.toCharArray();
        Vector lines = new Vector();
        StringBuffer line = new StringBuffer();
        StringBuffer word = new StringBuffer();

        for (int i = 0; i < chars.length; i++) {
            word.append(chars[i]);

            if (chars[i] == ' ') {
                if ((line.length() + word.length()) > len) {
                    lines.add(line.toString());
                    line.delete(0, line.length());
                }

                line.append(word);
                word.delete(0, word.length());
            }
        }

        // handle any extra chars in current word
        if (word.length() > 0) {
            if ((line.length() + word.length()) > len) {
                lines.add(line.toString());
                line.delete(0, line.length());
            }
            line.append(word);
        }

        // handle extra line
        if (line.length() > 0) {
            lines.add(line.toString());
        }

        String[] ret = new String[lines.size()];
        int c = 0; // counter
        for (Enumeration e = lines.elements(); e.hasMoreElements(); c++) {
            ret[c] = (String) e.nextElement();
        }

        return ret;
    }

}
