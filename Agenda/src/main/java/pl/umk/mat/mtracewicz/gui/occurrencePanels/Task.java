package pl.umk.mat.mtracewicz.gui.occurrencePanels;

import pl.umk.mat.mtracewicz.entity.CompletedTodo;
import pl.umk.mat.mtracewicz.entity.Tasks;
import pl.umk.mat.mtracewicz.gui.AgendaPanel;
import pl.umk.mat.mtracewicz.gui.utilityPanels.ColorPicker;
import pl.umk.mat.mtracewicz.gui.utilityPanels.DatePicker;
import pl.umk.mat.mtracewicz.utility.ColorPriorityHandler;
import pl.umk.mat.mtracewicz.utility.DatabaseConnector;
import pl.umk.mat.mtracewicz.utility.DateHandler;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;

/**
 * Class which represents panel corresponding to task from database
 */
public class Task extends AgendaPanel {
    /**
     * Object which stores data about task
     */
    private Tasks tasks;
    /**
     * Checkbox representing if task had been completed
     */
    private JCheckBox checkBox;
    /**
     * Label which shows task name
     */
    private JLabel name;
    /**
     * Label showing task deadline
     */
    private JLabel deadline;
    /**
     * Connector responsible for connection with database
     */
    private DatabaseConnector databaseConnector;
    /**
     * Constructor which creates this panel based on provided task
     * @param taskInfo Task which will be represented by this panel
     * @param databaseConnector Connector which will communicate with database
     */
    public Task(Tasks taskInfo,DatabaseConnector databaseConnector){
        this.setLayout(new GridLayout(1,5));
        this.tasks = taskInfo;
        this.databaseConnector = databaseConnector;
        this.checkBox = new JCheckBox();
        this.checkBox.setHorizontalAlignment(SwingConstants.CENTER);
        this.checkBox.addActionListener(e -> {
            tasks.setStatus(1);
            this.deleteTask();
        });
        this.add(checkBox);
        this.name = new JLabel(taskInfo.getName());
        this.add(name);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YYYY");
        this.deadline = new JLabel(sdf.format(taskInfo.getDeadline()));
        this.add(deadline);
        JButton editButton = new JButton("Edit");
        editButton.addActionListener(e -> this.editTask());
        this.add(editButton);
        JButton deleteButton = new JButton("X");
        deleteButton.addActionListener(e -> this.deleteTask());
        this.setBackground(ColorPriorityHandler.getPriorityColor(taskInfo.getPriority()));
        this.add(deleteButton);
    }

    /**
     * Constructor which creates this panel based on provided
     * completed task
     * @param taskInfo Task which will be represented by this panel
     */
    public Task(CompletedTodo taskInfo){
        this.setLayout(new GridLayout(1,5));
        this.checkBox = new JCheckBox();
        this.checkBox.setEnabled(false);
        this.checkBox.setHorizontalAlignment(SwingConstants.CENTER);
        this.checkBox.addActionListener(e -> {
            tasks.setStatus(1);
            this.deleteTask();
        });
        this.add(checkBox);
        checkBox.setSelected(true);
        this.name = new JLabel(taskInfo.getName());
        this.add(name);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YYYY");
        this.deadline = new JLabel(sdf.format(taskInfo.getDeadline()));
        this.add(deadline);
        this.setBackground(ColorPriorityHandler.getPriorityColor(taskInfo.getPriority()));
    }

    /**
     * Method which modifies panel according to input provided by user
     * in popups and edits corresponding task in database
     */
    private void editTask(){
        String newName;
        String newDeadline;
        Color newPriority;

        newName = super.stringPopUp("Enter new task name");
        if(!newName.equals("")){
            tasks.setName(newName);
            this.name.setText(newName);
        }
        newDeadline = DatePicker.datePopUp();
        if(newDeadline != null){
            tasks.setDeadline(DateHandler.getDateFromString(newDeadline));
            this.deadline.setText(newDeadline);
        }
        newPriority = ColorPicker.colorPopUp(ColorPriorityHandler.getPriorityColor(tasks.getPriority()));
        if(newPriority != null){
            tasks.setPriority(ColorPriorityHandler.getPriority(newPriority));
            this.setBackground(newPriority);
        }
        if(!newName.equals("") || newDeadline != null || newPriority != null){
            this.databaseConnector.editTask(this.tasks);
            this.updateComponent(this.getParent());
        }
    }

    /**
     * Method which removes this panel from its parent
     * and deletes corresponding task from database
     */
    private void deleteTask(){
        Container parent = this.getParent();
        this.getParent().remove(this);
        new Thread(() -> this.databaseConnector.deleteTask(tasks)).start();
        this.updateComponent(parent);
    }

}
