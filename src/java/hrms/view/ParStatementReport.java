/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.view;

import com.itextpdf.text.Anchor;
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
import hrms.model.fiscalyear.FiscalYear;
import hrms.model.parmast.DepartmentPromotionBean;
import hrms.model.parmast.DepartmentPromotionDetail;
import hrms.model.parmast.FiscalYearWiseParData;
import java.util.ArrayList;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.view.AbstractView;

/**
 *
 * @author manisha
 */
public class ParStatementReport extends AbstractView {

    @Override
    protected void renderMergedOutputModel(Map<String, Object> map, HttpServletRequest hsr, HttpServletResponse response) throws Exception {
        ArrayList cadrewisenrcReport = (ArrayList) map.get("cadrewisenrcReport");
        DepartmentPromotionBean dpcBean = (DepartmentPromotionBean) map.get("dpcBean");
        Document document = new Document(PageSize.A4.rotate());
        response.setHeader("Content-Disposition", "attachment; filename=ParStatementReport_" + dpcBean.getFiscalYearFrom() + "_" + dpcBean.getFiscalYearTo() + ".pdf");
        PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());
        writer.setViewerPreferences(PdfWriter.ALLOW_PRINTING | PdfWriter.PageLayoutSinglePage);
        document.open();

        Font f1 = new Font();
        f1.setSize(10);
        f1.setFamily("Times New Roman");

        int slNo = 1;

        PdfPTable table = null;

        PdfPTable innertable = null;

        table = new PdfPTable(11);
        table.setWidths(new int[]{1, 3, 3, 2, 2, 2, 2, 2, 2, 2, 2});
        table.setWidthPercentage(100);

        PdfPCell cell = null;
        PdfPCell innercell = null;

        cell = new PdfPCell(new Phrase("PAR Statement Detail Report", new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.BOLD)));
        cell.setColspan(11);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(11);
        cell.setFixedHeight(10);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("#", f1));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Employee Name", f1));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Designation", f1));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Date of birth", f1));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Year", f1));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Period From", f1));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Period To", f1));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Post Group", f1));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Status", f1));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Grade", f1));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Remarks", f1));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        for (int i = 0; i < cadrewisenrcReport.size(); i++) {
            DepartmentPromotionDetail departmentPromotionDetail = (DepartmentPromotionDetail) cadrewisenrcReport.get(i);
            ArrayList<FiscalYearWiseParData> fiscalYearList = departmentPromotionDetail.getFiscalYearList();
            int nooffiscalyear = fiscalYearList.size();
            int noofperiod = 0;
            int additionalrowspan = 0;
            for (int j = 0; j < nooffiscalyear; j++) {
                FiscalYearWiseParData fyear = fiscalYearList.get(j);
                noofperiod = noofperiod + fyear.getNoofperiod();
            }
            if (noofperiod > nooffiscalyear) {
                additionalrowspan = noofperiod - nooffiscalyear;
            }
            slNo++;
            PdfPCell slcell = new PdfPCell(new Phrase((i + 1) + "", f1));
            slcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            PdfPCell namecell = new PdfPCell(new Phrase(departmentPromotionDetail.getEmpName(), f1));
            namecell.setHorizontalAlignment(Element.ALIGN_LEFT);
            PdfPCell postcell = new PdfPCell(new Phrase(departmentPromotionDetail.getEmpPost(), f1));
            postcell.setHorizontalAlignment(Element.ALIGN_LEFT);
            PdfPCell dobcell = new PdfPCell(new Phrase(departmentPromotionDetail.getDob(), f1));
            dobcell.setHorizontalAlignment(Element.ALIGN_LEFT);
            System.out.println(departmentPromotionDetail.getEmpName() + ":noofperiod:" + noofperiod);

            /*if (nooffiscalyear > 1) {
             slcell.setRowspan(noofperiod);
             namecell.setRowspan(noofperiod);
             postcell.setRowspan(noofperiod);
             dobcell.setRowspan(noofperiod);
             }*/
            table.addCell(slcell);
            table.addCell(namecell);
            table.addCell(postcell);
            table.addCell(dobcell);

            PdfPTable yearTable = new PdfPTable(7);
            yearTable.setWidths(new int[]{2, 2, 2, 2, 2, 2, 2});
            yearTable.setWidthPercentage(100);
            for (int j = 0; j < nooffiscalyear; j++) {
                FiscalYearWiseParData fyear = fiscalYearList.get(j);
                ArrayList<FiscalYearWiseParData.Parabstractdata> yearwisedata = fyear.getYearwisedata();
                noofperiod = fyear.getNoofperiod();
                PdfPCell yearcell = new PdfPCell(new Phrase(fyear.getFy(), f1));
                yearcell.setHorizontalAlignment(Element.ALIGN_LEFT);
                yearcell.setRowspan(noofperiod);
                yearTable.addCell(yearcell);
                
                for (int k = 0; k < noofperiod; k++) {
                    FiscalYearWiseParData.Parabstractdata parabstractdata = yearwisedata.get(k);

                    cell = new PdfPCell(new Phrase(parabstractdata.getPeriodfrom(), f1));
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);                    
                    yearTable.addCell(cell);
                    
                    cell = new PdfPCell(new Phrase(parabstractdata.getPeriodto(), f1));
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    yearTable.addCell(cell);

                    cell = new PdfPCell(new Phrase(parabstractdata.getPostGroup(), f1));
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);                    
                    yearTable.addCell(cell);
                    
                    if (parabstractdata.getParstatus() == 17) {
                        Paragraph anchor_Holder = new Paragraph();
                        Anchor anchor = new Anchor("NRC");
                        anchor.setReference("https://apps.hrmsodisha.gov.in/getviewPARAdmindetail.htm?parId=" + parabstractdata.getParid() + "&taskId=");
                        cell = new PdfPCell();
                        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        anchor_Holder.add(anchor);
                        cell.addElement(anchor_Holder);
                        yearTable.addCell(cell);
                    } else if ((parabstractdata.getParstatus() != 17 && parabstractdata.getParid() != 0) && (parabstractdata.getIsreviewed().equals("Y") && parabstractdata.getPostGroup() != null && parabstractdata.getPostGroup().equals("A")) || parabstractdata.getPostGroup() != null && parabstractdata.getPostGroup().equals("B")) {
                        Paragraph anchor_Holder = new Paragraph();
                        Anchor anchor = new Anchor("PAR");
                        anchor.setReference("https://apps.hrmsodisha.gov.in/getviewPARAdmindetail.htm?parId=" + parabstractdata.getParid() + "&taskId=");
                        cell = new PdfPCell();
                        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        anchor_Holder.add(anchor);
                        cell.addElement(anchor_Holder);
                        yearTable.addCell(cell);
                    } else if (parabstractdata.getParid() == 0) {
                        cell = new PdfPCell(new Phrase("Not initiated", f1));
                        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        yearTable.addCell(cell);
                    } else if ((parabstractdata.getIsadversed() == null || parabstractdata.getIsadversed().equals("")) && parabstractdata.getPostGroup() != null && parabstractdata.getPostGroup().equals("A") && parabstractdata.getIsreviewed().equals("N")) {
                        cell = new PdfPCell(new Phrase("Not Reviewed", f1));
                        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        yearTable.addCell(cell); 
                    } else if (parabstractdata.getPostGroup() != null && parabstractdata.getPostGroup().equals("A") && parabstractdata.getIsadversed() != null && parabstractdata.getIsadversed().equals("Y")) {
                        cell = new PdfPCell(new Phrase("Adverse", f1));
                        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        yearTable.addCell(cell);
                    }

                    if ((parabstractdata.getPostGroup() != null && parabstractdata.getPostGroup().equals("A") && parabstractdata.getIsreviewed().equals("Y")) || parabstractdata.getPostGroup() != null && parabstractdata.getPostGroup().equals("B") && (parabstractdata.getParstatus() != 17)) {
                        cell = new PdfPCell(new Phrase(parabstractdata.getGradeName(), f1));
                        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        yearTable.addCell(cell);
                    }else{
                        cell = new PdfPCell(new Phrase("", f1));
                        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        yearTable.addCell(cell);
                    }

                    cell = new PdfPCell(new Phrase(parabstractdata.getRemark(), f1));
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    yearTable.addCell(cell);
                }
            }

            cell = new PdfPCell(yearTable);
            cell.setColspan(7);
            table.addCell(cell);

            System.out.println("************");
        }
        System.out.println("==============");
        document.add(table);
        System.out.println("==============");
        document.close();
    }

}
