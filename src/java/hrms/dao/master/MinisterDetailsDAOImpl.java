/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.master;

import hrms.SelectOption;
import hrms.common.DataBaseFunctions;
import hrms.dao.master.MinisterDetailsDAO;
import hrms.model.master.Department;
import hrms.model.master.MinisterDetails;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 *
 * @author Devi
 */
public class MinisterDetailsDAOImpl implements MinisterDetailsDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    
    
    
    public List getDeptListCode(int hidlmid){
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List lmidwisedeptList = new ArrayList();
         try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("select department_name,department_code from g_department where lmid=?");
             pstmt.setInt(1, hidlmid);
            rs = pstmt.executeQuery();
             while (rs.next()) { 
                MinisterDetails lmidwisedeptcode = new MinisterDetails();              
                lmidwisedeptcode.setDeptname(rs.getString("department_name"));
                lmidwisedeptcode.setDeptcode(rs.getString("department_code"));
                lmidwisedeptList.add(lmidwisedeptcode);
            }      
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return lmidwisedeptList;
         
    }
    
    public String getMultipleDeptList(int hidlmid){
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String departmentName = "";     
         try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("select department_name from g_department where lmid=?");
            pstmt.setInt(1, hidlmid);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                if(departmentName.equals("")){
                    departmentName = rs.getString("department_name");
                }
                else{
                departmentName = departmentName+" , "+rs.getString("department_name");              
                }
            }           
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return departmentName;
    
    }
    
    public String getExistingUserid(MinisterDetails mindetailsForm){
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;            
        String msg = "Y";
   
         try {
            int recordFound = 0;
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT COUNT(*) AS CNT FROM user_details WHERE username = ?");
            pstmt.setString(1, mindetailsForm.getUserid());            
            rs = pstmt.executeQuery();  
            
            if (rs.next()) {
                recordFound = rs.getInt("CNT");  
            }
            DataBaseFunctions.closeSqlObjects(pstmt);
             if (recordFound == 0) {
                  msg = "Y";  
             }
             else{
                    msg = "D";       
             }         
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }      
     return msg;  
    }
    
    
    
    
    @Override
    public List getMinisterDetailsList(String officiatingId) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List MinisterDetailsList = new ArrayList();

        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("select ARRAY_TO_STRING(ARRAY[initials, fname, mname,lname], ' ') minfullname,mobile,off_as,la_members.lmid, userdetail.username uid,userdetail.password pwd,\n"
                    + "goffice.off_name ministertype, la_members.active\n"
                    + "from (select * from la_members where off_as= ?) la_members\n"
                    + "left outer join g_officiating goffice on la_members.off_as=goffice.off_id\n"                
                    + "inner join user_details userdetail on la_members.lmid=userdetail.linkid::INTEGER and usertype='M' ");
            pstmt.setString(1, officiatingId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
 
                MinisterDetails detailofminister = new MinisterDetails();                  
                detailofminister.setFullname(rs.getString("minfullname"));
                detailofminister.setMobileno(rs.getString("mobile"));
                detailofminister.setUserid(rs.getString("uid"));
                detailofminister.setPassword(rs.getString("pwd"));
                detailofminister.setMinistertype(rs.getString("ministertype"));
                detailofminister.setActivemember(rs.getString("active"));
                String newdeptName = getMultipleDeptList (rs.getInt("lmid"));
                detailofminister.setDeptname(newdeptName);
                detailofminister.setHidlmid(rs.getInt("lmid"));
                //detailofminister.setDeptcode(rs.getString("deptcode"));
                MinisterDetailsList.add(detailofminister);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return MinisterDetailsList;
    }

    @Override
    public String saveMinisterDetails(MinisterDetails mindetailsForm) {
        PreparedStatement pst = null;
        Connection con = null;      
        ResultSet rs = null;
        String msg = "Y";
        try {                  
            con = dataSource.getConnection();  
                int maxlmid = 0;
            pst = con.prepareStatement("SELECT MAX( lmid )+1 maxlmid FROM la_members");
                rs = pst.executeQuery();                
                if (rs.next()) {
                    maxlmid = rs.getInt("maxlmid");
                }
            pst = con.prepareStatement("INSERT INTO la_members(off_as,lmid, initials, fname, mname, lname, userid, pwd, mobile, active) VALUES(?, ?,?,?,?,?,?,?,?,?)");
                      
                pst.setString(1, mindetailsForm.getHidoff_as());
                pst.setInt(2, maxlmid);
                pst.setString(3, mindetailsForm.getInitial());
                pst.setString(4, mindetailsForm.getFname().toUpperCase());
                pst.setString(5, mindetailsForm.getMnane().toUpperCase());
                pst.setString(6, mindetailsForm.getLname().toUpperCase());
                pst.setString(7, mindetailsForm.getUserid());
                pst.setString(8, mindetailsForm.getPassword());
                pst.setString(9, mindetailsForm.getMobileno());
                pst.setString(10, mindetailsForm.getActivemember());
                pst.executeUpdate();
   
            pst = con.prepareStatement("INSERT INTO user_details(username, password,linkid,usertype,enable,accountnonexpired,accountnonlocked,credentialsnonexpired) VALUES(?,?,?,?,?,?,?,?)");           
                pst.setString(1, mindetailsForm.getUserid());
                pst.setString(2, mindetailsForm.getPassword());
                pst.setInt(3, maxlmid);
                pst.setString(4, "M"); 
                pst.setInt(5, 1); 
                pst.setInt(6, 1); 
                pst.setInt(7, 1); 
                pst.setInt(8, 1); 
                pst.executeUpdate();
                
               
             if (mindetailsForm.getDeptname() != null && mindetailsForm.getDeptname().contains(",")) {
                String[] deptNameArr = mindetailsForm.getDeptname().split(",");
               
                for (int i = 0; i < deptNameArr.length; i++) {
                    pst = con.prepareStatement("UPDATE g_department SET lmid=? where  department_code =?");
                    pst.setInt(1, maxlmid);
                    pst.setString(2, deptNameArr[i]);
                    pst.executeUpdate();
                }
            } else {
                pst = con.prepareStatement("UPDATE g_department SET lmid=? where  department_code =?");
                pst.setInt(1, maxlmid);
                pst.setString(2, mindetailsForm.getDeptname());
                pst.executeUpdate();
            }                 
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return msg;
    }
    
    @Override
    public void updateMinisterDetails(MinisterDetails mindetailsForm) {
        int n = 0;
        PreparedStatement pst = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            //update only la member data
            pst = con.prepareStatement("UPDATE la_members SET initials=?, fname=?, mname=?,lname=?, userid=?, pwd=?, mobile=?, active=? WHERE lmid=?");
        
            pst.setString(1, mindetailsForm.getInitial());
            pst.setString(2, mindetailsForm.getFname().toUpperCase());
            pst.setString(3, mindetailsForm.getMnane().toUpperCase());
            pst.setString(4, mindetailsForm.getLname().toUpperCase());
            pst.setString(5, mindetailsForm.getUserid());
            pst.setString(6, mindetailsForm.getPassword());
            pst.setString(7, mindetailsForm.getMobileno());
            pst.setString(8, mindetailsForm.getActivemember());
           // pst.setString(9, mindetailsForm.getHidoff_as());    
            pst.setInt(9, mindetailsForm.getHidlmid());
            n = pst.executeUpdate();
            
            //update la member data with minister type
            pst = con.prepareStatement("UPDATE la_members SET initials=?, fname=?, mname=?,lname=?, userid=?, pwd=?, mobile=?, active=?, off_as=? WHERE lmid=?");
        
            pst.setString(1, mindetailsForm.getInitial());
            pst.setString(2, mindetailsForm.getFname());
            pst.setString(3, mindetailsForm.getMnane());
            pst.setString(4, mindetailsForm.getLname());
            pst.setString(5, mindetailsForm.getUserid());
            pst.setString(6, mindetailsForm.getPassword());
            pst.setString(7, mindetailsForm.getMobileno());
            pst.setString(8, mindetailsForm.getActivemember());
            pst.setString(9, mindetailsForm.getOff_as());    
            pst.setInt(10, mindetailsForm.getHidlmid());
            n = pst.executeUpdate();
            
            //set previous dept 0 for lmid 
            pst = con.prepareStatement("UPDATE g_department SET lmid=0 where  lmid =?");                    
            pst.setInt(1, mindetailsForm.getHidlmid());           
            pst.executeUpdate();
           
            
            //update dept of member         
             if (mindetailsForm.getDeptname() != null && mindetailsForm.getDeptname().contains(",")) {
                String[] deptNameArr = mindetailsForm.getDeptname().split(",");
               
                for (int i = 0; i < deptNameArr.length; i++) {
                    pst = con.prepareStatement("UPDATE g_department SET lmid=? where  department_code =?");
                    pst.setInt(1, mindetailsForm.getHidlmid());    
                    pst.setString(2, deptNameArr[i]);
                    pst.executeUpdate();
                }
            } else {
                pst = con.prepareStatement("UPDATE g_department SET lmid=? where  department_code =?");
                pst.setInt(1, mindetailsForm.getHidlmid());    
                pst.setString(2, mindetailsForm.getDeptname());
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
    public MinisterDetails editMinisterDetails(MinisterDetails mindetailsForm) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;      
        try {
            con = dataSource.getConnection();
                     
            String name = null;
                pstmt = con.prepareStatement("select ARRAY_TO_STRING(ARRAY[initials, fname, mname,lname], ' ') minfullname from la_members where lmid=?");
                 pstmt.setInt(1, mindetailsForm.getHidlmid());
                rs = pstmt.executeQuery();
                
                if (rs.next()) {
                    name = rs.getString("minfullname");                 
                }

                pstmt = con.prepareStatement("select la_members.lmid, initials, fname, mname,lname,mobile, active, off_as, la_members.lmid,goffice.off_id ministertype,"
                        + " goffice.off_name ministertypename,userdetail.username uid,userdetail.password pwd "
                        + " from la_members "
                        + " left outer join g_officiating goffice on la_members.off_as=goffice.off_id "
                      
                        + " left outer join user_details userdetail on la_members.lmid=userdetail.linkid::INTEGER and usertype='M' "
                        + " where la_members.lmid=? and userdetail.username=? ");

                pstmt.setInt(1, mindetailsForm.getHidlmid());              
                pstmt.setString(2, mindetailsForm.getUserid());              
                rs = pstmt.executeQuery();

                if (rs.next()) {
                    mindetailsForm = new MinisterDetails();
                    mindetailsForm.setFullname(name);
                    mindetailsForm.setHidlmid(rs.getInt("lmid"));
                    mindetailsForm.setInitial(rs.getString("initials"));
                    mindetailsForm.setFname(rs.getString("fname"));
                    mindetailsForm.setMnane(rs.getString("mname"));
                    mindetailsForm.setLname(rs.getString("lname"));
                    mindetailsForm.setMobileno(rs.getString("mobile"));
                    mindetailsForm.setActivemember(rs.getString("active"));
                    mindetailsForm.setUserid(rs.getString("uid"));
                    mindetailsForm.setPassword(rs.getString("pwd"));
                    mindetailsForm.setOff_as(rs.getString("ministertype"));
                    List editdeptName = getDeptListCode (mindetailsForm.getHidlmid());
                    mindetailsForm.setAssigneddeptlist(editdeptName);                               
                    mindetailsForm.setMinistertype(rs.getString("ministertypename"));             
                }
     
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return mindetailsForm;
    }

}
