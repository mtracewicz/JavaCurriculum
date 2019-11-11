package pl.umk.mat.mtracewicz.gui.contentPanels;

import pl.umk.mat.mtracewicz.entity.Items;
import pl.umk.mat.mtracewicz.entity.Notes;
import pl.umk.mat.mtracewicz.entity.Users;
import pl.umk.mat.mtracewicz.gui.Agenda;
import pl.umk.mat.mtracewicz.gui.occurrencePanels.Note;
import pl.umk.mat.mtracewicz.gui.occurrencePanels.Notebook;
import pl.umk.mat.mtracewicz.gui.utilityPanels.ColorPicker;
import pl.umk.mat.mtracewicz.utility.AgendaConstants;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

/**
 * Class representing panel notebooks and notes lists
 */
public class NotesPane extends ContentPanel {
    /**
     *  Number of notebooks owned by user
     */
    private int numberOfNotebooks;
    /**
     *  Panel storing notebooks of user
     */
    private JPanel notebooks;
    /**
     *  Panel storing notes of user
     */
    private JPanel notes;
    /**
     *  Vector storing panels for individual notebooks
     */
    private Vector<JPanel> notePanels;
    /**
     *  Reference to main frame
     */
    private Agenda agenda;
    /**
     *  String with name of notebook which is being currently used
     */
    private String currentNotebook;

    /**
     * Constructor which get reference to main frame
     * @param agenda Reference to main frame
     */
    public NotesPane(Agenda agenda) {
        super();
        this.agenda = agenda;
        currentNotebook = null;
        notePanels = new Vector<>();
        notes = new JPanel(new CardLayout());
        this.setUpNotebooks();
        window.add("notebooks",notebooks);
        window.add("notes",notes);
        this.add(window);
        this.showPanel("notebooks");
    }

    @Override
    public void resetPanel(){
        this.showPanel("notebooks");
        this.setUpNotebooksFooter();
        this.updateComponent(footer);
    }

    /**
     *  Method which sets up footer for when notebooks list is being shown
     */
    private void setUpNotebooksFooter(){
        this.clearPanel(footer);
        JButton addNewButton = new JButton("Add new notebook +");
        JLabel notebookLabel = new JLabel("New notebook name: ");
        JTextField notebookName = new JTextField();
        ColorPicker colorChooser = ColorPicker.createColorPicker(4);
        notebookLabel.setHorizontalAlignment(JLabel.RIGHT);

        footer.setLayout(new GridLayout(1,4));
        footer.add(addNewButton);
        footer.add(notebookLabel);
        footer.add(notebookName);
        footer.add(colorChooser);

        addNewButton.addActionListener( e -> {
            String name = notebookName.getText();
            if(!name.isEmpty()) {
                this.addNotebook(name);
            }else{
                this.emptyTextFieldPopUp("notebook name");
            }
            notebookName.setText(null);
        });

        this.setUpSize(footer,1920,50, AgendaConstants.MAXIMUM);
        this.updateComponent(footer);
    }

    /**
     *  Method which sets up footer for when notebook is open
     */
    private void setUpNotesFooter(){
        this.clearPanel(footer);
        JButton newNote = new JButton("Add new note +");
        JLabel newNoteNameLabel = new JLabel("Note title :");
        JTextField newNoteName = new JTextField();
        JButton back = new JButton("<- Back");
        Items i = agenda.getConnector().getItem(agenda.getUsername(),"notebook",currentNotebook);
        Color c = ColorPicker.getColor(i.getColor());
        ColorPicker colorPicker = ColorPicker.createColorPicker(4,c);

        newNoteNameLabel.setHorizontalAlignment(JLabel.RIGHT);

        back.addActionListener(e -> {
            this.showPanel("notebooks");
            this.setUpNotebooksFooter();
            this.updateComponent(footer);
        });
        newNote.addActionListener(e ->{
            if(!newNoteName.getText().isEmpty()){
                this.addNote(newNoteName.getText(),currentNotebook);
                newNoteName.setText(null);
            }else{
                this.emptyTextFieldPopUp("note name");
            }
        });

        footer.setLayout(new GridLayout(1,5));
        footer.add(newNote);
        footer.add(newNoteNameLabel);
        footer.add(newNoteName);
        footer.add(colorPicker);
        footer.add(back);
        this.setUpSize(footer,1920,50,AgendaConstants.MAXIMUM);
        this.updateComponent(footer);
    }

    /**
     *  Method to set up notebooks list view
     */
    private void setUpNotebooks(){
        notebooks = new JPanel();
        notebooks.setLayout(new BoxLayout(notebooks,BoxLayout.Y_AXIS));
        numberOfNotebooks = this.checkNumberOfNotebooks();
        this.setUpNotebooksFooter();
        if(this.numberOfNotebooks == 0){
            notebooks.add(new JLabel("You currently have no notebooks. Please add some!"));
        }else {
            this.loadNotebooks();
        }
    }

    /**
     *  Method which loads notebooks from database
     */
    private void loadNotebooks() {
        for(Object o: agenda.getConnector().getItems(agenda.getUsername(),"notebook")) {
            Items notebookData =(Items) o;
            Notebook panel = new Notebook(notebookData, agenda, this);
            notebooks.add(panel);
            this.updateComponent(notebooks);
            numberOfNotebooks++;
            JPanel notebookPanel = new JPanel();
            notebookPanel.setName(notebookData.getName());
            notePanels.add(notebookPanel);
            notes.add(notebookData.getName(), notebookPanel);
            new Thread(() -> this.setUpNotes(notebookData)).start();
        }
    }

    /**
     *  Method which checks number of notebooks for user
     * @return number of notebooks for user
     */
    private int checkNumberOfNotebooks(){
        return agenda.getConnector().getNumberOfItems(agenda.getUsername(),"notebook");
    }

    /**
     * Method to set up view for a single notebook
     * @param notebook notebook to set up
     */
    public void setUpNotes(Items notebook) {
        JPanel panel = null;
        for (JPanel c : notePanels) {
            if (c.getName().equals(notebook.getName())) {
                panel = c;
            }
        }
        if (panel != null) {
            JPanel notesWindow = new JPanel();
            JLabel nameLabel = new JLabel(notebook.getName());
            nameLabel.setHorizontalAlignment(JLabel.CENTER);
            notesWindow.setLayout(new BoxLayout(notesWindow, BoxLayout.Y_AXIS));
            panel.setLayout(new BorderLayout());
            panel.add(nameLabel, BorderLayout.NORTH);
            panel.add(notesWindow, BorderLayout.CENTER);
            int numberOfNotes = agenda.getConnector().getNumberOfNotes(notebook);
            if(numberOfNotes > 0){
                for(Object o: agenda.getConnector().getNotes(notebook)){
                    Notes notes = (Notes)o;
                    Note note = new Note(notes,agenda,notesWindow);
                    note.setName(notes.getTitle()+"NOTE");
                    notesWindow.add(note);
                }
            }
        }
    }

    /**
     * Method to show notebook with name matching provided name
     * @param name name of notebook to show
     */
    public void showNotebook(String name){
        CardLayout cardLayout = (CardLayout) notes.getLayout();
        cardLayout.show(notes, name);
        currentNotebook = name;
        this.setUpNotesFooter();
        this.showPanel("notes");
    }

    /**
     * Method to add notebooks
     * @param name name of notebook
     */
    private void addNotebook(String name){
        if(numberOfNotebooks == 0){
            notebooks.remove(0);
        }
        Notebook panel = new Notebook(name,agenda,this);
        notebooks.add(panel);
        this.updateComponent(notebooks);
        numberOfNotebooks++;

        Items notebookData = new Items();
        notebookData.setName(name);
        notebookData.setType("notebook");
        notebookData.setEmail(new Users(agenda.getUsername()));
        notebookData.setColor(ColorPicker.getColorName(((ColorPicker)(footer.getComponent(3))).getColor()));
        agenda.getConnector().addItem(notebookData,new Users(agenda.getUsername()));
        JPanel notebookPanel = new JPanel();
        notebookPanel.setName(name);
        notePanels.add(notebookPanel);
        notes.add(name, notebookPanel);
        new Thread(() -> this.setUpNotes(notebookData)).start();
    }

    /**
     * Method which adds note to notebook
     * @param name name of note
     * @param notebookName name of notebook to add note to
     */
    private void addNote(String name,String notebookName){
        JPanel notesWindow = null;
        for(JPanel p: notePanels){
            if(p.getName().equals(notebookName)){
                notesWindow = p;
                break;
            }
        }
        if(notesWindow == null){
            return;
        }
        notesWindow = (JPanel)notesWindow.getComponent(1);
        Notes note = new Notes();
        note.setTitle(name);
        note.setColor( ColorPicker.getColorName( ((ColorPicker)footer.getComponent(3)).getColor() ));
        note.setItems_id(agenda.getConnector().getItem(agenda.getUsername(),"notebook",notebookName));
        Note notePane = new Note(note,agenda,notesWindow);
        new Thread(() -> agenda.getConnector().addNote(note)).start();
        notePane.setName(name+"NOTE");
        this.setUpSize(notePane,1920,50,AgendaConstants.MAXIMUM);
        notesWindow.add(notePane);
        this.updateComponent(notesWindow);
    }

    /**
     * Getter for footer currently used in panel
     * @return footer of this panel
     */
    public JPanel getFooter(){
        return this.footer;
    }

    /**
     * Getter for notebooks
     * @return JPanel of Notebook lists
     */
    public JPanel getNotebooks() {
        return notebooks;
    }

    /**
     * Getter for number of notebooks
     * @return number of notebooks
     */
    public int getNumberOfNotebooks() {
        return numberOfNotebooks;
    }

    /**
     * Setter for number of notebooks
     * @param numberOfNotebooks new number of notebooks
     */
    public void setNumberOfNotebooks(int numberOfNotebooks) {
        this.numberOfNotebooks = numberOfNotebooks;
    }
}