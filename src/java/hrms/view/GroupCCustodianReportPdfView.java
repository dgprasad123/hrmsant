/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.view;

import static com.google.common.io.Files.map;
import static com.google.common.io.Files.map;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import hrms.common.CommonFunctions;
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
public class GroupCCustodianReportPdfView extends AbstractView {

    public GroupCCustodianReportPdfView() {
        setContentType("application/pdf");
    }

    protected void renderMergedOutputModel(Map<String, Object> map, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ArrayList groupCEmpList = (ArrayList) map.get("groupCEmpList");
        GroupCEmployee groupCEmployee = (GroupCEmployee) map.get("groupCEmployee");
        Document document = new Document(PageSize.A4.rotate());
        response.setHeader("Content-Disposition", "attachment; filename=GroupC Custodian Report_" + ".pdf");
        PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());
        writer.setViewerPreferences(PdfWriter.ALLOW_PRINTING | PdfWriter.PageLayoutSinglePage);
        document.open();

        Phrase phrs = new Phrase();
        try {
            Font f1 = new Font();
            f1.setSize(10);
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();

            PdfPTable table = null;
            PdfPTable innertable = null;

            table = new PdfPTable(5);
            table.setWidths(new int[]{2, 3, 3, 3, 3});
            table.setWidthPercentage(100);

            PdfPCell cell = null;
            PdfPCell innercell = null;

            table = new PdfPTable(5);
            table.setWidths(new int[]{2, 2, 2, 2, 2});
            table.setWidthPercentage(100);

            cell = new PdfPCell(new Phrase("Group C Employee List For Fit For Promotion and Not Fit For Promotion", new Font(Font.FontFamily.TIMES_ROMAN, 17, Font.BOLD)));
            cell.setColspan(5);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("For", new Font(Font.FontFamily.TIMES_ROMAN, 17, Font.BOLD)));
            cell.setColspan(5);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            phrs = new Phrase();
            Chunk c1 = new Chunk("Financial year ", new Font(Font.FontFamily.TIMES_ROMAN, 17, Font.BOLD));
            Chunk c2 = new Chunk(groupCEmployee.getFiscalyear(), new Font(Font.FontFamily.TIMES_ROMAN, 17, Font.BOLD));
            phrs.add(c1);
            phrs.add(c2);
            cell = new PdfPCell(phrs);
            cell.setColspan(5);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(5);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(5);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Employee Name", f1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Designation", f1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Remarks Of Reporting", f1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Remarks Of Reviewing", f1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Remarks Of Accepting", f1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            for (int i = 0; i < groupCEmpList.size(); i++) {
                groupCEmployee = (GroupCEmployee) groupCEmpList.get(i);
                innercell = new PdfPCell(new Phrase(groupCEmployee.getReviewedempname(), f1));
                innercell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(innercell);
                innercell = new PdfPCell(new Phrase(groupCEmployee.getReviewedpost(), f1));
                innercell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(innercell);
                if (groupCEmployee.getIsfitforShoulderingResponsibilityReporting() != null && groupCEmployee.getIsfitforShoulderingResponsibilityReporting().equals("Y")) {
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
                }
                if (groupCEmployee.getIsfitforShoulderingResponsibilityReviewing() != null && groupCEmployee.getIsfitforShoulderingResponsibilityReviewing().equals("Y")) {
                    innercell = new PdfPCell(new Phrase("Fit For Promotion", f1));
                    innercell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(innercell);
                } else if (groupCEmployee.getIsfitforShoulderingResponsibilityReviewing() != null && groupCEmployee.getIsfitforShoulderingResponsibilityReviewing().equals("N")) {
                    innercell = new PdfPCell(new Phrase("Not Fit For Promotion", f1));
                    innercell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(innercell);
                } else {
                    innercell = new PdfPCell(new Phrase("", f1));
                    innercell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(innercell);
                }
                if (groupCEmployee.getIsfitforShoulderingResponsibilityAccepting() != null && groupCEmployee.getIsfitforShoulderingResponsibilityAccepting().equals("Y")) {
                    innercell = new PdfPCell(new Phrase("Fit For Promotion", f1));
                    innercell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(innercell);
                } else if (groupCEmployee.getIsfitforShoulderingResponsibilityAccepting() != null && groupCEmployee.getIsfitforShoulderingResponsibilityAccepting().equals("N")) {
                    innercell = new PdfPCell(new Phrase("Not Fit For Promotion", f1));
                    innercell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(innercell);
                } else {
                    innercell = new PdfPCell(new Phrase("", f1));
                    innercell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(innercell);
                }

            }
            document.add(table);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();

        }
    }

}
