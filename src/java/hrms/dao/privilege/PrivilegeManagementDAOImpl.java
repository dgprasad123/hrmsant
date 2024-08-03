/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.privilege;

import hrms.SelectOption;
import hrms.common.DataBaseFunctions;
import hrms.model.login.Users;
import hrms.model.master.Module;
import hrms.model.privilege.Privilege;
import hrms.model.privilege.PrivilegeBean;
import hrms.model.privilege.RoleMapping;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Surendra
 */
public class PrivilegeManagementDAOImpl implements PrivilegeManagementDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public ArrayList getAssignedPrivilageList(String spc) {
        ArrayList assignedPrivilageList = new ArrayList();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet resultset = null;
        try {
            con = this.dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT PRIV_MAP_ID,ROLE_NAME,MOD_GRP_NAME,MOD_NAME FROM g_privilege_map "
                    + "INNER JOIN G_ROLE_MASTER ON g_privilege_map.ROLE_ID =  G_ROLE_MASTER.ROLE_ID "
                    + "LEFT OUTER JOIN g_module_group ON g_privilege_map.MOD_GRP_ID = g_module_group.MOD_GRP_ID "
                    + "LEFT OUTER JOIN g_module_link ON g_privilege_map.mod_id = g_module_link.mod_id "
                    + "WHERE g_privilege_map.SPC=? ");
            pstmt.setString(1, spc);
            resultset = pstmt.executeQuery();
            while (resultset.next()) {
                Privilege privilege = new Privilege();
                privilege.setPrivmapid(resultset.getInt("PRIV_MAP_ID"));
                privilege.setRolename(resultset.getString("ROLE_NAME"));
                privilege.setModulegroup(resultset.getString("MOD_GRP_NAME"));
                privilege.setModulename(resultset.getString("MOD_NAME"));
                assignedPrivilageList.add(privilege);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return assignedPrivilageList;
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
    public ArrayList getModuleGroupList(String roleId) {
        Statement st = null;
        ArrayList al = new ArrayList();
        ResultSet rs = null;
        String sqlQuery = null;
        RoleMapping rm = null;
        Connection con = null;
        try {
            con = this.dataSource.getConnection();
            st = con.createStatement();
            if (roleId != null && !roleId.equals("")) {
                sqlQuery = "SELECT G_MODULE_GROUP.MOD_GRP_ID,G_MODULE_GROUP.MOD_GRP_NAME ,G_GROUP_MAP.MOD_GRP_ID  MAPID FROM G_MODULE_GROUP "
                        + "INNER JOIN (SELECT * FROM G_GROUP_MAP WHERE G_GROUP_MAP.ROLE_ID = '" + roleId + "')  G_GROUP_MAP "
                        + "ON G_MODULE_GROUP.MOD_GRP_ID = G_GROUP_MAP.MOD_GRP_ID ORDER BY MOD_GRP_NAME";
            } else {
                sqlQuery = "SELECT MOD_GRP_ID,MOD_GRP_NAME FROM G_MODULE_GROUP ORDER BY MOD_GRP_NAME";
            }
            rs = st.executeQuery(sqlQuery);

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

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return al;
    }

    @Override
    public String assignPrivilege(Module module, String spc, String empid) {
        Connection con = null;
        PreparedStatement pst = null;
        Statement st = null;
        ResultSet rs = null;
        int mCode = 1;
        String status = "Y";
        try {
            boolean alreadyAssigned = false;
            con = this.dataSource.getConnection();
            pst = con.prepareStatement("SELECT * FROM g_privilege_map WHERE spc=? and mod_id=? and role_id=?");
            pst.setString(1, spc);
            pst.setInt(2, module.getModid());
            pst.setString(3, module.getRoleid());
            rs = pst.executeQuery();
            if (rs.next()) {
                alreadyAssigned = true;
                status = "E";
            }
            if (alreadyAssigned == false) {
                st = con.createStatement();
                rs = st.executeQuery("select max(priv_map_id) priv_map_id from g_privilege_map");
                while (rs.next()) {
                    if (rs != null) {
                        mCode = rs.getInt("priv_map_id") + 1;
                    }
                }

                pst = con.prepareStatement("insert into g_privilege_map(priv_map_id,spc,role_id,mod_grp_id,mod_id,hrmsid) values(?,?,?,?,?,?)");
                pst.setInt(1, mCode);
                pst.setString(2, spc);
                pst.setString(3, module.getRoleid());
                pst.setString(4, module.getModgrpid());
                pst.setInt(5, module.getModid());
                pst.setString(6, empid);
                pst.executeUpdate();
            }
        } catch (Exception e) {
            status = "N";
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return status;

    }

    public String revokePrivilege(String spc, int privmapid) {
        Connection con = null;
        PreparedStatement pst = null;
        String status = "Y";
        try {
            con = this.dataSource.getConnection();
            pst = con.prepareStatement("DELETE FROM g_privilege_map WHERE spc=? and priv_map_id=?");
            pst.setString(1, spc);
            pst.setInt(2, privmapid);
            pst.execute();
        } catch (Exception e) {
            status = "N";
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return status;
    }

    @Override
    public List getUserType() {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        List li = new ArrayList();
        try {
            con = this.dataSource.getConnection();
            pst = con.prepareStatement("select usertype from user_details where usertype != 'G' GROUP BY usertype");
            rs = pst.executeQuery();
            while (rs.next()) {
                li.add(rs.getString("usertype"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst, con);
        }
        return li;
    }

    @Override
    public int saveUserDetail(Users users) {
        Connection con = null;
        PreparedStatement pstmt = null;
        int errorCode = 0;
        try {
            con = dataSource.getConnection();
            String rule15MemoQry = "INSERT INTO user_details (username,password,usertype,linkid) VALUES (?,?,?,?)";
            pstmt = con.prepareStatement(rule15MemoQry);
            pstmt.setString(1, users.getUserName());
            pstmt.setString(2, users.getUserPassword());
            pstmt.setString(3, users.getUsertype());
            pstmt.setString(4, users.getEmpId());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            errorCode = e.getErrorCode();
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return errorCode;
    }

    @Override
    public List getUserList(String userType) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        List li = new ArrayList();
        try {
            con = this.dataSource.getConnection();
            pst = con.prepareStatement("SELECT USERNAME FROM USER_DETAILS WHERE USERTYPE=? ORDER BY USERTYPE");
            pst.setString(1, userType);
            rs = pst.executeQuery();
            while (rs.next()) {
                li.add(rs.getString("USERNAME"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst, con);
        }
        return li;
    }

    @Override
    public List getAssignedPrivilageUserNameSpecificList(String userName) {
        List assignedPrivilageList = new ArrayList();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet resultset = null;
        try {
            con = this.dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT g_user_privilege_map.mod_id,mod_name,url,PRIV_MAP_ID FROM g_user_privilege_map "
                    + "INNER JOIN g_module_link ON g_user_privilege_map.mod_id = g_module_link.mod_id WHERE username=? ");
            pstmt.setString(1, userName);
            resultset = pstmt.executeQuery();
            while (resultset.next()) {
                Privilege privilege = new Privilege();
                privilege.setPrivmapid(resultset.getInt("PRIV_MAP_ID"));
                //privilege.setModulegroup(resultset.getString("MOD_GRP_NAME"));
                privilege.setModulename(resultset.getString("MOD_NAME"));
                assignedPrivilageList.add(privilege);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return assignedPrivilageList;
    }

    @Override
    public List getModuleListUserNameSpecific(String userType) {

        PreparedStatement st = null;
        ArrayList al = new ArrayList();
        SelectOption so = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = this.dataSource.getConnection();
            st = con.prepareStatement("SELECT MOD_ID,MOD_NAME FROM G_MODULE_LINK WHERE module_type=? ORDER BY MOD_NAME");
            st.setString(1, userType);
            rs = st.executeQuery();
            while (rs.next()) {
                so = new SelectOption();
                so.setValue(rs.getString("MOD_ID"));
                so.setLabel(rs.getString("MOD_NAME"));
                al.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return al;
    }

    public String assignPrivilegeUserNameSpecific(String[] modulearray, String username) {
        Connection con = null;
        PreparedStatement pst = null;
        Statement st = null;
        ResultSet rs = null;
        int mCode = 1;
        String status = "Y";
        try {
            boolean alreadyAssigned = false;
            con = this.dataSource.getConnection();

            st = con.createStatement();
            rs = st.executeQuery("select max(priv_map_id) priv_map_id from g_user_privilege_map");

            while (rs.next()) {
                if (rs != null) {
                    mCode = rs.getInt("priv_map_id");
                }
            }
            for (int i = 0; i < modulearray.length; i++) {
                mCode++;
                pst = con.prepareStatement("insert into g_user_privilege_map(priv_map_id,username,mod_id) values(?,?,?)");
                pst.setInt(1, mCode);
                pst.setString(2, username);
                pst.setInt(3, Integer.parseInt(modulearray[i]));
                pst.executeUpdate();
            }
        } catch (Exception e) {
            status = "N";
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return status;
    }

    @Override
    public String assignPrivilegeUserNameSpecific(Module module, String username) {
        Connection con = null;
        PreparedStatement pst = null;
        Statement st = null;
        ResultSet rs = null;
        int mCode = 1;
        String status = "Y";
        try {
            boolean alreadyAssigned = false;
            con = this.dataSource.getConnection();
            pst = con.prepareStatement("SELECT * FROM g_user_privilege_map WHERE username=? and mod_id=?");
            pst.setString(1, username);
            pst.setInt(2, module.getModid());
            rs = pst.executeQuery();
            if (rs.next()) {
                alreadyAssigned = true;
                status = "E";
            }
            if (alreadyAssigned == false) {
                st = con.createStatement();
                rs = st.executeQuery("select max(priv_map_id) priv_map_id from g_user_privilege_map");

                while (rs.next()) {
                    if (rs != null) {
                        mCode = rs.getInt("priv_map_id") + 1;
                    }
                }

                pst = con.prepareStatement("insert into g_user_privilege_map(priv_map_id,username,mod_grp_id,mod_id) values(?,?,?,?)");
                pst.setInt(1, mCode);
                pst.setString(2, username);
                pst.setString(3, module.getModgrpid());
                pst.setInt(4, module.getModid());
                pst.executeUpdate();
            }
        } catch (Exception e) {
            status = "N";
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return status;

    }

    @Override
    public String revokePrivilageUserNameSpecific(String username, int privmapid) {
        Connection con = null;
        PreparedStatement pst = null;
        String status = "Y";
        try {
            con = this.dataSource.getConnection();
            pst = con.prepareStatement("DELETE FROM g_user_privilege_map WHERE username=? and priv_map_id=?");
            pst.setString(1, username);
            pst.setInt(2, privmapid);
            pst.execute();
        } catch (Exception e) {
            status = "N";
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return status;
    }

    @Override
    public ArrayList getAllAssignedPrivilegeUserList(String offCode) {
        ArrayList assignedPrivilagesList = new ArrayList();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet resultset = null;
        Statement stmt = null;

        try {
            con = this.dataSource.getConnection();
            stmt = con.createStatement();
            if (offCode != null && !offCode.equals("")) {
                String sql = ("SELECT DISTINCT G_PRIVILEGE_MAP.SPC,EMP_ID,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') FULL_NAME,GPF_NO,SPN FROM G_PRIVILEGE_MAP \n"
                        + "LEFT OUTER JOIN EMP_MAST ON G_PRIVILEGE_MAP.SPC=EMP_MAST.CUR_SPC\n"
                        + "LEFT OUTER JOIN G_SPC ON G_PRIVILEGE_MAP.SPC=G_SPC.SPC\n"
                        + "WHERE G_PRIVILEGE_MAP.SPC LIKE '" + offCode + "%'");
                pstmt = con.prepareStatement(sql);
                resultset = pstmt.executeQuery();
            }
            while (resultset.next()) {
                Privilege privilege = new Privilege();
                privilege.setEmpid(StringUtils.defaultString(resultset.getString("EMP_ID")));
                privilege.setEmpname(StringUtils.defaultString(resultset.getString("FULL_NAME")));
                privilege.setGpfno(StringUtils.defaultString(resultset.getString("GPF_NO")));
                privilege.setPrivspc(resultset.getString("SPC"));
                privilege.setPrivspn(resultset.getString("SPN"));
                assignedPrivilagesList.add(privilege);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
            DataBaseFunctions.closeSqlObjects(pstmt);
        }
        return assignedPrivilagesList;
    }

    @Override
    public int revokeUserPrivilege(String privspc) {
        Connection con = null;
        PreparedStatement pst = null;
        int status = 0;
        try {
            con = this.dataSource.getConnection();
            pst = con.prepareStatement("DELETE FROM G_PRIVILEGE_MAP WHERE SPC=? ");
            pst.setString(1, privspc);
            status = pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return status;
    }

    @Override
    public Module[] getUserPrivileageList(String username) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet result = null;
        List<Module> modulelist = new ArrayList<>();
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("SELECT priv_map_id,g_user_privilege_map.mod_id,mod_name,url FROM g_user_privilege_map INNER JOIN g_module_link ON g_user_privilege_map.mod_id = g_module_link.mod_id WHERE username=? order by mod_serial");
            pst.setString(1, username);
            result = pst.executeQuery();
            while (result.next()) {
                Module module = new Module();
                module.setPrivmapid(result.getInt("priv_map_id"));
                module.setModid(result.getInt("mod_id"));
                module.setModname(result.getString("mod_name"));
                module.setModurl(result.getString("url"));
                modulelist.add(module);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(result, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        Module moduleArray[] = modulelist.toArray(new Module[modulelist.size()]);
        return moduleArray;
    }

    @Override
    public Module[] getPrivileageList(String moduleType) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet result = null;
        List<Module> modulelist = new ArrayList<>();
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("select * from g_module_link where module_type=?");
            pst.setString(1, moduleType);
            result = pst.executeQuery();
            while (result.next()) {
                Module module = new Module();
                module.setModid(result.getInt("mod_id"));
                module.setModname(result.getString("mod_name"));
                module.setModurl(result.getString("url"));
                module.setEmpspecific(result.getString("emp_specific"));
                module.setModserial(result.getInt("mod_serial"));
                module.setNewwindow(result.getString("newwindow"));
                modulelist.add(module);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(result, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        Module moduleArray[] = modulelist.toArray(new Module[modulelist.size()]);
        return moduleArray;
    }

    @Override
    public void revokeUserPrivilege(int privMapId) {

        ResultSet rs = null;
        boolean flag = false;
        PreparedStatement pst = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();

            pst = con.prepareStatement("delete from g_user_privilege_map where priv_map_id=?");
            pst.setInt(1, privMapId);
            pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public ArrayList getGroupWisePrivilegeList(String spc) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        PrivilegeBean so = null;
        ArrayList abstractModuleList = new ArrayList();
        try {
            con = this.dataSource.getConnection();

            //String sql = "select * from module_group_abstarct order by group_name";
            String sql = "select * from module_group_abstarct" +
                         " left outer join (" +
                         " select abstract_group_id from module_assign2_group_abstract" +
                         " inner join (select * from g_privilege_map where spc=? and g_privilege_map.role_id='05') g_privilege_map" +
                         " on module_assign2_group_abstract.mod_id=g_privilege_map.mod_id group by abstract_group_id)temp" +
                         " on module_group_abstarct.group_id=temp.abstract_group_id";
            pst = con.prepareStatement(sql);
            pst.setString(1,spc);
            rs = pst.executeQuery();
            while (rs.next()) {
                so = new PrivilegeBean();
                so.setAbstractGroupId(rs.getString("group_id"));
                so.setAbstractGroupName(rs.getString("group_name"));
                so.setAssignedGroupId(rs.getString("abstract_group_id"));
                abstractModuleList.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return abstractModuleList;
    }

    @Override
    public String assignGroupWisePrivilege(String checkedAbstractModule, String spc,String empId) {
        Connection con = null;

        PreparedStatement selectpst = null;
        ResultSet selectrs = null;

        PreparedStatement insertpst = null;

        PreparedStatement maxIdpst = null;
        ResultSet maxIdrs = null;

        PreparedStatement deletepst = null;

        int mCode = 1;
        
        String status = "Y";
        try {
            con = this.dataSource.getConnection();


            String deletesql = "delete from g_privilege_map where spc=? and role_id='05'";
            deletepst = con.prepareStatement(deletesql);

            String selectsql = "select * from module_assign2_group_abstract where abstract_group_id=?";
            selectpst = con.prepareStatement(selectsql);

            String insertsql = "insert into g_privilege_map(priv_map_id,spc,role_id,mod_grp_id,mod_id,hrmsid) values(?,?,?,?,?,?)";
            insertpst = con.prepareStatement(insertsql);

            String maxIdsql = "select max(priv_map_id) priv_map_id from g_privilege_map";
            maxIdpst = con.prepareStatement(maxIdsql);

            String[] checkedAbstractModuleArr = checkedAbstractModule.split(",");
            if (checkedAbstractModuleArr != null && checkedAbstractModuleArr.length > 0) {
                deletepst.setString(1,spc);
                deletepst.executeUpdate();
                for (int i = 0; i < checkedAbstractModuleArr.length; i++) {
                    int abstractId = Integer.parseInt(checkedAbstractModuleArr[i]);
                    selectpst.setInt(1, abstractId);
                    selectrs = selectpst.executeQuery();
                    while (selectrs.next()) {

                        maxIdrs = maxIdpst.executeQuery();
                        if (maxIdrs.next()) {
                            if (maxIdrs != null) {
                                mCode = maxIdrs.getInt("priv_map_id") + 1;
                            }
                        }

                        String modGrpId = selectrs.getString("mod_grp_id");
                        int modId = selectrs.getInt("mod_id");

                        insertpst.setInt(1, mCode);
                        insertpst.setString(2, spc);
                        insertpst.setString(3, "05");
                        insertpst.setString(4, modGrpId);
                        insertpst.setInt(5, modId);
                        insertpst.setString(6, empId);
                        insertpst.executeUpdate();
                    }
                }
            }
        } catch (Exception e) {
            status = "N";
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(selectrs, selectpst);
            DataBaseFunctions.closeSqlObjects(con);
        }
      return status;  
    }

    @Override
    public String revokeGroupWisePrivilege(String checkedAbstractModule, String spc) {
        
        Connection con = null;

        PreparedStatement selectpst = null;
        ResultSet selectrs = null;

        PreparedStatement deletepst = null;

        String status = "R";
        try {
            con = this.dataSource.getConnection();


            String deletesql = "delete from g_privilege_map where spc=? and role_id='05' and mod_grp_id=? and mod_id=?";
            deletepst = con.prepareStatement(deletesql);
            
            String selectsql = "select * from module_assign2_group_abstract where abstract_group_id=?";
            selectpst = con.prepareStatement(selectsql);
            
            String[] checkedAbstractModuleArr = checkedAbstractModule.split(",");
            if (checkedAbstractModuleArr != null && checkedAbstractModuleArr.length > 0) {
                
                for (int i = 0; i < checkedAbstractModuleArr.length; i++) {
                    int abstractId = Integer.parseInt(checkedAbstractModuleArr[i]);
                    selectpst.setInt(1, abstractId);
                    selectrs = selectpst.executeQuery();
                    while (selectrs.next()) {

                        String modGrpId = selectrs.getString("mod_grp_id");
                        int modId = selectrs.getInt("mod_id");
                        
                        deletepst.setString(1, spc);
                        deletepst.setString(2, modGrpId);
                        deletepst.setInt(3, modId);
                        deletepst.executeUpdate();
                    }
                }
            }
        } catch (Exception e) {
            status = "N";
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(selectrs, selectpst);
            DataBaseFunctions.closeSqlObjects(con);
        }
      return status;
    }

    @Override
    public ArrayList getGroupWisePrivilegeAssignedList(String spc) {
        
        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;
        
        ArrayList assignedList = new ArrayList();
        SelectOption so = null;
        try{
            con = this.dataSource.getConnection();
            
            String sql = "select module_assign2_group_abstract.abstract_group_id from g_privilege_map" +
                         " left outer join module_assign2_group_abstract on g_privilege_map.mod_id=module_assign2_group_abstract.mod_id" +
                         " where spc=? and g_privilege_map.role_id='05' and g_privilege_map.mod_grp_id is not null" +
                         " group by module_assign2_group_abstract.abstract_group_id";
            pst = con.prepareStatement(sql);
            pst.setString(1,spc);
            rs = pst.executeQuery();
            while(rs.next()){
                so = new SelectOption();
                so.setValue(rs.getString("abstract_group_id"));
                assignedList.add(so);
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
       return assignedList; 
    }
}
