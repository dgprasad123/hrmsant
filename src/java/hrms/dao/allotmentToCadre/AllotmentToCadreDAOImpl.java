package hrms.dao.allotmentToCadre;

import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.dao.emppayrecord.EmpPayRecordDAO;
import hrms.dao.leaveAccount.ServiceBookLanguageDAO;
import hrms.dao.master.CadreDAO;
import hrms.dao.notification.NotificationDAO;
import hrms.model.allotmentToCadre.AllotmentToCadreForm;
import hrms.model.allotmentToCadre.AllotmentToCadreList;
import hrms.model.cadre.Cadre;
import hrms.model.emppayrecord.EmpPayRecordForm;
import hrms.model.notification.NotificationBean;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;

public class AllotmentToCadreDAOImpl implements AllotmentToCadreDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    protected EmpPayRecordDAO emppayrecordDAO;

    protected CadreDAO cadreDAO;
    protected ServiceBookLanguageDAO sbDAO;
    protected NotificationDAO notificationDao;

    public void setSbDAO(ServiceBookLanguageDAO sbDAO) {
        this.sbDAO = sbDAO;
    }

    public void setNotificationDao(NotificationDAO notificationDao) {
        this.notificationDao = notificationDao;
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

    @Override
    public List getAllotmentToCadreList(String empid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List allotmenToCadreList = new ArrayList();
        AllotmentToCadreList atcbean = null;
        try {
            con = this.dataSource.getConnection();

            String sql = "select emp_notification.doe,emp_notification.not_id,not_type,ordno,orddt from"
                    + " (select emp_id,doe,not_id,not_type,ordno,orddt from emp_notification where emp_id=? and not_type='ALLOT_CADRE')emp_notification";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            rs = pst.executeQuery();
            while (rs.next()) {
                atcbean = new AllotmentToCadreList();
                atcbean.setDoe(CommonFunctions.getFormattedOutputDate1(rs.getDate("doe")));
                atcbean.setNotId(rs.getString("not_id"));
                if (rs.getString("not_type") != null && !rs.getString("not_type").equals("")) {
                    if (rs.getString("not_type").equals("ALLOT_CADRE")) {
                        atcbean.setNotType("ALLOTMENT TO CADRE");
                    }
                }
                atcbean.setOrdNo(rs.getString("ordno"));
                atcbean.setOrdDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("orddt")));
                allotmenToCadreList.add(atcbean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return allotmenToCadreList;
    }

    @Override
    public void saveAllotmentToCadre(AllotmentToCadreForm allotmentToCadreForm, int notId) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        EmpPayRecordForm epayrecordform = new EmpPayRecordForm();
        Cadre cadreForm = new Cadre();
        try {
            con = this.dataSource.getConnection();

            epayrecordform.setEmpid(allotmentToCadreForm.getEmpid());
            epayrecordform.setNot_id(notId);
            epayrecordform.setNot_type("ALLOT_CADRE");
            epayrecordform.setPayscale(allotmentToCadreForm.getSltPayScale());
            epayrecordform.setBasic(allotmentToCadreForm.getTxtBasic());
            epayrecordform.setGp(allotmentToCadreForm.getTxtGP());
            epayrecordform.setS_pay(allotmentToCadreForm.getTxtSP());
            epayrecordform.setP_pay(allotmentToCadreForm.getTxtPP());
            epayrecordform.setOth_pay(allotmentToCadreForm.getTxtOP());
            epayrecordform.setOth_desc(allotmentToCadreForm.getTxtDescOP());
            epayrecordform.setWefDt(allotmentToCadreForm.getTxtWEFDt());
            epayrecordform.setWefTime(allotmentToCadreForm.getSltWEFTime());
            emppayrecordDAO.saveEmpPayRecordData(epayrecordform);

            cadreForm.setEmpId(allotmentToCadreForm.getEmpid());
            cadreForm.setNotId(notId);
            cadreForm.setNotType("ALLOT_CADRE");
            cadreForm.setCadreDept(allotmentToCadreForm.getSltCadreDept());
            cadreForm.setCadreName(allotmentToCadreForm.getSltCadre());
            cadreForm.setGrade(allotmentToCadreForm.getSltGrade());
            cadreForm.setCadreLvl(allotmentToCadreForm.getSltCadreLevel());
            cadreForm.setDescription(allotmentToCadreForm.getSltDescription());
            cadreForm.setAllotmentYear(allotmentToCadreForm.getTxtAllotmentYear());
            cadreForm.setCadreId(allotmentToCadreForm.getTxtCadreId());
            cadreForm.setPostingDept(allotmentToCadreForm.getSltPostingDept());
            cadreForm.setPostCode(allotmentToCadreForm.getSltGenericPost());
            cadreForm.setPostClassification(allotmentToCadreForm.getRdPostClassification());
            cadreForm.setPostStatus(allotmentToCadreForm.getRdPostStatus());
            cadreForm.setCadreJoiningWEFDt(allotmentToCadreForm.getTxtCadreJoiningWEFDt());
            cadreForm.setCadreJoiningWEFTime(allotmentToCadreForm.getSltCadreJoiningWEFTime());
            cadreDAO.saveCadreData(cadreForm);
            //save Language
            
            String sbLang=sbDAO.getAllotmentToCadreLangDetails(allotmentToCadreForm, notId, "ALLOT_CADRE");
            notificationDao.saveServiceBookLanguage(sbLang, notId, "ALLOT_CADRE");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void updateAllotmentToCadre(AllotmentToCadreForm allotmentToCadreForm) {

        Connection con = null;

        PreparedStatement pst = null;

        EmpPayRecordForm epayrecordform = new EmpPayRecordForm();
        //NotificationBean nb = new NotificationBean();
        Cadre cadreForm = new Cadre();
        try {
            con = this.dataSource.getConnection();

            epayrecordform.setPayid(allotmentToCadreForm.getHpayid());
            epayrecordform.setEmpid(allotmentToCadreForm.getEmpid());
            epayrecordform.setPayscale(allotmentToCadreForm.getSltPayScale());
            epayrecordform.setBasic(allotmentToCadreForm.getTxtBasic());
            epayrecordform.setGp(allotmentToCadreForm.getTxtGP());
            epayrecordform.setS_pay(allotmentToCadreForm.getTxtSP());
            epayrecordform.setP_pay(allotmentToCadreForm.getTxtPP());
            epayrecordform.setOth_pay(allotmentToCadreForm.getTxtOP());
            epayrecordform.setOth_desc(allotmentToCadreForm.getTxtDescOP());
            epayrecordform.setWefDt(allotmentToCadreForm.getTxtWEFDt());
            epayrecordform.setWefTime(allotmentToCadreForm.getSltWEFTime());
            emppayrecordDAO.updateEmpPayRecordData(epayrecordform);

            cadreForm.setCadreCode(allotmentToCadreForm.gethCadId());
            cadreForm.setEmpId(allotmentToCadreForm.getEmpid());
            cadreForm.setCadreDept(allotmentToCadreForm.getSltCadreDept());
            cadreForm.setCadreName(allotmentToCadreForm.getSltCadre());
            cadreForm.setGrade(allotmentToCadreForm.getSltGrade());
            cadreForm.setCadreLvl(allotmentToCadreForm.getSltCadreLevel());
            cadreForm.setDescription(allotmentToCadreForm.getSltDescription());
            cadreForm.setAllotmentYear(allotmentToCadreForm.getTxtAllotmentYear());
            cadreForm.setCadreId(allotmentToCadreForm.getTxtCadreId());
            cadreForm.setPostingDept(allotmentToCadreForm.getSltPostingDept());
            cadreForm.setPostCode(allotmentToCadreForm.getSltGenericPost());
            cadreForm.setPostClassification(allotmentToCadreForm.getRdPostClassification());
            cadreForm.setPostStatus(allotmentToCadreForm.getRdPostStatus());
            cadreForm.setCadreJoiningWEFDt(allotmentToCadreForm.getTxtCadreJoiningWEFDt());
            cadreForm.setCadreJoiningWEFTime(allotmentToCadreForm.getSltCadreJoiningWEFTime());
            cadreDAO.updateCadreData(cadreForm);
            //Save language
            
            String sbLang=sbDAO.getAllotmentToCadreLangDetails(allotmentToCadreForm, allotmentToCadreForm.getHnotid(), "ALLOT_CADRE");
            notificationDao.saveServiceBookLanguage(sbLang, allotmentToCadreForm.getHnotid(), "ALLOT_CADRE");
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public AllotmentToCadreForm getEmpAllotmentToCadreData(AllotmentToCadreForm allotmentToCadreForm, int notId) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = this.dataSource.getConnection();

            String sql = "select"
                    + " cad_id,joined_assuch,if_proforma,post_class,post_stat,emp_cadre.dept_code cadreDept,emp_cadre.cadre_code,grade,lvl,description,emp_cadre.posting_dept_code,emp_cadre.post_code,"
                    + " emp_notification.not_id,emp_notification.if_visible,ordno,orddt,emp_notification.dept_code notDeptCode,emp_notification.off_code notOffCode,"
                    + " auth,note,emp_cadre.wefd cadreWEFDate,emp_cadre.weft cadreWEFTime,pay_id,emp_pay_record.wef payrecordWEFDate,emp_pay_record.weft payrecordWEFTime,emp_pay_record.pay_scale,pay,s_pay,p_pay,"
                    + " oth_pay,emp_pay_record.gp,oth_desc from"
                    + " (select not_id,ordno,orddt,dept_code,off_code,auth,note,if_visible from emp_notification where EMP_ID=? AND NOT_ID=?)emp_notification"
                    + " left outer join"
                    + " (select pay_id,not_id,wef,weft,pay_scale,pay,s_pay,p_pay,oth_pay,gp,oth_desc from emp_pay_record where EMP_ID=? AND NOT_ID=?)emp_pay_record"
                    + " on emp_notification.not_id=emp_pay_record.not_id"
                    + " left outer join emp_cadre on emp_notification.not_id=emp_cadre.not_id";
            pst = con.prepareStatement(sql);
            pst.setString(1, allotmentToCadreForm.getEmpid());
            pst.setInt(2, notId);
            pst.setString(3, allotmentToCadreForm.getEmpid());
            pst.setInt(4, notId);
            rs = pst.executeQuery();
            if (rs.next()) {
                //repatriationForm.setPromotionId(rs.getString("pro_id"));
                allotmentToCadreForm.setTxtNotOrdNo(rs.getString("ordno"));
                allotmentToCadreForm.setHnotid(rs.getInt("not_id"));
                allotmentToCadreForm.setTxtNotOrdDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("orddt")));
                allotmentToCadreForm.setHidNotifyingDeptCode(rs.getString("notDeptCode"));
                allotmentToCadreForm.setHidNotifyingOffCode(rs.getString("notOffCode"));
                allotmentToCadreForm.setNotifyingSpc(rs.getString("auth"));
                allotmentToCadreForm.setNotifyingPostName(CommonFunctions.getSPN(con, rs.getString("auth")));

                allotmentToCadreForm.sethCadId(rs.getString("cad_id"));
                allotmentToCadreForm.setSltCadreDept(rs.getString("cadreDept"));
                allotmentToCadreForm.setSltCadre(rs.getString("cadre_code"));
                allotmentToCadreForm.setSltGrade(rs.getString("grade"));
                allotmentToCadreForm.setSltCadreLevel(rs.getString("lvl"));
                allotmentToCadreForm.setSltDescription(rs.getString("description"));
                allotmentToCadreForm.setTxtCadreJoiningWEFDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("cadreWEFDate")));
                allotmentToCadreForm.setSltCadreJoiningWEFTime(rs.getString("cadreWEFTime"));

                allotmentToCadreForm.setSltPostingDept(rs.getString("posting_dept_code"));
                allotmentToCadreForm.setSltGenericPost(rs.getString("post_code"));

                allotmentToCadreForm.setRdPostClassification(rs.getString("post_class"));
                allotmentToCadreForm.setRdPostStatus(rs.getString("post_stat"));

                allotmentToCadreForm.setHpayid(rs.getInt("pay_id"));
                allotmentToCadreForm.setSltPayScale(rs.getString("pay_scale"));
                allotmentToCadreForm.setTxtGP(rs.getString("gp"));
                allotmentToCadreForm.setTxtBasic(rs.getString("pay"));
                allotmentToCadreForm.setTxtSP(rs.getString("s_pay"));
                allotmentToCadreForm.setTxtPP(rs.getString("p_pay"));
                allotmentToCadreForm.setTxtOP(rs.getString("oth_pay"));
                allotmentToCadreForm.setTxtDescOP(rs.getString("oth_desc"));
                allotmentToCadreForm.setTxtWEFDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("payrecordWEFDate")));
                allotmentToCadreForm.setSltWEFTime(rs.getString("payrecordWEFTime"));
                allotmentToCadreForm.setNote(rs.getString("note"));
                if(rs.getString("if_visible")!=null && rs.getString("if_visible").equals("N")){
                    allotmentToCadreForm.setChkNotSBPrint("Y");
                }else{
                    allotmentToCadreForm.setChkNotSBPrint("N");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return allotmentToCadreForm;
    }
}
