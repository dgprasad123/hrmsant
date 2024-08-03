/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.servicecloser;

import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.dao.leaveAccount.ServiceBookLanguageDAO;
import hrms.model.servicecloser.EmployeeDeceased;
import hrms.model.servicecloser.Retirement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 *
 * @author Manas
 */
public class ServiceCloserDAOImpl implements ServiceCloserDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    protected ServiceBookLanguageDAO sbDAO;

    public void setSbDAO(ServiceBookLanguageDAO sbDAO) {
        this.sbDAO = sbDAO;
    }

    @Override
    public EmployeeDeceased getEmployeeDeceasedData(String empId) {
        EmployeeDeceased empDeceased = new EmployeeDeceased();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = this.dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT EMP_ID,DATE_OF_DECEASED,DECEASED_TIME,DECEASED_NOTE,DECEASED_TYPE FROM EMP_DECEASED WHERE EMP_ID=?");
            pstmt.setString(1, empId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                empDeceased.setDeceasedDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("DATE_OF_DECEASED")));
                empDeceased.setDeceasednote(rs.getString("DECEASED_NOTE"));
                empDeceased.setDeceasedTime(rs.getString("DECEASED_TIME"));
                empDeceased.setEmpId(rs.getString("EMP_ID"));
                empDeceased.setDeceasedType(rs.getString("DECEASED_TYPE"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return empDeceased;
    }

    @Override
    public void saveEmployeeDeceased(EmployeeDeceased employeeDeceased) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = this.dataSource.getConnection();
            pstmt = con.prepareStatement("INSERT INTO EMP_DECEASED (EMP_ID,SPC,DATE_OF_DECEASED,DECEASED_TIME,DECEASED_NOTE,DECEASED_TYPE) VALUES (?,?,?,?,?,?)");
            pstmt.setString(1, employeeDeceased.getEmpId());
            pstmt.setString(2, employeeDeceased.getCurspc());
            pstmt.setDate(3, new java.sql.Date(CommonFunctions.getDateFromString(employeeDeceased.getDeceasedDate(), "dd-MMM-yyyy").getTime()));
            pstmt.setString(4, employeeDeceased.getDeceasedTime());
            pstmt.setString(5, employeeDeceased.getDeceasednote());
            pstmt.setString(6, employeeDeceased.getDeceasedType());
            pstmt.execute();

            pstmt = con.prepareStatement("UPDATE EMP_MAST SET DEP_CODE='10',CUR_SPC=null WHERE EMP_ID =?");
            pstmt.setString(1, employeeDeceased.getEmpId());
            pstmt.execute();

            String rid = null;
            pstmt = con.prepareStatement("INSERT INTO EMP_RELIEVE(NOT_TYPE, NOT_ID, EMP_ID, RLV_DATE, RLV_TIME, SPC, IF_VISIBLE) VALUES (?,?,?,?,?,?,?)");
            //rid = CommonFunctions.getMaxCode("emp_relieve", "RELIEVE_ID", con);
            pstmt.setString(1, "DECEASED");
            pstmt.setInt(2, 0);
            pstmt.setString(3, employeeDeceased.getEmpId());
            pstmt.setDate(4, new java.sql.Date(CommonFunctions.getDateFromString(employeeDeceased.getDeceasedDate(), "dd-MMM-yyyy").getTime()));
            pstmt.setString(5, "AN");
            pstmt.setString(6, employeeDeceased.getCurspc());
            pstmt.setString(7, "Y");
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public Retirement getEmployeeRetirementData(String empId) {
        Retirement retirement = new Retirement();
        PreparedStatement pst = null;
        Connection con = null;
        ResultSet rs = null;

        PreparedStatement relievepst = null;
        ResultSet relievers = null;
        String relieveId = null;
        try {
            con = dataSource.getConnection();
            retirement.setNotType("GOO");
            retirement.setRetauthtyp("GOO");
            retirement.setRetired("Y");
            pst = con.prepareStatement("SELECT RET_ID,NOT_ID,RE_DEPT, RE_OFF, RE_SPC,ord_no,ord_date,auth_dept,auth_off,auth,ret_type,sb_description FROM emp_ret_res WHERE EMP_ID=?");
            pst.setString(1, empId);
            rs = pst.executeQuery();
            if (rs.next()) {
                retirement.setCurspc(rs.getString("RE_SPC"));
                retirement.setRetid(rs.getString("RET_ID"));
                retirement.setNotId(rs.getString("NOT_ID"));
                retirement.setOrdno(rs.getString("ord_no"));
                retirement.setOrdDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("ord_date")));
                retirement.setAuthDeptCode(rs.getString("auth_dept"));
                retirement.setAuthOfficeCode(rs.getString("auth_off"));
                retirement.setAuthSpc(rs.getString("auth"));
                retirement.setRettype(rs.getString("ret_type"));
                retirement.setRetiredfromdeptCode(rs.getString("RE_DEPT"));
                retirement.setRetiredfromofficeCode(rs.getString("RE_OFF"));
                retirement.setRetiredfromspc(rs.getString("RE_SPC"));
                retirement.setSbDescription(rs.getString("sb_description"));

                if (retirement.getNotId() != null && !retirement.getNotId().equals("")) {
                    relievepst = con.prepareStatement("select relieve_id from emp_relieve where not_id=?");
                    relievepst.setInt(1, Integer.parseInt(retirement.getNotId()));
                    relievers = relievepst.executeQuery();
                    if (relievers.next()) {
                        relieveId = relievers.getString("relieve_id");
                        retirement.setRelieveId(relieveId);
                    }
                }
            }
            if (retirement.getRetid() == null) {
                pst = con.prepareStatement("SELECT CUR_SPC,G_OFFICE.department_code,G_OFFICE.off_code\n"
                        + "FROM EMP_MAST INNER JOIN G_SPC ON EMP_MAST.CUR_SPC = G_SPC.SPC \n"
                        + "INNER JOIN G_OFFICE ON EMP_MAST.CUR_OFF_CODE=G_OFFICE.OFF_CODE\n"
                        + "WHERE EMP_ID=?");
                pst.setString(1, empId);
                rs = pst.executeQuery();
                if (rs.next()) {
                    retirement.setRetiredfromdeptCode(rs.getString("department_code"));
                    retirement.setRetiredfromofficeCode(rs.getString("off_code"));
                    retirement.setRetiredfromspc(rs.getString("CUR_SPC"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(relievers, relievepst);
            DataBaseFunctions.closeSqlObjects(rs, pst, con);
        }
        return retirement;
    }

    @Override
    public void saveEmployeeRetirement(Retirement retire) {
        PreparedStatement pst = null;
        Connection con = null;
        int notId = 0;
        try {
            con = this.dataSource.getConnection();

            pst = con.prepareStatement("INSERT INTO EMP_NOTIFICATION(NOT_TYPE,EMP_ID,DOE,TOE,IF_ASSUMED,ORDNO,ORDDT,DEPT_CODE,OFF_CODE,AUTH,NOTE,IF_VISIBLE,ENT_DEPT,ENT_OFF,ENT_AUTH,ACS,ASCS) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, "RETIREMENT");
            pst.setString(2, retire.getRetiredEmpid());
            pst.setDate(3, new java.sql.Date(new Date().getTime()));
            pst.setString(4, retire.getToe());
            pst.setString(5, retire.getIfAssumed());
            pst.setString(6, retire.getOrdno());
            pst.setDate(7, new java.sql.Date(CommonFunctions.getDateFromString(retire.getOrdDate(), "dd-MMM-yyyy").getTime()));
            pst.setString(8, retire.getAuthDeptCode());
            pst.setString(9, retire.getAuthOfficeCode());
            pst.setString(10, retire.getAuthSpc());
            pst.setString(11, retire.getNote());
            pst.setString(12, retire.getIfVisible());
            pst.setString(13, retire.getEntDeptCode());
            pst.setString(14, retire.getEntOfficeCode());
            pst.setString(15, retire.getEntAuthCode());
            pst.setString(16, retire.getCadreStatus());
            pst.setString(17, retire.getSubStatus());
            pst.executeUpdate();
            ResultSet rs = pst.getGeneratedKeys();
            rs.next();
            notId = rs.getInt("NOT_ID");

            pst = con.prepareStatement("INSERT INTO emp_ret_res (emp_id, doe, TOE, IF_ASSUMED, ret_type, dos, retired, ORD_NO, ORD_DATE, AUTH_DEPT, AUTH_OFF, AUTH, RE_DEPT, RE_OFF, RE_SPC, NOTE, ent_dept, ent_off, ent_auth, not_id) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            pst.setString(1, retire.getRetiredEmpid());
            pst.setDate(2, new java.sql.Date(new Date().getTime()));
            pst.setString(3, retire.getToe());
            pst.setString(4, retire.getIfAssumed());
            pst.setString(5, retire.getRettype());
            pst.setDate(6, new java.sql.Date(CommonFunctions.getDateFromString(retire.getDuedate(), "dd-MMM-yyyy").getTime()));
            pst.setString(7, retire.getRetired());
            pst.setString(8, retire.getOrdno().toUpperCase());
            pst.setDate(9, new java.sql.Date(CommonFunctions.getDateFromString(retire.getOrdDate(), "dd-MMM-yyyy").getTime()));
            pst.setString(10, retire.getAuthDeptCode());
            pst.setString(11, retire.getAuthOfficeCode());
            pst.setString(12, retire.getAuthSpc());
            pst.setString(13, retire.getRetiredfromdeptCode());
            pst.setString(14, retire.getRetiredfromofficeCode());
            pst.setString(15, retire.getRetiredfromspc());
            pst.setString(16, retire.getNote());
            pst.setString(17, retire.getEntDeptCode());
            pst.setString(18, retire.getEntOfficeCode());
            pst.setString(19, retire.getEntAuthCode());
            pst.setInt(20, notId);
            pst.executeUpdate();

            //String rid = CommonFunctions.getMaxCode("emp_relieve", "RELIEVE_ID", con);
            pst = con.prepareStatement("INSERT INTO emp_relieve( NOT_TYPE, NOT_ID, EMP_ID, DOE, MEMO_NO, MEMO_DATE, RLV_DATE, RLV_TIME, SPC, IF_VISIBLE,ENT_DEPT,ENT_OFF,ENT_AUTH) values (?,?,?,?,?,?,?,?,?,?,?,?,?)");
            pst.setString(1, "RETIREMENT");
            pst.setInt(2, notId);
            pst.setString(3, retire.getRetiredEmpid());
            pst.setDate(4, new java.sql.Date(new Date().getTime()));
            pst.setString(5, retire.getOrdno().toUpperCase());
            pst.setDate(6, new java.sql.Date(CommonFunctions.getDateFromString(retire.getOrdDate(), "dd-MMM-yyyy").getTime()));
            pst.setDate(7, new java.sql.Date(CommonFunctions.getDateFromString(retire.getDuedate(), "dd-MMM-yyyy").getTime()));
            pst.setString(8, "AN");
            pst.setString(9, retire.getRetiredfromspc());
            pst.setString(10, "Y");
            pst.setString(11, retire.getEntDeptCode());
            pst.setString(12, retire.getEntOfficeCode());
            pst.setString(13, retire.getEntAuthCode());
            pst.executeUpdate();

            pst = con.prepareStatement("UPDATE EMP_MAST SET IF_RETIRED=?, DEP_CODE='09' WHERE EMP_ID=?");
            pst.setString(1, retire.getRetired());
            pst.setString(2, retire.getRetiredEmpid());
            pst.executeUpdate();

            /*
             * Updating the Service Book Language
             */
            String sbLang = sbDAO.getRetirementDetails(retire);
            pst = con.prepareStatement("UPDATE EMP_NOTIFICATION SET SB_DESCRIPTION =? WHERE NOT_ID=? ");
            pst.setString(1, sbLang);
            pst.setInt(2, notId);
            pst.execute();

            pst = con.prepareStatement("UPDATE emp_ret_res SET SB_DESCRIPTION =? WHERE NOT_ID=? ");
            pst.setString(1, sbLang);
            pst.setInt(2, notId);
            pst.execute();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void updateEmployeeRetirement(Retirement retire) {

        PreparedStatement pst = null;
        Connection con = null;
        int notId = 0;
        try {
            con = this.dataSource.getConnection();

            if (retire.getNotId() != null && !retire.getNotId().equals("")) {
                notId = Integer.parseInt(retire.getNotId());
                pst = con.prepareStatement("UPDATE EMP_NOTIFICATION SET ORDNO=?,ORDDT=?,DEPT_CODE=?,OFF_CODE=?,AUTH=?,ENT_DEPT=?,ENT_OFF=?,ENT_AUTH=? WHERE NOT_ID=?");
                pst.setString(1, retire.getOrdno());
                pst.setDate(2, new java.sql.Date(CommonFunctions.getDateFromString(retire.getOrdDate(), "dd-MMM-yyyy").getTime()));
                pst.setString(3, retire.getAuthDeptCode());
                pst.setString(4, retire.getAuthOfficeCode());
                pst.setString(5, retire.getAuthSpc());
                pst.setString(6, retire.getEntDeptCode());
                pst.setString(7, retire.getEntOfficeCode());
                pst.setString(8, retire.getEntAuthCode());
                pst.setInt(9, notId);
                pst.executeUpdate();
            }

            if (retire.getRetid() != null && !retire.getRetid().equals("")) {
                pst = con.prepareStatement("UPDATE emp_ret_res SET ret_type=?, dos=?, retired=?, ORD_NO=?, ORD_DATE=?, AUTH_DEPT=?, AUTH_OFF=?, AUTH=?, RE_DEPT=?, RE_OFF=?, RE_SPC=?, NOTE=?, ent_dept=?, ent_off=?, ent_auth=? where ret_id=?");
                pst.setString(1, retire.getRettype());
                pst.setDate(2, new java.sql.Date(CommonFunctions.getDateFromString(retire.getDuedate(), "dd-MMM-yyyy").getTime()));
                pst.setString(3, retire.getRetired());
                pst.setString(4, retire.getOrdno());
                pst.setDate(5, new java.sql.Date(CommonFunctions.getDateFromString(retire.getOrdDate(), "dd-MMM-yyyy").getTime()));
                pst.setString(6, retire.getAuthDeptCode());
                pst.setString(7, retire.getAuthOfficeCode());
                pst.setString(8, retire.getAuthSpc());
                pst.setString(9, retire.getRetiredfromdeptCode());
                pst.setString(10, retire.getRetiredfromofficeCode());
                pst.setString(11, retire.getRetiredfromspc());
                pst.setString(12, retire.getNote());
                pst.setString(13, retire.getEntDeptCode());
                pst.setString(14, retire.getEntOfficeCode());
                pst.setString(15, retire.getEntAuthCode());
                pst.setInt(16, Integer.parseInt(retire.getRetid()));
                pst.executeUpdate();
            }

            if (retire.getRelieveId() != null && !retire.getRelieveId().equals("")) {
                pst = con.prepareStatement("UPDATE emp_relieve set MEMO_NO=?, MEMO_DATE=?, RLV_DATE=?, RLV_TIME=?, SPC=?,ENT_DEPT=?,ENT_OFF=?,ENT_AUTH=? WHERE RELIEVE_ID=?");
                pst.setString(1, retire.getOrdno());
                pst.setDate(2, new java.sql.Date(CommonFunctions.getDateFromString(retire.getOrdDate(), "dd-MMM-yyyy").getTime()));
                pst.setDate(3, new java.sql.Date(CommonFunctions.getDateFromString(retire.getDuedate(), "dd-MMM-yyyy").getTime()));
                pst.setString(4, "AN");
                pst.setString(5, retire.getRetiredfromspc());
                pst.setString(6, retire.getEntDeptCode());
                pst.setString(7, retire.getEntOfficeCode());
                pst.setString(8, retire.getEntAuthCode());
                pst.setInt(9, Integer.parseInt(retire.getRelieveId()));
                pst.executeUpdate();
            }
            /*
             * Updating the Service Book Language
             */
            String sbLang = sbDAO.getRetirementDetails(retire);
            pst = con.prepareStatement("UPDATE EMP_NOTIFICATION SET SB_DESCRIPTION =? WHERE NOT_ID=? ");
            pst.setString(1, sbLang);
            pst.setInt(2, notId);
            pst.execute();

            pst = con.prepareStatement("UPDATE emp_ret_res SET SB_DESCRIPTION =? WHERE NOT_ID=? ");
            pst.setString(1, sbLang);
            pst.setInt(2, notId);
            pst.execute();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void deleteEmployeeDeceased(EmployeeDeceased employeeDeceased) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String deceasedspc = "";
        try {
            con = this.dataSource.getConnection();

            String sql = "select * from EMP_DECEASED where emp_id=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, employeeDeceased.getEmpId());
            rs = pst.executeQuery();
            if (rs.next()) {
                deceasedspc = rs.getString("spc");
            }

            DataBaseFunctions.closeSqlObjects(rs, pst);

            if (!deceasedspc.equals("")) {
                sql = "delete from EMP_DECEASED where emp_id=?";
                pst = con.prepareStatement(sql);
                pst.setString(1, employeeDeceased.getEmpId());
                pst.executeUpdate();

                DataBaseFunctions.closeSqlObjects(rs, pst);

                sql = "delete from EMP_RELIEVE where emp_id=? and not_id=0 and not_type='DECEASED' and spc=?";
                pst = con.prepareStatement(sql);
                pst.setString(1, employeeDeceased.getEmpId());
                pst.setString(2, deceasedspc);
                pst.executeUpdate();

                DataBaseFunctions.closeSqlObjects(rs, pst);

                sql = "update emp_mast set cur_spc=?,dep_code='02' where emp_id=?";
                pst = con.prepareStatement(sql);
                pst.setString(1, deceasedspc);
                pst.setString(2, employeeDeceased.getEmpId());
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
    public void deleteEmployeeRetirement(Retirement retire) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String retiredspc = "";
        try {
            con = this.dataSource.getConnection();

            String sql = "select * from emp_ret_res where ret_id=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(retire.getRetid()));
            rs = pst.executeQuery();
            if (rs.next()) {
                retiredspc = rs.getString("RE_SPC");
            }

            DataBaseFunctions.closeSqlObjects(rs, pst);

            if (retire.getNotId() != null && !retire.getNotId().equals("")) {
                sql = "delete from emp_notification where not_id=?";
                pst = con.prepareStatement(sql);
                pst.setInt(1, Integer.parseInt(retire.getNotId()));
                pst.executeUpdate();

                DataBaseFunctions.closeSqlObjects(pst);
            }

            if (retire.getRetid() != null && !retire.getRetid().equals("")) {
                sql = "delete from emp_ret_res where not_id=?";
                pst = con.prepareStatement(sql);
                pst.setInt(1, Integer.parseInt(retire.getNotId()));
                pst.executeUpdate();

                DataBaseFunctions.closeSqlObjects(pst);
            }

            if (retire.getRelieveId() != null && !retire.getRelieveId().equals("")) {
                sql = "delete from emp_relieve where relieve_id=?";
                pst = con.prepareStatement(sql);
                pst.setInt(1, Integer.parseInt(retire.getRelieveId()));
                pst.executeUpdate();

                DataBaseFunctions.closeSqlObjects(pst);
            }

            pst = con.prepareStatement("UPDATE EMP_MAST SET DEP_CODE='02',CUR_SPC=?,IF_RETIRED='N' WHERE EMP_ID=?");
            pst.setString(1, retiredspc);
            pst.setString(2, retire.getRetiredEmpid());
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    @Override
    public void saveSuperannuation(Retirement retire) {
        PreparedStatement pst = null;
        Connection con = null;
        ResultSet rs = null;
        int notId = 0;
        String sbLang = null;
        try {
            con = this.dataSource.getConnection();
            pst = con.prepareStatement("INSERT INTO EMP_NOTIFICATION(NOT_TYPE,EMP_ID,DOE,TOE,ORDNO,ORDDT,DEPT_CODE,OFF_CODE,AUTH,ENT_DEPT,ENT_OFF,ENT_AUTH,IF_VISIBLE,SB_DESCRIPTION,RET_PAY) "
                    + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, "SUPERANNUATION");
            pst.setString(2, retire.getRetiredEmpid());
            pst.setDate(3, new java.sql.Date(new Date().getTime()));
            pst.setString(4, retire.getToe());
            //pst.setString(5, retire.getIfAssumed());
            pst.setString(5, retire.getOrdno());
            pst.setDate(6, new java.sql.Date(CommonFunctions.getDateFromString(retire.getOrdDate(), "dd-MMM-yyyy").getTime()));
            pst.setString(7, retire.getRetiredfromdeptCode());
            pst.setString(8, retire.getRetiredfromofficeCode());
            pst.setString(9, retire.getRetiredfromspc());

            pst.setString(10, retire.getAuthDeptCode());
            pst.setString(11, retire.getAuthOfficeCode());
            pst.setString(12, retire.getAuthSpc());

            //pst.setString(11, retire.getNote());
            pst.setString(13, "Y");
            pst.setString(14, sbLang);
            pst.setInt(15, retire.getRetPay());
            pst.executeUpdate();
            rs = pst.getGeneratedKeys();
            rs.next();
            notId = rs.getInt("NOT_ID");
            sbLang = sbDAO.getSuperanuationFormDetails(notId);
            pst = con.prepareStatement("Update emp_notification set sb_description=? where not_id=?");
            pst.setString(1, sbLang);
            pst.setInt(2, notId);
            pst.executeUpdate();
            //System.out.println("sblang:" + sbLang);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public Retirement getEmployeeSuperannuationData(String empId) {
        Retirement retirement = new Retirement();
        PreparedStatement pst = null;
        Connection con = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("SELECT not_id,emp_id,ordno,orddt,doe,toe,emp_notification.ent_dept,department_name,emp_notification.ent_off,off_en,ent_auth,spn,ret_pay FROM emp_notification\n"
                    + "inner join g_spc on g_spc.spc=emp_notification.ent_auth\n"
                    + "inner join g_office on g_office.off_code=emp_notification.ent_off\n"
                    + "inner join g_department on g_department.department_code=emp_notification.ent_dept\n"
                    + "WHERE EMP_ID=? AND NOT_TYPE='SUPERANNUATION' ");
            pst.setString(1, empId);
            rs = pst.executeQuery();
            if (rs.next()) {
                retirement.setNotId(rs.getString("not_id"));
                retirement.setOrdno(rs.getString("ordno"));
                retirement.setOrdDate(rs.getString("orddt"));
                retirement.setAuthDeptCode(rs.getString("ent_dept"));
                retirement.setAuthOfficeCode(rs.getString("ent_off"));
                retirement.setAuthSpc(rs.getString("ent_auth"));
                retirement.setRetPay(rs.getInt("ret_pay"));

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return retirement;
    }
}
