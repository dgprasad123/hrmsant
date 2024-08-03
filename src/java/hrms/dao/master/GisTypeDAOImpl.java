/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.master;
import hrms.common.DataBaseFunctions;
import hrms.model.master.GisType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 *
 * @author lenovo pc
 */
public class GisTypeDAOImpl implements GisTypeDAO{
    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public ArrayList getGisTypeList(){
         Connection con = null;
        ResultSet rs = null;
        PreparedStatement st = null;
        ArrayList gisList = new ArrayList();
        GisType gisType = null;
        try {
            con = dataSource.getConnection();
            st = con.prepareStatement("SELECT SCHEME_ID,SCHEME_NAME FROM G_SCHEME ORDER BY SCHEME_NAME");
            rs = st.executeQuery();
            while (rs.next()) {
                gisType = new GisType();
                gisType.setSchemeId(rs.getString("SCHEME_ID"));
                gisType.setSchemeName(rs.getString("SCHEME_NAME"));
                gisList.add(gisType);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return gisList;
    }
    
}
