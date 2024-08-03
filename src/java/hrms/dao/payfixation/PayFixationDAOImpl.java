package hrms.dao.payfixation;

import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.dao.emppayrecord.EmpPayRecordDAO;
import hrms.dao.incrementsanction.IncrementSanctionDAO;
import hrms.dao.leaveAccount.ServiceBookLanguageDAO;
import hrms.dao.notification.NotificationDAO;
import hrms.model.emppayrecord.EmpPayRecordForm;
import hrms.model.notification.NotificationBean;
import hrms.model.payfixation.PayFixation;
import hrms.model.payfixation.PayFixationList;
import hrms.model.payfixation.RetrospectiveIncrement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.apache.commons.lang.builder.ToStringBuilder;

public class PayFixationDAOImpl implements PayFixationDAO {

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

    protected EmpPayRecordDAO emppayrecordDAO;

    public void setEmppayrecordDAO(EmpPayRecordDAO emppayrecordDAO) {
        this.emppayrecordDAO = emppayrecordDAO;
    }

    protected NotificationDAO notificationDao;

    public void setNotificationDao(NotificationDAO notificationDao) {
        this.notificationDao = notificationDao;
    }

    protected ServiceBookLanguageDAO sbDAO;

    public void setSbDAO(ServiceBookLanguageDAO sbDAO) {
        this.sbDAO = sbDAO;
    }

    @Override
    public void insertPayFixationData(PayFixation payfix, int notId) {

        Connection con = null;

        PreparedStatement pst = null;
        PreparedStatement pstIncr = null;

        EmpPayRecordForm epayrecordform = new EmpPayRecordForm();
        try {
            con = this.dataSource.getConnection();

            String sql = "insert into emp_pay ( NOT_ID, NOT_TYPE, EMP_ID, DONI) values (?,?,?,?)";
            pst = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pst.setInt(1, notId);
            pst.setString(2, payfix.getNotType());
            pst.setString(3, payfix.getEmpid());
            if (payfix.getTxtNextIncrementDt() != null && !payfix.getTxtNextIncrementDt().equals("")) {
                pst.setTimestamp(4, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(payfix.getTxtNextIncrementDt()).getTime()));
            } else {
                pst.setTimestamp(4, null);
            }
            pst.executeUpdate();
            ResultSet rs = pst.getGeneratedKeys();
            rs.next();
            int maxPrid = rs.getInt("PRID");

            epayrecordform.setEmpid(payfix.getEmpid());
            epayrecordform.setNot_id(notId);
            epayrecordform.setNot_type(payfix.getNotType());
            epayrecordform.setPayscale(payfix.getSltPayScale());
            epayrecordform.setBasic(payfix.getTxtBasic());
            epayrecordform.setGp(payfix.getTxtGP());
            epayrecordform.setS_pay(payfix.getTxtSP());
            epayrecordform.setP_pay(payfix.getTxtPP());
            epayrecordform.setOth_pay(payfix.getTxtOP());
            epayrecordform.setOth_desc(payfix.getTxtDescOP());
            epayrecordform.setWefDt(payfix.getTxtWEFDt());
            epayrecordform.setWefTime(payfix.getSltWEFTime());
            epayrecordform.setReasonpayfixation(payfix.getSltPayFixationReason());
            epayrecordform.setPayLevel(payfix.getPayLevel());
            epayrecordform.setPayCell(payfix.getPayCell());
            if (payfix.getRdoPaycomm() != null && (payfix.getRdoPaycomm().equals("6") || payfix.getRdoPaycomm().equals("5"))) {
                epayrecordform.setPayCell(null);
                epayrecordform.setPayLevel(null);
            } else if (payfix.getRdoPaycomm() != null && payfix.getRdoPaycomm().equals("7")) {
                epayrecordform.setPayscale(null);
                epayrecordform.setGp(null);
            }
            emppayrecordDAO.saveEmpPayRecordData(epayrecordform);

            DataBaseFunctions.closeSqlObjects(pst);
            if (payfix.getRdTransaction().equalsIgnoreCase("C")) {
                sql = "UPDATE EMP_MAST SET CUR_SALARY=?,CUR_BASIC_SALARY=?,DATE_OF_NINCR=?,GP=?,SPAY=?,PPAY=?,OTHPAY=?,PAY_DATE=?,matrix_level=?, matrix_cell=? WHERE EMP_ID=?";
                pst = con.prepareStatement(sql);
                pst.setString(1, payfix.getSltPayScale());
                if (payfix.getTxtBasic() != null && !payfix.getTxtBasic().equals("")) {
                    pst.setDouble(2, Double.parseDouble(payfix.getTxtBasic()));
                } else {
                    pst.setDouble(2, 0);
                }
                if (payfix.getTxtNextIncrementDt() != null && !payfix.getTxtNextIncrementDt().equals("")) {
                    pst.setTimestamp(3, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(payfix.getTxtNextIncrementDt()).getTime()));
                } else {
                    pst.setTimestamp(3, null);
                }
                if (payfix.getTxtGP() != null && !payfix.getTxtGP().equals("")) {
                    pst.setDouble(4, Double.parseDouble(payfix.getTxtGP()));
                } else {
                    pst.setDouble(4, 0);
                }
                if (payfix.getTxtSP() != null && !payfix.getTxtSP().equals("")) {
                    pst.setInt(5, Integer.parseInt(payfix.getTxtSP()));
                } else {
                    pst.setInt(5, 0);
                }
                if (payfix.getTxtPP() != null && !payfix.getTxtPP().equals("")) {
                    pst.setInt(6, Integer.parseInt(payfix.getTxtPP()));
                } else {
                    pst.setInt(6, 0);
                }
                if (payfix.getTxtOP() != null && !payfix.getTxtOP().equals("")) {
                    pst.setInt(7, Integer.parseInt(payfix.getTxtOP()));
                } else {
                    pst.setInt(7, 0);
                }
                if (payfix.getTxtWEFDt() != null && !payfix.getTxtWEFDt().equals("")) {
                    pst.setTimestamp(8, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(payfix.getTxtWEFDt()).getTime()));
                } else {
                    pst.setTimestamp(8, null);
                }

                pst.setString(9, payfix.getPayLevel());

                pst.setString(10, payfix.getPayCell());

                pst.setString(11, payfix.getEmpid());
                pst.executeUpdate();
            }
            /*
             pst=con.prepareStatement("insert into emp_incr (incrid, prid, not_id, not_type, emp_id, incr, first_incr, second_incr, third_incr, fourth_incr, incr_type) values(?,?,?,?,?,?,?,?,?,?,?)");
             PreparedStatement pstNotification=con.prepareStatement("insert into emp_notification (not_id,not_type,emp_id,doe,ordno,orddt,dept_code,off_code,auth,if_assumed,toe,ent_dept,ent_off,ent_auth) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
             PreparedStatement pstPayRecord=con.prepareStatement("insert into emp_pay_record (pay_id, not_type, not_id, emp_id, wef, weft, pay_scale, pay, s_pay, p_pay, oth_pay, oth_desc) values(?,?,?,?,?,?,?,?,?,?,?,?)");
             */
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");

            pstIncr = con.prepareStatement("insert into emp_incr ( prid, not_id, not_type, emp_id, incr) values(?,?,?,?,?)");

            if (payfix.getRetroIncrement() != null) {
                for (int i = 0; i < payfix.getRetroIncrement().size(); i++) {
                    RetrospectiveIncrement rtoIncr = (RetrospectiveIncrement) payfix.getRetroIncrement().get(i);

                    /**
                     * INSERTING VALUES TO EMP_NOTIFICATION
                     */
                    NotificationBean nb = new NotificationBean();
                    nb.setNottype("INCREMENT");
                    nb.setEmpId(payfix.getEmpid());
                    nb.setDateofEntry(new Date());
                    nb.setOrdno(payfix.getTxtNotOrdNo());
                    nb.setOrdDate(sdf.parse(payfix.getTxtNotOrdDt()));
                    nb.setSancDeptCode(payfix.getHidNotifyingDeptCode());
                    nb.setSancOffCode(payfix.getHidNotifyingOffCode());
                    nb.setSancAuthCode(payfix.getNotifyingSpc());
                    //nb.setNote(payfix.getNote());
                    int maxnotid = notificationDao.insertNotificationData(nb);

                    /**
                     * INSERTING VALUES TO EMP_INCREMENT
                     */
                    System.out.println("maxPrid is: " + maxPrid);
                    pstIncr.setInt(1, maxPrid);
                    pstIncr.setInt(2, maxnotid);
                    pstIncr.setString(3, "INCREMENT");
                    pstIncr.setString(4, payfix.getEmpid());
                    if (rtoIncr.getIncrAmount() > 0) {
                        pstIncr.setDouble(5, rtoIncr.getIncrAmount());
                    } else {
                        pstIncr.setDouble(5, 0);
                    }
                    pstIncr.executeUpdate();

                    // Insert data into emp_pay_record
                    epayrecordform = new EmpPayRecordForm();
                    epayrecordform.setEmpid(payfix.getEmpid());
                    epayrecordform.setNot_id(maxnotid);
                    epayrecordform.setNot_type("INCREMENT");
                    epayrecordform.setPayscale(rtoIncr.getSltPayScale());
                    epayrecordform.setGp(rtoIncr.getTxtGP());
                    epayrecordform.setBasic(rtoIncr.getNewBasic() + "");

                    epayrecordform.setS_pay(rtoIncr.getSpecialPay());
                    epayrecordform.setP_pay(rtoIncr.getPersonalPay());
                    epayrecordform.setOth_pay(rtoIncr.getOtherPay());
                    epayrecordform.setOth_desc(rtoIncr.getDescOtherPay());
                    epayrecordform.setWefDt(rtoIncr.getWefDate());
                    epayrecordform.setWefTime(payfix.getSltWEFTime());

                    epayrecordform.setPayLevel(rtoIncr.getPayLevel());
                    epayrecordform.setPayCell(rtoIncr.getPayCell());

                    emppayrecordDAO.saveEmpPayRecordData(epayrecordform);

                }
            }
            /*
             * Updating the Service Book Language
             */
            if (payfix.getChkNotSBPrint() != null && payfix.getChkNotSBPrint().equals("Y")) {
                pst = con.prepareStatement("UPDATE EMP_NOTIFICATION SET IF_VISIBLE='N' WHERE NOT_ID=?");
                pst.setInt(1, notId);
                pst.execute();
            } else if (payfix.getChkNotSBPrint() == null || payfix.getChkNotSBPrint().equals("")) {
                String sbLang = sbDAO.getPayFixationDetails(payfix, notId, payfix.getNotType());

                System.out.println(" insert  == hhhhh" + sbLang);
                pst = con.prepareStatement("UPDATE EMP_NOTIFICATION SET SB_DESCRIPTION=?,IF_VISIBLE='Y' WHERE NOT_ID=?");
                pst.setString(1, sbLang);
                pst.setInt(2, notId);
                pst.execute();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void updatePayFixationData(PayFixation payfix) {

        Connection con = null;

        PreparedStatement pst = null;
        PreparedStatement pstIncr = null;
        PreparedStatement pstUpIncr = null;
        int maxPrid = 0;
        EmpPayRecordForm epayrecordform = new EmpPayRecordForm();
        String sql = "";
        try {
            con = this.dataSource.getConnection();
            maxPrid = Integer.parseInt(payfix.getPayid());
            if (payfix.getTxtNextIncrementDt() != null && !payfix.getTxtNextIncrementDt().equals("")) {
                sql = "update emp_pay set DONI = ? where PRID=? and emp_id=?";
                pst = con.prepareStatement(sql);
                pst.setTimestamp(1, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(payfix.getTxtNextIncrementDt()).getTime()));
                pst.setInt(2, Integer.parseInt(payfix.getPayid()));
                pst.setString(3, payfix.getEmpid());
                pst.executeUpdate();
            }
            epayrecordform.setNot_id(payfix.getNotId());
            epayrecordform.setEmpid(payfix.getEmpid());
            epayrecordform.setPayid(payfix.getPayRecordId());
            epayrecordform.setPayscale(payfix.getSltPayScale());
            epayrecordform.setPayCell(payfix.getPayCell());
            epayrecordform.setPayLevel(payfix.getPayLevel());
            epayrecordform.setBasic(payfix.getTxtBasic());
            epayrecordform.setGp(payfix.getTxtGP());
            epayrecordform.setS_pay(payfix.getTxtSP());
            epayrecordform.setP_pay(payfix.getTxtPP());
            epayrecordform.setOth_pay(payfix.getTxtOP());
            epayrecordform.setOth_desc(payfix.getTxtDescOP());
            epayrecordform.setWefDt(payfix.getTxtWEFDt());
            epayrecordform.setWefTime(payfix.getSltWEFTime());
            epayrecordform.setReasonpayfixation(payfix.getSltPayFixationReason());
            if (payfix.getRdoPaycomm() != null && (payfix.getRdoPaycomm().equals("6") || payfix.getRdoPaycomm().equals("5"))) {
                epayrecordform.setPayCell(null);
                epayrecordform.setPayLevel(null);
            } else if (payfix.getRdoPaycomm() != null && payfix.getRdoPaycomm().equals("7")) {
                epayrecordform.setPayscale(null);
                epayrecordform.setGp(null);
            }
            emppayrecordDAO.updateEmpPayRecordData(epayrecordform);

            DataBaseFunctions.closeSqlObjects(pst);

            if (payfix.getRdTransaction().equalsIgnoreCase("C")) {
                sql = "UPDATE EMP_MAST SET CUR_SALARY=?,CUR_BASIC_SALARY=?,DATE_OF_NINCR=?,GP=?,SPAY=?,PPAY=?,OTHPAY=?,PAY_DATE=?,matrix_level=?, matrix_cell=? WHERE EMP_ID=?";
                pst = con.prepareStatement(sql);
                pst.setString(1, payfix.getSltPayScale());
                if (payfix.getTxtBasic() != null && !payfix.getTxtBasic().equals("")) {
                    pst.setDouble(2, Double.parseDouble(payfix.getTxtBasic()));
                } else {
                    pst.setDouble(2, 0);
                }
                if (payfix.getTxtNextIncrementDt() != null && !payfix.getTxtNextIncrementDt().equals("")) {
                    pst.setTimestamp(3, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(payfix.getTxtNextIncrementDt()).getTime()));
                } else {
                    pst.setTimestamp(3, null);
                }
                if (payfix.getTxtGP() != null && !payfix.getTxtGP().equals("")) {
                    pst.setDouble(4, Double.parseDouble(payfix.getTxtGP()));
                } else {
                    pst.setDouble(4, 0);
                }
                if (payfix.getTxtSP() != null && !payfix.getTxtSP().equals("")) {
                    pst.setInt(5, Integer.parseInt(payfix.getTxtSP()));
                } else {
                    pst.setInt(5, 0);
                }
                if (payfix.getTxtPP() != null && !payfix.getTxtPP().equals("")) {
                    pst.setInt(6, Integer.parseInt(payfix.getTxtPP()));
                } else {
                    pst.setInt(6, 0);
                }
                if (payfix.getTxtOP() != null && !payfix.getTxtOP().equals("")) {
                    pst.setInt(7, Integer.parseInt(payfix.getTxtOP()));
                } else {
                    pst.setInt(7, 0);
                }
                if (payfix.getTxtWEFDt() != null && !payfix.getTxtWEFDt().equals("")) {
                    pst.setTimestamp(8, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(payfix.getTxtWEFDt()).getTime()));
                } else {
                    pst.setTimestamp(8, null);
                }

                pst.setString(9, payfix.getPayLevel());

                pst.setString(10, payfix.getPayCell());

                pst.setString(11, payfix.getEmpid());
                pst.executeUpdate();
            }

            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");

            if (payfix.getRetroIncrement() != null) {

                pstIncr = con.prepareStatement("insert into emp_incr ( prid, not_id, not_type, emp_id, incr) values(?,?,?,?,?)");
                pstUpIncr = con.prepareStatement("update emp_incr set prid=?, not_id=?, not_type=?, emp_id=?, incr=? where incrid=?");

                for (int i = 0; i < payfix.getRetroIncrement().size(); i++) {
                    RetrospectiveIncrement rtoIncr = (RetrospectiveIncrement) payfix.getRetroIncrement().get(i);

                    if (rtoIncr.getIncrId() < 1) {

                        /**
                         * INSERTING VALUES TO EMP_NOTIFICATION
                         */
                        NotificationBean nb = new NotificationBean();
                        nb.setNottype("INCREMENT");
                        nb.setEmpId(payfix.getEmpid());
                        nb.setDateofEntry(new Date());
                        nb.setOrdno(payfix.getTxtNotOrdNo());
                        nb.setOrdDate(sdf.parse(payfix.getTxtNotOrdDt()));
                        nb.setSancDeptCode(payfix.getHidNotifyingDeptCode());
                        nb.setSancOffCode(payfix.getHidNotifyingOffCode());
                        nb.setSancAuthCode(payfix.getNotifyingSpc());
                        //nb.setNote(payfix.getNote());
                        int maxnotid = notificationDao.insertNotificationData(nb);

                        /**
                         * INSERTING VALUES TO EMP_INCREMENT
                         */
                        pstIncr.setInt(1, maxPrid);
                        pstIncr.setInt(2, maxnotid);
                        pstIncr.setString(3, "INCREMENT");
                        pstIncr.setString(4, payfix.getEmpid());
                        if (rtoIncr.getIncrAmount() > 0) {
                            pstIncr.setDouble(5, rtoIncr.getIncrAmount());
                        } else {
                            pstIncr.setDouble(5, 0);
                        }
                        pstIncr.executeUpdate();

                        // Insert data into emp_pay_record
                        epayrecordform = new EmpPayRecordForm();
                        epayrecordform.setEmpid(payfix.getEmpid());
                        epayrecordform.setNot_id(maxnotid);
                        epayrecordform.setNot_type("INCREMENT");
                        epayrecordform.setPayscale(rtoIncr.getSltPayScale());
                        epayrecordform.setGp(rtoIncr.getTxtGP());
                        epayrecordform.setBasic(rtoIncr.getNewBasic() + "");
                        epayrecordform.setPayCell(rtoIncr.getPayCell());
                        epayrecordform.setPayLevel(rtoIncr.getPayLevel());
                        epayrecordform.setS_pay(rtoIncr.getSpecialPay());
                        epayrecordform.setP_pay(rtoIncr.getPersonalPay());
                        epayrecordform.setOth_pay(rtoIncr.getOtherPay());
                        epayrecordform.setOth_desc(rtoIncr.getDescOtherPay());
                        epayrecordform.setWefDt(rtoIncr.getWefDate());
                        epayrecordform.setWefTime(payfix.getSltWEFTime());
                        emppayrecordDAO.saveEmpPayRecordData(epayrecordform);
                    } else if (rtoIncr.getIncrId() > 0) {
                        /**
                         * updating VALUES TO EMP_NOTIFICATION
                         */

                        NotificationBean nb = new NotificationBean();
                        nb.setNottype("INCREMENT");
                        nb.setEmpId(payfix.getEmpid());
                        nb.setDateofEntry(new Date());
                        nb.setOrdno(payfix.getTxtNotOrdNo());
                        nb.setOrdDate(sdf.parse(payfix.getTxtNotOrdDt()));
                        nb.setSancDeptCode(payfix.getHidNotifyingDeptCode());
                        nb.setSancOffCode(payfix.getHidNotifyingOffCode());
                        nb.setSancAuthCode(payfix.getNotifyingSpc());
                        nb.setNotid(rtoIncr.getNotId());
                        notificationDao.modifyNotificationData(nb);

                        /**
                         * updating VALUES TO EMP_INCREMENT
                         */
                        pstUpIncr.setInt(1, maxPrid);
                        pstUpIncr.setInt(2, rtoIncr.getNotId());
                        pstUpIncr.setString(3, "INCREMENT");
                        pstUpIncr.setString(4, payfix.getEmpid());
                        if (rtoIncr.getIncrAmount() > 0) {
                            pstUpIncr.setDouble(5, rtoIncr.getIncrAmount());
                        } else {
                            pstUpIncr.setDouble(5, 0);
                        }
                        pstUpIncr.setInt(6, rtoIncr.getIncrId());
                        pstUpIncr.executeUpdate();

                        // update data into emp_pay_record
                        epayrecordform = new EmpPayRecordForm();
                        //epayrecordform.setPayid(rtoIncr.getPayrecorid());
                        epayrecordform.setPayid(rtoIncr.getPrId());
                        epayrecordform.setEmpid(payfix.getEmpid());
                        epayrecordform.setNot_id(rtoIncr.getNotId());
                        epayrecordform.setNot_type("INCREMENT");
                        epayrecordform.setPayscale(rtoIncr.getSltPayScale());
                        epayrecordform.setGp(rtoIncr.getTxtGP());
                        epayrecordform.setBasic(rtoIncr.getNewBasic() + "");
                        epayrecordform.setPayCell(rtoIncr.getPayCell());
                        epayrecordform.setPayLevel(rtoIncr.getPayLevel());
                        epayrecordform.setS_pay(rtoIncr.getSpecialPay());
                        epayrecordform.setP_pay(rtoIncr.getPersonalPay());
                        epayrecordform.setOth_pay(rtoIncr.getOtherPay());
                        epayrecordform.setOth_desc(rtoIncr.getDescOtherPay());
                        epayrecordform.setWefDt(rtoIncr.getWefDate());
                        epayrecordform.setWefTime(payfix.getSltWEFTime());
                        emppayrecordDAO.updateEmpPayRecordData(epayrecordform);
                    }
                }
            }

            if (payfix.getChkNotSBPrint() != null && payfix.getChkNotSBPrint().equals("Y")) {
                pst = con.prepareStatement("UPDATE EMP_NOTIFICATION SET IF_VISIBLE='N' WHERE NOT_ID=?");
                pst.setInt(1, payfix.getNotId());
                pst.execute();
            } else if (payfix.getChkNotSBPrint() == null || payfix.getChkNotSBPrint().equals("")) {
                String sbLang = sbDAO.getPayFixationDetails(payfix, payfix.getNotId(), payfix.getNotType());
                System.out.println(" update == hhhhh" + sbLang);
                pst = con.prepareStatement("UPDATE EMP_NOTIFICATION SET SB_DESCRIPTION=?,IF_VISIBLE='Y' WHERE NOT_ID=? ");
                pst.setString(1, sbLang);
                pst.setInt(2, payfix.getNotId());
                pst.execute();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void deleteRetrospectivePayFixationRow(int notId) {

        Connection con = null;
        PreparedStatement pst = null;
        try {
            con = dataSource.getConnection();

            pst = con.prepareStatement("DELETE FROM EMP_NOTIFICATION WHERE NOT_ID=?");
            pst.setInt(1, notId);
            pst.executeUpdate();

            pst = con.prepareStatement("DELETE FROM emp_incr WHERE NOT_ID=?");
            pst.setInt(1, notId);
            pst.executeUpdate();

            pst = con.prepareStatement("DELETE FROM emp_pay_record WHERE NOT_ID=?");
            pst.setInt(1, notId);
            pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void deletePayFixation(PayFixation payfix) {

        Connection con = null;
        PreparedStatement pst = null;
        try {
            con = dataSource.getConnection();

            if (payfix.getPayid() != null && !payfix.getPayid().equals("")) {
                pst = con.prepareStatement("DELETE FROM EMP_INCR WHERE PRID=? and emp_id=?");
                pst.setInt(1, Integer.parseInt(payfix.getPayid()));
                pst.setString(2, payfix.getEmpid());
                pst.executeUpdate();

                DataBaseFunctions.closeSqlObjects(pst);

                pst = con.prepareStatement("DELETE FROM EMP_PAY WHERE PRID=? and emp_id=?");
                pst.setInt(1, Integer.parseInt(payfix.getPayid()));
                pst.setString(2, payfix.getEmpid());
                pst.executeUpdate();

                DataBaseFunctions.closeSqlObjects(pst);
            }

            pst = con.prepareStatement("DELETE FROM EMP_PAY_RECORD WHERE PAY_ID=? and emp_id=?");
            pst.setInt(1, payfix.getPayRecordId());
            pst.setString(2, payfix.getEmpid());
            pst.executeUpdate();

            DataBaseFunctions.closeSqlObjects(pst);

            pst = con.prepareStatement("DELETE FROM EMP_NOTIFICATION WHERE NOT_ID=? and emp_id=?");
            pst.setInt(1, payfix.getNotId());
            pst.setString(2, payfix.getEmpid());
            pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public PayFixation editPayFixation(String empid, int notId) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        PayFixation payfix = new PayFixation();

        String sql = "";
        try {
            con = this.repodataSource.getConnection();

            payfix.setEmpid(empid);

            if (notId > 0) {
                //sql = "SELECT NOT_ID,NOT_TYPE,ORDNO,EMP_ID,ORDDT,DEPT_CODE,NOTE,OFF_CODE,AUTH FROM EMP_NOTIFICATION WHERE NOT_ID=? AND EMP_ID=?";
                sql = "SELECT organization_type,NOT_ID,NOT_TYPE,ORDNO,EMP_ID,ORDDT,EMP_NOTIFICATION.DEPT_CODE,NOTE,EMP_NOTIFICATION.OFF_CODE,AUTH,DEPARTMENT_NAME,OFF_EN,SPN,note,if_visible FROM"
                        + " (SELECT organization_type,NOT_ID,NOT_TYPE,ORDNO,EMP_ID,ORDDT,DEPT_CODE,NOTE,OFF_CODE,AUTH,if_visible FROM EMP_NOTIFICATION WHERE"
                        + " NOT_ID=? AND EMP_ID=?)EMP_NOTIFICATION"
                        + " LEFT OUTER JOIN G_DEPARTMENT ON EMP_NOTIFICATION.DEPT_CODE=G_DEPARTMENT.DEPARTMENT_CODE"
                        + " LEFT OUTER JOIN G_OFFICE ON EMP_NOTIFICATION.OFF_CODE=G_OFFICE.OFF_CODE"
                        + " LEFT OUTER JOIN G_SPC ON EMP_NOTIFICATION.AUTH=G_SPC.SPC";
                pst = con.prepareStatement(sql);
                pst.setInt(1, notId);
                pst.setString(2, empid);
                rs = pst.executeQuery();
                if (rs.next()) {
                    payfix.setNotId(rs.getInt("NOT_ID"));
                    payfix.setNotType(rs.getString("NOT_TYPE"));
                    payfix.setTxtNotOrdNo(rs.getString("ORDNO"));
                    if (rs.getDate("ORDDT") != null && !rs.getDate("ORDDT").equals("")) {
                        payfix.setTxtNotOrdDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("ORDDT")));
                    }
                    payfix.setRadnotifyingauthtype(rs.getString("organization_type"));
                    payfix.setHidNotifyingDeptCode(rs.getString("DEPT_CODE"));
                    payfix.setHidNotifyingOffCode(rs.getString("OFF_CODE"));
                    if (payfix.getRadnotifyingauthtype() != null && payfix.getRadnotifyingauthtype().equals("GOI")) {
                        payfix.setNotifyingPostName(getOtherSpn(rs.getString("AUTH")));
                        payfix.setHidNotifyingOthSpc(rs.getString("AUTH"));
                    } else {
                        payfix.setRadnotifyingauthtype("GOO");
                        payfix.setNotifyingSpc(rs.getString("AUTH"));
                        payfix.setNotifyingPostName(rs.getString("SPN"));
                    }
                    payfix.setNote(rs.getString("note"));
                    if (rs.getString("if_visible") != null && rs.getString("if_visible").equals("N")) {
                        payfix.setChkNotSBPrint("Y");
                    } else {
                        payfix.setChkNotSBPrint("N");
                    }
                }

                DataBaseFunctions.closeSqlObjects(rs, pst);

                sql = "SELECT prid,doni FROM emp_pay WHERE not_id=? AND EMP_ID=?";
                pst = con.prepareStatement(sql);
                pst.setInt(1, notId);
                pst.setString(2, empid);
                rs = pst.executeQuery();
                if (rs.next()) {
                    payfix.setPayid(rs.getInt("prid") + "");
                    payfix.setTxtNextIncrementDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("doni")));
                }

                DataBaseFunctions.closeSqlObjects(rs, pst);

                sql = "SELECT pay_id,wef,weft,pay_scale,pay,s_pay,p_pay,oth_pay,oth_desc,gp,pay_level, pay_cell,increment_reason  FROM emp_pay_record WHERE not_id=? AND EMP_ID=?";
                pst = con.prepareStatement(sql);
                pst.setInt(1, notId);
                pst.setString(2, empid);
                rs = pst.executeQuery();
                if (rs.next()) {
                    payfix.setPayRecordId(rs.getInt("pay_id"));
                    payfix.setSltPayScale(rs.getString("pay_scale"));
                    payfix.setTxtBasic(rs.getString("pay"));
                    payfix.setTxtGP(rs.getString("gp"));
                    payfix.setTxtPP(rs.getString("p_pay"));
                    payfix.setTxtSP(rs.getString("s_pay"));
                    payfix.setTxtOP(rs.getString("oth_pay"));
                    payfix.setTxtDescOP(rs.getString("oth_desc"));
                    payfix.setTxtWEFDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("wef")));
                    payfix.setSltWEFTime(rs.getString("weft"));
                    payfix.setPayCell(rs.getString("pay_cell"));
                    payfix.setPayLevel(rs.getString("pay_level"));
                    payfix.setSltPayFixationReason(rs.getString("increment_reason"));
                }

                DataBaseFunctions.closeSqlObjects(rs, pst);

                List li = new ArrayList();

                pst = con.prepareStatement("select  emp_incr.not_type, emp_incr.emp_id, emp_incr.incr, incrid, emp_incr.not_id, wef, weft, pay_scale, pay,"
                        + " s_pay, p_pay, oth_pay, oth_desc, gp, pay_level,pay_cell,emp_pay_record.pay_id from emp_incr "
                        + " inner join emp_pay_record on emp_incr.not_id=emp_pay_record.not_id "
                        + " where prid=? and emp_incr.emp_id=? ");
                pst.setInt(1, Integer.parseInt(payfix.getPayid()));
                pst.setString(2, empid);
                rs = pst.executeQuery();
                while (rs.next()) {
                    RetrospectiveIncrement retro = new RetrospectiveIncrement();
                    retro.setIncrId(rs.getInt("incrid"));
                    retro.setPrId(rs.getInt("pay_id"));
                    retro.setNotId(rs.getInt("not_id"));
                    retro.setWefDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("wef")));
                    retro.setNewBasic(rs.getInt("pay"));
                    retro.setIncrAmount(rs.getInt("incr"));
                    retro.setPersonalPay(rs.getString("p_pay"));
                    retro.setSpecialPay(rs.getString("s_pay"));
                    retro.setOtherPay(rs.getString("oth_pay"));
                    retro.setDescOtherPay(rs.getString("oth_desc"));
                    retro.setSltPayScale(rs.getString("pay_scale"));
                    retro.setTxtGP(rs.getString("gp"));
                    retro.setPayLevel(rs.getString("pay_level"));
                    retro.setPayCell(rs.getString("pay_cell"));
                    li.add(retro);
                }
                payfix.setRetroIncrement(li);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return payfix;
    }

    @Override
    public List findAllPayFixation(String empId, String notType) {
        List list = null;
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        PayFixationList payFixList = null;
        list = new ArrayList();
        try {
            con = this.repodataSource.getConnection();
            /*String sql = "select is_validated,prid pr_id,doni done,emp_notification.not_id nid,emp_notification.DOE ndoe,"
             + "emp_notification.link_id lnkid, emp_notification.SV_ID svid,emp_pay_record.wef pwef,emp_pay_record.weft pweft,"
             + "emp_pay_record.pay_scale ppayscale, emp_pay_record.pay ppay, emp_pay_record.gp gradepay from emp_pay "
             + "inner join emp_notification on emp_pay.not_id=emp_notification.not_id "
             + "inner join emp_pay_record on emp_notification.not_id = emp_pay_record.not_id "
             + "where emp_pay.emp_id=? and emp_pay.not_type=? ORDER BY ndoe DESC";*/
            String sql = "select e1.is_validated,prid pr_id,doni done,e1.not_id nid,e1.DOE ndoe,"
                    + " e1.link_id linkid, e1.SV_ID svid, e2.not_type canceltype,emp_pay_record.wef pwef,emp_pay_record.weft pweft,"
                    + " emp_pay_record.pay_scale ppayscale, emp_pay_record.pay ppay, emp_pay_record.gp gradepay from emp_pay"
                    + " inner join emp_notification e1 on emp_pay.not_id=e1.not_id"
                    + " inner join emp_pay_record on e1.not_id = emp_pay_record.not_id"
                    + " left outer join emp_notification e2 on e1.link_id=e2.not_id"
                    + " where emp_pay.emp_id=? and emp_pay.not_type=? ORDER BY ndoe DESC";
            pst = con.prepareStatement(sql);
            pst.setString(1, empId);
            pst.setString(2, notType);
            rs = pst.executeQuery();
            while (rs.next()) {
                payFixList = new PayFixationList();
                payFixList.setDoe(CommonFunctions.getFormattedOutputDate1(rs.getDate("NDOE")));
                payFixList.setNotId(rs.getString("NID"));
                payFixList.setGradePay(rs.getString("gradepay"));
                payFixList.setRevPayScale(rs.getString("ppayscale"));
                payFixList.setCurbasic(rs.getString("ppay"));
                payFixList.setWefDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("pwef")));
                if (rs.getString("pweft") != null && !rs.getString("pweft").equals("")) {
                    if (rs.getString("pweft").equals("FN")) {
                        payFixList.setWefTime("FORE NOON");
                    } else if (rs.getString("pweft").equals("AN")) {
                        payFixList.setWefTime("AFTER NOON");
                    }
                }
                payFixList.setDateOfNextIncrement(CommonFunctions.getFormattedOutputDate1(rs.getDate("done")));
                payFixList.setIsValidated(rs.getString("IS_VALIDATED"));

                payFixList.setCanceltype(rs.getString("canceltype"));
                if (rs.getString("linkid") != null && !rs.getString("linkid").equals("")) {
                    payFixList.setCancelnotid(rs.getString("linkid"));
                }

                list.add(payFixList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return list;
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
    public void insertCancelPayFixationData(NotificationBean nb, int notId, String othspc) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = this.dataSource.getConnection();

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
    public PayFixation editCancelPayFixation(String empid, int notId) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        PayFixation payfix = new PayFixation();

        String sql = "";
        try {
            con = this.repodataSource.getConnection();

            payfix.setEmpid(empid);

            if (notId > 0) {
                //sql = "SELECT NOT_ID,NOT_TYPE,ORDNO,EMP_ID,ORDDT,DEPT_CODE,NOTE,OFF_CODE,AUTH FROM EMP_NOTIFICATION WHERE NOT_ID=? AND EMP_ID=?";
                sql = "SELECT E1.organization_type,E1.NOT_ID not_id,E1.NOT_TYPE,E1.ORDNO,E1.EMP_ID,E1.ORDDT,E1.DEPT_CODE,E1.NOTE,E1.OFF_CODE,E1.AUTH,DEPARTMENT_NAME,OFF_EN,SPN,E1.note,E1.if_visible,E2.NOT_ID LINKID FROM"
                        + " (SELECT organization_type,NOT_ID,NOT_TYPE,ORDNO,EMP_ID,ORDDT,DEPT_CODE,NOTE,OFF_CODE,AUTH,if_visible FROM EMP_NOTIFICATION WHERE"
                        + " NOT_ID=? AND EMP_ID=?)E1"
                        + " INNER JOIN EMP_NOTIFICATION E2 ON E1.NOT_ID=E2.LINK_ID"
                        + " LEFT OUTER JOIN G_DEPARTMENT ON E1.DEPT_CODE=G_DEPARTMENT.DEPARTMENT_CODE"
                        + " LEFT OUTER JOIN G_OFFICE ON E1.OFF_CODE=G_OFFICE.OFF_CODE"
                        + " LEFT OUTER JOIN G_SPC ON E1.AUTH=G_SPC.SPC";
                pst = con.prepareStatement(sql);
                pst.setInt(1, notId);
                pst.setString(2, empid);
                rs = pst.executeQuery();
                if (rs.next()) {
                    payfix.setNotId(rs.getInt("NOT_ID"));
                    payfix.setLinkid(rs.getString("LINKID"));
                    payfix.setNotType(rs.getString("NOT_TYPE"));
                    payfix.setTxtNotOrdNo(rs.getString("ORDNO"));
                    if (rs.getDate("ORDDT") != null && !rs.getDate("ORDDT").equals("")) {
                        payfix.setTxtNotOrdDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("ORDDT")));
                    }
                    payfix.setRadnotifyingauthtype(rs.getString("organization_type"));
                    payfix.setHidNotifyingDeptCode(rs.getString("DEPT_CODE"));
                    payfix.setHidNotifyingOffCode(rs.getString("OFF_CODE"));
                    if (payfix.getRadnotifyingauthtype() != null && payfix.getRadnotifyingauthtype().equals("GOI")) {
                        payfix.setNotifyingPostName(getOtherSpn(rs.getString("AUTH")));
                        payfix.setHidNotifyingOthSpc(rs.getString("AUTH"));
                    } else {
                        payfix.setRadnotifyingauthtype("GOO");
                        payfix.setNotifyingSpc(rs.getString("AUTH"));
                        payfix.setNotifyingPostName(rs.getString("SPN"));
                    }
                    payfix.setNote(rs.getString("note"));
                    if (rs.getString("if_visible") != null && rs.getString("if_visible").equals("N")) {
                        payfix.setChkNotSBPrint("Y");
                    } else {
                        payfix.setChkNotSBPrint("N");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return payfix;
    }

    @Override
    public PayFixation editSupersedePayFixation(String empid, int notId) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        PayFixation payfix = new PayFixation();

        String sql = "";
        try {
            con = this.repodataSource.getConnection();

            payfix.setEmpid(empid);

            if (notId > 0) {
                //sql = "SELECT NOT_ID,NOT_TYPE,ORDNO,EMP_ID,ORDDT,DEPT_CODE,NOTE,OFF_CODE,AUTH FROM EMP_NOTIFICATION WHERE NOT_ID=? AND EMP_ID=?";
                /*sql = "SELECT organization_type,NOT_ID,NOT_TYPE,ORDNO,EMP_ID,ORDDT,EMP_NOTIFICATION.DEPT_CODE,NOTE,EMP_NOTIFICATION.OFF_CODE,AUTH,DEPARTMENT_NAME,OFF_EN,SPN,note,if_visible FROM"
                 + " (SELECT organization_type,NOT_ID,NOT_TYPE,ORDNO,EMP_ID,ORDDT,DEPT_CODE,NOTE,OFF_CODE,AUTH,if_visible FROM EMP_NOTIFICATION WHERE"
                 + " NOT_ID=? AND EMP_ID=?)EMP_NOTIFICATION"
                 + " LEFT OUTER JOIN G_DEPARTMENT ON EMP_NOTIFICATION.DEPT_CODE=G_DEPARTMENT.DEPARTMENT_CODE"
                 + " LEFT OUTER JOIN G_OFFICE ON EMP_NOTIFICATION.OFF_CODE=G_OFFICE.OFF_CODE"
                 + " LEFT OUTER JOIN G_SPC ON EMP_NOTIFICATION.AUTH=G_SPC.SPC";*/
                sql = "SELECT E1.organization_type,E1.NOT_ID,E1.NOT_TYPE,E1.ORDNO,E1.EMP_ID,E1.ORDDT,E1.DEPT_CODE,E1.NOTE,E1.OFF_CODE,E1.AUTH,DEPARTMENT_NAME,OFF_EN,SPN,E1.note,E1.if_visible,E2.NOT_ID LINKID FROM"
                        + " (SELECT organization_type,NOT_ID,NOT_TYPE,ORDNO,EMP_ID,ORDDT,DEPT_CODE,NOTE,OFF_CODE,AUTH,if_visible FROM EMP_NOTIFICATION WHERE"
                        + " NOT_ID=? AND EMP_ID=?)E1"
                        + " INNER JOIN EMP_NOTIFICATION E2 ON E1.NOT_ID=E2.LINK_ID"
                        + " LEFT OUTER JOIN G_DEPARTMENT ON E1.DEPT_CODE=G_DEPARTMENT.DEPARTMENT_CODE"
                        + " LEFT OUTER JOIN G_OFFICE ON E1.OFF_CODE=G_OFFICE.OFF_CODE"
                        + " LEFT OUTER JOIN G_SPC ON E1.AUTH=G_SPC.SPC";
                pst = con.prepareStatement(sql);
                pst.setInt(1, notId);
                pst.setString(2, empid);
                rs = pst.executeQuery();
                if (rs.next()) {
                    payfix.setNotId(rs.getInt("NOT_ID"));
                    payfix.setLinkid(rs.getString("LINKID"));
                    payfix.setNotType(rs.getString("NOT_TYPE"));
                    payfix.setTxtNotOrdNo(rs.getString("ORDNO"));
                    if (rs.getDate("ORDDT") != null && !rs.getDate("ORDDT").equals("")) {
                        payfix.setTxtNotOrdDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("ORDDT")));
                    }
                    payfix.setRadnotifyingauthtype(rs.getString("organization_type"));
                    payfix.setHidNotifyingDeptCode(rs.getString("DEPT_CODE"));
                    payfix.setHidNotifyingOffCode(rs.getString("OFF_CODE"));
                    if (payfix.getRadnotifyingauthtype() != null && payfix.getRadnotifyingauthtype().equals("GOI")) {
                        payfix.setNotifyingPostName(getOtherSpn(rs.getString("AUTH")));
                        payfix.setHidNotifyingOthSpc(rs.getString("AUTH"));
                    } else {
                        payfix.setRadnotifyingauthtype("GOO");
                        payfix.setNotifyingSpc(rs.getString("AUTH"));
                        payfix.setNotifyingPostName(rs.getString("SPN"));
                    }
                    payfix.setNote(rs.getString("note"));
                    if (rs.getString("if_visible") != null && rs.getString("if_visible").equals("N")) {
                        payfix.setChkNotSBPrint("Y");
                    } else {
                        payfix.setChkNotSBPrint("N");
                    }
                }

                DataBaseFunctions.closeSqlObjects(rs, pst);

                sql = "SELECT prid,doni FROM emp_pay WHERE not_id=? AND EMP_ID=?";
                pst = con.prepareStatement(sql);
                pst.setInt(1, notId);
                pst.setString(2, empid);
                rs = pst.executeQuery();
                if (rs.next()) {
                    payfix.setPayid(rs.getInt("prid") + "");
                    payfix.setTxtNextIncrementDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("doni")));
                }

                DataBaseFunctions.closeSqlObjects(rs, pst);

                sql = "SELECT pay_id,wef,weft,pay_scale,pay,s_pay,p_pay,oth_pay,oth_desc,gp,pay_level, pay_cell,increment_reason  FROM emp_pay_record WHERE not_id=? AND EMP_ID=?";
                pst = con.prepareStatement(sql);
                pst.setInt(1, notId);
                pst.setString(2, empid);
                rs = pst.executeQuery();
                if (rs.next()) {
                    payfix.setPayRecordId(rs.getInt("pay_id"));
                    payfix.setSltPayScale(rs.getString("pay_scale"));
                    payfix.setTxtBasic(rs.getString("pay"));
                    payfix.setTxtGP(rs.getString("gp"));
                    payfix.setTxtPP(rs.getString("p_pay"));
                    payfix.setTxtSP(rs.getString("s_pay"));
                    payfix.setTxtOP(rs.getString("oth_pay"));
                    payfix.setTxtDescOP(rs.getString("oth_desc"));
                    payfix.setTxtWEFDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("wef")));
                    payfix.setSltWEFTime(rs.getString("weft"));
                    payfix.setPayCell(rs.getString("pay_cell"));
                    payfix.setPayLevel(rs.getString("pay_level"));
                    payfix.setSltPayFixationReason(rs.getString("increment_reason"));
                }

                DataBaseFunctions.closeSqlObjects(rs, pst);

                List li = new ArrayList();

                pst = con.prepareStatement("select  emp_incr.not_type, emp_incr.emp_id, emp_incr.incr, incrid, emp_incr.not_id, wef, weft, pay_scale, pay,"
                        + " s_pay, p_pay, oth_pay, oth_desc, gp, pay_level,pay_cell,emp_pay_record.pay_id from emp_incr "
                        + " inner join emp_pay_record on emp_incr.not_id=emp_pay_record.not_id "
                        + " where prid=? and emp_incr.emp_id=? ");
                pst.setInt(1, Integer.parseInt(payfix.getPayid()));
                pst.setString(2, empid);
                rs = pst.executeQuery();
                while (rs.next()) {
                    RetrospectiveIncrement retro = new RetrospectiveIncrement();
                    retro.setIncrId(rs.getInt("incrid"));
                    retro.setPrId(rs.getInt("pay_id"));
                    retro.setNotId(rs.getInt("not_id"));
                    retro.setWefDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("wef")));
                    retro.setNewBasic(rs.getInt("pay"));
                    retro.setIncrAmount(rs.getInt("incr"));
                    retro.setPersonalPay(rs.getString("p_pay"));
                    retro.setSpecialPay(rs.getString("s_pay"));
                    retro.setOtherPay(rs.getString("oth_pay"));
                    retro.setDescOtherPay(rs.getString("oth_desc"));
                    retro.setSltPayScale(rs.getString("pay_scale"));
                    retro.setTxtGP(rs.getString("gp"));
                    retro.setPayLevel(rs.getString("pay_level"));
                    retro.setPayCell(rs.getString("pay_cell"));
                    li.add(retro);
                }
                payfix.setRetroIncrement(li);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return payfix;
    }

    @Override
    public void insertSupersedePayFixationData(PayFixation payfix, int notId) {

        Connection con = null;

        PreparedStatement pst = null;
        PreparedStatement pstIncr = null;

        EmpPayRecordForm epayrecordform = new EmpPayRecordForm();
        try {
            con = this.dataSource.getConnection();

            String sql = "insert into emp_pay ( NOT_ID, NOT_TYPE, EMP_ID, DONI) values (?,?,?,?)";
            pst = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pst.setInt(1, notId);
            pst.setString(2, payfix.getNotType());
            pst.setString(3, payfix.getEmpid());
            if (payfix.getTxtNextIncrementDt() != null && !payfix.getTxtNextIncrementDt().equals("")) {
                pst.setTimestamp(4, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(payfix.getTxtNextIncrementDt()).getTime()));
            } else {
                pst.setTimestamp(4, null);
            }
            pst.executeUpdate();
            ResultSet rs = pst.getGeneratedKeys();
            rs.next();
            int maxPrid = rs.getInt("PRID");

            epayrecordform.setEmpid(payfix.getEmpid());
            epayrecordform.setNot_id(notId);
            epayrecordform.setNot_type(payfix.getNotType());
            epayrecordform.setPayscale(payfix.getSltPayScale());
            epayrecordform.setBasic(payfix.getTxtBasic());
            epayrecordform.setGp(payfix.getTxtGP());
            epayrecordform.setS_pay(payfix.getTxtSP());
            epayrecordform.setP_pay(payfix.getTxtPP());
            epayrecordform.setOth_pay(payfix.getTxtOP());
            epayrecordform.setOth_desc(payfix.getTxtDescOP());
            epayrecordform.setWefDt(payfix.getTxtWEFDt());
            epayrecordform.setWefTime(payfix.getSltWEFTime());
            epayrecordform.setReasonpayfixation(payfix.getSltPayFixationReason());
            epayrecordform.setPayLevel(payfix.getPayLevel());
            epayrecordform.setPayCell(payfix.getPayCell());
            if (payfix.getRdoPaycomm() != null && (payfix.getRdoPaycomm().equals("6") || payfix.getRdoPaycomm().equals("5"))) {
                epayrecordform.setPayCell(null);
                epayrecordform.setPayLevel(null);
            } else if (payfix.getRdoPaycomm() != null && payfix.getRdoPaycomm().equals("7")) {
                epayrecordform.setPayscale(null);
                epayrecordform.setGp(null);
            }
            emppayrecordDAO.saveEmpPayRecordData(epayrecordform);

            DataBaseFunctions.closeSqlObjects(pst);
            if (payfix.getRdTransaction().equalsIgnoreCase("C")) {
                sql = "UPDATE EMP_MAST SET CUR_SALARY=?,CUR_BASIC_SALARY=?,DATE_OF_NINCR=?,GP=?,SPAY=?,PPAY=?,OTHPAY=?,PAY_DATE=? WHERE EMP_ID=?";
                pst = con.prepareStatement(sql);
                pst.setString(1, payfix.getSltPayScale());
                if (payfix.getTxtBasic() != null && !payfix.getTxtBasic().equals("")) {
                    pst.setDouble(2, Double.parseDouble(payfix.getTxtBasic()));
                } else {
                    pst.setDouble(2, 0);
                }
                if (payfix.getTxtNextIncrementDt() != null && !payfix.getTxtNextIncrementDt().equals("")) {
                    pst.setTimestamp(3, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(payfix.getTxtNextIncrementDt()).getTime()));
                } else {
                    pst.setTimestamp(3, null);
                }
                if (payfix.getTxtGP() != null && !payfix.getTxtGP().equals("")) {
                    pst.setDouble(4, Double.parseDouble(payfix.getTxtGP()));
                } else {
                    pst.setDouble(4, 0);
                }
                if (payfix.getTxtSP() != null && !payfix.getTxtSP().equals("")) {
                    pst.setInt(5, Integer.parseInt(payfix.getTxtSP()));
                } else {
                    pst.setInt(5, 0);
                }
                if (payfix.getTxtPP() != null && !payfix.getTxtPP().equals("")) {
                    pst.setInt(6, Integer.parseInt(payfix.getTxtPP()));
                } else {
                    pst.setInt(6, 0);
                }
                if (payfix.getTxtOP() != null && !payfix.getTxtOP().equals("")) {
                    pst.setInt(7, Integer.parseInt(payfix.getTxtOP()));
                } else {
                    pst.setInt(7, 0);
                }
                if (payfix.getTxtWEFDt() != null && !payfix.getTxtWEFDt().equals("")) {
                    pst.setTimestamp(8, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(payfix.getTxtWEFDt()).getTime()));
                } else {
                    pst.setTimestamp(8, null);
                }
                pst.setString(9, payfix.getEmpid());
                pst.executeUpdate();
            }
            DataBaseFunctions.closeSqlObjects(pst);
            /*
             pst=con.prepareStatement("insert into emp_incr (incrid, prid, not_id, not_type, emp_id, incr, first_incr, second_incr, third_incr, fourth_incr, incr_type) values(?,?,?,?,?,?,?,?,?,?,?)");
             PreparedStatement pstNotification=con.prepareStatement("insert into emp_notification (not_id,not_type,emp_id,doe,ordno,orddt,dept_code,off_code,auth,if_assumed,toe,ent_dept,ent_off,ent_auth) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
             PreparedStatement pstPayRecord=con.prepareStatement("insert into emp_pay_record (pay_id, not_type, not_id, emp_id, wef, weft, pay_scale, pay, s_pay, p_pay, oth_pay, oth_desc) values(?,?,?,?,?,?,?,?,?,?,?,?)");
             */
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");

            pstIncr = con.prepareStatement("insert into emp_incr ( prid, not_id, not_type, emp_id, incr) values(?,?,?,?,?)");

            if (payfix.getRetroIncrement() != null) {
                for (int i = 0; i < payfix.getRetroIncrement().size(); i++) {
                    RetrospectiveIncrement rtoIncr = (RetrospectiveIncrement) payfix.getRetroIncrement().get(i);

                    /**
                     * INSERTING VALUES TO EMP_NOTIFICATION
                     */
                    NotificationBean nb = new NotificationBean();
                    nb.setNottype("INCREMENT");
                    nb.setEmpId(payfix.getEmpid());
                    nb.setDateofEntry(new Date());
                    nb.setOrdno(payfix.getTxtNotOrdNo());
                    nb.setOrdDate(sdf.parse(payfix.getTxtNotOrdDt()));
                    nb.setSancDeptCode(payfix.getHidNotifyingDeptCode());
                    nb.setSancOffCode(payfix.getHidNotifyingOffCode());
                    nb.setSancAuthCode(payfix.getNotifyingSpc());
                    //nb.setNote(payfix.getNote());
                    int maxnotid = notificationDao.insertNotificationData(nb);

                    /**
                     * INSERTING VALUES TO EMP_INCREMENT
                     */
                    System.out.println("maxPrid is: " + maxPrid);
                    pstIncr.setInt(1, maxPrid);
                    pstIncr.setInt(2, maxnotid);
                    pstIncr.setString(3, "INCREMENT");
                    pstIncr.setString(4, payfix.getEmpid());
                    if (rtoIncr.getIncrAmount() > 0) {
                        pstIncr.setDouble(5, rtoIncr.getIncrAmount());
                    } else {
                        pstIncr.setDouble(5, 0);
                    }
                    pstIncr.executeUpdate();

                    // Insert data into emp_pay_record
                    epayrecordform = new EmpPayRecordForm();
                    epayrecordform.setEmpid(payfix.getEmpid());
                    epayrecordform.setNot_id(maxnotid);
                    epayrecordform.setNot_type("INCREMENT");
                    epayrecordform.setPayscale(rtoIncr.getSltPayScale());
                    epayrecordform.setGp(rtoIncr.getTxtGP());
                    epayrecordform.setBasic(rtoIncr.getNewBasic() + "");

                    epayrecordform.setS_pay(rtoIncr.getSpecialPay());
                    epayrecordform.setP_pay(rtoIncr.getPersonalPay());
                    epayrecordform.setOth_pay(rtoIncr.getOtherPay());
                    epayrecordform.setOth_desc(rtoIncr.getDescOtherPay());
                    epayrecordform.setWefDt(rtoIncr.getWefDate());
                    epayrecordform.setWefTime(payfix.getSltWEFTime());

                    epayrecordform.setPayLevel(rtoIncr.getPayLevel());
                    epayrecordform.setPayCell(rtoIncr.getPayCell());

                    emppayrecordDAO.saveEmpPayRecordData(epayrecordform);

                }
            }
            DataBaseFunctions.closeSqlObjects(pstIncr);
            /*
             * Updating the Service Book Language
             */
            if (payfix.getChkNotSBPrint() != null && payfix.getChkNotSBPrint().equals("Y")) {
                pst = con.prepareStatement("UPDATE EMP_NOTIFICATION SET IF_VISIBLE='N' WHERE NOT_ID=?");
                pst.setInt(1, notId);
                pst.execute();
            } else if (payfix.getChkNotSBPrint() == null || payfix.getChkNotSBPrint().equals("")) {
                String sbLanguage = sbDAO.getSupersedeLangDetails(Integer.parseInt(payfix.getLinkid()), payfix.getHidNotifyingOthSpc());

                pst = con.prepareStatement("UPDATE EMP_NOTIFICATION SET SB_DESCRIPTION=?,IF_VISIBLE='Y' WHERE NOT_ID=?");
                pst.setString(1, sbLanguage);
                pst.setInt(2, notId);
                pst.execute();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void updateSupersedePayFixationData(PayFixation payfix) {

        Connection con = null;

        PreparedStatement pst = null;
        PreparedStatement pstIncr = null;
        PreparedStatement pstUpIncr = null;
        int maxPrid = 0;
        EmpPayRecordForm epayrecordform = new EmpPayRecordForm();
        String sql = "";
        try {
            con = this.dataSource.getConnection();

            maxPrid = Integer.parseInt(payfix.getPayid());

            if (payfix.getTxtNextIncrementDt() != null && !payfix.getTxtNextIncrementDt().equals("")) {
                sql = "update emp_pay set DONI = ? where PRID=? and emp_id=?";
                pst = con.prepareStatement(sql);
                pst.setTimestamp(1, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(payfix.getTxtNextIncrementDt()).getTime()));
                pst.setInt(2, Integer.parseInt(payfix.getPayid()));
                pst.setString(3, payfix.getEmpid());
                pst.executeUpdate();
            }
            DataBaseFunctions.closeSqlObjects(pst);

            epayrecordform.setNot_id(payfix.getNotId());
            epayrecordform.setEmpid(payfix.getEmpid());
            epayrecordform.setPayid(payfix.getPayRecordId());
            epayrecordform.setPayscale(payfix.getSltPayScale());
            epayrecordform.setPayCell(payfix.getPayCell());
            epayrecordform.setPayLevel(payfix.getPayLevel());
            epayrecordform.setBasic(payfix.getTxtBasic());
            epayrecordform.setGp(payfix.getTxtGP());
            epayrecordform.setS_pay(payfix.getTxtSP());
            epayrecordform.setP_pay(payfix.getTxtPP());
            epayrecordform.setOth_pay(payfix.getTxtOP());
            epayrecordform.setOth_desc(payfix.getTxtDescOP());
            epayrecordform.setWefDt(payfix.getTxtWEFDt());
            epayrecordform.setWefTime(payfix.getSltWEFTime());
            epayrecordform.setReasonpayfixation(payfix.getSltPayFixationReason());
            if (payfix.getRdoPaycomm() != null && (payfix.getRdoPaycomm().equals("6") || payfix.getRdoPaycomm().equals("5"))) {
                epayrecordform.setPayCell(null);
                epayrecordform.setPayLevel(null);
            } else if (payfix.getRdoPaycomm() != null && payfix.getRdoPaycomm().equals("7")) {
                epayrecordform.setPayscale(null);
                epayrecordform.setGp(null);
            }
            emppayrecordDAO.updateEmpPayRecordData(epayrecordform);

            if (payfix.getRdTransaction().equalsIgnoreCase("C")) {
                sql = "UPDATE EMP_MAST SET CUR_SALARY=?,CUR_BASIC_SALARY=?,DATE_OF_NINCR=?,GP=?,SPAY=?,PPAY=?,OTHPAY=?,PAY_DATE=? WHERE EMP_ID=?";
                pst = con.prepareStatement(sql);
                pst.setString(1, payfix.getSltPayScale());
                if (payfix.getTxtBasic() != null && !payfix.getTxtBasic().equals("")) {
                    pst.setDouble(2, Double.parseDouble(payfix.getTxtBasic()));
                } else {
                    pst.setDouble(2, 0);
                }
                if (payfix.getTxtNextIncrementDt() != null && !payfix.getTxtNextIncrementDt().equals("")) {
                    pst.setTimestamp(3, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(payfix.getTxtNextIncrementDt()).getTime()));
                } else {
                    pst.setTimestamp(3, null);
                }
                if (payfix.getTxtGP() != null && !payfix.getTxtGP().equals("")) {
                    pst.setDouble(4, Double.parseDouble(payfix.getTxtGP()));
                } else {
                    pst.setDouble(4, 0);
                }
                if (payfix.getTxtSP() != null && !payfix.getTxtSP().equals("")) {
                    pst.setInt(5, Integer.parseInt(payfix.getTxtSP()));
                } else {
                    pst.setInt(5, 0);
                }
                if (payfix.getTxtPP() != null && !payfix.getTxtPP().equals("")) {
                    pst.setInt(6, Integer.parseInt(payfix.getTxtPP()));
                } else {
                    pst.setInt(6, 0);
                }
                if (payfix.getTxtOP() != null && !payfix.getTxtOP().equals("")) {
                    pst.setInt(7, Integer.parseInt(payfix.getTxtOP()));
                } else {
                    pst.setInt(7, 0);
                }
                if (payfix.getTxtWEFDt() != null && !payfix.getTxtWEFDt().equals("")) {
                    pst.setTimestamp(8, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(payfix.getTxtWEFDt()).getTime()));
                } else {
                    pst.setTimestamp(8, null);
                }
                pst.setString(9, payfix.getEmpid());
                pst.executeUpdate();
            }

            DataBaseFunctions.closeSqlObjects(pst);

            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");

            if (payfix.getRetroIncrement() != null) {

                pstIncr = con.prepareStatement("insert into emp_incr ( prid, not_id, not_type, emp_id, incr) values(?,?,?,?,?)");
                pstUpIncr = con.prepareStatement("update emp_incr set prid=?, not_id=?, not_type=?, emp_id=?, incr=? where incrid=?");

                for (int i = 0; i < payfix.getRetroIncrement().size(); i++) {
                    RetrospectiveIncrement rtoIncr = (RetrospectiveIncrement) payfix.getRetroIncrement().get(i);

                    if (rtoIncr.getIncrId() < 1) {

                        /**
                         * INSERTING VALUES TO EMP_NOTIFICATION
                         */
                        NotificationBean nb = new NotificationBean();
                        nb.setNottype("INCREMENT");
                        nb.setEmpId(payfix.getEmpid());
                        nb.setDateofEntry(new Date());
                        nb.setOrdno(payfix.getTxtNotOrdNo());
                        nb.setOrdDate(sdf.parse(payfix.getTxtNotOrdDt()));
                        nb.setSancDeptCode(payfix.getHidNotifyingDeptCode());
                        nb.setSancOffCode(payfix.getHidNotifyingOffCode());
                        nb.setSancAuthCode(payfix.getNotifyingSpc());
                        //nb.setNote(payfix.getNote());
                        int maxnotid = notificationDao.insertNotificationData(nb);

                        /**
                         * INSERTING VALUES TO EMP_INCREMENT
                         */
                        pstIncr.setInt(1, maxPrid);
                        pstIncr.setInt(2, maxnotid);
                        pstIncr.setString(3, "INCREMENT");
                        pstIncr.setString(4, payfix.getEmpid());
                        if (rtoIncr.getIncrAmount() > 0) {
                            pstIncr.setDouble(5, rtoIncr.getIncrAmount());
                        } else {
                            pstIncr.setDouble(5, 0);
                        }
                        pstIncr.executeUpdate();

                        // Insert data into emp_pay_record
                        epayrecordform = new EmpPayRecordForm();
                        epayrecordform.setEmpid(payfix.getEmpid());
                        epayrecordform.setNot_id(maxnotid);
                        epayrecordform.setNot_type("INCREMENT");
                        epayrecordform.setPayscale(rtoIncr.getSltPayScale());
                        epayrecordform.setGp(rtoIncr.getTxtGP());
                        epayrecordform.setBasic(rtoIncr.getNewBasic() + "");
                        epayrecordform.setPayCell(rtoIncr.getPayCell());
                        epayrecordform.setPayLevel(rtoIncr.getPayLevel());
                        epayrecordform.setS_pay(rtoIncr.getSpecialPay());
                        epayrecordform.setP_pay(rtoIncr.getPersonalPay());
                        epayrecordform.setOth_pay(rtoIncr.getOtherPay());
                        epayrecordform.setOth_desc(rtoIncr.getDescOtherPay());
                        epayrecordform.setWefDt(rtoIncr.getWefDate());
                        epayrecordform.setWefTime(payfix.getSltWEFTime());
                        emppayrecordDAO.saveEmpPayRecordData(epayrecordform);
                    } else if (rtoIncr.getIncrId() > 0) {
                        /**
                         * updating VALUES TO EMP_NOTIFICATION
                         */

                        NotificationBean nb = new NotificationBean();
                        nb.setNottype("INCREMENT");
                        nb.setEmpId(payfix.getEmpid());
                        nb.setDateofEntry(new Date());
                        nb.setOrdno(payfix.getTxtNotOrdNo());
                        nb.setOrdDate(sdf.parse(payfix.getTxtNotOrdDt()));
                        nb.setSancDeptCode(payfix.getHidNotifyingDeptCode());
                        nb.setSancOffCode(payfix.getHidNotifyingOffCode());
                        nb.setSancAuthCode(payfix.getNotifyingSpc());
                        nb.setNotid(rtoIncr.getNotId());
                        notificationDao.modifyNotificationData(nb);

                        /**
                         * updating VALUES TO EMP_INCREMENT
                         */
                        pstUpIncr.setInt(1, maxPrid);
                        pstUpIncr.setInt(2, rtoIncr.getNotId());
                        pstUpIncr.setString(3, "INCREMENT");
                        pstUpIncr.setString(4, payfix.getEmpid());
                        if (rtoIncr.getIncrAmount() > 0) {
                            pstUpIncr.setDouble(5, rtoIncr.getIncrAmount());
                        } else {
                            pstUpIncr.setDouble(5, 0);
                        }
                        pstUpIncr.setInt(6, rtoIncr.getIncrId());
                        pstUpIncr.executeUpdate();

                        // update data into emp_pay_record
                        epayrecordform = new EmpPayRecordForm();
                        //epayrecordform.setPayid(rtoIncr.getPayrecorid());
                        epayrecordform.setPayid(rtoIncr.getPrId());
                        epayrecordform.setEmpid(payfix.getEmpid());
                        epayrecordform.setNot_id(rtoIncr.getNotId());
                        epayrecordform.setNot_type("INCREMENT");
                        epayrecordform.setPayscale(rtoIncr.getSltPayScale());
                        epayrecordform.setGp(rtoIncr.getTxtGP());
                        epayrecordform.setBasic(rtoIncr.getNewBasic() + "");
                        epayrecordform.setPayCell(rtoIncr.getPayCell());
                        epayrecordform.setPayLevel(rtoIncr.getPayLevel());
                        epayrecordform.setS_pay(rtoIncr.getSpecialPay());
                        epayrecordform.setP_pay(rtoIncr.getPersonalPay());
                        epayrecordform.setOth_pay(rtoIncr.getOtherPay());
                        epayrecordform.setOth_desc(rtoIncr.getDescOtherPay());
                        epayrecordform.setWefDt(rtoIncr.getWefDate());
                        epayrecordform.setWefTime(payfix.getSltWEFTime());
                        emppayrecordDAO.updateEmpPayRecordData(epayrecordform);
                    }
                }
            }

            if (payfix.getChkNotSBPrint() != null && payfix.getChkNotSBPrint().equals("Y")) {
                pst = con.prepareStatement("UPDATE EMP_NOTIFICATION SET IF_VISIBLE='N' WHERE NOT_ID=?");
                pst.setInt(1, payfix.getNotId());
                pst.execute();
            } else if (payfix.getChkNotSBPrint() == null || payfix.getChkNotSBPrint().equals("")) {
                String sbLanguage = sbDAO.getSupersedeLangDetails(Integer.parseInt(payfix.getLinkid()), payfix.getHidNotifyingOthSpc());

                pst = con.prepareStatement("UPDATE EMP_NOTIFICATION SET SB_DESCRIPTION=?,IF_VISIBLE='Y' WHERE NOT_ID=?");
                pst.setString(1, sbLanguage);
                pst.setInt(2, payfix.getNotId());
                pst.execute();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstIncr, pstUpIncr);
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public String getEmployeeDateOfEntry(String empid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String doegov = "";
        try {
            con = this.dataSource.getConnection();

            String sql = "SELECT DOE_GOV FROM EMP_MAST WHERE EMP_ID=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            rs = pst.executeQuery();
            if (rs.next()) {
                doegov = rs.getString("DOE_GOV");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return doegov;
    }

    @Override
    public List findAllFinBenefits(String empId) {
        List list = null;
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        PayFixationList payFixList = null;
        list = new ArrayList();
        try {
            con = this.repodataSource.getConnection();
            /*String sql = "select is_validated,prid pr_id,doni done,emp_notification.not_id nid,emp_notification.DOE ndoe,"
             + "emp_notification.link_id lnkid, emp_notification.SV_ID svid,emp_pay_record.wef pwef,emp_pay_record.weft pweft,"
             + "emp_pay_record.pay_scale ppayscale, emp_pay_record.pay ppay, emp_pay_record.gp gradepay from emp_pay "
             + "inner join emp_notification on emp_pay.not_id=emp_notification.not_id "
             + "inner join emp_pay_record on emp_notification.not_id = emp_pay_record.not_id "
             + "where emp_pay.emp_id=? and emp_pay.not_type=? ORDER BY ndoe DESC";*/
            String sql = "select e1.not_id nid,e1.DOE ndoe,e1.link_id linkid, e1.SV_ID svid, e2.not_type canceltype,emp_pay_record.wef pwef,emp_pay_record.weft pweft,"
                    + " emp_pay_record.pay_scale ppayscale, emp_pay_record.pay ppay, emp_pay_record.gp gradepay, emp_pay_record.not_type, increment_reason from emp_pay"
                    + " inner join emp_notification e1 on emp_pay.not_id=e1.not_id"
                    + " inner join emp_pay_record on e1.not_id = emp_pay_record.not_id"
                    + " left outer join emp_notification e2 on e1.link_id=e2.not_id"
                    + " where emp_pay.emp_id=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, empId);

            rs = pst.executeQuery();
            while (rs.next()) {
                payFixList = new PayFixationList();
                payFixList.setDoe(CommonFunctions.getFormattedOutputDate1(rs.getDate("NDOE")));
                payFixList.setNotId(rs.getString("NID"));
                payFixList.setGradePay(rs.getString("gradepay"));
                payFixList.setRevPayScale(rs.getString("ppayscale"));
                payFixList.setCurbasic(rs.getString("ppay"));
                payFixList.setWefDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("pwef")));
                if (rs.getString("pweft") != null && !rs.getString("pweft").equals("")) {
                    if (rs.getString("pweft").equals("FN")) {
                        payFixList.setWefTime("FORE NOON");
                    } else if (rs.getString("pweft").equals("AN")) {
                        payFixList.setWefTime("AFTER NOON");
                    }
                }
                String incrementReason = "";
                if (rs.getString("increment_reason") != null) {
                    incrementReason = "<br />" + rs.getString("increment_reason");
                }

                payFixList.setNotType(rs.getString("not_type") + incrementReason);
                // payFixList.setDateOfNextIncrement(CommonFunctions.getFormattedOutputDate1(rs.getDate("done")));
                //payFixList.setIsValidated(rs.getString("IS_VALIDATED"));

                payFixList.setCanceltype(rs.getString("canceltype"));

                list.add(payFixList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return list;
    }

    @Override
    public void insertFinancialBenefitData(PayFixation payfix, int notId) {

        Connection con = null;

        PreparedStatement pst = null;
        PreparedStatement pstIncr = null;

        EmpPayRecordForm epayrecordform = new EmpPayRecordForm();
        try {
            con = this.dataSource.getConnection();

            String sql = "insert into emp_pay ( NOT_ID, NOT_TYPE, EMP_ID, DONI) values (?,?,?,?)";
            pst = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pst.setInt(1, notId);
            pst.setString(2, payfix.getNotType());
            pst.setString(3, payfix.getEmpid());
            if (payfix.getTxtNextIncrementDt() != null && !payfix.getTxtNextIncrementDt().equals("")) {
                pst.setTimestamp(4, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(payfix.getTxtNextIncrementDt()).getTime()));
            } else {
                pst.setTimestamp(4, null);
            }
            pst.executeUpdate();
            ResultSet rs = pst.getGeneratedKeys();
            rs.next();
            int maxPrid = rs.getInt("PRID");

            epayrecordform.setEmpid(payfix.getEmpid());
            epayrecordform.setNot_id(notId);
            epayrecordform.setNot_type(payfix.getNotType());
            epayrecordform.setPayscale(payfix.getSltPayScale());
            epayrecordform.setBasic(payfix.getTxtBasic());
            epayrecordform.setGp(payfix.getTxtGP());
            epayrecordform.setS_pay(payfix.getTxtSP());
            epayrecordform.setP_pay(payfix.getTxtPP());
            epayrecordform.setOth_pay(payfix.getTxtOP());
            epayrecordform.setOth_desc(payfix.getTxtDescOP());
            epayrecordform.setWefDt(payfix.getTxtWEFDt());

            epayrecordform.setWefTime(payfix.getSltWEFTime());
            epayrecordform.setReasonpayfixation(payfix.getSltPayFixationReason());
            epayrecordform.setPayLevel(payfix.getPayLevel());
            epayrecordform.setPayCell(payfix.getPayCell());
            if (payfix.getRdoPaycomm() != null && (payfix.getRdoPaycomm().equals("6") || payfix.getRdoPaycomm().equals("5"))) {
                epayrecordform.setPayCell(null);
                epayrecordform.setPayLevel(null);
            } else if (payfix.getRdoPaycomm() != null && payfix.getRdoPaycomm().equals("7")) {
                epayrecordform.setPayscale(null);
                epayrecordform.setGp(null);
            }
            emppayrecordDAO.saveEmpPayRecordData(epayrecordform);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    @Override
    public void updateFinancialBenefitData(PayFixation payfix) {

        Connection con = null;

        PreparedStatement pst = null;
        EmpPayRecordForm epayrecordform = new EmpPayRecordForm();
        String sql = "";
        try {
            con = this.dataSource.getConnection();

            if (payfix.getTxtNextIncrementDt() != null && !payfix.getTxtNextIncrementDt().equals("")) {
                sql = "update emp_pay set DONI = ? where PRID=? and emp_id=?";
                pst = con.prepareStatement(sql);
                pst.setTimestamp(1, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(payfix.getTxtNextIncrementDt()).getTime()));
                pst.setInt(2, Integer.parseInt(payfix.getPayid()));
                pst.setString(3, payfix.getEmpid());
                pst.executeUpdate();
            }
            epayrecordform.setNot_id(payfix.getNotId());
            epayrecordform.setEmpid(payfix.getEmpid());
            epayrecordform.setPayid(payfix.getPayRecordId());
            epayrecordform.setPayscale(payfix.getSltPayScale());
            epayrecordform.setPayCell(payfix.getPayCell());
            epayrecordform.setPayLevel(payfix.getPayLevel());
            epayrecordform.setBasic(payfix.getTxtBasic());
            epayrecordform.setGp(payfix.getTxtGP());
            epayrecordform.setS_pay(payfix.getTxtSP());
            epayrecordform.setP_pay(payfix.getTxtPP());
            epayrecordform.setOth_pay(payfix.getTxtOP());
            epayrecordform.setOth_desc(payfix.getTxtDescOP());
            epayrecordform.setWefDt(payfix.getTxtWEFDt());
            epayrecordform.setWefTime(payfix.getSltWEFTime());
            epayrecordform.setReasonpayfixation(payfix.getSltPayFixationReason());
            if (payfix.getRdoPaycomm() != null && (payfix.getRdoPaycomm().equals("6") || payfix.getRdoPaycomm().equals("5"))) {
                epayrecordform.setPayCell(null);
                epayrecordform.setPayLevel(null);
            } else if (payfix.getRdoPaycomm() != null && payfix.getRdoPaycomm().equals("7")) {
                epayrecordform.setPayscale(null);
                epayrecordform.setGp(null);
            }
            emppayrecordDAO.updateEmpPayRecordData(epayrecordform);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public String insertPayFixationDataSBCorrection(PayFixation payfix, int notId, int refcorrectionid) {

        Connection con = null;

        PreparedStatement pst = null;

        EmpPayRecordForm epayrecordform = new EmpPayRecordForm();

        String sbLang = "";
        try {
            con = this.dataSource.getConnection();

            String sql = "delete from emp_pay_log where not_id=? and not_type=? and emp_id=? and PRID=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, notId);
            pst.setString(2, payfix.getNotType());
            pst.setString(3, payfix.getEmpid());
            if (payfix.getPayid() != null && !payfix.getPayid().equals("")) {
                pst.setInt(4, Integer.parseInt(payfix.getPayid()));
            } else {
                pst.setInt(4, 0);
            }
            pst.executeUpdate();

            sql = "insert into emp_pay_log ( NOT_ID, NOT_TYPE, EMP_ID, DONI,PRID,REF_CORRECTION_ID) values (?,?,?,?,?,?)";
            pst = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pst.setInt(1, notId);
            pst.setString(2, payfix.getNotType());
            pst.setString(3, payfix.getEmpid());
            if (payfix.getTxtNextIncrementDt() != null && !payfix.getTxtNextIncrementDt().equals("")) {
                pst.setTimestamp(4, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(payfix.getTxtNextIncrementDt()).getTime()));
            } else {
                pst.setTimestamp(4, null);
            }
            if (payfix.getPayid() != null && !payfix.getPayid().equals("")) {
                pst.setInt(5, Integer.parseInt(payfix.getPayid()));
            } else {
                pst.setInt(5, 0);
            }
            pst.setInt(6, refcorrectionid);
            pst.executeUpdate();

            epayrecordform.setEmpid(payfix.getEmpid());
            epayrecordform.setNot_id(notId);
            epayrecordform.setNot_type(payfix.getNotType());
            epayrecordform.setPayscale(payfix.getSltPayScale());
            epayrecordform.setBasic(payfix.getTxtBasic());
            epayrecordform.setGp(payfix.getTxtGP());
            epayrecordform.setS_pay(payfix.getTxtSP());
            epayrecordform.setP_pay(payfix.getTxtPP());
            epayrecordform.setOth_pay(payfix.getTxtOP());
            epayrecordform.setOth_desc(payfix.getTxtDescOP());
            epayrecordform.setWefDt(payfix.getTxtWEFDt());
            epayrecordform.setWefTime(payfix.getSltWEFTime());
            epayrecordform.setReasonpayfixation(payfix.getSltPayFixationReason());
            epayrecordform.setPayLevel(payfix.getPayLevel());
            epayrecordform.setPayCell(payfix.getPayCell());
            if (payfix.getRdoPaycomm() != null && (payfix.getRdoPaycomm().equals("6") || payfix.getRdoPaycomm().equals("5"))) {
                epayrecordform.setPayCell(null);
                epayrecordform.setPayLevel(null);
            } else if (payfix.getRdoPaycomm() != null && payfix.getRdoPaycomm().equals("7")) {
                epayrecordform.setPayscale(null);
                epayrecordform.setGp(null);
            }
            epayrecordform.setPayid(payfix.getPayRecordId());
            epayrecordform.setRefcorrectionid(refcorrectionid);
            emppayrecordDAO.deleteEmpPayRecordDataSBCorrection(epayrecordform);
            emppayrecordDAO.saveEmpPayRecordDataSBCorrection(epayrecordform);

            /*
             * Updating the Service Book Language
             */
            if (payfix.getChkNotSBPrint() != null && payfix.getChkNotSBPrint().equals("Y")) {
                pst = con.prepareStatement("UPDATE EMP_NOTIFICATION_LOG SET IF_VISIBLE='N' WHERE NOT_ID=?");
                pst.setInt(1, notId);
                pst.execute();
            } else if (payfix.getChkNotSBPrint() == null || payfix.getChkNotSBPrint().equals("")) {
                sbLang = sbDAO.getPayFixationDetailsSBCorrection(payfix, notId, payfix.getNotType());

                pst = con.prepareStatement("UPDATE EMP_NOTIFICATION_LOG SET SB_DESCRIPTION=?,IF_VISIBLE='Y' WHERE NOT_ID=?");
                pst.setString(1, sbLang);
                pst.setInt(2, notId);
                pst.execute();
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
    public void approvePayFixationDataSBCorrection(PayFixation payfix, String entrydeptcode, String entryoffcode, String entryspc, String loginuserid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        try {
            con = this.dataSource.getConnection();

            if (payfix.getEntrytype() != null && payfix.getEntrytype().equals("NEW")) {
                pst = con.prepareStatement("INSERT INTO EMP_NOTIFICATION(NOT_TYPE,EMP_ID,DOE,ORDNO,ORDDT,DEPT_CODE,OFF_CODE,AUTH,IF_VISIBLE,ENT_DEPT,ENT_OFF,ENT_AUTH) "
                        + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
                pst.setString(1, payfix.getNotType());
                pst.setString(2, payfix.getEmpid());
                pst.setTimestamp(3, new Timestamp(new Date().getTime()));
                pst.setString(4, payfix.getTxtNotOrdNo());
                if (payfix.getTxtNotOrdDt() != null) {
                    pst.setTimestamp(5, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(payfix.getTxtNotOrdDt()).getTime()));
                } else {
                    pst.setTimestamp(5, null);
                }
                pst.setString(6, payfix.getHidNotifyingDeptCode());
                pst.setString(7, payfix.getHidNotifyingOffCode());
                pst.setString(8, payfix.getNotifyingSpc());
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

                String sql = "insert into emp_pay ( NOT_ID, NOT_TYPE, EMP_ID, DONI) values (?,?,?,?)";
                pst = con.prepareStatement(sql);
                pst.setInt(1, notId);
                pst.setString(2, payfix.getNotType());
                pst.setString(3, payfix.getEmpid());
                if (payfix.getTxtNextIncrementDt() != null && !payfix.getTxtNextIncrementDt().equals("")) {
                    pst.setTimestamp(4, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(payfix.getTxtNextIncrementDt()).getTime()));
                } else {
                    pst.setTimestamp(4, null);
                }
                pst.executeUpdate();

                DataBaseFunctions.closeSqlObjects(rs, pst);

                sql = "insert into emp_pay_record (not_type, not_id, emp_id, wef, weft, pay_scale, pay, s_pay, p_pay, oth_pay, oth_desc, gp, pay_level, pay_cell) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                pst = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                pst.setString(1, payfix.getNotType());
                pst.setInt(2, notId);
                pst.setString(3, payfix.getEmpid());
                if (payfix.getTxtWEFDt() != null && !payfix.getTxtWEFDt().equals("")) {
                    pst.setTimestamp(4, new Timestamp(sdf.parse(payfix.getTxtWEFDt()).getTime()));
                } else {
                    pst.setTimestamp(4, null);
                }
                pst.setString(5, payfix.getSltWEFTime());

                if (payfix.getTxtBasic() == null || payfix.getTxtBasic().equals("")) {
                    pst.setDouble(7, 0);
                } else {
                    pst.setDouble(7, java.lang.Double.parseDouble(payfix.getTxtBasic()));
                }
                if (payfix.getTxtPP() == null || payfix.getTxtPP().equalsIgnoreCase("")) {
                    pst.setDouble(8, 0);
                } else {
                    pst.setDouble(8, java.lang.Double.parseDouble(payfix.getTxtPP()));
                }
                if (payfix.getTxtSP() == null || payfix.getTxtSP().equalsIgnoreCase("")) {
                    pst.setDouble(9, 0);
                } else {
                    pst.setDouble(9, java.lang.Double.parseDouble(payfix.getTxtSP()));
                }
                if (payfix.getTxtOP() == null || payfix.getTxtOP().equalsIgnoreCase("")) {
                    pst.setDouble(10, 0);
                } else {
                    pst.setDouble(10, java.lang.Double.parseDouble(payfix.getTxtOP()));
                }
                if (payfix.getTxtDescOP() != null && !payfix.getTxtDescOP().equals("")) {
                    pst.setString(11, payfix.getTxtDescOP().toUpperCase());
                } else {
                    pst.setString(11, null);
                }
                if (payfix.getPayLevel() != null && !payfix.getPayLevel().equals("")) {
                    pst.setString(6, null);
                    pst.setDouble(12, 0);
                    pst.setString(13, payfix.getPayLevel());
                    if (payfix.getPayCell() != null && !payfix.getPayCell().equals("")) {
                        pst.setString(14, payfix.getPayCell());
                    }
                } else if (payfix.getSltPayScale() != null && !payfix.getSltPayScale().equals("")) {
                    pst.setString(6, payfix.getSltPayScale());
                    if (payfix.getTxtGP() == null || payfix.getTxtGP().equals("")) {
                        pst.setDouble(12, 0);
                    } else {
                        pst.setDouble(12, java.lang.Double.parseDouble(payfix.getTxtGP()));
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
                pst.setString(3, payfix.getEmpid());
                pst.executeUpdate();

                DataBaseFunctions.closeSqlObjects(rs, pst);

                String sbLang = sbDAO.getPayFixationDetails(payfix, notId, payfix.getRdoPaycomm());
                pst = con.prepareStatement("UPDATE EMP_NOTIFICATION SET SB_DESCRIPTION=?,IF_VISIBLE='Y' WHERE NOT_ID=?");
                pst.setString(1, sbLang);
                pst.setInt(2, notId);
                pst.execute();
            } else {
                NotificationBean nb = new NotificationBean();

                nb.setNottype(payfix.getNotType());
                nb.setEmpId(payfix.getEmpid());
                nb.setNotid(payfix.getNotId());
                nb.setOrdno(payfix.getTxtNotOrdNo());
                nb.setOrdDate(sdf.parse(payfix.getTxtNotOrdDt()));
                nb.setSancDeptCode(payfix.getHidNotifyingDeptCode());
                nb.setSancOffCode(payfix.getHidNotifyingOffCode());
                nb.setRadpostingauthtype(payfix.getRadnotifyingauthtype());
                if (payfix.getRadnotifyingauthtype() != null && payfix.getRadnotifyingauthtype().equals("GOI")) {
                    nb.setSancAuthCode(payfix.getHidNotifyingOthSpc());
                } else {
                    nb.setSancAuthCode(payfix.getNotifyingSpc());
                }
                nb.setEntryDeptCode(entrydeptcode);
                nb.setEntryOffCode(entryoffcode);
                nb.setEntryAuthCode(entryspc);
                nb.setNote(payfix.getNote());
                nb.setLoginuserid(loginuserid);
                notificationDao.modifyNotificationDataSBCorrection(nb);

                if (payfix.getTxtNextIncrementDt() != null && !payfix.getTxtNextIncrementDt().equals("")) {
                    String sql = "update emp_pay_log set DONI = ? where PRID=? and emp_id=?";
                    pst = con.prepareStatement(sql);
                    pst.setTimestamp(1, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(payfix.getTxtNextIncrementDt()).getTime()));
                    pst.setInt(2, Integer.parseInt(payfix.getPayid()));
                    pst.setString(3, payfix.getEmpid());
                    pst.executeUpdate();
                }

                EmpPayRecordForm epayrecordform = new EmpPayRecordForm();
                epayrecordform.setEmpid(payfix.getEmpid());
                epayrecordform.setNot_id(payfix.getNotId());
                epayrecordform.setPayid(payfix.getPayRecordId());
                epayrecordform.setBasic(payfix.getTxtBasic());
                if (payfix.getRdoPaycomm() != null && payfix.getRdoPaycomm().equals("6")) {
                    epayrecordform.setPayscale(payfix.getSltPayScale());
                    epayrecordform.setGp(payfix.getTxtGP());
                    epayrecordform.setPayCell(null);
                    epayrecordform.setPayLevel(null);

                } else if (payfix.getRdoPaycomm() != null && payfix.getRdoPaycomm().equals("7")) {
                    epayrecordform.setPayscale(null);
                    epayrecordform.setGp("0");
                    epayrecordform.setPayCell(payfix.getPayCell());
                    epayrecordform.setPayLevel(payfix.getPayLevel());
                } else if (payfix.getRdoPaycomm() != null && payfix.getRdoPaycomm().equals("REM")) {
                    epayrecordform.setIfRemuneration("Y");
                    epayrecordform.setPayscale(payfix.getSltPayScale());
                    epayrecordform.setGp(payfix.getTxtGP());
                    epayrecordform.setPayCell(payfix.getPayCell());
                    epayrecordform.setPayLevel(payfix.getPayLevel());
                } else {
                    epayrecordform.setIfRemuneration("N");
                }
                epayrecordform.setS_pay(payfix.getTxtSP());
                epayrecordform.setP_pay(payfix.getTxtPP());
                epayrecordform.setOth_pay(payfix.getTxtOP());
                epayrecordform.setOth_desc(payfix.getTxtDescOP());
                epayrecordform.setWefDt(payfix.getTxtWEFDt());
                epayrecordform.setWefTime(payfix.getSltWEFTime());
                emppayrecordDAO.updateEmpPayRecordDataSBCorrection(epayrecordform);

                String sql = "UPDATE emp_pay"
                        + " SET doni = emp_pay_log.doni FROM emp_pay_log"
                        + " WHERE emp_pay.not_id = emp_pay_log.not_id and emp_pay_log.not_id=?";
                pst = con.prepareStatement(sql);
                pst.setInt(1, payfix.getNotId());
                pst.executeUpdate();

                DataBaseFunctions.closeSqlObjects(pst);

                sql = "UPDATE emp_notification"
                        + " SET ordno = emp_notification_log.ordno,orddt=emp_notification_log.orddt,dept_code=emp_notification_log.dept_code,off_code=emp_notification_log.off_code,auth=emp_notification_log.auth,organization_type=emp_notification_log.organization_type,organization_type_posting=emp_notification_log.organization_type_posting"
                        + " FROM emp_notification_log"
                        + " WHERE emp_notification.not_id = emp_notification_log.not_id and emp_notification_log.not_id=?";
                pst = con.prepareStatement(sql);
                pst.setInt(1, payfix.getNotId());
                pst.executeUpdate();

                DataBaseFunctions.closeSqlObjects(pst);

                sql = "UPDATE emp_pay_record"
                        + " SET wef = emp_pay_record_log.wef,weft=emp_pay_record_log.weft,pay_scale=emp_pay_record_log.pay_scale,pay=emp_pay_record_log.pay,s_pay=emp_pay_record_log.s_pay,p_pay=emp_pay_record_log.p_pay,oth_pay=emp_pay_record_log.oth_pay,oth_desc=emp_pay_record_log.oth_desc,gp=emp_pay_record_log.gp"
                        + " FROM emp_pay_record_log"
                        + " WHERE emp_pay_record.not_id = emp_pay_record_log.not_id and emp_pay_record_log.not_id=?";
                pst = con.prepareStatement(sql);
                pst.setInt(1, payfix.getNotId());
                pst.executeUpdate();

                String sbLang = sbDAO.getPayFixationDetails(payfix, payfix.getNotId(), payfix.getNotType());
                pst = con.prepareStatement("UPDATE EMP_NOTIFICATION SET SB_DESCRIPTION=?,IF_VISIBLE='Y' WHERE NOT_ID=?");
                pst.setString(1, sbLang);
                pst.setInt(2, payfix.getNotId());
                pst.execute();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public PayFixation editPayFixationSBCorrectionDDO(String empid, int notId, int refcorrectionid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        PayFixation payfix = new PayFixation();

        String sql = "";
        try {
            con = this.repodataSource.getConnection();

            payfix.setEmpid(empid);

            //sql = "SELECT NOT_ID,NOT_TYPE,ORDNO,EMP_ID,ORDDT,DEPT_CODE,NOTE,OFF_CODE,AUTH FROM EMP_NOTIFICATION WHERE NOT_ID=? AND EMP_ID=?";
            sql = "SELECT organization_type,NOT_ID,NOT_TYPE,ORDNO,EMP_ID,ORDDT,EMP_NOTIFICATION.DEPT_CODE,NOTE,EMP_NOTIFICATION.OFF_CODE,AUTH,DEPARTMENT_NAME,OFF_EN,SPN,note,if_visible FROM"
                    + " (SELECT organization_type,NOT_ID,NOT_TYPE,ORDNO,EMP_ID,ORDDT,DEPT_CODE,NOTE,OFF_CODE,AUTH,if_visible FROM EMP_NOTIFICATION_LOG WHERE"
                    + " NOT_ID=? AND EMP_ID=? AND REF_CORRECTION_ID=?)EMP_NOTIFICATION"
                    + " LEFT OUTER JOIN G_DEPARTMENT ON EMP_NOTIFICATION.DEPT_CODE=G_DEPARTMENT.DEPARTMENT_CODE"
                    + " LEFT OUTER JOIN G_OFFICE ON EMP_NOTIFICATION.OFF_CODE=G_OFFICE.OFF_CODE"
                    + " LEFT OUTER JOIN G_SPC ON EMP_NOTIFICATION.AUTH=G_SPC.SPC";
            pst = con.prepareStatement(sql);
            pst.setInt(1, notId);
            pst.setString(2, empid);
            pst.setInt(3, refcorrectionid);
            rs = pst.executeQuery();
            if (rs.next()) {
                payfix.setNotId(rs.getInt("NOT_ID"));
                payfix.setNotType(rs.getString("NOT_TYPE"));
                payfix.setTxtNotOrdNo(rs.getString("ORDNO"));
                if (rs.getDate("ORDDT") != null && !rs.getDate("ORDDT").equals("")) {
                    payfix.setTxtNotOrdDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("ORDDT")));
                }
                payfix.setRadnotifyingauthtype(rs.getString("organization_type"));
                payfix.setHidNotifyingDeptCode(rs.getString("DEPT_CODE"));
                payfix.setHidNotifyingOffCode(rs.getString("OFF_CODE"));
                if (payfix.getRadnotifyingauthtype() != null && payfix.getRadnotifyingauthtype().equals("GOI")) {
                    payfix.setNotifyingPostName(getOtherSpn(rs.getString("AUTH")));
                    payfix.setHidNotifyingOthSpc(rs.getString("AUTH"));
                } else {
                    payfix.setRadnotifyingauthtype("GOO");
                    payfix.setNotifyingSpc(rs.getString("AUTH"));
                    payfix.setNotifyingPostName(rs.getString("SPN"));
                }
                payfix.setNote(rs.getString("note"));
                if (rs.getString("if_visible") != null && rs.getString("if_visible").equals("N")) {
                    payfix.setChkNotSBPrint("Y");
                } else {
                    payfix.setChkNotSBPrint("N");
                }
            }

            DataBaseFunctions.closeSqlObjects(rs, pst);

            sql = "SELECT prid,doni FROM emp_pay_log WHERE not_id=? AND EMP_ID=? AND REF_CORRECTION_ID=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, notId);
            pst.setString(2, empid);
            pst.setInt(3, refcorrectionid);
            rs = pst.executeQuery();
            if (rs.next()) {
                payfix.setPayid(rs.getInt("prid") + "");
                payfix.setTxtNextIncrementDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("doni")));
            }

            DataBaseFunctions.closeSqlObjects(rs, pst);

            sql = "SELECT pay_id,wef,weft,pay_scale,pay,s_pay,p_pay,oth_pay,oth_desc,gp,pay_level, pay_cell,increment_reason FROM emp_pay_record_log WHERE not_id=? AND EMP_ID=? AND REF_CORRECTION_ID=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, notId);
            pst.setString(2, empid);
            pst.setInt(3, refcorrectionid);
            rs = pst.executeQuery();
            if (rs.next()) {
                payfix.setPayRecordId(rs.getInt("pay_id"));
                payfix.setSltPayScale(rs.getString("pay_scale"));
                payfix.setTxtBasic(rs.getString("pay"));
                payfix.setTxtGP(rs.getString("gp"));
                payfix.setTxtPP(rs.getString("p_pay"));
                payfix.setTxtSP(rs.getString("s_pay"));
                payfix.setTxtOP(rs.getString("oth_pay"));
                payfix.setTxtDescOP(rs.getString("oth_desc"));
                payfix.setTxtWEFDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("wef")));
                payfix.setSltWEFTime(rs.getString("weft"));
                payfix.setPayCell(rs.getString("pay_cell"));
                payfix.setPayLevel(rs.getString("pay_level"));
                payfix.setSltPayFixationReason(rs.getString("increment_reason"));
            }

            DataBaseFunctions.closeSqlObjects(rs, pst);

            /*List li = new ArrayList();

             pst = con.prepareStatement("select emp_incr.not_type, emp_incr.emp_id, emp_incr.incr, incrid, emp_incr.not_id, wef, weft, pay_scale, pay,"
             + " s_pay, p_pay, oth_pay, oth_desc, gp, pay_level,pay_cell,emp_pay_record.pay_id from emp_incr_log emp_incr"
             + " inner join emp_pay_record_log emp_pay_record on emp_incr.not_id=emp_pay_record.not_id "
             + " where prid=? and emp_incr.emp_id=? ");
             pst.setInt(1, Integer.parseInt(payfix.getPayid()));
             pst.setString(2, empid);
             rs = pst.executeQuery();
             while (rs.next()) {
             RetrospectiveIncrement retro = new RetrospectiveIncrement();
             retro.setIncrId(rs.getInt("incrid"));
             retro.setPrId(rs.getInt("pay_id"));
             retro.setNotId(rs.getInt("not_id"));
             retro.setWefDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("wef")));
             retro.setNewBasic(rs.getInt("pay"));
             retro.setIncrAmount(rs.getInt("incr"));
             retro.setPersonalPay(rs.getString("p_pay"));
             retro.setSpecialPay(rs.getString("s_pay"));
             retro.setOtherPay(rs.getString("oth_pay"));
             retro.setDescOtherPay(rs.getString("oth_desc"));
             retro.setSltPayScale(rs.getString("pay_scale"));
             retro.setTxtGP(rs.getString("gp"));
             retro.setPayLevel(rs.getString("pay_level"));
             retro.setPayCell(rs.getString("pay_cell"));
             li.add(retro);
             }
             payfix.setRetroIncrement(li);*/
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return payfix;
    }
}
