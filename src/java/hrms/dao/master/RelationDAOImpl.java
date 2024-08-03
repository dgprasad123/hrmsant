/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.master;

import hrms.common.DataBaseFunctions;
import hrms.model.master.Language;
import hrms.model.master.Relation;
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
public class RelationDAOImpl implements RelationDAO{
    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public ArrayList getRelationList(){
       Connection con = null;
        ResultSet rs = null;
        PreparedStatement st = null;
        ArrayList relationList = new ArrayList();
        Relation relation = null;
        try {
            con = dataSource.getConnection();
            st = con.prepareStatement("SELECT INT_REL_ID,RELATION FROM G_RELATION ORDER BY RELATION");
            rs = st.executeQuery();
            while (rs.next()) {
                relation = new Relation();
                relation.setRelId(rs.getString("INT_REL_ID"));
                relation.setRelName(rs.getString("RELATION"));
                relationList.add(relation);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return relationList;
    } 
    
    
}
