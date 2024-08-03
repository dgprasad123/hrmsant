/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.report.gispassbook;

import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.model.report.gispassbook.gisPassbook;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 *
 * @author lenovo
 */
public class GisPassbookDAOImpl implements GisPassbookDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List getGisAlertList(String offCode, int Year) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List gisList = new ArrayList();
        gisPassbook gbean = new gisPassbook();
        try {
            conn = dataSource.getConnection();
            pstmt = conn.prepareStatement("select gisno, emp_id, gpf_no, cur_off_code, gistype, joindate_of_goo, ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMPNAME from emp_mast where gisno is null "
                    + " and extract (year from joindate_of_goo) =? and cur_off_code=? and (is_regular='Y' or  is_regular='G' or is_regular='W') ");
            pstmt.setInt(1, Year);
            pstmt.setString(2, offCode);

            rs = pstmt.executeQuery();
            while (rs.next()) {
                gbean = new gisPassbook();
                gbean.setEmpId(rs.getString("emp_id"));
                gbean.setGfpNo(rs.getString("gpf_no"));
                gbean.setEmpName(rs.getString("EMPNAME"));
                gbean.setJoinDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("joindate_of_goo")));
                gisList.add(gbean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs,pstmt);
            DataBaseFunctions.closeSqlObjects(conn);
        }
        return gisList;
    }

    @Override
    public ArrayList getgisPassbookList(String empId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList gisList = new ArrayList();
        gisPassbook gbean = null;
        try {
            conn = dataSource.getConnection();
            String gisQuery = "SELECT ddate,vchno,instru,gamt,auth,post,tr_name FROM emp_gis "
                    + " left outer join g_treasury on emp_gis.tr_code=g_treasury.tr_code"
                    + " left outer join g_spc on emp_gis.auth=g_spc.spc"
                    + " left outer join g_post on g_spc.gpc=g_post.post_code"
                    + " WHERE  EMP_ID=?";
            pstmt = conn.prepareStatement(gisQuery);
            pstmt.setString(1, empId);
            rs = pstmt.executeQuery();
            String status = "";
            int i = 0;
            while (rs.next()) {
                i++;
                gbean = new gisPassbook();
                if (rs.getTimestamp("ddate") != null) {
                    gbean.setDod(CommonFunctions.getFormattedOutputDate1(rs.getDate("ddate")));
                } else {
                    gbean.setDod(null);
                }
                gbean.setVchno(rs.getString("vchno"));
                gbean.setInstru(rs.getString("instru"));
                gbean.setAmoutDeposit(rs.getInt("gamt"));
                gbean.setDepositBy(rs.getString("post"));
                gbean.setTrName(rs.getString("tr_name"));
                gbean.setSlNo(i);
                gisList.add(gbean);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(conn);
        }
        return gisList;
    }

    @Override
    public ArrayList EmployeePostList(String offCode, int month, int Year) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList empList = new ArrayList();
        gisPassbook gbean = null;
        try {
            conn = dataSource.getConnection();
            String empQuery = "SELECT emp_id,gpf_no,TRIM(CONCAT(a.f_name, ' ', a.m_name, ' ', a.l_name)) as name,cur_basic_salary,deploy_type,post,c.cur_basic as preBasic,c.cur_desg as prevdes FROM emp_mast a"
                    + " LEFT OUTER JOIN g_deploy_type  b ON  a.dep_code=b.deploy_code left outer join g_spc ON   "
                    + " a.cur_spc=g_spc.spc left outer join g_post on g_spc.gpc=g_post.post_code left outer join aq_mast c"
                    + "  ON a.emp_id=c.emp_code AND c.aq_month=? and c.aq_year=? AND c.off_code=? "
                    + " WHERE   (a.next_office_code=? OR a.cur_off_code=?)";

            pstmt = conn.prepareStatement(empQuery);
            pstmt.setInt(1, month);
            pstmt.setInt(2, Year);
            pstmt.setString(3, offCode);
            pstmt.setString(4, offCode);
            pstmt.setString(5, offCode);
            rs = pstmt.executeQuery();
            String status = "";
            int i = 0;
            while (rs.next()) {
                i++;
                gbean = new gisPassbook();

                gbean.setEmpId(rs.getString("emp_id"));
                gbean.setEmpName(rs.getString("name"));
                gbean.setCurrentSal(rs.getInt("cur_basic_salary"));
                gbean.setPrevSal(rs.getInt("preBasic"));
                gbean.setCurPost(rs.getString("post"));
                gbean.setPrevPost(rs.getString("prevdes"));
                gbean.setGfpNo(rs.getString("gpf_no"));
                gbean.setCurrentStatus(rs.getString("deploy_type"));
                gbean.setSlNo(i);

                empList.add(gbean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(conn);
        }
        return empList;
    }

    @Override
    public List getgisEmployeeDetaills(String empId) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        gisPassbook gbean = null;
        ArrayList empDetails = new ArrayList();

        try {
            con = this.dataSource.getConnection();
            String sql1 = "select gpf_no,gisno,array_to_string(array[initials,f_name,m_name,l_name],' ')empnm from emp_mast where emp_id=?";
            ps = con.prepareStatement(sql1);
            ps.setString(1, empId);
            rs = ps.executeQuery();
            if (rs.next()) {
                gbean = new gisPassbook();
                gbean.setEmpId(empId);
                gbean.setGfpNo(rs.getString("gpf_no"));
                gbean.setGisNo(rs.getString("gisno"));
                gbean.setEmpName(rs.getString("empnm"));
                empDetails.add(gbean);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return empDetails;
    }

}
