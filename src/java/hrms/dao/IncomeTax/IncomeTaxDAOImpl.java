/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.IncomeTax;

import hrms.SelectOption;
import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.model.IncomeTax.AnnexureIBean;
import hrms.model.IncomeTax.IT24QBean;
import hrms.model.IncomeTax.IT24QDetailBean;
import hrms.model.IncomeTax.IncomeTaxBean;
import hrms.model.IncomeTax.IncomeTaxList;
import hrms.model.IncomeTax.DeducteeBean;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import static java.util.Collections.list;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Manoj PC
 */
public class IncomeTaxDAOImpl implements IncomeTaxDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List getYears() {
        ArrayList li = new ArrayList();
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int prevYear = year - 1;
        int nextYear = year + 1;
        SelectOption so = null;
        so = new SelectOption();
        so.setLabel(prevYear + "");
        so.setValue(prevYear + "");
        li.add(so);
        so = new SelectOption();
        so.setLabel(year + "");
        so.setValue(year + "");
        li.add(so);
        so = new SelectOption();
        so.setLabel(nextYear + "");
        so.setValue(nextYear + "");
        li.add(so);
        return li;
    }

    @Override
    public List getEmployeeWiseIT(int year, int month, String trCode) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        PreparedStatement ps1 = null;
        ResultSet rs1 = null;
        PreparedStatement ps2 = null;
        ResultSet rs2 = null;
        List ddoList = new ArrayList();
        IncomeTaxList itl = null;
        String aqSlnos = "";
        int numEmployees = 0;
        int totalAmount = 0;
        try {
            con = dataSource.getConnection();

            String ddoListQry = "select DISTINCT GO.off_en, GO.off_code from bill_mast BM INNER JOIN g_office GO ON BM.off_code = GO.off_code"
                    + " INNER JOIN aq_mast AM ON AM.bill_no = BM.bill_no"
                    + " where BM.tr_code = ? AND AM.aq_month = ? and AM.aq_year = ?";
            
            pstmt = con.prepareStatement(ddoListQry);
            pstmt.setString(1, trCode);
            pstmt.setInt(2, month);
            pstmt.setInt(3, year);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                itl = new IncomeTaxList();
                itl.setOfficeName(rs.getString("off_en"));
                String sql = "select COALESCE(COUNT(emp_code),0) AS num_emps, COALESCE(SUM(ad_amt), 0) AS total_amt from aq_dtls where aqsl_no IN(select AM.aqsl_no from bill_mast BM"
                        + " INNER JOIN aq_mast AM ON AM.bill_no = BM.bill_no"
                        + " where BM.tr_code = ? AND AM.aq_month = ? and AM.aq_year = ? AND BM.off_code = ?) and ad_code = 'IT' AND ad_amt > 0";
                ps1 = con.prepareStatement(sql);
                ps1.setString(1, trCode);
                ps1.setInt(2, month);
                ps1.setInt(3, year);
                ps1.setString(4, rs.getString("off_code"));
                rs1 = ps1.executeQuery();
                while (rs1.next()) {
                    numEmployees = rs1.getInt("num_emps");
                    totalAmount = rs1.getInt("total_amt");
                }
                itl.setTotalEmployees(numEmployees + "");
                itl.setTotalAmount(totalAmount + "");
                if (totalAmount > 0) {
                    ddoList.add(itl);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return ddoList;
    }

    @Override
    public List getDDODetailsList(String trCode) {

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List ddoList = new ArrayList();
        SelectOption so = null;
        try {
            con = dataSource.getConnection();

            String ddoListQry = "select off_code, ddo_code from g_office where tr_code=? and is_ddo=? order by ddo_code";
            pstmt = con.prepareStatement(ddoListQry);
            pstmt.setString(1, trCode);
            pstmt.setString(2, "Y");
            rs = pstmt.executeQuery();
            while (rs.next()) {

                so = new SelectOption();
                so.setLabel(rs.getString("ddo_code"));
                so.setValue(rs.getString("off_code"));
                ddoList.add(so);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return ddoList;
    }

    @Override
    public List getDDOEmployeeWiseIT(int year, int month, String offCode) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        PreparedStatement ps1 = null;
        ResultSet rs1 = null;
        PreparedStatement ps2 = null;
        ResultSet rs2 = null;
        List ddoList = new ArrayList();
        IncomeTaxBean itl = null;
        String aqSlnos = "";
        int numEmployees = 0;
        int totalAmount = 0;
        try {
            con = dataSource.getConnection();

            String ddoListQry = "select DISTINCT GO.off_en, GO.off_code from bill_mast BM"
                    + " INNER JOIN g_office GO ON BM.off_code = GO.off_code"
                    + " INNER JOIN aq_mast AM ON AM.bill_no = BM.bill_no"
                    + " where BM.off_code = ? AND AM.aq_month = ? and AM.aq_year = ?";
            
            pstmt = con.prepareStatement(ddoListQry);
            pstmt.setString(1, offCode);
            pstmt.setInt(2, month);
            pstmt.setInt(3, year);
            rs = pstmt.executeQuery();
            while (rs.next()) {

                String sql = "select (SELECT ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME, L_NAME], ' ') FROM emp_mast WHERE emp_id = EMP_CODE) as full_name"
                        + ", (select post from emp_mast EM INNER JOIN g_spc GS ON EM.cur_spc = GS.spc INNER JOIN g_post GP ON GS.gpc = GP.post_code where emp_id = EMP_CODE) AS post_name"
                        + ", (SELECT id_no FROM emp_id_doc WHERE emp_id = EMP_CODE AND id_description = 'PAN' LIMIT 1) AS pan_no"
                        + ", ad_amt from aq_dtls where aqsl_no IN(select AM.aqsl_no from bill_mast BM "
                        + " INNER JOIN aq_mast AM ON AM.bill_no = BM.bill_no "
                        + " where BM.off_code = ? AND AM.aq_month = ? and AM.aq_year = ?) and ad_code = 'IT' AND ad_amt > 0";
                ps1 = con.prepareStatement(sql);
                ps1.setString(1, offCode);
                ps1.setInt(2, month);
                ps1.setInt(3, year);
                
                rs1 = ps1.executeQuery();
                while (rs1.next()) {
                    itl = new IncomeTaxBean();
                    itl.setEmpName(rs1.getString("full_name"));
                    itl.setPan(rs1.getString("pan_no"));
                    itl.setAmount(rs1.getString("ad_amt"));
                    itl.setPostName(rs1.getString("post_name"));
                    ddoList.add(itl);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return ddoList;
    }

    @Override
    public void addExcelRowIntoDB(Workbook workbook, List li, String offCode) {
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet searchExistRs = null;
        try {
            con = this.dataSource.getConnection();

            Sheet sheet = workbook.getSheet(0);
            int noofRows = sheet.getRows();
            
            Cell cell1 = null;
            String ain = null;
            String financialYear = null;
            String monthofFiling = null;
            String tan = null;
            String natureOfDeduction = null;
            String receiptNumber = null;
            String ddoSerialNo = null;
            String voucherDate = null;
            String ProvisionalReceiptNo = null;
            String formType = null;
            String ddoName = null;
            String ainCategory = null;
            String binNumber = null;
            String tdsAmount = null;
            String tvDate = null;

            ps = con.prepareStatement("INSERT INTO g_it_binfile(tan_no, ddo_name, ain"
                    + ", ain_category, form_type, tv_date, bin_no, bsr_code, voucher_date"
                    + ", ddo_serial, amount_remitted, financial_year, month)"
                    + " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            int totalRecords = 0;
            for (int r = 0; r < noofRows; r++) {
                boolean skiprow = false;
                if (skiprow == false) {
                    totalRecords = 0;
                    if (r == 0) {
                        cell1 = sheet.getCell(0, r);
                        ain = cell1.getContents();    // Test Count + :
                        
                        cell1 = sheet.getCell(2, r);
                        financialYear = cell1.getContents();
                        
                        cell1 = sheet.getCell(6, r);
                        ain = cell1.getContents().replace("AIN : ", "");
                        
                        cell1 = sheet.getCell(4, r);
                        monthofFiling = cell1.getContents();
                        
                    }
                    if (r > 4) {
                        cell1 = sheet.getCell(0, r);
                        tan = cell1.getContents();
                        cell1 = sheet.getCell(1, r);
                        ddoName = cell1.getContents();
                        cell1 = sheet.getCell(2, r);
                        ainCategory = cell1.getContents();
                        cell1 = sheet.getCell(3, r);
                        formType = cell1.getContents();
                        cell1 = sheet.getCell(4, r);
                        voucherDate = cell1.getContents();
                        cell1 = sheet.getCell(5, r);
                        binNumber = cell1.getContents();
                        
                        cell1 = sheet.getCell(6, r);
                        tdsAmount = cell1.getContents().replace(",", "").replace(".00", "");
                        receiptNumber = binNumber.substring(0, 7);
                        tvDate = binNumber.substring(7, 15);
                        ddoSerialNo = binNumber.substring(15);

                        ps.setString(1, tan);
                        ps.setString(2, ddoName);
                        ps.setString(3, ain);
                        ps.setString(4, ainCategory);
                        ps.setString(5, formType);
                        ps.setString(6, voucherDate);
                        ps.setString(7, binNumber);
                        ps.setString(8, receiptNumber);
                        ps.setString(9, tvDate);
                        ps.setString(10, ddoSerialNo);
                        ps.setString(11, tdsAmount);
                        ps.setString(12, financialYear);
                        ps.setString(13, monthofFiling);
                        /*String query = "INSERT INTO g_it_binfile(tan_no, ddo_name, ain"
                         + ", ain_category, form_type, tv_date, bin_no, bsr_code, voucher_date"
                         + ", ddo_serial, amount_remitted, financial_year, month)"
                         + " VALUES('"+tan+"', '"+ddoName+"', '"+ain+"', '"+ainCategory+"', '"+formType+"', '"+voucherDate
                         +"', '"+binNumber+"', '"+receiptNumber+"', '"+tvDate+"', '"+ddoSerialNo+"', '"+tdsAmount+"', '"+financialYear+"', '"+monthofFiling+"');";
                         */
                        ps1 = con.prepareStatement("SELECT COALESCE(COUNT(*),0) AS total_rows from g_it_binfile"
                                + " WHERE ain = ? AND financial_year = ? AND month = ?"
                                + " AND tan_no = ? AND ddo_serial = ? AND form_type = ?");
                        ps1.setString(1, ain);
                        ps1.setString(2, financialYear);
                        ps1.setString(3, monthofFiling);
                        ps1.setString(4, tan);
                        ps1.setString(5, ddoSerialNo);
                        ps1.setString(6, formType);
                        searchExistRs = ps1.executeQuery();
                        while (searchExistRs.next()) {
                            totalRecords = searchExistRs.getInt("total_rows");
                        }
                        if (totalRecords == 0) {
                            ps.executeUpdate();
                        }
                    }
                    /*cell1 = sheet.getCell(2, r);
                     monthofFiling = cell1.getContents();
                     cell1 = sheet.getCell(3, r);
                     tan = cell1.getContents();
                     cell1 = sheet.getCell(4, r);
                     natureOfDeduction = cell1.getContents();
                     cell1 = sheet.getCell(5, r);
                     receiptNumber = cell1.getContents();
                     cell1 = sheet.getCell(6, r);
                     ddoSerialNo = cell1.getContents();
                     cell1 = sheet.getCell(7, r);
                     voucherDate = cell1.getContents();
                     cell1 = sheet.getCell(8, r);
                     ProvisionalReceiptNo = cell1.getContents();
                     cell1 = sheet.getCell(9, r);
                     typeOfStatement = cell1.getContents();
                     
                     //+"Month of Filing:"+monthofFiling
                     //+"TAN:"+tan+ "Nature of Ded:"+natureOfDeduction+"Receipt No:"+receiptNumber
                     //+" Serial No:"+ddoSerialNo+" Voucher Date:"+voucherDate+" Prov Receipt no:"
                     //+ProvisionalReceiptNo+"Type of Statement:"+typeOfStatement);
                     ps.setString(1, ain);
                     ps.setString(2, financialYear);
                     ps.setString(3, monthofFiling);
                     ps.setString(4, tan);
                     ps.setString(5, natureOfDeduction);
                     ps.setString(6, receiptNumber);
                     ps.setInt(7, Integer.parseInt(ddoSerialNo));
                     ps.setString(8, voucherDate);
                     ps.setString(9, ProvisionalReceiptNo);
                     ps.setString(10, typeOfStatement);

                     ps1 = con.prepareStatement("SELECT COALESCE(COUNT(*),0) AS total_rows from g_tr_binfile"
                     + " WHERE ain = ? AND financial_year = ? AND month_of_filing = ?"
                     + " AND tan_no = ? AND ddo_serial = ?");
                     ps1.setString(1, ain);
                     ps1.setString(2, financialYear);
                     ps1.setString(3, monthofFiling);
                     ps1.setString(4, tan);
                     ps1.setInt(5, Integer.parseInt(ddoSerialNo));
                     searchExistRs = ps1.executeQuery();
                     while (searchExistRs.next()) {
                     totalRecords = searchExistRs.getInt("total_rows");
                     }
                     if (totalRecords == 0) {
                     //ps.executeUpdate();
                     }*/

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(searchExistRs, ps);
            DataBaseFunctions.closeSqlObjects(ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public AnnexureIBean getAnnexure(String offCode) {
        AnnexureIBean AI = null;
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List ddoList = new ArrayList();
        int month = 2;
        int year = 2019;
        SelectOption so = null;
        PreparedStatement ps1 = null;
        ResultSet rs1 = null;
        PreparedStatement ps2 = null;
        ResultSet rs2 = null;
        IncomeTaxBean itl = null;
        String sql1 = null;
        String[] arrMonths = {"January", "February", "March", "April", "May", "June"};
        int monthIdx = 0;
        int count = 0;
        int totalAmount = 0;
        int rowCount = 0;
        int innerCount = 0;
        try {
            con = dataSource.getConnection();

            String ddoListQry = "select DISTINCT(emp_id),gpf_no,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME, L_NAME]"
                    + ", ' ') EMPNAME,g.spn,u.username,u.password,e.cur_spc, UPPER(tan_no) tan_no from emp_mast e "
                    + " inner join user_details u on e.emp_id=u.linkid"
                    + " inner join g_spc g on e.cur_spc=g.spc"
                    + " INNER JOIN g_privilege_map m on e.cur_spc=m.spc"
                    + " INNER JOIN g_office GO on GO.off_code=e.cur_off_code"
                    + " where cur_off_code='" + offCode + "' and IS_REGULAR='Y' and role_id='05'";
            pstmt = con.prepareStatement(ddoListQry);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                AI = new AnnexureIBean();
                AI.setEmployerName(rs.getString("EMPNAME"));
                AI.setTanNo(rs.getString("tan_no"));
            }
            sql1 = "select * FROM g_it_binfile WHERE tan_no = ? AND form_type = '24Q'";
            sql1 += " AND financial_year = ? AND month IN('April', 'May', 'June') ORDER BY ";
            sql1 += " CASE"
                    + " WHEN month='January' THEN 1"
                    + " WHEN month='February' THEN 2"
                    + " WHEN month='March' THEN 3"
                    + " WHEN month='April' THEN 4"
                    + " WHEN month='May' THEN 5"
                    + " WHEN month='June' THEN 6"
                    + " END";

            ps2 = con.prepareStatement(sql1);
            ps2.setString(1, AI.getTanNo());
            ps2.setString(2, "2019-20");
            
            rs2 = ps2.executeQuery();

            while (rs2.next()) {
                count++;
                for (int i = 0; i < arrMonths.length; i++) {
                    if (arrMonths[i].equals(rs2.getString("month"))) {
                        monthIdx = i;
                    }
                }
                String ddoQry = "select DISTINCT GO.off_en, GO.off_code from bill_mast BM"
                        + " INNER JOIN g_office GO ON BM.off_code = GO.off_code"
                        + " INNER JOIN aq_mast AM ON AM.bill_no = BM.bill_no"
                        + " where BM.off_code = ? AND AM.aq_month = ? and AM.aq_year = ?";
                
                pstmt = con.prepareStatement(ddoQry);
                pstmt.setString(1, offCode);
                pstmt.setInt(2, monthIdx);
                pstmt.setInt(3, year);
                rs = pstmt.executeQuery();
                while (rs.next()) {

                    String sql = "select EMP_CODE, (SELECT ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME, L_NAME], ' ') FROM emp_mast WHERE emp_id = EMP_CODE) as full_name"
                            + ", (select post from emp_mast EM INNER JOIN g_spc GS ON EM.cur_spc = GS.spc INNER JOIN g_post GP ON GS.gpc = GP.post_code where emp_id = EMP_CODE) AS post_name"
                            + ", (SELECT id_no FROM emp_id_doc WHERE emp_id = EMP_CODE AND id_description = 'PAN' LIMIT 1) AS pan_no"
                            + ", ad_amt from aq_dtls where aqsl_no IN(select AM.aqsl_no from bill_mast BM "
                            + " INNER JOIN aq_mast AM ON AM.bill_no = BM.bill_no "
                            + " where BM.off_code = ? AND AM.aq_month = ? and AM.aq_year = ?) and ad_code = 'IT' AND ad_amt > 0";
                    ps1 = con.prepareStatement(sql);
                    ps1.setString(1, offCode);
                    ps1.setInt(2, monthIdx);
                    ps1.setInt(3, year);
                    
                    rowCount = 0;
                    rs1 = ps1.executeQuery();
                    while (rs1.next()) {
                        // Process the row.
                        rowCount++;
                    }
                    rs1 = ps1.executeQuery();
                    innerCount = 0;
                    totalAmount = 0;
                    String vDate = null;
                    while (rs1.next()) {
                        innerCount++;
                        itl = new IncomeTaxBean();
                        vDate = rs2.getString("voucher_date").substring(0, 2)
                                + "/" + rs2.getString("voucher_date").substring(2, 4)
                                + "/" + rs2.getString("voucher_date").substring(4);
                        totalAmount += Integer.parseInt(rs1.getString("ad_amt"));
                        itl.setEmpName(rs1.getString("full_name"));
                        itl.setPan(rs1.getString("pan_no"));
                        itl.setAmount(rs1.getString("ad_amt"));
                        itl.setPostName(rs1.getString("post_name"));
                        itl.setEmpId(rs1.getString("EMP_CODE"));
                        itl.setDateDeposited("01/" + StringUtils.leftPad(((month + 1) + ""), 2, "0") + "/" + year);
                        itl.setBsrCode(rs2.getString("bsr_code"));
                        itl.setVoucherDate(vDate);
                        itl.setDdoSerial(rs2.getString("ddo_serial"));
                        itl.setChallanSerial(count + "");
                        itl.setTdsAmount(rs2.getString("amount_remitted"));
                        ddoList.add(itl);
                        
                        if (rowCount == innerCount) {
                            itl = new IncomeTaxBean();
                            itl.setAmount(totalAmount + "");
                            itl.setTdsAmount(rs2.getString("amount_remitted"));
                            
                            ddoList.add(itl);
                        }
                    }
                }

            }

            AI.setDataList(ddoList);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return AI;
    }

    @Override
    public ArrayList getFiscalYear() {
        int startYear = 2018;
        Calendar now = Calendar.getInstance();
        int curYear = now.get(Calendar.YEAR);
        int endYear = curYear;
        String fiscalYear = "";
        int year = Calendar.getInstance().get(Calendar.YEAR) + 2;
        ArrayList al = new ArrayList();
        for (int i = 0; endYear != year; i++) {
            fiscalYear = startYear + "-" + endYear;
            al.add(fiscalYear);
            startYear++;
            endYear++;
        }
        Collections.reverse(al);
        return al;
    }

    public String getCurFiscalYear() {
        Calendar now = Calendar.getInstance();
        int curYear = now.get(Calendar.YEAR);
        String fiscalYear = curYear + " - " + (curYear + 1);
        return fiscalYear;
    }

    @Override
    public IT24QBean get24Q(String offCode, String finyear, String finQuarter) {

        AnnexureIBean AI = null;

        Connection con = null;

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        ResultSet rs2 = null;
        PreparedStatement ps2 = null;

        List ddoList = new ArrayList();
        int month = 2;
        int year = 2019;

        SelectOption so = null;

        PreparedStatement ps1 = null;
        ResultSet rs1 = null;

        IT24QBean it24 = null;
        IT24QDetailBean it24Detail = null;
        try {
            con = dataSource.getConnection();

            String ddoListQry = "select DISTINCT(emp_id),off_en as office_name, department_name, GO.off_address, gpf_no,GO.ddo_code,GO.off_address,GO.state_code,GO.pincode,GO.tel_std,GO.tel_no,e.mobile,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME, L_NAME]\n"
                    + ", ' ') EMPNAME,tel_std, tel_no, pincode, g.spn,u.username,u.password,e.cur_spc"
                    + ", UPPER(tan_no) tan_no, e.mobile, e.email_id from emp_mast e \n"
                    + " inner join user_details u on e.emp_id=u.linkid\n"
                    + " inner join g_spc g on e.cur_spc=g.spc\n"
                    + " INNER JOIN g_privilege_map m on e.cur_spc=m.spc\n"
                    + " INNER JOIN g_office GO on GO.off_code=e.cur_off_code\n"
                    + " INNER JOIN g_department D ON D.department_code = GO.department_code"
                    + " where cur_off_code='" + offCode + "' and IS_REGULAR='Y' and role_id='05' LIMIT 1";
            pstmt = con.prepareStatement(ddoListQry);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                it24 = new IT24QBean();
                it24.setEmployerName(rs.getString("EMPNAME"));
                it24.setTan(rs.getString("tan_no"));
                it24.setDdoCode(rs.getString("ddo_code"));
                it24.setAddress(rs.getString("off_address"));
                it24.setPhone(rs.getString("tel_std") + "-" + rs.getString("tel_no"));
                it24.setMobile(rs.getString("mobile"));
                it24.setEmail(rs.getString("email_id"));
                it24.setFinancialYear(finyear);
                it24.setPinCode(rs.getString("pincode"));
                it24.setOfficeName(rs.getString("office_name"));
                it24.setDeptName(rs.getString("department_name"));
                it24.setOffAddress(rs.getString("off_address"));
                it24.setDesignation(rs.getString("spn"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return it24;
    }

    @Override
    public IT24QBean getChallanDetails(String offCode, String finyear, String finQuarter, String formType) {

        AnnexureIBean AI = null;

        Connection con = null;

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        ResultSet rs2 = null;
        PreparedStatement ps2 = null;

        List ddoList = new ArrayList();
        int month = 2;
        int year = 2019;

        SelectOption so = null;

        PreparedStatement ps1 = null;
        ResultSet rs1 = null;
        String sql = null;
        IT24QBean it24 = null;
        IT24QDetailBean it24Detail = null;
        String vDate = null;
        try {
            con = dataSource.getConnection();
            String ddoListQry = "select DISTINCT(emp_id),gpf_no,GO.ddo_code,GO.off_address,GO.state_code,GO.pincode,GO.tel_std,GO.tel_no,e.mobile,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME, L_NAME]\n"
                    + ", ' ') EMPNAME,g.spn,u.username,u.password,e.cur_spc, UPPER(tan_no) tan_no from emp_mast e \n"
                    + " inner join user_details u on e.emp_id=u.linkid\n"
                    + " inner join g_spc g on e.cur_spc=g.spc\n"
                    + " INNER JOIN g_privilege_map m on e.cur_spc=m.spc\n"
                    + " INNER JOIN g_office GO on GO.off_code=e.cur_off_code\n"
                    + " where cur_off_code='" + offCode + "' and IS_REGULAR='Y' and role_id='05' LIMIT 1";
            pstmt = con.prepareStatement(ddoListQry);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                it24 = new IT24QBean();
                it24.setEmployerName(rs.getString("EMPNAME"));
                it24.setTan(rs.getString("tan_no"));
                it24.setDdoCode(rs.getString("ddo_code"));
                it24.setAddress(rs.getString("off_address"));
                it24.setPhone(rs.getString("tel_std") + "-" + rs.getString("tel_no"));
                it24.setMobile(rs.getString("mobile"));
                it24.setFinancialYear(finyear);
                it24.setPinCode(rs.getString("pincode"));

            }
            sql = "select * FROM g_it_binfile WHERE tan_no = ? AND form_type = '" + formType + "'";
            sql += " AND financial_year = ? AND month IN('April', 'May', 'June') ORDER BY ";
            sql += " CASE"
                    + " WHEN month='January' THEN 1"
                    + " WHEN month='February' THEN 2"
                    + " WHEN month='March' THEN 3"
                    + " WHEN month='April' THEN 4"
                    + " WHEN month='May' THEN 5"
                    + " WHEN month='June' THEN 6"
                    + " END";

            ps2 = con.prepareStatement(sql);
            ps2.setString(1, it24.getTan());
            ps2.setString(2, "2019-20");
            
            rs2 = ps2.executeQuery();
            while (rs2.next()) {
                vDate = rs2.getString("voucher_date").substring(0, 2)
                        + "/" + rs2.getString("voucher_date").substring(2, 4)
                        + "/" + rs2.getString("voucher_date").substring(4);
                
                it24Detail = new IT24QDetailBean();
                it24Detail.setChallanNo(rs2.getString("bsr_code"));
                it24Detail.setDateDeposited(vDate);
                it24Detail.setDdoSerial(rs2.getString("ddo_serial"));
                it24Detail.setTdsAmount(rs2.getString("amount_remitted"));
                ddoList.add(it24Detail);
            }
            it24.setDataList(ddoList);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return it24;
    }

    @Override
    public int getTotal26QAmount(String financialYear, String quarter, String month, String offCode) {
        int totalTDSAmount = 0;
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ResultSet rs2 = null;
        PreparedStatement ps2 = null;
        List ddoList = new ArrayList();
        int year = 2019;
        IT24QBean it24 = null;
        PreparedStatement ps1 = null;
        ResultSet rs1 = null;
        String sql = null;
        try {
            con = dataSource.getConnection();
            String ddoListQry = "select DISTINCT(emp_id),gpf_no,GO.ddo_code,GO.off_address,GO.state_code,GO.pincode,GO.tel_std,GO.tel_no,e.mobile,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME, L_NAME]\n"
                    + ", ' ') EMPNAME,g.spn,u.username,u.password,e.cur_spc, UPPER(tan_no) tan_no from emp_mast e \n"
                    + " inner join user_details u on e.emp_id=u.linkid\n"
                    + " inner join g_spc g on e.cur_spc=g.spc\n"
                    + " INNER JOIN g_privilege_map m on e.cur_spc=m.spc\n"
                    + " INNER JOIN g_office GO on GO.off_code=e.cur_off_code\n"
                    + " where cur_off_code='" + offCode + "' and IS_REGULAR='Y' and role_id='05' LIMIT 1";
            pstmt = con.prepareStatement(ddoListQry);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                it24 = new IT24QBean();
                it24.setTan(rs.getString("tan_no"));
            }
            sql = "select * FROM g_it_binfile WHERE tan_no = ? AND form_type = '26Q'";
            sql += " AND financial_year = ? AND month = ? ORDER BY ";
            sql += " CASE"
                    + " WHEN month='January' THEN 1"
                    + " WHEN month='February' THEN 2"
                    + " WHEN month='March' THEN 3"
                    + " WHEN month='April' THEN 4"
                    + " WHEN month='May' THEN 5"
                    + " WHEN month='June' THEN 6"
                    + " END";
            
            ps2 = con.prepareStatement(sql);
            ps2.setString(1, it24.getTan());
            ps2.setString(2, "2019-20");
            ps2.setString(3, month);
            
            rs2 = ps2.executeQuery();
            while (rs2.next()) {
                totalTDSAmount = Integer.parseInt(rs2.getString("amount_remitted"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return totalTDSAmount;
    }

    @Override
    public void SaveDeducteeDetail(DeducteeBean dBean, String empId, String spc, String offCode) {
        Connection con = null;
        PreparedStatement pst = null;
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        try {
            con = this.dataSource.getConnection();
            pst = con.prepareStatement("INSERT INTO g_26q_mast(reference_number"
                    + ", pan, deductee_name, payment_date, amount, deductee_code, emp_id, off_code"
                    + ", emp_spc, date_added, financial_year, quarter, month) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)");
            String[] arrPan = dBean.getPan().split(",");
            String[] arrRefs = dBean.getRefNumber().split(",");
            String[] arrNames = dBean.getDeducteeName().split(",");
            String[] arrPdates = dBean.getPaymentDate().split(",");
            String[] arrAmount = dBean.getAmount().split(",");
            String[] arrDcodes = dBean.getDeducteeCode().split(",");
            
            for (int i = 0; i < arrPan.length; i++) {
                pst.setString(1, arrRefs[i]);
                pst.setString(2, arrPan[i]);
                pst.setString(3, arrNames[i]);
                pst.setTimestamp(4, new Timestamp(dateFormat.parse(arrPdates[i]).getTime()));
                pst.setInt(5, Integer.parseInt(arrAmount[i]));
                pst.setString(6, arrDcodes[i]);
                pst.setString(7, empId);
                pst.setString(8, offCode);
                pst.setString(9, spc);
                pst.setTimestamp(10, timestamp);
                pst.setString(11, dBean.getFinancialYear());
                pst.setInt(12, Integer.parseInt(dBean.getQuarter()));
                pst.setString(13, dBean.getMonth());
                pst.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public ArrayList get26QList(String offCode, String financialYear, String quarter, String month) {
        Connection con = null;

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        ResultSet rs2 = null;
        PreparedStatement ps2 = null;

        ArrayList DeducteeList = new ArrayList();
        DeducteeBean dBean = null;
        PreparedStatement ps1 = null;
        ResultSet rs1 = null;

        try {
            con = dataSource.getConnection();

            String ddoListQry = "SELECT * FROM g_26q_mast WHERE off_code = ? AND financial_year = ? AND quarter = ? AND month = ?";
            pstmt = con.prepareStatement(ddoListQry);
            pstmt.setString(1, offCode);
            pstmt.setString(2, financialYear);
            pstmt.setInt(3, Integer.parseInt(quarter));
            pstmt.setString(4, month);

            rs = pstmt.executeQuery();
            while (rs.next()) {
                dBean = new DeducteeBean();
                dBean.setRefNumber(rs.getString("reference_number"));
                dBean.setPan(rs.getString("pan"));
                dBean.setDeducteeName(rs.getString("deductee_name"));
                dBean.setDeducteeCode(rs.getString("deductee_code"));
                dBean.setPaymentDate(CommonFunctions.getFormattedOutputDate1(rs.getTimestamp("payment_date")));
                dBean.setAmount(rs.getInt("amount") + "");
                dBean.setDeducteeId(rs.getInt("unique_id") + "");
                DeducteeList.add(dBean);
                
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return DeducteeList;
    }

    @Override
    public void deleteDeductee(String deducteeId) {
        Connection con = null;
        PreparedStatement pst = null;
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        try {
            con = this.dataSource.getConnection();
            pst = con.prepareStatement("DELETE FROM g_26q_mast WHERE unique_id = ?");
            pst.setInt(1, Integer.parseInt(deducteeId));
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
    }
}
