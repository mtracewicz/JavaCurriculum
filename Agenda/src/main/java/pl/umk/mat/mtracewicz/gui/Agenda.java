package pl.umk.mat.mtracewicz.gui;

import pl.umk.mat.mtracewicz.gui.contentPanels.*;
import pl.umk.mat.mtracewicz.utility.DatabaseConnector;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;

/**
 * This class represents main frame of Agenda application
 */
public class Agenda {
    /**
     * Connector responsible for interactions with database
     */
    private DatabaseConnector connector;
    /**
     * Main frame of whole app
     */
    private JFrame mainFrame;
    /**
     *  Main window of Agenda app
     */
    private JPanel window;
    /**
     * Username of currently logged user
     */
    private String username;
    /**
     * Variable describing if user is logged in
     */
    private boolean logged;

    private CalendarPane calendarPane;
    /**
     * Panel displaying calendar in Agenda app
     */
    private ToDoPane todoPane;
    /**
     * Panel displaying notes and notebooks in Agenda app
     */
    private NotesPane notesPane;
    /**
     * Settings panel of Agenda app
     */
    private SettingPane settingsPane;

    /**
     * Constructor of Agenda class which sets up whole GUI of Agenda App
     */
    public Agenda(){
        this.connector = new DatabaseConnector();
        this.logged = false;
        try {
            SwingUtilities.invokeAndWait(() -> {
                this.mainFrame = new JFrame("Agenda");
                this.mainFrame.setPreferredSize(new Dimension(1920,1080));
                this.mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                this.mainFrame.setLayout(new BorderLayout());
                this.window = createWindow();
                this.mainFrame.add(createMenu(),BorderLayout.WEST);
                this.mainFrame.add(window,BorderLayout.CENTER);
                this.showFrame();
            });
        }catch(InvocationTargetException | InterruptedException e){
            e.printStackTrace(System.out);
        }
    }

    /**
     * Shows up main frame of app, sets its size to whole screen
     * and shows settings panel
     */
    private void showFrame(){
        CardLayout cl = (CardLayout)(window.getLayout());
        cl.show(window,"settings");
        this.mainFrame.setExtendedState(Frame.MAXIMIZED_BOTH);
        this.mainFrame.pack();
        this.mainFrame.setVisible(true);
    }

    /**
     * Pop up with error message that user needs to be logged in to access this content
     */
    private void loginPopUp(){
        JOptionPane.showMessageDialog(this.mainFrame, "You must login first!", "Inane error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Creates left menu of Agenda app
     * @return menu of Agenda app
     */
    private JPanel createMenu(){
        JPanel menu;
        menu = new JPanel();
        menu.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 5, Color.darkGray));
        menu.setLayout(new GridLayout(5, 1));

        CardLayout cl = (CardLayout)(window.getLayout());

        JButton calendar = new JButton("Calendar");
        calendar.setBackground(Color.gray);
        calendar.addActionListener(e -> {
            if(this.logged){
                if(this.calendarPane == null){
                    this.calendarPane = new CalendarPane(this);
                    this.window.add("calendar",this.calendarPane);
                }else {
                    this.calendarPane.resetPanel();
                }
                cl.show(this.window,"calendar" );
            }else this.loginPopUp();
        });

        JButton todo = new JButton("To-do");
        todo.setBackground(Color.gray);
        todo.addActionListener(e -> {
            if(this.logged){
                if(this.todoPane == null) {
                    this.todoPane = new ToDoPane(this);
                    this.window.add("todo", this.todoPane);
                }else {
                    this.todoPane.resetPanel();
                }
                cl.show(this.window,"todo" );
            }else this.loginPopUp();
        });

        JButton notes = new JButton("Notes");
        notes.setBackground(Color.gray);
        notes.addActionListener(e -> {
            if(this.logged){
                if(this.notesPane == null){
                    this.notesPane = new NotesPane(this);
                    this.window.add("notes",this.notesPane);
                }else {
                    this.notesPane.resetPanel();
                }
                cl.show(this.window,"notes" );
            }else this.loginPopUp();
        });
        JButton settings = new JButton("Settings");
        settings.setBackground(Color.gray);
        settings.addActionListener(e -> {
            if(this.logged){
                this.settingsPane.resetPanel();
                cl.show(this.window,"settings" );
            }
        });

        JPanel whiteSpace = new JPanel();
        whiteSpace.setBackground(Color.gray);

        menu.add(calendar);
        menu.add(todo);
        menu.add(notes);
        menu.add(whiteSpace);
        menu.add(settings);

        return menu;
    }

    /**
     * Creates main window of Agenda app and set up settings pane
     * @return main window of Agenda app
     */
    private JPanel createWindow(){
        JPanel mainWindow = new JPanel();
        mainWindow.setLayout(new CardLayout());
        this.settingsPane = new SettingPane(this);
        mainWindow.add("settings",this.settingsPane);
        return mainWindow;
    }

    /**
     * Gets username of current user
     * @return username of current user
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets username of current user
     * @param username username of current user
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Sets logged variable
     * @param logged true - if user is logged in, false - if not
     */
    public void setLogged(boolean logged) {
        this.logged = logged;
    }

    /**
     * Check if user is logged in
     * @return true - if user is logged in, false - if not
     */
    public boolean isLogged() {
        return logged;
    }

    /**
     * Returns connector object
     * @return connector object
     */
    public DatabaseConnector getConnector() {
        return connector;
    }
}
