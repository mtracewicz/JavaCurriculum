package pl.umk.mat.mtracewicz.gui.utilityPanels;

import pl.umk.mat.mtracewicz.gui.AgendaPanel;

import javax.swing.*;
import java.awt.*;

public class TimePicker extends AgendaPanel {
    JComboBox hours;
    JComboBox minutes;
    public String getTime(){
        return this.hours.getSelectedItem()+":"+this.minutes.getSelectedItem();
    }
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
