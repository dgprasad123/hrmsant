/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.master;

import hrms.SelectOption;
import hrms.common.DataBaseFunctions;
import hrms.model.master.SubstantivePost;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Durga
 */
public class SubStantivePostDAOImpl implements SubStantivePostDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    @Resource(name = "repodataSource")
    protected DataSource repodataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setRepodataSource(DataSource repodataSource) {
        this.repodataSource = repodataSource;
    }

    @Override
    public List getEmployeeWithSPCList(String offcode) {
        Connection con = null;

        PreparedStatement ps = null;
        ResultSet rs = null;

        List li = new ArrayList();
        SubstantivePost spc = null;

        try {
            con = this.dataSource.getConnection();

            ps = con.prepareStatement("select spc,post,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMPNAME,emp_id,spn,'' IF_AD_CHARGE from ("
                    + " select spc,gpc,spn from g_spc where off_code=? AND (IFUCLEAN!='Y' OR IFUCLEAN IS NULL)) g_spc "
                    + " inner join g_post on g_spc.GPC = g_post.POST_CODE "
                    + " left outer join EMP_MAST ON G_SPC.SPC=EMP_MAST.CUR_SPC"
                    + " UNION"
                    + " select EMP_JOIN.spc,post,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMPNAME,EMP_JOIN.emp_id ,spn,IF_AD_CHARGE from ("
                    + " select spc,gpc,spn from g_spc where off_code=? AND (IFUCLEAN!='Y' OR IFUCLEAN IS NULL)) g_spc"
                    + " inner join g_post on g_spc.GPC = g_post.POST_CODE"
                    + " left outer join EMP_JOIN ON G_SPC.SPC=EMP_JOIN.SPC"
                    + " left outer join EMP_MAST ON EMP_JOIN.EMP_ID=EMP_MAST.EMP_ID WHERE IF_AD_CHARGE='Y' ORDER BY POST");

            ps.setString(1, offcode);
            ps.setString(2, offcode);
            rs = ps.executeQuery();
            while (rs.next()) {
                spc = new SubstantivePost();
                if (rs.getString("EMPNAME") != null && !rs.getString("EMPNAME").equals("")) {
                    spc.setPostname(rs.getString("post"));

                    if (rs.getString("IF_AD_CHARGE").equals("Y")) {
                        spc.setEmpname(rs.getString("EMPNAME").concat(" (Addl. Charge)"));
                        spc.setIfadcharge("Y");
                        spc.setSpc(rs.getString("spc").concat("-").concat(rs.getString("emp_id")));
                    } else {
                        spc.setEmpname(rs.getString("EMPNAME"));
                        spc.setIfadcharge("N");
                        spc.setSpc(rs.getString("spc"));
                    }

                    spc.setSpn(rs.getString("spn"));
                    spc.setEmpid(rs.getString("emp_id"));
                    String spcHrmsId = rs.getString("spc") + "|" + rs.getString("emp_id") + "|" + rs.getString("post") + "|" + rs.getString("EMPNAME");
                    spc.setSpcHrmsId(spcHrmsId);
                    li.add(spc);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    @Override
    public List getAllSPCWithEmployee(String offcode) {
        Connection con = null;

        PreparedStatement ps = null;
        ResultSet rs = null;

        List li = new ArrayList();

        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("select spc,post,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMPNAME,emp_id,spn from ("
                    + "select spc,gpc,spn from g_spc where off_code=? AND (IFUCLEAN!='Y' OR IFUCLEAN IS NULL)) g_spc "
                    + "inner join g_post on g_spc.GPC = g_post.POST_CODE "
                    + "left outer join EMP_MAST ON G_SPC.SPC=EMP_MAST.CUR_SPC ORDER BY POST");

            ps.setString(1, offcode);
            rs = ps.executeQuery();
            while (rs.next()) {

                SubstantivePost spc = new SubstantivePost();
                spc.setPostname(rs.getString("post"));
                spc.setEmpname(rs.getString("EMPNAME"));
                spc.setSpc(rs.getString("spc"));
                spc.setSpn(rs.getString("spn"));
                spc.setEmpid(rs.getString("emp_id"));
                String spcHrmsId = rs.getString("spc") + "|" + rs.getString("emp_id") + "|" + rs.getString("post") + "|" + rs.getString("EMPNAME");
                spc.setSpcHrmsId(spcHrmsId);
                li.add(spc);

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    @Override
    public List getSPCList(String offcode) {
        Connection con = null;

        PreparedStatement ps = null;
        ResultSet rs = null;

        List li = new ArrayList();
        SelectOption so = null;
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("select spc,post,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMPNAME,emp_id from ("
                    + "select spc,gpc from g_spc where off_code='" + offcode + "' AND (IFUCLEAN!='Y' OR IFUCLEAN IS NULL)) g_spc "
                    + "inner join g_post on g_spc.GPC = g_post.POST_CODE "
                    + "left outer join EMP_MAST ON G_SPC.SPC=EMP_MAST.CUR_SPC ORDER BY POST");
            rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getString("EMPNAME") != null && !rs.getString("EMPNAME").equals("")) {
                    so = new SelectOption();
                    so.setLabel(rs.getString("post"));
                    so.setDesc(rs.getString("EMPNAME"));
                    String SpcHrmsId = rs.getString("spc") + "|" + rs.getString("emp_id");
                    so.setValue(SpcHrmsId);
                    li.add(so);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    @Override
    public List getGenericPostList(String offcode) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        List li = new ArrayList();
        SelectOption so = null;
        try {
            con = this.dataSource.getConnection();
            pst = con.prepareStatement("SELECT POST_CODE,POST FROM G_POST INNER JOIN (SELECT DISTINCT GPC FROM G_SPC WHERE OFF_CODE=? AND SPN is not null and is_sanctioned='Y' AND (IFUCLEAN !='Y' OR IFUCLEAN IS NULL)) AS G_SPC ON "
                    + "G_SPC.GPC=G_POST.POST_CODE ORDER BY POST");
            pst.setString(1, offcode);
            rs = pst.executeQuery();
            while (rs.next()) {
                so = new SelectOption();
                so.setValue(rs.getString("POST_CODE"));
                so.setLabel(rs.getString("POST"));
                li.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    @Override
    public List getGPCWiseSPCList(String empid, String offcode, String gpc) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        PreparedStatement pst1 = null;
        ResultSet rs1 = null;

        List li = new ArrayList();

        SelectOption so = null;

        String empspc = "";
        String empname = "";
        try {
            con = this.dataSource.getConnection();

            pst = con.prepareStatement("SELECT ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') FULL_NAME,CUR_SPC FROM EMP_MAST WHERE EMP_ID=?");
            pst.setString(1, empid);
            rs = pst.executeQuery();
            if (rs.next()) {
                empspc = rs.getString("CUR_SPC");
                empname = rs.getString("FULL_NAME");
            }

            pst1 = con.prepareStatement("SELECT CUR_SPC FROM EMP_MAST WHERE CUR_SPC=?");
            pst = con.prepareStatement("SELECT SPC,SPN FROM G_SPC WHERE OFF_CODE=? AND GPC=? and spn is not null AND (IFUCLEAN!='Y' OR IFUCLEAN IS NULL) ORDER BY SPN");
            pst.setString(1, offcode);
            pst.setString(2, gpc);
            rs = pst.executeQuery();
            while (rs.next()) {

                pst1.setString(1, rs.getString("SPC"));
                rs1 = pst1.executeQuery();
                if (!rs1.next()) {
                    //if (rs1.getString("CUR_SPC") == null || rs1.getString("CUR_SPC").equals("")) {
                    so = new SelectOption();
                    so.setValue(rs.getString("SPC"));
                    so.setLabel(rs.getString("SPN"));
                    li.add(so);
                    //}
                }
                if (empspc != null && empspc.equals(rs.getString("SPC"))) {
                    so = new SelectOption();
                    so.setValue(rs.getString("SPC"));
                    so.setLabel(rs.getString("SPN") + " " + empname);
                    li.add(so);
                }
            }
            //li.add(so);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    @Override
    public ArrayList getCadreWiseOfficeWiseSPC(String cadreCode, String offCode) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        ArrayList spcList = new ArrayList();
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("select SPC,SPN from g_cadre2post INNER JOIN G_SPC ON g_cadre2post.POST_CODE = G_SPC.gpc where g_cadre2post.cadre_code=? "
                    + "AND G_SPC.OFF_CODE=? AND (IFUCLEAN!='Y' OR IFUCLEAN IS NULL)");
            pstmt.setString(1, cadreCode);
            pstmt.setString(2, offCode);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                SubstantivePost spc = new SubstantivePost();
                spc.setSpn(rs.getString("SPN"));
                spc.setSpc(rs.getString("SPC"));
                spcList.add(spc);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return spcList;
    }

    @Override
    public List getPostListPaging(String offcode, String postToSearch, int page, int rows) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List postlist = new ArrayList();
        SubstantivePost sp = null;

        int minlimit = rows * (page - 1);
        int maxlimit = rows;
        try {
            con = dataSource.getConnection();

            String sql = "";
            if (postToSearch != null && !postToSearch.equals("")) {
                sql = "SELECT SPC,SPN FROM G_SPC WHERE OFF_CODE=? AND SPN LIKE ? ORDER BY SPN LIMIT ? OFFSET ?";
                pst = con.prepareStatement(sql);
                pst.setString(1, offcode);
                pst.setString(2, "%" + postToSearch.toUpperCase() + "%");
                pst.setInt(3, maxlimit);
                pst.setInt(4, minlimit);
            } else {
                //sql = "SELECT SPC,SPN FROM G_SPC WHERE OFF_CODE=? ORDER BY SPN LIMIT ? OFFSET ?";
                sql = "SELECT POST,POST_CODE FROM G_POST"
                        + " INNER JOIN G_OFFICE ON G_POST.DEPARTMENT_CODE=G_OFFICE.DEPARTMENT_CODE WHERE OFF_CODE=?"
                        + " ORDER BY POST ASC LIMIT ? OFFSET ?";
                pst = con.prepareStatement(sql);
                pst.setString(1, offcode);
                pst.setInt(2, maxlimit);
                pst.setInt(3, minlimit);
            }

            rs = pst.executeQuery();
            while (rs.next()) {
                sp = new SubstantivePost();
                sp.setSpc(rs.getString("POST_CODE"));
                sp.setSpn(rs.getString("POST"));
                postlist.add(sp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return postlist;
    }

    @Override
    public int getPostListCountPaging(String offcode, String postToSearch) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        int cnt = 0;

        try {
            con = this.dataSource.getConnection();

            String sql = "";

            if (postToSearch != null && !postToSearch.equals("")) {
                sql = "SELECT COUNT(*) CNT FROM G_SPC WHERE OFF_CODE=? AND SPN LIKE ?";
                pst = con.prepareStatement(sql);
                pst.setString(1, offcode);
                pst.setString(2, "%" + postToSearch.toUpperCase() + "%");
            } else {
                //sql = "SELECT COUNT(*) CNT FROM G_SPC WHERE OFF_CODE=?";
                sql = "SELECT count(*) cnt FROM G_POST"
                        + " INNER JOIN G_OFFICE ON G_POST.DEPARTMENT_CODE=G_OFFICE.DEPARTMENT_CODE WHERE OFF_CODE=?";
                pst = con.prepareStatement(sql);
                pst.setString(1, offcode);
            }
            rs = pst.executeQuery();
            if (rs.next()) {
                cnt = rs.getInt("CNT");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return cnt;
    }

    @Override
    public List getGPCWiseEmployeeList(String postcode, String offcode) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List emplist = new ArrayList();

        SelectOption so = null;
        try {
            con = this.dataSource.getConnection();

            String sql = "select EMP_ID,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ')"
                    + "EMPNAME,SPC from (select spc,off_code,gpc from g_spc where off_code=? and gpc=? AND (IFUCLEAN!='Y' OR IFUCLEAN IS NULL))g_spc inner join emp_mast on g_spc.spc=emp_mast.cur_spc order by f_name";
            pst = con.prepareStatement(sql);
            pst.setString(1, offcode);
            pst.setString(2, postcode);
            rs = pst.executeQuery();
            while (rs.next()) {
                so = new SelectOption();
                so.setValue(rs.getString("EMP_ID") + "-" + rs.getString("SPC"));
                so.setLabel(rs.getString("EMPNAME") + "(" + rs.getString("EMP_ID") + ")");
                emplist.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return emplist;
    }

    @Override
    public List getGPCWiseEmployeeListOnlySPC(String postcode) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List emplist = new ArrayList();

        SelectOption so = null;
        try {
            con = this.dataSource.getConnection();

            String sql = "select EMP_ID,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ')"
                    + "EMPNAME,SPC from (select spc,off_code,gpc from g_spc where gpc=? AND (IFUCLEAN!='Y' OR IFUCLEAN IS NULL))g_spc inner join emp_mast on g_spc.spc=emp_mast.cur_spc order by f_name";
            pst = con.prepareStatement(sql);
            pst.setString(1, postcode);
            rs = pst.executeQuery();
            while (rs.next()) {
                so = new SelectOption();
                so.setValue(rs.getString("SPC"));
                so.setLabel(rs.getString("EMPNAME") + "(" + rs.getString("EMP_ID") + ")");
                emplist.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return emplist;
    }

    @Override
    public List getOfficeWithSPCList(String offcode) {
        Connection con = null;

        PreparedStatement ps = null;
        ResultSet rs = null;

        List li = new ArrayList();

        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("select spc,post,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMPNAME,emp_id,spn from ("
                    + "select spc,gpc,spn from g_spc where off_code=? AND (IFUCLEAN!='Y' OR IFUCLEAN IS NULL)) g_spc "
                    + "inner join g_post on g_spc.GPC = g_post.POST_CODE "
                    + "left outer join EMP_MAST ON G_SPC.SPC=EMP_MAST.CUR_SPC ORDER BY POST");

            ps.setString(1, offcode);
            rs = ps.executeQuery();
            while (rs.next()) {

                SubstantivePost spc = new SubstantivePost();
                spc.setPostname(rs.getString("post"));
                spc.setEmpname(rs.getString("EMPNAME"));
                spc.setSpc(rs.getString("spc"));
                spc.setSpn(rs.getString("spn"));
                spc.setEmpid(rs.getString("emp_id"));
                String spcHrmsId = rs.getString("spc") + "|" + rs.getString("emp_id") + "|" + rs.getString("post") + "|" + rs.getString("EMPNAME");
                spc.setSpcHrmsId(spcHrmsId);
                li.add(spc);

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    @Override
    public List getCadreWisePostList(String offcode, String cadrecode) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List spclist = new ArrayList();

        SubstantivePost sp = null;
        String post = "";
        try {
            con = this.dataSource.getConnection();

            if (cadrecode != null && (cadrecode.equals("1101") || cadrecode.equals("1103") || cadrecode.equals("2668"))) {
                String sql = "SELECT TAB1.SPC SPC,TAB1.POST POST,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMPNAME FROM"
                        + " (SELECT TAB.POST,SPC FROM"
                        + " (SELECT G_POST.POST_CODE,G_POST.POST FROM (SELECT POST_CODE FROM G_CADRE2POST WHERE CADRE_CODE=?)CADRE2POST"
                        + " INNER JOIN G_POST ON G_POST.POST_CODE=CADRE2POST.POST_CODE)TAB INNER JOIN (SELECT SPC,GPC FROM G_SPC WHERE OFF_CODE=?) G_SPC ON G_SPC.GPC= TAB.POST_CODE)TAB1 LEFT OUTER JOIN"
                        + " EMP_MAST ON EMP_MAST.CUR_SPC=TAB1.SPC";
                pst = con.prepareStatement(sql);
                pst.setString(1, cadrecode);
                pst.setString(2, offcode);
            } else {
                String sql = "SELECT POST,SPC,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMPNAME FROM("
                        + " SELECT SPC,POST FROM ("
                        + " SELECT DISTINCT GPC,SPC FROM G_SPC WHERE OFF_CODE=?) G_SPC"
                        + " INNER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE) G_POST"
                        + " LEFT OUTER JOIN EMP_MAST ON G_POST.SPC=EMP_MAST.CUR_SPC ORDER BY POST";
                pst = con.prepareStatement(sql);
                pst.setString(1, offcode);
            }
            rs = pst.executeQuery();
            while (rs.next()) {
                post = rs.getString("POST");
                if (rs.getString("EMPNAME") != null && !rs.getString("EMPNAME").equals("")) {
                    post = post + ", " + rs.getString("EMPNAME");
                }
                sp = new SubstantivePost();
                sp.setPostname(post);
                sp.setSpc(rs.getString("SPC"));
                spclist.add(sp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return spclist;
    }

    @Override
    public List getSanctioningSPCOfficeWiseList(String deptcode, String offcode) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        List spclist = new ArrayList();
        String post = "";
        SubstantivePost sp = null;
        try {
            con = this.dataSource.getConnection();
            if (deptcode != null && deptcode.equals("LM")) {
                pst = con.prepareStatement("SELECT LMID,ARRAY_TO_STRING(ARRAY[INITIALS, FNAME, MNAME,LNAME], ' ') EMPNAME,OFF_AS,RANK FROM LA_MEMBERS WHERE ACTIVE='Y' AND OFF_AS=?");
                pst.setString(1, offcode);
                rs = pst.executeQuery();
                while (rs.next()) {
                    sp = new SubstantivePost();
                    sp.setPostname(rs.getString("EMPNAME"));
                    sp.setSpc(rs.getString("LMID"));
                    //wa.setPost(getAssignedDepartment(rs.getString("LMID"), offcode));                    
                    spclist.add(sp);
                }
            } else {
                String sql = "select spc,post,GPF_NO,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMPNAME from "
                        + "(select spc,gpc from g_spc where off_code=? AND IS_SANCTIONING_SPC = 'Y' AND (IFUCLEAN!='Y' OR IFUCLEAN IS NULL)) g_spc "
                        + "inner join g_post on g_spc.GPC = g_post.POST_CODE left outer join EMP_MAST ON G_SPC.SPC=EMP_MAST.CUR_SPC WHERE ISAUTHORITY='Y' ORDER BY POST";
                pst = con.prepareStatement(sql);
                pst.setString(1, offcode);
                rs = pst.executeQuery();
                while (rs.next()) {
                    post = rs.getString("post");
                    if (rs.getString("EMPNAME") != null && !rs.getString("EMPNAME").equals("")) {
                        post = post + ", " + rs.getString("EMPNAME") + "(" + rs.getString("GPF_NO") + ")";
                    }
                    sp = new SubstantivePost();
                    sp.setPostname(post);
                    sp.setSpc(rs.getString("SPC"));
                    spclist.add(sp);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return spclist;
    }

    @Override
    public List getSanctioningSPCOfficeWiseList(String offcode) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List spclist = new ArrayList();

        String post = "";

        SubstantivePost sp = null;
        try {
            con = this.dataSource.getConnection();

            String sql = "select spc,post,GPF_NO,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMPNAME from "
                    + "(select spc,gpc from g_spc where off_code=? AND IS_SANCTIONING_SPC = 'Y' AND (IFUCLEAN!='Y' OR IFUCLEAN IS NULL)) g_spc "
                    + "inner join g_post on g_spc.GPC = g_post.POST_CODE left outer join EMP_MAST ON G_SPC.SPC=EMP_MAST.CUR_SPC WHERE ISAUTHORITY='Y' ORDER BY POST";
            pst = con.prepareStatement(sql);
            pst.setString(1, offcode);
            rs = pst.executeQuery();
            while (rs.next()) {
                post = rs.getString("post");
                if (rs.getString("EMPNAME") != null && !rs.getString("EMPNAME").equals("")) {
                    post = post + ", " + rs.getString("EMPNAME") + "(" + rs.getString("GPF_NO") + ")";
                }
                sp = new SubstantivePost();
                sp.setPostname(post);
                sp.setSpc(rs.getString("SPC"));
                spclist.add(sp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return spclist;
    }

    @Override
    public List getEmployeeWithSPCOfficeWiseList(String offcode) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List spclist = new ArrayList();

        String post = "";

        SubstantivePost sp = null;
        try {
            con = this.dataSource.getConnection();

            String sql = "select spc,post,GPF_NO,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMPNAME,emp_id from "
                    + "(select spc,gpc from g_spc where off_code=? AND IS_SANCTIONING_SPC = 'Y' AND (IFUCLEAN!='Y' OR IFUCLEAN IS NULL)) g_spc "
                    + "inner join g_post on g_spc.GPC = g_post.POST_CODE inner join EMP_MAST ON G_SPC.SPC=EMP_MAST.CUR_SPC WHERE ISAUTHORITY='Y' ORDER BY POST";
            pst = con.prepareStatement(sql);
            pst.setString(1, offcode);
            rs = pst.executeQuery();
            while (rs.next()) {
                post = rs.getString("post");
                if (rs.getString("EMPNAME") != null && !rs.getString("EMPNAME").equals("")) {
                    post = post + ", " + rs.getString("EMPNAME") + "(" + rs.getString("GPF_NO") + ")";
                }
                sp = new SubstantivePost();
                sp.setEmpid(rs.getString("emp_id"));
                sp.setPostname(post);
                sp.setSpc(rs.getString("SPC"));
                spclist.add(sp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return spclist;
    }

    @Override
    public List getApprovingSPCOfficeWiseList(String offcode) {
        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List spclist = new ArrayList();

        String post = "";

        SubstantivePost sp = null;
        try {
            con = this.dataSource.getConnection();

            String sql = "SELECT spc,post,GPF_NO,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMPNAME,EMP_ID,DDO_HRMSID FROM G_OFFICE "
                    + "inner join EMP_MAST ON G_OFFICE.DDO_HRMSID=EMP_MAST.EMP_ID "
                    + "inner join G_SPC ON G_SPC.SPC=EMP_MAST.CUR_SPC "
                    + "inner join g_post on g_spc.GPC = g_post.POST_CODE "
                    + "WHERE G_OFFICE.OFF_CODE=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, offcode);
            rs = pst.executeQuery();
            while (rs.next()) {
                post = rs.getString("post");
                if (rs.getString("EMPNAME") != null && !rs.getString("EMPNAME").equals("")) {
                    post = post + ", " + rs.getString("EMPNAME") + "(" + rs.getString("GPF_NO") + ")";
                }
                sp = new SubstantivePost();
                sp.setEmpid(rs.getString("EMP_ID"));
                sp.setPostname(post);
                sp.setSpc(rs.getString("SPC"));
                spclist.add(sp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return spclist;
    }

    @Override
    public SubstantivePost getSpcDetail(String spc) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement st = null;
        SubstantivePost substantivePost = new SubstantivePost();
        try {
            con = dataSource.getConnection();
            st = con.prepareStatement("SELECT spc,gpc, spn,post FROM g_spc inner join g_post on g_spc.gpc = g_post.post_code WHERE spc = ? ");
            st.setString(1, spc);
            rs = st.executeQuery();
            if (rs.next()) {
                substantivePost.setSpc(rs.getString("spc"));
                substantivePost.setGpc(rs.getString("gpc"));
                substantivePost.setSpn(rs.getString("spn"));
                substantivePost.setPostname(rs.getString("post"));
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
    public List getSPCListWithEmployeeName(String offCode, String postCode) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        SubstantivePost spost = null;

        List spnlist = new ArrayList();
        try {
            con = this.dataSource.getConnection();
            /*String sql = "select emp_id,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMP_NAME,F_NAME,g_spc.spc,SPN,g_spc.pay_scale,pay_scale_7th_pay,g_spc.post_grp,pay_gp_level,g_spc.gp,"
             + " g_spc.cadre_code,G_DEPARTMENT.DEPARTMENT_CODE from g_spc"
             + " left outer join emp_mast on g_spc.spc=emp_mast.cur_spc LEFT OUTER JOIN G_CADRE ON G_SPC.CADRE_CODE=G_CADRE.CADRE_CODE"
             + " LEFT OUTER JOIN G_DEPARTMENT ON G_CADRE.DEPARTMENT_CODE=G_DEPARTMENT.DEPARTMENT_CODE"
             + " LEFT OUTER JOIN SECTION_POST_MAPPING ON G_SPC.SPC=SECTION_POST_MAPPING.SPC"
             + " LEFT OUTER JOIN BILL_SECTION_MAPPING ON SECTION_POST_MAPPING.SECTION_ID=BILL_SECTION_MAPPING.SECTION_ID"
             + " LEFT OUTER JOIN BILL_GROUP_MASTER ON BILL_SECTION_MAPPING.BILL_GROUP_ID=BILL_GROUP_MASTER.BILL_GROUP_ID"
             + " LEFT OUTER JOIN (SELECT BILL_GROUP_ID FROM aer2_co_mapping WHERE AER_ID=?)aer2_co_mapping ON BILL_GROUP_MASTER.BILL_GROUP_ID=aer2_co_mapping.BILL_GROUP_ID where gpc=? and G_SPC.off_code=? AND is_sanctioned='Y' and"
             + " (IFUCLEAN!='Y' OR IFUCLEAN IS NULL) ORDER BY SPN,F_NAME ASC";*/
            String sql = "select emp_id,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMP_NAME,F_NAME,g_spc.spc,SPN,g_spc.pay_scale,pay_scale_7th_pay,g_spc.post_grp,pay_gp_level,g_spc.gp,"
                    + " g_spc.cadre_code,G_DEPARTMENT.DEPARTMENT_CODE from g_spc"
                    + " left outer join emp_mast on g_spc.spc=emp_mast.cur_spc LEFT OUTER JOIN G_CADRE ON G_SPC.CADRE_CODE=G_CADRE.CADRE_CODE"
                    + " LEFT OUTER JOIN G_DEPARTMENT ON G_CADRE.DEPARTMENT_CODE=G_DEPARTMENT.DEPARTMENT_CODE"
                    + " LEFT OUTER JOIN SECTION_POST_MAPPING ON G_SPC.SPC=SECTION_POST_MAPPING.SPC"
                    + " LEFT OUTER JOIN BILL_SECTION_MAPPING ON SECTION_POST_MAPPING.SECTION_ID=BILL_SECTION_MAPPING.SECTION_ID"
                    + " LEFT OUTER JOIN BILL_GROUP_MASTER ON BILL_SECTION_MAPPING.BILL_GROUP_ID=BILL_GROUP_MASTER.BILL_GROUP_ID"
                    + " where gpc=? and G_SPC.off_code=? AND is_sanctioned='Y' and is_terminated='N' and"
                    + " (IFUCLEAN!='Y' OR IFUCLEAN IS NULL) ORDER BY SPN,F_NAME ASC";
            pst = con.prepareStatement(sql);
            pst.setString(1, postCode);
            pst.setString(2, offCode);
            rs = pst.executeQuery();
            while (rs.next()) {
                spost = new SubstantivePost();
                spost.setEmpname(rs.getString("EMP_NAME"));
                spost.setSpc(rs.getString("SPC"));
                spost.setSpn(rs.getString("SPN"));
                spost.setPayscale(rs.getString("pay_scale"));
                spost.setPayscale_7th(rs.getString("pay_scale_7th_pay"));
                spost.setPostgrp(rs.getString("post_grp"));
                spost.setPaylevel(rs.getString("pay_gp_level"));
                spost.setGp(rs.getString("gp"));
                spost.setCadre(rs.getString("cadre_code"));
                spost.setDept(rs.getString("DEPARTMENT_CODE"));
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
    public List getEmployeeNameWithSPC(String offCode, String postCode) {
        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;
        SubstantivePost spost = null;
        List spnlist = new ArrayList();
        try {
            con = this.dataSource.getConnection();
            String sql = "select emp_id,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMP_NAME,F_NAME,spc,SPN,g_spc.pay_scale,pay_scale_7th_pay,g_spc.post_grp,pay_gp_level,g_spc.gp,g_spc.cadre_code,G_DEPARTMENT.DEPARTMENT_CODE,gpf_no from g_spc"
                    + " INNER JOIN emp_mast on g_spc.spc=emp_mast.cur_spc LEFT OUTER JOIN G_CADRE ON G_SPC.CADRE_CODE=G_CADRE.CADRE_CODE"
                    + " LEFT OUTER JOIN G_DEPARTMENT ON G_CADRE.DEPARTMENT_CODE=G_DEPARTMENT.DEPARTMENT_CODE where gpc=? and off_code=? AND is_sanctioned='Y' and (IFUCLEAN!='Y' OR IFUCLEAN IS NULL)"
                    + " UNION"
                    + " select EMP_JOIN.emp_id,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMP_NAME,F_NAME,G_SPC.spc,SPN,g_spc.pay_scale,pay_scale_7th_pay,g_spc.post_grp,pay_gp_level,g_spc.gp,g_spc.cadre_code,G_DEPARTMENT.DEPARTMENT_CODE,gpf_no from emp_join"
                    + " INNER JOIN emp_mast on emp_join.emp_id=emp_mast.emp_id"
                    + " INNER JOIN G_SPC ON EMP_JOIN.SPC=G_SPC.SPC"
                    + " LEFT OUTER JOIN G_CADRE ON G_SPC.CADRE_CODE=G_CADRE.CADRE_CODE"
                    + " LEFT OUTER JOIN G_DEPARTMENT ON G_CADRE.DEPARTMENT_CODE=G_DEPARTMENT.DEPARTMENT_CODE"
                    + " where gpc=? and off_code=? AND IF_AD_CHARGE='Y' AND is_sanctioned='Y' and (IFUCLEAN!='Y' OR IFUCLEAN IS NULL) ORDER BY SPN,F_NAME ASC";
            pst = con.prepareStatement(sql);
            pst.setString(1, postCode);
            pst.setString(2, offCode);
            pst.setString(3, postCode);
            pst.setString(4, offCode);
            rs = pst.executeQuery();
            while (rs.next()) {
                spost = new SubstantivePost();
                spost.setEmpid(rs.getString("emp_id"));
                spost.setEmpname(rs.getString("EMP_NAME"));
                spost.setGpfNo(rs.getString("gpf_no"));
                spost.setSpc(rs.getString("SPC"));
                spost.setSpn(rs.getString("SPN"));
                spost.setPayscale(rs.getString("pay_scale"));
                spost.setPayscale_7th(rs.getString("pay_scale_7th_pay"));
                spost.setPostgrp(rs.getString("post_grp"));
                spost.setPaylevel(rs.getString("pay_gp_level"));
                spost.setGp(rs.getString("gp"));
                spost.setCadre(rs.getString("cadre_code"));
                spost.setDept(rs.getString("DEPARTMENT_CODE"));
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
    public List getPostCodeOfficeWise(String deptcode) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List postcodelist = new ArrayList();

        SubstantivePost spost = null;

        try {
            con = this.dataSource.getConnection();
            String sql = "SELECT POST_CODE,POST FROM G_POST"
                    + " LEFT OUTER JOIN G_SPC ON G_POST.POST_CODE=G_SPC.GPC WHERE G_POST.department_code=? AND (IFUCLEAN!='Y' OR IFUCLEAN IS NULL) GROUP BY POST_CODE,POST ORDER BY POST ASC";
            pst = con.prepareStatement(sql);
            pst.setString(1, deptcode);
            rs = pst.executeQuery();
            while (rs.next()) {
                spost = new SubstantivePost();
                spost.setPostCode(rs.getString("POST_CODE"));
                spost.setPostname(rs.getString("POST"));
                postcodelist.add(spost);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return postcodelist;
    }

    @Override
    public int updateSubstantivePost(String offCode, String gpc, String spc, String payscale_6th, String payscale_7th, String postgrp, String paylevel, String gp, String cadreCode, String chkGrantInAid, String teachingPost, String planOrNonPlan) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        int retVal = 0;
        try {
            con = this.dataSource.getConnection();
            if (spc != null && !spc.equals("")) {
                String sql = "UPDATE G_SPC SET pay_scale=?,pay_scale_7th_pay=?,post_grp=?,PAY_GP_LEVEL=?,gp=?,cadre_code=?,is_teaching_post=?,is_plan=? WHERE OFF_CODE=? AND GPC=? AND SPC=?";
                pst = con.prepareStatement(sql);
                pst.setString(1, payscale_6th);
                pst.setString(2, payscale_7th);
                if (chkGrantInAid != null && !chkGrantInAid.equals("")) {
                    pst.setString(3, "G");
                } else {
                    pst.setString(3, postgrp);
                }
                pst.setString(4, paylevel);
                if (gp != null && !gp.equals("")) {
                    pst.setInt(5, Integer.parseInt(gp));
                } else {
                    pst.setInt(5, 0);
                }
                pst.setString(6, cadreCode);
                if (teachingPost != null && !teachingPost.equals("")) {
                    pst.setString(7, teachingPost);
                } else {
                    pst.setString(7, "NT");
                }
                if (planOrNonPlan != null && !planOrNonPlan.equals("")) {
                    pst.setString(8, planOrNonPlan);
                } else {
                    pst.setString(8, "NP");
                }
                pst.setString(9, offCode);
                pst.setString(10, gpc);
                pst.setString(11, spc);

                retVal = pst.executeUpdate();
            } else {
                String sql = "UPDATE G_SPC SET pay_scale=?,pay_scale_7th_pay=?,post_grp=?,PAY_GP_LEVEL=?,gp=?,cadre_code=?,is_teaching_post=?,is_plan=? WHERE OFF_CODE=? AND GPC=?";
                pst = con.prepareStatement(sql);
                pst.setString(1, payscale_6th);
                pst.setString(2, payscale_7th);
                pst.setString(3, postgrp);
                pst.setString(4, paylevel);
                if (gp != null && !gp.equals("")) {
                    pst.setInt(5, Integer.parseInt(gp));
                } else {
                    pst.setInt(5, 0);
                }
                pst.setString(6, cadreCode);
                if (teachingPost != null && !teachingPost.equals("")) {
                    pst.setString(7, teachingPost);
                } else {
                    pst.setString(7, "NT");
                }
                if (planOrNonPlan != null && !planOrNonPlan.equals("")) {
                    pst.setString(8, planOrNonPlan);
                } else {
                    pst.setString(8, "NP");
                }
                pst.setString(9, offCode);
                pst.setString(10, gpc);
                retVal = pst.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return retVal;
    }

    @Override
    public int removeSubstantivePost(String offCode, String gpc, String spc, String filepath, MultipartFile spcTerminateFile, String terminationOrderno, String terminationOrderdate) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        int retVal = 0;

        try {
            con = this.dataSource.getConnection();

            InputStream inputStream = null;
            OutputStream outputStream = null;
            String filename = "";
            if (spcTerminateFile != null && !spcTerminateFile.isEmpty()) {
                long time = System.currentTimeMillis();
                filename = "TER_" + time + "";
            }

            String sql = "UPDATE G_SPC SET is_terminated='Y',ifuclean='Y',termination_file_id=?,post_termination_order_no=?,post_termination_order_date=? where off_code=? and gpc=? and spc=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, filename);
            pst.setString(2, terminationOrderno);
            if (terminationOrderdate != null && !terminationOrderdate.equals("")) {
                pst.setTimestamp(3, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(terminationOrderdate).getTime()));
            } else {
                pst.setTimestamp(3, null);
            }
            pst.setString(4, offCode);
            pst.setString(5, gpc);
            pst.setString(6, spc);
            retVal = pst.executeUpdate();

            if (spcTerminateFile != null && !spcTerminateFile.isEmpty()) {
                String dirpath = filepath;
                File newfile = new File(dirpath);
                if (!newfile.exists()) {
                    newfile.mkdirs();
                }

                outputStream = new FileOutputStream(dirpath + filename);
                int read = 0;
                byte[] bytes = new byte[1024];
                inputStream = spcTerminateFile.getInputStream();
                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return retVal;
    }

    @Override
    public int addSubstantivePost(String deptCode, String offCode, SubstantivePost substantivePost, String filepath) {

        Connection con = null;
        //Connection conora = null;

        PreparedStatement pst1 = null;
        PreparedStatement pst2 = null;
        ResultSet rs = null;

        PreparedStatement updatepst = null;
        PreparedStatement insertpst = null;

        int postAvailable = 0;
        int retVal = 0;

        String gpc = substantivePost.getPostCode();
        int noOfPost = Integer.parseInt(substantivePost.getTxtNoOfPost());
        try {
            con = this.dataSource.getConnection();

            MultipartFile spcAttchfile = substantivePost.getSpcFileAttch();
            InputStream inputStream = null;
            OutputStream outputStream = null;
            String filename = "";
            if (spcAttchfile != null && !spcAttchfile.isEmpty()) {
                long time = System.currentTimeMillis();
                filename = time + "";
            }

            pst2 = con.prepareStatement("SELECT SPC FROM G_SPC WHERE OFF_CODE=? AND GPC=? AND is_sanctioned='N'");

            String updateSql = "UPDATE G_SPC SET is_sanctioned='Y',IFUCLEAN='N',file_id=?,order_no=?,order_date=? WHERE SPC=?";
            updatepst = con.prepareStatement(updateSql);

            String insertSql = "insert into G_SPC (Dept_code, Off_Code, SPC, GPC, if_abolished, sub_id, territory, post_level, post_sl,is_gazetted,ORDER_NO,IFUCLEAN,PAY_SCALE,is_sanctioned,SPN,if_inoid,file_id,order_date) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            insertpst = con.prepareStatement(insertSql);

            String sql = "SELECT COUNT(*) CNT FROM G_SPC WHERE OFF_CODE=? AND GPC=? AND is_sanctioned='N'";
            pst1 = con.prepareStatement(sql);
            pst1.setString(1, offCode);
            pst1.setString(2, gpc);
            rs = pst1.executeQuery();
            if (rs.next()) {
                postAvailable = rs.getInt("CNT");
            }

            if (postAvailable > noOfPost) {
                postAvailable = noOfPost;
            }
            for (int i = 0; i < postAvailable; i++) {
                pst2.setString(1, offCode);
                pst2.setString(2, gpc);
                rs = pst2.executeQuery();
                if (rs.next()) {
                    updatepst.setString(1, filename);
                    updatepst.setString(2, substantivePost.getOrderNo());
                    if (substantivePost.getOrderDate() != null && !substantivePost.getOrderDate().equals("")) {
                        updatepst.setTimestamp(3, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(substantivePost.getOrderDate()).getTime()));
                    } else {
                        updatepst.setTimestamp(3, null);
                    }
                    updatepst.setString(4, rs.getString("SPC"));
                    retVal = updatepst.executeUpdate();
                }
            }
            for (int i = 1; i <= noOfPost - postAvailable; i++) {
                generateSPN(offCode, gpc, substantivePost);
                insertpst.setString(1, deptCode);
                insertpst.setString(2, offCode);
                insertpst.setString(3, generateSpc(offCode, gpc));
                insertpst.setString(4, gpc);
                insertpst.setString(5, "");
                insertpst.setString(6, substantivePost.getSubid());
                insertpst.setString(7, substantivePost.getTerritory());
                insertpst.setInt(8, 0);
                insertpst.setInt(9, 0);
                insertpst.setString(10, "");
                insertpst.setString(11, substantivePost.getOrderNo());
                insertpst.setString(12, "N");
                insertpst.setString(13, "");
                insertpst.setString(14, "Y");
                insertpst.setString(15, substantivePost.getSpn());
                insertpst.setString(16, "P");
                insertpst.setString(17, filename);
                if (substantivePost.getOrderDate() != null && !substantivePost.getOrderDate().equals("")) {
                    insertpst.setTimestamp(18, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(substantivePost.getOrderDate()).getTime()));
                } else {
                    insertpst.setTimestamp(18, null);
                }
                insertpst.executeUpdate();
            }
            if (spcAttchfile != null && !spcAttchfile.isEmpty()) {
                String dirpath = filepath;
                File newfile = new File(dirpath);
                if (!newfile.exists()) {
                    newfile.mkdirs();
                }

                outputStream = new FileOutputStream(dirpath + filename);
                int read = 0;
                byte[] bytes = new byte[1024];
                inputStream = spcAttchfile.getInputStream();
                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst1);
            DataBaseFunctions.closeSqlObjects(pst2);
            DataBaseFunctions.closeSqlObjects(updatepst);
            DataBaseFunctions.closeSqlObjects(insertpst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return retVal;
    }

    private String generateSpc(String offCode, String gpc) {

        Connection conora = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String mcode = "";
        try {
            conora = this.dataSource.getConnection();

            String sql = "select max(spc) mcode from G_SPC where off_code=? and gpc=?";
            pst = conora.prepareStatement(sql);
            pst.setString(1, offCode);
            pst.setString(2, gpc);
            rs = pst.executeQuery();
            if (rs.next()) {
                mcode = rs.getString("mcode");
            }
            if (mcode != null && !mcode.equals("")) {
                mcode = getNextString(mcode);
            } else {
                mcode = offCode + gpc + "0001";
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(conora);
        }
        return mcode;
    }

    private SubstantivePost generateSPN(String offCode, String gpc, SubstantivePost substantivePost) {

        Connection conora = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String spn = "";
        try {
            conora = this.dataSource.getConnection();

            String sql = "SELECT CATEGORY,SUFFIX FROM G_OFFICE WHERE OFF_CODE=?";
            pst = conora.prepareStatement(sql);
            pst.setString(1, offCode);
            rs = pst.executeQuery();
            String subid = "";
            String suffix = "";
            if (rs.next()) {
                if (rs.getString("CATEGORY") != null) {
                    subid = ", " + rs.getString("CATEGORY");
                }
                if (rs.getString("SUFFIX") != null) {
                    suffix = ", " + rs.getString("SUFFIX");
                }
                substantivePost.setSubid(rs.getString("CATEGORY"));
                substantivePost.setTerritory(rs.getString("SUFFIX"));
            }

            DataBaseFunctions.closeSqlObjects(rs, pst);

            sql = "SELECT POST FROM G_POST WHERE POST_CODE=?";
            pst = conora.prepareStatement(sql);
            pst.setString(1, gpc);
            rs = pst.executeQuery();
            if (rs.next()) {

                spn = rs.getString("POST") + subid + suffix;
                substantivePost.setSpn(spn);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(conora);
        }
        return substantivePost;
    }

    public static String getNextString(String previousString) {
        String nextString = "";
        long prevNum = 0;
        String zeroStr = "";
        if (previousString != null && !previousString.equals("")) {

            if (isNumeric(previousString) == true) {
                prevNum = Long.parseLong(previousString);
                prevNum++;
                nextString = String.valueOf(prevNum);
                if (nextString.length() < previousString.length()) {
                    for (int cnt = 0; cnt < (previousString.length() - nextString.length()); cnt++) {
                        zeroStr = zeroStr + "0";
                    }
                    nextString = zeroStr + nextString;
                }
            } else {
                if (isNumericChar(previousString.substring(0, 1)) == true) {

                    String tmp1 = previousString.substring(previousString.length() - 1, previousString.length());
                    char tmp = tmp1.charAt(0);
                    int asc = (int) tmp;
                    if (asc < 90) {
                        asc = asc + 1;
                        char[] newStr = {(char) asc};
                        String finalStr = new String(newStr);
                        nextString = previousString.substring(0, previousString.length() - 1) + finalStr;
                    } else {
                        nextString = previousString + "A";
                    }

                } else {
                    int i = 2;
                    for (i = 0; i < previousString.length(); i++) {
                        if (isNumericChar(previousString.substring(i, i + 1)) == true) {
                            break;
                        }
                    }
                    prevNum = Long.parseLong(previousString.substring(i, previousString.length()));
                    prevNum++;
                    for (int cnt = 0; cnt < (previousString.length() - i) - String.valueOf(prevNum).length(); cnt++) {
                        zeroStr = zeroStr + "0";
                    }
                    nextString = zeroStr + nextString;
                    nextString = previousString.substring(0, i) + zeroStr + String.valueOf(prevNum);
                }
            }
        } else {
            nextString = "1";
        }
        return nextString;
    }

    public static boolean isNumeric(String inStr) {
        boolean result = false;
        try {
            long num = Long.parseLong(inStr);
            result = true;
        } catch (NumberFormatException nfe) {
            //nfe.printStackTrace();
            result = false;
        }
        return result;
    }

    public static boolean isNumericChar(String inStr) {
        boolean result = false;
        if (inStr.equals("0") || inStr.equals("1") || inStr.equals("2") || inStr.equals("3") || inStr.equals("4") || inStr.equals("5") || inStr.equals("6") || inStr.equals("7") || inStr.equals("8") || inStr.equals("9")) {
            result = true;
        }
        return result;
    }

    @Override
    public List listSubPost(SubstantivePost substantivePost) {
        List list = null;
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        SubstantivePost pformdata = null;
        list = new ArrayList();
        String dcode = substantivePost.getDeptCode();
        String ocode = substantivePost.getOffCode();
        try {

            con = this.repodataSource.getConnection();
            pst = con.prepareStatement("SELECT PS.POST,PS.post_code, (SELECT COUNT(*) as cnt FROM g_spc WHERE gpc = PS.post_code AND off_code =? AND dept_code = ?) FROM (SELECT post, post_code FROM g_post GP WHERE GP.department_code=? ORDER BY post) AS PS");
            pst.setString(1, ocode);
            pst.setString(2, dcode);
            pst.setString(3, dcode);
            rs = pst.executeQuery();
            while (rs.next()) {
                pformdata = new SubstantivePost();
                pformdata.setPostname(rs.getString("post"));
                pformdata.setTotalPost(rs.getString("cnt"));
                pformdata.setDeptCode(dcode);
                pformdata.setOffCode(ocode);
                pformdata.setPostCode(rs.getString("post_code"));
                list.add(pformdata);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return list;
    }

    @Override
    public int savePostdataDetails(SubstantivePost substantivePost) {
        int n = 0;
        PreparedStatement insertpst = null;
        Connection con = null;
        ResultSet rs = null;
        String offCode = substantivePost.getOffCode();
        String deptcode = substantivePost.getDeptCode();
        String gpc = substantivePost.getPostCode();
        int noofPost = Integer.parseInt(substantivePost.getTxtNoOfPost());
        String orderdate = substantivePost.getOrderDate();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");

        try {
            con = dataSource.getConnection();
            Date outputDate = dateFormat.parse(orderdate);
            insertpst = con.prepareStatement("SELECT ((SELECT (max(cast(REPLACE(spc, (OFF_CODE || GPC), '') as integer))) AS new_code FROM g_spc WHERE off_code='" + offCode + "' AND gpc='" + gpc + "' group by cast(REPLACE(spc, (OFF_CODE || GPC), '') as integer) ORDER BY cast(REPLACE(spc, (OFF_CODE || GPC), '') as integer) DESC LIMIT 1)) AS new_code FROM g_spc  WHERE off_code='" + offCode + "' AND gpc='" + gpc + "' LIMIT 1");
            int cnt = 0;
            rs = insertpst.executeQuery();
            while (rs.next()) {
                cnt = rs.getInt("new_code");
            }
            insertpst = con.prepareStatement("INSERT INTO G_SPC (DEPT_CODE, OFF_CODE, SPC, GPC, IF_ABOLISHED, "
                    + "SUB_ID, TERRITORY, POST_LEVEL, POST_SL,IS_GAZETTED,ORDER_NO,ORDER_DATE, IFUCLEAN,PAY_SCALE,IS_SANCTIONED,SPN,IF_INOID) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            for (int x = 1; x <= noofPost; x++) {
                int newcnt = cnt + x;
                String newcntpad = "0000" + newcnt;
                newcntpad = newcntpad.substring(newcntpad.length() - 4);
                String newCode = offCode + gpc + newcntpad;
                generateSPN(offCode, gpc, substantivePost);
                insertpst.setString(1, deptcode);
                insertpst.setString(2, offCode);
                insertpst.setString(3, newCode);
                insertpst.setString(4, gpc);
                insertpst.setString(5, "");
                insertpst.setString(6, substantivePost.getSubid());
                insertpst.setString(7, substantivePost.getTerritory());
                insertpst.setInt(8, 0);
                insertpst.setInt(9, 0);
                insertpst.setString(10, "");
                insertpst.setString(11, substantivePost.getOrderNo());
                insertpst.setTimestamp(12, new Timestamp(outputDate.getTime()));
                insertpst.setString(13, "N");
                insertpst.setString(14, "");
                insertpst.setString(15, "Y");
                insertpst.setString(16, substantivePost.getSpn());
                insertpst.setString(17, "P");
                insertpst.executeUpdate();
            }
        } catch (SQLException | ParseException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(insertpst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return n;
    }//getGPCWiseSPCList

    @Override
    public List getGPCWiseSectionWiseSPCList(String offcode, String gpc) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        List li = new ArrayList();
        SelectOption so = null;
        try {
            con = this.dataSource.getConnection();
            if (isOnlineBillSubmissionEnabledForOffice(offcode) == true) {
                pst = con.prepareStatement("SELECT section_post_mapping.SPC,SPN,section_name FROM G_SPC"
                        + " inner join section_post_mapping on g_spc.spc=section_post_mapping.spc"
                        + " inner join g_section on section_post_mapping.section_id=g_section.section_id"
                        + " LEFT OUTER JOIN EMP_MAST ON EMP_MAST.CUR_SPC=G_SPC.SPC where g_spc.OFF_CODE=? AND GPC=? and spn is not null AND"
                        + " (IFUCLEAN!='Y' OR IFUCLEAN IS NULL) and is_sanctioned='Y' and (emp_id is null or emp_id = '') ORDER BY SPN");
                pst.setString(1, offcode);
                pst.setString(2, gpc);
                rs = pst.executeQuery();
                while (rs.next()) {
                    so = new SelectOption();
                    so.setValue(rs.getString("SPC"));
                    so.setLabel(rs.getString("SPN") + "(" + rs.getString("section_name") + ")");
                    li.add(so);
                }
            } else {
                pst = con.prepareStatement("SELECT SPC,SPN FROM G_SPC"
                        + " LEFT OUTER JOIN EMP_MAST ON EMP_MAST.CUR_SPC=G_SPC.SPC where g_spc.OFF_CODE=? AND GPC=? and spn is not null AND"
                        + " (IFUCLEAN!='Y' OR IFUCLEAN IS NULL) and is_sanctioned='Y' and (emp_id is null or emp_id = '') ORDER BY SPN");
                pst.setString(1, offcode);
                pst.setString(2, gpc);
                rs = pst.executeQuery();
                while (rs.next()) {
                    so = new SelectOption();
                    so.setValue(rs.getString("SPC"));
                    so.setLabel(rs.getString("SPN"));
                    li.add(so);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    @Override
    public List getGPCWiseSPCList(String offcode, String gpc) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        List li = new ArrayList();
        SelectOption so = null;
        try {
            con = this.dataSource.getConnection();
            pst = con.prepareStatement("SELECT SPC,SPN FROM G_SPC WHERE  OFF_CODE=? AND GPC=? AND (IFUCLEAN !='Y' OR IFUCLEAN IS NULL) and is_sanctioned='Y'");
            pst.setString(1, offcode);
            pst.setString(2, gpc);
            rs = pst.executeQuery();
            while (rs.next()) {
                so = new SelectOption();
                so.setValue(rs.getString("SPC"));
                so.setLabel(rs.getString("SPN"));
                li.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    @Override
    public String getDeptName(String deptCode) {
        String deptName = null;
        Connection con = null;
        Statement statement = null;
        ResultSet res = null;
        try {
            con = dataSource.getConnection();
            statement = con.createStatement();
            res = statement.executeQuery("SELECT DEPARTMENT_NAME FROM G_DEPARTMENT WHERE DEPARTMENT_CODE = '" + deptCode + "'");
            if (res.next()) {
                deptName = res.getString("DEPARTMENT_NAME");
            }
        } catch (SQLException sqe) {
            sqe.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(res, statement);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return deptName;
    }

    @Override
    public ArrayList getSPCList(String offCode, String postCode) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        ArrayList spcList = new ArrayList();
        SubstantivePost sp = null;
        try {
            con = dataSource.getConnection();
            ps = con.prepareStatement("SELECT GS.ifuclean, GS.spc,GS.spn, GS.is_sanctioned, GO.off_en, ARRAY_TO_STRING(ARRAY[INITIALS, EM.F_NAME, EM.M_NAME,EM.L_NAME], ' ') EMPNAME, emp_id, is_regular, gpf_no FROM g_spc GS "
                    + " INNER JOIN g_office GO ON GS.off_code = GO.off_code "
                    + " LEFT OUTER JOIN emp_mast EM ON EM.cur_spc = GS.spc"
                    + " WHERE GS.gpc = ? AND GS.off_code = ? ORDER BY GS.ifuclean,GS.is_sanctioned desc,GS.spc");
            ps.setString(1, postCode);
            ps.setString(2, offCode);
            rs = ps.executeQuery();
            while (rs.next()) {
                sp = new SubstantivePost();
                sp.setIfUclean(rs.getString("ifuclean"));
                sp.setIsSanctioned(rs.getString("is_sanctioned"));
                sp.setOfficeName(rs.getString("off_en"));
                sp.setEmpName(rs.getString("EMPNAME"));
                sp.setSpc(rs.getString("spc"));
                sp.setSpn(rs.getString("spn"));
                sp.setGpfNo(rs.getString("gpf_no"));
                sp.setSpcHrmsId(rs.getString("emp_id"));
                sp.setIsRegular(rs.getString("is_regular"));
                spcList.add(sp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return spcList;
    }

    public void changeSPCstatus(String columnName, String spc, String colStatus) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        if (columnName.equals("isSanctioned")) {
            columnName = "is_sanctioned";
        }
        try {
            con = dataSource.getConnection();
            if (colStatus.equals("Y")) {
                pstmt = con.prepareStatement("UPDATE g_spc SET " + columnName + " = 'N' WHERE spc = ?");
            }
            if (colStatus.equals("N")) {
                pstmt = con.prepareStatement("UPDATE g_spc SET " + columnName + " = 'Y' WHERE spc = ?");
            }
            pstmt.setString(1, spc);
            pstmt.executeUpdate();
        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public SubstantivePost postDetails(String officeCode, String postCode) {
        List list = null;
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        PreparedStatement pst1 = null;
        ResultSet rs1 = null;
        SubstantivePost pformdata = null;
        list = new ArrayList();
        SubstantivePost spc = new SubstantivePost();
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("SELECT POST FROM g_post WHERE post_code=?");
            pst.setString(1, postCode);
            rs = pst.executeQuery();

            if (rs.next()) {
                spc.setPostname(rs.getString("POST"));
            }
            pst1 = con.prepareStatement("SELECT off_en,department_name FROM g_office,g_department  WHERE  off_code=? AND g_office.department_code=g_department.department_code");
            pst1.setString(1, officeCode);
            rs1 = pst1.executeQuery();

            if (rs1.next()) {
                spc.setOfficeName(rs1.getString("OFF_EN"));
                spc.setDept(rs1.getString("department_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return spc;
    }

    @Override
    public ArrayList getVacantSubstantivePostList(String offcode, String postcode) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        SubstantivePost spost = null;

        ArrayList spnlist = new ArrayList();
        try {
            con = this.dataSource.getConnection();

            String sql = "select spc,spn,f_name from g_spc"
                    + " left outer join emp_mast on g_spc.spc=emp_mast.cur_spc"
                    + " where gpc=? and G_SPC.off_code=? AND is_sanctioned='Y' and (IFUCLEAN!='Y' OR IFUCLEAN IS NULL)";
            pst = con.prepareStatement(sql);
            pst.setString(1, postcode);
            pst.setString(2, offcode);
            rs = pst.executeQuery();
            while (rs.next()) {
                spost = new SubstantivePost();
                spost.setSpc(rs.getString("SPC"));
                spost.setSpn(rs.getString("SPN"));
                spnlist.add(spost);
                System.out.println("current spc:" + rs.getString("SPC"));
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
    public List getAuthorityGenericPostList(String offcode) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        SelectOption so = null;

        ArrayList gpclist = new ArrayList();
        try {
            con = this.dataSource.getConnection();

            String sql = "SELECT POST_CODE,POST FROM G_POST INNER JOIN (SELECT DISTINCT GPC FROM G_SPC WHERE OFF_CODE=? AND SPN is not null and is_sanctioned='Y' AND (IFUCLEAN !='Y' OR IFUCLEAN IS NULL)) AS G_SPC ON"
                    + " G_SPC.GPC=G_POST.POST_CODE where isauthority='Y' ORDER BY POST";
            pst = con.prepareStatement(sql);
            pst.setString(1, offcode);
            rs = pst.executeQuery();
            while (rs.next()) {
                so = new SelectOption();
                so.setValue(rs.getString("POST_CODE"));
                so.setLabel(rs.getString("POST"));
                gpclist.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return gpclist;

    }

    @Override
    public ArrayList getAuthoritySubstantivePostList(String offcode, String postcode) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        SubstantivePost spost = null;

        ArrayList spnlist = new ArrayList();
        try {
            con = this.dataSource.getConnection();

            String sql = "select spc,spn,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMPNAME from g_spc"
                    + " left outer join emp_mast on g_spc.spc=emp_mast.cur_spc"
                    + " where gpc=? and G_SPC.off_code=? AND is_sanctioned='Y' and (IFUCLEAN!='Y' OR IFUCLEAN IS NULL) order by f_name";
            pst = con.prepareStatement(sql);
            pst.setString(1, postcode);
            pst.setString(2, offcode);
            rs = pst.executeQuery();
            while (rs.next()) {
                spost = new SubstantivePost();
                spost.setSpc(rs.getString("SPC"));
                spost.setSpn(rs.getString("SPN") + "(" + rs.getString("EMPNAME") + ")");
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
    public ArrayList getSubstantivePostListBacklogEntry(String offcode, String postcode) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        SubstantivePost spost = null;

        ArrayList spnlist = new ArrayList();
        try {
            con = this.dataSource.getConnection();

            String sql = "select spc,spn from g_spc where gpc=? and off_code=? AND is_sanctioned='Y' and (IFUCLEAN!='Y' OR IFUCLEAN IS NULL)";
            pst = con.prepareStatement(sql);
            pst.setString(1, postcode);
            pst.setString(2, offcode);
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
    public List getDuplicatePostList(String offcode) {
        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        SubstantivePost spost = null;

        ArrayList dupspnlist = new ArrayList();
        try {
            con = this.dataSource.getConnection();

            String sql = "select g_spc.spc,spn,cntduplicate from\n"
                    + "(select * from(select cur_spc,count(*) cntduplicate from emp_mast where cur_off_code=? \n"
                    + "group by cur_spc)temp where cntduplicate > 1)emp \n"
                    + "inner join g_spc on g_spc.spc=emp.cur_spc where spn is not null";
            pst = con.prepareStatement(sql);
            pst.setString(1, offcode);
            rs = pst.executeQuery();
            while (rs.next()) {
                spost = new SubstantivePost();
                spost.setSpc(rs.getString("SPC"));
                spost.setSpn(rs.getString("SPN"));
                spost.setCntDuplicateSpc(rs.getString("cntduplicate"));
                dupspnlist.add(spost);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return dupspnlist;
    }

    @Override
    public List getDuplicatePostDetails(String spc) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        SubstantivePost subPost = null;
        List dupSpclistDetails = new ArrayList();
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("SELECT EMP_ID,GPF_NO,ACCT_TYPE,IS_REGULAR,CUR_SPC,SPN,ARRAY_TO_STRING(ARRAY[INITIALS,F_NAME,M_NAME,L_NAME],' ')EMPNAME,\n"
                    + "CUR_OFF_CODE,OFF_EN,GPC,DEPARTMENT_NAME FROM (SELECT * FROM EMP_MAST) EMPMAST\n"
                    + "INNER JOIN G_SPC ON G_SPC.SPC=EMPMAST.CUR_SPC\n"
                    + "INNER JOIN G_OFFICE ON G_OFFICE.OFF_CODE=EMPMAST.CUR_OFF_CODE\n"
                    + "INNER JOIN G_DEPARTMENT ON G_DEPARTMENT.DEPARTMENT_CODE=G_OFFICE.DEPARTMENT_CODE\n"
                    + "WHERE CUR_SPC=?");
            ps.setString(1, spc);
            rs = ps.executeQuery();
            while (rs.next()) {
                subPost = new SubstantivePost();
                subPost.setEmpid(rs.getString("EMP_ID"));
                subPost.setGpfNo(rs.getString("GPF_NO"));
                subPost.setIsRegular(rs.getString("IS_REGULAR"));
                subPost.setEmpName(rs.getString("EMPNAME"));
                subPost.setOffCode(rs.getString("CUR_OFF_CODE"));
                subPost.setSpc(rs.getString("CUR_SPC"));
                subPost.setGpc(rs.getString("GPC"));
                subPost.setSpn(rs.getString("SPN"));
                subPost.setOfficeName(rs.getString("OFF_EN"));
                subPost.setDept(rs.getString("DEPARTMENT_NAME"));
                dupSpclistDetails.add(subPost);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return dupSpclistDetails;
    }

    @Override
    public ArrayList getUnmappedSPCAll(String offcode, String postcode) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        ArrayList allBlankSpc = new ArrayList();
        SubstantivePost subPost = null;
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("SELECT SPC,SPN,GPC,GETGPOSTNAME(GPC) GPOST,  EMP_ID,IFUCLEAN FROM (SELECT SPC,SPN,GPC,GETGPOSTNAME(GPC) POST ,IFUCLEAN\n"
                    + "FROM G_SPC WHERE\n"
                    + "OFF_CODE =? AND IFUCLEAN='N' AND IS_SANCTIONED='Y' AND GPC=?\n"
                    + "ORDER BY POST) G_SPC \n"
                    + "LEFT OUTER JOIN EMP_MAST ON EMP_MAST.CUR_SPC=G_SPC.SPC  WHERE EMP_ID IS NULL ORDER BY SPC LIMIT 20");
            ps.setString(1, offcode);
            ps.setString(2, postcode);
            rs = ps.executeQuery();
            while (rs.next()) {
                subPost = new SubstantivePost();
                subPost.setSpc(rs.getString("SPC"));
                subPost.setSpn(rs.getString("SPN"));
                allBlankSpc.add(subPost);

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return allBlankSpc;
    }

    @Override
    public boolean updateBlankSpc(String newSpc, String empid, String oldSpc, String officeCode) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        int c = 0;
        boolean b = false;
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("UPDATE EMP_MAST SET CUR_SPC=? WHERE EMP_ID=? AND CUR_SPC=? AND CUR_OFF_CODE =? AND DEP_CODE='02'");
            ps.setString(1, newSpc);
            ps.setString(2, empid);
            ps.setString(3, oldSpc);
            ps.setString(4, officeCode);
            c = ps.executeUpdate();
            if (c == 1) {
                b = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return b;

    }

    @Override
    public SubstantivePost getEmpDetails(String empid) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        SubstantivePost subpost = null;
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("SELECT CUR_SPC,CUR_OFF_CODE,DEP_CODE FROM EMP_MAST WHERE EMP_ID=?");
            ps.setString(1, empid);
            rs = ps.executeQuery();
            if (rs.next()) {
                subpost = new SubstantivePost();
                subpost.setOffCode(rs.getString("CUR_OFF_CODE"));
                subpost.setSpc(rs.getString("CUR_SPC"));

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return subpost;
    }

    @Override
    public List getGOIOficeWisePostList(String offcode) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List li = new ArrayList();
        SelectOption so = null;
        try {
            con = this.repodataSource.getConnection();
            if (offcode != null && !offcode.equals("")) {
                pst = con.prepareStatement("select * from g_oth_postname where office_id=?");
                pst.setInt(1, Integer.parseInt(offcode));
                rs = pst.executeQuery();
                while (rs.next()) {
                    so = new SelectOption();
                    so.setValue(rs.getString("postname_id"));
                    so.setLabel(rs.getString("postname"));
                    li.add(so);
                    if (offcode != null && !offcode.equals("")) {
                        pst = con.prepareStatement("select * from g_oth_postname where office_id=?");
                        pst.setInt(1, Integer.parseInt(offcode));
                        rs = pst.executeQuery();
                        while (rs.next()) {
                            so = new SelectOption();
                            so.setValue(rs.getString("postname_id"));
                            so.setLabel(rs.getString("postname"));
                            li.add(so);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    private boolean isOnlineBillSubmissionEnabledForOffice(String offCode) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        boolean isEnabled = false;
        try {
            con = this.repodataSource.getConnection();
            pst = con.prepareStatement("select * from g_office where off_code=? and online_bill_submission='Y' and allow_esign='Y'");
            pst.setString(1, offCode);
            rs = pst.executeQuery();
            if (rs.next()) {
                isEnabled = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return isEnabled;
    }
}
