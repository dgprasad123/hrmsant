package hrms.dao.leaveSanction;

import hrms.SelectOption;
import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.dao.leaveAccount.ServiceBookLanguageDAO;
import hrms.model.leaveSanction.LeaveSanctionForm;
import hrms.model.leaveSanction.LeaveSanctionListBean;
import hrms.model.notification.NotificationBean;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;

public class LeaveSanctionDAOImpl implements LeaveSanctionDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    protected ServiceBookLanguageDAO sbDAO;

    public void setSbDAO(ServiceBookLanguageDAO sbDAO) {
        this.sbDAO = sbDAO;
    }

    @Override
    public List getLeaveSanctionList(String empid, String ordType) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        LeaveSanctionListBean lsListBean = null;
        List dataList = new ArrayList();
        try {
            con = this.dataSource.getConnection();

            String sql = "SELECT * FROM (SELECT leaveList.* FROM (SELECT emp_notification.is_validated,s_year,t_year,s_days,LVID,LNOTID,LNOTYPE,LSOSTID,LTOLID,LFDATE,LTDATE,LSUF,LSUFRM,LPREFIX,"
                    + " LPREFIXTO,LSYEAR,LTYEAR,LSDAYS,NOT_ID,NOT_TYPE,EMP_ID,DOE,TOE,IF_ASSUMED,ORDNO,ORDDT,DEPT_CODE,OFF_CODE,AUTH,ENT_DEPT,ENT_OFF,"
                    + " ENT_AUTH,NOTE,ISCANCELED,LINK_ID,IF_VISIBLE,CANCEL_TYPE,IF_MEDICAL,IF_COMMUTED,IF_LONGTERM,TR_DATA_TYPE ,SV_ID,SB_DESCRIPTION, status  FROM"
                    + " (SELECT s_year,t_year,s_days,emp_leave.leaveid lvid,emp_leave.not_id lnotid,emp_leave.not_type lnotype,emp_leave.lsot_id"
                    + " lsostid,emp_leave.tol_id ltolid,emp_leave.fdate lfdate,emp_leave.tdate ltdate,emp_leave.suffix_date lsuf,emp_leave.suffix_from"
                    + " lsufrm,emp_leave.prefix_date lprefix,emp_leave.prefix_to lprefixto,emp_leave.s_year lsyear,emp_leave.t_year ltyear,"
                    + " emp_leave.if_medical if_medical,emp_leave.if_commuted if_commuted,emp_leave.if_longterm if_longterm,"
                    + " emp_leave.s_days lsdays FROM emp_leave where EMP_ID=? AND LSOT_ID=?) leav inner join"
                    + " (select e1.*,e2.not_type cancel_type from (select * from emp_notification where EMP_ID=?) e1 left outer join"
                    + " (select * from emp_notification where EMP_ID=?) e2 on e1.link_id=e2.not_id)"
                    + " emp_notification on emp_notification.not_type = lnotype and emp_notification.not_id = lnotid ORDER BY DOE DESC)leaveList)leavetemp";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            pst.setString(2, ordType);
            pst.setString(3, empid);
            pst.setString(4, empid);
            rs = pst.executeQuery();
            while (rs.next()) {
                lsListBean = new LeaveSanctionListBean();
                lsListBean.setNotId(rs.getString("NOT_ID"));
                lsListBean.setDoe(CommonFunctions.getFormattedOutputDate1(rs.getDate("DOE")));
                lsListBean.setLeaveType(getLeaveTypeName(rs.getString("ltolid")));
                lsListBean.setFrmDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("lfdate")));
                lsListBean.setToDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("ltdate")));
                lsListBean.setSuffixFrmDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("lsufrm")));
                lsListBean.setSuffixToDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("lsuf")));
                lsListBean.setPrefixFrmDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("lprefix")));
                lsListBean.setPrefixToDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("lprefixto")));
                lsListBean.setLeaveStatus(rs.getString("status"));
                if (rs.getString("IF_LONGTERM") != null && rs.getString("IF_LONGTERM").equals("Y")) {
                    lsListBean.setIfLongLeave("YES");
                } else {
                    lsListBean.setIfLongLeave("NO");
                }
                lsListBean.setSltFromYear(rs.getString("s_year"));
                lsListBean.setSltToYear(rs.getString("t_year"));
                lsListBean.setTxtDays(rs.getString("s_days"));
                lsListBean.setSblanguage(rs.getString("SB_DESCRIPTION"));
                lsListBean.setIsValidated(rs.getString("is_validated"));

                lsListBean.setCanceltype(rs.getString("cancel_type"));
                if (rs.getString("LINK_ID") != null && !rs.getString("LINK_ID").equals("")) {
                    lsListBean.setCancelnotid(rs.getString("LINK_ID"));
                }
                dataList.add(lsListBean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return dataList;
    }

    @Override
    public String getLeaveTypeName(String leaveId) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String leaveType = "";
        try {
            con = this.dataSource.getConnection();

            String sql = "SELECT TOL_ID,TOL FROM G_LEAVE where TOL_ID=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, leaveId);
            rs = pst.executeQuery();
            if (rs.next()) {
                leaveType = rs.getString("TOL");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return leaveType;
    }

    @Override
    public List getLeaveTypeList(String ordType) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List leaveList = new ArrayList();

        String sql = "";
        SelectOption so = null;
        try {
            con = this.dataSource.getConnection();

            if (ordType != null && ordType.equals("01")) {
                sql = "SELECT DISTINCT TOL_ID,TOL FROM G_LEAVE WHERE IF_NOT_APP != 'Y' ORDER BY TOL_ID";
            }
            if (ordType != null && ordType.equals("02")) {
                sql = "SELECT DISTINCT TOL_ID,TOL FROM G_LEAVE WHERE CAN_SURRENDER = 'Y' ORDER BY TOL_ID";
            }
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                so = new SelectOption();
                so.setValue(rs.getString("TOL_ID"));
                so.setLabel(rs.getString("TOL"));
                leaveList.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return leaveList;
    }

    @Override
    public void saveLeaveSanction(LeaveSanctionForm lform, int notId) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        int days = 0;

        boolean isPeriodSubsetInAbs = false;
        boolean isPeriodSupersetInAbs = false;
        boolean isPeriodFrmOrToInAbs = false;
        try {
            con = this.dataSource.getConnection();

            if ((lform.getTxtFrmDt() != null && !lform.getTxtFrmDt().equals("")) && (lform.getTxtToDt() != null && !lform.getTxtToDt().equals(""))) {

                isPeriodSubsetInAbs = ifPeriodSubsetInAbsentee(lform.getEmpid(), lform.getTxtFrmDt(), lform.getTxtToDt());
                isPeriodSupersetInAbs = ifPeriodSupersetInAbsentee(lform.getEmpid(), lform.getTxtFrmDt(), lform.getTxtToDt());
                isPeriodFrmOrToInAbs = ifPeriodFromOrToExistInAbsentee(lform.getEmpid(), lform.getTxtFrmDt(), lform.getTxtToDt());
            }
            if (lform.getOrdType().equals("01")) {
                if ((lform.getTxtFrmDt() != null && !lform.getTxtFrmDt().equals("")) && (lform.getTxtToDt() != null && !lform.getTxtToDt().equals(""))) {
                    if (isPeriodSupersetInAbs || isPeriodFrmOrToInAbs) {
                        days = calculateNoOfDaysInAbsentee(lform.getEmpid(), lform.getTxtFrmDt(), lform.getTxtToDt());
                    } else {
                        days = dateDiffrenceBetween(lform.getTxtFrmDt(), lform.getTxtToDt());
                    }
                }
                if (days > 0) {
                    days = days + 1;
                }
            } else if (lform.getOrdType().equals("02")) {
                if (lform.getTxtDays() != null && !lform.getTxtDays().equals("")) {
                    days = Integer.parseInt(lform.getTxtDays());
                }
            }

            pst = con.prepareStatement("INSERT INTO EMP_LEAVE(NOT_ID, NOT_TYPE, EMP_ID, LSOT_ID, TOL_ID, FDATE, TDATE, SUFFIX_FROM, SUFFIX_DATE, PREFIX_DATE, PREFIX_TO, S_YEAR, T_YEAR, S_DAYS,IF_MEDICAL, IF_COMMUTED,IF_LONGTERM,JOIN_TIME_FROM,JOIN_TIME_TO) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            pst.setInt(1, notId);
            pst.setString(2, "LEAVE");
            pst.setString(3, lform.getEmpid());
            pst.setString(4, lform.getOrdType());
            pst.setString(5, lform.getSltLeaveType());
            if (lform.getTxtFrmDt() != null && !lform.getTxtFrmDt().equals("")) {
                pst.setTimestamp(6, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(lform.getTxtFrmDt()).getTime()));
            } else {
                pst.setTimestamp(6, null);
            }
            if (lform.getTxtToDt() != null && !lform.getTxtToDt().equals("")) {
                pst.setTimestamp(7, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(lform.getTxtToDt()).getTime()));
            } else {
                pst.setTimestamp(7, null);
            }
            if (lform.getOrdType().equals("01")) {
                if (lform.getTxtSuffixFrom() != null && !lform.getTxtSuffixFrom().equals("")) {
                    pst.setTimestamp(8, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(lform.getTxtSuffixFrom()).getTime()));
                } else {
                    pst.setTimestamp(8, null);
                }
                if (lform.getTxtSuffixTo() != null && !lform.getTxtSuffixTo().equals("")) {
                    pst.setTimestamp(9, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(lform.getTxtSuffixTo()).getTime()));
                } else {
                    pst.setTimestamp(9, null);
                }
                if (lform.getTxtPrefixFrom() != null && !lform.getTxtPrefixFrom().equals("")) {
                    pst.setTimestamp(10, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(lform.getTxtPrefixFrom()).getTime()));
                } else {
                    pst.setTimestamp(10, null);
                }
                if (lform.getTxtPrefixTo() != null && !lform.getTxtPrefixTo().equals("")) {
                    pst.setTimestamp(11, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(lform.getTxtPrefixTo()).getTime()));
                } else {
                    pst.setTimestamp(11, null);
                }
            } else {
                pst.setTimestamp(8, null);
                pst.setTimestamp(9, null);
                pst.setTimestamp(10, null);
                pst.setTimestamp(11, null);
            }

            if (lform.getOrdType().equals("01")) {
                pst.setString(12, null);
                pst.setString(13, null);
            } else {
                pst.setString(12, lform.getSltFromYear());
                pst.setString(13, lform.getSltToYear());
            }
            pst.setInt(14, days);
            if (lform.getOrdType().equals("01")) {
                if (lform.getChkMedicalCertificate() != null && !lform.getChkMedicalCertificate().equals("")) {
                    pst.setString(15, lform.getChkMedicalCertificate());
                } else {
                    pst.setString(15, "N");
                }
                if (lform.getChkCommuted() != null && !lform.getChkCommuted().equals("")) {
                    pst.setString(16, lform.getChkCommuted());
                } else {
                    pst.setString(16, "N");
                }
                if (lform.getChkIfLongTermBasis() != null && !lform.getChkIfLongTermBasis().equals("")) {
                    pst.setString(17, lform.getChkIfLongTermBasis());
                } else {
                    pst.setString(17, "N");
                }
            } else {
                pst.setString(15, "N");
                pst.setString(16, "N");
                pst.setString(17, "N");
            }
            if (lform.getTxtJoinTimeFrom() != null && !lform.getTxtJoinTimeFrom().equals("")) {
                pst.setTimestamp(18, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(lform.getTxtJoinTimeFrom()).getTime()));
            } else {
                pst.setTimestamp(18, null);
            }
            if (lform.getTxtJoinTimeTo() != null && !lform.getTxtJoinTimeTo().equals("")) {
                pst.setTimestamp(19, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(lform.getTxtJoinTimeTo()).getTime()));
            } else {
                pst.setTimestamp(19, null);
            }

            int maxLeaveId = 0;
            int ret = pst.executeUpdate();
            rs = pst.getGeneratedKeys();
            rs.next();
            maxLeaveId = rs.getInt("leaveid");

            String status = "";

            if (lform.getOrdType().equals("01")) {
                if (isPeriodSubsetInAbs || isPeriodSupersetInAbs) {
                    status = "SANCTIONED AND AVAILED";
                } else if (isPeriodFrmOrToInAbs) {
                    status = "SANCTIONED";
                } else if (lform.getChkIfLongTermBasis() != null && !lform.getChkIfLongTermBasis().equals("")) {
                    status = "SANCTIONED AND AVAILED";
                } else {
                    status = "SANCTIONED";
                }
            } else if (lform.getOrdType().equals("02")) {
                status = "SANCTIONED";
            }

            DataBaseFunctions.closeSqlObjects(rs, pst);

            String sbLanguage = sbDAO.getLeaveSanctionDetails(maxLeaveId, lform.getEmpid(), lform.getHidSanctioningOthSpc());
            pst = con.prepareStatement("UPDATE EMP_NOTIFICATION SET STATUS=? , SB_DESCRIPTION=? WHERE EMP_ID=? AND NOT_ID=?");
            pst.setString(1, status);
            pst.setString(2, sbLanguage);
            pst.setString(3, lform.getEmpid());
            pst.setInt(4, notId);
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void updateLeaveSanction(LeaveSanctionForm lform) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        int days = 0;

        boolean isPeriodSubsetInAbs = false;
        boolean isPeriodSupersetInAbs = false;
        boolean isPeriodFrmOrToInAbs = false;
        try {
            con = this.dataSource.getConnection();

            if ((lform.getTxtFrmDt() != null && !lform.getTxtFrmDt().equals("")) && (lform.getTxtToDt() != null && !lform.getTxtToDt().equals(""))) {
                isPeriodSubsetInAbs = ifPeriodSubsetInAbsentee(lform.getEmpid(), lform.getTxtFrmDt(), lform.getTxtToDt());
                isPeriodSupersetInAbs = ifPeriodSupersetInAbsentee(lform.getEmpid(), lform.getTxtFrmDt(), lform.getTxtToDt());
                isPeriodFrmOrToInAbs = ifPeriodFromOrToExistInAbsentee(lform.getEmpid(), lform.getTxtFrmDt(), lform.getTxtToDt());
            }
            if (lform.getOrdType().equals("01")) {
                if ((lform.getTxtFrmDt() != null && !lform.getTxtFrmDt().equals("")) && (lform.getTxtToDt() != null && !lform.getTxtToDt().equals(""))) {
                    if (isPeriodSupersetInAbs || isPeriodFrmOrToInAbs) {
                        days = calculateNoOfDaysInAbsentee(lform.getEmpid(), lform.getTxtFrmDt(), lform.getTxtToDt());
                    } else {
                        days = dateDiffrenceBetween(lform.getTxtFrmDt(), lform.getTxtToDt());
                    }
                }
                if (days > 0) {
                    days = days + 1;
                }
            }

            String sql = "UPDATE EMP_LEAVE SET TOL_ID=?, FDATE=?, TDATE=?, SUFFIX_FROM=?, SUFFIX_DATE=?, PREFIX_DATE=?, PREFIX_TO=?, S_YEAR=?, T_YEAR=?, S_DAYS=?,IF_MEDICAL=?, IF_COMMUTED=?,IF_LONGTERM=?,JOIN_TIME_FROM=?,JOIN_TIME_TO=? WHERE EMP_ID=? AND LEAVEID=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, lform.getSltLeaveType());
            if (lform.getTxtFrmDt() != null && !lform.getTxtFrmDt().equals("")) {
                pst.setTimestamp(2, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(lform.getTxtFrmDt()).getTime()));
            } else {
                pst.setTimestamp(2, null);
            }
            if (lform.getTxtToDt() != null && !lform.getTxtToDt().equals("")) {
                pst.setTimestamp(3, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(lform.getTxtToDt()).getTime()));
            } else {
                pst.setTimestamp(3, null);
            }
            if (lform.getOrdType().equals("01")) {
                if (lform.getTxtSuffixFrom() != null && !lform.getTxtSuffixFrom().equals("")) {
                    pst.setTimestamp(4, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(lform.getTxtSuffixFrom()).getTime()));
                } else {
                    pst.setTimestamp(4, null);
                }
                if (lform.getTxtSuffixTo() != null && !lform.getTxtSuffixTo().equals("")) {
                    pst.setTimestamp(5, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(lform.getTxtSuffixTo()).getTime()));
                } else {
                    pst.setTimestamp(5, null);
                }
                if (lform.getTxtPrefixFrom() != null && !lform.getTxtPrefixFrom().equals("")) {
                    pst.setTimestamp(6, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(lform.getTxtPrefixFrom()).getTime()));
                } else {
                    pst.setTimestamp(6, null);
                }
                if (lform.getTxtPrefixTo() != null && !lform.getTxtPrefixTo().equals("")) {
                    pst.setTimestamp(7, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(lform.getTxtPrefixTo()).getTime()));
                } else {
                    pst.setTimestamp(7, null);
                }
            } else {
                pst.setTimestamp(4, null);
                pst.setTimestamp(5, null);
                pst.setTimestamp(6, null);
                pst.setTimestamp(7, null);
            }
            if (lform.getOrdType().equals("01")) {
                pst.setString(8, null);
                pst.setString(9, null);
            } else {
                pst.setString(8, lform.getSltFromYear());
                pst.setString(9, lform.getSltToYear());
            }

            if (lform.getOrdType().equals("01")) {
                pst.setInt(10, days);
            } else {
                pst.setInt(10, Integer.parseInt(lform.getTxtDays()));
            }

            if (lform.getOrdType().equals("01")) {
                if (lform.getChkMedicalCertificate() != null && !lform.getChkMedicalCertificate().equals("")) {
                    pst.setString(11, lform.getChkMedicalCertificate());
                } else {
                    pst.setString(11, "N");
                }
                if (lform.getChkCommuted() != null && !lform.getChkCommuted().equals("")) {
                    pst.setString(12, lform.getChkCommuted());
                } else {
                    pst.setString(12, "N");
                }
                if (lform.getChkIfLongTermBasis() != null && !lform.getChkIfLongTermBasis().equals("")) {
                    pst.setString(13, lform.getChkIfLongTermBasis());
                } else {
                    pst.setString(13, "N");
                }
            } else {
                pst.setString(11, "N");
                pst.setString(12, "N");
                pst.setString(13, "N");
            }
            if (lform.getTxtJoinTimeFrom() != null && !lform.getTxtJoinTimeFrom().equals("")) {
                pst.setTimestamp(14, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(lform.getTxtJoinTimeFrom()).getTime()));
            } else {
                pst.setTimestamp(14, null);
            }
            if (lform.getTxtJoinTimeTo() != null && !lform.getTxtJoinTimeTo().equals("")) {
                pst.setTimestamp(15, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(lform.getTxtJoinTimeTo()).getTime()));
            } else {
                pst.setTimestamp(15, null);
            }
            pst.setString(16, lform.getEmpid());
            pst.setInt(17, Integer.parseInt(lform.getHleaveId()));
            pst.executeUpdate();

            String status = "";
            System.out.println(lform.getOrdType() + "**" + isPeriodSubsetInAbs + "**" + isPeriodSupersetInAbs + "**" + isPeriodFrmOrToInAbs);
            if (lform.getOrdType().equals("01")) {
                if (isPeriodSubsetInAbs || isPeriodSupersetInAbs) {
                    status = "SANCTIONED AND AVAILED";
                } else if (isPeriodFrmOrToInAbs) {
                    status = "SANCTIONED";
                } else if (lform.getChkIfLongTermBasis() != null && !lform.getChkIfLongTermBasis().equals("")) {
                    status = "SANCTIONED AND AVAILED";
                } else {
                    status = "SANCTIONED";
                }
            } else if (lform.getOrdType().equals("02")) {
                status = "SANCTIONED";
            }

            DataBaseFunctions.closeSqlObjects(rs, pst);

            String sbLanguage = sbDAO.getLeaveSanctionDetails(Integer.parseInt(lform.getHleaveId()), lform.getEmpid(), lform.getHidSanctioningOthSpc());
            sql = "UPDATE EMP_NOTIFICATION SET STATUS=? , SB_DESCRIPTION=? WHERE EMP_ID=? AND NOT_ID=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, status);
            pst.setString(2, sbLanguage);
            pst.setString(3, lform.getEmpid());
            pst.setInt(4, lform.getHnotid());
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    private boolean ifPeriodSubsetInAbsentee(String empid, String frmDt, String toDt) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        boolean status = false;
        try {
            con = this.dataSource.getConnection();

            String sql = "select * from EMP_ABSENTEE where EMP_ID=? AND (ABS_FROM<=? AND ABS_TO>=?)";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            if (frmDt != null && !frmDt.equals("")) {
                pst.setTimestamp(2, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(frmDt).getTime()));
            } else {
                pst.setTimestamp(2, null);
            }
            if (toDt != null && !toDt.equals("")) {
                pst.setTimestamp(3, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(toDt).getTime()));
            } else {
                pst.setTimestamp(3, null);
            }
            rs = pst.executeQuery();
            if (rs.next()) {
                status = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return status;
    }

    private boolean ifPeriodSupersetInAbsentee(String empId, String frmDt, String toDt) throws Exception {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        boolean status = false;
        try {
            con = this.dataSource.getConnection();

            String sql = "select * from EMP_ABSENTEE where EMP_ID=? AND ((ABS_FROM > ? AND ABS_TO <= ?) OR (ABS_FROM >= ? AND ABS_TO < ?))";
            pst = con.prepareStatement(sql);
            pst.setString(1, empId);
            if (frmDt != null && !frmDt.equals("")) {
                pst.setTimestamp(2, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(frmDt).getTime()));
            } else {
                pst.setTimestamp(2, null);
            }
            if (toDt != null && !toDt.equals("")) {
                pst.setTimestamp(3, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(toDt).getTime()));
            } else {
                pst.setTimestamp(3, null);
            }
            if (frmDt != null && !frmDt.equals("")) {
                pst.setTimestamp(4, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(frmDt).getTime()));
            } else {
                pst.setTimestamp(4, null);
            }
            if (toDt != null && !toDt.equals("")) {
                pst.setTimestamp(5, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(toDt).getTime()));
            } else {
                pst.setTimestamp(5, null);
            }
            rs = pst.executeQuery();
            if (rs.next()) {
                status = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return status;
    }

    private boolean ifPeriodFromOrToExistInAbsentee(String empId, String frmDt, String toDate) throws Exception {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        boolean status = false;
        try {
            con = this.dataSource.getConnection();
            String sql = "select * from EMP_ABSENTEE where EMP_ID=? AND (((? BETWEEN ABS_FROM AND ABS_TO) AND ABS_TO < ?) OR (ABS_FROM > ? AND (? BETWEEN ABS_FROM AND ABS_TO)))";
            pst = con.prepareStatement(sql);
            pst.setString(1, empId);
            if (frmDt != null && !frmDt.equals("")) {
                pst.setTimestamp(2, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(frmDt).getTime()));
            } else {
                pst.setTimestamp(2, null);
            }
            if (toDate != null && !toDate.equals("")) {
                pst.setTimestamp(3, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(toDate).getTime()));
            } else {
                pst.setTimestamp(3, null);
            }

            if (frmDt != null && !frmDt.equals("")) {
                pst.setTimestamp(4, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(frmDt).getTime()));
            } else {
                pst.setTimestamp(4, null);
            }
            if (toDate != null && !toDate.equals("")) {
                pst.setTimestamp(5, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(toDate).getTime()));
            } else {
                pst.setTimestamp(5, null);
            }
            rs = pst.executeQuery();
            if (rs.next()) {
                status = true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return status;
    }

    private int calculateNoOfDaysInAbsentee(String empid, String frmDt, String tDate) throws Exception {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        int days = -1;
        String absFdate = null;
        String absTdate = null;
        try {
            con = this.dataSource.getConnection();

            String sql = "select ABS_FROM,ABS_TO from EMP_ABSENTEE where EMP_ID=? AND ( (( ((? BETWEEN ABS_FROM AND ABS_TO) AND ABS_TO < ?) OR (ABS_FROM > ? AND (? BETWEEN ABS_FROM AND ABS_TO)) )) OR ((ABS_FROM > ? AND ABS_TO <= ?) OR (ABS_FROM >= ? AND ABS_TO < ?)))";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            if (frmDt != null && !frmDt.equals("")) {
                pst.setTimestamp(2, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(frmDt).getTime()));
            } else {
                pst.setTimestamp(2, null);
            }
            if (tDate != null && !tDate.equals("")) {
                pst.setTimestamp(3, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(tDate).getTime()));
            } else {
                pst.setTimestamp(3, null);
            }
            if (frmDt != null && !frmDt.equals("")) {
                pst.setTimestamp(4, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(frmDt).getTime()));
            } else {
                pst.setTimestamp(4, null);
            }
            if (tDate != null && !tDate.equals("")) {
                pst.setTimestamp(5, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(tDate).getTime()));
            } else {
                pst.setTimestamp(5, null);
            }
            if (frmDt != null && !frmDt.equals("")) {
                pst.setTimestamp(6, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(frmDt).getTime()));
            } else {
                pst.setTimestamp(6, null);
            }
            if (tDate != null && !tDate.equals("")) {
                pst.setTimestamp(7, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(tDate).getTime()));
            } else {
                pst.setTimestamp(7, null);
            }
            if (frmDt != null && !frmDt.equals("")) {
                pst.setTimestamp(8, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(frmDt).getTime()));
            } else {
                pst.setTimestamp(8, null);
            }
            if (tDate != null && !tDate.equals("")) {
                pst.setTimestamp(9, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(tDate).getTime()));
            } else {
                pst.setTimestamp(9, null);
            }
            rs = pst.executeQuery();
            if (rs.next()) {
                absFdate = rs.getString("ABS_FROM");
                absTdate = rs.getString("ABS_TO");
            }

            days = dateDiffrenceBetween(absFdate, absTdate);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return days;
    }

    private int dateDiffrenceBetween(String frmdate, String todt) throws Exception {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        int days = -1;
        try {
            con = this.dataSource.getConnection();

            String sql = "SELECT ?::date  - ?::date diff";
            pst = con.prepareStatement(sql);
            if (todt != null && !todt.equals("")) {
                pst.setTimestamp(1, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(todt).getTime()));
            } else {
                pst.setTimestamp(1, null);
            }
            if (frmdate != null && !frmdate.equals("")) {
                pst.setTimestamp(2, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(frmdate).getTime()));
            } else {
                pst.setTimestamp(2, null);
            }
            rs = pst.executeQuery();
            if (rs.next()) {
                if (rs.getString("DIFF") != null && !rs.getString("DIFF").equals("")) {
                    days = Integer.parseInt(rs.getString("DIFF"));
                } else {
                    days = -1;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return days;
    }

    @Override
    public LeaveSanctionForm editLeaveSanction(String empid, int notid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        LeaveSanctionForm lform = null;
        try {
            con = this.dataSource.getConnection();

            String sql = "select organization_type,s_year,t_year,s_days,leaveid,emp_notification.not_id,doe,ordno,orddt,dept_code,off_code,auth,tol_id,fdate,tdate,suffix_from,suffix_date,prefix_date,prefix_to,join_time_from,"
                    + " join_time_to,if_medical,if_commuted,if_longterm,note,if_visible from emp_notification"
                    + " inner join emp_leave on emp_notification.not_id=emp_leave.not_id"
                    + " where emp_notification.not_id=? and emp_notification.emp_id=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, notid);
            pst.setString(2, empid);
            rs = pst.executeQuery();
            if (rs.next()) {
                lform = new LeaveSanctionForm();
                lform.setHnotid(rs.getInt("not_id"));
                lform.setHleaveId(rs.getString("leaveid"));

                lform.setOrdType("01");

                lform.setTxtNotOrdNo(rs.getString("ordno"));
                lform.setTxtNotOrdDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("orddt")));

                lform.setHidSanctioningDeptCode(rs.getString("dept_code"));
                lform.setHidSanctioningOffCode(rs.getString("off_code"));
                if (lform.getHidSanctioningDeptCode() != null && !lform.getHidSanctioningDeptCode().equals("") && lform.getHidSanctioningDeptCode().equals("LM")) {
                    lform.setSanctioningPostName(CommonFunctions.getLMOfficiating(con, rs.getString("off_code")));
                } else {
                    lform.setSanctioningSpc(rs.getString("auth"));
                    lform.setSanctioningPostName(CommonFunctions.getSPN(con, rs.getString("auth")));
                }
                lform.setSltLeaveType(rs.getString("tol_id"));

                lform.setTxtFrmDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("fdate")));
                lform.setTxtToDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("tdate")));
                lform.setTxtPrefixFrom(CommonFunctions.getFormattedOutputDate1(rs.getDate("prefix_date")));
                lform.setTxtPrefixTo(CommonFunctions.getFormattedOutputDate1(rs.getDate("prefix_to")));
                lform.setTxtSuffixFrom(CommonFunctions.getFormattedOutputDate1(rs.getDate("suffix_from")));
                lform.setTxtSuffixTo(CommonFunctions.getFormattedOutputDate1(rs.getDate("suffix_date")));
                lform.setTxtJoinTimeFrom(CommonFunctions.getFormattedOutputDate1(rs.getDate("join_time_from")));
                lform.setTxtJoinTimeTo(CommonFunctions.getFormattedOutputDate1(rs.getDate("join_time_to")));
                lform.setChkMedicalCertificate(rs.getString("if_medical"));
                lform.setChkCommuted(rs.getString("if_commuted"));
                lform.setChkIfLongTermBasis(rs.getString("if_longterm"));

                lform.setSltFromYear(rs.getString("s_year"));
                lform.setSltToYear(rs.getString("t_year"));
                lform.setTxtDays(rs.getString("s_days"));

                lform.setNote(rs.getString("note"));

                lform.setRadsanctioningauthtype(rs.getString("organization_type"));

                if (lform.getRadsanctioningauthtype() != null && lform.getRadsanctioningauthtype().equals("GOI")) {
                    lform.setSanctioningPostName(getOtherSpn(rs.getString("auth")));
                    lform.setHidSanctioningOthSpc(rs.getString("auth"));
                } else {
                    lform.setRadsanctioningauthtype("GOO");
                }
                if (rs.getString("if_visible") != null && rs.getString("if_visible").equals("N")) {
                    lform.setChkNotSBPrint("Y");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return lform;
    }

    @Override
    public boolean deleteLeaveSanction(String empId, String leaveId) {
        Connection con = null;
        PreparedStatement pst = null;
        try {
            con = this.dataSource.getConnection();

            pst = con.prepareStatement("delete from emp_leave where emp_id=? and leaveid=?");
            pst.setString(1, empId);
            pst.setInt(2, Integer.parseInt(leaveId));
            pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return true;
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

    @Override
    public void updateCancellationNotificationData(int notid, int linknotid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = this.dataSource.getConnection();

            String sql = "update emp_notification set iscanceled='Y',link_id=? where not_id=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, linknotid);
            pst.setInt(2, notid);
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void saveCancelLeaveSanction(NotificationBean nb, int notId, String othspc) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = this.dataSource.getConnection();
            nb.setNottype("LEAVE");
            String sbCancelLanguage = sbDAO.getCancelLangDetails(nb, notId, othspc);
            pst = con.prepareStatement("UPDATE EMP_NOTIFICATION SET SB_DESCRIPTION=? WHERE EMP_ID=? AND NOT_ID=?");
            pst.setString(1, sbCancelLanguage);
            pst.setString(2, nb.getEmpId());
            pst.setInt(3, nb.getNotid());
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public LeaveSanctionForm editCancelLeaveSanction(String empid, int notid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        LeaveSanctionForm lform = null;
        try {
            con = this.dataSource.getConnection();

            /*String sql = "select organization_type,emp_notification.not_id,doe,ordno,orddt,dept_code,off_code,auth,note,if_visible from emp_notification"
             + " where emp_notification.not_id=? and emp_notification.emp_id=?";*/
            String sql = "select e1.organization_type,e1.not_id cancelid,e1.doe,e1.ordno,e1.orddt,e1.dept_code,e1.off_code,e1.auth,e1.note,e1.if_visible,e2.not_id notid"
                    + " from emp_notification e1"
                    + " inner join emp_notification e2 on e1.not_id=e2.link_id"
                    + " where e1.not_id=? and e1.emp_id=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, notid);
            pst.setString(2, empid);
            rs = pst.executeQuery();
            if (rs.next()) {
                lform = new LeaveSanctionForm();
                lform.setHnotid(rs.getInt("cancelid"));
                lform.setLinkid(rs.getInt("notid") + "");
                lform.setOrdType("01");

                lform.setTxtNotOrdNo(rs.getString("ordno"));
                lform.setTxtNotOrdDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("orddt")));

                lform.setHidSanctioningDeptCode(rs.getString("dept_code"));
                lform.setHidSanctioningOffCode(rs.getString("off_code"));
                if (lform.getHidSanctioningDeptCode() != null && !lform.getHidSanctioningDeptCode().equals("") && lform.getHidSanctioningDeptCode().equals("LM")) {
                    lform.setSanctioningPostName(CommonFunctions.getLMOfficiating(con, rs.getString("off_code")));
                } else {
                    lform.setSanctioningSpc(rs.getString("auth"));
                    lform.setSanctioningPostName(CommonFunctions.getSPN(con, rs.getString("auth")));
                }
                lform.setNote(rs.getString("note"));

                lform.setRadsanctioningauthtype(rs.getString("organization_type"));

                if (lform.getRadsanctioningauthtype() != null && lform.getRadsanctioningauthtype().equals("GOI")) {
                    lform.setSanctioningPostName(getOtherSpn(rs.getString("auth")));
                    lform.setHidSanctioningOthSpc(rs.getString("auth"));
                } else {
                    lform.setRadsanctioningauthtype("GOO");
                }
                if (rs.getString("if_visible") != null && rs.getString("if_visible").equals("N")) {
                    lform.setChkNotSBPrint("Y");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return lform;

    }

    @Override
    public void updateSupersedeNotificationData(int notid, int linkid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = this.dataSource.getConnection();

            String sql = "update emp_notification set iscanceled='Y',link_id=? where not_id=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, linkid);
            pst.setInt(2, notid);
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    @Override
    public void saveSupersedeLeaveSanction(LeaveSanctionForm lform, int notId) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        int days = 0;

        boolean isPeriodSubsetInAbs = false;
        boolean isPeriodSupersetInAbs = false;
        boolean isPeriodFrmOrToInAbs = false;
        try {
            con = this.dataSource.getConnection();

            if ((lform.getTxtFrmDt() != null && !lform.getTxtFrmDt().equals("")) && (lform.getTxtToDt() != null && !lform.getTxtToDt().equals(""))) {

                isPeriodSubsetInAbs = ifPeriodSubsetInAbsentee(lform.getEmpid(), lform.getTxtFrmDt(), lform.getTxtToDt());
                isPeriodSupersetInAbs = ifPeriodSupersetInAbsentee(lform.getEmpid(), lform.getTxtFrmDt(), lform.getTxtToDt());
                isPeriodFrmOrToInAbs = ifPeriodFromOrToExistInAbsentee(lform.getEmpid(), lform.getTxtFrmDt(), lform.getTxtToDt());
            }
            if (lform.getOrdType().equals("01")) {
                if ((lform.getTxtFrmDt() != null && !lform.getTxtFrmDt().equals("")) && (lform.getTxtToDt() != null && !lform.getTxtToDt().equals(""))) {
                    if (isPeriodSupersetInAbs || isPeriodFrmOrToInAbs) {
                        days = calculateNoOfDaysInAbsentee(lform.getEmpid(), lform.getTxtFrmDt(), lform.getTxtToDt());
                    } else {
                        days = dateDiffrenceBetween(lform.getTxtFrmDt(), lform.getTxtToDt());
                    }
                }
                if (days > 0) {
                    days = days + 1;
                }
            } else if (lform.getOrdType().equals("02")) {
                if (lform.getTxtDays() != null && !lform.getTxtDays().equals("")) {
                    days = Integer.parseInt(lform.getTxtDays());
                }
            }

            pst = con.prepareStatement("INSERT INTO EMP_LEAVE(NOT_ID, NOT_TYPE, EMP_ID, LSOT_ID, TOL_ID, FDATE, TDATE, SUFFIX_FROM, SUFFIX_DATE, PREFIX_DATE, PREFIX_TO, S_YEAR, T_YEAR, S_DAYS,IF_MEDICAL, IF_COMMUTED,IF_LONGTERM,JOIN_TIME_FROM,JOIN_TIME_TO) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            pst.setInt(1, notId);
            pst.setString(2, "LEAVE");
            pst.setString(3, lform.getEmpid());
            pst.setString(4, lform.getOrdType());
            pst.setString(5, lform.getSltLeaveType());
            if (lform.getTxtFrmDt() != null && !lform.getTxtFrmDt().equals("")) {
                pst.setTimestamp(6, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(lform.getTxtFrmDt()).getTime()));
            } else {
                pst.setTimestamp(6, null);
            }
            if (lform.getTxtToDt() != null && !lform.getTxtToDt().equals("")) {
                pst.setTimestamp(7, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(lform.getTxtToDt()).getTime()));
            } else {
                pst.setTimestamp(7, null);
            }
            if (lform.getOrdType().equals("01")) {
                if (lform.getTxtSuffixFrom() != null && !lform.getTxtSuffixFrom().equals("")) {
                    pst.setTimestamp(8, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(lform.getTxtSuffixFrom()).getTime()));
                } else {
                    pst.setTimestamp(8, null);
                }
                if (lform.getTxtSuffixTo() != null && !lform.getTxtSuffixTo().equals("")) {
                    pst.setTimestamp(9, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(lform.getTxtSuffixTo()).getTime()));
                } else {
                    pst.setTimestamp(9, null);
                }
                if (lform.getTxtPrefixFrom() != null && !lform.getTxtPrefixFrom().equals("")) {
                    pst.setTimestamp(10, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(lform.getTxtPrefixFrom()).getTime()));
                } else {
                    pst.setTimestamp(10, null);
                }
                if (lform.getTxtPrefixTo() != null && !lform.getTxtPrefixTo().equals("")) {
                    pst.setTimestamp(11, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(lform.getTxtPrefixTo()).getTime()));
                } else {
                    pst.setTimestamp(11, null);
                }
            } else {
                pst.setTimestamp(8, null);
                pst.setTimestamp(9, null);
                pst.setTimestamp(10, null);
                pst.setTimestamp(11, null);
            }

            if (lform.getOrdType().equals("01")) {
                pst.setString(12, null);
                pst.setString(13, null);
            } else {
                pst.setString(12, lform.getSltFromYear());
                pst.setString(13, lform.getSltToYear());
            }
            pst.setInt(14, days);
            if (lform.getOrdType().equals("01")) {
                if (lform.getChkMedicalCertificate() != null && !lform.getChkMedicalCertificate().equals("")) {
                    pst.setString(15, lform.getChkMedicalCertificate());
                } else {
                    pst.setString(15, "N");
                }
                if (lform.getChkCommuted() != null && !lform.getChkCommuted().equals("")) {
                    pst.setString(16, lform.getChkCommuted());
                } else {
                    pst.setString(16, "N");
                }
                if (lform.getChkIfLongTermBasis() != null && !lform.getChkIfLongTermBasis().equals("")) {
                    pst.setString(17, lform.getChkIfLongTermBasis());
                } else {
                    pst.setString(17, "N");
                }
            } else {
                pst.setString(15, "N");
                pst.setString(16, "N");
                pst.setString(17, "N");
            }
            if (lform.getTxtJoinTimeFrom() != null && !lform.getTxtJoinTimeFrom().equals("")) {
                pst.setTimestamp(18, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(lform.getTxtJoinTimeFrom()).getTime()));
            } else {
                pst.setTimestamp(18, null);
            }
            if (lform.getTxtJoinTimeTo() != null && !lform.getTxtJoinTimeTo().equals("")) {
                pst.setTimestamp(19, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(lform.getTxtJoinTimeTo()).getTime()));
            } else {
                pst.setTimestamp(19, null);
            }

            int maxLeaveId = 0;
            int ret = pst.executeUpdate();
            rs = pst.getGeneratedKeys();
            rs.next();
            maxLeaveId = rs.getInt("leaveid");

            String status = "";

            if (lform.getOrdType().equals("01")) {
                if (isPeriodSubsetInAbs || isPeriodSupersetInAbs) {
                    status = "SANCTIONED AND AVAILED";
                } else if (isPeriodFrmOrToInAbs) {
                    status = "SANCTIONED";
                } else if (lform.getChkIfLongTermBasis() != null && !lform.getChkIfLongTermBasis().equals("")) {
                    status = "SANCTIONED AND AVAILED";
                } else {
                    status = "SANCTIONED";
                }
            } else if (lform.getOrdType().equals("02")) {
                status = "SANCTIONED";
            }

            DataBaseFunctions.closeSqlObjects(rs, pst);

            String sbLanguage = sbDAO.getSupersedeLangDetails(Integer.parseInt(lform.getLinkid()),lform.getHidSanctioningOthSpc());
            pst = con.prepareStatement("UPDATE EMP_NOTIFICATION SET SB_DESCRIPTION=? WHERE EMP_ID=? AND NOT_ID=?");
            pst.setString(1, sbLanguage);
            pst.setString(2, lform.getEmpid());
            pst.setInt(3, notId);
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void updateSupersedeLeaveSanction(LeaveSanctionForm lform) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        int days = 0;

        boolean isPeriodSubsetInAbs = false;
        boolean isPeriodSupersetInAbs = false;
        boolean isPeriodFrmOrToInAbs = false;
        try {
            con = this.dataSource.getConnection();

            if ((lform.getTxtFrmDt() != null && !lform.getTxtFrmDt().equals("")) && (lform.getTxtToDt() != null && !lform.getTxtToDt().equals(""))) {
                isPeriodSubsetInAbs = ifPeriodSubsetInAbsentee(lform.getEmpid(), lform.getTxtFrmDt(), lform.getTxtToDt());
                isPeriodSupersetInAbs = ifPeriodSupersetInAbsentee(lform.getEmpid(), lform.getTxtFrmDt(), lform.getTxtToDt());
                isPeriodFrmOrToInAbs = ifPeriodFromOrToExistInAbsentee(lform.getEmpid(), lform.getTxtFrmDt(), lform.getTxtToDt());
            }
            if (lform.getOrdType().equals("01")) {
                if ((lform.getTxtFrmDt() != null && !lform.getTxtFrmDt().equals("")) && (lform.getTxtToDt() != null && !lform.getTxtToDt().equals(""))) {
                    if (isPeriodSupersetInAbs || isPeriodFrmOrToInAbs) {
                        days = calculateNoOfDaysInAbsentee(lform.getEmpid(), lform.getTxtFrmDt(), lform.getTxtToDt());
                    } else {
                        days = dateDiffrenceBetween(lform.getTxtFrmDt(), lform.getTxtToDt());
                    }
                }
                if (days > 0) {
                    days = days + 1;
                }
            }

            String sql = "UPDATE EMP_LEAVE SET TOL_ID=?, FDATE=?, TDATE=?, SUFFIX_FROM=?, SUFFIX_DATE=?, PREFIX_DATE=?, PREFIX_TO=?, S_YEAR=?, T_YEAR=?, S_DAYS=?,IF_MEDICAL=?, IF_COMMUTED=?,IF_LONGTERM=?,JOIN_TIME_FROM=?,JOIN_TIME_TO=? WHERE EMP_ID=? AND LEAVEID=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, lform.getSltLeaveType());
            if (lform.getTxtFrmDt() != null && !lform.getTxtFrmDt().equals("")) {
                pst.setTimestamp(2, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(lform.getTxtFrmDt()).getTime()));
            } else {
                pst.setTimestamp(2, null);
            }
            if (lform.getTxtToDt() != null && !lform.getTxtToDt().equals("")) {
                pst.setTimestamp(3, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(lform.getTxtToDt()).getTime()));
            } else {
                pst.setTimestamp(3, null);
            }
            if (lform.getOrdType().equals("01")) {
                if (lform.getTxtSuffixFrom() != null && !lform.getTxtSuffixFrom().equals("")) {
                    pst.setTimestamp(4, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(lform.getTxtSuffixFrom()).getTime()));
                } else {
                    pst.setTimestamp(4, null);
                }
                if (lform.getTxtSuffixTo() != null && !lform.getTxtSuffixTo().equals("")) {
                    pst.setTimestamp(5, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(lform.getTxtSuffixTo()).getTime()));
                } else {
                    pst.setTimestamp(5, null);
                }
                if (lform.getTxtPrefixFrom() != null && !lform.getTxtPrefixFrom().equals("")) {
                    pst.setTimestamp(6, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(lform.getTxtPrefixFrom()).getTime()));
                } else {
                    pst.setTimestamp(6, null);
                }
                if (lform.getTxtPrefixTo() != null && !lform.getTxtPrefixTo().equals("")) {
                    pst.setTimestamp(7, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(lform.getTxtPrefixTo()).getTime()));
                } else {
                    pst.setTimestamp(7, null);
                }
            } else {
                pst.setTimestamp(4, null);
                pst.setTimestamp(5, null);
                pst.setTimestamp(6, null);
                pst.setTimestamp(7, null);
            }
            if (lform.getOrdType().equals("01")) {
                pst.setString(8, null);
                pst.setString(9, null);
            } else {
                pst.setString(8, lform.getSltFromYear());
                pst.setString(9, lform.getSltToYear());
            }

            if (lform.getOrdType().equals("01")) {
                pst.setInt(10, days);
            } else {
                pst.setInt(10, Integer.parseInt(lform.getTxtDays()));
            }

            if (lform.getOrdType().equals("01")) {
                if (lform.getChkMedicalCertificate() != null && !lform.getChkMedicalCertificate().equals("")) {
                    pst.setString(11, lform.getChkMedicalCertificate());
                } else {
                    pst.setString(11, "N");
                }
                if (lform.getChkCommuted() != null && !lform.getChkCommuted().equals("")) {
                    pst.setString(12, lform.getChkCommuted());
                } else {
                    pst.setString(12, "N");
                }
                if (lform.getChkIfLongTermBasis() != null && !lform.getChkIfLongTermBasis().equals("")) {
                    pst.setString(13, lform.getChkIfLongTermBasis());
                } else {
                    pst.setString(13, "N");
                }
            } else {
                pst.setString(11, "N");
                pst.setString(12, "N");
                pst.setString(13, "N");
            }
            if (lform.getTxtJoinTimeFrom() != null && !lform.getTxtJoinTimeFrom().equals("")) {
                pst.setTimestamp(14, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(lform.getTxtJoinTimeFrom()).getTime()));
            } else {
                pst.setTimestamp(14, null);
            }
            if (lform.getTxtJoinTimeTo() != null && !lform.getTxtJoinTimeTo().equals("")) {
                pst.setTimestamp(15, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(lform.getTxtJoinTimeTo()).getTime()));
            } else {
                pst.setTimestamp(15, null);
            }
            pst.setString(16, lform.getEmpid());
            pst.setInt(17, Integer.parseInt(lform.getHleaveId()));
            pst.executeUpdate();

            String status = "";
            System.out.println(lform.getOrdType() + "**" + isPeriodSubsetInAbs + "**" + isPeriodSupersetInAbs + "**" + isPeriodFrmOrToInAbs);
            if (lform.getOrdType().equals("01")) {
                if (isPeriodSubsetInAbs || isPeriodSupersetInAbs) {
                    status = "SANCTIONED AND AVAILED";
                } else if (isPeriodFrmOrToInAbs) {
                    status = "SANCTIONED";
                } else if (lform.getChkIfLongTermBasis() != null && !lform.getChkIfLongTermBasis().equals("")) {
                    status = "SANCTIONED AND AVAILED";
                } else {
                    status = "SANCTIONED";
                }
            } else if (lform.getOrdType().equals("02")) {
                status = "SANCTIONED";
            }

            DataBaseFunctions.closeSqlObjects(rs, pst);

            String sbLanguage = sbDAO.getSupersedeLangDetails(Integer.parseInt(lform.getLinkid()),lform.getHidSanctioningOthSpc());
            pst = con.prepareStatement("UPDATE EMP_NOTIFICATION SET SB_DESCRIPTION=? WHERE EMP_ID=? AND NOT_ID=?");
            pst.setString(1, sbLanguage);
            pst.setString(2, lform.getEmpid());
            pst.setInt(3, lform.getHnotid());
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public LeaveSanctionForm editSupersedeLeaveSanction(String empid, int notid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        LeaveSanctionForm lform = null;
        try {
            con = this.dataSource.getConnection();

            String sql = "select e1.organization_type,s_year,t_year,s_days,leaveid,e1.not_id not_id,e1.doe,e1.ordno,e1.orddt,e1.dept_code,e1.off_code,e1.auth,tol_id,fdate,tdate,suffix_from,suffix_date,prefix_date,prefix_to,join_time_from,"
                    + " join_time_to,if_medical,if_commuted,if_longterm,e1.note,e1.if_visible,e2.not_id linkid from emp_notification e1"
                    + " inner join emp_leave on e1.not_id=emp_leave.not_id"
                    + " inner join emp_notification e2 on e1.not_id=e2.link_id"
                    + " where e1.not_id=? and e1.emp_id=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, notid);
            pst.setString(2, empid);
            rs = pst.executeQuery();
            if (rs.next()) {
                lform = new LeaveSanctionForm();
                lform.setHnotid(rs.getInt("not_id"));
                lform.setHleaveId(rs.getString("leaveid"));

                lform.setLinkid(rs.getString("linkid"));

                lform.setOrdType("01");

                lform.setTxtNotOrdNo(rs.getString("ordno"));
                lform.setTxtNotOrdDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("orddt")));

                lform.setHidSanctioningDeptCode(rs.getString("dept_code"));
                lform.setHidSanctioningOffCode(rs.getString("off_code"));
                if (lform.getHidSanctioningDeptCode() != null && !lform.getHidSanctioningDeptCode().equals("") && lform.getHidSanctioningDeptCode().equals("LM")) {
                    lform.setSanctioningPostName(CommonFunctions.getLMOfficiating(con, rs.getString("off_code")));
                } else {
                    lform.setSanctioningSpc(rs.getString("auth"));
                    lform.setSanctioningPostName(CommonFunctions.getSPN(con, rs.getString("auth")));
                }
                lform.setSltLeaveType(rs.getString("tol_id"));

                lform.setTxtFrmDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("fdate")));
                lform.setTxtToDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("tdate")));
                lform.setTxtPrefixFrom(CommonFunctions.getFormattedOutputDate1(rs.getDate("prefix_date")));
                lform.setTxtPrefixTo(CommonFunctions.getFormattedOutputDate1(rs.getDate("prefix_to")));
                lform.setTxtSuffixFrom(CommonFunctions.getFormattedOutputDate1(rs.getDate("suffix_from")));
                lform.setTxtSuffixTo(CommonFunctions.getFormattedOutputDate1(rs.getDate("suffix_date")));
                lform.setTxtJoinTimeFrom(CommonFunctions.getFormattedOutputDate1(rs.getDate("join_time_from")));
                lform.setTxtJoinTimeTo(CommonFunctions.getFormattedOutputDate1(rs.getDate("join_time_to")));
                lform.setChkMedicalCertificate(rs.getString("if_medical"));
                lform.setChkCommuted(rs.getString("if_commuted"));
                lform.setChkIfLongTermBasis(rs.getString("if_longterm"));

                lform.setSltFromYear(rs.getString("s_year"));
                lform.setSltToYear(rs.getString("t_year"));
                lform.setTxtDays(rs.getString("s_days"));

                lform.setNote(rs.getString("note"));

                lform.setRadsanctioningauthtype(rs.getString("organization_type"));

                if (lform.getRadsanctioningauthtype() != null && lform.getRadsanctioningauthtype().equals("GOI")) {
                    lform.setSanctioningPostName(getOtherSpn(rs.getString("auth")));
                    lform.setHidSanctioningOthSpc(rs.getString("auth"));
                } else {
                    lform.setRadsanctioningauthtype("GOO");
                }
                if (rs.getString("if_visible") != null && rs.getString("if_visible").equals("N")) {
                    lform.setChkNotSBPrint("Y");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return lform;

    }
}
