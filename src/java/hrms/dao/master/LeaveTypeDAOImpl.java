
package hrms.dao.master;
import hrms.common.DataBaseFunctions;
import hrms.model.master.Bank;
import hrms.model.master.LeaveTypeBean;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import javax.annotation.Resource;
import javax.sql.DataSource;
public class LeaveTypeDAOImpl implements LeaveTypeDAO{
   @Resource(name = "dataSource")
    protected DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

   @Override
    public ArrayList getLeaveTypeList() {
        Connection con = null;
        ResultSet rs = null;
        Statement st = null;
        ArrayList<LeaveTypeBean> leaveTypeList = new ArrayList();
        LeaveTypeBean leaveMast=null;
        try {
            con = dataSource.getConnection();
            st = con.createStatement();
            rs = st.executeQuery("SELECT DISTINCT TOL_ID,TOL FROM G_LEAVE WHERE IF_NOT_APP != 'Y' OR IF_NOT_APP IS NULL ORDER BY TOL ASC");
            while(rs.next()){
                leaveMast=new LeaveTypeBean();
                leaveMast.setTolid(rs.getString("TOL_ID"));
                leaveMast.setTol(rs.getString("TOL"));
                leaveTypeList.add(leaveMast);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return leaveTypeList;
    } 
}
