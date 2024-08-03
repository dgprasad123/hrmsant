/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.allowances;

import hrms.SelectOption;
import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.dao.leaveAccount.ServiceBookLanguageDAO;
import hrms.dao.notification.NotificationDAO;
import hrms.model.allowances.AllowanceModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 *
 * @author Madhusmita
 */
public class AllowancesDAOImpl implements AllowancesDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    protected ServiceBookLanguageDAO sbDAO;
    protected NotificationDAO notificationDao;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setNotificationDao(NotificationDAO notificationDao) {
        this.notificationDao = notificationDao;
    }

    public void setSbDAO(ServiceBookLanguageDAO sbDAO) {
        this.sbDAO = sbDAO;
    }

    @Override
    public List getAllowancesList(String emp_id) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        AllowanceModel allowBean = null;
        List allowancesList = new ArrayList();

        try {
            con = this.dataSource.getConnection();
            String qr1 = "select emp_notification.not_id, emp_notification.ordno, emp_notification.orddt, emp_notification.doe,"
                    + "emp_allow_dtls.* from emp_notification\n"
                    + "inner join emp_allow_dtls on emp_notification.not_id=emp_allow_dtls.not_id "
                    + "where emp_notification.emp_id=? and emp_notification.not_type='ALLOWANCES'";
            pst = con.prepareStatement(qr1);
            pst.setString(1, emp_id);
            rs = pst.executeQuery();
            while (rs.next()) {
                allowBean = new AllowanceModel();
                allowBean.setOrdno(rs.getString("ordno"));
                allowBean.setOrdDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("orddt")));
                allowBean.setDoe(CommonFunctions.getFormattedOutputDate1(rs.getDate("doe")));
                allowBean.setNotType(rs.getString("not_type"));
                allowBean.setNotId(rs.getInt("not_id"));
                allowancesList.add(allowBean);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return allowancesList;
    }

    @Override
    public void saveAllowance(String emp_id, String not_id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List getAllowancesDeductionList() {
        Connection con = null;
        ResultSet rs = null;
        Statement stmt = null;
        SelectOption so = null;
        List allowancesDeductions = new ArrayList();
        try {
            con = this.dataSource.getConnection();
            String sql = "select ad_code,ad_desc from g_ad_list  order by ad_desc";
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                AllowanceModel allowbean = new AllowanceModel();
                allowbean.setAllowanceId(rs.getString("ad_code"));
                allowbean.setAllowanceDesc(rs.getString("ad_desc"));
                allowancesDeductions.add(allowbean);

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
           DataBaseFunctions.closeSqlObjects(rs,stmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return allowancesDeductions;
    }

    @Override
    public void insertAllowancesData(AllowanceModel allowBean, int notId) {
        int n = 0;
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        try {
            con = this.dataSource.getConnection();
            String sql = "Insert into emp_allow_dtls(not_type,emp_id,ad_code,amt,wef,weft,not_id) values(?,?,?,?,?,?,?)";
            pst = con.prepareStatement(sql);
            pst.setString(1, "ALLOWANCES");
            pst.setString(2, allowBean.getEmpid());
            pst.setString(3, allowBean.getSltAllowanceCode());
            pst.setDouble(4, Double.parseDouble(allowBean.getAllowanceAmt()));
            pst.setTimestamp(5, new Timestamp(sdf.parse(allowBean.getTxtWEFDt()).getTime()));
            pst.setString(6, allowBean.getSltWEFTime());
            pst.setInt(7, notId);
            pst.executeUpdate();
            String sbLang = sbDAO.getAllowancesDeductionsLangDetails(allowBean);
            notificationDao.saveServiceBookLanguage(sbLang, notId, "ALLOWANCES");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    @Override
    public int updateAllowancesData(AllowanceModel allowBean, int notId) {
        int n = 0;
        PreparedStatement ps = null;
        Connection con = null;
        ResultSet rs = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("update emp_allow_dtls set ad_code=?,amt=?,wef=?,weft=? where not_id=? and emp_id=?");
            ps.setString(1, allowBean.getSltAllowanceCode());
            ps.setDouble(2, Double.parseDouble(allowBean.getAllowanceAmt()));
            ps.setTimestamp(3, new Timestamp(sdf.parse(allowBean.getTxtWEFDt()).getTime()));
            ps.setString(4, allowBean.getSltWEFTime());
            ps.setInt(5, notId);
            ps.setString(6, allowBean.getEmpid());
            ps.executeUpdate();
            String sbLang = sbDAO.getAllowancesDeductionsLangDetails(allowBean);
            notificationDao.saveServiceBookLanguage(sbLang, notId, "ALLOWANCES");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return n;
    }

    @Override
    public AllowanceModel editAllowancesData(int notId, String emp_id) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        AllowanceModel allowBean = null;
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("select emp_notification.note,emp_notification.not_id, emp_notification.ordno,emp_notification.auth,spn,emp_notification.dept_code,emp_notification.off_code, emp_notification.orddt,emp_notification.if_visible,\n"
                    + "emp_notification.doe,emp_allow_dtls.* from emp_notification inner join \n"
                    + "emp_allow_dtls on emp_notification.not_id=emp_allow_dtls.not_id \n"
                    + "left outer join g_spc on emp_notification.auth=g_spc.spc\n"
                    + "where emp_notification.emp_id=?\n"
                    + "and emp_notification.not_id=? and emp_notification.not_type='ALLOWANCES'");
            ps.setString(1, emp_id);
            ps.setInt(2, notId);
            rs = ps.executeQuery();
            if (rs.next()) {
                allowBean = new AllowanceModel();
                allowBean.setNotType("ALLOWANCES");
                allowBean.setEmpid(rs.getString("emp_id"));
                allowBean.setHnotid(rs.getInt("not_id"));
                allowBean.setOrdno(rs.getString("ordno"));
                allowBean.setHidTempNotifyingPost(rs.getString("auth"));
                allowBean.setOrdDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("orddt")));
                allowBean.setNotifyingPostName(CommonFunctions.getSPN(con, rs.getString("auth")));
                allowBean.setHidNotifyingDeptCode(rs.getString("dept_code"));
                allowBean.setHidNotifyingOffCode(rs.getString("off_code"));
                allowBean.setNotifyingSpc(rs.getString("auth"));
                allowBean.setNotifyingPostName(rs.getString("spn"));
                allowBean.setSltAllowanceCode(rs.getString("ad_code"));
                allowBean.setAllowanceAmt(rs.getString("amt"));
                allowBean.setTxtWEFDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("wef")));
                allowBean.setSltWEFTime(rs.getString("weft"));
                allowBean.setNote(rs.getString("note"));
                if (rs.getString("if_visible") != null && rs.getString("if_visible").equals("N")) {
                    allowBean.setChkNotSBPrint("Y");
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
            DataBaseFunctions.closeSqlObjects(rs, ps);
        }
        return allowBean;
    }

}
