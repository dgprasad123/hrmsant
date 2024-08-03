/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.dao.master;

import hrms.SelectOption;
import hrms.common.DataBaseFunctions;
import hrms.model.privilege.RoleProperty;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 *
 * @author DurgaPrasad
 */
public class RoleDAOImpl implements RoleDAO{
    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    @Override
    public ArrayList getRoleList() {
        Statement st = null;
        ArrayList al = new ArrayList();
        SelectOption so = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = this.dataSource.getConnection();
            st = con.createStatement();
            rs = st.executeQuery("SELECT role_id,role_name FROM g_role_master");
            while (rs.next()) {
                so = new SelectOption();
                so.setValue(rs.getString("role_id"));
                so.setLabel(rs.getString("role_name"));
                al.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return al;
    }

    @Override
    public void saveRole(RoleProperty roleProperty) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            con = dataSource.getConnection();
            if (roleProperty.getRoleId() == "") {
                pstmt = con.prepareStatement("INSERT INTO");
            }else{
                
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }
    
}
