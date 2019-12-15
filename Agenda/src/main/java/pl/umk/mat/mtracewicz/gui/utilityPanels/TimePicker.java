package pl.umk.mat.mtracewicz.gui.utilityPanels;

import pl.umk.mat.mtracewicz.gui.AgendaPanel;

import javax.swing.*;
import java.awt.*;

/**
 * Simple panel which contains two checkboxes, which allow to select hour and minutes
 */
public class TimePicker extends AgendaPanel {
    /**
     * Combo box which allows to chose hour of day
     */
    private JComboBox hours;
    /**
     * Combo box which allows to chose minutes of hour
     */
    private JComboBox minutes;

    /**
     * Method to get time selected by time picker
     * @return String in format hh:mm representing time selected in this time picker
     */
    public String getTime(){
        return this.hours.getSelectedItem()+":"+this.minutes.getSelectedItem();
    }

    /**
     * Constructor which sets up starting value of hour to provided argument
     * and minutes to 00
     * @param hour Hour which time picker will show at start
     */
    public TimePicker(int hour){
        this.setLayout(new GridLayout(1,2));
        hours = new JComboBox();
        minutes = new JComboBox();
        for(int i = 0;i < 24;i++){
            if(i<10){
                hours.addItem("0"+i);
            }else {
                hours.addItem(i);
            }
        }
        for(int i = 0;i < 60;i++){
            if(i<10){
                minutes.addItem("0"+i);
            }else {
                minutes.addItem(i);
            }
        }
        hours.setSelectedIndex(hour);
        this.add(hours);
        this.add(minutes);
    }

    /**
     * Constructor which sets up starting value of hour to first provided argument
     * and minutes to second provided argument
     * @param hour Hour which time picker will show at start
     * @param minute Minute which time picker will show at start
     */
    public TimePicker(int hour,int minute){
        this.setLayout(new GridLayout(1,2));
        hours = new JComboBox();
        minutes = new JComboBox();
        for(int i = 0;i < 24;i++){
            if(i<10){
                hours.addItem("0"+i);
            }else {
                hours.addItem(i);
            }
        }
        for(int i = 0;i < 60;i++){
            if(i<10){
                minutes.addItem("0"+i);
            }else {
                minutes.addItem(i);
            }
        }
        hours.setSelectedIndex(hour);
        minutes.setSelectedIndex(minute);
        this.add(hours);
        this.add(minutes);
    }
}
