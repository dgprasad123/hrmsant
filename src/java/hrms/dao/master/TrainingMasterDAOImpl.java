/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.master;

import hrms.common.DataBaseFunctions;
import hrms.model.master.TrainingBean;
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
 * @author lenovo
 */
public class TrainingMasterDAOImpl implements TrainingMasterDAO {
    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
     @Override
    public ArrayList getTrainingMasterTitle() {
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        TrainingBean training=null;
        ArrayList trainingList = new ArrayList();
        try {
            con = this.dataSource.getConnection();
            stmt = con.createStatement();
            rs = stmt.executeQuery("SELECT a.training_type,b.* FROM g_training_type a, g_training_title b WHERE a.training_type_id=b.training_type ORDER BY training_title");
            while (rs.next()) {
                training = new TrainingBean();
                training.setTrainingType(rs.getString("training_type"));
                training.setTrainingTitle(rs.getString("training_title"));
		 training.setTrainingId(rs.getInt("trainingsl"));
                trainingList.add(training);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, stmt);
            DataBaseFunctions.closeSqlObjects(con);
        }

        return trainingList;

    }
    @Override
    public List getTrainingType() {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List trainingList = new ArrayList();
        TrainingBean training=null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT training_type_id,training_type from g_training_type order by training_type asc");
           
            rs = pstmt.executeQuery();
            while (rs.next()) {
                 training = new TrainingBean();
                training.setTrainingType(rs.getString("training_type"));               
                training.setTrainingTitleId(rs.getString("training_type_id"));
               
                trainingList.add(training);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return trainingList;
    }
    
       @Override
    public void addNewTrainingMaster(TrainingBean training) {
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        String traningType = training.getTrainingType();
        String traningTitle = training.getTrainingTitle();
        try {
            con = this.dataSource.getConnection();
            stmt = con.createStatement();
           
            ps = con.prepareStatement("INSERT INTO g_training_title(training_type,training_title,training_title_id)VALUES(?,?,?)");           
            ps.setString(1, traningType.toUpperCase());
            ps.setString(2, traningTitle.toUpperCase());
             ps.setString(3, "NA");
            ps.executeUpdate();
          
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs,stmt);
            DataBaseFunctions.closeSqlObjects(ps);
            DataBaseFunctions.closeSqlObjects(con);           
            
        }
    }
    public void deleteTrainingMaster(int trainingId) {
        int n = 0;
        PreparedStatement pst = null;
        Connection con = null;
       
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("DELETE FROM g_training_title WHERE  trainingsl=?");          
            pst.setInt(1, trainingId);
          
            n = pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }
    
    @Override
    public TrainingBean editNewTrainingMaster(int trainingId, TrainingBean training) {
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        
       
        try {
            con = this.dataSource.getConnection();
            stmt = con.createStatement();
           
            ps = con.prepareStatement("SELECT * FROM g_training_title WHERE trainingsl=?");    
            //ps.setInt(1, trainingId);
            ps.setInt(1, trainingId);
            
            rs = ps.executeQuery();
            
            if (rs.next()) {
                
                training.setTrainingType(rs.getString("training_type"));
                training.setTrainingTitle(rs.getString("training_title"));     
            }
          
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs,stmt);
            DataBaseFunctions.closeSqlObjects(ps);
            DataBaseFunctions.closeSqlObjects(con);           
            
        }
        return training;
    }
    
     @Override
    public void updateNewTrainingMaster(TrainingBean training) {
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        String traningType = training.getTrainingType();
        String traningTitle = training.getTrainingTitle();
        try {
            con = this.dataSource.getConnection();
            stmt = con.createStatement();
           
            ps = con.prepareStatement("UPDATE g_training_title SET training_type=?,training_title=? WHERE trainingsl=?");           
            ps.setString(1, traningType.toUpperCase());
            ps.setString(2, traningTitle.toUpperCase());           
            ps.setInt(3, Integer.parseInt(training.getHiddentrainingid()));
            
            ps.executeUpdate();
          
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs,stmt);
            DataBaseFunctions.closeSqlObjects(ps);
            DataBaseFunctions.closeSqlObjects(con);           
            
        }
    }
    
}
