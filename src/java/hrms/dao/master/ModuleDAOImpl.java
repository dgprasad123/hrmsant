/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.master;

import hrms.common.DataBaseFunctions;
import hrms.model.master.Module;
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
public class ModuleDAOImpl implements ModuleDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public ArrayList getModuleList() {
        Connection con = null;
        ResultSet rs = null;
        Statement st = null;
        ArrayList moduleList = new ArrayList();
        try {
            con = dataSource.getConnection();
            st = con.createStatement();
            rs = st.executeQuery("SELECT * FROM G_MODULE_LINK");
            while (rs.next()) {
                Module mod = new Module();
                mod.setModid(rs.getInt("mod_id"));
                mod.setModname(rs.getString("mod_name"));
                mod.setModurl(rs.getString("url"));
                mod.setIsNew(rs.getString("is_new"));
                moduleList.add(mod);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return moduleList;
    }

    @Override
    public Module getModuleDetail(int modid) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        Module module = new Module();
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT * FROM G_MODULE_LINK WHERE MOD_ID=?");
            pstmt.setInt(1, modid);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                module.setModid(modid);
                module.setModname(rs.getString("mod_name"));
                module.setModurl(rs.getString("URL"));
                module.setEmpspecific(rs.getString("emp_specific"));
                module.setConverturl(rs.getString("convert_url"));
                module.setModserial(rs.getInt("mod_serial"));
                module.setNewwindow(rs.getString("newwindow"));
                module.setHelpurl(rs.getString("help_url"));
                module.setModuletype(rs.getString("module_type"));
                module.setHideurl(rs.getString("hide_url"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return module;
    }

    @Override
    public void saveModule(Module module) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            con = dataSource.getConnection();
            if (module.getModid() == 0) {
                int modid = 1;
                pstmt = con.prepareStatement("SELECT (MAX(MOD_ID)+1) NEW_MOD_ID FROM G_MODULE_LINK");
                rs = pstmt.executeQuery();
                if (rs.next()) {
                    modid = rs.getInt("NEW_MOD_ID");
                }
                pstmt = con.prepareStatement("INSERT INTO G_MODULE_LINK (MOD_ID,mod_name,URL,emp_specific,convert_url,mod_serial,newwindow,help_url,module_type,hide_url, is_new)VALUES(?,?,?,?,?,?,?,?,?,?,?)");
                pstmt.setInt(1, modid);
                pstmt.setString(2, module.getModname());
                pstmt.setString(3, module.getModurl());
                pstmt.setString(4, module.getEmpspecific());
                pstmt.setString(5, module.getConverturl());
                pstmt.setInt(6, module.getModserial());
                pstmt.setString(7, module.getNewwindow());
                pstmt.setString(8, module.getHelpurl());
                pstmt.setString(9, module.getModuletype());
                pstmt.setString(10, module.getHideurl());
                pstmt.setString(11, "Y");
                pstmt.executeUpdate();
            } else {
                pstmt = con.prepareStatement("UPDATE G_MODULE_LINK SET mod_name=?,URL=?,emp_specific=?,convert_url=?,mod_serial=?,newwindow=?,help_url=?,module_type=?,hide_url=? WHERE MOD_ID=?");
                pstmt.setString(1, module.getModname());
                pstmt.setString(2, module.getModurl());
                pstmt.setString(3, module.getEmpspecific());
                pstmt.setString(4, module.getConverturl());
                pstmt.setInt(5, module.getModserial());
                pstmt.setString(6, module.getNewwindow());
                pstmt.setString(7, module.getHelpurl());
                pstmt.setString(8, module.getModuletype());
                pstmt.setString(9, module.getHideurl());
                pstmt.setInt(10, module.getModid());
                pstmt.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public ArrayList getModuleList(String moduleGroupId) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        ArrayList moduleList = new ArrayList();
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT g_module_map.mod_id,mod_name FROM g_module_map INNER JOIN G_MODULE_LINK ON g_module_map.mod_id = G_MODULE_LINK.mod_id where g_module_map.mod_grp_id = ?");
            pstmt.setString(1, moduleGroupId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Module mod = new Module();
                mod.setModid(rs.getInt("mod_id"));
                mod.setModname(rs.getString("mod_name"));                
                moduleList.add(mod);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return moduleList;
    }
}
