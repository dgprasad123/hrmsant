package hrms.dao.leaveapply;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import hrms.SelectOption;
import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.common.SMSServices;
import hrms.common.SMSThread;
import hrms.model.employee.Employee;
import hrms.model.leave.AttachmentHelperBean;
import hrms.model.leave.Leave;
import hrms.model.leave.LeaveEntrytakenBean;
import hrms.model.leave.LeaveFlowDtls;
import hrms.model.leave.LeaveListDtlsBean;
import hrms.model.leave.LeaveOswass;
import hrms.model.leave.LeaveSancBean;
import hrms.model.leave.LeaveWsBean;
import hrms.model.leaveupdate.LeaveBalance;
import hrms.model.updateleave.UpdateLeave;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
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
import java.util.HashMap;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.json.JSONObject;

public class LeaveApplyDAOImpl implements LeaveApplyDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    protected MaxLeaveIdDAOImpl maxleaveidDao;

    public MaxLeaveIdDAOImpl getMaxleaveidDao() {
        return maxleaveidDao;
    }

    public void setMaxleaveidDao(MaxLeaveIdDAOImpl maxleaveidDao) {
        this.maxleaveidDao = maxleaveidDao;
    }

    @Override
    public List getLeaveApplyList(String empId) {
        List a1 = new ArrayList();
        PreparedStatement pst = null;
        ResultSet rs = null;
        Statement st = null;
        LeaveListDtlsBean lldb = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            st = con.createStatement();

            rs = st.executeQuery("  SELECT  (DATE_PART('year', APPROVE_TDATE::date) - DATE_PART('year', APPROVE_FDATE::date)) * 12 +   (DATE_PART('month', APPROVE_TDATE::date) - DATE_PART('month', APPROVE_FDATE::date))    AS DIFFDATE, "
                    + " G_PROCESS_STATUS.STATUS_ID, HW_EMP_LEAVE.TASK_ID TASK_ID,LEAVEID,EMP_ID,FDATE,TDATE,APPROVE_FDATE,APPROVE_TDATE,HW_EMP_LEAVE.TOL_ID,SUBMITEDTO,SUBMIT_AUTH,STATUS_NAME, "
                    + " INITIATED_ON,IS_SEEN,IS_EXTENDED,gleave.TOL FROM HW_EMP_LEAVE  "
                    + "INNER JOIN TASK_MASTER ON TASK_MASTER.TASK_ID=HW_EMP_LEAVE.TASK_ID  "
                    + "INNER JOIN G_PROCESS_STATUS ON G_PROCESS_STATUS.STATUS_ID=TASK_MASTER.STATUS_ID inner join (select distinct tol_id,tol from g_leave)gleave on gleave.tol_id=HW_EMP_LEAVE.tol_id "
                    + "WHERE EMP_ID='" + empId + "' AND TASK_MASTER.PROCESS_ID=1 ORDER BY INITIATED_ON DESC");

            while (rs.next()) {
                lldb = new LeaveListDtlsBean();
                lldb.setDateOfInitiation(CommonFunctions.getFormattedOutputDate3(rs.getDate("INITIATED_ON")));
                if (rs.getString("DIFFDATE") != null) {
                    int days = Integer.parseInt(rs.getString("DIFFDATE"));
                    lldb.setApproveDaysDiff("" + (days + 1));
                } else {
                    lldb.setApproveDaysDiff("");
                }
                lldb.setTolId(rs.getString("TOL_ID"));
                lldb.setLeaveType(rs.getString("TOL"));
                lldb.setFromDate(CommonFunctions.getFormattedOutputDate3(rs.getDate("FDATE")));
                lldb.setToDate(CommonFunctions.getFormattedOutputDate3(rs.getDate("TDATE")));
                if (rs.getDate("APPROVE_FDATE") != null) {
                    lldb.setTxtApproveFrom(CommonFunctions.getFormattedOutputDate3(rs.getDate("APPROVE_FDATE")));
                } else {
                    lldb.setTxtApproveFrom("");
                }
                if (rs.getDate("APPROVE_TDATE") != null) {
                    lldb.setTxtApproveTo(CommonFunctions.getFormattedOutputDate3(rs.getDate("APPROVE_TDATE")));
                } else {
                    lldb.setTxtApproveTo("");
                }

                lldb.setSubmittedTo(getNameWithPost(rs.getString("SUBMITEDTO"), rs.getString("SUBMIT_AUTH"), rs.getString("STATUS_ID")));
                lldb.setLeaveId(rs.getString("LEAVEID"));
                lldb.setTaskId(rs.getString("TASK_ID"));
                lldb.setStatus(rs.getString("STATUS_NAME"));
                lldb.setStatusId(rs.getString("STATUS_ID"));
                /////////////////////////////////////
                if (lldb.getStatusId().equalsIgnoreCase("5")) {
                    lldb.setEnableJoingRpt("Y");
                    lldb.setLeaveExtensionReq("Y");
                    if (rs.getString("tol_id").equals("CL")) {
                        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
                        Date curdate = formatter.parse(formatter.format(new Date()));
                        Date frmdate = formatter.parse(lldb.getFromDate());

                        if (frmdate.compareTo(curdate) > 0) {
                            lldb.setCancelFlag("Y");
                        }
                    }
                } else {
                    lldb.setEnableJoingRpt("N");
                    lldb.setLeaveExtensionReq("N");
                }

                lldb.setIsSeen(rs.getString("IS_SEEN"));
                lldb.setHidAuthEmpId(rs.getString("SUBMITEDTO"));
                lldb.setIsExtended(rs.getString("IS_EXTENDED"));
                if (lldb.getIsExtended() != null && lldb.getIsExtended().equalsIgnoreCase("Y")) {
                    lldb.setEnableJoingRpt("N");
                    lldb.setLeaveExtensionReq("N");
                }
                a1.add(lldb);
            }

            /*pst = con.prepareStatement("SELECT  (DATE_PART('year', APPROVE_TDATE::date) - DATE_PART('year', APPROVE_FDATE::date)) * 12 + (DATE_PART('month', APPROVE_TDATE::date) - DATE_PART('month', APPROVE_FDATE::date)) AS DIFFDATE,"
             + " LEAVEID,EMP_ID,FDATE,TDATE,APPROVE_FDATE,APPROVE_TDATE,EMP_LEAVE.TOL_ID,SUBMITEDTO,gleave.TOL FROM EMP_LEAVE"
             + " inner join (select distinct tol_id,tol from g_leave)gleave on gleave.tol_id=EMP_LEAVE.tol_id"
             + " WHERE EMP_ID=?");
             pst.setString(1, empId);
             rs = pst.executeQuery();
             while (rs.next()) {
             lldb = new LeaveListDtlsBean();
             lldb.setDateOfInitiation("");
             if (rs.getString("DIFFDATE") != null) {
             int days = Integer.parseInt(rs.getString("DIFFDATE"));
             lldb.setApproveDaysDiff("" + (days + 1));
             } else {
             lldb.setApproveDaysDiff("");
             }
             lldb.setLeaveType(rs.getString("TOL"));
             lldb.setFromDate(CommonFunctions.getFormattedOutputDate3(rs.getDate("FDATE")));
             lldb.setToDate(CommonFunctions.getFormattedOutputDate3(rs.getDate("TDATE")));
             if (rs.getDate("APPROVE_FDATE") != null) {
             lldb.setTxtApproveFrom(CommonFunctions.getFormattedOutputDate3(rs.getDate("APPROVE_FDATE")));
             } else {
             lldb.setTxtApproveFrom("");
             }
             if (rs.getDate("APPROVE_TDATE") != null) {
             lldb.setTxtApproveTo(CommonFunctions.getFormattedOutputDate3(rs.getDate("APPROVE_TDATE")));
             } else {
             lldb.setTxtApproveTo("");
             }
             lldb.setSubmittedTo("");
             lldb.setLeaveId(rs.getString("LEAVEID"));
             lldb.setTaskId("");
             lldb.setStatus("OSWAS");
             lldb.setStatusId("");
             if (lldb.getStatusId().equalsIgnoreCase("5")) {
             lldb.setEnableJoingRpt("Y");
             lldb.setLeaveExtensionReq("Y");
             } else {
             lldb.setEnableJoingRpt("N");
             lldb.setLeaveExtensionReq("N");
             }

             lldb.setIsSeen("");
             lldb.setHidAuthEmpId(rs.getString("SUBMITEDTO"));
             lldb.setIsExtended("");
             if (lldb.getIsExtended() != null && lldb.getIsExtended().equalsIgnoreCase("Y")) {
             lldb.setEnableJoingRpt("N");
             lldb.setLeaveExtensionReq("N");
             }
             a1.add(lldb);
             }*/
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(st);
            DataBaseFunctions.closeSqlObjects(con);

        }

        return a1;
    }

    public String getLeaveType(String tolId) {
        ResultSet rs = null;
        SelectOption so = null;
        Statement st = null;
        String leaveType = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            st = con.createStatement();
            rs = st.executeQuery("SELECT TOL FROM G_LEAVE WHERE TOL_ID='" + tolId + "'");
            if (rs.next()) {
                leaveType = rs.getString("TOL");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(st);
            DataBaseFunctions.closeSqlObjects(con);

        }
        return leaveType;
    }

    public String getApplicantNameWithPost(String authEmpId, String authSpc, String leaveStatus) {
        ResultSet rs = null;
        SelectOption so = null;
        String authority = null;
        Connection con = null;
        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;
        PreparedStatement pst = null;
        Statement st = null;
        try {

            con = dataSource.getConnection();
            if (leaveStatus != null && !leaveStatus.equals("") && (leaveStatus.equals("2") || leaveStatus.equals("3"))) {
                /* Pending Case */

                if (authSpc != null && !authSpc.equals("")) {
                    ps2 = con.prepareStatement("SELECT CUR_SPC, EMP_ID FROM EMP_MAST WHERE CUR_SPC=? ");
                    ps2.setString(1, authSpc);
                    rs = ps2.executeQuery();
                    if (rs.next()) {
                        authEmpId = rs.getString("EMP_ID");
                    }
                } else {
                    ps2 = con.prepareStatement("SELECT CUR_SPC, EMP_ID FROM EMP_MAST WHERE EMP_ID=? ");
                    ps2.setString(1, authEmpId);
                    rs = ps2.executeQuery();
                    if (rs.next()) {
                        authEmpId = rs.getString("EMP_ID");
                    }
                }

                DataBaseFunctions.closeSqlObjects(rs, ps2);

            }
            /* if Allowed or Approved Case auth emp id will be remain intact as parameter pass by function */
            if (authSpc != null && !authSpc.equals("")) {

                ps1 = con.prepareStatement("select concat_ws(' ', INITIALS ::text, F_NAME::text, M_NAME::text,L_NAME::text) AS EMPNAME, "
                        + "  getspn('" + authSpc + "') POST from emp_mast where cur_spc=? ");

                ps1.setString(1, authSpc);

                rs = ps1.executeQuery();
                if (rs.next()) {
                    if (rs.getString("POST") != null && !rs.getString("POST").equals("")) {
                        authority = rs.getString("EMPNAME") + " , " + rs.getString("POST");
                    } else {
                        authority = rs.getString("EMPNAME");
                    }
                }
            } else {
                ps1 = con.prepareStatement("select concat_ws(' ', INITIALS ::text, F_NAME::text, M_NAME::text,L_NAME::text) AS EMPNAME,POST_NOMENCLATURE "
                        + "  from emp_mast where emp_id=? ");

                ps1.setString(1, authEmpId);

                rs = ps1.executeQuery();
                if (rs.next()) {
                    if (rs.getString("POST_NOMENCLATURE") != null && !rs.getString("POST_NOMENCLATURE").equals("")) {
                        authority = rs.getString("EMPNAME") + " , " + rs.getString("POST_NOMENCLATURE");
                    } else {
                        authority = rs.getString("EMPNAME");
                    }
                }
            }
            DataBaseFunctions.closeSqlObjects(rs, ps1);
            /*Authority Having Additional Charge*/
            if (authority == null || authority.equals("")) {

                pst = con.prepareStatement("SELECT emp_join.SPC,G_SPC.SPN spn,emp_join.EMP_ID,concat_ws(' ', INITIALS ::text, F_NAME::text, M_NAME::text,L_NAME::text) AS EMPNAME,POST FROM EMP_JOIN "
                        + "left outer join emp_relieve on emp_join.join_id=emp_relieve.join_id "
                        + "INNER JOIN EMP_MAST ON emp_join.EMP_ID = EMP_MAST.EMP_ID and EMP_MAST.emp_id=? "
                        + "LEFT OUTER JOIN G_SPC ON G_SPC.SPC=emp_join.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_POST.POST_CODE=G_SPC.GPC "
                        + "WHERE  if_ad_charge='Y' and emp_join.SPC=? and emp_relieve.join_id is null and emp_join.spc is not null");
                pst.setString(1, authEmpId);
                pst.setString(2, authSpc);

                rs = pst.executeQuery();
                if (rs.next()) {
                    if (rs.getString("spn") != null && !rs.getString("spn").equals("")) {
                        authority = rs.getString("EMPNAME") + " , " + rs.getString("spn");

                    } else {
                        authority = rs.getString("EMPNAME");
                    }
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps1, ps2, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return authority;
    }

    @Override
    public String getNameWithPost(String authEmpId, String authSpc, String leaveStatus) {
        ResultSet rs = null;
        SelectOption so = null;
        String authority = null;
        Connection con = null;
        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;
        PreparedStatement pst = null;
        Statement st = null;
        try {
            con = dataSource.getConnection();
            if (leaveStatus != null && !leaveStatus.equals("") && (leaveStatus.equals("2") || leaveStatus.equals("3"))) {
                /* Pending Case */

                if (authSpc != null && !authSpc.equals("")) {
                    ps2 = con.prepareStatement("SELECT CUR_SPC, EMP_ID FROM EMP_MAST WHERE CUR_SPC=? ");
                    ps2.setString(1, authSpc);
                    rs = ps2.executeQuery();
                    if (rs.next()) {
                        authEmpId = rs.getString("EMP_ID");
                    }
                } else {
                    ps2 = con.prepareStatement("SELECT CUR_SPC, EMP_ID FROM EMP_MAST WHERE EMP_ID=? ");
                    ps2.setString(1, authEmpId);
                    rs = ps2.executeQuery();
                    if (rs.next()) {
                        authEmpId = rs.getString("EMP_ID");
                    }
                }

                DataBaseFunctions.closeSqlObjects(rs, ps2);

            }
            /* if Allowed or Approved Case auth emp id will be remain intact as parameter pass by function */
            if (authSpc != null && !authSpc.equals("")) {
                ps2 = con.prepareStatement("SELECT EMP_ID FROM EMP_JOIN WHERE EMP_ID=? ");
                ps2.setString(1, authEmpId);
                rs = ps2.executeQuery();
                if (rs.next()) {
                    pst = con.prepareStatement("SELECT emp_join.SPC,G_SPC.SPN spn,emp_join.EMP_ID,concat_ws(' ', INITIALS ::text, F_NAME::text, M_NAME::text,L_NAME::text) AS EMPNAME,POST FROM EMP_JOIN "
                            + "left outer join emp_relieve on emp_join.join_id=emp_relieve.join_id "
                            + "INNER JOIN EMP_MAST ON emp_join.EMP_ID = EMP_MAST.EMP_ID and EMP_MAST.emp_id=? "
                            + "LEFT OUTER JOIN G_SPC ON G_SPC.SPC=emp_join.SPC "
                            + "LEFT OUTER JOIN G_POST ON G_POST.POST_CODE=G_SPC.GPC "
                            + "WHERE  emp_join.SPC=? and emp_relieve.join_id is null and emp_join.spc is not null");

                    pst.setString(1, authEmpId);
                    pst.setString(2, authSpc);

                    rs = pst.executeQuery();
                    if (rs.next()) {
                        if (rs.getString("spn") != null && !rs.getString("spn").equals("")) {
                            authority = rs.getString("EMPNAME") + " , " + rs.getString("spn");
                        } else {
                            authority = rs.getString("EMPNAME");
                        }
                    } else {
                        ps1 = con.prepareStatement("select concat_ws(' ', INITIALS ::text, F_NAME::text, M_NAME::text,L_NAME::text) AS EMPNAME, "
                                + "  getspn('" + authSpc + "') POST from emp_mast where cur_spc=? ");
                        ps1.setString(1, authSpc);

                        rs = ps1.executeQuery();
                        if (rs.next()) {
                            if (rs.getString("POST") != null && !rs.getString("POST").equals("")) {
                                authority = rs.getString("EMPNAME") + " , " + rs.getString("POST");
                            } else {
                                authority = rs.getString("EMPNAME");
                            }
                        }
                    }
                } else {
                    ps1 = con.prepareStatement("select concat_ws(' ', INITIALS ::text, F_NAME::text, M_NAME::text,L_NAME::text) AS EMPNAME, "
                            + "  getspn('" + authSpc + "') POST from emp_mast where cur_spc=? ");
                    ps1.setString(1, authSpc);
                    rs = ps1.executeQuery();
                    if (rs.next()) {
                        if (rs.getString("POST") != null && !rs.getString("POST").equals("")) {
                            authority = rs.getString("EMPNAME") + " , " + rs.getString("POST");
                        } else {
                            authority = rs.getString("EMPNAME");
                        }
                    }
                }
            } else {

                ps1 = con.prepareStatement("select concat_ws(' ', INITIALS ::text, F_NAME::text, M_NAME::text,L_NAME::text) AS EMPNAME,POST_NOMENCLATURE "
                        + "  from emp_mast where emp_id=? ");

                ps1.setString(1, authEmpId);

                rs = ps1.executeQuery();
                if (rs.next()) {
                    if (rs.getString("POST_NOMENCLATURE") != null && !rs.getString("POST_NOMENCLATURE").equals("")) {
                        authority = rs.getString("EMPNAME") + " , " + rs.getString("POST_NOMENCLATURE");
                    } else {
                        authority = rs.getString("EMPNAME");
                    }
                }
            }
            DataBaseFunctions.closeSqlObjects(rs, ps1);
            /*Authority Having Additional Charge*/
            if (authority == null || authority.equals("")) {

                pst = con.prepareStatement("SELECT emp_join.SPC,G_SPC.SPN spn,emp_join.EMP_ID,concat_ws(' ', INITIALS ::text, F_NAME::text, M_NAME::text,L_NAME::text) AS EMPNAME,POST FROM EMP_JOIN "
                        + "left outer join emp_relieve on emp_join.join_id=emp_relieve.join_id "
                        + "INNER JOIN EMP_MAST ON emp_join.EMP_ID = EMP_MAST.EMP_ID and EMP_MAST.emp_id=? "
                        + "LEFT OUTER JOIN G_SPC ON G_SPC.SPC=emp_join.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_POST.POST_CODE=G_SPC.GPC "
                        + "WHERE  if_ad_charge='Y' and emp_join.SPC=? and emp_relieve.join_id is null and emp_join.spc is not null");
                pst.setString(1, authEmpId);
                pst.setString(2, authSpc);

                rs = pst.executeQuery();
                if (rs.next()) {
                    if (rs.getString("spn") != null && !rs.getString("spn").equals("")) {
                        authority = rs.getString("EMPNAME") + " , " + rs.getString("spn");
                    } else {
                        authority = rs.getString("EMPNAME");
                    }
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(ps1, ps2, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return authority;
    }

    public String getNameWithPostForLeaveList(String authEmpId, String authSpc, String leaveStatus, String initiatedOn) {
        ResultSet rs = null;
        SelectOption so = null;
        String authority = null;
        Connection con = null;
        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;
        PreparedStatement pst = null;
        Statement st = null;
        try {

            con = dataSource.getConnection();
            if (leaveStatus != null && !leaveStatus.equals("") && (leaveStatus.equals("2") || leaveStatus.equals("3"))) {
                /* Pending Case */

                if (authSpc != null && !authSpc.equals("")) {
                    ps2 = con.prepareStatement("SELECT CUR_SPC, EMP_ID FROM EMP_MAST WHERE CUR_SPC=? ");
                    ps2.setString(1, authSpc);
                    rs = ps2.executeQuery();
                    if (rs.next()) {
                        authEmpId = rs.getString("EMP_ID");
                    }
                } else {
                    ps2 = con.prepareStatement("SELECT CUR_SPC, EMP_ID FROM EMP_MAST WHERE EMP_ID=? ");
                    ps2.setString(1, authEmpId);
                    rs = ps2.executeQuery();
                    if (rs.next()) {
                        authEmpId = rs.getString("EMP_ID");
                    }
                }

                DataBaseFunctions.closeSqlObjects(rs, ps2);

            }
            /* if Allowed or Approved Case auth emp id will be remain intact as parameter pass by function */
            if (authSpc != null && !authSpc.equals("")) {

                ps1 = con.prepareStatement("select concat_ws(' ', INITIALS ::text, F_NAME::text, M_NAME::text,L_NAME::text) AS EMPNAME, "
                        + "  getspn('" + authSpc + "') POST from emp_mast where cur_spc=? ");

                ps1.setString(1, authSpc);

                rs = ps1.executeQuery();
                if (rs.next()) {
                    if (rs.getString("POST") != null && !rs.getString("POST").equals("")) {
                        authority = rs.getString("EMPNAME") + " , " + rs.getString("POST");

                    } else {
                        authority = rs.getString("EMPNAME");
                    }
                }
            } else {
                ps1 = con.prepareStatement("select concat_ws(' ', INITIALS ::text, F_NAME::text, M_NAME::text,L_NAME::text) AS EMPNAME,POST_NOMENCLATURE "
                        + "  from emp_mast where emp_id=? ");

                ps1.setString(1, authEmpId);

                rs = ps1.executeQuery();
                if (rs.next()) {
                    if (rs.getString("POST_NOMENCLATURE") != null && !rs.getString("POST_NOMENCLATURE").equals("")) {
                        authority = rs.getString("EMPNAME") + " , " + rs.getString("POST_NOMENCLATURE");
                    } else {
                        authority = rs.getString("EMPNAME");
                    }
                }
            }
            DataBaseFunctions.closeSqlObjects(rs, ps1);
            /*Authority Having Additional Charge*/
            if (authority == null || authority.equals("")) {

                pst = con.prepareStatement("SELECT emp_join.SPC,G_SPC.SPN spn,emp_join.EMP_ID,concat_ws(' ', INITIALS ::text, F_NAME::text, M_NAME::text,L_NAME::text) AS EMPNAME,POST FROM EMP_JOIN "
                        + "left outer join emp_relieve on emp_join.join_id=emp_relieve.join_id "
                        + "INNER JOIN EMP_MAST ON emp_join.EMP_ID = EMP_MAST.EMP_ID and EMP_MAST.emp_id=? "
                        + "LEFT OUTER JOIN G_SPC ON G_SPC.SPC=emp_join.SPC "
                        + "LEFT OUTER JOIN G_POST ON G_POST.POST_CODE=G_SPC.GPC "
                        + "WHERE  if_ad_charge='Y' and emp_join.SPC=? and emp_relieve.join_id is null and emp_join.spc is not null");
                pst.setString(1, authEmpId);
                pst.setString(2, authSpc);

                rs = pst.executeQuery();
                if (rs.next()) {
                    if (rs.getString("spn") != null && !rs.getString("spn").equals("")) {
                        authority = rs.getString("EMPNAME") + " , " + rs.getString("spn");

                    } else {
                        authority = rs.getString("EMPNAME");
                    }
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(ps1, ps2, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return authority;
    }

    public String getPost(String leaveAuth) {
        ResultSet rs = null;
        SelectOption so = null;
        Statement st = null;
        String authority = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            st = con.createStatement();
            rs = st.executeQuery("SELECT POST FROM(SELECT  GPC FROM G_SPC WHERE SPC='" + leaveAuth + "')TAB LEFT OUTER JOIN G_POST ON G_POST.POST_CODE=TAB.GPC");
            if (rs.next()) {
                if (rs.getString("POST") != null && !rs.getString("POST").equals("")) {
                    authority = rs.getString("POST");
                } else {
                    authority = "";
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return authority;
    }

    @Override
    public boolean saveLeave(Leave lfb) {
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        PreparedStatement pst = null;
        PreparedStatement pstmt = null;
        PreparedStatement pstmtt = null;
        String startTime = "";
        String leaveId = null;
        String getncode = null;
        Calendar cal = Calendar.getInstance();
        Timestamp timestamp = new Timestamp(cal.getTimeInMillis());
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
        boolean isLeaveApplied = true;
        try {
            con = dataSource.getConnection();
            st = con.createStatement();
            //int mcode = CommonFunctions.getMaxCodeInteger("TASK_MASTER", "TASK_ID", con);
            pst = con.prepareStatement("INSERT INTO TASK_MASTER(PROCESS_ID, INITIATED_BY, INITIATED_ON, STATUS_ID, PENDING_AT,APPLY_TO,INITIATED_SPC,PENDING_SPC,APPLY_TO_SPC) Values (?,?,?,?,?,?,?,?,?)", pst.RETURN_GENERATED_KEYS);
            pst.setInt(1, 1);
            pst.setString(2, lfb.getHidempId());
            pst.setTimestamp(3, timestamp);
            pst.setInt(4, 3);
//            String str = lfb.getTxtSancAuthority();
//            String[] temp;
//            String delimiter = "-";
//            temp = str.split(delimiter);
//            pst.setString(6, temp[0]);
//            pst.setString(7, temp[0]);
            pst.setString(5, lfb.getHidAuthEmpId());
            pst.setString(6, lfb.getHidAuthEmpId());

            pst.setString(7, lfb.getHidSpcCode());
//            pst.setString(9, temp[1]);
//            pst.setString(10, temp[1]);
            pst.setString(8, lfb.getHidSpcAuthCode());
            pst.setString(9, lfb.getHidSpcAuthCode());
            pst.executeUpdate();
            rs = pst.getGeneratedKeys();
            rs.next();
            int mcode = rs.getInt("TASK_ID");

            //leaveId = CommonFunctions.getMaxCode("HW_EMP_LEAVE", "LEAVEID", con);
            // getncode = CommonFunctions.getMaxNotIdCode(con);
            pstmt = con.prepareStatement("INSERT INTO HW_EMP_LEAVE(NOT_TYPE, EMP_ID, LSOT_ID, TOL_ID,FDATE,TDATE,PREFIX_DATE,PREFIX_TO,SUFFIX_FROM,SUFFIX_DATE,IF_MEDICAL,IF_COMMUTED,TASK_ID,SUBMIT_AUTH,SUBMITEDTO,IF_LONGTERM,APPLICANT_NOTE,ADDRESS,PHONENO,IF_HEADQUARTER,IS_EXTENDED,NO_OF_CHILD,ISSUING_AUTH_EMPID,ISSUING_AUTH_SPC,part_of_day) Values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            //pstmt.setString(1, leaveId);
            //  pstmt.setString(2, getncode);
            pstmt.setString(1, "LEAVE");
            pstmt.setString(2, lfb.getHidempId());
            pstmt.setString(3, "01");
            pstmt.setString(4, lfb.getSltleaveType());
            Date fdate = formatter.parse(lfb.getTxtperiodFrom());
            java.sql.Date fdateSql = new java.sql.Date(fdate.getTime());
            pstmt.setDate(5, fdateSql);
            Date tdate = formatter.parse(lfb.getTxtperiodTo());
            java.sql.Date todateSql = new java.sql.Date(tdate.getTime());
            pstmt.setDate(6, todateSql);
            if (lfb.getTxtprefixFrom() != null && !lfb.getTxtprefixFrom().equals("")) {
                Date prefixFrom = formatter.parse(lfb.getTxtprefixFrom());
                java.sql.Date prefixFromSql = new java.sql.Date(prefixFrom.getTime());
                pstmt.setDate(7, prefixFromSql);
            } else {
                pstmt.setDate(7, null);
            }
            if (lfb.getTxtprefixTo() != null && !lfb.getTxtprefixTo().equals("")) {
                Date prefixTo = formatter.parse(lfb.getTxtprefixTo());
                java.sql.Date prefixToSql = new java.sql.Date(prefixTo.getTime());
                pstmt.setDate(8, prefixToSql);
            } else {
                pstmt.setDate(8, null);
            }
            if (lfb.getTxtsuffixFrom() != null && !lfb.getTxtsuffixFrom().equals("")) {
                Date sufixFrom = formatter.parse(lfb.getTxtsuffixFrom());
                java.sql.Date sufixFromSql = new java.sql.Date(sufixFrom.getTime());
                pstmt.setDate(9, sufixFromSql);
            } else {
                pstmt.setDate(9, null);
            }
            if (lfb.getTxtsuffixTo() != null && !lfb.getTxtsuffixTo().equals("")) {
                Date sufixTo = formatter.parse(lfb.getTxtsuffixTo());
                java.sql.Date sufixToSql = new java.sql.Date(sufixTo.getTime());
                pstmt.setDate(10, sufixToSql);
            } else {
                pstmt.setDate(10, null);
            }
            pstmt.setString(11, lfb.getIfmedical());
            pstmt.setString(12, lfb.getIfcommuted());
            pstmt.setInt(13, mcode);
            // pstmt.setString(15, temp[1]);
            //  pstmt.setString(16, temp[0]);
            pstmt.setString(14, lfb.getHidSpcAuthCode());
            pstmt.setString(15, lfb.getHidAuthEmpId());
            pstmt.setString(16, lfb.getChkLongTerm());
            pstmt.setString(17, lfb.getTxtnote());
            pstmt.setString(18, lfb.getTxtconaddress());
            // int n=Integer.parseInt(lfb.getTxtphoneNo());

            if (lfb.getTxtphoneNo() != null && !lfb.getTxtphoneNo().equals("")) {
                pstmt.setDouble(19, Double.parseDouble(lfb.getTxtphoneNo()));
            } else {
                pstmt.setDouble(19, 0);
            }
            pstmt.setString(20, lfb.getHqperrad());
            pstmt.setString(21, " ");
            if (lfb.getSltChild() != null && !lfb.getSltChild().equals("")) {
                pstmt.setInt(22, Integer.parseInt(lfb.getSltChild()));
            } else {
                pstmt.setInt(22, 0);
            }
            pstmt.setString(23, lfb.getHidIssuingAuthEmpId());
            pstmt.setString(24, lfb.getHidIssuingSpcAuthCode());
            pstmt.setString(25, lfb.getSltPartofday());
            pstmt.executeUpdate();
            if (lfb.getLeaveId() != null && !lfb.getLeaveId().equals("")) {
                ps = con.prepareStatement("UPDATE HW_EMP_LEAVE SET IS_EXTENDED='Y' WHERE LEAVEID='" + lfb.getLeaveId() + "'");
                ps.executeUpdate();
            }
            if (lfb.getAttachmentid() != null) {
                for (int i = 0; i < lfb.getAttachmentid().length; i++) {
                    pstmtt = con.prepareStatement("UPDATE HW_ATTACHMENTS set TASK_ID=?,ATTACH_FLAG=? WHERE ATT_ID=" + lfb.getAttachmentid()[i]);
                    pstmtt.setInt(1, mcode);
                    pstmtt.setString(2, "M");
                    pstmtt.executeUpdate();
                }
            }
//            pst = con.prepareStatement("SELECT MOBILE FROM EMP_MAST WHERE EMP_ID=?");
//            pst.setString(1, lfb.getHidAuthEmpId());
//            rs = pst.executeQuery();
//            if (rs.next()) {
//                if (rs.getString("MOBILE") != null && !rs.getString("MOBILE").equals("")) {
//                    SMSThread smsthread = new SMSThread(lfb.getHidAuthEmpId(), rs.getString("MOBILE"), "L");
//                    Thread t1 = new Thread(smsthread);
//                    t1.start();
//                }
//            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(ps, pst, pstmtt);
            DataBaseFunctions.closeSqlObjects(con);

        }
        return isLeaveApplied;
    }

    @Override
    public String getAppOffice(String initiatedBy, String appSpc) {
        ResultSet rs = null;
        SelectOption so = null;
        Statement st = null;
        String authorityOff = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            st = con.createStatement();
            if (appSpc != null && !appSpc.equals("")) {
                rs = st.executeQuery("SELECT OFF_EN FROM(SELECT CUR_OFF_CODE FROM EMP_MAST WHERE CUR_SPC='" + appSpc + "') "
                        + "EMPMAST INNER JOIN G_OFFICE ON G_OFFICE.OFF_CODE=EMPMAST.CUR_OFF_CODE");
            } else {
                rs = st.executeQuery("SELECT OFF_EN FROM(SELECT CUR_OFF_CODE FROM EMP_MAST WHERE emp_id='" + initiatedBy + "') "
                        + "EMPMAST INNER JOIN G_OFFICE ON G_OFFICE.OFF_CODE=EMPMAST.CUR_OFF_CODE");
            }
            if (rs.next()) {
                authorityOff = rs.getString("OFF_EN");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return authorityOff;
    }

    @Override
    public String getAuthOffice(String authSpc) {
        ResultSet rs = null;
        SelectOption so = null;
        Statement st = null;
        String authorityOff = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            st = con.createStatement();
            rs = st.executeQuery("SELECT OFF_EN FROM(SELECT CUR_OFF_CODE FROM EMP_MAST WHERE CUR_SPC='" + authSpc + "') "
                    + "EMPMAST INNER JOIN G_OFFICE ON G_OFFICE.OFF_CODE=EMPMAST.CUR_OFF_CODE");

            if (rs.next()) {
                authorityOff = rs.getString("OFF_EN");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return authorityOff;
    }

    @Override
    public Leave getLeaveData(String taskId, String loginEmpId, String loggedinSpc) {
        Leave leaveForm = null;
        Connection con = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        Statement st = null;
        Statement st1 = null;
        String empStatusStr = null;
        String initiatedBy = null;
        String initiatedSpc = null;
        try {
            con = dataSource.getConnection();
            st = con.createStatement();
            rs = st.executeQuery("SELECT INITIATED_BY,INITIATED_SPC,INITIATED_ON,LEAVEID,FDATE,TDATE,TDATE + interval '1' day as EXTFDATE,APPROVE_FDATE,APPROVE_TDATE,DATE_PART('day', APPROVE_TDATE::timestamp - APPROVE_FDATE::timestamp)+1 daydiff, "
                    + "SUFFIX_FROM,SUFFIX_DATE,PREFIX_DATE,PREFIX_TO,HWEMP.TOL_ID,EMP_ID,SUBMIT_AUTH,submitedto, HWEMP.TASK_ID,APPLICANT_NOTE,ADDRESS,PHONENO,ORD_NO,ORD_DATE,PENDING_SPC ,PENDING_AT,IF_HEADQUARTER,STATUS_ID, "
                    + "JOINING_NOTE,JOIN_TIME_FROM,issuing_auth_spc,issuing_auth_empid,part_of_day,approved_spc,approved_empid,task_status_id,gleave.TOL FROM  "
                    + "(SELECT LEAVEID,FDATE,TDATE,APPROVE_FDATE,APPROVE_TDATE,SUFFIX_FROM,SUFFIX_DATE,PREFIX_DATE,PREFIX_TO,TOL_ID,EMP_ID, "
                    + "submitedto,SUBMIT_AUTH,TASK_ID,APPLICANT_NOTE,ADDRESS,PHONENO,ORD_NO,ORD_DATE,IF_HEADQUARTER,JOINING_NOTE,JOIN_TIME_FROM,issuing_auth_spc,issuing_auth_empid,part_of_day,approved_empid,approved_spc FROM HW_EMP_LEAVE WHERE TASK_ID='" + taskId + "')HWEMP   "
                    + "INNER JOIN TASK_MASTER ON HWEMP.TASK_ID=TASK_MASTER.TASK_ID left outer join workflow_log on workflow_log.task_id=HWEMP.task_id inner join (select distinct tol_id,tol from g_leave)gleave on gleave.tol_id=HWEMP.tol_id");
            if (rs.next()) {
                leaveForm = new Leave();
                initiatedBy = rs.getString("INITIATED_BY");
                initiatedSpc = rs.getString("INITIATED_SPC");
                leaveForm.setHidempId(initiatedBy);
                leaveForm.setHidSpcCode(initiatedSpc);
                leaveForm.setLeaveId(rs.getString("LEAVEID"));
                leaveForm.setStatusId(rs.getString("STATUS_ID"));
                leaveForm.setHidTaskId(rs.getString("TASK_ID"));
                leaveForm.setInitiatedOn(CommonFunctions.getFormattedOutputDate3(rs.getDate("INITIATED_ON")));
                leaveForm.setWorkFlowStatusId(rs.getString("task_status_id"));
                leaveForm.setTollid(rs.getString("TOL_ID"));
                leaveForm.setTxtperiodFrom(CommonFunctions.getFormattedOutputDate3(rs.getDate("FDATE")));
                leaveForm.setTxtperiodTo(CommonFunctions.getFormattedOutputDate3(rs.getDate("TDATE")));
                leaveForm.setExtendedFromDate(CommonFunctions.getFormattedOutputDate3(rs.getDate("EXTFDATE")));
                if (rs.getDate("APPROVE_FDATE") != null && rs.getDate("APPROVE_TDATE") != null) {
                    leaveForm.setTxtApproveFrom(CommonFunctions.getFormattedOutputDate3(rs.getDate("APPROVE_FDATE")));
                    leaveForm.setTxtApproveTo(CommonFunctions.getFormattedOutputDate3(rs.getDate("APPROVE_TDATE")));
                } else {
                    leaveForm.setTxtApproveFrom(CommonFunctions.getFormattedOutputDate3(rs.getDate("FDATE")));
                    leaveForm.setTxtApproveTo(CommonFunctions.getFormattedOutputDate3(rs.getDate("TDATE")));
                }
                leaveForm.setTxtsuffixFrom(CommonFunctions.getFormattedOutputDate3(rs.getDate("SUFFIX_FROM")));
                leaveForm.setTxtsuffixTo(CommonFunctions.getFormattedOutputDate3(rs.getDate("SUFFIX_DATE")));
                leaveForm.setTxtprefixFrom(CommonFunctions.getFormattedOutputDate3(rs.getDate("PREFIX_DATE")));
                leaveForm.setTxtprefixTo(CommonFunctions.getFormattedOutputDate3(rs.getDate("PREFIX_TO")));
                if (rs.getString("APPLICANT_NOTE") != null && !rs.getString("APPLICANT_NOTE").equals("")) {
                    leaveForm.setTxtnote(rs.getString("APPLICANT_NOTE"));
                }
                if (rs.getString("JOINING_NOTE") != null && !rs.getString("JOINING_NOTE").equals("")) {
                    leaveForm.setJoiningNote(rs.getString("JOINING_NOTE").toLowerCase());
                }
                leaveForm.setJoiningDate(CommonFunctions.getFormattedOutputDate3(rs.getDate("JOIN_TIME_FROM")));
                leaveForm.setSubmittedTo(getNameWithPost(rs.getString("submitedto"), rs.getString("SUBMIT_AUTH"), rs.getString("STATUS_ID")));
                leaveForm.setAuthPost(getPost(rs.getString("issuing_auth_spc")));
                leaveForm.setIssuingAuthName(getNameWithPost(rs.getString("issuing_auth_empid"), rs.getString("issuing_auth_spc"), rs.getString("STATUS_ID")));
                leaveForm.setPendingPostWthName(getNameWithPost(rs.getString("PENDING_AT"), rs.getString("PENDING_SPC"), rs.getString("STATUS_ID")));
                if (initiatedBy.equals(loginEmpId)) {
                    leaveForm.setApplicantName(getApplicantNameWithPost(rs.getString("INITIATED_BY"), rs.getString("INITIATED_SPC"), rs.getString("STATUS_ID")));
                } else {
                    leaveForm.setApplicantName(getApplicantNameWithPost(rs.getString("INITIATED_BY"), rs.getString("INITIATED_SPC"), rs.getString("STATUS_ID")));
                }

                leaveForm.setOffname(getAuthOffice(rs.getString("INITIATED_SPC")));
                leaveForm.setAppOffname(getAppOffice(initiatedBy, rs.getString("INITIATED_SPC")));
                leaveForm.setSltleaveType(rs.getString("TOL_ID"));
                leaveForm.setLeaveType(rs.getString("TOL"));
                leaveForm.setTxtconaddress(rs.getString("ADDRESS"));
                if (rs.getDouble("PHONENO") > 0) {
                    leaveForm.setTxtphoneNo(rs.getString("PHONENO"));
                } else {
                    leaveForm.setTxtphoneNo("");
                }
                leaveForm.setTxtOrdNo(rs.getString("ORD_NO"));
                leaveForm.setTxtOrdDate(CommonFunctions.getFormattedOutputDate3(rs.getDate("ORD_DATE")));
                if (rs.getString("IF_HEADQUARTER") != null && rs.getString("IF_HEADQUARTER").equals("Y")) {
                    leaveForm.setHqperrad("YES");
                } else {
                    leaveForm.setHqperrad("NO");
                }
                leaveForm.setHidAuthEmpId(rs.getString("PENDING_AT"));
                leaveForm.setHidSpcAuthCode(rs.getString("PENDING_SPC"));

                leaveForm.setHidIssuingAuthEmpId(rs.getString("approved_empid"));
                leaveForm.setHidIssuingSpcAuthCode(rs.getString("approved_spc"));

                if (rs.getString("TOL_ID").equals("HDL")) {
                    leaveForm.setLeaveBalance("0.5");
                    if (rs.getString("part_of_day").equals("FN")) {
                        leaveForm.setSltPartofday(" (FORE NOON) ");
                    } else if (rs.getString("part_of_day").equals("AN")) {
                        leaveForm.setSltPartofday(" (AFTER NOON) ");
                    }
                } else {
                    leaveForm.setLeaveBalance(rs.getString("daydiff"));
                }
                if (rs.getString("approved_spc") != null && !rs.getString("approved_spc").equals("")) {
                    leaveForm.setApprovedPost(getPost(rs.getString("approved_spc")));
                } else {
                    leaveForm.setApprovedPost(getPost(rs.getString("SUBMIT_AUTH")));
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return leaveForm;
    }

    @Override
    public ArrayList getFileName(String taskId, String attFlag
    ) {
        ResultSet rs = null;
        Connection con = null;
        Statement st = null;
        ArrayList attFileList = new ArrayList();
        AttachmentHelperBean ahb = null;
        try {
            con = dataSource.getConnection();
            st = con.createStatement();

            rs = st.executeQuery("SELECT * FROM HW_ATTACHMENTS WHERE ATTACH_FLAG='" + attFlag + "' AND TASK_ID='" + taskId + "'");
            while (rs.next()) {
                ahb = new AttachmentHelperBean();
                ahb.setAttFileName(rs.getString("ORIGINAL_FILENAME"));
                ahb.setAttId(rs.getString("ATT_ID"));
                attFileList.add(ahb);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return attFileList;
    }

    @Override
    public String getAuthorityEmpCode(String taskId
    ) {
        ResultSet rs = null;
        Connection con = null;
        Statement st = null;
        String authorityEmpCode = null;
        try {
            con = dataSource.getConnection();
            st = con.createStatement();
            rs = st.executeQuery("SELECT * FROM TASK_MASTER WHERE TASK_ID='" + taskId + "'");
            if (rs.next()) {
                authorityEmpCode = rs.getString("PENDING_AT");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return authorityEmpCode;
    }

    @Override
    public String getStatusId(String taskId
    ) {
        ResultSet rs = null;
        Connection con = null;
        Statement st = null;
        String statusId = null;
        try {
            con = dataSource.getConnection();
            st = con.createStatement();
            rs = st.executeQuery("SELECT STATUS_ID FROM TASK_MASTER WHERE TASK_ID='" + taskId + "'");
            if (rs.next()) {
                statusId = rs.getString("STATUS_ID");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return statusId;
    }

    @Override
    public void updateApproveDateAfterJoin(Leave leave) {
        Connection con = null;
        PreparedStatement pstt = null;
        PreparedStatement pstmt = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();
            st = con.createStatement();
            pstt = con.prepareStatement("UPDATE HW_EMP_LEAVE SET APPROVE_FDATE=?,APPROVE_TDATE=? WHERE TASK_ID=?");
            pstt.setString(1, leave.getTxtApproveFrom());
            pstt.setString(2, leave.getTxtApproveTo());
            pstt.setInt(3, Integer.parseInt(leave.getHidTaskId()));
            pstt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstt, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void updateApproveDate(Leave lfb) {
        PreparedStatement pstt = null;
        PreparedStatement pstmt = null;
        PreparedStatement pst = null;
        Statement st = null;
        ResultSet rs = null;
        String startTime = "";
        int mcode = 0;
        Calendar cal = Calendar.getInstance();
        Timestamp timestamp = new Timestamp(cal.getTimeInMillis());
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
        Connection con = null;
        try {
            con = dataSource.getConnection();
            st = con.createStatement();
            pstt = con.prepareStatement("UPDATE HW_EMP_LEAVE SET APPROVE_FDATE=?,APPROVE_TDATE=? WHERE TASK_ID=?");
            Date fappdate = formatter.parse(lfb.getTxtApproveFrom());
            java.sql.Date fappdateSql = new java.sql.Date(fappdate.getTime());
            pstt.setDate(1, fappdateSql);
            Date apptodate = formatter.parse(lfb.getTxtApproveTo());
            java.sql.Date fapptodateSql = new java.sql.Date(apptodate.getTime());
            pstt.setDate(2, fapptodateSql);
            pstt.setInt(3, Integer.parseInt(lfb.getHidTaskId()));
            pstt.executeUpdate();

            ////////////////////////////////////////////Modified on 27-03-2023 By Shantanu to send SMS alert//////////////////////////////////////////////////
            int taskId = Integer.parseInt(lfb.getHidTaskId());
            String sanctioningAuth = "";
            String establishmentAuth = "";

            //Get the authority details
            String sql_leave = "select * from hw_emp_leave where task_id=" + taskId;

            pst = con.prepareStatement(sql_leave);
            rs = pst.executeQuery();
            if (rs.next()) {
                //sanctioningAuth = rs.getString("submitedto");
                establishmentAuth = rs.getString("issuing_auth_empid");
            }
            //Send SMS alert to Establishment Section Authority (added by Shantanu on 27-03-2023)
            //1. Get the mobile no of the Establishment Section Authority
            /* String mobile = null;
             String sqlMobile = "SELECT mobile FROM emp_mast where emp_id = '"+establishmentAuth+"'";
             System.out.println("Establishment sqlMobile: "+sqlMobile);
             pst = con.prepareStatement(sqlMobile);
             rs = pst.executeQuery();
             if (rs.next()) {
             mobile = rs.getString("mobile");
             }
             SMS Template Details
             Template Id:1407167903976278520
             Template Name: LA_action_taken
             Your {#var#} has been {#var#}. HRMS Odisha.
            
             String msg = "Your Leave Cancel Request has been Approved. HRMS Odisha.";
             System.out.println("Establishment Message: "+msg);
             SMSServices smhttp = new SMSServices(mobile, msg, "1407167903976278520");*/

            //Send SMS alert to Applicant
            String applicantId = lfb.getEmpId();
            String applicantMobile = null;
            String sqlMobileApplicant = "SELECT mobile FROM emp_mast where emp_id = '" + applicantId + "'";
            //System.out.println("sqlMobileApplicant: " + sqlMobileApplicant);
            pst = con.prepareStatement(sqlMobileApplicant);
            rs = pst.executeQuery();
            if (rs.next()) {
                applicantMobile = rs.getString("mobile");
            }
            String msg = null;
            msg = "Your Leave application has been Approved. HRMS Odisha.";
            // System.out.println(" Applicant message:" + msg);
            SMSServices smhttpApp = new SMSServices(applicantMobile, msg, "1407167903976278520");

            /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//            if (lfb.getSltActionType().equals("1")) {
//                mcode = CommonFunctions.getMaxCodeInteger("WORKFLOW_LOG", "LOG_ID", con);
//                pstmt = con.prepareStatement("INSERT INTO WORKFLOW_LOG(LOG_ID,TASK_ID, TASK_ACTION_DATE, ACTION_TAKEN_BY, SPC_ONTIME, TASK_STATUS_ID, NOTE,FORWARD_TO,FORWARDED_SPC) Values (?,?,?,?,?,?,?,?,?)");
//                pstmt.setInt(1, mcode);
//                pstmt.setInt(2, Integer.parseInt(lfb.getHidTaskId()));
//                pstmt.setTimestamp(3, timestamp);
//                pstmt.setString(4, lfb.getHidempId());
//                pstmt.setString(5, lfb.getHidSpcCode());
//                pstmt.setInt(6, Integer.parseInt(lfb.getSltActionType()));
//                pstmt.setString(7, lfb.getTxtauthnote());
//                pstmt.setString(8, null);
//                pstmt.setString(9, null);
//                pstmt.executeUpdate();
//                pst = con.prepareStatement("UPDATE TASK_MASTER SET STATUS_ID=? WHERE TASK_ID=?");
//                pst.setInt(1, Integer.parseInt(lfb.getSltActionType()));
//                pst.setInt(2, Integer.parseInt(lfb.getHidTaskId()));
//                pst.executeUpdate();
//            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstt, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void updateMobileApproveDate(Leave lfb) {
        PreparedStatement pstt = null;
        PreparedStatement pstmt = null;
        PreparedStatement pst = null;
        Statement st = null;
        ResultSet rs = null;
        String startTime = "";
        int mcode = 0;
        Calendar cal = Calendar.getInstance();
        Timestamp timestamp = new Timestamp(cal.getTimeInMillis());
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
        Connection con = null;
        Date fappdate = null;
        Date apptodate = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT FDATE,TDATE FROM HW_EMP_LEAVE WHERE TASK_ID=?");
            pstmt.setInt(1, Integer.parseInt(lfb.getHidTaskId()));
            rs = pstmt.executeQuery();
            if (rs.next()) {
                pstt = con.prepareStatement("UPDATE HW_EMP_LEAVE SET APPROVE_FDATE=?,APPROVE_TDATE=? WHERE TASK_ID=?");
                pstt.setDate(1, rs.getDate("FDATE"));
                pstt.setDate(2, rs.getDate("TDATE"));

                pstt.setInt(3, Integer.parseInt(lfb.getHidTaskId()));
                pstt.executeUpdate();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstt, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public boolean updateTaskList(Leave lfb
    ) {
        PreparedStatement pst = null;
        PreparedStatement pstt = null;
        PreparedStatement pst1 = null;
        PreparedStatement pstmt = null;
        PreparedStatement pstmt1 = null;
        String startTime = "";
        ResultSet rs = null;
        int mcode = 0;
        Calendar cal = Calendar.getInstance();
        Timestamp timestamp = new Timestamp(cal.getTimeInMillis());
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
        Connection con = null;
        boolean actionTaken = true;
        try {
            con = dataSource.getConnection();
            if (lfb.getSltActionType() != null && lfb.getSltActionType().equals("1")) {

                pstmt = con.prepareStatement("INSERT INTO WORKFLOW_LOG(TASK_ID, TASK_ACTION_DATE, ACTION_TAKEN_BY, SPC_ONTIME, TASK_STATUS_ID, NOTE,FORWARD_TO,FORWARDED_SPC) Values (?,?,?,?,?,?,?,?)");

                pstmt.setInt(1, Integer.parseInt(lfb.getHidTaskId()));
                pstmt.setTimestamp(2, timestamp);
                pstmt.setString(3, lfb.getHidempId());
                pstmt.setString(4, lfb.getHidSpcCode());
                pstmt.setInt(5, Integer.parseInt(lfb.getSltActionType()));
                pstmt.setString(6, lfb.getTxtauthnote());
                pstmt.setString(7, lfb.getHidAuthEmpId());
                pstmt.setString(8, lfb.getHidSpcAuthCode());
                pstmt.executeUpdate();

                pst = con.prepareStatement("SELECT SUBMITEDTO,SUBMIT_AUTH FROM HW_EMP_LEAVE WHERE TASK_ID=?");
                pst.setInt(1, Integer.parseInt(lfb.getHidTaskId()));
                rs = pst.executeQuery();
                if (rs.next()) {
                    pstmt1 = con.prepareStatement("UPDATE HW_EMP_LEAVE SET approved_empid=?,approved_spc=? WHERE task_id=?");
                    pstmt1.setString(1, rs.getString("SUBMITEDTO"));
                    pstmt1.setString(2, rs.getString("SUBMIT_AUTH"));
                    pstmt1.setInt(3, Integer.parseInt(lfb.getHidTaskId()));
                    pstmt1.executeUpdate();
                }

                pst = con.prepareStatement("SELECT issuing_auth_empid,issuing_auth_spc FROM HW_EMP_LEAVE WHERE TASK_ID=?");
                pst.setInt(1, Integer.parseInt(lfb.getHidTaskId()));
                rs = pst.executeQuery();
                if (rs.next()) {
                    pst = con.prepareStatement("UPDATE TASK_MASTER SET PENDING_AT=?,PENDING_SPC=?,STATUS_ID=? WHERE TASK_ID=?");
                    pst.setString(1, rs.getString("issuing_auth_empid"));
                    pst.setString(2, rs.getString("issuing_auth_spc"));
                    pst.setInt(3, 5);
                    pst.setInt(4, Integer.parseInt(lfb.getHidTaskId()));
                    pst.executeUpdate();
                }

            }
            if (lfb.getSltActionType() != null && lfb.getSltActionType().equals("42")) {

                pstmt = con.prepareStatement("INSERT INTO WORKFLOW_LOG(TASK_ID, TASK_ACTION_DATE, ACTION_TAKEN_BY, SPC_ONTIME, TASK_STATUS_ID, NOTE,FORWARD_TO,FORWARDED_SPC) Values (?,?,?,?,?,?,?,?)");

                pstmt.setInt(1, Integer.parseInt(lfb.getHidTaskId()));
                pstmt.setTimestamp(2, timestamp);
                pstmt.setString(3, lfb.getHidempId());
                pstmt.setString(4, lfb.getHidSpcCode());
                pstmt.setInt(5, Integer.parseInt(lfb.getSltActionType()));
                pstmt.setString(6, lfb.getTxtauthnote());
                pstmt.setString(7, lfb.getHidAuthEmpId());
                pstmt.setString(8, lfb.getHidSpcAuthCode());
                pstmt.executeUpdate();
                pst = con.prepareStatement("SELECT issuing_auth_empid,issuing_auth_spc FROM HW_EMP_LEAVE WHERE TASK_ID=?");
                pst.setInt(1, Integer.parseInt(lfb.getHidTaskId()));
                rs = pst.executeQuery();
                if (rs.next()) {
                    pst = con.prepareStatement("UPDATE TASK_MASTER SET PENDING_AT=?,PENDING_SPC=?,STATUS_ID=? WHERE TASK_ID=?");
                    pst.setString(1, rs.getString("issuing_auth_empid"));
                    pst.setString(2, rs.getString("issuing_auth_spc"));
                    pst.setInt(3, Integer.parseInt(lfb.getSltActionType()));
                    pst.setInt(4, Integer.parseInt(lfb.getHidTaskId()));
                    pst.executeUpdate();
                }
            }
            if (lfb.getSltActionType() != null && lfb.getSltActionType().equals("6")) {
                pst = con.prepareStatement("UPDATE TASK_MASTER SET PENDING_AT=?,PENDING_SPC=? WHERE TASK_ID=?");
                pst.setString(1, lfb.getHidAuthEmpId());
                pst.setString(2, lfb.getHidSpcAuthCode());
                pst.setInt(3, Integer.parseInt(lfb.getHidTaskId()));
                pst.executeUpdate();
            }
            if (lfb.getSltActionType() != null && lfb.getSltActionType().equals("2")) {
                pst = con.prepareStatement("UPDATE TASK_MASTER SET PENDING_AT=?,PENDING_SPC=? WHERE TASK_ID=?");
                pst.setString(1, lfb.getHidAuthEmpId());
                pst.setString(2, lfb.getHidSpcAuthCode());
                pst.setInt(3, Integer.parseInt(lfb.getHidTaskId()));
                pst.executeUpdate();
                pstt = con.prepareStatement("UPDATE HW_EMP_LEAVE set SUBMITEDTO=?,SUBMIT_AUTH=? WHERE TASK_ID=?");
                pstt.setString(1, lfb.getHidAuthEmpId());
                pstt.setString(2, lfb.getHidSpcAuthCode());
                pstt.setInt(3, Integer.parseInt(lfb.getHidTaskId()));
                pstt.executeUpdate();

                pstmt = con.prepareStatement("INSERT INTO WORKFLOW_LOG(TASK_ID, TASK_ACTION_DATE, ACTION_TAKEN_BY, SPC_ONTIME, TASK_STATUS_ID, NOTE,FORWARD_TO,FORWARDED_SPC) Values (?,?,?,?,?,?,?,?)");

                pstmt.setInt(1, Integer.parseInt(lfb.getHidTaskId()));
                pstmt.setTimestamp(2, timestamp);
                pstmt.setString(3, lfb.getHidempId());
                pstmt.setString(4, lfb.getHidSpcCode());
                pstmt.setInt(5, Integer.parseInt(lfb.getSltActionType()));
                pstmt.setString(6, lfb.getTxtauthnote());
                pstmt.setString(7, lfb.getHidAuthEmpId());
                pstmt.setString(8, lfb.getHidSpcAuthCode());
                pstmt.executeUpdate();

            }
//            else {
//
//                pst = con.prepareStatement("UPDATE TASK_MASTER SET STATUS_ID=? WHERE TASK_ID=?");

//                pst.setInt(1, Integer.parseInt(lfb.getSltActionType()));
//
//                pst.setInt(2, Integer.parseInt(lfb.getHidTaskId()));
//                pst.executeUpdate();
//
//            }
//            if(lfb.getAttachmentid()!=null){
//                for (int i = 0; i < lfb.getAttachmentid().length; i++) {
//                    pst1 = con.prepareStatement("UPDATE HW_ATTACHMENTS set TASK_ID=?,ATTACH_FLAG=? WHERE ATT_ID=" + lfb.getAttachmentid()[i]);
//                    pst1.setInt(1, mcode);
//                    pst1.setString(2, "L");
//                    pst1.executeUpdate();
//                }
//            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst, pstmt, pst1, pstt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return actionTaken;
    }

    @Override
    public String getApplicant(String taskId
    ) {
        ResultSet rs = null;
        Connection con = null;
        Statement st = null;
        String authorityEmpCode = null;
        String empSpc = "";
        String empName = "";

        try {
            con = dataSource.getConnection();
            st = con.createStatement();
            rs = st.executeQuery("SELECT INITIATED_BY,INITIATED_SPC,STATUS_ID  FROM TASK_MASTER WHERE TASK_ID='" + taskId + "'");
            if (rs.next()) {
                empSpc = rs.getString("INITIATED_SPC");
                empName = getNameWithPost(rs.getString("INITIATED_BY"), rs.getString("INITIATED_SPC"), rs.getString("STATUS_ID"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);

        }
        return empName;
    }

    public ArrayList getLeaveWorkFlowDtls(String taskId) {

        ResultSet rs = null;
        Statement st = null;
        LeaveFlowDtls lfd = null;
        ArrayList leaveFlowList = new ArrayList();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy' 'HH:MM");
        Connection con = null;
        try {
            con = dataSource.getConnection();
            st = con.createStatement();

            rs = st.executeQuery("SELECT TASK_ACTION_DATE,APPROVE_FDATE,APPROVE_TDATE,WORKFLOWLOG.TASK_ID,TASK_ACTION_DATE,TASK_STATUS_ID,NOTE,FORWARDED_SPC,ACTION_TAKEN_BY,forward_to, "
                    + " SPC_ONTIME FROM(SELECT TASK_ID,TASK_ACTION_DATE,TASK_STATUS_ID,NOTE,FORWARDED_SPC,ACTION_TAKEN_BY,forward_to,SPC_ONTIME FROM  WORKFLOW_LOG "
                    + "  WHERE TASK_ID='" + taskId + "' ORDER BY TASK_ACTION_DATE)WORKFLOWLOG INNER JOIN HW_EMP_LEAVE ON HW_EMP_LEAVE.TASK_ID=WORKFLOWLOG.TASK_ID ORDER BY TASK_ACTION_DATE ASC");
            while (rs.next()) {
                lfd = new LeaveFlowDtls();
                lfd.setTaskId(rs.getString("TASK_ID"));

                if (rs.getString("TASK_STATUS_ID") != null && rs.getString("TASK_STATUS_ID").equals("2")) {
                    lfd.setActionTakenBy(getNameWithPost(rs.getString("forward_to"), rs.getString("FORWARDED_SPC"), rs.getString("TASK_STATUS_ID")));
                    lfd.setActionTaken(getNameWithPost(rs.getString("ACTION_TAKEN_BY"), rs.getString("SPC_ONTIME"), rs.getString("TASK_STATUS_ID")));
                } else {

                    lfd.setActionTaken(getNameWithPost(rs.getString("action_taken_by"), rs.getString("FORWARDED_SPC"), rs.getString("TASK_STATUS_ID")));
                    lfd.setActionTakenBy(getNameWithPost(rs.getString("action_taken_by"), rs.getString("FORWARDED_SPC"), rs.getString("TASK_STATUS_ID")));
                }
                if (rs.getString("TASK_STATUS_ID").equals("1")) {
                    lfd.setStatus("Approved By");
                }
                if (rs.getString("TASK_STATUS_ID").equals("42")) {
                    lfd.setStatus("Decline By");
                }
                if (rs.getString("TASK_STATUS_ID").equals("2")) {
                    lfd.setStatus("Forward To");
                }
                if (rs.getString("TASK_STATUS_ID").equals("3")) {
                    lfd.setStatus("Pending At");
                }
                if (rs.getString("TASK_STATUS_ID").equals("105")) {
                    lfd.setStatus("Cancelled By");
                }
                lfd.setTaskdate(CommonFunctions.getFormattedOutputDatAndTime(rs.getTimestamp("TASK_ACTION_DATE")));
                if (rs.getString("TASK_STATUS_ID").equals("1")) {
                    lfd.setTxtApproveFrom(CommonFunctions.getFormattedOutputDate3(rs.getDate("APPROVE_FDATE")));
                    lfd.setTxtApproveTo(CommonFunctions.getFormattedOutputDate3(rs.getDate("APPROVE_TDATE")));
                }

                lfd.setStatusId(rs.getString("TASK_STATUS_ID"));
                lfd.setNote(rs.getString("NOTE"));
                lfd.setActionTakenId(rs.getString("ACTION_TAKEN_BY"));
                // lfd.setAttachments(getLogFileName(con, rs.getInt("LOG_ID")));
                leaveFlowList.add(lfd);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);

        }
        return leaveFlowList;
    }

    public boolean getIfLeaveRecordExist(String empid, String leavecode, String fromdate, String todate) {
        ResultSet rs = null;
        SelectOption so = null;
        Connection con = null;
        Statement st = null;
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
        boolean duplPeriod = false;
        boolean status = true;
        String dbf1 = "";
        String dbt1 = "";
        String approveFrmdate = "";
        String approveTodate = "";
        String query = "";
        String leaveStatus = "";
        try {
            con = dataSource.getConnection();
            Date fd1 = formatter.parse(fromdate);
            Date td1 = formatter.parse(todate);
            st = con.createStatement();
            rs = st.executeQuery("SELECT * FROM(SELECT * FROM HW_EMP_LEAVE WHERE EMP_ID= '" + empid + "')HWEMP INNER JOIN TASK_MASTER ON HWEMP.TASK_ID=TASK_MASTER.TASK_ID WHERE STATUS_ID!='42'");

            while (rs.next()) {
                leaveStatus = rs.getString("STATUS_ID");

                dbf1 = CommonFunctions.getFormattedOutputDate3(rs.getDate("FDATE"));
                dbt1 = CommonFunctions.getFormattedOutputDate3(rs.getDate("TDATE"));
                approveFrmdate = CommonFunctions.getFormattedOutputDate3(rs.getDate("APPROVE_FDATE"));
                approveTodate = CommonFunctions.getFormattedOutputDate3(rs.getDate("APPROVE_TDATE"));

                Date frs1 = formatter.parse(dbf1);
                Date trs1 = formatter.parse(dbt1);

                if ((approveFrmdate != null && !approveFrmdate.equals("")) && (approveTodate != null && !approveTodate.equals(""))) {
                    Date appfrs1 = formatter.parse(approveFrmdate);
                    Date apptrs1 = formatter.parse(approveTodate);
                    if (fd1.compareTo(appfrs1) > 0 && (fd1.compareTo(apptrs1) > 0 && td1.compareTo(apptrs1) > 0)) {
                        duplPeriod = true;

                    } else if ((fd1.compareTo(appfrs1) < 0) && (td1.compareTo(appfrs1) < 0 && td1.compareTo(apptrs1) < 0)) {
                        duplPeriod = true;

                    } else {
                        duplPeriod = false;

                    }
                } else {

                    if (fd1.compareTo(frs1) > 0 && (fd1.compareTo(trs1) > 0 && td1.compareTo(trs1) > 0)) {
                        duplPeriod = true;

                    } else if ((fd1.compareTo(frs1) < 0) && (td1.compareTo(frs1) < 0 && td1.compareTo(trs1) < 0)) {
                        duplPeriod = true;

                    } else {
                        duplPeriod = false;

                    }
                }
                if (duplPeriod == false) {
                    status = false;
                }

                if (leaveStatus.equals("105")) {
                    status = true;
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return status;
    }

    public boolean ifEmpExist(String empid) {
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        boolean ifEmpExist = true;
        try {
            con = dataSource.getConnection();
            st = con.createStatement();
            rs = st.executeQuery("SELECT * FROM HW_EMP_LEAVE WHERE EMP_ID= '" + empid + "'");
            if (rs.next() == false) {
                ifEmpExist = true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return ifEmpExist;
    }

    public void updateLeaveOrder(Leave leave) {
        Connection con = null;
        PreparedStatement pst = null;
        PreparedStatement pstmt = null;
        PreparedStatement stmt = null;
        PreparedStatement pstt = null;
        ResultSet rs = null;
        int mcode = 0;
        Calendar cal = Calendar.getInstance();
        Timestamp timestamp = new Timestamp(cal.getTimeInMillis());
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
        try {
            con = dataSource.getConnection();

            if (leave.getStatusId().equals("1")) {
                pstmt = con.prepareStatement("UPDATE TASK_MASTER SET STATUS_ID=? ,PENDING_AT=?,PENDING_SPC=? WHERE TASK_ID=?");
                pstmt.setInt(1, 5);
                pstmt.setString(2, leave.getHidAuthEmpId());
                pstmt.setString(3, leave.getHidSpcAuthCode());
                pstmt.setInt(4, Integer.parseInt(leave.getHidTaskId()));
                pstmt.executeUpdate();
            }

            /*if (leave.getStatusId().equals("41")) {
             pstmt = con.prepareStatement("UPDATE TASK_MASTER SET STATUS_ID=?,PENDING_AT=?,PENDING_SPC=? WHERE TASK_ID=?");
             pstmt.setInt(1, 4);
             pstmt.setString(2, leave.getHidAuthEmpId());
             pstmt.setString(3, leave.getHidSpcAuthCode());
             pstmt.setInt(4, Integer.parseInt(leave.getHidTaskId()));
             pstmt.executeUpdate();
             }*/
            if (leave.getStatusId().equals("41")) {
                stmt = con.prepareStatement("UPDATE HW_EMP_LEAVE SET ORD_NO=?,ORD_DATE=?,IS_SB_UPDATED=?  WHERE TASK_ID=?");
                stmt.setString(1, leave.getTxtOrdNo());
                Date ordDate = formatter.parse(leave.getTxtOrdDate());
                java.sql.Date leaveOrdDt = new java.sql.Date(ordDate.getTime());
                stmt.setDate(2, leaveOrdDt);
                stmt.setString(3, "N");
                stmt.setInt(4, Integer.parseInt(leave.getHidTaskId()));
                stmt.executeUpdate();
                pstmt = con.prepareStatement("UPDATE TASK_MASTER SET STATUS_ID=?,PENDING_AT=?,PENDING_SPC=? WHERE TASK_ID=?");
                pstmt.setInt(1, 4);
                pstmt.setString(2, leave.getHidAuthEmpId());
                pstmt.setString(3, leave.getHidSpcAuthCode());
                pstmt.setInt(4, Integer.parseInt(leave.getHidTaskId()));
                pstmt.executeUpdate();
            }

            if (leave.getStatusId().equals("5")) {
                pstt = con.prepareStatement("UPDATE HW_EMP_LEAVE SET APPROVE_FDATE=?,APPROVE_TDATE=? WHERE TASK_ID=?");
                Date fappdate = formatter.parse(leave.getTxtApproveFrom());
                java.sql.Date fappdateSql = new java.sql.Date(fappdate.getTime());
                pstt.setDate(1, fappdateSql);
                Date apptodate = formatter.parse(leave.getTxtApproveTo());
                java.sql.Date fapptodateSql = new java.sql.Date(apptodate.getTime());
                pstt.setDate(2, fapptodateSql);
                pstt.setInt(3, Integer.parseInt(leave.getHidTaskId()));
                pstt.executeUpdate();

//                pstt = con.prepareStatement("UPDATE TASK_MASTER SET PENDING_AT=?,PENDING_SPC=? WHERE TASK_ID=?");
//                pstt.setString(1, leave.getHidAuthEmpId());
//                pstt.setString(2, leave.getHidSpcAuthCode());
//                pstt.setInt(3, Integer.parseInt(leave.getHidTaskId()));
//                pstt.executeUpdate();
                // if ((leave.getHidAuthEmpId() != null && !leave.getHidAuthEmpId().equals("")) && (leave.getHidSpcAuthCode() != null && !lfb.getHidSpcAuthCode().equals(""))) {
                /* mcode = CommonFunctions.getMaxCodeInteger("WORKFLOW_LOG", "LOG_ID", con);
                 pstmt = con.prepareStatement("INSERT INTO WORKFLOW_LOG(LOG_ID,TASK_ID, TASK_ACTION_DATE, ACTION_TAKEN_BY, SPC_ONTIME, TASK_STATUS_ID, NOTE,FORWARD_TO,FORWARDED_SPC) Values (?,?,?,?,?,?,?,?,?)");
                 pstmt.setInt(1, mcode);
                 pstmt.setInt(2, Integer.parseInt(leave.getHidTaskId()));
                 pstmt.setTimestamp(3, timestamp);
                 pstmt.setString(4, leave.getLoginUser());
                 pstmt.setString(5, leave.getHidSpcCode());
                 pstmt.setInt(6, Integer.parseInt(leave.getSltActionType()));
                 pstmt.setString(7, leave.getTxtauthnote());
                 pstmt.setString(8, leave.getHidAuthEmpId());
                 pstmt.setString(9, leave.getHidSpcAuthCode());
                 pstmt.executeUpdate();*/
            }
            /* pst = con.prepareStatement("SELECT MOBILE FROM EMP_MAST WHERE EMP_ID=?");
             pst.setString(1, leave.getHidempId());
             rs = pst.executeQuery();
             if (rs.next()) {
             if (rs.getString("MOBILE") != null && !rs.getString("MOBILE").equals("")) {
             SMSThread smsthread = new SMSThread(leave.getHidempId(), rs.getString("MOBILE"), "S");
             Thread t1 = new Thread(smsthread);
             t1.start();
             }
             }*/

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst, pstmt, stmt);
            DataBaseFunctions.closeSqlObjects(con);

        }
    }

    @Override
    public void updateClLeaveBalance(String empCode, String tolId, double noOfDays, String curYear, String curMonth) {
        Connection con = null;
        PreparedStatement stmt = null;
        Statement st = null;
        ResultSet rs = null;
        double available = 0;
        double currElAvailable = 0;
        double carryForward = 0;
        double currAvailable = 0;

        try {
            con = dataSource.getConnection();
            st = con.createStatement();

            if (tolId.equals("HDL")) {
                tolId = "CL";
                //System.out.println("SELECT AVAILABLE,TOL_ID FROM EMP_LEAVE_BALANCE WHERE EMP_ID='" + empCode + "' AND TOL_ID='" + tolId + "' AND YEAR='" + curYear + "'  AND MONTH='" + curMonth + "'");
                rs = st.executeQuery("SELECT AVAILABLE,TOL_ID FROM EMP_LEAVE_BALANCE WHERE EMP_ID='" + empCode + "' AND TOL_ID='" + tolId + "' AND YEAR='" + curYear + "'  AND MONTH='" + curMonth + "'");
                if (rs.next()) {
                    available = rs.getDouble("AVAILABLE");
                    if (available >= 0.5) {
                        currAvailable = available - 0.5;
                    } else {
                        currAvailable = 0;
                    }
                }

                stmt = con.prepareStatement("UPDATE EMP_LEAVE_BALANCE SET AVAILABLE=? WHERE EMP_ID=? AND TOL_ID=? AND YEAR=? AND MONTH=?");
                stmt.setDouble(1, currAvailable);
                stmt.setString(2, empCode);
                stmt.setString(3, tolId);
                stmt.setString(4, curYear);
                stmt.setString(5, curMonth);
                int n = stmt.executeUpdate();
            } else {
                // System.out.println("======" + noOfDays);
                //  System.out.println("SELECT AVAILABLE,TOL_ID FROM EMP_LEAVE_BALANCE WHERE EMP_ID='" + empCode + "' AND TOL_ID='" + tolId + "' AND YEAR='" + curYear + "'  AND MONTH='" + curMonth + "'");
                rs = st.executeQuery("SELECT AVAILABLE,TOL_ID FROM EMP_LEAVE_BALANCE WHERE EMP_ID='" + empCode + "' AND TOL_ID='" + tolId + "' AND YEAR='" + curYear + "'  AND MONTH='" + curMonth + "'");
                if (rs.next()) {
                    available = rs.getDouble("AVAILABLE");
                    // System.out.println("======" + available);
                    currAvailable = available - noOfDays;

                    if (available > 0 && currAvailable < 0) {
                        currAvailable = 0;
                        carryForward = noOfDays - available;

                    } else if (available <= 0) {
                        currAvailable = 0;
                        carryForward = noOfDays - available;

                    }
                    if (carryForward > 0) {
                        rs = st.executeQuery("SELECT AVAILABLE,TOL_ID FROM EMP_LEAVE_BALANCE WHERE EMP_ID='" + empCode + "' AND TOL_ID='EL' AND YEAR='" + curYear + "' AND MONTH='" + curMonth + "'");
                        if (rs.next()) {
                            available = rs.getDouble("AVAILABLE");
                        }
                        currElAvailable = available - carryForward;
                        stmt = con.prepareStatement("UPDATE EMP_LEAVE_BALANCE SET AVAILABLE=? WHERE EMP_ID=? AND TOL_ID='EL' AND YEAR=? AND MONTH=?");
                        stmt.setDouble(1, currElAvailable);
                        stmt.setString(2, empCode);
                        stmt.setString(3, curYear);
                        stmt.setString(4, curMonth);
                        stmt.executeUpdate();

                    }

                    stmt = con.prepareStatement("UPDATE EMP_LEAVE_BALANCE SET AVAILABLE=? WHERE EMP_ID=? AND TOL_ID=? AND YEAR=? AND MONTH=?");
                    stmt.setDouble(1, currAvailable);
                    stmt.setString(2, empCode);
                    stmt.setString(3, tolId);
                    stmt.setString(4, curYear);
                    stmt.setString(5, curMonth);
                    stmt.executeUpdate();

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    @Override
    public void updateElLeaveBalance(String empCode, String tolId, double noOfDays, String curYear, String curMonth) {
        Connection con = null;
        PreparedStatement stmt = null;
        Statement st = null;
        ResultSet rs = null;
        double available = 0;
        double currAvailable = 0;

        try {
            con = dataSource.getConnection();
            st = con.createStatement();

            rs = st.executeQuery("SELECT AVAILABLE,TOL_ID FROM EMP_LEAVE_BALANCE WHERE EMP_ID='" + empCode + "' AND TOL_ID='" + tolId + "' AND YEAR='" + curYear + "'  AND MONTH='" + curMonth + "'");
            if (rs.next()) {
                available = rs.getInt("AVAILABLE");
                currAvailable = available - noOfDays;
            }
            stmt = con.prepareStatement("UPDATE EMP_LEAVE_BALANCE SET AVAILABLE=? WHERE EMP_ID=? AND TOL_ID=? AND YEAR=? AND MONTH=?");
            stmt.setDouble(1, currAvailable);
            stmt.setString(2, empCode);
            stmt.setString(3, tolId);
            stmt.setString(4, curYear);
            stmt.setString(5, curMonth);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    @Override
    public void updateHplLeaveBalance(String empCode, String tolId, double noOfDays, String year) {
        Connection con = null;
        PreparedStatement stmt = null;
        Statement st = null;
        ResultSet rs = null;
        double available = 0;
        double currAvailable = 0;
        double currColAvailable = 0;

        try {
            con = dataSource.getConnection();
            st = con.createStatement();

            rs = st.executeQuery("SELECT AVAILABLE,TOL_ID FROM EMP_LEAVE_BALANCE WHERE EMP_ID='" + empCode + "' AND TOL_ID='" + tolId + "' AND YEAR='" + year + "'");
            if (rs.next()) {
                available = rs.getInt("AVAILABLE");
                currAvailable = available - noOfDays;
                rs = st.executeQuery("SELECT AVAILABLE,TOL_ID FROM EMP_LEAVE_BALANCE WHERE EMP_ID='" + empCode + "' AND TOL_ID='COL' AND YEAR=?");
                if (rs.next()) {
                    available = rs.getInt("AVAILABLE");
                }
                double colDed = noOfDays / 2;
                currColAvailable = available - colDed;
                stmt = con.prepareStatement("UPDATE EMP_LEAVE_BALANCE SET AVAILABLE=? WHERE EMP_ID=? AND TOL_ID='COL'  AND YEAR=?");
                stmt.setDouble(1, currColAvailable);
                stmt.setString(2, empCode);
                stmt.setString(3, year);
                stmt.executeUpdate();
            }
            stmt = con.prepareStatement("UPDATE EMP_LEAVE_BALANCE SET AVAILABLE=? WHERE EMP_ID=? AND TOL_ID=? AND YEAR=?");
            stmt.setDouble(1, currAvailable);
            stmt.setString(2, empCode);
            stmt.setString(3, tolId);
            stmt.setString(4, year);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    @Override
    public void updateCommutedLeaveBalance(String empCode, String tolId, double noOfDays) {
        Connection con = null;
        PreparedStatement stmt = null;
        Statement st = null;
        ResultSet rs = null;
        double available = 0;
        double currAvailable = 0;
        double currHplAvailable = 0;

        try {
            con = dataSource.getConnection();
            st = con.createStatement();

            rs = st.executeQuery("SELECT AVAILABLE,TOL_ID FROM EMP_LEAVE_BALANCE WHERE EMP_ID='" + empCode + "' AND TOL_ID='" + tolId + "'");
            if (rs.next()) {
                available = rs.getInt("AVAILABLE");
                currAvailable = available - noOfDays;

                rs = st.executeQuery("SELECT AVAILABLE,TOL_ID FROM EMP_LEAVE_BALANCE WHERE EMP_ID='" + empCode + "' AND TOL_ID='HPL'");
                if (rs.next()) {
                    available = rs.getInt("AVAILABLE");
                }
                currHplAvailable = available - (noOfDays * 2);
                stmt = con.prepareStatement("UPDATE EMP_LEAVE_BALANCE SET AVAILABLE=? WHERE EMP_ID=? AND TOL_ID='HPL'");
                stmt.setDouble(1, currHplAvailable);
                stmt.setString(2, empCode);
                stmt.executeUpdate();
            }
            stmt = con.prepareStatement("UPDATE EMP_LEAVE_BALANCE SET AVAILABLE=? WHERE EMP_ID=? AND TOL_ID=?");
            stmt.setDouble(1, currAvailable);
            stmt.setString(2, empCode);
            stmt.setString(3, tolId);
            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    @Override
    public void updateMaternityLeaveBalance(String empCode, String tolId, double noOfDays) {
        Connection con = null;
        PreparedStatement stmt = null;
        Statement st = null;
        ResultSet rs = null;
        double available = 0;
        double currAvailable = 0;
        try {
            con = dataSource.getConnection();
            st = con.createStatement();

            rs = st.executeQuery("SELECT AVAILABLE,TOL_ID FROM EMP_LEAVE_BALANCE WHERE EMP_ID='" + empCode + "' AND TOL_ID='" + tolId + "'");
            if (rs.next()) {
                available = rs.getInt("AVAILABLE");
                currAvailable = available - noOfDays;
            }
            stmt = con.prepareStatement("UPDATE EMP_LEAVE_BALANCE SET AVAILABLE=? WHERE EMP_ID=? AND TOL_ID=?");
            stmt.setDouble(1, currAvailable);
            stmt.setString(2, empCode);
            stmt.setString(3, tolId);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    @Override
    public void updatePaternityLeaveBalance(String empCode, String tolId, double noOfDays) {
        Connection con = null;
        PreparedStatement stmt = null;
        Statement st = null;
        ResultSet rs = null;
        double available = 0;
        double currAvailable = 0;
        try {
            con = dataSource.getConnection();
            st = con.createStatement();

            rs = st.executeQuery("SELECT AVAILABLE,TOL_ID FROM EMP_LEAVE_BALANCE WHERE EMP_ID='" + empCode + "' AND TOL_ID='" + tolId + "'");
            if (rs.next()) {
                available = rs.getInt("AVAILABLE");
                currAvailable = available - noOfDays;
            }
            stmt = con.prepareStatement("UPDATE EMP_LEAVE_BALANCE SET AVAILABLE=? WHERE EMP_ID=? AND TOL_ID=?");
            stmt.setDouble(1, currAvailable);
            stmt.setString(2, empCode);
            stmt.setString(3, tolId);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    /*@Override
     public void updateLeaveBalance(String empCode, String tolId, int noOfDays) {
     Connection con = null;
     PreparedStatement stmt = null;
     Statement st = null;
     ResultSet rs = null;
     int available = 0;
     int currElAvailable = 0;
     int carryForward = 0;
     int currAvailable = 0;
     long currColAvailable = 0;
     int currHplAvailable = 0;

     try {
     con = dataSource.getConnection();
     st = con.createStatement();
     
     rs = st.executeQuery("SELECT AVAILABLE,TOL_ID FROM EMP_LEAVE_BALANCE WHERE EMP_ID='" + empCode + "' AND TOL_ID='" + tolId + "'");
     if (rs.next()) {
     available = rs.getInt("AVAILABLE");
     currAvailable = available - noOfDays;
     if (tolId.equals("CL")) {
     if (available > 0 && currAvailable < 0) {
     currAvailable = 0;
     carryForward = noOfDays - available;

     } else if (available <= 0) {
     currAvailable = 0;
     carryForward = noOfDays - available;

     }
     if (carryForward > 0) {
     rs = st.executeQuery("SELECT AVAILABLE,TOL_ID FROM EMP_LEAVE_BALANCE WHERE EMP_ID='" + empCode + "' AND TOL_ID='EL'");
     if (rs.next()) {
     available = rs.getInt("AVAILABLE");
     }
     currElAvailable = available - carryForward;
     stmt = con.prepareStatement("UPDATE EMP_LEAVE_BALANCE SET AVAILABLE=? WHERE EMP_ID=? AND TOL_ID='EL'");
     stmt.setInt(1, currElAvailable);
     stmt.setString(2, empCode);
     stmt.executeUpdate();

     }
     }
     if (tolId.equals("EL")) {
     currAvailable = available - noOfDays;
     }
     if (tolId.equals("HPL")) {
     currAvailable = available - noOfDays;
     rs = st.executeQuery("SELECT AVAILABLE,TOL_ID FROM EMP_LEAVE_BALANCE WHERE EMP_ID='" + empCode + "' AND TOL_ID='COL'");
     if (rs.next()) {
     available = rs.getInt("AVAILABLE");
     }
     long colDed = noOfDays / 2;
     currColAvailable = available - colDed;
     stmt = con.prepareStatement("UPDATE EMP_LEAVE_BALANCE SET AVAILABLE=? WHERE EMP_ID=? AND TOL_ID='COL'");
     stmt.setLong(1, currColAvailable);
     stmt.setString(2, empCode);
     stmt.executeUpdate();
     }
     if (tolId.equals("COL")) {
     currAvailable = available - noOfDays;
     rs = st.executeQuery("SELECT AVAILABLE,TOL_ID FROM EMP_LEAVE_BALANCE WHERE EMP_ID='" + empCode + "' AND TOL_ID='HPL'");
     if (rs.next()) {
     available = rs.getInt("AVAILABLE");
     }
     currHplAvailable = available - (noOfDays * 2);
     stmt = con.prepareStatement("UPDATE EMP_LEAVE_BALANCE SET AVAILABLE=? WHERE EMP_ID=? AND TOL_ID='HPL'");
     stmt.setInt(1, currHplAvailable);
     stmt.setString(2, empCode);
     stmt.executeUpdate();
     }
     }
     
     stmt = con.prepareStatement("UPDATE EMP_LEAVE_BALANCE SET AVAILABLE=? WHERE EMP_ID=? AND TOL_ID=?");
     stmt.setInt(1, currAvailable);
     stmt.setString(2, empCode);
     stmt.setString(3, tolId);
     stmt.executeUpdate();

     } catch (Exception e) {
     e.printStackTrace();
     } finally {
     DataBaseFunctions.closeSqlObjects(con);
     }
     }*/
    @Override
    public String getLeaveBalanceInfo(String empCode, String tolId, String year, String strMonth) {
        Connection con = null;
        Statement st = null;
        Statement st2 = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        PreparedStatement pst = null;
        PreparedStatement pst1 = null;
        PreparedStatement pst2 = null;
        PreparedStatement pst3 = null;

        ResultSet rs = null;
        ResultSet rs1 = null;
        String elLeaveString = "";
        String tolid = "";
        double totcltaken;
        ResultSet rs2 = null;
        ResultSet rs3 = null;
        ResultSet rs4 = null;
        ResultSet resultSet = null;
        ResultSet resultSet1 = null;
        ResultSet resultSet2 = null;
        ResultSet resultSet3 = null;
        double noofdaysleavetaken = 0.0;
        double creditcount = 0.0;
        double leaveBalance = 0.0;
        int maximumLimit = 0;
        double holidaycnt = 0.0;
        int sundays = 0;
        int saturday = 0;
        int holidaysundays = 0;
        int holidaysaturday = 0;

        try {
            con = dataSource.getConnection();
            st = con.createStatement();
            st2 = con.createStatement();
            if (tolId.equals("CL")) {
                // cldays= calculateDateDiff(String fromdate, String toDate, String empId, String tolid)
                ps = con.prepareStatement("select fdate,tdate from hw_emp_leave  inner join task_master on task_master.task_id=hw_emp_leave.task_id where  "
                        + "(status_id=1 or status_id=4 or status_id=5 or status_id=41) and process_id=1 and tol_id is not null "
                        + "and extract(year from fdate) = ?  and extract(year from tdate) =? and   "
                        + "emp_id=?  and (tol_id='CL' or tol_id='HDL')");
                ps.setInt(1, Integer.parseInt(year));
                ps.setInt(2, Integer.parseInt(year));
                ps.setString(3, empCode);
                resultSet = ps.executeQuery();
                while (resultSet.next()) {
                    //  System.out.println("SELECT COALESCE(COUNT(*), 0) DATECOUNT FROM G_HOLIDAY WHERE fdate between '" + resultSet.getString("fdate") + "'::DATE and '" + resultSet.getString("tdate") + "'::DATE");
                    resultSet1 = st.executeQuery("SELECT COALESCE(COUNT(*), 0) DATECOUNT FROM G_HOLIDAY WHERE fdate between '" + resultSet.getString("fdate") + "'::DATE and '" + resultSet.getString("tdate") + "'::DATE");
                    while (resultSet1.next()) {
                        holidaycnt = holidaycnt + resultSet1.getInt("DATECOUNT");
                        Calendar c1 = Calendar.getInstance();
                        Date date1 = CommonFunctions.getFormattedOutputDate(resultSet.getDate("fdate"));
                        c1.setTime(date1);

                        Calendar c2 = Calendar.getInstance();
                        Date date2 = CommonFunctions.getFormattedOutputDate(resultSet.getDate("tdate"));
                        c2.setTime(date2);
                        // System.out.println("SELECT fdate,tdate FROM G_HOLIDAY WHERE fdate between '" + resultSet.getString("fdate") + "'::DATE and '" + resultSet.getString("tdate") + "'::DATE");
                        resultSet2 = st2.executeQuery("SELECT fdate,tdate FROM G_HOLIDAY WHERE fdate between '" + resultSet.getString("fdate") + "'::DATE and '" + resultSet.getString("tdate") + "'::DATE");
                        while (resultSet2.next()) {
                            Calendar c3 = Calendar.getInstance();
                            Date date3 = CommonFunctions.getFormattedOutputDate(resultSet2.getDate("fdate"));
                            c3.setTime(date3);
                            Calendar c4 = Calendar.getInstance();
                            Date date4 = CommonFunctions.getFormattedOutputDate(resultSet2.getDate("tdate"));
                            c4.setTime(date4);
                            while (!c3.after(c4)) {
                                if (c3.get(Calendar.DAY_OF_WEEK) == 7 && (c3.get(Calendar.WEEK_OF_MONTH) == 2 || c3.get(Calendar.WEEK_OF_MONTH) == 4)) {
                                    holidaysaturday++;
                                }

                                if (c3.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                                    holidaysundays++;
                                }
                                c3.add(Calendar.DATE, 1);
                            }

                            while (!c1.after(c2)) {

                                if (c1.get(Calendar.DAY_OF_WEEK) == 7 && (c1.get(Calendar.WEEK_OF_MONTH) == 2 || c1.get(Calendar.WEEK_OF_MONTH) == 4)) {
                                    saturday++;
                                }

                                if (c1.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                                    sundays++;
                                }
                                c1.add(Calendar.DATE, 1);

                            }
                        }
                    }
                }

                pst = con.prepareStatement("select sum(tol_id) totcltaken from(select task_id, "
                        + "CASE tol_id "
                        + "  WHEN 'CL' THEN DATE_PART('day', TDATE::timestamp - FDATE::timestamp)+1  "
                        + "  WHEN 'HDL' THEN DATE_PART('day', TDATE::timestamp - FDATE::timestamp)+0.5  "
                        + "  end tol_id "
                        + " from hw_emp_leave where extract(year from fdate) = ?  and extract(year from tdate) = ?  "
                        + "and emp_id=?   group by tol_id,TDATE,FDATE,task_id)a1 inner join task_master on task_master.task_id=a1.task_id where  "
                        + "(status_id=1 or status_id=4 or status_id=5 or status_id=41) and process_id=1 and tol_id is not null");
                pst.setInt(1, Integer.parseInt(year));
                pst.setInt(2, Integer.parseInt(year));
                pst.setString(3, empCode);
                rs1 = pst.executeQuery();
                if (rs1.next()) {
                    totcltaken = rs1.getDouble("totcltaken");
                    if (holidaysundays > 0 && sundays > 0) {
                        // holidaycnt=holidaycnt-holidaysundays;
                        sundays = sundays - holidaysundays;

                    }
                    if (holidaysaturday > 0 && saturday > 0 && holidaycnt > 0) {
                        //  holidaycnt=holidaycnt-holidaysaturday;
                        saturday = saturday - holidaysaturday;
                    }

                    totcltaken = totcltaken - (holidaycnt + sundays + saturday);
                    ps1 = con.prepareStatement("select * from emp_mast where emp_id=? and  gender='F' and is_regular='Y'");
                    ps1.setString(1, empCode);
                    resultSet3 = ps1.executeQuery();
                    if (resultSet3.next()) {
                        if (totcltaken > 0) {
                            totcltaken = 25 - totcltaken;
                        } else {
                            totcltaken = 25;
                        }
                    } else {
                        if (totcltaken > 0) {
                            totcltaken = 15 - totcltaken;
                        } else {
                            totcltaken = 15;
                        }
                    }
//                    if (totcltaken > 0) {
//                        totcltaken = 15 - totcltaken;
//                    } else {
//                        totcltaken = 15;
//                    }
                    elLeaveString = String.valueOf(totcltaken);
                }

//                if (tolId.equals("EL") && Integer.parseInt(currBalance) >= 0) {
//                    elLeaveString = balance + "  " + "(" + currBalance + ")";
//                }
//                if (tolId.equals("EL") && Integer.parseInt(currBalance) < 0) {
//                    elLeaveString = availableLeave;
//                }
//                if (tolId.equals("HPL")) {
//                    elLeaveString = rs.getString("AVAILABLE");;
//                }
//                if (tolId.equals("COL")) {
//                    elLeaveString = rs.getString("AVAILABLE");;
//                }
            } else if (tolId.equals("EL")) {
                pst1 = con.prepareStatement("select sum(noofdays)noofdays from( "
                        + "select fdate,tdate,DATE_PART('day', TDATE::timestamp - FDATE::timestamp)+1 noofdays from emp_leave where emp_id=? and tol_id='EL' "
                        + "union "
                        + "select fdate,tdate,DATE_PART('day', TDATE::timestamp - FDATE::timestamp)+1 noofdays from hw_emp_leave "
                        + "inner join task_master on task_master.task_id=hw_emp_leave.task_id where  "
                        + "(status_id=1 or status_id=4 or status_id=5 or status_id=41 or status_id=105)  and emp_id=? and tol_id='EL' "
                        + "order by fdate)a2");
                pst1.setString(1, empCode);
                pst1.setString(2, empCode);
                rs2 = pst1.executeQuery();
                if (rs2.next()) {
                    noofdaysleavetaken = rs2.getDouble("noofdays");
                }
                pst2 = con.prepareStatement("select sum(cr_count) creditcount from emp_leave_cr where emp_id=? and tol_id='EL'");
                pst2.setString(1, empCode);
                rs3 = pst2.executeQuery();
                if (rs3.next()) {
                    creditcount = rs3.getDouble("creditcount");
                }
                pst3 = con.prepareStatement("SELECT LCR_ID,EMP_ID,TOL_ID,SP_FROM,SP_TO,COM_MON,CR_COUNT,CR_TYPE,'1' SL FROM EMP_LEAVE_CR WHERE EMP_ID=? AND TOL_ID=? AND CR_TYPE='G'  ORDER BY SP_FROM");
                pst3.setString(1, empCode);
                pst3.setString(2, tolId);
                rs4 = pst3.executeQuery();
                while (rs4.next()) {
                    maximumLimit = getMaxCreditLimitofLeave(tolId, CommonFunctions.getFormattedOutputDate3(rs4.getDate("SP_FROM")), con);

                }

                if (String.valueOf(creditcount) != null) {
                    if (creditcount > maximumLimit) {
                        creditcount = maximumLimit;
                        if (creditcount >= noofdaysleavetaken) {
                            if (noofdaysleavetaken > 0) {
                                leaveBalance = (creditcount + 15) - noofdaysleavetaken;
                            } else {
                                leaveBalance = maximumLimit;
                            }
                        } else {
                            leaveBalance = 0;
                        }
                    } else {
                        if (creditcount > 0) {
                            leaveBalance = creditcount - noofdaysleavetaken;
                        } else {
                            leaveBalance = 0;
                        }

                    }
                    elLeaveString = leaveBalance + "";

                }

            } else if (tolId.equals("COL")) {
                pst1 = con.prepareStatement("select sum(noofdays)noofdays from( "
                        + "select fdate,tdate,DATE_PART('day', TDATE::timestamp - FDATE::timestamp)+1 noofdays from emp_leave where emp_id=? and tol_id='COL' "
                        + "union "
                        + "select fdate,tdate,DATE_PART('day', TDATE::timestamp - FDATE::timestamp)+1 noofdays from hw_emp_leave "
                        + "inner join task_master on task_master.task_id=hw_emp_leave.task_id where  "
                        + "(status_id=1 or status_id=4 or status_id=5 or status_id=41 or status_id=105)  and emp_id=? and tol_id='COL' "
                        + "order by fdate)a2");
                pst1.setString(1, empCode);
                pst1.setString(2, empCode);
                rs2 = pst1.executeQuery();
                if (rs2.next()) {
                    noofdaysleavetaken = rs2.getDouble("noofdays");
                }
                pst2 = con.prepareStatement("select sum(cr_count) creditcount from emp_leave_cr where emp_id=? and tol_id='COL'");
                pst2.setString(1, empCode);
                rs3 = pst2.executeQuery();
                if (rs3.next()) {
                    creditcount = rs3.getDouble("creditcount");
                }
                pst3 = con.prepareStatement("SELECT LCR_ID,EMP_ID,TOL_ID,SP_FROM,SP_TO,COM_MON,CR_COUNT,CR_TYPE,'1' SL FROM EMP_LEAVE_CR WHERE EMP_ID=? AND TOL_ID=? AND CR_TYPE='G'  ORDER BY SP_FROM");
                pst3.setString(1, empCode);
                pst3.setString(2, tolId);
                rs4 = pst3.executeQuery();
                while (rs4.next()) {
                    maximumLimit = getMaxCreditLimitofLeave(tolId, CommonFunctions.getFormattedOutputDate3(rs4.getDate("SP_FROM")), con);

                }

                if (String.valueOf(creditcount) != null) {
                    if (creditcount > maximumLimit) {
                        creditcount = maximumLimit;
                        if (noofdaysleavetaken > 0) {
                            if (creditcount >= noofdaysleavetaken) {
                                if (noofdaysleavetaken > 0) {
                                    leaveBalance = (creditcount + 10) - noofdaysleavetaken;
                                } else {
                                    leaveBalance = maximumLimit;
                                }
                            } else {
                                leaveBalance = 0;
                            }
                        } else {
                            leaveBalance = maximumLimit;
                        }
                    } else {
                        if (creditcount > 0) {
                            leaveBalance = creditcount - noofdaysleavetaken;
                        } else {
                            leaveBalance = 0;
                        }

                    }
                    elLeaveString = leaveBalance + "";

                }
            } else if (tolId.equals("HPL")) {

                pst1 = con.prepareStatement("select sum(noofdays)noofdays from( "
                        + "select fdate,tdate,DATE_PART('day', TDATE::timestamp - FDATE::timestamp)+1 noofdays from emp_leave where emp_id=? and tol_id='HPL' "
                        + "union "
                        + "select fdate,tdate,DATE_PART('day', TDATE::timestamp - FDATE::timestamp)+1 noofdays from hw_emp_leave "
                        + "inner join task_master on task_master.task_id=hw_emp_leave.task_id where  "
                        + "(status_id=1 or status_id=4 or status_id=5 or status_id=41 or status_id=105)  and emp_id=? and tol_id='HPL' "
                        + "order by fdate)a2");
                pst1.setString(1, empCode);
                pst1.setString(2, empCode);
                rs2 = pst1.executeQuery();
                if (rs2.next()) {
                    noofdaysleavetaken = rs2.getDouble("noofdays");
                }
                pst2 = con.prepareStatement("select sum(cr_count) creditcount from emp_leave_cr where emp_id=? and tol_id='HPL'");
                pst2.setString(1, empCode);
                rs3 = pst2.executeQuery();
                if (rs3.next()) {
                    creditcount = rs3.getDouble("creditcount");
                }
                pst3 = con.prepareStatement("SELECT LCR_ID,EMP_ID,TOL_ID,SP_FROM,SP_TO,COM_MON,CR_COUNT,CR_TYPE,'1' SL FROM EMP_LEAVE_CR WHERE EMP_ID=? AND TOL_ID=? AND CR_TYPE='G'  ORDER BY SP_FROM");
                pst3.setString(1, empCode);
                pst3.setString(2, tolId);
                rs4 = pst3.executeQuery();
                while (rs4.next()) {
                    maximumLimit = getMaxCreditLimitofLeave(tolId, CommonFunctions.getFormattedOutputDate3(rs4.getDate("SP_FROM")), con);

                }

                if (String.valueOf(creditcount) != null) {
                    if (creditcount > maximumLimit) {
                        creditcount = maximumLimit;
                        if (noofdaysleavetaken > 0) {
                            if (creditcount >= noofdaysleavetaken) {
                                if (noofdaysleavetaken > 0) {
                                    leaveBalance = (creditcount + 20) - noofdaysleavetaken;
                                } else {
                                    leaveBalance = maximumLimit;
                                }
                            } else {
                                leaveBalance = 0;
                            }
                        } else {
                            leaveBalance = maximumLimit;
                        }
                    } else {
                        if (creditcount > 0) {
                            leaveBalance = creditcount - noofdaysleavetaken;
                        } else {
                            leaveBalance = 0;
                        }

                    }
                    elLeaveString = leaveBalance + "";

                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(pst, pst1, pst2, pst3);
            DataBaseFunctions.closeSqlObjects(ps, ps1);
            DataBaseFunctions.closeSqlObjects(rs1, rs2, rs3, rs4);
            DataBaseFunctions.closeSqlObjects(resultSet, resultSet1, resultSet2, resultSet3);
            DataBaseFunctions.closeSqlObjects(st, st2);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return elLeaveString;
    }

    @Override
    public void getLeaveOpeningBalance(String empid, String tolId, String currDate) {
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        Statement st1 = null;
        ResultSet rs1 = null;
        Statement st2 = null;
        Statement st3 = null;
        Statement st4 = null;
        ResultSet rs2 = null;
        ResultSet rs3 = null;
        ResultSet rs4 = null;
        ResultSet rs5 = null;
        long crCount = 0;
        long totCrLeave = 0;
        PreparedStatement pstmt = null;
        PreparedStatement pstmt1 = null;
        int maximumLimit = 0;
        long debitCount = 0;
        long totdebitCount = 0;
        //String currYear = null;
        long leaveAvailable = 0;
        int finalLeaveLimit = 0;
        long totCasualdebitCount = 0;
        long casualdebitCount = 0;
        String currYearStartDate = "";
        String currYearEndDate = "";
        long carryClCredit = 0;
        long totDebtCont = 0;
        long leaveBalance = 0;
        boolean leaveBalanceFound = false;
        try {
            con = dataSource.getConnection();
            st = con.createStatement();
            st1 = con.createStatement();
            st2 = con.createStatement();
            st3 = con.createStatement();
            st4 = con.createStatement();
            Calendar cal = Calendar.getInstance();
            int curYear = cal.get(Calendar.YEAR);
            // int curmon = cal.get(Calendar.MONTH) + 1;
            SimpleDateFormat simpleformat = new SimpleDateFormat("MM");
            String strMonth = simpleformat.format(new Date());
            currYearStartDate = String.valueOf(curYear) + "-" + "-01-" + "01";
            currYearEndDate = String.valueOf(curYear) + "-" + "-12-" + "31";
            rs3 = st3.executeQuery("SELECT * FROM EMP_LEAVE_BALANCE WHERE EMP_ID='" + empid + "' AND TOL_ID='" + tolId + "' AND YEAR='" + curYear + "' AND MONTH='" + strMonth + "'");
            if (rs3.next()) {
                leaveBalanceFound = true;
            }
            if (leaveBalanceFound == false) {
                if (strMonth.equals("01")) {
                    pstmt = con.prepareStatement("INSERT INTO EMP_LEAVE_BALANCE(YEAR,MONTH,EMP_ID,TOL_ID,BALANCE,AVAILABLE)VALUES(?,?,?,?,?,?)");
                    pstmt.setString(1, String.valueOf(curYear));
                    pstmt.setString(2, strMonth);
                    pstmt.setString(3, empid);
                    pstmt.setString(4, tolId);
                    pstmt.setLong(5, leaveBalance);
                    pstmt.setLong(6, leaveAvailable);
                    pstmt.executeUpdate();
                } else {
                    pstmt1 = con.prepareStatement("select balance,available from emp_leave_balance where emp_id=? and tol_id=? and year=? and month=?");
                    pstmt1.setString(1, empid);
                    pstmt1.setString(2, tolId);
                    pstmt1.setString(3, String.valueOf(curYear));
                    if ((Integer.parseInt(strMonth) - 1) < 10) {
                        pstmt1.setString(4, "0" + (Integer.parseInt(strMonth) - 1));

                    } else {
                        pstmt1.setString(4, String.valueOf((Integer.parseInt(strMonth) - 1)));
                    }

                    rs5 = pstmt1.executeQuery();
                    if (rs5.next()) {

                        pstmt = con.prepareStatement("INSERT INTO EMP_LEAVE_BALANCE(YEAR,MONTH,EMP_ID,TOL_ID,BALANCE,AVAILABLE)VALUES(?,?,?,?,?,?)");
                        pstmt.setString(1, String.valueOf(curYear));
                        pstmt.setString(2, strMonth);
                        pstmt.setString(3, empid);
                        pstmt.setString(4, tolId);
                        pstmt.setLong(5, rs5.getInt("balance"));
                        pstmt.setDouble(6, rs5.getDouble("available"));
                        pstmt.executeUpdate();
                    }
                }
                rs = st.executeQuery("SELECT LCR_ID,EMP_ID,TOL_ID,SP_FROM,SP_TO,COM_MON,CR_COUNT,CR_TYPE,'1' SL FROM EMP_LEAVE_CR WHERE EMP_ID='" + empid + "' AND TOL_ID='" + tolId + "' AND CR_TYPE='G'  ORDER BY SP_FROM");
                if (!rs.next()) {
                    if (tolId.equals("CL")) {
                        leaveBalance = 15;
                        leaveAvailable = 15;
                    }
                    if (tolId.equals("HPL")) {
                        leaveBalance = 20;
                        leaveAvailable = 20;
                    }
                    if (tolId.equals("EL")) {
                        leaveBalance = 0;
                        leaveAvailable = 15;
                    }
                } else {
                    while (rs.next()) {

                        crCount = rs.getInt("CR_COUNT");
                        totCrLeave = totCrLeave + crCount;
                        maximumLimit = getMaxCreditLimitofLeave(rs.getString("TOL_ID"), CommonFunctions.getFormattedOutputDate3(rs.getDate("SP_FROM")), con);

                        finalLeaveLimit = maximumLimit + 15;
                        rs2 = st2.executeQuery("SELECT LSOT_ID,FDATE,TDATE, DATE_PART('day', TDATE::timestamp - FDATE::timestamp)+1 DEBIT_COUNT  FROM EMP_LEAVE "
                                + "WHERE (FDATE BETWEEN '" + rs.getDate("SP_FROM") + "' AND '" + rs.getDate("SP_TO") + "' "
                                + "OR    TDATE   BETWEEN '" + rs.getDate("SP_FROM") + "' AND '" + rs.getDate("SP_TO") + "') AND EMP_ID='" + empid + "' AND TOL_ID='" + tolId + "' AND FDATE >='" + rs.getDate("SP_FROM") + "'");
                        while (rs2.next()) {
                            debitCount = rs2.getInt("DEBIT_COUNT");
                            totdebitCount = totdebitCount + debitCount;

                            if (rs2.getDate("TDATE").after(rs.getDate("SP_TO"))) {
                                // System.out.println("SELECT DATE_PART('day', '" + rs.getDate("SP_TO") + "'::timestamp - '" + rs2.getDate("FDATE") + "'::timestamp)+1 DEBIT_COUNT,DATE_PART('day', '" + rs2.getDate("TDATE") + "'::timestamp - '" + rs.getDate("SP_TO") + "'::timestamp) DEBIT_COUNT1");
                                rs4 = st4.executeQuery("SELECT DATE_PART('day', '" + rs.getDate("SP_TO") + "'::timestamp - '" + rs2.getDate("FDATE") + "'::timestamp)+1 DEBIT_COUNT,DATE_PART('day', '" + rs2.getDate("TDATE") + "'::timestamp - '" + rs.getDate("SP_TO") + "'::timestamp) DEBIT_COUNT1");
                                if (rs4.next()) {
                                    totdebitCount = rs4.getLong("DEBIT_COUNT") + rs4.getLong("DEBIT_COUNT1");
                                }
                            }
                        }

                        totCrLeave = totCrLeave - totdebitCount;

                        if (tolId.equals("CL")) {
                            leaveBalance = 15;
                            leaveAvailable = totCrLeave + 15;
                        }
                        if (tolId.equals("EL")) {
                            if (totCrLeave >= maximumLimit) {
                                leaveBalance = maximumLimit;

                            } else {

                                leaveBalance = totCrLeave;
                            }
                            leaveAvailable = leaveBalance + 15;
                        }
                        if (tolId.equals("HPL")) {
                            leaveBalance = totCrLeave;
                            leaveAvailable = totCrLeave + 20;
                        }
                        if (tolId.equals("PL")) {
                            leaveBalance = totCrLeave;
                            leaveAvailable = totCrLeave + 15;
                        }
                        if (tolId.equals("ML")) {
                            leaveBalance = totCrLeave;
                            leaveAvailable = totCrLeave + 180;
                        }
                        //totCrLeave
                        totdebitCount = 0;

                    }
                }
            } else {
                rs = st.executeQuery("SELECT LCR_ID,EMP_ID,TOL_ID,SP_FROM,SP_TO,COM_MON,CR_COUNT,CR_TYPE,'1' SL FROM EMP_LEAVE_CR WHERE EMP_ID='" + empid + "' AND TOL_ID='" + tolId + "' AND CR_TYPE='G'  ORDER BY SP_FROM");
                while (rs.next()) {
                    crCount = rs.getInt("CR_COUNT");
                    totCrLeave = totCrLeave + crCount;
                    maximumLimit = getMaxCreditLimitofLeave(rs.getString("TOL_ID"), CommonFunctions.getFormattedOutputDate3(rs.getDate("SP_FROM")), con);
                    finalLeaveLimit = maximumLimit + 15;
                    rs2 = st2.executeQuery("SELECT LSOT_ID,FDATE,TDATE, DATE_PART('day', TDATE::timestamp - FDATE::timestamp)+1 DEBIT_COUNT  FROM EMP_LEAVE "
                            + "WHERE (FDATE BETWEEN '" + rs.getDate("SP_FROM") + "' AND '" + rs.getDate("SP_TO") + "' "
                            + "AND    TDATE   BETWEEN '" + rs.getDate("SP_FROM") + "' AND '" + rs.getDate("SP_TO") + "') AND EMP_ID='" + empid + "' AND TOL_ID='" + tolId + "' AND FDATE >='" + rs.getDate("SP_FROM") + "'");
                    while (rs2.next()) {
                        debitCount = rs2.getInt("DEBIT_COUNT");
                        totdebitCount = totdebitCount + debitCount;

                        if (rs2.getDate("TDATE").after(rs.getDate("SP_TO"))) {
                            // System.out.println("SELECT DATE_PART('day', '" + rs.getDate("SP_TO") + "'::timestamp - '" + rs2.getDate("FDATE") + "'::timestamp)+1 DEBIT_COUNT,DATE_PART('day', '" + rs2.getDate("TDATE") + "'::timestamp - '" + rs.getDate("SP_TO") + "'::timestamp) DEBIT_COUNT1");
                            System.out.println("SELECT DATE_PART('day', '" + rs.getDate("SP_TO") + "'::timestamp - '" + rs2.getDate("FDATE") + "'::timestamp)+1 DEBIT_COUNT,DATE_PART('day', '" + rs2.getDate("TDATE") + "'::timestamp - '" + rs.getDate("SP_TO") + "'::timestamp) DEBIT_COUNT1");
                            rs4 = st4.executeQuery("SELECT DATE_PART('day', '" + rs.getDate("SP_TO") + "'::timestamp - '" + rs2.getDate("FDATE") + "'::timestamp)+1 DEBIT_COUNT,DATE_PART('day', '" + rs2.getDate("TDATE") + "'::timestamp - '" + rs.getDate("SP_TO") + "'::timestamp) DEBIT_COUNT1");
                            if (rs4.next()) {
                                totdebitCount = rs4.getLong("DEBIT_COUNT") + rs4.getLong("DEBIT_COUNT1");
                            }
                        }
                    }

                    totCrLeave = totCrLeave - totdebitCount;
                    if (tolId.equals("CL")) {
                        leaveBalance = 15;
                        leaveAvailable = totCrLeave + 15;
                    }

                    if (tolId.equals("EL")) {
                        if (totCrLeave >= maximumLimit) {
                            leaveBalance = maximumLimit;
                        } else {
                            leaveBalance = totCrLeave;
                        }
                        leaveAvailable = leaveBalance + 15;
                    }
                    if (tolId.equals("HPL")) {
                        leaveBalance = totCrLeave;
                        leaveAvailable = totCrLeave + 20;
                    }
                    if (tolId.equals("PL")) {
                        leaveBalance = totCrLeave;
                        leaveAvailable = totCrLeave + 15;
                    }
                    if (tolId.equals("ML")) {
                        leaveBalance = totCrLeave;
                        leaveAvailable = totCrLeave + 180;
                    }
                    //totCrLeave
                    totdebitCount = 0;
                    pstmt = con.prepareStatement("update EMP_LEAVE_BALANCE set BALANCE=?, AVAILABLE=? where emp_id=? and tol_id=? and year=? and month=?");

                    pstmt.setLong(1, leaveBalance);
                    pstmt.setLong(2, leaveAvailable);
                    pstmt.setString(3, empid);
                    pstmt.setString(4, tolId);
                    pstmt.setString(5, String.valueOf(curYear));
                    pstmt.setString(6, strMonth);
                    pstmt.executeUpdate();

                }

            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, rs1, rs2, rs3);
            DataBaseFunctions.closeSqlObjects(st, st1, st2, st3);
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);

        }

    }

    public static int getMaxCreditLimitofLeave(String leaveType, String onDate, Connection con) throws Exception {
        int maximumLimit = 0;
        ResultSet rs = null;
        //Connection con=null;
        Statement st = null;

        try {
            //con=dataSource.getConnection();
            st = con.createStatement();

            rs = st.executeQuery("SELECT DISTINCT MAX_CR FROM g_leave WHERE TOL_ID='" + leaveType + "' AND WEF=(SELECT MAX(WEF) FROM g_leave WHERE TOL_ID='" + leaveType + "' AND WEF<='" + onDate + "')");
            //System.out.println("==qry==" + "SELECT DISTINCT MAX_CR FROM g_leave WHERE TOL_ID='" + leaveType + "' AND WEF=(SELECT MAX(WEF) FROM g_leave WHERE TOL_ID='" + leaveType + "' AND WEF<='" + onDate + "'");
            while (rs.next()) {
                if (rs.getString("MAX_CR") != null && !rs.getString("MAX_CR").equals("")) {
                    maximumLimit = Integer.parseInt(rs.getString("MAX_CR"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
        }
        return maximumLimit;
    }

    public static long getCarryOverFromCl(Connection con, String empid, String tolId, String currDate, String currYearStartDate, String currYearEndDate) throws Exception {

        Statement st = null;
        ResultSet rs = null;
        int maximumLimit = 0;
        long casualdebitCount = 0;
        long totCasualdebitCount = 0;
        long carryClCredit = 0;
        try {
            st = con.createStatement();
            maximumLimit = getMaxCreditLimitofLeave("CL", currDate, con);

            rs = st.executeQuery("SELECT LSOT_ID,FDATE,TDATE, DATE_PART('day', TDATE::timestamp - FDATE::timestamp)+1 DEBIT_COUNT  FROM EMP_LEAVE WHERE EMP_ID='" + empid + "' AND (FDATE BETWEEN '" + currYearStartDate + "' AND '" + currYearEndDate + "' OR TDATE   BETWEEN '" + currYearStartDate + "' AND '" + currYearEndDate + "')  AND TOL_ID='CL' ORDER BY FDATE");
            while (rs.next()) {
                casualdebitCount = rs.getInt("DEBIT_COUNT");

                totCasualdebitCount = totCasualdebitCount + casualdebitCount;
            }

            if (totCasualdebitCount >= 10 && totCasualdebitCount <= 15) {
                carryClCredit = totCasualdebitCount - 10;

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
        }
        return carryClCredit;
    }

    public void updateEmpleave(Leave lfb, int notId) {
        PreparedStatement pst = null;
        PreparedStatement pstmt = null;
        PreparedStatement pstmtt = null;
        Connection con = null;
        String leaveId = "";
        //String notId = "";
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
        try {
            con = dataSource.getConnection();
            leaveId = maxleaveidDao.getMaxLeaveId();
            pstmtt = con.prepareStatement("INSERT INTO EMP_LEAVE(NOT_ID, NOT_TYPE, EMP_ID, LSOT_ID, TOL_ID, FDATE, TDATE,PREFIX_DATE,PREFIX_TO,SUFFIX_FROM,SUFFIX_DATE)VALUES(?,?,?,?,?,?,?,?,?,?,?)");
            pstmtt.setInt(1, notId);
            pstmtt.setString(2, "LEAVE");
            pstmtt.setString(3, lfb.getEmpId());
            pstmtt.setString(4, "01");
            pstmtt.setString(5, lfb.getTollid());
            if (lfb.getTxtApproveFrom() != null && !lfb.getTxtApproveFrom().equals("")) {
                Date periodFrom = formatter.parse(lfb.getTxtApproveFrom());
                java.sql.Date periodFromSql = new java.sql.Date(periodFrom.getTime());
                pstmtt.setDate(6, periodFromSql);
            } else {
                pstmtt.setDate(6, null);
            }
            if (lfb.getTxtApproveTo() != null && !lfb.getTxtApproveTo().equals("")) {
                Date periodTo = formatter.parse(lfb.getTxtApproveTo());
                java.sql.Date periodToSql = new java.sql.Date(periodTo.getTime());
                pstmtt.setDate(7, periodToSql);
            } else {
                pstmtt.setDate(7, null);
            }

            if (lfb.getTxtprefixFrom() != null && !lfb.getTxtprefixFrom().equals("")) {
                Date prefixFrom = formatter.parse(lfb.getTxtprefixFrom());
                java.sql.Date prefixFromSql = new java.sql.Date(prefixFrom.getTime());
                pstmtt.setDate(8, prefixFromSql);
            } else {
                pstmtt.setDate(8, null);
            }
            if (lfb.getTxtprefixTo() != null && !lfb.getTxtprefixTo().equals("")) {
                Date prefixTo = formatter.parse(lfb.getTxtprefixTo());
                java.sql.Date prefixToSql = new java.sql.Date(prefixTo.getTime());
                pstmtt.setDate(9, prefixToSql);
            } else {
                pstmtt.setDate(9, null);
            }
            if (lfb.getTxtsuffixFrom() != null && !lfb.getTxtsuffixFrom().equals("")) {
                Date sufixFrom = formatter.parse(lfb.getTxtsuffixFrom());
                java.sql.Date sufixFromSql = new java.sql.Date(sufixFrom.getTime());
                pstmtt.setDate(10, sufixFromSql);
            } else {
                pstmtt.setDate(10, null);
            }
            if (lfb.getTxtsuffixTo() != null && !lfb.getTxtsuffixTo().equals("")) {
                Date sufixTo = formatter.parse(lfb.getTxtsuffixTo());
                java.sql.Date sufixToSql = new java.sql.Date(sufixTo.getTime());
                pstmtt.setDate(11, sufixToSql);
            } else {
                pstmtt.setDate(11, null);
            }
            pstmtt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmtt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    public void updateClLeaveBalanceAfterJoin(String initiatedEmpId, String tolId, double noOfDays, String year) {

        Connection con = null;
        PreparedStatement pst = null;
        PreparedStatement pst1 = null;
        ResultSet rs = null;
        double available = 0;
        try {

            con = dataSource.getConnection();
            pst = con.prepareStatement("select available from emp_leave_balance where emp_id=? and tol_id=? and year=?");
            pst.setString(1, initiatedEmpId);
            pst.setString(2, tolId);
            pst.setString(3, year);
            rs = pst.executeQuery();
            if (rs.next()) {
                pst1 = con.prepareStatement("update emp_leave_balance set available=? where emp_id=? and tol_id=? and year=?");
                available = rs.getDouble("available");

                available = available + noOfDays;
                pst1.setDouble(1, available);
                pst1.setString(2, initiatedEmpId);
                pst1.setString(3, tolId);
                pst1.setString(4, year);
                pst1.executeUpdate();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(pst1);
            DataBaseFunctions.closeSqlObjects(con);

        }
    }

    @Override
    public double calculateDateDiffAfterJoin(String joinDate, String toDate, String empId, String tolid) {
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        int dateDiff = 0;
        int holiDiff = 0;
        String query = "";
        String cId = "";
        ResultSet rs1 = null;
        Statement st1 = null;
        ResultSet rs2 = null;
        Statement st2 = null;
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
        //SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MMM-yyyy");

        try {

            Date d1 = formatter.parse(joinDate);
            Date d2 = formatter.parse(toDate);
            con = dataSource.getConnection();
            st = con.createStatement();
            st1 = con.createStatement();

            rs = st.executeQuery("SELECT DATE_PART('day', '" + toDate + "'::timestamp - '" + joinDate + "'::timestamp)+1 DATECOUNT");
            if (rs.next()) {
                dateDiff = rs.getInt("DATECOUNT");

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
            DataBaseFunctions.closeSqlObjects(st, st1);
            DataBaseFunctions.closeSqlObjects(rs, rs1);
        }
        return dateDiff;
    }

    @Override
    public double calculateDateDiff(String fromdate, String toDate, String empId, String tolid) {
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        int dateDiff = 0;
        int holiDiff = 0;
        String query = "";
        String cId = "";
        ResultSet rs1 = null;
        Statement st1 = null;
        ResultSet rs2 = null;
        Statement st2 = null;
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
        double daysdiff = 0;
        try {

            Date d1 = formatter.parse(fromdate);
            Date d2 = formatter.parse(toDate);
            con = dataSource.getConnection();
            st = con.createStatement();
            st1 = con.createStatement();

            rs = st.executeQuery("SELECT DATE_PART('day', '" + toDate + "'::timestamp - '" + fromdate + "'::timestamp)+1 DATECOUNT");
            if (rs.next()) {
                dateDiff = rs.getInt("DATECOUNT");
            }
            //rs1 = st1.executeQuery("SELECT DATE_PART('day', tdate::timestamp - fdate::timestamp)+1 DATECOUNT  FROM G_HOLIDAY  WHERE (fdate, tdate) OVERLAPS ('" + fromdate + "'::DATE, '" + toDate + "'::DATE)");
            rs1 = st1.executeQuery("SELECT COALESCE(COUNT(*), 0) DATECOUNT FROM G_HOLIDAY WHERE fdate between '" + fromdate + "'::DATE and '" + toDate + "'::DATE");

            while (rs1.next()) {
                holiDiff = holiDiff + rs1.getInt("DATECOUNT");
            }
            Calendar c1 = Calendar.getInstance();
            c1.setTime(d1);

            Calendar c2 = Calendar.getInstance();
            c2.setTime(d2);

            int sundays = 0;
            int saturday = 0;
            while (!c1.after(c2)) {
                if (c1.get(Calendar.DAY_OF_WEEK) == 7 && (c1.get(Calendar.WEEK_OF_MONTH) == 2 || c1.get(Calendar.WEEK_OF_MONTH) == 4)) {
                    saturday++;
                }
//                if (c1.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
//                    saturday++;
//                }

                if (c1.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                    sundays++;
                }

                c1.add(Calendar.DATE, 1);
            }
            if (tolid.equals("HDL")) {

                daysdiff = 0.5;
            } else if (tolid.equals("CL")) {

                daysdiff = dateDiff - (saturday + sundays + holiDiff);

            } else {
                daysdiff = dateDiff;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
            DataBaseFunctions.closeSqlObjects(st, st1);
            DataBaseFunctions.closeSqlObjects(rs, rs1);
        }
        return daysdiff;
    }

    @Override
    public void viewPDFfunc(Document document, Leave leave, String empid, String gender, String gender1
    ) {
        Chunk c1 = null;
        try {
            String toBeCapped = leave.getSltleaveType().toLowerCase();
            String[] arr = toBeCapped.split(" ");
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < arr.length; i++) {
                sb.append(Character.toUpperCase(arr[i].charAt(0)))
                        .append(arr[i].substring(1)).append(" ");
            }
            String leaveType = sb.toString();
            Font f1 = new Font();
            f1.setSize(10);
            f1.setFamily("Times New Roman");

            PdfPTable table = null;
            table = new PdfPTable(4);
            table.setWidths(new int[]{2, 3, 3, 3});
            table.setWidthPercentage(100);
            PdfPCell cell = null;
            cell = new PdfPCell(new Phrase("Government of Odisha", new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.BOLD)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(leave.getOffname(), new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("* * * * *", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(20);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("OFFICE ORDER", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.UNDERLINE)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(" Dated " + leave.getTxtOrdDate(), new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.BOLD)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(20);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            Phrase phrs = new Phrase();
            if (leave.getOffname() != null && !leave.getOffname().equals("")) {
                c1 = new Chunk("Order No. " + leave.getTxtOrdNo() + " " + toTitleCase(leave.getApplicantName()) + "," + toTitleCase(leave.getOffname()) + " is granted ", f1);
            } else {
                c1 = new Chunk("Order No. " + leave.getTxtOrdNo() + " " + toTitleCase(leave.getApplicantName()) + " is granted ", f1);
            }
            Chunk c2 = new Chunk(leaveType, new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD));
            //Chunk c3 = new Chunk("for a period of " + leave.getLeaveBalance() + " day with effect from ", f1);
            Chunk c3 = new Chunk("for a period of " + calculateDateDiff(leave.getTxtApproveFrom(), leave.getTxtApproveTo(), leave.getEmpId(), leave.getTollid()) + " day with effect from ", f1);
            Chunk c4 = new Chunk(leave.getTxtApproveFrom(), f1);
            Chunk c5 = new Chunk(" to ", f1);
            // Chunk c6 = new Chunk(leave.getTxtApproveTo() + "(" + leave.getSltPartofday() + ")" + " under Odisha Leave Rule-1966 ", f1);
            Chunk c6 = new Chunk(leave.getTxtApproveTo() + " under Odisha Leave Rule-1966 ", f1);
            Chunk c7 = new Chunk(".", f1);
            //Chunk c8 = new Chunk("The above period of leave will be counted towards his increment.", f1);
            phrs.add(c1);
            phrs.add(c2);
            phrs.add(c3);
            phrs.add(c4);
            phrs.add(c5);
            phrs.add(c6);
            phrs.add(c7);
            //phrs.add(c8);
            cell = new PdfPCell(phrs);
            cell.setColspan(4);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

//            cell = new PdfPCell(new Phrase("No. " + leave.getTxtOrdNo() + " " + leave.getApplicantName() + "," + leave.getOffname().toLowerCase() + " is granted " + p.add(leaveName) + " for the period from " + leave.getTxtApproveFrom() + " to " + leave.getTxtApproveTo() + ".", new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL)));
//            cell.setColspan(4);
//            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
//            cell.setBorder(Rectangle.NO_BORDER);
//            table.addCell(cell);
//            cell = new PdfPCell(new Phrase(" to " + leave.getTxtApproveTo() + ".", new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL)));
//            cell.setColspan(4);
//            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
//            cell.setBorder(Rectangle.NO_BORDER);
//            table.addCell(cell);
            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            cell = new PdfPCell(new Phrase("Certified that had " + toTitleCase(leave.getApplicantName()) + ", not gone on leave, " + gender + " would have continued in the said post.", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);

            cell = new PdfPCell(new Phrase("The above period of leave will be counted towards " + gender1 + " increment.", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(40);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(leave.getApprovedPost(), new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(30);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Memo No._____________________ ,/ Dt._______________________", new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Copy forwarded to Accounts  II Branch (in duplicate) for information and necessary action.", new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(40);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(leave.getApprovedPost(), new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Memo No._____________________ ,/ Dt._______________________", new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Copy forwarded to the person concerned / Guard file for information.", new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(40);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(leave.getApprovedPost(), new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            document.add(table);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    @Override
    public void viewBlankOrdnoDatePDFfunc(Document document, Leave leave, String empid, String gender, String gender1) {
        try {
            String toBeCapped = leave.getSltleaveType().toLowerCase();
            String[] arr = toBeCapped.split(" ");
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < arr.length; i++) {
                sb.append(Character.toUpperCase(arr[i].charAt(0)))
                        .append(arr[i].substring(1)).append(" ");
            }
            String leaveType = sb.toString();
            Font f1 = new Font();
            f1.setSize(10);
            f1.setFamily("Times New Roman");

            PdfPTable table = null;
            table = new PdfPTable(4);
            table.setWidths(new int[]{2, 3, 3, 3});
            table.setWidthPercentage(100);
            PdfPCell cell = null;
            cell = new PdfPCell(new Phrase("Government of Odisha", new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.BOLD)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(leave.getOffname(), new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("* * * * *", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(20);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("OFFICE ORDER", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.UNDERLINE)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(" Dated " + leave.getTxtOrdDate(), new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.BOLD)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(20);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            Phrase phrs = new Phrase();
            Chunk c1 = new Chunk("Order No._____________ /Dt.______________ " + toTitleCase(leave.getApplicantName()) + "," + toTitleCase(leave.getOffname()) + " is granted ", f1);
            Chunk c2 = new Chunk(leaveType, new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD));
            //Chunk c3 = new Chunk("for a period of " + leave.getLeaveBalance() + " day with effect from ", f1);
            //Chunk c3 = new Chunk("for a period of " + calculateDateDiff(new SimpleDateFormat("yyyy-MM-dd").format(new SimpleDateFormat("dd-MMM-yyyy").parse(leave.getTxtApproveFrom())), new SimpleDateFormat("yyyy-MM-dd").format(new SimpleDateFormat("dd-MMM-yyyy").parse(leave.getTxtApproveTo())), leave.getEmpId(), leave.getTollid()) + " day with effect from ", f1);
            Chunk c3 = new Chunk("for a period of " + calculateDateDiff(leave.getTxtApproveFrom(), leave.getTxtApproveTo(), leave.getEmpId(), leave.getTollid()) + " day with effect from ", f1);
            Chunk c4 = new Chunk(leave.getTxtApproveFrom(), f1);
            Chunk c5 = new Chunk(" to ", f1);
            // Chunk c6 = new Chunk(leave.getTxtApproveTo() + "(" + leave.getSltPartofday() + ")" + " under Odisha Leave Rule-1966 ", f1);
            Chunk c6 = new Chunk(leave.getTxtApproveTo() + " under Odisha Leave Rule-1966 ", f1);
            Chunk c7 = new Chunk(".", f1);
            //Chunk c8 = new Chunk("The above period of leave will be counted towards his increment.", f1);
            phrs.add(c1);
            phrs.add(c2);
            phrs.add(c3);
            phrs.add(c4);
            phrs.add(c5);
            phrs.add(c6);
            phrs.add(c7);
            //phrs.add(c8);
            cell = new PdfPCell(phrs);
            cell.setColspan(4);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

//            cell = new PdfPCell(new Phrase("No. " + leave.getTxtOrdNo() + " " + leave.getApplicantName() + "," + leave.getOffname().toLowerCase() + " is granted " + p.add(leaveName) + " for the period from " + leave.getTxtApproveFrom() + " to " + leave.getTxtApproveTo() + ".", new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL)));
//            cell.setColspan(4);
//            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
//            cell.setBorder(Rectangle.NO_BORDER);
//            table.addCell(cell);
//            cell = new PdfPCell(new Phrase(" to " + leave.getTxtApproveTo() + ".", new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL)));
//            cell.setColspan(4);
//            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
//            cell.setBorder(Rectangle.NO_BORDER);
//            table.addCell(cell);
            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            cell = new PdfPCell(new Phrase("Certified that had " + toTitleCase(leave.getApplicantName()) + ", not gone on leave, " + gender + " would have continued in the said post.", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);

            cell = new PdfPCell(new Phrase("The above period of leave will be counted towards " + gender1 + " increment.", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(40);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(leave.getApprovedPost(), new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(30);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Memo No._____________________ ,/ Dt._______________________", new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Copy forwarded to Accounts  II Branch (in duplicate) for information and necessary action.", new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(40);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(leave.getApprovedPost(), new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Memo No._____________________ ,/ Dt._______________________", new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Copy forwarded to the person concerned / Guard file for information.", new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(40);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(leave.getApprovedPost(), new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            document.add(table);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    public static String toTitleCase(String givenString) {
        String toBeCapped = givenString.trim().toLowerCase();
        String[] arr = toBeCapped.split("  ");
        String[] arr1 = toBeCapped.split(" ");
        StringBuffer sb = new StringBuffer();
        if (arr.length > 0) {
            for (int i = 0; i < arr.length; i++) {
                sb.append(Character.toUpperCase(arr[i].charAt(0)))
                        .append(arr[i].substring(1)).append(" ");
            }
        } else {
            for (int i = 0; i < arr1.length; i++) {
                sb.append(Character.toUpperCase(arr1[i].charAt(0)))
                        .append(arr1[i].substring(1)).append(" ");
            }
        }
        return sb.toString();
    }

    @Override
    public void viewAllowedPDFfunc(Document document, Leave leave, String empid, String gender, String gender1) {

        try {
            String toBeCapped = leave.getSltleaveType().toLowerCase().trim();
            String[] arr = toBeCapped.split(" ");
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < arr.length; i++) {
                sb.append(Character.toUpperCase(arr[i].charAt(0)))
                        .append(arr[i].substring(1)).append(" ");
            }
            String leaveType = sb.toString();
            Font f1 = new Font();
            f1.setSize(10);
            f1.setFamily("Times New Roman");

            PdfPTable table = null;
            table = new PdfPTable(4);
            table.setWidths(new int[]{2, 3, 3, 3});
            table.setWidthPercentage(100);
            PdfPCell cell = null;
            cell = new PdfPCell(new Phrase("Government of Odisha", new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.BOLD)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(leave.getAppOffname(), new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("* * * * *", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(20);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("OFFICE ORDER", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.UNDERLINE)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(20);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            Phrase phrs = new Phrase();
            Chunk c1 = new Chunk(toTitleCase(leave.getApplicantName()) + "," + toTitleCase(leave.getAppOffname()) + " is granted ", f1);
            Chunk c2 = new Chunk(leaveType, new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD));
            // Chunk c3 = new Chunk("for a period of " + leave.getLeaveBalance() + " day with effect from ", f1);
            //Chunk c3 = new Chunk("for a period of " + calculateDateDiff(new SimpleDateFormat("yyyy-MM-dd").format(new SimpleDateFormat("dd-MMM-yyyy").parse(leave.getTxtApproveFrom())), new SimpleDateFormat("yyyy-MM-dd").format(new SimpleDateFormat("dd-MMM-yyyy").parse(leave.getTxtApproveTo())), leave.getEmpId(), leave.getTollid()) + " day with effect from ", f1);
            Chunk c3 = new Chunk("for a period of " + calculateDateDiff(leave.getTxtApproveFrom(), leave.getTxtApproveTo(), leave.getEmpId(), leave.getTollid()) + " day with effect from ", f1);
            Chunk c4 = new Chunk(leave.getTxtApproveFrom(), f1);
            Chunk c5 = new Chunk(" to ", f1);
            Chunk c6 = new Chunk(leave.getTxtApproveTo() + " under Odisha Leave Rule-1966 ", f1);
            Chunk c7 = new Chunk(".", f1);
            phrs.add(c1);
            phrs.add(c2);
            phrs.add(c3);
            phrs.add(c4);
            phrs.add(c5);
            phrs.add(c6);
            phrs.add(c7);
            cell = new PdfPCell(phrs);
            cell.setColspan(4);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

//            cell = new PdfPCell(new Phrase("No. " + leave.getTxtOrdNo() + " " + leave.getApplicantName() + "," + leave.getOffname().toLowerCase() + " is granted " + p.add(leaveName) + " for the period from " + leave.getTxtApproveFrom() + " to " + leave.getTxtApproveTo() + ".", new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL)));
//            cell.setColspan(4);
//            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
//            cell.setBorder(Rectangle.NO_BORDER);
//            table.addCell(cell);
//            cell = new PdfPCell(new Phrase(" to " + leave.getTxtApproveTo() + ".", new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL)));
//            cell.setColspan(4);
//            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
//            cell.setBorder(Rectangle.NO_BORDER);
//            table.addCell(cell);
            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            cell = new PdfPCell(new Phrase("Certified that had " + toTitleCase(leave.getApplicantName()) + ", not gone on leave, " + gender + " would have continued in the said post.", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);

            cell = new PdfPCell(new Phrase("The above period of leave will be counted towards " + gender1 + " increment.", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(40);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(leave.getApprovedPost(), new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(30);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Memo No._____________________ ,/ Dt._______________________", new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Copy forwarded to Accounts  Branch (in duplicate) for information and necessary action.", new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(40);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(leave.getApprovedPost(), new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Memo No._____________________ ,/ Dt._______________________", new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Copy forwarded to the person concerned / Guard file for information.", new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(40);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(leave.getApprovedPost(), new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            document.add(table);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    @Override
    public LeaveEntrytakenBean getEntryTaken(String empId) {
        LeaveEntrytakenBean leb = null;
        Connection con = null;
        ResultSet rs = null;
        Statement st = null;
        try {
            con = dataSource.getConnection();
            st = con.createStatement();

            rs = st.executeQuery("SELECT CUR_OFF_CODE,DEPARTMENT_CODE,CUR_SPC FROM(SELECT CUR_OFF_CODE,CUR_SPC FROM EMP_MAST WHERE EMP_ID='" + empId + "')EMPMAST  "
                    + "INNER JOIN G_OFFICE ON G_OFFICE.OFF_CODE=EMPMAST.CUR_OFF_CODE");
            while (rs.next()) {
                leb = new LeaveEntrytakenBean();
                leb.setDeptCode(rs.getString("DEPARTMENT_CODE"));
                leb.setOffcode(rs.getString("CUR_OFF_CODE"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return leb;
    }

    @Override
    public LeaveSancBean getLeaveSancInfo(String taskId) {
        LeaveSancBean lsb = null;
        Connection con = null;
        ResultSet rs = null;
        Statement st = null;
        try {
            con = dataSource.getConnection();
            st = con.createStatement();

            rs = st.executeQuery("SELECT EMP_ID,TOL_ID,APPROVE_FDATE,APPROVE_TDATE,SUFFIX_FROM,SUFFIX_DATE,PREFIX_DATE,PREFIX_TO,DEPARTMENT_CODE,CUR_OFF_CODE,SUBMIT_AUTH,JOIN_TIME_FROM FROM "
                    + "(SELECT HWLEAVE.EMP_ID,TOL_ID,APPROVE_FDATE,APPROVE_TDATE,SUFFIX_FROM,SUFFIX_DATE,PREFIX_DATE,PREFIX_TO,CUR_OFF_CODE,SUBMIT_AUTH,JOIN_TIME_FROM FROM(SELECT EMP_ID,TOL_ID, "
                    + "APPROVE_FDATE,APPROVE_TDATE,SUFFIX_FROM,SUFFIX_DATE,PREFIX_DATE,PREFIX_TO,SUBMITEDTO,SUBMIT_AUTH,JOIN_TIME_FROM FROM HW_EMP_LEAVE WHERE TASK_ID='" + taskId + "')HWLEAVE INNER JOIN   "
                    + "EMP_MAST ON EMP_MAST.EMP_ID=HWLEAVE.SUBMITEDTO)TAB INNER JOIN G_OFFICE ON G_OFFICE.OFF_CODE=TAB.CUR_OFF_CODE");
            while (rs.next()) {
                lsb = new LeaveSancBean();
                lsb.setDeptCode(rs.getString("DEPARTMENT_CODE"));
                lsb.setOffCode(rs.getString("CUR_OFF_CODE"));
                lsb.setAuthCode(rs.getString("SUBMIT_AUTH"));
                lsb.setInitiatedEmpId(rs.getString("EMP_ID"));
                lsb.setTolId(rs.getString("TOL_ID"));
                lsb.setFromDate(rs.getString("APPROVE_FDATE"));
                lsb.setToDate(rs.getString("APPROVE_TDATE"));
                lsb.setPrefixFrom(rs.getString("PREFIX_DATE"));
                lsb.setPrefixTo(rs.getString("PREFIX_TO"));
                lsb.setSuffixeFrom(rs.getString("SUFFIX_FROM"));
                lsb.setSuffixeTo(rs.getString("SUFFIX_DATE"));
                lsb.setJoinDate(rs.getString("JOIN_TIME_FROM"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(st);
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(con);

        }
        return lsb;
    }

    @Override
    public boolean maxPeriodCount(Leave leaveForm) {
        Connection con = null;
        Statement st = null;
        Statement stmt = null;
        ResultSet rs = null;
        ResultSet rset = null;
        int dayCnt = 0;
        int maxPeriod = 0;
        boolean leaveAvail = false;
        try {
            con = dataSource.getConnection();
            st = con.createStatement();
            stmt = con.createStatement();
            rs = st.executeQuery("SELECT  DATE_PART('day', '" + leaveForm.getTxtperiodTo() + "'::timestamp - '" + leaveForm.getTxtperiodFrom() + "'::timestamp)+1 DAY_COUNT");

            if (rs.next()) {
                dayCnt = rs.getInt("DAY_COUNT");
            }

            rset = stmt.executeQuery("SELECT MAX( MAX_PERIOD)MAX_PERIOD FROM G_LEAVE WHERE TOL_ID='" + leaveForm.getSltleaveType() + "'");

            if (rset.next()) {
                maxPeriod = rset.getInt("MAX_PERIOD");
            }
            if (!leaveForm.getSltleaveType().equals("HDL")) {

                if (dayCnt > maxPeriod) {
                    leaveAvail = false;
                } else {
                    leaveAvail = true;
                }
            } else {
                leaveAvail = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(st);
            DataBaseFunctions.closeSqlObjects(stmt);
            DataBaseFunctions.closeSqlObjects(con);

        }
        return leaveAvail;
    }

    @Override
    public boolean ifMaxSurviveChild(String tolId, String empId) {
        Connection con = null;
        Statement st = null;
        Statement stmt = null;
        ResultSet rset = null;
        int maxChild = 0;
        boolean ret = false;
        try {
            con = dataSource.getConnection();
            stmt = con.createStatement();
            rset = stmt.executeQuery("SELECT MAX(NO_OF_CHILD)MAX_CHILD FROM HW_EMP_LEAVE WHERE TOL_ID='" + tolId + "' AND EMP_ID='" + empId + "'");
            if (rset.next()) {
                maxChild = rset.getInt("MAX_CHILD");
            }
            if (maxChild == 2) {
                ret = false;
            } else {
                ret = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return ret;
    }

    @Override
    public double maxLeavePeriodCount(String tolId) {
        Connection con = null;
        Statement st = null;
        Statement stmt = null;
        ResultSet rset = null;
        double maxPeriod = 0;
        try {
            con = dataSource.getConnection();
            stmt = con.createStatement();
            if (tolId == "HDL") {
                maxPeriod = 0.5;
            } else {
                rset = stmt.executeQuery("SELECT MAX( MAX_PERIOD)MAX_PERIOD FROM G_LEAVE WHERE TOL_ID='" + tolId + "'");
                if (rset.next()) {
                    maxPeriod = rset.getInt("MAX_PERIOD");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return maxPeriod;
    }

    @Override
    public LeaveWsBean[] getEmployeeLeave(String empid, String inputdate) {
        List<LeaveWsBean> lvlist = new ArrayList<>();
        String SQL = "SELECT LSOT, TOL_ID, FDATE, TDATE, SUFFIX_FROM, SUFFIX_DATE, PREFIX_DATE, PREFIX_TO, S_YEAR, T_YEAR, S_DAYS, ORDNO, ORDDT, NOTE FROM "
                + "(SELECT NOTE,NOT_ID,DOE, ORDNO, ORDDT FROM EMP_NOTIFICATION WHERE EMP_ID=? AND NOT_TYPE='LEAVE' AND DOE > ?)EMP_NOTIFICATION "
                + "INNER JOIN EMP_LEAVE ON EMP_NOTIFICATION.NOT_ID=EMP_LEAVE.NOT_ID "
                + "INNER JOIN G_LSOT ON EMP_LEAVE.LSOT_ID=G_LSOT.LSOT_ID";
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet result = null;
        try {
            conn = dataSource.getConnection();
            statement = conn.prepareStatement(SQL);
            statement.setString(1, empid);
            statement.setDate(2, new java.sql.Date(CommonFunctions.getDateFromString(inputdate, "yyyy").getTime()));
            result = statement.executeQuery();
            while (result.next()) {
                LeaveWsBean leave = new LeaveWsBean();
                leave.setLsot(result.getString("LSOT"));
                leave.setTol(result.getString("TOL_ID"));
                leave.setFdate(result.getString("FDATE"));
                leave.setTdate(result.getString("TDATE"));
                leave.setSuffixFrom(result.getString("SUFFIX_FROM"));
                leave.setSuffixDate(result.getString("SUFFIX_DATE"));
                leave.setPrefixDate(result.getString("PREFIX_DATE"));
                leave.setPrefixTo(result.getString("PREFIX_TO"));
                leave.setSyear(result.getString("S_YEAR"));
                leave.setTyear(result.getString("T_YEAR"));
                leave.setSdays(result.getString("S_DAYS"));
                leave.setOrdno(result.getString("ORDNO"));
                leave.setOrddate(result.getString("ORDDT"));
                leave.setNote(result.getString("NOTE"));
                lvlist.add(leave);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (result != null) {
                    result.close();
                    result = null;
                }
                if (statement != null) {
                    statement.close();
                    statement = null;
                }
                if (conn != null) {
                    conn.close();
                    conn = null;
                }
            } catch (SQLException e) {

            }
        }
        LeaveWsBean lvarray[] = lvlist.toArray(new LeaveWsBean[lvlist.size()]);
        return lvarray;
    }

    @Override
    public void saveJoinData(Leave leave) {
        Connection conn = null;
        PreparedStatement stmt = null;
        PreparedStatement pstmt = null;
        PreparedStatement pstt = null;
        PreparedStatement pstmtt = null;
        Calendar cal = Calendar.getInstance();
        Timestamp timestamp = new Timestamp(cal.getTimeInMillis());
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
        try {
            conn = dataSource.getConnection();
            stmt = conn.prepareStatement("UPDATE HW_EMP_LEAVE SET JOIN_TIME_FROM=?,JOINING_NOTE=? WHERE TASK_ID=?");
            //stmt.setString(1, leave.getJoiningDate());
            Date joinDate = formatter.parse(leave.getJoiningDate());
            java.sql.Date joiningDate = new java.sql.Date(joinDate.getTime());
            stmt.setDate(1, joiningDate);
            stmt.setString(2, leave.getJoiningNote());
            stmt.setInt(3, Integer.parseInt(leave.getHidTaskId()));

            stmt.executeUpdate();
            pstmt = conn.prepareStatement("UPDATE TASK_MASTER SET STATUS_ID=? WHERE TASK_ID=?");
            if (leave.getStatusId().equals("1")) {
                pstmt.setInt(1, 5);
            }
            if (leave.getStatusId().equals("5")) {
                pstmt.setInt(1, 41);
            }

            pstmt.setInt(2, Integer.parseInt(leave.getHidTaskId()));
            pstmt.executeUpdate();
            if (leave.getStatusId().equals("5")) {
                pstt = conn.prepareStatement("UPDATE TASK_MASTER SET PENDING_AT=?,PENDING_SPC=? WHERE TASK_ID=?");
                pstt.setString(1, leave.getHidAuthEmpId());
                pstt.setString(2, leave.getHidSpcAuthCode());
                pstt.setInt(3, Integer.parseInt(leave.getHidTaskId()));
                pstt.executeUpdate();

                // if ((leave.getHidAuthEmpId() != null && !leave.getHidAuthEmpId().equals("")) && (leave.getHidSpcAuthCode() != null && !lfb.getHidSpcAuthCode().equals(""))) {
//                mcode = CommonFunctions.getMaxCodeInteger("WORKFLOW_LOG", "LOG_ID", con);
//                pstmt = con.prepareStatement("INSERT INTO WORKFLOW_LOG(LOG_ID,TASK_ID, TASK_ACTION_DATE, ACTION_TAKEN_BY, SPC_ONTIME, TASK_STATUS_ID, NOTE,FORWARD_TO,FORWARDED_SPC) Values (?,?,?,?,?,?,?,?,?)");
//                pstmt.setInt(1, mcode);
//                pstmt.setInt(2, Integer.parseInt(leave.getHidTaskId()));
//                pstmt.setTimestamp(3, timestamp);
//                pstmt.setString(4, leave.getHidempId());
//                pstmt.setString(5, leave.getHidSpcCode());
//                pstmt.setInt(6, Integer.parseInt(leave.getSltActionType()));
//                pstmt.setString(7, leave.getTxtauthnote());
//                pstmt.setString(8, leave.getHidAuthEmpId());
//                pstmt.setString(9, leave.getHidSpcAuthCode());
//                pstmt.executeUpdate();
            }
            //int mcode = CommonFunctions.getMaxCodeInteger("TASK_MASTER", "TASK_ID", conn);
            if (leave.getAttachmentid() != null) {
                for (int i = 0; i < leave.getAttachmentid().length; i++) {
                    pstmtt = conn.prepareStatement("UPDATE HW_ATTACHMENTS set TASK_ID=?,ATTACH_FLAG=? WHERE ATT_ID=" + leave.getAttachmentid()[i]);
                    pstmtt.setInt(1, Integer.parseInt(leave.getHidTaskId()));
                    pstmtt.setString(2, "N");
                    pstmtt.executeUpdate();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(conn);
        }
    }

//    public void clDayCount(String fromDate, String toDate) {
//        String holiday = "";
//        Connection con = null;
//        Statement st = null;
//        ResultSet rs = null;
//        int dayCnt = 0;
//        try {
//            con = dataSource.getConnection();
//            st = con.createStatement();
//            rs = st.executeQuery("SELECT  DATE_PART('day', '" + fromDate + "'::timestamp - '" + toDate + "'::timestamp)+1 DAY_COUNT");
//            if (rs.next()) {
//                dayCnt = rs.getInt("DAY_COUNT");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
    public static boolean ifWorkingDay(String empId, String frmDate, Connection con) throws Exception {
        boolean status = false;
        Statement st = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        Statement st1 = null;
        String cId = "";
        String curddmmmyyyy = "";
        String queryForSPC = "";
        String SPC = "";
        String year = frmDate.substring(frmDate.length() - 4, frmDate.length());
        String month = frmDate.substring(3, 6).trim();
        int mon = getMonthAsInteger(month);
        String query = "";
        try {
            st = con.createStatement();
            st1 = con.createStatement();
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
            java.util.Date dt = new java.util.Date();
            curddmmmyyyy = dateFormat.format(dt);

            queryForSPC = "SELECT CID FROM G_CAL_POST WHERE SPC='" + SPC + "'";

            rs = st.executeQuery(queryForSPC);
            while (rs.next()) {
                cId = rs.getString("CID");
            }
            query = "SELECT G_HOLIDAY.HID,G_HOLIDAY.FDATE, G_HOLIDAY.TDATE,(G_HOLIDAY.TDATE-G_HOLIDAY.FDATE)+1 LENGTH,G_HOLIDAY.HTYPE  FROM G_CAL_HOLIDAYS INNER JOIN G_HOLIDAY ON G_CAL_HOLIDAYS.HID=G_HOLIDAY.HID AND G_CAL_HOLIDAYS.CYEAR='" + year + "' AND TO_char(G_HOLIDAY.FDATE,'MM')=" + mon + " AND G_CAL_HOLIDAYS.CID='" + cId + "'  AND (G_HOLIDAY.HTYPE='G' OR (G_HOLIDAY.HID IN (SELECT HID FROM EMP_CALENDAR WHERE EMP_ID='" + empId + "' AND CID='" + cId + "'))) WHERE TO_DATE('" + frmDate + "') >= FDATE AND TO_DATE('" + frmDate + "')<=TDATE";

            Calendar prefixFromdate = null;
            Calendar c = Calendar.getInstance();
            prefixFromdate = Calendar.getInstance();
            Date d = new Date(frmDate);
            prefixFromdate.setTime(d);
            int dayofWeek = prefixFromdate.get(prefixFromdate.DAY_OF_WEEK);
            c.setTime(d);
            c.set(Calendar.WEEK_OF_MONTH, 2);
            c.set(c.DAY_OF_WEEK, c.SATURDAY);
            if (d.equals(c.getTime())) {

                status = false;
            } else if (dayofWeek == 1) {

                status = false;
            } else {

                rs1 = st1.executeQuery(query);

                if (rs1.next()) {

                    status = false;
                } else {
                    status = true;
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
        }
        return status;
    }

    public static int getMonthAsInteger(String month) {
        int mon = 0;
        if (month.equalsIgnoreCase("JAN")) {
            mon = 1;
        }
        if (month.equalsIgnoreCase("FEB")) {
            mon = 2;
        }
        if (month.equalsIgnoreCase("MAR")) {
            mon = 3;
        }
        if (month.equalsIgnoreCase("APR")) {
            mon = 4;
        }
        if (month.equalsIgnoreCase("MAY")) {
            mon = 5;
        }
        if (month.equalsIgnoreCase("JUN")) {
            mon = 6;
        }
        if (month.equalsIgnoreCase("JUL")) {
            mon = 7;
        }
        if (month.equalsIgnoreCase("AUG")) {
            mon = 8;
        }
        if (month.equalsIgnoreCase("SEP")) {
            mon = 9;
        }
        if (month.equalsIgnoreCase("OCT")) {
            mon = 10;
        }
        if (month.equalsIgnoreCase("NOV")) {
            mon = 11;
        }
        if (month.equalsIgnoreCase("DEC")) {
            mon = 12;
        }
        return mon;
    }

    @Override
    public List getOffWiseEmpLeaveList(String offCode, String frdate, String todate) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        SimpleDateFormat inSDF = new SimpleDateFormat("dd-mm-yyyy");
        SimpleDateFormat outSDF = new SimpleDateFormat("yyyy-mm-dd");
        ArrayList empList = new ArrayList();
        Leave leave = null;
        String outFrDate = "";
        String outToDate = "";
        Statement st = null;
        try {
            con = dataSource.getConnection();
            st = con.createStatement();
            Date date1 = inSDF.parse(frdate);
            outFrDate = outSDF.format(date1);
            Date date2 = inSDF.parse(todate);
            outToDate = outSDF.format(date2);
            String empListQry = "select hwempleave.emp_id,INITIALS, F_NAME, M_NAME, L_NAME,POST,frdate,todate from(SELECT TO_CHAR(fdate,'DD-MM-YYYY')frdate,TO_CHAR(tdate,'DD-MM-YYYY')todate,emp_id FROM  hw_emp_leave WHERE  fdate between '" + outFrDate + "' and '" + outToDate + "') hwempleave  "
                    + "inner join emp_mast on emp_mast.emp_id=hwempleave.emp_id  LEFT OUTER JOIN g_spc GS ON emp_mast.cur_spc=GS.spc LEFT OUTER JOIN g_post GP ON GP.post_code= GS.gpc where cur_off_code='" + offCode + "' order by frdate,F_NAME";

            pstmt = con.prepareStatement(empListQry);
            rs = st.executeQuery(empListQry);
            while (rs.next()) {
                leave = new Leave();
                leave.setEmpId(rs.getString("emp_id"));
                leave.setIntitals(rs.getString("INITIALS"));
                leave.setFname(rs.getString("F_NAME"));
                leave.setMname(rs.getString("M_NAME"));
                leave.setLname(rs.getString("L_NAME"));
                leave.setPost(rs.getString("post"));
                leave.setLeavePeriodFrom(rs.getString("frdate"));
                leave.setLeavePeriodTo(rs.getString("todate"));
                empList.add(leave);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return empList;
    }

    public static String getLeaveBalance(String empId, String tolId, int daysdiff, Connection con) {
        // Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String leaveBalance = "";
        String empListQry = "";
        try {
            if (tolId.equals("HDL")) {
                tolId = "CL";
                empListQry = "SELECT TOL_ID,BALANCE,AVAILABLE,(BALANCE-AVAILABLE)TAKEN FROM EMP_LEAVE_BALANCE WHERE EMP_ID=? AND TOL_ID=? ";
            } else {
                empListQry = "SELECT TOL_ID,BALANCE,AVAILABLE,(BALANCE-AVAILABLE)TAKEN FROM EMP_LEAVE_BALANCE WHERE EMP_ID=? AND TOL_ID=? ";
            }

            pstmt = con.prepareStatement(empListQry);
            pstmt.setString(1, empId);
            pstmt.setString(2, tolId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                leaveBalance = "(" + rs.getString("AVAILABLE") + ")" + rs.getString("BALANCE");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
        }

        return leaveBalance;
    }

    @Override
    public ArrayList getAppliedLeaveEmpList(String offcode) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        ArrayList emplist = new ArrayList();
        Leave leave = null;
        try {
            con = this.dataSource.getConnection();
            String curDate = "";
            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
            curDate = dateFormat.format(cal.getTime());
            String sql = "select initiated_on,hw_emp_leave.emp_id,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME, L_NAME], ' ') EMPNAME,task_master.status_id status_id,status_name,tol,fdate,tdate,hw_emp_leave.tol_id, "
                    + "DATE_PART('day', tdate::timestamp - fdate::timestamp)+1 daydiff,post,spc,task_master.task_id from hw_emp_leave "
                    + "inner join emp_mast on hw_emp_leave.emp_id=emp_mast.emp_id "
                    + "inner join (SELECT DISTINCT TOL_ID,TOL FROM G_LEAVE WHERE IF_NOT_APP != 'Y' OR IF_NOT_APP IS NULL)g_leave on hw_emp_leave.tol_id=g_leave.tol_id "
                    + "inner join task_master on hw_emp_leave.task_id=task_master.task_id inner join g_process_status on g_process_status.status_id=task_master.status_id "
                    + "left outer join g_spc on emp_mast.cur_spc=g_spc.spc inner join g_post on g_post.post_code=g_spc.gpc  "
                    + "where cur_off_code=? and initiated_on>=?::DATE and not_type='LEAVE' and (task_master.status_id=4 OR task_master.status_id=1 OR task_master.status_id=3 OR task_master.status_id=5 OR task_master.status_id=41 )order by initiated_on desc";
            pst = con.prepareStatement(sql);
            pst.setString(1, offcode);
            pst.setString(2, curDate);
            rs = pst.executeQuery();
            while (rs.next()) {
                leave = new Leave();
                leave.setEmpId(rs.getString("emp_id"));
                leave.setApplicantName(rs.getString("EMPNAME"));
                leave.setHidSpcCode(rs.getString("spc"));
                leave.setTaskId(Integer.parseInt(rs.getString("task_id")));
                leave.setStatusId(rs.getString("status_id"));
                leave.setStatus(rs.getString("status_name"));
                leave.setPost(rs.getString("post"));
                leave.setSltleaveType(rs.getString("tol"));
                leave.setTxtperiodFrom(CommonFunctions.getFormattedOutputDate1(rs.getDate("fdate")));
                leave.setTxtperiodTo(CommonFunctions.getFormattedOutputDate1(rs.getDate("tdate")));
                if (!rs.getString("tol_id").equals("HDL")) {
                    leave.setDaysdiff(rs.getString("daydiff"));
                } else {
                    leave.setDaysdiff("0.5");
                }
                leave.setLeaveBalance(getLeaveBalance(rs.getString("emp_id"), rs.getString("tol_id"), rs.getInt("daydiff"), con));
                leave.setInitiatedOn(CommonFunctions.getFormattedOutputDate1(rs.getDate("initiated_on")));
                emplist.add(leave);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return emplist;
    }

    @Override
    public ArrayList getAbseneteeStmtList(String offCode, String fromDate, String toDate) {
        Connection con = null;
        ArrayList empabsenteelist = new ArrayList();
        Leave leave = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            con = this.dataSource.getConnection();
            pst = con.prepareStatement("select initiated_on,hw_emp_leave.emp_id,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME, L_NAME], ' ') EMPNAME,task_master.status_id status_id,status_name,tol,fdate,tdate,hw_emp_leave.tol_id, "
                    + "DATE_PART('day', tdate::timestamp - fdate::timestamp)+1 daydiff,post,spc,task_master.task_id from hw_emp_leave   "
                    + "inner join emp_mast on hw_emp_leave.emp_id=emp_mast.emp_id   "
                    + "inner join (SELECT DISTINCT TOL_ID,TOL FROM G_LEAVE WHERE IF_NOT_APP != 'Y' OR IF_NOT_APP IS NULL)g_leave on hw_emp_leave.tol_id=g_leave.tol_id   "
                    + "inner join task_master on hw_emp_leave.task_id=task_master.task_id inner join g_process_status on g_process_status.status_id=task_master.status_id  "
                    + "left outer join g_spc on emp_mast.cur_spc=g_spc.spc inner join g_post on g_post.post_code=g_spc.gpc   "
                    + "where cur_off_code=?  and not_type='LEAVE' and (task_master.status_id=4 OR task_master.status_id=1 OR task_master.status_id=3 OR task_master.status_id=5 OR task_master.status_id=41 ) and   "
                    + "(fdate>=?::DATE and tdate<= ?::DATE)  order by initiated_on desc");
            pst.setString(1, offCode);
            pst.setString(2, fromDate);
            pst.setString(3, toDate);
            rs = pst.executeQuery();
            while (rs.next()) {
                leave = new Leave();
                leave.setEmpId(rs.getString("emp_id"));
                leave.setApplicantName(rs.getString("EMPNAME"));
                leave.setHidSpcCode(rs.getString("spc"));
                leave.setTaskId(Integer.parseInt(rs.getString("task_id")));
                leave.setStatusId(rs.getString("status_id"));
                leave.setStatus(rs.getString("status_name"));
                leave.setPost(rs.getString("post"));
                leave.setSltleaveType(rs.getString("tol"));
                leave.setTxtperiodFrom(CommonFunctions.getFormattedOutputDate1(rs.getDate("fdate")));
                leave.setTxtperiodTo(CommonFunctions.getFormattedOutputDate1(rs.getDate("tdate")));
                leave.setDaysdiff(rs.getString("daydiff"));
                leave.setLeaveBalance(getLeaveBalance(rs.getString("emp_id"), rs.getString("tol_id"), rs.getInt("daydiff"), con));
                leave.setInitiatedOn(CommonFunctions.getFormattedOutputDate1(rs.getDate("initiated_on")));
                empabsenteelist.add(leave);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return empabsenteelist;
    }

    @Override
    public void cancelLeave(String taskid) {
        Connection con = null;
        PreparedStatement pst = null;
        try {
            con = this.dataSource.getConnection();
            pst = con.prepareStatement("DELETE FROM HW_EMP_LEAVE WHERE TASK_ID=?");
            pst.setInt(1, Integer.parseInt(taskid));
            pst.executeUpdate();
            pst = con.prepareStatement("DELETE FROM TASK_MASTER WHERE TASK_ID=?");
            pst.setInt(1, Integer.parseInt(taskid));
            pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    @Override
    public void saveFcmToken(String fcmtoken, String linkid, String serverkey) {
        Connection con = null;
        PreparedStatement pst = null;
        PreparedStatement pst1 = null;
        PreparedStatement pst2 = null;
        ResultSet rs = null;
        try {

            con = this.dataSource.getConnection();
            pst1 = con.prepareStatement("select link_id from fcm_master where link_id=?");
            pst1.setString(1, linkid);
            rs = pst1.executeQuery();
            if (rs.next()) {
                pst2 = con.prepareStatement("update fcm_master set fcm_token=? where link_id=?");
                pst2.setString(1, fcmtoken);
                pst2.setString(2, linkid);
                pst2.executeUpdate();
            } else {
                pst = con.prepareStatement("insert into fcm_master (link_id,fcm_token,legacy_key)values(?,?,?)");
                pst.setString(1, linkid);
                pst.setString(2, fcmtoken);
                pst.setString(3, serverkey);
                pst.executeUpdate();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(pst, pst1, pst2);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void deleteFcmToken(String linkid) {
        Connection con = null;
        PreparedStatement pst = null;
        try {
            con = this.dataSource.getConnection();
            pst = con.prepareStatement("delete from fcm_master where link_id=?");
            pst.setString(1, linkid);
            pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public String getFcmToken(String linkid) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        String fcmToken = "";
        try {
            con = this.dataSource.getConnection();
            pst = con.prepareStatement("select fcm_token from fcm_master where link_id=?");
            pst.setString(1, linkid);
            rs = pst.executeQuery();
            if (rs.next()) {
                fcmToken = rs.getString("fcm_token");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return fcmToken;
    }

    @Override
    public void pushFCMNotification(String userDeviceIdKey, String applicantId, String spcCode, String statusId) {
        try {
            // userDeviceIdKey = "c2kkaUXvq1E:APA91bENuFnB3MW1gHkh1nZZnRhwuC1JJ-9oi6synS8qemtCw96l2R1WV_M3G73Su3D5hJAc0v0Q02bm4I2X3Xke7ssmdrwJJCY3u4xjyUx-dItIgJmBOFWI3UR7qEhR53-BDU9UYrE2";
            String authKey = "AAAAhjJdqQ4:APA91bES6w7PkLsKT6Ct8oQ3DLsF_Y-pt2JbVexGktT5vCK-YlYlDMJZh8iGBLFOTi18bMdMM_6RK6VOq7rOGU4-jbwm38xqT3FEqFYwIvuhcvkgT7D1608KgAXVpKjybkW4xME-8YcM";   // You FCM AUTH key
            //String authKey = AUTH_KEY_FCM; // You FCM AUTH key
            String FMCurl = "https://fcm.googleapis.com/fcm/send";

            URL url = new URL(FMCurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "key=" + authKey);
            conn.setRequestProperty("Content-Type", "application/json");

            JSONObject json = new JSONObject();
            json.put("to", userDeviceIdKey.trim());
            JSONObject info = new JSONObject();
            info.put("title", "HRMS"); // Notification title
            info.put("body", "LEAVE IS APPLIED BY " + getNameWithPost(applicantId, spcCode, statusId)); // Notification body
            json.put("notification", info);

            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(json.toString());
            wr.flush();
            conn.getInputStream();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void pushApproveFCMNotification(String userDeviceIdKey, String authId, String authSpcCode, String statusId) {
        try {
            // userDeviceIdKey = "c2kkaUXvq1E:APA91bENuFnB3MW1gHkh1nZZnRhwuC1JJ-9oi6synS8qemtCw96l2R1WV_M3G73Su3D5hJAc0v0Q02bm4I2X3Xke7ssmdrwJJCY3u4xjyUx-dItIgJmBOFWI3UR7qEhR53-BDU9UYrE2";
            String authKey = "AAAAhjJdqQ4:APA91bES6w7PkLsKT6Ct8oQ3DLsF_Y-pt2JbVexGktT5vCK-YlYlDMJZh8iGBLFOTi18bMdMM_6RK6VOq7rOGU4-jbwm38xqT3FEqFYwIvuhcvkgT7D1608KgAXVpKjybkW4xME-8YcM";   // You FCM AUTH key
            //String authKey = AUTH_KEY_FCM; // You FCM AUTH key
            String FMCurl = "https://fcm.googleapis.com/fcm/send";

            URL url = new URL(FMCurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "key=" + authKey);
            conn.setRequestProperty("Content-Type", "application/json");

            JSONObject json = new JSONObject();
            json.put("to", userDeviceIdKey.trim());
            JSONObject info = new JSONObject();
            info.put("title", "HRMS"); // Notification title
            info.put("body", "LEAVE IS APPROVED BY " + getNameWithPost(authId, authSpcCode, statusId)); // Notification body
            json.put("notification", info);

            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(json.toString());
            wr.flush();
            conn.getInputStream();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public LeaveOswass getLeaveDataForOSWAS(String empid, int taskId) {

        LeaveOswass leaveForm = null;
        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = this.dataSource.getConnection();

            String sql = "SELECT HWEMP.EMP_ID,TOL_ID,FDATE,TDATE,PREFIX_DATE,PREFIX_TO,SUFFIX_FROM,SUFFIX_DATE,LEAVEID,submitedto,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') APPROVER_NAME,SPN,POST,department_name,applicant_note,task_action_date,TDATE + interval '1' day as EXTFDATE,APPROVE_FDATE,APPROVE_TDATE,DATE_PART('day', APPROVE_TDATE::timestamp - APPROVE_FDATE::timestamp)+1 daydiff,SUBMIT_AUTH,submitedto, HWEMP.TASK_ID,ADDRESS,PHONENO,ORD_NO,ORD_DATE,PENDING_SPC ,PENDING_AT,IF_HEADQUARTER,STATUS_ID,JOINING_NOTE,JOIN_TIME_FROM,issuing_auth_spc,part_of_day,"
                    + " approved_spc,task_status_id FROM"
                    + " (SELECT LEAVEID,FDATE,TDATE,APPROVE_FDATE,APPROVE_TDATE,SUFFIX_FROM,SUFFIX_DATE,PREFIX_DATE,PREFIX_TO,TOL_ID,EMP_ID,submitedto,SUBMIT_AUTH,TASK_ID,APPLICANT_NOTE,ADDRESS,PHONENO,ORD_NO,ORD_DATE,IF_HEADQUARTER,JOINING_NOTE,"
                    + " JOIN_TIME_FROM,issuing_auth_spc,part_of_day,approved_spc FROM HW_EMP_LEAVE WHERE EMP_ID=? AND TASK_ID=?)HWEMP"
                    + " INNER JOIN TASK_MASTER ON HWEMP.TASK_ID=TASK_MASTER.TASK_ID left outer join workflow_log on workflow_log.task_id=HWEMP.task_id"
                    + " INNER JOIN EMP_MAST ON HWEMP.submitedto=EMP_MAST.EMP_ID"
                    + " LEFT OUTER JOIN G_SPC ON EMP_MAST.CUR_SPC=G_SPC.SPC"
                    + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE"
                    + " LEFT OUTER JOIN G_DEPARTMENT ON G_SPC.dept_code=G_DEPARTMENT.department_code";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            pst.setInt(2, taskId);
            rs = pst.executeQuery();
            if (rs.next()) {
                leaveForm = new LeaveOswass();

                leaveForm.setEmpId(rs.getString("EMP_ID"));
                leaveForm.setTollid(rs.getString("TOL_ID"));
                if (rs.getString("FDATE") != null && !rs.getString("FDATE").equals("")) {
                    leaveForm.setTxtperiodFrom(CommonFunctions.getFormattedOutputDate3(rs.getDate("FDATE")));
                } else {
                    leaveForm.setTxtperiodFrom("");
                }
                if (rs.getString("TDATE") != null && !rs.getString("TDATE").equals("")) {
                    leaveForm.setTxtperiodTo(CommonFunctions.getFormattedOutputDate3(rs.getDate("TDATE")));
                } else {
                    leaveForm.setTxtperiodTo("");
                }
                if (rs.getString("PREFIX_DATE") != null && !rs.getString("PREFIX_DATE").equals("")) {
                    leaveForm.setTxtprefixFrom(CommonFunctions.getFormattedOutputDate3(rs.getDate("PREFIX_DATE")));
                } else {
                    leaveForm.setTxtprefixFrom("");
                }
                if (rs.getString("PREFIX_TO") != null && !rs.getString("PREFIX_TO").equals("")) {
                    leaveForm.setTxtprefixTo(CommonFunctions.getFormattedOutputDate3(rs.getDate("PREFIX_TO")));
                } else {
                    leaveForm.setTxtprefixTo("");
                }
                if (rs.getString("SUFFIX_FROM") != null && !rs.getString("SUFFIX_FROM").equals("")) {
                    leaveForm.setTxtsuffixFrom(CommonFunctions.getFormattedOutputDate3(rs.getDate("SUFFIX_FROM")));
                } else {
                    leaveForm.setTxtsuffixFrom("");
                }
                if (rs.getString("SUFFIX_DATE") != null && !rs.getString("SUFFIX_DATE").equals("")) {
                    leaveForm.setTxtsuffixTo(CommonFunctions.getFormattedOutputDate3(rs.getDate("SUFFIX_DATE")));
                } else {
                    leaveForm.setTxtsuffixTo("");
                }
                leaveForm.setLeaveId(rs.getString("LEAVEID"));

                if (rs.getString("task_action_date") != null && !rs.getString("task_action_date").equals("")) {
                    leaveForm.setTaskActionDate(CommonFunctions.getFormattedOutputDate3(rs.getDate("task_action_date")));
                } else {
                    leaveForm.setTaskActionDate("");
                }

                String approverName = rs.getString("APPROVER_NAME");
                if (rs.getString("POST") != null && !rs.getString("POST").equals("")) {
                    approverName = approverName + "," + rs.getString("POST");
                }
                if (rs.getString("department_name") != null && !rs.getString("department_name").equals("")) {
                    approverName = approverName + "," + rs.getString("department_name");
                }
                leaveForm.setSubmittedTo(approverName);
                if (rs.getString("APPLICANT_NOTE") != null && !rs.getString("APPLICANT_NOTE").equals("")) {
                    leaveForm.setTxtnote(rs.getString("APPLICANT_NOTE"));
                } else {
                    leaveForm.setTxtnote("");
                }
                leaveForm.setHidAuthEmpId(rs.getString("submitedto"));

                double noOfDays = calculateDateDiff(leaveForm.getTxtperiodFrom(), leaveForm.getTxtperiodTo(), empid, leaveForm.getTollid());
                leaveForm.setNoofDays(noOfDays + "");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return leaveForm;
    }

    @Override
    public String isOSWASUser(String empid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String oswaruser = "N";
        try {
            con = this.dataSource.getConnection();

            String sql = "select is_oswas_user from emp_mast where emp_id=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            rs = pst.executeQuery();
            if (rs.next()) {
                oswaruser = rs.getString("is_oswas_user");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return oswaruser;
    }

    @Override
    public String getGender(String empId) {
        String gender = "";
        String gender1 = "";
        String gender2 = "";
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {

            con = this.dataSource.getConnection();
            pst = con.prepareStatement("select gender from emp_mast where emp_id=?");
            pst.setString(1, empId);
            rs = pst.executeQuery();
            if (rs.next()) {
                gender1 = rs.getString("gender");
                if (gender1.equalsIgnoreCase("M")) {
                    gender2 = "he";
                }
                if (gender1.equalsIgnoreCase("F")) {
                    gender2 = "she";
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return gender2;
    }

    @Override
    public void saveCLCancelData(Leave leaveForm) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");

        String fromdate = "";
        String todate = "";
        try {
            con = this.dataSource.getConnection();
            // System.out.println("Inside the Cancel Leave ApplyDAOIMPL");
            /*String sql = "update task_master set status_id=104 where task_id=?";
             pst = con.prepareStatement(sql);
             pst.setInt(1, Integer.parseInt(leaveForm.getHidTaskId()));
             pst.executeUpdate();*/

            int taskId = Integer.parseInt(leaveForm.getHidTaskId());
            String pendingAt = "";
            String pendingAtSPC = "";

            //Get the authority details to modify the pending at and pending spc details
            String sql_leave = "select * from hw_emp_leave where task_id=" + taskId;
            pst = con.prepareStatement(sql_leave);
            rs = pst.executeQuery();
            if (rs.next()) {
                pendingAt = rs.getString("submitedto");
                pendingAtSPC = rs.getString("submit_auth");
            }

            // System.out.println("Pending at Empid :" + pendingAt + " Pending at spc: " + pendingAtSPC);
            String sql = "update task_master set status_id=104, pending_at = ?, pending_spc = ? where task_id=?";
            String printSql = "update task_master set status_id=104, pending_at = '" + pendingAt + "', pending_spc = '" + pendingAtSPC + "' where task_id=" + taskId;
            //  System.out.println("printSqlCancelRequest" + printSql);

            pst = con.prepareStatement(sql);
            pst.setString(1, pendingAt);
            pst.setString(2, pendingAtSPC);
            pst.setInt(3, taskId);
            pst.executeUpdate();

            DataBaseFunctions.closeSqlObjects(rs, pst);

            if (leaveForm.getRdFullPartPeriod() != null && !leaveForm.getRdFullPartPeriod().equals("")) {
                if (leaveForm.getRdFullPartPeriod().equals("Part")) {
                    sql = "update hw_emp_leave set approve_fdate=?,approve_tdate=? where task_id=?";
                    pst = con.prepareStatement(sql);
                    pst.setTimestamp(1, new Timestamp(formatter.parse(leaveForm.getTxtApproveFrom()).getTime()));
                    pst.setTimestamp(2, new Timestamp(formatter.parse(leaveForm.getTxtApproveTo()).getTime()));
                    pst.setInt(3, Integer.parseInt(leaveForm.getHidTaskId()));
                    pst.executeUpdate();
                } else if (leaveForm.getRdFullPartPeriod().equals("Full")) {
                    String datesql = "select fdate,tdate from hw_emp_leave where task_id=?";
                    pst = con.prepareStatement(datesql);
                    pst.setInt(1, Integer.parseInt(leaveForm.getHidTaskId()));
                    rs = pst.executeQuery();
                    if (rs.next()) {
                        fromdate = CommonFunctions.getFormattedOutputDate3(rs.getDate("fdate"));
                        todate = CommonFunctions.getFormattedOutputDate3(rs.getDate("tdate"));
                    }

                    sql = "update hw_emp_leave set approve_fdate=?,approve_tdate=? where task_id=?";
                    pst = con.prepareStatement(sql);
                    pst.setTimestamp(1, new Timestamp(formatter.parse(fromdate).getTime()));
                    pst.setTimestamp(2, new Timestamp(formatter.parse(todate).getTime()));
                    pst.setInt(3, Integer.parseInt(leaveForm.getHidTaskId()));
                    pst.executeUpdate();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void saveEOCancelData(Leave leaveForm) {
        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");

        String fromdate = "";
        String todate = "";

        try {
            con = this.dataSource.getConnection();
            int taskId = Integer.parseInt(leaveForm.getHidTaskId());
            String pendingAt = "";
            String pendingAtSPC = "";

            //Get the authority details to modify the pending at and pending spc details
            String sql_leave = "select * from hw_emp_leave where task_id=" + taskId;
            pst = con.prepareStatement(sql_leave);
            rs = pst.executeQuery();
            if (rs.next()) {
                pendingAt = rs.getString("submitedto");
                pendingAtSPC = rs.getString("submit_auth");
            }

            //Send SMS alert to Sanctioning Authority (added by Shantanu on 24-03-2023)
            //1. Get the mobile no of the sanctioning authority
            String mobile = null;
            String sqlMobile = "SELECT mobile FROM emp_mast where emp_id = '" + pendingAt + "'";
            pst = con.prepareStatement(sqlMobile);
            rs = pst.executeQuery();
            if (rs.next()) {
                mobile = rs.getString("mobile");
            }
            String msg = "Leave Cancel Request received. HRMS Odisha.";
            SMSServices smhttp = new SMSServices(mobile, msg, "1407167903984063272");

            //Send SMS alert to Applicant
            String applicantId = leaveForm.getEmpId();
            String applicantMobile = null;
            String sqlMobileApplicant = "SELECT mobile FROM emp_mast where emp_id = '" + applicantId + "'";
            pst = con.prepareStatement(sqlMobileApplicant);
            rs = pst.executeQuery();
            if (rs.next()) {
                applicantMobile = rs.getString("mobile");
            }
            msg = "Leave Cancel Request received. HRMS Odisha.";
            SMSServices smhttpApp = new SMSServices(applicantMobile, msg, "1407167903984063272");

            // System.out.println("Pending at Empid :" + pendingAt + " Pending at spc: " + pendingAtSPC);
            String sql = "update task_master set status_id=104, pending_at = ?, pending_spc = ? where task_id=?";
            String printSql = "update task_master set status_id=104, pending_at = '" + pendingAt + "', pending_spc = '" + pendingAtSPC + "' where task_id=" + taskId;
            // System.out.println("printSqlCancelRequest" + printSql);

            pst = con.prepareStatement(sql);
            pst.setString(1, pendingAt);
            pst.setString(2, pendingAtSPC);
            pst.setInt(3, taskId);
            pst.executeUpdate();

            DataBaseFunctions.closeSqlObjects(rs, pst);

            if (leaveForm.getRdFullPartPeriod() != null && !leaveForm.getRdFullPartPeriod().equals("")) {
                if (leaveForm.getRdFullPartPeriod().equals("Part")) {
                    sql = "update hw_emp_leave set approve_fdate=?,approve_tdate=? where task_id=?";
                    pst = con.prepareStatement(sql);
                    pst.setTimestamp(1, new Timestamp(formatter.parse(leaveForm.getTxtApproveFrom()).getTime()));
                    pst.setTimestamp(2, new Timestamp(formatter.parse(leaveForm.getTxtApproveTo()).getTime()));
                    pst.setInt(3, Integer.parseInt(leaveForm.getHidTaskId()));
                    pst.executeUpdate();
                } else if (leaveForm.getRdFullPartPeriod().equals("Full")) {
                    String datesql = "select fdate,tdate from hw_emp_leave where task_id=?";
                    pst = con.prepareStatement(datesql);
                    pst.setInt(1, Integer.parseInt(leaveForm.getHidTaskId()));
                    rs = pst.executeQuery();
                    if (rs.next()) {
                        fromdate = CommonFunctions.getFormattedOutputDate3(rs.getDate("fdate"));
                        todate = CommonFunctions.getFormattedOutputDate3(rs.getDate("tdate"));
                    }

                    sql = "update hw_emp_leave set approve_fdate=?,approve_tdate=? where task_id=?";
                    pst = con.prepareStatement(sql);
                    pst.setTimestamp(1, new Timestamp(formatter.parse(fromdate).getTime()));
                    pst.setTimestamp(2, new Timestamp(formatter.parse(todate).getTime()));
                    pst.setInt(3, Integer.parseInt(leaveForm.getHidTaskId()));
                    pst.executeUpdate();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void approveCLCancel(Leave leaveForm, String applicantEmpId) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        int mcode = 0;
        String fromdate = "";
        String todate = "";
        String pendingAt = "";
        String pendingAtSPC = "";
        try {
            con = this.dataSource.getConnection();

            Calendar cal = Calendar.getInstance();

            String curYear = cal.get(Calendar.YEAR) + "";
            Timestamp timestamp = new Timestamp(cal.getTimeInMillis());
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");

            pst = con.prepareStatement("INSERT INTO WORKFLOW_LOG(TASK_ID, TASK_ACTION_DATE, ACTION_TAKEN_BY, SPC_ONTIME, TASK_STATUS_ID, NOTE,FORWARD_TO,FORWARDED_SPC) Values (?,?,?,?,?,?,?,?)");

            pst.setInt(1, Integer.parseInt(leaveForm.getHidTaskId()));
            pst.setTimestamp(2, timestamp);
            pst.setString(3, leaveForm.getHidempId());
            pst.setString(4, leaveForm.getHidSpcCode());
            pst.setInt(5, Integer.parseInt(leaveForm.getSltActionType()));
            pst.setString(6, leaveForm.getTxtauthnote());
            pst.setString(7, leaveForm.getHidAuthEmpId());
            pst.setString(8, leaveForm.getHidSpcAuthCode());
            pst.executeUpdate();

            DataBaseFunctions.closeSqlObjects(rs, pst);

            //Get the pending at and pending spc from hw_emp_leave table(Added by Shantanu on 16-03-2023)
            //Update the pending at and pending spc in task_master table
            int taskId = Integer.parseInt(leaveForm.getHidTaskId());
            String sql_leave = "select * from hw_emp_leave where task_id=" + taskId;
            pst = con.prepareStatement(sql_leave);
            rs = pst.executeQuery();
            if (rs.next()) {
                pendingAt = rs.getString("issuing_auth_empid");
                pendingAtSPC = rs.getString("issuing_auth_spc");
            }

            String sql = "update task_master set status_id=?, pending_at = ?, pending_spc = ?where task_id=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(leaveForm.getSltActionType()));
            pst.setString(2, pendingAt);
            pst.setString(3, pendingAtSPC);
            pst.setInt(4, Integer.parseInt(leaveForm.getHidTaskId()));
            pst.executeUpdate();

            sql = "select approve_fdate,approve_tdate from hw_emp_leave where task_id=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(leaveForm.getHidTaskId()));
            rs = pst.executeQuery();
            if (rs.next()) {
                fromdate = CommonFunctions.getFormattedOutputDate3(rs.getDate("approve_fdate"));
                todate = CommonFunctions.getFormattedOutputDate3(rs.getDate("approve_tdate"));
            }

            double noofdays = calculateDateDiff(fromdate, todate, leaveForm.getEmpId(), leaveForm.getTollid());

            DataBaseFunctions.closeSqlObjects(rs, pst);

            sql = "update emp_leave_balance set available=available+" + noofdays + " where emp_id=? and tol_id=? and year=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, applicantEmpId);
            pst.setString(2, leaveForm.getTollid());
            pst.setString(3, curYear);
            int retVal = pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void updateCLBalance(String offCode, String postCode, String clDays, String year, String month) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        PreparedStatement selectpst = null;
        ResultSet selectrs = null;
        PreparedStatement insertpst = null;
        PreparedStatement insertpst1 = null;
        SelectOption so = null;
        ArrayList emplist = new ArrayList();
        Calendar cal = Calendar.getInstance();
        int curYear = cal.get(Calendar.YEAR);
        int curMonth = cal.get(Calendar.MONTH) - 5;

        try {
            con = this.dataSource.getConnection();
            String insertsql = "insert into emp_leave_balance(year,emp_id,tol_id,balance,available,month) values(?,?,?,?,?,?)";
            String insertsql1 = "update  emp_leave_balance set balance=?,month='JAN'  where emp_id=? and year=? and tol_id='CL'";
            insertpst = con.prepareStatement(insertsql);
            insertpst1 = con.prepareStatement(insertsql1);

            String selectsql = "select emp_code,f_name,m_name,l_name,aq_month from g_post inner join g_spc on g_spc.gpc=g_post.post_code inner join emp_mast on emp_mast.cur_spc=g_spc.spc  "
                    + "inner join aq_mast on aq_mast.emp_code=emp_mast.emp_id where post_code=? and g_spc.off_code=? "
                    + "and  aq_year=? and aq_month=? and emp_code is not null";
            selectpst = con.prepareStatement(selectsql);
            selectpst.setString(1, postCode);
            selectpst.setString(2, offCode);
            selectpst.setInt(3, curYear);
            selectpst.setInt(4, curMonth);
            selectrs = selectpst.executeQuery();
            while (selectrs.next()) {
                so = new SelectOption();
                so.setValue(selectrs.getString("emp_code"));
                emplist.add(so);
            }

            if (emplist != null && emplist.size() > 0) {
                so = null;

                for (int i = 0; i < emplist.size(); i++) {
                    so = (SelectOption) emplist.get(i);

                    if (isDuplicate(con, so.getValue(), year).equals("N")) {
                        insertpst.setString(1, year);
                        insertpst.setString(2, so.getValue());
                        insertpst.setString(3, "CL");
                        insertpst.setInt(4, Integer.parseInt(clDays));
                        insertpst.setDouble(5, Integer.parseInt(clDays));
                        insertpst.setString(6, "JAN");
                        insertpst.executeUpdate();
                    } else {

                        insertpst1.setInt(1, Integer.parseInt(clDays));
                        insertpst1.setString(2, so.getValue());
                        insertpst1.setString(3, year);

                        insertpst1.executeUpdate();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    private static String isDuplicate(Connection con, String empid, String year) {
        PreparedStatement pst = null;
        ResultSet rs = null;

        String isDuplicate = "N";
        try {
            String sql = "select count(*) cnt from emp_leave_balance where emp_id=? and tol_id=? and year=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            pst.setString(2, "CL");
            pst.setString(3, year);
            rs = pst.executeQuery();
            if (rs.next()) {
                if (rs.getInt("cnt") > 0) {
                    isDuplicate = "Y";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
        }
        return isDuplicate;
    }

    public ArrayList getSearchLeaveList(String criteria, String searchString) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        ArrayList searchedLeaveList = new ArrayList();
        Leave leaveForm = null;
        String searchList = "";
        String searchListByTaskId = "";
        try {
            con = dataSource.getConnection();
            if (criteria != null && criteria.equals("HRMSID")) {
                searchList = "SELECT G_PROCESS_STATUS.STATUS_ID, HW_EMP_LEAVE.TASK_ID TASK_ID,LEAVEID,EMP_ID,FDATE,TDATE,APPROVE_FDATE,APPROVE_TDATE,HW_EMP_LEAVE.TOL_ID,STATUS_NAME, "
                        + "INITIATED_ON,IS_SEEN,IS_EXTENDED,gleave.TOL,approve_fdate,approve_tdate FROM HW_EMP_LEAVE  "
                        + "INNER JOIN TASK_MASTER ON TASK_MASTER.TASK_ID=HW_EMP_LEAVE.TASK_ID  "
                        + "INNER JOIN G_PROCESS_STATUS ON G_PROCESS_STATUS.STATUS_ID=TASK_MASTER.STATUS_ID inner join (select distinct tol_id,tol from g_leave)gleave on gleave.tol_id=HW_EMP_LEAVE.tol_id "
                        + "WHERE EMP_ID=? AND TASK_MASTER.PROCESS_ID=1 ORDER BY INITIATED_ON DESC";
                pstmt = con.prepareStatement(searchList);
                pstmt.setString(1, searchString);
            } else if (criteria != null && criteria.equals("TASKID")) {
                searchList = "SELECT G_PROCESS_STATUS.STATUS_ID, HW_EMP_LEAVE.TASK_ID TASK_ID,LEAVEID,EMP_ID,FDATE,TDATE,APPROVE_FDATE,APPROVE_TDATE,HW_EMP_LEAVE.TOL_ID,STATUS_NAME, "
                        + "INITIATED_ON,IS_SEEN,IS_EXTENDED,gleave.TOL,approve_fdate,approve_tdate FROM HW_EMP_LEAVE  "
                        + "INNER JOIN TASK_MASTER ON TASK_MASTER.TASK_ID=HW_EMP_LEAVE.TASK_ID  "
                        + "INNER JOIN G_PROCESS_STATUS ON G_PROCESS_STATUS.STATUS_ID=TASK_MASTER.STATUS_ID inner join (select distinct tol_id,tol from g_leave)gleave on gleave.tol_id=HW_EMP_LEAVE.tol_id "
                        + "WHERE TASK_MASTER.TASK_ID=? AND TASK_MASTER.PROCESS_ID=1 ORDER BY INITIATED_ON DESC";
                pstmt = con.prepareStatement(searchList);
                pstmt.setInt(1, Integer.parseInt(searchString));
            }
            rs = pstmt.executeQuery();
            while (rs.next()) {
                leaveForm = new Leave();
                leaveForm.setTxtperiodFrom(CommonFunctions.getFormattedOutputDate3(rs.getDate("FDATE")));
                leaveForm.setTxtperiodTo(CommonFunctions.getFormattedOutputDate3(rs.getDate("TDATE")));
                leaveForm.setTxtApproveFrom(CommonFunctions.getFormattedOutputDate3(rs.getDate("approve_fdate")));
                leaveForm.setTxtApproveTo(CommonFunctions.getFormattedOutputDate3(rs.getDate("approve_tdate")));
                leaveForm.setSltleaveType(rs.getString("TOL"));
                leaveForm.setTaskId(Integer.parseInt(rs.getString("task_id")));
                leaveForm.setStatus(rs.getString("STATUS_NAME"));
                searchedLeaveList.add(leaveForm);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return searchedLeaveList;

    }

    public void deleteLeave(String taskId) {
        Connection con = null;
        PreparedStatement pstmt = null;
        PreparedStatement pstmt1 = null;
        PreparedStatement pstmt2 = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("delete from hw_emp_leave where task_id=?");
            pstmt.setInt(1, Integer.parseInt(taskId));
            pstmt.executeUpdate();
            pstmt1 = con.prepareStatement("delete from task_master where task_id=?");
            pstmt1.setInt(1, Integer.parseInt(taskId));
            pstmt1.executeUpdate();
            pstmt2 = con.prepareStatement("delete from workflow_log where task_id=?");
            pstmt2.setInt(1, Integer.parseInt(taskId));
            pstmt2.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt, pstmt1, pstmt2);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    public void saveManageLeave(Leave leaveForm) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("update hw_emp_leave set tol_id=?,fdate=?,tdate=?,approve_fdate=?,approve_tdate=? where task_id=?");
            pstmt.setString(1, leaveForm.getSltleaveType());
            if (leaveForm.getTxtperiodFrom() != null && !leaveForm.getTxtperiodFrom().equals("")) {
                pstmt.setTimestamp(2, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(leaveForm.getTxtperiodFrom()).getTime()));
            } else {
                pstmt.setTimestamp(2, null);
            }
            if (leaveForm.getTxtperiodTo() != null && !leaveForm.getTxtperiodTo().equals("")) {
                pstmt.setTimestamp(3, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(leaveForm.getTxtperiodTo()).getTime()));
            } else {
                pstmt.setTimestamp(3, null);
            }
            if (leaveForm.getTxtApproveFrom() != null && !leaveForm.getTxtApproveFrom().equals("")) {
                pstmt.setTimestamp(4, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(leaveForm.getTxtApproveFrom()).getTime()));
            } else {
                pstmt.setTimestamp(4, null);
            }
            if (leaveForm.getTxtApproveTo() != null && !leaveForm.getTxtApproveTo().equals("")) {
                pstmt.setTimestamp(5, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(leaveForm.getTxtApproveTo()).getTime()));
            } else {
                pstmt.setTimestamp(5, null);
            }
            pstmt.setInt(6, leaveForm.getTaskId());
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

}
