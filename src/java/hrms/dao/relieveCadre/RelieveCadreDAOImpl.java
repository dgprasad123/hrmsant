package hrms.dao.relieveCadre;

import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.dao.emppayrecord.EmpPayRecordDAO;
import hrms.dao.leaveAccount.ServiceBookLanguageDAO;
import hrms.dao.master.CadreDAO;
import hrms.dao.notification.NotificationDAO;
import hrms.model.cadre.Cadre;
import hrms.model.emppayrecord.EmpPayRecordForm;
import hrms.model.relieveCadre.RelieveCadreForm;
import hrms.model.relieveCadre.RelieveCadreList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;

public class RelieveCadreDAOImpl implements RelieveCadreDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    protected EmpPayRecordDAO emppayrecordDAO;

    protected CadreDAO cadreDAO;

    protected ServiceBookLanguageDAO sbDAO;
    protected NotificationDAO notificationDao;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setEmppayrecordDAO(EmpPayRecordDAO emppayrecordDAO) {
        this.emppayrecordDAO = emppayrecordDAO;
    }

    public void setCadreDAO(CadreDAO cadreDAO) {
        this.cadreDAO = cadreDAO;
    }

    public void setSbDAO(ServiceBookLanguageDAO sbDAO) {
        this.sbDAO = sbDAO;
    }

    public void setNotificationDao(NotificationDAO notificationDao) {
        this.notificationDao = notificationDao;
    }

    @Override
    public List getRelieveCadreList(String empid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List relieveCadreList = new ArrayList();
        RelieveCadreList rcbean = null;
        try {
            con = this.dataSource.getConnection();

            String sql = "select emp_notification.doe,emp_notification.not_id,not_type,ordno,orddt from"
                    + " (select emp_id,doe,not_id,not_type,ordno,orddt from emp_notification where emp_id=? and not_type='RELIEVE_CADRE')emp_notification";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            rs = pst.executeQuery();
            while (rs.next()) {
                rcbean = new RelieveCadreList();
                rcbean.setDoe(CommonFunctions.getFormattedOutputDate1(rs.getDate("doe")));
                rcbean.setNotId(rs.getString("not_id"));
                if (rs.getString("not_type") != null && !rs.getString("not_type").equals("")) {
                    if (rs.getString("not_type").equals("RELIEVE_CADRE")) {
                        rcbean.setNotType("RELIEVE FROM CADRE");
                    }
                }
                rcbean.setOrdNo(rs.getString("ordno"));
                rcbean.setOrdDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("orddt")));
                relieveCadreList.add(rcbean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return relieveCadreList;
    }

    @Override
    public void saveRelieveCadre(RelieveCadreForm relieveCadreForm, int notId) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        EmpPayRecordForm epayrecordform = new EmpPayRecordForm();
        Cadre cadreForm = new Cadre();
        try {
            con = this.dataSource.getConnection();

            epayrecordform.setEmpid(relieveCadreForm.getEmpid());
            epayrecordform.setNot_id(notId);
            epayrecordform.setNot_type("RELIEVE_CADRE");
            epayrecordform.setPayscale(relieveCadreForm.getSltPayScale());
            epayrecordform.setBasic(relieveCadreForm.getTxtBasic());
            epayrecordform.setGp(relieveCadreForm.getTxtGP());
            epayrecordform.setS_pay(relieveCadreForm.getTxtSP());
            epayrecordform.setP_pay(relieveCadreForm.getTxtPP());
            epayrecordform.setOth_pay(relieveCadreForm.getTxtOP());
            epayrecordform.setOth_desc(relieveCadreForm.getTxtDescOP());
            epayrecordform.setWefDt(relieveCadreForm.getTxtWEFDt());
            epayrecordform.setWefTime(relieveCadreForm.getSltWEFTime());
            emppayrecordDAO.saveEmpPayRecordData(epayrecordform);

            cadreForm.setEmpId(relieveCadreForm.getEmpid());
            cadreForm.setNotId(notId);
            cadreForm.setNotType("RELIEVE_CADRE");
            cadreForm.setCadreDept(relieveCadreForm.getSltCadreDept());
            cadreForm.setCadreName(relieveCadreForm.getSltCadre());
            cadreForm.setGrade(relieveCadreForm.getSltGrade());
            cadreForm.setCadreLvl(relieveCadreForm.getSltCadreLevel());
            cadreForm.setDescription(relieveCadreForm.getSltDescription());
            cadreForm.setAllotmentYear(relieveCadreForm.getTxtAllotmentYear());
            cadreForm.setCadreId(relieveCadreForm.getTxtCadreId());
            cadreForm.setPostingDept(relieveCadreForm.getSltPostingDept());
            cadreForm.setPostCode(relieveCadreForm.getSltGenericPost());
            cadreForm.setPostClassification(relieveCadreForm.getRdPostClassification());
            cadreForm.setPostStatus(relieveCadreForm.getRdPostStatus());
            cadreForm.setCadreJoiningWEFDt(relieveCadreForm.getTxtCadreJoiningWEFDt());
            cadreForm.setCadreJoiningWEFTime(relieveCadreForm.getSltCadreJoiningWEFTime());
            cadreDAO.saveCadreData(cadreForm);
            String sbLang = sbDAO.getRelieveFromCadreDetails(relieveCadreForm, notId, "RELIEVE_CADRE");
            notificationDao.saveServiceBookLanguage(sbLang, notId, "RELIEVE_CADRE");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    @Override
    public void updateRelieveCadre(RelieveCadreForm relieveCadreForm) {

        Connection con = null;

        PreparedStatement pst = null;

        EmpPayRecordForm epayrecordform = new EmpPayRecordForm();
        Cadre cadreForm = new Cadre();
        try {
            con = this.dataSource.getConnection();

            epayrecordform.setPayid(relieveCadreForm.getHpayid());
            epayrecordform.setEmpid(relieveCadreForm.getEmpid());
            epayrecordform.setPayscale(relieveCadreForm.getSltPayScale());
            epayrecordform.setBasic(relieveCadreForm.getTxtBasic());
            epayrecordform.setGp(relieveCadreForm.getTxtGP());
            epayrecordform.setS_pay(relieveCadreForm.getTxtSP());
            epayrecordform.setP_pay(relieveCadreForm.getTxtPP());
            epayrecordform.setOth_pay(relieveCadreForm.getTxtOP());
            epayrecordform.setOth_desc(relieveCadreForm.getTxtDescOP());
            epayrecordform.setWefDt(relieveCadreForm.getTxtWEFDt());
            epayrecordform.setWefTime(relieveCadreForm.getSltWEFTime());
            emppayrecordDAO.updateEmpPayRecordData(epayrecordform);

            cadreForm.setCadreCode(relieveCadreForm.gethCadId());
            cadreForm.setEmpId(relieveCadreForm.getEmpid());
            cadreForm.setCadreDept(relieveCadreForm.getSltCadreDept());
            cadreForm.setCadreName(relieveCadreForm.getSltCadre());
            cadreForm.setGrade(relieveCadreForm.getSltGrade());
            cadreForm.setCadreLvl(relieveCadreForm.getSltCadreLevel());
            cadreForm.setDescription(relieveCadreForm.getSltDescription());
            cadreForm.setAllotmentYear(relieveCadreForm.getTxtAllotmentYear());
            cadreForm.setCadreId(relieveCadreForm.getTxtCadreId());
            cadreForm.setPostingDept(relieveCadreForm.getSltPostingDept());
            cadreForm.setPostCode(relieveCadreForm.getSltGenericPost());
            cadreForm.setPostClassification(relieveCadreForm.getRdPostClassification());
            cadreForm.setPostStatus(relieveCadreForm.getRdPostStatus());
            cadreForm.setCadreJoiningWEFDt(relieveCadreForm.getTxtCadreJoiningWEFDt());
            cadreForm.setCadreJoiningWEFTime(relieveCadreForm.getSltCadreJoiningWEFTime());
            cadreDAO.updateCadreData(cadreForm);
            String sbLang = sbDAO.getRelieveFromCadreDetails(relieveCadreForm, relieveCadreForm.getHnotid(), "RELIEVE_CADRE");
            notificationDao.saveServiceBookLanguage(sbLang, relieveCadreForm.getHnotid(), "RELIEVE_CADRE");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public RelieveCadreForm getEmpRelieveCadreData(RelieveCadreForm relieveCadreForm, int notId) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = this.dataSource.getConnection();

            String sql = "select"
                    + " cad_id,joined_assuch,if_proforma,post_class,post_stat,emp_cadre.dept_code cadreDept,emp_cadre.cadre_code,grade,lvl,description,emp_cadre.posting_dept_code,emp_cadre.post_code,"
                    + " emp_notification.not_id,ordno,orddt,emp_notification.dept_code notDeptCode,emp_notification.off_code notOffCode,emp_notification.if_visible,"
                    + " auth,note,emp_cadre.wefd cadreWEFDate,emp_cadre.weft cadreWEFTime,pay_id,emp_pay_record.wef payrecordWEFDate,emp_pay_record.weft payrecordWEFTime,emp_pay_record.pay_scale,pay,s_pay,p_pay,"
                    + " oth_pay,emp_pay_record.gp,oth_desc from"
                    + " (select not_id,ordno,orddt,dept_code,off_code,auth,note,if_visible from emp_notification where EMP_ID=? AND NOT_ID=?)emp_notification"
                    + " left outer join"
                    + " (select pay_id,not_id,wef,weft,pay_scale,pay,s_pay,p_pay,oth_pay,gp,oth_desc from emp_pay_record where EMP_ID=? AND NOT_ID=?)emp_pay_record"
                    + " on emp_notification.not_id=emp_pay_record.not_id"
                    + " left outer join emp_cadre on emp_notification.not_id=emp_cadre.not_id";
            pst = con.prepareStatement(sql);
            pst.setString(1, relieveCadreForm.getEmpid());
            pst.setInt(2, notId);
            pst.setString(3, relieveCadreForm.getEmpid());
            pst.setInt(4, notId);
            rs = pst.executeQuery();
            if (rs.next()) {
                //repatriationForm.setPromotionId(rs.getString("pro_id"));
                relieveCadreForm.setTxtNotOrdNo(rs.getString("ordno"));
                relieveCadreForm.setHnotid(rs.getInt("not_id"));
                relieveCadreForm.setTxtNotOrdDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("orddt")));
                relieveCadreForm.setHidNotifyingDeptCode(rs.getString("notDeptCode"));
                relieveCadreForm.setHidNotifyingOffCode(rs.getString("notOffCode"));
                relieveCadreForm.setNotifyingSpc(rs.getString("auth"));
                relieveCadreForm.setNotifyingPostName(CommonFunctions.getSPN(con, rs.getString("auth")));

                relieveCadreForm.sethCadId(rs.getString("cad_id"));
                relieveCadreForm.setSltCadreDept(rs.getString("cadreDept"));
                relieveCadreForm.setSltCadre(rs.getString("cadre_code"));
                relieveCadreForm.setSltGrade(rs.getString("grade"));
                relieveCadreForm.setSltCadreLevel(rs.getString("lvl"));
                relieveCadreForm.setSltDescription(rs.getString("description"));
                relieveCadreForm.setTxtCadreJoiningWEFDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("cadreWEFDate")));
                relieveCadreForm.setSltCadreJoiningWEFTime(rs.getString("cadreWEFTime"));

                relieveCadreForm.setSltPostingDept(rs.getString("posting_dept_code"));
                relieveCadreForm.setSltGenericPost(rs.getString("post_code"));

                relieveCadreForm.setRdPostClassification(rs.getString("post_class"));
                relieveCadreForm.setRdPostStatus(rs.getString("post_stat"));

                relieveCadreForm.setHpayid(rs.getInt("pay_id"));
                relieveCadreForm.setSltPayScale(rs.getString("pay_scale"));
                relieveCadreForm.setTxtGP(rs.getString("gp"));
                relieveCadreForm.setTxtBasic(rs.getString("pay"));
                relieveCadreForm.setTxtSP(rs.getString("s_pay"));
                relieveCadreForm.setTxtPP(rs.getString("p_pay"));
                relieveCadreForm.setTxtOP(rs.getString("oth_pay"));
                relieveCadreForm.setTxtDescOP(rs.getString("oth_desc"));
                relieveCadreForm.setTxtWEFDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("payrecordWEFDate")));
                relieveCadreForm.setSltWEFTime(rs.getString("payrecordWEFTime"));
                relieveCadreForm.setNote(rs.getString("note"));
                if (rs.getString("if_visible") != null && rs.getString("if_visible").equals("N")) {
                    relieveCadreForm.setChkNotSBPrint("Y");
                } else {
                    relieveCadreForm.setChkNotSBPrint("N");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return relieveCadreForm;
    }
}
