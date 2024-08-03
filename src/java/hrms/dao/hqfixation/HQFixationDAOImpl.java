
package hrms.dao.hqfixation;

import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.model.suspension.Suspension;
import hrms.model.suspension.SuspensionList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;

public class HQFixationDAOImpl implements HQFixationDAO{
     @Resource(name = "dataSource")
    protected DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
     public List findAllHQFixation(String empId){
          List list = null;
        Connection con=null;
        PreparedStatement pst=null;
        ResultSet rs=null;
        SuspensionList susList=null;
        list=new ArrayList();
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("SELECT sp_id, emp_id, doe,ordno, orddt,wefd FROM emp_suspension WHERE emp_id=? and HQ_FIX='Y'");
            pst.setString(1, empId);
            rs = pst.executeQuery();
            while (rs.next()) {
                susList = new SuspensionList();
                susList.setSpId(rs.getString("sp_id"));
                susList.setDoe(rs.getString("DOE"));
                susList.setOrdno(rs.getString("ORDNO"));
                susList.setOrdDate(rs.getString("ORDDT"));
                susList.setWefdate(rs.getString("wefd"));
                list.add(susList);
            }
        } catch (Exception e) {
                e.printStackTrace();
        } finally {
             DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return list;
     }
    
    public int insertHQFixationData(Suspension hqf){
        int n = 0;
        PreparedStatement pst = null;
        Connection con = null;
        String susid="";
        try {
            con = dataSource.getConnection();
            susid = CommonFunctions.getMaxCode("emp_suspension","sp_id",con);
            pst = con.prepareStatement("insert into emp_suspension (sp_id, emp_id, doe, TOE, IF_ASSUMED, ordno, orddt, auth_dept, auth_off, auth, wefd, weft, SUSP_ALLOW, HQ_OFF_CODE, note, IF_VISIBLE,ent_dept,ent_off,ent_auth,HQ_FIX,ACS) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
             pst.setString(1, susid);
            pst.setString(2, hqf.getEmpid());
            pst.setString(3, hqf.getDoe());
            pst.setString(4, hqf.getToe());
            pst.setString(5, hqf.getIfAssumed());
            pst.setString(6, hqf.getOrdno());
            pst.setString(7, hqf.getOrdDate());
            pst.setString(8, hqf.getSltDept());
            pst.setString(9, hqf.getSltOffice());
            pst.setString(10,hqf.getSltAuth());
            pst.setString(11,hqf.getWefdate());
            pst.setString(12,hqf.getSltweftime());
            pst.setString(13,hqf.getTxtallowance());
            pst.setString(14,hqf.getSlthqoffice());
            pst.setString(15,hqf.getNote());
            pst.setString(16, hqf.getIfVisible());
            pst.setString(17, hqf.getEntDept());
            pst.setString(18, hqf.getEntOffice());
            pst.setString(19, hqf.getEntAuth());
            pst.setString(20,"Y");
            pst.setString(21, hqf.getCadreStatus());
          
        }catch(Exception e){
            e.printStackTrace();
        }
         return n; 
        }
    
    public int updateHQFixationData(Suspension hqf){
        int n = 0;
        PreparedStatement pst = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("UPDATE  emp_suspension SET emp_id=?, doe=?, TOE=?, IF_ASSUMED=?, ordno=?, orddt=?, auth_dept=?, auth_off=?, auth=?, wefd=?, weft=?, SUSP_ALLOW=?, HQ_OFF_CODE=?, note=?, IF_VISIBLE=?,ent_dept=?,ent_off=?,ent_auth=?,HQ_FIX=?,ACS=? WHERE sp_id=?");
            pst.setString(1, hqf.getEmpid());
            pst.setString(2, hqf.getDoe());
            pst.setString(3, hqf.getToe());
            pst.setString(4, hqf.getIfAssumed());
            pst.setString(5, hqf.getOrdno());
            pst.setString(6, hqf.getOrdDate());
            pst.setString(7, hqf.getSltDept());
            pst.setString(8, hqf.getSltOffice());
            pst.setString(9,hqf.getSltAuth());
            pst.setString(10,hqf.getWefdate());
            pst.setString(11,hqf.getSltweftime());
            pst.setString(12,hqf.getTxtallowance());
            pst.setString(13,hqf.getSlthqoffice());
            pst.setString(14,hqf.getNote());
            pst.setString(15, hqf.getIfVisible());
            pst.setString(16, hqf.getEntDept());
            pst.setString(17, hqf.getEntOffice());
            pst.setString(18, hqf.getEntAuth());
            pst.setString(19,"Y");
            pst.setString(20, hqf.getCadreStatus());
            n = pst.executeUpdate();
        }catch(Exception e){
            e.printStackTrace();
        }
         return n; 
    }
    
    public int deleteHQFixation(String hqfId){
         int n = 0;
        Connection con = null;
        PreparedStatement pst = null;
        try {
            con = dataSource.getConnection();
            pst=con.prepareStatement("DELETE FROM EMP_SUSPENSION WHERE SP_ID =? and HQ_FIX='Y'");
            pst.setString(1, hqfId);
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
             DataBaseFunctions.closeSqlObjects(con);
        }
        return n;
    }
    
    public Suspension editHQFixation(String hqfId){
       Suspension hqf = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("SELECT emp_id, doe, TOE, IF_ASSUMED, ordno, orddt, auth_dept, auth_off, auth, wefd, weft, SUSP_ALLOW, HQ_OFF_CODE, note, IF_VISIBLE,ent_dept,ent_off,ent_auth,HQ_FIX,ACS FROM emp_suspension WHERE sp_id=?");
            pst.setString(1, hqfId);
            rs = pst.executeQuery();
            if (rs.next()) {
                hqf = new Suspension();
                hqf.setEmpid(rs.getString("EMP_ID"));
                hqf.setDoe(rs.getString("DOE"));
                hqf.setToe(rs.getString("TOE"));
                hqf.setIfAssumed(rs.getString("IF_ASSUMED"));
                hqf.setOrdno(rs.getString("ORDNO"));
                hqf.setOrdDate(rs.getString("ORDDT"));
                hqf.setSltDept(rs.getString("DEPT_CODE"));
                hqf.setSltOffice(rs.getString("OFF_CODE"));
                hqf.setSltAuth(rs.getString("AUTH"));
                hqf.setNote(rs.getString("NOTE"));
                hqf.setIfVisible(rs.getString("IF_VISIBLE"));
                hqf.setEntDept(rs.getString("ENT_DEPT"));
                hqf.setEntOffice(rs.getString("ENT_OFF"));
                hqf.setEntAuth(rs.getString("ENT_AUTH"));
                hqf.setCadreStatus(rs.getString("ACS"));
                
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return hqf; 
    }
}
