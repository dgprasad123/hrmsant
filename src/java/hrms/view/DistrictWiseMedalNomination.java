/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.view;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.springframework.web.servlet.view.document.AbstractExcelView;

/**
 *
 * @author Manas
 */
public class DistrictWiseMedalNomination extends AbstractExcelView {

    @Override
    protected void buildExcelDocument(Map<String, Object> model,
            HSSFWorkbook workbook, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        response.setHeader("Content-Disposition", "attachment; filename=\"DistrictWiseMedalNominationList.xls\"");
        HSSFSheet sheet = workbook.createSheet("Nomination List");
        String[] headers = (String[]) model.get("headers");
        List data = (List) model.get("data");
        CellStyle style = workbook.createCellStyle();
        org.apache.poi.ss.usermodel.Font font = workbook.createFont();
        font.setFontName("Arial");
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        style.setFont(font);

        HSSFRow header = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            header.createCell(i).setCellValue(headers[i]);
            header.getCell(i).setCellStyle(style);
        }
        for (int i = 0; i < data.size(); i++) {
            HSSFRow dataRow = sheet.createRow(i + 1);
            List rowData = (List)data.get(i);
            for (int j = 0; j < rowData.size(); j++) {
                dataRow.createCell(j).setCellValue((String)rowData.get(j));
            }
        }
    }
}
