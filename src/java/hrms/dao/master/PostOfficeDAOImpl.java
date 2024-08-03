/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.master;

import hrms.common.DataBaseFunctions;
import hrms.model.master.PostOffice;
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
public class PostOfficeDAOImpl implements PostOfficeDAO {
       @Resource(name = "dataSource")
    protected DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
        @Override
     public ArrayList getPostOfficeList(String distCode){
         Connection con = null;
        ResultSet rs = null;
        PreparedStatement st = null;
        ArrayList poList = new ArrayList();
        PostOffice po=null;
        try {
            con = dataSource.getConnection();
            st = con.prepareStatement("SELECT PO_CODE,PO_NAME,PIN FROM G_PO WHERE DIST_CODE=? ORDER BY PO_NAME");
            st.setString(1, distCode);
            rs = st.executeQuery();
            while(rs.next()){
                po=new PostOffice();
                po.setPostOfficeCode(rs.getString("PO_CODE"));
                po.setPostOfficeName(rs.getString("PO_NAME"));
                po.setPinCode(rs.getString("PIN"));             
                poList.add(po);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
       return poList;
    }
     
     @Override
     public PostOffice  editPostOffice(PostOffice  postoff){
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = this.dataSource.getConnection();

            String sql = "SELECT po_code,po_name,pin,gdist.dist_code ,gst.state_code\n"
                    + "FROM (select * from g_po where po_code=?)g_po\n"
                    + "left outer join g_district gdist on g_po.dist_code=gdist.dist_code\n"
                    + "left outer join g_state gst on gdist.state_code=gst.state_code";
            pst = con.prepareStatement(sql);
            pst.setString(1,postoff.getPostOfficeCode());
            rs = pst.executeQuery();
            if (rs.next()) {
                
               postoff.setHidPoCode(rs.getString("po_code"));                      
               postoff.setPostOfficeCode(rs.getString("po_code"));
               postoff.setPostOfficeName(rs.getString("po_name")); 
               postoff.setPinCode(rs.getString("pin")); 
               postoff.setDistCode(rs.getString("dist_code"));
               postoff.setStateCode(rs.getString("state_code"));              
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
       return postoff; 
     }
     
     @Override
     public void saveNewPostOffice(PostOffice  postoff){
          Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null; 
        String maxpoCodeStr = null;
        try {
                con = dataSource.getConnection();
               
                int recordFound = 0;
                pst = con.prepareStatement("SELECT COUNT(po_code) as count_po FROM g_po where dist_code=?");
                    pst.setString(1, postoff.getHiddistCode()); 
                    rs = pst.executeQuery(); 
                if (rs.next()) {
                    recordFound = rs.getInt("count_po");  
                   }
                 
                if(recordFound==0){
                    String distCode= postoff.getHiddistCode();
                    maxpoCodeStr = distCode + "0001" ;
                    
                    pst = con.prepareStatement("INSERT INTO g_po(po_code,dist_code, po_name,pin) VALUES(?,?,?,?)");
                    pst.setString(1, maxpoCodeStr);                  
                    pst.setString(2, postoff.getHiddistCode());
                    pst.setString(3, postoff.getPostOfficeName().toUpperCase());  
                    pst.setString(4, postoff.getPinCode()); 
                    pst.executeUpdate(); 
                    
                }else{
                    pst = con.prepareStatement("SELECT MAX( po_code ) ::INTEGER +1 maxpocode FROM g_po where dist_code=?");
                    pst.setString(1, postoff.getHiddistCode());
                    rs = pst.executeQuery();                
                if (rs.next()) {
                    maxpoCodeStr = rs.getString("maxpocode");                 
                }
                    pst = con.prepareStatement("INSERT INTO g_po(po_code,dist_code, po_name,pin) VALUES(?,?,?,?)");
                    pst.setString(1, maxpoCodeStr);                  
                    pst.setString(2, postoff.getHiddistCode());
                    pst.setString(3, postoff.getPostOfficeName().toUpperCase());  
                    pst.setString(4, postoff.getPinCode()); 
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
     public void updatePostOffice(PostOffice  postoff){
         Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();               
            pst = con.prepareStatement("UPDATE g_po SET po_name=?,pin=? where po_code=?");          
                pst.setString(1, postoff.getPostOfficeName().toUpperCase());               
                pst.setString(2, postoff.getPinCode());   
                pst.setString(3, postoff.getPostOfficeCode());   
                
                pst.executeUpdate(); 
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }    
     
     
     }
     
}
