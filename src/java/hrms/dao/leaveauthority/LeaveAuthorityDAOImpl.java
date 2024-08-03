package hrms.dao.leaveauthority;

import hrms.SelectOption;
import hrms.common.DataBaseFunctions;
import hrms.model.leaveauthority.LeaveAuthorityBean;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.hibernate.Session;

public class LeaveAuthorityDAOImpl implements LeaveAuthorityDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List leaveAuthorityList(String logedinUserSpc, String loginOffcode, String empId, String processCode, String finYear) {
        Session session = null;
        Connection con = null;
        List leaveAuthList = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = "";
        LeaveAuthorityBean lab = null;
        String[] yeartemp = finYear.split("-");
        int year1 = Integer.parseInt(yeartemp[0]);
        int year2 = year1 + 2;
        ArrayList al = new ArrayList();
        String finyear1 = "01-APR-" + year1;
        String finyear2 = "31-MAR-" + year2;
        try {
            con = dataSource.getConnection();
//            pstmt = con.prepareStatement("SELECT LMID, EMPNAME,OFF_AS,OFF_NAME,FNAME FROM( "
//                    + "SELECT LMID,INITIALS||' '||FNAME||' '||MNAME||' '||LNAME EMPNAME,OFF_AS,FNAME FROM( "
//                    + "SELECT AUTHORITY_SPC FROM WORKFLOW_AUTHORITY WHERE APPLICANT_SPC ='" + logedinUserSpc + "' AND PROCESS_ID='" + processCode + "' AND AUTHORITY_TYPE='LM') WORKFLOW_AUTHORITY "
//                    + "INNER JOIN LA_MEMBERS ON WORKFLOW_AUTHORITY.AUTHORITY_SPC= LA_MEMBERS.LMID::text) LA_MEMBERS "
//                    + "INNER JOIN G_OFFICIATING  ON LA_MEMBERS.OFF_AS=G_OFFICIATING.OFF_ID ORDER BY FNAME");
//            rs = pstmt.executeQuery();
//            while (rs.next()) {
//                lab = new LeaveAuthorityBean();
//                lab.setDesignation(rs.getString("OFF_NAME"));
//                lab.setRadempid(rs.getString("LMID"));
//                lab.setName(rs.getString("EMPNAME"));
//                lab.setSpc(rs.getString("LMID"));
//                al.add(lab);
//            }
            if (logedinUserSpc != null && !logedinUserSpc.equals("")) {
                sql = "SELECT EMPNAME,AUTHORITY_SPC,EMP_ID,F_NAME,SPN  " 
                       + "   FROM(SELECT EMP_ID,AUTHORITY_SPC,concat_ws(' ', INITIALS ::text, F_NAME::text, M_NAME::text,L_NAME::text) AS EMPNAME,F_NAME FROM (SELECT AUTHORITY_SPC FROM WORKFLOW_AUTHORITY WHERE APPLICANT_SPC ='" + logedinUserSpc + "'  "
                        + "   AND PROCESS_ID=1 )WFAUTH INNER JOIN EMP_MAST ON  EMP_MAST.CUR_SPC=WFAUTH.AUTHORITY_SPC)TAB INNER JOIN  " 
                        + "   G_SPC ON G_SPC.SPC=TAB.AUTHORITY_SPC  " 
                        + "   UNION " 
                        + "   SELECT EMPNAME,AUTHORITY_SPC,EMP_RELIEVE.EMP_ID,F_NAME,SPN FROM (SELECT * FROM WORKFLOW_AUTHORITY WHERE APPLICANT_SPC='" + logedinUserSpc + "' AND PROCESS_ID=1)WORKFLOW_AUTHORITY " 
                        + "   INNER JOIN  " 
                        + "   (SELECT emp_join.SPC,emp_join.EMP_ID FROM EMP_JOIN " 
                        + "   left outer join emp_relieve on emp_join.join_id=emp_relieve.join_id " 
                        + "   WHERE  if_ad_charge='Y'  and emp_relieve.join_id is null and emp_join.spc is not null)EMP_RELIEVE " 
                        + "   ON WORKFLOW_AUTHORITY.AUTHORITY_SPC=EMP_RELIEVE.SPC " 
                        + "   LEFT OUTER JOIN (SELECT EMP_ID,concat_ws(' ', INITIALS ::text, F_NAME::text, M_NAME::text,L_NAME::text) AS EMPNAME,F_NAME FROM EMP_MAST)EMP_MAST ON EMP_RELIEVE.EMP_ID=EMP_MAST.EMP_ID  " 
                        + "   LEFT OUTER JOIN G_SPC ON WORKFLOW_AUTHORITY.AUTHORITY_SPC=G_SPC.SPC  " 
                        + "   ORDER BY F_NAME";
            } else {
                sql = "SELECT EMPNAME,AUTHORITY_SPC,EMP_ID,F_NAME,SPN  "
                        + "   FROM(SELECT EMP_ID,AUTHORITY_SPC,concat_ws(' ', INITIALS ::text, F_NAME::text, M_NAME::text,L_NAME::text) AS EMPNAME,F_NAME FROM (SELECT AUTHORITY_SPC FROM WORKFLOW_AUTHORITY WHERE APPLICANT_EMP_ID ='" + empId + "'  "
                        + "   AND PROCESS_ID=1 )WFAUTH INNER JOIN EMP_MAST ON  EMP_MAST.CUR_SPC=WFAUTH.AUTHORITY_SPC)TAB INNER JOIN  "
                        + "   G_SPC ON G_SPC.SPC=TAB.AUTHORITY_SPC  "
                        + "   UNION "
                        + "   SELECT EMPNAME,AUTHORITY_SPC,EMP_RELIEVE.EMP_ID,F_NAME,SPN FROM (SELECT * FROM WORKFLOW_AUTHORITY WHERE APPLICANT_EMP_ID='" + empId + "' AND PROCESS_ID=1)WORKFLOW_AUTHORITY  "
                        + "   INNER JOIN  "
                        + "   (SELECT emp_join.SPC,emp_join.EMP_ID FROM EMP_JOIN  "
                        + "   left outer join emp_relieve on emp_join.join_id=emp_relieve.join_id "
                        + "   WHERE  if_ad_charge='Y'  and emp_relieve.join_id is null and emp_join.spc is not null)EMP_RELIEVE  "
                        + "   ON WORKFLOW_AUTHORITY.AUTHORITY_SPC=EMP_RELIEVE.SPC  "
                        + "   LEFT OUTER JOIN (SELECT EMP_ID,concat_ws(' ', INITIALS ::text, F_NAME::text, M_NAME::text,L_NAME::text) AS EMPNAME,F_NAME FROM EMP_MAST)EMP_MAST ON EMP_RELIEVE.EMP_ID=EMP_MAST.EMP_ID  "
                        + "   LEFT OUTER JOIN G_SPC ON WORKFLOW_AUTHORITY.AUTHORITY_SPC=G_SPC.SPC  "
                        + "   ORDER BY F_NAME";
            }

            pstmt = con.prepareStatement(sql);
            //pstmt.setString(1, logedinUserSpc);
            //pstmt.setInt(2, 1);
            //pstmt.setString(3, logedinUserSpc);
            //pstmt.setInt(4, 1);
            //pstmt.setString(5, loginOffcode);
            int i = 1;
            rs = pstmt.executeQuery();
            while (rs.next()) {
                lab = new LeaveAuthorityBean();
                lab.setEmpId(rs.getString("EMP_ID"));
                lab.setDesignation(rs.getString("SPN"));
                lab.setRadempid(rs.getString("EMP_ID") + ":" + rs.getString("EMPNAME") + ":" + rs.getString("SPN") + ":" + rs.getString("AUTHORITY_SPC"));
                lab.setName(rs.getString("EMPNAME"));
                lab.setSpc(rs.getString("AUTHORITY_SPC") + ":" + logedinUserSpc);
                al.add(lab);
                i++;
            }

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            DataBaseFunctions.closeSqlObjects(rs,pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }

        return al;

    }

    @Override
    public List otherStaffList(String logedinUserSpc, String empId, String processCode, String finYear) {
        Session session = null;
        Connection con = null;
        List leaveAuthList = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        LeaveAuthorityBean lab = null;
        String[] yeartemp = finYear.split("-");
        int year1 = Integer.parseInt(yeartemp[0]);
        int year2 = year1 + 2;
        ArrayList al = new ArrayList();
        String finyear1 = "01-APR-" + year1;
        String finyear2 = "31-MAR-" + year2;
        try {

            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT LMID, EMPNAME,OFF_AS,OFF_NAME,FNAME FROM( "
                    + "SELECT LMID,INITIALS||' '||FNAME||' '||MNAME||' '||LNAME EMPNAME,OFF_AS,FNAME FROM( "
                    + "SELECT AUTHORITY_SPC FROM WORKFLOW_AUTHORITY WHERE APPLICANT_SPC ='" + logedinUserSpc + "' AND PROCESS_ID='" + processCode + "' AND AUTHORITY_TYPE='LM') WORKFLOW_AUTHORITY "
                    + "INNER JOIN LA_MEMBERS ON WORKFLOW_AUTHORITY.AUTHORITY_SPC= LA_MEMBERS.LMID::text) LA_MEMBERS "
                    + "INNER JOIN G_OFFICIATING  ON LA_MEMBERS.OFF_AS=G_OFFICIATING.OFF_ID ORDER BY FNAME");
            rs = pstmt.executeQuery();
            while (rs.next()) {
                lab = new LeaveAuthorityBean();
                lab.setDesignation(rs.getString("OFF_NAME"));
                lab.setRadempid(rs.getString("LMID"));
                lab.setName(rs.getString("EMPNAME"));
                lab.setSpc(rs.getString("LMID"));
                al.add(lab);
            }
            String sql = "SELECT EMPNAME,AUTHORITY_SPC,EMP_ID,F_NAME,SPN "
                    + " FROM(SELECT EMP_ID,AUTHORITY_SPC,concat_ws(' ', INITIALS ::text, F_NAME::text, M_NAME::text,L_NAME::text) AS EMPNAME,F_NAME FROM (SELECT AUTHORITY_SPC FROM WORKFLOW_AUTHORITY WHERE APPLICANT_SPC ='" + logedinUserSpc + "' "
                    + " AND PROCESS_ID='" + processCode + "' )WFAUTH INNER JOIN EMP_MAST ON  EMP_MAST.CUR_SPC=WFAUTH.AUTHORITY_SPC)TAB INNER JOIN "
                    + " G_SPC ON G_SPC.SPC=TAB.AUTHORITY_SPC "
                    + " UNION"
                    + " SELECT EMPNAME,AUTHORITY_SPC,EMP_RELIEVE.EMP_ID,F_NAME,SPN FROM (SELECT * FROM WORKFLOW_AUTHORITY WHERE APPLICANT_SPC='" + logedinUserSpc + "' AND PROCESS_ID='" + processCode + "')WORKFLOW_AUTHORITY "
                    + " INNER JOIN "
                    + " (SELECT SPC,EMP_ID FROM EMP_RELIEVE WHERE RLV_DATE BETWEEN '" + finyear1 + "' AND '" + finyear2 + "' AND SPC='" + logedinUserSpc + "')EMP_RELIEVE "
                    + " ON WORKFLOW_AUTHORITY.AUTHORITY_SPC=EMP_RELIEVE.SPC "
                    + " LEFT OUTER JOIN (SELECT EMP_ID,concat_ws(' ', INITIALS ::text, F_NAME::text, M_NAME::text,L_NAME::text) AS EMPNAME,F_NAME FROM EMP_MAST)EMP_MAST ON EMP_RELIEVE.EMP_ID=EMP_MAST.EMP_ID "
                    + " LEFT OUTER JOIN G_SPC ON WORKFLOW_AUTHORITY.AUTHORITY_SPC=G_SPC.SPC "
                    + " ORDER BY F_NAME";

            pstmt = con.prepareStatement(sql);

            rs = pstmt.executeQuery();
            while (rs.next()) {
                lab = new LeaveAuthorityBean();
                lab.setDesignation(rs.getString("SPN"));
                lab.setRadempid(rs.getString("EMP_ID") + ":" + rs.getString("EMPNAME") + ":" + rs.getString("SPN") + ":" + rs.getString("AUTHORITY_SPC"));
                lab.setName(rs.getString("EMPNAME"));
                lab.setSpc(rs.getString("AUTHORITY_SPC"));
                al.add(lab);
            }

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            DataBaseFunctions.closeSqlObjects(rs,pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }

        return al;

    }

    @Override
    public ArrayList getDeptList() throws Exception {
        ResultSet rs = null;
        Statement st = null;
        SelectOption so = null;
        ArrayList deptList = new ArrayList();
        Connection con = null;
        try {
            con = dataSource.getConnection();
            st = con.createStatement();
            rs = st.executeQuery("SELECT DEPARTMENT_CODE,DEPARTMENT_NAME FROM G_DEPARTMENT");
            while (rs.next()) {
                so = new SelectOption();
                so.setValue(rs.getString("DEPARTMENT_CODE"));
                so.setLabel(rs.getString("DEPARTMENT_NAME"));
                deptList.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs,st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return deptList;
    }

    @Override
    public ArrayList getOfficeList(String deptCode) throws Exception {
        ResultSet rs = null;
        Statement st = null;
        SelectOption so = null;
        ArrayList officeList = new ArrayList();
        Connection con = null;
        try {
            con = dataSource.getConnection();
            st = con.createStatement();
            rs = st.executeQuery("SELECT OFF_CODE,OFF_EN,DDO_CODE FROM G_OFFICE WHERE DEPARTMENT_CODE='" + deptCode + "' AND (IS_DDO='N' OR OFF_STATUS='F') ORDER BY OFF_EN");
            while (rs.next()) {
                so = new SelectOption();
                so.setValue(rs.getString("OFF_CODE"));
                so.setLabel(rs.getString("OFF_EN") + "(" + rs.getString("DDO_CODE") + ")");
                officeList.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs,st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return officeList;
    }

    @Override
    public ArrayList getAuthorityList(String deptcode, String offCode) throws Exception {
        ResultSet rs = null;

        LeaveAuthorityBean lab = null;
        ArrayList al = new ArrayList();
        PreparedStatement pst = null;
        String name[] = null;
        String sqlQuery = "";
        Connection con = null;

        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("SELECT POST_CODE,POST FROM ("
                    + " SELECT GPC FROM G_SPC WHERE OFF_CODE=? GROUP BY GPC)G_SPC"
                    + " INNER JOIN (SELECT POST_CODE,POST FROM G_POST WHERE ISAUTHORITY='Y')G_POST ON G_SPC.GPC=G_POST.POST_CODE ORDER BY POST");
            pst.setString(1, offCode);
            rs = pst.executeQuery();
            while (rs.next()) {
                lab = new LeaveAuthorityBean();
                lab.setDesignation(rs.getString("POST"));
                lab.setPost_code(rs.getString("POST_CODE"));
                al.add(lab);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(rs);
        }
        return al;
    }

    public ArrayList getOtherStaffList(String deptCode, String offCode) throws Exception {
        ResultSet rs = null;
        ResultSet resultSet = null;
        LeaveAuthorityBean lab = null;
        ArrayList al = new ArrayList();
        Statement st = null;
        Statement stmt = null;
        String name[] = null;
        String sqlQuery = "";
        Connection con = null;

        try {
            con = dataSource.getConnection();
            st = con.createStatement();

            /*sqlQuery = "SELECT * FROM(select SPC,POST,GPF_NO,EMP_ID,F_NAME,DECODE(TRIM(INITIALS),null,'',TRIM(INITIALS)||' ')||DECODE(TRIM(F_NAME),null,'',TRIM(F_NAME)||' ') "
             + "||DECODE(TRIM(M_NAME),null,'',TRIM(M_NAME)||' ')||DECODE(TRIM(L_NAME),null,'',TRIM(L_NAME) ) EMPNAME from (select spc,gpc  "
             + "from g_spc where off_code='" + offCode + "' AND (IFUCLEAN!='Y' OR IFUCLEAN IS NULL)) g_spc  "
             + "inner join  (SELECT * FROM G_POST WHERE ISAUTHORITY='Y')g_post on g_spc.GPC = g_post.POST_CODE INNER JOIN EMP_MAST ON G_SPC.SPC=EMP_MAST.CUR_SPC ) "
             + "ORDER BY POST,F_NAME";*/
            sqlQuery = "SELECT POST_CODE,POST FROM ("
                    + " SELECT GPC FROM G_SPC WHERE OFF_CODE='" + offCode + "' GROUP BY GPC)G_SPC"
                    + " INNER JOIN (SELECT POST_CODE,POST FROM G_POST)G_POST ON G_SPC.GPC=G_POST.POST_CODE ORDER BY POST";
            rs = st.executeQuery(sqlQuery);
            while (rs.next()) {
                lab = new LeaveAuthorityBean();
                lab.setDesignation(rs.getString("POST"));
                lab.setPost_code(rs.getString("POST_CODE"));
                //pab.setName(rs.getString("EMPNAME"));
                //pab.setSpc(rs.getString("SPC"));
                al.add(lab);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs,st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return al;
    }

    @Override
    public boolean saveAuthority(String applicantEmpId, String applicantspc, String authSpcCode, String processCode, String deptCode, String offcode) throws Exception {
        Statement st = null;
        Statement st1 = null;
        PreparedStatement pst = null;

        ResultSet rs = null;
        ResultSet rs1 = null;
        boolean ret = false;
        Connection con = null;
        String authSpc = "";
        String[] postCode = authSpcCode.split(",");
        try {
            con = dataSource.getConnection();
            st = con.createStatement();
            st1 = con.createStatement();

            if (authSpcCode != null) {
                for (int i = 0; i < postCode.length; i++) {
                    authSpc = getAuthoritySpc(con, postCode[i], offcode);
                    if (applicantspc != null && !applicantspc.equals("")) {
                        
                        rs = st.executeQuery("SELECT AUTHORITY_SPC,APPLICANT_EMP_ID,APPLICANT_SPC  FROM WORKFLOW_AUTHORITY WHERE APPLICANT_SPC='" + applicantspc + "'  AND AUTHORITY_SPC='" + authSpc + "' AND PROCESS_ID='" + processCode + "'");
                    } else {
                        rs = st.executeQuery("SELECT AUTHORITY_SPC,APPLICANT_EMP_ID,APPLICANT_SPC  FROM WORKFLOW_AUTHORITY WHERE  APPLICANT_EMP_ID='" + applicantEmpId + "' AND AUTHORITY_SPC='" + authSpc + "' AND PROCESS_ID='" + processCode + "'");
                    }
                    if (rs.next()) {
                        ret = false;
                    } else {
                        pst = con.prepareStatement("INSERT INTO WORKFLOW_AUTHORITY(APPLICANT_EMP_ID,APPLICANT_SPC,PROCESS_ID,AUTHORITY_SPC) values (?,?,?,?)");
                        String sql = "SELECT SPC FROM G_SPC WHERE GPC='" + postCode[i] + "' AND OFF_CODE='" + offcode + "' AND (IFUCLEAN='N' OR IFUCLEAN IS NULL)";
                        rs1 = st1.executeQuery(sql);
                        while (rs1.next()) {
                            if (rs1.getString("SPC") != null && !rs1.getString("SPC").equals("")) {
                                pst.setString(1, applicantEmpId);
                                pst.setString(2, applicantspc);
                                pst.setInt(3, Integer.parseInt(processCode));
                                pst.setString(4, rs1.getString("SPC"));
                                pst.executeUpdate();
                                ret = true;
                            }
                        }

                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs,st);
            DataBaseFunctions.closeSqlObjects(rs1,st1);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return ret;
    }

    String getAuthoritySpc(Connection con, String postCode, String offCode) throws Exception {
        String authSpc = "";
        Statement st = null;
        ResultSet rs = null;
        try {
            st = con.createStatement();
            
            String sql = "SELECT SPC FROM G_SPC WHERE GPC='" + postCode + "' AND OFF_CODE='" + offCode + "' AND (IFUCLEAN='N' OR IFUCLEAN IS NULL)";
            rs = st.executeQuery(sql);
            if (rs.next()) {
                authSpc = rs.getString("SPC");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(st);
            DataBaseFunctions.closeSqlObjects(rs);

        }
        return authSpc;
    }

     public void deleteSancAuth(String authSpc, String applicantSpc) {
        Connection con = null;
        PreparedStatement pst = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("delete from workflow_authority where applicant_spc=? and authority_spc=? and process_id=1");
            pst.setString(1, applicantSpc);
             pst.setString(2, authSpc);
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);

        }
     }
}
