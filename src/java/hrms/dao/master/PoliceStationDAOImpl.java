/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.master;

import hrms.common.DataBaseFunctions;
import hrms.model.master.PoliceStation;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 *
 * @author lenovo pc
 */
public class PoliceStationDAOImpl implements PoliceStationDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public ArrayList getPoliceStationList(String distCode) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement st = null;
        ArrayList poStList = new ArrayList();
        PoliceStation ps = null;
        try {
            con = dataSource.getConnection();
            st = con.prepareStatement("SELECT PS_CODE,PS_NAME FROM G_PS WHERE DIST_CODE=? ORDER BY PS_NAME");
            st.setString(1, distCode);
            rs = st.executeQuery();
            while (rs.next()) {
                ps = new PoliceStation();
                ps.setPsCode(rs.getString("PS_CODE"));
                ps.setPsName(rs.getString("PS_NAME"));
                poStList.add(ps);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return poStList;
    }
    
    @Override
    public PoliceStation editPoliceStation(PoliceStation pstation){
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = this.dataSource.getConnection();

            String sql = "SELECT ps_code,ps_name,gdist.dist_code ,gst.state_code\n"
                    + "FROM (select * from g_ps where ps_code=?)g_ps\n"
                    + "left outer join g_district gdist on g_ps.dist_code=gdist.dist_code\n"
                    + "left outer join g_state gst on gdist.state_code=gst.state_code";
            pst = con.prepareStatement(sql);
            pst.setString(1,pstation.getPsCode());
            rs = pst.executeQuery();
            if (rs.next()) {
                
               pstation.setHidPsCode(rs.getString("ps_code"));                      
               pstation.setPsCode(rs.getString("ps_code"));
               pstation.setPsName(rs.getString("ps_name")); 
               pstation.setDistCode(rs.getString("dist_code"));
               pstation.setStateCode(rs.getString("state_code"));              
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
       return pstation; 
    }
    
    @Override
    public void saveNewPoliceStation(PoliceStation pstation){
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;    
        String maxpsCodeStr= null;
        try {
                con = dataSource.getConnection();             
                int recordFound = 0;
                pst = con.prepareStatement("SELECT COUNT(ps_code) as count_ps FROM g_ps where dist_code=?");
                    pst.setString(1, pstation.getHiddistCode()); 
                    rs = pst.executeQuery(); 
                 if (rs.next()) {
                    recordFound = rs.getInt("count_ps");  
                    }
                
                if(recordFound==0){
                    String distCode= pstation.getHiddistCode();
                    maxpsCodeStr = distCode + "0001" ; 
                    
                    pst = con.prepareStatement("INSERT INTO g_ps(ps_code,dist_code, ps_name) VALUES(?,?,?)");
                    pst.setString(1, maxpsCodeStr);                  
                    pst.setString(2, pstation.getHiddistCode());
                    pst.setString(3, pstation.getPsName().toUpperCase());                  
                    pst.executeUpdate();                    
                }else{
                    pst = con.prepareStatement("SELECT MAX( ps_code ) ::INTEGER +1 maxpscode FROM g_ps where dist_code=?");
                    pst.setString(1, pstation.getHiddistCode());
                    rs = pst.executeQuery();                
                    if (rs.next()) {
                        maxpsCodeStr = rs.getString("maxpscode");                 
                    }
                    
                    pst = con.prepareStatement("INSERT INTO g_ps(ps_code,dist_code, ps_name) VALUES(?,?,?)");
                    pst.setString(1, maxpsCodeStr);                  
                    pst.setString(2, pstation.getHiddistCode());
                    pst.setString(3, pstation.getPsName().toUpperCase());                  
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
    public void updateNewPoliceStation(PoliceStation pstation){
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();               
            pst = con.prepareStatement("UPDATE g_ps SET ps_name=? where ps_code=?");          
                pst.setString(1, pstation.getPsName().toUpperCase());
                pst.setString(2, pstation.getPsCode());              
                pst.executeUpdate(); 
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }    
    
    }

}
