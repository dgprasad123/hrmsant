/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.retrenchment;

import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.model.resignation.Resignation;
import hrms.model.retrenchment.Retrenchment;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 *
 * @author lenovo pc
 */
public class RetrenchmentDAOImpl implements RetrenchmentDAO{
     @Resource(name = "dataSource")
    protected DataSource dataSource;
    public DataSource getDataSource() {
        return dataSource;
    }
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
     public int insertRetrenchmentData(Retrenchment retrench){
          int n = 0;
        PreparedStatement pst = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            String mcode = CommonFunctions.getMaxCode("EMP_NOTIFICATION", "NOT_ID", con);
            pst = con.prepareStatement("INSERT INTO EMP_NOTIFICATION(NOT_ID,NOT_TYPE,EMP_ID,DOE,TOE,IF_ASSUMED,ORDNO,ORDDT,DEPT_CODE,OFF_CODE,AUTH,NOTE,IF_VISIBLE,ENT_DEPT,ENT_OFF,ENT_AUTH,ACS,ASCS) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            pst.setString(1, mcode);
            pst.setString(2, "RETRENCHMENT");
            pst.setString(3, retrench.getEmpid());
            pst.setString(4, retrench.getDoe());
            pst.setString(5, retrench.getToe());
            pst.setString(6, retrench.getIfAssumed());
            pst.setString(7, retrench.getOrdno());
            pst.setString(8, retrench.getOrdDate());
            pst.setString(9, retrench.getSltDept());
            pst.setString(10, retrench.getSltOffice());
            pst.setString(11, retrench.getSltAuth());
            pst.setString(12, retrench.getNote());
            pst.setString(13, retrench.getIfVisible());
            pst.setString(14, retrench.getEntDept());
            pst.setString(15, retrench.getEntOffice());
            pst.setString(16, retrench.getEntAuth());
            pst.setString(17, retrench.getCadreStatus());
            pst.setString(18, retrench.getSubStatus());
            n = pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return n;
     }
    
    public int updateRetrenchmentData(Retrenchment retrench){
         int n = 0;
        PreparedStatement pst = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("UPDATE  EMP_NOTIFICATION SET NOT_TYPE=?,EMP_ID=?,DOE=?,TOE=?,IF_ASSUMED=?,ORDNO=?,ORDDT=?,DEPT_CODE=?,OFF_CODE=?,AUTH,NOTE=?,IF_VISIBLE=?,ENT_DEPT=?,ENT_OFF=?,ENT_AUTH=?,ACS=?,ASCS=? WHERE NOT_ID=?");
            pst.setString(1, "RETRENCHMENT");
            pst.setString(2, retrench.getEmpid());
            pst.setString(3, retrench.getDoe());
            pst.setString(4, retrench.getToe());
            pst.setString(5, retrench.getIfAssumed());
            pst.setString(6, retrench.getOrdno());
            pst.setString(7, retrench.getOrdDate());
            pst.setString(8, retrench.getSltDept());
            pst.setString(9, retrench.getSltOffice());
            pst.setString(10, retrench.getSltAuth());
            pst.setString(11, retrench.getNote());
            pst.setString(12, retrench.getIfVisible());
            pst.setString(13, retrench.getEntDept());
            pst.setString(14, retrench.getEntOffice());
            pst.setString(15, retrench.getEntAuth());
            pst.setString(16, retrench.getCadreStatus());
            pst.setString(17, retrench.getSubStatus());
            pst.setString(18, retrench.getNotId());
            n = pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return n;
    }
    
    public int deleteRetrenchment(String retrenchId){
         int n = 0;
        Connection con = null;
        PreparedStatement pst = null;
        try {
            con = dataSource.getConnection();
            
            if(retrenchId != null && !retrenchId.equals("")){
                pst = con.prepareStatement("DELETE FROM EMP_NOTIFICATION WHERE NOT_ID=?");
                pst.setInt(1, Integer.parseInt(retrenchId));
                pst.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return n;
    }
    
    public Retrenchment editRetrenchment(String retrenchId){
         Retrenchment retrench = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("SELECT NOT_TYPE,EMP_ID,DOE,TOE,IF_ASSUMED,ORDNO,ORDDT,DEPT_CODE,OFF_CODE,AUTH,NOTE,IF_VISIBLE,ENT_DEPT,ENT_OFF,ENT_AUTH,ACS,ASCS FROM EMP_NOTIFICATION WHERE NOT_ID=?");
            pst.setString(1, retrenchId);
            rs = pst.executeQuery();
            if (rs.next()) {
                retrench = new Retrenchment();
                retrench.setNotType("RETRENCHMENT");
                retrench.setEmpid(rs.getString("EMP_ID"));
                retrench.setDoe(rs.getString("DOE"));
                retrench.setToe(rs.getString("TOE"));
                retrench.setIfAssumed(rs.getString("IF_ASSUMED"));
                retrench.setOrdno(rs.getString("ORDNO"));
                retrench.setOrdDate(rs.getString("ORDDT"));
                retrench.setSltDept(rs.getString("DEPT_CODE"));
                retrench.setSltOffice(rs.getString("OFF_CODE"));
                retrench.setSltAuth(rs.getString("AUTH"));
                retrench.setNote(rs.getString("NOTE"));
                retrench.setIfVisible(rs.getString("IF_VISIBLE"));
                retrench.setEntDept(rs.getString("ENT_DEPT"));
                retrench.setEntOffice(rs.getString("ENT_OFF"));
                retrench.setEntAuth(rs.getString("ENT_AUTH"));
                retrench.setCadreStatus(rs.getString("ACS"));
                retrench.setSubStatus(rs.getString("ASCS"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return retrench;
    }
}
