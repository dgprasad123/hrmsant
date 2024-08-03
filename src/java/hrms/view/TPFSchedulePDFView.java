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
import hrms.model.common.CommonReportParamBean;
import hrms.model.payroll.tpfschedule.TPFEmployeeScheduleBean;
import hrms.model.payroll.tpfschedule.TPFScheduleBean;
import hrms.model.payroll.tpfschedule.TpfTypeBean;
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
public class TPFSchedulePDFView extends AbstractView {

    public TPFSchedulePDFView() {
        setContentType("application/pdf");
    }

    @Override
    protected boolean generatesDownloadContent() {
        return true;
    }

    @Override
    protected void renderMergedOutputModel(Map<String, Object> model,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        List tpfAbstractList = (List) model.get("TPFAbstract");
        List tpfEmpList = (List) model.get("TPFList");
        int total = (int) model.get("showAmt");
        String totalFig = (String) model.get("showAmtText");
        String billDesc = (String) model.get("billDesc");
        String billMon = (String) model.get("billMonth");
        String offname = (String) model.get("offname");
        int billYear = (int) model.get("billYear");
        int pageNo = 0;
        int slno = 0;
        int empcnt = 0;
        int tot = 0;
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();
        Font f1 = new Font();
        f1.setSize(9);
        f1.setFamily("Times New Roman");

        PdfPTable table = null;
        table = new PdfPTable(2);
        table.setWidths(new int[]{5, 2});
        table.setWidthPercentage(100);
        PdfPCell cell = null;

        PdfPTable innertable = null;
        innertable = new PdfPTable(8);
        innertable.setWidths(new float[]{1, 1.5f, 3f, 1.5f, 1.5f, 1.5f, 1.5f, 1.5f});
        innertable.setWidthPercentage(100);
        PdfPCell innercell = null;
       
        if (tpfEmpList != null && tpfEmpList.size() > 0) {
            Iterator itr = tpfEmpList.iterator();
            TpfTypeBean tpfTypeBean = null;

            while (itr.hasNext()) {
                tpfTypeBean = (TpfTypeBean) itr.next();
                slno++;
                boolean empPrinted = false;

                pageNo++;
                printHeader(innertable, innercell, billDesc, tpfTypeBean.getGpfType(), pageNo, f1);
                printHeader1(innertable, innercell, billMon, billYear, f1);

                List tpfDtlsList = tpfTypeBean.getEmpGpfList();
                if (tpfDtlsList != null && tpfDtlsList.size() > 0) {
                    Iterator itr1 = tpfDtlsList.iterator();
                    TPFEmployeeScheduleBean tpfSchBean = null;
                    while (itr1.hasNext()) {
                        tpfSchBean = (TPFEmployeeScheduleBean) itr1.next();
                        tot = tot + tpfSchBean.getTotalReleased();

                        empcnt++;

                        //1st row inside while
                        innercell = new PdfPCell(new Phrase(empcnt + "", f1));
                        innercell.setBorder(Rectangle.TOP);
                        innertable.addCell(innercell);
                        innercell = new PdfPCell(new Phrase(tpfSchBean.getAccNo(), f1));
                        innercell.setBorder(Rectangle.TOP);
                        innertable.addCell(innercell);
                        innercell = new PdfPCell(new Phrase(tpfSchBean.getName() + "\n" + tpfSchBean.getDesignation(), f1));
                        innercell.setBorder(Rectangle.TOP);
                        innertable.addCell(innercell);
                        innercell = new PdfPCell(new Phrase(tpfSchBean.getBasicPay() + "\n" + tpfSchBean.getScaleOfPay(), f1));
                        innercell.setBorder(Rectangle.TOP);
                        innertable.addCell(innercell);
                        innercell = new PdfPCell(new Phrase("" + tpfSchBean.getMonthlySub(), f1));
                        innercell.setBorder(Rectangle.TOP);
                        innertable.addCell(innercell);
                        innercell = new PdfPCell(new Phrase(tpfSchBean.getNoOfInst(), f1));
                        innercell.setBorder(Rectangle.TOP);
                        innertable.addCell(innercell);
                        innercell = new PdfPCell(new Phrase("" + tpfSchBean.getTotalReleased(), f1));
                        innercell.setBorder(Rectangle.TOP);
                        innertable.addCell(innercell);
                        innercell = new PdfPCell(new Phrase(tpfSchBean.getDob() + "\n" + tpfSchBean.getDor(), f1));
                        innercell.setBorder(Rectangle.TOP);
                        innertable.addCell(innercell);
                        if (empcnt == tpfDtlsList.size()) {
                            printGrandTotal(innertable, innercell, tot, totalFig, f1);
                        }
                        if (empcnt == 20 && empcnt <= tpfDtlsList.size()) {
                            pageNo++;
                            // printHeader(innertable, innercell, billDesc, tpfTypeBean.getGpfType(), pageNo, f1);
                            printHeader1(innertable, innercell, billMon, billYear, f1);
                        }

                    }

                }
                empcnt = 0;
                tot = 0;

            }
            printAbstract(innertable, innercell, billDesc, billMon, tpfAbstractList, total, totalFig, offname, pageNo, f1);
        }

        document.add(innertable);
        document.close();
    }

    private void printHeader(PdfPTable table, PdfPCell cell, String billDesc, String gpfType, int pagno, Font f1) throws Exception {

        cell = new PdfPCell(new Phrase("Page: " + pagno, f1));
        cell.setColspan(8);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(StringUtils.defaultString("TEACHER PROVIDENT FUND"), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
        cell.setColspan(8);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("BILL NO: " + StringUtils.defaultString(billDesc), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
        cell.setColspan(8);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("SCHEDULE OF " + StringUtils.defaultString(gpfType), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
        cell.setColspan(8);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Demand No-8009- State G.P.F Withdrawals", new Font(Font.FontFamily.TIMES_ROMAN, 8)));
        cell.setColspan(8);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("1. This form should not be used for transactions of Teacher Provident Fund for which Form No. O.T.C. 63 has been provided. The account Nos. should be arranged in serial order.", new Font(Font.FontFamily.TIMES_ROMAN, 8)));
        cell.setColspan(8);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("2. Col. 1 quote account number unfailingly. The guide letters e.g. I.C.S.(ICS Provident Fund) etc., should be unvariably, prefixed to account numbers. ", new Font(Font.FontFamily.TIMES_ROMAN, 8)));
        cell.setColspan(8);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("3. In the remark column give reasons for discontinuance of subscription such as 'Proceeded on leave', 'Transfered to ........................... office ................... District ....................', 'Quitted service', 'Died or Discontinued under Rule II'. ", new Font(Font.FontFamily.TIMES_ROMAN, 8)));
        cell.setColspan(8);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("4. In the remark column write description against every new name such as 'New Subscriber', 'came on transfer from ........................... office ................... District ..................... resumed subscription'. ", new Font(Font.FontFamily.TIMES_ROMAN, 8)));
        cell.setColspan(8);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("5. Separate Schedule should be prepared in respect of person whose accounts are kept by different Accountant-General. ", new Font(Font.FontFamily.TIMES_ROMAN, 8)));
        cell.setColspan(8);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(cell);
    }

    private void printHeader1(PdfPTable table, PdfPCell cell, String billMon, int billYear, Font f1) throws Exception {
        cell = new PdfPCell(new Phrase("", f1));
        cell.setColspan(8);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setFixedHeight(20);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(StringUtils.defaultString("DEDUCTION MADE FROM THE SALARY FOR " + billMon + " " + billYear), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
        cell.setColspan(8);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", f1));
        cell.setColspan(8);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setFixedHeight(20);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(StringUtils.repeat("-", 173), f1));
        cell.setColspan(8);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Sl No", f1));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("ACCOUNT NO\nDATE OF ENTRY\nINTO GOVT. SER.", f1));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("NAME OF THE SUBSCRIBER\nDESIGNATION", f1));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("BASIC PAY\nSCALE OF PAY", f1));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("MONTHLY SUBSCRIPTION", f1));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("REFUND OF\nWITHDRAWALS\nAMT/NO. OF INST.", f1));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("TOTAL RELEASED", f1));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("REMARKS\nD.O.B and D.O.R.", f1));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(StringUtils.repeat("-", 173), f1));
        cell.setColspan(8);
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
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("5", f1));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("6", f1));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("7", f1));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("8", f1));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(StringUtils.repeat("-", 173), f1));
        cell.setColspan(8);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

    }

    private void printPageFooter(PdfPTable table, PdfPCell cell, int pageTotal, Font f1) throws Exception {

        cell = new PdfPCell(new Phrase(StringUtils.repeat("-", 173), f1));
        cell.setColspan(8);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("* Carry Forward * :", f1));
        cell.setColspan(8);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(StringUtils.repeat("-", 173), f1));
        cell.setColspan(8);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        //cell = new PdfPCell(new Phrase("In Words (Rupees "+StringUtils.upperCase(Numtowordconvertion.convertNumber(pageTotal)+" ) Only"),f1));
        cell.setColspan(8);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
    }

    private void printGrandTotal(PdfPTable table, PdfPCell cell, int total, String totalFig, Font f1) throws Exception {

        cell = new PdfPCell(new Phrase(StringUtils.repeat("-", 173), f1));
        cell.setColspan(8);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("*Total * :", f1));
        //cell.setColspan(4);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", f1));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", f1));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", f1));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", f1));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", f1));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(total + "", f1));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", f1));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(StringUtils.repeat("-", 173), f1));
        cell.setColspan(8);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("RUPEES " + StringUtils.defaultString(totalFig) + " ONLY", f1));
        // cell = new PdfPCell(new Phrase("In Words (Ruppes "+StringUtils.upperCase(totalFig+" ) Only");
        cell.setColspan(8);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", f1));
        cell.setColspan(8);
        cell.setFixedHeight(20);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Certified that all particulars of recovery have been correctly furnished as per the instruction issued in respect of preparation of G.P.F. Schedules. ", f1));
        cell.setColspan(8);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Voucher No.................................... ", f1));
        cell.setColspan(8);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("FOR USE IN AUDIT OFFICE", f1));
        cell.setColspan(8);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Certified that the name and account No. of individual deduction and total shown in column - 6 have been checked with ref. to the bill vide page 224 of the Audit Manual. ", f1));
        cell.setColspan(8);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Certified that the rates of pay shown in column - 4 have been verified with amount drawn in this bill. ", f1));
        cell.setColspan(8);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("AUDITOR", f1));
        cell.setColspan(8);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);

    }

    private void printAbstract(PdfPTable table, PdfPCell cell, String billDesc, String billMonth, List tpfAbstractList, int grandTotal, String totalFig, String offname, int pagno, Font f1) throws Exception {
        int total = 0;
        cell = new PdfPCell(new Phrase("Page: " + pagno, f1));
        cell.setColspan(8);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(StringUtils.defaultString("TPF ABSTRACT"), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
        cell.setColspan(8);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("BILL NO: " + StringUtils.defaultString(billDesc), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
        cell.setColspan(8);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("PF CODE ", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
        cell.setColspan(4);
        cell.setBorder(Rectangle.BOX);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("TOTAL AMOUNT ", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
        cell.setColspan(4);
        cell.setBorder(Rectangle.BOX);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        if (tpfAbstractList != null && tpfAbstractList.size() > 0) {
            Iterator itr = tpfAbstractList.iterator();
            TPFScheduleBean tpfSchBean = null;

            while (itr.hasNext()) {
                tpfSchBean = (TPFScheduleBean) itr.next();
                total = total + Integer.parseInt(tpfSchBean.getTotalamt());
                cell = new PdfPCell(new Phrase(tpfSchBean.getPfcode(), new Font(Font.FontFamily.TIMES_ROMAN, 10)));
                cell.setColspan(4);
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("" + total, new Font(Font.FontFamily.TIMES_ROMAN, 10)));
                cell.setColspan(4);
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
            }
        }
        cell = new PdfPCell(new Phrase(StringUtils.repeat("-", 173), f1));
        cell.setColspan(8);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("*Total * :", f1));
        //cell.setColspan(4);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", f1));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", f1));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", f1));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", f1));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", f1));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(grandTotal + "", f1));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", f1));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(StringUtils.repeat("-", 173), f1));
        cell.setColspan(8);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("RUPEES " + StringUtils.defaultString(totalFig) + " ONLY", f1));
        // cell = new PdfPCell(new Phrase("In Words (Ruppes "+StringUtils.upperCase(totalFig+" ) Only");
        cell.setColspan(8);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", f1));
        cell.setColspan(8);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setFixedHeight(30);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Signature of the D.D.O. with Seal ", new Font(Font.FontFamily.TIMES_ROMAN, 8)));
        cell.setColspan(8);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(offname, new Font(Font.FontFamily.TIMES_ROMAN, 10)));
        cell.setColspan(8);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Date:", new Font(Font.FontFamily.TIMES_ROMAN, 10)));
        cell.setColspan(8);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Certified that all particulars of recovery have been correctly furnished as per the instruction issued in respect of preparation of G.P.F. Schedules. ", f1));
        cell.setColspan(8);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Voucher No.................................... ", f1));
        cell.setColspan(8);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("FOR USE IN AUDIT OFFICE", f1));
        cell.setColspan(8);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Certified that the name and account No. of individual deduction and total shown in column - 6 have been checked with ref. to the bill vide page 224 of the Audit Manual. ", f1));
        cell.setColspan(8);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Certified that the rates of pay shown in column - 4 have been verified with amount drawn in this bill. ", f1));
        cell.setColspan(8);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", f1));
        cell.setColspan(8);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setFixedHeight(20);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("AUDITOR", f1));
        cell.setColspan(8);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", f1));
        cell.setColspan(8);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setFixedHeight(20);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Under Rs " + grandTotal + " " + StringUtils.defaultString(totalFig) + " ONLY", f1));
        cell.setColspan(8);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", f1));
        cell.setColspan(8);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setFixedHeight(20);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Schedule LIII - Form No. 186", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
        cell.setColspan(4);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("ORIGINAL/DUPLICATE/TRIPLICATE ", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
        cell.setColspan(4);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", f1));
        cell.setColspan(8);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setFixedHeight(20);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("(See S.R.s 52)" + total, new Font(Font.FontFamily.TIMES_ROMAN, 10)));
        cell.setColspan(3);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("CHALLAN NO. " + total, new Font(Font.FontFamily.TIMES_ROMAN, 10)));
        cell.setColspan(5);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", f1));
        cell.setColspan(8);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setFixedHeight(20);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("(O.T.C-06)Challan of Cash paid into the Treasury/Sub-Treasury at ", new Font(Font.FontFamily.TIMES_ROMAN, 10)));
        cell.setColspan(3);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("", new Font(Font.FontFamily.TIMES_ROMAN, 10)));
        cell.setColspan(5);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", f1));
        cell.setColspan(8);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setFixedHeight(20);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("State/Reserve Bank of India", new Font(Font.FontFamily.TIMES_ROMAN, 8)));
        cell.setColspan(3);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("", new Font(Font.FontFamily.TIMES_ROMAN, 10)));
        cell.setColspan(5);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", f1));
        cell.setColspan(8);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setFixedHeight(20);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("To be filled in by the remitter ", new Font(Font.FontFamily.TIMES_ROMAN, 8)));
        cell.setColspan(5);
        cell.setBorder(Rectangle.BOX);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("To be filled by the Departmental Officer of the Treasury ", new Font(Font.FontFamily.TIMES_ROMAN, 8)));
        cell.setColspan(3);
        cell.setBorder(Rectangle.BOX);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("By Whom tendered ", new Font(Font.FontFamily.TIMES_ROMAN, 8)));
        cell.setBorder(Rectangle.BOX);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Name [Designation] and address of the person \n on whose behalf money is paid  ", new Font(Font.FontFamily.TIMES_ROMAN, 8)));
        cell.setBorder(Rectangle.BOX);
        cell.setColspan(2);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Full particulars of the remittance and of authority [if any]   ", new Font(Font.FontFamily.TIMES_ROMAN, 8)));
        cell.setBorder(Rectangle.BOX);
        cell.setColspan(2);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Amount    ", new Font(Font.FontFamily.TIMES_ROMAN, 8)));
        cell.setBorder(Rectangle.BOX);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Head of account ", new Font(Font.FontFamily.TIMES_ROMAN, 8)));
        cell.setBorder(Rectangle.BOX);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Order of the Bank ", new Font(Font.FontFamily.TIMES_ROMAN, 8)));
        cell.setBorder(Rectangle.BOX);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Name: \n \n Signature: ", new Font(Font.FontFamily.TIMES_ROMAN, 8)));
        cell.setBorder(Rectangle.BOX);
        cell.setRotation(90);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(offname, new Font(Font.FontFamily.TIMES_ROMAN, 8)));
        cell.setBorder(Rectangle.BOX);
        cell.setColspan(2);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Monthly Subscription of TPF for the month of " + billMonth + " of no of \n employees in Bill No -" + billDesc + " \n \n \n Total:" , new Font(Font.FontFamily.TIMES_ROMAN, 8)));
        cell.setBorder(Rectangle.BOX);
        cell.setColspan(2);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Rs. " + grandTotal + "\n \n \n  Rs." + grandTotal, new Font(Font.FontFamily.TIMES_ROMAN, 8)));
        cell.setBorder(Rectangle.BOX);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("8009 State Provident Fund \n & Other Provident Fund \n Misc Provident Fund of the\n employees of the Aided \n Educational Institutions", new Font(Font.FontFamily.TIMES_ROMAN, 8)));
        cell.setBorder(Rectangle.BOX);
        cell.setRotation(90);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Signature and full \n designation of the officer\nordering the money to be paid in.\n\nDate- ", new Font(Font.FontFamily.TIMES_ROMAN, 8)));
        cell.setBorder(Rectangle.BOX);
        cell.setRotation(90);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("1) To be used only in the case of remittances to Bank through an Officer of the Government\n"
                + "(in words) Rs " + grandTotal + "/- (RUPEES " + totalFig + ") ONLY\n"
                + "Rs " + grandTotal + "/- (RUPEES " + totalFig + ") ONLY by T.C to 8009 GPF of AEI ", new Font(Font.FontFamily.TIMES_ROMAN, 8)));
        cell.setBorder(Rectangle.BOX);
        cell.setColspan(8);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        
         cell = new PdfPCell(new Phrase("", f1));
        cell.setColspan(8);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setFixedHeight(20);
        table.addCell(cell);
        
        cell = new PdfPCell(new Phrase("Received payment\nTreasurer ", new Font(Font.FontFamily.TIMES_ROMAN, 8)));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(2);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        
        cell = new PdfPCell(new Phrase("Accountant", new Font(Font.FontFamily.TIMES_ROMAN, 8)));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(2);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        
        cell = new PdfPCell(new Phrase("Date..............................................Treasury Officer/Agent\n(See Instructions on overleaf) ", new Font(Font.FontFamily.TIMES_ROMAN, 8)));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(4);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        
        

    }

}
