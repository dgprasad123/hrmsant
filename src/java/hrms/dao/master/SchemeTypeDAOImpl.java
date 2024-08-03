package hrms.dao.master;

import hrms.common.DataBaseFunctions;
import hrms.model.master.LoanType;
import hrms.model.master.SchemeType;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import javax.annotation.Resource;
import javax.sql.DataSource;

public class SchemeTypeDAOImpl implements SchemeTypeDAO{
     @Resource(name = "dataSource")
    protected DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public ArrayList getSchemeTypeList(){
        Connection con = null;
        ResultSet rs = null;
        Statement st = null;
        ArrayList schemeTypeList = new ArrayList();
        SchemeType schemeType=null;
        try {
            con = dataSource.getConnection();
            st = con.createStatement();
            rs = st.executeQuery("SELECT SCHEME_ID,SCHEME_NAME FROM G_SCHEME");
            while(rs.next()){
                schemeType=new SchemeType();
                schemeType.setSchemeId(rs.getString("SCHEME_ID"));
                schemeType.setSchemeName(rs.getString("SCHEME_NAME"));
                schemeTypeList.add(schemeType);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return schemeTypeList;
    }

}
