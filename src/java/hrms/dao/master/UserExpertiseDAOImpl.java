package hrms.dao.master;

import hrms.common.DataBaseFunctions;
import hrms.model.login.UserExpertise;
import hrms.model.master.Treasury;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.springframework.stereotype.Component;

/**
 *
 * @author Manas Jena
 */
@Component
public class UserExpertiseDAOImpl implements UserExpertiseDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public String saveUserExpertise(UserExpertise ue) {
        Connection con = null;
        PreparedStatement pst = null;
        Statement st = null;
        ResultSet rs = null;
        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
        String startTime = dateFormat.format(cal.getTime());

        String msg = "Success";
        try {
            con = this.dataSource.getConnection();
            st = con.createStatement();
            pst = con.prepareStatement("INSERT INTO g_emp_expertise(emp_id, emp_name, designation, grade, department"
                    + ", current_place_of_posting, office_stationed, area_of_expertise, area_of_interest, willingness_to_serve"
                    + ", mobile, landline_number, office_phone_number, email_id, date_updated)"
                    + " Values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            pst.setString(1, ue.getEmpid());
            pst.setString(2, ue.getName());
            pst.setString(3, ue.getDesignation());
            pst.setString(4, ue.getGrade());
            pst.setString(5, ue.getDeptname());
            pst.setString(6, ue.getPostingPlace());
            pst.setString(7, ue.getCurofficename());
            pst.setString(8, ue.getAreaOfExpertise());
            pst.setString(9, ue.getAreaOfInterest());
            pst.setString(10, ue.getVolWillingness());
            pst.setString(11, ue.getMobile());
            pst.setString(12, ue.getLandline());
            pst.setString(13, ue.getOfficePhone());
            pst.setString(14, ue.getEmailid());
            pst.setTimestamp(15, new Timestamp(dateFormat.parse(startTime).getTime()));

            pst.executeUpdate();
        } catch (Exception e) {
            msg = "Error";
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return msg;
    }

    @Override
    public String updateUserExpertise(UserExpertise ue, String empid) {
        Connection con = null;
        PreparedStatement pst = null;
        Statement st = null;
        ResultSet rs = null;
        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
        String startTime = dateFormat.format(cal.getTime());

        String msg = "Success";
        try {
            con = this.dataSource.getConnection();
            st = con.createStatement();
            pst = con.prepareStatement("UPDATE g_emp_expertise SET"
                    + " emp_name = ?"
                    + ", designation = ?"
                    + ", grade = ?"
                    + ", department = ?"
                    + ", current_place_of_posting = ?"
                    + ", office_stationed = ?"
                    + ", area_of_expertise = ?"
                    + ", area_of_interest = ?"
                    + ", willingness_to_serve = ?"
                    + ", mobile = ?"
                    + ", landline_number = ?"
                    + ", office_phone_number = ?"
                    + ", email_id = ?"
                    + ", date_updated = ? WHERE emp_id = ?");

            pst.setString(1, ue.getName());
            pst.setString(2, ue.getDesignation());
            pst.setString(3, ue.getGrade());
            pst.setString(4, ue.getDeptname());
            pst.setString(5, ue.getPostingPlace());
            pst.setString(6, ue.getCurofficename());
            pst.setString(7, ue.getAreaOfExpertise());
            pst.setString(8, ue.getAreaOfInterest());
            pst.setString(9, ue.getVolWillingness());
            pst.setString(10, ue.getMobile());
            pst.setString(11, ue.getLandline());
            pst.setString(12, ue.getOfficePhone());
            pst.setString(13, ue.getEmailid());
            pst.setTimestamp(14, new Timestamp(dateFormat.parse(startTime).getTime()));
            pst.setString(15, ue.getEmpid());

            pst.executeUpdate();
        } catch (Exception e) {
            msg = "Error";
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return msg;
    }

    @Override
    public UserExpertise getUserExpertise(String empid) {
        UserExpertise ue = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("SELECT * FROM g_emp_expertise WHERE emp_id=?");
            pst.setString(1, empid);
            rs = pst.executeQuery();
            if (rs.next()) {
                ue = new UserExpertise();
                ue.setAreaOfExpertise(rs.getString("area_of_expertise"));
                ue.setAreaOfInterest(rs.getString("area_of_interest"));
                ue.setEmpid(rs.getString("emp_id"));
                ue.setName(rs.getString("emp_name"));
                ue.setDesignation(rs.getString("designation"));
                ue.setGrade(rs.getString("grade"));
                ue.setDeptname(rs.getString("department"));
                ue.setPostingPlace(rs.getString("current_place_of_posting"));
                ue.setCurofficename(rs.getString("office_stationed"));
                ue.setVolWillingness(rs.getString("willingness_to_server"));
                ue.setMobile(rs.getString("mobile"));
                ue.setLandline(rs.getString("landline_number"));
                ue.setOfficePhone(rs.getString("office_phone_number"));
                ue.setEmailid(rs.getString("email_id"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return ue;
    }

    @Override
    public List getUserExpertiseList(int maxlimit, int minlimit) {
        List list = null;
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        UserExpertise ue = null;
        list = new ArrayList();
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("select g_emp_expertise.* from g_emp_expertise inner join emp_mast on g_emp_expertise.emp_id=emp_mast.emp_id left outer join g_cadre on emp_mast.cur_cadre_code=g_cadre.cadre_code\n"
                    + "where g_cadre.cadre_code in ('1101','1103','1165','0723') and g_emp_expertise.designation NOT like '%CLERK%' ORDER BY unique_id DESC limit ? offset ?");
            pst.setInt(1, maxlimit);
            pst.setInt(2, minlimit);
            rs = pst.executeQuery();
            while (rs.next()) {
                ue = new UserExpertise();
                ue.setAreaOfExpertise(rs.getString("area_of_expertise"));
                ue.setAreaOfInterest(rs.getString("area_of_interest"));
                ue.setEmpid(rs.getString("emp_id"));
                ue.setName("<strong>" + rs.getString("emp_name") + "</strong><br />" + rs.getString("designation") + "<br />" + rs.getString("department"));
                ue.setDesignation(rs.getString("designation"));
                ue.setGrade(rs.getString("grade"));
                ue.setDeptname(rs.getString("department"));
                ue.setPostingPlace(rs.getString("current_place_of_posting"));
                ue.setCurofficename(rs.getString("office_stationed"));
                ue.setVolWillingness(rs.getString("willingness_to_serve"));
                ue.setMobile(rs.getString("mobile"));
                ue.setLandline(rs.getString("landline_number"));
                ue.setOfficePhone(rs.getString("office_phone_number"));
                ue.setEmailid(rs.getString("email_id"));

                list.add(ue);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return list;
    }

    @Override
    public int getUserExpertiseCount() {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        int total = 0;
        try {
            con = this.dataSource.getConnection();

            String sql = "select count(*) cnt from g_emp_expertise inner join emp_mast on g_emp_expertise.emp_id=emp_mast.emp_id left outer join g_cadre on emp_mast.cur_cadre_code=g_cadre.cadre_code\n"
                    + "where g_cadre.cadre_code in ('1101','1103','1165','0723') and g_emp_expertise.designation NOT like '%CLERK%'";
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                total = rs.getInt("cnt");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return total;
    }
    @Override
    public String ViewExpertiseList() {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        UserExpertise ue = null;
        String content = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("select g_emp_expertise.* from g_emp_expertise inner join emp_mast on g_emp_expertise.emp_id=emp_mast.emp_id left outer join g_cadre on emp_mast.cur_cadre_code=g_cadre.cadre_code\n"
                    + "where g_cadre.cadre_code in ('1101','1103','1165','0723') and g_emp_expertise.designation NOT like '%CLERK%' ORDER BY unique_id DESC");
            rs = pst.executeQuery();
            content = "<table width='100%' cellspacing='1' cellpadding='4' border='0' style='background:#EAEAEA;font-family:Arial;font-size:6pt;'>"
                    + "  <tr style='font-weight:bold;background:#EAEAEA;'>"
                    + "  <td>Name</td>"
                    + "  <td>Designation</td>"
                    + "  <td>Grade</td>"
                    + "  <td>Department</td>"
                    + "  <td>Current Place of Posting</td>"
                    + "  <td>Office Stationed</td>"
                    + "  <td>Area of Expertise</td>"
                    + "  <td>Area of Interest</td>"
                    + "  <td>Willingness to Serve</td>"
                    + "  <td>Mobile</td>"
                    + "  <td>Email</td>"
                    + "  </tr>";
            while (rs.next()) {
                ue = new UserExpertise();
                   content+= "  <tr style='background:#FFFFFF;'>"
                    + "  <td>"+rs.getString("emp_name")+"</td>"
                    + "  <td>"+rs.getString("designation")+"</td>"
                    + "  <td>"+rs.getString("grade")+"</td>"
                    + "  <td>"+rs.getString("department")+"</td>"
                    + "  <td>"+rs.getString("current_place_of_posting")+"</td>"
                    + "  <td>"+rs.getString("office_stationed")+"</td>"
                    + "  <td>"+rs.getString("area_of_expertise")+"</td>"
                    + "  <td>"+rs.getString("area_of_interest")+"</td>"
                    + "  <td>"+rs.getString("willingness_to_serve")+"</td>"
                    + "  <td>"+rs.getString("mobile")+"</td>"
                    + "  <td>"+rs.getString("email_id")+"</td>"
                    + "  </tr>";                
                /*ue.setAreaOfExpertise(rs.getString("area_of_expertise"));
                ue.setAreaOfInterest(rs.getString("area_of_interest"));
                ue.setEmpid(rs.getString("emp_id"));
                ue.setName("<strong>"+rs.getString("emp_name")+"</strong><br />"+rs.getString("designation")+"<br />"+rs.getString("department"));
                ue.setDesignation(rs.getString("designation"));
                ue.setGrade(rs.getString("grade"));
                ue.setDeptname(rs.getString("department"));
                ue.setPostingPlace(rs.getString("current_place_of_posting"));
                ue.setCurofficename(rs.getString("office_stationed"));
                ue.setVolWillingness(rs.getString("willingness_to_serve"));
                ue.setMobile(rs.getString("mobile"));
                ue.setLandline(rs.getString("landline_number"));
                ue.setOfficePhone(rs.getString("office_phone_number"));
                ue.setEmailid(rs.getString("email_id"));*/
                //list.add(ue);
            }
            content+= "</table>";
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return content;
    }

}
