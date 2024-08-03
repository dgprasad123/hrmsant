/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.master;

import hrms.SelectOption;
import hrms.common.DataBaseFunctions;
import hrms.model.master.Grade;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 *
 * @author BIBHUTI
 */
public class GradeDAOImpl implements GradeDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

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
    public List getGradeList(String cadreCode) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;
        Grade gd = null;

        List gradelist = new ArrayList();
        SelectOption so = null;
        try {
            con = dataSource.getConnection();

            pst = con.prepareStatement("select * from G_CADRE_GRADE WHERE CADRE_CODE=?");
            pst.setString(1, cadreCode);
            //System.out.println("cadre code is" + cadreCode);
            rs = pst.executeQuery();
            while (rs.next()) {
                gd = new Grade();
                gd.setGrade(rs.getString("grade"));
                gd.setGradeLevel(rs.getInt("grade_level"));
                gd.setSanction(rs.getInt("no_of_sanction_post"));
                gd.setCadreGradeCode(rs.getInt("cadre_grade_code"));
                gradelist.add(gd);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return gradelist;
    }

    public Grade getGradeData(int cadreGradeCode) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;
        Grade grade = new Grade();
        try {
            con = dataSource.getConnection();

            pst = con.prepareStatement("SELECT * from G_CADRE_GRADE where cadre_grade_code=?");
            pst.setInt(1, cadreGradeCode);
            rs = pst.executeQuery();
            if (rs.next()) {
                grade.setDeptCode(rs.getString("department_code"));
                grade.setCadreCode(rs.getString("cadre_code"));
                grade.setGrade(rs.getString("grade"));
                grade.setSanction(rs.getInt("no_of_sanction_post"));
                grade.setGradeLevel(rs.getInt("grade_level"));
                grade.setIs_visible(rs.getString("is_visible"));
                grade.setIs_obsolate(rs.getString("is_obsolate"));
                grade.setCadreGradeCode(cadreGradeCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return grade;
    }

    public void saveGrade(Grade grade) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();

            pst = con.prepareStatement("INSERT INTO G_CADRE_GRADE(department_code,cadre_code,grade,grade_level,no_of_sanction_post,is_visible,is_obsolate) VALUES(?,?,?,?,?,?,?)");
            pst.setString(1, grade.getDeptCode());
            pst.setString(2, grade.getCadreCode());
            pst.setString(3, grade.getGrade());
            pst.setInt(4, grade.getGradeLevel());
            pst.setInt(5, grade.getSanction());
            pst.setString(6, grade.getIs_visible());
            pst.setString(7, grade.getIs_obsolate());
            pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void updateGrade(Grade  grade) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("UPDATE G_CADRE_GRADE SET grade=?,grade_level=?,no_of_sanction_post=?,is_visible=?,is_obsolate=? where cadre_grade_code=?");

            
            pst.setString(1, grade.getGrade());
            pst.setInt(2, grade.getGradeLevel());
            pst.setInt(3, grade.getSanction());
            pst.setString(4, grade.getIs_visible());
            pst.setString(5, grade.getIs_obsolate());
            pst.setInt(6, grade.getCadreGradeCode());
            //System.out.println("grade.getCadreGradeCode()"+grade.getCadreGradeCode());
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }
    @Override
    public String getCadreName(String cadreCode) {
        Connection con = null;

        Statement stmt = null;
        ResultSet rs = null;

        String cadreName = "";
        try {
            con = dataSource.getConnection();
            String sql = "SELECT cadre_name from g_cadre where cadre_code='" + cadreCode + "'";
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                cadreName = rs.getString("cadre_name");

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return cadreName;
    }

    @Override
    public String getGradeName(String grade) {
        Connection con = null;

        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            con = dataSource.getConnection();
            String sql = "SELECT grade from G_CADRE_GRADE where grade='" + grade + "'";
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                grade = rs.getString("grade");

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return grade;
    }
}
