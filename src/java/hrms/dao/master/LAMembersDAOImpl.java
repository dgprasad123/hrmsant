/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.master;

import hrms.common.DataBaseFunctions;
import hrms.model.lamembers.LAMembers;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 *
 * @author Manas
 */
public class LAMembersDAOImpl implements LAMembersDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List getLAMembersList() {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        ArrayList lamemberlist = new ArrayList();
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT * FROM LA_MEMBERS INNER JOIN G_OFFICIATING ON LA_MEMBERS.OFF_AS = G_OFFICIATING.OFF_ID");
            rs = pstmt.executeQuery();
            while (rs.next()) {
                LAMembers lamember = new LAMembers();
                lamember.setEmpId(rs.getInt("LMID"));
                lamember.setInitials(rs.getString("INITIALS"));
                lamember.setFname(rs.getString("FNAME"));
                lamember.setMname(rs.getString("MNAME"));
                lamember.setLname(rs.getString("LNAME"));
                lamember.setOffName(rs.getString("OFF_NAME"));
                lamember.setActive(rs.getString("ACTIVE"));
                lamemberlist.add(lamember);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return lamemberlist;
    }

    @Override
    public LAMembers getLAMemberDetail(int lmid) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        LAMembers laMembersDetail = new LAMembers();
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT * FROM LA_MEMBERS INNER JOIN G_OFFICIATING ON LA_MEMBERS.OFF_AS = G_OFFICIATING.OFF_ID WHERE LMID=?");
            pstmt.setInt(1, lmid);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                laMembersDetail.setEmpId(rs.getInt("LMID"));
                laMembersDetail.setInitials(rs.getString("INITIALS"));
                laMembersDetail.setFname(rs.getString("FNAME"));
                laMembersDetail.setMname(rs.getString("MNAME"));
                laMembersDetail.setLname(rs.getString("LNAME"));
                laMembersDetail.setOffName(rs.getString("OFF_NAME"));
                laMembersDetail.setActive(rs.getString("ACTIVE"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return laMembersDetail;
    }

    @Override
    public void saveLAMember(LAMembers lamembers) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        Statement stmt = null;
        int mcode = 0;
        try {
            con = dataSource.getConnection();
            stmt = con.createStatement();
            rs = stmt.executeQuery("select max(lmid)+1 lmid from LA_MEMBERS");
            if (rs.next()) {
                mcode = rs.getInt("lmid");
            }
            pstmt = con.prepareStatement("INSERT INTO LA_MEMBERS (lmid, initials, fname ,mname, lname, off_as, active,mobile) VALUES (?,?,?,?,?,?,?,?)");
            pstmt.setInt(1, mcode);
            pstmt.setString(2, lamembers.getInitials());
            pstmt.setString(3, lamembers.getFname());
            pstmt.setString(4, lamembers.getMname());
            pstmt.setString(5, lamembers.getLname());
            pstmt.setString(6, lamembers.getOff_as());
            pstmt.setString(7, "Y");
            pstmt.setString(8, lamembers.getMobile());
            pstmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public List getLAMembersList(String officiating) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        ArrayList lamemberlist = new ArrayList();
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT * FROM LA_MEMBERS INNER JOIN G_OFFICIATING ON LA_MEMBERS.OFF_AS = G_OFFICIATING.OFF_ID WHERE OFF_AS=?");
            pstmt.setString(1, officiating);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                LAMembers lamember = new LAMembers();
                lamember.setEmpId(rs.getInt("LMID"));
                lamember.setInitials(rs.getString("INITIALS"));
                lamember.setFname(rs.getString("FNAME"));
                lamember.setMname(rs.getString("MNAME"));
                lamember.setLname(rs.getString("LNAME"));
                lamember.setOffName(rs.getString("OFF_NAME"));
                lamember.setActive(rs.getString("ACTIVE"));
                lamemberlist.add(lamember);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return lamemberlist;
    }

    @Override
    public void inActivateMember(int lmid) {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("UPDATE LA_MEMBERS SET ACTIVE='N' WHERE LMID=?");
            pstmt.setInt(1, lmid);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void activateMember(int lmid) {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("UPDATE LA_MEMBERS SET ACTIVE='Y' WHERE LMID=?");
            pstmt.setInt(1, lmid);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

}
