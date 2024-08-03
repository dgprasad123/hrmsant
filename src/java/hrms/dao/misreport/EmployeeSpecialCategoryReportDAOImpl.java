/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.misreport;

import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.model.employee.Employee;
import hrms.model.misreport.EmployeeSpecialCategoryReport;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.NumberFormats;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

/**
 *
 * @author HP
 */
public class EmployeeSpecialCategoryReportDAOImpl implements EmployeeSpecialCategoryReportDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List getEmployeeSpecialCategoryReportList() {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List spempList = new ArrayList();
        EmployeeSpecialCategoryReport specialcategory = null;
        try {
            con = dataSource.getConnection();
            ps = con.prepareStatement("select getfullname(emp_id) empname,emp_id,is_regular,\n"
                    + "(select updated_date from equarter.admin_log \n"
                    + "where user_name='hrmsupport11'\n"
                    + "and sqlupdated like 'empType=A,spluserCategory%' and empid=e.emp_id order by \n"
                    + "updated_date desc limit 1) \n"
                    + "Dt_of_Special_Category,usertype category,spn designation,off_en office_name,\n"
                    + "getdeptname(o.department_code)department_name\n"
                    + "from emp_mast e\n"
                    + "left outer join g_spc g on e.cur_spc=g.spc\n"
                    + "left outer join g_office o on e.cur_off_code=o.off_code\n"
                    + "where is_regular='A' order by empname");

            rs = ps.executeQuery();
            while (rs.next()) {
                specialcategory = new EmployeeSpecialCategoryReport();
                specialcategory.setHrmsid(rs.getString("EMP_ID"));
                specialcategory.setEmpname(rs.getString("empname"));
                specialcategory.setDateofspecialcategory(CommonFunctions.getFormattedOutputDate1(rs.getDate("Dt_of_Special_Category")));
                System.out.println("specialcategory:" + specialcategory.getDateofspecialcategory());
                specialcategory.setSpecialcategorysubtype(rs.getString("category"));
                specialcategory.setOfficename(rs.getString("office_name"));
                specialcategory.setDepartmentname(rs.getString("department_name"));
                spempList.add(specialcategory);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return spempList;

    }

    @Override
    public void downloadEmployeeSpecialCategoryReportExcel(OutputStream out, String fileName, WritableWorkbook workbook) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            con = this.dataSource.getConnection();

            WritableSheet sheet = workbook.createSheet(fileName, 0);
            WritableFont headformat = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
            WritableCellFormat headcell3 = new WritableCellFormat(headformat);
            headcell3.setVerticalAlignment(VerticalAlignment.CENTRE);
            headcell3.setWrap(true);

            WritableCellFormat headcell = new WritableCellFormat(headformat);
            headcell.setVerticalAlignment(VerticalAlignment.CENTRE);
            headcell.setWrap(true);
            headcell.setBorder(Border.ALL, BorderLineStyle.DOUBLE);

            WritableCellFormat innercell = new WritableCellFormat(NumberFormats.INTEGER);
            innercell.setVerticalAlignment(VerticalAlignment.CENTRE);
            innercell.setWrap(true);

            int col = 0;
            int widthInChars = 10;

            Label label = null;
            jxl.write.Number num = null;

            label = new Label(0, 0, "Sl No.", headcell);//column,row
            sheet.setColumnView(col, widthInChars);
            col++;

            widthInChars = 20;
            sheet.addCell(label);
            label = new Label(1, 0, "Employee Name", headcell);//column,row
            sheet.setColumnView(col, widthInChars);
            col++;

            widthInChars = 40;
            sheet.addCell(label);
            label = new Label(2, 0, "HRMS ID", headcell);//column,row
            sheet.setColumnView(col, widthInChars);
            col++;

            widthInChars = 40;
            sheet.addCell(label);
            label = new Label(3, 0, "Date of Special Category", headcell);//column,row
            sheet.setColumnView(col, widthInChars);
            col++;

            widthInChars = 15;
            sheet.addCell(label);
            label = new Label(4, 0, "Special Category Sub Type", headcell);//column,row
            sheet.setColumnView(col, widthInChars);
            col++;
            sheet.addCell(label);

            widthInChars = 60;
            label = new Label(5, 0, "Office Name", headcell);//column,row
            sheet.setColumnView(col, widthInChars);
            col++;
            sheet.addCell(label);

            widthInChars = 30;
            label = new Label(6, 0, "Department name", headcell);//column,row
            sheet.setColumnView(col, widthInChars);
            col++;
            sheet.addCell(label);

            int row = 0;
            int slno = 0;

            String sql = ("select getfullname(emp_id) empname,emp_id,is_regular,\n"
                    + "(select updated_date from equarter.admin_log \n"
                    + "where user_name='hrmsupport11'\n"
                    + "and sqlupdated like 'empType=A,spluserCategory%' and empid=e.emp_id order by \n"
                    + "updated_date desc limit 1) \n"
                    + "Dt_of_Special_Category,usertype category,spn designation,off_en office_name,\n"
                    + "getdeptname(o.department_code)department_name\n"
                    + "from emp_mast e\n"
                    + "left outer join g_spc g on e.cur_spc=g.spc\n"
                    + "left outer join g_office o on e.cur_off_code=o.off_code\n"
                    + "where is_regular='A' order by empname");
            int pCount = 0;
            pst = con.prepareStatement(sql);

            rs = pst.executeQuery();

            while (rs.next()) {
                pCount++;

                slno += 1;
                row += 1;

                num = new jxl.write.Number(0, row, pCount, innercell);//column,row
                sheet.setColumnView(col, 10);
                sheet.addCell(num);
                col++;
                widthInChars = 20;

                label = new Label(1, row, rs.getString("empname"), innercell);//column,row
                sheet.setColumnView(col, widthInChars);
                sheet.addCell(label);
                col++;
                widthInChars = 40;

                label = new Label(2, row, rs.getString("EMP_ID"), innercell);//column,row
                sheet.setColumnView(col, widthInChars);
                sheet.addCell(label);
                col++;
                widthInChars = 40;

                label = new Label(3, row, rs.getString("Dt_of_Special_Category"), innercell);//column,row
                sheet.setColumnView(col, widthInChars);
                sheet.addCell(label);
                col++;
                widthInChars = 40;
                label = new Label(4, row, rs.getString("category"), innercell);//column,row
                sheet.setColumnView(col, 15);
                sheet.addCell(label);
                col++;

                widthInChars = 40;
                label = new Label(5, row, rs.getString("office_name"), innercell);//column,row
                sheet.setColumnView(col, widthInChars);
                sheet.addCell(label);
                col++;

                widthInChars = 40;
                label = new Label(6, row, rs.getString("department_name"), innercell);//column,row
                sheet.setColumnView(col, widthInChars);
                sheet.addCell(label);
                col++;

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    @Override
    public List searchEmployeeSpecialCategoryReportList(String fdate, String tdate) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List spempList = new ArrayList();
        EmployeeSpecialCategoryReport specialcategory = null;
        SimpleDateFormat sdf = new SimpleDateFormat("EEEEE MMMMM yyyy hh:mm:ss a");
        try {
            con = dataSource.getConnection();
            ps = con.prepareStatement("Select a.* from\n"
                    + "(select getfullname(emp_id) empname,emp_id,is_regular,\n"
                    + "(select updated_date from equarter.admin_log\n"
                    + "where user_name='hrmsupport11'\n"
                    + "and sqlupdated like 'empType=A,spluserCategory%' and empid=e.emp_id order by\n"
                    + "updated_date desc limit 1) \n"
                    + "Dt_of_Special_Category,usertype category,spn designation,off_en office_name,\n"
                    + "getdeptname(o.department_code)department_name\n"
                    + "from emp_mast e\n"
                    + "left outer join g_spc g on e.cur_spc=g.spc\n"
                    + "left outer join g_office o on e.cur_off_code=o.off_code\n"
                    + "where is_regular='A' order by empname)a\n"
                    + "where Dt_of_Special_Category BETWEEN ? AND ? ");
            
            ps.setTimestamp(1, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(fdate).getTime()));
            ps.setTimestamp(2, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(tdate).getTime()));

            rs = ps.executeQuery();
            while (rs.next()) {
                specialcategory = new EmployeeSpecialCategoryReport();
                specialcategory.setHrmsid(rs.getString("EMP_ID"));
                specialcategory.setEmpname(rs.getString("empname"));
                specialcategory.setDateofspecialcategory(CommonFunctions.getFormattedOutputDate1(rs.getDate("Dt_of_Special_Category")));
                System.out.println("specialcategory:" + specialcategory.getDateofspecialcategory());
                specialcategory.setSpecialcategorysubtype(rs.getString("category"));
                specialcategory.setOfficename(rs.getString("office_name"));
                specialcategory.setDepartmentname(rs.getString("department_name"));
                spempList.add(specialcategory);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return spempList;

    }

}
