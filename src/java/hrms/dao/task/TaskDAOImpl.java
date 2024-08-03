/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.task;

import hrms.SelectOption;
import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.model.task.TaskListHelperBean;
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
import java.util.Collections;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 *
 * @author Surendra
 */
public class TaskDAOImpl implements TaskDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    @Resource(name = "repodataSource")
    protected DataSource repodataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setRepodataSource(DataSource repodataSource) {
        this.repodataSource = repodataSource;
    }

    public String getApplicantName(Connection con, String applicant, String prid, int taskid) throws Exception {
        ResultSet rs = null;
        SelectOption so = null;
        Statement st = null;
        String authority = null;
        String sql = "";

        try {
            st = con.createStatement();

            if (prid.equals("3")) {
                sql = "SELECT EMPNAME,POST FROM(SELECT GPC,EMPNAME FROM(SELECT CUR_SPC,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMPNAME,EMP_ID FROM EMP_MAST WHERE EMP_ID='" + applicant + "')EMPMAST LEFT OUTER JOIN (SELECT EMP_ID,SPC FROM PAR_MASTER WHERE TASK_ID='" + taskid + "')PAR_MASTER ON EMPMAST.EMP_ID="
                        + " PAR_MASTER.EMP_ID INNER JOIN G_SPC ON G_SPC.SPC=PAR_MASTER.SPC)TAB LEFT OUTER JOIN"
                        + " G_POST ON G_POST.POST_CODE=TAB.GPC";
                rs = st.executeQuery(sql);
                if (rs.next()) {
                    if (rs.getString("POST") != null && !rs.getString("POST").equals("")) {
                        authority = rs.getString("EMPNAME") + "," + rs.getString("POST");
                    } else {
                        authority = rs.getString("EMPNAME");
                    }
                } else {
                    sql = "SELECT EMPNAME,POST FROM"
                            + " (SELECT GPC,EMPNAME FROM"
                            + " (SELECT CUR_SPC,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMPNAME,EMP_ID FROM EMP_MAST"
                            + " WHERE EMP_ID='" + applicant + "')EMPMAST LEFT OUTER JOIN G_SPC ON G_SPC.SPC=EMPMAST.CUR_SPC)TAB LEFT OUTER JOIN"
                            + " G_POST ON G_POST.POST_CODE=TAB.GPC";
                    rs = st.executeQuery(sql);
                    if (rs.next()) {
                        if (rs.getString("POST") != null && !rs.getString("POST").equals("")) {
                            authority = rs.getString("EMPNAME") + "," + rs.getString("POST");
                        } else {
                            authority = rs.getString("EMPNAME");
                        }
                    }
                }
            } else {
                sql = "SELECT EMPNAME,POST,OFF_EN,post_nomenclature FROM(SELECT OFF_CODE,GPC,EMPNAME,post_nomenclature FROM(SELECT CUR_SPC,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMPNAME,EMP_ID,post_nomenclature FROM EMP_MAST WHERE EMP_ID='" + applicant + "')EMPMAST  "
                        + " LEFT OUTER JOIN G_SPC ON G_SPC.SPC=EMPMAST.CUR_SPC)TAB LEFT OUTER JOIN"
                        + " G_POST ON G_POST.POST_CODE=TAB.GPC LEFT OUTER JOIN G_OFFICE ON TAB.OFF_CODE=G_OFFICE.OFF_CODE";
                rs = st.executeQuery(sql);
                if (rs.next()) {
                    if (rs.getString("POST") != null && !rs.getString("POST").equals("")) {
                        authority = rs.getString("EMPNAME") + "," + rs.getString("POST");
                        if (prid.equals("13")) {
                            if (rs.getString("OFF_EN") != null && !rs.getString("OFF_EN").equals("")) {
                                authority = authority + "<br />" + rs.getString("OFF_EN");
                            }
                        }
                    } else {
                        authority = rs.getString("EMPNAME") + "," + rs.getString("post_nomenclature");
                        if (prid.equals("13")) {
                            if (rs.getString("OFF_EN") != null && !rs.getString("OFF_EN").equals("")) {
                                authority = authority + "<br />" + rs.getString("OFF_EN");
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(st);
        }
        return authority;
    }

    public String getAdditionalChargeSpc(String loginEmpId) {
        PreparedStatement pst = null;
        ResultSet rs = null;
        Connection con = null;
        String spc = "";
        try {
            con = this.dataSource.getConnection();
            pst = con.prepareStatement("SELECT emp_join.SPC,G_SPC.SPN spn,emp_join.EMP_ID,concat_ws(' ', INITIALS ::text, F_NAME::text, M_NAME::text,L_NAME::text) AS EMPNAME,POST FROM EMP_JOIN "
                    + "left outer join emp_relieve on emp_join.join_id=emp_relieve.join_id "
                    + "INNER JOIN EMP_MAST ON emp_join.EMP_ID = EMP_MAST.EMP_ID "
                    + "LEFT OUTER JOIN G_SPC ON G_SPC.SPC=emp_join.SPC "
                    + "LEFT OUTER JOIN G_POST ON G_POST.POST_CODE=G_SPC.GPC "
                    + "WHERE  if_ad_charge='Y' and emp_join.emp_id=? and emp_relieve.join_id is null and emp_join.spc is not null");
            pst.setString(1, loginEmpId);
            rs = pst.executeQuery();

            while (rs.next()) {
                if (spc != null && !spc.equals("")) {
                    spc = spc + "@" + rs.getString("SPC");
                } else {
                    spc = rs.getString("SPC");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return spc;
    }

    @Override
    public List getMyTaskList(String empId, String logedinUserSpc, String parstatus, int page, int rows, String empname, String gpfno, int processId) {
        Connection con = null;
        ResultSet rs = null;
        Statement st = null;
        ArrayList a1 = new ArrayList();

        PreparedStatement pst = null;
        //int firstpage = (page > 1) ? ((page - 1) * rows) + 1 : 1;
        int minlimit = rows * (page - 1);
        int maxlimit = rows;
        TaskListHelperBean lldb = null;
        String emp_mast_index = "";
        String task_master_index = "";
        try {
            con = this.repodataSource.getConnection();

            st = con.createStatement();
            String sql = "";

            if (empname != null && !empname.equals("")) {
                emp_mast_index = "WHERE F_NAME LIKE '%" + empname.toUpperCase() + "%' OR M_NAME LIKE '%" + empname.toUpperCase() + "%' OR L_NAME LIKE '%" + empname.toUpperCase() + "%' OR ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') LIKE '%" + empname.toUpperCase() + "%'";
            } else if (gpfno != null && !gpfno.equals("")) {
                emp_mast_index = "WHERE GPF_NO='" + gpfno + "'";
            }

            if (processId > 0) {
                task_master_index = " AND PROCESS_ID='" + processId + "'";
            }

            if (parstatus != null && !parstatus.equals("") && emp_mast_index.equals("")) {
                if (!parstatus.equals("9")) {

                    sql = "SELECT PROCESS_NAME,TASK_MASTER.PROCESS_ID PROCESS_ID,F_NAME,M_NAME,L_NAME,STATUS_NAME,INITIATED_ON,SPN,INITIATED_BY,TASK_MASTER.TASK_ID,TASK_MASTER.STATUS_ID,G_PROCESS_STATUS.IS_COMPLETED FROM (SELECT * FROM (SELECT TASK_ID,STATUS_ID,PROCESS_ID,INITIATED_BY,INITIATED_SPC,INITIATED_ON FROM TASK_MASTER WHERE PENDING_AT='" + empId + "'" + task_master_index + " AND STATUS_ID = '" + parstatus + "' AND initiated_on::DATE > '2024-01-01')"
                     + " TASK_MASTER)TASK_MASTER"
                     + " LEFT OUTER JOIN G_WORKFLOW_PROCESS ON  TASK_MASTER.PROCESS_ID=G_WORKFLOW_PROCESS.PROCESS_ID"
                     + " INNER JOIN G_PROCESS_STATUS ON TASK_MASTER.STATUS_ID = G_PROCESS_STATUS.STATUS_ID"
                     + " INNER JOIN G_SPC ON TASK_MASTER.INITIATED_SPC = G_SPC.SPC"
                     + " INNER JOIN EMP_MAST ON EMP_MAST.EMP_ID = TASK_MASTER.INITIATED_BY"
                     + " INNER JOIN PAR_MASTER ON TASK_MASTER.TASK_ID=PAR_MASTER.TASK_ID"
                     + " INNER JOIN (SELECT FY,auth_remarks_closed FROM FINANCIAL_YEAR WHERE AUTH_REMARKS_CLOSED='N' and FY != '2023-24')FINANCIAL_YEAR ON PAR_MASTER.FISCAL_YEAR=FINANCIAL_YEAR.FY"
                     + " UNION"
                     + " SELECT PROCESS_NAME,TASK_MASTER.PROCESS_ID PROCESS_ID,F_NAME,M_NAME,L_NAME,STATUS_NAME,INITIATED_ON,SPN,INITIATED_BY,TASK_MASTER.TASK_ID,TASK_MASTER.STATUS_ID,G_PROCESS_STATUS.IS_COMPLETED FROM (SELECT * FROM (SELECT TASK_ID,STATUS_ID,PROCESS_ID,INITIATED_BY,INITIATED_SPC,INITIATED_ON FROM TASK_MASTER WHERE PENDING_AT='" + empId + "'" + task_master_index + " AND STATUS_ID = '" + parstatus + "')"
                     + " TASK_MASTER)TASK_MASTER"
                     + " LEFT OUTER JOIN G_WORKFLOW_PROCESS ON  TASK_MASTER.PROCESS_ID=G_WORKFLOW_PROCESS.PROCESS_ID"
                     + " INNER JOIN G_PROCESS_STATUS ON TASK_MASTER.STATUS_ID = G_PROCESS_STATUS.STATUS_ID"
                     + " INNER JOIN G_SPC ON TASK_MASTER.INITIATED_SPC = G_SPC.SPC"
                     + " INNER JOIN EMP_MAST ON EMP_MAST.EMP_ID = TASK_MASTER.INITIATED_BY"
                     + " INNER JOIN PAR_MASTER ON TASK_MASTER.TASK_ID=PAR_MASTER.TASK_ID"
                     + " INNER JOIN (SELECT FY,auth_remarks_closed FROM FINANCIAL_YEAR WHERE AUTH_REMARKS_CLOSED='N' and FY = '2023-24')FINANCIAL_YEAR ON PAR_MASTER.FISCAL_YEAR=FINANCIAL_YEAR.FY ORDER BY TASK_ID ASC LIMIT " + maxlimit + " OFFSET " + minlimit; 
                   /* sql = "SELECT PROCESS_NAME,TASK_MASTER.PROCESS_ID PROCESS_ID,F_NAME,M_NAME,L_NAME,STATUS_NAME,INITIATED_ON,SPN,INITIATED_BY,TASK_MASTER.TASK_ID,TASK_MASTER.STATUS_ID,G_PROCESS_STATUS.IS_COMPLETED FROM (SELECT * FROM (SELECT TASK_ID,STATUS_ID,PROCESS_ID,INITIATED_BY,INITIATED_SPC,INITIATED_ON FROM TASK_MASTER WHERE PENDING_AT='" + empId + "'" + task_master_index + " AND STATUS_ID = '" + parstatus + "')"
                            + " TASK_MASTER)TASK_MASTER"
                            + " LEFT OUTER JOIN G_WORKFLOW_PROCESS ON  TASK_MASTER.PROCESS_ID=G_WORKFLOW_PROCESS.PROCESS_ID"
                            + " INNER JOIN G_PROCESS_STATUS ON TASK_MASTER.STATUS_ID = G_PROCESS_STATUS.STATUS_ID"
                            + " INNER JOIN G_SPC ON TASK_MASTER.INITIATED_SPC = G_SPC.SPC"
                            + " INNER JOIN EMP_MAST ON EMP_MAST.EMP_ID = TASK_MASTER.INITIATED_BY"
                            + " INNER JOIN PAR_MASTER ON TASK_MASTER.TASK_ID=PAR_MASTER.TASK_ID"
                            + " INNER JOIN (SELECT FY,auth_remarks_closed FROM FINANCIAL_YEAR WHERE AUTH_REMARKS_CLOSED='N')FINANCIAL_YEAR ON PAR_MASTER.FISCAL_YEAR=FINANCIAL_YEAR.FY"
                            + " ORDER BY TASK_ID ASC LIMIT " + maxlimit + " OFFSET " + minlimit;*/

                    rs = st.executeQuery(sql);
                    int i = 0;
                    while (rs.next()) {
                        i++;
                        lldb = new TaskListHelperBean();
                        lldb.setCount(i);
                        lldb.setDateOfInitiation(CommonFunctions.getFormattedOutputDate(rs.getDate("INITIATED_ON")));
                        lldb.setDateOfInitiationAsString(CommonFunctions.getFormattedOutputDate1(rs.getDate("INITIATED_ON")));
                        lldb.setProcessname(rs.getString("PROCESS_NAME"));
                        lldb.setApplicant(getApplicantName(con, rs.getString("INITIATED_BY"), rs.getString("PROCESS_ID"), rs.getInt("TASK_ID")));
                        lldb.setTaskId(rs.getInt("TASK_ID"));
                        lldb.setStatusId(rs.getInt("STATUS_ID"));
                        if (lldb.getStatusId() > 0) {
                            if (lldb.getStatusId() == 6) {
                                lldb.setStatus("PENDING AS REPORTING AUTHORITY");
                            } else if (lldb.getStatusId() == 7) {
                                lldb.setStatus("PENDING AS REVIEWING AUTHORITY");
                            } else if (lldb.getStatusId() == 8) {
                                lldb.setStatus("PENDING AS ACCEPTING AUTHORITY");
                            }
                        }
                        lldb.setAppEmpCode(rs.getString("INITIATED_BY"));
                        lldb.setProcessId(rs.getInt("PROCESS_ID"));
                        lldb.setIstaskcompleted(rs.getString("IS_COMPLETED"));
                        /*if(rs.getString("STATUS_ID") != null && rs.getString("STATUS_ID").equals("8")){
                         lldb.setGrading(rs.getString("OVERALLGRADING"));    
                         }*/
                        lldb.setAuth("");
                        a1.add(lldb);
                        Collections.sort(a1);
                    }
                } else if (parstatus.equals("9")) {
                    /*String reportsql = "SELECT SUBMITTED_ON,ACTIONURL,PAR_MASTER.PARID,PROCESS_NAME,TASK_MASTER.PROCESS_ID PROCESS_ID,F_NAME,M_NAME,L_NAME,STATUS_NAME,INITIATED_ON,"
                     + " SPN,INITIATED_BY,TASK_MASTER.TASK_ID,TASK_MASTER.STATUS_ID,PAR_REPORTING_TRAN.IS_COMPLETED FROM"
                     + " (SELECT PAR_ID,REPORTING_EMP_ID,REPORTING_CUR_SPC,SUBMITTED_ON,IS_COMPLETED FROM PAR_REPORTING_TRAN"
                     + " WHERE REPORTING_EMP_ID='" + empId + "' AND (IS_COMPLETED='Y' OR IS_COMPLETED='F'))PAR_REPORTING_TRAN"
                     + " INNER JOIN PAR_MASTER ON PAR_REPORTING_TRAN.PAR_ID=PAR_MASTER.PARID"
                     + " INNER JOIN TASK_MASTER ON PAR_MASTER.TASK_ID=TASK_MASTER.TASK_ID"
                     + " LEFT OUTER JOIN G_WORKFLOW_PROCESS ON  TASK_MASTER.PROCESS_ID=G_WORKFLOW_PROCESS.PROCESS_ID"
                     + " INNER JOIN G_PROCESS_STATUS ON TASK_MASTER.STATUS_ID = G_PROCESS_STATUS.STATUS_ID"
                     + " INNER JOIN G_SPC ON TASK_MASTER.INITIATED_SPC = G_SPC.SPC"
                     + " INNER JOIN EMP_MAST ON EMP_MAST.EMP_ID = TASK_MASTER.INITIATED_BY ORDER BY TASK_ID";
                     rs = st.executeQuery(reportsql);
                     int i = 0;
                     String actionurl = "";
                     while (rs.next()) {
                     actionurl = rs.getString("ACTIONURL");
                     actionurl = actionurl.substring(1);
                     i++;
                     lldb = new TaskListHelperBean();
                     lldb.setCount(i);
                     lldb.setSubmitted_on(CommonFunctions.getFormattedOutputDate1(rs.getDate("SUBMITTED_ON")));
                     lldb.setDateOfInitiation(CommonFunctions.getFormattedOutputDate(rs.getDate("INITIATED_ON")));
                     lldb.setDateOfInitiationAsString(CommonFunctions.getFormattedOutputDate1(rs.getDate("INITIATED_ON")));
                     lldb.setProcessname(rs.getString("PROCESS_NAME"));
                     lldb.setApplicant(getApplicantName(con, rs.getString("INITIATED_BY"), rs.getString("PROCESS_ID"), rs.getInt("TASK_ID")));
                     //lldb.setStatus(rs.getString("STATUS_NAME"));
                     if ((rs.getString("IS_COMPLETED") != null && rs.getString("IS_COMPLETED").equals("Y")) && (rs.getString("STATUS_ID") != null && rs.getString("STATUS_ID").equals("21"))) {
                     lldb.setStatus("PAR ADVERSED BY CUSTODIAN");
                     } else if (rs.getString("IS_COMPLETED") != null && rs.getString("IS_COMPLETED").equals("Y")) {
                     lldb.setStatus("COMPLETED AS REPORTING AUTHORITY");
                     } else if (rs.getString("IS_COMPLETED") != null && rs.getString("IS_COMPLETED").equals("F")) {
                     lldb.setStatus("FORCE FORWARDED BY SYSTEM");
                     }
                     lldb.setTaskId(rs.getInt("TASK_ID"));
                     lldb.setStatusId(9);
                     lldb.setAppEmpCode(rs.getString("INITIATED_BY"));
                     lldb.setProcessId(rs.getInt("PROCESS_ID"));
                     lldb.setIstaskcompleted("<a href='" + actionurl + rs.getString("TASK_ID") + "&auth=REPORTING' target='_blank'>View</a>");
                     lldb.setAuth("REPORTING");
                     a1.add(lldb);
                     Collections.sort(a1);
                     }
                     String reviewsql = "SELECT SUBMITTED_ON,ACTIONURL,PAR_MASTER.PARID,PROCESS_NAME,TASK_MASTER.PROCESS_ID PROCESS_ID,F_NAME,M_NAME,L_NAME,STATUS_NAME,INITIATED_ON,"
                     + " SPN,INITIATED_BY,TASK_MASTER.TASK_ID,TASK_MASTER.STATUS_ID,PAR_REVIEWING_TRAN.IS_COMPLETED FROM"
                     + " (SELECT PAR_ID,REVIEWING_EMP_ID,REVIEWING_CUR_SPC,SUBMITTED_ON,IS_COMPLETED FROM PAR_REVIEWING_TRAN"
                     + " WHERE REVIEWING_EMP_ID='" + empId + "' AND (IS_COMPLETED='Y' OR IS_COMPLETED='F'))PAR_REVIEWING_TRAN"
                     + " INNER JOIN PAR_MASTER ON PAR_REVIEWING_TRAN.PAR_ID=PAR_MASTER.PARID"
                     + " INNER JOIN TASK_MASTER ON PAR_MASTER.TASK_ID=TASK_MASTER.TASK_ID"
                     + " LEFT OUTER JOIN G_WORKFLOW_PROCESS ON TASK_MASTER.PROCESS_ID=G_WORKFLOW_PROCESS.PROCESS_ID"
                     + " INNER JOIN G_PROCESS_STATUS ON TASK_MASTER.STATUS_ID = G_PROCESS_STATUS.STATUS_ID"
                     + " INNER JOIN G_SPC ON TASK_MASTER.INITIATED_SPC = G_SPC.SPC"
                     + " INNER JOIN EMP_MAST ON EMP_MAST.EMP_ID = TASK_MASTER.INITIATED_BY ORDER BY TASK_ID";
                     rs = st.executeQuery(reviewsql);
                     while (rs.next()) {
                     actionurl = rs.getString("ACTIONURL");
                     actionurl = actionurl.substring(1);
                     i++;
                     lldb = new TaskListHelperBean();
                     lldb.setCount(i);
                     lldb.setSubmitted_on(CommonFunctions.getFormattedOutputDate1(rs.getDate("SUBMITTED_ON")));
                     lldb.setDateOfInitiation(CommonFunctions.getFormattedOutputDate(rs.getDate("INITIATED_ON")));
                     lldb.setDateOfInitiationAsString(CommonFunctions.getFormattedOutputDate1(rs.getDate("INITIATED_ON")));
                     lldb.setProcessname(rs.getString("PROCESS_NAME"));
                     lldb.setApplicant(getApplicantName(con, rs.getString("INITIATED_BY"), rs.getString("PROCESS_ID"), rs.getInt("TASK_ID")));
                     //lldb.setStatus(rs.getString("STATUS_NAME"));
                     if ((rs.getString("IS_COMPLETED") != null && rs.getString("IS_COMPLETED").equals("Y")) && (rs.getString("STATUS_ID") != null && rs.getString("STATUS_ID").equals("21"))) {
                     lldb.setStatus("PAR ADVERSED BY CUSTODIAN");
                     } else if (rs.getString("IS_COMPLETED") != null && rs.getString("IS_COMPLETED").equals("Y")) {
                     lldb.setStatus("COMPLETED AS REVIEWING AUTHORITY");
                     } else if (rs.getString("IS_COMPLETED") != null && rs.getString("IS_COMPLETED").equals("F")) {
                     lldb.setStatus("FORCE FORWARDED BY SYSTEM");
                     }
                     lldb.setTaskId(rs.getInt("TASK_ID"));
                     lldb.setStatusId(9);
                     lldb.setAppEmpCode(rs.getString("INITIATED_BY"));
                     lldb.setProcessId(rs.getInt("PROCESS_ID"));
                     lldb.setIstaskcompleted("<a href='" + actionurl + rs.getString("TASK_ID") + "&auth=REVIEWING' target='_blank'>View</a>");
                     lldb.setAuth("REVIEWING");
                     a1.add(lldb);
                     Collections.sort(a1);
                     }
                     String acceptsql = "SELECT SUBMITTED_ON,ACTIONURL,PAR_MASTER.PARID,PROCESS_NAME,TASK_MASTER.PROCESS_ID PROCESS_ID,F_NAME,M_NAME,L_NAME,STATUS_NAME,INITIATED_ON,"
                     + " SPN,INITIATED_BY,TASK_MASTER.TASK_ID,TASK_MASTER.STATUS_ID,PAR_ACCEPTING_TRAN.IS_COMPLETED FROM"
                     + " (SELECT PAR_ID,ACCEPTING_EMP_ID,ACCEPTING_CUR_SPC,SUBMITTED_ON,IS_COMPLETED FROM PAR_ACCEPTING_TRAN"
                     + " WHERE ACCEPTING_EMP_ID='" + empId + "' AND (IS_COMPLETED='Y' OR IS_COMPLETED='F'))PAR_ACCEPTING_TRAN"
                     + " INNER JOIN PAR_MASTER ON PAR_ACCEPTING_TRAN.PAR_ID=PAR_MASTER.PARID"
                     + " INNER JOIN TASK_MASTER ON PAR_MASTER.TASK_ID=TASK_MASTER.TASK_ID"
                     + " LEFT OUTER JOIN G_WORKFLOW_PROCESS ON TASK_MASTER.PROCESS_ID=G_WORKFLOW_PROCESS.PROCESS_ID"
                     + " INNER JOIN G_PROCESS_STATUS ON TASK_MASTER.STATUS_ID = G_PROCESS_STATUS.STATUS_ID"
                     + " INNER JOIN G_SPC ON TASK_MASTER.INITIATED_SPC = G_SPC.SPC"
                     + " INNER JOIN EMP_MAST ON EMP_MAST.EMP_ID = TASK_MASTER.INITIATED_BY ORDER BY TASK_ID";
                     rs = st.executeQuery(acceptsql);
                     while (rs.next()) {
                     actionurl = rs.getString("ACTIONURL");
                     actionurl = actionurl.substring(1);
                     i++;
                     lldb = new TaskListHelperBean();
                     lldb.setCount(i);
                     lldb.setSubmitted_on(CommonFunctions.getFormattedOutputDate1(rs.getDate("SUBMITTED_ON")));
                     lldb.setDateOfInitiation(CommonFunctions.getFormattedOutputDate(rs.getDate("INITIATED_ON")));
                     lldb.setDateOfInitiationAsString(CommonFunctions.getFormattedOutputDate1(rs.getDate("INITIATED_ON")));
                     lldb.setProcessname(rs.getString("PROCESS_NAME"));
                     lldb.setApplicant(getApplicantName(con, rs.getString("INITIATED_BY"), rs.getString("PROCESS_ID"), rs.getInt("TASK_ID")));
                     //lldb.setStatus(rs.getString("STATUS_NAME"));
                     if ((rs.getString("IS_COMPLETED") != null && rs.getString("IS_COMPLETED").equals("Y")) && (rs.getString("STATUS_ID") != null && rs.getString("STATUS_ID").equals("21"))) {
                     lldb.setStatus("PAR ADVERSED BY CUSTODIAN");
                     } else if (rs.getString("IS_COMPLETED") != null && rs.getString("IS_COMPLETED").equals("Y")) {
                     lldb.setStatus("COMPLETED AS ACCEPTING AUTHORITY");
                     } else if (rs.getString("IS_COMPLETED") != null && rs.getString("IS_COMPLETED").equals("F")) {
                     lldb.setStatus("FORCE FORWARDED BY SYSTEM");
                     }
                     lldb.setTaskId(rs.getInt("TASK_ID"));
                     lldb.setStatusId(9);
                     lldb.setAppEmpCode(rs.getString("INITIATED_BY"));
                     lldb.setProcessId(rs.getInt("PROCESS_ID"));
                     lldb.setIstaskcompleted("<a href='" + actionurl + rs.getString("TASK_ID") + "&auth=ACCEPTING' target='_blank'>View</a>");
                     lldb.setAuth("ACCEPTING");
                     a1.add(lldb);
                     Collections.sort(a1);
                     }*/
                    int i = 0;
                    String actionurl = "";
                    String completedsql = "select * FROM (SELECT SUBMITTED_ON,ACTIONURL,PAR_MASTER.PARID,PROCESS_NAME,TASK_MASTER.PROCESS_ID PROCESS_ID,F_NAME,M_NAME,L_NAME,STATUS_NAME,INITIATED_ON,"
                            + " SPN,INITIATED_BY,TASK_MASTER.TASK_ID,TASK_MASTER.STATUS_ID,PAR_REPORTING_TRAN.IS_COMPLETED,'REP' PUSER FROM"
                            + " (SELECT PAR_ID,REPORTING_EMP_ID,REPORTING_CUR_SPC,SUBMITTED_ON,IS_COMPLETED FROM PAR_REPORTING_TRAN"
                            + " WHERE REPORTING_EMP_ID=? AND (IS_COMPLETED='Y' OR IS_COMPLETED='F'))PAR_REPORTING_TRAN"
                            + " INNER JOIN PAR_MASTER ON PAR_REPORTING_TRAN.PAR_ID=PAR_MASTER.PARID"
                            + " INNER JOIN TASK_MASTER ON PAR_MASTER.TASK_ID=TASK_MASTER.TASK_ID"
                            + " LEFT OUTER JOIN G_WORKFLOW_PROCESS ON  TASK_MASTER.PROCESS_ID=G_WORKFLOW_PROCESS.PROCESS_ID"
                            + " INNER JOIN G_PROCESS_STATUS ON TASK_MASTER.STATUS_ID = G_PROCESS_STATUS.STATUS_ID"
                            + " INNER JOIN G_SPC ON TASK_MASTER.INITIATED_SPC = G_SPC.SPC"
                            + " INNER JOIN EMP_MAST ON EMP_MAST.EMP_ID = TASK_MASTER.INITIATED_BY"
                            + " UNION"
                            + " SELECT SUBMITTED_ON,ACTIONURL,PAR_MASTER.PARID,PROCESS_NAME,TASK_MASTER.PROCESS_ID PROCESS_ID,F_NAME,M_NAME,L_NAME,STATUS_NAME,INITIATED_ON,"
                            + " SPN,INITIATED_BY,TASK_MASTER.TASK_ID,TASK_MASTER.STATUS_ID,PAR_REVIEWING_TRAN.IS_COMPLETED,'REV' PUSER FROM"
                            + " (SELECT PAR_ID,REVIEWING_EMP_ID,REVIEWING_CUR_SPC,SUBMITTED_ON,IS_COMPLETED FROM PAR_REVIEWING_TRAN"
                            + " WHERE REVIEWING_EMP_ID=? AND (IS_COMPLETED='Y' OR IS_COMPLETED='F'))PAR_REVIEWING_TRAN"
                            + " INNER JOIN PAR_MASTER ON PAR_REVIEWING_TRAN.PAR_ID=PAR_MASTER.PARID"
                            + " INNER JOIN TASK_MASTER ON PAR_MASTER.TASK_ID=TASK_MASTER.TASK_ID"
                            + " LEFT OUTER JOIN G_WORKFLOW_PROCESS ON TASK_MASTER.PROCESS_ID=G_WORKFLOW_PROCESS.PROCESS_ID"
                            + " INNER JOIN G_PROCESS_STATUS ON TASK_MASTER.STATUS_ID = G_PROCESS_STATUS.STATUS_ID"
                            + " INNER JOIN G_SPC ON TASK_MASTER.INITIATED_SPC = G_SPC.SPC"
                            + " INNER JOIN EMP_MAST ON EMP_MAST.EMP_ID = TASK_MASTER.INITIATED_BY"
                            + " UNION"
                            + " SELECT SUBMITTED_ON,ACTIONURL,PAR_MASTER.PARID,PROCESS_NAME,TASK_MASTER.PROCESS_ID PROCESS_ID,F_NAME,M_NAME,L_NAME,STATUS_NAME,INITIATED_ON,"
                            + " SPN,INITIATED_BY,TASK_MASTER.TASK_ID,TASK_MASTER.STATUS_ID,PAR_ACCEPTING_TRAN.IS_COMPLETED,'ACP' PUSER FROM"
                            + " (SELECT PAR_ID,ACCEPTING_EMP_ID,ACCEPTING_CUR_SPC,SUBMITTED_ON,IS_COMPLETED FROM PAR_ACCEPTING_TRAN"
                            + " WHERE ACCEPTING_EMP_ID=? AND (IS_COMPLETED='Y' OR IS_COMPLETED='F'))PAR_ACCEPTING_TRAN"
                            + " INNER JOIN PAR_MASTER ON PAR_ACCEPTING_TRAN.PAR_ID=PAR_MASTER.PARID"
                            + " INNER JOIN TASK_MASTER ON PAR_MASTER.TASK_ID=TASK_MASTER.TASK_ID"
                            + " LEFT OUTER JOIN G_WORKFLOW_PROCESS ON TASK_MASTER.PROCESS_ID=G_WORKFLOW_PROCESS.PROCESS_ID"
                            + " INNER JOIN G_PROCESS_STATUS ON TASK_MASTER.STATUS_ID = G_PROCESS_STATUS.STATUS_ID"
                            + " INNER JOIN G_SPC ON TASK_MASTER.INITIATED_SPC = G_SPC.SPC"
                            + " INNER JOIN EMP_MAST ON EMP_MAST.EMP_ID = TASK_MASTER.INITIATED_BY)temp ORDER BY TASK_ID LIMIT " + maxlimit + " OFFSET " + minlimit;
                    pst = con.prepareStatement(completedsql);
                    pst.setString(1, empId);
                    pst.setString(2, empId);
                    pst.setString(3, empId);
                    rs = pst.executeQuery();
                    while (rs.next()) {
                        actionurl = rs.getString("ACTIONURL");
                        actionurl = actionurl.substring(1);
                        i++;
                        lldb = new TaskListHelperBean();
                        lldb.setCount(i);
                        lldb.setSubmitted_on(CommonFunctions.getFormattedOutputDate1(rs.getDate("SUBMITTED_ON")));
                        lldb.setDateOfInitiation(CommonFunctions.getFormattedOutputDate(rs.getDate("INITIATED_ON")));
                        lldb.setDateOfInitiationAsString(CommonFunctions.getFormattedOutputDate1(rs.getDate("INITIATED_ON")));
                        lldb.setProcessname(rs.getString("PROCESS_NAME"));
                        lldb.setApplicant(getApplicantName(con, rs.getString("INITIATED_BY"), rs.getString("PROCESS_ID"), rs.getInt("TASK_ID")));
                        //lldb.setStatus(rs.getString("STATUS_NAME"));
                        if ((rs.getString("IS_COMPLETED") != null && rs.getString("IS_COMPLETED").equals("Y")) && (rs.getString("STATUS_ID") != null && rs.getString("STATUS_ID").equals("21"))) {
                            lldb.setStatus("PAR ADVERSED BY CUSTODIAN");
                        } else if (rs.getString("IS_COMPLETED") != null && rs.getString("IS_COMPLETED").equals("Y")) {
                            if (rs.getString("PUSER").equals("REP")) {
                                lldb.setStatus("COMPLETED AS REPORTING AUTHORITY");
                            } else if (rs.getString("PUSER").equals("REV")) {
                                lldb.setStatus("COMPLETED AS REVIEWING AUTHORITY");
                            } else if (rs.getString("PUSER").equals("ACP")) {
                                lldb.setStatus("COMPLETED AS ACCEPTING AUTHORITY");
                            }
                        } else if (rs.getString("IS_COMPLETED") != null && rs.getString("IS_COMPLETED").equals("F")) {
                            lldb.setStatus("FORCE FORWARDED BY SYSTEM");
                        }
                        lldb.setTaskId(rs.getInt("TASK_ID"));
                        lldb.setStatusId(9);
                        lldb.setAppEmpCode(rs.getString("INITIATED_BY"));
                        lldb.setProcessId(rs.getInt("PROCESS_ID"));
                        if (rs.getString("PUSER").equals("REP")) {
                            lldb.setIstaskcompleted("<a href='" + actionurl + rs.getString("TASK_ID") + "&auth=REPORTING' target='_blank'>View</a>");
                            lldb.setAuth("REPORTING");
                        } else if (rs.getString("PUSER").equals("REV")) {
                            lldb.setIstaskcompleted("<a href='" + actionurl + rs.getString("TASK_ID") + "&auth=REVIEWING' target='_blank'>View</a>");
                            lldb.setAuth("REVIEWING");
                        } else if (rs.getString("PUSER").equals("ACP")) {
                            lldb.setIstaskcompleted("<a href='" + actionurl + rs.getString("TASK_ID") + "&auth=ACCEPTING' target='_blank'>View</a>");
                            lldb.setAuth("ACCEPTING");
                        }
                        a1.add(lldb);
                        Collections.sort(a1);
                    }
                }
            } else if (parstatus != null && !parstatus.equals("") && emp_mast_index != null && !emp_mast_index.equals("")) {
                if (parstatus.equals("6") || parstatus.equals("7") || parstatus.equals("8")) {
                    sql = "SELECT PROCESS_NAME,TASK_MASTER.PROCESS_ID PROCESS_ID,F_NAME,M_NAME,L_NAME,STATUS_NAME,INITIATED_ON,SPN,INITIATED_BY,TASK_MASTER.TASK_ID,G_PROCESS_STATUS.STATUS_ID,IS_COMPLETED FROM ("
                            + " SELECT * FROM (SELECT TASK_ID,STATUS_ID,PROCESS_ID,INITIATED_BY,INITIATED_SPC,INITIATED_ON FROM TASK_MASTER WHERE PENDING_AT='" + empId + "'" + task_master_index + " AND (STATUS_ID != '16') ORDER BY INITIATED_ON DESC)"
                            + " TASK_MASTER)TASK_MASTER"
                            + " LEFT OUTER JOIN G_WORKFLOW_PROCESS ON TASK_MASTER.PROCESS_ID=G_WORKFLOW_PROCESS.PROCESS_ID"
                            + " INNER JOIN G_PROCESS_STATUS ON TASK_MASTER.STATUS_ID = G_PROCESS_STATUS.STATUS_ID"
                            + " INNER JOIN G_SPC ON TASK_MASTER.INITIATED_SPC = G_SPC.SPC"
                            + " INNER JOIN (SELECT EMP_ID,F_NAME,M_NAME,L_NAME FROM EMP_MAST " + emp_mast_index + ")EMP_MAST ON EMP_MAST.EMP_ID = TASK_MASTER.INITIATED_BY"
                            + " LEFT OUTER JOIN PAR_ADVERSE_COMM ON TASK_MASTER.TASK_ID=PAR_ADVERSE_COMM.TASK_ID"
                            + " INNER JOIN PAR_MASTER ON TASK_MASTER.TASK_ID=PAR_MASTER.TASK_ID"
                            + " INNER JOIN (SELECT FY,auth_remarks_closed FROM FINANCIAL_YEAR WHERE AUTH_REMARKS_CLOSED='N')FINANCIAL_YEAR ON PAR_MASTER.FISCAL_YEAR=FINANCIAL_YEAR.FY ORDER BY TASK_ID DESC";
                    rs = st.executeQuery(sql);
                    int i = 0;
                    while (rs.next()) {
                        i++;
                        lldb = new TaskListHelperBean();
                        lldb.setCount(i);
                        lldb.setDateOfInitiation(CommonFunctions.getFormattedOutputDate(rs.getDate("INITIATED_ON")));
                        lldb.setDateOfInitiationAsString(CommonFunctions.getFormattedOutputDate1(rs.getDate("INITIATED_ON")));
                        lldb.setProcessname(rs.getString("PROCESS_NAME"));
                        lldb.setApplicant(getApplicantName(con, rs.getString("INITIATED_BY"), rs.getString("PROCESS_ID"), rs.getInt("TASK_ID")));
                        lldb.setTaskId(rs.getInt("TASK_ID"));
                        lldb.setStatusId(rs.getInt("STATUS_ID"));
                        if (lldb.getStatusId() > 0) {
                            if (lldb.getStatusId() == 6) {
                                lldb.setStatus("PENDING AS REPORTING AUTHORITY");
                            } else if (lldb.getStatusId() == 7) {
                                lldb.setStatus("PENDING AS REVIEWING AUTHORITY");
                            } else if (lldb.getStatusId() == 8) {
                                lldb.setStatus("PENDING AS ACCEPTING AUTHORITY");
                            }
                        }
                        lldb.setAppEmpCode(rs.getString("INITIATED_BY"));
                        lldb.setProcessId(rs.getInt("PROCESS_ID"));
                        lldb.setIstaskcompleted(rs.getString("IS_COMPLETED"));
                        /*if(rs.getString("STATUS_ID") != null && rs.getString("STATUS_ID").equals("8")){
                         lldb.setGrading(rs.getString("OVERALLGRADING"));    
                         }*/
                        lldb.setAuth("");
                        a1.add(lldb);
                        Collections.sort(a1);
                    }
                }
            } else {
                if (rows == 0) {
                    sql = "SELECT RECIEV_AUTH_TYPE,PROCESS_NAME,TASK_MASTER.PROCESS_ID PROCESS_ID,F_NAME,M_NAME,L_NAME,STATUS_NAME,INITIATED_ON,SPN,INITIATED_BY,TASK_MASTER.TASK_ID,TASK_MASTER.STATUS_ID,IS_COMPLETED FROM (SELECT * FROM (SELECT TASK_ID,STATUS_ID,PROCESS_ID,INITIATED_BY,INITIATED_SPC,INITIATED_ON FROM TASK_MASTER WHERE PENDING_AT='" + empId + "' AND STATUS_ID != '16')"
                            + " TASK_MASTER)TASK_MASTER"
                            + " LEFT OUTER JOIN G_WORKFLOW_PROCESS ON  TASK_MASTER.PROCESS_ID=G_WORKFLOW_PROCESS.PROCESS_ID"
                            + " INNER JOIN G_PROCESS_STATUS ON TASK_MASTER.STATUS_ID = G_PROCESS_STATUS.STATUS_ID"
                            + " INNER JOIN G_SPC ON TASK_MASTER.INITIATED_SPC = G_SPC.SPC"
                            + " INNER JOIN (SELECT EMP_ID,F_NAME,M_NAME,L_NAME FROM EMP_MAST)EMP_MAST ON EMP_MAST.EMP_ID = TASK_MASTER.INITIATED_BY"
                            + " LEFT OUTER JOIN PAR_ADVERSE_COMM ON TASK_MASTER.TASK_ID=PAR_ADVERSE_COMM.TASK_ID"
                            + " LEFT OUTER JOIN PAR_MASTER ON TASK_MASTER.TASK_ID=PAR_MASTER.TASK_ID"
                            + " LEFT OUTER JOIN (SELECT FY,IS_CLOSED FROM FINANCIAL_YEAR WHERE IS_CLOSED='N')FINANCIAL_YEAR ON PAR_MASTER.FISCAL_YEAR=FINANCIAL_YEAR.FY ORDER BY TASK_ID";
                    rs = st.executeQuery(sql);
                    int i = 0;
                    while (rs.next()) {
                        i++;
                        lldb = new TaskListHelperBean();
                        lldb.setCount(i);
                        lldb.setDateOfInitiation(CommonFunctions.getFormattedOutputDate(rs.getDate("INITIATED_ON")));
                        lldb.setDateOfInitiationAsString(CommonFunctions.getFormattedOutputDate1(rs.getDate("INITIATED_ON")));
                        lldb.setProcessname(rs.getString("PROCESS_NAME"));
                        lldb.setApplicant(getApplicantName(con, rs.getString("INITIATED_BY"), rs.getString("PROCESS_ID"), rs.getInt("TASK_ID")));
                        lldb.setTaskId(rs.getInt("TASK_ID"));
                        lldb.setStatusId(rs.getInt("STATUS_ID"));
                        if (lldb.getStatusId() > 0) {
                            if (lldb.getStatusId() == 6) {
                                lldb.setStatus("PENDING AS REPORTING AUTHORITY");
                            } else if (lldb.getStatusId() == 7) {
                                lldb.setStatus("PENDING AS REVIEWING AUTHORITY");
                            } else if (lldb.getStatusId() == 8) {
                                lldb.setStatus("PENDING AS ACCEPTING AUTHORITY");
                            } else if (lldb.getStatusId() == 21) {
                                lldb.setApplicant(getPARApplicant(con, rs.getString("INITIATED_BY"), "21"));
                                lldb.setStatus("PAR IS ADVERSED BY CUSTODIAN");
                            } else {
                                lldb.setStatus(rs.getString("STATUS_NAME"));
                            }
                        }
                        lldb.setAppEmpCode(rs.getString("INITIATED_BY"));
                        lldb.setProcessId(rs.getInt("PROCESS_ID"));
                        lldb.setIstaskcompleted(rs.getString("IS_COMPLETED"));
                        /*if(rs.getString("STATUS_ID") != null && rs.getString("STATUS_ID").equals("8")){
                         lldb.setGrading(rs.getString("OVERALLGRADING"));    
                         }*/
                        lldb.setAuth("");
                        a1.add(lldb);
                        Collections.sort(a1);
                    }
                } else {
                    /*sql = "SELECT IS_CLOSED,RECIEV_AUTH_TYPE,PROCESS_NAME,TASK_MASTER.PROCESS_ID PROCESS_ID,F_NAME,M_NAME,L_NAME,STATUS_NAME,INITIATED_ON,SPN,INITIATED_BY,TASK_MASTER.TASK_ID,G_PROCESS_STATUS.STATUS_ID,IS_COMPLETED FROM ("
                     + "SELECT * FROM (SELECT TASK_ID,STATUS_ID,PROCESS_ID,INITIATED_BY,INITIATED_SPC,INITIATED_ON FROM TASK_MASTER WHERE PENDING_AT='" + empId + "'" + task_master_index + " AND (STATUS_ID != '16') ORDER BY INITIATED_ON DESC)"
                     + " TASK_MASTER)TASK_MASTER"
                     + " LEFT OUTER JOIN G_WORKFLOW_PROCESS ON TASK_MASTER.PROCESS_ID=G_WORKFLOW_PROCESS.PROCESS_ID"
                     + " INNER JOIN G_PROCESS_STATUS ON TASK_MASTER.STATUS_ID = G_PROCESS_STATUS.STATUS_ID"
                     + " INNER JOIN G_SPC ON TASK_MASTER.INITIATED_SPC = G_SPC.SPC"
                     + " INNER JOIN (SELECT EMP_ID,F_NAME,M_NAME,L_NAME FROM EMP_MAST " + emp_mast_index + ")EMP_MAST ON EMP_MAST.EMP_ID = TASK_MASTER.INITIATED_BY"
                     + " LEFT OUTER JOIN PAR_ADVERSE_COMM ON TASK_MASTER.TASK_ID=PAR_ADVERSE_COMM.TASK_ID"
                     + " LEFT OUTER JOIN PAR_MASTER ON TASK_MASTER.TASK_ID=PAR_MASTER.TASK_ID"
                     + " LEFT OUTER JOIN FINANCIAL_YEAR ON PAR_MASTER.FISCAL_YEAR=FINANCIAL_YEAR.FY ORDER BY TASK_ID";*/
                    if (task_master_index != null && !task_master_index.equals("")) {
                        if (processId != 3) {
                            sql = "SELECT PROCESS_NAME,TASK_MASTER.PROCESS_ID PROCESS_ID,F_NAME,M_NAME,L_NAME,STATUS_NAME,INITIATED_ON,SPN,INITIATED_BY,TASK_MASTER.TASK_ID,G_PROCESS_STATUS.STATUS_ID,IS_COMPLETED FROM ("
                                    + " SELECT * FROM (SELECT TASK_ID,STATUS_ID,PROCESS_ID,INITIATED_BY,INITIATED_SPC,INITIATED_ON FROM TASK_MASTER WHERE PENDING_AT='" + empId + "'" + task_master_index + " AND (STATUS_ID != '16') ORDER BY INITIATED_ON DESC)"
                                    + " TASK_MASTER)TASK_MASTER"
                                    + " LEFT OUTER JOIN G_WORKFLOW_PROCESS ON TASK_MASTER.PROCESS_ID=G_WORKFLOW_PROCESS.PROCESS_ID"
                                    + " INNER JOIN G_PROCESS_STATUS ON TASK_MASTER.STATUS_ID = G_PROCESS_STATUS.STATUS_ID"
                                    + " INNER JOIN G_SPC ON TASK_MASTER.INITIATED_SPC = G_SPC.SPC"
                                    + " INNER JOIN (SELECT EMP_ID,F_NAME,M_NAME,L_NAME FROM EMP_MAST " + emp_mast_index + ")EMP_MAST ON EMP_MAST.EMP_ID = TASK_MASTER.INITIATED_BY"
                                    + " LEFT OUTER JOIN PAR_ADVERSE_COMM ON TASK_MASTER.TASK_ID=PAR_ADVERSE_COMM.TASK_ID ORDER BY TASK_ID DESC";
                        } else {
                            sql = "SELECT PROCESS_NAME,TASK_MASTER.PROCESS_ID PROCESS_ID,F_NAME,M_NAME,L_NAME,STATUS_NAME,INITIATED_ON,SPN,INITIATED_BY,TASK_MASTER.TASK_ID,G_PROCESS_STATUS.STATUS_ID,IS_COMPLETED FROM ("
                                    + " SELECT * FROM (SELECT TASK_ID,STATUS_ID,PROCESS_ID,INITIATED_BY,INITIATED_SPC,INITIATED_ON FROM TASK_MASTER WHERE PENDING_AT='" + empId + "'" + task_master_index + " AND (STATUS_ID != '16') AND initiated_on::DATE > '2023-08-01' ORDER BY INITIATED_ON DESC)"
                                    + " TASK_MASTER)TASK_MASTER"
                                    + " LEFT OUTER JOIN G_WORKFLOW_PROCESS ON TASK_MASTER.PROCESS_ID=G_WORKFLOW_PROCESS.PROCESS_ID"
                                    + " INNER JOIN G_PROCESS_STATUS ON TASK_MASTER.STATUS_ID = G_PROCESS_STATUS.STATUS_ID"
                                    + " INNER JOIN G_SPC ON TASK_MASTER.INITIATED_SPC = G_SPC.SPC"
                                    + " INNER JOIN (SELECT EMP_ID,F_NAME,M_NAME,L_NAME FROM EMP_MAST " + emp_mast_index + ")EMP_MAST ON EMP_MAST.EMP_ID = TASK_MASTER.INITIATED_BY"
                                    + " LEFT OUTER JOIN PAR_ADVERSE_COMM ON TASK_MASTER.TASK_ID=PAR_ADVERSE_COMM.TASK_ID"
                                    + " INNER JOIN PAR_MASTER ON TASK_MASTER.TASK_ID=PAR_MASTER.TASK_ID"
                                    + " INNER JOIN (SELECT FY,auth_remarks_closed FROM FINANCIAL_YEAR WHERE AUTH_REMARKS_CLOSED='N')FINANCIAL_YEAR ON PAR_MASTER.FISCAL_YEAR=FINANCIAL_YEAR.FY"
                                    + " ORDER BY TASK_ID DESC";
                        }
                    } else {
                        /*if (cadrecode != null && cadrecode.equals("1484")) {
                         sql = "SELECT '' auth_remarks_closed,'' RECIEV_AUTH_TYPE,PROCESS_NAME,TASK_MASTER.PROCESS_ID PROCESS_ID,F_NAME,M_NAME,L_NAME,STATUS_NAME,"
                         + " INITIATED_ON,SPN,INITIATED_BY,TASK_MASTER.TASK_ID,G_PROCESS_STATUS.STATUS_ID,IS_COMPLETED FROM ("
                         + " SELECT * FROM (SELECT TASK_ID,STATUS_ID,PROCESS_ID,INITIATED_BY,INITIATED_SPC,INITIATED_ON FROM TASK_MASTER WHERE"
                         + " PENDING_AT='" + empId + "' AND PROCESS_ID != 3 AND (STATUS_ID != '16') ORDER BY INITIATED_ON DESC)"
                         + " TASK_MASTER)TASK_MASTER"
                         + " LEFT OUTER JOIN G_WORKFLOW_PROCESS ON TASK_MASTER.PROCESS_ID=G_WORKFLOW_PROCESS.PROCESS_ID"
                         + " INNER JOIN G_PROCESS_STATUS ON TASK_MASTER.STATUS_ID = G_PROCESS_STATUS.STATUS_ID"
                         + " LEFT OUTER JOIN G_SPC ON TASK_MASTER.INITIATED_SPC = G_SPC.SPC"
                         + " LEFT OUTER JOIN (SELECT EMP_ID,F_NAME,M_NAME,L_NAME FROM EMP_MAST)EMP_MAST ON EMP_MAST.EMP_ID = "
                         + " TASK_MASTER.INITIATED_BY"
                         + " UNION"
                         + " SELECT auth_remarks_closed,RECIEV_AUTH_TYPE,PROCESS_NAME,TASK_MASTER.PROCESS_ID PROCESS_ID,F_NAME,M_NAME,L_NAME,STATUS_NAME,"
                         + " INITIATED_ON,SPN,INITIATED_BY,TASK_MASTER.TASK_ID,G_PROCESS_STATUS.STATUS_ID,IS_COMPLETED FROM ("
                         + " SELECT * FROM (SELECT TASK_ID,STATUS_ID,PROCESS_ID,INITIATED_BY,INITIATED_SPC,INITIATED_ON FROM TASK_MASTER WHERE"
                         + " PENDING_AT='" + empId + "' AND PROCESS_ID = 3 AND (STATUS_ID != '16') ORDER BY INITIATED_ON DESC)"
                         + " TASK_MASTER)TASK_MASTER"
                         + " LEFT OUTER JOIN G_WORKFLOW_PROCESS ON TASK_MASTER.PROCESS_ID=G_WORKFLOW_PROCESS.PROCESS_ID"
                         + " INNER JOIN G_PROCESS_STATUS ON TASK_MASTER.STATUS_ID = G_PROCESS_STATUS.STATUS_ID"
                         + " LEFT OUTER JOIN G_SPC ON TASK_MASTER.INITIATED_SPC = G_SPC.SPC"
                         + " INNER JOIN (SELECT EMP_ID,F_NAME,M_NAME,L_NAME FROM EMP_MAST)EMP_MAST ON EMP_MAST.EMP_ID = "
                         + " TASK_MASTER.INITIATED_BY"
                         + " LEFT OUTER JOIN PAR_ADVERSE_COMM ON TASK_MASTER.TASK_ID=PAR_ADVERSE_COMM.TASK_ID"
                         + " INNER JOIN PAR_MASTER ON TASK_MASTER.TASK_ID=PAR_MASTER.TASK_ID"
                         + " INNER JOIN (SELECT FY,auth_remarks_closed FROM FINANCIAL_YEAR WHERE AUTH_REMARKS_CLOSED='N')FINANCIAL_YEAR ON"
                         + " PAR_MASTER.FISCAL_YEAR=FINANCIAL_YEAR.FY ORDER BY TASK_ID DESC"; */
                        sql = "SELECT '' auth_remarks_closed,'' RECIEV_AUTH_TYPE,PROCESS_NAME,TASK_MASTER.PROCESS_ID PROCESS_ID,F_NAME,M_NAME,L_NAME,STATUS_NAME,"
                                + " INITIATED_ON,SPN,INITIATED_BY,TASK_MASTER.TASK_ID,G_PROCESS_STATUS.STATUS_ID,IS_COMPLETED FROM ("
                                + " SELECT * FROM (SELECT TASK_ID,STATUS_ID,PROCESS_ID,INITIATED_BY,INITIATED_SPC,INITIATED_ON FROM TASK_MASTER WHERE"
                                + " PENDING_AT='" + empId + "' AND PROCESS_ID != 3 AND (STATUS_ID != '16' and extract('year' from INITIATED_ON)>2022) ORDER BY INITIATED_ON DESC)"
                                + " TASK_MASTER)TASK_MASTER"
                                + " LEFT OUTER JOIN G_WORKFLOW_PROCESS ON TASK_MASTER.PROCESS_ID=G_WORKFLOW_PROCESS.PROCESS_ID"
                                + " INNER JOIN G_PROCESS_STATUS ON TASK_MASTER.STATUS_ID = G_PROCESS_STATUS.STATUS_ID"
                                + " LEFT OUTER JOIN G_SPC ON TASK_MASTER.INITIATED_SPC = G_SPC.SPC"
                                + " INNER JOIN EMP_MAST ON EMP_MAST.EMP_ID = "
                                + " TASK_MASTER.INITIATED_BY "
                                + " UNION"
                                + " SELECT auth_remarks_closed,RECIEV_AUTH_TYPE,PROCESS_NAME,TASK_MASTER.PROCESS_ID PROCESS_ID,F_NAME,M_NAME,L_NAME,STATUS_NAME,"
                                + " INITIATED_ON,SPN,INITIATED_BY,TASK_MASTER.TASK_ID,G_PROCESS_STATUS.STATUS_ID,IS_COMPLETED FROM ("
                                + " SELECT * FROM (SELECT TASK_ID,STATUS_ID,PROCESS_ID,INITIATED_BY,INITIATED_SPC,INITIATED_ON FROM TASK_MASTER WHERE"
                                + " PENDING_AT='" + empId + "' AND PROCESS_ID = 3 AND (STATUS_ID != '16') AND initiated_on::DATE > '2023-08-01' ORDER BY INITIATED_ON DESC)"
                                + " TASK_MASTER)TASK_MASTER"
                                + " LEFT OUTER JOIN G_WORKFLOW_PROCESS ON TASK_MASTER.PROCESS_ID=G_WORKFLOW_PROCESS.PROCESS_ID"
                                + " INNER JOIN G_PROCESS_STATUS ON TASK_MASTER.STATUS_ID = G_PROCESS_STATUS.STATUS_ID"
                                + " INNER JOIN G_SPC ON TASK_MASTER.INITIATED_SPC = G_SPC.SPC"
                                + " INNER JOIN EMP_MAST ON EMP_MAST.EMP_ID = "
                                + " TASK_MASTER.INITIATED_BY"
                                + " LEFT OUTER JOIN PAR_ADVERSE_COMM ON TASK_MASTER.TASK_ID=PAR_ADVERSE_COMM.TASK_ID"
                                + " INNER JOIN PAR_MASTER ON TASK_MASTER.TASK_ID=PAR_MASTER.TASK_ID"
                                + " INNER JOIN (SELECT FY,auth_remarks_closed FROM FINANCIAL_YEAR WHERE AUTH_REMARKS_CLOSED='N')FINANCIAL_YEAR ON"
                                + " PAR_MASTER.FISCAL_YEAR=FINANCIAL_YEAR.FY"
                                + " UNION"
                                + " SELECT auth_remarks_closed,RECIEV_AUTH_TYPE,PROCESS_NAME,TASK_MASTER.PROCESS_ID PROCESS_ID,F_NAME,M_NAME,L_NAME,STATUS_NAME,"
                                + " INITIATED_ON,SPN,INITIATED_BY,TASK_MASTER.TASK_ID,G_PROCESS_STATUS.STATUS_ID,IS_COMPLETED FROM ("
                                + " SELECT * FROM (SELECT TASK_ID,STATUS_ID,PROCESS_ID,INITIATED_BY,INITIATED_SPC,INITIATED_ON FROM TASK_MASTER WHERE"
                                + " PENDING_AT='" + empId + "' AND PROCESS_ID = 26 ORDER BY INITIATED_ON DESC)"
                                + " TASK_MASTER)TASK_MASTER"
                                + " LEFT OUTER JOIN G_WORKFLOW_PROCESS ON TASK_MASTER.PROCESS_ID=G_WORKFLOW_PROCESS.PROCESS_ID"
                                + " INNER JOIN G_PROCESS_STATUS ON TASK_MASTER.STATUS_ID = G_PROCESS_STATUS.STATUS_ID"
                                + " LEFT OUTER JOIN G_SPC ON TASK_MASTER.INITIATED_SPC = G_SPC.SPC"
                                + " LEFT OUTER JOIN EMP_MAST ON EMP_MAST.EMP_ID = "
                                + " TASK_MASTER.INITIATED_BY"
                                + " LEFT OUTER JOIN PAR_ADVERSE_COMM ON TASK_MASTER.TASK_ID=PAR_ADVERSE_COMM.TASK_ID"
                                + " LEFT OUTER JOIN PAR_MASTER ON TASK_MASTER.TASK_ID=PAR_MASTER.TASK_ID"
                                + " LEFT OUTER JOIN (SELECT FY,auth_remarks_closed FROM FINANCIAL_YEAR WHERE AUTH_REMARKS_CLOSED='N')FINANCIAL_YEAR ON"
                                + " PAR_MASTER.FISCAL_YEAR=FINANCIAL_YEAR.FY ORDER BY TASK_ID DESC";

                    }
                    sql = sql + " LIMIT " + maxlimit + " OFFSET " + minlimit;
                    rs = st.executeQuery(sql);
                    int i = 0;
                    while (rs.next()) {

                        i++;
                        lldb = new TaskListHelperBean();
                        lldb.setCount(i);
                        lldb.setDateOfInitiation(CommonFunctions.getFormattedOutputDate(rs.getDate("INITIATED_ON")));
                        lldb.setDateOfInitiationAsString(CommonFunctions.getFormattedOutputDate1(rs.getDate("INITIATED_ON")));
                        lldb.setProcessname(rs.getString("PROCESS_NAME"));
                        lldb.setApplicant(getApplicantName(con, rs.getString("INITIATED_BY"), rs.getString("PROCESS_ID"), rs.getInt("TASK_ID")));
                        lldb.setTaskId(rs.getInt("TASK_ID"));
                        lldb.setStatusId(rs.getInt("STATUS_ID"));
                        if (lldb.getStatusId() >= 0) {
                            if (lldb.getStatusId() == 6) {
                                lldb.setStatus("PENDING AS REPORTING AUTHORITY");
                            } else if (lldb.getStatusId() == 7) {
                                lldb.setStatus("PENDING AS REVIEWING AUTHORITY");
                            } else if (lldb.getStatusId() == 8) {
                                lldb.setStatus("PENDING AS ACCEPTING AUTHORITY");
                            } else if (lldb.getStatusId() == 21) {
                                lldb.setApplicant(getPARApplicant(con, rs.getString("INITIATED_BY"), "21"));
                                lldb.setStatus("PAR IS ADVERSED BY CUSTODIAN");
                            } else {
                                lldb.setStatus(rs.getString("STATUS_NAME"));
                            }
                        }
                        lldb.setAppEmpCode(rs.getString("INITIATED_BY"));
                        lldb.setProcessId(rs.getInt("PROCESS_ID"));
                        lldb.setIstaskcompleted(rs.getString("IS_COMPLETED"));
                        /*if(rs.getString("STATUS_ID") != null && rs.getString("STATUS_ID").equals("8")){
                         lldb.setGrading(rs.getString("OVERALLGRADING"));    
                         }*/
                        lldb.setAuth("");
                        a1.add(lldb);
                        Collections.sort(a1);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return a1;
    }

    @Override
    public int getTaskListTotalCnt(String empId, String parstatus) {

        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;

        PreparedStatement pst = null;

        int totalCnt = 0;
        try {
            con = this.repodataSource.getConnection();
            stmt = con.createStatement();

            String sql = "";
            if (parstatus != null && !parstatus.equals("")) {
                if (parstatus.equals("9")) {
                    sql = "select count(*) cnt FROM (SELECT SUBMITTED_ON,ACTIONURL,PAR_MASTER.PARID,PROCESS_NAME,TASK_MASTER.PROCESS_ID PROCESS_ID,F_NAME,M_NAME,L_NAME,STATUS_NAME,INITIATED_ON,"
                            + "   SPN,INITIATED_BY,TASK_MASTER.TASK_ID,TASK_MASTER.STATUS_ID,PAR_REPORTING_TRAN.IS_COMPLETED,'REP' PUSER FROM"
                            + "   (SELECT PAR_ID,REPORTING_EMP_ID,REPORTING_CUR_SPC,SUBMITTED_ON,IS_COMPLETED FROM PAR_REPORTING_TRAN"
                            + "   WHERE REPORTING_EMP_ID=? AND (IS_COMPLETED='Y' OR IS_COMPLETED='F'))PAR_REPORTING_TRAN"
                            + "   INNER JOIN PAR_MASTER ON PAR_REPORTING_TRAN.PAR_ID=PAR_MASTER.PARID"
                            + "   INNER JOIN TASK_MASTER ON PAR_MASTER.TASK_ID=TASK_MASTER.TASK_ID"
                            + "   LEFT OUTER JOIN G_WORKFLOW_PROCESS ON  TASK_MASTER.PROCESS_ID=G_WORKFLOW_PROCESS.PROCESS_ID"
                            + "   INNER JOIN G_PROCESS_STATUS ON TASK_MASTER.STATUS_ID = G_PROCESS_STATUS.STATUS_ID"
                            + "   INNER JOIN G_SPC ON TASK_MASTER.INITIATED_SPC = G_SPC.SPC"
                            + "   INNER JOIN EMP_MAST ON EMP_MAST.EMP_ID = TASK_MASTER.INITIATED_BY"
                            + "   UNION"
                            + "   SELECT SUBMITTED_ON,ACTIONURL,PAR_MASTER.PARID,PROCESS_NAME,TASK_MASTER.PROCESS_ID PROCESS_ID,F_NAME,M_NAME,L_NAME,STATUS_NAME,INITIATED_ON,"
                            + "   SPN,INITIATED_BY,TASK_MASTER.TASK_ID,TASK_MASTER.STATUS_ID,PAR_REVIEWING_TRAN.IS_COMPLETED,'REV' PUSER FROM"
                            + "   (SELECT PAR_ID,REVIEWING_EMP_ID,REVIEWING_CUR_SPC,SUBMITTED_ON,IS_COMPLETED FROM PAR_REVIEWING_TRAN"
                            + "   WHERE REVIEWING_EMP_ID=? AND (IS_COMPLETED='Y' OR IS_COMPLETED='F'))PAR_REVIEWING_TRAN"
                            + "   INNER JOIN PAR_MASTER ON PAR_REVIEWING_TRAN.PAR_ID=PAR_MASTER.PARID"
                            + "   INNER JOIN TASK_MASTER ON PAR_MASTER.TASK_ID=TASK_MASTER.TASK_ID"
                            + "   LEFT OUTER JOIN G_WORKFLOW_PROCESS ON TASK_MASTER.PROCESS_ID=G_WORKFLOW_PROCESS.PROCESS_ID"
                            + "   INNER JOIN G_PROCESS_STATUS ON TASK_MASTER.STATUS_ID = G_PROCESS_STATUS.STATUS_ID"
                            + "   INNER JOIN G_SPC ON TASK_MASTER.INITIATED_SPC = G_SPC.SPC"
                            + "   INNER JOIN EMP_MAST ON EMP_MAST.EMP_ID = TASK_MASTER.INITIATED_BY"
                            + "   UNION"
                            + "   SELECT SUBMITTED_ON,ACTIONURL,PAR_MASTER.PARID,PROCESS_NAME,TASK_MASTER.PROCESS_ID PROCESS_ID,F_NAME,M_NAME,L_NAME,STATUS_NAME,INITIATED_ON,"
                            + "   SPN,INITIATED_BY,TASK_MASTER.TASK_ID,TASK_MASTER.STATUS_ID,PAR_ACCEPTING_TRAN.IS_COMPLETED,'ACP' PUSER FROM"
                            + "   (SELECT PAR_ID,ACCEPTING_EMP_ID,ACCEPTING_CUR_SPC,SUBMITTED_ON,IS_COMPLETED FROM PAR_ACCEPTING_TRAN"
                            + "   WHERE ACCEPTING_EMP_ID=? AND (IS_COMPLETED='Y' OR IS_COMPLETED='F'))PAR_ACCEPTING_TRAN"
                            + "   INNER JOIN PAR_MASTER ON PAR_ACCEPTING_TRAN.PAR_ID=PAR_MASTER.PARID"
                            + "   INNER JOIN TASK_MASTER ON PAR_MASTER.TASK_ID=TASK_MASTER.TASK_ID"
                            + "   LEFT OUTER JOIN G_WORKFLOW_PROCESS ON TASK_MASTER.PROCESS_ID=G_WORKFLOW_PROCESS.PROCESS_ID"
                            + "   INNER JOIN G_PROCESS_STATUS ON TASK_MASTER.STATUS_ID = G_PROCESS_STATUS.STATUS_ID"
                            + "   INNER JOIN G_SPC ON TASK_MASTER.INITIATED_SPC = G_SPC.SPC"
                            + "   INNER JOIN EMP_MAST ON EMP_MAST.EMP_ID = TASK_MASTER.INITIATED_BY)temp";
                    pst = con.prepareStatement(sql);
                    pst.setString(1, empId);
                    pst.setString(2, empId);
                    pst.setString(3, empId);
                    rs = pst.executeQuery();
                    if (rs.next()) {
                        totalCnt = rs.getInt("CNT");
                    }
                } else {
                    sql = "SELECT COUNT(*) CNT FROM (SELECT PROCESS_NAME,TASK_MASTER.PROCESS_ID PROCESS_ID,F_NAME,M_NAME,L_NAME,STATUS_NAME,INITIATED_ON,SPN,INITIATED_BY,TASK_MASTER.TASK_ID,TASK_MASTER.STATUS_ID,IS_COMPLETED FROM(SELECT TASK_ID,STATUS_ID,PROCESS_ID,INITIATED_BY,INITIATED_SPC,INITIATED_ON FROM TASK_MASTER WHERE PENDING_AT='" + empId + "' AND STATUS_ID = '" + parstatus + "' AND initiated_on::DATE > '2024-01-01')"
                            + " TASK_MASTER"
                            + " LEFT OUTER JOIN G_WORKFLOW_PROCESS ON TASK_MASTER.PROCESS_ID=G_WORKFLOW_PROCESS.PROCESS_ID"
                            + " INNER JOIN G_PROCESS_STATUS ON TASK_MASTER.STATUS_ID = G_PROCESS_STATUS.STATUS_ID"
                            + " INNER JOIN G_SPC ON TASK_MASTER.INITIATED_SPC = G_SPC.SPC"
                            + " INNER JOIN EMP_MAST ON EMP_MAST.EMP_ID = TASK_MASTER.INITIATED_BY"
                            + " INNER JOIN PAR_MASTER ON TASK_MASTER.TASK_ID=PAR_MASTER.TASK_ID"
                            + " INNER JOIN (SELECT FY,auth_remarks_closed FROM FINANCIAL_YEAR WHERE AUTH_REMARKS_CLOSED='N')FINANCIAL_YEAR ON"
                            + " PAR_MASTER.FISCAL_YEAR=FINANCIAL_YEAR.FY)TEMP";
                    /*sql = "SELECT COUNT(*) CNT FROM (SELECT PROCESS_NAME,TASK_MASTER.PROCESS_ID PROCESS_ID,F_NAME,M_NAME,L_NAME,STATUS_NAME,INITIATED_ON,SPN,INITIATED_BY,TASK_MASTER.TASK_ID,TASK_MASTER.STATUS_ID,IS_COMPLETED FROM(SELECT TASK_ID,STATUS_ID,PROCESS_ID,INITIATED_BY,INITIATED_SPC,INITIATED_ON FROM TASK_MASTER WHERE PENDING_AT='" + empId + "' AND STATUS_ID = '" + parstatus + "')"
                            + " TASK_MASTER"
                            + " LEFT OUTER JOIN G_WORKFLOW_PROCESS ON TASK_MASTER.PROCESS_ID=G_WORKFLOW_PROCESS.PROCESS_ID"
                            + " INNER JOIN G_PROCESS_STATUS ON TASK_MASTER.STATUS_ID = G_PROCESS_STATUS.STATUS_ID"
                            + " INNER JOIN G_SPC ON TASK_MASTER.INITIATED_SPC = G_SPC.SPC"
                            + " INNER JOIN EMP_MAST ON EMP_MAST.EMP_ID = TASK_MASTER.INITIATED_BY"
                            + " INNER JOIN PAR_MASTER ON TASK_MASTER.TASK_ID=PAR_MASTER.TASK_ID"
                            + " INNER JOIN (SELECT FY,auth_remarks_closed FROM FINANCIAL_YEAR WHERE AUTH_REMARKS_CLOSED='N')FINANCIAL_YEAR ON"
                            + " PAR_MASTER.FISCAL_YEAR=FINANCIAL_YEAR.FY)TEMP";*/
                    rs = stmt.executeQuery(sql);
                    if (rs.next()) {
                        totalCnt = rs.getInt("CNT");
                    }
                }
            } else {
                sql = "SELECT COUNT(*) CNT FROM (SELECT PROCESS_NAME,TASK_MASTER.PROCESS_ID PROCESS_ID,F_NAME,M_NAME,L_NAME,STATUS_NAME,INITIATED_ON,SPN,INITIATED_BY,TASK_ID,TASK_MASTER.STATUS_ID,IS_COMPLETED FROM(SELECT TASK_ID,STATUS_ID,PROCESS_ID,INITIATED_BY,INITIATED_SPC,INITIATED_ON FROM TASK_MASTER WHERE PENDING_AT='" + empId + "' AND PROCESS_ID != '3' AND STATUS_ID != '16' and extract('year' from INITIATED_ON)>2022)"
                        + " TASK_MASTER"
                        + " LEFT OUTER JOIN G_WORKFLOW_PROCESS ON  TASK_MASTER.PROCESS_ID=G_WORKFLOW_PROCESS.PROCESS_ID"
                        + " INNER JOIN G_PROCESS_STATUS ON TASK_MASTER.STATUS_ID = G_PROCESS_STATUS.STATUS_ID"
                        + " INNER JOIN G_SPC ON TASK_MASTER.INITIATED_SPC = G_SPC.SPC"
                        + " INNER JOIN EMP_MAST ON EMP_MAST.EMP_ID = TASK_MASTER.INITIATED_BY ORDER BY INITIATED_ON, TASK_ID DESC)TEMP";
                rs = stmt.executeQuery(sql);
                if (rs.next()) {
                    totalCnt = rs.getInt("CNT");
                }

                sql = "SELECT COUNT(*) CNT FROM (SELECT TASK_MASTER.* FROM (SELECT TASK_ID,STATUS_ID,PROCESS_ID,INITIATED_BY,INITIATED_SPC,INITIATED_ON FROM TASK_MASTER WHERE PENDING_AT='" + empId + "' AND PROCESS_ID = '3' AND STATUS_ID != '16' AND initiated_on::DATE > '2024-01-01') "
                        + " TASK_MASTER"
                        + " LEFT OUTER JOIN G_WORKFLOW_PROCESS ON TASK_MASTER.PROCESS_ID=G_WORKFLOW_PROCESS.PROCESS_ID"
                        + " INNER JOIN G_PROCESS_STATUS ON TASK_MASTER.STATUS_ID = G_PROCESS_STATUS.STATUS_ID"
                        + " INNER JOIN G_SPC ON TASK_MASTER.INITIATED_SPC = G_SPC.SPC"
                        + " INNER JOIN EMP_MAST ON EMP_MAST.EMP_ID = TASK_MASTER.INITIATED_BY"
                        + " INNER JOIN PAR_MASTER ON TASK_MASTER.TASK_ID=PAR_MASTER.TASK_ID"
                        + " INNER JOIN (SELECT FY,auth_remarks_closed FROM FINANCIAL_YEAR WHERE AUTH_REMARKS_CLOSED='N')FINANCIAL_YEAR ON"
                        + " PAR_MASTER.FISCAL_YEAR=FINANCIAL_YEAR.FY)TEMP";
                rs = stmt.executeQuery(sql);
                if (rs.next()) {
                    totalCnt = totalCnt + rs.getInt("CNT");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(rs, stmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return totalCnt;
    }

    public String getPARApplicant(Connection con, String leaveApplicant, String prid) throws Exception {
        ResultSet rs = null;
        SelectOption so = null;
        Statement st = null;
        String authority = null;
        String sql = "";

        try {
            st = con.createStatement();
            if (prid.equals("")) {
                sql = "SELECT EMPNAME,POST FROM(SELECT GPC,EMPNAME FROM(SELECT CUR_SPC,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMPNAME,EMP_ID FROM EMP_MAST WHERE EMP_ID='" + leaveApplicant + "')EMPMAST LEFT OUTER JOIN PAR_MASTER ON EMPMAST.EMP_ID="
                        + " PAR_MASTER.EMP_ID INNER JOIN G_SPC ON G_SPC.SPC=PAR_MASTER.SPC)TAB LEFT OUTER JOIN"
                        + " G_POST ON G_POST.POST_CODE=TAB.GPC";
            } else if (prid.equals("21")) {
                sql = "SELECT EMPNAME,POST FROM(SELECT GPC,EMPNAME FROM(SELECT CUR_SPC,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMPNAME,EMP_ID FROM EMP_MAST WHERE EMP_ID='" + leaveApplicant + "')EMPMAST LEFT OUTER JOIN PAR_ADVERSE_COMM ON EMPMAST.EMP_ID="
                        + " PAR_ADVERSE_COMM.SENDER_ID INNER JOIN G_SPC ON G_SPC.SPC=PAR_ADVERSE_COMM.SENDER_SPC)TAB LEFT OUTER JOIN"
                        + " G_POST ON G_POST.POST_CODE=TAB.GPC";
            }
            rs = st.executeQuery(sql);
            if (rs.next()) {
                if (rs.getString("POST") != null && !rs.getString("POST").equals("")) {
                    authority = rs.getString("EMPNAME") + "," + rs.getString("POST");
                } else {
                    authority = rs.getString("EMPNAME");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
        }
        return authority;
    }

    @Override
    public String getDispatcherString(int taskid) {
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        String actionurl = "";
        try {
            con = dataSource.getConnection();
            stmt = con.createStatement();
            String sql = "SELECT TASK_ID,PROCESS_NAME,ACTIONURL,APPLY_TO FROM (SELECT * FROM TASK_MASTER WHERE TASK_ID='" + taskid + "')TASK_MASTER INNER JOIN G_WORKFLOW_PROCESS ON G_WORKFLOW_PROCESS.PROCESS_ID = TASK_MASTER.PROCESS_ID";
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                actionurl = rs.getString("ACTIONURL");
                System.out.println("the val of url is=====813===" + actionurl);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, stmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return actionurl;
    }

    @Override
    public TaskListHelperBean getTaskDetails(int taskid) {
        TaskListHelperBean taskBean = new TaskListHelperBean();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT status_id,process_id, pending_at, pending_spc, initiated_by,initiated_spc, IS_COMPLETED, ref_id,get_empname_from_type(initiated_by ,'G') as initiated_by_Name,"
                    + "getspn(initiated_spc) as initiated_by_desg FROM TASK_MASTER WHERE TASK_ID=?");
            pstmt.setInt(1, taskid);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                taskBean.setStatusId(rs.getInt("status_id"));
                taskBean.setProcessId(rs.getInt("process_id"));
                taskBean.setPendingAt(rs.getString("pending_at"));
                taskBean.setPendingSpc(rs.getString("pending_spc"));
                taskBean.setAppEmpCode(rs.getString("initiated_by"));
                taskBean.setAppSpc(rs.getString("initiated_spc"));
                taskBean.setIstaskcompleted(rs.getString("IS_COMPLETED"));
                taskBean.setReferenceId(rs.getInt("ref_id"));
                taskBean.setInitiatedByName(rs.getString("initiated_by_Name"));
                taskBean.setInitiatedByPost(rs.getString("initiated_by_desg"));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return taskBean;
    }

    @Override
    public List getProcessList() {
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        List processlist = new ArrayList();
        SelectOption so = null;
        try {
            con = dataSource.getConnection();

            so = new SelectOption();
            so.setLabel("ALL");
            so.setValue("");
            processlist.add(so);

            stmt = con.createStatement();
            String sql = "SELECT PROCESS_ID,PROCESS_NAME FROM G_WORKFLOW_PROCESS WHERE ACTIVE='Y' ORDER BY PROCESS_NAME";
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                so = new SelectOption();
                so.setLabel(rs.getString("PROCESS_NAME") + " ");
                so.setValue(rs.getString("PROCESS_ID"));
                processlist.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, stmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return processlist;
    }

    @Override
    public List getMyTaskList(String empid) {
        Connection con = null;
        ResultSet result = null;
        PreparedStatement statement = null;
        ArrayList tasklist = new ArrayList();

        try {
            con = this.dataSource.getConnection();
            String SQL = "SELECT PROCESS_NAME,TASK_MASTER.PROCESS_ID PROCESS_ID,F_NAME,M_NAME,L_NAME,STATUS_NAME,INITIATED_ON,SPN,INITIATED_BY,TASK_ID,G_PROCESS_STATUS.STATUS_ID,IS_COMPLETED FROM (SELECT * FROM (SELECT TASK_ID,STATUS_ID,PROCESS_ID,INITIATED_BY,INITIATED_SPC,INITIATED_ON,ROW_NUMBER() OVER (ORDER BY TASK_ID) Row_Num FROM TASK_MASTER WHERE PENDING_AT=? AND STATUS_ID != '16')"
                    + " TASK_MASTER)TASK_MASTER"
                    + " LEFT OUTER JOIN G_WORKFLOW_PROCESS ON  TASK_MASTER.PROCESS_ID=G_WORKFLOW_PROCESS.PROCESS_ID"
                    + " INNER JOIN G_PROCESS_STATUS ON TASK_MASTER.STATUS_ID = G_PROCESS_STATUS.STATUS_ID"
                    + " INNER JOIN G_SPC ON TASK_MASTER.INITIATED_SPC = G_SPC.SPC"
                    + " INNER JOIN EMP_MAST ON EMP_MAST.EMP_ID = TASK_MASTER.INITIATED_BY ORDER BY TASK_ID";
            System.out.println("getMyTaskList Query: " + SQL);
            statement = con.prepareStatement(SQL);
            statement.setString(1, empid);
            result = statement.executeQuery();
            while (result.next()) {
                TaskListHelperBean tlhb = new TaskListHelperBean();
                tlhb.setDateOfInitiation(result.getDate("INITIATED_ON"));
                tlhb.setProcessname(result.getString("PROCESS_NAME"));
                tlhb.setTaskId(result.getInt("TASK_ID"));
                tlhb.setStatusId(result.getInt("STATUS_ID"));
                tlhb.setApplicant(getApplicantName(con, result.getString("INITIATED_BY"), result.getString("PROCESS_ID"), result.getInt("TASK_ID")));

                if (tlhb.getStatusId() != 0) {

                    if (tlhb.getStatusId() == 6) {
                        tlhb.setStatus("PENDING AS REPORTING AUTHORITY");
                    } else if (tlhb.getStatusId() == 7) {
                        tlhb.setStatus("PENDING AS REVIEWING AUTHORITY");
                    } else if (tlhb.getStatusId() == 8) {
                        tlhb.setStatus("PENDING AS ACCEPTING AUTHORITY");
                    } else if (tlhb.getStatusId() == 1) {
                        tlhb.setStatus("APPROVED");
                    } else if (tlhb.getStatusId() == 2) {
                        tlhb.setStatus("FORWARD");
                    } else if (tlhb.getStatusId() == 3) {
                        tlhb.setStatus("PENDING");
                    } else if (tlhb.getStatusId() == 4) {
                        tlhb.setStatus("SANCTIONED");
                    } else if (tlhb.getStatusId() == 5) {
                        tlhb.setStatus("ALLOWED");
                    } else if (tlhb.getStatusId() == 41) {
                        tlhb.setStatus("JOINED");
                    } else if (tlhb.getStatusId() == 42) {
                        tlhb.setStatus("DECLINE");
                    }
                }
                tlhb.setAppEmpCode(result.getString("INITIATED_BY"));
                tlhb.setProcessId(result.getInt("PROCESS_ID"));
                tlhb.setIstaskcompleted(result.getString("IS_COMPLETED"));
                tasklist.add(tlhb);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(result, statement);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return tasklist;

    }

    @Override
    public String getLoggedInEmpNameForCompletedTask(String empid) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        String empname = "";
        try {
            con = this.dataSource.getConnection();

            String sql = "SELECT ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME, L_NAME], ' ') EMPNAME FROM EMP_MAST WHERE EMP_ID=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            rs = pst.executeQuery();
            if (rs.next()) {
                empname = rs.getString("EMPNAME");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return empname;
    }

    @Override
    public void closeTask(int taskid) {
        Connection con = null;
        PreparedStatement pst = null;

        String startTime = "";
        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
        startTime = dateFormat.format(cal.getTime());
        try {
            con = this.dataSource.getConnection();
            pst = con.prepareStatement("UPDATE TASK_MASTER SET is_completed = 'Y',completed_on=? where task_id=?");
            pst.setTimestamp(1, new Timestamp(dateFormat.parse(startTime).getTime()));
            pst.setInt(2, taskid);
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    public int getTaskListTotalPARCnt(String empId, int parstatus) {
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        PreparedStatement pst = null;

        int totalCntPAR = 0;
        try {
            con = this.repodataSource.getConnection();
            stmt = con.createStatement();

            String sql = "";
            if (parstatus == 0) {
                sql = "SELECT COUNT(*) CNT FROM (SELECT TASK_MASTER.* FROM (SELECT TASK_ID,STATUS_ID,PROCESS_ID,INITIATED_BY,INITIATED_SPC,INITIATED_ON FROM TASK_MASTER WHERE PENDING_AT='" + empId + "' AND PROCESS_ID = '3' AND initiated_on::DATE > '2023-08-01')"
                        + " TASK_MASTER"
                        + " LEFT OUTER JOIN G_WORKFLOW_PROCESS ON TASK_MASTER.PROCESS_ID=G_WORKFLOW_PROCESS.PROCESS_ID"
                        + " INNER JOIN G_PROCESS_STATUS ON TASK_MASTER.STATUS_ID = G_PROCESS_STATUS.STATUS_ID"
                        + " INNER JOIN G_SPC ON TASK_MASTER.INITIATED_SPC = G_SPC.SPC"
                        + " INNER JOIN EMP_MAST ON EMP_MAST.EMP_ID = TASK_MASTER.INITIATED_BY"
                        + " INNER JOIN PAR_MASTER ON TASK_MASTER.TASK_ID=PAR_MASTER.TASK_ID"
                        + " INNER JOIN (SELECT FY,auth_remarks_closed FROM FINANCIAL_YEAR WHERE AUTH_REMARKS_CLOSED='N')FINANCIAL_YEAR ON"
                        + " PAR_MASTER.FISCAL_YEAR=FINANCIAL_YEAR.FY)TEMP";
                pst = con.prepareStatement(sql);
                rs = pst.executeQuery();
                if (rs.next()) {
                    totalCntPAR = rs.getInt("CNT");
                }
            } else {
                sql = "SELECT COUNT(*) CNT FROM (SELECT TASK_MASTER.* FROM (SELECT TASK_ID,STATUS_ID,PROCESS_ID,INITIATED_BY,INITIATED_SPC,INITIATED_ON FROM TASK_MASTER WHERE PENDING_AT='" + empId + "' AND PROCESS_ID = '3' AND STATUS_ID =? AND initiated_on::DATE > '2023-08-01')"
                        + " TASK_MASTER"
                        + " LEFT OUTER JOIN G_WORKFLOW_PROCESS ON TASK_MASTER.PROCESS_ID=G_WORKFLOW_PROCESS.PROCESS_ID"
                        + " INNER JOIN G_PROCESS_STATUS ON TASK_MASTER.STATUS_ID = G_PROCESS_STATUS.STATUS_ID"
                        + " INNER JOIN G_SPC ON TASK_MASTER.INITIATED_SPC = G_SPC.SPC"
                        + " INNER JOIN EMP_MAST ON EMP_MAST.EMP_ID = TASK_MASTER.INITIATED_BY"
                        + " INNER JOIN PAR_MASTER ON TASK_MASTER.TASK_ID=PAR_MASTER.TASK_ID"
                        + " INNER JOIN (SELECT FY,auth_remarks_closed FROM FINANCIAL_YEAR WHERE AUTH_REMARKS_CLOSED='N')FINANCIAL_YEAR ON"
                        + " PAR_MASTER.FISCAL_YEAR=FINANCIAL_YEAR.FY)TEMP";

                pst = con.prepareStatement(sql);
                pst.setInt(1, parstatus);
                rs = pst.executeQuery();
                if (rs.next()) {
                    totalCntPAR = rs.getInt("CNT");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(rs, stmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return totalCntPAR;
    }

}
