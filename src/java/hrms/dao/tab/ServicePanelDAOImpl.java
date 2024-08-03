/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.tab;

import hrms.common.DataBaseFunctions;
import hrms.model.login.Users;
import hrms.model.tab.ModuleGroup;
import hrms.model.tab.ModuleProperty;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 *
 * @author Surendra
 */
public class ServicePanelDAOImpl implements ServicePanelDAO {

    @Resource(name = "repodataSource")
    protected DataSource repodataSource;

    public void setRepodataSource(DataSource repodataSource) {
        this.repodataSource = repodataSource;
    }

    @Override
    public hrms.model.login.Users getSelectedEmployeeInfo(String employeeId) {
        Users emp = new Users();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        String sql = "";
        try {
            con = this.repodataSource.getConnection();

            ps = con.prepareStatement("SELECT emp_mast.cur_off_code,emp_mast.emp_id,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME, L_NAME], ' ') EMPNAME,\n"
                    + "post_nomenclature, gpost.post, deploy_type, pay_helduptype, gpf_no, g_cadre.CADRE_NAME, emp_mast.is_regular,\n"
                    + "acct_type, if_gpf_assumed,dep_code, department_name, g_department.department_code, emp_mast.post_grp_type,emp_mast.is_regular,\n"
                    + "emp_mast.cur_basic_salary, emp_mast.pay_commission, joindate_of_goo,deputation_offcode,deputation_spc,gpost1.post deputationPost,\n"
                    + "if_reengaged,emp_ret_res.retired,if_retired  from emp_mast\n"
                    + "inner join g_deploy_type G ON emp_mast.dep_code=G.deploy_code\n"
                    + "left outer join g_spc on emp_mast.cur_spc=g_spc.spc\n"
                    + "left outer join g_department on g_spc.dept_code=g_department.department_code\n"
                    + "left outer join g_post gpost on g_spc.gpc=gpost.post_code\n"
                    + "left outer join (select emp_id, 'PAY HELD UP' pay_helduptype from emp_pay_heldup  where to_date is null and emp_id=? limit 1 ) \n"
                    + "emp_pay_heldup on emp_mast.emp_id=emp_pay_heldup.emp_id\n"
                    + "left outer join g_cadre on emp_mast.cur_cadre_code=g_cadre.cadre_code\n"
                    + "left outer join g_post gpost1 on substr(deputation_spc,14,6)=gpost1.post_code\n"
                    + "left outer join emp_ret_res on emp_mast.emp_id=emp_ret_res.emp_id \n"
                    + "where emp_mast.emp_id=?");
            ps.setString(1, employeeId);
            ps.setString(2, employeeId);
            rs = ps.executeQuery();
            if (rs.next()) {
                emp.setEmpId(rs.getString("emp_id"));
                emp.setFullName(rs.getString("EMPNAME"));

                if (rs.getString("is_regular") != null && !rs.getString("is_regular").equals("")) {
                    if (rs.getString("is_regular").equals("N") || rs.getString("is_regular").equals("D")) {
                        emp.setPostname(rs.getString("post_nomenclature"));
                    } else {
                        if (rs.getString("dep_code").equals("07")) {
                            if (rs.getString("deputationPost") != null && !rs.getString("deputationPost").equals("")) {
                                emp.setPostname(rs.getString("deputationPost"));
                            }
                        } else {
                            emp.setPostname(rs.getString("post"));
                        }
                    }

                } else {
                    emp.setPostname("");
                }
                emp.setDepstatus(rs.getString("deploy_type"));
                if (rs.getString("pay_helduptype") != null && !rs.getString("pay_helduptype").equals("")) {
                    emp.setPayheldupstatus("( " + rs.getString("pay_helduptype") + " )");
                }
                emp.setCadrename(rs.getString("cadre_name"));
                if (rs.getString("is_regular").equalsIgnoreCase("N")) {
                    emp.setPayCommission("");
                } else {
                    emp.setPayCommission(rs.getString("pay_commission"));
                }
                emp.setGpfno(rs.getString("gpf_no"));
                emp.setAcctType(rs.getString("acct_type"));

                emp.setIfgpfAssumed(rs.getString("if_gpf_assumed"));
                emp.setDeptName(rs.getString("department_name"));
                emp.setDeptcode(rs.getString("department_code"));

                emp.setCurBasic(rs.getDouble("cur_basic_salary"));
                emp.setUsertype(rs.getString("is_regular"));
                emp.setJoinDateGoo(rs.getDate("joindate_of_goo"));
                emp.setOffcode(rs.getString("cur_off_code"));
                emp.setIsRegular(rs.getString("is_regular"));
                emp.setPostgrp(rs.getString("post_grp_type"));
                emp.setIfReengaged(rs.getString("if_reengaged"));
                emp.setIfRetired(rs.getString("if_retired"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs,ps);
            DataBaseFunctions.closeSqlObjects(con);
        }

        return emp;
    }

    @Override
    public ArrayList getRollWiseGrpInfo(String rolid, String spc, String ifEmpClicked, boolean isddo, String employeeTypeLink) {
        Connection con = null;
        ArrayList roleListarr = new ArrayList();
        ArrayList moduleList = new ArrayList();
        ModuleGroup mg = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            con = this.repodataSource.getConnection();
            st = con.createStatement();
            String sqlStr = "";
            String criteriaForEmployeeType = "";

            if (employeeTypeLink != null && !employeeTypeLink.equals("")) {
                if (employeeTypeLink.equalsIgnoreCase("ALL") || employeeTypeLink.equalsIgnoreCase("NA")) {
                    criteriaForEmployeeType = "";
                } else if (employeeTypeLink.equalsIgnoreCase("R")) {
                    criteriaForEmployeeType = " and (G_MODULE_LINK.employee_type='R' or G_MODULE_LINK.employee_type='B') ";
                } else if (employeeTypeLink.equalsIgnoreCase("C")) {
                    criteriaForEmployeeType = " and (G_MODULE_LINK.employee_type='C' or G_MODULE_LINK.employee_type='B' or G_MODULE_LINK.mod_id in (322,74)) ";
                }
            }

            if (rolid != null && (rolid.equals("06") || rolid.equals("09"))) {
                sqlStr = "SELECT ROLE_ID,GRPMOD.MOD_GRP_ID,ROLLMODGRP.MOD_GRP_NAME MODGRPNAME,GRPMOD.MOD_ID,GRPMOD.MOD_NAME,URL,HELP_URL,EMP_SPECIFIC,SERIAL,CONVERT_URL,MOD_SERIAL,NEWWINDOW, IS_NEW  FROM (SELECT G_GROUP_MAP.ROLE_ID,G_MODULE_GROUP.MOD_GRP_ID,G_MODULE_GROUP.MOD_GRP_NAME,SERIAL FROM G_GROUP_MAP INNER JOIN G_MODULE_GROUP ON G_GROUP_MAP.MOD_GRP_ID = G_MODULE_GROUP.MOD_GRP_ID WHERE ROLE_ID='" + rolid + "')  ROLLMODGRP  INNER JOIN  (SELECT distinct G_MODULE_MAP.MOD_GRP_ID,G_MODULE_LINK.MOD_ID,MOD_NAME,URL,HELP_URL,EMP_SPECIFIC,CONVERT_URL,MOD_SERIAL,NEWWINDOW, IS_NEW FROM G_MODULE_MAP INNER JOIN (SELECT G_MODULE_LINK.* FROM G_MODULE_LINK INNER JOIN G_PRIVILEGE_MAP ON G_MODULE_LINK.MOD_ID=G_PRIVILEGE_MAP.MOD_ID WHERE G_PRIVILEGE_MAP.SPC='" + spc + "' AND ROLE_ID='" + rolid + "' AND EMP_SPECIFIC='N' AND HIDE_URL='N')  G_MODULE_LINK ON G_MODULE_MAP.MOD_ID=G_MODULE_LINK.MOD_ID) GRPMOD  ON ROLLMODGRP.MOD_GRP_ID=GRPMOD.MOD_GRP_ID  ORDER BY SERIAL,MODGRPNAME,MOD_SERIAL,MOD_NAME";
            } else if (rolid != null && rolid.equals("01")) {
                sqlStr = "SELECT ROLE_ID,GRPMOD.MOD_GRP_ID,ROLLMODGRP.MOD_GRP_NAME MODGRPNAME,GRPMOD.MOD_ID,GRPMOD.MOD_NAME,URL,HELP_URL,EMP_SPECIFIC,SERIAL,CONVERT_URL,MOD_SERIAL,NEWWINDOW, IS_NEW  FROM (SELECT G_GROUP_MAP.ROLE_ID,G_MODULE_GROUP.MOD_GRP_ID,G_MODULE_GROUP.MOD_GRP_NAME,SERIAL FROM G_GROUP_MAP INNER JOIN G_MODULE_GROUP ON G_GROUP_MAP.MOD_GRP_ID = G_MODULE_GROUP.MOD_GRP_ID WHERE ROLE_ID='" + rolid + "')  ROLLMODGRP  INNER JOIN  (SELECT distinct G_MODULE_MAP.MOD_GRP_ID,G_MODULE_LINK.MOD_ID,MOD_NAME,URL,HELP_URL,EMP_SPECIFIC,CONVERT_URL,MOD_SERIAL,NEWWINDOW, IS_NEW FROM G_MODULE_MAP INNER JOIN (SELECT G_MODULE_LINK.* FROM G_MODULE_LINK INNER JOIN G_PRIVILEGE_MAP ON G_MODULE_LINK.MOD_ID=G_PRIVILEGE_MAP.MOD_ID WHERE G_PRIVILEGE_MAP.SPC='" + spc + "' AND ROLE_ID='" + rolid + "' AND EMP_SPECIFIC='Y' AND HIDE_URL='N')  G_MODULE_LINK ON G_MODULE_MAP.MOD_ID=G_MODULE_LINK.MOD_ID) GRPMOD  ON ROLLMODGRP.MOD_GRP_ID=GRPMOD.MOD_GRP_ID  ORDER BY SERIAL,MODGRPNAME,MOD_SERIAL,MOD_NAME";
            } else if (rolid != null && rolid.equals("08")) {
                sqlStr = "SELECT '' off_code,MODULE_MAP.MOD_GRP_ID, MOD_GRP_NAME MODGRPNAME, MOD_ID, MOD_NAME, URL,HELP_URL, EMP_SPECIFIC, SERIAL, CONVERT_URL, MOD_SERIAL, NEWWINDOW , IS_NEW  FROM (SELECT  G_MODULE.MOD_ID, MOD_GRP_ID, MOD_NAME, URL,HELP_URL, EMP_SPECIFIC, CONVERT_URL, MOD_SERIAL, NEWWINDOW FROM ( "
                        + "SELECT MOD_ID, MOD_NAME, URL,HELP_URL, EMP_SPECIFIC, CONVERT_URL, MOD_SERIAL, NEWWINDOW, IS_NEW FROM G_MODULE_LINK WHERE EMP_SPECIFIC='Y' AND HIDE_URL='N') G_MODULE "
                        + "INNER JOIN G_MODULE_MAP ON G_MODULE.MOD_ID=G_MODULE_MAP.MOD_ID) MODULE_MAP "
                        + "INNER JOIN G_MODULE_GROUP ON MODULE_MAP.MOD_GRP_ID=G_MODULE_GROUP.MOD_GRP_ID ORDER BY SERIAL,MODGRPNAME,MOD_SERIAL,MOD_NAME";
            } else if ((rolid != null && rolid.equals("03") || rolid.equals("02")) && (spc == null || spc.equals(""))) {
                sqlStr = "SELECT '' off_code,MODULE_MAP.MOD_GRP_ID, MOD_GRP_NAME MODGRPNAME, MOD_ID, MOD_NAME, URL,HELP_URL, EMP_SPECIFIC, SERIAL, CONVERT_URL, MOD_SERIAL, NEWWINDOW, IS_NEW  "
                        + "FROM (SELECT  G_MODULE.MOD_ID, MOD_GRP_ID, MOD_NAME, URL,HELP_URL, EMP_SPECIFIC, CONVERT_URL, MOD_SERIAL, NEWWINDOW, IS_NEW FROM ( "
                        + "SELECT MOD_ID, MOD_NAME, URL,HELP_URL, EMP_SPECIFIC, CONVERT_URL, MOD_SERIAL, NEWWINDOW, IS_NEW FROM G_MODULE_LINK WHERE MOD_ROLE_ID='" + rolid + "' AND HIDE_URL='N') G_MODULE "
                        + "INNER JOIN G_MODULE_MAP ON G_MODULE.MOD_ID=G_MODULE_MAP.MOD_ID) MODULE_MAP  "
                        + "INNER JOIN G_MODULE_GROUP ON MODULE_MAP.MOD_GRP_ID=G_MODULE_GROUP.MOD_GRP_ID ORDER BY SERIAL,MODGRPNAME,MOD_SERIAL,MOD_NAME";
            } else {
                if (isddo == true) {
                    if (ifEmpClicked.equalsIgnoreCase("Y")) {
                        sqlStr = "SELECT '' off_code,ROLE_ID,GRPMOD.MOD_GRP_ID,ROLLMODGRP.MOD_GRP_NAME MODGRPNAME,GRPMOD.MOD_ID,GRPMOD.MOD_NAME,URL,HELP_URL,EMP_SPECIFIC,SERIAL,CONVERT_URL,\n"
                                + "MOD_SERIAL,NEWWINDOW, IS_NEW  FROM (SELECT G_GROUP_MAP.ROLE_ID,G_MODULE_GROUP.MOD_GRP_ID,G_MODULE_GROUP.MOD_GRP_NAME,SERIAL FROM G_GROUP_MAP \n"
                                + "INNER JOIN G_MODULE_GROUP ON G_GROUP_MAP.MOD_GRP_ID = G_MODULE_GROUP.MOD_GRP_ID WHERE ROLE_ID='" + rolid + "')  ROLLMODGRP  \n"
                                + "INNER JOIN  (SELECT distinct G_MODULE_MAP.MOD_GRP_ID,G_MODULE_LINK.MOD_ID,MOD_NAME,URL,HELP_URL,EMP_SPECIFIC,CONVERT_URL,MOD_SERIAL,NEWWINDOW, \n"
                                + "IS_NEW FROM G_MODULE_MAP INNER JOIN (SELECT G_MODULE_LINK.* FROM G_MODULE_LINK INNER JOIN G_PRIVILEGE_MAP ON G_MODULE_LINK.MOD_ID=G_PRIVILEGE_MAP.MOD_ID WHERE \n"
                                + "G_PRIVILEGE_MAP.SPC='" + spc + "' AND ROLE_ID='" + rolid + "' AND EMP_SPECIFIC='Y' AND HIDE_URL='N' " + criteriaForEmployeeType + " )  G_MODULE_LINK \n"
                                + "ON G_MODULE_MAP.MOD_ID=G_MODULE_LINK.MOD_ID) GRPMOD  ON ROLLMODGRP.MOD_GRP_ID=GRPMOD.MOD_GRP_ID  ORDER BY SERIAL,MODGRPNAME,MOD_SERIAL,MOD_NAME";
                       //System.out.println("DDO-->" + sqlStr);
                    } else {
                        sqlStr = "SELECT '' off_code,ROLE_ID,GRPMOD.MOD_GRP_ID,ROLLMODGRP.MOD_GRP_NAME MODGRPNAME,GRPMOD.MOD_ID,GRPMOD.MOD_NAME,URL,HELP_URL,EMP_SPECIFIC,SERIAL, "
                                + "CONVERT_URL,MOD_SERIAL,NEWWINDOW , IS_NEW FROM ( "
                                + "SELECT G_GROUP_MAP.ROLE_ID,G_MODULE_GROUP.MOD_GRP_ID,G_MODULE_GROUP.MOD_GRP_NAME,SERIAL FROM G_GROUP_MAP "
                                + "INNER JOIN G_MODULE_GROUP ON G_GROUP_MAP.MOD_GRP_ID = G_MODULE_GROUP.MOD_GRP_ID WHERE ROLE_ID='" + rolid + "')  ROLLMODGRP  "
                                + "INNER JOIN  ( "
                                + "  SELECT distinct G_MODULE_MAP.MOD_GRP_ID,G_MODULE.MOD_ID,MOD_NAME,URL,HELP_URL,EMP_SPECIFIC,CONVERT_URL,MOD_SERIAL,NEWWINDOW , IS_NEW FROM G_MODULE_MAP "
                                + "    INNER JOIN ( "
                                + "      SELECT * FROM ( "
                                + "SELECT G_MODULE_LINK.MOD_ID,MOD_NAME,URL,EMP_SPECIFIC,CONVERT_URL,MOD_SERIAL,NEWWINDOW,HELP_URL,MODULE_TYPE,IS_DIRECTLINK,MOD_ROLE_ID,EMPTYPE, "
                                + "PRIV_MAP_ID,SPC,ROLE_ID,MOD_GRP_ID, IS_NEW FROM G_MODULE_LINK WHERE HIDE_URL='N' "
                                + "INNER JOIN G_PRIVILEGE_MAP ON G_MODULE_LINK.MOD_ID=G_PRIVILEGE_MAP.MOD_ID "
                                + "WHERE G_PRIVILEGE_MAP.SPC='" + spc + "' AND ROLE_ID='" + rolid + "' AND EMP_SPECIFIC='N' "
                                + "UNION "
                                + "SELECT G_MODULE_LINK.MOD_ID,MOD_NAME,URL,EMP_SPECIFIC,CONVERT_URL,MOD_SERIAL,NEWWINDOW, IS_NEW, "
                                + " HELP_URL, MODULE_TYPE, IS_DIRECTLINK,'' MOD_ROLE_ID, EMPTYPE,0 PRIV_MAP_ID,'" + spc + "' SPC,'" + rolid + "' ROLE_ID,'017' MOD_GRP_ID "
                                + " FROM G_MODULE_LINK WHERE MOD_ID='205' ) G_MODULE_LINK "
                                + "    )  G_MODULE_LINK ON G_MODULE_MAP.MOD_ID=G_MODULE_LINK.MOD_ID) "
                                + "GRPMOD  ON ROLLMODGRP.MOD_GRP_ID=GRPMOD.MOD_GRP_ID  "
                                + "ORDER BY SERIAL,MODGRPNAME,MOD_SERIAL,MOD_NAME";
                       // System.out.println("str::" + sqlStr);

                    }
                } else {
                    if (ifEmpClicked.equalsIgnoreCase("Y") && !employeeTypeLink.equalsIgnoreCase("F")) {
                        System.out.println("criteria");
                        sqlStr = "SELECT off_code,spc,role_id,mod_grp_id,modgrpname,mod_id,mod_name,url,help_url,emp_specific,serial,convert_url,mod_serial,newwindow,is_new FROM\n"
                                + "(select g_spc.off_code,gprivmodmaplink.* from\n"
                                + "(SELECT SPC,ROLE_ID,GRPMOD.MOD_GRP_ID,ROLLMODGRP.MOD_GRP_NAME MODGRPNAME,GRPMOD.MOD_ID,GRPMOD.MOD_NAME,URL,HELP_URL,EMP_SPECIFIC,SERIAL,CONVERT_URL,\n"
                                + "MOD_SERIAL,NEWWINDOW, IS_NEW  FROM (SELECT G_GROUP_MAP.ROLE_ID,G_MODULE_GROUP.MOD_GRP_ID,G_MODULE_GROUP.MOD_GRP_NAME,SERIAL FROM G_GROUP_MAP\n"
                                + "INNER JOIN G_MODULE_GROUP ON G_GROUP_MAP.MOD_GRP_ID = G_MODULE_GROUP.MOD_GRP_ID WHERE ROLE_ID='" + rolid + "')  ROLLMODGRP \n"
                                + "INNER JOIN  (SELECT distinct G_MODULE_MAP.MOD_GRP_ID,G_MODULE_LINK.MOD_ID,MOD_NAME,URL,HELP_URL,EMP_SPECIFIC,CONVERT_URL,MOD_SERIAL,NEWWINDOW, \n"
                                + "IS_NEW,SPC FROM G_MODULE_MAP \n"
                                + "INNER JOIN (SELECT G_MODULE_LINK.*,G_PRIVILEGE_MAP.SPC FROM G_MODULE_LINK \n"
                                + "INNER JOIN G_PRIVILEGE_MAP ON G_MODULE_LINK.MOD_ID=G_PRIVILEGE_MAP.MOD_ID WHERE\n"
                                + "(G_PRIVILEGE_MAP.SPC='" + spc + "' or G_PRIVILEGE_MAP.spc in (select spc from emp_join where \n"
                                + "emp_id in (select emp_id from emp_mast where cur_spc='" + spc + "')\n"
                                + "and not_type='ADDITIONAL_CHARGE' and if_ad_charge='Y')) AND ROLE_ID='" + rolid + "' AND \n"
                                + "EMP_SPECIFIC='Y' AND HIDE_URL='N' " + criteriaForEmployeeType + ")  G_MODULE_LINK\n"
                                + "ON G_MODULE_MAP.MOD_ID=G_MODULE_LINK.MOD_ID) GRPMOD  ON ROLLMODGRP.MOD_GRP_ID=GRPMOD.MOD_GRP_ID  \n"
                                + "ORDER BY SPC,SERIAL,MODGRPNAME,MOD_SERIAL,MOD_NAME)gprivmodmaplink\n"
                                + "left outer join g_spc on gprivmodmaplink.spc=g_spc.spc)temp "
                                + "group by off_code,spc,role_id,mod_grp_id,modgrpname,mod_id,mod_name,url,help_url,emp_specific,serial,convert_url,mod_serial,newwindow,is_new\n"
                                + "order by modgrpname";
                        //System.out.println("ELSE:::" + sqlStr);
                    } else if (ifEmpClicked.equalsIgnoreCase("Y") && employeeTypeLink.equalsIgnoreCase("F")) {
                        System.out.println("===Deputation====");
                        sqlStr = "SELECT off_code,spc,role_id,mod_grp_id,modgrpname,mod_id,mod_name,url,help_url,emp_specific,serial,convert_url,mod_serial,newwindow,is_new FROM\n"
                                + "(select g_spc.off_code,gprivmodmaplink.* from\n"
                                + "(SELECT SPC,ROLE_ID,GRPMOD.MOD_GRP_ID,ROLLMODGRP.MOD_GRP_NAME MODGRPNAME,GRPMOD.MOD_ID,GRPMOD.MOD_NAME,URL,HELP_URL,EMP_SPECIFIC,SERIAL,CONVERT_URL,\n"
                                + "MOD_SERIAL,NEWWINDOW, IS_NEW  FROM (SELECT G_GROUP_MAP.ROLE_ID,G_MODULE_GROUP.MOD_GRP_ID,G_MODULE_GROUP.MOD_GRP_NAME,SERIAL FROM G_GROUP_MAP\n"
                                + "INNER JOIN G_MODULE_GROUP ON G_GROUP_MAP.MOD_GRP_ID = G_MODULE_GROUP.MOD_GRP_ID WHERE ROLE_ID='" + rolid + "')  ROLLMODGRP\n"
                                + "INNER JOIN  (SELECT distinct G_MODULE_MAP.MOD_GRP_ID,G_MODULE_LINK.MOD_ID,MOD_NAME,URL,HELP_URL,EMP_SPECIFIC,CONVERT_URL,MOD_SERIAL,NEWWINDOW,\n"
                                + "IS_NEW,SPC FROM G_MODULE_MAP\n"
                                + "INNER JOIN (SELECT G_MODULE_LINK.*,G_PRIVILEGE_MAP.SPC FROM G_MODULE_LINK\n"
                                + "INNER JOIN G_PRIVILEGE_MAP ON G_MODULE_LINK.MOD_ID=G_PRIVILEGE_MAP.MOD_ID WHERE\n"
                                + "(G_PRIVILEGE_MAP.SPC='" + spc + "' or G_PRIVILEGE_MAP.spc in (select spc from emp_join where\n"
                                + "emp_id in (select emp_id from emp_mast where cur_spc='" + spc + "')\n"
                                + "and not_type='ADDITIONAL_CHARGE' and if_ad_charge='Y')) AND ROLE_ID='" + rolid + "' AND\n"
                                + "EMP_SPECIFIC='Y' AND HIDE_URL='N'  )  G_MODULE_LINK\n"
                                + "ON G_MODULE_MAP.MOD_ID=G_MODULE_LINK.MOD_ID) GRPMOD  ON ROLLMODGRP.MOD_GRP_ID=GRPMOD.MOD_GRP_ID \n"
                                + "ORDER BY SPC,SERIAL,MODGRPNAME,MOD_SERIAL,MOD_NAME)gprivmodmaplink\n"
                                + "left outer join g_spc on gprivmodmaplink.spc=g_spc.spc)temp\n"
                                + "group by off_code,spc,role_id,mod_grp_id,modgrpname,mod_id,mod_name,url,help_url,emp_specific,serial,convert_url,mod_serial,newwindow,is_new\n"
                                + "having mod_id in ('23') order by modgrpname";
                       // System.out.println("str::" + sqlStr);

                    } else {
                        System.out.println("No Criteria");
                        sqlStr = "SELECT off_code,spc,role_id,mod_grp_id,modgrpname,mod_id,mod_name,url,help_url,emp_specific,serial,convert_url,mod_serial,newwindow,is_new FROM\n"
                                + "(select g_spc.off_code,gprivmodmaplink.* from\n"
                                + "(SELECT GRPMOD.spc,ROLE_ID,GRPMOD.MOD_GRP_ID,ROLLMODGRP.MOD_GRP_NAME MODGRPNAME,GRPMOD.MOD_ID,GRPMOD.MOD_NAME,URL,HELP_URL,EMP_SPECIFIC,SERIAL,\n"
                                + "CONVERT_URL,MOD_SERIAL,NEWWINDOW, IS_NEW  FROM (SELECT G_GROUP_MAP.ROLE_ID,G_MODULE_GROUP.MOD_GRP_ID,G_MODULE_GROUP.MOD_GRP_NAME,SERIAL\n"
                                + "FROM G_GROUP_MAP INNER JOIN G_MODULE_GROUP ON G_GROUP_MAP.MOD_GRP_ID = G_MODULE_GROUP.MOD_GRP_ID WHERE ROLE_ID='" + rolid + "')  ROLLMODGRP \n"
                                + "INNER JOIN  (SELECT distinct G_MODULE_MAP.MOD_GRP_ID,G_MODULE_LINK.MOD_ID,MOD_NAME,URL,HELP_URL,EMP_SPECIFIC,CONVERT_URL,MOD_SERIAL,NEWWINDOW, \n"
                                + "IS_NEW,spc FROM G_MODULE_MAP INNER JOIN (SELECT g_privilege_map.spc,G_MODULE_LINK.* FROM G_MODULE_LINK INNER JOIN G_PRIVILEGE_MAP ON \n"
                                + "G_MODULE_LINK.MOD_ID=G_PRIVILEGE_MAP.MOD_ID WHERE (G_PRIVILEGE_MAP.SPC='" + spc + "' or \n"
                                + "G_PRIVILEGE_MAP.spc in (select spc from emp_join where \n"
                                + "emp_id in (select emp_id from emp_mast where cur_spc='" + spc + "')\n"
                                + "and not_type='ADDITIONAL_CHARGE' and if_ad_charge='Y'))  AND ROLE_ID='" + rolid + "' AND EMP_SPECIFIC='N' \n"
                                + "AND HIDE_URL='N' )  G_MODULE_LINK ON G_MODULE_MAP.MOD_ID=G_MODULE_LINK.MOD_ID) GRPMOD  \n"
                                + "ON ROLLMODGRP.MOD_GRP_ID=GRPMOD.MOD_GRP_ID  ORDER BY SERIAL,MODGRPNAME,MOD_SERIAL,MOD_NAME,spc)gprivmodmaplink\n"
                                + "left outer join g_spc on gprivmodmaplink.spc=g_spc.spc)temp "
                                + "group by off_code,spc,role_id,mod_grp_id,modgrpname,mod_id,mod_name,url,help_url,emp_specific,serial,convert_url,mod_serial,newwindow,is_new\n"
                                + "order by modgrpname";
                    }
                }
            }
            rs = st.executeQuery(sqlStr);
            String prvGrp = null;
            ModuleProperty mp = null;
            while (rs.next()) {
                if (prvGrp == null) {
                    mg = new ModuleGroup();
                    mg.setModGrpId(rs.getString("MOD_GRP_ID"));
                    mg.setModGrpName(rs.getString("MODGRPNAME"));
                }
                mp = new ModuleProperty();
                mp.setModuleId(rs.getString("MOD_ID"));
                mp.setModuleName(rs.getString("MOD_NAME"));
                mp.setLinkurl(rs.getString("URL"));
                mp.setHelpUrl(rs.getString("HELP_URL"));
                mp.setIfspecific(rs.getString("EMP_SPECIFIC"));
                mp.setConvertUrl(rs.getString("CONVERT_URL"));
                mp.setIsNew(rs.getString("IS_NEW"));
                //--Office wise to set module name--//

                if (rs.getString("NEWWINDOW") == null || rs.getString("NEWWINDOW").equals("")) {
                    mp.setNewWindow("N");
                } else {
                    mp.setNewWindow(rs.getString("NEWWINDOW"));
                }

                if (prvGrp != null && !prvGrp.equals(rs.getString("MOD_GRP_ID"))) {
                    roleListarr.add(mg);
                    mg = new ModuleGroup();
                    moduleList = new ArrayList();
                    mg.setModGrpId(rs.getString("MOD_GRP_ID"));
                    mg.setModGrpName(rs.getString("MODGRPNAME"));
                    /*if (rs.getString("off_code") != null && !rs.getString("off_code").equals("")) {
                     mg.setModuleOfc(rs.getString("off_code"));
                     }*/

                }
                if (rs.getString("off_code") != null && !rs.getString("off_code").equals("")) {
                    mp.setModOfc(rs.getString("off_code"));
                }
                moduleList.add(mp);
                mg.setModuleListArr(moduleList);
                prvGrp = rs.getString("MOD_GRP_ID");
            }
            if (mg != null) {
                roleListarr.add(mg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs,st);
            DataBaseFunctions.closeSqlObjects(con);
        }

        return roleListarr;
    }

}
