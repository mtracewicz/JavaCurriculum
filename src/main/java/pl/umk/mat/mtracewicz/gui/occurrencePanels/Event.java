package pl.umk.mat.mtracewicz.gui.occurrencePanels;

import pl.umk.mat.mtracewicz.entity.*;
import pl.umk.mat.mtracewicz.gui.Agenda;
import pl.umk.mat.mtracewicz.gui.AgendaPanel;
import pl.umk.mat.mtracewicz.gui.utilityPanels.ColorPicker;
import pl.umk.mat.mtracewicz.gui.utilityPanels.DatePicker;
import pl.umk.mat.mtracewicz.gui.utilityPanels.TimePicker;
import pl.umk.mat.mtracewicz.utility.AgendaConstants;
import pl.umk.mat.mtracewicz.utility.ColorPriorityHandler;
import pl.umk.mat.mtracewicz.utility.DateHandler;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;

/**
 * Class which represents panel corresponding to Event in database
 */
public class Event extends AgendaPanel {
    /**
     *  Object corresponding to event in database
     */
    private Events event;

    /**
     * Label "title"
     */
    private JLabel titleLabel;
    /**
     *  Text filed to input title
     */
    private JTextField title;
    /**
     * Panel which allows to select and delete calendars
     */
    private JPanel calPanel;
    /**
     *  Combobox allowing to select calendars
     */
    private JComboBox calendar;
    /**
     *  Label "start"
     */
    private JLabel startLabel;
    /**
     *  Date picker for start date of event
     */
    private DatePicker startDate;
    /**
     *  Time picker for start date of event
     */
    private TimePicker startTime;
    /**
     *  Label "end"
     */
    private JLabel endLabel;
    /**
     * Date picker for end date of event
     */
    private DatePicker endDate;
    /**
     * Time picker for start date of event
     */
    private TimePicker endTime;
    /**
     *  Label "Location"
     */
    private JLabel locationLabel;
    /**
     *  Text field to insert location of event
     */
    private JTextField location;
    /**
     *  Label "note"
     */
    private JLabel noteLabel;
    /**
     *  Text area for note
     */
    private JTextArea note;
    /**
     *  Label "color"
     */
    private JLabel colorLabel;
    /**
     *  Color picker which allows to select color for event
     */
    private ColorPicker color;
    /**
     *  Button which allows to show tasks related to event
     */
    private JButton showTasks;
    /**
     *  Reference to main frame
     */
    private Agenda agenda;
    /**
     *  Vector of tasks connected to event
     */
    private Vector<Tasks> tasksVector;


    /**
     * Method which returns events object correlated with this panel
     * @return event object
     */
    public Events getEvent(){
        return this.event;
    }

    /**
     * Constructor for new events
     * @param day Day which event takes place on
     * @param month Month which event takes place on
     * @param year Year which event takes place on
     * @param hour Hour which event takes place on
     * @param agenda Reference to main frame
     */
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

    /**
     * Constructor which creates object based on events object
     * @param events Events object to represent by this panel
     * @param agenda Reference to main frame
     */
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
        endTime = new TimePicker(Integer.valueOf(endHour.substring(0,2)),Integer.valueOf(endHour.substring(3)));
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

    /**
     *  Method which loads calendars from database
     */
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
                agenda.getConnector().addItem(cal,user);
                calendar.addItem(name);
                calendar.removeItem("Add new calendar +");
                calendar.addItem("Add new calendar +");
                calendar.setSelectedItem(name);
            }
        });
        if(agenda.getConnector().getNumberOfItems(agenda.getUsername(),"calendar") > 0) {
            List calendars = agenda.getConnector().getItems(agenda.getUsername(),"calendar");
            for(Object o:calendars){
                calendar.addItem(((Items)o).getName());
            }
        }
        calendar.addItem("Add new calendar +");
        JButton deleteCalendar = new JButton("Delete calendar");
        deleteCalendar.addActionListener(e ->{
            new Thread( () -> {
                String currentItem = calendar.getSelectedItem().toString();
                System.out.println(currentItem);
                agenda.getConnector().deleteItem(agenda.getConnector().getItem(
                        agenda.getUsername(), "calendar", currentItem));
                calendar.removeItem(currentItem);
            }).start();
        });
        calPanel.add(calendar);
        calPanel.add(deleteCalendar);
    }

    /**
     * Method which creates panel which allows to add tasks
     * @param parentPanel Panel to add tasks to
     * @return panel which adds tasks
     */
    private JPanel newTask(JPanel parentPanel){
        JPanel panel = new JPanel(new GridLayout(2,4));
        JButton newItem = new JButton();
        JTextField name = new JTextField();
        DatePicker date = new DatePicker();
        ColorPicker priority = ColorPicker.createColorPicker(4,Color.RED);
        newItem.setText("+");
        newItem.addActionListener(e -> {
            Tasks tasks = new Tasks();
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
            if(allFiled){
                Task t = new Task(tasks, agenda.getConnector());
                this.setUpSize(t,parentPanel.getWidth(),50,AgendaConstants.MAXIMUM);
                parentPanel.add(t);
                tasksVector.add(tasks);
                this.updateComponent(parentPanel);
            }
        });
        panel.add(new JLabel("Add new"));
        panel.add(new JLabel("Name:"));
        panel.add(new JLabel("Date:"));
        panel.add(new JLabel("Priority:"));
        panel.add(newItem);
        panel.add(name);
        panel.add(date);
        panel.add(priority);
        return panel;
    }

    /**
     *  Popup containing tasks connected with event
     */
    private void tasksPopUp(){
        JPanel panel = new JPanel(new BorderLayout());
        this.setUpSize(panel,800,600,AgendaConstants.MAXIMUM);
        JPanel tasksPanel = new JPanel();
        tasksPanel.setLayout(new BoxLayout(tasksPanel,BoxLayout.Y_AXIS));
        JScrollPane sp = new JScrollPane(tasksPanel);
        JPanel newTask = newTask(tasksPanel);
        panel.add(sp,BorderLayout.CENTER);
        panel.add(newTask,BorderLayout.SOUTH);
        if(agenda.getConnector().getNumberOfTasks(event) > 0) {
            for (Object o : agenda.getConnector().getTasks(event)) {
                Tasks t = (Tasks)o;
                Task task = new Task(t, agenda.getConnector());
                this.setUpSize(task,790,50,AgendaConstants.MAXIMUM);
                tasksPanel.add(task);
            }
        }
        JOptionPane.showMessageDialog(this,panel);
    }

    /**
     * Method to save event to database
     */
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
        time = endTime.getTime();
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
        event.setEnd(calInst.getTime());
        event.setColor(ColorPicker.getColorName(color.getColor()));
        event.setLocalization(location.getText());
        Notes notes = new Notes();
        if(!note.getText().isEmpty()) {
            notes.setText(note.getText());
            if(event.getNotes_id() == null){
                event.setNotes_id(notes);
                agenda.getConnector().addNote(notes);
            }else {
                notes = event.getNotes_id();
                notes.setText(note.getText());
                agenda.getConnector().editNote(notes);
            }
        }
        String calName = calendar.getSelectedItem().toString();
        Items cal = null;
        if(calName.equals("Add new calendar +")){
            JOptionPane.showMessageDialog(null,"Please select a calendar");
        }else {
            cal = agenda.getConnector().getItem(agenda.getUsername(), "calendar",calName );
        }
        if(cal != null) {
            event.setItem_id(cal);
        }
        agenda.getConnector().saveEvent(event);
        for(Tasks t: tasksVector) {
            agenda.getConnector().addOrEditTask(t);
        }
        if(notes.getText() != null && !notes.getText().isEmpty()) {
            notes.setEvents_id(event);
            agenda.getConnector().editNote(notes);
        }
        tasksVector.clear();
    }

    /**
     *  Method to delete event from database
     */
    public void deleteEvent(){
        agenda.getConnector().deleteEvent(event);
    }
}
