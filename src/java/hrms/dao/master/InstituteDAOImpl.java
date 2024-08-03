/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.master;

import hrms.common.DataBaseFunctions;
import hrms.model.trainingadmin.InstituteForm;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 *
 * @author Manoj PC
 */
public class InstituteDAOImpl implements InstituteDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List getInstituteList(String ownerId, int page, int rows) {
        List li = new ArrayList();
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            con = this.dataSource.getConnection();
            st = con.createStatement();

            int firstpage = (page > 1) ? ((page - 1) * rows) : 0;
            rs = st.executeQuery("SELECT *"
                    + " FROM g_institutions limit " + rows + " offset " + firstpage);
            InstituteForm inf = null;
            while (rs.next()) {
                inf = new InstituteForm();
                inf.setInstituteId(rs.getInt("institution_code"));
                inf.setInstituteName(rs.getString("institution_name"));
                inf.setLocation(rs.getString("location"));
                if (rs.getString("website") != null) {
                    inf.setWebsite(rs.getString("website"));
                } else {
                    inf.setWebsite("");
                }
                if (rs.getString("email") != null) {
                    inf.setEmail(rs.getString("email"));
                } else {
                    inf.setEmail("");
                }
                if (rs.getString("phone") != null) {
                    inf.setPhone(rs.getString("phone"));
                } else {
                    inf.setPhone("");
                }
                if (rs.getString("contact_person") != null) {
                    inf.setContactPerson(rs.getString("contact_person"));
                } else {
                    inf.setContactPerson("");
                }
                inf.setOutsideTerritory(rs.getString("outside_territory"));
                li.add(inf);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    @Override
    public void updateInstitute(InstituteForm instForm, String empId) {

    }

    @Override
    public void saveInstitute(InstituteForm instForm, String empId) {
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        String instituteName = null;
        String location = null;
        String website = null;
        String email = null;
        String phone = null;
        String contactPerson = null;
        String outsideTerritory = null;
        String username = null;
        String password = null;
        try {
            con = this.dataSource.getConnection();
            //trainingFaculty = trainingPrograms.getTrainingProgram();
            instituteName = instForm.getInstituteName();
            location = instForm.getLocation();
            website = instForm.getWebsite();
            email = instForm.getEmail();
            phone = instForm.getPhone();
            contactPerson = instForm.getContactPerson();
            outsideTerritory = instForm.getOutsideTerritory();
            username = instForm.getUsername();
            password = instForm.getPassword();
            String str = "INSERT INTO g_institutions(institution_name, location, website, email, phone, contact_person, outside_territory) values(?,?,?,?,?,?,?)";
            ps = con.prepareStatement(str, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, instituteName);
            ps.setString(2, location);
            ps.setString(3, website);
            ps.setString(4, email);
            ps.setString(5, phone);
            ps.setString(6, contactPerson);
            ps.setString(7, outsideTerritory);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            int instituteCode = rs.getInt(1);
            //Now Insert into user details table
            String str1 = "INSERT INTO user_details(username, password, enable, accountnonexpired"
                    + ", accountnonlocked, credentialsnonexpired, usertype, linkid) VALUES(?,?,?,?,?,?,?,?)";
            ps1 = con.prepareStatement(str1);
            ps1.setString(1, username);
            ps1.setString(2, password);
            ps1.setInt(3, 1);
            ps1.setInt(4, 1);
            ps1.setInt(5, 1);
            ps1.setInt(6, 1);
            ps1.setString(7, "I");
            ps1.setString(8, "" + instituteCode);
            ps1.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void saveOnlineInstitute(InstituteForm instForm, String empId) {
        Connection con = null;
        PreparedStatement ps = null;
        String instituteName = null;
        String location = null;
        String website = null;
        String email = null;
        String phone = null;
        String contactPerson = null;
        String outsideTerritory = null;
        try {
            con = this.dataSource.getConnection();
            //trainingFaculty = trainingPrograms.getTrainingProgram();
            instituteName = instForm.getInstituteName();
            location = instForm.getLocation();
            website = instForm.getWebsite();
            email = instForm.getEmail();
            phone = instForm.getPhone();
            contactPerson = instForm.getContactPerson();
            outsideTerritory = instForm.getOutsideTerritory();
            String str = "INSERT INTO g_institutions(institution_name, location, website, email, phone, contact_person, outside_territory, is_online, dept_code, is_allowed, course_name) values(?,?,?,?,?,?,?, ?,?, ?,?)";
            ps = con.prepareStatement(str, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, instituteName);
            ps.setString(2, location);
            ps.setString(3, website);
            ps.setString(4, email);
            ps.setString(5, phone);
            ps.setString(6, contactPerson);
            ps.setString(7, outsideTerritory);
            ps.setString(8, "Y");
            ps.setString(9, instForm.getDeptCode());
            ps.setString(10, instForm.getIsAllowed());
            ps.setString(11, instForm.getCourseName());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public InstituteForm getOnlineInstituteDetail(int instituteId) {
        InstituteForm instForm = new InstituteForm();
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        InstituteForm inf = null;
        try {
            con = this.dataSource.getConnection();
            st = con.createStatement();

            rs = st.executeQuery("SELECT *"
                    + " FROM g_institutions WHERE institution_code = "+instituteId);
            
            while (rs.next()) {
                inf = new InstituteForm();
                
                inf.setInstituteId(rs.getInt("institution_code"));
                inf.setInstituteName(rs.getString("institution_name"));
                inf.setLocation(rs.getString("location"));
                inf.setCourseName(rs.getString("course_name"));
                if (rs.getString("website") != null) {
                    inf.setWebsite(rs.getString("website"));
                } else {
                    inf.setWebsite("");
                }
                if (rs.getString("email") != null) {
                    inf.setEmail(rs.getString("email"));
                } else {
                    inf.setEmail("");
                }
                if (rs.getString("phone") != null) {
                    inf.setPhone(rs.getString("phone"));
                } else {
                    inf.setPhone("");
                }
                if (rs.getString("contact_person") != null) {
                    inf.setContactPerson(rs.getString("contact_person"));
                } else {
                    inf.setContactPerson("");
                }
                inf.setOutsideTerritory(rs.getString("outside_territory"));
                inf.setIsAllowed(rs.getString("is_allowed"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }        
        return inf;
    }

    @Override
    public void updateOnlineInstitute(InstituteForm instForm, int instituteId) {
        Connection con = null;
        PreparedStatement ps = null;
        String instituteName = null;
        String location = null;
        String website = null;
        String email = null;
        String phone = null;
        String contactPerson = null;
        String outsideTerritory = null;
        try {
            con = this.dataSource.getConnection();
            //trainingFaculty = trainingPrograms.getTrainingProgram();
            instituteName = instForm.getInstituteName();
            location = instForm.getLocation();
            website = instForm.getWebsite();
            email = instForm.getEmail();
            phone = instForm.getPhone();
            contactPerson = instForm.getContactPerson();
            outsideTerritory = instForm.getOutsideTerritory();
            String str = "UPDATE g_institutions SET institution_name = ?"
                    + ", location = ?"
                    + ", website = ?"
                    + ", email = ?"
                    + ", phone = ?"
                    + ", contact_person = ?"
                    + ", outside_territory = ?"
                    + ", is_allowed = ?"
                    + ", course_name = ?"
                    + " WHERE institution_code = ?";
            ps = con.prepareStatement(str);
            ps.setString(1, instituteName);
            ps.setString(2, location);
            ps.setString(3, website);
            ps.setString(4, email);
            ps.setString(5, phone);
            ps.setString(6, contactPerson);
            ps.setString(7, outsideTerritory);
            ps.setString(8, instForm.getIsAllowed());
            ps.setString(9, instForm.getCourseName());
            ps.setInt(10, instituteId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public List getTAInstituteList() {
        List li = new ArrayList();
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            con = this.dataSource.getConnection();
            st = con.createStatement();

            rs = st.executeQuery("SELECT *"
                    + " FROM g_institutions ");
            InstituteForm inf = null;
            while (rs.next()) {
                inf = new InstituteForm();
                inf.setInstituteId(rs.getInt("institution_code"));
                inf.setInstituteName(rs.getString("institution_name"));
                inf.setLocation(rs.getString("location"));
                if (rs.getString("website") != null) {
                    inf.setWebsite(rs.getString("website"));
                } else {
                    inf.setWebsite("");
                }
                if (rs.getString("email") != null) {
                    inf.setEmail(rs.getString("email"));
                } else {
                    inf.setEmail("");
                }
                if (rs.getString("phone") != null) {
                    inf.setPhone(rs.getString("phone"));
                } else {
                    inf.setPhone("");
                }
                if (rs.getString("contact_person") != null) {
                    inf.setContactPerson(rs.getString("contact_person"));
                } else {
                    inf.setContactPerson("");
                }
                inf.setOutsideTerritory(rs.getString("outside_territory"));
                li.add(inf);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    @Override
    public List getOnlineInstituteList(String deptCode, String isAllowed) {
        List li = new ArrayList();
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        String sql = null;
        try {
            con = this.dataSource.getConnection();
            st = con.createStatement();
            sql = "SELECT GI.*, D.department_name FROM g_institutions GI LEFT OUTER JOIN g_department D ON GI.dept_code = D.department_code WHERE GI.is_online = 'Y'";
            if (!deptCode.equals("")) {
                sql += " AND dept_code = '" + deptCode + "'";
            }
            if (!isAllowed.equals("")) {
                sql += " AND is_allowed = '" + isAllowed + "'";
            }
            rs = st.executeQuery(sql);
            InstituteForm inf = null;
            while (rs.next()) {
                inf = new InstituteForm();
                inf.setInstituteId(rs.getInt("institution_code"));
                inf.setInstituteName(rs.getString("institution_name"));
                inf.setLocation(rs.getString("location"));
                if (rs.getString("website") != null) {
                    inf.setWebsite(rs.getString("website"));
                    inf.setInstituteDetail(rs.getString("institution_name")+"("+rs.getString("website")+") Course: "+rs.getString("course_name"));
                } else {
                    inf.setWebsite("");
                    inf.setInstituteDetail(rs.getString("institution_name"));
                }
                if (rs.getString("email") != null) {
                    inf.setEmail(rs.getString("email"));
                } else {
                    inf.setEmail("");
                }
                if (rs.getString("phone") != null) {
                    inf.setPhone(rs.getString("phone"));
                } else {
                    inf.setPhone("");
                }
                if (rs.getString("contact_person") != null) {
                    inf.setContactPerson(rs.getString("contact_person"));
                } else {
                    inf.setContactPerson("");
                }
                inf.setOutsideTerritory(rs.getString("outside_territory"));
                inf.setDeptName(rs.getString("department_name"));
                inf.setIsAllowed(rs.getString("is_allowed"));
                inf.setCourseName(rs.getString("course_name"));
                li.add(inf);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    @Override
    public List getDeptOnlineInstituteList(String deptId) {
        List li = new ArrayList();
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            con = this.dataSource.getConnection();
            st = con.createStatement();

            rs = st.executeQuery("SELECT *"
                    + " FROM g_institutions  WHERE is_online = 'Y' AND dept_code = '" + deptId + "'");
            InstituteForm inf = null;
            while (rs.next()) {
                inf = new InstituteForm();
                inf.setInstituteId(rs.getInt("institution_code"));
                inf.setInstituteName(rs.getString("institution_name"));
                inf.setLocation(rs.getString("location"));
                inf.setCourseName(rs.getString("course_name"));
                if (rs.getString("website") != null) {
                    inf.setWebsite(rs.getString("website"));
                } else {
                    inf.setWebsite("");
                }
                if (rs.getString("email") != null) {
                    inf.setEmail(rs.getString("email"));
                } else {
                    inf.setEmail("");
                }
                if (rs.getString("phone") != null) {
                    inf.setPhone(rs.getString("phone"));
                } else {
                    inf.setPhone("");
                }
                if (rs.getString("contact_person") != null) {
                    inf.setContactPerson(rs.getString("contact_person"));
                } else {
                    inf.setContactPerson("");
                }
                inf.setOutsideTerritory(rs.getString("outside_territory"));
                li.add(inf);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }
}
