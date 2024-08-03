/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.dao.master;

import hrms.common.DataBaseFunctions;
import hrms.model.master.DegreeBean;
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
public class DegreeDAOImpl implements DegreeDAO {
    
        @Resource(name = "dataSource")
    protected DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    @Override
    public ArrayList getDegreeList() {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement st = null;
        ArrayList degreeList = new ArrayList();
        DegreeBean degreeBean = null;
        try {
            con = dataSource.getConnection();
            st = con.prepareStatement("SELECT degree_sl,degree FROM G_DEGREE ORDER BY degree");
            rs = st.executeQuery();
            while (rs.next()) {
                degreeBean = new DegreeBean();
                degreeBean.setDegreesl(rs.getInt("degree_sl"));
                degreeBean.setDegreename(rs.getString("degree"));
                degreeList.add(degreeBean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return degreeList;
    }
    
    
     public void saveUserDetails(DegreeBean degreeBean) {
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
             con = this.dataSource.getConnection();

            String sql = "select COUNT(*) from G_DEGREE where degree=? HAVING COUNT(*) >= 1";
            ps = con.prepareStatement(sql);
            ps.setString(1, degreeBean.getDegreename());
             rs = ps.executeQuery();
            if (rs.next()== true) {
                System.out.println("Duplicate Entry not allowed");
            }else{
            ps = con.prepareStatement("INSERT INTO G_DEGREE (degree) VALUES (?)");
            ps.setString(1, degreeBean.getDegreename().toUpperCase());
            ps.executeUpdate();
            }
            
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps, con);
        }

    }
     
     @Override
    public DegreeBean editDegree(int degreeSlno) {
       DegreeBean degreeBean = new DegreeBean();
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = this.dataSource.getConnection();

            pst =  con.prepareStatement("select * from G_DEGREE where degree_sl=?");

          
            pst.setInt(1, degreeSlno);
            //pst.setString(1, university.getBoardCode());
            
            rs = pst.executeQuery();
            while (rs.next()) {

                degreeBean.setDegreesl(rs.getInt("degree_sl"));
                degreeBean.setDegreename(rs.getString("degree"));

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return degreeBean;
    }
    
   

    @Override
    public void updateDegreeDetails(DegreeBean degreeBean) {
      Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("UPDATE G_DEGREE SET degree=? WHERE degree_sl=?");
            ps.setString(1, degreeBean.getDegreename().toUpperCase());
            ps.setInt(2, degreeBean.getDegreesl());
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps, con);
        }}
    
    @Override
    public void deleteDegree(int slno) {
        ResultSet rs = null;
        boolean flag = false;
        PreparedStatement pst = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("delete from G_DEGREE where degree_sl=?");
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
