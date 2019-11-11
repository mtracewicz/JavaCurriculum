package pl.umk.mat.mtracewicz.gui.occurrencePanels;

import pl.umk.mat.mtracewicz.entity.Items;
import pl.umk.mat.mtracewicz.gui.Agenda;
import pl.umk.mat.mtracewicz.gui.AgendaPanel;
import pl.umk.mat.mtracewicz.gui.contentPanels.NotesPane;
import pl.umk.mat.mtracewicz.gui.utilityPanels.ColorPicker;
import pl.umk.mat.mtracewicz.utility.AgendaConstants;

import javax.swing.*;
import java.awt.*;

/**
 * Class which represents panel which shows notebooks of user
 */
public class Notebook extends AgendaPanel {
    /**
     * Reference to Agenda object which will allow to get user and use connector
     */
    private Agenda agenda;
    /**
     *  Label showing name of notebook
     */
    private JLabel notebookName;
    /**
     *  Button allowing to access corresponding notebook
     */
    private JButton notebook;
    /**
     * Button which deletes notebook
     */
    private JButton deleteNotebook;
    /**
     * Button which shows new name popup for notebook
     */
    private JButton editNotebook;
    /**
     * Button which shows new color popup for notebook
     */
    private JButton editNotebookColorButton;
    /**
     * Represents list of notebook
     */
    private JPanel notebooks;
    /**
     *  Parent panel of notebook
     */
    private NotesPane parent;

    /**
     * Constructor of notebook used when creating new notebook
     * @param name name of notebook
     * @param agenda reference to main frame of program
     * @param parent reference to parent panel
     */
    public Notebook(String name, Agenda agenda,NotesPane parent){
        this.agenda = agenda;
        this.parent = parent;
        this.notebooks = parent.getNotebooks();
        this.notebookName = new JLabel(name);
        this.notebook = new JButton("Pick");
        this.deleteNotebook = new JButton("X");
        this.editNotebook = new JButton("Change name");
        this.editNotebookColorButton = new JButton("Change color");
        this.notebookName.setHorizontalAlignment(SwingUtilities.CENTER);
        this.setName(name);
        this.notebook.addActionListener( e -> parent.showNotebook(name));
        this.deleteNotebook.addActionListener( e -> deleteNotebook(name));
        this.editNotebook.addActionListener(e -> editNotebook(name));
        this.editNotebookColorButton.addActionListener(e -> editNotebookColor(name));
        this.setLayout(new GridLayout(1,5));
        this.setBackground(((ColorPicker)(parent.getFooter()).getComponent(3)).getColor());
        this.add(notebookName);
        this.add(notebook);
        this.add(editNotebook);
        this.add(editNotebookColorButton);
        this.add(deleteNotebook);
        this.setUpSize(this,1920,50, AgendaConstants.ALL);
    }

    /**
     * Constructor of notebook used when loading notebook from database
     * @param notebookData Data of notebook from database
     * @param agenda reference to main frame of program
     * @param parent reference to parent panel
     */
    public Notebook(Items notebookData, Agenda agenda,NotesPane parent){
        String name = notebookData.getName();
        this.parent = parent;
        this.notebooks = parent.getNotebooks();
        this.agenda = agenda;
        this.notebookName = new JLabel(name);
        this.notebook = new JButton("Pick");
        this.deleteNotebook = new JButton("X");
        this.editNotebook = new JButton("Change name");
        this.editNotebookColorButton = new JButton("Change color");
        this.notebookName.setHorizontalAlignment(SwingUtilities.CENTER);
        this.setName(name);
        this.notebook.addActionListener( e -> parent.showNotebook(name));
        this.deleteNotebook.addActionListener( e -> deleteNotebook(name));
        this.editNotebook.addActionListener(e -> editNotebook(name));
        this.editNotebookColorButton.addActionListener(e -> editNotebookColor(name));
        this.setLayout(new GridLayout(1,5));
        this.setBackground(ColorPicker.getColor(notebookData.getColor()));
        this.add(notebookName);
        this.add(notebook);
        this.add(editNotebook);
        this.add(editNotebookColorButton);
        this.add(deleteNotebook);
        this.setUpSize(this,1920,50, AgendaConstants.ALL);
    }

    /**
     * This method removes notebook with provided name
     * from notebooks list and deletes it from database
     * @param name Name of notebook to delete
     */
    private void deleteNotebook(String name){
        for(int i = 0;i < notebooks.getComponentCount();i++){
            if(notebooks.getComponent(i).getName().equals(name)){
                notebooks.remove(i);
                new Thread(() ->{
                    Items toDelete = agenda.getConnector().getItem(agenda.getUsername(),"notebook",name);
                    agenda.getConnector().deleteItem(toDelete);
                }).start();
                parent.setNumberOfNotebooks(parent.getNumberOfNotebooks()-1);
                if(parent.getNumberOfNotebooks() == 0){
                    notebooks.add(new JLabel("You currently have no notebooks. Please add some!"));
                }
                this.updateComponent(notebooks);
                break;
            }
        }
    }

    /**
     * Method which allows to change notebooks name,
     * then saves edited notebook to database
     * @param name name of notebook to edit
     */
    private void editNotebook(String name){
        for(int i = 0;i < notebooks.getComponentCount();i++){
            if(notebooks.getComponent(i).getName().equals(name)){
                JPanel panel = (JPanel)notebooks.getComponent(i);
                JLabel button = (JLabel)panel.getComponent(0);
                String newName = this.stringPopUp("New notebook name: ");
                Items toEdit = agenda.getConnector().getItem(agenda.getUsername(),"notebook",name);
                if(!newName.isEmpty()) {
                    button.setText(newName);
                    new Thread(() -> {
                        toEdit.setName(newName);
                        agenda.getConnector().editItem(toEdit);
                    }).start();
                    new Thread(() -> parent.setUpNotes(toEdit)).start();
                }
                this.updateComponent(notebooks);
            }
        }
    }

    /**
     * Method which allows to change notebooks color,
     * then saves edited notebook to database
     * @param name name of notebook to edit
     */
    private void editNotebookColor(String name) {
        for (int i = 0; i < notebooks.getComponentCount(); i++) {
            if (notebooks.getComponent(i).getName().equals(name)) {
                JPanel panel = (JPanel) notebooks.getComponent(i);
                ColorPicker colorPicker = ColorPicker.createColorPicker(4);
                int okCXL = JOptionPane.showConfirmDialog(null, colorPicker, "Chose color", JOptionPane.OK_CANCEL_OPTION);
                if(okCXL == JOptionPane.OK_OPTION) {
                    Color newColor = colorPicker.getColor();
                    panel.setBackground(newColor);
                    new Thread(() -> {
                        Items toEdit = agenda.getConnector().getItem(agenda.getUsername(), "notebook", name);
                        toEdit.setColor(ColorPicker.getColorName(newColor));
                        agenda.getConnector().editItem(toEdit);
                    }).start();
                    this.updateComponent(notebooks);
                }
            }
        }
    }
}
