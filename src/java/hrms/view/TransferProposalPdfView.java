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
import com.itextpdf.tool.xml.ElementHandler;
import com.itextpdf.tool.xml.Writable;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.pipeline.WritableElement;
import hrms.model.TransferProposal.TransferProposalBean;
import hrms.model.TransferProposal.TransferProposalDetail;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.springframework.web.servlet.view.AbstractView;

/**
 *
 * @author Manas
 */
public class TransferProposalPdfView extends AbstractView {

    public TransferProposalPdfView() {
        setContentType("application/pdf");
    }

    @Override
    protected boolean generatesDownloadContent() {
        return true;
    }

    private Paragraph parseHtmlToPDF(String str) throws IOException {
        StringReader body = new StringReader(str);
        final Paragraph para = new Paragraph();

        XMLWorkerHelper.getInstance().parseXHtml(new ElementHandler() {
            @Override
            public void add(Writable w) {
                if (w instanceof WritableElement) {
                    List<Element> elements = ((WritableElement) w).elements();
                    for (Element e : elements) {
                        para.add(e);
                    }
                }
            }
        }, body);
        return para;
    }

    @Override
    protected void renderMergedOutputModel(Map<String, Object> model,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        Document document = new Document(PageSize.A4);
        try {
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();
            TransferProposalBean transferProposalBean = (TransferProposalBean) model.get("tpf");
            ArrayList transferEmployeeList = (ArrayList) model.get("transferEmployeeList");
            Font textFont = new Font(Font.FontFamily.HELVETICA, 11f, Font.NORMAL, BaseColor.BLACK);
            Font bigTextFont = new Font(Font.FontFamily.TIMES_ROMAN, 10f, Font.NORMAL, BaseColor.BLACK);
            Font boldTextFont1 = new Font(Font.FontFamily.HELVETICA, 12f, Font.BOLD | Font.UNDERLINE, BaseColor.BLACK);
            Font boldTextFont = new Font(Font.FontFamily.HELVETICA, 9f, Font.BOLD, BaseColor.BLACK);
            Font hdrTextFont = new Font(Font.FontFamily.HELVETICA, 10f, Font.BOLD, BaseColor.BLACK);

            //start of first table 
            //PdfPTable maintable = new PdfPTable(1);
            //maintable.setWidths(new int[]{8});
            //maintable.setWidthPercentage(100);
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
            String orderdate = "____________________";
            if (transferProposalBean.getOrderDate() != null) {
                orderdate = transferProposalBean.getOrderDate();
            }
            heading = new Paragraph("Bhubaneswar Dated the " + orderdate, textFont);
            heading.setAlignment(Element.ALIGN_CENTER);
            document.add(heading);
            Paragraph letterHead = new Paragraph(transferProposalBean.getLetterheader(),textFont);
            letterHead.setAlignment(Paragraph.ALIGN_JUSTIFIED);
            letterHead.setSpacingBefore(15f);
            document.add(letterHead);
            String newPost = "";
            for (int i = 0; i < transferEmployeeList.size(); i++) {
                TransferProposalBean tpf = new TransferProposalBean();
                tpf = (TransferProposalBean) transferEmployeeList.get(i);
                ArrayList transferProposalDetailList = tpf.getTransferProposalDetail();
                TransferProposalDetail transferProposalDetail = (TransferProposalDetail) transferProposalDetailList.get(0);
                Paragraph phrs = new Paragraph(18);
                //phrs.setFont(bigTextFont);
                phrs.setAlignment(Paragraph.ALIGN_JUSTIFIED);

                //Phrase phrs = new Phrase();
                String letterNo = "";
                if (transferProposalDetail.getLetterno() != null) {
                    letterNo = transferProposalDetail.getLetterno();
                }
                if (letterNo.equals("null/0")) {
                    letterNo = "____________________";
                }
                String cadreInfo = "";
                if (tpf.getCadreName() != null) {
                    cadreInfo = ", " + tpf.getCadreName();
                }
                Chunk c1 = new Chunk("\nNo." + letterNo + ", ", textFont);
                Chunk c2 = new Chunk(tpf.getEmpName() + cadreInfo, boldTextFont);
                String postName = tpf.getPostedPostName().replace(",", ", ");
                Chunk c3 = new Chunk(" at present " + WordUtils.capitalizeFully(postName), textFont);
                Chunk c5 = null;
                if (transferProposalDetail.getIsadditional().equals("N")) {
                    newPost = transferProposalDetail.getNewpost().replace(",", ", ");
                    c5 = new Chunk(" is transferred and is posted as " + WordUtils.capitalizeFully(newPost) + ".", textFont);
                } else if (transferProposalDetail.getIsadditional().equals("D")) {
                    newPost = transferProposalDetail.getNewpost().replace(",", ", ");
                    c5 = new Chunk(" is posted as " + WordUtils.capitalizeFully(newPost) + " on Foreign Service terms & conditions.", textFont);
                }
                phrs.add(c1);
                phrs.add(c2);
                phrs.add(c3);
                phrs.add(c5);
                for (int j = 0; j < transferProposalDetailList.size(); j++) {
                    transferProposalDetail = (TransferProposalDetail) transferProposalDetailList.get(j);
                    if (transferProposalDetail.getIsadditional().equals("Y")) {
                        newPost = transferProposalDetail.getNewpost().replace(",", ", ");
                        Chunk c4 = new Chunk("\n\nHe is allowed to remain additional charge of " + WordUtils.capitalizeFully(newPost) + ".", textFont);
                        phrs.add(c4);
                    }
                }
                document.add(phrs);
                //maincell = new PdfPCell(phrs);
                //maincell.setBorder(Rectangle.NO_BORDER);
                //maincell.setHorizontalAlignment(Element.ALIGN_LEFT);
                //maintable.addCell(maincell);
            }
            //document.add(maintable);
            
            //parseHtmlToPDF("<html>" + transferProposalBean.getLetterfooter() + "</html>");
            Paragraph letterFooter = new Paragraph(transferProposalBean.getLetterfooter(),textFont);
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
