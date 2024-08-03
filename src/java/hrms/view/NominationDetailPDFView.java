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
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import hrms.model.recommendation.RecommendationDetailBean;
import java.io.File;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.view.AbstractView;

/**
 *
 * @author lenovo pc
 */
public class NominationDetailPDFView extends AbstractView {

    public NominationDetailPDFView() {
        setContentType("application/pdf");
    }

    @Override
    protected boolean generatesDownloadContent() {
        return true;
    }

    protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {

        RecommendationDetailBean recommendationDetailBean = (RecommendationDetailBean) model.get("recommendationDetailBean");
        Document document = new Document(PageSize.A4);
        Phrase phrs = new Phrase();
        PdfWriter writer = null;
        try {
            Font f1 = new Font();
            f1.setSize(10);
            writer = PdfWriter.getInstance(document, response.getOutputStream());
            document.open();

            PdfPTable table = null;
            PdfPTable innertable = null;

            table = new PdfPTable(4);
            table.setWidths(new int[]{2, 3, 3, 3});
            table.setWidthPercentage(100);

            PdfPCell cell = null;

            cell = new PdfPCell(new Phrase("Format For Submission of Information by the higher authorities / fiels establishments / Directorates / District Collectors"
                    + "to the Screening Committe for consideration of Out of turn promotion (within the batch and across the batch) / award of Incentives", new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.BOLD)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(StringUtils.repeat("_", 93), f1));
            cell.setColspan(4);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("1. Name and designation of the employee:", new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.BOLD)));
            cell.setColspan(2);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBorderWidth(1f);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(recommendationDetailBean.getRecommendedempname() + ", " + recommendationDetailBean.getRecommendedpost(), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)));
            cell.setColspan(2);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(" 2. Nomination Type:", new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.BOLD)));
            cell.setColspan(2);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBorderWidth(1f);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(recommendationDetailBean.getRecommenadationType().toUpperCase(), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBorderWidth(1f);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            if (!recommendationDetailBean.getRecommenadationType().equals("Premature Retirement")) {
                cell = new PdfPCell(new Phrase(" 3. Name Of the Office:", new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.BOLD)));
                cell.setColspan(2);
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setBorderWidth(1f);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(recommendationDetailBean.getRecommendedempofficename(), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)));
                cell.setColspan(2);
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setBorderWidth(1f);
                table.addCell(cell);

                cell = new PdfPCell();
                cell.setColspan(4);
                cell.setFixedHeight(10);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(" 4. Detail information:", new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.BOLD)));
                cell.setColspan(2);
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setBorderWidth(1f);
                table.addCell(cell);

                cell = new PdfPCell();
                cell.setColspan(4);
                cell.setFixedHeight(10);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(" a) Commendation issued by Government Authorities", new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.BOLD)));
                cell.setColspan(2);
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setBorderWidth(1f);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(recommendationDetailBean.getRecommendationandCommendation(), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)));
                cell.setColspan(2);
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setBorderWidth(1f);
                table.addCell(cell);

                cell = new PdfPCell();
                cell.setColspan(4);
                cell.setFixedHeight(10);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(" b) Exceptional work done in public interest", new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.BOLD)));
                cell.setColspan(2);
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setBorderWidth(1f);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(recommendationDetailBean.getEmpExceptionalWork(), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)));
                cell.setColspan(2);
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setBorderWidth(1f);
                table.addCell(cell);

                cell = new PdfPCell();
                cell.setColspan(4);
                cell.setFixedHeight(10);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(" c) Any document(s) certifying performance and conduct", new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.BOLD)));
                cell.setColspan(2);
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setBorderWidth(1f);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(recommendationDetailBean.getEmpActivitiesandIssue(), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)));
                cell.setColspan(2);
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setBorderWidth(1f);
                table.addCell(cell);

                cell = new PdfPCell();
                cell.setColspan(4);
                cell.setFixedHeight(10);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(" d) Reason(s) for nomination by field office", new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.BOLD)));
                cell.setColspan(2);
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setBorderWidth(1f);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(recommendationDetailBean.getReasonforrecommendation(), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)));
                cell.setColspan(2);
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setBorderWidth(1f);
                table.addCell(cell);
                if (recommendationDetailBean.getIsSubmittedToDept() != null && recommendationDetailBean.getIsSubmittedToDept().equals("Y")) {
                    cell = new PdfPCell(new Phrase(" Overall views on why the employee is considered worthy of nomination", new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.BOLD)));
                    cell.setColspan(2);
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setBorderWidth(1f);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(recommendationDetailBean.getOverallviews(), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)));
                    cell.setColspan(2);
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setBorderWidth(1f);
                    table.addCell(cell);
                }
            } else {
                cell = new PdfPCell(new Phrase("3. Overall reasons to nominate for Premature Retirement", new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.BOLD)));
                cell.setColspan(2);
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setBorderWidth(1f);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(recommendationDetailBean.getReasonforrecommendation(), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)));
                cell.setColspan(2);
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setBorderWidth(1f);
                table.addCell(cell);
            }

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            document.add(table);

            /*---------Add Suporting Document------------*/
            if (recommendationDetailBean.getAuthoritiesdiskfilename() != null && !recommendationDetailBean.getAuthoritiesdiskfilename().equals("")) {
                String src = recommendationDetailBean.getUploadedPath() + File.separator + recommendationDetailBean.getAuthoritiesdiskfilename();
                PdfReader reader = new PdfReader(src);
                int totalPage = reader.getNumberOfPages();
                PdfImportedPage page;
                PdfContentByte pdfContentByte = writer.getDirectContent();
                for (int i = 1; i <= totalPage; i++) {
                    document.newPage();
                    page = writer.getImportedPage(reader, i);
                    pdfContentByte.addTemplate(page, 0, 0);
                }
            }
            if (recommendationDetailBean.getExceptionalworkdiskfilename() != null && !recommendationDetailBean.getExceptionalworkdiskfilename().equals("")) {
                String src = recommendationDetailBean.getUploadedPath() + File.separator + recommendationDetailBean.getExceptionalworkdiskfilename();
                PdfReader reader = new PdfReader(src);
                int totalPage = reader.getNumberOfPages();
                PdfImportedPage page;
                PdfContentByte pdfContentByte = writer.getDirectContent();
                for (int i = 1; i <= totalPage; i++) {
                    document.newPage();
                    page = writer.getImportedPage(reader, i);
                    pdfContentByte.addTemplate(page, 0, 0);
                }
            }
            if (recommendationDetailBean.getOtheractivitiesdiskfilename() != null && !recommendationDetailBean.getOtheractivitiesdiskfilename().equals("")) {
                String src = recommendationDetailBean.getUploadedPath() + File.separator + recommendationDetailBean.getOtheractivitiesdiskfilename();
                PdfReader reader = new PdfReader(src);
                int totalPage = reader.getNumberOfPages();
                PdfImportedPage page;
                PdfContentByte pdfContentByte = writer.getDirectContent();
                for (int i = 1; i <= totalPage; i++) {
                    document.newPage();
                    page = writer.getImportedPage(reader, i);
                    pdfContentByte.addTemplate(page, 0, 0);
                }
            }
            if (recommendationDetailBean.getReasonforrecommendationdiskfilename() != null && !recommendationDetailBean.getReasonforrecommendationdiskfilename().equals("")) {
                String src = recommendationDetailBean.getUploadedPath() + File.separator + recommendationDetailBean.getReasonforrecommendationdiskfilename();
                PdfReader reader = new PdfReader(src);
                int totalPage = reader.getNumberOfPages();
                PdfImportedPage page;
                PdfContentByte pdfContentByte = writer.getDirectContent();
                for (int i = 1; i <= totalPage; i++) {
                    document.newPage();
                    page = writer.getImportedPage(reader, i);
                    pdfContentByte.addTemplate(page, 0, 0);
                }
            }
            if (recommendationDetailBean.getOverallviewsdiskfilename() != null && !recommendationDetailBean.getOverallviewsdiskfilename().equals("")) {
                String src = recommendationDetailBean.getUploadedPath() + File.separator + recommendationDetailBean.getOverallviewsdiskfilename();
                PdfReader reader = new PdfReader(src);
                int totalPage = reader.getNumberOfPages();
                PdfImportedPage page;
                PdfContentByte pdfContentByte = writer.getDirectContent();
                for (int i = 1; i <= totalPage; i++) {
                    document.newPage();
                    page = writer.getImportedPage(reader, i);
                    pdfContentByte.addTemplate(page, 0, 0);
                }
            }
            /*---------Add Suporting Document-----reasonforrecommendation-------*/
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }
}
