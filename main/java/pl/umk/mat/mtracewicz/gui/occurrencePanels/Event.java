package pl.umk.mat.mtracewicz.gui.occurrencePanels;

import pl.umk.mat.mtracewicz.entity.*;
import pl.umk.mat.mtracewicz.gui.Agenda;
import pl.umk.mat.mtracewicz.gui.utilityPanels.ColorPicker;
import pl.umk.mat.mtracewicz.gui.utilityPanels.DatePicker;
import pl.umk.mat.mtracewicz.gui.utilityPanels.TimePicker;
import pl.umk.mat.mtracewicz.utility.AgendaConstants;
import pl.umk.mat.mtracewicz.utility.ColorPriorityHandler;
import pl.umk.mat.mtracewicz.utility.DatabaseConnector;
import pl.umk.mat.mtracewicz.utility.DateHandler;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;

public class Event extends OccurrencePanel {
    private Events event;

    private JLabel titleLabel;
    private JTextField title;
    private JPanel calPanel;
    private JComboBox calendar;
    private JButton deleteCalendar;
    private JLabel startLabel;
    private DatePicker startDate;
    private TimePicker startTime;
    private JLabel endLabel;
    private DatePicker endDate;
    private TimePicker endTime;
    private JLabel locationLabel;
    private JTextField location;
    private JLabel noteLabel;
    private JTextArea note;
    private JLabel colorLabel;
    private ColorPicker color;
    private JButton showTasks;
    private Agenda agenda;
    private Vector<Tasks> tasksVector;


    public Events getEvent(){
        return this.event;
    }
    public Event(int day,int month,int year,int hour,Agenda agenda){
        this.agenda = agenda;
        this.event = new Events();
        this.tasksVector = new Vector<>();
        this.loadCalendars();
        this.setLayout(new GridLayout(0,1));
        this.setUpSize(this, 300, 400, AgendaConstants.MINIMUM);
        titleLabel = new JLabel("Title:");
        title = new JTextField();
        this.setUpSize(title, 300, 25, AgendaConstants.MAXIMUM);
        startLabel = new JLabel("Start date:");
        startLabel.setHorizontalAlignment(JLabel.LEFT);
        startDate = new DatePicker();
        startDate.setDate(day,month,year);
        startTime = new TimePicker(hour);
        endLabel = new JLabel("End date:");
        endLabel.setHorizontalAlignment(JLabel.LEFT);
        endDate = new DatePicker();
        endDate.setDate(day,month,year);
        endTime = new TimePicker(hour+1);
        locationLabel= new JLabel("Location:");
        locationLabel.setHorizontalAlignment(JLabel.LEFT);
        location = new JTextField();
        this.setUpSize(location, 300, 25, AgendaConstants.MAXIMUM);
        noteLabel = new JLabel("Note:");
        noteLabel.setHorizontalAlignment(JLabel.LEFT);
        note= new JTextArea();
        colorLabel= new JLabel("Color:");
        colorLabel.setHorizontalAlignment(JLabel.LEFT);
        color = ColorPicker.createColorPicker(5);
        this.setUpSize(color, 300, 50, AgendaConstants.MINIMUM);
        showTasks = new JButton("Show tasks");
        showTasks.addActionListener(e -> this.tasksPopUp());

        this.add(titleLabel);
        this.add(title);
        this.add(calPanel);
        this.add(startLabel);
        this.add(startDate);
        this.add(startTime);
        this.add(endLabel);
        this.add(endDate);
        this.add(endTime);
        this.add(locationLabel);
        this.add(location);
        this.add(noteLabel);
        this.add(note);
        this.add(colorLabel);
        this.add(color);
        this.add(showTasks);
    }
    public Event(Events events, Agenda agenda){
        this.event = events;
        this.agenda = agenda;
        this.tasksVector = new Vector<>();
        this.loadCalendars();
        this.setLayout(new GridLayout(0,1));
        this.setUpSize(this, 300, 400, AgendaConstants.MINIMUM);
        titleLabel = new JLabel("Title:");
        title = new JTextField();
        title.setText(events.getName());
        this.setUpSize(title, 300, 25, AgendaConstants.MAXIMUM);
        startLabel = new JLabel("Start date:");
        startLabel.setHorizontalAlignment(JLabel.LEFT);
        startDate = new DatePicker();
        Date start = events.getStart();
        startDate.setDate(DateHandler.getStringOfDate(start));
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String startHour = sdf.format(start);
        startTime = new TimePicker(Integer.valueOf(startHour.substring(0,2)),Integer.valueOf(startHour.substring(3)));
        endLabel = new JLabel("End date:");
        endLabel.setHorizontalAlignment(JLabel.LEFT);
        endDate = new DatePicker();
        Date end = events.getEnd();
        endDate.setDate(DateHandler.getStringOfDate(end));
        String endHour = sdf.format(end);
        TimePicker endTime = new TimePicker(Integer.valueOf(endHour.substring(0,2)),Integer.valueOf(endHour.substring(3)));
        locationLabel = new JLabel("Location:");
        locationLabel.setHorizontalAlignment(JLabel.LEFT);
        location = new JTextField();
        location.setText(events.getLocalization());
        this.setUpSize(location, 300, 25, AgendaConstants.MAXIMUM);
        noteLabel = new JLabel("Note:");
        noteLabel.setHorizontalAlignment(JLabel.LEFT);
        note = new JTextArea();
        if(events.getNotes_id() != null) {
            note.setText(events.getNotes_id().getText());
        }
        colorLabel = new JLabel("Color:");
        colorLabel.setHorizontalAlignment(JLabel.LEFT);
        color = ColorPicker.createColorPicker(5);
        color.setColor(events.getColor());
        this.setUpSize(color, 300, 50, AgendaConstants.MINIMUM);
        showTasks = new JButton("Show tasks");


        this.add(titleLabel);
        this.add(title);
        this.add(calPanel);
        this.add(startLabel);
        this.add(startDate);
        this.add(startTime);
        this.add(endLabel);
        this.add(endDate);
        this.add(endTime);
        this.add(locationLabel);
        this.add(location);
        this.add(noteLabel);
        this.add(note);
        this.add(colorLabel);
        this.add(color);
        this.add(showTasks);
    }
    private void loadCalendars(){
        this.calPanel = new JPanel(new GridLayout(1,2));
        this.calendar = new JComboBox();
        calendar.addActionListener(e ->{
            if(calendar.getSelectedItem().toString().equals("Add new calendar +")){
                Items cal = new Items();
                String name = this.stringPopUp("New Calendar name");
                String color = ColorPicker.getColorName(ColorPicker.colorPopUp());
                cal.setName(name);
                cal.setType("calendar");
                cal.setColor(color);
                Users user = new Users();
                user.setEmail(agenda.getUsername());
                cal.setEmail(user);
                agenda.connector.addItem(cal,user);
                calendar.addItem(name);
                calendar.removeItem("Add new calendar +");
                calendar.addItem("Add new calendar +");
                calendar.setSelectedItem(name);
            }
        });
        if(agenda.connector.getNumberOfItems(agenda.getUsername(),"calendar") > 0) {
            List calendars = agenda.connector.getItems(agenda.getUsername(),"calendar");
            for(Object o:calendars){
                calendar.addItem(((Items)o).getName());
            }
        }
        calendar.addItem("Add new calendar +");
        this.deleteCalendar = new JButton("Delete calendar");
        this.deleteCalendar.addActionListener(e ->{
            new Thread( () -> {
                String currentItem = calendar.getSelectedItem().toString();
                System.out.println(currentItem);
                agenda.connector.deleteItem(agenda.connector.getItem(
                        agenda.getUsername(), "calendar", currentItem));
                calendar.removeItem(currentItem);
            }).start();
        });
        calPanel.add(calendar);
        calPanel.add(deleteCalendar);
    }
    private void newTaskPopUp(JPanel parentPanel){
        Tasks tasks = new Tasks();
        JPanel panel = new JPanel(new GridLayout(2,4));
        JButton newItem = new JButton();
        JTextField name = new JTextField();
        DatePicker date = new DatePicker();
        ColorPicker priority = ColorPicker.createColorPicker(4,Color.RED);
        newItem.setText("+");
        newItem.addActionListener(e -> {
            tasks.setEvents_id(event);
            boolean allFiled = true;
            if(!name.getText().isEmpty()) {
                tasks.setName(name.getText());
                name.setText(null);
            }else {
                JOptionPane.showMessageDialog(null, "Please fill in name", "Inane error", JOptionPane.ERROR_MESSAGE);
                allFiled = false;
            }
            if(!date.getDate().isEmpty()) {
                tasks.setDeadline(DateHandler.getDateFromString(date.getDate()));
                date.resetDate();
            }
            tasks.setPriority(ColorPriorityHandler.getPriority(priority.getColor()));
            tasks.setStatus(0);
        });
        panel.add(new JLabel("Add new"));
        panel.add(new JLabel("Name:"));
        panel.add(new JLabel("Date:"));
        panel.add(new JLabel("Priority:"));
        panel.add(newItem);
        panel.add(name);
        panel.add(date);
        panel.add(priority);
        JOptionPane.showMessageDialog(null,panel);
        parentPanel.add(new Task(tasks,agenda.connector));
        tasksVector.add(tasks);
    }
    private void tasksPopUp(){
        JPanel panel = new JPanel(new BorderLayout());
        JPanel tasksPanel = new JPanel(new GridLayout(0,1));
        JButton newTask = new JButton("New task +");
        boolean add = true;
        panel.add(tasksPanel,BorderLayout.CENTER);
        panel.add(newTask,BorderLayout.SOUTH);
        if(agenda.connector.getNumberOfTaks(event) > 0) {
            for (Tasks t : agenda.connector.getTasks(event.getItem_id().getName(), event)) {
                Task task = new Task(t, agenda.connector);
                tasksPanel.add(task);
            }
        }else {
            tasksPanel.add(new JLabel("You have no tasks yet"));
            add = true;
        }
        tasksPanel.add(newTask);
        boolean finalAdd = add;
        newTask.addActionListener(e -> {
            if(!finalAdd) {
                tasksPanel.remove(0);
            }
            newTaskPopUp(tasksPanel);
        });
        JOptionPane.showMessageDialog(this,tasksPanel);
    }
    public void saveEvent(){
        event.setName(title.getText());
        Calendar calInst = Calendar.getInstance();
        String date = startDate.getDate();
        String time = startTime.getTime();
        int hour,minutes;
        if(time.substring(0,2).equals("00")) {
            hour = 0;
        }else{
            hour = Integer.valueOf(time.substring(0,2));
        }
        if(time.substring(3).equals("00")) {
            minutes = 0;
        }else {
            minutes = Integer.valueOf(time.substring(3));
        }
        calInst.set(DateHandler.getYearFromString(date),DateHandler.getMonthFromString(date) - 1
                ,DateHandler.getDayFromString(date),hour,minutes,0);
        event.setStart(calInst.getTime());
        date = endDate.getDate();
//        time = endTime.getTime();
        calInst.set(DateHandler.getYearFromString(date),DateHandler.getMonthFromString(date) - 1
                ,DateHandler.getDayFromString(date),Integer.valueOf(time.substring(0,2)),Integer.valueOf(time.substring(3)),0);
        event.setEnd(calInst.getTime());
        event.setColor(ColorPicker.getColorName(color.getColor()));
        event.setLocalization(location.getText());
        Notes notes = new Notes();
        if(!note.getText().isEmpty()) {
            notes.setText(note.getText());
            event.setNotes_id(notes);
            agenda.connector.addNote(notes);
        }
        String calName = calendar.getSelectedItem().toString();
        Items cal = null;
        if(calName.equals("Add new calendar +")){
            JOptionPane.showMessageDialog(null,"Please select a calendar");
        }else {
            cal = agenda.connector.getItem(agenda.getUsername(), "calendar",calName );
        }
        if(cal != null) {
            event.setItem_id(cal);
        }
        agenda.connector.saveEvent(event);
        for(Tasks t: tasksVector) {
            agenda.connector.addOrEditTask(t);
        }
    }
    public void deleteEvent(){
        agenda.connector.deleteEvent(event);
    }
}
