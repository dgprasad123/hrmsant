
package hrms.dao.master;

import hrms.common.DataBaseFunctions;
import hrms.model.master.IdentityType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.annotation.Resource;
import javax.sql.DataSource;

public class IdentityTypeDAOImpl implements IdentityTypeDAO{
     @Resource(name = "dataSource")
    protected DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public ArrayList getIdentityTypeList(){
     Connection con = null;
        ResultSet rs = null;
        PreparedStatement st = null;
        ArrayList idDocList = new ArrayList();
        IdentityType idType = null;
        try {
            con = dataSource.getConnection();
            st = con.prepareStatement("SELECT ID_DESCRIPTION,INT_IDENTITY_TYPE_ID FROM G_ID_DOC ORDER BY ID_DESCRIPTION");
            rs = st.executeQuery();
            while (rs.next()) {
                idType = new IdentityType();
                idType.setIdentityTypeId(rs.getString("ID_DESCRIPTION"));
                idType.setIdentityType(rs.getString("ID_DESCRIPTION"));
                idDocList.add(idType);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return idDocList;
    }
}
