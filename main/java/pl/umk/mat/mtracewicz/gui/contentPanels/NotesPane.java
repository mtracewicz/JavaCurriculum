package pl.umk.mat.mtracewicz.gui.contentPanels;

import pl.umk.mat.mtracewicz.gui.AgendaPanel;
import pl.umk.mat.mtracewicz.gui.utilityPanels.ColorPicker;
import pl.umk.mat.mtracewicz.utility.AgendaConstants;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public class NotesPane extends ContentPanel {
    private int numberOfNotebooks;
    private JPanel notebooks;
    private JPanel notes;
    private Vector<JPanel> notePanels;

    @Override
    public void resetPanel(){
        this.showPanel("notebooks");
        this.setUpNotebooksFooter();
        this.updateComponent(footer);
    }
    private void setUpNotebooksFooter(){
        JPanel newNotebookArea = new JPanel();
        JButton addNewButton = new JButton("Add new notebook +");
        JLabel notebookLabel = new JLabel("New notebook name: ");
        JTextField notebookName = new JTextField();
        ColorPicker colorChooser = ColorPicker.createColorPicker(4);
        this.clearPanel(footer);
        notebookLabel.setHorizontalAlignment(JLabel.RIGHT);

        newNotebookArea.setLayout(new GridLayout(1,4));
        newNotebookArea.add(addNewButton);
        newNotebookArea.add(notebookLabel);
        newNotebookArea.add(notebookName);
        newNotebookArea.add(colorChooser);

        addNewButton.addActionListener( e -> {
            String name = notebookName.getText();
            if(!name.isEmpty()) {
                this.addNotebook(name);
            }else{
                this.emptyTextFieldPopUp("notebook name");
            }
            notebookName.setText(null);
        });

        this.setUpSize(newNotebookArea,footer.getParent().getWidth(),50, AgendaConstants.MAXIMUM);
        footer.add(newNotebookArea);
    }
    private JPanel setUpNotesFooter(){
        JPanel tmpFooter = new JPanel();
        JButton newNote = new JButton("Add new note +");
        JLabel newNoteNameLabel = new JLabel("Note title :");
        JTextField newNoteName = new JTextField();
        JButton back = new JButton("<- Back");
        ColorPicker colorPicker = ColorPicker.createColorPicker(4);

        newNoteNameLabel.setHorizontalAlignment(JLabel.RIGHT);

        if(footer.getComponentCount()> 0){
            for(int i = 0; i < footer.getComponentCount(); i++) {
                footer.remove(i);
            }
        }

        back.addActionListener(e -> {
            this.showPanel("notebooks");
            this.setUpNotebooksFooter();
            this.updateComponent(footer);
        });
        newNote.addActionListener(e ->{
            if(!newNoteName.getText().isEmpty()){
                this.addNote(newNoteName.getText(),tmpFooter);
                newNoteName.setText(null);
            }else{
                this.emptyTextFieldPopUp("note name");
            }
        });

        tmpFooter.setLayout(new GridLayout(1,5));
        tmpFooter.add(newNote);
        tmpFooter.add(newNoteNameLabel);
        tmpFooter.add(newNoteName);
        tmpFooter.add(colorPicker);
        tmpFooter.add(back);
        this.setUpSize(tmpFooter,1920,50,AgendaConstants.MAXIMUM);
        return tmpFooter;
    }
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
    private int checkNumberOfNotebooks(){
        return 0;
    }
    private void loadNotebooks() {

    }
    private void setUpNotes(String name,boolean show) {
        JPanel panel = null;
        for (JPanel c : notePanels) {
            if (c.getName().equals(name)) {
                panel = c;
            }
        }
        if (panel != null) {
            JPanel notesWindow = new JPanel();
            JLabel nameLabel = new JLabel(name);
            nameLabel.setHorizontalAlignment(JLabel.CENTER);
            notesWindow.setLayout(new BoxLayout(notesWindow, BoxLayout.Y_AXIS));
            panel.setLayout(new BorderLayout());
            panel.add(nameLabel, BorderLayout.NORTH);
            panel.add(notesWindow, BorderLayout.CENTER);
            this.clearPanel(footer);
            if (panel.getComponentCount() <= 2) {
                panel.add(this.setUpNotesFooter(), BorderLayout.SOUTH);
            }
            if (show) {
                CardLayout cardLayout = (CardLayout) notes.getLayout();
                cardLayout.show(notes, name);
                this.showPanel("notes");
            }
        }
    }
    private void editNotebookColor(String name) {
        for (int i = 0; i < notebooks.getComponentCount(); i++) {
            if (notebooks.getComponent(i).getName().equals(name)) {
                JPanel panel = (JPanel) notebooks.getComponent(i);
                ColorPicker colorPicker = ColorPicker.createColorPicker(4);
                JOptionPane.showConfirmDialog(window, colorPicker, "Chose color", JOptionPane.OK_CANCEL_OPTION);
                panel.setBackground(colorPicker.getColor());
                this.updateComponent(notebooks);
            }
        }
    }
    private void addNotebook(String name){
        JPanel panel = new JPanel();
        JLabel notebookName = new JLabel(name);
        notebookName.setHorizontalAlignment(SwingUtilities.CENTER);
        JButton notebook = new JButton("Pick");
        JButton deleteNotebook = new JButton("X");
        JButton editNotebook = new JButton("Change name");
        JButton editNotebookColorButton = new JButton("Change color");
        panel.setName(name);
        notebook.addActionListener( e -> setUpNotes(name,true));
        if(numberOfNotebooks == 0){
            notebooks.remove(0);
        }
        deleteNotebook.addActionListener( e -> deleteNotebook(name));
        editNotebook.addActionListener(e -> editNotebook(name));
        editNotebookColorButton.addActionListener(e -> editNotebookColor(name));
        panel.setLayout(new GridLayout(1,5));
        panel.setBackground(((ColorPicker)((JPanel)(footer.getComponent(0))).getComponent(3)).getColor());
        panel.add(notebookName);
        panel.add(notebook);
        panel.add(editNotebook);
        panel.add(editNotebookColorButton);
        panel.add(deleteNotebook);
        this.setUpSize(panel,1920,50,AgendaConstants.ALL);
        notebooks.add(panel);
        this.updateComponent(notebooks);
        numberOfNotebooks++;

        JPanel notebookPanel = new JPanel();
        notebookPanel.setName(name);
        notePanels.add(notebookPanel);
        notes.add(name, notebookPanel);
    }
    private void deleteNotebook(String name){
        for(int i = 0;i < notebooks.getComponentCount();i++){
            if(notebooks.getComponent(i).getName().equals(name)){
                notebooks.remove(i);
                numberOfNotebooks--;
                if(this.numberOfNotebooks == 0){
                    notebooks.add(new JLabel("You currently have no notebooks. Please add some!"));
                }
                this.updateComponent(notebooks);
                break;
            }
        }
    }
    private void editNotebook(String name){
        for(int i = 0;i < notebooks.getComponentCount();i++){
            if(notebooks.getComponent(i).getName().equals(name)){
                JPanel panel = (JPanel)notebooks.getComponent(i);
                JLabel button = (JLabel)panel.getComponent(0);
                button.setText(this.stringPopUp("New notebook name: "));
                this.setUpNotes(button.getText(),false);
                this.updateComponent(notebooks);
            }
        }
    }
    private void addNote(String name,JPanel pane){
        JPanel notesWindow = null;
        for(JPanel p: notePanels){
            if(p.getName().equals(pane.getParent().getName())){
                notesWindow = p;
                break;
            }
        }
        if(notesWindow == null){
            return;
        }
        notesWindow = (JPanel)notesWindow.getComponent(1);
        JPanel notePane = new JPanel();
        JLabel noteName = new JLabel(name);
        noteName.setHorizontalAlignment(SwingUtilities.CENTER);
        JButton edit = new JButton("Edit");
        JButton changeTitle = new JButton("Change title");
        JButton changeColor = new JButton("Change color");
        JButton delete = new JButton("X");
        notePane.setName(name+"NOTE");

        edit.addActionListener(e -> notePane.setBackground(editNote(name)));
        changeTitle.addActionListener( e -> editNoteName(name,pane));
        delete.addActionListener(e -> deleteNote(name,pane));
        changeColor.addActionListener(e-> editNoteColor(name,pane));

        notePane.setLayout(new GridLayout(1,5));
        notePane.add(noteName);
        notePane.add(edit);
        notePane.add(changeTitle);
        notePane.add(changeColor);
        notePane.add(delete);
        notePane.setBackground(((ColorPicker)pane.getComponent(3)).getColor());
        this.setUpSize(notePane,1920,50,AgendaConstants.MAXIMUM);
        notesWindow.add(notePane);
        this.updateComponent(notesWindow);
    }
    private void deleteNote(String name,JPanel pane){
        JPanel notesWindow;
        notesWindow = (JPanel)pane.getParent().getComponent(1);
        for(Component c: notesWindow.getComponents()){
            if(c.getName().equals(name+"NOTE")){
               notesWindow.remove(c);
               break;
            }
        }
        this.updateComponent(notesWindow);
    }
    private void editNoteName(String name,JPanel pane){
        JPanel notesWindow;
        notesWindow = (JPanel)pane.getParent().getComponent(1);
        for(Component c: notesWindow.getComponents()){
            if(c.getName().equals(name+"NOTE")){
                String newName =this.stringPopUp("New note title: ");
                if(newName != null) {
                    ((JLabel) ((JPanel) c).getComponent(0)).setText(newName);
                }
                break;
            }
        }
    }
    private void editNoteColor(String name,JPanel pane){
        JPanel notesWindow;
        notesWindow = (JPanel)pane.getParent().getComponent(1);
        for(Component c: notesWindow.getComponents()){
            if(c.getName().equals(name+"NOTE")){
                    ColorPicker colorPicker = ColorPicker.createColorPicker(4);
                    JOptionPane.showConfirmDialog(window, colorPicker, "Chose color", JOptionPane.OK_CANCEL_OPTION);
                    Color color = colorPicker.getColor();
                    c.setBackground(color);
                break;
            }
        }
    }
    private Color editNote(String name){
        JPanel note = new JPanel();
        JLabel titleLabel = new JLabel("Title:");
        JTextField title = new JTextField();
        title.setText(name);
        JLabel noteLabel = new JLabel("Note:");
        JTextArea noteBody = new JTextArea();
        ColorPicker colorPicker = ColorPicker.createColorPicker(4);
        this.setUpSize(colorPicker,800,50,AgendaConstants.MINIMUM);
        String titleString;
        String noteString;
        Color color = Color.RED;

        this.setUpSize(noteBody,800,600,AgendaConstants.MINIMUM);
        note.setLayout(new BoxLayout(note,BoxLayout.Y_AXIS));
        note.add(titleLabel);
        note.add(title);
        note.add(noteLabel);
        note.add(noteBody);
        note.add(colorPicker);
        int okCxl = JOptionPane.showConfirmDialog(null, note, "Note", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if(okCxl != JOptionPane.CANCEL_OPTION){
            titleString = title.getText();
            noteString = noteBody.getText();
            color = colorPicker.getColor();
            System.out.println(titleString+"\n"+noteString+""+color.toString());
        }
        return color;
    }
    public NotesPane() {
        super();
        notePanels = new Vector<>();
        this.setUpNotebooks();
        notes = new JPanel(new CardLayout());
        window.add("notebooks",notebooks);
        window.add("notes",notes);
        this.add(window);
    }
}