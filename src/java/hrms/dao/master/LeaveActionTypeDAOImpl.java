
package hrms.dao.master;

import hrms.SelectOption;
import hrms.common.DataBaseFunctions;
import hrms.model.master.LeaveActionType;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import javax.annotation.Resource;
import javax.sql.DataSource;

public class LeaveActionTypeDAOImpl implements LeaveActionTypeDAO{
    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

     @Override
    public ArrayList getActionType(){
        ArrayList al = new ArrayList();
        ResultSet rs = null;
        LeaveActionType leaveActType = null;
        Statement st = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            st = con.createStatement();
            rs = st.executeQuery("SELECT STATUS_ID,STATUS_NAME FROM G_PROCESS_STATUS WHERE PROCESS_ID='1' AND STATUS_ID!='3' AND STATUS_ID!='4' AND STATUS_ID!='5' AND STATUS_ID!='41' AND STATUS_ID!='104' AND STATUS_ID!='105' ORDER BY STATUS_ID DESC ");
            while (rs.next()) {
                leaveActType = new LeaveActionType();
                leaveActType.setStatusId(rs.getString("STATUS_ID"));
                leaveActType.setStatusName(rs.getString("STATUS_NAME"));
                al.add(leaveActType);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return al;
    }
}
