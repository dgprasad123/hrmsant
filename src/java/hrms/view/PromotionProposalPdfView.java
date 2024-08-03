/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.view;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
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
import hrms.model.TransferProposal.TransferProposalBean;
import hrms.model.TransferProposal.TransferProposalDetail;
import java.util.ArrayList;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.WordUtils;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.springframework.web.servlet.view.AbstractView;

/**
 *
 * @author Manas
 */
public class PromotionProposalPdfView extends AbstractView {

    @Override
    protected boolean generatesDownloadContent() {
        return true;
    }

    @Override
    protected void renderMergedOutputModel(Map<String, Object> model,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        Document document = new Document(PageSize.A4);
        try {
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();
            ArrayList transferEmployeeList = (ArrayList) model.get("transferEmployeeList");
            TransferProposalBean transferProposalBean = (TransferProposalBean) model.get("tpf");

            Font textFont = new Font(Font.FontFamily.HELVETICA, 11f, Font.NORMAL, BaseColor.BLACK);
            Font bigTextFont = new Font(Font.FontFamily.HELVETICA, 9f, Font.NORMAL, BaseColor.BLACK);
            Font boldTextFont1 = new Font(Font.FontFamily.HELVETICA, 12f, Font.BOLD | Font.UNDERLINE, BaseColor.BLACK);
            Font boldTextFont = new Font(Font.FontFamily.HELVETICA, 9f, Font.BOLD, BaseColor.BLACK);
            Font hdrTextFont = new Font(Font.FontFamily.HELVETICA, 10f, Font.BOLD, BaseColor.BLACK);

            PdfPCell maincell = null;

            //START OF FIRST MAIN ROW
            Paragraph heading = new Paragraph("GOVERNMENT OF ODISHA", textFont);
            heading.setAlignment(Element.ALIGN_CENTER);
            document.add(heading);

            heading = new Paragraph("General Administration & Public Grievance Department", textFont);
            heading.setAlignment(Element.ALIGN_CENTER);
            document.add(heading);

            heading = new Paragraph("***", textFont);
            heading.setAlignment(Element.ALIGN_CENTER);
            document.add(heading);

            heading = new Paragraph("NOTIFICATION", boldTextFont1);
            heading.setAlignment(Element.ALIGN_CENTER);
            document.add(heading);
            String orderDate = "______________";
            if (transferProposalBean.getOrderDate() != null) {
                orderDate = transferProposalBean.getOrderDate();
            }
            heading = new Paragraph("Bhubaneswar Dated the " + orderDate, textFont);
            heading.setAlignment(Element.ALIGN_CENTER);
            document.add(heading);
            String orderNumber = "______________";
            if (transferProposalBean.getOrderNumber() != null && !transferProposalBean.getOrderNumber().equals("null")) {
                orderNumber = transferProposalBean.getOrderDate();
            }
            Paragraph letterHead = new Paragraph(transferProposalBean.getLetterheader(), textFont);
            letterHead.setAlignment(Paragraph.ALIGN_JUSTIFIED);
            letterHead.setSpacingBefore(15f);
            document.add(letterHead);

            PdfPTable maintable = new PdfPTable(3);
            maintable.setWidths(new int[]{1, 3, 4});
            maintable.setWidthPercentage(100);
            maintable.setSpacingBefore(12f);
            maintable.addCell(new PdfPCell(new Paragraph("Sl.No.", boldTextFont)));
            maintable.addCell(new PdfPCell(new Paragraph("Name of the officer", boldTextFont)));
            maintable.addCell(new PdfPCell(new Paragraph("Present Position / Place of Posting", boldTextFont)));

            for (int i = 0; i < transferEmployeeList.size(); i++) {
                TransferProposalBean tpf = (TransferProposalBean) transferEmployeeList.get(i);
                ArrayList transferProposalDetailList = tpf.getTransferProposalDetail();
                TransferProposalDetail transferProposalDetail = (TransferProposalDetail) transferProposalDetailList.get(0);
                maintable.addCell(new PdfPCell(new Paragraph((i + 1) + "", textFont)));
                maintable.addCell(new PdfPCell(new Paragraph(WordUtils.capitalizeFully(tpf.getEmpName()), textFont)));

                Paragraph phrs = new Paragraph(42);
                if (transferProposalDetail.getIsadditional().equals("N")) {
                    Chunk c1 = new Chunk(WordUtils.capitalizeFully(transferProposalDetail.getNewpost()), textFont);
                    phrs.add(c1);

                }
                for (int j = 0; j < transferProposalDetailList.size(); j++) {
                    transferProposalDetail = (TransferProposalDetail) transferProposalDetailList.get(j);
                    if (transferProposalDetail.getIsadditional().equals("Y")) {
                        Chunk c4 = new Chunk("\nHe is allowed to remain additional charge of " + WordUtils.capitalizeFully(transferProposalDetail.getNewpost()) + ".", textFont);
                        phrs.add(c4);
                    }
                }
                maintable.addCell(new PdfPCell(phrs));
            }
            document.add(maintable);
            Paragraph letterFooter = new Paragraph(transferProposalBean.getLetterfooter(), textFont);
            letterFooter.setAlignment(Paragraph.ALIGN_JUSTIFIED);
            letterFooter.setSpacingBefore(20f);
            document.add(letterFooter);

            PdfPTable signtable = new PdfPTable(1);
            signtable.setSpacingBefore(22f);
            //signtable.setWidths(new int[]{3});
            signtable.setWidthPercentage(100);
            signtable.setHorizontalAlignment(Element.ALIGN_RIGHT);

            PdfPCell cellOne = new PdfPCell(new Phrase("By order of the Governor"));
            cellOne.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cellOne.setBorder(Rectangle.NO_BORDER);
            cellOne.setFixedHeight(40);
            signtable.addCell(cellOne);

            cellOne = new PdfPCell(new Phrase(transferProposalBean.getAuthorityName()));
            cellOne.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cellOne.setBorder(Rectangle.NO_BORDER);
            signtable.addCell(cellOne);
            if (transferProposalBean.getAuthorityDesg() != null) {
                cellOne = new PdfPCell(new Phrase("(" + transferProposalBean.getAuthorityDesg() + ")"));
                cellOne.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cellOne.setBorder(Rectangle.NO_BORDER);
                signtable.addCell(cellOne);
            }
            document.add(signtable);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();

        }
    }
}
