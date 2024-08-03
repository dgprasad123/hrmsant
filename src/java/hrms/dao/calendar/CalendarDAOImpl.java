/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.calendar;

import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.model.calendar.CalendarMonth;
import hrms.model.calendar.CalendarYearMonth;
import hrms.model.calendar.HolidayBean;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 *
 * @author Durga
 */
public class CalendarDAOImpl implements CalendarDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List getHolidayList() {
        List li = new ArrayList();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("SELECT HTYPE, to_char(G_HOLIDAY.FDATE, 'DD-MM-YYYY') AS hdate, description"
                    + "  FROM G_HOLIDAY WHERE G_HOLIDAY.HTYPE='G' OR G_HOLIDAY.HTYPE='O'");
            rs = ps.executeQuery();
            while (rs.next()) {
                li.add(rs.getString("hdate") + "->" + rs.getString("description") + "->" + rs.getString("HTYPE"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    @Override
    public int addHoliday(HolidayBean hbean) {
        int status = 0;
        Connection con = null;
        PreparedStatement ps = null;
        int hid = 0;
        try {
            con = this.dataSource.getConnection();
            hid = CommonFunctions.getMaxCode(con, "g_holiday", "hid");
            String str = "INSERT INTO g_holiday(htype, fdate, tdate, description) values(?,?,?,?)";
            ps = con.prepareStatement(str);
            ps.setString(1, hbean.getHolidayType());
            ps.setDate(2, new java.sql.Date(hbean.getFdate().getTime()));
            ps.setDate(3, new java.sql.Date(hbean.getTdate().getTime()));
            ps.setString(4, hbean.getHolidayName());
            status = ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return status;
    }

    @Override
    public int removeHoliday(int holidayId) {
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        int status = 0;
        try {
            con = this.dataSource.getConnection();
            st = con.createStatement();
            String str = "DELETE FROM g_holiday WHERE hid = " + holidayId;
            ps = con.prepareStatement(str);
            status = ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return status;
    }

    @Override
    public HolidayBean editHoliday(int holidayId) {
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        HolidayBean hBean = null;
        try {
            con = this.dataSource.getConnection();
            st = con.createStatement();
            rs = st.executeQuery("SELECT hid, htype, fdate,tdate, description FROM g_holiday WHERE hid = " + holidayId);
            while (rs.next()) {
                hBean = new HolidayBean();
                hBean.setHolidayType(rs.getString("htype"));
                hBean.setHolidayId(Integer.parseInt(rs.getString("hid")));
                hBean.setHolidayName(rs.getString("description"));
                hBean.setFdate(rs.getDate("fdate"));
                hBean.setTdate(rs.getDate("fdate"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return hBean;
    }

    @Override
    public int updateHoliday(HolidayBean hBean) {
        Connection con = null;
        PreparedStatement ps = null;
        String holidayType = null;
        String holidayName = null;
        String fDate = null;
        Date dt1 = null;
        int status = 0;
        int holidayId = 0;
        try {
            con = this.dataSource.getConnection();
            holidayType = hBean.getHolidayType();
            holidayName = hBean.getHolidayName();
            holidayId = hBean.getHolidayId();

            String str = "UPDATE g_holiday SET htype = ? ,fdate = ?,tdate = ?, description = ? WHERE hid = ?";
            ps = con.prepareStatement(str);
            ps.setString(1, holidayType);
            ps.setDate(2, new java.sql.Date(hBean.getFdate().getTime()));
            ps.setDate(3, new java.sql.Date(hBean.getTdate().getTime()));
            ps.setString(4, holidayName);
            ps.setInt(5, holidayId);
            status = ps.executeUpdate();
            

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return status;
    }

    @Override
    public List getHolidayList(int year) {
        List li = new ArrayList();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("select * FROM G_HOLIDAY WHERE EXTRACT(year from fdate)= ?");
            ps.setInt(1, year);
            rs = ps.executeQuery();
            while (rs.next()) {
                HolidayBean holidayBean = new HolidayBean();
                holidayBean.setHolidayId(rs.getInt("HID"));
                holidayBean.setHolidayName(rs.getString("DESCRIPTION"));
                holidayBean.setHolidayType(rs.getString("HTYPE"));
                holidayBean.setFdate(rs.getDate("FDATE"));
                holidayBean.setTdate(rs.getDate("TDATE"));
                li.add(holidayBean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    @Override
    public List getEmployeeCalendar(String empId) {
        List calendarList = new ArrayList();
        List holidays = getHolidayList(2023);        
        for (int i = 0; i < 12; i++) {
            CalendarYearMonth calYearMonth = new CalendarYearMonth();
            calYearMonth.setCalendarYear(2023);
            CalendarMonth calMonth = new CalendarMonth(i,2023,holidays);            
            calYearMonth.setCalendarMonth(calMonth);            
            calendarList.add(calYearMonth);
        }
        return calendarList;
    }
}
