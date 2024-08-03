/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.common;

import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Manas Jena
 */
public class CalendarCommonMethods {
    public static String getFullMonthAsString(int month) {
    String monthString=null;
        switch(month) 
        {
            case 0:
                monthString="JANUARY";
                break;
            case 1:
                monthString="FEBRUARY";
                break;
            case 2:
                monthString="MARCH";
                break;
            case 3:
                monthString="APRIL";
                break;
            case 4:
                monthString="MAY";
                break;
            case 5:
                monthString="JUNE";
                break;
            case 6:
                monthString="JULY";
                break;
            case 7:
                monthString="AUGUST";
                break;
            case 8:
                monthString="SEPTEMBER";
                break;
            case 9:
                monthString="OCTOBER";
                break;
            case 10:
                monthString="NOVEMBER";
                break;
            case 11:
                monthString="DECEMBER";
                break;
        }
        return monthString;
    }
    public static String getFullNameMonthAsString(int month) {
        String monthString = null;
        switch (month) {
            case 0:
                monthString = "JANUARY";
                break;
            case 1:
                monthString = "FEBRUARY";
                break;
            case 2:
                monthString = "MARCH";
                break;
            case 3:
                monthString = "APRIL";
                break;
            case 4:
                monthString = "MAY";
                break;
            case 5:
                monthString = "JUNE";
                break;
            case 6:
                monthString = "JULY";
                break;
            case 7:
                monthString = "AUGUST";
                break;
            case 8:
                monthString = "SEPTEMBER";
                break;
            case 9:
                monthString = "OCTOBER";
                break;
            case 10:
                monthString = "NOVEMBER";
                break;
            case 11:
                monthString = "DECEMBER";
                break;
        }
        return monthString;
    }
    public static String getMonthAsString(int month) {
    String monthString=null;
        switch(month) 
        {
            case 0:
                monthString="JAN";
                break;
            case 1:
                monthString="FEB";
                break;
            case 2:
                monthString="MAR";
                break;
            case 3:
                monthString="APR";
                break;
            case 4:
                monthString="MAY";
                break;
            case 5:
                monthString="JUN";
                break;
            case 6:
                monthString="JUL";
                break;
            case 7:
                monthString="AUG";
                break;
            case 8:
                monthString="SEP";
                break;
            case 9:
                monthString="OCT";
                break;
            case 10:
                monthString="NOV";
                break;
            case 11:
                monthString="DEC";
                break;
        }
        return monthString;
    }
    public static String[] populateFields(int theYear, int theMonth) {
        // initialize variables for later from user selections
        //var theMonth = form.chooseMonth.selectedIndex
        //var theYear = form.chooseYear.options[form.chooseYear.selectedIndex].text
        // initialize date-dependent variables

        // which is the first day of this month?
         
        int firstDay = getFirstDay(theYear, theMonth);
        // total number of <TD>...</TD> tags needed in for loop below
        int howMany = getMonthLen(theYear, theMonth);
       
        // set month and year in top field
        //form.oneMonth.value = theMonths[theMonth] + " " + theYear
        // fill fields of table
        String[] day = new String[42];
        for (int i = 0; i < 42; i++) {
            if (i < firstDay || i >= (howMany + firstDay)) {
                // before and after actual dates, empty fields
                // address fields by name and [index] number
                day[i] = "";
            } else {
                // enter date values
                day[i] = (i - firstDay + 1) + "";
            }
        }
        return day;
    }
            public static int getFirstDay(int theYear, int theMonth) {
        Calendar myDate=Calendar.getInstance();
        myDate.set(Calendar.YEAR,theYear);
        myDate.set(Calendar.MONTH,theMonth);
        myDate.set(Calendar.DATE,1);
        return (myDate.get(Calendar.DAY_OF_WEEK)-1);
    }
                public static int getMonthLen(int theYear, int theMonth) {
        long oneDay = 1000 * 60 * 60 * 24;
        long len = 0;
        //Calendar thisMonth=Calendar.getInstance();
        //thisMonth.set(Calendar.YEAR,theYear);
        //thisMonth.set(Calendar.MONTH,theMonth);
        //thisMonth.set(Calendar.DATE,1);
        //Calendar nextMonth=Calendar.getInstance();
        //nextMonth.set(Calendar.YEAR,theYear);
        //nextMonth.set(Calendar.MONTH,theMonth+1);
        //nextMonth.set(Calendar.DATE,1);
        Date thisMonth = new Date(theYear, theMonth, 1);
        Date nextMonth = new Date(theYear, theMonth + 1, 1);                        
        len = (nextMonth.getTime() - thisMonth.getTime()) / oneDay;             
        return new Long(len).intValue();
    }
    
    public static boolean isDatePresentInList(int date){
        /*boolean status = false;
        for(int i=0;i<holidayList.length;i++) {
            if(date==holidayList[i]) {
                status = true;
                break;
            }
        }*/
        return false;
    }

}
