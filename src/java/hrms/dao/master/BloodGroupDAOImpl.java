
package hrms.dao.master;

import hrms.common.DataBaseFunctions;
import hrms.model.master.BloodGroup;
import hrms.model.master.Category;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.annotation.Resource;
import javax.sql.DataSource;

public class BloodGroupDAOImpl implements BloodGroupDAO{
     @Resource(name = "dataSource")
    protected DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public ArrayList getBloodGroup() {
       Connection con = null;
        ResultSet rs = null;
        PreparedStatement st = null;
        ArrayList bloodgrpList = new ArrayList();
        BloodGroup bloodGrp = null;
        try {
            con = dataSource.getConnection();
            st = con.prepareStatement("SELECT BLOODGRP FROM G_BLOODGRP ORDER BY BLOODGRP");
            rs = st.executeQuery();
            while (rs.next()) {
                bloodGrp = new BloodGroup();
                bloodGrp.setBloodgrpId(rs.getString("BLOODGRP"));
                bloodGrp.setBloodgrp(rs.getString("BLOODGRP"));
                bloodgrpList.add(bloodGrp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return bloodgrpList;
    }   
    
    
}
