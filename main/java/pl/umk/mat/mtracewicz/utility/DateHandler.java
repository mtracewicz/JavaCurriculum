package pl.umk.mat.mtracewicz.utility;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateHandler {
    public static Date getDateFromString(String date){
        Calendar calendar = Calendar.getInstance();
        calendar.set(getYearFromString(date),getMonthFromString(date) - 1,getDayFromString(date));
        return calendar.getTime();
    }
    public static String getStringOfDate(int day,int month,int year){
        String date = "";
        if(day>=10 && month>=10){
            date = ""+day+"/"+month+"/"+year;
        }else if(day >= 10){
            date = ""+day+"/0"+month+"/"+year;
        }else if(month >= 10){
            date = "0"+day+"/"+month+"/"+year;
        }else{
            date = "0"+day+"/0"+month+"/"+year;
        }
        return date;
    }
    public static String getStringOfDate(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YYYY");
        String dateToReturn = sdf.format(date);
        int day = DateHandler.getDayFromString(dateToReturn);
        int month = DateHandler.getMonthFromString(dateToReturn);
        int year = DateHandler.getYearFromString(dateToReturn);
        if(day>=10 && month>=10){
            dateToReturn = ""+day+"/"+month+"/"+year;
        }else if(day >= 10){
            dateToReturn = ""+day+"/0"+month+"/"+year;
        }else if(month >= 10){
            dateToReturn = "0"+day+"/"+month+"/"+year;
        }else{
            dateToReturn = "0"+day+"/0"+month+"/"+year;
        }
        return dateToReturn;
    }
    public static int getDayFromString(String date){
        return Integer.valueOf(date.substring(0,2));
    }
    public static int getMonthFromString(String date){
        return Integer.valueOf(date.substring(3,5));
    }
    public static int getYearFromString(String date){
        return Integer.valueOf(date.substring(6));
    }
    public static int getNumberOfDaysInMonth(int month,int year){
        switch (month){
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                return 31;
            case 2:
                if(checkIfYearIsLeapYear(year)){
                    return 29;
                }else {
                    return 28;
                }
            case 4:
            case 6:
            case 9:
            case 11:
                return 30;
        }
        return -1;
    }
    public static int getFirstDayOfMonth(int month,int year){
        Calendar cal1 = Calendar.getInstance();
        cal1.set(year,month-1,1);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("u");
        return Integer.valueOf(simpleDateFormat.format(cal1.getTime()));
    }
    private static boolean checkIfYearIsLeapYear(int year){
        return (year % 4 == 0 && year % 100 != 0) || year % 400 == 0;
    }

}
