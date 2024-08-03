package hrms.dao.master;

import hrms.SelectOption;
import hrms.common.DataBaseFunctions;
import hrms.model.cadre.Cadre;
//import hrms.model.master.Cadre;
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
import org.apache.commons.lang.builder.ToStringBuilder;

public class CadreDAOImpl implements CadreDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    protected MaxCadreIdDAOImpl maxCadreIdDAO;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setMaxCadreIdDAO(MaxCadreIdDAOImpl maxCadreIdDAO) {
        this.maxCadreIdDAO = maxCadreIdDAO;
    }

    @Override
    public List getCadreList(String deptcode) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List cadrelist = new ArrayList();
        SelectOption so = null;
        try {
            con = dataSource.getConnection();

            pst = con.prepareStatement("SELECT cadre_code,cadre_name from g_cadre where department_code=? order by cadre_name asc");
            pst.setString(1, deptcode);
            rs = pst.executeQuery();
            while (rs.next()) {
                so = new SelectOption();
                so.setValue(rs.getString("cadre_code"));
                so.setLabel(rs.getString("cadre_name"));
                cadrelist.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return cadrelist;
    }

    public List getCadreListfOrPAR(String deptcode) {
        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List cadrelist = new ArrayList();
        Cadre cadre = null;
        try {
            con = dataSource.getConnection();

            pst = con.prepareStatement("SELECT cadre_code,cadre_name from g_cadre where department_code=? order by cadre_name asc");
            pst.setString(1, deptcode);
            rs = pst.executeQuery();
            while (rs.next()) {
                cadre = new Cadre();
                cadre.setCadreCode(rs.getString("cadre_code"));
                cadre.setCadreName(rs.getString("cadre_name"));
                cadrelist.add(cadre);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return cadrelist;
    }

    @Override
    public void saveCadreData(Cadre cadreform) {

        Connection con = null;

        PreparedStatement pst = null;

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        try {
            con = this.dataSource.getConnection();

            //String maxcadid = maxCadreIdDAO.getMaxCadreId();
            pst = con.prepareStatement("INSERT INTO EMP_CADRE(not_type, not_id, emp_id, wefd, weft, dept_code, cadre_code, grade, post_code, post_class, post_stat, joined_assuch, "
                    + "lvl, DESCRIPTION, IF_PROFORMA, IF_JOINED,allot_year,cadreid,POSTING_DEPT_CODE,cur_cadre_grade_code)  VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            //pst.setString(1, maxcadid);
            pst.setString(1, cadreform.getNotType());
            pst.setInt(2, cadreform.getNotId());
            pst.setString(3, cadreform.getEmpId());
            if (cadreform.getCadreJoiningWEFDt() != null && !cadreform.getCadreJoiningWEFDt().equals("")) {
                pst.setTimestamp(4, new Timestamp(sdf.parse(cadreform.getCadreJoiningWEFDt()).getTime()));
            } else {
                pst.setTimestamp(4, null);
            }
            pst.setString(5, cadreform.getCadreJoiningWEFTime());
            pst.setString(6, cadreform.getCadreDept());
            pst.setString(7, cadreform.getCadreName());
            if (cadreform.getGrade() != null && !cadreform.getGrade().equals("")) {
                pst.setString(8, cadreform.getGradeCodeName());
            } else {
                pst.setString(8, null);
            }

            pst.setString(9, cadreform.getPostCode());
            pst.setString(10, cadreform.getPostClassification());
            pst.setString(11, cadreform.getPostStatus());
            pst.setString(12, cadreform.getJoinedAsSuch());
            pst.setString(13, cadreform.getCadreLvl());
            pst.setString(14, cadreform.getDescription());
            pst.setString(15, cadreform.getIfProforma());
            pst.setString(16, "");
            pst.setString(17, cadreform.getAllotmentYear());
            pst.setString(18, cadreform.getCadreId());
            pst.setString(19, cadreform.getPostingDept());
            if (cadreform.getGrade() != null && !cadreform.getGrade().equals("")) {
                pst.setInt(20, Integer.parseInt(cadreform.getGrade()));
            } else {
                pst.setInt(20, 0);
            }

            pst.executeUpdate();

            ResultSet rs = pst.getGeneratedKeys();
            rs.next();
            int cadid = rs.getInt("cad_id");

            DataBaseFunctions.closeSqlObjects(rs, pst);

            String sqlString = ToStringBuilder.reflectionToString(pst);
            pst = con.prepareStatement("UPDATE EMP_CADRE SET query_string=? WHERE cad_id=? AND EMP_ID=?");
            pst.setString(1, sqlString);
            pst.setInt(2, cadid);
            pst.setString(3, cadreform.getEmpId());
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void updateCadreData(Cadre cadreform) {

        Connection con = null;

        PreparedStatement pst = null;

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        try {
            if (cadreform.getCadreCode() != null && !cadreform.getCadreCode().equals("")) {

                con = this.dataSource.getConnection();

                pst = con.prepareStatement("UPDATE EMP_CADRE SET wefd=?, weft=?, dept_code=?, cadre_code=?, grade=?, post_code=?, post_class=?, post_stat=?, joined_assuch=?, lvl=?, DESCRIPTION=?, IF_PROFORMA=?, IF_JOINED=?,allot_year=?,"
                        + "cadreid=?,POSTING_DEPT_CODE=?,cur_cadre_grade_code=? WHERE cad_id=?");
                if (cadreform.getCadreJoiningWEFDt() != null && !cadreform.getCadreJoiningWEFDt().equals("")) {
                    pst.setTimestamp(1, new Timestamp(sdf.parse(cadreform.getCadreJoiningWEFDt()).getTime()));
                } else {
                    pst.setTimestamp(1, null);
                }
                pst.setString(2, cadreform.getCadreJoiningWEFTime());
                pst.setString(3, cadreform.getCadreDept());
                pst.setString(4, cadreform.getCadreName());
                if (cadreform.getGrade() != null && !cadreform.getGrade().equals("")) {
                    pst.setString(5, cadreform.getGradeCodeName());
                } else {
                    pst.setString(5, null);
                }

                pst.setString(6, cadreform.getPostCode());
                pst.setString(7, cadreform.getPostClassification());
                pst.setString(8, cadreform.getPostStatus());
                pst.setString(9, cadreform.getJoinedAsSuch());
                pst.setString(10, cadreform.getCadreLvl());
                pst.setString(11, cadreform.getDescription());
                pst.setString(12, cadreform.getIfProforma());
                pst.setString(13, "");
                pst.setString(14, cadreform.getAllotmentYear());
                pst.setString(15, cadreform.getCadreId());
                pst.setString(16, cadreform.getPostingDept());
                if (cadreform.getGrade() != null && !cadreform.getGrade().equals("")) {
                    pst.setInt(17, Integer.parseInt(cadreform.getGrade()));
                } else {
                    pst.setInt(17, 0);
                }

                pst.setInt(18, Integer.parseInt(cadreform.getCadreCode()));
                pst.executeUpdate();

                String sqlString = ToStringBuilder.reflectionToString(pst);
                pst = con.prepareStatement("UPDATE EMP_CADRE SET query_string=? WHERE cad_id=? AND EMP_ID=?");
                pst.setString(1, sqlString);
                pst.setInt(2, Integer.parseInt(cadreform.getCadreCode()));
                pst.setString(3, cadreform.getEmpId());
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
    public void deleteCadreData(Cadre cadreform) {

        Connection con = null;

        PreparedStatement pst = null;

        try {
            if (cadreform.getCadreCode() != null && !cadreform.getCadreCode().equals("")) {
                con = this.dataSource.getConnection();

                pst = con.prepareStatement("DELETE FROM EMP_CADRE WHERE cad_id=?");
                pst.setInt(1, Integer.parseInt(cadreform.getCadreCode()));
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
    public List getGradeList(String cadreCode) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List gradelist = new ArrayList();
        SelectOption so = null;
        try {
            con = dataSource.getConnection();

            pst = con.prepareStatement("select cadre_grade_code,GRADE from G_CADRE_GRADE WHERE CADRE_CODE=? order by grade");
            pst.setString(1, cadreCode);
            rs = pst.executeQuery();
            while (rs.next()) {
                so = new SelectOption();
                so.setValue(rs.getString("cadre_grade_code"));
                so.setLabel(rs.getString("GRADE"));
                gradelist.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return gradelist;
    }

    @Override
    public List getCadreLevelList() {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List levellist = new ArrayList();
        SelectOption so = null;
        try {
            con = dataSource.getConnection();

            pst = con.prepareStatement("select * from G_CADRE_LEVEL order by LVL");
            rs = pst.executeQuery();
            while (rs.next()) {
                so = new SelectOption();
                so.setValue(rs.getString("lvl"));
                so.setLabel(rs.getString("lvl"));
                levellist.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return levellist;
    }

    @Override
    public List getDescription(String cdrLevel) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List desclist = new ArrayList();
        SelectOption so = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("select * from G_LEVEL_DESC WHERE lvl=?");
            pst.setString(1, cdrLevel);
            rs = pst.executeQuery();
            while (rs.next()) {
                so = new SelectOption();
                so.setValue(rs.getString("DESC_A"));
                so.setLabel(rs.getString("DESC_A"));
                desclist.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return desclist;
    }

    @Override
    public String getCadreName(String cadrecode) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String cadrename = "";
        try {
            con = this.dataSource.getConnection();

            String sql = "select cadre_name from g_cadre where cadre_code=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, cadrecode);
            rs = pst.executeQuery();
            if (rs.next()) {
                cadrename = rs.getString("cadre_name");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return cadrename;
    }

    @Override
    public Cadre editCadre(Cadre cadre) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = this.dataSource.getConnection();

            String sql = "select cadre_code,cadre_name,department_code from g_cadre where cadre_code=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, cadre.getCadreCode());
            rs = pst.executeQuery();
            if (rs.next()) {
                cadre.setHidcadreCode(rs.getString("cadre_code"));
                cadre.setCadreCode(rs.getString("cadre_code"));
                cadre.setCadreName(rs.getString("cadre_name"));
                cadre.setDeptCode(rs.getString("department_code"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return cadre;
    }

    @Override
    public List getEmployeeList(String cadreCode) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        List empCadreWisesList = new ArrayList();

        try {
            con = this.dataSource.getConnection();

            String sql = "Select emp_id,gpf_no,ARRAY_TO_STRING(ARRAY[initials, f_name, m_name,l_name], ' ') fullname , goff.off_en officename ,gspc.spn, gpost.post postname\n"
                    + "from (select * from emp_mast where cadre_code= ?)emp_mast\n"
                    + "left outer join g_office goff on emp_mast.cur_off_code=goff.off_code\n"
                    + "left outer join g_spc gspc on emp_mast.cur_spc=gspc.spc\n"
                    + "left outer join g_post gpost on gspc.gpc=gpost.post_code";
            pst = con.prepareStatement(sql);
            pst.setString(1, cadreCode);
            rs = pst.executeQuery();
            while (rs.next()) {
                Cadre empdata = new Cadre();
                empdata.setHrmsId(rs.getString("emp_id"));
                empdata.setGpfno(rs.getString("gpf_no"));
                empdata.setEmpfullName(rs.getString("fullname"));
                empdata.setOfficeName(rs.getString("officename"));
                empdata.setDesignation(rs.getString("postname"));
                empCadreWisesList.add(empdata);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return empCadreWisesList;
    }

    @Override
    public void saveNewCadre(Cadre cadre) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        String maxcadreCodeStr = "";
        try {
            con = dataSource.getConnection();
            int maxcadreCode = 0;

            pst = con.prepareStatement("SELECT MAX( cadre_code ) ::INTEGER +1 maxcadrecode FROM g_cadre where department_code=? ");
            pst.setString(1, cadre.getHiddeptCode());
            rs = pst.executeQuery();
            if (rs.next()) {
                maxcadreCode = rs.getInt("maxcadrecode");
                int length = String.valueOf(maxcadreCode).length();
                if (length < 4) {
                    maxcadreCodeStr = "0" + maxcadreCode;
                } else {
                    maxcadreCodeStr = rs.getString("maxcadrecode");
                }
            }
            if (maxcadreCodeStr != null) {
                pst = con.prepareStatement("INSERT INTO g_cadre(cadre_code,cadre_name, department_code) VALUES(?, ?,?)");
                pst.setString(1, maxcadreCodeStr);
                pst.setString(2, cadre.getCadreName().toUpperCase());
                pst.setString(3, cadre.getHiddeptCode());
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
    public void updateNewCadre(Cadre cadre) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("UPDATE g_cadre SET cadre_name=? where cadre_code=?");

            pst.setString(1, cadre.getCadreName().toUpperCase());
            pst.setString(2, cadre.getCadreCode());
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public List getGradeListDetails(String cadreCode) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection con = null;
        List gradelistDetails = new ArrayList();
        Cadre cadre = null;
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("select GRADE,CADRE_GRADE_CODE from G_CADRE_GRADE WHERE CADRE_CODE=?");
            ps.setString(1, cadreCode);
            rs = ps.executeQuery();
            while (rs.next()) {
                cadre = new Cadre();
                cadre.setGrade(rs.getString("GRADE"));
                cadre.setGradeCode(rs.getInt("CADRE_GRADE_CODE"));
                cadre.setGradeCodeName(Integer.toString(rs.getInt("CADRE_GRADE_CODE")).concat(";").concat(rs.getString("GRADE")));
                gradelistDetails.add(cadre);

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return gradelistDetails;

    }

    @Override
    public List getPostListCadreCodeAndDeptCodeWise(String deptCode, String cadreCode) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        List postlist = new ArrayList();
        Cadre cadre = null;
        try {
            con = dataSource.getConnection();

            pst = con.prepareStatement("select * from g_cadre2post "
                    + "inner join g_post on g_cadre2post.post_code = g_post.post_code "
                    + "inner join g_cadre on g_cadre2post.cadre_code = g_cadre.cadre_code "
                    + "inner join g_department on g_cadre.department_code = g_department.department_code "
                    + "where g_cadre.department_code=? and g_cadre2post.cadre_code=?");
            pst.setString(1, deptCode);
            pst.setString(2, cadreCode);
            rs = pst.executeQuery();
            while (rs.next()) {
                cadre = new Cadre();
                cadre.setPostCode(rs.getString("post_code"));
                cadre.setPostGrp(rs.getString("post"));
                postlist.add(cadre);
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
    public void saveCadreDataSBCorrection(Cadre cadreform) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        try {
            con = this.dataSource.getConnection();

            pst = con.prepareStatement("INSERT INTO EMP_CADRE_LOG(not_type, not_id, emp_id, wefd, weft, dept_code, cadre_code, grade, post_code, post_class, post_stat, joined_assuch, "
                    + "lvl, DESCRIPTION, IF_PROFORMA, IF_JOINED,allot_year,cadreid,POSTING_DEPT_CODE,cur_cadre_grade_code,REF_CORRECTION_ID)  VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, cadreform.getNotType());
            pst.setInt(2, cadreform.getNotId());
            pst.setString(3, cadreform.getEmpId());
            if (cadreform.getCadreJoiningWEFDt() != null && !cadreform.getCadreJoiningWEFDt().equals("")) {
                pst.setTimestamp(4, new Timestamp(sdf.parse(cadreform.getCadreJoiningWEFDt()).getTime()));
            } else {
                pst.setTimestamp(4, null);
            }
            pst.setString(5, cadreform.getCadreJoiningWEFTime());
            pst.setString(6, cadreform.getCadreDept());
            pst.setString(7, cadreform.getCadreName());
            if (cadreform.getGrade() != null && !cadreform.getGrade().equals("")) {
                pst.setString(8, cadreform.getGradeCodeName());
            } else {
                pst.setString(8, null);
            }
            pst.setString(9, cadreform.getPostCode());
            pst.setString(10, cadreform.getPostClassification());
            pst.setString(11, cadreform.getPostStatus());
            pst.setString(12, cadreform.getJoinedAsSuch());
            pst.setString(13, cadreform.getCadreLvl());
            pst.setString(14, cadreform.getDescription());
            pst.setString(15, cadreform.getIfProforma());
            pst.setString(16, "");
            pst.setString(17, cadreform.getAllotmentYear());
            pst.setString(18, cadreform.getCadreId());
            pst.setString(19, cadreform.getPostingDept());
            if (cadreform.getGrade() != null && !cadreform.getGrade().equals("")) {
                pst.setInt(20, Integer.parseInt(cadreform.getGrade()));
            } else {
                pst.setInt(20, 0);
            }
            pst.setInt(21, Integer.parseInt(cadreform.getRefcorrectionid()));
            pst.executeUpdate();

            rs = pst.getGeneratedKeys();
            rs.next();
            int cadid = rs.getInt("cad_id");

            DataBaseFunctions.closeSqlObjects(rs, pst);

            String sqlString = ToStringBuilder.reflectionToString(pst);
            pst = con.prepareStatement("UPDATE EMP_CADRE_LOG SET query_string=? WHERE cad_id=? AND EMP_ID=?");
            pst.setString(1, sqlString);
            pst.setInt(2, cadid);
            pst.setString(3, cadreform.getEmpId());
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void updateCadreDataSBCorrection(Cadre cadreform) {

        Connection con = null;

        PreparedStatement pst = null;

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        try {
            if (cadreform.getCadreCode() != null && !cadreform.getCadreCode().equals("")) {

                con = this.dataSource.getConnection();

                pst = con.prepareStatement("UPDATE EMP_CADRE_LOG SET wefd=?, weft=?, dept_code=?, cadre_code=?, grade=?, post_code=?, post_class=?, post_stat=?, joined_assuch=?, lvl=?, DESCRIPTION=?, IF_PROFORMA=?, IF_JOINED=?,allot_year=?,"
                        + "cadreid=?,POSTING_DEPT_CODE=?,cur_cadre_grade_code=? WHERE cad_id=?");
                if (cadreform.getCadreJoiningWEFDt() != null && !cadreform.getCadreJoiningWEFDt().equals("")) {
                    pst.setTimestamp(1, new Timestamp(sdf.parse(cadreform.getCadreJoiningWEFDt()).getTime()));
                } else {
                    pst.setTimestamp(1, null);
                }
                pst.setString(2, cadreform.getCadreJoiningWEFTime());
                pst.setString(3, cadreform.getCadreDept());
                pst.setString(4, cadreform.getCadreName());
                if (cadreform.getGrade() != null && !cadreform.getGrade().equals("")) {
                    pst.setString(5, cadreform.getGradeCodeName());
                } else {
                    pst.setString(5, null);
                }

                pst.setString(6, cadreform.getPostCode());
                pst.setString(7, cadreform.getPostClassification());
                pst.setString(8, cadreform.getPostStatus());
                pst.setString(9, cadreform.getJoinedAsSuch());
                pst.setString(10, cadreform.getCadreLvl());
                pst.setString(11, cadreform.getDescription());
                pst.setString(12, cadreform.getIfProforma());
                pst.setString(13, "");
                pst.setString(14, cadreform.getAllotmentYear());
                pst.setString(15, cadreform.getCadreId());
                pst.setString(16, cadreform.getPostingDept());
                if (cadreform.getGrade() != null && !cadreform.getGrade().equals("")) {
                    pst.setInt(17, Integer.parseInt(cadreform.getGrade()));
                } else {
                    pst.setInt(17, 0);
                }

                pst.setInt(18, Integer.parseInt(cadreform.getCadreCode()));
                pst.executeUpdate();

                String sqlString = ToStringBuilder.reflectionToString(pst);
                pst = con.prepareStatement("UPDATE EMP_CADRE SET query_string=? WHERE cad_id=? AND EMP_ID=?");
                pst.setString(1, sqlString);
                pst.setInt(2, Integer.parseInt(cadreform.getCadreCode()));
                pst.setString(3, cadreform.getEmpId());
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
    public void deleteCadreDataSBCorrection(Cadre cadreform) {

        Connection con = null;

        PreparedStatement pst = null;

        try {
            if (cadreform.getCadreCode() != null && !cadreform.getCadreCode().equals("")) {
                con = this.dataSource.getConnection();

                pst = con.prepareStatement("DELETE FROM EMP_CADRE_LOG WHERE cad_id=?");
                pst.setInt(1, Integer.parseInt(cadreform.getCadreCode()));
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
    public String getCadreGradeName(String gradecode) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String gradeName = "";
        try {
            con = this.dataSource.getConnection();
            if (gradecode != null && !gradecode.equals("")) {
                String sql = "SELECT grade from g_cadre_grade where cadre_grade_code=?";
                pst = con.prepareStatement(sql);
                pst.setInt(1, Integer.parseInt(gradecode));
                rs = pst.executeQuery();
                if (rs.next()) {
                    gradeName = rs.getString("grade");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return gradeName;
    }
}
