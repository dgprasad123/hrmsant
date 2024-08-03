/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.master;

import hrms.common.DataBaseFunctions;
import hrms.model.master.State;
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
 * @author Manas Jena
 */
public class StateDAOImpl implements StateDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List getStateList() {
        Connection con = null;

        Statement stmt = null;
        ResultSet rs = null;

        List statelist = new ArrayList();
        try {
            con = dataSource.getConnection();
            stmt = con.createStatement();
            rs = stmt.executeQuery("SELECT * FROM g_state order by state_name asc");
            while (rs.next()) {
                State state = new State();                
                state.setStatecode(rs.getString("state_code"));
                state.setStatename(rs.getString("state_name"));
                statelist.add(state);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return statelist;
    }
@Override
    public State getStateDetails(String stateCode) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement st = null;
        State state = new State();
        try {
            con = dataSource.getConnection();
            st = con.prepareStatement("SELECT STATE_CODE,STATE_NAME FROM G_STATE WHERE STATE_CODE=? ");
            st.setString(1, stateCode);
            rs = st.executeQuery();
            if (rs.next()) {
                state.setStatecode(rs.getString("state_code"));
                state.setStatename(rs.getString("state_name"));

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return state;
    }
    
    @Override
    public void saveNewState(State  state){
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        String maxcadreCodeStr = "";
        try {
                con = dataSource.getConnection();
                int maxcadreCode = 0;

                pst = con.prepareStatement("SELECT MAX( state_code ) ::INTEGER +1 maxstatecode FROM g_state");
            
                rs = pst.executeQuery();                
                if (rs.next()) {
                    maxcadreCode = rs.getInt("maxstatecode");                 
                }
                if(maxcadreCodeStr != null){
                    pst = con.prepareStatement("INSERT INTO g_state(state_code,state_name, country) VALUES(?, ?,?)");
                    pst.setInt(1, maxcadreCode);
                    pst.setString(2, state.getStatename());
                    pst.setString(3, state.getCountryName());
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
    public void updateNewState(State  state){
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();               
            pst = con.prepareStatement("UPDATE g_state SET state_name=?,country=? where state_code=?");
           
                pst.setString(1, state.getStatename());
                pst.setString(2, state.getCountryName());
                pst.setString(3, state.getStatecode());    
                pst.executeUpdate(); 
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }    
    }
    
    @Override
    public State editState(State  state){
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = this.dataSource.getConnection();

            String sql = "select state_code,state_name,country from g_state where state_code=?";  
            pst = con.prepareStatement(sql);
            pst.setString(1,state.getStatecode());
            rs = pst.executeQuery();
            if (rs.next()) {
               state.setHidstateCode(rs.getString("state_code"));              
               state.setStatecode(rs.getString("state_code"));
               state.setStatename(rs.getString("state_name"));  
               state.setCountryName(rs.getString("country"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
       return state;
    
    }
}


