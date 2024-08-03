/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.master;

import hrms.common.DataBaseFunctions;
import hrms.model.master.Village;
import java.math.BigInteger;
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
public class VillageDAOImpl implements VillageDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override

    public ArrayList getVillageList(String distCode, String psCode) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement st = null;
        ArrayList villList = new ArrayList();
        Village vill = null;
        try {
            con = dataSource.getConnection();
            st = con.prepareStatement("SELECT VILL_CODE,VILLAGE_NAME FROM G_VILLAGE WHERE DIST_CODE=? AND PS_CODE=? ORDER BY VILLAGE_NAME");
            st.setString(1, distCode);
            st.setString(2, psCode);
            rs = st.executeQuery();
            while (rs.next()) {
                vill = new Village();
                vill.setVillageCode(rs.getString("VILL_CODE"));
                vill.setVillageName(rs.getString("VILLAGE_NAME"));
                villList.add(vill);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return villList;
    }
    
    
    @Override
    public Village editVillage(Village  village){
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = this.dataSource.getConnection();

            String sql = "SELECT VILL_CODE,VILLAGE_NAME,gdist.dist_code ,gst.state_code,gps.ps_code\n"
                    + "FROM (select * from g_village where VILL_CODE=?)g_village\n"
                    + "left outer join g_district gdist on g_village.dist_code=gdist.dist_code\n"
                    + "left outer join g_state gst on gdist.state_code=gst.state_code\n"
                    + "left outer join g_ps gps on g_village.ps_code=gps.ps_code";
            pst = con.prepareStatement(sql);
            pst.setString(1,village.getVillageCode());
            rs = pst.executeQuery();
            if (rs.next()) {
                
               village.setHidvilCode(rs.getString("VILL_CODE"));                      
               village.setVillageCode(rs.getString("VILL_CODE"));
               village.setVillageName(rs.getString("VILLAGE_NAME"));               
               village.setDistCode(rs.getString("dist_code"));
               village.setStateCode(rs.getString("state_code"));    
               village.setPsCode(rs.getString("ps_code"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
       return village; 
    
    }
    
    @Override
    public void saveNewVillage(Village  village){
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;     
        String maxvilCodeStr = "";
        try {
                con = dataSource.getConnection();
                int maxvilCode = 0;            
                int recordFound = 0;
                
                pst = con.prepareStatement("SELECT COUNT(vill_code) as count_vill FROM g_village where ps_code=?");
                    pst.setString(1, village.getHidpsCode()); 
                    rs = pst.executeQuery(); 
                 if (rs.next()) {
                    recordFound = rs.getInt("count_vill");  
                    }
               
                 if(recordFound ==0 ){
                     
                    String psCode= village.getHidpsCode();
                    maxvilCodeStr = psCode + "00000001" ; 
                     
                    pst = con.prepareStatement("INSERT INTO g_village(VILL_CODE,dist_code, VILLAGE_NAME,ps_code) VALUES(?,?,?,?)");
                    pst.setString(1, maxvilCodeStr);                  
                    pst.setString(2, village.getHiddistCode());
                    pst.setString(3, village.getVillageName().toUpperCase());  
                    pst.setString(4, village.getHidpsCode()); 
                    pst.executeUpdate();                   
                 
                 }
                else{
                    pst = con.prepareStatement("SELECT MAX( VILL_CODE ) ::BIGINT +1 maxvillcode FROM g_village where ps_code=?");
                    pst.setString(1, village.getHidpsCode());
                    rs = pst.executeQuery();  
                    
                    if (rs.next()) {
                    maxvilCodeStr = rs.getString("maxvillcode");                      
                    }
                    
                    pst = con.prepareStatement("INSERT INTO g_village(VILL_CODE,dist_code, VILLAGE_NAME,ps_code) VALUES(?,?,?,?)");
                    pst.setString(1, maxvilCodeStr);                  
                    pst.setString(2, village.getHiddistCode());
                    pst.setString(3, village.getVillageName().toUpperCase());  
                    pst.setString(4, village.getHidpsCode()); 
                    pst.executeUpdate();               
                 }
                            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }  
    
    }
    
    @Override
    public void updateVillage(Village  village){
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();               
            pst = con.prepareStatement("UPDATE g_village SET VILLAGE_NAME=? where VILL_CODE=?");          
                pst.setString(1, village.getVillageName().toUpperCase());                           
                pst.setString(2, village.getVillageCode());   
                
                pst.executeUpdate(); 
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }    
    
    
    }
}
