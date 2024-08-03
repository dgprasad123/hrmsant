package hrms.dao.report.districtwiseemployeedata;

import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class DistrictWiseEmployeeDataDAOImpl implements DistrictWiseEmployeeDataDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void downloadDistrictWiseEmployeeDataExcel(HttpServletResponse response, String distCode) {

        Connection con = null;

        PreparedStatement pst1 = null;
        ResultSet rs1 = null;
        PreparedStatement pst2 = null;
        ResultSet rs2 = null;

        String distName = "";
        int rowNum = 0;
        int slno = 0;
        try {
            con = this.dataSource.getConnection();

            pst1 = con.prepareStatement("select dist_name from g_district where dist_code=?");
            pst1.setString(1, distCode);
            rs1 = pst1.executeQuery();
            if (rs1.next()) {
                distName = rs1.getString("dist_name");
            }
            System.out.println("distName:" + distName);
            String sql2 = "select tab.* from (   \n"
                    + "select emp_mast.emp_id,gpf_no,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') FULL_NAME,  \n"
                    + "f_name,gender,cur_spc,post spn,doe_gov,dos,post_grp_type,email_id,mobile,cur_salary,cur_basic_salary,off_en,\n"
                    + "eelectionid.id_no voterId,eaadhar.id_no aadhar,matrix_cell,matrix_level,\n"
                    + "(select ARRAY_TO_STRING(ARRAY[address, bl_name, g_district.dist_name], ' ') dist_name \n"
                    + " from emp_address  \n"
                    + "left outer join g_district on emp_address.dist_code=g_district.dist_code  \n"
                    + "left outer join g_block on emp_address.bl_code=g_block.bl_code::text  \n"
                    + "where emp_id=emp_mast.emp_id and address_type='PERMANENT'  \n"
                    + "order by address_id   \n"
                    + "desc limit 1 ) dist_name, g_cadre.cadre_name, post_grp_type, emp_mast.category  from emp_mast   \n"
                    + "left outer join g_office on emp_mast.cur_off_code=g_office.off_code   \n"
                    + "left outer join g_spc on emp_mast.cur_spc=g_spc.spc\n"
                    + "left outer join g_post on g_spc.gpc=g_post.post_code \n"
                    + "left outer join (select * from emp_id_doc where id_description='ELECTION ID CARD')eelectionid on emp_mast.emp_id=eelectionid.emp_id  \n"
                    + "left outer join (select * from emp_id_doc where id_description='AADHAAR')eaadhar on emp_mast.emp_id=eaadhar.emp_id  \n"
                    + "left outer join g_cadre on emp_mast.cur_cadre_code=g_cadre.cadre_code  \n"
                    + "where g_office.dist_code =? and dep_code in ('01','02','03','04','05','06','07','11') ) tab   \n"
                    + "order by off_en, f_name ";
            pst2 = con.prepareStatement(sql2);

            OutputStream out = response.getOutputStream();
            Workbook workbook = new XSSFWorkbook();
            CreationHelper createHelper = workbook.getCreationHelper();
            Sheet sheet = workbook.createSheet("Employee");

            Font headerFont = workbook.createFont();
            //headerFont.setBoldweight(true);
            headerFont.setFontHeightInPoints((short) 14);
            headerFont.setColor(IndexedColors.RED.getIndex());

            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFont(headerFont);

            Row headerRow = sheet.createRow(0);

            Cell cell = headerRow.createCell(0);
            cell.setCellValue("Sl No");
            cell.setCellStyle(headerCellStyle);

            cell = headerRow.createCell(1);
            cell.setCellValue("Name");
            cell.setCellStyle(headerCellStyle);

            cell = headerRow.createCell(2);
            cell.setCellValue("Sex(M/F)");
            cell.setCellStyle(headerCellStyle);

            cell = headerRow.createCell(3);
            cell.setCellValue("Designation");
            cell.setCellStyle(headerCellStyle);

            cell = headerRow.createCell(4);
            cell.setCellValue("Date from which Serving in the District");
            cell.setCellStyle(headerCellStyle);

            cell = headerRow.createCell(5);
            cell.setCellValue("Date of Superannuation");
            cell.setCellStyle(headerCellStyle);

            cell = headerRow.createCell(6);
            cell.setCellValue("Category(Group - A,B,C,D)");
            cell.setCellStyle(headerCellStyle);

            cell = headerRow.createCell(7);
            cell.setCellValue("Email ID/Cell Phone No.");
            cell.setCellStyle(headerCellStyle);

            cell = headerRow.createCell(8);
            cell.setCellValue("Pay Band Level Applicable");
            cell.setCellStyle(headerCellStyle);

            /*cell = headerRow.createCell(9);
             cell.setCellValue("Pay Scale");
             cell.setCellStyle(headerCellStyle);*/
            cell = headerRow.createCell(9);
            cell.setCellValue("Present Basic Pay/Consolidated Remuneration");
            cell.setCellStyle(headerCellStyle);

            cell = headerRow.createCell(10);
            cell.setCellValue("Details of the Posting with and address thereof");
            cell.setCellStyle(headerCellStyle);

            cell = headerRow.createCell(11);
            cell.setCellValue("Home District with address of the Employee");
            cell.setCellStyle(headerCellStyle);

            cell = headerRow.createCell(12);
            cell.setCellValue("Where posted for 3 years or more in the last four years in the same District(Yes/No)");
            cell.setCellStyle(headerCellStyle);

            cell = headerRow.createCell(13);
            cell.setCellValue("AC of which permanent Resident");
            cell.setCellStyle(headerCellStyle);

            cell = headerRow.createCell(14);
            cell.setCellValue("AC in which working");
            cell.setCellStyle(headerCellStyle);

            cell = headerRow.createCell(15);
            cell.setCellValue("AC in which residing");
            cell.setCellStyle(headerCellStyle);

            cell = headerRow.createCell(16);
            cell.setCellValue("AC in which registered as a Voter");
            cell.setCellStyle(headerCellStyle);

            cell = headerRow.createCell(17);
            cell.setCellValue("EPIC No.(Voter ID Card No.)");
            cell.setCellStyle(headerCellStyle);

            cell = headerRow.createCell(18);
            cell.setCellValue("Aadhar No.");
            cell.setCellStyle(headerCellStyle);

            cell = headerRow.createCell(19);
            cell.setCellValue("Cadre");
            cell.setCellStyle(headerCellStyle);

            rowNum += 1;

            pst2.setString(1, distCode);
            rs2 = pst2.executeQuery();
            while (rs2.next()) {
                rowNum += 1;
                slno += 1;

                Row row = sheet.createRow(rowNum);

                row.createCell(0).setCellValue(slno);

                row.createCell(1).setCellValue(rs2.getString("FULL_NAME"));
                System.out.println("rs2.getString(\"FULL_NAME\")" + rs2.getString("FULL_NAME"));

                row.createCell(2).setCellValue(rs2.getString("gender"));

                row.createCell(3).setCellValue(rs2.getString("spn"));

                row.createCell(4).setCellValue(CommonFunctions.getFormattedOutputDate1(rs2.getDate("doe_gov")));

                row.createCell(5).setCellValue(CommonFunctions.getFormattedOutputDate1(rs2.getDate("dos")));

                row.createCell(6).setCellValue(rs2.getString("post_grp_type"));

                row.createCell(7).setCellValue(StringUtils.defaultString(rs2.getString("email_id")) + " / " + StringUtils.defaultString(rs2.getString("mobile")));
                if ((rs2.getString("matrix_cell") != null && !rs2.getString("matrix_cell").equals("")) && (rs2.getString("matrix_level") != null && !rs2.getString("matrix_level").equals(""))) {
                    row.createCell(8).setCellValue("Cell-".concat(rs2.getString("matrix_cell")).concat("/").concat("Level-".concat(rs2.getString("matrix_level"))));
                } else if (rs2.getString("cur_salary") != null && !rs2.getString("cur_salary").equals("")) {
                    row.createCell(8).setCellValue(rs2.getString("cur_salary"));
                } else {
                    row.createCell(8).setCellValue("");
                }

                //row.createCell().setCellValue(rs2.getString("cur_salary"));
                row.createCell(9).setCellValue(rs2.getString("cur_basic_salary"));

                row.createCell(10).setCellValue(rs2.getString("off_en"));

                row.createCell(11).setCellValue(rs2.getString("dist_name"));

                row.createCell(12).setCellValue("");

                row.createCell(13).setCellValue("");

                row.createCell(14).setCellValue("");

                row.createCell(15).setCellValue("");

                row.createCell(16).setCellValue("");

                row.createCell(17).setCellValue(rs2.getString("voterId"));

                row.createCell(18).setCellValue(rs2.getString("aadhar"));

                row.createCell(19).setCellValue(rs2.getString("cadre_name"));
            }

            //offCode = ")";
            //FileOutputStream fileOut = new FileOutputStream("poi-generated-file.xlsx");
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment;filename=" + distName + ".xls");
            //response.setBufferSize(1024);
            workbook.write(response.getOutputStream());
            //fileOut.close();

            // Closing the workbook
            //workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs2, pst2);
            DataBaseFunctions.closeSqlObjects(rs1, pst1);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

}
