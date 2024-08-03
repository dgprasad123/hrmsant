
package hrms.dao.payrevision;
import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.model.payfixation.PayFixation;
import hrms.model.payrevision.PayRevisionList;
import hrms.model.payrevision.Payrevision;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;

public class PayrevisionDAOImpl implements PayrevisionDAO{
    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
      public int insertPayrevisionData(Payrevision payrev){
           int n = 0;
        PreparedStatement pst = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            String mcode = CommonFunctions.getMaxCode("EMP_NOTIFICATION", "NOT_ID", con);
            pst = con.prepareStatement("INSERT INTO EMP_NOTIFICATION(NOT_ID,NOT_TYPE,EMP_ID,DOE,TOE,IF_ASSUMED,ORDNO,ORDDT,DEPT_CODE,OFF_CODE,AUTH,NOTE,IF_VISIBLE,ENT_DEPT,ENT_OFF,ENT_AUTH,ACS,ASCS) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            pst.setString(1, mcode);
            pst.setString(2, "PAYREVISION");
            pst.setString(3, payrev.getEmpid());
            pst.setString(4, payrev.getDoe());
            pst.setString(5, payrev.getToe());
            pst.setString(6, payrev.getIfAssumed());
            pst.setString(7, payrev.getOrdno());
            pst.setString(8, payrev.getOrdDate());
            pst.setString(9, payrev.getSltDept());
            pst.setString(10, payrev.getSltOffice());
            pst.setString(11, payrev.getSltAuth());
            pst.setString(12, payrev.getNote());
            pst.setString(13, payrev.getIfVisible());
            pst.setString(14, payrev.getEntDept());
            pst.setString(15, payrev.getEntOffice());
            pst.setString(16, payrev.getEntAuth());
            pst.setString(17, payrev.getCadreStatus());
            pst.setString(18, payrev.getSubStatus());
            n = pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return n;
      }
    
    public int updatePayrevisionData(Payrevision payrev){
        int n = 0;
        PreparedStatement pst = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("UPDATE  EMP_NOTIFICATION SET NOT_TYPE=?,EMP_ID=?,DOE=?,TOE=?,IF_ASSUMED=?,ORDNO=?,ORDDT=?,DEPT_CODE=?,OFF_CODE=?,AUTH,NOTE=?,IF_VISIBLE=?,ENT_DEPT=?,ENT_OFF=?,ENT_AUTH=?,ACS=?,ASCS=? WHERE NOT_ID=?");
            pst.setString(1, "PAYREVISION");
            pst.setString(2, payrev.getEmpid());
            pst.setString(3, payrev.getDoe());
            pst.setString(4, payrev.getToe());
            pst.setString(5, payrev.getIfAssumed());
            pst.setString(6, payrev.getOrdno());
            pst.setString(7, payrev.getOrdDate());
            pst.setString(8, payrev.getSltDept());
            pst.setString(9, payrev.getSltOffice());
            pst.setString(10, payrev.getSltAuth());
            pst.setString(11, payrev.getNote());
            pst.setString(12, payrev.getIfVisible());
            pst.setString(13, payrev.getEntDept());
            pst.setString(14, payrev.getEntOffice());
            pst.setString(15, payrev.getEntAuth());
            pst.setString(16, payrev.getCadreStatus());
            pst.setString(17, payrev.getSubStatus());
            pst.setString(18, payrev.getNotId());
            n = pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return n;
    }
    
    public int deletePayrevision(String payrevisionId){
         int n = 0;
        Connection con = null;
        PreparedStatement pst = null;
        try {
            con = dataSource.getConnection();
            
            if(payrevisionId != null && !payrevisionId.equals("")){
                pst = con.prepareStatement("DELETE FROM EMP_NOTIFICATION WHERE NOT_ID=?");
                pst.setInt(1, Integer.parseInt(payrevisionId));
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
    
    public Payrevision editPayrevision(String payrevisionId){
        Payrevision payrev = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("SELECT NOT_TYPE,EMP_ID,DOE,TOE,IF_ASSUMED,ORDNO,ORDDT,DEPT_CODE,OFF_CODE,AUTH,NOTE,IF_VISIBLE,ENT_DEPT,ENT_OFF,ENT_AUTH,ACS,ASCS FROM EMP_NOTIFICATION WHERE NOT_ID=?");
            pst.setString(1, payrevisionId);
            rs = pst.executeQuery();
            if (rs.next()) {
                payrev = new Payrevision();
                payrev.setNotType(rs.getString("NOT_TYPE"));
                payrev.setEmpid(rs.getString("EMP_ID"));
                payrev.setDoe(rs.getString("DOE"));
                payrev.setToe(rs.getString("TOE"));
                payrev.setIfAssumed(rs.getString("IF_ASSUMED"));
                payrev.setOrdno(rs.getString("ORDNO"));
                payrev.setOrdDate(rs.getString("ORDDT"));
                payrev.setSltDept(rs.getString("DEPT_CODE"));
                payrev.setSltOffice(rs.getString("OFF_CODE"));
                payrev.setSltAuth(rs.getString("AUTH"));
                payrev.setNote(rs.getString("NOTE"));
                payrev.setIfVisible(rs.getString("IF_VISIBLE"));
                payrev.setEntDept(rs.getString("ENT_DEPT"));
                payrev.setEntOffice(rs.getString("ENT_OFF"));
                payrev.setEntAuth(rs.getString("ENT_AUTH"));
                payrev.setCadreStatus(rs.getString("ACS"));
                payrev.setSubStatus(rs.getString("ASCS"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return payrev;
    }
    
    public List findAllPayrevision(String empId){
          List list = null;
        Connection con=null;
        PreparedStatement pst=null;
        ResultSet rs=null;
        PayRevisionList payRevList=null;
        list=new ArrayList();
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("SELECT NOT_ID,NOT_TYPE,DOE,ORDNO,ORDDT FROM EMP_NOTIFICATION WHERE EMP_ID=? AND NOT_TYPE='PAYREVISION'");
            pst.setString(1, empId);
            rs = pst.executeQuery();
            while (rs.next()) {
                payRevList = new PayRevisionList();
                payRevList.setNotId(rs.getString("NOT_ID"));
                payRevList.setNotType(rs.getString("NOT_TYPE"));
                payRevList.setDoe(rs.getString("DOE"));
                payRevList.setOrdno(rs.getString("ORDNO"));
                payRevList.setOrdDate(rs.getString("ORDDT"));
                list.add(payRevList);
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
