/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.master;

import hrms.common.DataBaseFunctions;
import hrms.model.master.Faculty;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 *
 * @author lenovo pc
 */
public class FacultyDAOImpl implements FacultyDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public ArrayList getFacultyList() {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement st = null;
        ArrayList facultyList = new ArrayList();
        Faculty faculty = null;
        try {
            con = dataSource.getConnection();
            st = con.prepareStatement("SELECT FACULTY_CODE,FACULTY_NAME,FACULTY_TYPE FROM G_FACULTY ORDER BY FACULTY_NAME");
            rs = st.executeQuery();
            while (rs.next()) {
                faculty = new Faculty();
                faculty.setFacultyCode(rs.getString("FACULTY_CODE"));
                faculty.setFacultyName(rs.getString("FACULTY_NAME"));
                faculty.setFacultyType(rs.getString("FACULTY_TYPE"));
                facultyList.add(faculty);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return facultyList;
    }
    
    @Override
    public ArrayList getSubjectFacultyList() {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement st = null;
        ArrayList facultyList = new ArrayList();
        Faculty faculty = null;
        try {
            con = dataSource.getConnection();
            st = con.prepareStatement("SELECT FACULTY_TYPE FROM G_FACULTY ORDER BY FACULTY_TYPE");
            rs = st.executeQuery();
            while (rs.next()) {
                faculty = new Faculty();                
                faculty.setFacultyType(rs.getString("FACULTY_TYPE"));
                facultyList.add(faculty);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return facultyList;
    }

}
