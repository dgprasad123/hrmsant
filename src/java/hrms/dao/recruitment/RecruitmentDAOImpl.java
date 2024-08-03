package hrms.dao.recruitment;

import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.dao.emppayrecord.EmpPayRecordDAO;
import hrms.dao.leaveAccount.ServiceBookLanguageDAO;
import hrms.dao.master.CadreDAO;
import hrms.model.cadre.Cadre;
import hrms.model.emppayrecord.EmpPayRecordForm;
import hrms.model.recruitment.Recruitment;
import hrms.model.recruitment.RecruitmentModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;

public class RecruitmentDAOImpl implements RecruitmentDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    protected EmpPayRecordDAO emppayrecordDAO;

    protected CadreDAO cadreDAO;

    protected ServiceBookLanguageDAO sbDAO;

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

    public void insertRecruitmentData(RecruitmentModel recruit, int notId) {

        Connection con = null;

        PreparedStatement pst = null;

        EmpPayRecordForm epayrecordform = new EmpPayRecordForm();
        Cadre cadreForm = new Cadre();
        try {
            con = this.dataSource.getConnection();

            epayrecordform.setEmpid(recruit.getEmpid());
            epayrecordform.setNot_id(notId);
            epayrecordform.setNot_type(recruit.getSltNotificationType());
            epayrecordform.setPayscale(recruit.getSltPayScale());
            epayrecordform.setBasic(recruit.getTxtBasic());
            epayrecordform.setGp(recruit.getTxtGP());
            epayrecordform.setS_pay(recruit.getTxtSP());
            epayrecordform.setP_pay(recruit.getTxtPP());
            epayrecordform.setOth_pay(recruit.getTxtOP());
            epayrecordform.setOth_desc(recruit.getTxtDescOP());
            epayrecordform.setWefDt(recruit.getTxtWEFDt());
            epayrecordform.setWefTime(recruit.getSltWEFTime());
            emppayrecordDAO.saveEmpPayRecordData(epayrecordform);

            cadreForm.setEmpId(recruit.getEmpid());
            cadreForm.setNotId(notId);
            cadreForm.setNotType(recruit.getSltNotificationType());
            cadreForm.setCadreDept(recruit.getSltCadreDept());
            cadreForm.setCadreName(recruit.getSltCadre());
            cadreForm.setGrade(recruit.getSltGrade());
            cadreForm.setCadreLvl(recruit.getSltCadreLevel());
            cadreForm.setDescription(recruit.getSltDescription());
            cadreForm.setAllotmentYear(recruit.getTxtAllotmentYear());
            cadreForm.setCadreId(recruit.getTxtCadreId());
            cadreForm.setPostingDept(recruit.getSltPostingDept());
            cadreForm.setPostCode(recruit.getSltGenericPost());
            cadreForm.setPostClassification(recruit.getRdPostClassification());
            cadreForm.setPostStatus(recruit.getRdPostStatus());
            cadreForm.setCadreJoiningWEFDt(recruit.getTxtCadreJoiningWEFDt());
            cadreForm.setCadreJoiningWEFTime(recruit.getSltCadreJoiningWEFTime());
            cadreForm.setJoinedAsSuch(recruit.getChkJoinedAsSuch());
            cadreForm.setIfProforma(recruit.getChkProformaPromotion());
            cadreDAO.saveCadreData(cadreForm);

            if (recruit.getRdTransaction() != null && recruit.getRdTransaction().equalsIgnoreCase("C")) {
                String sql = "UPDATE EMP_MAST SET CUR_CADRE_CODE=?,cur_basic_salary=?,post_grp_type=? WHERE EMP_ID=? ";
                pst = con.prepareStatement(sql);
                pst.setString(1, recruit.getSltCadre());
                pst.setInt(2, Integer.parseInt(recruit.getTxtBasic()));
                pst.setString(3, recruit.getSltPostGroup());
                pst.setString(4, recruit.getEmpid());
                pst.executeUpdate();
            }

            /*
             * Updating the Service Book Language
             */
            String sbLang = sbDAO.getFirstAppointDetails(recruit, notId, recruit.getSltNotificationType());
            pst = con.prepareStatement("UPDATE EMP_NOTIFICATION SET SB_DESCRIPTION =? WHERE NOT_ID=? ");
            pst.setString(1, sbLang);
            pst.setInt(2, notId);
            pst.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    public void updateRecruitmentData(RecruitmentModel recruit) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        EmpPayRecordForm epayrecordform = new EmpPayRecordForm();
        Cadre cadreForm = new Cadre();
        try {
            con = this.dataSource.getConnection();

            if (recruit.getHpayid() > 0) {

                epayrecordform.setPayid(recruit.getHpayid());
                epayrecordform.setEmpid(recruit.getEmpid());
                epayrecordform.setPayscale(recruit.getSltPayScale());
                epayrecordform.setBasic(recruit.getTxtBasic());
                epayrecordform.setGp(recruit.getTxtGP());
                epayrecordform.setS_pay(recruit.getTxtSP());
                epayrecordform.setP_pay(recruit.getTxtPP());
                epayrecordform.setOth_pay(recruit.getTxtOP());
                epayrecordform.setOth_desc(recruit.getTxtDescOP());
                epayrecordform.setWefDt(recruit.getTxtWEFDt());
                epayrecordform.setWefTime(recruit.getSltWEFTime());
                emppayrecordDAO.updateEmpPayRecordData(epayrecordform);
            } else {
                epayrecordform.setEmpid(recruit.getEmpid());
                epayrecordform.setNot_id(recruit.getHnotid());
                epayrecordform.setNot_type(recruit.getSltNotificationType());
                epayrecordform.setPayscale(recruit.getSltPayScale());
                epayrecordform.setBasic(recruit.getTxtBasic());
                epayrecordform.setGp(recruit.getTxtGP());
                epayrecordform.setS_pay(recruit.getTxtSP());
                epayrecordform.setP_pay(recruit.getTxtPP());
                epayrecordform.setOth_pay(recruit.getTxtOP());
                epayrecordform.setOth_desc(recruit.getTxtDescOP());
                epayrecordform.setWefDt(recruit.getTxtWEFDt());
                epayrecordform.setWefTime(recruit.getSltWEFTime());
                emppayrecordDAO.saveEmpPayRecordData(epayrecordform);
            }

            if (recruit.getHcadId() != null && !recruit.getHcadId().equals("")) {
                cadreForm.setCadreCode(recruit.getHcadId());
                cadreForm.setEmpId(recruit.getEmpid());
                cadreForm.setCadreDept(recruit.getSltCadreDept());
                cadreForm.setCadreName(recruit.getSltCadre());
                cadreForm.setGrade(recruit.getSltGrade());
                cadreForm.setCadreLvl(recruit.getSltCadreLevel());
                cadreForm.setDescription(recruit.getSltDescription());
                cadreForm.setAllotmentYear(recruit.getTxtAllotmentYear());
                cadreForm.setCadreId(recruit.getTxtCadreId());
                cadreForm.setPostingDept(recruit.getSltPostingDept());
                cadreForm.setPostCode(recruit.getSltGenericPost());
                cadreForm.setPostClassification(recruit.getRdPostClassification());
                cadreForm.setPostStatus(recruit.getRdPostStatus());
                cadreForm.setCadreJoiningWEFDt(recruit.getTxtCadreJoiningWEFDt());
                cadreForm.setCadreJoiningWEFTime(recruit.getSltCadreJoiningWEFTime());
                cadreForm.setIfJoined(recruit.getChkJoinedAsSuch());
                cadreForm.setIfProforma(recruit.getChkProformaPromotion());
                cadreDAO.updateCadreData(cadreForm);
            } else {
                cadreForm.setEmpId(recruit.getEmpid());
                cadreForm.setNotId(recruit.getHnotid());
                cadreForm.setNotType(recruit.getSltNotificationType());
                cadreForm.setCadreDept(recruit.getSltCadreDept());
                cadreForm.setCadreName(recruit.getSltCadre());
                cadreForm.setGrade(recruit.getSltGrade());
                cadreForm.setCadreLvl(recruit.getSltCadreLevel());
                cadreForm.setDescription(recruit.getSltDescription());
                cadreForm.setAllotmentYear(recruit.getTxtAllotmentYear());
                cadreForm.setCadreId(recruit.getTxtCadreId());
                cadreForm.setPostingDept(recruit.getSltPostingDept());
                cadreForm.setPostCode(recruit.getSltGenericPost());
                cadreForm.setPostClassification(recruit.getRdPostClassification());
                cadreForm.setPostStatus(recruit.getRdPostStatus());
                cadreForm.setCadreJoiningWEFDt(recruit.getTxtCadreJoiningWEFDt());
                cadreForm.setCadreJoiningWEFTime(recruit.getSltCadreJoiningWEFTime());
                cadreForm.setJoinedAsSuch(recruit.getChkJoinedAsSuch());
                cadreForm.setIfProforma(recruit.getChkProformaPromotion());
                cadreDAO.saveCadreData(cadreForm);
            }

            pst = con.prepareStatement("UPDATE EMP_NOTIFICATION SET NOT_TYPE=? WHERE NOT_ID=?");
            pst.setString(1, recruit.getSltNotificationType());
            pst.setInt(2, recruit.getHnotid());
            pst.execute();
            /*
             * Updating the Service Book Language
             */
            String sbLang = sbDAO.getFirstAppointDetails(recruit, recruit.getHnotid(), recruit.getSltNotificationType());
            pst = con.prepareStatement("UPDATE EMP_NOTIFICATION SET SB_DESCRIPTION =? WHERE NOT_ID=? ");
            pst.setString(1, sbLang);
            pst.setInt(2, recruit.getHnotid());
            pst.execute();

            if (recruit.getRdTransaction() != null && recruit.getRdTransaction().equalsIgnoreCase("C")) {
                String sql = "UPDATE EMP_MAST SET CUR_CADRE_CODE=?,cur_basic_salary=?,post_grp_type=? WHERE EMP_ID=?";
                pst = con.prepareStatement(sql);
                pst.setString(1, recruit.getSltCadre());
                pst.setInt(2, Integer.parseInt(recruit.getTxtBasic()));
                pst.setString(3, recruit.getSltPostGroup());                
                pst.setString(4, recruit.getEmpid());
                pst.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    public void deleteRecruitment(RecruitmentModel recruit) {

        EmpPayRecordForm epayrecordform = new EmpPayRecordForm();
        Cadre cadreForm = new Cadre();

        try {
            epayrecordform.setEmpid(recruit.getEmpid());
            epayrecordform.setPayid(recruit.getHpayid());
            emppayrecordDAO.deleteEmpPayRecordData(epayrecordform);

            cadreForm.setCadreCode(recruit.getHcadId());
            cadreDAO.deleteCadreData(cadreForm);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    public RecruitmentModel editRecruitment(String empid, int notId) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        RecruitmentModel recruit = null;

        try {
            con = this.dataSource.getConnection();

            String sql = "select"
                    + " (select post_grp_type from emp_mast where emp_id=emp_notification.emp_id)post_grp_type,emp_notification.if_visible,emp_notification.organization_type_posting,emp_notification.not_type,cad_id,joined_assuch,if_proforma,post_class,post_stat,emp_cadre.dept_code cadreDept,emp_cadre.cadre_code,grade,lvl,description,emp_cadre.posting_dept_code,emp_cadre.post_code,allot_year,cadreid,emp_notification.not_id,ordno,orddt,emp_notification.dept_code notDeptCode,emp_notification.off_code notOffCode,"
                    + " auth,note,emp_cadre.wefd cadreWEFDate,emp_cadre.weft cadreWEFTime,pay_id,"
                    + " emp_pay_record.wef payrecordWEFDate,emp_pay_record.weft payrecordWEFTime,emp_pay_record.pay_scale,pay,s_pay,p_pay,oth_pay,emp_pay_record.gp,oth_desc from"
                    + " (select emp_id,organization_type_posting,if_visible,not_id,not_type,ordno,orddt,dept_code,off_code,auth,note from emp_notification where EMP_ID=? AND NOT_ID=?)emp_notification"
                    + " left outer join (select pay_id,not_id,wef,weft,pay_scale,pay,s_pay,p_pay,oth_pay,gp,oth_desc from emp_pay_record"
                    + " where EMP_ID=? AND NOT_ID=?)emp_pay_record"
                    + " on emp_notification.not_id=emp_pay_record.not_id"
                    + " left outer join emp_cadre on emp_notification.not_id=emp_cadre.not_id";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            pst.setInt(2, notId);
            pst.setString(3, empid);
            pst.setInt(4, notId);
            rs = pst.executeQuery();
            if (rs.next()) {
                recruit = new RecruitmentModel();
                recruit.setSltNotificationType(rs.getString("not_type"));
                recruit.setEmpid(empid);
                recruit.setTxtNotOrdNo(rs.getString("ordno"));
                recruit.setTxtNotOrdDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("orddt")));
                recruit.setHidNotifyingDeptCode(rs.getString("notDeptCode"));
                recruit.setHidNotifyingOffCode(rs.getString("notOffCode"));
                recruit.setNotifyingSpc(rs.getString("auth"));
                recruit.setNotifyingPostName(CommonFunctions.getSPN(con, rs.getString("auth")));

                recruit.setHcadId(rs.getString("cad_id"));
                recruit.setSltCadreDept(rs.getString("cadreDept"));
                recruit.setSltCadre(rs.getString("cadre_code"));
                recruit.setSltGrade(rs.getString("grade"));
                recruit.setSltCadreLevel(rs.getString("lvl"));
                recruit.setSltDescription(rs.getString("description"));
                recruit.setTxtAllotmentYear(rs.getString("allot_year"));
                recruit.setTxtCadreId(rs.getString("cadreid"));
                recruit.setSltPostingDept(rs.getString("posting_dept_code"));
                recruit.setSltGenericPost(rs.getString("post_code"));
                recruit.setRdPostClassification(rs.getString("post_class"));
                recruit.setRdPostStatus(rs.getString("post_stat"));
                recruit.setTxtCadreJoiningWEFDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("cadreWEFDate")));
                recruit.setSltCadreJoiningWEFTime(rs.getString("cadreWEFTime"));

                recruit.setHpayid(rs.getInt("pay_id"));
                recruit.setTxtWEFDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("payrecordWEFDate")));
                recruit.setSltWEFTime(rs.getString("payrecordWEFTime"));
                recruit.setSltPayScale(rs.getString("pay_scale"));
                recruit.setTxtGP(rs.getString("gp"));
                recruit.setTxtBasic(rs.getString("pay"));
                recruit.setTxtPP(rs.getString("p_pay"));
                recruit.setTxtOP(rs.getString("oth_pay"));

                recruit.setNote(rs.getString("note"));

                if (rs.getString("if_visible") != null && rs.getString("if_visible").equals("N")) {
                    recruit.setChkNotSBPrint("Y");
                }

                if (rs.getString("organization_type_posting") != null && rs.getString("organization_type_posting").equals("GOI")) {
                    recruit.setRadpostingauthtype("GOI");
                    recruit.setHidOthSpc(rs.getString("auth"));
                    recruit.setNotifyingPostName(getOtherSpn(rs.getString("auth")));
                } else {
                    recruit.setRadpostingauthtype("GOO");
                }
                recruit.setSltPostGroup(rs.getString("post_grp_type"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return recruit;
    }

    public List findAllRecruitment(String empId) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        Recruitment recruitment = null;

        List list = new ArrayList();
        try {
            con = this.dataSource.getConnection();

            pst = con.prepareStatement("select is_validated,emp_notification.doe,emp_notification.not_id,emp_notification.not_type,ordno,orddt from"
                    + " (select is_validated,emp_id,doe,not_id,not_type,ordno,orddt from emp_notification where emp_id=? and not_type in ('FIRST_APPOINTMENT','REHABILITATION','VALIDATION'))emp_notification");
            pst.setString(1, empId);
            rs = pst.executeQuery();
            while (rs.next()) {
                recruitment = new Recruitment();
                recruitment.setNotId(rs.getString("NOT_ID"));
                recruitment.setNotType(rs.getString("NOT_TYPE"));
                recruitment.setDoe(CommonFunctions.getFormattedOutputDate1(rs.getDate("DOE")));
                recruitment.setOrdno(rs.getString("ORDNO"));
                recruitment.setOrdDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("ORDDT")));
                recruitment.setIsValidated(rs.getString("is_validated"));
                list.add(recruitment);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
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
}
