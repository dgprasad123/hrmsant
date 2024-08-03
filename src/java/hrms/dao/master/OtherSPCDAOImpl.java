/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.master;

import hrms.common.DataBaseFunctions;
import hrms.model.master.OtherSPC;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 *
 * @author lenovo
 */
public class OtherSPCDAOImpl implements OtherSPCDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List getOtherSPCList(String offType) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List officelist = new ArrayList();
        OtherSPC ospc = null;
        try {
            con = dataSource.getConnection();
            // System.out.println("SELECT oth_spc,off_en from g_oth_spc where is_active='Y' AND oth_spc LIKE '"+offType+"%' order by off_en asc");
            pstmt = con.prepareStatement("SELECT oth_spc,off_en from g_oth_spc where is_active='Y' AND oth_spc ='"+offType+"' order by off_en asc");
            rs = pstmt.executeQuery();
            while (rs.next()) {
                ospc = new OtherSPC();
                ospc.setStrOtherSPC(rs.getString("oth_spc"));
                ospc.setOffName(rs.getString("off_en"));
                officelist.add(ospc);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return officelist;
    }

    @Override
    public void saveOtherSPCList(OtherSPC ospc) {
        
        Connection con = null;
        
        ResultSet rs = null;
        PreparedStatement ps = null;
        
        String officeName = ospc.getStrOtherSPC();       
        String otherspc = ospc.getHiddenoffType();
       
        String maxBCode = null;
        try {
            con = this.dataSource.getConnection();
            
            if(otherspc != null && otherspc.equals("SGO")){
                ospc.setSltCategory(null);
            }
            
            ps = con.prepareStatement("INSERT INTO g_oth_spc(oth_spc,off_en,is_active,category,dept_name) VALUES(?,?,?,?,?)");
            ps.setString(1, otherspc);
            ps.setString(2, officeName.toUpperCase());
            ps.setString(3, "Y");
            ps.setString(4, ospc.getSltCategory());
            ps.setString(5, ospc.getSltDept());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs,ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }
}
