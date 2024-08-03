package hrms.dao.incrementsanction;

import hrms.SelectOption;
import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.dao.empinfo.EmployeeInformationDAO;
import hrms.dao.emppayrecord.EmpPayRecordDAO;
import hrms.dao.leaveAccount.ServiceBookLanguageDAO;
import hrms.dao.notification.NotificationDAO;
import hrms.model.empinfo.EmployeeInformation;
import hrms.model.emppayrecord.EmpPayRecordForm;
import hrms.model.incrementsanction.IncrementBean;
import hrms.model.incrementsanction.IncrementForm;
import hrms.model.notification.NotificationBean;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

public class IncrementSanctionDAOImpl implements IncrementSanctionDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;
    @Resource(name = "repodataSource")
    protected DataSource repodataSource;

    public NotificationDAO notificationDao;

    protected EmpPayRecordDAO emppayrecordDAO;

    protected EmployeeInformationDAO empinfoDAO;

    protected ServiceBookLanguageDAO sbDAO;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setRepodataSource(DataSource repodataSource) {
        this.repodataSource = repodataSource;
    }

    public void setEmppayrecordDAO(EmpPayRecordDAO emppayrecordDAO) {
        this.emppayrecordDAO = emppayrecordDAO;
    }

    public void setNotificationDao(NotificationDAO notificationDao) {
        this.notificationDao = notificationDao;
    }

    public void setSbDAO(ServiceBookLanguageDAO sbDAO) {
        this.sbDAO = sbDAO;
    }

    public void setEmpinfoDAO(EmployeeInformationDAO empinfoDAO) {
        this.empinfoDAO = empinfoDAO;
    }

    @Override
    public List getPayMatrixLevel() {
        Connection con = null;
        List al = new ArrayList();
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            con = this.repodataSource.getConnection();
            pst = con.prepareStatement("select gp_level from pay_matrix_2017 group by gp_level order by gp_level");
            rs = pst.executeQuery();
            while (rs.next()) {
                SelectOption so = new SelectOption();
                so.setLabel(rs.getInt("gp_level") + "");
                so.setValue(rs.getInt("gp_level") + "");
                al.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return al;
    }

    @Override
    public List getPayMatrixCell() {
        Connection con = null;
        List al = new ArrayList();
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            con = repodataSource.getConnection();
            pst = con.prepareStatement("select level_slno from pay_matrix_2017 group by level_slno order by level_slno");
            rs = pst.executeQuery();
            while (rs.next()) {
                SelectOption so = new SelectOption();
                so.setLabel(rs.getInt("level_slno") + "");
                so.setValue(rs.getInt("level_slno") + "");
                al.add(so);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }

        return al;
    }

    @Override
    public void saveIncrement(IncrementForm incfb, String entdept, String entoff, String entauth, String payCommission, String loginuserid) {

        Connection con = null;

        PreparedStatement pst = null;

        NotificationBean nb = new NotificationBean();
        EmpPayRecordForm epayrecordform = new EmpPayRecordForm();
        EmployeeInformation ei = new EmployeeInformation();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        try {
            con = dataSource.getConnection();

            if (incfb.getEmpid() != null && !incfb.getEmpid().equals("")) {

                nb.setNottype("INCREMENT");
                nb.setEmpId(incfb.getEmpid());
                nb.setDateofEntry(new Date());
                nb.setOrdno(incfb.getTxtSanctionOrderNo());
                nb.setOrdDate(sdf.parse(incfb.getTxtSanctionOrderDt()));
                nb.setSancDeptCode(incfb.getDeptCode());
                nb.setSancOffCode(incfb.getHidOffCode());
                nb.setRadpostingauthtype(incfb.getRadsancauthtype());
                if (incfb.getRadsancauthtype() != null && incfb.getRadsancauthtype().equals("GOI")) {
                    nb.setSancAuthCode(incfb.getHidSancAuthorityOthSpc());
                } else {
                    nb.setSancAuthCode(incfb.getHidSpc());
                }
                nb.setEntryDeptCode(entdept);
                nb.setEntryOffCode(entoff);
                nb.setEntryAuthCode(entauth);
                nb.setNote(incfb.getTxtIncrNote());
                nb.setLoginuserid(loginuserid);
                int notid = notificationDao.insertNotificationData(nb);

                //pst = con.prepareStatement("UPDATE EMP_MAST SET ST_DATE_OF_CUR_SALARY=DATE_OF_NINCR,DATE_OF_NINCR=(SELECT ADD_MONTHS(EMP_MAST.DATE_OF_NINCR,12) FROM EMP_MAST WHERE EMP_ID='" + incfb.getEmpid() + "') WHERE EMP_ID=?");
                /*
                 pst = con.prepareStatement("UPDATE EMP_MAST SET matrix_level=?, matrix_cell=?, cur_salary=?, ST_DATE_OF_CUR_SALARY=DATE_OF_NINCR,DATE_OF_NINCR=(SELECT DATE_OF_NINCR + CAST('12 MONTHS' AS INTERVAL) FROM EMP_MAST WHERE EMP_ID=?) WHERE EMP_ID=?");
                 pst.setInt(1, Integer.parseInt(incfb.getPayLevel()));
                 pst.setInt(2, Integer.parseInt(incfb.getPayCell()));
                 pst.setString(3, incfb.getSltPayScale());
                 pst.setString(4, incfb.getEmpid());
                 pst.setString(5, incfb.getEmpid());
                 pst.executeUpdate();
                 */
                pst = con.prepareStatement("insert into emp_incr (not_id, not_type, emp_id, incr, first_incr, second_incr, third_incr, fourth_incr, incr_type) values(?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);

                pst.setInt(1, notid);
                pst.setString(2, "INCREMENT");
                pst.setString(3, incfb.getEmpid());
                if (incfb.getTxtIncrAmt() != null && !incfb.getTxtIncrAmt().equalsIgnoreCase("")) {
                    pst.setDouble(4, Double.parseDouble(incfb.getTxtIncrAmt()));
                } else {
                    pst.setDouble(4, 0);
                }
                if (incfb.getIncrementLvl() != null && incfb.getIncrementLvl().equals("1")) {
                    pst.setString(5, incfb.getIncrementLvl());
                } else {
                    pst.setString(5, "");
                }
                if (incfb.getIncrementLvl() != null && incfb.getIncrementLvl().equals("2")) {
                    pst.setString(6, incfb.getIncrementLvl());
                } else {
                    pst.setString(6, "");
                }
                if (incfb.getIncrementLvl() != null && incfb.getIncrementLvl().equals("3")) {
                    pst.setString(7, incfb.getIncrementLvl());
                } else {
                    pst.setString(7, "");
                }
                if (incfb.getIncrementLvl() != null && incfb.getIncrementLvl().equals("4")) {
                    pst.setString(8, incfb.getIncrementLvl());
                } else {
                    pst.setString(8, "");
                }
                if (incfb.getIncrementType() != null && !incfb.getIncrementType().equals("")) {
                    pst.setString(9, incfb.getIncrementType());
                } else {
                    pst.setString(9, "");
                }
                pst.executeUpdate();

                ResultSet rs = pst.getGeneratedKeys();
                rs.next();
                int incrid = rs.getInt("incrid");

                String sqlString = ToStringBuilder.reflectionToString(pst);
                //sbDAO.createTransactionLog(loginuserid, incfb.getEmpid(), "INCREMENT", sqlString);

                pst = con.prepareStatement("UPDATE emp_incr SET query_string=? where incrid=? and EMP_ID=?");
                pst.setString(1, sqlString);
                pst.setInt(2, incrid);
                pst.setString(3, incfb.getEmpid());
                pst.executeUpdate();

                epayrecordform.setEmpid(incfb.getEmpid());
                epayrecordform.setNot_id(notid);
                epayrecordform.setNot_type("INCREMENT");
                epayrecordform.setPayscale(incfb.getSltPayScale());
                if (incfb.getContRemAmount() != null && !incfb.getContRemAmount().equals("")) {
                    epayrecordform.setBasic((incfb.getContRemAmount()));
                } else {
                    epayrecordform.setBasic(incfb.getTxtNewBasic());
                }
                if (incfb.getRdoPaycomm() != null && incfb.getRdoPaycomm().equals("REM")) {
                    epayrecordform.setIfRemuneration("Y");
                } else {
                    epayrecordform.setIfRemuneration("N");
                }
                //epayrecordform.setBasic(incfb.getTxtNewBasic());
                epayrecordform.setGp(incfb.getTxtGradePay());
                epayrecordform.setS_pay(incfb.getTxtSPay());
                epayrecordform.setP_pay(incfb.getTxtP_pay());
                epayrecordform.setOth_pay(incfb.getTxtOthPay());
                epayrecordform.setOth_desc(incfb.getTxtDescOth());
                epayrecordform.setWefDt(incfb.getTxtWEFDt());
                epayrecordform.setWefTime(incfb.getTxtWEFTime());
                epayrecordform.setPayLevel(incfb.getPayLevel());
                epayrecordform.setPayCell(incfb.getPayCell());
                emppayrecordDAO.saveEmpPayRecordData(epayrecordform);

                String serverDate = sdf.format(new Date());

                //if (serverDate != null && !serverDate.trim().equals("")) {
                //boolean updatepay = empinfoDAO.isupdatePayOrPostingInfo(incfb.getEmpid(), incfb.getTxtWEFDt(), incfb.getTxtSanctionOrderDt(), "PAY");
                //if (updatepay == true) {
                if (incfb.getRdTransaction() != null && incfb.getRdTransaction().equals("C")) {
                    pst = con.prepareStatement("UPDATE EMP_MAST SET matrix_level=?, matrix_cell=?, cur_salary=?, PAY_DATE=?, CUR_BASIC_SALARY=?, gp=?, spay=?, ppay=?, othpay=?, ST_DATE_OF_CUR_SALARY=DATE_OF_NINCR,DATE_OF_NINCR=(SELECT DATE_OF_NINCR + CAST('12 MONTHS' AS INTERVAL) FROM EMP_MAST WHERE EMP_ID=?) WHERE EMP_ID=?");
                    if (incfb.getRdoPaycomm() != null && incfb.getRdoPaycomm().equals("6")) {
                        pst.setInt(1, 0);
                        pst.setInt(2, 0);
                        pst.setString(3, incfb.getSltPayScale());
                        if (incfb.getTxtGradePay() != null && !incfb.getTxtGradePay().equals("")) {
                            pst.setDouble(6, Double.parseDouble(incfb.getTxtGradePay()));
                        } else {
                            pst.setDouble(6, 0);
                        }
                        if (incfb.getTxtNewBasic() != null && !incfb.getTxtNewBasic().equals("")) {
                            pst.setDouble(5, Double.parseDouble(incfb.getTxtNewBasic()));
                        } else {
                            pst.setDouble(5, 0);
                        }

                    } else if (incfb.getRdoPaycomm() != null && incfb.getRdoPaycomm().equals("7")) {

                        pst.setString(1, incfb.getPayLevel());

                        pst.setString(2, incfb.getPayCell());

                        pst.setString(3, null);
                        pst.setDouble(6, 0);
                        if (incfb.getTxtNewBasic() != null && !incfb.getTxtNewBasic().equals("")) {
                            pst.setDouble(5, Double.parseDouble(incfb.getTxtNewBasic()));
                        } else {
                            pst.setDouble(5, 0);
                        }
                    } else if (incfb.getRdoPaycomm() != null && incfb.getRdoPaycomm().equals("REM")) {
                        pst.setInt(1, 0);
                        pst.setInt(2, 0);
                        pst.setString(3, null);
                        pst.setDouble(5, Double.parseDouble(incfb.getContRemAmount()));
                        pst.setDouble(6, 0);
                    }

                    pst.setTimestamp(4, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(incfb.getTxtWEFDt()).getTime()));
                    //pst.setDouble(5, Double.parseDouble(incfb.getTxtNewBasic()));

                    if (incfb.getTxtSPay() != null && !incfb.getTxtSPay().equals("")) {
                        pst.setDouble(7, Double.parseDouble(incfb.getTxtSPay()));
                    } else {
                        pst.setDouble(7, 0);
                    }

                    if (incfb.getTxtP_pay() != null && !incfb.getTxtP_pay().equals("")) {
                        pst.setDouble(8, Double.parseDouble(incfb.getTxtP_pay()));
                    } else {
                        pst.setDouble(8, 0);
                    }

                    if (incfb.getTxtOthPay() != null && !incfb.getTxtOthPay().equals("")) {
                        pst.setDouble(9, Double.parseDouble(incfb.getTxtOthPay()));
                    } else {
                        pst.setDouble(9, 0);
                    }

                    pst.setString(10, incfb.getEmpid());
                    pst.setString(11, incfb.getEmpid());
                    pst.executeUpdate();

                    sqlString = ToStringBuilder.reflectionToString(pst);
                    sbDAO.createTransactionLog(loginuserid, incfb.getEmpid(), "INCREMENT", sqlString);
                }
                //}
                //}

                /*
                 * Updating the Service Book Language
                 */
                if (incfb.getChkNotSBPrint() != null && incfb.getChkNotSBPrint().equals("Y")) {
                    pst = con.prepareStatement("UPDATE EMP_NOTIFICATION SET IF_VISIBLE='N' WHERE NOT_ID=?");
                    pst.setInt(1, notid);
                    pst.execute();
                } else {
                    String sbLang = sbDAO.getIncrementSanctionDetails(incfb, notid, payCommission);
                    pst = con.prepareStatement("UPDATE EMP_NOTIFICATION SET SB_DESCRIPTION=?,IF_VISIBLE='Y' WHERE NOT_ID=? ");
                    pst.setString(1, sbLang);
                    pst.setInt(2, notid);
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

    @Override
    public List getIncrementList(String empId) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List incrementlist = new ArrayList();
        IncrementBean inc = null;
        try {
            con = repodataSource.getConnection();

            /*String sql = "SELECT is_validated,EMP_NOTIFICATION.modified_by,EMP_NOTIFICATION.modified_on,EMP_NOTIFICATION.IF_VISIBLE,"
             + "SV_ID,EMP_NOTIFICATION.NOT_ID,INCRID,INCR,FIRST_INCR,SECOND_INCR,THIRD_INCR,FOURTH_INCR,INCR_TYPE,DOE, ORDNO,"
             + "ORDDT,AUTH,OFF_CODE,DEPT_CODE,WEF,WEFT,pay_scale,s_pay, p_pay,oth_pay,oth_desc,pay,gp, pay_level, pay_cell FROM EMP_INCR "
             + "LEFT OUTER JOIN EMP_NOTIFICATION ON EMP_INCR.NOT_ID=EMP_NOTIFICATION.NOT_ID "
             + "LEFT OUTER JOIN EMP_PAY_RECORD ON EMP_PAY_RECORD.NOT_ID=EMP_NOTIFICATION.NOT_ID "
             + "WHERE EMP_INCR.NOT_TYPE='INCREMENT' AND EMP_INCR.EMP_ID=? AND PRID IS NULL ORDER BY WEF DESC";*/
            String sql = "SELECT E2.not_type canceltype,E2.not_id linkid,E1.is_validated,E1.modified_by,E1.modified_on,E1.IF_VISIBLE,"
                    + " E1.SV_ID,E1.NOT_ID,INCRID,INCR,FIRST_INCR,SECOND_INCR,THIRD_INCR,FOURTH_INCR,INCR_TYPE,E1.DOE, E1.ORDNO,"
                    + " E1.ORDDT,E1.AUTH,E1.OFF_CODE,E1.DEPT_CODE,WEF,WEFT,pay_scale,s_pay, p_pay,oth_pay,oth_desc,pay,gp, pay_level, pay_cell FROM EMP_INCR"
                    + " LEFT OUTER JOIN EMP_NOTIFICATION E1 ON EMP_INCR.NOT_ID=E1.NOT_ID"
                    + " LEFT OUTER JOIN EMP_NOTIFICATION E2 ON E1.LINK_ID=E2.NOT_ID"
                    + " LEFT OUTER JOIN EMP_PAY_RECORD ON EMP_PAY_RECORD.NOT_ID=E1.NOT_ID"
                    + " WHERE EMP_INCR.NOT_TYPE='INCREMENT' AND EMP_INCR.EMP_ID=? AND PRID IS NULL ORDER BY WEF DESC";

            pst = con.prepareStatement(sql);
            pst.setString(1, empId);
            rs = pst.executeQuery();
            while (rs.next()) {
                inc = new IncrementBean();

                inc.setHidIncrId(StringUtils.defaultString(rs.getString("INCRID")));
                inc.setHnotid(StringUtils.defaultString(rs.getString("not_id")));
                if (rs.getDate("DOE") != null && !rs.getDate("DOE").equals("")) {
                    inc.setDoe(CommonFunctions.getFormattedOutputDate1(rs.getDate("DOE")));
                } else {
                    inc.setDoe("");
                }
                if (rs.getDate("WEF") != null && !rs.getDate("WEF").equals("")) {
                    inc.setEffDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("WEF")));
                } else {
                    inc.setEffDate("");
                }
                inc.setEffTime(StringUtils.defaultString(rs.getString("WEFT")));
                inc.setIncrAmt(StringUtils.defaultString(rs.getString("INCR")));
                inc.setNewBasic(StringUtils.defaultString(rs.getString("pay")));
                inc.setGradePay(StringUtils.defaultString(rs.getString("gp")));

                if (rs.getString("IF_VISIBLE") != null && rs.getString("IF_VISIBLE").equals("N")) {
                    inc.setPrintInServiceBook("No");
                } else {
                    inc.setPrintInServiceBook("Yes");
                }

                inc.setModifiedBy(StringUtils.defaultString(rs.getString("modified_by")) + "<br />" + StringUtils.defaultString(CommonFunctions.getFormattedOutputDate1(rs.getDate("modified_on"))));

                inc.setIsValidated(rs.getString("is_validated"));

                inc.setCanceltype(rs.getString("canceltype"));
                inc.setCancelnotid(rs.getString("linkid"));
                incrementlist.add(inc);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return incrementlist;
    }

    @Override
    public void updateIncrement(IncrementForm incfb, String entdept, String entoff, String entauth, String payCommission, String loginuserid) throws Exception {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        boolean dateincr = false;

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");

        NotificationBean nb = new NotificationBean();
        EmpPayRecordForm epayrecordform = new EmpPayRecordForm();
        EmployeeInformation ei = new EmployeeInformation();
        try {
            con = dataSource.getConnection();

            if (incfb.getEmpid() != null && !incfb.getEmpid().equals("")) {

                nb.setNottype("INCREMENT");
                nb.setEmpId(incfb.getEmpid());
                nb.setNotid(incfb.getHnotid());
                nb.setOrdno(incfb.getTxtSanctionOrderNo());
                nb.setOrdDate(sdf.parse(incfb.getTxtSanctionOrderDt()));
                nb.setSancDeptCode(incfb.getDeptCode());
                nb.setSancOffCode(incfb.getHidOffCode());
                //nb.setSancAuthCode(incfb.getHidSpc());
                nb.setRadpostingauthtype(incfb.getRadsancauthtype());
                if (incfb.getRadsancauthtype() != null && incfb.getRadsancauthtype().equals("GOI")) {
                    nb.setSancAuthCode(incfb.getHidSancAuthorityOthSpc());
                } else {
                    nb.setSancAuthCode(incfb.getHidSpc());
                }
                nb.setEntryDeptCode(entdept);
                nb.setEntryOffCode(entoff);
                nb.setEntryAuthCode(entauth);
                nb.setNote(incfb.getTxtIncrNote());
                nb.setLoginuserid(loginuserid);
                notificationDao.modifyNotificationData(nb);

                dateincr = getCmpDate(con, "DATE_OF_NINCR", incfb.getEmpid());

                pst = con.prepareStatement("update emp_incr set not_id=?,not_type=?,incr=?,first_incr=?,second_incr=?,third_incr=?,fourth_incr=?,incr_type=? where emp_id=? and incrid=?");

                pst.setInt(1, incfb.getHnotid());
                pst.setString(2, "INCREMENT");
                if (incfb.getTxtIncrAmt() != null && !incfb.getTxtIncrAmt().equalsIgnoreCase("")) {
                    pst.setDouble(3, java.lang.Double.parseDouble(incfb.getTxtIncrAmt()));
                } else {
                    pst.setDouble(3, 0);
                }
                if (incfb.getIncrementLvl() != null && incfb.getIncrementLvl().equals("1")) {
                    pst.setString(4, incfb.getIncrementLvl());
                } else {
                    pst.setString(4, "N");
                }
                if (incfb.getIncrementLvl() != null && incfb.getIncrementLvl().equals("2")) {
                    pst.setString(5, incfb.getIncrementLvl());
                } else {
                    pst.setString(5, "N");
                }
                if (incfb.getIncrementLvl() != null && incfb.getIncrementLvl().equals("3")) {
                    pst.setString(6, incfb.getIncrementLvl());
                } else {
                    pst.setString(6, "N");
                }
                if (incfb.getIncrementLvl() != null && incfb.getIncrementLvl().equals("4")) {
                    pst.setString(7, incfb.getIncrementLvl());
                } else {
                    pst.setString(7, "N");
                }
                if (incfb.getIncrementType() != null && !incfb.getIncrementType().equals("")) {
                    pst.setString(8, incfb.getIncrementType());
                } else {
                    pst.setString(8, "N");
                }
                pst.setString(9, incfb.getEmpid());
                pst.setInt(10, incfb.getHidIncrId());
                pst.executeUpdate();

                String sqlString = ToStringBuilder.reflectionToString(pst);
                sbDAO.createTransactionLog(loginuserid, incfb.getEmpid(), "INCREMENT", sqlString);

                epayrecordform.setEmpid(incfb.getEmpid());
                epayrecordform.setNot_id(incfb.getHnotid());
                epayrecordform.setNot_type("INCREMENT");
                epayrecordform.setPayid(incfb.getHidPayId());
                //epayrecordform.setPayscale(incfb.getSltPayScale());
                if (incfb.getContRemAmount() != null && !incfb.getContRemAmount().equals("")) {
                    epayrecordform.setBasic((incfb.getContRemAmount()));
                } else {
                    epayrecordform.setBasic(incfb.getTxtNewBasic());
                }
                if (incfb.getRdoPaycomm() != null && incfb.getRdoPaycomm().equals("6")) {
                    epayrecordform.setPayscale(incfb.getSltPayScale());
                    epayrecordform.setGp(incfb.getTxtGradePay());
                    epayrecordform.setPayCell(null);
                    epayrecordform.setPayLevel(null);

                } else if (incfb.getRdoPaycomm() != null && incfb.getRdoPaycomm().equals("7")) {
                    epayrecordform.setPayscale(null);
                    epayrecordform.setGp("0");
                    epayrecordform.setPayCell(incfb.getPayCell());
                    epayrecordform.setPayLevel(incfb.getPayLevel());
                } else if (incfb.getRdoPaycomm() != null && incfb.getRdoPaycomm().equals("REM")) {
                    epayrecordform.setIfRemuneration("Y");
                    epayrecordform.setPayscale(incfb.getSltPayScale());
                    epayrecordform.setGp(incfb.getTxtGradePay());
                    epayrecordform.setPayCell(incfb.getPayCell());
                    epayrecordform.setPayLevel(incfb.getPayLevel());
                } else {
                    epayrecordform.setIfRemuneration("N");
                }
                //epayrecordform.setBasic(incfb.getTxtNewBasic());
                //epayrecordform.setGp(incfb.getTxtGradePay());
                epayrecordform.setS_pay(incfb.getTxtSPay());
                epayrecordform.setP_pay(incfb.getTxtP_pay());
                epayrecordform.setOth_pay(incfb.getTxtOthPay());
                epayrecordform.setOth_desc(incfb.getTxtDescOth());
                epayrecordform.setWefDt(incfb.getTxtWEFDt());
                epayrecordform.setWefTime(incfb.getTxtWEFTime());
                //epayrecordform.setPayCell(incfb.getPayCell());
                //epayrecordform.setPayLevel(incfb.getPayLevel());
                emppayrecordDAO.updateEmpPayRecordData(epayrecordform);

                String serverDate = sdf.format(new Date());
                //if (serverDate != null && !serverDate.trim().equals("")) {
                //boolean updatepay = empinfoDAO.isupdatePayOrPostingInfo(incfb.getEmpid(), incfb.getTxtWEFDt(), incfb.getTxtSanctionOrderDt(), "PAY");
                //if (updatepay == true) {
                if (incfb.getRdTransaction() != null && incfb.getRdTransaction().equals("C")) {
                    pst = con.prepareStatement("UPDATE EMP_MAST SET matrix_level=?, matrix_cell=?, cur_salary=?, PAY_DATE=?, CUR_BASIC_SALARY=?, gp=?, spay=?, ppay=?, othpay=?, ST_DATE_OF_CUR_SALARY=DATE_OF_NINCR,DATE_OF_NINCR=(SELECT DATE_OF_NINCR + CAST('12 MONTHS' AS INTERVAL) FROM EMP_MAST WHERE EMP_ID=?) WHERE EMP_ID=?");
                    if (incfb.getRdoPaycomm() != null && incfb.getRdoPaycomm().equals("6")) {
                        pst.setInt(1, 0);
                        pst.setInt(2, 0);
                        pst.setString(3, incfb.getSltPayScale());
                        if (incfb.getTxtGradePay() != null && !incfb.getTxtGradePay().equals("")) {
                            pst.setDouble(6, Double.parseDouble(incfb.getTxtGradePay()));
                        } else {
                            pst.setDouble(6, 0);
                        }
                        if (incfb.getTxtNewBasic() != null && !incfb.getTxtNewBasic().equals("")) {
                            pst.setDouble(5, Double.parseDouble(incfb.getTxtNewBasic()));
                        } else {
                            pst.setDouble(5, 0);
                        }

                    } else if (incfb.getRdoPaycomm() != null && incfb.getRdoPaycomm().equals("7")) {

                        pst.setString(1, incfb.getPayLevel());

                        pst.setString(2, incfb.getPayCell());

                        pst.setString(3, null);
                        pst.setDouble(6, 0);
                        if (incfb.getTxtNewBasic() != null && !incfb.getTxtNewBasic().equals("")) {
                            pst.setDouble(5, Double.parseDouble(incfb.getTxtNewBasic()));
                        } else {
                            pst.setDouble(5, 0);
                        }
                    } else if (incfb.getRdoPaycomm() != null && incfb.getRdoPaycomm().equals("REM")) {
                        pst.setDouble(5, Double.parseDouble(incfb.getContRemAmount()));
                        if (incfb.getPayLevel() != null && !incfb.getPayLevel().equals("")) {
                            pst.setInt(1, Integer.parseInt(incfb.getPayLevel()));
                        } else {
                            pst.setInt(1, 0);
                        }

                        if (incfb.getPayCell() != null && !incfb.getPayCell().equals("")) {
                            pst.setInt(2, Integer.parseInt(incfb.getPayCell()));
                        } else {
                            pst.setInt(2, 0);
                        }
                        pst.setString(3, incfb.getSltPayScale());
                        if (incfb.getTxtGradePay() != null && !incfb.getTxtGradePay().equals("")) {
                            pst.setDouble(6, Double.parseDouble(incfb.getTxtGradePay()));
                        } else {
                            pst.setDouble(6, 0);
                        }
                    }

                    pst.setTimestamp(4, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(incfb.getTxtWEFDt()).getTime()));
                    //pst.setDouble(5, Double.parseDouble(incfb.getTxtNewBasic()));

                    if (incfb.getTxtSPay() != null && !incfb.getTxtSPay().equals("")) {
                        pst.setDouble(7, Double.parseDouble(incfb.getTxtSPay()));
                    } else {
                        pst.setDouble(7, 0);
                    }

                    if (incfb.getTxtP_pay() != null && !incfb.getTxtP_pay().equals("")) {
                        pst.setDouble(8, Double.parseDouble(incfb.getTxtP_pay()));
                    } else {
                        pst.setDouble(8, 0);
                    }

                    if (incfb.getTxtOthPay() != null && !incfb.getTxtOthPay().equals("")) {
                        pst.setDouble(9, Double.parseDouble(incfb.getTxtOthPay()));
                    } else {
                        pst.setDouble(9, 0);
                    }

                    pst.setString(10, incfb.getEmpid());
                    pst.setString(11, incfb.getEmpid());
                    pst.executeUpdate();

                    sqlString = ToStringBuilder.reflectionToString(pst);
                    sbDAO.createTransactionLog(loginuserid, incfb.getEmpid(), "INCREMENT", sqlString);
                }
                //}
                //}

                /*
                 * Updating the Service Book Language
                 */
                if (incfb.getChkNotSBPrint() != null && incfb.getChkNotSBPrint().equals("Y")) {
                    pst = con.prepareStatement("UPDATE EMP_NOTIFICATION SET IF_VISIBLE='N' WHERE NOT_ID=?");
                    pst.setInt(1, incfb.getHnotid());
                    pst.execute();
                } else {
                    String sbLang = sbDAO.getIncrementSanctionDetails(incfb, incfb.getHnotid(), payCommission);

                    pst = con.prepareStatement("UPDATE EMP_NOTIFICATION SET SB_DESCRIPTION=?,IF_VISIBLE='Y' WHERE NOT_ID=?");
                    pst.setString(1, sbLang);
                    pst.setInt(2, incfb.getHnotid());
                    pst.execute();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    private boolean getCmpDate(Connection con, String colName, String empid) throws Exception {

        Statement stmt = null;
        ResultSet rs = null;
        int curyear = 0;
        String curyear1 = "";
        boolean flag = false;
        try {
            Calendar cal = Calendar.getInstance();
            curyear = cal.get(Calendar.YEAR);
            curyear1 = curyear + "";
            stmt = con.createStatement();
            String sql = "SELECT " + colName + " FROM EMP_MAST WHERE EMP_ID='" + empid + "'";
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                if (rs.getString("DATE_OF_NINCR") != null && !rs.getString("DATE_OF_NINCR").equals("")) {
                    if (Integer.parseInt(CommonFunctions.getFormattedOutputDate1(rs.getDate("DATE_OF_NINCR")).substring(7)) > Integer.parseInt(curyear1)) {
                        flag = false;
                    } else if (Integer.parseInt(CommonFunctions.getFormattedOutputDate1(rs.getDate("DATE_OF_NINCR")).substring(7)) == Integer.parseInt(curyear1)) {
                        flag = true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, stmt);
        }
        return flag;
    }

    @Override
    public IncrementForm getEmpIncRowDataForSB(String empId, int notId, String payCommission) {
        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        IncrementForm incfb = new IncrementForm();
        String incrLvl = "";
        try {
            con = dataSource.getConnection();

            String sql = "select incrid, emp_notification.not_id, pay_id, ordno, orddt, emp_notification.dept_code, emp_notification.off_code, auth, spn, wef, weft, "
                    + " emp_pay_record.pay_scale, incr, p_pay, oth_pay, s_pay, pay, oth_desc, emp_pay_record.gp, first_incr, second_incr, "
                    + " third_incr, fourth_incr, incr_type, note, pay_level, pay_cell     from emp_incr "
                    + " inner join emp_notification on emp_incr.not_id=emp_notification.not_id"
                    + " left outer join emp_pay_record on emp_notification.not_id=emp_pay_record.not_id"
                    + " left outer join g_spc on emp_notification.auth=g_spc.spc"
                    + " where emp_incr.emp_id=? and emp_incr.not_id =? and emp_notification.NOT_TYPE='INCREMENT' ";
            pst = con.prepareStatement(sql);
            pst.setString(1, empId);
            pst.setInt(2, notId);
            rs = pst.executeQuery();
            if (rs.next()) {

                incfb.setHidIncrId(rs.getInt("incrid"));
                incfb.setHnotid(rs.getInt("not_id"));
                incfb.setHidPayId(rs.getInt("pay_id"));

                incfb.setTxtSanctionOrderNo(rs.getString("ordno"));
                if (rs.getDate("orddt") != null && !rs.getDate("orddt").equals("")) {
                    incfb.setTxtSanctionOrderDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("orddt")));
                }
                if (rs.getString("dept_code") != null && !rs.getString("dept_code").equals("")) {
                    incfb.setDeptCode(rs.getString("dept_code"));
                }
                if (rs.getString("off_code") != null && !rs.getString("off_code").equals("")) {
                    incfb.setHidOffCode(rs.getString("off_code"));
                }
                if (rs.getString("auth") != null && !rs.getString("auth").equals("")) {
                    incfb.setHidSpc(rs.getString("auth"));
                }
                if (rs.getString("spn") != null && !rs.getString("spn").equals("")) {
                    incfb.setSancAuthPostName(rs.getString("spn"));
                }
                if (rs.getDate("wef") != null && !rs.getDate("wef").equals("")) {
                    incfb.setTxtWEFDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("wef")));
                }
                incfb.setTxtWEFTime(rs.getString("weft"));
                incfb.setSltPayScale(rs.getString("pay_scale"));
                incfb.setPayCell(rs.getString("pay_cell"));
                incfb.setPayLevel(rs.getString("pay_level"));
                incfb.setTxtIncrAmt(rs.getString("incr"));
                incfb.setTxtP_pay(rs.getString("p_pay"));
                incfb.setTxtOthPay(rs.getString("oth_pay"));
                incfb.setTxtSPay(rs.getString("s_pay"));
                incfb.setTxtNewBasic(rs.getString("pay"));
                incfb.setTxtDescOth(rs.getString("oth_desc"));
                incfb.setTxtGradePay(rs.getString("gp"));
                if (rs.getString("first_incr") != null && rs.getString("first_incr").equals("1")) {
                    incrLvl = rs.getString("first_incr");
                } else if (rs.getString("second_incr") != null && rs.getString("second_incr").equals("2")) {
                    incrLvl = rs.getString("second_incr");
                } else if (rs.getString("third_incr") != null && rs.getString("third_incr").equals("3")) {
                    incrLvl = rs.getString("third_incr");
                } else if (rs.getString("fourth_incr") != null && rs.getString("fourth_incr").equals("4")) {
                    incrLvl = rs.getString("fourth_incr");
                }
                incfb.setIncrementLvl(incrLvl);
                incfb.setIncrementType(rs.getString("incr_type"));
                incfb.setTxtIncrNote(rs.getString("note"));

                /*
                 * Updating the Service Book Language
                 */
                String sbLang = sbDAO.getIncrementSanctionDetails(incfb, incfb.getHnotid(), payCommission);

                pst = con.prepareStatement("UPDATE EMP_NOTIFICATION SET SB_DESCRIPTION =? WHERE NOT_ID=? ");
                pst.setString(1, sbLang);
                pst.setInt(2, incfb.getHnotid());
                pst.execute();

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return incfb;
    }

    @Override
    public IncrementForm getEmpIncRowData(String empId, int incid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        IncrementForm incfb = new IncrementForm();
        String incrLvl = "";
        try {
            con = repodataSource.getConnection();

            String sql = "SELECT * FROM (SELECT E.*,F.INCRID,F.PRID,F.INCR,F.FIRST_INCR,F.second_incr,F.third_incr,F.fourth_incr,\n"
                    + "F.incr_type,G_SPC.SPN FROM EMP_NOTIFICATION E INNER JOIN EMP_INCR F ON E.NOT_ID=F.NOT_ID LEFT OUTER JOIN G_SPC ON E.AUTH=G_SPC.SPC\n"
                    + "WHERE E.NOT_TYPE='INCREMENT' AND E.EMP_ID=? AND F.INCRID=? AND F.PRID IS NULL) T \n"
                    + "LEFT OUTER JOIN \n"
                    + "(SELECT * FROM EMP_PAY_RECORD WHERE NOT_TYPE='INCREMENT' AND EMP_ID=?) R ON \n"
                    + "T.NOT_ID=R.NOT_ID";
            pst = con.prepareStatement(sql);
            pst.setString(1, empId);
            pst.setInt(2, incid);
            pst.setString(3, empId);
            rs = pst.executeQuery();
            if (rs.next()) {

                incfb.setHidIncrId(incid);
                incfb.setHnotid(Integer.parseInt(rs.getString("not_id")));
                incfb.setHidPayId(rs.getInt("pay_id"));

                incfb.setTxtSanctionOrderNo(rs.getString("ordno"));
                if (rs.getDate("orddt") != null && !rs.getDate("orddt").equals("")) {
                    incfb.setTxtSanctionOrderDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("orddt")));
                }

                incfb.setRadsancauthtype(rs.getString("organization_type"));

                if (rs.getString("dept_code") != null && !rs.getString("dept_code").equals("")) {
                    incfb.setDeptCode(rs.getString("dept_code"));
                }
                if (rs.getString("off_code") != null && !rs.getString("off_code").equals("")) {
                    incfb.setHidOffCode(rs.getString("off_code"));
                }
                if (rs.getString("auth") != null && !rs.getString("auth").equals("")) {
                    incfb.setHidSpc(rs.getString("auth"));
                }
                if (rs.getString("spn") != null && !rs.getString("spn").equals("")) {
                    incfb.setSancAuthPostName(rs.getString("spn"));
                }
                if (rs.getDate("wef") != null && !rs.getDate("wef").equals("")) {
                    incfb.setTxtWEFDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("wef")));
                }
                incfb.setTxtWEFTime(rs.getString("weft"));
                incfb.setSltPayScale(rs.getString("pay_scale"));
                incfb.setPayCell(rs.getString("pay_cell"));
                incfb.setPayLevel(rs.getString("pay_level"));
                incfb.setTxtIncrAmt(rs.getString("incr"));
                if (rs.getString("p_pay") != null && !rs.getString("p_pay").equals("") && rs.getDouble("p_pay") > 1) {
                    incfb.setTxtP_pay(rs.getString("p_pay"));
                }
                if (rs.getString("oth_pay") != null && !rs.getString("oth_pay").equals("") && rs.getDouble("oth_pay") > 1) {
                    incfb.setTxtOthPay(rs.getString("oth_pay"));
                }
                if (rs.getString("s_pay") != null && !rs.getString("s_pay").equals("") && rs.getDouble("s_pay") > 1) {
                    incfb.setTxtSPay(rs.getString("s_pay"));
                }
                if (rs.getString("gp") != null && !rs.getString("gp").equals("") && rs.getDouble("gp") > 1) {
                    incfb.setTxtGradePay(rs.getString("gp"));
                }

                incfb.setTxtNewBasic(rs.getString("pay"));

                incfb.setTxtDescOth(rs.getString("oth_desc"));

                if (rs.getString("first_incr") != null && rs.getString("first_incr").equals("1")) {
                    incrLvl = rs.getString("first_incr");
                } else if (rs.getString("second_incr") != null && rs.getString("second_incr").equals("2")) {
                    incrLvl = rs.getString("second_incr");
                } else if (rs.getString("third_incr") != null && rs.getString("third_incr").equals("3")) {
                    incrLvl = rs.getString("third_incr");
                } else if (rs.getString("fourth_incr") != null && rs.getString("fourth_incr").equals("4")) {
                    incrLvl = rs.getString("fourth_incr");
                }
                incfb.setIncrementLvl(incrLvl);
                incfb.setIncrementType(rs.getString("incr_type"));
                incfb.setTxtIncrNote(rs.getString("note"));

                if (rs.getString("if_visible") != null && rs.getString("if_visible").equals("N")) {
                    incfb.setChkNotSBPrint("Y");
                }

                if (incfb.getRadsancauthtype() != null && incfb.getRadsancauthtype().equals("GOI")) {
                    incfb.setSancAuthPostName(getOtherSpn(rs.getString("auth")));
                    incfb.setHidSancAuthorityOthSpc(rs.getString("auth"));
                } else {
                    incfb.setRadsancauthtype("GOO");
                }
                if (rs.getString("if_remuneration") != null && rs.getString("if_remuneration").equals("Y")) {
                    incfb.setIsRemuneration(rs.getString("if_remuneration"));
                    incfb.setHidremBasic(rs.getString("pay"));
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return incfb;
    }

    @Override
    public int getIncrementListCount(String empId) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        int totalcnt = 0;
        try {
            con = repodataSource.getConnection();

            String sql = "SELECT count(*) cnt FROM (SELECT incr.* FROM(SELECT SV_ID,CANCEL_TYPE,LINK_ID,T.NOT_ID,INCRID,INCR,"
                    + " FIRST_INCR,SECOND_INCR,THIRD_INCR,FOURTH_INCR,INCR_TYPE,DOE, ORDNO,ORDDT,AUTH,OFF_CODE,DEPT_CODE,WEF,WEFT,"
                    + " pay_scale,s_pay, p_pay,oth_pay,oth_desc,pay,gp FROM (SELECT E.*,F.PRID,F.INCRID,F.INCR,F.FIRST_INCR,"
                    + " F.second_incr, F.third_incr,F.fourth_incr,F.incr_type FROM"
                    + " (SELECT * FROM EMP_INCR WHERE NOT_TYPE='INCREMENT' AND EMP_ID=? AND PRID IS NULL) F  LEFT OUTER JOIN"
                    + " (select N1.*,N2.NOT_TYPE CANCEL_TYPE FROM  ("
                    + " SELECT SV_ID,NOT_ID, NOT_TYPE, EMP_ID, DOE, TOE, IF_ASSUMED, ORDNO, ORDDT, DEPT_CODE, OFF_CODE, AUTH, ent_dept,"
                    + " ent_off, ent_auth, NOTE, ISCANCELED, LINK_ID  FROM EMP_NOTIFICATION WHERE EMP_ID=?"
                    + " and NOT_TYPE='INCREMENT') N1 LEFT OUTER JOIN (SELECT SV_ID,NOT_ID, NOT_TYPE, EMP_ID, DOE, TOE, IF_ASSUMED,"
                    + " ORDNO, ORDDT, DEPT_CODE, OFF_CODE, AUTH, ent_dept, ent_off, ent_auth, NOTE, ISCANCELED, LINK_ID FROM"
                    + " EMP_NOTIFICATION WHERE EMP_ID=?)  N2  ON  N1.LINK_ID=N2.NOT_ID)  E"
                    + " ON E.NOT_ID=F.NOT_ID ) T LEFT OUTER JOIN (SELECT * FROM EMP_PAY_RECORD WHERE NOT_TYPE='INCREMENT'"
                    + " AND EMP_ID=?)  R ON T.NOT_ID=R.NOT_ID  ORDER BY DOE DESC) incr)incrtemp";
            pst = con.prepareStatement(sql);
            pst.setString(1, empId);
            pst.setString(2, empId);
            pst.setString(3, empId);
            pst.setString(4, empId);
            rs = pst.executeQuery();
            if (rs.next()) {
                totalcnt = rs.getInt("cnt");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return totalcnt;
    }

    @Override
    public boolean deleteIncrement(IncrementForm incrementForm) {

        Connection con = null;
        boolean deleted = false;
        Statement stmt = null;
        ResultSet rs = null;

        PreparedStatement pst = null;

        String linkid = null;
        int status = 0;

        Format sdf = new SimpleDateFormat("dd-MMM-yyyy");

        try {
            con = dataSource.getConnection();

            pst = con.prepareStatement("delete from emp_incr where incrid=? and emp_id=? and not_id=?");

            stmt = con.createStatement();
            String sql = "SELECT LINK_ID FROM EMP_NOTIFICATION WHERE EMP_ID='" + incrementForm.getEmpid() + "' AND NOT_ID=" + incrementForm.getHnotid();
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                linkid = rs.getString("LINK_ID");
            }

            if (linkid == null) {
                if (incrementForm.getEmpid() != null && incrementForm.getHidIncrId() > 0) {
                    rs = stmt.executeQuery("SELECT NOT_ID FROM EMP_NOTIFICATION WHERE LINK_ID='" + incrementForm.getHnotid() + "'");
                    if (rs.next()) {
                        linkid = rs.getString("NOT_ID");
                    }
                    if (linkid != null && !linkid.equals("")) {
                        deleteSuperInc(incrementForm.getEmpid(), linkid, con);
                    }
                    pst.setInt(1, incrementForm.getHidIncrId());
                    pst.setString(2, incrementForm.getEmpid());
                    pst.setInt(3, incrementForm.getHnotid());
                    status = pst.executeUpdate();
                }
                DataBaseFunctions.closeSqlObjects(pst);
                pst = con.prepareStatement("delete from emp_pay_record where not_id=" + incrementForm.getHnotid() + " and emp_id='" + incrementForm.getEmpid() + "' and not_type='INCREMENT'");
                status = pst.executeUpdate();

                DataBaseFunctions.closeSqlObjects(pst);
                pst = con.prepareStatement("delete from emp_notification where not_id=" + incrementForm.getHnotid() + " and emp_id='" + incrementForm.getEmpid() + "' and not_type='INCREMENT'");
                status = pst.executeUpdate();

                //ChronologySerial.deletefromSBOutput(con, incrementForm.getEmpid(), "INCREMENT", incrementForm.getHnotid());
                //CommonFunctions.deleteOthSpc(incrementForm.getTxtothPostName(), con);
                String serverDate = sdf.format(new Date());

                if (serverDate != null && !serverDate.trim().equals("")) {
                    //StatusAsOn.updateEmpPayInfoOnDate(incrementForm.getEmpid(), serverDate, con);
                }
                deleted = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return deleted;
    }

    public boolean deleteSuperInc(String empid, String notid, Connection con) throws Exception {

        PreparedStatement pst = null;

        boolean delete = false;
        int conf = 0;

        try {

            if (notid != null || !notid.equals("")) {
                pst = con.prepareStatement("update emp_notification set ISCANCELED='N',LINK_ID=NULL where not_id='" + notid + "' and emp_id='" + empid + "' and not_type='INCREMENT'");
                conf = pst.executeUpdate();
            }

            if (conf > 0) {
                delete = true;
            } else {
                delete = false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
        }
        return delete;
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
    public void saveCancelIncrement(IncrementForm incfb, String entdept, String entoff, String entauth, String loginuserid) {

        Connection con = null;

        PreparedStatement pst = null;

        NotificationBean nb = new NotificationBean();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        try {
            con = dataSource.getConnection();

            if (incfb.getEmpid() != null && !incfb.getEmpid().equals("")) {

                nb.setNottype("CANCELLATION");
                nb.setEmpId(incfb.getEmpid());
                nb.setDateofEntry(new Date());
                nb.setOrdno(incfb.getTxtSanctionOrderNo());
                nb.setOrdDate(sdf.parse(incfb.getTxtSanctionOrderDt()));
                nb.setSancDeptCode(incfb.getDeptCode());
                nb.setSancOffCode(incfb.getHidOffCode());
                nb.setRadpostingauthtype(incfb.getRadsancauthtype());
                if (incfb.getRadsancauthtype() != null && incfb.getRadsancauthtype().equals("GOI")) {
                    nb.setSancAuthCode(incfb.getHidSancAuthorityOthSpc());
                } else {
                    nb.setSancAuthCode(incfb.getHidSpc());
                }
                nb.setEntryDeptCode(entdept);
                nb.setEntryOffCode(entoff);
                nb.setEntryAuthCode(entauth);
                nb.setNote(incfb.getTxtIncrNote());
                nb.setLoginuserid(loginuserid);
                int notid = notificationDao.insertNotificationData(nb);

                notificationDao.updateCancellationNotificationData(Integer.parseInt(incfb.getLinkid()), notid);

                if (incfb.getChkNotSBPrint() != null && incfb.getChkNotSBPrint().equals("Y")) {
                    pst = con.prepareStatement("UPDATE EMP_NOTIFICATION SET IF_VISIBLE='N' WHERE NOT_ID=?");
                    pst.setInt(1, notid);
                    pst.execute();
                } else {
                    nb.setNottype("INCREMENT");
                    String sbCancelLang = sbDAO.getCancelLangDetails(nb, notid, incfb.getHidSancAuthorityOthSpc());
                    pst = con.prepareStatement("UPDATE EMP_NOTIFICATION SET SB_DESCRIPTION=?,IF_VISIBLE='Y' WHERE NOT_ID=?");
                    pst.setString(1, sbCancelLang);
                    pst.setInt(2, notid);
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

    @Override
    public IncrementForm editCancelIncRowData(String empid, int notid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        IncrementForm incfb = new IncrementForm();
        try {
            con = this.repodataSource.getConnection();

            String sql = "SELECT E1.*,E2.NOT_ID LINKID,G_SPC.SPN FROM EMP_NOTIFICATION E1"
                    + " INNER JOIN EMP_NOTIFICATION E2 ON E1.NOT_ID=E2.LINK_ID"
                    + " LEFT OUTER JOIN G_SPC ON E1.AUTH=G_SPC.SPC"
                    + " WHERE E1.NOT_TYPE='CANCELLATION' AND E1.EMP_ID=? AND E1.NOT_ID=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            pst.setInt(2, notid);
            rs = pst.executeQuery();
            if (rs.next()) {

                incfb.setHnotid(rs.getInt("not_id"));
                incfb.setLinkid(rs.getString("LINKID"));

                incfb.setTxtSanctionOrderNo(rs.getString("ordno"));
                if (rs.getDate("orddt") != null && !rs.getDate("orddt").equals("")) {
                    incfb.setTxtSanctionOrderDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("orddt")));
                }

                incfb.setRadsancauthtype(rs.getString("organization_type"));

                if (rs.getString("dept_code") != null && !rs.getString("dept_code").equals("")) {
                    incfb.setDeptCode(rs.getString("dept_code"));
                }
                if (rs.getString("off_code") != null && !rs.getString("off_code").equals("")) {
                    incfb.setHidOffCode(rs.getString("off_code"));
                }
                if (rs.getString("auth") != null && !rs.getString("auth").equals("")) {
                    incfb.setHidSpc(rs.getString("auth"));
                }
                if (rs.getString("spn") != null && !rs.getString("spn").equals("")) {
                    incfb.setSancAuthPostName(rs.getString("spn"));
                }

                incfb.setTxtIncrNote(rs.getString("note"));

                if (rs.getString("if_visible") != null && rs.getString("if_visible").equals("N")) {
                    incfb.setChkNotSBPrint("Y");
                }

                if (incfb.getRadsancauthtype() != null && incfb.getRadsancauthtype().equals("GOI")) {
                    incfb.setSancAuthPostName(getOtherSpn(rs.getString("auth")));
                    incfb.setHidSancAuthorityOthSpc(rs.getString("auth"));
                } else {
                    incfb.setRadsancauthtype("GOO");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return incfb;
    }

    @Override
    public void updateCancelIncrement(IncrementForm incfb, String entdept, String entoff, String entauth, String loginuserid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");

        NotificationBean nb = new NotificationBean();
        try {
            con = dataSource.getConnection();

            if (incfb.getEmpid() != null && !incfb.getEmpid().equals("")) {

                nb.setEmpId(incfb.getEmpid());
                nb.setNotid(incfb.getHnotid());
                nb.setOrdno(incfb.getTxtSanctionOrderNo());
                nb.setOrdDate(sdf.parse(incfb.getTxtSanctionOrderDt()));
                nb.setSancDeptCode(incfb.getDeptCode());
                nb.setSancOffCode(incfb.getHidOffCode());
                //nb.setSancAuthCode(incfb.getHidSpc());
                nb.setRadpostingauthtype(incfb.getRadsancauthtype());
                if (incfb.getRadsancauthtype() != null && incfb.getRadsancauthtype().equals("GOI")) {
                    nb.setSancAuthCode(incfb.getHidSancAuthorityOthSpc());
                } else {
                    nb.setSancAuthCode(incfb.getHidSpc());
                }
                nb.setEntryDeptCode(entdept);
                nb.setEntryOffCode(entoff);
                nb.setEntryAuthCode(entauth);
                nb.setNote(incfb.getTxtIncrNote());
                nb.setLoginuserid(loginuserid);
                notificationDao.modifyNotificationData(nb);

                if (incfb.getChkNotSBPrint() != null && incfb.getChkNotSBPrint().equals("Y")) {
                    pst = con.prepareStatement("UPDATE EMP_NOTIFICATION SET IF_VISIBLE='N' WHERE NOT_ID=?");
                    pst.setInt(1, nb.getNotid());
                    pst.execute();
                } else {
                    nb.setNottype("INCREMENT");
                    String sbCancelLang = sbDAO.getCancelLangDetails(nb, nb.getNotid(), incfb.getHidSancAuthorityOthSpc());
                    pst = con.prepareStatement("UPDATE EMP_NOTIFICATION SET SB_DESCRIPTION=?,IF_VISIBLE='Y' WHERE NOT_ID=?");
                    pst.setString(1, sbCancelLang);
                    pst.setInt(2, nb.getNotid());
                    pst.execute();
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
    public IncrementForm editSupersedeIncRowData(String empId, int notid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        IncrementForm incfb = new IncrementForm();
        String incrLvl = "";
        try {
            con = this.repodataSource.getConnection();

            /*String sql = "SELECT * FROM (SELECT E.*,F.INCRID,F.PRID,F.INCR,F.FIRST_INCR,F.second_incr,F.third_incr,F.fourth_incr,"
             + " F.incr_type,G_SPC.SPN FROM EMP_NOTIFICATION E INNER JOIN EMP_INCR F ON E.NOT_ID=F.NOT_ID LEFT OUTER JOIN G_SPC ON E.AUTH=G_SPC.SPC"
             + " WHERE E.NOT_TYPE='INCREMENT'"
             + " AND E.EMP_ID=? AND F.INCRID=? AND F.PRID IS NULL) T LEFT OUTER JOIN ("
             + " SELECT * FROM EMP_PAY_RECORD WHERE NOT_TYPE='INCREMENT' AND EMP_ID=?) R ON T.NOT_ID=R.NOT_ID";*/
            String sql = "SELECT R.*,E2.NOT_ID linkid,E.*,F.INCRID,F.PRID,F.INCR,F.FIRST_INCR,F.second_incr,F.third_incr,F.fourth_incr,"
                    + " F.incr_type,G_SPC.SPN FROM EMP_NOTIFICATION E"
                    + " INNER JOIN EMP_NOTIFICATION E2 ON E.NOT_ID=E2.LINK_ID"
                    + " INNER JOIN EMP_INCR F ON E.NOT_ID=F.NOT_ID"
                    + " LEFT OUTER JOIN G_SPC ON E.AUTH=G_SPC.SPC"
                    + " LEFT OUTER JOIN EMP_PAY_RECORD R ON E.NOT_ID=R.NOT_ID"
                    + " WHERE E.NOT_TYPE='INCREMENT' AND E.EMP_ID=? AND E.NOT_ID=? AND F.PRID IS NULL";
            pst = con.prepareStatement(sql);
            pst.setString(1, empId);
            pst.setInt(2, notid);
            rs = pst.executeQuery();
            if (rs.next()) {
                incfb.setHidIncrId(rs.getInt("INCRID"));
                incfb.setLinkid(rs.getString("linkid"));
                incfb.setHnotid(rs.getInt("not_id"));
                incfb.setHidPayId(rs.getInt("pay_id"));
                incfb.setTxtSanctionOrderNo(rs.getString("ordno"));
                if (rs.getDate("orddt") != null && !rs.getDate("orddt").equals("")) {
                    incfb.setTxtSanctionOrderDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("orddt")));
                }
                incfb.setRadsancauthtype(rs.getString("organization_type"));
                if (rs.getString("dept_code") != null && !rs.getString("dept_code").equals("")) {
                    incfb.setDeptCode(rs.getString("dept_code"));
                }
                if (rs.getString("off_code") != null && !rs.getString("off_code").equals("")) {
                    incfb.setHidOffCode(rs.getString("off_code"));
                }
                if (rs.getString("auth") != null && !rs.getString("auth").equals("")) {
                    incfb.setHidSpc(rs.getString("auth"));
                }
                if (rs.getString("spn") != null && !rs.getString("spn").equals("")) {
                    incfb.setSancAuthPostName(rs.getString("spn"));
                }
                if (rs.getDate("wef") != null && !rs.getDate("wef").equals("")) {
                    incfb.setTxtWEFDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("wef")));
                }
                incfb.setTxtWEFTime(rs.getString("weft"));
                incfb.setSltPayScale(rs.getString("pay_scale"));
                incfb.setPayCell(rs.getString("pay_cell"));
                incfb.setPayLevel(rs.getString("pay_level"));
                incfb.setTxtIncrAmt(rs.getString("incr"));
                if (rs.getString("p_pay") != null && !rs.getString("p_pay").equals("") && rs.getDouble("p_pay") > 1) {
                    incfb.setTxtP_pay(rs.getString("p_pay"));
                }
                if (rs.getString("oth_pay") != null && !rs.getString("oth_pay").equals("") && rs.getDouble("oth_pay") > 1) {
                    incfb.setTxtOthPay(rs.getString("oth_pay"));
                }
                if (rs.getString("s_pay") != null && !rs.getString("s_pay").equals("") && rs.getDouble("s_pay") > 1) {
                    incfb.setTxtSPay(rs.getString("s_pay"));
                }
                if (rs.getString("gp") != null && !rs.getString("gp").equals("") && rs.getDouble("gp") > 1) {
                    incfb.setTxtGradePay(rs.getString("gp"));
                }

                incfb.setTxtNewBasic(rs.getString("pay"));
                incfb.setTxtDescOth(rs.getString("oth_desc"));

                if (rs.getString("first_incr") != null && rs.getString("first_incr").equals("1")) {
                    incrLvl = rs.getString("first_incr");
                } else if (rs.getString("second_incr") != null && rs.getString("second_incr").equals("2")) {
                    incrLvl = rs.getString("second_incr");
                } else if (rs.getString("third_incr") != null && rs.getString("third_incr").equals("3")) {
                    incrLvl = rs.getString("third_incr");
                } else if (rs.getString("fourth_incr") != null && rs.getString("fourth_incr").equals("4")) {
                    incrLvl = rs.getString("fourth_incr");
                }
                incfb.setIncrementLvl(incrLvl);
                incfb.setIncrementType(rs.getString("incr_type"));
                incfb.setTxtIncrNote(rs.getString("note"));

                if (rs.getString("if_visible") != null && rs.getString("if_visible").equals("N")) {
                    incfb.setChkNotSBPrint("Y");
                }

                if (incfb.getRadsancauthtype() != null && incfb.getRadsancauthtype().equals("GOI")) {
                    incfb.setSancAuthPostName(getOtherSpn(rs.getString("auth")));
                    incfb.setHidSancAuthorityOthSpc(rs.getString("auth"));
                } else {
                    incfb.setRadsancauthtype("GOO");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return incfb;
    }

    @Override
    public void saveSupersedeIncrement(IncrementForm incfb, String entdept, String entoff, String entauth, String payComm, String loginuserid) {

        Connection con = null;

        PreparedStatement pst = null;

        NotificationBean nb = new NotificationBean();
        EmpPayRecordForm epayrecordform = new EmpPayRecordForm();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        try {
            con = this.dataSource.getConnection();

            if (incfb.getEmpid() != null && !incfb.getEmpid().equals("")) {

                nb.setNottype("INCREMENT");
                nb.setEmpId(incfb.getEmpid());
                nb.setDateofEntry(new Date());
                nb.setOrdno(incfb.getTxtSanctionOrderNo());
                nb.setOrdDate(sdf.parse(incfb.getTxtSanctionOrderDt()));
                nb.setSancDeptCode(incfb.getDeptCode());
                nb.setSancOffCode(incfb.getHidOffCode());
                nb.setRadpostingauthtype(incfb.getRadsancauthtype());
                if (incfb.getRadsancauthtype() != null && incfb.getRadsancauthtype().equals("GOI")) {
                    nb.setSancAuthCode(incfb.getHidSancAuthorityOthSpc());
                } else {
                    nb.setSancAuthCode(incfb.getHidSpc());
                }
                nb.setEntryDeptCode(entdept);
                nb.setEntryOffCode(entoff);
                nb.setEntryAuthCode(entauth);
                nb.setNote(incfb.getTxtIncrNote());
                nb.setLoginuserid(loginuserid);
                int notid = notificationDao.insertNotificationData(nb);

                notificationDao.updateSupersedeNotificationData(Integer.parseInt(incfb.getLinkid()), notid);

                pst = con.prepareStatement("insert into emp_incr (not_id, not_type, emp_id, incr, first_incr, second_incr, third_incr, fourth_incr, incr_type) values(?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
                pst.setInt(1, notid);
                pst.setString(2, "INCREMENT");
                pst.setString(3, incfb.getEmpid());
                if (incfb.getTxtIncrAmt() != null && !incfb.getTxtIncrAmt().equalsIgnoreCase("")) {
                    pst.setDouble(4, Double.parseDouble(incfb.getTxtIncrAmt()));
                } else {
                    pst.setDouble(4, 0);
                }
                if (incfb.getIncrementLvl() != null && incfb.getIncrementLvl().equals("1")) {
                    pst.setString(5, incfb.getIncrementLvl());
                } else {
                    pst.setString(5, "");
                }
                if (incfb.getIncrementLvl() != null && incfb.getIncrementLvl().equals("2")) {
                    pst.setString(6, incfb.getIncrementLvl());
                } else {
                    pst.setString(6, "");
                }
                if (incfb.getIncrementLvl() != null && incfb.getIncrementLvl().equals("3")) {
                    pst.setString(7, incfb.getIncrementLvl());
                } else {
                    pst.setString(7, "");
                }
                if (incfb.getIncrementLvl() != null && incfb.getIncrementLvl().equals("4")) {
                    pst.setString(8, incfb.getIncrementLvl());
                } else {
                    pst.setString(8, "");
                }
                if (incfb.getIncrementType() != null && !incfb.getIncrementType().equals("")) {
                    pst.setString(9, incfb.getIncrementType());
                } else {
                    pst.setString(9, "");
                }
                pst.executeUpdate();

                ResultSet rs = pst.getGeneratedKeys();
                rs.next();
                int incrid = rs.getInt("incrid");

                String sqlString = ToStringBuilder.reflectionToString(pst);
                //sbDAO.createTransactionLog(loginuserid, incfb.getEmpid(), "INCREMENT", sqlString);
                pst = con.prepareStatement("UPDATE emp_incr SET query_string=? where incrid=? and EMP_ID=?");
                pst.setString(1, sqlString);
                pst.setInt(2, incrid);
                pst.setString(3, incfb.getEmpid());
                pst.executeUpdate();

                epayrecordform.setEmpid(incfb.getEmpid());
                epayrecordform.setNot_id(notid);
                epayrecordform.setNot_type("INCREMENT");
                epayrecordform.setPayscale(incfb.getSltPayScale());
                epayrecordform.setBasic(incfb.getTxtNewBasic());
                epayrecordform.setGp(incfb.getTxtGradePay());
                epayrecordform.setS_pay(incfb.getTxtSPay());
                epayrecordform.setP_pay(incfb.getTxtP_pay());
                epayrecordform.setOth_pay(incfb.getTxtOthPay());
                epayrecordform.setOth_desc(incfb.getTxtDescOth());
                epayrecordform.setWefDt(incfb.getTxtWEFDt());
                epayrecordform.setWefTime(incfb.getTxtWEFTime());
                epayrecordform.setPayLevel(incfb.getPayLevel());
                epayrecordform.setPayCell(incfb.getPayCell());
                emppayrecordDAO.saveEmpPayRecordData(epayrecordform);

                String serverDate = sdf.format(new Date());

                //if (serverDate != null && !serverDate.trim().equals("")) {
                //boolean updatepay = empinfoDAO.isupdatePayOrPostingInfo(incfb.getEmpid(), incfb.getTxtWEFDt(), incfb.getTxtSanctionOrderDt(), "PAY");
                //if (updatepay == true) {
                if (incfb.getRdTransaction() != null && incfb.getRdTransaction().equals("C")) {
                    pst = con.prepareStatement("UPDATE EMP_MAST SET matrix_level=?, matrix_cell=?, cur_salary=?, PAY_DATE=?, CUR_BASIC_SALARY=?, gp=?, spay=?, ppay=?, othpay=?, ST_DATE_OF_CUR_SALARY=DATE_OF_NINCR,DATE_OF_NINCR=(SELECT DATE_OF_NINCR + CAST('12 MONTHS' AS INTERVAL) FROM EMP_MAST WHERE EMP_ID=?) WHERE EMP_ID=?");
                    if (incfb.getPayLevel() != null && !incfb.getPayLevel().equals("")) {
                        pst.setInt(1, Integer.parseInt(incfb.getPayLevel()));
                    } else {
                        pst.setInt(1, 0);
                    }
                    if (incfb.getPayCell() != null && !incfb.getPayCell().equals("")) {
                        pst.setInt(2, Integer.parseInt(incfb.getPayCell()));
                    } else {
                        pst.setInt(2, 0);
                    }
                    pst.setString(3, incfb.getSltPayScale());
                    pst.setTimestamp(4, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(incfb.getTxtWEFDt()).getTime()));
                    pst.setDouble(5, Double.parseDouble(incfb.getTxtNewBasic()));
                    if (incfb.getTxtGradePay() != null && !incfb.getTxtGradePay().equals("")) {
                        pst.setDouble(6, Double.parseDouble(incfb.getTxtGradePay()));
                    } else {
                        pst.setDouble(6, 0);
                    }
                    if (incfb.getTxtSPay() != null && !incfb.getTxtSPay().equals("")) {
                        pst.setInt(7, Integer.parseInt(incfb.getTxtSPay()));
                    } else {
                        pst.setInt(7, 0);
                    }
                    if (incfb.getTxtP_pay() != null && !incfb.getTxtP_pay().equals("")) {
                        pst.setInt(8, Integer.parseInt(incfb.getTxtP_pay()));
                    } else {
                        pst.setInt(8, 0);
                    }
                    if (incfb.getTxtOthPay() != null && !incfb.getTxtOthPay().equals("")) {
                        pst.setInt(9, Integer.parseInt(incfb.getTxtOthPay()));
                    } else {
                        pst.setInt(9, 0);
                    }
                    pst.setString(10, incfb.getEmpid());
                    pst.setString(11, incfb.getEmpid());
                    pst.executeUpdate();

                    sqlString = ToStringBuilder.reflectionToString(pst);
                    sbDAO.createTransactionLog(loginuserid, incfb.getEmpid(), "INCREMENT", sqlString);
                }
                //}
                //}

                /*
                 * Updating the Service Book Language
                 */
                if (incfb.getChkNotSBPrint() != null && incfb.getChkNotSBPrint().equals("Y")) {
                    pst = con.prepareStatement("UPDATE EMP_NOTIFICATION SET IF_VISIBLE='N' WHERE NOT_ID=?");
                    pst.setInt(1, notid);
                    pst.execute();
                } else if (incfb.getChkNotSBPrint() == null || incfb.getChkNotSBPrint().equals("")) {
                    String sbSupersedeLanguage = sbDAO.getSupersedeLangDetails(Integer.parseInt(incfb.getLinkid()), incfb.getHidSancAuthorityOthSpc());

                    pst = con.prepareStatement("UPDATE EMP_NOTIFICATION SET SB_DESCRIPTION=?,IF_VISIBLE='Y' WHERE NOT_ID=?");
                    pst.setString(1, sbSupersedeLanguage);
                    pst.setInt(2, notid);
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

    @Override
    public void updateSupersedeIncrement(IncrementForm incfb, String entdept, String entoff, String entauth, String payComm, String loginuserid) throws Exception {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        boolean dateincr = false;

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");

        NotificationBean nb = new NotificationBean();
        EmpPayRecordForm epayrecordform = new EmpPayRecordForm();
        EmployeeInformation ei = new EmployeeInformation();
        try {
            con = this.dataSource.getConnection();

            if (incfb.getEmpid() != null && !incfb.getEmpid().equals("")) {

                nb.setEmpId(incfb.getEmpid());
                nb.setNotid(incfb.getHnotid());
                nb.setOrdno(incfb.getTxtSanctionOrderNo());
                nb.setOrdDate(sdf.parse(incfb.getTxtSanctionOrderDt()));
                nb.setSancDeptCode(incfb.getDeptCode());
                nb.setSancOffCode(incfb.getHidOffCode());
                //nb.setSancAuthCode(incfb.getHidSpc());
                nb.setRadpostingauthtype(incfb.getRadsancauthtype());
                if (incfb.getRadsancauthtype() != null && incfb.getRadsancauthtype().equals("GOI")) {
                    nb.setSancAuthCode(incfb.getHidSancAuthorityOthSpc());
                } else {
                    nb.setSancAuthCode(incfb.getHidSpc());
                }
                nb.setEntryDeptCode(entdept);
                nb.setEntryOffCode(entoff);
                nb.setEntryAuthCode(entauth);
                nb.setNote(incfb.getTxtIncrNote());
                nb.setLoginuserid(loginuserid);
                notificationDao.modifyNotificationData(nb);

                dateincr = getCmpDate(con, "DATE_OF_NINCR", incfb.getEmpid());

                pst = con.prepareStatement("update emp_incr set not_id=?,not_type=?,incr=?,first_incr=?,second_incr=?,third_incr=?,fourth_incr=?,incr_type=? where emp_id=? and incrid=?");

                pst.setInt(1, incfb.getHnotid());
                pst.setString(2, "INCREMENT");
                if (incfb.getTxtIncrAmt() != null && !incfb.getTxtIncrAmt().equalsIgnoreCase("")) {
                    pst.setDouble(3, java.lang.Double.parseDouble(incfb.getTxtIncrAmt()));
                } else {
                    pst.setDouble(3, 0);
                }
                if (incfb.getIncrementLvl() != null && incfb.getIncrementLvl().equals("1")) {
                    pst.setString(4, incfb.getIncrementLvl());
                } else {
                    pst.setString(4, "N");
                }
                if (incfb.getIncrementLvl() != null && incfb.getIncrementLvl().equals("2")) {
                    pst.setString(5, incfb.getIncrementLvl());
                } else {
                    pst.setString(5, "N");
                }
                if (incfb.getIncrementLvl() != null && incfb.getIncrementLvl().equals("3")) {
                    pst.setString(6, incfb.getIncrementLvl());
                } else {
                    pst.setString(6, "N");
                }
                if (incfb.getIncrementLvl() != null && incfb.getIncrementLvl().equals("4")) {
                    pst.setString(7, incfb.getIncrementLvl());
                } else {
                    pst.setString(7, "N");
                }
                if (incfb.getIncrementType() != null && !incfb.getIncrementType().equals("")) {
                    pst.setString(8, incfb.getIncrementType());
                } else {
                    pst.setString(8, "N");
                }
                pst.setString(9, incfb.getEmpid());
                pst.setInt(10, incfb.getHidIncrId());
                pst.executeUpdate();

                String sqlString = ToStringBuilder.reflectionToString(pst);
                sbDAO.createTransactionLog(loginuserid, incfb.getEmpid(), "INCREMENT", sqlString);

                epayrecordform.setEmpid(incfb.getEmpid());
                epayrecordform.setNot_id(incfb.getHnotid());
                epayrecordform.setNot_type("INCREMENT");
                epayrecordform.setPayid(incfb.getHidPayId());
                epayrecordform.setPayscale(incfb.getSltPayScale());
                epayrecordform.setBasic(incfb.getTxtNewBasic());
                epayrecordform.setGp(incfb.getTxtGradePay());
                epayrecordform.setS_pay(incfb.getTxtSPay());
                epayrecordform.setP_pay(incfb.getTxtP_pay());
                epayrecordform.setOth_pay(incfb.getTxtOthPay());
                epayrecordform.setOth_desc(incfb.getTxtDescOth());
                epayrecordform.setWefDt(incfb.getTxtWEFDt());
                epayrecordform.setWefTime(incfb.getTxtWEFTime());
                epayrecordform.setPayCell(incfb.getPayCell());
                epayrecordform.setPayLevel(incfb.getPayLevel());
                emppayrecordDAO.updateEmpPayRecordData(epayrecordform);

                String serverDate = sdf.format(new Date());
                //if (serverDate != null && !serverDate.trim().equals("")) {
                //boolean updatepay = empinfoDAO.isupdatePayOrPostingInfo(incfb.getEmpid(), incfb.getTxtWEFDt(), incfb.getTxtSanctionOrderDt(), "PAY");
                //if (updatepay == true) {
                if (incfb.getRdTransaction() != null && incfb.getRdTransaction().equals("C")) {
                    pst = con.prepareStatement("UPDATE EMP_MAST SET matrix_level=?, matrix_cell=?, cur_salary=?, PAY_DATE=?, CUR_BASIC_SALARY=?, gp=?, spay=?, ppay=?, othpay=?, ST_DATE_OF_CUR_SALARY=DATE_OF_NINCR,DATE_OF_NINCR=(SELECT DATE_OF_NINCR + CAST('12 MONTHS' AS INTERVAL) FROM EMP_MAST WHERE EMP_ID=?) WHERE EMP_ID=?");
                    if (incfb.getPayLevel() != null && !incfb.getPayLevel().equals("")) {
                        pst.setInt(1, Integer.parseInt(incfb.getPayLevel()));
                    } else {
                        pst.setInt(1, 0);
                    }
                    if (incfb.getPayCell() != null && !incfb.getPayCell().equals("")) {
                        pst.setInt(2, Integer.parseInt(incfb.getPayCell()));
                    } else {
                        pst.setInt(2, 0);
                    }
                    pst.setString(3, incfb.getSltPayScale());
                    pst.setTimestamp(4, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(incfb.getTxtWEFDt()).getTime()));
                    pst.setDouble(5, Double.parseDouble(incfb.getTxtNewBasic()));
                    if (incfb.getTxtGradePay() != null && !incfb.getTxtGradePay().equals("")) {
                        pst.setDouble(6, Double.parseDouble(incfb.getTxtGradePay()));
                    } else {
                        pst.setDouble(6, 0);
                    }
                    if (incfb.getTxtSPay() != null && !incfb.getTxtSPay().equals("")) {
                        pst.setInt(7, Integer.parseInt(incfb.getTxtSPay()));
                    } else {
                        pst.setInt(7, 0);
                    }
                    if (incfb.getTxtP_pay() != null && !incfb.getTxtP_pay().equals("")) {
                        pst.setInt(8, Integer.parseInt(incfb.getTxtP_pay()));
                    } else {
                        pst.setInt(8, 0);
                    }
                    if (incfb.getTxtOthPay() != null && !incfb.getTxtOthPay().equals("")) {
                        pst.setInt(9, Integer.parseInt(incfb.getTxtOthPay()));
                    } else {
                        pst.setInt(9, 0);
                    }
                    pst.setString(10, incfb.getEmpid());
                    pst.setString(11, incfb.getEmpid());
                    pst.executeUpdate();

                    sqlString = ToStringBuilder.reflectionToString(pst);
                    sbDAO.createTransactionLog(loginuserid, incfb.getEmpid(), "INCREMENT", sqlString);
                }
                //}
                //}

                /*
                 * Updating the Service Book Language
                 */
                if (incfb.getChkNotSBPrint() != null && incfb.getChkNotSBPrint().equals("Y")) {
                    pst = con.prepareStatement("UPDATE EMP_NOTIFICATION SET IF_VISIBLE='N' WHERE NOT_ID=?");
                    pst.setInt(1, nb.getNotid());
                    pst.execute();
                } else if (incfb.getChkNotSBPrint() == null || incfb.getChkNotSBPrint().equals("")) {
                    String sbSupersedeLanguage = sbDAO.getSupersedeLangDetails(Integer.parseInt(incfb.getLinkid()), incfb.getHidSancAuthorityOthSpc());

                    pst = con.prepareStatement("UPDATE EMP_NOTIFICATION SET SB_DESCRIPTION=?,IF_VISIBLE='Y' WHERE NOT_ID=?");
                    pst.setString(1, sbSupersedeLanguage);
                    pst.setInt(2, nb.getNotid());
                    pst.execute();
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
    public String checkEmployeeLawStatus(String empid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String employeelawstatus = "N";

        try {
            con = this.repodataSource.getConnection();

            String sql = "select law_level from emp_mast where emp_id=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            rs = pst.executeQuery();
            if (rs.next()) {
                if (rs.getString("law_level") != null && rs.getString("law_level").equals("Y")) {
                    employeelawstatus = "Y";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return employeelawstatus;
    }

    @Override
    public String checkEmployeeIPSStatus(String empid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String employeeipsstatus = "N";

        try {
            con = this.repodataSource.getConnection();

            String sql = "select emp_id from emp_mast where emp_id=? and cur_cadre_code='1166'";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            rs = pst.executeQuery();
            if (rs.next()) {
                if (rs.getString("emp_id") != null && !rs.getString("emp_id").equals("")) {
                    employeeipsstatus = "Y";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return employeeipsstatus;
    }

    @Override
    public List getPayMatrixLevelForLAW() {

        Connection con = null;
        List levellist = new ArrayList();
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            con = this.repodataSource.getConnection();

            pst = con.prepareStatement("select law_level from pay_matrix_2017 group by law_level having law_level like 'J-%' order by law_level");
            rs = pst.executeQuery();
            while (rs.next()) {
                SelectOption so = new SelectOption();
                so.setLabel(rs.getString("law_level"));
                so.setValue(rs.getString("law_level"));
                levellist.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return levellist;
    }

    @Override
    public List getPayMatrixLevelAllForLAW() {
        Connection con = null;
        List lawlevellist = new ArrayList();
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            con = this.repodataSource.getConnection();

            pst = con.prepareStatement("select law_level from pay_matrix_2017 group by law_level order by law_level asc");
            rs = pst.executeQuery();
            while (rs.next()) {
                SelectOption so = new SelectOption();
                so.setLabel(rs.getString("law_level"));
                so.setValue(rs.getString("law_level"));
                lawlevellist.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return lawlevellist;
    }

    @Override
    public List getPayMatrixCellForLAWLevel(String matrixLevel) {
        Connection con = null;
        List celllist = new ArrayList();
        PreparedStatement pst = null;
        ResultSet rs = null;
        SelectOption so = null;
        try {
            con = this.dataSource.getConnection();
            // matrixLevel.contains("J-") is for Judiciary employees
            pst = con.prepareStatement("select level_slno from pay_matrix_2017 where law_level=? order by level_slno ");
            pst.setString(1, matrixLevel);

            rs = pst.executeQuery();
            while (rs.next()) {
                so = new SelectOption();
                so.setLabel(rs.getString("level_slno"));
                so.setValue(rs.getString("level_slno"));
                celllist.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return celllist;
    }

    @Override
    public List getPayMatrixCellForGPLevel(String matrixLevel) {
        Connection con = null;
        List celllist = new ArrayList();
        PreparedStatement pst = null;
        ResultSet rs = null;
        SelectOption so = null;
        try {
            con = this.dataSource.getConnection();
            // matrixLevel.contains("J-") is for Judiciary employees
            if (matrixLevel != null && !matrixLevel.equals("")) {
                pst = con.prepareStatement("select level_slno from pay_matrix_2017 where gp_level=? order by level_slno ");
                pst.setInt(1, Integer.parseInt(matrixLevel));

                rs = pst.executeQuery();
                while (rs.next()) {
                    so = new SelectOption();
                    so.setLabel(rs.getString("level_slno"));
                    so.setValue(rs.getString("level_slno"));
                    celllist.add(so);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return celllist;
    }

    @Override
    public List getPayMatrixLevelForIPS() {

        Connection con = null;

        List levellist = new ArrayList();

        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            con = this.repodataSource.getConnection();

            pst = con.prepareStatement("select ips_level from pay_matrix_2017 group by ips_level order by ips_level");
            rs = pst.executeQuery();
            while (rs.next()) {
                SelectOption so = new SelectOption();
                so.setLabel(rs.getString("ips_level"));
                so.setValue(rs.getString("ips_level"));
                levellist.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return levellist;
    }

    @Override
    public String saveIncrementSBCorrection(IncrementForm incfb, String entdept, String entoff, String entauth, String payComm, String loginuserid, int sbcorrectionid) {

        Connection con = null;

        PreparedStatement pst = null;

        NotificationBean nb = new NotificationBean();
        EmpPayRecordForm epayrecordform = new EmpPayRecordForm();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");

        String sbLang = "";
        try {
            con = dataSource.getConnection();

            if (incfb.getEmpid() != null && !incfb.getEmpid().equals("")) {

                nb.setNottype("INCREMENT");
                nb.setEmpId(incfb.getEmpid());
                nb.setDateofEntry(new Date());
                nb.setOrdno(incfb.getTxtSanctionOrderNo());
                nb.setOrdDate(sdf.parse(incfb.getTxtSanctionOrderDt()));
                nb.setSancDeptCode(incfb.getDeptCode());
                nb.setSancOffCode(incfb.getHidOffCode());
                nb.setRadpostingauthtype(incfb.getRadsancauthtype());
                if (incfb.getRadsancauthtype() != null && incfb.getRadsancauthtype().equals("GOI")) {
                    nb.setSancAuthCode(incfb.getHidSancAuthorityOthSpc());
                } else {
                    nb.setSancAuthCode(incfb.getHidSpc());
                }
                nb.setEntryDeptCode(entdept);
                nb.setEntryOffCode(entoff);
                nb.setEntryAuthCode(entauth);
                nb.setNote(incfb.getTxtIncrNote());
                nb.setLoginuserid(loginuserid);
                nb.setNotid(incfb.getHnotid());
                nb.setRefcorrectionid(sbcorrectionid);
                int notid = incfb.getHnotid();
                notificationDao.deleteNotificationDataSBCorrection(notid, nb.getNottype());
                notificationDao.insertNotificationDataSBCorrection(nb);

                int incrid = incfb.getHidIncrId();
                pst = con.prepareStatement("DELETE FROM emp_incr_log WHERE incrid=?");
                pst.setInt(1, incrid);
                pst.executeUpdate();

                pst = con.prepareStatement("insert into emp_incr_log (not_id, not_type, emp_id, incr, first_incr, second_incr, third_incr, fourth_incr, incr_type,incrid,REF_CORRECTION_ID) values(?,?,?,?,?,?,?,?,?,?,?)");
                pst.setInt(1, notid);
                pst.setString(2, "INCREMENT");
                pst.setString(3, incfb.getEmpid());
                if (incfb.getTxtIncrAmt() != null && !incfb.getTxtIncrAmt().equalsIgnoreCase("")) {
                    pst.setDouble(4, Double.parseDouble(incfb.getTxtIncrAmt()));
                } else {
                    pst.setDouble(4, 0);
                }
                if (incfb.getIncrementLvl() != null && incfb.getIncrementLvl().equals("1")) {
                    pst.setString(5, incfb.getIncrementLvl());
                } else {
                    pst.setString(5, "");
                }
                if (incfb.getIncrementLvl() != null && incfb.getIncrementLvl().equals("2")) {
                    pst.setString(6, incfb.getIncrementLvl());
                } else {
                    pst.setString(6, "");
                }
                if (incfb.getIncrementLvl() != null && incfb.getIncrementLvl().equals("3")) {
                    pst.setString(7, incfb.getIncrementLvl());
                } else {
                    pst.setString(7, "");
                }
                if (incfb.getIncrementLvl() != null && incfb.getIncrementLvl().equals("4")) {
                    pst.setString(8, incfb.getIncrementLvl());
                } else {
                    pst.setString(8, "");
                }
                if (incfb.getIncrementType() != null && !incfb.getIncrementType().equals("")) {
                    pst.setString(9, incfb.getIncrementType());
                } else {
                    pst.setString(9, "");
                }
                pst.setInt(10, incrid);
                pst.setInt(11, sbcorrectionid);
                pst.executeUpdate();

                String sqlString = ToStringBuilder.reflectionToString(pst);
                //sbDAO.createTransactionLog(loginuserid, incfb.getEmpid(), "INCREMENT", sqlString);

                pst = con.prepareStatement("UPDATE emp_incr_log SET query_string=? where incrid=? and EMP_ID=?");
                pst.setString(1, sqlString);
                pst.setInt(2, incrid);
                pst.setString(3, incfb.getEmpid());
                pst.executeUpdate();

                epayrecordform.setEmpid(incfb.getEmpid());
                epayrecordform.setNot_id(notid);
                epayrecordform.setNot_type("INCREMENT");
                epayrecordform.setPayscale(incfb.getSltPayScale());
                if (incfb.getContRemAmount() != null && !incfb.getContRemAmount().equals("")) {
                    epayrecordform.setBasic((incfb.getContRemAmount()));
                } else {
                    epayrecordform.setBasic(incfb.getTxtNewBasic());
                }
                if (incfb.getRdoPaycomm() != null && incfb.getRdoPaycomm().equals("REM")) {
                    epayrecordform.setIfRemuneration("Y");
                } else {
                    epayrecordform.setIfRemuneration("N");
                }
                //epayrecordform.setBasic(incfb.getTxtNewBasic());
                epayrecordform.setGp(incfb.getTxtGradePay());
                epayrecordform.setS_pay(incfb.getTxtSPay());
                epayrecordform.setP_pay(incfb.getTxtP_pay());
                epayrecordform.setOth_pay(incfb.getTxtOthPay());
                epayrecordform.setOth_desc(incfb.getTxtDescOth());
                epayrecordform.setWefDt(incfb.getTxtWEFDt());
                epayrecordform.setWefTime(incfb.getTxtWEFTime());
                epayrecordform.setPayLevel(incfb.getPayLevel());
                epayrecordform.setPayCell(incfb.getPayCell());
                epayrecordform.setPayid(incfb.getHidPayId());
                epayrecordform.setRefcorrectionid(sbcorrectionid);
                emppayrecordDAO.deleteEmpPayRecordDataSBCorrection(epayrecordform);
                emppayrecordDAO.saveEmpPayRecordDataSBCorrection(epayrecordform);

                if (incfb.getChkNotSBPrint() != null && incfb.getChkNotSBPrint().equals("Y")) {
                    pst = con.prepareStatement("UPDATE EMP_NOTIFICATION_LOG SET IF_VISIBLE='N' WHERE NOT_ID=?");
                    pst.setInt(1, notid);
                    pst.execute();
                } else {
                    sbLang = sbDAO.getIncrementSanctionDetails(incfb, notid, payComm);
                    pst = con.prepareStatement("UPDATE EMP_NOTIFICATION_LOG SET SB_DESCRIPTION=?,IF_VISIBLE='Y' WHERE NOT_ID=?");
                    pst.setString(1, sbLang);
                    pst.setInt(2, notid);
                    pst.execute();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return sbLang;
    }

    @Override
    public IncrementForm getEmpIncRowDataSBCorrection(String empId, int incid, int correctionid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        IncrementForm incfb = new IncrementForm();
        String incrLvl = "";
        try {
            con = repodataSource.getConnection();

            String sql = "SELECT * FROM (SELECT E.*,F.INCRID,F.PRID,F.INCR,F.FIRST_INCR,F.second_incr,F.third_incr,F.fourth_incr,\n"
                    + "F.incr_type,G_SPC.SPN FROM EMP_NOTIFICATION_LOG E INNER JOIN EMP_INCR_LOG F ON E.NOT_ID=F.NOT_ID LEFT OUTER JOIN G_SPC ON E.AUTH=G_SPC.SPC\n"
                    + "WHERE E.NOT_TYPE='INCREMENT' AND E.EMP_ID=? AND F.INCRID=? AND F.REF_CORRECTION_ID=? AND F.PRID IS NULL) T \n"
                    + "LEFT OUTER JOIN \n"
                    + "(SELECT * FROM EMP_PAY_RECORD_LOG WHERE NOT_TYPE='INCREMENT' AND EMP_ID=?) R ON \n"
                    + "T.NOT_ID=R.NOT_ID";
            pst = con.prepareStatement(sql);
            pst.setString(1, empId);
            pst.setInt(2, incid);
            pst.setInt(3, correctionid);
            pst.setString(4, empId);
            rs = pst.executeQuery();
            if (rs.next()) {
                incfb.setHidIncrId(incid);
                incfb.setHnotid(Integer.parseInt(rs.getString("not_id")));
                incfb.setHidPayId(rs.getInt("pay_id"));

                incfb.setTxtSanctionOrderNo(rs.getString("ordno"));
                if (rs.getDate("orddt") != null && !rs.getDate("orddt").equals("")) {
                    incfb.setTxtSanctionOrderDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("orddt")));
                }

                incfb.setRadsancauthtype(rs.getString("organization_type"));

                if (rs.getString("dept_code") != null && !rs.getString("dept_code").equals("")) {
                    incfb.setDeptCode(rs.getString("dept_code"));
                }
                if (rs.getString("off_code") != null && !rs.getString("off_code").equals("")) {
                    incfb.setHidOffCode(rs.getString("off_code"));
                }
                if (rs.getString("auth") != null && !rs.getString("auth").equals("")) {
                    incfb.setHidSpc(rs.getString("auth"));
                }
                if (rs.getString("spn") != null && !rs.getString("spn").equals("")) {
                    incfb.setSancAuthPostName(rs.getString("spn"));
                }
                if (rs.getDate("wef") != null && !rs.getDate("wef").equals("")) {
                    incfb.setTxtWEFDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("wef")));
                }
                incfb.setTxtWEFTime(rs.getString("weft"));
                incfb.setSltPayScale(rs.getString("pay_scale"));
                incfb.setPayCell(rs.getString("pay_cell"));
                incfb.setPayLevel(rs.getString("pay_level"));
                incfb.setTxtIncrAmt(rs.getString("incr"));
                if (rs.getString("p_pay") != null && !rs.getString("p_pay").equals("") && rs.getDouble("p_pay") > 1) {
                    incfb.setTxtP_pay(rs.getString("p_pay"));
                }
                if (rs.getString("oth_pay") != null && !rs.getString("oth_pay").equals("") && rs.getDouble("oth_pay") > 1) {
                    incfb.setTxtOthPay(rs.getString("oth_pay"));
                }
                if (rs.getString("s_pay") != null && !rs.getString("s_pay").equals("") && rs.getDouble("s_pay") > 1) {
                    incfb.setTxtSPay(rs.getString("s_pay"));
                }
                if (rs.getString("gp") != null && !rs.getString("gp").equals("") && rs.getDouble("gp") > 1) {
                    incfb.setTxtGradePay(rs.getString("gp"));
                }

                incfb.setTxtNewBasic(rs.getString("pay"));

                incfb.setTxtDescOth(rs.getString("oth_desc"));

                if (rs.getString("first_incr") != null && rs.getString("first_incr").equals("1")) {
                    incrLvl = rs.getString("first_incr");
                } else if (rs.getString("second_incr") != null && rs.getString("second_incr").equals("2")) {
                    incrLvl = rs.getString("second_incr");
                } else if (rs.getString("third_incr") != null && rs.getString("third_incr").equals("3")) {
                    incrLvl = rs.getString("third_incr");
                } else if (rs.getString("fourth_incr") != null && rs.getString("fourth_incr").equals("4")) {
                    incrLvl = rs.getString("fourth_incr");
                }
                incfb.setIncrementLvl(incrLvl);
                incfb.setIncrementType(rs.getString("incr_type"));
                incfb.setTxtIncrNote(rs.getString("note"));

                if (rs.getString("if_visible") != null && rs.getString("if_visible").equals("N")) {
                    incfb.setChkNotSBPrint("Y");
                }

                if (incfb.getRadsancauthtype() != null && incfb.getRadsancauthtype().equals("GOI")) {
                    incfb.setSancAuthPostName(getOtherSpn(rs.getString("auth")));
                    incfb.setHidSancAuthorityOthSpc(rs.getString("auth"));
                } else {
                    incfb.setRadsancauthtype("GOO");
                }
                if (rs.getString("if_remuneration") != null && rs.getString("if_remuneration").equals("Y")) {
                    incfb.setIsRemuneration(rs.getString("if_remuneration"));
                    incfb.setHidremBasic(rs.getString("pay"));
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return incfb;
    }

    @Override
    public void approveIncrementSBCorrection(IncrementForm incrementForm, String entrydeptcode, String entryoffcode, String entryspc, String loginuserid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");

        try {
            con = this.dataSource.getConnection();

            if (incrementForm.getEntrytype() != null && incrementForm.getEntrytype().equals("NEW")) {

                pst = con.prepareStatement("INSERT INTO EMP_NOTIFICATION(NOT_TYPE,EMP_ID,DOE,ORDNO,ORDDT,DEPT_CODE,OFF_CODE,AUTH,IF_VISIBLE,ENT_DEPT,ENT_OFF,ENT_AUTH) "
                        + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
                pst.setString(1, "INCREMENT");
                pst.setString(2, incrementForm.getEmpid());
                pst.setTimestamp(3, new Timestamp(new Date().getTime()));
                pst.setString(4, incrementForm.getTxtSanctionOrderNo());
                if (incrementForm.getTxtSanctionOrderDt() != null) {
                    pst.setTimestamp(5, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(incrementForm.getTxtSanctionOrderDt()).getTime()));
                } else {
                    pst.setTimestamp(5, null);
                }
                pst.setString(6, incrementForm.getDeptCode());
                pst.setString(7, incrementForm.getHidOffCode());
                pst.setString(8, incrementForm.getHidSpc());
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

                pst = con.prepareStatement("insert into emp_incr (not_id, not_type, emp_id, incr, first_incr, second_incr, third_incr, fourth_incr, incr_type) values(?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
                pst.setInt(1, notId);
                pst.setString(2, "INCREMENT");
                pst.setString(3, incrementForm.getEmpid());
                if (incrementForm.getTxtIncrAmt() != null && !incrementForm.getTxtIncrAmt().equalsIgnoreCase("")) {
                    pst.setDouble(4, Double.parseDouble(incrementForm.getTxtIncrAmt()));
                } else {
                    pst.setDouble(4, 0);
                }
                if (incrementForm.getIncrementLvl() != null && incrementForm.getIncrementLvl().equals("1")) {
                    pst.setString(5, incrementForm.getIncrementLvl());
                } else {
                    pst.setString(5, "");
                }
                if (incrementForm.getIncrementLvl() != null && incrementForm.getIncrementLvl().equals("2")) {
                    pst.setString(6, incrementForm.getIncrementLvl());
                } else {
                    pst.setString(6, "");
                }
                if (incrementForm.getIncrementLvl() != null && incrementForm.getIncrementLvl().equals("3")) {
                    pst.setString(7, incrementForm.getIncrementLvl());
                } else {
                    pst.setString(7, "");
                }
                if (incrementForm.getIncrementLvl() != null && incrementForm.getIncrementLvl().equals("4")) {
                    pst.setString(8, incrementForm.getIncrementLvl());
                } else {
                    pst.setString(8, "");
                }
                if (incrementForm.getIncrementType() != null && !incrementForm.getIncrementType().equals("")) {
                    pst.setString(9, incrementForm.getIncrementType());
                } else {
                    pst.setString(9, "");
                }
                pst.executeUpdate();

                DataBaseFunctions.closeSqlObjects(rs, pst);

                String sql = "insert into emp_pay_record (not_type, not_id, emp_id, wef, weft, pay_scale, pay, s_pay, p_pay, oth_pay, oth_desc, gp, pay_level, pay_cell) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                pst = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                pst.setString(1, "INCREMENT");
                pst.setInt(2, notId);
                pst.setString(3, incrementForm.getEmpid());
                if (incrementForm.getTxtWEFDt() != null && !incrementForm.getTxtWEFDt().equals("")) {
                    pst.setTimestamp(4, new Timestamp(sdf.parse(incrementForm.getTxtWEFDt()).getTime()));
                } else {
                    pst.setTimestamp(4, null);
                }
                if (incrementForm.getTxtWEFTime() != null && !incrementForm.getTxtWEFTime().equals("")) {
                    pst.setString(5, incrementForm.getTxtWEFTime());
                } else {
                    pst.setString(5, null);
                }
                if (incrementForm.getTxtNewBasic() == null || incrementForm.getTxtNewBasic().equals("")) {
                    pst.setDouble(7, 0);
                } else {
                    pst.setDouble(7, java.lang.Double.parseDouble(incrementForm.getTxtNewBasic()));
                }
                if (incrementForm.getTxtSPay() == null || incrementForm.getTxtSPay().equalsIgnoreCase("")) {
                    pst.setDouble(8, 0);
                } else {
                    pst.setDouble(8, java.lang.Double.parseDouble(incrementForm.getTxtSPay()));
                }
                if (incrementForm.getTxtP_pay() == null || incrementForm.getTxtP_pay().equalsIgnoreCase("")) {
                    pst.setDouble(9, 0);
                } else {
                    pst.setDouble(9, java.lang.Double.parseDouble(incrementForm.getTxtP_pay()));
                }
                if (incrementForm.getTxtOthPay() == null || incrementForm.getTxtOthPay().equalsIgnoreCase("")) {
                    pst.setDouble(10, 0);
                } else {
                    pst.setDouble(10, java.lang.Double.parseDouble(incrementForm.getTxtOthPay()));
                }
                if (incrementForm.getTxtOthPay() != null && !incrementForm.getTxtOthPay().equals("")) {
                    pst.setString(11, incrementForm.getTxtOthPay().toUpperCase());
                } else {
                    pst.setString(11, null);
                }
                if (incrementForm.getPayLevel() != null && !incrementForm.getPayLevel().equals("")) {
                    pst.setString(6, null);
                    pst.setDouble(12, 0);
                    pst.setString(13, incrementForm.getPayLevel());
                    if (incrementForm.getPayCell() != null && !incrementForm.getPayCell().equals("")) {
                        pst.setString(14, incrementForm.getPayCell());
                    }
                } else if (incrementForm.getSltPayScale() != null && !incrementForm.getSltPayScale().equals("")) {
                    pst.setString(6, incrementForm.getSltPayScale());
                    if (incrementForm.getTxtGradePay() == null || incrementForm.getTxtGradePay().equals("")) {
                        pst.setDouble(12, 0);
                    } else {
                        pst.setDouble(12, java.lang.Double.parseDouble(incrementForm.getTxtGradePay()));
                    }
                    pst.setString(13, null);
                    pst.setString(14, null);
                } else {
                    pst.setString(6, null);
                    pst.setDouble(12, 0);
                    pst.setString(13, null);
                    pst.setString(14, null);
                }
                pst.executeUpdate();

                rs = pst.getGeneratedKeys();
                rs.next();
                int payid = rs.getInt("pay_id");

                DataBaseFunctions.closeSqlObjects(rs, pst);

                sqlString = ToStringBuilder.reflectionToString(pst);
                pst = con.prepareStatement("UPDATE emp_pay_record SET query_string=? where PAY_ID=? and EMP_ID=?");
                pst.setString(1, sqlString);
                pst.setInt(2, payid);
                pst.setString(3, incrementForm.getEmpid());
                pst.executeUpdate();

                DataBaseFunctions.closeSqlObjects(rs, pst);

                String sbLang = sbDAO.getIncrementSanctionDetails(incrementForm, notId, incrementForm.getRdoPaycomm());
                pst = con.prepareStatement("UPDATE EMP_NOTIFICATION SET SB_DESCRIPTION=?,IF_VISIBLE='Y' WHERE NOT_ID=?");
                pst.setString(1, sbLang);
                pst.setInt(2, notId);
                pst.execute();
            } else {
                NotificationBean nb = new NotificationBean();

                nb.setNottype("INCREMENT");
                nb.setEmpId(incrementForm.getEmpid());
                nb.setNotid(incrementForm.getHnotid());
                nb.setOrdno(incrementForm.getTxtSanctionOrderNo());
                nb.setOrdDate(sdf.parse(incrementForm.getTxtSanctionOrderDt()));
                nb.setSancDeptCode(incrementForm.getDeptCode());
                nb.setSancOffCode(incrementForm.getHidOffCode());
                nb.setRadpostingauthtype(incrementForm.getRadsancauthtype());
                if (incrementForm.getRadsancauthtype() != null && incrementForm.getRadsancauthtype().equals("GOI")) {
                    nb.setSancAuthCode(incrementForm.getHidSancAuthorityOthSpc());
                } else {
                    nb.setSancAuthCode(incrementForm.getHidSpc());
                }
                nb.setEntryDeptCode(entrydeptcode);
                nb.setEntryOffCode(entryoffcode);
                nb.setEntryAuthCode(entryspc);
                nb.setNote(incrementForm.getTxtIncrNote());
                nb.setLoginuserid(loginuserid);
                notificationDao.modifyNotificationDataSBCorrection(nb);

                pst = con.prepareStatement("update emp_incr_log set incr=?,first_incr=?,second_incr=?,third_incr=?,fourth_incr=?,incr_type=? where emp_id=? and incrid=?");

                if (incrementForm.getTxtIncrAmt() != null && !incrementForm.getTxtIncrAmt().equalsIgnoreCase("")) {
                    pst.setDouble(1, java.lang.Double.parseDouble(incrementForm.getTxtIncrAmt()));
                } else {
                    pst.setDouble(1, 0);
                }
                if (incrementForm.getIncrementLvl() != null && incrementForm.getIncrementLvl().equals("1")) {
                    pst.setString(2, incrementForm.getIncrementLvl());
                } else {
                    pst.setString(2, "N");
                }
                if (incrementForm.getIncrementLvl() != null && incrementForm.getIncrementLvl().equals("2")) {
                    pst.setString(3, incrementForm.getIncrementLvl());
                } else {
                    pst.setString(3, "N");
                }
                if (incrementForm.getIncrementLvl() != null && incrementForm.getIncrementLvl().equals("3")) {
                    pst.setString(4, incrementForm.getIncrementLvl());
                } else {
                    pst.setString(4, "N");
                }
                if (incrementForm.getIncrementLvl() != null && incrementForm.getIncrementLvl().equals("4")) {
                    pst.setString(5, incrementForm.getIncrementLvl());
                } else {
                    pst.setString(5, "N");
                }
                if (incrementForm.getIncrementType() != null && !incrementForm.getIncrementType().equals("")) {
                    pst.setString(6, incrementForm.getIncrementType());
                } else {
                    pst.setString(6, "N");
                }
                pst.setString(7, incrementForm.getEmpid());
                pst.setInt(8, incrementForm.getHidIncrId());
                pst.executeUpdate();

                EmpPayRecordForm epayrecordform = new EmpPayRecordForm();
                epayrecordform.setEmpid(incrementForm.getEmpid());
                epayrecordform.setNot_id(incrementForm.getHnotid());
                epayrecordform.setPayid(incrementForm.getHidPayId());
                if (incrementForm.getContRemAmount() != null && !incrementForm.getContRemAmount().equals("")) {
                    epayrecordform.setBasic((incrementForm.getContRemAmount()));
                } else {
                    epayrecordform.setBasic(incrementForm.getTxtNewBasic());
                }
                if (incrementForm.getRdoPaycomm() != null && incrementForm.getRdoPaycomm().equals("6")) {
                    epayrecordform.setPayscale(incrementForm.getSltPayScale());
                    epayrecordform.setGp(incrementForm.getTxtGradePay());
                    epayrecordform.setPayCell(null);
                    epayrecordform.setPayLevel(null);

                } else if (incrementForm.getRdoPaycomm() != null && incrementForm.getRdoPaycomm().equals("7")) {
                    epayrecordform.setPayscale(null);
                    epayrecordform.setGp("0");
                    epayrecordform.setPayCell(incrementForm.getPayCell());
                    epayrecordform.setPayLevel(incrementForm.getPayLevel());
                } else if (incrementForm.getRdoPaycomm() != null && incrementForm.getRdoPaycomm().equals("REM")) {
                    epayrecordform.setIfRemuneration("Y");
                    epayrecordform.setPayscale(incrementForm.getSltPayScale());
                    epayrecordform.setGp(incrementForm.getTxtGradePay());
                    epayrecordform.setPayCell(incrementForm.getPayCell());
                    epayrecordform.setPayLevel(incrementForm.getPayLevel());
                } else {
                    epayrecordform.setIfRemuneration("N");
                }
                epayrecordform.setS_pay(incrementForm.getTxtSPay());
                epayrecordform.setP_pay(incrementForm.getTxtP_pay());
                epayrecordform.setOth_pay(incrementForm.getTxtOthPay());
                epayrecordform.setOth_desc(incrementForm.getTxtDescOth());
                epayrecordform.setWefDt(incrementForm.getTxtWEFDt());
                epayrecordform.setWefTime(incrementForm.getTxtWEFTime());
                emppayrecordDAO.updateEmpPayRecordDataSBCorrection(epayrecordform);

                String sql = "UPDATE emp_incr"
                        + " SET incr = emp_incr_log.incr,first_incr=emp_incr_log.first_incr,second_incr=emp_incr_log.second_incr,third_incr=emp_incr_log.third_incr,fourth_incr=emp_incr_log.fourth_incr,incr_type=emp_incr_log.incr_type"
                        + " FROM emp_incr_log"
                        + " WHERE emp_incr.not_id = emp_incr_log.not_id and emp_incr_log.not_id=?";
                pst = con.prepareStatement(sql);
                pst.setInt(1, incrementForm.getHnotid());
                pst.executeUpdate();

                DataBaseFunctions.closeSqlObjects(pst);

                sql = "UPDATE emp_notification"
                        + " SET ordno = emp_notification_log.ordno,orddt=emp_notification_log.orddt,dept_code=emp_notification_log.dept_code,off_code=emp_notification_log.off_code,auth=emp_notification_log.auth,organization_type=emp_notification_log.organization_type,organization_type_posting=emp_notification_log.organization_type_posting"
                        + " FROM emp_notification_log"
                        + " WHERE emp_notification.not_id = emp_notification_log.not_id and emp_notification_log.not_id=?";
                pst = con.prepareStatement(sql);
                pst.setInt(1, incrementForm.getHnotid());
                pst.executeUpdate();

                DataBaseFunctions.closeSqlObjects(pst);

                sql = "UPDATE emp_pay_record"
                        + " SET wef = emp_pay_record_log.wef,weft=emp_pay_record_log.weft,pay_scale=emp_pay_record_log.pay_scale,pay=emp_pay_record_log.pay,s_pay=emp_pay_record_log.s_pay,p_pay=emp_pay_record_log.p_pay,oth_pay=emp_pay_record_log.oth_pay,oth_desc=emp_pay_record_log.oth_desc,gp=emp_pay_record_log.gp"
                        + " FROM emp_pay_record_log"
                        + " WHERE emp_pay_record.not_id = emp_pay_record_log.not_id and emp_pay_record_log.not_id=?";
                pst = con.prepareStatement(sql);
                pst.setInt(1, incrementForm.getHnotid());
                pst.executeUpdate();

                String sbLang = sbDAO.getIncrementSanctionDetails(incrementForm, incrementForm.getHnotid(), incrementForm.getRdoPaycomm());
                pst = con.prepareStatement("UPDATE EMP_NOTIFICATION SET SB_DESCRIPTION=?,IF_VISIBLE='Y' WHERE NOT_ID=?");
                pst.setString(1, sbLang);
                pst.setInt(2, incrementForm.getHnotid());
                pst.execute();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public String getEmployeeMaxIncrementDate(String empid) {
        PreparedStatement ps = null;
        Connection con = null;
        ResultSet rs = null;
        String maxDate = null;
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("select to_char(max(wef),'YYYY-MM-DD')wefdate from\n"
                    + "(SELECT E2.not_type canceltype,E2.not_id linkid,E1.is_validated,E1.modified_by,E1.modified_on,E1.IF_VISIBLE,\n"
                    + "E1.SV_ID,E1.NOT_ID,INCRID,INCR,FIRST_INCR,SECOND_INCR,THIRD_INCR,FOURTH_INCR,INCR_TYPE,E1.DOE, E1.ORDNO,\n"
                    + "E1.ORDDT,E1.AUTH,E1.OFF_CODE,E1.DEPT_CODE,WEF,WEFT,pay_scale,s_pay, p_pay,oth_pay,oth_desc,pay,gp, pay_level, pay_cell FROM EMP_INCR\n"
                    + "LEFT OUTER JOIN EMP_NOTIFICATION E1 ON EMP_INCR.NOT_ID=E1.NOT_ID\n"
                    + "LEFT OUTER JOIN EMP_NOTIFICATION E2 ON E1.LINK_ID=E2.NOT_ID\n"
                    + "LEFT OUTER JOIN EMP_PAY_RECORD ON EMP_PAY_RECORD.NOT_ID=E1.NOT_ID\n"
                    + "WHERE EMP_INCR.NOT_TYPE='INCREMENT' AND EMP_INCR.EMP_ID=? AND PRID IS NULL ORDER BY WEF DESC)a");
            ps.setString(1, empid);
            rs = ps.executeQuery();
            if (rs.next()) {
                maxDate = rs.getString("wefdate");
            }

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return maxDate;
    }
}
