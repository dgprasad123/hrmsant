/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.calendar;

import hrms.model.calendar.HolidayBean;
import java.util.List;

/**
 *
 * @author Durga
 */
public interface CalendarDAO {

    public List getHolidayList();

    public List getHolidayList(int year);

    public int addHoliday(HolidayBean holiday);

    public int removeHoliday(int holidayId);

    public HolidayBean editHoliday(int holidayId);

    public int updateHoliday(HolidayBean holiday);
    
    public List getEmployeeCalendar(String empId);
}
