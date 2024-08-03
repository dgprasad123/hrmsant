/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.master;

import hrms.common.DataBaseFunctions;
import hrms.model.master.University;

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
public class UniversityDAOImpl implements UniversityDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public ArrayList getUniversityList() {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement st = null;
        ArrayList universityList = new ArrayList();
        University university = null;
        try {
            con = dataSource.getConnection();
            st = con.prepareStatement("SELECT board_sl_no,BOARD FROM G_BOARD_UNIVERSITY ORDER BY BOARD");
            rs = st.executeQuery();
            while (rs.next()) {
                university = new University();
                university.setBoardserialNumber(rs.getInt("board_sl_no"));
                university.setBoardName(rs.getString("BOARD"));
                universityList.add(university);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return universityList;
    }

    @Override
    public University getUniversityBoardDetail(String boardCode) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement st = null;
        University university = new University();
        try {
            con = dataSource.getConnection();
            st = con.prepareStatement("SELECT BOARD FROM G_BOARD_UNIVERSITY WHERE board_sl_no=? ");
            st.setString(1, boardCode);
            rs = st.executeQuery();
            if (rs.next()) {
                university.setBoardCode(rs.getString("board_sl_no"));
                university.setBoardName(rs.getString("BOARD"));

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return university;
    }

    @Override
    public void saveNewBoard(University university) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();

            pst = con.prepareStatement("INSERT INTO G_BOARD_UNIVERSITY(BOARD) VALUES(?)");
            pst.setString(1, university.getBoardName());
            pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void updateNewBoard(University university) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("UPDATE G_BOARD_UNIVERSITY SET BOARD=? where board_sl_no=?");
            pst.setString(1, university.getBoardName());
            pst.setInt(2, university.getBoardserialNumber());
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public University editUniversityBoard(University university) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = this.dataSource.getConnection();

            String sql = "select * from G_BOARD_UNIVERSITY where board_sl_no=?";
            
            pst = con.prepareStatement(sql);
            pst.setInt(1,university.getBoardserialNumber());
            //pst.setString(1, university.getBoardCode());
            rs = pst.executeQuery();
            if (rs.next()) {

                university.setBoardserialNumber(rs.getInt("board_sl_no"));
                university.setBoardName(rs.getString("BOARD"));

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return university;

    }
    public void deleteBoardDetail(University  university) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("DELETE FROM  G_BOARD_UNIVERSITY where board_sl_no=?");
            pst.setInt(1, university.getBoardserialNumber());
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }
}
