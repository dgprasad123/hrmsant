package hrms.dao.repatriation;

import hrms.SelectOption;
import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.dao.emppayrecord.EmpPayRecordDAO;
import hrms.dao.leaveAccount.ServiceBookLanguageDAO;
import hrms.dao.master.CadreDAO;
import hrms.dao.notification.NotificationDAO;
import hrms.model.cadre.Cadre;
import hrms.model.emppayrecord.EmpPayRecordForm;
import hrms.model.repatriation.RepatriationForm;
import hrms.model.repatriation.RepatriationList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;

public class RepatriationDAOImpl implements RepatriationDAO {

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
    public List getRepatriationList(String empid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List repatriationList = new ArrayList();
        RepatriationList rbean = null;
        try {
            con = this.dataSource.getConnection();

            String sql = "select is_validated,emp_notification.doe,emp_notification.not_id,not_type,ordno,orddt from"
                    + " (select is_validated,emp_id,doe,not_id,not_type,ordno,orddt from emp_notification where emp_id=? and not_type='REPATRIATION')emp_notification";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            rs = pst.executeQuery();
            while (rs.next()) {
                rbean = new RepatriationList();
                rbean.setDoe(CommonFunctions.getFormattedOutputDate1(rs.getDate("doe")));
                rbean.setNotId(rs.getString("not_id"));
                rbean.setNotType(rs.getString("not_type"));
                rbean.setOrdNo(rs.getString("ordno"));
                rbean.setOrdDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("orddt")));
                rbean.setIsValidated(rs.getString("is_validated"));
                repatriationList.add(rbean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return repatriationList;
    }

    @Override
    public List getAllotDescList(String notType) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List allotDescList = new ArrayList();

        SelectOption so = null;
        try {
            con = this.dataSource.getConnection();

            String sql = "select * from g_allot_desc where not_type=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, notType);
            rs = pst.executeQuery();
            while (rs.next()) {
                so = new SelectOption();
                so.setValue(rs.getString("allot_desc"));
                so.setLabel(rs.getString("allot_desc"));
                allotDescList.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return allotDescList;
    }

    @Override
    public void saveRepatriation(RepatriationForm repatriationForm, int notId) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        EmpPayRecordForm epayrecordform = new EmpPayRecordForm();
        Cadre cadreForm = new Cadre();
        try {
            con = this.dataSource.getConnection();

            if (repatriationForm.getRdTransaction() != null && repatriationForm.getRdTransaction().equals("C")) {
                pst = con.prepareStatement("UPDATE EMP_MAST SET cur_salary=?,cur_basic_salary=?,gp=? WHERE EMP_ID=?");
                pst.setString(1, repatriationForm.getSltPayScale());
                if (repatriationForm.getTxtBasic() != null && !repatriationForm.getTxtBasic().equals("")) {
                    pst.setDouble(2, Double.parseDouble(repatriationForm.getTxtBasic()));
                } else {
                    pst.setDouble(2, 0);
                }
                if (repatriationForm.getTxtGP() != null && !repatriationForm.getTxtGP().equals("")) {
                    pst.setDouble(3, Double.parseDouble(repatriationForm.getTxtGP()));
                } else {
                    pst.setDouble(3, 0);
                }
                pst.setString(4, repatriationForm.getEmpid());
                pst.executeUpdate();
            }

            epayrecordform.setEmpid(repatriationForm.getEmpid());
            epayrecordform.setNot_id(notId);
            epayrecordform.setNot_type("REPATRIATION");
            epayrecordform.setPayscale(repatriationForm.getSltPayScale());
            epayrecordform.setBasic(repatriationForm.getTxtBasic());
            epayrecordform.setGp(repatriationForm.getTxtGP());
            epayrecordform.setS_pay(repatriationForm.getTxtSP());
            epayrecordform.setP_pay(repatriationForm.getTxtPP());
            epayrecordform.setOth_pay(repatriationForm.getTxtOP());
            epayrecordform.setOth_desc(repatriationForm.getTxtDescOP());
            epayrecordform.setWefDt(repatriationForm.getTxtWEFDt());
            epayrecordform.setWefTime(repatriationForm.getSltWEFTime());
            emppayrecordDAO.saveEmpPayRecordData(epayrecordform);

            cadreForm.setEmpId(repatriationForm.getEmpid());
            cadreForm.setNotId(notId);
            cadreForm.setNotType("REPATRIATION");
            cadreForm.setCadreDept(repatriationForm.getSltCadreDept());
            cadreForm.setCadreName(repatriationForm.getSltCadre());
            cadreForm.setGrade(repatriationForm.getSltGrade());
            cadreForm.setCadreLvl(repatriationForm.getSltCadreLevel());
            cadreForm.setDescription(repatriationForm.getSltDescription());
            cadreForm.setAllotmentYear(repatriationForm.getTxtAllotmentYear());
            cadreForm.setCadreId(repatriationForm.getTxtCadreId());
            cadreForm.setPostingDept(repatriationForm.getSltPostingDept());
            cadreForm.setPostCode(repatriationForm.getSltGenericPost());
            cadreForm.setPostClassification(repatriationForm.getRdPostClassification());
            cadreForm.setPostStatus(repatriationForm.getRdPostStatus());
            cadreForm.setCadreJoiningWEFDt(repatriationForm.getTxtCadreJoiningWEFDt());
            cadreForm.setCadreJoiningWEFTime(repatriationForm.getSltCadreJoiningWEFTime());
            cadreDAO.saveCadreData(cadreForm);
            String sbLang = sbDAO.getRepatriationLangDetails(repatriationForm, notId, "REPATRIATION");
            notificationDao.saveServiceBookLanguage(sbLang, notId, "REPATRIATION");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    @Override
    public void updateRepatriation(RepatriationForm repatriationForm) {

        Connection con = null;

        PreparedStatement pst = null;

        EmpPayRecordForm epayrecordform = new EmpPayRecordForm();
        Cadre cadreForm = new Cadre();
        try {
            con = this.dataSource.getConnection();

            if (repatriationForm.getRdTransaction() != null && repatriationForm.getRdTransaction().equals("C")) {
                pst = con.prepareStatement("UPDATE EMP_MAST SET cur_salary=?,cur_basic_salary=?,gp=? WHERE EMP_ID=?");
                pst.setString(1, repatriationForm.getSltPayScale());
                if (repatriationForm.getTxtBasic() != null && !repatriationForm.getTxtBasic().equals("")) {
                    pst.setDouble(2, Double.parseDouble(repatriationForm.getTxtBasic()));
                } else {
                    pst.setDouble(2, 0);
                }
                if (repatriationForm.getTxtGP() != null && !repatriationForm.getTxtGP().equals("")) {
                    pst.setDouble(3, Double.parseDouble(repatriationForm.getTxtGP()));
                } else {
                    pst.setDouble(3, 0);
                }
                pst.setString(4, repatriationForm.getEmpid());
                pst.executeUpdate();
            }

            epayrecordform.setPayid(repatriationForm.getHpayid());
            epayrecordform.setEmpid(repatriationForm.getEmpid());
            epayrecordform.setPayscale(repatriationForm.getSltPayScale());
            epayrecordform.setBasic(repatriationForm.getTxtBasic());
            epayrecordform.setGp(repatriationForm.getTxtGP());
            epayrecordform.setS_pay(repatriationForm.getTxtSP());
            epayrecordform.setP_pay(repatriationForm.getTxtPP());
            epayrecordform.setOth_pay(repatriationForm.getTxtOP());
            epayrecordform.setOth_desc(repatriationForm.getTxtDescOP());
            epayrecordform.setWefDt(repatriationForm.getTxtWEFDt());
            epayrecordform.setWefTime(repatriationForm.getSltWEFTime());
            emppayrecordDAO.updateEmpPayRecordData(epayrecordform);

            cadreForm.setCadreCode(repatriationForm.gethCadId());
            cadreForm.setEmpId(repatriationForm.getEmpid());
            cadreForm.setCadreDept(repatriationForm.getSltCadreDept());
            cadreForm.setCadreName(repatriationForm.getSltCadre());
            cadreForm.setGrade(repatriationForm.getSltGrade());
            cadreForm.setCadreLvl(repatriationForm.getSltCadreLevel());
            cadreForm.setDescription(repatriationForm.getSltDescription());
            cadreForm.setAllotmentYear(repatriationForm.getTxtAllotmentYear());
            cadreForm.setCadreId(repatriationForm.getTxtCadreId());
            cadreForm.setPostingDept(repatriationForm.getSltPostingDept());
            cadreForm.setPostCode(repatriationForm.getSltGenericPost());
            cadreForm.setPostClassification(repatriationForm.getRdPostClassification());
            cadreForm.setPostStatus(repatriationForm.getRdPostStatus());
            cadreForm.setCadreJoiningWEFDt(repatriationForm.getTxtCadreJoiningWEFDt());
            cadreForm.setCadreJoiningWEFTime(repatriationForm.getSltCadreJoiningWEFTime());
            cadreDAO.updateCadreData(cadreForm);
            String sbLang = sbDAO.getRepatriationLangDetails(repatriationForm, repatriationForm.getHnotid(), "REPATRIATION");
            notificationDao.saveServiceBookLanguage(sbLang, repatriationForm.getHnotid(), "REPATRIATION");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public RepatriationForm getEmpRepatriationData(RepatriationForm repatriationForm, int notId) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = this.dataSource.getConnection();

            String sql = "select"
                    + " cad_id,joined_assuch,if_proforma,post_class,post_stat,emp_cadre.dept_code cadreDept,emp_cadre.cadre_code,grade,lvl,description,emp_cadre.dept_code,emp_cadre.post_code,"
                    + " emp_notification.not_id,emp_notification.if_visible,ordno,orddt,emp_notification.dept_code notDeptCode,emp_notification.off_code notOffCode,"
                    + " auth,note,emp_cadre.wefd cadreWEFDate,emp_cadre.weft cadreWEFTime,pay_id,emp_pay_record.wef payrecordWEFDate,emp_pay_record.weft payrecordWEFTime,emp_pay_record.pay_scale,pay,s_pay,p_pay,"
                    + " oth_pay,emp_pay_record.gp,oth_desc from"
                    + " (select not_id,ordno,orddt,dept_code,off_code,auth,note,if_visible from emp_notification where EMP_ID=? AND NOT_ID=?)emp_notification"
                    + " left outer join"
                    + " (select pay_id,not_id,wef,weft,pay_scale,pay,s_pay,p_pay,oth_pay,gp,oth_desc from emp_pay_record where EMP_ID=? AND NOT_ID=?)emp_pay_record"
                    + " on emp_notification.not_id=emp_pay_record.not_id"
                    + " left outer join emp_cadre on emp_notification.not_id=emp_cadre.not_id";
            pst = con.prepareStatement(sql);
            pst.setString(1, repatriationForm.getEmpid());
            pst.setInt(2, notId);
            pst.setString(3, repatriationForm.getEmpid());
            pst.setInt(4, notId);
            rs = pst.executeQuery();
            if (rs.next()) {
                //repatriationForm.setPromotionId(rs.getString("pro_id"));
                repatriationForm.setTxtNotOrdNo(rs.getString("ordno"));
                repatriationForm.setHnotid(rs.getInt("not_id"));
                repatriationForm.setTxtNotOrdDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("orddt")));
                repatriationForm.setHidNotifyingDeptCode(rs.getString("notDeptCode"));
                repatriationForm.setHidNotifyingOffCode(rs.getString("notOffCode"));
                repatriationForm.setNotifyingSpc(rs.getString("auth"));
                repatriationForm.setNotifyingPostName(CommonFunctions.getSPN(con, rs.getString("auth")));

                repatriationForm.sethCadId(rs.getString("cad_id"));
                repatriationForm.setSltCadreDept(rs.getString("cadreDept"));
                repatriationForm.setSltCadre(rs.getString("cadre_code"));
                repatriationForm.setSltGrade(rs.getString("grade"));
                repatriationForm.setSltCadreLevel(rs.getString("lvl"));
                repatriationForm.setSltDescription(rs.getString("description"));
                repatriationForm.setTxtCadreJoiningWEFDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("cadreWEFDate")));
                repatriationForm.setSltCadreJoiningWEFTime(rs.getString("cadreWEFTime"));

                repatriationForm.setSltPostingDept(rs.getString("dept_code"));
                repatriationForm.setSltGenericPost(rs.getString("post_code"));

                repatriationForm.setRdPostClassification(rs.getString("post_class"));
                repatriationForm.setRdPostStatus(rs.getString("post_stat"));

                repatriationForm.setHpayid(rs.getInt("pay_id"));
                repatriationForm.setSltPayScale(rs.getString("pay_scale"));
                repatriationForm.setTxtGP(rs.getString("gp"));
                repatriationForm.setTxtBasic(rs.getString("pay"));
                repatriationForm.setTxtSP(rs.getString("s_pay"));
                repatriationForm.setTxtPP(rs.getString("p_pay"));
                repatriationForm.setTxtOP(rs.getString("oth_pay"));
                repatriationForm.setTxtDescOP(rs.getString("oth_desc"));
                repatriationForm.setTxtWEFDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("payrecordWEFDate")));
                repatriationForm.setSltWEFTime(rs.getString("payrecordWEFTime"));
                repatriationForm.setNote(rs.getString("note"));
                if (rs.getString("if_visible") != null && rs.getString("if_visible").equals("N")) {
                    repatriationForm.setChkNotSBPrint("Y");
                } else {
                    repatriationForm.setChkNotSBPrint("N");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return repatriationForm;
    }
}
