package hrms.common;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;

public class Numtowordconvertion {

    private static String[] tensPlace = {"", "Ten", " Twenty", " Thirty", " Forty", " Fifty", " Sixty", "Seventy", "Eighty", "Ninety"};

    private static String[] unitPlace = {"",
        " One", " Two", " Three", " Four", " Five", " Six", " Seven", " Eight",
        " Nine", "Ten", "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen",
        "Sixteen", "Seventeen", "Eighteen", "Nineteen"};

    public static String convertNumber(int number) {
        String wordStr = "";
        if (number > 0) {
            String num = String.valueOf(number);
            int tenplace = 0;
            int hundredplace = 0;
            int thousandplace = 0;
            int lakhplace = 0;
            int croreplace = 0;

            if (number < 20) {
                if (number == 0) {
                    wordStr = "zero";
                } else {
                    wordStr = unitPlace[number];
                }
            } else {
                if (number < 100) {
                    if (number < 20) {
                        wordStr = wordStr + unitPlace[number];
                    } else {
                        tenplace = number / 10;
                        wordStr = wordStr + tensPlace[tenplace];
                        if ((number - tenplace * 10) > 0) {
                            wordStr = wordStr + unitPlace[(number - tenplace * 10)];
                        }
                    }
                } else if (number >= 100 && number < 1000) {
                    hundredplace = number / 100;
                    if (hundredplace > 0) {
                        wordStr = unitPlace[hundredplace] + " hundred ";
                    }
                    number = number - hundredplace * 100;
                    if (number < 20) {
                        wordStr = wordStr + unitPlace[number];
                    } else {
                        tenplace = number / 10;
                        wordStr = wordStr + tensPlace[tenplace];
                        if ((number - tenplace * 10) > 0) {
                            wordStr = wordStr + unitPlace[(number - tenplace * 10)];
                        }
                    }
                } else if (number >= 1000 && number < 100000) {
                    thousandplace = number / 1000;
                    if (thousandplace > 0) {
                        wordStr = convertNumber(thousandplace) + " thousand ";
                    }

                    number = number - thousandplace * 1000;
                    hundredplace = number / 100;
                    if (hundredplace > 0) {
                        wordStr = wordStr + unitPlace[hundredplace] + " hundred ";
                    }
                    number = number - hundredplace * 100;
                    if (number < 20) {
                        wordStr = wordStr + unitPlace[number];
                    } else {
                        tenplace = number / 10;
                        wordStr = wordStr + tensPlace[tenplace];
                        if ((number - tenplace * 10) > 0) {
                            wordStr = wordStr + unitPlace[(number - tenplace * 10)];
                        }
                    }
                } else if (number >= 100000 && number < 10000000) {
                    lakhplace = number / 100000;
                    if (lakhplace > 0) {
                        wordStr = convertNumber(lakhplace) + " lakh ";
                    }
                    number = number - lakhplace * 100000;
                    thousandplace = number / 1000;
                    if (thousandplace > 0) {
                        wordStr = wordStr + convertNumber(thousandplace) + " thousand ";
                    }
                    number = number - thousandplace * 1000;
                    hundredplace = number / 100;
                    if (hundredplace > 0) {
                        wordStr = wordStr + unitPlace[hundredplace] + " hundred ";
                    }
                    number = number - hundredplace * 100;
                    if (number < 20) {
                        wordStr = wordStr + unitPlace[number];
                    } else {
                        tenplace = number / 10;
                        wordStr = wordStr + tensPlace[tenplace];
                        if ((number - tenplace * 10) > 0) {
                            wordStr = wordStr + unitPlace[(number - tenplace * 10)];
                        }
                    }
                } else if (number > 10000000) {
                    croreplace = number / 10000000;
                    if (croreplace > 0) {
                        wordStr = convertNumber(croreplace) + " crore ";
                    }
                    number = number - croreplace * 10000000;
                    lakhplace = number / 100000;
                    if (lakhplace > 0) {
                        wordStr = wordStr + convertNumber(lakhplace) + " lakh ";
                    }
                    number = number - lakhplace * 100000;
                    thousandplace = number / 1000;
                    if (thousandplace > 0) {
                        wordStr = wordStr + convertNumber(thousandplace) + " thousand ";
                    }
                    number = number - thousandplace * 1000;
                    hundredplace = number / 100;
                    if (hundredplace > 0) {
                        wordStr = wordStr + unitPlace[hundredplace] + " hundred ";
                    }
                    number = number - hundredplace * 100;
                    if (number < 20) {
                        wordStr = wordStr + unitPlace[number];
                    } else {
                        tenplace = number / 10;
                        wordStr = wordStr + tensPlace[tenplace];
                        if ((number - tenplace * 10) > 0) {
                            wordStr = wordStr + unitPlace[(number - tenplace * 10)];
                        }
                    }

                }

            }
        }
        return wordStr;
    }

    public static String getFormattedDOB(int year, int month, int date) {
        String s1 = "";
        Calendar cl = new GregorianCalendar();
        int hundredyrs = 0;
        int yrs = 0;
        String yrstr = null;
        String[] monthNames = {"", " January ", " February ", " March ", " April ", " May ", " June ", " July ", " August ", " September ", " October ", " November ", " December "};

        String[] dateNames = {"", " First ", " Second ", " Third ", " Fourth ", " Fifth ", " Sixth ", " Seventh ", " Eighth ", " Nineth ", " Tenth ", " Eleventh ", " Tweleveth ",
            " Thirteenth ", " Fourteenth ", " Fifteenth ", " Sixteenth ", " Seventeenth ", " Eighteenth ", " Ninteenth ", " Twentyth ", " Twentyfirst ", " Twentysecond ", " Twentythird ", " Twentyfourth ", " Twentyfifth ", " Twentysixth ", " Twentyseventh ", " Twentyeighth ", " TwentyNineth ", " Thirtyth ", " Thirtyfirst "};

        if (year < 2000) {
            hundredyrs = year / 100;
            yrs = year - hundredyrs * 100;
            yrstr = convertNumber(hundredyrs) + " hundred " + convertNumber(yrs);
        } else {
            yrstr = convertNumber(year);
        }

        s1 = dateNames[date] + " " + monthNames[month] + " " + yrstr;

        return s1;

    }

    public static int[] getDateParts(String inputDate) {
        int yearmonthdate[] = new int[3];
        int i = 0;
        StringTokenizer str = new StringTokenizer(inputDate, "-");
        while (str.hasMoreElements()) {
            yearmonthdate[i] = Integer.parseInt(str.nextToken());
            i++;
        }

        return yearmonthdate;
    }
}
