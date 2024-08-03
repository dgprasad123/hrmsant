/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.master;

import hrms.common.DataBaseFunctions;
import hrms.model.master.Relation;
import hrms.model.master.Religion;
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
public class ReligionDAOImpl implements ReligionDAO{
     @Resource(name = "dataSource")
    protected DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public ArrayList getReligionList(){
       Connection con = null;
        ResultSet rs = null;
        PreparedStatement st = null;
        ArrayList religionList = new ArrayList();
        Religion religion = null;
        try {
            con = dataSource.getConnection();
            st = con.prepareStatement("SELECT int_religion_id,religion FROM g_religion ORDER BY religion");
            rs = st.executeQuery();
            while (rs.next()) {
                religion = new Religion();
                religion.setReligionId(rs.getString("int_religion_id"));
                religion.setReligion(rs.getString("religion"));
                religionList.add(religion);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return religionList;
    } 
    
}
