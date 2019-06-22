package pl.umk.mat.mtracewicz.gui;

import pl.umk.mat.mtracewicz.gui.contentPanels.*;
import pl.umk.mat.mtracewicz.utility.DatabaseConnector;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;

public class Agenda {
    public DatabaseConnector connector;
    private JFrame mainFrame;
    private JPanel window;
    private String username;
    private boolean logged;

    private CalendarPane calendarPane;
    private ToDoPane todoPane;
    private NotesPane notesPane;
    private SettingPane settingsPane;

    public Agenda(){
        new Thread( () -> this.connector = new DatabaseConnector() ).start();
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
    private void showFrame(){
        CardLayout cl = (CardLayout)(window.getLayout());
        cl.show(window,"settings");
        this.mainFrame.setExtendedState(Frame.MAXIMIZED_BOTH);
        this.mainFrame.pack();
        this.mainFrame.setVisible(true);
    }
    private void loginPopUp(){
        JOptionPane.showMessageDialog(this.mainFrame, "You must login first!", "Inane error", JOptionPane.ERROR_MESSAGE);
    }
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
                    this.notesPane = new NotesPane();
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
    private JPanel createWindow(){
        JPanel mainWindow = new JPanel();
        mainWindow.setLayout(new CardLayout());
        this.settingsPane = new SettingPane(this);
        mainWindow.add("settings",this.settingsPane);
        return mainWindow;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setLogged(boolean logged) {
        this.logged = logged;
    }
    public boolean isLogged() {
        return logged;
    }
}
