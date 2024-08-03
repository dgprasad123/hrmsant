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
import hrms.common.Numtowordconvertion;
import hrms.model.payroll.schedule.BillContributionRepotBean;
import hrms.model.payroll.schedule.ScheduleHelper;
import java.sql.Statement;
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
public class BillContributionPDFView extends AbstractView {

    public BillContributionPDFView() {
        setContentType("application/pdf");
    }

    @Override
    protected boolean generatesDownloadContent() {
        return true;
    }

    @Override
    protected void renderMergedOutputModel(Map<String, Object> model,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        int billmonth = 0;
        int slno = 0;
        int pageNo = 0;
        BillContributionRepotBean ecfb = (BillContributionRepotBean) model.get("NPSHeader");
        List empList = (List) model.get("NpsEmpList");
        String totalCpf = (String) model.get("TotCpf");
        String totalFig = (String) model.get("TotCpfFig");
        String totalGcpf = (String) model.get("TotGcpf");
        String totalGcpfFig = (String) model.get("TotGcpfFig");
        String annexure = (String) model.get("annexure");
        String grandTot = (String) model.get("GrandTot");
        response.setContentType("application/pdf");
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();
        Font f1 = new Font();
        f1.setSize(9);
        f1.setFamily("Times New Roman");

        Font f2 = new Font();
        f2.setSize(6);
        f2.setFamily("Times New Roman");

        PdfPTable table = new PdfPTable(2);
        table.setWidths(new int[]{5, 2});

        table.setWidthPercentage(100);

        PdfPCell cell = null;

        String annexureType = null;
        if (annexure.equalsIgnoreCase("annexure1")) {
            annexureType = "ANNEXURE- I";
        } else if (annexure.equalsIgnoreCase("annexure2")) {
            annexureType = "ANNEXURE- II";
        } else if (annexure.equalsIgnoreCase("annexure3")) {
            annexureType = "ANNEXURE- III";
        } else if (annexure.equalsIgnoreCase("annexure4")) {
            annexureType = "ANNEXURE- IV";
        }

        cell = new PdfPCell(new Phrase(StringUtils.defaultString(annexureType), new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD | Font.UNDERLINE)));
        cell.setColspan(2);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        if (!annexure.equalsIgnoreCase("annexure4")) {
            cell = new PdfPCell(new Phrase("FORMAT OF SCHEDULE OF GOVERNMENT SERVANT'S CONTRIBUTIONS", f1));
            cell.setColspan(2);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("TOWARDS TIER-I OF THE NEW PENSION SCHEME", f1));
            cell.setColspan(2);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
        } else {
            cell = new PdfPCell(new Phrase("FORMAT IN WHICH INFORMATION ON CONTRIBUTIONS IS REQUIRED TO BE SENT BY\nTREASURY OFFICER TO THE TRUSTEE BANK,", f1));
            cell.setColspan(2);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
        }

        if (annexure.equalsIgnoreCase("annexure1")) {
            cell = new PdfPCell(new Phrase("(TO BE ATTACHED WITH THE PAY BILL)", f1));
            cell.setColspan(2);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
        } else if (annexure.equalsIgnoreCase("annexure2")) {
            cell = new PdfPCell(new Phrase("(TO BE ATTACHED WITH THE PAY BILL FOR DRAWAL OF GOVT CONTRIBUTION)", f1));
            cell.setColspan(2);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
        } else if (annexure.equalsIgnoreCase("annexure3") || annexure.equalsIgnoreCase("annexure4")) {
            cell = new PdfPCell();
            cell.setColspan(2);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
        }
        cell = new PdfPCell();
        cell.setColspan(2);
        cell.setFixedHeight(10);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        if (!annexure.equalsIgnoreCase("annexure4")) {
            cell = new PdfPCell(new Phrase("BILL NO/DATE: " + StringUtils.defaultString(ecfb.getBillDesc()) + "/" + StringUtils.defaultString(ecfb.getBillDate()), f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("MONTH/YEAR: " + StringUtils.defaultString(ecfb.getBillMonth()) + "/" + StringUtils.defaultString(ecfb.getBillYear()), f1));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
        }

        if (annexure.equalsIgnoreCase("annexure1") || annexure.equalsIgnoreCase("annexure2")) {
            cell = new PdfPCell();
            cell.setColspan(2);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("NAME OF THE DDO/REGISTRATION NO:" + StringUtils.defaultString(ecfb.getDdoName()) + "/" + StringUtils.defaultString(ecfb.getDdoRegdNo()), f1));
            cell.setColspan(2);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
        } else if (annexure.equalsIgnoreCase("annexure4")) {
            cell = new PdfPCell();
            cell.setColspan(2);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("NAME OF THE DDO/REGISTRATION NO:" + StringUtils.defaultString(ecfb.getDdoName()) + "/" + StringUtils.defaultString(ecfb.getDdoRegdNo()), f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Bill No" + StringUtils.defaultString(ecfb.getBillDesc()) + "/ Dtd." + StringUtils.defaultString(ecfb.getBillDate()), f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
        } else if (annexure.equalsIgnoreCase("annexure3")) {
            cell = new PdfPCell();
            cell.setColspan(2);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("NAME OF THE DDO:" + StringUtils.defaultString(ecfb.getDdoName()), f1));
            cell.setColspan(2);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
        }

        if (annexure.equalsIgnoreCase("annexure3")) {
            cell = new PdfPCell();
            cell.setColspan(2);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("REGISTRATION NO:" + StringUtils.defaultString(ecfb.getDdoRegdNo()), f1));
            cell.setColspan(2);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
        }

        cell = new PdfPCell();
        cell.setColspan(2);
        cell.setFixedHeight(10);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        if (!annexure.equalsIgnoreCase("annexure4")) {
            cell = new PdfPCell(new Phrase("NAME OF OFFICE & ADDRESS-" + StringUtils.defaultString(ecfb.getOffName()), f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
        } else {
            cell = new PdfPCell(new Phrase("DEDUCTION FOR THE MONTH/YEAR OF-" + StringUtils.defaultString(ecfb.getBillMonth()) + "/" + StringUtils.defaultString(ecfb.getBillYear()), f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
        }
        cell = new PdfPCell(new Phrase("DTO REGISTRATION NO:" + StringUtils.defaultString(ecfb.getDtoRegdNo()), f1));
        cell.setBorder(Rectangle.NO_BORDER);
        //cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        document.add(table);

        if (annexure.equalsIgnoreCase("annexure1")) {
            table = new PdfPTable(8);
            table.setWidths(new float[]{0.5f, 1, 3, 3, 1.2f, 1.2f, 1.2f, 5});
        } else if (annexure.equalsIgnoreCase("annexure2")) {
            table = new PdfPTable(8);
            table.setWidths(new float[]{0.5f, 2, 3.5f, 3.5f, 1.2f, 1.2f, 1.2f, 2});
        } else if (annexure.equalsIgnoreCase("annexure3")) {
            table = new PdfPTable(9);
            table.setWidths(new float[]{0.5f, 2, 3.5f, 3.5f, 2, 1.2f, 2, 1, 1.2f});
        } else if (annexure.equalsIgnoreCase("annexure4")) {
            table = new PdfPTable(9);
            table.setWidths(new float[]{0.5f, 2, 2, 3.5f, 1.5f, 1.5f, 1.5f, 1.2f, 1});
        }

        table.setWidthPercentage(100);

        if (annexure.equalsIgnoreCase("annexure1") || annexure.equalsIgnoreCase("annexure2")) {
            cell = new PdfPCell();
            cell.setColspan(8);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
        } else {
            cell = new PdfPCell();
            cell.setColspan(9);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
        }

       // ecfb.setEmpcontlist(ecf.getEmpContList(billno, con));
        // annList = ecfb.getEmpcontlist();
        double arrearAmtTotal = 0;
        if (empList != null && empList.size() > 0) {
            Iterator itr = empList.iterator();
            BillContributionRepotBean ecbean = null;
            while (itr.hasNext()) {
                ecbean = (BillContributionRepotBean) itr.next();

                ecfb.setTotCpf(ecbean.getTotCpf());
                ecfb.setTotGcpf(ecbean.getTotGcpf());
                ecfb.setTotCpfWord(ecbean.getTotCpfWord());
                ecfb.setTotGcpfWord(ecbean.getTotGcpfWord());
                ecfb.setGrandTotal(ecbean.getGrandTotal());

                slno++;
                if (pageNo == 0) {
                    pageNo++;
                    if (annexure.equalsIgnoreCase("annexure1")) {
                        printHeaderAnnexure1(table, cell, f1);
                    } else if (annexure.equalsIgnoreCase("annexure2")) {
                        printHeaderAnnexure2(table, cell, f1);
                    } else if (annexure.equalsIgnoreCase("annexure3")) {
                        printHeaderAnnexure3(table, cell, f1);
                    } else if (annexure.equalsIgnoreCase("annexure4")) {
                        printHeaderAnnexure4(table, cell, f1);
                    }
                }
                
                if (annexure.equalsIgnoreCase("annexure1")) {
                    arrearAmtTotal = arrearAmtTotal + Double.parseDouble(ecbean.getArrearAmt());
                    
                    cell = new PdfPCell(new Phrase(slno + "", f1));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(StringUtils.defaultString(ecbean.getGpfNo()), f1));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(StringUtils.defaultString(ecbean.getEmpname()), f1));
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(StringUtils.defaultString(ecbean.getEmpdesg()), f1));
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(StringUtils.defaultString(ecbean.getEmpBasicSal()) + "\n" + StringUtils.defaultString(ecbean.getEmpGradepay()) + "\n" + StringUtils.defaultString(ecbean.getEmpPersonalpay()), f1));
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(StringUtils.defaultString(ecbean.getEmpDearnespay()), f1));
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(StringUtils.defaultString(Math.round(Double.parseDouble(ecbean.getTotal())) + ""), f1));
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(cell);
                    //start of inner table

                    PdfPTable innertable = new PdfPTable(4);
                    innertable.setWidths(new float[]{0.5f, 0.5f, 0.5f, 0.5f});
                    innertable.setWidthPercentage(100);

                    PdfPCell innercell = null;

                    innercell = new PdfPCell(new Phrase(StringUtils.defaultString(Math.round(Double.parseDouble(ecbean.getEmpCpf())) + ""), f1));
                    innercell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    innertable.addCell(innercell);
                    innercell = new PdfPCell(new Phrase(StringUtils.defaultString(ecbean.getArrInstalment()), f1));
                    innercell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    innertable.addCell(innercell);
                    innercell = new PdfPCell(new Phrase(StringUtils.defaultString(ecbean.getArrearAmt()), f1));
                    innercell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    innertable.addCell(innercell);
                    innercell = new PdfPCell(new Phrase(StringUtils.defaultString(Double.parseDouble(ecbean.getEmpCpf()) + Double.parseDouble(ecbean.getArrearAmt()) + ""), f1));
                    innercell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    innertable.addCell(innercell);
                    //end of inner table
                    cell = new PdfPCell(innertable);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);

                    if (slno % 15 == 0 && slno != empList.size()) {
                        document.add(table);
                        document.newPage();

                        table = new PdfPTable(8);
                        table.setWidths(new float[]{0.5f, 1, 3, 3, 1.2f, 1.2f, 1.2f, 5});
                        table.setWidthPercentage(100);

                        pageNo++;
                        printHeaderAnnexure1(table, cell, f1);
                    }
                    if (slno == empList.size()) {
                        double gcpfTotalA1 = Integer.parseInt(ecfb.getTotCpf()) + arrearAmtTotal;
                        String gcpfTotalFigA1 = Numtowordconvertion.convertNumber((int)gcpfTotalA1);                
                        
                        printFooterAnnexure1(table, cell, f1, f2, ecfb.getTotCpf(), ecfb.getTotCpfWord(), gcpfTotalA1+"", gcpfTotalFigA1);
                    }
                } else if (annexure.equalsIgnoreCase("annexure2")) {

                    cell = new PdfPCell(new Phrase(slno + "", f1));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(StringUtils.defaultString(ecbean.getGpfNo()), f1));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(StringUtils.defaultString(ecbean.getEmpname()), f1));
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(StringUtils.defaultString(ecbean.getEmpdesg()), f1));
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(StringUtils.defaultString(ecbean.getEmpBasicSal()) + "\n" + StringUtils.defaultString(ecbean.getEmpGradepay()) + "\n" + StringUtils.defaultString(ecbean.getEmpPersonalpay()), f1));
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(StringUtils.defaultString(ecbean.getEmpDearnespay()), f1));
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(StringUtils.defaultString(Math.round(Double.parseDouble(ecbean.getEmpGcpf())) + ""), f1));
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase("", f1));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);

                    if (slno % 15 == 0 && slno != empList.size()) {
                        document.add(table);
                        document.newPage();

                        table = new PdfPTable(8);
                        table.setWidths(new float[]{0.5f, 2, 3.5f, 3.5f, 1.2f, 1.2f, 1.2f, 2});
                        table.setWidthPercentage(100);

                        pageNo++;
                        printHeaderAnnexure2(table, cell, f1);
                    }
                    if (slno == empList.size()) {
                        printFooterAnnexure2(table, cell, f1, f2, ecfb.getTotGcpf(), ecfb.getTotGcpfWord());
                    }
                } else if (annexure.equalsIgnoreCase("annexure3")) {
                    
                    arrearAmtTotal = arrearAmtTotal + Double.parseDouble(ecbean.getArrearAmt());
                    
                    cell = new PdfPCell(new Phrase(slno + "", f1));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(StringUtils.defaultString(ecbean.getGpfNo()), f1));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(StringUtils.defaultString(ecbean.getEmpname()), f1));
                    //cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(StringUtils.defaultString(ecbean.getEmpdesg()), f1));
                    //cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(StringUtils.defaultString(ecfb.getBillDesc()) + "\n" + StringUtils.defaultString(ecfb.getBillDate()), f1));
                    //cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(StringUtils.defaultString(Math.round(Double.parseDouble(ecbean.getEmpCpf()) + Double.parseDouble(ecbean.getArrearAmt())) + ""), f1));
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(StringUtils.defaultString(ecfb.getBillDesc()) + "\n" + StringUtils.defaultString(ecfb.getBillDate()), f1));
                    //cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(StringUtils.defaultString(Math.round(Double.parseDouble(ecbean.getEmpGcpf())) + ""), f1));
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(StringUtils.defaultString(Math.round(Double.parseDouble(ecbean.getTotalAnnexure3())) + ""), f1));
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(cell);

                    if (slno % 15 == 0 && slno != empList.size()) {
                        document.add(table);
                        document.newPage();

                        table = new PdfPTable(9);
                        table.setWidths(new float[]{0.5f, 2, 3.5f, 3.5f, 2, 1.2f, 2, 1, 1});
                        table.setWidthPercentage(100);

                        pageNo++;
                        printHeaderAnnexure3(table, cell, f1);
                    }
                    if (slno == empList.size()) {
                        double cpfTotalA3 = Integer.parseInt(ecfb.getTotCpf()) + arrearAmtTotal;
                        String cpfTotalFigA3 = Numtowordconvertion.convertNumber((int)cpfTotalA3);
                        printFooterAnnexure3(table, cell, f1, f2, cpfTotalA3+"" , cpfTotalFigA3, ecfb.getTotGcpf(), ecfb.getTotGcpfWord(), ecfb.getGrandTotal());
                    }
                } else if (annexure.equalsIgnoreCase("annexure4")) {
                    
                    arrearAmtTotal = arrearAmtTotal + Double.parseDouble(ecbean.getArrearAmt());
                    
                    int cpfpGcpf = 0;
                    if (ecbean.getEmpCpf() != null && ecbean.getEmpGcpf() != null) {
                        cpfpGcpf = Integer.parseInt(Math.round(Double.parseDouble(ecbean.getEmpCpf())) + "") + Integer.parseInt(ecbean.getEmpGcpf());
                    }
                    cell = new PdfPCell(new Phrase(slno + "", f1));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                    cell = new PdfPCell();
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(StringUtils.defaultString(ecbean.getGpfNo()), f1));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(StringUtils.defaultString(ecbean.getEmpname()), f1));
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(StringUtils.defaultString(ecbean.getEmpBasicSal()) + "\n\n" + StringUtils.defaultString(ecbean.getEmpGradepay()) + "\n\n" + StringUtils.defaultString(ecbean.getEmpDearnespay()), f1));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(StringUtils.defaultString(Math.round(Double.parseDouble(ecbean.getEmpCpf()) + Double.parseDouble(ecbean.getArrearAmt())) + ""), f1));
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(StringUtils.defaultString(ecbean.getEmpGcpf()), f1));
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(StringUtils.defaultString(Math.round(Double.parseDouble(ecbean.getTotalAnnexure3())) + ""), f1));
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(cell);
                    cell = new PdfPCell();
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(cell);

//                    Statement stmt = con.createStatement();
//                    ResultSet rs = stmt.executeQuery("SELECT TR_NAME FROM (SELECT TR_CODE FROM BILL_MAST WHERE BILL_NO='" + billno + "') BILL_MAST"
//                            + " INNER JOIN G_TREASURY ON BILL_MAST.TR_CODE=G_TREASURY.TR_CODE");
//                    if (rs.next()) {
//                        ecfb.setTrName(rs.getString("TR_NAME"));
//
//                    }
                  //  DataBaseFunctions.closeSqlObjects(rs, stmt);

                    if (slno % 15 == 0 && slno != empList.size()) {
                        document.add(table);
                        document.newPage();

                        table = new PdfPTable(9);
                        table.setWidths(new float[]{0.5f, 2, 2, 3.5f, 1.5f, 1.5f, 1.5f, 1, 1});
                        table.setWidthPercentage(100);

                        pageNo++;
                        printHeaderAnnexure4(table, cell, f1);
                    }
                    if (slno == empList.size()) {
                        double cpfTotalA4 = Integer.parseInt(ecfb.getTotCpf()) + arrearAmtTotal;
                        String cpfTotalFigA4 = Numtowordconvertion.convertNumber((int)cpfTotalA4);
                        printFooterAnnexure4(table, cell, f1, cpfTotalA4+"", ecbean.getTotGcpf(), ecfb.getTreasuryName(), ecfb.getGrandTotal());
                    }
                }
            }

        }

        document.add(table);
        document.close();

    }

    private void printHeaderAnnexure1(PdfPTable table, PdfPCell cell, Font f1) throws Exception {

        cell = new PdfPCell(new Phrase("Sl No", f1));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("PRAN", f1));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Name of Govt Servant(in Block Letters)", f1));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Designation", f1));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Basic Pay + GP(Rs.) + P.Pay", f1));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("D.A(Rs.)", f1));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Total\n(Rs.)", f1));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        //start of inner table
        Font f2 = new Font();
        f2.setSize(6);

        PdfPTable innertable = new PdfPTable(4);
        innertable.setWidths(new float[]{0.5f, 0.5f, 0.5f, 0.5f});
        innertable.setWidthPercentage(100);

        PdfPCell innercell = null;

        innercell = new PdfPCell(new Phrase("Employees Contribution", f1));
        innercell.setColspan(4);
        innercell.setHorizontalAlignment(Element.ALIGN_CENTER);
        innertable.addCell(innercell);

        innercell = new PdfPCell(new Phrase("Current(Rs.)", f2));
        innercell.setRowspan(2);
        innercell.setBorder(Rectangle.NO_BORDER);
        innercell.setHorizontalAlignment(Element.ALIGN_CENTER);
        innercell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        innertable.addCell(innercell);
        innercell = new PdfPCell(new Phrase("Arrears", f2));
        innercell.setColspan(3);
        innercell.setHorizontalAlignment(Element.ALIGN_CENTER);
        innertable.addCell(innercell);

        innercell = new PdfPCell(new Phrase("Installment", f2));
        innertable.addCell(innercell);
        innercell = new PdfPCell(new Phrase("Amount(Rs.)", f2));
        innertable.addCell(innercell);
        innercell = new PdfPCell(new Phrase("Total(Rs.)", f2));
        innertable.addCell(innercell);
        //end of inner table
        cell = new PdfPCell(innertable);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("1", f1));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("2", f1));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("3", f1));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("4", f1));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("5", f1));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("6", f1));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("7", f1));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        //start of inner table
        innertable = new PdfPTable(4);
        innertable.setWidths(new float[]{0.5f, 0.5f, 0.5f, 0.5f});
        innertable.setWidthPercentage(100);

        //PdfPCell innercell = null;
        innercell = new PdfPCell(new Phrase("8", f1));
        innercell.setHorizontalAlignment(Element.ALIGN_CENTER);
        innertable.addCell(innercell);
        innercell = new PdfPCell(new Phrase("9", f1));
        innercell.setHorizontalAlignment(Element.ALIGN_CENTER);
        innertable.addCell(innercell);
        innercell = new PdfPCell(new Phrase("10", f1));
        innercell.setHorizontalAlignment(Element.ALIGN_CENTER);
        innertable.addCell(innercell);
        innercell = new PdfPCell(new Phrase("11", f1));
        innercell.setHorizontalAlignment(Element.ALIGN_CENTER);
        innertable.addCell(innercell);
        //end of inner table
        cell = new PdfPCell(innertable);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
    }

    private void printFooterAnnexure1(PdfPTable table, PdfPCell cell, Font f1, Font f2, String cpf, String cpfword, String gcpf, String gcpfword) throws Exception {

        cell = new PdfPCell(new Phrase("Grand Total:", f1));
        cell.setColspan(7);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);

        //start of inner table
        PdfPTable innertable = new PdfPTable(4);
        innertable.setWidths(new float[]{0.5f, 0.5f, 0.5f, 0.5f});
        innertable.setWidthPercentage(100);

        PdfPCell innercell = null;

        innercell = new PdfPCell(new Phrase(StringUtils.defaultString(cpf), f1));
        innercell.setBorder(Rectangle.NO_BORDER);
        innercell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        innertable.addCell(innercell);
        innercell = new PdfPCell();
        innercell.setBorder(Rectangle.NO_BORDER);
        innertable.addCell(innercell);
        innercell = new PdfPCell(new Phrase("Grand Total:", f1));
        innercell.setBorder(Rectangle.NO_BORDER);
        innertable.addCell(innercell);
        innercell = new PdfPCell(new Phrase(StringUtils.defaultString(gcpf), f1));
        innercell.setBorder(Rectangle.NO_BORDER);
        innercell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        innertable.addCell(innercell);
        //end of inner table
        cell = new PdfPCell(innertable);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(7);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);

        //start of inner table
        innertable = new PdfPTable(4);
        innertable.setWidths(new float[]{0.5f, 0.5f, 0.5f, 0.5f});
        innertable.setWidthPercentage(100);

                        //PdfPCell innercell = null;
        innercell = new PdfPCell(new Phrase("RUPEES " + StringUtils.defaultString(cpfword) + " ONLY", f2));
        innercell.setBorder(Rectangle.NO_BORDER);
        innertable.addCell(innercell);
        innercell = new PdfPCell();
        innercell.setBorder(Rectangle.NO_BORDER);
        innertable.addCell(innercell);
        innercell = new PdfPCell();
        innercell.setBorder(Rectangle.NO_BORDER);
        innertable.addCell(innercell);
        innercell = new PdfPCell(new Phrase("RUPEES " + StringUtils.defaultString(gcpfword) + " ONLY", f2));
        innercell.setBorder(Rectangle.NO_BORDER);
        innertable.addCell(innercell);
        //end of inner table
        cell = new PdfPCell(innertable);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(8);
        cell.setFixedHeight(20);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("The Basic pay entered in the column 5 of the above statement has been verified with the entries made in the Service Book Pay Bill.", f1));
        cell.setColspan(8);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(8);
        cell.setFixedHeight(10);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("(Rupees ..........................................................................................)", f1));
        cell.setColspan(8);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(8);
        cell.setFixedHeight(10);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("This is to certify that the employees mentioned above is/are appointed in a pensionable establishment request vacancy/vacancies", f1));
        cell.setColspan(8);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(8);
        cell.setFixedHeight(30);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(5);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Signature", f1));
        cell.setColspan(3);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(8);
        cell.setFixedHeight(10);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(5);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Drawing and Disbursing Officer", f1));
        cell.setColspan(3);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(8);
        cell.setFixedHeight(10);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(5);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("With the Designation and Date", f1));
        cell.setColspan(3);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(8);
        cell.setFixedHeight(10);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(5);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("(With Seal)", f1));
        cell.setColspan(3);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
    }

    private void printHeaderAnnexure2(PdfPTable table, PdfPCell cell, Font f1) {

        cell = new PdfPCell(new Phrase("Sl No", f1));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("PRAN", f1));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Name of Govt Servant", f1));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Designation", f1));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Basic Pay + GP(Rs.) + P.Pay", f1));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("D.A(Rs.)", f1));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Govt's Contribution(Rs.)", f1));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Remarks", f1));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("1", f1));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("2", f1));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("3", f1));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("4", f1));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("5", f1));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("6", f1));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("7", f1));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("8", f1));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
    }

    private void printFooterAnnexure2(PdfPTable table, PdfPCell cell, Font f1, Font f2, String cpf, String cpfword) throws Exception {

        cell = new PdfPCell();
        cell.setColspan(4);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Grand Total", f1));
        cell.setColspan(2);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(StringUtils.defaultString(cpf), f1));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(4);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        cell = new PdfPCell();
        cell.setColspan(2);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("RUPEES " + StringUtils.defaultString(cpfword) + " ONLY", f2));
        cell.setColspan(2);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(8);
        cell.setFixedHeight(20);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("The Basic pay entered in the column 5 of the above statement has been verified with the entries made in the Service Book Pay Bill.", f1));
        cell.setColspan(8);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(8);
        cell.setFixedHeight(10);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("(Rupees ..........................................................................................)", f1));
        cell.setColspan(8);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(8);
        cell.setFixedHeight(10);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("This is to certify that the employees mentioned above is/are appointed in a pensionable establishment request vacancy/vacancies", f1));
        cell.setColspan(8);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(8);
        cell.setFixedHeight(30);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(5);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Signature", f1));
        cell.setColspan(3);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(8);
        cell.setFixedHeight(10);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(5);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Drawing and Disbursing Officer", f1));
        cell.setColspan(3);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(8);
        cell.setFixedHeight(10);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(5);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("With the Designation and Date", f1));
        cell.setColspan(3);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(8);
        cell.setFixedHeight(10);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(5);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("(With Seal)", f1));
        cell.setColspan(3);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
    }

    private void printHeaderAnnexure3(PdfPTable table, PdfPCell cell, Font f1) {

        cell = new PdfPCell(new Phrase("Sl No", f1));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("PRAN", f1));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Name of Govt Servant(in Block Letter)", f1));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Designation", f1));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Bill No and Date of Employees Contribution", f1));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Amount of Employees contribution", f1));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Bill No & Date of Govt contribution", f1));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Amount of Govt contribution", f1));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Total\n(in Rs.)", f1));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("1", f1));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("2", f1));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("3", f1));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("4", f1));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("5", f1));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("6", f1));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("7", f1));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("8", f1));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("9", f1));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
    }

    private void printFooterAnnexure3(PdfPTable table, PdfPCell cell, Font f1, Font f2, String cpf, String cpfword, String gcpf, String gcpfword, String total) {

        cell = new PdfPCell(new Phrase("Grand Total", f1));
        cell.setColspan(5);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(StringUtils.defaultString(cpf), f1));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Grand Total", f1));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(StringUtils.defaultString(gcpf), f1));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(StringUtils.defaultString(total), f1));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(5);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("RUPPES " + StringUtils.defaultString(cpfword) + " ONLY", f2));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("RUPPES " + StringUtils.defaultString(gcpfword), f2));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(9);
        cell.setFixedHeight(20);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("The Basic pay entered in the column 5 of the above statement has been verified with the entries made in the Service Book Pay Bill.", f1));
        cell.setColspan(9);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(9);
        cell.setFixedHeight(10);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("(Rupees ..........................................................................................)", f1));
        cell.setColspan(9);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(9);
        cell.setFixedHeight(10);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("This is to certify that the employees mentioned above is/are appointed in a pensionable establishment request vacancy/vacancies", f1));
        cell.setColspan(9);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(9);
        cell.setFixedHeight(30);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(6);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Signature", f1));
        cell.setColspan(3);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(9);
        cell.setFixedHeight(10);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(5);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Drawing and Disbursing Officer", f1));
        cell.setColspan(4);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(9);
        cell.setFixedHeight(10);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(5);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("With the Designation and Date", f1));
        cell.setColspan(4);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(9);
        cell.setFixedHeight(10);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(6);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("(With Seal)", f1));
        cell.setColspan(3);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
    }

    private void printHeaderAnnexure4(PdfPTable table, PdfPCell cell, Font f1) {

        cell = new PdfPCell(new Phrase("Sl No", f1));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Treasury Officers\nRegd No(issued by CRA)", f1));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("PRAN", f1));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Name of Govt Servant", f1));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Basic Pay + GP + DA\nRs.", f1));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Government Servants\nContribution\nunder Tier-I\nRs.", f1));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Government Contribution\nunder Tier-I\nRs.", f1));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("TOTAL", f1));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Remarks", f1));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("1", f1));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("2", f1));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("3", f1));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("4", f1));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("5", f1));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("6", f1));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("7", f1));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("8", f1));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("9", f1));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
    }

    private void printFooterAnnexure4(PdfPTable table, PdfPCell cell, Font f1, String totcpf, String totgcpf, String trName, String total) {

        cell = new PdfPCell();
        cell.setColspan(4);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("TOTAL", f1));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(StringUtils.defaultString(totcpf), f1));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(StringUtils.defaultString(totgcpf), f1));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(StringUtils.defaultString(total), f1));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(9);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setFixedHeight(30);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(4);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Treasury Officer", f1));
        cell.setColspan(3);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell();
        cell.setColspan(2);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(4);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(trName, f1));
        cell.setColspan(3);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell();
        cell.setColspan(2);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
    }

}
