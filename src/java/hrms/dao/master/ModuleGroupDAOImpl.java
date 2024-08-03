/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.dao.master;

import hrms.common.DataBaseFunctions;
import hrms.model.privilege.ModuleGroup;
import hrms.model.privilege.RoleMapping;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 *
 * @author DurgaPrasad
 */
public class ModuleGroupDAOImpl implements ModuleGroupDAO{
    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    @Override
    public ArrayList getModuleGroupList(String roleId) {
        PreparedStatement pstmt = null;
        ArrayList al = new ArrayList();
        ResultSet rs = null;
        String sqlQuery = null;
        RoleMapping rm = null;
        Connection con = null;
        try {
            con = this.dataSource.getConnection();
            
            if (roleId != null && !roleId.equals("")) {
                sqlQuery = "SELECT G_MODULE_GROUP.MOD_GRP_ID,G_MODULE_GROUP.MOD_GRP_NAME ,G_GROUP_MAP.MOD_GRP_ID  MAPID FROM G_MODULE_GROUP "
                        + "LEFT OUTER JOIN (SELECT * FROM G_GROUP_MAP WHERE G_GROUP_MAP.ROLE_ID = ?)  G_GROUP_MAP "
                        + "ON G_MODULE_GROUP.MOD_GRP_ID = G_GROUP_MAP.MOD_GRP_ID ORDER BY MOD_GRP_NAME";
                pstmt = con.prepareStatement(sqlQuery);
                pstmt.setString(1, roleId);
            } else {
                sqlQuery = "SELECT MOD_GRP_ID,MOD_GRP_NAME FROM G_MODULE_GROUP ORDER BY MOD_GRP_NAME";
                pstmt = con.prepareStatement(sqlQuery);
            }
            rs = pstmt.executeQuery(sqlQuery);

            while (rs.next()) {
                rm = new RoleMapping();
                rm.setModId(rs.getString("MOD_GRP_ID"));
                rm.setModGrpName(rs.getString("MOD_GRP_NAME"));
                if (roleId != null && !roleId.equals("")) {
                    rm.setModGrpMap(rs.getString("MAPID"));
                } else {
                    rm.setModGrpMap("");
                }
                al.add(rm);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return al;
    }

    @Override
    public void saveModuleGroup(ModuleGroup moduleGroup) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
