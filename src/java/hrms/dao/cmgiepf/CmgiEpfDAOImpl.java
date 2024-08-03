package hrms.dao.cmgiepf;

import hrms.common.DataBaseFunctions;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.annotation.Resource;
import javax.sql.DataSource;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.Number;

public class CmgiEpfDAOImpl implements CmgiEpfDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public WritableWorkbook downloadEPFExcel(OutputStream out) throws IOException {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        int row = 0;

        WritableWorkbook workbook = Workbook.createWorkbook(out);
        try {
            con = this.dataSource.getConnection();

            WritableSheet sheet = workbook.createSheet("OLSGAD0010001", 0);

            WritableFont headformat = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);

            WritableCellFormat headcell = new WritableCellFormat(headformat);
            headcell.setAlignment(Alignment.CENTRE);
            headcell.setVerticalAlignment(VerticalAlignment.CENTRE);
            headcell.setWrap(true);
            
            WritableFont innerformat = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD);

            WritableCellFormat innercell = new WritableCellFormat(innerformat);

            Label label = new Label(1, row++, "CMGI EPF REPORT", headcell);//column,row
            sheet.addCell(label);
            sheet.mergeCells(1, 0, 10, 0);

            Number num = null;
            
            row += 1;
            
            label = new Label(0, row, "SL No", headcell);
            sheet.addCell(label);
            label = new Label(1, row, "UAN", headcell);
            sheet.addCell(label);
            label = new Label(2, row, "Member Name", headcell);
            sheet.addCell(label);
            label = new Label(3, row, "Gross Wages", headcell);
            sheet.addCell(label);
            label = new Label(4, row, "Gross Wages", headcell);
            sheet.addCell(label);
            label = new Label(5, row, "EPF Wages", headcell);
            sheet.addCell(label);
            label = new Label(6, row, "EPS Wages", headcell);
            sheet.addCell(label);
            label = new Label(7, row, "EPF Contribution remitted", headcell);
            sheet.addCell(label);
            label = new Label(8, row, "EPS Contribution remitted", headcell);
            sheet.addCell(label);
            label = new Label(9, row, "EPF and EPS Diff remitted", headcell);
            sheet.addCell(label);
            label = new Label(10, row, "NCP Days", headcell);
            sheet.addCell(label);
            label = new Label(11, row, "Refund of Advances", headcell);
            sheet.addCell(label);

            String sql = "select f_name,gpf_no,cur_basic_salary,fixedvalue from emp_mast"
                    + " left outer join update_ad_info on emp_mast.emp_id=update_ad_info.updation_ref_code where cur_off_code='OLSGAD0010001' ORDER BY F_NAME";
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            int slno = 0;
            while (rs.next()) {
                row += 1;
                slno += 1;
                
                num = new Number(0, row, slno, innercell);
                sheet.addCell(num);
                label = new Label(1, row, rs.getString("gpf_no"), innercell);
                sheet.addCell(label);
                sheet.setColumnView(1, 20);
                label = new Label(2, row, rs.getString("f_name"), innercell);
                sheet.addCell(label);
                sheet.setColumnView(2, 25);
                num = new Number(3, row, rs.getDouble("cur_basic_salary"), innercell);
                sheet.addCell(num);
                num = new Number(4, row, 1500, innercell);
                sheet.addCell(num);
                num = new Number(5, row, rs.getInt("fixedvalue"), innercell);
                sheet.addCell(num);
                label = new Label(6, row, "", innercell);
                sheet.addCell(label);
                label = new Label(7, row, "", innercell);
                sheet.addCell(label);
                label = new Label(8, row, "", innercell);
                sheet.addCell(label);
                label = new Label(9, row, "", innercell);
                sheet.addCell(label);
                label = new Label(10, row, "", innercell);
                sheet.addCell(label);
                label = new Label(11, row, "", innercell);
                sheet.addCell(label);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return workbook;
    }

}
