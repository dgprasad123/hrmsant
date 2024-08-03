package hrms.dao.additionalCharge;

import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.model.additionalCharge.AdditionalCharge;
import hrms.model.additionalCharge.AdditionalChargeList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;

public class AdditionalChargeDAOImpl implements AdditionalChargeDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public int insertAdditionalChargeData(AdditionalCharge addchage) {
        int n = 0;
        PreparedStatement pst = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            /*String mcode = CommonFunctions.getMaxCode("EMP_NOTIFICATION", "NOT_ID", con);
             pst = con.prepareStatement("INSERT INTO EMP_NOTIFICATION(NOT_ID,NOT_TYPE,EMP_ID,DOE,TOE,IF_ASSUMED,ORDNO,ORDDT,DEPT_CODE,OFF_CODE,AUTH,NOTE,IF_VISIBLE,ENT_DEPT,ENT_OFF,ENT_AUTH,ACS,ASCS) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
             pst.setString(1, mcode);
             pst.setString(2, "ADDITIONAL_CHARGE");
             pst.setString(3, addchage.getEmpid());
             pst.setString(4, addchage.getDoe());
             pst.setString(5, "");
             pst.setString(6, "");
             pst.setString(7, addchage.getTxtNotOrdNo());
             pst.setString(8, addchage.getTxtNotOrdDt());
             pst.setString(9, addchage.getSltDept());
             pst.setString(10, addchage.getSltOffice());
             pst.setString(11, addchage.getSltAuth());
             pst.setString(12, addchage.getNote());
             pst.setString(13, addchage.getIfVisible());
             pst.setString(14, addchage.getEntDept());
             pst.setString(15, addchage.getEntOffice());
             pst.setString(16, addchage.getEntAuth());
             pst.setString(17, addchage.getCadreStatus());
             pst.setString(18, addchage.getSubStatus());
             n = pst.executeUpdate();*/

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return n;
    }

    public int updateAdditionalChargeData(AdditionalCharge addchage) {
        int n = 0;
        PreparedStatement pst = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            /*pst = con.prepareStatement("UPDATE  EMP_NOTIFICATION SET NOT_TYPE=?,EMP_ID=?,DOE=?,TOE=?,IF_ASSUMED=?,ORDNO=?,ORDDT=?,DEPT_CODE=?,OFF_CODE=?,AUTH,NOTE=?,IF_VISIBLE=?,ENT_DEPT=?,ENT_OFF=?,ENT_AUTH=?,ACS=?,ASCS=? WHERE NOT_ID='" + addchage.getNotId() + "'");
             pst.setString(1, "ADDITIONAL_CHARGE");
             pst.setString(2, addchage.getEmpid());
             pst.setString(3, addchage.getDoe());
             pst.setString(4, addchage.getToe());
             pst.setString(5, addchage.getIfAssumed());
             pst.setString(6, addchage.getOrdno());
             pst.setString(7, addchage.getOrdDate());
             pst.setString(8, addchage.getSltDept());
             pst.setString(9, addchage.getSltOffice());
             pst.setString(10, addchage.getSltAuth());
             pst.setString(11, addchage.getNote());
             pst.setString(12, addchage.getIfVisible());
             pst.setString(13, addchage.getEntDept());
             pst.setString(14, addchage.getEntOffice());
             pst.setString(15, addchage.getEntAuth());
             pst.setString(16, addchage.getCadreStatus());
             pst.setString(17, addchage.getSubStatus());
             pst.setString(18, addchage.getNotId());
             n = pst.executeUpdate();*/

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return n;
    }

    public void deleteAdditionalCharge(String empid, int notId, String spc) {

        Connection con = null;

        PreparedStatement pst = null;
        PreparedStatement pst1 = null;
        ResultSet rs = null;
        try {
            con = this.dataSource.getConnection();

            /* pst = con.prepareStatement("update emp_notification set if_visible='N' where emp_id=? and not_type='ADDITIONAL_CHARGE' and not_id=?");
             pst.setString(1, empid);
             pst.setInt(2, notId);
             pst.executeUpdate();*/
            pst = con.prepareStatement("update emp_join set if_ad_charge='N'  where emp_id=? and not_type='ADDITIONAL_CHARGE' and not_id=? and spc=?");
            pst.setString(1, empid);
            pst.setInt(2, notId);
            pst.setString(3, spc);
            pst.executeUpdate();

            pst1 = con.prepareStatement("Select * from g_privilege_map where spc=? and hrmsid=?");
            pst1.setString(1, spc);
            pst1.setString(2, empid);
            rs = pst1.executeQuery();
            if (rs.next()) {
                pst = con.prepareStatement("Delete from g_privilege_map where spc=? and hrmsid=?");
                pst.setString(1, spc);
                pst.setString(2, empid);
                pst.executeUpdate();

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst, pst1);
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    public AdditionalCharge editAdditionalCharge(String empid, int notId) {
        AdditionalCharge addchage = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("SELECT EMP_NOTIFICATION.NOT_TYPE,EMP_NOTIFICATION.EMP_ID,EMP_NOTIFICATION.DOE,ORDNO,ORDDT,DEPT_CODE,OFF_CODE,AUTH,EMP_NOTIFICATION.NOTE,"
                    + " auth_dept,auth_off,spc,join_id,join_date,join_time,EMP_NOTIFICATION.if_visible FROM EMP_NOTIFICATION"
                    + " LEFT OUTER JOIN EMP_JOIN ON EMP_NOTIFICATION.NOT_ID=EMP_JOIN.NOT_ID WHERE EMP_NOTIFICATION.EMP_ID=? AND"
                    + " EMP_NOTIFICATION.NOT_ID=?");
            pst.setString(1, empid);
            pst.setInt(2, notId);
            rs = pst.executeQuery();
            if (rs.next()) {
                addchage = new AdditionalCharge();
                addchage.setNotId(notId);
                addchage.setNotType("ADDITIONAL_CHARGE");
                addchage.setEmpid(rs.getString("EMP_ID"));
                addchage.setDoe(rs.getString("DOE"));
                addchage.setTxtNotOrdNo(rs.getString("ORDNO"));
                addchage.setTxtNotOrdDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("ORDDT")));
                addchage.setHidAuthDeptCode(rs.getString("DEPT_CODE"));
                addchage.setHidAuthOffCode(rs.getString("OFF_CODE"));
                addchage.setHidTempAuthOffCode(rs.getString("OFF_CODE"));
                addchage.setAuthSpc(rs.getString("AUTH"));
                addchage.setHidTempAuthSpc(rs.getString("AUTH"));
                addchage.setAuthPostName(CommonFunctions.getSPN(con, rs.getString("AUTH")));
                addchage.setHidPostedDeptCode(rs.getString("auth_dept"));
                addchage.setHidPostedOffCode(rs.getString("auth_off"));
                addchage.setHidTempPostedOffCode(rs.getString("auth_off"));
                addchage.setPostedspc(rs.getString("spc"));
                addchage.setHidTempPostedspc(rs.getString("spc"));
                addchage.setPostedPostName(CommonFunctions.getSPN(con, rs.getString("spc")));
                addchage.setJoinid(rs.getString("join_id"));
                addchage.setTxtJoinDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("join_date")));
                addchage.setSltJoinTime(rs.getString("join_time"));
                addchage.setNote(rs.getString("NOTE"));
                if (rs.getString("if_visible") != null && rs.getString("if_visible").equals("N")) {
                    addchage.setChkNotSBPrint("Y");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return addchage;
    }

    public List findAllAdditionalCharge(String empId) {
        List list = null;
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        AdditionalChargeList addList = null;
        list = new ArrayList();
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("select en.is_validated,en.DOE,en.ORDNO,en.ORDDT,en.not_id,join_id,if_ad_charge,spc from\n"
                    + "(SELECT * FROM EMP_NOTIFICATION WHERE EMP_ID=? AND NOT_TYPE='ADDITIONAL_CHARGE')en\n"
                    + "inner join \n"
                    + "(select * from emp_join where emp_id=? and not_type='ADDITIONAL_CHARGE')ej on\n"
                    + "en.not_id=ej.not_id");

            pst.setString(1, empId);
            pst.setString(2, empId);
            rs = pst.executeQuery();
            while (rs.next()) {
                addList = new AdditionalChargeList();
                addList.setNotId(rs.getString("NOT_ID"));
                addList.setNotType("ADDITIONAL_CHARGE");
                addList.setDoe(CommonFunctions.getFormattedOutputDate1(rs.getDate("DOE")));
                addList.setOrdno(rs.getString("ORDNO"));
                addList.setAddlspc(rs.getString("spc"));
                addList.setOrdDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("ORDDT")));
                addList.setIsValidated(rs.getString("is_validated"));
                if (rs.getString("if_ad_charge") != null && !rs.getString("if_ad_charge").equals("") && rs.getString("if_ad_charge").equals("Y")) {
                    addList.setIfadcharge("YES");
                } else if (rs.getString("if_ad_charge").equals("N") || rs.getString("if_ad_charge").equals("")) {
                    addList.setIfadcharge("NO");
                }
                list.add(addList);
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
    public void updateDDOInOfficeDetails(String offCode, String postCode, String empid) {

        Connection con = null;
        PreparedStatement pst = null;

        try {
            con = this.dataSource.getConnection();

            pst = con.prepareStatement("UPDATE G_OFFICE SET DDO_HRMSID=?,DDO_POST=? WHERE OFF_CODE=?");
            pst.setString(1, empid);
            pst.setString(2, postCode);
            pst.setString(3, offCode);
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

}
