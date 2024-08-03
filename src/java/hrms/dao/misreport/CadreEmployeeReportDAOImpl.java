/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.misreport;

import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.model.employee.Employee;
import hrms.model.employee.EmployeeERCommentForm;
import hrms.model.misreport.IncumbancyChart;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import javax.annotation.Resource;
import javax.sql.DataSource;
//import static org.apache.http.client.utils.DateUtils.formatDate;

/**
 *
 * @author Manas Jena
 */
public class CadreEmployeeReportDAOImpl implements CadreEmployeeReportDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public ArrayList getEmployeeList(String cadreCode, String allYear, String sortby) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList employees = new ArrayList();
        try {
            con = this.dataSource.getConnection();
            String sql = "SELECT emp_id,initials,f_name,m_name,l_name,cadreid,rec_source,cur_spc,allotment_year,SPN,cur_salary,cur_basic_salary,G_OFFICE.OFF_ADDRESS,DOS, DOB,JOINDATE_OF_GOO FROM emp_mast "
                    + "left outer join G_SPC ON emp_mast.cur_spc = G_SPC.SPC "
                    + "left outer join G_OFFICE ON emp_mast.cur_off_code = G_OFFICE.OFF_CODE "
                    + "WHERE emp_mast.CUR_CADRE_CODE=? and (dep_code != '09' and dep_code != '08' and dep_code != '10')";
            if (allYear != null && !allYear.equals("")) {
                sql = sql + " AND allotment_year='" + allYear + "'";

            }            
            if (sortby.equalsIgnoreCase("DOS")) {
                sql = sql + " order by DOS";
            } else {
                sql = sql + " order by allotment_year,cadre_rank_slno";
            }
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, cadreCode);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Employee employee = new Employee();
                employee.setEmpid(rs.getString("emp_id"));
                employee.setIntitals(rs.getString("initials"));
                employee.setFname(rs.getString("f_name"));
                employee.setMname(rs.getString("m_name"));
                employee.setLname(rs.getString("l_name"));
                employee.setCadreId(rs.getString("cadreid"));
                employee.setRecsource(rs.getString("rec_source"));
                employee.setCardeallotmentyear(rs.getString("allotment_year"));
                employee.setSpn(rs.getString("SPN"));
                employee.setStation(rs.getString("OFF_ADDRESS"));
                employee.setAddlCharge("");
                employee.setRemark("");
                employee.setPayScale(rs.getString("cur_salary"));
                employee.setBasic(rs.getInt("cur_basic_salary"));
                employee.setDob(CommonFunctions.getFormattedOutputDate1(rs.getDate("DOB")));
                employee.setDos(CommonFunctions.getFormattedOutputDate1(rs.getDate("DOS")));
                employee.setJoindategoo(CommonFunctions.getFormattedOutputDate1(rs.getDate("JOINDATE_OF_GOO")));
                employees.add(employee);
            }
        } catch (Exception exe) {
            exe.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return employees;
    }

    @Override
    public Employee getEmployeeData(String empid, String cadreCode) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Employee employee = null;
        try {
            con = this.dataSource.getConnection();
            /*pstmt = con.prepareStatement("SELECT emp_id,initials,f_name,m_name,l_name,cadreid,allotment_year,domicile,prm_dist_code,rec_source,jointime_of_goo,SPN,prm_address,curr_post_doj,cur_salary,dist_name FROM emp_mast "
             + "LEFT JOIN g_district ON g_district.dist_code=emp_mast.prm_dist_code "
             + "LEFT OUTER JOIN G_SPC ON emp_mast.cur_spc = G_SPC.SPC WHERE emp_id=?");*/
            pstmt = con.prepareStatement("SELECT spn,emp_mast.emp_id,initials,f_name,m_name,l_name,cadreid,allotment_year,domicile,rec_source,joindate_of_goo,curr_post_doj,cur_salary,cur_spc,dist_name,emp_address.dist_code prm_dist_code,address prm_address"
                    + " from emp_mast"
                    + " left outer join (select * from emp_address where address_type='PERMANENT' order by address_id desc limit 1)emp_address on emp_mast.emp_id=emp_mast.emp_id"
                    + " left outer join g_district on emp_address.dist_code=g_district.dist_code"
                    + " LEFT OUTER JOIN G_SPC ON emp_mast.cur_spc = G_SPC.SPC WHERE emp_mast.emp_id=?");

            pstmt.setString(1, empid);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                employee = new Employee();
                employee.setEmpid(rs.getString("emp_id"));
                employee.setIntitals(rs.getString("initials"));
                employee.setFname(rs.getString("f_name"));
                employee.setMname(rs.getString("m_name"));
                employee.setLname(rs.getString("l_name"));
                employee.setCadreId(rs.getString("cadreid"));
                employee.setRecsource(rs.getString("rec_source"));
                employee.setCardeallotmentyear(rs.getString("allotment_year"));
                employee.setDomicile(rs.getString("domicile"));
                employee.setPermanentdist(rs.getString("dist_name"));
                employee.setPermanentaddr(rs.getString("prm_address"));
                employee.setJoindategoo(CommonFunctions.getFormattedOutputDate1(rs.getDate("joindate_of_goo")));
                employee.setSpn(rs.getString("SPN"));
                //employee.setDateOfCurPosting(formatDate(rs.getDate("curr_post_doj")));
                employee.setPayScale(rs.getString("cur_salary"));
            }
        } catch (Exception exe) {
            exe.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return employee;
    }

    @Override
    public void saveEmployeeData(Employee employee) {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = this.dataSource.getConnection();
            pstmt = con.prepareStatement("INSERT INTO emp_mast_cadre (emp_id,f_name,m_name,l_name,dob,gender)");

        } catch (Exception exe) {
            exe.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    public static String getEmpID() throws Exception {
        Connection con = null;
        ResultSet rs = null;
        String maxID = null;
        String strID = null;
        PreparedStatement pstmt = null;
        try {

            pstmt = con.prepareStatement("SELECT MAX(EMP_ID) EMP_ID FROM emp_mast_cadre WHERE EMP_ID LIKE '91%'");
            rs = pstmt.executeQuery();
            int empCtr = 0;
            if (rs != null) {
                while (rs.next()) {
                    maxID = rs.getString("EMP_ID");
                    if (maxID != null && !maxID.equals("")) {
                        empCtr = Integer.parseInt(maxID);
                        empCtr++;
                        strID = Integer.toString(empCtr);
                    } else {
                        strID = "91000001";
                    }
                }
            } else {
                strID = "91000001";
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(con);
        }

        return strID;

    }

    @Override
    public ArrayList getIncumbancyChart(String spc) {
        ArrayList incumbancychart = new ArrayList();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = this.dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT EMP_JOIN.EMP_ID,EMP_JOIN.NOT_TYPE,JOIN_DATE,RLV_DATE,F_NAME,M_NAME,L_NAME FROM EMP_JOIN "
                    + "LEFT OUTER JOIN EMP_RELIEVE ON  EMP_JOIN.JOIN_ID = EMP_RELIEVE.JOIN_ID "
                    + "INNER JOIN EMP_MAST ON EMP_MAST.EMP_ID = EMP_JOIN.emp_id where EMP_JOIN.spc = ?");
            pstmt.setString(1, spc);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                IncumbancyChart ichart = new IncumbancyChart();
                ichart.setEmpid(rs.getString("EMP_ID"));
                ichart.setFname(rs.getString("F_NAME"));
                ichart.setMname(rs.getString("M_NAME"));
                ichart.setLname(rs.getString("L_NAME"));
                ichart.setJointype(rs.getString("NOT_TYPE"));
                ichart.setJoindate(rs.getDate("JOIN_DATE"));
                ichart.setRelievedate(rs.getDate("RLV_DATE"));
                incumbancychart.add(ichart);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return incumbancychart;
    }

    @Override
    public ArrayList getEmployeeIncumbancyChart(String empid) {
        ArrayList incumbancychart = new ArrayList();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = this.dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT EMP_JOIN.EMP_ID,EMP_JOIN.NOT_TYPE,JOIN_DATE,RLV_DATE,F_NAME,M_NAME,L_NAME FROM EMP_JOIN "
                    + "LEFT OUTER JOIN EMP_RELIEVE ON  EMP_JOIN.JOIN_ID = EMP_RELIEVE.JOIN_ID "
                    + "INNER JOIN EMP_MAST ON EMP_MAST.EMP_ID = EMP_JOIN.emp_id where EMP_JOIN.emp_id = ?");
            pstmt.setString(1, empid);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                IncumbancyChart ichart = new IncumbancyChart();
                ichart.setEmpid(rs.getString("EMP_ID"));
                ichart.setFname(rs.getString("F_NAME"));
                ichart.setMname(rs.getString("M_NAME"));
                ichart.setLname(rs.getString("L_NAME"));
                ichart.setJointype(rs.getString("NOT_TYPE"));
                ichart.setJoindate(rs.getDate("JOIN_DATE"));
                ichart.setRelievedate(rs.getDate("RLV_DATE"));
                incumbancychart.add(ichart);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return incumbancychart;
    }

    @Override
    public void SavecommentERSheet(EmployeeERCommentForm ersheet, String empid, String gpfNo) {

        Connection con = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        String createdDateTime = "";
        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
        createdDateTime = dateFormat.format(cal.getTime());

        try {
            con = dataSource.getConnection();
            ps = con.prepareStatement("SELECT * FROM ersheet_comments WHERE empid=? ");
            ps.setString(1, empid);
            rs1 = ps.executeQuery();
            if (rs1.next()) {
                ps1 = con.prepareStatement("UPDATE ersheet_comments SET profile_comment=?,eductaion_comment=?,training_comment=?,exp_comment=?,deputation_comment=? WHERE empid=?");
                ps1.setString(1, ersheet.getProfile_comment());
                ps1.setString(2, ersheet.getEducation_comment());
                ps1.setString(3, ersheet.getTraining_comment());
                ps1.setString(4, ersheet.getExperience_comment());
                ps1.setString(5, ersheet.getDeputation_comment());
                ps1.setString(6, empid);
                ps1.executeUpdate();
            } else {
                int comentId = CommonFunctions.getMaxCode(con, "ersheet_comments", "comments_id");
                ps1 = con.prepareStatement("INSERT INTO ersheet_comments (profile_comment,eductaion_comment,training_comment,exp_comment,empid,gpfno,entry_date_time,status,comments_id,deputation_comment) VALUES (?,?,?,?,?,?,?,?,?,?)");
                ps1.setString(1, ersheet.getProfile_comment());
                ps1.setString(2, ersheet.getEducation_comment());
                ps1.setString(3, ersheet.getTraining_comment());
                ps1.setString(4, ersheet.getExperience_comment());
                ps1.setString(5, empid);
                ps1.setString(6, gpfNo);
                ps1.setTimestamp(7, new Timestamp(dateFormat.parse(createdDateTime).getTime()));
                ps1.setString(8, "Pending");
                ps1.setInt(9, comentId);
                ps1.setString(10, ersheet.getDeputation_comment());
                ps1.execute();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
            DataBaseFunctions.closeSqlObjects(ps, ps1);
        }
    }

    @Override
    public EmployeeERCommentForm editcommentErsheet(String empId) {
        EmployeeERCommentForm ersheet = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("SELECT * FROM ersheet_comments WHERE empid=?");
            pst.setString(1, empId);
            rs = pst.executeQuery();
            if (rs.next()) {
                ersheet = new EmployeeERCommentForm();
                ersheet.setProfile_comment(rs.getString("profile_comment"));
                ersheet.setEducation_comment(rs.getString("eductaion_comment"));
                ersheet.setTraining_comment(rs.getString("training_comment"));
                ersheet.setExperience_comment(rs.getString("exp_comment"));
                ersheet.setDeputation_comment(rs.getString("deputation_comment"));

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return ersheet;
    }
}
