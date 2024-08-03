/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.master;

import hrms.SelectOption;
import hrms.common.DataBaseFunctions;
import hrms.model.master.GPost;
import hrms.model.master.GPostGroup;
import hrms.model.master.SubstantivePost;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.apache.commons.lang.StringUtils;
//import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Manas Jena
 */
public class PostDAOImpl implements PostDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public ArrayList getPostList(String departmentCode) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        ArrayList postList = new ArrayList();
        GPost gp = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("select * from g_post  where department_code=? order by post ");
            pstmt.setString(1, departmentCode);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                gp = new GPost();
                gp.setPostcode(rs.getString("POST_CODE"));
                gp.setPost(rs.getString("POST"));
                if (rs.getString("isauthority") != null && rs.getString("isauthority").equals("Y")) {
                    gp.setIsauth("Y");
                } else {
                    gp.setIsauth("N");
                }
                postList.add(gp);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return postList;
    }

    @Override
    public ArrayList getOfficeWisePostList(String offCode) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        ArrayList postList = new ArrayList();
        GPost gp = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT POST,POST_CODE FROM (SELECT DISTINCT(GPC) GPC FROM g_spc where off_code = ? and  is_sanctioned='Y') AS T1 INNER JOIN G_POST ON T1.GPC = G_POST.POST_CODE ORDER BY POST");
            pstmt.setString(1, offCode);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                gp = new GPost();
                gp.setPostcode(rs.getString("POST_CODE"));
                gp.setPost(rs.getString("POST"));
                postList.add(gp);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return postList;
    }

    @Override
    public ArrayList getGenericPostList(String departmentCode) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        ArrayList postList = new ArrayList();
        GPost gp = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("select * from g_post  where department_code=? and isauthority='Y' order by post ");
            pstmt.setString(1, departmentCode);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                gp = new GPost();
                gp.setPostcode(rs.getString("POST_CODE"));
                gp.setPost(rs.getString("POST"));
                postList.add(gp);

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return postList;
    }

    @Override
    public ArrayList getCadrewisePostList(String departmentCode, String cadreCode) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        ArrayList postList = new ArrayList();
        GPost gp = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT g_cadre2post.post_code,POST from g_post  inner join g_cadre2post on g_post.POST_CODE = g_cadre2post.post_code where department_code=? and cadre_code=? order by post");
            pstmt.setString(1, departmentCode);
            pstmt.setString(2, cadreCode);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                gp = new GPost();
                gp.setPostcode(rs.getString("POST_CODE"));
                gp.setPost(rs.getString("POST"));
                postList.add(gp);

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return postList;
    }

    @Override
    public ArrayList getPostList(String departmentCode, String offcode) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        ArrayList postList = new ArrayList();
        GPost gp = null;
        try {
            con = dataSource.getConnection();
            ps = con.prepareStatement("select distinct post_code, post  from g_post a \n"
                    + "                    left outer join g_spc b on a.post_code=b.gpc where a.department_code=? and b.off_code=? and is_sanctioned='Y' order by post ");
            ps.setString(1, departmentCode);
            ps.setString(2, offcode);
            rs = ps.executeQuery();
            while (rs.next()) {
                gp = new GPost();
                gp.setPostcode(rs.getString("POST_CODE"));
                gp.setPost(rs.getString("POST"));
                postList.add(gp);

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return postList;
    }

    @Override
    public ArrayList getPostListForAERMapping(String departmentCode) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        ArrayList postList = new ArrayList();
        GPost gp = null;
        try {
            con = dataSource.getConnection();
            ps = con.prepareStatement("select distinct post_code, post  from g_post a \n"
                    + " left outer join g_spc b on a.post_code=b.gpc where a.department_code=? and is_sanctioned='Y' order by post ");
            ps.setString(1, departmentCode);
            rs = ps.executeQuery();
            while (rs.next()) {
                gp = new GPost();
                gp.setPostcode(rs.getString("POST_CODE"));
                gp.setPost(rs.getString("POST"));
                postList.add(gp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return postList;
    }

    @Override
    public String getPostName(String postcode) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        String postname = "";
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT POST from g_post where post_code=?");
            pstmt.setString(1, postcode);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                postname = rs.getString("POST");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return postname;
    }

    @Override
    public String savePost(GPost post) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        String postCode = "";
        try {
            con = dataSource.getConnection();
            if (post.getPostcode() == null || post.getPostcode().equals("")) {
                pstmt = con.prepareStatement("select max(post_code) as postCode from g_post where department_code=?");
                pstmt.setString(1, post.getDeptcode());
                rs = pstmt.executeQuery();
                if (rs.next()) {
                    postCode = rs.getString("postCode");
                    if (postCode != null) {
                        if (postCode.length() == 5) {
                            postCode = StringUtils.right("00" + (Integer.parseInt(postCode) + 1), 5);
                        } else {
                            postCode = StringUtils.right("00" + (Integer.parseInt(postCode) + 1), 6);
                        }
                    } else {
                        postCode = post.getDeptcode() + "0001";
                    }
                }
                pstmt = con.prepareStatement("INSERT INTO G_POST (department_code,post_code,post,is_foreign_body) VALUES (?,?,?,?)");
                pstmt.setString(1, post.getDeptcode());
                pstmt.setString(2, postCode);
                pstmt.setString(3, post.getPost());
                pstmt.setString(4, post.getSltForeignBody());
                pstmt.executeUpdate();
            } else {
                pstmt = con.prepareStatement("UPDATE G_POST SET post=? WHERE post_code = ?");
                pstmt.setString(1, post.getPost());
                pstmt.setString(2, post.getPostcode());
                pstmt.executeUpdate();
            }
        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return postCode;
    }

    @Override
    public GPost getPostDetail(String postcode) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        GPost post = new GPost();
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT * FROM G_POST WHERE POST_CODE=?");
            pstmt.setString(1, postcode);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                post.setPostcode(rs.getString("post_code"));
                post.setPost(rs.getString("post"));
                post.setDeptcode(rs.getString("department_code"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return post;
    }

    @Override
    public ArrayList getCadreList(String departmentCode, String postCode) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        ArrayList postList = new ArrayList();
        GPost gp = null;
        try {
            con = dataSource.getConnection();
            ps = con.prepareStatement("select C.cadre_code, C.cadre_name"
                    + ", (SELECT COALESCE(COUNT(*),0) FROM g_cadre2post WHERE cadre_code = C.cadre_code"
                    + " AND post_code = ?) AS numPosts from g_cadre C"
                    + "                    where C.department_code=? order by C.cadre_name");
            ps.setString(1, postCode);
            ps.setString(2, departmentCode);
            rs = ps.executeQuery();
            while (rs.next()) {
                gp = new GPost();
                gp.setPostcode(rs.getString("cadre_code"));
                gp.setPost(rs.getString("cadre_name"));
                gp.setIfael(rs.getString("numPosts"));
                postList.add(gp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return postList;
    }

    @Override
    public void saveCadrePost(String postcode, String cadrecode, String mapAction) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            con = dataSource.getConnection();
            if (mapAction.equals("map")) {
                pstmt = con.prepareStatement("INSERT INTO g_cadre2post (cadre_code,post_code) VALUES (?,?)");
            }
            if (mapAction.equals("unmap")) {
                pstmt = con.prepareStatement("DELETE FROM g_cadre2post WHERE cadre_code = ? and post_code = ?");
            }
            pstmt.setString(1, cadrecode);
            pstmt.setString(2, postcode);
            pstmt.executeUpdate();
        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public ArrayList getModifiedPostList(String departmentCode) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        ArrayList postList = new ArrayList();
        GPost gp = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("select P.*,\n"
                    + "(SELECT COALESCE(COUNT(*),0) from g_spc WHERE gpc = P.post_code AND dept_code = P.department_code AND ifuclean='N' and is_sanctioned='Y') AS tot_post,\n"
                    + "(SELECT COALESCE(COUNT(*),0) from emp_mast\n"
                    + "inner join g_spc on emp_mast.cur_spc=g_spc.spc WHERE gpc = P.post_code) AS availEmp,\n"
                    + "(SELECT COALESCE(COUNT(*),0) from emp_mast where gen_post_name=P.post_code)genPostName_availemp,\n"
                    + "(select post_code from g_post where post_code=new_post_code and if_merged='V' and post_code=P.post_code) vpost\n"
                    + "from g_post P where P.department_code=? and P.if_merged<>'Y' order by P.post");
            pstmt.setString(1, departmentCode);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                gp = new GPost();
                gp.setPostcode(rs.getString("POST_CODE"));
                gp.setPost(rs.getString("POST"));
                gp.setVacation(rs.getString("Vacation"));
                gp.setPostgrpType(rs.getString("post_group_type"));
                gp.setIsauth(rs.getString("isauthority"));
                gp.setTotNumPost(rs.getInt("tot_post") + "");
                gp.setNumEmployees(rs.getInt("availEmp") + "");
                gp.setAvailEmpOnGenPostName(rs.getInt("genPostName_availemp") + "");
                if (rs.getString("vpost") != null && !rs.getString("vpost").equals("")) {
                    gp.setVerifiedMergedPost("V");
                } else {
                    gp.setVerifiedMergedPost("U");
                }
                postList.add(gp);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return postList;
    }

    @Override
    public void mergeDuplicatePost(GPost post) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs2 = null;
        PreparedStatement ps2 = null;
        PreparedStatement ps3 = null;
        int mergeId = 0;
        String spc = null;
        try {
            con = dataSource.getConnection();
            ps = con.prepareStatement("SELECT (COALESCE(MAX(merge_group),0)+1) AS new_merge_group FROM g_post"
                    + " WHERE department_code = ?");
            ps.setString(1, post.getDeptcode());
            rs = ps.executeQuery();
            while (rs.next()) {
                mergeId = rs.getInt("new_merge_group");
            }
            String[] strPostCodes = post.getPostCodes().split(",");
            String isCorrect = null;
            String postCode = "";
            String offCode = "";
            String masterPostCode = "";
            int maxId = 0;
            String newSpc = "";
            ArrayList al = new ArrayList();

            for (int i = 0; i < strPostCodes.length; i++) {
                postCode = strPostCodes[i];
                if (postCode.equals(post.getIsCorrect())) {
                    //Find the distinct off code from spc
                    ps1 = con.prepareStatement("UPDATE "
                            + " g_spc SET new_spc = spc, new_gpc = gpc WHERE gpc = ? AND dept_code = ?");
                    ps1.setString(1, postCode);
                    ps1.setString(2, post.getDeptcode());
                    ps1.executeUpdate();
                }
            }
            for (int i = 0; i < strPostCodes.length; i++) {
                isCorrect = "N";
                postCode = strPostCodes[i];
                masterPostCode = post.getIsCorrect();
                if (postCode.equals(post.getIsCorrect())) {
                    isCorrect = "Y";
                }
                pstmt = con.prepareStatement("UPDATE g_post SET merge_group = ?"
                        + ", is_correct = ?, new_post_code = ? WHERE post_code = ?");
                pstmt.setInt(1, mergeId);
                pstmt.setString(2, isCorrect);
                pstmt.setString(3, post.getIsCorrect());
                pstmt.setString(4, postCode);
                pstmt.executeUpdate();
                if (!postCode.equals(post.getIsCorrect())) {
                    ps2 = con.prepareStatement("SELECT * FROM g_spc"
                            + " WHERE dept_code = ? AND gpc = ? ORDER BY off_code, CAST(REPLACE(spc, CONCAT(off_code, gpc), '') AS INTEGER)");
                    ps2.setString(1, post.getDeptcode());
                    ps2.setString(2, postCode);
                    rs2 = ps2.executeQuery();
                    while (rs2.next()) {
                        spc = rs2.getString("spc");
                        offCode = rs2.getString("off_code");
                        maxId = getSPCSuffix(post.getDeptcode(), masterPostCode, offCode);
                        newSpc = offCode + masterPostCode + StringUtils.leftPad((maxId + ""), 4, "0");

                        ps3 = con.prepareStatement("UPDATE g_spc"
                                + " SET new_spc = ?, new_gpc = ? WHERE spc = ?");
                        ps3.setString(1, newSpc);
                        ps3.setString(2, masterPostCode);
                        ps3.setString(3, rs2.getString("spc"));
                        ps3.executeUpdate();
                    }
                }

            }

        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(rs2, ps2);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public ArrayList viewDuplicatePostList(String departmentCode) {
        Connection con = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        PreparedStatement pstmt = null;
        PreparedStatement ps = null;
        ArrayList al = new ArrayList();
        GPost gp = null;
        try {
            con = dataSource.getConnection();
            ps = con.prepareStatement("SELECT DISTINCT merge_group FROM g_post WHERE department_code = ?"
                    + " AND merge_group > 0 ORDER BY merge_group");
            ps.setString(1, departmentCode);
            rs1 = ps.executeQuery();
            while (rs1.next()) {
                ArrayList postList = new ArrayList();
                GPostGroup GPGroup = new GPostGroup();
                GPGroup.setMergeGroup(rs1.getInt("merge_group") + "");
                pstmt = con.prepareStatement("select P.*, (SELECT COALESCE(COUNT(*),0) from g_spc"
                        + " WHERE gpc = P.post_code) AS total_rows from g_post P"
                        + " where P.department_code=? AND merge_group = ? order by P.post");
                pstmt.setString(1, departmentCode);
                pstmt.setInt(2, rs1.getInt("merge_group"));
                rs = pstmt.executeQuery();
                while (rs.next()) {
                    gp = new GPost();
                    gp.setPostcode(rs.getString("POST_CODE"));
                    gp.setPost(rs.getString("POST"));
                    gp.setNumEmployees(rs.getInt("total_rows") + "");
                    gp.setIsCorrect(rs.getString("is_correct"));
                    postList.add(gp);
                }
                GPGroup.setDataList(postList);
                al.add(GPGroup);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return al;
    }

    private int getSPCSuffix(String deptCode, String postCode, String offCode) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        int maxId = 0;
        try {
            con = dataSource.getConnection();
            ps = con.prepareStatement("SELECT "
                    + " COALESCE(MAX(CAST(REPLACE(new_spc, CONCAT(off_code, new_gpc), '') AS INTEGER)), 0)+1 AS max_id FROM"
                    + " g_spc WHERE new_gpc = ? AND dept_code = ? AND off_code = ?");
            ps.setString(1, postCode);
            ps.setString(2, deptCode);
            ps.setString(3, offCode);
            rs = ps.executeQuery();
            while (rs.next()) {
                maxId = rs.getInt("max_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return maxId;
    }

    @Override
    public void setPostAsAuthority(String postcode, String status) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            con = this.dataSource.getConnection();

            String sql = "update g_post set isauthority=? where post_code=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, status);
            pst.setString(2, postcode);
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public ArrayList getForeignBodyPostList(String departmentCode) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        ArrayList postList = new ArrayList();
        GPost gp = new GPost();
        try {
            con = this.dataSource.getConnection();

            String sql = "select * from g_post where department_code=? and is_foreign_body='SGO'";
            pst = con.prepareStatement(sql);
            pst.setString(1, departmentCode);
            rs = pst.executeQuery();
            while (rs.next()) {
                gp = new GPost();
                gp.setPostcode(rs.getString("POST_CODE"));
                gp.setPost(rs.getString("POST"));
                postList.add(gp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return postList;
    }

    @Override
    public void checkDuplicatePost(GPost post) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        try {

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    @Override
    public GPost getPostDetailsforMerging(String dupPost, String finPost) {
        Connection con = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        GPost gpost = null;
        try {
            con = this.dataSource.getConnection();

            ps = con.prepareStatement("SELECT * FROM G_POST WHERE POST_CODE=?");
            ps.setString(1, dupPost);
            rs = ps.executeQuery();
            ps1 = con.prepareStatement("SELECT * FROM G_POST WHERE POST_CODE=?");
            ps1.setString(1, finPost);
            rs1 = ps1.executeQuery();
            if (rs.next() && rs1.next()) {
                gpost = new GPost();
                gpost.setDupVacation(rs.getString("Vacation"));
                gpost.setDupIsAuthority(rs.getString("Isauthority"));
                gpost.setPostgrpType(rs.getString("post_group_type"));
                gpost.setFinVacation(rs1.getString("Vacation"));
                gpost.setFinIsAuthority(rs1.getString("Isauthority"));
                gpost.setFinPostGrp(rs1.getString("post_group_type"));
                gpost.setHiddeptCode(rs1.getString("department_code"));
            }

            if (gpost.getDupVacation() != null && !gpost.getDupVacation().equals("") && gpost.getDupVacation().equals("G")) {
                gpost.setFinVacation(gpost.getDupVacation());
            } else if (gpost.getFinVacation() != null && !gpost.getFinVacation().equals("") && gpost.getFinVacation().equals("G")) {
                gpost.setFinVacation(gpost.getFinVacation());
            }
            if (gpost.getDupIsAuthority() != null && !gpost.getDupIsAuthority().equals("") && gpost.getDupIsAuthority().equals("Y")) {
                gpost.setFinIsAuthority(gpost.getDupIsAuthority());
            } else if (gpost.getFinIsAuthority() != null && !gpost.getFinIsAuthority().equals("") && gpost.getFinIsAuthority().equals("Y")) {
                gpost.setFinIsAuthority(gpost.getFinIsAuthority());
            }
            if ((gpost.getFinPostGrp() != null && !gpost.getFinPostGrp().equals("")) && (gpost.getDupPostGrp() == null || gpost.getDupPostGrp().equals(""))) {
                gpost.setFinPostGrp(gpost.getFinPostGrp());

                ps = con.prepareStatement("Update g_post set post_group_type=? where post_code=?");
                ps.setString(1, gpost.getFinPostGrp());
                ps.setString(2, finPost);
                ps.executeUpdate();
            }

            ps = con.prepareStatement("Update g_post set isauthority=? where post_code=?");
            ps.setString(1, gpost.getFinIsAuthority());
            ps.setString(2, finPost);
            ps.executeUpdate();
            ps = con.prepareStatement("Update g_post set if_merged='Y',new_post_code=? where post_code=?");
            ps.setString(1, finPost);
            ps.setString(2, dupPost);
            ps.executeUpdate();
            ps = con.prepareStatement("Update g_post set if_merged='V',new_post_code=? where post_code=?");
            ps.setString(1, finPost);
            ps.setString(2, finPost);
            ps.executeUpdate();
            ps = con.prepareStatement("SELECT SUBSTR(SPC,14,6)GPC FROM G_SPC WHERE SUBSTR(SPC,14,6)=?");
            ps.setString(1, dupPost);
            rs = ps.executeQuery();
            while (rs.next()) {
                ps = con.prepareStatement("UPDATE G_SPC SET OLD_GPC=?,IF_MERGED='Y',GPC=? WHERE SUBSTR(SPC,14,6)=?");
                ps.setString(1, dupPost);
                ps.setString(2, finPost);
                ps.setString(3, dupPost);
                ps.executeUpdate();
            }
            ps = con.prepareStatement("SELECT SUBSTR(SPC,14,6)GPC FROM G_SPC WHERE SUBSTR(SPC,14,6)=?");
            ps.setString(1, finPost);
            rs = ps.executeQuery();
            while (rs.next()) {
                ps = con.prepareStatement("UPDATE G_SPC SET OLD_GPC=?,IF_MERGED='V' WHERE SUBSTR(SPC,14,6)=?");
                ps.setString(1, finPost);
                ps.setString(2, finPost);
                ps.executeUpdate();
            }
            //genpostname updated in duplicate post code
            ps = con.prepareStatement("SELECT SUBSTR(CUR_SPC,14,6) CUR_GPC,COUNT(*)NOOFEMP FROM EMP_MAST WHERE SUBSTR(CUR_SPC,14,6)=? GROUP BY CUR_GPC");
            ps.setString(1, dupPost);
            rs = ps.executeQuery();
            if (rs.next()) {
                int cntEmp = 0;
                cntEmp = rs.getInt("NOOFEMP");
                //System.out.println(rs.getString("CUR_GPC") + "->DupEmp:" + cntEmp);
                if (cntEmp > 0) {
                    ps = con.prepareStatement("update emp_mast set gen_post_name=? WHERE SUBSTR(CUR_SPC,14,6)=?");
                    ps.setString(1, finPost);
                    ps.setString(2, dupPost);
                    ps.executeUpdate();
                }
            }
            //genpostname updated in final post code
            ps = con.prepareStatement("SELECT SUBSTR(CUR_SPC,14,6) CUR_GPC,COUNT(*)NOOFEMP FROM EMP_MAST WHERE SUBSTR(CUR_SPC,14,6)=? GROUP BY CUR_GPC");
            ps.setString(1, finPost);
            rs = ps.executeQuery();
            if (rs.next()) {
                int cntEmp = 0;
                cntEmp = rs.getInt("NOOFEMP");
                //System.out.println(rs.getString("CUR_GPC") + "->FinalEmp:" + cntEmp);
                if (cntEmp > 0) {
                    ps = con.prepareStatement("update emp_mast set gen_post_name=? WHERE SUBSTR(CUR_SPC,14,6)=?");
                    ps.setString(1, finPost);
                    ps.setString(2, finPost);
                    ps.executeUpdate();
                }
            }
            //gpost.setMergingMsg("Both the Post are Successfully Merged");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(rs1, ps1);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return gpost;
    }

    @Override
    public ArrayList getTotalSPC(String dupPost, String finPost) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        ArrayList totalPostList = new ArrayList();;
        GPost gpost = null;
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("select distinct(substring(spn,1,position(',' in spn)-1))GenericPostName, count(*)totPost  from g_spc where gpc in (?,?) and ifuclean='N' AND "
                    + "is_sanctioned='Y' \n"
                    + " group by (substring(spn,1,position(',' in spn)-1)) order by GenericPostName");
            ps.setString(1, dupPost);
            ps.setString(2, finPost);
            rs = ps.executeQuery();
            while (rs.next()) {
                gpost = new GPost();
                gpost.setPost(rs.getString("GenericPostName"));
                gpost.setTotSpc(rs.getInt("totPost"));
                gpost.setDupPostCode(dupPost);
                gpost.setFinPostCode(finPost);
                totalPostList.add(gpost);

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return totalPostList;
    }

    @Override
    public void getUpdateAndReplaceSPN(GPost post, String finalPostName, String postList) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            con = this.dataSource.getConnection();
            String str = postList;
            String[] chkPostValue = str.split(",");
            for (String chkPostName : chkPostValue) {
                ps = con.prepareStatement("UPDATE G_SPC SET \n"
                        + "SPN=CONCAT(?,SUBSTRING(SPN,LENGTH(SUBSTRING(SPN,1,POSITION(',' IN SPN)-1))+1,LENGTH(SPN)-LENGTH(SUBSTRING(SPN,1,POSITION(',' IN SPN)-1))))\n"
                        + "WHERE GPC IN (?,?) AND  IFUCLEAN='N' AND IS_SANCTIONED='Y' AND SUBSTRING(SPN,1,POSITION(',' IN SPN)-1)=? ");
                ps.setString(1, finalPostName);
                ps.setString(2, post.getHidDupPostCode());
                ps.setString(3, post.getHidFinPostCode());
                ps.setString(4, chkPostName);
                ps.executeUpdate();

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public ArrayList viewPreviousMergedPost(String finPost) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        ArrayList ar = new ArrayList();
        GPost gpost = null;
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("SELECT POST_CODE,POST FROM G_POST WHERE NEW_POST_CODE=? AND POST_CODE NOT IN (?)");
            ps.setString(1, finPost);
            ps.setString(2, finPost);
            rs = ps.executeQuery();
            while (rs.next()) {
                gpost = new GPost();
                gpost.setPostCodes(rs.getString("POST_CODE"));
                gpost.setPost(rs.getString("POST"));
                ar.add(gpost);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return ar;
    }

}
