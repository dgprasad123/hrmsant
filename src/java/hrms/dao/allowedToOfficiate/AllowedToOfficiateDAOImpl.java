/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.allowedToOfficiate;

import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.dao.emppayrecord.EmpPayRecordDAO;
import hrms.dao.leaveAccount.ServiceBookLanguageDAO;
import hrms.dao.master.CadreDAO;
import hrms.model.absorption.AbsorptionModel;
import hrms.model.allowedToOfficiate.AllowedToOfficiateModel;
import hrms.model.cadre.Cadre;
import hrms.model.emppayrecord.EmpPayRecordForm;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 *
 * @author Madhusmita
 */
public class AllowedToOfficiateDAOImpl implements AllowedToOfficiateDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;
    protected EmpPayRecordDAO emppayrecordDAO;

    protected CadreDAO cadreDAO;
    protected ServiceBookLanguageDAO sbDAO;

    public void setCadreDAO(CadreDAO cadreDAO) {
        this.cadreDAO = cadreDAO;
    }

    public void setSbDAO(ServiceBookLanguageDAO sbDAO) {
        this.sbDAO = sbDAO;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List findallOfficiation(String empid) {
        List list = null;
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        AllowedToOfficiateModel officiationBean = null;
        list = new ArrayList();
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("select emp_notification.emp_id,doe,cad_id,emp_cadre.cadre_code,grade,\n"
                    + "lvl,description,emp_cadre.posting_dept_code,emp_cadre.post_code, emp_cadre.wefd cadreWEFDate,emp_cadre.weft cadreWEFTime,cadreid,allot_year,\n"
                    + "emp_notification.not_id,ordno,orddt,emp_notification.dept_code notDeptCode,emp_notification.off_code notOffCode,auth,note from\n"
                    + "(select emp_id,not_id,doe,ordno,orddt,dept_code,off_code,auth,note from emp_notification where EMP_ID=? AND NOT_TYPE='OFFICIATE' ORDER BY EMP_ID)emp_notification\n"
                    + "left outer join emp_cadre on emp_notification.not_id=emp_cadre.not_id");
            pst.setString(1, empid);
            rs = pst.executeQuery();
            while (rs.next()) {
                officiationBean = new AllowedToOfficiateModel();
                //officiationBean.setHpayid(rs.getInt("pay_id"));
                officiationBean.sethCadId(rs.getString("cad_id"));
                officiationBean.setNotId(rs.getInt("NOT_ID"));
                officiationBean.setNotType("OFFICIATE");
                officiationBean.setDoe(CommonFunctions.getFormattedOutputDate1(rs.getDate("doe")));
                officiationBean.setOrdno(rs.getString("ORDNO"));
                officiationBean.setOrdDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("ORDDT")));
                list.add(officiationBean);
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
    public int insertOfficiationData(AllowedToOfficiateModel officiation, int notId) {
        int n = 0;
        PreparedStatement pst = null;
        Connection con = null;
        EmpPayRecordForm epayrecordform = null;
        Cadre cadreForm = new Cadre();
        try {
            con = this.dataSource.getConnection();
            cadreForm.setEmpId(officiation.getEmpid());
            cadreForm.setNotId(notId);
            cadreForm.setNotType("OFFICIATE");
            cadreForm.setCadreDept(officiation.getSltCadreDept());
            cadreForm.setCadreName(officiation.getSltCadre());
            cadreForm.setGrade(officiation.getSltGrade());
            cadreForm.setCadreLvl(officiation.getSltCadreLevel());
            cadreForm.setDescription(officiation.getSltDescription());
            cadreForm.setAllotmentYear(officiation.getTxtAllotmentYear());
            cadreForm.setCadreId(officiation.getTxtCadreId());
            cadreForm.setPostingDept(officiation.getSltPostingDept());
            cadreForm.setPostCode(officiation.getSltGenericPost());
            cadreForm.setPostClassification(officiation.getRdPostClassification());
            cadreForm.setPostStatus(officiation.getRdPostStatus());
            cadreForm.setCadreJoiningWEFDt(officiation.getTxtCadreJoiningWEFDt());
            cadreForm.setCadreJoiningWEFTime(officiation.getSltCadreJoiningWEFTime());
            //cadreForm.setJoinedAsSuch(officiation.getChkJoinedAsSuch());
            cadreDAO.saveCadreData(cadreForm);
            String sblang = sbDAO.getAllowedToOfficiateDetails(officiation, notId, "OFFICIATE");
            pst = con.prepareStatement("UPDATE EMP_NOTIFICATION SET SB_DESCRIPTION=? WHERE NOT_ID=? AND "
                    + "NOT_TYPE='OFFICIATE' ");
            pst.setString(1, sblang);
            System.out.println("Sb_description:" + sblang);
            pst.setInt(2, cadreForm.getNotId());
            System.out.println("Not-ID:" + cadreForm.getNotId());
            pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return n;
    }

    @Override
    public int updateOfficiationData(AllowedToOfficiateModel officiation, int notId) {
        int n = 0;
        PreparedStatement pst = null;
        Connection con = null;
        EmpPayRecordForm epayrecordform = null;
        Cadre cadreForm = new Cadre();
        try {
            con = this.dataSource.getConnection();
            //int notid1=officiation.getNotId();
            System.out.println("Not Update ID:" + notId);
            cadreForm.setCadreCode(officiation.gethCadId());
            cadreForm.setEmpId(officiation.getEmpid());
            cadreForm.setCadreDept(officiation.getSltCadreDept());
            cadreForm.setCadreName(officiation.getSltCadre());
            cadreForm.setGrade(officiation.getSltGrade());
            cadreForm.setCadreLvl(officiation.getSltCadreLevel());
            cadreForm.setDescription(officiation.getSltDescription());
            cadreForm.setAllotmentYear(officiation.getTxtAllotmentYear());

            cadreForm.setCadreId(officiation.getTxtCadreId());
            cadreForm.setPostingDept(officiation.getSltPostingDept());
            cadreForm.setPostCode(officiation.getSltGenericPost());
            cadreForm.setPostClassification(officiation.getRdPostClassification());
            cadreForm.setPostStatus(officiation.getRdPostStatus());
            cadreForm.setCadreJoiningWEFDt(officiation.getTxtCadreJoiningWEFDt());
            cadreForm.setCadreJoiningWEFTime(officiation.getSltCadreJoiningWEFTime());
            cadreForm.setJoinedAsSuch(officiation.getChkJoinedAsSuch());
            cadreDAO.updateCadreData(cadreForm);
            String sblang = sbDAO.getAllowedToOfficiateDetails(officiation, notId, "OFFICIATE");
            pst = con.prepareStatement("UPDATE EMP_NOTIFICATION SET SB_DESCRIPTION=? WHERE NOT_ID=? AND "
                    + "NOT_TYPE='OFFICIATE' ");
            pst.setString(1, sblang);
            System.out.println("Sb_description:" + sblang);
            pst.setInt(2, notId);
            System.out.println("Not-ID:" + notId);
            pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return n;
    }

    @Override
    public AllowedToOfficiateModel editOfficiationData(int notId, String empid) {
        ResultSet rs = null;
        PreparedStatement pst = null;
        AllowedToOfficiateModel officiation = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("select emp_notification.emp_id,emp_notification.not_id,emp_notification.if_visible,doe,cad_id,emp_cadre.dept_code cadre_dept,emp_cadre.cadre_code,grade,\n"
                    + " lvl,description,emp_cadre.posting_dept_code,emp_cadre.post_code, emp_cadre.wefd cadreWEFDate,emp_cadre.weft cadreWEFTime,cadreid,allot_year,\n"
                    + " emp_notification.not_id,ordno,orddt,emp_notification.dept_code notDeptCode,emp_notification.off_code notOffCode,auth,note,\n"
                    + " emp_cadre.post_class,emp_cadre.post_stat from(select * from emp_notification where EMP_ID=? and not_id=? AND NOT_TYPE='OFFICIATE' ORDER BY EMP_ID)emp_notification\n"
                    + "left outer join emp_cadre on emp_notification.not_id=emp_cadre.not_id");
            pst.setString(1, empid);
            pst.setInt(2, notId);
            rs = pst.executeQuery();
            if (rs.next()) {
                officiation = new AllowedToOfficiateModel();
                officiation.setNotType("OFFICIATE");
                officiation.setEmpid(rs.getString("EMP_ID"));
                officiation.setOrdno(rs.getString("ORDNO"));
                officiation.setOrdDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("ORDDT")));
                officiation.setHidNotifyingDeptCode(rs.getString("notDeptCode"));
                officiation.setHidNotifyingOffCode(rs.getString("notOffCode"));
                officiation.setHidTempNotifyingOffCode(rs.getString("notOffCode"));
                officiation.setNotifyingSpc(rs.getString("auth"));
                officiation.setHidTempNotifyingPost(rs.getString("auth"));
                officiation.setNotifyingPostName(CommonFunctions.getSPN(con, rs.getString("auth")));
                officiation.setHnotid(rs.getInt("not_id"));
                officiation.sethCadId(rs.getString("cad_id"));
                officiation.setSltCadreDept(rs.getString("cadre_dept"));
                officiation.setSltCadre(rs.getString("cadre_code"));
                officiation.setHidTempCadre(rs.getString("cadre_code"));
                officiation.setSltGrade(rs.getString("grade"));
                officiation.setHidTempGrade(rs.getString("grade"));
                officiation.setSltCadreLevel(rs.getString("lvl"));
                officiation.setSltDescription(rs.getString("description"));
                officiation.setHidTempDescription(rs.getString("description"));
                officiation.setTxtAllotmentYear(rs.getString("allot_year"));
                officiation.setTxtCadreId(rs.getString("cadreid"));
                officiation.setTxtCadreJoiningWEFDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("cadreWEFDate")));
                officiation.setSltCadreJoiningWEFTime(rs.getString("cadreWEFTime"));
                officiation.setSltPostingDept(rs.getString("posting_dept_code"));
                officiation.setSltGenericPost(rs.getString("post_code"));
                officiation.setHidTempGenericPost(rs.getString("post_code"));
                officiation.setRdPostClassification(rs.getString("post_class"));
                officiation.setRdPostStatus(rs.getString("post_stat"));
                officiation.setNote(rs.getString("note"));
                if (rs.getString("if_visible") != null && rs.getString("if_visible").equals("N")) {
                    officiation.setChkNotSBPrint("Y");
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return officiation;
    }

    @Override
    public int deleteOfficiationData(AllowedToOfficiateModel officiation
    ) {
        int n = 0;
        //EmpPayRecordForm epayrecordform = new EmpPayRecordForm();
        Cadre cadreForm = new Cadre();
        try {
            //cadreForm.setEmpId(officiation.getEmpid());
            //epayrecordform.setPayid(absorptionForm.getHpayid());
            //emppayrecordDAO.deleteEmpPayRecordData(epayrecordform);
            cadreForm.setCadreId(officiation.gethCadId());
            cadreDAO.deleteCadreData(cadreForm);
            System.out.println("DElete Cadre");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return n;
    }
}
