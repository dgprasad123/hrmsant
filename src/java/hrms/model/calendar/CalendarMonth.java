/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.calendar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Manas
 */
public class CalendarMonth {

    private int month;
    private String monthName;
    private int year;
    private CalendarDay days[][];
    private List holidaysBean;
    private int numberOfWeeks;
    private final String monthNames[] = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

    public CalendarMonth() {
    }

    public CalendarMonth(int month, int year, List holidayList) {
        days = new CalendarDay[6][7];
        numberOfWeeks = 0;
        this.month = month;
        this.year = year;
        this.holidaysBean = holidayList;
        buildWeeks();
    }

    private void buildWeeks() {
        Calendar c = Calendar.getInstance();
        c.setFirstDayOfWeek(1);
        c.set(year, month, 1);
        int nthSaturDay = 0;
        for (; c.get(2) == month; c.add(5, 1)) {
            int weekNumber = c.get(4) - 1;
            int dayOfWeek = calculateDay(c.get(7));
            CalendarDay calday = new CalendarDay();
            calday.setCaldate(c.get(5));
            calday.setCalmonth(month);
            calday.setCalyear(year);

            if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
                nthSaturDay++;
            }
            if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY || (c.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY && nthSaturDay == 2)) {
                calday.setIsHoliday(true);
            } else {
                calday.setIsHoliday(false);
            }
            HolidayBean thbean = checkIsHoliday(calday);
            if (thbean != null) {
                calday.setIsHoliday(true);
                calday.setHolidayName(thbean.getHolidayName());
            }
            days[weekNumber][dayOfWeek] = calday;
            numberOfWeeks = weekNumber;
        }
    }

    public HolidayBean checkIsHoliday(CalendarDay calday) {
        HolidayBean holidayBean = null;
        Calendar c = Calendar.getInstance();        
        c.set(calday.getCalyear(), calday.getCalmonth(), calday.getCaldate());
        try {
            for (int i = 0; i < this.holidaysBean.size(); i++) {
                HolidayBean hbean = (HolidayBean) this.holidaysBean.get(i);

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date currdate = c.getTime();
                String tcurrdate = dateFormat.format(currdate);
                currdate = sdf.parse(tcurrdate);
                int k = currdate.compareTo(hbean.getFdate());
                int j = hbean.getTdate().compareTo(currdate);                
                if (hbean.getFdate().compareTo(currdate) * currdate.compareTo(hbean.getTdate()) >= 0) {
                    holidayBean = new HolidayBean();
                    holidayBean.setHolidayName(hbean.getHolidayName());
                }

            }
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return holidayBean;
    }

    public List getHolidaysBean() {
        return holidaysBean;
    }

    public void setHolidaysBean(List holidaysBean) {
        this.holidaysBean = holidaysBean;
    }

    public int getNumberOfWeeks() {
        return numberOfWeeks;
    }

    public String getMonthName() {
        return monthNames[this.month];
    }

    public CalendarDay[][] getDays() {
        return days;
    }

    public void setDays(CalendarDay[][] days) {
        this.days = days;
    }

    public int calculateDay(int day) {
        if (day == 1) {
            return 0;
        }
        if (day == 2) {
            return 1;
        }
        if (day == 3) {
            return 2;
        }
        if (day == 4) {
            return 3;
        }
        if (day == 5) {
            return 4;
        }
        if (day == 6) {
            return 5;
        }
        return day != 7 ? 7 : 6;
    }

}
