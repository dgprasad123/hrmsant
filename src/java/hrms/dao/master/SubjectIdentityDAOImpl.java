/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.master;

import hrms.common.DataBaseFunctions;
import hrms.model.master.SubjectIdentityBean;
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
public class SubjectIdentityDAOImpl implements SubjectIdentityDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     *
     * @return
     */
    @Override
    public ArrayList getsubjectIdentityList() {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement st = null;
        ArrayList degreeList = new ArrayList();
        SubjectIdentityBean subjectIdentityBean = null;
        try {
            con = dataSource.getConnection();
            st = con.prepareStatement("SELECT id_subject,subject_name FROM G_SUBJECT ORDER BY SUBJECT_NAME");
            rs = st.executeQuery();
            while (rs.next()) {
                subjectIdentityBean = new SubjectIdentityBean();
                subjectIdentityBean.setSubjectsl(rs.getInt("id_subject"));
                subjectIdentityBean.setSubjectname(rs.getString("subject_name"));
                degreeList.add(subjectIdentityBean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return degreeList;
    }

    @Override
    public void saveSubjectDetails(SubjectIdentityBean subjectIdentityBean) {
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            con = this.dataSource.getConnection();
            
            String sql = "select COUNT(*) from G_SUBJECT where subject_name=? HAVING COUNT(*) >= 1";
            ps = con.prepareStatement(sql);
            ps.setString(1, subjectIdentityBean.getSubjectname());
            rs = ps.executeQuery();
            if (rs.next() == true) {
                System.out.println("Duplicate Entry not allowed");
            } else {
                ps = con.prepareStatement("INSERT INTO G_SUBJECT (subject_name) VALUES (?)");
                ps.setString(1, subjectIdentityBean.getSubjectname().toUpperCase());
                ps.executeUpdate();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps, con);
        }
    }
    
     @Override
    public SubjectIdentityBean editSubject(int subjectSlno) {
       SubjectIdentityBean subjectIdentityBean = new SubjectIdentityBean();
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = this.dataSource.getConnection();

            pst =  con.prepareStatement("select * from G_SUBJECT where id_subject=?");

          
            pst.setInt(1, subjectSlno);
            //pst.setString(1, university.getBoardCode());
            
            rs = pst.executeQuery();
            while (rs.next()) {

                subjectIdentityBean.setSubjectsl(rs.getInt("id_subject"));
                subjectIdentityBean.setSubjectname(rs.getString("subject_name"));

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return subjectIdentityBean;
    }
    
     @Override
    public void updateSubjectDetails(SubjectIdentityBean subjectIdentityBean) {
      Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("UPDATE G_SUBJECT SET subject_name=? WHERE id_subject=?");
            ps.setString(1, subjectIdentityBean.getSubjectname().toUpperCase());
            ps.setInt(2, subjectIdentityBean.getSubjectsl());
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
            pst = con.prepareStatement("delete from G_SUBJECT where id_subject=?");
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
