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
public class CalendarDay {
    private int caldate;
    private int calyear;
    private int calmonth;
    private boolean isHoliday;
    private String holidayName;

    public int getCaldate() {
        return caldate;
    }

    public void setCaldate(int caldate) {
        this.caldate = caldate;
    }

    public int getCalyear() {
        return calyear;
    }

    public void setCalyear(int calyear) {
        this.calyear = calyear;
    }

    public int getCalmonth() {
        return calmonth;
    }

    public void setCalmonth(int calmonth) {
        this.calmonth = calmonth;
    }

    public boolean isIsHoliday() {
        return isHoliday;
    }

    public void setIsHoliday(boolean isHoliday) {
        this.isHoliday = isHoliday;
    }

    public String getHolidayName() {
        return holidayName;
    }

    public void setHolidayName(String holidayName) {
        this.holidayName = holidayName;
    }
    
    
}
