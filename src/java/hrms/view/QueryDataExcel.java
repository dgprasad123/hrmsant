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
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;

import org.springframework.web.servlet.view.document.AbstractExcelView;

/**
 *
 * @author SURAJ
 */
public class QueryDataExcel extends AbstractExcelView {

    @Override
    protected void buildExcelDocument(Map<String, Object> model,
            HSSFWorkbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<List> dataLists = (List) model.get("datalist");
        response.setHeader("Content-Disposition", "attachment; filename=\"QueryData.xls\"");
        HSSFSheet sheet = workbook.createSheet("Data");

        /*Column Header Style*/
        CellStyle style = workbook.createCellStyle();
        org.apache.poi.ss.usermodel.Font font = workbook.createFont();
        font.setFontName("Arial");
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        style.setFont(font);
        /*Column Header Style*/
        int row = 0;
        for (List dataList : dataLists) {
            if (row == 0) {
                int column = 0;
                HSSFRow header = sheet.createRow(row);
                for (Object data : dataList) {                    
                    header.createCell(column).setCellValue(StringUtils.defaultString(data.toString()));
                    header.getCell(column).setCellStyle(style);
                    column++;
                }
                row++;
            }else{
                int column = 0;
                HSSFRow header = sheet.createRow(row);
                for (Object data : dataList) {                    
                    String dataString = "";
                    if(data != null){
                        dataString = data.toString();
                    }
                    header.createCell(column).setCellValue(dataString);                    
                    column++;
                }
                row++;
            }
        }
    }

}
