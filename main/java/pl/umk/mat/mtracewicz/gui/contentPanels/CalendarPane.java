package pl.umk.mat.mtracewicz.gui.contentPanels;
import pl.umk.mat.mtracewicz.entity.Events;
import pl.umk.mat.mtracewicz.entity.Users;
import pl.umk.mat.mtracewicz.gui.Agenda;
import pl.umk.mat.mtracewicz.gui.occurrencePanels.Event;
import pl.umk.mat.mtracewicz.gui.utilityPanels.ColorPicker;
import pl.umk.mat.mtracewicz.gui.utilityPanels.DatePicker;
import pl.umk.mat.mtracewicz.utility.AgendaConstants;
import pl.umk.mat.mtracewicz.utility.ColorPriorityHandler;
import pl.umk.mat.mtracewicz.utility.DateHandler;

import javax.swing.*;
import javax.swing.text.View;
import java.awt.*;
import java.awt.List;
import java.text.SimpleDateFormat;
import java.util.*;

public class CalendarPane extends ContentPanel {
    private JPanel header;
    private Agenda agenda;
    private view currentView;
    private int today;
    private int currentMonth;
    private int currentYear;
    private String currentDate;
    private java.util.List events;
    private enum view {
        DAY,WEEK,MONTH
    }

    public CalendarPane(Agenda agenda) {
        super();
        this.currentView = view.MONTH;
        this.agenda = agenda;

        events = agenda.connector.getEvents(new Users(agenda.getUsername()));
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dayGetter = new SimpleDateFormat("dd");
        SimpleDateFormat monthGetter = new SimpleDateFormat("MM");
        SimpleDateFormat yearGetter = new SimpleDateFormat("YYYY");
        this.today = Integer.valueOf(dayGetter.format(calendar.getTime()));
        this.currentMonth = Integer.valueOf(monthGetter.format(calendar.getTime()));
        this.currentYear = Integer.valueOf(yearGetter.format(calendar.getTime()));

        this.setUpHeader();
        this.setUpFooter();
        this.setUpDayView(this.today,this.currentMonth,this.currentYear);
        this.setUpWeekView(this.today,this.currentMonth,this.currentYear);
        this.setUpMonthView(this.today,this.currentMonth,this.currentYear);
    }

    @Override
    public void resetPanel(){
        DatePicker dp = (DatePicker)this.footer.getComponent(1);
        dp.setDate(today,currentMonth,currentYear);
        this.setUpMonthView(today,currentMonth,currentYear);
        this.showPanel("moth");
    }
    private void setUpHeader(){
        header = new JPanel(new GridLayout(2,3));
        JButton dayView = new JButton("Day");
        dayView.addActionListener(e ->{
            this.setUpDayView(DateHandler.getDayFromString(currentDate),
                    DateHandler.getMonthFromString(currentDate),DateHandler.getYearFromString(currentDate));
            this.showPanel("day");
            this.currentView = view.DAY;
        });
        JButton weekView = new JButton("Week");
        weekView.addActionListener(e ->{
            this.setUpWeekView(DateHandler.getDayFromString(currentDate),
                    DateHandler.getMonthFromString(currentDate),DateHandler.getYearFromString(currentDate));
            this.showPanel("week");
            this.currentView = view.WEEK;
        });
        JButton monthView = new JButton("Month");
        monthView.addActionListener(e ->{
            this.setUpMonthView(DateHandler.getDayFromString(currentDate),
                    DateHandler.getMonthFromString(currentDate),DateHandler.getYearFromString(currentDate));
            this.showPanel("month");
            this.currentView = view.MONTH;
        });
        JLabel view = new JLabel("View:");
        view.setHorizontalAlignment(SwingUtilities.CENTER);

        header.add(new JPanel());
        header.add(view);
        header.add(new JPanel());
        header.add(dayView);
        header.add(weekView);
        header.add(monthView);

        this.add(header, BorderLayout.NORTH);
    }
    private void setUpFooter(){
        this.footer.setLayout(new GridLayout(1,3));

        DatePicker datePicker = new DatePicker();
        JComboBox box1 = (JComboBox)datePicker.getComponent(0);
        JComboBox box2 = (JComboBox)datePicker.getComponent(1);
        JComboBox box3 = (JComboBox)datePicker.getComponent(2);

        box1.addActionListener( e -> {
            this.currentView = view.DAY;
            dataPickerReaction(datePicker);
        });
        box2.addActionListener( e -> dataPickerReaction(datePicker));
        box3.addActionListener( e -> dataPickerReaction(datePicker));

        JButton back = new JButton("<-");
        back.addActionListener(e ->{
            int d = DateHandler.getDayFromString(this.currentDate);
            int m = DateHandler.getMonthFromString(this.currentDate);
            int y = DateHandler.getYearFromString(this.currentDate);
            Calendar calendar = Calendar.getInstance();
            calendar.set(y,m-1,d);

            if(currentView == view.MONTH) {
                calendar.add(Calendar.MONTH,-1);
            }else if(currentView == view.DAY){
                calendar.add(Calendar.DAY_OF_MONTH,-1);
            }else if(currentView == view.WEEK){
                calendar.add(Calendar.DAY_OF_MONTH,-7);
            }

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/YYYY");
            String tmp = simpleDateFormat.format(calendar.getTime());
            d = DateHandler.getDayFromString(tmp);
            m = DateHandler.getMonthFromString(tmp);
            y = DateHandler.getYearFromString(tmp);

            datePicker.setDate(d,m,y);
            if(currentView == view.MONTH) {
                this.setUpMonthView(d,m,y);
                this.showPanel("month");
            }else if(currentView == view.DAY){
                this.setUpDayView(d,m,y);
                this.showPanel("day");
            }else if(currentView == view.WEEK){
                this.setUpWeekView(d,m,y);
                this.showPanel("week");
            }
        });

        JButton forth = new JButton("->");
        forth.addActionListener(e ->{
            int d = DateHandler.getDayFromString(this.currentDate);
            int m = DateHandler.getMonthFromString(this.currentDate);
            int y = DateHandler.getYearFromString(this.currentDate);
            Calendar calendar = Calendar.getInstance();
            calendar.set(y,m-1,d);

            if(currentView == view.MONTH) {
                calendar.add(Calendar.MONTH,1);
            }else if(currentView == view.DAY){
                calendar.add(Calendar.DAY_OF_MONTH,1);
            }else if(currentView == view.WEEK){
                calendar.add(Calendar.DAY_OF_MONTH,7);
            }

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/YYYY");
            String tmp = simpleDateFormat.format(calendar.getTime());
            d = DateHandler.getDayFromString(tmp);
            m = DateHandler.getMonthFromString(tmp);
            y = DateHandler.getYearFromString(tmp);

            datePicker.setDate(d,m,y);
            if(currentView == view.MONTH) {
                this.setUpMonthView(d,m,y);
                this.showPanel("month");
            }else if(currentView == view.DAY){
                this.setUpDayView(d,m,y);
                this.showPanel("day");
            }else if(currentView == view.WEEK){
                this.setUpWeekView(d,m,y);
                this.showPanel("week");
            }
            this.updateComponent(window);
        });
        this.footer.add(back);
        this.footer.add(datePicker);
        this.footer.add(forth);
    }
    private void dataPickerReaction(DatePicker datePicker) {
        int d = DateHandler.getDayFromString(datePicker.getDate());
        int m = DateHandler.getMonthFromString(datePicker.getDate());
        int y = DateHandler.getYearFromString(datePicker.getDate());
        if (currentView == view.MONTH) {
            this.setUpMonthView(d, m, y);
            this.showPanel("month");
        } else if (currentView == view.DAY) {
            this.setUpDayView(d, m, y);
            this.showPanel("day");
        } else if (currentView == view.WEEK) {
            this.setUpWeekView(d, m, y);
            this.showPanel("week");
        }
    }
    private Events eventPopUp(int day,int month,int year,int hour){
        Event eventForm = new Event(day,month,year,hour,agenda);
        Object[] options = {"Save","Delete","Cancel"};
        int ok  = JOptionPane.showOptionDialog(null,eventForm,
                "Event",JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,null,options,options[2]);
        if(ok == JOptionPane.OK_OPTION) {
            eventForm.saveEvent();
            return eventForm.getEvent();
        }else if(ok == JOptionPane.NO_OPTION) {
            events.remove(eventForm.getEvent());
            eventForm.deleteEvent();
            return null;
        }else {
            return null;
        }
    }
    private Events eventPopUp(Events event){
        Event eventForm = new Event(event,agenda);
        Object[] options = {"Save","Delete","Cancel"};
        int ok  = JOptionPane.showOptionDialog(null,eventForm,
                "Event",JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,null,options,options[2]);
        if(ok == JOptionPane.OK_OPTION) {
            eventForm.saveEvent();
            return eventForm.getEvent();
        }else if(ok == JOptionPane.NO_OPTION) {
            events.remove(eventForm.getEvent());
            eventForm.deleteEvent();
            return null;
        }else {
            return null;
        }
    }
    private JPanel createDayView(int day,int month,int year){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E");
        Calendar calendar = Calendar.getInstance();
        calendar.set(year,month-1,day);

        if(day>=10 && month>=10){
            this.currentDate = ""+day+"/"+month+"/"+year;
        }else if(day >= 10){
            this.currentDate = ""+day+"/0"+month+"/"+year;
        }else if(month >= 10){
            this.currentDate = "0"+day+"/"+month+"/"+year;
        }else{
            this.currentDate = "0"+day+"/0"+month+"/"+year;
        }
        JButton date = new JButton(simpleDateFormat.format(calendar.getTime())+" "+this.currentDate);
        date.addActionListener(e -> {
            DatePicker dp = (DatePicker)footer.getComponent(1);
            String dayString = date.getText().substring(date.getText().length()-10);
            dp.setDate(DateHandler.getDayFromString(dayString),
                    DateHandler.getMonthFromString(dayString),DateHandler.getYearFromString(dayString));
            this.setUpDayView(DateHandler.getDayFromString(dayString),
                DateHandler.getMonthFromString(dayString),DateHandler.getYearFromString(dayString));
        });
        JPanel upperPanel = new JPanel(new GridLayout(1,1));
        upperPanel.add(date);
        this.setUpSize(upperPanel,140,25, AgendaConstants.ALL);
        JPanel dayPane = new JPanel(new GridLayout(0,1));

        for(int i = 0;i < 24;i++){
            JButton button = new JButton();
            if(i >= 10) {
                button.setText("" + i+":00");
            }else {
                button.setText("0"+i+":00");
            }
            button.setHorizontalAlignment(JButton.LEFT);
            calendar.set(year,month-1,day,i,0,0);
            Calendar calendar1 = Calendar.getInstance();
            if(i+1 < 24) {
                calendar1.set(year, month - 1, day, i + 1, 0,0);
            }else {
                calendar1.set(year, month - 1, day+1, 0, 0,0);
            }
            Events event = getEvent(calendar,calendar1);
            if( event != null){
                button.setBackground(ColorPicker.getColor(event.getColor()));
                button.setText(button.getText()+"  "+event.getName());
            }else {
                button.setBackground(Color.white);
            }
            button.addActionListener(e ->{
                Events events1;
                if( event != null){
                     events1 = this.eventPopUp(event);
                }else {
                     events1 = this.eventPopUp(day,month,year,Integer.valueOf(button.getText().substring(0,2)));
                }
                if(events1 != null){
                    if(!this.contains(events1)) {
                        events.add(events1);
                    }
                }
                if(currentView == view.DAY) {
                    this.setUpDayView(day,month,year);
                }else{
                    this.setUpWeekView(day,month,year);
                }
            });
            dayPane.add(button);
        }

        JPanel dayView = new JPanel();
        dayView.setLayout(new BoxLayout(dayView,BoxLayout.Y_AXIS));
        dayView.add(upperPanel);
        dayView.add(dayPane);

        dayView.setBorder(BorderFactory.createMatteBorder(2,2,2,2,Color.GRAY));
        return dayView;
    }
    private void setUpDayView(int day,int month,int year){
        this.window.add("day",this.createDayView(day,month,year));
        this.showPanel("day");
        this.updateComponent(window);
    }
    private void setUpWeekView(int day,int month,int year){
        JPanel weekView = new JPanel(new GridLayout(1,7));
        SimpleDateFormat sdf = new SimpleDateFormat("u");
        Calendar calendar = Calendar.getInstance();
        calendar.set(year,month-1,day);

        int todayDay = Integer.valueOf(sdf.format(calendar.getTime()));
        for(int i = todayDay - 1;i >= 1;i--){
            weekView.add(this.createDayView(day - i,month,year));
        }
        for(int i = 0;todayDay+i <= 7;i++){
            weekView.add(this.createDayView(day + i,month,year));
        }
        if(day>=10 && month>=10){
            this.currentDate = ""+day+"/"+month+"/"+year;
        }else if(day >= 10){
            this.currentDate = ""+day+"/0"+month+"/"+year;
        }else if(month >= 10){
            this.currentDate = "0"+day+"/"+month+"/"+year;
        }else{
            this.currentDate = "0"+day+"/0"+month+"/"+year;
        }
        JScrollPane scrollPane = new JScrollPane(weekView);
        this.window.add("week",scrollPane);
        this.showPanel("week");
        this.updateComponent(weekView);
    }
    private void setUpMonthView(int day, int month,int year){
        Calendar calendar = Calendar.getInstance();
        calendar.set(year,month - 1,day);
        SimpleDateFormat helper = new SimpleDateFormat("MM/YYYY");
        if(this.currentDate != null &&!this.currentDate.equals(helper.format(calendar.getTime()))){
            if(window.getComponentCount() > 0 ) {
                for (Component c : window.getComponents()) {
                    if (c.getName() != null && c.getName().equals("month")) {
                        window.remove(c);
                        break;
                    }
                }
            }
        }
        if(day>=10 && month>=10){
            this.currentDate = ""+day+"/"+month+"/"+year;
        }else if(day >= 10){
            this.currentDate = ""+day+"/0"+month+"/"+year;
        }else if(month >= 10){
            this.currentDate = "0"+day+"/"+month+"/"+year;
        }else{
            this.currentDate = "0"+day+"/0"+month+"/"+year;
        }
        JPanel monthView = new JPanel(new GridLayout(7,7));
        this.fillInMonthPane(monthView,month,year);

        JLabel monthOfYear = new JLabel(month+"/"+year);
        monthOfYear.setHorizontalAlignment(SwingUtilities.CENTER);

        JPanel holder = new JPanel(new BorderLayout());
        holder.add(monthOfYear,BorderLayout.NORTH);
        holder.add(monthView,BorderLayout.CENTER);
        this.window.add("month",holder);
        this.showPanel("month");
        this.updateComponent(holder);
        holder.setName("month");
    }
    private Events getEvent(Calendar calendar,Calendar calendar1){

        for(Object e:events){
            Events event = (Events)e;
            if((event.getStart().equals(calendar.getTime()) ||
                    calendar.getTime().before(event.getStart()) )
                    && (event.getStart().before(calendar1.getTime()))){
                return event;
            }
        }
        return null;
    }
    private void fillInMonthPane(JPanel panel,int month,int year){
        int numberOfDaysInMonth = DateHandler.getNumberOfDaysInMonth(month,year);
        int firstDay = DateHandler.getFirstDayOfMonth(month,year);
        int emptyNumber, created = 0;
        JLabel monday = new JLabel("Monday");
        monday.setHorizontalAlignment(SwingUtilities.CENTER);
        monday.setBorder(BorderFactory.createMatteBorder(4,2,2,2,Color.DARK_GRAY));
        JLabel tuesday = new JLabel("Tuesday");
        tuesday.setHorizontalAlignment(SwingUtilities.CENTER);
        tuesday.setBorder(BorderFactory.createMatteBorder(4,2,2,2,Color.DARK_GRAY));
        JLabel wednesday = new JLabel("Wednesday");
        wednesday.setHorizontalAlignment(SwingUtilities.CENTER);
        wednesday.setBorder(BorderFactory.createMatteBorder(4,2,2,2,Color.DARK_GRAY));
        JLabel thursday = new JLabel("Thursday");
        thursday.setHorizontalAlignment(SwingUtilities.CENTER);
        thursday.setBorder(BorderFactory.createMatteBorder(4,2,2,2,Color.DARK_GRAY));
        JLabel friday = new JLabel("Friday");
        friday.setHorizontalAlignment(SwingUtilities.CENTER);
        friday.setBorder(BorderFactory.createMatteBorder(4,2,2,2,Color.DARK_GRAY));
        JLabel saturday = new JLabel("Saturday");
        saturday.setHorizontalAlignment(SwingUtilities.CENTER);
        saturday.setBorder(BorderFactory.createMatteBorder(4,2,2,2,Color.DARK_GRAY));
        JLabel sunday = new JLabel("Sunday");
        sunday.setHorizontalAlignment(SwingUtilities.CENTER);
        sunday.setBorder(BorderFactory.createMatteBorder(4,2,2,4,Color.DARK_GRAY));
        panel.add(monday);
        panel.add(tuesday);
        panel.add(wednesday);
        panel.add(thursday);
        panel.add(friday);
        panel.add(saturday);
        panel.add(sunday);

        emptyNumber = firstDay - 1;
        for(int i = 0; i < emptyNumber;i++){
            JPanel pane = new JPanel();
            pane.setBackground(Color.GRAY);
            pane.setBorder(BorderFactory.createMatteBorder(2,2,2,2,Color.DARK_GRAY));
            panel.add(pane);
            created++;
        }
        for(int i = 1; i <= numberOfDaysInMonth;i++){
            JButton button = new JButton(""+i);
            Events event = agenda.connector.isThereAnEvent(i,month,year,new Users(agenda.getUsername()));
            if( event != null){
                button.setBackground(ColorPicker.getColor(event.getColor()));
                this.updateComponent(button);
            }
            button.setName(""+i);
            button.addActionListener(e -> {
                this.setUpDayView(Integer.valueOf(button.getName()),month,year);
                this.currentView = view.DAY;
                this.showPanel("day");
            });
            button.setBorder(BorderFactory.createMatteBorder(2,2,2,2,Color.DARK_GRAY));
            panel.add(button);
            created++;
        }
        for(int i = created;i<42;i++){
            JPanel pane = new JPanel();
            pane.setBackground(Color.GRAY);
            pane.setBorder(BorderFactory.createMatteBorder(2,2,2,2,Color.DARK_GRAY));
            panel.add(pane);
        }
    }
    private boolean contains(Events e){
        for(Object o:events){
            if(e.equals((Events)o)){
                return true;
            }
        }
        return false;
    }
}