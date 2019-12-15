package pl.umk.mat.mtracewicz.gui.utilityPanels;

import pl.umk.mat.mtracewicz.gui.AgendaPanel;
import pl.umk.mat.mtracewicz.utility.DateHandler;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Simple panel which allows user to select date
 */
public class DatePicker extends AgendaPanel {
    /**
     * Popup asking user to select a date
     * @return Date selected by user or null if window got closed
     */
    public static String datePopUp(){
        DatePicker datePicker = new DatePicker();
        int ok = JOptionPane.showConfirmDialog(null, datePicker, "Enter Data", JOptionPane.OK_CANCEL_OPTION);
        if (ok == JOptionPane.OK_OPTION) {
            return datePicker.getDate();
        }else {
            return null;
        }
    }
    /**
     * Constructor for DatePicker,
     * sets value to today
     */
    public DatePicker(){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd");
        int day = Integer.valueOf(sdf.format(cal.getTime()));
        sdf = new SimpleDateFormat("MM");
        int month = Integer.valueOf(sdf.format(cal.getTime()));
        sdf = new SimpleDateFormat("YYYY");
        int year = Integer.valueOf(sdf.format(cal.getTime()));
        JComboBox yearBox = new JComboBox();
        JComboBox monthBox = new JComboBox();
        JComboBox dayBox = new JComboBox();

        for(int i= -5;i <= 5;i++){
            yearBox.addItem(year + i);
        }

        for(int i = 1;i <= 12;i++){
            monthBox.addItem(i);
        }

        for(int i = 1;i <= 31;i++){
            dayBox.addItem(i);
        }


        dayBox.setSelectedIndex(day - 1);
        monthBox.setSelectedIndex(month - 1);
        yearBox.setSelectedIndex(5);

        this.setLayout(new GridLayout(1,3));
        this.add(dayBox);
        this.add(monthBox);
        this.add(yearBox);
    }

    /**
     * Method to get String representing date selected in this Datepicker
     * @return String in format dd/MM/YYYY representing selected date
     */
    public String getDate(){
        JComboBox day= (JComboBox)this.getComponent(0);
        JComboBox month = (JComboBox)this.getComponent(1);
        JComboBox year = (JComboBox)this.getComponent(2);

        int dayInt = day.getSelectedIndex() + 1;
        int monthInt = month.getSelectedIndex() + 1;
        int yearInt = Integer.valueOf(year.getSelectedItem().toString());
        if(dayInt > DateHandler.getNumberOfDaysInMonth(monthInt,yearInt)){
            dayInt = DateHandler.getNumberOfDaysInMonth(monthInt,yearInt);
            JOptionPane.showMessageDialog(null, "Day exceeds number of days in month.", "Inane error", JOptionPane.ERROR_MESSAGE);
            day.setSelectedIndex(dayInt-1);
        }
        return DateHandler.getStringOfDate(dayInt,monthInt,yearInt);
    }

    /**
     * Sets selected date in this Datepicker to day,month,year
     * @param day day which will be selected
     * @param month month which will be selected
     * @param year year which will be selected
     */
    public void setDate(int day,int month,int year){
        JComboBox dayBox = (JComboBox)this.getComponent(0);
        JComboBox monthBox = (JComboBox)this.getComponent(1);
        JComboBox yearBox = (JComboBox)this.getComponent(2);

        if(day > DateHandler.getNumberOfDaysInMonth(month,year)){
            day = DateHandler.getNumberOfDaysInMonth(month,year);
            JOptionPane.showMessageDialog(null, "Day exceeds number of days in month.", "Inane error", JOptionPane.ERROR_MESSAGE);
        }
        dayBox.setSelectedIndex(day-1);
        monthBox.setSelectedIndex(month-1);
        yearBox.setSelectedItem(year);

    }

    /**
     * Sets selected date in this Datepicker to date
     * @param date date which will be selected
     */
    public void setDate(String date){
        JComboBox dayBox = (JComboBox)this.getComponent(0);
        JComboBox monthBox = (JComboBox)this.getComponent(1);
        JComboBox yearBox = (JComboBox)this.getComponent(2);
        int day = DateHandler.getDayFromString(date);
        int month = DateHandler.getMonthFromString(date);
        int year = DateHandler.getYearFromString(date);
        if(day > DateHandler.getNumberOfDaysInMonth(month,year)){
            day = DateHandler.getNumberOfDaysInMonth(month,year);
            JOptionPane.showMessageDialog(null, "Day exceeds number of days in month.", "Inane error", JOptionPane.ERROR_MESSAGE);
        }
        dayBox.setSelectedIndex(day-1);
        monthBox.setSelectedIndex(month-1);
        yearBox.setSelectedItem(year);
    }

    /**
     * Sets selected date in this Datepicker back to today
     */
    public void resetDate(){
        JComboBox dayBox = (JComboBox)this.getComponent(0);
        JComboBox monthBox = (JComboBox)this.getComponent(1);
        JComboBox yearBox = (JComboBox)this.getComponent(2);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YYYY");
        String today = sdf.format(calendar.getTime());
        dayBox.setSelectedIndex(DateHandler.getDayFromString(today));
        monthBox.setSelectedIndex(DateHandler.getMonthFromString(today));
        yearBox.setSelectedIndex(5);
    }
}
