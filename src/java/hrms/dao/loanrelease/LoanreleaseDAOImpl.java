/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.loanrelease;

import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.dao.leaveAccount.ServiceBookLanguageDAO;
import hrms.dao.notification.NotificationDAO;
import hrms.model.loanrelease.LoanRelease;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;

public class LoanreleaseDAOImpl implements LoanreleaseDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    protected String uploadPath;
    protected ServiceBookLanguageDAO sbDAO;
    protected NotificationDAO notificationDao;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setUploadPath(String uploadPath) {
        this.uploadPath = uploadPath;
    }

    public void setSbDAO(ServiceBookLanguageDAO sbDAO) {
        this.sbDAO = sbDAO;
    }

    public void setNotificationDao(NotificationDAO notificationDao) {
        this.notificationDao = notificationDao;
    }

    @Override
    public List getLoanreleaseList(String empid) {
        List releastLoanList = new ArrayList<>();
        String SQL = "select repid,not_id ,not_type,emp_id,emp_loan_tran.loan_tp ,rep_type,inst_no,amount,loan_name  FROM emp_loan_tran inner join g_loan "
                + "on emp_loan_tran.loan_tp=g_loan.loan_tp WHERE emp_id=? and tran_type=?";
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            conn = dataSource.getConnection();
            statement = conn.prepareStatement(SQL);
            statement.setString(1, empid);
            statement.setString(2, "RELEASE");
            rs = statement.executeQuery();
            while (rs.next()) {
                LoanRelease loanrelease = new LoanRelease();
                loanrelease.setHrepid(rs.getString("repid"));
                loanrelease.setHnid(rs.getInt("not_id"));
                loanrelease.setSltloan(rs.getString("loan_name"));
                loanrelease.setTxtEid(rs.getString("emp_id"));
                if (rs.getString("rep_type") != null && rs.getString("rep_type").equals("I")) {
                    loanrelease.setPtype("INSTALLMENT");
                } else if (rs.getString("rep_type") != null && rs.getString("rep_type").equals("F")) {
                    loanrelease.setPtype("FULL");
                }
                loanrelease.setTxtinstno(rs.getString("inst_no"));
                loanrelease.setTxtamount(rs.getString("amount"));
                releastLoanList.add(loanrelease);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, statement);
            DataBaseFunctions.closeSqlObjects(conn);
        }

        return releastLoanList;
    }

    @Override
    public void saveLoanrelease(LoanRelease loanrelease, int notId) {
        Connection con = null;
        PreparedStatement pst = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        try {
            con = dataSource.getConnection();
            String sql = "insert into emp_loan_tran(repid,not_id, not_type,emp_id,loan_tp,tran_type,rep_type,inst_no,amount) values(?,?,?,?,?,?,?,?,?)";
            pst = con.prepareStatement(sql);
            pst.setString(1, CommonFunctions.getMaxCode("emp_loan_tran", "repid", con));
            pst.setInt(2, notId);
            pst.setString(3, "LOAN_TRAN");
            pst.setString(4, loanrelease.getTxtEid());
            pst.setString(5, loanrelease.getSltloan());
            pst.setString(6, "RELEASE");
            pst.setString(7, loanrelease.getPtype());
            pst.setString(8, loanrelease.getTxtinstno());
            pst.setDouble(9, Double.parseDouble(loanrelease.getTxtamount()));
            pst.executeUpdate();
            String sbLang = sbDAO.getReleaseOfLoanLngDetails(loanrelease, notId);
            notificationDao.saveServiceBookLanguage(sbLang, notId, "LOAN_TRAN");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    public void updateLoanrelease(LoanRelease loanrelease) {
        Connection con = null;
        PreparedStatement pst = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        try {
            con = dataSource.getConnection();
            String sql = "update emp_loan_tran set not_type=?,emp_id=?,loan_tp=?,tran_type=?,rep_type=?,inst_no=?,amount=? where repid=? and not_id=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, "LOAN_TRAN");
            pst.setString(2, loanrelease.getTxtEid());
            pst.setString(3, loanrelease.getSltloan());
            pst.setString(4, "RELEASE");
            pst.setString(5, loanrelease.getPtype());
            pst.setString(6, loanrelease.getTxtinstno());
            pst.setDouble(7, Double.parseDouble(loanrelease.getTxtamount()));
            pst.setString(8, loanrelease.getHrepid());
            pst.setInt(9, loanrelease.getHnid());
            pst.executeUpdate();
            String sbLang = sbDAO.getReleaseOfLoanLngDetails(loanrelease, loanrelease.getHnid());
            notificationDao.saveServiceBookLanguage(sbLang, loanrelease.getHnid(), "LOAN_TRAN");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    public LoanRelease getLoanReleaseData(String repid, int notid) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        LoanRelease loanrelease = new LoanRelease();
        try {
            con = dataSource.getConnection();
            String sql = "select if_visible,emp_loan_tran.not_id ,emp_loan_tran.not_type,emp_loan_tran.emp_id,loan_tp ,rep_type,inst_no,amount,ordno,orddt,dept_code,off_code,auth,note  FROM emp_loan_tran\n"
                    + "inner join emp_notification on emp_loan_tran.not_id=emp_notification.not_id  where repid=? and emp_loan_tran.not_id=?";

            pst = con.prepareStatement(sql);
            pst.setString(1, repid);
            pst.setInt(2, notid);
            rs = pst.executeQuery();
            if (rs.next()) {

                loanrelease.setTxtreorderno(rs.getString("ordno"));
                loanrelease.setTxtreorderdt(CommonFunctions.getFormattedOutputDate1(rs.getDate("orddt")));
                loanrelease.setSancAuthName(CommonFunctions.getSPN(con, rs.getString("auth")));
                loanrelease.setSltdept(rs.getString("dept_code"));
                loanrelease.setSltoffice(rs.getString("off_code"));
                loanrelease.setSltauth(rs.getString("auth"));
                loanrelease.setSltloan(rs.getString("loan_tp"));
                loanrelease.setTxtEid(rs.getString("emp_id"));
                loanrelease.setPtype(rs.getString("rep_type"));
                loanrelease.setTxtinstno(rs.getString("inst_no"));
                loanrelease.setTxtamount(rs.getString("amount"));
                loanrelease.setTxtnote(rs.getString("note"));
                if (rs.getString("if_visible") != null && rs.getString("if_visible").equals("N")) {
                    loanrelease.setChkNotSBPrint("Y");
                } 

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return loanrelease;
    }

    public void deleteLoanRelease(String repid, int notid) {
        Connection con = null;
        PreparedStatement pst = null;
        try {
            con = this.dataSource.getConnection();
            pst = con.prepareStatement("DELETE FROM EMP_NOTIFICATION WHERE NOT_ID=?");
            pst.setInt(1, notid);
            pst.executeUpdate();
            pst = con.prepareStatement("DELETE FROM EMP_LOAN_TRAN WHERE REPID=?");
            pst.setString(1, repid);
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }
}
