/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.performanceappraisal;

import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.model.parmast.SIParCadreBean;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 *
 * @author hp
 */
public class SIParCadreDAOImpl implements SIParCadreDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List getSIParDetailsEmpIdWise(String searchCriteria, String searchString) {

        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        SIParCadreBean parCadre = new SIParCadreBean();
        List empDataList = new ArrayList();

        try {
            con = this.dataSource.getConnection();
            if (searchCriteria != null && !searchCriteria.equals("")) {
                String column;
                switch (searchCriteria) {
                    case "hrmsId":
                        column = "emp_id";
                        break;
                    case "accountType":
                        column = "gpf_no";
                        break;
                    case "mobileNumber":
                        column = "mobile";
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid parameter type");
                }

//                String sql = "select dep_code,emp_id, gpf_no,acct_type,initials,f_name,m_name,l_name,dob,mobile,cur_cadre_code,gc.cadre_name,"
//                        + "cur_cadre_grade,post_grp_type,cur_off_code,off_en,cur_spc,spn from emp_mast em "
//                        + "inner join g_spc gs on em.cur_spc = gs.spc "
//                        + "inner join g_cadre gc on em.cur_cadre_code = gc.cadre_code "
//                        + "inner join g_office gf on gs.off_code = gf.off_code where "+column+" = ?";
                String sql = "select emp_id, gpf_no,acct_type,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMPNAME,dob,mobile,post_grp_type,"
                        + "cur_off_code,cur_spc,gs.spn,gof.off_en, gof.department_code, gof.dist_code,"
                        + "em.cur_cadre_code,gc.cadre_name from emp_mast em "
                        + "inner join g_office gof on em.cur_off_code = gof.off_code "
                        + "inner join g_spc gs on em.cur_spc = gs.spc "
                        + "left outer join g_cadre gc on em.cur_cadre_code = gc.cadre_code "
                        + "where "+column+" = ?  and gof.department_code = '14'";
                pst = con.prepareStatement(sql);
                pst.setString(1, searchString);
                rs = pst.executeQuery();
                while (rs.next()) {
                    
                    parCadre = new SIParCadreBean();
                    parCadre.setHrmsId(rs.getString("emp_id"));
                    parCadre.setEncHrmsId(CommonFunctions.encodedTxt(rs.getString("emp_id")));
                    parCadre.setGpfNumber(rs.getString("gpf_no"));
                    parCadre.setAccountType(rs.getString("acct_type"));
                    
                    parCadre.setEmpName(rs.getString("EMPNAME"));
//                    parCadre.setInitials(rs.getString("initials"));
//                    parCadre.setFirstName(rs.getString("f_name"));
//                    parCadre.setMiddleName(rs.getString("m_name"));
//                    parCadre.setLastName(rs.getString("l_name"));
                    parCadre.setDob(CommonFunctions.getFormattedOutputDate6(rs.getDate("DOB")));
                    parCadre.setMobileNumber(rs.getString("mobile"));
                    
                    parCadre.setPostGroupType(rs.getString("post_grp_type"));
                    parCadre.setCurOffCode(rs.getString("cur_off_code"));
                    parCadre.setCurSpc(rs.getString("cur_spc"));
                    parCadre.setSpn(rs.getString("spn"));
                    parCadre.setOfficeName(rs.getString("off_en"));
                    
                    parCadre.setDepCode(rs.getString("department_code"));
                    parCadre.setCurcadrecode(rs.getString("cur_cadre_code"));
                    parCadre.setCadreName(rs.getString("cadre_name"));
                    
                    //parCadre.setCurcadregrade(rs.getString("cur_cadre_grade"));
                    empDataList.add(parCadre);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return empDataList;
    }

    @Override
    public String updateSIParDetailsEmpIdWise(String loginHrmsId, SIParCadreBean siParCadre) {
        Connection con = null;
        PreparedStatement pst = null;
        String updSts = "";
        
        try {
            con = this.dataSource.getConnection();
            String forHrmsId = CommonFunctions.decodedTxt(siParCadre.getEncHrmsId());
            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat2 = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
            String createdDateTime = dateFormat2.format(cal.getTime());

            pst = con.prepareStatement("INSERT INTO equarter.sicadre_update_log(update_by, update_date, for_hrms_id, purpose)VALUES (?, ?, ?, ?)");
            pst.setString(1, loginHrmsId);
            pst.setString(2, createdDateTime);
            pst.setString(3, forHrmsId);
            pst.setString(4, "SI Cadre Update");
            pst.executeUpdate();
            DataBaseFunctions.closeSqlObjects(pst);
            
            String updateSql = "UPDATE hrmis2.emp_mast SET cur_cadre_code = ?,post_grp_type = ? WHERE emp_id = ?";
            pst = con.prepareStatement(updateSql);
            pst.setString(1, siParCadre.getCurcadrecode());
            pst.setString(2, siParCadre.getPostGroupType());
            pst.setString(3, forHrmsId);
            int insSts = pst.executeUpdate();
            if(insSts == 1){
                updSts = "Y";
            }else{
                updSts = "N";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return updSts;
    }

    @Override
    public SIParCadreBean getSIParDetailsByEmpId(String hrmsId) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        SIParCadreBean siParCadre = new SIParCadreBean();

        try {
            con = this.dataSource.getConnection();
//            String sql = "select dep_code,emp_id, gpf_no,acct_type,initials,f_name,m_name,l_name,dob,mobile,cur_cadre_code,gc.cadre_name,"
//                    + "cur_cadre_grade,post_grp_type,cur_off_code,off_en,cur_spc,spn from emp_mast em "
//                    + "inner join g_spc gs on em.cur_spc = gs.spc "
//                    + "inner join g_cadre gc on em.cur_cadre_code = gc.cadre_code "
//                    + "inner join g_office gf on gs.off_code = gf.off_code where emp_id =?";
//            
            String sql = "select emp_id, gpf_no,acct_type,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMPNAME,"
                    + "dob,mobile,gpf_no,post_grp_type,cur_off_code,cur_spc,gs.spn,gof.off_en, gof.department_code, gof.dist_code,"
                    + "em.cur_cadre_code,gc.cadre_name from emp_mast em "
                    + "inner join g_office gof on em.cur_off_code = gof.off_code "
                    + "inner join g_spc gs on em.cur_spc = gs.spc "
                    + "left outer join g_cadre gc on em.cur_cadre_code = gc.cadre_code "
                    + "where emp_id = ?  and gof.department_code = '14'";
            pst = con.prepareStatement(sql);
            pst.setString(1, hrmsId);
            rs = pst.executeQuery();
            if (rs.next()) {

                siParCadre.setHrmsId(rs.getString("emp_id"));
                siParCadre.setEncHrmsId(CommonFunctions.encodedTxt(rs.getString("emp_id")));
                siParCadre.setEmpName(rs.getString("EMPNAME"));
                siParCadre.setDob(CommonFunctions.getFormattedOutputDate6(rs.getDate("DOB")));
                siParCadre.setMobileNumber(rs.getString("mobile"));
                siParCadre.setGpfNumber(rs.getString("gpf_no"));
                //siParCadre.setAccountType(rs.getString("acct_type"));

                siParCadre.setPostGroupType(rs.getString("post_grp_type"));
                siParCadre.setCurOffCode(rs.getString("cur_off_code"));
                siParCadre.setCurSpc(rs.getString("cur_spc"));
                siParCadre.setSpn(rs.getString("spn"));
                siParCadre.setOfficeName(rs.getString("off_en"));

                siParCadre.setDepCode(rs.getString("department_code"));
                siParCadre.setCurcadrecode(rs.getString("cur_cadre_code"));
                siParCadre.setCadreName(rs.getString("cadre_name"));
                    
                siParCadre.setEncSearchString(CommonFunctions.encodedTxt(rs.getString("emp_id")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return siParCadre;
    }
}
