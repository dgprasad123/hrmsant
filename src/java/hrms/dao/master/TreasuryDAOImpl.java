/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.master;

import hrms.common.DataBaseFunctions;
import hrms.model.master.Treasury;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.springframework.stereotype.Component;

/**
 *
 * @author Manas Jena
 */
@Component
public class TreasuryDAOImpl implements TreasuryDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;
    
    public DataSource getDataSource() {
        return dataSource;
    }
    
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    @Override
    public ArrayList getTreasuryList() {
        Connection con = null;
        ResultSet rs = null;
        Statement st = null;
        ArrayList treasuryList = new ArrayList();
        try {            
            con = dataSource.getConnection();
            st = con.createStatement();
            rs = st.executeQuery("select * from g_treasury order by tr_name asc");
            while (rs.next()) {
                Treasury treasury = new Treasury();
                treasury.setTreasuryCode(rs.getString("tr_code"));
                treasury.setTreasuryName(rs.getString("tr_name"));
                treasury.setOfficeCode(rs.getString("off_code"));
                treasury.setAgtreasuryCode(rs.getString("ag_treasury_code"));
                treasuryList.add(treasury);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs,st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return treasuryList;
    }
    
    @Override
     public List getTreasuryListByOffCode(String offcode){
         Connection con = null;
        ResultSet rs = null;
        Statement st = null;
        List treasuryList = new ArrayList();
        try {            
            con = dataSource.getConnection();
            st = con.createStatement();
            rs = st.executeQuery("select g_office.tr_code, tr_name from g_office "
                    + " inner join g_treasury on g_office.tr_code=g_treasury.tr_code "
                    + " where g_office.off_code='"+offcode+"' order by tr_name asc");
            while (rs.next()) {
                Treasury treasury = new Treasury();
                treasury.setTreasuryCode(rs.getString("tr_code"));
                treasury.setTreasuryName(rs.getString("tr_name"));
                treasuryList.add(treasury);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs,st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return treasuryList;
     }
    @Override
    public ArrayList getAGTreasuryList() {
        Connection con = null;
        ResultSet rs = null;
        Statement st = null;
        ArrayList treasuryList = new ArrayList();
        try {            
            con = dataSource.getConnection();
            st = con.createStatement();
            rs = st.executeQuery("select distinct ag_treasury_code from g_treasury where ag_treasury_code is not null order by ag_treasury_code asc ");
            while (rs.next()) {
                Treasury treasury = new Treasury();
                treasury.setAgtreasuryCode(rs.getString("ag_treasury_code"));
                treasury.setTreasuryName(rs.getString("ag_treasury_code"));
                treasuryList.add(treasury);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs,st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return treasuryList;
    }
    @Override
    public String  getAqTreasuryCode(String trCode) {
        Connection con = null;
        ResultSet rs = null;
        Statement st = null;
        String aqTrCode="";
        try {            
            con = dataSource.getConnection();
            st = con.createStatement();
            rs = st.executeQuery("select  ag_treasury_code from g_treasury where ag_treasury_code is not null and tr_code='"+trCode+"' ");
            while (rs.next()) {
               aqTrCode=rs.getString("ag_treasury_code");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs,st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return aqTrCode;
    }
    
    @Override
    public Treasury getTreasuryDetails(String trCode) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        Treasury trBean = null;
        try {            
            con = dataSource.getConnection();
            String trDtlsQry = "select tr_code, tr_name, off_code, ag_treasury_code, dist_code, int_treasury_id, parent_tr_code "
                    + "from g_treasury where tr_code=? ";
            pstmt = con.prepareStatement(trDtlsQry);
            pstmt.setString(1, trCode);
            rs = pstmt.executeQuery();
            if (rs.next()) {
               
                trBean = new Treasury();
                
                trBean.setTreasuryCode(rs.getString("tr_code"));
                trBean.setTreasuryName(rs.getString("tr_name"));
                trBean.setOfficeCode(rs.getString("off_code"));
                trBean.setAgtreasuryCode(rs.getString("ag_treasury_code"));
                trBean.setDistCode(rs.getString("dist_code"));
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs,pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return trBean;
    }

    @Override
    public ArrayList getTreasuryListAG(String agtrcode) {
        Connection con = null;
        ResultSet rs = null;
        Statement st = null;
        ArrayList treasuryList = new ArrayList();
        try {            
            con = dataSource.getConnection();
            st = con.createStatement();
            rs = st.executeQuery("select * from g_treasury where ag_treasury_code like '%"+agtrcode+"%' order by tr_name asc");
            while (rs.next()) {
                Treasury treasury = new Treasury();
                treasury.setTreasuryCode(rs.getString("tr_code"));
                treasury.setTreasuryName(rs.getString("tr_name"));
                treasury.setOfficeCode(rs.getString("off_code"));
                treasury.setAgtreasuryCode(rs.getString("ag_treasury_code"));
                treasuryList.add(treasury);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs,st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return treasuryList;
    }

    @Override
    public ArrayList getParentTreasuryList() {
        
        Connection con = null;
        
        PreparedStatement pst = null;
        ResultSet rs = null;
        
        Treasury treasury = null;
        ArrayList parentTreasuryList = new ArrayList();
        try{
            con = this.dataSource.getConnection();
            
            String sql = "select tr_code,tr_name from g_treasury where parent_tr_code is null order by tr_name";
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            while(rs.next()){
                treasury = new Treasury();
                treasury.setTreasuryCode(rs.getString("tr_code"));
                treasury.setTreasuryName(rs.getString("tr_name"));
                parentTreasuryList.add(treasury);
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            DataBaseFunctions.closeSqlObjects(rs,pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
       return parentTreasuryList; 
    }

    @Override
    public ArrayList getSubTreasuryList(String parentTrCode) {
        
        Connection con = null;
        
        PreparedStatement pst = null;
        ResultSet rs = null;
        
        Treasury treasury = null;
        ArrayList subTreasuryList = new ArrayList();
        try{
            con = this.dataSource.getConnection();
            
            String sql = "select tr_code,tr_name from g_treasury where parent_tr_code=? order by tr_name";
            pst = con.prepareStatement(sql);
            pst.setString(1,parentTrCode);
            rs = pst.executeQuery();
            while(rs.next()){
                treasury = new Treasury();
                treasury.setTreasuryCode(rs.getString("tr_code"));
                treasury.setTreasuryName(rs.getString("tr_name"));
                subTreasuryList.add(treasury);
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            DataBaseFunctions.closeSqlObjects(rs,pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
      return subTreasuryList;  
    }

    @Override
    public String getTreasuryName(String trcode) {
        
        Connection con = null;
        
        PreparedStatement pst = null;
        ResultSet rs = null;
        
        String trname = "";
        try{
            con = this.dataSource.getConnection();
            
            String sql = "select tr_name from g_treasury where tr_code=?";
            pst = con.prepareStatement(sql);
            pst.setString(1,trcode);
            rs = pst.executeQuery();
            if(rs.next()){
                trname = rs.getString("tr_name");
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            DataBaseFunctions.closeSqlObjects(rs,pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
      return trname;
    }
}


