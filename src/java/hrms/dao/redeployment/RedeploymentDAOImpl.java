package hrms.dao.redeployment;

import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.dao.emppayrecord.EmpPayRecordDAO;
import hrms.dao.leaveAccount.ServiceBookLanguageDAO;
import hrms.dao.master.CadreDAO;
import hrms.dao.notification.NotificationDAO;
import hrms.model.cadre.Cadre;
import hrms.model.emppayrecord.EmpPayRecordForm;
import hrms.model.redeployment.RedeplomentList;
import hrms.model.redeployment.Redeployment;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.apache.commons.lang.builder.ToStringBuilder;

public class RedeploymentDAOImpl implements RedeploymentDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;
    protected EmpPayRecordDAO emppayrecordDAO;
    protected ServiceBookLanguageDAO sbDAO;
    protected NotificationDAO notificationDao;

    public void setSbDAO(ServiceBookLanguageDAO sbDAO) {
        this.sbDAO = sbDAO;
    }

    public void setNotificationDao(NotificationDAO notificationDao) {
        this.notificationDao = notificationDao;
    }
    

    protected CadreDAO cadreDAO;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setEmppayrecordDAO(EmpPayRecordDAO emppayrecordDAO) {
        this.emppayrecordDAO = emppayrecordDAO;
    }

    public void setCadreDAO(CadreDAO cadreDAO) {
        this.cadreDAO = cadreDAO;
    }

    public int insertRedeploymentData(Redeployment redeploy, int notId) {
        int n = 0;
        PreparedStatement pst = null;
        Connection con = null;
        EmpPayRecordForm epayrecordform = null;
        Cadre cadreForm = new Cadre();
        try {
            con = this.dataSource.getConnection();
            String sql = "INSERT INTO EMP_TRANSFER (NOT_ID, NOT_TYPE, EMP_ID,DEPT_CODE,OFF_CODE, NEXT_SPC,IF_DEPLOYED) VALUES(?,?,?,?,?,?,?)";
            //pst = con.prepareStatement(sql);
            pst = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            //  pst.setString(1, maxTransferIdDao.getMaxTransferId()); //CommonFunctions.getMaxCode("EMP_TRANSFER", "TR_ID", con));
            pst.setInt(1, notId);
            pst.setString(2, "REDEPLOYMENT");
            pst.setString(3, redeploy.getEmpid());
            pst.setString(4, redeploy.getHidPostedDeptCode());
            pst.setString(5, redeploy.getHidPostedOffCode());
            pst.setString(6, redeploy.getPostedspc());
            pst.setString(7, "");
            pst.executeUpdate();
            System.out.println("Posted SPC:" + redeploy.getPostedspc());

            ResultSet rs = pst.getGeneratedKeys();
            rs.next();
            int trid = rs.getInt("TR_ID");

            String sqlString = ToStringBuilder.reflectionToString(pst);
            pst = con.prepareStatement("UPDATE EMP_TRANSFER SET query_string=? WHERE TR_ID=? AND EMP_ID=?");
            pst.setString(1, sqlString);
            pst.setInt(2, trid);
            pst.setString(3, redeploy.getEmpid());
            pst.executeUpdate();

            if (redeploy.getRdTransaction() != null && redeploy.getRdTransaction().equals("C")) {
                sql = "UPDATE EMP_MAST SET CUR_OFF_CODE=?,CUR_SPC=?,NEXT_OFFICE_CODE=?,CUR_SALARY=?,CUR_BASIC_SALARY=?,GP=? WHERE EMP_ID=?";
                pst = con.prepareStatement(sql);
                pst.setString(1, redeploy.getHidPostedOffCode());
                pst.setString(2, redeploy.getPostedspc());
                pst.setString(3, redeploy.getHidPostedOffCode());
                pst.setString(4, redeploy.getSltPayScale());
                if (redeploy.getTxtBasic() != null && !redeploy.getTxtBasic().equals("")) {
                    pst.setDouble(5, Double.parseDouble(redeploy.getTxtBasic()));
                } else {
                    pst.setDouble(5, 0);
                }
                if (redeploy.getTxtGP() != null && !redeploy.getTxtGP().equals("")) {
                    pst.setDouble(6, Double.parseDouble(redeploy.getTxtGP()));
                } else {
                    pst.setDouble(6, 0);
                }
                pst.setString(7, redeploy.getEmpid());
                pst.executeUpdate();
            }

            epayrecordform = new EmpPayRecordForm();
            epayrecordform.setEmpid(redeploy.getEmpid());
            epayrecordform.setNot_id(notId);
            epayrecordform.setNot_type("REDEPLOYMENT");
            epayrecordform.setPayscale(redeploy.getSltPayScale());
            epayrecordform.setBasic(redeploy.getTxtBasic());
            epayrecordform.setGp(redeploy.getTxtGP());
            epayrecordform.setS_pay(redeploy.getTxtSP());
            epayrecordform.setP_pay(redeploy.getTxtPP());
            epayrecordform.setOth_pay(redeploy.getTxtOP());
            epayrecordform.setOth_desc(redeploy.getTxtDescOP());
            epayrecordform.setWefDt(redeploy.getTxtWEFDt());
            epayrecordform.setWefTime(redeploy.getSltWEFTime());
            emppayrecordDAO.saveEmpPayRecordData(epayrecordform);

            cadreForm.setEmpId(redeploy.getEmpid());
            cadreForm.setNotId(notId);
            cadreForm.setNotType("REDEPLOYMENT");
            cadreForm.setCadreDept(redeploy.getSltCadreDept());
            cadreForm.setCadreName(redeploy.getSltCadre());
            cadreForm.setGrade(redeploy.getSltGrade());
            cadreForm.setCadreLvl(redeploy.getSltCadreLevel());
            cadreForm.setDescription(redeploy.getSltDescription());
            cadreForm.setAllotmentYear(redeploy.getTxtAllotmentYear());
            cadreForm.setCadreId(redeploy.getTxtCadreId());
            cadreForm.setPostingDept(redeploy.getSltPostingDept());
            cadreForm.setPostCode(redeploy.getSltGenericPost());
            cadreForm.setPostClassification(redeploy.getRdPostClassification());
            cadreForm.setPostStatus(redeploy.getRdPostStatus());
            cadreForm.setCadreJoiningWEFDt(redeploy.getTxtCadreJoiningWEFDt());
            cadreForm.setCadreJoiningWEFTime(redeploy.getSltCadreJoiningWEFTime());
            cadreForm.setJoinedAsSuch(redeploy.getChkJoinedAsSuch());
            //cadreForm.setIfProforma(absorp.getChkProformaPromotion());
            cadreDAO.saveCadreData(cadreForm);
            String sbLang=sbDAO.getRedeploymentLangDetails(redeploy, notId, "REDEPLOYMENT");
            notificationDao.saveServiceBookLanguage(sbLang, notId, "REDEPLOYMENT");
            

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return n;
    }

    public int updateRedeploymentData(Redeployment redeploy) {
        int n = 0;
        PreparedStatement pst = null;
        Connection con = null;
        EmpPayRecordForm epayrecordform = null;
        Cadre cadreForm = new Cadre();
        try {
            con = this.dataSource.getConnection();
            String sql = "UPDATE EMP_TRANSFER SET DEPT_CODE=?,OFF_CODE=?, NEXT_SPC=?,IF_DEPLOYED=? WHERE EMP_ID=? AND TR_ID=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, redeploy.getHidPostedDeptCode());
            pst.setString(2, redeploy.getHidPostedOffCode());
            pst.setString(3, redeploy.getPostedspc());
            pst.setString(4, "");
            pst.setString(5, redeploy.getEmpid());
            pst.setInt(6, Integer.parseInt(redeploy.getHtrid()));
            pst.executeUpdate();

            if (redeploy.getRdTransaction() != null && redeploy.getRdTransaction().equals("C")) {
                sql = "UPDATE EMP_MAST SET NEXT_OFFICE_CODE=?,CUR_SALARY=?,CUR_BASIC_SALARY=?,GP=? WHERE EMP_ID=?";
                pst = con.prepareStatement(sql);
                pst.setString(1, redeploy.getHidPostedOffCode());
                pst.setString(2, redeploy.getSltPayScale());
                if (redeploy.getTxtBasic() != null && !redeploy.getTxtBasic().equals("")) {
                    pst.setDouble(3, Double.parseDouble(redeploy.getTxtBasic()));
                } else {
                    pst.setDouble(3, 0);
                }
                if (redeploy.getTxtGP() != null && !redeploy.getTxtGP().equals("")) {
                    pst.setDouble(4, Double.parseDouble(redeploy.getTxtGP()));
                } else {
                    pst.setDouble(4, 0);
                }
                pst.setString(5, redeploy.getEmpid());
                pst.executeUpdate();
            }

            epayrecordform = new EmpPayRecordForm();
            epayrecordform.setPayid(redeploy.getHpayid());
            epayrecordform.setEmpid(redeploy.getEmpid());
            epayrecordform.setPayscale(redeploy.getSltPayScale());
            epayrecordform.setBasic(redeploy.getTxtBasic());
            epayrecordform.setGp(redeploy.getTxtGP());
            epayrecordform.setS_pay(redeploy.getTxtSP());
            epayrecordform.setP_pay(redeploy.getTxtPP());
            epayrecordform.setOth_pay(redeploy.getTxtOP());
            epayrecordform.setOth_desc(redeploy.getTxtDescOP());
            epayrecordform.setWefDt(redeploy.getTxtWEFDt());
            epayrecordform.setWefTime(redeploy.getSltWEFTime());
            emppayrecordDAO.updateEmpPayRecordData(epayrecordform);

            cadreForm.setCadreCode(redeploy.gethCadId());
            cadreForm.setEmpId(redeploy.getEmpid());
            cadreForm.setCadreDept(redeploy.getSltCadreDept());
            cadreForm.setCadreName(redeploy.getSltCadre());
            cadreForm.setGrade(redeploy.getSltGrade());
            cadreForm.setCadreLvl(redeploy.getSltCadreLevel());
            cadreForm.setDescription(redeploy.getSltDescription());
            cadreForm.setAllotmentYear(redeploy.getTxtAllotmentYear());

            cadreForm.setCadreId(redeploy.getTxtCadreId());
            cadreForm.setPostingDept(redeploy.getSltPostingDept());
            cadreForm.setPostCode(redeploy.getSltGenericPost());
            cadreForm.setPostClassification(redeploy.getRdPostClassification());
            cadreForm.setPostStatus(redeploy.getRdPostStatus());
            cadreForm.setCadreJoiningWEFDt(redeploy.getTxtCadreJoiningWEFDt());
            cadreForm.setCadreJoiningWEFTime(redeploy.getSltCadreJoiningWEFTime());
            cadreForm.setJoinedAsSuch(redeploy.getChkJoinedAsSuch());
            //cadreForm.setIfProforma(absorp.getChkProformaPromotion());
            cadreDAO.updateCadreData(cadreForm);
            String sbLang=sbDAO.getRedeploymentLangDetails(redeploy, redeploy.getHnotid(), "REDEPLOYMENT");
            notificationDao.saveServiceBookLanguage(sbLang, redeploy.getHnotid(), "REDEPLOYMENT");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return n;
    }

    public int deleteRedeployment(String redeploymentId) {
        int n = 0;
        Connection con = null;
        PreparedStatement pst = null;
        try {
            con = dataSource.getConnection();
            if (redeploymentId != null && !redeploymentId.equals("")) {
                pst = con.prepareStatement("DELETE FROM EMP_NOTIFICATION WHERE NOT_ID=?");
                pst.setInt(1, Integer.parseInt(redeploymentId));
                pst.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return n;
    }

    public Redeployment editRedeployment(int notId, String empId) {
        Redeployment redeployment = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("select emp_cadre.emp_id,emp_transfer.tr_id,emp_transfer.dept_code,emp_transfer.off_code,emp_transfer.next_spc,cad_id,pay_id,joined_assuch,if_proforma,post_class,post_stat,emp_cadre.dept_code cadreDept,emp_cadre.cadre_code,grade,\n"
                    + "  lvl,description,emp_cadre.posting_dept_code,emp_cadre.post_code, emp_cadre.wefd cadreWEFDate,emp_cadre.weft cadreWEFTime,cadreid,allot_year,\n"
                    + "  emp_notification.not_id,ordno,orddt,emp_notification.dept_code notDeptCode,emp_notification.off_code notOffCode,auth,note,if_visible,\n"
                    + "  emp_pay_record.wef payrecordWEFDate,emp_pay_record.weft payrecordWEFTime,emp_pay_record.pay_scale,pay,s_pay,p_pay,oth_pay,emp_pay_record.gp,oth_desc from\n"
                    + " (select not_id,ordno,orddt,dept_code,off_code,auth,note,if_visible from emp_notification where EMP_ID=? AND NOT_ID=?)emp_notification\n"
                    + "  left outer join (select pay_id,not_id,wef,weft,pay_scale,pay,s_pay,p_pay,oth_pay,gp,oth_desc from emp_pay_record\n"
                    + " where EMP_ID=? AND NOT_ID=?)emp_pay_record\n"
                    + "  on emp_notification.not_id=emp_pay_record.not_id\n"
                    + "  left outer join emp_cadre on emp_notification.not_id=emp_cadre.not_id\n"
                    + "  left outer join emp_transfer on emp_notification.not_id=emp_transfer.not_id");
            pst.setString(1, empId);
            pst.setInt(2, notId);
            pst.setString(3, empId);
            pst.setInt(4, notId);
            rs = pst.executeQuery();
            if (rs.next()) {
                redeployment = new Redeployment();
                redeployment.setNotType("REDEPLOYMENT");
                redeployment.setEmpid(rs.getString("EMP_ID"));

                redeployment.setOrdno(rs.getString("ORDNO"));
                redeployment.setOrdDate(rs.getString("ORDDT"));

                redeployment.setHidNotifyingDeptCode(rs.getString("notDeptCode"));
                redeployment.setHidNotifyingOffCode(rs.getString("notOffCode"));
                redeployment.setHidTempNotifyingOffCode(rs.getString("notOffCode"));
                redeployment.setNotifyingSpc(rs.getString("auth"));
                redeployment.setHidTempNotifyingPost(rs.getString("auth"));
                redeployment.setNotifyingPostName(CommonFunctions.getSPN(con, rs.getString("auth")));

                //absorp.setSltAllotmentDesc(rs.getString("allot_desc"));
                //  absorp.setChkRetroPromotion(rs.getString("if_retrospective"));
                redeployment.sethCadId(rs.getString("cad_id"));
                redeployment.setSltCadreDept(rs.getString("cadreDept"));
                redeployment.setSltCadre(rs.getString("cadre_code"));
                redeployment.setHidTempCadre(rs.getString("cadre_code"));
                redeployment.setSltGrade(rs.getString("grade"));
                redeployment.setHidTempGrade(rs.getString("grade"));
                redeployment.setSltCadreLevel(rs.getString("lvl"));
                redeployment.setSltDescription(rs.getString("description"));
                redeployment.setHidTempDescription(rs.getString("description"));
                redeployment.setTxtAllotmentYear(rs.getString("allot_year"));
                redeployment.setTxtCadreId(rs.getString("cadreid"));
                redeployment.setTxtCadreJoiningWEFDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("cadreWEFDate")));
                redeployment.setSltCadreJoiningWEFTime(rs.getString("cadreWEFTime"));

                redeployment.setSltPostingDept(rs.getString("posting_dept_code"));
                redeployment.setSltGenericPost(rs.getString("post_code"));
                redeployment.setHidTempGenericPost(rs.getString("post_code"));

                redeployment.setRdPostClassification(rs.getString("post_class"));
                redeployment.setRdPostStatus(rs.getString("post_stat"));

                redeployment.setChkJoinedAsSuch(rs.getString("joined_assuch"));
                redeployment.setChkProformaPromotion(rs.getString("if_proforma"));

                redeployment.setHpayid(rs.getInt("pay_id"));
                redeployment.setSltPayScale(rs.getString("pay_scale"));
                redeployment.setTxtGP(rs.getString("gp"));
                redeployment.setTxtBasic(rs.getString("pay"));
                redeployment.setTxtSP(rs.getString("s_pay"));
                redeployment.setTxtPP(rs.getString("p_pay"));
                redeployment.setTxtOP(rs.getString("oth_pay"));
                redeployment.setTxtDescOP(rs.getString("oth_desc"));
                redeployment.setTxtWEFDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("payrecordWEFDate")));
                redeployment.setSltWEFTime(rs.getString("payrecordWEFTime"));
                redeployment.setNote(rs.getString("note"));
                redeployment.setHtrid(rs.getString("tr_id"));
                redeployment.setHidPostedDeptCode(rs.getString("dept_code"));
                redeployment.setHidPostedOffCode(rs.getString("off_code"));
                redeployment.setPostedspc(rs.getString("next_spc"));
                redeployment.setPostedPostName(CommonFunctions.getSPN(con, rs.getString("next_spc")));
                if (rs.getString("if_visible") != null && rs.getString("if_visible").equals("N")) {
                    redeployment.setChkNotSBPrint("Y");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return redeployment;
    }

    public List findAllRedeployment(String empId) {
        List list = null;
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        RedeplomentList redepList = null;
        list = new ArrayList();
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("SELECT NOT_ID,DOE,ORDNO,ORDDT FROM EMP_NOTIFICATION WHERE EMP_ID=? AND NOT_TYPE='REDEPLOYMENT'");
            pst.setString(1, empId);
            rs = pst.executeQuery();
            while (rs.next()) {
                redepList = new RedeplomentList();
                redepList.setNotId(rs.getString("NOT_ID"));
                redepList.setNotType("REDEPLOYMENT");
                redepList.setDoe(CommonFunctions.getFormattedOutputDate1(rs.getDate("DOE")));
                redepList.setOrdno(rs.getString("ORDNO"));
                redepList.setOrdDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("ORDDT")));
                list.add(redepList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return list;

    }

}
