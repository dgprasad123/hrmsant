/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.leave;

import hrms.SelectOption;
import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.dao.leaveAccount.ServiceBookLanguageDAO;
import hrms.dao.leaveapply.MaxLeaveIdDAOImpl;
import hrms.dao.master.OfficeDAO;
import hrms.dao.master.SubStantivePostDAO;
import hrms.model.leave.LSOrderType;
import hrms.model.leave.Leave;
import hrms.model.leave.LeaveOpeningBalanceBean;
import hrms.model.leave.LeaveType;
import hrms.model.leave.MiscLeaveBean;
import hrms.model.leave.MiscLeaveForm;
import hrms.model.master.Office;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Manoj PC
 */
public class MiscLeaveDAOImpl implements MiscLeaveDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

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
    
    protected ServiceBookLanguageDAO sbDAO;

    public void setSbDAO(ServiceBookLanguageDAO sbDAO) {
        this.sbDAO = sbDAO;
    }

    @Override
    public LSOrderType getLSOrderType(String LSOrdId) {
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        Connection con = null;
        LSOrderType lso = new LSOrderType();
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT * FROM G_LSOT where LSOT_ID=?");
            pstmt.setString(1, LSOrdId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                lso.setLsotid(LSOrdId);
                lso.setLsot(rs.getString("LSOT"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return lso;
    }

    @Override
    public LeaveType getLeaveType(String leaveId) {
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        Connection con = null;
        LeaveType lt = new LeaveType();
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT * FROM G_LEAVE where TOL_ID=?");
            pstmt.setString(1, leaveId.toUpperCase());
            rs = pstmt.executeQuery();

            while (rs.next()) {
                lt.setLeaveID(rs.getString("TOL_ID"));
                lt.setLeaveType(rs.getString("LSOT"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return lt;
    }

    @Override
    public ArrayList getLeaveList(String EmpID, String ordType) {
        ResultSet rs = null;
        PreparedStatement pst = null;
        Connection con = null;
        LeaveType leavetype = null;
        LSOrderType lordertype = null;
        ArrayList al = new ArrayList();
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("SELECT * FROM (SELECT leaveList.* FROM(SELECT is_validated,LVID,LNOTID,LNOTYPE,LSOSTID,LTOLID,LFDATE,LTDATE,LSUF,LSUFRM,LPREFIX,LPREFIXTO,LSYEAR,LTYEAR,LSDAYS,NOT_ID,NOT_TYPE,EMP_ID,DOE,TOE,IF_ASSUMED,ORDNO,ORDDT,DEPT_CODE,OFF_CODE,AUTH,ENT_DEPT,ENT_OFF,ENT_AUTH,NOTE,ISCANCELED,LINK_ID,IF_VISIBLE,CANCEL_TYPE,IF_MEDICAL,IF_COMMUTED,IF_LONGTERM,TR_DATA_TYPE ,SV_ID FROM (SELECT emp_leave.leaveid lvid,emp_leave.not_id lnotid,emp_leave.not_type lnotype,emp_leave.lsot_id "
                    + "lsostid,emp_leave.tol_id ltolid,emp_leave.fdate lfdate,emp_leave.tdate ltdate,emp_leave.suffix_date lsuf,emp_leave.suffix_from "
                    + "lsufrm,emp_leave.prefix_date lprefix,emp_leave.prefix_to lprefixto,emp_leave.s_year lsyear,emp_leave.t_year ltyear,emp_leave.if_medical if_medical,emp_leave.if_commuted if_commuted,emp_leave.if_longterm if_longterm,"
                    + "emp_leave.s_days lsdays  FROM emp_leave where EMP_ID='" + EmpID + "' AND LSOT_ID='" + ordType + "') leav inner join "
                    + "(select e1.*,e2.not_type cancel_type from (select * from emp_notification where EMP_ID='" + EmpID + "') e1 left outer join "
                    + "(select * from emp_notification where EMP_ID='" + EmpID + "') e2 on e1.link_id=e2.not_id)"
                    + " emp_notification on emp_notification.not_type = lnotype and emp_notification.not_id = lnotid ORDER BY DOE DESC)leaveList)leavetemp");
            rs = pst.executeQuery();
            while (rs.next()) {
                MiscLeaveForm leave = new MiscLeaveForm();
                leave.setDateOfEntry(CommonFunctions.getFormattedOutputDate1(rs.getDate("doe")));
                leave.setLeaveId(rs.getString("LVID"));
                leave.setNotificationId(rs.getInt("not_id"));
                leave.setNotificationType(rs.getString("not_type"));
                leave.setOrderNumber(rs.getString("ordno"));
                leave.setOrderDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("orddt")));
                leave.setFromDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("lfdate")));
                leave.setToDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("ltdate")));
                leave.setIsValidated(rs.getString("is_validated"));
                al.add(leave);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return al;
    }

    @Override
    public void saveMiscLeave(MiscLeaveForm mLeave, int notid, String lsotId) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs=null;
        try {
            con = dataSource.getConnection();

            String sql = "INSERT INTO emp_leave (not_id, not_type, emp_id, lsot_id"
                    + ", fdate, tdate) VALUES(?,?,?,?,?,?)";
            pst = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
            pst.setInt(1, notid);
            pst.setString(2, "LEAVE");
            pst.setString(3, mLeave.getEmpId());
            pst.setString(4, lsotId);
            pst.setTimestamp(5, new Timestamp(dateFormat.parse(mLeave.getFromDate()).getTime()));
            pst.setTimestamp(6, new Timestamp(dateFormat.parse(mLeave.getToDate()).getTime()));
            
            int maxLeaveId = 0;
            int ret = pst.executeUpdate();
            rs = pst.getGeneratedKeys();
            rs.next();
            maxLeaveId = rs.getInt("leaveid");
            
            String sbLanguage = sbDAO.getLeaveSanctionDetails(maxLeaveId, mLeave.getEmpId(), mLeave.getHidAuthorityOthSpc());
            sql = "UPDATE EMP_NOTIFICATION SET  SB_DESCRIPTION=? WHERE EMP_ID=? AND NOT_ID=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, sbLanguage);
            pst.setString(2, mLeave.getEmpId());
            pst.setInt(3, notid);
            pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
            DataBaseFunctions.closeSqlObjects(pst);
        }
    }

    @Override
    public MiscLeaveForm getMiscLeaveData(String leaveId) {
        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;
        MiscLeaveForm mlForm = new MiscLeaveForm();
        
        try {
            con = dataSource.getConnection();

            String sql = "SELECT EN.*, EA.* FROM emp_leave EA "
                    + " LEFT OUTER JOIN emp_notification EN ON EA.not_id = EN.not_id"
                    + " WHERE EA.leaveid=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(leaveId));
            rs = pst.executeQuery();
            if (rs.next()) {
                
                mlForm.setLeaveId(rs.getString("leaveid"));
                mlForm.setNotificationId(rs.getInt("not_id"));
                mlForm.setOrderDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("orddt")));
                mlForm.setOrderNumber(rs.getString("ordno"));
                mlForm.setHidTempDeptCode(rs.getString("dept_code"));
                mlForm.setHidTempAuthOffCode(rs.getString("off_code"));
                mlForm.setHidTempAuthPost(rs.getString("auth"));
                mlForm.setAuthPostName(CommonFunctions.getSPN(con, rs.getString("auth")));
                mlForm.setRadauthtype(rs.getString("organization_type"));
                mlForm.setHidAuthDeptCode(rs.getString("dept_code"));
                mlForm.setHidAuthOffCode(rs.getString("off_code"));
                if(mlForm.getRadauthtype() != null && mlForm.getRadauthtype().equals("GOI")){
                    mlForm.setAuthPostName(getOtherSpn(rs.getString("auth")));
                    mlForm.setHidAuthorityOthSpc(rs.getString("auth"));
                }else{
                    mlForm.setRadauthtype("GOO");
                    mlForm.setAuthSpc(rs.getString("auth"));
                }
                mlForm.setHidPostedDeptCode(rs.getString("dept_code"));
                mlForm.setHidPostedOffCode(rs.getString("off_code"));
                mlForm.setFromDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("fdate")));
                mlForm.setToDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("tdate")));
                mlForm.setNote(rs.getString("note"));
                if(rs.getString("if_visible")!=null && rs.getString("if_visible").equals("N")){
                    mlForm.setChkNotSBPrint("Y");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
            DataBaseFunctions.closeSqlObjects(pst);
        }
        return mlForm;
    }

    @Override
    public void updateMiscLeave(MiscLeaveForm mLeave, int notid, String leaveId) {
        Connection con = null;
        PreparedStatement pst = null;
        try {
            con = dataSource.getConnection();

            String sql = "UPDATE emp_leave SET fdate = ?, tdate = ? WHERE leaveid = ?";
            pst = con.prepareStatement(sql);
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
            pst.setTimestamp(1, new Timestamp(dateFormat.parse(mLeave.getFromDate()).getTime()));
            pst.setTimestamp(2, new Timestamp(dateFormat.parse(mLeave.getToDate()).getTime()));
            pst.setInt(3, Integer.parseInt(leaveId));
            pst.executeUpdate();
            
            String sbLanguage = sbDAO.getLeaveSanctionDetails(Integer.parseInt(leaveId), mLeave.getEmpId(),mLeave.getHidAuthorityOthSpc());
            sql = "UPDATE EMP_NOTIFICATION SET  SB_DESCRIPTION=? WHERE EMP_ID=? AND NOT_ID=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, sbLanguage);
            pst.setString(2, mLeave.getEmpId());
            pst.setInt(3, notid);
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
            DataBaseFunctions.closeSqlObjects(pst);
        }
    }
    @Override
    public void deleteMiscLeave(String leaveId)
    {
        Connection con = null;
        PreparedStatement pst = null;
        try {
            con = dataSource.getConnection();

            String sql = "DELETE FROM emp_leave WHERE leaveid = ?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(leaveId));
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
            DataBaseFunctions.closeSqlObjects(pst);
        }
    }
    @Override
    public ArrayList getOpeningBalanceList(String empId) {
        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;
        MiscLeaveForm mlForm = new MiscLeaveForm();
        ArrayList al = new ArrayList();
        try {
            con = dataSource.getConnection();

            String sql = "select distinct GL.tol_id,tol, EO.* from g_leave GL LEFT OUTER JOIN emp_leave_ob EO ON GL.tol_id = EO.tol_id  AND EO.emp_id = ?" 
                    +" where if_leave_cr='Y' ORDER BY GL.tol_id";
            pst = con.prepareStatement(sql);
            pst.setString(1, empId);
            rs = pst.executeQuery();
            while (rs.next()) {
                LeaveOpeningBalanceBean lo = new LeaveOpeningBalanceBean();
                lo.setLeaveId(rs.getString("tol_id"));
                lo.setLeaveType(rs.getString("tol"));
                lo.setOpeningBalanceDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("ob_date")));
                lo.setTime(rs.getString("ob_time"));
                lo.setOpeningBalance(rs.getDouble("ob")+"");
                lo.setLobId(rs.getString("lob_id"));
                al.add(lo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
            DataBaseFunctions.closeSqlObjects(pst);
        }
        return al;
    }
    public void addOpeningBalance(LeaveOpeningBalanceBean loBean)
    {
        Connection con = null;
        PreparedStatement pst = null;
        try {
            con = dataSource.getConnection();

            String sql = "INSERT INTO emp_leave_ob( emp_id, tol_id, ob_date, ob_time, ob) VALUES(?,?,?,?,?)";
            pst = con.prepareStatement(sql);
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
            pst.setString(1, loBean.getEmpId());
            pst.setString(2, loBean.getLeaveId());
            pst.setTimestamp(3, new Timestamp(dateFormat.parse(loBean.getOpeningBalanceDate()).getTime()));
            pst.setString(4, loBean.getTime());
            pst.setDouble(5, Double.parseDouble(loBean.getOpeningBalance()));
            //pst.setString(6, maxleaveidDao.getMaxLeaveId());
            
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
            DataBaseFunctions.closeSqlObjects(pst);
        }
    }
    
    public void updateOpeningBalance(LeaveOpeningBalanceBean loBean)
    {
        Connection con = null;
        PreparedStatement pst = null;
        try {
            con = dataSource.getConnection();

            String sql = "UPDATE emp_leave_ob SET tol_id = ?, ob_date = ?, ob_time = ?, ob = ? where lob_id= ?";
            pst = con.prepareStatement(sql);
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
            pst.setString(1, loBean.getLeaveId());
            pst.setTimestamp(2, new Timestamp(dateFormat.parse(loBean.getOpeningBalanceDate()).getTime()));
            pst.setString(3, loBean.getTime());
            pst.setDouble(4, Double.parseDouble(loBean.getOpeningBalance()));
            pst.setInt(5, Integer.parseInt(loBean.getLobId()));
            
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
            DataBaseFunctions.closeSqlObjects(pst);
        }
    }
    @Override
    public void deleteOpeningBalance(String lobId)
    {
        Connection con = null;
        PreparedStatement pst = null;
        try {
            con = dataSource.getConnection();
            
            String sql = "DELETE FROM emp_leave_ob WHERE lob_id = ?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(lobId));
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
            DataBaseFunctions.closeSqlObjects(pst);
        }
    }
    
    public String getOtherSpn(String othSpc) {
        
        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;
        String spn = "";
        try {
            con = this.dataSource.getConnection();
            
            if (othSpc != null && !othSpc.equals("")) {
                pst = con.prepareStatement("SELECT oth_spc, off_en FROM g_oth_spc WHERE other_spc_id=? and is_active='Y'");
                pst.setInt(1, Integer.parseInt(othSpc));
                rs = pst.executeQuery();
                if (rs.next()) {
                    spn = rs.getString("off_en");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return spn;
    }
}
