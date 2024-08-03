/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.EnrollmentToInsurance;

import hrms.SelectOption;
import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.dao.leaveAccount.ServiceBookLanguageDAO;
import hrms.dao.notification.NotificationDAO;
import hrms.model.EnrollmentToInsurance.Enrollmenttoinsurance;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 *
 * @author Surendra
 */
public class EnrollmenttoinsuranceDAOImpl implements EnrollmenttoinsuranceDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;
    
    protected ServiceBookLanguageDAO sbDAO;
    
     protected NotificationDAO notificationDao;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    public void setSbDAO(ServiceBookLanguageDAO sbDAO) {
        this.sbDAO = sbDAO;
    }
    
    public void setNotificationDao(NotificationDAO notificationDao) {
        this.notificationDao = notificationDao;
    }
    public ArrayList getSchemeList() {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList getSchemeList = new ArrayList();

        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT * FROM g_scheme");

            rs = pstmt.executeQuery();
            while (rs.next()) {
                SelectOption so = new SelectOption();
                so.setValue(rs.getString("scheme_id"));
                so.setLabel(rs.getString("scheme_name"));
                getSchemeList.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return getSchemeList;
    }

    @Override
    public void saveEnrollment(Enrollmenttoinsurance enrollmentForm, int notid) {
        Connection con = null;

        PreparedStatement pst = null;

        try {
            con = this.dataSource.getConnection();

            String sql = "INSERT INTO emp_enrollment (NOT_ID,NOT_TYPE,EMP_ID,SCHEME_ID,GISNO,WEF,SUB_AMT) VALUES(?,?,?,?,?,?,?)";
            pst = con.prepareStatement(sql);
            pst.setInt(1, notid);
            pst.setString(2, "ENROLLMENT");
            pst.setString(3, enrollmentForm.getEmpid());
            pst.setString(4, enrollmentForm.getSchemename());
            pst.setString(5, enrollmentForm.getInsaccountno());         
            if (enrollmentForm.getWitheffectdate() != null && !enrollmentForm.getWitheffectdate().equals("")) {
                pst.setTimestamp(6, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse((String) enrollmentForm.getWitheffectdate()).getTime()));
            } else {
                pst.setTimestamp(6, null);
            }        
            double amount = Double.parseDouble(enrollmentForm.getSubamount());
            pst.setDouble(7, amount);
            pst.executeUpdate();

            String sbLang = sbDAO.getEnrollmentSbDetails(enrollmentForm, "ENROLLMENT", notid);
            pst = con.prepareStatement("UPDATE emp_notification SET SB_DESCRIPTION =? WHERE NOT_ID=? ");
            pst.setString(1, sbLang);
            pst.setInt(2, notid);
            pst.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public List getEnrollmentList(String empid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List enrollmentlist = new ArrayList();
        Enrollmenttoinsurance dptBean = null;

        try {
            con = dataSource.getConnection();
            String sql = "select emp_notification.not_id,emp_notification.is_validated,scheme_name,wef,\n"
                    + "gisno,sub_amt,ordno,orddt from (select is_validated,emp_id,not_id,not_type,ordno,orddt from \n"
                    + "emp_notification where emp_id=? and not_type='ENROLLMENT')\n"
                    + "emp_notification\n"
                    + "inner join emp_enrollment on emp_notification.not_id=emp_enrollment.not_id\n"
                    + "left outer join g_scheme on emp_enrollment.scheme_id=g_scheme.scheme_id ;";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            rs = pst.executeQuery();
            while (rs.next()) {
                dptBean = new Enrollmenttoinsurance();
                dptBean.setNotId(rs.getString("not_id"));        
                dptBean.setIsValidated(rs.getString("is_validated"));
                dptBean.setSchemename(rs.getString("scheme_name"));
                dptBean.setInsaccountno(rs.getString("gisno"));
                dptBean.setWitheffectdate(CommonFunctions.getFormattedOutputDate1(rs.getDate("wef")));                             
                dptBean.setSubamount(rs.getString("sub_amt"));              
                enrollmentlist.add(dptBean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return enrollmentlist;
    }

    @Override
    public Enrollmenttoinsurance getempEnrollmentData(String empid, int notId) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        Enrollmenttoinsurance deptnBean = new Enrollmenttoinsurance();
        try {
            con = this.dataSource.getConnection();

            String sql = "select emp_notification.not_id,emp_notification.not_type,emp_enrollment.scheme_id,wef,\n"
                    + "gisno,scheme_name, sub_amt, ordno, orddt,organization_type,organization_type_posting,if_visible,emp_notification.dept_code,emp_notification.off_code,auth,spn,note\n"
                    + "from (select * from emp_notification \n"
                    + "where emp_id= ? and not_type='ENROLLMENT' and not_id= ?) emp_notification\n"
                    + "inner join emp_enrollment on emp_notification.not_id=emp_enrollment.not_id\n"
                    + "left outer join g_scheme on emp_enrollment.scheme_id=g_scheme.scheme_id \n"
                    + "left outer join g_spc on auth=g_spc.spc;";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            pst.setInt(2, notId);
            
            rs = pst.executeQuery();
            if (rs.next()) {
                deptnBean.setHidNotId(rs.getInt("not_id"));
                deptnBean.setHidNotifyingDeptCode(rs.getString("dept_code"));
                deptnBean.setHidNotifyingOffCode(rs.getString("off_code"));
                deptnBean.setHidNotiSpc(rs.getString("auth"));
                deptnBean.setNotifyingSpc(rs.getString("spn"));
                deptnBean.setTxtNotOrdNo(rs.getString("ordno"));
                deptnBean.setTxtNotOrdDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("orddt")));
                deptnBean.setNote(rs.getString("note"));             
                deptnBean.setRadpostingauthtype(rs.getString("organization_type"));
                deptnBean.setRadnotifyingauthtype(rs.getString("organization_type_posting"));
                deptnBean.setSchemename(rs.getString("scheme_id"));
                deptnBean.setSchemetype(rs.getString("scheme_name"));
                deptnBean.setInsaccountno(rs.getString("gisno"));
                deptnBean.setWitheffectdate(CommonFunctions.getFormattedOutputDate1(rs.getDate("wef")));
                deptnBean.setSubamount(rs.getString("sub_amt"));
                
                if(rs.getString("organization_type") != null && rs.getString("organization_type").equals("GOI")){
                    deptnBean.setRadpostingauthtype("GOI");
                    deptnBean.setHidNotifyingOthSpc(rs.getString("auth"));
                    deptnBean.setNotifyingSpc(getOtherSpn(rs.getString("auth")));
                }else{
                    deptnBean.setRadpostingauthtype("GOO");
                }
                
               /* if (rs.getString("organization_type_posting") != null && rs.getString("organization_type_posting").equals("GOI")) {
                    deptnBean.setHidNotifyingOthSpc(rs.getString("auth"));
                    deptnBean.setNotifyingSpc(getOtherSpn(rs.getString("auth")));
                }*/
                
                if(rs.getString("if_visible") != null && rs.getString("if_visible").equals("N")){
                    deptnBean.setChkNotSBPrint("Y");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return deptnBean;
    }

    @Override
    public void updateEnrollment(Enrollmenttoinsurance enrollmentForm) {

        Connection con = null;

        PreparedStatement pst = null;

        try {
            con = this.dataSource.getConnection();

           
                String sql = "UPDATE emp_enrollment SET SCHEME_ID=?,GISNO=?,WEF=?,SUB_AMT=? WHERE NOT_ID=? AND EMP_ID=?";
                pst = con.prepareStatement(sql);
        
                pst.setString(1, enrollmentForm.getSchemename());
                pst.setString(2, (enrollmentForm.getInsaccountno()));
                
                if (enrollmentForm.getWitheffectdate() != null && !enrollmentForm.getWitheffectdate().equals("")) {
                pst.setTimestamp(3, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse((String) enrollmentForm.getWitheffectdate()).getTime()));
                } else {
                    pst.setTimestamp(3, null);
                }                  
                double amount = Double.parseDouble(enrollmentForm.getSubamount());
                pst.setDouble(4, amount); 
                pst.setInt(5, enrollmentForm.getHidNotId());
                pst.setString(6, enrollmentForm.getEmpid());
               
                pst.executeUpdate();

                String sbLang = sbDAO.getEnrollmentSbDetails(enrollmentForm, "ENROLLMENT", enrollmentForm.getHidNotId());

                pst = con.prepareStatement("UPDATE emp_notification SET SB_DESCRIPTION =? WHERE NOT_ID=? ");
                pst.setString(1, sbLang);
                pst.setInt(2, enrollmentForm.getHidNotId());
                pst.execute();
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
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
