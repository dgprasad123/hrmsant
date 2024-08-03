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
import hrms.model.parmast.GroupCEmployee;
import java.util.ArrayList;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.view.AbstractView;

/**
 *
 * @author Manisha
 */
public class GroupCEmployeeForAppraisee extends AbstractView {

    @Override
    protected void renderMergedOutputModel(Map<String, Object> map, HttpServletRequest hsr, HttpServletResponse response) throws Exception {
        ArrayList empList = (ArrayList) map.get("empList");
        GroupCEmployee groupCEmployee = (GroupCEmployee) map.get("groupCEmployee");
        //GroupCInitiatedbean groupCInitiatedbean = (GroupCInitiatedbean) map.get("groupCInitiatedbean");
        Document document = new Document(PageSize.A4.rotate());
        response.setHeader("Content-Disposition", "attachment; filename=GroupCStatementReport_" + groupCEmployee.getGroupCpromotionId() + "_" + ".pdf");
        PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());
        writer.setViewerPreferences(PdfWriter.ALLOW_PRINTING | PdfWriter.PageLayoutSinglePage);
        document.open();

        Font f1 = new Font();
        f1.setSize(10);
        f1.setFamily("Times New Roman");

        int slNo = 1;

        PdfPTable table = null;

        PdfPTable innertable = null;

        table = new PdfPTable(3);
        table.setWidths(new int[]{1, 3, 3});
        table.setWidthPercentage(100);

        PdfPCell cell = null;
        PdfPCell innercell = null;

        cell = new PdfPCell(new Phrase("GroupC Promotion Detail Report", new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.BOLD)));
        cell.setColspan(3);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        /*cell = new PdfPCell(new Phrase("for", new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.NORMAL)));
        cell.setColspan(4);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        Phrase phrs = new Phrase();
        Chunk c1 = new Chunk("Financial year ", f1);
        Chunk c2 = new Chunk(groupCInitiatedbean.getFiscalyear(), new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.BOLD));
        phrs.add(c1);
        phrs.add(c2);
        cell = new PdfPCell(phrs);
        cell.setColspan(4);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(4);
        cell.setFixedHeight(20);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        phrs = new Phrase();
        c1 = new Chunk("Initiated By: ", f1);
        c2 = new Chunk(groupCInitiatedbean.getReportingempname(), new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.BOLD));
        Chunk c3 = new Chunk("-", new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.BOLD));
        Chunk c4 = new Chunk(groupCInitiatedbean.getReportingpost(), new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.BOLD));
        phrs.add(c1);
        phrs.add(c2);
        phrs.add(c3);
        phrs.add(c4);
        cell = new PdfPCell(phrs);
        cell.setColspan(4);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(4);
        cell.setFixedHeight(20);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell); */

        cell = new PdfPCell();
        cell.setColspan(5);
        cell.setFixedHeight(14);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("SI No", f1));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Employee Name", f1));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Designation", f1));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        
        /*cell = new PdfPCell(new Phrase("Remarks", f1));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);*/

        /*ArrayList empList = groupCInitiatedbean.getReportingdata();
        GroupCEmployee groupCEmployee = null; */
        

        for (int i = 0; i < empList.size(); i++) {
            groupCEmployee = (GroupCEmployee) empList.get(i);

            slNo++;
            innercell = new PdfPCell(new Phrase((i + 1) + "", f1));
            innercell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(innercell);
            innercell = new PdfPCell(new Phrase(groupCEmployee.getReviewedempname(), f1));
            innercell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(innercell);
            innercell = new PdfPCell(new Phrase(groupCEmployee.getReviewedpost(), f1));
            innercell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(innercell);
            

            /*if (groupCEmployee.getIsfitforShoulderingResponsibilityReporting() != null && groupCEmployee.getIsfitforShoulderingResponsibilityReporting().equals("Y")) {
                innercell = new PdfPCell(new Phrase("Fit For Promotion", f1));
                innercell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(innercell);
            } else if (groupCEmployee.getIsfitforShoulderingResponsibilityReporting() != null && groupCEmployee.getIsfitforShoulderingResponsibilityReporting().equals("N")) {
                innercell = new PdfPCell(new Phrase("Not Fit For Promotion", f1));
                innercell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(innercell);
            } else {
                innercell = new PdfPCell(new Phrase("", f1));
                innercell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(innercell);
            } */

        }

        document.add(table);

        document.close();
    }

}
