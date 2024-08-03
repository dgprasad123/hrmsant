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
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import hrms.model.parmast.ParAdverseCommunicationDetail;
import java.io.File;
import java.sql.Timestamp;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.view.AbstractView;

/**
 *
 * @author Manas
 */
public class SiParAdverseRemarkIntimationPDF extends AbstractView {

    public SiParAdverseRemarkIntimationPDF() {
        setContentType("application/pdf");
    }

    @Override
    protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest hsr, HttpServletResponse response) throws Exception {
        ParAdverseCommunicationDetail empData = (ParAdverseCommunicationDetail) model.get("EmpData");
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();
        
        FontFactory.register(System.getProperty("file.separator")+"resources"+System.getProperty("file.separator")+"fonts"+System.getProperty("file.separator")+"tahoma.‌​ttf", "my_bold_font");
        //FontFactory.register(System.getProperty("file.separator")+"resources"+System.getProperty("file.separator")+"fonts"+System.getProperty("file.separator")+"arial.‌​ttf", "my_bold_font");
        Font hdrTextFont = FontFactory.getFont("my_bold_font");
        hdrTextFont.setColor(BaseColor.BLACK);
        hdrTextFont.setStyle(Font.BOLD);
        hdrTextFont.setSize(10);
        
        FontFactory.register(System.getProperty("file.separator")+"resources"+System.getProperty("file.separator")+"fonts"+System.getProperty("file.separator")+"tahoma.‌​ttf", "my_bold_font");
        //FontFactory.register(System.getProperty("file.separator")+"resources"+System.getProperty("file.separator")+"fonts"+System.getProperty("file.separator")+"arial.‌​ttf", "my_bold_font");
        Font dataHdrTextFont = FontFactory.getFont("my_bold_font");
        dataHdrTextFont.setColor(BaseColor.BLACK);
        dataHdrTextFont.setStyle(Font.BOLD);
        dataHdrTextFont.setSize(11);
        
        FontFactory.register(System.getProperty("file.separator")+"resources"+System.getProperty("file.separator")+"fonts"+System.getProperty("file.separator")+"tahoma.‌​ttf", "my_bold_font");
        //FontFactory.register(System.getProperty("file.separator")+"resources"+System.getProperty("file.separator")+"fonts"+System.getProperty("file.separator")+"arial.‌​ttf", "my_bold_font");
        Font dataValTextFont = FontFactory.getFont("my_bold_font");
        dataValTextFont.setColor(BaseColor.BLACK);
        dataValTextFont.setStyle(Font.NORMAL);
        dataValTextFont.setSize(11);
        
        FontFactory.register(System.getProperty("file.separator")+"resources"+System.getProperty("file.separator")+"fonts"+System.getProperty("file.separator")+"tahoma.‌​ttf", "my_bold_font");
        //FontFactory.register(System.getProperty("file.separator")+"resources"+System.getProperty("file.separator")+"fonts"+System.getProperty("file.separator")+"arial.‌​ttf", "my_bold_font");
        Font footerTextFont = FontFactory.getFont("my_bold_font");
        footerTextFont.setColor(BaseColor.BLACK);
        footerTextFont.setStyle(Font.NORMAL);
        footerTextFont.setSize(8);
        
        //Font hdrTextFont = new Font(Font.FontFamily.COURIER, 12, Font.BOLD, BaseColor.BLACK);
        //Font dataHdrTextFont = new Font(Font.FontFamily.COURIER, 14, Font.BOLD, BaseColor.BLACK);
        //Font dataValTextFont = new Font(Font.FontFamily.COURIER, 13, Font.NORMAL, BaseColor.BLACK);
        //Font footerTextFont = new Font(Font.FontFamily.COURIER, 8, Font.BOLD, BaseColor.BLACK);
            
        //String url = "D:/OFFICE WORK/myWork/HRMSOpenSource/web/images/OpLogo.jpg";
        String url = empData.getOpLogoFilePath()+ "OpLogo.jpg";
        File f = null;
        f = new File(url);
        Image img1 = null;
        if (f.exists()) {
            img1 = Image.getInstance(url);
            //img1.scalePercent(29f);
            img1.scaleToFit(120f, 140f);//Width, height
        }
            
        PdfPTable dataHdrTbl = new PdfPTable(3);
        dataHdrTbl.setWidthPercentage(100);
        float[] columnWidths1 = new float[]{35f, 30f, 35f};
        dataHdrTbl.setWidths(columnWidths1);
        
        PdfPCell dataHdrCell = null;
        
        PdfPTable innerHdrTbl = new PdfPTable(1);
        innerHdrTbl.setWidthPercentage(100);
        PdfPCell innerHdrCell = null;
                 
        dataHdrCell = new PdfPCell(new Phrase("CONFIDENTIAL", dataValTextFont));
        dataHdrCell.setColspan(3);
        dataHdrCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        dataHdrCell.setBorder(Rectangle.NO_BORDER);
        dataHdrTbl.addCell(dataHdrCell);
        
        dataHdrCell = new PdfPCell(new Phrase("", dataValTextFont));
        dataHdrCell.setColspan(3);
        dataHdrCell.setMinimumHeight(10);
        dataHdrCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        dataHdrCell.setBorder(Rectangle.NO_BORDER);
        dataHdrTbl.addCell(dataHdrCell);
        
        Chunk c12 = new Chunk("From \n", dataHdrTextFont);
        Chunk c22 = new Chunk(empData.getCustodianFullName()+",\n", hdrTextFont);
        Chunk c23 = new Chunk(empData.getCustodianDesignation(), hdrTextFont);
        Paragraph p12 = new Paragraph();
        p12.add(c12);
        p12.add(c22);
        p12.add(c23);
        
        dataHdrCell = new PdfPCell(p12);
        dataHdrCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        dataHdrCell.setBorder(Rectangle.NO_BORDER);
        dataHdrTbl.addCell(dataHdrCell);
        
        if (img1 != null) {
            
            innerHdrCell = new PdfPCell(img1);
            innerHdrCell.setBorder(Rectangle.NO_BORDER);
            innerHdrCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            innerHdrTbl.addCell(innerHdrCell);

            innerHdrCell = new PdfPCell(new Phrase("ODISHA POLICE", hdrTextFont));
            innerHdrCell.setBorder(Rectangle.NO_BORDER);
            innerHdrCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            innerHdrTbl.addCell(innerHdrCell);
            
            dataHdrCell = new PdfPCell(innerHdrTbl);
            
        }else{
            innerHdrCell = new PdfPCell(new Phrase("", dataHdrTextFont));
            innerHdrCell.setBorder(Rectangle.NO_BORDER);
            innerHdrCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            innerHdrTbl.addCell(innerHdrCell);

            innerHdrCell = new PdfPCell(new Phrase("ODISHA POLICE", new Font(Font.FontFamily.HELVETICA, 13, Font.BOLD, BaseColor.BLACK)));
            innerHdrCell.setBorder(Rectangle.NO_BORDER);
            innerHdrCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            innerHdrTbl.addCell(innerHdrCell);
            
            dataHdrCell = new PdfPCell(innerHdrTbl);
        }
        dataHdrCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        dataHdrCell.setBorder(Rectangle.NO_BORDER);
        dataHdrTbl.addCell(dataHdrCell);
        
        Chunk c11 = new Chunk("", dataHdrTextFont);
        Chunk c21 = new Chunk(empData.getCustodianAddress(), hdrTextFont);
        Paragraph p11 = new Paragraph();
        p11.add(c11);
        p11.add(c21);
        dataHdrCell = new PdfPCell(p11);
        dataHdrCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        dataHdrCell.setBorder(Rectangle.NO_BORDER);
        dataHdrTbl.addCell(dataHdrCell);

        document.add(dataHdrTbl);
        //=========================
        
        PdfPTable dataValTbl = new PdfPTable(2);
        dataValTbl.setWidthPercentage(100);
        float[] columnWidths2 = new float[]{50f, 50f};
        dataValTbl.setWidths(columnWidths2);
        
        PdfPCell dataValCell = null;
        String fullName = empData.getAppraiseFname();
        String fName = fullName.split(" ")[0];
        dataValCell = new PdfPCell(new Phrase("Dear "+fName+",", dataHdrTextFont));
        dataValCell.setColspan(2);
        dataValCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        dataValCell.setBorder(Rectangle.NO_BORDER);
        dataValTbl.addCell(dataValCell);
        
        dataValCell = new PdfPCell(new Phrase("", dataHdrTextFont));
        dataValCell.setColspan(2);
        dataValCell.setMinimumHeight(10);
        dataValCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        dataValCell.setBorder(Rectangle.NO_BORDER);
        dataValTbl.addCell(dataValCell);
        
        Chunk c1 = new Chunk("         On review of your Performance Appraisal Report(PAR) for the assessment year ", dataValTextFont);
        Chunk c2 = new Chunk(empData.getFiscalYear(), dataHdrTextFont);
        Chunk c3 = new Chunk(", it revealed that ", dataValTextFont);
        Chunk c4 = new Chunk(empData.getRemarksdetail(), dataHdrTextFont);
        Chunk c5 = new Chunk(". You have been graded as ", dataValTextFont);
        Chunk c6 = new Chunk(empData.getFinalGrading(), dataHdrTextFont);
        Chunk c7 = new Chunk(" Officer,which is treated as adverse. I hope you will try to improve. "
            + "The adverse remarks have been communicated to you in your HRMS ID ", dataValTextFont);
        Chunk c8 = new Chunk(empData.getAppraiseHrmsId(), dataHdrTextFont);
        Chunk c9 = new Chunk(" on dated ", dataValTextFont);
        Chunk c10 = new Chunk(empData.getAdverseCommunicationOnDate()+". ", dataHdrTextFont);
        
        Paragraph p1 = new Paragraph();
        p1.add(c1);
        p1.add(c2);
        p1.add(c3);
        p1.add(c4);
        p1.add(c5);
        p1.add(c6);
        p1.add(c7);
        p1.add(c8);
        p1.add(c9);
        p1.add(c10);
        
        dataValCell = new PdfPCell(p1);
        dataValCell.setColspan(2);
        dataValCell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
        dataValCell.setBorder(Rectangle.NO_BORDER);
        dataValTbl.addCell(dataValCell);
        
        dataValCell = new PdfPCell(new Phrase("", dataHdrTextFont));
        dataValCell.setColspan(2);
        dataValCell.setMinimumHeight(20);
        dataValCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        dataValCell.setBorder(Rectangle.NO_BORDER);
        dataValTbl.addCell(dataValCell);
        
        dataValCell = new PdfPCell(new Phrase("         If you wish to represent against the above adverse remarks you may do so "
            + "within 45(forty five) days from the date of receipt of this communication. Your representation should be in duplicate.", dataValTextFont));
        dataValCell.setColspan(2);
        dataValCell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
        dataValCell.setBorder(Rectangle.NO_BORDER);
        dataValTbl.addCell(dataValCell);
        
        dataValCell = new PdfPCell(new Phrase("", dataHdrTextFont));
        dataValCell.setColspan(2);
        dataValCell.setMinimumHeight(20);
        dataValCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        dataValCell.setBorder(Rectangle.NO_BORDER);
        dataValTbl.addCell(dataValCell);
        
        
        dataValCell = new PdfPCell(new Phrase("", dataHdrTextFont));
        dataValCell.setMinimumHeight(30);
        dataValCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        dataValCell.setBorder(Rectangle.NO_BORDER);
        dataValTbl.addCell(dataValCell);
        
            PdfPTable innerDataTbl1 = new PdfPTable(1);
            innerDataTbl1.setWidthPercentage(100);
            PdfPCell innerDataCell1 = null;
        
            innerDataCell1 = new PdfPCell(new Phrase("Yours Sincerely ", dataHdrTextFont));
            innerDataCell1.setBorder(Rectangle.NO_BORDER);
            innerDataCell1.setHorizontalAlignment(Element.ALIGN_LEFT);
            innerDataTbl1.addCell(innerDataCell1);

            innerDataCell1 = new PdfPCell(new Phrase("", dataValTextFont));
            innerDataCell1.setBorder(Rectangle.NO_BORDER);
            innerDataCell1.setMinimumHeight(20);
            innerDataCell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            innerDataTbl1.addCell(innerDataCell1);
            
            innerDataCell1 = new PdfPCell(new Phrase("("+empData.getCustodianFullName()+")", dataValTextFont));
            innerDataCell1.setBorder(Rectangle.NO_BORDER);
            innerDataCell1.setHorizontalAlignment(Element.ALIGN_LEFT);
            innerDataTbl1.addCell(innerDataCell1);
            
            innerDataCell1 = new PdfPCell(new Phrase("", dataHdrTextFont));
            innerDataCell1.setBorder(Rectangle.NO_BORDER);
            innerDataCell1.setHorizontalAlignment(Element.ALIGN_LEFT);
            innerDataTbl1.addCell(innerDataCell1);
        
        dataValCell = new PdfPCell(innerDataTbl1);
        dataValCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        dataValCell.setBorder(Rectangle.NO_BORDER);
        dataValTbl.addCell(dataValCell);
        
        
        dataValCell = new PdfPCell(new Phrase("", dataHdrTextFont));
        dataValCell.setColspan(2);
        dataValCell.setMinimumHeight(45);
        dataValCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        dataValCell.setBorder(Rectangle.NO_BORDER);
        dataValTbl.addCell(dataValCell);
        
            PdfPTable innerDataTbl2 = new PdfPTable(1);
            innerDataTbl2.setWidthPercentage(100);
            PdfPCell innerDataCell2 = null;

            innerDataCell2 = new PdfPCell(new Phrase(empData.getAppraiseFname()+"\n"+"Mob:-"+empData.getMobileNo(), hdrTextFont));
            innerDataCell2.setBorder(Rectangle.NO_BORDER);
            innerDataCell2.setHorizontalAlignment(Element.ALIGN_LEFT);
            innerDataTbl2.addCell(innerDataCell2);
            
            innerDataCell2 = new PdfPCell(new Phrase(empData.getAppraisePostingDetail(), hdrTextFont));
            innerDataCell2.setBorder(Rectangle.NO_BORDER);
            innerDataCell2.setHorizontalAlignment(Element.ALIGN_LEFT);
            innerDataTbl2.addCell(innerDataCell2);
        
        dataValCell = new PdfPCell(innerDataTbl2);
        dataValCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        dataValCell.setBorder(Rectangle.NO_BORDER);
        dataValTbl.addCell(dataValCell);
        
        dataValCell = new PdfPCell(new Phrase("", dataHdrTextFont));
        dataValCell.setColspan(2);
        dataValCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        dataValCell.setBorder(Rectangle.NO_BORDER);
        dataValTbl.addCell(dataValCell);
        
        
        dataValCell = new PdfPCell(new Phrase("", dataHdrTextFont));
        dataValCell.setColspan(2);
        dataValCell.setMinimumHeight(50);
        dataValCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        dataValCell.setBorder(Rectangle.BOTTOM);
        dataValTbl.addCell(dataValCell);
        
        
            PdfPTable innerDataTbl3 = new PdfPTable(3);
            float[] columnWidths3 = new float[]{40f, 20f, 40f};
            innerDataTbl3.setWidths(columnWidths3);
            PdfPCell innerDataCell3 = null;

            innerDataCell3 = new PdfPCell(new Phrase(empData.getWebAddress(), footerTextFont));
            innerDataCell3.setBorder(Rectangle.NO_BORDER);
            innerDataCell3.setHorizontalAlignment(Element.ALIGN_LEFT);
            innerDataTbl3.addCell(innerDataCell3);

            innerDataCell3 = new PdfPCell(new Phrase("", footerTextFont));
            innerDataCell3.setBorder(Rectangle.NO_BORDER);
            innerDataCell3.setHorizontalAlignment(Element.ALIGN_LEFT);
            innerDataTbl3.addCell(innerDataCell3);

            innerDataCell3 = new PdfPCell(new Phrase(empData.getCustodianAddress(), footerTextFont));
            innerDataCell3.setBorder(Rectangle.NO_BORDER);
            innerDataCell3.setHorizontalAlignment(Element.ALIGN_LEFT);
            innerDataTbl3.addCell(innerDataCell3);

            innerDataCell3 = new PdfPCell(new Phrase(empData.getWebSite(), footerTextFont));
            innerDataCell3.setBorder(Rectangle.NO_BORDER);
            innerDataCell3.setHorizontalAlignment(Element.ALIGN_LEFT);
            innerDataTbl3.addCell(innerDataCell3);

            innerDataCell3 = new PdfPCell(new Phrase("", footerTextFont));
            innerDataCell3.setBorder(Rectangle.NO_BORDER);
            innerDataCell3.setHorizontalAlignment(Element.ALIGN_LEFT);
            innerDataTbl3.addCell(innerDataCell3);

            innerDataCell3 = new PdfPCell(new Phrase("Tel./Fax No: "+empData.getTelFaxNo(), footerTextFont));
            innerDataCell3.setBorder(Rectangle.NO_BORDER);
            innerDataCell3.setHorizontalAlignment(Element.ALIGN_LEFT);
            innerDataTbl3.addCell(innerDataCell3);
                
        dataValCell = new PdfPCell(innerDataTbl3);
        dataValCell.setColspan(2);
        dataValCell.setMinimumHeight(40);
        dataValCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        dataValCell.setBorder(Rectangle.BOTTOM);
        dataValTbl.addCell(dataValCell);
             
        //SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        dataValCell = new PdfPCell(new Phrase("Printed on:- "+timestamp, footerTextFont));
        dataValCell.setColspan(2);
        dataValCell.setMinimumHeight(30);
        dataValCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        dataValCell.setBorder(Rectangle.NO_BORDER);
        dataValTbl.addCell(dataValCell);
                
        document.add(dataValTbl);
        
        document.close();
    }
}
