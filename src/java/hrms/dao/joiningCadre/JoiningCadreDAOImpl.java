package hrms.dao.joiningCadre;

import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.dao.emppayrecord.EmpPayRecordDAO;
import hrms.dao.leaveAccount.ServiceBookLanguageDAO;
import hrms.dao.master.CadreDAO;
import hrms.model.cadre.Cadre;
import hrms.model.emppayrecord.EmpPayRecordForm;
import hrms.model.joiningCadre.JoiningCadreForm;
import hrms.model.joiningCadre.JoiningCadreList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;

public class JoiningCadreDAOImpl implements JoiningCadreDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    protected EmpPayRecordDAO emppayrecordDAO;

    protected CadreDAO cadreDAO;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setEmppayrecordDAO(EmpPayRecordDAO emppayrecordDAO) {
        this.emppayrecordDAO = emppayrecordDAO;
    }

    public void setCadreDAO(CadreDAO cadreDAO) {
        this.cadreDAO = cadreDAO;
    }

    protected ServiceBookLanguageDAO sbDAO;

    public void setSbDAO(ServiceBookLanguageDAO sbDAO) {
        this.sbDAO = sbDAO;
    }

    @Override
    public List getJoiningCadreList(String empid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List joiningCadreList = new ArrayList();
        JoiningCadreList jcbean = null;
        try {
            con = this.dataSource.getConnection();

            String sql = "select emp_notification.doe,emp_notification.not_id,not_type,ordno,orddt from"
                    + " (select emp_id,doe,not_id,not_type,ordno,orddt from emp_notification where emp_id=? and not_type='JOIN_CADRE')emp_notification";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            rs = pst.executeQuery();
            while (rs.next()) {
                jcbean = new JoiningCadreList();
                jcbean.setDoe(CommonFunctions.getFormattedOutputDate1(rs.getDate("doe")));
                jcbean.setNotId(rs.getString("not_id"));
                if (rs.getString("not_type") != null && !rs.getString("not_type").equals("")) {
                    if (rs.getString("not_type").equals("JOIN_CADRE")) {
                        jcbean.setNotType("JOINING TO CADRE");
                    }
                }
                jcbean.setOrdNo(rs.getString("ordno"));
                jcbean.setOrdDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("orddt")));
                joiningCadreList.add(jcbean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return joiningCadreList;
    }

    @Override
    public void saveJoiningCadre(JoiningCadreForm joiningCadreForm, int notId) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        EmpPayRecordForm epayrecordform = new EmpPayRecordForm();
        Cadre cadreForm = new Cadre();
        try {
            con = this.dataSource.getConnection();

            epayrecordform.setEmpid(joiningCadreForm.getEmpid());
            epayrecordform.setNot_id(notId);
            epayrecordform.setNot_type("JOIN_CADRE");
            epayrecordform.setPayscale(joiningCadreForm.getSltPayScale());
            epayrecordform.setBasic(joiningCadreForm.getTxtBasic());
            epayrecordform.setGp(joiningCadreForm.getTxtGP());
            epayrecordform.setS_pay(joiningCadreForm.getTxtSP());
            epayrecordform.setP_pay(joiningCadreForm.getTxtPP());
            epayrecordform.setOth_pay(joiningCadreForm.getTxtOP());
            epayrecordform.setOth_desc(joiningCadreForm.getTxtDescOP());
            epayrecordform.setWefDt(joiningCadreForm.getTxtWEFDt());
            epayrecordform.setWefTime(joiningCadreForm.getSltWEFTime());
            emppayrecordDAO.saveEmpPayRecordData(epayrecordform);

            cadreForm.setEmpId(joiningCadreForm.getEmpid());
            cadreForm.setNotId(notId);
            cadreForm.setNotType("JOIN_CADRE");
            cadreForm.setCadreDept(joiningCadreForm.getSltCadreDept());
            cadreForm.setCadreName(joiningCadreForm.getSltCadre());
            cadreForm.setGrade(joiningCadreForm.getSltGrade());
            cadreForm.setCadreLvl(joiningCadreForm.getSltCadreLevel());
            cadreForm.setDescription(joiningCadreForm.getSltDescription());
            cadreForm.setAllotmentYear(joiningCadreForm.getTxtAllotmentYear());
            cadreForm.setCadreId(joiningCadreForm.getTxtCadreId());
            cadreForm.setPostingDept(joiningCadreForm.getSltPostingDept());
            cadreForm.setPostCode(joiningCadreForm.getSltGenericPost());
            cadreForm.setPostClassification(joiningCadreForm.getRdPostClassification());
            cadreForm.setPostStatus(joiningCadreForm.getRdPostStatus());
            cadreForm.setCadreJoiningWEFDt(joiningCadreForm.getTxtCadreJoiningWEFDt());
            cadreForm.setCadreJoiningWEFTime(joiningCadreForm.getSltCadreJoiningWEFTime());
            cadreDAO.saveCadreData(cadreForm);

            pst = con.prepareStatement("update emp_mast set cur_cadre_code=?,post_grp_type=? where emp_id=? ");
            pst.setString(1, joiningCadreForm.getSltCadre());
            pst.setString(2, joiningCadreForm.getSltPostGrp());
            pst.setString(3, joiningCadreForm.getEmpid());
            pst.execute();

            /*
             * Updating the Service Book Language
             */
            joiningCadreForm.setSltGrade(cadreDAO.getCadreGradeName(joiningCadreForm.getSltGrade()));
            String sbLanguage = sbDAO.getJoininginCadreDetails(joiningCadreForm, notId, "JOIN_CADRE");
            pst = con.prepareStatement("UPDATE EMP_NOTIFICATION SET SB_DESCRIPTION =? ,IF_VISIBLE='Y' WHERE NOT_ID=?");
            pst.setString(1, sbLanguage);
            pst.setInt(2, notId);
            pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void updateJoiningCadre(JoiningCadreForm joiningCadreForm) {

        Connection con = null;

        PreparedStatement pst = null;

        EmpPayRecordForm epayrecordform = new EmpPayRecordForm();
        Cadre cadreForm = new Cadre();
        try {
            con = this.dataSource.getConnection();

            epayrecordform.setPayid(joiningCadreForm.getHpayid());
            epayrecordform.setEmpid(joiningCadreForm.getEmpid());
            epayrecordform.setPayscale(joiningCadreForm.getSltPayScale());
            epayrecordform.setBasic(joiningCadreForm.getTxtBasic());
            epayrecordform.setGp(joiningCadreForm.getTxtGP());
            epayrecordform.setS_pay(joiningCadreForm.getTxtSP());
            epayrecordform.setP_pay(joiningCadreForm.getTxtPP());
            epayrecordform.setOth_pay(joiningCadreForm.getTxtOP());
            epayrecordform.setOth_desc(joiningCadreForm.getTxtDescOP());
            epayrecordform.setWefDt(joiningCadreForm.getTxtWEFDt());
            epayrecordform.setWefTime(joiningCadreForm.getSltWEFTime());
            emppayrecordDAO.updateEmpPayRecordData(epayrecordform);

            cadreForm.setNotType("JOIN_CADRE");
            cadreForm.setNotId(joiningCadreForm.getHnotid());
            cadreForm.setCadreCode(joiningCadreForm.gethCadId());
            cadreForm.setEmpId(joiningCadreForm.getEmpid());
            cadreForm.setCadreDept(joiningCadreForm.getSltCadreDept());
            cadreForm.setCadreName(joiningCadreForm.getSltCadre());
            cadreForm.setGrade(joiningCadreForm.getSltGrade());
            cadreForm.setCadreLvl(joiningCadreForm.getSltCadreLevel());
            cadreForm.setDescription(joiningCadreForm.getSltDescription());
            cadreForm.setAllotmentYear(joiningCadreForm.getTxtAllotmentYear());
            cadreForm.setCadreId(joiningCadreForm.getTxtCadreId());
            cadreForm.setPostingDept(joiningCadreForm.getSltPostingDept());
            cadreForm.setPostCode(joiningCadreForm.getSltGenericPost());
            cadreForm.setPostClassification(joiningCadreForm.getRdPostClassification());
            cadreForm.setPostStatus(joiningCadreForm.getRdPostStatus());
            cadreForm.setCadreJoiningWEFDt(joiningCadreForm.getTxtCadreJoiningWEFDt());
            cadreForm.setCadreJoiningWEFTime(joiningCadreForm.getSltCadreJoiningWEFTime());
            cadreDAO.updateCadreData(cadreForm);

            pst = con.prepareStatement("update emp_mast set cur_cadre_code=?,post_grp_type=? where emp_id=? ");
            pst.setString(1, joiningCadreForm.getSltCadre());
            pst.setString(2, joiningCadreForm.getSltPostGrp());
            pst.setString(3, joiningCadreForm.getEmpid());
            pst.execute();
            /*
             * Updating the Service Book Language
             */
            
            joiningCadreForm.setSltGrade(cadreDAO.getCadreGradeName(joiningCadreForm.getSltGrade()));            
            String sbLanguage = sbDAO.getJoininginCadreDetails(joiningCadreForm, joiningCadreForm.getHnotid(), "JOIN_CADRE");
            pst = con.prepareStatement("UPDATE EMP_NOTIFICATION SET SB_DESCRIPTION =? WHERE NOT_ID=?");
            pst.setString(1, sbLanguage);
            pst.setInt(2, joiningCadreForm.getHnotid());
            pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public JoiningCadreForm getEmpJoiningCadreData(JoiningCadreForm joiningCadreForm, int notId) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = this.dataSource.getConnection();

            String sql = "select"
                    + " emp_cadre.cur_cadre_grade_code,post_grp_type,cad_id,joined_assuch,if_proforma,post_class,post_stat,emp_cadre.dept_code cadreDept,emp_cadre.cadre_code,grade,lvl,description,emp_cadre.posting_dept_code,emp_cadre.post_code,"
                    + " emp_notification.not_id,emp_notification.if_visible,ordno,orddt,emp_notification.dept_code notDeptCode,emp_notification.off_code notOffCode,"
                    + " auth,note,emp_cadre.wefd cadreWEFDate,emp_cadre.weft cadreWEFTime,pay_id,emp_pay_record.wef payrecordWEFDate,emp_pay_record.weft payrecordWEFTime,emp_pay_record.pay_scale,pay,s_pay,p_pay,"
                    + " oth_pay,emp_pay_record.gp,oth_desc from"
                    + " (select emp_id,not_id,ordno,orddt,dept_code,off_code,auth,note,if_visible from emp_notification where EMP_ID=? AND NOT_ID=?)emp_notification"
                    + " left outer join"
                    + " (select pay_id,not_id,wef,weft,pay_scale,pay,s_pay,p_pay,oth_pay,gp,oth_desc from emp_pay_record where EMP_ID=? AND NOT_ID=?)emp_pay_record"
                    + " on emp_notification.not_id=emp_pay_record.not_id"
                    + " left outer join emp_cadre on emp_notification.not_id=emp_cadre.not_id"
                    + " left outer join emp_mast on emp_notification.emp_id=emp_mast.emp_id";
            pst = con.prepareStatement(sql);
            pst.setString(1, joiningCadreForm.getEmpid());
            pst.setInt(2, notId);
            pst.setString(3, joiningCadreForm.getEmpid());
            pst.setInt(4, notId);
            rs = pst.executeQuery();
            if (rs.next()) {
                //repatriationForm.setPromotionId(rs.getString("pro_id"));
                joiningCadreForm.setTxtNotOrdNo(rs.getString("ordno"));
                joiningCadreForm.setHnotid(rs.getInt("not_id"));
                joiningCadreForm.setTxtNotOrdDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("orddt")));
                joiningCadreForm.setHidNotifyingDeptCode(rs.getString("notDeptCode"));
                joiningCadreForm.setHidNotifyingOffCode(rs.getString("notOffCode"));
                joiningCadreForm.setNotifyingSpc(rs.getString("auth"));
                joiningCadreForm.setNotifyingPostName(CommonFunctions.getSPN(con, rs.getString("auth")));

                joiningCadreForm.sethCadId(rs.getString("cad_id"));
                joiningCadreForm.setSltCadreDept(rs.getString("cadreDept"));
                joiningCadreForm.setSltCadre(rs.getString("cadre_code"));
                joiningCadreForm.setSltGrade(rs.getString("cur_cadre_grade_code"));
                joiningCadreForm.setSltCadreLevel(rs.getString("lvl"));
                joiningCadreForm.setSltDescription(rs.getString("description"));
                joiningCadreForm.setTxtCadreJoiningWEFDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("cadreWEFDate")));
                joiningCadreForm.setSltCadreJoiningWEFTime(rs.getString("cadreWEFTime"));

                joiningCadreForm.setSltPostingDept(rs.getString("posting_dept_code"));
                joiningCadreForm.setSltGenericPost(rs.getString("post_code"));

                joiningCadreForm.setRdPostClassification(rs.getString("post_class"));
                joiningCadreForm.setRdPostStatus(rs.getString("post_stat"));

                joiningCadreForm.setHpayid(rs.getInt("pay_id"));
                joiningCadreForm.setSltPayScale(rs.getString("pay_scale"));
                joiningCadreForm.setTxtGP(rs.getString("gp"));
                joiningCadreForm.setTxtBasic(rs.getString("pay"));
                joiningCadreForm.setTxtSP(rs.getString("s_pay"));
                joiningCadreForm.setTxtPP(rs.getString("p_pay"));
                joiningCadreForm.setTxtOP(rs.getString("oth_pay"));
                joiningCadreForm.setTxtDescOP(rs.getString("oth_desc"));
                joiningCadreForm.setTxtWEFDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("payrecordWEFDate")));
                joiningCadreForm.setSltWEFTime(rs.getString("payrecordWEFTime"));
                joiningCadreForm.setNote(rs.getString("note"));
                System.out.println("if visible:" + rs.getString("if_visible"));
                if (rs.getString("if_visible") != null && rs.getString("if_visible").equals("N")) {
                    joiningCadreForm.setChkNotSBPrint("Y");
                } else {
                    joiningCadreForm.setChkNotSBPrint("N");
                }
                joiningCadreForm.setSltPostGrp(rs.getString("post_grp_type"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return joiningCadreForm;
    }
}
