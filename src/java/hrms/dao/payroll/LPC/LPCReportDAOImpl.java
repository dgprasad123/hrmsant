/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.payroll.LPC;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import hrms.common.DataBaseFunctions;
import hrms.model.common.FileAttribute;
import hrms.model.login.Users;
import hrms.model.payroll.LPC.LPCReport;
import hrms.model.payroll.payslip.PaySlipListBean;
import java.io.File;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.sql.DataSource;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author lenovo
 */
public class LPCReportDAOImpl implements LPCReportDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void downloadLPCReport(Document document, Users lub, int month, int year, String SelectedMonthYear, String txtRlvDt, String sltRlvTime, PaySlipListBean pbean, String fname, String rlvId, PaySlipListBean pblpc) {
        Connection con = null;
        PreparedStatement pstmt = null;
        PreparedStatement pstmt1 = null;
        PreparedStatement ps1 = null;
        ResultSet rs1, rs2, rs3 = null;
        ArrayList outList = new ArrayList();
        Statement stmt = null;

        try {
            con = dataSource.getConnection();
            // stmt = con.createStatement();
            String basicSalary = "NIL";
            String deall = "NIL";
            String daPerCeil = "";
            String daPer = "";
            double gpAmount = 0;

            if (pbean.getBasic() != null && !pbean.getBasic().equals("") && !pbean.getBasic().equals("0")) {
                basicSalary = pbean.getBasic();
                deall = pbean.getTotallowance();
                double dedpay = 0;
                if (deall != null && !deall.equals("") && !deall.equals("0")) {
                    dedpay = Double.parseDouble(deall);
                }
                double bpay = Double.parseDouble(basicSalary);

                /**
                 * ********************************** GP
                 * ******************************
                 */
                String gpListQuery = "SELECT ad_amt FROM aq_dtls WHERE  aq_year=? AND aq_month=?  AND ad_code=? AND emp_code=?  AND ad_amt > 0";
                // System.out.println("");
                pstmt = con.prepareStatement(gpListQuery);
                pstmt.setInt(1, year);
                pstmt.setInt(2, month);
                pstmt.setString(3, "GP");
                pstmt.setString(4, lub.getEmpId());
                rs1 = pstmt.executeQuery();
                while (rs1.next()) {
                    gpAmount = rs1.getInt("ad_amt");

                }

                String payType = lub.getPayCommission();
                // gpAmount=4200;
                Users ues = getLpcEmpDetail(lub.getEmpId());
                String empType = ues.getIsRegular();
                if (empType == null || empType.equalsIgnoreCase("N") || empType.equalsIgnoreCase("C")) {
                    daPer = "0";
                    daPerCeil = "0";
                } else {
                    if (bpay > 0) {
                        String daLpc[] = getDAforLPC(payType, month, year, bpay, gpAmount);
                        daPer = daLpc[0];
                        daPerCeil = daLpc[1];

                    }
                }

                /*if (bpay > 0) {
                 String daLpc[]=getDAforLPC(payType,month, year,bpay,gpAmount);                    
                 daPer = daLpc[0];
                 daPerCeil = daLpc[1];

                 }*/
                //System.out.println("----"+pblpc.getComments());
                ps1 = con.prepareStatement("UPDATE emp_relieve SET lpc_file_path=? WHERE relieve_id=? AND EMP_ID=?");
                ps1.setString(1, fname);
                ps1.setInt(2, Integer.parseInt(rlvId));
                // ps1.setBigDecimal(2, new BigDecimal(rlvId));
                ps1.setString(3, lub.getEmpId());
                ps1.executeUpdate();

            }
            Users ue = getLpcEmpDetail(lub.getEmpId());
            // System.out.println("basicSalary" + basicSalary);
            document.open();
            Font f1 = new Font();
            f1.setSize(10);
            f1.setFamily("Times New Roman");
            Font hdrTextFont = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.BLACK);
            Font hdrTextFont1 = new Font(Font.FontFamily.HELVETICA, 7, Font.BOLD, BaseColor.BLACK);
            Font dataHdrFont = new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD, BaseColor.BLACK);
            Font dataHdrFont1 = new Font(Font.FontFamily.HELVETICA, 7, Font.UNDERLINE);
            Font dataValFont = new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL);

            PdfPTable table1 = new PdfPTable(2);
            table1.setWidths(new int[]{5, 5});
            table1.setWidthPercentage(100);
            PdfPCell datacell;

            datacell = new PdfPCell(new Phrase("Schedule 1, III-Form No.195", dataValFont));
            datacell.setBorder(Rectangle.NO_BORDER);
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("OTC FORM", dataValFont));
            datacell.setBorder(Rectangle.NO_BORDER);
            datacell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table1.addCell(datacell);

            document.add(table1);

            PdfPTable table = new PdfPTable(1);
            table.setWidths(new int[]{5});
            table.setWidthPercentage(100);
            PdfPCell cell;

            cell = new PdfPCell(new Phrase("LAST PAY CERTIFICATE " + "(" + SelectedMonthYear + ")", hdrTextFont));
            cell.setFixedHeight(20);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);

            table.addCell(cell);

            cell = new PdfPCell(new Phrase("OBVERSE", hdrTextFont1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setFixedHeight(30);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            Phrase phrs = new Phrase();
            Chunk c1 = new Chunk("LAST PAY CERTIFICATE OF", dataValFont);
            Chunk c2 = new Chunk(ue.getFullName() + ", " + ue.getPostname() + ", of " + ue.getOffname(), hdrTextFont1);
            Chunk c3 = new Chunk("  transfer on promotion w.e.f ", dataValFont);
            Chunk c4 = new Chunk(txtRlvDt + "(" + sltRlvTime + ")", hdrTextFont1);
            // Chunk c4 = new Chunk(ue.getEmpDos(), hdrTextFont1);
            phrs.add(c1);
            phrs.add(c2);
            phrs.add(c3);
            phrs.add(c4);

            cell = new PdfPCell(phrs);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setFixedHeight(15);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            Phrase phrs1 = new Phrase();
            Chunk c5 = new Chunk("2.He/She has been paid upto ", dataValFont);
            Chunk c6 = new Chunk(txtRlvDt + "(" + sltRlvTime + ")", hdrTextFont1);
            Chunk c7 = new Chunk("in the follwoing rates ", dataValFont);

            phrs1.add(c5);
            phrs1.add(c6);
            phrs1.add(c7);

            cell = new PdfPCell(phrs1);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setFixedHeight(15);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            Phrase phrs2 = new Phrase();
            Chunk c8 = new Chunk("3.His/Her GPF/ PPAN No ", dataValFont);
            Chunk c9 = new Chunk(ue.getGpfno(), hdrTextFont1);

            phrs2.add(c8);
            phrs2.add(c9);

            cell = new PdfPCell(phrs2);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setFixedHeight(15);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            Phrase phrs3 = new Phrase();
            Chunk c10 = new Chunk("4.His/Her HRMS ID No is ", dataValFont);
            Chunk c11 = new Chunk(lub.getEmpId(), hdrTextFont1);

            phrs3.add(c10);
            phrs3.add(c11);

            cell = new PdfPCell(phrs3);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setFixedHeight(15);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Particulars", dataHdrFont1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            document.add(table);

            PdfPTable table2 = new PdfPTable(4);
            table2.setWidths(new int[]{5, 5, 5, 5});
            table2.setWidthPercentage(100);
            PdfPCell cell2;

            cell2 = new PdfPCell(new Phrase("Substantive Pay", dataValFont));
            cell2.setBorder(Rectangle.NO_BORDER);
            cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
            table2.addCell(cell2);

            cell2 = new PdfPCell(new Phrase("RS " + basicSalary + "/ Per Month", dataValFont));
            cell2.setBorder(Rectangle.NO_BORDER);
            cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
            table2.addCell(cell2);

            cell2 = new PdfPCell(new Phrase("Pay", dataValFont));
            cell2.setBorder(Rectangle.NO_BORDER);
            cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
            table2.addCell(cell2);

            cell2 = new PdfPCell(new Phrase("RS " + basicSalary + "/ Per Month", dataValFont));
            cell2.setBorder(Rectangle.NO_BORDER);
            cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
            table2.addCell(cell2);

            cell2 = new PdfPCell(new Phrase("Grade Pay", dataValFont));
            cell2.setBorder(Rectangle.NO_BORDER);
            cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
            table2.addCell(cell2);

            cell2 = new PdfPCell(new Phrase("RS. ", dataValFont));
            cell2.setBorder(Rectangle.NO_BORDER);
            cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
            table2.addCell(cell2);

            cell2 = new PdfPCell(new Phrase("GP", dataValFont));
            cell2.setBorder(Rectangle.NO_BORDER);
            cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
            table2.addCell(cell2);

            cell2 = new PdfPCell(new Phrase("RS. " + gpAmount, dataValFont));
            cell2.setBorder(Rectangle.NO_BORDER);
            cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
            table2.addCell(cell2);

            cell2 = new PdfPCell(new Phrase("Spl Pay", dataValFont));
            cell2.setBorder(Rectangle.NO_BORDER);
            cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
            table2.addCell(cell2);

            cell2 = new PdfPCell(new Phrase("RS 0.00/-", dataValFont));
            cell2.setBorder(Rectangle.NO_BORDER);
            cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
            table2.addCell(cell2);

            cell2 = new PdfPCell(new Phrase("Spl Pay", dataValFont));
            cell2.setBorder(Rectangle.NO_BORDER);
            cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
            table2.addCell(cell2);

            cell2 = new PdfPCell(new Phrase("RS 0.00/-", dataValFont));
            cell2.setBorder(Rectangle.NO_BORDER);
            cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
            table2.addCell(cell2);

            cell2 = new PdfPCell(new Phrase("Allowances,", dataValFont));
            cell2.setBorder(Rectangle.NO_BORDER);
            cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
            table2.addCell(cell2);

            cell2 = new PdfPCell(new Phrase(" ", dataValFont));
            cell2.setBorder(Rectangle.NO_BORDER);
            cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
            table2.addCell(cell2);

            cell2 = new PdfPCell(new Phrase("", dataValFont));
            cell2.setBorder(Rectangle.NO_BORDER);
            cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
            table2.addCell(cell2);

            cell2 = new PdfPCell(new Phrase("", dataValFont));
            cell2.setBorder(Rectangle.NO_BORDER);
            cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
            table2.addCell(cell2);

            cell2 = new PdfPCell(new Phrase("DA @ " + daPer, dataValFont));
            cell2.setBorder(Rectangle.NO_BORDER);
            cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
            table2.addCell(cell2);

            cell2 = new PdfPCell(new Phrase("RS " + daPerCeil + "/ Per Month", dataValFont));
            cell2.setBorder(Rectangle.NO_BORDER);
            cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
            table2.addCell(cell2);

            cell2 = new PdfPCell(new Phrase("DA", dataValFont));
            cell2.setBorder(Rectangle.NO_BORDER);
            cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
            table2.addCell(cell2);

            cell2 = new PdfPCell(new Phrase("RS " + daPerCeil + "/ Per Month", dataValFont));
            cell2.setBorder(Rectangle.NO_BORDER);
            cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
            table2.addCell(cell2);

            document.add(table2);

            PdfPTable table3 = new PdfPTable(1);
            table3.setWidths(new int[]{5});
            table3.setWidthPercentage(100);
            PdfPCell cell3;

            cell3 = new PdfPCell(new Phrase("", hdrTextFont1));
            cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell3.setFixedHeight(15);
            cell3.setBorder(Rectangle.NO_BORDER);
            table3.addCell(cell3);

            cell3 = new PdfPCell(new Phrase("DEDUCTIONS", hdrTextFont1));
            cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell3.setFixedHeight(15);
            cell3.setBorder(Rectangle.NO_BORDER);
            table3.addCell(cell3);

            cell3 = new PdfPCell(new Phrase("5.He/She made over the charge of the office of the " + ue.getOffname() + " Department on the " + sltRlvTime + " of " + txtRlvDt, dataValFont));
            cell3.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell3.setFixedHeight(15);
            cell3.setBorder(Rectangle.NO_BORDER);
            table3.addCell(cell3);

            cell3 = new PdfPCell(new Phrase("6.Recoveries are to be made from the pay of the Goverment servant as detailed as reverse", dataValFont));
            cell3.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell3.setFixedHeight(15);
            cell3.setBorder(Rectangle.NO_BORDER);
            table3.addCell(cell3);

            cell3 = new PdfPCell(new Phrase("7.He/She has been paid leave salary as details below. Deducation has been made as noted on reverse", dataValFont));
            cell3.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell3.setFixedHeight(15);
            cell3.setBorder(Rectangle.NO_BORDER);
            table3.addCell(cell3);
            document.add(table3);

            String deducationListQuery = "SELECT ad_amt,ad_desc,acc_no,ad_ref_id,ad_code FROM aq_dtls WHERE  aq_year=? AND aq_month=?  AND ad_type=? AND emp_code=?  AND ad_amt > 0";
            // System.out.println("");
            pstmt1 = con.prepareStatement(deducationListQuery);
            pstmt1.setInt(1, year);
            pstmt1.setInt(2, month);
            pstmt1.setString(3, "D");
            pstmt1.setString(4, lub.getEmpId());
            rs3 = pstmt1.executeQuery();

            PdfPTable tableD = new PdfPTable(2);
            tableD.setWidths(new int[]{5, 5});
            tableD.setWidthPercentage(100);
            PdfPCell cellD;

            cellD = new PdfPCell(new Phrase("Deducation Details", hdrTextFont1));
            cellD.setBorder(Rectangle.NO_BORDER);
            cellD.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableD.addCell(cellD);

            cellD = new PdfPCell(new Phrase("Amount", hdrTextFont1));
            cellD.setBorder(Rectangle.NO_BORDER);
            cellD.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableD.addCell(cellD);

            String DeducationDetails = "NIL";
            int adDAmount = 0;
            int D = 0;

            while (rs3.next()) {
                D++;
                DeducationDetails = rs3.getString("ad_desc");
                adDAmount = rs3.getInt("ad_amt");

                cellD = new PdfPCell(new Phrase(DeducationDetails, dataValFont));
                cellD.setBorder(Rectangle.NO_BORDER);
                cellD.setHorizontalAlignment(Element.ALIGN_LEFT);
                tableD.addCell(cellD);

                cellD = new PdfPCell(new Phrase("Rs." + adDAmount + "/-", dataValFont));
                cellD.setBorder(Rectangle.NO_BORDER);
                cellD.setHorizontalAlignment(Element.ALIGN_LEFT);
                tableD.addCell(cellD);
            }

            if (D == 0) {
                cellD = new PdfPCell(new Phrase("NIL", dataValFont));
                cellD.setBorder(Rectangle.NO_BORDER);
                cellD.setHorizontalAlignment(Element.ALIGN_LEFT);
                tableD.addCell(cellD);

                cellD = new PdfPCell(new Phrase("NIL ", dataValFont));
                cellD.setBorder(Rectangle.NO_BORDER);
                cellD.setHorizontalAlignment(Element.ALIGN_LEFT);
                tableD.addCell(cellD);
            }
            document.add(tableD);

            PdfPTable tabledraw = new PdfPTable(1);
            tabledraw.setWidths(new int[]{5});
            tabledraw.setWidthPercentage(100);
            PdfPCell celldraw;

            celldraw = new PdfPCell(new Phrase(" ", dataValFont));
            celldraw.setHorizontalAlignment(Element.ALIGN_LEFT);
            celldraw.setFixedHeight(15);
            celldraw.setBorder(Rectangle.NO_BORDER);
            tabledraw.addCell(celldraw);

            celldraw = new PdfPCell(new Phrase("8.He/She is also entitled to draw the following", dataValFont));
            celldraw.setHorizontalAlignment(Element.ALIGN_LEFT);
            celldraw.setFixedHeight(15);
            celldraw.setBorder(Rectangle.NO_BORDER);
            tabledraw.addCell(celldraw);

            celldraw = new PdfPCell(new Phrase(" ", dataValFont));
            celldraw.setHorizontalAlignment(Element.ALIGN_LEFT);
            celldraw.setFixedHeight(15);
            celldraw.setBorder(Rectangle.NO_BORDER);
            tabledraw.addCell(celldraw);
            document.add(tabledraw);

            PdfPTable table4 = new PdfPTable(3);
            table4.setWidths(new int[]{5, 5, 5});
            table4.setWidthPercentage(100);
            PdfPCell cell4;

            cell4 = new PdfPCell(new Phrase("Period", hdrTextFont1));
            cell4.setBorder(Rectangle.NO_BORDER);
            cell4.setHorizontalAlignment(Element.ALIGN_LEFT);
            table4.addCell(cell4);

            cell4 = new PdfPCell(new Phrase("Rate", hdrTextFont1));
            cell4.setBorder(Rectangle.NO_BORDER);
            cell4.setHorizontalAlignment(Element.ALIGN_LEFT);
            table4.addCell(cell4);

            cell4 = new PdfPCell(new Phrase("Amount", hdrTextFont1));
            cell4.setBorder(Rectangle.NO_BORDER);
            cell4.setHorizontalAlignment(Element.ALIGN_LEFT);
            table4.addCell(cell4);

            cell4 = new PdfPCell(new Phrase("From", dataValFont));
            cell4.setBorder(Rectangle.NO_BORDER);
            cell4.setHorizontalAlignment(Element.ALIGN_LEFT);
            table4.addCell(cell4);

            cell4 = new PdfPCell(new Phrase("To", dataValFont));
            cell4.setBorder(Rectangle.NO_BORDER);
            cell4.setHorizontalAlignment(Element.ALIGN_LEFT);
            table4.addCell(cell4);

            cell4 = new PdfPCell(new Phrase("At Rs.   a month ", dataValFont));
            cell4.setBorder(Rectangle.NO_BORDER);
            cell4.setHorizontalAlignment(Element.ALIGN_LEFT);
            table4.addCell(cell4);

            cell4 = new PdfPCell(new Phrase("From", dataValFont));
            cell4.setBorder(Rectangle.NO_BORDER);
            cell4.setHorizontalAlignment(Element.ALIGN_LEFT);
            table4.addCell(cell4);

            cell4 = new PdfPCell(new Phrase("To", dataValFont));
            cell4.setBorder(Rectangle.NO_BORDER);
            cell4.setHorizontalAlignment(Element.ALIGN_LEFT);
            table4.addCell(cell4);

            cell4 = new PdfPCell(new Phrase("At Rs.   a month ", dataValFont));
            cell4.setBorder(Rectangle.NO_BORDER);
            cell4.setHorizontalAlignment(Element.ALIGN_LEFT);
            table4.addCell(cell4);

            cell4 = new PdfPCell(new Phrase("From", dataValFont));
            cell4.setBorder(Rectangle.NO_BORDER);
            cell4.setHorizontalAlignment(Element.ALIGN_LEFT);
            table4.addCell(cell4);

            cell4 = new PdfPCell(new Phrase("To", dataValFont));
            cell4.setBorder(Rectangle.NO_BORDER);
            cell4.setHorizontalAlignment(Element.ALIGN_LEFT);
            table4.addCell(cell4);

            cell4 = new PdfPCell(new Phrase("At Rs.   a month ", dataValFont));
            cell4.setBorder(Rectangle.NO_BORDER);
            cell4.setHorizontalAlignment(Element.ALIGN_LEFT);
            table4.addCell(cell4);
            document.add(table4);

            PdfPTable table5 = new PdfPTable(1);
            table5.setWidths(new int[]{5});
            table5.setWidthPercentage(100);
            PdfPCell cell5;

            cell5 = new PdfPCell(new Phrase("9.He/She is also entitled to joining time from 15 days ", dataValFont));
            cell5.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell5.setFixedHeight(15);
            cell5.setBorder(Rectangle.NO_BORDER);
            table5.addCell(cell5);

            cell5 = new PdfPCell(new Phrase("10.His/Her finances the insurance policies details below from Provident Fund. ", dataValFont));
            cell5.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell5.setFixedHeight(15);
            cell5.setBorder(Rectangle.NO_BORDER);
            table5.addCell(cell5);
            document.add(table5);

            String licListQuery = "SELECT ad_amt,ad_desc,acc_no,ad_ref_id,ad_code FROM aq_dtls WHERE  aq_year=? AND aq_month=?  AND ad_code=? AND emp_code=?  AND ad_amt > 0";
            // System.out.println("");
            pstmt = con.prepareStatement(licListQuery);
            pstmt.setInt(1, year);
            pstmt.setInt(2, month);
            pstmt.setString(3, "LIC");
            pstmt.setString(4, lub.getEmpId());
            rs1 = pstmt.executeQuery();

            String status = "";
            String PolicyNo = "NIL";
            String adCode = "NIL";
            int adAmount = 0;
            int i = 0;

            PdfPTable table9 = new PdfPTable(3);
            table9.setWidths(new int[]{5, 5, 5});
            table9.setWidthPercentage(100);
            PdfPCell cell9;

            cell9 = new PdfPCell(new Phrase("Insurance Company", hdrTextFont1));
            cell9.setBorder(Rectangle.NO_BORDER);
            cell9.setHorizontalAlignment(Element.ALIGN_LEFT);
            table9.addCell(cell9);

            cell9 = new PdfPCell(new Phrase("Policy No", hdrTextFont1));
            cell9.setBorder(Rectangle.NO_BORDER);
            cell9.setHorizontalAlignment(Element.ALIGN_LEFT);
            table9.addCell(cell9);

            cell9 = new PdfPCell(new Phrase("Amount", hdrTextFont1));
            cell9.setBorder(Rectangle.NO_BORDER);
            cell9.setHorizontalAlignment(Element.ALIGN_LEFT);
            table9.addCell(cell9);

            while (rs1.next()) {
                i++;
                PolicyNo = rs1.getString("acc_no");
                adAmount = rs1.getInt("ad_amt");
                adCode = rs1.getString("ad_code");

                cell9 = new PdfPCell(new Phrase(adCode, dataValFont));
                cell9.setBorder(Rectangle.NO_BORDER);
                cell9.setHorizontalAlignment(Element.ALIGN_LEFT);
                table9.addCell(cell9);

                cell9 = new PdfPCell(new Phrase(PolicyNo, dataValFont));
                cell9.setBorder(Rectangle.NO_BORDER);
                cell9.setHorizontalAlignment(Element.ALIGN_LEFT);
                table9.addCell(cell9);

                cell9 = new PdfPCell(new Phrase(adAmount + "/-", dataValFont));
                cell9.setBorder(Rectangle.NO_BORDER);
                cell9.setHorizontalAlignment(Element.ALIGN_LEFT);
                table9.addCell(cell9);
            }

            if (i == 0) {
                cell9 = new PdfPCell(new Phrase("NIL", dataValFont));
                cell9.setBorder(Rectangle.NO_BORDER);
                cell9.setHorizontalAlignment(Element.ALIGN_LEFT);
                table9.addCell(cell9);

                cell9 = new PdfPCell(new Phrase("NIL", dataValFont));
                cell9.setBorder(Rectangle.NO_BORDER);
                cell9.setHorizontalAlignment(Element.ALIGN_LEFT);
                table9.addCell(cell9);

                cell9 = new PdfPCell(new Phrase("NIL ", dataValFont));
                cell9.setBorder(Rectangle.NO_BORDER);
                cell9.setHorizontalAlignment(Element.ALIGN_LEFT);
                table9.addCell(cell9);
            }
            document.add(table9);

            PdfPTable table8 = new PdfPTable(1);
            table8.setWidths(new int[]{5});
            table8.setWidthPercentage(100);
            PdfPCell cell8;

            cell8 = new PdfPCell(new Phrase("11. The details of the Income Tx recovered from him up to the date from the beginning of the current Financial Year are noted on the reverse", dataValFont));
            cell8.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell8.setFixedHeight(15);
            cell8.setBorder(Rectangle.NO_BORDER);
            table8.addCell(cell8);

            cell8 = new PdfPCell(new Phrase(" ", dataValFont));
            cell8.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell8.setFixedHeight(15);
            cell8.setBorder(Rectangle.NO_BORDER);
            table8.addCell(cell8);
            document.add(table8);
            if (pblpc.getComments() != null && !pblpc.getComments().equals("")) {

                /* cell8 = new PdfPCell(new Phrase("NB. " + pblpc.getComments(), dataValFont));
                 cell8.setHorizontalAlignment(Element.ALIGN_LEFT);
                 cell8.setFixedHeight(15);
                 cell8.setBorder(Rectangle.NO_BORDER);
                 table8.addCell(cell8);

                 cell8 = new PdfPCell(new Phrase(" ", dataValFont));
                 cell8.setHorizontalAlignment(Element.ALIGN_LEFT);
                 cell8.setFixedHeight(15);
                 cell8.setBorder(Rectangle.NO_BORDER);
                 table8.addCell(cell8);*/
                String msg5 = "NB. " + pblpc.getComments();

                Paragraph point5 = new Paragraph(msg5, dataValFont);
                document.add(point5);

            }

            PdfPTable table6 = new PdfPTable(2);
            table6.setWidths(new int[]{5, 5});
            table6.setWidthPercentage(100);
            PdfPCell cell6;

            cell6 = new PdfPCell(new Phrase(" ", dataValFont));
            cell6.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell6.setFixedHeight(15);
            cell6.setBorder(Rectangle.NO_BORDER);
            table6.addCell(cell6);

            cell6 = new PdfPCell(new Phrase(" ", dataValFont));
            cell6.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell6.setFixedHeight(15);
            cell6.setBorder(Rectangle.NO_BORDER);
            table6.addCell(cell6);

            cell6 = new PdfPCell(new Phrase("Date:", dataValFont));
            cell6.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell6.setFixedHeight(15);
            cell6.setBorder(Rectangle.NO_BORDER);
            table6.addCell(cell6);

            cell6 = new PdfPCell(new Phrase("Signature:", dataValFont));
            cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell6.setFixedHeight(15);
            cell6.setBorder(Rectangle.NO_BORDER);
            table6.addCell(cell6);

            document.add(table6);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt, ps1);
            DataBaseFunctions.closeSqlObjects(con);
            document.close();
        }
    }

    public String generateLPC(String empid) {
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        String status = "1";
        try {
            con = this.dataSource.getConnection();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return status;

    }

    @Override
    public FileAttribute downlaodLPCFile(String rlvId, String filepath, Users lub) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        File f = null;
        FileAttribute fa = new FileAttribute();
        try {
            con = dataSource.getConnection();

            String sql = "SELECT lpc_file_path FROM emp_relieve  WHERE relieve_id=? AND EMP_ID=?";
            pst = con.prepareStatement(sql);

            //  pst.setBigDecimal(1, new BigDecimal(rlvId));
            // ps1.setInt(2, Integer.parseInt(rlvId));
            pst.setInt(1, Integer.parseInt(rlvId));
            //pst.setBigDecimal(2, new BigDecimal(rlvId));

            pst.setString(2, lub.getEmpId());
            rs = pst.executeQuery();
            if (rs.next()) {
                String dirpath = filepath + "/" + rs.getString("lpc_file_path");
                f = new File(dirpath);
                if (f.exists()) {
                    // fa.setDiskFileName(rs.getString(""));
                    fa.setOriginalFileName(rs.getString("lpc_file_path"));
                    fa.setFileType("application/pdf");
                    fa.setUploadAttachment(f);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return fa;
    }

    @Override
    public Users getLpcEmpDetail(String empId) {
        Users empBean = new Users();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = null;
        try {
            con = this.dataSource.getConnection();
            sql = "SELECT C.POST, E.off_en,E.ddo_code,ARRAY_TO_STRING(ARRAY[INITIALS, A.F_NAME, A.M_NAME,A.L_NAME], ' ') fullname,A.is_regular"
                    + " , A.gpf_no,A.gp,acct_type ,A.cur_basic_salary,to_char(A.dob, 'DD-Mon-YYYY') as dob,to_char(A.dos, 'DD-Mon-YYYY') as dos FROM   EMP_MAST A  "
                    + " LEFT outer join G_SPC B on A.CUR_SPC=B.SPC  "
                    + "	 left outer join g_post C on B.gpc=C.post_code"
                    + "	LEFT OUTER JOIN g_department GD ON C.department_code = GD.department_code, G_OFFICE E   "
                    + "	WHERE A.CUR_OFF_CODE=E.OFF_CODE AND A.EMP_ID=? ";
            ps = con.prepareStatement(sql);
            ps.setString(1, empId);
            rs = ps.executeQuery();
            if (rs.next()) {
                empBean.setFullName(rs.getString("fullname"));
                empBean.setGpfno(rs.getString("gpf_no"));
                empBean.setGp(rs.getString("gp"));
                empBean.setBasicsalry(rs.getString("cur_basic_salary"));
                empBean.setIsRegular(rs.getString("is_regular"));
                empBean.setEmpDob(rs.getString("dob"));
                empBean.setEmpDos(rs.getString("dos"));
                empBean.setPostname(rs.getString("POST"));
                empBean.setOffname(rs.getString("off_en"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return empBean;
    }

    public String[] getDAforLPC(String payCom, int aqmonth, int aqyear, double basic, double gp) {

        double v_finaladAmt = 0;
        String v_daformula = "";
        String str[] = new String[2];
        try {
            if (payCom == null || payCom.equals("") || payCom.equalsIgnoreCase("6")) {
                if ((aqyear == 2023)) {
                    float da_f = (float) ((basic + gp) * 2.12);
                    v_finaladAmt = Math.round(da_f);
                    v_daformula = "(BASIC+GP)*(212/100)";
                } else if ((aqmonth >= 8 && aqyear == 2022)) {
                    float da_f = (float) ((basic + gp) * 2.03);
                    v_finaladAmt = Math.round(da_f);
                    //v_finaladAmt = new Long(Math.round(((payComponent.getBasic() + payComponent.getGp()) * 2.03))).intValue();
                    v_daformula = "(BASIC+GP)*(203/100)";
                } else if ((aqmonth < 8 && aqyear == 2022)) {
                    v_finaladAmt = new Long(Math.round(((basic + gp) * 1.96))).intValue();
                    v_daformula = "(BASIC+GP)*(196/100)";
                } else if ((aqmonth >= 9 && aqyear == 2021)) {
                    v_finaladAmt = new Long(Math.round(((basic + gp) * 1.89))).intValue();
                    v_daformula = "(BASIC+GP)*(189/100)";
                } else if ((aqmonth >= 6 && aqyear == 2019) || (aqmonth >= 0 && aqyear >= 2020)) {
                    v_finaladAmt = new Long(Math.round(((basic + gp) * 1.64))).intValue();
                    v_daformula = "(BASIC+GP)*(164/100)";
                } else if ((aqmonth >= 6 && aqyear == 2018) || (aqmonth <= 6 && aqyear == 2019)) {
                    v_finaladAmt = new Long(Math.round(((basic + gp) * 1.54))).intValue();
                    v_daformula = "(BASIC+GP)*(154/100)";
                } else if (aqmonth >= 3 && aqmonth <= 7 && aqyear < 2019) {
                    v_finaladAmt = new Long(Math.round(((basic + gp) * 1.42))).intValue();
                    v_daformula = "(BASIC+GP)*(142/100)";
                } else {
                    v_finaladAmt = new Long(Math.round(((basic + gp) * 1.39))).intValue();
                    v_daformula = "(BASIC+GP)*(139/100)";
                }
            } else {
                if ((aqyear == 2023)) {
                    v_finaladAmt = new Long(Math.round(basic * 0.38)).intValue();
                    v_daformula = "(BASIC)*(38/100)";
                } else if ((aqmonth >= 8 && aqyear == 2022)) {
                    v_finaladAmt = new Long(Math.round(basic * 0.34)).intValue();
                    v_daformula = "(BASIC)*(34/100)";
                } else if ((aqmonth < 8 && aqyear == 2022)) {
                    v_finaladAmt = new Long(Math.round(basic * 0.31)).intValue();
                    v_daformula = "(BASIC)*(31/100)";
                } else if ((aqmonth >= 9 && aqyear == 2021)) {
                    v_finaladAmt = new Long(Math.round(basic * 0.28)).intValue();
                    v_daformula = "(BASIC)*(28/100)";
                } else if ((aqmonth >= 6 && aqyear == 2019) || (aqmonth >= 0 && aqyear >= 2020)) {
                    v_finaladAmt = new Long(Math.round(basic * 0.17)).intValue();
                    v_daformula = "(BASIC)*(17/100)";
                } else if (aqmonth >= 2 && aqmonth <= 5 && aqyear == 2019) {
                    v_finaladAmt = new Long(Math.round(basic * 0.12)).intValue();
                    v_daformula = "(BASIC)*(12/100)";
                } else if ((aqmonth > 7 && aqmonth <= 11 && aqyear == 2018) || (aqmonth < 2 && aqyear == 2019)) {
                    v_finaladAmt = new Long(Math.round(basic * 0.09)).intValue();
                    v_daformula = "(BASIC)*(9/100)";
                } else if (aqmonth >= 3 && aqmonth <= 7 && aqyear == 2018) {
                    v_finaladAmt = new Long(Math.round(basic * 0.07)).intValue();
                    v_daformula = "(BASIC)*(7/100)";
                } else {
                    v_finaladAmt = new Long(Math.round(basic * 0.05)).intValue();
                    v_daformula = "(BASIC)*(5/100)";
                }
            }
            str[0] = v_daformula;
            str[1] = v_finaladAmt + "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }
}
