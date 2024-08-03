package hrms.dao.loansanction;

import hrms.SelectOption;
import hrms.common.CalendarCommonMethods;
import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.dao.leaveAccount.ServiceBookLanguageDAO;
import hrms.dao.notification.NotificationDAO;
import hrms.model.common.FileAttribute;
import hrms.model.loan.EmployeeLoan;
import hrms.model.loan.EmployeeLoanAccBean;
import hrms.model.loan.GroupLoanBean;
import hrms.model.loan.GroupLoanForm;
import hrms.model.loan.Loan;
import hrms.model.master.Office;
import hrms.model.notification.NotificationBean;
import hrms.model.payroll.aqdtls.AqDtlsModel;
import hrms.model.payroll.aqmast.AqmastModel;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.apache.commons.lang.builder.ToStringBuilder;

public class LoanSancDAOImpl implements LoanSancDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    protected ServiceBookLanguageDAO sbDAO;

    public NotificationDAO notificationDao;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    protected MaxLoanIdDAOImpl maxloanidDao;

    public MaxLoanIdDAOImpl getMaxloanidDao() {
        return maxloanidDao;
    }

    public void setMaxloanidDao(MaxLoanIdDAOImpl maxloanidDao) {
        this.maxloanidDao = maxloanidDao;
    }

    public void setSbDAO(ServiceBookLanguageDAO sbDAO) {
        this.sbDAO = sbDAO;
    }

    public void setNotificationDao(NotificationDAO notificationDao) {
        this.notificationDao = notificationDao;
    }

    @Override
    public List getLoanSancList(String empId) {
        List loanlist = new ArrayList();
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        Loan loanForm = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            /*pstmt = con.prepareStatement("select emp_notification.is_validated,ag1.loan_id ag1loanid,ag1.calculation_id,ag2.loan_id ag2loanid,LOANID,lnnotid,LNNOTYPE,LNTP,ACC_NO,LNAMNT,LOAN_NAME,P_RECOVERED,I_RECOVERED,NOW_DEDN, bt_id_p, bt_id_i from(  select LOANID,NOT_ID,LOAN_TP,emp_loan_sanc.loanid lnid, emp_loan_sanc.not_id lnnotid, "
             + "emp_loan_sanc.not_type lnnotype,emp_loan_sanc.loan_tp lntp,emp_loan_sanc.amount lnamnt,ACC_NO,P_RECOVERED,I_RECOVERED,NOW_DEDN, bt_id_p, bt_id_i from emp_loan_sanc where emp_loan_sanc.emp_id=? and "
             + "emp_loan_sanc.not_type='LOAN_SANC') temploan "
             + " left outer join emp_notification on temploan.not_id=emp_notification.not_id"
             + " left outer join g_loan on temploan.loan_tp=g_loan.loan_tp "
             + " left outer join ag_interest_calculation ag1 on temploan.loanid=ag1.ad_ref_id"
             + " left outer join ag_interest_calculation ag2 on temploan.loanid=ag2.loan_id order by P_RECOVERED");*/
            pstmt = con.prepareStatement("select ag_loan_interest_detail_calculation.loanid agdetailloanid,ag_loan_interest_detail_calculation.gpf_no,emp_notification.is_validated,ag1.loan_id ag1loanid,ag1.calculation_id,ag2.loan_id ag2loanid,temploan.LOANID,lnnotid,LNNOTYPE,LNTP,ACC_NO,LNAMNT,LOAN_NAME,P_RECOVERED,I_RECOVERED,NOW_DEDN, bt_id_p, bt_id_i from(  select LOANID,NOT_ID,LOAN_TP,emp_loan_sanc.loanid lnid, emp_loan_sanc.not_id lnnotid, "
                    + "emp_loan_sanc.not_type lnnotype,emp_loan_sanc.loan_tp lntp,emp_loan_sanc.amount lnamnt,ACC_NO,P_RECOVERED,I_RECOVERED,NOW_DEDN, bt_id_p, bt_id_i from emp_loan_sanc where emp_loan_sanc.emp_id=? and "
                    + "emp_loan_sanc.not_type='LOAN_SANC') temploan "
                    + " left outer join emp_notification on temploan.not_id=emp_notification.not_id"
                    + " left outer join g_loan on temploan.loan_tp=g_loan.loan_tp "
                    + " left outer join ag_interest_calculation ag1 on temploan.loanid=ag1.ad_ref_id"
                    + " left outer join ag_interest_calculation ag2 on temploan.loanid=ag2.loan_id"
                    + " left outer join ag_loan_interest_detail_calculation on temploan.loanid=ag_loan_interest_detail_calculation.loanid order by P_RECOVERED");
            pstmt.setString(1, empId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                loanForm = new Loan();
                loanForm.setNowDeduct(rs.getString("NOW_DEDN"));
                if (rs.getString("NOW_DEDN") != null && rs.getString("NOW_DEDN").equals("P")) {
                    loanForm.setCompletedRecovery(rs.getInt("P_RECOVERED"));
                    loanForm.setBtid(rs.getString("bt_id_p"));
                } else if (rs.getString("NOW_DEDN") != null && rs.getString("NOW_DEDN").equals("I")) {
                    loanForm.setCompletedRecovery(rs.getInt("I_RECOVERED"));
                    loanForm.setBtid(rs.getString("bt_id_i"));
                }
                loanForm.setNotid(rs.getInt("lnnotid"));
                loanForm.setLoanid(rs.getString("LOANID"));
                loanForm.setLoanTp(rs.getString("LNTP"));
                loanForm.setSltloan(rs.getString("LOAN_NAME"));
                loanForm.setTxtamount(rs.getDouble("LNAMNT"));
                loanForm.setAccountNo(rs.getString("ACC_NO"));
                loanForm.setAgcalculationid(rs.getString("calculation_id"));
                loanForm.setAg1loanid(rs.getString("ag1loanid"));
                loanForm.setAg2loanid(rs.getString("ag2loanid"));
                loanForm.setIsValidated(rs.getString("is_validated"));
                loanForm.setInterestdetailreportgpfno(rs.getString("gpf_no"));
                loanlist.add(loanForm);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return loanlist;
    }

    @Override
    public void saveLoanDetail(Loan lfb, int notId) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String loanId = "1";

        try {
            con = dataSource.getConnection();
            //loanId = maxloanidDao.getLoanId();
            //loanId = maxloanidDao.checkDuplicateMaxCode(loanId);
            //System.out.println("loanId is: "+loanId);
            if (lfb.getBtid() == null || lfb.getBtid().equals("")) {
                pstmt = con.prepareStatement("select bt_code_principal, bt_code_interest  from g_loan where loan_tp=?");
                pstmt.setString(1, lfb.getSltloan());
                rs = pstmt.executeQuery();
                if (rs.next()) {
                    if (lfb.getNowDeduct().equals("P")) {
                        lfb.setBtid(rs.getString("bt_code_principal"));
                    } else {
                        lfb.setBtid(rs.getString("bt_code_interest"));
                    }
                }
            }

            if (lfb.getNowDeduct().equals("P") && !loanId.equals("")) {
                pstmt = con.prepareStatement("insert into emp_loan_sanc(not_id, not_type, emp_id, loan_tp, amount,ACC_NO,VCH_NO,VCH_DATE,TR_CODE,NOW_DEDN,DED_ST_DATE,P_ORG_AMT,P_TOT_NO_INSTL,P_INSTL_AMT,P_LAST_INSTL_NO,P_INSTL_NO,P_CUM_RECOVERED,P_RECOVERED,BANK_CODE,BRANCH_CODE,bt_id_p,loan_gc_percent) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
                //pstmt.setString(1, loanId);
                pstmt.setInt(1, notId);
                pstmt.setString(2, "LOAN_SANC");
                pstmt.setString(3, lfb.getEmpid());
                pstmt.setString(4, lfb.getSltloan());
                pstmt.setDouble(5, lfb.getTxtamount());
                if (lfb.getAccountNo() != null && !lfb.getAccountNo().equalsIgnoreCase("")) {
                    pstmt.setString(6, lfb.getAccountNo());
                } else {
                    pstmt.setString(6, null);
                }
                if (lfb.getVoucherNo() != null && !lfb.getVoucherNo().equalsIgnoreCase("")) {
                    pstmt.setString(7, lfb.getVoucherNo());
                } else {
                    pstmt.setString(7, null);
                }
                if (lfb.getVoucherDate() != null && !lfb.getVoucherDate().equals("")) {
                    Date date1 = new SimpleDateFormat("dd-MMM-yyyy").parse(lfb.getVoucherDate());
                    pstmt.setDate(8, new java.sql.Date(date1.getTime()));
                } else {
                    pstmt.setDate(8, null);
                }
                if (lfb.getTreasuryname() != null && !lfb.getTreasuryname().equalsIgnoreCase("")) {
                    pstmt.setString(9, lfb.getTreasuryname());
                } else {
                    pstmt.setString(9, null);
                }

                pstmt.setString(10, lfb.getNowDeduct());
                if (lfb.getDeductionStartDate() != null && !lfb.getDeductionStartDate().equals("")) {

                    pstmt.setDate(11, new java.sql.Date(lfb.getDeductionStartDate().getTime()));
                } else {
                    pstmt.setDate(11, null);
                }

                pstmt.setInt(12, lfb.getOriginalAmt());
                pstmt.setInt(13, lfb.getTotalNoOfInsl());
                pstmt.setInt(14, lfb.getInstalmentAmount());
                pstmt.setInt(15, lfb.getLastPaidInstalNo());
                pstmt.setInt(16, lfb.getMonthlyinstlno());
                pstmt.setInt(17, lfb.getCumulativeAmtPaid());
                pstmt.setInt(18, lfb.getCompletedRecovery());
                if (lfb.getBank() != null && !lfb.getBank().equals("")) {
                    pstmt.setString(19, lfb.getBank());
                } else {
                    pstmt.setString(19, null);
                }
                if (lfb.getBranch() != null && !lfb.getBranch().equals("")) {
                    pstmt.setString(20, lfb.getBranch());
                } else {
                    pstmt.setString(20, null);
                }
                pstmt.setString(21, lfb.getBtid());
                if (lfb.getSltloan() != null && lfb.getSltloan().equals("NPSL") && lfb.getSltGovernmentContr() != null && !lfb.getSltGovernmentContr().equals("")) {
                    pstmt.setInt(22, Integer.parseInt(lfb.getSltGovernmentContr()));
                } else {
                    pstmt.setInt(22, 0);
                }
                pstmt.executeUpdate();
            } else if (lfb.getNowDeduct().equals("I") && !loanId.equals("")) {
                pstmt = con.prepareStatement("insert into emp_loan_sanc(not_id, not_type, emp_id, loan_tp, amount,ACC_NO,VCH_NO,VCH_DATE,TR_CODE,NOW_DEDN,DED_ST_DATE,I_ORG_AMT,I_TOT_NO_INST,I_INSTL_AMT,I_LAST_INSTL_NO,I_INSTL_NO,I_CUM_RECOVERED,I_RECOVERED,BANK_CODE,BRANCH_CODE,bt_id_i,prin_reference_loan_id) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
                //pstmt.setString(1, loanId);
                pstmt.setInt(1, notId);
                pstmt.setString(2, "LOAN_SANC");
                pstmt.setString(3, lfb.getEmpid());
                pstmt.setString(4, lfb.getSltloan());
                pstmt.setDouble(5, lfb.getTxtamount());
                if (lfb.getAccountNo() != null && !lfb.getAccountNo().equalsIgnoreCase("")) {
                    pstmt.setString(6, lfb.getAccountNo());
                } else {
                    pstmt.setString(6, null);
                }
                if (lfb.getVoucherNo() != null && !lfb.getVoucherNo().equalsIgnoreCase("")) {
                    pstmt.setString(7, lfb.getVoucherNo());
                } else {
                    pstmt.setString(7, null);
                }
                if (lfb.getVoucherDate() != null && !lfb.getVoucherDate().equals("")) {
                    Date date1 = new SimpleDateFormat("dd-MMM-yyyy").parse(lfb.getVoucherDate());
                    pstmt.setDate(8, new java.sql.Date(date1.getTime()));
                } else {
                    pstmt.setDate(8, null);
                }
                if (lfb.getTreasuryname() != null && !lfb.getTreasuryname().equalsIgnoreCase("")) {
                    pstmt.setString(9, lfb.getTreasuryname());
                } else {
                    pstmt.setString(9, null);
                }

                pstmt.setString(10, lfb.getNowDeduct());
                if (lfb.getDeductionStartDate() != null && !lfb.getDeductionStartDate().equals("")) {
                    pstmt.setDate(11, new java.sql.Date(lfb.getDeductionStartDate().getTime()));
                } else {
                    pstmt.setDate(11, null);
                }

                pstmt.setInt(12, lfb.getOriginalAmt());
                pstmt.setInt(13, lfb.getTotalNoOfInsl());
                pstmt.setInt(14, lfb.getInstalmentAmount());
                pstmt.setInt(15, lfb.getLastPaidInstalNo());
                pstmt.setInt(16, lfb.getMonthlyinstlno());
                pstmt.setInt(17, lfb.getCumulativeAmtPaid());
                pstmt.setInt(18, lfb.getCompletedRecovery());
                if (lfb.getBank() != null && !lfb.getBank().equals("")) {
                    pstmt.setString(19, lfb.getBank());
                } else {
                    pstmt.setString(19, null);
                }
                if (lfb.getBranch() != null && !lfb.getBranch().equals("")) {
                    pstmt.setString(20, lfb.getBranch());
                } else {
                    pstmt.setString(20, null);
                }
                pstmt.setString(21, lfb.getBtid());
                pstmt.setString(22, lfb.getPrinloanid());
                pstmt.executeUpdate();

                if (lfb.getSltloan() != null && !lfb.getSltloan().equals("")) {
                    if (lfb.getSltloan().equals("BICA") || lfb.getSltloan().equals("CMPA") || lfb.getSltloan().equals("MOPA") || lfb.getSltloan().equals("MCA") || lfb.getSltloan().equals("VE")) {
                        if (lfb.getAgcalculationid() != null && !lfb.getAgcalculationid().equals("")) {
                            pstmt = con.prepareStatement("UPDATE ag_interest_calculation set loan_id=? where ad_ref_id=?");
                            pstmt.setString(1, loanId);
                            pstmt.setString(2, lfb.getAgAdrefid());
                            pstmt.executeUpdate();
                        }

                    }
                }
            }
            if (lfb.getSltloan().equals("BICA") || lfb.getSltloan().equals("CMPA") || lfb.getSltloan().equals("MOPA") || lfb.getSltloan().equals("MCA") || lfb.getSltloan().equals("VE")) {
                if (lfb.getNowDeduct().equals("P")) {
                    /*
                     * Updating the Service Book Language
                     */
                    lfb.setNotid(notId);
                    String sbLang = sbDAO.getLoanSancDetails(lfb);
                    pstmt = con.prepareStatement("UPDATE EMP_NOTIFICATION SET SB_DESCRIPTION =? WHERE NOT_ID=? ");
                    pstmt.setString(1, sbLang);
                    pstmt.setInt(2, lfb.getNotid());
                    pstmt.execute();

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void updateLoanDetail(Loan lfb, String filepath) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        PreparedStatement gcpstmt = null;

        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            con = dataSource.getConnection();
            if (lfb.getBtid() == null || lfb.getBtid().equals("")) {
                pstmt = con.prepareStatement("select bt_code_principal, bt_code_interest from g_loan where loan_tp=?");
                pstmt.setString(1, lfb.getSltloan());
                rs = pstmt.executeQuery();
                if (rs.next()) {
                    if (lfb.getNowDeduct().equals("P")) {
                        lfb.setBtid(rs.getString("bt_code_principal"));
                    } else {
                        lfb.setBtid(rs.getString("bt_code_interest"));
                    }
                }
            }
            if (lfb.getNowDeduct().equals("P")) {
                pstmt = con.prepareStatement("update emp_loan_sanc set loan_tp=?, amount=?,ACC_NO=?,VCH_NO=?,VCH_DATE=?,TR_CODE=?,NOW_DEDN=?,DED_ST_DATE=?,"
                        + "P_ORG_AMT=?,P_TOT_NO_INSTL=?,P_INSTL_AMT=?,P_LAST_INSTL_NO=?,P_INSTL_NO=?,P_CUM_RECOVERED=?,P_RECOVERED=?,BANK_CODE=?,BRANCH_CODE=?,bt_id_p=?, "
                        + "not_id=?,loan_gc_percent=? where loanid=?");
                pstmt.setString(1, lfb.getSltloan());
                pstmt.setDouble(2, lfb.getTxtamount());
                if (lfb.getAccountNo() != null && !lfb.getAccountNo().equalsIgnoreCase("")) {
                    pstmt.setString(3, lfb.getAccountNo());
                } else {
                    pstmt.setString(3, null);
                }
                if (lfb.getVoucherNo() != null && !lfb.getVoucherNo().equalsIgnoreCase("")) {
                    pstmt.setString(4, lfb.getVoucherNo());
                } else {
                    pstmt.setString(4, null);
                }
                if (lfb.getVoucherDate() != null && !lfb.getVoucherDate().equals("")) {
                    Date date1 = new SimpleDateFormat("dd-MMM-yyyy").parse(lfb.getVoucherDate());
                    pstmt.setDate(5, new java.sql.Date(date1.getTime()));
                } else {
                    pstmt.setDate(5, null);
                }
                if (lfb.getTreasuryname() != null && !lfb.getTreasuryname().equalsIgnoreCase("")) {
                    pstmt.setString(6, lfb.getTreasuryname());
                } else {
                    pstmt.setString(6, null);
                }

                if (lfb.getNowDeduct() != null && !lfb.getNowDeduct().equalsIgnoreCase("")) {
                    pstmt.setString(7, lfb.getNowDeduct());
                } else {
                    pstmt.setString(7, null);
                }
                if (lfb.getDeductionStartDate() != null && !lfb.getDeductionStartDate().equals("")) {
                    pstmt.setDate(8, new java.sql.Date(lfb.getDeductionStartDate().getTime()));
                } else {
                    pstmt.setDate(8, null);
                }

                pstmt.setInt(9, lfb.getOriginalAmt());
                pstmt.setInt(10, lfb.getTotalNoOfInsl());
                pstmt.setInt(11, lfb.getInstalmentAmount());
                pstmt.setInt(12, lfb.getLastPaidInstalNo());
                pstmt.setInt(13, lfb.getMonthlyinstlno());
                pstmt.setInt(14, lfb.getCumulativeAmtPaid());
                pstmt.setInt(15, lfb.getCompletedRecovery());
                if (lfb.getBank() != null && !lfb.getBank().equals("")) {
                    pstmt.setString(16, lfb.getBank());
                } else {
                    pstmt.setString(16, null);
                }
                if (lfb.getBranch() != null && !lfb.getBranch().equals("")) {
                    pstmt.setString(17, lfb.getBranch());
                } else {
                    pstmt.setString(17, null);
                }
                pstmt.setString(18, lfb.getBtid());
                pstmt.setInt(19, lfb.getNotid());
                if (lfb.getSltloan() != null && lfb.getSltloan().equals("NPSL") && lfb.getSltGovernmentContr() != null && !lfb.getSltGovernmentContr().equals("")) {
                    pstmt.setInt(20, Integer.parseInt(lfb.getSltGovernmentContr()));
                } else {
                    pstmt.setInt(20, 0);
                }
                pstmt.setString(21, lfb.getLoanid());
                pstmt.executeUpdate();

                if (lfb.getSltloan() != null && lfb.getSltloan().equals("NPSL")) {
                    if (lfb.getGcfile() != null && !lfb.getGcfile().isEmpty()) {
                        int read = 0;
                        byte[] bytes = new byte[1024];

                        inputStream = lfb.getGcfile().getInputStream();
                        String diskfilename = "SB" + System.currentTimeMillis() + "";
                        File newFile1 = new File(filepath + diskfilename);
                        if (!newFile1.exists()) {
                            newFile1.createNewFile();
                        }
                        outputStream = new FileOutputStream(newFile1);

                        while ((read = inputStream.read(bytes)) != -1) {
                            outputStream.write(bytes, 0, read);
                        }

                        String insFileQry = "update emp_loan_sanc set file_type=?, file_path=?, file_name=?, original_name=?, doc_name=? where loanid=?";
                        gcpstmt = con.prepareStatement(insFileQry);
                        gcpstmt.setString(1, lfb.getGcfile().getContentType());
                        gcpstmt.setString(2, filepath);
                        gcpstmt.setString(3, diskfilename);
                        gcpstmt.setString(4, lfb.getGcfile().getOriginalFilename());
                        gcpstmt.setString(5, "GCFILE");
                        gcpstmt.setString(6, lfb.getLoanid());
                        gcpstmt.executeUpdate();

                    }
                }
            } else if (lfb.getNowDeduct().equals("I")) {
                pstmt = con.prepareStatement("update emp_loan_sanc set  loan_tp=?, amount=?,ACC_NO=?,VCH_NO=?,VCH_DATE=?,TR_CODE=?,NOW_DEDN=?,DED_ST_DATE=?,I_ORG_AMT=?,I_TOT_NO_INST=?,I_INSTL_AMT=?,I_LAST_INSTL_NO=?,I_INSTL_NO=?,I_CUM_RECOVERED=?,I_RECOVERED=?,BANK_CODE=?,BRANCH_CODE=?,bt_id_i=?, not_id=? where loanid=?");
                pstmt.setString(1, lfb.getSltloan());
                pstmt.setDouble(2, lfb.getTxtamount());
                if (lfb.getAccountNo() != null && !lfb.getAccountNo().equalsIgnoreCase("")) {
                    pstmt.setString(3, lfb.getAccountNo());
                } else {
                    pstmt.setString(3, null);
                }
                if (lfb.getVoucherNo() != null && !lfb.getVoucherNo().equalsIgnoreCase("")) {
                    pstmt.setString(4, lfb.getVoucherNo());
                } else {
                    pstmt.setString(4, null);
                }
                if (lfb.getVoucherDate() != null && !lfb.getVoucherDate().equals("")) {
                    Date date1 = new SimpleDateFormat("dd-MMM-yyyy").parse(lfb.getVoucherDate());
                    pstmt.setDate(5, new java.sql.Date(date1.getTime()));
                } else {
                    pstmt.setDate(5, null);
                }
                if (lfb.getTreasuryname() != null && !lfb.getTreasuryname().equalsIgnoreCase("")) {
                    pstmt.setString(6, lfb.getTreasuryname());
                } else {
                    pstmt.setString(6, null);
                }

                if (lfb.getNowDeduct() != null && !lfb.getNowDeduct().equalsIgnoreCase("")) {
                    pstmt.setString(7, lfb.getNowDeduct());
                } else {
                    pstmt.setString(7, null);
                }
                if (lfb.getDeductionStartDate() != null && !lfb.getDeductionStartDate().equals("")) {
                    pstmt.setDate(8, new java.sql.Date(lfb.getDeductionStartDate().getTime()));
                } else {
                    pstmt.setDate(8, null);
                }

                pstmt.setInt(9, lfb.getOriginalAmt());
                pstmt.setInt(10, lfb.getTotalNoOfInsl());
                pstmt.setInt(11, lfb.getInstalmentAmount());
                pstmt.setInt(12, lfb.getLastPaidInstalNo());
                pstmt.setInt(13, lfb.getMonthlyinstlno());
                pstmt.setInt(14, lfb.getCumulativeAmtPaid());
                pstmt.setInt(15, lfb.getCompletedRecovery());
                if (lfb.getBank() != null && !lfb.getBank().equals("")) {
                    pstmt.setString(16, lfb.getBank());
                } else {
                    pstmt.setString(16, null);
                }
                if (lfb.getBranch() != null && !lfb.getBranch().equals("")) {
                    pstmt.setString(17, lfb.getBranch());
                } else {
                    pstmt.setString(17, null);
                }
                pstmt.setString(18, lfb.getBtid());
                pstmt.setInt(19, lfb.getNotid());
                pstmt.setString(20, lfb.getLoanid());
                pstmt.executeUpdate();
            }
            if (lfb.getSltloan().equals("BICA") || lfb.getSltloan().equals("CMPA") || lfb.getSltloan().equals("MOPA") || lfb.getSltloan().equals("MCA") || lfb.getSltloan().equals("VE")) {
                if (lfb.getNowDeduct().equals("P")) {
                    /*
                     * Updating the Service Book Language
                     */

                    String sbLang = sbDAO.getLoanSancDetails(lfb);
                    pstmt = con.prepareStatement("UPDATE EMP_NOTIFICATION SET SB_DESCRIPTION =? WHERE NOT_ID=? ");
                    pstmt.setString(1, sbLang);
                    pstmt.setInt(2, lfb.getNotid());
                    pstmt.execute();

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public Loan editLoanData(String loanId) {
        Connection con = null;
        PreparedStatement pstmt = null;
        Loan lfb = new Loan();
        ResultSet rs = null;
        String input = "2013-09-14";
        // DateFormat format = DateFormat.getInstance();
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-mm-dd");
        SimpleDateFormat format2 = new SimpleDateFormat("dd-MMM-yy");
        Date date;

        PreparedStatement gcpstmt = null;
        ResultSet gcrs = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("select emploan.file_name, emploan.original_name, emploan.doc_name,loan_gc_percent,empnoti.if_visible,\n"
                    + "ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMPNAME,post,empnoti.not_id as notid,note,empnoti.not_type as nottype, empnoti.emp_id as empid, \n"
                    + "loan_tp, amount,ACC_NO,VCH_NO,VCH_DATE,emploan.TR_CODE trcode,emploan.NOW_DEDN as nowdedn,\n"
                    + " DED_ST_DATE,P_ORG_AMT,P_TOT_NO_INSTL, P_INSTL_AMT,P_LAST_INSTL_NO,P_LAST_PMT_MON,P_CUM_RECOVERED,I_ORG_AMT,I_TOT_NO_INST, I_INSTL_NO,\n"
                    + " I_INSTL_AMT,I_LAST_INSTL_NO,I_LAST_PMT_MON, I_CUM_RECOVERED,P_RECOVERED,I_RECOVERED,PVT_DESC,emploan.BANK_CODE as bankcode,emploan.BRANCH_CODE as branchcode,\n"
                    + " BT_ID_P,BT_ID_I,P_INSTL_NO,auth,ordno,orddt,empnoti.dept_code deptcode,empnoti.off_code as offcode,substr(auth,14,6)authpost,gspc1.spn spn1, IS_VERIFIED,bt_id_p,bt_id_i,dist_code  \n"
                    + " from emp_loan_sanc emploan\n"
                    + " left outer join emp_notification empnoti on emploan.not_id=empnoti.not_id\n"
                    + " left outer join emp_mast empmast on empnoti.auth=empmast.cur_spc\n"
                    + " left outer join g_office goff on empnoti.off_code=goff.off_code\n"
                    + " left outer join g_spc gspc on empmast.cur_spc=gspc.spc\n"
                    + " left outer join g_spc gspc1 on empnoti.auth=gspc1.spc\n"
                    + " left outer join g_post gpost on gspc.gpc=gpost.post_code\n"
                    + " where emploan.loanid=?");
            pstmt.setString(1, loanId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                lfb = new Loan();
                lfb.setLoanid(loanId);
                lfb.setNotid(rs.getInt("notid"));
                lfb.setOrderno(rs.getString("ordno"));
                lfb.setOrderdate(CommonFunctions.getFormattedOutputDate1(rs.getDate("orddt")));
                lfb.setNotType(rs.getString("nottype"));
                lfb.setEmpid(rs.getString("empid"));
                lfb.setSltloan(rs.getString("loan_tp"));
                lfb.setTxtamount(rs.getInt("amount"));
                /*if (rs.getString("empname") != null && rs.getString("post") != null) {
                    lfb.setAuthority(rs.getString("empname") + "," + rs.getString("spn1"));
                } else {
                    lfb.setAuthority(rs.getString("spn1"));
                }*/
                lfb.setAuthority(rs.getString("spn1"));
                //lfb.setHidAuthDeptCode(rs.getString("deptcode"));
                lfb.setHidSancDeptCode(rs.getString("deptcode"));
                // .lfb.setHidAuthOffCode(rs.getString("offcode"));
                lfb.setHidSancOffCode(rs.getString("offcode"));
                // lfb.setHidSpcAuthCode(rs.getString("auth"));
                lfb.setSancSpc(rs.getString("auth"));
                lfb.setHidSancGenericPost(rs.getString("authpost"));
               // lfb.setAuthority(rs.getString("spn1"));
                lfb.setHidSancDistCode(rs.getString("dist_code"));
                lfb.setAccountNo(rs.getString("ACC_NO"));
                lfb.setVoucherNo(rs.getString("VCH_NO"));
                lfb.setVoucherDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("VCH_DATE")));
                lfb.setTreasuryname(rs.getString("trcode"));
                lfb.setNowDeduct(rs.getString("nowdedn"));
                lfb.setDeductionStartDate(rs.getDate("DED_ST_DATE"));

                if (lfb.getNowDeduct() != null && lfb.getNowDeduct().equalsIgnoreCase("P")) {
                    lfb.setOriginalAmt(rs.getInt("P_ORG_AMT"));
                    lfb.setTotalNoOfInsl(rs.getInt("P_TOT_NO_INSTL"));
                    lfb.setInstalmentAmount(rs.getInt("P_INSTL_AMT"));
                    lfb.setLastPaidInstalNo(rs.getInt("P_LAST_INSTL_NO"));
                    lfb.setMonthlyinstlno(rs.getInt("P_INSTL_NO"));
                    lfb.setCumulativeAmtPaid(rs.getInt("P_CUM_RECOVERED"));
                    lfb.setCompletedRecovery(rs.getInt("P_RECOVERED"));
                    lfb.setBtid(rs.getString("bt_id_p"));
                } else {
                    lfb.setOriginalAmt(rs.getInt("I_ORG_AMT"));
                    lfb.setTotalNoOfInsl(rs.getInt("I_TOT_NO_INST"));
                    lfb.setInstalmentAmount(rs.getInt("I_INSTL_AMT"));
                    lfb.setLastPaidInstalNo(rs.getInt("I_LAST_INSTL_NO"));
                    lfb.setMonthlyinstlno(rs.getInt("I_INSTL_NO"));
                    lfb.setCumulativeAmtPaid(rs.getInt("I_CUM_RECOVERED"));
                    lfb.setCompletedRecovery(rs.getInt("I_RECOVERED"));
                    lfb.setBtid(rs.getString("bt_id_i"));
                }
                lfb.setTxtpvtloan(rs.getString("PVT_DESC"));
                lfb.setBank(rs.getString("bankcode"));
                lfb.setBranch(rs.getString("branchcode"));
                lfb.setNote(rs.getString("note"));
                lfb.setIsverified(rs.getString("IS_VERIFIED"));
                if (rs.getString("if_visible") != null && !rs.getString("if_visible").equals("")) {
                    if (rs.getString("if_visible").equals("Y")) {
                        lfb.setChkNotSBPrint("N");
                    } else if (rs.getString("if_visible").equals("N")) {
                        lfb.setChkNotSBPrint("Y");
                    }
                }
                lfb.setSltGovernmentContr(rs.getString("loan_gc_percent"));
                lfb.setOriginalFileNameGcfileDoc(rs.getString("original_name"));
                lfb.setDiskFilenameGcfileDoc(rs.getString("file_name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return lfb;
    }

    @Override
    public void removeLoanData(String loanId, int notid) {
        Connection con = null;
        PreparedStatement pstmt = null;
        PreparedStatement pstmtt = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("delete from emp_loan_sanc where loanid=?");
            pstmt.setString(1, loanId);
            pstmt.executeUpdate();
            pstmtt = con.prepareStatement("delete from emp_notification where not_id=?");
            pstmtt.setInt(1, notid);
            pstmtt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(pstmtt);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    @Override
    public Loan getLoanInterestIntimationData(String loanid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        Loan lform = new Loan();
        try {
            con = this.dataSource.getConnection();

            String sql = " select ag_interest_calculation.*,LOAN_NAME from ag_interest_calculation"
                    + " left outer join g_loan on ag_interest_calculation.loan_type=g_loan.loan_tp"
                    + " where loan_id=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, loanid);
            rs = pst.executeQuery();
            if (rs.next()) {
                if (rs.getString("loan_type").equals("COMP") && rs.getString("LOAN_NAME") == null) {
                    lform.setLoanName("COMPUTER ADVANCE");
                } else {
                    lform.setLoanName(rs.getString("COMPUTER LOAN_NAME"));
                }
                lform.setOriginalAmt(Integer.parseInt(rs.getString("prin_paid")));
                lform.setInstalmentAmount(0);
                lform.setAgInterestAmt(rs.getString("total_interest"));
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
    public String getLoanTP(String loanid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String loantp = "";
        try {
            con = this.dataSource.getConnection();

            String sql = "select loan_tp from emp_loan_sanc where loanid=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, loanid);
            rs = pst.executeQuery();
            if (rs.next()) {
                loantp = rs.getString("loan_tp");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return loantp;
    }

    @Override
    public String getLoanName(String loantp) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String loanname = "";
        try {
            con = this.dataSource.getConnection();

            String sql = "select loan_name from g_loan where loan_tp=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, loantp);
            rs = pst.executeQuery();
            if (rs.next()) {
                loanname = rs.getString("loan_name");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return loanname;
    }

    @Override
    public int updateLoanSanctionSBData(Loan lfb) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String ifvisible = "Y";

        int retVal = 0;
        try {
            con = this.dataSource.getConnection();

            if (lfb.getChkNotSBPrint() != null && !lfb.getChkNotSBPrint().equals("")) {
                if (lfb.getChkNotSBPrint().equals("Y")) {
                    ifvisible = "N";
                }
            }

            String sql = "update EMP_NOTIFICATION set ORDNO=?,ORDDT=? where NOT_ID=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, lfb.getOrderno());
            if (lfb.getOrderdate() != null) {
                pst.setTimestamp(2, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(lfb.getOrderdate()).getTime()));
            } else {
                pst.setTimestamp(2, null);
            }
            pst.setInt(3, lfb.getNotid());
            pst.executeUpdate();

            DataBaseFunctions.closeSqlObjects(rs, pst);

            if (ifvisible.equals("N")) {
                sql = "UPDATE EMP_NOTIFICATION SET IF_VISIBLE=? WHERE NOT_ID=?";
                pst = con.prepareStatement(sql);
                pst.setString(1, ifvisible);
                pst.setInt(2, lfb.getNotid());
                retVal = pst.executeUpdate();
            } else {
                sql = "select * from emp_notification where not_id=?";
                pst = con.prepareStatement(sql);
                pst.setInt(1, lfb.getNotid());
                rs = pst.executeQuery();
                if (rs.next()) {
                    lfb.setOrderno(rs.getString("ordno"));
                    lfb.setOrderdate(CommonFunctions.getFormattedOutputDate1(rs.getDate("orddt")));
                    lfb.setHidAuthDeptCode(rs.getString("dept_code"));
                    lfb.setHidAuthOffCode(rs.getString("off_code"));
                    lfb.setHidSpcAuthCode(rs.getString("auth"));
                }

                DataBaseFunctions.closeSqlObjects(rs, pst);

                sql = "select * from emp_loan_sanc where loanid=?";
                pst = con.prepareStatement(sql);
                pst.setString(1, lfb.getLoanid());
                rs = pst.executeQuery();
                if (rs.next()) {
                    lfb.setSltloan(rs.getString("loan_tp"));
                    lfb.setTxtamount(rs.getDouble("amount"));
                }

                DataBaseFunctions.closeSqlObjects(rs, pst);

                String sbLang = sbDAO.getLoanSancDetails(lfb);
                pst = con.prepareStatement("UPDATE EMP_NOTIFICATION SET SB_DESCRIPTION=?,IF_VISIBLE='Y' WHERE NOT_ID=?");
                pst.setString(1, sbLang);
                pst.setInt(2, lfb.getNotid());
                retVal = pst.executeUpdate();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return retVal;
    }

    @Override
    public int getAGInterestDetailedReportEmployeeCount(String offcode) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        int count = 0;
        try {
            con = this.dataSource.getConnection();

            String sql = "select count(*) cnt from ag_loan_interest_detail_calculation"
                    + " inner join emp_loan_sanc on ag_loan_interest_detail_calculation.loanid=emp_loan_sanc.loanid"
                    + " inner join emp_mast on emp_loan_sanc.emp_id=emp_mast.emp_id"
                    + " inner join g_office on emp_mast.cur_off_code=g_office.off_code"
                    + " where g_office.off_code=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, offcode);
            rs = pst.executeQuery();
            if (rs.next()) {
                count = rs.getInt("cnt");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return count;
    }

    @Override
    public FileAttribute getAttachedDocument(String filePath, String docName, String loanid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        File f = null;
        FileAttribute fa = new FileAttribute();
        try {
            con = this.dataSource.getConnection();
            String sql = "";
            if (docName != null && !docName.equals("")) {
                sql = "SELECT * FROM emp_loan_sanc WHERE loanid=? and doc_name=?";
                pst = con.prepareStatement(sql);
                pst.setString(1, loanid);
                pst.setString(2, docName);
                rs = pst.executeQuery();
                if (rs.next()) {
                    String dirpath = filePath + rs.getString("file_name");

                    f = new File(dirpath);
                    if (f.exists()) {
                        fa.setDiskFileName(rs.getString("file_name"));
                        fa.setOriginalFileName(rs.getString("original_name"));
                        fa.setFileType(rs.getString("file_type"));
                        fa.setUploadAttachment(f);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return fa;
    }

    @Override
    public int deleteLoanAttachment(String loanid, String filepath, FileAttribute fa) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        File f = null;

        int deletestatus = 0;
        try {
            con = this.dataSource.getConnection();

            String sql = "update emp_loan_sanc set file_type=null, file_path=null, file_name=null, original_name=null, doc_name=null where loanid=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, loanid);
            deletestatus = pst.executeUpdate();

            if (deletestatus == 1) {
                String dirpath = filepath + fa.getDiskFileName();
                f = new File(dirpath);
                if (f.exists()) {
                    boolean isDeleted = f.delete();
                    if (isDeleted == true) {

                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return deletestatus;
    }

    @Override
    public String SaveLoanDetailSBCorrection(Loan loanForm, int notId, int sbcorrectionid) {

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String loanId = "1";
        String sbLang = "";

        try {
            con = dataSource.getConnection();

            if (loanForm.getBtid() == null || loanForm.getBtid().equals("")) {
                pstmt = con.prepareStatement("select bt_code_principal, bt_code_interest from g_loan where loan_tp=?");
                pstmt.setString(1, loanForm.getSltloan());
                rs = pstmt.executeQuery();
                if (rs.next()) {
                    if (loanForm.getNowDeduct().equals("P")) {
                        loanForm.setBtid(rs.getString("bt_code_principal"));
                    } else {
                        loanForm.setBtid(rs.getString("bt_code_interest"));
                    }
                }
            }

            pstmt = con.prepareStatement("delete from EMP_LOAN_SANC_SB_CORRECTION where emp_id=? and loanid=?");
            pstmt.setString(1, loanForm.getEmpid());
            pstmt.setString(2, loanForm.getLoanid());
            pstmt.executeUpdate();

            if (loanForm.getNowDeduct().equals("P") && !loanId.equals("")) {
                pstmt = con.prepareStatement("insert into EMP_LOAN_SANC_SB_CORRECTION(not_id, not_type, emp_id, loan_tp, amount,ACC_NO,VCH_NO,VCH_DATE,TR_CODE,NOW_DEDN,DED_ST_DATE,P_ORG_AMT,P_TOT_NO_INSTL,P_INSTL_AMT,P_LAST_INSTL_NO,P_INSTL_NO,P_CUM_RECOVERED,P_RECOVERED,BANK_CODE,BRANCH_CODE,bt_id_p,loan_gc_percent,ref_correction_id,loanid) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
                pstmt.setInt(1, notId);
                pstmt.setString(2, "LOAN_SANC");
                pstmt.setString(3, loanForm.getEmpid());
                pstmt.setString(4, loanForm.getSltloan());
                pstmt.setDouble(5, loanForm.getTxtamount());
                if (loanForm.getAccountNo() != null && !loanForm.getAccountNo().equalsIgnoreCase("")) {
                    pstmt.setString(6, loanForm.getAccountNo());
                } else {
                    pstmt.setString(6, null);
                }
                if (loanForm.getVoucherNo() != null && !loanForm.getVoucherNo().equalsIgnoreCase("")) {
                    pstmt.setString(7, loanForm.getVoucherNo());
                } else {
                    pstmt.setString(7, null);
                }
                if (loanForm.getVoucherDate() != null && !loanForm.getVoucherDate().equals("")) {
                    Date date1 = new SimpleDateFormat("dd-MMM-yyyy").parse(loanForm.getVoucherDate());
                    pstmt.setDate(8, new java.sql.Date(date1.getTime()));
                } else {
                    pstmt.setDate(8, null);
                }
                if (loanForm.getTreasuryname() != null && !loanForm.getTreasuryname().equalsIgnoreCase("")) {
                    pstmt.setString(9, loanForm.getTreasuryname());
                } else {
                    pstmt.setString(9, null);
                }

                pstmt.setString(10, loanForm.getNowDeduct());
                if (loanForm.getDeductionStartDate() != null && !loanForm.getDeductionStartDate().equals("")) {

                    pstmt.setDate(11, new java.sql.Date(loanForm.getDeductionStartDate().getTime()));
                } else {
                    pstmt.setDate(11, null);
                }

                pstmt.setInt(12, loanForm.getOriginalAmt());
                pstmt.setInt(13, loanForm.getTotalNoOfInsl());
                pstmt.setInt(14, loanForm.getInstalmentAmount());
                pstmt.setInt(15, loanForm.getLastPaidInstalNo());
                pstmt.setInt(16, loanForm.getMonthlyinstlno());
                pstmt.setInt(17, loanForm.getCumulativeAmtPaid());
                if (loanForm.getCompletedRecoverySBCorrection() != null && !loanForm.getCompletedRecoverySBCorrection().equals("")) {
                    pstmt.setInt(18, Integer.parseInt(loanForm.getCompletedRecoverySBCorrection()));
                } else {
                    pstmt.setInt(18, 0);
                }
                if (loanForm.getBank() != null && !loanForm.getBank().equals("")) {
                    pstmt.setString(19, loanForm.getBank());
                } else {
                    pstmt.setString(19, null);
                }
                if (loanForm.getBranch() != null && !loanForm.getBranch().equals("")) {
                    pstmt.setString(20, loanForm.getBranch());
                } else {
                    pstmt.setString(20, null);
                }
                pstmt.setString(21, loanForm.getBtid());
                if (loanForm.getSltloan() != null && loanForm.getSltloan().equals("NPSL") && loanForm.getSltGovernmentContr() != null && !loanForm.getSltGovernmentContr().equals("")) {
                    pstmt.setInt(22, Integer.parseInt(loanForm.getSltGovernmentContr()));
                } else {
                    pstmt.setInt(22, 0);
                }
                pstmt.setInt(23, sbcorrectionid);
                pstmt.setString(24, loanForm.getLoanid());
                pstmt.executeUpdate();
            } else if (loanForm.getNowDeduct().equals("I") && !loanId.equals("")) {
                pstmt = con.prepareStatement("insert into EMP_LOAN_SANC_SB_CORRECTION(not_id, not_type, emp_id, loan_tp, amount,ACC_NO,VCH_NO,VCH_DATE,TR_CODE,NOW_DEDN,DED_ST_DATE,I_ORG_AMT,I_TOT_NO_INST,I_INSTL_AMT,I_LAST_INSTL_NO,I_INSTL_NO,I_CUM_RECOVERED,I_RECOVERED,BANK_CODE,BRANCH_CODE,bt_id_i,prin_reference_loan_id,ref_correction_id,loanid) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
                pstmt.setInt(1, notId);
                pstmt.setString(2, "LOAN_SANC");
                pstmt.setString(3, loanForm.getEmpid());
                pstmt.setString(4, loanForm.getSltloan());
                pstmt.setDouble(5, loanForm.getTxtamount());
                if (loanForm.getAccountNo() != null && !loanForm.getAccountNo().equalsIgnoreCase("")) {
                    pstmt.setString(6, loanForm.getAccountNo());
                } else {
                    pstmt.setString(6, null);
                }
                if (loanForm.getVoucherNo() != null && !loanForm.getVoucherNo().equalsIgnoreCase("")) {
                    pstmt.setString(7, loanForm.getVoucherNo());
                } else {
                    pstmt.setString(7, null);
                }
                if (loanForm.getVoucherDate() != null && !loanForm.getVoucherDate().equals("")) {
                    Date date1 = new SimpleDateFormat("dd-MMM-yyyy").parse(loanForm.getVoucherDate());
                    pstmt.setDate(8, new java.sql.Date(date1.getTime()));
                } else {
                    pstmt.setDate(8, null);
                }
                if (loanForm.getTreasuryname() != null && !loanForm.getTreasuryname().equalsIgnoreCase("")) {
                    pstmt.setString(9, loanForm.getTreasuryname());
                } else {
                    pstmt.setString(9, null);
                }

                pstmt.setString(10, loanForm.getNowDeduct());
                if (loanForm.getDeductionStartDate() != null && !loanForm.getDeductionStartDate().equals("")) {
                    pstmt.setDate(11, new java.sql.Date(loanForm.getDeductionStartDate().getTime()));
                } else {
                    pstmt.setDate(11, null);
                }

                pstmt.setInt(12, loanForm.getOriginalAmt());
                pstmt.setInt(13, loanForm.getTotalNoOfInsl());
                pstmt.setInt(14, loanForm.getInstalmentAmount());
                pstmt.setInt(15, loanForm.getLastPaidInstalNo());
                pstmt.setInt(16, loanForm.getMonthlyinstlno());
                pstmt.setInt(17, loanForm.getCumulativeAmtPaid());
                if (loanForm.getCompletedRecoverySBCorrection() != null && !loanForm.getCompletedRecoverySBCorrection().equals("")) {
                    pstmt.setInt(18, Integer.parseInt(loanForm.getCompletedRecoverySBCorrection()));
                } else {
                    pstmt.setInt(18, 0);
                }
                if (loanForm.getBank() != null && !loanForm.getBank().equals("")) {
                    pstmt.setString(19, loanForm.getBank());
                } else {
                    pstmt.setString(19, null);
                }
                if (loanForm.getBranch() != null && !loanForm.getBranch().equals("")) {
                    pstmt.setString(20, loanForm.getBranch());
                } else {
                    pstmt.setString(20, null);
                }
                pstmt.setString(21, loanForm.getBtid());
                pstmt.setString(22, loanForm.getPrinloanid());
                pstmt.setInt(23, sbcorrectionid);
                pstmt.setString(24, loanForm.getLoanid());
                pstmt.executeUpdate();
            }
            if (loanForm.getNowDeduct().equals("P")) {
                loanForm.setNotid(notId);
                sbLang = sbDAO.getLoanSancDetails(loanForm);
                pstmt = con.prepareStatement("UPDATE EMP_NOTIFICATION_LOG SET SB_DESCRIPTION =? WHERE NOT_ID=? ");
                pstmt.setString(1, sbLang);
                pstmt.setInt(2, notId);
                pstmt.execute();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return sbLang;
    }

    @Override
    public Loan editLoanDataSBCorrectionDDO(String loanId, int sbcorrectionid) {

        Connection con = null;
        PreparedStatement pstmt = null;
        Loan lfb = new Loan();
        ResultSet rs = null;

        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("select emploan.file_name, emploan.original_name, emploan.doc_name,loan_gc_percent,empnoti.if_visible,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMPNAME,post,empnoti.not_id as notid,note,empnoti.not_type as nottype, empnoti.emp_id as empid, loan_tp, amount,ACC_NO,VCH_NO,VCH_DATE,emploan.TR_CODE trcode,emploan.NOW_DEDN as nowdedn, "
                    + " DED_ST_DATE,P_ORG_AMT,P_TOT_NO_INSTL, P_INSTL_AMT,P_LAST_INSTL_NO,P_LAST_PMT_MON,P_CUM_RECOVERED,I_ORG_AMT,I_TOT_NO_INST, I_INSTL_NO, "
                    + " I_INSTL_AMT,I_LAST_INSTL_NO,I_LAST_PMT_MON, I_CUM_RECOVERED,P_RECOVERED,I_RECOVERED,PVT_DESC,emploan.BANK_CODE as bankcode,emploan.BRANCH_CODE as branchcode, "
                    + " BT_ID_P,BT_ID_I,P_INSTL_NO,auth,ordno,orddt,empnoti.dept_code deptcode,empnoti.off_code as offcode, IS_VERIFIED,bt_id_p,bt_id_i from EMP_LOAN_SANC_SB_CORRECTION emploan  "
                    + " left outer join emp_notification_log empnoti on emploan.not_id=empnoti.not_id "
                    + " left outer join emp_mast empmast on empnoti.auth=empmast.cur_spc "
                    + " left outer join g_spc gspc on empmast.cur_spc=gspc.spc "
                    + " left outer join g_post gpost on gspc.gpc=gpost.post_code "
                    + " where emploan.loanid=? and empnoti.REF_CORRECTION_ID=?");
            pstmt.setString(1, loanId);
            pstmt.setInt(2, sbcorrectionid);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                lfb = new Loan();
                lfb.setLoanid(loanId);
                lfb.setNotid(rs.getInt("notid"));
                lfb.setOrderno(rs.getString("ordno"));
                lfb.setOrderdate(CommonFunctions.getFormattedOutputDate1(rs.getDate("orddt")));
                lfb.setNotType(rs.getString("nottype"));
                lfb.setEmpid(rs.getString("empid"));
                lfb.setSltloan(rs.getString("loan_tp"));
                lfb.setTxtamount(rs.getInt("amount"));
                if (rs.getString("empname") != null && rs.getString("post") != null) {
                    lfb.setAuthority(rs.getString("empname") + "," + rs.getString("post"));
                } else {
                    lfb.setAuthority("");
                }
                lfb.setHidAuthDeptCode(rs.getString("deptcode"));
                lfb.setHidAuthOffCode(rs.getString("offcode"));
                lfb.setHidSpcAuthCode(rs.getString("auth"));
                lfb.setAccountNo(rs.getString("ACC_NO"));
                lfb.setVoucherNo(rs.getString("VCH_NO"));
                lfb.setVoucherDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("VCH_DATE")));
                lfb.setTreasuryname(rs.getString("trcode"));
                lfb.setNowDeduct(rs.getString("nowdedn"));
                lfb.setDeductionStartDate(rs.getDate("DED_ST_DATE"));
                if (lfb.getNowDeduct() != null && lfb.getNowDeduct().equalsIgnoreCase("P")) {
                    lfb.setOriginalAmt(rs.getInt("P_ORG_AMT"));
                    lfb.setTotalNoOfInsl(rs.getInt("P_TOT_NO_INSTL"));
                    lfb.setInstalmentAmount(rs.getInt("P_INSTL_AMT"));
                    lfb.setLastPaidInstalNo(rs.getInt("P_LAST_INSTL_NO"));
                    lfb.setMonthlyinstlno(rs.getInt("P_INSTL_NO"));
                    lfb.setCumulativeAmtPaid(rs.getInt("P_CUM_RECOVERED"));
                    lfb.setCompletedRecoverySBCorrection(rs.getString("P_RECOVERED"));
                    lfb.setBtid(rs.getString("bt_id_p"));
                } else {
                    lfb.setOriginalAmt(rs.getInt("I_ORG_AMT"));
                    lfb.setTotalNoOfInsl(rs.getInt("I_TOT_NO_INST"));
                    lfb.setInstalmentAmount(rs.getInt("I_INSTL_AMT"));
                    lfb.setLastPaidInstalNo(rs.getInt("I_LAST_INSTL_NO"));
                    lfb.setMonthlyinstlno(rs.getInt("I_INSTL_NO"));
                    lfb.setCumulativeAmtPaid(rs.getInt("I_CUM_RECOVERED"));
                    lfb.setCompletedRecoverySBCorrection(rs.getString("I_RECOVERED"));
                    lfb.setBtid(rs.getString("bt_id_i"));
                }
                lfb.setTxtpvtloan(rs.getString("PVT_DESC"));
                lfb.setBank(rs.getString("bankcode"));
                lfb.setBranch(rs.getString("branchcode"));
                lfb.setNote(rs.getString("note"));
                lfb.setIsverified(rs.getString("IS_VERIFIED"));
                if (rs.getString("if_visible") != null && !rs.getString("if_visible").equals("")) {
                    if (rs.getString("if_visible").equals("Y")) {
                        lfb.setChkNotSBPrint("N");
                    } else if (rs.getString("if_visible").equals("N")) {
                        lfb.setChkNotSBPrint("Y");
                    }
                }
                lfb.setSltGovernmentContr(rs.getString("loan_gc_percent"));
                lfb.setOriginalFileNameGcfileDoc(rs.getString("original_name"));
                lfb.setDiskFilenameGcfileDoc(rs.getString("file_name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return lfb;
    }

    @Override
    public void approveLoansanctionDataSBCorrection(Loan loanForm, String entrydeptcode, String entryoffcode, String entryspc, String loginuserid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        try {
            con = this.dataSource.getConnection();

            if (loanForm.getEntryTypeSBCorrection() != null && loanForm.getEntryTypeSBCorrection().equals("NEW")) {
                pst = con.prepareStatement("INSERT INTO EMP_NOTIFICATION(NOT_TYPE,EMP_ID,DOE,ORDNO,ORDDT,DEPT_CODE,OFF_CODE,AUTH,IF_VISIBLE,ENT_DEPT,ENT_OFF,ENT_AUTH) "
                        + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
                pst.setString(1, "LOAN_SANC");
                pst.setString(2, loanForm.getEmpid());
                pst.setTimestamp(3, new Timestamp(new Date().getTime()));
                pst.setString(4, loanForm.getOrderno());
                if (loanForm.getOrderdate() != null) {
                    pst.setTimestamp(5, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(loanForm.getOrderdate()).getTime()));
                } else {
                    pst.setTimestamp(5, null);
                }
                pst.setString(6, loanForm.getHidSancDeptCode());
                pst.setString(7, loanForm.getHidSancOffCode());
                pst.setString(8, loanForm.getSancSpc());
                pst.setString(9, "Y");
                pst.setString(10, entrydeptcode);
                pst.setString(11, entryoffcode);
                pst.setString(12, entryspc);
                pst.executeUpdate();

                rs = pst.getGeneratedKeys();
                rs.next();
                int notId = rs.getInt("NOT_ID");

                DataBaseFunctions.closeSqlObjects(rs, pst);

                String sqlString = ToStringBuilder.reflectionToString(pst);
                pst = con.prepareStatement("UPDATE emp_notification SET MODIFIED_BY=?,MODIFIED_ON=?,query_string=? where NOT_ID=?");
                pst.setString(1, loginuserid);
                pst.setTimestamp(2, new Timestamp(new Date().getTime()));
                pst.setString(3, sqlString);
                pst.setInt(4, notId);
                pst.executeUpdate();

                DataBaseFunctions.closeSqlObjects(rs, pst);

                pst = con.prepareStatement("insert into emp_loan_sanc(not_id, not_type, emp_id, loan_tp, amount,ACC_NO,VCH_NO,VCH_DATE,TR_CODE,NOW_DEDN,DED_ST_DATE,P_ORG_AMT,P_TOT_NO_INSTL,P_INSTL_AMT,P_LAST_INSTL_NO,P_INSTL_NO,P_CUM_RECOVERED,P_RECOVERED,BANK_CODE,BRANCH_CODE,bt_id_p,loan_gc_percent) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
                pst.setInt(1, notId);
                pst.setString(2, "LOAN_SANC");
                pst.setString(3, loanForm.getEmpid());
                pst.setString(4, loanForm.getSltloan());
                pst.setDouble(5, loanForm.getTxtamount());
                if (loanForm.getAccountNo() != null && !loanForm.getAccountNo().equalsIgnoreCase("")) {
                    pst.setString(6, loanForm.getAccountNo());
                } else {
                    pst.setString(6, null);
                }
                if (loanForm.getVoucherNo() != null && !loanForm.getVoucherNo().equalsIgnoreCase("")) {
                    pst.setString(7, loanForm.getVoucherNo());
                } else {
                    pst.setString(7, null);
                }
                if (loanForm.getVoucherDate() != null && !loanForm.getVoucherDate().equals("")) {
                    Date date1 = new SimpleDateFormat("dd-MMM-yyyy").parse(loanForm.getVoucherDate());
                    pst.setDate(8, new java.sql.Date(date1.getTime()));
                } else {
                    pst.setDate(8, null);
                }
                if (loanForm.getTreasuryname() != null && !loanForm.getTreasuryname().equalsIgnoreCase("")) {
                    pst.setString(9, loanForm.getTreasuryname());
                } else {
                    pst.setString(9, null);
                }

                pst.setString(10, loanForm.getNowDeduct());
                if (loanForm.getDeductionStartDate() != null && !loanForm.getDeductionStartDate().equals("")) {
                    pst.setDate(11, new java.sql.Date(loanForm.getDeductionStartDate().getTime()));
                } else {
                    pst.setDate(11, null);
                }
                pst.setInt(12, loanForm.getOriginalAmt());
                pst.setInt(13, loanForm.getTotalNoOfInsl());
                pst.setInt(14, loanForm.getInstalmentAmount());
                pst.setInt(15, loanForm.getLastPaidInstalNo());
                pst.setInt(16, loanForm.getMonthlyinstlno());
                pst.setInt(17, loanForm.getCumulativeAmtPaid());
                pst.setInt(18, loanForm.getCompletedRecovery());
                if (loanForm.getBank() != null && !loanForm.getBank().equals("")) {
                    pst.setString(19, loanForm.getBank());
                } else {
                    pst.setString(19, null);
                }
                if (loanForm.getBranch() != null && !loanForm.getBranch().equals("")) {
                    pst.setString(20, loanForm.getBranch());
                } else {
                    pst.setString(20, null);
                }
                pst.setString(21, loanForm.getBtid());
                if (loanForm.getSltloan() != null && loanForm.getSltloan().equals("NPSL") && loanForm.getSltGovernmentContr() != null && !loanForm.getSltGovernmentContr().equals("")) {
                    pst.setInt(22, Integer.parseInt(loanForm.getSltGovernmentContr()));
                } else {
                    pst.setInt(22, 0);
                }
                pst.executeUpdate();

                DataBaseFunctions.closeSqlObjects(rs, pst);

                if (loanForm.getNowDeduct().equals("P")) {
                    String sbLang = sbDAO.getLoanSancDetails(loanForm);
                    pst = con.prepareStatement("UPDATE EMP_NOTIFICATION SET SB_DESCRIPTION=? WHERE NOT_ID=?");
                    pst.setString(1, sbLang);
                    pst.setInt(2, notId);
                    pst.execute();
                }
            } else {
                NotificationBean nb = new NotificationBean();

                nb.setNottype("LOAN_SANC");
                nb.setNotid(loanForm.getNotid());
                nb.setEmpId(loanForm.getEmpid());
                nb.setDateofEntry(new Date());
                nb.setOrdno(loanForm.getOrderno());
                nb.setOrdDate(CommonFunctions.getDateFromString(loanForm.getOrderdate(), "dd-MMM-yyyy"));
                nb.setNote(loanForm.getNote());
                nb.setSancDeptCode(loanForm.getHidSancDeptCode());
                nb.setSancOffCode(loanForm.getHidSancOffCode());
                nb.setSancAuthCode(loanForm.getSancSpc());
                if (loanForm.getChkNotSBPrint() != null && loanForm.getChkNotSBPrint().equals("Y")) {
                    nb.setIfVisible("N");
                }
                notificationDao.modifyNotificationDataSBCorrection(nb);

                pst = con.prepareStatement("update EMP_LOAN_SANC_SB_CORRECTION set loan_tp=?, amount=?,ACC_NO=?,VCH_NO=?,VCH_DATE=?,TR_CODE=?,NOW_DEDN=?,DED_ST_DATE=?,P_ORG_AMT=?,P_TOT_NO_INSTL=?,P_INSTL_AMT=?,P_LAST_INSTL_NO=?,P_INSTL_NO=?,P_CUM_RECOVERED=?,P_RECOVERED=?,BANK_CODE=?,BRANCH_CODE=?,bt_id_p=?, not_id=?,loan_gc_percent=? where loanid=?");
                pst.setString(1, loanForm.getSltloan());
                pst.setDouble(2, loanForm.getTxtamount());
                if (loanForm.getAccountNo() != null && !loanForm.getAccountNo().equalsIgnoreCase("")) {
                    pst.setString(3, loanForm.getAccountNo());
                } else {
                    pst.setString(3, null);
                }
                if (loanForm.getVoucherNo() != null && !loanForm.getVoucherNo().equalsIgnoreCase("")) {
                    pst.setString(4, loanForm.getVoucherNo());
                } else {
                    pst.setString(4, null);
                }
                if (loanForm.getVoucherDate() != null && !loanForm.getVoucherDate().equals("")) {
                    Date date1 = new SimpleDateFormat("dd-MMM-yyyy").parse(loanForm.getVoucherDate());
                    pst.setDate(5, new java.sql.Date(date1.getTime()));
                } else {
                    pst.setDate(5, null);
                }
                if (loanForm.getTreasuryname() != null && !loanForm.getTreasuryname().equalsIgnoreCase("")) {
                    pst.setString(6, loanForm.getTreasuryname());
                } else {
                    pst.setString(6, null);
                }

                if (loanForm.getNowDeduct() != null && !loanForm.getNowDeduct().equalsIgnoreCase("")) {
                    pst.setString(7, loanForm.getNowDeduct());
                } else {
                    pst.setString(7, null);
                }
                if (loanForm.getDeductionStartDate() != null && !loanForm.getDeductionStartDate().equals("")) {
                    pst.setDate(8, new java.sql.Date(loanForm.getDeductionStartDate().getTime()));
                } else {
                    pst.setDate(8, null);
                }
                pst.setInt(9, loanForm.getOriginalAmt());
                pst.setInt(10, loanForm.getTotalNoOfInsl());
                pst.setInt(11, loanForm.getInstalmentAmount());
                pst.setInt(12, loanForm.getLastPaidInstalNo());
                pst.setInt(13, loanForm.getMonthlyinstlno());
                pst.setInt(14, loanForm.getCumulativeAmtPaid());
                if (loanForm.getCompletedRecoverySBCorrection() != null && !loanForm.getCompletedRecoverySBCorrection().equals("")) {
                    pst.setInt(15, Integer.parseInt(loanForm.getCompletedRecoverySBCorrection()));
                } else {
                    pst.setInt(15, 0);
                }
                if (loanForm.getBank() != null && !loanForm.getBank().equals("")) {
                    pst.setString(16, loanForm.getBank());
                } else {
                    pst.setString(16, null);
                }
                if (loanForm.getBranch() != null && !loanForm.getBranch().equals("")) {
                    pst.setString(17, loanForm.getBranch());
                } else {
                    pst.setString(17, null);
                }
                pst.setString(18, loanForm.getBtid());
                pst.setInt(19, loanForm.getNotid());
                if (loanForm.getSltloan() != null && loanForm.getSltloan().equals("NPSL") && loanForm.getSltGovernmentContr() != null && !loanForm.getSltGovernmentContr().equals("")) {
                    pst.setInt(20, Integer.parseInt(loanForm.getSltGovernmentContr()));
                } else {
                    pst.setInt(20, 0);
                }
                pst.setString(21, loanForm.getLoanid());
                pst.executeUpdate();

                DataBaseFunctions.closeSqlObjects(pst);

                String sql = "UPDATE emp_notification"
                        + " SET ordno = emp_notification_log.ordno,orddt=emp_notification_log.orddt,dept_code=emp_notification_log.dept_code,off_code=emp_notification_log.off_code,auth=emp_notification_log.auth,organization_type=emp_notification_log.organization_type,organization_type_posting=emp_notification_log.organization_type_posting"
                        + " FROM emp_notification_log"
                        + " WHERE emp_notification.not_id = emp_notification_log.not_id and emp_notification_log.not_id=?";
                pst = con.prepareStatement(sql);
                pst.setInt(1, loanForm.getNotid());
                pst.executeUpdate();

                DataBaseFunctions.closeSqlObjects(pst);

                sql = "UPDATE emp_loan_sanc"
                        + " SET amount = EMP_LOAN_SANC_SB_CORRECTION.amount,acc_no=EMP_LOAN_SANC_SB_CORRECTION.acc_no,tr_code=EMP_LOAN_SANC_SB_CORRECTION.tr_code,vch_no=EMP_LOAN_SANC_SB_CORRECTION.vch_no,"
                        + " vch_date=EMP_LOAN_SANC_SB_CORRECTION.vch_date,p_org_amt=EMP_LOAN_SANC_SB_CORRECTION.p_org_amt,p_tot_no_instl=EMP_LOAN_SANC_SB_CORRECTION.p_tot_no_instl,p_instl_amt=EMP_LOAN_SANC_SB_CORRECTION.p_instl_amt,"
                        + " p_last_instl_no=EMP_LOAN_SANC_SB_CORRECTION.p_last_instl_no,p_last_pmt_mon=EMP_LOAN_SANC_SB_CORRECTION.p_last_pmt_mon,p_cum_recovered=EMP_LOAN_SANC_SB_CORRECTION.p_cum_recovered,"
                        + " p_recovered=EMP_LOAN_SANC_SB_CORRECTION.p_recovered,i_org_amt=EMP_LOAN_SANC_SB_CORRECTION.i_org_amt,i_tot_no_inst=EMP_LOAN_SANC_SB_CORRECTION.i_tot_no_inst,"
                        + " i_instl_amt=EMP_LOAN_SANC_SB_CORRECTION.i_instl_amt,i_last_instl_no=EMP_LOAN_SANC_SB_CORRECTION.i_last_instl_no,i_last_pmt_mon=EMP_LOAN_SANC_SB_CORRECTION.i_last_pmt_mon,"
                        + " i_cum_recovered=EMP_LOAN_SANC_SB_CORRECTION.i_cum_recovered,now_dedn=EMP_LOAN_SANC_SB_CORRECTION.now_dedn,p_instl_no=EMP_LOAN_SANC_SB_CORRECTION.p_instl_no,"
                        + " i_instl_no=EMP_LOAN_SANC_SB_CORRECTION.i_instl_no FROM EMP_LOAN_SANC_SB_CORRECTION"
                        + " WHERE emp_loan_sanc.not_id = EMP_LOAN_SANC_SB_CORRECTION.not_id and EMP_LOAN_SANC_SB_CORRECTION.not_id=?";
                pst = con.prepareStatement(sql);
                pst.setInt(1, loanForm.getNotid());
                pst.executeUpdate();

                if (loanForm.getNowDeduct().equals("P")) {
                    /*
                     * Updating the Service Book Language
                     */
                    loanForm.setNotid(loanForm.getNotid());
                    String sbLang = sbDAO.getLoanSancDetails(loanForm);
                    pst = con.prepareStatement("UPDATE EMP_NOTIFICATION SET SB_DESCRIPTION=? WHERE NOT_ID=?");
                    pst.setString(1, sbLang);
                    pst.setInt(2, loanForm.getNotid());
                    pst.execute();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    public class LoanRefernce {

        private int noofinstalmentRecovered;
        private int cummAmtRecovered;

        public int getNoofinstalmentRecovered() {
            return noofinstalmentRecovered;
        }

        public void setNoofinstalmentRecovered(int noofinstalmentRecovered) {
            this.noofinstalmentRecovered = noofinstalmentRecovered;
        }

        public int getCummAmtRecovered() {
            return cummAmtRecovered;
        }

        public void setCummAmtRecovered(int cummAmtRecovered) {
            this.cummAmtRecovered = cummAmtRecovered;
        }

    }

    public LoanRefernce getRefCount(String loanId, String nowdedn, String empId, int loanemi) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet res = null;
        int cummAmtRecovered = 0;
        int noofinstalmentRecovered = 0;
        LoanRefernce loanRefernce = new LoanRefernce();
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT INSTALMENT_COUNT,AD_AMT FROM AQ_DTLS WHERE AD_REF_ID = ? AND NOW_DEDN=? AND AD_AMT != 0");
            pstmt.setString(1, loanId);
            pstmt.setString(2, nowdedn);
            res = pstmt.executeQuery();
            while (res.next()) {
                cummAmtRecovered = cummAmtRecovered + res.getInt("AD_AMT");
                noofinstalmentRecovered = noofinstalmentRecovered + res.getInt("INSTALMENT_COUNT");
            }
            pstmt = con.prepareStatement("SELECT sum(INSTALMENT_COUNT) as INSTALMENT_COUNT,sum(AD_AMT) as AD_AMT FROM hrmis.AQ_DTLS1 WHERE EMP_CODE=? AND AD_REF_ID = ? AND NOW_DEDN=? AND AD_AMT != 0");
            pstmt.setString(1, empId);
            pstmt.setString(2, loanId);
            pstmt.setString(3, nowdedn);
            res = pstmt.executeQuery();
            if (res.next()) {
                int emi = res.getInt("AD_AMT");
                int emicnt = res.getInt("INSTALMENT_COUNT");

                cummAmtRecovered = cummAmtRecovered + emi;
                noofinstalmentRecovered = noofinstalmentRecovered + emicnt;
            }
            pstmt = con.prepareStatement("SELECT AD_AMT,INSTALMENT_COUNT FROM LOAN_ADJUSTMENT WHERE AD_REF_ID = ? AND DED_TYPE=? AND AD_AMT != 0");
            pstmt.setString(1, loanId);
            pstmt.setString(2, nowdedn);
            res = pstmt.executeQuery();
            while (res.next()) {
                cummAmtRecovered = cummAmtRecovered + res.getInt("AD_AMT");
                noofinstalmentRecovered = noofinstalmentRecovered + res.getInt("INSTALMENT_COUNT");
            }
            loanRefernce.setCummAmtRecovered(cummAmtRecovered);
            loanRefernce.setNoofinstalmentRecovered(noofinstalmentRecovered);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(res, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return loanRefernce;
    }

    @Override
    public List getPrincipalLoanListForBill(String empId, int payday, String depcode) {
        List loanlist = new ArrayList();
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        PreparedStatement pstmt1 = null;
        Loan loanForm = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT loan_gc_percent,LOANID,G_LOAN.LOAN_TP,LOAN_NAME,DISB_DATE,P_TOT_NO_INSTL,P_INSTL_AMT,P_LAST_INSTL_NO,ACC_NO, ORDNO ,G_LOAN.REP_COL,G_LOAN.ROW_NO,HAVE_INT,NOW_DEDN,P_ORG_AMT,"
                    + "BT_CODE_PRINCIPAL,BT_CODE_INTEREST,BT_ID_P,P_CUM_RECOVERED FROM (SELECT loan_gc_percent,LOANID,LOAN_TP,DISB_DATE,P_TOT_NO_INSTL,P_INSTL_AMT,P_LAST_INSTL_NO,ACC_NO,GETORDNO(NOT_ID) ORDNO,NOW_DEDN,P_ORG_AMT,BT_ID_P,P_CUM_RECOVERED FROM EMP_LOAN_SANC "
                    + "where (P_RECOVERED IS NULL OR P_RECOVERED = '0') AND (P_INSTL_AMT IS NOT NULL OR P_INSTL_AMT > 0) AND NOW_DEDN = 'P' AND (STOP_LOAN IS NULL OR STOP_LOAN = 'N') AND  EMP_ID = ? "
                    + "UNION "
                    + "SELECT loan_gc_percent,LOANID,LOAN_TP,DISB_DATE,P_TOT_NO_INSTL,P_INSTL_AMT,P_LAST_INSTL_NO,ACC_NO,GETORDNO(NOT_ID) ORDNO,NOW_DEDN,P_ORG_AMT,BT_ID_P,P_CUM_RECOVERED FROM EMP_LOAN_SANC WHERE EMP_ID = ? AND NOW_DEDN = 'I' AND "
                    + "(I_RECOVERED IS NULL OR I_RECOVERED = '0'))EMP_LOAN_SANC1 RIGHT OUTER JOIN G_LOAN ON EMP_LOAN_SANC1.LOAN_TP = G_LOAN.LOAN_TP");
            pstmt.setString(1, empId);
            pstmt.setString(2, empId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                loanForm = new Loan();
                loanForm.setLoanid(rs.getString("LOANID"));
                loanForm.setSltloan(rs.getString("LOAN_TP"));
                loanForm.setLoanName(rs.getString("LOAN_NAME"));
                loanForm.setOrderdate(CommonFunctions.getFormattedOutputDate1(rs.getDate("DISB_DATE")));
                loanForm.setTotalNoOfInsl(rs.getInt("P_TOT_NO_INSTL"));
                loanForm.setInstalmentAmount(rs.getInt("P_INSTL_AMT"));
                loanForm.setLastPaidInstalNo(rs.getInt("P_LAST_INSTL_NO"));
                loanForm.setAccountNo(rs.getString("ACC_NO"));
                loanForm.setOrderno(rs.getString("ORDNO"));
                loanForm.setRepcol(rs.getInt("REP_COL"));
                loanForm.setRowno(rs.getInt("ROW_NO"));
                loanForm.setHaveInt(rs.getString("HAVE_INT"));
                loanForm.setNowdedn(rs.getString("NOW_DEDN"));
                loanForm.setOriginalAmt(rs.getInt("P_ORG_AMT"));
                if (rs.getString("BT_ID_P") != null && !rs.getString("BT_ID_P").equals("")) {
                    loanForm.setBtid(rs.getString("BT_ID_P"));
                } else {
                    loanForm.setBtid(rs.getString("BT_CODE_PRINCIPAL"));
                }
                loanForm.setCumulativeAmtPaid(rs.getInt("P_CUM_RECOVERED"));

                int adamt = loanForm.getInstalmentAmount();
                int cumamtrec = 0;
                int refcount = 0;
                String refdesc = "";

                double cpfamt_gc = 0;

                // if (loanForm.getLoanid() != null && (loanForm.getNowdedn().equals("P") || loanForm.getNowdedn().equals("B")) && payday >= 15 && (depcode == null || !depcode.equals("05"))) {
                if (loanForm.getLoanid() != null && (loanForm.getNowdedn().equals("P") || loanForm.getNowdedn().equals("B")) && payday >= 15) {
                    LoanRefernce loanRefernce = getRefCount(loanForm.getLoanid(), loanForm.getNowdedn(), empId, adamt);
                    cumamtrec = loanRefernce.getCummAmtRecovered() + loanForm.getCumulativeAmtPaid();
                    int calamt = cumamtrec + adamt;
                    refcount = loanRefernce.getNoofinstalmentRecovered() + loanForm.getLastPaidInstalNo() + 1;
                    if (refcount == loanForm.getTotalNoOfInsl()) {
                        refdesc = refcount + "/" + loanForm.getTotalNoOfInsl();
                        if (calamt > loanForm.getOriginalAmt() && loanForm.getOriginalAmt() > 0) {
                            int extra_amt = calamt - loanForm.getOriginalAmt();
                            adamt = adamt - extra_amt;
                            cumamtrec = cumamtrec + adamt;
                        } else if (calamt < loanForm.getOriginalAmt() && loanForm.getOriginalAmt() > 0) {
                            int extra_amt = loanForm.getOriginalAmt() - calamt;
                            adamt = adamt + extra_amt;
                            cumamtrec = cumamtrec + adamt;
                        } else {
                            cumamtrec = cumamtrec + adamt;
                        }
                        if (loanForm.getSltloan().equals("NPSL")) {
                            if (rs.getString("loan_gc_percent") != null && !rs.getString("loan_gc_percent").equals("")) {
                                if (rs.getString("loan_gc_percent").equals("14")) {
                                    cpfamt_gc = loanForm.getInstalmentAmount() * 0.14;
                                } else if (rs.getString("loan_gc_percent").equals("10")) {
                                    cpfamt_gc = loanForm.getInstalmentAmount() * 0.1;
                                }
                            }
                        }
                    } else if (refcount < loanForm.getTotalNoOfInsl()) {
                        cumamtrec = loanRefernce.getCummAmtRecovered() + loanForm.getCumulativeAmtPaid() + adamt;
                        refdesc = refcount + "/" + loanForm.getTotalNoOfInsl();
                        if (loanForm.getSltloan().equals("NPSL")) {
                            if (rs.getString("loan_gc_percent") != null && !rs.getString("loan_gc_percent").equals("")) {
                                if (rs.getString("loan_gc_percent").equals("14")) {
                                    cpfamt_gc = loanForm.getInstalmentAmount() * 0.14;
                                } else if (rs.getString("loan_gc_percent").equals("10")) {
                                    cpfamt_gc = loanForm.getInstalmentAmount() * 0.1;
                                }
                            }
                        }
                    } else {
                        pstmt1 = con.prepareStatement("UPDATE EMP_LOAN_SANC SET P_RECOVERED = 1 WHERE LOANID=?");
                        pstmt1.setString(1, loanForm.getLoanid());
                        pstmt1.executeUpdate();
                        refcount = 0;
                        refdesc = "";
                        adamt = 0;
                        cumamtrec = 0;
                    }
                } else {
                    loanForm.setNowdedn("P");
                    refcount = 0;
                    refdesc = "";
                    adamt = 0;
                    cumamtrec = 0;
                }
                loanForm.setInstalmentAmount(adamt);
                loanForm.setCummrecovered(cumamtrec);
                loanForm.setRefCount(refcount);
                loanForm.setRefDesc(refdesc);
                loanForm.setCpfAmtGc(cpfamt_gc + "");
                loanForm.setGcPercent(rs.getString("loan_gc_percent"));
                loanlist.add(loanForm);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(pstmt1);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return loanlist;
    }

    @Override
    public List getInterestLoanListForBill(String empId, int payday, String depcode) {
        List loanlist = new ArrayList();
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        PreparedStatement pstmt1 = null;
        Loan loanForm = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT LOANID,G_LOAN.LOAN_TP,LOAN_NAME,DISB_DATE,I_TOT_NO_INST,I_INSTL_AMT,I_LAST_INSTL_NO,ACC_NO, ORDNO ,G_LOAN.REP_COL,G_LOAN.ROW_NO,NOW_DEDN,I_ORG_AMT,BT_CODE_INTEREST,I_CUM_RECOVERED "
                    + "FROM (SELECT LOANID,LOAN_TP,DISB_DATE,I_TOT_NO_INST,I_INSTL_AMT,I_LAST_INSTL_NO,ACC_NO,GETORDNO(NOT_ID) ORDNO,NOW_DEDN,I_ORG_AMT,I_CUM_RECOVERED FROM EMP_LOAN_SANC where (I_RECOVERED IS NULL OR I_RECOVERED = 0) AND "
                    + "(NOW_DEDN='I' OR NOW_DEDN='B') AND STOP_LOAN IS NULL AND EMP_ID = ?)EMP_LOAN_SANC1 INNER JOIN G_LOAN ON EMP_LOAN_SANC1.LOAN_TP = G_LOAN.LOAN_TP");
            pstmt.setString(1, empId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                loanForm = new Loan();
                loanForm.setLoanid(rs.getString("LOANID"));
                loanForm.setSltloan(rs.getString("LOAN_TP"));
                loanForm.setLoanName(rs.getString("LOAN_NAME"));
                loanForm.setOrderdate(CommonFunctions.getFormattedOutputDate1(rs.getDate("DISB_DATE")));
                loanForm.setTotalNoOfInsl(rs.getInt("I_TOT_NO_INST"));
                loanForm.setInstalmentAmount(rs.getInt("I_INSTL_AMT"));
                loanForm.setLastPaidInstalNo(rs.getInt("I_LAST_INSTL_NO"));
                loanForm.setAccountNo(rs.getString("ACC_NO"));
                loanForm.setOrderno(rs.getString("ORDNO"));
                loanForm.setRepcol(rs.getInt("REP_COL"));
                loanForm.setRowno(rs.getInt("ROW_NO"));
                loanForm.setNowdedn(rs.getString("NOW_DEDN"));
                loanForm.setOriginalAmt(rs.getInt("I_ORG_AMT"));
                loanForm.setBtid(rs.getString("BT_CODE_INTEREST"));
                loanForm.setCumulativeAmtPaid(rs.getInt("I_CUM_RECOVERED"));
                int adamt = loanForm.getInstalmentAmount();
                int cumamtrec = 0;
                int refcount = 0;
                String refdesc = "";
                // if (loanForm.getLoanid() != null && loanForm.getNowdedn().equals("I") && payday >= 15 && !depcode.equals("05")) {
                if (loanForm.getLoanid() != null && loanForm.getNowdedn().equals("I") && payday >= 15) {
                    LoanRefernce loanRefernce = getRefCount(loanForm.getLoanid(), loanForm.getNowdedn(), empId, adamt);
                    cumamtrec = loanRefernce.getCummAmtRecovered() + loanForm.getCumulativeAmtPaid();
                    int calamt = cumamtrec + adamt;
                    refcount = loanRefernce.getNoofinstalmentRecovered() + loanForm.getLastPaidInstalNo() + 1;
                    if (refcount == loanForm.getTotalNoOfInsl()) {
                        refdesc = refcount + "/" + loanForm.getTotalNoOfInsl();
                        if (calamt > loanForm.getOriginalAmt() && loanForm.getOriginalAmt() > 0) {
                            int extra_amt = calamt - loanForm.getOriginalAmt();
                            adamt = adamt - extra_amt;
                            cumamtrec = cumamtrec + adamt;
                        } else if (calamt < loanForm.getOriginalAmt() && loanForm.getOriginalAmt() > 0) {
                            int extra_amt = loanForm.getOriginalAmt() - calamt;
                            adamt = adamt + extra_amt;
                            cumamtrec = cumamtrec + adamt;
                        } else {
                            cumamtrec = cumamtrec + adamt;
                        }
                    } else if (refcount < loanForm.getTotalNoOfInsl()) {
                        cumamtrec = loanRefernce.getCummAmtRecovered() + loanForm.getCumulativeAmtPaid() + adamt;
                        refdesc = refcount + "/" + loanForm.getTotalNoOfInsl();
                    } else {
                        pstmt1 = con.prepareStatement("UPDATE EMP_LOAN_SANC SET I_RECOVERED = 1 WHERE LOANID=?");
                        pstmt1.setString(1, loanForm.getLoanid());
                        pstmt1.executeUpdate();
                        refcount = 0;
                        refdesc = "";
                        adamt = 0;
                        cumamtrec = 0;
                    }
                } else {
                    adamt = 0;
                }
                loanForm.setInstalmentAmount(adamt);
                loanForm.setCummrecovered(cumamtrec);
                loanForm.setRefCount(refcount);
                loanForm.setRefDesc(refdesc);
                loanlist.add(loanForm);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(pstmt1);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return loanlist;
    }

    @Override
    public List getLoanDeductionDeailEmpWise(String empId, String loanType) {
        List loanDetailList = new ArrayList();
        Connection con = null;
        PreparedStatement pstmt = null;
        PreparedStatement pstmt1 = null;
        PreparedStatement pstmt2 = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        ResultSet rs2 = null;
        Loan loan = null;
        List loanaccList = new ArrayList();
        List loanList = new ArrayList();
        EmployeeLoan employeeLoan = null;

        try {
            con = dataSource.getConnection();
            //pstmt = con.prepareStatement("select NOW_DEDN,LOANID,P_RECOVERED,I_RECOVERED,AMOUNT,DISB_DATE from emp_loan_sanc where emp_id=? and loan_tp=? GROUP BY LOANID,P_RECOVERED,I_RECOVERED,AMOUNT,DISB_DATE,NOW_DEDN ORDER BY P_RECOVERED DESC");
            pstmt = con.prepareStatement("select distinct emploan.LOANID,emploan.NOW_DEDN,P_RECOVERED,I_RECOVERED,AMOUNT,DISB_DATE  from\n"
                    + "(select NOW_DEDN,LOANID,P_RECOVERED,I_RECOVERED,AMOUNT,DISB_DATE from emp_loan_sanc where emp_id=? and loan_tp=?)emploan\n"
                    + "inner join \n"
                    + "(select ad_ref_id from\n"
                    + "(select distinct ad_ref_id from aq_dtls where emp_code=? and ad_code=? and AD_AMT>0)aqdtls \n"
                    + "union\n"
                    + "(select distinct ad_ref_id from hrmis.aq_dtls1 where emp_code=? and ad_code=? and AD_AMT>0))aqdtls1\n"
                    + "on emploan.loanid=aqdtls1.ad_ref_id");

            pstmt1 = con.prepareStatement("select VCH_NO,VCH_DATE,aq_mast.bill_no,BILL_DESC,aq_mast.bill_date,aq_dtls.* from (select aq_year,aq_month,REF_COUNT,AD_AMT,AQSL_NO,TOT_REC_AMT from aq_dtls where ad_ref_id=? and emp_code=? and AD_AMT>0)aq_dtls"
                    + " left outer join (select AQSL_NO,bill_no,emp_code,bill_date from aq_mast where emp_code=?)aq_mast on aq_dtls.AQSL_NO=aq_mast.AQSL_NO"
                    + " left outer join bill_mast on aq_mast.bill_no=bill_mast.bill_no order by ref_count,aq_dtls.aq_year,aq_dtls.aq_month asc");
            pstmt2 = con.prepareStatement("select VCH_NO,VCH_DATE,aq_mast.bill_no,BILL_DESC,aq_mast.bill_date,aq_dtls.* from (select aq_year,aq_month,REF_COUNT,AD_AMT,AQSL_NO,TOT_REC_AMT from hrmis.aq_dtls1 where ad_ref_id=? and emp_code=? and AD_AMT>0)aq_dtls"
                    + " left outer join (select AQSL_NO,bill_no,emp_code,bill_date from aq_mast where emp_code=?)aq_mast on aq_dtls.AQSL_NO=aq_mast.AQSL_NO"
                    + " left outer join bill_mast on aq_mast.bill_no=bill_mast.bill_no order by ref_count,aq_dtls.aq_year,aq_dtls.aq_month asc");

            pstmt.setString(1, empId);
            pstmt.setString(2, loanType);
            pstmt.setString(3, empId);
            pstmt.setString(4, loanType);
            pstmt.setString(5, empId);
            pstmt.setString(6, loanType);

            rs = pstmt.executeQuery();
            while (rs.next()) {
                loanaccList = new ArrayList();
                employeeLoan = new EmployeeLoan();
                employeeLoan.setLoanamt(rs.getString("AMOUNT"));
                employeeLoan.setLoandate(rs.getString("DISB_DATE"));
                employeeLoan.setNow_ded(rs.getString("NOW_DEDN"));
                employeeLoan.setLoan_id(rs.getString("LOANID"));
                if (employeeLoan.getNow_ded() != null && employeeLoan.getNow_ded().equals("P")) {
                    employeeLoan.setIscompleted(rs.getString("P_RECOVERED"));
                } else if (employeeLoan.getNow_ded() != null && employeeLoan.getNow_ded().equals("I")) {
                    employeeLoan.setIscompleted(rs.getString("I_RECOVERED"));
                }

                String adRefId = rs.getString("LOANID");
                pstmt1.setString(1, adRefId);
                pstmt1.setString(2, empId);
                pstmt1.setString(3, empId);
                rs1 = pstmt1.executeQuery();
                while (rs1.next()) {
                    EmployeeLoanAccBean ebean = new EmployeeLoanAccBean();

                    ebean.setDedmonth(CalendarCommonMethods.getMonthAsString(rs1.getInt("aq_month")));
                    ebean.setDedyear(rs1.getString("aq_year"));
                    ebean.setInslno(rs1.getString("REF_COUNT"));
                    ebean.setDedamt(Double.toString(rs1.getDouble("AD_AMT")));
                    double balance = 0;
                    if (employeeLoan.getLoanamt() != null && !employeeLoan.getLoanamt().equals("")) {
                        balance = Double.parseDouble(employeeLoan.getLoanamt()) - rs1.getDouble("TOT_REC_AMT");
                    }
                    ebean.setBal(Double.toString(balance));
                    //ebean.setBal(bal + "");
                    ebean.setBillNo(rs1.getString("BILL_DESC"));
                    ebean.setBilldate(CommonFunctions.getFormattedOutputDate1(rs1.getDate("BILL_DATE")));
                    ebean.setVchno(rs1.getString("VCH_NO"));
                    ebean.setVchdate(CommonFunctions.getFormattedOutputDate1(rs1.getDate("VCH_DATE")));
                    loanaccList.add(ebean);
                }

                pstmt2.setString(1, adRefId);
                pstmt2.setString(2, empId);
                pstmt2.setString(3, empId);
                rs2 = pstmt2.executeQuery();
                while (rs2.next()) {
                    EmployeeLoanAccBean ebean = new EmployeeLoanAccBean();
                    ebean.setLoanid(adRefId);
                    ebean.setDedmonth(CalendarCommonMethods.getMonthAsString(rs2.getInt("aq_month")));
                    ebean.setDedyear(rs2.getString("aq_year"));
                    ebean.setInslno(rs2.getString("REF_COUNT"));
                    ebean.setDedamt(Double.toString(rs2.getDouble("AD_AMT")));
                    int bal = 0;
                    double balance = 0;
                    if (employeeLoan.getLoanamt() != null && !employeeLoan.getLoanamt().equals("")) {
                        balance = Double.parseDouble(employeeLoan.getLoanamt()) - rs2.getDouble("TOT_REC_AMT");

                    }
                    ebean.setBal(Double.toString(balance));
                    ebean.setBillNo(rs2.getString("BILL_DESC"));
                    ebean.setBilldate(CommonFunctions.getFormattedOutputDate1(rs2.getDate("BILL_DATE")));
                    ebean.setVchno(rs2.getString("VCH_NO"));
                    ebean.setVchdate(CommonFunctions.getFormattedOutputDate1(rs2.getDate("VCH_DATE")));
                    loanaccList.add(ebean);
                }

                employeeLoan.setLoanAccList(loanaccList);
                loanList.add(employeeLoan);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(rs1, pstmt1);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return loanList;
    }

    @Override
    public String getFABTId(String demandno) {
        String btId = "";
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();

            pstmt = con.prepareStatement("SELECT fa_bt_id FROM g_demand_no where demand_no=? ");
            pstmt.setString(1, demandno);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                btId = rs.getString("fa_bt_id");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return btId;
    }

    @Override
    public String getGPFBTId(String gpfType) {
        String btId = "";
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();

            pstmt = con.prepareStatement("SELECT BT_ID  FROM G_GPF_TYPE WHERE GPF_TYPE = ?");
            pstmt.setString(1, gpfType);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                btId = rs.getString("BT_ID");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return btId;
    }

    @Override
    public AqDtlsModel[] getAqDtlsModelFromPLoanList(List principalLoanList, AqmastModel aqmast) {
        ArrayList<AqDtlsModel> list = new ArrayList<>();
        for (int i = 0; i < principalLoanList.size(); i++) {
            Loan loanForm = (Loan) principalLoanList.get(i);
            AqDtlsModel aqModel = new AqDtlsModel();
            aqModel.setAqGroup(aqmast.getAqGroup());
            aqModel.setAqSlNo(aqmast.getAqSlNo());
            aqModel.setDdoOff("");
            aqModel.setEmpCode(aqmast.getEmpCode());
            aqModel.setPayMon(aqmast.getPayMonth());
            aqModel.setPayYear(aqmast.getPayYear());
            aqModel.setAqDate(aqmast.getAqDate());
            aqModel.setAqMonth(aqmast.getAqMonth());
            aqModel.setAqYear(aqmast.getAqYear());
            aqModel.setAqType(aqmast.getAqType());
            aqModel.setRefOrderNo(aqmast.getRefOrder());
            aqModel.setRefOrderDate(aqmast.getRefDate());
            aqModel.setSlNo(loanForm.getRowno());
            aqModel.setAdCode(loanForm.getSltloan());
            aqModel.setAdDesc(loanForm.getLoanName());
            aqModel.setAdType("D");
            aqModel.setAltUnit(loanForm.getGcPercent());
            aqModel.setDedType("L");
            aqModel.setAdAmt(loanForm.getInstalmentAmount());
            aqModel.setAccNo(null);
            aqModel.setRefDesc(loanForm.getRefDesc());
            aqModel.setRefCount(loanForm.getRefCount());
            aqModel.setSchedule(loanForm.getSltloan());
            aqModel.setNowDedn(loanForm.getNowdedn());
            aqModel.setTotRecAmt(loanForm.getCummrecovered());
            aqModel.setRepCol(loanForm.getRepcol());
            aqModel.setAdRefId(loanForm.getLoanid());
            if (loanForm.getSltloan().equals("FA")) {
                aqModel.setBtId(getFABTId(aqmast.getDemandNumber()));
            } else if (loanForm.getSltloan().equals("GA")) {
                aqModel.setBtId(getGPFBTId(aqmast.getGpfType()));
            } else {
                aqModel.setBtId(loanForm.getBtid());
            }
            aqModel.setInstalCount(1);
            aqModel.setAdAmtGc(loanForm.getCpfAmtGc());
            list.add(aqModel);
        }
        AqDtlsModel aqDtlsModels[] = list.toArray(new AqDtlsModel[list.size()]);
        return aqDtlsModels;
    }

    @Override
    public AqDtlsModel[] getAqDtlsModelFromILoanList(List interestLoanList, AqmastModel aqmast) {
        ArrayList<AqDtlsModel> list = new ArrayList<>();
        for (int i = 0; i < interestLoanList.size(); i++) {
            Loan loanForm = (Loan) interestLoanList.get(i);
            AqDtlsModel aqModel = new AqDtlsModel();
            aqModel.setAqGroup(aqmast.getAqGroup());
            aqModel.setAqSlNo(aqmast.getAqSlNo());
            aqModel.setDdoOff("");
            aqModel.setEmpCode(aqmast.getEmpCode());
            aqModel.setPayMon(aqmast.getPayMonth());
            aqModel.setPayYear(aqmast.getPayYear());
            aqModel.setAqDate(aqmast.getAqDate());
            aqModel.setAqMonth(aqmast.getAqMonth());
            aqModel.setAqYear(aqmast.getAqYear());
            aqModel.setAqType(aqmast.getAqType());
            aqModel.setRefOrderNo(aqmast.getRefOrder());
            aqModel.setRefOrderDate(aqmast.getRefDate());
            aqModel.setSlNo(loanForm.getRowno());
            aqModel.setAdCode(loanForm.getSltloan());
            aqModel.setAdDesc(loanForm.getLoanName());
            aqModel.setAdType("D");
            aqModel.setAltUnit(null);
            aqModel.setDedType("L");
            aqModel.setAdAmt(loanForm.getInstalmentAmount());
            aqModel.setAccNo(null);
            aqModel.setRefDesc(loanForm.getRefDesc());
            aqModel.setRefCount(0);
            aqModel.setSchedule(loanForm.getSltloan());
            aqModel.setNowDedn(loanForm.getNowdedn());
            aqModel.setTotRecAmt(loanForm.getCummrecovered());
            aqModel.setRepCol(loanForm.getRepcol());
            aqModel.setAdRefId(loanForm.getLoanid());
            aqModel.setBtId(loanForm.getBtid());
            aqModel.setInstalCount(1);
            list.add(aqModel);
        }
        AqDtlsModel aqDtlsModels[] = list.toArray(new AqDtlsModel[list.size()]);
        return aqDtlsModels;
    }

    @Override
    public ArrayList getMonthWiseInstlment(String empCode, String loanId) {
        ArrayList monthwiseinstl = new ArrayList();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT AQ_MONTH,AQ_YEAR,AD_AMT,TOT_REC_AMT,REF_DESC,NOW_DEDN FROM AQ_DTLS WHERE AD_REF_ID=? AND AD_AMT > 0 ORDER BY AQ_YEAR DESC,AQ_MONTH DESC");
            pstmt.setString(1, loanId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Loan ln = new Loan();
                ln.setMonth(CommonFunctions.getMonthAsString(rs.getInt("AQ_MONTH")));
                ln.setYear("" + rs.getInt("AQ_YEAR"));
                ln.setInstalmentAmount(rs.getInt("AD_AMT"));
                ln.setCumulativeAmtPaid(rs.getInt("TOT_REC_AMT"));
                ln.setRefDesc(rs.getString("REF_DESC"));
                ln.setNowdedn(rs.getString("NOW_DEDN"));
                monthwiseinstl.add(ln);
            }
            pstmt = con.prepareStatement("SELECT AQ_MONTH,AQ_YEAR,AD_AMT,TOT_REC_AMT,REF_DESC,NOW_DEDN FROM HRMIS.AQ_DTLS1 WHERE EMP_CODE=? AND AD_REF_ID=? AND AD_AMT > 0 ORDER BY AQ_YEAR DESC,AQ_MONTH DESC");
            pstmt.setString(1, empCode);
            pstmt.setString(2, loanId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Loan ln = new Loan();
                ln.setMonth(CommonFunctions.getMonthAsString(rs.getInt("AQ_MONTH")));
                ln.setYear("" + rs.getInt("AQ_YEAR"));
                ln.setInstalmentAmount(rs.getInt("AD_AMT"));
                ln.setCumulativeAmtPaid(rs.getInt("TOT_REC_AMT"));
                ln.setRefDesc(rs.getString("REF_DESC"));
                ln.setNowdedn(rs.getString("NOW_DEDN"));
                monthwiseinstl.add(ln);
            }
            pstmt = con.prepareStatement("SELECT * FROM LOAN_ADJUSTMENT WHERE AD_REF_ID=?");
            pstmt.setString(1, loanId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Loan ln = new Loan();
                ln.setMonth("");
                ln.setYear("");
                ln.setInstalmentAmount(rs.getInt("AD_AMT"));
                ln.setCumulativeAmtPaid(rs.getInt("TOT_REC_AMT"));
                ln.setRefDesc(rs.getString("REF_DESC"));
                ln.setNowdedn(rs.getString("DED_TYPE"));
                monthwiseinstl.add(ln);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return monthwiseinstl;
    }

    @Override
    public List EmployeeWiseLoanList(String empId) {
        List loanlist = new ArrayList();
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        Loan loanForm = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("select LOANID,lnnotid,LNNOTYPE,LNTP,ACC_NO,LNAMNT,LOAN_NAME,P_RECOVERED,I_RECOVERED,NOW_DEDN from(  select LOANID,NOT_ID,LOAN_TP,emp_loan_sanc.loanid lnid, emp_loan_sanc.not_id lnnotid, "
                    + "emp_loan_sanc.not_type lnnotype,emp_loan_sanc.loan_tp lntp,emp_loan_sanc.amount lnamnt,ACC_NO,P_RECOVERED,I_RECOVERED,NOW_DEDN from emp_loan_sanc where emp_loan_sanc.emp_id=? and "
                    + "emp_loan_sanc.not_type='LOAN_SANC') temploan left outer join g_loan on temploan.loan_tp=g_loan.loan_tp order by P_RECOVERED");
            pstmt.setString(1, empId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                loanForm = new Loan();
                if (rs.getString("NOW_DEDN") != null && rs.getString("NOW_DEDN").equals("P")) {
                    loanForm.setCompletedRecovery(rs.getInt("P_RECOVERED"));
                } else if (rs.getString("NOW_DEDN") != null && rs.getString("NOW_DEDN").equals("I")) {
                    loanForm.setCompletedRecovery(rs.getInt("I_RECOVERED"));
                }
                loanForm.setNotid(rs.getInt("lnnotid"));
                loanForm.setLoanid(rs.getString("LOANID"));
                loanForm.setSltloan(rs.getString("LOAN_NAME"));
                loanForm.setTxtamount(rs.getDouble("LNAMNT"));
                loanForm.setAccountNo(rs.getString("ACC_NO"));
                loanlist.add(loanForm);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return loanlist;
    }

    @Override
    public ArrayList getSectionList(String offCode) {
        ArrayList sectionList = new ArrayList();
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        Connection con = null;

        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT BILL_GROUP_ID,DESCRIPTION FROM BILL_GROUP_MASTER WHERE OFF_CODE=? ORDER BY DESCRIPTION");
            pstmt.setString(1, offCode);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                SelectOption so = new SelectOption();
                so.setValue(rs.getString("BILL_GROUP_ID"));
                so.setLabel(rs.getString("DESCRIPTION"));
                sectionList.add(so);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return sectionList;
    }

    @Override
    public ArrayList getLoanList() {
        ArrayList loanList = new ArrayList();
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        Connection con = null;

        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT LOAN_TP,LOAN_NAME FROM G_LOAN ORDER BY LOAN_NAME");
            rs = pstmt.executeQuery();
            while (rs.next()) {
                SelectOption so = new SelectOption();
                so.setValue(rs.getString("LOAN_TP"));
                so.setLabel(rs.getString("LOAN_NAME"));
                loanList.add(so);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return loanList;
    }

    @Override
    public ArrayList getEmpList(String offCode, String billname, String loanname) {
        ArrayList empList = new ArrayList();
        PreparedStatement pst = null;
        PreparedStatement pst1 = null;
        PreparedStatement pst2 = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        ResultSet rs2 = null;
        GroupLoanBean gBean = null;
        Connection con = null;
        int i = 0;
        try {
            con = dataSource.getConnection();

            String sql = "SELECT SECTION_ID FROM BILL_SECTION_MAPPING WHERE BILL_GROUP_ID=? ORDER BY SEC_SL_NO";
            pst = con.prepareStatement(sql);
            BigDecimal bDecimal = new BigDecimal(billname);
            pst.setBigDecimal(1, bDecimal);
            rs = pst.executeQuery();
            while (rs.next()) {
                String sql1 = "SELECT * FROM ("
                        + " SELECT ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMP_NAME,EMP_ID,GPF_NO,CUR_SPC,CUR_OFF_CODE FROM ("
                        + " SELECT SPC,SECTION_ID FROM SECTION_POST_MAPPING WHERE SECTION_ID=? ) SECTION_POST_MAPPING"
                        + " INNER JOIN (SELECT EMP_ID,GPF_NO,CUR_SPC,CUR_OFF_CODE,INITIALS,F_NAME,M_NAME,L_NAME FROM EMP_MAST WHERE CUR_OFF_CODE=?) EMP_MAST ON SECTION_POST_MAPPING.SPC=EMP_MAST.CUR_SPC) EMP_MAST"
                        + " LEFT OUTER JOIN"
                        + " (select LOANID, EMP_ID,NOW_DEDN,LOAN_TP,P_ORG_AMT,"
                        + " P_TOT_NO_INSTL,P_INSTL_AMT,P_LAST_INSTL_NO,P_CUM_RECOVERED,I_ORG_AMT,I_TOT_NO_INST,I_INSTL_AMT,I_LAST_INSTL_NO,I_CUM_RECOVERED,P_RECOVERED,STOP_LOAN"
                        + " from emp_loan_sanc where loan_tp=? and p_recovered is null and i_recovered is null) emp_loan_sanc"
                        + " ON EMP_MAST.EMP_ID=emp_loan_sanc.EMP_ID order by emp_name";

                pst1 = con.prepareStatement(sql1);
                BigDecimal bDecimal1 = new BigDecimal(rs.getString("SECTION_ID"));
                pst1.setBigDecimal(1, bDecimal1);
                pst1.setString(2, offCode);
                pst1.setString(3, loanname);

                rs1 = pst1.executeQuery();
                while (rs1.next()) {
                    gBean = new GroupLoanBean();
                    i++;
                    gBean.setSlno(i);
                    gBean.setEmpname(rs1.getString("EMP_NAME"));
                    gBean.setEmpid(rs1.getString("EMP_ID"));
                    gBean.setGpfno(rs1.getString("GPF_NO"));
                    gBean.setLoanId(rs1.getString("LOANID"));
                    gBean.setChkNowDeduct(rs1.getString("NOW_DEDN"));
                    //gbean.setIs_recovered(rs1.getString("IS_RECOVERED"));
                    if (rs1.getString("NOW_DEDN") != null && rs1.getString("NOW_DEDN").equalsIgnoreCase("P")) {
                        gBean.setOrgloanamt(rs1.getString("P_ORG_AMT"));
                        gBean.setTotalinstlno(rs1.getString("P_TOT_NO_INSTL"));
                        gBean.setInstlamt(rs1.getString("P_INSTL_AMT"));
                        gBean.setLastpaidinstlno(rs1.getString("P_LAST_INSTL_NO"));
                        gBean.setCumulativeamtpaid(rs1.getString("P_CUM_RECOVERED"));
                    } else if (rs1.getString("NOW_DEDN") != null && rs1.getString("NOW_DEDN").equalsIgnoreCase("I")) {
                        gBean.setOrgloanamt(rs1.getString("I_ORG_AMT"));
                        gBean.setTotalinstlno(rs1.getString("I_TOT_NO_INST"));
                        gBean.setInstlamt(rs1.getString("I_INSTL_AMT"));
                        gBean.setLastpaidinstlno(rs1.getString("I_LAST_INSTL_NO"));
                        gBean.setCumulativeamtpaid(rs1.getString("I_CUM_RECOVERED"));
                    }
                    gBean.setStoploan(rs1.getString("STOP_LOAN"));
                    empList.add(gBean);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(rs1, pst1);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return empList;
    }

    @Override
    public void saveEmpData(String insertdata) {
        PreparedStatement pst = null;
        PreparedStatement pst1 = null;

        String insertfirstpart = insertdata;
        String[] insertsecondpart;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("INSERT INTO EMP_LOAN_SANC(NOT_ID,NOT_TYPE,LOAN_TP,EMP_ID,NOW_DEDN,P_ORG_AMT,P_TOT_NO_INSTL,P_INSTL_AMT,P_LAST_INSTL_NO,P_CUM_RECOVERED,I_ORG_AMT,I_TOT_NO_INST,I_INSTL_AMT,I_LAST_INSTL_NO,I_CUM_RECOVERED) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            pst1 = con.prepareStatement("UPDATE EMP_LOAN_SANC SET NOW_DEDN=?,P_ORG_AMT=?,P_TOT_NO_INSTL=?,P_INSTL_AMT=?,P_LAST_INSTL_NO=?,P_CUM_RECOVERED=?,I_ORG_AMT=?,I_TOT_NO_INST=?,I_INSTL_AMT=?,I_LAST_INSTL_NO=?,I_CUM_RECOVERED=? WHERE EMP_ID=? AND LOAN_TP=?");

            /*if(hidselectedforUpdate != null && !hidselectedforUpdate.equals("")){
             updatefirstpart = hidselectedforUpdate.split("@");
             for(int i = 0;i < updatefirstpart.length;i++){
             updatesecondpart = updatefirstpart[i].split(",");
             pst1.setString(1,updatesecondpart[1]);
             if(updatesecondpart[1] != null && updatesecondpart[1].equalsIgnoreCase("P")){
             pst1.setString(2,updatesecondpart[2]);
             pst1.setString(3,updatesecondpart[3]);
             pst1.setString(4,updatesecondpart[4]);
             pst1.setString(5,updatesecondpart[5]);
             pst1.setString(6,updatesecondpart[6]);
             pst1.setString(7,null);
             pst1.setString(8,null);
             pst1.setString(9,null);
             pst1.setString(10,null);
             pst1.setString(11,null);
             }else if(updatesecondpart[1] != null && updatesecondpart[1].equalsIgnoreCase("I")){
             pst1.setString(2,null);
             pst1.setString(3,null);
             pst1.setString(4,null);
             pst1.setString(5,null);
             pst1.setString(6,null);
             pst1.setString(7,updatesecondpart[2]);
             pst1.setString(8,updatesecondpart[3]);
             pst1.setString(9,updatesecondpart[4]);
             pst1.setString(10,updatesecondpart[5]);
             pst1.setString(11,updatesecondpart[6]);
             }
             pst1.setString(12,updatesecondpart[0]);
             pst1.setString(13,gForm.getSltLoanlist());
             num1 = pst1.executeUpdate();
             }
             }*/
            //billname|loan|empid|now_ded|org_amount|total_instl_no|instlamt|last_paid_instl_no|cumulative_amount_paid@@
            insertsecondpart = insertfirstpart.split("\\|");
            //pst.setString(1, CommonFunctions.getMaxCode("EMP_LOAN_SANC", "LOANID", con));
            pst.setInt(1, Integer.parseInt(insertsecondpart[9]));
            pst.setString(2, "LOAN_SANC");
            //pst.setString(4,form.getSltLoanlist());
            pst.setString(3, insertsecondpart[1]);
            pst.setString(4, insertsecondpart[2]);
            pst.setString(5, insertsecondpart[3]);

            if (insertsecondpart[3] != null && insertsecondpart[3].equalsIgnoreCase("P")) {
                pst.setInt(6, Integer.parseInt(insertsecondpart[4]));
                pst.setInt(7, Integer.parseInt(insertsecondpart[5]));
                pst.setInt(8, Integer.parseInt(insertsecondpart[6]));
                pst.setInt(9, Integer.parseInt(insertsecondpart[7]));
                pst.setInt(10, Integer.parseInt(insertsecondpart[8]));
                pst.setInt(11, 0);
                pst.setInt(12, 0);
                pst.setInt(13, 0);
                pst.setInt(14, 0);
                pst.setInt(15, 0);
            } else if (insertsecondpart[3] != null && insertsecondpart[3].equalsIgnoreCase("I")) {
                pst.setInt(6, 0);
                pst.setInt(7, 0);
                pst.setInt(8, 0);
                pst.setInt(9, 0);
                pst.setInt(10, 0);
                pst.setInt(11, Integer.parseInt(insertsecondpart[4]));
                pst.setInt(12, Integer.parseInt(insertsecondpart[5]));
                pst.setInt(13, Integer.parseInt(insertsecondpart[6]));
                pst.setInt(14, Integer.parseInt(insertsecondpart[7]));
                pst.setInt(15, Integer.parseInt(insertsecondpart[8]));
            }
            pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(pst1);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void startStopLoandata(String loanId, String status) {
        PreparedStatement pst = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("UPDATE EMP_LOAN_SANC SET STOP_LOAN=? WHERE LOANID=?");
            pst.setString(1, status);
            pst.setString(2, loanId);
            pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    /* @Override
     public void saveStartLoandata(GroupLoanForm gForm) {
     PreparedStatement pst = null;
     Connection con = null;
     String hidstartemp = gForm.getHidselectedemptoStart();
     String startemp[];
     try {
     con = dataSource.getConnection();
     pst = con.prepareStatement("UPDATE EMP_LOAN_SANC SET STOP_LOAN=? WHERE EMP_ID=? AND LOAN_TP=?");
     if (hidstartemp != null && !hidstartemp.equals("")) {
     startemp = hidstartemp.split("@");
     for (int i = 0; i < startemp.length; i++) {
                    
     pst.setString(1, null);
     pst.setString(2, startemp[i]);
     pst.setString(3, gForm.getSltLoanlist());
     pst.executeUpdate();
     }
     }
     } catch (Exception e) {
     e.printStackTrace();
     } finally {
     DataBaseFunctions.closeSqlObjects(pst);
     }
     }*/
    @Override
    public Loan editAGInterestLoanData(String empid, String adrefid, Loan lfb) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = this.dataSource.getConnection();

            String sql = "select * from ag_interest_calculation where ad_ref_id=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, adrefid);
            rs = pst.executeQuery();
            if (rs.next()) {
                lfb.setEmpid(empid);
                lfb.setAgcalculationid(rs.getString("calculation_id"));
                lfb.setAgAdrefid(adrefid);
                lfb.setOrderno(rs.getString("sanc_no") + "(1)");
                lfb.setOrderdate(CommonFunctions.getFormattedOutputDate1(rs.getDate("sanc_date")));
                if (rs.getString("loan_type") != null && rs.getString("loan_type").equals("COMP")) {
                    lfb.setSltloan("CMPA");
                }
                if (rs.getString("loan_styp") != null) {
                    if (rs.getString("loan_styp").equals("MCYCLE-1") || rs.getString("loan_styp").equals("MCYCLE-2") || rs.getString("loan_styp").equals("MCYCLE-3") || rs.getString("loan_styp").equals("MOPED-1")) {
                        lfb.setSltloan("MCA");
                    }
                    if (rs.getString("loan_styp").equals("MCAR")) {
                        lfb.setSltloan("VE");
                    }
                }
                lfb.setNowDeduct("I");
                lfb.setTxtamount(rs.getInt("total_interest"));
                lfb.setOriginalAmt(rs.getInt("total_interest"));
                lfb.setTotalNoOfInsl(rs.getInt("int_inst"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return lfb;
    }

}
