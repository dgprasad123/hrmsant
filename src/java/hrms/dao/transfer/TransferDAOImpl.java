package hrms.dao.transfer;

import hrms.SelectOption;
import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.common.Message;
import hrms.dao.emppayrecord.EmpPayRecordDAO;
import hrms.dao.emppayrecord.EmpPayRecordDAOImpl;
import hrms.dao.leaveAccount.ServiceBookLanguageDAO;
import hrms.model.emppayrecord.EmpPayRecordForm;
import hrms.model.login.LoginUserBean;
import hrms.model.notification.NotificationBean;
import hrms.model.notification.OtherSPCBean;
import hrms.model.transfer.TransferBean;
import hrms.model.transfer.TransferForm;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.apache.commons.lang.builder.ToStringBuilder;

public class TransferDAOImpl implements TransferDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    protected MaxTransferIdDAOImpl maxTransferIdDao;

    protected EmpPayRecordDAO emppayrecordDAO;

    protected ServiceBookLanguageDAO sbDAO;

    public void setSbDAO(ServiceBookLanguageDAO sbDAO) {
        this.sbDAO = sbDAO;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setMaxTransferIdDao(MaxTransferIdDAOImpl maxTransferIdDao) {
        this.maxTransferIdDao = maxTransferIdDao;
    }

    public void setEmppayrecordDAO(EmpPayRecordDAOImpl emppayrecordDAO) {
        this.emppayrecordDAO = emppayrecordDAO;
    }

    @Override
    public void saveTransferDeputation(TransferForm transferForm, int notid, String loginempid, String loginuserid) {

        Connection con = null;

        PreparedStatement pst = null;

        EmpPayRecordForm epayrecordform = new EmpPayRecordForm();

        try {
            con = this.dataSource.getConnection();

            if (transferForm.getRadpostingauthtype() != null && transferForm.getRadpostingauthtype().equals("GOI")) {
                transferForm.setPostedspc(transferForm.getHidPostedOthSpc());
            }

            String sql = "INSERT INTO EMP_TRANSFER (NOT_ID, NOT_TYPE, EMP_ID,DEPT_CODE,OFF_CODE, NEXT_SPC,IF_DEPLOYED,FIELD_OFF_CODE,entry_taken_by,entry_taken_on) VALUES(?,?,?,?,?,?,?,?,?,?)";
            pst = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            //pst.setString(1, maxTransferIdDao.getMaxTransferId()); //CommonFunctions.getMaxCode("EMP_TRANSFER", "TR_ID", con));
            pst.setInt(1, notid);
            pst.setString(2, transferForm.getHnotType());
            pst.setString(3, transferForm.getEmpid());
            pst.setString(4, transferForm.getHidPostedDeptCode());
            pst.setString(5, transferForm.getHidPostedOffCode());
            pst.setString(6, transferForm.getPostedspc());
            pst.setString(7, "");
            pst.setString(8, transferForm.getSltPostedFieldOff());
            pst.setString(9, loginempid);
            pst.setTimestamp(10, new Timestamp(new Date().getTime()));
            pst.executeUpdate();

            ResultSet rs = pst.getGeneratedKeys();
            rs.next();
            int trid = rs.getInt("TR_ID");

            DataBaseFunctions.closeSqlObjects(rs, pst);

            String sqlString = ToStringBuilder.reflectionToString(pst);
            pst = con.prepareStatement("UPDATE EMP_TRANSFER SET query_string=? WHERE TR_ID=? AND EMP_ID=?");
            pst.setString(1, sqlString);
            pst.setInt(2, trid);
            pst.setString(3, transferForm.getEmpid());
            pst.executeUpdate();

            DataBaseFunctions.closeSqlObjects(pst);

            if (transferForm.getRdTransaction() != null && transferForm.getRdTransaction().equals("C")) {
                sql = "UPDATE EMP_MAST SET NEXT_OFFICE_CODE=?,pay_commission=? WHERE EMP_ID=?";
                pst = con.prepareStatement(sql);
                pst.setString(1, transferForm.getHidPostedOffCode());
                if (transferForm.getRdoPaycomm() != null && !transferForm.getRdoPaycomm().equals("")) {
                    pst.setInt(2, Integer.parseInt(transferForm.getRdoPaycomm()));
                } else {
                    pst.setInt(2, 0);
                }
                pst.setString(3, transferForm.getEmpid());
                pst.executeUpdate();

                DataBaseFunctions.closeSqlObjects(pst);

                sqlString = ToStringBuilder.reflectionToString(pst);
                sbDAO.createTransactionLog(loginuserid, transferForm.getEmpid(), "TRANSFER", sqlString);
            }

            epayrecordform.setEmpid(transferForm.getEmpid());
            epayrecordform.setNot_id(notid);
            epayrecordform.setNot_type(transferForm.getHnotType());
            epayrecordform.setPayscale(transferForm.getSltPayScale());
            epayrecordform.setBasic(transferForm.getTxtBasic());
            epayrecordform.setGp(transferForm.getTxtGP());
            epayrecordform.setS_pay(transferForm.getTxtSP());
            epayrecordform.setP_pay(transferForm.getTxtPP());
            epayrecordform.setOth_pay(transferForm.getTxtOP());
            epayrecordform.setOth_desc(transferForm.getTxtDescOP());
            epayrecordform.setWefDt(transferForm.getTxtWEFDt());
            epayrecordform.setWefTime(transferForm.getSltWEFTime());
            epayrecordform.setPayLevel(transferForm.getPayLevel());
            epayrecordform.setPayCell(transferForm.getPayCell());

            emppayrecordDAO.saveEmpPayRecordData(epayrecordform);

            /*
             * Updating the Service Book Language
             */
            if (transferForm.getChkNotSBPrint() != null && transferForm.getChkNotSBPrint().equals("Y")) {
                pst = con.prepareStatement("UPDATE EMP_NOTIFICATION SET IF_VISIBLE='N' WHERE NOT_ID=?");
                pst.setInt(1, notid);
                pst.execute();
            } else if (transferForm.getChkNotSBPrint() == null || transferForm.getChkNotSBPrint().equals("")) {
                String sbLang = sbDAO.getTransferDetails(transferForm, notid, transferForm.getHnotType());
                /* pst = con.prepareStatement("UPDATE EMP_NOTIFICATION SET SB_DESCRIPTION =?,IF_VISIBLE='Y' WHERE NOT_ID=? ");
                 pst.setString(1, sbLang);
                 pst.setInt(2, notid);*/

                pst = con.prepareStatement("UPDATE EMP_NOTIFICATION SET IF_VISIBLE='Y' WHERE NOT_ID=? ");
                pst.setInt(1, notid);
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
    public void saveTransfer(TransferForm transferForm, int notid, String loginempid, String loginuserid) {

        Connection con = null;

        PreparedStatement pst = null;

        EmpPayRecordForm epayrecordform = new EmpPayRecordForm();

        try {
            con = this.dataSource.getConnection();

            if (transferForm.getRadpostingauthtype() != null && transferForm.getRadpostingauthtype().equals("GOI")) {
                transferForm.setPostedspc(transferForm.getHidPostedOthSpc());
            }

            String sql = "INSERT INTO EMP_TRANSFER (NOT_ID, NOT_TYPE, EMP_ID,DEPT_CODE,OFF_CODE, NEXT_SPC,IF_DEPLOYED,FIELD_OFF_CODE,entry_taken_by,entry_taken_on) VALUES(?,?,?,?,?,?,?,?,?,?)";
            pst = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            //pst.setString(1, maxTransferIdDao.getMaxTransferId()); //CommonFunctions.getMaxCode("EMP_TRANSFER", "TR_ID", con));
            pst.setInt(1, notid);
            pst.setString(2, transferForm.getHnotType());
            pst.setString(3, transferForm.getEmpid());
            pst.setString(4, transferForm.getHidPostedDeptCode());
            pst.setString(5, transferForm.getHidPostedOffCode());
            pst.setString(6, transferForm.getPostedspc());
            pst.setString(7, "");
            pst.setString(8, transferForm.getSltPostedFieldOff());
            pst.setString(9, loginempid);
            pst.setTimestamp(10, new Timestamp(new Date().getTime()));
            pst.executeUpdate();

            ResultSet rs = pst.getGeneratedKeys();
            rs.next();
            int trid = rs.getInt("TR_ID");

            DataBaseFunctions.closeSqlObjects(rs, pst);

            String sqlString = ToStringBuilder.reflectionToString(pst);
            pst = con.prepareStatement("UPDATE EMP_TRANSFER SET query_string=? WHERE TR_ID=? AND EMP_ID=?");
            pst.setString(1, sqlString);
            pst.setInt(2, trid);
            pst.setString(3, transferForm.getEmpid());
            pst.executeUpdate();

            DataBaseFunctions.closeSqlObjects(pst);

            if (transferForm.getRdTransaction() != null && transferForm.getRdTransaction().equals("C")) {
                sql = "UPDATE EMP_MAST SET NEXT_OFFICE_CODE=?,pay_commission=? WHERE EMP_ID=?";
                pst = con.prepareStatement(sql);
                pst.setString(1, transferForm.getHidPostedOffCode());
                if (transferForm.getRdoPaycomm() != null && !transferForm.getRdoPaycomm().equals("")) {
                    pst.setInt(2, Integer.parseInt(transferForm.getRdoPaycomm()));
                } else {
                    pst.setInt(2, 0);
                }
                pst.setString(3, transferForm.getEmpid());
                pst.executeUpdate();

                DataBaseFunctions.closeSqlObjects(pst);

                sqlString = ToStringBuilder.reflectionToString(pst);
                sbDAO.createTransactionLog(loginuserid, transferForm.getEmpid(), "TRANSFER", sqlString);
            }

            epayrecordform.setEmpid(transferForm.getEmpid());
            epayrecordform.setNot_id(notid);
            epayrecordform.setNot_type(transferForm.getHnotType());
            epayrecordform.setPayscale(transferForm.getSltPayScale());
            epayrecordform.setBasic(transferForm.getTxtBasic());
            epayrecordform.setGp(transferForm.getTxtGP());
            epayrecordform.setS_pay(transferForm.getTxtSP());
            epayrecordform.setP_pay(transferForm.getTxtPP());
            epayrecordform.setOth_pay(transferForm.getTxtOP());
            epayrecordform.setOth_desc(transferForm.getTxtDescOP());
            epayrecordform.setWefDt(transferForm.getTxtWEFDt());
            epayrecordform.setWefTime(transferForm.getSltWEFTime());
            epayrecordform.setPayLevel(transferForm.getPayLevel());
            epayrecordform.setPayCell(transferForm.getPayCell());

            emppayrecordDAO.saveEmpPayRecordData(epayrecordform);

            /*
             * Updating the Service Book Language
             */
            if (transferForm.getChkNotSBPrint() != null && transferForm.getChkNotSBPrint().equals("Y")) {
                pst = con.prepareStatement("UPDATE EMP_NOTIFICATION SET IF_VISIBLE='N' WHERE NOT_ID=?");
                pst.setInt(1, notid);
                pst.execute();
            } else if (transferForm.getChkNotSBPrint() == null || transferForm.getChkNotSBPrint().equals("")) {
                String sbLang = sbDAO.getTransferDetails(transferForm, notid, transferForm.getHnotType());
                pst = con.prepareStatement("UPDATE EMP_NOTIFICATION SET SB_DESCRIPTION =?,IF_VISIBLE='Y' WHERE NOT_ID=? ");
                pst.setString(1, sbLang);
                pst.setInt(2, notid);

//                pst = con.prepareStatement("UPDATE EMP_NOTIFICATION SET IF_VISIBLE='Y' WHERE NOT_ID=? ");
//                pst.setInt(1, notid);
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
    public void updateTransfer(TransferForm transferForm, String loginempid, String loginuserid) {

        Connection con = null;

        PreparedStatement pst = null;

        EmpPayRecordForm epayrecordform = new EmpPayRecordForm();
        try {
            con = this.dataSource.getConnection();

            if (transferForm.getRadpostingauthtype() != null && transferForm.getRadpostingauthtype().equals("GOI")) {
                transferForm.setPostedspc(transferForm.getHidPostedOthSpc());
            }

            String sql = "UPDATE EMP_TRANSFER SET DEPT_CODE=?,OFF_CODE=?, NEXT_SPC=?,IF_DEPLOYED=?,FIELD_OFF_CODE=?,entry_taken_by=?,entry_taken_on=? WHERE EMP_ID=? AND TR_ID=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, transferForm.getHidPostedDeptCode());
            pst.setString(2, transferForm.getHidPostedOffCode());
            pst.setString(3, transferForm.getPostedspc());
            pst.setString(4, "");
            pst.setString(5, transferForm.getSltPostedFieldOff());
            pst.setString(6, loginempid);
            pst.setTimestamp(7, new Timestamp(new Date().getTime()));
            pst.setString(8, transferForm.getEmpid());
            pst.setInt(9, Integer.parseInt(transferForm.getTransferId()));
            pst.executeUpdate();

            DataBaseFunctions.closeSqlObjects(pst);

            String sqlString = ToStringBuilder.reflectionToString(pst);
            pst = con.prepareStatement("UPDATE EMP_TRANSFER SET query_string=? WHERE TR_ID=? AND EMP_ID=?");
            pst.setString(1, sqlString);
            pst.setInt(2, Integer.parseInt(transferForm.getTransferId()));
            pst.setString(3, transferForm.getEmpid());
            pst.executeUpdate();

            DataBaseFunctions.closeSqlObjects(pst);

            if (transferForm.getRdTransaction() != null && transferForm.getRdTransaction().equals("C")) {
                sql = "UPDATE EMP_MAST SET NEXT_OFFICE_CODE=?,pay_commission=? WHERE EMP_ID=?";
                pst = con.prepareStatement(sql);
                pst.setString(1, transferForm.getHidPostedOffCode());
                if (transferForm.getRdoPaycomm() != null && !transferForm.getRdoPaycomm().equals("")) {
                    pst.setInt(2, Integer.parseInt(transferForm.getRdoPaycomm()));
                } else {
                    pst.setInt(2, 0);
                }
                pst.setString(3, transferForm.getEmpid());
                pst.executeUpdate();

                DataBaseFunctions.closeSqlObjects(pst);

                sqlString = ToStringBuilder.reflectionToString(pst);
                sbDAO.createTransactionLog(loginuserid, transferForm.getEmpid(), "TRANSFER", sqlString);
            }

            epayrecordform.setPayid(transferForm.getHpayid());
            epayrecordform.setEmpid(transferForm.getEmpid());
            epayrecordform.setPayscale(transferForm.getSltPayScale());
            epayrecordform.setBasic(transferForm.getTxtBasic());
            epayrecordform.setGp(transferForm.getTxtGP());
            epayrecordform.setS_pay(transferForm.getTxtSP());
            epayrecordform.setP_pay(transferForm.getTxtPP());
            epayrecordform.setOth_pay(transferForm.getTxtOP());
            epayrecordform.setOth_desc(transferForm.getTxtDescOP());
            epayrecordform.setWefDt(transferForm.getTxtWEFDt());
            epayrecordform.setWefTime(transferForm.getSltWEFTime());
            epayrecordform.setPayLevel(transferForm.getPayLevel());
            epayrecordform.setPayCell(transferForm.getPayCell());
            emppayrecordDAO.updateEmpPayRecordData(epayrecordform);

            /*
             * Updating the Service Book Language
             */
            if (transferForm.getChkNotSBPrint() != null && transferForm.getChkNotSBPrint().equals("Y")) {
                pst = con.prepareStatement("UPDATE EMP_NOTIFICATION SET IF_VISIBLE='N' WHERE NOT_ID=? ");
                pst.setInt(1, transferForm.getHnotid());
                pst.execute();
            } else if ((transferForm.getChkNotSBPrint() == null || transferForm.getChkNotSBPrint().equals("")) && (transferForm.getHnotType().equalsIgnoreCase("TRANSFER") || transferForm.getHnotType().equalsIgnoreCase("POSTING"))) {
                String sbLang = sbDAO.getTransferDetails(transferForm, transferForm.getHnotid(), transferForm.getHnotType());
                pst = con.prepareStatement("UPDATE EMP_NOTIFICATION SET SB_DESCRIPTION =?,IF_VISIBLE='Y' WHERE NOT_ID=? ");
                pst.setString(1, sbLang);
                pst.setInt(2, transferForm.getHnotid());
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
    public void updateTransferDeputation(TransferForm transferForm, String loginempid, String loginuserid) {

        Connection con = null;

        PreparedStatement pst = null;

        EmpPayRecordForm epayrecordform = new EmpPayRecordForm();
        try {
            con = this.dataSource.getConnection();

            if (transferForm.getRadpostingauthtype() != null && transferForm.getRadpostingauthtype().equals("GOI")) {
                transferForm.setPostedspc(transferForm.getHidPostedOthSpc());
            }

            String sql = "UPDATE EMP_TRANSFER SET DEPT_CODE=?,OFF_CODE=?, NEXT_SPC=?,IF_DEPLOYED=?,FIELD_OFF_CODE=?,entry_taken_by=?,entry_taken_on=? WHERE EMP_ID=? AND TR_ID=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, transferForm.getHidPostedDeptCode());
            pst.setString(2, transferForm.getHidPostedOffCode());
            pst.setString(3, transferForm.getPostedspc());
            pst.setString(4, "");
            pst.setString(5, transferForm.getSltPostedFieldOff());
            pst.setString(6, loginempid);
            pst.setTimestamp(7, new Timestamp(new Date().getTime()));
            pst.setString(8, transferForm.getEmpid());
            pst.setInt(9, Integer.parseInt(transferForm.getTransferId()));
            pst.executeUpdate();

            DataBaseFunctions.closeSqlObjects(pst);

            String sqlString = ToStringBuilder.reflectionToString(pst);
            pst = con.prepareStatement("UPDATE EMP_TRANSFER SET query_string=? WHERE TR_ID=? AND EMP_ID=?");
            pst.setString(1, sqlString);
            pst.setInt(2, Integer.parseInt(transferForm.getTransferId()));
            pst.setString(3, transferForm.getEmpid());
            pst.executeUpdate();

            DataBaseFunctions.closeSqlObjects(pst);

            if (transferForm.getRdTransaction() != null && transferForm.getRdTransaction().equals("C")) {
                sql = "UPDATE EMP_MAST SET NEXT_OFFICE_CODE=?,pay_commission=? WHERE EMP_ID=?";
                pst = con.prepareStatement(sql);
                pst.setString(1, transferForm.getHidPostedOffCode());
                if (transferForm.getRdoPaycomm() != null && !transferForm.getRdoPaycomm().equals("")) {
                    pst.setInt(2, Integer.parseInt(transferForm.getRdoPaycomm()));
                } else {
                    pst.setInt(2, 0);
                }
                pst.setString(3, transferForm.getEmpid());
                pst.executeUpdate();

                DataBaseFunctions.closeSqlObjects(pst);

                sqlString = ToStringBuilder.reflectionToString(pst);
                sbDAO.createTransactionLog(loginuserid, transferForm.getEmpid(), "TRANSFER", sqlString);
            }

            epayrecordform.setPayid(transferForm.getHpayid());
            epayrecordform.setEmpid(transferForm.getEmpid());
            epayrecordform.setPayscale(transferForm.getSltPayScale());
            epayrecordform.setBasic(transferForm.getTxtBasic());
            epayrecordform.setGp(transferForm.getTxtGP());
            epayrecordform.setS_pay(transferForm.getTxtSP());
            epayrecordform.setP_pay(transferForm.getTxtPP());
            epayrecordform.setOth_pay(transferForm.getTxtOP());
            epayrecordform.setOth_desc(transferForm.getTxtDescOP());
            epayrecordform.setWefDt(transferForm.getTxtWEFDt());
            epayrecordform.setWefTime(transferForm.getSltWEFTime());
            epayrecordform.setPayLevel(transferForm.getPayLevel());
            epayrecordform.setPayCell(transferForm.getPayCell());
            emppayrecordDAO.updateEmpPayRecordData(epayrecordform);

            /*
             * Updating the Service Book Language
             */
            if (transferForm.getChkNotSBPrint() != null && transferForm.getChkNotSBPrint().equals("Y")) {
                pst = con.prepareStatement("UPDATE EMP_NOTIFICATION SET IF_VISIBLE='N' WHERE NOT_ID=? ");
                pst.setInt(1, transferForm.getHnotid());
                pst.execute();
            } else if ((transferForm.getChkNotSBPrint() == null || transferForm.getChkNotSBPrint().equals("")) && (transferForm.getHnotType().equalsIgnoreCase("TRANSFER") || transferForm.getHnotType().equalsIgnoreCase("POSTING"))) {
                String sbLang = sbDAO.getTransferDetails(transferForm, transferForm.getHnotid(), transferForm.getHnotType());
                pst = con.prepareStatement("UPDATE EMP_NOTIFICATION SET SB_DESCRIPTION =?,IF_VISIBLE='Y' WHERE NOT_ID=? ");
                pst.setString(1, sbLang);
                pst.setInt(2, transferForm.getHnotid());
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
    public void deleteTransfer(TransferForm transferForm) {

        Connection con = null;

        PreparedStatement pst = null;

        EmpPayRecordForm epayrecordform = new EmpPayRecordForm();
        try {
            con = this.dataSource.getConnection();

            String sql = "DELETE FROM EMP_TRANSFER WHERE EMP_ID=? AND TR_ID=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, transferForm.getEmpid());
            pst.setInt(2, Integer.parseInt(transferForm.getTransferId()));
            pst.executeUpdate();

            DataBaseFunctions.closeSqlObjects(pst);

            epayrecordform.setEmpid(transferForm.getEmpid());
            epayrecordform.setPayid(transferForm.getHpayid());
            emppayrecordDAO.deleteEmpPayRecordData(epayrecordform);

            sql = "update emp_mast set NEXT_OFFICE_CODE=null where emp_id=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, transferForm.getEmpid());
            pst.executeUpdate();

            DataBaseFunctions.closeSqlObjects(pst);

            //Delete from Emp Join
            sql = "DELETE FROM emp_join WHERE not_id = '" + transferForm.getHnotid() + "' AND not_type = '" + transferForm.getHnotType() + "'";
            pst = con.prepareStatement(sql);
            pst.executeUpdate();

            DataBaseFunctions.closeSqlObjects(pst);

            //Delete from Emp Relieve
            sql = "DELETE FROM emp_relieve WHERE not_id = '" + transferForm.getHnotid() + "' AND not_type = '" + transferForm.getHnotType() + "'";
            pst = con.prepareStatement(sql);
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public int getTransferListCount(String empid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        int count = 0;
        try {
            con = this.dataSource.getConnection();

            String sql = "select count(*) cnt from (select emp_id,doe,not_id,not_type,ordno,orddt from emp_notification where emp_id=?"
                    + " and not_type='TRANSFER')temp";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
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
    public List getTransferList(String empid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List transferlist = new ArrayList();
        TransferBean tbean = null;
        try {
            con = this.dataSource.getConnection();

            /*String sql = "select emp_notification.is_validated,emp_notification.modified_by,emp_notification.modified_on,tr_id,emp_notification.doe,emp_notification.not_id,emp_transfer.not_type,ordno,orddt,off_en,relieve_id,emp_notification.if_visible from"
             + " (select is_validated,modified_by,modified_on,emp_id,doe,not_id,not_type,ordno,orddt,if_visible from emp_notification where emp_id=? and not_type='TRANSFER')emp_notification"
             + " left outer join emp_transfer on emp_notification.not_id=emp_transfer.not_id"
             + " left outer join emp_relieve on emp_notification.not_id=emp_relieve.not_id"
             + " left outer join g_office on emp_transfer.off_code=g_office.off_code order by orddt desc";*/
            String sql = "select e2.not_id linkid ,e2.not_type canceltype,e1.is_validated,e1.modified_by,e1.modified_on,tr_id,e1.doe,e1.not_id,emp_transfer.not_type,e1.ordno,e1.orddt,off_en,relieve_id,e1.if_visible from"
                    + " (select link_id,is_validated,modified_by,modified_on,emp_id,doe,not_id,not_type,ordno,orddt,if_visible from emp_notification where emp_id=? and not_type='TRANSFER')e1"
                    + " left outer join emp_transfer on e1.not_id=emp_transfer.not_id"
                    + " left outer join emp_relieve on e1.not_id=emp_relieve.not_id"
                    + " left outer join emp_notification e2 on e1.link_id=e2.not_id"
                    + " left outer join g_office on emp_transfer.off_code=g_office.off_code order by orddt desc;";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            rs = pst.executeQuery();
            while (rs.next()) {
                tbean = new TransferBean();
                tbean.setTransferId(rs.getString("tr_id"));
                tbean.setHnotid(rs.getString("not_id"));
                tbean.setNotType(rs.getString("not_type"));
                tbean.setDoe(CommonFunctions.getFormattedOutputDate1(rs.getDate("DOE")));
                tbean.setOrdno(rs.getString("ordno"));
                tbean.setOrdt(CommonFunctions.getFormattedOutputDate1(rs.getDate("orddt")));
                tbean.setTransferToOffice(rs.getString("off_en"));
                tbean.setHrlvid(rs.getString("relieve_id"));
                tbean.setIfVisible(rs.getString("if_visible"));
                if (rs.getString("modified_by") != null && !rs.getString("modified_by").equals("")) {
                    tbean.setModifiedBy(rs.getString("modified_by") + "<br />" + CommonFunctions.getFormattedOutputDate1(rs.getDate("modified_on")));
                } else {
                    tbean.setModifiedBy("");
                }
                tbean.setIsValidated(rs.getString("is_validated"));

                tbean.setCanceltype(rs.getString("canceltype"));
                tbean.setCancelnotid(rs.getString("linkid"));

                transferlist.add(tbean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return transferlist;
    }

    @Override
    public TransferForm getEmpTransferData(TransferForm trform, int notificationId) throws SQLException {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = this.dataSource.getConnection();

            String sql = "select"
                    + " organization_type,organization_type_posting,emp_notification.if_visible,spc1.gpc transGPC,spc2.gpc authGPC,office1.dist_code transDistCode,office2.dist_code authDistCode,emp_notification.not_id,ordno,orddt,emp_notification.dept_code notDeptCode,emp_notification.off_code notOffCode,"
                    + " auth,note,tr_id,emp_transfer.dept_code transDeptCode,emp_transfer.off_code transOffCode,next_spc,field_off_code,pay_id,wef,weft,emp_pay_record.pay_scale,"
                    + " pay,s_pay,p_pay,oth_pay,emp_pay_record.gp,oth_desc,spc1.spn,pay_level,pay_cell from"
                    + " (SELECT not_id,tr_id,dept_code,off_code,next_spc,field_off_code FROM EMP_TRANSFER WHERE EMP_ID=? AND NOT_ID=?)emp_transfer"
                    + " inner join (select organization_type,organization_type_posting,not_id,ordno,orddt,dept_code,off_code,auth,note,if_visible from emp_notification where EMP_ID=? AND NOT_ID=?)emp_notification"
                    + " on emp_transfer.not_id=emp_notification.not_id"
                    + " left outer join (select pay_id,not_id,wef,weft,pay_scale,pay,s_pay,p_pay,oth_pay,gp,oth_desc,pay_level,pay_cell from emp_pay_record where EMP_ID=? AND NOT_ID=?)emp_pay_record"
                    + " on emp_notification.not_id=emp_pay_record.not_id"
                    + " left outer join g_spc spc1 on emp_transfer.next_spc=spc1.spc"
                    + " left outer join g_spc spc2 on emp_notification.auth=spc2.spc"
                    + " left outer join g_office office1 on emp_transfer.off_code=office1.off_code"
                    + " left outer join g_office office2 on emp_notification.off_code=office2.off_code";

            pst = con.prepareStatement(sql);
            pst.setString(1, trform.getEmpid());
            pst.setInt(2, notificationId);
            pst.setString(3, trform.getEmpid());
            pst.setInt(4, notificationId);
            pst.setString(5, trform.getEmpid());
            pst.setInt(6, notificationId);
            rs = pst.executeQuery();
            if (rs.next()) {
                if (rs.getString("if_visible") != null && rs.getString("if_visible").equals("N")) {
                    trform.setChkNotSBPrint("Y");
                } else {
                    trform.setChkNotSBPrint("N");
                }
                trform.setTransferId(rs.getString("tr_id"));
                trform.setTxtNotOrdNo(rs.getString("ordno"));
                trform.setTxtNotOrdDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("orddt")));
                trform.setRadsanctioningauthtype(rs.getString("organization_type"));
                trform.setHidAuthDeptCode(rs.getString("notDeptCode"));
                trform.setHidAuthDistCode(rs.getString("authDistCode"));
                trform.setHidAuthOffCode(rs.getString("notOffCode"));
                trform.setGenericpostAuth(rs.getString("authGPC"));
                trform.setHidTempAuthOffCode(rs.getString("notOffCode"));
                if (trform.getRadsanctioningauthtype() != null && trform.getRadsanctioningauthtype().equals("GOI")) {
                    trform.setHidSanctioningOthSpc(rs.getString("auth"));
                    trform.setAuthPostName(getOtherSpn(rs.getString("auth")));
                } else {
                    trform.setRadsanctioningauthtype("GOO");
                    trform.setAuthSpc(rs.getString("auth"));
                    trform.setHidTempAuthPost(rs.getString("auth"));
                    trform.setAuthPostName(CommonFunctions.getSPN(con, rs.getString("auth")));
                }
                trform.setRdAuthType("GOO");

                trform.setRadpostingauthtype(rs.getString("organization_type_posting"));
                trform.setHidPostedDeptCode(rs.getString("transDeptCode"));
                trform.setHidPostedDistCode(rs.getString("transDistCode"));
                trform.setHidPostedOffCode(rs.getString("transOffCode"));
                trform.setHidTempPostedOffCode(rs.getString("transOffCode"));
                trform.setGenericpostPosted(rs.getString("transGPC"));
                if (trform.getRadpostingauthtype() != null && trform.getRadpostingauthtype().equals("GOI")) {
                    trform.setHidPostedOthSpc(rs.getString("next_spc"));
                    trform.setPostedPostName(getOtherSpn(rs.getString("next_spc")));
                } else {
                    trform.setRadpostingauthtype("GOO");
                    trform.setPostedspc(rs.getString("next_spc"));
                    trform.setHidTempPostedPost(rs.getString("next_spc"));
                    trform.setPostedPostName(rs.getString("SPN"));
                }
                trform.setSltPostedFieldOff(rs.getString("field_off_code"));
                trform.setHidTempPostedFieldOffCode(rs.getString("field_off_code"));

                trform.setRdPostedAuthType("GOO");

                trform.setHpayid(rs.getInt("pay_id"));
                trform.setSltPayScale(rs.getString("pay_scale"));
                trform.setTxtGP(rs.getString("gp"));
                trform.setPayLevel(rs.getString("pay_level"));
                trform.setPayCell(rs.getString("pay_cell"));
                trform.setPayLevelBacklog(rs.getString("pay_level"));
                trform.setPayCellBacklog(rs.getString("pay_cell"));
                trform.setTxtBasic(rs.getString("pay"));
                trform.setTxtSP(rs.getString("s_pay"));
                trform.setTxtPP(rs.getString("p_pay"));
                trform.setTxtOP(rs.getString("oth_pay"));
                trform.setTxtDescOP(rs.getString("oth_desc"));
                trform.setTxtWEFDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("wef")));
                trform.setSltWEFTime(rs.getString("weft"));
                trform.setNote(rs.getString("note"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return trform;
    }

    public OtherSPCBean getOthSPCData(String spc) throws SQLException {

        Connection con = null;

        Statement stmt = null;
        ResultSet rs = null;

        OtherSPCBean othbean = new OtherSPCBean();
        try {
            con = this.dataSource.getConnection();

            if (spc != null && !spc.equals("") && !(spc.length() < 3)) {
                if (spc.substring(0, 3).equalsIgnoreCase("GOI") || spc.substring(0, 3).equalsIgnoreCase("OSG") || spc.substring(0, 3).equalsIgnoreCase("FRB") || spc.substring(0, 3).equalsIgnoreCase("ORG")) {
                    /*check if exist in G_SPC*/
                    boolean bln = false;
                    rs = stmt.executeQuery("Select * from G_SPC where SPC='" + spc + "' and (ifuclean='N' or ifuclean is null) order by SPN ");
                    if (rs.next()) {
                        bln = true;
                        othbean.setOthDeptCode(rs.getString("DEPT_CODE"));
                        othbean.setOthOffCode(rs.getString("OFF_CODE"));
                        othbean.setOthPostCode(rs.getString("SPC"));
                    }
                    /*check if exist in G_SPC*/

                    if (bln == false) {
                        rs = null;
                        rs = stmt.executeQuery("SELECT * FROM G_OTH_SPC WHERE OTH_SPC='" + spc + "' order by AUTH_NAME");
                        if (rs.next()) {
                            if (rs.getString("DEPT_NAME") != null && !rs.getString("DEPT_NAME").equalsIgnoreCase("")) {
                                othbean.setOthDeptCode(rs.getString("DEPT_NAME"));
                                othbean.setOthDeptName(rs.getString("DEPT_NAME"));
                            }
                            if (rs.getString("OFF_EN") != null && !rs.getString("OFF_EN").equalsIgnoreCase("")) {
                                othbean.setOthOffCode(rs.getString("OFF_EN"));
                                othbean.setOthOffName(rs.getString("OFF_EN"));
                            }
                            if (rs.getString("AUTH_NAME") != null && !rs.getString("AUTH_NAME").equalsIgnoreCase("")) {
                                othbean.setOthPostName(rs.getString("AUTH_NAME"));
                            }
                            String othspn = null;
                            if ((rs.getString("AUTH_NAME") == null || rs.getString("AUTH_NAME").equalsIgnoreCase(""))
                                    && (rs.getString("OFF_EN") != null && !rs.getString("OFF_EN").equalsIgnoreCase(""))
                                    && (rs.getString("DEPT_NAME") != null && !rs.getString("DEPT_NAME").equalsIgnoreCase(""))) {
                                othspn = rs.getString("OFF_EN") + ", " + rs.getString("DEPT_NAME");
                            }
                            if ((rs.getString("AUTH_NAME") == null || rs.getString("AUTH_NAME").equalsIgnoreCase(""))
                                    && (rs.getString("OFF_EN") != null && !rs.getString("OFF_EN").equalsIgnoreCase(""))
                                    && (rs.getString("DEPT_NAME") == null || rs.getString("DEPT_NAME").equalsIgnoreCase(""))) {
                                othspn = rs.getString("OFF_EN");
                            }
                            if ((rs.getString("AUTH_NAME") == null || rs.getString("AUTH_NAME").equalsIgnoreCase(""))
                                    && (rs.getString("OFF_EN") == null || rs.getString("OFF_EN").equalsIgnoreCase(""))
                                    && (rs.getString("DEPT_NAME") != null && !rs.getString("DEPT_NAME").equalsIgnoreCase(""))) {
                                othspn = rs.getString("DEPT_NAME");
                            }
                            if ((rs.getString("AUTH_NAME") != null && !rs.getString("AUTH_NAME").equalsIgnoreCase(""))
                                    && (rs.getString("OFF_EN") == null || rs.getString("OFF_EN").equalsIgnoreCase(""))
                                    && (rs.getString("DEPT_NAME") == null || rs.getString("DEPT_NAME").equalsIgnoreCase(""))) {
                                othspn = rs.getString("AUTH_NAME");
                            } else if ((rs.getString("AUTH_NAME") != null && !rs.getString("AUTH_NAME").equalsIgnoreCase(""))
                                    && (rs.getString("OFF_EN") == null || rs.getString("OFF_EN").equalsIgnoreCase(""))
                                    && (rs.getString("DEPT_NAME") != null && !rs.getString("DEPT_NAME").equalsIgnoreCase(""))) {
                                othspn = rs.getString("AUTH_NAME") + ", " + rs.getString("DEPT_NAME");
                            } else if ((rs.getString("AUTH_NAME") != null && !rs.getString("AUTH_NAME").equalsIgnoreCase(""))
                                    && (rs.getString("OFF_EN") != null && !rs.getString("OFF_EN").equalsIgnoreCase(""))
                                    && (rs.getString("DEPT_NAME") == null || rs.getString("DEPT_NAME").equalsIgnoreCase(""))) {
                                othspn = rs.getString("AUTH_NAME") + ", " + rs.getString("OFF_EN");
                            } else if ((rs.getString("AUTH_NAME") != null && !rs.getString("AUTH_NAME").equalsIgnoreCase(""))
                                    && (rs.getString("OFF_EN") != null && !rs.getString("OFF_EN").equalsIgnoreCase(""))
                                    && (rs.getString("DEPT_NAME") != null && !rs.getString("DEPT_NAME").equalsIgnoreCase(""))) {
                                othspn = rs.getString("AUTH_NAME") + ", " + rs.getString("OFF_EN") + ", " + rs.getString("DEPT_NAME");
                            }
                        }
                    }
                } else {
                    rs = stmt.executeQuery("Select * from G_SPC where SPC='" + spc + "' and (ifuclean='N' or ifuclean is null) order by SPN ");
                    while (rs.next()) {
                        othbean.setOthDeptCode(rs.getString("DEPT_CODE"));
                        othbean.setOthOffCode(rs.getString("OFF_CODE"));
                        othbean.setOthPostCode(rs.getString("SPC"));
                        othbean.setOthPostName(rs.getString("SPN"));
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, stmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return othbean;
    }

    @Override
    public List getPostList(String deptCode, String offCode) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        SelectOption so = null;
        List plist = new ArrayList();
        try {
            con = this.dataSource.getConnection();

            String sql = "SELECT SPC,SPN FROM G_SPC WHERE DEPT_CODE=? AND OFF_CODE=? ORDER BY SPN ASC";
            pst = con.prepareStatement(sql);
            pst.setString(1, deptCode);
            pst.setString(2, offCode);
            rs = pst.executeQuery();
            while (rs.next()) {
                so = new SelectOption();
                so.setLabel(rs.getString("SPN"));
                so.setValue(rs.getString("SPC"));
                plist.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return plist;
    }

    @Override
    public int getPostListCount(String deptCode, String offCode) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        int count = 0;
        try {
            con = this.dataSource.getConnection();

            String sql = "SELECT count(SPC) cnt FROM G_SPC WHERE DEPT_CODE=? AND OFF_CODE=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, deptCode);
            pst.setString(2, offCode);
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
    public String getCadreCode(String empid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String cadreCode = "";
        try {
            con = this.dataSource.getConnection();

            String sql = "SELECT CUR_CADRE_CODE FROM EMP_MAST WHERE EMP_ID=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            rs = pst.executeQuery();
            if (rs.next()) {
                cadreCode = rs.getString("CUR_CADRE_CODE");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return cadreCode;
    }

    @Override
    public LoginUserBean[] getTransferListOfficeWise(String offcode, int year, int month) {
        Connection con = null;
        PreparedStatement ps = null;
        List<LoginUserBean> li = new ArrayList<>();
        try {
            String sql = "select EMP.EMP_ID,CUR_OFF_CODE from emp_relieve RELV "
                    + "INNER JOIN EMP_JOIN EMPJOIN ON RELV.JOIN_ID=EMPJOIN.JOIN_ID "
                    + "inner join EMP_MAST EMP ON RELV.EMP_ID=EMP.EMP_ID "
                    + "where RELV.spc like '" + offcode + "%' AND EXTRACT(YEAR FROM RLV_DATE)=? AND EXTRACT(MONTH FROM RLV_DATE)=? AND EMP.CUR_OFF_CODE<>'" + offcode + "'";
            con = this.dataSource.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, year);
            ps.setInt(2, month);
            ResultSet rs = ps.executeQuery();
            LoginUserBean us = null;
            while (rs.next()) {
                us = new LoginUserBean();
                us.setLoginempid(rs.getString("emp_id"));
                us.setLoginoffcode(rs.getString("CUR_OFF_CODE"));
                li.add(us);
            }
            DataBaseFunctions.closeSqlObjects(rs);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        LoginUserBean frarray[] = li.toArray(new LoginUserBean[li.size()]);
        return frarray;
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
    public void saveCancelTransfer(NotificationBean nb, TransferForm transferForm, int notid) {

        Connection con = null;
        PreparedStatement pst = null;

        try {
            con = this.dataSource.getConnection();

            if (transferForm.getChkNotSBPrint() != null && transferForm.getChkNotSBPrint().equals("Y")) {
                pst = con.prepareStatement("UPDATE EMP_NOTIFICATION SET IF_VISIBLE='N' WHERE NOT_ID=?");
                pst.setInt(1, notid);
                pst.execute();
            } else if (transferForm.getChkNotSBPrint() == null || transferForm.getChkNotSBPrint().equals("")) {
                nb.setNottype("TRANSFER");
                String sbCancelLang = sbDAO.getCancelLangDetails(nb, notid, transferForm.getHidSanctioningOthSpc());
                pst = con.prepareStatement("UPDATE EMP_NOTIFICATION SET SB_DESCRIPTION=?,IF_VISIBLE='Y' WHERE NOT_ID=?");
                pst.setString(1, sbCancelLang);
                pst.setInt(2, notid);
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
    public TransferForm getCancelTransferData(TransferForm trform, int notificationId) throws SQLException {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = this.dataSource.getConnection();

            String sql = "select e2.not_id linkid,e1.organization_type,e1.organization_type_posting,e1.if_visible,g_spc.gpc authGPC,g_office.dist_code authDistCode,"
                    + " e1.not_id,e1.ordno,e1.orddt,e1.dept_code notDeptCode,e1.off_code notOffCode,"
                    + " e1.auth,e1.note from emp_notification e1"
                    + " inner join emp_notification e2 on e1.not_id=e2.link_id"
                    + " left outer join g_spc on e1.auth=g_spc.spc"
                    + " left outer join g_office on e1.off_code=g_office.off_code"
                    + " where e1.EMP_ID=? AND e1.NOT_ID=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, trform.getEmpid());
            pst.setInt(2, notificationId);
            rs = pst.executeQuery();
            if (rs.next()) {
                if (rs.getString("if_visible") != null && rs.getString("if_visible").equals("N")) {
                    trform.setChkNotSBPrint("Y");
                } else {
                    trform.setChkNotSBPrint("N");
                }
                trform.setHnotid(rs.getInt("not_id"));
                trform.setLinkid(rs.getString("linkid"));
                trform.setTxtNotOrdNo(rs.getString("ordno"));
                trform.setTxtNotOrdDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("orddt")));
                trform.setRadsanctioningauthtype(rs.getString("organization_type"));
                trform.setHidAuthDeptCode(rs.getString("notDeptCode"));
                trform.setHidAuthDistCode(rs.getString("authDistCode"));
                trform.setHidAuthOffCode(rs.getString("notOffCode"));
                trform.setGenericpostAuth(rs.getString("authGPC"));
                if (trform.getRadsanctioningauthtype() != null && trform.getRadsanctioningauthtype().equals("GOI")) {
                    trform.setHidSanctioningOthSpc(rs.getString("auth"));
                    trform.setAuthPostName(getOtherSpn(rs.getString("auth")));
                } else {
                    trform.setRadsanctioningauthtype("GOO");
                    trform.setAuthSpc(rs.getString("auth"));
                    trform.setAuthPostName(CommonFunctions.getSPN(con, rs.getString("auth")));
                }
                trform.setNote(rs.getString("note"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return trform;
    }

    @Override
    public TransferForm getEmpSupersedeTransferData(TransferForm trform, int notificationId) throws SQLException {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = this.dataSource.getConnection();

            String sql = "select"
                    + " e1.not_id,e2.not_id linkid,e1.organization_type,e1.organization_type_posting,e1.if_visible,spc1.gpc transGPC,spc2.gpc authGPC,office1.dist_code transDistCode,office2.dist_code authDistCode,e1.not_id,e1.ordno,e1.orddt,e1.dept_code notDeptCode,e1.off_code notOffCode,"
                    + " e1.auth,e1.note,tr_id,emp_transfer.dept_code transDeptCode,emp_transfer.off_code transOffCode,next_spc,field_off_code,pay_id,wef,weft,emp_pay_record.pay_scale,"
                    + " pay,s_pay,p_pay,oth_pay,emp_pay_record.gp,oth_desc,spc1.spn,pay_level,pay_cell from"
                    + " (SELECT not_id,tr_id,dept_code,off_code,next_spc,field_off_code FROM EMP_TRANSFER WHERE EMP_ID=? AND NOT_ID=?)emp_transfer"
                    + " inner join (select organization_type,organization_type_posting,not_id,ordno,orddt,dept_code,off_code,auth,note,if_visible from emp_notification where EMP_ID=? AND NOT_ID=?)emp_notification e1"
                    + " on emp_transfer.not_id=emp_notification.not_id"
                    + " left outer join (select pay_id,not_id,wef,weft,pay_scale,pay,s_pay,p_pay,oth_pay,gp,oth_desc,pay_level,pay_cell from emp_pay_record where EMP_ID=? AND NOT_ID=?)emp_pay_record"
                    + " on emp_notification.not_id=emp_pay_record.not_id"
                    + " inner join emp_notification e2 on e1.not_id=e2.link_id"
                    + " left outer join g_spc spc1 on emp_transfer.next_spc=spc1.spc"
                    + " left outer join g_spc spc2 on emp_notification.auth=spc2.spc"
                    + " left outer join g_office office1 on emp_transfer.off_code=office1.off_code"
                    + " left outer join g_office office2 on emp_notification.off_code=office2.off_code";
            pst = con.prepareStatement(sql);
            pst.setString(1, trform.getEmpid());
            pst.setInt(2, notificationId);
            pst.setString(3, trform.getEmpid());
            pst.setInt(4, notificationId);
            pst.setString(5, trform.getEmpid());
            pst.setInt(6, notificationId);
            rs = pst.executeQuery();
            if (rs.next()) {
                if (rs.getString("if_visible") != null && rs.getString("if_visible").equals("N")) {
                    trform.setChkNotSBPrint("Y");
                } else {
                    trform.setChkNotSBPrint("N");
                }
                trform.setHnotid(rs.getInt("not_id"));
                trform.setTransferId(rs.getString("tr_id"));
                trform.setLinkid(rs.getString("linkid"));
                trform.setTxtNotOrdNo(rs.getString("ordno"));
                trform.setTxtNotOrdDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("orddt")));
                trform.setRadsanctioningauthtype(rs.getString("organization_type"));
                trform.setHidAuthDeptCode(rs.getString("notDeptCode"));
                trform.setHidAuthDistCode(rs.getString("authDistCode"));
                trform.setHidAuthOffCode(rs.getString("notOffCode"));
                trform.setGenericpostAuth(rs.getString("authGPC"));
                trform.setHidTempAuthOffCode(rs.getString("notOffCode"));
                if (trform.getRadsanctioningauthtype() != null && trform.getRadsanctioningauthtype().equals("GOI")) {
                    trform.setHidSanctioningOthSpc(rs.getString("auth"));
                    trform.setAuthPostName(getOtherSpn(rs.getString("auth")));
                } else {
                    trform.setRadsanctioningauthtype("GOO");
                    trform.setAuthSpc(rs.getString("auth"));
                    trform.setAuthPostName(CommonFunctions.getSPN(con, rs.getString("auth")));
                }
                trform.setRdAuthType("GOO");

                trform.setRadpostingauthtype(rs.getString("organization_type_posting"));
                trform.setHidPostedDeptCode(rs.getString("transDeptCode"));
                trform.setHidPostedDistCode(rs.getString("transDistCode"));
                trform.setHidPostedOffCode(rs.getString("transOffCode"));
                trform.setGenericpostPosted(rs.getString("transGPC"));
                if (trform.getRadpostingauthtype() != null && trform.getRadpostingauthtype().equals("GOI")) {
                    trform.setHidPostedOthSpc(rs.getString("next_spc"));
                    trform.setPostedPostName(getOtherSpn(rs.getString("next_spc")));
                } else {
                    trform.setRadpostingauthtype("GOO");
                    trform.setPostedspc(rs.getString("next_spc"));
                    trform.setPostedPostName(rs.getString("SPN"));
                }
                trform.setSltPostedFieldOff(rs.getString("field_off_code"));

                trform.setRdPostedAuthType("GOO");

                trform.setHpayid(rs.getInt("pay_id"));
                trform.setSltPayScale(rs.getString("pay_scale"));
                trform.setTxtGP(rs.getString("gp"));
                trform.setPayLevel(rs.getString("pay_level"));
                trform.setPayCell(rs.getString("pay_cell"));
                trform.setTxtBasic(rs.getString("pay"));
                trform.setTxtSP(rs.getString("s_pay"));
                trform.setTxtPP(rs.getString("p_pay"));
                trform.setTxtOP(rs.getString("oth_pay"));
                trform.setTxtDescOP(rs.getString("oth_desc"));
                trform.setTxtWEFDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("wef")));
                trform.setSltWEFTime(rs.getString("weft"));
                trform.setNote(rs.getString("note"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return trform;
    }

    @Override
    public void SaveSupersedeTransfer(TransferForm transferForm, int notid, String loginempid, String loginuserid) {

        Connection con = null;

        PreparedStatement pst = null;

        EmpPayRecordForm epayrecordform = new EmpPayRecordForm();

        try {
            con = this.dataSource.getConnection();

            if (transferForm.getRadpostingauthtype() != null && transferForm.getRadpostingauthtype().equals("GOI")) {
                transferForm.setPostedspc(transferForm.getHidPostedOthSpc());
            }

            String sql = "INSERT INTO EMP_TRANSFER (NOT_ID, NOT_TYPE, EMP_ID,DEPT_CODE,OFF_CODE, NEXT_SPC,IF_DEPLOYED,FIELD_OFF_CODE,entry_taken_by,entry_taken_on) VALUES(?,?,?,?,?,?,?,?,?,?)";
            pst = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            //pst.setString(1, maxTransferIdDao.getMaxTransferId()); //CommonFunctions.getMaxCode("EMP_TRANSFER", "TR_ID", con));
            pst.setInt(1, notid);
            pst.setString(2, transferForm.getHnotType());
            pst.setString(3, transferForm.getEmpid());
            pst.setString(4, transferForm.getHidPostedDeptCode());
            pst.setString(5, transferForm.getHidPostedOffCode());
            pst.setString(6, transferForm.getPostedspc());
            pst.setString(7, "");
            pst.setString(8, transferForm.getSltPostedFieldOff());
            pst.setString(9, loginempid);
            pst.setTimestamp(10, new Timestamp(new Date().getTime()));
            pst.executeUpdate();

            ResultSet rs = pst.getGeneratedKeys();
            rs.next();
            int trid = rs.getInt("TR_ID");

            DataBaseFunctions.closeSqlObjects(rs, pst);

            String sqlString = ToStringBuilder.reflectionToString(pst);
            pst = con.prepareStatement("UPDATE EMP_TRANSFER SET query_string=? WHERE TR_ID=? AND EMP_ID=?");
            pst.setString(1, sqlString);
            pst.setInt(2, trid);
            pst.setString(3, transferForm.getEmpid());
            pst.executeUpdate();

            DataBaseFunctions.closeSqlObjects(pst);

            epayrecordform.setEmpid(transferForm.getEmpid());
            epayrecordform.setNot_id(notid);
            epayrecordform.setNot_type(transferForm.getHnotType());
            epayrecordform.setPayscale(transferForm.getSltPayScale());
            epayrecordform.setBasic(transferForm.getTxtBasic());
            epayrecordform.setGp(transferForm.getTxtGP());
            epayrecordform.setS_pay(transferForm.getTxtSP());
            epayrecordform.setP_pay(transferForm.getTxtPP());
            epayrecordform.setOth_pay(transferForm.getTxtOP());
            epayrecordform.setOth_desc(transferForm.getTxtDescOP());
            epayrecordform.setWefDt(transferForm.getTxtWEFDt());
            epayrecordform.setWefTime(transferForm.getSltWEFTime());
            epayrecordform.setPayLevel(transferForm.getPayLevel());
            epayrecordform.setPayCell(transferForm.getPayCell());
            emppayrecordDAO.saveEmpPayRecordData(epayrecordform);

            /*
             * Updating the Service Book Language
             */
            if (transferForm.getChkNotSBPrint() != null && transferForm.getChkNotSBPrint().equals("Y")) {
                pst = con.prepareStatement("UPDATE EMP_NOTIFICATION SET IF_VISIBLE='N' WHERE NOT_ID=?");
                pst.setInt(1, notid);
                pst.execute();
            } else if (transferForm.getChkNotSBPrint() == null || transferForm.getChkNotSBPrint().equals("")) {
                String sbLanguage = sbDAO.getSupersedeLangDetails(Integer.parseInt(transferForm.getLinkid()), transferForm.getHidSanctioningOthSpc());
                pst = con.prepareStatement("UPDATE EMP_NOTIFICATION SET SB_DESCRIPTION=?,IF_VISIBLE='Y' WHERE NOT_ID=?");
                pst.setString(1, sbLanguage);
                pst.setInt(2, notid);
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
    public void UpdateSupersedeTransfer(TransferForm transferForm, String loginempid, String loginuserid) {

        Connection con = null;

        PreparedStatement pst = null;

        EmpPayRecordForm epayrecordform = new EmpPayRecordForm();
        try {
            con = this.dataSource.getConnection();

            if (transferForm.getRadpostingauthtype() != null && transferForm.getRadpostingauthtype().equals("GOI")) {
                transferForm.setPostedspc(transferForm.getHidPostedOthSpc());
            }

            String sql = "UPDATE EMP_TRANSFER SET DEPT_CODE=?,OFF_CODE=?, NEXT_SPC=?,IF_DEPLOYED=?,FIELD_OFF_CODE=?,entry_taken_by=?,entry_taken_on=? WHERE EMP_ID=? AND TR_ID=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, transferForm.getHidPostedDeptCode());
            pst.setString(2, transferForm.getHidPostedOffCode());
            pst.setString(3, transferForm.getPostedspc());
            pst.setString(4, "");
            pst.setString(5, transferForm.getSltPostedFieldOff());
            pst.setString(6, loginempid);
            pst.setTimestamp(7, new Timestamp(new Date().getTime()));
            pst.setString(8, transferForm.getEmpid());
            pst.setInt(9, Integer.parseInt(transferForm.getTransferId()));
            pst.executeUpdate();

            DataBaseFunctions.closeSqlObjects(pst);

            String sqlString = ToStringBuilder.reflectionToString(pst);
            pst = con.prepareStatement("UPDATE EMP_TRANSFER SET query_string=? WHERE TR_ID=? AND EMP_ID=?");
            pst.setString(1, sqlString);
            pst.setInt(2, Integer.parseInt(transferForm.getTransferId()));
            pst.setString(3, transferForm.getEmpid());
            pst.executeUpdate();

            DataBaseFunctions.closeSqlObjects(pst);

            epayrecordform.setPayid(transferForm.getHpayid());
            epayrecordform.setEmpid(transferForm.getEmpid());
            epayrecordform.setPayscale(transferForm.getSltPayScale());
            epayrecordform.setBasic(transferForm.getTxtBasic());
            epayrecordform.setGp(transferForm.getTxtGP());
            epayrecordform.setS_pay(transferForm.getTxtSP());
            epayrecordform.setP_pay(transferForm.getTxtPP());
            epayrecordform.setOth_pay(transferForm.getTxtOP());
            epayrecordform.setOth_desc(transferForm.getTxtDescOP());
            epayrecordform.setWefDt(transferForm.getTxtWEFDt());
            epayrecordform.setWefTime(transferForm.getSltWEFTime());
            epayrecordform.setPayLevel(transferForm.getPayLevel());
            epayrecordform.setPayCell(transferForm.getPayCell());
            emppayrecordDAO.updateEmpPayRecordData(epayrecordform);

            /*
             * Updating the Service Book Language
             */
            if (transferForm.getChkNotSBPrint() != null && transferForm.getChkNotSBPrint().equals("Y")) {
                pst = con.prepareStatement("UPDATE EMP_NOTIFICATION SET IF_VISIBLE='N' WHERE NOT_ID=?");
                pst.setInt(1, transferForm.getHnotid());
                pst.execute();
            } else if ((transferForm.getChkNotSBPrint() == null || transferForm.getChkNotSBPrint().equals("")) && (transferForm.getHnotType().equalsIgnoreCase("TRANSFER") || transferForm.getHnotType().equalsIgnoreCase("POSTING"))) {
                String sbLanguage = sbDAO.getSupersedeLangDetails(Integer.parseInt(transferForm.getLinkid()), transferForm.getHidSanctioningOthSpc());
                pst = con.prepareStatement("UPDATE EMP_NOTIFICATION SET SB_DESCRIPTION=?,IF_VISIBLE='Y' WHERE NOT_ID=?");
                pst.setString(1, sbLanguage);
                pst.setInt(2, transferForm.getHnotid());
                pst.execute();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }
}
