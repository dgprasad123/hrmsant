package hrms.dao.empeducation;

import hrms.SelectOption;
import hrms.common.DataBaseFunctions;
import hrms.model.empeducation.EmpEducationList;
import hrms.model.empeducation.EmployeeEducation;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;

public class EmployeeEducationDAOList implements EmployeeEducationDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public ArrayList getQualification() throws Exception {
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        ArrayList alquali = new ArrayList();
        SelectOption so;
        try {
            con = dataSource.getConnection();
            rs = st.executeQuery("select QUALIFICATION from G_QUALIFICATION ORDER BY QUALIFICATION");
            while (rs.next()) {
                so = new SelectOption();
                so.setValue(rs.getString("QUALIFICATION"));
                so.setLabel(rs.getString("QUALIFICATION"));
                alquali.add(so);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return alquali;
    }

    public ArrayList getFaculty() throws Exception {
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        ArrayList alfac = new ArrayList();
        SelectOption so;
        try {
            con = dataSource.getConnection();
            rs = st.executeQuery("select FACULTY_TYPE from G_FACULTY ORDER BY FACULTY_TYPE");
            while (rs.next()) {
                so = new SelectOption();
                so.setValue(rs.getString("FACULTY_TYPE"));
                so.setLabel(rs.getString("FACULTY_TYPE"));
                alfac.add(so);
            }

        } catch (Exception e) {

            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return alfac;
    }

    public ArrayList getDegree() throws Exception {
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        ArrayList aldegree = new ArrayList();
        SelectOption so;
        try {
            con = dataSource.getConnection();
            rs = st.executeQuery("select DEGREE from G_DEGREE ORDER BY DEGREE");
            while (rs.next()) {
                so = new SelectOption();
                so.setValue(rs.getString("DEGREE"));
                so.setLabel(rs.getString("DEGREE"));
                aldegree.add(so);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return aldegree;
    }

    public ArrayList getUniversity() throws Exception {

        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        ArrayList aluniver = new ArrayList();
        SelectOption so;
        try {
            rs = st.executeQuery("select BOARD from G_BOARD_UNIVERSITY ORDER BY BOARD");
            while (rs.next()) {
                so = new SelectOption();
                so.setValue(rs.getString("BOARD"));
                so.setLabel(rs.getString("BOARD"));
                aluniver.add(so);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return aluniver;
    }

    public int insertEmployeeEducationData(EmployeeEducation empEdu) {
        Connection con = null;
        PreparedStatement pst = null;
        int n = 0;
        try {
            pst = con.prepareStatement("INSERT INTO EMP_QUALIFICATION (emp_id, qualification, faculty, yop, degree, subjects, institute, board_univ, doe, TOE, IF_ASSUMED, note,ent_dept,ent_off,ent_auth) Values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            pst.setString(1, empEdu.getEmpId());
            if (empEdu.getSltquali() != null && !empEdu.getSltquali().equalsIgnoreCase("")) {
                pst.setString(2, empEdu.getSltquali().toUpperCase());
            } else {
                pst.setString(2, null);
            }
            if (empEdu.getSltfac() != null && !empEdu.getSltfac().equalsIgnoreCase("")) {
                pst.setString(3, empEdu.getSltfac().toUpperCase());
            } else {
                pst.setString(3, null);
            }
            pst.setString(4, empEdu.getTxtyear().toUpperCase());
            if (empEdu.getSltdegree() != null && !empEdu.getSltdegree().equalsIgnoreCase("")) {
                pst.setString(5, empEdu.getSltdegree().toUpperCase());
            } else {
                pst.setString(5, null);
            }
            if (empEdu.getTxtsub() != null && !empEdu.getTxtsub().equalsIgnoreCase("")) {
                pst.setString(6, empEdu.getTxtsub().toUpperCase());
            } else {
                pst.setString(6, null);
            }
            if (empEdu.getTxtinsti() != null && !empEdu.getTxtinsti().equalsIgnoreCase("")) {
                pst.setString(7, empEdu.getTxtinsti().toUpperCase());
            } else {
                pst.setString(7, null);
            }
            if (empEdu.getSltuni() != null && !empEdu.getSltuni().equalsIgnoreCase("")) {
                pst.setString(8, empEdu.getSltuni().toUpperCase());
            } else {
                pst.setString(8, null);
            }
            pst.setString(9, empEdu.getDoe());
            pst.setString(10, empEdu.getToe());
            pst.setString(11, empEdu.getChkAssumedDoe().toUpperCase());
            pst.setString(12, empEdu.getTxtnote());

            if (empEdu.getEsltdept() != null && !empEdu.getEsltdept().trim().equalsIgnoreCase("")) {
                pst.setString(13, empEdu.getEsltdept().toUpperCase());
            } else {
                pst.setString(13, null);
            }
            if (empEdu.getEsltoffice() != null && !empEdu.getEsltoffice().trim().equalsIgnoreCase("")) {
                pst.setString(14, empEdu.getEsltoffice().toUpperCase());
            } else {
                pst.setString(14, null);
            }
            if (empEdu.getEsltauth() != null && !empEdu.getEsltauth().trim().equalsIgnoreCase("")) {
                pst.setString(15, empEdu.getEsltauth().toUpperCase());
            } else {
                pst.setString(15, null);
            }
            n = pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return n;

    }

    public int updateEmployeeEducationData(EmployeeEducation edu) {
        Connection con = null;
        PreparedStatement pst = null;
        int n = 0;
        try {
            pst = con.prepareStatement("UPDATE EMP_QUALIFICATION SET QUALIFICATION=?,FACULTY=?,YOP=?,DEGREE=?,SUBJECTS=?,INSTITUTE=?,BOARD_UNIV=?,NOTE=?,DOE=?,if_assumed=?,ent_dept=?,ent_off=?,ent_auth=?,emp_id=? WHERE QFN_ID=?");
            if (edu.getSltquali() != null && !edu.getSltquali().equalsIgnoreCase("")) {
                pst.setString(1, edu.getSltquali().toUpperCase());
            } else {
                pst.setString(1, null);
            }

            if (edu.getSltfac() != null && !edu.getSltfac().equalsIgnoreCase("")) {
                pst.setString(2, edu.getSltfac().toUpperCase());
            } else {
                pst.setString(2, null);
            }

            pst.setString(3, edu.getTxtyear().toUpperCase());
            if (edu.getSltdegree() != null && !edu.getSltdegree().equalsIgnoreCase("")) {
                pst.setString(4, edu.getSltdegree().toUpperCase());
            } else {
                pst.setString(4, null);
            }
            pst.setString(5, edu.getTxtsub().toUpperCase());
            pst.setString(6, edu.getTxtinsti().toUpperCase());
            if (edu.getSltuni() != null && !edu.getSltuni().equalsIgnoreCase("")) {
                pst.setString(7, edu.getSltuni().toUpperCase());
            } else {
                pst.setString(7, null);
            }
            pst.setString(8, edu.getTxtnote());
            pst.setString(9, edu.getDoe());
            pst.setString(10, edu.getChkAssumedDoe().toUpperCase());
            if (edu.getEsltdept() != null && !edu.getEsltdept().trim().equalsIgnoreCase("")) {
                pst.setString(11, edu.getEsltdept().toUpperCase());
            } else {
                pst.setString(11, null);
            }
            if (edu.getEsltoffice() != null && !edu.getEsltoffice().trim().equalsIgnoreCase("")) {
                pst.setString(12, edu.getEsltoffice().toUpperCase());
            } else {
                pst.setString(12, null);
            }
            if (edu.getEsltauth() != null && !edu.getEsltauth().trim().equalsIgnoreCase("")) {
                pst.setString(13, edu.getEsltauth().toUpperCase());
            } else {
                pst.setString(13, null);
            }
            pst.setString(14, edu.getEmpId());
            pst.setString(15, edu.getQfnId());
            n = pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return n;
    }

    public int deleteEmployeeEducationData(String empId, String qualificationId) {
        int n = 0;
        Connection con = null;
        PreparedStatement pst = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("DELETE FROM EMP_QUALIFICATION WHERE QFN_ID=?");
            pst.setString(1, qualificationId);
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return n;
    }

    public EmployeeEducation editEmployeeEducationData(String eduId, String qualification) {
        EmployeeEducation empEdu = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("SELECT emp_id, qualification, faculty, yop, degree, subjects, institute, board_univ, doe, TOE, IF_ASSUMED, note,ent_dept,ent_off,ent_auth FROM EMP_QUALIFICATION WHERE QFN_ID=?");
            pst.setString(1, eduId);
            rs = pst.executeQuery();
            if (rs.next()) {
                empEdu = new EmployeeEducation();
                empEdu.setEmpId(rs.getString("emp_id"));
                empEdu.setSltquali(rs.getString("qualification"));
                empEdu.setSltfac(rs.getString("faculty"));
                empEdu.setTxtyear(rs.getString("yop"));
                empEdu.setSltdegree(rs.getString("degree"));
                empEdu.setTxtsub(rs.getString("subjects"));
                empEdu.setTxtinsti(rs.getString("institute"));
                empEdu.setSltuni(rs.getString("board_univ"));
                empEdu.setDoe(rs.getString("doe"));
                empEdu.setToe(rs.getString("TOE"));
                empEdu.setChkAssumedDoe(rs.getString("IF_ASSUMED"));
                empEdu.setTxtnote(rs.getString("note"));
                empEdu.setEsltdept(rs.getString("ent_dept"));
                empEdu.setEsltoffice(rs.getString("ent_off"));
                empEdu.setEsltauth(rs.getString("ent_auth"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return empEdu;
    }

    public List findAllEmployeeEducation(String empId) {
        List list = null;
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        EmpEducationList eduList = null;
        list = new ArrayList();
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("SELECT QFN_ID, qualification, faculty, yop, degree, subjects, institute, board_univ, doe, TOE, IF_ASSUMED, note FROM EMP_QUALIFICATION WHERE EMP_ID='" + empId + "'");
            pst.setString(1, empId);
            rs = pst.executeQuery();
            while (rs.next()) {
                eduList = new EmpEducationList();
                eduList.setQfnId(rs.getString("QFN_ID"));
                eduList.setQuali(rs.getString("qualification"));
                eduList.setDoe(rs.getString("DOE"));
                eduList.setFaculty(rs.getString("faculty"));
                eduList.setDegree(rs.getString("degree"));
                eduList.setYear(rs.getString("yop"));
                list.add(eduList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return list;

    }
}
