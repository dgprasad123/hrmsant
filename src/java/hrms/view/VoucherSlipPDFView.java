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
import hrms.model.payroll.schedule.VoucherSlipBean;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.view.AbstractView;

public class VoucherSlipPDFView extends AbstractView {

    public VoucherSlipPDFView() {
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
        VoucherSlipBean voucherBean = (VoucherSlipBean) model.get("VoucherHeadr");
        String net = "";
        String gross = "";
        try {
            Font f1 = new Font();
            f1.setSize(10);
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();

            Paragraph p1 = new Paragraph("VOUCHER SLIP", new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD));
            p1.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(p1);

            Paragraph p2 = new Paragraph("FORM O.G.F.R. -25", f1);
            p2.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(p2);

            Paragraph p3 = new Paragraph("(See: Rule 318)", f1);
            p3.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(p3);

            Paragraph p4 = new Paragraph("(To be returned in original by the Treasury Officer)", f1);
            p4.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(p4);

            Font f2 = new Font();
            f2.setSize(10);

            PdfPTable table = new PdfPTable(3);
            table.setWidths(new float[]{4, .25f, 4});
            table.setWidthPercentage(100);

            PdfPCell cell = null;

            cell = new PdfPCell(new Phrase("Demand No:-" + voucherBean.getDemandno() + voucherBean.getMajorhead() + voucherBean.getMinorheaddesc() + voucherBean.getMinorhead() + voucherBean.getSubminorhead1desc() + voucherBean.getPostType() + voucherBean.getSubminorhead2desc() + voucherBean.getSubminorhead3(), f2));
            cell.setColspan(3);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("", f2));
            cell.setColspan(3);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(StringUtils.repeat("~", 88), f2));
            cell.setColspan(3);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            /*1st Row*/
            cell = new PdfPCell(new Phrase("", f2));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("|", f2));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("", f2));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(cell);

            /*2nd Row*/
            cell = new PdfPCell(new Phrase("", f2));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("|", f2));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("(To be filled in the Treasury)", f2));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(cell);

            /*3rd Row*/
            cell = new PdfPCell(new Phrase("", f2));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("|", f2));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("", f2));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            /*4th Row*/
            cell = new PdfPCell(new Phrase("", f2));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("|", f2));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("", f2));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            /*5th Row*/
            cell = new PdfPCell(new Phrase("To", f2));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("|", f2));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("To", f2));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            /*6th Row*/
            cell = new PdfPCell(new Phrase("The " + StringUtils.defaultString(voucherBean.getTreasuryName()), f2));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("|", f2));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("The " + StringUtils.defaultString(voucherBean.getDdoName()), f2));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(cell);

            /*7th Row*/
            cell = new PdfPCell(new Phrase("", f2));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("|", f2));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("", f2));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            /*10th Row*/
            cell = new PdfPCell(new Phrase("", f2));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("|", f2));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("", f2));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            /*11th Row*/
            cell = new PdfPCell(new Phrase("Please furnish the Treasury Voucher No and Date of the Bill sent herewith for encashment", f2));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("|", f2));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Returned with Treasury Voucher No and date as noted below:-", f2));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            //12th row
            cell = new PdfPCell(new Phrase("", f2));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("|", f2));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("", f2));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            /*14th row*/
            cell = new PdfPCell(new Phrase("", f2));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("|", f2));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("", f2));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            /*15th row*/
            cell = new PdfPCell(new Phrase("", f2));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("|", f2));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("", f2));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            /*16th row*/
            cell = new PdfPCell(new Phrase(StringUtils.defaultString(voucherBean.getDdoName()), f2));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("|", f2));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("", f2));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            /*17th row*/
            cell = new PdfPCell(new Phrase("(Drawing Officer)", f2));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("|", f2));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Signature..................................................", f2));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            //18th row
            cell = new PdfPCell(new Phrase("", f2));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("|", f2));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Treasury Officer", f2));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            //19th row
            cell = new PdfPCell(new Phrase("", f2));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("|", f2));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("", f2));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            //20th row
            cell = new PdfPCell(new Phrase(StringUtils.repeat("~", 88), f2));
            cell.setColspan(3);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            //21th row
            cell = new PdfPCell(new Phrase("", f2));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("|", f2));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("", f2));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            //22th row
            cell = new PdfPCell(new Phrase("Bill No:- " + StringUtils.defaultString(voucherBean.getBillDesc()), f2));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("|", f2));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Amount Paid........................................................", f2));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            //23th row
            cell = new PdfPCell(new Phrase("", f2));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("|", f2));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("", f2));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            //24th row
            cell = new PdfPCell(new Phrase("Bill Particulars:-", f2));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("|", f2));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("T.V.No..............................", f2));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            //25th row
            cell = new PdfPCell(new Phrase("", f2));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("|", f2));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("", f2));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            //26th row
            cell = new PdfPCell(new Phrase("Monthly PayBill of ", f2));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("|", f2));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Date..............................", f2));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            //27th row
            cell = new PdfPCell(new Phrase(StringUtils.defaultString(voucherBean.getOfficeName()), f2));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("|", f2));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("", f2));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

           //28th row
            cell = new PdfPCell(new Phrase("for " + (Integer.parseInt(voucherBean.getMonth()) + 1) + "/" + StringUtils.defaultString(voucherBean.getYear()), f2));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("|", f2));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("", f2));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            //30th row
            cell = new PdfPCell(new Phrase("", f2));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("|", f2));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("", f2));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            //31th row
            cell = new PdfPCell(new Phrase("Gross   : " + Double.valueOf(voucherBean.getGrossAmount() + "").longValue() + StringUtils.leftPad("", 40) + gross, f2));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("|", f2));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("", f2));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            //33th row
            cell = new PdfPCell(new Phrase("Net   : " + Double.valueOf(voucherBean.getNetAmount() + "").longValue() + StringUtils.leftPad("", 40) + net, f2));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("|", f2));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("", f2));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            //35th row
            cell = new PdfPCell(new Phrase("", f2));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("|", f2));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("", f2));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            //36th row
            cell = new PdfPCell(new Phrase("", f2));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("|", f2));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("", f2));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            //37th row
            cell = new PdfPCell(new Phrase("", f2));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("|", f2));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("", f2));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            //38th row
            cell = new PdfPCell(new Phrase("Signature of Accountant", f2));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("|", f2));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Signature of Treasury", f2));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            //39th row
            cell = new PdfPCell(new Phrase("", f2));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("|", f2));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Accountant", f2));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            //40th row
            cell = new PdfPCell(new Phrase("", f2));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("|", f2));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("", f2));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            //41th row
            cell = new PdfPCell(new Phrase("", f2));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("|", f2));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("", f2));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(StringUtils.repeat("~", 88), f2));
            cell.setColspan(3);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            document.add(table);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();

        }

    }
}
