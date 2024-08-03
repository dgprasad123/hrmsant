/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.headquarterleaving;

import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.dao.leaveAccount.ServiceBookLanguageDAO;
import hrms.dao.notification.NotificationDAO;
import hrms.model.headquarterleaving.HeadQuarterLeaving;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 *
 * @author lenovo
 */
public class HeadQuarterLeavingDAOImpl implements HeadQuarterLeavingDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;
    protected ServiceBookLanguageDAO sbDAO;
    protected NotificationDAO notificationDao;

    public void setSbDAO(ServiceBookLanguageDAO sbDAO) {
        this.sbDAO = sbDAO;
    }

    public void setNotificationDao(NotificationDAO notificationDao) {
        this.notificationDao = notificationDao;
    }
    

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List HaedQuarterLeavingList(String empid) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        List leavinglist = new ArrayList();
        HeadQuarterLeaving tbean = null;
        try {
            con = dataSource.getConnection();
            String sql = "SELECT * FROM emp_permission WHERE emp_id=? ORDER BY per_id DESC";
            
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            rs = pst.executeQuery();
            while (rs.next()) {
                
                tbean = new HeadQuarterLeaving();
                tbean.setPermissionId(rs.getString("per_id"));
                tbean.setDoe(CommonFunctions.getFormattedOutputDate1(rs.getDate("doe")));
                tbean.setType(rs.getString("per_type"));
                tbean.setFtime(rs.getString("ftime"));
                tbean.setEtime(rs.getString("ttime"));
                tbean.setStrFdate(CommonFunctions.getFormattedOutputDate1(rs.getDate("fdate")));
                tbean.setStrEdate(CommonFunctions.getFormattedOutputDate1(rs.getDate("tdate")));
                //   tbean.sets
                // tbean.sets(CommonFunctions.getFormattedOutputDate1(rs.getDate("doe")));
                leavinglist.add(tbean);

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return leavinglist;
    }

    @Override
    public void savePermission(HeadQuarterLeaving leavingForm, String ent_deptCode, String ent_officeCode, String ent_auth) {

        Connection con = null;
        PreparedStatement pst = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date cdate = new Date();

        try {

            String desc = "GRANTED HEADQUARTERS LEAVING PERMISSION FROM  " + CommonFunctions.getFormattedOutputDate1(leavingForm.getFdate()) + "(" + leavingForm.getFtime() + ")  TO " + CommonFunctions.getFormattedOutputDate1(leavingForm.getEdate()) + " (" + leavingForm.getEtime() + ")  VIDE NOTIFICATION/ OFFICE ORDER NO. " + leavingForm.getOrdno().toUpperCase() + " DATED " + CommonFunctions.getFormattedOutputDate1(leavingForm.getOrdt()) + ".";
            con = dataSource.getConnection();
            String per_id = CommonFunctions.getMaxCode("emp_permission", "per_id", con);
            String sql = "INSERT INTO emp_permission (emp_id, if_assumed,per_type,ord_no,ord_date,dept_code,off_code,auth,ent_dept,ent_off,ent_auth,fdate,ftime,tdate,ttime,note,is_locked,doe,sb_description,if_visible) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            pst = con.prepareStatement(sql);
            pst.setString(1, leavingForm.getEmpid());
            pst.setString(2, "N");
            pst.setString(3, leavingForm.getType());
            pst.setString(4, leavingForm.getOrdno());
            if (leavingForm.getOrdt() != null) {
                pst.setDate(5, new java.sql.Date(leavingForm.getOrdt().getTime()));
            } else {
                pst.setDate(5, null);
            }
            pst.setString(6, leavingForm.getHidAuthDeptCode());
            pst.setString(7, leavingForm.getHidAuthOffCode());
            pst.setString(8, leavingForm.getAuthSpc());

            pst.setString(9, ent_deptCode);
            pst.setString(10, ent_officeCode);
            pst.setString(11, ent_auth);
            if (leavingForm.getFdate() != null) {
                pst.setDate(12, new java.sql.Date(leavingForm.getFdate().getTime()));
            } else {
                pst.setDate(12, null);
            }
            pst.setString(13, leavingForm.getFtime());
            if (leavingForm.getEdate() != null) {
                pst.setDate(14, new java.sql.Date(leavingForm.getEdate().getTime()));
            } else {
                pst.setDate(14, null);
            }
            pst.setString(15, leavingForm.getEtime());
            pst.setString(16, leavingForm.getNote());
            pst.setString(17, "N");
            pst.setDate(18, new java.sql.Date(cdate.getTime()));
            pst.setString(19, desc);
            if(leavingForm.getChkNotSBPrintnew()!=null && leavingForm.getChkNotSBPrintnew().equals("Y")){
                pst.setString(20, "N");
            }else{
                 pst.setString(20, "Y");
            }
            pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public HeadQuarterLeaving getEmpPermissionData(HeadQuarterLeaving leavingForm, String permissionId, String empid) throws SQLException {

        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        HeadQuarterLeaving tbean = null;
        try {
            con = dataSource.getConnection();
            String sql = "SELECT * FROM emp_permission WHERE emp_id=? AND per_id=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            pst.setInt(2, Integer.parseInt(permissionId));
            rs = pst.executeQuery();
            while (rs.next()) {
                tbean = new HeadQuarterLeaving();
                tbean.setPermissionId(rs.getString("per_id"));
                tbean.setDoe(CommonFunctions.getFormattedOutputDate1(rs.getDate("doe")));
                tbean.setType(rs.getString("per_type"));
                tbean.setFtime(rs.getString("ftime"));
                tbean.setEtime(rs.getString("ttime"));
                tbean.setStrFdate(CommonFunctions.getFormattedOutputDate1(rs.getDate("fdate")));
                tbean.setStrEdate(CommonFunctions.getFormattedOutputDate1(rs.getDate("tdate")));
                tbean.setOrdno(rs.getString("ord_no"));
                tbean.setStrOrdt(CommonFunctions.getFormattedOutputDate1(rs.getDate("ord_date")));
                tbean.setNote(rs.getString("note"));
                tbean.setHidAuthDeptCode(rs.getString("dept_code"));
                tbean.setHidAuthOffCode(rs.getString("off_code"));
                tbean.setAuthSpc(rs.getString("auth"));
                tbean.setAuthPostName(CommonFunctions.getSPN(con, rs.getString("auth")));
                tbean.setEmpid(rs.getString("emp_id"));
                if(rs.getString("if_visible")!=null && rs.getString("if_visible").equals("N")){
                  tbean.setChkNotSBPrintedit("Y");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return tbean;
    }

    @Override
    public void updatePermission(HeadQuarterLeaving leavingForm, String ent_deptCode, String ent_officeCode, String ent_auth) {

        Connection con = null;
        PreparedStatement pst = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date cdate = new Date();

        try {
            String desc = "GRANTED HEADQUARTERS LEAVING PERMISSION FROM  " + leavingForm.getStrFdate() + "(" + leavingForm.getFtime() + ")  TO " + leavingForm.getStrEdate() + " (" + leavingForm.getEtime() + ")  VIDE NOTIFICATION/ OFFICE ORDER NO. " + leavingForm.getOrdno().toUpperCase() + " DATED " + leavingForm.getStrOrdt() + ".";
            con = dataSource.getConnection();
            String sql = "UPDATE emp_permission SET per_type=?,ord_no=?,ord_date=?,dept_code=?,off_code=?,auth=?,ent_dept=?,ent_off=?,ent_auth=?,fdate=?,ftime=?,tdate=?,ttime=?,note=?,sb_description=?,if_visible=? WHERE emp_id=? AND per_id=?";
            
            
            pst = con.prepareStatement(sql);
            pst.setString(1, leavingForm.getType());
            pst.setString(2, leavingForm.getOrdno());
            if (leavingForm.getStrOrdt() != null) {
                pst.setTimestamp(3, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(leavingForm.getStrOrdt()).getTime()));
            } else {
                pst.setTimestamp(3, null);
            }
            pst.setString(4, leavingForm.getHidAuthDeptCode());
            pst.setString(5, leavingForm.getHidAuthOffCode());
            pst.setString(6, leavingForm.getAuthSpc());
            pst.setString(7, ent_deptCode);
            pst.setString(8, ent_officeCode);
            pst.setString(9, ent_auth);
            if (leavingForm.getStrFdate() != null) {
                pst.setTimestamp(10, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(leavingForm.getStrFdate()).getTime()));

            } else {
                pst.setTimestamp(10, null);
            }
            pst.setString(11, leavingForm.getFtime());
            if (leavingForm.getStrEdate() != null) {
                pst.setTimestamp(12, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(leavingForm.getStrEdate()).getTime()));
            } else {
                pst.setDate(12, null);
            }
            pst.setString(13, leavingForm.getEtime());
            pst.setString(14, leavingForm.getNote());
            pst.setString(15, desc);
            if(leavingForm.getChkNotSBPrintedit()!=null && leavingForm.getChkNotSBPrintedit().equals("Y")){
                pst.setString(16, "N");
            }else{
                pst.setString(16, "Y");
            }
            pst.setString(17, leavingForm.getEmpid());
            pst.setInt(18, Integer.parseInt(leavingForm.getPermissionId()));

            pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public List DetentionList(String empid) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        List leavinglist = new ArrayList();
        HeadQuarterLeaving tbean = null;
        try {
            con = dataSource.getConnection();
            String sql = "SELECT b.is_validated,a.*,b.doe,c.tol_id FROM emp_dtnd_vac a , emp_notification b,emp_leave_cr c  WHERE a.emp_id=? AND a.not_id=b.not_id AND a.lcr_id=c.lcr_id AND a.not_type='DET_VAC' ORDER BY dv_id DESC";
            
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            rs = pst.executeQuery();
            while (rs.next()) {
                tbean = new HeadQuarterLeaving();
                tbean.setDv_id(rs.getString("dv_id"));
                tbean.setDoe(CommonFunctions.getFormattedOutputDate1(rs.getDate("doe")));
                tbean.setType(rs.getString("tol_id"));
                tbean.setStrFdate(CommonFunctions.getFormattedOutputDate1(rs.getDate("fdate")));
                tbean.setStrEdate(CommonFunctions.getFormattedOutputDate1(rs.getDate("tdate")));
                tbean.setL_days(rs.getDouble("l_days"));
                tbean.setIsValidated(rs.getString("is_validated"));
                leavinglist.add(tbean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return leavinglist;
    }

    @Override
    public void saveDetention(HeadQuarterLeaving leavingForm, int notid) {

        Connection con = null;
        PreparedStatement pst = null;
        PreparedStatement pst1 = null;

        try {
            con = this.dataSource.getConnection();
            
            String lcr_id = CommonFunctions.getMaxCode("emp_leave_cr", "lcr_id", con);
            String sqlLcr = "INSERT INTO emp_leave_cr (lcr_id, emp_id, tol_id, sp_from,sp_to) VALUES(?,?,?,?,?)";
            pst1 = con.prepareStatement(sqlLcr);
            pst1.setString(1, lcr_id);
            pst1.setString(2, leavingForm.getEmpid());
            pst1.setString(3, leavingForm.getType());
            if (leavingForm.getFdate() != null) {
                pst1.setDate(4, new java.sql.Date(leavingForm.getFdate().getTime()));
            } else {
                pst1.setDate(4, null);
            }
            if (leavingForm.getFdate() != null) {
                pst1.setDate(5, new java.sql.Date(leavingForm.getFdate().getTime()));
            } else {
                pst1.setDate(5, null);
            }
            pst1.executeUpdate();
            
            DataBaseFunctions.closeSqlObjects(pst1);
            
            String dv_id = CommonFunctions.getMaxCode("emp_dtnd_vac", "dv_id", con);
            String sql = "INSERT INTO emp_dtnd_vac (dv_id, NOT_ID, NOT_TYPE, EMP_ID,vyear,fdate,tdate,l_days,lcr_id) VALUES(?,?,?,?,?,?,?,?,?)";
            pst = con.prepareStatement(sql);
            pst.setString(1, dv_id);
            pst.setInt(2, notid);
            pst.setString(3, "DET_VAC");
            pst.setString(4, leavingForm.getEmpid());
            pst.setString(5, leavingForm.getYear());
            if (leavingForm.getFdate() != null) {
                pst.setDate(6, new java.sql.Date(leavingForm.getFdate().getTime()));
            } else {
                pst.setDate(6, null);
            }
            if (leavingForm.getEdate() != null) {
                pst.setDate(7, new java.sql.Date(leavingForm.getEdate().getTime()));
            } else {
                pst.setDate(7, null);
            }
            pst.setDouble(8, leavingForm.getL_days());
            pst.setString(9, lcr_id);
            pst.executeUpdate();
            
            String sbLang=sbDAO.getDetentionOnVacationLangDetails(leavingForm, notid, "DET_VAC");
            notificationDao.saveServiceBookLanguage(sbLang, notid, "DET_VAC");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst,con);
        }
    }

    @Override
    public HeadQuarterLeaving getEmpDetentionData(HeadQuarterLeaving leavingForm, String detentionId, String empid) throws SQLException {

        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        HeadQuarterLeaving tbean = null;
        try {
            con = dataSource.getConnection();
            String sql = "SELECT a.*,b.*,c.tol_id FROM emp_dtnd_vac a , emp_notification b,emp_leave_cr c  WHERE a.emp_id=? AND a.not_id=b.not_id AND a.lcr_id=c.lcr_id AND a.not_type='DET_VAC' AND a.dv_id=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            pst.setString(2, detentionId);
            rs = pst.executeQuery();
            while (rs.next()) {
                tbean = new HeadQuarterLeaving();
                tbean.setDv_id(rs.getString("dv_id"));
                tbean.setDoe(CommonFunctions.getFormattedOutputDate1(rs.getDate("doe")));
                tbean.setStrFdate(CommonFunctions.getFormattedOutputDate1(rs.getDate("fdate")));
                tbean.setStrEdate(CommonFunctions.getFormattedOutputDate1(rs.getDate("tdate")));
                tbean.setType(rs.getString("tol_id"));
                tbean.setOrdno(rs.getString("ordno"));
                tbean.setStrOrdt(CommonFunctions.getFormattedOutputDate1(rs.getDate("orddt")));
                tbean.setNote(rs.getString("note"));
                tbean.setYear(rs.getString("vyear"));
                tbean.setHidAuthDeptCode(rs.getString("dept_code"));
                tbean.setHidAuthOffCode(rs.getString("off_code"));
                tbean.setAuthSpc(rs.getString("auth"));

                tbean.setAuthPostName(CommonFunctions.getSPN(con, rs.getString("auth")));
                tbean.setEmpid(rs.getString("emp_id"));
                tbean.setL_days(rs.getDouble("l_days"));
                tbean.setHnotid(rs.getInt("not_id"));
                tbean.setLcrid(rs.getString("lcr_id"));
                if(rs.getString("if_visible")!=null && rs.getString("if_visible").equals("N")){
                  tbean.setChkNotSBPrintedit("Y");
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return tbean;
    }

    @Override
    public void updateDetention(HeadQuarterLeaving leavingForm, String ent_deptCode, String ent_officeCode, String ent_auth) {

        Connection con = null;
        PreparedStatement pst = null;
        PreparedStatement pst1 = null;
        PreparedStatement pst2 = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date cdate = new Date();

        try {
            con = dataSource.getConnection();

            String sqlNote = "UPDATE emp_notification SET ordno=?,orddt=?,dept_code=?,off_code=?,auth=?,ent_dept=?,ent_off=?,ent_auth=?,note=?,if_visible=? WHERE emp_id=? AND not_id=?";
            pst = con.prepareStatement(sqlNote);
            pst.setString(1, leavingForm.getOrdno());
            if (leavingForm.getStrOrdt() != null) {
                pst.setTimestamp(2, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(leavingForm.getStrOrdt()).getTime()));
            } else {
                pst.setTimestamp(2, null);
            }
            pst.setString(3, leavingForm.getHidAuthDeptCode());
            pst.setString(4, leavingForm.getHidAuthOffCode());
            pst.setString(5, leavingForm.getAuthSpc());
            pst.setString(6, ent_deptCode);
            pst.setString(7, ent_officeCode);
            pst.setString(8, ent_auth);
            pst.setString(9, leavingForm.getNote());
            if(leavingForm.getChkNotSBPrintedit()!=null && leavingForm.getChkNotSBPrintedit().equals("Y")){
                pst.setString(10, "N");
            }else{
                pst.setString(10, "Y");
            }
            pst.setString(11, leavingForm.getEmpid());
            pst.setInt(12, leavingForm.getHnotid());
            pst.executeUpdate();

            String sqlLeavCr = "UPDATE emp_leave_cr SET tol_id=?,sp_from=?,sp_to=? WHERE emp_id=? AND lcr_id=?";
            pst1 = con.prepareStatement(sqlLeavCr);
            pst1.setString(1, leavingForm.getType());
            if (leavingForm.getStrFdate() != null) {
                pst1.setTimestamp(2, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(leavingForm.getStrFdate()).getTime()));

            } else {
                pst1.setTimestamp(2, null);
            }
            if (leavingForm.getStrEdate() != null) {
                pst1.setTimestamp(3, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(leavingForm.getStrEdate()).getTime()));
            } else {
                pst1.setDate(3, null);
            }

            pst1.setString(4, leavingForm.getEmpid());
            pst1.setString(5, leavingForm.getLcrid());
            pst1.executeUpdate();

            String sqldet = "UPDATE emp_dtnd_vac SET vyear=?,fdate=?,tdate=?,l_days=? WHERE emp_id=? AND dv_id=?";
            pst2 = con.prepareStatement(sqldet);
            pst2.setString(1, leavingForm.getYear());
            if (leavingForm.getStrFdate() != null) {
                pst2.setTimestamp(2, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(leavingForm.getStrFdate()).getTime()));

            } else {
                pst2.setTimestamp(2, null);
            }
            if (leavingForm.getStrEdate() != null) {
                pst2.setTimestamp(3, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(leavingForm.getStrEdate()).getTime()));
            } else {
                pst2.setDate(3, null);
            }
            pst2.setDouble(4, leavingForm.getL_days());
             pst2.setString(5, leavingForm.getEmpid());
            pst2.setString(6, leavingForm.getDv_id());
            pst2.executeUpdate();
            String sbLang=sbDAO.getDetentionOnVacationLangDetails(leavingForm, leavingForm.getHnotid(), "DET_VAC");
            notificationDao.saveServiceBookLanguage(sbLang, leavingForm.getHnotid(), "DET_VAC");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

}
