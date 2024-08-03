/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.dao.master;

import hrms.common.DataBaseFunctions;
import hrms.model.master.FacultyBean;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 *
 * @author Surendra
 */
public class FacultyMasterDAOImpl implements FacultyMasterDAO {
    
    /**
     *
     */
    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

  

    @Override
    public ArrayList getFacultyList() {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement st = null;
        ArrayList facultyList = new ArrayList();
        FacultyBean facultyBean = null;
        try {
            con = dataSource.getConnection();
            st = con.prepareStatement("SELECT * FROM g_faculty ");
            rs = st.executeQuery();
            while (rs.next()) {
                facultyBean = new FacultyBean();
                facultyBean.setFacultyType(rs.getString("faculty_type"));
                facultyBean.setFacultySlno(rs.getInt("faculty_id"));
                facultyList.add(facultyBean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return facultyList;
    }
    
    public FacultyBean editQualification(int slno) {

        FacultyBean facultyBean = new FacultyBean();
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = this.dataSource.getConnection();

            String sql = "select * from g_faculty where faculty_id=?";

            pst = con.prepareStatement(sql);
            pst.setInt(1, slno);
            //pst.setString(1, university.getBoardCode());
            rs = pst.executeQuery();
            if (rs.next()) {

                facultyBean.setFacultySlno(rs.getInt("faculty_id"));
                facultyBean.setFacultyType(rs.getString("faculty_type"));

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return facultyBean;
    }
    
    public void saveUserDetails(FacultyBean facultyBean) {
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
             con = this.dataSource.getConnection();

            String sql = "select COUNT(*) from g_faculty where faculty_type=? HAVING COUNT(*) >= 1";
            ps = con.prepareStatement(sql);
            ps.setString(1, facultyBean.getFacultyType());
             rs = ps.executeQuery();
            if (rs.next()== true) {
                System.out.println("Duplicate Entry not allowed");
            }else{
            ps = con.prepareStatement("INSERT INTO g_faculty (faculty_type) VALUES (?)");
            ps.setString(1, facultyBean.getFacultyType().toUpperCase());
            ps.executeUpdate();
            }
            
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps, con);
        }

    }

    @Override
    public Object editFaculty(int facultySlno) {
       FacultyBean facultyBean = new FacultyBean();
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = this.dataSource.getConnection();

            String sql = "select * from g_faculty where faculty_id=?";

            pst = con.prepareStatement(sql);
            pst.setInt(1, facultySlno);
            //pst.setString(1, university.getBoardCode());
            rs = pst.executeQuery();
            if (rs.next()) {

                facultyBean.setFacultySlno(rs.getInt("faculty_id"));
                facultyBean.setFacultyType(rs.getString("faculty_type"));

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return facultyBean;
    }
    
    

    @Override
    public void updateFacultyDetails(FacultyBean facultyBean) {
      Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            con = this.dataSource.getConnection();
            System.out.println("facultyBean.getFacultySlno() "+facultyBean.getFacultyserialNumber());
            ps = con.prepareStatement("UPDATE g_faculty SET faculty_type=? WHERE faculty_id=?");
            ps.setString(1, facultyBean.getFacultyType().toUpperCase());
            ps.setInt(2, facultyBean.getFacultySlno());
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps, con);
        }}
    
    @Override
    public void deleteFaculty(int slno) {
        System.out.println("slno");
        ResultSet rs = null;
        boolean flag = false;
        PreparedStatement pst = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("delete from g_faculty where faculty_id=?");
            pst.setInt(1, slno);
            pst.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

}
