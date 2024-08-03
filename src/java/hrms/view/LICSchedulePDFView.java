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
import hrms.model.common.CommonReportParamBean;
import hrms.model.payroll.schedule.LicScheduleBean;
import hrms.model.payroll.schedule.LicSchedulePolicyBean;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.view.AbstractView;

public class LICSchedulePDFView extends AbstractView {

    public LICSchedulePDFView() {
        setContentType("application/pdf");
    }

    @Override
    protected boolean generatesDownloadContent() {
        return true;
    }

    @Override
    protected void renderMergedOutputModel(Map<String, Object> model,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LicScheduleBean licHeaderBean = (LicScheduleBean) model.get("licBeanHeader");
        CommonReportParamBean crb = (CommonReportParamBean) model.get("crb");
        List licEmpList = (List) model.get("LicEmpList");
        List premiumDtlsArray = (List) model.get("premiumDtlsArray");
        //LicScheduleBean licHeaderBean1 = (LicScheduleBean) model.get("LicEmpList");
        // int grandtotal = (int) model.get("GTotal");
        String totalFig = (String) model.get("GTFig");
        int pageNo = 0;
        int slno = 0;
        int empcnt = 0;
        int total = 0;
        int cnt = 0;
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();
        Font f1 = new Font();
        f1.setSize(9);
        f1.setFamily("Times New Roman");
        PdfPTable table = null;
        table = new PdfPTable(7);
        table.setWidths(new float[]{1, 5, 3, 1.5f, 1.5f, 1.5f, 1.5f});
        table.setWidthPercentage(100);
        PdfPCell cell = null;
        if (licEmpList != null && licEmpList.size() > 0) {
            Iterator itr = licEmpList.iterator();
            LicScheduleBean epdls = null;
            while (itr.hasNext()) {
                epdls = (LicScheduleBean) itr.next();
                slno++;
                boolean empPrinted = false;
                if (pageNo == 0) {
                    /*table = new PdfPTable(7);
                     table.setWidths(new float[]{1,5,3,1.5f,1.5f,1.5f,1.5f});
                     table.setWidthPercentage(100);*/
                    pageNo++;
                    printHeader(crb, table, cell, licHeaderBean.getBilldesc(), pageNo, f1);
                    //cell = new PdfPCell(new Phrase(licHeaderBean., f1));
                    //   cell.setColspan(7);
                    //  table.addCell(cell);
                }
                // ArrayList licpolList = new ArrayList();
                //licpolList = licfunctionalities.getEmployeeDtlsInfo(con,epdls.getEmpCode(),epdls.getAqslno());
                List licpolList = epdls.getPremiumDetails();
                if (licpolList != null && licpolList.size() > 0) {
                    Iterator itr1 = licpolList.iterator();
                    LicSchedulePolicyBean epdpols = null;
                    while (itr1.hasNext()) {
                        epdpols = (LicSchedulePolicyBean) itr1.next();
                        total = total + Integer.parseInt(epdpols.getAmount());
                        if (empPrinted == false) {
                            
                            empcnt++;
                            empPrinted = true;
                            //1st row inside while
                            cell = new PdfPCell(new Phrase(empcnt + "", f1));
                            cell.setBorder(Rectangle.TOP);
                            table.addCell(cell);
                            cell = new PdfPCell(new Phrase(epdls.getEmpname(), f1));
                            cell.setBorder(Rectangle.TOP);
                            table.addCell(cell);
                            cell = new PdfPCell(new Phrase(epdpols.getPolicyNo(), f1));
                            cell.setBorder(Rectangle.TOP);
                            table.addCell(cell);
                            cell = new PdfPCell(new Phrase(epdpols.getRecoveryMonth(), f1));
                            cell.setBorder(Rectangle.TOP);
                            table.addCell(cell);
                            cell = new PdfPCell(new Phrase(epdpols.getAmount(), f1));
                            cell.setBorder(Rectangle.TOP);
                            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                            table.addCell(cell);
                            cell = new PdfPCell(new Phrase(epdpols.getAmount(), f1));
                            cell.setBorder(Rectangle.TOP);
                            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                            table.addCell(cell);
                            cell = new PdfPCell(new Phrase("", f1));
                            cell.setBorder(Rectangle.TOP);
                            table.addCell(cell);

                            //2nd row inside while
                            cell = new PdfPCell(new Phrase("", f1));
                            cell.setBorder(Rectangle.NO_BORDER);
                            table.addCell(cell);
                            cell = new PdfPCell(new Phrase(epdls.getEmpdesg(), f1));
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
                        } else {
                            
                            cell = new PdfPCell(new Phrase("", f1));
                            cell.setBorder(Rectangle.NO_BORDER);
                            table.addCell(cell);
                            cell = new PdfPCell(new Phrase("", f1));
                            cell.setBorder(Rectangle.NO_BORDER);
                            table.addCell(cell);
                            cell = new PdfPCell(new Phrase(epdpols.getPolicyNo(), f1));
                            cell.setBorder(Rectangle.NO_BORDER);
                            table.addCell(cell);
                            cell = new PdfPCell(new Phrase(epdpols.getRecoveryMonth(), f1));
                            cell.setBorder(Rectangle.NO_BORDER);
                            table.addCell(cell);
                            cell = new PdfPCell(new Phrase(epdpols.getAmount(), f1));
                            cell.setBorder(Rectangle.NO_BORDER);
                            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                            table.addCell(cell);
                            cell = new PdfPCell(new Phrase(epdpols.getAmount(), f1));
                            cell.setBorder(Rectangle.NO_BORDER);
                            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                            table.addCell(cell);
                            cell = new PdfPCell(new Phrase("", f1));
                            cell.setBorder(Rectangle.NO_BORDER);
                            table.addCell(cell);
                        }
                    }
                }

                /*if(empcnt%8 == 0){
                 if(empcnt != 0 && empcnt != licList.size()){
                 printPageFooter(table,cell,total,f1);
                 document.add(table);
                 document.newPage();
                                            
                 table = new PdfPTable(7);
                 table.setWidths(new float[]{1,5,3,1.5f,1.5f,1.5f,1.5f});
                 table.setWidthPercentage(100);
                                            
                 pageNo++;
                 printHeader(crb,table,cell,billdesc,pageNo,f1);
                 printCarryForward(table,cell,total,pageNo-1,f1);
                 }
                 }*/
            }
        }
        if (pageNo != 0) {
            printPageFooter(table, cell, total, f1);
            document.add(table);
            document.newPage();

            table = new PdfPTable(7);
            table.setWidths(new float[]{1, 5, 3, 1.5f, 1.5f, 1.5f, 1.5f});
            table.setWidthPercentage(100);

            pageNo++;
            printHeader1(table, cell, f1);
        }

        //if(slno == licList.size()){
        printGrandTotal(crb.getDdoname(), table, cell,total, totalFig, f1);
        document.add(table);
        //}
        if (slno == 0) {
            Paragraph p1 = new Paragraph("There is no record");
            // p1.setAlignment("center");
            document.add(p1);
        }

        document.close();

    }

    private void printHeader(CommonReportParamBean crb, PdfPTable table, PdfPCell cell, String billdesc, int pagno, Font f1) throws Exception {

        String offname = crb.getOfficeen();

        cell = new PdfPCell(new Phrase(offname, new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD)));
        cell.setColspan(7);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Page: " + pagno, f1));
        cell.setColspan(7);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Bill No:", f1));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(billdesc, new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD)));
        cell.setColspan(6);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("8448-LIC Deduction Govt Employees", f1));
        cell.setColspan(7);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("BT SL NO-7100", f1));
        cell.setColspan(7);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("LIFE INSURANCE CORPORATION OF INDIA", f1));
        cell.setColspan(3);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("SCHEDULE FORM 'C' P A CODE NO : " + StringUtils.defaultString(crb.getPacode()), f1));
        cell.setColspan(4);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("_________  DIVISION ", f1));
        cell.setColspan(2);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Designation of Drawing officer :", f1));
        cell.setColspan(5);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", f1));
        cell.setColspan(2);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(crb.getDdoname(), f1));
        cell.setColspan(5);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", f1));
        cell.setColspan(7);
        cell.setFixedHeight(10);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Orissa State Government servant policies        (This statement in triplicate should be", f1));
        cell.setColspan(7);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("statement showing deduction on account          completed everymonth. Two copies to be", f1));
        cell.setColspan(7);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("of premia towords Life Insurance Corporation   sent along with the paybill and other to", f1));
        cell.setColspan(7);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("of India policies from pay salary for :              be retained in Office along with the copy", f1));
        cell.setColspan(7);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(crb.getAqmonth() + "-" + crb.getAqyear() + " of pay Bill", f1));
        cell.setColspan(7);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Name and Address of Institution:", f1));
        cell.setColspan(7);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(offname, f1));
        cell.setColspan(7);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(StringUtils.repeat("-", 173), f1));
        cell.setColspan(7);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Sl No", f1));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Name of Policy Holder", f1));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Policy No", f1));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Month to\nwhich Reco-\nvery Relate", f1));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Premium", f1));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Amt Deducted", f1));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Remarks", f1));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(StringUtils.repeat("-", 173), f1));
        cell.setColspan(7);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", f1));
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

        cell = new PdfPCell(new Phrase(StringUtils.repeat("-", 173), f1));
        cell.setColspan(7);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

    }

    private void printHeader1(PdfPTable table, PdfPCell cell, Font f1) throws Exception {

        cell = new PdfPCell(new Phrase("Sl No", f1));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Name of Policy Holder", f1));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Policy No", f1));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Month to\nwhich Reco-\nvery Relate", f1));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Premium", f1));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Amt Deducted", f1));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Remarks", f1));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(StringUtils.repeat("-", 173), f1));
        cell.setColspan(7);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", f1));
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

        cell = new PdfPCell(new Phrase(StringUtils.repeat("-", 173), f1));
        cell.setColspan(7);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
    }

    private void printPageFooter(PdfPTable table, PdfPCell cell, int pageTotal, Font f1) throws Exception {

        cell = new PdfPCell(new Phrase(StringUtils.repeat("-", 173), f1));
        cell.setColspan(7);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("* Page Total * :", f1));
        cell.setColspan(2);
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
        cell = new PdfPCell(new Phrase(pageTotal + "", f1));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("", f1));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(StringUtils.repeat("-", 173), f1));
        cell.setColspan(7);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        //cell = new PdfPCell(new Phrase("In Words (Rupees "+StringUtils.upperCase(Numtowordconvertion.convertNumber(pageTotal)+" ) Only"),f1));
        cell.setColspan(7);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
    }

    private void printGrandTotal(String ddoname, PdfPTable table, PdfPCell cell,int total, String totalFig, Font f1) throws Exception {

        cell = new PdfPCell(new Phrase(StringUtils.repeat("-", 173), f1));
        cell.setColspan(7);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("* Grand Total * :", f1));
        cell.setColspan(2);
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
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("", f1));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(StringUtils.repeat("-", 173), f1));
        cell.setColspan(7);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("RUPEES " + StringUtils.defaultString(totalFig) + " ONLY", f1));
        // cell = new PdfPCell(new Phrase("In Words (Ruppes "+StringUtils.upperCase(totalFig+" ) Only");
        cell.setColspan(7);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Place : _________", f1));
        cell.setColspan(7);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Date : _________", f1));
        cell.setColspan(7);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", f1));
        cell.setColspan(7);
        cell.setFixedHeight(20);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(ddoname, f1));
        cell.setColspan(7);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(StringUtils.repeat("-", 173), f1));
        cell.setColspan(7);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("FOR USE IN TREASURY/BANK", f1));
        cell.setColspan(7);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Name of Treasury/Bank :_________________________________________", f1));
        cell.setColspan(7);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Amount remitted  : ____________________Challan No., Serial No :_____________", f1));
        cell.setColspan(7);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Date : _________", f1));
        cell.setColspan(7);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Treasury Officer/Bank Agent", f1));
        cell.setColspan(7);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);

    }

}
