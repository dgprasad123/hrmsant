/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.master;

import hrms.common.DataBaseFunctions;
import hrms.model.master.QualificationBean;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

/**
 *
 * @author Surendra
 */
public class QualificationMasterDAOImpl implements QualificationMasterDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public ArrayList getQualificationList() {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement st = null;
        ArrayList qualificationList = new ArrayList();
        QualificationBean qualificationBean = null;
        try {
            con = dataSource.getConnection();
            st = con.prepareStatement("SELECT * FROM g_qualification ");
            rs = st.executeQuery();
            while (rs.next()) {
                qualificationBean = new QualificationBean();
                qualificationBean.setQualification(rs.getString("qualification"));
                qualificationBean.setQualification_sl_no(rs.getInt("qualification_sl_no"));
                qualificationList.add(qualificationBean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return qualificationList;
    }

    @Override
    public QualificationBean editQualification(int slno) {

        QualificationBean qualificationBean = new QualificationBean();
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = this.dataSource.getConnection();

            String sql = "select * from g_qualification where qualification_sl_no=?";

            pst = con.prepareStatement(sql);
            pst.setInt(1, slno);
            //pst.setString(1, university.getBoardCode());
            rs = pst.executeQuery();
            if (rs.next()) {

                qualificationBean.setQualificationserialNumber(rs.getInt("qualification_sl_no"));
                qualificationBean.setQualificationName(rs.getString("qualification"));

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return qualificationBean;
    }

    public void saveUserDetails(QualificationBean qualificationBean) {
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
             con = this.dataSource.getConnection();

            String sql = "select COUNT(*) from g_qualification where qualification=? HAVING COUNT(*) >= 1";
            ps = con.prepareStatement(sql);
            ps.setString(1, qualificationBean.getQualificationName());
             rs = ps.executeQuery();
            if (rs.next()== true) {
                System.out.println("Duplicate Entry not allowed");
            }else{
            ps = con.prepareStatement("INSERT INTO g_qualification (qualification) VALUES (?)");
            ps.setString(1, qualificationBean.getQualificationName().toUpperCase());
            ps.executeUpdate();
            }
            
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps, con);
        }

    }

    public void updateUserDetails(QualificationBean qualificationBean) {
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            con = this.dataSource.getConnection();

            ps = con.prepareStatement("UPDATE g_qualification SET qualification=? WHERE qualification_sl_no=?");
            ps.setString(1, qualificationBean.getQualificationName().toUpperCase());
            ps.setInt(2, qualificationBean.getQualificationserialNumber());
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps, con);
        }

    }

    @Override
    public void deleteQualification(int slno) {
        ResultSet rs = null;
        boolean flag = false;
        PreparedStatement pst = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("delete from g_qualification where qualification_sl_no=?");
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
