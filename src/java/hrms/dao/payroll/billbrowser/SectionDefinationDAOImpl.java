/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.payroll.billbrowser;

import hrms.SelectOption;
import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.model.master.SubstantivePost;
import hrms.model.payroll.billbrowser.AssignPostList;
import hrms.model.payroll.billbrowser.SectionDefinition;
import hrms.model.payroll.billbrowser.SectionDtlSPCWiseEmp;
import hrms.model.payroll.grouppayfixation.GroupPayFixation;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Manas Jena
 */
public class SectionDefinationDAOImpl implements SectionDefinationDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public boolean getAerReportSubmittedStatus(String offcode) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        boolean status = false;
        String ddooffcode = "";
        try {
            con = dataSource.getConnection();

            ps = con.prepareStatement("select off_code from g_office "
                    + " where ddo_code=(select ddo_code from g_office "
                    + " where off_code=?) and online_bill_submission='Y' and is_ddo='Y' ");

            ps.setString(1, offcode);
            rs = ps.executeQuery();
            if (rs.next()) {
                ddooffcode = rs.getString("off_code");
            }
            DataBaseFunctions.closeSqlObjects(rs, ps);

            ps = con.prepareStatement("select count(*) cnt from aer_report_submit where off_code=? "
                    + " and is_operator_submitted=? and fy=?");
            ps.setString(1, ddooffcode);
            ps.setString(2, "Y");
            ps.setString(3, "2020-21");
            rs = ps.executeQuery();
            if (rs.next()) {
                if (rs.getInt("cnt") > 0) {
                    status = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return status;
    }

    @Override
    public ArrayList getSectionList(String offcode) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        ArrayList al = new ArrayList();
        try {
            con = dataSource.getConnection();
            /*
             this code need to be check for slow
            
             // off_code='BAMEDN0010000'
            
             pstmt = con.prepareStatement("select g_section.section_id,section_name,BILL_GROUP_MASTER.DESCRIPTION,(SELECT COUNT(*) FROM SECTION_POST_MAPPING WHERE SECTION_ID=g_section.section_id) NOOFPOST, "
             + "(SELECT COUNT(*) FROM SECTION_POST_MAPPING INNER JOIN EMP_MAST ON SECTION_POST_MAPPING.SPC=EMP_MAST.CUR_SPC WHERE SECTION_ID=g_section.section_id)MANINPOSITION from g_section "
             + "LEFT OUTER JOIN BILL_SECTION_MAPPING ON g_section.SECTION_ID = BILL_SECTION_MAPPING.SECTION_ID "
             + "LEFT OUTER JOIN BILL_GROUP_MASTER ON BILL_GROUP_MASTER.BILL_GROUP_ID=BILL_SECTION_MAPPING.BILL_GROUP_ID "
             + "where g_section.off_code=? order by section_name asc");
            
             */

            pstmt = con.prepareStatement("select g_section.section_id,section_name,BILL_GROUP_MASTER.DESCRIPTION "
                    + " from g_section "
                    + "LEFT OUTER JOIN BILL_SECTION_MAPPING ON g_section.SECTION_ID = BILL_SECTION_MAPPING.SECTION_ID "
                    + "LEFT OUTER JOIN BILL_GROUP_MASTER ON BILL_GROUP_MASTER.BILL_GROUP_ID=BILL_SECTION_MAPPING.BILL_GROUP_ID "
                    + "where g_section.off_code=? order by section_name asc");
            pstmt.setString(1, offcode);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                SectionDefinition secDef = new SectionDefinition();
                secDef.setSectionId(rs.getInt("section_id"));
                secDef.setSection(rs.getString("section_name"));
                secDef.setBillgroup(rs.getString("description"));
                //secDef.setNofpost(rs.getInt("noofpost"));
                // secDef.setMenInPos(rs.getInt("maninposition"));
                al.add(secDef);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return al;
    }

    @Override
    public ArrayList getSectionListExcludeCurrentSection(String offcode, int selectedSectionId) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        ArrayList al = new ArrayList();
        try {
            con = dataSource.getConnection();

            pstmt = con.prepareStatement("select g_section.section_id,section_name,BILL_GROUP_MASTER.DESCRIPTION, g_section.bill_type sectiontype, BILL_GROUP_MASTER.bill_type \n"
                    + " from g_section \n"
                    + "LEFT OUTER JOIN BILL_SECTION_MAPPING ON g_section.SECTION_ID = BILL_SECTION_MAPPING.SECTION_ID  "
                    + "LEFT OUTER JOIN BILL_GROUP_MASTER ON BILL_GROUP_MASTER.BILL_GROUP_ID=BILL_SECTION_MAPPING.BILL_GROUP_ID  "
                    + "where g_section.off_code=? and g_section.SECTION_ID <>? order by section_name asc	");
            pstmt.setString(1, offcode);
            pstmt.setInt(2, selectedSectionId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                SectionDefinition secDef = new SectionDefinition();
                secDef.setSectionId(rs.getInt("section_id"));
                secDef.setSection(rs.getString("section_name"));
                secDef.setBillgroup(rs.getString("description"));
                secDef.setBillType(rs.getString("bill_type"));
                secDef.setRadoption(rs.getString("sectiontype"));
                al.add(secDef);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return al;
    }

    @Override
    public ArrayList getBillGroupWiseSectionList(BigDecimal billgroupid) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        ArrayList al = new ArrayList();
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT BILL_SECTION_MAPPING.SECTION_ID,SECTION_NAME,SEC_SL_NO,G_SECTION.BILL_TYPE FROM BILL_SECTION_MAPPING INNER JOIN G_SECTION ON BILL_SECTION_MAPPING.SECTION_ID=G_SECTION.SECTION_ID WHERE BILL_GROUP_ID=?");
            pstmt.setBigDecimal(1, billgroupid);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                SectionDefinition secDef = new SectionDefinition();
                secDef.setSectionId(rs.getInt("SECTION_ID"));
                secDef.setSection(rs.getString("SECTION_NAME"));
                secDef.setSecslno(rs.getInt("SEC_SL_NO"));
                secDef.setBillType(rs.getString("BILL_TYPE"));

                al.add(secDef);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return al;
    }

    @Override
    public ArrayList getSPCWiseContEmpInSection(int sectionid) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        ArrayList empList = new ArrayList();
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT EMP_MAST.EMP_ID,F_NAME, M_NAME, L_NAME,tempjobtype,CUR_BASIC_SALARY,GP FROM SECTION_POST_MAPPING "
                    + " INNER JOIN EMP_MAST ON SECTION_POST_MAPPING.SPC=EMP_MAST.EMP_ID "
                    + " INNER JOIN g_temp_jobtype ON EMP_MAST.JOB_TYPE_ID = g_temp_jobtype.jobtype_code WHERE SECTION_ID=?");
            pstmt.setInt(1, sectionid);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                SectionDtlSPCWiseEmp sdswe = new SectionDtlSPCWiseEmp();
                sdswe.setPostname(rs.getString("post_nomenclature"));
                sdswe.setAcctype(rs.getString("ACCT_TYPE"));
                sdswe.setGpfaccno(rs.getString("GPF_NO"));
                sdswe.setEmpid(rs.getString("EMP_ID"));
                sdswe.setCurBasicSalary(rs.getInt("CUR_BASIC_SALARY"));
                sdswe.setGp(rs.getInt("GP"));
                sdswe.setEmpname((StringUtils.defaultString(rs.getString("F_NAME")) + " " + StringUtils.defaultString(rs.getString("M_NAME")) + " " + StringUtils.defaultString(rs.getString("L_NAME"))).replaceAll("\\s+", " "));
                sdswe.setBankcode(rs.getString("BANK_CODE"));
                sdswe.setBankaccno(rs.getString("BANK_ACC_NO"));
                sdswe.setBranchcode(rs.getString("BRANCH_CODE"));
                empList.add(sdswe);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }

        return empList;
    }

    public SectionDtlSPCWiseEmp getSPCWiseContEmp(String empId) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        SectionDtlSPCWiseEmp sdswe = new SectionDtlSPCWiseEmp();
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement(" SELECT section_post_mapping.spc,post_nomenclature,EMP_ID,POST_SL_NO,CUR_BASIC_SALARY,EMP_MAST.GP,PREV_BASIC_SALARY,PREV_GP,PAY_DATE, "
                    + " F_NAME,M_NAME,L_NAME,DEP_CODE,G_BRANCH.BANK_CODE, G_BRANCH.BRANCH_CODE, G_BRANCH.IFSC_CODE, BANK_ACC_NO,BRASS_NO,ACCT_TYPE,GPF_NO,JOB_TYPE_ID, emp_mast.if_gpf_assumed FROM section_post_mapping "
                    + " INNER JOIN EMP_MAST ON section_post_mapping.SPC = EMP_MAST.EMP_ID "
                    + " LEFT OUTER JOIN g_temp_jobtype ON EMP_MAST.JOB_TYPE_ID=g_temp_jobtype.jobtype_code "
                    + " LEFT OUTER JOIN G_BRANCH ON EMP_MAST.BRANCH_CODE=G_BRANCH.BRANCH_CODE "
                    + " WHERE EMP_MAST.EMP_ID=?");
            pstmt.setString(1, empId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                sdswe.setSpc(rs.getString("spc"));
                sdswe.setPostname(rs.getString("post_nomenclature"));
                sdswe.setAcctype(rs.getString("ACCT_TYPE"));
                sdswe.setGpfaccno(rs.getString("GPF_NO"));
                sdswe.setAccountAssume(rs.getString("if_gpf_assumed"));
                sdswe.setEmpid(rs.getString("EMP_ID"));
                sdswe.setCurBasicSalary(rs.getInt("CUR_BASIC_SALARY"));
                sdswe.setGp(rs.getInt("GP"));
                sdswe.setEmpname((StringUtils.defaultString(rs.getString("F_NAME")) + " " + StringUtils.defaultString(rs.getString("M_NAME")) + " " + StringUtils.defaultString(rs.getString("L_NAME"))).replaceAll("\\s+", " "));
                sdswe.setBankcode(rs.getString("BANK_CODE"));
                sdswe.setBankaccno(rs.getString("BANK_ACC_NO"));
                sdswe.setBranchcode(rs.getString("BRANCH_CODE"));
                sdswe.setIfscCode(rs.getString("IFSC_CODE"));
                sdswe.setJobtypeid(rs.getInt("JOB_TYPE_ID"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return sdswe;
    }

    @Override
    public SectionDtlSPCWiseEmp getSPCEmpSection(String empId) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        SectionDtlSPCWiseEmp sdswe = new SectionDtlSPCWiseEmp();
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT cur_spc,POST,EMP_ID,CUR_BASIC_SALARY,EMP_MAST.GP,PREV_BASIC_SALARY,PREV_GP,PAY_DATE, F_NAME,M_NAME,L_NAME, DEP_CODE, G_BRANCH.BANK_CODE, G_BRANCH.BRANCH_CODE, G_BRANCH.IFSC_CODE, "
                    + " BANK_ACC_NO, G_SPC.OFF_CODE, ACCT_TYPE, GPF_NO, IS_GAZETTED, DOE_GOV, JOB_TYPE_ID, cur_salary, emp_mast.matrix_level, emp_mast.matrix_cell, emp_mast.if_gpf_assumed FROM EMP_MAST "
                    + " LEFT OUTER JOIN G_SPC ON EMP_MAST.CUR_SPC = G_SPC.SPC "
                    + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                    + " LEFT OUTER JOIN G_BRANCH ON EMP_MAST.BRANCH_CODE=G_BRANCH.BRANCH_CODE "
                    + " WHERE EMP_MAST.EMP_ID=?");
            pstmt.setString(1, empId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                sdswe.setSpc(rs.getString("cur_spc"));
                sdswe.setOffcode(rs.getString("OFF_CODE"));
                sdswe.setPostname(rs.getString("POST"));
                sdswe.setEmpid(rs.getString("EMP_ID"));
                sdswe.setEmpname((StringUtils.defaultString(rs.getString("F_NAME")) + " " + StringUtils.defaultString(rs.getString("M_NAME")) + " " + StringUtils.defaultString(rs.getString("L_NAME"))).replaceAll("\\s+", " "));
                sdswe.setGpfaccno(rs.getString("GPF_NO"));
                sdswe.setAccountAssume(rs.getString("if_gpf_assumed"));
                sdswe.setAcctype(rs.getString("ACCT_TYPE"));
                sdswe.setBankcode(rs.getString("BANK_CODE"));
                sdswe.setBankaccno(rs.getString("BANK_ACC_NO"));
                sdswe.setBranchcode(rs.getString("BRANCH_CODE"));
                sdswe.setIfscCode(rs.getString("IFSC_CODE"));
                sdswe.setDoegov(rs.getDate("DOE_GOV"));
                if (rs.getDate("PAY_DATE") != null) {
                    sdswe.setPayDate(new Date(rs.getDate("PAY_DATE").getTime()));
                }
                sdswe.setCurBasicSalary(rs.getInt("CUR_BASIC_SALARY"));
                sdswe.setGp(rs.getInt("GP"));
                sdswe.setPrevBasicSalary(rs.getInt("PREV_BASIC_SALARY"));
                if (rs.getString("matrix_cell") != null && !rs.getString("matrix_cell").equals("")) {
                    sdswe.setPayscale("CELL " + rs.getString("matrix_cell") + " OF LEVEL " + rs.getString("matrix_level"));
                } else {
                    sdswe.setPayscale(rs.getString("cur_salary"));
                }
                //sdswe.setGp(rs.getInt("PREV_GP"));
                sdswe.setIsgazetted(rs.getString("IS_GAZETTED"));
                sdswe.setFname(rs.getString("F_NAME"));
                sdswe.setMname(rs.getString("M_NAME"));
                sdswe.setLname(rs.getString("L_NAME"));
                sdswe.setDepcode(rs.getString("DEP_CODE"));
                sdswe.setJobtypeid(rs.getInt("JOB_TYPE_ID"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return sdswe;
    }

    @Override
    public ArrayList getSPCWiseEmpInSection(int sectionid) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        ArrayList al = new ArrayList();
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT section_post_mapping.spc,POST,EMP_ID,POST_SL_NO,ORDER_NO,ORDER_DATE,PAY_SCALE,"
                    + "CUR_BASIC_SALARY,EMP_MAST.GP,PREV_BASIC_SALARY,PREV_GP,PAY_DATE, "
                    + "F_NAME,M_NAME,L_NAME,DEP_CODE,BANK_CODE, BRANCH_CODE,BANK_ACC_NO,G_SPC.OFF_CODE,DEP_CODE,ACCT_TYPE,GPF_NO,IS_GAZETTED FROM section_post_mapping "
                    + " INNER JOIN G_SPC ON section_post_mapping.SPC = G_SPC.SPC "
                    + " INNER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                    + " LEFT OUTER JOIN EMP_MAST ON section_post_mapping.spc = EMP_MAST.CUR_SPC WHERE section_id=?");
            pstmt.setInt(1, sectionid);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                SectionDtlSPCWiseEmp sdswe = new SectionDtlSPCWiseEmp();
                sdswe.setSpc(rs.getString("spc"));
                sdswe.setOffcode(rs.getString("OFF_CODE"));
                sdswe.setPostname(rs.getString("POST"));
                sdswe.setEmpid(rs.getString("EMP_ID"));
                sdswe.setEmpname((StringUtils.defaultString(rs.getString("F_NAME")) + " " + StringUtils.defaultString(rs.getString("M_NAME")) + " " + StringUtils.defaultString(rs.getString("L_NAME"))).replaceAll("\\s+", " "));
                sdswe.setGpfaccno(rs.getString("GPF_NO"));
                sdswe.setAcctype(rs.getString("ACCT_TYPE"));
                sdswe.setPostslno(rs.getInt("POST_SL_NO"));
                sdswe.setOrderno(rs.getString("ORDER_NO"));
                sdswe.setOrderdate(rs.getDate("ORDER_DATE"));
                sdswe.setPayscale(rs.getString("PAY_SCALE"));
                sdswe.setBankcode(rs.getString("BANK_CODE"));
                sdswe.setBankaccno(rs.getString("BANK_ACC_NO"));
                sdswe.setBranchcode(rs.getString("BRANCH_CODE"));
                if (rs.getDate("PAY_DATE") != null) {
                    sdswe.setPayDate(new Date(rs.getDate("PAY_DATE").getTime()));
                }
                sdswe.setCurBasicSalary(rs.getInt("CUR_BASIC_SALARY"));
                sdswe.setGp(rs.getInt("GP"));
                sdswe.setPrevBasicSalary(rs.getInt("PREV_BASIC_SALARY"));
                //sdswe.setGp(rs.getInt("PREV_GP"));
                sdswe.setIsgazetted(rs.getString("IS_GAZETTED"));
                sdswe.setFname(rs.getString("F_NAME"));
                sdswe.setMname(rs.getString("M_NAME"));
                sdswe.setLname(rs.getString("L_NAME"));
                sdswe.setDepcode(rs.getString("DEP_CODE"));
                al.add(sdswe);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return al;
    }

    @Override
    public ArrayList getSPCWiseEmpInSection(int sectionid, String offcode) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        ArrayList al = new ArrayList();
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT section_post_mapping.spc,POST,EMP_MAST.EMP_ID,POST_SL_NO,ORDER_NO,ORDER_DATE,PAY_SCALE,"
                    + "CUR_BASIC_SALARY,EMP_MAST.GP,PREV_BASIC_SALARY,PREV_GP,PAY_DATE, "
                    + "F_NAME,M_NAME,L_NAME,DEP_CODE,BANK_CODE, BRANCH_CODE,BANK_ACC_NO,G_SPC.OFF_CODE,DEP_CODE,ACCT_TYPE,GPF_NO,IS_GAZETTED,DOE_GOV, FROM_DATE FROM section_post_mapping "
                    + " INNER JOIN G_SPC ON section_post_mapping.SPC = G_SPC.SPC "
                    + " INNER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                    + " LEFT OUTER JOIN EMP_MAST ON section_post_mapping.spc = EMP_MAST.CUR_SPC "
                    + "LEFT OUTER JOIN (SELECT * FROM EMP_PAY_HELDUP WHERE OFF_CODE=? AND TO_DATE IS NULL) AS EMP_PAY_HELDUP ON EMP_MAST.EMP_ID=EMP_PAY_HELDUP.EMP_ID "
                    + "WHERE section_id=?");
            pstmt.setInt(2, sectionid);
            pstmt.setString(1, offcode);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                if (rs.getDate("FROM_DATE") == null) {
                    SectionDtlSPCWiseEmp sdswe = new SectionDtlSPCWiseEmp();
                    sdswe.setSpc(rs.getString("spc"));
                    sdswe.setOffcode(rs.getString("OFF_CODE"));
                    sdswe.setPostname(rs.getString("POST"));
                    sdswe.setEmpid(rs.getString("EMP_ID"));
                    sdswe.setDoegov(rs.getDate("DOE_GOV"));
                    sdswe.setEmpname((StringUtils.defaultString(rs.getString("F_NAME")) + " " + StringUtils.defaultString(rs.getString("M_NAME")) + " " + StringUtils.defaultString(rs.getString("L_NAME"))).replaceAll("\\s+", " "));
                    sdswe.setGpfaccno(rs.getString("GPF_NO"));
                    sdswe.setAcctype(rs.getString("ACCT_TYPE"));
                    sdswe.setPostslno(rs.getInt("POST_SL_NO"));
                    sdswe.setOrderno(rs.getString("ORDER_NO"));
                    sdswe.setOrderdate(rs.getDate("ORDER_DATE"));
                    sdswe.setPayscale(rs.getString("PAY_SCALE"));
                    sdswe.setBankcode(rs.getString("BANK_CODE"));
                    sdswe.setBankaccno(rs.getString("BANK_ACC_NO"));
                    sdswe.setBranchcode(rs.getString("BRANCH_CODE"));
                    if (rs.getDate("PAY_DATE") != null) {
                        sdswe.setPayDate(new Date(rs.getDate("PAY_DATE").getTime()));
                    }
                    sdswe.setCurBasicSalary(rs.getInt("CUR_BASIC_SALARY"));
                    sdswe.setGp(rs.getInt("GP"));
                    sdswe.setPrevBasicSalary(rs.getInt("PREV_BASIC_SALARY"));
                    //sdswe.setGp(rs.getInt("PREV_GP"));
                    sdswe.setIsgazetted(rs.getString("IS_GAZETTED"));
                    sdswe.setFname(rs.getString("F_NAME"));
                    sdswe.setMname(rs.getString("M_NAME"));
                    sdswe.setLname(rs.getString("L_NAME"));
                    sdswe.setDepcode(rs.getString("DEP_CODE"));
                    al.add(sdswe);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return al;
    }

    @Override
    public ArrayList getSPCWiseEmpOnlyInSection(int sectionid) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        ArrayList al = new ArrayList();
        try {
            con = dataSource.getConnection();
            String billType = "";
            pstmt = con.prepareStatement("SELECT BILL_TYPE FROM G_SECTION WHERE SECTION_ID=?");
            pstmt.setInt(1, sectionid);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                billType = rs.getString("BILL_TYPE");
            }

            if (billType.equals("CONTRACTUAL")) {
                pstmt = con.prepareStatement("SELECT EMP_ID,POST_SL_NO, CUR_BASIC_SALARY,EMP_MAST.GP,PREV_BASIC_SALARY,PREV_GP,PAY_DATE, "
                        + "F_NAME,M_NAME,L_NAME,DEP_CODE,BANK_CODE, BRANCH_CODE,BANK_ACC_NO,DEP_CODE,ACCT_TYPE,GPF_NO, POST_NOMENCLATURE,DOE_GOV, PRE_GP FROM section_post_mapping "
                        + "INNER JOIN EMP_MAST ON section_post_mapping.SPC = EMP_MAST.EMP_ID WHERE section_id=? AND EMP_ID != ''");
                pstmt.setInt(1, sectionid);
                rs = pstmt.executeQuery();
                while (rs.next()) {
                    SectionDtlSPCWiseEmp sdswe = new SectionDtlSPCWiseEmp();
                    sdswe.setEmpid(rs.getString("EMP_ID"));
                    sdswe.setPostname(rs.getString("POST_NOMENCLATURE"));
                    sdswe.setEmpname((StringUtils.defaultString(rs.getString("F_NAME")) + " " + StringUtils.defaultString(rs.getString("M_NAME")) + " " + StringUtils.defaultString(rs.getString("L_NAME"))).replaceAll("\\s+", " "));
                    sdswe.setOrderdate(rs.getDate("DOE_GOV"));
                    sdswe.setGp(rs.getInt("PRE_GP"));
                    al.add(sdswe);
                }
            } else {
                pstmt = con.prepareStatement("SELECT section_post_mapping.spc,POST,EMP_ID,POST_SL_NO,ORDER_NO,ORDER_DATE,PAY_SCALE,"
                        + "CUR_BASIC_SALARY,EMP_MAST.GP,PREV_BASIC_SALARY,PREV_GP,PAY_DATE, "
                        + "F_NAME,M_NAME,L_NAME,DEP_CODE,BANK_CODE, BRANCH_CODE,BANK_ACC_NO,G_SPC.OFF_CODE,DEP_CODE,ACCT_TYPE,GPF_NO,IS_GAZETTED FROM section_post_mapping "
                        + " INNER JOIN G_SPC ON section_post_mapping.SPC = G_SPC.SPC "
                        + " INNER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                        + " LEFT OUTER JOIN EMP_MAST ON section_post_mapping.spc = EMP_MAST.CUR_SPC WHERE section_id=? AND EMP_ID != ''");
                pstmt.setInt(1, sectionid);
                rs = pstmt.executeQuery();
                while (rs.next()) {
                    SectionDtlSPCWiseEmp sdswe = new SectionDtlSPCWiseEmp();
                    sdswe.setSpc(rs.getString("SPC"));
                    sdswe.setOffcode(rs.getString("OFF_CODE"));
                    sdswe.setPostname(rs.getString("POST"));
                    sdswe.setEmpid(rs.getString("EMP_ID"));
                    sdswe.setEmpname((StringUtils.defaultString(rs.getString("F_NAME")) + " " + StringUtils.defaultString(rs.getString("M_NAME")) + " " + StringUtils.defaultString(rs.getString("L_NAME"))).replaceAll("\\s+", " "));
                    sdswe.setGpfaccno(rs.getString("GPF_NO"));
                    sdswe.setAcctype(rs.getString("ACCT_TYPE"));
                    sdswe.setPostslno(rs.getInt("POST_SL_NO"));
                    sdswe.setOrderno(rs.getString("ORDER_NO"));
                    sdswe.setOrderdate(rs.getDate("ORDER_DATE"));
                    sdswe.setPayscale(rs.getString("PAY_SCALE"));
                    sdswe.setBankcode(rs.getString("BANK_CODE"));
                    sdswe.setBankaccno(rs.getString("BANK_ACC_NO"));
                    sdswe.setBranchcode(rs.getString("BRANCH_CODE"));
                    if (rs.getDate("PAY_DATE") != null) {
                        sdswe.setPayDate(new Date(rs.getDate("PAY_DATE").getTime()));
                    }
                    sdswe.setCurBasicSalary(rs.getInt("CUR_BASIC_SALARY"));
                    sdswe.setGp(rs.getInt("GP"));
                    sdswe.setPrevBasicSalary(rs.getInt("PREV_BASIC_SALARY"));
                    //sdswe.setGp(rs.getInt("PREV_GP"));
                    sdswe.setIsgazetted(rs.getString("IS_GAZETTED"));
                    sdswe.setFname(rs.getString("F_NAME"));
                    sdswe.setMname(rs.getString("M_NAME"));
                    sdswe.setLname(rs.getString("L_NAME"));
                    sdswe.setDepcode(rs.getString("DEP_CODE"));
                    al.add(sdswe);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return al;
    }

    @Override
    public String getBillType(int sectionid) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        String billtype = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT BILL_TYPE FROM G_SECTION WHERE SECTION_ID = ?");
            pstmt.setInt(1, sectionid);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                billtype = rs.getString("BILL_TYPE");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return billtype;
    }

    public ArrayList getTotalAvailableSixYearContractEmp(String offcode) {
        Connection con = null;
        ArrayList availableEmpList = new ArrayList();
        ResultSet rs = null;
        SelectOption so = null;
        PreparedStatement pstmt = null;
        String ordno = null;
        String orddate = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT * FROM (select  POST_LEVEL,SPC,SPN, ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME, L_NAME,'(',GPF_NO,')'], ' ')  fullname,"
                    + "ORDER_NO,ORDER_DATE,PAY_SCALE from (select CUR_SPC,EMP_ID,INITIALS,F_NAME,M_NAME,L_NAME,GPF_NO from emp_mast where emp_mast.CUR_OFF_CODE = ? AND IS_REGULAR='C')tab1 "
                    + "RIGHT OUTER JOIN (SELECT G_SPC.SPC,SPN,POST_LEVEL,ORDER_NO,ORDER_DATE,SECTION_ID,PAY_SCALE from(SELECT SPC,SPN,POST_LEVEL,ORDER_NO,ORDER_DATE,PAY_SCALE FROM G_SPC WHERE OFF_CODE = ? AND "
                    + "(IFUCLEAN!='Y' OR IFUCLEAN IS NULL) and is_sanctioned='Y')G_SPC left outer join SECTION_POST_MAPPING on G_SPC.spc = SECTION_POST_MAPPING.spc where SECTION_ID is null)G_SPC on G_SPC.SPC=tab1.CUR_SPC ORDER BY SPN)tbl2");
            pstmt.setString(1, offcode);
            pstmt.setString(2, offcode);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                so = new SelectOption();
                so.setValue(rs.getString("SPC"));
                ordno = rs.getString("ORDER_NO");
                orddate = CommonFunctions.getFormattedOutputDate1(rs.getDate("ORDER_DATE"));
                if (ordno != null && (orddate != null && !orddate.equals("")) && rs.getString("fullname") != null && !rs.getString("fullname").equals("")) {
                    so.setLabel(rs.getString("SPN") + " (" + ordno + "," + orddate + ", " + StringUtils.defaultString(rs.getString("PAY_SCALE")) + ")" + " (" + rs.getString("fullname") + ")");
                } else if (rs.getString("fullname") != null && !rs.getString("fullname").equals("") && ordno != null && !ordno.equals("")) {
                    so.setLabel(rs.getString("SPN") + " (" + ordno + ", " + StringUtils.defaultString(rs.getString("PAY_SCALE")) + ")" + " (" + rs.getString("fullname") + ")");
                } else if (rs.getString("fullname") != null && !rs.getString("fullname").equals("") && orddate != null && !orddate.equals("")) {
                    so.setLabel(rs.getString("SPN") + " (" + orddate + ", " + StringUtils.defaultString(rs.getString("PAY_SCALE")) + ")" + " (" + rs.getString("fullname") + ")");
                } else if (orddate != null && !orddate.equals("") && ordno != null && !ordno.equals("")) {
                    so.setLabel(rs.getString("SPN") + " (" + ordno + "," + orddate + ", " + StringUtils.defaultString(rs.getString("PAY_SCALE")) + ")");
                } else if (ordno != null && !ordno.equals("")) {
                    so.setLabel(rs.getString("SPN") + " (" + ordno + ", " + StringUtils.defaultString(rs.getString("PAY_SCALE")) + ")");
                } else if (orddate != null && !orddate.equals("")) {
                    so.setLabel(rs.getString("SPN") + " (" + orddate + ", " + StringUtils.defaultString(rs.getString("PAY_SCALE")) + ")");
                } else if (rs.getString("fullname") != null && !rs.getString("fullname").equals("")) {
                    so.setLabel(rs.getString("SPN") + " (" + rs.getString("fullname") + ")");
                } else {
                    if (rs.getString("PAY_SCALE") != null && !rs.getString("PAY_SCALE").equals("")) {
                        so.setLabel(rs.getString("SPN") + "( " + rs.getString("PAY_SCALE") + " )");
                    } else {
                        so.setLabel(rs.getString("SPN"));
                    }
                }
                availableEmpList.add(so);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return availableEmpList;
    }

    @Override
    public ArrayList getTotalAvailableEmp(String offcode) {
        Connection con = null;
        ArrayList availableEmpList = new ArrayList();
        ResultSet rs = null;
        SelectOption so = null;
        PreparedStatement pstmt = null;
        String ordno = null;
        String orddate = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT * FROM (select  POST_LEVEL,SPC,SPN, ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME, L_NAME,'(',GPF_NO,')'], ' ')  fullname,"
                    + "ORDER_NO,ORDER_DATE,PAY_SCALE from (select CUR_SPC,EMP_ID,INITIALS,F_NAME,M_NAME,L_NAME,GPF_NO from emp_mast where emp_mast.CUR_OFF_CODE = ? AND (IS_REGULAR='Y' or  IS_REGULAR='W'  OR IS_REGULAR='G' or IS_REGULAR='C'))tab1 "
                    + "RIGHT OUTER JOIN (SELECT G_SPC.SPC,SPN,POST_LEVEL,ORDER_NO,ORDER_DATE,SECTION_ID,PAY_SCALE from(SELECT SPC,SPN,POST_LEVEL,ORDER_NO,ORDER_DATE,PAY_SCALE FROM G_SPC WHERE OFF_CODE = ? AND "
                    + "(IFUCLEAN!='Y' OR IFUCLEAN IS NULL) and is_sanctioned='Y' )G_SPC left outer join SECTION_POST_MAPPING on G_SPC.spc = SECTION_POST_MAPPING.spc where SECTION_ID is null)G_SPC on G_SPC.SPC=tab1.CUR_SPC ORDER BY SPN)tbl2");
            pstmt.setString(1, offcode);
            pstmt.setString(2, offcode);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                so = new SelectOption();
                so.setValue(rs.getString("SPC"));
                ordno = rs.getString("ORDER_NO");
                orddate = CommonFunctions.getFormattedOutputDate1(rs.getDate("ORDER_DATE"));
                if (ordno != null && (orddate != null && !orddate.equals("")) && rs.getString("fullname") != null && !rs.getString("fullname").equals("")) {
                    so.setLabel(rs.getString("SPN") + " (" + ordno + "," + orddate + ", " + StringUtils.defaultString(rs.getString("PAY_SCALE")) + ")" + " (" + rs.getString("fullname") + ")");
                } else if (rs.getString("fullname") != null && !rs.getString("fullname").equals("") && ordno != null && !ordno.equals("")) {
                    so.setLabel(rs.getString("SPN") + " (" + ordno + ", " + StringUtils.defaultString(rs.getString("PAY_SCALE")) + ")" + " (" + rs.getString("fullname") + ")");
                } else if (rs.getString("fullname") != null && !rs.getString("fullname").equals("") && orddate != null && !orddate.equals("")) {
                    so.setLabel(rs.getString("SPN") + " (" + orddate + ", " + StringUtils.defaultString(rs.getString("PAY_SCALE")) + ")" + " (" + rs.getString("fullname") + ")");
                } else if (orddate != null && !orddate.equals("") && ordno != null && !ordno.equals("")) {
                    so.setLabel(rs.getString("SPN") + " (" + ordno + "," + orddate + ", " + StringUtils.defaultString(rs.getString("PAY_SCALE")) + ")");
                } else if (ordno != null && !ordno.equals("")) {
                    so.setLabel(rs.getString("SPN") + " (" + ordno + ", " + StringUtils.defaultString(rs.getString("PAY_SCALE")) + ")");
                } else if (orddate != null && !orddate.equals("")) {
                    so.setLabel(rs.getString("SPN") + " (" + orddate + ", " + StringUtils.defaultString(rs.getString("PAY_SCALE")) + ")");
                } else if (rs.getString("fullname") != null && !rs.getString("fullname").equals("")) {
                    so.setLabel(rs.getString("SPN") + " (" + rs.getString("fullname") + ")");
                } else {
                    if (rs.getString("PAY_SCALE") != null && !rs.getString("PAY_SCALE").equals("")) {
                        so.setLabel(rs.getString("SPN") + "( " + rs.getString("PAY_SCALE") + " )");
                    } else {
                        so.setLabel(rs.getString("SPN"));
                    }
                }
                availableEmpList.add(so);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return availableEmpList;
    }

    @Override
    public ArrayList getTotalAssignEmp(String offcode, int sectionid) {
        Connection con = null;
        ArrayList assignEmpList = new ArrayList();
        ResultSet rs = null;
        SelectOption so = null;
        PreparedStatement pstmt = null;
        String ordno = null;
        String orddate = null;
        try {
            con = dataSource.getConnection();

            pstmt = con.prepareStatement("SELECT SPC,SPN,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME, L_NAME],' ') empname, ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME, L_NAME,'(',GPF_NO,')'], ' ')  fullname,"
                    + "POST_SL_NO,ORDER_NO,ORDER_DATE,PAY_SCALE  FROM ( SELECT SPN,G_SPC.SPC,EMP_ID,INITIALS,F_NAME,M_NAME,L_NAME,POST_SL_NO,ORDER_NO,ORDER_DATE,PAY_SCALE,GPF_NO FROM (SELECT SPC,POST_SL_NO FROM SECTION_POST_MAPPING "
                    + "WHERE SECTION_ID=? ORDER BY POST_SL_NO)SECTION_POST_MAPPING LEFT OUTER JOIN (SELECT SPN,SPC,ORDER_NO,ORDER_DATE,PAY_SCALE FROM G_SPC WHERE OFF_CODE=? )G_SPC ON SECTION_POST_MAPPING.SPC=G_SPC.SPC "
                    + "LEFT OUTER JOIN (SELECT EMP_ID,initials,f_name,m_name,l_name,CUR_SPC,GPF_NO FROM EMP_MAST WHERE CUR_OFF_CODE=? AND (DEP_CODE='02' OR DEP_CODE='05'))EMP_MAST ON SECTION_POST_MAPPING.SPC=EMP_MAST.CUR_SPC) SECTION_MAPPING_LIST ORDER BY POST_SL_NO");

            pstmt.setInt(1, sectionid);
            pstmt.setString(2, offcode);
            pstmt.setString(3, offcode);

            rs = pstmt.executeQuery();
            while (rs.next()) {
                AssignPostList asp = new AssignPostList();
                asp.setSpc(rs.getString("SPC"));
                asp.setSectionId(sectionid);
                ordno = rs.getString("ORDER_NO");
                orddate = CommonFunctions.getFormattedOutputDate1(rs.getDate("ORDER_DATE"));

                if (ordno != null && (orddate != null && !orddate.equals("")) && rs.getString("fullname") != null && !rs.getString("fullname").equals("")) {
                    asp.setSpn(rs.getString("SPN") + " (" + ordno + "," + orddate + ", " + StringUtils.defaultString(rs.getString("PAY_SCALE")) + ")" + " (" + rs.getString("fullname") + ")");
                } else if (rs.getString("fullname") != null && !rs.getString("fullname").equals("") && ordno != null && !ordno.equals("")) {
                    asp.setSpn(rs.getString("SPN") + " (" + ordno + ", " + StringUtils.defaultString(rs.getString("PAY_SCALE")) + ")" + " (" + rs.getString("fullname") + ")");
                } else if (rs.getString("fullname") != null && !rs.getString("fullname").equals("") && orddate != null && !orddate.equals("")) {
                    asp.setSpn(rs.getString("SPN") + " (" + orddate + ", " + StringUtils.defaultString(rs.getString("PAY_SCALE")) + ")" + " (" + rs.getString("fullname") + ")");
                } else if (orddate != null && !orddate.equals("") && ordno != null && !ordno.equals("")) {
                    asp.setSpn(rs.getString("SPN") + " (" + ordno + "," + orddate + ", " + StringUtils.defaultString(rs.getString("PAY_SCALE")) + ")");
                } else if (ordno != null && !ordno.equals("")) {
                    asp.setSpn(rs.getString("SPN") + " (" + ordno + ", " + StringUtils.defaultString(rs.getString("PAY_SCALE")) + ")");
                } else if (orddate != null && !orddate.equals("")) {
                    asp.setSpn(rs.getString("SPN") + " (" + orddate + ", " + StringUtils.defaultString(rs.getString("PAY_SCALE")) + ")");
                } else if (rs.getString("empname") != null && !rs.getString("empname").equals("")) {
                    asp.setSpn(rs.getString("SPN") + " (" + rs.getString("fullname") + ")");
                } else {
                    if (rs.getString("PAY_SCALE") != null && !rs.getString("PAY_SCALE").equals("")) {
                        asp.setSpn(rs.getString("SPN") + "( " + rs.getString("PAY_SCALE") + " )");
                    } else {
                        asp.setSpn(rs.getString("SPN"));
                    }
                }
                asp.setSerialNo(rs.getString("POST_SL_NO"));
                if(asp.getSpn() != null && !asp.getSpn().equals("")){
                    assignEmpList.add(asp);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return assignEmpList;
    }

    @Override
    public ArrayList getTotalAvailableContractEmp(String offcode) {
        Connection con = null;
        ArrayList availableEmpList = new ArrayList();
        ResultSet rs = null;
        SelectOption so = null;
        PreparedStatement pstmt = null;
        String usertype = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT EMP_ID,USERTYPE,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME, L_NAME], ' ') FULL_NAME,POST_NOMENCLATURE FROM EMP_MAST WHERE CUR_OFF_CODE=? AND (IS_REGULAR='N' OR IS_REGULAR='B' OR IS_REGULAR='D')  "
                    + "AND EMP_ID NOT IN (SELECT SPC FROM SECTION_POST_MAPPING) ORDER BY F_NAME");
            pstmt.setString(1, offcode);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                so = new SelectOption();
                so.setValue(rs.getString("EMP_ID"));
                if (rs.getString("USERTYPE") != null && !rs.getString("USERTYPE").equals("")) {
                    usertype = " (" + rs.getString("USERTYPE").toUpperCase() + ")";
                } else {
                    usertype = "";
                }
                so.setLabel(rs.getString("POST_NOMENCLATURE") + " (" + rs.getString("FULL_NAME") + ")" + usertype);
                availableEmpList.add(so);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return availableEmpList;
    }

    @Override
    public ArrayList getTotalAssignContractEmp(String offcode, int sectionid) {
        Connection con = null;
        ArrayList assignEmpList = new ArrayList();
        ResultSet rs = null;
        AssignPostList asp = null;
        PreparedStatement pstmt = null;
        String usertype = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME, L_NAME], ' ') FULL_NAME,SECTION_ID,USERTYPE,EMP_MAST.EMP_ID,POST_SL_NO,POST_NOMENCLATURE FROM( "
                    + "SELECT SECTION_ID,SPC,POST_SL_NO FROM SECTION_POST_MAPPING WHERE SECTION_ID=?) SECTION_POST_MAPPING  "
                    + "INNER JOIN EMP_MAST ON SECTION_POST_MAPPING.SPC=EMP_MAST.EMP_ID ORDER BY POST_SL_NO");
            pstmt.setInt(1, sectionid);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                asp = new AssignPostList();
                asp.setSpc(rs.getString("EMP_ID"));
                asp.setSectionId(sectionid);

                asp.setSpn(rs.getString("POST_NOMENCLATURE") + " (" + rs.getString("FULL_NAME") + ")");

                asp.setSerialNo(rs.getString("POST_SL_NO"));
                assignEmpList.add(asp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return assignEmpList;
    }

    @Override
    public String getSectionName(int sectionid) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        String secName = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT SECTION_NAME FROM G_SECTION WHERE SECTION_ID=?");
            pstmt.setInt(1, sectionid);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                secName = rs.getString("SECTION_NAME").toUpperCase();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return secName;
    }

    @Override
    public void mapPost(int sectionid, String spc) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        String billtype = null;

        int postserial = 1;
        try {
            con = this.dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT BILL_TYPE FROM G_SECTION WHERE SECTION_ID = ?");
            pstmt.setInt(1, sectionid);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                billtype = rs.getString("BILL_TYPE");
            }
            DataBaseFunctions.closeSqlObjects(pstmt);

            System.out.println(spc + "bill type==" + sectionid);
            if (billtype == null || billtype.equals("") || billtype.equals("REGULAR")) {
                pstmt = con.prepareStatement("SELECT MAX(POST_SL_NO) POST_SL_NO FROM SECTION_POST_MAPPING WHERE SECTION_ID=?");//GET HRMSID
                pstmt.setInt(1, sectionid);
                rs = pstmt.executeQuery();
                if (rs.next()) {
                    postserial = rs.getInt("POST_SL_NO") + 1;
                } else {
                    postserial = 1;
                }

                DataBaseFunctions.closeSqlObjects(rs, pstmt);

                pstmt = con.prepareStatement("INSERT INTO SECTION_POST_MAPPING (SECTION_ID,SPC,POST_SL_NO) VALUES (?,?,?)");
                pstmt.setInt(1, sectionid);
                pstmt.setString(2, spc);
                pstmt.setInt(3, postserial);
                pstmt.execute();

                DataBaseFunctions.closeSqlObjects(pstmt);
            } else if (billtype != null && (billtype.equals("CONTRACTUAL") || billtype.equals("XCADRE") || billtype.equals("NONGOVTAID") || billtype.equals("SP_CATGORY") || billtype.equals("DEPUTATION"))) {
                System.out.println("billtype:" + billtype);

                pstmt = con.prepareStatement("SELECT MAX(POST_SL_NO) POST_SL_NO FROM SECTION_POST_MAPPING WHERE SECTION_ID=?");//GET HRMSID
                pstmt.setInt(1, sectionid);
                rs = pstmt.executeQuery();
                if (rs.next()) {
                    postserial = rs.getInt("POST_SL_NO") + 1;
                } else {
                    postserial = 1;
                }

                DataBaseFunctions.closeSqlObjects(rs, pstmt);

                pstmt = con.prepareStatement("INSERT INTO SECTION_POST_MAPPING (SECTION_ID,SPC,POST_SL_NO) VALUES (?,?,?)");
                pstmt.setInt(1, sectionid);
                pstmt.setString(2, spc); //GET HRMSID
                pstmt.setInt(3, postserial);
                pstmt.execute();

                DataBaseFunctions.closeSqlObjects(pstmt);
            } else if (billtype != null && billtype.equals("CONT6_REG")) {
                pstmt = con.prepareStatement("SELECT MAX(POST_SL_NO) POST_SL_NO FROM SECTION_POST_MAPPING WHERE SECTION_ID=?");//GET HRMSID
                pstmt.setInt(1, sectionid);
                rs = pstmt.executeQuery();

                if (rs.next()) {
                    postserial = rs.getInt("POST_SL_NO") + 1;
                } else {
                    postserial = 1;
                }

                DataBaseFunctions.closeSqlObjects(rs, pstmt);

                pstmt = con.prepareStatement("UPDATE EMP_MAST SET is_regular='C' WHERE CUR_SPC=?");
                pstmt.setString(1, spc);
                //pstmt.execute();

                DataBaseFunctions.closeSqlObjects(pstmt);

                pstmt = con.prepareStatement("INSERT INTO SECTION_POST_MAPPING (SECTION_ID,SPC,POST_SL_NO) VALUES (?,?,?)");
                pstmt.setInt(1, sectionid);
                pstmt.setString(2, spc);
                pstmt.setInt(3, postserial);
                pstmt.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void removePost(String spc) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("DELETE FROM SECTION_POST_MAPPING WHERE SPC=?");
            pstmt.setString(1, spc);
            pstmt.execute();

            System.out.println("spc==" + spc);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    public int getMaxNumericCode(String tblName, String fieldName) {
        Connection con = null;
        String maxQueryService = "SELECT MAX(" + fieldName + ")+1 as MaxId FROM " + tblName;
        Statement stamt = null;
        ResultSet resultset = null;
        String temp = "";
        int maxId = 1;
        try {
            con = dataSource.getConnection();
            stamt = con.createStatement();
            resultset = stamt.executeQuery(maxQueryService);

            if (resultset.next()) {
                temp = resultset.getString("MaxId");
                if (temp != null && !temp.equals("")) {
                    maxId = Integer.parseInt(temp);
                } else {
                    maxId = 1;
                }

            }

        } catch (SQLException sqe) {
            sqe.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(resultset, stamt);
            DataBaseFunctions.closeSqlObjects(con);
        }

        return maxId;
    }

    @Override
    public ArrayList getSPCList(String sectionId) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        ArrayList al = new ArrayList();
        GroupPayFixation groupPayFix = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT EMP.EMP_ID,GPF_NO,SPC,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMPNAME,mon_basic,REVISED.GP,existing_pay_scale,revised_basic,G_POST.POST FROM SECTION_POST_MAPPING MAPPING"
                    + " INNER JOIN EMP_MAST EMP ON MAPPING.SPC=EMP.CUR_SPC LEFT OUTER JOIN EMP_PAY_REVISED_2016 REVISED ON EMP.EMP_ID=REVISED.EMP_ID LEFT OUTER JOIN G_POST ON REVISED.POST=G_POST.POST_CODE"
                    + "  WHERE MAPPING.SECTION_ID=?");
            pstmt.setInt(1, Integer.parseInt(sectionId));
            rs = pstmt.executeQuery();
            while (rs.next()) {
                groupPayFix = new GroupPayFixation();
                groupPayFix.setEmpId(rs.getString("EMP_ID"));
                groupPayFix.setEmpName(rs.getString("EMPNAME"));
                groupPayFix.setGpfNo(rs.getString("GPF_NO"));
                groupPayFix.setPost(rs.getString("POST"));
                groupPayFix.setCurPayScale(rs.getString("existing_pay_scale"));
                groupPayFix.setPreviousBasic(rs.getString("mon_basic"));
                groupPayFix.setPreviousGp(rs.getString("GP"));
                groupPayFix.setRevisedBasic(rs.getString("revised_basic"));
                al.add(groupPayFix);
            }
        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return al;
    }

    @Override
    public void saveBillSection(String offCode, SectionDefinition SD) {
        int n = 0;
        PreparedStatement pst = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            //String maxCode = CommonFunctions.getMaxCode("g_section", "section_id", con);
            pst = con.prepareStatement("INSERT INTO g_section(off_code,section_name,no_of_emp,bill_type,is_locked) VALUES(?, ?, ?, ?, ?)");
            //pst.setInt(1, Integer.parseInt(maxCode));
            pst.setString(1, offCode);
            pst.setString(2, SD.getSection());
            pst.setInt(3, SD.getNofpost());
            pst.setString(4, SD.getBillType());
            pst.setString(5, "N");

            n = pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    public SectionDefinition getBillSection(int billSectionId) {
        SectionDefinition SD = null;
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            con = this.dataSource.getConnection();
            st = con.createStatement();

            rs = st.executeQuery("SELECT * FROM g_section WHERE section_id = " + billSectionId);

            while (rs.next()) {
                SD = new SectionDefinition();
                SD.setBillType(rs.getString("bill_type"));
                SD.setNofpost(rs.getInt("no_of_emp"));
                SD.setSection(rs.getString("section_name"));
                SD.setSectionId(billSectionId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return SD;
    }

    @Override
    public void updateBillSection(SectionDefinition SD) {
        int n = 0;
        PreparedStatement pst = null;
        Connection con = null;
        int sectionId = SD.getSectionId();
        String sectionName = SD.getSection();
        String billType = SD.getBillType();
        int nofPost = SD.getNofpost();
        try {
            con = dataSource.getConnection();

            pst = con.prepareStatement("UPDATE g_section SET section_name = ?, no_of_emp = ?, bill_type =? WHERE section_id = ?");
            pst.setString(1, sectionName);
            pst.setInt(2, nofPost);
            pst.setString(3, billType);
            pst.setInt(4, sectionId);

            n = pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void updatePosition(String positionNo) {
        PreparedStatement pst = null;
        Connection con = null;

        try {
            con = dataSource.getConnection();

            pst = con.prepareStatement("UPDATE SECTION_POST_MAPPING SET POST_SL_NO=? WHERE SPC =?");
            String str[] = positionNo.split("-");
            for (int i = 0; i < str.length; i++) {
                String str2[] = str[i].split("@");
                pst.setInt(1, Integer.parseInt(str2[1]));
                pst.setString(2, str2[0]);
                pst.execute();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    public void resetPostSlNumber(String offCode, int sectionId) {
        PreparedStatement pst = null;
        PreparedStatement pst2 = null;
        Connection con = null;
        ResultSet rs = null;
        String sql = "";
        int i = 1;
        try {
            con = dataSource.getConnection();

            sql = " select * from SECTION_POST_MAPPING where section_id=?  ORDER BY POST_SL_NO";

            pst = con.prepareStatement("UPDATE SECTION_POST_MAPPING SET POST_SL_NO =? WHERE MAPPING_ID=?");

            pst2 = con.prepareStatement(sql);
            pst2.setInt(1, sectionId);
            // pst2.setString(2, offCode);
            rs = pst2.executeQuery();
            while (rs.next()) {
                pst.setInt(1, i);
                pst.setInt(2, Integer.parseInt(rs.getString("MAPPING_ID")));
                pst.execute();
                i++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public String verifyMapPost(int sectionid, String spc) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        PreparedStatement pst1 = null;
        ResultSet rs1 = null;

        String acctType = "";
        String allowPostMap = "Y";
        try {
            con = this.dataSource.getConnection();

            String sql = "select count(*) cnt from SECTION_POST_MAPPING where SECTION_ID=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, sectionid);
            rs = pst.executeQuery();
            if (rs.next()) {
                if (rs.getInt("cnt") > 0) {
                    String sql1 = "select emp_mast.acct_type from SECTION_POST_MAPPING"
                            + " inner join emp_mast on SECTION_POST_MAPPING.spc=emp_mast.cur_spc where SECTION_ID=? group by acct_type";
                    pst1 = con.prepareStatement(sql1);
                    pst1.setInt(1, sectionid);
                    rs1 = pst1.executeQuery();
                    if (rs1.next()) {
                        acctType = rs1.getString("acct_type");
                        allowPostMap = getAcctTypeOfMappedSPC(acctType, spc);
                    } else {
                        allowPostMap = "Y";
                    }
                }
            }

            if (allowPostMap.equals("Y")) {
                DataBaseFunctions.closeSqlObjects(rs1, pst1);
                DataBaseFunctions.closeSqlObjects(rs, pst);

                sql = "select ifuclean,is_sanctioned from g_spc where spc=?";
                pst = con.prepareStatement(sql);
                pst.setString(1, spc);
                rs = pst.executeQuery();
                if (rs.next()) {
                    if (rs.getString("ifuclean") != null && rs.getString("ifuclean").equals("Y")) {
                        allowPostMap = "I";
                    }
                    if (rs.getString("is_sanctioned") != null && rs.getString("is_sanctioned").equals("N")) {
                        allowPostMap = "I";
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs1, pst1);
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return allowPostMap;
    }

    private String getAcctTypeOfMappedSPC(String acctType, String spc) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String allowPostMap = "Y";
        try {
            con = this.dataSource.getConnection();

            String sql = "select acct_type from emp_mast where cur_spc=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, spc);
            rs = pst.executeQuery();
            if (rs.next()) {
                if (rs.getString("acct_type") != null && !rs.getString("acct_type").equals("")) {
                    if (!acctType.equals(rs.getString("acct_type").trim())) {
                        allowPostMap = "N";
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return allowPostMap;
    }

    @Override
    public SubstantivePost getSpcDetails(String spc) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement st = null;
        SubstantivePost substantivePost = new SubstantivePost();
        try {
            con = dataSource.getConnection();
            st = con.prepareStatement("SELECT * FROM g_spc WHERE spc = ? ");
            st.setString(1, spc);
            rs = st.executeQuery();
            if (rs.next()) {
                substantivePost.setOrderNo("");
                substantivePost.setOrderDate("");
                substantivePost.setGp("");
                if (rs.getString("order_no") != null && !rs.getString("order_no").equals("")) {
                    substantivePost.setOrderNo(rs.getString("order_no"));
                }

                if (rs.getDate("order_date") != null && !rs.getDate("order_date").equals("")) {
                    substantivePost.setOrderDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("order_date")));
                }
                //if (rs.getInt("gp") != null) {
                substantivePost.setGp(rs.getInt("gp") + "");
                //}
                substantivePost.setPostgrp(rs.getString("post_grp"));
                substantivePost.setPaylevel(rs.getString("level_7thpay"));
                substantivePost.setPayscale(rs.getString("pay_scale"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return substantivePost;
    }

    @Override
    public void updateSpcDetails(SubstantivePost sp) {
        PreparedStatement pst = null;
        Connection con = null;
        ResultSet rs = null;
        String sql = "";
        int i = 1;
        try {
            con = dataSource.getConnection();

            pst = con.prepareStatement("UPDATE g_spc SET order_no =?, order_date = ?"
                    + ", pay_scale = ?, gp = ?, level_7thpay = ?, post_grp = ? WHERE spc=?");

            pst.setString(1, sp.getOrderNo());
            if (sp.getOrderDate() != null && !sp.getOrderDate().equals("")) {
                pst.setTimestamp(2, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(sp.getOrderDate()).getTime()));
            } else {
                pst.setTimestamp(2, null);
            }
            pst.setString(3, sp.getPayscale());
            pst.setInt(4, Integer.parseInt(sp.getGp()));
            pst.setInt(5, Integer.parseInt(sp.getPaylevel()));
            pst.setString(6, sp.getPostgrp());
            pst.setString(7, sp.getSpc());
            pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public List sectionwiseSubstantivePostList(int sectionId) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List spnlist = new ArrayList();
        SubstantivePost spost = null;
        try {
            con = this.dataSource.getConnection();

            String sql = "select section_post_mapping.spc,g_spc.spn from section_post_mapping"
                    + " inner join g_spc on section_post_mapping.spc=g_spc.spc where section_id=? order by g_spc.spn";
            pst = con.prepareStatement(sql);
            pst.setInt(1, sectionId);
            rs = pst.executeQuery();
            while (rs.next()) {
                spost = new SubstantivePost();
                spost.setSpc(rs.getString("SPC"));
                spost.setSpn(rs.getString("SPN"));
                spnlist.add(spost);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return spnlist;
    }

    @Override
    public ArrayList getTotalAvailSpecialCategoryEmp(String offcode) {
        Connection con = null;
        ArrayList availableEmpList = new ArrayList();
        ResultSet rs = null;
        SelectOption so = null;
        PreparedStatement pstmt = null;
        String ordno = null;
        String orddate = null;
        String usertype = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT * FROM (select EMP_ID, POST_LEVEL,SPC,SPN, ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME, L_NAME,'(',GPF_NO,')'], ' ')  fullname,\n"
                    + "ORDER_NO,ORDER_DATE,PAY_SCALE,post_nomenclature,usertype from (select CUR_SPC,EMP_ID,INITIALS,F_NAME,M_NAME,L_NAME,GPF_NO,post_nomenclature,usertype from emp_mast where \n"
                    + "emp_mast.CUR_OFF_CODE=? AND IS_REGULAR='A' AND (DEP_CODE='02' OR DEP_CODE='05') \n"
                    + "AND (CUR_SPC NOT IN (SELECT SPC FROM SECTION_POST_MAPPING) or cur_spc is null)\n"
                    + "and (EMP_ID NOT IN (SELECT SPC FROM SECTION_POST_MAPPING)))tab1 \n"
                    + "left OUTER JOIN (SELECT G_SPC.SPC,SPN,POST_LEVEL,ORDER_NO,ORDER_DATE,SECTION_ID,PAY_SCALE from(SELECT SPC,SPN,POST_LEVEL,ORDER_NO,ORDER_DATE,PAY_SCALE \n"
                    + "FROM G_SPC WHERE OFF_CODE = ? AND \n"
                    + "(IFUCLEAN!='Y' OR IFUCLEAN IS NULL) and is_sanctioned='Y' )G_SPC \n"
                    + "left outer join SECTION_POST_MAPPING on \n"
                    + "G_SPC.spc = SECTION_POST_MAPPING.spc where SECTION_ID is null)G_SPC on G_SPC.SPC=tab1.CUR_SPC ORDER BY SPN)tbl2");
            pstmt.setString(1, offcode);
            pstmt.setString(2, offcode);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                so = new SelectOption();

                if (rs.getString("USERTYPE") != null && !rs.getString("USERTYPE").equals("")) {
                    usertype = " (" + rs.getString("USERTYPE").toUpperCase() + ")";
                } else {
                    usertype = "";
                }
                if (rs.getString("SPC") != null && !rs.getString("SPC").equals("")) {
                    so.setValue(rs.getString("SPC"));
                } else {
                    so.setValue(rs.getString("EMP_ID"));
                }

                ordno = rs.getString("ORDER_NO");
                orddate = CommonFunctions.getFormattedOutputDate1(rs.getDate("ORDER_DATE"));

                if (rs.getString("SPN") != null && !rs.getString("SPN").equals("")) {
                    if (ordno != null && (orddate != null && !orddate.equals("")) && rs.getString("fullname") != null && !rs.getString("fullname").equals("")) {
                        so.setLabel(rs.getString("SPN") + " (" + ordno + "," + orddate + ", " + StringUtils.defaultString(rs.getString("PAY_SCALE")) + ")" + " (" + rs.getString("fullname") + ")");
                    } else if (rs.getString("fullname") != null && !rs.getString("fullname").equals("") && ordno != null && !ordno.equals("")) {
                        so.setLabel(rs.getString("SPN") + " (" + ordno + ", " + StringUtils.defaultString(rs.getString("PAY_SCALE")) + ")" + " (" + rs.getString("fullname") + ")");
                    } else if (rs.getString("fullname") != null && !rs.getString("fullname").equals("") && orddate != null && !orddate.equals("")) {
                        so.setLabel(rs.getString("SPN") + " (" + orddate + ", " + StringUtils.defaultString(rs.getString("PAY_SCALE")) + ")" + " (" + rs.getString("fullname") + ")");
                    } else if (orddate != null && !orddate.equals("") && ordno != null && !ordno.equals("")) {
                        so.setLabel(rs.getString("SPN") + " (" + ordno + "," + orddate + ", " + StringUtils.defaultString(rs.getString("PAY_SCALE")) + ")");
                    } else if (ordno != null && !ordno.equals("")) {
                        so.setLabel(rs.getString("SPN") + " (" + ordno + ", " + StringUtils.defaultString(rs.getString("PAY_SCALE")) + ")");
                    } else if (orddate != null && !orddate.equals("")) {
                        so.setLabel(rs.getString("SPN") + " (" + orddate + ", " + StringUtils.defaultString(rs.getString("PAY_SCALE")) + ")");
                    } else if (rs.getString("fullname") != null && !rs.getString("fullname").equals("")) {
                        so.setLabel(rs.getString("SPN") + " (" + rs.getString("fullname") + ")");
                    } else {
                        if (rs.getString("PAY_SCALE") != null && !rs.getString("PAY_SCALE").equals("")) {
                            so.setLabel(rs.getString("SPN") + "( " + rs.getString("PAY_SCALE") + " )");
                        } else {
                            so.setLabel(rs.getString("SPN"));
                        }
                    }
                } else {
                    so.setLabel(rs.getString("POST_NOMENCLATURE") + " (" + rs.getString("fullname") + ")" + usertype);
                }

                availableEmpList.add(so);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return availableEmpList;

    }

    @Override
    public ArrayList getTotalAssignSpecialCategoryEmp(String offcode, int sectionid) {
        Connection con = null;
        ArrayList assignEmpList = new ArrayList();
        ResultSet rs = null;
        SelectOption so = null;
        PreparedStatement pstmt = null;
        String ordno = null;
        String orddate = null;
        try {
            con = dataSource.getConnection();

            pstmt = con.prepareStatement("SELECT EMP_ID,SECTION_MAPPING_LIST.SPCPOST,SPN,POST_NOMENCLATURE, ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME, L_NAME,'(',GPF_NO,')'], ' ')  fullname,\n"
                    + "POST_SL_NO,ORDER_NO,ORDER_DATE,PAY_SCALE  FROM (SELECT SECTION_POST_MAPPING.SPC SPCPOST,SPN,G_SPC.SPC,EMP_ID,POST_NOMENCLATURE,INITIALS,F_NAME,M_NAME,L_NAME,POST_SL_NO,\n"
                    + "ORDER_NO,ORDER_DATE,PAY_SCALE,GPF_NO FROM (SELECT SPC,POST_SL_NO FROM SECTION_POST_MAPPING \n"
                    + "WHERE SECTION_ID=? ORDER BY POST_SL_NO)SECTION_POST_MAPPING LEFT OUTER JOIN (SELECT SPN,SPC,ORDER_NO,ORDER_DATE,PAY_SCALE \n"
                    + "FROM G_SPC WHERE OFF_CODE=? )G_SPC ON SECTION_POST_MAPPING.SPC=G_SPC.SPC\n"
                    + "LEFT OUTER JOIN (SELECT EMP_ID,initials,f_name,m_name,l_name,POST_NOMENCLATURE,CUR_SPC,GPF_NO FROM EMP_MAST WHERE CUR_OFF_CODE=? and IS_REGULAR='A' AND \n"
                    + "(DEP_CODE='02' OR DEP_CODE='05'))EMP_MAST ON (SECTION_POST_MAPPING.SPC=EMP_MAST.CUR_SPC or \n"
                    + "SECTION_POST_MAPPING.SPC=EMP_MAST.emp_id)) SECTION_MAPPING_LIST ORDER BY POST_SL_NO");

            pstmt.setInt(1, sectionid);
            pstmt.setString(2, offcode);
            pstmt.setString(3, offcode);

            rs = pstmt.executeQuery();
            while (rs.next()) {
                AssignPostList asp = new AssignPostList();
                asp.setSpc(rs.getString("SPCPOST"));
                asp.setSectionId(sectionid);
                ordno = rs.getString("ORDER_NO");
                orddate = CommonFunctions.getFormattedOutputDate1(rs.getDate("ORDER_DATE"));
                if (rs.getString("SPN") != null && !rs.getString("SPN").equals("")) {
                    if (ordno != null && (orddate != null && !orddate.equals("")) && rs.getString("fullname") != null && !rs.getString("fullname").equals("")) {
                        asp.setSpn(rs.getString("SPN") + " (" + ordno + "," + orddate + ", " + StringUtils.defaultString(rs.getString("PAY_SCALE")) + ")" + " (" + rs.getString("fullname") + ")");
                    } else if (rs.getString("fullname") != null && !rs.getString("fullname").equals("") && ordno != null && !ordno.equals("")) {
                        asp.setSpn(rs.getString("SPN") + " (" + ordno + ", " + StringUtils.defaultString(rs.getString("PAY_SCALE")) + ")" + " (" + rs.getString("fullname") + ")");
                    } else if (rs.getString("fullname") != null && !rs.getString("fullname").equals("") && orddate != null && !orddate.equals("")) {
                        asp.setSpn(rs.getString("SPN") + " (" + orddate + ", " + StringUtils.defaultString(rs.getString("PAY_SCALE")) + ")" + " (" + rs.getString("fullname") + ")");
                    } else if (orddate != null && !orddate.equals("") && ordno != null && !ordno.equals("")) {
                        asp.setSpn(rs.getString("SPN") + " (" + ordno + "," + orddate + ", " + StringUtils.defaultString(rs.getString("PAY_SCALE")) + ")");
                    } else if (ordno != null && !ordno.equals("")) {
                        asp.setSpn(rs.getString("SPN") + " (" + ordno + ", " + StringUtils.defaultString(rs.getString("PAY_SCALE")) + ")");
                    } else if (orddate != null && !orddate.equals("")) {
                        asp.setSpn(rs.getString("SPN") + " (" + orddate + ", " + StringUtils.defaultString(rs.getString("PAY_SCALE")) + ")");
                    } else if (rs.getString("fullname") != null && !rs.getString("fullname").equals("")) {
                        asp.setSpn(rs.getString("SPN") + " (" + rs.getString("fullname") + ")");
                    } else {
                        if (rs.getString("PAY_SCALE") != null && !rs.getString("PAY_SCALE").equals("")) {
                            asp.setSpn(rs.getString("SPN") + "( " + rs.getString("PAY_SCALE") + " )");
                        } else {
                            asp.setSpn(rs.getString("SPN"));
                        }
                    }
                    asp.setSerialNo(rs.getString("POST_SL_NO"));
                } else {
                    asp.setSpn(rs.getString("POST_NOMENCLATURE") + " (" + rs.getString("fullname") + ")");

                }

                assignEmpList.add(asp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return assignEmpList;
    }

    @Override
    public ArrayList getSPCWiseSpCategEmpInSection(int sectionid) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        ArrayList empList = new ArrayList();
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT section_post_mapping.spc,post_nomenclature,EMP_ID,POST_SL_NO,CUR_BASIC_SALARY,EMP_MAST.GP,PREV_BASIC_SALARY,PREV_GP,PAY_DATE,\n"
                    + "F_NAME,M_NAME,L_NAME,DEP_CODE,EMP_MAST.BANK_CODE, EMP_MAST.BRANCH_CODE,BANK_ACC_NO, g_branch.ifsc_code, BRASS_NO,ACCT_TYPE,GPF_NO,emp_mast.if_gpf_assumed,JOB_TYPE_ID FROM section_post_mapping\n"
                    + "left outer JOIN EMP_MAST ON (section_post_mapping.SPC = EMP_MAST.EMP_ID or section_post_mapping.SPC = EMP_MAST.cur_spc)\n"
                    + "LEFT OUTER JOIN g_temp_jobtype ON EMP_MAST.JOB_TYPE_ID=g_temp_jobtype.jobtype_code\n"
                    + "LEFT OUTER JOIN G_BRANCH ON EMP_MAST.BRANCH_CODE=G_BRANCH.BRANCH_CODE\n"
                    + "WHERE section_id=? ORDER BY post_sl_no");

            pstmt.setInt(1, sectionid);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                SectionDtlSPCWiseEmp sdswe = new SectionDtlSPCWiseEmp();
                sdswe.setPostname(rs.getString("post_nomenclature"));
                sdswe.setAccountAssume(rs.getString("if_gpf_assumed"));
                sdswe.setAcctype(rs.getString("ACCT_TYPE"));
                sdswe.setGpfaccno(rs.getString("GPF_NO"));
                sdswe.setEmpid(rs.getString("EMP_ID"));
                sdswe.setCurBasicSalary(rs.getInt("CUR_BASIC_SALARY"));
                sdswe.setGp(rs.getInt("GP"));
                sdswe.setEmpname((StringUtils.defaultString(rs.getString("F_NAME")) + " " + StringUtils.defaultString(rs.getString("M_NAME")) + " " + StringUtils.defaultString(rs.getString("L_NAME"))).replaceAll("\\s+", " "));
                sdswe.setBankcode(rs.getString("BANK_CODE"));
                sdswe.setBankaccno(rs.getString("BANK_ACC_NO"));
                sdswe.setBranchcode(rs.getString("BRANCH_CODE"));
                sdswe.setIfscCode(rs.getString("ifsc_code"));
                sdswe.setJobtypeid(rs.getInt("JOB_TYPE_ID"));
                sdswe.setPayheldup("N");
                sdswe.setDepcode("02");
                empList.add(sdswe);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }

        return empList;
    }

    @Override
    public ArrayList getTotalAvailDeputationEmp(String deptCode) {
        Connection con = null;
        ArrayList availableEmpList = new ArrayList();
        ResultSet rs = null;
        SelectOption so = null;
        PreparedStatement pstmt = null;

        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("select emp_id,deputation_offcode,deputation_spc,spn,cur_spc,dep_code,cur_off_code,\n"
                    + "ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') FULL_NAME,SPN\n"
                    + "from emp_mast \n"
                    + "inner join g_office on g_office.off_code=emp_mast.deputation_offcode\n"
                    + "inner join g_spc on g_spc.spc=emp_mast.deputation_spc\n"
                    + "where (deputation_offcode is not null and deputation_offcode <> '') \n"
                    + "and (deputation_spc is not null and deputation_spc <> '') and dep_code='07'\n"
                    + "AND (deputation_spc NOT IN (SELECT SPC FROM SECTION_POST_MAPPING) or deputation_spc is null)\n"
                    + "and department_code=? AND (IS_REGULAR='Y' or  IS_REGULAR='W'  OR IS_REGULAR='G' or IS_REGULAR='C') ORDER BY SPN");
            pstmt.setString(1, deptCode);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                so = new SelectOption();

                if (rs.getString("deputation_spc") != null && !rs.getString("deputation_spc").equals("")) {
                    so.setValue(rs.getString("deputation_spc"));
                }
                if (rs.getString("SPN") != null && !rs.getString("SPN").equals("")) {
                    if (rs.getString("FULL_NAME") != null && !rs.getString("FULL_NAME").equals("")) {
                        so.setLabel(rs.getString("spn") + " (" + rs.getString("FULL_NAME") + ")");
                    }
                }
                availableEmpList.add(so);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return availableEmpList;

    }

    @Override
    public ArrayList getTotalAssignDeputationEmp(int sectionid) {
        Connection con = null;
        ArrayList assignEmpList = new ArrayList();
        ResultSet rs = null;
        AssignPostList asp = null;
        PreparedStatement pstmt = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME, L_NAME], ' ') FULL_NAME,SECTION_ID,USERTYPE,EMP_MAST.EMP_ID,POST_SL_NO,\n"
                    + "POST_NOMENCLATURE,deputation_spc,deputation_offcode,spn FROM(\n"
                    + "SELECT SECTION_ID,SPC,POST_SL_NO FROM SECTION_POST_MAPPING WHERE SECTION_ID=?) SECTION_POST_MAPPING\n"
                    + "INNER JOIN EMP_MAST ON SECTION_POST_MAPPING.SPC=EMP_MAST.DEPUTATION_SPC \n"
                    + "left outer join g_spc on g_spc.spc=emp_mast.deputation_spc where dep_code='07'  ORDER BY POST_SL_NO");
            pstmt.setInt(1, sectionid);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                asp = new AssignPostList();
                asp.setSpc(rs.getString("deputation_spc"));
                asp.setSectionId(sectionid);

                asp.setSpn(rs.getString("spn") + " (" + rs.getString("FULL_NAME") + ")");

                asp.setSerialNo(rs.getString("POST_SL_NO"));
                assignEmpList.add(asp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return assignEmpList;
    }

}
