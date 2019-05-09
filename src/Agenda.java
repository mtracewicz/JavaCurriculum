import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;

public class Agenda {
    private JFrame mainFrame;
    private JPanel window;

    private Agenda(){
        try {
            SwingUtilities.invokeAndWait(() -> {
                mainFrame = new JFrame("Agenda");
                mainFrame.setPreferredSize(new Dimension(1920,1080));
                mainFrame.setLayout(new BorderLayout());
                mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                window = createWindow();
                mainFrame.add(createMenu(),BorderLayout.WEST);
                mainFrame.add(window,BorderLayout.CENTER);
                showFrame();
            });
        }catch(InvocationTargetException | InterruptedException e){
            e.printStackTrace(System.out);
        }
    }

    private JPanel createWindow(){
        JPanel mainWindow = new JPanel();
        mainWindow.setLayout(new CardLayout());

        JPanel calendarPane = new JPanel();
        JPanel todoPane = new JPanel();
        JPanel notesPane = new JPanel();

        calendarPane.setName("Calendar");
        todoPane.setName("To-do");
        notesPane.setName("Notes");

        calendarPane.add(new JLabel("Calendar"));
        todoPane.add(new JLabel("To-do"));
        notesPane.add(new JLabel("Notes"));

        mainWindow.add("calendar",calendarPane);
        mainWindow.add("todo",todoPane);
        mainWindow.add("notes",notesPane);

        return mainWindow;
    }

    private void showFrame(){
        mainFrame.pack();
        mainFrame.setVisible(true);
    }

    private JPanel createMenu(){
        JPanel menu;
        JButton calendar;
        JButton todo;
        JButton notes;

        menu = new JPanel();
        menu.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 5, Color.darkGray));
        menu.setLayout(new GridLayout(3, 1));

        calendar = new JButton("Calendar");
        todo = new JButton("To-do");
        notes = new JButton("Notes");

        CardLayout cl = (CardLayout)(window.getLayout());
        calendar.addActionListener(e -> cl.show(window,"calendar" ));
        todo.addActionListener(e -> cl.show(window,"todo" ));
        notes.addActionListener(e -> cl.show(window,"notes" ));

        menu.add(calendar);
        menu.add(todo);
        menu.add(notes);

        return menu;
    }

    public static void main(String[] args){
        Agenda agenda = new Agenda();
    }
}
