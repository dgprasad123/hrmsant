package hrms.dao.resignation;

import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.model.resignation.Resignation;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;

public class ResignationDAOImpl implements ResignationDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Resignation getEmployeeResignationData(String empId) {
        Resignation resignation = new Resignation();
        PreparedStatement pst = null;
        Connection con = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();
            resignation.setNotType("GOO");
            resignation.setRetauthtyp("GOO");
            resignation.setRetired("Y");
            pst = con.prepareStatement("SELECT ER.dos,ER.doe, ER.RET_ID,ER.RE_DEPT, ER.RE_OFF, ER.RE_SPC, ER.not_id,ORDNO,ORDDT,DEPT_CODE"
                    + ",EN.NOTE,OFF_CODE,EN.AUTH FROM emp_ret_res ER INNER JOIN emp_notification EN ON ER.not_id = EN.not_id"
                    + " WHERE ER.EMP_ID=? AND EN.not_type = 'RESIGNATION'");
            pst.setString(1, empId);
            rs = pst.executeQuery();
            if (rs.next()) {
                resignation.setCurspc(rs.getString("RE_SPC"));
                resignation.setRetid(rs.getString("RET_ID"));
                resignation.setRetiredfromdeptCode(rs.getString("RE_DEPT"));
                resignation.setRetiredfromofficeCode(rs.getString("RE_OFF"));
                resignation.setRetiredfromspc(rs.getString("RE_SPC"));
                resignation.setAuthDeptCode(rs.getString("DEPT_CODE"));
                resignation.setAuthOfficeCode(rs.getString("OFF_CODE"));
                resignation.setAuthSpc(rs.getString("AUTH"));
                resignation.setOrdDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("ORDDT")));
                resignation.setOrdno(rs.getString("ORDNO"));
                resignation.setDuedate(CommonFunctions.getFormattedOutputDate1(rs.getDate("dos")));
                resignation.setNote(rs.getString("NOTE"));
            }
            if (resignation.getRetid() == null) {
                pst = con.prepareStatement("SELECT CUR_SPC,dept_code,off_code FROM EMP_MAST INNER JOIN G_SPC ON EMP_MAST.CUR_SPC = G_SPC.SPC WHERE EMP_ID=?");
                pst.setString(1, empId);
                rs = pst.executeQuery();
                if (rs.next()) {
                    resignation.setRetiredfromdeptCode(rs.getString("dept_code"));
                    resignation.setRetiredfromofficeCode(rs.getString("off_code"));
                    resignation.setRetiredfromspc(rs.getString("CUR_SPC"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst, con);
        }
        return resignation;
    }

    @Override
    public void saveEmployeeResignation(Resignation resig) {
        PreparedStatement pst = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();

            pst = con.prepareStatement("INSERT INTO EMP_NOTIFICATION(NOT_TYPE,EMP_ID,DOE,TOE,IF_ASSUMED,ORDNO,ORDDT,DEPT_CODE,OFF_CODE,AUTH,NOTE,IF_VISIBLE,ENT_DEPT,ENT_OFF,ENT_AUTH,ACS,ASCS) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);

            pst.setString(1, "RESIGNATION");
            pst.setString(2, resig.getRetiredEmpid());
            pst.setDate(3, new java.sql.Date(new Date().getTime()));
            pst.setString(4, resig.getToe());
            pst.setString(5, resig.getIfAssumed());
            pst.setString(6, resig.getOrdno().toUpperCase());
            System.out.println("orderno;" + resig.getOrdno());
            pst.setDate(7, new java.sql.Date(CommonFunctions.getDateFromString(resig.getOrdDate(), "dd-MMM-yyyy").getTime()));
            pst.setString(8, resig.getAuthDeptCode());
            pst.setString(9, resig.getAuthOfficeCode());
            pst.setString(10, resig.getAuthSpc());
            pst.setString(11, resig.getNote());
            pst.setString(12, resig.getIfVisible());
            pst.setString(13, resig.getEntDeptCode());
            pst.setString(14, resig.getEntOfficeCode());
            pst.setString(15, resig.getEntAuthCode());
            pst.setString(16, resig.getCadreStatus());
            pst.setString(17, resig.getSubStatus());
            pst.executeUpdate();

            ResultSet rs = pst.getGeneratedKeys();
            rs.next();
            int notId = rs.getInt("NOT_ID");
            System.out.println("new noteid#" + notId);

            pst = con.prepareStatement("INSERT INTO emp_ret_res (emp_id, doe, TOE, IF_ASSUMED, ret_type, dos, retired, ORD_NO, ORD_DATE, AUTH_DEPT, AUTH_OFF, AUTH, RE_DEPT, RE_OFF, RE_SPC, NOTE, ent_dept, ent_off, ent_auth, not_id) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            pst.setString(1, resig.getRetiredEmpid());
            pst.setDate(2, new java.sql.Date(new Date().getTime()));
            pst.setString(3, resig.getToe());
            pst.setString(4, resig.getIfAssumed());
            pst.setString(5, resig.getRegtype());
            if (resig.getDuedate() != null && !resig.getDuedate().equals("")) {
                pst.setTimestamp(6, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(resig.getDuedate()).getTime()));
            } else {
                pst.setTimestamp(6, null);
            }
            pst.setString(7, resig.getRetired());
            pst.setString(8, resig.getOrdno().toUpperCase());
            pst.setDate(9, new java.sql.Date(CommonFunctions.getDateFromString(resig.getOrdDate(), "dd-MMM-yyyy").getTime()));
            pst.setString(10, resig.getAuthDeptCode());
            pst.setString(11, resig.getAuthOfficeCode());
            pst.setString(12, resig.getAuthSpc());

            pst.setString(13, resig.getRetiredfromdeptCode());
            pst.setString(14, resig.getRetiredfromofficeCode());
            pst.setString(15, resig.getRetiredfromspc());
            pst.setString(16, resig.getNote());

            pst.setString(17, resig.getEntDeptCode());
            pst.setString(18, resig.getEntOfficeCode());
            pst.setString(19, resig.getEntAuthCode());
            pst.setInt(20, notId);
            pst.executeUpdate();

            String rid = CommonFunctions.getMaxCode("emp_relieve", "RELIEVE_ID", con);
            pst = con.prepareStatement("INSERT INTO emp_relieve(NOT_TYPE, NOT_ID, EMP_ID, DOE, MEMO_NO, MEMO_DATE, RLV_DATE, RLV_TIME, SPC, IF_VISIBLE,ENT_DEPT,ENT_OFF,ENT_AUTH) values (?,?,?,?,?,?,?,?,?,?,?,?,?)");
            pst.setString(1, "RESIGNATION");
            pst.setInt(2, notId);
            pst.setString(3, resig.getRetiredEmpid());
            pst.setDate(4, new java.sql.Date(new Date().getTime()));
            pst.setString(5, resig.getOrdno().toUpperCase());
            pst.setDate(6, new java.sql.Date(CommonFunctions.getDateFromString(resig.getOrdDate(), "dd-MMM-yyyy").getTime()));
            pst.setDate(7, new java.sql.Date(CommonFunctions.getDateFromString(resig.getDuedate(), "dd-MMM-yyyy").getTime()));
            pst.setString(8, "AN");
            pst.setString(9, resig.getRetiredfromspc());
            pst.setString(10, "Y");
            pst.setString(11, resig.getEntDeptCode());
            pst.setString(12, resig.getEntOfficeCode());
            pst.setString(13, resig.getEntAuthCode());
            pst.executeUpdate();

            pst = con.prepareStatement("UPDATE EMP_MAST SET dep_code='08', CUR_SPC=null WHERE EMP_ID=?");
            pst.setString(1, resig.getRetiredEmpid());
            pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void deleteEmployeeResignation(Resignation resig) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String resignatedSPC = "";
        int notid = 0;

        int retval = 0;
        try {
            con = this.dataSource.getConnection();

            String sql = "select * from emp_ret_res where emp_id=? and ret_type='RG' and retired='Y'";
            pst = con.prepareStatement(sql);
            pst.setString(1, resig.getRetiredEmpid());
            rs = pst.executeQuery();
            if (rs.next()) {
                resignatedSPC = rs.getString("RE_SPC");
                notid = rs.getInt("not_id");
            }

            DataBaseFunctions.closeSqlObjects(rs, pst);

            if (!resignatedSPC.equals("") && notid > 0) {
                sql = "delete from emp_notification where not_id=? and not_type='RESIGNATION'";
                pst = con.prepareStatement(sql);
                pst.setInt(1, notid);
                retval = pst.executeUpdate();

                DataBaseFunctions.closeSqlObjects(rs, pst);

                if (retval > 0) {
                    sql = "delete from emp_ret_res where emp_id=? and ret_type='RG'";
                    pst = con.prepareStatement(sql);
                    pst.setString(1, resig.getRetiredEmpid());
                    retval = pst.executeUpdate();

                    DataBaseFunctions.closeSqlObjects(rs, pst);

                    sql = "delete from emp_relieve where emp_id=? and not_id=?";
                    pst = con.prepareStatement(sql);
                    pst.setString(1, resig.getRetiredEmpid());
                    pst.setInt(2, notid);
                    retval = pst.executeUpdate();

                    DataBaseFunctions.closeSqlObjects(rs, pst);

                    sql = "update emp_mast set cur_spc=?,dep_code='02' where emp_id=?";
                    pst = con.prepareStatement(sql);
                    pst.setString(1, resignatedSPC);
                    pst.setString(2, resig.getRetiredEmpid());
                    retval = pst.executeUpdate();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    @Override
    public List getResginationList(String empId) {
        PreparedStatement pst = null;
        Connection con = null;
        ResultSet rs = null;
        Resignation resg = null;
        List resignationList = new ArrayList();

        try {
            con = this.dataSource.getConnection();
            pst = con.prepareStatement("select empnotification.not_id,empnotification.not_type,empnotification.emp_Id,emp_ret_res.doe,ordno,orddt from\n"
                    + "(select * from emp_notification where not_type='RESIGNATION' and emp_id=?)empnotification\n"
                    + "inner join emp_ret_res on empnotification.not_id=emp_ret_res.not_id\n"
                    + "inner join emp_relieve on empnotification.not_id=emp_relieve.not_id");
            pst.setString(1, empId);
            rs = pst.executeQuery();
            while (rs.next()) {
                resg = new Resignation();
                resg.setNotId(rs.getString("not_id"));
                resg.setDoe(CommonFunctions.getFormattedOutputDate1(rs.getDate("doe")));
                resg.setOrdno(rs.getString("ordno"));
                resg.setOrdDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("orddt")));
                resignationList.add(resg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return resignationList;
    }

    @Override
    public Resignation editResignationData(String notId) {
        PreparedStatement pst = null;
        Connection con = null;
        ResultSet rs = null;
        Resignation resgn = null;
        try {
            con = this.dataSource.getConnection();
            pst = con.prepareStatement("select ret_id,gpf_no,g_spc.spn postname,array_to_string(array[initials,f_name,m_name,l_name],' ')empname,empnotification.not_id,\n"
                    + "empnotification.not_type,empnotification.emp_Id,emp_ret_res.doe,\n"
                    + "ordno,orddt,emp_ret_res.dos dateofresig,empnotification.note,auth_dept,auth_off,empnotification.auth,\n"
                    + "g_department.department_name authDept,g_office.off_en authOfc,gauthspc.spn authPost,"
                    + "gdept.department_name resgDept,gofc.off_en resgOfc,"
                    + "gresgspc.spn resgPost,re_dept,re_off,re_spc from\n"
                    + "(select * from emp_notification where not_type='RESIGNATION' and not_id=?)empnotification\n"
                    + "inner join emp_ret_res on empnotification.not_id=emp_ret_res.not_id\n"
                    + "inner join emp_relieve on empnotification.not_id=emp_relieve.not_id\n"
                    + "inner join emp_mast on empnotification.emp_id=emp_mast.emp_id\n"
                    + "left outer join g_spc on emp_mast.cur_spc=g_spc.spc\n"
                    + "left outer join g_department on auth_dept=g_department.department_code\n"
                    + "left outer join g_office on auth_off=g_office.off_code\n"
                    + "left outer join g_spc gauthspc on empnotification.auth=gauthspc.spc\n"
                    + "left outer join g_department gdept on re_dept=gdept.department_code\n"
                    + "left outer join g_office gofc on re_off=gofc.off_code\n"
                    + "left outer join g_spc gresgspc on re_spc=gresgspc.spc");
            pst.setInt(1, Integer.parseInt(notId));
            rs = pst.executeQuery();
            if (rs.next()) {
                resgn = new Resignation();
                resgn.setRetid(notId);
                resgn.setHidnotid(rs.getInt("not_id"));
                resgn.setHidempid(rs.getString("emp_Id"));
                resgn.setDoe(rs.getString("doe"));
                resgn.setOrdno(rs.getString("ordno"));
                resgn.setOrdDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("orddt")));
                resgn.setDuedate(CommonFunctions.getFormattedOutputDate1(rs.getDate("dateofresig")));
                resgn.setNote(rs.getString("note"));
                //resgn.setAuth_dept_code(rs.getString("auth_dept"));
                //resgn.setAuth_off_code(rs.getString("auth_off"));
                resgn.setAuthDeptCode(rs.getString("auth_dept"));
                resgn.setAuthOfficeCode(rs.getString("auth_off"));
                resgn.setAuthSpc(rs.getString("auth"));
                //resgn.setRetiredfromdeptCode(rs.getString("resgDept"));
                //resgn.setRetiredfromofficeCode(rs.getString("resgOfc"));
                //resgn.setRetiredfromspc(rs.getString("resgPost"));
                resgn.setRetiredfromdeptCode(rs.getString("re_dept"));
                resgn.setRetiredfromofficeCode(rs.getString("re_off"));
                resgn.setRetiredfromspc(rs.getString("re_spc"));
                resgn.setRet_dept_code(rs.getString("re_dept"));
                resgn.setRet_off_code(rs.getString("re_off"));
                resgn.setRetdDeptName(rs.getString("resgDept"));
                resgn.setRetdPostName(rs.getString("resgPost"));
                resgn.setRetdoffName(rs.getString("resgOfc"));

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return resgn;

    }

    @Override
    public void updateEmployeeResignation(Resignation resig) {
        PreparedStatement pst1 = null;
        PreparedStatement pst2 = null;
        Connection con = null;
        ResultSet rs = null;
        try {
            con = this.dataSource.getConnection();

            pst1 = con.prepareStatement("update emp_ret_res set DOE=?,TOE=?,IF_ASSUMED=?,dos=?,retired=?, \n"
                    + "ORD_NO=?, ORD_DATE=?, AUTH_DEPT=?, AUTH_OFF=?,AUTH=?,RE_DEPT=?, \n"
                    + "RE_OFF=?,RE_SPC=?,NOTE=?,ent_dept=?,ent_off=?,ent_auth=? where not_id=? and emp_id=?");
            pst1.setDate(1, new java.sql.Date(new Date().getTime()));
            pst1.setString(2, resig.getToe());
            pst1.setString(3, resig.getIfAssumed());
            if (resig.getDuedate() != null && !resig.getDuedate().equals("")) {
                pst1.setTimestamp(4, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(resig.getDuedate()).getTime()));
            } else {
                pst1.setTimestamp(4, null);
            }
            pst1.setString(5, resig.getRetired());
            pst1.setString(6, resig.getOrdno());
            pst1.setDate(7, new java.sql.Date(CommonFunctions.getDateFromString(resig.getOrdDate(), "dd-MMM-yyyy").getTime()));
            pst1.setString(8, resig.getAuthDeptCode());
            pst1.setString(9, resig.getAuthOfficeCode());
            pst1.setString(10, resig.getAuthSpc());
            pst1.setString(11, resig.getRetiredfromdeptCode());
            pst1.setString(12, resig.getRetiredfromofficeCode());
            pst1.setString(13, resig.getRetiredfromspc());
            pst1.setString(14, resig.getNote());
            pst1.setString(15, resig.getEntDeptCode());
            pst1.setString(16, resig.getEntOfficeCode());
            pst1.setString(17, resig.getEntAuthCode());
            pst1.setInt(18, resig.getHidnotid());
            pst1.setString(19, resig.getHidempid());
            pst1.executeUpdate();

            pst2 = con.prepareStatement("update emp_relieve set DOE=?, MEMO_NO=?, MEMO_DATE=?, RLV_DATE=?, RLV_TIME=?, SPC=?, IF_VISIBLE=?,"
                    + "ENT_DEPT=?,ENT_OFF=?,ENT_AUTH=?,NOTE=?\n"
                    + "where not_id=? and emp_id=?");
            pst2.setDate(1, new java.sql.Date(new Date().getTime()));
            pst2.setString(2, resig.getOrdno());
            pst2.setDate(3, new java.sql.Date(CommonFunctions.getDateFromString(resig.getOrdDate(), "dd-MMM-yyyy").getTime()));
            pst2.setDate(4, new java.sql.Date(CommonFunctions.getDateFromString(resig.getDuedate(), "dd-MMM-yyyy").getTime()));
            pst2.setString(5, "AN");
            pst2.setString(6, resig.getRetiredfromspc());
            pst2.setString(7, "Y");
            pst2.setString(8, resig.getEntDeptCode());
            pst2.setString(9, resig.getEntOfficeCode());
            pst2.setString(10, resig.getEntAuthCode());
            pst2.setString(11, resig.getNote());
            pst2.setInt(12, resig.getHidnotid());
            pst2.setString(13, resig.getHidempid());
            pst2.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst1);
            DataBaseFunctions.closeSqlObjects(pst2);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void deleteResignation(Resignation resig) {

        Connection con = null;

        PreparedStatement pst = null;

        try {
            con = this.dataSource.getConnection();
            System.out.println("resig.getRetiredEmpid() is: " + resig.getRetiredEmpid());
            System.out.println("resig.getHidnotid() is: " + resig.getHidnotid());
            pst = con.prepareStatement("DELETE FROM EMP_RET_RES WHERE EMP_ID=? AND NOT_ID=?");
            pst.setString(1, resig.getRetiredEmpid());
            pst.setInt(2, resig.getHidnotid());
            pst.executeUpdate();

            pst = con.prepareStatement("DELETE FROM EMP_RELIEVE WHERE EMP_ID=? AND NOT_ID=?");
            pst.setString(1, resig.getRetiredEmpid());
            pst.setInt(2, resig.getHidnotid());
            pst.executeUpdate();

            pst = con.prepareStatement("DELETE FROM EMP_NOTIFICATION WHERE EMP_ID=? AND NOT_ID=?");
            pst.setString(1, resig.getRetiredEmpid());
            pst.setInt(2, resig.getHidnotid());
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public Resignation getEmployeeResignationDataForNewEntry(String empId) {

        Resignation resignation = new Resignation();
        PreparedStatement pst = null;
        Connection con = null;
        ResultSet rs = null;
        try {
            con = this.dataSource.getConnection();
            
            resignation.setNotType("GOO");
            resignation.setRetauthtyp("GOO");
            resignation.setRetired("Y");

            pst = con.prepareStatement("SELECT CUR_SPC,dept_code,off_code FROM EMP_MAST INNER JOIN G_SPC ON EMP_MAST.CUR_SPC = G_SPC.SPC WHERE EMP_ID=?");
            pst.setString(1, empId);
            rs = pst.executeQuery();
            if (rs.next()) {
                resignation.setRetiredfromdeptCode(rs.getString("dept_code"));
                resignation.setRetiredfromofficeCode(rs.getString("off_code"));
                resignation.setRetiredfromspc(rs.getString("CUR_SPC"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst, con);
        }
        return resignation;
    }
}
