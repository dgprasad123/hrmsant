/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.common;

import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author Manas
 */
public class CommonMasterDataFunction {

    public static String getDeptName(String deptCode, Statement st) throws Exception {
        String deptName = null;
        ResultSet rs = null;
        try {
            rs = st.executeQuery("SELECT DEPARTMENT_NAME FROM G_DEPARTMENT WHERE DEPARTMENT_CODE='" + deptCode + "'");
            while (rs.next()) {
                deptName = rs.getString("DEPARTMENT_NAME").toUpperCase();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
        }
        return deptName;
    }

    public static String getOffName(String offCode, Statement st) throws Exception {
        String offName = null;
        ResultSet rs = null;
        try {
            rs = st.executeQuery("SELECT OFF_EN FROM G_OFFICE WHERE OFF_CODE='" + offCode + "'");
            while (rs.next()) {
                offName = rs.getString("OFF_EN").toUpperCase();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
        }
        return offName;
    }

    public static String getSPN(String spc, Statement st) throws Exception {
        String spn = "";
        ResultSet rs = null;
        try {
            if (spc != null && !spc.trim().equals("")) {
                if (spc.substring(0, 3).equalsIgnoreCase("GOI") || spc.substring(0, 3).equalsIgnoreCase("OSG") || spc.substring(0, 3).equalsIgnoreCase("FRB") || spc.substring(0, 3).equalsIgnoreCase("ORG")) {
                    rs = st.executeQuery("SELECT AUTH_NAME,OFF_EN,DEPT_NAME FROM G_OTH_SPC WHERE OTH_SPC='" + spc + "' order by AUTH_NAME");
                    while (rs.next()) {
                        String othspn = null;
                        if ((rs.getString("AUTH_NAME") == null || rs.getString("AUTH_NAME").trim().equals(""))
                                && (rs.getString("OFF_EN") != null && !rs.getString("OFF_EN").trim().equals(""))
                                && (rs.getString("DEPT_NAME") != null && !rs.getString("DEPT_NAME").trim().equals(""))) {
                            othspn = rs.getString("OFF_EN") + ", " + rs.getString("DEPT_NAME");
                        }
                        if ((rs.getString("AUTH_NAME") == null || rs.getString("AUTH_NAME").trim().equals(""))
                                && (rs.getString("OFF_EN") != null && !rs.getString("OFF_EN").trim().equals(""))
                                && (rs.getString("DEPT_NAME") == null || rs.getString("DEPT_NAME").trim().equals(""))) {
                            othspn = rs.getString("OFF_EN");
                        }
                        if ((rs.getString("AUTH_NAME") == null || rs.getString("AUTH_NAME").trim().equals(""))
                                && (rs.getString("OFF_EN") == null || rs.getString("OFF_EN").trim().equals(""))
                                && (rs.getString("DEPT_NAME") != null && !rs.getString("DEPT_NAME").trim().equals(""))) {
                            othspn = rs.getString("DEPT_NAME");
                        }
                        if ((rs.getString("AUTH_NAME") != null && !rs.getString("AUTH_NAME").trim().equals(""))
                                && (rs.getString("OFF_EN") == null || rs.getString("OFF_EN").trim().equals(""))
                                && (rs.getString("DEPT_NAME") == null || rs.getString("DEPT_NAME").trim().equals(""))) {
                            othspn = rs.getString("AUTH_NAME");
                        } else if ((rs.getString("AUTH_NAME") != null && !rs.getString("AUTH_NAME").trim().equals(""))
                                && (rs.getString("OFF_EN") == null || rs.getString("OFF_EN").trim().equals(""))
                                && (rs.getString("DEPT_NAME") != null && !rs.getString("DEPT_NAME").trim().equals(""))) {
                            othspn = rs.getString("AUTH_NAME") + ", " + rs.getString("DEPT_NAME");
                        } else if ((rs.getString("AUTH_NAME") != null && !rs.getString("AUTH_NAME").trim().equals(""))
                                && (rs.getString("OFF_EN") != null && !rs.getString("OFF_EN").trim().equals(""))
                                && (rs.getString("DEPT_NAME") == null || rs.getString("DEPT_NAME").trim().equals(""))) {
                            othspn = rs.getString("AUTH_NAME") + ", " + rs.getString("OFF_EN");
                        } else if ((rs.getString("AUTH_NAME") != null && !rs.getString("AUTH_NAME").trim().equals(""))
                                && (rs.getString("OFF_EN") != null && !rs.getString("OFF_EN").trim().equals(""))
                                && (rs.getString("DEPT_NAME") != null && !rs.getString("DEPT_NAME").trim().equals(""))) {
                            othspn = rs.getString("AUTH_NAME") + ", " + rs.getString("OFF_EN") + ", " + rs.getString("DEPT_NAME");
                        }
                        spn = othspn;
                    }
                } else {
                    rs = st.executeQuery("Select SPN from G_SPC where SPC='" + spc + "' AND SPN IS NOT NULL");
                    while (rs.next()) {
                        spn = rs.getString("SPN");
                    }
                    if (rs != null) {
                        rs.close();
                        rs = null;
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                rs.close();
                rs = null;
            }
        }
        return spn;
    }
}
