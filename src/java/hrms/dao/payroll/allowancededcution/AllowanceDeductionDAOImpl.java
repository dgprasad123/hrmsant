/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.payroll.allowancededcution;

import hrms.SelectOption;
import hrms.common.DataBaseFunctions;
import hrms.common.Message;
import hrms.model.employee.PayComponent;
import hrms.model.payroll.allowancededcution.AllowanceDeduction;
import hrms.model.payroll.allowancededcution.Formula;
import hrms.model.payroll.allowancededcution.ManageADEmployeeList;
import hrms.model.payroll.allowancededcution.OfficeWiseAllowanceAndDeductionForm;
import hrms.model.payroll.allowancededcution.OfficeWiseAllowanceAndDeductionListBean;
import hrms.model.payroll.aqdtls.AqDtlsModel;
import hrms.model.payroll.aqmast.AqmastModel;
import hrms.model.payroll.billbrowser.SectionDtlSPCWiseEmp;
import hrms.model.payroll.billmast.BillMastModel;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 *
 * @author Manas Jena
 */
public class AllowanceDeductionDAOImpl implements AllowanceDeductionDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List getHRAFormulaList() {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List hraformulaList = new ArrayList();

        SelectOption so = null;
        try {
            con = this.dataSource.getConnection();

            String sql = "select * from g_formula where ad_code='53' order by formula_name";
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                so = new SelectOption();
                so.setLabel(rs.getString("formula_name"));
                so.setValue(rs.getString("formula_name"));
                hraformulaList.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return hraformulaList;
    }

    @Override
    public void uploadAllowanceExcelData(String adCode, Workbook workbook,String formula) {

        Connection con = null;

        PreparedStatement pst1 = null;
        ResultSet rs1 = null;

        PreparedStatement pst2 = null;

        PreparedStatement pst3 = null;
        String sql2 = null;

        String empId = "";
        int fixedValue = 0;

        try {
            con = this.dataSource.getConnection();

            String sql1 = "select * from update_ad_info where ref_ad_code=? and updation_ref_code=? and where_updated='E'";
            pst1 = con.prepareStatement(sql1);

            sql2 = "insert into update_ad_info(ref_ad_code,is_fixed,fixedvalue,where_updated,updation_ref_code,ad_formula) values(?,?,?,?,?,?)";
            pst2 = con.prepareStatement(sql2);

            String sql3 = "update update_ad_info set is_fixed=?,fixedvalue=?,ad_formula=? where ref_ad_code=? and updation_ref_code=?";
            pst3 = con.prepareStatement(sql3);

            Sheet firstSheet = workbook.getSheetAt(0);
            int noofRows = firstSheet.getLastRowNum();
            for (int r = 1; r <= noofRows; r++) {
                Row row = firstSheet.getRow(r);
                int index = 0;
                if (row != null) {
                    for (int count = 0; count < row.getLastCellNum(); count++) {
                        Cell cell = row.getCell(count, Row.RETURN_BLANK_AS_NULL);
                        if (cell == null || cell.equals("")) {
                            continue;
                        }
                        index++;
                        switch (cell.getCellType()) {

                            case Cell.CELL_TYPE_STRING:
                                if (index == 1) {
                                    empId = cell.getStringCellValue();
                                }
                                if (index == 2) {
                                    fixedValue = Integer.parseInt(cell.getStringCellValue());
                                }
                                break;
                            case Cell.CELL_TYPE_NUMERIC:
                                if (index == 1) {
                                    empId = cell.getNumericCellValue() + "";
                                    BigDecimal bg = new BigDecimal(empId);
                                    empId = bg.longValue() + "";
                                }
                                if (index == 2) {
                                    Double d = new Double(cell.getNumericCellValue());
                                    fixedValue = d.intValue();
                                }
                                break;
                            case Cell.CELL_TYPE_BLANK:
                                break;
                        }
                    }
                    pst1.setString(1, adCode);
                    pst1.setString(2, empId);
                    rs1 = pst1.executeQuery();
                    if (rs1.next()) {
                        pst3.setInt(1, 0);
                        pst3.setInt(2, 0);
                        pst3.setString(3, formula);
                        pst3.setString(4, adCode);
                        pst3.setString(5, empId);
                        pst3.executeUpdate();
                    } else {
                        pst2.setString(1, adCode);
                        pst2.setInt(2, 0);
                        pst2.setInt(3, 0);
                        pst2.setString(4, "E");
                        pst2.setString(5, empId);
                        pst2.setString(6, formula);
                        pst2.executeUpdate();
                    }
                    //System.out.println("adcode:value:empid:btid-------->" + adCode + ":" + fixedValue + ":" + empId + ":" + btid);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst1, pst2, pst3);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    public class ADOBJECT {

        private int adamt;
        private String btid;
        private String daformula;

        public int getAdamt() {
            return adamt;
        }

        public void setAdamt(int adamt) {
            this.adamt = adamt;
        }

        public String getBtid() {
            return btid;
        }

        public void setBtid(String btid) {
            this.btid = btid;
        }

        public String getDaformula() {
            return daformula;
        }

        public void setDaformula(String daformula) {
            this.daformula = daformula;
        }
    }

    @Override
    public ArrayList getEmployeeWiseAllowance(String empId) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList allowanceList = new ArrayList();
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT G_AD_LIST.ad_code,ad_desc"
                    + ",G_AD_LIST.ad_formula as O_Formula,UPDATE_AD_INFO.ad_formula as U_Formula"
                    + ",G_AD_LIST.fixedvalue as O_VALUE,UPDATE_AD_INFO.fixedvalue as U_VALUE"
                    + ",AD_CODE_NAME, G_AD_LIST.OBJECT_HEAD, UPDATE_AD_INFO.BT_ID AS U_BT_ID FROM G_AD_LIST "
                    + "LEFT OUTER JOIN (SELECT * FROM UPDATE_AD_INFO WHERE where_updated = 'E' AND UPDATION_REF_CODE=?)UPDATE_AD_INFO ON G_AD_LIST.AD_CODE=UPDATE_AD_INFO.REF_AD_CODE "
                    + "WHERE AD_TYPE='A' order by ad_desc ");
            pstmt.setString(1, empId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                AllowanceDeduction allowance = new AllowanceDeduction();
                allowance.setAdcode(rs.getString("ad_code"));
                allowance.setAddesc(rs.getString("ad_desc"));
                allowance.setAdcodename(rs.getString("AD_CODE_NAME"));
                if (rs.getString("U_Formula") != null) {
                    allowance.setFormula(rs.getString("U_Formula"));
                } else {
                    allowance.setFormula(rs.getString("O_Formula"));
                }
                if (rs.getString("U_VALUE") != null) {
                    allowance.setAdvalue(rs.getInt("U_VALUE"));
                } else {
                    allowance.setAdvalue(rs.getInt("O_VALUE"));
                }
                if (rs.getString("U_BT_ID") != null) {
                    allowance.setHead(rs.getString("U_BT_ID"));
                } else {
                    allowance.setHead(rs.getString("OBJECT_HEAD"));
                }
                allowanceList.add(allowance);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return allowanceList;
    }

    @Override
    public ArrayList getEmployeeWiseDeduction(String empId) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList deductionList = new ArrayList();
        try {

            String gpfAcctType = "GPF";
            String cadreCode = "";
            String gpfAccno = "";

            con = dataSource.getConnection();

            pstmt = con.prepareStatement(" select gpf_no, acct_type, cur_cadre_code, cadre_type from emp_mast \n"
                    + " left outer join g_cadre on emp_mast.cur_cadre_code=g_cadre.cadre_code\n"
                    + " where emp_id=? ");
            pstmt.setString(1, empId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                gpfAcctType = rs.getString("acct_type");
            }
            DataBaseFunctions.closeSqlObjects(rs, pstmt);

            pstmt = con.prepareStatement("SELECT G_AD_LIST.ad_code,ad_desc"
                    + ",G_AD_LIST.ad_formula as O_Formula,UPDATE_AD_INFO.ad_formula as U_Formula"
                    + ",G_AD_LIST.fixedvalue as O_VALUE,UPDATE_AD_INFO.fixedvalue as U_VALUE"
                    + ",AD_CODE_NAME, G_AD_LIST.bt_id AS g_bt_id, UPDATE_AD_INFO.bt_id AS u_bt_id FROM G_AD_LIST "
                    + "LEFT OUTER JOIN (SELECT * FROM UPDATE_AD_INFO WHERE where_updated = 'E' AND UPDATION_REF_CODE=?)UPDATE_AD_INFO ON G_AD_LIST.AD_CODE=UPDATE_AD_INFO.REF_AD_CODE "
                    + "WHERE AD_TYPE='D' AND IS_PRIVATE_DEDN='N' order by ad_desc ");
            pstmt.setString(1, empId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                AllowanceDeduction deduction = new AllowanceDeduction();
                deduction.setAdcode(rs.getString("ad_code"));
                deduction.setAddesc(rs.getString("ad_desc"));
                deduction.setAdcodename(rs.getString("AD_CODE_NAME"));
                if (rs.getString("U_Formula") != null) {
                    deduction.setFormula(rs.getString("U_Formula"));
                } else {
                    deduction.setFormula(rs.getString("O_Formula"));
                }
                if (rs.getString("U_VALUE") != null) {
                    deduction.setAdvalue(rs.getInt("U_VALUE"));
                } else {
                    deduction.setAdvalue(rs.getInt("O_VALUE"));
                }
                if (rs.getString("u_bt_id") != null) {
                    deduction.setHead(rs.getString("u_bt_id"));
                } else {
                    deduction.setHead(rs.getString("g_bt_id"));
                }

                if (gpfAcctType.equalsIgnoreCase("GPF") && !rs.getString("AD_CODE_NAME").equals("TPF")
                        && !rs.getString("AD_CODE_NAME").equals("CPF") && !rs.getString("AD_CODE_NAME").equals("EPFDED")
                        && !rs.getString("AD_CODE_NAME").equals("TLIC")) {
                    deductionList.add(deduction);
                } else if (gpfAcctType.equalsIgnoreCase("PRAN") && !rs.getString("AD_CODE_NAME").equals("TPF")
                        && !rs.getString("AD_CODE_NAME").equals("GPF") && !rs.getString("AD_CODE_NAME").equals("EPFDED")
                        && !rs.getString("AD_CODE_NAME").equals("TLIC") && !rs.getString("AD_CODE_NAME").equals("GPDD")
                        && !rs.getString("AD_CODE_NAME").equals("GPIR")) {

                    deductionList.add(deduction);
                } else if (gpfAcctType.equalsIgnoreCase("TPF") && !rs.getString("AD_CODE_NAME").equals("GPF")
                        && !rs.getString("AD_CODE_NAME").equals("EPFDED") && !rs.getString("AD_CODE_NAME").equals("LIC")
                        && !rs.getString("AD_CODE_NAME").equals("GPDD") && !rs.getString("AD_CODE_NAME").equals("GPIR")
                        && !rs.getString("AD_CODE_NAME").equals("CPF")) {
                    deductionList.add(deduction);
                } else if (gpfAcctType.equalsIgnoreCase("EPF") && !rs.getString("AD_CODE_NAME").equals("GPF")
                        && !rs.getString("AD_CODE_NAME").equals("TPF") && !rs.getString("AD_CODE_NAME").equals("TLIC")
                        && !rs.getString("AD_CODE_NAME").equals("GPDD") && !rs.getString("AD_CODE_NAME").equals("GPIR")
                        && !rs.getString("AD_CODE_NAME").equals("CPF")) {
                    deductionList.add(deduction);
                } else if (gpfAcctType.equalsIgnoreCase("GIA") && !rs.getString("AD_CODE_NAME").equals("GPF")
                        && !rs.getString("AD_CODE_NAME").equals("TPF") && !rs.getString("AD_CODE_NAME").equals("TLIC")
                        && !rs.getString("AD_CODE_NAME").equals("GPDD") && !rs.getString("AD_CODE_NAME").equals("GPIR")
                        && !rs.getString("AD_CODE_NAME").equals("CPF") && !rs.getString("AD_CODE_NAME").equals("EPF")) {
                    deductionList.add(deduction);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return deductionList;
    }

    @Override
    public ArrayList getEmployeeWisePvtDeduction(String empId) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList pvtDeductionList = new ArrayList();
        String gpfAcctType = "GPF";
        try {
            con = dataSource.getConnection();

            pstmt = con.prepareStatement(" select gpf_no, acct_type, cur_cadre_code, cadre_type from emp_mast \n"
                    + " left outer join g_cadre on emp_mast.cur_cadre_code=g_cadre.cadre_code\n"
                    + " where emp_id=? ");
            pstmt.setString(1, empId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                gpfAcctType = rs.getString("acct_type");
            }
            DataBaseFunctions.closeSqlObjects(rs, pstmt);

            pstmt = con.prepareStatement("SELECT G_AD_LIST.ad_code,ad_desc,G_AD_LIST.ad_formula as O_Formula,UPDATE_AD_INFO.ad_formula as U_Formula,G_AD_LIST.fixedvalue as O_VALUE,UPDATE_AD_INFO.fixedvalue as U_VALUE,AD_CODE_NAME FROM G_AD_LIST "
                    + "LEFT OUTER JOIN (SELECT * FROM UPDATE_AD_INFO WHERE where_updated = 'E' AND UPDATION_REF_CODE=?)UPDATE_AD_INFO ON G_AD_LIST.AD_CODE=UPDATE_AD_INFO.REF_AD_CODE "
                    + "WHERE AD_TYPE='D' AND IS_PRIVATE_DEDN='Y'  order by ad_desc ");
            pstmt.setString(1, empId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                AllowanceDeduction pvtdeduction = new AllowanceDeduction();

                pvtdeduction.setAdcode(rs.getString("ad_code"));
                pvtdeduction.setAddesc(rs.getString("ad_desc"));
                pvtdeduction.setAdcodename(rs.getString("AD_CODE_NAME"));
                if (rs.getString("U_Formula") != null) {
                    pvtdeduction.setFormula(rs.getString("U_Formula"));
                } else {
                    pvtdeduction.setFormula(rs.getString("O_Formula"));
                }
                if (rs.getString("U_VALUE") != null) {
                    pvtdeduction.setAdvalue(rs.getInt("U_VALUE"));
                } else {
                    pvtdeduction.setAdvalue(rs.getInt("O_VALUE"));
                }
                if ((gpfAcctType.equalsIgnoreCase("GPF") || gpfAcctType.equalsIgnoreCase("TPF") || gpfAcctType.equalsIgnoreCase("PRAN") || gpfAcctType.equalsIgnoreCase("GIA")) && !rs.getString("AD_CODE_NAME").equals("EPFDED")) {
                    pvtDeductionList.add(pvtdeduction);
                } else if (gpfAcctType.equalsIgnoreCase("EPF")) {
                    pvtDeductionList.add(pvtdeduction);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return pvtDeductionList;
    }

    @Override
    public ArrayList getOfficeWiseAllowance(String empId) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList allowanceList = new ArrayList();
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT G_AD_LIST.ad_code as ref_ad_code,UPDATE_AD_INFO.ad_code"
                    + ",ad_desc,G_AD_LIST.ad_formula as O_Formula, UPDATE_AD_INFO.is_fixed"
                    + ",UPDATE_AD_INFO.ad_formula as U_Formula,G_AD_LIST.fixedvalue as O_VALUE"
                    + ",UPDATE_AD_INFO.fixedvalue as U_VALUE,AD_CODE_NAME, object_head"
                    + ", UPDATE_AD_INFO.bt_id AS u_bt_id, where_updated FROM G_AD_LIST "
                    + "LEFT OUTER JOIN (SELECT * FROM UPDATE_AD_INFO WHERE where_updated = 'O' AND UPDATION_REF_CODE=?)UPDATE_AD_INFO ON G_AD_LIST.AD_CODE=UPDATE_AD_INFO.REF_AD_CODE "
                    + "WHERE AD_TYPE='A' order by G_AD_LIST.ad_desc ");
            pstmt.setString(1, empId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                AllowanceDeduction allowance = new AllowanceDeduction();
                allowance.setAdtype("A");
                allowance.setAdcode(rs.getString("ad_code"));
                allowance.setRefadcode(rs.getString("ref_ad_code"));
                allowance.setAddesc(rs.getString("ad_desc"));
                allowance.setAdcodename(rs.getString("AD_CODE_NAME"));
                allowance.setWhereupdated(rs.getString("where_updated"));
                if (rs.getInt("is_fixed") == 0) {
                    if (rs.getString("U_Formula") != null && !rs.getString("U_Formula").equals("") && !rs.getString("U_Formula").equals("0")) {
                        allowance.setFormula(rs.getString("U_Formula"));
                    } else {
                        allowance.setFormula(rs.getString("O_Formula"));
                    }
                }
                if (rs.getString("U_VALUE") != null) {
                    allowance.setAdvalue(rs.getInt("U_VALUE"));
                } else {
                    allowance.setAdvalue(rs.getInt("O_VALUE"));
                }
                if (rs.getString("U_BT_ID") != null) {
                    allowance.setHead(rs.getString("U_BT_ID"));
                } else {
                    allowance.setHead(rs.getString("OBJECT_HEAD"));
                }
                allowanceList.add(allowance);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return allowanceList;
    }

    @Override
    public ArrayList getOfficeWiseDeduction(String empId) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList deductionList = new ArrayList();
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT G_AD_LIST.ad_code, UPDATE_AD_INFO.is_fixed"
                    + ",ad_desc,G_AD_LIST.ad_formula as O_Formula"
                    + ",UPDATE_AD_INFO.ad_formula as U_Formula,G_AD_LIST.fixedvalue as O_VALUE"
                    + ",UPDATE_AD_INFO.fixedvalue as U_VALUE"
                    + ",AD_CODE_NAME, UPDATE_AD_INFO.bt_id AS u_bt_id, G_AD_LIST.bt_id AS g_bt_id FROM G_AD_LIST "
                    + "LEFT OUTER JOIN (SELECT * FROM UPDATE_AD_INFO WHERE where_updated = 'O' AND UPDATION_REF_CODE=?)UPDATE_AD_INFO ON G_AD_LIST.AD_CODE=UPDATE_AD_INFO.REF_AD_CODE "
                    + "WHERE AD_TYPE='D' AND IS_PRIVATE_DEDN='N' order by G_AD_LIST.ad_desc ");
            pstmt.setString(1, empId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                AllowanceDeduction deduction = new AllowanceDeduction();
                deduction.setAdtype("D");
                deduction.setRefadcode(rs.getString("ad_code"));
                deduction.setAddesc(rs.getString("ad_desc"));
                deduction.setAdcodename(rs.getString("AD_CODE_NAME"));
                if (rs.getInt("is_fixed") == 0) {
                    if (rs.getString("U_Formula") != null) {
                        deduction.setFormula(rs.getString("U_Formula"));
                    } else {
                        deduction.setFormula(rs.getString("O_Formula"));
                    }
                }
                if (rs.getString("U_VALUE") != null) {
                    deduction.setAdvalue(rs.getInt("U_VALUE"));
                } else {
                    deduction.setAdvalue(rs.getInt("O_VALUE"));
                }
                if (rs.getString("u_bt_id") != null) {
                    deduction.setHead(rs.getString("u_bt_id"));
                } else {
                    deduction.setHead(rs.getString("g_bt_id"));
                }
                deductionList.add(deduction);

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return deductionList;
    }

    @Override
    public ArrayList getOfficeWisePvtDeduction(String empId) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList pvtDeductionList = new ArrayList();
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT G_AD_LIST.ad_code,ad_desc, UPDATE_AD_INFO.is_fixed"
                    + ",G_AD_LIST.ad_formula as O_Formula,UPDATE_AD_INFO.ad_formula as U_Formula"
                    + ",G_AD_LIST.fixedvalue as O_VALUE,UPDATE_AD_INFO.fixedvalue as U_VALUE"
                    + ",AD_CODE_NAME, G_AD_LIST.bt_id AS g_bt_id, UPDATE_AD_INFO.bt_id AS u_bt_id  FROM G_AD_LIST "
                    + "LEFT OUTER JOIN (SELECT * FROM UPDATE_AD_INFO WHERE where_updated = 'O' AND UPDATION_REF_CODE=?)UPDATE_AD_INFO ON G_AD_LIST.AD_CODE=UPDATE_AD_INFO.REF_AD_CODE "
                    + "WHERE AD_TYPE='D' AND IS_PRIVATE_DEDN='Y' order by G_AD_LIST.ad_desc ");
            pstmt.setString(1, empId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                AllowanceDeduction pvtdeduction = new AllowanceDeduction();
                pvtdeduction.setAdtype("P");
                pvtdeduction.setRefadcode(rs.getString("ad_code"));
                pvtdeduction.setAddesc(rs.getString("ad_desc"));
                pvtdeduction.setAdcodename(rs.getString("AD_CODE_NAME"));
                if (rs.getInt("is_fixed") == 0) {
                    if (rs.getString("U_Formula") != null) {
                        pvtdeduction.setFormula(rs.getString("U_Formula"));
                    } else {
                        pvtdeduction.setFormula(rs.getString("O_Formula"));
                    }
                }
                if (rs.getString("U_VALUE") != null) {
                    pvtdeduction.setAdvalue(rs.getInt("U_VALUE"));
                } else {
                    pvtdeduction.setAdvalue(rs.getInt("O_VALUE"));
                }
                if (rs.getString("u_bt_id") != null) {
                    pvtdeduction.setHead(rs.getString("u_bt_id"));
                } else {
                    pvtdeduction.setHead(rs.getString("g_bt_id"));
                }

                pvtDeductionList.add(pvtdeduction);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return pvtDeductionList;
    }

    @Override
    public AllowanceDeduction getAllowanceDeductionDetail(String adcode, String updationRefCode, String whereUpdated) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        AllowanceDeduction ad = new AllowanceDeduction();
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT G_AD_LIST.ad_code, UPDATE_AD_INFO.is_fixed, AD_TYPE"
                    + ",ad_desc,G_AD_LIST.ad_formula as O_Formula"
                    + ",UPDATE_AD_INFO.ad_formula as U_Formula,G_AD_LIST.fixedvalue as O_VALUE"
                    + ",UPDATE_AD_INFO.fixedvalue as U_VALUE"
                    + ",AD_CODE_NAME, UPDATE_AD_INFO.bt_id AS u_bt_id, G_AD_LIST.bt_id AS g_bt_id"
                    + ",G_AD_LIST.object_head AS g_object_head"
                    + " FROM G_AD_LIST "
                    + "LEFT OUTER JOIN (SELECT * FROM UPDATE_AD_INFO WHERE where_updated = '" + whereUpdated + "'"
                    + " AND UPDATION_REF_CODE=?) UPDATE_AD_INFO ON G_AD_LIST.AD_CODE=UPDATE_AD_INFO.REF_AD_CODE "
                    + "WHERE G_AD_LIST.ad_code=?  order by G_AD_LIST.ad_desc");
            pstmt.setString(1, updationRefCode);
            pstmt.setString(2, adcode);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                ad.setAdcode(rs.getString("ad_code"));
                ad.setAddesc(rs.getString("AD_DESC"));
                ad.setAdcodename(rs.getString("AD_CODE_NAME"));
                ad.setIsfixed(rs.getInt("is_fixed"));
                ad.setAdtype(rs.getString("AD_TYPE"));
                if (rs.getString("AD_TYPE").equals("A")) {
                    if (rs.getString("u_bt_id") != null) {
                        ad.setHead(rs.getString("u_bt_id"));
                    } else {
                        ad.setHead(rs.getString("g_object_head"));
                    }
                } else {
                    if (rs.getString("u_bt_id") != null) {
                        ad.setHead(rs.getString("u_bt_id"));
                    } else {
                        ad.setHead(rs.getString("g_bt_id"));
                    }
                }
                if (rs.getString("U_Formula") != null) {
                    ad.setFormula(rs.getString("U_Formula"));
                } else {
                    ad.setFormula(rs.getString("O_Formula"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return ad;
    }

    @Override
    public Message deleteAllowanceDeductionDetail(AllowanceDeduction adBean) {
        Connection con = null;
        PreparedStatement pstmt = null;
        Message msg = new Message();
        msg.setStatus("Success");
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("DELETE FROM UPDATE_AD_INFO WHERE REF_AD_CODE=? AND WHERE_UPDATED=? AND UPDATION_REF_CODE=?");
            pstmt.setString(1, adBean.getAdcode());
            pstmt.setString(2, adBean.getWhereupdated());
            pstmt.setString(3, adBean.getUpdationRefCode());
            pstmt.executeUpdate();
        } catch (Exception e) {
            msg.setStatus("Error");
            msg.setMessage(e.getMessage());
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return msg;
    }

    @Override
    public Message saveAllowanceDeductionDetail(AllowanceDeduction adbean) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String nCode = null;
        Message msg = new Message();
        msg.setStatus("Success");
        try {
            con = dataSource.getConnection();
            if (adbean.getWhereupdated() != null && adbean.getWhereupdated().equals("G")) {
                con = dataSource.getConnection();
                pstmt = con.prepareStatement("SELECT MAX(CAST(AD_CODE as Integer)) AD_CODE FROM UPDATE_AD_INFO");
                rs = pstmt.executeQuery();
                if (rs.next()) {
                    nCode = rs.getString("AD_CODE");
                    if (nCode != null) {
                        nCode = "" + (Integer.parseInt(nCode) + 1);
                        if (nCode.length() < 2) {
                            nCode = "0" + nCode;
                        }
                    } else {
                        nCode = "01";
                    }
                }
                pstmt = con.prepareStatement("INSERT INTO UPDATE_AD_INFO (REF_AD_CODE, AD_CODE, AD_FORMULA, IS_FIXED,FIXEDVALUE,AD_ORGFORMULA,WHERE_UPDATED,UPDATION_REF_CODE,BT_ID) values (?,?,?,?,?,?,?,?,?)");
                pstmt.setString(1, adbean.getAdcode());
                pstmt.setString(2, nCode);
                if (adbean.getIsfixed() == 1) {
                    pstmt.setString(3, null);
                    pstmt.setInt(4, 1);
                    pstmt.setInt(5, adbean.getAdvalue());
                    pstmt.setString(6, null);
                } else {
                    String str[] = adbean.getFormula().split(",");
                    pstmt.setString(3, str[1]);
                    pstmt.setInt(4, 0);
                    pstmt.setString(5, null);
                    pstmt.setString(6, str[0]);
                }
                pstmt.setString(7, "O");
                pstmt.setString(8, adbean.getUpdationRefCode());
                pstmt.setString(9, adbean.getHead());
                pstmt.executeUpdate();
            } else if (adbean.getWhereupdated() != null && adbean.getWhereupdated().equals("O")) {

            } else if (adbean.getWhereupdated() != null && adbean.getWhereupdated().equals("E")) {
                pstmt = con.prepareStatement("SELECT * FROM UPDATE_AD_INFO WHERE REF_AD_CODE=? AND WHERE_UPDATED=? AND UPDATION_REF_CODE=?");
                pstmt.setString(1, adbean.getAdcode());
                pstmt.setString(2, adbean.getWhereupdated());
                pstmt.setString(3, adbean.getUpdationRefCode());
                rs = pstmt.executeQuery();
                boolean recordFound = false;
                if (rs.next()) {
                    recordFound = true;
                }
                if (recordFound == false) {
                    pstmt = con.prepareStatement("INSERT INTO UPDATE_AD_INFO (REF_AD_CODE, AD_FORMULA, IS_FIXED,FIXEDVALUE,AD_ORGFORMULA,WHERE_UPDATED,UPDATION_REF_CODE,BT_ID) values (?,?,?,?,?,?,?,?)");
                    pstmt.setString(1, adbean.getAdcode());
                    if (adbean.getAdamttype().equals("1")) {
                        pstmt.setString(2, null);
                        pstmt.setInt(3, 1);
                        pstmt.setInt(4, adbean.getAdvalue());
                        pstmt.setString(5, null);
                    } else if (adbean.getAdamttype().equals("0")) {
                        Formula formula = getFormulaDetail(adbean.getAdcode(), adbean.getFormula());
                        pstmt.setString(2, formula.getFormulaName());
                        pstmt.setInt(3, 0);
                        pstmt.setInt(4, 0);
                        pstmt.setString(5, formula.getOrgformula());
                    }
                    pstmt.setString(6, adbean.getWhereupdated());
                    pstmt.setString(7, adbean.getUpdationRefCode());
                    pstmt.setString(8, adbean.getHead());
                    pstmt.executeUpdate();
                } else if (recordFound == true) {
                    if (adbean.getAdamttype().equals("1")) {
                        pstmt = con.prepareStatement("UPDATE UPDATE_AD_INFO SET FIXEDVALUE=?,IS_FIXED=1,AD_FORMULA=null,AD_ORGFORMULA=null, bt_id = ? WHERE REF_AD_CODE=? AND WHERE_UPDATED=? AND UPDATION_REF_CODE=?");
                        pstmt.setInt(1, adbean.getAdvalue());
                        pstmt.setString(2, adbean.getHead());
                        pstmt.setString(3, adbean.getAdcode());
                        pstmt.setString(4, adbean.getWhereupdated());
                        pstmt.setString(5, adbean.getUpdationRefCode());
                        pstmt.executeUpdate();
                    } else if (adbean.getAdamttype().equals("0")) {
                        Formula formula = getFormulaDetail(adbean.getAdcode(), adbean.getFormula());
                        pstmt = con.prepareStatement("UPDATE UPDATE_AD_INFO SET FIXEDVALUE=0,IS_FIXED=0,AD_FORMULA=?,AD_ORGFORMULA=? WHERE REF_AD_CODE=? AND WHERE_UPDATED=? AND UPDATION_REF_CODE=?");
                        pstmt.setString(1, formula.getFormulaName());
                        pstmt.setString(2, formula.getOrgformula());
                        pstmt.setString(3, adbean.getAdcode());
                        pstmt.setString(4, adbean.getWhereupdated());
                        pstmt.setString(5, adbean.getUpdationRefCode());
                        pstmt.executeUpdate();
                    }

                }
            }
        } catch (Exception e) {
            msg.setStatus("Error");
            msg.setMessage(e.getMessage());
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return msg;
    }

    @Override
    public Message saveOfficeAllowanceDeductionDetail(AllowanceDeduction adbean) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String nCode = null;
        Message msg = new Message();
        msg.setStatus("Success");
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT * FROM UPDATE_AD_INFO WHERE REF_AD_CODE=? AND WHERE_UPDATED=? AND UPDATION_REF_CODE=?");
            pstmt.setString(1, adbean.getAdcode());
            pstmt.setString(2, adbean.getWhereupdated());
            pstmt.setString(3, adbean.getUpdationRefCode());
            rs = pstmt.executeQuery();
            boolean recordFound = false;
            if (rs.next()) {
                recordFound = true;
            }
            if (recordFound == false) {
                pstmt = con.prepareStatement("INSERT INTO UPDATE_AD_INFO (REF_AD_CODE, AD_FORMULA, IS_FIXED,FIXEDVALUE,AD_ORGFORMULA,WHERE_UPDATED,UPDATION_REF_CODE,BT_ID) values (?,?,?,?,?,?,?,?)");
                pstmt.setString(1, adbean.getAdcode());
                if (adbean.getAdamttype().equals("1")) {
                    pstmt.setString(2, null);
                    pstmt.setInt(3, 1);
                    pstmt.setInt(4, adbean.getAdvalue());
                    pstmt.setString(5, null);
                } else if (adbean.getAdamttype().equals("0")) {
                    String str[] = adbean.getFormula().split(",");
                    pstmt.setString(2, adbean.getFormula());
                    pstmt.setInt(3, 0);
                    pstmt.setInt(4, 0);
                    pstmt.setString(5, adbean.getFormula());
                }
                pstmt.setString(6, adbean.getWhereupdated());
                pstmt.setString(7, adbean.getUpdationRefCode());
                pstmt.setString(8, adbean.getHead());
                pstmt.executeUpdate();
            } else if (recordFound == true) {
                pstmt = con.prepareStatement("UPDATE UPDATE_AD_INFO SET FIXEDVALUE=?"
                        + ", ad_formula=?, is_fixed=?, bt_id = ? WHERE REF_AD_CODE=?"
                        + " AND WHERE_UPDATED=? AND UPDATION_REF_CODE=?");
                if (adbean.getAdamttype().equals("1")) {
                    pstmt.setInt(1, adbean.getAdvalue());
                    pstmt.setString(2, "");
                }
                if (adbean.getAdamttype().equals("0")) {
                    pstmt.setInt(1, 0);
                    pstmt.setString(2, adbean.getFormula());
                }

                pstmt.setInt(3, Integer.parseInt(adbean.getAdamttype()));
                pstmt.setString(4, adbean.getHead());
                pstmt.setString(5, adbean.getAdcode());
                pstmt.setString(6, adbean.getWhereupdated());
                pstmt.setString(7, adbean.getUpdationRefCode());
                pstmt.executeUpdate();
            }
        } catch (Exception e) {
            msg.setStatus("Error");
            msg.setMessage(e.getMessage());
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return msg;
    }

    @Override
    public AllowanceDeduction getUpdatedAllowanceDeductionDetail(String adcode) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        AllowanceDeduction ad = new AllowanceDeduction();
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT UPDATE_AD_INFO.ad_code,AD_DESC,AD_CODE_NAME,UPDATE_AD_INFO.fixedvalue,UPDATE_AD_INFO.is_fixed,AD_TYPE,OBJECT_HEAD,UPDATE_AD_INFO.BT_ID FROM (SELECT * FROM UPDATE_AD_INFO WHERE where_updated = 'O' AND ad_code=?)UPDATE_AD_INFO INNER JOIN g_ad_list ON UPDATE_AD_INFO.ref_ad_code = g_ad_list.ad_code");
            pstmt.setString(1, adcode);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                ad.setAdcode(rs.getString("ad_code"));
                ad.setAddesc(rs.getString("AD_DESC"));
                ad.setAdcodename(rs.getString("AD_CODE_NAME"));
                ad.setAdvalue(rs.getInt("fixedvalue"));
                ad.setIsfixed(rs.getInt("is_fixed"));
                if (rs.getString("AD_TYPE").equals("A")) {
                    ad.setHead(rs.getString("OBJECT_HEAD"));
                } else {
                    ad.setHead(rs.getString("BT_ID"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return ad;
    }

    @Override
    public AllowanceDeduction getUpdatedAllowanceDeduction(AllowanceDeduction adBean) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT fixedvalue,IS_FIXED, ad_formula FROM UPDATE_AD_INFO WHERE REF_AD_CODE=? AND WHERE_UPDATED=? AND UPDATION_REF_CODE=?");
            pstmt.setString(1, adBean.getAdcode());
            pstmt.setString(2, adBean.getWhereupdated());
            pstmt.setString(3, adBean.getUpdationRefCode());
            rs = pstmt.executeQuery();
            if (rs.next()) {
                adBean.setAdvalue(rs.getInt("fixedvalue"));
                adBean.setAdamttype(rs.getString("IS_FIXED"));
                adBean.setFormula(rs.getString("ad_formula"));
                adBean.setIsupdated(1);
            } else {
                adBean.setIsupdated(0);
                adBean.setAdvalue(0);
                adBean.setAdamttype("0");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return adBean;
    }

    public ADOBJECT getAdAmt(SectionDtlSPCWiseEmp sdswe, String adcode, String offCode, int aqday, int payday, int workday, int traing_days, PayComponent payComponent, int aqmonth, int aqyear, String v_depcode) {
        int v_finaladAmt = 0;
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String v_adconfigured = "N";
        int v_adamt = 0;
        int v_fixed = 0;
        String v_ad_orgformula = "";
        String v_btId = "";
        int v_reccnt = 0;
        String v_gpc = "";
        String v_daformula = "";
        try {
            con = dataSource.getConnection();
            if (v_adconfigured.equals("N")) {

                /* Allowance or Deduction amount is updated at the Employee End */
                pstmt = con.prepareStatement("SELECT FIXEDVALUE,IS_FIXED,AD_ORGFORMULA,BT_ID FROM UPDATE_AD_INFO WHERE UPDATION_REF_CODE = ? AND "
                        + "WHERE_UPDATED = 'E'  AND REF_AD_CODE = ?");
                pstmt.setString(1, sdswe.getEmpid());
                pstmt.setString(2, adcode);
                rs = pstmt.executeQuery();

                if (rs.next()) {
                    v_reccnt = 1;
                    v_adamt = rs.getInt("FIXEDVALUE");
                    v_fixed = rs.getInt("IS_FIXED");
                    v_ad_orgformula = rs.getString("AD_ORGFORMULA");
                    v_btId = rs.getString("BT_ID");
                    if (v_fixed == 1) {
                        v_finaladAmt = v_adamt;
                    } else {
                        v_finaladAmt = calculate(v_ad_orgformula, sdswe.getEmpid(), aqmonth, aqyear);
                    }
                    v_adconfigured = "Y";
                }
                pstmt.close();
                pstmt = null;
            }

            /* Allowance or Deduction amount is updated at the POST End */
            if (v_finaladAmt == 0 && v_adconfigured.equals("N") && sdswe.getSpc() != null) {
                pstmt = con.prepareStatement("SELECT GPC FROM G_SPC WHERE SPC=?");
                pstmt.setString(1, sdswe.getSpc());
                rs = pstmt.executeQuery();
                if (rs.next()) {
                    v_gpc = rs.getString("GPC");
                }

                pstmt = con.prepareStatement("SELECT FIXEDVALUE,IS_FIXED,AD_ORGFORMULA FROM UPDATE_AD_INFO WHERE "
                        + "SUBSTR(UPDATION_REF_CODE,0,14) = ? AND SUBSTR(UPDATION_REF_CODE,15) =? AND WHERE_UPDATED = 'P'  AND"
                        + " REF_AD_CODE = ?");
                pstmt.setString(1, offCode);
                pstmt.setString(2, v_gpc);
                pstmt.setString(3, adcode);
                rs = pstmt.executeQuery();
                if (rs.next()) {
                    v_reccnt = 1;
                    v_adamt = rs.getInt("FIXEDVALUE");
                    v_fixed = rs.getInt("IS_FIXED");
                    v_ad_orgformula = rs.getString("AD_ORGFORMULA");
                    if (v_fixed == 1) {
                        v_finaladAmt = v_adamt;
                    } else {

                    }
                    v_adconfigured = "Y";
                }
                pstmt.close();
                pstmt = null;
            }
            /* Allowance or Deduction amount is updated at the Office End */
            if (v_finaladAmt == 0 && v_adconfigured.equals("N")) {
                pstmt = con.prepareStatement("SELECT REF_AD_CODE, FIXEDVALUE,IS_FIXED,AD_ORGFORMULA,BT_ID, ORA_AD_ORGFORMULA FROM UPDATE_AD_INFO WHERE UPDATION_REF_CODE = ? AND WHERE_UPDATED = 'O' AND REF_AD_CODE = ?");
                pstmt.setString(1, offCode);
                pstmt.setString(2, adcode);
                rs = pstmt.executeQuery();
                if (rs.next()) {
                    v_reccnt = 1;
                    v_adamt = rs.getInt("FIXEDVALUE");
                    v_fixed = rs.getInt("IS_FIXED");
                    v_ad_orgformula = rs.getString("AD_ORGFORMULA");
                    v_btId = rs.getString("BT_ID");

                    if (v_fixed == 1) {
                        v_finaladAmt = v_adamt;
                    } else {
                        if (rs.getString("REF_AD_CODE") != null && !rs.getString("REF_AD_CODE").equals("") && rs.getString("REF_AD_CODE").equals("54")) {
                            v_finaladAmt = 0;
                        } else if (rs.getString("ORA_AD_ORGFORMULA") != null && !rs.getString("ORA_AD_ORGFORMULA").equals("")) {
                            v_finaladAmt = new Long(Math.round(((payComponent.getBasic() + payComponent.getGp()) * Double.parseDouble(rs.getString("ORA_AD_ORGFORMULA"))))).intValue();
                        }
                    }
                    v_adconfigured = "Y";
                }
                pstmt.close();
                pstmt = null;
            }

            /* Allowance or Deduction amount is Calculated From Global Formula */
            if (adcode.equals("56")) {
            }
            if (v_reccnt == 0) {
                if (v_finaladAmt == 0 && v_adconfigured.equals("N")) {

                    pstmt = con.prepareStatement("SELECT FIXEDVALUE,IS_FIXED,AD_ORGFORMULA,BT_ID,SCHEDULE,OBJECT_HEAD,AD_TYPE FROM G_AD_LIST WHERE AD_CODE = ?");
                    pstmt.setString(1, adcode);
                    rs = pstmt.executeQuery();
                    if (rs.next()) {
                        v_reccnt = 1;
                        v_adamt = rs.getInt("FIXEDVALUE");
                        v_fixed = rs.getInt("IS_FIXED");
                        v_ad_orgformula = rs.getString("AD_ORGFORMULA");
                        if (rs.getString("AD_TYPE") != null && rs.getString("AD_TYPE").equals("A")) {
                            v_btId = rs.getString("OBJECT_HEAD");
                        } else {
                            v_btId = rs.getString("BT_ID");
                        }
                        if (adcode.equals("52")) {
                            if (payComponent.getPaycommission() == 5) {
                                if (aqyear >= 2017) {
                                    v_finaladAmt = new Long(Math.round(((payComponent.getBasic() + payComponent.getDp()) * 2.64))).intValue();
                                    v_daformula = "(BASIC+DP)*(264/100)";
                                }
                            } else if (payComponent.getPaycommission() == 6) {

                                if ((aqmonth >= 10 && aqyear == 2023) || aqyear == 2024) {
                                    float da_f = (float) ((payComponent.getBasic() + payComponent.getGp()) * 2.30);
                                    v_finaladAmt = Math.round(da_f);
                                    v_daformula = "(BASIC+GP)*(230/100)";
                                } else if ((aqmonth > 4 && aqmonth < 10 && aqyear == 2023)) {
                                    float da_f = (float) ((payComponent.getBasic() + payComponent.getGp()) * 2.21);
                                    v_finaladAmt = Math.round(da_f);
                                    v_daformula = "(BASIC+GP)*(221/100)";
                                } else if ((aqmonth <= 4 && aqyear == 2023)) {
                                    float da_f = (float) ((payComponent.getBasic() + payComponent.getGp()) * 2.12);
                                    v_finaladAmt = Math.round(da_f);
                                    v_daformula = "(BASIC+GP)*(212/100)";
                                } else if ((aqmonth >= 8 && aqyear == 2022)) {
                                    float da_f = (float) ((payComponent.getBasic() + payComponent.getGp()) * 2.03);
                                    v_finaladAmt = Math.round(da_f);
                                    //v_finaladAmt = new Long(Math.round(((payComponent.getBasic() + payComponent.getGp()) * 2.03))).intValue();
                                    v_daformula = "(BASIC+GP)*(203/100)";
                                } else if ((aqmonth < 8 && aqyear == 2022)) {
                                    v_finaladAmt = new Long(Math.round(((payComponent.getBasic() + payComponent.getGp()) * 1.96))).intValue();
                                    v_daformula = "(BASIC+GP)*(196/100)";
                                } else if ((aqmonth >= 9 && aqyear == 2021)) {
                                    v_finaladAmt = new Long(Math.round(((payComponent.getBasic() + payComponent.getGp()) * 1.89))).intValue();
                                    v_daformula = "(BASIC+GP)*(189/100)";
                                } else if ((aqmonth >= 6 && aqyear == 2019) || (aqmonth >= 0 && aqyear >= 2020)) {
                                    v_finaladAmt = new Long(Math.round(((payComponent.getBasic() + payComponent.getGp()) * 1.64))).intValue();
                                    v_daformula = "(BASIC+GP)*(164/100)";
                                } else if ((aqmonth >= 6 && aqyear == 2018) || (aqmonth <= 6 && aqyear == 2019)) {
                                    v_finaladAmt = new Long(Math.round(((payComponent.getBasic() + payComponent.getGp()) * 1.54))).intValue();
                                    v_daformula = "(BASIC+GP)*(154/100)";
                                } else if (aqmonth >= 3 && aqmonth <= 7 && aqyear < 2019) {
                                    v_finaladAmt = new Long(Math.round(((payComponent.getBasic() + payComponent.getGp()) * 1.42))).intValue();
                                    v_daformula = "(BASIC+GP)*(142/100)";
                                } else {
                                    v_finaladAmt = new Long(Math.round(((payComponent.getBasic() + payComponent.getGp()) * 1.39))).intValue();
                                    v_daformula = "(BASIC+GP)*(139/100)";
                                }

                            } else if (payComponent.getPaycommission() == 7) {
                                if (aqmonth >= 2 && aqyear == 2024) {
                                    v_finaladAmt = new Long(Math.round(payComponent.getBasic() * 0.50)).intValue();
                                    v_daformula = "(BASIC)*(50/100)";
                                } else if ((aqmonth > 8 && aqyear == 2023 || aqmonth < 2 && aqyear == 2024)) {
                                    v_finaladAmt = new Long(Math.round(payComponent.getBasic() * 0.46)).intValue();
                                    v_daformula = "(BASIC)*(46/100)";
                                } else if ((aqmonth > 4 && aqmonth <= 8 && aqyear == 2023)) {
                                    v_finaladAmt = new Long(Math.round(payComponent.getBasic() * 0.42)).intValue();
                                    v_daformula = "(BASIC)*(42/100)";
                                } else if ((aqmonth <= 4 && aqyear == 2023)) {
                                    v_finaladAmt = new Long(Math.round(payComponent.getBasic() * 0.38)).intValue();
                                    v_daformula = "(BASIC)*(38/100)";
                                } else if ((aqmonth >= 8 && aqyear == 2022)) {
                                    v_finaladAmt = new Long(Math.round(payComponent.getBasic() * 0.34)).intValue();
                                    v_daformula = "(BASIC)*(34/100)";
                                } else if ((aqmonth < 8 && aqyear == 2022)) {
                                    v_finaladAmt = new Long(Math.round(payComponent.getBasic() * 0.31)).intValue();
                                    v_daformula = "(BASIC)*(31/100)";
                                } else if ((aqmonth >= 9 && aqyear == 2021)) {
                                    v_finaladAmt = new Long(Math.round(payComponent.getBasic() * 0.28)).intValue();
                                    v_daformula = "(BASIC)*(28/100)";
                                } else if ((aqmonth >= 6 && aqyear == 2019) || (aqmonth >= 0 && aqyear >= 2020)) {
                                    v_finaladAmt = new Long(Math.round(payComponent.getBasic() * 0.17)).intValue();
                                    v_daformula = "(BASIC)*(17/100)";
                                } else if (aqmonth <= 5 && aqyear == 2019) {
                                    v_finaladAmt = new Long(Math.round(payComponent.getBasic() * 0.12)).intValue();
                                    v_daformula = "(BASIC)*(12/100)";
                                } else if ((aqmonth > 7 && aqmonth <= 11 && aqyear == 2018) || (aqmonth < 2 && aqyear == 2019)) {
                                    v_finaladAmt = new Long(Math.round(payComponent.getBasic() * 0.09)).intValue();
                                    v_daformula = "(BASIC)*(9/100)";
                                } else if (aqmonth >= 3 && aqmonth <= 7 && aqyear == 2018) {
                                    v_finaladAmt = new Long(Math.round(payComponent.getBasic() * 0.07)).intValue();
                                    v_daformula = "(BASIC)*(7/100)";
                                } else {
                                    v_finaladAmt = new Long(Math.round(payComponent.getBasic() * 0.05)).intValue();
                                    v_daformula = "(BASIC)*(5/100)";
                                }
                            }

                            if (v_depcode.equals("05")) {
                                pstmt = con.prepareStatement("SELECT FIXEDVALUE,IS_FIXED,AD_ORGFORMULA,BT_ID FROM UPDATE_AD_INFO WHERE UPDATION_REF_CODE = ? AND WHERE_UPDATED = 'E' AND REF_AD_CODE = ?");
                                pstmt.setString(1, sdswe.getEmpid());
                                pstmt.setString(2, "52");
                                rs = pstmt.executeQuery();
                                if (rs.next()) {
                                    v_adamt = rs.getInt("FIXEDVALUE");
                                    v_fixed = rs.getInt("IS_FIXED");
                                    v_btId = rs.getString("BT_ID");
                                    if (v_fixed == 1) {
                                        v_finaladAmt = v_adamt;
                                    } else {

                                    }
                                }
                                pstmt.close();
                                pstmt = null;
                            }
                        } else {
                            if (v_fixed == 1) {
                                v_finaladAmt = v_adamt;
                            } else {

                            }
                        }
                        v_adconfigured = "Y";
                    }

                    if (pstmt != null) {
                        pstmt.close();
                        pstmt = null;
                    }

                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        if (adcode.equals("56")) {
        }
        ADOBJECT adObject = new ADOBJECT();
        adObject.setAdamt(v_finaladAmt);
        adObject.setBtid(v_btId);
        adObject.setDaformula(v_daformula);
        return adObject;
    }

    private int calculate(String oformula, String empid, int month, int year) {
        Connection con = null;
        Statement stmt = null;
        ResultSet res = null;
        int advalue = 0;
        try {
            con = dataSource.getConnection();
            if (oformula != null) {
                String sql = oformula.replaceAll("@empid", empid);
                sql = sql.replaceAll("@month", String.valueOf(month));
                sql = sql.replaceAll("@year", String.valueOf(year));
                stmt = con.createStatement();
                res = stmt.executeQuery(sql);
                if (res.next()) {
                    advalue = new Double(Math.round(res.getDouble("ADVALUE"))).intValue();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(res, stmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return advalue;
    }

    public ADOBJECT getAdAmt(String curspc, String empCode, String adcode, String offCode, int aqday, int payday, int workday, int traing_days, PayComponent payComponent) {
        int v_finaladAmt = 0;
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String v_adconfigured = "N";
        int v_adamt = 0;
        int v_fixed = 0;
        String v_ad_orgformula = "";
        String v_btId = "";
        int v_reccnt = 0;
        String v_gpc = "";
        try {
            con = dataSource.getConnection();
            if (v_adconfigured.equals("N")) {
                /* Allowance or Deduction amount is updated at the Employee End */
                pstmt = con.prepareStatement("SELECT FIXEDVALUE,IS_FIXED,AD_ORGFORMULA,BT_ID FROM UPDATE_AD_INFO WHERE UPDATION_REF_CODE = ? AND "
                        + "WHERE_UPDATED = 'E'  AND REF_AD_CODE = ?");
                pstmt.setString(1, empCode);
                pstmt.setString(2, adcode);
                rs = pstmt.executeQuery();

                if (rs.next()) {
                    v_reccnt = 1;
                    v_adamt = rs.getInt("FIXEDVALUE");
                    v_fixed = rs.getInt("IS_FIXED");
                    v_ad_orgformula = rs.getString("AD_ORGFORMULA");
                    v_btId = rs.getString("BT_ID");
                    if (v_fixed == 1) {
                        v_finaladAmt = v_adamt;
                    } else {

                    }
                    v_adconfigured = "Y";
                }
                pstmt.close();
                pstmt = null;
            }
            /* Allowance or Deduction amount is updated at the POST End */
            if (v_finaladAmt == 0 && v_adconfigured.equals("N") && curspc != null) {

                pstmt = con.prepareStatement("SELECT GPC FROM G_SPC WHERE SPC=?");
                pstmt.setString(1, curspc);
                rs = pstmt.executeQuery();
                if (rs.next()) {
                    v_gpc = rs.getString("GPC");
                }
                pstmt = con.prepareStatement("SELECT FIXEDVALUE,IS_FIXED,AD_ORGFORMULA FROM UPDATE_AD_INFO WHERE "
                        + "SUBSTR(UPDATION_REF_CODE,0,14) = ? AND SUBSTR(UPDATION_REF_CODE,15) =? AND WHERE_UPDATED = 'P'  AND"
                        + " REF_AD_CODE = ?");
                pstmt.setString(1, offCode);
                pstmt.setString(2, v_gpc);
                pstmt.setString(3, adcode);
                rs = pstmt.executeQuery();
                if (rs.next()) {
                    v_reccnt = 1;
                    v_adamt = rs.getInt("FIXEDVALUE");
                    v_fixed = rs.getInt("IS_FIXED");
                    v_ad_orgformula = rs.getString("AD_ORGFORMULA");
                    if (v_fixed == 1) {
                        v_finaladAmt = v_adamt;
                    } else {

                    }
                    v_adconfigured = "Y";
                }
                pstmt.close();
                pstmt = null;
            }
            /* Allowance or Deduction amount is updated at the Office End */
            if (v_finaladAmt == 0 && v_adconfigured.equals("N")) {

                pstmt = con.prepareStatement("SELECT FIXEDVALUE,IS_FIXED,AD_ORGFORMULA,BT_ID FROM UPDATE_AD_INFO WHERE UPDATION_REF_CODE = ? AND WHERE_UPDATED = 'O' AND REF_AD_CODE = ?");
                pstmt.setString(1, offCode);
                pstmt.setString(2, adcode);
                rs = pstmt.executeQuery();
                if (rs.next()) {
                    v_reccnt = 1;
                    v_adamt = rs.getInt("FIXEDVALUE");
                    v_fixed = rs.getInt("IS_FIXED");
                    v_ad_orgformula = rs.getString("AD_ORGFORMULA");
                    v_btId = rs.getString("BT_ID");

                    if (v_fixed == 1) {
                        v_finaladAmt = v_adamt;
                    } else {

                    }
                    v_adconfigured = "Y";
                }
                pstmt.close();
                pstmt = null;
            }
            /* Allowance or Deduction amount is Calculated From Global Formula */
            if (v_reccnt == 0) {
                if (v_finaladAmt == 0 && v_adconfigured.equals("N")) {

                    pstmt = con.prepareStatement("SELECT FIXEDVALUE,IS_FIXED,AD_ORGFORMULA,BT_ID,SCHEDULE,OBJECT_HEAD,AD_TYPE FROM G_AD_LIST WHERE AD_CODE = ?");
                    pstmt.setString(1, adcode);
                    rs = pstmt.executeQuery();
                    if (rs.next()) {
                        v_reccnt = 1;
                        v_adamt = rs.getInt("FIXEDVALUE");
                        v_fixed = rs.getInt("IS_FIXED");
                        v_ad_orgformula = rs.getString("AD_ORGFORMULA");
                        if (rs.getString("AD_TYPE") != null && rs.getString("AD_TYPE").equals("A")) {
                            v_btId = rs.getString("OBJECT_HEAD");
                        } else {
                            v_btId = rs.getString("BT_ID");
                        }

                        if (v_fixed == 1) {
                            v_finaladAmt = v_adamt;
                        } else {

                        }
                        v_adconfigured = "Y";
                    }

                    pstmt.close();
                    pstmt = null;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }

        ADOBJECT adObject = new ADOBJECT();
        adObject.setAdamt(v_finaladAmt);
        adObject.setBtid(v_btId);
        return adObject;
    }

    @Override
    public ArrayList getAllowanceList(SectionDtlSPCWiseEmp sdswe, String offCode, int aqday, int payday, int workday, int traing_days, PayComponent payComponent) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList allowanceList = new ArrayList();
        try {
            con = dataSource.getConnection();
            if (sdswe.getJobtypeid() == 1) {
                pstmt = con.prepareStatement("SELECT AD_CODE,AD_DESC,AD_CODE_NAME,AD_TYPE,ALT_UNIT,DED_TYPE,SCHEDULE,REP_COL,ROW_NO,OBJECT_HEAD "
                        + "FROM (SELECT AD_CODE,AD_DESC,AD_CODE_NAME,AD_TYPE,ALT_UNIT,DED_TYPE,SCHEDULE,REP_COL,ROW_NO,OBJECT_HEAD FROM G_AD_LIST "
                        + "WHERE AD_TYPE='A' AND (ad_code_name='DCLUPA' OR ad_code_name='WASHAL'))G_AD_LIST");
                rs = pstmt.executeQuery();
                int v_adamt = 0;
                while (rs.next()) {
                    if (!rs.getString("AD_CODE_NAME").equalsIgnoreCase("HRA")) {
                        AllowanceDeduction alwded = new AllowanceDeduction();
                        alwded.setAdcode(rs.getString("AD_CODE"));
                        alwded.setAddesc(rs.getString("AD_DESC"));
                        alwded.setAdcodename(rs.getString("AD_CODE_NAME"));
                        alwded.setAdamttype(rs.getString("AD_TYPE"));
                        alwded.setAltUnit(rs.getString("ALT_UNIT"));
                        alwded.setDedType(rs.getString("DED_TYPE"));
                        alwded.setSchedule(rs.getString("SCHEDULE"));
                        alwded.setRownum(rs.getInt("ROW_NO"));
                        alwded.setRepCol(rs.getInt("REP_COL"));
                        alwded.setHead(rs.getString("OBJECT_HEAD"));
                        String v_adcodename = alwded.getAdcodename();
                        if (v_adcodename.equalsIgnoreCase("DCLUPA")) {
                            v_adamt = workday * 300;
                        } else if (v_adcodename.equalsIgnoreCase("WASHAL")) {
                            v_adamt = new Double(workday * 0.83).intValue();
                        }
                        alwded.setAdvalue(v_adamt);
                        allowanceList.add(alwded);
                    }

                }
            } else {
                pstmt = con.prepareStatement("SELECT AD_CODE,AD_DESC,AD_CODE_NAME,AD_TYPE,ALT_UNIT,DED_TYPE,SCHEDULE,REP_COL,ROW_NO,OBJECT_HEAD "
                        + "FROM (SELECT AD_CODE,AD_DESC,AD_CODE_NAME,AD_TYPE,ALT_UNIT,DED_TYPE,SCHEDULE,REP_COL,ROW_NO,OBJECT_HEAD FROM G_AD_LIST "
                        + "WHERE AD_TYPE='A')G_AD_LIST");
                rs = pstmt.executeQuery();
                int v_adamt = 0;
                while (rs.next()) {
                    if (!rs.getString("AD_CODE_NAME").equalsIgnoreCase("HRA")) {
                        AllowanceDeduction alwded = new AllowanceDeduction();
                        alwded.setAdcode(rs.getString("AD_CODE"));
                        alwded.setAddesc(rs.getString("AD_DESC"));
                        alwded.setAdcodename(rs.getString("AD_CODE_NAME"));
                        alwded.setAdamttype(rs.getString("AD_TYPE"));
                        alwded.setAltUnit(rs.getString("ALT_UNIT"));
                        alwded.setDedType(rs.getString("DED_TYPE"));
                        alwded.setSchedule(rs.getString("SCHEDULE"));
                        alwded.setRownum(rs.getInt("ROW_NO"));
                        alwded.setRepCol(rs.getInt("REP_COL"));
                        alwded.setHead(rs.getString("OBJECT_HEAD"));
                        String v_adcodename = alwded.getAdcodename();
                        String v_schedule = alwded.getSchedule();
                        ADOBJECT v_adobject = null;

                        v_adobject = getAdAmt(sdswe.getSpc(), sdswe.getEmpid(), alwded.getAdcode(), offCode, aqday, payday, workday, traing_days, payComponent);

                        String v_btid = "";
                        if (v_adobject.btid != null) {
                            v_btid = v_adobject.btid;
                        }
                        v_adamt = v_adobject.adamt;

                        if (aqday > workday) {
                            if (v_schedule.equals("OA") && (v_adcodename != "GCA" && v_adcodename != "SP" && v_adcodename != "GAAL")) {
                                v_adamt = (v_adamt / aqday) * workday;
                            }
                            if (v_adamt > 0) {
                                v_adamt = (v_adamt / aqday) * payday;
                            }
                        }
                        alwded.setAdvalue(v_adamt);
                        if (v_adcodename.equalsIgnoreCase("GP")) {
                            alwded.setAdvalue(payComponent.getGp());
                        }

                        if (v_adcodename.equalsIgnoreCase("SP")) {
                            alwded.setAdvalue(0);
                        }

                        allowanceList.add(alwded);
                    }

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return allowanceList;
    }

    @Override
    public ArrayList getAllowanceList(String curspc, String empCode, String configuredlvl, BigDecimal billGroupId, String offCode, int aqday, int payday, int workday, int traing_days, PayComponent payComponent, String v_bill_type, String v_depcode) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList allowanceList = new ArrayList();
        try {
            con = dataSource.getConnection();

            if (configuredlvl == null || configuredlvl.equals("B")) {
                pstmt = con.prepareStatement(" SELECT G_AD_LIST.AD_CODE,AD_DESC,AD_CODE_NAME,AD_TYPE,ALT_UNIT,DED_TYPE,SCHEDULE,REP_COL,ROW_NO,COL_NUMBER,ROW_NUMBER,OBJECT_HEAD FROM "
                        + "(SELECT AD_CODE,AD_DESC,AD_CODE_NAME,AD_TYPE,ALT_UNIT,DED_TYPE,SCHEDULE,REP_COL,ROW_NO,OBJECT_HEAD FROM G_AD_LIST WHERE AD_TYPE='A')G_AD_LIST "
                        + "LEFT OUTER JOIN (select AD_CODE,COL_NUMBER,ROW_NUMBER from bill_configuration where bill_group_id = ?)bill_configuration ON "
                        + "G_AD_LIST.AD_CODE = bill_configuration.AD_CODE");
                pstmt.setBigDecimal(1, billGroupId);
                rs = pstmt.executeQuery();
                int v_adamt_temp = 0;
                int v_adamt = 0;
                while (rs.next()) {
                    if (!rs.getString("AD_CODE_NAME").equalsIgnoreCase("HRA")) {
                        int confrepcol = rs.getInt("COL_NUMBER");
                        int confreprow = rs.getInt("ROW_NUMBER");
                        AllowanceDeduction alwded = new AllowanceDeduction();
                        alwded.setAdcode(rs.getString("AD_CODE"));
                        alwded.setAddesc(rs.getString("AD_DESC"));
                        alwded.setAdcodename(rs.getString("AD_CODE_NAME"));
                        alwded.setAdamttype(rs.getString("AD_TYPE"));
                        alwded.setAltUnit(rs.getString("ALT_UNIT"));
                        alwded.setDedType(rs.getString("DED_TYPE"));
                        alwded.setSchedule(rs.getString("SCHEDULE"));
                        alwded.setRownum(rs.getInt("ROW_NO"));
                        alwded.setRepCol(rs.getInt("REP_COL"));

                        String v_btid = rs.getString("OBJECT_HEAD");
                        ADOBJECT v_adobject = new ADOBJECT();
                        String v_adcodename = alwded.getAdcodename();
                        if (!v_adcodename.equalsIgnoreCase("HRA")) {
                            v_adobject = getAdAmt(curspc, empCode, alwded.getAdcode(), offCode, aqday, payday, workday, traing_days, payComponent);
                            if (v_adobject.getBtid() != null && v_adcodename.equalsIgnoreCase("DP") && !v_adobject.getBtid().equals("")) {
                                v_btid = v_adobject.btid;
                            }
                            if (v_adobject.getBtid() != null && v_adobject.getBtid().equals("")) {
                                if (v_bill_type.equals("69") && (v_adcodename.equalsIgnoreCase("DA") || v_adcodename.equalsIgnoreCase("DP") || alwded.getAdcodename().equalsIgnoreCase("GP") || alwded.getAdcodename().equalsIgnoreCase("SP") || alwded.getSchedule().equalsIgnoreCase("OA"))) {
                                    v_btid = "921";
                                } else {
                                    if (alwded.getAdcodename().equalsIgnoreCase("DP")) {
                                        v_btid = v_adobject.btid;
                                    }
                                    v_btid = v_adobject.btid;
                                }

                            }

                        } else {
                            if (v_bill_type.equals("69") && (alwded.getAdcodename().equalsIgnoreCase("DA") || alwded.getAdcodename().equalsIgnoreCase("DP") || alwded.getAdcodename().equalsIgnoreCase("GP") || alwded.getAdcodename().equalsIgnoreCase("SP") || alwded.getSchedule().equalsIgnoreCase("OA"))) {
                                v_btid = "921";
                            }
                        }
                        v_adamt_temp = v_adobject.getAdamt();
                        v_adamt = v_adamt_temp;
                        if (v_adamt_temp > 0) {
                            if (v_depcode.equals("05")) {
                                if (v_adcodename.equals("KMA") || v_adcodename.equals("BTLA") || v_adcodename.equals("SPLDIT") || v_adcodename.equals("RAL") || v_adcodename.equals("CLAL") || v_adcodename.equals("HA")) {
                                    v_adamt = 0;
                                } else if (!v_adcodename.equals("GCA") && !v_adcodename.equals("LFQ")) {
                                    v_adamt = v_adamt_temp / 2;
                                }
                            }
                        }
                        alwded.setAdvalue(v_adamt);
                        if (v_adcodename.equalsIgnoreCase("GP")) {
                            alwded.setAdvalue(payComponent.getGp());
                        }

                        if (v_adcodename.equalsIgnoreCase("SP")) {
                            alwded.setAdvalue(0);
                        }
                        if (v_adcodename.equalsIgnoreCase("PPAY")) {
                            alwded.setAdvalue(v_adamt_temp);
                        }
                        /*
                         IF v_adcodename = 'DA' THEN            
                         v_da := v_adamt;           
                         END IF;
                         */
                        if (confrepcol != 0) {
                            alwded.setRepCol(confrepcol);

                        }
                        if (confreprow != 0) {
                            alwded.setRownum(confrepcol);
                        }
                        alwded.setHead(v_btid);
                        allowanceList.add(alwded);
                    }
                }
            } else {
                pstmt = con.prepareStatement("SELECT G_AD_LIST.AD_CODE,AD_DESC,AD_CODE_NAME,AD_TYPE,ALT_UNIT,DED_TYPE,SCHEDULE,REP_COL,ROW_NO,COL_NUMBER,ROW_NUMBER,OBJECT_HEAD FROM (SELECT AD_CODE,AD_DESC,AD_CODE_NAME,AD_TYPE,ALT_UNIT,DED_TYPE,SCHEDULE,REP_COL,ROW_NO,OBJECT_HEAD FROM G_AD_LIST WHERE AD_TYPE='A')G_AD_LIST "
                        + "LEFT OUTER JOIN (select AD_CODE,COL_NUMBER,ROW_NUMBER from bill_configuration where OFF_CODE = ? and bill_group_id is null)bill_configuration ON "
                        + "G_AD_LIST.AD_CODE = bill_configuration.AD_CODE");
                pstmt.setString(1, offCode);
                rs = pstmt.executeQuery();
                int v_adamt_temp = 0;
                int v_adamt = 0;
                String v_btid = null;
                ADOBJECT v_adobject = null;

                while (rs.next()) {
                    v_btid = rs.getString("OBJECT_HEAD");
                    int confrepcol = rs.getInt("COL_NUMBER");
                    int confreprow = rs.getInt("ROW_NUMBER");
                    AllowanceDeduction alwded = new AllowanceDeduction();
                    alwded.setAdcode(rs.getString("AD_CODE"));
                    alwded.setAddesc(rs.getString("AD_DESC"));
                    alwded.setAdcodename(rs.getString("AD_CODE_NAME"));
                    alwded.setAdamttype(rs.getString("AD_TYPE"));
                    alwded.setAltUnit(rs.getString("ALT_UNIT"));
                    alwded.setDedType(rs.getString("DED_TYPE"));
                    alwded.setSchedule(rs.getString("SCHEDULE"));
                    alwded.setRownum(rs.getInt("ROW_NO"));
                    alwded.setRepCol(rs.getInt("REP_COL"));
                    alwded.setHead(rs.getString("OBJECT_HEAD"));
                    String v_adcodename = alwded.getAdcodename();
                    if (!rs.getString("AD_CODE_NAME").equalsIgnoreCase("HRA")) {
                        v_adobject = getAdAmt(curspc, empCode, alwded.getAdcode(), offCode, aqday, payday, workday, traing_days, payComponent);
                        if (v_adobject.getBtid() != null && !v_adobject.getBtid().equals("")) {
                            if (v_bill_type.equals("69") && (alwded.getAdcodename().equalsIgnoreCase("DA") || alwded.getAdcodename().equalsIgnoreCase("DP") || alwded.getAdcodename().equalsIgnoreCase("GP") || alwded.getAdcodename().equalsIgnoreCase("SP") || alwded.getSchedule().equalsIgnoreCase("OA"))) {
                                v_btid = "921";
                            } else {
                                v_btid = v_adobject.btid;
                            }
                        } else {
                            if (v_bill_type.equals("69") && (alwded.getAdcodename().equalsIgnoreCase("DA") || alwded.getAdcodename().equalsIgnoreCase("DP") || alwded.getAdcodename().equalsIgnoreCase("GP") || alwded.getAdcodename().equalsIgnoreCase("SP") || alwded.getSchedule().equalsIgnoreCase("OA"))) {
                                v_btid = "921";
                            }
                        }
                        v_adamt = v_adobject.adamt;
                        if (v_adamt_temp > 0) {
                            if (v_depcode.equals("05")) {
                                if (v_adcodename.equals("KMA") || v_adcodename.equals("BTLA") || v_adcodename.equals("SPLDIT") || v_adcodename.equals("RAL") || v_adcodename.equals("CLAL") || v_adcodename.equals("HA")) {
                                    v_adamt = 0;
                                } else if (!v_adcodename.equals("GCA") && !v_adcodename.equals("LFQ")) {
                                    v_adamt = v_adamt_temp / 2;
                                }
                            }
                        }
                        alwded.setAdvalue(v_adamt);
                        if (v_adcodename.equalsIgnoreCase("GP")) {
                            alwded.setAdvalue(payComponent.getGp());
                        }

                        if (v_adcodename.equalsIgnoreCase("SP")) {
                            alwded.setAdvalue(0);
                        }
                        if (v_adcodename.equalsIgnoreCase("PPAY")) {
                            alwded.setAdvalue(v_adamt_temp);
                        }
                        /*
                         IF v_adcodename = 'DA' THEN            
                         v_da := v_adamt;           
                         END IF;
                         */
                        if (confrepcol != 0) {
                            alwded.setRepCol(confrepcol);

                        }
                        if (confreprow != 0) {
                            alwded.setRownum(confrepcol);
                        }
                        alwded.setHead(v_btid);
                        allowanceList.add(alwded);

                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return allowanceList;
    }

    @Override
    public ArrayList getAllowanceList(SectionDtlSPCWiseEmp sdswe, String configuredlvl, BigDecimal billGroupId, String offCode, int aqday, int payday, int workday, int traing_days, PayComponent payComponent, String v_bill_type, String v_depcode, int aqmonth, int aqyear) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList allowanceList = new ArrayList();
        try {
            con = dataSource.getConnection();
            if (configuredlvl == null || configuredlvl.equals("B")) {
                pstmt = con.prepareStatement(" SELECT G_AD_LIST.AD_CODE,AD_DESC,AD_CODE_NAME,AD_TYPE,ALT_UNIT,DED_TYPE,SCHEDULE,REP_COL,ROW_NO,COL_NUMBER,ROW_NUMBER,OBJECT_HEAD FROM "
                        + "(SELECT AD_CODE,AD_DESC,AD_CODE_NAME,AD_TYPE,ALT_UNIT,DED_TYPE,SCHEDULE,REP_COL,ROW_NO,OBJECT_HEAD FROM G_AD_LIST WHERE AD_TYPE='A')G_AD_LIST "
                        + "LEFT OUTER JOIN (select AD_CODE,COL_NUMBER,ROW_NUMBER from bill_configuration where bill_group_id = ?)bill_configuration ON "
                        + "G_AD_LIST.AD_CODE = bill_configuration.AD_CODE");
                pstmt.setBigDecimal(1, billGroupId);
                rs = pstmt.executeQuery();
                int v_adamt_temp = 0;
                int v_adamt = 0;
                while (rs.next()) {
                    if (rs.getString("AD_CODE_NAME") != null && !rs.getString("AD_CODE_NAME").equalsIgnoreCase("HRA")) {
                        int confrepcol = rs.getInt("COL_NUMBER");
                        int confreprow = rs.getInt("ROW_NUMBER");
                        AllowanceDeduction alwded = new AllowanceDeduction();
                        alwded.setAdcode(rs.getString("AD_CODE"));
                        alwded.setAddesc(rs.getString("AD_DESC"));
                        alwded.setAdcodename(rs.getString("AD_CODE_NAME"));
                        alwded.setAdamttype(rs.getString("AD_TYPE"));
                        alwded.setAltUnit(rs.getString("ALT_UNIT"));
                        alwded.setDedType(rs.getString("DED_TYPE"));
                        alwded.setSchedule(rs.getString("SCHEDULE"));
                        alwded.setRownum(rs.getInt("ROW_NO"));
                        alwded.setRepCol(rs.getInt("REP_COL"));

                        String v_btid = rs.getString("OBJECT_HEAD");
                        ADOBJECT v_adobject = new ADOBJECT();
                        String v_adcodename = alwded.getAdcodename();
                        if (!v_adcodename.equalsIgnoreCase("HRA")) {

                            v_adobject = getAdAmt(sdswe, alwded.getAdcode(), offCode, aqday, payday, workday, traing_days, payComponent, aqmonth, aqyear, v_depcode);
                            if (v_adcodename.equals("DA")) {
                                //System.out.println("DA Formula is: " + v_adobject.daformula);
                                //System.out.println("DA Amount is: " + v_adobject.adamt);
                            }
                            if (v_adobject.getBtid() != null && v_adcodename.equalsIgnoreCase("DP") && !v_adobject.getBtid().equals("")) {
                                v_btid = v_adobject.btid;
                            }
                            if (v_adobject.getBtid() != null && v_adobject.getBtid().equals("")) {
                                if (v_bill_type.equals("69") && (v_adcodename.equalsIgnoreCase("DA") || v_adcodename.equalsIgnoreCase("DP") || alwded.getAdcodename().equalsIgnoreCase("GP") || alwded.getAdcodename().equalsIgnoreCase("SP") || alwded.getSchedule().equalsIgnoreCase("OA"))) {
                                    v_btid = "921";
                                } else {
                                    if (alwded.getAdcodename().equalsIgnoreCase("DP")) {
                                        v_btid = v_adobject.btid;
                                    }
                                    v_btid = v_adobject.btid;
                                }

                            }

                        } else {
                            if (v_bill_type.equals("69") && (alwded.getAdcodename().equalsIgnoreCase("DA") || alwded.getAdcodename().equalsIgnoreCase("DP") || alwded.getAdcodename().equalsIgnoreCase("GP") || alwded.getAdcodename().equalsIgnoreCase("SP") || alwded.getSchedule().equalsIgnoreCase("OA"))) {
                                v_btid = "921";
                            }
                        }
                        v_adamt_temp = v_adobject.getAdamt();
                        v_adamt = v_adamt_temp;
                        if (v_adamt_temp > 0) {
                            if (v_depcode.equals("05")) {
                                if (v_adcodename.equals("KMA") || v_adcodename.equals("BTLA") || v_adcodename.equals("SPLDIT") || v_adcodename.equals("RAL") || v_adcodename.equals("CLAL") || v_adcodename.equals("HA")) {
                                    v_adamt = 0;
                                } else if (!v_adcodename.equals("GCA") && !v_adcodename.equals("LFQ") && !v_adcodename.equals("DA")) {
                                    v_adamt = v_adamt_temp / 2;
                                }
                            } else if (v_adcodename.equals("GALO") || v_adcodename.equals("BYCAL") || v_adcodename.equals("RNKA") || v_adcodename.equals("SPA") || v_adcodename.equals("SPLA") || v_adcodename.equals("KTMAL") || v_adcodename.equals("KMA") || v_adcodename.equals("BTLA") || v_adcodename.equals("SPLDIT") || v_adcodename.equals("RAL") || v_adcodename.equals("CLAL") || v_adcodename.equals("HA")) {
                                double v_temp_adamt = (double) v_adamt_temp / aqday;
                                double v_adamt_decimal = Math.round(v_temp_adamt * workday);
                                Double newData = new Double(v_adamt_decimal);
                                v_adamt = newData.intValue();
                            } else {
                                v_adamt = v_adamt_temp;
                            }
                        }
                        alwded.setAdvalue(v_adamt);
                        if (v_adcodename.equalsIgnoreCase("GP")) {
                            //alwded.setAdvalue(payComponent.getGp());
                            double v_temp_adamt = (double) payComponent.getGp() / aqday;
                            double v_adamt_decimal = Math.round(v_temp_adamt * workday);
                            Double newData = new Double(v_adamt_decimal);
                            v_adamt = newData.intValue();
                            alwded.setAdvalue(v_adamt);
                            payComponent.setGp(v_adamt);
                        }

                        if (v_adcodename.equalsIgnoreCase("SP")) {
                            alwded.setAdvalue(0);
                        }
                        if (v_adcodename.equalsIgnoreCase("PPAY")) {
                            alwded.setAdvalue(v_adamt_temp);
                        }

                        if (v_adcodename.equalsIgnoreCase("DA")) {
                            System.out.println("da formula-=" + v_adobject.getDaformula());
                            alwded.setFormula(v_adobject.getDaformula());
                        }
                        /*
                         IF v_adcodename = 'DA' THEN            
                         v_da := v_adamt;           
                         END IF;
                         */
                        if (confrepcol != 0) {
                            alwded.setRepCol(confrepcol);

                        }
                        if (confreprow != 0) {
                            alwded.setRownum(confrepcol);
                        }
                        alwded.setHead(v_btid);
                        allowanceList.add(alwded);
                    }
                }
            } else {
                pstmt = con.prepareStatement("SELECT G_AD_LIST.AD_CODE,AD_DESC,AD_CODE_NAME,AD_TYPE,ALT_UNIT,DED_TYPE,SCHEDULE,REP_COL,ROW_NO,COL_NUMBER,ROW_NUMBER,OBJECT_HEAD FROM (SELECT AD_CODE,AD_DESC,AD_CODE_NAME,AD_TYPE,ALT_UNIT,DED_TYPE,SCHEDULE,REP_COL,ROW_NO,OBJECT_HEAD FROM G_AD_LIST WHERE AD_TYPE='A')G_AD_LIST "
                        + "LEFT OUTER JOIN (select AD_CODE,COL_NUMBER,ROW_NUMBER from bill_configuration where OFF_CODE = ? and bill_group_id is null)bill_configuration ON "
                        + "G_AD_LIST.AD_CODE = bill_configuration.AD_CODE");
                pstmt.setString(1, offCode);
                rs = pstmt.executeQuery();
                int v_adamt_temp = 0;
                int v_adamt = 0;
                String v_btid = null;
                ADOBJECT v_adobject = null;

                while (rs.next()) {
                    v_btid = rs.getString("OBJECT_HEAD");
                    int confrepcol = rs.getInt("COL_NUMBER");
                    int confreprow = rs.getInt("ROW_NUMBER");
                    AllowanceDeduction alwded = new AllowanceDeduction();
                    alwded.setAdcode(rs.getString("AD_CODE"));
                    alwded.setAddesc(rs.getString("AD_DESC"));
                    alwded.setAdcodename(rs.getString("AD_CODE_NAME"));
                    alwded.setAdamttype(rs.getString("AD_TYPE"));
                    alwded.setAltUnit(rs.getString("ALT_UNIT"));
                    alwded.setDedType(rs.getString("DED_TYPE"));
                    alwded.setSchedule(rs.getString("SCHEDULE"));
                    alwded.setRownum(rs.getInt("ROW_NO"));
                    alwded.setRepCol(rs.getInt("REP_COL"));
                    alwded.setHead(rs.getString("OBJECT_HEAD"));
                    String v_adcodename = alwded.getAdcodename();
                    if (!rs.getString("AD_CODE_NAME").equalsIgnoreCase("HRA")) {
                        v_adobject = getAdAmt(sdswe, alwded.getAdcode(), offCode, aqday, payday, workday, traing_days, payComponent, aqmonth, aqyear, v_depcode);
                        if (v_adobject.getBtid() != null && !v_adobject.getBtid().equals("")) {
                            if (v_bill_type.equals("69") && (alwded.getAdcodename().equalsIgnoreCase("DA") || alwded.getAdcodename().equalsIgnoreCase("DP") || alwded.getAdcodename().equalsIgnoreCase("GP") || alwded.getAdcodename().equalsIgnoreCase("SP") || alwded.getSchedule().equalsIgnoreCase("OA"))) {
                                v_btid = "921";
                            } else {
                                v_btid = v_adobject.btid;
                            }
                        } else {
                            if (v_bill_type.equals("69") && (alwded.getAdcodename().equalsIgnoreCase("DA") || alwded.getAdcodename().equalsIgnoreCase("DP") || alwded.getAdcodename().equalsIgnoreCase("GP") || alwded.getAdcodename().equalsIgnoreCase("SP") || alwded.getSchedule().equalsIgnoreCase("OA"))) {
                                v_btid = "921";
                            }
                        }
                        v_adamt_temp = v_adobject.getAdamt();
                        v_adamt = v_adamt_temp;
                        if (v_adamt_temp > 0) {
                            if (v_depcode.equals("05")) {
                                if (v_adcodename.equals("KMA") || v_adcodename.equals("BTLA") || v_adcodename.equals("SPLDIT") || v_adcodename.equals("RAL") || v_adcodename.equals("CLAL") || v_adcodename.equals("HA")) {
                                    v_adamt = 0;
                                } else if (!v_adcodename.equals("GCA") && !v_adcodename.equals("LFQ") && !v_adcodename.equals("DA")) {
                                    v_adamt = v_adamt_temp / 2;
                                }
                            } else if (v_adcodename.equals("KMA") || v_adcodename.equals("BTLA") || v_adcodename.equals("SPLDIT") || v_adcodename.equals("RAL") || v_adcodename.equals("CLAL") || v_adcodename.equals("HA")) {
                                double v_temp_adamt = (double) v_adamt_temp / aqday;
                                double v_adamt_decimal = Math.round(v_temp_adamt * workday);
                                Double newData = new Double(v_adamt_decimal);
                                v_adamt = newData.intValue();
                            } else {
                                v_adamt = v_adamt_temp;
                            }
                        }
                        alwded.setAdvalue(v_adamt);
                        if (v_adcodename.equalsIgnoreCase("GP")) {
                            //alwded.setAdvalue(payComponent.getGp());
                            double v_temp_adamt = (double) payComponent.getGp() / aqday;
                            double v_adamt_decimal = Math.round(v_temp_adamt * workday);
                            Double newData = new Double(v_adamt_decimal);
                            v_adamt = newData.intValue();
                            alwded.setAdvalue(v_adamt);
                            payComponent.setGp(v_adamt);
                        }

                        if (v_adcodename.equalsIgnoreCase("SP")) {
                            alwded.setAdvalue(0);
                        }
                        if (v_adcodename.equalsIgnoreCase("PPAY")) {
                            alwded.setAdvalue(v_adamt_temp);
                        }
                        if (v_adcodename.equalsIgnoreCase("DA")) {
                            alwded.setFormula(v_adobject.getDaformula());
                        }
                        if (confrepcol != 0) {
                            alwded.setRepCol(confrepcol);

                        }
                        if (confreprow != 0) {
                            alwded.setRownum(confrepcol);
                        }
                        alwded.setHead(v_btid);
                        allowanceList.add(alwded);

                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return allowanceList;
    }

    public AqDtlsModel[] getDA(String empCode, BigDecimal billGroupId, PayComponent payComponent, long gross, AqmastModel aqmast) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList<AqDtlsModel> allowanceList = new ArrayList<>();
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT G_AD_LIST.AD_CODE,AD_DESC,AD_CODE_NAME,AD_TYPE,ALT_UNIT,DED_TYPE,SCHEDULE,REP_COL,ROW_NO,rep_col,row_no,OBJECT_HEAD from g_ad_list where ad_code_NAME='DA'");
            rs = pstmt.executeQuery();
            if (rs.next()) {

                long da;
                if (payComponent.getIspayrevised() == null || payComponent.getIspayrevised().equalsIgnoreCase("N")) {
                    da = Math.round(((payComponent.getBasic() + payComponent.getGp()) * 1.39));
                } else {
                    da = Math.round(payComponent.getBasic() * 0.05);
                }

                /*if (alwded.getAdcodename().equalsIgnoreCase("GP")) {
                 adAmt = 0;
                 }*/
                long adAmt = da;

                /*if (alwded.getAdcodename().equalsIgnoreCase("CPF")) {
                 adAmt = (int) ((aqmast.getCurBasic() + da) * 0.1);
                 }*/
                //gross = gross + adAmt;
                AqDtlsModel aqModel = new AqDtlsModel();
                aqModel.setAqGroup(aqmast.getAqGroup());
                aqModel.setAqSlNo(aqmast.getAqSlNo());
                aqModel.setDdoOff("");
                aqModel.setEmpCode(aqmast.getEmpCode());
                aqModel.setPayMon(aqmast.getPayMonth());
                aqModel.setPayYear(aqmast.getPayYear());
                aqModel.setAqDate(aqmast.getAqDate());
                aqModel.setAqMonth(aqmast.getAqMonth());
                aqModel.setAqYear(aqmast.getAqYear());
                aqModel.setAqType(aqmast.getAqType());
                aqModel.setRefOrderNo(aqmast.getRefOrder());
                aqModel.setRefOrderDate(aqmast.getRefDate());
                aqModel.setSlNo(rs.getInt("ROW_NO"));
                aqModel.setAdCode(rs.getString("AD_CODE_NAME"));
                aqModel.setAdDesc(rs.getString("AD_DESC"));
                aqModel.setAdType(rs.getString("AD_TYPE"));
                aqModel.setAltUnit(rs.getString("ALT_UNIT"));
                aqModel.setDedType(rs.getString("DED_TYPE"));
                aqModel.setAdAmt(adAmt);
                aqModel.setAccNo(null);
                aqModel.setRefDesc(null);
                aqModel.setRefCount(0);
                aqModel.setSchedule(rs.getString("SCHEDULE"));
                aqModel.setNowDedn(null);
                aqModel.setTotRecAmt(0);
                aqModel.setRepCol(rs.getInt("REP_COL"));
                aqModel.setAdRefId(null);
                aqModel.setBtId(rs.getString("OBJECT_HEAD"));
                aqModel.setInstalCount(0);
                allowanceList.add(aqModel);

            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }

        AqDtlsModel aqDtlsModels[] = allowanceList.toArray(new AqDtlsModel[allowanceList.size()]);
        return aqDtlsModels;
    }

    @Override
    public ArrayList getAllowanceList(String configuredlvl, BigDecimal billGroupId, String offcode, String empId, PayComponent payComponent
    ) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList allowanceList = new ArrayList();
        try {
            con = dataSource.getConnection();
            if (configuredlvl == null || configuredlvl.equals("B")) {
                pstmt = con.prepareStatement("SELECT G_AD_LIST.AD_CODE,AD_DESC,AD_CODE_NAME,AD_TYPE,ALT_UNIT,DED_TYPE,SCHEDULE,REP_COL,ROW_NO,COL_NUMBER,ROW_NUMBER,OBJECT_HEAD, update_ad_info.FIXEDVALUE FROM "
                        + "(SELECT AD_CODE,AD_DESC,AD_CODE_NAME, AD_TYPE,ALT_UNIT,DED_TYPE,SCHEDULE,REP_COL,ROW_NO,OBJECT_HEAD FROM G_AD_LIST WHERE AD_TYPE='A')G_AD_LIST "
                        + "LEFT OUTER JOIN (select AD_CODE,COL_NUMBER,ROW_NUMBER from bill_configuration where bill_group_id = ?)bill_configuration ON G_AD_LIST.AD_CODE = bill_configuration.AD_CODE "
                        + "LEFT OUTER JOIN (SELECT * FROM update_ad_info WHERE updation_ref_code = ? AND where_updated = 'E')update_ad_info ON G_AD_LIST.AD_CODE = update_ad_info.REF_AD_CODE");
                pstmt.setBigDecimal(1, billGroupId);
                pstmt.setString(2, empId);
                rs = pstmt.executeQuery();
                while (rs.next()) {
                    if (!rs.getString("AD_CODE_NAME").equalsIgnoreCase("HRA")) {
                        AllowanceDeduction alwded = new AllowanceDeduction();
                        alwded.setAdcode(rs.getString("AD_CODE"));
                        alwded.setAddesc(rs.getString("AD_DESC"));
                        alwded.setAdcodename(rs.getString("AD_CODE_NAME"));
                        alwded.setAdamttype(rs.getString("AD_TYPE"));
                        alwded.setHead(rs.getString("OBJECT_HEAD"));
                        if (rs.getString("AD_CODE_NAME").equalsIgnoreCase("GP")) {
                            alwded.setAdvalue(payComponent.getGp());
                        } else {
                            alwded.setAdvalue(rs.getInt("FIXEDVALUE"));
                        }

                        alwded.setRownum(rs.getInt("ROW_NO"));
                        alwded.setRepCol(rs.getInt("REP_COL"));
                        alwded.setAltUnit(rs.getString("ALT_UNIT"));
                        alwded.setSchedule(rs.getString("SCHEDULE"));
                        allowanceList.add(alwded);
                    }
                }
            } else {
                pstmt = con.prepareStatement("SELECT G_AD_LIST.AD_CODE,AD_DESC,AD_CODE_NAME,AD_TYPE,ALT_UNIT,DED_TYPE,SCHEDULE,REP_COL,ROW_NO,COL_NUMBER,ROW_NUMBER,OBJECT_HEAD, update_ad_info.FIXEDVALUE FROM "
                        + "(SELECT AD_CODE,AD_DESC,AD_CODE_NAME,AD_TYPE,ALT_UNIT,DED_TYPE,SCHEDULE,REP_COL,ROW_NO,OBJECT_HEAD FROM G_AD_LIST WHERE AD_TYPE='A')G_AD_LIST "
                        + "LEFT OUTER JOIN (select AD_CODE,COL_NUMBER,ROW_NUMBER from bill_configuration where OFF_CODE = ?)bill_configuration ON G_AD_LIST.AD_CODE = bill_configuration.AD_CODE "
                        + "LEFT OUTER JOIN (SELECT * FROM update_ad_info WHERE updation_ref_code = ? AND where_updated = 'E')update_ad_info ON G_AD_LIST.AD_CODE = update_ad_info.REF_AD_CODE");
                pstmt.setString(1, offcode);
                pstmt.setString(2, empId);
                rs = pstmt.executeQuery();
                while (rs.next()) {
                    if (!rs.getString("AD_CODE_NAME").equalsIgnoreCase("HRA")) {
                        AllowanceDeduction alwded = new AllowanceDeduction();
                        alwded.setAdcode(rs.getString("AD_CODE"));
                        alwded.setAddesc(rs.getString("AD_DESC"));
                        alwded.setAdcodename(rs.getString("AD_CODE_NAME"));
                        alwded.setAdamttype(rs.getString("AD_TYPE"));
                        alwded.setHead(rs.getString("OBJECT_HEAD"));
                        if (rs.getString("AD_CODE_NAME").equalsIgnoreCase("GP")) {
                            alwded.setAdvalue(payComponent.getGp());
                        } else {
                            alwded.setAdvalue(rs.getInt("FIXEDVALUE"));
                        }
                        alwded.setRownum(rs.getInt("ROW_NO"));
                        alwded.setRepCol(rs.getInt("REP_COL"));
                        alwded.setAltUnit(rs.getString("ALT_UNIT"));
                        alwded.setSchedule(rs.getString("SCHEDULE"));
                        allowanceList.add(alwded);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return allowanceList;
    }

    public String getGPFSeries(String gpfSeries) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String gpfbtid = "";
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT BT_ID FROM G_GPF_TYPE WHERE GPF_TYPE = ?");
            pstmt.setString(1, gpfSeries);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                gpfbtid = rs.getString("BT_ID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return gpfbtid;
    }

    public ArrayList getDeductionList(String curspc, String empCode, String configuredlvl, BigDecimal billGroupId, String offCode, int aqday, int payday, int workday, int traing_days, PayComponent payComponent, String bill_type, String v_depcode, String gpfSeries) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList deductionList = new ArrayList();
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT AD_CODE,AD_DESC,AD_CODE_NAME,AD_TYPE,ALT_UNIT,DED_TYPE,SCHEDULE,REP_COL,ROW_NO,BT_ID FROM G_AD_LIST WHERE AD_TYPE='D' AND SHOW_IN_ADLIST='Y' ORDER BY  CAST (AD_CODE AS INTEGER )");
            rs = pstmt.executeQuery();
            String v_btid = null;
            String v_gpfbtid = getGPFSeries(gpfSeries);
            while (rs.next()) {
                AllowanceDeduction alwded = new AllowanceDeduction();
                alwded.setAdcode(rs.getString("AD_CODE"));
                String v_schedule = rs.getString("SCHEDULE");
                v_btid = rs.getString("BT_ID");
                if (v_schedule != null && !v_schedule.equalsIgnoreCase("HRR") && !v_schedule.equalsIgnoreCase("WRR") && !v_schedule.equalsIgnoreCase("LIC") && !v_schedule.equalsIgnoreCase("CPF") && !v_schedule.equalsIgnoreCase("PT")) {
                    String v_adcodename = rs.getString("AD_CODE_NAME");
                    int v_adamt = 0;
                    if (payday < 15 || (v_depcode != null && v_depcode.equals("05"))) {
                        if (!v_adcodename.equals("CGEGIS")) {
                            v_adamt = 0;
                        }
                    } else {
                        ADOBJECT v_adobject = getAdAmt(curspc, empCode, alwded.getAdcode(), offCode, aqday, payday, workday, traing_days, payComponent);

                        if (v_adobject.getBtid() != null && !v_adobject.getBtid().equals("")) {
                            v_btid = v_adobject.btid;
                            if (v_schedule.equals("GPF") || v_schedule.equals("GPDD") || v_schedule.equals("GPIR")) {
                                v_btid = v_gpfbtid;
                            }
                        }
                        v_adamt = v_adobject.adamt;

                    }

                    alwded.setAddesc(rs.getString("AD_DESC"));
                    alwded.setAdcodename(v_adcodename);
                    alwded.setAdamttype(rs.getString("AD_TYPE"));
                    alwded.setDedType(rs.getString("DED_TYPE"));
                    alwded.setHead(v_btid);
                    alwded.setAdvalue(v_adamt);
                    alwded.setRownum(rs.getInt("ROW_NO"));
                    alwded.setRepCol(rs.getInt("REP_COL"));
                    alwded.setAltUnit(rs.getString("ALT_UNIT"));
                    alwded.setSchedule(rs.getString("SCHEDULE"));
                    deductionList.add(alwded);
                }
                if (rs.getString("AD_CODE_NAME") != null && rs.getString("AD_CODE_NAME").equals("EPF")) {
                    ADOBJECT v_adobject = getAdAmt(curspc, empCode, alwded.getAdcode(), offCode, aqday, payday, workday, traing_days, payComponent);

                    int v_adamt = v_adobject.adamt;

                    alwded.setAddesc(rs.getString("AD_DESC"));
                    alwded.setAdcodename(rs.getString("AD_CODE_NAME"));
                    alwded.setAdamttype(rs.getString("AD_TYPE"));
                    alwded.setDedType(rs.getString("DED_TYPE"));
                    alwded.setHead(null);
                    alwded.setAdvalue(v_adamt);
                    alwded.setRownum(rs.getInt("ROW_NO"));
                    alwded.setRepCol(rs.getInt("REP_COL"));
                    alwded.setAltUnit(rs.getString("ALT_UNIT"));
                    deductionList.add(alwded);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return deductionList;
    }

    public ArrayList getDeductionListDHE(SectionDtlSPCWiseEmp sdswe, String configuredlvl, AqmastModel aqMastModel, BillMastModel billMastModel, int aqday, int payday, int workday, int traing_days, PayComponent payComponent, String gpfSeries) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList deductionList = new ArrayList();
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT AD_CODE,AD_DESC,AD_CODE_NAME,AD_TYPE,ALT_UNIT,DED_TYPE,SCHEDULE,REP_COL,ROW_NO,BT_ID FROM G_AD_LIST WHERE AD_TYPE='D' AND SHOW_IN_ADLIST='Y' ORDER BY  CAST (AD_CODE AS INTEGER )");
            rs = pstmt.executeQuery();
            String v_btid = null;
            String v_gpfbtid = getGPFSeries(gpfSeries);
            while (rs.next()) {
                AllowanceDeduction alwded = new AllowanceDeduction();
                alwded.setAdcode(rs.getString("AD_CODE"));
                String v_schedule = rs.getString("SCHEDULE");
                v_btid = rs.getString("BT_ID");

                if (v_schedule != null && !v_schedule.equalsIgnoreCase("HRR") && !v_schedule.equalsIgnoreCase("WRR") && !v_schedule.equalsIgnoreCase("LIC") && !v_schedule.equalsIgnoreCase("CPF") && !v_schedule.equalsIgnoreCase("PT")) {
                    String v_adcodename = rs.getString("AD_CODE_NAME");
                    int v_adamt = 0;
                    if ((payday < 15 || aqMastModel.getDeptCode().equals("05")) && !v_schedule.equalsIgnoreCase("IT")) {
                        if (!v_adcodename.equals("CGEGIS")) {
                            v_adamt = 0;
                        }
                    } else {
                        ADOBJECT v_adobject = getAdAmt(sdswe, alwded.getAdcode(), billMastModel.getOffCode(), aqday, payday, workday, traing_days, payComponent, aqMastModel.getAqMonth(), aqMastModel.getAqYear(), "");

                        if (v_adobject.getBtid() != null && !v_adobject.getBtid().equals("")) {
                            v_btid = v_adobject.btid;
                            if (v_schedule.equals("GPF") || v_schedule.equals("GPDD") || v_schedule.equals("GPIR")) {
                                v_btid = v_gpfbtid;
                            }
                        }
                        v_adamt = v_adobject.adamt;
                    }

                    alwded.setAddesc(rs.getString("AD_DESC"));
                    alwded.setAdcodename(v_adcodename);
                    alwded.setAdamttype(rs.getString("AD_TYPE"));
                    alwded.setDedType(rs.getString("DED_TYPE"));
                    alwded.setHead(v_btid);
                    if (rs.getString("SCHEDULE").equalsIgnoreCase("GIS")) {
                        if (aqMastModel.getCadreType() != null && !aqMastModel.getCadreType().equals("") && (aqMastModel.getCadreType().equals("AIS") || aqMastModel.getCadreType().equals("DAO"))) {
                            alwded.setAdvalue(v_adamt);
                        } else {
                            alwded.setAdvalue(0);
                        }
                    } else {
                        alwded.setAdvalue(v_adamt);
                    }

                    alwded.setRownum(rs.getInt("ROW_NO"));
                    alwded.setRepCol(rs.getInt("REP_COL"));
                    alwded.setAltUnit(rs.getString("ALT_UNIT"));
                    alwded.setSchedule(rs.getString("SCHEDULE"));
                    deductionList.add(alwded);
                }
                if (rs.getString("AD_CODE_NAME") != null && (rs.getString("AD_CODE_NAME").equals("EPF") || rs.getString("AD_CODE_NAME").equals("EPFDED"))) {
                    ADOBJECT v_adobject = getAdAmt(sdswe, alwded.getAdcode(), billMastModel.getOffCode(), aqday, payday, workday, traing_days, payComponent, aqMastModel.getAqMonth(), aqMastModel.getAqYear(), "");

                    //int v_adamt = v_adobject.adamt;
                    int v_adamt = 0;

                    alwded.setAddesc(rs.getString("AD_DESC"));
                    alwded.setAdcodename(rs.getString("AD_CODE_NAME"));
                    alwded.setAdamttype(rs.getString("AD_TYPE"));
                    alwded.setDedType(rs.getString("DED_TYPE"));
                    alwded.setHead(null);
                    alwded.setAdvalue(v_adamt);
                    alwded.setRownum(rs.getInt("ROW_NO"));
                    alwded.setRepCol(rs.getInt("REP_COL"));
                    alwded.setAltUnit(rs.getString("ALT_UNIT"));
                    deductionList.add(alwded);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return deductionList;
    }

    @Override
    public ArrayList getDeductionList(SectionDtlSPCWiseEmp sdswe, String configuredlvl, AqmastModel aqMastModel, BillMastModel billMastModel, int aqday, int payday, int workday, int traing_days, PayComponent payComponent, String gpfSeries) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList deductionList = new ArrayList();
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT AD_CODE,AD_DESC,AD_CODE_NAME,AD_TYPE,ALT_UNIT,DED_TYPE,SCHEDULE,REP_COL,ROW_NO,BT_ID FROM G_AD_LIST WHERE AD_TYPE='D' AND SHOW_IN_ADLIST='Y' ORDER BY  CAST (AD_CODE AS INTEGER )");
            rs = pstmt.executeQuery();
            String v_btid = null;
            String v_gpfbtid = getGPFSeries(gpfSeries);
            while (rs.next()) {
                AllowanceDeduction alwded = new AllowanceDeduction();
                alwded.setAdcode(rs.getString("AD_CODE"));
                String v_schedule = rs.getString("SCHEDULE");
                v_btid = rs.getString("BT_ID");

                if (v_schedule != null && !v_schedule.equalsIgnoreCase("HRR") && !v_schedule.equalsIgnoreCase("WRR") && !v_schedule.equalsIgnoreCase("LIC") && !v_schedule.equalsIgnoreCase("CPF") && !v_schedule.equalsIgnoreCase("PT")) {
                    String v_adcodename = rs.getString("AD_CODE_NAME");
                    int v_adamt = 0;
                    if ((payday < 15 || aqMastModel.getDeptCode().equals("05")) && !v_schedule.equalsIgnoreCase("IT")) {
                        if (!v_adcodename.equals("CGEGIS")) {
                            v_adamt = 0;
                        }
                    } else {
                        ADOBJECT v_adobject = getAdAmt(sdswe, alwded.getAdcode(), billMastModel.getOffCode(), aqday, payday, workday, traing_days, payComponent, aqMastModel.getAqMonth(), aqMastModel.getAqYear(), "");
                        if (v_schedule != null && v_schedule.equals("GPFDAO")) {
                            System.out.println("v_schedule is: " + v_adobject.btid);
                        }
                        if (v_adobject.getBtid() != null && !v_adobject.getBtid().equals("")) {
                            v_btid = v_adobject.btid;
                            if (v_schedule.equals("GPF") || v_schedule.equals("GPDD") || v_schedule.equals("GPIR")) {
                                v_btid = v_gpfbtid;
                            }
                        }
                        v_adamt = v_adobject.adamt;
                    }

                    alwded.setAddesc(rs.getString("AD_DESC"));
                    alwded.setAdcodename(v_adcodename);

                    alwded.setAdamttype(rs.getString("AD_TYPE"));
                    alwded.setDedType(rs.getString("DED_TYPE"));
                    alwded.setHead(v_btid);
                    if (rs.getString("SCHEDULE").equalsIgnoreCase("GIS")) {
                        if (aqMastModel.getCadreType() != null && !aqMastModel.getCadreType().equals("") && (aqMastModel.getCadreType().equals("AIS") || aqMastModel.getCadreType().equals("DAO"))) {
                            alwded.setAdvalue(v_adamt);
                        } else {
                            alwded.setAdvalue(0);
                        }
                    } else {
                        alwded.setAdvalue(v_adamt);
                    }

                    alwded.setRownum(rs.getInt("ROW_NO"));
                    alwded.setRepCol(rs.getInt("REP_COL"));
                    alwded.setAltUnit(rs.getString("ALT_UNIT"));
                    alwded.setSchedule(rs.getString("SCHEDULE"));
                    deductionList.add(alwded);
                }
                if (rs.getString("AD_CODE_NAME") != null && rs.getString("AD_CODE_NAME").equals("EPF")) {
                    ADOBJECT v_adobject = getAdAmt(sdswe, alwded.getAdcode(), billMastModel.getOffCode(), aqday, payday, workday, traing_days, payComponent, aqMastModel.getAqMonth(), aqMastModel.getAqYear(), "");

                    int v_adamt = v_adobject.adamt;
                    //int v_adamt = 0;

                    alwded.setAddesc(rs.getString("AD_DESC"));
                    alwded.setAdcodename(rs.getString("AD_CODE_NAME"));
                    alwded.setAdamttype(rs.getString("AD_TYPE"));
                    alwded.setDedType(rs.getString("DED_TYPE"));
                    alwded.setHead(null);
                    alwded.setAdvalue(v_adamt);
                    alwded.setRownum(rs.getInt("ROW_NO"));
                    alwded.setRepCol(rs.getInt("REP_COL"));
                    alwded.setAltUnit(rs.getString("ALT_UNIT"));
                    deductionList.add(alwded);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return deductionList;
    }

    @Override
    public ArrayList getDeductionList(String empId) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList deductionList = new ArrayList();
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT G_AD_LIST.AD_CODE,AD_DESC,AD_CODE_NAME,AD_TYPE,ALT_UNIT,DED_TYPE,SCHEDULE,REP_COL,ROW_NO,G_AD_LIST.BT_ID,update_ad_info.FIXEDVALUE FROM G_AD_LIST "
                    + "LEFT OUTER JOIN (SELECT * FROM update_ad_info WHERE updation_ref_code = ? AND where_updated = 'E')update_ad_info ON G_AD_LIST.AD_CODE = update_ad_info.REF_AD_CODE "
                    + "WHERE AD_TYPE='D' AND SHOW_IN_ADLIST='Y' ORDER BY  AD_CODE");
            pstmt.setString(1, empId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                String v_schedule = rs.getString("SCHEDULE");
                if (v_schedule != null && !v_schedule.equalsIgnoreCase("HRR") && !v_schedule.equalsIgnoreCase("WRR") && !v_schedule.equalsIgnoreCase("LIC") && !v_schedule.equalsIgnoreCase("CPF") && !v_schedule.equalsIgnoreCase("PT")) {
                    AllowanceDeduction alwded = new AllowanceDeduction();
                    alwded.setAdcode(rs.getString("AD_CODE"));
                    alwded.setAddesc(rs.getString("AD_DESC"));
                    alwded.setAdcodename(rs.getString("AD_CODE_NAME"));
                    alwded.setAdamttype(rs.getString("AD_TYPE"));
                    alwded.setDedType(rs.getString("DED_TYPE"));
                    alwded.setHead(rs.getString("BT_ID"));
                    alwded.setAdvalue(rs.getInt("FIXEDVALUE"));
                    alwded.setRownum(rs.getInt("ROW_NO"));
                    alwded.setRepCol(rs.getInt("REP_COL"));
                    alwded.setAltUnit(rs.getString("ALT_UNIT"));
                    alwded.setSchedule(rs.getString("SCHEDULE"));
                    deductionList.add(alwded);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return deductionList;
    }

    @Override
    public AqDtlsModel[] getAqDtlsModelFromAllowanceList(ArrayList allowanceList, AqmastModel aqmast, PayComponent payComponent, String empType, int aqmonth) {
        ArrayList<AqDtlsModel> list = new ArrayList<>();
        int grossAmt = 0;

        for (int i = 0; i < allowanceList.size(); i++) {
            AllowanceDeduction alwded = (AllowanceDeduction) allowanceList.get(i);
            int adAmt = alwded.getAdvalue();
            /*if (alwded.getAdcodename().equalsIgnoreCase("GP")) {
             adAmt = 0;
             }*/
            if (alwded.getAdcodename().equalsIgnoreCase("DA")) {
                if (!aqmast.getEmpType().equals("R") && !aqmast.getEmpType().equals("W") && !aqmast.getEmpType().equals("G") && !aqmast.getEmpType().equals("B") && !aqmast.getEmpType().equals("D")) {
                    adAmt = 0;
                }
            }
            /*if (alwded.getAdcodename().equalsIgnoreCase("CPF")) {
             adAmt = (int) ((aqmast.getCurBasic() + da) * 0.1);
             }*/

            grossAmt = grossAmt + adAmt;
            AqDtlsModel aqModel = new AqDtlsModel();
            aqModel.setDaformula(alwded.getFormula());
            aqModel.setAqGroup(aqmast.getAqGroup());
            aqModel.setAqSlNo(aqmast.getAqSlNo());
            aqModel.setDdoOff("");
            aqModel.setEmpCode(aqmast.getEmpCode());
            aqModel.setPayMon(aqmast.getPayMonth());
            aqModel.setPayYear(aqmast.getPayYear());
            aqModel.setAqDate(aqmast.getAqDate());
            aqModel.setAqMonth(aqmast.getAqMonth());
            aqModel.setAqYear(aqmast.getAqYear());
            aqModel.setAqType(aqmast.getAqType());
            aqModel.setRefOrderNo(aqmast.getRefOrder());
            aqModel.setRefOrderDate(aqmast.getRefDate());
            aqModel.setSlNo(alwded.getRownum());
            aqModel.setAdCode(alwded.getAdcodename());
            aqModel.setAdDesc(alwded.getAddesc());
            aqModel.setAdType(alwded.getAdamttype());
            aqModel.setAltUnit(alwded.getAltUnit());
            aqModel.setDedType(alwded.getDedType());
            aqModel.setDaformula(alwded.getFormula());
            aqModel.setAdAmt(adAmt);
            aqModel.setAccNo(null);
            aqModel.setRefDesc(null);
            aqModel.setRefCount(0);
            aqModel.setSchedule(alwded.getSchedule());
            aqModel.setNowDedn(null);
            aqModel.setTotRecAmt(0);
            aqModel.setRepCol(alwded.getRepCol());
            aqModel.setAdRefId(null);
            if (empType.equals("R") || empType.equals("W") || empType.equals("G") || aqmast.getEmpType().equals("B")) {
                aqModel.setBtId(alwded.getHead());
            } else {
                aqModel.setBtId("000");
            }
            aqModel.setInstalCount(0);
            list.add(aqModel);
        }
        aqmast.setGrossAmt(grossAmt);
        AqDtlsModel aqDtlsModels[] = list.toArray(new AqDtlsModel[list.size()]);
        return aqDtlsModels;
    }

    @Override
    public AqDtlsModel[] getAqDtlsModelForContractualFromAllowanceList(ArrayList allowanceList, AqmastModel aqmast, PayComponent payComponent, String empType, int aqmonth) {
        ArrayList<AqDtlsModel> list = new ArrayList<>();
        int da = 0;
        int grossAmt = 0;
        for (int i = 0; i < allowanceList.size(); i++) {
            AllowanceDeduction alwded = (AllowanceDeduction) allowanceList.get(i);
            int adAmt = alwded.getAdvalue();
            /*if (alwded.getAdcodename().equalsIgnoreCase("GP")) {
             adAmt = 0;
             }*/
            if (alwded.getAdcodename().equalsIgnoreCase("DA")) {
                adAmt = da;
            }
            /*if (alwded.getAdcodename().equalsIgnoreCase("CPF")) {
             adAmt = (int) ((aqmast.getCurBasic() + da) * 0.1);
             }*/
            grossAmt = grossAmt + adAmt;
            AqDtlsModel aqModel = new AqDtlsModel();
            aqModel.setAqGroup(aqmast.getAqGroup());
            aqModel.setAqSlNo(aqmast.getAqSlNo());
            aqModel.setDdoOff("");
            aqModel.setEmpCode(aqmast.getEmpCode());
            aqModel.setPayMon(aqmast.getPayMonth());
            aqModel.setPayYear(aqmast.getPayYear());
            aqModel.setAqDate(aqmast.getAqDate());
            aqModel.setAqMonth(aqmast.getAqMonth());
            aqModel.setAqYear(aqmast.getAqYear());
            aqModel.setAqType(aqmast.getAqType());
            aqModel.setRefOrderNo(aqmast.getRefOrder());
            aqModel.setRefOrderDate(aqmast.getRefDate());
            aqModel.setSlNo(alwded.getRownum());
            aqModel.setAdCode(alwded.getAdcodename());
            aqModel.setAdDesc(alwded.getAddesc());
            aqModel.setAdType(alwded.getAdamttype());
            aqModel.setAltUnit(alwded.getAltUnit());
            aqModel.setDedType(alwded.getDedType());
            aqModel.setAdAmt(adAmt);
            aqModel.setAccNo(null);
            aqModel.setRefDesc(null);
            aqModel.setRefCount(0);
            aqModel.setSchedule(alwded.getSchedule());
            aqModel.setNowDedn(null);
            aqModel.setTotRecAmt(0);
            aqModel.setRepCol(alwded.getRepCol());
            aqModel.setAdRefId(null);
            if (empType.equals("R") || empType.equals("W") || empType.equals("G")) {
                aqModel.setBtId(alwded.getHead());
            } else {
                aqModel.setBtId("000");
            }
            aqModel.setInstalCount(0);
            list.add(aqModel);
        }
        aqmast.setGrossAmt(grossAmt);
        AqDtlsModel aqDtlsModels[] = list.toArray(new AqDtlsModel[list.size()]);
        return aqDtlsModels;
    }

    @Override
    public AqDtlsModel[] getAqDtlsModelFromDeductionList(ArrayList allowanceList, AqmastModel aqmast, PayComponent payComponent) {
        ArrayList<AqDtlsModel> list = new ArrayList<>();

        for (int i = 0; i < allowanceList.size(); i++) {
            AllowanceDeduction alwded = (AllowanceDeduction) allowanceList.get(i);
            long adAmt = alwded.getAdvalue();
            if (alwded.getAdcodename().equals("CPF") || alwded.getAdcodename().equals("GPF") || alwded.getAdcodename().equals("TPF") || alwded.getAdcodename().equals("EPFDED")) {
                adAmt = 0;
            }
            AqDtlsModel aqModel = new AqDtlsModel();
            aqModel.setAqGroup(aqmast.getAqGroup());
            aqModel.setAqSlNo(aqmast.getAqSlNo());
            aqModel.setDdoOff("");
            aqModel.setEmpCode(aqmast.getEmpCode());
            aqModel.setPayMon(aqmast.getPayMonth());
            aqModel.setPayYear(aqmast.getPayYear());
            aqModel.setAqDate(aqmast.getAqDate());
            aqModel.setAqMonth(aqmast.getAqMonth());
            aqModel.setAqYear(aqmast.getAqYear());
            aqModel.setAqType(aqmast.getAqType());
            aqModel.setRefOrderNo(aqmast.getRefOrder());
            aqModel.setRefOrderDate(aqmast.getRefDate());
            aqModel.setSlNo(alwded.getRownum());
            aqModel.setAdCode(alwded.getAdcodename());
            aqModel.setAdDesc(alwded.getAddesc());
            aqModel.setAdType(alwded.getAdamttype());
            aqModel.setDedType(alwded.getDedType());
            aqModel.setAltUnit(alwded.getAltUnit());
            aqModel.setDedType(alwded.getDedType());
            aqModel.setAdAmt(adAmt);
            aqModel.setAccNo(null);
            aqModel.setRefDesc(null);
            aqModel.setRefCount(0);
            aqModel.setSchedule(alwded.getSchedule());
            aqModel.setNowDedn(null);
            aqModel.setTotRecAmt(0);
            aqModel.setRepCol(alwded.getRepCol());
            aqModel.setAdRefId(null);
            aqModel.setBtId(alwded.getHead());
            aqModel.setInstalCount(0);
            list.add(aqModel);
        }
        AqDtlsModel aqDtlsModels[] = list.toArray(new AqDtlsModel[list.size()]);
        return aqDtlsModels;
    }

    @Override
    public ArrayList getFormulaList(String adCode) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList formulaList = new ArrayList();
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT * FROM g_formula WHERE ad_code = ? ORDER BY f_id");
            pstmt.setInt(1, Integer.parseInt(adCode));
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Formula formula = new Formula();
                formula.setFid(rs.getInt("f_id"));
                formula.setFormulaName(rs.getString("formula_name"));
                formulaList.add(formula);

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return formulaList;
    }

    @Override
    public Formula getFormulaDetail(int fid) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Formula formula = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT * FROM g_formula WHERE F_ID=?");
            pstmt.setInt(1, fid);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                formula = new Formula();
                formula.setFormulaName("FORMULA_NAME");
                formula.setOrgformula("ORG_FORMULA");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return formula;
    }

    public Formula getFormulaDetail(String adCode, String formulaname) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Formula formula = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT * FROM g_formula WHERE ad_code = ? and formula_name = ? ");
            pstmt.setInt(1, Integer.parseInt(adCode));
            pstmt.setString(2, formulaname);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                formula = new Formula();
                formula.setFormulaName(rs.getString("FORMULA_NAME"));
                formula.setOrgformula(rs.getString("ORG_FORMULA"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return formula;
    }

    @Override
    public void deleteAdInfo(String empId, String adCode, String wheretoUpdate) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("DELETE FROM update_ad_info "
                    + " WHERE ref_ad_code = ? AND WHERE_UPDATED = ? AND UPDATION_REF_CODE = ?");

            pstmt.setString(1, adCode);
            pstmt.setString(2, wheretoUpdate);
            pstmt.setString(3, empId);
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public ArrayList getofficeWisePostListAdvance(String offCode, String adCode) {
        ArrayList officeList = new ArrayList();
        Connection con = null;
        PreparedStatement pstmt = null;
        PreparedStatement pstmt1 = null;
        PreparedStatement pstmt2 = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        ResultSet rs2 = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT DISTINCT GPC FROM G_SPC WHERE OFF_CODE=? AND SPN is not null AND (IFUCLEAN!='Y' OR IFUCLEAN IS NULL) ");
            pstmt.setString(1, offCode);
            rs = pstmt.executeQuery();
            while (rs.next()) {

                pstmt1 = con.prepareStatement("SELECT POST_CODE,POST FROM G_POST WHERE POST_CODE=? ORDER BY POST");
                pstmt1.setString(1, rs.getString("GPC"));
                rs1 = pstmt1.executeQuery();
                while (rs1.next()) {
                    pstmt2 = con.prepareStatement("select is_fixed, fixedvalue, ad_formula from "
                            + " update_ad_info WHERE ref_ad_code = ? "
                            + " AND where_updated = ? AND updation_ref_code = ?");
                    pstmt2.setString(1, adCode);
                    pstmt2.setString(2, "P");
                    pstmt2.setString(3, offCode + "-" + rs1.getString("POST_CODE"));
                    rs2 = pstmt2.executeQuery();
                    AllowanceDeduction ad = new AllowanceDeduction();
                    ad.setPostName(rs1.getString("POST"));
                    ad.setPostCode(rs1.getString("POST_CODE"));
                    ad.setAdamttype("");
                    ad.setAdvalue(0);
                    ad.setFormula("");
                    while (rs2.next()) {
                        ad.setAdamttype(rs2.getString("is_fixed"));
                        ad.setFixedValue(rs2.getInt("fixedvalue") + "");
                        ad.setFormula(rs2.getString("ad_formula"));
                    }
                    officeList.add(ad);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(rs1, pstmt1);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return officeList;
    }

    @Override
    public Message saveAdvanceAllowanceDeductionDetail(AllowanceDeduction adbean, String offCode) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String nCode = null;
        Message msg = new Message();
        msg.setStatus("Success");
        String strPostIds = null;
        String[] arrPosts = null;
        String[] arrAdmtType = null;
        String[] arrAdVal = null;
        String[] arrFormula = null;
        String updationRefCode = null;
        String postCode = null;
        String fixedType = null;
        String fixedValue = null;
        String formula = null;

        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("DELETE FROM UPDATE_AD_INFO WHERE REF_AD_CODE=? AND WHERE_UPDATED=? AND UPDATION_REF_CODE like '" + offCode + "-%'");
            pstmt.setString(1, adbean.getAdcode());
            pstmt.setString(2, "P");
            pstmt.executeUpdate();
            strPostIds = adbean.getPostCode().replace(",", ", ");
            arrPosts = strPostIds.split(",");

            arrAdmtType = adbean.getAdamttype().replace(",", ", ").split(",");
            arrAdVal = adbean.getFixedValue().replace(",", ", ").split(",");
            arrFormula = adbean.getFormula().split(",");
            for (int i = 0; i < arrPosts.length; i++) {
                postCode = arrPosts[i].trim();
                fixedType = arrAdmtType[i].trim();
                fixedValue = arrAdVal[i].trim();
                formula = arrFormula[i].replace("<=====>", "");
                updationRefCode = offCode + "-" + postCode;
                if (!fixedType.equals("")) {
                    pstmt = con.prepareStatement("INSERT INTO UPDATE_AD_INFO (REF_AD_CODE, AD_FORMULA, IS_FIXED"
                            + ",FIXEDVALUE,AD_ORGFORMULA,WHERE_UPDATED,UPDATION_REF_CODE) values (?,?,?,?,?,?,?)");
                    pstmt.setString(1, adbean.getAdcode());
                    if (fixedType.equals("1")) {
                        pstmt.setString(2, null);
                        pstmt.setInt(3, 1);
                        pstmt.setInt(4, Integer.parseInt(fixedValue));
                        pstmt.setString(5, null);
                    } else if (fixedType.equals("0")) {
                        String str[] = adbean.getFormula().split(",");
                        pstmt.setString(2, formula);
                        pstmt.setInt(3, 0);
                        pstmt.setInt(4, 0);
                        pstmt.setString(5, formula);
                    }
                    pstmt.setString(6, "P");
                    pstmt.setString(7, updationRefCode);
                    pstmt.executeUpdate();
                }
            }

        } catch (Exception e) {
            msg.setStatus("Error");
            msg.setMessage(e.getMessage());
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return msg;
    }

    @Override
    public ArrayList getPrivateDednList() {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        ArrayList pvtDednList = new ArrayList();
        SelectOption so = null;
        try {
            con = this.dataSource.getConnection();

            String sql = "select ad_code,ad_desc from g_ad_list where is_private_dedn='Y' order by ad_desc";
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                so = new SelectOption();
                so.setValue(rs.getString("ad_code"));
                so.setLabel(rs.getString("ad_desc"));
                pvtDednList.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return pvtDednList;
    }

    @Override
    public void uploadDeductionExcelData(String adCode, Workbook workbook) {

        Connection con = null;

        PreparedStatement pst1 = null;
        ResultSet rs1 = null;

        PreparedStatement pst2 = null;

        PreparedStatement pst3 = null;
        String sql2 = null;

        String empId = "";
        int fixedValue = 0;
        String isPrivateDedn = null;
        String btid = null;

        try {
            con = this.dataSource.getConnection();

            String checkDednType = "Select is_private_dedn,bt_id from g_ad_list where ad_code='" + adCode + "'";
            pst1 = con.prepareStatement(checkDednType);
            rs1 = pst1.executeQuery();
            if (rs1.next()) {
                isPrivateDedn = rs1.getString("is_private_dedn");
                btid = rs1.getString("bt_id");
            }

            String sql1 = "select * from update_ad_info where ref_ad_code=? and updation_ref_code=? and where_updated='E'";
            pst1 = con.prepareStatement(sql1);

            sql2 = "insert into update_ad_info(ref_ad_code,is_fixed,fixedvalue,where_updated,updation_ref_code,bt_id) values(?,?,?,?,?,?)";
            pst2 = con.prepareStatement(sql2);

            String sql3 = "update update_ad_info set fixedvalue=? where ref_ad_code=? and updation_ref_code=?";
            pst3 = con.prepareStatement(sql3);

            Sheet firstSheet = workbook.getSheetAt(0);
            int noofRows = firstSheet.getLastRowNum();
            for (int r = 1; r <= noofRows; r++) {
                Row row = firstSheet.getRow(r);
                int index = 0;
                if (row != null) {
                    for (int count = 0; count < row.getLastCellNum(); count++) {
                        Cell cell = row.getCell(count, Row.RETURN_BLANK_AS_NULL);
                        if (cell == null || cell.equals("")) {
                            continue;
                        }
                        index++;
                        switch (cell.getCellType()) {

                            case Cell.CELL_TYPE_STRING:
                                if (index == 1) {
                                    empId = cell.getStringCellValue();
                                }
                                if (index == 2) {
                                    fixedValue = Integer.parseInt(cell.getStringCellValue());
                                }
                                break;
                            case Cell.CELL_TYPE_NUMERIC:
                                if (index == 1) {
                                    empId = cell.getNumericCellValue() + "";
                                    BigDecimal bg = new BigDecimal(empId);
                                    empId = bg.longValue() + "";
                                }
                                if (index == 2) {
                                    Double d = new Double(cell.getNumericCellValue());
                                    fixedValue = d.intValue();
                                }
                                break;
                            case Cell.CELL_TYPE_BLANK:
                                break;
                        }
                    }
                    pst1.setString(1, adCode);
                    pst1.setString(2, empId);
                    rs1 = pst1.executeQuery();
                    if (rs1.next()) {
                        pst3.setInt(1, fixedValue);
                        pst3.setString(2, adCode);
                        pst3.setString(3, empId);
                        pst3.executeUpdate();
                    } else {
                        pst2.setString(1, adCode);
                        pst2.setInt(2, 1);
                        pst2.setInt(3, fixedValue);
                        pst2.setString(4, "E");
                        pst2.setString(5, empId);
                        if (isPrivateDedn.equals("Y")) {
                            pst2.setString(6, null);
                        } else {
                            pst2.setString(6, btid);
                        }
                        pst2.executeUpdate();
                    }
                    //System.out.println("adcode:value:empid:btid-------->" + adCode + ":" + fixedValue + ":" + empId + ":" + btid);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst1, pst2, pst3);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public List getBillWiseAllowanceAndDeductionList(String offCode, String bilGrpId, String adCode) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List employeeList = new ArrayList();
        OfficeWiseAllowanceAndDeductionListBean bean = null;
        int i = 0;
        try {
            con = this.dataSource.getConnection();

            String sql = "select aq_mast.emp_code,gpf_acc_no,emp_name,cur_desg,update_ad_info.ref_ad_code,update_ad_info.fixedvalue from bill_group_master"
                    + " inner join aq_mast on bill_group_master.bill_group_id=aq_mast.aq_group"
                    + " inner join update_ad_info on aq_mast.emp_code=update_ad_info.updation_ref_code"
                    + " inner join g_ad_list on update_ad_info.ref_ad_code=g_ad_list.ad_code"
                    + " where bill_group_master.off_code=? and bill_group_id=? and g_ad_list.ad_code_name=? and emp_name is not null order by emp_name";
            pst = con.prepareStatement(sql);
            pst.setString(1, offCode);
            pst.setBigDecimal(2, BigDecimal.valueOf(Double.parseDouble(bilGrpId)));
            pst.setString(3, adCode);
            rs = pst.executeQuery();
            while (rs.next()) {
                bean = new OfficeWiseAllowanceAndDeductionListBean();
                bean.setEmpid(rs.getString("emp_code"));
                bean.setChkEmployeeId(rs.getString("emp_code") + "-" + i);
                bean.setGpfNo(rs.getString("gpf_acc_no"));
                bean.setEmpname(rs.getString("emp_name"));
                bean.setDesg(rs.getString("cur_desg"));
                bean.setAdAmt(rs.getString("fixedvalue"));
                employeeList.add(bean);
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return employeeList;
    }

    @Override
    public List getDeductionList() {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List allowanceDeductionList = new ArrayList();
        SelectOption so = null;
        try {
            con = this.dataSource.getConnection();

            String sql = "select ad_code_name,ad_desc from g_ad_list where ad_type='D' and group_updation_allowed='Y' order by ad_desc";
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                so = new SelectOption();
                so.setValue(rs.getString("ad_code_name"));
                so.setLabel(rs.getString("ad_desc"));
                allowanceDeductionList.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return allowanceDeductionList;
    }

    @Override
    public void updateDeductionData(OfficeWiseAllowanceAndDeductionForm allowanceDeductionForm) {

        Connection con = null;

        PreparedStatement pst = null;

        try {
            con = this.dataSource.getConnection();

            String refADCode = getDeductionAdCode(allowanceDeductionForm.getSltDeductionName());

            String sql = "update update_ad_info set fixedvalue=? where ref_ad_code=? and updation_ref_code=?";
            pst = con.prepareStatement(sql);

            String[] checkedEmployees = allowanceDeductionForm.getChkEmployee().split(",");
            String[] amount = allowanceDeductionForm.getAdAmt().split(",");

            for (int i = 0; i < checkedEmployees.length; i++) {
                String[] tempEmp = checkedEmployees[i].split("-");
                String empid = tempEmp[0];
                int amtIndex = Integer.parseInt(tempEmp[1]);
                int amt = Integer.parseInt(amount[amtIndex]);
                pst.setInt(1, amt);
                pst.setString(2, refADCode);
                pst.setString(3, empid);
                pst.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void deleteDeductionData(OfficeWiseAllowanceAndDeductionForm allowanceDeductionForm) {

        Connection con = null;

        PreparedStatement pst = null;

        try {
            con = this.dataSource.getConnection();

            String refADCode = getDeductionAdCode(allowanceDeductionForm.getSltDeductionName());

            String sql = "delete from update_ad_info where ref_ad_code=? and updation_ref_code=?";
            pst = con.prepareStatement(sql);

            String[] checkedEmployees = allowanceDeductionForm.getChkEmployee().split(",");

            for (int i = 0; i < checkedEmployees.length; i++) {
                String[] tempEmp = checkedEmployees[i].split("-");
                String empid = tempEmp[0];
                pst.setString(1, refADCode);
                pst.setString(2, empid);
                pst.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    private String getDeductionAdCode(String adCodeName) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String adCode = "";
        try {
            con = this.dataSource.getConnection();

            String sql = "select ad_code from g_ad_list where ad_code_name=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, adCodeName);
            rs = pst.executeQuery();
            if (rs.next()) {
                adCode = rs.getString("ad_code");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return adCode;
    }

    @Override
    public ArrayList billGroupWiseDeductionList(String offcode) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList billGroupList = new ArrayList();
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT * FROM BILL_GROUP_MASTER WHERE OFF_CODE=?  and (is_deleted ='N' or is_deleted  is null)");
            pstmt.setString(1, offcode);
            rs = pstmt.executeQuery();
            int i = 0;
            while (rs.next()) {
                i++;
                AllowanceDeduction billGroup = new AllowanceDeduction();
                billGroup.setSlno(i);
                billGroup.setBillgroupid(rs.getBigDecimal("bill_group_id"));
                billGroup.setBillgroupdesc(rs.getString("description"));

                billGroupList.add(billGroup);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return billGroupList;
    }

    @Override
    public ArrayList billWiseDeductionListDetails(String groupId) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList billGroupList = new ArrayList();
        String empname = "";
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT TRIM(CONCAT(emp_mast.f_name, ' ', emp_mast.m_name, ' ', emp_mast.l_name)) as name,emp_id,update_ad_info.fixedvalue,ref_ad_code,ad_desc FROM emp_mast ,bill_group_master,bill_section_mapping,section_post_mapping,update_ad_info,g_ad_list WHERE "
                    + " emp_mast.cur_spc=section_post_mapping.spc AND section_post_mapping.section_id=bill_section_mapping.section_id AND bill_section_mapping.bill_group_id=bill_group_master.bill_group_id AND "
                    + " emp_mast.emp_id=update_ad_info.updation_ref_code AND update_ad_info.ref_ad_code=g_ad_list.ad_code"
                    + " AND  bill_section_mapping.bill_group_id=? AND bill_group_master. bill_group_id=? ORDER BY emp_mast.f_name");

            pstmt.setBigDecimal(1, BigDecimal.valueOf(Double.parseDouble(groupId)));
            pstmt.setBigDecimal(2, BigDecimal.valueOf(Double.parseDouble(groupId)));
            rs = pstmt.executeQuery();
            int i = 0;
            int k = 0;
            while (rs.next()) {

                AllowanceDeduction billGroup = new AllowanceDeduction();
                if (empname.equals("")) {
                    billGroup.setEmpName(rs.getString("name"));
                    empname = rs.getString("name");
                    i++;
                    k = 2;
                    billGroup.setSlno(i);
                } else if (empname.equals(rs.getString("name"))) {
                    empname = rs.getString("name");
                    billGroup.setEmpName("");
                    billGroup.setSlno(0);
                    k = 1;
                } else {
                    billGroup.setEmpName(rs.getString("name"));
                    empname = rs.getString("name");
                    i++;
                    k = 2;
                    billGroup.setSlno(i);
                }

                billGroup.setCnt(k);
                billGroup.setFixValue(rs.getBigDecimal("fixedvalue"));

                billGroup.setDeductionType(rs.getString("ad_desc"));
                billGroupList.add(billGroup);

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return billGroupList;
    }

    @Override
    public ArrayList getAllowanceDeductionData(String adtype) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        ArrayList adList = new ArrayList();

        SelectOption so = null;
        try {
            con = this.dataSource.getConnection();

            String sql = "select ad_code,ad_desc from g_ad_list where ad_type=? order by ad_desc";
            pst = con.prepareStatement(sql);
            pst.setString(1, adtype);
            rs = pst.executeQuery();
            while (rs.next()) {
                so = new SelectOption();
                so.setValue(rs.getString("ad_code"));
                so.setLabel(rs.getString("ad_desc"));
                adList.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return adList;
    }

    @Override
    public ArrayList getADEmployeeList(String offCode, String adCode) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        ManageADEmployeeList madelistbean = null;
        ArrayList emplist = new ArrayList();
        try {
            con = this.dataSource.getConnection();

            String sql = "SELECT UPDATION_REF_CODE,UPDATE_AD_INFO.REF_AD_CODE,EMP_NAME,EMP_ID,CUR_OFF_CODE,AD_CODE,AD_ORGFORMULA,FIXEDVALUE FROM"
                    + " (SELECT UPDATION_REF_CODE,FIXEDVALUE,AD_ORGFORMULA,REF_AD_CODE,AD_CODE FROM UPDATE_AD_INFO WHERE"
                    + " REF_AD_CODE=? AND WHERE_UPDATED='E')UPDATE_AD_INFO INNER JOIN (SELECT ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMP_NAME,EMP_ID,CUR_OFF_CODE FROM EMP_MAST"
                    + " WHERE CUR_OFF_CODE=?)EMP_MAST ON UPDATE_AD_INFO.UPDATION_REF_CODE=EMP_MAST.EMP_ID ORDER BY EMP_NAME";
            pst = con.prepareStatement(sql);
            pst.setString(1, adCode);
            pst.setString(2, offCode);
            rs = pst.executeQuery();
            while (rs.next()) {
                madelistbean = new ManageADEmployeeList();
                madelistbean.setEmpid(rs.getString("EMP_ID"));
                madelistbean.setEmpname(rs.getString("EMP_NAME"));
                if (rs.getString("FIXEDVALUE") != null && !rs.getString("FIXEDVALUE").equals("")) {
                    madelistbean.setAmt(rs.getString("FIXEDVALUE"));
                } else {
                    madelistbean.setAmt(0 + "");
                }
                emplist.add(madelistbean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return emplist;
    }

    @Override
    public void deleteADEmployee(String offCode, String adCode, String chkEmployee) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = this.dataSource.getConnection();

            String sql = "delete from update_ad_info where updation_ref_code=? and ref_ad_code=?";
            pst = con.prepareStatement(sql);

            String[] empIdArr = chkEmployee.split(",");

            for (int i = 0; i < empIdArr.length; i++) {
                String empId = empIdArr[i];
                pst.setString(1, empId);
                pst.setString(2, adCode);
                pst.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public List getDednList() {
        List deductionList = new ArrayList();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        SelectOption so = null;
        try {
            con = this.dataSource.getConnection();
            String sqlQry = "select ad_code,ad_desc from g_ad_list where ad_type='D' and is_private_dedn <> 'Y'\n"
                    + "and ad_code not in ('54','55','123','76') order by ad_desc";
            ps = con.prepareStatement(sqlQry);
            rs = ps.executeQuery();
            while (rs.next()) {
                so = new SelectOption();
                so.setLabel(rs.getString("ad_desc"));
                so.setValue(rs.getString("ad_code"));
                deductionList.add(so);

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return deductionList;
    }
}
