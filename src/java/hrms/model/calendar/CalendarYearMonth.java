/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.calendar;

/**
 *
 * @author Manas
 */
public class CalendarYearMonth {

    private int calendarYear;
    private CalendarMonth calendarMonth;

    public int getCalendarYear() {
        return calendarYear;
    }

    public void setCalendarYear(int calendarYear) {
        this.calendarYear = calendarYear;
    }

    public CalendarMonth getCalendarMonth() {
        return calendarMonth;
    }

    public void setCalendarMonth(CalendarMonth calendarMonth) {
        this.calendarMonth = calendarMonth;
    }

}
