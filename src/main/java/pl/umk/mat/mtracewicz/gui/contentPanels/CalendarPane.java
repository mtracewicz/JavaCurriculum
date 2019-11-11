package pl.umk.mat.mtracewicz.gui.contentPanels;
import pl.umk.mat.mtracewicz.entity.Events;
import pl.umk.mat.mtracewicz.entity.Users;
import pl.umk.mat.mtracewicz.gui.Agenda;
import pl.umk.mat.mtracewicz.gui.occurrencePanels.Event;
import pl.umk.mat.mtracewicz.gui.utilityPanels.ColorPicker;
import pl.umk.mat.mtracewicz.gui.utilityPanels.DatePicker;
import pl.umk.mat.mtracewicz.utility.AgendaConstants;
import pl.umk.mat.mtracewicz.utility.DateHandler;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *  Class representing panel showing calendars in app
 */
public class CalendarPane extends ContentPanel {
    /**
     * Reference to main frame of app
     */
    private Agenda agenda;
    /**
     * Variable which saves what is currently shown on panel
     */
    private view currentView;
    /**
     *  Integer representing today
     */
    private int today;
    /**
     * Integer representing current month
     */
    private int currentMonth;
    /**
     * Integer representing current year
     */
    private int currentYear;
    /**
     *  String representing date which is currently being used
     */
    private String currentDate;
    /**
     *  List of events for user
     */
    private java.util.List events;

    /**
     * Enum representing all possible views,
     * which are what can be displayed in this panel
     */
    private enum view {
        DAY,WEEK,MONTH
    }

    /**
     * Constructor with reference to main frame
     * @param agenda reference to main frame
     */
    public CalendarPane(Agenda agenda) {
        super();
        this.agenda = agenda;
        events = agenda.getConnector().getEvents(new Users(agenda.getUsername()));
        if(events == null){
            events = new ArrayList();
        }
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dayGetter = new SimpleDateFormat("dd");
        SimpleDateFormat monthGetter = new SimpleDateFormat("MM");
        SimpleDateFormat yearGetter = new SimpleDateFormat("YYYY");
        this.today = Integer.valueOf(dayGetter.format(calendar.getTime()));
        this.currentMonth = Integer.valueOf(monthGetter.format(calendar.getTime()));
        this.currentYear = Integer.valueOf(yearGetter.format(calendar.getTime()));

        this.setUpHeader();
        this.setUpFooter();
        this.setUpMonthView(this.today,this.currentMonth,this.currentYear);
        this.currentView = view.MONTH;
    }

    @Override
    public void resetPanel(){
        DatePicker dp = (DatePicker)this.footer.getComponent(1);
        dp.setDate(today,currentMonth,currentYear);
        this.setUpMonthView(today,currentMonth,currentYear);
        this.showPanel("moth");
    }

    /**
     * Method which sets up header for this panel
     */
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

    /**
     * Method which sets up footer for this panel
     */
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
            }else if(currentView == view.WEEK) {
                calendar.add(Calendar.DAY_OF_MONTH, 7);
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/YYYY");
            String tmp = simpleDateFormat.format(calendar.getTime());
            d = DateHandler.getDayFromString(tmp);
            m = DateHandler.getMonthFromString(tmp);
            y = DateHandler.getYearFromString(tmp);

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
        this.footer.add(back);
        this.footer.add(datePicker);
        this.footer.add(forth);
    }

    /**
     * Method which changes current view and date in reaction to
     * what is selected in date picker
     * @param datePicker Data picker from which date will be taken
     */
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

    /**
     * Pop up to show for new event when creating one
     * @param day day of event
     * @param month month of event
     * @param year year of event
     * @param hour hour of event
     * @return Event created in popup
     */
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

    /**
     * Pop up to show for new event when editing one
     * @param event event to show
     * @return edited event
     */
    private Events eventPopUp(Events event){
        Event eventForm = new Event(event,agenda);
        Object[] options = {"Save","Delete","Cancel"};
        int ok  = JOptionPane.showOptionDialog(null,eventForm,
                "Event",JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,null,options,options[2]);
        if(ok == JOptionPane.OK_OPTION) {
            new Thread(eventForm::saveEvent).start();
            return eventForm.getEvent();
        }else if(ok == JOptionPane.NO_OPTION) {
            events.remove(eventForm.getEvent());
            new Thread(eventForm::deleteEvent).start();
            return null;
        }else {
            return null;
        }
    }

    /**
     * Creating panel for set day/month/year
     * @param day day to show
     * @param month month in which day is
     * @param year year in which day is
     * @return Panel showing day
     */
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
            if(i == 0) {
                calendar.set(year, month - 1, day-1, 23, 59, 59);
            }else{
                calendar.set(year, month - 1, day, i-1, 59, 59);
            }
            Calendar calendar1 = Calendar.getInstance();
            calendar1.set(year, month - 1, day, i, 59,59);
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

    /**
     * Sets up day view for selected day
     * @param day day to setup
     * @param month month in which day is
     * @param year year in which day is
     */
    private void setUpDayView(int day,int month,int year){
        this.window.add("day",this.createDayView(day,month,year));
        this.showPanel("day");
        this.updateComponent(window);
    }

    /**
     * Sets up week view(which are seven day panels connected) for given date
     * @param day day to determine week
     * @param month month in which dy takes place
     * @param year year in which dy takes place
     */
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

    /**
     * Sets up month view for provided date
     * @param day day to set up for
     * @param month month which will be set up
     * @param year year in which is month
     */
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

    /**
     * Gets event which takes places on or after date represented by calendar
     * and before date represented by calendar1
     * @param calendar Calendar to get first date from
     * @param calendar1 Calendar to get second date from
     * @return If there is an event then it is returned otherwise returns null
     */
    private Events getEvent(Calendar calendar,Calendar calendar1){
        Date d;
        Date d1 = calendar.getTime();
        Date d2 = calendar1.getTime();
        if(events != null) {
            for (Object e : events) {
                Events event = (Events) e;
                d = event.getStart();
                if ((d.after(d1) || d.equals(d1)) && d.before(d2)) {
                    return event;
                }
            }
        }
        return null;
    }

    /**
     * Sets buttons representing days in provided panel in
     * in proper places of calendar
     * @param panel panel to fill
     * @param month month which will be filled
     * @param year year of month
     */
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
        new Thread(() ->{
            for(int i = 1; i< numberOfDaysInMonth; i++){
            Events event = agenda.getConnector().isThereAnEvent(i,month,year,new Users(agenda.getUsername()));
            if( event != null) {
                panel.getComponent(6+i+emptyNumber).setBackground(ColorPicker.getColor(event.getColor()));
                this.updateComponent(panel.getComponent(6+i+emptyNumber));
            } }}).start();

    }

    /**
     * Checks if events contains e
     * @param e event to search for
     * @return true if contains, false otherwise
     */
    private boolean contains(Events e){
        for(Object o:events){
            if(e.equals((Events)o)){
                return true;
            }
        }
        return false;
    }
}