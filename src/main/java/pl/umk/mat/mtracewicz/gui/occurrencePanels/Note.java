package pl.umk.mat.mtracewicz.gui.occurrencePanels;

import pl.umk.mat.mtracewicz.entity.Notes;
import pl.umk.mat.mtracewicz.entity.Tasks;
import pl.umk.mat.mtracewicz.gui.Agenda;
import pl.umk.mat.mtracewicz.gui.AgendaPanel;
import pl.umk.mat.mtracewicz.gui.utilityPanels.ColorPicker;
import pl.umk.mat.mtracewicz.gui.utilityPanels.DatePicker;
import pl.umk.mat.mtracewicz.utility.AgendaConstants;
import pl.umk.mat.mtracewicz.utility.ColorPriorityHandler;
import pl.umk.mat.mtracewicz.utility.DateHandler;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

/**
 *  Class representing panel corresponding to note in database
 */
public class Note extends AgendaPanel {
    /**
     *  Object storing data of note from database corresponding to panel
     */
    private Notes note;
    /**
     *  Reference to main frame object allowing to get username and use connector
     */
    private Agenda agenda;
    /**
     * Panel to which note is added
     */
    private JPanel window;
    /**
     *  Label showing name of note
     */
    private JLabel noteName;
    /**
        Vector of tasks connected to note
     */
    private Vector<Tasks> tasksVector;

    /**
     * Constructor which build panel from note object
     * @param note Note corresponding to this panel
     * @param agenda Reference to main frame of app
     * @param window Panel to which notes will be added
     */
    public Note(Notes note, Agenda agenda,JPanel window){
        this.tasksVector = new Vector<>();
        this.note = note;
        this.agenda = agenda;
        this.window = window;
        JButton showTasks = new JButton("Show Tasks");
        showTasks.addActionListener(e -> {
            this.tasksPopUp();
            new Thread( () -> {
                for (Object o : tasksVector) {
                    Tasks t = (Tasks) o;
                    agenda.getConnector().addOrEditTask(t);
                }
                tasksVector.clear();
            }).start();
        });
        this.setBackground(ColorPicker.getColor(note.getColor()));
        noteName = new JLabel(note.getTitle());
        noteName.setHorizontalAlignment(SwingUtilities.CENTER);
        JButton edit = new JButton("Edit");
        JButton changeTitle = new JButton("Change title");
        JButton changeColor = new JButton("Change color");
        JButton delete = new JButton("X");

        edit.addActionListener(e -> editNote());
        changeTitle.addActionListener(e -> editNoteName());
        delete.addActionListener(e -> deleteNote());
        changeColor.addActionListener(e-> editNoteColor());

        this.setLayout(new GridLayout(1,5));
        this.add(noteName);
        this.add(edit);
        this.add(changeTitle);
        this.add(changeColor);
        this.add(showTasks);
        this.add(delete);
        this.setUpSize(this,1920,50,AgendaConstants.MAXIMUM);
    }

    /**
     *  Method to delete note from panel and database
     */
    private void deleteNote(){
        window.remove(this);
        agenda.getConnector().deleteNote(note);
        this.updateComponent(window);
    }

    /**
     *  Method which allows to modify title,color and content of note
     */
    private void editNote(){
        JPanel noteP = new JPanel();
        JLabel titleLabel = new JLabel("Title:");
        JTextField title = new JTextField();
        title.setText(note.getTitle());
        JLabel noteLabel = new JLabel("Note:");
        JTextArea noteBody = new JTextArea();
        noteBody.setText(note.getText());
        Color color = ColorPicker.getColor(note.getColor());
        ColorPicker colorPicker = ColorPicker.createColorPicker(4,color);
        this.setUpSize(colorPicker,800,50, AgendaConstants.MINIMUM);

        this.setUpSize(noteBody,800,600,AgendaConstants.MINIMUM);
        noteP.setLayout(new BoxLayout(noteP,BoxLayout.Y_AXIS));
        noteP.add(titleLabel);
        noteP.add(title);
        noteP.add(noteLabel);
        noteP.add(noteBody);
        noteP.add(colorPicker);
        int okCxl = JOptionPane.showConfirmDialog(null, noteP, "Note", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if(okCxl != JOptionPane.CANCEL_OPTION){
            this.setBackground(colorPicker.getColor());
            this.noteName.setText(title.getText());
            note.setColor(ColorPicker.getColorName(colorPicker.getColor()));
            note.setTitle(title.getText());
            note.setText(noteBody.getText());
            new Thread( () -> agenda.getConnector().editNote(note)).start();
        }
    }

    /**
     *  Method to edit note title
     */
    private void editNoteName() {
        String newName = this.stringPopUp("New note title: ");
        if (newName != null && !newName.isEmpty()) {
            this.noteName.setText(newName);
            note.setTitle(newName);
            new Thread(() -> agenda.getConnector().editNote(note)).start();
        }
    }

    /**
     *  Method to change note color
     */
    private void editNoteColor() {
        ColorPicker colorPicker = ColorPicker.createColorPicker(4, ColorPicker.getColor(note.getColor()));
        int okCXL = JOptionPane.showConfirmDialog(null, colorPicker, "Chose color", JOptionPane.OK_CANCEL_OPTION);
        if (okCXL == JOptionPane.OK_OPTION) {
            Color color = colorPicker.getColor();
            this.setBackground(color);
            note.setColor(ColorPicker.getColorName(color));
            new Thread(() -> agenda.getConnector().editNote(note)).start();
        }
    }

    /**
     * Method creating panel which allows to add new tasks
     * @param parentPanel Panel to which tasks will be added
     * @return Panel which allows to add tasks
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
            tasks.setNotes_id(note);
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
     *  Popup containing tasks connected to note
     */
    private void tasksPopUp(){
        JPanel panel = new JPanel(new BorderLayout());
        this.setUpSize(panel,800,600,AgendaConstants.MAXIMUM);
        JPanel tasksPanel = new JPanel();
        tasksPanel.setLayout(new BoxLayout(tasksPanel,BoxLayout.Y_AXIS));
        JScrollPane sp = new JScrollPane(tasksPanel,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        JPanel newTask = newTask(tasksPanel);
        panel.add(sp,BorderLayout.CENTER);
        panel.add(newTask,BorderLayout.SOUTH);
        if(agenda.getConnector().getNumberOfTasks(note) > 0) {
            for (Object o : agenda.getConnector().getTasks(note)) {
                Tasks t = (Tasks)o;
                Task task = new Task(t, agenda.getConnector());
                this.setUpSize(task,790,50,AgendaConstants.MAXIMUM);
                tasksPanel.add(task);
            }
        }
        JOptionPane.showMessageDialog(this,panel);
    }
}
