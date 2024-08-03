
package hrms.dao.master;

import hrms.common.DataBaseFunctions;
import hrms.model.master.GisType;
import hrms.model.master.MaritalStatus;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.annotation.Resource;
import javax.sql.DataSource;

public class MaritalStatusDAOImpl implements MaritalStatusDAO{
     @Resource(name = "dataSource")
    protected DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public ArrayList getMaritalList(){
         Connection con = null;
        ResultSet rs = null;
        PreparedStatement st = null;
        ArrayList maritalList = new ArrayList();
        MaritalStatus mStatus = null;
        try {
            con = dataSource.getConnection();
            st = con.prepareStatement("SELECT M_STATUS,INT_MARITAL_STATUS_ID FROM G_MARITAL ORDER BY M_STATUS");
            rs = st.executeQuery();
            while (rs.next()) {
                mStatus = new MaritalStatus();
                mStatus.setMaritalStatus(rs.getString("M_STATUS"));
                mStatus.setMaritalId(rs.getString("INT_MARITAL_STATUS_ID"));
                maritalList.add(mStatus);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return maritalList;
    }
    
}
