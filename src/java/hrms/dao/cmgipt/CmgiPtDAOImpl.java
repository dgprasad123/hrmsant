package hrms.dao.cmgipt;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.common.Numtowordconvertion;
import hrms.model.cmgipt.CmgiPtBean;
import hrms.model.common.CommonReportParamBean;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.apache.commons.lang.StringUtils;

public class CmgiPtDAOImpl implements CmgiPtDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void ptPDF(Document document, CommonReportParamBean crb, String billNo) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        PdfPTable table = null;
        PdfPCell cell = null;

        int pageNo = 0;
        int basic = 0;
        int slno = 0;
        int total = 0;
        int cnt = 0;

        String year = "";
        String month = "";
        String billdesc = "";
        int payBillMonth = 0;
        int payBillYear = 0;

        ArrayList ptList = null;
        try {
            con = this.dataSource.getConnection();

            String sql = "SELECT AQ_MONTH,AQ_YEAR,BILL_DESC,BILL_DATE,VCH_NO,GROSS_AMT,DED_AMT FROM BILL_MAST WHERE BILL_NO=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(billNo));
            rs = pst.executeQuery();
            if (rs.next()) {
                year = rs.getString("AQ_YEAR");
                month = rs.getString("AQ_MONTH");
                billdesc = rs.getString("BILL_DESC");
            }

            Font f1 = new Font();
            f1.setSize(10);
            f1.setFamily("Times New Roman");

            if (crb.getAqmonth() <= 10) {
                payBillMonth = crb.getAqmonth() + 1;
                payBillYear = Integer.parseInt(year);
            } else {
                payBillMonth = 0;
                payBillYear = Integer.parseInt(year) + 1;
            }

            ptList = getPTScheduleDtlsForEmployee(billNo);
            if (ptList != null && ptList.size() > 0) {
                Iterator itr = ptList.iterator();
                CmgiPtBean pts = null;
                while (itr.hasNext()) {
                    pts = (CmgiPtBean) itr.next();
                    slno++;
                    if (pageNo == 0) {
                        table = new PdfPTable(4);
                        table.setWidths(new float[]{0.5f, 5, 0.5f, 0.7f});
                        table.setWidthPercentage(100);
                        pageNo++;
                        printHeader(crb, table, cell, pageNo, billdesc, f1);
                    }

                    total = total + Integer.parseInt(pts.getEmpTaxOnProffesion());

                    //1st row inside while
                    cell = new PdfPCell(new Paragraph(slno + "", f1));
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(pts.getEmpName(), f1));
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(pts.getEmpGrossSal(), f1));
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(pts.getEmpTaxOnProffesion(), f1));
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(cell);

                    //2nd row inside while
                    cell = new PdfPCell(new Paragraph("", f1));
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(pts.getEmpDesig(), f1));
                    cell.setColspan(3);
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);

                    if (slno % 20 == 0) {
                        printPageTotal(table, cell, f1, total);
                        document.add(table);
                        document.newPage();

                        table = new PdfPTable(4);
                        table.setWidths(new float[]{0.5f, 5, 0.5f, 0.7f});
                        table.setWidthPercentage(100);

                        pageNo++;
                        printHeader(crb, table, cell, pageNo, billdesc, f1);
                    }
                    if (slno == ptList.size()) {
                        printPageFooter(crb, table, cell, f1, total, payBillMonth, payBillYear);
                        document.add(table);
                    }
                }
            }
            if (ptList.size() == 0 || ptList == null) {
                printHeader(crb, table, cell, pageNo + 1, billdesc, f1);
                document.add(table);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    private void printPageTotal(PdfPTable table, PdfPCell cell, Font f1, int pageTotal) throws Exception {

        cell = new PdfPCell(new Phrase(StringUtils.repeat("-", 155), f1));
        cell.setColspan(4);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" * Page Total * :", f1));
        cell.setColspan(2);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("", f1));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(pageTotal + "", f1));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("In Words (Rupees " + StringUtils.upperCase(Numtowordconvertion.convertNumber(pageTotal) + " ) Only"), f1));
        cell.setColspan(4);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);

    }

    private void printPageFooter(CommonReportParamBean crb, PdfPTable table, PdfPCell cell, Font f1, int pageTotal, int payBillMonth, int payBillYear) throws Exception {

        cell = new PdfPCell(new Phrase(StringUtils.repeat("-", 155), f1));
        cell.setColspan(4);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" * Grand Total * :", f1));
        cell.setColspan(2);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("", f1));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(pageTotal + "", f1));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("In Words (Rupees " + StringUtils.upperCase(Numtowordconvertion.convertNumber(pageTotal) + " ) Only"), f1));
        cell.setColspan(4);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(StringUtils.repeat("-", 155), f1));
        cell.setColspan(4);
        cell.setFixedHeight(10);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("For the month of " + CommonFunctions.getMonthAsString(crb.getAqmonth()) + " payable on " + CommonFunctions.getMonthAsString(payBillMonth) + " " + payBillYear, f1));
        cell.setColspan(2);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("", f1));
        cell.setColspan(2);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Designation of the Drawing Officer: ", f1));
        cell.setColspan(4);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", f1));
        cell.setColspan(4);
        cell.setFixedHeight(10);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(crb.getDdoname(), f1));
        cell.setColspan(4);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);
    }

    private void printHeader(CommonReportParamBean crb, PdfPTable table, PdfPCell cell, int pageno, String billdesc, Font f1) throws Exception {

        cell = new PdfPCell(new Paragraph("Page : " + pageno, f1));
        cell.setColspan(4);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("", f1));
        cell.setColspan(4);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("SCHEDULE OF RECOVERY OF TAX ON PROFESSION", new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD)));
        cell.setColspan(4);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        String deptname = crb.getOfficeen();
        cell = new PdfPCell(new Paragraph("OF " + deptname, new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD)));
        cell.setColspan(4);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("********************", f1));
        cell.setColspan(4);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("0028-OTHER TAXES ON INCOME AND EXPENDITURE-107-TAXES ON PROFESSION", f1));
        cell.setColspan(4);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("TRADES, CALLING AND EMPLOYMENT-01045-TAXES ON PROFESSION", f1));
        cell.setColspan(4);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(cell);

        Font f2 = new Font();
        f2.setSize(9);
        f2.setStyle(Font.BOLD);

        cell = new PdfPCell(new Paragraph("", f1));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        cell = new PdfPCell(new Paragraph("Pay Bill No :", f1));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);
        cell = new PdfPCell(new Paragraph(billdesc, f2));
        cell.setColspan(2);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("", f1));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        cell = new PdfPCell(new Paragraph("For the month of: ", f1));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);
        cell = new PdfPCell(new Paragraph("" + CommonFunctions.getMonthAsString(crb.getAqmonth()) + "-" + crb.getAqyear(), f2));
        cell.setColspan(2);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(StringUtils.repeat("-", 155), f1));
        cell.setColspan(4);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("No", f1));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        cell = new PdfPCell(new Paragraph("Name and Designation", f1));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        cell = new PdfPCell(new Paragraph("Salary", f1));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);
        cell = new PdfPCell(new Paragraph("Deducted", f1));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("", f1));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        cell = new PdfPCell(new Paragraph("of employee", f1));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        cell = new PdfPCell(new Paragraph("(in Rs.)", f1));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);
        cell = new PdfPCell(new Paragraph("(in Rs.)", f1));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(StringUtils.repeat("-", 155), f1));
        cell.setColspan(4);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("1", f1));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(cell);
        cell = new PdfPCell(new Paragraph("2", f1));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        cell = new PdfPCell(new Paragraph("3", f1));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        cell = new PdfPCell(new Paragraph("4", f1));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(StringUtils.repeat("-", 155), f1));
        cell.setColspan(4);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

    }

    private ArrayList getPTScheduleDtlsForEmployee(String billNo) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;
        PreparedStatement pst1 = null;
        ResultSet rs1 = null;

        CmgiPtBean cmgibean = null;

        int basicSal = 0;
        String empCode = "";

        String gross = "";
        ArrayList emplist = new ArrayList();
        int carryForwardTax = 0;
        int totalGross = 0;
        try {

            con = this.dataSource.getConnection();

            String innersql = "SELECT AD_AMT,AD_DESC FROM(select AQSL_NO from AQ_MAST where EMP_CODE = ? and BILL_NO=?)AQ_MAST"
                    + " INNER JOIN (SELECT AQSL_NO,AD_DESC,AD_AMT FROM AQ_DTLS WHERE AD_TYPE='A')AQ_DTLS on AQ_DTLS.AQSL_NO=AQ_MAST.AQSL_NO";
            pst1 = con.prepareStatement(innersql);

            String outersql = "SELECT DTL.EMP_CODE,DTL.EMP_NAME,DTL.CUR_DESG,DTL.CUR_BASIC, DTL.AD_AMT FROM((SELECT * FROM BILL_MAST WHERE BILL_MAST.BILL_NO=?)BILL_MAST LEFT OUTER JOIN (SELECT AQ_MAST.EMP_CODE,AQ_MAST.BILL_NO,AQ_MAST.CUR_DESG,AQ_MAST.EMP_NAME,AQ_MAST.CUR_BASIC,AQ_MAST.POST_SL_NO, AQ_DTLS.AD_AMT FROM((SELECT * FROM AQ_MAST WHERE AQ_MAST.BILL_NO=?)AQ_MAST INNER JOIN (SELECT * FROM AQ_DTLS WHERE AQ_DTLS.SCHEDULE='PT' AND AQ_DTLS.AD_TYPE='D' AND AD_AMT > 0)AQ_DTLS ON AQ_DTLS.AQSL_NO=AQ_MAST.AQSL_NO))DTL ON BILL_MAST.BILL_NO=DTL.BILL_NO)order by POST_SL_NO";
            pst = con.prepareStatement(outersql);
            pst.setInt(1, Integer.parseInt(billNo));
            pst.setInt(2, Integer.parseInt(billNo));
            rs = pst.executeQuery();
            while (rs.next()) {
                cmgibean = new CmgiPtBean();
                cmgibean.setEmpName(rs.getString("EMP_NAME"));
                cmgibean.setEmpDesig(rs.getString("CUR_DESG"));
                if (rs.getString("AD_AMT") != null && !rs.getString("AD_AMT").equals("")) {
                    cmgibean.setEmpTaxOnProffesion(rs.getString("AD_AMT"));
                } else {
                    cmgibean.setEmpTaxOnProffesion("0");
                }
                carryForwardTax = carryForwardTax + rs.getInt("AD_AMT");
                cmgibean.setTotalTax(carryForwardTax + "");
                empCode = rs.getString("EMP_CODE");
                basicSal = rs.getInt("CUR_BASIC");

                pst1.setString(1, empCode);
                pst1.setInt(2, Integer.parseInt(billNo));
                rs1 = pst1.executeQuery();
                int totalAllowance = 0;
                while (rs1.next()) {
                    totalAllowance = totalAllowance + rs1.getInt("AD_AMT");
                }
                gross = basicSal + totalAllowance + "";
                totalGross = totalGross + Integer.parseInt(gross);

                cmgibean.setTotalGross(totalGross + "");
                cmgibean.setEmpGrossSal(gross);

                emplist.add(cmgibean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return emplist;
    }

    
}
