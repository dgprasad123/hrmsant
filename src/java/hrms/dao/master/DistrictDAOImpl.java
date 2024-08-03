/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.master;

import hrms.common.DataBaseFunctions;
import hrms.model.master.District;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 *
 * @author Manas Jena
 */
public class DistrictDAOImpl implements DistrictDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public ArrayList getDistrictList() {
        Connection con = null;
        ResultSet rs = null;
        Statement st = null;
        ArrayList districtList = new ArrayList();
        try {
            con = dataSource.getConnection();
            st = con.createStatement();
            rs = st.executeQuery("select * from g_district where state_code='21' and is_rev_district='Y' order by dist_name");
            while (rs.next()) {
                District district = new District();
                district.setDistCode(rs.getString("dist_code"));
                district.setDistName(rs.getString("dist_name"));
                districtList.add(district);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return districtList;
    }

    @Override
    public ArrayList getDistrictList(String stateCode) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        ArrayList districtList = new ArrayList();
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("select * from g_district where state_code=? and is_rev_district='Y' order by dist_name");
            pstmt.setString(1, stateCode);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                District district = new District();
                district.setDistCode(rs.getString("dist_code"));
                district.setDistName(rs.getString("dist_name"));
                districtList.add(district);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return districtList;
    }

    @Override
    public ArrayList getPoliceDistrictList() {
        Connection con = null;
        ResultSet rs = null;
        Statement st = null;
        ArrayList districtList = new ArrayList();
        try {
            con = dataSource.getConnection();
            st = con.createStatement();
            rs = st.executeQuery("select * from g_district where state_code IN ('21', '51','19','60') order by dist_name");
            while (rs.next()) {
                District district = new District();
                district.setDistCode(rs.getString("dist_code"));
                district.setDistName(rs.getString("dist_name"));
                districtList.add(district);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return districtList;
    }
    
    @Override
    public District editDistrict(District  district){
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = this.dataSource.getConnection();

            String sql = "select dist_code,dist_name,is_rev_district,state_code from g_district where dist_code=?";  
            pst = con.prepareStatement(sql);
            pst.setString(1,district.getDistCode());
            rs = pst.executeQuery();
            if (rs.next()) {
               district.setHiddistCode(rs.getString("dist_code"));              
               district.setDistCode(rs.getString("dist_code"));
               district.setDistName(rs.getString("dist_name"));   
               district.setActivedist(rs.getString("is_rev_district"));  
               district.setStateCode(rs.getString("state_code"));              
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
       return district;
    
    }
    
    @Override
    public void saveNewDistrict(District  district){
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;     
        try {
                con = dataSource.getConnection();
                int maxdistCode = 0;

                pst = con.prepareStatement("SELECT MAX( dist_code ) ::INTEGER +1 maxdistcode FROM g_district where state_code=?");
                pst.setString(1, district.getHidstateCode());
                rs = pst.executeQuery();                
                if (rs.next()) {
                    maxdistCode = rs.getInt("maxdistcode");                 
                }
                    pst = con.prepareStatement("INSERT INTO g_district(dist_code,dist_name, state_code,is_rev_district) VALUES(?, ?,?,?)");
                    pst.setInt(1, maxdistCode);                  
                    pst.setString(2, district.getDistName().toUpperCase());
                    pst.setString(3, district.getHidstateCode());
                    pst.setString(4, district.getActivedist());
                    pst.executeUpdate(); 
                
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }
    
    @Override
    public void updateNewDistrict(District  district){
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();               
            pst = con.prepareStatement("UPDATE g_district SET dist_name=?,is_rev_district=? where dist_code=?");
           
                pst.setString(1, district.getDistName().toUpperCase());
                pst.setString(2, district.getActivedist());
                pst.setString(3, district.getDistCode());    
                pst.executeUpdate(); 
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }    
    }

    @Override
    public String getDistrictName(String distCode) {        
        Connection con=null;
        ResultSet rs=null;
        PreparedStatement ps=null;
        String distName=null;
        try{
            con = dataSource.getConnection();
            ps=con.prepareStatement("Select * from G_district where dist_code=?");
            ps.setString(1, distCode);
            rs=ps.executeQuery();
            if(rs.next()){
                distName=rs.getString("dist_name");
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
        finally {
            DataBaseFunctions.closeSqlObjects(ps);
            DataBaseFunctions.closeSqlObjects(con);
        }  
        return distName;
    }
    
   
}
