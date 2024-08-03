/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.view;

import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import hrms.model.payroll.payslip.ADDetails;
import hrms.model.payroll.payslip.PaySlipDetailBean;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.view.AbstractView;

/**
 *
 * @author Manas
 */
public class PaySlipPdfView extends AbstractView {

    public PaySlipPdfView() {
        setContentType("application/pdf");
    }

    @Override
    protected boolean generatesDownloadContent() {
        return true;
    }

    @Override
    protected void renderMergedOutputModel(Map<String, Object> model,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        Document document = new Document(PageSize.A4);
        ADDetails ad = null;
        double totalAllowance = 0;
        double totalDeduction = 0;
        double totalPrivateDeduction = 0;
        double totalLoan = 0;
        PaySlipDetailBean pbeandetail = (PaySlipDetailBean) model.get("empdata");
        ADDetails[] allowancelist = (ADDetails[]) model.get("allowancelist");
        ADDetails[] deductionlist = (ADDetails[]) model.get("deductionlist");
        List loanList = (List) model.get("loanList");
        List privateDeductionlist = (List) model.get("privateDeductionlist");
        PdfPCell cell;
        try {
            Font f1 = new Font();
            f1.setSize(10);
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();

            Paragraph p1 = new Paragraph("PAY SLIP", new Font(Font.FontFamily.TIMES_ROMAN, 10));
            p1.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(p1);
            p1 = new Paragraph("( " + pbeandetail.getOffName() + " )", new Font(Font.FontFamily.TIMES_ROMAN, 8));
            p1.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(p1);
            p1 = new Paragraph("Name and Designation of the Incumbent: " + pbeandetail.getEmpName() + ", " + pbeandetail.getCurDesig(), new Font(Font.FontFamily.TIMES_ROMAN, 8));
            p1.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(p1);
            document.add(new Paragraph(""));
            p1 = new Paragraph("------------------------------------------------------------------------------------");
            p1.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(p1);

            Font fontH1 = new Font();
            fontH1.setSize(8);

            /*Employee Basic Profile and Pay*/
            float[] pointColumnWidths = {100F, 150F, 100F, 150F, 100F, 150F};
            PdfPTable empBasicProfileTbl = new PdfPTable(pointColumnWidths);
            empBasicProfileTbl.setWidthPercentage(100);
            empBasicProfileTbl.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            empBasicProfileTbl.getDefaultCell().setPhrase(p1);

            empBasicProfileTbl.addCell(new Phrase("GPF Ac No :", fontH1));
            empBasicProfileTbl.addCell(new Phrase(pbeandetail.getGpfno(), fontH1));
            empBasicProfileTbl.addCell(new Phrase("For the month of :", fontH1));
            empBasicProfileTbl.addCell(new Phrase(pbeandetail.getForMonth() + "-" + pbeandetail.getForYear(), fontH1));
            empBasicProfileTbl.addCell(new Phrase("Bank :", fontH1));
            empBasicProfileTbl.addCell(new Phrase(pbeandetail.getBank(), fontH1));

            empBasicProfileTbl.addCell(new Phrase("Scale of Pay :", fontH1));
            empBasicProfileTbl.addCell(new Phrase(pbeandetail.getScalePay(), fontH1));
            empBasicProfileTbl.addCell(new Phrase("No of days worked :", fontH1));
            empBasicProfileTbl.addCell(new Phrase(pbeandetail.getDaysWork(), fontH1));
            empBasicProfileTbl.addCell(new Phrase("Bank A/c No :", fontH1));
            empBasicProfileTbl.addCell(new Phrase(pbeandetail.getBankAcno(), fontH1));

            empBasicProfileTbl.addCell(new Phrase("Current Basic :", fontH1));
            empBasicProfileTbl.addCell(new Phrase(pbeandetail.getCurBasic(), fontH1));
            empBasicProfileTbl.addCell(new Phrase("Pay of the Month :", fontH1));
            empBasicProfileTbl.addCell(new Phrase(pbeandetail.getCurBasic(), fontH1));
            empBasicProfileTbl.addCell(new Phrase("Bill No :", fontH1));
            empBasicProfileTbl.addCell(new Phrase(pbeandetail.getBillNo(), fontH1));

            empBasicProfileTbl.addCell(new Phrase("TV No:", fontH1));
            empBasicProfileTbl.addCell(new Phrase(pbeandetail.getVchno(), fontH1));
            empBasicProfileTbl.addCell(new Phrase("TV Date:", fontH1));
            empBasicProfileTbl.addCell(new Phrase(pbeandetail.getVchdate(), fontH1));
            empBasicProfileTbl.addCell(new Phrase("Bill Date:", fontH1));
            empBasicProfileTbl.addCell(new Phrase(pbeandetail.getBilldate(), fontH1));
            document.add(empBasicProfileTbl);

            Font fontH2 = new Font();
            fontH2.setSize(8);
            /*Employee Basic Profile and Pay*/
            float[] pointColumnWidths1 = {50F, 250F, 150F};
            PdfPTable salaryStructure = new PdfPTable(3);
            salaryStructure.setWidthPercentage(100);
            salaryStructure.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            /*Allowance List*/
            PdfPTable allowanceTbl = new PdfPTable(pointColumnWidths1);
            allowanceTbl.addCell(new Phrase("SL", fontH2));
            allowanceTbl.addCell(new Phrase("Allowances", fontH2));
            allowanceTbl.addCell(new Phrase("Amount", fontH2));
            for (int i = 0; i < allowancelist.length; i++) {
                ad = (ADDetails) allowancelist[i];
                allowanceTbl.addCell(new Phrase((i + 1) + "", fontH2));
                allowanceTbl.addCell(new Phrase(ad.getAdCode(), fontH2));
                allowanceTbl.addCell(new Phrase(ad.getAdAmount() + "", fontH2));
                totalAllowance = ad.getAdAmount();
            }            
            salaryStructure.addCell(allowanceTbl);
            /*Allowance List*/

            /*Deduction and Loan List*/
            PdfPTable deductionTbl = new PdfPTable(pointColumnWidths1);
            deductionTbl.addCell(new Phrase("SL", fontH2));
            deductionTbl.addCell(new Phrase("Deductions", fontH2));
            deductionTbl.addCell(new Phrase("Amount", fontH2));
            for (int i = 0; i < deductionlist.length; i++) {
                ad = (ADDetails) deductionlist[i];
                deductionTbl.addCell(new Phrase((i + 1) + "", fontH2));
                deductionTbl.addCell(new Phrase(ad.getAdCode(), fontH2));
                deductionTbl.addCell(new Phrase(ad.getAdAmount() + "", fontH2));
                totalDeduction = ad.getAdAmount();
            }
            for (int i = 0; i < loanList.size(); i++) {
                ad = (ADDetails) loanList.get(i);
                deductionTbl.addCell(new Phrase((i + 1) + "", fontH2));
                deductionTbl.addCell(new Phrase(ad.getAdCode(), fontH2));
                deductionTbl.addCell(new Phrase(ad.getAdAmount() + "", fontH2));
                totalLoan = ad.getAdAmount();
            }
            salaryStructure.addCell(deductionTbl);
            /*Deduction and Loan List*/

            /*Private Deduction List*/
            PdfPTable pvtDeductionTbl = new PdfPTable(pointColumnWidths1);
            pvtDeductionTbl.addCell(new Phrase("SL", fontH2));
            pvtDeductionTbl.addCell(new Phrase("Pvt Deductions", fontH2));
            pvtDeductionTbl.addCell(new Phrase("Amount", fontH2));
            for (int i = 0; i < privateDeductionlist.size(); i++) {
                ad = (ADDetails) privateDeductionlist.get(i);
                pvtDeductionTbl.addCell(new Phrase((i + 1) + "", fontH2));
                pvtDeductionTbl.addCell(new Phrase(ad.getAdCode(), fontH2));
                pvtDeductionTbl.addCell(new Phrase(ad.getAdAmount() + "", fontH2));
                totalPrivateDeduction = ad.getAdAmount();
            }
            salaryStructure.addCell(pvtDeductionTbl);
            /*Private Deduction List*/
            
            allowanceTbl = new PdfPTable(pointColumnWidths1);
            cell = new PdfPCell(new Phrase("Total Allowance", fontH2));
            cell.setColspan(2);
            allowanceTbl.addCell(cell);            
            allowanceTbl.addCell(new Phrase(totalAllowance+"", fontH2));
            
            cell = new PdfPCell(new Phrase("Gross Amount", fontH2));
            cell.setColspan(2);
            allowanceTbl.addCell(cell);
            if(pbeandetail.getCurBasic() != null && !pbeandetail.getCurBasic().equals("")){
                allowanceTbl.addCell(new Phrase((totalAllowance+Double.parseDouble(pbeandetail.getCurBasic()))+"", fontH2));
            }else{
                allowanceTbl.addCell(new Phrase((totalAllowance)+"", fontH2));
            }
            salaryStructure.addCell(allowanceTbl);
            
            deductionTbl = new PdfPTable(pointColumnWidths1);
            cell = new PdfPCell(new Phrase("Total Deduction", fontH2));
            cell.setColspan(2);
            deductionTbl.addCell(cell);            
            deductionTbl.addCell(new Phrase(totalDeduction+"", fontH2));
            salaryStructure.addCell(deductionTbl);
            
            pvtDeductionTbl = new PdfPTable(pointColumnWidths1);
            cell = new PdfPCell(new Phrase("Private Deduction", fontH2));
            cell.setColspan(2);
            pvtDeductionTbl.addCell(cell);            
            pvtDeductionTbl.addCell(new Phrase(totalPrivateDeduction+"", fontH2));
            
            cell = new PdfPCell(new Phrase("Net amount", fontH2));
            cell.setColspan(2);
            pvtDeductionTbl.addCell(cell);
            double netAmount = 0;
            if(pbeandetail.getCurBasic() != null && !pbeandetail.getCurBasic().equals("")){
                netAmount = (totalAllowance+ Double.parseDouble(pbeandetail.getCurBasic())) - (totalDeduction+totalPrivateDeduction);
            }else{
                netAmount = totalAllowance - (totalDeduction+totalPrivateDeduction);
            }            
            pvtDeductionTbl.addCell(new Phrase((netAmount)+"", fontH2));
            
            salaryStructure.addCell(pvtDeductionTbl);            
            document.add(salaryStructure);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();

        }
    }
}
