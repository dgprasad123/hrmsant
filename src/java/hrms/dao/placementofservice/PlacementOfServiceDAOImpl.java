/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.placementofservice;

import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.model.placementofservice.PlacementOfServiceBean;
import hrms.model.placementofservice.PlacementOfServiceForm;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 *
 * @author DurgaPrasad
 */
public class PlacementOfServiceDAOImpl implements PlacementOfServiceDAO  {
    
    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List getPlacementOfServiceList(String empid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List confirmationofserviceList = new ArrayList();
        PlacementOfServiceBean cBean = null;
        try {
            con = this.dataSource.getConnection();

            String sql = "select doe,not_id,not_type,ordno,orddt from emp_notification where emp_id=? and not_type='SERVICE_DISPOSAL'"
                    + " order by emp_notification.orddt desc, emp_notification.doe desc";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            rs = pst.executeQuery();
            while (rs.next()) {
                cBean = new PlacementOfServiceBean();
                cBean.setHnotid(rs.getString("not_id"));
                cBean.setNotType(rs.getString("not_type"));
                cBean.setDoe(CommonFunctions.getFormattedOutputDate1(rs.getDate("DOE")));
                cBean.setOrdno(rs.getString("ordno"));
                cBean.setOrdt(CommonFunctions.getFormattedOutputDate1(rs.getDate("orddt")));
                confirmationofserviceList.add(cBean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return confirmationofserviceList;
    }

    @Override
    public List getAllotDescList(String notType) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void savePlacementOfService(PlacementOfServiceForm placementOfServiceForm, int notId, String loginempid, String sblanguage) {
        
        Connection con = null;

        PreparedStatement pst = null;

        try {
            con = this.dataSource.getConnection();
            
            if (placementOfServiceForm.getChkNotSBPrint() != null && placementOfServiceForm.getChkNotSBPrint().equals("Y")) {
                pst = con.prepareStatement("UPDATE EMP_NOTIFICATION SET if_visible='N' WHERE NOT_ID=?");
                pst.setInt(1, notId);
                pst.executeUpdate();
            } else {
                pst = con.prepareStatement("UPDATE EMP_NOTIFICATION SET SB_DESCRIPTION=?,if_visible='Y' WHERE NOT_ID=?");
                pst.setString(1, sblanguage);
                pst.setInt(2, notId);
                pst.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void updatePlacementOfService(PlacementOfServiceForm placementOfServiceForm, String loginempid) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public PlacementOfServiceForm getPlacementOfServiceData(PlacementOfServiceForm placementOfServiceForm, int notId) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = dataSource.getConnection();

            String sql = "select"
                    + " emp_notification.if_visible,cad_id,joined_assuch,if_proforma,post_class,post_stat,emp_cadre.dept_code cadreDept,emp_cadre.cadre_code,grade,emp_cadre.lvl,description,emp_cadre.posting_dept_code,"
                    + " emp_cadre.post_code,emp_notification.not_id,ordno,orddt,emp_notification.dept_code notDeptCode,emp_notification.off_code notOffCode,auth,note,emp_cadre.wefd cadreWEFDate,emp_cadre.weft cadreWEFTime,"
                    + " pay_id,emp_pay_record.wef payrecordWEFDate,emp_pay_record.weft payrecordWEFTime,emp_pay_record.pay_scale,pay,s_pay,p_pay,oth_pay,emp_pay_record.gp,"
                    + " oth_desc,office1.dist_code notdistcode,spc1.gpc notgpc,spc1.spn authSpn from"
                    + " (select * from emp_notification where EMP_ID=? AND NOT_ID=?)emp_notification"
                    + " left outer join (select pay_id,not_id,wef,weft,pay_scale,pay,s_pay,p_pay,oth_pay,gp,oth_desc from emp_pay_record where EMP_ID=? AND NOT_ID=?)emp_pay_record"
                    + " on emp_notification.not_id=emp_pay_record.not_id"
                    + " left outer join emp_cadre on emp_notification.not_id=emp_cadre.not_id"
                    + " left outer join g_office office1 on emp_notification.off_code=office1.off_code"
                    + " left outer join g_spc spc1 on emp_notification.auth=spc1.spc";
            pst = con.prepareStatement(sql);
            pst.setString(1, placementOfServiceForm.getEmpid());
            pst.setInt(2, notId);
            pst.setString(3, placementOfServiceForm.getEmpid());
            pst.setInt(4, notId);
            rs = pst.executeQuery();
            if (rs.next()) {
                if (rs.getString("if_visible") != null && rs.getString("if_visible").equals("N")) {
                    placementOfServiceForm.setChkNotSBPrint("Y");
                } else {
                    placementOfServiceForm.setChkNotSBPrint("N");
                }

                placementOfServiceForm.setTxtNotOrdNo(rs.getString("ordno"));
                placementOfServiceForm.setHnotid(rs.getInt("not_id"));
                placementOfServiceForm.setTxtNotOrdDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("orddt")));
                placementOfServiceForm.setHidNotifyingDeptCode(rs.getString("notDeptCode"));
                placementOfServiceForm.setHidNotifyingDistCode(rs.getString("notdistcode"));
                placementOfServiceForm.setHidNotifyingOffCode(rs.getString("notOffCode"));
                placementOfServiceForm.setHidNotifyingGPC(rs.getString("notgpc"));

                placementOfServiceForm.setNotifyingSpc(rs.getString("auth"));
                placementOfServiceForm.setNotifyingPostName(rs.getString("authSpn"));

                placementOfServiceForm.setHcadId(rs.getString("cad_id"));
                placementOfServiceForm.setSltCadreDept(rs.getString("cadreDept"));
                placementOfServiceForm.setSltCadre(rs.getString("cadre_code"));

                placementOfServiceForm.setSltGrade(rs.getString("grade"));

                placementOfServiceForm.setSltCadreLevel(rs.getString("lvl"));
                placementOfServiceForm.setSltDescription(rs.getString("description"));
                placementOfServiceForm.setTxtCadreJoiningWEFDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("cadreWEFDate")));
                placementOfServiceForm.setSltCadreJoiningWEFTime(rs.getString("cadreWEFTime"));

                placementOfServiceForm.setSltPostingDept(rs.getString("posting_dept_code"));
                placementOfServiceForm.setSltGenericPost(rs.getString("post_code"));

                placementOfServiceForm.setRdPostClassification(rs.getString("post_class"));
                placementOfServiceForm.setRdPostStatus(rs.getString("post_stat"));

                placementOfServiceForm.setHpayid(rs.getInt("pay_id"));
                placementOfServiceForm.setSltPayScale(rs.getString("pay_scale"));
                placementOfServiceForm.setTxtGP(rs.getString("gp"));
                placementOfServiceForm.setTxtBasic(rs.getString("pay"));
                placementOfServiceForm.setTxtSP(rs.getString("s_pay"));
                placementOfServiceForm.setTxtPP(rs.getString("p_pay"));
                placementOfServiceForm.setTxtOP(rs.getString("oth_pay"));
                placementOfServiceForm.setTxtDescOP(rs.getString("oth_desc"));
                placementOfServiceForm.setTxtWEFDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("payrecordWEFDate")));
                placementOfServiceForm.setSltWEFTime(rs.getString("payrecordWEFTime"));
                placementOfServiceForm.setNote(rs.getString("note"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return placementOfServiceForm;
    }
    
}
