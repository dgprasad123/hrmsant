/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.payroll.billbrowser;

import hrms.model.login.LoginUserBean;
import java.io.BufferedOutputStream;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import jxl.Workbook;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.Label;
import org.springframework.beans.factory.annotation.Autowired;
import hrms.dao.payroll.billbrowser.AqReportDAOImpl;
import hrms.model.payroll.billbrowser.AcquaintanceBean;
import hrms.model.payroll.billbrowser.AcquaintanceDtlBean;
import hrms.model.payroll.billbrowser.AqReportFormBean;
import hrms.model.payroll.billbrowser.BillConfigObj;
import java.util.ArrayList;
import jxl.format.Colour;

/**
 *
 * @author Manas Jena
 */
@Controller
@SessionAttributes("LoginUserBean")
public class AqReportController {

    @Autowired
    AqReportDAOImpl AqReportDAO;

    @ResponseBody
    @RequestMapping(value = "aqReportExcel", method = RequestMethod.GET)
    public void AqReportExcel(HttpServletRequest request, @ModelAttribute("AqReportFormBean") AqReportFormBean aqformbean, @ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletResponse response) throws IOException {
        int gross = 0;
        int grosstotal = 0;
        int totalded = 0;
        int netpay = 0;
        int totalpvtded = 0;
        int balance = 0;

        int col = 0;
        int repcol = 0;

        response.setContentType("application/vnd.ms-excel");
        BufferedOutputStream output = null;

        try {
            String aqDtlsTableName = AqReportDAO.getAqDtlsTableName(aqformbean.getBillNo());
            BillConfigObj billConfig = AqReportDAO.getBillConfig(aqformbean.getBillNo());
            String[] col6 = billConfig.getCol6List();
            String[] col7 = billConfig.getCol7List();

            output = new BufferedOutputStream(response.getOutputStream());
            WritableWorkbook workbook = Workbook.createWorkbook(output);
            WritableSheet sheet = workbook.createSheet("Aquitance", 0);
            WritableFont headformat = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
            WritableCellFormat headcell = new WritableCellFormat(headformat);
            WritableFont cellformat = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD);
            WritableCellFormat innercell = new WritableCellFormat(cellformat);

            WritableCellFormat colorheadercell = new WritableCellFormat(headformat);
            colorheadercell.setBorder(Border.RIGHT, BorderLineStyle.MEDIUM_DASHED, Colour.RED);

            WritableCellFormat colorcell = new WritableCellFormat(cellformat);
            colorcell.setBorder(Border.RIGHT, BorderLineStyle.MEDIUM_DASHED, Colour.RED);

            /*Print Bill Header*/
            sheet.mergeCells(0, 0, 0, 1);
            Label label = new Label(0, 0, "EMP NAME", headcell);//column,row
            sheet.addCell(label);
            sheet.mergeCells(1, 0, 1, 1);
            label = new Label(1, 0, "GPF NO", headcell);//column,row
            sheet.addCell(label);
            sheet.mergeCells(2, 0, 2, 1);
            label = new Label(2, 0, "POST", headcell);//column,row
            sheet.addCell(label);
            sheet.mergeCells(3, 0, 3, 1);
            label = new Label(3, 0, "BANK_ACC_NO", headcell);//column,row
            sheet.addCell(label);
            sheet.mergeCells(4, 0, 4, 1);
            label = new Label(4, 0, "PAY SCALE", headcell);//column,row
            sheet.addCell(label);
            sheet.mergeCells(5, 0, 5, 1);
            label = new Label(5, 0, "BASIC", headcell);//column,row
            sheet.addCell(label);
            sheet.mergeCells(6, 0, 6, 1);
            label = new Label(6, 0, "SPECIAL PAY", headcell);//column,row
            sheet.addCell(label);
            sheet.mergeCells(7, 0, 7, 1);
            label = new Label(7, 0, "GRADE PAY", headcell);//column,row
            sheet.addCell(label);
            sheet.mergeCells(8, 0, 8, 1);
            label = new Label(8, 0, "DP", headcell);//column,row
            sheet.addCell(label);
            sheet.mergeCells(9, 0, 9, 1);
            label = new Label(9, 0, "DA", headcell);//column,row
            sheet.addCell(label);
            sheet.mergeCells(10, 0, 10, 1);
            label = new Label(10, 0, "IR", headcell);//column,row
            sheet.addCell(label);
            //sheet.mergeCells(10,0,10,1);
            if (col6 == null) {
                sheet.mergeCells(11, 0, 11, 1);
                label = new Label(11, 0, "HRA", headcell);//column,row
                sheet.addCell(label);
                col = 11;
            } else if (col6 != null && col6.length > 0) {
                
                col = 10;
                for (int i = 0; i < col6.length; i++) {
                    col = col + 1;
                    label = new Label(col, 1, AqReportDAO.getADCodeDesc(col6[i]), headcell);
                    sheet.addCell(label);
                }
            }
            if (col7 != null && col7.length > 0) {
                sheet.mergeCells(11, 0, 18, 0);
                label = new Label(11, 0, "OTHER ALLOWANCE", headcell);//column,row
                sheet.addCell(label);
                for (int i = 0; i < col7.length; i++) {
                    col = col + 1;
                    label = new Label(col, 1, AqReportDAO.getADCodeDesc(col7[i]), headcell);
                    sheet.addCell(label);
                }
            } else {
                col = 11;
            }
            col = col + 1;
            label = new Label(col, 1, "GROSS", headcell);
            sheet.addCell(label);
            col = col + 1;
            label = new Label(col, 1, "BRASS NO", headcell);
            sheet.addCell(label);

            int row = 1;
            int row1 = 1;
            ArrayList acquaintanceList = AqReportDAO.getAcquaintance(aqformbean.getBillNo());
            for (int q = 0; q < acquaintanceList.size(); q++) {
                AcquaintanceBean aqBean = (AcquaintanceBean) acquaintanceList.get(q);
                row++;
                row1++;
                label = new Label(0, row, aqBean.getEmpname(), innercell);
                sheet.addCell(label);
                label = new Label(1, row, aqBean.getGpfaccno(), innercell);
                sheet.addCell(label);
                label = new Label(2, row, aqBean.getCurdesg(), innercell);
                sheet.addCell(label);
                label = new Label(3, row, aqBean.getBankaccno(), innercell);
                sheet.addCell(label);
                label = new Label(4, row, aqBean.getPayscale(), innercell);
                sheet.addCell(label);
                label = new Label(5, row, aqBean.getCurbasic() + "", innercell);
                sheet.addCell(label);

                ArrayList aqDtl = AqReportDAO.getAcquaintanceDtl(aqBean.getAqslno(),aqDtlsTableName);

                boolean inWhile = false;
                for (int k = 0; k < aqDtl.size(); k++) {
                    inWhile = true;
                    AcquaintanceDtlBean aqdtl = (AcquaintanceDtlBean) aqDtl.get(k);
                    String adcode = aqdtl.getAdcode();
                    repcol = aqdtl.getRepcol();
                    if (adcode.equals("SP")) {
                        gross = gross + aqdtl.getAdamt();
                        label = new Label(6, row, aqdtl.getAdamt() + "", innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("GP")) {
                        gross = gross + aqdtl.getAdamt();
                        label = new Label(7, row, aqdtl.getAdamt() + "", innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("DP")) {
                        gross = gross + aqdtl.getAdamt();
                        label = new Label(8, row, aqdtl.getAdamt() + "", innercell);
                        sheet.addCell(label);
                    }

                    if (adcode.equals("DA")) {
                        gross = gross + aqdtl.getAdamt();
                        label = new Label(9, row, aqdtl.getAdamt() + "", innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("IR")) {
                        gross = gross + aqdtl.getAdamt();
                        label = new Label(10, row, aqdtl.getAdamt() + "", innercell);
                        sheet.addCell(label);
                    }
                    if (col6 == null) {
                        if (adcode.equals("HRA")) {
                            gross = gross + aqdtl.getAdamt();
                            label = new Label(11, row, aqdtl.getAdamt() + "", innercell);
                            sheet.addCell(label);
                        }
                        col = 11;
                    } else if (col6 != null && col6.length > 0) {
                        col = 10;
                        for (int i = 0; i < col6.length; i++) {
                            col = col + 1;
                            if (repcol == 6) {
                                if (adcode.equals(col6[i])) {
                                    gross = gross + aqdtl.getAdamt();
                                    label = new Label(col, row, aqdtl.getAdamt() + "", innercell);
                                    sheet.addCell(label);
                                }
                            }
                        }
                    }
                    //col = col + 1;

                    if (col7 != null && col7.length > 0) {
                        for (int j = 0; j < col7.length; j++) {
                            col = col + 1;
                            if (repcol == 7) {
                                if (adcode.equals(col7[j])) {
                                    gross = gross + aqdtl.getAdamt();
                                    label = new Label(col, row, aqdtl.getAdamt() + "", innercell);
                                    sheet.addCell(label);
                                }
                            }
                        }
                    } else {
                        col = 11;
                    }
                }
                if (inWhile == false) {
                    col = 10;
                    if (col6 != null && col6.length > 0) {
                        for (int j = 0; j < col6.length; j++) {
                            col = col + 1;
                        }
                    }
                    if (col7 != null && col7.length > 0) {
                        for (int j = 0; j < col7.length; j++) {
                            col = col + 1;
                        }
                    }
                }
                col = col + 1;
                gross = gross + aqBean.getCurbasic();
                label = new Label(col, row, "" + gross, innercell);
                sheet.addCell(label);
                gross = 0;
            }

            row = row + 2;
            label = new Label(6, row, "DEDUCTIONS", headcell);
            sheet.addCell(label);

            row++;

            label = new Label(0, row, "EMP NAME", headcell);//column,row
            sheet.addCell(label);
            //sheet.mergeCells(1,0,1,1);
            label = new Label(1, row, "GPF NO", headcell);//column,row
            sheet.addCell(label);
            //sheet.mergeCells(2,0,2,1);
            label = new Label(2, row, "POST", headcell);//column,row
            sheet.addCell(label);
            //sheet.mergeCells(3,0,3,1);
            label = new Label(3, row, "PAY SCALE", headcell);//column,row
            sheet.addCell(label);
            //sheet.mergeCells(4,0,4,1);
            label = new Label(4, row, "BASIC", headcell);//column,row
            sheet.addCell(label);
            //sheet.mergeCells(5,0,5,1);
            label = new Label(5, row, AqReportDAO.getADCodeDesc("GPF"), headcell);//column,row
            sheet.addCell(label);
            //sheet.mergeCells(6,0,6,1);
            label = new Label(6, row, AqReportDAO.getADCodeDesc("CPF"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(7, row, AqReportDAO.getADCodeDesc("TPF"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(8, row, AqReportDAO.getADCodeDesc("HRR"), headcell);//column,row
            sheet.addCell(label);
            //sheet.mergeCells(7,0,7,1);
            label = new Label(9, row, AqReportDAO.getADCodeDesc("FA"), headcell);//column,row
            sheet.addCell(label);
            //sheet.mergeCells(8,0,8,1);
            label = new Label(10, row, AqReportDAO.getADCodeDesc("MCA"), headcell);//column,row
            sheet.addCell(label);
            //sheet.mergeCells(9,0,9,1);
            label = new Label(11, row, AqReportDAO.getADCodeDesc("OR"), headcell);//column,row
            sheet.addCell(label);
            //sheet.mergeCells(10,0,10,1);
            label = new Label(12, row, AqReportDAO.getADCodeDesc("HBA"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(13, row, AqReportDAO.getADCodeDesc("PT"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(14, row, AqReportDAO.getADCodeDesc("GA"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(15, row, AqReportDAO.getADCodeDesc("VE"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(16, row, AqReportDAO.getADCodeDesc("SHBA"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(17, row, AqReportDAO.getADCodeDesc("MOPA"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(18, row, AqReportDAO.getADCodeDesc("GPDD"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(19, row, AqReportDAO.getADCodeDesc("HC"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(20, row, AqReportDAO.getADCodeDesc("LIC"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(21, row, AqReportDAO.getADCodeDesc("PA"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(22, row, AqReportDAO.getADCodeDesc("HIGL"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(23, row, AqReportDAO.getADCodeDesc("LIGL"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(24, row, AqReportDAO.getADCodeDesc("MIGL"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(25, row, AqReportDAO.getADCodeDesc("MAL"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(26, row, AqReportDAO.getADCodeDesc("GPIR"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(27, row, AqReportDAO.getADCodeDesc("GIS"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(28, row, AqReportDAO.getADCodeDesc("IT"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(29, row, AqReportDAO.getADCodeDesc("WRR"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(30, row, AqReportDAO.getADCodeDesc("GISA"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(31, row, AqReportDAO.getADCodeDesc("VE"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(32, row, AqReportDAO.getADCodeDesc("EP"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(33, row, AqReportDAO.getADCodeDesc("SWR"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(34, row, "Total Deductions", headcell);//column,row
            sheet.addCell(label);
            //sheet.mergeCells(11,0,15,0);

            acquaintanceList = AqReportDAO.getAcquaintance(aqformbean.getBillNo());
            for (int q = 0; q < acquaintanceList.size(); q++) {
                row++;
                row1++;
                AcquaintanceBean aqBean = (AcquaintanceBean) acquaintanceList.get(q);
                label = new Label(0, row, aqBean.getEmpname(), innercell);
                sheet.addCell(label);
                label = new Label(1, row, aqBean.getGpfaccno(), innercell);
                sheet.addCell(label);
                label = new Label(2, row, aqBean.getCurdesg(), innercell);
                sheet.addCell(label);
                label = new Label(3, row, aqBean.getPayscale(), innercell);
                sheet.addCell(label);
                label = new Label(4, row, aqBean.getCurbasic() + "", innercell);
                sheet.addCell(label);

                ArrayList aqDtl = AqReportDAO.getAcquaintanceDtl(aqBean.getAqslno(),aqDtlsTableName);

                for (int k = 0; k < aqDtl.size(); k++) {
                    AcquaintanceDtlBean aqdtl = (AcquaintanceDtlBean) aqDtl.get(k);
                    String adcode = aqdtl.getAdcode();
                    if (adcode.equals("GPF")) {
                        totalded = totalded + aqdtl.getAdamt();
                        label = new Label(5, row, aqdtl.getAdamt() + "", innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("CPF")) {
                        totalded = totalded + aqdtl.getAdamt();
                        label = new Label(6, row, aqdtl.getAdamt() + "", innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("TPF")) {
                        totalded = totalded + aqdtl.getAdamt();
                        label = new Label(7, row, aqdtl.getAdamt() + "", innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("HRR")) {
                        totalded = totalded + aqdtl.getAdamt();
                        label = new Label(8, row, aqdtl.getAdamt() + "", innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("FA")) {
                        totalded = totalded + aqdtl.getAdamt();
                        label = new Label(9, row, aqdtl.getAdamt() + "", innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("MCA")) {
                        totalded = totalded + aqdtl.getAdamt();
                        label = new Label(10, row, aqdtl.getAdamt() + "", innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("OR")) {
                        totalded = totalded + aqdtl.getAdamt();
                        label = new Label(11, row, aqdtl.getAdamt() + "", innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("HBA")) {
                        totalded = totalded + aqdtl.getAdamt();
                        label = new Label(12, row, aqdtl.getAdamt() + "", innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("PT")) {
                        totalded = totalded + aqdtl.getAdamt();
                        label = new Label(13, row, aqdtl.getAdamt() + "", innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("GA")) {
                        totalded = totalded + aqdtl.getAdamt();
                        label = new Label(14, row, aqdtl.getAdamt() + "", innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("VE")) {
                        totalded = totalded + aqdtl.getAdamt();
                        label = new Label(15, row, aqdtl.getAdamt() + "", innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("SHBA")) {
                        totalded = totalded + aqdtl.getAdamt();
                        label = new Label(16, row, aqdtl.getAdamt() + "", innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("MOPA")) {
                        totalded = totalded + aqdtl.getAdamt();
                        label = new Label(17, row, aqdtl.getAdamt() + "", innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("GPDD")) {
                        totalded = totalded + aqdtl.getAdamt();
                        label = new Label(18, row, aqdtl.getAdamt() + "", innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("HC")) {
                        totalded = totalded + aqdtl.getAdamt();
                        label = new Label(19, row, aqdtl.getAdamt() + "", innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("LIC")) {
                        totalded = totalded + aqdtl.getAdamt();
                        label = new Label(20, row, aqdtl.getAdamt() + "", innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("PA")) {
                        totalded = totalded + aqdtl.getAdamt();
                        label = new Label(21, row, aqdtl.getAdamt() + "", innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("HIGL")) {
                        totalded = totalded + aqdtl.getAdamt();
                        label = new Label(22, row, aqdtl.getAdamt() + "", innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("LIGL")) {
                        totalded = totalded + aqdtl.getAdamt();
                        label = new Label(23, row, aqdtl.getAdamt() + "", innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("MIGL")) {
                        totalded = totalded + aqdtl.getAdamt();
                        label = new Label(24, row, aqdtl.getAdamt() + "", innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("MAL")) {
                        totalded = totalded + aqdtl.getAdamt();
                        label = new Label(25, row, aqdtl.getAdamt() + "", innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("GPIR")) {
                        totalded = totalded + aqdtl.getAdamt();
                        label = new Label(26, row, aqdtl.getAdamt() + "", innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("GIS")) {
                        totalded = totalded + aqdtl.getAdamt();
                        label = new Label(27, row, aqdtl.getAdamt() + "", innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("IT")) {
                        totalded = totalded + aqdtl.getAdamt();
                        label = new Label(28, row, aqdtl.getAdamt() + "", innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("WRR")) {
                        totalded = totalded + aqdtl.getAdamt();
                        label = new Label(29, row, aqdtl.getAdamt() + "", innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("GISA")) {
                        totalded = totalded + aqdtl.getAdamt();
                        label = new Label(30, row, aqdtl.getAdamt() + "", innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("VE")) {
                        totalded = totalded + aqdtl.getAdamt();
                        label = new Label(31, row, aqdtl.getAdamt() + "", innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("EP")) {
                        totalded = totalded + aqdtl.getAdamt();
                        label = new Label(32, row, aqdtl.getAdamt() + "", innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("SWR")) {
                        totalded = totalded + aqdtl.getAdamt();
                        label = new Label(33, row, aqdtl.getAdamt() + "", innercell);
                        sheet.addCell(label);
                    }
                }
                label = new Label(34, row, totalded + "", innercell);
                sheet.addCell(label);
                totalded = 0;
            }

            row = row + 2;
            label = new Label(6, row, "PRIVATE DEDUCTIONS", headcell);
            sheet.addCell(label);

            row++;

            label = new Label(0, row, "EMP NAME", headcell);//column,row
            sheet.addCell(label);
            //sheet.mergeCells(1,0,1,1);
            label = new Label(1, row, "GPF NO", headcell);//column,row
            sheet.addCell(label);
            //sheet.mergeCells(2,0,2,1);
            label = new Label(2, row, "POST", headcell);//column,row
            sheet.addCell(label);
            //sheet.mergeCells(3,0,3,1);
            label = new Label(3, row, "PAY SCALE", headcell);//column,row
            sheet.addCell(label);
            //sheet.mergeCells(4,0,4,1);
            label = new Label(4, row, "BASIC", headcell);//column,row
            sheet.addCell(label);
            //sheet.mergeCells(5,0,5,1);
            label = new Label(5, row, AqReportDAO.getADCodeDesc("ALBDL"), headcell);//column,row
            sheet.addCell(label);
            //sheet.mergeCells(6,0,6,1);
            label = new Label(6, row, AqReportDAO.getADCodeDesc("ASSCN"), headcell);//column,row
            sheet.addCell(label);
            //sheet.mergeCells(7,0,7,1);
            label = new Label(7, row, AqReportDAO.getADCodeDesc("BADN"), headcell);//column,row
            sheet.addCell(label);
            //sheet.mergeCells(8,0,8,1);
            label = new Label(8, row, AqReportDAO.getADCodeDesc("BANKLN"), headcell);//column,row
            sheet.addCell(label);
            //sheet.mergeCells(9,0,9,1);
            label = new Label(9, row, AqReportDAO.getADCodeDesc("CLUB"), headcell);//column,row
            sheet.addCell(label);
            //sheet.mergeCells(10,0,10,1);
            label = new Label(10, row, AqReportDAO.getADCodeDesc("CMRF"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(11, row, AqReportDAO.getADCodeDesc("CRFL"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(12, row, AqReportDAO.getADCodeDesc("DHE"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(13, row, AqReportDAO.getADCodeDesc("DTHREL"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(14, row, AqReportDAO.getADCodeDesc("ELCTRC"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(15, row, AqReportDAO.getADCodeDesc("FARD"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(16, row, AqReportDAO.getADCodeDesc("FC"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(17, row, AqReportDAO.getADCodeDesc("FL"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(18, row, AqReportDAO.getADCodeDesc("GOVTRC"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(19, row, AqReportDAO.getADCodeDesc("HFY"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(20, row, AqReportDAO.getADCodeDesc("MISDED"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(21, row, AqReportDAO.getADCodeDesc("NGBL"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(22, row, AqReportDAO.getADCodeDesc("OPW"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(23, row, AqReportDAO.getADCodeDesc("OPWLN"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(24, row, AqReportDAO.getADCodeDesc("OSCBL"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(25, row, AqReportDAO.getADCodeDesc("PAYSLP"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(26, row, AqReportDAO.getADCodeDesc("SBIKL"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(27, row, AqReportDAO.getADCodeDesc("SBIL"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(28, row, AqReportDAO.getADCodeDesc("SBILN"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(29, row, AqReportDAO.getADCodeDesc("SOCIL"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(30, row, AqReportDAO.getADCodeDesc("SPRTS"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(31, row, AqReportDAO.getADCodeDesc("SYNDC"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(32, row, AqReportDAO.getADCodeDesc("SYNDLN"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(33, row, AqReportDAO.getADCodeDesc("TEMPFND"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(34, row, AqReportDAO.getADCodeDesc("TEMPHRR"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(35, row, AqReportDAO.getADCodeDesc("UBI"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(36, row, AqReportDAO.getADCodeDesc("UCB"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(37, row, AqReportDAO.getADCodeDesc("UCOLN"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(38, row, AqReportDAO.getADCodeDesc("UWFND"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(39, row, AqReportDAO.getADCodeDesc("UWLN"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(40, row, AqReportDAO.getADCodeDesc("WC"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(41, row, AqReportDAO.getADCodeDesc("DHOBI"), headcell);//column,row
            sheet.addCell(label);
            label = new Label(42, row, "Total Pvt Deductions", headcell);//column,row
            sheet.addCell(label);

            acquaintanceList = AqReportDAO.getAcquaintance1(aqformbean.getBillNo(), aqDtlsTableName);
            for (int q = 0; q < acquaintanceList.size(); q++) {
                row++;
                row1++;
                AcquaintanceBean aqBean = (AcquaintanceBean) acquaintanceList.get(q);
                label = new Label(0, row, aqBean.getEmpname(), innercell);
                sheet.addCell(label);
                label = new Label(1, row, aqBean.getGpfaccno(), innercell);
                sheet.addCell(label);
                label = new Label(2, row, aqBean.getCurdesg(), innercell);
                sheet.addCell(label);
                label = new Label(3, row, aqBean.getPayscale(), innercell);
                sheet.addCell(label);
                label = new Label(4, row, aqBean.getCurbasic() + "", innercell);
                sheet.addCell(label);

                ArrayList aqdtlDed = AqReportDAO.getAcquaintanceDtlDed(aqBean.getEmpcode(), aqBean.getAqslno(),aqDtlsTableName);

                for (int k = 0; k < aqdtlDed.size(); k++) {
                    AcquaintanceDtlBean aqdtlded = (AcquaintanceDtlBean) aqdtlDed.get(k);
                    String adcode = aqdtlded.getAdcode();
                    if (adcode.equals("ALBDL")) {
                        totalpvtded = totalpvtded + aqdtlded.getAdamt();
                        label = new Label(5, row, aqdtlded.getAdamt() + "", innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("ASSCN")) {
                        totalpvtded = totalpvtded + aqdtlded.getAdamt();
                        label = new Label(6, row, aqdtlded.getAdamt() + "", innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("BADN")) {
                        totalpvtded = totalpvtded + aqdtlded.getAdamt();
                        label = new Label(7, row, aqdtlded.getAdamt() + "", innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("BANKLN")) {
                        totalpvtded = totalpvtded + aqdtlded.getAdamt();
                        label = new Label(8, row, aqdtlded.getAdamt() + "", innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("CLUB")) {
                        totalpvtded = totalpvtded + aqdtlded.getAdamt();
                        label = new Label(9, row, aqdtlded.getAdamt() + "", innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("CMRF")) {
                        totalpvtded = totalpvtded + aqdtlded.getAdamt();
                        label = new Label(10, row, aqdtlded.getAdamt() + "", innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("CRFL")) {
                        totalpvtded = totalpvtded + aqdtlded.getAdamt();
                        label = new Label(11, row, aqdtlded.getAdamt() + "", innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("DHE")) {
                        totalpvtded = totalpvtded + aqdtlded.getAdamt();
                        label = new Label(12, row, aqdtlded.getAdamt() + "", innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("DTHREL")) {
                        totalpvtded = totalpvtded + aqdtlded.getAdamt();
                        label = new Label(13, row, aqdtlded.getAdamt() + "", innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("ELCTRC")) {
                        totalpvtded = totalpvtded + aqdtlded.getAdamt();
                        label = new Label(14, row, aqdtlded.getAdamt() + "", innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("FARD")) {
                        totalpvtded = totalpvtded + aqdtlded.getAdamt();
                        label = new Label(15, row, aqdtlded.getAdamt() + "", innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("FC")) {
                        totalpvtded = totalpvtded + aqdtlded.getAdamt();
                        label = new Label(16, row, aqdtlded.getAdamt() + "", innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("FL")) {
                        totalpvtded = totalpvtded + aqdtlded.getAdamt();
                        label = new Label(17, row, aqdtlded.getAdamt() + "", innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("GOVTRC")) {
                        totalpvtded = totalpvtded + aqdtlded.getAdamt();
                        label = new Label(18, row, aqdtlded.getAdamt() + "", innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("HFY")) {
                        totalpvtded = totalpvtded + aqdtlded.getAdamt();
                        label = new Label(19, row, aqdtlded.getAdamt() + "", innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("MISDED")) {
                        totalpvtded = totalpvtded + aqdtlded.getAdamt();
                        label = new Label(20, row, aqdtlded.getAdamt() + "", innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("NGBL")) {
                        totalpvtded = totalpvtded + aqdtlded.getAdamt();
                        label = new Label(21, row, aqdtlded.getAdamt() + "", innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("OPW")) {
                        totalpvtded = totalpvtded + aqdtlded.getAdamt();
                        label = new Label(22, row, aqdtlded.getAdamt() + "", innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("OPWLN")) {
                        totalpvtded = totalpvtded + aqdtlded.getAdamt();
                        label = new Label(23, row, aqdtlded.getAdamt() + "", innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("OSCBL")) {
                        totalpvtded = totalpvtded + aqdtlded.getAdamt();
                        label = new Label(24, row, aqdtlded.getAdamt() + "", innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("PAYSLP")) {
                        totalpvtded = totalpvtded + aqdtlded.getAdamt();
                        label = new Label(25, row, aqdtlded.getAdamt() + "", innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("SBIKL")) {
                        totalpvtded = totalpvtded + aqdtlded.getAdamt();
                        label = new Label(26, row, aqdtlded.getAdamt() + "", innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("SBIL")) {
                        totalpvtded = totalpvtded + aqdtlded.getAdamt();
                        label = new Label(27, row, aqdtlded.getAdamt() + "", innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("SBILN")) {
                        totalpvtded = totalpvtded + aqdtlded.getAdamt();
                        label = new Label(28, row, aqdtlded.getAdamt() + "", innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("SOCIL")) {
                        totalpvtded = totalpvtded + aqdtlded.getAdamt();
                        label = new Label(29, row, aqdtlded.getAdamt() + "", innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("SPRTS")) {
                        totalpvtded = totalpvtded + aqdtlded.getAdamt();
                        label = new Label(30, row, aqdtlded.getAdamt() + "", innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("SYNDC")) {
                        totalpvtded = totalpvtded + aqdtlded.getAdamt();
                        label = new Label(31, row, aqdtlded.getAdamt() + "", innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("SYNDLN")) {
                        totalpvtded = totalpvtded + aqdtlded.getAdamt();
                        label = new Label(32, row, aqdtlded.getAdamt() + "", innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("TEMPFND")) {
                        totalpvtded = totalpvtded + aqdtlded.getAdamt();
                        label = new Label(33, row, aqdtlded.getAdamt() + "", innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("TEMPHRR")) {
                        totalpvtded = totalpvtded + aqdtlded.getAdamt();
                        label = new Label(34, row, aqdtlded.getAdamt() + "", innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("UBI")) {
                        totalpvtded = totalpvtded + aqdtlded.getAdamt();
                        label = new Label(35, row, aqdtlded.getAdamt() + "", innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("UCB")) {
                        totalpvtded = totalpvtded + aqdtlded.getAdamt();
                        label = new Label(36, row, aqdtlded.getAdamt() + "", innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("UCOLN")) {
                        totalpvtded = totalpvtded + aqdtlded.getAdamt();
                        label = new Label(37, row, aqdtlded.getAdamt() + "", innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("UWFND")) {
                        totalpvtded = totalpvtded + aqdtlded.getAdamt();
                        label = new Label(38, row, aqdtlded.getAdamt() + "", innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("UWLN")) {
                        totalpvtded = totalpvtded + aqdtlded.getAdamt();
                        label = new Label(39, row, aqdtlded.getAdamt() + "", innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("WC")) {
                        totalpvtded = totalpvtded + aqdtlded.getAdamt();
                        label = new Label(40, row, aqdtlded.getAdamt() + "", innercell);
                        sheet.addCell(label);
                    }
                    if (adcode.equals("DHOBI")) {
                        totalpvtded = totalpvtded + aqdtlded.getAdamt();
                        label = new Label(41, row, aqdtlded.getAdamt() + "", innercell);
                        sheet.addCell(label);
                    }
                }
                label = new Label(42, row, totalpvtded + "", innercell);
                sheet.addCell(label);
                totalpvtded = 0;
            }

            //Creating Second Sheet
            sheet = workbook.createSheet("Aquitance2", 1);

            sheet.mergeCells(0, 0, 0, 1);
            Label label2 = new Label(0, 0, "EMP NAME", headcell);//column,row
            sheet.addCell(label2);
            sheet.mergeCells(1, 0, 1, 1);
            label2 = new Label(1, 0, "GPF NO", headcell);//column,row
            sheet.addCell(label2);
            sheet.mergeCells(2, 0, 2, 1);
            label2 = new Label(2, 0, "POST", headcell);//column,row
            sheet.addCell(label2);
            sheet.mergeCells(3, 0, 3, 1);
            label2 = new Label(3, 0, "BANK_ACC_NO", headcell);//column,row
            sheet.addCell(label2);
            sheet.mergeCells(4, 0, 4, 1);
            label2 = new Label(4, 0, "PAY SCALE", headcell);//column,row
            sheet.addCell(label2);
            sheet.mergeCells(5, 0, 5, 1);
            label2 = new Label(5, 0, "BASIC", headcell);//column,row
            sheet.addCell(label2);
            sheet.mergeCells(6, 0, 6, 1);
            label2 = new Label(6, 0, "SPECIAL PAY", headcell);//column,row
            sheet.addCell(label2);
            sheet.mergeCells(7, 0, 7, 1);
            label2 = new Label(7, 0, "GRADE PAY", headcell);//column,row
            sheet.addCell(label2);
            sheet.mergeCells(8, 0, 8, 1);
            label2 = new Label(8, 0, "DP", headcell);//column,row
            sheet.addCell(label2);
            sheet.mergeCells(9, 0, 9, 1);
            label2 = new Label(9, 0, "DA", headcell);//column,row
            sheet.addCell(label2);
            sheet.mergeCells(10, 0, 10, 1);
            label2 = new Label(10, 0, "IR", headcell);//column,row
            sheet.addCell(label2);
            if (col6 == null) {
                sheet.mergeCells(11, 0, 11, 1);
                label2 = new Label(11, 0, "HRA", headcell);//column,row
                sheet.addCell(label2);
                col = 11;
            } else if (col6 != null && col6.length > 0) {
                
                col = 10;
                for (int i = 0; i < col6.length; i++) {
                    col = col + 1;
                    label2 = new Label(col, 1, AqReportDAO.getADCodeDesc(col6[i]), headcell);
                    sheet.addCell(label2);
                }
            }
            if (col7 != null && col7.length > 0) {
                sheet.mergeCells(11, 0, 18, 0);
                label2 = new Label(11, 0, "OTHER ALLOWANCE", headcell);//column,row
                sheet.addCell(label2);
                for (int i = 0; i < col7.length; i++) {
                    col = col + 1;
                    label2 = new Label(col, 1, AqReportDAO.getADCodeDesc(col7[i]), headcell);
                    sheet.addCell(label2);
                }
            } else {
                col = 11;
            }
            col = col + 1;
            label2 = new Label(col, 1, "GROSS", colorheadercell);//column,row
            sheet.addCell(label2);

            row = 0;

            //sheet.mergeCells(28,0,35,0);
            label2 = new Label(32, row, "DEDUCTIONS", headcell);
            sheet.addCell(label2);

            row = 1;

            col = col + 2;
            label2 = new Label(col, row, AqReportDAO.getADCodeDesc("GPF"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, AqReportDAO.getADCodeDesc("CPF"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, AqReportDAO.getADCodeDesc("TPF"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, AqReportDAO.getADCodeDesc("HRR"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, AqReportDAO.getADCodeDesc("FA"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, AqReportDAO.getADCodeDesc("MCA"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, AqReportDAO.getADCodeDesc("OR"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, AqReportDAO.getADCodeDesc("HBA"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, AqReportDAO.getADCodeDesc("PT"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, AqReportDAO.getADCodeDesc("GA"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, AqReportDAO.getADCodeDesc("VE"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, AqReportDAO.getADCodeDesc("SHBA"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, AqReportDAO.getADCodeDesc("MOPA"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, AqReportDAO.getADCodeDesc("GPDD"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, AqReportDAO.getADCodeDesc("HC"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, AqReportDAO.getADCodeDesc("LIC"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, AqReportDAO.getADCodeDesc("PA"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, AqReportDAO.getADCodeDesc("HIGL"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, AqReportDAO.getADCodeDesc("LIGL"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, AqReportDAO.getADCodeDesc("MIGL"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, AqReportDAO.getADCodeDesc("MAL"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, AqReportDAO.getADCodeDesc("GPIR"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, AqReportDAO.getADCodeDesc("GIS"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, AqReportDAO.getADCodeDesc("IT"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, AqReportDAO.getADCodeDesc("WRR"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, AqReportDAO.getADCodeDesc("GISA"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, AqReportDAO.getADCodeDesc("VE"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, AqReportDAO.getADCodeDesc("EP"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, AqReportDAO.getADCodeDesc("SWR"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, "Total Deductions", colorheadercell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, "Net Pay", colorheadercell);//column,row
            sheet.addCell(label2);
            //sheet.mergeCells(11,0,15,0);

            row = 0;
            col = col + 3;
            label2 = new Label(col, row, "PRIVATE DEDUCTIONS", headcell);
            sheet.addCell(label2);

            row = 1;

            //col = col;
            label2 = new Label(col, row, AqReportDAO.getADCodeDesc("ALBDL"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, AqReportDAO.getADCodeDesc("ASSCN"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, AqReportDAO.getADCodeDesc("BADN"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, AqReportDAO.getADCodeDesc("BANKLN"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, AqReportDAO.getADCodeDesc("CLUB"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, AqReportDAO.getADCodeDesc("CMRF"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, AqReportDAO.getADCodeDesc("CRFL"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, AqReportDAO.getADCodeDesc("DHE"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, AqReportDAO.getADCodeDesc("DTHREL"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, AqReportDAO.getADCodeDesc("ELCTRC"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, AqReportDAO.getADCodeDesc("FARD"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, AqReportDAO.getADCodeDesc("FC"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, AqReportDAO.getADCodeDesc("FL"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, AqReportDAO.getADCodeDesc("GOVTRC"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, AqReportDAO.getADCodeDesc("HFY"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, AqReportDAO.getADCodeDesc("MISDED"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, AqReportDAO.getADCodeDesc("NGBL"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, AqReportDAO.getADCodeDesc("OPW"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, AqReportDAO.getADCodeDesc("OPWLN"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, AqReportDAO.getADCodeDesc("OSCBL"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, AqReportDAO.getADCodeDesc("PAYSLP"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, AqReportDAO.getADCodeDesc("SBIKL"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, AqReportDAO.getADCodeDesc("SBIL"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, AqReportDAO.getADCodeDesc("SBILN"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, AqReportDAO.getADCodeDesc("SOCIL"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, AqReportDAO.getADCodeDesc("SPRTS"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, AqReportDAO.getADCodeDesc("SYNDC"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, AqReportDAO.getADCodeDesc("SYNDLN"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, AqReportDAO.getADCodeDesc("TEMPFND"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, AqReportDAO.getADCodeDesc("TEMPHRR"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, AqReportDAO.getADCodeDesc("UBI"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, AqReportDAO.getADCodeDesc("UCB"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, AqReportDAO.getADCodeDesc("UCOLN"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, AqReportDAO.getADCodeDesc("UWFND"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, AqReportDAO.getADCodeDesc("UWLN"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, AqReportDAO.getADCodeDesc("WC"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, AqReportDAO.getADCodeDesc("DHOBI"), headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, "Total Pvt Deductions", headcell);//column,row
            sheet.addCell(label2);
            col = col + 1;
            label2 = new Label(col, row, "Net Balance", headcell);//column,row
            sheet.addCell(label2);

            row = 1;
            row1 = 1;

            for (int q = 0; q < acquaintanceList.size(); q++) {
                row++;
                row1++;
                AcquaintanceBean aqBean = (AcquaintanceBean) acquaintanceList.get(q);
                label2 = new Label(0, row, aqBean.getEmpname(), innercell);
                sheet.addCell(label2);
                label2 = new Label(1, row, aqBean.getGpfaccno(), innercell);
                sheet.addCell(label2);
                label2 = new Label(2, row, aqBean.getCurdesg(), innercell);
                sheet.addCell(label2);
                label2 = new Label(3, row, aqBean.getBankaccno(), innercell);
                sheet.addCell(label2);
                label2 = new Label(4, row, aqBean.getPayscale(), innercell);
                sheet.addCell(label2);
                label2 = new Label(5, row, aqBean.getCurbasic() + "", innercell);
                sheet.addCell(label2);
                gross = gross + aqBean.getCurbasic();

                ArrayList aqdtlList = AqReportDAO.getAcquaintanceDtl(aqBean.getAqslno(),aqDtlsTableName);
                boolean insideWhile = false;
                for (int k = 0; k < aqdtlList.size(); k++) {
                    AcquaintanceDtlBean aqdtl = (AcquaintanceDtlBean) aqdtlList.get(k);
                    String adcode = aqdtl.getAdcode();
                    insideWhile = true;
                    repcol = aqdtl.getRepcol();
                    if (adcode.equals("SP")) {
                        gross = gross + aqdtl.getAdamt();
                        label2 = new Label(6, row, aqdtl.getAdamt() + "", innercell);
                        sheet.addCell(label2);
                    }
                    if (adcode.equals("GP")) {
                        gross = gross + aqdtl.getAdamt();
                        label2 = new Label(7, row, aqdtl.getAdamt() + "", innercell);
                        sheet.addCell(label2);
                    }
                    if (adcode.equals("DP")) {
                        gross = gross + aqdtl.getAdamt();
                        label2 = new Label(8, row, aqdtl.getAdamt() + "", innercell);
                        sheet.addCell(label2);
                    }

                    if (adcode.equals("DA")) {
                        gross = gross + aqdtl.getAdamt();
                        label2 = new Label(9, row, aqdtl.getAdamt() + "", innercell);
                        sheet.addCell(label2);
                    }
                    if (adcode.equals("IR")) {
                        gross = gross + aqdtl.getAdamt();
                        label2 = new Label(10, row, aqdtl.getAdamt() + "", innercell);
                        sheet.addCell(label2);
                    }
                    if (col6 == null) {
                        if (adcode.equals("HRA")) {
                            gross = gross + aqdtl.getAdamt();
                            label2 = new Label(11, row, aqdtl.getAdamt() + "", innercell);
                            sheet.addCell(label2);
                        }
                        col = 11;
                    } else if (col6 != null && col6.length > 0) {
                        col = 10;
                        for (int i = 0; i < col6.length; i++) {
                            col = col + 1;
                            if (repcol == 6) {
                                if (adcode.equals(col6[i])) {
                                    gross = gross + aqdtl.getAdamt();
                                    label2 = new Label(col, row, aqdtl.getAdamt() + "", innercell);
                                    sheet.addCell(label2);
                                }
                            }
                        }
                    }
                    //col = col + 1;

                    if (col7 != null && col7.length > 0) {
                        for (int j = 0; j < col7.length; j++) {
                            col = col + 1;
                            if (repcol == 7) {
                                if (adcode.equals(col7[j])) {
                                    gross = gross + aqdtl.getAdamt();
                                    label2 = new Label(col, row, aqdtl.getAdamt() + "", innercell);
                                    sheet.addCell(label2);
                                }
                            }
                        }
                    } else {
                        col = 11;
                    }
                }
                if (insideWhile == false) {
                    col = 10;
                    if (col6 != null && col6.length > 0) {
                        for (int j = 0; j < col6.length; j++) {
                            col = col + 1;
                        }
                    }
                    if (col7 != null && col7.length > 0) {
                        for (int j = 0; j < col7.length; j++) {
                            col = col + 1;
                        }
                    }
                }
                col = col + 1;
                grosstotal = gross;

                label2 = new Label(col, row, "" + gross, colorcell);
                sheet.addCell(label2);
                gross = 0;

                int prevcol = col + 1;
                col = col + 1;
                boolean insideDedWhile = false;
                
                
                ArrayList aqdtlList1 = AqReportDAO.getAcquaintanceDtl(aqBean.getAqslno(),aqDtlsTableName);

                for (int k = 0; k < aqdtlList1.size(); k++) {
                    insideDedWhile = true;
                    AcquaintanceDtlBean aqdtl = (AcquaintanceDtlBean) aqdtlList.get(k);
                    String adcode = aqdtl.getAdcode();
                    col = prevcol;
                    col = col + 1;
                    if (adcode.equals("GPF")) {
                        totalded = totalded + aqdtl.getAdamt();
                        label2 = new Label(col, row, aqdtl.getAdamt() + "", innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("CPF")) {
                        totalded = totalded + aqdtl.getAdamt();
                        label2 = new Label(col, row, aqdtl.getAdamt() + "", innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("TPF")) {
                        totalded = totalded + aqdtl.getAdamt();
                        label2 = new Label(col, row, aqdtl.getAdamt() + "", innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("HRR")) {
                        totalded = totalded + aqdtl.getAdamt();
                        label2 = new Label(col, row, aqdtl.getAdamt() + "", innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("FA")) {
                        totalded = totalded + aqdtl.getAdamt();
                        label2 = new Label(col, row, aqdtl.getAdamt() + "", innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("MCA")) {
                        totalded = totalded + aqdtl.getAdamt();
                        label2 = new Label(col, row, aqdtl.getAdamt() + "", innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("OR")) {
                        totalded = totalded + aqdtl.getAdamt();
                        label2 = new Label(col, row, aqdtl.getAdamt() + "", innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("HBA")) {
                        totalded = totalded + aqdtl.getAdamt();
                        label2 = new Label(col, row, aqdtl.getAdamt() + "", innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("PT")) {
                        totalded = totalded + aqdtl.getAdamt();
                        label2 = new Label(col, row, aqdtl.getAdamt() + "", innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("GA")) {
                        totalded = totalded + aqdtl.getAdamt();
                        label2 = new Label(col, row, aqdtl.getAdamt() + "", innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("VE")) {
                        totalded = totalded + aqdtl.getAdamt();
                        label2 = new Label(col, row, aqdtl.getAdamt() + "", innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("SHBA")) {
                        totalded = totalded + aqdtl.getAdamt();
                        label2 = new Label(col, row, aqdtl.getAdamt() + "", innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("MOPA")) {
                        totalded = totalded + aqdtl.getAdamt();
                        label2 = new Label(col, row, aqdtl.getAdamt() + "", innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("GPDD")) {
                        totalded = totalded + aqdtl.getAdamt();
                        label2 = new Label(col, row, aqdtl.getAdamt() + "", innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("HC")) {
                        totalded = totalded + aqdtl.getAdamt();
                        label2 = new Label(col, row, aqdtl.getAdamt() + "", innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("LIC")) {
                        totalded = totalded + aqdtl.getAdamt();
                        label2 = new Label(col, row, aqdtl.getAdamt() + "", innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("PA")) {
                        totalded = totalded + aqdtl.getAdamt();
                        label2 = new Label(col, row, aqdtl.getAdamt() + "", innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("HIGL")) {
                        totalded = totalded + aqdtl.getAdamt();
                        label2 = new Label(col, row, aqdtl.getAdamt() + "", innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("LIGL")) {
                        totalded = totalded + aqdtl.getAdamt();
                        label2 = new Label(col, row, aqdtl.getAdamt() + "", innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("MIGL")) {
                        totalded = totalded + aqdtl.getAdamt();
                        label2 = new Label(col, row, aqdtl.getAdamt() + "", innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("MAL")) {
                        totalded = totalded + aqdtl.getAdamt();
                        label2 = new Label(col, row, aqdtl.getAdamt() + "", innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("GPIR")) {
                        totalded = totalded + aqdtl.getAdamt();
                        label2 = new Label(col, row, aqdtl.getAdamt() + "", innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("GIS")) {
                        totalded = totalded + aqdtl.getAdamt();
                        label2 = new Label(col, row, aqdtl.getAdamt() + "", innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("IT")) {
                        totalded = totalded + aqdtl.getAdamt();
                        label2 = new Label(col, row, aqdtl.getAdamt() + "", innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("WRR")) {
                        totalded = totalded + aqdtl.getAdamt();
                        label2 = new Label(col, row, aqdtl.getAdamt() + "", innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("GISA")) {
                        totalded = totalded + aqdtl.getAdamt();
                        label2 = new Label(col, row, aqdtl.getAdamt() + "", innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("VE")) {
                        totalded = totalded + aqdtl.getAdamt();
                        label2 = new Label(col, row, aqdtl.getAdamt() + "", innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("EP")) {
                        totalded = totalded + aqdtl.getAdamt();
                        label2 = new Label(col, row, aqdtl.getAdamt() + "", innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("SWR")) {
                        totalded = totalded + aqdtl.getAdamt();
                        label2 = new Label(col, row, aqdtl.getAdamt() + "", innercell);
                        sheet.addCell(label2);
                    }
                }
                if (insideDedWhile == false) {
                    col = col + 29;
                }
                
                netpay = grosstotal - totalded;
                
                col = col + 1;
                label2 = new Label(col, row, totalded + "", colorcell);
                sheet.addCell(label2);
                totalded = 0;
                col = col + 1;
                label2 = new Label(col, row, netpay + "", colorcell);
                sheet.addCell(label2);

                ArrayList aqdtlDed = AqReportDAO.getAcquaintanceDtlDed(aqBean.getEmpcode(), aqBean.getAqslno(),aqDtlsTableName);

                boolean insidePvtDedWhile = false;
                int prevpvtcol = col + 2;
                
                //col = prevpvtcol;
                for (int k = 0; k < aqdtlDed.size(); k++) {
                    AcquaintanceDtlBean aqdtlded = (AcquaintanceDtlBean) aqdtlDed.get(k);
                    insidePvtDedWhile = true;
                    String adcode = aqdtlded.getAdcode();
                    col = prevpvtcol;
                    col = col + 1;
                    
                    if (adcode.equals("ALBDL")) {
                        totalpvtded = totalpvtded + aqdtlded.getAdamt();
                        label2 = new Label(col, row, aqdtlded.getAdamt() + "", innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("ASSCN")) {
                        totalpvtded = totalpvtded + aqdtlded.getAdamt();
                        label2 = new Label(col, row, aqdtlded.getAdamt() + "", innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("BADN")) {
                        totalpvtded = totalpvtded + aqdtlded.getAdamt();
                        label2 = new Label(col, row, aqdtlded.getAdamt() + "", innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("BANKLN")) {
                        totalpvtded = totalpvtded + aqdtlded.getAdamt();
                        label2 = new Label(col, row, aqdtlded.getAdamt() + "", innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("CLUB")) {
                        totalpvtded = totalpvtded + aqdtlded.getAdamt();
                        label2 = new Label(col, row, aqdtlded.getAdamt() + "", innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("CMRF")) {
                        totalpvtded = totalpvtded + aqdtlded.getAdamt();
                        label2 = new Label(col, row, aqdtlded.getAdamt() + "", innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("CRFL")) {
                        totalpvtded = totalpvtded + aqdtlded.getAdamt();
                        label2 = new Label(col, row, aqdtlded.getAdamt() + "", innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("DHE")) {
                        totalpvtded = totalpvtded + aqdtlded.getAdamt();
                        label2 = new Label(col, row, aqdtlded.getAdamt() + "", innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("DTHREL")) {
                        totalpvtded = totalpvtded + aqdtlded.getAdamt();
                        label2 = new Label(col, row, aqdtlded.getAdamt() + "", innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("ELCTRC")) {
                        totalpvtded = totalpvtded + aqdtlded.getAdamt();
                        label2 = new Label(col, row, aqdtlded.getAdamt() + "", innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("FARD")) {
                        totalpvtded = totalpvtded + aqdtlded.getAdamt();
                        label2 = new Label(col, row, aqdtlded.getAdamt() + "", innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("FC")) {
                        totalpvtded = totalpvtded + aqdtlded.getAdamt();
                        label2 = new Label(col, row, aqdtlded.getAdamt() + "", innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("FL")) {
                        totalpvtded = totalpvtded + aqdtlded.getAdamt();
                        label2 = new Label(col, row, aqdtlded.getAdamt() + "", innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("GOVTRC")) {
                        totalpvtded = totalpvtded + aqdtlded.getAdamt();
                        label2 = new Label(col, row, aqdtlded.getAdamt() + "", innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("HFY")) {
                        totalpvtded = totalpvtded + aqdtlded.getAdamt();
                        label2 = new Label(col, row, aqdtlded.getAdamt() + "", innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("MISDED")) {
                        totalpvtded = totalpvtded + aqdtlded.getAdamt();
                        label2 = new Label(col, row, aqdtlded.getAdamt() + "", innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("NGBL")) {
                        totalpvtded = totalpvtded + aqdtlded.getAdamt();
                        label2 = new Label(col, row, aqdtlded.getAdamt() + "", innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("OPW")) {
                        totalpvtded = totalpvtded + aqdtlded.getAdamt();
                        label2 = new Label(col, row, aqdtlded.getAdamt() + "", innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("OPWLN")) {
                        totalpvtded = totalpvtded + aqdtlded.getAdamt();
                        label2 = new Label(col, row, aqdtlded.getAdamt() + "", innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("OSCBL")) {
                        totalpvtded = totalpvtded + aqdtlded.getAdamt();
                        label2 = new Label(col, row, aqdtlded.getAdamt() + "", innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("PAYSLP")) {
                        totalpvtded = totalpvtded + aqdtlded.getAdamt();
                        label2 = new Label(col, row, aqdtlded.getAdamt() + "", innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("SBIKL")) {
                        totalpvtded = totalpvtded + aqdtlded.getAdamt();
                        label2 = new Label(col, row, aqdtlded.getAdamt() + "", innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("SBIL")) {
                        totalpvtded = totalpvtded + aqdtlded.getAdamt();
                        label2 = new Label(col, row, aqdtlded.getAdamt() + "", innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("SBILN")) {
                        totalpvtded = totalpvtded + aqdtlded.getAdamt();
                        label2 = new Label(col, row, aqdtlded.getAdamt() + "", innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("SOCIL")) {
                        totalpvtded = totalpvtded + aqdtlded.getAdamt();
                        label2 = new Label(col, row, aqdtlded.getAdamt() + "", innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("SPRTS")) {
                        totalpvtded = totalpvtded + aqdtlded.getAdamt();
                        label2 = new Label(col, row, aqdtlded.getAdamt() + "", innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("SYNDC")) {
                        totalpvtded = totalpvtded + aqdtlded.getAdamt();
                        label2 = new Label(col, row, aqdtlded.getAdamt() + "", innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("SYNDLN")) {
                        totalpvtded = totalpvtded + aqdtlded.getAdamt();
                        label2 = new Label(col, row, aqdtlded.getAdamt() + "", innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("TEMPFND")) {
                        totalpvtded = totalpvtded + aqdtlded.getAdamt();
                        label2 = new Label(col, row, aqdtlded.getAdamt() + "", innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("TEMPHRR")) {
                        totalpvtded = totalpvtded + aqdtlded.getAdamt();
                        label2 = new Label(col, row, aqdtlded.getAdamt() + "", innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("UBI")) {
                        totalpvtded = totalpvtded + aqdtlded.getAdamt();
                        label2 = new Label(col, row, aqdtlded.getAdamt() + "", innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("UCB")) {
                        totalpvtded = totalpvtded + aqdtlded.getAdamt();
                        label2 = new Label(col, row, aqdtlded.getAdamt() + "", innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("UCOLN")) {
                        totalpvtded = totalpvtded + aqdtlded.getAdamt();
                        label2 = new Label(col, row, aqdtlded.getAdamt() + "", innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("UWFND")) {
                        totalpvtded = totalpvtded + aqdtlded.getAdamt();
                        label2 = new Label(col, row, aqdtlded.getAdamt() + "", innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("UWLN")) {
                        totalpvtded = totalpvtded + aqdtlded.getAdamt();
                        label2 = new Label(col, row, aqdtlded.getAdamt() + "", innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("WC")) {
                        totalpvtded = totalpvtded + aqdtlded.getAdamt();
                        label2 = new Label(col, row, aqdtlded.getAdamt() + "", innercell);
                        sheet.addCell(label2);
                    }
                    col = col + 1;
                    if (adcode.equals("DHOBI")) {
                        totalpvtded = totalpvtded + aqdtlded.getAdamt();
                        label2 = new Label(col, row, aqdtlded.getAdamt() + "", innercell);
                        sheet.addCell(label2);
                    }
                }
                if (insidePvtDedWhile == false) {
                    col = col + 39;
                }
                col = col + 1;
                balance = netpay - totalpvtded;
                label2 = new Label(col, row, totalpvtded + "", innercell);
                sheet.addCell(label2);
                col = col + 1;
                label2 = new Label(col, row, balance + "", innercell);
                sheet.addCell(label2);
                totalpvtded = 0;
            }
            response.setHeader("Content-Disposition", "attachment; filename=\"Aquitancereport.xls\"");
            workbook.write();
            workbook.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
