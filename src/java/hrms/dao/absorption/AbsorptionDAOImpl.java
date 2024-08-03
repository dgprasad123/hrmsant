package hrms.dao.absorption;

import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.dao.emppayrecord.EmpPayRecordDAO;
import hrms.dao.leaveAccount.ServiceBookLanguageDAO;
import hrms.dao.master.CadreDAO;
import hrms.dao.notification.NotificationDAO;
import hrms.model.absorption.Absorption;
import hrms.model.absorption.AbsorptionModel;
import hrms.model.cadre.Cadre;
import hrms.model.emppayrecord.EmpPayRecordForm;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;

public class AbsorptionDAOImpl implements AbsorptionDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;
    protected EmpPayRecordDAO emppayrecordDAO;

    protected CadreDAO cadreDAO;
   protected ServiceBookLanguageDAO sbDAO;
   protected NotificationDAO notificationDao;

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

    public void setSbDAO(ServiceBookLanguageDAO sbDAO) {
        this.sbDAO = sbDAO;
    }

    public void setNotificationDao(NotificationDAO notificationDao) {
        this.notificationDao = notificationDao;
    }
    

    public int insertAbsorptionData(AbsorptionModel absorp, int notId) {
        int n = 0;
        PreparedStatement pst = null;
        Connection con = null;
        EmpPayRecordForm epayrecordform = null;
        Cadre cadreForm = new Cadre();
        try {
            con = this.dataSource.getConnection();
            pst = con.prepareStatement("UPDATE EMP_MAST SET cur_salary=?,cur_basic_salary=?,gp=? WHERE EMP_ID=?");
            // pst.setString(1, absorp.getHidPostedOffCode());
            pst.setString(1, absorp.getSltPayScale());
            if (absorp.getTxtBasic() != null && !absorp.getTxtBasic().equals("")) {
                pst.setDouble(2, Double.parseDouble(absorp.getTxtBasic()));
            } else {
                pst.setDouble(2, 0);
            }
            if (absorp.getTxtGP() != null && !absorp.getTxtGP().equals("")) {
                pst.setDouble(3, Double.parseDouble(absorp.getTxtGP()));
            } else {
                pst.setDouble(3, 0);
            }
            pst.setString(4, absorp.getEmpid());
            pst.executeUpdate();
            epayrecordform = new EmpPayRecordForm();
            epayrecordform.setEmpid(absorp.getEmpid());
            epayrecordform.setNot_id(notId);
            epayrecordform.setNot_type("ABSORPTION");
            epayrecordform.setPayscale(absorp.getSltPayScale());
            epayrecordform.setBasic(absorp.getTxtBasic());
            epayrecordform.setGp(absorp.getTxtGP());
            epayrecordform.setS_pay(absorp.getTxtSP());
            epayrecordform.setP_pay(absorp.getTxtPP());
            epayrecordform.setOth_pay(absorp.getTxtOP());
            epayrecordform.setOth_desc(absorp.getTxtDescOP());
            epayrecordform.setWefDt(absorp.getTxtWEFDt());
            epayrecordform.setWefTime(absorp.getSltWEFTime());
            emppayrecordDAO.saveEmpPayRecordData(epayrecordform);

            cadreForm.setEmpId(absorp.getEmpid());
            cadreForm.setNotId(notId);
            cadreForm.setNotType("ABSORPTION");
            cadreForm.setCadreDept(absorp.getSltCadreDept());
            cadreForm.setCadreName(absorp.getSltCadre());
            cadreForm.setGrade(absorp.getSltGrade());
            cadreForm.setCadreLvl(absorp.getSltCadreLevel());
            cadreForm.setDescription(absorp.getSltDescription());
            cadreForm.setAllotmentYear(absorp.getTxtAllotmentYear());
            cadreForm.setCadreId(absorp.getTxtCadreId());
            cadreForm.setPostingDept(absorp.getSltPostingDept());
            cadreForm.setPostCode(absorp.getSltGenericPost());
            cadreForm.setPostClassification(absorp.getRdPostClassification());
            cadreForm.setPostStatus(absorp.getRdPostStatus());
            cadreForm.setCadreJoiningWEFDt(absorp.getTxtCadreJoiningWEFDt());
            cadreForm.setCadreJoiningWEFTime(absorp.getSltCadreJoiningWEFTime());
            cadreForm.setJoinedAsSuch(absorp.getChkJoinedAsSuch());
            //cadreForm.setIfProforma(absorp.getChkProformaPromotion());
            cadreDAO.saveCadreData(cadreForm);
            String sbLang=sbDAO.getAbsorptionLangDetails(absorp, notId);
            notificationDao.saveServiceBookLanguage(sbLang, notId, "ABSORPTION");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return n;
    }

    public int updateAbsorptionData(AbsorptionModel absorp) {
        int n = 0;
        PreparedStatement pst = null;
        Connection con = null;
        EmpPayRecordForm epayrecordform = null;
        Cadre cadreForm = new Cadre();
        try {
            con = this.dataSource.getConnection();
            pst = con.prepareStatement("UPDATE EMP_MAST SET cur_salary=?,cur_basic_salary=?,gp=? WHERE EMP_ID=?");
            // pst.setString(1, absorp.getHidPostedOffCode());
            pst.setString(1, absorp.getSltPayScale());
            if (absorp.getTxtBasic() != null && !absorp.getTxtBasic().equals("")) {
                pst.setDouble(2, Double.parseDouble(absorp.getTxtBasic()));
            } else {
                pst.setDouble(2, 0);
            }
            if (absorp.getTxtGP() != null && !absorp.getTxtGP().equals("")) {
                pst.setDouble(3, Double.parseDouble(absorp.getTxtGP()));
            } else {
                pst.setDouble(3, 0);
            }
            pst.setString(4, absorp.getEmpid());
            pst.executeUpdate();
            epayrecordform = new EmpPayRecordForm();
            epayrecordform.setPayid(absorp.getHpayid());
            epayrecordform.setEmpid(absorp.getEmpid());
            epayrecordform.setPayscale(absorp.getSltPayScale());
            epayrecordform.setBasic(absorp.getTxtBasic());
            epayrecordform.setGp(absorp.getTxtGP());
            epayrecordform.setS_pay(absorp.getTxtSP());
            epayrecordform.setP_pay(absorp.getTxtPP());
            epayrecordform.setOth_pay(absorp.getTxtOP());
            epayrecordform.setOth_desc(absorp.getTxtDescOP());
            epayrecordform.setWefDt(absorp.getTxtWEFDt());
            epayrecordform.setWefTime(absorp.getSltWEFTime());
            emppayrecordDAO.updateEmpPayRecordData(epayrecordform);

            cadreForm.setCadreCode(absorp.gethCadId());
            cadreForm.setEmpId(absorp.getEmpid());
            cadreForm.setCadreDept(absorp.getSltCadreDept());
            cadreForm.setCadreName(absorp.getSltCadre());
            cadreForm.setGrade(absorp.getSltGrade());
            cadreForm.setCadreLvl(absorp.getSltCadreLevel());
            cadreForm.setDescription(absorp.getSltDescription());
            cadreForm.setAllotmentYear(absorp.getTxtAllotmentYear());

            cadreForm.setCadreId(absorp.getTxtCadreId());
            cadreForm.setPostingDept(absorp.getSltPostingDept());
            cadreForm.setPostCode(absorp.getSltGenericPost());
            cadreForm.setPostClassification(absorp.getRdPostClassification());
            cadreForm.setPostStatus(absorp.getRdPostStatus());
            cadreForm.setCadreJoiningWEFDt(absorp.getTxtCadreJoiningWEFDt());
            cadreForm.setCadreJoiningWEFTime(absorp.getSltCadreJoiningWEFTime());
            cadreForm.setJoinedAsSuch(absorp.getChkJoinedAsSuch());
            //cadreForm.setIfProforma(absorp.getChkProformaPromotion());
            cadreDAO.updateCadreData(cadreForm);
            String sbLang=sbDAO.getAbsorptionLangDetails(absorp, absorp.getHnotid());
            notificationDao.saveServiceBookLanguage(sbLang, absorp.getHnotid(), "ABSORPTION");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return n;
    }

    public int deleteAbsorptionData(AbsorptionModel absorptionForm) {
        int n = 0;
        EmpPayRecordForm epayrecordform = new EmpPayRecordForm();
        Cadre cadreForm = new Cadre();
        try {
            epayrecordform.setEmpid(absorptionForm.getEmpid());
            epayrecordform.setPayid(absorptionForm.getHpayid());
            emppayrecordDAO.deleteEmpPayRecordData(epayrecordform);
            cadreForm.setCadreId(absorptionForm.gethCadId());
            cadreDAO.deleteCadreData(cadreForm);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return n;
    }

    public AbsorptionModel editAbsorptionData(int notId, String empId) {
        AbsorptionModel absorp = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("select emp_id,cad_id,pay_id,joined_assuch,if_proforma,post_class,post_stat,emp_cadre.dept_code cadreDept,emp_cadre.cadre_code,grade,"
                    + " lvl,description,emp_cadre.posting_dept_code,emp_cadre.post_code, emp_cadre.wefd cadreWEFDate,emp_cadre.weft cadreWEFTime,cadreid,allot_year,\n"
                    + " emp_notification.not_id,ordno,orddt,emp_notification.dept_code notDeptCode,emp_notification.off_code notOffCode,auth,note,if_visible, "
                    + " emp_pay_record.wef payrecordWEFDate,emp_pay_record.weft payrecordWEFTime,emp_pay_record.pay_scale,pay,s_pay,p_pay,oth_pay,emp_pay_record.gp,oth_desc from "
                    + "(select not_id,ordno,orddt,dept_code,off_code,auth,note,if_visible from emp_notification where EMP_ID=? AND NOT_ID=?)emp_notification "
                    + " left outer join (select pay_id,not_id,wef,weft,pay_scale,pay,s_pay,p_pay,oth_pay,gp,oth_desc from emp_pay_record "
                    + "where EMP_ID=? AND NOT_ID=?)emp_pay_record "
                    + " on emp_notification.not_id=emp_pay_record.not_id "
                    + " left outer join emp_cadre on emp_notification.not_id=emp_cadre.not_id");
            pst.setString(1, empId);
            pst.setInt(2, notId);
            pst.setString(3, empId);
            pst.setInt(4, notId);
            rs = pst.executeQuery();
            if (rs.next()) {
                absorp = new AbsorptionModel();
                absorp.setNotType("ABSORPTION");
                absorp.setEmpid(rs.getString("EMP_ID"));

                absorp.setOrdno(rs.getString("ORDNO"));
                absorp.setOrdDate(rs.getString("ORDDT"));

                absorp.setHidNotifyingDeptCode(rs.getString("notDeptCode"));
                absorp.setHidNotifyingOffCode(rs.getString("notOffCode"));
                absorp.setHidTempNotifyingOffCode(rs.getString("notOffCode"));
                absorp.setNotifyingSpc(rs.getString("auth"));
                absorp.setHidTempNotifyingPost(rs.getString("auth"));
                absorp.setNotifyingPostName(CommonFunctions.getSPN(con, rs.getString("auth")));

                //absorp.setSltAllotmentDesc(rs.getString("allot_desc"));
                //  absorp.setChkRetroPromotion(rs.getString("if_retrospective"));
                absorp.sethCadId(rs.getString("cad_id"));
                absorp.setSltCadreDept(rs.getString("cadreDept"));
                absorp.setSltCadre(rs.getString("cadre_code"));
                absorp.setHidTempCadre(rs.getString("cadre_code"));
                absorp.setSltGrade(rs.getString("grade"));
                absorp.setHidTempGrade(rs.getString("grade"));
                absorp.setSltCadreLevel(rs.getString("lvl"));
                absorp.setSltDescription(rs.getString("description"));
                absorp.setHidTempDescription(rs.getString("description"));
                absorp.setTxtAllotmentYear(rs.getString("allot_year"));
                absorp.setTxtCadreId(rs.getString("cadreid"));
                absorp.setTxtCadreJoiningWEFDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("cadreWEFDate")));
                absorp.setSltCadreJoiningWEFTime(rs.getString("cadreWEFTime"));

                absorp.setSltPostingDept(rs.getString("posting_dept_code"));
                absorp.setSltGenericPost(rs.getString("post_code"));
                absorp.setHidTempGenericPost(rs.getString("post_code"));

                absorp.setRdPostClassification(rs.getString("post_class"));
                absorp.setRdPostStatus(rs.getString("post_stat"));

                absorp.setChkJoinedAsSuch(rs.getString("joined_assuch"));
                absorp.setChkProformaPromotion(rs.getString("if_proforma"));

                absorp.setHpayid(rs.getInt("pay_id"));
                absorp.setSltPayScale(rs.getString("pay_scale"));
                absorp.setTxtGP(rs.getString("gp"));
                absorp.setTxtBasic(rs.getString("pay"));
                absorp.setTxtSP(rs.getString("s_pay"));
                absorp.setTxtPP(rs.getString("p_pay"));
                absorp.setTxtOP(rs.getString("oth_pay"));
                absorp.setTxtDescOP(rs.getString("oth_desc"));
                absorp.setTxtWEFDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("payrecordWEFDate")));
                absorp.setSltWEFTime(rs.getString("payrecordWEFTime"));
                absorp.setNote(rs.getString("note"));
                if (rs.getString("if_visible") != null && rs.getString("if_visible").equals("N")) {
                    absorp.setChkNotSBPrint("Y");
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return absorp;
    }

    public List findAllAbsorption(String empId) {
        List list = null;
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        Absorption absorpBean = null;
        list = new ArrayList();
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("select emp_id,doe,cad_id,pay_id,joined_assuch,if_proforma,post_class,post_stat,emp_cadre.dept_code cadreDept,emp_cadre.cadre_code,grade,\n"
                    + " lvl,description,emp_cadre.posting_dept_code,emp_cadre.post_code, emp_cadre.wefd cadreWEFDate,emp_cadre.weft cadreWEFTime,cadreid,allot_year,\n"
                    + "emp_notification.not_id,ordno,orddt,emp_notification.dept_code notDeptCode,emp_notification.off_code notOffCode,auth,note, \n"
                    + "emp_pay_record.wef payrecordWEFDate,emp_pay_record.weft payrecordWEFTime,emp_pay_record.pay_scale,pay,s_pay,p_pay,oth_pay,emp_pay_record.gp,oth_desc from \n"
                    + "(select not_id,doe,ordno,orddt,dept_code,off_code,auth,note from emp_notification where EMP_ID=? AND NOT_TYPE='ABSORPTION')emp_notification \n"
                    + " left outer join (select pay_id,not_id,wef,weft,pay_scale,pay,s_pay,p_pay,oth_pay,gp,oth_desc from emp_pay_record \n"
                    + "where EMP_ID=? AND NOT_TYPE='ABSORPTION')emp_pay_record \n"
                    + "on emp_notification.not_id=emp_pay_record.not_id \n"
                    + " left outer join emp_cadre on emp_notification.not_id=emp_cadre.not_id");
            pst.setString(1, empId);
            pst.setString(2, empId);
            rs = pst.executeQuery();
            while (rs.next()) {
                absorpBean = new Absorption();
                absorpBean.setHpayid(rs.getString("pay_id"));
                absorpBean.sethCadId(rs.getString("cad_id"));
                absorpBean.setNotId(rs.getString("NOT_ID"));
                absorpBean.setNotType("ABSORPTION");
                absorpBean.setDoe(CommonFunctions.getFormattedOutputDate1(rs.getDate("doe")));
                absorpBean.setOrdno(rs.getString("ORDNO"));
                absorpBean.setOrdDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("ORDDT")));
                list.add(absorpBean);
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
