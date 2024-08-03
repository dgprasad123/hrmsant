package hrms.dao.payroll.arrear;

import hrms.common.DataBaseFunctions;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.annotation.Resource;
import javax.sql.DataSource;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.NumberFormats;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.Number;

public class BeneficiaryListArrearDAOImpl implements BeneficiaryListArrearDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void downloadBeneficiaryListArrearExcel(OutputStream out, String offcode, WritableWorkbook workbook, String billNo) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            con = this.dataSource.getConnection();

            WritableSheet sheet = workbook.createSheet(offcode, 0);
            WritableFont headformat = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
            WritableCellFormat headcell3 = new WritableCellFormat(headformat);
            headcell3.setAlignment(Alignment.CENTRE);
            headcell3.setVerticalAlignment(VerticalAlignment.CENTRE);
            headcell3.setWrap(true);

            WritableCellFormat headcell = new WritableCellFormat(headformat);
            headcell.setAlignment(Alignment.CENTRE);
            headcell.setVerticalAlignment(VerticalAlignment.CENTRE);
            headcell.setWrap(true);
            headcell.setBorder(Border.ALL, BorderLineStyle.DOUBLE);

            WritableCellFormat innercell = new WritableCellFormat(NumberFormats.INTEGER);
            innercell.setAlignment(Alignment.CENTRE);
            innercell.setVerticalAlignment(VerticalAlignment.CENTRE);
            innercell.setWrap(true);

            int col = 0;
            int widthInChars = 16;
            
            Label label = null;
            Number num = null;
            
            label = new Label(0, 0, "BENF_ACCT_NO", headcell);//column,row
            sheet.setColumnView(col, widthInChars);
            col++;
            widthInChars = 23;
            sheet.addCell(label);
            label = new Label(1, 0, "BENF_BANK_IFSC_CODE", headcell);//column,row
            sheet.setColumnView(col, widthInChars);
            col++;
            widthInChars = 15;
            sheet.addCell(label);
            label = new Label(2, 0, "AMOUNT", headcell);//column,row
            sheet.setColumnView(col, widthInChars);
            col++;
            widthInChars = 16;
            sheet.addCell(label);
            label = new Label(3, 0, "BENF_FLAG", headcell);//column,row
            sheet.setColumnView(col, widthInChars);
            col++;
            widthInChars = 33;
            sheet.addCell(label);

            int row = 0;
            int slno = 0;

            String sql = "select arr_mast.bank_acc_no,emp_mast.bank_code,emp_mast.branch_code,arr_mast.ifsc_code,arrear_pay,inctax,cpf_head,pt from arr_mast"
                    + " inner join emp_mast on arr_mast.emp_id=emp_mast.emp_id"
                    + " left outer join g_branch on emp_mast.branch_code=g_branch.branch_code where bill_no=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(billNo));
            rs = pst.executeQuery();
            while (rs.next()) {
                int gross = rs.getInt("arrear_pay");
                int dedAmt = rs.getInt("inctax") + rs.getInt("cpf_head") + rs.getInt("pt");
                int net = gross - dedAmt;
                
                slno += 1;
                row += 1;
                
                label = new Label(0, row, rs.getString("bank_acc_no"), innercell);//column,row
                sheet.setColumnView(col, widthInChars);
                col++;
                widthInChars = 23;
                sheet.addCell(label);
                label = new Label(1, row, rs.getString("ifsc_code"), innercell);//column,row
                sheet.setColumnView(col, widthInChars);
                col++;
                widthInChars = 15;
                sheet.addCell(label);
                num = new Number(2, row, net, innercell);//column,row
                sheet.setColumnView(col, widthInChars);
                col++;
                widthInChars = 16;
                sheet.addCell(num);
                label = new Label(3, row, "Employee", innercell);//column,row
                sheet.setColumnView(col, widthInChars);
                col++;
                widthInChars = 33;
                sheet.addCell(label);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }
}
