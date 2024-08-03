/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.master;

import hrms.common.DataBaseFunctions;
import hrms.model.master.SubDivision;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 *
 * @author manisha
 */
public class SubDivisionDAOImpl implements SubDivisionDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List getSubDivisionList() {
        Connection con = null;

        Statement stmt = null;
        ResultSet rs = null;

        List subdivisionlist = new ArrayList();
        try {
            con = dataSource.getConnection();
            stmt = con.createStatement();
            rs = stmt.executeQuery("SELECT * FROM g_sub_division");
            while (rs.next()) {
                SubDivision subdivision = new SubDivision();
                subdivision.setSubDivisionCode(rs.getString("sub_division_code"));
                subdivision.setSubDivisionName(rs.getString("sub_division_name"));
                subdivisionlist.add(subdivision);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return subdivisionlist;
    }

    @Override
    public SubDivision getSubDivisionDetail(String subDivisionCode) {

        Connection con = null;
        ResultSet rs = null;
        PreparedStatement st = null;
        SubDivision subdivision = new SubDivision();
        try {
            con = dataSource.getConnection();
            st = con.prepareStatement("SELECT sub_division_code,sub_division_name FROM g_sub_division WHERE sub_division_code=? ");
            st.setString(1, subDivisionCode);
            rs = st.executeQuery();
            if (rs.next()) {
                subdivision.setSubDivisionCode(rs.getString("sub_division_code"));
                subdivision.setSubDivisionName(rs.getString("sub_division_name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return subdivision;
    }

}
