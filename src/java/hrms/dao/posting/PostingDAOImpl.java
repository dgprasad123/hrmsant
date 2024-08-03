package hrms.dao.posting;

import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.dao.emppayrecord.EmpPayRecordDAO;
import hrms.dao.leaveAccount.ServiceBookLanguageDAO;
import hrms.dao.transfer.MaxTransferIdDAOImpl;
import hrms.model.emppayrecord.EmpPayRecordForm;
import hrms.model.posting.PostingForm;
import hrms.model.posting.PostingListBean;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;

public class PostingDAOImpl implements PostingDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    @Resource(name = "repodataSource")
    protected DataSource repodataSource;

    protected MaxTransferIdDAOImpl maxTransferIdDao;

    protected EmpPayRecordDAO emppayrecordDAO;

    protected ServiceBookLanguageDAO sbDAO;

    public void setSbDAO(ServiceBookLanguageDAO sbDAO) {
        this.sbDAO = sbDAO;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setRepodataSource(DataSource repodataSource) {
        this.repodataSource = repodataSource;
    }

    public void setMaxTransferIdDao(MaxTransferIdDAOImpl maxTransferIdDao) {
        this.maxTransferIdDao = maxTransferIdDao;
    }

    public void setEmppayrecordDAO(EmpPayRecordDAO emppayrecordDAO) {
        this.emppayrecordDAO = emppayrecordDAO;
    }

    @Override
    public List findAllPosting(String empid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List postingList = new ArrayList();

        PostingListBean pbean = null;
        try {
            con = this.repodataSource.getConnection();

            String sql = "select is_validated,tr_id,emp_notification.doe,emp_notification.not_id,emp_notification.not_type,ordno,orddt from"
                    + " (select is_validated,emp_id,doe,not_id,not_type,ordno,orddt from emp_notification where emp_id=? and not_type='POSTING')emp_notification"
                    + " left outer join emp_transfer on emp_notification.not_id=emp_transfer.not_id";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            rs = pst.executeQuery();
            while (rs.next()) {
                pbean = new PostingListBean();
                pbean.setHnotid(rs.getString("not_id"));
                pbean.setNotType(rs.getString("not_type"));
                pbean.setDoe(CommonFunctions.getFormattedOutputDate1(rs.getDate("DOE")));
                pbean.setOrdno(rs.getString("ordno"));
                pbean.setOrdDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("orddt")));
                pbean.setIsValidated(rs.getString("is_validated"));
                postingList.add(pbean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return postingList;
    }

    @Override
    public void savePosting(PostingForm postingform, int notid) {

        Connection con = null;

        PreparedStatement pst = null;

        EmpPayRecordForm epayrecordform = new EmpPayRecordForm();
        try {
            con = this.dataSource.getConnection();

            if (postingform.getRadpostingauthtype() != null && postingform.getRadpostingauthtype().equals("GOI")) {
                postingform.setPostedspc(postingform.getHidPostingOthSpc());
            }

            String sql = "INSERT INTO EMP_TRANSFER (NOT_ID, NOT_TYPE, EMP_ID,DEPT_CODE,OFF_CODE, NEXT_SPC,IF_DEPLOYED,FIELD_OFF_CODE) VALUES(?,?,?,?,?,?,?,?)";
            pst = con.prepareStatement(sql);
            pst.setInt(1, notid);
            pst.setString(2, "POSTING");
            pst.setString(3, postingform.getEmpid());
            pst.setString(4, postingform.getHidPostedDeptCode());
            pst.setString(5, postingform.getHidPostedOffCode());
            pst.setString(6, postingform.getPostedspc());
            pst.setString(7, "");
            pst.setString(8, postingform.getSltPostedFieldOff());
            pst.executeUpdate();

            if (postingform.getRdTransaction() != null && postingform.getRdTransaction().equals("C")) {
                sql = "UPDATE EMP_MAST SET NEXT_OFFICE_CODE=? WHERE EMP_ID=?";
                pst = con.prepareStatement(sql);
                pst.setString(1, postingform.getHidPostedOffCode());
                pst.setString(2, postingform.getEmpid());
                pst.executeUpdate();
            }

            epayrecordform.setEmpid(postingform.getEmpid());
            epayrecordform.setNot_id(notid);
            epayrecordform.setNot_type("POSTING");
            epayrecordform.setPayscale(postingform.getSltPayScale());
            epayrecordform.setBasic(postingform.getTxtBasic());
            epayrecordform.setGp(postingform.getTxtGP());
            epayrecordform.setS_pay(postingform.getTxtSP());
            epayrecordform.setP_pay(postingform.getTxtPP());
            epayrecordform.setOth_pay(postingform.getTxtOP());
            epayrecordform.setOth_desc(postingform.getTxtDescOP());
            epayrecordform.setWefDt(postingform.getTxtWEFDt());
            epayrecordform.setWefTime(postingform.getSltWEFTime());
            emppayrecordDAO.saveEmpPayRecordData(epayrecordform);

            /*
             * Updating the Service Book Language
             */
            if (postingform.getChkNotSBPrint() != null && postingform.getChkNotSBPrint().equals("Y")) {
                pst = con.prepareStatement("UPDATE EMP_NOTIFICATION SET IF_VISIBLE='N' WHERE NOT_ID=?");
                pst.setInt(1, notid);
                pst.execute();
            } else if (postingform.getChkNotSBPrint() == null || postingform.getChkNotSBPrint().equals("")) {
                String sbLang = sbDAO.getPostingDetails(postingform, notid, "POSTING");
                pst = con.prepareStatement("UPDATE EMP_NOTIFICATION SET SB_DESCRIPTION=?,IF_VISIBLE='Y' WHERE NOT_ID=? ");
                pst.setString(1, sbLang);
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
    public void updatePosting(PostingForm postingform) {

        Connection con = null;

        PreparedStatement pst = null;

        EmpPayRecordForm epayrecordform = new EmpPayRecordForm();

        try {
            con = this.dataSource.getConnection();

            if (postingform.getRadpostingauthtype() != null && postingform.getRadpostingauthtype().equals("GOI")) {
                postingform.setPostedspc(postingform.getHidPostingOthSpc());
            }

            String sql = "UPDATE EMP_TRANSFER SET DEPT_CODE=?,OFF_CODE=?, NEXT_SPC=?,IF_DEPLOYED=?,FIELD_OFF_CODE=? WHERE EMP_ID=? AND TR_ID=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, postingform.getHidPostedDeptCode());
            pst.setString(2, postingform.getHidPostedOffCode());
            pst.setString(3, postingform.getPostedspc());
            pst.setString(4, "");
            pst.setString(5, postingform.getSltPostedFieldOff());
            pst.setString(6, postingform.getEmpid());
            pst.setInt(7, Integer.parseInt(postingform.getTransferId()));
            pst.executeUpdate();

            if (postingform.getRdTransaction() != null && postingform.getRdTransaction().equals("C")) {
                sql = "UPDATE EMP_MAST SET NEXT_OFFICE_CODE=? WHERE EMP_ID=?";
                pst = con.prepareStatement(sql);
                pst.setString(1, postingform.getHidPostedOffCode());
                pst.setString(2, postingform.getEmpid());
                pst.executeUpdate();
            }

            epayrecordform.setPayid(postingform.getHpayid());
            epayrecordform.setEmpid(postingform.getEmpid());
            epayrecordform.setPayscale(postingform.getSltPayScale());
            epayrecordform.setBasic(postingform.getTxtBasic());
            epayrecordform.setGp(postingform.getTxtGP());
            epayrecordform.setS_pay(postingform.getTxtSP());
            epayrecordform.setP_pay(postingform.getTxtPP());
            epayrecordform.setOth_pay(postingform.getTxtOP());
            epayrecordform.setOth_desc(postingform.getTxtDescOP());
            epayrecordform.setWefDt(postingform.getTxtWEFDt());
            epayrecordform.setWefTime(postingform.getSltWEFTime());
            if (epayrecordform.getPayid() > 0) {
                emppayrecordDAO.updateEmpPayRecordData(epayrecordform);
            } else {
                epayrecordform.setNot_id(postingform.getHnotid());
                epayrecordform.setNot_type("POSTING");
                emppayrecordDAO.saveEmpPayRecordData(epayrecordform);
            }

            if (postingform.getChkNotSBPrint() != null && postingform.getChkNotSBPrint().equals("Y")) {
                pst = con.prepareStatement("UPDATE EMP_NOTIFICATION SET IF_VISIBLE='N' WHERE NOT_ID=?");
                pst.setInt(1, postingform.getHnotid());
                pst.execute();
            } else if (postingform.getChkNotSBPrint() == null || postingform.getChkNotSBPrint().equals("")) {
                String sbLang = sbDAO.getPostingDetails(postingform, postingform.getHnotid(), "POSTING");
                pst = con.prepareStatement("UPDATE EMP_NOTIFICATION SET SB_DESCRIPTION=?,IF_VISIBLE='Y' WHERE NOT_ID=? ");
                pst.setString(1, sbLang);
                pst.setInt(2, postingform.getHnotid());
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
    public void deletePosting(PostingForm postingform) {
        Connection con = null;

        PreparedStatement pst = null;

        EmpPayRecordForm epayrecordform = new EmpPayRecordForm();

        try {
            con = dataSource.getConnection();

            String sql = "update emp_mast set NEXT_OFFICE_CODE=null where emp_id=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, postingform.getEmpid());
            pst.executeUpdate();

            //Delete from Emp Join
            sql = "DELETE FROM emp_join WHERE not_id = '" + postingform.getHnotid() + "' AND not_type = 'POSTING'";
            pst = con.prepareStatement(sql);
            pst.executeUpdate();

            //Delete from Emp Relieve
            sql = "DELETE FROM emp_relieve WHERE not_id = '" + postingform.getHnotid() + "' AND not_type = 'POSTING'";
            pst = con.prepareStatement(sql);
            pst.executeUpdate();

            //Delete from emp Pay Record
            sql = "DELETE FROM emp_pay_record WHERE not_id = '" + postingform.getHnotid() + "' AND not_type = 'POSTING'";
            pst = con.prepareStatement(sql);
            pst.executeUpdate();

            //Delete from emp Pay Record
            sql = "DELETE FROM emp_transfer WHERE not_id = '" + postingform.getHnotid() + "' AND not_type = 'POSTING'";
            pst = con.prepareStatement(sql);
            pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public PostingForm getPostingData(PostingForm postingform, int notificationId) throws SQLException {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            con = this.repodataSource.getConnection();

            String sql = "select"
                    + " emp_notification.not_id,ordno,orddt,emp_notification.dept_code notDeptCode,office1.dist_code notDistCode,emp_notification.off_code notOffCode,"
                    + " auth,note,tr_id,emp_transfer.dept_code transDeptCode,office2.dist_code transDistCode,emp_transfer.off_code transOffCode,next_spc,field_off_code,pay_id,wef,weft,emp_pay_record.pay_scale,"
                    + " pay,s_pay,p_pay,oth_pay,emp_pay_record.gp,oth_desc,spn,organization_type,organization_type_posting from"
                    + " (SELECT not_id,tr_id,dept_code,off_code,next_spc,field_off_code FROM EMP_TRANSFER WHERE EMP_ID=? AND NOT_ID=?)emp_transfer"
                    + " inner join (select not_id,ordno,orddt,dept_code,off_code,auth,note,organization_type,organization_type_posting from emp_notification where EMP_ID=? AND NOT_ID=?)emp_notification"
                    + " on emp_transfer.not_id=emp_notification.not_id"
                    + " left outer join (select pay_id,not_id,wef,weft,pay_scale,pay,s_pay,p_pay,oth_pay,gp,oth_desc from emp_pay_record where EMP_ID=? AND NOT_ID=?)emp_pay_record"
                    + " on emp_notification.not_id=emp_pay_record.not_id"
                    + " left outer join g_spc on emp_transfer.next_spc=g_spc.spc"
                    + " left outer join g_office office1 on emp_notification.off_code=office1.off_code"
                    + " left outer join g_office office2 on emp_transfer.off_code=office2.off_code";
            pst = con.prepareStatement(sql);
            pst.setString(1, postingform.getEmpid());
            pst.setInt(2, notificationId);
            pst.setString(3, postingform.getEmpid());
            pst.setInt(4, notificationId);
            pst.setString(5, postingform.getEmpid());
            pst.setInt(6, notificationId);
            rs = pst.executeQuery();
            if (rs.next()) {
                postingform.setTransferId(rs.getString("tr_id"));

                postingform.setTxtNotOrdNo(rs.getString("ordno"));
                postingform.setTxtNotOrdDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("orddt")));
                postingform.setHidAuthDeptCode(rs.getString("notDeptCode"));
                postingform.setHidAuthDistCode(rs.getString("notDistCode"));
                postingform.setHidAuthOffCode(rs.getString("notOffCode"));
                postingform.setAuthSpc(rs.getString("auth"));
                postingform.setAuthPostName(CommonFunctions.getSPN(con, rs.getString("auth")));

                postingform.setHidPostedDeptCode(rs.getString("transDeptCode"));
                postingform.setHidPostedDistCode(rs.getString("transDistCode"));
                postingform.setHidPostedOffCode(rs.getString("transOffCode"));
                postingform.setPostedspc(rs.getString("next_spc"));
                postingform.setPostedPostName(rs.getString("SPN"));
                postingform.setSltPostedFieldOff(rs.getString("field_off_code"));

                postingform.setHpayid(rs.getInt("pay_id"));
                postingform.setSltPayScale(rs.getString("pay_scale"));
                postingform.setTxtGP(rs.getString("gp"));
                postingform.setTxtBasic(rs.getString("pay"));
                postingform.setTxtSP(rs.getString("s_pay"));
                postingform.setTxtPP(rs.getString("p_pay"));
                postingform.setTxtOP(rs.getString("oth_pay"));
                postingform.setTxtDescOP(rs.getString("oth_desc"));
                postingform.setTxtWEFDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("wef")));
                postingform.setSltWEFTime(rs.getString("weft"));
                postingform.setNote(rs.getString("note"));

                if (rs.getString("organization_type") != null && rs.getString("organization_type").equals("GOI")) {
                    postingform.setRadnotifyingauthtype(rs.getString("organization_type"));
                    postingform.setHidNotifyingOthSpc(rs.getString("auth"));
                    postingform.setAuthPostName(getOtherSpn(rs.getString("AUTH")));
                } else {
                    postingform.setRadnotifyingauthtype(rs.getString("organization_type"));
                }

                if (rs.getString("organization_type_posting") != null && rs.getString("organization_type_posting").equals("GOI")) {
                    postingform.setRadpostingauthtype(rs.getString("organization_type_posting"));
                    postingform.setHidPostingOthSpc(rs.getString("next_spc"));
                    postingform.setPostedPostName(getOtherSpn(rs.getString("next_spc")));
                } else {
                    postingform.setRadpostingauthtype(rs.getString("organization_type_posting"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return postingform;
    }

    public String getOtherSpn(String othSpc) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        String spn = "";
        try {
            con = this.repodataSource.getConnection();
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
