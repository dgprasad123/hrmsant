
package hrms.dao.reemployment;
import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;

import hrms.model.redeployment.Redeployment;
import hrms.model.reemployment.Reemployment;
import hrms.model.reemployment.ReemploymentList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;
public class ReemploymentDAOImpl implements ReemploymentDAO{
    @Resource(name = "dataSource")
    protected DataSource dataSource;
    public DataSource getDataSource() {
        return dataSource;
    }
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
  public int insertReemploymentData(Reemployment reemployment){
       int n = 0;
        PreparedStatement pst = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            String mcode = CommonFunctions.getMaxCode("EMP_NOTIFICATION", "NOT_ID", con);
            pst = con.prepareStatement("INSERT INTO EMP_NOTIFICATION(NOT_ID,NOT_TYPE,EMP_ID,DOE,TOE,IF_ASSUMED,ORDNO,ORDDT,DEPT_CODE,OFF_CODE,AUTH,NOTE,IF_VISIBLE,ENT_DEPT,ENT_OFF,ENT_AUTH,ACS,ASCS) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            pst.setString(1, mcode);
            pst.setString(2, "REEMPLOYMENT");
            pst.setString(3, reemployment.getEmpid());
            pst.setString(4, reemployment.getDoe());
            pst.setString(5, reemployment.getToe());
            pst.setString(6, reemployment.getIfAssumed());
            pst.setString(7, reemployment.getOrdno());
            pst.setString(8, reemployment.getOrdDate());
            pst.setString(9, reemployment.getSltDept());
            pst.setString(10, reemployment.getSltOffice());
            pst.setString(11, reemployment.getSltAuth());
            pst.setString(12, reemployment.getNote());
            pst.setString(13, reemployment.getIfVisible());
            pst.setString(14, reemployment.getEntDept());
            pst.setString(15, reemployment.getEntOffice());
            pst.setString(16, reemployment.getEntAuth());
            pst.setString(17, reemployment.getCadreStatus());
            pst.setString(18, reemployment.getSubStatus());
            n = pst.executeUpdate();
            
            pst = con.prepareStatement("UPDATE EMP_MAST SET IF_GPF_ASSUMED='Y' WHERE EMP_ID=?");
            pst.setString(1,reemployment.getEmpid());
            pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return n;
  }
    
    public int updateReemploymentData(Reemployment reemployment){
         int n = 0;
        PreparedStatement pst = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("UPDATE  EMP_NOTIFICATION SET NOT_TYPE=?,EMP_ID=?,DOE=?,TOE=?,IF_ASSUMED=?,ORDNO=?,ORDDT=?,DEPT_CODE=?,OFF_CODE=?,AUTH,NOTE=?,IF_VISIBLE=?,ENT_DEPT=?,ENT_OFF=?,ENT_AUTH=?,ACS=?,ASCS=? WHERE NOT_ID=?");
            pst.setString(1, "REEMPLOYMENT");
            pst.setString(2, reemployment.getEmpid());
            pst.setString(3, reemployment.getDoe());
            pst.setString(4, reemployment.getToe());
            pst.setString(5, reemployment.getIfAssumed());
            pst.setString(6, reemployment.getOrdno());
            pst.setString(7, reemployment.getOrdDate());
            pst.setString(8, reemployment.getSltDept());
            pst.setString(9, reemployment.getSltOffice());
            pst.setString(10, reemployment.getSltAuth());
            pst.setString(11, reemployment.getNote());
            pst.setString(12, reemployment.getIfVisible());
            pst.setString(13, reemployment.getEntDept());
            pst.setString(14, reemployment.getEntOffice());
            pst.setString(15, reemployment.getEntAuth());
            pst.setString(16, reemployment.getCadreStatus());
            pst.setString(17, reemployment.getSubStatus());
            pst.setString(18, reemployment.getNotId());
            n = pst.executeUpdate();
            
            pst = con.prepareStatement("UPDATE EMP_MAST SET IF_GPF_ASSUMED='Y' WHERE EMP_ID=?");
            pst.setString(1,reemployment.getEmpid());
            pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return n;
    }
    
    public int deleteReemployment(String reemploymentId){
         int n = 0;
        Connection con = null;
        PreparedStatement pst = null;
        try {
            con = dataSource.getConnection();
            
            if(reemploymentId != null && !reemploymentId.equals("")){
                pst = con.prepareStatement("DELETE FROM EMP_NOTIFICATION WHERE NOT_ID=?");
                pst.setInt(1, Integer.parseInt(reemploymentId));
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
    
    public Reemployment editReemployment(String reemploymentId){
          Reemployment reemp = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("SELECT NOT_TYPE,EMP_ID,DOE,TOE,IF_ASSUMED,ORDNO,ORDDT,DEPT_CODE,OFF_CODE,AUTH,NOTE,IF_VISIBLE,ENT_DEPT,ENT_OFF,ENT_AUTH,ACS,ASCS FROM EMP_NOTIFICATION WHERE NOT_ID=?");
            pst.setString(1, reemploymentId);
            rs = pst.executeQuery();
            if (rs.next()) {
                reemp = new Reemployment();
                reemp.setNotType("REEMPLOYMENT");
                reemp.setEmpid(rs.getString("EMP_ID"));
                reemp.setDoe(rs.getString("DOE"));
                reemp.setToe(rs.getString("TOE"));
                reemp.setIfAssumed(rs.getString("IF_ASSUMED"));
                reemp.setOrdno(rs.getString("ORDNO"));
                reemp.setOrdDate(rs.getString("ORDDT"));
                reemp.setSltDept(rs.getString("DEPT_CODE"));
                reemp.setSltOffice(rs.getString("OFF_CODE"));
                reemp.setSltAuth(rs.getString("AUTH"));
                reemp.setNote(rs.getString("NOTE"));
                reemp.setIfVisible(rs.getString("IF_VISIBLE"));
                reemp.setEntDept(rs.getString("ENT_DEPT"));
                reemp.setEntOffice(rs.getString("ENT_OFF"));
                reemp.setEntAuth(rs.getString("ENT_AUTH"));
                reemp.setCadreStatus(rs.getString("ACS"));
                reemp.setSubStatus(rs.getString("ASCS"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return reemp;
    }
    
    public List findAllReemployment(String empId){
         List list = null;
        Connection con=null;
        PreparedStatement pst=null;
        ResultSet rs=null;
        ReemploymentList reempList=null;
        list=new ArrayList();
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("SELECT NOT_ID,DOE,ORDNO,ORDDT FROM EMP_NOTIFICATION WHERE EMP_ID=? AND NOT_TYPE='REEMPLOYMENT'");
            pst.setString(1, empId);
            rs = pst.executeQuery();
            while (rs.next()) {
                reempList = new ReemploymentList();
                reempList.setNotId(rs.getString("NOT_ID"));
                reempList.setNotType("REEMPLOYMENT");
                reempList.setDoe(rs.getString("DOE"));
                reempList.setOrdno(rs.getString("ORDNO"));
                reempList.setOrdDate(rs.getString("ORDDT"));
                list.add(reempList);
            }
        } catch (Exception e) {
                e.printStackTrace();
        } finally {
             DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return list;
    }   
}
